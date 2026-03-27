import React, { useState, useRef, useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView, FlatList, Platform, Modal, TextInput } from 'react-native';
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
    <View style={{ flex: 1, marginRight: 8, overflow: 'hidden' }}>
      <View style={{ flexDirection: 'row', gap: 4 }}>
        {displayedDots.map((c, i) => (
          <View 
             key={i} 
             style={{ 
               width: 8, 
               height: 8, 
               borderRadius: 4, 
               backgroundColor: c,
               marginLeft: i > 0 && i % dotsPerSegment === 0 ? 12 : 0 
             }} 
          />
        ))}
      </View>
    </View>
  );
};


interface Sk8lytzControllerProps {
  lockedProduct?: ProductType;
  isPaired?: boolean;
  points?: number;
  devices?: any[];
  onLongPressDevice?: (device: any) => void;
  writeToDevice?: (payload: number[]) => Promise<void>;
  isPoweredOn?: boolean;
}

export default function Sk8lytzController({ lockedProduct, isPaired, points, devices, onLongPressDevice, writeToDevice: parentWriteToDevice, isPoweredOn = true }: Sk8lytzControllerProps) {
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
  const [micSensitivity, setMicSensitivity] = useState<number>(50);
  const [musicHue, setMusicHue] = useState(180);
  const [recording, setRecording] = useState<any>(null); // Use any for Recording to avoid version-specific type issues
  const [audioMagnitude, setAudioMagnitude] = useState<number>(0);
  const magnitudeInterval = useRef<NodeJS.Timeout | null>(null);

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

  // Favorites Array
  const [favorites, setFavorites] = useState<any[]>([]);

  const [isFavPromptVisible, setIsFavPromptVisible] = useState(false);
  const [favPromptName, setFavPromptName] = useState('');

  const handleSaveFavoriteClick = () => {
     let defaultName = '';
     try { defaultName = currentStatusText || `${activeMode} Preset`; } catch(e) { defaultName = `${activeMode} Preset`; }
     setFavPromptName(defaultName);
     setIsFavPromptVisible(true);
  };

  const handleConfirmSaveFavorite = () => {
     let defaultName = '';
     try { defaultName = currentStatusText || `${activeMode} Preset`; } catch(e) { defaultName = `${activeMode} Preset`; }
     const name = favPromptName.trim() || defaultName;
     const newFav = {
        id: Date.now().toString(),
        name,
        mode: activeMode,
        color: selectedColor,
        patternId: activeMode === 'MUSIC' ? musicPatternId : (activeMode === 'FIXED' ? fixedPatternId : selectedPatternId),
        speed,
        brightness,
        fixedColorMode, fixedFgColor, fixedBgColor, fixedHue,
        multiColors, multiTransition, multiLength,
        candleAmplitude
     };
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

  const loadFavorite = (fav: any) => {
     setActiveMode(fav.mode);
     setSpeed(fav.speed);
     setBrightness(fav.brightness);
     if (fav.color) setSelectedColor(fav.color);
     
     if (fav.mode === 'FIXED') {
        setFixedPatternId(fav.patternId);
        setFixedColorMode(fav.fixedColorMode);
        setFixedFgColor(fav.fixedFgColor);
        setFixedBgColor(fav.fixedBgColor);
        applyFixedPattern(fav.patternId, fav.fixedFgColor, fav.fixedBgColor, fav.speed, fav.brightness);
     } else if (fav.mode === 'RBM') {
        setSelectedPatternId(fav.patternId);
        if (writeToDevice) writeToDevice(ZenggeProtocol.setCustomRbm(fav.patternId, fav.speed, fav.brightness));
     } else if (fav.mode === 'MUSIC') {
        setMusicPatternId(fav.patternId);
        handleMusicChange(fav.patternId, micSensitivity, fav.brightness, micSource);
     } else if (fav.mode === 'MULTICOLOR') {
        setMultiColors(fav.multiColors);
        setMultiTransition(fav.multiTransition);
        setMultiLength(fav.multiLength);
        if (writeToDevice) {
           const rgbColors = fav.multiColors.map((h: string) => ({r: parseInt(h.slice(1,3),16)||0, g: parseInt(h.slice(3,5),16)||0, b: parseInt(h.slice(5,7),16)||0}));
           writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, fav.multiLength, fav.multiTransition, fav.speed));
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
        const sorting = devices && devices.length > 0 ? devices[0].sorting || 'GRB' : 'GRB';
        const rawR = parseInt(selectedColor.slice(1, 3), 16) || 255;
        const rawG = parseInt(selectedColor.slice(3, 5), 16) || 255;
        const rawB = parseInt(selectedColor.slice(5, 7), 16) || 255;
        let finalR = rawR; let finalG = rawG; let finalB = rawB;
        if (sorting === 'GRB') { finalR = rawG; finalG = rawR; }
        await writeToDevice(ZenggeProtocol.setCandleMode(finalR, finalG, finalB, speed, brightness, candleAmplitude));
    } else {
        // Solid fallback explicitly forced to length=10, transitionType=1 to stop physical node scrambling/jumping. 
        const colors = Array(10).fill({ r, g, b });
        await writeToDevice(ZenggeProtocol.setMultiColor(colors, 100, 1, 1));
    }
  };

  /** Helper to parse a hex string array into GRB-sorted valid hardware RGB array */
  const generateSortedColors = (hexArray: string[]) => {
      const sorting = devices && devices.length > 0 ? devices[0].sorting || 'GRB' : 'GRB';
      return hexArray.map(h => {
          const r = parseInt(h.slice(1,3), 16) || 0;
          const g = parseInt(h.slice(3,5), 16) || 0;
          const b = parseInt(h.slice(5,7), 16) || 0;
          return sorting === 'GRB' ? { r: g, g: r, b } : { r, g, b };
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
    const fgRgb = { 
      r: Math.round(parseInt(fg.slice(1, 3), 16) * factor), 
      g: Math.round(parseInt(fg.slice(3, 5), 16) * factor), 
      b: Math.round(parseInt(fg.slice(5, 7), 16) * factor) 
    };
    const bgRgb = { 
      r: Math.round(parseInt(bg.slice(1, 3), 16) * factor), 
      g: Math.round(parseInt(bg.slice(3, 5), 16) * factor), 
      b: Math.round(parseInt(bg.slice(5, 7), 16) * factor) 
    };

    if (patternId === 1) {
      const forcedSpeed = currentSpeed > 0 ? currentSpeed : 100;
      const solidColors = Array(10).fill({ r: fgRgb.r, g: fgRgb.g, b: fgRgb.b }); // Buffer stabilized length >= 10
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
      let arr: any[] = [];
      if (patternId === 2) arr = [fgRgb, bgRgb, bgRgb, bgRgb, bgRgb, bgRgb, bgRgb, bgRgb];
      if (patternId === 3) arr = [fgRgb, {r: Math.floor(fgRgb.r*0.5), g: Math.floor(fgRgb.g*0.5), b: Math.floor(fgRgb.b*0.5)}, {r: Math.floor(fgRgb.r*0.2), g: Math.floor(fgRgb.g*0.2), b: Math.floor(fgRgb.b*0.2)}, bgRgb, bgRgb, bgRgb];
      if (patternId === 4) arr = [fgRgb, fgRgb, fgRgb, fgRgb, bgRgb, bgRgb, bgRgb, bgRgb];
      if (patternId === 5) arr = [fgRgb, fgRgb, bgRgb, bgRgb];
      if (patternId === 9) arr = [bgRgb, bgRgb, fgRgb, fgRgb, bgRgb, bgRgb];
      if (patternId === 10) arr = [fgRgb, bgRgb, bgRgb, bgRgb, bgRgb, fgRgb];
      
      // CRITICAL HARDWARE FIX: Arrays smaller than 10 trigger hardware glitches. 
      // Safely multiply the base array chunk footprint until length hits native safe limit >= 10.
      while (arr.length > 0 && arr.length < 10) {
        arr = [...arr, ...arr];
      }
      
      // Send with TransitionType 0x00 (Static Flow/Marquee) to actually animate across the matrix
      writeToDevice(ZenggeProtocol.setMultiColor(arr, currentSpeed, 1, 0));
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
  const [musicColorFocus, setMusicColorFocus] = useState<'PRIMARY' | 'SECONDARY'>('PRIMARY');
  const [musicSetting, setMusicSetting] = useState<'SENSITIVITY' | 'BRIGHTNESS'>('SENSITIVITY');

  const [fixedPatternId, setFixedPatternId] = useState<number>(1);
  const [fixedColorMode, setFixedColorMode] = useState<'FOREGROUND' | 'BACKGROUND'>('FOREGROUND');
  const [fixedFgColor, setFixedFgColor] = useState<string>('#00FF00');
  const [fixedBgColor, setFixedBgColor] = useState<string>('#000000');
  const [fixedHue, setFixedHue] = useState<number>(120);

  // -- App Microphone Logic --
  useEffect(() => {
    if (activeMode === 'MUSIC' && micSource === 'APP' && isPoweredOn) {
      startRecording();
    } else {
      stopRecording();
    }
    return () => {
      stopRecording();
    };
  }, [activeMode, micSource, isPoweredOn]);

  // -- Analytics Logging --
  const logTimers = useRef<Record<string, ReturnType<typeof setTimeout>>>({});

  // Mode change logger
  useEffect(() => {
    AppLogger.log('MODE_CHANGED', { mode: activeMode });
  }, [activeMode]);

  // Pattern change logger (PRESETS mode)
  useEffect(() => {
    const name = getRbmPatternName(selectedPatternId);
    AppLogger.log('PATTERN_CHANGED', { 
      pattern: `ID:${selectedPatternId}`, 
      name,
      mode: activeMode,
      color: selectedColor 
    });
  }, [selectedPatternId]);

  // Color change logger
  useEffect(() => {
    AppLogger.log('COLOR_CHANGED', { hex: selectedColor });
  }, [selectedColor]);

  // Brightness change logger (debounced 600ms)
  useEffect(() => {
    clearTimeout(logTimers.current['brightness']);
    logTimers.current['brightness'] = setTimeout(() => {
      AppLogger.log('BRIGHTNESS_CHANGED', { value: brightness, mode: activeMode });
    }, 600);
  }, [brightness]);

  // Speed change logger (debounced 600ms)
  useEffect(() => {
    clearTimeout(logTimers.current['speed']);
    logTimers.current['speed'] = setTimeout(() => {
      AppLogger.log('SPEED_CHANGED', { value: speed, mode: activeMode });
    }, 600);
  }, [speed]);

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
          if (parsed.activeMode) setActiveMode(parsed.activeMode);
          if (parsed.selectedColor) setSelectedColor(parsed.selectedColor);
          if (parsed.selectedPatternId) setSelectedPatternId(parsed.selectedPatternId);
          if (parsed.brightness !== undefined) setBrightness(parsed.brightness);
          else setBrightness(90);
          if (parsed.speed !== undefined) setSpeed(parsed.speed);
          else setSpeed(50);
          if (parsed.micSensitivity !== undefined) setMicSensitivity(parsed.micSensitivity);
          if (parsed.musicHue !== undefined) setMusicHue(parsed.musicHue);
          if (parsed.musicSecondaryHue !== undefined) setMusicSecondaryHue(parsed.musicSecondaryHue);
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
  }, []);

  React.useEffect(() => {
    const stateBlob = {
      activeMode, selectedColor, selectedPatternId, brightness, speed,
      micSensitivity, musicHue, musicSecondaryHue, musicMatrixStyle, musicPatternId, micSource, musicSetting,
      fixedPatternId, fixedColorMode, fixedFgColor, fixedBgColor, fixedHue, candleAmplitude
    };
    AsyncStorage.setItem('@Sk8lytz_ControllerState', JSON.stringify(stateBlob)).catch(() => {});
  }, [
    activeMode, selectedColor, selectedPatternId, brightness, speed, 
    micSensitivity, musicHue, musicSecondaryHue, musicMatrixStyle, musicPatternId, micSource, musicSetting,
    fixedPatternId, fixedColorMode, fixedFgColor, fixedBgColor, fixedHue, candleAmplitude
  ]);

  const handleMusicChange = (
    patternId: number = musicPatternId,
    sens: number = micSensitivity,
    bright: number = brightness,
    src: 'APP' | 'DEVICE' = micSource,
    currentHue: number = musicHue,
    secondHue: number = musicSecondaryHue,
    matrix: number = musicMatrixStyle
  ) => {
    if (!writeToDevice) return;
    
    const isDeviceMic = src === 'DEVICE';
    
    const f1 = (n: number, k = (n + currentHue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
    const c1 = { r: Math.round(f1(5) * 255), g: Math.round(f1(3) * 255), b: Math.round(f1(1) * 255) };
    
    const f2 = (n: number, k = (n + secondHue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
    const c2 = { r: Math.round(f2(5) * 255), g: Math.round(f2(3) * 255), b: Math.round(f2(1) * 255) };
    
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
          mode={activeMode === 'FIXED' ? (fixedSubMode === 'MULTI' ? 'MULTICOLOR' : (fixedSubMode === 'CANDLE' ? 'CANDLE' : (fixedSubMode === 'PATTERN' ? 'RBM' : (fixedSubMode === 'MUSIC' ? 'MUSIC' : (fixedSubMode === 'CAMERA' ? 'CAMERA' : 'FIXED'))))) : activeMode} 
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
          multiColors={multiColors}
          multiTransition={multiTransition}
        />
      </View>
      </View>

      <View style={styles.controlsContainer}>
        <View style={styles.modesContainer}>
          {modes.map((mode: any) => (
            <TouchableOpacity 
              key={mode.id}
              style={[
                styles.modePill, 
                { 
                  flex: 1,
                  height: 48, 
                  paddingVertical: 4, 
                  paddingHorizontal: 2, 
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
        </View>

        <ScrollView style={styles.activeModeContainer} contentContainerStyle={{ paddingBottom: 40 }} showsVerticalScrollIndicator={false}>
          {activeMode === 'PRESETS' && (
            <View style={{ marginBottom: 8 }}>
              <Text style={[Typography.title, isDark && { color: '#FFF' }]}>Your Saved Favorites</Text>
              
              {favorites.length === 0 && (
                 <Text style={{ color: Colors.textMuted, fontSize: 13, marginBottom: 12 }}>
                    You have no saved favorites. Click the heart icon on any pattern in the Visualizer to save it here!
                 </Text>
              )}
              
              <View style={styles.presetContainer}>
                 {favorites.map((fav, idx) => (
                  <TouchableOpacity 
                    key={fav.id}
                    style={[styles.presetCard, { borderColor: Colors.primary }]}
                    onPress={() => loadFavorite(fav)}
                  >
                    <TouchableOpacity 
                       style={{ position: 'absolute', right: 12, top: 12, zIndex: 10 }} 
                       onPress={() => deleteFavorite(fav.id)}
                    >
                       <MaterialCommunityIcons name="trash-can-outline" size={20} color={Colors.textMuted} />
                    </TouchableOpacity>
                    <Text style={styles.presetTitle}>{fav.name}</Text>
                    <Text style={styles.presetDesc}>Mode: {fav.mode} | Speed: {fav.speed}%</Text>
                  </TouchableOpacity>
                 ))}
              </View>
            </View>
          )}

          {activeMode === 'FIXED' && (
            <View style={{ marginBottom: 8 }}>
              
              {/* FIXED PATTERNS COLLAPSIBLE */}
              <TouchableOpacity onPress={() => setIsFixedExpanded(!isFixedExpanded)} style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: isFixedExpanded ? 8 : 16 }}>
                 <Text style={[Typography.title, isDark && { color: '#FFF' }, { marginBottom: 0 }]}>Fixed Patterns</Text>
                 <MaterialCommunityIcons name={isFixedExpanded ? "chevron-up" : "chevron-down"} size={24} color={Colors.textMuted} />
              </TouchableOpacity>
              
              {isFixedExpanded && (
                <View>
                  <View style={{ backgroundColor: '#000000', borderRadius: 8, padding: 8, marginBottom: 8, height: 170 }}>
                    <ScrollView nestedScrollEnabled={true}>
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
                        style={{ flexDirection: 'row', alignItems: 'center', paddingVertical: 5, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.05)' }}
                      >
                        <FixedPatternPreviewRow baseDots={pattern.dots} patternId={pattern.id} speed={speed} points={devices?.[0]?.points || points || 16} segments={devices?.[0]?.segments || 1} />
                        <Text style={{ color: Colors.textMuted, fontSize: 16 }}>{fixedPatternId === pattern.id ? '✓' : '→'}</Text>
                      </TouchableOpacity>
                    ))}
                    </ScrollView>
                  </View>

                  {/* Fixed Pattern block Fg/Bg buttons moved down above Color Grid natively */}
                </View>
              )}

              {/* MULTI-COLOR PRESETS & DIY BUILDER COLLAPSIBLE */}
              <TouchableOpacity onPress={() => setIsPresetsExpanded(!isPresetsExpanded)} style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: isPresetsExpanded ? 8 : 16 }}>
                 <Text style={[Typography.title, isDark && { color: '#FFF' }, { marginBottom: 0 }]}>Multi-Color Arrays</Text>
                 <MaterialCommunityIcons name={isPresetsExpanded ? "chevron-up" : "chevron-down"} size={24} color={Colors.textMuted} />
              </TouchableOpacity>
              
              {isPresetsExpanded && (
                <View>
                  <Text style={{ color: Colors.textMuted, fontSize: 12, marginBottom: 8, fontWeight: 'bold', marginTop: 8 }}>QUICK PRESETS</Text>
                  <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 8, marginBottom: 16 }}>
                    {[
                      { name: 'Rainbow', colors: ['#FF0000', '#FF7F00', '#FFFF00', '#00FF00', '#0000FF', '#4B0082', '#9400D3'], type: 3 },
                      { name: 'America', colors: ['#FF0000', '#FFFFFF', '#0000FF'], type: 3 },
                      { name: 'Cyberpunk', colors: ['#00FFFF', '#FF00FF', '#FFFF00'], type: 3 },
                      { name: 'Forest', colors: ['#00FF00', '#008000', '#228B22', '#32CD32'], type: 1 },
                      { name: 'Sunset', colors: ['#FF0000', '#FF4500', '#FF8C00', '#FFA500'], type: 1 },
                      { name: 'Ice', colors: ['#FFFFFF', '#E0FFFF', '#00FFFF', '#0000FF'], type: 1 }
                    ].map((preset, idx) => (
                      <TouchableOpacity 
                        key={idx}
                        onPress={() => {
                            setFixedSubMode('MULTI');
                            setMultiColors(preset.colors);
                            setMultiTransition(preset.type);
                            if (writeToDevice) {
                                const rgbColors = generateSortedColors(preset.colors);
                                writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, multiLength, preset.type, speed));
                            }
                        }}
                        style={{
                            paddingVertical: 10, paddingHorizontal: 16, 
                            backgroundColor: 'rgba(255,255,255,0.05)', 
                            borderRadius: 8, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)'
                        }}
                      >
                        <Text style={{ color: '#FFF', fontWeight: 'bold', fontSize: 13, marginBottom: 6 }}>{preset.name}</Text>
                        <View style={{ flexDirection: 'row', gap: 4 }}>
                           {preset.colors.map((c, i) => (
                              <View key={i} style={{ width: 12, height: 12, borderRadius: 6, backgroundColor: c }} />
                           ))}
                        </View>
                      </TouchableOpacity>
                    ))}
                  </View>

                  <Text style={{ color: Colors.textMuted, fontSize: 12, marginBottom: 8, fontWeight: 'bold' }}>DIY ARRAY BUILDER</Text>
                  <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 10, paddingBottom: 16 }}>
                    {multiColors.map((hex, index) => (
                      <TouchableOpacity key={index} style={{ width: 44, height: 44, borderRadius: 22, backgroundColor: hex, borderWidth: 2, borderColor: '#FFF', shadowColor: hex, shadowOpacity: 0.8, shadowRadius: 4 }} onPress={() => {
                        setFixedSubMode('MULTI');
                        const newArr = [...multiColors];
                        newArr[index] = selectedColor;
                        setMultiColors(newArr);
                        const rgbColors = generateSortedColors(newArr);
                        if(writeToDevice) writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, multiLength, multiTransition, speed));
                      }} />
                    ))}
                    {multiColors.length < 16 && (
                      <TouchableOpacity style={{ width: 44, height: 44, borderRadius: 22, backgroundColor: 'rgba(255,255,255,0.1)', borderWidth: 1, borderColor: '#FFF', justifyContent: 'center', alignItems: 'center' }} onPress={() => {
                         setFixedSubMode('MULTI');
                         const newArr = [...multiColors, selectedColor];
                         setMultiColors(newArr);
                         const rgbColors = generateSortedColors(newArr);
                         if(writeToDevice) writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, multiLength, multiTransition, speed));
                      }}>
                        <Text style={{ color: '#FFF', fontSize: 24, fontWeight: 'bold' }}>+</Text>
                      </TouchableOpacity>
                    )}
                    {multiColors.length > 1 && (
                      <TouchableOpacity style={{ width: 44, height: 44, borderRadius: 22, backgroundColor: 'rgba(255,0,0,0.3)', borderWidth: 1, borderColor: '#FFF', justifyContent: 'center', alignItems: 'center' }} onPress={() => {
                         setFixedSubMode('MULTI');
                         const newArr = [...multiColors];
                         newArr.pop();
                         setMultiColors(newArr);
                         const rgbColors = generateSortedColors(newArr);
                         if(writeToDevice) writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, multiLength, multiTransition, speed));
                      }}>
                        <Text style={{ color: '#FFF', fontSize: 24, fontWeight: 'bold', lineHeight: 26 }}>-</Text>
                      </TouchableOpacity>
                    )}
                  </View>

                  <Text style={{ color: Colors.textMuted, fontSize: 12, marginBottom: 8, fontWeight: 'bold' }}>TRANSITION TYPE</Text>
                  <ScrollView horizontal showsHorizontalScrollIndicator={false} style={{ flexDirection: 'row', marginBottom: 20 }}>
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
                           if(writeToDevice) writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, multiLength, mode.val, speed));
                        }} 
                        style={{ 
                          paddingHorizontal: 16, paddingVertical: 10, 
                          backgroundColor: multiTransition === mode.val ? Colors.primary : 'rgba(255,255,255,0.05)', 
                          borderRadius: 8, marginRight: 8 
                        }}
                      >
                        <Text style={{ color: multiTransition === mode.val ? '#000' : '#FFF', fontWeight: 'bold', fontSize: 12 }}>{mode.label}</Text>
                      </TouchableOpacity>
                    ))}
                  </ScrollView>
                </View>
              )}

              {/* CANDLE EMULATOR COLLAPSIBLE */}
              <TouchableOpacity onPress={() => { setIsCandleExpanded(!isCandleExpanded); if (!isCandleExpanded) setFixedSubMode('CANDLE'); }} style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: isCandleExpanded ? 8 : 16 }}>
                 <Text style={[Typography.title, isDark && { color: '#FFF' }, { marginBottom: 0 }]}>Candle</Text>
                 <MaterialCommunityIcons name={isCandleExpanded ? "chevron-up" : "chevron-down"} size={24} color={Colors.textMuted} />
              </TouchableOpacity>
              
              {isCandleExpanded && (
                <View style={{ marginBottom: 16 }}>
                  <Text style={{ color: Colors.textMuted, fontSize: 13, marginBottom: 12 }}>
                    Simulates dynamic volumetric tracking mathematically in hardware using raw LED flickering logic.
                  </Text>
                  
                  <View style={{ flexDirection: 'row', backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 12, padding: 4, marginTop: 4, marginBottom: 16 }}>
                    {[
                      { label: 'SLOW', value: 1 },
                      { label: 'MEDIUM', value: 2 },
                      { label: 'FAST', value: 3 }
                    ].map(speedObj => {
                      const isActive = Math.round(candleAmplitude) === speedObj.value;
                      return (
                        <TouchableOpacity 
                          key={speedObj.value}
                          onPress={() => {
                             setCandleAmplitude(speedObj.value);
                             setFixedSubMode('CANDLE');
                             if (writeToDevice) {
                                const rawR = parseInt(selectedColor.substring(1, 3), 16) || 255;
                                const rawG = parseInt(selectedColor.substring(3, 5), 16) || 255;
                                const rawB = parseInt(selectedColor.substring(5, 7), 16) || 255;
                                let finalR = rawR; let finalG = rawG; let finalB = rawB;
                                const sorting = devices && devices.length > 0 ? devices[0].sorting || 'GRB' : 'GRB';
                                if (sorting === 'GRB') { finalR = rawG; finalG = rawR; }
                                writeToDevice(ZenggeProtocol.setCandleMode(finalR, finalG, finalB, speed, brightness, speedObj.value));
                             }
                          }}
                          style={{
                            flex: 1,
                            paddingVertical: 12,
                            borderRadius: 8,
                            backgroundColor: isActive ? Colors.primary : 'transparent',
                            alignItems: 'center',
                            justifyContent: 'center',
                          }}
                        >
                          <Text style={{ 
                            fontSize: 12, 
                            fontWeight: 'bold', 
                            color: isActive ? '#000000' : Colors.textMuted 
                          }}>
                            {speedObj.label}
                          </Text>
                        </TouchableOpacity>
                      );
                    })}
                  </View>
                </View>
              )}
              {/* PROGRAMS COLLAPSIBLE */}
              <TouchableOpacity onPress={() => { setIsProgramsExpanded(!isProgramsExpanded); if (!isProgramsExpanded) setFixedSubMode('RBM'); }} style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: isProgramsExpanded ? 8 : 16 }}>
                 <Text style={[Typography.title, isDark && { color: '#FFF' }, { marginBottom: 0 }]}>Programs</Text>
                 <MaterialCommunityIcons name={isProgramsExpanded ? "chevron-up" : "chevron-down"} size={24} color={Colors.textMuted} />
              </TouchableOpacity>
              
              {isProgramsExpanded && (
                <View style={{ marginBottom: 16, marginTop: 12 }}>
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

              {/* MUSIC COLLAPSIBLE */}
              <TouchableOpacity onPress={() => { setIsMusicExpanded(!isMusicExpanded); if (!isMusicExpanded) setFixedSubMode('MUSIC'); }} style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: isMusicExpanded ? 8 : 16 }}>
                 <Text style={[Typography.title, isDark && { color: '#FFF' }, { marginBottom: 0 }]}>Music Sync</Text>
                 <MaterialCommunityIcons name={isMusicExpanded ? "chevron-up" : "chevron-down"} size={24} color={Colors.textMuted} />
              </TouchableOpacity>
              
              {isMusicExpanded && (
                <View style={{ marginBottom: 16 }}>
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

              <View style={styles.musicVisualizerSection}>
                <SpectrumVisualizer />
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
                    <MaterialCommunityIcons name="sync" size={20} color="#FFF" />
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

              <View style={{ flexDirection: 'row', marginBottom: 12, marginTop: 12 }}>
                <TouchableOpacity 
                  onPress={() => {
                     setFixedSubMode('MUSIC');
                     setMusicMatrixStyle(39);
                     handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicHue, musicSecondaryHue, 39);
                  }}
                  style={{ flex: 1, paddingVertical: 10, alignItems: 'center', backgroundColor: musicMatrixStyle === 39 ? Colors.primary : Colors.surfaceHighlight, borderTopLeftRadius: Layout.borderRadius, borderBottomLeftRadius: Layout.borderRadius }}
                >
                  <Text style={{ color: musicMatrixStyle === 39 ? '#000' : Colors.textMuted, fontWeight: 'bold' }}>Light Screen</Text>
                </TouchableOpacity>
                <TouchableOpacity 
                  onPress={() => {
                     setFixedSubMode('MUSIC');
                     setMusicMatrixStyle(38);
                     handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicHue, musicSecondaryHue, 38);
                  }}
                  style={{ flex: 1, paddingVertical: 10, alignItems: 'center', backgroundColor: musicMatrixStyle === 38 ? Colors.secondary : Colors.surfaceHighlight, borderTopRightRadius: Layout.borderRadius, borderBottomRightRadius: Layout.borderRadius }}
                >
                  <Text style={{ color: musicMatrixStyle === 38 ? '#000' : Colors.textMuted, fontWeight: 'bold' }}>Light Bar</Text>
                </TouchableOpacity>
              </View>


                </View>
              )}
              {/* CAMERA COLLAPSIBLE */}
              <TouchableOpacity onPress={() => { setIsCameraExpanded(!isCameraExpanded); if (!isCameraExpanded) setFixedSubMode('CAMERA'); }} style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: isCameraExpanded ? 8 : 16 }}>
                 <Text style={[Typography.title, isDark && { color: '#FFF' }, { marginBottom: 0 }]}>Camera</Text>
                 <MaterialCommunityIcons name={isCameraExpanded ? "chevron-up" : "chevron-down"} size={24} color={Colors.textMuted} />
              </TouchableOpacity>
              
              {isCameraExpanded && (
                <View style={{ marginBottom: 16 }}>


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
        </ScrollView>

        {/* UNIVERSAL SLIDERS FOOTER - Always at the bottom */}
        {true && (
          <View style={[styles.sceneSlidersContainer, { marginTop: 8, borderTopWidth: 1, borderTopColor: 'rgba(255,255,255,0.05)', paddingTop: 8 }]}>
            {/* Color Grid wrappers */}
            {(activeMode !== 'PRESETS' && !(activeMode === 'FIXED' && fixedSubMode === 'RBM')) && (
              <View style={{ marginBottom: 8 }}>
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
                        height: 20,
                        borderRadius: 10,
                        backgroundColor: dynamicColor,
                        justifyContent: 'center',
                        alignItems: 'center',
                        marginBottom: 12,
                        shadowColor: dynamicColor,
                        shadowOpacity: 0.8,
                        shadowRadius: 10,
                        shadowOffset: { width: 0, height: 0 },
                        elevation: 6,
                        borderWidth: 1,
                        borderColor: 'rgba(255,255,255,0.4)'
                      }}
                    >
                      <Text style={{ color: '#FFF', fontSize: 10, fontWeight: '800', letterSpacing: 2, textShadowColor: '#000', textShadowRadius: 4, textShadowOffset: {width: 0, height: 1} }}>
                        SELECTED COLOR
                      </Text>
                    </TouchableOpacity>
                  );
                })()}

                {/* Music Mode Split Color Tracker */}
                {(activeMode === 'FIXED' && fixedSubMode === 'MUSIC') && (() => {
                  const hueToHex = (hue: number) => {
                     const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                     return "#" + [f(5), f(3), f(1)].map(x => Math.round(x * 255).toString(16).padStart(2, "0")).join("");
                  };
                  const primHex = hueToHex(musicHue).toUpperCase();
                  const secHex = hueToHex(musicSecondaryHue).toUpperCase();
                  
                  return (
                    <View style={{
                        flexDirection: 'row', width: '100%', height: 24, borderRadius: 12, marginBottom: 16,
                        borderWidth: 1, borderColor: 'rgba(255,255,255,0.4)', backgroundColor: 'transparent'
                    }}>
                        <TouchableOpacity 
                          activeOpacity={0.9}
                          onPress={() => setMusicColorFocus('PRIMARY')}
                          style={{ flex: 1, backgroundColor: primHex, justifyContent: 'center', alignItems: 'center', opacity: musicColorFocus === 'PRIMARY' ? 1.0 : 0.4, borderTopLeftRadius: 11, borderBottomLeftRadius: 11, shadowColor: primHex, shadowOpacity: 1, shadowRadius: 16, shadowOffset: { width: 0, height: 0 }, elevation: 12 }}
                        >
                          <Text style={{ color: '#FFFFFF', fontSize: 10, fontWeight: '900', letterSpacing: 1, textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 6 }}>SOUND COLUMN</Text>
                        </TouchableOpacity>
                        <TouchableOpacity 
                          activeOpacity={0.9}
                          onPress={() => setMusicColorFocus('SECONDARY')}
                          style={{ flex: 1, backgroundColor: secHex, justifyContent: 'center', alignItems: 'center', opacity: musicColorFocus === 'SECONDARY' ? 1.0 : 0.4, borderLeftWidth: 1, borderLeftColor: 'rgba(255,255,255,0.4)', borderTopRightRadius: 11, borderBottomRightRadius: 11, shadowColor: secHex, shadowOpacity: 1, shadowRadius: 16, shadowOffset: { width: 0, height: 0 }, elevation: 12 }}
                        >
                          <Text style={{ color: '#FFFFFF', fontSize: 10, fontWeight: '900', letterSpacing: 1, textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 6 }}>DROP COLOR</Text>
                        </TouchableOpacity>
                    </View>
                  );
                })()}

                {/* Fixed Pattern Mode Split Color Tracker */}
                {(activeMode === 'FIXED' && fixedSubMode === 'PATTERN' && fixedPatternId !== 1) && (
                    <View style={{
                        flexDirection: 'row', width: '100%', height: 24, borderRadius: 12, marginBottom: 16,
                        borderWidth: 1, borderColor: 'rgba(255,255,255,0.4)', backgroundColor: 'transparent'
                    }}>
                        <TouchableOpacity 
                          activeOpacity={0.9}
                          onPress={() => setFixedColorMode('FOREGROUND')}
                          style={{ flex: 1, backgroundColor: fixedFgColor, justifyContent: 'center', alignItems: 'center', opacity: fixedColorMode === 'FOREGROUND' ? 1.0 : 0.4, borderTopLeftRadius: 11, borderBottomLeftRadius: 11, shadowColor: fixedFgColor, shadowOpacity: 1, shadowRadius: 16, shadowOffset: { width: 0, height: 0 }, elevation: 12 }}
                        >
                          <Text style={{ color: '#FFFFFF', fontSize: 10, fontWeight: '900', letterSpacing: 1, textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 6 }}>FOREGROUND</Text>
                        </TouchableOpacity>
                        <TouchableOpacity 
                          activeOpacity={0.9}
                          onPress={() => setFixedColorMode('BACKGROUND')}
                          style={{ flex: 1, backgroundColor: fixedBgColor, justifyContent: 'center', alignItems: 'center', opacity: fixedColorMode === 'BACKGROUND' ? 1.0 : 0.4, borderLeftWidth: 1, borderLeftColor: 'rgba(255,255,255,0.4)', borderTopRightRadius: 11, borderBottomRightRadius: 11, shadowColor: fixedBgColor, shadowOpacity: 1, shadowRadius: 16, shadowOffset: { width: 0, height: 0 }, elevation: 12 }}
                        >
                          <Text style={{ color: '#FFFFFF', fontSize: 10, fontWeight: '900', letterSpacing: 1, textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 6 }}>BACKGROUND</Text>
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
                    } else if (activeMode === 'MUSIC') {
                      const hue = musicColorFocus === 'PRIMARY' ? musicHue : musicSecondaryHue;
                      const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                      dynamicColor = "#" + [f(5), f(3), f(1)].map(x => Math.round(x * 255).toString(16).padStart(2, "0").toUpperCase()).join("");
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
                            } else {
                               setSelectedColor(color);
                               if (hueMap[color] !== undefined) setFixedHue(hueMap[color]);
                            }
                          } else if (activeMode === 'MUSIC') {
                            const newHue = hueMap[color] !== undefined ? hueMap[color] : 0;
                            if (musicColorFocus === 'PRIMARY') {
                                setMusicHue(newHue);
                                handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, newHue, musicSecondaryHue);
                            } else {
                                setMusicSecondaryHue(newHue);
                                handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicHue, newHue);
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
                            width: 26, 
                            height: 26, 
                            borderRadius: 13,
                            shadowColor: color,
                            shadowOpacity: 1,
                            shadowRadius: 16,
                            shadowOffset: { width: 0, height: 0 },
                            elevation: 12
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
            {(activeMode !== 'PRESETS' && !(activeMode === 'FIXED' && (fixedSubMode === 'RBM' || fixedSubMode === 'CAMERA'))) && (
              <View style={[styles.controlRow, { marginTop: 0, height: 32 }]}>
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
                      }
                    } else if (activeMode === 'MUSIC') {
                      if (musicColorFocus === 'PRIMARY') {
                         handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, hue, musicSecondaryHue, musicMatrixStyle);
                      } else {
                         handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicHue, hue, musicMatrixStyle);
                      }
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
            <View style={[styles.controlRow, { marginTop: 24 }]}>
              <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center' }}>
                <Text style={{ position: 'absolute', top: -20, left: 24, fontSize: 10, color: Colors.textMuted, fontWeight: 'bold', letterSpacing: 1 }}>BRIGHTNESS</Text>
                <Text style={{ color: Colors.textMuted, marginRight: 8, fontSize: 16 }}>☀️</Text>

                <CustomSlider 
                  value={brightness}
                  onValueChange={setBrightness}
                  minimumValue={0}
                  maximumValue={100}
                  style={{ flex: 1 }}
                  onSlidingComplete={(val) => {
                    if (writeToDevice) {
                      if (activeMode === 'MUSIC') {
                        handleMusicChange(musicPatternId, micSensitivity, val, micSource, musicHue, musicSecondaryHue, musicMatrixStyle);
                      } else {
                        if (activeMode === 'FIXED') {
                          if (fixedSubMode === 'PATTERN') {
                            applyFixedPattern(fixedPatternId, fixedFgColor, fixedBgColor, speed, val);
                          } else if (fixedSubMode === 'MULTI') {
                            const factor = val / 100;
                            const rgbColors = multiColors.map(h => ({
                                r: Math.round((parseInt(h.slice(1,3), 16) || 0) * factor),
                                g: Math.round((parseInt(h.slice(3,5), 16) || 0) * factor),
                                b: Math.round((parseInt(h.slice(5,7), 16) || 0) * factor)
                            }));
                            writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, multiLength, multiTransition, speed));
                          } else if (fixedSubMode === 'CANDLE') {
                            const rawR = parseInt(selectedColor.substring(1, 3), 16) || 255;
                            const rawG = parseInt(selectedColor.substring(3, 5), 16) || 255;
                            const rawB = parseInt(selectedColor.substring(5, 7), 16) || 255;
                            let finalR = rawR; let finalG = rawG; let finalB = rawB;
                            const sorting = devices && devices.length > 0 ? devices[0].sorting || 'GRB' : 'GRB';
                            if (sorting === 'GRB') { finalR = rawG; finalG = rawR; }
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
                <Text style={{ color: Colors.text, marginLeft: 10, width: 45, fontWeight: 'bold', fontSize: 13, textAlign: 'right' }}>
                  {Math.round(brightness)}%
                </Text>
              </View>
            </View>
            )}

            {/* Speed Slider - Hidden in MUSIC, CANDLE, and CAMERA */}
            {!(activeMode === 'FIXED' && (fixedSubMode === 'MUSIC' || fixedSubMode === 'CANDLE' || fixedSubMode === 'CAMERA')) && (
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
                          if (fixedSubMode === 'PATTERN') {
                            applyFixedPattern(fixedPatternId, fixedFgColor, fixedBgColor, val);
                          } else if (fixedSubMode === 'MULTI') {
                            const factor = brightness / 100;
                            const rgbColors = multiColors.map(h => ({
                                r: Math.round((parseInt(h.slice(1,3), 16) || 0) * factor),
                                g: Math.round((parseInt(h.slice(3,5), 16) || 0) * factor),
                                b: Math.round((parseInt(h.slice(5,7), 16) || 0) * factor)
                            }));
                            writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, multiLength, multiTransition, val));
                          } else if (fixedSubMode === 'CANDLE') {
                            const rawR = parseInt(selectedColor.substring(1, 3), 16) || 255;
                            const rawG = parseInt(selectedColor.substring(3, 5), 16) || 255;
                            const rawB = parseInt(selectedColor.substring(5, 7), 16) || 255;
                            let finalR = rawR; let finalG = rawG; let finalB = rawB;
                            const sorting = devices && devices.length > 0 ? devices[0].sorting || 'GRB' : 'GRB';
                            if (sorting === 'GRB') { finalR = rawG; finalG = rawR; }
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
                  <Text style={{ color: Colors.text, marginLeft: 10, width: 45, fontWeight: 'bold', fontSize: 13, textAlign: 'right' }}>
                    {Math.round(speed)}
                  </Text>
                </View>
              </View>
            )}

            {/* Sensitivity Slider - Visible ONLY in MUSIC */}
            {(activeMode === 'FIXED' && fixedSubMode === 'MUSIC') && (
              <View style={[styles.controlRow, { marginTop: 12 }]}>
                <Text style={{ position: 'absolute', top: -14, left: 24, fontSize: 10, color: Colors.textMuted, fontWeight: 'bold', letterSpacing: 1 }}>SENSITIVITY</Text>
                <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center' }}>
                  <Text style={{ color: Colors.textMuted, marginRight: 8, fontSize: 16 }}>🎚️</Text>
                  <CustomSlider 
                    value={micSensitivity}
                    onValueChange={setMicSensitivity}
                    onSlidingComplete={(val) => {
                      handleMusicChange(musicPatternId, val, brightness, micSource, musicHue, musicSecondaryHue, musicMatrixStyle);
                    }}
                    minimumValue={0}
                    maximumValue={100}
                    style={{ flex: 1 }}
                  />
                  <Text style={{ color: Colors.text, marginLeft: 10, width: 45, fontWeight: 'bold', fontSize: 13, textAlign: 'right' }}>
                    {Math.round(micSensitivity)}%
                  </Text>
                </View>
              </View>
            )}


          </View>
        )}
        {/* Favorite Prompt Modal */}
        <Modal visible={isFavPromptVisible} transparent animationType="fade">
          <View style={{ flex: 1, backgroundColor: 'rgba(0,0,0,0.85)', justifyContent: 'center', alignItems: 'center', padding: 20 }}>
            <View style={{ backgroundColor: Colors.surface, padding: 24, borderRadius: 20, width: '100%', maxWidth: 340, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}>
              <Text style={{ color: '#FFF', fontSize: 18, fontWeight: 'bold', marginBottom: 12, textAlign: 'center' }}>Save Favorite</Text>
              <Text style={{ color: Colors.textMuted, fontSize: 14, marginBottom: 20, textAlign: 'center' }}>Name your preset. Leave blank to use the default name.</Text>
              <TextInput
                style={{ backgroundColor: 'rgba(255,255,255,0.05)', color: '#FFF', padding: 12, borderRadius: 8, fontSize: 16, marginBottom: 24, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}
                placeholder="Custom Preset Name..."
                placeholderTextColor="rgba(255,255,255,0.3)"
                value={favPromptName}
                onChangeText={setFavPromptName}
                autoFocus
              />
              <View style={{ flexDirection: 'row', gap: 12 }}>
                <TouchableOpacity style={{ flex: 1, padding: 14, borderRadius: 10, backgroundColor: 'rgba(255,255,255,0.05)' }} onPress={() => setIsFavPromptVisible(false)}>
                  <Text style={{ color: '#FFF', textAlign: 'center', fontWeight: 'bold' }}>Cancel</Text>
                </TouchableOpacity>
                <TouchableOpacity style={{ flex: 1, padding: 14, borderRadius: 10, backgroundColor: Colors.primary }} onPress={handleConfirmSaveFavorite}>
                  <Text style={{ color: '#000', textAlign: 'center', fontWeight: 'bold' }}>Save</Text>
                </TouchableOpacity>
              </View>
            </View>
          </View>
        </Modal>

      </View>
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
    color: Colors.isDark ? '#FFF' : '#000',
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
    padding: 8,
    borderRadius: 12,
    width: 90,
  },
  micBtnActive: {
    backgroundColor: 'rgba(255,255,255,0.05)',
  },
  micIconCircle: {
    width: 48,
    height: 48,
    borderRadius: 24,
    backgroundColor: 'rgba(255,255,255,0.05)',
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 6,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
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
  }
});
