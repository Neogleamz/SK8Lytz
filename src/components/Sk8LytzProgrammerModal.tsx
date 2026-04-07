import React, { useState, useEffect } from 'react';
import {
  View, Text, StyleSheet, Modal, SafeAreaView, TouchableOpacity,
  ScrollView, ActivityIndicator, Platform, TextInput
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { AppLogger } from '../services/AppLogger';
import { useTheme } from '../context/ThemeContext';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Typography } from '../theme/theme';
import {
  ZenggeProtocol,
  HardwareSettings,
  SK8_DEFAULTS,
  IC_TYPES,
  COLOR_SORTING_RGB,
} from '../protocols/ZenggeProtocol';

// ─── Types ─────────────────────────────────────────────────────────────────────

interface ScannedDevice {
  id: string;
  name: string;
  rssi: number;
}

interface Sk8LytzProgrammerModalProps {
  visible: boolean;
  onClose: () => void;
  onExitToLogs?: () => void;
  allDevices: any[];
  deviceConfigs?: Record<string, any>;       // pre-populated from scan probe
  connectToDevice?: (d: any) => Promise<void>;
  disconnectFromDevice?: (id: string) => Promise<void>;
  writeToDevice: (data: number[], deviceId?: string) => Promise<void>;
  isScanning: boolean;
  isScanProbing?: boolean;
  handleScan: () => void;
}

const PROFILES_STORAGE_KEY = 'ng_programmer_profiles';

const IC_LIST = Object.entries(IC_TYPES).map(([k, v]) => ({ index: Number(k), name: v }));
const SORTING_LIST = Object.entries(COLOR_SORTING_RGB).map(([k, v]) => ({ index: Number(k), name: v }));

type ActiveProfileType = 'HALOZ' | 'SOULZ';

// ─── Component ────────────────────────────────────────────────────────────────

