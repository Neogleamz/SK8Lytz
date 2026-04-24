import React from 'react';
import { ScrollView, Text, TouchableOpacity, View, Platform } from 'react-native';
import { Spacing } from '../../../../theme/theme';
import { useDiagnosticLabStyles } from './DiagnosticLabStyles';

interface DevicesTabProps {
  bleState: string;
  handleScan?: () => void;
  allDevices: any[];
  connectedDevices: any[];
  liveDeviceConfigs: any;
  registeredDevices: any[];
  targetDeviceId: string | null;
  setTargetDeviceId: (id: string | null) => void;
  liveRxPayload?: any;
  connectToDevice?: (device: any) => void;
  hwSettings: any;
}

export function DiagnosticLabDevicesTab({
  bleState,
  handleScan,
  allDevices,
  connectedDevices,
  liveDeviceConfigs,
  registeredDevices,
  targetDeviceId,
  setTargetDeviceId,
  connectToDevice,
  hwSettings,
}: DevicesTabProps) {
  const { S, txtPri, txtMuted, cyan, border } = useDiagnosticLabStyles();

  return (
    <ScrollView contentContainerStyle={{ paddingBottom: Spacing.xxxl }}>
      <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: Spacing.xl }}>
        <Text style={[S.sectionTitle, { color: txtPri }]}>HARDWARE SCANNER</Text>
        <TouchableOpacity 
          style={{ backgroundColor: bleState === 'SCANNING' ? border : cyan, paddingHorizontal: Spacing.lg, paddingVertical: Spacing.md, borderRadius: 8, borderWidth: 1, borderColor: bleState === 'SCANNING' ? border : cyan }} 
          onPress={() => { if (handleScan) handleScan(); }}
          disabled={bleState === 'SCANNING' || bleState === 'PROBING'}
        >
          <Text style={{ color: bleState === 'SCANNING' ? txtMuted : '#000', fontWeight: '900', fontSize: 11, letterSpacing: 0.5 }}>
            {bleState === 'SCANNING' ? 'SCANNING...' : bleState === 'PROBING' ? 'PROBING...' : 'START SCAN'}
          </Text>
        </TouchableOpacity>
      </View>
      
      {(!allDevices || allDevices.length === 0) && (
        <Text style={S.hint}>No devices found. Tap START SCAN to begin.</Text>
      )}

      {allDevices?.map((d: any, idx) => {
        const cfg = { ...(liveDeviceConfigs?.[d.id] || {}) };
        const points   = cfg.points    ?? d.points    ?? null;
        const segments = cfg.segments  ?? d.segments  ?? null;
        const sorting  = cfg.sorting   ?? d.sorting   ?? cfg.colorSortingName ?? null;
        const stripType= cfg.stripType ?? d.stripType ?? cfg.icName           ?? null;
        const isConn   = connectedDevices?.some((c: any) => c.id === d.id);
        const isTarget = targetDeviceId === d.id;

        return (
          <View key={d.id || idx} style={[S.diagBox, isTarget && { borderColor: cyan, borderWidth: 1.5 }]}>
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: Spacing.sm }}>
              <View style={{ flex: 1 }}>
                <Text style={{ color: txtPri, fontWeight: 'bold', fontSize: 13 }} numberOfLines={1}>
                  {cfg.name || d.name || 'Unknown Device'}
                </Text>
                {registeredDevices.some(rd => rd.device_mac === d.id) ? (
                  <Text style={{ color: '#FFD700', fontSize: 10, fontWeight: '700', marginTop: Spacing.xxs }}>★ REGISTERED TO YOU</Text>
                ) : (d.owner_ids && d.owner_ids.length > 0) ? (
                  <Text style={{ color: '#FF4040', fontSize: 10, fontWeight: '700', marginTop: Spacing.xxs }}>
                    🔒 CLAIMED BY: {d.owner_ids.length > 1 ? `${d.owner_ids.length} USERS` : `${d.owner_ids[0].substring(0,8)}...`}
                  </Text>
                ) : (
                  <Text style={{ color: txtMuted, fontSize: 10, fontStyle: 'italic', marginTop: Spacing.xxs }}>Unregistered (Telemetry Active)</Text>
                )}
              </View>
              {isConn
                ? (
                  <View style={{ flexDirection: 'row', gap: Spacing.sm }}>
                    <Text style={{ color: '#00E676', fontSize: 10, fontWeight: '900', alignSelf: 'center' }}>● LIVE</Text>
                    <TouchableOpacity 
                      onPress={() => setTargetDeviceId(d.id)} 
                      style={{ backgroundColor: isTarget ? cyan : border, paddingHorizontal: Spacing.md, paddingVertical: Spacing.sm, borderRadius: 6 }}
                    >
                      <Text style={{ color: isTarget ? '#000' : txtPri, fontSize: 10, fontWeight: 'bold' }}>
                        {isTarget ? 'TARGETED' : 'TARGET'}
                      </Text>
                    </TouchableOpacity>
                  </View>
                )
                : (
                  <TouchableOpacity 
                    onPress={() => connectToDevice && connectToDevice(d)} 
                    style={{ backgroundColor: '#9D4EFF', paddingHorizontal: Spacing.md, paddingVertical: Spacing.sm, borderRadius: 6 }}
                  >
                    <Text style={{ color: '#FFF', fontSize: 10, fontWeight: 'bold' }}>CONNECT</Text>
                  </TouchableOpacity>
                )
              }
            </View>
            <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.md, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>
              {d.id}
            </Text>
            
            <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.md }}>
              <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                <Text style={{ color: txtMuted, fontSize: 10 }}>LEDs </Text>
                <Text style={{ color: cyan, fontSize: 10, fontWeight: 'bold' }}>{points ?? '?'}</Text>
              </View>
              <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                <Text style={{ color: txtMuted, fontSize: 10 }}>SEG </Text>
                <Text style={{ color: cyan, fontSize: 10, fontWeight: 'bold' }}>{segments ?? '?'}</Text>
              </View>
              <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                <Text style={{ color: txtMuted, fontSize: 10 }}>SORT </Text>
                <Text style={{ color: '#FF69B4', fontSize: 10, fontWeight: 'bold' }}>{sorting ?? '?'}</Text>
              </View>
              <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                <Text style={{ color: txtMuted, fontSize: 10 }}>IC </Text>
                <Text style={{ color: '#FFD700', fontSize: 10, fontWeight: 'bold' }}>{stripType ?? '?'}</Text>
              </View>
            </View>

            {isTarget && hwSettings?.detected && (
              <View style={{ marginTop: Spacing.md, paddingTop: Spacing.md, borderTopWidth: 1, borderTopColor: border }}>
                <Text style={{ color: '#00CC88', fontSize: 10, fontWeight: 'bold' }}>
                  ✓ Hardware settings populated from active probe.
                </Text>
              </View>
            )}
          </View>
        );
      })}
    </ScrollView>
  );
}
