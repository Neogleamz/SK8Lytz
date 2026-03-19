import React, { useEffect, useRef, useMemo } from 'react';
import { View, StyleSheet, Animated } from 'react-native';

interface ProductVisualizerProps {
  product: 'HALOZ' | 'SOULZ';
  color: string;
  mode: string;
  patternId: number | null;
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

export default function ProductVisualizer({ product, color, mode, patternId }: ProductVisualizerProps) {
  const animValue = useRef(new Animated.Value(0)).current;

  // Total number of discrete addressable pixels to simulate
  const numLeds = product === 'HALOZ' ? 24 : 43;

  useEffect(() => {
    animValue.stopAnimation();
    
    if (mode === 'PRESETS' || mode === 'RBM' || mode === 'MUSIC') {
      animValue.setValue(0);
      const duration = (mode === 'MUSIC') ? 800 : (mode === 'RBM' ? 2000 : 3000);
      
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
  }, [product, mode, color, patternId]);

  // Compute the position and phase of each individual WS2812B LED dot
  const leds = useMemo(() => {
    const list = [];
    for (let i = 0; i < numLeds; i++) {
       const fract = i / numLeds; // Dot position [0-1] around the ring
       
       // Calculate geometry position
       let top = 0; let left = 0;
       if (product === 'HALOZ') {
         // Circle map
         const angle = fract * 2 * Math.PI;
         top = 32 - 3 + Math.sin(angle) * 32;
         left = 32 - 3 + Math.cos(angle) * 32;
       } else {
         // SOULZ map (boot sole outline)
         const angle = fract * 2 * Math.PI;
         top = 55 - 3 + Math.sin(angle) * 55;
         
         // Pinch the middle to create a "waist" shape like a shoe sole
         const verticalPos = Math.sin(angle); // 1 at top, -1 at bottom
         const pinch = 1 - 0.3 * Math.exp(-Math.pow(verticalPos - 0.1, 2) * 5); 
         left = 25 - 3 + (Math.cos(angle) * 25 * pinch);
       }

       let dotColor: any = color;
       let dotOpacity: any = 1;

       if (mode === 'PRESETS') {
          if (product === 'HALOZ') {
            // Rainbow Flow
            const rainbowColors = [0, 1/6, 2/6, 3/6, 4/6, 5/6, 1].map(t => HSLToHex((t - fract + 1) % 1 * 360, 100, 50));
            dotColor = animValue.interpolate({ inputRange: [0, 0.16, 0.33, 0.5, 0.66, 0.83, 1], outputRange: rainbowColors });
          } else {
            // Cyan Pulse
            const ripple = (Math.sin(fract * Math.PI * 2) + 1) / 2;
            dotOpacity = animValue.interpolate({ inputRange: [0, 0.5, 1], outputRange: [0.2 + (ripple * 0.1), 1, 0.2 + (ripple * 0.1)] });
            dotColor = '#00FFFF';
          }
       } else if (mode === 'RBM') {
          // Addressable "Chase" effect with wrap-around logic
          dotOpacity = animValue.interpolate({
             inputRange: [
               0, 
               Math.max(0, fract - 0.15), 
               fract, 
               Math.min(1, fract + 0.15), 
               1
             ],
             outputRange: [0.1, 0.1, 1, 0.1, 0.1]
          });
          
          if (patternId && patternId % 2 === 0) {
            dotColor = animValue.interpolate({ inputRange: [0, 0.5, 1], outputRange: ['#FF00FF', '#00FFFF', '#FF00FF'] });
          } else {
            dotColor = animValue.interpolate({ inputRange: [0, 1], outputRange: ['#FF0000', '#FFFF00'] });
          }
       } else if (mode === 'MUSIC') {
          // Audio Reactive Pulse (Addressable style - expanding from center)
          const distFromCenter = Math.abs(fract - 0.5) * 2; // 0 at center, 1 at ends
          dotOpacity = animValue.interpolate({
             inputRange: [0, 0.5, 1],
             outputRange: [0.1, 1.0 - (distFromCenter * 0.5), 0.1]
          });
          dotColor = '#00FF00'; 
       }

       list.push({
         key: `led_${i}`,
         position: { top, left, position: 'absolute' as const },
         activeColor: dotColor,
         activeOpacity: dotOpacity,
       });
    }
    return list;
  }, [product, mode, color, numLeds, patternId]);

  return (
    <View style={styles.container}>
      <View style={{ flexDirection: 'row' }}>
         {/* Left Unit */}
         <View style={product === 'HALOZ' ? styles.haloBase : styles.soulBase}>
            {leds.map(led => (
               <Animated.View key={led.key} style={[
                  product === 'HALOZ' ? styles.ledDot : styles.ledDotSmall, 
                  led.position, 
                  { backgroundColor: led.activeColor, opacity: led.activeOpacity, shadowColor: led.activeColor }
               ]} />
            ))}
         </View>
         
         {/* Right Unit */}
         <View style={[product === 'HALOZ' ? styles.haloBase : styles.soulBase, { marginLeft: 32 }]}>
            {leds.map(led => (
               <Animated.View key={led.key} style={[
                  product === 'HALOZ' ? styles.ledDot : styles.ledDotSmall, 
                  led.position, 
                  { backgroundColor: led.activeColor, opacity: led.activeOpacity, shadowColor: led.activeColor }
               ]} />
            ))}
         </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: 32,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#050505', // near pitch black for max pop
    borderRadius: 16,
    marginBottom: 20,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.05)',
    minHeight: 180,
  },
  haloBase: {
    width: 64, height: 64,
  },
  soulBase: {
    width: 50, height: 110,
  },
  ledDot: {
    width: 6,
    height: 6,
    borderRadius: 3,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 1,
    shadowRadius: 6,
    elevation: 6,
  },
  ledDotSmall: {
    width: 4,
    height: 4,
    borderRadius: 2,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 1,
    shadowRadius: 4,
    elevation: 4,
  }
});
