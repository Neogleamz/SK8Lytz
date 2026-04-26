import React, { useEffect, useRef, useState } from 'react';
import { View, ViewStyle } from 'react-native';
import { getVisualizerFrame, RGB } from '../protocols/PatternEngine';
import { hexToRgb } from '../utils/ColorUtils';

interface LEDStripPreviewProps {
  // Source: PatternEngine only
  patternId: number;             // 1-45

  // Colors
  fg: string;                   // hex color
  bg: string;                   // hex color

  // Hardware
  numLEDs: number;              // from hwSettings.ledPoints

  // Animation
  speed?: number;               // 1-100
  brightness?: number;          // 0-100
  direction?: 0 | 1;
  autoPlay?: boolean;           // default true

  // Display
  dotSize?: number;             // default 6px
  height?: number;              // default 16px
  style?: ViewStyle;
}

export const LEDStripPreview = React.memo(({ patternId, fg, bg, numLEDs, speed, brightness = 100, direction = 1, autoPlay = true, dotSize = 6, height = 16, style }: LEDStripPreviewProps) => {
  // Sync initial frame at t=0 so cards render immediately (no blank flash).
  // Static patterns are complete at t=0 and never need the interval.
  const bFactor = brightness / 100;
  const [frame, setFrame] = useState<RGB[]>(() => {
    const raw = getVisualizerFrame(patternId, hexToRgb(fg), hexToRgb(bg), numLEDs, 0, direction);
    return bFactor < 1 ? raw.map(c => ({ r: Math.round(c.r * bFactor), g: Math.round(c.g * bFactor), b: Math.round(c.b * bFactor) })) : raw;
  });

  // Position-sensitive hash: (i+1) weighting catches translations (chases/marquees).
  const prevHashRef = useRef<number>(
    frame.reduce((acc, c, i) => acc + (i + 1) * (c.r * 65536 + c.g * 256 + c.b), 0)
  );

  useEffect(() => {
    if (!autoPlay) return;
    const interval = setInterval(() => {
      const currentSpeed = speed || 50;
      const tick = (Date.now() % (1000 / currentSpeed * 100)) / (1000 / currentSpeed * 100);
      const raw = getVisualizerFrame(patternId, hexToRgb(fg), hexToRgb(bg), numLEDs, tick, direction);
      const next = bFactor < 1 ? raw.map(c => ({ r: Math.round(c.r * bFactor), g: Math.round(c.g * bFactor), b: Math.round(c.b * bFactor) })) : raw;

      const hash = next.reduce((acc, c, i) => acc + (i + 1) * (c.r * 65536 + c.g * 256 + c.b), 0);
      if (hash !== prevHashRef.current) {
        prevHashRef.current = hash;
        setFrame(next);
      }
    }, 50); // 20fps — original smooth rate; viewport gate caps active cards to ~8
    return () => clearInterval(interval);
  }, [patternId, fg, bg, numLEDs, speed, brightness, direction, autoPlay]);

  return (
    <View style={[{ height, flexDirection: 'row', borderRadius: 4, overflow: 'hidden' }, style]}>
      {frame.map((c, i) => (
        <View key={i} style={{ flex: 1, backgroundColor: `rgb(${c.r},${c.g},${c.b})` }} />
      ))}
    </View>
  );
});
