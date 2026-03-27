import React, { useState, useEffect } from 'react';
import { View, Text, ScrollView, TouchableOpacity, Dimensions } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Typography, Layout } from '../theme/theme';
import CustomSlider from './CustomSlider';
import ProductVisualizer from './ProductVisualizer';
import ArcPatternWheel from './ArcPatternWheel';
import CameraTracker from './CameraTracker';
import { getRbmPatternName } from '../constants/RbmPatterns';
import { MusicDictionary } from '../utils/MusicDictionary';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import AsyncStorage from '@react-native-async-storage/async-storage';

interface ModernControllerProps {
  lockedProduct?: string;
  isPaired?: boolean;
  points?: number;
  devices: any[];
  onLongPressDevice?: (device: any) => void;
  writeToDevice: (data: number[]) => void;
  isPoweredOn?: boolean;
}

const { width: SCREEN_WIDTH } = Dimensions.get('window');

type TabRoute = 'COLORS' | 'MUSIC' | 'SCENES' | 'CAMERA';

export default function ModernController({
  lockedProduct,
  isPaired,
  points,
  onLongPressDevice,
  devices,
  writeToDevice,
  isPoweredOn = true,
}: ModernControllerProps) {
  // -- Master Navigation --
  const [activeTab, setActiveTab] = useState<TabRoute>('COLORS');

  // -- Master Variables --
  const [brightness, setBrightness] = useState<number>(100);
  const [speed, setSpeed] = useState<number>(50);

  // -- MUSIC Tab State --
  const [sensitivity, setSensitivity] = useState(80);
  const [activePattern, setActivePattern] = useState<number>(1); 

  // -- COLORS Tab State --
  const [selectedColor, setSelectedColor] = useState<string>('#00D4FF');
  const [selectedHue, setSelectedHue] = useState<number>(180);
  const [isCandleMode, setIsCandleMode] = useState<boolean>(false);
  const [candleAmplitude, setCandleAmplitude] = useState<number>(2);
  const [favorites, setFavorites] = useState<any[]>([]);

  // NeoGleamz Modern Neon Dark Palette
  const NeoColors = {
    bg: '#0B0914',
    cardLight: 'rgba(255,255,255,0.03)',
    cardBorder: 'rgba(255,255,255,0.08)',
    text: '#FFFFFF',
    textMuted: '#888899',
    accentOrange: '#FF5500',
    accentBlue: '#00D4FF',
  };

  const tabs: { key: TabRoute; icon: keyof typeof MaterialCommunityIcons.glyphMap; label: string }[] = [
    { key: 'COLORS', icon: 'palette-swatch', label: 'COLORS' },
    { key: 'MUSIC', icon: 'music-note-plus', label: 'MUSIC' },
    { key: 'SCENES', icon: 'animation-play', label: 'SCENES' },
    { key: 'CAMERA', icon: 'camera-iris', label: 'CAMERA' },
  ];

  // --- NATIVE PAYLOAD DISPATCHERS ---

  const sendSolidColor = (hex: string = selectedColor, currentBright: number = brightness) => {
    setIsCandleMode(false);
    const factor = currentBright / 100;
    const r = Math.round(parseInt(hex.slice(1, 3), 16) * factor) || 0;
    const g = Math.round(parseInt(hex.slice(3, 5), 16) * factor) || 0;
    const b = Math.round(parseInt(hex.slice(5, 7), 16) * factor) || 0;
    
    // Send solid multi-color payload format length=10
    const colors = Array(10).fill({ r, g, b });
    const payload = ZenggeProtocol.setMultiColor(colors, 100, 1, 1);
    devices.forEach(d => writeToDevice(payload));
  };

  const sendCandleMode = (hex: string = selectedColor, currentBright: number = brightness, amp: number = candleAmplitude) => {
    setIsCandleMode(true);
    const factor = currentBright / 100;
    const rawR = Math.round(parseInt(hex.slice(1, 3), 16) * factor) || 0;
    const rawG = Math.round(parseInt(hex.slice(3, 5), 16) * factor) || 0;
    const rawB = Math.round(parseInt(hex.slice(5, 7), 16) * factor) || 0;
    
    // Assume GRB standard for simplified protocol handling on magic controller
    const payload = ZenggeProtocol.setCandleMode(rawG, rawR, rawB, speed, currentBright, amp);
    devices.forEach(d => writeToDevice(payload));
  };

  const handleMusicModeSelect = (id: number, currentSens: number = sensitivity) => {
     setActivePattern(id);
     const payload = ZenggeProtocol.setMusicConfig(
         true, 0x27, id, 
         {r: 255, g: 0, b: 255}, {r: 0, g: 255, b: 255},   
         Math.round(currentSens), brightness 
     );
     devices.forEach(d => writeToDevice(payload));
  };

  // --- EVENT HANDLERS ---

  const handleHueSelection = (hue: number) => {
    setSelectedHue(hue);
    const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
    const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0").toUpperCase()).join("");
    const hex = rgb2hex(f(5), f(3), f(1));
    setSelectedColor(hex);
  };

  const handleHueComplete = (hue: number) => {
    handleHueSelection(hue);
    if (isCandleMode) sendCandleMode(selectedColor, brightness, candleAmplitude);
    else sendSolidColor(selectedColor, brightness);
  };

  const setQuickColor = (hex: string, hueTarget: number) => {
     setSelectedColor(hex);
     setSelectedHue(hueTarget);
     if (isCandleMode) sendCandleMode(hex, brightness, candleAmplitude);
     else sendSolidColor(hex, brightness);
  };

  // Extract visualizer aesthetic safely
  const getIconForArchetype = (archetype: string) => {
      switch (archetype) {
          case 'WAVE': return 'wave';
          case 'PULSE': return 'heart-pulse';
          case 'VU_METER': return 'equalizer';
          case 'STROBE': return 'flash';
          default: return 'music';
      }
  };

  return (
    <View style={{ flex: 1, backgroundColor: NeoColors.bg }}>

      {/* Main Hardware Visualizer Viewport */}
      <View style={{ height: 240, justifyContent: 'center', alignItems: 'center', marginTop: 10 }}>
        <ProductVisualizer 
          product={(lockedProduct as 'HALOZ' | 'SOULZ') || 'HALOZ'} 
          color={activeTab === 'COLORS' ? selectedColor : NeoColors.accentBlue}
          mode={activeTab === 'COLORS' ? (isCandleMode ? 'CANDLE' : 'FIXED') : 'FIXED'}
          patternId={activePattern}
          isPaired={isPaired} 
          points={points || 16} 
          devices={devices}
          onLongPressDevice={onLongPressDevice}
          isPoweredOn={isPoweredOn}
          statusText={`${activeTab} MODE`}
        />
      </View>

      <View style={{ 
          flexDirection: 'row', 
          marginHorizontal: 16, 
          marginTop: 16, 
          backgroundColor: 'rgba(255,255,255,0.02)',
          borderRadius: 24,
          padding: 4,
          borderWidth: 1,
          borderColor: 'rgba(255,255,255,0.05)'
      }}>
        {tabs.map(tab => {
          const isActive = activeTab === tab.key;
          return (
            <TouchableOpacity 
              key={tab.key}
              style={{
                flex: 1,
                alignItems: 'center',
                paddingVertical: 12,
                backgroundColor: isActive ? 'rgba(255,85,0,0.15)' : 'transparent',
                borderRadius: 20,
              }}
              onPress={() => setActiveTab(tab.key)}
            >
              <MaterialCommunityIcons 
                name={tab.icon} 
                size={22} 
                color={isActive ? NeoColors.accentOrange : NeoColors.textMuted} 
              />
              <Text style={[Typography.caption, { 
                marginTop: 4, 
                fontSize: 9,
                letterSpacing: 0.5,
                color: isActive ? NeoColors.accentOrange : NeoColors.textMuted,
                fontWeight: isActive ? 'bold' : 'normal'
              }]}>
                {tab.label}
              </Text>
            </TouchableOpacity>
          );
        })}
      </View>

      <ScrollView contentContainerStyle={{ padding: 16, paddingBottom: 160 }}>
        
        {/* COLORS TAB */}
        {activeTab === 'COLORS' && (
           <View>
              {/* Quick Pick Grid */}
              <Text style={[Typography.title, { color: NeoColors.text, marginBottom: 12, letterSpacing: 1, fontSize: 13 }]}>
                 QUICK PALETTE
              </Text>
              <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginBottom: 24 }}>
                 {[{ c: '#FF0000', h: 0 }, { c: '#FF5500', h: 30 }, { c: '#FFFF00', h: 60 }, { c: '#00FF00', h: 120 }, { c: '#00D4FF', h: 180 }, { c: '#0000FF', h: 240 }, { c: '#FF00FF', h: 300 }, { c: '#FFFFFF', h: 0 }].map((item, idx) => (
                    <TouchableOpacity
                       key={idx}
                       onPress={() => setQuickColor(item.c, item.h)}
                       style={{
                          width: 36, height: 36, borderRadius: 18,
                          backgroundColor: item.c,
                          borderWidth: 2,
                          borderColor: selectedColor === item.c ? '#FFF' : 'transparent',
                          shadowColor: item.c,
                          shadowOpacity: selectedColor === item.c ? 0.8 : 0.2,
                          shadowRadius: 6, elevation: 4
                       }}
                    />
                 ))}
              </View>

              {/* Advanced Hue Gradient Slider */}
              <View style={{ backgroundColor: NeoColors.cardLight, borderRadius: 24, padding: 20, borderWidth: 1, borderColor: NeoColors.cardBorder, marginBottom: 24 }}>
                 <Text style={[Typography.title, { color: NeoColors.text, marginBottom: 16 }]}>HUE SELECTOR</Text>
                 <View style={{ height: 36, justifyContent: 'center' }}>
                   <CustomSlider 
                      gradientTrack={true}
                      value={selectedHue}
                      onValueChange={handleHueSelection}
                      onSlidingComplete={handleHueComplete}
                      minimumValue={0}
                      maximumValue={360}
                      style={{ position: 'absolute', width: '100%' }}
                   />
                 </View>
              </View>

              {/* Ambient Candle Mode Toggle */}
              <TouchableOpacity
                 activeOpacity={0.8}
                 onPress={() => {
                    const nextMode = !isCandleMode;
                    setIsCandleMode(nextMode);
                    if (nextMode) sendCandleMode(selectedColor, brightness, candleAmplitude);
                    else sendSolidColor(selectedColor, brightness);
                 }}
                 style={{
                    backgroundColor: isCandleMode ? 'rgba(255,85,0,0.1)' : NeoColors.cardLight,
                    borderColor: isCandleMode ? NeoColors.accentOrange : NeoColors.cardBorder,
                    borderWidth: 1, borderRadius: 20, padding: 16,
                    flexDirection: 'row', alignItems: 'center'
                 }}
              >
                 <View style={{ width: 44, height: 44, borderRadius: 22, backgroundColor: isCandleMode ? NeoColors.accentOrange : 'rgba(255,255,255,0.05)', justifyContent: 'center', alignItems: 'center', marginRight: 16 }}>
                    <MaterialCommunityIcons name="fire" size={24} color={isCandleMode ? '#000' : NeoColors.textMuted} />
                 </View>
                 <View style={{ flex: 1 }}>
                    <Text style={[Typography.body, { color: isCandleMode ? NeoColors.accentOrange : NeoColors.text, fontWeight: 'bold' }]}>Candle Ambient Mode</Text>
                    <Text style={[Typography.caption, { color: NeoColors.textMuted }]}>Hardware volume flicker emulation</Text>
                 </View>
                 <MaterialCommunityIcons name={isCandleMode ? "toggle-switch" : "toggle-switch-off-outline"} size={40} color={isCandleMode ? NeoColors.accentOrange : NeoColors.textMuted} />
              </TouchableOpacity>
           </View>
        )}

        {/* MUSIC TAB */}
        {activeTab === 'MUSIC' && (
          <View>
            <Text style={[Typography.title, { color: NeoColors.text, marginBottom: 16, letterSpacing: 1 }]}>
               SYNC PATTERNS
            </Text>

            <View style={{ flexDirection: 'row', flexWrap: 'wrap', justifyContent: 'space-between' }}>
               {Object.values(MusicDictionary).map((mode) => {
                   const isActive = activePattern === mode.id;
                   return (
                       <TouchableOpacity
                          key={mode.id}
                          activeOpacity={0.7}
                          onPress={() => handleMusicModeSelect(mode.id)}
                          style={{
                              width: '48%',
                              backgroundColor: isActive ? 'rgba(0, 212, 255, 0.1)' : NeoColors.cardLight,
                              borderColor: isActive ? NeoColors.accentBlue : NeoColors.cardBorder,
                              borderWidth: isActive ? 1.5 : 1,
                              borderRadius: 16,
                              padding: 16,
                              marginBottom: 16,
                              alignItems: 'flex-start',
                              shadowColor: isActive ? NeoColors.accentBlue : '#000',
                              shadowOffset: { width: 0, height: 4 },
                              shadowOpacity: isActive ? 0.3 : 0.1,
                              shadowRadius: 8,
                              elevation: isActive ? 8 : 2
                          }}
                       >
                          <View style={{ 
                              width: 40, 
                              height: 40, 
                              borderRadius: 20, 
                              backgroundColor: isActive ? NeoColors.accentBlue : 'rgba(255,255,255,0.05)',
                              alignItems: 'center',
                              justifyContent: 'center',
                              marginBottom: 12
                          }}>
                              <MaterialCommunityIcons 
                                  name={getIconForArchetype(mode.archetype) as any} 
                                  size={24} 
                                  color={isActive ? '#000' : NeoColors.text} 
                              />
                          </View>
                          <Text style={[Typography.body, { color: isActive ? NeoColors.text : '#CCC', fontWeight: 'bold' }]}>{mode.name}</Text>
                          <Text style={[Typography.caption, { color: isActive ? NeoColors.accentBlue : NeoColors.textMuted, marginTop: 4, fontSize: 10 }]}>{mode.archetype}</Text>
                       </TouchableOpacity>
                   )
               })}
            </View>

            <View style={{ marginTop: 24, padding: 20, backgroundColor: NeoColors.cardLight, borderRadius: 24, borderWidth: 1, borderColor: NeoColors.cardBorder }}>
               <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
                 <Text style={[Typography.title, { color: NeoColors.text }]}>MIC SENSITIVITY</Text>
                 <Text style={{ color: NeoColors.accentOrange, fontWeight: '900', fontSize: 16 }}>{Math.round(sensitivity)}%</Text>
               </View>
               <CustomSlider 
                  value={sensitivity}
                  onValueChange={setSensitivity}
                  onSlidingComplete={(val) => handleMusicModeSelect(activePattern, val)}
                  minimumValue={0}
                  maximumValue={100}
               />
            </View>
          </View>
        )}

        {/* SCENES TAB */}
        {activeTab === 'SCENES' && (
          <View>
             <Text style={[Typography.title, { color: NeoColors.text, marginBottom: 16, letterSpacing: 1 }]}>
               ANIMATION PROGRAMS
             </Text>
             
             <View style={{ marginBottom: 24, paddingVertical: 10, backgroundColor: NeoColors.cardLight, borderRadius: 24, borderWidth: 1, borderColor: NeoColors.cardBorder }}>
                <ArcPatternWheel 
                  value={activePattern}
                  onValueChange={(pid) => {
                    setActivePattern(pid);
                    const payload = pid === 400 
                        ? ZenggeProtocol.setMultiColor(Array(10).fill({ r: 255, g: 0, b: 0 }), speed, 1, 0)
                        : ZenggeProtocol.setCustomRbm(pid, speed, brightness);
                    devices.forEach(d => writeToDevice(payload));
                  }}
                  min={1}
                  max={302}
                  itemLabel={(val) => getRbmPatternName(val)}
                />
             </View>

             <Text style={[Typography.title, { color: NeoColors.text, marginBottom: 16, letterSpacing: 1 }]}>
               MULTI-COLOR ARRAYS
             </Text>
             <ScrollView horizontal showsHorizontalScrollIndicator={false} style={{ marginBottom: 24 }}>
                {[
                  { name: 'Rainbow', colors: ['#FF0000', '#FF7F00', '#FFFF00', '#00FF00', '#0000FF', '#4B0082', '#9400D3'], type: 3 },
                  { name: 'America', colors: ['#FF0000', '#FFFFFF', '#0000FF'], type: 3 },
                  { name: 'Cyberpunk', colors: ['#00FFFF', '#FF00FF', '#FFFF00'], type: 3 },
                  { name: 'Forest', colors: ['#00FF00', '#008000', '#228B22', '#32CD32'], type: 1 }
                ].map((preset, idx) => (
                   <TouchableOpacity 
                     key={idx}
                     onPress={() => {
                        const rgbColors = preset.colors.map(h => ({r: parseInt(h.slice(1,3),16)||0, g: parseInt(h.slice(3,5),16)||0, b: parseInt(h.slice(5,7),16)||0}));
                        const payload = ZenggeProtocol.setMultiColor(rgbColors, 16, preset.type, speed);
                        devices.forEach(d => writeToDevice(payload));
                     }}
                     style={{
                        paddingVertical: 12, paddingHorizontal: 20, 
                        backgroundColor: NeoColors.cardLight, 
                        borderRadius: 16, marginRight: 12, borderWidth: 1, borderColor: NeoColors.cardBorder
                     }}
                   >
                     <Text style={[Typography.body, { color: NeoColors.text, fontWeight: 'bold' }]}>{preset.name}</Text>
                   </TouchableOpacity>
                ))}
             </ScrollView>
          </View>
        )}

        {/* CAMERA TAB */}
        {activeTab === 'CAMERA' && (
          <View style={{ padding: 8, backgroundColor: 'transparent', borderRadius: 24, overflow: 'hidden' }}>
             <CameraTracker 
                isActive={activeTab === 'CAMERA'} 
                onColorDetected={(hex) => {
                   setSelectedColor(hex);
                   sendSolidColor(hex, brightness);
                }} 
             />
          </View>
        )}
      </ScrollView>

      {/* UNIVERSAL MASTER SLIDERS BLOCK */}
      {(activeTab === 'COLORS' || activeTab === 'SCENES') && (
        <View style={{ 
           position: 'absolute', bottom: 0, left: 0, right: 0, 
           backgroundColor: 'rgba(11,9,20,0.95)', borderTopWidth: 1, borderTopColor: NeoColors.cardBorder,
           padding: 20, paddingBottom: 40
        }}>
           <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 16 }}>
              <MaterialCommunityIcons name="brightness-6" size={20} color={NeoColors.textMuted} style={{ marginRight: 12 }} />
              <CustomSlider 
                 value={brightness} onValueChange={setBrightness}
                 onSlidingComplete={(val) => {
                    if (activeTab === 'COLORS') {
                       if (isCandleMode) sendCandleMode(selectedColor, val, candleAmplitude);
                       else sendSolidColor(selectedColor, val);
                    }
                 }}
                 minimumValue={0} maximumValue={100} style={{ flex: 1 }}
              />
              <Text style={{ color: '#FFF', width: 44, textAlign: 'right', fontWeight: 'bold' }}>{Math.round(brightness)}%</Text>
           </View>

           <View style={{ flexDirection: 'row', alignItems: 'center' }}>
              <MaterialCommunityIcons name="speedometer" size={20} color={NeoColors.textMuted} style={{ marginRight: 12 }} />
              <CustomSlider 
                 value={speed} onValueChange={setSpeed}
                 onSlidingComplete={(val) => {
                    if (activeTab === 'COLORS' && isCandleMode) {
                        sendCandleMode(selectedColor, brightness, candleAmplitude); // Speed adjusts candle flicker natively via state propagation
                    }
                 }}
                 minimumValue={0} maximumValue={100} style={{ flex: 1 }}
              />
              <Text style={{ color: '#FFF', width: 44, textAlign: 'right', fontWeight: 'bold' }}>{Math.round(speed)}</Text>
           </View>
        </View>
      )}
    </View>
  );
}
