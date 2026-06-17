import React, { useEffect, useState, useRef } from 'react';
import { View, Text, ActivityIndicator, ScrollView, StyleSheet } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { supabase } from '../../../services/supabaseClient';
import { AppLogger } from '../../../services/appLogger';
import { ErrorCard } from '../../ErrorCard';
import type { ViewState } from '../../../types/ViewState';

interface GlobalAnalyticsSummary {
  fleet_total_distance_meters?: number;
  fleet_total_app_time_sec?: number;
  fleet_total_street_sessions?: number;
}

export default function GlobalAnalyticsPanel({ Colors }: { Colors: Record<string, string> }) {
  const [data, setData] = useState<GlobalAnalyticsSummary | null>(null);
  /** 4-state FSM: idle → loading → success/empty/error */
  const [viewState, setViewState] = useState<ViewState>('loading');
  const [errorMsg, setErrorMsg] = useState('');
  const isMountedRef = useRef(true);

  useEffect(() => {
    isMountedRef.current = true;
    return () => { isMountedRef.current = false; };
  }, []);

  const loadGlobalStats = async () => {
    if (!supabase) return;
    try {
      setViewState('loading');
      setErrorMsg('');
      const { data: rawData, error } = await supabase.rpc('admin_get_global_telemetry');
      if (error) throw error;
      if (!isMountedRef.current) return;
      if (rawData && typeof rawData === 'object') {
        const obj = rawData as Record<string, unknown>;
        setData({
          fleet_total_distance_meters: typeof obj.fleet_total_distance_meters === 'number' ? obj.fleet_total_distance_meters : undefined,
          fleet_total_app_time_sec: typeof obj.fleet_total_app_time_sec === 'number' ? obj.fleet_total_app_time_sec : undefined,
          fleet_total_street_sessions: typeof obj.fleet_total_street_sessions === 'number' ? obj.fleet_total_street_sessions : undefined,
        });
        setViewState('success');
      } else {
        setViewState('empty');
      }
    } catch (e: unknown) {
      if (!isMountedRef.current) return;
      const err = e instanceof Error ? e : new Error(String(e));
      AppLogger.error('[GlobalAnalytics] RPC failed', err, { payload_size: 0, ssi: 0 });
      setErrorMsg('Failed to load. Tap to retry.');
      setViewState('error');
    }
  };

  useEffect(() => {
    loadGlobalStats();
  }, []);

  if (viewState === 'loading') {
    return (
      <View style={{ padding: 40, alignItems: 'center' }}>
        <ActivityIndicator color={Colors.primary} size="large" />
      </View>
    );
  }

  if (viewState === 'error') {
    return <ErrorCard message={errorMsg} onRetry={loadGlobalStats} />;
  }

  const distance = data?.fleet_total_distance_meters 
    ? (data.fleet_total_distance_meters / 1609.34).toFixed(1) 
    : '0';
  const hours = data?.fleet_total_app_time_sec 
    ? Math.floor(data.fleet_total_app_time_sec / 3600) 
    : 0;
  const sessions = data?.fleet_total_street_sessions || 0;

  return (
    <ScrollView style={{ flex: 1 }} showsVerticalScrollIndicator={false}>
      <View style={{ marginBottom: 16 }}>
        <Text style={[styles.headerText, { color: Colors.textMuted }]}>
          FLEET ANALYTICS (GLOBAL)
        </Text>
        <Text style={[styles.subText, { color: Colors.textMuted }]}>
          Aggregated lifetime metrics from all connected users.
        </Text>
      </View>

      <View style={styles.grid}>
        <View style={[styles.card, { backgroundColor: 'rgba(255,255,255,0.05)' }]}>
          <MaterialCommunityIcons name="skate" size={24} color={Colors.primary} />
          <Text style={[styles.value, { color: Colors.text }]}>{distance}</Text>
          <Text style={[styles.label, { color: Colors.textMuted }]}>TOTAL MILES</Text>
        </View>
        <View style={[styles.card, { backgroundColor: 'rgba(255,255,255,0.05)' }]}>
          <MaterialCommunityIcons name="timer-sand" size={24} color={Colors.primary} />
          <Text style={[styles.value, { color: Colors.text }]}>{hours}h</Text>
          <Text style={[styles.label, { color: Colors.textMuted }]}>SKATED TIME</Text>
        </View>
        <View style={[styles.card, { backgroundColor: 'rgba(255,255,255,0.05)' }]}>
          <MaterialCommunityIcons name="map-marker-path" size={24} color={Colors.primary} />
          <Text style={[styles.value, { color: Colors.text }]}>{sessions}</Text>
          <Text style={[styles.label, { color: Colors.textMuted }]}>STREET SESSIONS</Text>
        </View>
      </View>
      
      {/* Pattern tracking placeholder - we can extend the RPC to aggregate all patterns in the future */}
      <View style={[styles.insightCard, { backgroundColor: 'rgba(255,255,255,0.05)' }]}>
        <Text style={[styles.insightHeader, { color: Colors.text }]}>Feature Adoption</Text>
        <Text style={[styles.insightText, { color: Colors.textMuted }]}>
          Pattern heatmaps and UI conversion rates are automatically tracked in the database and will be exposed here in the next analytics update.
        </Text>
      </View>

      <View style={{ height: 40 }} />
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  headerText: {
    fontSize: 11,
    fontWeight: '800',
    letterSpacing: 1.5,
  },
  subText: {
    fontSize: 12,
    marginTop: 4,
  },
  grid: {
    flexDirection: 'row',
    gap: 12,
    marginBottom: 20,
  },
  card: {
    flex: 1,
    borderRadius: 14,
    padding: 16,
    alignItems: 'center',
  },
  value: {
    fontSize: 20,
    fontWeight: '900',
    marginTop: 8,
  },
  label: {
    fontSize: 10,
    fontWeight: '700',
    marginTop: 4,
  },
  insightCard: {
    padding: 16,
    borderRadius: 14,
    borderLeftWidth: 3,
    borderLeftColor: '#FFAA00',
  },
  insightHeader: {
    fontSize: 14,
    fontWeight: '700',
    marginBottom: 8,
  },
  insightText: {
    fontSize: 12,
    lineHeight: 18,
  }
});
