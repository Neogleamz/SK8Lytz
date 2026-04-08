import React, { useState, useEffect } from 'react';
import { View, Text, TouchableOpacity, ScrollView, StyleSheet } from 'react-native';
import { useTheme } from '../context/ThemeContext';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { BuilderNode, PositionalMathBuffer } from '../protocols/PositionalMathBuffer';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import CustomSlider from './CustomSlider';

interface Props {
  nodes: BuilderNode[];
  onNodesChange: (nodes: BuilderNode[]) => void;
  fillMode: 'GRADIENT' | 'SOLID';
  onFillModeChange: (mode: 'GRADIENT' | 'SOLID') => void;
  transitionType: number;
  onTransitionTypeChange: (type: number) => void;
  speed: number;
  deviceLedCount: number;
  selectedColor: string; // The universal color passed from DockedController
  writeToDevice?: (payload: number[]) => Promise<void>;
}

export default function PositionalGradientBuilder({ 
   nodes, onNodesChange, 
   fillMode, onFillModeChange, 
   transitionType, onTransitionTypeChange,
   speed, deviceLedCount, selectedColor, writeToDevice
}: Props) {
  const { Colors, isDark } = useTheme();

  const [activeNodeId, setActiveNodeId] = useState<string | null>(nodes[0]?.id || null);

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
         const payload = ZenggeProtocol.setMultiColor(generatedRgbArray, mappedSpeed, 1, transitionType);
         writeToDevice(payload);
     }
  }, [nodes, fillMode, transitionType, speed, deviceLedCount]);

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

  // Generates physical RGB preview arrays so the UI bar matches string output visually
  const previewLeds = PositionalMathBuffer.generateArray(nodes, 100, fillMode === 'GRADIENT');

  return (
    <View style={{ flex: 1, backgroundColor: isDark ? 'rgba(0,0,0,0.15)' : 'rgba(0,0,0,0.03)', borderRadius: 12, borderWidth: 1, borderColor: isDark ? 'rgba(255,255,255,0.05)' : 'transparent', padding: 8 }}>
      
      {/* 1. TOP HEADER - ADD NODE */}
      <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 6 }}>
         <Text style={{ color: Colors.textMuted, fontSize: 11, fontWeight: 'bold' }}>LAYOUT (MAX 16)</Text>
         <TouchableOpacity 
            onPress={addNode}
            disabled={nodes.length >= 16}
            style={{ paddingHorizontal: 8, paddingVertical: 2, backgroundColor: nodes.length >= 16 ? 'rgba(255,255,255,0.1)' : Colors.primary, borderRadius: 12 }}
         >
            <Text style={{ color: nodes.length >= 16 ? 'rgba(255,255,255,0.3)' : '#000', fontSize: 10, fontWeight: 'bold' }}>+ ADD PIN</Text>
         </TouchableOpacity>
      </View>

      {/* 2. VISUAL MAP PREVIEW */}
      <View style={{ width: '100%', height: 10, borderRadius: 5, flexDirection: 'row', overflow: 'hidden', marginBottom: 8 }}>
         {previewLeds.map((c, i) => (
             <View key={i} style={{ flex: 1, backgroundColor: `rgb(${c.r}, ${c.g}, ${c.b})` }} />
         ))}
      </View>

      {/* 3. PIN SELECTOR ROW */}
      <View style={{ marginBottom: 8 }}>
         <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={{ gap: 6 }}>
            {nodes.map(n => (
                <TouchableOpacity 
                   key={n.id}
                   onPress={() => setActiveNodeId(n.id)}
                   style={{
                       width: 30, height: 30, borderRadius: 15, backgroundColor: n.colorHex,
                       borderWidth: activeNodeId === n.id ? 2 : 1,
                       borderColor: activeNodeId === n.id ? Colors.primary : 'rgba(255,255,255,0.3)',
                       justifyContent: 'center', alignItems: 'center',
                       shadowColor: n.colorHex, shadowOpacity: activeNodeId === n.id ? 0.6 : 0, shadowRadius: 4
                   }}
                >
                   {activeNodeId === n.id && <MaterialCommunityIcons name="map-marker-down" size={14} color={['#FFFFFF', '#FFF'].includes(n.colorHex.toUpperCase()) ? '#000' : '#FFF'} />}
                </TouchableOpacity>
            ))}
         </ScrollView>
      </View>

      {/* 4. ACTIVE PIN EDITOR */}
      {activeNode && (
         <View style={{ backgroundColor: 'rgba(0,0,0,0.2)', borderRadius: 8, padding: 8, marginBottom: 8 }}>
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 2 }}>
               <Text style={{ color: '#FFF', fontWeight: 'bold', fontSize: 11 }}>Edit Pin @ {activeNode.position}%</Text>
               <TouchableOpacity onPress={() => removeNode(activeNode.id)} disabled={nodes.length <= 1}>
                   <MaterialCommunityIcons name="trash-can-outline" size={16} color={nodes.length <= 1 ? 'rgba(255,255,255,0.2)' : '#FF4444'} />
               </TouchableOpacity>
            </View>

            <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: 0 }}>
                <MaterialCommunityIcons name="arrow-left-right" size={16} color={Colors.textMuted} style={{ marginRight: 6 }} />
                <CustomSlider 
                    value={activeNode.position}
                    onValueChange={(val) => updateNode(activeNode.id, { position: Math.round(val) })}
                    minimumValue={0}
                    maximumValue={100}
                    style={{ flex: 1, transform: [{ scale: 0.95 }] }}
                />
            </View>
         </View>
      )}

      {/* 5. BEHAVIOR TIER */}
      <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 4 }}>
          <Text style={{ color: Colors.textMuted, fontSize: 10, fontWeight: 'bold' }}>BEHAVIOR</Text>
          <View style={{ flexDirection: 'row', backgroundColor: 'rgba(0,0,0,0.2)', borderRadius: 6, padding: 2, flex: 0.7 }}>
              <TouchableOpacity 
                  onPress={() => onFillModeChange('GRADIENT')}
                  style={{ flex: 1, paddingVertical: 4, alignItems: 'center', backgroundColor: fillMode === 'GRADIENT' ? Colors.surfaceHighlight : 'transparent', borderRadius: 4 }}
              >
                  <Text style={{ color: fillMode === 'GRADIENT' ? Colors.primary : Colors.textMuted, fontWeight: 'bold', fontSize: 9 }}>GRADIENT</Text>
              </TouchableOpacity>
              <TouchableOpacity 
                  onPress={() => onFillModeChange('SOLID')}
                  style={{ flex: 1, paddingVertical: 4, alignItems: 'center', backgroundColor: fillMode === 'SOLID' ? Colors.surfaceHighlight : 'transparent', borderRadius: 4 }}
              >
                  <Text style={{ color: fillMode === 'SOLID' ? Colors.primary : Colors.textMuted, fontWeight: 'bold', fontSize: 9 }}>FILL-IN</Text>
              </TouchableOpacity>
          </View>
      </View>

      <Text style={{ color: Colors.textMuted, fontSize: 10, fontWeight: 'bold', marginBottom: 4 }}>ANIMATION</Text>
      <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 4 }}>
          {[
              { id: 1, label: 'STATIC' },
              { id: 2, label: 'GRADUAL' },
              { id: 3, label: 'STROBE' },
              { id: 4, label: 'WATER' },
              { id: 5, label: 'JUMP' }
          ].map(t => (
              <TouchableOpacity 
                  key={t.id}
                  onPress={() => onTransitionTypeChange(t.id)}
                  style={{ paddingHorizontal: 8, paddingVertical: 4, borderRadius: 6, borderWidth: 1, borderColor: transitionType === t.id ? Colors.primary : 'rgba(255,255,255,0.1)', backgroundColor: transitionType === t.id ? 'rgba(0,240,255,0.1)' : 'transparent' }}
              >
                  <Text style={{ color: transitionType === t.id ? Colors.primary : Colors.textMuted, fontSize: 10, fontWeight: 'bold' }}>{t.label}</Text>
              </TouchableOpacity>
          ))}
      </View>

    </View>
  );
}
