import React, { useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView, FlatList, Platform } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { Typography, Layout } from '../theme/theme';
import { useTheme } from '../context/ThemeContext';
import ProductVisualizer from './ProductVisualizer';
import CustomSlider from './CustomSlider';
import ArcPatternWheel from './ArcPatternWheel';
import SpectrumVisualizer from './SpectrumVisualizer';
import CameraTracker from './CameraTracker';
import { getRbmPatternName } from '../constants/RbmPatterns';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { MaterialCommunityIcons } from '@expo/vector-icons';

type ProductType = 'HALOZ' | 'SOULZ';
type ModeType = 'PRESETS' | 'FIXED' | 'RBM' | 'MUSIC' | 'CAMERA' | 'CUSTOM' | 'MULTICOLOR';

const MUSIC_PATTERNS = [
  'Rock',
  'Normal',
  'Jazz',
  'Classical'
];


interface Sk8lytzControllerProps {
  lockedProduct?: ProductType;
  isPaired?: boolean;
  points?: number;
  devices?: any[];
  onLongPressDevice?: (device: any) => void;
  writeToDevice?: (payload: number[]) => Promise<void>;
  isPoweredOn?: boolean;
}

export default function Sk8lytzController({ lockedProduct, isPaired, points, devices, onLongPressDevice, writeToDevice, isPoweredOn = true }: Sk8lytzControllerProps) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const [activeProduct, setActiveProduct] = useState<ProductType>(lockedProduct || 'HALOZ');
  const [activeMode, setActiveMode] = useState<ModeType>('PRESETS');
  const [selectedColor, setSelectedColor] = useState<string>('#00F0FF');
  const [selectedHue, setSelectedHue] = useState<number>(180);
  const [selectedPatternId, setSelectedPatternId] = useState<number>(1);
  const [brightness, setBrightness] = useState<number>(90);
  const [speed, setSpeed] = useState<number>(50);
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

  React.useEffect(() => {
    AsyncStorage.getItem('@Sk8lytz_ControllerState').then((saved) => {
      if (saved) {
        try {
          const parsed = JSON.parse(saved);
          if (parsed.activeMode) setActiveMode(parsed.activeMode);
          if (parsed.selectedColor) setSelectedColor(parsed.selectedColor);
          if (parsed.selectedPatternId) setSelectedPatternId(parsed.selectedPatternId);
          if (parsed.brightness !== undefined) setBrightness(parsed.brightness);
          else setBrightness(90);
          if (parsed.speed !== undefined) setSpeed(parsed.speed);
          else setSpeed(50);
          if (parsed.micSensitivity !== undefined) setMicSensitivity(parsed.micSensitivity);
          if (parsed.musicHue !== undefined) setMusicHue(parsed.musicHue);
          if (parsed.musicMode) setMusicModeState(parsed.musicMode);
          if (parsed.musicPatternId) setMusicPatternId(parsed.musicPatternId);
          if (parsed.micSource) setMicSource(parsed.micSource);
          if (parsed.musicOption) setMusicOption(parsed.musicOption);
          if (parsed.musicSetting) setMusicSetting(parsed.musicSetting);
          if (parsed.fixedPatternId) setFixedPatternId(parsed.fixedPatternId);
          if (parsed.fixedColorMode) setFixedColorMode(parsed.fixedColorMode);
          if (parsed.fixedFgColor) setFixedFgColor(parsed.fixedFgColor);
          if (parsed.fixedBgColor) setFixedBgColor(parsed.fixedBgColor);
          if (parsed.fixedHue !== undefined) setFixedHue(parsed.fixedHue);
        } catch(e) {}
      }
    });
  }, []);

  React.useEffect(() => {
    const stateBlob = {
      activeMode, selectedColor, selectedPatternId, brightness, speed,
      micSensitivity, musicHue, musicMode, musicPatternId, micSource, musicOption, musicSetting,
      fixedPatternId, fixedColorMode, fixedFgColor, fixedBgColor, fixedHue
    };
    AsyncStorage.setItem('@Sk8lytz_ControllerState', JSON.stringify(stateBlob)).catch(() => {});
  }, [
    activeMode, selectedColor, selectedPatternId, brightness, speed, 
    micSensitivity, musicHue, musicMode, musicPatternId, micSource, musicOption, musicSetting,
    fixedPatternId, fixedColorMode, fixedFgColor, fixedBgColor, fixedHue
  ]);

  const handleMusicChange = (
    patternId: number = musicPatternId,
    sens: number = micSensitivity,
    bright: number = brightness,
    src: 'APP' | 'DEVICE' = micSource,
    currentHue: number = musicHue
  ) => {
    if (!writeToDevice) return;
    
    const isDeviceMic = src === 'DEVICE';
    const modeTypeVal = 0x26;
    
    const f1 = (n: number, k = (n + currentHue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
    const c1 = { r: Math.round(f1(5) * 255), g: Math.round(f1(3) * 255), b: Math.round(f1(1) * 255) };
    
    const compHue = (currentHue + 180) % 360;
    const f2 = (n: number, k = (n + compHue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
    const c2 = { r: Math.round(f2(5) * 255), g: Math.round(f2(3) * 255), b: Math.round(f2(1) * 255) };
    
    writeToDevice(ZenggeProtocol.setMusicConfig(
      isDeviceMic,
      modeTypeVal,
      patternId,
      c1,
      c2,
      sens,
      bright
    ));
  };

  const getColorName = (hex: string) => {
    const map: {[key: string]: string} = {
      '#FF0000': 'Red', '#FFFF00': 'Yellow', '#00FF00': 'Green', 
      '#00FFFF': 'Cyan', '#0000FF': 'Blue', '#FF00FF': 'Magenta', 
      '#FFFFFF': 'White', '#000000': 'Black'
    };
    const upperHex = hex.toUpperCase();
    return map[upperHex] || 'Custom';
  };

  const currentStatusText = React.useMemo(() => {
    switch(activeMode) {
      case 'FIXED':
        return `Fixed - ${getColorName(fixedColorMode === 'FOREGROUND' ? fixedFgColor : fixedBgColor)}`;
      case 'RBM':
        return `Programs - ${getRbmPatternName(selectedPatternId)}`;
      case 'MUSIC':
        return `Music - ${MUSIC_PATTERNS[musicPatternId - 1] || 'Pattern ' + musicPatternId}`;
      case 'CAMERA': return 'Camera';
      case 'CUSTOM': return 'Custom';
      case 'MULTICOLOR': return 'Multicolor';
      case 'PRESETS': return 'Presets';
      default: return activeMode;
    }
  }, [activeMode, fixedColorMode, fixedFgColor, fixedBgColor, selectedPatternId, musicPatternId, selectedColor]);

  const modes: { id: ModeType; label: string; icon: keyof typeof MaterialCommunityIcons.glyphMap }[] = [
    { id: 'PRESETS', label: 'Favorites', icon: 'star-outline' },
    { id: 'FIXED', label: 'Fixed', icon: 'palette-outline' },
    { id: 'RBM', label: 'Programs', icon: 'auto-fix' },
    { id: 'MUSIC', label: 'Music', icon: 'music-note' },
    { id: 'CAMERA', label: 'Camera', icon: 'camera-outline' },
    { id: 'MULTICOLOR', label: 'Multi', icon: 'gradient-horizontal' },
    { id: 'CUSTOM', label: 'DIY', icon: 'cog-outline' }
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
            {activeProduct === 'HALOZ' && (
              <LinearGradient colors={[Colors.primary, Colors.accent]} start={{x: 0, y: 0}} end={{x: 1, y: 1}} style={StyleSheet.absoluteFill} />
            )}
            <Text style={[styles.tabText, activeProduct === 'HALOZ' && styles.activeTabText]}>
               HALOZ
            </Text>
          </TouchableOpacity>
          <TouchableOpacity 
            style={[styles.tab, activeProduct === 'SOULZ' && styles.activeTab]} 
            onPress={() => setActiveProduct('SOULZ')}
          >
            {activeProduct === 'SOULZ' && (
              <LinearGradient colors={[Colors.secondary, Colors.accent]} start={{x: 0, y: 0}} end={{x: 1, y: 1}} style={StyleSheet.absoluteFill} />
            )}
            <Text style={[styles.tabText, activeProduct === 'SOULZ' && styles.activeTabText]}>
              SOULZ
            </Text>
          </TouchableOpacity>
        </View>
      )}

      {/* Visual Product Shape Selector/Indicator - ENLARGED FOCUS */}
      <View style={styles.visualizerWrapper}>
      <View style={{ marginBottom: 8, width: '100%' }}>
        <ProductVisualizer 
          product={activeProduct} 
          color={activeMode === 'FIXED' ? (fixedColorMode === 'FOREGROUND' ? fixedFgColor : fixedBgColor) : (activeMode === 'MUSIC' ? "#" + [1-Math.max(Math.min((5 + musicHue / 60) % 6, 4 - ((5 + musicHue / 60) % 6), 1), 0), 1-Math.max(Math.min((3 + musicHue / 60) % 6, 4 - ((3 + musicHue / 60) % 6), 1), 0), 1-Math.max(Math.min((1 + musicHue / 60) % 6, 4 - ((1 + musicHue / 60) % 6), 1), 0)].map(x => Math.round(x * 255).toString(16).padStart(2, "0")).join("") : selectedColor)} 
          mode={activeMode} 
          patternId={activeMode === 'MUSIC' ? musicPatternId : (activeMode === 'FIXED' ? fixedPatternId : selectedPatternId)} 
          isPaired={isPaired}
          points={points}
          devices={devices}
          onLongPressDevice={onLongPressDevice}
          brightness={brightness}
          speed={speed}
          fixedFgColor={fixedFgColor}
          fixedBgColor={fixedBgColor}
          isPoweredOn={isPoweredOn}
          statusText={currentStatusText}
        />
      </View>
      </View>

      <View style={styles.controlsContainer}>
        <View style={{ marginBottom: 12 }}>
          <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={{ paddingHorizontal: 4, gap: 12 }}>
            {modes.map((mode: any) => (
              <TouchableOpacity 
                key={mode.id}
                style={[
                  styles.modePill, 
                  { 
                    width: 54, 
                    height: 48, 
                    paddingVertical: 4, 
                    paddingHorizontal: 2, 
                    marginRight: 0, 
                    borderRadius: 8,
                    alignItems: 'center', 
                    justifyContent: 'center',
                    backgroundColor: activeMode === mode.id ? 'transparent' : Colors.background,
                    borderWidth: activeMode === mode.id ? 0 : 1
                  }
                ]}
                onPress={() => setActiveMode(mode.id)}
              >
                {activeMode === mode.id && (
                  <LinearGradient 
                    colors={[Colors.primary, Colors.accent]} 
                    start={{x: 0, y: 0}} end={{x: 1, y: 1}} 
                    style={StyleSheet.absoluteFill} 
                  />
                )}
                <MaterialCommunityIcons 
                  name={mode.icon} 
                  size={20} 
                  color={activeMode === mode.id ? '#FFFFFF' : Colors.textMuted} 
                  style={{ marginBottom: 4, zIndex: 2 }} 
                />
                <Text 
                  style={[
                    styles.modePillText, 
                    activeMode === mode.id && styles.activeModePillText,
                    { fontSize: 10, textAlign: 'center', zIndex: 2, fontWeight: activeMode === mode.id ? 'bold' : '600' }
                  ]}
                >
                  {mode.label}
                </Text>
              </TouchableOpacity>
            ))}
          </ScrollView>
        </View>

        <View style={styles.activeModeContainer}>
          {activeMode === 'PRESETS' && (
            <View style={{ marginBottom: 8 }}>
              <Text style={Typography.title}>Signature Lighting Presets</Text>
              <Text style={[Typography.caption, { marginTop: 8 }]}>One-tap replicas of our official product showcase effects.</Text>
              
              <View style={styles.presetContainer}>
                  <TouchableOpacity 
                    style={[styles.presetCard, { borderColor: Colors.primary }]}
                    onPress={() => {
                    setSelectedColor('#FF0000');
                    if (writeToDevice) writeToDevice(ZenggeProtocol.setColor(255, 0, 0));
                  }}
                  >
                    <Text style={styles.presetTitle}>{activeProduct} Rainbow Flow</Text>
                    <Text style={styles.presetDesc}>Continuous full-spectrum color chase simulating individual addressable LEDs moving at moderate speed.</Text>
                  </TouchableOpacity>
              </View>
            </View>
          )}

          {activeMode === 'FIXED' && (
            <View style={{ marginBottom: 8 }}>
              
              <View style={{ backgroundColor: Colors.surfaceHighlight, borderRadius: 8, padding: 8, marginBottom: 8, height: 120 }}>
                <ScrollView nestedScrollEnabled={true}>
                {[
                  { id: 1, label: 'Solid', dots: ['#00FF00','#00FF00','#00FF00','#00FF00','#00FF00','#00FF00','#00FF00','#00FF00'] },
                  { id: 2, label: 'Single Dot', dots: ['#00FFFF','transparent','transparent','transparent','transparent','transparent','transparent','transparent'] },
                  { id: 3, label: 'Comet', dots: ['#FF0000','rgba(255,0,0,0.6)','rgba(255,0,0,0.3)','rgba(255,0,0,0.1)','transparent','transparent','transparent','transparent'] },
                  { id: 4, label: 'Dashed', dots: ['#FFFF00','#FFFF00','transparent','transparent','#FFFF00','#FFFF00','transparent','transparent'] },
                  { id: 5, label: 'Alternating', dots: ['#FF00FF','transparent','#FF00FF','transparent','#FFFFFF','transparent','#FFFFFF','transparent'] },
                  { id: 6, label: 'Breath', dots: ['#00FF00', 'rgba(0,255,0,0.5)', 'transparent', 'rgba(0,255,0,0.5)', '#00FF00', 'rgba(0,255,0,0.5)', 'transparent', 'rgba(0,255,0,0.5)'] },
                  { id: 7, label: 'Flash', dots: ['#00FFFF', 'transparent', '#00FFFF', 'transparent', '#00FFFF', 'transparent', '#00FFFF', 'transparent'] },
                  { id: 8, label: 'Strobe', dots: ['#FFFFFF', 'transparent', '#FFFFFF', 'transparent', '#FFFFFF', 'transparent', '#FFFFFF', 'transparent'] },
                  { id: 9, label: 'Wave', dots: ['transparent', 'rgba(0,255,255,0.5)', '#00FFFF', '#00FFFF', 'rgba(0,255,255,0.5)', 'transparent', 'transparent', 'transparent'] },
                  { id: 10, label: 'Pinch', dots: ['#FF00FF', 'rgba(255,0,255,0.5)', 'transparent', 'transparent', 'transparent', 'rgba(255,0,255,0.5)', '#FF00FF', 'transparent'] }
                ].map(pattern => (
                  <TouchableOpacity 
                    key={pattern.id}
                    onPress={() => {
                      setFixedPatternId(pattern.id);
                      if (writeToDevice) {
                        const fgHex = fixedColorMode === 'FOREGROUND' ? fixedFgColor : (fixedFgColor || '#00FF00');
                        const bgHex = fixedColorMode === 'BACKGROUND' ? fixedBgColor : (fixedBgColor || '#000000');
                        const fg = { r: parseInt(fgHex.slice(1, 3), 16), g: parseInt(fgHex.slice(3, 5), 16), b: parseInt(fgHex.slice(5, 7), 16) };
                        const bg = { r: parseInt(bgHex.slice(1, 3), 16), g: parseInt(bgHex.slice(3, 5), 16), b: parseInt(bgHex.slice(5, 7), 16) };
                        
                        if (pattern.id === 1) {
                          writeToDevice(ZenggeProtocol.setColor(fg.r, fg.g, fg.b));
                        } else if (pattern.id === 6) {
                          writeToDevice(ZenggeProtocol.setCustomMode([{ mode: 1, speed, color1: fg, color2: bg }, { mode: 1, speed, color1: bg, color2: fg }]));
                        } else if (pattern.id === 7) {
                          writeToDevice(ZenggeProtocol.setCustomMode([{ mode: 2, speed, color1: fg, color2: bg }, { mode: 2, speed, color1: bg, color2: fg }]));
                        } else if (pattern.id === 8) {
                          writeToDevice(ZenggeProtocol.setCustomMode([{ mode: 2, speed: 100, color1: fg, color2: bg }, { mode: 2, speed: 100, color1: bg, color2: fg }]));
                        } else {
                          let arr: any[] = [];
                          if (pattern.id === 2) arr = [fg, bg, bg, bg, bg, bg, bg, bg];
                          if (pattern.id === 3) arr = [fg, {r: Math.floor(fg.r*0.5), g: Math.floor(fg.g*0.5), b: Math.floor(fg.b*0.5)}, {r: Math.floor(fg.r*0.2), g: Math.floor(fg.g*0.2), b: Math.floor(fg.b*0.2)}, bg, bg, bg];
                          if (pattern.id === 4) arr = [fg, fg, fg, fg, bg, bg, bg, bg];
                          if (pattern.id === 5) arr = [fg, fg, bg, bg];
                          if (pattern.id === 9) arr = [bg, bg, fg, fg, bg, bg];
                          if (pattern.id === 10) arr = [fg, bg, bg, bg, bg, fg];
                          writeToDevice(ZenggeProtocol.setMultiColor(arr, speed, 1));
                        }
                      }
                    }}
                    style={{ flexDirection: 'row', alignItems: 'center', paddingVertical: 8, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.05)' }}
                  >
                    <Text style={{ marginRight: 10, fontSize: 16 }}>🎨</Text>
                    <View style={{ flexDirection: 'row', flex: 1, gap: 3 }}>
                      {pattern.dots.map((c, i) => (
                        <View key={i} style={{ width: 6, height: 6, borderRadius: 3, backgroundColor: c }} />
                      ))}
                    </View>
                    <Text style={{ color: Colors.textMuted, fontSize: 16 }}>{fixedPatternId === pattern.id ? '✓' : '→'}</Text>
                  </TouchableOpacity>
                ))}
                </ScrollView>
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


            </View>
          )}

          {activeMode === 'RBM' && (
            <View style={{ marginBottom: 8 }}>
              <ArcPatternWheel 
                value={selectedPatternId}
                onValueChange={(pid) => {
                  setSelectedPatternId(pid);
                  if (writeToDevice) {
                    writeToDevice(ZenggeProtocol.setRbmMode(pid, speed, brightness));
                  }
                }}
                min={1}
                max={100}
                itemLabel={(val) => getRbmPatternName(val)}
              />
            </View>
          )}

          {activeMode === 'CAMERA' && (
            <View style={[styles.sceneContainer, { padding: 8, backgroundColor: 'transparent', borderWidth: 0 }]}>
              <CameraTracker 
                isActive={activeMode === 'CAMERA'} 
                onColorDetected={(hex) => {
                  setSelectedColor(hex);
                  if (writeToDevice) {
                    const r = parseInt(hex.substring(1, 3), 16);
                    const g = parseInt(hex.substring(3, 5), 16);
                    const b = parseInt(hex.substring(5, 7), 16);
                    writeToDevice(ZenggeProtocol.setColor(r, g, b));
                  }
                }} 
              />
            </View>
          )}

          {activeMode === 'MUSIC' && (
            <View style={styles.sceneContainer}>
              <View style={[styles.musicToggleHeader, { justifyContent: 'center' }]}>
                <View style={[styles.musicModeIndicator, { alignItems: 'center' }]}>
                  <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                    <TouchableOpacity onPress={() => {
                      const pid = musicPatternId > 1 ? musicPatternId - 1 : MUSIC_PATTERNS.length;
                      setMusicPatternId(pid);
                      handleMusicChange(pid);
                    }} style={{ paddingHorizontal: 10 }}>
                      <Text style={{ color: '#FFF', fontSize: 20, fontWeight: 'bold' }}>{'<'}</Text>
                    </TouchableOpacity>
                    <View style={[styles.musicModeCircle, { width: 32, height: 32, borderRadius: 16 }]}>
                      <Text style={[styles.musicModeNumber, { fontSize: 14 }]}>{musicPatternId}</Text>
                    </View>
                    <TouchableOpacity onPress={() => {
                      const pid = musicPatternId < MUSIC_PATTERNS.length ? musicPatternId + 1 : 1;
                      setMusicPatternId(pid);
                      handleMusicChange(pid);
                    }} style={{ paddingHorizontal: 10 }}>
                      <Text style={{ color: '#FFF', fontSize: 20, fontWeight: 'bold' }}>{'>'}</Text>
                    </TouchableOpacity>
                  </View>
                  <Text style={[Typography.caption, { marginTop: 4, color: Colors.primary, fontWeight: 'bold', fontSize: 13 }]}>
                    {MUSIC_PATTERNS[musicPatternId - 1] || `Effect ${musicPatternId}`}
                  </Text>
                </View>
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


            </View>
          )}


          {activeMode === 'MULTICOLOR' && (
            <View style={{ marginBottom: 8 }}>
              <Text style={Typography.title}>Multi-color Segments</Text>
              <Text style={[Typography.caption, { marginTop: 8 }]}>Set unique colors for different parts of your boards.</Text>
              
              <View style={{ flexDirection: 'row', gap: 8, marginTop: 8, flexWrap: 'wrap' }}>
                {[1, 2, 3, 4, 5, 6, 7].map((seg) => (
                  <TouchableOpacity 
                    key={seg} 
                    style={[styles.colorButton, { backgroundColor: seg === 1 ? '#FF0000' : (seg === 2 ? '#00FF00' : '#0000FF'), width: 32, height: 32, borderRadius: 16 }]} 
                  />
                ))}
              </View>

              <TouchableOpacity 
                style={[styles.presetCard, { marginTop: 10, borderLeftColor: Colors.secondary }]}
                onPress={() => {
                  if (writeToDevice) {
                    const segmentColors = [
                      { r: 255, g: 0, b: 0 },
                      { r: 0, g: 255, b: 0 },
                      { r: 0, g: 0, b: 255 },
                      { r: 255, g: 255, b: 0 },
                      { r: 0, g: 255, b: 255 }
                    ];
                    writeToDevice(ZenggeProtocol.setMultiColor(segmentColors, speed, 1));
                  }
                }}
              >
                <Text style={styles.presetTitle}>Apply Segmented Scene</Text>
                <Text style={styles.presetDesc}>Push these colors to your controllers synchronously.</Text>
              </TouchableOpacity>
            </View>
          )}

          {activeMode === 'CUSTOM' && (
            <View style={{ marginBottom: 8 }}>
              <Text style={Typography.title}>DIY Pattern Builder</Text>
              <Text style={[Typography.caption, { marginTop: 8 }]}>Stack up to 32 animated steps for a custom light show.</Text>
              
              <View style={{ backgroundColor: Colors.surfaceHighlight, borderRadius: 8, padding: 12, marginTop: 16, marginBottom: 12 }}>
                 <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 8 }}>
                    <View style={{ width: 12, height: 12, borderRadius: 6, backgroundColor: '#FF0000', marginRight: 12 }} />
                    <Text style={{ color: '#FFF', flex: 1 }}>Step 1: Gradual Red</Text>
                    <Text style={{ color: Colors.textMuted }}>65%</Text>
                 </View>
                 <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                    <View style={{ width: 12, height: 12, borderRadius: 6, backgroundColor: '#0000FF', marginRight: 12 }} />
                    <Text style={{ color: '#FFF', flex: 1 }}>Step 2: Jumping Blue</Text>
                    <Text style={{ color: Colors.textMuted }}>40%</Text>
                 </View>
              </View>

              <TouchableOpacity 
                style={[styles.presetCard, { borderLeftColor: Colors.primary }]}
                onPress={() => {
                  if (writeToDevice) {
                    const steps = [
                      { mode: 1, speed: 60, color1: { r: 255, g: 0, b: 0 }, color2: { r: 0, g: 0, b: 0 } },
                      { mode: 2, speed: 40, color1: { r: 0, g: 0, b: 255 }, color2: { r: 0, g: 0, b: 0 } }
                    ];
                    writeToDevice(ZenggeProtocol.setCustomMode(steps));
                  }
                }}
              >
                <Text style={styles.presetTitle}>Save & Sync DIY</Text>
                <Text style={styles.presetDesc}>Flash this sequence to the controller's permanent memory.</Text>
              </TouchableOpacity>
            </View>
          )}
          {/* UNIVERSAL SLIDERS SECTION */}
          {activeMode !== 'CAMERA' && activeMode !== 'CUSTOM' && activeMode !== 'PRESETS' && (
            <View style={[styles.sceneSlidersContainer, { marginTop: 8 }]}>
              {/* Color Grid - Hidden in Programs Mode */}
              {activeMode !== 'RBM' && (
                <View style={[styles.colorGrid, { marginBottom: 8 }]}>
                  {['#FF0000', '#FFFF00', '#00FF00', '#00FFFF', '#0000FF', '#FF00FF', '#FFFFFF'].map(color => (
                    <TouchableOpacity 
                      key={color} 
                      onPress={() => {
                        const hueMap: {[key: string]: number} = {
                          '#FF0000': 0, '#FFFF00': 60, '#00FF00': 120, 
                          '#00FFFF': 180, '#0000FF': 240, '#FF00FF': 300, '#FFFFFF': 0
                        };
                        if (activeMode === 'FIXED') {
                          if (fixedColorMode === 'FOREGROUND') setFixedFgColor(color);
                          else setFixedBgColor(color);
                          if (hueMap[color] !== undefined) setFixedHue(hueMap[color]);
                        } else if (activeMode === 'MUSIC') {
                          if (hueMap[color] !== undefined) setMusicHue(hueMap[color]);
                        } else {
                          setSelectedColor(color);
                          if (hueMap[color] !== undefined) setSelectedHue(hueMap[color]);
                        }

                        if (writeToDevice) {
                          if (activeMode === 'MUSIC') {
                            handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, hueMap[color] || 0);
                          } else {
                            const r = parseInt(color.slice(1, 3), 16);
                            const g = parseInt(color.slice(3, 5), 16);
                            const b = parseInt(color.slice(5, 7), 16);
                            writeToDevice(ZenggeProtocol.setColor(r, g, b));
                          }
                        }
                      }}
                      style={[
                        styles.colorButton, 
                        { backgroundColor: color, flex: 1, marginHorizontal: 2, height: 40, borderRadius: 4 },
                        ((activeMode === 'FIXED' ? (fixedColorMode === 'FOREGROUND' ? fixedFgColor : fixedBgColor) : selectedColor) === color) && styles.selectedColorButton
                      ]} 
                    />
                  ))}
                </View>
              )}

              {/* Hue Slider - Hidden in Programs Mode */}
              {activeMode !== 'RBM' && (
                <View style={[styles.controlRow, { marginTop: 0, height: 32 }]}>
                  <CustomSlider 
                    gradientTrack={true}
                    value={activeMode === 'FIXED' ? fixedHue : (activeMode === 'MUSIC' ? musicHue : selectedHue)}
                    onValueChange={(hue) => {
                      if (activeMode === 'FIXED') {
                        setFixedHue(hue);
                        const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                        const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0")).join("");
                        const hex = rgb2hex(f(5), f(3), f(1));
                        if (fixedColorMode === 'FOREGROUND') setFixedFgColor(hex);
                        else setFixedBgColor(hex);
                      } else if (activeMode === 'MUSIC') {
                        setMusicHue(hue);
                      } else {
                        setSelectedHue(hue);
                        const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                        const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0").toUpperCase()).join("");
                        setSelectedColor(rgb2hex(f(5), f(3), f(1)));
                      }
                    }}
                    onSlidingComplete={(hue) => {
                      if (activeMode === 'MUSIC') {
                        handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, hue);
                      } else if (writeToDevice) {
                        const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                        const r = Math.round(f(5) * 255);
                        const g = Math.round(f(3) * 255);
                        const b = Math.round(f(1) * 255);
                        writeToDevice(ZenggeProtocol.setColor(r, g, b));
                      }
                    }}
                    minimumValue={0}
                    maximumValue={360}
                    style={{ position: 'absolute', width: '100%', height: 32 }}
                  />
                </View>
              )}

              {/* Brightness / Sensitivity Slider */}
              <View style={[styles.controlRow, { marginTop: 24 }]}>
                <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center' }}>
                  <Text style={{ color: Colors.textMuted, marginRight: 8, fontSize: 16 }}>
                    {activeMode === 'MUSIC' && musicSetting === 'SENSITIVITY' ? '🎚️' : '☀️'}
                  </Text>
                  
                  {activeMode === 'MUSIC' && (
                    <View style={{ flexDirection: 'row', position: 'absolute', top: -35, left: 24 }}>
                      <TouchableOpacity style={{ marginRight: 16 }} onPress={() => setMusicSetting('SENSITIVITY')}>
                        <Text style={{ color: musicSetting === 'SENSITIVITY' ? Colors.primary : Colors.textMuted, fontSize: 12, fontWeight: 'bold' }}>SENSITIVITY</Text>
                      </TouchableOpacity>
                      <TouchableOpacity onPress={() => setMusicSetting('BRIGHTNESS')}>
                        <Text style={{ color: musicSetting === 'BRIGHTNESS' ? Colors.primary : Colors.textMuted, fontSize: 12, fontWeight: 'bold' }}>BRIGHTNESS</Text>
                      </TouchableOpacity>
                    </View>
                  )}
                  
                  {activeMode !== 'MUSIC' && (
                    <Text style={{ position: 'absolute', top: -20, left: 24, fontSize: 10, color: Colors.textMuted, fontWeight: 'bold', letterSpacing: 1 }}>BRIGHTNESS</Text>
                  )}

                  <CustomSlider 
                    value={activeMode === 'MUSIC' && musicSetting === 'SENSITIVITY' ? micSensitivity : brightness}
                    onValueChange={activeMode === 'MUSIC' && musicSetting === 'SENSITIVITY' ? setMicSensitivity : setBrightness}
                    minimumValue={0}
                    maximumValue={100}
                    style={{ flex: 1 }}
                    onSlidingComplete={(val) => {
                      if (writeToDevice) {
                        if (activeMode === 'RBM') {
                          writeToDevice(ZenggeProtocol.setRbmMode(selectedPatternId, speed, val));
                        } else if (activeMode === 'MUSIC') {
                          if (musicSetting === 'SENSITIVITY') {
                            handleMusicChange(musicPatternId, val, brightness, micSource, musicHue);
                          } else {
                            handleMusicChange(musicPatternId, micSensitivity, val, micSource, musicHue);
                          }
                        } else {
                          // Support brightness for Fixed, Presets, Multicolor via RGB scaling
                          const factor = val / 100;
                          const fgHex = activeMode === 'FIXED' ? fixedFgColor : (activeMode === 'MULTICOLOR' ? '#FFFFFF' : selectedColor);
                          const bgHex = activeMode === 'FIXED' ? fixedBgColor : '#000000';
                          
                          const fg = { 
                            r: Math.round(parseInt(fgHex.slice(1, 3), 16) * factor), 
                            g: Math.round(parseInt(fgHex.slice(3, 5), 16) * factor), 
                            b: Math.round(parseInt(fgHex.slice(5, 7), 16) * factor) 
                          };
                          const bg = { 
                            r: Math.round(parseInt(bgHex.slice(1, 3), 16) * factor), 
                            g: Math.round(parseInt(bgHex.slice(3, 5), 16) * factor), 
                            b: Math.round(parseInt(bgHex.slice(5, 7), 16) * factor) 
                          };

                          if (activeMode === 'FIXED' && fixedPatternId !== 1) {
                            if (fixedPatternId === 6) {
                              writeToDevice(ZenggeProtocol.setCustomMode([{ mode: 1, speed, color1: fg, color2: bg }, { mode: 1, speed, color1: bg, color2: fg }]));
                            } else if (fixedPatternId === 7) {
                              writeToDevice(ZenggeProtocol.setCustomMode([{ mode: 2, speed, color1: fg, color2: bg }, { mode: 2, speed, color1: bg, color2: fg }]));
                            } else {
                              let arr: any[] = [];
                              if (fixedPatternId === 2) arr = [fg, bg, bg, bg, bg, bg, bg, bg];
                              if (fixedPatternId === 3) arr = [fg, {r: Math.floor(fg.r*0.5), g: Math.floor(fg.g*0.5), b: Math.floor(fg.b*0.5)}, {r: Math.floor(fg.r*0.2), g: Math.floor(fg.g*0.2), b: Math.floor(fg.b*0.2)}, bg, bg, bg];
                              if (fixedPatternId === 4) arr = [fg, fg, fg, fg, bg, bg, bg, bg];
                              if (fixedPatternId === 5) arr = [fg, fg, bg, bg];
                              writeToDevice(ZenggeProtocol.setMultiColor(arr, speed, 1));
                            }
                          } else {
                            writeToDevice(ZenggeProtocol.setColor(fg.r, fg.g, fg.b));
                          }
                        }
                      }
                    }}
                  />
                  <Text style={{ color: Colors.text, marginLeft: 10, width: 45, fontWeight: 'bold', fontSize: 13, textAlign: 'right' }}>
                    {Math.round(activeMode === 'MUSIC' && musicSetting === 'SENSITIVITY' ? micSensitivity : brightness)}%
                  </Text>
                  <Text style={{ color: Colors.textMuted, marginLeft: 8, fontSize: 16 }}>
                    {activeMode === 'MUSIC' && musicSetting === 'SENSITIVITY' ? '🎚️' : '☀️'}
                  </Text>
                </View>
              </View>

              {/* Speed Slider */}
              {activeMode !== 'MUSIC' && (
                <View style={[styles.controlRow, { marginTop: 12 }]}>
                  <Text style={{ position: 'absolute', top: -14, left: 24, fontSize: 10, color: Colors.textMuted, fontWeight: 'bold', letterSpacing: 1 }}>SPEED</Text>
                  <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center' }}>
                    <Text style={{ color: Colors.textMuted, marginRight: 8, fontSize: 16 }}>🚀</Text>
                    <CustomSlider 
                      value={speed}
                      onValueChange={setSpeed}
                      onSlidingComplete={(val) => {
                        if (writeToDevice) {
                          if (activeMode === 'FIXED') {
                            const fgHex = fixedColorMode === 'FOREGROUND' ? fixedFgColor : (fixedFgColor || '#00FF00');
                            const bgHex = fixedColorMode === 'BACKGROUND' ? fixedBgColor : (fixedBgColor || '#000000');
                            const fg = { r: parseInt(fgHex.slice(1, 3), 16), g: parseInt(fgHex.slice(3, 5), 16), b: parseInt(fgHex.slice(5, 7), 16) };
                            const bg = { r: parseInt(bgHex.slice(1, 3), 16), g: parseInt(bgHex.slice(3, 5), 16), b: parseInt(bgHex.slice(5, 7), 16) };
                            
                            if (fixedPatternId === 1) {
                              writeToDevice(ZenggeProtocol.setColor(fg.r, fg.g, fg.b));
                            } else if (fixedPatternId === 6) { 
                              const steps = [{ mode: 1, speed: val, color1: fg, color2: bg }, { mode: 1, speed: val, color1: bg, color2: fg }];
                              writeToDevice(ZenggeProtocol.setCustomMode(steps));
                            } else if (fixedPatternId === 7) { 
                              const steps = [{ mode: 2, speed: val, color1: fg, color2: bg }, { mode: 2, speed: val, color1: bg, color2: fg }];
                              writeToDevice(ZenggeProtocol.setCustomMode(steps));
                            } else { 
                              let arr: any[] = [];
                              if (fixedPatternId === 2) arr = [fg, bg, bg, bg, bg, bg, bg, bg];
                              if (fixedPatternId === 3) arr = [fg, {r: Math.floor(fg.r*0.5), g: Math.floor(fg.g*0.5), b: Math.floor(fg.b*0.5)}, {r: Math.floor(fg.r*0.2), g: Math.floor(fg.g*0.2), b: Math.floor(fg.b*0.2)}, bg, bg, bg];
                              if (fixedPatternId === 4) arr = [fg, fg, fg, fg, bg, bg, bg, bg];
                              if (fixedPatternId === 5) arr = [fg, fg, bg, bg];
                              writeToDevice(ZenggeProtocol.setMultiColor(arr, val, 1));
                            }
                          } else if (activeMode === 'RBM') {
                            writeToDevice(ZenggeProtocol.setRbmMode(selectedPatternId, val, brightness));
                          } else if (activeMode === 'MULTICOLOR') {
                            const segmentColors = [
                              { r: 255, g: 0, b: 0 },
                              { r: 0, g: 255, b: 0 },
                              { r: 0, g: 0, b: 255 },
                              { r: 255, g: 255, b: 0 },
                              { r: 0, g: 255, b: 255 }
                            ];
                            writeToDevice(ZenggeProtocol.setMultiColor(segmentColors, val, 1));
                          }
                        }
                      }}
                      minimumValue={0}
                      maximumValue={100}
                      style={{ flex: 1 }}
                    />
                    <Text style={{ color: Colors.text, marginLeft: 10, width: 45, fontWeight: 'bold', fontSize: 13, textAlign: 'right' }}>
                      {Math.round(speed)}
                    </Text>
                  </View>
                </View>
              )}
            </View>
          )}

        </View>

      </View>
    </View>
  );
}

