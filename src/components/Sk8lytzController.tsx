import React, { useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView } from 'react-native';
import { Colors, Typography, Layout } from '../theme/theme';
import ProductVisualizer from './ProductVisualizer';

type ProductType = 'HALOZ' | 'SOULZ';
type ModeType = 'PRESETS' | 'COLORS' | 'RBM' | 'MUSIC' | 'CAMERA' | 'CUSTOM';

export default function Sk8lytzController() {
  const [activeProduct, setActiveProduct] = useState<ProductType>('HALOZ');
  const [activeMode, setActiveMode] = useState<ModeType>('PRESETS');
  const [selectedColor, setSelectedColor] = useState<string>('#00F0FF');
  const [selectedPatternId, setSelectedPatternId] = useState<number | null>(null);

  const modes: { id: ModeType; label: string }[] = [
    { id: 'PRESETS', label: 'Neogleamz Presets' },
    { id: 'COLORS', label: 'Solid Colors' },
    { id: 'RBM', label: '100+ RBM Modes' },
    { id: 'MUSIC', label: 'Music Sync' },
    { id: 'CAMERA', label: 'Camera Color' },
    { id: 'CUSTOM', label: 'Custom' }
  ];

  return (
    <View style={styles.container}>
      <Text style={[Typography.title, { marginBottom: 12 }]}>SK8Lytz Controls</Text>
      
      {/* Product Selector */}
      <View style={styles.tabContainer}>
        <TouchableOpacity 
          style={[styles.tab, activeProduct === 'HALOZ' && styles.activeTab]} 
          onPress={() => setActiveProduct('HALOZ')}
        >
          <Text style={[styles.tabText, activeProduct === 'HALOZ' && styles.activeTabText]}>
            HALOZ
          </Text>
        </TouchableOpacity>
        <TouchableOpacity 
          style={[styles.tab, activeProduct === 'SOULZ' && styles.activeTab]} 
          onPress={() => setActiveProduct('SOULZ')}
        >
          <Text style={[styles.tabText, activeProduct === 'SOULZ' && styles.activeTabText]}>
            SOULZ
          </Text>
        </TouchableOpacity>
      </View>

      {/* Visual Product Shape Selector/Indicator */}
      <ProductVisualizer 
        product={activeProduct} 
        color={selectedColor} 
        mode={activeMode} 
        patternId={selectedPatternId} 
      />

      <View style={styles.controlsContainer}>
        {/* Mode Selector */}
        <ScrollView horizontal showsHorizontalScrollIndicator={false} style={styles.modesScroll}>
          {modes.map(mode => (
            <TouchableOpacity 
              key={mode.id}
              style={[styles.modePill, activeMode === mode.id && styles.activeModePill]}
              onPress={() => setActiveMode(mode.id)}
            >
              <Text style={[styles.modePillText, activeMode === mode.id && styles.activeModePillText]}>
                {mode.label}
              </Text>
            </TouchableOpacity>
          ))}
        </ScrollView>

        <View style={styles.activeModeContainer}>
          {activeMode === 'PRESETS' && (
            <View>
              <Text style={Typography.body}>Signature Lighting Presets</Text>
              <Text style={[Typography.caption, { marginTop: 8 }]}>One-tap replicas of our official product showcase effects.</Text>
              
              <View style={styles.presetContainer}>
                {activeProduct === 'HALOZ' ? (
                  <TouchableOpacity 
                    style={[styles.presetCard, { borderColor: '#FF00FF' }]}
                    onPress={() => setSelectedColor('#FF0000')}
                  >
                    <Text style={styles.presetTitle}>HALOZ Rainbow Flow</Text>
                    <Text style={styles.presetDesc}>Continuous 360° full-spectrum color chase simulating individual WS2812B LEDs moving at moderate speed.</Text>
                  </TouchableOpacity>
                ) : (
                  <TouchableOpacity 
                    style={[styles.presetCard, { borderColor: '#00FFFF' }]}
                    onPress={() => setSelectedColor('#00FFFF')}
                  >
                    <Text style={styles.presetTitle}>SOULZ Cyan Pulse</Text>
                    <Text style={styles.presetDesc}>Slow, breathing ice-cyan sequence cascading across your addressable underglow LEDs.</Text>
                  </TouchableOpacity>
                )}
              </View>
            </View>
          )}

          {activeMode === 'COLORS' && (
            <View>
              <Text style={Typography.body}>Select Solid Color</Text>
              <View style={styles.colorGrid}>
                {['#FF0000', '#00FF00', '#0000FF', '#FF00FF', '#FFFF00', '#00FFFF', '#FFFFFF', '#FF8C00'].map(color => (
                  <TouchableOpacity 
                    key={color} 
                    onPress={() => setSelectedColor(color)}
                    style={[
                      styles.colorButton, 
                      { backgroundColor: color },
                      selectedColor === color && styles.selectedColorButton
                    ]} 
                  />
                ))}
              </View>
            </View>
          )}

          {activeMode === 'RBM' && (
            <View>
              <Text style={Typography.body}>100+ RBM Pattern Modes</Text>
              <Text style={[Typography.caption, { marginTop: 8 }]}>Select a dynamic Rainbow or RGB pattern.</Text>
              
              <ScrollView horizontal showsHorizontalScrollIndicator={false} style={{ marginTop: 12 }}>
                {[1, 2, 3, 4, 10, 25, 42, 99, 100].map(patternId => (
                  <TouchableOpacity 
                    key={patternId}
                    style={[
                      styles.presetCard, 
                      { marginRight: 8, paddingHorizontal: 20, paddingVertical: 12, borderLeftWidth: 2 },
                      selectedPatternId === patternId && { borderColor: Colors.secondary, backgroundColor: Colors.surfaceHighlight }
                    ]}
                    onPress={() => setSelectedPatternId(patternId)}
                  >
                    <Text style={[
                      Typography.caption, 
                      { color: selectedPatternId === patternId ? Colors.secondary : Colors.text, fontWeight: 'bold' }
                    ]}>
                      Pattern {patternId}
                    </Text>
                  </TouchableOpacity>
                ))}
              </ScrollView>

              <View style={styles.controlRow}>
                <Text style={Typography.caption}>Pattern Speed</Text>
                <View style={[styles.placeholderSlider, { backgroundColor: 'rgba(255,255,255,0.1)' }]}>
                  <View style={[styles.sliderFill, { width: '65%', backgroundColor: Colors.primary }]} />
                </View>
              </View>
            </View>
          )}

          {activeMode === 'MUSIC' && (
            <View>
              <Text style={Typography.body}>Music Sync Mode</Text>
              <Text style={[Typography.caption, { marginTop: 8 }]}>React to device audio or microphone to synchronize LED pulses.</Text>
              <View style={[styles.controlRow, { marginTop: 16 }]}>
                <Text style={Typography.caption}>Microphone Sensitivity</Text>
                <View style={[styles.placeholderSlider, { backgroundColor: 'rgba(255,255,255,0.1)' }]}>
                  <View style={[styles.sliderFill, { width: '80%', backgroundColor: Colors.primary }]} />
                </View>
              </View>
              <View style={{ flexDirection: 'row', gap: 12, marginTop: 16 }}>
                <TouchableOpacity style={[styles.tab, styles.activeTab]}>
                  <Text style={styles.activeTabText}>Device Mic</Text>
                </TouchableOpacity>
                <TouchableOpacity style={[styles.tab, { backgroundColor: Colors.surfaceHighlight }]}>
                  <Text style={styles.tabText}>Internal Audio</Text>
                </TouchableOpacity>
              </View>
            </View>
          )}

          {activeMode === 'CAMERA' && (
            <View>
              <Text style={Typography.body}>Camera Color Picker</Text>
              <Text style={[Typography.caption, { marginTop: 8 }]}>Point your camera at any object to extract real-time ambient color.</Text>
              <TouchableOpacity style={[styles.presetCard, { marginTop: 16, alignItems: 'center', justifyContent: 'center', paddingVertical: 32 }]} >
                <Text style={[Typography.body, { color: Colors.primary, fontWeight: 'bold' }]}>+ Open Camera</Text>
              </TouchableOpacity>
            </View>
          )}

          {activeMode === 'CUSTOM' && (
            <View>
              <Text style={Typography.body}>Custom Pattern Builder</Text>
              <Text style={[Typography.caption, { marginTop: 8 }]}>Create your own LED sequences.</Text>
              <View style={{ flexDirection: 'row', gap: 8, marginTop: 16 }}>
                <TouchableOpacity style={styles.colorButton} />
                <TouchableOpacity style={[styles.colorButton, { backgroundColor: '#FF00FF' }]} />
                <TouchableOpacity style={[styles.colorButton, { backgroundColor: '#00FFFF' }]} />
                <TouchableOpacity style={[styles.colorButton, { backgroundColor: 'transparent', alignItems: 'center', justifyContent: 'center' }]}>
                  <Text style={{ color: Colors.textMuted }}>+</Text>
                </TouchableOpacity>
              </View>
              <View style={styles.controlRow}>
                <Text style={Typography.caption}>Sequence Speed</Text>
                <View style={[styles.placeholderSlider, { backgroundColor: 'rgba(255,255,255,0.1)' }]}>
                  <View style={[styles.sliderFill, { width: '30%', backgroundColor: Colors.primary }]} />
                </View>
              </View>
            </View>
          )}
        </View>

        {/* Global Brightness */}
        <View style={[styles.controlRow, { marginTop: 32 }]}>
          <Text style={Typography.caption}>Global Brightness</Text>
          <View style={styles.placeholderSlider}>
            <View style={[styles.sliderFill, { width: '80%' }]} />
          </View>
        </View>

      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    marginTop: 20,
  },
  tabContainer: {
    flexDirection: 'row',
    backgroundColor: Colors.surfaceHighlight,
    borderRadius: Layout.borderRadius,
    padding: 4,
    marginBottom: 20,
  },
  tab: {
    flex: 1,
    paddingVertical: 12,
    alignItems: 'center',
    borderRadius: Layout.borderRadius - 4,
  },
  activeTab: {
    backgroundColor: Colors.primary,
  },
  tabText: {
    ...Typography.body,
    color: Colors.textMuted,
    fontWeight: 'bold',
  },
  activeTabText: {
    color: Colors.text,
  },
  controlsContainer: {
    padding: 20,
    backgroundColor: Colors.surface,
    borderRadius: Layout.borderRadius,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
  },
  modesScroll: {
    flexDirection: 'row',
    marginBottom: 20,
  },
  modePill: {
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 20,
    backgroundColor: Colors.background,
    marginRight: 12,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
  },
  activeModePill: {
    backgroundColor: Colors.secondary,
    borderColor: Colors.secondary,
  },
  modePillText: {
    color: Colors.textMuted,
    fontWeight: '600',
  },
  activeModePillText: {
    color: Colors.background,
    fontWeight: 'bold',
  },
  activeModeContainer: {
    minHeight: 120,
  },
  controlRow: {
    marginTop: 20,
  },
  placeholderSlider: {
    height: 8,
    backgroundColor: Colors.surfaceHighlight,
    borderRadius: 4,
    marginTop: 8,
    overflow: 'hidden',
  },
  sliderFill: {
    height: '100%',
    backgroundColor: Colors.secondary,
    borderRadius: 4,
  },
  colorGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    marginTop: 16,
    justifyContent: 'flex-start',
    gap: 12,
  },
  colorButton: {
    width: 32,
    height: 32,
    borderRadius: 16,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.2)',
  },
  selectedColorButton: {
    borderWidth: 3,
    borderColor: Colors.text,
    transform: [{ scale: 1.1 }]
  },
  presetContainer: {
    marginTop: 16,
    gap: 12,
  },
  presetCard: {
    padding: 16,
    backgroundColor: Colors.surfaceHighlight,
    borderRadius: Layout.borderRadius,
    borderWidth: 1,
    borderLeftWidth: 6,
  },
  presetTitle: {
    ...Typography.body,
    fontWeight: 'bold',
    color: Colors.text,
  },
  presetDesc: {
    ...Typography.caption,
    marginTop: 4,
  }
});
