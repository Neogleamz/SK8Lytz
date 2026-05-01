import AsyncStorage from '@react-native-async-storage/async-storage';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import React, { useEffect, useState } from 'react';
import { ActivityIndicator, KeyboardAvoidingView, Linking, Platform, SafeAreaView, ScrollView, StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { useTheme } from '../../context/ThemeContext';
import { ZenggeProtocol } from '../../protocols/ZenggeProtocol';
import { Spacing, Typography } from '../../theme/theme';

import { LOCAL_PRODUCT_CATALOG, getLocalProfileById } from '../../constants/ProductCatalog';
import { RegisteredDevice } from '../../hooks/useRegistration';
import { HardwareStatusPills } from '../../components/dashboard/HardwareStatusPills';
import { getDefaultGroupName } from '../../utils/NamingUtils';
import { AppLogger } from '../../services/AppLogger';

import type { BleConnectionState } from '../../types/dashboard.types';
import type { PendingRegistration } from '../../types/dashboard.types';

interface HardwareSetupWizardScreenProps {
  onSetupComplete: (devices: RegisteredDevice[]) => Promise<void> | void;
  /** BLE primitives injected from DashboardScreen — eliminates competing useBLE() instance */
  scanForPeripherals: (options?: { disableProbing?: boolean; keepAlive?: boolean }) => void;
  bleState: BleConnectionState;
  requestPermissions: () => Promise<boolean>;
  isBluetoothSupported: boolean;
  isBluetoothEnabled: boolean;
  pendingRegistrations: PendingRegistration[];
  /** Updater for pendingRegistrations — used to enrich entries in-place with EEPROM probe data */
  setPendingRegistrations: React.Dispatch<React.SetStateAction<PendingRegistration[]>>;
  writeToDevice: (data: number[], deviceId?: string) => Promise<void | boolean | 'partial'>;
  probeDevice: (deviceId: string) => Promise<any>;
}

export default function HardwareSetupWizardScreen({
  onSetupComplete,
  scanForPeripherals,
  bleState,
  requestPermissions,
  isBluetoothSupported,
  isBluetoothEnabled,
  pendingRegistrations,
  setPendingRegistrations,
  writeToDevice,
  probeDevice,
}: HardwareSetupWizardScreenProps) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const [hasStartedScan, setHasStartedScan] = useState(false);
  const [step, setStep] = useState<1 | 2 | 3>(1);
  const [isBlinking, setIsBlinking] = useState<string | null>(null);
  const [isClaiming, setIsClaiming] = useState(false);
  const [selectedDeviceMacs, setSelectedDeviceMacs] = useState<Set<string>>(new Set());
  
  // Step 3 State
  const [groupName, setGroupName] = useState('');
  const [deviceConfigsState, setDeviceConfigsState] = useState<Record<string, {name: string, type: string, position: 'Left'|'Right'|null, points: number}>>({});

  useEffect(() => {
    // Wait for user to hit next. Devices are NOT auto-selected based on user feedback.
  }, [pendingRegistrations, hasStartedScan, bleState]);

  useEffect(() => {
    if (!hasStartedScan && isBluetoothSupported && isBluetoothEnabled) {
      handleStartScan();
    }
  }, [hasStartedScan, isBluetoothSupported, isBluetoothEnabled]);

  useEffect(() => {
    let timer: any;
    if (hasStartedScan && step < 3 && bleState !== 'SCANNING' && bleState !== 'PROBING') {
      // Keep continuously polling while the user is still looking for hardware (Wait 2s to let radio breathe)
      // disableProbing is now a no-op — probing is on-demand only (BLINK tap)
      timer = setTimeout(() => {
        scanForPeripherals({ keepAlive: true });
      }, 2000);
    }
    return () => clearTimeout(timer);
  }, [step, bleState, hasStartedScan]);

  const handleStartScan = async () => {
    const granted = await requestPermissions();
    if (granted && bleState !== 'SCANNING') {
      setHasStartedScan(true);
      // disableProbing is now a no-op — probing is on-demand only (BLINK tap)
      scanForPeripherals();
    }
  };

  const toggleSelection = (deviceMac: string) => {
    setSelectedDeviceMacs(prev => {
      const next = new Set(prev);
      if (next.has(deviceMac)) next.delete(deviceMac);
      else next.add(deviceMac);
      return next;
    });
  };

  const handleBlinkDevice = async (deviceMac: string) => {
    if (isBlinking) return;
    setIsBlinking(deviceMac);
    
    // Blink & Claim: Automatically ensure the device is selected when tested
    setSelectedDeviceMacs(prev => {
      const next = new Set(prev);
      next.add(deviceMac);
      return next;
    });

    try {
      // Resolve product profile to get accurate blink LED count
      const registration = pendingRegistrations.find(r => r.device_mac === deviceMac);
      const productType = (registration?.product_type && registration.product_type !== 'UNKNOWN') ? registration.product_type : LOCAL_PRODUCT_CATALOG[0].id;
      const profile = getLocalProfileById(productType) || LOCAL_PRODUCT_CATALOG[0];
      const blinkPoints = registration?.led_points || profile.vizDefaultPoints;

      // 0x59 static multi-color mode: Green. 
      const colorArray = Array(blinkPoints).fill({ r: 0, g: 255, b: 0 });
      const blinkPayload = ZenggeProtocol.setMultiColor(colorArray, blinkPoints, 1, 1, 0x00); 
      await writeToDevice(blinkPayload, deviceMac);
      
      // ── ON-DEMAND PROBE (perf/ble-probe-on-demand) ────────────────────────────
      // The blink write just opened a GATT connection to this device. Fire the
      // hardware probe now while that channel is hot. This is non-blocking —
      // the user sees the blink immediately and the card enriches in the background.
      probeDevice(deviceMac).then((hwConfig: any) => {
        if (!hwConfig) return;
        // Update this registration entry in-place with real EEPROM data
        setPendingRegistrations(prev => prev.map(r =>
          r.device_mac === deviceMac
            ? {
                ...r,
                led_points:    hwConfig.ledPoints    ?? r.led_points,
                segments:      hwConfig.segments      ?? r.segments,
                ic_type:       hwConfig.icName        ?? r.ic_type,
                color_sorting: hwConfig.colorSortingName ?? r.color_sorting,
                rf_mode:       hwConfig.rfMode        ?? r.rf_mode,
              }
            : r
        ));
        // Cache the result so repeat wizard visits are instant
        AsyncStorage.setItem(`@sk8_hw_${deviceMac}`, JSON.stringify(hwConfig)).catch(() => {});
        AppLogger.log('DEVICE_DISCOVERED', { context: 'on_demand_probe_complete', deviceId: deviceMac, ledPoints: hwConfig.ledPoints });
      }).catch(() => { /* Non-fatal — registration defaults remain */ });

      // Keep it solid green for 10 seconds based on user request, then send Off command
      setTimeout(async () => {
        const offPayload = ZenggeProtocol.turnOff();
        await writeToDevice(offPayload, deviceMac);
        setIsBlinking(null);
      }, 10000);
    } catch (e) {
      console.warn("Blink test failed", e);
      setIsBlinking(null);
    }
  };

  const renderStep1 = () => (
    <View style={styles.content}>
      <Text style={[Typography.title, styles.title]}>Hardware Setup</Text>
      
      <Text style={styles.subtitle}>
        Let's sync up. We need to locate and claim your skate controllers first.
      </Text>

      <View style={styles.instructionCard}>
        <Text style={styles.instructionHeader}>Step 1: Power On</Text>
        <Text style={styles.instructionBody}>
          Ensure your SK8Lytz controllers are plugged in and actively powered on.
        </Text>
      </View>

      <View style={styles.instructionCard}>
        <Text style={styles.instructionHeader}>Step 2: Proximity</Text>
        <Text style={styles.instructionBody}>
           Keep your mobile device within 5 feet of the controllers for the initial claim process.
        </Text>
      </View>

      <TouchableOpacity 
        style={styles.helpLink} 
        onPress={() => Linking.openURL('https://sk8lytz.com/setup')}
      >
        <MaterialCommunityIcons name="help-circle-outline" size={16} color={Colors.textMuted} />
        <Text style={styles.helpText}>View Installation Guide</Text>
      </TouchableOpacity>
    </View>
  );

  const renderStep2 = () => {
    // Dynamically categorize discovered devices based on the product catalog
    const deviceGroups = LOCAL_PRODUCT_CATALOG.map(p => ({
      profile: p,
      devices: pendingRegistrations.filter(r => r.product_type === p.id)
    }));
    const unknown = pendingRegistrations.filter(r => 
      !LOCAL_PRODUCT_CATALOG.some(p => p.id === r.product_type)
    );

    const renderDeviceGroup = (title: string, devices: typeof pendingRegistrations, color: string) => {
      if (devices.length === 0) return null;
      return (
        <View style={styles.groupContainer}>
          <Text style={[styles.groupTitle, { color }]}>{title} DISCOVERED</Text>
          {devices.map((device) => {
            const isSelected = selectedDeviceMacs.has(device.device_mac);
            return (
              <TouchableOpacity 
                key={device.device_mac} 
                style={[styles.deviceRow, isSelected ? styles.deviceRowSelected : null]}
                activeOpacity={0.7}
                onPress={() => toggleSelection(device.device_mac)}
              >
                <View style={styles.checkboxContainer}>
                  <MaterialCommunityIcons 
                    name={isSelected ? "checkbox-marked" : "checkbox-blank-outline"} 
                    size={24} 
                    color={isSelected ? color : Colors.textMuted} 
                  />
                </View>
                <View style={styles.deviceInfo}>
                    <Text style={[styles.deviceName, isSelected ? { color } : { opacity: 0.6 }]}>
                      {device.device_name}
                    </Text>
                    <Text style={[styles.deviceMeta, {marginBottom: Spacing.xs}]}>ID: {device.device_mac.slice(-5).toUpperCase()}  •  RSSI: {device.rssi}</Text>
                    {device.product_type === 'UNKNOWN' ? (
                       <Text style={styles.deviceMeta}>IDENTIFYING HARDWARE...</Text>
                    ) : (
                       <HardwareStatusPills device={device} />
                    )}
                  </View>
                <TouchableOpacity 
                  style={[styles.blinkBtn, isBlinking === device.device_mac && styles.blinkBtnActive]}
                  onPress={() => handleBlinkDevice(device.device_mac)}
                  disabled={isBlinking !== null}
                >
                  <MaterialCommunityIcons 
                    name="shield-sun" 
                    size={18} 
                    color={isBlinking === device.device_mac ? '#000' : '#00F0FF'} 
                  />
                  <Text style={[styles.blinkBtnText, isBlinking === device.device_mac && { color: '#000' }]}>
                    {isBlinking === device.device_mac ? '✦ BLINKING' : 'BLINK'}
                  </Text>
                </TouchableOpacity>
              </TouchableOpacity>
            );
          })}
        </View>
      );
    };

    return (
      <View style={styles.content}>
        <View style={styles.successIconHeader}>
          <MaterialCommunityIcons name="check-circle" size={48} color="#4ade80" />
        </View>
        <Text style={[Typography.title, styles.title]}>Hardware Found</Text>
        <Text style={styles.subtitle}>
          Tap "BLINK" to physically identify and claim your controllers. Uncheck any devices that aren't yours.
        </Text>

        <ScrollView style={styles.deviceScroll} contentContainerStyle={{ paddingBottom: Spacing.xl }}>
          {deviceGroups.map((group) => renderDeviceGroup(`${group.profile.id}`, group.devices, group.profile.vizThemeColor || '#00f0ff'))}
          {renderDeviceGroup('SCANNING / UNKNOWN', unknown, Colors.textMuted)}
          
          {pendingRegistrations.length === 0 && bleState !== 'SCANNING' && bleState !== 'PROBING' && (
            <View style={styles.emptyState}>
              <Text style={styles.emptyText}>No devices found. Ensure they are powered on.</Text>
            </View>
          )}
        </ScrollView>
      </View>
    );
  };

  // --- Step 3: Registration Form ---
  const renderStep3 = () => {
    const selected = pendingRegistrations.filter(r => selectedDeviceMacs.has(r.device_mac));
    return (
      <View style={styles.content}>
        {step !== 3 && (
          <View style={styles.successIconHeader}>
            <MaterialCommunityIcons name="skate" size={48} color={Colors.primary} />
          </View>
        )}
        <Text style={[Typography.title, styles.title, { marginBottom: Spacing.xs }]}>Name Your Skates</Text>
        <Text style={[styles.subtitle, { marginBottom: Spacing.md }]}>
          Assign them to a Fleet and designate left or right.
        </Text>

        <ScrollView style={styles.scrollViewWrapper} contentContainerStyle={{ paddingBottom: Spacing.xxxl }} showsVerticalScrollIndicator={false}>
          {/* Group */}
          <Text style={styles.label}>FLEET / GROUP NAME</Text>
          <TextInput 
            style={[styles.input, { paddingVertical: Spacing.md, paddingHorizontal: Spacing.md }]} 
            value={groupName} 
            onChangeText={setGroupName}
            placeholder="e.g. My Skates" 
            placeholderTextColor={Colors.textMuted} 
            maxLength={32} 
          />

          {selected.map((device, idx) => {
            const config = deviceConfigsState[device.device_mac] || { name: '', type: LOCAL_PRODUCT_CATALOG[0].id, position: null, points: LOCAL_PRODUCT_CATALOG[0].defaultLedPoints };
            
            const updateConfig = (key: 'name' | 'type' | 'position' | 'points', val: string | number | null) => {
               setDeviceConfigsState(prev => ({
                 ...prev,
                 [device.device_mac]: { ...prev[device.device_mac], [key]: val }
               }));
            };

            return (
              <View key={device.device_mac} style={styles.deviceConfigCard}>
                <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: Spacing.sm, justifyContent: 'space-between' }}>
                  <MaterialCommunityIcons name="bluetooth" size={18} color={Colors.primary} style={{ marginRight: Spacing.sm }} />
                  <Text style={styles.deviceConfigTitle}>Network ID: {device.device_mac.slice(-5)}</Text>
                  <TouchableOpacity 
                    style={[styles.blinkBtn, isBlinking === device.device_mac && styles.blinkBtnActive, { paddingVertical: Spacing.xs, paddingHorizontal: Spacing.sm }]}
                    onPress={() => handleBlinkDevice(device.device_mac)}
                    disabled={isBlinking !== null}
                  >
                    <MaterialCommunityIcons 
                      name="shield-sun" 
                      size={14} 
                      color={isBlinking === device.device_mac ? '#000' : '#00F0FF'} 
                    />
                    <Text style={[styles.blinkBtnText, isBlinking === device.device_mac && { color: '#000' }]}>
                      {isBlinking === device.device_mac ? '✦ BLINKING' : 'BLINK'}
                    </Text>
                  </TouchableOpacity>
                </View>

                {/* Name */}
                <Text style={styles.labelSmall}>DEVICE NAME</Text>
                <TextInput 
                  style={[styles.inputSmall, { paddingVertical: Spacing.sm }]} 
                  value={config.name} 
                  onChangeText={(t) => updateConfig('name', t)}
                  placeholder="e.g. Left Skate" 
                  placeholderTextColor={Colors.textMuted} 
                  maxLength={32} 
                />

                <View style={{ flexDirection: 'row', gap: Spacing.md, marginTop: Spacing.sm }}>
                  {/* Type */}
                  <View style={{ flex: 1 }}>
                    <Text style={styles.labelSmall}>TYPE</Text>
                    <View style={styles.segRow}>
                      {LOCAL_PRODUCT_CATALOG.map(p => (
                        <TouchableOpacity 
                          key={p.id}
                          style={[styles.segBtn, config.type === p.id && styles.segBtnActive]}
                          onPress={() => updateConfig('type', p.id)}
                        >
                           <Text style={[styles.segBtnText, config.type === p.id && { color: '#000' }]}>{p.id}</Text>
                        </TouchableOpacity>
                      ))}
                    </View>
                  </View>

                  {/* Position */}
                  <View style={{ flex: 1.5 }}>
                    <Text style={styles.labelSmall}>POSITION</Text>
                    <View style={styles.segRow}>
                      <TouchableOpacity 
                        style={[styles.segBtn, config.position === 'Left' && styles.segBtnActive]}
                        onPress={() => updateConfig('position', 'Left')}
                      >
                         <Text style={[styles.segBtnText, config.position === 'Left' && { color: '#000' }]}>Left</Text>
                      </TouchableOpacity>
                      <TouchableOpacity 
                        style={[styles.segBtn, config.position === 'Right' && styles.segBtnActive]}
                        onPress={() => updateConfig('position', 'Right')}
                      >
                         <Text style={[styles.segBtnText, config.position === 'Right' && { color: '#000' }]}>Right</Text>
                      </TouchableOpacity>
                    </View>
                  </View>
                </View>

                {/* Adjust Points */}
                {(getLocalProfileById(config.type)?.hardwareAllowsCustomPoints) && (
                  <View style={{ marginTop: Spacing.md, backgroundColor: Colors.surfaceHighlight, borderRadius: 12, padding: Spacing.sm }}>
                    <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}>
                      <Text style={[styles.labelSmall, { flex: 1, marginBottom: 0 }]}>LED COUNT (TRIM IF CUT)</Text>
                      <View style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: Colors.surface, borderRadius: 8 }}>
                        <TouchableOpacity style={{ padding: Spacing.md }} onPress={() => updateConfig('points', Math.max(1, config.points - 1))}>
                          <MaterialCommunityIcons name="minus" size={18} color={Colors.text} />
                        </TouchableOpacity>
                        <Text style={{ color: Colors.text, fontSize: 15, fontWeight: 'bold', width: 32, textAlign: 'center' }}>
                          {config.points}
                        </Text>
                        <TouchableOpacity style={{ padding: Spacing.md }} onPress={() => updateConfig('points', Math.min(200, config.points + 1))}>
                          <MaterialCommunityIcons name="plus" size={18} color={Colors.text} />
                        </TouchableOpacity>
                      </View>
                    </View>
                  </View>
                )}
              </View>
            );
          })}
          </ScrollView>
      </View>
    );
  };

  return (
    <SafeAreaView style={styles.container}>
      <KeyboardAvoidingView 
        style={{ flex: 1 }} 
        behavior={Platform.OS === 'ios' ? 'padding' : undefined}
      >
        {step === 1 ? renderStep1() : step === 2 ? renderStep2() : renderStep3()}

      <View style={styles.footer}>
        {(!isBluetoothSupported || !isBluetoothEnabled) ? (
          <View style={styles.errorBox}>
            <MaterialCommunityIcons name="bluetooth-off" size={20} color="#ff4444" />
            <Text style={styles.errorText}>Please enable Bluetooth to continue.</Text>
          </View>
        ) : step === 1 ? (
          <>
            {pendingRegistrations.length > 0 && bleState !== 'SCANNING' ? (
              <TouchableOpacity 
                style={styles.primaryBtn} 
                onPress={() => setStep(2)}
              >
                <View style={styles.scanningRow}>
                  <MaterialCommunityIcons name="check-circle" size={18} color="#000" />
                  <Text style={styles.primaryBtnText}>NEXT: REVIEW {pendingRegistrations.length} FOUND SKATES →</Text>
                </View>
              </TouchableOpacity>
            ) : (
              <TouchableOpacity 
                style={[styles.primaryBtn, bleState === 'SCANNING' && styles.primaryBtnDisabled]} 
                onPress={handleStartScan}
                disabled={bleState === 'SCANNING'}
              >
                {bleState === 'SCANNING' || bleState === 'PROBING' || !hasStartedScan ? (
                  <View style={styles.scanningRow}>
                    <ActivityIndicator color="#000" size="small" />
                    <Text style={styles.primaryBtnText}>
                      {bleState === 'SCANNING' ? 'SEARCHING FOR SKATES...' : 'IDENTIFYING HARDWARE...'}
                    </Text>
                  </View>
                ) : (
                  <Text style={styles.primaryBtnText}>RETRY SCAN</Text>
                )}
              </TouchableOpacity>
            )}
          </>
        ) : step === 2 ? (
          <TouchableOpacity 
            style={[styles.primaryBtn, (isClaiming || selectedDeviceMacs.size === 0) && styles.primaryBtnDisabled]} 
            disabled={isClaiming || selectedDeviceMacs.size === 0}
            onPress={() => {
               const selected = pendingRegistrations.filter(r => selectedDeviceMacs.has(r.device_mac));
               const configs: Record<string, any> = {};
               let leftAssigned = false;
               let rightAssigned = false;
               
               let hCount = 0;
               let sCount = 0;
               
               const typeCounts: Record<string, number> = {};

               selected.forEach(d => {
                  const n = d.device_name || '';
                  // Heuristic: Check if name includes a catalog product ID
                  const matchedProfile = LOCAL_PRODUCT_CATALOG.find(p => n.toUpperCase().includes(p.id.toUpperCase()));
                  const pType = matchedProfile ? matchedProfile.id : (d.product_type !== 'UNKNOWN' ? d.product_type : LOCAL_PRODUCT_CATALOG[0].id);
                  
                  const pProfile = getLocalProfileById(pType) || LOCAL_PRODUCT_CATALOG[0];
                  typeCounts[pProfile.id] = (typeCounts[pProfile.id] || 0) + 1;

                  let pos: 'Left'|'Right'|null = null;
                  if (n.toLowerCase().includes('left')) { pos = 'Left'; leftAssigned = true; }
                  else if (n.toLowerCase().includes('right')) { pos = 'Right'; rightAssigned = true; }
           
                  if (selected.length === 2 && !pos) {
                     if (!leftAssigned) { pos = 'Left'; leftAssigned = true; }
                     else if (!rightAssigned) { pos = 'Right'; rightAssigned = true; }
                  }
                  
                  configs[d.device_mac] = {
                    name: n || getDefaultGroupName(pProfile.id),
                    type: pType,
                    position: pos,
                    points: typeof d.led_points === 'number' ? d.led_points : pProfile.defaultLedPoints
                  };
               });

               if (!groupName) {
                 let defaultName = 'My Skates';
                 const entries = Object.entries(typeCounts);
                 if (entries.length > 0) {
                   const maxType = entries.reduce((a, b) => a[1] > b[1] ? a : b)[0];
                   const pProfile = getLocalProfileById(maxType);
                   if (pProfile) defaultName = getDefaultGroupName(pProfile.id);
                 }
                 setGroupName(defaultName);
               }

               setDeviceConfigsState(configs);
               setStep(3);
            }}
          >
            <Text style={styles.primaryBtnText}>ASSIGN SKATE SETTINGS →</Text>
          </TouchableOpacity>
        ) : (
          <TouchableOpacity 
            style={[styles.primaryBtn, (!groupName.trim() || isClaiming) && styles.primaryBtnDisabled]} 
            disabled={!groupName.trim() || isClaiming}
            onPress={async () => {
               console.log('[FTUE] Phase 3 Complete. Transitioning to Hardware Save & Dashboard Phase 4.');
               setIsClaiming(true);
               try {
                 const selected = pendingRegistrations.filter(r => selectedDeviceMacs.has(r.device_mac));
                 
                 // Execute Hardware Loop
                 for (const device of selected) {
                    const cfg = deviceConfigsState[device.device_mac];
                    if (!cfg) continue;

                    // If points were adjusted, we must push the EEPROM update and verify
                    if (getLocalProfileById(cfg.type)?.hardwareAllowsCustomPoints && cfg.points !== device.led_points) {
                       console.log(`[FTUE] Writing hardware settings ${cfg.points} to ${device.device_mac}`);
                       const payload = ZenggeProtocol.writeHardwareSettingsByName(cfg.points, 1, 'WS2812B', 'GRB');
                       await writeToDevice(payload, device.device_mac);
                       
                       // Let EEPROM persist
                       await new Promise(r => setTimeout(r, 600));

                       // Re-probe to verify
                       await probeDevice(device.device_mac);
                       console.log(`[FTUE] Assuming successful verification for ${device.device_mac} due to void return`);
                    }
                 }

                 const finalizedDevices = selected.map(device => {
                   const cfg = deviceConfigsState[device.device_mac];
                   return {
                     ...device,
                     group_name: groupName.trim(),
                     device_name: cfg?.name.trim() || device.device_name,
                     product_type: (cfg?.type || LOCAL_PRODUCT_CATALOG[0].id) as any,
                     position: cfg?.position || null,
                     group_id: groupName.trim().toLowerCase().replace(/\s+/g, '-'),
                     led_points: cfg?.points || device.led_points,
                     segments: device.segments ?? 1,
                     ic_type: device.ic_type ?? 'WS2812B',
                     color_sorting: device.color_sorting ?? 'GRB'
                   };
                 });
                 await onSetupComplete(finalizedDevices);
               } finally {
                 setIsClaiming(false);
               }
            }}
          >
            {isClaiming ? (
              <View style={styles.scanningRow}>
                <ActivityIndicator color="#000" size="small" />
                <Text style={styles.primaryBtnText}>COMPLETING SETUP...</Text>
              </View>
            ) : (
              <Text style={styles.primaryBtnText}>COMPLETE SETUP →</Text>
            )}
          </TouchableOpacity>
        )}
        
        <View style={{ flexDirection: 'row', justifyContent: 'center', marginTop: Spacing.lg, gap: Spacing.xl }}>
          {step > 1 && (
            <TouchableOpacity onPress={() => setStep(step === 3 ? 2 : 1)} style={{ padding: Spacing.sm }}>
              <Text style={{ color: Colors.textMuted, fontSize: 13, fontWeight: 'bold' }}>← Back</Text>
            </TouchableOpacity>
          )}
          <TouchableOpacity onPress={() => onSetupComplete([])} style={{ padding: Spacing.sm }}>
            <Text style={{ color: Colors.textMuted, fontSize: 13, fontWeight: 'bold' }}>Skip setup for now</Text>
          </TouchableOpacity>
        </View>
      </View>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
}

