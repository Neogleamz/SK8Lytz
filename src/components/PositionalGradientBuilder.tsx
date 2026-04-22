import { Spacing } from '../theme/theme';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import React, { useEffect, useState } from 'react';
import { ActivityIndicator, Alert, Modal, ScrollView, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { useTheme } from '../context/ThemeContext';
import { BuilderNode, CustomBuilderPreset, PositionalMathBuffer } from '../protocols/PositionalMathBuffer';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { AppLogger } from '../services/AppLogger';
import { supabase } from '../services/supabaseClient';
import CustomSlider from './CustomSlider';
import MarqueeText from './MarqueeText';

interface Props {
  nodes: BuilderNode[];
  onNodesChange: (nodes: BuilderNode[]) => void;
  fillMode: 'GRADIENT' | 'SOLID';
  onFillModeChange: (mode: 'GRADIENT' | 'SOLID') => void;
  transitionType: number;
  onTransitionTypeChange: (type: number) => void;
  direction: number;
  onDirectionChange: (dir: number) => void;
  speed: number;
  deviceLedCount: number;
  selectedColor: string; // The universal color passed from DockedController
  writeToDevice?: (payload: number[]) => Promise<void | boolean | 'partial'>;
}

export default function PositionalGradientBuilder({ 
   nodes, onNodesChange, 
   fillMode, onFillModeChange, 
   transitionType, onTransitionTypeChange,
   direction, onDirectionChange,
   speed, deviceLedCount, selectedColor, writeToDevice
}: Props) {
  const { Colors, isDark } = useTheme();

  const [activeNodeId, setActiveNodeId] = useState<string | null>(nodes[0]?.id || null);
  
  // Library State
  const [presets, setPresets] = useState<CustomBuilderPreset[]>([]);
  const [isLoadingLibrary, setIsLoadingLibrary] = useState(true);
  
  // Save Modal State
  const [saveModalVisible, setSaveModalVisible] = useState(false);
  const [presetNameInput, setPresetNameInput] = useState('');
  const [isSaving, setIsSaving] = useState(false);

  // Load Library on Mount
  useEffect(() => {
     loadLibrary();
  }, []);

  const loadLibrary = async () => {
      setIsLoadingLibrary(true);
      try {
          // 1. Load Local
          const localData = await AsyncStorage.getItem('@Sk8lytz_Builder_Presets');
          let localPresets: CustomBuilderPreset[] = [];
          if (localData) {
              try {
                  const parsed = JSON.parse(localData);
                  if (Array.isArray(parsed)) {
                      // Filter out corrupted or legacy caches that don't match the new CustomBuilderPreset interface
                      localPresets = parsed.filter(p => p && p.id && p.name && Array.isArray(p.nodes));
                  }
              } catch (e) {
                  AppLogger.warn('Failed to parse local builder presets', e);
              }
          }
          
          // 2. Load Cloud (if logged in)
          const { data: userAuth } = await supabase.auth.getUser();
          let cloudPresets: CustomBuilderPreset[] = [];
          
          if (userAuth?.user) {
              const { data, error } = await supabase
                  .from('custom_builder_presets')
                  .select('*')
                  .eq('user_id', userAuth.user.id);
                  
              if (!error && data) {
                  // Ensure cloud data matches interface
                  cloudPresets = (data as any as CustomBuilderPreset[]).filter(p => p && p.id && p.name && Array.isArray(p.nodes));
              }
          }
          
          // Merge deduplicating by ID (Cloud wins if collision, though UI shouldn't allow it)
          const merged = [...cloudPresets, ...localPresets.filter(lp => !cloudPresets.find(cp => cp.id === lp.id))];
          setPresets(merged.sort((a,b) => (b.name > a.name ? -1 : 1))); // Alphabetical
      } catch (err) {
          console.error("Error loading builder presets", err);
      } finally {
          setIsLoadingLibrary(false);
      }
  };

  const handleSavePreset = async () => {
      if (!presetNameInput.trim()) return;
      setIsSaving(true);
      
      const newPreset: CustomBuilderPreset = {
          id: `local_${Date.now()}_${Math.random().toString(36).substring(2,9)}`,
          name: presetNameInput.trim(),
          nodes: nodes,
          fill_mode: fillMode,
          transition_type: transitionType
      };

      try {
          const { data: userAuth } = await supabase.auth.getUser();
          
          if (userAuth?.user) {
              // Save to Cloud
              newPreset.id = `cloud_${Date.now()}_${Math.random().toString(36).substring(2,9)}`;
              newPreset.user_id = userAuth.user.id;
              
              const { error } = await supabase.from('custom_builder_presets').insert(newPreset as any);
              if (error) throw error;
          } else {
              // Save to Local Only
              const localData = await AsyncStorage.getItem('@Sk8lytz_Builder_Presets');
              let localPresets: CustomBuilderPreset[] = [];
              if (localData) {
                  try {
                      const parsed = JSON.parse(localData);
                      if (Array.isArray(parsed)) localPresets = parsed.filter(p => p && p.id && p.name && Array.isArray(p.nodes));
                  } catch (e) {}
              }
              localPresets.push(newPreset);
              await AsyncStorage.setItem('@Sk8lytz_Builder_Presets', JSON.stringify(localPresets));
          }
          
          setSaveModalVisible(false);
          setPresetNameInput('');
          loadLibrary(); // Reload UI
          AppLogger.log('BUILDER_PRESET_SAVED', { id: newPreset.id, name: newPreset.name });
      } catch (err) {
          console.error("Save error", err);
          Alert.alert("Save Error", "Failed to save the preset.");
      } finally {
          setIsSaving(false);
      }
  };

  const loadPreset = (p: CustomBuilderPreset) => {
      onNodesChange(p.nodes);
      onFillModeChange(p.fill_mode);
      onTransitionTypeChange(p.transition_type);
      if (p.nodes.length > 0) setActiveNodeId(p.nodes[0].id);
      AppLogger.log('BUILDER_PRESET_LOADED', { id: p.id, name: p.name });
  };

  const deletePreset = async (p: CustomBuilderPreset) => {
      Alert.alert("Delete Preset", `Are you sure you want to delete '${p.name}'?`, [
          { text: "Cancel", style: "cancel" },
          { text: "Delete", style: "destructive", onPress: async () => {
              try {
                  if (p.id.startsWith('local_')) {
                      // Delete Local
                      const localData = await AsyncStorage.getItem('@Sk8lytz_Builder_Presets');
                      let localPresets: CustomBuilderPreset[] = localData ? JSON.parse(localData) : [];
                      localPresets = localPresets.filter(x => x.id !== p.id);
                      await AsyncStorage.setItem('@Sk8lytz_Builder_Presets', JSON.stringify(localPresets));
                  } else {
                      // Delete Cloud
                      await supabase.from('custom_builder_presets').delete().eq('id', p.id);
                  }
                  AppLogger.log('BUILDER_PRESET_DELETED', { id: p.id, name: p.name });
                  loadLibrary();
              } catch(err) {
                  console.error(err);
              }
          }}
      ]);
  };

  // Auto-sync universal color picker changes to the active pin
  useEffect(() => {
     if (activeNodeId) {
         const current = nodes.find(n => n.id === activeNodeId);
         if (current && current.colorHex !== selectedColor) {
             const updated = nodes.map(n => n.id === activeNodeId ? { ...n, colorHex: selectedColor } : n);
             onNodesChange(updated);
         }
     }
  }, [selectedColor]);

  // Dispatch payloads whenever parameters change
  useEffect(() => {
     if (writeToDevice) {
         const generatedRgbArray = PositionalMathBuffer.generateArray(nodes, deviceLedCount, fillMode === 'GRADIENT');
         // Normalize speed 0-100 to 0x01-0x1F (1-31)
         const mappedSpeed = Math.max(1, Math.min(31, Math.round((speed / 100) * 31)));
         const payload = ZenggeProtocol.setMultiColor(generatedRgbArray, mappedSpeed, direction, transitionType);
         writeToDevice(payload);
     }
  }, [nodes, fillMode, transitionType, direction, speed, deviceLedCount]);

  const addNode = () => {
      let newPosition = 50;
      if (nodes.length > 0) {
          const last = nodes[nodes.length - 1];
          newPosition = Math.min(100, last.position + Math.floor((100 - last.position) / 2));
          if (newPosition === last.position) newPosition = Math.max(0, last.position - 10);
      }
      
      const newNode: BuilderNode = {
          id: `node_${Date.now()}`,
          position: newPosition,
          colorHex: selectedColor // Inherit universal color
      };
      
      const updated = [...nodes, newNode].sort((a,b) => a.position - b.position);
      onNodesChange(updated);
      setActiveNodeId(newNode.id);
  };

  const removeNode = (id: string) => {
      if (nodes.length <= 1) return; // Must have at least 1
      const updated = nodes.filter(n => n.id !== id);
      onNodesChange(updated);
      if (activeNodeId === id) {
          setActiveNodeId(updated[0].id);
      }
  };

  const updateNode = (id: string, updates: Partial<BuilderNode>) => {
      const updated = nodes.map(n => n.id === id ? { ...n, ...updates } : n);
      onNodesChange(updated.sort((a,b) => a.position - b.position));
  };

  const activeNode = nodes.find(n => n.id === activeNodeId);

  // Generates physical RGB preview arrays so the UI bar matches actual device LED count.
  // BUG FIX: was hardcoded 100 — the preview bar now reflects deviceLedCount correctly.
  const maxPins = Math.min(deviceLedCount, 32);
  const previewLeds = PositionalMathBuffer.generateArray(nodes, deviceLedCount, fillMode === 'GRADIENT');

  return (
    <View style={{ flex: 1, backgroundColor: isDark ? 'rgba(0,0,0,0.15)' : 'rgba(0,0,0,0.03)', borderRadius: 12, borderWidth: 1, borderColor: isDark ? 'rgba(255,255,255,0.05)' : 'transparent', padding: Spacing.sm }}>
      
      {/* 0. STABILIZED LIBRARY SHELF (SAVED PRESETS) */}
      <View style={{ height: 68, marginBottom: Spacing.xs, paddingBottom: Spacing.xs, borderBottomWidth: 1, borderColor: isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.05)' }}>
         <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={{ gap: Spacing.sm, paddingHorizontal: Spacing.xs }}>
            {Array.from({ length: 8 }).map((_, idx) => {
               const p = presets[idx];
               if (isLoadingLibrary) {
                  return (
                     <View key={`skeleton_${idx}`} style={{ width: 110, height: 50, borderRadius: 12, backgroundColor: isDark ? 'rgba(255,255,255,0.03)' : 'rgba(0,0,0,0.03)', borderWidth: 1, borderColor: 'rgba(255,255,255,0.05)', justifyContent: 'center', alignItems: 'center' }}>
                        <View style={{ opacity: 0.3 }}>
                           <ActivityIndicator size="small" color={Colors.textMuted} />
                        </View>
                     </View>
                  );
               }
               if (!p) {
                  return (
                     <View key={`empty_${idx}`} style={{ width: 110, height: 50, borderRadius: 12, borderWidth: 1, borderColor: isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.05)', borderStyle: 'dashed', justifyContent: 'center', alignItems: 'center', opacity: 0.4 }}>
                        <MaterialCommunityIcons name="plus" size={16} color={Colors.textMuted} />
                        <Text style={{ color: Colors.textMuted, fontSize: 8, fontWeight: 'bold' }}>SLOT {idx + 1}</Text>
                     </View>
                  );
               }
               
               // Generate a simple color pattern for the background
               const pillPreviewColors = PositionalMathBuffer.generateArray(p.nodes, 10, p.fill_mode === 'GRADIENT');

               return (
                  <TouchableOpacity 
                     key={p.id}
                     onLongPress={() => deletePreset(p)}
                     onPress={() => loadPreset(p)}
                     activeOpacity={0.8}
                     style={{ width: 110, height: 50, borderRadius: 12, overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)', backgroundColor: '#111' }}
                  >
                     {/* Pattern Background */}
                     <View style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0, flexDirection: 'row', opacity: 0.4 }}>
                        {pillPreviewColors.map((c, i) => (
                           <View key={i} style={{ flex: 1, backgroundColor: `rgb(${c.r}, ${c.g}, ${c.b})` }} />
                        ))}
                     </View>
                     
                     {/* Overlay Content */}
                     <View style={{ flex: 1, padding: Spacing.sm, justifyContent: 'space-between' }}>
                        <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' }}>
                           <MaterialCommunityIcons name={p.id.startsWith('local_') ? 'cellphone' : 'cloud-check'} size={12} color="#FFF" style={{ opacity: 0.8 }} />
                           <View style={{ flexDirection: 'row' }}>
                              {p.nodes.slice(0, 3).map((n, i) => (
                                 <View key={n.id} style={{ width: 6, height: 6, borderRadius: 3, backgroundColor: n.colorHex, marginLeft: i === 0 ? 0 : -2, borderWidth: 0.5, borderColor: '#000' }} />
                              ))}
                           </View>
                        </View>
                        
                        <MarqueeText style={{ color: '#FFF', fontSize: 10, fontWeight: '900', textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 2 }}>
                           {p.name.toUpperCase()}
                        </MarqueeText>
                     </View>
                  </TouchableOpacity>
               );
            })}
         </ScrollView>
      </View>

      {/* 1. TOP HEADER - LAYOUT & SAVE */}
      <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: Spacing.xxs }}>
         <Text style={{ color: Colors.textMuted, fontSize: 9, fontWeight: 'bold' }}>LAYOUT (MAX {maxPins})</Text>
         <TouchableOpacity 
            onPress={() => setSaveModalVisible(true)}
            style={{ paddingHorizontal: Spacing.sm, paddingVertical: Spacing.xxs, backgroundColor: 'rgba(255,255,255,0.1)', borderRadius: 10, flexDirection: 'row', alignItems: 'center' }}
         >
            <MaterialCommunityIcons name="content-save" size={10} color={Colors.textMuted} style={{ marginRight: Spacing.xxs }} />
            <Text style={{ color: Colors.textMuted, fontSize: 9, fontWeight: 'bold' }}>SAVE</Text>
         </TouchableOpacity>
      </View>

      {/* 2. VISUAL MAP PREVIEW */}
      <View style={{ width: '100%', height: 10, borderRadius: 5, flexDirection: 'row', overflow: 'hidden', marginBottom: Spacing.xs }}>
         {previewLeds.map((c, i) => (
             <View key={i} style={{ flex: 1, backgroundColor: `rgb(${c.r}, ${c.g}, ${c.b})` }} />
         ))}
      </View>

      {/* 3. PIN SELECTOR ROW */}
      <View style={{ marginBottom: Spacing.xs }}>
         <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={{ gap: Spacing.sm, alignItems: 'center' }}>
            {nodes.map(n => (
                <TouchableOpacity 
                   key={n.id}
                   onPress={() => setActiveNodeId(n.id)}
                   style={{
                       width: 24, height: 24, borderRadius: 12, backgroundColor: n.colorHex,
                       borderWidth: activeNodeId === n.id ? 2 : 1,
                       borderColor: activeNodeId === n.id ? Colors.primary : 'rgba(255,255,255,0.3)',
                       justifyContent: 'center', alignItems: 'center',
                       shadowColor: n.colorHex, shadowOpacity: activeNodeId === n.id ? 0.6 : 0, shadowRadius: 4
                   }}
                >
                   {activeNodeId === n.id && <MaterialCommunityIcons name="map-marker-down" size={12} color={['#FFFFFF', '#FFF', '#FFFF00', '#00FF00'].includes(n.colorHex.toUpperCase()) ? '#000' : '#FFF'} />}
                </TouchableOpacity>
            ))}
            {nodes.length < maxPins && (
                <TouchableOpacity 
                   onPress={addNode}
                   style={{
                       width: 24, height: 24, borderRadius: 12, 
                       borderWidth: 1, borderColor: Colors.textMuted, borderStyle: 'dashed',
                       justifyContent: 'center', alignItems: 'center',
                       backgroundColor: 'rgba(255,255,255,0.05)'
                   }}
                >
                   <MaterialCommunityIcons name="plus" size={14} color={Colors.textMuted} />
                </TouchableOpacity>
            )}
         </ScrollView>
      </View>

      {/* 4. ACTIVE PIN EDITOR */}
      {activeNode && (
         <View style={{ backgroundColor: 'rgba(0,0,0,0.2)', borderRadius: 6, paddingHorizontal: Spacing.sm, paddingVertical: Spacing.xxs, marginBottom: Spacing.xs, flexDirection: 'row', alignItems: 'center' }}>
            <Text style={{ color: '#FFF', fontWeight: 'bold', fontSize: 10, width: 36 }}>@{activeNode.position}%</Text>
            <CustomSlider 
                value={activeNode.position}
                onValueChange={(val) => updateNode(activeNode.id, { position: Math.round(val) })}
                minimumValue={0}
                maximumValue={100}
                style={{ flex: 1, transform: [{ scale: 0.95 }], height: 30 }}
            />
            <TouchableOpacity onPress={() => removeNode(activeNode.id)} disabled={nodes.length <= 1} style={{ marginLeft: Spacing.sm }}>
                <MaterialCommunityIcons name="trash-can-outline" size={16} color={nodes.length <= 1 ? 'rgba(255,255,255,0.2)' : '#FF4444'} />
            </TouchableOpacity>
         </View>
      )}

      {/* 5. BEHAVIOR TIER */}
      <View style={{ flexDirection: 'row', gap: Spacing.sm, marginBottom: Spacing.xs }}>
          <View style={{ flex: 1 }}>
              <Text style={{ color: Colors.textMuted, fontSize: 9, fontWeight: 'bold', marginBottom: Spacing.xxs }}>FILLING</Text>
              <View style={{ flexDirection: 'row', backgroundColor: 'rgba(0,0,0,0.2)', borderRadius: 6, padding: Spacing.xxs }}>
                  <TouchableOpacity 
                      onPress={() => onFillModeChange('GRADIENT')}
                      style={{ flex: 1, paddingVertical: Spacing.xxs, alignItems: 'center', backgroundColor: fillMode === 'GRADIENT' ? Colors.surfaceHighlight : 'transparent', borderRadius: 4 }}
                  >
                      <Text style={{ color: fillMode === 'GRADIENT' ? Colors.primary : Colors.textMuted, fontWeight: 'bold', fontSize: 9 }}>GRADIENT</Text>
                  </TouchableOpacity>
                  <TouchableOpacity 
                      onPress={() => onFillModeChange('SOLID')}
                      style={{ flex: 1, paddingVertical: Spacing.xxs, alignItems: 'center', backgroundColor: fillMode === 'SOLID' ? Colors.surfaceHighlight : 'transparent', borderRadius: 4 }}
                  >
                      <Text style={{ color: fillMode === 'SOLID' ? Colors.primary : Colors.textMuted, fontWeight: 'bold', fontSize: 9 }}>SOLID</Text>
                  </TouchableOpacity>
              </View>
          </View>

          <View style={{ flex: 1 }}>
              <Text style={{ color: Colors.textMuted, fontSize: 9, fontWeight: 'bold', marginBottom: Spacing.xxs }}>DIRECTION</Text>
              <View style={{ flexDirection: 'row', backgroundColor: 'rgba(0,0,0,0.2)', borderRadius: 6, padding: Spacing.xxs }}>
                  <TouchableOpacity 
                      onPress={() => onDirectionChange(1)}
                      style={{ flex: 1, paddingVertical: Spacing.xxs, alignItems: 'center', backgroundColor: direction === 1 ? Colors.surfaceHighlight : 'transparent', borderRadius: 4 }}
                  >
                      <Text style={{ color: direction === 1 ? Colors.primary : Colors.textMuted, fontWeight: 'bold', fontSize: 9 }}>FORWARD</Text>
                  </TouchableOpacity>
                  <TouchableOpacity 
                      onPress={() => onDirectionChange(0)}
                      style={{ flex: 1, paddingVertical: Spacing.xxs, alignItems: 'center', backgroundColor: direction === 0 ? Colors.surfaceHighlight : 'transparent', borderRadius: 4 }}
                  >
                      <Text style={{ color: direction === 0 ? Colors.primary : Colors.textMuted, fontWeight: 'bold', fontSize: 9 }}>REVERSE</Text>
                  </TouchableOpacity>
              </View>
          </View>
      </View>

      <Text style={{ color: Colors.textMuted, fontSize: 9, fontWeight: 'bold', marginBottom: Spacing.xxs }}>ANIMATION</Text>
      <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.xs }}>
          {[
              // BUG FIX: IDs now match ZenggeProtocol transitionType bytes directly.
              // Old mapping (1-5) was off-by-one — STATIC sent Gradual, STROBE sent Water, etc.
              // Ground truth @ ZenggeProtocol.ts:577: 0x00=Static, 0x01=Gradual, 0x02=Strobe, 0x03=RunningWater
              { id: 0x00, label: 'STATIC', icon: 'led-strip' as const },
              { id: 0x01, label: 'FADE',   icon: 'gradient-horizontal' as const },
              { id: 0x02, label: 'STROBE', icon: 'lightning-bolt' as const },
              { id: 0x03, label: 'SCROLL', icon: 'arrow-right-bold' as const },
              { id: 0x04, label: 'JUMP',   icon: 'swap-vertical' as const },
          ].map(t => (
              <TouchableOpacity 
                  key={t.id}
                  onPress={() => onTransitionTypeChange(t.id)}
                  style={{ paddingHorizontal: Spacing.sm, paddingVertical: Spacing.xxs, borderRadius: 6, borderWidth: 1, borderColor: transitionType === t.id ? Colors.primary : 'rgba(255,255,255,0.1)', backgroundColor: transitionType === t.id ? 'rgba(0,240,255,0.1)' : 'transparent', flexDirection: 'row', alignItems: 'center', gap: 3 }}
              >
                  <MaterialCommunityIcons name={t.icon} size={9} color={transitionType === t.id ? Colors.primary : Colors.textMuted} />
                  <Text style={{ color: transitionType === t.id ? Colors.primary : Colors.textMuted, fontSize: 9, fontWeight: 'bold' }}>{t.label}</Text>
              </TouchableOpacity>
          ))}
      </View>

      {/* 6. SAVE MODAL */}
      <Modal visible={saveModalVisible} transparent animationType="fade">
          <View style={{ flex: 1, backgroundColor: 'rgba(0,0,0,0.8)', justifyContent: 'center', alignItems: 'center', padding: Spacing.xl }}>
             <View style={{ width: '100%', maxWidth: 340, backgroundColor: isDark ? '#111' : '#FFF', borderRadius: 16, padding: Spacing.xl, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}>
                 <Text style={{ color: isDark ? '#FFF' : '#000', fontSize: 16, fontWeight: 'bold', marginBottom: Spacing.md }}>Save Custom Preset</Text>
                 <Text style={{ color: Colors.textMuted, fontSize: 12, marginBottom: Spacing.lg }}>Give your sequence a name. It will be saved securely to the cloud if you are logged in, otherwise it will save locally to this device.</Text>
                 
                 <TextInput 
                    value={presetNameInput}
                    onChangeText={setPresetNameInput}
                    placeholder="e.g. Neon Fire Trail"
                    placeholderTextColor="rgba(255,255,255,0.3)"
                    style={{ backgroundColor: 'rgba(0,0,0,0.2)', color: '#FFF', padding: Spacing.md, borderRadius: 8, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)', marginBottom: Spacing.xl }}
                 />
                 
                 <View style={{ flexDirection: 'row', gap: Spacing.md, justifyContent: 'flex-end' }}>
                     <TouchableOpacity onPress={() => !isSaving && setSaveModalVisible(false)} style={{ padding: Spacing.md }}>
                         <Text style={{ color: Colors.textMuted, fontWeight: 'bold' }}>CANCEL</Text>
                     </TouchableOpacity>
                     <TouchableOpacity 
                        onPress={handleSavePreset}
                        disabled={isSaving || !presetNameInput.trim()}
                        style={{ paddingHorizontal: Spacing.xl, paddingVertical: Spacing.md, backgroundColor: Colors.primary, borderRadius: 8, opacity: (!presetNameInput.trim() || isSaving) ? 0.5 : 1, flexDirection: 'row', alignItems: 'center' }}
                     >
                         {isSaving && <ActivityIndicator size="small" color="#000" style={{ marginRight: Spacing.sm }} />}
                         <Text style={{ color: '#000', fontWeight: 'bold' }}>SAVE</Text>
                     </TouchableOpacity>
                 </View>
             </View>
          </View>
      </Modal>

    </View>
  );
}
