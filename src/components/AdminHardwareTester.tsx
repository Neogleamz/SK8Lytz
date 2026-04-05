/**
 * AdminHardwareTester.tsx — SK8Lytz Admin BLE Diagnostic Terminal
 *
 * Hidden admin panel for Zengge BLE protocol inspection and hardware configuration.
 * Accessible via secret gesture in DashboardScreen. NOT visible to end users.
 *
 * Three-tab architecture:
 *  SNIFFER  — Live RX/TX BLE packet timeline with protocol parsing
 *             Parses 0x63 (HW Settings Response), 0x81 (Status), 0x62 (HW Write)
 *             Logs RAW_PAYLOAD events (the ONLY source of this event type)
 *  FACTORY  — Payload generator for all Zengge command types
 *             (0x59 Solid Array, 0x51 Custom, 0x42 RBM, 0x73 Music, CANDLE, MULTI)
 *             Editable hex field with direct TX to connected hardware
 *  CONFIG   — EEPROM register configurator
 *             Polls hardware via 0x63 (current gen) or 0x81 (legacy)
 *             Burns new settings via 0x62 write command
 *
 * Depends on: ZenggeProtocol (all command generation), AppLogger (RAW_PAYLOAD)
 * Platform: React Native (Android + Web)
 */
import React, { useState, useEffect } from 'react';
import {
  View, Text, StyleSheet, Modal, TouchableOpacity, ScrollView,
  FlatList, Platform, SafeAreaView, TextInput, ActivityIndicator
} from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { ZenggeProtocol, IC_TYPES, COLOR_SORTING_RGB } from '../protocols/ZenggeProtocol';
import CustomSlider from './CustomSlider';

