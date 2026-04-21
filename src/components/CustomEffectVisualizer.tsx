import React, { useEffect, useMemo, useState } from 'react';
import { View } from 'react-native';
import { getVisualizerFrame } from '../protocols/PatternEngine';
import { Spacing } from '../theme/theme';

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
    
    // Leverage the new PatternEngine which dynamically tiles and animates
    // the mathematical array to match exactly the requested points.
    const colors = getVisualizerFrame(effectId, fgRgb, bgRgb, points, tick);
    
    return colors.map(c => `rgb(${c.r}, ${c.g}, ${c.b})`);
  }, [effectId, fgColorHex, bgColorHex, tick, points]);

  return (
    <View style={{ flex: 1, marginRight: Spacing.sm, height: 8, overflow: 'hidden' }}>
      <View style={{ flex: 1, flexDirection: 'row', gap: Spacing.xxs }}>
        {displayedDots.slice(0, 10).map((c, i) => (
          <View 
             key={i} 
             style={{ 
               flex: 1,
               borderRadius: 1000, 
               backgroundColor: c
             }} 
          />
        ))}
      </View>
    </View>
  );
};

export default CustomEffectVisualizer;
