import React, { useState, useEffect, useCallback, useRef } from 'react';
import {
  View, Text, StyleSheet, Modal, SafeAreaView, TouchableOpacity,
  ScrollView, TextInput, ActivityIndicator, Animated, Alert, Platform,
} from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useTheme } from '../context/ThemeContext';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Typography } from '../theme/theme';
import {
  ZenggeProtocol,
  HardwareSettings,
  IC_TYPES,
  COLOR_SORTING_RGB,
  SK8_DEFAULTS,
  HW_CONSTRAINTS,
  icTypeIndex,
  colorSortingIndex,
} from '../protocols/ZenggeProtocol';

// ─── Types ─────────────────────────────────────────────────────────────────────

interface ScannedDevice {
  id: string;
  name: string;
  rssi: number;
  manufacturerData?: string;
  firmwareVer?: number;
  ledVersion?: number;
  bleVersion?: number;
  hardwareSettings?: HardwareSettings;
  queryState: 'idle' | 'querying' | 'done' | 'error';
}

interface Sk8LytzProgrammerModalProps {
  visible: boolean;
  onClose: () => void;
  onExitToLogs?: () => void;
  allDevices: any[];
  writeToDevice: (data: number[], deviceId?: string) => Promise<void>;
  isScanning: boolean;
  handleScan: () => void;
  setOnDataReceived?: (cb: (deviceId: string, data: number[]) => void) => void;
}

// ─── Constants ────────────────────────────────────────────────────────────────

const IC_LIST = Object.entries(IC_TYPES).map(([k, v]) => ({ index: Number(k), name: v }));
const SORTING_LIST = Object.entries(COLOR_SORTING_RGB).map(([k, v]) => ({ index: Number(k), name: v }));
const CACHE_PREFIX = '@sk8_hw_';

// ─── Component ────────────────────────────────────────────────────────────────

