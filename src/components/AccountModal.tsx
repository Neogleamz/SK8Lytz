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
import { AdvancedHardwareModal } from './admin/AdvancedHardwareModal';
import AccountTabProfile from './account/AccountTabProfile';
import AccountTabSecurity from './account/AccountTabSecurity';
import AccountTabCrewz from './account/AccountTabCrewz';
import AccountTabDevices from './account/AccountTabDevices';
import AccountTabSettings from './account/AccountTabSettings';
import AccountTabStats from './account/AccountTabStats';


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
  /** BLE write function — threaded to AdvancedHardwareModal to avoid duplicate useBLE() */
  writeToDevice?: (payload: number[], targetId?: string) => Promise<boolean | 'partial'>;
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
  writeToDevice,
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
      <View style={styles.overlay}>
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

          {(() => {
            const tabProps = {
              Colors, styles, profilePhotoUri, profile, setProfile, avatarHue, setAvatarHue,
              userEmail, editName, setEditName, editUsername, setEditUsername, savingProfile,
              handleSaveProfile, handlePickProfilePhoto, crews, history, devices,
              newPwd, setNewPwd, confirmPwd, setConfirmPwd, showNewPwd, setShowNewPwd, savingPwd,
              handleChangePassword, newEmail, setNewEmail, savingEmail, handleChangeEmail,
              crewStep, setCrewStep, crewError, setCrewError, newCrewName, setNewCrewName, crewLoading,
              handleCreateCrew, joinCode, setJoinCode, handleJoinCrew, handleDeleteCrew, handleLeaveCrew,
              groupedDevices, editingGroupId, groupNewName, setGroupNewName, handleRenameGroup, setEditingGroupId,
              handleForgetGroup, editingDeviceId, deviceNewName, setDeviceNewName, handleRenameDevice, setEditingDeviceId,
              setAdvancedModalDevice, setAdvancedModalVisible, handleForgetDevice,
              notifCrewInvites, setNotifCrewInvites, notifSessionReminders, setNotifSessionReminders,
              notifLeaderHandoff, setNotifLeaderHandoff, saveNotifPrefs, isDark, toggleTheme, setShowEula,
              handleSignOut, handleDeleteAccount, statsLoading, lifetimeStats, recentSessions,
            };

            if (accountLoading) return <AccountModalSkeleton />;
            switch (tab) {
              case 'profile':  return <AccountTabProfile {...tabProps} />;
              case 'security': return <AccountTabSecurity {...tabProps} />;
              case 'crews':    return <AccountTabCrewz {...tabProps} />;
              case 'devices':  return <AccountTabDevices {...tabProps} />;
              case 'stats':    return <AccountTabStats {...tabProps} />;
              case 'settings': return <AccountTabSettings {...tabProps} />;
              default:         return <AccountTabProfile {...tabProps} />;
            }
          })()}
        </View>
      </View>

      {/* Child Modals */}
      <AdvancedHardwareModal 
        visible={advancedModalVisible}
        onClose={() => setAdvancedModalVisible(false)}
        targetDeviceId={advancedModalDevice?.mac || advancedModalDevice?.id || null}
        currentPoints={advancedModalDevice?.led_points}
        currentSegments={advancedModalDevice?.segments}
        currentIcType={advancedModalDevice?.ic_type}
        currentSorting={advancedModalDevice?.color_sorting}
        writeToDevice={writeToDevice ?? (async () => false)}
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
