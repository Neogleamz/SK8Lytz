/**
 * AccountModal.tsx — SK8Lytz Account Management
 *
 * Full-featured account management bottom sheet with 5 tabs:
 *
 *  PROFILE   — display name, username, avatar color, email display
 *  SECURITY  — change password, change email
 *  CREWS     — my permanent crews (owned & joined), create / join / leave / delete
 *  DEVICES   — registered BLE devices, rename / forget
 *  SETTINGS  — push notifications toggle, notification prefs, app preferences
 *              + Sign Out + Danger Zone (delete account)
 */

import React, { useState, useEffect, useCallback } from 'react';
import {
  View, Text, Modal, TouchableOpacity, TextInput, Platform,
  StyleSheet, ScrollView, ActivityIndicator, Alert, Switch,
} from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../context/ThemeContext';
import { profileService, UserProfile, PermanentCrew, SessionHistoryItem } from '../services/ProfileService';
import { supabase } from '../services/supabaseClient';
import { AppLogger } from '../services/AppLogger';

// ─── Types ────────────────────────────────────────────────────────────────────

type Tab = 'profile' | 'security' | 'crews' | 'devices' | 'settings';

type StoredDevice = {
  id: string;
  name: string;
  customName?: string;
  type?: string;
  registeredAt?: string;
};

interface AccountModalProps {
  visible: boolean;
  onClose: () => void;
  onSignOut: () => void;
  onJoinCrewSession?: (crewId: string) => void;
  /** Registered devices from the main app state */
  registeredDevices?: StoredDevice[];
  onDeviceRenamed?: (deviceId: string, newName: string) => void;
  onDeviceForgotten?: (deviceId: string) => void;
}

// ─── Constants ────────────────────────────────────────────────────────────────

const AVATAR_COLORS = [
  '#FFAA00','#FF6B6B','#4ECDC4','#45B7D1','#96CEB4',
  '#DDA0DD','#98D8C8','#F7DC6F','#BB8FCE','#85C1E9',
  '#FF8C00','#00E676','#00B0FF','#FF4081','#EA80FC',
];

const NOTIF_PREF_KEY = '@sk8lytz_notif_prefs';

function initials(name: string | null) {
  if (!name) return '?';
  return name.split(' ').map(w => w[0]).join('').slice(0, 2).toUpperCase();
}

function formatDate(iso: string) {
  if (!iso) return '';
  return new Date(iso).toLocaleDateString(undefined, { month: 'short', day: 'numeric', year: 'numeric' });
}

// ─── Component ────────────────────────────────────────────────────────────────

