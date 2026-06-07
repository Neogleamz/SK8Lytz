import { AccountTabStatsProps } from './types';
import React from 'react';
import { View, Text, ScrollView } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Spacing } from '../../theme/theme';
import SkaterStatsPanel from './SkaterStatsPanel';

export default function AccountTabStats({
  Colors,
  styles,
  statsLoading,
  lifetimeStats,
  recentSessions,
  crews,
  history,
  devices,
}: AccountTabStatsProps) {
  /** Formats seconds into e.g. "1h 23m" */
  const fmtDuration = (sec: number) => {
    const h = Math.floor(sec / 3600);
    const m = Math.floor((sec % 3600) / 60);
    return h > 0 ? `${h}h ${m}m` : `${m}m`;
  };
  const fmtDate = (iso: string) =>
    new Date(iso).toLocaleDateString(undefined, { month: 'short', day: 'numeric' });

  if (statsLoading) {
    return (
      <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center', paddingVertical: Spacing.giant }}>
        <MaterialCommunityIcons name="lightning-bolt" size={32} color={Colors.primary} />
        <Text style={{ color: Colors.textMuted, marginTop: Spacing.md, fontSize: 13 }}>Loading your stats…</Text>
      </View>
    );
  }

  const noData = !lifetimeStats || lifetimeStats.totalSessions === 0;

  return (
    <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
      <SkaterStatsPanel Colors={Colors} />
      
      {/* Overview Stats */}
      <View style={[styles.statsRow, { marginTop: Spacing.sm, marginBottom: Spacing.xl }]}>
        <View style={styles.statCard}>
          <Text style={styles.statNum}>{crews?.length || 0}</Text>
          <Text style={styles.statLabel}>CREWZ</Text>
        </View>
        <View style={styles.statCard}>
          <Text style={styles.statNum}>{history?.length || '—'}</Text>
          <Text style={styles.statLabel}>Sessions</Text>
        </View>
        <View style={styles.statCard}>
          <Text style={styles.statNum}>{devices?.length || 0}</Text>
          <Text style={styles.statLabel}>Devices</Text>
        </View>
      </View>

      {/* ── Crew session history preview ── */}
      {history?.length > 0 && (
        <View style={{ marginBottom: Spacing.xl }}>
          <Text style={styles.sectionHeader}>RECENT CREW SESSIONS</Text>
          {history.slice(0, 5).map((item: import('../../services/TelemetryService.types').SessionData, i: number) => (
            <View key={`h-${i}`} style={styles.historyRow}>
              <View style={[styles.historyDot, { backgroundColor: item.role === 'leader' ? '#FFAA00' : '#00AAFF' }]} />
              <View style={{ flex: 1 }}>
                <Text style={styles.historyName}>{item.session_name}</Text>
                <Text style={styles.historyDate}>{fmtDate(item.joined_at)}</Text>
              </View>
              <Text style={{ fontSize: 12, color: item.role === 'leader' ? '#FFAA00' : '#00AAFF' }}>
                {item.role === 'leader' ? '👑' : '⚡'}
              </Text>
            </View>
          ))}
        </View>
      )}

      {noData ? (
        <View style={{ alignItems: 'center', paddingVertical: Spacing.huge }}>
          <MaterialCommunityIcons name="skate" size={48} color={Colors.textMuted} />
          <Text style={[styles.hint, { textAlign: 'center', marginTop: Spacing.lg, fontSize: 14 }]}>
            No sessions saved yet.{`\n`}Enable Street Mode and skate to record your first session!
          </Text>
        </View>
      ) : (
        <>
          {/* ── Lifetime stat grid ── */}
          <Text style={styles.sectionHeader}>LIFETIME STATS</Text>
          <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.sm, marginBottom: Spacing.lg }}>
            {[
              { icon: 'flag-checkered',        val: String(lifetimeStats!.totalSessions),                        unit: '',    lbl: 'Sessions' },
              { icon: 'map-marker-distance',   val: lifetimeStats!.totalDistanceMiles.toFixed(1),               unit: 'mi',  lbl: 'Distance' },
              { icon: 'speedometer',           val: lifetimeStats!.lifetimePeakSpeedMph.toFixed(1),             unit: 'mph', lbl: 'Record Speed' },
              { icon: 'gauge',                 val: lifetimeStats!.lifetimePeakGForce?.toFixed(1) || '0.0',     unit: 'g',   lbl: 'Peak G-Force' },
              { icon: 'speedometer-medium',    val: lifetimeStats!.lifetimeAvgSpeedMph.toFixed(1),              unit: 'mph', lbl: 'Avg Speed' },
              { icon: 'timer-outline',         val: fmtDuration(lifetimeStats!.totalDurationSec),               unit: '',    lbl: 'Time on Skates' },
              ...(lifetimeStats!.lifetimeCalories > 0 ? [{ icon: 'fire', val: String(lifetimeStats!.lifetimeCalories), unit: 'kcal', lbl: 'Total Burned' }] : []),
              ...(lifetimeStats!.lifetimePeakBpm ? [{ icon: 'heart-pulse', val: String(lifetimeStats!.lifetimePeakBpm), unit: 'bpm', lbl: 'Peak HR' }] : []),
            ].map(({ icon, val, unit, lbl }) => (
              <View key={lbl} style={{
                width: '47%', backgroundColor: 'rgba(255,255,255,0.05)',
                borderRadius: 16, padding: Spacing.lg, alignItems: 'center',
              }}>
                <MaterialCommunityIcons name={icon as keyof typeof MaterialCommunityIcons.glyphMap} size={20} color={Colors.primary} />
                <View style={{ flexDirection: 'row', alignItems: 'flex-end', gap: Spacing.xxs, marginTop: Spacing.sm }}>
                  <Text style={{ color: '#FFF', fontSize: 22, fontWeight: '900' }}>{val}</Text>
                  {unit ? <Text style={{ color: Colors.primary, fontSize: 10, fontWeight: '700', marginBottom: Spacing.xxs }}>{unit}</Text> : null}
                </View>
                <Text style={{ color: 'rgba(255,255,255,0.4)', fontSize: 10, fontWeight: '600', letterSpacing: 0.8, marginTop: Spacing.xs }}>{lbl}</Text>
              </View>
            ))}
          </View>

          {/* ── Recent sessions list ── */}
          {recentSessions.length > 0 && (
            <>
              <Text style={[styles.sectionHeader, { marginTop: Spacing.sm }]}>RECENT SESSIONS</Text>
              {recentSessions.map((s: import('../../services/TelemetryService.types').SessionData) => (
                <View key={s.id} style={{
                  backgroundColor: 'rgba(255,255,255,0.04)',
                  borderRadius: 14, padding: Spacing.lg, marginBottom: Spacing.sm,
                  borderLeftWidth: 3, borderLeftColor: Colors.primary,
                }}>
                  <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginBottom: Spacing.sm }}>
                    <Text style={{ color: '#FFF', fontWeight: '700', fontSize: 14 }}>{fmtDate(s.sessionDate)}</Text>
                    <Text style={{ color: Colors.textMuted, fontSize: 12 }}>{fmtDuration(s.durationSec)}</Text>
                  </View>
                  <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.lg }}>
                    <View style={{ alignItems: 'center', minWidth: 50 }}>
                      <Text style={{ color: '#FFF', fontWeight: '800', fontSize: 16 }}>{s.distanceMiles.toFixed(2)}</Text>
                      <Text style={{ color: Colors.textMuted, fontSize: 10 }}>mi</Text>
                    </View>
                    <View style={{ alignItems: 'center', minWidth: 50 }}>
                      <Text style={{ color: '#FFF', fontWeight: '800', fontSize: 16 }}>{s.peakSpeedMph.toFixed(1)}</Text>
                      <Text style={{ color: Colors.textMuted, fontSize: 10 }}>peak mph</Text>
                    </View>
                    <View style={{ alignItems: 'center', minWidth: 50 }}>
                      <Text style={{ color: '#FFF', fontWeight: '800', fontSize: 16 }}>{s.avgSpeedMph.toFixed(1)}</Text>
                      <Text style={{ color: Colors.textMuted, fontSize: 10 }}>avg mph</Text>
                    </View>
                    {s.peakGForce != null && (
                      <View style={{ alignItems: 'center', minWidth: 50 }}>
                        <Text style={{ color: '#FF6B35', fontWeight: '800', fontSize: 16 }}>{s.peakGForce.toFixed(1)}</Text>
                        <Text style={{ color: Colors.textMuted, fontSize: 10 }}>peak g</Text>
                      </View>
                    )}
                    {s.calories != null && s.calories > 0 && (
                      <View style={{ alignItems: 'center', minWidth: 50 }}>
                        <Text style={{ color: '#FF4444', fontWeight: '800', fontSize: 16 }}>{s.calories}</Text>
                        <Text style={{ color: Colors.textMuted, fontSize: 10 }}>kcal</Text>
                      </View>
                    )}
                    {s.avgBpm != null && (
                      <View style={{ alignItems: 'center', minWidth: 50 }}>
                        <Text style={{ color: '#FF4444', fontWeight: '800', fontSize: 16 }}>{s.avgBpm}</Text>
                        <Text style={{ color: Colors.textMuted, fontSize: 10 }}>avg bpm</Text>
                      </View>
                    )}
                  </View>
                </View>
              ))}
            </>
          )}
        </>
      )}
      <View style={{ height: 30 }} />
    </ScrollView>
  );
}
