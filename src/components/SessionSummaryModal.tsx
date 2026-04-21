import { Spacing } from '../theme/theme';
/**
 * SessionSummaryModal.tsx — SK8Lytz Post-Session Debrief
 *
 * Displayed after the user taps "Save Session" in Street Mode.
 * Shows a visually rich breakdown of the completed skate and
 * prompts the user to save or discard.
 *
 * Design:
 *  - Dark glassmorphism card with speed-zone accent colour
 *  - 4 large metric tiles (distance, peak speed, avg speed, g-force)
 *  - Calorie estimate and session duration
 *  - Save / Discard CTA row
 *
 * Platform: React Native (Android + iOS) + Web-safe
 */
import { MaterialCommunityIcons } from '@expo/vector-icons';
import React, { useCallback } from 'react';
import {
    Modal,
    Platform,
    StyleSheet,
    Text,
    TouchableOpacity,
    View,
} from 'react-native';
import { useTheme } from '../context/ThemeContext';
import type { ISessionSnapshot } from '../services/SpeedTrackingService';

// ── Helpers ────────────────────────────────────────────────────────────────────

function formatDuration(sec: number): string {
  const m = Math.floor(sec / 60);
  const s = Math.round(sec % 60);
  return `${m}:${s.toString().padStart(2, '0')}`;
}

function formatDistance(miles: number): string {
  return miles.toFixed(2);
}

/** Maps a peak speed to a colour theme for the session card accent. */
function speedAccentColor(peakMph: number): string {
  if (peakMph >= 18) return '#FF3D00'; // inferno — top tier
  if (peakMph >= 12) return '#FF8C00'; // SK8Lytz orange — cruising fast
  if (peakMph >= 6)  return '#00E676'; // neon green — moderate
  return '#00B0FF';                    // cool blue  — chill session
}

function estimateCalories(avgSpeedMph: number, durationSec: number): number {
  const MET = avgSpeedMph > 12 ? 12 : avgSpeedMph > 8 ? 9 : 7;
  return Math.round(MET * 70 * (durationSec / 3600));
}

// ── Types ──────────────────────────────────────────────────────────────────────

interface SessionSummaryModalProps {
  visible: boolean;
  snapshot: ISessionSnapshot | null;
  onSave: () => void;
  onDiscard: () => void;
}

// ── Metric tile ───────────────────────────────────────────────────────────────

interface MetricTileProps {
  icon: string;
  value: string;
  unit: string;
  label: string;
  accentColor: string;
}

function MetricTile({ icon, value, unit, label, accentColor }: MetricTileProps) {
  return (
    <View style={tileStyles.container}>
      <MaterialCommunityIcons name={icon as any} size={20} color={accentColor} />
      <View style={tileStyles.valueRow}>
        <Text style={tileStyles.value}>{value}</Text>
        <Text style={[tileStyles.unit, { color: accentColor }]}>{unit}</Text>
      </View>
      <Text style={tileStyles.label}>{label}</Text>
    </View>
  );
}

const tileStyles = StyleSheet.create({
  container: {
    flex: 1, alignItems: 'center', justifyContent: 'center',
    backgroundColor: 'rgba(255,255,255,0.05)',
    borderRadius: 16, padding: Spacing.md, margin: Spacing.xs, minHeight: 88,
  },
  valueRow: { flexDirection: 'row', alignItems: 'flex-end', gap: Spacing.xxs, marginTop: Spacing.xs },
  value:    { color: '#FFF', fontSize: 24, fontWeight: '900', letterSpacing: -0.5 },
  unit:     { fontSize: 11, fontWeight: '700', marginBottom: Spacing.xxs },
  label:    { color: 'rgba(255,255,255,0.5)', fontSize: 10, fontWeight: '600',
              letterSpacing: 0.8, marginTop: Spacing.xs, textAlign: 'center' },
});

// ── Main component ─────────────────────────────────────────────────────────────

