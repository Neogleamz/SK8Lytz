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
  SafeAreaView,
  TextInput
} from 'react-native';
import DateTimePicker from '@react-native-community/datetimepicker';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { supabase } from '../services/supabaseClient';
import PositionalGradientBuilder from './PositionalGradientBuilder';
import { BuilderNode } from '../protocols/PositionalMathBuffer';
import { useTheme } from '../context/ThemeContext';

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

  // --- Create Pick Modal State ---
  const [createModalVisible, setCreateModalVisible] = useState(false);
  const [createName, setCreateName] = useState('');
  const [createMode, setCreateMode] = useState<'BUILDER' | 'PROGRAMS'>('BUILDER');
  const [createPatternId, setCreatePatternId] = useState<number>(1);
  const [createSpeed, setCreateSpeed] = useState<number>(50);
  const [createBrightness, setCreateBrightness] = useState<number>(100);

  // Builder Specific State
  const [builderNodes, setBuilderNodes] = useState<BuilderNode[]>([
      { id: 'n1', position: 0, colorHex: '#FF0000' },
      { id: 'n2', position: 100, colorHex: '#0000FF' }
  ]);
  const [builderFillMode, setBuilderFillMode] = useState<'GRADIENT' | 'SOLID'>('GRADIENT');
  const [builderTransitionType, setBuilderTransitionType] = useState<number>(2);
  const [builderDirection, setBuilderDirection] = useState<number>(1);

  const handleCreatePick = async () => {
    if (!createName.trim()) {
      Alert.alert('Validation Error', 'Pick must have a name.');
      return;
    }
    
    setIsLoading(true);
    try {
      let insertData: Record<string, unknown> = {
        name: createName.trim(),
        custom_name: createName.trim(),
        is_active: false,
        speed: createSpeed,
        brightness: createBrightness,
      };

      if (createMode === 'BUILDER') {
        insertData.mode = 'MULTI';
        insertData.multi_colors = builderNodes.map(n => n.colorHex);
        insertData.multi_transition = builderTransitionType;
        insertData.multi_length = 16;
      } else {
        insertData.mode = 'PROGRAMS';
        insertData.pattern_id = createPatternId;
      }

      const { error } = await supabase.from('sk8lytz_picks').insert(insertData as any);
      if (error) throw error;
      
      setCreateModalVisible(false);
      setCreateName('');
      fetchPicks();
    } catch (err: unknown) {
      Alert.alert('Create Error', err instanceof Error ? err.message : String(err));
    } finally {
      setIsLoading(false);
    }
  };

  const handleDeletePick = async (pickId: string) => {
    Alert.alert('Delete Pick', 'Are you sure you want to permanently delete this Pick?', [
      { text: 'Cancel', style: 'cancel' },
      { 
        text: 'Delete', 
        style: 'destructive', 
        onPress: async () => {
          setIsLoading(true);
          try {
            const { error } = await supabase.from('sk8lytz_picks').delete().eq('id', pickId);
            if (error) throw error;
            fetchPicks();
          } catch(err: unknown) {
            Alert.alert('Delete Error', err instanceof Error ? err.message : String(err));
            setIsLoading(false);
          }
        }
      }
    ]);
  };

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

  const { Colors, isDark } = useTheme();
  const bg = Colors.background;
  const textPrimary = Colors.text;
  const textMuted = Colors.textMuted;
  const cardBg = Colors.surface;
  const borderColor = Colors.surfaceHighlight;

  return (
    <Modal visible={visible} animationType="slide" presentationStyle="fullScreen" onRequestClose={onClose}>
      <SafeAreaView style={[styles.root, { backgroundColor: bg }]}>
        {/* ── Canonical Admin Header ── */}
        <View style={[styles.header, { borderBottomColor: borderColor, backgroundColor: Colors.surface }]}>
          <TouchableOpacity onPress={onClose} style={styles.backBtn}>
            <MaterialCommunityIcons name="arrow-left" size={24} color={Colors.primary} />
          </TouchableOpacity>
          <View style={{ flex: 1 }}>
            <Text style={[styles.title, { color: Colors.text }]}>📅 PICKS SCHEDULER</Text>
            <Text style={{ color: textMuted, fontSize: 11, marginTop: 2 }}>Manage and schedule SK8Lytz spotlight picks</Text>
          </View>
          <TouchableOpacity
            onPress={() => setCreateModalVisible(true)}
            style={{ backgroundColor: Colors.primary, paddingHorizontal: 12, paddingVertical: 6, borderRadius: 8 }}
          >
            <Text style={{ color: isDark ? '#000' : '#FFF', fontWeight: '900', fontSize: 12, letterSpacing: 0.5 }}>+ NEW</Text>
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
                    <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                      <Text style={[styles.pickName, { color: textPrimary }]}>{pick.custom_name || pick.name}</Text>
                      <TouchableOpacity onPress={() => handleDeletePick(pick.id)}>
                        <MaterialCommunityIcons name="trash-can-outline" size={16} color="#ff4040" />
                      </TouchableOpacity>
                    </View>
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
         {/* Create New Pick Modal */}
         <Modal visible={createModalVisible} animationType="slide" presentationStyle="pageSheet" onRequestClose={() => setCreateModalVisible(false)}>
           <SafeAreaView style={{ flex: 1, backgroundColor: '#1E1E1E' }}>
             <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', padding: 16, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.1)' }}>
               <Text style={{ color: '#FFF', fontSize: 18, fontWeight: 'bold' }}>Create New Pick</Text>
               <TouchableOpacity onPress={() => setCreateModalVisible(false)}>
                 <MaterialCommunityIcons name="close" size={24} color="#FFF" />
               </TouchableOpacity>
             </View>
             
             <ScrollView style={{ flex: 1, padding: 16 }}>
               <Text style={{ color: '#999', marginBottom: 4, fontWeight: 'bold' }}>PICK NAME</Text>
               <TextInput 
                 value={createName}
                 onChangeText={setCreateName}
                 placeholder="e.g., Neon Cyberpunk"
                 placeholderTextColor="#666"
                 style={{ backgroundColor: 'rgba(255,255,255,0.1)', color: '#FFF', padding: 12, borderRadius: 8, marginBottom: 16, fontSize: 16 }}
               />

               <Text style={{ color: '#999', marginBottom: 4, fontWeight: 'bold' }}>MODE TYPE</Text>
               <View style={{ flexDirection: 'row', gap: 10, marginBottom: 16 }}>
                 <TouchableOpacity onPress={() => setCreateMode('BUILDER')} style={{ flex: 1, padding: 12, borderRadius: 8, backgroundColor: createMode === 'BUILDER' ? '#00AAFF' : 'rgba(255,255,255,0.1)' }}>
                   <Text style={{ color: '#FFF', textAlign: 'center', fontWeight: 'bold' }}>Array Builder</Text>
                 </TouchableOpacity>
                 <TouchableOpacity onPress={() => setCreateMode('PROGRAMS')} style={{ flex: 1, padding: 12, borderRadius: 8, backgroundColor: createMode === 'PROGRAMS' ? '#00AAFF' : 'rgba(255,255,255,0.1)' }}>
                   <Text style={{ color: '#FFF', textAlign: 'center', fontWeight: 'bold' }}>Program Pattern</Text>
                 </TouchableOpacity>
               </View>

               {createMode === 'BUILDER' && (
                 <View style={{ backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 12, padding: 12, marginBottom: 16 }}>
                   <Text style={{ color: '#FFF', fontWeight: 'bold', marginBottom: 8 }}>Array Builder</Text>
                   <PositionalGradientBuilder 
                     nodes={builderNodes}
                     onNodesChange={setBuilderNodes}
                     fillMode={builderFillMode}
                     onFillModeChange={setBuilderFillMode}
                     transitionType={builderTransitionType}
                     onTransitionTypeChange={setBuilderTransitionType}
                     direction={builderDirection}
                     onDirectionChange={setBuilderDirection}
                     speed={createSpeed}
                     deviceLedCount={16}
                     selectedColor="#FFFFFF"
                   />
                 </View>
               )}

               {createMode === 'PROGRAMS' && (
                 <View style={{ backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 12, padding: 12, marginBottom: 16 }}>
                   <Text style={{ color: '#FFF', fontWeight: 'bold', marginBottom: 8 }}>Pattern ID</Text>
                   <View style={{ flexDirection: 'row', alignItems: 'center', gap: 10 }}>
                     <TouchableOpacity onPress={() => setCreatePatternId(Math.max(1, createPatternId - 1))} style={{ padding: 12, backgroundColor: 'rgba(255,255,255,0.1)', borderRadius: 8 }}>
                       <MaterialCommunityIcons name="minus" size={20} color="#FFF" />
                     </TouchableOpacity>
                     <Text style={{ color: '#FFF', fontSize: 24, fontWeight: 'bold', flex: 1, textAlign: 'center' }}>{createPatternId}</Text>
                     <TouchableOpacity onPress={() => setCreatePatternId(Math.min(100, createPatternId + 1))} style={{ padding: 12, backgroundColor: 'rgba(255,255,255,0.1)', borderRadius: 8 }}>
                       <MaterialCommunityIcons name="plus" size={20} color="#FFF" />
                     </TouchableOpacity>
                   </View>
                 </View>
               )}

               <Text style={{ color: '#999', marginBottom: 4, fontWeight: 'bold' }}>SPEED ({createSpeed}%)</Text>
               <View style={{ flexDirection: 'row', alignItems: 'center', gap: 10, marginBottom: 16 }}>
                 <TouchableOpacity onPress={() => setCreateSpeed(Math.max(0, createSpeed - 5))} style={{ padding: 10, backgroundColor: 'rgba(255,255,255,0.1)', borderRadius: 8 }}>
                   <MaterialCommunityIcons name="minus" size={16} color="#FFF" />
                 </TouchableOpacity>
                 <Text style={{ color: '#FFF', flex: 1, textAlign: 'center' }}>Speed</Text>
                 <TouchableOpacity onPress={() => setCreateSpeed(Math.min(100, createSpeed + 5))} style={{ padding: 10, backgroundColor: 'rgba(255,255,255,0.1)', borderRadius: 8 }}>
                   <MaterialCommunityIcons name="plus" size={16} color="#FFF" />
                 </TouchableOpacity>
               </View>

               <Text style={{ color: '#999', marginBottom: 4, fontWeight: 'bold' }}>BRIGHTNESS ({createBrightness}%)</Text>
               <View style={{ flexDirection: 'row', alignItems: 'center', gap: 10, marginBottom: 30 }}>
                 <TouchableOpacity onPress={() => setCreateBrightness(Math.max(0, createBrightness - 5))} style={{ padding: 10, backgroundColor: 'rgba(255,255,255,0.1)', borderRadius: 8 }}>
                   <MaterialCommunityIcons name="minus" size={16} color="#FFF" />
                 </TouchableOpacity>
                 <Text style={{ color: '#FFF', flex: 1, textAlign: 'center' }}>Brightness</Text>
                 <TouchableOpacity onPress={() => setCreateBrightness(Math.min(100, createBrightness + 5))} style={{ padding: 10, backgroundColor: 'rgba(255,255,255,0.1)', borderRadius: 8 }}>
                   <MaterialCommunityIcons name="plus" size={16} color="#FFF" />
                 </TouchableOpacity>
               </View>

             </ScrollView>

             <View style={{ padding: 16, borderTopWidth: 1, borderTopColor: 'rgba(255,255,255,0.1)' }}>
               <TouchableOpacity onPress={handleCreatePick} style={{ backgroundColor: '#00E676', padding: 16, borderRadius: 12, alignItems: 'center' }}>
                 <Text style={{ color: '#000', fontSize: 16, fontWeight: 'bold' }}>Publish New Pick</Text>
               </TouchableOpacity>
             </View>
           </SafeAreaView>
         </Modal>

      </SafeAreaView>
    </Modal>
  );
}

