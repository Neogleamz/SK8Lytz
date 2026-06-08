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

export interface HardwareBlacklistPanelProps {
  visible: boolean;
  onClose: () => void;
  bg: string;
  cardBg: string;
  borderColor: string;
  textPrimary: string;
  textMuted: string;
}

interface BlacklistedDevice {
  mac_address: string;
  reason: string;
  added_by: string;
  created_at: string;
}

export function HardwareBlacklistPanel({
  visible,
  onClose,
  bg,
  cardBg,
  borderColor,
  textPrimary,
  textMuted,
}: HardwareBlacklistPanelProps) {
  const [blacklist, setBlacklist] = useState<BlacklistedDevice[]>([]);
  const [loading, setLoading] = useState(true);
  const [isRefreshing, setIsRefreshing] = useState(false);
  const [newMac, setNewMac] = useState('');
  const [newReason, setNewReason] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const fetchBlacklist = async () => {
    try {
      setLoading(true);
      const { data, error } = await supabase
        .from('hardware_blacklist')
        .select('*')
        .order('created_at', { ascending: false });

      if (error) throw error;
      setBlacklist(data as BlacklistedDevice[]);
    } catch (e: unknown) {
      AppLogger.error('Failed to fetch hardware blacklist', e instanceof Error ? e.message : String(e));
      Alert.alert('Error', 'Failed to fetch hardware blacklist: ' + (e instanceof Error ? e.message : String(e)));
    } finally {
      setLoading(false);
      setIsRefreshing(false);
    }
  };

  useEffect(() => {
    if (visible) {
      fetchBlacklist();
    }
  }, [visible]);

  const handleAdd = async () => {
    if (!newMac.trim() || !newReason.trim()) {
      Alert.alert('Error', 'Both MAC Address and Reason are required.');
      return;
    }
    
    // Basic MAC address validation
    const macRegex = /^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$/;
    const formattedMac = newMac.trim().toUpperCase().replace(/-/g, ':');
    
    if (!macRegex.test(formattedMac)) {
      Alert.alert('Error', 'Invalid MAC Address format. Use XX:XX:XX:XX:XX:XX');
      return;
    }

    try {
      setIsSubmitting(true);
      const { error } = await supabase.rpc('admin_add_hardware_blacklist', {
        p_mac_address: formattedMac,
        p_reason: newReason.trim()
      });

      if (error) throw error;
      
      Alert.alert('Success', 'Device added to blacklist.');
      setNewMac('');
      setNewReason('');
      fetchBlacklist();
    } catch (e: unknown) {
      Alert.alert('Failed to Add', (e instanceof Error ? e.message : String(e)));
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleRemove = (mac: string) => {
    Alert.alert(
      'Remove from Blacklist',
      `Are you sure you want to remove ${mac} from the blacklist?`,
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Remove',
          style: 'destructive',
          onPress: async () => {
            try {
              const { error } = await supabase.rpc('admin_remove_hardware_blacklist', {
                p_mac_address: mac
              });
              if (error) throw error;
              Alert.alert('Success', 'Device removed from blacklist.');
              fetchBlacklist();
            } catch (e: unknown) {
              Alert.alert('Failed', (e instanceof Error ? e.message : String(e)));
            }
          },
        },
      ]
    );
  };

  const renderItem = ({ item }: { item: BlacklistedDevice }) => {
    return (
      <View style={[styles.card, { backgroundColor: cardBg, borderColor }]}>
        <View style={styles.cardHeader}>
          <View style={{flexDirection: 'row', alignItems: 'center'}}>
            <MaterialCommunityIcons name="chip" size={20} color="#ff4040" style={{marginRight: 8}} />
            <Text style={[styles.macText, { color: textPrimary }]}>
              {item.mac_address}
            </Text>
          </View>
          <Text style={[styles.dateText, { color: textMuted }]}>
            {new Date(item.created_at).toLocaleDateString()}
          </Text>
        </View>

        <Text style={[styles.reasonText, { color: textMuted }]}>
          Reason: {item.reason}
        </Text>

        <View style={styles.actionRow}>
          <TouchableOpacity onPress={() => handleRemove(item.mac_address)} style={styles.actionBtn}>
            <MaterialCommunityIcons name="delete-outline" size={18} color="#ff4040" />
            <Text style={[styles.actionText, { color: '#ff4040' }]}>Remove</Text>
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
          <Text style={[styles.headerTitle, { color: textPrimary }]}>Hardware Blacklist</Text>
        </View>

        <View style={[styles.formContainer, { backgroundColor: cardBg, borderColor }]}>
          <Text style={[styles.formTitle, { color: textPrimary }]}>Add to Blacklist</Text>
          <TextInput
            style={[styles.input, { color: textPrimary, borderColor }]}
            placeholder="MAC Address (XX:XX:XX:XX:XX:XX)"
            placeholderTextColor={textMuted}
            value={newMac}
            onChangeText={setNewMac}
            autoCapitalize="characters"
          />
          <TextInput
            style={[styles.input, { color: textPrimary, borderColor, marginTop: Spacing.sm }]}
            placeholder="Reason (e.g. Stolen, Malicious)"
            placeholderTextColor={textMuted}
            value={newReason}
            onChangeText={setNewReason}
          />
          <TouchableOpacity 
            style={[styles.submitBtn, { opacity: isSubmitting ? 0.7 : 1 }]} 
            onPress={handleAdd}
            disabled={isSubmitting}
          >
            {isSubmitting ? (
              <ActivityIndicator color="#000" size="small" />
            ) : (
              <Text style={styles.submitBtnText}>Add Device</Text>
            )}
          </TouchableOpacity>
        </View>

        {loading && !isRefreshing ? (
          <ActivityIndicator size="large" color="#ff4040" style={{ marginTop: Spacing.xl }} />
        ) : (
          <FlatList
            data={blacklist}
            renderItem={renderItem}
            keyExtractor={(i) => i.mac_address}
            contentContainerStyle={styles.list}
            refreshing={isRefreshing}
            onRefresh={() => {
              setIsRefreshing(true);
              fetchBlacklist();
            }}
            ListEmptyComponent={<Text style={{color: textMuted, textAlign: 'center', marginTop: 20}}>No devices currently blacklisted.</Text>}
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
  formContainer: {
    padding: Spacing.md,
    borderBottomWidth: 1,
  },
  formTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: Spacing.sm,
  },
  input: {
    borderWidth: 1,
    borderRadius: 8,
    padding: Spacing.md,
    fontSize: 15,
  },
  submitBtn: {
    backgroundColor: '#ff4040',
    padding: Spacing.md,
    borderRadius: 8,
    alignItems: 'center',
    marginTop: Spacing.md,
  },
  submitBtnText: {
    color: '#FFF',
    fontWeight: 'bold',
    fontSize: 15,
  },
  list: {
    padding: Spacing.md,
    paddingBottom: 100,
  },
  card: {
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
  macText: { 
    fontSize: 15, 
    fontWeight: 'bold',
    fontFamily: 'monospace',
  },
  dateText: { fontSize: 12 },
  reasonText: { fontSize: 14, marginBottom: Spacing.sm },
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