const createStyles = (Colors: import('../theme/theme').ThemePalette) => StyleSheet.create({
  container: {
    paddingHorizontal: 0,
    paddingBottom: 20,
    paddingTop: 0,
  },
  visualizerWrapper: {
    width: '100%',
    alignItems: 'stretch',
    marginVertical: 2,
  },
  tabContainer: {
    flexDirection: 'row',
    backgroundColor: Colors.surfaceHighlight,
    borderRadius: Layout.borderRadius,
    padding: 6,
    marginBottom: 8,
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.08)',
  },
  tab: {
    flex: 1,
    paddingVertical: 8,
    alignItems: 'center',
    borderRadius: Layout.borderRadius - 6,
    overflow: 'hidden',
  },
  activeTab: {
    backgroundColor: 'transparent',
    borderWidth: 0,
  },
  tabText: {
    ...Typography.body,
    color: Colors.textMuted,
    fontWeight: '800',
    letterSpacing: 1,
    zIndex: 2,
  },
  activeTabText: {
    color: Colors.isDark ? '#FFF' : Colors.accent,
  },
  controlsContainer: {
    padding: 10,
    backgroundColor: Colors.isDark ? 'rgba(21, 25, 40, 0.7)' : Colors.surface,
    borderRadius: Layout.borderRadius + 4,
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255, 255, 255, 0.1)' : 'rgba(0,0,0,0.08)',
  },
  modesScroll: {
    flexDirection: 'row',
    marginBottom: 4,
  },
  modePill: {
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: Layout.borderRadius,
    backgroundColor: Colors.background,
    marginRight: 12,
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255, 255, 255, 0.05)' : 'rgba(0,0,0,0.08)',
    overflow: 'hidden',
    justifyContent: 'center',
  },
  activeModePill: {
    backgroundColor: 'transparent',
    borderColor: 'transparent',
  },
  modePillText: {
    color: Colors.textMuted,
    fontWeight: '600',
  },
  activeModePillText: {
    color: Colors.isDark ? Colors.background : Colors.surface,
    fontWeight: 'bold',
  },
  activeModeContainer: {
    minHeight: 120,
  },
  controlRow: {
    marginTop: 6,
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
    borderColor: Colors.isDark ? 'rgba(255,255,255,0.2)' : 'rgba(0,0,0,0.15)',
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
    color: Colors.textMuted,
  },
  sceneContainer: {
    backgroundColor: Colors.isDark ? '#050505' : Colors.surfaceHighlight,
    borderRadius: 24,
    padding: 2,
    marginTop: 8,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.08)',
  },
  sceneHeader: {
    padding: 16,
    borderBottomWidth: 1,
    borderBottomColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.08)',
    alignItems: 'center',
  },
  sceneTitle: {
    ...Typography.title,
    color: Colors.text,
    fontSize: 18,
  },
  rbmWheelSection: {
    height: 180,
    backgroundColor: Colors.isDark ? '#050505' : Colors.surfaceHighlight,
    justifyContent: 'center',
    alignItems: 'center',
  },
  sceneSlidersContainer: {
    padding: 16,
    backgroundColor: Colors.isDark ? 'rgba(255,255,255,0.02)' : 'rgba(0,0,0,0.02)',
  },
  sceneLabel: {
    ...Typography.caption,
    color: Colors.textMuted,
    fontSize: 12,
    textTransform: 'uppercase',
    letterSpacing: 1,
    fontWeight: '700',
  },
  musicToggleHeader: {
    flexDirection: 'row',
    backgroundColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : Colors.surfaceHighlight,
    borderRadius: 25,
    padding: 4,
    alignItems: 'center',
    margin: 12,
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255,255,255,0.1)' : 'rgba(0,0,0,0.08)',
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
    color: Colors.textMuted,
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
    backgroundColor: Colors.isDark ? 'rgba(0,0,0,0.6)' : Colors.surfaceHighlight,
  },
  musicModeNumber: {
    color: Colors.text,
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
    marginBottom: 8,
  },
  micControlSection: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 15,
    marginBottom: 4,
  },
  micIconBtn: {
    alignItems: 'center',
  },
  micIconText: {
    fontSize: 24,
    color: Colors.textMuted,
  },
  micSubText: {
    fontSize: 10,
    color: Colors.textMuted,
    marginTop: 4,
    textTransform: 'uppercase',
    fontWeight: '600',
  },
  playButtonMain: {
    width: 32,
    height: 32,
    borderRadius: 16,
    borderWidth: 2,
    borderColor: Colors.primary,
    alignItems: 'center',
    justifyContent: 'center',
  },
  playIconInner: {
    width: 24,
    height: 24,
    borderRadius: 12,
    backgroundColor: Colors.primary,
    alignItems: 'center',
    justifyContent: 'center',
  },
  musicOptionsGrid: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    paddingHorizontal: 10,
    marginBottom: 4,
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
    borderColor: Colors.textMuted,
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
    color: Colors.text,
    fontSize: 13,
    fontWeight: '600',
  },
  gradientSliderTrack: {
    height: 6,
    borderRadius: 3,
    backgroundColor: Colors.surfaceHighlight,
    overflow: 'hidden',
  },
  musicSettingsToggleRow: {
    flexDirection: 'row',
    justifyContent: 'flex-start',
    gap: 30,
    marginBottom: 4,
  }
});
