import React, { useState, useEffect, useRef } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Platform } from 'react-native';
import { Camera, CameraView, useCameraPermissions } from 'expo-camera';
import { Buffer } from 'buffer';
import { Colors } from '../theme/theme';

let ImageManipulator: any = null;
let jpeg: any = null;

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

  if (Platform.OS === 'web') {
    return (
      <View style={[styles.container, { justifyContent: 'center', alignItems: 'center' }]}>
        <Text style={{ color: Colors.text, textAlign: 'center', fontSize: 16 }}>
          Camera Tracker Optical Telemetry is only supported on Native Android/iOS builds.
        </Text>
      </View>
    );
  }

  const handlePress = async (event: any) => {
    if (!isActive || !permission?.granted || !cameraRef.current || isProcessing || !layout.width || !layout.height) return;

    const { locationX, locationY } = event.nativeEvent;
    
    setIsProcessing(true);
    try {
      const photo = await cameraRef.current.takePictureAsync({ quality: 0.2, skipProcessing: true });
      if (!photo) {
        setIsProcessing(false);
        return;
      }
      
      // Map screen touch to photo coordinates
      const x_p = (locationX / layout.width) * photo.width;
      const y_p = (locationY / layout.height) * photo.height;
      
      const cropSize = 20; // Slightly larger for better averaging
      const originX = Math.max(0, Math.min(photo.width - cropSize, x_p - cropSize / 2));
      const originY = Math.max(0, Math.min(photo.height - cropSize, y_p - cropSize / 2));

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

        // VIVIDNESS NORMAILZATION ALGORITHM:
        // Raw camera feeds are inherently washed out by white light (glare). 
        // For physical LEDs, any white mixed into the RGB channel degrades the pure hue.
        // We eliminate the lowest channel (white noise) by pushing it to 0, and pull the highest channel to 255.
        const max = Math.max(avgR, avgG, avgB);
        const min = Math.min(avgR, avgG, avgB);
        
        if (max > 0 && max !== min) {
           avgR = Math.round(((avgR - min) / (max - min)) * 255);
           avgG = Math.round(((avgG - min) / (max - min)) * 255);
           avgB = Math.round(((avgB - min) / (max - min)) * 255);
        }
        
        const hex = '#' + [avgR, avgG, avgB].map(x => x.toString(16).padStart(2, '0')).join('').toUpperCase();
        setDetectedHex(hex);
        onColorDetected(hex);
      }
    } catch (e) {
      console.log('Camera capture failed', e);
    } finally {
      setIsProcessing(false);
    }
  };

  if (!permission) {
    return <View />;
  }

  if (!permission.granted) {
    return (
      <View style={styles.container}>
        <Text style={styles.message}>We need your permission to show the camera</Text>
        <TouchableOpacity style={styles.button} onPress={requestPermission}>
          <Text style={{ color: Colors.isDark ? '#FFF' : '#000', fontWeight: 'bold' }}>GRANT PERMISSION</Text>
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
    minHeight: 180,
    backgroundColor: '#050505',
    borderRadius: 24,
    marginTop: 8,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.05)',
    overflow: 'hidden',
    alignItems: 'center',
    paddingTop: 16,
    paddingBottom: 16
  },
  message: {
    color: '#FFF',
    textAlign: 'center',
    paddingBottom: 10,
    marginTop: 100
  },
  button: {
    backgroundColor: Colors.primary,
    paddingHorizontal: 24,
    paddingVertical: 12,
    borderRadius: 12,
  },
  cameraBox: {
    width: '90%',
    flex: 1,
    marginBottom: 8,
    borderRadius: 16,
    overflow: 'hidden',
    backgroundColor: '#000',
    borderWidth: 2,
    borderColor: 'rgba(255,255,255,0.1)',
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
    backgroundColor: 'rgba(0,0,0,0.2)',
  },
  instructionText: {
    color: '#FFF',
    fontSize: 18,
    fontWeight: '600',
    textAlign: 'center',
    backgroundColor: 'rgba(0,0,0,0.5)',
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 20,
  },
  statusBox: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 8,
    paddingHorizontal: 24,
    paddingVertical: 8,
    backgroundColor: 'rgba(255,255,255,0.05)',
    borderRadius: 100,
  },
  swatch: {
    width: 24,
    height: 24,
    borderRadius: 12,
    marginRight: 12,
    borderWidth: 1,
    borderColor: '#FFF',
  },
  hexText: {
    color: '#FFF',
    fontSize: 16,
    fontWeight: 'bold',
    fontFamily: 'monospace',
    letterSpacing: 2
  }
});
