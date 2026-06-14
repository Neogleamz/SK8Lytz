import { MaterialCommunityIcons } from '@expo/vector-icons';
import React, { useCallback, useEffect, useMemo, useState } from 'react';
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
import { AppLogger } from '../../../services/AppLogger';
import { supabase } from '../../../services/supabaseClient';
import { Spacing, Typography } from '../../../theme/theme';
import { ErrorCard } from '../../ErrorCard';

export interface AdminRosterPanelProps {
  visible: boolean;
  onClose: () => void;
  bg: string;
  cardBg: string;
  borderColor: string;
  textPrimary: string;
  textMuted: string;
}

interface AdminProfile {
  user_id: string;
  username: string | null;
  display_name: string | null;
  role: 'user' | 'moderator' | 'admin';
  created_at: string;
}

const AdminRosterCard = React.memo(({
  item,
  cardBg,
  borderColor,
  textPrimary,
  textMuted,
  onRevokeAdmin,
}: {
  item: AdminProfile;
  cardBg: string;
  borderColor: string;
  textPrimary: string;
  textMuted: string;
  onRevokeAdmin: (userId: string, username: string | null) => void;
}) => {
  const handleRevoke = useCallback(() => {
    onRevokeAdmin(item.user_id, item.username);
  }, [onRevokeAdmin, item.user_id, item.username]);

  return (
    <View style={[styles.userCard, { backgroundColor: cardBg, borderColor }]}>
      <View style={styles.cardHeader}>
        <View>
          <Text style={[styles.userTitle, { color: textPrimary }]}>
            {item.display_name || 'No Display Name'}
          </Text>
          <Text style={[styles.userSubtitle, { color: textMuted }]}>
            @{item.username || 'unknown'} • Promoted: {new Date(item.created_at).toLocaleDateString()}
          </Text>
        </View>
        <MaterialCommunityIcons name="shield-star" size={20} color="#FFD700" />
      </View>

      <View style={styles.actionRow}>
        <TouchableOpacity onPress={handleRevoke} style={styles.actionBtn}>
          <MaterialCommunityIcons name="shield-remove-outline" size={18} color="#ff4040" />
          <Text style={[styles.actionText, { color: '#ff4040' }]}>Revoke Admin</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
});

export function AdminRosterPanel({
  visible,
  onClose,
  bg,
  cardBg,
  borderColor,
  textPrimary,
  textMuted,
}: AdminRosterPanelProps) {
  const [admins, setAdmins] = useState<AdminProfile[]>([]);
  type ViewState = 'loading' | 'refreshing' | 'error' | 'success';
  const [status, setStatus] = useState<ViewState>('loading');

  const fetchAdmins = useCallback(async () => {
    try {
      setStatus(prev => prev === 'refreshing' ? 'refreshing' : 'loading');
      const { data, error } = await supabase
        .from('user_profiles')
        .select('*')
        .eq('role', 'admin')
        .order('created_at', { ascending: false });

      if (error) throw error;
      setAdmins(data as AdminProfile[]);
      setStatus('success');
    } catch (e: unknown) {
      AppLogger.error('Failed to fetch admins for roster panel', e, { payload_size: 0, ssi: 0 });
      Alert.alert('Error', 'Failed to fetch admins: ' + (e instanceof Error ? e.message : String(e)));
      setStatus('error');
    }
  }, []);

  useEffect(() => {
    if (visible) {
      fetchAdmins();
    }
  }, [visible, fetchAdmins]);

  const handleRevokeAdmin = useCallback((userId: string, username: string | null) => {
    Alert.alert(
      'Revoke Admin Privileges',
      `Are you sure you want to demote ${username || 'this user'} to a regular user?`,
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Revoke',
          style: 'destructive',
          onPress: async () => {
            try {
              // Use SECURITY DEFINER RPC — direct client UPDATE is blocked by RLS.
              const { error } = await supabase.rpc('admin_revoke_admin_role', {
                p_target_user_id: userId,
              });
              if (error) throw error;
              Alert.alert('Success', 'User has been demoted to regular user.');
              fetchAdmins();
            } catch (e: unknown) {
              Alert.alert('Failed', (e instanceof Error ? e.message : String(e)));
            }
          },
        },
      ]
    );
  }, [fetchAdmins]);

  const renderItem = useCallback(({ item }: { item: AdminProfile }) => {
    return (
      <AdminRosterCard
        item={item}
        cardBg={cardBg}
        borderColor={borderColor}
        textPrimary={textPrimary}
        textMuted={textMuted}
        onRevokeAdmin={handleRevokeAdmin}
      />
    );
  }, [cardBg, borderColor, textPrimary, textMuted, handleRevokeAdmin]);

  const handleRefresh = useCallback(() => {
    setStatus('refreshing');
    fetchAdmins();
  }, [fetchAdmins]);

  const renderEmpty = useCallback(() => (
    <View style={{ padding: 20, alignItems: 'center' }}>
      <Text style={{ color: textMuted }}>No admins found.</Text>
    </View>
  ), [textMuted]);

  const keyExtractor = useCallback((i: AdminProfile) => i.user_id, []);

  return (
    <Modal visible={visible} animationType="slide" presentationStyle="fullScreen" onRequestClose={onClose}>
      <SafeAreaView style={[styles.root, { backgroundColor: bg }]}>
        <View style={[styles.header, { borderBottomColor: borderColor }]}>
          <TouchableOpacity onPress={onClose} style={styles.closeBtn}>
            <MaterialCommunityIcons name="arrow-left" size={24} color={textPrimary} />
          </TouchableOpacity>
          <Text style={[styles.headerTitle, { color: textPrimary }]}>Admin Roster</Text>
        </View>

        {status === 'loading' ? (
          <ActivityIndicator size="large" color="#FFD700" style={{ marginTop: Spacing.xl }} />
        ) : status === 'error' ? (
          <View style={{ padding: Spacing.md }}>
            <ErrorCard message="Failed to load roster. Tap to retry." onRetry={fetchAdmins} />
          </View>
        ) : (
          <FlatList removeClippedSubviews={true} initialNumToRender={12} windowSize={5}
            data={admins}
            renderItem={renderItem}
            keyExtractor={keyExtractor}
            ListEmptyComponent={renderEmpty}
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
  userCard: {
    borderWidth: 1,
    borderRadius: 8,
    padding: Spacing.md,
    marginBottom: Spacing.md,
  },
  cardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    marginBottom: Spacing.sm,
  },
  userTitle: { ...Typography.body },
  userSubtitle: { fontSize: 13, marginTop: 2 },
  actionRow: {
    flexDirection: 'row',
    justifyContent: 'flex-end',
    borderTopWidth: 1,
    borderTopColor: '#ffffff10',
    paddingTop: Spacing.sm,
    gap: Spacing.md,
  },
  actionBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
    paddingVertical: 4,
    paddingHorizontal: 8,
  },
  actionText: {
    fontSize: 13,
    fontWeight: '600',
  },
});
