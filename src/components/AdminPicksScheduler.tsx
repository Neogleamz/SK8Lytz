import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  Modal,
  TouchableOpacity,
  ScrollView,
  Switch,
  Platform,
  Alert,
  SafeAreaView
} from 'react-native';
import DateTimePicker from '@react-native-community/datetimepicker';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { supabase } from '../utils/supabase';

interface Sk8LytzPick {
  id: string;
  name: string;
  custom_name: string | null;
  mode: string;
  active_from: string | null;
  active_until: string | null;
  is_active: boolean;
  sort_order: number;
}

interface AdminPicksSchedulerProps {
  visible: boolean;
  onClose: () => void;
}

export default function AdminPicksScheduler({ visible, onClose }: AdminPicksSchedulerProps) {
  const [picks, setPicks] = useState<Sk8LytzPick[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [datePickerConfig, setDatePickerConfig] = useState<{
    visible: boolean;
    pickId: string;
    field: 'active_from' | 'active_until';
    currentDate: Date;
  } | null>(null);

  useEffect(() => {
    if (visible) fetchPicks();
  }, [visible]);

  const fetchPicks = async () => {
    setIsLoading(true);
    try {
      const { data, error } = await supabase
        .from('sk8lytz_picks')
        .select('*')
        .order('sort_order', { ascending: true });

      if (error) throw error;
      if (data) setPicks(data as Sk8LytzPick[]);
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : String(err);
      Alert.alert('Load Error', msg);
    } finally {
      setIsLoading(false);
    }
  };

  const handleToggleActive = async (pickId: string, currentStatus: boolean) => {
    try {
      // Optimistic update
      setPicks(prev => prev.map(p => p.id === pickId ? { ...p, is_active: !currentStatus } : p));
      
      const { error } = await supabase
        .from('sk8lytz_picks')
        .update({ is_active: !currentStatus })
        .eq('id', pickId);

      if (error) {
        throw error;
      }
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : String(err);
      Alert.alert('Update Error', msg);
      // Rollback
      fetchPicks();
    }
  };

  const openDatePicker = (pickId: string, field: 'active_from' | 'active_until', currentValue: string | null) => {
    let d = new Date();
    if (currentValue) {
      const parsed = new Date(currentValue);
      if (!isNaN(parsed.getTime())) d = parsed;
    }
    setDatePickerConfig({
      visible: true,
      pickId,
      field,
      currentDate: d
    });
  };

  const onDateChanged = async (event: any, selectedDate?: Date) => {
    if (Platform.OS !== 'ios') {
       // Android closes immediately after selection
       setDatePickerConfig(null);
    }
    if (event.type === 'dismissed' || !selectedDate || !datePickerConfig) return;
    
    // We only need YYYY-MM-DD
    const isoString = selectedDate.toISOString(); 
    // In timezone of user, actually might be better to keep local YYYY-MM-DD
    const yyyyMmDd = isoString.split('T')[0];

    try {
      setPicks(prev => prev.map(p => p.id === datePickerConfig.pickId ? { ...p, [datePickerConfig.field]: yyyyMmDd } : p));

      const { error } = await supabase
        .from('sk8lytz_picks')
        .update({ [datePickerConfig.field]: yyyyMmDd })
        .eq('id', datePickerConfig.pickId);

      if (error) throw error;
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : String(err);
      Alert.alert('Save Error', msg);
      fetchPicks();
    }
  };

  const clearDate = async (pickId: string, field: 'active_from' | 'active_until') => {
    try {
      setPicks(prev => prev.map(p => p.id === pickId ? { ...p, [field]: null } : p));

      const { error } = await supabase
        .from('sk8lytz_picks')
        .update({ [field]: null })
        .eq('id', pickId);

      if (error) throw error;
    } catch (err: unknown) {
       Alert.alert('Error clearing time', err instanceof Error ? err.message : String(err));
       fetchPicks();
    }
  };

  const bg = '#FFFFFF';
  const textPrimary = '#000000';
  const textMuted = '#444444';
  const cardBg = '#F8F8F8';
  const borderColor = '#CCCCCC';

  return (
    <Modal visible={visible} animationType="slide" presentationStyle="fullScreen" onRequestClose={onClose}>
      <SafeAreaView style={[styles.root, { backgroundColor: bg }]}>
        <View style={[styles.header, { borderBottomColor: borderColor }]}>
          <Text style={[styles.title, { color: textPrimary }]}>🗓️ SK8Lytz Picks Scheduler</Text>
          <TouchableOpacity onPress={onClose} style={styles.closeBtn}>
            <MaterialCommunityIcons name="close" size={24} color={textPrimary} />
          </TouchableOpacity>
        </View>

        <ScrollView style={styles.content}>
          <Text style={[styles.instructions, { color: textMuted }]}>
            Toggle 'Active' to enable a pick. If dates are set, it will only appear publicly between those dates while Active.
          </Text>

          {isLoading && picks.length === 0 ? (
            <Text style={{ textAlign: 'center', marginTop: 40, color: textMuted }}>Loading picks...</Text>
          ) : (
            picks.map((pick) => (
              <View key={pick.id} style={[styles.card, { backgroundColor: cardBg, borderColor }]}>
                <View style={styles.cardHeader}>
                  <View style={{ flex: 1, paddingRight: 10 }}>
                    <Text style={[styles.pickName, { color: textPrimary }]}>{pick.custom_name || pick.name}</Text>
                    <Text style={[styles.pickMode, { color: textMuted }]}>Mode: {pick.mode}</Text>
                  </View>
                  <Switch
                    value={pick.is_active}
                    onValueChange={() => handleToggleActive(pick.id, pick.is_active)}
                    trackColor={{ false: '#767577', true: '#00E676' }}
                    thumbColor="#FFF"
                  />
                </View>

                <View style={{ borderTopWidth: 1, borderTopColor: borderColor, paddingTop: 12 }}>
                  <Text style={{ color: textPrimary, fontSize: 13, fontWeight: '700', marginBottom: 6 }}>Visibility Window</Text>
                  
                  <View style={styles.dateRow}>
                    <Text style={{ width: 45, color: textMuted, fontSize: 13 }}>From:</Text>
                    {pick.active_from ? (
                      <View style={styles.dateChip}>
                        <Text style={{ color: textPrimary, fontSize: 13, marginRight: 8, fontWeight: '600' }}>{pick.active_from}</Text>
                        <TouchableOpacity onPress={() => openDatePicker(pick.id, 'active_from', pick.active_from)}>
                          <MaterialCommunityIcons name="pencil" size={16} color="#00AAFF" />
                        </TouchableOpacity>
                        <TouchableOpacity onPress={() => clearDate(pick.id, 'active_from')} style={{ marginLeft: 12 }}>
                          <MaterialCommunityIcons name="close-circle" size={16} color="#ff4040" />
                        </TouchableOpacity>
                      </View>
                    ) : (
                      <TouchableOpacity onPress={() => openDatePicker(pick.id, 'active_from', null)} style={styles.addDateBtn}>
                        <MaterialCommunityIcons name="calendar-plus" size={14} color="#FFF" style={{ marginRight: 6 }}/>
                        <Text style={{ color: '#FFF', fontSize: 12, fontWeight: '700' }}>Set Start Date</Text>
                      </TouchableOpacity>
                    )}
                  </View>

                  <View style={styles.dateRow}>
                    <Text style={{ width: 45, color: textMuted, fontSize: 13 }}>Until:</Text>
                    {pick.active_until ? (
                      <View style={styles.dateChip}>
                        <Text style={{ color: textPrimary, fontSize: 13, marginRight: 8, fontWeight: '600' }}>{pick.active_until}</Text>
                        <TouchableOpacity onPress={() => openDatePicker(pick.id, 'active_until', pick.active_until)}>
                          <MaterialCommunityIcons name="pencil" size={16} color="#00AAFF" />
                        </TouchableOpacity>
                        <TouchableOpacity onPress={() => clearDate(pick.id, 'active_until')} style={{ marginLeft: 12 }}>
                          <MaterialCommunityIcons name="close-circle" size={16} color="#ff4040" />
                        </TouchableOpacity>
                      </View>
                    ) : (
                      <TouchableOpacity onPress={() => openDatePicker(pick.id, 'active_until', null)} style={styles.addDateBtn}>
                        <MaterialCommunityIcons name="calendar-plus" size={14} color="#FFF" style={{ marginRight: 6 }}/>
                        <Text style={{ color: '#FFF', fontSize: 12, fontWeight: '700' }}>Set End Date</Text>
                      </TouchableOpacity>
                    )}
                  </View>
                </View>
              </View>
            ))
          )}
          <View style={{ height: 40 }} />
        </ScrollView>

        {datePickerConfig?.visible && (
          <View style={Platform.OS === 'ios' ? styles.iosDatePickerOverlay : {}}>
             <DateTimePicker
               value={datePickerConfig.currentDate}
               mode="date"
               display="default"
               onChange={onDateChanged}
             />
             {Platform.OS === 'ios' && (
               <View style={styles.iosDatePickerActions}>
                  <TouchableOpacity onPress={() => setDatePickerConfig(null)} style={{ padding: 12 }}>
                     <Text style={{ color: '#00AAFF', fontSize: 16, fontWeight: '700' }}>Done</Text>
                  </TouchableOpacity>
               </View>
             )}
          </View>
        )}
      </SafeAreaView>
    </Modal>
  );
}

const styles = StyleSheet.create({
  root: { flex: 1 },
  header: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between',
    paddingHorizontal: 20, paddingBottom: 12, borderBottomWidth: 1, paddingTop: Platform.OS === 'android' ? 10 : 0
  },
  title: { fontSize: 20, fontWeight: '800', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' },
  closeBtn: { padding: 4 },
  content: { flex: 1, padding: 16 },
  instructions: { fontSize: 12, lineHeight: 18, marginBottom: 16, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' },
  card: { padding: 16, borderRadius: 8, borderWidth: 1, marginBottom: 16 },
  cardHeader: { flexDirection: 'row', alignItems: 'flex-start', justifyContent: 'space-between', marginBottom: 16 },
  pickName: { fontSize: 16, fontWeight: '800', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' },
  pickMode: { fontSize: 12, marginTop: 4, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' },
  dateRow: { flexDirection: 'row', alignItems: 'center', marginVertical: 6 },
  dateChip: { flexDirection: 'row', alignItems: 'center', backgroundColor: 'rgba(0,0,0,0.05)', paddingHorizontal: 12, paddingVertical: 6, borderRadius: 6, borderWidth: 1, borderColor: 'rgba(0,0,0,0.1)' },
  addDateBtn: { flexDirection: 'row', alignItems: 'center', backgroundColor: '#000000', paddingHorizontal: 12, paddingVertical: 6, borderRadius: 6 },
  iosDatePickerOverlay: {
    position: 'absolute', bottom: 0, left: 0, right: 0, 
    backgroundColor: '#FFFFFF', paddingBottom: 30, borderTopWidth: 1, borderTopColor: '#CCC'
  },
  iosDatePickerActions: { flexDirection: 'row', justifyContent: 'flex-end', paddingHorizontal: 16, backgroundColor: '#F8F8F8', borderBottomWidth: 1, borderBottomColor: '#EEE' }
});
