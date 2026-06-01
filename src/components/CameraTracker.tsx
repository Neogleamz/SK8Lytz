import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';
import React, { useCallback, useEffect, useRef, useState } from 'react';
import { AppState, Linking, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { Camera, useCameraDevice, useCameraPermission, useFrameOutput, Frame } from 'react-native-vision-camera';
import { runOnJS } from 'react-native-worklets';
import { useResizer } from 'react-native-vision-camera-resizer';
import { requestPermission } from '../services/PermissionService';
import { Colors, Spacing } from '../theme/theme';
import { rgbToHex } from '../utils/ColorUtils';
import { extractKMeansPalette, RGB } from '../utils/kMeansPalette';

export interface CameraTrackerProps {
  onColorDetected: (hex: string) => void;
  onVibePaletteDetected?: (colors: RGB[]) => void;
  subMode: 'SNIPER' | 'VIBE';
  isActive: boolean;
  liveColorRef?: React.MutableRefObject<string>;
}


export default function CameraTracker({
  onColorDetected: _onColorDetected,
  onVibePaletteDetected,
  subMode,
  isActive,
  liveColorRef,
}: CameraTrackerProps) {
  const device = useCameraDevice('back');
  const { hasPermission, requestPermission: requestFromHook } = useCameraPermission();
  const [liveHex, setLiveHex] = useState<string>('#FFFFFF');

  const onVibePaletteDetectedRef = useRef(onVibePaletteDetected);

  useEffect(() => {
    onVibePaletteDetectedRef.current = onVibePaletteDetected;
  }, [onVibePaletteDetected]);

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
    const hex = rgbToHex(r, g, b);
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
  const { resizer, error } = useResizer({
    width: 50,
    height: 50,
    channelOrder: 'rgb',
    dataType: 'uint8',
    scaleMode: 'cover',
    pixelLayout: 'interleaved',
  });

  useEffect(() => {
    if (resizer) {
      console.log('Camera Sniper: GPU Resizer loaded successfully!');
    } else if (error) {
      console.error('Camera Sniper: GPU Resizer failed to load:', error);
    }
  }, [resizer, error]);

  // Create worklet references to callbacks
  // Use runOnJS from react-native-worklets
  const runOnJSSniper = React.useMemo(() => runOnJS(dispatchSniperColor), [dispatchSniperColor]);
  const runOnJSVibe = React.useMemo(() => runOnJS(dispatchVibePalette), [dispatchVibePalette]);

  // Memoize the frame processor onFrame callback to persist throttled timestamp in closure
  // and handle JSI worklet scopes without stale React Ref read failures.
  const onFrame = React.useMemo(() => {
    let lastProcessed = 0;
    return (frame: Frame) => {
      'worklet';

      try {
        const now = Date.now();
        // 200ms interval = 5Hz execution cap
        if (now - lastProcessed < 200) {
          return;
        }
        lastProcessed = now;

        if (resizer != null) {
          const resized = resizer.resize(frame);
          try {
            const buffer = resized.getPixelBuffer();
            const resizedArray = new Uint8Array(buffer);
            
            // Dynamically detect bytes per pixel to handle both RGB (3) and RGBA/BGRA (4) layouts
            // Enforce a minimum of 3 to completely eliminate infinite loop hazards on zero-length buffers
            const channels = Math.max(3, Math.floor(resizedArray.length / 2500));

            if (resizedArray.length >= 7500) {
              if (subMode === 'SNIPER') {
                // 1. SNIPER mode: sampling center pixel (25x25 on a 50x50 grid)
                const centerIdx = (25 * 50 + 25) * channels;
                const r = resizedArray[centerIdx];
                const g = resizedArray[centerIdx + 1];
                const b = resizedArray[centerIdx + 2];

                runOnJSSniper(r, g, b);
              } else {
                // 2. VIBE mode: K-Means palette extraction (k=3)
                const pixels: RGB[] = [];
                for (let i = 0; i < resizedArray.length; i += channels) {
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
          } finally {
            resized.dispose(); // CRITICAL: Dispose GPUFrame immediately to prevent leaks
          }
        }
      } catch (err) {
        console.error('Camera Frame Processor Error:', err);
      } finally {
        frame.dispose(); // CRITICAL: Dispose Frame immediately to prevent stalls
      }
    };
  }, [resizer, subMode, runOnJSSniper, runOnJSVibe]);

  const frameOutput = useFrameOutput({
    pixelFormat: 'yuv',
    onFrame,
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
    borderRadius: 16,
    marginHorizontal: Spacing.md,
    marginTop: Spacing.md,
    marginBottom: Spacing.sm,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
    overflow: 'hidden',
    elevation: 8,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 8,
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
