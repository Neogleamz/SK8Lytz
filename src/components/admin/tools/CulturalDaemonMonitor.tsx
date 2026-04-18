import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, Modal, SafeAreaView, TouchableOpacity, ActivityIndicator } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { supabase } from '../../../utils/supabase';
import { Spacing } from '../../../theme/theme';

interface CulturalDaemonMonitorProps {
  visible: boolean;
  onClose: () => void;
  bg: string;
  cardBg: string;
  borderColor: string;
  textPrimary: string;
  textMuted: string;
}

interface DaemonStatus {
  status: 'ONLINE' | 'SLEEPING' | 'ERROR' | 'OFFLINE';
  current_target: string | null;
  total_enriched: number;
  total_denials: number;
  last_heartbeat: string | null;
  last_error: string | null;
}

const DAEMON_ID = '00000000-0000-0000-0000-000000000000';

export function CulturalDaemonMonitor({ visible, onClose, bg, cardBg, borderColor, textPrimary, textMuted }: CulturalDaemonMonitorProps) {
  const [data, setData] = useState<DaemonStatus | null>(null);
  const [loading, setLoading] = useState(true);

  const fetchData = async () => {
    try {
      const { data: row, error } = await supabase
        .from('daemon_status')
        .select('*')
        .eq('id', DAEMON_ID)
        .single();
        
      if (!error && row) {
        setData(row as DaemonStatus);
      }
    } catch(e) {
      console.warn("Failed to fetch daemon pulse", e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (visible) {
      fetchData();
      const interval = setInterval(fetchData, 10000); // Poll every 10 seconds
      return () => clearInterval(interval);
    }
  }, [visible]);

  const getStatusColor = (status: string) => {
    if (status === 'ONLINE') return '#00f0ff'; // Cyan
    if (status === 'SLEEPING') return '#00E676'; // Muted Green
    if (status === 'ERROR') return '#ff4040'; // Red
    return '#888888'; // Offline
  };

  const getStatusIcon = (status: string) => {
    if (status === 'ONLINE') return 'robot-outline';
    if (status === 'SLEEPING') return 'sleep';
    if (status === 'ERROR') return 'alert-circle-outline';
    return 'power-plug-off-outline';
  };

  const isActive = data?.status === 'ONLINE' || data?.status === 'SLEEPING';
  const statusColor = getStatusColor(data?.status || 'OFFLINE');

  return (
    <Modal visible={visible} animationType="slide" presentationStyle="fullScreen" onRequestClose={onClose}>
      <SafeAreaView style={[styles.container, { backgroundColor: bg }]}>
        <View style={[styles.header, { borderBottomColor: borderColor }]}>
          <View style={styles.headerLeft}>
            <MaterialCommunityIcons name="robot" size={24} color="#00f0ff" />
            <Text style={[styles.title, { color: textPrimary }]}>Cultural Daemon Pulse</Text>
          </View>
          <TouchableOpacity onPress={onClose} style={styles.closeBtn}>
            <MaterialCommunityIcons name="close" size={24} color={textPrimary} />
          </TouchableOpacity>
        </View>

        {loading && !data ? (
          <View style={styles.center}>
             <ActivityIndicator size="large" color="#00f0ff" />
          </View>
        ) : (
          <View style={styles.content}>
            
            {/* Status Hero */}
            <View style={[styles.heroCard, { backgroundColor: cardBg, borderColor }]}>
              <View style={styles.heroRow}>
                <View style={[styles.pulseDot, { backgroundColor: statusColor }]} />
                <Text style={[styles.statusText, { color: statusColor }]}>{data?.status || 'OFFLINE'}</Text>
              </View>
              <Text style={[styles.lastHeartbeat, { color: textMuted }]}>
                Last Heartbeat: {data?.last_heartbeat ? new Date(data.last_heartbeat).toLocaleTimeString() : 'N/A'}
              </Text>
              <MaterialCommunityIcons 
                name={getStatusIcon(data?.status || 'OFFLINE')} 
                size={80} 
                color={statusColor} 
                style={styles.heroIcon} 
              />
            </View>

            {/* Current Target Block */}
            <View style={[styles.block, { backgroundColor: cardBg, borderColor }]}>
              <Text style={[styles.blockTitle, { color: textMuted }]}>CURRENT TARGET</Text>
              <Text style={[styles.targetText, { color: textPrimary }]}>
                {data?.current_target || 'None / Idle'}
              </Text>
            </View>

            {/* Metrics Grid */}
            <View style={styles.metricsGrid}>
              <View style={[styles.metricCard, { backgroundColor: cardBg, borderColor }]}>
                <View style={styles.metricHeader}>
                  <MaterialCommunityIcons name="database-arrow-up-outline" size={20} color="#00E676" />
                  <Text style={[styles.metricLabel, { color: textMuted }]}>Enriched</Text>
                </View>
                <Text style={[styles.metricValue, { color: textPrimary }]}>{data?.total_enriched || 0}</Text>
              </View>

              <View style={[styles.metricCard, { backgroundColor: cardBg, borderColor }]}>
                <View style={styles.metricHeader}>
                  <MaterialCommunityIcons name="shield-lock-outline" size={20} color="#ff4040" />
                  <Text style={[styles.metricLabel, { color: textMuted }]}>Denials</Text>
                </View>
                <Text style={[styles.metricValue, { color: textPrimary }]}>{data?.total_denials || 0}</Text>
              </View>
            </View>

            {/* Error Log */}
            {data?.last_error && (
              <View style={[styles.errorBlock, { backgroundColor: cardBg, borderColor: '#ff4040' }]}>
                <View style={styles.errorHeader}>
                  <MaterialCommunityIcons name="alert" size={16} color="#ff4040" />
                  <Text style={styles.errorTitle}>LATEST TRACE</Text>
                </View>
                <Text style={styles.errorText}>{data.last_error}</Text>
              </View>
            )}

          </View>
        )}
      </SafeAreaView>
    </Modal>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: Spacing.md,
    paddingVertical: Spacing.sm,
    borderBottomWidth: 1,
  },
  headerLeft: { flexDirection: 'row', alignItems: 'center', gap: Spacing.sm },
  title: { fontSize: 20, fontWeight: '700' },
  closeBtn: { padding: Spacing.xs },
  center: { flex: 1, justifyContent: 'center', alignItems: 'center' },
  content: { padding: Spacing.md, gap: Spacing.md },

  heroCard: {
    borderWidth: 1,
    borderRadius: 12,
    padding: Spacing.lg,
    alignItems: 'center',
    marginBottom: Spacing.xs,
    position: 'relative',
    overflow: 'hidden'
  },
  heroRow: { flexDirection: 'row', alignItems: 'center', gap: 8 },
  pulseDot: { width: 12, height: 12, borderRadius: 6 },
  statusText: { fontSize: 24, fontWeight: '800', letterSpacing: 2 },
  lastHeartbeat: { fontSize: 13, marginTop: 4 },
  heroIcon: { position: 'absolute', right: -10, bottom: -10, opacity: 0.1 },

  block: {
    borderWidth: 1,
    borderRadius: 12,
    padding: Spacing.md,
  },
  blockTitle: { fontSize: 12, fontWeight: '700', letterSpacing: 1, marginBottom: 4 },
  targetText: { fontSize: 18, fontWeight: '600' },

  metricsGrid: { flexDirection: 'row', gap: Spacing.md },
  metricCard: {
    flex: 1,
    borderWidth: 1,
    borderRadius: 12,
    padding: Spacing.md,
  },
  metricHeader: { flexDirection: 'row', alignItems: 'center', gap: 6, marginBottom: Spacing.sm },
  metricLabel: { fontSize: 13, fontWeight: '600' },
  metricValue: { fontSize: 32, fontWeight: '700' },

  errorBlock: {
    borderWidth: 1,
    borderRadius: 12,
    padding: Spacing.md,
  },
  errorHeader: { flexDirection: 'row', alignItems: 'center', gap: 6, marginBottom: 4 },
  errorTitle: { fontSize: 12, fontWeight: '700', letterSpacing: 1, color: '#ff4040' },
  errorText: { fontSize: 14, color: '#ff4040', opacity: 0.8 },
});
