/**
 * FixedPatternPreviewRow.tsx — Animated LED strip preview row.
 *
 * Renders a horizontal row of color dots that scroll/animate
 * to visualise the active fixed-pattern at the configured speed.
 *
 * Extracted from DockedController.tsx (Phase 1 S4 extraction, 2026-06-18).
 */
import React from 'react';
import { StyleSheet, View } from 'react-native';
import { Spacing } from '../../theme/theme';

interface FixedPatternPreviewRowProps {
  baseDots: string[];
  patternId: number;
  speed: number;
  points?: number;
  segments?: number;
}

const FixedPatternPreviewRow = ({
  baseDots,
  patternId,
  speed,
  points = 16,
  segments = 1,
}: FixedPatternPreviewRowProps) => {
  const [offset, setOffset] = React.useState(0);

  // Extend the 8-element static base array explicitly to match physical point counts natively
  const fullArray = React.useMemo(() => {
    const arr: string[] = [];

    // Hardware repeats the geometric bounds across the segment boundary safely
    const dotsPerSegment = Math.max(1, Math.floor(points / Math.max(1, segments)));

    for (let i = 0; i < points; i++) {
      // Map native segment repetitions safely simulating string data
      const segmentLocalIndex = i % dotsPerSegment;
      arr.push(baseDots[segmentLocalIndex % baseDots.length]);
    }
    return arr;
  }, [baseDots, points, segments]);

  React.useEffect(() => {
    const intervalTime = Math.max(30, 200 - (speed * 1.7));
    const int = setInterval(() => {
      setOffset(o => (o + 1) % fullArray.length);
    }, intervalTime);
    return () => clearInterval(int);
  }, [fullArray.length, speed]);

  const displayedDots = React.useMemo(() => {
    return [...fullArray.slice(fullArray.length - offset), ...fullArray.slice(0, fullArray.length - offset)];
  }, [fullArray, offset]);

  return (
    <View style={styles.container}>
      <View style={styles.row}>
        {displayedDots.slice(0, 10).map((c, i) => (
          <View
            key={i}
            style={[styles.dot, { backgroundColor: c }]}
          />
        ))}
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, marginRight: Spacing.sm, height: 8, overflow: 'hidden' },
  row: { flex: 1, flexDirection: 'row', gap: Spacing.xxs },
  dot: { flex: 1, borderRadius: 4 }
});

export default FixedPatternPreviewRow;
