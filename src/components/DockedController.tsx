/**
 * DockedController.tsx — SK8Lytz Primary LED Control Interface
 *
 * The main user-facing control panel for LED mode management.
 * Renders as a bottom sheet overlay on DashboardScreen.
 *
 * Responsibilities:
 *  - Mode switching: Fixed, Music, Camera, Pattern (RBM), Candle, DIY Array
 *  - Color picker, RGB sliders, brightness & speed knobs
 *  - Pattern wheel (ArcPatternWheel / VerticalPatternDrum)
 *  - Music EQ visualizer (SpectrumVisualizer)
 *  - Favorites system and Quick Presets
 *  - Per-device and group analytics telemetry (MODE_CHANGED, PATTERN_CHANGED, COLOR_CHANGED)
 *
 * Depends on: ZenggeProtocol, AppLogger, useBLE (via prop injection), ThemeContext
 * Platform: React Native (Android + Web)
 */
import React, { useState, useRef, useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView, FlatList, Platform, Modal, TextInput, Animated } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { Typography, Layout } from '../theme/theme';
import { Audio } from 'expo-av';
import { Buffer } from 'buffer';
import { useTheme } from '../context/ThemeContext';
import ProductVisualizer from './ProductVisualizer';
import CustomSlider from './CustomSlider';
import VerticalPatternDrum from './VerticalPatternDrum';
import SpectrumVisualizer from './SpectrumVisualizer';
import CircularKnob from './CircularKnob';
import CameraTracker from './CameraTracker';
import { getRbmPatternName } from '../constants/RbmPatterns';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { AppLogger } from '../services/AppLogger';
import { supabase } from '../services/supabaseClient';

const AnimatedIcon = Animated.createAnimatedComponent(MaterialCommunityIcons);

type ProductType = 'HALOZ' | 'SOULZ';
type ModeType = 'PRESETS' | 'FIXED' | 'RBM' | 'MUSIC' | 'CAMERA' | 'CUSTOM' | 'MULTICOLOR' | 'CANDLE';

const MUSIC_PATTERNS = [
  'Soft',
  'Cheerful',
  'Energy',
  'Relax',
  'Passion',
  'Brisk',
  'Rhythm',
  'Rolling',
  'Flicker',
  'Accumulation',
  'Shuttle',
  'Fireworks',
  'Snow'
];

const FixedPatternPreviewRow = ({ baseDots, patternId, speed, points = 16, segments = 1 }: { baseDots: string[], patternId: number, speed: number, points?: number, segments?: number }) => {
  const [offset, setOffset] = React.useState(0);

  // Extend the 8-element static base array explicitly to match physical point counts natively
  const fullArray = React.useMemo(() => {
     const arr: string[] = [];
     
     // Hardware repeats the geometric bounds across the segment boundary safely
     const dotsPerSegment = Math.max(1, Math.floor(points / Math.max(1, segments)));
     
     for (let i = 0; i < points; i++) {
        // Map native segment repetitions safely simulating string data
        const segmentLocalIndex = i % dotsPerSegment;
        arr.push(baseDots[segmentLocalIndex % baseDots.length]);
     }
     return arr;
  }, [baseDots, points, segments]);

  React.useEffect(() => {
    if (patternId === 1) return; 
    const intervalTime = Math.max(30, 200 - (speed * 1.7)); 
    const int = setInterval(() => {
      setOffset(o => (o + 1) % fullArray.length);
    }, intervalTime);
    return () => clearInterval(int);
  }, [fullArray.length, patternId, speed]);

  const displayedDots = React.useMemo(() => {
    if (patternId === 1) return fullArray;
    return [...fullArray.slice(fullArray.length - offset), ...fullArray.slice(0, fullArray.length - offset)];
  }, [fullArray, offset, patternId]);
  
  const dotsPerSegment = Math.max(1, Math.floor(points / Math.max(1, segments)));

  return (
    <View style={{ flex: 1, marginRight: 8, height: 8, overflow: 'hidden' }}>
      <View style={{ flex: 1, flexDirection: 'row', gap: 2 }}>
        {displayedDots.slice(0, 10).map((c, i) => (
          <View 
             key={i} 
             style={{ 
               flex: 1,
               borderRadius: 4, 
               backgroundColor: c
             }} 
          />
        ))}
      </View>
    </View>
  );
};


export interface IDeviceState {
  id: string;
  name: string;
  points?: number;
  segments?: number;
  sorting?: 'RGB' | 'GRB' | 'BRG' | 'RBG' | 'BGR' | 'GBR';
  [key: string]: any; // safe loose fallback for undocumented BLE peripheral keys
}

export interface IFavoriteState {
  id: string;
  name: string;
  customName?: string;
  mode: string;
  color?: string;
  patternId?: number;
  speed: number;
  brightness: number;
  fixedColorMode?: 'FOREGROUND' | 'BACKGROUND';
  fixedFgColor?: string;
  fixedBgColor?: string;
  fixedHue?: number;
  multiColors?: string[];
  multiTransition?: number;
  multiLength?: number;
  candleAmplitude?: number;
  musicPrimaryColor?: string;
  musicSecondaryColor?: string;
  micSensitivity?: number;
  micSource?: 'APP' | 'DEVICE';
  musicMatrixStyle?: number;
}

export interface IQuickPreset {
  name: string;
  colors: string[];
  type: number;
}

interface Sk8lytzControllerProps {
  hwSettings?: any;
  lockedProduct?: ProductType;
  isPaired?: boolean;
  points?: number;
  devices?: IDeviceState[];
  onLongPressDevice?: (device: IDeviceState) => void;
  writeToDevice?: (payload: number[]) => Promise<void>;
  isPoweredOn?: boolean;
  onDisconnect?: () => void;
}

// CURATED_PRESETS logic moved to internal component state for Supabase updating

const MarqueeText = ({ children, style }: any) => {
  const [textWidth, setTextWidth] = useState(0);
  const [containerWidth, setContainerWidth] = useState(0);
  const anim = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    if (textWidth > containerWidth && containerWidth > 0) {
      Animated.loop(
        Animated.sequence([
          Animated.delay(1500),
          Animated.timing(anim, {
            toValue: -(textWidth - containerWidth + 8),
            duration: 18 * (textWidth - containerWidth),
            useNativeDriver: true,
          }),
          Animated.delay(1000),
          Animated.timing(anim, {
            toValue: 0,
            duration: 0,
            useNativeDriver: true,
          })
        ])
      ).start();
    } else {
      anim.setValue(0);
      anim.stopAnimation();
    }
  }, [textWidth, containerWidth]);

  return (
    <View style={{ overflow: 'hidden', width: '100%', alignItems: 'center', marginVertical: 2 }} onLayout={(e) => setContainerWidth(e.nativeEvent.layout.width)}>
      <Animated.Text
        numberOfLines={1}
        onLayout={(e) => setTextWidth(e.nativeEvent.layout.width)}
        style={[style, { transform: [{ translateX: anim }] }]}
      >
        {children}
      </Animated.Text>
    </View>
  );
};

