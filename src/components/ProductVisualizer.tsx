import React, { useEffect, useRef, useMemo } from 'react';
import { View, StyleSheet, Animated, Text, TouchableOpacity } from 'react-native';

interface DeviceConfig {
  id?: string;
  name?: string;
  type?: 'HALOZ' | 'SOULZ';
  points?: number;
}

interface ProductVisualizerProps {
  product: 'HALOZ' | 'SOULZ';
  color: string;
  mode: string;
  patternId: number | null;
  isPaired?: boolean;
  points?: number;
  devices?: DeviceConfig[];
  onLongPressDevice?: (device: any) => void;
  fixedFgColor?: string;
  fixedBgColor?: string;
  brightness?: number;
  speed?: number;
  isPoweredOn?: boolean;
  statusText?: string;
}

// Convert HSL to Hex manually as React Native Interpolate handles strict string maps better
function HSLToHex(h: number, s: number, l: number) {
  h = (h % 360 + 360) % 360; // Normalize [0-360]
  s /= 100; l /= 100;
  let c = (1 - Math.abs(2 * l - 1)) * s,
      x = c * (1 - Math.abs((h / 60) % 2 - 1)),
      m = l - c/2,
      r = 0, g = 0, b = 0;

  if (0 <= h && h < 60) { r = c; g = x; b = 0; } 
  else if (60 <= h && h < 120) { r = x; g = c; b = 0; } 
  else if (120 <= h && h < 180) { r = 0; g = c; b = x; } 
  else if (180 <= h && h < 240) { r = 0; g = x; b = c; } 
  else if (240 <= h && h < 300) { r = x; g = 0; b = c; } 
  else if (300 <= h && h <= 360) { r = c; g = 0; b = x; }
  
  r = Math.round((r + m) * 255);
  g = Math.round((g + m) * 255);
  b = Math.round((b + m) * 255);
  
  const toHex = (n: number) => { const hex = n.toString(16); return hex.length === 1 ? '0' + hex : hex; };
  return `#${toHex(r)}${toHex(g)}${toHex(b)}`;
}

