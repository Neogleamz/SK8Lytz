import { MaterialCommunityIcons } from '@expo/vector-icons';
import React from 'react';
import { Text, View } from 'react-native';
import { Spacing } from '../../theme/theme';

export interface CrewDetailStatsProps {
  stats: any;
  styles: any;
  Colors: any;
}

export const CrewDetailStats = ({ stats, styles, Colors }: CrewDetailStatsProps) => {
  const lastActiveStr = stats?.lastActive
    ? new Date(stats.lastActive).toLocaleDateString(undefined, { month: 'short', day: 'numeric', year: 'numeric' })
    : null;

  const fmtDuration = (sec: number) => {
    const h = Math.floor(sec / 3600);
    const m = Math.floor((sec % 3600) / 60);
    return h > 0 ? `${h}h ${m}m` : `${m}m`;
  };

  return (
    <View style={{ marginBottom: Spacing.xl }}>
      <Text style={styles.label}>CREW STATS</Text>
      <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.sm, marginBottom: Spacing.lg }}>
        {[
          { icon: 'flag-checkered', val: String(stats?.sessionCount ?? 0), unit: '', lbl: 'Sessions' },
          { icon: 'map-marker-distance', val: stats?.totalDistanceMiles?.toFixed(1) ?? '0.0', unit: 'mi', lbl: 'Distance' },
          { icon: 'speedometer', val: stats?.peakSpeedMph?.toFixed(1) ?? '0.0', unit: 'mph', lbl: 'Top Speed' },
          { icon: 'gauge', val: stats?.peakGForce?.toFixed(1) ?? '0.0', unit: 'g', lbl: 'Peak G-Force' },
          { icon: 'speedometer-medium', val: stats?.avgSpeedMph?.toFixed(1) ?? '0.0', unit: 'mph', lbl: 'Avg Speed' },
          { icon: 'timer-outline', val: fmtDuration(stats?.totalDurationSec ?? 0), unit: '', lbl: 'Time on Skates' },
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

      <Text style={styles.label}>ACTIVITY INFO</Text>
      <View style={styles.statsRow}>
        <View style={[styles.statCard, { flex: 1 }]}>
          <Text style={styles.statNum} numberOfLines={1}>{lastActiveStr ?? '—'}</Text>
          <Text style={styles.statLabel}>Last Active</Text>
        </View>
        <View style={[styles.statCard, { flex: 1.4 }]}>
          <Text style={styles.statNum} numberOfLines={1}>{stats?.topScene ?? '—'}</Text>
          <Text style={styles.statLabel}>Top Scene</Text>
        </View>
      </View>
    </View>
  );
};