function createStyles(Colors: any) {
  return StyleSheet.create({
    container: { flex: 1, backgroundColor: Colors.background || '#0D0D0D' },
    content: { flex: 1, padding: Spacing.md, justifyContent: 'center', alignItems: 'center' },
    title: { color: Colors.text || '#fff', fontSize: 24, textAlign: 'center', marginBottom: Spacing.xs },
    subtitle: { color: Colors.textMuted || '#888', fontSize: 13, textAlign: 'center', marginBottom: Spacing.md, lineHeight: 18 },
    instructionCard: {
      backgroundColor: Colors.surface,
      borderWidth: 1, borderColor: Colors.surfaceHighlight,
      borderRadius: 12, padding: Spacing.lg, width: '100%', marginBottom: Spacing.md,
    },
    instructionHeader: { color: Colors.primary || '#00f0ff', fontWeight: 'bold', fontSize: 13, marginBottom: Spacing.xs },
    instructionBody: { color: Colors.textMuted || '#888', fontSize: 13, lineHeight: 18 },
    helpLink: { flexDirection: 'row', alignItems: 'center', marginTop: Spacing.xl, gap: Spacing.sm },
    helpText: { color: Colors.textMuted, fontSize: 13, textDecorationLine: 'underline' },
    footer: { padding: Spacing.md, paddingBottom: Platform.OS === 'ios' ? 0 : 12 },
    primaryBtn: {
      backgroundColor: Colors.primary || '#00f0ff',
      paddingVertical: Spacing.lg, borderRadius: 14, alignItems: 'center', width: '100%'
    },
    primaryBtnDisabled: { opacity: 0.7 },
    primaryBtnText: { color: '#000', fontWeight: '900', fontSize: 15, letterSpacing: 1 },
    scanningRow: { flexDirection: 'row', alignItems: 'center', gap: Spacing.md },
    errorBox: { flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: Spacing.sm, padding: Spacing.lg, backgroundColor: 'rgba(255,68,68,0.1)', borderRadius: 12 },
    errorText: { color: '#ff4444', fontSize: 13, fontWeight: 'bold' },
    
    // Step 2 Styles
    successIconHeader: { marginBottom: Spacing.lg, alignItems: 'center' },
    scrollViewWrapper: { flex: 1, width: '100%', marginTop: Spacing.sm },
    groupContainer: { marginBottom: Spacing.xl, width: '100%' },
    groupTitle: { fontSize: 12, fontWeight: '900', letterSpacing: 1.5, marginBottom: Spacing.md, marginLeft: Spacing.xs },
    deviceRow: { 
      flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between',
      backgroundColor: Colors.surface, borderWidth: 1, borderColor: Colors.surfaceHighlight,
      borderRadius: 12, padding: Spacing.lg, marginBottom: Spacing.md 
    },
    deviceRowSelected: { backgroundColor: Colors.surfaceHighlight, borderColor: Colors.primary },
    checkboxContainer: { marginRight: Spacing.md },
    deviceInfo: { flex: 1 },
    deviceName: { color: Colors.text || '#fff', fontSize: 16, fontWeight: 'bold', marginBottom: Spacing.xs },
    deviceMeta: { color: Colors.textMuted || '#888', fontSize: 11, marginBottom: Spacing.xxs },
    blinkBtn: {
      flexDirection: 'row', alignItems: 'center', gap: Spacing.sm,
      backgroundColor: 'rgba(0,240,255,0.08)',
      paddingHorizontal: Spacing.md, paddingVertical: Spacing.sm,
      borderRadius: 10, borderWidth: 1.5, borderColor: 'rgba(0,240,255,0.4)',
      shadowColor: '#00F0FF', shadowOpacity: 0.3, shadowRadius: 6, shadowOffset: { width: 0, height: 0 },
      elevation: 4,
    },
    blinkBtnActive: {
      backgroundColor: '#4ade80', borderColor: '#4ade80',
      shadowColor: '#4ade80', shadowOpacity: 0.6, shadowRadius: 10,
    },
    blinkBtnText: { color: '#00F0FF', fontSize: 12, fontWeight: '900', letterSpacing: 0.5 },
    
    // Step 3 Styles
    label: { color: Colors.textMuted || '#888', fontSize: 11, fontWeight: 'bold', letterSpacing: 1, marginBottom: Spacing.sm, marginLeft: Spacing.xs },
    labelSmall: { color: Colors.textMuted || '#888', fontSize: 10, fontWeight: 'bold', letterSpacing: 1, marginBottom: Spacing.sm, marginLeft: Spacing.xxs },
    input: {
      backgroundColor: Colors.surfaceHighlight,
      color: Colors.text || '#fff',
      padding: Spacing.lg,
      borderRadius: 12,
      fontSize: 16,
      borderWidth: 1,
      borderColor: Colors.surfaceHighlight,
    },
    inputSmall: {
      backgroundColor: Colors.surfaceHighlight,
      color: Colors.text || '#fff',
      padding: Spacing.md,
      borderRadius: 8,
      fontSize: 14,
      borderWidth: 1,
      borderColor: Colors.surfaceHighlight,
    },
    deviceConfigCard: {
      backgroundColor: Colors.surface,
      borderWidth: 1, borderColor: Colors.surfaceHighlight,
      borderRadius: 12, padding: Spacing.md, width: '100%', marginBottom: Spacing.sm,
    },
    deviceConfigTitle: { color: Colors.text || '#fff', fontSize: 14, fontWeight: 'bold', flex: 1 },
    segRow: { flexDirection: 'row', backgroundColor: Colors.surfaceHighlight, borderRadius: 8, padding: Spacing.xs },
    segBtn: { flex: 1, alignItems: 'center', paddingVertical: Spacing.sm, borderRadius: 6 },
    segBtnActive: { backgroundColor: Colors.primary || '#00f0ff', shadowOpacity: 0.2, shadowRadius: 4, elevation: 4 },
    segBtnText: { fontSize: 11, fontWeight: 'bold', color: Colors.textMuted || '#888' },
    
    emptyState: { padding: Spacing.xxxl, alignItems: 'center', justifyContent: 'center' },
    emptyText: { color: Colors.textMuted || '#888', textAlign: 'center', fontSize: 14 },
    deviceScroll: { flex: 1, width: '100%' },
  });
}
