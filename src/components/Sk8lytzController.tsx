import React, { useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView, FlatList, Platform } from 'react-native';
import { Colors, Typography, Layout } from '../theme/theme';
import ProductVisualizer from './ProductVisualizer';
import CustomSlider from './CustomSlider';
import ArcPatternWheel from './ArcPatternWheel';
import SpectrumVisualizer from './SpectrumVisualizer';
import { getRbmPatternName } from '../constants/RbmPatterns';

type ProductType = 'HALOZ' | 'SOULZ';
type ModeType = 'PRESETS' | 'FIXED' | 'RBM' | 'MUSIC' | 'CAMERA' | 'CUSTOM';

const MUSIC_PATTERNS = [
  'Energetic',
  'Rhythm',
  'Spectrum',
  'Rolling',
  'Pulse',
  'Flash',
  'Wave',
  'Heartbeat',
  'Breathing',
  'Strobe'
];


interface Sk8lytzControllerProps {
  lockedProduct?: ProductType;
  isPaired?: boolean;
  points?: number;
  devices?: any[];
  onLongPressDevice?: (device: any) => void;
}

export default function Sk8lytzController({ lockedProduct, isPaired, points, devices, onLongPressDevice }: Sk8lytzControllerProps) {
  const [activeProduct, setActiveProduct] = useState<ProductType>(lockedProduct || 'HALOZ');
  const [activeMode, setActiveMode] = useState<ModeType>('PRESETS');
  const [selectedColor, setSelectedColor] = useState<string>('#00F0FF');
  const [selectedPatternId, setSelectedPatternId] = useState<number>(1);
  const [brightness, setBrightness] = useState<number>(80);
  const [speed, setSpeed] = useState<number>(65);
  const [micSensitivity, setMicSensitivity] = useState<number>(50);
  const [musicHue, setMusicHue] = useState<number>(180);
  const [musicMode, setMusicModeState] = useState<'SCREEN' | 'BAR'>('SCREEN');
  const [musicPatternId, setMusicPatternId] = useState<number>(1);
  const [micSource, setMicSource] = useState<'APP' | 'DEVICE'>('APP');
  const [musicOption, setMusicOption] = useState<'DROP' | 'COLUMN' | 'COLOR'>('COLUMN');
  const [musicSetting, setMusicSetting] = useState<'SENSITIVITY' | 'BRIGHTNESS'>('SENSITIVITY');

  const [fixedPatternId, setFixedPatternId] = useState<number>(1);
  const [fixedColorMode, setFixedColorMode] = useState<'FOREGROUND' | 'BACKGROUND'>('FOREGROUND');
  const [fixedFgColor, setFixedFgColor] = useState<string>('#00FF00');
  const [fixedBgColor, setFixedBgColor] = useState<string>('#000000');
  const [fixedHue, setFixedHue] = useState<number>(120);

  React.useEffect(() => {
    if (lockedProduct) {
      setActiveProduct(lockedProduct);
    }
  }, [lockedProduct]);

  const modes: { id: ModeType; label: string }[] = [
    { id: 'PRESETS', label: 'Neogleamz Presets' },
    { id: 'FIXED', label: 'Fixed Colors' },
    { id: 'RBM', label: '100+ RBM Modes' },
    { id: 'MUSIC', label: 'Music Sync' },
    { id: 'CAMERA', label: 'Camera Color' },
    { id: 'CUSTOM', label: 'Custom' }
  ];

  return (
    <View style={styles.container}>
      
      {/* Product Selector - Only show if NO lockedProduct is provided */}
      {!lockedProduct && (
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
      )}

      {/* Visual Product Shape Selector/Indicator - ENLARGED FOCUS */}
      <View style={styles.visualizerWrapper}>
        <ProductVisualizer 
          product={activeProduct} 
          color={activeMode === 'FIXED' ? (fixedColorMode === 'FOREGROUND' ? fixedFgColor : fixedBgColor) : selectedColor} 
          mode={activeMode} 
          patternId={selectedPatternId} 
          isPaired={isPaired}
          points={points}
          devices={devices}
          onLongPressDevice={onLongPressDevice}
        />
      </View>

      <Text style={[Typography.title, { marginBottom: 16, marginTop: 4, textAlign: 'center' }]}>SK8Lytz Controls</Text>

      <View style={styles.controlsContainer}>
        <View style={{ flexDirection: 'row', flexWrap: 'wrap', justifyContent: 'space-between', marginBottom: 24 }}>
          {[
            { id: 'FIXED', label: 'Fixed' },
            { id: 'RBM', label: 'Programs' },
            { id: 'MUSIC', label: 'Music' },
            { id: 'CAMERA', label: 'Camera' },
            { id: 'CUSTOM', label: 'Custom' },
            { id: 'PRESETS', label: 'Presets' }
          ].map((mode: any) => (
            <TouchableOpacity 
              key={mode.id}
              style={[
                styles.modePill, 
                { width: '31%', marginBottom: 8, paddingVertical: 10, alignSelf: 'stretch' },
                activeMode === mode.id && styles.activeModePill
              ]}
              onPress={() => setActiveMode(mode.id)}
            >
              <Text 
                style={[
                  styles.modePillText, 
                  activeMode === mode.id && styles.activeModePillText,
                  { fontSize: 13, textAlign: 'center' }
                ]}
                numberOfLines={1}
                adjustsFontSizeToFit
              >
                {mode.label}
              </Text>
            </TouchableOpacity>
          ))}
        </View>

        <View style={styles.activeModeContainer}>
          {activeMode === 'PRESETS' && (
            <View style={{ marginBottom: 16 }}>
              <Text style={Typography.title}>Signature Lighting Presets</Text>
              <Text style={[Typography.caption, { marginTop: 8 }]}>One-tap replicas of our official product showcase effects.</Text>
              
              <View style={styles.presetContainer}>
                {activeProduct === 'HALOZ' ? (
                  <TouchableOpacity 
                    style={[styles.presetCard, { borderColor: Colors.primary }]}
                    onPress={() => setSelectedColor('#FF0000')}
                  >
                    <Text style={styles.presetTitle}>HALOZ Rainbow Flow</Text>
                    <Text style={styles.presetDesc}>Continuous 360° full-spectrum color chase simulating individual WS2812B LEDs moving at moderate speed.</Text>
                  </TouchableOpacity>
                ) : (
                  <>
                    <TouchableOpacity 
                      style={[styles.presetCard, { borderColor: Colors.secondary, marginBottom: 12 }]}
                      onPress={() => setSelectedColor('#00FFFF')}
                    >
                      <Text style={styles.presetTitle}>SOULZ Cyan Pulse</Text>
                      <Text style={styles.presetDesc}>Slow, breathing ice-cyan sequence cascading across your addressable underglow LEDs.</Text>
                    </TouchableOpacity>
                    <TouchableOpacity 
                      style={[styles.presetCard, { borderColor: Colors.primary }]}
                      onPress={() => setSelectedColor('#FF0000')}
                    >
                      <Text style={styles.presetTitle}>SOULZ Rainbow Flow</Text>
                      <Text style={styles.presetDesc}>Continuous full-spectrum color chase simulating individual addressable LEDs moving at moderate speed.</Text>
                    </TouchableOpacity>
                  </>
                )}
              </View>
            </View>
          )}

          {activeMode === 'FIXED' && (
            <View style={{ marginBottom: 16 }}>
              
              <View style={{ backgroundColor: Colors.surfaceHighlight, borderRadius: 8, padding: 8, marginBottom: 16 }}>
                {[
                  { id: 1, label: 'Solid', dots: ['#00FF00','#00FF00','#00FF00','#00FF00','#00FF00','#00FF00','#00FF00','#00FF00'] },
                  { id: 2, label: 'Single Dot', dots: ['#00FFFF','transparent','transparent','transparent','transparent','transparent','transparent','transparent'] },
                  { id: 3, label: 'Comet', dots: ['#FF0000','rgba(255,0,0,0.6)','rgba(255,0,0,0.3)','rgba(255,0,0,0.1)','transparent','transparent','transparent','transparent'] },
                  { id: 4, label: 'Dashed', dots: ['#FFFF00','#FFFF00','transparent','transparent','#FFFF00','#FFFF00','transparent','transparent'] },
                  { id: 5, label: 'Alternating', dots: ['#FF00FF','transparent','#FF00FF','transparent','#FFFFFF','transparent','#FFFFFF','transparent'] }
                ].map(pattern => (
                  <TouchableOpacity 
                    key={pattern.id}
                    onPress={() => setFixedPatternId(pattern.id)}
                    style={{ flexDirection: 'row', alignItems: 'center', paddingVertical: 12, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.05)' }}
                  >
                    <Text style={{ marginRight: 12, fontSize: 18 }}>🎨</Text>
                    <View style={{ flexDirection: 'row', flex: 1, gap: 4 }}>
                      {pattern.dots.map((c, i) => (
                        <View key={i} style={{ width: 8, height: 8, borderRadius: 4, backgroundColor: c }} />
                      ))}
                    </View>
                    <Text style={{ color: Colors.textMuted, fontSize: 18 }}>{fixedPatternId === pattern.id ? '✓' : '→'}</Text>
                  </TouchableOpacity>
                ))}
              </View>

              <View style={{ flexDirection: 'row', marginBottom: 12 }}>
                <TouchableOpacity 
                  onPress={() => setFixedColorMode('FOREGROUND')}
                  style={{ flex: 1, paddingVertical: 10, alignItems: 'center', backgroundColor: fixedColorMode === 'FOREGROUND' ? '#00AEEF' : Colors.surfaceHighlight, borderTopLeftRadius: Layout.borderRadius, borderBottomLeftRadius: Layout.borderRadius }}
                >
                  <Text style={{ color: fixedColorMode === 'FOREGROUND' ? '#FFF' : Colors.textMuted, fontWeight: 'bold' }}>FOREGROUND</Text>
                </TouchableOpacity>
                <TouchableOpacity 
                  onPress={() => setFixedColorMode('BACKGROUND')}
                  style={{ flex: 1, paddingVertical: 10, alignItems: 'center', backgroundColor: fixedColorMode === 'BACKGROUND' ? '#3B4A5A' : Colors.surfaceHighlight, borderTopRightRadius: Layout.borderRadius, borderBottomRightRadius: Layout.borderRadius }}
                >
                  <Text style={{ color: fixedColorMode === 'BACKGROUND' ? '#FFF' : Colors.textMuted, fontWeight: 'bold' }}>BACKGROUND</Text>
                </TouchableOpacity>
              </View>

              <View style={[styles.colorGrid, { marginBottom: 16 }]}>
                {['#FF0000', '#FFFF00', '#00FF00', '#00FFFF', '#0000FF', '#FF00FF', '#FFFFFF'].map(color => (
                  <TouchableOpacity 
                    key={color} 
                    onPress={() => fixedColorMode === 'FOREGROUND' ? setFixedFgColor(color) : setFixedBgColor(color)}
                    style={[
                      styles.colorButton, 
                      { backgroundColor: color, flex: 1, marginHorizontal: 2, height: 40, borderRadius: 4 },
                      (fixedColorMode === 'FOREGROUND' ? fixedFgColor : fixedBgColor) === color && styles.selectedColorButton
                    ]} 
                  />
                ))}
              </View>

              <View style={styles.sceneSlidersContainer}>
                <View style={[styles.controlRow, { marginTop: 0, height: 40 }]}>
                   <CustomSlider 
                    gradientTrack={true}
                    value={fixedHue}
                    onValueChange={(hue) => {
                      setFixedHue(hue);
                      const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                      const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0")).join("");
                      const hex = rgb2hex(f(5), f(3), f(1));
                      if (fixedColorMode === 'FOREGROUND') setFixedFgColor(hex);
                      else setFixedBgColor(hex);
                    }}
                    minimumValue={0}
                    maximumValue={360}
                    style={{ position: 'absolute', width: '100%', height: 40 }}
                  />
                </View>

                <View style={[styles.controlRow, { marginTop: 16 }]}>
                  <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center' }}>
                    <Text style={{ color: Colors.textMuted, marginRight: 8 }}>☀️</Text>
                    <CustomSlider 
                      value={brightness}
                      onValueChange={setBrightness}
                      minimumValue={0}
                      maximumValue={100}
                      style={{ flex: 1 }}
                    />
                    <Text style={{ color: Colors.textMuted, marginLeft: 8 }}>☀️</Text>
                  </View>
                </View>

                <View style={[styles.controlRow, { marginTop: 12 }]}>
                  <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center' }}>
                    <Text style={{ color: Colors.textMuted, marginRight: 8 }}>🚀</Text>
                    <CustomSlider 
                      value={speed}
                      onValueChange={setSpeed}
                      minimumValue={0}
                      maximumValue={100}
                      style={{ flex: 1 }}
                    />
                    <Text style={{ color: Colors.textMuted, marginLeft: 8 }}>🚀</Text>
                  </View>
                </View>

              </View>
            </View>
          )}

          {activeMode === 'RBM' && (
            <View style={styles.sceneContainer}>
              <View style={styles.sceneHeader}>
                <Text style={styles.sceneTitle}>100+ RBM Modes</Text>
              </View>
              
              <View style={styles.rbmWheelSection}>
                <ArcPatternWheel 
                  value={selectedPatternId} 
                  onValueChange={setSelectedPatternId} 
                  min={1} 
                  max={100} 
                  itemLabel={(item: number) => getRbmPatternName(item)}
                />
              </View>

              <View style={styles.sceneSlidersContainer}>
                <View style={styles.controlRow}>
                  <View style={{ flex: 1 }}>
                    <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginBottom: 4 }}>
                      <Text style={styles.sceneLabel}>Brightness</Text>
                      <Text style={[styles.sceneLabel, { color: Colors.primary }]}>{brightness}%</Text>
                    </View>
                    <CustomSlider 
                      value={brightness}
                      onValueChange={setBrightness}
                      minimumValue={0}
                      maximumValue={100}
                    />
                  </View>
                </View>

                <View style={[styles.controlRow, { marginTop: 12 }]}>
                  <View style={{ flex: 1 }}>
                    <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginBottom: 4 }}>
                      <Text style={styles.sceneLabel}>Pattern Speed</Text>
                      <Text style={[styles.sceneLabel, { color: Colors.primary }]}>{speed}%</Text>
                    </View>
                    <CustomSlider 
                      value={speed}
                      onValueChange={setSpeed}
                      minimumValue={0}
                      maximumValue={100}
                    />
                  </View>
                </View>
              </View>
            </View>
          )}

          {activeMode === 'MUSIC' && (
            <View style={styles.sceneContainer}>
              <View style={styles.musicToggleHeader}>
                <TouchableOpacity 
                  style={[styles.musicToggleOption, musicMode === 'SCREEN' && styles.musicToggleActive]} 
                  onPress={() => setMusicModeState('SCREEN')}
                >
                  <Text style={[styles.musicToggleText, musicMode === 'SCREEN' && styles.musicToggleActiveText]}>LIGHT SCREEN MODE</Text>
                </TouchableOpacity>
                <View style={styles.musicModeIndicator}>
                  <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                    <TouchableOpacity onPress={() => setMusicPatternId(prev => (prev > 1 ? prev - 1 : 10))} style={{ paddingHorizontal: 16 }}>
                      <Text style={{ color: '#FFF', fontSize: 24, fontWeight: 'bold' }}>{'<'}</Text>
                    </TouchableOpacity>
                    <View style={styles.musicModeCircle}>
                      <Text style={[styles.musicModeNumber, { fontSize: 16 }]}>{musicPatternId}</Text>
                      <View style={styles.musicModeRefresh}>
                        <Text style={{ color: '#000', fontSize: 10, fontWeight: 'bold' }}>↺</Text>
                      </View>
                    </View>
                    <TouchableOpacity onPress={() => setMusicPatternId(prev => (prev < 10 ? prev + 1 : 1))} style={{ paddingHorizontal: 16 }}>
                      <Text style={{ color: '#FFF', fontSize: 24, fontWeight: 'bold' }}>{'>'}</Text>
                    </TouchableOpacity>
                  </View>
                  <Text style={{ color: Colors.primary, fontSize: 12, marginTop: 8, fontWeight: '800', textTransform: 'uppercase', letterSpacing: 1 }}>
                    {MUSIC_PATTERNS[musicPatternId - 1]}
                  </Text>
                </View>
                <TouchableOpacity 
                  style={[styles.musicToggleOption, musicMode === 'BAR' && styles.musicToggleActive]} 
                  onPress={() => setMusicModeState('BAR')}
                >
                  <Text style={[styles.musicToggleText, musicMode === 'BAR' && styles.musicToggleActiveText]}>LIGHT BAR MODE</Text>
                </TouchableOpacity>
              </View>

              <View style={styles.musicVisualizerSection}>
                <SpectrumVisualizer />
              </View>

              <View style={styles.micControlSection}>
                <TouchableOpacity style={styles.micIconBtn} onPress={() => setMicSource('APP')}>
                  <Text style={[styles.micIconText, micSource === 'APP' && { color: Colors.primary }]}>🎙️</Text>
                  <Text style={styles.micSubText}>APP microphone</Text>
                </TouchableOpacity>

                <TouchableOpacity style={styles.playButtonMain}>
                  <View style={styles.playIconInner}>
                    <Text style={{ color: '#FFF', fontSize: 24, marginLeft: 4 }}>▶</Text>
                  </View>
                </TouchableOpacity>

                <TouchableOpacity style={styles.micIconBtn} onPress={() => setMicSource('DEVICE')}>
                  <Text style={[styles.micIconText, micSource === 'DEVICE' && { color: Colors.primary }]}>🎤</Text>
                  <Text style={styles.micSubText}>device microphone</Text>
                </TouchableOpacity>
              </View>

              <View style={styles.musicOptionsGrid}>
                <TouchableOpacity style={styles.radioItem} onPress={() => setMusicOption('DROP')}>
                  <View style={[styles.radioOuter, musicOption === 'DROP' && styles.radioActive]}>
                    <View style={musicOption === 'DROP' && styles.radioInner} />
                  </View>
                  <Text style={styles.radioLabel}>drop color</Text>
                </TouchableOpacity>

                <TouchableOpacity style={styles.radioItem} onPress={() => setMusicOption('COLUMN')}>
                  <View style={[styles.radioOuter, musicOption === 'COLUMN' && styles.radioActive]}>
                    <View style={musicOption === 'COLUMN' && styles.radioInner} />
                  </View>
                  <Text style={styles.radioLabel}>sound column</Text>
                </TouchableOpacity>
              </View>

              <View style={styles.sceneSlidersContainer}>
                <View style={[styles.controlRow, { marginTop: 0, height: 40 }]}>
                   <View style={styles.gradientSliderTrack} />
                   <CustomSlider 
                    value={musicHue}
                    onValueChange={setMusicHue}
                    minimumValue={0}
                    maximumValue={360}
                    style={{ position: 'absolute', width: '100%', height: 40 }}
                  />
                </View>

                <View style={[styles.musicSettingsToggleRow, { marginTop: 20 }]}>
                  <TouchableOpacity style={styles.radioItem} onPress={() => setMusicSetting('SENSITIVITY')}>
                    <View style={[styles.radioOuter, musicSetting === 'SENSITIVITY' && styles.radioActive]}>
                      <View style={musicSetting === 'SENSITIVITY' && styles.radioInner} />
                    </View>
                    <Text style={styles.radioLabel}>sensitivity</Text>
                  </TouchableOpacity>

                  <TouchableOpacity style={styles.radioItem} onPress={() => setMusicSetting('BRIGHTNESS')}>
                    <View style={[styles.radioOuter, musicSetting === 'BRIGHTNESS' && styles.radioActive]}>
                      <View style={musicSetting === 'BRIGHTNESS' && styles.radioInner} />
                    </View>
                    <Text style={styles.radioLabel}>Brightness</Text>
                  </TouchableOpacity>
                </View>

                <View style={[styles.controlRow, { marginTop: 8 }]}>
                  <CustomSlider 
                    value={musicSetting === 'SENSITIVITY' ? micSensitivity : brightness}
                    onValueChange={musicSetting === 'SENSITIVITY' ? setMicSensitivity : setBrightness}
                    minimumValue={0}
                    maximumValue={100}
                  />
                </View>
              </View>
            </View>
          )}

          {activeMode === 'CAMERA' && (
            <View style={{ marginBottom: 16 }}>
              <Text style={Typography.title}>Camera Color Picker</Text>
              <Text style={[Typography.caption, { marginTop: 8 }]}>Point your camera at any object to extract real-time ambient color.</Text>
              <TouchableOpacity style={[styles.presetCard, { marginTop: 16, alignItems: 'center', justifyContent: 'center', paddingVertical: 16 }]} >
                <Text style={[Typography.body, { color: Colors.primary, fontWeight: 'bold' }]}>+ Open Camera</Text>
              </TouchableOpacity>
            </View>
          )}

          {activeMode === 'CUSTOM' && (
            <View style={{ marginBottom: 16 }}>
              <Text style={Typography.title}>Custom Pattern Builder</Text>
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
                <CustomSlider 
                  style={{ flex: 1, marginLeft: 16 }}
                  value={speed}
                  onValueChange={setSpeed}
                  minimumValue={0}
                  maximumValue={100}
                />
              </View>
            </View>
          )}
        </View>

      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    paddingHorizontal: 12,
    paddingBottom: 20,
    paddingTop: 0,
  },
  visualizerWrapper: {
    transform: [{ scale: 1.15 }],
    marginVertical: 4,
    alignItems: 'center',
    justifyContent: 'center',
  },
  tabContainer: {
    flexDirection: 'row',
    backgroundColor: Colors.surfaceHighlight,
    borderRadius: Layout.borderRadius,
    padding: 4,
    marginBottom: 12,
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
    padding: 14,
    backgroundColor: Colors.surface,
    borderRadius: Layout.borderRadius,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
  },
  modesScroll: {
    flexDirection: 'row',
    marginBottom: 12,
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
    marginTop: 12,
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
  },
  sceneContainer: {
    backgroundColor: '#050505',
    borderRadius: 24,
    padding: 2,
    marginTop: 8,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.05)',
  },
  sceneHeader: {
    padding: 16,
    borderBottomWidth: 1,
    borderBottomColor: 'rgba(255,255,255,0.05)',
    alignItems: 'center',
  },
  sceneTitle: {
    ...Typography.title,
    color: '#FFF',
    fontSize: 18,
  },
  rbmWheelSection: {
    height: 180,
    backgroundColor: '#050505',
    justifyContent: 'center',
    alignItems: 'center',
  },
  sceneSlidersContainer: {
    padding: 16,
    backgroundColor: 'rgba(255,255,255,0.02)',
  },
  sceneLabel: {
    ...Typography.caption,
    color: 'rgba(255,255,255,0.7)',
    fontSize: 12,
    textTransform: 'uppercase',
    letterSpacing: 1,
    fontWeight: '700',
  },
  musicToggleHeader: {
    flexDirection: 'row',
    backgroundColor: 'rgba(255,255,255,0.05)',
    borderRadius: 25,
    padding: 4,
    alignItems: 'center',
    margin: 12,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
  },
  musicToggleOption: {
    flex: 1,
    paddingVertical: 10,
    alignItems: 'center',
    borderRadius: 20,
  },
  musicToggleActive: {
    backgroundColor: Colors.primary, 
  },
  musicToggleActiveText: {
    color: '#000',
    fontWeight: 'bold',
  },
  musicToggleText: {
    color: 'rgba(255,255,255,0.5)',
    fontSize: 11,
    fontWeight: '700',
  },
  musicModeIndicator: {
    width: 60,
    alignItems: 'center',
    justifyContent: 'center',
  },
  musicModeCircle: {
    width: 44,
    height: 44,
    borderRadius: 22,
    borderWidth: 2,
    borderColor: Colors.primary,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'rgba(0,0,0,0.6)',
  },
  musicModeNumber: {
    color: '#FFF',
    fontSize: 18,
    fontWeight: 'bold',
  },
  musicModeRefresh: {
    position: 'absolute',
    top: -5,
    right: -5,
    width: 20,
    height: 20,
    borderRadius: 10,
    backgroundColor: Colors.primary,
    alignItems: 'center',
    justifyContent: 'center',
  },
  musicVisualizerSection: {
    paddingHorizontal: 16,
    marginBottom: 16,
  },
  micControlSection: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 30,
    marginBottom: 20,
  },
  micIconBtn: {
    alignItems: 'center',
  },
  micIconText: {
    fontSize: 32,
    color: 'rgba(255,255,255,0.4)',
  },
  micSubText: {
    fontSize: 10,
    color: 'rgba(255,255,255,0.3)',
    marginTop: 4,
    textTransform: 'uppercase',
    fontWeight: '600',
  },
  playButtonMain: {
    width: 70,
    height: 70,
    borderRadius: 35,
    borderWidth: 2,
    borderColor: Colors.primary,
    alignItems: 'center',
    justifyContent: 'center',
  },
  playIconInner: {
    width: 58,
    height: 58,
    borderRadius: 29,
    backgroundColor: Colors.primary,
    alignItems: 'center',
    justifyContent: 'center',
  },
  musicOptionsGrid: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    paddingHorizontal: 20,
    marginBottom: 20,
  },
  radioItem: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  radioOuter: {
    width: 20,
    height: 20,
    borderRadius: 10,
    borderWidth: 2,
    borderColor: 'rgba(255,255,255,0.3)',
    marginRight: 8,
    alignItems: 'center',
    justifyContent: 'center',
  },
  radioActive: {
    borderColor: Colors.primary,
  },
  radioInner: {
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: Colors.primary,
  },
  radioLabel: {
    color: '#FFF',
    fontSize: 15,
    fontWeight: '600',
  },
  gradientSliderTrack: {
    height: 6,
    borderRadius: 3,
    backgroundColor: '#333',
    overflow: 'hidden',
  },
  musicSettingsToggleRow: {
    flexDirection: 'row',
    justifyContent: 'flex-start',
    gap: 30,
    marginBottom: 12,
  }
});