export default function DockedController({ hwSettings, lockedProduct, isPaired, points, devices, onLongPressDevice, writeToDevice: parentWriteToDevice, isPoweredOn = true, onDisconnect }: Sk8lytzControllerProps) {
  const { Colors, isDark } = useTheme();
  const styles = createStyles(Colors);
  const [lastSentPayload, setLastSentPayload] = useState<number[]>([]);
  
  const writeToDevice = async (payload: number[]) => {
    setLastSentPayload([...payload]);
    if (parentWriteToDevice) {
      await parentWriteToDevice(payload);
    }
  };
  
  const [activeProduct, setActiveProduct] = useState<ProductType>(lockedProduct || 'HALOZ');
  const [activeMode, setActiveMode] = useState<ModeType>('PRESETS');
  const [selectedColor, setSelectedColor] = useState<string>('#00F0FF');
  const [selectedHue, setSelectedHue] = useState<number>(180);
  const [selectedPatternId, setSelectedPatternId] = useState<number>(1);
  const [brightness, setBrightness] = useState<number>(90);
  const [speed, setSpeed] = useState<number>(50);
  const [micSensitivity, setMicSensitivity] = useState<number>(80);
  const [musicHue, setMusicHue] = useState(180);
  const [recording, setRecording] = useState<Audio.Recording | null>(null);
  const [audioMagnitude, setAudioMagnitude] = useState<number>(0);
  const magnitudeInterval = useRef<NodeJS.Timeout | null>(null);

  const [quickPresets, setQuickPresets] = useState<IQuickPreset[]>([
    { name: 'Rainbow', colors: ['#FF0000', '#FF7F00', '#FFFF00', '#00FF00', '#0000FF', '#4B0082', '#9400D3'], type: 3 },
    { name: 'America', colors: ['#FF0000', '#FFFFFF', '#0000FF'], type: 3 },
    { name: 'Cyberpunk', colors: ['#00FFFF', '#FF00FF', '#FFFF00'], type: 3 },
    { name: 'Forest', colors: ['#00FF00', '#008000', '#228B22', '#32CD32'], type: 1 },
    { name: 'Sunset', colors: ['#FF0000', '#FF4500', '#FF8C00', '#FFA500'], type: 1 },
    { name: 'Ice', colors: ['#FFFFFF', '#E0FFFF', '#00FFFF', '#0000FF'], type: 1 },
    { name: 'Custom 1', colors: ['#000000', '#FFFFFF', '#000000'], type: 3 },
    { name: 'Custom 2', colors: ['#000000', '#FFFFFF', '#000000'], type: 3 }
  ]);
  const [isQuickPromptVisible, setIsQuickPromptVisible] = useState(false);
  const [quickPromptName, setQuickPromptName] = useState('');
  const [quickPromptTargetIndex, setQuickPromptTargetIndex] = useState(-1);
  const [activeQuickPresetIndex, setActiveQuickPresetIndex] = useState<number | null>(null);

  // Multi-Color DIY State
  const [multiColors, setMultiColors] = useState<string[]>(['#FF0000', '#00FF00', '#0000FF']);
  const [multiTransition, setMultiTransition] = useState<number>(3); // 3=Running Water default
  const [multiLength, setMultiLength] = useState<number>(16);
  const [candleAmplitude, setCandleAmplitude] = useState<number>(2);

  // Collapsible Layout States
  const [isFixedExpanded, setIsFixedExpanded] = useState<boolean>(true);
  const [isPresetsExpanded, setIsPresetsExpanded] = useState<boolean>(false);
  const [isCandleExpanded, setIsCandleExpanded] = useState<boolean>(false);
  const [isProgramsExpanded, setIsProgramsExpanded] = useState<boolean>(false);
  const [isMusicExpanded, setIsMusicExpanded] = useState<boolean>(false);
  const [isCameraExpanded, setIsCameraExpanded] = useState<boolean>(false);

  // Active Sub-Mode for the Consolidated Fixed Tab
  const [fixedSubMode, setFixedSubMode] = useState<'PATTERN' | 'MULTI' | 'CANDLE' | 'RBM' | 'MUSIC' | 'CAMERA'>('PATTERN');

  const candleAnim = useRef(new Animated.Value(1)).current;

  useEffect(() => {
    let isActive = true;
    if (fixedSubMode === 'CANDLE') {
       const flicker = () => {
          if (!isActive) return;
          let minOp = 0.8; let maxOp = 1.0;
          let minDur = 100; let maxDur = 300;

          if (candleAmplitude === 1) { // CALM
             minOp = 0.85; maxOp = 1.0; minDur = 300; maxDur = 600;
          } else if (candleAmplitude === 2) { // FLICKERING
             minOp = 0.5; maxOp = 1.0; minDur = 80; maxDur = 250;
          } else { // TURBULENT
             minOp = 0.2; maxOp = 1.0; minDur = 30; maxDur = 120;
          }

          const nextOp = Math.random() * (maxOp - minOp) + minOp;
          const nextDur = Math.random() * (maxDur - minDur) + minDur;

          Animated.timing(candleAnim, {
             toValue: nextOp,
             duration: nextDur,
             useNativeDriver: true
          }).start(({ finished }) => {
             if (finished) flicker();
          });
       };
       flicker();
    } else {
       candleAnim.stopAnimation();
       candleAnim.setValue(1);
    }
    return () => { isActive = false; };
  }, [fixedSubMode, candleAmplitude]);

  // Favorites Array
  const [favorites, setFavorites] = useState<IFavoriteState[]>([]);

  const [isFavPromptVisible, setIsFavPromptVisible] = useState(false);
  const [favPromptName, setFavPromptName] = useState('');
  const [favPromptTargetId, setFavPromptTargetId] = useState<string | null>(null);
  const [activeFavoriteId, setActiveFavoriteId] = useState<string | null>(null);
  const [isDiyBuilderExpanded, setIsDiyBuilderExpanded] = useState(false);

  const handleSaveFavoriteClick = () => {
     if (activeFavoriteId) {
        const activeFav = favorites.find(f => f.id === activeFavoriteId);
        if (activeFav) {
           setFavPromptTargetId(activeFav.id);
           setFavPromptName(activeFav.name);
           setIsFavPromptVisible(true);
           return;
        }
     }
     let defaultName = '';
     try { defaultName = currentStatusText || `${activeMode} Preset`; } catch(e) { defaultName = `${activeMode} Preset`; }
     setFavPromptTargetId(null);
     setFavPromptName(defaultName);
     setIsFavPromptVisible(true);
  };

  const handleConfirmSaveFavorite = () => {
     let defaultName = '';
     try { defaultName = currentStatusText || `${activeMode} Preset`; } catch(e) { defaultName = `${activeMode} Preset`; }
     const name = favPromptName.trim() || defaultName;

     const newFav = {
        id: favPromptTargetId || Date.now().toString(),
        name,
        mode: activeMode === 'FIXED' ? fixedSubMode : activeMode,
        color: selectedColor,
        patternId: activeMode === 'MUSIC' ? musicPatternId : (activeMode === 'FIXED' ? fixedPatternId : selectedPatternId),
        speed,
        brightness,
        fixedColorMode, fixedFgColor, fixedBgColor, fixedHue,
        multiColors, multiTransition, multiLength,
        candleAmplitude,
        musicPrimaryColor, musicSecondaryColor, micSensitivity, micSource, musicMatrixStyle
     };

     if (favPromptTargetId) {
        const newFavorites = favorites.map(f => f.id === favPromptTargetId ? newFav : f);
        setFavorites(newFavorites);
        AsyncStorage.setItem('@Sk8lytz_Favorites', JSON.stringify(newFavorites));
        setIsFavPromptVisible(false);
        setFavPromptTargetId(null);
        return;
     }

     const newFavorites = [...favorites, newFav];
     setFavorites(newFavorites);
     AsyncStorage.setItem('@Sk8lytz_Favorites', JSON.stringify(newFavorites));
     setIsFavPromptVisible(false);
  };

  const deleteFavorite = (id: string) => {
     const newFavorites = favorites.filter(f => f.id !== id);
     setFavorites(newFavorites);
     AsyncStorage.setItem('@Sk8lytz_Favorites', JSON.stringify(newFavorites));
  };

  const loadFavorite = (favRaw: IFavoriteState) => {
     const fav: any = favRaw;
     setActiveFavoriteId(fav.id);
     setActiveMode('FIXED');
       
     // Handle Legacy vs New Mode Signatures
     const targetSubMode = fav.mode === 'FIXED' ? 'PATTERN' : (fav.mode === 'MULTICOLOR' ? 'MULTI' : fav.mode);
     setFixedSubMode(targetSubMode as any);
       
     setSpeed(fav.speed);
     setBrightness(fav.brightness);
     if (fav.color) setSelectedColor(fav.color);
       
     if (targetSubMode === 'PATTERN') {
        setFixedPatternId(fav.patternId);
        setFixedColorMode(fav.fixedColorMode);
        setFixedFgColor(fav.fixedFgColor);
        setFixedBgColor(fav.fixedBgColor);
        applyFixedPattern(fav.patternId, fav.fixedFgColor, fav.fixedBgColor, fav.speed, fav.brightness);
     } else if (targetSubMode === 'RBM') {
        setSelectedPatternId(fav.patternId);
        if (writeToDevice) writeToDevice(ZenggeProtocol.setCustomRbm(fav.patternId, fav.speed, fav.brightness));
     } else if (targetSubMode === 'MUSIC') {
        setMusicPatternId(fav.patternId);
        handleMusicChange(fav.patternId, micSensitivity, fav.brightness, micSource);
     } else if (targetSubMode === 'MULTI') {
        setMultiColors(fav.multiColors);
        setMultiTransition(fav.multiTransition);
        setMultiLength(fav.multiLength || 16);
        if (writeToDevice) {
           const sortIdx = hwSettings?.colorSorting ?? 2;
           const rgbColors = fav.multiColors.map((h: string) => {
              const r = parseInt(h.slice(1,3), 16) || 0;
              const g = parseInt(h.slice(3,5), 16) || 0;
              const b = parseInt(h.slice(5,7), 16) || 0;
              return ZenggeProtocol.applyColorSorting(r, g, b, sortIdx);
           });
           
           const pts = hwSettings?.ledPoints || points || 16;
           const segs = hwSettings?.segments || 1;
           const maxLen = Math.max(1, Math.floor(pts / segs));
           const appliedLength = (fav.multiLength || 16) > maxLen ? maxLen : (fav.multiLength || 16);
           
           writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, appliedLength, fav.multiTransition, fav.speed));
        }
     } else if (fav.mode === 'CANDLE') {
        setCandleAmplitude(fav.candleAmplitude);
        // Defer native command dispatch for candle
        setTimeout(() => {
           sendColor(parseInt(fav.color.slice(1,3),16)||0, parseInt(fav.color.slice(3,5),16)||0, parseInt(fav.color.slice(5,7),16)||0);
        }, 100);
     } else {
        setTimeout(() => {
           sendColor(parseInt(fav.color.slice(1,3),16)||0, parseInt(fav.color.slice(3,5),16)||0, parseInt(fav.color.slice(5,7),16)||0);
        }, 100);
     }
  };

  /** Unified color sender */
  const sendColor = async (r: number, g: number, b: number) => {
    if (!writeToDevice) return;
    if (activeMode === 'CANDLE') {
        const sortIdx = hwSettings?.colorSorting ?? 2;
        const rawR = parseInt(selectedColor.slice(1, 3), 16) || 255;
        const rawG = parseInt(selectedColor.slice(3, 5), 16) || 255;
        const rawB = parseInt(selectedColor.slice(5, 7), 16) || 255;
        const { r: finalR, g: finalG, b: finalB } = ZenggeProtocol.applyColorSorting(rawR, rawG, rawB, sortIdx);
        await writeToDevice(ZenggeProtocol.setCandleMode(finalR, finalG, finalB, speed, brightness, candleAmplitude));
    } else {
        // Solid fallback explicitly forced to length=10, transitionType=1 to stop physical node scrambling/jumping. 
        const colors = Array(10).fill({ r, g, b });
        await writeToDevice(ZenggeProtocol.setMultiColor(colors, 100, 1, 1));
    }
  };

  /** Helper to parse a hex string array into GRB-sorted valid hardware RGB array */
  const generateSortedColors = (hexArray: string[]) => {
      const sortIdx = hwSettings?.colorSorting ?? 2;
      return hexArray.map(h => {
          const r = parseInt(h.slice(1,3), 16) || 0;
          const g = parseInt(h.slice(3,5), 16) || 0;
          const b = parseInt(h.slice(5,7), 16) || 0;
          return ZenggeProtocol.applyColorSorting(r, g, b, sortIdx);
      });
  };

  /** Helper to apply current fixed pattern state to devices */
  const applyFixedPattern = async (
    patternId: number = fixedPatternId,
    fg: string = fixedFgColor,
    bg: string = fixedBgColor,
    currentSpeed: number = speed,
    currentBrightness: number = brightness
  ) => {
    if (!writeToDevice) return;

    const factor = currentBrightness / 100;
    const fgRgbRaw = { 
      r: Math.round(parseInt(fg.slice(1, 3), 16) * factor), 
      g: Math.round(parseInt(fg.slice(3, 5), 16) * factor), 
      b: Math.round(parseInt(fg.slice(5, 7), 16) * factor) 
    };
    const bgRgbRaw = { 
      r: Math.round(parseInt(bg.slice(1, 3), 16) * factor), 
      g: Math.round(parseInt(bg.slice(3, 5), 16) * factor), 
      b: Math.round(parseInt(bg.slice(5, 7), 16) * factor) 
    };
    
    const sortIdx = hwSettings?.colorSorting ?? 2;
    const fgRgb = ZenggeProtocol.applyColorSorting(fgRgbRaw.r, fgRgbRaw.g, fgRgbRaw.b, sortIdx);
    const bgRgb = ZenggeProtocol.applyColorSorting(bgRgbRaw.r, bgRgbRaw.g, bgRgbRaw.b, sortIdx);

    const pts = hwSettings?.ledPoints || points || 16;
    const segs = hwSettings?.segments || 1;
    const effectivePoints = Math.max(1, Math.floor(pts / segs));

    if (patternId === 1) {
      const forcedSpeed = currentSpeed > 0 ? currentSpeed : 100;
      const solidColors = Array(effectivePoints).fill({ r: fgRgb.r, g: fgRgb.g, b: fgRgb.b }); // Buffer natively bounded
      writeToDevice(ZenggeProtocol.setMultiColor(solidColors, forcedSpeed, 1, 1));
    } else if (patternId === 6) {
      writeToDevice(ZenggeProtocol.setCustomMode([
        { mode: 1, speed: currentSpeed, color1: fgRgb, color2: bgRgb }, 
        { mode: 1, speed: currentSpeed, color1: bgRgb, color2: fgRgb }
      ]));
    } else if (patternId === 7) {
      writeToDevice(ZenggeProtocol.setCustomMode([
        { mode: 2, speed: currentSpeed, color1: fgRgb, color2: bgRgb }, 
        { mode: 2, speed: currentSpeed, color1: bgRgb, color2: fgRgb }
      ]));
    } else if (patternId === 8) {
      writeToDevice(ZenggeProtocol.setCustomMode([
        { mode: 2, speed: 100, color1: fgRgb, color2: bgRgb }, 
        { mode: 2, speed: 100, color1: bgRgb, color2: fgRgb }
      ]));
    } else {
      let arr: {r: number, g: number, b: number}[] = [];
      if (patternId === 2) arr = [fgRgb, bgRgb, bgRgb, bgRgb, bgRgb, bgRgb, bgRgb, bgRgb];
      if (patternId === 3) arr = [fgRgb, {r: Math.floor(fgRgb.r*0.5), g: Math.floor(fgRgb.g*0.5), b: Math.floor(fgRgb.b*0.5)}, {r: Math.floor(fgRgb.r*0.2), g: Math.floor(fgRgb.g*0.2), b: Math.floor(fgRgb.b*0.2)}, bgRgb, bgRgb, bgRgb];
      if (patternId === 4) arr = [fgRgb, fgRgb, fgRgb, fgRgb, bgRgb, bgRgb, bgRgb, bgRgb];
      if (patternId === 5) arr = [fgRgb, fgRgb, bgRgb, bgRgb];
      if (patternId === 9) arr = [bgRgb, bgRgb, fgRgb, fgRgb, bgRgb, bgRgb];
      if (patternId === 10) arr = [fgRgb, bgRgb, bgRgb, bgRgb, bgRgb, fgRgb];
      
      const chunk = [...arr];
      while (arr.length > 0 && arr.length < effectivePoints) {
        arr = [...arr, ...chunk];
      }
      arr = arr.slice(0, effectivePoints);
      
      // Send with TransitionType 0x03 (Running Water/Marquee) to actually animate across the matrix
      writeToDevice(ZenggeProtocol.setMultiColor(arr, currentSpeed, 1, 3));
    }
  };

  const applyEmergencyPattern = (spd: number, bright: number) => {
    if (!writeToDevice) return;
    const factor = bright / 100;
    const red = { r: Math.round(255 * factor), g: 0, b: 0 };
    const white = { r: Math.round(255 * factor), g: Math.round(255 * factor), b: Math.round(255 * factor) };
    const yellow = { r: Math.round(255 * factor), g: Math.round(255 * factor), b: 0 };
    const bg = { r: 0, g: 0, b: 0 };
    
    // Construct a 16-point buffer for the controller (Length >= 10 Safe)
    const arr = [
      red, red, red, red,      // Bottom
      yellow, bg, yellow, bg,  // Flowing mid
      white, white, white, white, // Top
      yellow, bg, yellow, bg   // Flowing mid
    ];
    // EXPLICIT Transition Type 0x00 for Native Marquee Flow
    writeToDevice(ZenggeProtocol.setMultiColor(arr, spd, 1, 0));
  };

  const [musicPatternId, setMusicPatternId] = useState<number>(1);
  const [micSource, setMicSource] = useState<'APP' | 'DEVICE'>('APP');
  const [musicMatrixStyle, setMusicMatrixStyle] = useState<number>(39); // 0x27 (39) Light Screen, 0x26 (38) Light Bar
  const [musicSecondaryHue, setMusicSecondaryHue] = useState<number>(300);
  const [musicPrimaryColor, setMusicPrimaryColor] = useState<string>('#FF00FF');
  const [musicSecondaryColor, setMusicSecondaryColor] = useState<string>('#00FFFF');
  const [musicColorFocus, setMusicColorFocus] = useState<'PRIMARY' | 'SECONDARY'>('PRIMARY');
  const [musicSetting, setMusicSetting] = useState<'SENSITIVITY' | 'BRIGHTNESS'>('SENSITIVITY');

  const [fixedPatternId, setFixedPatternId] = useState<number>(1);
  const [fixedColorMode, setFixedColorMode] = useState<'FOREGROUND' | 'BACKGROUND'>('FOREGROUND');
  const [fixedFgColor, setFixedFgColor] = useState<string>('#00FF00');
  const [fixedBgColor, setFixedBgColor] = useState<string>('#000000');
  const [fixedHue, setFixedHue] = useState<number>(120);

  // -- Curated Presets (SK8Lytz Picks) --
  const [curatedPresets, setCuratedPresets] = useState<IFavoriteState[]>([]);

  useEffect(() => {
    const fetchPicks = async () => {
      try {
        if (!supabase) return;
        const { data, error } = await supabase.storage.from('sk8lytz-settings').download('sk8lytz-picks.json');
        if (error) {
          console.warn('[SK8Lytz Picks] No custom picks config found or accessible:', error.message);
        } else if (data) {
          const text = await new Promise<string>((resolve, reject) => {
            const fr = new FileReader();
            fr.onload = () => resolve(fr.result as string);
            fr.onerror = reject;
            fr.readAsText(data);
          });
          const json = JSON.parse(text);
          if (Array.isArray(json)) {
            setCuratedPresets(json);
          } else {
            console.warn('[SK8Lytz Picks] Fetched config is not an array.');
          }
        }
      } catch (e) {
        console.warn('[SK8Lytz Picks] Exception fetching', e);
      }
    };
    fetchPicks();
  }, []);

  // -- App Microphone Logic --
  useEffect(() => {
    const isMusicActive = activeMode === 'MUSIC' || (activeMode === 'FIXED' && fixedSubMode === 'MUSIC');
    if (isMusicActive && micSource === 'APP' && isPoweredOn) {
      startRecording();
    } else {
      stopRecording();
    }
    return () => {
      stopRecording();
    };
  }, [activeMode, fixedSubMode, micSource, isPoweredOn]);

  // -- Analytics Logging --
  const logTimers = useRef<Record<string, ReturnType<typeof setTimeout>>>({});

  const deviceContext = React.useMemo(() => {
    if (!devices || devices.length === 0) return { target: 'none' };
    if (devices.length === 1) return { target: 'device', deviceId: devices[0].id };
    return { target: 'group', deviceIds: devices.map(d => d.id), groupSize: devices.length };
  }, [devices]);

  // Mode change logger
  useEffect(() => {
    AppLogger.log('MODE_CHANGED', { mode: activeMode, ...deviceContext });
  }, [activeMode, deviceContext]);

  useEffect(() => {
    const name = getRbmPatternName(selectedPatternId);
    AppLogger.log('PATTERN_CHANGED', { 
      pattern: `ID:${selectedPatternId}`, 
      name,
      mode: activeMode,
      color: selectedColor,
      ...deviceContext
    });
  }, [selectedPatternId, deviceContext]);

  // Color change logger
  useEffect(() => {
    AppLogger.log('COLOR_CHANGED', { hex: selectedColor, ...deviceContext });
  }, [selectedColor, deviceContext]);

  // Brightness change logger (debounced 600ms)
  useEffect(() => {
    clearTimeout(logTimers.current['brightness']);
    logTimers.current['brightness'] = setTimeout(() => {
      AppLogger.log('BRIGHTNESS_CHANGED', { value: brightness, mode: activeMode, ...deviceContext });
    }, 600);
  }, [brightness, activeMode, deviceContext]);

  // Speed change logger (debounced 600ms)
  useEffect(() => {
    clearTimeout(logTimers.current['speed']);
    logTimers.current['speed'] = setTimeout(() => {
      AppLogger.log('SPEED_CHANGED', { value: speed, mode: activeMode, ...deviceContext });
    }, 600);
  }, [speed, activeMode, deviceContext]);

  const startRecording = async () => {
    try {
      const { granted } = await Audio.requestPermissionsAsync();
      if (!granted) return;

      await Audio.setAudioModeAsync({
        allowsRecordingIOS: true,
        playsInSilentModeIOS: true,
      });

      const { recording: newRecording } = await Audio.Recording.createAsync(
        {
          ...Audio.RecordingOptionsPresets.LOW_QUALITY,
          android: {
            ...Audio.RecordingOptionsPresets.LOW_QUALITY.android,
            extension: '.m4a',
            outputFormat: Audio.AndroidOutputFormat.MPEG_4,
            audioEncoder: Audio.AndroidAudioEncoder.AAC,
            sampleRate: 44100,
            numberOfChannels: 1,
            bitRate: 128000,
          },
          ios: {
            ...Audio.RecordingOptionsPresets.LOW_QUALITY.ios,
            extension: '.m4a',
            outputFormat: Audio.IOSOutputFormat.MPEG4AAC,
            audioQuality: Audio.IOSAudioQuality.MIN,
            sampleRate: 44100,
            numberOfChannels: 1,
            bitRate: 128000,
            linearPCMBitDepth: 16,
            linearPCMIsBigEndian: false,
            linearPCMIsFloat: false,
          },
        },
        null, // initialStatus
        50 // progressUpdateIntervalMillis
      );
      
      await newRecording.setProgressUpdateInterval(50);
      setRecording(newRecording);

      // Start magnitude stream
      magnitudeInterval.current = setInterval(async () => {
        if (!writeToDevice) return;
        const stats = await newRecording.getStatusAsync();
        if (stats.canRecord && stats.isRecording) {
          // stats.metering ranges from -160 to 0. 
          // Typical music peaks around -20 to 0.
          const metering = stats.metering ?? -160;
          // Map -60...0 to 0...1 for usable visualization
          const normalized = Math.max(0, Math.min(1, (metering + 60) / 60));
          
          setAudioMagnitude(normalized);
          
          // Send to physical device (Symphony 0x74 command expects 0-255)
          const deviceMag = Math.floor(normalized * 255);
          writeToDevice(ZenggeProtocol.sendMusicMagnitude(deviceMag));
        }
      }, 50);

    } catch (err) {
      console.error('Failed to start recording', err);
    }
  };

  const stopRecording = async () => {
    if (magnitudeInterval.current) {
      clearInterval(magnitudeInterval.current);
      magnitudeInterval.current = null;
    }
    if (recording) {
      try {
        await recording.stopAndUnloadAsync();
      } catch (e) {}
      setRecording(null);
    }
  };

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
          // if (parsed.activeMode) setActiveMode(parsed.activeMode); // Ensure Docked Mode always defaults to PRESETS on load
          if (parsed.selectedColor) setSelectedColor(parsed.selectedColor);
          if (parsed.selectedPatternId) setSelectedPatternId(parsed.selectedPatternId);
          if (parsed.brightness !== undefined) setBrightness(parsed.brightness);
          else setBrightness(90);
          if (parsed.speed !== undefined) setSpeed(parsed.speed);
          else setSpeed(50);
          if (parsed.micSensitivity !== undefined) setMicSensitivity(parsed.micSensitivity);
          if (parsed.musicHue !== undefined) setMusicHue(parsed.musicHue);
          if (parsed.musicSecondaryHue !== undefined) setMusicSecondaryHue(parsed.musicSecondaryHue);
          if (parsed.musicPrimaryColor) setMusicPrimaryColor(parsed.musicPrimaryColor);
          if (parsed.musicSecondaryColor) setMusicSecondaryColor(parsed.musicSecondaryColor);
          if (parsed.musicMatrixStyle) setMusicMatrixStyle(parsed.musicMatrixStyle);
          if (parsed.musicPatternId) setMusicPatternId(parsed.musicPatternId);
          if (parsed.micSource) setMicSource(parsed.micSource);
          if (parsed.musicSetting) setMusicSetting(parsed.musicSetting);
          if (parsed.fixedPatternId) setFixedPatternId(parsed.fixedPatternId);
          if (parsed.fixedColorMode) setFixedColorMode(parsed.fixedColorMode);
          if (parsed.fixedFgColor) setFixedFgColor(parsed.fixedFgColor);
          if (parsed.fixedBgColor) setFixedBgColor(parsed.fixedBgColor);
          if (parsed.fixedHue !== undefined) setFixedHue(parsed.fixedHue);
          if (parsed.candleAmplitude !== undefined) setCandleAmplitude(parsed.candleAmplitude);
        } catch(e) {}
      }
    });

    AsyncStorage.getItem('@Sk8lytz_Favorites').then((saved) => {
        if (saved) {
            try {
                const parsed = JSON.parse(saved);
                if (parsed && parsed.length > 0) {
                   setFavorites(parsed);
                } else {
                   // Fallback to default if somehow parsed as empty
                   const defaultFavorites = [{
                       id: 'default-1',
                       name: 'Programs - ' + getRbmPatternName(3),
                       mode: 'RBM',
                       patternId: 3,
                       speed: 50,
                       brightness: 90
                   }];
                   setFavorites(defaultFavorites);
                   AsyncStorage.setItem('@Sk8lytz_Favorites', JSON.stringify(defaultFavorites));
                }
            } catch(e) {}
        } else {
            // First time load, inject default Program mode pattern 3
            const defaultFavorites = [{
                id: 'default-1',
                name: 'Programs - ' + getRbmPatternName(3),
                mode: 'RBM',
                patternId: 3,
                speed: 50,
                brightness: 90
            }];
            setFavorites(defaultFavorites);
            AsyncStorage.setItem('@Sk8lytz_Favorites', JSON.stringify(defaultFavorites));
        }
    });

    AsyncStorage.getItem('@Sk8lytz_QuickPresets').then((saved) => {
        if (saved) {
            try {
                const parsed = JSON.parse(saved);
                if (parsed && parsed.length > 0) setQuickPresets(parsed);
            } catch(e) {}
        }
    });
  }, []);

  React.useEffect(() => {
    const stateBlob = {
      activeMode, selectedColor, selectedPatternId, brightness, speed,
      micSensitivity, musicHue, musicSecondaryHue, musicPrimaryColor, musicSecondaryColor, musicMatrixStyle, musicPatternId, micSource, musicSetting,
      fixedPatternId, fixedColorMode, fixedFgColor, fixedBgColor, fixedHue, candleAmplitude
    };
    AsyncStorage.setItem('@Sk8lytz_ControllerState', JSON.stringify(stateBlob)).catch(() => {});
  }, [
    activeMode, selectedColor, selectedPatternId, brightness, speed, 
    micSensitivity, musicHue, musicSecondaryHue, musicPrimaryColor, musicSecondaryColor, musicMatrixStyle, musicPatternId, micSource, musicSetting,
    fixedPatternId, fixedColorMode, fixedFgColor, fixedBgColor, fixedHue, candleAmplitude
  ]);

  const handleMusicChange = (
    patternId: number = musicPatternId,
    sens: number = micSensitivity,
    bright: number = brightness,
    src: 'APP' | 'DEVICE' = micSource,
    color1Hex: string = musicPrimaryColor,
    color2Hex: string = musicSecondaryColor,
    matrix: number = musicMatrixStyle
  ) => {
    if (!writeToDevice) return;
    
    const isDeviceMic = src === 'DEVICE';
    
    const c1Raw = { 
       r: parseInt(color1Hex.slice(1,3), 16) || 0, 
       g: parseInt(color1Hex.slice(3,5), 16) || 0, 
       b: parseInt(color1Hex.slice(5,7), 16) || 0 
    };
    
    const c2Raw = { 
       r: parseInt(color2Hex.slice(1,3), 16) || 0, 
       g: parseInt(color2Hex.slice(3,5), 16) || 0, 
       b: parseInt(color2Hex.slice(5,7), 16) || 0 
    };
    
    const sortIdx = hwSettings?.colorSorting ?? 2;
    const c1 = ZenggeProtocol.applyColorSorting(c1Raw.r, c1Raw.g, c1Raw.b, sortIdx);
    const c2 = ZenggeProtocol.applyColorSorting(c2Raw.r, c2Raw.g, c2Raw.b, sortIdx);
    
    writeToDevice(ZenggeProtocol.setMusicConfig(
      isDeviceMic,
      matrix,
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
    switch (activeMode) {
      case 'FIXED':
        const fixedClr = fixedColorMode === 'FOREGROUND' ? fixedFgColor : fixedBgColor;
        return `Fixed - ${getColorName(fixedClr)}`;
      case 'RBM':
        return `Programs - ${getRbmPatternName(selectedPatternId)}`;
      case 'MUSIC':
        const patternName = MUSIC_PATTERNS[musicPatternId - 1] || `Effect ${musicPatternId}`;
        return `Music - ${patternName}`;
      case 'CAMERA': return 'Camera';
      case 'CUSTOM': return 'Custom';
      case 'MULTICOLOR': return 'Multicolor Array';
      case 'CANDLE': return 'Candle Mode';
      case 'PRESETS': return 'Styles';
      default: return activeMode;
    }
  }, [activeMode, fixedColorMode, fixedFgColor, fixedBgColor, selectedPatternId, musicPatternId, selectedColor]);
  const modes = [
    { id: 'PRESETS', label: 'Styles', icon: 'star-outline' },
    { id: 'FIXED', label: 'Fixed', icon: 'palette-outline' }
  ];

  const visualizerColor = React.useMemo(() => {
    if (activeMode === 'FIXED') {
      if (fixedSubMode === 'CANDLE' || fixedSubMode === 'MULTI' || fixedSubMode === 'PATTERN' || fixedSubMode === 'CAMERA') return selectedColor;
      if (fixedSubMode === 'MUSIC') {
        const f = (n: number, k = (n + musicHue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
        const hex = [f(5), f(3), f(1)].map(x => Math.round(x * 255).toString(16).padStart(2, '0')).join('');
        return `#${hex}`;
      }
      return fixedColorMode === 'FOREGROUND' ? fixedFgColor : fixedBgColor;
    }
    return selectedColor;
  }, [activeMode, fixedColorMode, fixedFgColor, fixedBgColor, musicHue, selectedColor, fixedSubMode]);

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
        <TouchableOpacity 
           style={{ position: 'absolute', top: 12, right: 16, zIndex: 100, backgroundColor: 'rgba(255,255,255,0.1)', padding: 6, borderRadius: 20 }}
           onPress={handleSaveFavoriteClick}
        >
           <MaterialCommunityIcons name="heart-plus-outline" size={22} color={Colors.primary} />
        </TouchableOpacity>
        <ProductVisualizer 
          product={activeProduct} 
          color={visualizerColor} 
          mode={activeMode === 'PRESETS' ? 'MULTICOLOR' : activeMode === 'FIXED' ? (fixedSubMode === 'MULTI' ? 'MULTICOLOR' : (fixedSubMode === 'CANDLE' ? 'CANDLE' : (fixedSubMode === 'PATTERN' ? 'RBM' : (fixedSubMode === 'MUSIC' ? 'MUSIC' : (fixedSubMode === 'CAMERA' ? 'CAMERA' : 'FIXED'))))) : activeMode} 
          patternId={activeMode === 'FIXED' && fixedSubMode === 'MUSIC' ? musicPatternId : (activeMode === 'FIXED' && fixedSubMode === 'PATTERN' ? fixedPatternId : selectedPatternId)} 
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
          audioMagnitude={audioMagnitude}
          rawHexPayload={lastSentPayload}
          multiColors={activeMode === 'PRESETS' ? ['#FF0000', '#FF7F00', '#FFFF00', '#00FF00', '#0000FF', '#4B0082', '#9400D3'] : multiColors}
          multiTransition={activeMode === 'PRESETS' ? 3 : multiTransition}
        />
      </View>
      </View>
      
      {/* Removed Active Mode Header to save vertical space */}

      <View style={[styles.controlsContainer, { padding: 4, overflow: 'hidden' }]}>
        <View style={[styles.activeModeContainer, { flex: 1, justifyContent: 'space-evenly' }]}>
          {activeMode === 'PRESETS' && (
            <View style={{ flex: 1, paddingHorizontal: Layout.padding, justifyContent: 'space-evenly' }}>
              
              <Text style={[Typography.title, isDark && { color: '#FFF' }, { fontSize: 13 }]}>YOURS</Text>
              
              <View style={[styles.presetContainer, { flex: 1 }]}>
                 {Array.from({ length: 4 }).map((_, idx) => {
                    const fav = favorites[idx];
                    if (!fav) return <View key={`empty-yours-${idx}`} style={[styles.presetCard, { borderWidth: 1.5, borderStyle: 'dashed', borderColor: 'rgba(255,255,255,0.08)', backgroundColor: 'transparent', elevation: 0, shadowOpacity: 0 }]} />;
                    return (
                      <TouchableOpacity 
                        key={fav.id}
                        style={[styles.presetCard, { borderColor: Colors.primary }]}
                        onPress={() => loadFavorite(fav)}
                      >
                        <TouchableOpacity 
                           style={{ position: 'absolute', right: 4, top: 4, zIndex: 10, padding: 4 }} 
                           onPress={() => {
                              setFavPromptTargetId(fav.id);
                              setFavPromptName(fav.name);
                              setIsFavPromptVisible(true);
                           }}
                        >
                           <MaterialCommunityIcons name="pencil-outline" size={12} color={Colors.textMuted} />
                        </TouchableOpacity>

                        <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'center', width: '100%', marginTop: 2, marginBottom: 2, gap: 10, opacity: 0.8, paddingHorizontal: 16 }}>
                           <View style={{ flexDirection: 'row', alignItems: 'center', gap: 2 }}>
                              {fav.mode === 'MUSIC' ? (
                                 <><MaterialCommunityIcons name="microphone-outline" size={9} color={Colors.primary} /><Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(fav.micSensitivity || fav.speed || 50)}%</Text></>
                              ) : (
                                 <><MaterialCommunityIcons name="speedometer" size={9} color={Colors.primary} /><Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(fav.speed || 50)}%</Text></>
                              )}
                           </View>
                           <View style={{ flexDirection: 'row', alignItems: 'center', gap: 2 }}>
                              <MaterialCommunityIcons name="brightness-6" size={9} color={Colors.primary} />
                              <Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(fav.brightness || 100)}%</Text>
                           </View>
                        </View>
                        <View style={{ width: '100%', minHeight: 20, justifyContent: 'center', alignItems: 'center' }}>
                           <MarqueeText style={[styles.presetTitle, { fontSize: 13, textAlign: 'center', width: '100%' }]}>{fav.name}</MarqueeText>
                        </View>
                        {(() => {
                           if (fav.mode === 'CANDLE' || (fav.mode === 'PATTERN' && fav.patternId === 1) || (fav.mode === 'FIXED' && fav.patternId === 1)) {
                              const c = fav.mode === 'CANDLE' ? fav.color : (fav.fixedFgColor || Colors.primary);
                              return <View style={{ width: '60%', height: 6, borderRadius: 3, backgroundColor: c, borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', marginTop: 4, marginBottom: 2 }} />;
                           } else if (fav.mode === 'MUSIC') {
                              return (
                                 <View style={{ width: '60%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', marginTop: 4, marginBottom: 2 }}>
                                    <View style={{ flex: 1, backgroundColor: fav.musicPrimaryColor || '#00FFFF' }} />
                                    <View style={{ flex: 1, backgroundColor: fav.musicSecondaryColor || '#FF00FF' }} />
                                 </View>
                              );
                           } else if (fav.mode === 'PATTERN' || fav.mode === 'FIXED') {
                              return (
                                 <View style={{ width: '60%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', marginTop: 4, marginBottom: 2 }}>
                                    <View style={{ flex: 1, backgroundColor: fav.fixedFgColor || '#FFFFFF' }} />
                                    <View style={{ flex: 1, backgroundColor: fav.fixedBgColor || '#000000' }} />
                                 </View>
                              );
                           } else if (fav.mode === 'MULTI') {
                              const colors = fav.multiColors || ['#FFFFFF'];
                              return (
                                 <View style={{ width: '80%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', marginTop: 4, marginBottom: 2 }}>
                                    {colors.map((c: string, i: number) => <View key={i} style={{ flex: 1, backgroundColor: c }} />)}
                                 </View>
                              );
                           } else {
                              return <MaterialCommunityIcons name={fav.mode === 'RBM' ? 'animation-play' : 'shape-square-plus'} size={14} color={Colors.primary} style={{ marginTop: 4, marginBottom: 2 }} />;
                           }
                        })()}
                      </TouchableOpacity>
                    );
                 })}
              </View>

              <Text style={[Typography.title, isDark && { color: '#FFF' }, { fontSize: 13, marginTop: 4 }]}>SK8Lytz Picks</Text>
              
              <View style={[styles.presetContainer, { flex: 1 }]}>
                 {Array.from({ length: Math.max(4, curatedPresets.length) }).map((_, idx) => {
                    const fav = curatedPresets[idx];
                    if (!fav) return <View key={`empty-ours-${idx}`} style={[styles.presetCard, { borderWidth: 1.5, borderStyle: 'dashed', borderColor: 'rgba(255,255,255,0.08)', backgroundColor: 'transparent', elevation: 0, shadowOpacity: 0 }]} />;
                    return (
                      <TouchableOpacity 
                        key={fav.id}
                        style={[styles.presetCard, { borderColor: Colors.secondary }]}
                        onPress={() => loadFavorite(fav)}
                      >
                        <View style={{ width: '100%', minHeight: 20, justifyContent: 'center', alignItems: 'center', marginTop: 2 }}>
                           <MarqueeText style={[styles.presetTitle, { fontSize: 13, textAlign: 'center', width: '100%' }]}>{fav.customName || fav.name}</MarqueeText>
                        </View>
                        <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'center', marginTop: 2, marginBottom: 4, gap: 4, opacity: 0.8 }}>
                           {fav.mode === 'MUSIC' ? (
                              <><MaterialCommunityIcons name="microphone-outline" size={10} color={Colors.secondary} /><Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(fav.micSensitivity || fav.speed || 50)}%</Text></>
                           ) : (
                              <><MaterialCommunityIcons name="speedometer" size={10} color={Colors.secondary} /><Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(fav.speed || 50)}%</Text></>
                           )}
                           <Text style={{ fontSize: 9, color: Colors.textMuted }}> | </Text>
                           <MaterialCommunityIcons name="brightness-6" size={10} color={Colors.secondary} />
                           <Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(fav.brightness || 100)}%</Text>
                        </View>
                        {(() => {
                           if (fav.mode === 'CANDLE' || (fav.mode === 'PATTERN' && fav.patternId === 1) || (fav.mode === 'FIXED' && fav.patternId === 1)) {
                              const c = fav.mode === 'CANDLE' ? fav.color : (fav.fixedFgColor || Colors.primary);
                              return <View style={{ width: '60%', height: 6, borderRadius: 3, backgroundColor: c, borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', marginTop: 4, marginBottom: 2 }} />;
                           } else if (fav.mode === 'MUSIC') {
                              return (
                                 <View style={{ width: '60%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', marginTop: 4, marginBottom: 2 }}>
                                    <View style={{ flex: 1, backgroundColor: fav.musicPrimaryColor || '#00FFFF' }} />
                                    <View style={{ flex: 1, backgroundColor: fav.musicSecondaryColor || '#FF00FF' }} />
                                 </View>
                              );
                           } else if (fav.mode === 'PATTERN' || fav.mode === 'FIXED') {
                              return (
                                 <View style={{ width: '60%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', marginTop: 4, marginBottom: 2 }}>
                                    <View style={{ flex: 1, backgroundColor: fav.fixedFgColor || '#FFFFFF' }} />
                                    <View style={{ flex: 1, backgroundColor: fav.fixedBgColor || '#000000' }} />
                                 </View>
                              );
                           } else if (fav.mode === 'MULTI') {
                              const colors = fav.multiColors || ['#FFFFFF'];
                              return (
                                 <View style={{ width: '80%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', marginTop: 4, marginBottom: 2 }}>
                                    {colors.map((c: string, i: number) => <View key={i} style={{ flex: 1, backgroundColor: c }} />)}
                                 </View>
                              );
                           } else {
                              return <MaterialCommunityIcons name={fav.mode === 'RBM' ? 'animation-play' : 'shape-square-plus'} size={14} color={Colors.secondary} style={{ marginTop: 4, marginBottom: 2 }} />;
                           }
                        })()}
                      </TouchableOpacity>
                    );
                 })}
              </View>
            </View>
          )}

          {activeMode === 'FIXED' && (
            <View style={{ flex: 1, marginBottom: 8, justifyContent: 'flex-start' }}>
              
              {/* UNIFIED SOLID & MULTI-COLOR PRESETS & CANDLE & DIY BUILDER */}
              {(fixedSubMode === 'MULTI' || fixedSubMode === 'PATTERN') && (
                <View style={{ flex: 1, width: '100%', marginBottom: 4 }}>
                  
                  {/* UNIFIED TOGGLE */}
                  <View style={{ flexDirection: 'row', marginBottom: 6, marginTop: 2, flexShrink: 0, minHeight: 36 }}>
                    <TouchableOpacity 
                      onPress={() => {
                        setFixedSubMode('PATTERN');
                        if (fixedPatternId === 1) setFixedPatternId(2);
                      }}
                      style={{ flex: 1, paddingVertical: 6, alignItems: 'center', backgroundColor: fixedSubMode === 'PATTERN' ? Colors.primary : Colors.surfaceHighlight, borderTopLeftRadius: Layout.borderRadius, borderBottomLeftRadius: Layout.borderRadius }}
                    >
                      <Text style={{ color: fixedSubMode === 'PATTERN' ? '#000' : Colors.textMuted, fontWeight: 'bold' }}>Solid Patterns</Text>
                    </TouchableOpacity>
                    <TouchableOpacity 
                      onPress={() => setFixedSubMode('MULTI')}
                      style={{ flex: 1, paddingVertical: 6, alignItems: 'center', backgroundColor: fixedSubMode === 'MULTI' ? Colors.primary : Colors.surfaceHighlight, borderLeftWidth: 1, borderColor: 'rgba(255,255,255,0.05)', borderTopRightRadius: Layout.borderRadius, borderBottomRightRadius: Layout.borderRadius }}
                    >
                      <Text style={{ color: fixedSubMode === 'MULTI' ? '#000' : Colors.textMuted, fontWeight: 'bold' }}>Presets & DIY</Text>
                    </TouchableOpacity>
                  </View>

                  {/* SOLID PATTERNS TIER */}
                  {fixedSubMode === 'PATTERN' && (
                  <View style={{ flex: 1, paddingBottom: 6 }}>
                    <ScrollView 
                       style={{ flex: 1, backgroundColor: Colors.isDark ? '#000000' : 'rgba(0,0,0,0.04)', borderRadius: 8 }} 
                       contentContainerStyle={{ padding: 8, flexDirection: 'row', flexWrap: 'wrap', justifyContent: 'space-between' }}
                       showsVerticalScrollIndicator={false}
                    >
                      {(() => {
                        const fgRgb = (hex: string, alpha: number) => {
                           const h = hex || '#FFFFFF';
                           const r = parseInt(h.substring(1, 3), 16) || 0;
                           const g = parseInt(h.substring(3, 5), 16) || 0;
                           const b = parseInt(h.substring(5, 7), 16) || 0;
                           return `rgba(${r},${g},${b},${alpha})`;
                        };
                        const f = fixedFgColor || '#FFFFFF';
                        const b = fixedBgColor || 'transparent';
                        return [
                          { id: 1, label: 'Solid', dots: [f, f, f, f, f, f, f, f] },
                          { id: 2, label: 'Single Dot', dots: [f, b, b, b, b, b, b, b] },
                          { id: 3, label: 'Comet', dots: [f, fgRgb(f, 0.6), fgRgb(f, 0.3), fgRgb(f, 0.1), b, b, b, b] },
                          { id: 4, label: 'Dashed', dots: [f, f, b, b, f, f, b, b] },
                          { id: 5, label: 'Alternating', dots: [f, b, f, b, '#FFFFFF', b, '#FFFFFF', b] },
                          { id: 6, label: 'Breath', dots: [f, fgRgb(f, 0.5), b, fgRgb(f, 0.5), f, fgRgb(f, 0.5), b, fgRgb(f, 0.5)] },
                          { id: 7, label: 'Flash', dots: [f, b, f, b, f, b, f, b] },
                          { id: 8, label: 'Strobe', dots: ['#FFFFFF', b, '#FFFFFF', b, '#FFFFFF', b, '#FFFFFF', b] },
                          { id: 9, label: 'Wave', dots: [b, fgRgb(f, 0.5), f, f, fgRgb(f, 0.5), b, b, b] },
                          { id: 10, label: 'Pinch', dots: [f, fgRgb(f, 0.5), b, b, b, fgRgb(f, 0.5), f, b] }
                        ];
                      })().map(pattern => (
                        <TouchableOpacity 
                          key={pattern.id}
                          onPress={() => {
                            setFixedSubMode('PATTERN');
                            setFixedPatternId(pattern.id);
                            applyFixedPattern(pattern.id);
                            if (pattern.id === 1) {
                               setFixedColorMode('FOREGROUND');
                            }
                          }}
                          style={{ width: '48%', minHeight: 40, marginBottom: 8, flexDirection: 'row', alignItems: 'center', borderBottomWidth: 1, borderBottomColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.05)' }}
                        >
                          <Text style={{ color: Colors.text, flex: 1, fontWeight: 'bold', fontSize: 13 }} numberOfLines={1}>{pattern.label}</Text>
                          <FixedPatternPreviewRow baseDots={pattern.dots} patternId={pattern.id} speed={speed} points={devices?.[0]?.points || points || 16} segments={devices?.[0]?.segments || 1} />
                        </TouchableOpacity>
                      ))}
                    </ScrollView>
                  </View>
                  )}

                  {/* QUICK PRESETS TIER */}
                  {fixedSubMode === 'MULTI' && (
                  <View style={{ flex: 1, paddingBottom: 6 }}>
                    <Text style={{ color: Colors.textMuted, fontSize: 11, fontWeight: 'bold', marginBottom: 4 }}>PRESETS & DIY</Text>
                    <View style={{ flex: 1, flexDirection: 'column', flexWrap: 'wrap', alignContent: 'stretch', gap: 4 }}>
                      {quickPresets.map((preset, idx) => (
                        <TouchableOpacity 
                          key={idx}
                          onPress={() => {
                              setActiveQuickPresetIndex(idx);
                              setFixedSubMode('MULTI');
                              setMultiColors(preset.colors);
                              setMultiTransition(preset.type);
                              if (writeToDevice) {
                                  const rgbColors = generateSortedColors(preset.colors);
                                  const pts = hwSettings?.ledPoints || points || 16; const appliedLength = Math.min(multiLength, Math.max(1, Math.floor(pts / (hwSettings?.segments || 1))));
                                  writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, appliedLength, preset.type, speed));
                              }
                          }}
                          style={{
                              flex: 1, minHeight: 45, justifyContent: 'center', paddingHorizontal: 10,
                              backgroundColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.05)', 
                              borderRadius: 8, borderWidth: 1, borderColor: activeQuickPresetIndex === idx ? Colors.primary : (Colors.isDark ? 'rgba(255,255,255,0.1)' : 'rgba(0,0,0,0.1)')
                          }}
                        >
                          <TouchableOpacity 
                            style={{ position: 'absolute', right: 10, top: '50%', marginTop: -14, padding: 6, zIndex: 10 }}
                            onPress={() => {
                               setActiveQuickPresetIndex(idx);
                               setFixedSubMode('MULTI');
                               setMultiColors(preset.colors);
                               setMultiTransition(preset.type);
                               setIsDiyBuilderExpanded(true);
                            }}
                          >
                             <MaterialCommunityIcons name="pencil-outline" size={16} color={Colors.textMuted} />
                          </TouchableOpacity>

                          <View style={{ width: '80%', height: 20, justifyContent: 'center', alignItems: 'center' }}>
                            <MarqueeText style={{ color: Colors.text, fontWeight: 'bold', fontSize: 11 }}>{preset.name}</MarqueeText>
                          </View>
                          <View style={{ flexDirection: 'row', gap: 2, justifyContent: 'center', marginRight: '15%' }}>
                             {preset.colors.slice(0,6).map((c: string, i: number) => (
                                <View key={i} style={{ width: 6, height: 6, borderRadius: 3, backgroundColor: c }} />
                             ))}
                             {preset.colors.length > 6 && <View style={{ width: 6, height: 6, borderRadius: 3, backgroundColor: '#888' }} />}
                          </View>
                        </TouchableOpacity>
                      ))}
                    </View>

                    {!isDiyBuilderExpanded && (
                      <TouchableOpacity 
                        style={{ marginTop: 8, padding: 12, backgroundColor: Colors.surfaceHighlight, borderRadius: 8, alignItems: 'center', borderWidth: 1, borderColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.05)' }}
                        onPress={() => {
                           setActiveQuickPresetIndex(null);
                           setIsDiyBuilderExpanded(true);
                        }}
                      >
                        <Text style={{ color: Colors.text, fontWeight: 'bold', fontSize: 12 }}>+ Create New DIY Array</Text>
                      </TouchableOpacity>
                    )}

                    {isDiyBuilderExpanded && (
                    <View style={{ marginTop: 12, padding: 10, backgroundColor: Colors.isDark ? 'rgba(255,255,255,0.02)' : 'rgba(0,0,0,0.02)', borderRadius: 12, borderWidth: 1, borderColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.05)' }}>
                      <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 }}>
                         <Text style={{ color: Colors.textMuted, fontSize: 11, fontWeight: 'bold' }}>DIY ARRAY BUILDER</Text>
                         <TouchableOpacity onPress={() => setIsDiyBuilderExpanded(false)} style={{ padding: 4 }}>
                            <MaterialCommunityIcons name="chevron-up" size={24} color={Colors.textMuted} />
                         </TouchableOpacity>
                      </View>

                      <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 6 }}>
                        {multiColors.map((hex, index) => (
                          <TouchableOpacity key={index} style={{ width: 34, height: 34, borderRadius: 17, backgroundColor: hex, borderWidth: 2, borderColor: '#FFF', shadowColor: hex, shadowOpacity: 0.8, shadowRadius: 4 }} onPress={() => {
                            setFixedSubMode('MULTI');
                            const newArr = [...multiColors];
                            newArr[index] = selectedColor;
                            setMultiColors(newArr);
                            const rgbColors = generateSortedColors(newArr);
                            const pts = hwSettings?.ledPoints || points || 16; const appliedLength = Math.min(multiLength, Math.max(1, Math.floor(pts / (hwSettings?.segments || 1))));
                            if(writeToDevice) writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, appliedLength, multiTransition, speed));
                          }} />
                        ))}
                        {multiColors.length < 16 && (
                          <TouchableOpacity style={{ width: 34, height: 34, borderRadius: 17, backgroundColor: Colors.isDark ? 'rgba(255,255,255,0.1)' : 'rgba(0,0,0,0.05)', borderWidth: 1, borderColor: Colors.isDark ? '#FFF' : Colors.text, justifyContent: 'center', alignItems: 'center' }} onPress={() => {
                             setFixedSubMode('MULTI');
                             const newArr = [...multiColors, selectedColor];
                             setMultiColors(newArr);
                             const rgbColors = generateSortedColors(newArr);
                             const pts = hwSettings?.ledPoints || points || 16; const appliedLength = Math.min(multiLength, Math.max(1, Math.floor(pts / (hwSettings?.segments || 1))));
                            if(writeToDevice) writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, appliedLength, multiTransition, speed));
                          }}>
                            <Text style={{ color: Colors.text, fontSize: 20, fontWeight: 'bold' }}>+</Text>
                          </TouchableOpacity>
                        )}
                        {multiColors.length > 1 && (
                          <TouchableOpacity style={{ width: 34, height: 34, borderRadius: 17, backgroundColor: 'rgba(255,0,0,0.3)', borderWidth: 1, borderColor: Colors.isDark ? '#FFF' : Colors.text, justifyContent: 'center', alignItems: 'center' }} onPress={() => {
                             setFixedSubMode('MULTI');
                             const newArr = [...multiColors];
                             newArr.pop();
                             setMultiColors(newArr);
                             const rgbColors = generateSortedColors(newArr);
                             const pts = hwSettings?.ledPoints || points || 16; const appliedLength = Math.min(multiLength, Math.max(1, Math.floor(pts / (hwSettings?.segments || 1))));
                            if(writeToDevice) writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, appliedLength, multiTransition, speed));
                          }}>
                            <Text style={{ color: Colors.text, fontSize: 20, fontWeight: 'bold', lineHeight: 22 }}>-</Text>
                          </TouchableOpacity>
                        )}
                        <TouchableOpacity style={{ width: 34, height: 34, borderRadius: 17, backgroundColor: Colors.primary, borderWidth: 1, borderColor: Colors.isDark ? 'rgba(255,255,255,0.2)' : 'rgba(0,0,0,0.1)', justifyContent: 'center', alignItems: 'center', marginLeft: 'auto' }} onPress={() => {
                            if (activeQuickPresetIndex !== null && quickPresets[activeQuickPresetIndex]) {
                                setQuickPromptTargetIndex(activeQuickPresetIndex);
                                setQuickPromptName(quickPresets[activeQuickPresetIndex].name);
                            } else {
                                setQuickPromptTargetIndex(-1);
                                setQuickPromptName('Custom Preset');
                            }
                            setIsQuickPromptVisible(true);
                        }}>
                          <MaterialCommunityIcons name="content-save" size={16} color="#000" />
                        </TouchableOpacity>
                      </View>

                      <Text style={{ color: Colors.textMuted, fontSize: 11, marginBottom: 4, marginTop: 12, fontWeight: 'bold' }}>TRANSITION TYPE</Text>
                      <View style={{ flexDirection: 'row', flexWrap: 'wrap', marginBottom: 6 }}>
                        {[
                          { label: 'Static', val: 0 },
                          { label: 'Gradual', val: 1 },
                          { label: 'Strobe', val: 2 },
                          { label: 'Running Water', val: 3 }
                        ].map((mode) => (
                          <TouchableOpacity 
                            key={mode.val} 
                            onPress={() => {
                               setFixedSubMode('MULTI');
                               setMultiTransition(mode.val);
                               const rgbColors = generateSortedColors(multiColors);
                               const pts = hwSettings?.ledPoints || points || 16; const appliedLength = Math.min(multiLength, Math.max(1, Math.floor(pts / (hwSettings?.segments || 1))));
                               if(writeToDevice) writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, appliedLength, mode.val, speed));
                            }} 
                            style={{ 
                              paddingHorizontal: 12, paddingVertical: 6, 
                              backgroundColor: multiTransition === mode.val ? Colors.primary : (Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.05)'), 
                              borderRadius: 8, marginRight: 6, marginBottom: 6 
                            }}
                          >
                            <Text style={{ color: multiTransition === mode.val ? '#000' : Colors.text, fontWeight: 'bold', fontSize: 11 }}>{mode.label}</Text>
                          </TouchableOpacity>
                        ))}
                      </View>
                    </View>
                    )}
                  </View>
                  )}

                </View>
              )}
              {/* PROGRAMS */}
              {fixedSubMode === 'RBM' && (
                <View style={{ width: '100%', paddingVertical: 8, height: 350 }}>
                  <VerticalPatternDrum 
                    value={selectedPatternId}
                    onValueChange={(pid) => {
                      setFixedSubMode('RBM');
                      setSelectedPatternId(pid);
                      if (writeToDevice) {
                          if (pid === 103) {
                              applyEmergencyPattern(speed, brightness);
                          } else {
                              writeToDevice(ZenggeProtocol.setCustomRbm(pid, speed, brightness));
                          }
                      }
                    }}
                    min={1}
                    max={103}
                    itemLabel={(val) => {
                       const name = getRbmPatternName(val);
                       return name.split(': ')[1] || name;
                    }}
                  />
                </View>
              )}

              {/* MUSIC SYNC */}
              {fixedSubMode === 'MUSIC' && (
                <View style={{ flex: 1, width: '100%', marginBottom: 4 }}>
                  <View style={{ flexDirection: 'row', marginBottom: 6, marginTop: 2, flexShrink: 0, minHeight: 36 }}>
                    <TouchableOpacity 
                      onPress={() => {
                         setFixedSubMode('MUSIC');
                         setMusicMatrixStyle(39);
                         handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, musicSecondaryColor, 39);
                      }}
                      style={{ flex: 1, paddingVertical: 6, alignItems: 'center', backgroundColor: musicMatrixStyle === 39 ? Colors.primary : Colors.surfaceHighlight, borderTopLeftRadius: Layout.borderRadius, borderBottomLeftRadius: Layout.borderRadius }}
                    >
                      <Text style={{ color: musicMatrixStyle === 39 ? '#000' : Colors.textMuted, fontWeight: 'bold' }}>Light Screen</Text>
                    </TouchableOpacity>
                    <TouchableOpacity 
                      onPress={() => {
                         setFixedSubMode('MUSIC');
                         setMusicMatrixStyle(38);
                         handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, musicSecondaryColor, 38);
                      }}
                      style={{ flex: 1, paddingVertical: 6, alignItems: 'center', backgroundColor: musicMatrixStyle === 38 ? Colors.primary : Colors.surfaceHighlight, borderLeftWidth: 1, borderColor: 'rgba(255,255,255,0.05)', borderTopRightRadius: Layout.borderRadius, borderBottomRightRadius: Layout.borderRadius }}
                    >
                      <Text style={{ color: musicMatrixStyle === 38 ? '#000' : Colors.textMuted, fontWeight: 'bold' }}>Light Bar</Text>
                    </TouchableOpacity>
                  </View>
                  <View style={{ flex: 1, width: '100%', justifyContent: 'space-evenly' }}>
                  <View style={[styles.musicToggleHeader, { justifyContent: 'center' }]}>
                <View style={[styles.musicModeIndicator, { alignItems: 'center' }]}>
                  <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                    <TouchableOpacity onPress={() => {
                      setFixedSubMode('MUSIC');
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
                      setFixedSubMode('MUSIC');
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

              <View style={[styles.musicVisualizerSection, { flex: 1, justifyContent: 'center' }]}>
                <SpectrumVisualizer magnitude={audioMagnitude} />
              </View>

              <View style={styles.micControlSection}>
                <TouchableOpacity 
                  style={[styles.micIconBtn, micSource === 'APP' && styles.micBtnActive]} 
                  onPress={() => {
                    setFixedSubMode('MUSIC');
                    setMicSource('APP');
                    handleMusicChange(musicPatternId, micSensitivity, brightness, 'APP');
                  }}
                >
                  <View style={[styles.micIconCircle, micSource === 'APP' && { backgroundColor: Colors.primary }]}>
                    <Text style={[styles.micIconText, micSource === 'APP' && { color: '#FFF' }]}>🎙️</Text>
                  </View>
                  <Text style={[styles.micSubText, micSource === 'APP' && { color: Colors.primary, fontWeight: 'bold' }]}>APP MIC</Text>
                </TouchableOpacity>

                <TouchableOpacity 
                  style={styles.playButtonMain}
                  onPress={() => handleMusicChange()}
                >
                  <View style={styles.playIconInner}>
                    <MaterialCommunityIcons name="play" size={24} color="#FFF" />
                  </View>
                </TouchableOpacity>

                <TouchableOpacity 
                  style={[styles.micIconBtn, micSource === 'DEVICE' && styles.micBtnActive]} 
                  onPress={() => {
                    setFixedSubMode('MUSIC');
                    setMicSource('DEVICE');
                    handleMusicChange(musicPatternId, micSensitivity, brightness, 'DEVICE');
                  }}
                >
                  <View style={[styles.micIconCircle, micSource === 'DEVICE' && { backgroundColor: Colors.primary }]}>
                    <Text style={[styles.micIconText, micSource === 'DEVICE' && { color: '#FFF' }]}>🎤</Text>
                  </View>
                  <Text style={[styles.micSubText, micSource === 'DEVICE' && { color: Colors.primary, fontWeight: 'bold' }]}>DEVICE MIC</Text>
                </TouchableOpacity>
              </View>

                </View>
                </View>
              )}



              {/* CAMERA */}
              {fixedSubMode === 'CAMERA' && (
                <View style={{ flex: 1 }}>


                  <CameraTracker 
                     isActive={activeMode === 'FIXED' && fixedSubMode === 'CAMERA'} 
                     onColorDetected={(hex) => {
                        setSelectedColor(hex);
                        const r = parseInt(hex.slice(1, 3), 16);
                        const g = parseInt(hex.slice(3, 5), 16);
                        const b = parseInt(hex.slice(5, 7), 16);
                        sendColor(r, g, b);
                     }} 
                  />
                </View>
              )}
            </View>
          )}


          {activeMode === 'CUSTOM' && (
            <View style={{ marginBottom: 8 }}>
              <Text style={[Typography.title, isDark && { color: '#FFF' }]}>DIY Pattern Builder</Text>
              
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
              </TouchableOpacity>
            </View>
          )}
        </View>



        {/* UNIVERSAL SLIDERS FOOTER - Hidden in PRESETS */}
        {activeMode !== 'PRESETS' && (
          <View style={[styles.sceneSlidersContainer, { marginTop: 8, borderTopWidth: 1, borderTopColor: 'rgba(255,255,255,0.05)', paddingTop: 8, paddingBottom: 0 }]}>
            {/* Color Grid wrappers */}
            {!(activeMode === 'FIXED' && fixedSubMode === 'RBM') && (
              <View style={{ marginBottom: 4 }}>
                {/* Dynamic Selected Color Bar */}
                {!(activeMode === 'FIXED' && (fixedSubMode === 'MUSIC' || (fixedSubMode === 'PATTERN' && fixedPatternId !== 1))) && (() => {
                  const dynamicColor = selectedColor;
                  
                  return (
                    <TouchableOpacity 
                      activeOpacity={0.9}
                      onPress={() => {
                         // Send immediately if pressed just in case
                         const r = parseInt(dynamicColor.slice(1, 3), 16) || 255;
                         const g = parseInt(dynamicColor.slice(3, 5), 16) || 255;
                         const b = parseInt(dynamicColor.slice(5, 7), 16) || 255;
                         if (activeMode === 'FIXED' && fixedSubMode !== 'PATTERN') sendColor(r, g, b);
                      }}
                      style={{
                        width: '100%',
                        height: 14,
                        borderRadius: 7,
                        backgroundColor: dynamicColor,
                        justifyContent: 'center',
                        alignItems: 'center',
                        marginBottom: 4,
                        shadowColor: dynamicColor,
                        shadowOpacity: 0.8,
                        shadowRadius: 10,
                        shadowOffset: { width: 0, height: 0 },
                        elevation: 6,
                        borderWidth: 1,
                        borderColor: 'rgba(255,255,255,0.4)'
                      }}
                    >
                      <Text style={{ color: '#FFF', fontSize: 9, fontWeight: '800', letterSpacing: 2, textShadowColor: '#000', textShadowRadius: 4, textShadowOffset: {width: 0, height: 1} }}>
                        SELECTED COLOR
                      </Text>
                    </TouchableOpacity>
                  );
                })()}

                {/* Music Mode Split Color Tracker */}
                {(activeMode === 'FIXED' && fixedSubMode === 'MUSIC') && (() => {
                  const primHex = musicPrimaryColor;
                  const secHex = musicSecondaryColor;
                  
                  return (
                    <View style={{
                        flexDirection: 'row', width: '100%', height: 18, borderRadius: 9, marginBottom: 4,
                        borderWidth: 1, borderColor: 'rgba(255,255,255,0.4)', backgroundColor: 'transparent'
                    }}>
                        <TouchableOpacity 
                          activeOpacity={0.9}
                          onPress={() => setMusicColorFocus('PRIMARY')}
                          style={{ flex: 1, backgroundColor: primHex, justifyContent: 'center', alignItems: 'center', opacity: musicColorFocus === 'PRIMARY' ? 1.0 : 0.4, borderTopLeftRadius: 8, borderBottomLeftRadius: 8, shadowColor: primHex, shadowOpacity: 1, shadowRadius: 16, shadowOffset: { width: 0, height: 0 }, elevation: 12 }}
                        >
                          <Text style={{ color: '#FFFFFF', fontSize: 9, fontWeight: '900', letterSpacing: 1, textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 6 }}>SOUND COLUMN</Text>
                        </TouchableOpacity>
                        <TouchableOpacity 
                          activeOpacity={0.9}
                          onPress={() => setMusicColorFocus('SECONDARY')}
                          style={{ flex: 1, backgroundColor: secHex, justifyContent: 'center', alignItems: 'center', opacity: musicColorFocus === 'SECONDARY' ? 1.0 : 0.4, borderLeftWidth: 1, borderLeftColor: 'rgba(255,255,255,0.4)', borderTopRightRadius: 8, borderBottomRightRadius: 8, shadowColor: secHex, shadowOpacity: 1, shadowRadius: 16, shadowOffset: { width: 0, height: 0 }, elevation: 12 }}
                        >
                          <Text style={{ color: '#FFFFFF', fontSize: 9, fontWeight: '900', letterSpacing: 1, textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 6 }}>DROP COLOR</Text>
                        </TouchableOpacity>
                    </View>
                  );
                })()}

                {/* Fixed Pattern Mode Split Color Tracker */}
                {(activeMode === 'FIXED' && fixedSubMode === 'PATTERN' && fixedPatternId !== 1) && (
                    <View style={{
                        flexDirection: 'row', width: '100%', height: 18, borderRadius: 9, marginBottom: 4,
                        borderWidth: 1, borderColor: 'rgba(255,255,255,0.4)', backgroundColor: 'transparent'
                    }}>
                        <TouchableOpacity 
                          activeOpacity={0.9}
                          onPress={() => setFixedColorMode('FOREGROUND')}
                          style={{ flex: 1, backgroundColor: fixedFgColor, justifyContent: 'center', alignItems: 'center', opacity: fixedColorMode === 'FOREGROUND' ? 1.0 : 0.4, borderTopLeftRadius: 8, borderBottomLeftRadius: 8, shadowColor: fixedFgColor, shadowOpacity: 1, shadowRadius: 16, shadowOffset: { width: 0, height: 0 }, elevation: 12 }}
                        >
                          <Text style={{ color: '#FFFFFF', fontSize: 9, fontWeight: '900', letterSpacing: 1, textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 6 }}>FOREGROUND</Text>
                        </TouchableOpacity>
                        <TouchableOpacity 
                          activeOpacity={0.9}
                          onPress={() => setFixedColorMode('BACKGROUND')}
                          style={{ flex: 1, backgroundColor: fixedBgColor, justifyContent: 'center', alignItems: 'center', opacity: fixedColorMode === 'BACKGROUND' ? 1.0 : 0.4, borderLeftWidth: 1, borderLeftColor: 'rgba(255,255,255,0.4)', borderTopRightRadius: 8, borderBottomRightRadius: 8, shadowColor: fixedBgColor, shadowOpacity: 1, shadowRadius: 16, shadowOffset: { width: 0, height: 0 }, elevation: 12 }}
                        >
                          <Text style={{ color: '#FFFFFF', fontSize: 9, fontWeight: '900', letterSpacing: 1, textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 6 }}>BACKGROUND</Text>
                        </TouchableOpacity>
                    </View>
                )}

                {/* 9 Preset Colors Grid */}
                {!(activeMode === 'FIXED' && fixedSubMode === 'CAMERA') && (
                <View style={[styles.colorGrid, { paddingHorizontal: 0, flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }]}>
                  {[
                    '#FF0000', '#FF8000', '#FFFF00', '#00FF00', '#00FFFF', '#0000FF', '#800080', '#FF00FF', '#FFFFFF'
                  ].map((color, index) => {
                    let dynamicColor = selectedColor;
                    if (activeMode === 'FIXED' && fixedSubMode === 'PATTERN') {
                      dynamicColor = fixedColorMode === 'FOREGROUND' ? fixedFgColor : fixedBgColor;
                    } else if (activeMode === 'FIXED' && fixedSubMode === 'MUSIC') {
                      dynamicColor = musicColorFocus === 'PRIMARY' ? musicPrimaryColor : musicSecondaryColor;
                    }
                    
                    const isActive = typeof dynamicColor === 'string' && dynamicColor.toUpperCase() === color.toUpperCase();
                    return (
                      <TouchableOpacity 
                        key={index} 
                        onPress={() => {
                          const hueMap: {[key: string]: number} = {
                            '#FF0000': 0, '#FF8000': 30, '#FFFF00': 60, '#00FF00': 120, 
                            '#00FFFF': 180, '#0000FF': 240, '#800080': 280, '#FF00FF': 300, '#FFFFFF': 0
                          };
                          if (activeMode === 'FIXED') {
                            if (fixedSubMode === 'CANDLE') {
                               setSelectedColor(color);
                               if (hueMap[color] !== undefined) setFixedHue(hueMap[color]);
                               if (writeToDevice) {
                                   const rawR = parseInt(color.substring(1, 3), 16) || 255;
                                   const rawG = parseInt(color.substring(3, 5), 16) || 255;
                                   const rawB = parseInt(color.substring(5, 7), 16) || 255;
                                   let finalR = rawR; let finalG = rawG; let finalB = rawB;
                                   const sorting = devices && devices.length > 0 ? devices[0].sorting || 'GRB' : 'GRB';
                                   if (sorting === 'GRB') { finalR = rawG; finalG = rawR; }
                                   writeToDevice(ZenggeProtocol.setCandleMode(finalR, finalG, finalB, speed, brightness, candleAmplitude));
                               }
                            } else if (fixedSubMode === 'PATTERN') {
                               let newFg = fixedFgColor;
                               let newBg = fixedBgColor;
                               if (fixedColorMode === 'FOREGROUND') {
                                 newFg = color;
                                 setFixedFgColor(color);
                                 setSelectedColor(color);
                               } else {
                                 newBg = color;
                                 setFixedBgColor(color);
                                 setSelectedColor(color);
                               }
                               if (hueMap[color] !== undefined) setFixedHue(hueMap[color]);
                               applyFixedPattern(fixedPatternId, newFg, newBg);
                            } else if (fixedSubMode === 'MUSIC') {
                               if (musicColorFocus === 'PRIMARY') {
                                   setMusicPrimaryColor(color);
                                   if (hueMap[color] !== undefined) setMusicHue(hueMap[color]);
                                   handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, color, musicSecondaryColor);
                               } else {
                                   setMusicSecondaryColor(color);
                                   if (hueMap[color] !== undefined) setMusicSecondaryHue(hueMap[color]);
                                   handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, color);
                               }
                            } else {
                               setSelectedColor(color);
                               if (hueMap[color] !== undefined) setFixedHue(hueMap[color]);
                            }
                          } else {
                            setSelectedColor(color);
                            if (hueMap[color] !== undefined) setSelectedHue(hueMap[color]);
                            const r = parseInt(color.slice(1, 3), 16);
                            const g = parseInt(color.slice(3, 5), 16);
                            const b = parseInt(color.slice(5, 7), 16);
                            sendColor(r, g, b);
                          }
                        }}
                        style={[
                          { 
                            backgroundColor: color, 
                            width: 20, 
                            height: 20, 
                            borderRadius: 10,
                            shadowColor: color,
                            shadowOpacity: 1,
                            shadowRadius: 10,
                            shadowOffset: { width: 0, height: 0 },
                            elevation: 8,
                            margin: 2
                          },
                          isActive && { borderWidth: 2, borderColor: '#FFF' }
                        ]} 
                      />
                    );
                  })}
                </View>
                )}
              </View>
            )}

            {/* Old Color Focus Toggle for Music Mode has been moved above the color grid */}

            {/* Hue Slider */}
            {!(activeMode === 'FIXED' && (fixedSubMode === 'RBM' || fixedSubMode === 'CAMERA')) && (
              <View style={[styles.controlRow, { marginTop: 0, height: 32, flexShrink: 0 }]}>
                <CustomSlider 
                  gradientTrack={true}
                  value={activeMode === 'FIXED' ? (fixedSubMode === 'MUSIC' ? (musicColorFocus === 'PRIMARY' ? musicHue : musicSecondaryHue) : fixedHue) : selectedHue}
                  onValueChange={(hue) => {
                    if (activeMode === 'FIXED') {
                      if (fixedSubMode === 'CANDLE') {
                        setFixedHue(hue);
                        const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                        const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0")).join("");
                        const hex = rgb2hex(f(5), f(3), f(1));
                        setSelectedColor(hex);
                      } else if (fixedSubMode === 'PATTERN') {
                        setFixedHue(hue);
                        const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                        const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0")).join("");
                        const hex = rgb2hex(f(5), f(3), f(1));
                        if (fixedColorMode === 'FOREGROUND') {
                            setFixedFgColor(hex);
                            setSelectedColor(hex);
                        } else {
                            setFixedBgColor(hex);
                            setSelectedColor(hex);
                        }
                      } else if (fixedSubMode === 'MUSIC') {
                         const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                         const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0").toUpperCase()).join("");
                         const hex = rgb2hex(f(5), f(3), f(1));
                         if (musicColorFocus === 'PRIMARY') {
                            setMusicPrimaryColor(hex);
                            setMusicHue(hue);
                         } else {
                            setMusicSecondaryColor(hex);
                            setMusicSecondaryHue(hue);
                         }
                      } else {
                        setFixedHue(hue);
                        const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                        const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0")).join("");
                        const hex = rgb2hex(f(5), f(3), f(1));
                        setSelectedColor(hex);
                      }
                    } else if (activeMode === 'MUSIC') {
                      if (musicColorFocus === 'PRIMARY') setMusicHue(hue);
                      else setMusicSecondaryHue(hue);
                    } else {
                      setSelectedHue(hue);
                      const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                      const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0").toUpperCase()).join("");
                      setSelectedColor(rgb2hex(f(5), f(3), f(1)));
                    }
                  }}
                  onSlidingComplete={(hue) => {
                    if (activeMode === 'FIXED') {
                      if (fixedSubMode === 'CANDLE') {
                         if (writeToDevice) {
                             const rawR = parseInt(selectedColor.substring(1, 3), 16) || 255;
                             const rawG = parseInt(selectedColor.substring(3, 5), 16) || 255;
                             const rawB = parseInt(selectedColor.substring(5, 7), 16) || 255;
                             let finalR = rawR; let finalG = rawG; let finalB = rawB;
                             const sorting = devices && devices.length > 0 ? devices[0].sorting || 'GRB' : 'GRB';
                             if (sorting === 'GRB') { finalR = rawG; finalG = rawR; }
                             writeToDevice(ZenggeProtocol.setCandleMode(finalR, finalG, finalB, speed, brightness, candleAmplitude));
                         }
                      } else if (fixedSubMode === 'PATTERN') {
                         applyFixedPattern(fixedPatternId, fixedFgColor, fixedBgColor);
                      } else if (fixedSubMode === 'MUSIC') {
                         const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                         const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0").toUpperCase()).join("");
                         const hex = rgb2hex(f(5), f(3), f(1));
                         if (musicColorFocus === 'PRIMARY') {
                            handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, hex, musicSecondaryColor, musicMatrixStyle);
                         } else {
                            handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, hex, musicMatrixStyle);
                         }
                      }
                     } else if (activeMode === 'MUSIC') {
                       // Deprecated top route
                     } else {
                      const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                      const r = Math.round(f(5) * 255);
                      const g = Math.round(f(3) * 255);
                      const b = Math.round(f(1) * 255);
                      sendColor(r, g, b);
                    }
                  }}
                  minimumValue={0}
                  maximumValue={360}
                  style={{ position: 'absolute', width: '100%', height: 32 }}
                />
              </View>
            )}

            {/* Brightness Slider - Hidden in PRESETS and CAMERA (Presets have their own Brightness logic mapped inside the component block, while Camera explicitly forces raw camera detection bounds) */}
            {!(activeMode === 'FIXED' && fixedSubMode === 'CAMERA') && (
            <View style={[styles.controlRow, { marginTop: 8, marginBottom: 4, flexShrink: 0, minHeight: 40 }]}>
              <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center' }}>
                <MaterialCommunityIcons name="white-balance-sunny" size={22} color={Colors.textMuted} style={{ marginRight: 12, width: 30, textAlign: 'center', flexShrink: 0 }} />

                <CustomSlider 
                  value={brightness}
                  onValueChange={setBrightness}
                  minimumValue={0}
                  maximumValue={100}
                  style={{ flex: 1 }}
                  onSlidingComplete={(val) => {
                    if (writeToDevice) {
                      if (activeMode === 'FIXED' && fixedSubMode === 'MUSIC') {
                        handleMusicChange(musicPatternId, micSensitivity, val, micSource);
                      } else {
                        if (activeMode === 'FIXED') {
                          if (fixedSubMode === 'PATTERN') {
                            applyFixedPattern(fixedPatternId, fixedFgColor, fixedBgColor, speed, val);
                          } else if (fixedSubMode === 'MULTI') {
                            const factor = val / 100;
                            const sortIdx = hwSettings?.colorSorting ?? 2;
                            const rgbColors = multiColors.map(h => {
                                const rawR = Math.round((parseInt(h.slice(1,3), 16) || 0) * factor);
                                const rawG = Math.round((parseInt(h.slice(3,5), 16) || 0) * factor);
                                const rawB = Math.round((parseInt(h.slice(5,7), 16) || 0) * factor);
                                return ZenggeProtocol.applyColorSorting(rawR, rawG, rawB, sortIdx);
                            });
                            const pts = hwSettings?.ledPoints || points || 16;
                            const appliedLength = Math.min(multiLength, Math.max(1, Math.floor(pts / (hwSettings?.segments || 1))));
                            writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, appliedLength, multiTransition, speed));
                          } else if (fixedSubMode === 'CANDLE') {
                            const rawR = parseInt(selectedColor.substring(1, 3), 16) || 255;
                            const rawG = parseInt(selectedColor.substring(3, 5), 16) || 255;
                            const rawB = parseInt(selectedColor.substring(5, 7), 16) || 255;
                            const sortIdx = hwSettings?.colorSorting ?? 2;
                            const { r: finalR, g: finalG, b: finalB } = ZenggeProtocol.applyColorSorting(rawR, rawG, rawB, sortIdx);
                            writeToDevice(ZenggeProtocol.setCandleMode(finalR, finalG, finalB, speed, val, candleAmplitude));
                          } else if (fixedSubMode === 'RBM') {
                            if (selectedPatternId === 100) {
                              applyEmergencyPattern(speed, val);
                            } else {
                              writeToDevice(ZenggeProtocol.setCustomRbm(selectedPatternId, speed, val));
                            }
                          }
                        } else {
                          // Standard scaled color for other modes
                          const factor = val / 100;
                          const hex = activeMode === 'MULTICOLOR' ? '#FFFFFF' : selectedColor;
                          const r = Math.round(parseInt(hex.slice(1, 3), 16) * factor);
                          const g = Math.round(parseInt(hex.slice(3, 5), 16) * factor);
                          const b = Math.round(parseInt(hex.slice(5, 7), 16) * factor);
                          sendColor(r, g, b);
                        }
                      }
                    }
                  }}
                />
              </View>
            </View>
            )}

            {/* Speed Slider - Hidden in MUSIC, and CAMERA */}
            {!(activeMode === 'FIXED' && (fixedSubMode === 'MUSIC' || fixedSubMode === 'CAMERA')) && (
              <View style={[styles.controlRow, { marginTop: 4, marginBottom: 4, flexShrink: 0, minHeight: 40 }]}>
                <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center' }}>
                  <MaterialCommunityIcons name="engine-outline" size={22} color={Colors.textMuted} style={{ marginRight: 12, width: 30, textAlign: 'center', flexShrink: 0 }} />
                  <CustomSlider 
                    value={speed}
                    onValueChange={setSpeed}
                    onSlidingComplete={(val) => {
                      if (writeToDevice) {
                        if (activeMode === 'FIXED') {
                          if (fixedSubMode === 'PATTERN') {
                            applyFixedPattern(fixedPatternId, fixedFgColor, fixedBgColor, val);
                          } else if (fixedSubMode === 'MULTI') {
                            const factor = brightness / 100;
                            const sortIdx = hwSettings?.colorSorting ?? 2;
                            const rgbColors = multiColors.map(h => {
                                const rawR = Math.round((parseInt(h.slice(1,3), 16) || 0) * factor);
                                const rawG = Math.round((parseInt(h.slice(3,5), 16) || 0) * factor);
                                const rawB = Math.round((parseInt(h.slice(5,7), 16) || 0) * factor);
                                return ZenggeProtocol.applyColorSorting(rawR, rawG, rawB, sortIdx);
                            });
                            const pts = hwSettings?.ledPoints || points || 16;
                            const appliedLength = Math.min(multiLength, Math.max(1, Math.floor(pts / (hwSettings?.segments || 1))));
                            writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, appliedLength, multiTransition, val));
                          } else if (fixedSubMode === 'CANDLE') {
                            const rawR = parseInt(selectedColor.substring(1, 3), 16) || 255;
                            const rawG = parseInt(selectedColor.substring(3, 5), 16) || 255;
                            const rawB = parseInt(selectedColor.substring(5, 7), 16) || 255;
                            const sortIdx = hwSettings?.colorSorting ?? 2;
                            const { r: finalR, g: finalG, b: finalB } = ZenggeProtocol.applyColorSorting(rawR, rawG, rawB, sortIdx);
                            writeToDevice(ZenggeProtocol.setCandleMode(finalR, finalG, finalB, val, brightness, candleAmplitude));
                          } else if (fixedSubMode === 'RBM') {
                            if (selectedPatternId === 100) {
                              applyEmergencyPattern(val, brightness);
                            } else {
                              writeToDevice(ZenggeProtocol.setCustomRbm(selectedPatternId, val, brightness));
                            }
                          }
                        }
                      }
                    }}
                    minimumValue={0}
                    maximumValue={100}
                    style={{ flex: 1 }}
                  />
                </View>
              </View>
            )}

            {/* Sensitivity Slider - Visible ONLY in MUSIC */}
            {(activeMode === 'FIXED' && fixedSubMode === 'MUSIC') && (
              <View style={[styles.controlRow, { marginTop: 4, marginBottom: 4, flexShrink: 0, minHeight: 40 }]}>
                <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center' }}>
                  <MaterialCommunityIcons name="microphone-outline" size={22} color={Colors.textMuted} style={{ marginRight: 12, width: 30, textAlign: 'center', flexShrink: 0 }} />
                  <CustomSlider 
                    value={micSensitivity}
                    onValueChange={setMicSensitivity}
                    onSlidingComplete={(val) => {
                      handleMusicChange(musicPatternId, val, brightness, micSource, musicPrimaryColor, musicSecondaryColor, musicMatrixStyle);
                    }}
                    minimumValue={0}
                    maximumValue={100}
                    style={{ flex: 1 }}
                  />
                </View>
              </View>
            )}
          </View>
        )}
      </View>

        {/* THE FLOATING DOCK */}
        <View style={{ marginBottom: 4 }}>
          <View style={[styles.floatingDock, { marginBottom: 0 }]}>
            {[
              { id: 'HOME', icon: 'home-outline' },
              { id: 'PRESETS', icon: 'cards-heart-outline' },
              { id: 'MULTI', icon: 'palette' },
              { id: 'RBM', icon: 'animation-play' },
              { id: 'MUSIC', icon: 'music' },
              { id: 'CAMERA', icon: 'camera' }
            ].map(dockItem => {
              const isActive = (activeMode === 'FIXED' && (fixedSubMode === dockItem.id || (dockItem.id === 'MULTI' && (fixedSubMode === 'PATTERN' || fixedSubMode === 'CANDLE')))) || activeMode === dockItem.id;
              return (
                <TouchableOpacity
                  key={dockItem.id}
                  onPress={() => {
                     if (dockItem.id === 'HOME') {
                         if (onDisconnect) onDisconnect();
                     } else if (dockItem.id === 'PRESETS') { 
                         setActiveMode('PRESETS'); 
                     } else {
                        setActiveMode('FIXED');
                        if (dockItem.id === 'MULTI') {
                           setFixedSubMode('PATTERN');
                        } else {
                           setFixedSubMode(dockItem.id as any);
                        }
                     }
                  }}
                  style={[styles.dockIconCont, isActive && styles.dockIconActive]}
                >
                  <MaterialCommunityIcons 
                    name={dockItem.icon as any} 
                    size={22} 
                    color={isActive ? '#000000' : Colors.textMuted} 
                  />
                </TouchableOpacity>
              );
            })}
          </View>
        </View>
        {/* Quick Preset Prompt Modal */}
        <Modal visible={isQuickPromptVisible} transparent animationType="fade">
          <View style={{ flex: 1, backgroundColor: 'rgba(0,0,0,0.85)', justifyContent: 'center', alignItems: 'center', padding: 20 }}>
            <View style={{ backgroundColor: Colors.surface, padding: 24, borderRadius: 20, width: '100%', maxWidth: 340, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}>
              <Text style={{ color: '#FFF', fontSize: 18, fontWeight: 'bold', marginBottom: 12, textAlign: 'center' }}>
                 {quickPromptTargetIndex === -1 ? 'Save Quick Preset' : 'Edit Quick Preset'}
              </Text>
              <Text style={{ color: Colors.textMuted, fontSize: 14, marginBottom: 20, textAlign: 'center' }}>
                 {quickPromptTargetIndex === -1 ? 'Name your new preset to store it in the Quick bar.' : 'Rename your preset or delete it from the bar.'}
              </Text>
              <TextInput
                style={{ backgroundColor: 'rgba(255,255,255,0.05)', color: '#FFF', padding: 12, borderRadius: 8, fontSize: 16, marginBottom: 24, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}
                placeholder="Preset Name..."
                placeholderTextColor="rgba(255,255,255,0.3)"
                value={quickPromptName}
                onChangeText={setQuickPromptName}
                autoFocus
              />
              <View style={{ flexDirection: 'row', gap: 12 }}>
                {quickPromptTargetIndex !== -1 && (
                   <TouchableOpacity style={{ flex: 1, padding: 14, borderRadius: 10, backgroundColor: 'rgba(255,0,0,0.3)' }} onPress={() => {
                       const newArr = [...quickPresets];
                       newArr.splice(quickPromptTargetIndex, 1);
                       setQuickPresets(newArr);
                       AsyncStorage.setItem('@Sk8lytz_QuickPresets', JSON.stringify(newArr));
                       setIsQuickPromptVisible(false);
                   }}>
                     <Text style={{ color: '#FFF', textAlign: 'center', fontWeight: 'bold' }}>Delete</Text>
                   </TouchableOpacity>
                )}
                {quickPromptTargetIndex === -1 && (
                   <TouchableOpacity style={{ flex: 1, padding: 14, borderRadius: 10, backgroundColor: 'rgba(255,255,255,0.05)' }} onPress={() => setIsQuickPromptVisible(false)}>
                     <Text style={{ color: '#FFF', textAlign: 'center', fontWeight: 'bold' }}>Cancel</Text>
                   </TouchableOpacity>
                )}
                <TouchableOpacity style={{ flex: 1, padding: 14, borderRadius: 10, backgroundColor: Colors.primary }} onPress={() => {
                    const newArr = [...quickPresets];
                    const safeName = quickPromptName.trim() || 'Preset';
                    if (quickPromptTargetIndex === -1) {
                        newArr.push({ name: safeName, colors: multiColors, type: multiTransition });
                    } else {
                        newArr[quickPromptTargetIndex].name = safeName;
                    }
                    setQuickPresets(newArr);
                    AsyncStorage.setItem('@Sk8lytz_QuickPresets', JSON.stringify(newArr));
                    setIsQuickPromptVisible(false);
                }}>
                  <Text style={{ color: '#000', textAlign: 'center', fontWeight: 'bold' }}>Save</Text>
                </TouchableOpacity>
              </View>
            </View>
          </View>
        </Modal>

        {/* Favorite Prompt Modal */}
        <Modal visible={isFavPromptVisible} transparent animationType="fade">
          <View style={{ flex: 1, backgroundColor: 'rgba(0,0,0,0.85)', justifyContent: 'center', alignItems: 'center', padding: 20 }}>
            <View style={{ backgroundColor: Colors.surface, padding: 24, borderRadius: 20, width: '100%', maxWidth: 340, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}>
              <Text style={{ color: '#FFF', fontSize: 18, fontWeight: 'bold', marginBottom: 12, textAlign: 'center' }}>
                 {favPromptTargetId ? 'Edit Favorite' : 'Save Favorite'}
              </Text>
              <Text style={{ color: Colors.textMuted, fontSize: 14, marginBottom: 20, textAlign: 'center' }}>
                 {favPromptTargetId ? 'Rename your preset or delete it.' : 'Name your preset. Leave blank to use the default name.'}
              </Text>
              <TextInput
                style={{ backgroundColor: 'rgba(255,255,255,0.05)', color: '#FFF', padding: 12, borderRadius: 8, fontSize: 16, marginBottom: 24, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}
                placeholder="Custom Preset Name..."
                placeholderTextColor="rgba(255,255,255,0.3)"
                value={favPromptName}
                onChangeText={setFavPromptName}
                autoFocus
              />
              <View style={{ flexDirection: 'row', gap: 12 }}>
                {favPromptTargetId && (
                   <TouchableOpacity style={{ flex: 1, padding: 14, borderRadius: 10, backgroundColor: 'rgba(255,0,0,0.3)' }} onPress={() => { deleteFavorite(favPromptTargetId); setIsFavPromptVisible(false); setFavPromptTargetId(null); }}>
                     <Text style={{ color: '#FFF', textAlign: 'center', fontWeight: 'bold' }}>Delete</Text>
                   </TouchableOpacity>
                )}
                {(!favPromptTargetId) && (
                   <TouchableOpacity style={{ flex: 1, padding: 14, borderRadius: 10, backgroundColor: 'rgba(255,255,255,0.05)' }} onPress={() => setIsFavPromptVisible(false)}>
                     <Text style={{ color: '#FFF', textAlign: 'center', fontWeight: 'bold' }}>Cancel</Text>
                   </TouchableOpacity>
                )}
                <TouchableOpacity style={{ flex: 1, padding: 14, borderRadius: 10, backgroundColor: Colors.primary }} onPress={handleConfirmSaveFavorite}>
                  <Text style={{ color: '#000', textAlign: 'center', fontWeight: 'bold' }}>Save</Text>
                </TouchableOpacity>
              </View>
            </View>
          </View>
        </Modal>

      </View>
  );
}


const createStyles = (Colors: import('../theme/theme').ThemePalette) => StyleSheet.create({
  container: {
    flex: 1,
    paddingHorizontal: 0,
    paddingBottom: 0,
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
    flex: 1,
    padding: 10,
    backgroundColor: Colors.isDark ? 'rgba(21, 25, 40, 0.7)' : Colors.surface,
    borderRadius: Layout.borderRadius + 4,
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255, 255, 255, 0.1)' : 'rgba(0,0,0,0.08)',
  },
  modesContainer: {
    flexDirection: 'row',
    justifyContent: 'center',
    gap: 6,
    marginBottom: 12,
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
    flex: 1,
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
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
    alignContent: 'flex-start',
    gap: 6
  },
  presetCard: {
    width: '48%',
    minHeight: 80,
    padding: 6,
    backgroundColor: Colors.isDark ? 'rgba(0,0,0,0.6)' : 'rgba(0,0,0,0.04)',
    borderRadius: 16,
    borderWidth: 1.5,
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.25,
    shadowRadius: 10,
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
    color: Colors.isDark ? '#FFF' : '#000',
    fontWeight: 'bold',
  },
  musicToggleText: {
    color: Colors.textMuted,
    fontSize: 11,
    fontWeight: '700',
  },
  musicModeIndicator: {
    width: '100%',
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
    padding: 8,
    borderRadius: 12,
    width: 90,
  },
  micBtnActive: {
    backgroundColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.05)',
  },
  micIconCircle: {
    width: 48,
    height: 48,
    borderRadius: 24,
    backgroundColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.05)',
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 6,
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255,255,255,0.1)' : 'rgba(0,0,0,0.1)',
  },
  micIconText: {
    fontSize: 24,
    color: Colors.textMuted,
  },
  micSubText: {
    fontSize: 9,
    color: Colors.textMuted,
    textAlign: 'center',
    textTransform: 'uppercase',
    fontWeight: '600',
  },
  playButtonMain: {
    width: 44,
    height: 44,
    borderRadius: 22,
    borderWidth: 2,
    borderColor: Colors.primary,
    alignItems: 'center',
    justifyContent: 'center',
  },
  playIconInner: {
    width: 34,
    height: 34,
    borderRadius: 17,
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
  },
  floatingDock: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: Colors.isDark ? 'rgba(8, 10, 16, 0.98)' : 'rgba(255, 255, 255, 0.98)',
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(0, 240, 255, 0.35)' : 'rgba(0, 200, 255, 0.4)',
    borderRadius: 30,
    paddingVertical: 10,
    paddingHorizontal: 20,
    marginHorizontal: 16,
    marginBottom: 8,
    marginTop: 8,
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 8 },
    shadowOpacity: 0.6,
    shadowRadius: 20,
    elevation: 15
  },
  dockIconCont: {
    width: 44,
    height: 44,
    borderRadius: 22,
    backgroundColor: Colors.isDark ? 'rgba(255,255,255,0.03)' : 'rgba(0,0,0,0.05)',
    alignItems: 'center',
    justifyContent: 'center',
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.08)',
  },
  dockIconActive: {
    backgroundColor: Colors.primary,
    borderColor: Colors.primary,
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.9,
    shadowRadius: 12,
    elevation: 8,
    transform: [{ scale: 1.15 }]
  },
  dockActiveText: {
    color: Colors.primary,
    fontFamily: 'Righteous',
    fontSize: 18,
    textAlign: 'center',
    letterSpacing: 4,
    textTransform: 'uppercase',
    textShadowColor: Colors.primary,
    textShadowOffset: { width: 0, height: 0 },
    textShadowRadius: 24,
    opacity: 1.0,
    marginTop: 4,
  }
});
