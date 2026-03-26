import React, { useState, useEffect } from 'react';
import { View, Text, TextInput, TouchableOpacity, ScrollView, StyleSheet, Platform, Switch, Alert } from 'react-native';
import { useTheme } from '../context/ThemeContext';
import { Typography, Layout } from '../theme/theme';
import CustomSlider from './CustomSlider';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import ProductVisualizer from './ProductVisualizer';
import CameraTracker from './CameraTracker';

interface HardwareTestControllerProps {
  writeToDevice?: (payload: number[]) => Promise<void>;
  device?: any;
  allDevices?: any[];
  isScanning?: boolean;
  handleScan?: () => void;
  connectToDevice?: (item: any) => Promise<void>;
  handleDisconnect?: () => void;
  isActuallyConnected?: boolean;
  lastRawNotification?: {deviceId: string, payloadHex: string} | null;
}

const SettingWithExplanation = ({ title, description, children, Colors }: { title: string, description: string, children: React.ReactNode, Colors: any }) => {
  const [expanded, setExpanded] = useState(false);
  return (
    <View style={{ marginBottom: 12 }}>
      <TouchableOpacity onPress={() => setExpanded(!expanded)} style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 8 }} activeOpacity={0.7}>
        <Text style={{ color: Colors.textMuted, flex: 1, fontWeight: 'bold' }}>{title}</Text>
        <MaterialCommunityIcons name={expanded ? "chevron-up" : "information-outline"} size={16} color={Colors.primary} />
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

