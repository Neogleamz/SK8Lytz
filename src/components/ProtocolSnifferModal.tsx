import React, { useState, useEffect, useCallback } from 'react';
import {
  View, Text, StyleSheet, Modal, TouchableOpacity, ScrollView,
  FlatList, Platform, SafeAreaView, TextInput, ActivityIndicator, Switch
} from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../context/ThemeContext';
import { AppLogger } from '../services/AppLogger';
import { ZenggeProtocol, IC_TYPES, COLOR_SORTING_RGB } from '../protocols/ZenggeProtocol';
import CustomSlider from './CustomSlider';
import ProductVisualizer from './ProductVisualizer';
import CameraTracker from './CameraTracker';

interface ProtocolSnifferModalProps {
  visible: boolean;
  onClose: () => void;
  writeToDevice?: (data: number[], deviceId?: string) => Promise<void>;
  liveRxPayload?: { deviceId: string; payloadHex: string; timestamp?: number } | null;
  connectedDevices?: { id: string, name: string | null }[];
  allDevices?: any[];
  isScanning?: boolean;
  handleScan?: () => void;
  connectToDevice?: (item: any) => Promise<void>;
  handleDisconnect?: () => void;
}

export default function ProtocolSnifferModal({ 
  visible, onClose, writeToDevice, liveRxPayload, connectedDevices = [],
  allDevices = [], isScanning = false, handleScan, connectToDevice, handleDisconnect 
}: ProtocolSnifferModalProps) {
  const { Colors, isDark } = useTheme();
  
  // States
  const [snifferLogs, setSnifferLogs] = useState<{dir: 'TX' | 'RX', hex: string, t: number, dev?: string}[]>([]);
  const [snifferInput, setSnifferInput] = useState('');
  const [isSnifferPaused, setIsSnifferPaused] = useState(false);
  const [snifferTarget, setSnifferTarget] = useState<string>('ALL');

  // Device Selection Matrix
  const [isScanningExpanded, setIsScanningExpanded] = useState(false);

  // Hardware Re-Config Injector
  const [hwPoints, setHwPoints] = useState('43');
  const [hwSegments, setHwSegments] = useState('1');
  const [hwColorOrder, setHwColorOrder] = useState('GRB');
  const [hwStripType, setHwStripType] = useState('WS2812B');

  // Extracted Controller Specs Tracker
  const [detectedPoints, setDetectedPoints] = useState<number | null>(null);
  const [detectedSegments, setDetectedSegments] = useState<number | null>(null);
  const [detectedColorOrder, setDetectedColorOrder] = useState<string | null>(null);
  const [detectedStripType, setDetectedStripType] = useState<string | null>(null);
  const [detectedFirmware, setDetectedFirmware] = useState<string | null>(null);
  const [detectedLive, setDetectedLive] = useState<boolean>(false);
  
  // Builder State
  const [protocol, setProtocol] = useState<'0x59' | '0x51' | '0x42' | '0x73' | '0x71' | '0x2A' | 'CANDLE' | 'CAMERA' | 'MULTI'>('0x59');
  const [r, setR] = useState(255);
  const [g, setG] = useState(0);
  const [b, setB] = useState(0);
  const [speed, setSpeed] = useState(100);
  const [length, setLength] = useState(10);
  const [transitionType, setTransitionType] = useState<number>(3);
  const [segmentDirection, setSegmentDirection] = useState<number>(1);
  const [brightness, setBrightness] = useState(100);
  const [rbmPattern, setRbmPattern] = useState<number>(1);
  const [multiColors, setMultiColors] = useState<string[]>(['#FF0000', '#00FF00', '#0000FF']);
  const [multiTransition, setMultiTransition] = useState<number>(3);
  const [isDeviceMic, setIsDeviceMic] = useState<number>(1);
  const [musicModeType, setMusicModeType] = useState<number>(38);
  const [musicPatternId, setMusicPatternId] = useState<number>(1);
  const [musicSensitivity, setMusicSensitivity] = useState<number>(50);
  const [r2, setR2] = useState<number>(0);
  const [g2, setG2] = useState<number>(0);
  const [b2, setB2] = useState<number>(255);
  const [rfAuthMode, setRfAuthMode] = useState<'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED'>('ALLOW_ALL');
  const [candleAmplitude, setCandleAmplitude] = useState<number>(2);
  const [powerTesterState, setPowerTesterState] = useState<boolean>(true);
  
  const [currentGeneratedPayload, setCurrentGeneratedPayload] = useState<number[]>([]);
  const currentPreviewColor = `rgb(${r}, ${g}, ${b})`;

  const SettingWithExplanation = ({ title, description, children, ColorsKey }: { title: string, description: string, children: React.ReactNode, ColorsKey: any }) => {
    const [expanded, setExpanded] = useState(false);
    return (
      <View style={{ marginBottom: 12 }}>
        <TouchableOpacity onPress={() => setExpanded(!expanded)} style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 8 }} activeOpacity={0.7}>
          <Text style={{ color: ColorsKey.textMuted || '#aaa', flex: 1, fontWeight: 'bold' }}>{title}</Text>
          <MaterialCommunityIcons name={expanded ? "chevron-up" : "information-outline"} size={16} color={ColorsKey.primary || '#00f0ff'} />
        </TouchableOpacity>
        {expanded && (
          <Text style={{ color: '#888', fontSize: 11, marginBottom: 8, fontStyle: 'italic', backgroundColor: 'rgba(255,255,255,0.05)', padding: 8, borderRadius: 6 }}>
            {description}
          </Text>
        )}
        {children}
      </View>
    );
  };
  
  const load = useCallback(async () => {
    const logs = await AppLogger.getLogs();
    
    const loadedHistory = logs.filter(l => l.e === 'RAW_PAYLOAD').map(l => ({
       dir: l.d.dir as 'RX' | 'TX',
       hex: l.d.hex,
       t: l.t,
       dev: l.d.deviceId
    }));
    
    setSnifferLogs(prev => {
       const merged = [...prev];
       loadedHistory.forEach(h => {
         if (!merged.find(m => m.t === h.t && m.hex === h.hex)) {
           merged.push(h);
         }
       });
       return merged.sort((a,b) => b.t - a.t);
    });
  }, []);

  // Hydrate from global database explicitly
  useEffect(() => {
    if (visible) load();
  }, [visible, load]);

  // React to Live Payloads & Extract Intercepted States
  useEffect(() => {
    if (visible && liveRxPayload && liveRxPayload.payloadHex && !isSnifferPaused) {
      if (snifferTarget !== 'ALL' && liveRxPayload.deviceId !== snifferTarget) return;

      // 1. Process Timeline Sink
      setSnifferLogs(prev => {
        const merged = [...prev];
        const rxHash = { dir: 'RX' as const, hex: liveRxPayload.payloadHex, t: liveRxPayload.timestamp || Date.now(), dev: liveRxPayload.deviceId };
        if (!merged.find(m => m.hex === rxHash.hex && (Date.now() - m.t < 50))) {
            merged.unshift(rxHash);
        }
        return merged;
      });

      // 2. Parse Intercepted Payload to Extract Hardware Specs
      const bytes = liveRxPayload.payloadHex.split(' ').map(h => parseInt(h, 16));
      
      // PRIMARY: Parse 0x63 hardware settings response (correct full parser)
      // Works whether raw (starts 0x63) or V2-wrapped (offset 8 bytes in)
      const hw63 = ZenggeProtocol.parseHardwareSettingsResponse(bytes);
      if (hw63) {
        setDetectedPoints(hw63.ledPoints);
        setDetectedColorOrder(hw63.colorSortingName);
        setDetectedStripType(hw63.icName);
        setDetectedLive(true);
      } else {
        // FALLBACK: legacy 0x81/0x10 response parser
        const parsedHardware = ZenggeProtocol.parseHardwareConfig(bytes);
        if (parsedHardware) {
          setDetectedPoints(parsedHardware.points);
          setDetectedSegments(parsedHardware.segments);
          setDetectedColorOrder(parsedHardware.sorting);
          setDetectedStripType(parsedHardware.stripType);
          setDetectedLive(false);
        }
      }

      // 0x32 ping reveals Firmware on some controllers
      if (bytes[0] === 0x32 && bytes.length > 5) {
         setDetectedFirmware(`${bytes[2]}.${bytes[3]}.${bytes[4]}`);
      }

    }
  }, [liveRxPayload, visible, isSnifferPaused, snifferTarget]);

  // Generate Internal Payload state bound to Sliders
  useEffect(() => {
    let payload: number[] = [];
    const factor = brightness / 100;
    const color = { r: Math.round(r * factor), g: Math.round(g * factor), b: Math.round(b * factor) };

    if (protocol === '0x59') {
      const totalPoints = length;
      const totalLen = (totalPoints * 3) + 9;
      payload = new Array(totalLen);
      payload[0] = 0x59;
      payload[1] = (totalLen >> 8) & 0xFF;
      payload[2] = totalLen & 0xFF;
      let idx = 3;
      for (let i = 0; i < totalPoints; i++) {
        payload[idx++] = color.r; payload[idx++] = color.g; payload[idx++] = color.b;
      }
      payload[idx++] = (totalPoints >> 8) & 0xFF; payload[idx++] = totalPoints & 0xFF;
      payload[idx++] = transitionType; payload[idx++] = speed; payload[idx++] = segmentDirection;
      payload[idx] = ZenggeProtocol.calculateChecksum(payload.slice(0, totalLen - 1));
      payload = ZenggeProtocol.wrapCommand(payload);
    } else if (protocol === '0x51') {
      const bg = { r: 0, g: 0, b: 0 };
      payload = ZenggeProtocol.setCustomMode([
        { mode: transitionType === 0 ? 1 : transitionType, speed: speed, color1: color, color2: bg },
        { mode: transitionType === 0 ? 1 : transitionType, speed: speed, color1: bg, color2: color }
      ]);
    } else if (protocol === 'CAMERA') {
      payload = ZenggeProtocol.setColor(color.r, color.g, color.b);
    } else if (protocol === '0x73') {
      const c2 = { r: Math.round(r2 * factor), g: Math.round(g2 * factor), b: Math.round(b2 * factor) };
      payload = ZenggeProtocol.setMusicConfig(isDeviceMic === 1, musicModeType, musicPatternId, color, c2, musicSensitivity, brightness);
    } else if (protocol === '0x42') {
      payload = ZenggeProtocol.setCustomRbm(rbmPattern, speed, brightness);
    } else if (protocol === 'CANDLE') {
      payload = ZenggeProtocol.setCandleMode(r, g, b, speed, brightness, candleAmplitude);
    } else if (protocol === '0x2A') {
      payload = ZenggeProtocol.setRfRemoteState(rfAuthMode);
    } else if (protocol === '0x71') {
      payload = powerTesterState ? ZenggeProtocol.turnOn() : ZenggeProtocol.turnOff();
    } else if (protocol === 'MULTI') {
      const rgbColors = multiColors.map(hex => ({ r: parseInt(hex.slice(1,3), 16) || 0, g: parseInt(hex.slice(3,5), 16) || 0, b: parseInt(hex.slice(5,7), 16) || 0 }));
      payload = ZenggeProtocol.setMultiColor(rgbColors, speed, 1, multiTransition);
    }
    
    setCurrentGeneratedPayload(payload);
    setSnifferInput(payload.map(b => b.toString(16).toUpperCase().padStart(2, '0')).join(' '));
  }, [protocol, r, g, b, r2, g2, b2, speed, length, transitionType, brightness, segmentDirection, rbmPattern, isDeviceMic, musicModeType, musicPatternId, musicSensitivity, candleAmplitude, multiColors, multiTransition, powerTesterState]);

  const handleSendSniffer = async (hexStr: string) => {
    if (!writeToDevice) return;
    try {
      const bytes = hexStr.replace(/[^0-9A-Fa-f]/g, '').match(/.{1,2}/g)?.map(b => parseInt(b, 16)) || [];
      if (bytes.length === 0) return;
      await writeToDevice(bytes, snifferTarget === 'ALL' ? undefined : snifferTarget);
      
      if (!isSnifferPaused) {
        setSnifferLogs(prev => [{ dir: 'TX', hex: bytes.map(b => b.toString(16).padStart(2, '0').toUpperCase()).join(' '), t: Date.now(), dev: snifferTarget }, ...prev]);
      }
    } catch (e) {
      console.warn("Sniffer TX Error", e);
    }
  };

  function formatTime(ms: number): string {
    const d = new Date(ms);
    return `${d.toLocaleTimeString('en-US', { hour12: false })}`;
  }

  const renderAnalyzedPayload = (hex: string) => {
    const bytes = hex.split(' ');
    if (bytes.length === 0) return <Text style={{ color: '#000' }}>{hex}</Text>;
    
    if (bytes[0] === '81' && bytes.length >= 14) {
      const power = bytes[1] === '01' ? 'ON' : 'OFF';
      const powerColor = bytes[1] === '01' ? '#00ff80' : '#ff4040';
      const pattern = parseInt(bytes[2], 16);
      const speed = parseInt(bytes[4], 16);
      const colorHex = `#${bytes[6] || '00'}${bytes[7] || '00'}${bytes[8] || '00'}`;
      return (
        <View style={{ flexDirection: 'row', flexWrap: 'wrap', alignItems: 'center', gap: 6 }}>
          <Text style={{ color: '#FFD700', fontSize: 11, fontWeight: 'bold' }}>[STATUS]</Text>
          <Text style={{ color: powerColor, fontSize: 11 }}>PWR:{power}</Text>
          <Text style={{ color: '#c084fc', fontSize: 11 }}>PAT:{pattern}</Text>
          <Text style={{ color: '#AADDFF', fontSize: 11 }}>SPD:{speed}</Text>
          <View style={{ width: 12, height: 12, backgroundColor: colorHex, borderRadius: 2, borderWidth: 1, borderColor: '#fff' }} />
          <Text style={{ color: '#666', fontSize: 10, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>{hex}</Text>
        </View>
      );
    }
    
    // 0x63 hardware settings RESPONSE (12-byte reply from device)
    // Points are little-endian swapped: bytes[8]=Low, bytes[9]=High
    if (bytes[0] === '63' && bytes.length >= 12) {
       const icType = parseInt(bytes[3], 16);
       // SWAP: index 8=Low, index 9=High
       const ptsLow  = parseInt(bytes[8], 16);
       const ptsHigh = parseInt(bytes[9], 16);
       const points = (ptsHigh << 8) | ptsLow;
       const sorting = parseInt(bytes[10], 16);
       const icName = IC_TYPES[icType] || `IC#${icType}`;
       const sortName = COLOR_SORTING_RGB[sorting] || `SORT#${sorting}`;
       return (
         <View style={{ flexDirection: 'row', flexWrap: 'wrap', alignItems: 'center', gap: 6 }}>
           <Text style={{ color: '#00cc88', fontSize: 11, fontWeight: 'bold' }}>[0x63 HW RESP]</Text>
           <Text style={{ color: '#16A34A', fontSize: 11 }}>LEDS:{points}</Text>
           <Text style={{ color: '#007AFF', fontSize: 11 }}>{icName}</Text>
           <Text style={{ color: '#ff70ff', fontSize: 11 }}>{sortName}</Text>
           <Text style={{ color: '#666', fontSize: 10, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>{hex}</Text>
         </View>
       );
    }
    // 0x62 hardware settings WRITE (sent to device)
    if (bytes[0] === '62' && bytes.length >= 11) {
       const ptsHigh = parseInt(bytes[1], 16);
       const ptsLow  = parseInt(bytes[2], 16);
       const points  = (ptsHigh << 8) | ptsLow;
       const segHigh = parseInt(bytes[3], 16);
       const segLow  = parseInt(bytes[4], 16);
       const segs    = (segHigh << 8) | segLow;
       const icType  = parseInt(bytes[5], 16);
       const sorting = parseInt(bytes[6], 16);
       const icName  = IC_TYPES[icType] || `IC#${icType}`;
       const sortName = COLOR_SORTING_RGB[sorting] || `SORT#${sorting}`;
       return (
         <View style={{ flexDirection: 'row', flexWrap: 'wrap', alignItems: 'center', gap: 6 }}>
           <Text style={{ color: '#ff9500', fontSize: 11, fontWeight: 'bold' }}>[0x62 HW WRITE]</Text>
           <Text style={{ color: '#16A34A', fontSize: 11 }}>LEDS:{points}</Text>
           <Text style={{ color: '#ff70ff', fontSize: 11 }}>SEGS:{segs}</Text>
           <Text style={{ color: '#007AFF', fontSize: 11 }}>{icName}</Text>
           <Text style={{ color: '#c084fc', fontSize: 11 }}>{sortName}</Text>
           <Text style={{ color: '#666', fontSize: 10, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>{hex}</Text>
         </View>
       );
    }
    // V2-wrapped 0x63 response (starts with 00 xx 80 00 ...)
    if (bytes[0] === '00' && bytes.length > 12 && bytes[2] === '80') {
      const inner = bytes.slice(8);
      if (inner[0] === '63' && inner.length >= 12) {
        const ptsLow  = parseInt(inner[8], 16);
        const ptsHigh = parseInt(inner[9], 16);
        const points = (ptsHigh << 8) | ptsLow;
        const icType = parseInt(inner[3], 16);
        const icName = IC_TYPES[icType] || `IC#${icType}`;
        const sortName = COLOR_SORTING_RGB[parseInt(inner[10], 16)] || '?';
        return (
          <View style={{ flexDirection: 'row', flexWrap: 'wrap', alignItems: 'center', gap: 6 }}>
            <Text style={{ color: '#00cc88', fontSize: 11, fontWeight: 'bold' }}>[0x63 WRAPPED]</Text>
            <Text style={{ color: '#16A34A', fontSize: 11 }}>LEDS:{points}</Text>
            <Text style={{ color: '#007AFF', fontSize: 11 }}>{icName}</Text>
            <Text style={{ color: '#ff70ff', fontSize: 11 }}>{sortName}</Text>
            <Text style={{ color: '#666', fontSize: 10, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>{hex}</Text>
          </View>
        );
      }
    }

    if (bytes[0] === '81' && bytes.length < 10) return <Text style={{ color: '#00ccff', fontSize: 11 }}>[PING] {hex}</Text>;
    if (bytes[0] === '2B') return <Text style={{ color: '#ff7000', fontSize: 11 }}>[R-CONFIG] {hex}</Text>;

    return <Text style={{ color: '#FFF', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', fontSize: 11 }}>{hex}</Text>;
  };

  const filteredLogs = snifferTarget === 'ALL' ? snifferLogs : snifferLogs.filter(m => m.dev === snifferTarget || !m.dev);

  return (
    <Modal visible={visible} animationType="slide" presentationStyle="fullScreen" onRequestClose={onClose}>
      <SafeAreaView style={[styles.root, { backgroundColor: '#F5F5F7', paddingTop: Platform.OS === 'android' ? 32 : 0 }]}>
        
        {/* Header matching Tester & Programmer */}
        <View style={styles.modalHeader}>
          <TouchableOpacity onPress={onClose} style={{ padding: 12, backgroundColor: '#E5E5EA', borderRadius: 20 }}>
            <MaterialCommunityIcons name="close" size={22} color="#FFF" />
          </TouchableOpacity>
          <View style={{ flex: 1, alignItems: 'center' }}>
            <Text style={styles.title}>HARDWARE TESTER</Text>
          </View>
          <TouchableOpacity style={{ padding: 12, opacity: 0 }}>
             <MaterialCommunityIcons name="close" size={22} color="#FFF" />
          </TouchableOpacity>
        </View>

        <View style={{ flex: 1, paddingHorizontal: 16 }}>
          {/* Chronological Native Display as a unified scroll canvas */}
          <View style={{ flex: 1, backgroundColor: '#FFF', borderRadius: 8, borderWidth: 1, borderColor: '#E5E5EA', padding: 8 }}>
            <ScrollView style={{ flex: 1 }} nestedScrollEnabled>
               <View style={{ paddingBottom: 16 }}>
                    {/* Target Device UI Matrix with Scan built in */}
                    <View style={{ marginBottom: 16 }}>
                       <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 }}>
                          <Text style={{ color: '#666', fontSize: 12, fontWeight: 'bold' }}>TARGET DEVICE NODE</Text>
                          <TouchableOpacity onPress={() => setIsScanningExpanded(!isScanningExpanded)} style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: '#E5E5EA', paddingLeft: 8, paddingRight: 4, paddingVertical: 4, borderRadius: 12 }}>
                             <Text style={{ color: '#007AFF', fontSize: 10, fontWeight: 'bold', marginRight: 4 }}>
                               {isScanningExpanded ? 'HIDE SCANNER' : 'SCAN DEVICES'}
                             </Text>
                             <MaterialCommunityIcons name={isScanningExpanded ? "chevron-up" : "radar"} size={14} color="#007AFF" />
                          </TouchableOpacity>
                       </View>
                       
                       {isScanningExpanded && (
                          <View style={{ backgroundColor: '#F9F9F9', borderRadius: 8, padding: 12, marginBottom: 12, borderWidth: 1, borderColor: '#E5E5EA' }}>
                             <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 }}>
                               <Text style={{ color: '#000', fontSize: 12 }}>Discoverable BLE Nodes</Text>
                               <TouchableOpacity 
                                 onPress={handleScan}
                                 style={{ backgroundColor: '#007AFF', paddingHorizontal: 16, paddingVertical: 6, borderRadius: 16, flexDirection: 'row', alignItems: 'center' }}
                               >
                                 {isScanning && <ActivityIndicator size="small" color="#FFF" style={{ marginRight: 6 }} />}
                                 <Text style={{ color: '#FFF', fontWeight: 'bold', fontSize: 11 }}>{isScanning ? "SCANNING..." : "SCAN"}</Text>
                               </TouchableOpacity>
                             </View>
                             <View style={{ maxHeight: 150 }}>
                               <ScrollView nestedScrollEnabled>
                                 {allDevices.length === 0 && <Text style={{ color: '#666', fontSize: 11 }}>No un-connected devices found.</Text>}
                                 {allDevices.map((d, i) => {
                                   const isConn = connectedDevices.find(c => c.id === d.id);
                                   return (
                                     <View key={i} style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', paddingVertical: 8, borderBottomWidth: 1, borderBottomColor: '#E5E5EA' }}>
                                       <View>
                                         <Text style={{ color: '#000', fontSize: 13, fontWeight: 'bold' }}>{d.name || 'Unknown Device'}</Text>
                                         <Text style={{ color: '#666', fontSize: 10 }}>{d.id}</Text>
                                       </View>
                                       {isConn ? (
                                         <TouchableOpacity onPress={handleDisconnect} style={{ backgroundColor: '#ff4040', paddingHorizontal: 12, paddingVertical: 6, borderRadius: 12 }}>
                                           <Text style={{ color: '#FFF', fontSize: 10, fontWeight: 'bold' }}>DISCONNECT</Text>
                                         </TouchableOpacity>
                                       ) : (
                                         <TouchableOpacity onPress={() => connectToDevice && connectToDevice(d)} style={{ backgroundColor: '#00ff80', paddingHorizontal: 12, paddingVertical: 6, borderRadius: 12 }}>
                                           <Text style={{ color: '#000', fontSize: 10, fontWeight: 'bold' }}>CONNECT</Text>
                                         </TouchableOpacity>
                                       )}
                                     </View>
                                   );
                                 })}
                               </ScrollView>
                             </View>
                          </View>
                       )}

                       <ScrollView horizontal showsHorizontalScrollIndicator={false}>
                          <TouchableOpacity onPress={() => setSnifferTarget('ALL')} style={{ paddingHorizontal: 14, paddingVertical: 6, borderRadius: 16, backgroundColor: snifferTarget === 'ALL' ? '#007AFF' : '#F0F0F0', marginRight: 8, borderWidth: 1, borderColor: snifferTarget === 'ALL' ? '#007AFF' : '#CCC' }}>
                             <Text style={{ color: snifferTarget === 'ALL' ? '#FFF' : '#000', fontSize: 11, fontWeight: 'bold' }}>ALL</Text>
                          </TouchableOpacity>
                          {connectedDevices.map(d => (
                             <TouchableOpacity key={d.id} onPress={() => setSnifferTarget(d.id)} style={{ paddingHorizontal: 14, paddingVertical: 6, borderRadius: 16, backgroundColor: snifferTarget === d.id ? '#007AFF' : '#F0F0F0', marginRight: 8, borderWidth: 1, borderColor: snifferTarget === d.id ? '#007AFF' : '#CCC' }}>
                               <Text style={{ color: snifferTarget === d.id ? '#FFF' : '#000', fontSize: 11, fontWeight: 'bold' }}>{d.name || d.id.slice(-5)}</Text>
                             </TouchableOpacity>
                          ))}
                       </ScrollView>
                    </View>

                    {/* Extracted Hardware Specifications (Moved up) */}
                    <View style={{ marginBottom: 16, backgroundColor: '#F0FDF4', padding: 12, borderRadius: 8, borderWidth: 1, borderColor: '#A7F3D0' }}>
                      <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12, flexWrap: 'wrap', gap: 8 }}>
                        <Text style={{ color: '#16A34A', fontSize: 11, fontWeight: 'bold' }}>DETECTED STATE</Text>
                        <TouchableOpacity onPress={() => {
                            const payload10 = ZenggeProtocol.queryHardwareConfig();
                            const payload63 = ZenggeProtocol.wrapCommand([0x63, 0x14, 0x00, 0x00]);
                            const payload32 = ZenggeProtocol.wrapCommand([0x32, 0x3A, 0x3B, 0x0F]); // Query Firmware Boot Details
                            handleSendSniffer(payload10.map(b => b.toString(16).padStart(2,'0').toUpperCase()).join(' '));
                            setTimeout(() => handleSendSniffer(payload63.map(b => b.toString(16).padStart(2,'0').toUpperCase()).join(' ')), 100);
                            setTimeout(() => handleSendSniffer(payload32.map(b => b.toString(16).padStart(2,'0').toUpperCase()).join(' ')), 200);
                        }} style={{ backgroundColor: '#E0F2FE', borderColor: '#00f0ff', borderWidth: 1, paddingHorizontal: 12, paddingVertical: 4, borderRadius: 6 }}>
                          <Text style={{ color: '#007AFF', fontSize: 9, fontWeight: 'bold' }}>AUTO PROBE (0x63 HW QUERY)</Text>
                        </TouchableOpacity>
                      </View>
                      <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 6 }}>
                        <View style={{ flex: 1, minWidth: '30%', backgroundColor: '#FFF', padding: 8, borderRadius: 6, borderWidth: 1, borderColor: '#E5E5EA' }}>
                           <Text style={{ color: '#666', fontSize: 9, marginBottom: 2 }}>POINTS</Text>
                           <Text style={{ color: detectedPoints ? '#000' : '#888', fontSize: 13, fontWeight: 'bold' }}>{detectedPoints || '?'}</Text>
                        </View>
                        <View style={{ flex: 1, minWidth: '30%', backgroundColor: '#FFF', padding: 8, borderRadius: 6, borderWidth: 1, borderColor: '#E5E5EA' }}>
                           <Text style={{ color: '#666', fontSize: 9, marginBottom: 2 }}>SEGMENTS</Text>
                           <Text style={{ color: detectedSegments ? '#000' : '#888', fontSize: 13, fontWeight: 'bold' }}>{detectedSegments || '?'}</Text>
                        </View>
                        <View style={{ flex: 1, minWidth: '30%', backgroundColor: '#FFF', padding: 8, borderRadius: 6, borderWidth: 1, borderColor: '#E5E5EA' }}>
                           <Text style={{ color: '#666', fontSize: 9, marginBottom: 2 }}>COLOR ORDER</Text>
                           <Text style={{ color: detectedColorOrder ? '#000' : '#888', fontSize: 13, fontWeight: 'bold' }}>{detectedColorOrder || '?'}</Text>
                        </View>
                        <View style={{ flex: 1, minWidth: '30%', backgroundColor: '#FFF', padding: 8, borderRadius: 6, borderWidth: 1, borderColor: '#E5E5EA' }}>
                           <Text style={{ color: '#666', fontSize: 9, marginBottom: 2 }}>STRIP IC</Text>
                           <Text style={{ color: detectedStripType ? '#000' : '#888', fontSize: 13, fontWeight: 'bold' }}>{detectedStripType || '?'}</Text>
                        </View>
                        <View style={{ flex: 1, minWidth: '30%', backgroundColor: '#FFF', padding: 8, borderRadius: 6, borderWidth: 1, borderColor: '#E5E5EA' }}>
                           <Text style={{ color: '#666', fontSize: 9, marginBottom: 2 }}>FIRMWARE</Text>
                           <Text style={{ color: detectedFirmware ? '#000' : '#888', fontSize: 13, fontWeight: 'bold' }}>{detectedFirmware || '?'}</Text>
                        </View>
                      </View>
                    </View>

                    {/* Integrated Hardware Tester: Payload Generator */}
                    <View style={{ marginBottom: 16, backgroundColor: '#F5F5F7', padding: 12, borderRadius: 8, borderWidth: 1, borderColor: '#BAE6FD' }}>
                      <Text style={{ color: '#007AFF', fontSize: 13, fontWeight: 'bold', marginBottom: 12 }}>PAYLOAD BUILDER & INJECTOR</Text>
                      
                      <View style={{ marginBottom: -30, marginTop: -30, width: '100%', height: 180, transform: [{ scale: 0.4 }] }}>
                        <ProductVisualizer 
                          product={snifferTarget.toLowerCase().includes('soul') ? 'SOULZ' : 'HALOZ'} 
                          color={currentPreviewColor} 
                          mode="FIXED" 
                          patternId={1} 
                          isPaired={!!writeToDevice}
                          points={parseInt(String(detectedPoints || hwPoints || 43), 10)}
                          brightness={brightness}
                          speed={speed}
                          isPoweredOn={powerTesterState}
                          statusText={`LIVE PREVIEW\nEXPECTED TX BYTES: ${currentGeneratedPayload.length}`}
                          rawHexPayload={currentGeneratedPayload}
                          audioMagnitude={0}
                        />
                      </View>

                      <Text style={{ color: '#666', fontSize: 11, fontWeight: 'bold', marginBottom: 8 }}>PROTOCOL TARGET LAYER</Text>
                      <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 6, marginBottom: 16 }}>
                        {['0x59', '0x51', '0x42', '0x73', '0x71', '0x2A', 'CANDLE', 'CAMERA', 'MULTI'].map(p => (
                          <TouchableOpacity 
                            key={p} 
                            onPress={() => setProtocol(p as any)}
                            style={{ paddingHorizontal: 12, paddingVertical: 8, borderRadius: 6, backgroundColor: protocol === p ? '#007AFF' : '#F0F0F0', flexGrow: 1, alignItems: 'center' }}
                          >
                            <Text style={{ color: protocol === p ? '#FFF' : '#333', fontWeight: 'bold', fontSize: 10 }}>{p}</Text>
                          </TouchableOpacity>
                        ))}
                      </View>

                      <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 16 }}>
                        <View style={{ width: 32, height: 32, borderRadius: 16, backgroundColor: currentPreviewColor, borderWidth: 2, borderColor: '#CCC', marginRight: 12 }} />
                        <Text style={{ color: '#666', fontSize: 11, fontWeight: 'bold' }}>RGB COLOR BUFFER: {r},{g},{b}</Text>
                      </View>

                      <View style={{ gap: 8, marginBottom: 16 }}>
                        <View><Text style={{ color: '#FF4444', fontSize: 9, fontWeight: 'bold' }}>R ({r})</Text><CustomSlider value={r} minimumValue={0} maximumValue={255} onValueChange={setR} /></View>
                        <View><Text style={{ color: '#44FF44', fontSize: 9, fontWeight: 'bold' }}>G ({g})</Text><CustomSlider value={g} minimumValue={0} maximumValue={255} onValueChange={setG} /></View>
                        <View><Text style={{ color: '#4444FF', fontSize: 9, fontWeight: 'bold' }}>B ({b})</Text><CustomSlider value={b} minimumValue={0} maximumValue={255} onValueChange={setB} /></View>
                      </View>

                      {protocol === '0x59' && (
                        <View style={{ gap: 8, marginBottom: 16, backgroundColor: '#FFF7ED', padding: 10, borderRadius: 6 }}>
                          <Text style={{ color: '#EA580C', fontSize: 11, fontWeight: 'bold', marginBottom: 4 }}>0x59 SEGMENT PARAMETERS</Text>
                          <SettingWithExplanation title={`Segment Array Scale (${length})`} description="Calculated node length sent to IC buffers." ColorsKey={{textMuted: '#aaa', primary: '#EA580C'}}>
                            <CustomSlider value={length} minimumValue={1} maximumValue={600} onValueChange={setLength} />
                          </SettingWithExplanation>
                          <SettingWithExplanation title={`Animation Transition (${transitionType})`} description="3 = Wave Pattern natively on most chips." ColorsKey={{textMuted: '#aaa', primary: '#EA580C'}}>
                             <CustomSlider value={transitionType} minimumValue={0} maximumValue={3} onValueChange={setTransitionType} />
                          </SettingWithExplanation>
                          <SettingWithExplanation title={`Matrix Flow Extent (${segmentDirection})`} description="Direction polarity 1 vs 0." ColorsKey={{textMuted: '#aaa', primary: '#EA580C'}}>
                             <CustomSlider value={segmentDirection} minimumValue={0} maximumValue={1} onValueChange={setSegmentDirection} />
                          </SettingWithExplanation>
                        </View>
                      )}

                      {protocol === '0x42' && (
                        <View style={{ gap: 8, marginBottom: 16, backgroundColor: '#F0FDF4', padding: 10, borderRadius: 6 }}>
                          <SettingWithExplanation title={`ROM Memory RBM Block (${rbmPattern})`} description="Internal loop index reference logic 1-210" ColorsKey={{textMuted: '#aaa', primary: '#16A34A'}}>
                             <CustomSlider value={rbmPattern} minimumValue={1} maximumValue={210} onValueChange={setRbmPattern} />
                          </SettingWithExplanation>
                        </View>
                      )}

                      {protocol === 'CAMERA' && (
                        <View style={{ height: 280, borderRadius: 12, overflow: 'hidden', borderWidth: 1, borderColor: '#00f0ff', marginBottom: 16 }}>
                          <CameraTracker isActive={true} onColorDetected={(h) => {
                             setR(parseInt(h.slice(1, 3), 16)); setG(parseInt(h.slice(3, 5), 16)); setB(parseInt(h.slice(5, 7), 16));
                             if (writeToDevice) {
                               const bBytes = ZenggeProtocol.setColor(r, g, b);
                               handleSendSniffer(bBytes.map(bb => bb.toString(16).padStart(2,'0').toUpperCase()).join(' '));
                             }
                          }} />
                        </View>
                      )}

                      <View style={{ gap: 8, marginTop: 8, marginBottom: 24 }}>
                        <SettingWithExplanation title={`Transmission Throttle / Speed (${speed})`} description="Interval byte mapped across 0-100 scales." ColorsKey={{textMuted: '#aaa', primary: '#00f0ff'}}>
                          <CustomSlider value={speed} minimumValue={0} maximumValue={100} onValueChange={setSpeed} />
                        </SettingWithExplanation>
                        <SettingWithExplanation title={`Software Bit-Biting Dimmer (${brightness}%)`} description="Software scaled byte modifier to cap amplitude draws natively before dispatching frame payloads." ColorsKey={{textMuted: '#aaa', primary: '#00f0ff'}}>
                          <CustomSlider value={brightness} minimumValue={0} maximumValue={100} onValueChange={setBrightness} />
                        </SettingWithExplanation>
                      </View>

                      {/* Integrated Dictionary */}
                      <Text style={{ color: '#666', fontSize: 12, fontWeight: 'bold', marginBottom: 8, marginTop: 8 }}>DIAGNOSTIC DICTIONARY & QUICK PROBES</Text>
                      <ScrollView horizontal showsHorizontalScrollIndicator={false} style={{ flexDirection: 'row', marginBottom: 16, flexGrow: 0 }}>
                        {[
                          { label: '0x81 Sync', hex: '81 8A 8B 96' },
                          { label: '0x10 Ping', hex: '10 00 00 10' },
                          { label: '0x2B RConfig', hex: '2B 2C 2D 00' },
                          { label: '0x32 Boot', hex: '32 3A 3B 0F' },
                          { label: '0x63 HW QUERY ✓', hex: null, highlight: true },
                        ].map((probe) => (
                          <TouchableOpacity 
                            key={probe.label} 
                            onPress={() => { const h = probe.hex ?? ZenggeProtocol.queryHardwareSettings(false).map(b => b.toString(16).padStart(2,'0').toUpperCase()).join(' '); setSnifferInput(h); }}
                            style={{ backgroundColor: '#E0F2FE', borderColor: '#00f0ff', borderWidth: 1, paddingHorizontal: 14, paddingVertical: 6, borderRadius: 20, marginRight: 8 }}
                          >
                            <Text style={{ color: '#007AFF', fontWeight: 'bold', fontSize: 11 }}>{probe.label}</Text>
                          </TouchableOpacity>
                        ))}
                      </ScrollView>

                      {/* Hardware Configuration Injector */}
                      <View style={{ marginBottom: 16, backgroundColor: '#F9F9F9', padding: 12, borderRadius: 8, borderWidth: 1, borderColor: '#E5E5EA' }}>
                         <Text style={{ color: '#007AFF', fontSize: 11, fontWeight: 'bold', marginBottom: 4 }}>HARDWARE CONFIG INJECTOR</Text>
                          <Text style={{ color: '#888', fontSize: 10, marginBottom: 12 }}>0x62 = correct new protocol · 0x81 = legacy</Text>
                         <View style={{ flexDirection: 'row', gap: 6, marginBottom: 12, flexWrap: 'wrap' }}>
                            <View style={{ flex: 1, minWidth: '22%' }}>
                              <Text style={{ color: '#bbb', fontSize: 9, marginBottom: 4 }}>POINTS</Text>
                              <TextInput style={{ backgroundColor: '#F5F5F7', color: '#FFF', padding: 6, borderRadius: 4, fontFamily: 'monospace', fontSize: 12 }} value={hwPoints} onChangeText={setHwPoints} keyboardType="numeric" />
                            </View>
                            <View style={{ flex: 1, minWidth: '22%' }}>
                              <Text style={{ color: '#bbb', fontSize: 9, marginBottom: 4 }}>SEGMENTS</Text>
                              <TextInput style={{ backgroundColor: '#F5F5F7', color: '#FFF', padding: 6, borderRadius: 4, fontFamily: 'monospace', fontSize: 12 }} value={hwSegments} onChangeText={setHwSegments} keyboardType="numeric" />
                            </View>
                            <View style={{ flex: 1, minWidth: '22%' }}>
                              <Text style={{ color: '#bbb', fontSize: 9, marginBottom: 4 }}>COLOR</Text>
                              <TextInput style={{ backgroundColor: '#F5F5F7', color: '#FFF', padding: 6, borderRadius: 4, fontFamily: 'monospace', fontSize: 12 }} value={hwColorOrder} onChangeText={setHwColorOrder} autoCapitalize="characters" />
                            </View>
                            <View style={{ flex: 1, minWidth: '22%' }}>
                              <Text style={{ color: '#bbb', fontSize: 9, marginBottom: 4 }}>TYPE</Text>
                              <TextInput style={{ backgroundColor: '#F5F5F7', color: '#FFF', padding: 6, borderRadius: 4, fontFamily: 'monospace', fontSize: 12 }} value={hwStripType} onChangeText={setHwStripType} autoCapitalize="characters" />
                            </View>
                         </View>
                         <View style={{ flexDirection: 'row', gap: 8 }}>
                            <TouchableOpacity style={{ flex: 2, backgroundColor: 'rgba(0,204,136,0.15)', borderColor: '#00cc88', borderWidth: 1, padding: 10, borderRadius: 6, alignItems: 'center' }} onPress={() => {
                               const icIndex = parseInt(Object.keys(IC_TYPES).find(k => IC_TYPES[parseInt(k)] === hwStripType) || '1');
                               const sortIndex = parseInt(Object.keys(COLOR_SORTING_RGB).find(k => COLOR_SORTING_RGB[parseInt(k)] === hwColorOrder) || '2');
                               const pBytes = ZenggeProtocol.writeHardwareSettings(parseInt(hwPoints)||43, parseInt(hwSegments)||1, icIndex, sortIndex);
                               setSnifferInput(pBytes.map(b => b.toString(16).padStart(2,'0').toUpperCase()).join(' '));
                            }}>
                               <Text style={{ color: '#00cc88', fontWeight: 'bold', fontSize: 11 }}>PREPARE 0x62 ✓</Text>
                            </TouchableOpacity>
                            <TouchableOpacity style={{ flex: 1, backgroundColor: 'rgba(0,122,255,0.1)', borderColor: '#007AFF', borderWidth: 1, padding: 10, borderRadius: 6, alignItems: 'center' }} onPress={() => {
                               const pBytes = ZenggeProtocol.setHardwareConfig(parseInt(hwPoints)||43, hwColorOrder, hwStripType, parseInt(hwSegments)||1);
                               setSnifferInput(pBytes.map(b => b.toString(16).padStart(2,'0').toUpperCase()).join(' '));
                            }}>
                               <Text style={{ color: '#007AFF', fontWeight: 'bold', fontSize: 11 }}>0x81 (legacy)</Text>
                            </TouchableOpacity>
                          </View>
                      </View>

                    </View>

                    <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginTop: 16, marginBottom: 4, alignItems: 'center' }}>
                      <Text style={{ color: '#666', fontSize: 11, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', fontWeight: 'bold' }}>CHRONOLOGICAL TIMELINE STREAM</Text>
                      <TouchableOpacity onPress={() => setIsSnifferPaused(!isSnifferPaused)} style={{ backgroundColor: isSnifferPaused ? '#FEE2E2' : '#DCFCE7', paddingHorizontal: 8, paddingVertical: 4, borderRadius: 4 }}>
                        <Text style={{ color: isSnifferPaused ? '#DC2626' : '#16A34A', fontSize: 9, fontWeight: 'bold' }}>{isSnifferPaused ? '▶ RESUME' : '⏸ PAUSE'}</Text>
                      </TouchableOpacity>
                    </View>
                 </View>
               <FlatList 
                  data={filteredLogs}
                  scrollEnabled={false}
                  keyExtractor={(item, i) => `${item.t}-${i}`}
                  ListEmptyComponent={<Text style={{ color: '#888', fontFamily: 'monospace', fontSize: 12, marginTop: 20, textAlign: 'center' }}>Waiting for BLE traffic...</Text>}
                  renderItem={({ item }) => (
                    <View style={{ flexDirection: 'row', marginBottom: 8, opacity: Date.now() - item.t > 15000 ? 0.7 : 1, alignItems: 'flex-start', borderBottomWidth: 1, borderBottomColor: '#E5E5EA', paddingBottom: 6 }}>
                      <Text style={{ color: item.dir === 'TX' ? '#D97706' : '#16A34A', width: 26, fontWeight: 'bold', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', fontSize: 11 }}>{item.dir}</Text>
                      <Text style={{ color: '#777', width: 66, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', fontSize: 10, marginTop: 1 }}>{formatTime(item.t)}</Text>
                   <View style={{ flex: 1 }}>
                     {renderAnalyzedPayload(item.hex)}
                     {(item.dev && item.dev !== 'ALL') && (
                       <Text style={{ color: '#555', fontSize: 9 }}>DEV: {item.dev.slice(-5)}</Text>
                     )}
                   </View>
                 </View>
               )}
            />
          </ScrollView>

          {/* Floating TX Injection Bar Statically Docked */}
          <View style={{ flexDirection: 'row', marginTop: 12, paddingTop: 12, borderTopWidth: 1, borderTopColor: '#333' }}>
            <TextInput
               style={{ flex: 1, backgroundColor: '#222', color: '#FFF', borderColor: '#E5E5EA', borderWidth: 1, borderRadius: 8, paddingHorizontal: 12, height: 44, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', fontSize: 14 }}
               placeholder="ex: 81 8A 8B 96"
               placeholderTextColor="#666"
               value={snifferInput}
               onChangeText={setSnifferInput}
               autoCapitalize="none"
            />
            <TouchableOpacity 
               style={{ backgroundColor: '#007AFF', justifyContent: 'center', alignItems: 'center', paddingHorizontal: 20, borderRadius: 8, marginLeft: 8 }}
               onPress={() => handleSendSniffer(snifferInput)}
            >
               <Text style={{ color: '#000', fontWeight: 'bold', fontSize: 12 }}>TX: FIRE PAYLOAD</Text>
            </TouchableOpacity>
          </View>
          </View>
        </View>
      </SafeAreaView>
    </Modal>
  );
}

const styles = StyleSheet.create({
  root: { flex: 1 },
  modalHeader: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between',
    paddingHorizontal: 8, paddingBottom: 16, paddingTop: Platform.OS === 'ios' ? 0 : 16,
    borderBottomWidth: 1, borderBottomColor: '#E5E5EA', marginBottom: 16
  },
  title: { fontSize: 16, fontWeight: '800', letterSpacing: 1, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', color: '#000' },
});
