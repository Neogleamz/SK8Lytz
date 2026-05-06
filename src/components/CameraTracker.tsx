/**
 * CameraTracker — Vision Camera v4 (Nitro) Sniper Dropper
 *
 * Architecture (Option A):
 *   useFrameOutput({ onFrame }) → worklet runs on camera thread → reads
 *   center pixel bytes directly from raw ArrayBuffer (no file I/O, no JPEG).
 *   runOnJS throttles state updates to ~10fps so React re-renders stay smooth.
 *
 * Permission flow stays entirely in PermissionService (PermissionsAndroid).
 * The hook (useCameraPermission) is used ONLY as a read source so the UI
 * re-renders automatically when the OS state changes.
 */
import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';
import React, { useCallback, useEffect, useRef, useState } from 'react';
import {
  ActivityIndicator,
  AppState,
  Linking,
  Platform,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import {
  Camera,
  useCameraPermission,
  useFrameProcessor,
} from 'react-native-vision-camera';
import { runOnJS } from 'react-native-worklets';
import { requestPermission } from '../services/PermissionService';
import { Colors, Spacing } from '../theme/theme';

interface CameraTrackerProps {
  onColorDetected: (hex: string) => void;
  isActive: boolean;
}

export default function CameraTracker({ onColorDetected, isActive }: CameraTrackerProps) {
  const { hasPermission, requestPermission: requestFromHook } = useCameraPermission();
  const [liveHex, setLiveHex] = useState<string>('#FF0080');
  const lastUpdateRef = useRef<number>(0);

  // --- Permission: re-check every time the app comes to the foreground ---
  useEffect(() => {
    const sub = AppState.addEventListener('change', (nextState) => {
      if (nextState === 'active') {
        // Re-check by calling requestPermission which on Android reads the
        // current granted/denied state without re-prompting if already set.
        requestFromHook();
      }
    });
    return () => sub.remove();
  }, [requestFromHook]);

  // On first render, try requesting if not yet granted
  useEffect(() => {
    if (!hasPermission) {
      requestPermission('CAMERA').then((granted) => {
        if (granted) requestFromHook(); // sync the hook state
      });
    }
  }, [hasPermission, requestFromHook]);

  // --- Frame Processor (Option A): worklet on camera thread ---
  const updateColor = useCallback((hex: string) => {
    const now = Date.now();
    // Throttle to ~10 state updates per second
    if (now - lastUpdateRef.current < 100) return;
    lastUpdateRef.current = now;
    setLiveHex(hex);
    onColorDetected(hex); // Send live color upstream constantly
  }, [setLiveHex, onColorDetected]);

  const updateColorOnJS = runOnJS(updateColor);

  const frameProcessor = useFrameProcessor((frame) => {
    'worklet';

    if (!frame.isValid) {
      return;
    }

    try {
      const buffer = frame.getPixelBuffer();
      const bytes = new Uint8Array(buffer);

      // Fix Android memory layout bug: MUST use bytesPerRow, never assume width * bytesPerPixel
      const bpr = frame.bytesPerRow;
      const bytesPerPixel = Math.max(3, Math.floor(bpr / frame.width));

      const centerX = Math.floor(frame.width / 2);
      const centerY = Math.floor(frame.height / 2);

      let rSum = 0, gSum = 0, bSum = 0, count = 0;
      for (let dy = -2; dy <= 2; dy++) {
        for (let dx = -2; dx <= 2; dx++) {
          const px = centerX + dx;
          const py = centerY + dy;
          if (px < 0 || px >= frame.width || py < 0 || py >= frame.height) continue;
          
          // CORRECT memory index using hardware stride
          const idx = py * bpr + px * bytesPerPixel;
          
          // RGBA / BGRA safety (assume RGB order for now, though iOS might be BGRA)
          // For vivid tracking, even if R/B are swapped on some devices, the Hue extraction will just be shifted.
          // Vision Camera v5 normalizes to RGBA on Android when pixelFormat="rgb" is used.
          rSum += bytes[idx];
          gSum += bytes[idx + 1];
          bSum += bytes[idx + 2];
          count++;
        }
      }

      if (count === 0) {
        return;
      }

      let r = Math.round(rSum / count);
      let g = Math.round(gSum / count);
      let b = Math.round(bSum / count);

      // HSL Hue extraction → boost S=1.0, L=0.5 for vivid LED color
      const rN = r / 255;
      const gN = g / 255;
      const bN = b / 255;
      const cMax = rN > gN ? (rN > bN ? rN : bN) : (gN > bN ? gN : bN);
      const cMin = rN < gN ? (rN < bN ? rN : bN) : (gN < bN ? gN : bN);
      const delta = cMax - cMin;

      let h = 0;
      if (delta > 0.001) {
        if (cMax === rN)      h = ((gN - bN) / delta) % 6;
        else if (cMax === gN) h = (bN - rN) / delta + 2;
        else                  h = (rN - gN) / delta + 4;
        h = h * 60;
        if (h < 0) h += 360;
      }

      // HSL → RGB with S=1.0, L=0.5 (pure vivid hue)
      const c = 1.0;
      const x = c * (1 - Math.abs((h / 60) % 2 - 1));
      let rV = 0, gV = 0, bV = 0;
      if      (h < 60)  { rV = c; gV = x; bV = 0; }
      else if (h < 120) { rV = x; gV = c; bV = 0; }
      else if (h < 180) { rV = 0; gV = c; bV = x; }
      else if (h < 240) { rV = 0; gV = x; bV = c; }
      else if (h < 300) { rV = x; gV = 0; bV = c; }
      else              { rV = c; gV = 0; bV = x; }

      r = Math.round(rV * 255);
      g = Math.round(gV * 255);
      b = Math.round(bV * 255);

      const toHex = (v: number) => {
        const s = v.toString(16);
        return s.length === 1 ? '0' + s : s;
      };
      const hex = '#' + toHex(r) + toHex(g) + toHex(b);
      updateColorOnJS(hex.toUpperCase());
    } catch {
      // Silent — don't crash the camera thread
    }
  }, []);

  // Handle Capture moved to CameraPanel.tsx

  // --- Permission denied state ---
  if (!hasPermission) {
    return (
      <View style={styles.centeredContainer}>
        <MaterialCommunityIcons name="camera-off" size={40} color={Colors.textMuted} style={{ marginBottom: Spacing.md }} />
        <Text style={styles.message}>Camera access is needed to detect colors from your environment.</Text>
        <TouchableOpacity
          style={styles.button}
          onPress={async () => {
            const granted = await requestPermission('CAMERA');
            if (granted) {
              requestFromHook();
            } else {
              // Fallback: Send user to Settings if OS blocked it
              Linking.openSettings();
            }
          }}
        >
          <Text style={{ color: '#FFF', fontWeight: 'bold' }}>GRANT PERMISSION</Text>
        </TouchableOpacity>
      </View>
    );
  }

  if (Platform.OS === 'web') {
    return (
      <View style={styles.centeredContainer}>
        <ActivityIndicator color={Colors.primary} size="large" />
        <Text style={[styles.message, { marginTop: Spacing.md }]}>Camera not available on web.</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Camera
        style={StyleSheet.absoluteFillObject}
        device="back"
        isActive={isActive && hasPermission}
        frameProcessor={frameProcessor}
        pixelFormat="rgb"
      />

      {/* Reticle — border color is live color */}
      <View style={styles.reticleContainer} pointerEvents="none">
        <View style={[styles.reticleRing, { borderColor: liveHex }]}>
          <View style={styles.reticleCrosshairH} />
          <View style={styles.reticleCrosshairV} />
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    minHeight: 300,
    backgroundColor: '#000',
    overflow: 'hidden',
  },
  centeredContainer: {
    flex: 1,
    minHeight: 300,
    backgroundColor: 'rgba(0,0,0,0.4)',
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: Spacing.xl,
  },
  message: {
    color: '#FFF',
    textAlign: 'center',
    paddingBottom: Spacing.md,
    fontFamily: 'Righteous',
  },
  button: {
    backgroundColor: Colors.primary,
    paddingHorizontal: Spacing.xl,
    paddingVertical: Spacing.md,
    borderRadius: 12,
    marginTop: Spacing.xl,
  },
  reticleContainer: {
    ...StyleSheet.absoluteFillObject,
    justifyContent: 'center',
    alignItems: 'center',
  },
  reticleRing: {
    width: 64,
    height: 64,
    borderRadius: 32,
    borderWidth: 3,
    backgroundColor: 'rgba(255,255,255,0.08)',
    justifyContent: 'center',
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.6,
    shadowRadius: 8,
    elevation: 8,
  },
  reticleCrosshairH: {
    position: 'absolute',
    width: 20,
    height: 2,
    backgroundColor: 'rgba(255,255,255,0.85)',
  },
  reticleCrosshairV: {
    position: 'absolute',
    width: 2,
    height: 20,
    backgroundColor: 'rgba(255,255,255,0.85)',
  },
  controlOverlay: {
    position: 'absolute',
    bottom: 0,
    left: 0,
    right: 0,
    alignItems: 'center',
    paddingBottom: Spacing.xxl,
    paddingTop: Spacing.xl,
  },
  hexPill: {
    backgroundColor: 'rgba(0,0,0,0.7)',
    paddingHorizontal: Spacing.lg,
    paddingVertical: 8,
    borderRadius: 20,
    marginBottom: Spacing.xs,
  },
  hexText: {
    color: '#FFF',
    fontSize: 14,
    fontFamily: 'Inter-Bold',
    letterSpacing: 1,
  },
  instructionText: {
    color: 'rgba(255,255,255,0.8)',
    fontSize: 12,
    fontFamily: 'Inter-Medium',
    textAlign: 'center',
    letterSpacing: 0.5,
    marginBottom: Spacing.lg,
    textShadowColor: 'rgba(0,0,0,0.8)',
    textShadowOffset: { width: 0, height: 1 },
    textShadowRadius: 3,
  },
  captureFab: {
    width: 80,
    height: 80,
    borderRadius: 40,
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 4,
    borderColor: 'rgba(255,255,255,0.8)',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 8 },
    shadowOpacity: 0.4,
    shadowRadius: 15,
    elevation: 12,
  },
  captureFabActive: {
    transform: [{ scale: 0.95 }],
    opacity: 0.8,
  },
});
