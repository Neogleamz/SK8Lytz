import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';
import { Buffer } from 'buffer';
import { CameraView, useCameraPermissions } from 'expo-camera';
import React, { useEffect, useRef, useState } from 'react';
import { ActivityIndicator, Linking, Platform, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { Colors, Spacing } from '../theme/theme';
import { openGlobalPermissionsModal } from '../services/PermissionService';

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

// Ensure the Base64 snippet has no MIME header
const extractBase64Data = (uri: string) => uri.replace(/^data:image\/\w+;base64,/, '');

export default function CameraTracker({ onColorDetected, isActive }: CameraTrackerProps) {
  const [permission, requestPermission] = useCameraPermissions();
  const [detectedHex, setDetectedHex] = useState<string>('#000000');
  const [isProcessing, setIsProcessing] = useState(false);
  const [layout, setLayout] = useState({ width: 0, height: 0 });
  const cameraRef = useRef<CameraView>(null);

  // Aggressive prompt for undetermined permissions on mount
  useEffect(() => {
    if (permission && permission.status === 'undetermined') {
      openGlobalPermissionsModal().then(() => requestPermission());
    }
  }, [permission, requestPermission]);

  if (Platform.OS === 'web') {
    return (
      <View style={[styles.container, { justifyContent: 'center', alignItems: 'center', backgroundColor: 'rgba(0,0,0,0.4)', padding: Spacing.xl }]}>
        <MaterialCommunityIcons name="camera-iris" size={48} color={Colors.primary} style={{ marginBottom: Spacing.lg, opacity: 0.8 }} />
        <Text style={[styles.message, { fontSize: 18, marginBottom: Spacing.sm }]}>Optical Simulation Mode</Text>
        <Text style={{ color: Colors.textMuted, textAlign: 'center', fontSize: 13, lineHeight: 18, marginBottom: Spacing.xl }}>
          Optical telemetry requires native hardware acceleration reserved for Android & iOS builds.
        </Text>
        
        <TouchableOpacity 
          style={[styles.button, { backgroundColor: 'rgba(0,240,255,0.15)', borderWidth: 1, borderColor: Colors.primary }]}
          onPress={() => {
            const randomHex = '#' + Math.floor(Math.random()*16777215).toString(16).padStart(6, '0').toUpperCase();
            setDetectedHex(randomHex);
            onColorDetected(randomHex);
          }}
        >
          <Text style={{ color: Colors.primary, fontWeight: 'bold', letterSpacing: 1 }}>SIMULATE TELEMETRY</Text>
        </TouchableOpacity>
      </View>
    );
  }

  const handlePress = async (event: { nativeEvent: { locationX: number; locationY: number } }) => {
    if (!isActive || !permission?.granted || !cameraRef.current || isProcessing || !layout.width || !layout.height) return;
    if (!ImageManipulator || !jpeg) return; // native libs not loaded (should not happen on device)

    const { locationX, locationY } = event.nativeEvent;
    
    setIsProcessing(true);
    try {
      const photo = await cameraRef.current.takePictureAsync({ quality: 0.1, skipProcessing: true });
      if (!photo) {
        setIsProcessing(false);
        return;
      }
      
      // Map screen touch to photo coordinates
      const x_p = (locationX / layout.width) * photo.width;
      const y_p = (locationY / layout.height) * photo.height;
      
      const cropSize = 10; // 10x10 sample area to average out camera noise
      const originX = Math.floor(Math.max(0, Math.min(photo.width - cropSize, x_p)));
      const originY = Math.floor(Math.max(0, Math.min(photo.height - cropSize, y_p)));

      const result = await ImageManipulator.manipulateAsync(
        photo.uri,
        [{ crop: { originX, originY, width: cropSize, height: cropSize } }],
        { base64: true, format: ImageManipulator.SaveFormat.JPEG, compress: 1.0 }
      );

      if (result.base64) {
        const rawData = extractBase64Data(result.base64);
        const buffer = Buffer.from(rawData, 'base64');
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

        // VIVIDNESS NORMALIZATION ALGORITHM (HSL BOOST):
        // Convert averaged RGB to HSL, boost saturation to make colors pop on LEDs,
        // without destroying underlying whites and greys like a min/max stretch would.
        const rNorm = avgR / 255;
        const gNorm = avgG / 255;
        const bNorm = avgB / 255;
        const cMax = Math.max(rNorm, gNorm, bNorm);
        const cMin = Math.min(rNorm, gNorm, bNorm);
        const delta = cMax - cMin;

        let h = 0, s = 0, l = (cMax + cMin) / 2;

        if (delta !== 0) {
          s = delta / (1 - Math.abs(2 * l - 1));
          switch (cMax) {
            case rNorm: h = ((gNorm - bNorm) / delta) % 6; break;
            case gNorm: h = (bNorm - rNorm) / delta + 2; break;
            case bNorm: h = (rNorm - gNorm) / delta + 4; break;
          }
          h = Math.round(h * 60);
          if (h < 0) h += 360;
        }

        // Boost saturation safely by 0.2 (20%)
        s = Math.min(1, s + 0.2);

        // Convert back to RGB
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
        setDetectedHex(hex);
        onColorDetected(hex);
      }
    } catch (e: any) {
      console.error('Camera capture failed', e);
    } finally {
      setIsProcessing(false);
    }
  };

  if (!permission) {
    // Still loading permission status — show spinner, not blank
    return (
      <View style={[styles.container, { justifyContent: 'center', alignItems: 'center' }]}>
        <ActivityIndicator size="large" color={Colors.primary} />
        <Text style={[styles.message, { marginTop: Spacing.md }]}>Checking camera access…</Text>
      </View>
    );
  }

  if (!permission.granted) {
    // If we've explicitly been denied and the OS says we can't ask again, then we fallback to open settings
    const requiresSettings = permission.status === 'denied' && !permission.canAskAgain;
    return (
      <View style={[styles.container, { justifyContent: 'center', alignItems: 'center', paddingHorizontal: Spacing.xl }]}>
        <MaterialCommunityIcons name="camera-off" size={40} color={Colors.textMuted} style={{ marginBottom: Spacing.md }} />
        <Text style={styles.message}>
          {requiresSettings
            ? 'Camera access was denied permanently. Please enable it in your device Settings.'
            : 'Camera access is needed to detect colors from your environment.'}
        </Text>
        <TouchableOpacity style={styles.button} onPress={requiresSettings ? () => Linking.openSettings() : async () => { await openGlobalPermissionsModal(); requestPermission(); }}>
          <Text style={{ color: Colors.isDark ? '#FFF' : '#000', fontWeight: 'bold' }}>
            {requiresSettings ? 'OPEN SETTINGS' : 'GRANT PERMISSION'}
          </Text>
        </TouchableOpacity>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <View style={styles.cameraBox}>
        <TouchableOpacity 
          activeOpacity={1} 
          onPress={handlePress}
          style={{ flex: 1 }}
        >
          <CameraView 
            style={styles.camera} 
            ref={cameraRef} 
            facing="back"
            onLayout={(e) => {
              setLayout({
                width: e.nativeEvent.layout.width,
                height: e.nativeEvent.layout.height
              });
            }}
          >
             <View style={styles.instructionOverlay}>
                <Text style={styles.instructionText}>
                  {isProcessing ? 'Analyzing...' : 'Touch screen to pick color'}
                </Text>
             </View>
          </CameraView>
        </TouchableOpacity>
      </View>
      <View style={styles.statusBox}>
         <View style={[styles.swatch, { backgroundColor: detectedHex }]} />
         <Text style={styles.hexText}>{detectedHex}</Text>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    minHeight: 200,
    backgroundColor: '#000',
    borderRadius: 0,
    marginTop: 0,
    borderWidth: 0,
    overflow: 'hidden',
    alignItems: 'center',
    paddingTop: 0,
    paddingBottom: 0
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
  cameraBox: {
    width: '100%',
    flex: 1,
    backgroundColor: '#000',
  },
  camera: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  instructionOverlay: {
    ...StyleSheet.absoluteFillObject,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0,0,0,0.1)',
  },
  instructionText: {
    color: '#FFF',
    fontSize: 16,
    fontFamily: 'Righteous',
    textAlign: 'center',
    backgroundColor: 'rgba(0,0,0,0.6)',
    paddingHorizontal: Spacing.lg,
    paddingVertical: Spacing.md,
    borderRadius: 24,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.2)',
  },
  statusBox: {
    position: 'absolute',
    bottom: 0,
    left: 0,
    right: 0,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    paddingHorizontal: Spacing.lg,
    paddingVertical: Spacing.md,
    backgroundColor: 'rgba(0,0,0,0.85)',
    borderTopWidth: 1,
    borderTopColor: 'rgba(255,255,255,0.15)',
  },
  swatch: {
    width: 20,
    height: 20,
    borderRadius: 10,
    marginRight: Spacing.md,
    borderWidth: 1,
    borderColor: '#FFF',
  },
  hexText: {
    color: '#FFF',
    fontSize: 14,
    fontFamily: 'Righteous',
    letterSpacing: 1
  }
});
