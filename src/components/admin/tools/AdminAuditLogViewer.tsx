import { MaterialCommunityIcons } from '@expo/vector-icons';
import React, { useCallback, useEffect, useMemo, useState, useRef } from 'react';
import {
    ActivityIndicator,
    Alert,
    FlatList,
    Modal,
    StyleSheet,
    Text,
    TouchableOpacity,
    View,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { AppLogger } from '../../../services/appLogger';
import { supabase } from '../../../services/supabaseClient';
import { Spacing, Typography } from '../../../theme/theme';
import { ErrorCard } from '../../ErrorCard';
import { EmptyState } from '../../EmptyState';

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
  type ViewState = 'loading' | 'refreshing' | 'error' | 'success';
  const [status, setStatus] = useState<ViewState>('loading');
  const isMountedRef = useRef(true);

  useEffect(() => {
    isMountedRef.current = true;
    return () => { isMountedRef.current = false; };
  }, []);

  const fetchLogs = useCallback(async () => {
    try {
      setStatus(prev => prev === 'refreshing' ? 'refreshing' : 'loading');
      const { data, error } = await supabase
        .from('admin_audit_logs')
        .select('*')
        .order('created_at', { ascending: false })
        .limit(100);

      if (error) throw error;
      
      const fetchedLogs: AuditLogEntry[] = (data || []).map((row: any) => ({
        id: String(row.id || ''),
        admin_id: String(row.admin_id || ''),
        target_user_id: row.target_user_id ? String(row.target_user_id) : null,
        action: String(row.action || ''),
        reason: row.reason ? String(row.reason) : null,
        created_at: String(row.created_at || ''),
      }));
      if (!isMountedRef.current) return;
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
          if (!isMountedRef.current) return;
          setProfiles(pMap);
        }
      }
      if (!isMountedRef.current) return;
      setStatus('success');
    } catch (e: unknown) {
      if (!isMountedRef.current) return;
      const error = e instanceof Error ? e : new Error(String(e));
      AppLogger.error('Failed to fetch audit logs', error, { payload_size: 0, ssi: 0 });
      setStatus('error');
    }
  }, []);

  useEffect(() => {
    if (visible) {
      fetchLogs();
    }
  }, [visible, fetchLogs]);

  const getActionColor = (action: string) => {
    if (action.includes('BAN') || action.includes('DELETE') || action.includes('RESET') || action.includes('REVOKE')) return '#ff4040';
    if (action.includes('ADD_HARDWARE')) return '#FFD700';
    return '#00f0ff';
  };

  const cardStyle = useMemo(() => ({ backgroundColor: cardBg, borderColor }), [cardBg, borderColor]);

  const textMutedStyle = useMemo(() => ({ color: textMuted }), [textMuted]);
  const textPrimaryStyle = useMemo(() => ({ color: textPrimary }), [textPrimary]);
  const reasonStyle = useMemo(() => ({ color: textMuted, backgroundColor: '#ffffff10' }), [textMuted]);

  const renderItem = useCallback(({ item }: { item: AuditLogEntry }) => {
    const adminName = profiles[item.admin_id] || item.admin_id.substring(0, 8);
    const targetName = item.target_user_id ? (profiles[item.target_user_id] || item.target_user_id.substring(0, 8)) : null;

    return (
      <View style={[styles.logCard, cardStyle]}>
        <View style={styles.cardHeader}>
          <Text style={[styles.actionText, { color: getActionColor(item.action) }]}>
            {item.action}
          </Text>
          <Text style={[styles.timeText, textMutedStyle]}>
            {new Date(item.created_at).toLocaleString()}
          </Text>
        </View>

        <View style={styles.logBody}>
          <Text style={[styles.detailText, textPrimaryStyle]}>
            <Text style={{ fontWeight: 'bold' }}>{adminName}</Text> executed action
            {targetName && <Text> on <Text style={{ fontWeight: 'bold' }}>{targetName}</Text></Text>}.
          </Text>
          {item.reason && (
            <Text style={[styles.reasonText, reasonStyle]}>
              Reason: {item.reason}
            </Text>
          )}
        </View>
      </View>
    );
  }, [profiles, cardStyle, textMutedStyle, textPrimaryStyle, reasonStyle]);

  const handleRefresh = useCallback(() => {
    setStatus('refreshing');
    fetchLogs();
  }, [fetchLogs]);

  const renderEmpty = useCallback(() => (
    <EmptyState message="No items found" />
  ), []);

  const keyExtractor = useCallback((i: AuditLogEntry) => i.id, []);

  return (
    <Modal visible={visible} animationType="slide" presentationStyle="fullScreen" onRequestClose={onClose}>
      <SafeAreaView style={[styles.root, { backgroundColor: bg }]}>
        <View style={[styles.header, { borderBottomColor: borderColor }]}>
          <TouchableOpacity onPress={onClose} style={styles.closeBtn}>
            <MaterialCommunityIcons name="arrow-left" size={24} color={textPrimary} />
          </TouchableOpacity>
          <Text style={[styles.headerTitle, { color: textPrimary }]}>Audit Trail</Text>
        </View>

        {status === 'loading' ? (
          <ActivityIndicator size="large" color="#00f0ff" style={{ marginTop: Spacing.xl }} />
        ) : status === 'error' ? (
          <ErrorCard message="Failed to load. Tap to retry." onRetry={fetchLogs} />
        ) : (
          <FlatList removeClippedSubviews={true} initialNumToRender={12} windowSize={5}
            data={logs}
            ListEmptyComponent={renderEmpty}
            renderItem={renderItem}
            keyExtractor={keyExtractor}
            contentContainerStyle={styles.list}
            refreshing={status === 'refreshing'}
            onRefresh={handleRefresh}
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
