import React, { useState, useEffect, useRef } from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { Camera, CameraView, useCameraPermissions } from 'expo-camera';
import * as ImageManipulator from 'expo-image-manipulator';
import jpeg from 'jpeg-js';
import { Buffer } from 'buffer';
import { Colors } from '../theme/theme';

interface CameraTrackerProps {
  onColorDetected: (hex: string) => void;
  isActive: boolean;
}

// Ensure the Base64 snippet has no MIME header
const extractBase64Data = (uri: string) => uri.replace(/^data:image\/\w+;base64,/, '');

export default function CameraTracker({ onColorDetected, isActive }: CameraTrackerProps) {
  const [permission, requestPermission] = useCameraPermissions();
  const [detectedHex, setDetectedHex] = useState<string>('#000000');
  const cameraRef = useRef<CameraView>(null);

  useEffect(() => {
    if (!isActive || !permission?.granted) return;
    
    const interval = setInterval(async () => {
      if (cameraRef.current) {
        try {
          const photo = await cameraRef.current.takePictureAsync({ quality: 0.1, skipProcessing: true });
          if (!photo) return;
          
          // Crop it straight exactly at the geometric center
          // A tiny 10x10 slice gives us 100 pixels to aggressively average
          const centerX = photo.width / 2;
          const centerY = photo.height / 2;
          
          const result = await ImageManipulator.manipulateAsync(
            photo.uri,
            [{ crop: { originX: centerX - 5, originY: centerY - 5, width: 10, height: 10 } }],
            { base64: true, format: ImageManipulator.SaveFormat.JPEG, compress: 1.0 }
          );

          if (result.base64) {
            const rawData = extractBase64Data(result.base64);
            const buffer = Buffer.from(rawData, 'base64');
            const decoded = jpeg.decode(buffer, { useTArray: true });
            
            let r = 0, g = 0, b = 0;
            const pixelCount = decoded.width * decoded.height;
            
            // Loop through the contiguous RGBA sequence
            for (let i = 0; i < decoded.data.length; i += 4) {
                r += decoded.data[i];
                g += decoded.data[i + 1];
                b += decoded.data[i + 2];
            }
            
            const avgR = Math.round(r / pixelCount);
            const avgG = Math.round(g / pixelCount);
            const avgB = Math.round(b / pixelCount);
            
            const hex = '#' + [avgR, avgG, avgB].map(x => x.toString(16).padStart(2, '0')).join('');
            setDetectedHex(hex);
            onColorDetected(hex);
          }
        } catch (e) {
          console.log('Camera capture dropped cycle', e);
        }
      }
    }, 1000); // 1000ms loop

    return () => clearInterval(interval);
  }, [isActive, permission?.granted, onColorDetected]);

  if (!permission) {
    return <View />;
  }

  if (!permission.granted) {
    return (
      <View style={styles.container}>
        <Text style={styles.message}>We need your permission to show the camera</Text>
        <TouchableOpacity style={styles.button} onPress={requestPermission}>
          <Text style={{ color: '#000', fontWeight: 'bold' }}>GRANT PERMISSION</Text>
        </TouchableOpacity>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <View style={styles.cameraBox}>
        <CameraView style={styles.camera} ref={cameraRef} facing="back">
           <View style={styles.crosshair}>
              <Text style={styles.crosshairText}>[ x ]</Text>
           </View>
        </CameraView>
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
    height: 400,
    backgroundColor: '#050505',
    borderRadius: 24,
    marginTop: 8,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.05)',
    overflow: 'hidden',
    alignItems: 'center',
    paddingTop: 16
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
    height: 280,
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
  crosshair: {
    justifyContent: 'center',
    alignItems: 'center',
  },
  crosshairText: {
    color: 'rgba(255, 0, 0, 0.8)',
    fontSize: 32,
    fontWeight: '800',
  },
  statusBox: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 16,
    paddingHorizontal: 24,
    paddingVertical: 12,
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
