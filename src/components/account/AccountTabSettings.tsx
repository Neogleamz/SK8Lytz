import { AccountTabSettingsProps } from './account.types';
import React from 'react';
import { View, Text, ScrollView, TouchableOpacity, Switch } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Spacing } from '../../theme/theme';

export default function AccountTabSettings({
  Colors,
  styles,
  notifCrewInvites,
  setNotifCrewInvites,
  notifSessionReminders,
  setNotifSessionReminders,
  notifLeaderHandoff,
  setNotifLeaderHandoff,
  saveNotifPrefs,
  isDark,
  toggleTheme,
  setShowEula,
  handleDeleteAccount,
  healthSyncEnabled,
  handleToggleHealthSync,
  autoPauseEnabled,
  handleToggleAutoPause,
}: AccountTabSettingsProps) {
  return (
    <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
      {/* Notification preferences */}
      <Text style={styles.sectionHeader}>PUSH NOTIFICATIONS</Text>

      {[
        { label: 'Crew Invites & Session Alerts', sub: 'When a crew session starts or you\'re invited', val: notifCrewInvites, set: (v: boolean) => { setNotifCrewInvites(v); saveNotifPrefs({ crewInvites: v, sessionReminders: notifSessionReminders, leaderHandoff: notifLeaderHandoff }); } },
        { label: 'Session Reminders', sub: '15 minutes before a scheduled session', val: notifSessionReminders, set: (v: boolean) => { setNotifSessionReminders(v); saveNotifPrefs({ crewInvites: notifCrewInvites, sessionReminders: v, leaderHandoff: notifLeaderHandoff }); } },
        { label: 'Leadership Changes', sub: 'When the leader role is passed to you', val: notifLeaderHandoff, set: (v: boolean) => { setNotifLeaderHandoff(v); saveNotifPrefs({ crewInvites: notifCrewInvites, sessionReminders: notifSessionReminders, leaderHandoff: v }); } },
      ].map(({ label, sub, val, set }) => (
        <View key={label} style={styles.settingRow}>
          <View style={{ flex: 1 }}>
            <Text style={styles.settingLabel}>{label}</Text>
            <Text style={styles.settingSubLabel}>{sub}</Text>
          </View>
          <Switch
            value={val}
            onValueChange={set}
            trackColor={{ false: 'rgba(255,255,255,0.15)', true: Colors.primary }}
            thumbColor="#FFF"
          />
        </View>
      ))}

      {/* Health Sync preferences */}
      <Text style={[styles.sectionHeader, { marginTop: Spacing.xl }]}>HEALTH INTEGRATION</Text>
      <View style={styles.settingRow}>
        <View style={{ flex: 1 }}>
          <Text style={styles.settingLabel}>Sync with Health App</Text>
          <Text style={styles.settingSubLabel}>Track active calories and heart rate (Apple Health / Google Fit)</Text>
        </View>
        <Switch
          value={healthSyncEnabled}
          onValueChange={handleToggleHealthSync}
          trackColor={{ false: 'rgba(255,255,255,0.15)', true: Colors.primary }}
          thumbColor="#FFF"
        />
      </View>

      {/* Auto Pause */}
      <Text style={[styles.sectionHeader, { marginTop: Spacing.xl }]}>SESSION TRACKING</Text>
      <View style={styles.settingRow}>
        <View style={{ flex: 1 }}>
          <Text style={styles.settingLabel}>Auto-Pause Session</Text>
          <Text style={styles.settingSubLabel}>Automatically pause workout when speed drops below 0.2 mph for 10s</Text>
        </View>
        <Switch
          value={autoPauseEnabled}
          onValueChange={handleToggleAutoPause}
          trackColor={{ false: 'rgba(255,255,255,0.15)', true: Colors.primary }}
          thumbColor="#FFF"
        />
      </View>

      {/* App preferences */}
      <Text style={[styles.sectionHeader, { marginTop: Spacing.xl }]}>APP</Text>

      <View style={styles.settingRow}>
        <View style={{ flex: 1 }}>
          <Text style={styles.settingLabel}>{isDark ? 'Dark Mode' : 'Light Mode'}</Text>
          <Text style={styles.settingSubLabel}>Toggle app theme</Text>
        </View>
        <Switch
          value={isDark}
          onValueChange={toggleTheme}
          trackColor={{ false: 'rgba(255,255,255,0.15)', true: Colors.primary }}
          thumbColor="#FFF"
        />
      </View>




      {/* Danger zone */}
      <View style={styles.dangerZone}>
        <Text style={styles.dangerHeader}>⚠️ DANGER ZONE</Text>
        <Text style={styles.dangerSub}>These actions are permanent and cannot be reversed.</Text>
        <TouchableOpacity style={styles.deleteAccountBtn} onPress={handleDeleteAccount}>
          <MaterialCommunityIcons name="delete-forever" size={16} color="#FF4444" />
          <Text style={styles.deleteAccountText}>Delete My Account</Text>
        </TouchableOpacity>
      </View>

      <View style={{ height: 30 }} />
    </ScrollView>
  );
}