const VisualizerUnit = ({ device, color, mode, patternId, animValue, fallbackProduct, fallbackPoints, onLongPress, fixedFgColor, fixedBgColor, brightness = 100, isPoweredOn = true }: any) => {
  const product = device.type || fallbackProduct;
  const pointsPerSide = device.points || fallbackPoints || (product === 'HALOZ' ? 24 : 43);
  const numLeds = pointsPerSide * 2;

  const leds = useMemo(() => {
    const list = [];
    const numSamples = 5000;
    const pathSamples = [];
    let totalLength = 0;
    
    // SCALE FACTOR
    const S = 0.55;

    for (let i = 0; i <= numSamples; i++) {
        const fract = i / numSamples;
        const angle = (fract * 2 * Math.PI) + (Math.PI / 2);
        let top = 0; let left = 0;
        
        if (product === 'HALOZ') {
          const p = 0.6;
          const sgnCos = Math.sign(Math.cos(angle)) || 1;
          const sgnSin = Math.sign(Math.sin(angle)) || 1;
          left = (80 + sgnCos * Math.pow(Math.abs(Math.cos(angle)), p) * 70) * S;
          top = (120 + sgnSin * Math.pow(Math.abs(Math.sin(angle)), p) * 110) * S;
        } else {
          top = (150 + Math.sin(angle) * 150) * S;
          const verticalPos = Math.sin(angle);
          const pinch = 1 - 0.3 * Math.exp(-Math.pow(verticalPos - 0.1, 2) * 5); 
          left = (70 + (Math.cos(angle) * 70 * pinch)) * S;
        }

        if (i > 0) {
            const prev = pathSamples[i - 1];
            const dx = left - prev.left;
            const dy = top - prev.top;
            totalLength += Math.sqrt(dx*dx + dy*dy);
        }
        pathSamples.push({ top, left, length: totalLength });
    }

    let lastSampleIdx = 0;
    for (let i = 0; i < numLeds; i++) {
        const targetLength = (i / numLeds) * totalLength;
        let p1 = pathSamples[lastSampleIdx];
        let p2 = pathSamples[lastSampleIdx + 1];
        
        for (let j = lastSampleIdx; j < numSamples; j++) {
            if (pathSamples[j+1].length >= targetLength) {
                p1 = pathSamples[j];
                p2 = pathSamples[j+1];
                lastSampleIdx = j;
                break;
            }
        }
        
        const segmentLength = p2.length - p1.length;
        const t = segmentLength <= 0.0001 ? 0 : (targetLength - p1.length) / segmentLength;
        
        const fract = (i / numLeds);
        const mirroredFract = fract <= 0.5 ? fract * 2 : (1 - fract) * 2;
        let dotColor: any = isPoweredOn ? color : '#333333';
        let dotOpacity: any = isPoweredOn ? 1 : 0.2;

        if (isPoweredOn) {
          if (mode === 'PRESETS') {
             const rainbowColors = [0, 1/6, 2/6, 3/6, 4/6, 5/6, 1].map(v => HSLToHex((v - mirroredFract + 1) % 1 * 360, 100, 50));
             dotColor = animValue.interpolate({ inputRange: [0, 0.16, 0.33, 0.5, 0.66, 0.83, 1], outputRange: rainbowColors });
          } else if (mode === 'RBM') {
             dotOpacity = animValue.interpolate({
                inputRange: [0, Math.max(0, mirroredFract - 0.15), mirroredFract, Math.min(1, mirroredFract + 0.15), 1],
                outputRange: [0.1, 0.1, 1, 0.1, 0.1]
             });
             if (patternId && patternId % 2 === 0) {
               dotColor = animValue.interpolate({ inputRange: [0, 0.5, 1], outputRange: ['#FF6E00', '#FFD60A', '#FF6E00'] });
             } else {
               dotColor = animValue.interpolate({ inputRange: [0, 1], outputRange: ['#FF0000', '#FFFF00'] });
             }
          } else if (mode === 'MUSIC') {
             const h = 1 - mirroredFract;
             const colorVal = parseInt(color.replace('#',''), 16) || 0;
             const r = (colorVal >> 16) & 255;
             const g = (colorVal >> 8) & 255;
             const b = colorVal & 255;
             const compHex = '#' + ((255-r)<<16 | (255-g)<<8 | (255-b)).toString(16).padStart(6, '0');
             
             if (patternId === 1 || patternId === 2) { 
               dotOpacity = animValue.interpolate({ 
                 inputRange: [0, 0.1, 0.5, 1], 
                 outputRange: [0.1, 1.0 * (h + 0.2), 0.5 * h, 0.1] 
               });
               dotColor = (h > 0.5) ? color : compHex;
             } else if (patternId === 3 || patternId === 7) { 
               dotOpacity = animValue.interpolate({ inputRange: [0, 0.5, 1], outputRange: [0.4, 1, 0.4] });
               dotColor = animValue.interpolate({ inputRange: [0, 0.5, 1], outputRange: [color, compHex, color] });
             } else if (patternId === 6 || patternId === 10) { 
               dotOpacity = animValue.interpolate({ inputRange: [0, 0.2, 0.3, 1], outputRange: [0.1, 0.1, 1, 0.1] });
               dotColor = color;
             } else if (patternId === 8 || patternId === 9) { 
               dotOpacity = animValue.interpolate({ inputRange: [0, 0.1, 0.2, 0.3, 1], outputRange: [0.2, 1, 0.2, 1, 0.2] });
               dotColor = animValue.interpolate({ inputRange: [0, 1], outputRange: [color, compHex] });
             } else { 
               dotOpacity = animValue.interpolate({
                  inputRange: [0, Math.max(0, mirroredFract - 0.2), mirroredFract, Math.min(1, mirroredFract + 0.1), 1],
                  outputRange: [0.1, 0.1, 1, 0.1, 0.1]
               });
               dotColor = animValue.interpolate({ inputRange: [0, 1], outputRange: [color, compHex] });
             }
          } else if (mode === 'FIXED') {
             const fg = fixedFgColor || color;
             const bg = fixedBgColor || '#000000';
             
             if (patternId === 1) {
               dotColor = fg;
             } else if (patternId === 2) {
               dotOpacity = animValue.interpolate({
                  inputRange: [0, Math.max(0, mirroredFract - 0.1), mirroredFract, Math.min(1, mirroredFract + 0.1), 1],
                  outputRange: [0.1, 0.1, 1, 0.1, 0.1]
               });
               dotColor = fg;
             } else if (patternId === 3) {
               dotOpacity = animValue.interpolate({
                  inputRange: [0, Math.max(0, mirroredFract - 0.25), mirroredFract, Math.min(1, mirroredFract + 0.05), 1],
                  outputRange: [0.1, 0.1, 1, 0.1, 0.1]
               });
               dotColor = animValue.interpolate({ inputRange: [0, 1], outputRange: [fg, bg] });
             } else if (patternId === 4) {
               dotColor = (i % 8 < 4) ? fg : bg;
               dotOpacity = animValue.interpolate({ inputRange: [0, 0.5, 1], outputRange: [1, 0.2, 1] });
             } else if (patternId === 5) {
               dotColor = (i % 4 < 2) ? fg : bg;
               dotOpacity = animValue.interpolate({ inputRange: [0, 0.5, 1], outputRange: [1, 0.2, 1] });
             } else if (patternId === 6) {
               dotColor = animValue.interpolate({ inputRange: [0, 0.5, 1], outputRange: [fg, bg, fg] });
             } else if (patternId === 7) {
               dotColor = animValue.interpolate({ inputRange: [0, 0.49, 0.5, 0.99, 1], outputRange: [fg, fg, bg, bg, fg] });
             }
          }
        }

        const dotSize = product === 'HALOZ' ? 12 : 10;
        const offset = dotSize / 2;
        const left = p1.left + (p2.left - p1.left) * t - offset;
        const top = p1.top + (p2.top - p1.top) * t - offset;

        list.push({
          key: `led_${i}`,
          position: { top, left, position: 'absolute' as const },
          activeColor: dotColor,
          activeOpacity: dotOpacity,
        });
    }
    return list;
  }, [product, mode, color, numLeds, patternId, isPoweredOn]);

  return (
    <TouchableOpacity 
      activeOpacity={onLongPress ? 0.8 : 1}
      onLongPress={onLongPress ? () => onLongPress(device) : undefined}
      style={{ alignItems: 'center', marginHorizontal: 12, paddingVertical: 4 }}
    >
      <View style={[
        product === 'HALOZ' ? styles.haloBase : styles.soulBase, 
        { alignSelf: 'center', opacity: isPoweredOn ? (brightness === 0 ? 0 : 0.08 + Math.pow(brightness / 100, 0.6) * 0.92) : 0.2 }
      ]}>
         {leds.map(led => (
            <Animated.View key={led.key} style={[
               product === 'HALOZ' ? styles.ledDot : styles.ledDotSmall, 
               led.position, 
               { 
                 backgroundColor: led.activeColor, 
                 opacity: led.activeOpacity,
                 shadowColor: isPoweredOn ? led.activeColor : 'transparent',
                 width: product === 'HALOZ' ? 12 : 10,
                 height: product === 'HALOZ' ? 12 : 10,
                 borderRadius: product === 'HALOZ' ? 6 : 5,
               }
            ]} />
         ))}
      </View>
      <View style={{ marginTop: 4, alignItems: 'center', zIndex: 10, width: 100 }}>
         <Text 
           style={{ color: 'white', fontWeight: 'bold', fontSize: 11, textAlign: 'center', opacity: isPoweredOn ? 1.0 : 0.4 }}
           numberOfLines={2}
         >
           {device.name || product}
         </Text>
      </View>
    </TouchableOpacity>
  );
};

