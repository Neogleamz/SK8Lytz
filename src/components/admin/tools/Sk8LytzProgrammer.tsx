import { MaterialCommunityIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import React, { useEffect, useState } from 'react';
import {
    ActivityIndicator, Platform,
    SafeAreaView,
    ScrollView,
    StyleSheet,
    Text,
    TextInput,
    TouchableOpacity,
    View
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { LOCAL_PRODUCT_CATALOG } from '../../../constants/ProductCatalog';
import { useTheme } from '../../../context/ThemeContext';
import {
    COLOR_SORTING_RGB,
    HardwareSettings,
    IC_TYPES,
    ZenggeProtocol,
} from '../../../protocols/ZenggeProtocol';
import { AppLogger } from '../../../services/AppLogger';
import { Spacing, Typography } from '../../../theme/theme';

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
  allDevices: ScannedDevice[];
  deviceConfigs?: Record<string, HardwareSettings>;       // pre-populated from scan probe
  connectToDevice?: (d: ScannedDevice) => Promise<void>;
  disconnectFromDevice?: (id: string) => Promise<void>;
  writeToDevice: (data: number[], deviceId?: string) => Promise<void | boolean | 'partial'>;
  bleState?: string;
  handleScan: () => void;
}

const PROFILES_STORAGE_KEY = 'ng_programmer_profiles';

const IC_LIST = Object.entries(IC_TYPES).map(([k, v]) => ({ index: Number(k), name: v }));
const SORTING_LIST = Object.entries(COLOR_SORTING_RGB).map(([k, v]) => ({ index: Number(k), name: v }));

type ActiveProfileType = string;

// ─── Component ────────────────────────────────────────────────────────────────

export default function Sk8LytzProgrammer({
  visible, onClose, onExitToLogs, allDevices,
  deviceConfigs = {},
  connectToDevice, disconnectFromDevice,
  writeToDevice, bleState = 'IDLE', handleScan
}: Sk8LytzProgrammerModalProps) {
  const { Colors, isDark } = useTheme();
  const insets = useSafeAreaInsets();

  const bg       = Colors.background;
  const cardBg   = Colors.surface;
  const txtPri   = Colors.text;
  const txtMuted = Colors.textMuted;
  const border   = Colors.surfaceHighlight;
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
  const [activeProfile, setActiveProfile] = useState<ActiveProfileType>(LOCAL_PRODUCT_CATALOG[0].id);
  
  const [profiles, setProfiles] = useState<Record<string, HardwareSettings>>(() => {
    const initial: Record<string, HardwareSettings> = {};
    LOCAL_PRODUCT_CATALOG.forEach(p => {
      initial[p.id] = {
        ledPoints: p.vizDefaultPoints,
        segments: 1, // Programmer defaults to 1 segment to keep it simple
        icType: 1, // Default WS2812B
        icName: 'WS2812B',
        colorSorting: 2, // Default GRB
        colorSortingName: 'GRB',
        detected: false
      };
    });
    return initial;
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
  const currentProfileColor = LOCAL_PRODUCT_CATALOG.find(p => p.id === activeProfile)?.vizThemeColor || orange;

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
    <View style={{ flex: 1 }}>
      <SafeAreaView style={[s.root, { backgroundColor: bg }]}>

        {/* ── Header ── */}
        <View style={[s.topBar, { borderBottomColor: border, paddingTop: insets.top || 16, paddingBottom: Spacing.lg }]}>
          <TouchableOpacity onPress={onClose} style={s.backBtn}>
            <MaterialCommunityIcons name="arrow-left" size={24} color={Colors.primary} />
          </TouchableOpacity>
          <View style={{ flex: 1 }}>
            <Text style={[Typography.title, { color: Colors.text, fontSize: 18, textTransform: 'uppercase', letterSpacing: 1.5 }]}>⚡ BATCH PROGRAMMER</Text>
            <Text style={{ color: txtMuted, fontSize: 11, marginTop: Spacing.xxs }}>
              Configure multiple controllers instantly
            </Text>
          </View>
        </View>

        <ScrollView style={{ flex: 1 }} contentContainerStyle={{ padding: Spacing.lg }}>

          {/* ── Profile Selector & Config ── */}
          <View style={[s.card, { backgroundColor: cardBg, borderColor: border, marginBottom: Spacing.lg }]}>
             <View style={{ flexDirection: 'row', marginBottom: Spacing.lg, borderRadius: 8, overflow: 'hidden', borderWidth: 1, borderColor: border }}>
                 {LOCAL_PRODUCT_CATALOG.map(p => {
                   const isSelected = activeProfile === p.id;
                   return (
                     <TouchableOpacity
                        key={p.id}
                        style={{ flex: 1, paddingVertical: Spacing.md, alignItems: 'center', backgroundColor: isSelected ? p.vizThemeColor : 'transparent' }}
                        onPress={() => setActiveProfile(p.id)}
                     >
                         <Text style={{ fontWeight: 'bold', color: isSelected ? '#000' : txtMuted }}>{p.id} PROFILE</Text>
                     </TouchableOpacity>
                   );
                 })}
             </View>

             <Text style={{ color: txtPri, fontWeight: 'bold', marginBottom: Spacing.md }}>Profile Defaults (Tap to cycle payload values)</Text>
             
             {/* Points + Segments: tap to cycle OR type directly */}
             <View style={{ flexDirection: 'row', gap: Spacing.md, marginBottom: Spacing.md }}>
                <View style={[s.configBtn, { flex: 1, borderColor: border }]}>
                    <Text style={{ color: txtMuted, fontSize: 11, marginBottom: Spacing.xs }}>POINTS (LEDs)</Text>
                    <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm }}>
                      <TouchableOpacity onPress={() => cycleProperty('points')}>
                        <Text style={{ color: currentProfileColor, fontSize: 20 }}>‹</Text>
                      </TouchableOpacity>
                      <TextInput
                        style={{ color: currentProfileColor, fontWeight: 'bold', fontSize: 20, minWidth: 48, textAlign: 'center' }}
                        value={pointsText}
                        onChangeText={setPointsText}
                        onBlur={commitPoints}
                        keyboardType="numeric"
                        selectTextOnFocus
                      />
                      <TouchableOpacity onPress={() => { const v = Math.min(300, currentProfile.ledPoints + 1); updateActiveProfile({ ledPoints: v }); setPointsText(String(v)); }}>
                        <Text style={{ color: currentProfileColor, fontSize: 20 }}>›</Text>
                      </TouchableOpacity>
                    </View>
                </View>
                <View style={[s.configBtn, { flex: 1, borderColor: border }]}>
                    <Text style={{ color: txtMuted, fontSize: 11, marginBottom: Spacing.xs }}>SEGMENTS</Text>
                    <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm }}>
                      <TouchableOpacity onPress={() => cycleProperty('segments')}>
                        <Text style={{ color: currentProfileColor, fontSize: 20 }}>‹</Text>
                      </TouchableOpacity>
                      <TextInput
                        style={{ color: currentProfileColor, fontWeight: 'bold', fontSize: 20, minWidth: 32, textAlign: 'center' }}
                        value={segmentsText}
                        onChangeText={setSegmentsText}
                        onBlur={commitSegments}
                        keyboardType="numeric"
                        selectTextOnFocus
                      />
                      <TouchableOpacity onPress={() => { const maxS = Math.floor(2048/currentProfile.ledPoints); const v = Math.min(maxS, currentProfile.segments + 1); updateActiveProfile({ segments: v }); setSegmentsText(String(v)); }}>
                        <Text style={{ color: currentProfileColor, fontSize: 20 }}>›</Text>
                      </TouchableOpacity>
                    </View>
                </View>
             </View>

             <View style={{ flexDirection: 'row', gap: Spacing.md }}>
                <TouchableOpacity style={[s.configBtn, { flex: 1, borderColor: border }]} onPress={cycleIC}>
                    <Text style={{ color: txtMuted, fontSize: 11 }}>STRIP TYPE (IC)</Text>
                    <Text style={{ color: currentProfileColor, fontWeight: 'bold', fontSize: 16 }}>{currentProfile.icName}</Text>
                </TouchableOpacity>
                <TouchableOpacity style={[s.configBtn, { flex: 1, borderColor: border }]} onPress={cycleSorting}>
                    <Text style={{ color: txtMuted, fontSize: 11 }}>COLOR SORTING</Text>
                    <Text style={{ color: currentProfileColor, fontWeight: 'bold', fontSize: 16 }}>{currentProfile.colorSortingName}</Text>
                </TouchableOpacity>
             </View>
          </View>

          {/* ── Device List Header ── */}
          <View style={[s.row, { marginBottom: Spacing.md, paddingHorizontal: Spacing.xs }]}>
             <Text style={{ color: txtPri, fontWeight: 'bold', fontSize: 16 }}>Broadcast Targets</Text>
             <View style={s.row}>
                <TouchableOpacity onPress={handleScan} disabled={bleState === 'SCANNING' || bleState === 'PROBING'} style={{ marginRight: Spacing.lg }}>
                    <Text style={{ color: bleState === 'SCANNING' ? cyan : bleState === 'PROBING' ? '#a855f7' : cyan, fontSize: 12, fontWeight: 'bold' }}>
                      {bleState === 'SCANNING' ? 'SCANNING...' : bleState === 'PROBING' ? 'PROBING...' : 'SCAN'}
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
            <View style={{ alignItems: 'center', paddingVertical: Spacing.huge }}>
              <MaterialCommunityIcons name="bluetooth-off" size={48} color={border} />
              <Text style={{ color: txtMuted, marginTop: Spacing.md, fontSize: 14 }}>
                No SK8Lytz hardware detected.
              </Text>
              <Text style={{ color: txtMuted, fontSize: 12, marginTop: Spacing.xs }}>
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
                  style={[s.deviceCard, { backgroundColor: cardBg, borderColor: isSelected ? currentProfileColor : border, opacity: status === 'pending' ? 0.7 : 1 }]}
                  onPress={() => toggleDevice(device.id)}
                  activeOpacity={0.75}
                  disabled={isFlashing}
                >
                  <View style={s.row}>
                     <View style={{ width: 24, alignItems: 'center' }}>
                         {isSelected ? (
                             <MaterialCommunityIcons name="checkbox-marked" size={22} color={currentProfileColor} />
                         ) : (
                             <MaterialCommunityIcons name="checkbox-blank-outline" size={22} color={border} />
                         )}
                     </View>
                     <View style={{ flex: 1, marginLeft: Spacing.md }}>
                        <Text style={{ color: txtPri, fontWeight: '800', fontSize: 14 }}>{device.name}</Text>
                        <Text style={{ color: txtMuted, fontSize: 11, marginTop: Spacing.xxs, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>
                          {device.id} ({device.rssi} dBm)
                        </Text>
                        {/* Show detected hw from scan probe */}
                        {detected && (
                          <Text style={{ color: '#00cc88', fontSize: 10, marginTop: Spacing.xxs }}>
                            ✓ {detected.ledPoints ?? (detected as any).points ?? '?'}pts · {detected.segments ?? '?'}seg · {detected.icName ?? (detected as any).stripType ?? '?'} · {detected.colorSortingName ?? (detected as any).sorting ?? '?'}
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
                style={[s.flashBtn, { opacity: (selectedIds.length === 0 || isFlashing) ? 0.5 : 1, backgroundColor: currentProfileColor }]}
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
    </View>
  );
}

const s = StyleSheet.create({
  root: { flex: 1 },
  topBar: {
    paddingHorizontal: Spacing.lg,
    paddingVertical: Spacing.md,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    borderBottomWidth: 1,
  },
  backBtn: {
    marginRight: Spacing.lg,
    padding: Spacing.xs,
  },
  card: {
    borderRadius: 12,
    borderWidth: 1,
    padding: Spacing.lg,
  },
  row: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  configBtn: {
      borderWidth: 1,
      borderRadius: 8,
      padding: Spacing.md,
      alignItems: 'center',
      backgroundColor: 'rgba(255,255,255,0.03)'
  },
  deviceCard: {
    borderRadius: 12,
    borderWidth: 1,
    padding: Spacing.md,
    marginBottom: Spacing.sm,
  },
  footer: {
      position: 'absolute',
      bottom: 0,
      left: 0,
      right: 0,
      padding: Spacing.lg,
      paddingBottom: Spacing.lg, // insets.bottom applied inline via s.footerDynamic
      borderTopWidth: 1,
  },
  flashBtn: {
      paddingVertical: Spacing.lg,
      borderRadius: 12,
      alignItems: 'center',
      justifyContent: 'center',
      ...Platform.select({
        web: { boxShadow: '0px 4px 8px rgba(0,0,0,0.3)' } as any,
        default: { shadowColor: '#000000', shadowOffset: { width: 0, height: 4 }, shadowOpacity: 0.3, shadowRadius: 8, elevation: 6 }
      })
  }
});
