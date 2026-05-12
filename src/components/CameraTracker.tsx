/**
 * CameraTracker — Silent snapshot-based ambient color sampler
 *
 * Architecture:
 *   Every 600ms → cameraRef.current.takeSnapshot() (NO shutter sound)
 *   → nitro Image.resize(1,1) → toRawPixelData() → BGRA/RGBA buffer
 *   → extract center pixel → HSL hue → vivid LED hex → onColorDetected
 *
 * No frame processors. No worklets. No YUV parsing. Pure JS thread.
 * Uses react-native-nitro-image's Image API — included with vision-camera v5.
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
  type CameraRef,
  useCameraDevice,
  useCameraPermission,
} from 'react-native-vision-camera';
import { requestPermission } from '../services/PermissionService';
import { Colors, Spacing } from '../theme/theme';

interface CameraTrackerProps {
  onColorDetected: (hex: string) => void;
  isActive: boolean;
}

// ---------------------------------------------------------------------------
// Extract vivid hue hex from raw RGB values
// ---------------------------------------------------------------------------
function rgbToVividHex(r: number, g: number, b: number): string {
  const rN = r / 255;
  const gN = g / 255;
  const bN = b / 255;
  const cMax = Math.max(rN, gN, bN);
  const cMin = Math.min(rN, gN, bN);
  const delta = cMax - cMin;

  let h = 0;
  if (delta > 0.001) {
    if (cMax === rN)      h = ((gN - bN) / delta) % 6;
    else if (cMax === gN) h = (bN - rN) / delta + 2;
    else                  h = (rN - gN) / delta + 4;
    h = h * 60;
    if (h < 0) h += 360;
  }

  // Convert hue → vivid RGB (S=1.0, L=0.5)
  const c = 1.0;
  const x = c * (1 - Math.abs((h / 60) % 2 - 1));
  let rV = 0, gV = 0, bV = 0;
  if      (h < 60)  { rV = c; gV = x; bV = 0; }
  else if (h < 120) { rV = x; gV = c; bV = 0; }
  else if (h < 180) { rV = 0; gV = c; bV = x; }
  else if (h < 240) { rV = 0; gV = x; bV = c; }
  else if (h < 300) { rV = x; gV = 0; bV = c; }
  else              { rV = c; gV = 0; bV = x; }

  const toHex = (v: number) => {
    const s = Math.round(v * 255).toString(16);
    return s.length === 1 ? '0' + s : s;
  };
  return ('#' + toHex(rV) + toHex(gV) + toHex(bV)).toUpperCase();
}

export default function CameraTracker({ onColorDetected, isActive }: CameraTrackerProps) {
  const device = useCameraDevice('back');
  const { hasPermission, requestPermission: requestFromHook } = useCameraPermission();
  const [liveHex, setLiveHex] = useState<string>('#FF0080');

  const cameraRef = useRef<CameraRef>(null);
  const intervalRef = useRef<ReturnType<typeof setInterval> | null>(null);
  const isSamplingRef = useRef(false);

  // --- Permission management ---
  useEffect(() => {
    const sub = AppState.addEventListener('change', (nextState) => {
      if (nextState === 'active') requestFromHook();
    });
    return () => sub.remove();
  }, [requestFromHook]);

  useEffect(() => {
    if (!hasPermission) {
      requestPermission('CAMERA').then((granted) => {
        if (granted) requestFromHook();
      });
    }
  }, [hasPermission, requestFromHook]);

  // --- Core silent sampler ---
  const sampleColor = useCallback(async () => {
    if (!cameraRef.current || isSamplingRef.current) return;
    isSamplingRef.current = true;

    try {
      // takeSnapshot() reads from the preview buffer — NO shutter sound, NO disk I/O
      const image = await cameraRef.current.takeSnapshot();

      // Resize to 1×1 — averages all pixels to a single color. Pure native, fast.
      const tiny = image.resize(1, 1);

      // Get raw pixel bytes + format (BGRA on Android, RGBA on iOS typically)
      const { buffer, pixelFormat } = tiny.toRawPixelData();
      const bytes = new Uint8Array(buffer);

      let r = 0, g = 0, b = 0;
      switch (pixelFormat) {
        case 'BGRA':
        case 'BGR':
        case 'BGRX':
          b = bytes[0]; g = bytes[1]; r = bytes[2];
          break;
        case 'ABGR':
          b = bytes[1]; g = bytes[2]; r = bytes[3];
          break;
        case 'RGBA':
        case 'RGB':
        case 'RGBX':
          r = bytes[0]; g = bytes[1]; b = bytes[2];
          break;
        case 'ARGB':
        case 'XRGB':
          r = bytes[1]; g = bytes[2]; b = bytes[3];
          break;
        default:
          // Unknown format — try BGRA (most common on Android ARM64)
          b = bytes[0]; g = bytes[1]; r = bytes[2];
      }

      if (isNaN(r) || isNaN(g) || isNaN(b)) return;

      const hex = rgbToVividHex(r, g, b);
      setLiveHex(hex);
      onColorDetected(hex);
    } catch {
      // Silently skip — camera may still be warming up
    } finally {
      isSamplingRef.current = false;
    }
  }, [onColorDetected]);

  // --- Start/stop loop ---
  useEffect(() => {
    if (isActive && hasPermission && device) {
      // Brief warmup delay before first snapshot
      const startTimer = setTimeout(() => {
        intervalRef.current = setInterval(sampleColor, 600);
      }, 900);

      return () => {
        clearTimeout(startTimer);
        if (intervalRef.current) {
          clearInterval(intervalRef.current);
          intervalRef.current = null;
        }
      };
    } else {
      if (intervalRef.current) {
        clearInterval(intervalRef.current);
        intervalRef.current = null;
      }
    }
  }, [isActive, hasPermission, device, sampleColor]);

  // --- Guard states ---
  if (!hasPermission) {
    return (
      <View style={styles.centeredContainer}>
        <MaterialCommunityIcons name="camera-off" size={40} color={Colors.textMuted} style={{ marginBottom: Spacing.md }} />
        <Text style={styles.message}>Camera access is needed to detect colors from your environment.</Text>
        <TouchableOpacity
          style={styles.button}
          onPress={async () => {
            const granted = await requestPermission('CAMERA');
            if (granted) requestFromHook();
            else Linking.openSettings();
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

  if (device == null) {
    return (
      <View style={styles.centeredContainer}>
        <ActivityIndicator color={Colors.primary} size="large" />
        <Text style={[styles.message, { marginTop: Spacing.md }]}>Initializing Camera...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Camera
        ref={cameraRef}
        style={StyleSheet.absoluteFillObject}
        device={device}
        isActive={isActive && hasPermission}
      />

      {/* Reticle — border color tracks detected ambient color */}
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
});
