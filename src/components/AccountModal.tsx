import { Spacing } from '../theme/theme';
/**
 * AccountModal.tsx — SK8Lytz Account Management
 *
 * Full-featured account management bottom sheet with 5 tabs:
 *
 *  PROFILE   — display name, username, avatar color, email display
 *  SECURITY  — change password, change email
 *  CREWZ     — my permanent crews (owned & joined), create / join / leave / delete
 *  DEVICES   — registered BLE devices, rename / forget
 *  SETTINGS  — push notifications toggle, notification prefs, app preferences
 *              + Sign Out + Danger Zone (delete account)
 */

import { MaterialCommunityIcons } from '@expo/vector-icons';
import React, { useEffect, useRef, useState } from 'react';
import {
    ActivityIndicator, Alert,
    Animated,
    Image,
    KeyboardAvoidingView,
    Modal,
    Platform,
    ScrollView,
    StyleSheet,
    Switch,
    Text,
    TextInput,
    TouchableOpacity,
    View,
} from 'react-native';
import { useTheme } from '../context/ThemeContext';
import { useAccountOverview } from '../hooks/useAccountOverview';
import { useSkateStats } from '../hooks/useSkateStats';
import { PermanentCrew, profileService } from '../services/ProfileService';
import { supabase } from '../services/supabaseClient';
import EulaModal from './modals/EulaModal';
import GranularPermissionsList from './permissions/GranularPermissionsList';
import { HardwareStatusPills } from './dashboard/HardwareStatusPills';
import { AdvancedHardwareModal } from './admin/AdvancedHardwareModal';


// ─── Types ────────────────────────────────────────────────────────────────────

type Tab = 'profile' | 'security' | 'crews' | 'devices' | 'settings' | 'stats';

/** Lightweight device display type for AccountModal, mapped from RegisteredDevice. */
export type StoredDevice = {
  id: string;
  mac?: string;
  name: string;
  customName?: string;
  groupName?: string;
  type?: string;
  registeredAt?: string;
  led_points?: number;
  segments?: number;
  ic_type?: string;
  color_sorting?: string;
};

interface AccountModalProps {
  visible: boolean;
  onClose: () => void;
  onSignOut: () => void;
  onJoinCrewSession?: (crewId: string) => void;
  /** Registered devices from the main app state — SINGLE SOURCE OF TRUTH */
  registeredDevices?: StoredDevice[];
  onDeviceRenamed?: (deviceId: string, newName: string) => void;
  onDeviceForgotten?: (deviceId: string) => void;
  onGroupRenamed?: (oldGroupName: string, newGroupName: string) => void;
  onGroupForgotten?: (groupName: string) => void;
  /** When true, hides online-only tabs like CREWZ */
  isOfflineMode?: boolean;
}

// ─── Constants ────────────────────────────────────────────────────────────────

import { STORAGE_PREFIX } from '../constants/AppConstants';
import CustomSlider from './CustomSlider';

const NOTIF_PREF_KEY = `${STORAGE_PREFIX}notif_prefs`;

// hexToHue moved to hooks

function initials(name: string | null) {
  if (!name) return '?';
  return name.split(' ').map(w => w[0]).join('').slice(0, 2).toUpperCase();
}

function formatDate(iso: string) {
  if (!iso) return '';
  return new Date(iso).toLocaleDateString(undefined, { month: 'short', day: 'numeric', year: 'numeric' });
}

// ─── AccountModalSkeleton ─────────────────────────────────────────────────────

function AccountModalSkeleton() {
  const pulse = useRef(new Animated.Value(0.4)).current;

  useEffect(() => {
    Animated.loop(
      Animated.sequence([
        Animated.timing(pulse, { toValue: 1, duration: 700, useNativeDriver: true }),
        Animated.timing(pulse, { toValue: 0.4, duration: 700, useNativeDriver: true }),
      ])
    ).start();
  }, [pulse]);

  const SkeletonBar = ({ w = '100%', h = 14, mb = 12, br = 8 }: { w?: string | number; h?: number; mb?: number; br?: number }) => (
    <Animated.View
      style={{
        width: w as any, height: h, borderRadius: br,
        backgroundColor: 'rgba(255,255,255,0.08)',
        marginBottom: mb,
        opacity: pulse,
      }}
    />
  );

  return (
    <View style={{ padding: Spacing.xl, paddingTop: Spacing.lg }}>
      {/* Avatar placeholder */}
      <Animated.View style={{ width: 80, height: 80, borderRadius: 40, backgroundColor: 'rgba(255,255,255,0.08)', alignSelf: 'center', marginBottom: Spacing.xl, opacity: pulse }} />
      {/* Email line */}
      <SkeletonBar w="50%" h={10} mb={Spacing.xl} br={6} />
      {/* Field labels + inputs */}
      <SkeletonBar w="30%" h={10} mb={8} />
      <SkeletonBar w="100%" h={44} mb={Spacing.lg} br={12} />
      <SkeletonBar w="30%" h={10} mb={8} />
      <SkeletonBar w="100%" h={44} mb={Spacing.xl} br={12} />
      {/* Stats row */}
      <View style={{ flexDirection: 'row', gap: Spacing.md, marginTop: Spacing.md }}>
        <SkeletonBar w="32%" h={70} mb={0} br={14} />
        <SkeletonBar w="32%" h={70} mb={0} br={14} />
        <SkeletonBar w="32%" h={70} mb={0} br={14} />
      </View>
    </View>
  );
}