interface AdminHardwareTesterProps {
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

type TabType = 'SNIFFER' | 'FACTORY' | 'CONFIG';

export default function AdminHardwareTester({ 
  visible, onClose, writeToDevice, liveRxPayload, connectedDevices = [],
  allDevices = [], isScanning = false, handleScan, connectToDevice, handleDisconnect 
}: AdminHardwareTesterProps) {
  
  const [activeTab, setActiveTab] = useState<TabType>('SNIFFER');
  const [snifferTarget, setSnifferTarget] = useState<string>('ALL');
  
  // Tab A: Sniffer Timeline States
  const [snifferLogs, setSnifferLogs] = useState<{dir: 'TX' | 'RX', hex: string, t: number, dev?: string}[]>([]);
  const [isSnifferPaused, setIsSnifferPaused] = useState(false);
  const [isDeviceMenuExpanded, setIsDeviceMenuExpanded] = useState(false);

  // Tab B: Payload Factory States
  const [protocol, setProtocol] = useState<'0x59' | '0x51' | '0x42' | '0x73' | '0x71' | '0x2A' | 'CANDLE' | 'CAMERA' | 'MULTI'>('0x59');
  const [r, setR] = useState(255); const [g, setG] = useState(0); const [b, setB] = useState(0);
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
  const [r2, setR2] = useState<number>(0); const [g2, setG2] = useState<number>(0); const [b2, setB2] = useState<number>(255);
  const [rfAuthMode, setRfAuthMode] = useState<'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED'>('ALLOW_ALL');
  const [candleAmplitude, setCandleAmplitude] = useState<number>(2);
  const [powerTesterState, setPowerTesterState] = useState<boolean>(true);
  const [factoryPayloadHex, setFactoryPayloadHex] = useState<string>('');

  // Tab C: Configurator States
  const [hwPoints, setHwPoints] = useState('43');
  const [hwSegments, setHwSegments] = useState('1');
  const [hwColorOrder, setHwColorOrder] = useState('GRB');
  const [hwStripType, setHwStripType] = useState('WS2812B');

  const [detectedPoints, setDetectedPoints] = useState<number | null>(null);
  const [detectedSegments, setDetectedSegments] = useState<number | null>(null);
  const [detectedColorOrder, setDetectedColorOrder] = useState<string | null>(null);
  const [detectedStripType, setDetectedStripType] = useState<string | null>(null);
  const [detectedFirmware, setDetectedFirmware] = useState<string | null>(null);

  // RX Listener hook
  useEffect(() => {
    if (visible && liveRxPayload && liveRxPayload.payloadHex && !isSnifferPaused) {
      if (snifferTarget !== 'ALL' && liveRxPayload.deviceId !== snifferTarget) return;

      setSnifferLogs(prev => {
        const merged = [...prev];
        const rxHash = { dir: 'RX' as const, hex: liveRxPayload.payloadHex, t: liveRxPayload.timestamp || Date.now(), dev: liveRxPayload.deviceId };
        if (!merged.find(m => m.hex === rxHash.hex && (Date.now() - m.t < 50))) {
            merged.unshift(rxHash);
        }
        // Memory boundary limit
        if (merged.length > 500) return merged.slice(0, 500);
        return merged;
      });

      const bytes = liveRxPayload.payloadHex.split(' ').map(h => parseInt(h, 16));
      
      const hw63 = ZenggeProtocol.parseHardwareSettingsResponse(bytes);
      if (hw63) {
        setDetectedPoints(hw63.ledPoints); setDetectedColorOrder(hw63.colorSortingName); setDetectedStripType(hw63.icName);
      } else {
        const parsedHardware = ZenggeProtocol.parseHardwareConfig(bytes);
        if (parsedHardware) {
          setDetectedPoints(parsedHardware.points); setDetectedSegments(parsedHardware.segments); setDetectedColorOrder(parsedHardware.sorting); setDetectedStripType(parsedHardware.stripType);
        }
      }

      if (bytes[0] === 0x32 && bytes.length > 5) {
         setDetectedFirmware(`${bytes[2]}.${bytes[3]}.${bytes[4]}`);
      }
    }
  }, [liveRxPayload, visible, isSnifferPaused, snifferTarget]);

  // Payload Generator hook
  useEffect(() => {
    let payload: number[] = [];
    const factor = brightness / 100;
    const color = { r: Math.round(r * factor), g: Math.round(g * factor), b: Math.round(b * factor) };

    if (protocol === '0x59') {
      const totalPoints = length;
      const totalLen = (totalPoints * 3) + 9;
      payload = new Array(totalLen);
      payload[0] = 0x59; payload[1] = (totalLen >> 8) & 0xFF; payload[2] = totalLen & 0xFF;
      let idx = 3;
      for (let i = 0; i < totalPoints; i++) { payload[idx++] = color.r; payload[idx++] = color.g; payload[idx++] = color.b; }
      payload[idx++] = (totalPoints >> 8) & 0xFF; payload[idx++] = totalPoints & 0xFF;
      payload[idx++] = transitionType; payload[idx++] = speed; payload[idx++] = segmentDirection;
      payload[idx] = ZenggeProtocol.calculateChecksum(payload.slice(0, totalLen - 1));
      payload = ZenggeProtocol.wrapCommand(payload);
    } else if (protocol === '0x51') {
      const bg = { r: 0, g: 0, b: 0 };
      payload = ZenggeProtocol.setCustomMode([
        { mode: transitionType === 0 ? 1 : transitionType, speed, color1: color, color2: bg },
        { mode: transitionType === 0 ? 1 : transitionType, speed, color1: bg, color2: color }
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
    
    setFactoryPayloadHex(payload.map(b => b.toString(16).toUpperCase().padStart(2, '0')).join(' '));
  }, [protocol, r, g, b, r2, g2, b2, speed, length, transitionType, brightness, segmentDirection, rbmPattern, isDeviceMic, musicModeType, musicPatternId, musicSensitivity, candleAmplitude, multiColors, multiTransition, powerTesterState]);

  const handleTransmitPayload = async (hexStr: string) => {
    if (!writeToDevice) return;
    try {
      const bytes = hexStr.replace(/[^0-9A-Fa-f]/g, '').match(/.{1,2}/g)?.map(bi => parseInt(bi, 16)) || [];
      if (bytes.length === 0) return;
      await writeToDevice(bytes, snifferTarget === 'ALL' ? undefined : snifferTarget);
      
      if (!isSnifferPaused) {
        setSnifferLogs(prev => [{ dir: 'TX', hex: bytes.map(bi => bi.toString(16).padStart(2, '0').toUpperCase()).join(' '), t: Date.now(), dev: snifferTarget }, ...prev]);
      }
    } catch (e) {
      console.warn("Sniffer TX Error", e);
    }
  };

  const formatTime = (ms: number): string => new Date(ms).toLocaleTimeString('en-US', { hour12: false });

  const renderAnalyzedPayload = (hex: string) => {
    const bytes = hex.split(' ');
    if (bytes.length === 0) return <Text style={{ color: '#000' }}>{hex}</Text>;
    
    if (bytes[0] === '81' && bytes.length >= 14) {
      const powerColor = bytes[1] === '01' ? '#00ff80' : '#ff4040';
      const colorHex = `#${bytes[6] || '00'}${bytes[7] || '00'}${bytes[8] || '00'}`;
      return (
        <View style={styles.parsedPillRow}>
          <Text style={{ color: '#FFD700', fontSize: 11, fontWeight: 'bold' }}>[STATUS]</Text>
          <Text style={{ color: powerColor, fontSize: 11 }}>PWR:{bytes[1] === '01' ? 'ON' : 'OFF'}</Text>
          <Text style={{ color: '#c084fc', fontSize: 11 }}>PAT:{parseInt(bytes[2], 16)}</Text>
          <Text style={{ color: '#AADDFF', fontSize: 11 }}>SPD:{parseInt(bytes[4], 16)}</Text>
          <View style={{ width: 12, height: 12, backgroundColor: colorHex, borderRadius: 2 }} />
          <Text style={styles.hexText}>{hex}</Text>
        </View>
      );
    }
    
    if (bytes[0] === '63' && bytes.length >= 12) {
       const pts = (parseInt(bytes[9], 16) << 8) | parseInt(bytes[8], 16);
       return (
         <View style={styles.parsedPillRow}>
           <Text style={{ color: '#00cc88', fontSize: 11, fontWeight: 'bold' }}>[0x63 HW RESP]</Text>
           <Text style={{ color: '#16A34A', fontSize: 11 }}>LEDS:{pts}</Text>
           <Text style={{ color: '#007AFF', fontSize: 11 }}>{IC_TYPES[parseInt(bytes[3], 16)] || 'IC'}</Text>
           <Text style={{ color: '#ff70ff', fontSize: 11 }}>{COLOR_SORTING_RGB[parseInt(bytes[10], 16)] || 'SORT'}</Text>
           <Text style={styles.hexText}>{hex}</Text>
         </View>
       );
    }

    if (bytes[0] === '62' && bytes.length >= 11) {
       const pts = (parseInt(bytes[1], 16) << 8) | parseInt(bytes[2], 16);
       return (
         <View style={styles.parsedPillRow}>
           <Text style={{ color: '#ff9500', fontSize: 11, fontWeight: 'bold' }}>[0x62 HW WRITE]</Text>
           <Text style={{ color: '#16A34A', fontSize: 11 }}>LEDS:{pts}</Text>
           <Text style={styles.hexText}>{hex}</Text>
         </View>
       );
    }

    if (bytes[0] === '81' && bytes.length < 10) return <Text style={{ color: '#00ccff', fontSize: 11 }}>[PING] {hex}</Text>;
    if (bytes[0] === '2B') return <Text style={{ color: '#ff7000', fontSize: 11 }}>[R-CONFIG] {hex}</Text>;

    return <Text style={{ color: '#FFF', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', fontSize: 11 }}>{hex}</Text>;
  };

  const filteredLogs = snifferTarget === 'ALL' ? snifferLogs : snifferLogs.filter(m => m.dev === snifferTarget || !m.dev);

  // Components UI Partitions
  const renderDeviceSelectMenu = () => (
    <View style={styles.deviceMenuCard}>
       <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 }}>
          <Text style={{ color: '#ccc', fontSize: 12, fontWeight: 'bold' }}>TARGET DEVICE NODE</Text>
          <TouchableOpacity onPress={() => setIsDeviceMenuExpanded(!isDeviceMenuExpanded)} style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: '#333', paddingHorizontal: 12, paddingVertical: 4, borderRadius: 12 }}>
             <Text style={{ color: '#00ccff', fontSize: 10, fontWeight: 'bold', marginRight: 4 }}>
               {isDeviceMenuExpanded ? 'HIDE SCANNER' : 'SCAN DEVICES'}
             </Text>
             <MaterialCommunityIcons name={isDeviceMenuExpanded ? "chevron-up" : "radar"} size={14} color="#00ccff" />
          </TouchableOpacity>
       </View>
       {isDeviceMenuExpanded && (
          <View style={{ backgroundColor: '#1A1A1A', borderRadius: 8, padding: 12, marginBottom: 12, borderWidth: 1, borderColor: '#333' }}>
             <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 }}>
               <Text style={{ color: '#aaa', fontSize: 12 }}>Discoverable BLE Nodes</Text>
               <TouchableOpacity onPress={handleScan} style={{ backgroundColor: '#007AFF', paddingHorizontal: 16, paddingVertical: 6, borderRadius: 16, flexDirection: 'row', alignItems: 'center' }}>
                 {isScanning && <ActivityIndicator size="small" color="#FFF" style={{ marginRight: 6 }} />}
                 <Text style={{ color: '#FFF', fontWeight: 'bold', fontSize: 11 }}>{isScanning ? "SCANNING..." : "SCAN"}</Text>
               </TouchableOpacity>
             </View>
             <ScrollView style={{ maxHeight: 150 }} nestedScrollEnabled>
               {allDevices.length === 0 && <Text style={{ color: '#666', fontSize: 11 }}>No un-connected devices found.</Text>}
               {allDevices.map((d, i) => {
                 const isConn = connectedDevices.find(c => c.id === d.id);
                 return (
                   <View key={i} style={styles.deviceScanRow}>
                     <View>
                       <Text style={{ color: '#FFF', fontSize: 13, fontWeight: 'bold' }}>{d.name || 'Unknown Device'}</Text>
                       <Text style={{ color: '#888', fontSize: 10 }}>{d.id}</Text>
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
       )}
       <ScrollView horizontal showsHorizontalScrollIndicator={false}>
          <TouchableOpacity onPress={() => setSnifferTarget('ALL')} style={[styles.targetNodeBtn, snifferTarget === 'ALL' && styles.targetNodeBtnActive]}>
             <Text style={{ color: snifferTarget === 'ALL' ? '#FFF' : '#888', fontSize: 11, fontWeight: 'bold' }}>ALL</Text>
          </TouchableOpacity>
          {connectedDevices.map(d => (
             <TouchableOpacity key={d.id} onPress={() => setSnifferTarget(d.id)} style={[styles.targetNodeBtn, snifferTarget === d.id && styles.targetNodeBtnActive]}>
               <Text style={{ color: snifferTarget === d.id ? '#FFF' : '#888', fontSize: 11, fontWeight: 'bold' }}>{d.name || d.id.slice(-5)}</Text>
             </TouchableOpacity>
          ))}
       </ScrollView>
    </View>
  );

  return (
    <Modal visible={visible} animationType="slide" presentationStyle="fullScreen" onRequestClose={onClose}>
      <SafeAreaView style={styles.root}>
        <View style={styles.modalHeader}>
          <TouchableOpacity onPress={onClose} style={styles.closeBtn}>
            <MaterialCommunityIcons name="close" size={22} color="#FFF" />
          </TouchableOpacity>
          <View style={{ flex: 1, alignItems: 'center' }}>
            <Text style={styles.title}>ADMIN HARDWARE TESTER</Text>
          </View>
          <TouchableOpacity style={[styles.closeBtn, { opacity: 0 }]}><MaterialCommunityIcons name="close" size={22} color="#FFF" /></TouchableOpacity>
        </View>

        <View style={styles.tabContainer}>
           {(['SNIFFER', 'FACTORY', 'CONFIG'] as TabType[]).map(tab => (
              <TouchableOpacity key={tab} onPress={() => setActiveTab(tab)} style={[styles.tabBtn, activeTab === tab && styles.tabBtnActive]}>
                 <Text style={[styles.tabBtnText, activeTab === tab && styles.tabBtnTextActive]}>{tab}</Text>
              </TouchableOpacity>
           ))}
        </View>

        <View style={styles.contentContainer}>
            {renderDeviceSelectMenu()}

            {activeTab === 'SNIFFER' && (
              <View style={styles.panelBlock}>
                <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginBottom: 16, alignItems: 'center' }}>
                  <Text style={styles.panelTitle}>BLE TRACE TIMELINE</Text>
                  <TouchableOpacity onPress={() => setIsSnifferPaused(!isSnifferPaused)} style={[styles.pauseBtn, isSnifferPaused && { backgroundColor: '#ff4040' }]}>
                    <Text style={{ color: '#FFF', fontSize: 9, fontWeight: 'bold' }}>{isSnifferPaused ? '▶ RESUME' : '⏸ PAUSE'}</Text>
                  </TouchableOpacity>
                </View>
                <FlatList 
                  data={filteredLogs}
                  keyExtractor={(item, i) => `${item.t}-${i}`}
                  style={{ flex: 1, backgroundColor: '#000', borderRadius: 8, padding: 8, borderWidth: 1, borderColor: '#333' }}
                  contentContainerStyle={{ paddingBottom: 24 }}
                  renderItem={({ item }) => (
                    <View style={{ flexDirection: 'row', marginVertical: 4, borderBottomWidth: 1, borderBottomColor: '#1A1A1A', paddingBottom: 6 }}>
                      <View style={{ width: 44, alignItems: 'flex-start' }}>
                        <View style={{ backgroundColor: item.dir === 'TX' ? 'rgba(255, 64, 64, 0.2)' : 'rgba(0, 122, 255, 0.2)', paddingHorizontal: 4, borderRadius: 4, borderWidth: 1, borderColor: item.dir === 'TX' ? '#ff4040' : '#007AFF' }}>
                           <Text style={{ color: item.dir === 'TX' ? '#ff4040' : '#00f0ff', fontSize: 10, fontWeight: 'bold' }}>{item.dir}</Text>
                         </View>
                      </View>
                      <View style={{ flex: 1 }}>
                        {renderAnalyzedPayload(item.hex)}
                        <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginTop: 4 }}>
                          <Text style={{ color: '#444', fontSize: 9 }}>{formatTime(item.t)}</Text>
                          <Text style={{ color: '#444', fontSize: 9 }}>{item.dev?.slice(-5) || 'ALL'}</Text>
                        </View>
                      </View>
                    </View>
                  )}
                />
              </View>
            )}

            {activeTab === 'FACTORY' && (
              <ScrollView style={styles.panelBlock} contentContainerStyle={{ paddingBottom: 40 }}>
                <Text style={styles.panelTitle}>PAYLOAD BUILDER</Text>
                
                <ScrollView horizontal showsHorizontalScrollIndicator={false} style={{ marginBottom: 16 }}>
                  {(['0x59', '0x51', '0x42', 'CANDLE', 'MULTI', 'CAMERA'] as const).map(p => (
                    <TouchableOpacity key={p} onPress={() => setProtocol(p)} style={[styles.protocolChip, protocol === p && styles.protocolChipActive]}>
                      <Text style={[styles.protocolChipText, protocol === p && styles.protocolChipTextActive]}>{p}</Text>
                    </TouchableOpacity>
                  ))}
                </ScrollView>

                <View style={styles.factorySandbox}>
                   {/* Simplified Factory UI matching Protocol Sniffer capabilities but darkened */}
                   <View style={{ marginBottom: 16 }}>
                       <Text style={{ color: '#888', fontSize: 10, marginBottom: 8 }}>BASE RGB ANCHOR</Text>
                       <View style={{ flexDirection: 'row', gap: 8, marginBottom: 12 }}>
                         {[{l:'R',v:r,c:'#ff4040',f:setR}, {l:'G',v:g,c:'#00ff80',f:setG}, {l:'B',v:b,c:'#007AFF',f:setB}].map(clr => (
                            <View key={clr.l} style={{ flex: 1 }}>
                              <Text style={{ color: clr.c, fontSize: 12, fontWeight: 'bold', marginBottom: 6 }}>{clr.l} ({clr.v})</Text>
                              <CustomSlider value={clr.v} minimumValue={0} maximumValue={255} onValueChange={clr.f} />
                            </View>
                         ))}
                       </View>
                   </View>

                   <View style={{ marginBottom: 16 }}>
                       <Text style={{ color: '#888', fontSize: 10, marginBottom: 8 }}>FRAME METRICS</Text>
                       <Text style={{ color: '#ccc', fontSize: 12, marginBottom: 4 }}>Brightness: {brightness}%</Text>
                       <CustomSlider value={brightness} minimumValue={0} maximumValue={100} onValueChange={setBrightness} />
                       <View style={{ height: 12 }} />
                       <Text style={{ color: '#ccc', fontSize: 12, marginBottom: 4 }}>Speed: {speed}%</Text>
                       <CustomSlider value={speed} minimumValue={0} maximumValue={100} onValueChange={setSpeed} />
                   </View>

                   {(protocol === '0x42') && (
                       <View style={{ marginBottom: 16 }}>
                           <Text style={{ color: '#888', fontSize: 10, marginBottom: 8 }}>RBM MATRIX BLOCK</Text>
                           <Text style={{ color: '#00ccff', fontSize: 12, marginBottom: 4 }}>Pattern ID: {rbmPattern}</Text>
                           <CustomSlider value={rbmPattern} minimumValue={1} maximumValue={210} onValueChange={setRbmPattern} />
                       </View>
                   )}
                </View>

                <View style={styles.transmitBox}>
                   <TextInput 
                     style={styles.hexInput} 
                     value={factoryPayloadHex} 
                     onChangeText={setFactoryPayloadHex}
                   />
                   <TouchableOpacity style={styles.txBtn} onPress={() => handleTransmitPayload(factoryPayloadHex)}>
                      <Text style={{ color: '#000', fontWeight: 'bold' }}>TX FRAME</Text>
                   </TouchableOpacity>
                </View>
              </ScrollView>
            )}

            {activeTab === 'CONFIG' && (
              <ScrollView style={styles.panelBlock} contentContainerStyle={{ paddingBottom: 40 }}>
                <Text style={styles.panelTitle}>EEPROM METADATA REGISTERS</Text>

                <View style={styles.diagnosticsBox}>
                   <Text style={{ color: '#00f0ff', fontSize: 11, fontWeight: 'bold', marginBottom: 8 }}>Extracted Read-Only Values</Text>
                   <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 12 }}>
                      <Text style={{ color: '#aaa', fontSize: 12 }}>LEDS: <Text style={{ color: '#FFF' }}>{detectedPoints ?? '--'}</Text></Text>
                      <Text style={{ color: '#aaa', fontSize: 12 }}>SEGS: <Text style={{ color: '#FFF' }}>{detectedSegments ?? '--'}</Text></Text>
                      <Text style={{ color: '#aaa', fontSize: 12 }}>SORT: <Text style={{ color: '#FFF' }}>{detectedColorOrder ?? '--'}</Text></Text>
                      <Text style={{ color: '#aaa', fontSize: 12 }}>CHIP: <Text style={{ color: '#FFF' }}>{detectedStripType ?? '--'}</Text></Text>
                      <Text style={{ color: '#aaa', fontSize: 12 }}>FW: <Text style={{ color: '#FFF' }}>{detectedFirmware ?? '--'}</Text></Text>
                   </View>
                   <View style={{ flexDirection: 'row', gap: 8, marginTop: 16 }}>
                     <TouchableOpacity style={styles.queryBtn} onPress={() => handleTransmitPayload('63 14 15 2E')}>
                        <Text style={styles.queryBtnText}>POLL 0x63 (Current Gen)</Text>
                     </TouchableOpacity>
                     <TouchableOpacity style={styles.queryBtn} onPress={() => handleTransmitPayload('81 8A 8B 96')}>
                        <Text style={styles.queryBtnText}>POLL 0x81 (Legacy)</Text>
                     </TouchableOpacity>
                   </View>
                </View>

                <View style={styles.burnBox}>
                  <Text style={{ color: '#ff4040', fontSize: 11, fontWeight: 'bold', marginBottom: 12 }}>WRITE PROTECTED REGISTERS (DANGER)</Text>
                  <View style={{ flexDirection: 'row', gap: 8, marginBottom: 16 }}>
                     <View style={{ flex: 1 }}>
                       <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>POINTS (1-1024)</Text>
                       <TextInput style={styles.darkInput} value={hwPoints} onChangeText={setHwPoints} keyboardType="numeric" />
                     </View>
                     <View style={{ flex: 1 }}>
                       <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>SEGMENTS</Text>
                       <TextInput style={styles.darkInput} value={hwSegments} onChangeText={setHwSegments} keyboardType="numeric" />
                     </View>
                     <View style={{ flex: 1 }}>
                       <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>COLOR</Text>
                       <TextInput style={styles.darkInput} value={hwColorOrder} onChangeText={setHwColorOrder} autoCapitalize="characters" />
                     </View>
                     <View style={{ flex: 1 }}>
                       <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>IC CHIP</Text>
                       <TextInput style={styles.darkInput} value={hwStripType} onChangeText={setHwStripType} autoCapitalize="characters" />
                     </View>
                  </View>
                  
                  <View style={{ flexDirection: 'row', gap: 8 }}>
                     <TouchableOpacity style={[styles.txBtn, { flex: 2, backgroundColor: '#16A34A' }]} onPress={() => {
                         const icIndex = parseInt(Object.keys(IC_TYPES).find(k => IC_TYPES[parseInt(k)] === hwStripType) || '1');
                         const sortIndex = parseInt(Object.keys(COLOR_SORTING_RGB).find(k => COLOR_SORTING_RGB[parseInt(k)] === hwColorOrder) || '2');
                         const pBytes = ZenggeProtocol.writeHardwareSettings(parseInt(hwPoints)||43, parseInt(hwSegments)||1, icIndex, sortIndex);
                         handleTransmitPayload(pBytes.map(b => b.toString(16).padStart(2,'0').toUpperCase()).join(' '));
                     }}>
                        <Text style={{ color: '#FFF', fontWeight: 'bold' }}>BURN TO V2 (0x62)</Text>
                     </TouchableOpacity>
                     <TouchableOpacity style={[styles.txBtn, { flex: 1, backgroundColor: '#333' }]} onPress={() => {
                         const pBytes = ZenggeProtocol.setHardwareConfig(parseInt(hwPoints)||43, hwColorOrder, hwStripType, parseInt(hwSegments)||1);
                         handleTransmitPayload(pBytes.map(b => b.toString(16).padStart(2,'0').toUpperCase()).join(' '));
                     }}>
                        <Text style={{ color: '#FFF', fontWeight: 'bold' }}>BURN LEGACY</Text>
                     </TouchableOpacity>
                  </View>
                </View>
              </ScrollView>
            )}

        </View>
      </SafeAreaView>
    </Modal>
  );
}

