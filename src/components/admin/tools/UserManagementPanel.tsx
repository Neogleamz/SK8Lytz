import { MaterialCommunityIcons } from '@expo/vector-icons';
import React, { useCallback, useEffect, useMemo, useState, useRef } from 'react';
import {
    ActivityIndicator,
    Alert,
    FlatList,
    Modal,
    StyleSheet,
    Text,
    TextInput,
    TouchableOpacity,
    View,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { AppLogger } from '../../../services/AppLogger';
import { supabase } from '../../../services/supabaseClient';
import { Spacing, Typography } from '../../../theme/theme';
import { ErrorCard } from '../../ErrorCard';
import { EmptyState } from '../../EmptyState';

export interface UserManagementPanelProps {
  visible: boolean;
  onClose: () => void;
  bg: string;
  cardBg: string;
  borderColor: string;
  textPrimary: string;
  textMuted: string;
}

interface AdminUserProfile {
  user_id: string;
  username: string | null;
  display_name: string | null;
  email?: string | null;
  role: 'user' | 'moderator' | 'admin';
  is_banned: boolean;
  ban_reason: string | null;
  created_at: string;
}

const UserCard = React.memo(({
  item,
  cardBg,
  borderColor,
  textPrimary,
  textMuted,
  onBan,
  onRevokeBan,
  onResetPassword,
  onRevokeSessions,
  onExportData,
  onSoftDelete,
}: {
  item: AdminUserProfile;
  cardBg: string;
  borderColor: string;
  textPrimary: string;
  textMuted: string;
  onBan: (id: string) => void;
  onRevokeBan: (id: string) => void;
  onResetPassword: (id: string) => void;
  onRevokeSessions: (id: string) => void;
  onExportData: (id: string, name: string | null) => void;
  onSoftDelete: (id: string) => void;
}) => {
  const isBanned = item.is_banned;
  const handleBanPress = useCallback(() => onBan(item.user_id), [onBan, item.user_id]);
  const handleRevokeBanPress = useCallback(() => onRevokeBan(item.user_id), [onRevokeBan, item.user_id]);
  const handleResetPasswordPress = useCallback(() => onResetPassword(item.user_id), [onResetPassword, item.user_id]);
  const handleRevokeSessionsPress = useCallback(() => onRevokeSessions(item.user_id), [onRevokeSessions, item.user_id]);
  const handleExportDataPress = useCallback(() => onExportData(item.user_id, item.display_name), [onExportData, item.user_id, item.display_name]);
  const handleSoftDeletePress = useCallback(() => onSoftDelete(item.user_id), [onSoftDelete, item.user_id]);

  return (
    <View style={[styles.userCard, { backgroundColor: cardBg, borderColor }]}>
      <View style={styles.cardHeader}>
        <View>
          <Text style={[styles.userTitle, { color: textPrimary }]}>
            {item.display_name || 'No Display Name'}
          </Text>
          <Text style={[styles.userSubtitle, { color: textMuted }]}>
            @{item.username || 'unknown'} • {item.role.toUpperCase()}
          </Text>
        </View>
        {isBanned && (
          <MaterialCommunityIcons name="gavel" size={20} color="#ff4040" />
        )}
      </View>

      {isBanned && (
        <View style={[styles.bannedNotice, { backgroundColor: '#ff404020' }]}>
          <Text style={styles.bannedText}>Banned: {item.ban_reason}</Text>
        </View>
      )}

      <View style={styles.actionRow}>
        {isBanned ? (
          <TouchableOpacity onPress={handleRevokeBanPress} style={styles.actionBtn}>
            <MaterialCommunityIcons name="shield-check" size={18} color="#00E676" />
            <Text style={[styles.actionText, { color: '#00E676' }]}>Unban</Text>
          </TouchableOpacity>
        ) : (
          <TouchableOpacity onPress={handleBanPress} style={styles.actionBtn}>
            <MaterialCommunityIcons name="gavel" size={18} color="#ff4040" />
            <Text style={[styles.actionText, { color: '#ff4040' }]}>Ban</Text>
          </TouchableOpacity>
        )}

        <TouchableOpacity onPress={handleResetPasswordPress} style={styles.actionBtn}>
          <MaterialCommunityIcons name="lock-reset" size={18} color="#FF5A00" />
          <Text style={[styles.actionText, { color: '#FF5A00' }]}>Lockout</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={handleRevokeSessionsPress} style={styles.actionBtn}>
          <MaterialCommunityIcons name="logout-variant" size={18} color="#FFD700" />
          <Text style={[styles.actionText, { color: '#FFD700' }]}>Revoke</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={handleExportDataPress} style={styles.actionBtn}>
          <MaterialCommunityIcons name="database-export" size={18} color="#00f0ff" />
          <Text style={[styles.actionText, { color: '#00f0ff' }]}>Export</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={handleSoftDeletePress} style={styles.actionBtn}>
          <MaterialCommunityIcons name="delete-empty" size={18} color="#ff4040" />
          <Text style={[styles.actionText, { color: '#ff4040' }]}>Soft Delete</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
});

export function UserManagementPanel({
  visible,
  onClose,
  bg,
  cardBg,
  borderColor,
  textPrimary,
  textMuted,
}: UserManagementPanelProps) {
  const [users, setUsers] = useState<AdminUserProfile[]>([]);
  const [searchQuery, setSearchQuery] = useState('');
  type ViewState = 'loading' | 'refreshing' | 'error' | 'success';
  const [status, setStatus] = useState<ViewState>('loading');
  const isMountedRef = useRef(true);

  useEffect(() => {
    isMountedRef.current = true;
    return () => { isMountedRef.current = false; };
  }, []);

  const fetchUsers = useCallback(async () => {
    try {
      setStatus(prev => prev === 'refreshing' ? 'refreshing' : 'loading');
      const { data, error } = await supabase
        .from('user_profiles')
        .select('*')
        .order('created_at', { ascending: false })
        .limit(100);

      if (error) throw error;
      if (!isMountedRef.current) return;
      setUsers(data as AdminUserProfile[]);
      setStatus('success');
    } catch (e: unknown) {
      if (!isMountedRef.current) return;
      AppLogger.error('Failed to fetch users for admin panel', e instanceof Error ? e : new Error(String(e)), { payload_size: 0, ssi: 0 });
      setStatus('error');
    }
  }, []);

  useEffect(() => {
    if (visible) {
      fetchUsers();
    }
  }, [visible, fetchUsers]);

  const handleBan = useCallback((userId: string) => {
    Alert.prompt(
      'Ban User',
      'Enter a reason for the ban:',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Ban',
          style: 'destructive',
          onPress: async (reason?: string) => {
            if (!reason) {
              Alert.alert('Error', 'Ban reason is required');
              return;
            }
            try {
              const { error } = await supabase.rpc('admin_ban_user', {
                p_target_user_id: userId,
                p_reason: reason,
              });
              if (error) throw error;
              Alert.alert('Success', 'User has been banned.');
              fetchUsers();
            } catch (e: unknown) {
              Alert.alert('Ban Failed', (e instanceof Error ? e.message : String(e)));
            }
          },
        },
      ],
      'plain-text'
    );
  }, [fetchUsers]);

  const handleRevokeBan = useCallback(async (userId: string) => {
    Alert.alert('Revoke Ban', 'Are you sure you want to lift the ban?', [
      { text: 'Cancel', style: 'cancel' },
      {
        text: 'Revoke',
        onPress: async () => {
          try {
            const { error } = await supabase.rpc('admin_revoke_ban', {
              p_target_user_id: userId,
            });
            if (error) throw error;
            Alert.alert('Success', 'Ban revoked.');
            fetchUsers();
          } catch (e: unknown) {
            Alert.alert('Failed', (e instanceof Error ? e.message : String(e)));
          }
        },
      },
    ]);
  }, [fetchUsers]);

  const handleResetPassword = useCallback((userId: string) => {
    Alert.alert(
      'Force Password Reset',
      'This will lock the user out by scrambling their password. They must use the recovery flow. Continue?',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Lock Out',
          style: 'destructive',
          onPress: async () => {
            try {
              const { error } = await supabase.rpc('admin_force_password_reset', {
                p_target_user_id: userId,
              });
              if (error) throw error;
              Alert.alert('Success', 'Password scrambled.');
            } catch (e: unknown) {
              Alert.alert('Failed', (e instanceof Error ? e.message : String(e)));
            }
          },
        },
      ]
    );
  }, []);

  const handleSoftDelete = useCallback((userId: string) => {
    Alert.alert(
      'Soft Delete User',
      'This will ban the user and scramble their email/password pending a bulk purge. Proceed?',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Soft Delete',
          style: 'destructive',
          onPress: async () => {
            try {
              const { error } = await supabase.rpc('admin_soft_delete_user', {
                p_target_user_id: userId,
              });
              if (error) throw error;
              Alert.alert('Success', 'User soft deleted.');
              fetchUsers();
            } catch (e: unknown) {
              Alert.alert('Failed', (e instanceof Error ? e.message : String(e)));
            }
          },
        },
      ]
    );
  }, [fetchUsers]);

  const handleRevokeSessions = useCallback((userId: string) => {
    Alert.alert(
      'Revoke Sessions',
      'This will instantly log the user out of all devices by destroying their active tokens. Proceed?',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Revoke',
          style: 'destructive',
          onPress: async () => {
            try {
              const { error } = await supabase.rpc('admin_revoke_sessions', {
                p_target_user_id: userId,
              });
              if (error) throw error;
              Alert.alert('Success', 'All sessions revoked.');
            } catch (e: unknown) {
              Alert.alert('Failed', (e instanceof Error ? e.message : String(e)));
            }
          },
        },
      ]
    );
  }, []);

  const handleExportData = useCallback(async (userId: string, displayName: string | null) => {
    try {
      setStatus('loading');
      const { data, error } = await supabase.rpc('admin_export_user_data', {
        p_target_user_id: userId,
      });
      if (error) throw error;
      
      const dataLen = data ? JSON.stringify(data).length : 0;
      AppLogger.log('DATA_EXPORT', { byteLength: dataLen });
      Alert.alert('Data Exported', `Data for ${displayName || userId} has been fetched and logged to the telemetry stream. (Length: ${dataLen} bytes)`);
      setStatus('success');
    } catch (e: unknown) {
      Alert.alert('Export Failed', (e instanceof Error ? e.message : String(e)));
      setStatus('success');
    }
  }, []);

  const filteredUsers = users.filter((u) => {
    const q = searchQuery.toLowerCase();
    return (
      (u.username && u.username.toLowerCase().includes(q)) ||
      (u.display_name && u.display_name.toLowerCase().includes(q)) ||
      (u.user_id && u.user_id.toLowerCase().includes(q))
    );
  });

  const renderItem = useCallback(({ item }: { item: AdminUserProfile }) => {
    return (
      <UserCard
        item={item}
        cardBg={cardBg}
        borderColor={borderColor}
        textPrimary={textPrimary}
        textMuted={textMuted}
        onBan={handleBan}
        onRevokeBan={handleRevokeBan}
        onResetPassword={handleResetPassword}
        onRevokeSessions={handleRevokeSessions}
        onExportData={handleExportData}
        onSoftDelete={handleSoftDelete}
      />
    );
  }, [cardBg, borderColor, textPrimary, textMuted, handleBan, handleRevokeBan, handleResetPassword, handleRevokeSessions, handleExportData, handleSoftDelete]);

  const handleRefresh = useCallback(() => {
    setStatus('refreshing');
    fetchUsers();
  }, [fetchUsers]);

  const renderEmpty = useCallback(() => (
    <EmptyState message="No users found" />
  ), []);

  const keyExtractor = useCallback((i: AdminUserProfile, index: number) => i.user_id || String(index), []);

  return (
    <Modal visible={visible} animationType="slide" presentationStyle="fullScreen" onRequestClose={onClose}>
      <SafeAreaView style={[styles.root, { backgroundColor: bg }]}>
        <View style={[styles.header, { borderBottomColor: borderColor }]}>
          <TouchableOpacity onPress={onClose} style={styles.closeBtn}>
            <MaterialCommunityIcons name="arrow-left" size={24} color={textPrimary} />
          </TouchableOpacity>
          <Text style={[styles.headerTitle, { color: textPrimary }]}>User Management</Text>
        </View>

        <View style={[styles.searchContainer, { borderBottomColor: borderColor }]}>
          <MaterialCommunityIcons name="magnify" size={20} color={textMuted} />
          <TextInput
            style={[styles.searchInput, { color: textPrimary }]}
            placeholder="Search users..."
            placeholderTextColor={textMuted}
            value={searchQuery}
            onChangeText={setSearchQuery}
          />
        </View>

        {status === 'loading' ? (
          <ActivityIndicator size="large" color="#00f0ff" style={{ marginTop: Spacing.xl }} />
        ) : status === 'error' ? (
          <ErrorCard message="Failed to load. Tap to retry." onRetry={fetchUsers} />
        ) : (
          <FlatList removeClippedSubviews={true} initialNumToRender={12} windowSize={5}
            data={filteredUsers}
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
  searchContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: Spacing.md,
    paddingVertical: Spacing.sm,
    borderBottomWidth: 1,
  },
  searchInput: {
    flex: 1,
    marginLeft: Spacing.sm,
    fontSize: 16,
    padding: Spacing.sm,
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
  bannedNotice: {
    padding: Spacing.sm,
    borderRadius: 4,
    marginBottom: Spacing.md,
  },
  bannedText: {
    color: '#ff4040',
    fontSize: 12,
    fontWeight: '700',
  },
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
