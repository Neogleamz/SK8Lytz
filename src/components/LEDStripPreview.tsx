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
  const [frame, setFrame] = useState<RGB[]>([]);

  // Frame-diff guard: prevents calling setFrame when the pixel array is identical.
  const prevFrameRef = useRef<string>('');

  useEffect(() => {
    if (!autoPlay) return;
    const interval = setInterval(() => {
      const currentSpeed = speed || 50;
      const tick = (Date.now() % (1000 / currentSpeed * 100)) / (1000 / currentSpeed * 100);
      const rawFrame = getVisualizerFrame(patternId, hexToRgb(fg), hexToRgb(bg), numLEDs, tick, direction);
      
      const bFactor = brightness / 100;
      const nextFrame = bFactor < 1 ? rawFrame.map(c => ({
        r: Math.round(c.r * bFactor),
        g: Math.round(c.g * bFactor),
        b: Math.round(c.b * bFactor)
      })) : rawFrame;

      // Only commit to React state if pixels actually changed this tick
      const serialized = nextFrame.map(c => `${c.r},${c.g},${c.b}`).join('|');
      if (serialized !== prevFrameRef.current) {
        prevFrameRef.current = serialized;
        setFrame(nextFrame);
      }
    }, 50); // 20fps
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