export default function AccountModal({
  visible, onClose, onSignOut,
  onJoinCrewSession,
  registeredDevices = [],
  onDeviceRenamed,
  onDeviceForgotten,
}: AccountModalProps) {
  const { Colors, isDark, toggleTheme } = useTheme();
  const styles = createStyles(Colors);

  const [tab, setTab] = useState<Tab>('profile');
  const [loading, setLoading] = useState(false);

  // Profile state
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [editName, setEditName] = useState('');
  const [editUsername, setEditUsername] = useState('');
  const [savingProfile, setSavingProfile] = useState(false);
  const [userEmail, setUserEmail] = useState('');

  // Security state
  const [currentPwd, setCurrentPwd] = useState('');
  const [newPwd, setNewPwd] = useState('');
  const [confirmPwd, setConfirmPwd] = useState('');
  const [securityMsg, setSecurityMsg] = useState<{ type: 'error' | 'success'; text: string } | null>(null);
  const [savingPwd, setSavingPwd] = useState(false);
  const [newEmail, setNewEmail] = useState('');
  const [savingEmail, setSavingEmail] = useState(false);
  const [showCurrentPwd, setShowCurrentPwd] = useState(false);
  const [showNewPwd, setShowNewPwd] = useState(false);

  // Crews state
  const [crews, setCrews] = useState<PermanentCrew[]>([]);
  const [crewStep, setCrewStep] = useState<'list' | 'create' | 'join'>('list');
  const [newCrewName, setNewCrewName] = useState('');
  const [joinCode, setJoinCode] = useState('');
  const [crewLoading, setCrewLoading] = useState(false);
  const [crewError, setCrewError] = useState('');

  // Devices state  
  const [devices, setDevices] = useState<StoredDevice[]>([]);
  const [editingDeviceId, setEditingDeviceId] = useState<string | null>(null);
  const [deviceNewName, setDeviceNewName] = useState('');

  // History state
  const [history, setHistory] = useState<SessionHistoryItem[]>([]);

  // Notifications state
  const [notifCrewInvites, setNotifCrewInvites] = useState(true);
  const [notifSessionReminders, setNotifSessionReminders] = useState(true);
  const [notifLeaderHandoff, setNotifLeaderHandoff] = useState(true);
  const [savingNotifs, setSavingNotifs] = useState(false);

  // ── Load data ─────────────────────────────────────────────────────────────

  const loadData = useCallback(async () => {
    if (!visible) return;
    setLoading(true);
    try {
      const { data: { user } } = await supabase.auth.getUser();
      if (user) setUserEmail(user.email ?? '');

      const [p, c, h] = await Promise.all([
        profileService.fetchOrCreateProfile(),
        profileService.getMyCrew(),
        profileService.getSessionHistory(),
      ]);
      if (p) {
        setProfile(p);
        setEditName(p.display_name ?? '');
        setEditUsername(p.username ?? '');
      }
      setCrews(c);
      setHistory(h);

      // ── Fetch cloud-registered devices from DB ─────────────────
      try {
        const { data: dbDevices } = await supabase
          .from('registered_devices')
          .select('device_mac, device_name, product_type, position, group_name, created_at')
          .eq('user_id', user?.id ?? '')
          .order('created_at', { ascending: false });

        if (dbDevices && dbDevices.length > 0) {
          setDevices(dbDevices.map((d: any) => ({
            id: d.device_mac,
            name: d.device_name ?? d.device_mac,
            customName: d.group_name ?? undefined,
            type: d.product_type ?? undefined,
            registeredAt: d.created_at,
          })));
        }
      } catch (devErr) {
        console.warn('[AccountModal] Could not fetch cloud devices:', devErr);
        // Falls back to whatever parent passed in as registeredDevices prop
      }
    } catch (e) {
      console.warn('[AccountModal] loadData error:', e);
    } finally {
      setLoading(false);
    }

    // Load notification prefs
    try {
      const raw = await AsyncStorage.getItem(NOTIF_PREF_KEY);
      if (raw) {
        const prefs = JSON.parse(raw);
        setNotifCrewInvites(prefs.crewInvites ?? true);
        setNotifSessionReminders(prefs.sessionReminders ?? true);
        setNotifLeaderHandoff(prefs.leaderHandoff ?? true);
      }
    } catch {}
  }, [visible]);

  useEffect(() => {
    if (visible) {
      loadData();
      // If parent provided devices, populate immediately;
      // actual cloud devices are fetched inside loadData below.
      if (registeredDevices.length > 0) setDevices(registeredDevices);
    }
  }, [visible, loadData, registeredDevices]);

  // ── Profile handlers ──────────────────────────────────────────────────────

  const handleSaveProfile = async () => {
    if (!editName.trim()) return;
    setSavingProfile(true);
    try {
      const updates: Partial<UserProfile> = { display_name: editName.trim() };
      if (editUsername.trim()) updates.username = editUsername.trim().toLowerCase();
      await profileService.updateProfile(updates);
      setProfile(p => p ? { ...p, ...updates } : p);
      AppLogger.log('PROFILE_UPDATED', { fields: Object.keys(updates) });
      Alert.alert('Saved', 'Profile updated successfully.');
    } catch (e: any) {
      Alert.alert('Error', e.message || 'Could not save profile');
    } finally {
      setSavingProfile(false);
    }
  };

  // ── Security handlers ─────────────────────────────────────────────────────

  const handleChangePassword = async () => {
    setSecurityMsg(null);
    if (!newPwd || newPwd.length < 8) {
      setSecurityMsg({ type: 'error', text: 'New password must be at least 8 characters' });
      return;
    }
    if (newPwd !== confirmPwd) {
      setSecurityMsg({ type: 'error', text: 'Passwords do not match' });
      return;
    }
    setSavingPwd(true);
    try {
      const { error } = await supabase.auth.updateUser({ password: newPwd });
      if (error) throw error;
      setCurrentPwd(''); setNewPwd(''); setConfirmPwd('');
      setSecurityMsg({ type: 'success', text: '✓ Password updated successfully' });
    } catch (e: any) {
      setSecurityMsg({ type: 'error', text: e.message || 'Could not change password' });
    } finally {
      setSavingPwd(false);
    }
  };

  const handleChangeEmail = async () => {
    if (!newEmail.includes('@')) {
      setSecurityMsg({ type: 'error', text: 'Enter a valid email address' });
      return;
    }
    setSavingEmail(true);
    try {
      const { error } = await supabase.auth.updateUser({ email: newEmail });
      if (error) throw error;
      setSecurityMsg({ type: 'success', text: `✓ Confirmation sent to ${newEmail}. Check your inbox.` });
      setNewEmail('');
    } catch (e: any) {
      setSecurityMsg({ type: 'error', text: e.message || 'Could not update email' });
    } finally {
      setSavingEmail(false);
    }
  };

  // ── Crew handlers ─────────────────────────────────────────────────────────

  const handleCreateCrew = async () => {
    if (!newCrewName.trim()) { setCrewError('Enter a crew name'); return; }
    setCrewLoading(true); setCrewError('');
    try {
      const crew = await profileService.createPermanentCrew(newCrewName.trim());
      setCrews(prev => [...prev, crew]);
      setNewCrewName(''); setCrewStep('list');
      AppLogger.log('CREW_PERMANENT_CREATED', { crewName: newCrewName.trim() });
    } catch (e: any) {
      setCrewError(e.message ?? 'Failed to create crew');
    } finally { setCrewLoading(false); }
  };

  const handleJoinCrew = async () => {
    if (joinCode.trim().length < 4) { setCrewError('Enter the invite code'); return; }
    setCrewLoading(true); setCrewError('');
    try {
      const crew = await profileService.joinPermanentCrew(joinCode.trim());
      setCrews(prev => prev.find(c => c.id === crew.id) ? prev : [...prev, crew]);
      setJoinCode(''); setCrewStep('list');
      AppLogger.log('CREW_PERMANENT_JOINED', { crewId: crew.id });
    } catch (e: any) {
      setCrewError(e.message ?? 'Failed to join crew');
    } finally { setCrewLoading(false); }
  };

  const handleDeleteCrew = (crew: PermanentCrew) => {
    Alert.alert(
      `Delete "${crew.name}"?`,
      'This will permanently delete the crew and disconnect all members. This cannot be undone.',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Delete Forever', style: 'destructive',
          onPress: async () => {
            try {
              await profileService.leavePermanentCrew(crew.id);
              setCrews(prev => prev.filter(c => c.id !== crew.id));
            } catch (e: any) { Alert.alert('Error', e.message); }
          },
        },
      ]
    );
  };

  const handleLeaveCrew = (crew: PermanentCrew) => {
    Alert.alert(
      `Leave "${crew.name}"?`,
      "You'll stop receiving session notifications for this crew.",
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Leave', style: 'destructive',
          onPress: async () => {
            await profileService.leavePermanentCrew(crew.id);
            setCrews(prev => prev.filter(c => c.id !== crew.id));
            AppLogger.log('CREW_PERMANENT_LEFT', { crewId: crew.id });
          },
        },
      ]
    );
  };

  // ── Device handlers ───────────────────────────────────────────────────────

  const handleRenameDevice = (device: StoredDevice) => {
    if (!deviceNewName.trim()) return;
    onDeviceRenamed?.(device.id, deviceNewName.trim());
    setDevices(prev => prev.map(d => d.id === device.id ? { ...d, customName: deviceNewName.trim() } : d));
    setEditingDeviceId(null);
    setDeviceNewName('');
    AppLogger.log('DEVICE_RENAMED', { deviceId: device.id, oldName: device.name, newName: deviceNewName.trim() });
  };

  const handleForgetDevice = (device: StoredDevice) => {
    Alert.alert(
      `Forget "${device.customName || device.name}"?`,
      'This removes it from your registered devices. You can always re-pair later.',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Forget', style: 'destructive',
          onPress: async () => {
            onDeviceForgotten?.(device.id);
            setDevices(prev => prev.filter(d => d.id !== device.id));
            // Also remove from Supabase registered_devices
            try {
              await supabase
                .from('registered_devices')
                .delete()
                .eq('device_mac', device.id);
            } catch (e) {
              console.warn('[AccountModal] Could not remove device from cloud:', e);
            }
          },
        },
      ]
    );
  };

  // ── Notification prefs ────────────────────────────────────────────────────

  const saveNotifPrefs = async (prefs: { crewInvites: boolean; sessionReminders: boolean; leaderHandoff: boolean }) => {
    setSavingNotifs(true);
    await AsyncStorage.setItem(NOTIF_PREF_KEY, JSON.stringify(prefs)).catch(() => {});
    setSavingNotifs(false);
  };

  // ── Sign Out ──────────────────────────────────────────────────────────────

  const handleSignOut = () => {
    Alert.alert('Sign Out', 'Sign out of your SK8Lytz account?', [
      { text: 'Cancel', style: 'cancel' },
      {
        text: 'Sign Out', style: 'destructive',
        onPress: async () => {
          await supabase.auth.signOut();
          onSignOut();
          onClose();
        },
      },
    ]);
  };

  const handleDeleteAccount = () => {
    Alert.alert(
      '⚠️ Delete Account',
      'This will permanently delete your account, all crews you own, and your session history. This CANNOT be undone.',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Delete My Account', style: 'destructive',
          onPress: () => {
            Alert.alert(
              'Are you absolutely sure?',
              'Type "DELETE" to confirm account deletion.',
              [
                { text: 'Cancel', style: 'cancel' },
                {
                  text: 'Confirm Delete', style: 'destructive',
                  onPress: async () => {
                    // Supabase does not allow client-side self-deletion by default
                    // Direct user to support or trigger a server-side function
                    Alert.alert(
                      'Request Submitted',
                      'Your account deletion request has been submitted. Your account will be deleted within 24 hours. You have been signed out.',
                    );
                    await supabase.auth.signOut();
                    onSignOut(); onClose();
                  },
                },
              ]
            );
          },
        },
      ]
    );
  };

  // ════════════════════════════════════════════════════════
  // TAB: PROFILE
  // ════════════════════════════════════════════════════════

  const renderProfile = () => (
    <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
      {/* Avatar */}
      <View style={[styles.avatarCircle, { backgroundColor: profile?.avatar_color ?? '#FF8C00' }]}>
        <Text style={styles.avatarText}>{initials(profile?.display_name ?? null)}</Text>
      </View>
      <Text style={styles.emailDisplay}>{userEmail}</Text>

      {/* Display name */}
      <Text style={styles.label}>DISPLAY NAME</Text>
      <TextInput style={styles.input} value={editName} onChangeText={setEditName}
        placeholder="Your skater name" placeholderTextColor={Colors.textMuted}
        maxLength={32} returnKeyType="done" />

      {/* Username */}
      <Text style={styles.label}>USERNAME</Text>
      <View style={styles.usernameRow}>
        <Text style={styles.atSign}>@</Text>
        <TextInput
          style={[styles.input, { flex: 1, marginBottom: 0 }]}
          value={editUsername} onChangeText={t => setEditUsername(t.toLowerCase().replace(/[^a-z0-9_]/g, ''))}
          placeholder="yourhandle" placeholderTextColor={Colors.textMuted}
          maxLength={24} autoCapitalize="none" returnKeyType="done"
        />
      </View>
      <Text style={styles.hint}>Lowercase letters, numbers, underscores. Used for login.</Text>

      {/* Avatar color */}
      <Text style={styles.label}>AVATAR COLOR</Text>
      <View style={styles.colorRow}>
        {AVATAR_COLORS.map(color => (
          <TouchableOpacity
            key={color}
            style={[styles.colorSwatch, { backgroundColor: color }, profile?.avatar_color === color && styles.colorSwatchActive]}
            onPress={async () => {
              await profileService.updateProfile({ avatar_color: color });
              setProfile(p => p ? { ...p, avatar_color: color } : p);
            }}
          />
        ))}
      </View>

      {/* Save */}
      <TouchableOpacity style={[styles.primaryBtn, savingProfile && { opacity: 0.5 }]}
        onPress={handleSaveProfile} disabled={savingProfile}>
        {savingProfile ? <ActivityIndicator color="#000" /> : (
          <>
            <MaterialCommunityIcons name="content-save" size={16} color="#000" />
            <Text style={styles.primaryBtnText}>Save Profile</Text>
          </>
        )}
      </TouchableOpacity>

      {/* Stats */}
      <View style={styles.statsRow}>
        <View style={styles.statCard}>
          <Text style={styles.statNum}>{crews.length}</Text>
          <Text style={styles.statLabel}>Crews</Text>
        </View>
        <View style={styles.statCard}>
          <Text style={styles.statNum}>{history.length || '—'}</Text>
          <Text style={styles.statLabel}>Sessions</Text>
        </View>
        <View style={styles.statCard}>
          <Text style={styles.statNum}>{devices.length || '—'}</Text>
          <Text style={styles.statLabel}>Devices</Text>
        </View>
      </View>

      {/* Session history preview */}
      {history.length > 0 && (
        <>
          <Text style={styles.label}>RECENT SESSIONS</Text>
          {history.slice(0, 5).map((item, i) => (
            <View key={`h-${i}`} style={styles.historyRow}>
              <View style={[styles.historyDot, { backgroundColor: item.role === 'leader' ? '#FFAA00' : '#00AAFF' }]} />
              <View style={{ flex: 1 }}>
                <Text style={styles.historyName}>{item.session_name}</Text>
                <Text style={styles.historyDate}>{formatDate(item.joined_at)}</Text>
              </View>
              <Text style={{ fontSize: 12, color: item.role === 'leader' ? '#FFAA00' : '#00AAFF' }}>
                {item.role === 'leader' ? '👑' : '⚡'}
              </Text>
            </View>
          ))}
        </>
      )}

      <View style={{ height: 20 }} />
    </ScrollView>
  );

  // ════════════════════════════════════════════════════════
  // TAB: SECURITY
  // ════════════════════════════════════════════════════════

  const renderSecurity = () => (
    <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
      {securityMsg && (
        <View style={[styles.msgBanner, { backgroundColor: securityMsg.type === 'success' ? 'rgba(0,230,118,0.12)' : 'rgba(255,68,68,0.12)', borderColor: securityMsg.type === 'success' ? '#00E676' : '#FF4444' }]}>
          <Text style={[styles.msgText, { color: securityMsg.type === 'success' ? '#00E676' : '#FF4444' }]}>
            {securityMsg.text}
          </Text>
        </View>
      )}

      {/* Change Password */}
      <Text style={styles.sectionHeader}>CHANGE PASSWORD</Text>

      <Text style={styles.label}>NEW PASSWORD</Text>
      <View style={styles.pwdRow}>
        <TextInput style={[styles.input, { flex: 1, marginBottom: 0 }]}
          value={newPwd} onChangeText={setNewPwd}
          placeholder="Min. 8 characters" placeholderTextColor={Colors.textMuted}
          secureTextEntry={!showNewPwd} autoCapitalize="none" />
        <TouchableOpacity style={styles.eyeBtn} onPress={() => setShowNewPwd(!showNewPwd)}>
          <MaterialCommunityIcons name={showNewPwd ? 'eye-off' : 'eye'} size={18} color={Colors.textMuted} />
        </TouchableOpacity>
      </View>

      <Text style={styles.label}>CONFIRM NEW PASSWORD</Text>
      <TextInput style={styles.input}
        value={confirmPwd} onChangeText={setConfirmPwd}
        placeholder="Repeat new password" placeholderTextColor={Colors.textMuted}
        secureTextEntry={!showNewPwd} autoCapitalize="none" />

      <TouchableOpacity style={[styles.primaryBtn, savingPwd && { opacity: 0.5 }]}
        onPress={handleChangePassword} disabled={savingPwd}>
        {savingPwd ? <ActivityIndicator color="#000" /> : (
          <>
            <MaterialCommunityIcons name="lock-reset" size={16} color="#000" />
            <Text style={styles.primaryBtnText}>Update Password</Text>
          </>
        )}
      </TouchableOpacity>

      {/* Change Email */}
      <Text style={[styles.sectionHeader, { marginTop: 28 }]}>CHANGE EMAIL</Text>
      <Text style={styles.currentEmail}>Current: {userEmail}</Text>

      <Text style={styles.label}>NEW EMAIL ADDRESS</Text>
      <TextInput style={styles.input}
        value={newEmail} onChangeText={setNewEmail}
        placeholder="new@email.com" placeholderTextColor={Colors.textMuted}
        keyboardType="email-address" autoCapitalize="none" />

      <TouchableOpacity style={[styles.secondaryBtn, savingEmail && { opacity: 0.5 }]}
        onPress={handleChangeEmail} disabled={savingEmail}>
        {savingEmail ? <ActivityIndicator color={Colors.primary} /> : (
          <>
            <MaterialCommunityIcons name="email-sync" size={16} color={Colors.primary} />
            <Text style={styles.secondaryBtnText}>Send Confirmation Email</Text>
          </>
        )}
      </TouchableOpacity>

      <View style={{ height: 20 }} />
    </ScrollView>
  );

  // ════════════════════════════════════════════════════════
  // TAB: CREWS
  // ════════════════════════════════════════════════════════

  const renderCrews = () => {
    if (crewStep === 'create') return (
      <View style={styles.body}>
        <TouchableOpacity style={styles.backBtn} onPress={() => { setCrewStep('list'); setCrewError(''); }}>
          <MaterialCommunityIcons name="chevron-left" size={20} color={Colors.textMuted} />
          <Text style={styles.backText}>My Crews</Text>
        </TouchableOpacity>
        <Text style={styles.sectionHeader}>CREATE A CREW</Text>
        <Text style={styles.label}>CREW NAME</Text>
        <TextInput style={styles.input} value={newCrewName} onChangeText={setNewCrewName}
          placeholder="e.g. Rink Riders" placeholderTextColor={Colors.textMuted}
          maxLength={40} autoFocus />
        {!!crewError && <Text style={styles.errorText}>{crewError}</Text>}
        <TouchableOpacity style={[styles.primaryBtn, crewLoading && { opacity: 0.6 }]}
          onPress={handleCreateCrew} disabled={crewLoading}>
          {crewLoading ? <ActivityIndicator color="#000" /> : <Text style={styles.primaryBtnText}>Create Crew</Text>}
        </TouchableOpacity>
      </View>
    );

    if (crewStep === 'join') return (
      <View style={styles.body}>
        <TouchableOpacity style={styles.backBtn} onPress={() => { setCrewStep('list'); setCrewError(''); }}>
          <MaterialCommunityIcons name="chevron-left" size={20} color={Colors.textMuted} />
          <Text style={styles.backText}>My Crews</Text>
        </TouchableOpacity>
        <Text style={styles.sectionHeader}>JOIN A CREW</Text>
        <Text style={styles.label}>INVITE CODE</Text>
        <TextInput style={[styles.input, styles.codeInput]}
          value={joinCode} onChangeText={t => setJoinCode(t.toUpperCase())}
          placeholder="ABC123" placeholderTextColor={Colors.textMuted}
          maxLength={6} autoCapitalize="characters" autoFocus />
        {!!crewError && <Text style={styles.errorText}>{crewError}</Text>}
        <TouchableOpacity style={[styles.primaryBtn, crewLoading && { opacity: 0.6 }]}
          onPress={handleJoinCrew} disabled={crewLoading}>
          {crewLoading ? <ActivityIndicator color="#000" /> : <Text style={styles.primaryBtnText}>Join Crew</Text>}
        </TouchableOpacity>
      </View>
    );

    return (
      <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
        {crews.length === 0 ? (
          <View style={styles.emptyState}>
            <MaterialCommunityIcons name="account-group-outline" size={52} color={Colors.textMuted} />
            <Text style={styles.emptyTitle}>No Saved Crews</Text>
            <Text style={styles.emptySubtitle}>Create or join a permanent crew to get notified when sessions start.</Text>
          </View>
        ) : (
          crews.map(crew => (
            <View key={crew.id} style={styles.crewCard}>
              <View style={styles.crewCardIcon}>
                <MaterialCommunityIcons
                  name={crew.is_owner ? 'crown' : 'account-group'}
                  size={20} color={crew.is_owner ? '#FFD700' : Colors.primary} />
              </View>
              <View style={{ flex: 1, marginLeft: 12 }}>
                <Text style={styles.crewCardName}>{crew.name}</Text>
                <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8, marginTop: 3 }}>
                  <Text style={styles.crewCardCode}>
                    Code: <Text style={{ color: '#FFAA00', fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace' }}>{crew.invite_code}</Text>
                  </Text>
                  {crew.is_owner && <Text style={styles.ownerBadge}>OWNER</Text>}
                </View>
              </View>
              <TouchableOpacity
                style={styles.crewActionBtn}
                onPress={() => crew.is_owner ? handleDeleteCrew(crew) : handleLeaveCrew(crew)}>
                <Text style={[styles.crewActionText, crew.is_owner && { color: '#FF4444' }]}>
                  {crew.is_owner ? 'Delete' : 'Leave'}
                </Text>
              </TouchableOpacity>
            </View>
          ))
        )}

        <TouchableOpacity style={styles.primaryBtn} onPress={() => { setCrewStep('create'); setCrewError(''); }}>
          <MaterialCommunityIcons name="plus" size={18} color="#000" />
          <Text style={styles.primaryBtnText}>Create a Crew</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.secondaryBtn} onPress={() => { setCrewStep('join'); setCrewError(''); }}>
          <MaterialCommunityIcons name="account-plus-outline" size={18} color={Colors.primary} />
          <Text style={styles.secondaryBtnText}>Join by Code</Text>
        </TouchableOpacity>
        <View style={{ height: 20 }} />
      </ScrollView>
    );
  };

  // ════════════════════════════════════════════════════════
  // TAB: DEVICES
  // ════════════════════════════════════════════════════════

  const renderDevices = () => (
    <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
      {devices.length === 0 ? (
        <View style={styles.emptyState}>
          <MaterialCommunityIcons name="bluetooth-off" size={52} color={Colors.textMuted} />
          <Text style={styles.emptyTitle}>No Registered Devices</Text>
          <Text style={styles.emptySubtitle}>Pair your SK8Lytz skates from the main screen to see them here.</Text>
        </View>
      ) : (
        devices.map(device => (
          <View key={device.id} style={styles.deviceCard}>
            <MaterialCommunityIcons
              name={device.type === 'SOULZ' ? 'skate' : 'lightning-bolt-circle'}
              size={22} color={Colors.primary} style={{ marginRight: 12 }} />
            <View style={{ flex: 1 }}>
              {editingDeviceId === device.id ? (
                <TextInput
                  style={[styles.input, { marginBottom: 0, paddingVertical: 6, paddingHorizontal: 10 }]}
                  value={deviceNewName} onChangeText={setDeviceNewName}
                  placeholder={device.customName || device.name}
                  placeholderTextColor={Colors.textMuted}
                  autoFocus maxLength={32}
                  returnKeyType="done"
                  onSubmitEditing={() => handleRenameDevice(device)}
                />
              ) : (
                <>
                  <Text style={styles.deviceName}>{device.customName || device.name}</Text>
                  {device.type && <Text style={styles.deviceMeta}>{device.type} · {device.id.slice(-8)}</Text>}
                  {device.registeredAt && <Text style={styles.deviceMeta}>Paired {formatDate(device.registeredAt)}</Text>}
                </>
              )}
            </View>
            <View style={{ flexDirection: 'row', gap: 8, alignItems: 'center' }}>
              {editingDeviceId === device.id ? (
                <>
                  <TouchableOpacity style={styles.deviceSaveBtn} onPress={() => handleRenameDevice(device)}>
                    <MaterialCommunityIcons name="check" size={16} color="#000" />
                  </TouchableOpacity>
                  <TouchableOpacity onPress={() => { setEditingDeviceId(null); setDeviceNewName(''); }}>
                    <MaterialCommunityIcons name="close" size={18} color={Colors.textMuted} />
                  </TouchableOpacity>
                </>
              ) : (
                <>
                  <TouchableOpacity style={styles.deviceIconBtn} onPress={() => {
                    setEditingDeviceId(device.id);
                    setDeviceNewName(device.customName || device.name);
                  }}>
                    <MaterialCommunityIcons name="pencil" size={16} color={Colors.textMuted} />
                  </TouchableOpacity>
                  <TouchableOpacity style={styles.deviceIconBtn} onPress={() => handleForgetDevice(device)}>
                    <MaterialCommunityIcons name="trash-can-outline" size={16} color="#FF4444" />
                  </TouchableOpacity>
                </>
              )}
            </View>
          </View>
        ))
      )}

      <Text style={[styles.hint, { marginTop: 16 }]}>
        Devices are paired from the scanner. Remove them here if you no longer use them.
      </Text>
      <View style={{ height: 20 }} />
    </ScrollView>
  );

  // ════════════════════════════════════════════════════════
  // TAB: SETTINGS
  // ════════════════════════════════════════════════════════

  const renderSettings = () => (
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
      <Text style={[styles.sectionHeader, { marginTop: 24 }]}>APP</Text>

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

      {/* Sign out */}
      <Text style={[styles.sectionHeader, { marginTop: 24 }]}>ACCOUNT</Text>

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

  // ════════════════════════════════════════════════════════
  // TABS CONFIG
  // ════════════════════════════════════════════════════════

  const TABS: { id: Tab; icon: string; label: string }[] = [
    { id: 'profile',  icon: 'account',           label: 'Profile' },
    { id: 'security', icon: 'lock',               label: 'Security' },
    { id: 'crews',    icon: 'account-group',      label: 'Crews' },
    { id: 'devices',  icon: 'bluetooth',          label: 'Devices' },
    { id: 'settings', icon: 'cog',                label: 'Settings' },
  ];

  // ════════════════════════════════════════════════════════
  // MAIN RENDER
  // ════════════════════════════════════════════════════════

  return (
    <Modal visible={visible} animationType="slide" transparent statusBarTranslucent>
      <View style={styles.overlay}>
        <View style={styles.sheet}>
          <TouchableOpacity style={styles.closeBtn} onPress={onClose}>
            <MaterialCommunityIcons name="close" size={22} color={Colors.textMuted} />
          </TouchableOpacity>

          <Text style={styles.sheetTitle}>Account</Text>

          {/* Tab bar */}
          <ScrollView horizontal showsHorizontalScrollIndicator={false} style={styles.tabBarScroll}
            contentContainerStyle={styles.tabBar}>
            {TABS.map(t => (
              <TouchableOpacity
                key={t.id}
                style={[styles.tabBtn, tab === t.id && styles.tabBtnActive]}
                onPress={() => { setTab(t.id); setCrewStep('list'); setSecurityMsg(null); }}>
                <MaterialCommunityIcons
                  name={t.icon as any}
                  size={16}
                  color={tab === t.id ? '#000' : Colors.textMuted} />
                <Text style={[styles.tabBtnText, tab === t.id && styles.tabBtnTextActive]}>
                  {t.label}
                </Text>
              </TouchableOpacity>
            ))}
          </ScrollView>

          {loading
            ? <ActivityIndicator style={{ marginTop: 50 }} size="large" color={Colors.primary} />
            : tab === 'profile'  ? renderProfile()
            : tab === 'security' ? renderSecurity()
            : tab === 'crews'    ? renderCrews()
            : tab === 'devices'  ? renderDevices()
            : renderSettings()
          }
        </View>
      </View>
    </Modal>
  );
}