export default function Sk8LytzProgrammerModal({
  visible, onClose, onExitToLogs, allDevices,
  writeToDevice, isScanning, handleScan, setOnDataReceived,
}: Sk8LytzProgrammerModalProps) {
  const { Colors, isDark } = useTheme();

  // Colour aliases
  const bg       = isDark ? '#0a0d18' : '#f0f2f5';
  const cardBg   = isDark ? '#141829' : '#ffffff';
  const card2    = isDark ? '#1c2138' : '#f8faff';
  const txtPri   = isDark ? '#ffffff' : '#111827';
  const txtMuted = isDark ? '#8a96b3' : '#6b7280';
  const border   = isDark ? '#252c47' : '#e5e7eb';
  const cyan     = '#00f0ff';
  const orange   = Colors.primary;
  const amber    = Colors.secondary;
  const green    = '#00e887';

  // ─── State ──────────────────────────────────────────────────────────────────
  const [scannedDevices, setScannedDevices] = useState<ScannedDevice[]>([]);
  const [selectedDevice, setSelectedDevice] = useState<ScannedDevice | null>(null);
  const [editSettings, setEditSettings] = useState<HardwareSettings | null>(null);
  const [saveState, setSaveState] = useState<'idle' | 'saving' | 'saved'>('idle');
  const [pendingResponses, setPendingResponses] = useState<Record<string, number[]>>({});
  const pendingRef = useRef<Record<string, (data: number[]) => void>>({});

  // ─── Sync allDevices → scannedDevices ───────────────────────────────────────
  useEffect(() => {
    if (!visible) return;
    const updated: ScannedDevice[] = allDevices.map(d => {
      const existing = scannedDevices.find(s => s.id === d.id);
      if (existing) return existing;

      // Parse firmware from advertisement during scan
      let fwInfo: { firmwareVer: number; ledVersion: number; bleVersion: number } | null = null;
      if (d.manufacturerData) {
        fwInfo = ZenggeProtocol.parseFirmwareFromAdvertisement(d.manufacturerData);
      }

      return {
        id: d.id,
        name: d.name || d.id.slice(-8),
        rssi: d.rssi || -99,
        manufacturerData: d.manufacturerData,
        firmwareVer: fwInfo?.firmwareVer,
        ledVersion: fwInfo?.ledVersion,
        bleVersion: fwInfo?.bleVersion,
        queryState: 'idle' as const,
      };
    });
    setScannedDevices(updated);
  }, [allDevices, visible]);

  // ─── BLE Response listener ──────────────────────────────────────────────────
  useEffect(() => {
    if (!setOnDataReceived) return;
    setOnDataReceived((deviceId: string, data: number[]) => {
      // Try parse as 0x63 hardware settings response
      const parsed = ZenggeProtocol.parseHardwareSettingsResponse(data);
      if (parsed) {
        setScannedDevices(prev => prev.map(d => {
          if (d.id !== deviceId) return d;
          return { ...d, hardwareSettings: parsed, queryState: 'done' };
        }));
        // If this device is currently selected, refresh editSettings
        setSelectedDevice(prev => {
          if (!prev || prev.id !== deviceId) return prev;
          const updated = { ...prev, hardwareSettings: parsed, queryState: 'done' as const };
          setEditSettings({ ...parsed });
          return updated;
        });
        // Cache to AsyncStorage
        AsyncStorage.setItem(`${CACHE_PREFIX}${deviceId}`, JSON.stringify(parsed)).catch(() => {});
      }
      // Resolve any pending callbacks
      if (pendingRef.current[deviceId]) {
        pendingRef.current[deviceId](data);
        delete pendingRef.current[deviceId];
      }
    });
  }, [setOnDataReceived]);

  // ─── Load cached settings on open ───────────────────────────────────────────
  useEffect(() => {
    if (!visible) return;
    scannedDevices.forEach(async d => {
      if (d.hardwareSettings) return;
      try {
        const cached = await AsyncStorage.getItem(`${CACHE_PREFIX}${d.id}`);
        if (cached) {
          const hw: HardwareSettings = JSON.parse(cached);
          setScannedDevices(prev => prev.map(s =>
            s.id === d.id ? { ...s, hardwareSettings: { ...hw, detected: false }, queryState: 'done' } : s
          ));
        }
      } catch {}
    });
  }, [scannedDevices.length, visible]);

  // ─── Query device hardware settings ─────────────────────────────────────────
  const queryDevice = useCallback(async (device: ScannedDevice) => {
    setScannedDevices(prev => prev.map(d =>
      d.id === device.id ? { ...d, queryState: 'querying' } : d
    ));

    try {
      await writeToDevice(ZenggeProtocol.queryHardwareSettings(false), device.id);

      // Wait up to 5 s for response (pendingRef callback set by effect above)
      await new Promise<void>((resolve, reject) => {
        const timer = setTimeout(() => {
          delete pendingRef.current[device.id];
          reject(new Error('timeout'));
        }, 5000);
        pendingRef.current[device.id] = () => {
          clearTimeout(timer);
          resolve();
        };
      });
    } catch (e: any) {
      const isTimeout = e?.message === 'timeout';
      setScannedDevices(prev => prev.map(d =>
        d.id === device.id ? {
          ...d,
          queryState: isTimeout ? 'error' : 'done',
          hardwareSettings: d.hardwareSettings, // keep cached if any
        } : d
      ));
    }
  }, [writeToDevice]);

  // ─── Query all visible devices ───────────────────────────────────────────────
  const queryAll = async () => {
    for (const d of scannedDevices) {
      await queryDevice(d);
    }
  };

  // ─── Open detail panel ───────────────────────────────────────────────────────
  const openDevice = (device: ScannedDevice) => {
    setSelectedDevice(device);
    if (device.hardwareSettings) {
      setEditSettings({ ...device.hardwareSettings });
    } else {
      // Default based on name heuristic
      const isHaloz = device.name?.toUpperCase().includes('HALO');
      const def = isHaloz ? SK8_DEFAULTS.HALOZ : SK8_DEFAULTS.SOULZ;
      setEditSettings({
        ledPoints: def.points,
        segments: def.segments,
        icType: def.icType,
        icName: def.icName,
        colorSorting: def.sorting,
        colorSortingName: def.sortingName,
        detected: false,
      });
    }
    setSaveState('idle');
  };

  const closeDetail = () => {
    setSelectedDevice(null);
    setEditSettings(null);
    setSaveState('idle');
  };

  // ─── Save settings ───────────────────────────────────────────────────────────
  const saveSettings = async () => {
    if (!selectedDevice || !editSettings) return;
    setSaveState('saving');
    try {
      const cmd = ZenggeProtocol.writeHardwareSettings(
        editSettings.ledPoints,
        editSettings.segments,
        editSettings.icType,
        editSettings.colorSorting
      );
      await writeToDevice(cmd, selectedDevice.id);

      // Persist
      await AsyncStorage.setItem(`${CACHE_PREFIX}${selectedDevice.id}`, JSON.stringify(editSettings));

      // Update local state
      setScannedDevices(prev => prev.map(d =>
        d.id === selectedDevice.id ? { ...d, hardwareSettings: editSettings } : d
      ));
      setSelectedDevice(prev => prev ? { ...prev, hardwareSettings: editSettings } : prev);

      setSaveState('saved');
      setTimeout(() => setSaveState('idle'), 2000);
    } catch (e) {
      setSaveState('idle');
      Alert.alert('Write Failed', 'Could not save to device. Make sure it is connected.');
    }
  };

  // ─── RSSI bar ───────────────────────────────────────────────────────────────
  const rssiLevel = (rssi: number) => {
    if (rssi > -60) return { bars: 4, color: green };
    if (rssi > -70) return { bars: 3, color: amber };
    if (rssi > -80) return { bars: 2, color: orange };
    return { bars: 1, color: '#ff3d71' };
  };

  // ─── Render ─────────────────────────────────────────────────────────────────
  return (
    <Modal visible={visible} animationType="slide" presentationStyle="fullScreen" onRequestClose={onClose}>
      <SafeAreaView style={[s.root, { backgroundColor: bg }]}>

        {/* ── Header ── */}
        <View style={[s.topBar, { borderBottomColor: border }]}>
          <View>
            <Text style={[Typography.title, { color: cyan, fontSize: 18 }]}>⚡ HARDWARE TESTER</Text>
            <Text style={{ color: txtMuted, fontSize: 11, marginTop: 2 }}>
              {scannedDevices.length} device{scannedDevices.length !== 1 ? 's' : ''} found
            </Text>
          </View>
          <TouchableOpacity
            style={[s.exitBtn, { borderColor: 'rgba(255,60,60,0.4)', backgroundColor: 'rgba(255,60,60,0.1)' }]}
            onPress={() => { if (onExitToLogs) onExitToLogs(); else onClose(); }}
          >
            <Text style={{ color: '#FF8888', fontSize: 10, fontWeight: '900', letterSpacing: 1 }}>EXIT</Text>
          </TouchableOpacity>
        </View>

        <ScrollView style={{ flex: 1 }} contentContainerStyle={{ padding: 16 }}>

          {/* ── Scan + Query All controls ── */}
          <View style={[s.card, { backgroundColor: cardBg, borderColor: border, marginBottom: 16 }]}>
            <View style={s.row}>
              <TouchableOpacity
                style={[s.actionBtn, { flex: 1, borderColor: cyan, backgroundColor: 'rgba(0,240,255,0.08)' }]}
                onPress={handleScan}
                disabled={isScanning}
              >
                {isScanning
                  ? <ActivityIndicator color={cyan} size="small" />
                  : <MaterialCommunityIcons name="bluetooth-search" size={16} color={cyan} />
                }
                <Text style={[s.actionBtnText, { color: cyan }]}>
                  {isScanning ? 'SCANNING...' : 'SCAN DEVICES'}
                </Text>
              </TouchableOpacity>

              <View style={{ width: 12 }} />

              <TouchableOpacity
                style={[s.actionBtn, { flex: 1, borderColor: amber, backgroundColor: 'rgba(255,184,0,0.08)' }]}
                onPress={queryAll}
                disabled={scannedDevices.length === 0}
              >
                <MaterialCommunityIcons name="download-network" size={16} color={amber} />
                <Text style={[s.actionBtnText, { color: amber }]}>QUERY ALL</Text>
              </TouchableOpacity>
            </View>
          </View>

          {/* ── Device List ── */}
          {scannedDevices.length === 0 ? (
            <View style={{ alignItems: 'center', paddingVertical: 48 }}>
              <MaterialCommunityIcons name="bluetooth-off" size={48} color={border} />
              <Text style={{ color: txtMuted, marginTop: 12, fontSize: 14 }}>
                No SK8Lytz hardware detected.
              </Text>
              <Text style={{ color: txtMuted, fontSize: 12, marginTop: 4 }}>
                Press SCAN DEVICES to search.
              </Text>
            </View>
          ) : (
            scannedDevices.map(device => {
              const sig = rssiLevel(device.rssi);
              const hw = device.hardwareSettings;
              return (
                <TouchableOpacity
                  key={device.id}
                  style={[s.deviceCard, { backgroundColor: cardBg, borderColor: border }]}
                  onPress={() => openDevice(device)}
                  activeOpacity={0.75}
                >
                  {/* Left: icon + name */}
                  <View style={{ flex: 1 }}>
                    <View style={s.row}>
                      <View style={[s.statusDot, {
                        backgroundColor: hw?.detected ? green : (hw ? amber : border)
                      }]} />
                      <Text style={{ color: txtPri, fontWeight: '800', fontSize: 14 }}>{device.name}</Text>
                    </View>
                    <Text style={{ color: txtMuted, fontSize: 11, marginTop: 3, fontFamily: 'monospace' }}>
                      {device.id}
                    </Text>

                    {/* Hardware info row */}
                    {hw ? (
                      <View style={[s.row, { marginTop: 8, flexWrap: 'wrap', gap: 6 }]}>
                        <View style={[s.badge, { borderColor: cyan + '60', backgroundColor: 'rgba(0,240,255,0.07)' }]}>
                          <Text style={[s.badgeText, { color: cyan }]}>{hw.icName}</Text>
                        </View>
                        <View style={[s.badge, { borderColor: orange + '60', backgroundColor: 'rgba(255,90,0,0.07)' }]}>
                          <Text style={[s.badgeText, { color: orange }]}>{hw.colorSortingName}</Text>
                        </View>
                        <View style={[s.badge, { borderColor: amber + '60', backgroundColor: 'rgba(255,184,0,0.07)' }]}>
                          <Text style={[s.badgeText, { color: amber }]}>{hw.ledPoints} pts</Text>
                        </View>
                        {device.firmwareVer !== undefined && (
                          <View style={[s.badge, { borderColor: border }]}>
                            <Text style={[s.badgeText, { color: txtMuted }]}>FW {device.firmwareVer}.{device.ledVersion}</Text>
                          </View>
                        )}
                        {hw.detected && (
                          <View style={[s.badge, { borderColor: green + '60', backgroundColor: 'rgba(0,232,135,0.07)' }]}>
                            <Text style={[s.badgeText, { color: green }]}>✓ LIVE</Text>
                          </View>
                        )}
                      </View>
                    ) : (
                      <Text style={{ color: txtMuted, fontSize: 11, marginTop: 6 }}>
                        {device.queryState === 'querying' ? '⏳ Querying hardware...' :
                         device.queryState === 'error'    ? '⚠ No response — tap to retry' :
                         'Tap to connect & query settings'}
                      </Text>
                    )}
                  </View>

                  {/* Right: RSSI + query btn + chevron */}
                  <View style={{ alignItems: 'flex-end', gap: 8 }}>
                    {/* RSSI bars */}
                    <View style={s.rssiBars}>
                      {[1, 2, 3, 4].map(i => (
                        <View key={i} style={[s.rssiBar, {
                          height: 4 + i * 4,
                          backgroundColor: i <= sig.bars ? sig.color : border,
                        }]} />
                      ))}
                    </View>
                    <Text style={{ color: txtMuted, fontSize: 10 }}>{device.rssi} dBm</Text>

                    {device.queryState === 'querying'
                      ? <ActivityIndicator color={cyan} size="small" />
                      : <TouchableOpacity
                          onPress={() => queryDevice(device)}
                          style={[s.qryBtn, { borderColor: cyan + '80' }]}
                        >
                          <Text style={{ color: cyan, fontSize: 10, fontWeight: '800' }}>QUERY</Text>
                        </TouchableOpacity>
                    }

                    <MaterialCommunityIcons name="chevron-right" size={18} color={txtMuted} />
                  </View>
                </TouchableOpacity>
              );
            })
          )}

          <View style={{ height: 40 }} />
        </ScrollView>
      </SafeAreaView>

      {/* ════════════════════════════════════════════════════════════════════════
          DEVICE DETAIL PANEL (full-screen slide-up modal)
          ════════════════════════════════════════════════════════════════════════ */}
      <Modal
        visible={!!selectedDevice}
        animationType="slide"
        presentationStyle="fullScreen"
        onRequestClose={closeDetail}
      >
        <SafeAreaView style={[s.root, { backgroundColor: bg }]}>
          {/* Detail header */}
          <View style={[s.topBar, { borderBottomColor: border }]}>
            <TouchableOpacity onPress={closeDetail} style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
              <MaterialCommunityIcons name="arrow-left" size={20} color={cyan} />
              <Text style={{ color: cyan, fontWeight: '700' }}>Back</Text>
            </TouchableOpacity>
            <Text style={{ color: txtPri, fontWeight: '800', fontSize: 15 }}>{selectedDevice?.name}</Text>
            <TouchableOpacity
              onPress={() => selectedDevice && queryDevice(selectedDevice)}
              style={[s.qryBtn, { borderColor: amber + '80' }]}
            >
              {selectedDevice?.queryState === 'querying'
                ? <ActivityIndicator color={amber} size="small" style={{ width: 32 }} />
                : <Text style={{ color: amber, fontSize: 10, fontWeight: '800' }}>RE-QUERY</Text>
              }
            </TouchableOpacity>
          </View>

          {editSettings ? (
            <ScrollView style={{ flex: 1 }} contentContainerStyle={{ padding: 16 }}>

              {/* Device Identity card */}
              <View style={[s.card, { backgroundColor: cardBg, borderColor: border, marginBottom: 16 }]}>
                <Text style={[s.sectionLabel, { color: txtMuted }]}>DEVICE IDENTITY</Text>
                <View style={[s.row, { marginTop: 10, gap: 12 }]}>
                  <View style={{ flex: 1 }}>
                    <Text style={[s.infoLabel, { color: txtMuted }]}>MAC / ID</Text>
                    <Text style={{ color: txtPri, fontSize: 12, fontFamily: 'monospace' }} numberOfLines={1}>
                      {selectedDevice?.id}
                    </Text>
                  </View>
                  <View>
                    <Text style={[s.infoLabel, { color: txtMuted }]}>FIRMWARE</Text>
                    <Text style={{ color: editSettings.detected ? green : txtMuted, fontSize: 13, fontWeight: '700' }}>
                      {selectedDevice?.firmwareVer !== undefined
                        ? `v${selectedDevice.firmwareVer}.${selectedDevice.ledVersion}`
                        : 'Unknown'}
                    </Text>
                  </View>
                  <View>
                    <Text style={[s.infoLabel, { color: txtMuted }]}>BLE VER</Text>
                    <Text style={{ color: txtMuted, fontSize: 13 }}>
                      {selectedDevice?.bleVersion ?? '—'}
                    </Text>
                  </View>
                </View>
                {editSettings.detected && (
                  <View style={[s.liveTag, { marginTop: 12 }]}>
                    <Text style={{ color: green, fontSize: 11, fontWeight: '800' }}>✓ LIVE HARDWARE DATA — Settings queried from physical device</Text>
                  </View>
                )}
              </View>

              {/* LED Points + Segments */}
              <View style={[s.card, { backgroundColor: cardBg, borderColor: border, marginBottom: 16 }]}>
                <Text style={[s.sectionLabel, { color: txtMuted }]}>LED CONFIGURATION</Text>

                <View style={[s.row, { marginTop: 14, gap: 12 }]}>
                  {/* Points */}
                  <View style={{ flex: 1 }}>
                    <Text style={[s.infoLabel, { color: txtMuted }]}>LED POINTS</Text>
                    <TextInput
                      style={[s.numInput, { backgroundColor: card2, borderColor: border, color: txtPri }]}
                      value={String(editSettings.ledPoints)}
                      onChangeText={t => {
                        const n = parseInt(t.replace(/[^0-9]/g, '')) || 1;
                        const safe = Math.min(n, HW_CONSTRAINTS.maxPoints);
                        setEditSettings(prev => prev ? { ...prev, ledPoints: safe } : prev);
                      }}
                      keyboardType="number-pad"
                      maxLength={3}
                    />
                    <Text style={{ color: txtMuted, fontSize: 10, marginTop: 4 }}>Max {HW_CONSTRAINTS.maxPoints}</Text>
                  </View>

                  {/* Segments */}
                  <View style={{ flex: 1 }}>
                    <Text style={[s.infoLabel, { color: txtMuted }]}>SEGMENTS</Text>
                    <TextInput
                      style={[s.numInput, { backgroundColor: card2, borderColor: border, color: txtPri }]}
                      value={String(editSettings.segments)}
                      onChangeText={t => {
                        const n = parseInt(t.replace(/[^0-9]/g, '')) || 1;
                        const maxS = Math.floor(HW_CONSTRAINTS.maxPxS / Math.max(1, editSettings.ledPoints));
                        const safe = Math.min(n, maxS);
                        setEditSettings(prev => prev ? { ...prev, segments: safe } : prev);
                      }}
                      keyboardType="number-pad"
                      maxLength={4}
                    />
                    <Text style={{ color: txtMuted, fontSize: 10, marginTop: 4 }}>Max {Math.floor(HW_CONSTRAINTS.maxPxS / Math.max(1, editSettings.ledPoints))}</Text>
                  </View>

                  {/* Total */}
                  <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
                    <Text style={[s.infoLabel, { color: txtMuted }]}>TOTAL LEDs</Text>
                    <Text style={{
                      color: editSettings.ledPoints * editSettings.segments > HW_CONSTRAINTS.maxPxS ? '#ff3d71' : cyan,
                      fontSize: 24, fontWeight: '900',
                    }}>
                      {editSettings.ledPoints * editSettings.segments}
                    </Text>
                    <Text style={{ color: txtMuted, fontSize: 9 }}>/ {HW_CONSTRAINTS.maxPxS} max</Text>
                  </View>
                </View>
              </View>

              {/* IC Strip Type */}
              <View style={[s.card, { backgroundColor: cardBg, borderColor: border, marginBottom: 16 }]}>
                <Text style={[s.sectionLabel, { color: txtMuted }]}>IC STRIP TYPE</Text>
                <ScrollView horizontal showsHorizontalScrollIndicator={false} style={{ marginTop: 12 }}>
                  {IC_LIST.map(({ index, name }) => {
                    const active = editSettings.icType === index;
                    return (
                      <TouchableOpacity
                        key={index}
                        style={[s.chip, {
                          borderColor: active ? cyan : border,
                          backgroundColor: active ? 'rgba(0,240,255,0.12)' : 'transparent',
                          marginRight: 8,
                        }]}
                        onPress={() => setEditSettings(prev => prev ? {
                          ...prev, icType: index, icName: name
                        } : prev)}
                      >
                        <Text style={[s.chipText, { color: active ? cyan : txtMuted, fontWeight: active ? '900' : '600' }]}>
                          {name}
                        </Text>
                        {(index === SK8_DEFAULTS.HALOZ.icType) && (
                          <Text style={{ color: cyan, fontSize: 8, fontWeight: '800', opacity: 0.7 }}> HALOZ</Text>
                        )}
                        {(index === SK8_DEFAULTS.SOULZ.icType) && (
                          <Text style={{ color: orange, fontSize: 8, fontWeight: '800', opacity: 0.7 }}> SOULZ</Text>
                        )}
                      </TouchableOpacity>
                    );
                  })}
                </ScrollView>
                <Text style={{ color: txtMuted, fontSize: 11, marginTop: 10 }}>
                  Selected: <Text style={{ color: cyan, fontWeight: '800' }}>{editSettings.icName}</Text>
                </Text>
              </View>

              {/* Color Sorting */}
              <View style={[s.card, { backgroundColor: cardBg, borderColor: border, marginBottom: 16 }]}>
                <Text style={[s.sectionLabel, { color: txtMuted }]}>COLOR SORTING / WIRING ORDER</Text>
                <View style={[s.row, { flexWrap: 'wrap', gap: 8, marginTop: 12 }]}>
                  {SORTING_LIST.map(({ index, name }) => {
                    const active = editSettings.colorSorting === index;
                    return (
                      <TouchableOpacity
                        key={index}
                        style={[s.chip, {
                          borderColor: active ? orange : border,
                          backgroundColor: active ? 'rgba(255,90,0,0.12)' : 'transparent',
                        }]}
                        onPress={() => setEditSettings(prev => prev ? {
                          ...prev, colorSorting: index, colorSortingName: name
                        } : prev)}
                      >
                        <Text style={[s.chipText, { color: active ? orange : txtMuted, fontWeight: active ? '900' : '600' }]}>
                          {name}
                        </Text>
                      </TouchableOpacity>
                    );
                  })}
                </View>
                <Text style={{ color: txtMuted, fontSize: 11, marginTop: 10 }}>
                  Selected: <Text style={{ color: orange, fontWeight: '800' }}>{editSettings.colorSortingName}</Text>
                  {editSettings.colorSorting === 2 && <Text style={{ color: txtMuted }}> (GRB — standard WS2812B)</Text>}
                </Text>
              </View>

              {/* Quick Presets */}
              <View style={[s.card, { backgroundColor: cardBg, borderColor: border, marginBottom: 24 }]}>
                <Text style={[s.sectionLabel, { color: txtMuted }]}>QUICK PRESETS</Text>
                <View style={[s.row, { marginTop: 12, gap: 12 }]}>
                  <TouchableOpacity
                    style={[s.presetBtn, { borderColor: cyan, backgroundColor: 'rgba(0,240,255,0.08)' }]}
                    onPress={() => setEditSettings(prev => prev ? {
                      ...prev,
                      ledPoints: SK8_DEFAULTS.HALOZ.points,
                      segments: SK8_DEFAULTS.HALOZ.segments,
                      icType: SK8_DEFAULTS.HALOZ.icType,
                      icName: SK8_DEFAULTS.HALOZ.icName,
                      colorSorting: SK8_DEFAULTS.HALOZ.sorting,
                      colorSortingName: SK8_DEFAULTS.HALOZ.sortingName,
                    } : prev)}
                  >
                    <Text style={{ color: cyan, fontWeight: '900', fontSize: 13 }}>HALOZ</Text>
                    <Text style={{ color: cyan, fontSize: 10, opacity: 0.7, marginTop: 2 }}>
                      {SK8_DEFAULTS.HALOZ.points}pt • {SK8_DEFAULTS.HALOZ.icName} • {SK8_DEFAULTS.HALOZ.sortingName}
                    </Text>
                  </TouchableOpacity>
                  <TouchableOpacity
                    style={[s.presetBtn, { borderColor: orange, backgroundColor: 'rgba(255,90,0,0.08)' }]}
                    onPress={() => setEditSettings(prev => prev ? {
                      ...prev,
                      ledPoints: SK8_DEFAULTS.SOULZ.points,
                      segments: SK8_DEFAULTS.SOULZ.segments,
                      icType: SK8_DEFAULTS.SOULZ.icType,
                      icName: SK8_DEFAULTS.SOULZ.icName,
                      colorSorting: SK8_DEFAULTS.SOULZ.sorting,
                      colorSortingName: SK8_DEFAULTS.SOULZ.sortingName,
                    } : prev)}
                  >
                    <Text style={{ color: orange, fontWeight: '900', fontSize: 13 }}>SOULZ</Text>
                    <Text style={{ color: orange, fontSize: 10, opacity: 0.7, marginTop: 2 }}>
                      {SK8_DEFAULTS.SOULZ.points}pt • {SK8_DEFAULTS.SOULZ.icName} • {SK8_DEFAULTS.SOULZ.sortingName}
                    </Text>
                  </TouchableOpacity>
                </View>
              </View>

              <View style={{ height: 100 }} />
            </ScrollView>
          ) : (
            <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
              <ActivityIndicator color={cyan} size="large" />
              <Text style={{ color: txtMuted, marginTop: 12 }}>Loading settings...</Text>
            </View>
          )}

          {/* ── Save Footer ── */}
          {editSettings && (
            <View style={[s.saveFooter, { backgroundColor: cardBg, borderTopColor: border }]}>
              <TouchableOpacity style={[s.cancelBtn, { borderColor: border }]} onPress={closeDetail}>
                <Text style={{ color: txtMuted, fontWeight: '700' }}>CANCEL</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={[s.saveBtn, {
                  backgroundColor: saveState === 'saved' ? green : orange,
                  opacity: saveState === 'saving' ? 0.6 : 1,
                }]}
                onPress={saveSettings}
                disabled={saveState === 'saving'}
              >
                {saveState === 'saving' ? (
                  <ActivityIndicator color="#fff" size="small" />
                ) : (
                  <Text style={{ color: '#fff', fontWeight: '900', letterSpacing: 1 }}>
                    {saveState === 'saved' ? '✓ SAVED TO DEVICE' : 'WRITE TO HARDWARE (0x62)'}
                  </Text>
                )}
              </TouchableOpacity>
            </View>
          )}
        </SafeAreaView>
      </Modal>
    </Modal>
  );
}

