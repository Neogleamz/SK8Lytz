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
  const [loading, setLoading] = useState(true);
  const [isRefreshing, setIsRefreshing] = useState(false);

  const fetchAdmins = async () => {
    try {
      setLoading(true);
      const { data, error } = await supabase
        .from('user_profiles')
        .select('*')
        .eq('role', 'admin')
        .order('created_at', { ascending: false });

      if (error) throw error;
      setAdmins(data as AdminProfile[]);
    } catch (e: unknown) {
      AppLogger.error('Failed to fetch admins for roster panel', e instanceof Error ? e.message : String(e));
      Alert.alert('Error', 'Failed to fetch admins: ' + (e instanceof Error ? e.message : String(e)));
    } finally {
      setLoading(false);
      setIsRefreshing(false);
    }
  };

  useEffect(() => {
    if (visible) {
      fetchAdmins();
    }
  }, [visible]);

  const handleRevokeAdmin = (userId: string, username: string | null) => {
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
  };

  const renderItem = ({ item }: { item: AdminProfile }) => {
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
          <TouchableOpacity onPress={() => handleRevokeAdmin(item.user_id, item.username)} style={styles.actionBtn}>
            <MaterialCommunityIcons name="shield-remove-outline" size={18} color="#ff4040" />
            <Text style={[styles.actionText, { color: '#ff4040' }]}>Revoke Admin</Text>
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
          <Text style={[styles.headerTitle, { color: textPrimary }]}>Admin Roster</Text>
        </View>

        {loading && !isRefreshing ? (
          <ActivityIndicator size="large" color="#FFD700" style={{ marginTop: Spacing.xl }} />
        ) : (
          <FlatList removeClippedSubviews={true} initialNumToRender={12} windowSize={5}
            data={admins}
            renderItem={renderItem}
            keyExtractor={(i) => i.user_id}
            contentContainerStyle={styles.list}
            refreshing={isRefreshing}
            onRefresh={() => {
              setIsRefreshing(true);
              fetchAdmins();
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