export default function HardwareTestController({ 
  writeToDevice, 
  device,
  allDevices = [],
  isScanning = false,
  handleScan,
  connectToDevice,
  handleDisconnect,
  isActuallyConnected = false,
  lastRawNotification
}: HardwareTestControllerProps) {
  const { Colors, isDark } = useTheme();

  const points = device?.points || 43;
  const product = device?.name || 'DEVICE';
  
  // Hardware Re-Config State
  const [hwPoints, setHwPoints] = useState<number | null>(null);
  const [hwSegments, setHwSegments] = useState<number | null>(null);
  const [hwColorOrder, setHwColorOrder] = useState<string | null>(null);
  const [hwStripType, setHwStripType] = useState<string | null>(null);

  const [isQueryingHardware, setIsQueryingHardware] = useState(false);

  const COLOR_ORDERS = ['RGB', 'RBG', 'GRB', 'GBR', 'BRG', 'BGR'];
  const STRIP_TYPES = ['SM16703', 'WS2811', 'WS2812B', 'SK6812'];

  useEffect(() => {
    if (isQueryingHardware && lastRawNotification && lastRawNotification.deviceId === (device?.id)) {
       const payloadStr = lastRawNotification.payloadHex.split(' ');
       const payloadBytes = payloadStr.map(s => parseInt(s, 16));
       if (payloadBytes[0] === 0x10) {
           const parsed = ZenggeProtocol.parseHardwareConfig(payloadBytes);
           if (parsed) {
              setHwPoints(parsed.points);
              setHwSegments(parsed.segments);
              setHwColorOrder(parsed.sorting);
              setHwStripType(parsed.stripType);
              setIsQueryingHardware(false);
              Alert.alert(
                "Hardware Query Successful",
                `Points: ${parsed.points}\nSegments: ${parsed.segments}\nColor Order: ${parsed.sorting}\nPlatform IC: ${parsed.stripType}`
              );
           }
       }
    }
  }, [lastRawNotification]);
  
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
  
  // Music Mode Sync State (0x73)
  const [isDeviceMic, setIsDeviceMic] = useState<number>(1);
  const [musicModeType, setMusicModeType] = useState<number>(38); // 0x26
  const [musicPatternId, setMusicPatternId] = useState<number>(1);
  const [musicSensitivity, setMusicSensitivity] = useState<number>(50);
  const [r2, setR2] = useState<number>(0);
  const [g2, setG2] = useState<number>(0);
  const [b2, setB2] = useState<number>(255);
  
  // 2.4G RF Remote State (0x2A)
  const [rfAuthMode, setRfAuthMode] = useState<'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED'>('ALLOW_ALL');

  // Candle Mode State (0x39)
  const [candleAmplitude, setCandleAmplitude] = useState<number>(2);

  // MULTI DIY Mode State (Zengge 0x59 Overlay)
  const [multiColors, setMultiColors] = useState<string[]>(['#FF0000', '#00FF00', '#0000FF']);
  const [multiTransition, setMultiTransition] = useState<number>(3); // 3=Running Water default

  // Power State (0x71)
  const [powerTesterState, setPowerTesterState] = useState<boolean>(true);

  // Raw Hex State
  const [currentPayload, setCurrentPayload] = useState<number[]>([]);
  const [rawHexString, setRawHexString] = useState('');
  const [annotatedPayload, setAnnotatedPayload] = useState<{hex: string, label: string, color: string}[]>([]);
  const [isManualOverride, setIsManualOverride] = useState(false);

  // Generate payload
  useEffect(() => {
    if (isManualOverride) return;

    let payload: number[] = [];
    const factor = brightness / 100;
    const color = { 
      r: Math.round(r * factor), 
      g: Math.round(g * factor), 
      b: Math.round(b * factor) 
    };

    if (protocol === '0x59') {
      // Manual explicit length manipulation requested by user
      const totalPoints = length;
      const totalLen = (totalPoints * 3) + 9;
      payload = new Array(totalLen);
      payload[0] = 0x59;
      payload[1] = (totalLen >> 8) & 0xFF;
      payload[2] = totalLen & 0xFF;
      let idx = 3;
      for (let i = 0; i < totalPoints; i++) {
        payload[idx++] = color.r;
        payload[idx++] = color.g;
        payload[idx++] = color.b;
      }
      payload[idx++] = (totalPoints >> 8) & 0xFF;
      payload[idx++] = totalPoints & 0xFF;
      payload[idx++] = transitionType;
      payload[idx++] = speed;
      payload[idx++] = segmentDirection;
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
    
    const hex = payload.map(byte => byte.toString(16).toUpperCase().padStart(2, '0')).join(' ');
    setRawHexString(hex);
    setCurrentPayload([...payload]);

    const annotations: {hex: string, label: string, color: string}[] = [];
    const getHex = (v: number) => v.toString(16).toUpperCase().padStart(2, '0');
    
    payload.forEach((byte, i) => {
        let label = 'Unk';
        let color = '#888';
        const h = getHex(byte);
        
        if (i === 0) { label = 'Pfx'; color = '#666'; }
        else if (i === 1) { label = 'Seq'; color = '#888'; }
        else if (i === 2 || i === 3) { label = 'Mgc'; color = '#666'; }
        else if (i === 4 || i === 5) { label = 'Len'; color = '#4488FF'; }
        else if (i === 6) { label = 'Fmly'; color = '#AA44FF'; }
        else if (i === payload.length - 1) { label = 'Chk'; color = '#FF4444'; }
        else {
            const innerIdx = i - 7;
            const op = payload[7];
            
            if (op === 0x59) {
                if (innerIdx === 0) { label = 'OpC'; color = '#FFA500'; }
                else if (innerIdx === 1 || innerIdx === 2) { label = 'SegL'; color = '#4488FF'; }
                else {
                    const rgbEnd = 2 + (length * 3);
                    if (innerIdx > 2 && innerIdx <= rgbEnd) {
                        const channel = (innerIdx - 3) % 3;
                        label = channel === 0 ? 'R' : channel === 1 ? 'G' : 'B';
                        color = channel === 0 ? '#FF4444' : channel === 1 ? '#44FF44' : '#4444FF';
                    } else if (innerIdx === rgbEnd + 1 || innerIdx === rgbEnd + 2) {
                        label = 'Pts'; color = '#4488FF';
                    } else if (innerIdx === rgbEnd + 3) {
                        label = 'Typ'; color = '#FFD700';
                    } else if (innerIdx === rgbEnd + 4) {
                        label = 'Spd'; color = '#00FFFF';
                    } else if (innerIdx === rgbEnd + 5) {
                        label = 'Dir'; color = '#FF00FF';
                    } else if (innerIdx === rgbEnd + 6) {
                        label = 'iChk'; color = '#FF4444';
                    }
                }
            } else if (op === 0x42) {
                if (innerIdx === 0) { label = 'OpC'; color = '#00FF44'; }
                else if (innerIdx === 1) { label = 'Pat'; color = '#FFD700'; }
                else if (innerIdx === 2) { label = 'Spd'; color = '#00FFFF'; }
                else if (innerIdx === 3) { label = 'Bri'; color = '#FF8800'; }
                else if (innerIdx === 4 || innerIdx === 5 || innerIdx === 6 || innerIdx === 7) { label = 'PAD'; color = '#666'; }
                else if (innerIdx === 8) { label = 'iChk'; color = '#FF4444'; }
            } else if (op === 0x31) {
                if (innerIdx === 0) { label = 'OpC'; color = '#4488FF'; }
                else if (innerIdx === 1) { label = 'R'; color = '#FF4444'; }
                else if (innerIdx === 2) { label = 'G'; color = '#44FF44'; }
                else if (innerIdx === 3) { label = 'B'; color = '#4444FF'; }
                else if (innerIdx === 4 || innerIdx === 5) { label = 'W'; color = '#FFF'; }
                else if (innerIdx === 6 || innerIdx === 7) { label = 'PAD'; color = '#666'; }
                else if (innerIdx === 8) { label = 'iChk'; color = '#FF4444'; }
            } else if (op === 0x25) {
                if (innerIdx === 0) { label = 'OpC'; color = '#FF00FF'; }
                else if (innerIdx === 1) { label = 'Pat'; color = '#FFD700'; }
                else if (innerIdx === 2) { label = 'Spd'; color = '#00FFFF'; }
                else if (innerIdx === 3) { label = 'iChk'; color = '#FF4444'; }
            } else {
                if (innerIdx === 0) { label = 'OpC'; color = '#FF00FF'; }
                else { label = 'Dat'; color = '#888'; }
            }
        }
        annotations.push({ hex: h, label, color });
    });
    setAnnotatedPayload(annotations);

  }, [protocol, r, g, b, r2, g2, b2, speed, length, transitionType, brightness, segmentDirection, rbmPattern, isDeviceMic, musicModeType, musicPatternId, musicSensitivity, candleAmplitude, multiColors, multiTransition, isManualOverride, powerTesterState]);

  const handleSend = () => {
    if (!writeToDevice) return;
    try {
      // Parse the hex string back to byte array
      const text = rawHexString.replace(/[^0-9A-Fa-f]/g, '');
      const bytes = [];
      for (let i = 0; i < text.length; i += 2) {
        bytes.push(parseInt(text.substring(i, i + 2), 16));
      }
      if (bytes.length > 0) {
        writeToDevice(bytes);
      }
    } catch (e) {
      alert("Invalid Hex Format");
    }
  };

  const handleManualHexEdit = (text: string) => {
    setIsManualOverride(true);
    setRawHexString(text.toUpperCase());
  };

  const pushHardwareConfig = async () => {
    if (!writeToDevice) return;
    if (hwPoints === null || hwColorOrder === null || hwStripType === null || hwSegments === null) {
      Alert.alert("Missing Parameters", "Please query the physical hardware settings first, or manually parameterize all values before rewriting the core matrix.");
      return;
    }
    const payload = ZenggeProtocol.setHardwareConfig(hwPoints, hwColorOrder, hwStripType, hwSegments);
    await writeToDevice(payload);
  };

  const currentPreviewColor = `rgb(${r}, ${g}, ${b})`;

  const handleCameraColor = (hex: string) => {
    const rVal = parseInt(hex.slice(1, 3), 16);
    const gVal = parseInt(hex.slice(3, 5), 16);
    const bVal = parseInt(hex.slice(5, 7), 16);
    setR(rVal);
    setG(gVal);
    setB(bVal);
    if (writeToDevice) {
        // Auto-transmit native payload instantly on capture
        const payload = ZenggeProtocol.setColor(rVal, gVal, bVal);
        writeToDevice(payload);
    }
  };

  return (
    <View style={{ flex: 1 }}>
      <ScrollView style={{ flex: 1 }} contentContainerStyle={{ padding: Layout.padding, paddingBottom: 100 }}>
        
        {/* Connection Manager */}
      <View style={{ backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 8, padding: 12, marginBottom: 20 }}>
        <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 }}>
           <Text style={[Typography.title, { color: Colors.primary, fontSize: 14 }]}>Connection Manager</Text>
           <TouchableOpacity onPress={handleScan} style={{ backgroundColor: Colors.surfaceHighlight, paddingHorizontal: 12, paddingVertical: 6, borderRadius: 8 }}>
             <Text style={{ color: Colors.text, fontSize: 12, fontWeight: 'bold' }}>{isScanning ? 'SCANNING...' : 'SCAN'}</Text>
           </TouchableOpacity>
        </View>

        {isActuallyConnected ? (
          <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', backgroundColor: '#000', padding: 12, borderRadius: 8, borderWidth: 1, borderColor: '#333' }}>
            <View>
              <Text style={{ color: Colors.success, fontWeight: 'bold', marginBottom: 4 }}>CONNECTED</Text>
              <Text style={{ color: Colors.textMuted, fontSize: 12 }}>{device?.name || 'Unknown Device'} | {device?.id}</Text>
            </View>
            <TouchableOpacity onPress={handleDisconnect} style={{ backgroundColor: Colors.error, paddingHorizontal: 16, paddingVertical: 8, borderRadius: 8 }}>
              <Text style={{ color: '#FFF', fontWeight: 'bold', fontSize: 12 }}>DISCONNECT</Text>
            </TouchableOpacity>
          </View>
        ) : (
          <View>
            {(!allDevices || allDevices.length === 0) ? (
              <Text style={{ color: Colors.textMuted, fontSize: 12, fontStyle: 'italic', textAlign: 'center', padding: 12 }}>No devices found. Tap Scan to discover nearby Symphony Hardware.</Text>
            ) : (
              allDevices.map((d: any) => (
                <TouchableOpacity 
                   key={d.id} 
                   onPress={() => connectToDevice && connectToDevice(d)}
                   style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', paddingVertical: 12, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.1)' }}
                >
                  <View>
                    <Text style={{ color: Colors.text, fontWeight: 'bold', fontSize: 14 }}>{d.name || 'Unknown Symphony Device'}</Text>
                    <Text style={{ color: Colors.textMuted, fontSize: 11, fontFamily: Platform.OS === 'ios' ? 'Courier' : 'monospace', marginTop: 4 }}>MAC: {d.id}</Text>
                  </View>
                  <MaterialCommunityIcons name="bluetooth-connect" size={24} color={Colors.primary} />
                </TouchableOpacity>
              ))
            )}
          </View>
        )}
      </View>

      <View style={{ marginBottom: 20, width: '100%', height: 200 }}>
        <ProductVisualizer 
          product={device?.type || (product.toLowerCase().includes('soul') ? 'SOULZ' : 'HALOZ')} 
          color={currentPreviewColor} 
          mode="FIXED" 
          patternId={1} 
          isPaired={!!writeToDevice}
          points={hwPoints === null ? 43 : hwPoints}
          brightness={brightness}
          speed={speed}
          isPoweredOn={powerTesterState}
          statusText={`TEST 0X${currentPayload[0]?.toString(16).toUpperCase() || '00'} | SPEED: ${speed}`}
          rawHexPayload={currentPayload}
          audioMagnitude={0}
        />
      </View>

      {/* Target Hardware Profile */}
      <View style={{ backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 8, padding: 12, marginBottom: 20 }}>
        <Text style={[Typography.title, { color: Colors.primary, fontSize: 14, marginBottom: 8 }]}>Hardware Internal Specs</Text>
        <Text style={{ color: Colors.textMuted, fontSize: 12 }}>Name: {device?.name || 'Unknown'} | MAC: {device?.id || 'Unknown'}</Text>
        <Text style={{ color: Colors.textMuted, fontSize: 12 }}>Read Points: {device?.points || 'Default'} | Segments: {device?.segments || 'Default'}</Text>
        <Text style={{ color: Colors.textMuted, fontSize: 12 }}>Sorting: {device?.sorting || 'Default'} | Strip IC: {device?.stripType || 'Default'}</Text>
        
        <View style={{ marginTop: 16, backgroundColor: '#000', padding: 12, borderRadius: 8, borderWidth: 1, borderColor: '#333' }}>
          <Text style={[Typography.title, { color: '#FFF', fontSize: 14, marginBottom: 12 }]}>Re-Configure Device Logic</Text>
          
          <SettingWithExplanation title={`Total LED Points (${hwPoints === null ? '????' : hwPoints})`} description="The physical number of distinct LEDs on the strip. Determines the absolute end of the hardware transmission envelope. Sent via 0x81 payload." Colors={Colors}>
             <CustomSlider value={hwPoints === null ? 43 : hwPoints} minimumValue={1} maximumValue={600} onValueChange={setHwPoints} />
          </SettingWithExplanation>

          <SettingWithExplanation title={`Segments / Repeating Blocks (${hwSegments === null ? '????' : hwSegments})`} description="Splits the LED points into duplicated virtual segments. E.g., 40 points with 2 segments = two identical 20-LED mirroring blocks. Sent via 0x81 payload." Colors={Colors}>
            <CustomSlider value={hwSegments === null ? 1 : hwSegments} minimumValue={1} maximumValue={100} onValueChange={setHwSegments} />
          </SettingWithExplanation>
          
          <SettingWithExplanation title={`Color Ordering (${hwColorOrder === null ? '????' : hwColorOrder})`} description="The explicit RGB mapping translation expected by the hardware. Mismatching this array causes Red to visually trigger as Green, etc. Sent via 0x81 payload." Colors={Colors}>
            <ScrollView horizontal showsHorizontalScrollIndicator={false} style={{ flexDirection: 'row', marginBottom: 4 }}>
              {COLOR_ORDERS.map((order) => (
                <TouchableOpacity key={order} onPress={() => setHwColorOrder(order)} style={{ paddingHorizontal: 12, paddingVertical: 6, backgroundColor: hwColorOrder === order ? Colors.primary : '#333', borderRadius: 16, marginRight: 8 }}>
                  <Text style={{ color: hwColorOrder === order ? '#000' : '#FFF', fontWeight: 'bold' }}>{order}</Text>
                </TouchableOpacity>
              ))}
            </ScrollView>
          </SettingWithExplanation>

          <SettingWithExplanation title={`Controller IC Protocol (${hwStripType === null ? '????' : hwStripType})`} description="The standard physical communication chip protocol driving the internal LEDs. Handled by the Skate's motherboard. Mismatching this causes rapid flickering, data scrambling, or total failure. Sent via 0x81 payload." Colors={Colors}>
            <ScrollView horizontal showsHorizontalScrollIndicator={false} style={{ flexDirection: 'row', marginBottom: 4 }}>
              {STRIP_TYPES.map((ic) => (
                <TouchableOpacity key={ic} onPress={() => setHwStripType(ic)} style={{ paddingHorizontal: 12, paddingVertical: 6, backgroundColor: hwStripType === ic ? Colors.primary : '#333', borderRadius: 16, marginRight: 8 }}>
                  <Text style={{ color: hwStripType === ic ? '#000' : '#FFF', fontWeight: 'bold' }}>{ic}</Text>
                </TouchableOpacity>
              ))}
            </ScrollView>
          </SettingWithExplanation>

          <TouchableOpacity 
            onPress={() => {
              if (writeToDevice) {
                setIsQueryingHardware(true);
                writeToDevice(ZenggeProtocol.queryHardwareConfig());
              }
            }} 
            style={{ backgroundColor: '#00AEEF', padding: 12, borderRadius: 8, alignItems: 'center', marginBottom: 8 }}
          >
            <Text style={{ color: '#FFF', fontWeight: 'bold' }}>QUERY PHYSICAL HARDWARE SETTINGS (0x10)</Text>
          </TouchableOpacity>

          {lastRawNotification && lastRawNotification.deviceId === (device?.id) && (
             <View style={{ backgroundColor: 'rgba(0, 240, 255, 0.1)', padding: 8, borderRadius: 6, marginBottom: 12, borderWidth: 1, borderColor: 'rgba(0, 240, 255, 0.3)' }}>
               <Text style={{ color: '#00f0ff', fontSize: 10, fontWeight: 'bold', marginBottom: 4 }}>LATEST HARDWARE RESPONSE:</Text>
               <Text style={{ color: '#FFF', fontFamily: Platform.OS === 'ios' ? 'Courier' : 'monospace', fontSize: 13 }}>{lastRawNotification.payloadHex}</Text>
             </View>
          )}

          <TouchableOpacity onPress={pushHardwareConfig} style={{ backgroundColor: Colors.error, padding: 12, borderRadius: 8, alignItems: 'center' }}>
            <Text style={{ color: '#FFF', fontWeight: 'bold' }}>OVERWRITE HARDWARE CONFIG (0x81)</Text>
          </TouchableOpacity>
        </View>
      </View>

      {/* Raw Payload Visualizer & Editor */}
      <View style={{ backgroundColor: '#1E1E1E', borderRadius: 8, padding: 12, marginBottom: 20, borderWidth: 1, borderColor: isManualOverride ? Colors.error : Colors.primary }}>
        <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 }}>
          <Text style={[Typography.title, { color: isManualOverride ? Colors.error : Colors.primary, fontSize: 14 }]}>
            {isManualOverride ? 'MANUAL PAYLOAD OVERRIDE' : 'LIVE GENERATED PAYLOAD'}
          </Text>
          {isManualOverride && (
            <TouchableOpacity onPress={() => setIsManualOverride(false)}>
              <Text style={{ color: Colors.primary, fontSize: 12, fontWeight: 'bold' }}>RESET TO SLIDERS</Text>
            </TouchableOpacity>
          )}
        </View>
        
        {isManualOverride ? (
          <TextInput
            style={{
              color: '#FFFFFF',
              fontFamily: Platform.OS === 'ios' ? 'Courier' : 'monospace',
              fontSize: 14,
              minHeight: 80,
              textAlignVertical: 'top'
            }}
            multiline
            value={rawHexString}
            onChangeText={handleManualHexEdit}
            placeholder="00 FF 1A..."
            placeholderTextColor="#666"
          />
        ) : (
          <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 4 }}>
            {annotatedPayload.map((ann, i) => (
              <View key={i} style={{ alignItems: 'center', width: 28, marginBottom: 8 }}>
                <Text style={{ color: ann.color, fontFamily: Platform.OS === 'ios' ? 'Courier' : 'monospace', fontSize: 13, fontWeight: 'bold' }}>{ann.hex}</Text>
                <Text style={{ color: ann.color, fontSize: 8, marginTop: 2, opacity: 0.8, fontWeight: '600' }} numberOfLines={1} adjustsFontSizeToFit>{ann.label}</Text>
              </View>
            ))}
          </View>
        )}

      </View>

      <Text style={[Typography.title, isDark && { color: '#FFF' }, { marginBottom: 12 }]}>Protocol Selector</Text>
      <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 8, marginBottom: 20 }}>
        {['0x59', '0x51', '0x42', '0x73', '0x71', '0x2A', 'CANDLE', 'CAMERA'].map(p => (
          <TouchableOpacity 
            key={p} 
            onPress={() => setProtocol(p as any)}
            style={{ padding: 10, borderRadius: 8, alignItems: 'center', backgroundColor: protocol === p ? Colors.primary : Colors.surfaceHighlight, flexGrow: 1 }}
          >
            <Text style={{ color: protocol === p ? '#000' : Colors.textMuted, fontWeight: 'bold' }}>{p}</Text>
          </TouchableOpacity>
        ))}
      </View>

      <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 20 }}>
        <View style={{ width: 40, height: 40, borderRadius: 20, backgroundColor: currentPreviewColor, borderWidth: 2, borderColor: '#FFF', marginRight: 16 }} />
        <Text style={[Typography.title, { color: Colors.textMuted }]}>RGB: {r},{g},{b}</Text>
      </View>

      {/* R G B Settings */}
      <View style={{ gap: 16, marginBottom: 20 }}>
        <Text style={{ color: Colors.textMuted, fontSize: 12, marginBottom: -8 }}>Color 1 (Primary / Sound Column Color)</Text>
        <View>
          <Text style={{ color: '#FF4444', fontWeight: 'bold', marginBottom: 4 }}>RED ({r})</Text>
          <CustomSlider value={r} minimumValue={0} maximumValue={255} onValueChange={setR} />
        </View>
        <View>
          <Text style={{ color: '#44FF44', fontWeight: 'bold', marginBottom: 4 }}>GREEN ({g})</Text>
          <CustomSlider value={g} minimumValue={0} maximumValue={255} onValueChange={setG} />
        </View>
        <View>
          <Text style={{ color: '#4444FF', fontWeight: 'bold', marginBottom: 4 }}>BLUE ({b})</Text>
          <CustomSlider value={b} minimumValue={0} maximumValue={255} onValueChange={setB} />
        </View>
      </View>

      {protocol === '0x59' && (
        <View style={{ gap: 16, marginBottom: 20, backgroundColor: 'rgba(255,255,255,0.05)', padding: 12, borderRadius: 8 }}>
          <Text style={[Typography.title, { color: '#FFA500', fontSize: 16 }]}>0x59 Segment Parameters</Text>
          <SettingWithExplanation title={`Arr Length / Scale (${length})`} description={`The number of color data entries processed per animation frame. CRITICAL: Hardware testing confirms any length below 10 causes the first chunk of LEDs to glitch/flicker. Must be set >= 10 for absolute stability.`} Colors={Colors}>
            <CustomSlider value={length} minimumValue={1} maximumValue={points * 2} onValueChange={setLength} />
          </SettingWithExplanation>

          <SettingWithExplanation title={`Type Byte (${transitionType === 0 ? 'Static' : transitionType === 1 ? 'Gradual' : transitionType === 2 ? 'Strobe' : 'Jump'})`} description="CRITICAL: Hardware confirms that 0x59 strictly operates as a running marquee buffer. Types 0, 1, 2 are completely ignored by the IC. Only 3 (Jump/Flow) actually works natively on the Symphony controller." Colors={Colors}>
             <CustomSlider value={transitionType} minimumValue={0} maximumValue={3} onValueChange={setTransitionType} />
          </SettingWithExplanation>
          
          <SettingWithExplanation title={`Flow Direction (${segmentDirection === 0 ? 'Reverse/Back' : 'Forward/Right'})`} description="Determines if the array sweeps forward from LED 0 or backwards from the end of the strip upon rendering." Colors={Colors}>
             <CustomSlider value={segmentDirection} minimumValue={0} maximumValue={1} onValueChange={setSegmentDirection} />
          </SettingWithExplanation>
        </View>
      )}

      {protocol === '0x51' && (
        <View style={{ gap: 16, marginBottom: 20, backgroundColor: 'rgba(255,255,255,0.05)', padding: 12, borderRadius: 8 }}>
          <Text style={[Typography.title, { color: '#00F0FF', fontSize: 16 }]}>0x51 Custom Parameters</Text>
          <SettingWithExplanation title={`Effect Type (${transitionType === 1 ? 'Gradual' : transitionType === 2 ? 'Jump' : 'Strobe'})`} description="0x51 supports explicitly injecting transition nodes into the skate's memory bank alongside up to 16 colors." Colors={Colors}>
             <CustomSlider value={transitionType} minimumValue={1} maximumValue={3} onValueChange={setTransitionType} />
          </SettingWithExplanation>
        </View>
      )}

      {protocol === '0x42' && (
        <View style={{ gap: 16, marginBottom: 20, backgroundColor: 'rgba(255,255,255,0.05)', padding: 12, borderRadius: 8 }}>
          <Text style={[Typography.title, { color: '#00FF44', fontSize: 16 }]}>0x42 Fixed ROM Pattern</Text>
          <SettingWithExplanation title={`RBM Pattern ID (${rbmPattern})`} description="Triggers a hardcoded light pattern baked directly into the Symphony controller's physical ROM footprint. Note: Addressing patterns 101-210 accesses extended Halloween/Christmas looping effects undocumented in standard guides." Colors={Colors}>
             <CustomSlider value={rbmPattern} minimumValue={1} maximumValue={210} onValueChange={setRbmPattern} />
          </SettingWithExplanation>
        </View>
      )}

      {protocol === 'CANDLE' && (
        <View style={{ gap: 16, marginBottom: 20, backgroundColor: 'rgba(255,255,255,0.05)', padding: 12, borderRadius: 8 }}>
          <Text style={[Typography.title, { color: '#FFA500', fontSize: 16 }]}>Candle Flicker Simulation</Text>
          <SettingWithExplanation title={`Flicker Effect (${candleAmplitude})`} description="Forces a random flickering array physically inside the hardware using an undocumented algorithm to simulate deep candles." Colors={Colors}>
             <CustomSlider value={candleAmplitude} minimumValue={1} maximumValue={3} onValueChange={setCandleAmplitude} />
          </SettingWithExplanation>
        </View>
      )}

      {protocol === 'MULTI' && (
        <View style={{ gap: 16, marginBottom: 20, backgroundColor: 'rgba(255,255,255,0.05)', padding: 12, borderRadius: 8 }}>
          <Text style={[Typography.title, { color: '#FF00FF', fontSize: 16 }]}>Multi-Color DIY Engine (0x59)</Text>
          <SettingWithExplanation title={`List of Colors (${multiColors.length})`} description="The discrete colored array sequence mapped structurally to the Segment configuration. Tap blocks to overwrite." Colors={Colors}>
            <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 8, paddingBottom: 8 }}>
              {multiColors.map((hex, index) => (
                <TouchableOpacity key={index} style={{ width: 44, height: 44, borderRadius: 22, backgroundColor: hex, borderWidth: 2, borderColor: '#FFF', shadowColor: hex, shadowOpacity: 0.8, shadowRadius: 4 }} onPress={() => {
                  const newArr = [...multiColors];
                  newArr[index] = `#${r.toString(16).padStart(2,'0')}${g.toString(16).padStart(2,'0')}${b.toString(16).padStart(2,'0')}`.toUpperCase();
                  setMultiColors(newArr);
                }} />
              ))}
              {multiColors.length < 16 && (
                <TouchableOpacity style={{ width: 44, height: 44, borderRadius: 22, backgroundColor: 'rgba(255,255,255,0.1)', borderWidth: 1, borderColor: '#FFF', justifyContent: 'center', alignItems: 'center' }} onPress={() => {
                   setMultiColors([...multiColors, `#${r.toString(16).padStart(2,'0')}${g.toString(16).padStart(2,'0')}${b.toString(16).padStart(2,'0')}`.toUpperCase()]);
                }}>
                  <Text style={{ color: '#FFF', fontSize: 24, fontWeight: 'bold' }}>+</Text>
                </TouchableOpacity>
              )}
              {multiColors.length > 1 && (
                <TouchableOpacity style={{ width: 44, height: 44, borderRadius: 22, backgroundColor: 'rgba(255,0,0,0.5)', borderWidth: 1, borderColor: '#FFF', justifyContent: 'center', alignItems: 'center' }} onPress={() => {
                   const newArr = [...multiColors];
                   newArr.pop();
                   setMultiColors(newArr);
                }}>
                  <Text style={{ color: '#FFF', fontSize: 24, fontWeight: 'bold', lineHeight: 26 }}>-</Text>
                </TouchableOpacity>
              )}
            </View>
          </SettingWithExplanation>
          
          <SettingWithExplanation title={`Segment Size (${length})`} description={`The computational size scaling the recurring colored pattern globally. Minimum 10 pixels required.`} Colors={Colors}>
            <CustomSlider value={length} minimumValue={10} maximumValue={points * 2} onValueChange={setLength} />
          </SettingWithExplanation>

          <SettingWithExplanation title={`Zengge Transition (${multiTransition === 0 ? 'Static' : multiTransition === 1 ? 'Gradual' : multiTransition === 2 ? 'Strobe' : 'Running Water'})`} description="Strict translation of the 4 native Zengge animation engines bounded seamlessly inside the 0x59 byte wrapper." Colors={Colors}>
             <CustomSlider value={multiTransition} minimumValue={0} maximumValue={3} onValueChange={setMultiTransition} />
          </SettingWithExplanation>
        </View>
      )}

      {protocol === '0x2A' && (
        <View style={{ gap: 16, marginBottom: 20, backgroundColor: 'rgba(255,255,255,0.05)', padding: 12, borderRadius: 8 }}>
          <Text style={[Typography.title, { color: '#00FF44', fontSize: 16 }]}>0x2A Physical RF Remote Boundaries</Text>
          <SettingWithExplanation title="RF Authorization Gateway" description="Sets the native bounding box for physical 2.4G remotes linking to this unit. Modifying this explicitly locks out or grants access to external handheld controllers natively." Colors={Colors}>
             <ScrollView horizontal showsHorizontalScrollIndicator={false} style={{ flexDirection: 'row', marginBottom: 4 }}>
               {[
                 { label: 'Allow All', val: 'ALLOW_ALL' },
                 { label: 'Allow Only Paired', val: 'ALLOW_PAIRED' },
                 { label: 'Allow None', val: 'ALLOW_NONE' }
               ].map((mode) => (
                 <TouchableOpacity 
                   key={mode.val} 
                   onPress={() => setRfAuthMode(mode.val as any)} 
                   style={{ 
                     paddingHorizontal: 12, paddingVertical: 8, 
                     backgroundColor: rfAuthMode === mode.val ? Colors.primary : 'rgba(255,255,255,0.1)', 
                     borderRadius: 8, marginRight: 8 
                   }}
                 >
                   <Text style={{ color: rfAuthMode === mode.val ? '#000' : '#FFF', fontWeight: 'bold' }}>{mode.label}</Text>
                 </TouchableOpacity>
               ))}
             </ScrollView>
          </SettingWithExplanation>
        </View>
      )}

      {protocol === '0x71' && (
        <View style={{ gap: 16, marginBottom: 20, backgroundColor: 'rgba(255,255,255,0.05)', padding: 12, borderRadius: 8 }}>
          <Text style={[Typography.title, { color: Colors.primary, fontSize: 16 }]}>0x71 Power Override</Text>
          <SettingWithExplanation title="Power ON / OFF" description="Directly transmits brute force system Power ON / OFF vectors to the native motherboard receiver." Colors={Colors}>
             <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}>
                <Text style={{ color: Colors.textMuted }}>Command Power State</Text>
                <Switch 
                  value={powerTesterState}
                  onValueChange={setPowerTesterState}
                  trackColor={{ false: '#3B4A5A', true: Colors.primary }}
                  thumbColor="#FFF"
                />
             </View>
          </SettingWithExplanation>
        </View>
      )}

      {protocol === '0x73' && (
        <View style={{ gap: 16, marginBottom: 20, backgroundColor: 'rgba(255,255,255,0.05)', padding: 12, borderRadius: 8 }}>
          <Text style={[Typography.title, { color: '#FFD700', fontSize: 16 }]}>0x73 Music Symphony Parameters</Text>
          <SettingWithExplanation title={`Mic Source (${isDeviceMic === 1 ? 'Native Hardware Controller Mic' : 'App Software Microphone'})`} description="0x01 forces the physical chip mic to activate. 0x00 silences hardware and relies entirely on BLE streamed magnitudes." Colors={Colors}>
             <CustomSlider value={isDeviceMic} minimumValue={0} maximumValue={1} onValueChange={setIsDeviceMic} />
          </SettingWithExplanation>
          <SettingWithExplanation title={`Hardware Matrix Style (${musicModeType === 38 ? '0x26 Light Bar' : '0x27 Light Screen'})`} description="Internal mathematical configuration differentiating horizontal scaling vs panel clustering." Colors={Colors}>
             <CustomSlider value={musicModeType} minimumValue={38} maximumValue={39} onValueChange={setMusicModeType} />
          </SettingWithExplanation>
          <SettingWithExplanation title={`Symphony Pattern Definition (${musicPatternId})`} description="Physical music patterns burnt into controller ROM." Colors={Colors}>
             <CustomSlider value={musicPatternId} minimumValue={1} maximumValue={16} onValueChange={setMusicPatternId} />
          </SettingWithExplanation>
          <SettingWithExplanation title={`Mic Sensitivity Gain (${musicSensitivity}%)`} description="The floor amplification scaling factor utilized by the active microphone." Colors={Colors}>
             <CustomSlider value={musicSensitivity} minimumValue={1} maximumValue={100} onValueChange={setMusicSensitivity} />
          </SettingWithExplanation>
          <View>
             <Text style={{ color: '#FFF', fontWeight: 'bold', marginTop: 8, marginBottom: 8, alignSelf: 'center' }}>Drop Color (Secondary Variables)</Text>
             <View style={{ flexDirection: 'row', gap: 16 }}>
               <View style={{ flex: 1 }}><Text style={{ color: '#FF4444', fontWeight: 'bold' }}>R2</Text><CustomSlider value={r2} minimumValue={0} maximumValue={255} onValueChange={setR2} /></View>
               <View style={{ flex: 1 }}><Text style={{ color: '#44FF44', fontWeight: 'bold' }}>G2</Text><CustomSlider value={g2} minimumValue={0} maximumValue={255} onValueChange={setG2} /></View>
               <View style={{ flex: 1 }}><Text style={{ color: '#4444FF', fontWeight: 'bold' }}>B2</Text><CustomSlider value={b2} minimumValue={0} maximumValue={255} onValueChange={setB2} /></View>
             </View>
          </View>
        </View>
      )}

      {protocol === 'CAMERA' && (
        <View style={{ gap: 16, marginBottom: 20, backgroundColor: 'rgba(255,255,255,0.05)', padding: 12, borderRadius: 8 }}>
          <Text style={[Typography.title, { color: '#FFF', fontSize: 16 }]}>Optical Camera Telemetry</Text>
          <Text style={{ color: Colors.textMuted, fontSize: 12, marginBottom: 8 }}>
            Tap anywhere on the camera feed to extract the average hex color of that chunk. The payload will instantly auto-transmit to hardware over Bluetooth.
          </Text>
          <View style={{ height: 350, borderRadius: 12, overflow: 'hidden', borderWidth: 1, borderColor: Colors.primary }}>
            <CameraTracker isActive={true} onColorDetected={handleCameraColor} />
          </View>
        </View>
      )}

      {/* Global modifiers */}
      <View style={{ gap: 8, marginBottom: 40 }}>
        <SettingWithExplanation title={`Sync Speed / Interval (${speed})`} description="The native animation interval. The hardware actively ignores some protocol arrays if speed is strictly 0." Colors={Colors}>
          <CustomSlider value={speed} minimumValue={0} maximumValue={100} onValueChange={setSpeed} />
        </SettingWithExplanation>
        
        <SettingWithExplanation title={`Data Matrix Brightness Mod (${brightness}%)`} description="A software-level modifier that mathematically dims the RGB hex boundaries (lowering voltage amplitudes) BEFORE generating the payload array. Required to prevent absolute current clipping." Colors={Colors}>
          <CustomSlider value={brightness} minimumValue={0} maximumValue={100} onValueChange={setBrightness} />
        </SettingWithExplanation>
      </View>

      </ScrollView>

      {/* Floating Transmit Button */}
      <View style={{ position: 'absolute', bottom: 20, left: Layout.padding, right: Layout.padding }}>
        <TouchableOpacity 
          style={{ backgroundColor: Colors.primary, padding: 16, borderRadius: 12, alignItems: 'center', shadowColor: '#000', shadowOffset: { width: 0, height: 4 }, shadowOpacity: 0.5, shadowRadius: 8, elevation: 10 }}
          onPress={handleSend}
          activeOpacity={0.8}
        >
          <Text style={{ color: '#000', fontWeight: '900', fontSize: 16 }}>TRANSMIT PAYLOAD</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}
