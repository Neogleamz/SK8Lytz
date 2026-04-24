import React from 'react';
import { View, Text, ScrollView, TouchableOpacity, Switch } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Spacing } from '../../theme/theme';
import GranularPermissionsList from '../permissions/GranularPermissionsList';

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
  handleSignOut,
  handleDeleteAccount,
}: any) {
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

      {/* Privacy & Permissions */}
      <Text style={[styles.sectionHeader, { marginTop: Spacing.xl }]}>PRIVACY & PERMISSIONS</Text>
      <GranularPermissionsList />

      {/* Legal & Compliance */}
      <Text style={[styles.sectionHeader, { marginTop: Spacing.xl }]}>LEGAL</Text>

      <TouchableOpacity style={styles.signOutBtn} onPress={() => setShowEula(true)}>
        <MaterialCommunityIcons name="file-document-outline" size={18} color={Colors.textMuted} />
        <Text style={[styles.signOutText, { color: Colors.text }]}>Review EULA</Text>
      </TouchableOpacity>

      {/* Sign out */}
      <Text style={[styles.sectionHeader, { marginTop: Spacing.xl }]}>ACCOUNT</Text>

      <TouchableOpacity style={styles.signOutBtn} onPress={handleSignOut}>
        <MaterialCommunityIcons name="logout" size={18} color="#FF4444" />
        <Text style={styles.signOutText}>Sign Out</Text>
      </TouchableOpacity>

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
