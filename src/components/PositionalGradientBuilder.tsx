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
  writeToDevice?: (payload: number[]) => Promise<void>;
}

export default function PositionalGradientBuilder({ 
   nodes, onNodesChange, 
   fillMode, onFillModeChange, 
   transitionType, onTransitionTypeChange,
   speed, deviceLedCount, writeToDevice
}: Props) {
  const { Colors, isDark } = useTheme();

  const [activeNodeId, setActiveNodeId] = useState<string | null>(nodes[0]?.id || null);

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
      // Find a safe spot
      let newPosition = 50;
      if (nodes.length > 0) {
          const last = nodes[nodes.length - 1];
          newPosition = Math.min(100, last.position + Math.floor((100 - last.position) / 2));
          if (newPosition === last.position) newPosition = Math.max(0, last.position - 10);
      }
      
      const newNode: BuilderNode = {
          id: `node_${Date.now()}`,
          position: newPosition,
          colorHex: '#FFFFFF'
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
    <ScrollView 
       style={{ flex: 1, backgroundColor: isDark ? 'rgba(0,0,0,0.15)' : 'rgba(0,0,0,0.03)', borderRadius: 12, borderWidth: 1, borderColor: isDark ? 'rgba(255,255,255,0.05)' : 'transparent' }}
       contentContainerStyle={{ padding: 12, paddingBottom: 24 }}
       showsVerticalScrollIndicator={false}
    >
      
      {/* 1. TOP HEADER - ADD NODE */}
      <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 }}>
         <Text style={{ color: Colors.textMuted, fontSize: 13, fontWeight: 'bold' }}>LAYOUT (MAX 16)</Text>
         <TouchableOpacity 
            onPress={addNode}
            disabled={nodes.length >= 16}
            style={{ paddingHorizontal: 12, paddingVertical: 4, backgroundColor: nodes.length >= 16 ? 'rgba(255,255,255,0.1)' : Colors.primary, borderRadius: 16 }}
         >
            <Text style={{ color: nodes.length >= 16 ? 'rgba(255,255,255,0.3)' : '#000', fontSize: 12, fontWeight: 'bold' }}>+ ADD PIN</Text>
         </TouchableOpacity>
      </View>

      {/* 2. VISUAL MAP PREVIEW */}
      <View style={{ width: '100%', height: 16, borderRadius: 8, flexDirection: 'row', overflow: 'hidden', marginBottom: 12 }}>
         {previewLeds.map((c, i) => (
             <View key={i} style={{ flex: 1, backgroundColor: `rgb(${c.r}, ${c.g}, ${c.b})` }} />
         ))}
      </View>

      {/* 3. PIN SELECTOR ROW */}
      <View style={{ marginBottom: 16 }}>
         <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={{ gap: 8 }}>
            {nodes.map(n => (
                <TouchableOpacity 
                   key={n.id}
                   onPress={() => setActiveNodeId(n.id)}
                   style={{
                       width: 44, height: 44, borderRadius: 22, backgroundColor: n.colorHex,
                       borderWidth: activeNodeId === n.id ? 3 : 1,
                       borderColor: activeNodeId === n.id ? Colors.primary : 'rgba(255,255,255,0.3)',
                       justifyContent: 'center', alignItems: 'center',
                       shadowColor: n.colorHex, shadowOpacity: activeNodeId === n.id ? 0.6 : 0, shadowRadius: 8
                   }}
                >
                   {activeNodeId === n.id && <MaterialCommunityIcons name="map-marker-down" size={20} color={['#FFFFFF', '#FFF'].includes(n.colorHex.toUpperCase()) ? '#000' : '#FFF'} />}
                </TouchableOpacity>
            ))}
         </ScrollView>
      </View>

      {/* 4. ACTIVE PIN EDITOR */}
      {activeNode && (
         <View style={{ backgroundColor: 'rgba(0,0,0,0.2)', borderRadius: 8, padding: 12, marginBottom: 16 }}>
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 }}>
               <Text style={{ color: '#FFF', fontWeight: 'bold' }}>Edit Pin @ {activeNode.position}%</Text>
               <TouchableOpacity onPress={() => removeNode(activeNode.id)} disabled={nodes.length <= 1}>
                   <MaterialCommunityIcons name="trash-can-outline" size={20} color={nodes.length <= 1 ? 'rgba(255,255,255,0.2)' : '#FF4444'} />
               </TouchableOpacity>
            </View>

            <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: 4 }}>
                <MaterialCommunityIcons name="arrow-left-right" size={18} color={Colors.textMuted} style={{ marginRight: 8 }} />
                <CustomSlider 
                    value={activeNode.position}
                    onValueChange={(val) => updateNode(activeNode.id, { position: Math.round(val) })}
                    minimumValue={0}
                    maximumValue={100}
                    style={{ flex: 1 }}
                />
            </View>
            
            {/* Embedded Mini Color Swatch Grid for active pin */}
            <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 6, marginTop: 12 }}>
                {['#FF0000', '#FF8C00', '#FFFF00', '#00FF00', '#00FFFF', '#0000FF', '#FF00FF', '#FFFFFF', '#000000'].map(c => (
                   <TouchableOpacity 
                      key={c} 
                      onPress={() => updateNode(activeNode.id, { colorHex: c })}
                      style={{ width: 28, height: 28, borderRadius: 14, backgroundColor: c, borderWidth: 1, borderColor: activeNode.colorHex === c ? '#FFF' : 'transparent' }} 
                   />
                ))}
            </View>
         </View>
      )}

      {/* 5. BEHAVIOR TIER */}
      <Text style={{ color: Colors.textMuted, fontSize: 13, fontWeight: 'bold', marginBottom: 8 }}>BEHAVIOR</Text>
      <View style={{ flexDirection: 'row', backgroundColor: 'rgba(0,0,0,0.2)', borderRadius: 8, padding: 4, marginBottom: 12 }}>
          <TouchableOpacity 
              onPress={() => onFillModeChange('GRADIENT')}
              style={{ flex: 1, paddingVertical: 8, alignItems: 'center', backgroundColor: fillMode === 'GRADIENT' ? Colors.surfaceHighlight : 'transparent', borderRadius: 4 }}
          >
              <Text style={{ color: fillMode === 'GRADIENT' ? Colors.primary : Colors.textMuted, fontWeight: 'bold', fontSize: 12 }}>GRADIENT</Text>
          </TouchableOpacity>
          <TouchableOpacity 
              onPress={() => onFillModeChange('SOLID')}
              style={{ flex: 1, paddingVertical: 8, alignItems: 'center', backgroundColor: fillMode === 'SOLID' ? Colors.surfaceHighlight : 'transparent', borderRadius: 4 }}
          >
              <Text style={{ color: fillMode === 'SOLID' ? Colors.primary : Colors.textMuted, fontWeight: 'bold', fontSize: 12 }}>FILL-IN</Text>
          </TouchableOpacity>
      </View>

      <Text style={{ color: Colors.textMuted, fontSize: 13, fontWeight: 'bold', marginBottom: 8 }}>ANIMATION</Text>
      <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 8 }}>
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
                  style={{ paddingHorizontal: 12, paddingVertical: 8, borderRadius: 8, borderWidth: 1, borderColor: transitionType === t.id ? Colors.primary : 'rgba(255,255,255,0.1)', backgroundColor: transitionType === t.id ? 'rgba(0,240,255,0.1)' : 'transparent' }}
              >
                  <Text style={{ color: transitionType === t.id ? Colors.primary : Colors.textMuted, fontSize: 12, fontWeight: 'bold' }}>{t.label}</Text>
              </TouchableOpacity>
          ))}
      </View>

    </ScrollView>
  );
}