export default function ProductVisualizer({ product, color, mode, patternId, isPaired, points, devices, fixedFgColor, fixedBgColor, onLongPressDevice, brightness = 100, speed = 50, isPoweredOn = true, statusText }: ProductVisualizerProps) {
  const animValue = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    animValue.stopAnimation();
    
    if (isPoweredOn && (mode === 'PRESETS' || mode === 'RBM' || mode === 'MUSIC' || mode === 'FIXED')) {
      animValue.setValue(0);
      const baseDuration = (mode === 'MUSIC') ? 800 : (mode === 'RBM' ? 2000 : (mode === 'FIXED' ? 1500 : 3000));
      // Speed 0 = 5x slower, Speed 100 = 0.4x faster
      const duration = baseDuration / (0.4 + (speed / 100) * 2.1); 
      
      Animated.loop(
        Animated.timing(animValue, {
          toValue: 1,
          duration: duration,
          useNativeDriver: false, 
        })
      ).start();
    } else {
      animValue.setValue(1);
    }
  }, [product, mode, color, patternId, speed, isPoweredOn]);

  // Fallback if no specific devices array is provided: construct one or two devices based on isPaired flag
  const renderDevices = (devices && devices.length > 0) ? devices : (
    isPaired 
      ? [{ name: `${product} Left`, type: product, points }, { name: `${product} Right`, type: product, points }]
      : [{ name: product, type: product, points }]
  );

  return (
    <View style={styles.container}>
      {statusText && (
        <View style={{ width: '100%', alignItems: 'center', marginBottom: 6 }}>
          <Text style={{ color: 'white', opacity: 0.8, fontSize: 10, fontWeight: 'bold', letterSpacing: 1.5, textTransform: 'uppercase' }}>
            {statusText}
          </Text>
        </View>
      )}
      <View style={{ flexDirection: 'row', justifyContent: 'center', alignItems: 'center', flexWrap: 'wrap' }}>
         {renderDevices.map((dev, index) => (
           <VisualizerUnit 
             key={dev.id || index.toString()} 
             device={dev}
             color={color}
             mode={mode}
             patternId={patternId}
             animValue={animValue}
             fallbackProduct={product}
             fallbackPoints={points}
             fixedFgColor={fixedFgColor}
             fixedBgColor={fixedBgColor}
             onLongPress={onLongPressDevice}
             brightness={brightness}
             isPoweredOn={isPoweredOn}
           />
         ))}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: 6,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#000000', 
    borderRadius: 20,
    marginBottom: 0,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.12)',
    minHeight: 110,
    width: '100%',
  },
  haloBase: {
    width: 88, height: 132,
  },
  soulBase: {
    width: 77, height: 165,
  },
  ledDot: {
    width: 12,
    height: 12,
    borderRadius: 6,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 1,
    shadowRadius: 12,
    elevation: 8,
  },
  ledDotSmall: {
    width: 10,
    height: 10,
    borderRadius: 5,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 1,
    shadowRadius: 10,
    elevation: 6,
  }
});