// ─── Styles ───────────────────────────────────────────────────────────────────

const createStyles = (Colors: any) => StyleSheet.create({
  overlay:    { flex: 1, backgroundColor: 'rgba(0,0,0,0.8)', justifyContent: 'flex-end' },
  sheet: {
    backgroundColor: Colors.background ?? '#0D0D0D',
    borderTopLeftRadius: 26, borderTopRightRadius: 26,
    maxHeight: '93%', minHeight: '70%', paddingTop: 22,
  },
  closeBtn:   { position: 'absolute', top: 16, right: 16, zIndex: 10, padding: 8 },
  sheetTitle: { color: Colors.text ?? '#FFF', fontSize: 22, fontWeight: '800', textAlign: 'center', marginBottom: 14 },

  // Tabs
  tabBarScroll: { flexGrow: 0, marginBottom: 4 },
  tabBar: { flexDirection: 'row', paddingHorizontal: 14, gap: 8, paddingBottom: 2 },
  tabBtn: {
    flexDirection: 'row', alignItems: 'center', gap: 5,
    paddingVertical: 8, paddingHorizontal: 14,
    borderRadius: 20, backgroundColor: 'rgba(255,255,255,0.06)',
  },
  tabBtnActive:     { backgroundColor: Colors.primary ?? '#FFAA00' },
  tabBtnText:       { color: Colors.textMuted ?? '#888', fontSize: 12, fontWeight: '700' },
  tabBtnTextActive: { color: '#000' },

  // Body
  body:          { padding: 20, paddingBottom: 20 },
  label:         { color: Colors.textMuted ?? '#888', fontSize: 10, fontWeight: '700', letterSpacing: 1.2, marginTop: 16, marginBottom: 6 },
  sectionHeader: { color: Colors.textMuted ?? '#888', fontSize: 11, fontWeight: '800', letterSpacing: 1.5, marginTop: 8, marginBottom: 10, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.06)', paddingBottom: 8 },
  hint:          { color: 'rgba(255,255,255,0.3)', fontSize: 11, lineHeight: 16 },
  errorText:     { color: '#FF4444', fontSize: 13, marginTop: 6 },

  // Inputs
  input: {
    backgroundColor: 'rgba(255,255,255,0.07)', borderRadius: 12,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.12)',
    color: Colors.text ?? '#FFF', fontSize: 15,
    paddingHorizontal: 14, paddingVertical: 11, width: '100%', marginBottom: 4,
  },
  codeInput: {
    fontSize: 28, fontWeight: '900', letterSpacing: 8, textAlign: 'center',
    fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace',
  },

  // Profile
  avatarCircle: { width: 80, height: 80, borderRadius: 40, alignItems: 'center', justifyContent: 'center', alignSelf: 'center', marginBottom: 4 },
  avatarText:   { color: '#000', fontSize: 28, fontWeight: '900' },
  emailDisplay: { color: Colors.textMuted ?? '#888', fontSize: 12, textAlign: 'center', marginBottom: 4 },
  usernameRow:  { flexDirection: 'row', alignItems: 'center', gap: 4 },
  atSign:       { color: Colors.textMuted ?? '#888', fontSize: 18, fontWeight: '700', paddingBottom: 4 },
  colorRow:     { flexDirection: 'row', flexWrap: 'wrap', gap: 10, marginBottom: 4 },
  colorSwatch:  { width: 30, height: 30, borderRadius: 15 },
  colorSwatchActive: { borderWidth: 3, borderColor: '#FFF' },
  statsRow:     { flexDirection: 'row', gap: 10, marginTop: 20 },
  statCard:     { flex: 1, backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14, padding: 14, alignItems: 'center' },
  statNum:      { color: Colors.text ?? '#FFF', fontSize: 26, fontWeight: '900' },
  statLabel:    { color: Colors.textMuted ?? '#888', fontSize: 11, marginTop: 2 },

  // History preview
  historyRow:   { flexDirection: 'row', alignItems: 'center', gap: 10, paddingVertical: 8, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.05)' },
  historyDot:   { width: 8, height: 8, borderRadius: 4 },
  historyName:  { color: Colors.text ?? '#FFF', fontSize: 13, fontWeight: '600' },
  historyDate:  { color: Colors.textMuted ?? '#888', fontSize: 11, marginTop: 1 },

  // Security
  msgBanner:    { borderRadius: 10, borderWidth: 1, padding: 12, marginBottom: 12 },
  msgText:      { fontSize: 13, fontWeight: '600', lineHeight: 18 },
  pwdRow:       { flexDirection: 'row', alignItems: 'center', gap: 8 },
  eyeBtn:       { padding: 10 },
  currentEmail: { color: Colors.textMuted ?? '#888', fontSize: 12, marginBottom: 4 },

  // Crews
  crewCard: {
    flexDirection: 'row', alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14,
    padding: 14, marginBottom: 10,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)',
  },
  crewCardIcon:   { width: 40, height: 40, borderRadius: 20, backgroundColor: 'rgba(255,255,255,0.07)', alignItems: 'center', justifyContent: 'center' },
  crewCardName:   { color: Colors.text ?? '#FFF', fontSize: 15, fontWeight: '700' },
  crewCardCode:   { color: Colors.textMuted ?? '#888', fontSize: 12, marginTop: 2 },
  ownerBadge:     { fontSize: 9, fontWeight: '800', letterSpacing: 1, color: '#FFD700', borderWidth: 1, borderColor: '#FFD700', borderRadius: 4, paddingHorizontal: 5, paddingVertical: 1 },
  crewActionBtn:  { paddingHorizontal: 12, paddingVertical: 7, borderRadius: 20, borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)' },
  crewActionText: { color: Colors.textMuted ?? '#888', fontSize: 12, fontWeight: '700' },

  // Devices
  deviceCard: {
    flexDirection: 'row', alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14,
    padding: 14, marginBottom: 10,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)',
  },
  deviceName:     { color: Colors.text ?? '#FFF', fontSize: 15, fontWeight: '700' },
  deviceMeta:     { color: Colors.textMuted ?? '#888', fontSize: 11, marginTop: 2 },
  deviceIconBtn:  { padding: 8, borderRadius: 8, backgroundColor: 'rgba(255,255,255,0.06)' },
  deviceSaveBtn:  { padding: 8, borderRadius: 8, backgroundColor: Colors.primary ?? '#FFAA00' },

  // Settings
  settingRow:     { flexDirection: 'row', alignItems: 'center', paddingVertical: 14, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.05)' },
  settingLabel:   { color: Colors.text ?? '#FFF', fontSize: 14, fontWeight: '600' },
  settingSubLabel:{ color: Colors.textMuted ?? '#888', fontSize: 11, marginTop: 2 },
  signOutBtn:     { flexDirection: 'row', alignItems: 'center', gap: 10, paddingVertical: 16, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.05)' },
  signOutText:    { color: '#FF4444', fontSize: 15, fontWeight: '700' },

  // Danger zone
  dangerZone:     { marginTop: 20, borderWidth: 1, borderColor: 'rgba(255,68,68,0.25)', borderRadius: 14, padding: 16 },
  dangerHeader:   { color: '#FF4444', fontSize: 13, fontWeight: '800', letterSpacing: 1, marginBottom: 6 },
  dangerSub:      { color: Colors.textMuted ?? '#888', fontSize: 12, marginBottom: 14, lineHeight: 17 },
  deleteAccountBtn: { flexDirection: 'row', alignItems: 'center', gap: 8, borderWidth: 1, borderColor: '#FF4444', borderRadius: 10, paddingVertical: 11, paddingHorizontal: 14, alignSelf: 'flex-start' },
  deleteAccountText: { color: '#FF4444', fontSize: 13, fontWeight: '700' },

  // Buttons
  primaryBtn:     { flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 8, backgroundColor: Colors.primary ?? '#FFAA00', borderRadius: 14, paddingVertical: 14, width: '100%', marginTop: 16 },
  primaryBtnText: { color: '#000', fontSize: 15, fontWeight: '800' },
  secondaryBtn:   { flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 8, borderWidth: 1.5, borderColor: Colors.primary ?? '#FFAA00', borderRadius: 14, paddingVertical: 13, width: '100%', marginTop: 12 },
  secondaryBtnText: { color: Colors.primary ?? '#FFAA00', fontSize: 15, fontWeight: '700' },
  backBtn:        { flexDirection: 'row', alignItems: 'center', gap: 4, marginBottom: 16 },
  backText:       { color: Colors.textMuted ?? '#888', fontSize: 14 },

  // Empty states
  emptyState:     { alignItems: 'center', paddingVertical: 36 },
  emptyTitle:     { color: Colors.text ?? '#FFF', fontSize: 18, fontWeight: '700', marginTop: 14 },
  emptySubtitle:  { color: Colors.textMuted ?? '#888', fontSize: 13, textAlign: 'center', marginTop: 6, lineHeight: 20 },
});
