import React, { useMemo, useState, useEffect } from 'react';
import { View } from 'react-native';
import { ZenggeVisualizerMath } from '../protocols/ZenggeVisualizerMath';

interface CustomEffectVisualizerProps {
  effectId: number;
  speed: number;
  fgColorHex?: string;
  bgColorHex?: string;
  points?: number;
  segments?: number;
  direction?: boolean;
}

const hexToRgb = (hex: string) => {
  const h = hex || '#000000';
  return {
    r: parseInt(h.substring(1, 3), 16) || 0,
    g: parseInt(h.substring(3, 5), 16) || 0,
    b: parseInt(h.substring(5, 7), 16) || 0,
  };
};

export const CustomEffectVisualizer: React.FC<CustomEffectVisualizerProps> = ({ 
  effectId, 
  speed, 
  fgColorHex = '#FF0000', 
  bgColorHex = '#000000',
  points = 16,
  segments = 1,
  direction = true
}) => {
  const [tick, setTick] = useState(0);

  useEffect(() => {
    // 0x51 and 0x59 modes both map 1-100 speed slider differently natively,
    // but visually we approximate it as:
    const frameRate = Math.max(16, 200 - (speed * 1.8)); 
    const tickIncrement = speed / 2000; // Faster speed = higher tick step
    
    let currentTick = 0;
    const interval = setInterval(() => {
      currentTick = (currentTick + tickIncrement) % 1;
      setTick(currentTick);
    }, frameRate);
    
    return () => clearInterval(interval);
  }, [speed]);

  const displayedDots = useMemo(() => {
    const fgRgb = hexToRgb(fgColorHex);
    const bgRgb = hexToRgb(bgColorHex);
    const base16 = ZenggeVisualizerMath.getVisualizerDots(effectId, fgRgb, bgRgb, tick, direction, segments > 1);
    
    // Stretch the 16 native hardware visualizer buffer into the actual hardware rendering 
    // constraints by slicing or repeating based on segments.
    // For now we slice exactly the number of points so it fits the LED box exactly.
    const arr: string[] = [];
    const dotsPerSegment = Math.max(1, Math.floor(points / Math.max(1, segments)));
    
    for (let i = 0; i < points; i++) {
       const segmentLocalIndex = i % dotsPerSegment;
       const colorObj = base16[segmentLocalIndex % 16];
       arr.push(`rgb(${colorObj.r}, ${colorObj.g}, ${colorObj.b})`);
    }
    
    return arr;
  }, [effectId, fgColorHex, bgColorHex, tick, points, segments, direction]);

  return (
    <View style={{ flex: 1, marginRight: 8, height: 8, overflow: 'hidden' }}>
      <View style={{ flex: 1, flexDirection: 'row', gap: 2 }}>
        {displayedDots.slice(0, 10).map((c, i) => (
          <View 
             key={i} 
             style={{ 
               flex: 1,
               borderRadius: 4, 
               backgroundColor: c
             }} 
          />
        ))}
      </View>
    </View>
  );
};

export default CustomEffectVisualizer;