const styles = StyleSheet.create({
  root: { flex: 1, backgroundColor: '#0A0A0A' },
  modalHeader: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', padding: 16, borderBottomWidth: 1, borderBottomColor: '#222', paddingTop: Platform.OS === 'android' ? 32 : 16 },
  closeBtn: { padding: 8, backgroundColor: '#222', borderRadius: 20 },
  title: { color: '#FFF', fontSize: 14, fontWeight: '900', letterSpacing: 1 },
  tabContainer: { flexDirection: 'row', borderBottomWidth: 1, borderBottomColor: '#222', paddingHorizontal: 16 },
  tabBtn: { flex: 1, paddingVertical: 14, alignItems: 'center', borderBottomWidth: 2, borderBottomColor: 'transparent' },
  tabBtnActive: { borderBottomColor: '#00f0ff' },
  tabBtnText: { color: '#666', fontSize: 11, fontWeight: 'bold' },
  tabBtnTextActive: { color: '#00ccff' },
  contentContainer: { flex: 1, padding: 16 },
  deviceMenuCard: { backgroundColor: '#111', padding: 12, borderRadius: 8, borderWidth: 1, borderColor: '#222', marginBottom: 16 },
  deviceScanRow: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', paddingVertical: 8, borderBottomWidth: 1, borderBottomColor: '#222' },
  targetNodeBtn: { paddingHorizontal: 14, paddingVertical: 6, borderRadius: 16, backgroundColor: '#222', marginRight: 8, borderWidth: 1, borderColor: '#333' },
  targetNodeBtnActive: { backgroundColor: '#007AFF', borderColor: '#00f0ff' },
  panelBlock: { flex: 1 },
  panelTitle: { color: '#FFF', fontSize: 12, fontWeight: 'bold', marginBottom: 16, letterSpacing: 1 },
  pauseBtn: { backgroundColor: '#333', paddingHorizontal: 12, paddingVertical: 6, borderRadius: 4 },
  parsedPillRow: { flexDirection: 'row', flexWrap: 'wrap', alignItems: 'center', gap: 6 },
  hexText: { color: '#888', fontSize: 10, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' },
  protocolChip: { paddingHorizontal: 12, paddingVertical: 6, borderRadius: 16, backgroundColor: '#222', marginRight: 8, borderWidth: 1, borderColor: '#333' },
  protocolChipActive: { backgroundColor: 'rgba(0, 240, 255, 0.1)', borderColor: '#00f0ff' },
  protocolChipText: { color: '#888', fontSize: 11, fontWeight: 'bold' },
  protocolChipTextActive: { color: '#00f0ff' },
  factorySandbox: { backgroundColor: '#111', padding: 16, borderRadius: 8, borderWidth: 1, borderColor: '#222', marginBottom: 16 },
  transmitBox: { flexDirection: 'row', gap: 8 },
  hexInput: { flex: 1, backgroundColor: '#000', color: '#00f0ff', borderWidth: 1, borderColor: '#333', borderRadius: 8, paddingHorizontal: 12, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', fontSize: 13 },
  txBtn: { backgroundColor: '#00ccff', justifyContent: 'center', alignItems: 'center', paddingHorizontal: 20, borderRadius: 8, paddingVertical: 10 },
  diagnosticsBox: { backgroundColor: 'rgba(0, 122, 255, 0.05)', padding: 16, borderRadius: 8, borderWidth: 1, borderColor: 'rgba(0, 122, 255, 0.3)', marginBottom: 24 },
  queryBtn: { flex: 1, backgroundColor: '#000', borderWidth: 1, borderColor: '#007AFF', padding: 8, borderRadius: 6, alignItems: 'center' },
  queryBtnText: { color: '#00ccff', fontSize: 10, fontWeight: 'bold' },
  burnBox: { backgroundColor: 'rgba(255, 64, 64, 0.05)', padding: 16, borderRadius: 8, borderWidth: 1, borderColor: 'rgba(255, 64, 64, 0.3)' },
  darkInput: { backgroundColor: '#000', color: '#FFF', padding: 8, borderRadius: 4, fontFamily: 'monospace', fontSize: 12, borderWidth: 1, borderColor: '#333' }
});
