import { MaterialCommunityIcons } from '@expo/vector-icons';
import React from 'react';
import { ScrollView, Text, TouchableOpacity, View } from 'react-native';
import { Spacing } from '../../theme/theme';
import { adminStyles as styles } from './adminStyles';
import { TabProps } from './DeviceTab';

export interface AdminTabProps extends TabProps {
  onOpenProgrammer?: () => void;
  onOpenLab?: () => void;
  setIsPicksSchedulerVisible: (v: boolean) => void;
  setIsProductManagerVisible: (v: boolean) => void;
  setIsAppManagerVisible: (v: boolean) => void;
  setIsUserManagementVisible: (v: boolean) => void;
  setIsRosterVisible: (v: boolean) => void;
  setIsAuditVisible: (v: boolean) => void;
  setIsBlacklistVisible: (v: boolean) => void;
  setIsFeatureFlagsVisible: (v: boolean) => void;
  setIsGlobalAnalyticsVisible: (v: boolean) => void;
}

export const AdminTab = React.memo(({ 
  onOpenProgrammer, onOpenLab, setIsPicksSchedulerVisible, 
  setIsProductManagerVisible, setIsAppManagerVisible, setIsUserManagementVisible,
  setIsRosterVisible, setIsAuditVisible, setIsBlacklistVisible, setIsFeatureFlagsVisible,
  setIsGlobalAnalyticsVisible,
  textMuted, textPrimary, cardBg, borderColor 
}: AdminTabProps) => {
  return (
    <ScrollView style={styles.tabContent}>
      <Text style={[styles.statSection, { color: textPrimary }]}>🛠️ Engineering Tools</Text>
      <TouchableOpacity onPress={onOpenLab} style={[styles.statCard, { backgroundColor: cardBg, borderColor, flexDirection: 'row', alignItems: 'center' }]}>
        <MaterialCommunityIcons name="flask" size={24} color="#FF7000" />
        <View style={{ marginLeft: Spacing.md }}>
          <Text style={{ color: textPrimary, fontWeight: '700' }}>Diagnostic Lab</Text>
          <Text style={{ color: textMuted, fontSize: 12 }}>Low-level GATT explorer & payload builder</Text>
        </View>
      </TouchableOpacity>
      <TouchableOpacity onPress={onOpenProgrammer} style={[styles.statCard, { backgroundColor: cardBg, borderColor, flexDirection: 'row', alignItems: 'center', marginTop: Spacing.md }]}>
        <MaterialCommunityIcons name="chip" size={24} color="#00f0ff" />
        <View style={{ marginLeft: Spacing.md }}>
          <Text style={{ color: textPrimary, fontWeight: '700' }}>Firmware Over-The-Air</Text>
          <Text style={{ color: textMuted, fontSize: 12 }}>Register-level EEPROM flash (0x62)</Text>
        </View>
      </TouchableOpacity>

      <Text style={[styles.statSection, { color: textPrimary, marginTop: Spacing.xl }]}>⚙️ Remote Configuration</Text>
      <TouchableOpacity onPress={() => setIsAppManagerVisible(true)} style={[styles.statCard, { backgroundColor: cardBg, borderColor, flexDirection: 'row', alignItems: 'center' }]}>
        <MaterialCommunityIcons name="shield-lock-outline" size={24} color="#9D4EFF" />
        <View style={{ marginLeft: Spacing.md }}>
          <Text style={{ color: textPrimary, fontWeight: '700' }}>App Manager</Text>
          <Text style={{ color: textMuted, fontSize: 12 }}>Master governance and policy compliance</Text>
        </View>
        <MaterialCommunityIcons name="chevron-right" size={20} color={textMuted} style={{ marginLeft: 'auto' }} />
      </TouchableOpacity>

      <TouchableOpacity onPress={() => setIsUserManagementVisible(true)} style={[styles.statCard, { backgroundColor: cardBg, borderColor, flexDirection: 'row', alignItems: 'center', marginTop: Spacing.md }]}>
        <MaterialCommunityIcons name="account-group-outline" size={24} color="#00E676" />
        <View style={{ marginLeft: Spacing.md }}>
          <Text style={{ color: textPrimary, fontWeight: '700' }}>User Management</Text>
          <Text style={{ color: textMuted, fontSize: 12 }}>Ban, lockout, or supervise users</Text>
        </View>
        <MaterialCommunityIcons name="chevron-right" size={20} color={textMuted} style={{ marginLeft: 'auto' }} />
      </TouchableOpacity>

      <TouchableOpacity onPress={() => setIsRosterVisible(true)} style={[styles.statCard, { backgroundColor: cardBg, borderColor, flexDirection: 'row', alignItems: 'center', marginTop: Spacing.md }]}>
        <MaterialCommunityIcons name="shield-star" size={24} color="#FFD700" />
        <View style={{ marginLeft: Spacing.md }}>
          <Text style={{ color: textPrimary, fontWeight: '700' }}>Admin Roster</Text>
          <Text style={{ color: textMuted, fontSize: 12 }}>View and manage administrators</Text>
        </View>
        <MaterialCommunityIcons name="chevron-right" size={20} color={textMuted} style={{ marginLeft: 'auto' }} />
      </TouchableOpacity>

      <TouchableOpacity onPress={() => setIsAuditVisible(true)} style={[styles.statCard, { backgroundColor: cardBg, borderColor, flexDirection: 'row', alignItems: 'center', marginTop: Spacing.md }]}>
        <MaterialCommunityIcons name="text-box-search-outline" size={24} color="#FF5A00" />
        <View style={{ marginLeft: Spacing.md }}>
          <Text style={{ color: textPrimary, fontWeight: '700' }}>Audit Trail</Text>
          <Text style={{ color: textMuted, fontSize: 12 }}>Log of all administrative actions</Text>
        </View>
        <MaterialCommunityIcons name="chevron-right" size={20} color={textMuted} style={{ marginLeft: 'auto' }} />
      </TouchableOpacity>

      <TouchableOpacity onPress={() => setIsBlacklistVisible(true)} style={[styles.statCard, { backgroundColor: cardBg, borderColor, flexDirection: 'row', alignItems: 'center', marginTop: Spacing.md }]}>
        <MaterialCommunityIcons name="chip" size={24} color="#ff4040" />
        <View style={{ marginLeft: Spacing.md }}>
          <Text style={{ color: textPrimary, fontWeight: '700' }}>Hardware Blacklist</Text>
          <Text style={{ color: textMuted, fontSize: 12 }}>Block specific MAC addresses</Text>
        </View>
        <MaterialCommunityIcons name="chevron-right" size={20} color={textMuted} style={{ marginLeft: 'auto' }} />
      </TouchableOpacity>

      <TouchableOpacity onPress={() => setIsFeatureFlagsVisible(true)} style={[styles.statCard, { backgroundColor: cardBg, borderColor, flexDirection: 'row', alignItems: 'center', marginTop: Spacing.md }]}>
        <MaterialCommunityIcons name="flag-variant" size={24} color="#00f0ff" />
        <View style={{ marginLeft: Spacing.md }}>
          <Text style={{ color: textPrimary, fontWeight: '700' }}>Feature Flags</Text>
          <Text style={{ color: textMuted, fontSize: 12 }}>Manage A/B testing and rollouts</Text>
        </View>
        <MaterialCommunityIcons name="chevron-right" size={20} color={textMuted} style={{ marginLeft: 'auto' }} />
      </TouchableOpacity>

      <TouchableOpacity onPress={() => setIsGlobalAnalyticsVisible(true)} style={[styles.statCard, { backgroundColor: cardBg, borderColor, flexDirection: 'row', alignItems: 'center', marginTop: Spacing.md }]}>
        <MaterialCommunityIcons name="chart-bar" size={24} color="#00E676" />
        <View style={{ marginLeft: Spacing.md }}>
          <Text style={{ color: textPrimary, fontWeight: '700' }}>Global Analytics</Text>
          <Text style={{ color: textMuted, fontSize: 12 }}>Macroscopic fleet-wide telemetry</Text>
        </View>
        <MaterialCommunityIcons name="chevron-right" size={20} color={textMuted} style={{ marginLeft: 'auto' }} />
      </TouchableOpacity>

      <TouchableOpacity onPress={() => setIsProductManagerVisible(true)} style={[styles.statCard, { backgroundColor: cardBg, borderColor, flexDirection: 'row', alignItems: 'center', marginTop: Spacing.md }]}>
        <MaterialCommunityIcons name="cube-scan" size={24} color="#FF5A00" />
        <View style={{ marginLeft: Spacing.md }}>
          <Text style={{ color: textPrimary, fontWeight: '700' }}>Product Manager</Text>
          <Text style={{ color: textMuted, fontSize: 12 }}>Edit hardware catalogs and LED config</Text>
        </View>
        <MaterialCommunityIcons name="chevron-right" size={20} color={textMuted} style={{ marginLeft: 'auto' }} />
      </TouchableOpacity>

      <Text style={[styles.statSection, { color: textPrimary, marginTop: Spacing.xl }]}>📅 Content Scheduling</Text>
      <TouchableOpacity onPress={() => setIsPicksSchedulerVisible(true)} style={[styles.statCard, { backgroundColor: cardBg, borderColor, flexDirection: 'row', alignItems: 'center' }]}>
        <MaterialCommunityIcons name="calendar-clock" size={24} color="#FFD700" />
        <View style={{ marginLeft: Spacing.md }}>
          <Text style={{ color: textPrimary, fontWeight: '700' }}>Community Picks Queue</Text>
          <Text style={{ color: textMuted, fontSize: 12 }}>Schedule and manage featured animations</Text>
        </View>
        <MaterialCommunityIcons name="chevron-right" size={20} color={textMuted} style={{ marginLeft: 'auto' }} />
      </TouchableOpacity>
    </ScrollView>
  );
});