// ─── Component ────────────────────────────────────────────────────────────────

export default function AccountModal({
  visible, onClose, onSignOut,
  onJoinCrewSession,
  registeredDevices = [],
  onDeviceRenamed,
  onDeviceForgotten,
  onGroupRenamed,
  onGroupForgotten,
  isOfflineMode = false,
}: AccountModalProps) {
  const { Colors, isDark, toggleTheme } = useTheme();
  const styles = createStyles(Colors);

  const [tab, setTab] = useState<Tab>('profile');
  const [showEula, setShowEula] = useState(false);

  // --- Domain Hooks ---
  const {
    loading: accountLoading,
    profile, setProfile,
    editName, setEditName,
    editUsername, setEditUsername,
    savingProfile,
    profilePhotoUri,
    avatarHue, setAvatarHue,
    userEmail,
    crews, setCrews,
    crewStep, setCrewStep,
    newCrewName, setNewCrewName,
    joinCode, setJoinCode,
    crewLoading,
    crewError, setCrewError,
    history,
    notifCrewInvites, setNotifCrewInvites,
    notifSessionReminders, setNotifSessionReminders,
    notifLeaderHandoff, setNotifLeaderHandoff,
    handleSaveProfile,
    handlePickProfilePhoto,
    saveNotifPrefs,
    handleCreateCrew,
    handleJoinCrew,
    handleLeaveCrew: leaveCrewHook,
  } = useAccountOverview(visible);

  const handleDeleteCrew = (crew: PermanentCrew) => {
    Alert.alert(
      `Delete "${crew.name}"?`,
      'This will permanently delete the crew and disconnect all members. This cannot be undone.',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Delete Forever', style: 'destructive',
          onPress: async () => {
            await leaveCrewHook(crew.id);
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
            await leaveCrewHook(crew.id);
          },
        },
      ]
    );
  };

  const {
    lifetimeStats,
    recentSessions,
    statsLoading,
  } = useSkateStats(visible);

  // ─── Device Fleet State (was useDeviceFleet — now reads from shared registeredDevices prop) ──
  const devices = registeredDevices;
  const [editingDeviceId, setEditingDeviceId] = useState<string | null>(null);
  const [deviceNewName, setDeviceNewName] = useState('');
  const [editingGroupId, setEditingGroupId] = useState<string | null>(null);
  const [groupNewName, setGroupNewName] = useState('');
  
  const [advancedModalVisible, setAdvancedModalVisible] = useState(false);
  const [advancedModalDevice, setAdvancedModalDevice] = useState<StoredDevice | null>(null);

  const groupedDevices = React.useMemo(() => {
    const groups: { [key: string]: StoredDevice[] } = { "_Ungrouped": [] };
    devices.forEach(d => {
      const gName = d.groupName || '';
      if (gName) {
        if (!groups[gName]) groups[gName] = [];
        groups[gName].push(d);
      } else {
        groups["_Ungrouped"].push(d);
      }
    });
    return groups;
  }, [devices]);

  const handleRenameDevice = (device: StoredDevice) => {
    if (!deviceNewName.trim()) return;
    const newName = deviceNewName.trim();
    onDeviceRenamed?.(device.id, newName);
    setEditingDeviceId(null);
    setDeviceNewName('');
  };

  const handleForgetDevice = (device: StoredDevice) => {
    Alert.alert(
      `Forget "${device.customName || device.name}"?`,
      'This removes it from your registered devices. You can always re-pair later.',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Forget', style: 'destructive',
          onPress: () => { onDeviceForgotten?.(device.id); },
        },
      ]
    );
  };

  const handleRenameGroup = (oldName: string) => {
    if (!groupNewName.trim()) return;
    const newName = groupNewName.trim();
    onGroupRenamed?.(oldName, newName);
    setEditingGroupId(null);
    setGroupNewName('');
  };

  const handleForgetGroup = (groupName: string) => {
    Alert.alert(
      `Forget Group "${groupName}"?`,
      'This removes all devices within this group from your registered devices.',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Forget All', style: 'destructive',
          onPress: () => { onGroupForgotten?.(groupName); },
        },
      ]
    );
  };



  // Security state (Kept local for now as per Phase 3 scope)
  const [_currentPwd, setCurrentPwd] = useState('');
  const [newPwd, setNewPwd] = useState('');
  const [confirmPwd, setConfirmPwd] = useState('');
  const [securityMsg, setSecurityMsg] = useState<{ type: 'error' | 'success'; text: string } | null>(null);
  const [savingPwd, setSavingPwd] = useState(false);
  const [newEmail, setNewEmail] = useState('');
  const [savingEmail, setSavingEmail] = useState(false);
  const [_showCurrentPwd, _setShowCurrentPwd] = useState(false);
  const [showNewPwd, setShowNewPwd] = useState(false);

  // ── Profile handlers ──────────────────────────────────────────────────────

  // Profile, Crews, and Device handlers moved to hooks

  // ── Notification prefs ────────────────────────────────────────────────────

  // Notification and Security handlers
  const handleSaveNotifPrefs = (prefs: any) => saveNotifPrefs(prefs);

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

  // ── Sign Out ──────────────────────────────────────────────────────────────

  const handleSignOut = async () => {
    if (Platform.OS === 'web') {
      // On web, Alert.alert() shows a browser native confirm() which can block automation.
      // Sign out directly — the app will redirect to AuthScreen via the auth state listener.
      await supabase.auth.signOut();
      onSignOut();
      onClose();
      return;
    }
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
                    try {
                      // Attempt to run the dedicated Account Deletion RPC
                      const { error } = await (supabase as any).rpc('delete_account');
                      if (error) {
                        console.error('Account deletion RPC failed:', error);
                        throw new Error('Database rejection. Please contact support.');
                      }

                      Alert.alert(
                        'Account Deleted',
                        'Your account and all associated data have been permanently erased.',
                      );
                      await supabase.auth.signOut();
                      onSignOut();
                      onClose();
                    } catch (e: any) {
                      Alert.alert('Deletion Failed', e.message);
                    }
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
      {/* Avatar — tappable to pick photo */}
      <TouchableOpacity onPress={handlePickProfilePhoto} style={{ alignSelf: 'center', marginBottom: Spacing.xs }}>
        {(profilePhotoUri || profile?.avatar_url)
          ? <Image source={{ uri: profilePhotoUri ?? profile?.avatar_url! }}
              style={[styles.avatarCircle, { overflow: 'hidden' }]} />
          : <View style={[styles.avatarCircle, { backgroundColor: profile?.avatar_color ?? '#FF8C00' }]}>
              <Text style={styles.avatarText}>{initials(profile?.display_name ?? null)}</Text>
            </View>}
        <View style={{ position: 'absolute', bottom: 2, right: 2, backgroundColor: 'rgba(0,0,0,0.6)', borderRadius: 10, padding: Spacing.xxs }}>
          <MaterialCommunityIcons name="camera" size={12} color="#FFF" />
        </View>
      </TouchableOpacity>
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
      <Text style={[styles.label, { marginBottom: Spacing.lg }]}>AVATAR COLOR</Text>
      <View style={{ flexShrink: 0, minHeight: 40, marginBottom: Spacing.xl }}>
        <CustomSlider
          gradientTrack={true}
          value={avatarHue}
          onValueChange={(hue) => {
            setAvatarHue(hue);
            const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
            const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0").toUpperCase()).join("");
            const newHex = rgb2hex(f(5), f(3), f(1));
            setProfile(p => p ? { ...p, avatar_color: newHex } : p);
          }}
          onSlidingComplete={async (hue) => {
            const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
            const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0").toUpperCase()).join("");
            const newHex = rgb2hex(f(5), f(3), f(1));
            await profileService.updateProfile({ avatar_color: newHex });
          }}
          minimumValue={0}
          maximumValue={360}
          step={1}
          thumbTintColor="#FFF"
        />
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
          <Text style={styles.statLabel}>CREWZ</Text>
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
      {/* Security messages moved above */}

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
      <Text style={[styles.sectionHeader, { marginTop: Spacing.xxl }]}>CHANGE EMAIL</Text>
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
          <Text style={styles.backText}>My CREWZ</Text>
        </TouchableOpacity>
        <Text style={styles.sectionHeader}>CREATE A CREW</Text>
        <Text style={styles.label}>CREW NAME</Text>
        <TextInput style={styles.input} value={newCrewName} onChangeText={setNewCrewName}
          placeholder="e.g. Rink Riders" placeholderTextColor={Colors.textMuted}
          maxLength={40} autoFocus />
        {!!crewError && <Text style={styles.errorText}>{crewError}</Text>}
        <TouchableOpacity style={[styles.primaryBtn, crewLoading && { opacity: 0.6 }]}
          onPress={handleCreateCrew} disabled={crewLoading}>
          {crewLoading ? <ActivityIndicator color="#000" /> : <Text style={styles.primaryBtnText}>Create CREW</Text>}
        </TouchableOpacity>
      </View>
    );

    if (crewStep === 'join') return (
      <View style={styles.body}>
        <TouchableOpacity style={styles.backBtn} onPress={() => { setCrewStep('list'); setCrewError(''); }}>
          <MaterialCommunityIcons name="chevron-left" size={20} color={Colors.textMuted} />
          <Text style={styles.backText}>My CREWZ</Text>
        </TouchableOpacity>
        <Text style={styles.sectionHeader}>JOIN PRIVATE CREW</Text>
        <Text style={{ color: Colors.textMuted, fontSize: 12, marginBottom: Spacing.md, lineHeight: 17 }}>
          Enter the 6-character invite code from a private CREW. Public CREWZ don't need a code — browse them from the CREWZ HUB.
        </Text>
        <Text style={styles.label}>PRIVATE INVITE CODE</Text>
        <TextInput style={[styles.input, styles.codeInput]}
          value={joinCode} onChangeText={t => setJoinCode(t.toUpperCase())}
          placeholder="ABC123" placeholderTextColor={Colors.textMuted}
          maxLength={6} autoCapitalize="characters" autoFocus />
        {!!crewError && <Text style={styles.errorText}>{crewError}</Text>}
        <TouchableOpacity style={[styles.primaryBtn, crewLoading && { opacity: 0.6 }]}
          onPress={handleJoinCrew} disabled={crewLoading}>
          {crewLoading ? <ActivityIndicator color="#000" /> : <Text style={styles.primaryBtnText}>Join Private CREW</Text>}
        </TouchableOpacity>
      </View>
    );

    return (
      <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
        {crews.length === 0 ? (
          <View style={styles.emptyState}>
            <MaterialCommunityIcons name="account-group-outline" size={52} color={Colors.textMuted} />
            <Text style={styles.emptyTitle}>No Saved CREWZ</Text>
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
              <View style={{ flex: 1, marginLeft: Spacing.md }}>
                <Text style={styles.crewCardName}>{crew.name}</Text>
                <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, marginTop: Spacing.xxs }}>
                  {crew.is_public ? (
                    // Public crew — no code needed, show badge instead
                    <Text style={[styles.crewCardCode, { color: '#00C853' }]}>🌍 Public</Text>
                  ) : (
                    // Private crew — show invite code (owner shares it to invite others)
                    <Text style={styles.crewCardCode}>
                      Code: <Text style={{ color: '#FFAA00', fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace' }}>{crew.invite_code}</Text>
                    </Text>
                  )}
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
          <Text style={styles.primaryBtnText}>Create a CREW</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.secondaryBtn} onPress={() => { setCrewStep('join'); setCrewError(''); }}>
          <MaterialCommunityIcons name="lock-outline" size={18} color={Colors.textMuted} />
          <Text style={[styles.secondaryBtnText, { color: Colors.textMuted }]}>Join Private CREW (by Code)</Text>
        </TouchableOpacity>
        <View style={{ height: 20 }} />
      </ScrollView>
    );
  };

  // ════════════════════════════════════════════════════════
  // TAB: DEVICES
  // ════════════════════════════════════════════════════════

  // Device handlers moved to hooks

  const renderDevices = () => (
    <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
      {devices.length === 0 ? (
        <View style={styles.emptyState}>
          <MaterialCommunityIcons name="bluetooth-off" size={52} color={Colors.textMuted} />
          <Text style={styles.emptyTitle}>No Registered Devices</Text>
          <Text style={styles.emptySubtitle}>Pair your SK8Lytz skates from the main screen to see them here.</Text>
        </View>
      ) : (
        Object.entries(groupedDevices).map(([groupName, groupDevs]) => {
          if (groupDevs.length === 0) return null;
          const isUngrouped = groupName === "_Ungrouped";

          return (
            <View key={groupName} style={{ marginBottom: Spacing.lg }}>
              {!isUngrouped && (
                <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: Spacing.sm, paddingHorizontal: Spacing.lg }}>
                  {editingGroupId === groupName ? (
                    <TextInput
                      style={[styles.input, { flex: 1, marginBottom: 0, paddingVertical: Spacing.sm, paddingHorizontal: Spacing.md, marginRight: Spacing.sm }]}
                      value={groupNewName} onChangeText={setGroupNewName}
                      placeholder={groupName} placeholderTextColor={Colors.textMuted}
                      autoFocus maxLength={32} returnKeyType="done"
                      onSubmitEditing={() => handleRenameGroup(groupName)}
                    />
                  ) : (
                    <Text style={{ fontSize: 16, fontWeight: 'bold', color: Colors.text, textTransform: 'uppercase' }}>
                      {groupName}
                    </Text>
                  )}
                  
                  <View style={{ flexDirection: 'row', gap: Spacing.sm }}>
                    {editingGroupId === groupName ? (
                      <>
                        <TouchableOpacity style={styles.deviceSaveBtn} onPress={() => handleRenameGroup(groupName)}>
                          <MaterialCommunityIcons name="check" size={16} color="#000" />
                        </TouchableOpacity>
                        <TouchableOpacity onPress={() => { setEditingGroupId(null); setGroupNewName(''); }}>
                          <MaterialCommunityIcons name="close" size={18} color={Colors.textMuted} />
                        </TouchableOpacity>
                      </>
                    ) : (
                      <>
                        <TouchableOpacity onPress={() => { setEditingGroupId(groupName); setGroupNewName(groupName); }}>
                          <MaterialCommunityIcons name="pencil" size={18} color={Colors.textMuted} />
                        </TouchableOpacity>
                        <TouchableOpacity onPress={() => handleForgetGroup(groupName)}>
                          <MaterialCommunityIcons name="trash-can-outline" size={18} color="#FF4444" />
                        </TouchableOpacity>
                      </>
                    )}
                  </View>
                </View>
              )}

              {groupDevs.map(device => (
                <View key={device.id} style={styles.deviceCard}>
                  <MaterialCommunityIcons
                    name={device.type === 'SOULZ' ? 'skate' : 'lightning-bolt-circle'}
                    size={22} color={Colors.primary} style={{ marginRight: Spacing.md }} />
                  <View style={{ flex: 1 }}>
                    {editingDeviceId === device.id ? (
                      <TextInput
                        style={[styles.input, { marginBottom: 0, paddingVertical: Spacing.sm, paddingHorizontal: Spacing.md }]}
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
                        {/* Hardware pills — shows LED count, strip type, sorting from registration data */}
                        {(device.led_points || device.ic_type) && (
                          <HardwareStatusPills device={{
                            led_points: device.led_points,
                            segments: device.segments,
                            ic_type: device.ic_type,
                            color_sorting: device.color_sorting,
                          }} />
                        )}
                      </>
                    )}
                  </View>
                  <View style={{ flexDirection: 'row', gap: Spacing.sm, alignItems: 'center' }}>
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
                        <TouchableOpacity style={styles.deviceIconBtn} onPress={() => {
                          setAdvancedModalDevice(device);
                          setAdvancedModalVisible(true);
                        }}>
                          <MaterialCommunityIcons name="chip" size={16} color={Colors.primary} />
                        </TouchableOpacity>
                        <TouchableOpacity style={styles.deviceIconBtn} onPress={() => handleForgetDevice(device)}>
                          <MaterialCommunityIcons name="trash-can-outline" size={16} color="#FF4444" />
                        </TouchableOpacity>
                      </>
                    )}
                  </View>
                </View>
              ))}
            </View>
          );
        })
      )}

      <Text style={[styles.hint, { marginTop: Spacing.lg }]}>
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

  // ════════════════════════════════════════════════════════
  // TAB: STATS
  // ════════════════════════════════════════════════════════

  const renderStats = () => {
    /** Formats seconds into e.g. "1h 23m" */
    const fmtDuration = (sec: number) => {
      const h = Math.floor(sec / 3600);
      const m = Math.floor((sec % 3600) / 60);
      return h > 0 ? `${h}h ${m}m` : `${m}m`;
    };
    const fmtDate = (iso: string) =>
      new Date(iso).toLocaleDateString(undefined, { month: 'short', day: 'numeric' });

    if (statsLoading) {
      return (
        <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center', paddingVertical: Spacing.giant }}>
          <MaterialCommunityIcons name="lightning-bolt" size={32} color={Colors.primary} />
          <Text style={{ color: Colors.textMuted, marginTop: Spacing.md, fontSize: 13 }}>Loading your stats…</Text>
        </View>
      );
    }

    const noData = !lifetimeStats || lifetimeStats.totalSessions === 0;

    return (
      <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
        {noData ? (
          <View style={{ alignItems: 'center', paddingVertical: Spacing.huge }}>
            <MaterialCommunityIcons name="skate" size={48} color={Colors.textMuted} />
            <Text style={[styles.hint, { textAlign: 'center', marginTop: Spacing.lg, fontSize: 14 }]}>
              No sessions saved yet.{`\n`}Enable Street Mode and skate to record your first session!
            </Text>
          </View>
        ) : (
          <>
            {/* ── Lifetime stat grid ── */}
            <Text style={styles.sectionHeader}>LIFETIME STATS</Text>
            <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.sm, marginBottom: Spacing.lg }}>
              {[
                { icon: 'flag-checkered',        val: String(lifetimeStats!.totalSessions),                        unit: '',    lbl: 'Sessions' },
                { icon: 'map-marker-distance',   val: lifetimeStats!.totalDistanceMiles.toFixed(1),               unit: 'mi',  lbl: 'Distance' },
                { icon: 'speedometer',           val: lifetimeStats!.lifetimePeakSpeedMph.toFixed(1),             unit: 'mph', lbl: 'Record Speed' },
                { icon: 'gauge',                 val: lifetimeStats!.lifetimeAvgSpeedMph.toFixed(1),              unit: 'mph', lbl: 'Avg Speed' },
                { icon: 'timer-outline',         val: fmtDuration(lifetimeStats!.totalDurationSec),               unit: '',    lbl: 'Time on Skates' },
                { icon: 'fire',                  val: String(lifetimeStats!.lifetimeCalories),                    unit: 'kcal',lbl: 'Calories' },
              ].map(({ icon, val, unit, lbl }) => (
                <View key={lbl} style={{
                  width: '47%', backgroundColor: 'rgba(255,255,255,0.05)',
                  borderRadius: 16, padding: Spacing.lg, alignItems: 'center',
                }}>
                  <MaterialCommunityIcons name={icon as any} size={20} color={Colors.primary} />
                  <View style={{ flexDirection: 'row', alignItems: 'flex-end', gap: Spacing.xxs, marginTop: Spacing.sm }}>
                    <Text style={{ color: '#FFF', fontSize: 22, fontWeight: '900' }}>{val}</Text>
                    {unit ? <Text style={{ color: Colors.primary, fontSize: 10, fontWeight: '700', marginBottom: Spacing.xxs }}>{unit}</Text> : null}
                  </View>
                  <Text style={{ color: 'rgba(255,255,255,0.4)', fontSize: 10, fontWeight: '600', letterSpacing: 0.8, marginTop: Spacing.xs }}>{lbl}</Text>
                </View>
              ))}
            </View>

            {/* ── Recent sessions list ── */}
            {recentSessions.length > 0 && (
              <>
                <Text style={[styles.sectionHeader, { marginTop: Spacing.sm }]}>RECENT SESSIONS</Text>
                {recentSessions.map(s => (
                  <View key={s.id} style={{
                    backgroundColor: 'rgba(255,255,255,0.04)',
                    borderRadius: 14, padding: Spacing.lg, marginBottom: Spacing.sm,
                    borderLeftWidth: 3, borderLeftColor: Colors.primary,
                  }}>
                    <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginBottom: Spacing.sm }}>
                      <Text style={{ color: '#FFF', fontWeight: '700', fontSize: 14 }}>{fmtDate(s.sessionDate)}</Text>
                      <Text style={{ color: Colors.textMuted, fontSize: 12 }}>{fmtDuration(s.durationSec)}</Text>
                    </View>
                    <View style={{ flexDirection: 'row', gap: Spacing.lg }}>
                      <View style={{ alignItems: 'center' }}>
                        <Text style={{ color: '#FFF', fontWeight: '800', fontSize: 16 }}>{s.distanceMiles.toFixed(2)}</Text>
                        <Text style={{ color: Colors.textMuted, fontSize: 10 }}>mi</Text>
                      </View>
                      <View style={{ alignItems: 'center' }}>
                        <Text style={{ color: '#FFF', fontWeight: '800', fontSize: 16 }}>{s.peakSpeedMph.toFixed(1)}</Text>
                        <Text style={{ color: Colors.textMuted, fontSize: 10 }}>peak mph</Text>
                      </View>
                      <View style={{ alignItems: 'center' }}>
                        <Text style={{ color: '#FFF', fontWeight: '800', fontSize: 16 }}>{s.avgSpeedMph.toFixed(1)}</Text>
                        <Text style={{ color: Colors.textMuted, fontSize: 10 }}>avg mph</Text>
                      </View>
                      {s.calories != null && (
                        <View style={{ alignItems: 'center' }}>
                          <Text style={{ color: '#FF6B35', fontWeight: '800', fontSize: 16 }}>{s.calories}</Text>
                          <Text style={{ color: Colors.textMuted, fontSize: 10 }}>kcal</Text>
                        </View>
                      )}
                    </View>
                  </View>
                ))}
              </>
            )}
          </>
        )}
        <View style={{ height: 30 }} />
      </ScrollView>
    );
  };

  // ════════════════════════════════════════════════════════
  // TABS CONFIG
  // ════════════════════════════════════════════════════════

  const ALL_TABS: { id: Tab; icon: string; label: string }[] = [
    { id: 'profile',  icon: 'account',           label: 'Profile' },
    { id: 'security', icon: 'lock',               label: 'Security' },
    { id: 'crews',    icon: 'account-group',      label: 'CREWZ' },
    { id: 'devices',  icon: 'bluetooth',          label: 'Devices' },
    { id: 'stats',    icon: 'lightning-bolt',     label: 'Stats' },
    { id: 'settings', icon: 'cog',                label: 'Settings' },
  ];

  // Hide CREWZ tab when in offline mode — crews require Supabase
  const TABS = isOfflineMode ? ALL_TABS.filter(t => t.id !== 'crews') : ALL_TABS;

  // ════════════════════════════════════════════════════════
  // MAIN RENDER
  // ════════════════════════════════════════════════════════

  return (
    <Modal visible={visible} animationType="slide" transparent statusBarTranslucent>
      {showEula && (
        <EulaModal 
          visible={showEula} 
          onAccept={() => setShowEula(false)} 
          onDecline={() => setShowEula(false)} 
          isViewOnly={true} 
        />
      )}
      <KeyboardAvoidingView
        style={styles.overlay}
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        keyboardVerticalOffset={Platform.OS === 'ios' ? 0 : 24}>
        <View style={styles.sheet}>
          <TouchableOpacity style={styles.closeBtn} onPress={onClose}>
            <MaterialCommunityIcons name="close" size={22} color={Colors.textMuted} />
          </TouchableOpacity>

          <Text style={styles.sheetTitle}>Account</Text>

          {/* Tab bar — fixed row, all 5 tabs share equal width */}
          <View style={styles.tabBar}>
            {TABS.map(t => (
              <TouchableOpacity
                key={t.id}
                style={[styles.tabBtn, tab === t.id && styles.tabBtnActive]}
                onPress={() => { setTab(t.id); setCrewStep('list'); setSecurityMsg(null); }}>
                <MaterialCommunityIcons
                  name={t.icon as any}
                  size={16}
                  color={tab === t.id ? '#000' : Colors.textMuted} />
                <Text style={[styles.tabBtnText, tab === t.id && styles.tabBtnTextActive]}
                  numberOfLines={1}>
                  {t.label}
                </Text>
              </TouchableOpacity>
            ))}
          </View>

          {accountLoading
            ? <AccountModalSkeleton />
            : tab === 'profile'  ? renderProfile()
            : tab === 'security' ? renderSecurity()
            : tab === 'crews'    ? renderCrews()
            : tab === 'devices'  ? renderDevices()
            : tab === 'stats'    ? renderStats()
            : renderSettings()
          }
        </View>
      </KeyboardAvoidingView>

      {/* Child Modals */}
      <AdvancedHardwareModal 
        visible={advancedModalVisible}
        onClose={() => setAdvancedModalVisible(false)}
        targetDeviceId={advancedModalDevice?.mac || advancedModalDevice?.id || null}
        currentPoints={advancedModalDevice?.led_points}
        currentSegments={advancedModalDevice?.segments}
        currentIcType={advancedModalDevice?.ic_type}
        currentSorting={advancedModalDevice?.color_sorting}
      />
    </Modal>
  );
}