// ─── Styles ────────────────────────────────────────────────────────────────────
const s = StyleSheet.create({
  root: { flex: 1 },
  topBar: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 16,
    paddingVertical: 14,
    borderBottomWidth: 1,
  },
  exitBtn: {
    borderWidth: 1,
    borderRadius: 12,
    paddingVertical: 6,
    paddingHorizontal: 12,
  },
  card: {
    borderWidth: 1,
    borderRadius: 14,
    padding: 16,
  },
  row: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  sectionLabel: {
    fontSize: 10,
    fontWeight: '900',
    letterSpacing: 1.5,
    textTransform: 'uppercase',
  },
  infoLabel: {
    fontSize: 9,
    fontWeight: '800',
    letterSpacing: 1,
    textTransform: 'uppercase',
    marginBottom: 4,
  },
  statusDot: {
    width: 8,
    height: 8,
    borderRadius: 4,
    marginRight: 8,
  },
  badge: {
    borderWidth: 1,
    borderRadius: 6,
    paddingHorizontal: 8,
    paddingVertical: 3,
  },
  badgeText: {
    fontSize: 10,
    fontWeight: '800',
  },
  deviceCard: {
    borderWidth: 1,
    borderRadius: 14,
    padding: 14,
    marginBottom: 10,
    flexDirection: 'row',
    alignItems: 'flex-start',
  },
  rssiBars: {
    flexDirection: 'row',
    alignItems: 'flex-end',
    gap: 3,
    height: 20,
  },
  rssiBar: {
    width: 4,
    borderRadius: 2,
  },
  qryBtn: {
    borderWidth: 1,
    borderRadius: 8,
    paddingVertical: 5,
    paddingHorizontal: 10,
    alignItems: 'center',
  },
  actionBtn: {
    borderWidth: 1,
    borderRadius: 10,
    paddingVertical: 12,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 8,
  },
  actionBtnText: {
    fontSize: 12,
    fontWeight: '900',
    letterSpacing: 0.5,
  },
  numInput: {
    borderWidth: 1,
    borderRadius: 10,
    padding: 12,
    fontSize: 20,
    fontWeight: '900',
    textAlign: 'center',
  },
  chip: {
    borderWidth: 1,
    borderRadius: 8,
    paddingVertical: 8,
    paddingHorizontal: 12,
    flexDirection: 'row',
    alignItems: 'center',
  },
  chipText: {
    fontSize: 12,
  },
  presetBtn: {
    flex: 1,
    borderWidth: 1,
    borderRadius: 10,
    paddingVertical: 14,
    alignItems: 'center',
  },
  liveTag: {
    backgroundColor: 'rgba(0,232,135,0.08)',
    borderWidth: 1,
    borderColor: 'rgba(0,232,135,0.3)',
    borderRadius: 8,
    paddingVertical: 6,
    paddingHorizontal: 10,
  },
  saveFooter: {
    flexDirection: 'row',
    gap: 12,
    padding: 16,
    borderTopWidth: 1,
    paddingBottom: Platform.OS === 'ios' ? 28 : 16,
  },
  cancelBtn: {
    flex: 1,
    borderWidth: 1,
    borderRadius: 12,
    paddingVertical: 14,
    alignItems: 'center',
    justifyContent: 'center',
  },
  saveBtn: {
    flex: 3,
    borderRadius: 12,
    paddingVertical: 14,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
