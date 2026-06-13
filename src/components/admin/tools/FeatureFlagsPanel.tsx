import { MaterialCommunityIcons } from '@expo/vector-icons';
import React, { useCallback, useEffect, useMemo, useState } from 'react';
import {
    ActivityIndicator,
    Alert,
    FlatList,
    Modal,
    StyleSheet,
    Switch,
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

export interface FeatureFlagsPanelProps {
  visible: boolean;
  onClose: () => void;
  bg: string;
  cardBg: string;
  borderColor: string;
  textPrimary: string;
  textMuted: string;
}

interface FeatureFlag {
  id: string;
  flag_key: string;
  target_user_id: string | null;
  is_enabled: boolean;
  created_at: string;
}

const FeatureFlagCard = React.memo(({
  item,
  cardBg,
  borderColor,
  textPrimary,
  textMuted,
  onToggle,
  onRemove,
}: {
  item: FeatureFlag;
  cardBg: string;
  borderColor: string;
  textPrimary: string;
  textMuted: string;
  onToggle: (id: string, currentVal: boolean) => void;
  onRemove: (id: string) => void;
}) => {
  const isGlobal = !item.target_user_id;
  const handleTogglePress = useCallback(() => {
    onToggle(item.id, item.is_enabled);
  }, [onToggle, item.id, item.is_enabled]);

  const handleRemovePress = useCallback(() => {
    onRemove(item.id);
  }, [onRemove, item.id]);

  return (
    <View style={[styles.card, { backgroundColor: cardBg, borderColor }]}>
      <View style={styles.cardHeader}>
        <View style={{flex: 1, marginRight: Spacing.md}}>
          <Text style={[styles.keyText, { color: textPrimary }]}>
            {item.flag_key}
          </Text>
          <Text style={[styles.targetText, { color: isGlobal ? '#00f0ff' : textMuted }]}>
            {isGlobal ? 'GLOBAL TARGET' : `USER: ${item.target_user_id}`}
          </Text>
        </View>
        <Switch
          value={item.is_enabled}
          onValueChange={handleTogglePress}
          trackColor={{ false: '#444', true: '#00f0ff' }}
        />
      </View>

      <View style={styles.actionRow}>
        <TouchableOpacity onPress={handleRemovePress} style={styles.actionBtn}>
          <MaterialCommunityIcons name="delete-outline" size={18} color="#ff4040" />
          <Text style={[styles.actionText, { color: '#ff4040' }]}>Delete</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
});

export function FeatureFlagsPanel({
  visible,
  onClose,
  bg,
  cardBg,
  borderColor,
  textPrimary,
  textMuted,
}: FeatureFlagsPanelProps) {
  type FlagPanelStatus = 'idle' | 'loading' | 'refreshing' | 'submitting' | 'error';
  const [status, setStatus] = useState<FlagPanelStatus>('loading');
  const loading = status === 'loading';
  const isRefreshing = status === 'refreshing';
  const isSubmitting = status === 'submitting';
  
  const [flags, setFlags] = useState<FeatureFlag[]>([]);
  const [newKey, setNewKey] = useState('');
  const [newUserId, setNewUserId] = useState('');

  const fetchFlags = useCallback(async () => {
    try {
      setStatus(prev => prev === 'idle' ? 'refreshing' : 'loading');
      const { data, error } = await supabase
        .from('feature_flags')
        .select('*')
        .order('created_at', { ascending: false });

      if (error) throw error;
      setFlags(data as FeatureFlag[]);
    } catch (e: unknown) {
      AppLogger.error('Failed to fetch feature flags', e, { payload_size: 0, ssi: 0 });
      Alert.alert('Error', 'Failed to fetch feature flags: ' + (e instanceof Error ? e.message : String(e)));
      setStatus('error');
    } finally {
      setStatus('idle');
    }
  }, []);

  useEffect(() => {
    if (visible) {
      fetchFlags();
    }
  }, [visible, fetchFlags]);

  const handleAdd = useCallback(async () => {
    if (!newKey.trim()) {
      Alert.alert('Error', 'Flag Key is required.');
      return;
    }

    try {
      setStatus('submitting');
      const targetUserId = newUserId.trim() || null;

      const { error } = await supabase
        .from('feature_flags')
        .insert([{
          flag_key: newKey.trim(),
          target_user_id: targetUserId,
          is_enabled: false
        }]);

      if (error) throw error;
      
      Alert.alert('Success', 'Feature flag created.');
      setNewKey('');
      setNewUserId('');
      fetchFlags();
    } catch (e: unknown) {
      Alert.alert('Failed to Create', (e instanceof Error ? e.message : String(e)));
      setStatus('idle');
    }
  }, [newKey, newUserId, fetchFlags]);

  const handleToggle = useCallback(async (id: string, currentVal: boolean) => {
    try {
      setStatus('submitting');
      const { error } = await supabase
        .from('feature_flags')
        .update({ is_enabled: !currentVal })
        .eq('id', id);

      if (error) throw error;
      fetchFlags();
    } catch (e: unknown) {
      Alert.alert('Failed to Update', (e instanceof Error ? e.message : String(e)));
      setStatus('idle');
    }
  }, [fetchFlags]);

  const handleRemove = useCallback((id: string) => {
    Alert.alert(
      'Delete Feature Flag',
      `Are you sure you want to delete this feature flag?`,
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Delete',
          style: 'destructive',
          onPress: async () => {
            try {
              const { error } = await supabase.from('feature_flags').delete().eq('id', id);
              if (error) throw error;
              fetchFlags();
            } catch (e: unknown) {
              Alert.alert('Failed', (e instanceof Error ? e.message : String(e)));
            }
          },
        },
      ]
    );
  }, [fetchFlags]);

  const renderItem = useCallback(({ item }: { item: FeatureFlag }) => {
    return (
      <FeatureFlagCard
        item={item}
        cardBg={cardBg}
        borderColor={borderColor}
        textPrimary={textPrimary}
        textMuted={textMuted}
        onToggle={handleToggle}
        onRemove={handleRemove}
      />
    );
  }, [cardBg, borderColor, textPrimary, textMuted, handleToggle, handleRemove]);

  const handleRefresh = useCallback(() => {
    fetchFlags();
  }, [fetchFlags]);

  const renderEmpty = useCallback(() => (
    <Text style={{color: textMuted, textAlign: 'center', marginTop: 20}}>No feature flags found.</Text>
  ), [textMuted]);

  const keyExtractor = useCallback((i: FeatureFlag) => i.id, []);

  return (
    <Modal visible={visible} animationType="slide" presentationStyle="fullScreen" onRequestClose={onClose}>
      <SafeAreaView style={[styles.root, { backgroundColor: bg }]}>
        <View style={[styles.header, { borderBottomColor: borderColor }]}>
          <TouchableOpacity onPress={onClose} style={styles.closeBtn}>
            <MaterialCommunityIcons name="arrow-left" size={24} color={textPrimary} />
          </TouchableOpacity>
          <Text style={[styles.headerTitle, { color: textPrimary }]}>Feature Flags</Text>
        </View>

        <View style={[styles.formContainer, { backgroundColor: cardBg, borderColor }]}>
          <Text style={[styles.formTitle, { color: textPrimary }]}>Create Flag</Text>
          <TextInput
            style={[styles.input, { color: textPrimary, borderColor }]}
            placeholder="Flag Key (e.g. enable_street_mode_v2)"
            placeholderTextColor={textMuted}
            value={newKey}
            onChangeText={setNewKey}
            autoCapitalize="none"
          />
          <TextInput
            style={[styles.input, { color: textPrimary, borderColor, marginTop: Spacing.sm }]}
            placeholder="Target User UUID (Leave blank for GLOBAL)"
            placeholderTextColor={textMuted}
            value={newUserId}
            onChangeText={setNewUserId}
          />
          <TouchableOpacity 
            style={[styles.submitBtn, { opacity: isSubmitting ? 0.7 : 1 }]} 
            onPress={handleAdd}
            disabled={isSubmitting}
          >
            {isSubmitting ? (
              <ActivityIndicator color="#000" size="small" />
            ) : (
              <Text style={styles.submitBtnText}>Create Flag</Text>
            )}
          </TouchableOpacity>
        </View>

        {status === 'error' ? (
          <ErrorCard message="Failed to load. Tap to retry." onRetry={fetchFlags} />
        ) : loading && !isRefreshing ? (
          <ActivityIndicator size="large" color="#00f0ff" style={{ marginTop: Spacing.xl }} />
        ) : (
          <FlatList removeClippedSubviews={true} initialNumToRender={12} windowSize={5}
            data={flags}
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
    backgroundColor: '#00f0ff',
    padding: Spacing.md,
    borderRadius: 8,
    alignItems: 'center',
    marginTop: Spacing.md,
  },
  submitBtnText: {
    color: '#000',
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
  keyText: { 
    fontSize: 15, 
    fontWeight: 'bold',
    fontFamily: 'monospace',
  },
  targetText: { fontSize: 12, marginTop: 4, fontWeight: '600' },
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
