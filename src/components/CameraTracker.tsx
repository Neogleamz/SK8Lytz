/**
 * CameraTracker — Platform-Split Ambient Color Sampler
 *
 * FIX HISTORY (why previous attempts failed):
 *   1. takeSnapshot() is @platform Android ONLY (PreviewView.nitro.ts L113)
 *      → iOS always threw, bare catch{} swallowed it, reticle froze at #FF0080
 *   2. Default Camera implementationMode='performance' uses SurfaceView on Android
 *      which does NOT support takeSnapshot. Requires 'compatible' (TextureView).
 *   3. All errors were silently eaten — debugging was impossible
 *
 * CORRECT ARCHITECTURE (Verified from VC5 source, 2026-05-26):
 *
 *   Android:
 *     <Camera implementationMode="compatible"> (TextureView — required for takeSnapshot)
 *     cameraRef.current.takeSnapshot()        → Promise<NitroImage>
 *     image.resize(1,1).toRawPixelData()      → { buffer: ArrayBuffer, pixelFormat }
 *     extract R/G/B bytes → rgbToVividHex()   → onColorDetected(hex)
 *
 *   iOS:
 *     useFrameOutput({ pixelFormat: 'rgb', onFrame: worklet })  ← CameraFrameOutput
 *     frame.getPixelBuffer()  → ArrayBuffer[0]=R, [1]=G, [2]=B  ← non-planar RGB
 *     runOnJS(dispatchColor)(r, g, b)   → onColorDetected(hex)  ← back to React
 *
 * Sources:
 *   PreviewView.nitro.ts L113    — takeSnapshot() @platform Android
 *   Image.nitro.ts L93           — toRawPixelData() sync API
 *   Frame.nitro.ts L280          — getPixelBuffer() for non-planar RGB frames
 *   useFrameOutput.ts L121       — iOS worklet hook
 *   react-native-worklets L14    — runOnJS export
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
  useFrameOutput,
} from 'react-native-vision-camera';
import { runOnJS } from 'react-native-worklets';
import { requestPermission } from '../services/PermissionService';
import { Colors, Spacing } from '../theme/theme';

interface CameraTrackerProps {
  onColorDetected: (hex: string) => void;
  isActive: boolean;
}

// ---------------------------------------------------------------------------
// Extract vivid hue hex from raw RGB byte values (0–255)
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

  // Boost to vivid neon (S=1.0, L=0.5) — skate LEDs are pure hue, not ambient
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

  // Android-only refs — not used on iOS
  const cameraRef = useRef<CameraRef>(null);
  const intervalRef = useRef<ReturnType<typeof setInterval> | null>(null);
  const isSamplingRef = useRef(false);

  // Stable JS callback — safe to call from worklet via runOnJS
  const dispatchColor = useCallback((r: number, g: number, b: number) => {
    if (isNaN(r) || isNaN(g) || isNaN(b)) return;
    const hex = rgbToVividHex(r, g, b);
    setLiveHex(hex);
    onColorDetected(hex);
  }, [onColorDetected]);

  // ── iOS PATH: useFrameOutput worklet ──────────────────────────────────────
  // Verified source: useFrameOutput.ts L121, Frame.nitro.ts L280
  // onFrame MUST be a worklet — runs on camera native thread, not JS thread.
  // pixelFormat='rgb' → non-planar → getPixelBuffer()[0..2] = R, G, B
  // ─────────────────────────────────────────────────────────────────────────
  const dispatchColorJS = runOnJS(dispatchColor);

  const frameOutput = useFrameOutput({
    // Only wire onFrame on iOS — on Android we use takeSnapshot instead
    onFrame: Platform.OS === 'ios' ? (frame) => {
      'worklet';
      try {
        if (!frame.isValid) { frame.dispose(); return; }
        // Non-planar RGB: bytes[0]=R, bytes[1]=G, bytes[2]=B
        const buf = frame.getPixelBuffer();
        const bytes = new Uint8Array(buf);
        dispatchColorJS(bytes[0] ?? 0, bytes[1] ?? 0, bytes[2] ?? 0);
      } finally {
        frame.dispose();
      }
    } : undefined,
    // 1×1 resolution forces the camera pipeline to downsample to a single pixel
    // (color average of the entire frame) — minimum CPU + BLE noise
    targetResolution: { width: 1, height: 1 },
    pixelFormat: 'rgb',    // Non-planar RGB → getPixelBuffer() is safe
    dropFramesWhileBusy: true,
  });

  // ── Permission management ─────────────────────────────────────────────────
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

  // ── Android PATH: takeSnapshot() polling loop ─────────────────────────────
  // Verified source: PreviewView.nitro.ts L113 (@platform Android)
  // REQUIRES implementationMode="compatible" (TextureView) on <Camera> below.
  // SurfaceView (default 'performance') does NOT support takeSnapshot().
  // ─────────────────────────────────────────────────────────────────────────
  const sampleColor = useCallback(async () => {
    if (!cameraRef.current || isSamplingRef.current) return;
    isSamplingRef.current = true;

    try {
      // takeSnapshot() reads from the TextureView preview buffer
      // — NO shutter sound, NO disk I/O, NO JPEG encoding
      const image = await cameraRef.current.takeSnapshot();

      // Synchronous resize to 1×1 → pixel average of full frame
      const tiny = image.resize(1, 1);

      // Synchronous raw pixel access — BGRA on ARM64 Android (little-endian)
      const { buffer, pixelFormat } = tiny.toRawPixelData();
      const bytes = new Uint8Array(buffer);

      let r = 0, g = 0, b = 0;
      switch (pixelFormat) {
        case 'BGRA':
        case 'BGR':
        case 'BGRX':
          b = bytes[0] ?? 0; g = bytes[1] ?? 0; r = bytes[2] ?? 0;
          break;
        case 'ABGR':
          b = bytes[1] ?? 0; g = bytes[2] ?? 0; r = bytes[3] ?? 0;
          break;
        case 'RGBA':
        case 'RGB':
        case 'RGBX':
          r = bytes[0] ?? 0; g = bytes[1] ?? 0; b = bytes[2] ?? 0;
          break;
        case 'ARGB':
        case 'XRGB':
          r = bytes[1] ?? 0; g = bytes[2] ?? 0; b = bytes[3] ?? 0;
          break;
        default:
          // Fallback: ARM64 Android emits BGRA from Bitmap.ARGB_8888 little-endian
          b = bytes[0] ?? 0; g = bytes[1] ?? 0; r = bytes[2] ?? 0;
      }

      dispatchColor(r, g, b);
    } catch (e) {
      // DEV-only: surface real errors so we stop flying blind
      if (__DEV__) console.error('[CameraTracker] takeSnapshot failed:', e);
    } finally {
      isSamplingRef.current = false;
    }
  }, [dispatchColor]);

  // Start/stop Android poll loop — only runs on Android
  useEffect(() => {
    if (Platform.OS !== 'android') return;
    if (isActive && hasPermission && device) {
      // 900ms warmup: TextureView needs one render cycle before snapshot works
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

  // ── Guard states ──────────────────────────────────────────────────────────
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
        // CRITICAL FIX: 'compatible' uses TextureView on Android.
        // takeSnapshot() REQUIRES TextureView — SurfaceView ('performance') does not
        // support snapshots and will throw "Camera Preview doesn't support snapshots".
        // Source: PreviewView.nitro.ts implementationMode docs.
        implementationMode={Platform.OS === 'android' ? 'compatible' : undefined}
        // iOS wires the frameOutput for worklet-based color sampling
        outputs={Platform.OS === 'ios' ? [frameOutput] : []}
      />

      {/* Reticle — border color tracks detected ambient color in real time */}
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
    ...Platform.select({
      web: {
        boxShadow: '0px 0px 8px rgba(0,0,0,0.6)',
      },
      default: {
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 0 },
        shadowOpacity: 0.6,
        shadowRadius: 8,
        elevation: 8,
      }
    })
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