// ─── Styles ───────────────────────────────────────────────────────────────────

const createStyles = (Colors: any) => StyleSheet.create({
  overlay:    { flex: 1, backgroundColor: 'rgba(0,0,0,0.8)', justifyContent: 'flex-end' },
  sheet: {
    backgroundColor: Colors.background ?? '#0D0D0D',
    borderTopLeftRadius: 26, borderTopRightRadius: 26,
    maxHeight: '93%', minHeight: '70%', paddingTop: Spacing.xl,
  },
  closeBtn:   { position: 'absolute', top: 16, right: 16, zIndex: 10, padding: Spacing.sm },
  sheetTitle: { color: Colors.text ?? '#FFF', fontSize: 22, fontWeight: '800', textAlign: 'center', marginBottom: Spacing.lg },

  // Tabs
  tabBar: {
    flexDirection: 'row', paddingHorizontal: Spacing.md, gap: Spacing.sm,
    marginBottom: Spacing.xs, paddingBottom: Spacing.xxs,
  },
  tabBtn: {
    flex: 1, flexDirection: 'column', alignItems: 'center', justifyContent: 'center', gap: Spacing.xxs,
    paddingVertical: Spacing.sm, paddingHorizontal: Spacing.xs,
    borderRadius: 12, backgroundColor: 'rgba(255,255,255,0.06)',
  },
  tabBtnActive:     { backgroundColor: Colors.primary ?? '#FFAA00' },
  tabBtnText:       { color: Colors.textMuted ?? '#888', fontSize: 10, fontWeight: '700', textAlign: 'center' },
  tabBtnTextActive: { color: '#000' },

  // Body
  body:          { padding: Spacing.xl, paddingBottom: Spacing.xl },
  label:         { color: Colors.textMuted ?? '#888', fontSize: 10, fontWeight: '700', letterSpacing: 1.2, marginTop: Spacing.lg, marginBottom: Spacing.sm },
  sectionHeader: { color: Colors.textMuted ?? '#888', fontSize: 11, fontWeight: '800', letterSpacing: 1.5, marginTop: Spacing.sm, marginBottom: Spacing.md, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.06)', paddingBottom: Spacing.sm },
  hint:          { color: 'rgba(255,255,255,0.3)', fontSize: 11, lineHeight: 16 },
  errorText:     { color: '#FF4444', fontSize: 13, marginTop: Spacing.sm },

  // Inputs
  input: {
    backgroundColor: 'rgba(255,255,255,0.07)', borderRadius: 12,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.12)',
    color: Colors.text ?? '#FFF', fontSize: 15,
    paddingHorizontal: Spacing.lg, paddingVertical: Spacing.md, width: '100%', marginBottom: Spacing.xs,
  },
  codeInput: {
    fontSize: 28, fontWeight: '900', letterSpacing: 8, textAlign: 'center',
    fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace',
  },

  // Profile
  avatarCircle: { width: 80, height: 80, borderRadius: 40, alignItems: 'center', justifyContent: 'center', alignSelf: 'center', marginBottom: Spacing.xs },
  avatarText:   { color: '#000', fontSize: 28, fontWeight: '900' },
  emailDisplay: { color: Colors.textMuted ?? '#888', fontSize: 12, textAlign: 'center', marginBottom: Spacing.xs },
  usernameRow:  { flexDirection: 'row', alignItems: 'center', gap: Spacing.xs },
  atSign:       { color: Colors.textMuted ?? '#888', fontSize: 18, fontWeight: '700', paddingBottom: Spacing.xs },
  colorRow:     { flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.md, marginBottom: Spacing.xs },
  colorSwatch:  { width: 30, height: 30, borderRadius: 15 },
  colorSwatchActive: { borderWidth: 3, borderColor: '#FFF' },
  statsRow:     { flexDirection: 'row', gap: Spacing.md, marginTop: Spacing.xl },
  statCard:     { flex: 1, backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14, padding: Spacing.lg, alignItems: 'center' },
  statNum:      { color: Colors.text ?? '#FFF', fontSize: 26, fontWeight: '900' },
  statLabel:    { color: Colors.textMuted ?? '#888', fontSize: 11, marginTop: Spacing.xxs },

  // History preview
  historyRow:   { flexDirection: 'row', alignItems: 'center', gap: Spacing.md, paddingVertical: Spacing.sm, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.05)' },
  historyDot:   { width: 8, height: 8, borderRadius: 4 },
  historyName:  { color: Colors.text ?? '#FFF', fontSize: 13, fontWeight: '600' },
  historyDate:  { color: Colors.textMuted ?? '#888', fontSize: 11, marginTop: 1 },

  // Security
  msgBanner:    { borderRadius: 10, borderWidth: 1, padding: Spacing.md, marginBottom: Spacing.md },
  msgText:      { fontSize: 13, fontWeight: '600', lineHeight: 18 },
  pwdRow:       { flexDirection: 'row', alignItems: 'center', gap: Spacing.sm },
  eyeBtn:       { padding: Spacing.md },
  currentEmail: { color: Colors.textMuted ?? '#888', fontSize: 12, marginBottom: Spacing.xs },

  // Crews
  crewCard: {
    flexDirection: 'row', alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14,
    padding: Spacing.lg, marginBottom: Spacing.md,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)',
  },
  crewCardIcon:   { width: 40, height: 40, borderRadius: 20, backgroundColor: 'rgba(255,255,255,0.07)', alignItems: 'center', justifyContent: 'center' },
  crewCardName:   { color: Colors.text ?? '#FFF', fontSize: 15, fontWeight: '700' },
  crewCardCode:   { color: Colors.textMuted ?? '#888', fontSize: 12, marginTop: Spacing.xxs },
  ownerBadge:     { fontSize: 9, fontWeight: '800', letterSpacing: 1, color: '#FFD700', borderWidth: 1, borderColor: '#FFD700', borderRadius: 4, paddingHorizontal: Spacing.xs, paddingVertical: 1 },
  crewActionBtn:  { paddingHorizontal: Spacing.md, paddingVertical: Spacing.sm, borderRadius: 20, borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)' },
  crewActionText: { color: Colors.textMuted ?? '#888', fontSize: 12, fontWeight: '700' },

  // Devices
  deviceCard: {
    flexDirection: 'row', alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14,
    padding: Spacing.lg, marginBottom: Spacing.md,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)',
  },
  deviceName:     { color: Colors.text ?? '#FFF', fontSize: 15, fontWeight: '700' },
  deviceMeta:     { color: Colors.textMuted ?? '#888', fontSize: 11, marginTop: Spacing.xxs },
  deviceIconBtn:  { padding: Spacing.sm, borderRadius: 8, backgroundColor: 'rgba(255,255,255,0.06)' },
  deviceSaveBtn:  { padding: Spacing.sm, borderRadius: 8, backgroundColor: Colors.primary ?? '#FFAA00' },

  // Settings
  settingRow:     { flexDirection: 'row', alignItems: 'center', paddingVertical: Spacing.lg, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.05)' },
  settingLabel:   { color: Colors.text ?? '#FFF', fontSize: 14, fontWeight: '600' },
  settingSubLabel:{ color: Colors.textMuted ?? '#888', fontSize: 11, marginTop: Spacing.xxs },
  signOutBtn:     { flexDirection: 'row', alignItems: 'center', gap: Spacing.md, paddingVertical: Spacing.lg, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.05)' },
  signOutText:    { color: '#FF4444', fontSize: 15, fontWeight: '700' },

  // Danger zone
  dangerZone:     { marginTop: Spacing.xl, borderWidth: 1, borderColor: 'rgba(255,68,68,0.25)', borderRadius: 14, padding: Spacing.lg },
  dangerHeader:   { color: '#FF4444', fontSize: 13, fontWeight: '800', letterSpacing: 1, marginBottom: Spacing.sm },
  dangerSub:      { color: Colors.textMuted ?? '#888', fontSize: 12, marginBottom: Spacing.lg, lineHeight: 17 },
  deleteAccountBtn: { flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, borderWidth: 1, borderColor: '#FF4444', borderRadius: 10, paddingVertical: Spacing.md, paddingHorizontal: Spacing.lg, alignSelf: 'flex-start' },
  deleteAccountText: { color: '#FF4444', fontSize: 13, fontWeight: '700' },

  // Buttons
  primaryBtn:     { flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: Spacing.sm, backgroundColor: Colors.primary ?? '#FFAA00', borderRadius: 14, paddingVertical: Spacing.lg, width: '100%', marginTop: Spacing.lg },
  primaryBtnText: { color: '#000', fontSize: 15, fontWeight: '800' },
  secondaryBtn:   { flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: Spacing.sm, borderWidth: 1.5, borderColor: Colors.primary ?? '#FFAA00', borderRadius: 14, paddingVertical: Spacing.md, width: '100%', marginTop: Spacing.md },
  secondaryBtnText: { color: Colors.primary ?? '#FFAA00', fontSize: 15, fontWeight: '700' },
  backBtn:        { flexDirection: 'row', alignItems: 'center', gap: Spacing.xs, marginBottom: Spacing.lg },
  backText:       { color: Colors.textMuted ?? '#888', fontSize: 14 },

  // Empty states
  emptyState:     { alignItems: 'center', paddingVertical: Spacing.xxxl },
  emptyTitle:     { color: Colors.text ?? '#FFF', fontSize: 18, fontWeight: '700', marginTop: Spacing.lg },
  emptySubtitle:  { color: Colors.textMuted ?? '#888', fontSize: 13, textAlign: 'center', marginTop: Spacing.sm, lineHeight: 20 },
});
