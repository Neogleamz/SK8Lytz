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
    Platform,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { AppLogger } from '../../../services/appLogger';
import { supabase } from '../../../services/supabaseClient';
import { Spacing, Typography } from '../../../theme/theme';
import { ErrorCard } from '../../ErrorCard';

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

const HardwareBlacklistCard = React.memo(({
  item,
  cardBg,
  borderColor,
  textPrimary,
  textMuted,
  onRemove,
}: {
  item: BlacklistedDevice;
  cardBg: string;
  borderColor: string;
  textPrimary: string;
  textMuted: string;
  onRemove: (macAddress: string) => void;
}) => {
  const handleRemovePress = useCallback(() => {
    onRemove(item.mac_address);
  }, [onRemove, item.mac_address]);

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
        <TouchableOpacity onPress={handleRemovePress} style={styles.actionBtn}>
          <MaterialCommunityIcons name="delete-outline" size={18} color="#ff4040" />
          <Text style={[styles.actionText, { color: '#ff4040' }]}>Remove</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
});

export function HardwareBlacklistPanel({
  visible,
  onClose,
  bg,
  cardBg,
  borderColor,
  textPrimary,
  textMuted,
}: HardwareBlacklistPanelProps) {
  type BlacklistPanelStatus = 'idle' | 'loading' | 'refreshing' | 'submitting' | 'error';
  const [status, setStatus] = useState<BlacklistPanelStatus>('loading');
  const loading = status === 'loading';
  const isRefreshing = status === 'refreshing';
  const isSubmitting = status === 'submitting';

  const [blacklist, setBlacklist] = useState<BlacklistedDevice[]>([]);
  const [newMac, setNewMac] = useState('');
  const [newReason, setNewReason] = useState('');
  const isMountedRef = useRef(true);

  useEffect(() => {
    isMountedRef.current = true;
    return () => { isMountedRef.current = false; };
  }, []);

  const fetchBlacklist = useCallback(async () => {
    try {
      setStatus(prev => prev === 'idle' ? 'refreshing' : 'loading');
      const { data, error } = await supabase
        .from('hardware_blacklist')
        .select('*')
        .order('created_at', { ascending: false });

      if (error) throw error;
      if (!isMountedRef.current) return;
      setBlacklist(data as BlacklistedDevice[]);
    } catch (e: unknown) {
      if (!isMountedRef.current) return;
      AppLogger.error('Failed to fetch hardware blacklist', e instanceof Error ? e : new Error(String(e)), { payload_size: 0, ssi: 0 });
      Alert.alert('Error', 'Failed to fetch hardware blacklist: ' + (e instanceof Error ? e.message : String(e)));
      setStatus('error');
    } finally {
      if (isMountedRef.current) {
        setStatus('idle');
      }
    }
  }, []);

  useEffect(() => {
    if (visible) {
      fetchBlacklist();
    }
  }, [visible, fetchBlacklist]);

  const handleAdd = useCallback(async () => {
    if (!newMac.trim() || !newReason.trim()) {
      Alert.alert('Error', 'Both MAC Address and Reason are required.');
      return;
    }
    
    const macRegex = /^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$/;
    const formattedMac = newMac.trim().toUpperCase().replace(/-/g, ':');
    
    if (!macRegex.test(formattedMac)) {
      Alert.alert('Error', 'Invalid MAC Address format. Use XX:XX:XX:XX:XX:XX');
      return;
    }

    try {
      setStatus('submitting');
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
      Alert.alert('Failed to Blacklist', (e instanceof Error ? e.message : String(e)));
      setStatus('idle');
    }
  }, [newMac, newReason, fetchBlacklist]);

  const handleRemove = useCallback((macAddress: string) => {
    Alert.alert(
      'Remove from Blacklist',
      `Are you sure you want to remove ${macAddress} from the blacklist?`,
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Remove',
          style: 'destructive',
          onPress: async () => {
            try {
              setStatus('submitting');
              const { error } = await supabase.rpc('admin_remove_hardware_blacklist', {
                p_mac_address: macAddress
              });
              if (error) throw error;
              Alert.alert('Success', 'Device removed from blacklist.');
              fetchBlacklist();
            } catch (e: unknown) {
              Alert.alert('Failed to Remove', (e instanceof Error ? e.message : String(e)));
              setStatus('idle');
            }
          },
        },
      ]
    );
  }, [fetchBlacklist]);

  const renderItem = useCallback(({ item }: { item: BlacklistedDevice }) => {
    return (
      <HardwareBlacklistCard
        item={item}
        cardBg={cardBg}
        borderColor={borderColor}
        textPrimary={textPrimary}
        textMuted={textMuted}
        onRemove={handleRemove}
      />
    );
  }, [cardBg, borderColor, textPrimary, textMuted, handleRemove]);

  const handleRefresh = useCallback(() => {
    fetchBlacklist();
  }, [fetchBlacklist]);

  const renderEmpty = useCallback(() => (
    <Text style={{color: textMuted, textAlign: 'center', marginTop: 20}}>No devices currently blacklisted.</Text>
  ), [textMuted]);

  const keyExtractor = useCallback((i: BlacklistedDevice) => i.mac_address, []);

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

        {status === 'error' ? (
          <ErrorCard message="Failed to load. Tap to retry." onRetry={fetchBlacklist} />
        ) : loading && !isRefreshing ? (
          <ActivityIndicator size="large" color="#ff4040" style={{ marginTop: Spacing.xl }} />
        ) : (
          <FlatList removeClippedSubviews={true} initialNumToRender={12} windowSize={5}
            data={blacklist}
            renderItem={renderItem}
            keyExtractor={keyExtractor}
            contentContainerStyle={styles.list}
            refreshing={isRefreshing}
            onRefresh={handleRefresh}
            ListEmptyComponent={renderEmpty}
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
    fontFamily: Platform.select({ ios: 'Courier', default: 'monospace' }),
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