export default function Sk8LytzProgrammerModal({
  visible, onClose, onExitToLogs, allDevices,
  deviceConfigs = {},
  connectToDevice, disconnectFromDevice,
  writeToDevice, isScanning, isScanProbing = false, handleScan
}: Sk8LytzProgrammerModalProps) {
  const { Colors, isDark } = useTheme();
  const insets = useSafeAreaInsets();

  const bg       = isDark ? '#0a0d18' : '#f0f2f5';
  const cardBg   = isDark ? '#141829' : '#ffffff';
  const txtPri   = isDark ? '#ffffff' : '#111827';
  const txtMuted = isDark ? '#8a96b3' : '#6b7280';
  const border   = isDark ? '#252c47' : '#e5e7eb';
  const cyan     = '#00f0ff';
  const orange   = Colors.primary;
  const amber    = Colors.secondary;

  useEffect(() => {
    if (visible) AppLogger.log('SCREEN_OPENED', { screenName: 'Device Auto-Programmer' });
  }, [visible]);
  const green    = '#00e887';

  // ─── State ──────────────────────────────────────────────────────────────────
  const [scannedDevices, setScannedDevices] = useState<ScannedDevice[]>([]);
  const [selectedIds, setSelectedIds] = useState<string[]>([]);
  const [activeProfile, setActiveProfile] = useState<ActiveProfileType>('HALOZ');
  
  const [profiles, setProfiles] = useState<Record<ActiveProfileType, HardwareSettings>>({
    HALOZ: {
      ledPoints: SK8_DEFAULTS.HALOZ.points,
      segments: SK8_DEFAULTS.HALOZ.segments,
      icType: SK8_DEFAULTS.HALOZ.icType,
      icName: SK8_DEFAULTS.HALOZ.icName,
      colorSorting: SK8_DEFAULTS.HALOZ.sorting,
      colorSortingName: SK8_DEFAULTS.HALOZ.sortingName,
      detected: false
    },
    SOULZ: {
      ledPoints: SK8_DEFAULTS.SOULZ.points,
      segments: SK8_DEFAULTS.SOULZ.segments,
      icType: SK8_DEFAULTS.SOULZ.icType,
      icName: SK8_DEFAULTS.SOULZ.icName,
      colorSorting: SK8_DEFAULTS.SOULZ.sorting,
      colorSortingName: SK8_DEFAULTS.SOULZ.sortingName,
      detected: false
    }
  });

  const [flashStatus, setFlashStatus] = useState<Record<string, 'idle' | 'pending' | 'success' | 'failed'>>({});
  const [isFlashing, setIsFlashing] = useState(false);

  // Local editable text for points/segments (avoids tap-cycling)
  const [pointsText, setPointsText] = useState(String(profiles[activeProfile].ledPoints));
  const [segmentsText, setSegmentsText] = useState(String(profiles[activeProfile].segments));

  // Sync allDevices → scannedDevices, pre-populate detected hw from scan probe
  useEffect(() => {
    if (!visible) return;
    const updated: ScannedDevice[] = allDevices.map(d => ({
      id: d.id,
      name: d.name || d.id.slice(-8),
      rssi: d.rssi || -99,
    }));
    setScannedDevices(updated);
    setFlashStatus(prev => {
      const next = { ...prev };
      updated.forEach(d => { if (!next[d.id]) next[d.id] = 'idle'; });
      return next;
    });
  }, [allDevices, visible]);

  // Sync text inputs when profile changes
  useEffect(() => {
    setPointsText(String(profiles[activeProfile].ledPoints));
    setSegmentsText(String(profiles[activeProfile].segments));
  }, [activeProfile]);

  // Commit typed points/segments to profile state on blur
  const commitPoints = () => {
    const v = Math.max(1, Math.min(300, parseInt(pointsText) || currentProfile.ledPoints));
    setPointsText(String(v));
    updateActiveProfile({ ledPoints: v });
  };
  const commitSegments = () => {
    const v = Math.max(1, Math.min(2048, parseInt(segmentsText) || currentProfile.segments));
    setSegmentsText(String(v));
    updateActiveProfile({ segments: v });
  };

  // ─── Load Profiles ──────────────────────────────────────────────────────────
  useEffect(() => {
      const load = async () => {
          try {
              const saved = await AsyncStorage.getItem(PROFILES_STORAGE_KEY);
              if (saved) setProfiles(JSON.parse(saved));
          } catch(e) {}
      };
      if (visible) load();
  }, [visible]);

  // ─── Save Profiles ──────────────────────────────────────────────────────────
  const saveProfileChange = async (newProfiles: Record<ActiveProfileType, HardwareSettings>) => {
      setProfiles(newProfiles);
      try {
          await AsyncStorage.setItem(PROFILES_STORAGE_KEY, JSON.stringify(newProfiles));
      } catch (e) {
          console.error('[Programmer] Failed to persist profile');
      }
  };

  const updateActiveProfile = (update: Partial<HardwareSettings>) => {
      const p = profiles[activeProfile];
      const newSettings = { ...p, ...update };
      saveProfileChange({ ...profiles, [activeProfile]: newSettings });
  };

  // ─── Batch Flashing — connect → write 0x62 → disconnect ────────────────────
  const handleFlash = async () => {
      if (selectedIds.length === 0) return;
      setIsFlashing(true);

      const config = profiles[activeProfile];
      const cmd = ZenggeProtocol.writeHardwareSettings(
          config.ledPoints,
          config.segments,
          config.icType,
          config.colorSorting
      );

      for (const id of selectedIds) {
          setFlashStatus(prev => ({ ...prev, [id]: 'pending' }));
          try {
              // If device needs connection first, connect then write then disconnect
              const device = allDevices.find(d => d.id === id);
              if (connectToDevice && device) {
                  await connectToDevice(device);
                  await new Promise(res => setTimeout(res, 600)); // let GATT settle
              }
              await writeToDevice(cmd, id);
              await new Promise(res => setTimeout(res, 400)); // let write land
              if (disconnectFromDevice) {
                  await disconnectFromDevice(id);
                  await new Promise(res => setTimeout(res, 400)); // gap between ops
              }
              setFlashStatus(prev => ({ ...prev, [id]: 'success' }));
              AppLogger.log('PERFORMANCE_METRIC', { metricName: 'HW_CONFIG_FLASHED', value: 1, unit: id, deviceId: id });
          } catch (e) {
              setFlashStatus(prev => ({ ...prev, [id]: 'failed' }));
          }
      }
      setIsFlashing(false);
  };

  const toggleSelectAll = () => {
      if (selectedIds.length === scannedDevices.length) {
          setSelectedIds([]);
      } else {
          setSelectedIds(scannedDevices.map(d => d.id));
      }
  };

  const toggleDevice = (id: string) => {
      if (selectedIds.includes(id)) {
          setSelectedIds(selectedIds.filter(i => i !== id));
      } else {
          setSelectedIds([...selectedIds, id]);
      }
  };

  // ─── UI Helpers ─────────────────────────────────────────────────────────────
  const currentProfile = profiles[activeProfile];

  const cycleProperty = (field: 'points' | 'segments') => {
      if (field === 'points') {
          const v = Math.min(300, currentProfile.ledPoints + 1);
          updateActiveProfile({ ledPoints: v === 1 ? 300 : v });
          setPointsText(String(v === 1 ? 300 : v));
      } else {
          const v = currentProfile.segments + 1;
          const clamped = v > Math.floor(2048 / currentProfile.ledPoints) ? 1 : v;
          updateActiveProfile({ segments: clamped });
          setSegmentsText(String(clamped));
      }
  };

  const cycleIC = () => {
      const idx = IC_LIST.findIndex(i => i.index === currentProfile.icType);
      const next = IC_LIST[(idx + 1) % IC_LIST.length];
      updateActiveProfile({ icType: next.index, icName: next.name });
  };

  const cycleSorting = () => {
      const idx = SORTING_LIST.findIndex(i => i.index === currentProfile.colorSorting);
      const next = SORTING_LIST[(idx + 1) % SORTING_LIST.length];
      updateActiveProfile({ colorSorting: next.index, colorSortingName: next.name });
  };

  // ─── Render ─────────────────────────────────────────────────────────────────
  return (
    <Modal visible={visible} animationType="slide" presentationStyle="fullScreen" onRequestClose={onClose}>
      <SafeAreaView style={[s.root, { backgroundColor: bg }]}>

        {/* ── Header ── */}
        <View style={[s.topBar, { borderBottomColor: border }]}>
          <View>
            <Text style={[Typography.title, { color: orange, fontSize: 18 }]}>⚡ BATCH PROGRAMMER</Text>
            <Text style={{ color: txtMuted, fontSize: 11, marginTop: 2 }}>
              Configure multiple controllers instantly
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

          {/* ── Profile Selector & Config ── */}
          <View style={[s.card, { backgroundColor: cardBg, borderColor: border, marginBottom: 16 }]}>
             <View style={{ flexDirection: 'row', marginBottom: 16, borderRadius: 8, overflow: 'hidden', borderWidth: 1, borderColor: border }}>
                 <TouchableOpacity 
                    style={{ flex: 1, paddingVertical: 12, alignItems: 'center', backgroundColor: activeProfile === 'HALOZ' ? cyan : 'transparent' }}
                    onPress={() => setActiveProfile('HALOZ')}
                 >
                     <Text style={{ fontWeight: 'bold', color: activeProfile === 'HALOZ' ? '#000' : txtMuted }}>HALOZ PROFILE</Text>
                 </TouchableOpacity>
                 <TouchableOpacity 
                    style={{ flex: 1, paddingVertical: 12, alignItems: 'center', backgroundColor: activeProfile === 'SOULZ' ? amber : 'transparent' }}
                    onPress={() => setActiveProfile('SOULZ')}
                 >
                     <Text style={{ fontWeight: 'bold', color: activeProfile === 'SOULZ' ? '#000' : txtMuted }}>SOULZ PROFILE</Text>
                 </TouchableOpacity>
             </View>

             <Text style={{ color: txtPri, fontWeight: 'bold', marginBottom: 12 }}>Profile Defaults (Tap to cycle payload values)</Text>
             
             {/* Points + Segments: tap to cycle OR type directly */}
             <View style={{ flexDirection: 'row', gap: 12, marginBottom: 12 }}>
                <View style={[s.configBtn, { flex: 1, borderColor: border }]}>
                    <Text style={{ color: txtMuted, fontSize: 11, marginBottom: 4 }}>POINTS (LEDs)</Text>
                    <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                      <TouchableOpacity onPress={() => cycleProperty('points')}>
                        <Text style={{ color: activeProfile==='HALOZ'? cyan : amber, fontSize: 20 }}>‹</Text>
                      </TouchableOpacity>
                      <TextInput
                        style={{ color: activeProfile==='HALOZ'? cyan : amber, fontWeight: 'bold', fontSize: 20, minWidth: 48, textAlign: 'center' }}
                        value={pointsText}
                        onChangeText={setPointsText}
                        onBlur={commitPoints}
                        keyboardType="numeric"
                        selectTextOnFocus
                      />
                      <TouchableOpacity onPress={() => { const v = Math.min(300, currentProfile.ledPoints + 1); updateActiveProfile({ ledPoints: v }); setPointsText(String(v)); }}>
                        <Text style={{ color: activeProfile==='HALOZ'? cyan : amber, fontSize: 20 }}>›</Text>
                      </TouchableOpacity>
                    </View>
                </View>
                <View style={[s.configBtn, { flex: 1, borderColor: border }]}>
                    <Text style={{ color: txtMuted, fontSize: 11, marginBottom: 4 }}>SEGMENTS</Text>
                    <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                      <TouchableOpacity onPress={() => cycleProperty('segments')}>
                        <Text style={{ color: activeProfile==='HALOZ'? cyan : amber, fontSize: 20 }}>‹</Text>
                      </TouchableOpacity>
                      <TextInput
                        style={{ color: activeProfile==='HALOZ'? cyan : amber, fontWeight: 'bold', fontSize: 20, minWidth: 32, textAlign: 'center' }}
                        value={segmentsText}
                        onChangeText={setSegmentsText}
                        onBlur={commitSegments}
                        keyboardType="numeric"
                        selectTextOnFocus
                      />
                      <TouchableOpacity onPress={() => { const maxS = Math.floor(2048/currentProfile.ledPoints); const v = Math.min(maxS, currentProfile.segments + 1); updateActiveProfile({ segments: v }); setSegmentsText(String(v)); }}>
                        <Text style={{ color: activeProfile==='HALOZ'? cyan : amber, fontSize: 20 }}>›</Text>
                      </TouchableOpacity>
                    </View>
                </View>
             </View>

             <View style={{ flexDirection: 'row', gap: 12 }}>
                <TouchableOpacity style={[s.configBtn, { flex: 1, borderColor: border }]} onPress={cycleIC}>
                    <Text style={{ color: txtMuted, fontSize: 11 }}>STRIP TYPE (IC)</Text>
                    <Text style={{ color: activeProfile === 'HALOZ'? cyan : amber, fontWeight: 'bold', fontSize: 16 }}>{currentProfile.icName}</Text>
                </TouchableOpacity>
                <TouchableOpacity style={[s.configBtn, { flex: 1, borderColor: border }]} onPress={cycleSorting}>
                    <Text style={{ color: txtMuted, fontSize: 11 }}>COLOR SORTING</Text>
                    <Text style={{ color: activeProfile === 'HALOZ'? cyan : amber, fontWeight: 'bold', fontSize: 16 }}>{currentProfile.colorSortingName}</Text>
                </TouchableOpacity>
             </View>
          </View>

          {/* ── Device List Header ── */}
          <View style={[s.row, { marginBottom: 12, paddingHorizontal: 4 }]}>
             <Text style={{ color: txtPri, fontWeight: 'bold', fontSize: 16 }}>Broadcast Targets</Text>
             <View style={s.row}>
                <TouchableOpacity onPress={handleScan} disabled={isScanning || isScanProbing} style={{ marginRight: 16 }}>
                    <Text style={{ color: isScanning ? cyan : isScanProbing ? '#a855f7' : cyan, fontSize: 12, fontWeight: 'bold' }}>
                      {isScanning ? 'SCANNING...' : isScanProbing ? 'PROBING...' : 'SCAN'}
                    </Text>
                </TouchableOpacity>
                {scannedDevices.length > 0 && (
                  <TouchableOpacity onPress={toggleSelectAll}>
                      <Text style={{ color: amber, fontSize: 12, fontWeight: 'bold' }}>
                          {selectedIds.length === scannedDevices.length ? 'DESELECT ALL' : 'SELECT ALL'}
                      </Text>
                  </TouchableOpacity>
                )}
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
                Ensure units are powered on and tap SCAN.
              </Text>
            </View>
          ) : (
            scannedDevices.map(device => {
              const isSelected = selectedIds.includes(device.id);
              const status = flashStatus[device.id] || 'idle';
              const detected = deviceConfigs[device.id];
              
              return (
                <TouchableOpacity
                  key={device.id}
                  style={[s.deviceCard, { backgroundColor: cardBg, borderColor: isSelected ? (activeProfile==='HALOZ'?cyan:amber) : border, opacity: status === 'pending' ? 0.7 : 1 }]}
                  onPress={() => toggleDevice(device.id)}
                  activeOpacity={0.75}
                  disabled={isFlashing}
                >
                  <View style={s.row}>
                     <View style={{ width: 24, alignItems: 'center' }}>
                         {isSelected ? (
                             <MaterialCommunityIcons name="checkbox-marked" size={22} color={activeProfile==='HALOZ'?cyan:amber} />
                         ) : (
                             <MaterialCommunityIcons name="checkbox-blank-outline" size={22} color={border} />
                         )}
                     </View>
                     <View style={{ flex: 1, marginLeft: 12 }}>
                        <Text style={{ color: txtPri, fontWeight: '800', fontSize: 14 }}>{device.name}</Text>
                        <Text style={{ color: txtMuted, fontSize: 11, marginTop: 2, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>
                          {device.id} ({device.rssi} dBm)
                        </Text>
                        {/* Show detected hw from scan probe */}
                        {detected && (
                          <Text style={{ color: '#00cc88', fontSize: 10, marginTop: 2 }}>
                            ✓ {detected.ledPoints ?? detected.points ?? '?'}pts · {detected.segments ?? '?'}seg · {detected.icName ?? detected.stripType ?? '?'} · {detected.colorSortingName ?? detected.sorting ?? '?'}
                          </Text>
                        )}
                     </View>
                     <View style={{ alignItems: 'flex-end' }}>
                         {status === 'pending' && <ActivityIndicator size="small" color={cyan} />}
                         {status === 'success' && <Text style={{ color: green, fontWeight: 'bold', fontSize: 12 }}>SUCCESS</Text>}
                         {status === 'failed' && <Text style={{ color: '#FF3D71', fontWeight: 'bold', fontSize: 12 }}>FAILED</Text>}
                     </View>
                  </View>
                </TouchableOpacity>
              );
            })
          )}
          
          <View style={{ height: 100 }} />
        </ScrollView>

        {/* ── Action Footer ── */}
        <View style={[s.footer, { backgroundColor: cardBg, borderColor: border, paddingBottom: Math.max(insets.bottom, 16) }]}>
            <TouchableOpacity 
                style={[s.flashBtn, { opacity: (selectedIds.length === 0 || isFlashing) ? 0.5 : 1, backgroundColor: activeProfile === 'HALOZ' ? cyan : amber }]}
                disabled={selectedIds.length === 0 || isFlashing}
                onPress={handleFlash}
            >
                {isFlashing ? (
                    <ActivityIndicator color="#000" />
                ) : (
                    <Text style={{ color: '#000', fontWeight: '900', fontSize: 16 }}>
                        FLASH {selectedIds.length} DEVICE{selectedIds.length !== 1 ? 'S' : ''}
                    </Text>
                )}
            </TouchableOpacity>
        </View>
      </SafeAreaView>
    </Modal>
  );
}

const s = StyleSheet.create({
  root: { flex: 1 },
  topBar: {
    paddingHorizontal: 16,
    paddingVertical: 12,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    borderBottomWidth: 1,
  },
  exitBtn: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 6,
    borderWidth: 1,
  },
  card: {
    borderRadius: 12,
    borderWidth: 1,
    padding: 16,
  },
  row: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  configBtn: {
      borderWidth: 1,
      borderRadius: 8,
      padding: 12,
      alignItems: 'center',
      backgroundColor: 'rgba(255,255,255,0.03)'
  },
  deviceCard: {
    borderRadius: 12,
    borderWidth: 1,
    padding: 12,
    marginBottom: 8,
  },
  footer: {
      position: 'absolute',
      bottom: 0,
      left: 0,
      right: 0,
      padding: 16,
      paddingBottom: 16, // insets.bottom applied inline via s.footerDynamic
      borderTopWidth: 1,
  },
  flashBtn: {
      paddingVertical: 16,
      borderRadius: 12,
      alignItems: 'center',
      justifyContent: 'center',
      shadowColor: '#000',
      shadowOffset: { width: 0, height: 4 },
      shadowOpacity: 0.3,
      shadowRadius: 8,
      elevation: 6
  }
});
