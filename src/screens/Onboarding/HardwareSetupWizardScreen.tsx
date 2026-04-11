import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ActivityIndicator, SafeAreaView, Linking, Platform, ScrollView, KeyboardAvoidingView, TextInput } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../../context/ThemeContext';
import { Typography } from '../../theme/theme';
import useBLE from '../../hooks/useBLE';
import { ZenggeProtocol } from '../../protocols/ZenggeProtocol';

import { RegisteredDevice } from '../../hooks/useRegistration';
import { LOCAL_PRODUCT_CATALOG, getLocalProfileById } from '../../constants/ProductCatalog';

interface HardwareSetupWizardScreenProps {
  onSetupComplete: (devices: RegisteredDevice[]) => Promise<void> | void;
}

export default function HardwareSetupWizardScreen({ onSetupComplete }: HardwareSetupWizardScreenProps) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const { scanForPeripherals, isScanning, isScanProbing, requestPermissions, isBluetoothSupported, isBluetoothEnabled, pendingRegistrations, writeToDevice, probeDevice } = useBLE();
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
  }, [pendingRegistrations, hasStartedScan, isScanning]);

  useEffect(() => {
    if (!hasStartedScan && isBluetoothSupported && isBluetoothEnabled) {
      handleStartScan();
    }
  }, [hasStartedScan, isBluetoothSupported, isBluetoothEnabled]);

  const handleStartScan = async () => {
    const granted = await requestPermissions();
    if (granted && !isScanning) {
      setHasStartedScan(true);
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
      const productType = (registration?.product_type && registration.product_type !== 'UNKNOWN') ? registration.product_type : 'HALOZ';
      const profile = LOCAL_PRODUCT_CATALOG.find(p => p.id === productType) || LOCAL_PRODUCT_CATALOG[0];
      const blinkPoints = registration?.led_points || profile.vizDefaultPoints;

      // 0x59 static multi-color mode: Green. 
      const colorArray = Array(blinkPoints).fill({ r: 0, g: 255, b: 0 });
      const blinkPayload = ZenggeProtocol.setMultiColor(colorArray, 1, 1, 0x00); 
      await writeToDevice(blinkPayload, deviceMac);
      
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
                    <Text style={styles.deviceMeta}>MAC: {device.device_mac.slice(-5).toUpperCase()} • RSSI: {device.rssi}</Text>
                    <Text style={styles.deviceMeta}>
                      {device.product_type === 'UNKNOWN' ? 'IDENTIFYING HARDWARE...' : `LEDS: ${device.led_points} • IC: ${device.ic_type}`}
                    </Text>
                  </View>
                <TouchableOpacity 
                  style={[styles.blinkBtn, isBlinking === device.device_mac && styles.blinkBtnActive]}
                  onPress={() => handleBlinkDevice(device.device_mac)}
                  disabled={isBlinking !== null}
                >
                  <MaterialCommunityIcons 
                    name="shield-sun" 
                    size={18} 
                    color={isBlinking === device.device_mac ? '#000' : Colors.text} 
                  />
                  <Text style={[styles.blinkBtnText, isBlinking === device.device_mac && { color: '#000' }]}>
                    {isBlinking === device.device_mac ? 'BLINKING' : 'BLINK'}
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

        <ScrollView style={styles.deviceScroll} contentContainerStyle={{ paddingBottom: 20 }}>
          {deviceGroups.map((group) => renderDeviceGroup(`${group.profile.id}`, group.devices, group.profile.vizThemeColor || '#00f0ff'))}
          {renderDeviceGroup('SCANNING / UNKNOWN', unknown, Colors.textMuted)}
          
          {pendingRegistrations.length === 0 && !isScanning && !isScanProbing && (
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
        <Text style={[Typography.title, styles.title, { marginBottom: 4 }]}>Name Your Skates</Text>
        <Text style={[styles.subtitle, { marginBottom: 12 }]}>
          Assign them to a Fleet and designate left or right.
        </Text>

        <View style={styles.scrollViewWrapper}>
          {/* Group */}
          <Text style={styles.label}>FLEET / GROUP NAME</Text>
          <TextInput 
            style={[styles.input, { paddingVertical: 10, paddingHorizontal: 12 }]} 
            value={groupName} 
            onChangeText={setGroupName}
            placeholder="e.g. My Skates" 
            placeholderTextColor={Colors.textMuted} 
            maxLength={32} 
          />

          {selected.map((device, idx) => {
            const config = deviceConfigsState[device.device_mac] || { name: '', type: LOCAL_PRODUCT_CATALOG[0].id, position: null, points: LOCAL_PRODUCT_CATALOG[0].defaultLedPoints };
            
            const updateConfig = (key: string, val: any) => {
               setDeviceConfigsState(prev => ({
                 ...prev,
                 [device.device_mac]: { ...prev[device.device_mac], [key]: val }
               }));
            };

            return (
              <View key={device.device_mac} style={styles.deviceConfigCard}>
                <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 8, justifyContent: 'space-between' }}>
                  <MaterialCommunityIcons name="bluetooth" size={18} color={Colors.primary} style={{ marginRight: 8 }} />
                  <Text style={styles.deviceConfigTitle}>Network ID: {device.device_mac.slice(-5)}</Text>
                  <TouchableOpacity 
                    style={[styles.blinkBtn, isBlinking === device.device_mac && styles.blinkBtnActive, { paddingVertical: 4, paddingHorizontal: 8 }]}
                    onPress={() => handleBlinkDevice(device.device_mac)}
                    disabled={isBlinking !== null}
                  >
                    <MaterialCommunityIcons 
                      name="shield-sun" 
                      size={14} 
                      color={isBlinking === device.device_mac ? '#000' : Colors.text} 
                    />
                    <Text style={[styles.blinkBtnText, isBlinking === device.device_mac && { color: '#000' }]}>
                      {isBlinking === device.device_mac ? 'BLINKING' : 'BLINK'}
                    </Text>
                  </TouchableOpacity>
                </View>

                {/* Name */}
                <Text style={styles.labelSmall}>DEVICE NAME</Text>
                <TextInput 
                  style={[styles.inputSmall, { paddingVertical: 8 }]} 
                  value={config.name} 
                  onChangeText={(t) => updateConfig('name', t)}
                  placeholder="e.g. Left Skate" 
                  placeholderTextColor={Colors.textMuted} 
                  maxLength={32} 
                />

                <View style={{ flexDirection: 'row', gap: 12, marginTop: 8 }}>
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

                {/* Adjust Points (only for non-RING products, e.g. SOULZ, RAILZ — not HALOZ) */}
                {(getLocalProfileById(config.type)?.vizShape !== 'RING') && (
                  <View style={{ marginTop: 12, backgroundColor: Colors.surfaceHighlight, borderRadius: 12, padding: 8 }}>
                    <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}>
                      <Text style={[styles.labelSmall, { flex: 1, marginBottom: 0 }]}>LED COUNT (TRIM IF CUT)</Text>
                      <View style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: Colors.surface, borderRadius: 8 }}>
                        <TouchableOpacity style={{ padding: 10 }} onPress={() => updateConfig('points', Math.max(1, config.points - 1))}>
                          <MaterialCommunityIcons name="minus" size={18} color={Colors.text} />
                        </TouchableOpacity>
                        <Text style={{ color: Colors.text, fontSize: 15, fontWeight: 'bold', width: 32, textAlign: 'center' }}>
                          {config.points}
                        </Text>
                        <TouchableOpacity style={{ padding: 10 }} onPress={() => updateConfig('points', Math.min(200, config.points + 1))}>
                          <MaterialCommunityIcons name="plus" size={18} color={Colors.text} />
                        </TouchableOpacity>
                      </View>
                    </View>
                  </View>
                )}
              </View>
            );
          })}
          </View>
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
            {pendingRegistrations.length > 0 && !isScanning ? (
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
                style={[styles.primaryBtn, isScanning && styles.primaryBtnDisabled]} 
                onPress={handleStartScan}
                disabled={isScanning}
              >
                {isScanning || isScanProbing || !hasStartedScan ? (
                  <View style={styles.scanningRow}>
                    <ActivityIndicator color="#000" size="small" />
                    <Text style={styles.primaryBtnText}>
                      {isScanning ? 'SEARCHING FOR SKATES...' : 'IDENTIFYING HARDWARE...'}
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

               selected.forEach(d => {
                  const n = d.device_name || '';
                  // Heuristic: Check if name includes a catalog product ID
                  const matchedProfile = LOCAL_PRODUCT_CATALOG.find(p => n.toUpperCase().includes(p.id.toUpperCase()));
                  let pType = (matchedProfile ? matchedProfile.id : (d.product_type !== 'UNKNOWN' ? d.product_type : 'SOULZ')) as 'HALOZ' | 'SOULZ' | 'RAILZ';
                  
                  const pProfile = LOCAL_PRODUCT_CATALOG.find(p => p.id === pType) || LOCAL_PRODUCT_CATALOG[0];
                  if (pType === 'HALOZ') hCount++;
                  if (pType === 'SOULZ') sCount++;

                  let pos: 'Left'|'Right'|null = null;
                  if (n.toLowerCase().includes('left')) { pos = 'Left'; leftAssigned = true; }
                  else if (n.toLowerCase().includes('right')) { pos = 'Right'; rightAssigned = true; }
           
                  if (selected.length === 2 && !pos) {
                     if (!leftAssigned) { pos = 'Left'; leftAssigned = true; }
                     else if (!rightAssigned) { pos = 'Right'; rightAssigned = true; }
                  }
                  
                  configs[d.device_mac] = {
                    name: n || `My SK8Lytz ${pType}`,
                    type: pType,
                    position: pos,
                    points: typeof d.led_points === 'number' ? d.led_points : pProfile.defaultLedPoints
                  };
               });

               if (!groupName) {
                 let defaultName = 'My Skates';
                 if (hCount > 0 && sCount === 0) defaultName = 'My SK8Lytz HALOZ';
                 if (sCount > 0 && hCount === 0) defaultName = 'My SK8Lytz SOULZ';
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

                    // If it's SOULZ and points were adjusted, we must push the EEPROM update and verify
                    if (cfg.type === 'SOULZ' && cfg.points !== device.led_points) {
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
                     group_id: '',
                     led_points: cfg?.points || device.led_points,
                     segments: 1,
                     ic_type: 'WS2812B',
                     color_sorting: 'GRB'
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
        
        <View style={{ flexDirection: 'row', justifyContent: 'center', marginTop: 16, gap: 24 }}>
          {step > 1 && (
            <TouchableOpacity onPress={() => setStep(step === 3 ? 2 : 1)} style={{ padding: 8 }}>
              <Text style={{ color: Colors.textMuted, fontSize: 13, fontWeight: 'bold' }}>← Back</Text>
            </TouchableOpacity>
          )}
          <TouchableOpacity onPress={() => onSetupComplete([])} style={{ padding: 8 }}>
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
    content: { flex: 1, padding: 12, justifyContent: 'center', alignItems: 'center' },
    title: { color: Colors.text || '#fff', fontSize: 24, textAlign: 'center', marginBottom: 4 },
    subtitle: { color: Colors.textMuted || '#888', fontSize: 13, textAlign: 'center', marginBottom: 12, lineHeight: 18 },
    instructionCard: {
      backgroundColor: Colors.surface,
      borderWidth: 1, borderColor: Colors.surfaceHighlight,
      borderRadius: 12, padding: 16, width: '100%', marginBottom: 12,
    },
    instructionHeader: { color: Colors.primary || '#00f0ff', fontWeight: 'bold', fontSize: 13, marginBottom: 4 },
    instructionBody: { color: Colors.textMuted || '#888', fontSize: 13, lineHeight: 18 },
    helpLink: { flexDirection: 'row', alignItems: 'center', marginTop: 24, gap: 6 },
    helpText: { color: Colors.textMuted, fontSize: 13, textDecorationLine: 'underline' },
    footer: { padding: 12, paddingBottom: Platform.OS === 'ios' ? 0 : 12 },
    primaryBtn: {
      backgroundColor: Colors.primary || '#00f0ff',
      paddingVertical: 16, borderRadius: 14, alignItems: 'center', width: '100%'
    },
    primaryBtnDisabled: { opacity: 0.7 },
    primaryBtnText: { color: '#000', fontWeight: '900', fontSize: 15, letterSpacing: 1 },
    scanningRow: { flexDirection: 'row', alignItems: 'center', gap: 10 },
    errorBox: { flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 8, padding: 16, backgroundColor: 'rgba(255,68,68,0.1)', borderRadius: 12 },
    errorText: { color: '#ff4444', fontSize: 13, fontWeight: 'bold' },
    
    // Step 2 Styles
    successIconHeader: { marginBottom: 16, alignItems: 'center' },
    scrollViewWrapper: { flex: 1, width: '100%', marginTop: 8 },
    groupContainer: { marginBottom: 24, width: '100%' },
    groupTitle: { fontSize: 12, fontWeight: '900', letterSpacing: 1.5, marginBottom: 12, marginLeft: 4 },
    deviceRow: { 
      flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between',
      backgroundColor: Colors.surface, borderWidth: 1, borderColor: Colors.surfaceHighlight,
      borderRadius: 12, padding: 14, marginBottom: 10 
    },
    deviceRowSelected: { backgroundColor: Colors.surfaceHighlight, borderColor: Colors.primary },
    checkboxContainer: { marginRight: 12 },
    deviceInfo: { flex: 1 },
    deviceName: { color: Colors.text || '#fff', fontSize: 16, fontWeight: 'bold', marginBottom: 4 },
    deviceMeta: { color: Colors.textMuted || '#888', fontSize: 11, marginBottom: 2 },
    blinkBtn: {
      flexDirection: 'row', alignItems: 'center', gap: 6,
      backgroundColor: Colors.surfaceHighlight, paddingHorizontal: 12, paddingVertical: 8,
      borderRadius: 8, borderWidth: 1, borderColor: Colors.surfaceHighlight
    },
    blinkBtnActive: { backgroundColor: '#4ade80', borderColor: '#4ade80' },
    blinkBtnText: { color: Colors.text || '#fff', fontSize: 12, fontWeight: '800' },
    
    // Step 3 Styles
    label: { color: Colors.textMuted || '#888', fontSize: 11, fontWeight: 'bold', letterSpacing: 1, marginBottom: 8, marginLeft: 4 },
    labelSmall: { color: Colors.textMuted || '#888', fontSize: 10, fontWeight: 'bold', letterSpacing: 1, marginBottom: 6, marginLeft: 2 },
    input: {
      backgroundColor: Colors.surfaceHighlight,
      color: Colors.text || '#fff',
      padding: 16,
      borderRadius: 12,
      fontSize: 16,
      borderWidth: 1,
      borderColor: Colors.surfaceHighlight,
    },
    inputSmall: {
      backgroundColor: Colors.surfaceHighlight,
      color: Colors.text || '#fff',
      padding: 12,
      borderRadius: 8,
      fontSize: 14,
      borderWidth: 1,
      borderColor: Colors.surfaceHighlight,
    },
    deviceConfigCard: {
      backgroundColor: Colors.surface,
      borderWidth: 1, borderColor: Colors.surfaceHighlight,
      borderRadius: 12, padding: 10, width: '100%', marginBottom: 8,
    },
    deviceConfigTitle: { color: Colors.text || '#fff', fontSize: 14, fontWeight: 'bold', flex: 1 },
    segRow: { flexDirection: 'row', backgroundColor: Colors.surfaceHighlight, borderRadius: 8, padding: 4 },
    segBtn: { flex: 1, alignItems: 'center', paddingVertical: 8, borderRadius: 6 },
    segBtnActive: { backgroundColor: Colors.primary || '#00f0ff', shadowOpacity: 0.2, shadowRadius: 4, elevation: 4 },
    segBtnText: { fontSize: 11, fontWeight: 'bold', color: Colors.textMuted || '#888' },
    
    emptyState: { padding: 40, alignItems: 'center', justifyContent: 'center' },
    emptyText: { color: Colors.textMuted || '#888', textAlign: 'center', fontSize: 14 },
    deviceScroll: { flex: 1, width: '100%' },
  });
}
