import React, { useState, useEffect, useCallback } from 'react';
import {
  View, Text, StyleSheet, Modal, TouchableOpacity, ScrollView,
  FlatList, Platform, SafeAreaView, TextInput, ActivityIndicator
} from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../context/ThemeContext';
import { AppLogger } from '../services/AppLogger';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';

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

      // 2. Parse Intercepted Payload to Extract Hardware Specs organically
      const bytes = liveRxPayload.payloadHex.split(' ').map(h => parseInt(h, 16));
      
      const parsedHardware = ZenggeProtocol.parseHardwareConfig(bytes);
      if (parsedHardware) {
         setDetectedPoints(parsedHardware.points);
         setDetectedSegments(parsedHardware.segments);
         setDetectedColorOrder(parsedHardware.sorting);
         setDetectedStripType(parsedHardware.stripType);
      }

      // 0x32 ping reveals Firmware on some controllers. Simple hook wrapper.
      if (bytes[0] === 0x32 && bytes.length > 5) {
         setDetectedFirmware(`${bytes[2]}.${bytes[3]}.${bytes[4]}`);
      }

    }
  }, [liveRxPayload, visible, isSnifferPaused, snifferTarget]);

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
    if (bytes.length === 0) return <Text style={{ color: '#FFF' }}>{hex}</Text>;
    
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
          <Text style={{ color: '#888', fontSize: 10, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>{hex}</Text>
        </View>
      );
    }
    
    if ((bytes[0] === '00' && bytes[1] === '63') || bytes[0] === '63') {
       const offset = bytes[0] === '00' ? 1 : 0;
       if (bytes.length > offset + 5) {
         const b2 = parseInt(bytes[offset + 1], 16);
         const b3 = parseInt(bytes[offset + 2], 16);
         const points = (b2 << 8) | b3;
         const segments = parseInt(bytes[offset + 4], 16);
         return (
           <View style={{ flexDirection: 'row', flexWrap: 'wrap', alignItems: 'center', gap: 6 }}>
             <Text style={{ color: '#00f0ff', fontSize: 11, fontWeight: 'bold' }}>[HARDWARE]</Text>
             <Text style={{ color: '#00ff80', fontSize: 11 }}>LEDS:{points}</Text>
             <Text style={{ color: '#ff70ff', fontSize: 11 }}>SEGS:{segments}</Text>
             <Text style={{ color: '#888', fontSize: 10, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>{hex}</Text>
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
      <SafeAreaView style={[styles.root, { backgroundColor: '#111', paddingTop: Platform.OS === 'android' ? 32 : 0 }]}>
        
        {/* Header matching Tester & Programmer */}
        <View style={styles.modalHeader}>
          <TouchableOpacity onPress={onClose} style={{ padding: 12, backgroundColor: '#333', borderRadius: 20 }}>
            <MaterialCommunityIcons name="close" size={22} color="#FFF" />
          </TouchableOpacity>
          <View style={{ flex: 1, alignItems: 'center' }}>
            <Text style={styles.title}>PROTOCOL SNIFFER</Text>
          </View>
          <TouchableOpacity style={{ padding: 12, opacity: 0 }}>
             <MaterialCommunityIcons name="close" size={22} color="#FFF" />
          </TouchableOpacity>
        </View>

        <View style={{ flex: 1, paddingHorizontal: 16 }}>
          {/* Chronological Native Display as a unified scroll canvas */}
          <View style={{ flex: 1, backgroundColor: '#000', borderRadius: 8, borderWidth: 1, borderColor: '#333', padding: 8 }}>
            <FlatList 
               data={filteredLogs}
               keyExtractor={(_, i) => String(i)}
               ListHeaderComponent={() => (
                 <View style={{ paddingBottom: 16 }}>
                    {/* Target Device UI Matrix with Scan built in */}
                    <View style={{ marginBottom: 16 }}>
                       <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 }}>
                          <Text style={{ color: '#bbb', fontSize: 12, fontWeight: 'bold' }}>TARGET DEVICE NODE</Text>
                          <TouchableOpacity onPress={() => setIsScanningExpanded(!isScanningExpanded)} style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: '#333', paddingLeft: 8, paddingRight: 4, paddingVertical: 4, borderRadius: 12 }}>
                             <Text style={{ color: '#00f0ff', fontSize: 10, fontWeight: 'bold', marginRight: 4 }}>
                               {isScanningExpanded ? 'HIDE SCANNER' : 'SCAN DEVICES'}
                             </Text>
                             <MaterialCommunityIcons name={isScanningExpanded ? "chevron-up" : "radar"} size={14} color="#00f0ff" />
                          </TouchableOpacity>
                       </View>
                       
                       {isScanningExpanded && (
                          <View style={{ backgroundColor: '#222', borderRadius: 8, padding: 12, marginBottom: 12, borderWidth: 1, borderColor: '#333' }}>
                             <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 }}>
                               <Text style={{ color: '#FFF', fontSize: 12 }}>Discoverable BLE Nodes</Text>
                               <TouchableOpacity 
                                 onPress={handleScan}
                                 style={{ backgroundColor: '#00AEEF', paddingHorizontal: 16, paddingVertical: 6, borderRadius: 16, flexDirection: 'row', alignItems: 'center' }}
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
                                     <View key={i} style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', paddingVertical: 8, borderBottomWidth: 1, borderBottomColor: '#333' }}>
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
                          </View>
                       )}

                       <ScrollView horizontal showsHorizontalScrollIndicator={false}>
                          <TouchableOpacity onPress={() => setSnifferTarget('ALL')} style={{ paddingHorizontal: 14, paddingVertical: 6, borderRadius: 16, backgroundColor: snifferTarget === 'ALL' ? '#00f0ff' : '#222', marginRight: 8, borderWidth: 1, borderColor: snifferTarget === 'ALL' ? '#00f0ff' : '#444' }}>
                             <Text style={{ color: snifferTarget === 'ALL' ? '#000' : '#FFF', fontSize: 11, fontWeight: 'bold' }}>ALL</Text>
                          </TouchableOpacity>
                          {connectedDevices.map(d => (
                             <TouchableOpacity key={d.id} onPress={() => setSnifferTarget(d.id)} style={{ paddingHorizontal: 14, paddingVertical: 6, borderRadius: 16, backgroundColor: snifferTarget === d.id ? '#00f0ff' : '#222', marginRight: 8, borderWidth: 1, borderColor: snifferTarget === d.id ? '#00f0ff' : '#444' }}>
                               <Text style={{ color: snifferTarget === d.id ? '#000' : '#FFF', fontSize: 11, fontWeight: 'bold' }}>{d.name || d.id.slice(-5)}</Text>
                             </TouchableOpacity>
                          ))}
                       </ScrollView>
                    </View>

                    {/* Dictionary */}
                    <Text style={{ color: '#bbb', fontSize: 12, fontWeight: 'bold', marginBottom: 8 }}>DIAGNOSTIC DICTIONARY</Text>
                    <ScrollView horizontal showsHorizontalScrollIndicator={false} style={{ flexDirection: 'row', marginBottom: 16, flexGrow: 0 }}>
                      {[
                        { label: '0x81 Sync', hex: '81 8A 8B 96' },
                        { label: '0x10 Ping', hex: '10 00 00 10' },
                        { label: '0x2B RConfig', hex: '2B 2C 2D 00' },
                        { label: '0x32 Boot', hex: '32 3A 3B 0F' },
                        { label: '0x63 IC Probe', hex: '63 14 00 00' }
                      ].map((probe) => (
                        <TouchableOpacity 
                          key={probe.label} 
                          onPress={() => handleSendSniffer(probe.hex)}
                          style={{ backgroundColor: 'rgba(0, 240, 255, 0.1)', borderColor: '#00f0ff', borderWidth: 1, paddingHorizontal: 14, paddingVertical: 6, borderRadius: 20, marginRight: 8 }}
                        >
                          <Text style={{ color: '#00f0ff', fontWeight: 'bold', fontSize: 11 }}>{probe.label}</Text>
                        </TouchableOpacity>
                      ))}
                    </ScrollView>

                    {/* Hardware Configuration Injector */}
                    <View style={{ marginBottom: 16, backgroundColor: '#222', padding: 12, borderRadius: 8, borderWidth: 1, borderColor: '#444' }}>
                       <Text style={{ color: '#00f0ff', fontSize: 11, fontWeight: 'bold', marginBottom: 12 }}>HARDWARE CONFIG INJECTOR (0x81)</Text>
                       <View style={{ flexDirection: 'row', gap: 6, marginBottom: 12, flexWrap: 'wrap' }}>
                          <View style={{ flex: 1, minWidth: '22%' }}>
                            <Text style={{ color: '#bbb', fontSize: 9, marginBottom: 4 }}>POINTS</Text>
                            <TextInput style={{ backgroundColor: '#111', color: '#FFF', padding: 6, borderRadius: 4, fontFamily: 'monospace', fontSize: 12 }} value={hwPoints} onChangeText={setHwPoints} keyboardType="numeric" />
                          </View>
                          <View style={{ flex: 1, minWidth: '22%' }}>
                            <Text style={{ color: '#bbb', fontSize: 9, marginBottom: 4 }}>SEGMENTS</Text>
                            <TextInput style={{ backgroundColor: '#111', color: '#FFF', padding: 6, borderRadius: 4, fontFamily: 'monospace', fontSize: 12 }} value={hwSegments} onChangeText={setHwSegments} keyboardType="numeric" />
                          </View>
                          <View style={{ flex: 1, minWidth: '22%' }}>
                            <Text style={{ color: '#bbb', fontSize: 9, marginBottom: 4 }}>COLOR</Text>
                            <TextInput style={{ backgroundColor: '#111', color: '#FFF', padding: 6, borderRadius: 4, fontFamily: 'monospace', fontSize: 12 }} value={hwColorOrder} onChangeText={setHwColorOrder} autoCapitalize="characters" />
                          </View>
                          <View style={{ flex: 1, minWidth: '22%' }}>
                            <Text style={{ color: '#bbb', fontSize: 9, marginBottom: 4 }}>TYPE</Text>
                            <TextInput style={{ backgroundColor: '#111', color: '#FFF', padding: 6, borderRadius: 4, fontFamily: 'monospace', fontSize: 12 }} value={hwStripType} onChangeText={setHwStripType} autoCapitalize="characters" />
                          </View>
                       </View>
                       <TouchableOpacity style={{ backgroundColor: '#ff4040', padding: 10, borderRadius: 6, alignItems: 'center' }} onPress={() => {
                          const pBytes = ZenggeProtocol.setHardwareConfig(parseInt(hwPoints)||43, hwColorOrder, hwStripType, parseInt(hwSegments)||1);
                          handleSendSniffer(pBytes.map(b => b.toString(16).padStart(2,'0').toUpperCase()).join(' '));
                       }}>
                          <Text style={{ color: '#FFF', fontWeight: 'bold', fontSize: 11 }}>TX SET HARDWARE MATRIX</Text>
                       </TouchableOpacity>
                    </View>

                    {/* Extracted Hardware Specifications */}
                    <View style={{ marginBottom: 16, backgroundColor: '#192518', padding: 12, borderRadius: 8, borderWidth: 1, borderColor: '#00ff8050' }}>
                      <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12, flexWrap: 'wrap', gap: 8 }}>
                        <Text style={{ color: '#00ff80', fontSize: 11, fontWeight: 'bold' }}>DETECTED STATE</Text>
                        <TouchableOpacity onPress={() => {
                            const payload10 = ZenggeProtocol.queryHardwareConfig();
                            const payload63 = ZenggeProtocol.wrapCommand([0x63, 0x14, 0x00, 0x00]);
                            const payload32 = ZenggeProtocol.wrapCommand([0x32, 0x3A, 0x3B, 0x0F]); // Query Firmware Boot Details
                            handleSendSniffer(payload10.map(b => b.toString(16).padStart(2,'0').toUpperCase()).join(' '));
                            setTimeout(() => handleSendSniffer(payload63.map(b => b.toString(16).padStart(2,'0').toUpperCase()).join(' ')), 100);
                            setTimeout(() => handleSendSniffer(payload32.map(b => b.toString(16).padStart(2,'0').toUpperCase()).join(' ')), 200);
                        }} style={{ backgroundColor: 'rgba(0, 240, 255, 0.1)', borderColor: '#00f0ff', borderWidth: 1, paddingHorizontal: 12, paddingVertical: 4, borderRadius: 6 }}>
                          <Text style={{ color: '#00f0ff', fontSize: 9, fontWeight: 'bold' }}>AUTO PROBE (0x10 / 0x63)</Text>
                        </TouchableOpacity>
                      </View>
                      <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 6 }}>
                        <View style={{ flex: 1, minWidth: '30%', backgroundColor: '#000', padding: 8, borderRadius: 6, borderWidth: 1, borderColor: '#333' }}>
                           <Text style={{ color: '#888', fontSize: 9, marginBottom: 2 }}>POINTS</Text>
                           <Text style={{ color: detectedPoints ? '#FFF' : '#444', fontSize: 13, fontWeight: 'bold' }}>{detectedPoints || '?'}</Text>
                        </View>
                        <View style={{ flex: 1, minWidth: '30%', backgroundColor: '#000', padding: 8, borderRadius: 6, borderWidth: 1, borderColor: '#333' }}>
                           <Text style={{ color: '#888', fontSize: 9, marginBottom: 2 }}>SEGMENTS</Text>
                           <Text style={{ color: detectedSegments ? '#FFF' : '#444', fontSize: 13, fontWeight: 'bold' }}>{detectedSegments || '?'}</Text>
                        </View>
                        <View style={{ flex: 1, minWidth: '30%', backgroundColor: '#000', padding: 8, borderRadius: 6, borderWidth: 1, borderColor: '#333' }}>
                           <Text style={{ color: '#888', fontSize: 9, marginBottom: 2 }}>COLOR ORDER</Text>
                           <Text style={{ color: detectedColorOrder ? '#FFF' : '#444', fontSize: 13, fontWeight: 'bold' }}>{detectedColorOrder || '?'}</Text>
                        </View>
                        <View style={{ flex: 1, minWidth: '30%', backgroundColor: '#000', padding: 8, borderRadius: 6, borderWidth: 1, borderColor: '#333' }}>
                           <Text style={{ color: '#888', fontSize: 9, marginBottom: 2 }}>STRIP IC</Text>
                           <Text style={{ color: detectedStripType ? '#FFF' : '#444', fontSize: 13, fontWeight: 'bold' }}>{detectedStripType || '?'}</Text>
                        </View>
                        <View style={{ flex: 1, minWidth: '30%', backgroundColor: '#000', padding: 8, borderRadius: 6, borderWidth: 1, borderColor: '#333' }}>
                           <Text style={{ color: '#888', fontSize: 9, marginBottom: 2 }}>FIRMWARE</Text>
                           <Text style={{ color: detectedFirmware ? '#FFF' : '#444', fontSize: 13, fontWeight: 'bold' }}>{detectedFirmware || '?'}</Text>
                        </View>
                      </View>
                    </View>

                    {/* TX Manual Injection */}
                    <View style={{ flexDirection: 'row', marginBottom: 8 }}>
                      <TextInput
                         style={{ flex: 1, backgroundColor: '#222', color: '#FFF', borderColor: '#444', borderWidth: 1, borderRadius: 8, paddingHorizontal: 12, height: 40, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', fontSize: 12 }}
                         placeholder="ex: 81 8A 8B 96"
                         placeholderTextColor="#666"
                         value={snifferInput}
                         onChangeText={setSnifferInput}
                         autoCapitalize="none"
                      />
                      <TouchableOpacity 
                         style={{ backgroundColor: '#00AEEF', justifyContent: 'center', alignItems: 'center', paddingHorizontal: 16, borderRadius: 8, marginLeft: 8 }}
                         onPress={() => handleSendSniffer(snifferInput)}
                      >
                         <Text style={{ color: '#000', fontWeight: 'bold', fontSize: 12 }}>TX</Text>
                      </TouchableOpacity>
                    </View>

                    <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginTop: 16, marginBottom: 4, alignItems: 'center' }}>
                      <Text style={{ color: '#bbb', fontSize: 11, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', fontWeight: 'bold' }}>CHRONOLOGICAL TIMELINE STREAM</Text>
                      <TouchableOpacity onPress={() => setIsSnifferPaused(!isSnifferPaused)} style={{ backgroundColor: isSnifferPaused ? '#ff404020' : '#00ff8020', paddingHorizontal: 8, paddingVertical: 4, borderRadius: 4 }}>
                        <Text style={{ color: isSnifferPaused ? '#ff4040' : '#00ff80', fontSize: 9, fontWeight: 'bold' }}>{isSnifferPaused ? '▶ RESUME' : '⏸ PAUSE'}</Text>
                      </TouchableOpacity>
                    </View>
                 </View>
               )}
               ListEmptyComponent={<Text style={{ color: '#555', fontFamily: 'monospace', fontSize: 12, marginTop: 20, textAlign: 'center' }}>Waiting for BLE traffic...</Text>}
               renderItem={({ item }) => (
                 <View style={{ flexDirection: 'row', marginBottom: 8, opacity: Date.now() - item.t > 15000 ? 0.7 : 1, alignItems: 'flex-start', borderBottomWidth: 1, borderBottomColor: '#222', paddingBottom: 6 }}>
                   <Text style={{ color: item.dir === 'TX' ? '#FFD700' : '#00ff80', width: 26, fontWeight: 'bold', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', fontSize: 11 }}>{item.dir}</Text>
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
    borderBottomWidth: 1, borderBottomColor: '#333', marginBottom: 16
  },
  title: { fontSize: 16, fontWeight: '800', letterSpacing: 1, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', color: '#FFF' },
});
