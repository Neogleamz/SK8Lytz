import { MaterialCommunityIcons } from '@expo/vector-icons';
import React, { useEffect, useState } from 'react';
import {
    ActivityIndicator,
    Alert,
    FlatList,
    Modal,
    SafeAreaView,
    StyleSheet,
    Text,
    TouchableOpacity,
    View,
} from 'react-native';
import { AppLogger } from '../../../services/AppLogger';
import { supabase } from '../../../services/supabaseClient';
import { Spacing, Typography } from '../../../theme/theme';

export interface AdminAuditLogViewerProps {
  visible: boolean;
  onClose: () => void;
  bg: string;
  cardBg: string;
  borderColor: string;
  textPrimary: string;
  textMuted: string;
}

interface AuditLogEntry {
  id: string;
  admin_id: string;
  target_user_id: string | null;
  action: string;
  reason: string | null;
  created_at: string;
}

interface ProfileMap {
  [key: string]: string;
}

export function AdminAuditLogViewer({
  visible,
  onClose,
  bg,
  cardBg,
  borderColor,
  textPrimary,
  textMuted,
}: AdminAuditLogViewerProps) {
  const [logs, setLogs] = useState<AuditLogEntry[]>([]);
  const [profiles, setProfiles] = useState<ProfileMap>({});
  const [loading, setLoading] = useState(true);
  const [isRefreshing, setIsRefreshing] = useState(false);

  const fetchLogs = async () => {
    try {
      setLoading(true);
      const { data, error } = await supabase
        .from('admin_audit_logs')
        .select('*')
        .order('created_at', { ascending: false })
        .limit(100);

      if (error) throw error;
      
      const fetchedLogs = data as AuditLogEntry[];
      setLogs(fetchedLogs);

      // Collect unique user IDs to fetch profiles for display names
      const uniqueIds = new Set<string>();
      fetchedLogs.forEach(log => {
        if (log.admin_id) uniqueIds.add(log.admin_id);
        if (log.target_user_id) uniqueIds.add(log.target_user_id);
      });

      if (uniqueIds.size > 0) {
        const { data: profileData } = await supabase
          .from('user_profiles')
          .select('user_id, display_name, username')
          .in('user_id', Array.from(uniqueIds));
          
        if (profileData) {
          const pMap: ProfileMap = {};
          profileData.forEach(p => {
            pMap[p.user_id] = p.display_name || p.username || p.user_id;
          });
          setProfiles(pMap);
        }
      }

    } catch (e) {
      AppLogger.error('Failed to fetch audit logs', e);
      Alert.alert('Error', 'Failed to fetch audit logs: ' + (e instanceof Error ? e.message : String(e)));
    } finally {
      setLoading(false);
      setIsRefreshing(false);
    }
  };

  useEffect(() => {
    if (visible) {
      fetchLogs();
    }
  }, [visible]);

  const getActionColor = (action: string) => {
    if (action.includes('BAN') || action.includes('DELETE') || action.includes('RESET') || action.includes('REVOKE')) return '#ff4040';
    if (action.includes('ADD_HARDWARE')) return '#FFD700';
    return '#00f0ff';
  };

  const renderItem = ({ item }: { item: AuditLogEntry }) => {
    const adminName = profiles[item.admin_id] || item.admin_id.substring(0, 8);
    const targetName = item.target_user_id ? (profiles[item.target_user_id] || item.target_user_id.substring(0, 8)) : null;

    return (
      <View style={[styles.logCard, { backgroundColor: cardBg, borderColor }]}>
        <View style={styles.cardHeader}>
          <Text style={[styles.actionText, { color: getActionColor(item.action) }]}>
            {item.action}
          </Text>
          <Text style={[styles.timeText, { color: textMuted }]}>
            {new Date(item.created_at).toLocaleString()}
          </Text>
        </View>

        <View style={styles.logBody}>
          <Text style={[styles.detailText, { color: textPrimary }]}>
            <Text style={{ fontWeight: 'bold' }}>{adminName}</Text> executed action
            {targetName && <Text> on <Text style={{ fontWeight: 'bold' }}>{targetName}</Text></Text>}.
          </Text>
          {item.reason && (
            <Text style={[styles.reasonText, { color: textMuted, backgroundColor: '#ffffff10' }]}>
              Reason: {item.reason}
            </Text>
          )}
        </View>
      </View>
    );
  };

  return (
    <Modal visible={visible} animationType="slide" presentationStyle="fullScreen" onRequestClose={onClose}>
      <SafeAreaView style={[styles.root, { backgroundColor: bg }]}>
        <View style={[styles.header, { borderBottomColor: borderColor }]}>
          <TouchableOpacity onPress={onClose} style={styles.closeBtn}>
            <MaterialCommunityIcons name="arrow-left" size={24} color={textPrimary} />
          </TouchableOpacity>
          <Text style={[styles.headerTitle, { color: textPrimary }]}>Audit Trail</Text>
        </View>

        {loading && !isRefreshing ? (
          <ActivityIndicator size="large" color="#00f0ff" style={{ marginTop: Spacing.xl }} />
        ) : (
          <FlatList
            data={logs}
            renderItem={renderItem}
            keyExtractor={(i) => i.id}
            contentContainerStyle={styles.list}
            refreshing={isRefreshing}
            onRefresh={() => {
              setIsRefreshing(true);
              fetchLogs();
            }}
          />
        )}
      </SafeAreaView>
    </Modal>
  );
}

const styles = StyleSheet.create({
  root: { flex: 1 },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: Spacing.md,
    borderBottomWidth: 1,
  },
  closeBtn: {
    padding: Spacing.sm,
    marginRight: Spacing.sm,
  },
  headerTitle: {
    ...Typography.header,
  },
  list: {
    padding: Spacing.md,
    paddingBottom: 100,
  },
  logCard: {
    borderWidth: 1,
    borderRadius: 8,
    padding: Spacing.md,
    marginBottom: Spacing.md,
  },
  cardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: Spacing.sm,
  },
  actionText: { 
    fontSize: 14, 
    fontWeight: 'bold' 
  },
  timeText: { 
    fontSize: 12 
  },
  logBody: {
    marginTop: 4,
  },
  detailText: {
    fontSize: 14,
    lineHeight: 20,
  },
  reasonText: {
    marginTop: Spacing.sm,
    padding: Spacing.sm,
    borderRadius: 4,
    fontSize: 13,
    fontStyle: 'italic',
  },
});