const styles = StyleSheet.create({
  root: { flex: 1 },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 16,
    paddingTop: 16,
    paddingBottom: 16,
    borderBottomWidth: 1,
  },
  backBtn: { marginRight: 16, padding: 4 },
  title: { fontSize: 18, fontWeight: '900', letterSpacing: 1.5, textTransform: 'uppercase' },
  content: { flex: 1, padding: 16 },
  instructions: { fontSize: 12, lineHeight: 18, marginBottom: 16 },
  card: { padding: 16, borderRadius: 12, borderWidth: 1, marginBottom: 16 },
  cardHeader: { flexDirection: 'row', alignItems: 'flex-start', justifyContent: 'space-between', marginBottom: 16 },
  pickName: { fontSize: 16, fontWeight: '800' },
  pickMode: { fontSize: 12, marginTop: 4 },
  dateRow: { flexDirection: 'row', alignItems: 'center', marginVertical: 6 },
  dateChip: { flexDirection: 'row', alignItems: 'center', paddingHorizontal: 12, paddingVertical: 6, borderRadius: 6, borderWidth: 1 },
  addDateBtn: { flexDirection: 'row', alignItems: 'center', paddingHorizontal: 12, paddingVertical: 6, borderRadius: 6, backgroundColor: 'rgba(0,240,255,0.15)', borderWidth: 1, borderColor: 'rgba(0,240,255,0.3)' },
  iosDatePickerOverlay: {
    position: 'absolute', bottom: 0, left: 0, right: 0,
    paddingBottom: 30, borderTopWidth: 1,
  },
  iosDatePickerActions: { flexDirection: 'row', justifyContent: 'flex-end', paddingHorizontal: 16, borderBottomWidth: 1 }
});
