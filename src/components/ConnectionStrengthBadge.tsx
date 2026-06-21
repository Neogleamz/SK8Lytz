import React from 'react';
import { View, StyleSheet } from 'react-native';
import type { StyleProp, ViewStyle } from 'react-native';
import { RSSI_WEAK_THRESHOLD, RSSI_CRITICAL_THRESHOLD } from '../hooks/ble/useBLERSSIMonitor';

// ── Signal tier colours ───────────────────────────────────────────────────────
const COLOUR_EXCELLENT = '#4CAF50'; // ≥ -60 dBm  — green
const COLOUR_GOOD      = '#FFC107'; // -60 to -75 — amber
const COLOUR_WEAK      = '#FF6B35'; // -75 to -82 — orange
const COLOUR_CRITICAL  = '#F44336'; // < -82 dBm  — red
const COLOUR_INACTIVE  = '#3A3A3A'; // unfilled bar

interface SignalTier {
  bars: number; // 1–3 bars filled
  colour: string;
  label: string;
}

function getSignalTier(rssi: number): SignalTier {
  if (rssi >= -60) return { bars: 3, colour: COLOUR_EXCELLENT, label: 'Excellent' };
  if (rssi >= RSSI_WEAK_THRESHOLD) return { bars: 2, colour: COLOUR_GOOD, label: 'Good' };
  if (rssi >= RSSI_CRITICAL_THRESHOLD) return { bars: 1, colour: COLOUR_WEAK, label: 'Weak' };
  return { bars: 1, colour: COLOUR_CRITICAL, label: 'Critical' };
}

export interface ConnectionStrengthBadgeProps {
  /** Live RSSI in dBm. Badge is hidden when null/undefined. */
  rssi?: number | null;
  style?: StyleProp<ViewStyle>;
}

/**
 * ConnectionStrengthBadge — 3-bar BLE signal strength indicator.
 *
 * Renders three stacked vertical bars (like a mobile signal icon) coloured
 * according to RSSI tier. No SVG dependency — pure View rectangles.
 * Hidden when rssi is null/undefined (device freshly connected, no poll yet).
 *
 * Designed for overlay inside DeviceItem — small (18×14 pt footprint).
 */
// TODO(i18n): Replace with global i18n.t when framework is adopted
const t_i18n = (key: string) => key;

export function ConnectionStrengthBadge({ rssi, style }: ConnectionStrengthBadgeProps) {
  if (rssi == null) return null;

  const { bars, colour, label } = getSignalTier(rssi);

  return (
    <View 
      style={[styles.container, style]}
      accessible={true}
      accessibilityRole="image"
      accessibilityLabel={t_i18n(`Signal strength: ${label}`)}
    >
      {/* Three bars, left=shortest, right=tallest */}
      <View style={[styles.bar, styles.bar1, bars >= 1 ? { backgroundColor: colour } : styles.barInactive]} />
      <View style={[styles.bar, styles.bar2, bars >= 2 ? { backgroundColor: colour } : styles.barInactive]} />
      <View style={[styles.bar, styles.bar3, bars >= 3 ? { backgroundColor: colour } : styles.barInactive]} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'flex-end',
    gap: 2,
    width: 18,
    height: 14,
  },
  bar: {
    flex: 1,
    borderRadius: 1,
  },
  barInactive: {
    backgroundColor: COLOUR_INACTIVE,
  },
  // Heights: short → medium → tall (signal-icon convention)
  bar1: { height: 5 },
  bar2: { height: 9 },
  bar3: { height: 14 },
});
