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
    TextInput,
    TouchableOpacity,
    View,
} from 'react-native';
import { AppLogger } from '../../../services/AppLogger';
import { supabase } from '../../../services/supabaseClient';
import { Spacing, Typography } from '../../../theme/theme';

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
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [isRefreshing, setIsRefreshing] = useState(false);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      // Notice: auth.users email is not directly readable via select from public.user_profiles unless linked.
      // We will just fetch profiles. Since this is an admin panel, we can join with RPC if we need emails, 
      // or we just show display names.
      const { data, error } = await supabase
        .from('user_profiles')
        .select('*')
        .order('created_at', { ascending: false })
        .limit(100);

      if (error) throw error;
      setUsers(data as AdminUserProfile[]);
    } catch (e: any) {
      AppLogger.error('Failed to fetch users for admin panel', e);
      Alert.alert('Error', 'Failed to fetch users: ' + e.message);
    } finally {
      setLoading(false);
      setIsRefreshing(false);
    }
  };

  useEffect(() => {
    if (visible) {
      fetchUsers();
    }
  }, [visible]);

  const handleBan = (userId: string) => {
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
            } catch (err: any) {
              Alert.alert('Ban Failed', err.message);
            }
          },
        },
      ],
      'plain-text'
    );
  };

  const handleRevokeBan = async (userId: string) => {
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
          } catch (err: any) {
            Alert.alert('Failed', err.message);
          }
        },
      },
    ]);
  };

  const handleResetPassword = (userId: string) => {
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
            } catch (err: any) {
              Alert.alert('Failed', err.message);
            }
          },
        },
      ]
    );
  };

  const handleSoftDelete = (userId: string) => {
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
            } catch (err: any) {
              Alert.alert('Failed', err.message);
            }
          },
        },
      ]
    );
  };

  const filteredUsers = users.filter((u) => {
    const q = searchQuery.toLowerCase();
    return (
      (u.username && u.username.toLowerCase().includes(q)) ||
      (u.display_name && u.display_name.toLowerCase().includes(q)) ||
      (u.user_id && u.user_id.toLowerCase().includes(q))
    );
  });

  const renderItem = ({ item }: { item: AdminUserProfile }) => {
    const isBanned = item.is_banned;
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
            <TouchableOpacity onPress={() => handleRevokeBan(item.user_id)} style={styles.actionBtn}>
              <MaterialCommunityIcons name="shield-check" size={18} color="#00E676" />
              <Text style={[styles.actionText, { color: '#00E676' }]}>Unban</Text>
            </TouchableOpacity>
          ) : (
            <TouchableOpacity onPress={() => handleBan(item.user_id)} style={styles.actionBtn}>
              <MaterialCommunityIcons name="gavel" size={18} color="#ff4040" />
              <Text style={[styles.actionText, { color: '#ff4040' }]}>Ban</Text>
            </TouchableOpacity>
          )}

          <TouchableOpacity onPress={() => handleResetPassword(item.user_id)} style={styles.actionBtn}>
            <MaterialCommunityIcons name="lock-reset" size={18} color="#FF5A00" />
            <Text style={[styles.actionText, { color: '#FF5A00' }]}>Lockout</Text>
          </TouchableOpacity>

          <TouchableOpacity onPress={() => handleSoftDelete(item.user_id)} style={styles.actionBtn}>
            <MaterialCommunityIcons name="delete-empty" size={18} color="#ff4040" />
            <Text style={[styles.actionText, { color: '#ff4040' }]}>Soft Delete</Text>
          </TouchableOpacity>
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

        {loading && !isRefreshing ? (
          <ActivityIndicator size="large" color="#00f0ff" style={{ marginTop: Spacing.xl }} />
        ) : (
          <FlatList
            data={filteredUsers}
            renderItem={renderItem}
            keyExtractor={(i) => i.user_id}
            contentContainerStyle={styles.list}
            refreshing={isRefreshing}
            onRefresh={() => {
              setIsRefreshing(true);
              fetchUsers();
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
