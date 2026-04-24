import React from 'react';
import { View, Text, ScrollView } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Spacing } from '../../theme/theme';

export default function AccountTabStats({
  Colors,
  styles,
  statsLoading,
  lifetimeStats,
  recentSessions,
}: any) {
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
              { icon: 'gauge',                 val: lifetimeStats!.lifetimeAvgSpeedMph.toFixed(1),              unit: 'mph', lbl: 'Avg Speed' },
              { icon: 'timer-outline',         val: fmtDuration(lifetimeStats!.totalDurationSec),               unit: '',    lbl: 'Time on Skates' },
              { icon: 'fire',                  val: String(lifetimeStats!.lifetimeCalories),                    unit: 'kcal',lbl: 'Calories' },
            ].map(({ icon, val, unit, lbl }) => (
              <View key={lbl} style={{
                width: '47%', backgroundColor: 'rgba(255,255,255,0.05)',
                borderRadius: 16, padding: Spacing.lg, alignItems: 'center',
              }}>
                <MaterialCommunityIcons name={icon as any} size={20} color={Colors.primary} />
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
              {recentSessions.map((s: any) => (
                <View key={s.id} style={{
                  backgroundColor: 'rgba(255,255,255,0.04)',
                  borderRadius: 14, padding: Spacing.lg, marginBottom: Spacing.sm,
                  borderLeftWidth: 3, borderLeftColor: Colors.primary,
                }}>
                  <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginBottom: Spacing.sm }}>
                    <Text style={{ color: '#FFF', fontWeight: '700', fontSize: 14 }}>{fmtDate(s.sessionDate)}</Text>
                    <Text style={{ color: Colors.textMuted, fontSize: 12 }}>{fmtDuration(s.durationSec)}</Text>
                  </View>
                  <View style={{ flexDirection: 'row', gap: Spacing.lg }}>
                    <View style={{ alignItems: 'center' }}>
                      <Text style={{ color: '#FFF', fontWeight: '800', fontSize: 16 }}>{s.distanceMiles.toFixed(2)}</Text>
                      <Text style={{ color: Colors.textMuted, fontSize: 10 }}>mi</Text>
                    </View>
                    <View style={{ alignItems: 'center' }}>
                      <Text style={{ color: '#FFF', fontWeight: '800', fontSize: 16 }}>{s.peakSpeedMph.toFixed(1)}</Text>
                      <Text style={{ color: Colors.textMuted, fontSize: 10 }}>peak mph</Text>
                    </View>
                    <View style={{ alignItems: 'center' }}>
                      <Text style={{ color: '#FFF', fontWeight: '800', fontSize: 16 }}>{s.avgSpeedMph.toFixed(1)}</Text>
                      <Text style={{ color: Colors.textMuted, fontSize: 10 }}>avg mph</Text>
                    </View>
                    {s.calories != null && (
                      <View style={{ alignItems: 'center' }}>
                        <Text style={{ color: '#FF6B35', fontWeight: '800', fontSize: 16 }}>{s.calories}</Text>
                        <Text style={{ color: Colors.textMuted, fontSize: 10 }}>kcal</Text>
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
