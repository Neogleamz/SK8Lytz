import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';
import { Buffer } from 'buffer';
import React, { useEffect, useRef, useState, useCallback } from 'react';
import { ActivityIndicator, Linking, Platform, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { Camera, useCameraDevice, useCameraPermission } from 'react-native-vision-camera';
import { Colors, Spacing } from '../theme/theme';
import { openGlobalPermissionsModal } from '../services/PermissionService';
import * as FileSystem from 'expo-file-system';

// Types for dynamically loaded native-only libraries
type ImageManipulatorModule = {
  manipulateAsync: (
    uri: string,
    actions: Array<{ crop?: { originX: number; originY: number; width: number; height: number } }>,
    options: { base64?: boolean; format?: string; compress?: number }
  ) => Promise<{ uri: string; base64?: string; width: number; height: number }>;
  SaveFormat: { JPEG: string; PNG: string };
};
type JpegModule = {
  decode: (buffer: Buffer, options?: { useTArray?: boolean }) => { width: number; height: number; data: Uint8Array };
};

let ImageManipulator: ImageManipulatorModule | null = null;
let jpeg: JpegModule | null = null;

if (Platform.OS !== 'web') {
  try {
    ImageManipulator = require('expo-image-manipulator');
    jpeg = require('jpeg-js');
  } catch (e) {
    console.warn("Optical native libraries bypassed on Web array.");
  }
}

interface CameraTrackerProps {
  onColorDetected: (hex: string) => void;
  isActive: boolean;
}

export default function CameraTracker({ onColorDetected, isActive }: CameraTrackerProps) {
  const { hasPermission, requestPermission } = useCameraPermission();
  const device = useCameraDevice('back');
  const cameraRef = useRef<Camera>(null);
  
  const [liveHex, setLiveHex] = useState<string>('#000000');
  const [isCapturing, setIsCapturing] = useState(false);
  const [layout, setLayout] = useState({ width: 0, height: 0 });
  const isProcessingRef = useRef(false);

  // Aggressive prompt for undetermined permissions
  useEffect(() => {
    if (!hasPermission) {
      openGlobalPermissionsModal().then(() => requestPermission());
    }
  }, [hasPermission, requestPermission]);

  // Continuous background polling loop (Simulated Frame Processor)
  useEffect(() => {
    if (!isActive || !hasPermission || !device || !layout.width || !layout.height) return;

    let mounted = true;
    const POLLING_INTERVAL_MS = 300; // ~3 FPS

    const pollCenterPixel = async () => {
      if (!mounted || isProcessingRef.current || !cameraRef.current || !ImageManipulator || !jpeg) return;

      isProcessingRef.current = true;
      try {
        const photo = await cameraRef.current.takePhoto({
          qualityPrioritization: 'speed',
          enableShutterSound: false,
          flash: 'off',
        });

        const cropSize = 10; 
        const originX = Math.floor(Math.max(0, (photo.width / 2) - (cropSize / 2)));
        const originY = Math.floor(Math.max(0, (photo.height / 2) - (cropSize / 2)));

        const result = await ImageManipulator.manipulateAsync(
          `file://${photo.path}`,
          [{ crop: { originX, originY, width: cropSize, height: cropSize } }],
          { base64: true, format: ImageManipulator.SaveFormat.JPEG, compress: 1.0 }
        );

        // Cleanup the massive original photo from flash storage to prevent leaks
        FileSystem.deleteAsync(`file://${photo.path}`).catch(() => {});

        if (result.base64 && mounted) {
          const buffer = Buffer.from(result.base64, 'base64');
          const decoded = jpeg.decode(buffer, { useTArray: true });
          
          let r = 0, g = 0, b = 0;
          const pixelCount = decoded.width * decoded.height;
          
          for (let i = 0; i < decoded.data.length; i += 4) {
              r += decoded.data[i];
              g += decoded.data[i + 1];
              b += decoded.data[i + 2];
          }
          
          let avgR = Math.round(r / pixelCount);
          let avgG = Math.round(g / pixelCount);
          let avgB = Math.round(b / pixelCount);

          // VIVIDNESS NORMALIZATION (HSL BOOST)
          const rNorm = avgR / 255;
          const gNorm = avgG / 255;
          const bNorm = avgB / 255;
          const cMax = Math.max(rNorm, gNorm, bNorm);
          const cMin = Math.min(rNorm, gNorm, bNorm);
          const delta = cMax - cMin;

          let h = 0;
          if (delta !== 0) {
            switch (cMax) {
              case rNorm: h = ((gNorm - bNorm) / delta) % 6; break;
              case gNorm: h = (bNorm - rNorm) / delta + 2; break;
              case bNorm: h = (rNorm - gNorm) / delta + 4; break;
            }
            h = Math.round(h * 60);
            if (h < 0) h += 360;
          }

          // Lock S and L for pure vibrant LED color
          const s = 1.0;
          const l = 0.5;

          const c = (1 - Math.abs(2 * l - 1)) * s;
          const x = c * (1 - Math.abs((h / 60) % 2 - 1));
          const m = l - c / 2;
          let rPrime = 0, gPrime = 0, bPrime = 0;

          if (h >= 0 && h < 60) { rPrime = c; gPrime = x; bPrime = 0; }
          else if (h >= 60 && h < 120) { rPrime = x; gPrime = c; bPrime = 0; }
          else if (h >= 120 && h < 180) { rPrime = 0; gPrime = c; bPrime = x; }
          else if (h >= 180 && h < 240) { rPrime = 0; gPrime = x; bPrime = c; }
          else if (h >= 240 && h < 300) { rPrime = x; gPrime = 0; bPrime = c; }
          else if (h >= 300 && h < 360) { rPrime = c; gPrime = 0; bPrime = x; }

          avgR = Math.round((rPrime + m) * 255);
          avgG = Math.round((gPrime + m) * 255);
          avgB = Math.round((bPrime + m) * 255);
          
          const hex = '#' + [avgR, avgG, avgB].map(x => x.toString(16).padStart(2, '0')).join('').toUpperCase();
          setLiveHex(hex);
        }
      } catch (e) {
        // Silent catch for background polling
      } finally {
        if (mounted) isProcessingRef.current = false;
      }
    };

    const intervalId = setInterval(pollCenterPixel, POLLING_INTERVAL_MS);
    return () => {
      mounted = false;
      clearInterval(intervalId);
    };
  }, [isActive, hasPermission, device, layout.width, layout.height]);

  const handleCapture = useCallback(() => {
    setIsCapturing(true);
    onColorDetected(liveHex);
    // Haptic simulation via slight delay before resetting capture state
    setTimeout(() => setIsCapturing(false), 200);
  }, [liveHex, onColorDetected]);

  if (Platform.OS === 'web' || !device) {
    return (
      <View style={[styles.container, { justifyContent: 'center', alignItems: 'center', backgroundColor: 'rgba(0,0,0,0.4)', padding: Spacing.xl }]}>
        <ActivityIndicator color={Colors.primary} size="large" />
      </View>
    );
  }

  if (!hasPermission) {
    return (
      <View style={[styles.container, { justifyContent: 'center', alignItems: 'center', paddingHorizontal: Spacing.xl }]}>
        <MaterialCommunityIcons name="camera-off" size={40} color={Colors.textMuted} style={{ marginBottom: Spacing.md }} />
        <Text style={styles.message}>Camera access is needed to detect colors from your environment.</Text>
        <TouchableOpacity style={styles.button} onPress={async () => { await openGlobalPermissionsModal(); requestPermission(); }}>
          <Text style={{ color: Colors.isDark ? '#FFF' : '#000', fontWeight: 'bold' }}>
            GRANT PERMISSION
          </Text>
        </TouchableOpacity>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Camera 
        style={StyleSheet.absoluteFillObject} 
        device={device}
        isActive={isActive}
        ref={cameraRef}
        photo={true}
        onLayout={(e) => {
          setLayout({
            width: e.nativeEvent.layout.width,
            height: e.nativeEvent.layout.height
          });
        }}
      />
      
      {/* Reticle Overlay */}
      <View style={styles.reticleContainer} pointerEvents="none">
        <View style={[styles.reticleRing, { borderColor: liveHex }]}>
          <View style={styles.reticleCrosshairHorizontal} />
          <View style={styles.reticleCrosshairVertical} />
        </View>
      </View>

      {/* Control Overlay */}
      <View style={styles.instructionOverlay}>
         <View style={styles.pillContainer}>
           <Text style={styles.pillText}>{liveHex}</Text>
         </View>
         <Text style={styles.instructionText}>
           Center target on a light source and capture
         </Text>

         {/* Massive CAPTURE FAB */}
         <TouchableOpacity 
            activeOpacity={0.8}
            onPress={handleCapture}
            style={[
              styles.captureFab, 
              { backgroundColor: liveHex },
              isCapturing && styles.captureFabActive
            ]}
          >
            <MaterialCommunityIcons 
              name="line-scan" 
              size={36} 
              color={liveHex === '#FFFFFF' || liveHex === '#FFFF00' ? '#000' : '#FFF'} 
            />
         </TouchableOpacity>
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
    width: 60,
    height: 60,
    borderRadius: 30,
    borderWidth: 3,
    backgroundColor: 'rgba(255,255,255,0.1)',
    justifyContent: 'center',
    alignItems: 'center',
    ...Platform.select({
      web: { boxShadow: '0px 0px 10px rgba(0,0,0,0.5)' } as any,
      default: { shadowColor: '#000', shadowOffset: { width: 0, height: 0 }, shadowOpacity: 0.5, shadowRadius: 10 }
    }),
  },
  reticleCrosshairHorizontal: {
    position: 'absolute',
    width: 20,
    height: 2,
    backgroundColor: 'rgba(255,255,255,0.8)',
  },
  reticleCrosshairVertical: {
    position: 'absolute',
    width: 2,
    height: 20,
    backgroundColor: 'rgba(255,255,255,0.8)',
  },
  instructionOverlay: {
    position: 'absolute',
    bottom: 0,
    left: 0,
    right: 0,
    alignItems: 'center',
    paddingBottom: Spacing.xxl,
    paddingTop: Spacing.xl,
  },
  pillContainer: {
    backgroundColor: 'rgba(0,0,0,0.7)',
    paddingHorizontal: Spacing.lg,
    paddingVertical: 8,
    borderRadius: 20,
    marginBottom: Spacing.xs,
  },
  pillText: {
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
    ...Platform.select({
      web: { boxShadow: '0px 8px 15px rgba(0,0,0,0.4)' } as any,
      default: { shadowColor: '#000', shadowOffset: { width: 0, height: 8 }, shadowOpacity: 0.4, shadowRadius: 15 }
    }),
  },
  captureFabActive: {
    transform: [{ scale: 0.95 }],
    opacity: 0.8,
  }
});
