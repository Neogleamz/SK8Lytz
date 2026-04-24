import { MaterialCommunityIcons } from '@expo/vector-icons';
import React, { useState } from 'react';
import { Alert, SafeAreaView, ScrollView, Switch, Text, TouchableOpacity, View } from 'react-native';
import { Spacing } from '../../../theme/theme';
import { adminStyles as styles } from '../adminStyles';
import { CONTROLS_REGISTRY, ControlEntry } from '../../../constants/ControlsRegistry';

export interface AppManagerProps {
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

const TABS = Object.keys(CONTROLS_REGISTRY);

export const AppManager = React.memo(({
  visible, onClose, appSettings, handlePolicyToggle, updateSetting,
  bg, cardBg, borderColor, textPrimary, textMuted
}: AppManagerProps) => {
  const [activeTab, setActiveTab] = useState(TABS[0]);

  const renderControl = (ctrl: ControlEntry, index: number) => {
    const isDanger = ctrl.riskLevel === 'danger';
    const trackTrue = isDanger ? '#FF4444' : '#00f0ff';
    const isLast = index === CONTROLS_REGISTRY[activeTab].length - 1;

    if (ctrl.type === 'switch') {
      const currentValue = appSettings[ctrl.key] ?? ctrl.defaultValue;
      const isTrue = currentValue === true;

      const toggle = (v: boolean) => {
        if (ctrl.confirmTitle && ctrl.confirmMsg && v !== (ctrl.defaultValue === true)) {
          // Only show alert if moving away from default safety
          handlePolicyToggle(ctrl.key, v, ctrl.confirmTitle, ctrl.confirmMsg);
        } else {
          updateSetting(ctrl.key, v);
        }
      };

      return (
        <View key={ctrl.key} style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', padding: Spacing.lg, borderBottomWidth: isLast ? 0 : 1, borderBottomColor: borderColor }}>
          <View style={{ flex: 1, marginRight: Spacing.lg }}>
            <Text style={{ color: textPrimary, fontSize: 15, fontWeight: '700' }}>{ctrl.label}</Text>
            <Text style={{ color: textMuted, fontSize: 12, marginTop: Spacing.xxs }}>{ctrl.subLabel}</Text>
          </View>
          <Switch value={isTrue} onValueChange={toggle} trackColor={{ false: '#444', true: trackTrue }} />
        </View>
      );
    }

    if (ctrl.type === 'action') {
      let currentVal = appSettings[ctrl.key] ?? ctrl.defaultValue;
      
      const fireAction = () => {
        if (ctrl.key === 'required_eula_version') {
          const next = parseInt(String(currentVal), 10) + 1;
          Alert.alert(
            ctrl.confirmTitle || 'Confirm Action',
            ctrl.confirmMsg || 'Are you sure you want to proceed?',
            [
              { text: 'Cancel', style: 'cancel' },
              { text: ctrl.actionLabel || 'Confirm', style: 'destructive', onPress: () => updateSetting(ctrl.key, next.toString()) }
            ]
          );
        }
      };

      return (
        <View key={ctrl.key} style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', padding: Spacing.lg, borderBottomWidth: isLast ? 0 : 1, borderBottomColor: borderColor }}>
          <View style={{ flex: 1, marginRight: Spacing.lg }}>
            <Text style={{ color: textPrimary, fontSize: 15, fontWeight: '700' }}>{ctrl.label}</Text>
            <Text style={{ color: textMuted, fontSize: 12, marginTop: Spacing.xxs }}>
              {ctrl.key === 'required_eula_version' ? `Current target: v${currentVal}. ` : ''}{ctrl.subLabel}
            </Text>
          </View>
          <TouchableOpacity 
            style={{ backgroundColor: isDanger ? '#FF4444' : '#00f0ff', paddingVertical: Spacing.md, paddingHorizontal: Spacing.md, borderRadius: 8, alignItems: 'center' }}
            onPress={fireAction}
          >
            <Text style={{ color: isDanger ? '#FFF' : '#000', fontWeight: 'bold', fontSize: 13 }}>
              {ctrl.key === 'required_eula_version' ? `BUMP v${parseInt(String(currentVal), 10) + 1}` : ctrl.actionLabel}
            </Text>
          </TouchableOpacity>
        </View>
      );
    }
    return null;
  };

  return (
    <View style={{ flex: 1 }}>
      <SafeAreaView style={{ flex: 1, backgroundColor: bg }}>
        {/* Header */}
        <View style={{ flexDirection: 'row', alignItems: 'center', padding: Spacing.lg, borderBottomWidth: 1, borderBottomColor: borderColor, backgroundColor: cardBg }}>
          <TouchableOpacity onPress={onClose} style={{ marginRight: Spacing.lg, padding: Spacing.xs }}>
            <MaterialCommunityIcons name="arrow-left" size={24} color="#9D4EFF" />
          </TouchableOpacity>
          <View style={{ flex: 1 }}>
            <Text style={[styles.title, { color: textPrimary }]}>🛡️ CONTROLS HUB</Text>
            <Text style={{ color: textMuted, fontSize: 11, marginTop: Spacing.xxs }}>Registry-driven governance engine</Text>
          </View>
        </View>
        
        {/* Horizontal Tabs */}
        <View style={{ borderBottomWidth: 1, borderBottomColor: borderColor, backgroundColor: cardBg }}>
          <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={{ paddingHorizontal: Spacing.md }}>
            {TABS.map(tab => {
              const isActive = activeTab === tab;
              return (
                <TouchableOpacity 
                  key={tab} 
                  onPress={() => setActiveTab(tab)}
                  style={{ 
                    paddingVertical: Spacing.md, 
                    paddingHorizontal: Spacing.lg,
                    borderBottomWidth: 2,
                    borderBottomColor: isActive ? '#9D4EFF' : 'transparent'
                  }}
                >
                  <Text style={{ color: isActive ? textPrimary : textMuted, fontWeight: isActive ? 'bold' : 'normal', fontSize: 14 }}>
                    {tab.toUpperCase()}
                  </Text>
                </TouchableOpacity>
              );
            })}
          </ScrollView>
        </View>

        {/* Dynamic Content */}
        <ScrollView style={{ flex: 1, padding: Spacing.lg }}>
          <Text style={{ color: textMuted, fontSize: 12, marginBottom: Spacing.lg }}>
            These policies are globally enforced. Safety locks are required for critical changes.
          </Text>

          <View style={{ backgroundColor: cardBg, borderWidth: 1, borderColor: borderColor, borderRadius: 12, marginBottom: Spacing.xxl, overflow: 'hidden' }}>
            {CONTROLS_REGISTRY[activeTab]?.map((ctrl, index) => renderControl(ctrl, index))}
          </View>
        </ScrollView>
      </SafeAreaView>
    </View>
  );
});
