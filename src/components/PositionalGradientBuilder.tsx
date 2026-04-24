import { Spacing } from '../theme/theme';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import React, { useEffect, useState } from 'react';
import { TouchableOpacity, View, Text, ScrollView } from 'react-native';
import { useTheme } from '../context/ThemeContext';
import { BuilderNode, PositionalMathBuffer } from '../protocols/PositionalMathBuffer';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import CustomSlider from './CustomSlider';
import { COLOR_PRESET_PALETTE, hexToHue, hueToHex } from '../utils/ColorUtils';

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

  // Internal color state is now directly managed via activeNode instead of external sync

  // Dispatch payloads whenever parameters change
  useEffect(() => {
     if (writeToDevice) {
         const generatedRgbArray = PositionalMathBuffer.generateArray(nodes, deviceLedCount, fillMode === 'GRADIENT');
         // Normalize speed 0-100 to 0x01-0x1F (1-31)
         const mappedSpeed = Math.max(1, Math.min(31, Math.round((speed / 100) * 31)));
         const payload = ZenggeProtocol.setMultiColor(generatedRgbArray, deviceLedCount, mappedSpeed, direction, transitionType);
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
      
      <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: Spacing.xxs }}>
         <Text style={{ color: Colors.textMuted, fontSize: 9, fontWeight: 'bold' }}>LAYOUT (MAX {maxPins})</Text>
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
         <View style={{ backgroundColor: 'rgba(0,0,0,0.2)', borderRadius: 6, padding: Spacing.sm, marginBottom: Spacing.xs }}>
            <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: Spacing.sm }}>
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

            {/* COLOR CONTROLS FOR ACTIVE PIN */}
            <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: Spacing.xs }}>
               {COLOR_PRESET_PALETTE.map((color) => (
                  <TouchableOpacity
                     key={color}
                     onPress={() => updateNode(activeNode.id, { colorHex: color })}
                     style={{
                        width: 22, height: 22, borderRadius: 11, backgroundColor: color,
                        borderWidth: 2, borderColor: activeNode.colorHex.toUpperCase() === color.toUpperCase() ? '#00F0FF' : 'rgba(255,255,255,0.2)'
                     }}
                  />
               ))}
            </View>
            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
               <MaterialCommunityIcons name="palette" size={16} color={activeNode.colorHex} style={{ marginRight: Spacing.xs }} />
               <CustomSlider
                  value={hexToHue(activeNode.colorHex)}
                  onValueChange={(val) => updateNode(activeNode.id, { colorHex: hueToHex(val) })}
                  minimumValue={0}
                  maximumValue={360}
                  style={{ flex: 1, transform: [{ scale: 0.95 }], height: 30 }}
               />
            </View>
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
              // APK-PROVEN commandType bytes — StaticColorfulMode.java (ZENGGE_DECOMPILED 2026-04-23):
              // 0x01=Static (freeze)  0x02=Running (scroll)  0x03=Strobe  0x04=Jump
              { id: 0x01, label: 'STATIC',  icon: 'pause-circle-outline' as const },
              { id: 0x02, label: 'FLOW',    icon: 'arrow-right-bold' as const },
              { id: 0x03, label: 'STROBE',  icon: 'flash' as const },
              { id: 0x04, label: 'JUMP',    icon: 'skip-forward' as const },
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

    </View>
  );
}