export default function SessionSummaryModal({
  visible, snapshot, onSave, onDiscard,
}: SessionSummaryModalProps) {
  const { Colors } = useTheme();

  const accentColor = snapshot ? speedAccentColor(snapshot.peakSpeedMph) : '#FF8C00';
  const calories = snapshot
    ? estimateCalories(snapshot.avgSpeedMph, snapshot.durationSec)
    : 0;

  const handleSave = useCallback(() => { onSave(); }, [onSave]);
  const handleDiscard = useCallback(() => { onDiscard(); }, [onDiscard]);

  if (!snapshot) return null;

  return (
    <Modal visible={visible} animationType="slide" transparent statusBarTranslucent>
      <View style={styles.overlay}>
        <View style={[styles.card, { borderColor: accentColor }]}>

          {/* Header */}
          <View style={styles.headerRow}>
            <MaterialCommunityIcons name="flag-checkered" size={24} color={accentColor} />
            <Text style={styles.title}>Session Complete</Text>
            <MaterialCommunityIcons name="flag-checkered" size={24} color={accentColor} />
          </View>

          {/* Duration pill */}
          <View style={[styles.durationPill, { backgroundColor: accentColor + '25' }]}>
            <MaterialCommunityIcons name="timer-outline" size={14} color={accentColor} />
            <Text style={[styles.durationText, { color: accentColor }]}>
              {formatDuration(snapshot.durationSec)}
            </Text>
          </View>

          {/* Primary metrics grid */}
          <View style={styles.grid}>
            <MetricTile
              icon="map-marker-distance"
              value={formatDistance(snapshot.distanceMiles)}
              unit="mi"
              label="DISTANCE"
              accentColor={accentColor}
            />
            <MetricTile
              icon="speedometer"
              value={snapshot.peakSpeedMph.toFixed(1)}
              unit="mph"
              label="PEAK SPEED"
              accentColor={accentColor}
            />
          </View>
          <View style={styles.grid}>
            <MetricTile
              icon="gauge"
              value={snapshot.avgSpeedMph.toFixed(1)}
              unit="mph"
              label="AVG SPEED"
              accentColor={accentColor}
            />
            <MetricTile
              icon="axis-z-rotate-clockwise"
              value={snapshot.peakGForce.toFixed(2)}
              unit="G"
              label="PEAK G-FORCE"
              accentColor={accentColor}
            />
          </View>

          {/* Calorie estimate */}
          <View style={styles.calorieRow}>
            <MaterialCommunityIcons name="fire" size={16} color="#FF6B35" />
            <Text style={styles.calorieText}>≈ {calories} kcal burned</Text>
          </View>

          {/* CTA buttons */}
          <View style={styles.ctaRow}>
            <TouchableOpacity style={styles.discardBtn} onPress={handleDiscard} activeOpacity={0.8}>
              <Text style={styles.discardText}>Discard</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={[styles.saveBtn, { backgroundColor: accentColor }]}
              onPress={handleSave}
              activeOpacity={0.85}
            >
              <MaterialCommunityIcons name="content-save" size={16} color="#000" />
              <Text style={styles.saveText}>Save Session</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    </Modal>
  );
}

// ── Styles ─────────────────────────────────────────────────────────────────────

const styles = StyleSheet.create({
  overlay: {
    flex: 1, backgroundColor: 'rgba(0,0,0,0.85)',
    justifyContent: 'center', alignItems: 'center', padding: Spacing.xl,
  },
  card: {
    width: '100%', maxWidth: 400,
    backgroundColor: '#111',
    borderRadius: 24, borderWidth: 1.5,
    padding: Spacing.xl,
    ...Platform.select({
      web: { boxShadow: '0px 0px 30px rgba(0,0,0,0.8)' } as any,
      default: { shadowColor: '#000000', shadowOffset: { width: 0, height: 0 }, shadowOpacity: 0.8, shadowRadius: 30, elevation: 20 }
    }),
  },
  headerRow: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: Spacing.md, marginBottom: Spacing.md,
  },
  title: {
    color: '#FFF', fontSize: 22, fontWeight: '900', letterSpacing: 0.5,
  },
  durationPill: {
    flexDirection: 'row', alignItems: 'center', gap: Spacing.sm,
    alignSelf: 'center', paddingHorizontal: Spacing.lg, paddingVertical: Spacing.sm,
    borderRadius: 999, marginBottom: Spacing.lg,
  },
  durationText: { fontSize: 15, fontWeight: '800', letterSpacing: 1 },
  grid:   { flexDirection: 'row', marginBottom: 0 },
  calorieRow: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: Spacing.sm,
    marginTop: Spacing.lg, marginBottom: Spacing.sm,
  },
  calorieText: { color: 'rgba(255,255,255,0.6)', fontSize: 13, fontWeight: '600' },
  ctaRow: { flexDirection: 'row', gap: Spacing.md, marginTop: Spacing.xl },
  discardBtn: {
    flex: 1, paddingVertical: Spacing.lg, borderRadius: 14,
    backgroundColor: 'rgba(255,255,255,0.07)',
    alignItems: 'center', justifyContent: 'center',
  },
  discardText: { color: 'rgba(255,255,255,0.5)', fontSize: 15, fontWeight: '700' },
  saveBtn: {
    flex: 2, paddingVertical: Spacing.lg, borderRadius: 14,
    flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: Spacing.sm,
  },
  saveText: { color: '#000', fontSize: 15, fontWeight: '900' },
});
