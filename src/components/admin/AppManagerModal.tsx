import React from 'react';
import { View, Text, Switch, TouchableOpacity, ScrollView, Modal, SafeAreaView, Alert } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Spacing } from '../../theme/theme';
import { adminStyles as styles } from './adminStyles';

export interface AppManagerModalProps {
  visible: boolean;
  onClose: () => void;
  appSettings: Record<string, string | boolean>;
  handlePolicyToggle: (key: string, value: boolean, title: string, msg: string) => void;
  updateSetting: (key: string, value: string | boolean) => void;
  bg: string;
  cardBg: string;
  borderColor: string;
  textPrimary: string;
  textMuted: string;
}

export const AppManagerModal = React.memo(({
  visible, onClose, appSettings, handlePolicyToggle, updateSetting,
  bg, cardBg, borderColor, textPrimary, textMuted
}: AppManagerModalProps) => {
  return (
    <Modal visible={visible} animationType="slide" presentationStyle="fullScreen" onRequestClose={onClose}>
      <SafeAreaView style={{ flex: 1, backgroundColor: bg }}>
        <View style={{ flexDirection: 'row', alignItems: 'center', padding: Spacing.lg, borderBottomWidth: 1, borderBottomColor: borderColor, backgroundColor: cardBg }}>
          <TouchableOpacity onPress={onClose} style={{ marginRight: Spacing.lg, padding: Spacing.xs }}>
            <MaterialCommunityIcons name="arrow-left" size={24} color="#9D4EFF" />
          </TouchableOpacity>
          <View style={{ flex: 1 }}>
            <Text style={[styles.title, { color: textPrimary }]}>🛡️ APP MANAGER</Text>
            <Text style={{ color: textMuted, fontSize: 11, marginTop: Spacing.xxs }}>Master governance engine & policy overrides</Text>
          </View>
        </View>
        <ScrollView style={{ flex: 1, padding: Spacing.lg }}>
          <Text style={{ color: textMuted, fontSize: 12, marginBottom: Spacing.lg }}>
            These policies are globally enforced. Safety locks are required for critical changes.
          </Text>

          <Text style={{ color: textPrimary, fontWeight: 'bold', marginBottom: Spacing.sm, marginTop: Spacing.sm }}>Crew Hub Governance</Text>
          <View style={{ backgroundColor: cardBg, borderWidth: 1, borderColor: borderColor, borderRadius: 12, marginBottom: Spacing.lg, overflow: 'hidden' }}>
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', padding: Spacing.lg, borderBottomWidth: 1, borderBottomColor: borderColor }}>
              <View style={{ flex: 1, marginRight: Spacing.lg }}>
                <Text style={{ color: textPrimary, fontSize: 15, fontWeight: '700' }}>Global Lock</Text>
                <Text style={{ color: textMuted, fontSize: 12, marginTop: Spacing.xxs }}>Completely disable the Crew Hub for everyone.</Text>
              </View>
              <Switch
                value={appSettings['global_crew_hub_locked'] === true}
                onValueChange={(v) => handlePolicyToggle('global_crew_hub_locked', v, 'Lock Crew Hub?', 'This disables the entire social network. Proceed?')}
                trackColor={{ false: '#444', true: '#FF4444' }}
              />
            </View>
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', padding: Spacing.lg }}>
              <View style={{ flex: 1, marginRight: Spacing.lg }}>
                <Text style={{ color: textPrimary, fontSize: 15, fontWeight: '700' }}>Hide When Offline</Text>
                <Text style={{ color: textMuted, fontSize: 12, marginTop: Spacing.xxs }}>Hide the tab completely if the device drops connection.</Text>
              </View>
              <Switch
                value={appSettings['offline_crew_hub_hidden'] === true}
                onValueChange={(v) => updateSetting('offline_crew_hub_hidden', v)}
                trackColor={{ false: '#444', true: '#00f0ff' }}
              />
            </View>
          </View>

          <Text style={{ color: textPrimary, fontWeight: 'bold', marginBottom: Spacing.sm }}>Community Hub Governance</Text>
          <View style={{ backgroundColor: cardBg, borderWidth: 1, borderColor: borderColor, borderRadius: 12, marginBottom: Spacing.lg, overflow: 'hidden' }}>
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', padding: Spacing.lg, borderBottomWidth: 1, borderBottomColor: borderColor }}>
              <View style={{ flex: 1, marginRight: Spacing.lg }}>
                <Text style={{ color: textPrimary, fontSize: 15, fontWeight: '700' }}>Global Lock</Text>
                <Text style={{ color: textMuted, fontSize: 12, marginTop: Spacing.xxs }}>Disable Community Picks and Favorites.</Text>
              </View>
              <Switch
                value={appSettings['global_community_hub_locked'] === true}
                onValueChange={(v) => handlePolicyToggle('global_community_hub_locked', v, 'Lock Community?', 'This disables global community picks. Proceed?')}
                trackColor={{ false: '#444', true: '#FF4444' }}
              />
            </View>
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', padding: Spacing.lg }}>
              <View style={{ flex: 1, marginRight: Spacing.lg }}>
                <Text style={{ color: textPrimary, fontSize: 15, fontWeight: '700' }}>Hide When Offline</Text>
                <Text style={{ color: textMuted, fontSize: 12, marginTop: Spacing.xxs }}>Remove access to cached Community picks.</Text>
              </View>
              <Switch
                value={appSettings['offline_community_hub_hidden'] === true}
                onValueChange={(v) => updateSetting('offline_community_hub_hidden', v)}
                trackColor={{ false: '#444', true: '#00f0ff' }}
              />
            </View>
          </View>

          <Text style={{ color: textPrimary, fontWeight: 'bold', marginBottom: Spacing.sm }}>Maps & Infrastructure</Text>
          <View style={{ backgroundColor: cardBg, borderWidth: 1, borderColor: borderColor, borderRadius: 12, marginBottom: Spacing.lg, overflow: 'hidden' }}>
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', padding: Spacing.lg, borderBottomWidth: 1, borderBottomColor: borderColor }}>
              <View style={{ flex: 1, marginRight: Spacing.lg }}>
                <Text style={{ color: textPrimary, fontSize: 15, fontWeight: '700' }}>Lock Skate Maps</Text>
                <Text style={{ color: textMuted, fontSize: 12, marginTop: Spacing.xxs }}>Disable all mapping functionality.</Text>
              </View>
              <Switch
                value={appSettings['global_maps_locked'] === true}
                onValueChange={(v) => handlePolicyToggle('global_maps_locked', v, 'Lock Skate Maps?', 'This disables location and map features. Proceed?')}
                trackColor={{ false: '#444', true: '#FF4444' }}
              />
            </View>
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', padding: Spacing.lg, borderBottomWidth: 1, borderBottomColor: borderColor }}>
              <View style={{ flex: 1, marginRight: Spacing.lg }}>
                <Text style={{ color: textPrimary, fontSize: 15, fontWeight: '700' }}>Hide When Offline</Text>
                <Text style={{ color: textMuted, fontSize: 12, marginTop: Spacing.xxs }}>Remove Maps access when offline.</Text>
              </View>
              <Switch
                value={appSettings['offline_maps_hidden'] === true}
                onValueChange={(v) => updateSetting('offline_maps_hidden', v)}
                trackColor={{ false: '#444', true: '#00f0ff' }}
              />
            </View>
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', padding: Spacing.lg }}>
              <View style={{ flex: 1, marginRight: Spacing.lg }}>
                <Text style={{ color: textPrimary, fontSize: 15, fontWeight: '700' }}>Telemetry Uploads</Text>
                <Text style={{ color: textMuted, fontSize: 12, marginTop: Spacing.xxs }}>Enable or disable crash telemetry ingest.</Text>
              </View>
              <Switch
                value={appSettings['global_telemetry_enabled'] === true}
                onValueChange={(v) => updateSetting('global_telemetry_enabled', v)}
                trackColor={{ false: '#444', true: '#00f0ff' }}
              />
            </View>
          </View>

          <Text style={{ color: textPrimary, fontWeight: 'bold', marginBottom: Spacing.sm }}>Legal Compliance</Text>
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: cardBg, borderWidth: 1, borderColor: borderColor, padding: Spacing.lg, borderRadius: 12, marginBottom: Spacing.xxl }}>
            <View style={{ flex: 1, marginRight: Spacing.lg }}>
              <Text style={{ color: textPrimary, fontSize: 15, fontWeight: '700' }}>Required EULA Version</Text>
              <Text style={{ color: textMuted, fontSize: 12, marginTop: Spacing.xxs }}>Current required target: v{appSettings['required_eula_version'] || '1'}. Users with lower versions will be gated.</Text>
            </View>
            <TouchableOpacity 
              style={{ backgroundColor: '#FF4444', paddingVertical: Spacing.md, paddingHorizontal: Spacing.md, borderRadius: 8, alignItems: 'center' }}
              onPress={() => {
                const current = parseInt(String(appSettings['required_eula_version'] || '1'), 10);
                const next = current + 1;
                Alert.alert(
                  "Bump EULA Version?",
                  `Are you sure you want to enforce v${next}? All active users must re-accept the terms.`,
                  [
                    { text: "Cancel", style: "cancel" },
                    { text: "Enforce Version", style: "destructive", onPress: () => updateSetting('required_eula_version', next.toString()) }
                  ]
                );
              }}
            >
              <Text style={{ color: '#FFF', fontWeight: 'bold', fontSize: 13 }}>BUMP v{parseInt(String(appSettings['required_eula_version'] || '1'), 10) + 1}</Text>
            </TouchableOpacity>
          </View>
        </ScrollView>
      </SafeAreaView>
    </Modal>
  );
});
