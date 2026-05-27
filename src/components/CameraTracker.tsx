import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';
import React, { useCallback, useEffect, useRef, useState } from 'react';
import { AppState, Linking, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { Camera, useCameraDevice, useCameraPermission, useFrameOutput, Frame } from 'react-native-vision-camera';
import { runOnJS } from 'react-native-worklets';
import { useResizer } from 'react-native-vision-camera-resizer';
import { requestPermission } from '../services/PermissionService';
import { Colors, Spacing } from '../theme/theme';
import { extractKMeansPalette, RGB } from '../utils/kMeansPalette';

export interface CameraTrackerProps {
  onColorDetected: (hex: string) => void;
  onVibePaletteDetected?: (colors: RGB[]) => void;
  subMode: 'SNIPER' | 'VIBE';
  isActive: boolean;
  liveColorRef?: React.MutableRefObject<string>;
}

// ---------------------------------------------------------------------------
// Extract vivid hue hex from raw RGB byte values (0–255)
// ---------------------------------------------------------------------------
export function rgbToVividHex(r: number, g: number, b: number): string {
  const rN = r / 255;
  const gN = g / 255;
  const bN = b / 255;
  const cMax = Math.max(rN, gN, bN);
  const cMin = Math.min(rN, gN, bN);
  const delta = cMax - cMin;

  // NEUTRAL SNAPPING GATE: If delta < 0.15, snap to White to avoid noise jumping to Blue/Red
  if (delta < 0.15) {
    return '#FFFFFF';
  }

  let h = 0;
  if (cMax === rN)      h = ((gN - bN) / delta) % 6;
  else if (cMax === gN) h = (bN - rN) / delta + 2;
  else                  h = (rN - gN) / delta + 4;
  h = h * 60;
  if (h < 0) h += 360;

  // Boost to vivid neon (S=1.0, L=0.5)
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

export default function CameraTracker({
  onColorDetected,
  onVibePaletteDetected,
  subMode,
  isActive,
  liveColorRef,
}: CameraTrackerProps) {
  const device = useCameraDevice('back');
  const { hasPermission, requestPermission: requestFromHook } = useCameraPermission();
  const [liveHex, setLiveHex] = useState<string>('#FFFFFF');

  const onColorDetectedRef = useRef(onColorDetected);
  const onVibePaletteDetectedRef = useRef(onVibePaletteDetected);
  const subModeRef = useRef(subMode);

  useEffect(() => {
    onColorDetectedRef.current = onColorDetected;
  }, [onColorDetected]);

  useEffect(() => {
    onVibePaletteDetectedRef.current = onVibePaletteDetected;
  }, [onVibePaletteDetected]);

  useEffect(() => {
    subModeRef.current = subMode;
  }, [subMode]);

  // Synchronize camera permission state on AppState change
  useEffect(() => {
    const sub = AppState.addEventListener('change', (nextState) => {
      if (nextState === 'active') requestFromHook();
    });
    return () => sub.remove();
  }, [requestFromHook]);

  // Automatically request camera permissions if not granted
  useEffect(() => {
    if (!hasPermission) {
      requestPermission('CAMERA').then((granted) => {
        if (granted) requestFromHook();
      });
    }
  }, [hasPermission, requestFromHook]);

  // The JS dispatch functions that receive the worklet threads values
  const dispatchSniperColor = useCallback((r: number, g: number, b: number) => {
    if (isNaN(r) || isNaN(g) || isNaN(b)) return;
    const hex = rgbToVividHex(r, g, b);
    setLiveHex(hex);
    if (liveColorRef) {
      liveColorRef.current = hex;
    }
  }, [liveColorRef]);

  const dispatchVibePalette = useCallback((colors: RGB[]) => {
    if (onVibePaletteDetectedRef.current) {
      onVibePaletteDetectedRef.current(colors);
    }
  }, []);

  // Frame processor GPU resizer configuration
  const { resizer } = useResizer({
    width: 50,
    height: 50,
    channelOrder: 'rgb',
    dataType: 'uint8',
    scaleMode: 'cover',
    pixelLayout: 'interleaved',
  });

  // Create worklet references to callbacks
  // Use runOnJS from react-native-worklets
  const runOnJSSniper = runOnJS(dispatchSniperColor);
  const runOnJSVibe = runOnJS(dispatchVibePalette);

  // Hard-throttled Frame Processor logic running at 5Hz (every 200ms)
  const lastProcessedRef = useRef<number>(0);

  const frameOutput = useFrameOutput({
    pixelFormat: 'yuv',
    onFrame: (frame: Frame) => {
      'worklet';

      try {
        const now = performance.now();
        // 200ms interval = 5Hz execution cap
        if (now - lastProcessedRef.current < 200) {
          return;
        }
        lastProcessedRef.current = now;

        if (resizer != null) {
          const resized = resizer.resize(frame);
          const buffer = resized.getPixelBuffer();
          const resizedArray = new Uint8Array(buffer);

          if (resizedArray.length >= 7500) {
            const currentSubMode = subModeRef.current;

            if (currentSubMode === 'SNIPER') {
              // 1. SNIPER mode: sampling center pixel
              const centerIdx = (25 * 50 + 25) * 3;
              const r = resizedArray[centerIdx];
              const g = resizedArray[centerIdx + 1];
              const b = resizedArray[centerIdx + 2];

              runOnJSSniper(r, g, b);
            } else {
              // 2. VIBE mode: K-Means palette extraction (k=3)
              const pixels: RGB[] = [];
              for (let i = 0; i < 7500; i += 3) {
                pixels.push({
                  r: resizedArray[i],
                  g: resizedArray[i + 1],
                  b: resizedArray[i + 2],
                });
              }

              const palette = extractKMeansPalette(pixels, 3, 5);
              runOnJSVibe(palette);
            }
          }
          resized.dispose(); // CRITICAL: Dispose GPUFrame immediately to prevent leaks
        }
      } finally {
        frame.dispose(); // CRITICAL: Dispose Frame immediately to prevent stalls
      }
    },
  });

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

  if (device == null) {
    return (
      <View style={styles.centeredContainer}>
        <Text style={[styles.message, { marginTop: Spacing.md }]}>Initializing Camera...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Camera
        style={StyleSheet.absoluteFillObject}
        device={device}
        isActive={isActive && hasPermission}
        outputs={[frameOutput]}
      />
      {subMode === 'SNIPER' && (
        <View style={styles.reticleContainer} pointerEvents="none">
          <View style={[styles.reticleRing, { borderColor: liveHex }]}>
            <View style={styles.reticleCrosshairH} />
            <View style={styles.reticleCrosshairV} />
          </View>
        </View>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#000',
    overflow: 'hidden',
  },
  centeredContainer: {
    flex: 1,
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
    elevation: 8,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.6,
    shadowRadius: 8,
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
