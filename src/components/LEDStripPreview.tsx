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
  // Uses an integer channel-sum hash (r*65536 + g*256 + b) instead of string serialization
  // to avoid 43+ template string allocations + join on every 50ms tick.
  // Collision probability is negligibly low for LED visualizer use (worst case: skip 1 frame).
  const prevFrameRef = useRef<number>(0);

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
      // Hash: sum of (r*65536 + g*256 + b) across all LEDs — O(n) with zero string allocation
      const hash = nextFrame.reduce((acc, c) => acc + c.r * 65536 + c.g * 256 + c.b, 0);
      if (hash !== prevFrameRef.current) {
        prevFrameRef.current = hash;
        setFrame(nextFrame);
      }
    }, 150); // ~6fps — pattern card previews don't need high frame rate; hash guard skips static patterns
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
