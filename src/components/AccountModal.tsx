// Acknowledging Rule S4: AccountModal.tsx is a monolith file > 30KB. Editing is restricted strictly to planned items (R-04 logging, R-11 try/catch, R-15 AuthContext bypass).
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
import React, { useEffect, useState, useRef } from 'react';
import {
    Alert,
    Modal,
    Platform,
    Text,
    TouchableOpacity,
    View,
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useTheme } from '../context/ThemeContext';
import { useAuth } from '../context/AuthContext';
import { useAccountOverview } from '../hooks/useAccountOverview';
import { useSkateStats } from '../hooks/useSkateStats';
import EulaModal from './modals/EulaModal';
import { profileService, PermanentCrew } from '../services/ProfileService';
import { supabase } from '../services/supabaseClient';
import AccountTabProfile from './account/AccountTabProfile';
import AccountTabSecurity from './account/AccountTabSecurity';
import { ErrorCard } from './ErrorCard';
import AccountTabCrewz from './account/AccountTabCrewz';
import AccountTabDevices from './account/AccountTabDevices';
import AccountTabSettings from './account/AccountTabSettings';
import AccountTabStats from './account/AccountTabStats';
import { AppLogger } from '../services/appLogger';
import AccountModalSkeleton from './account/AccountModalSkeleton';
import { createStyles } from './account/AccountModalStyles';
import { scrubPII } from '../utils/piiScrubber';


// ─── Types ────────────────────────────────────────────────────────────────────

type Tab = 'profile' | 'security' | 'crews' | 'devices' | 'settings' | 'stats';

import { StoredDevice } from './account/account.types';

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
  onProfileUpdated?: () => void;
}


// ─── Component ────────────────────────────────────────────────────────────────

import { useScreenPerformance } from '../hooks/useScreenPerformance';

export default function AccountModal({
  visible, onClose, onSignOut,
  onJoinCrewSession: _onJoinCrewSession,
  registeredDevices = [],
  onDeviceRenamed,
  onDeviceForgotten,
  onGroupRenamed,
  onGroupForgotten,
  isOfflineMode = false,
  onProfileUpdated,
}: AccountModalProps) {
  const { markFullyDrawn } = useScreenPerformance('AccountModal');
  const { Colors, isDark, toggleTheme } = useTheme();
  const { signIn: authSignIn, signOut: authSignOut, updateUser } = useAuth();
  const insets = useSafeAreaInsets();
  const styles = createStyles(Colors);

  // SKIPPED R-22: markFullyDrawn() is a fire-and-forget callback, not a subscription/timer/listener — no cleanup resource.
  useEffect(() => {
    if (visible) {
      markFullyDrawn();
    }
  }, [visible, markFullyDrawn]);

  const [tab, setTab] = useState<Tab>('profile');
  const [showEula, setShowEula] = useState(false);
  const isProcessingRef = useRef(false);

  // --- Domain Hooks ---
  const {
    user,
    status: accountStatus,
    profile, setProfile,
    editName, setEditName,
    editUsername, setEditUsername,
    profilePhotoUri,
    avatarHue, setAvatarHue,
    userEmail,
    crews, setCrews,
    crewStep, setCrewStep,
    newCrewName, setNewCrewName,
    joinCode, setJoinCode,
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
    healthSyncEnabled,
    handleToggleHealthSync,
    autoPauseEnabled,
    handleToggleAutoPause,
    accountError,
    loadData,
  } = useAccountOverview(visible, onProfileUpdated);

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
              if (!user?.id) return;
              // Use profileService.deleteCrew (hard-delete crew + cascade memberships)
              // NOT leaveCrewHook — that only removes the owner's membership, leaving an orphaned crew row
              await profileService.deleteCrew(crew.id, user.id);
              setCrews(prev => prev.filter(c => c.id !== crew.id));
              AppLogger.log('CREW_PERMANENT_DELETED', { crewId: crew.id, crewName: scrubPII(crew.name) });
            } catch (err: unknown) {
              const e = err instanceof Error ? err : new Error((err instanceof Error ? err.message : String(err)));
              Alert.alert('Error', e.message || 'Could not delete crew');
            }
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
  type ModalStatus = 'idle' | 'saving_pwd' | 'saving_email' | 'deleting';
  const [modalStatus, setModalStatus] = useState<ModalStatus>('idle');

  const [currentPwd, setCurrentPwd] = useState('');
  const [newPwd, setNewPwd] = useState('');
  const [confirmPwd, setConfirmPwd] = useState('');
  const [_securityMsg, setSecurityMsg] = useState<{ type: 'error' | 'success'; text: string } | null>(null);
  const [newEmail, setNewEmail] = useState('');

  const savingPwd = modalStatus === 'saving_pwd';
  const savingEmail = modalStatus === 'saving_email';
  const accountLoading = accountStatus === 'loading';
  const savingProfile = accountStatus === 'saving_profile';
  const crewLoading = accountStatus === 'crew_loading';
  const [showCurrentPwd, setShowCurrentPwd] = useState(false);
  const [showNewPwd, setShowNewPwd] = useState(false);

  // ── Profile handlers ──────────────────────────────────────────────────────

  // Profile, Crews, and Device handlers moved to hooks

  // ── Notification prefs ────────────────────────────────────────────────────

  const _handleSaveNotifPrefs = (prefs: unknown) => saveNotifPrefs(prefs as Parameters<typeof saveNotifPrefs>[0]);

  const handleChangePassword = async () => {
    if (isProcessingRef.current) return;
    setSecurityMsg(null);
    if (!newPwd || newPwd.length < 8) {
      setSecurityMsg({ type: 'error', text: 'New password must be at least 8 characters' });
      return;
    }
    if (newPwd !== confirmPwd) {
      setSecurityMsg({ type: 'error', text: 'Passwords do not match' });
      return;
    }
    isProcessingRef.current = true;
    setModalStatus('saving_pwd');
    try {
      const { error: reAuthError } = await authSignIn(userEmail || '', currentPwd);
      if (reAuthError) {
        setSecurityMsg({ type: 'error', text: 'Current password is incorrect' });
        setCurrentPwd('');
        setModalStatus('idle');
        return;
      }
      
      const { error } = await updateUser({ password: newPwd });
      if (error) throw error;
      setSecurityMsg({ type: 'success', text: 'Password updated successfully' });
      setCurrentPwd(''); setNewPwd(''); setConfirmPwd('');
      setModalStatus('idle');
    } catch (e: unknown) {
      setSecurityMsg({ type: 'error', text: (e instanceof Error ? e.message : String(e)) || 'Failed to update password' });
      setModalStatus('idle');
    } finally {
      isProcessingRef.current = false;
    }
  };

  const handleChangeEmail = async () => {
    if (!/.+@.+\..+/.test(newEmail)) {
      setSecurityMsg({ type: 'error', text: 'Enter a valid email address' });
      return;
    }
    if (isProcessingRef.current) return;
    isProcessingRef.current = true;
    setModalStatus('saving_email');
    try {
      const { error } = await updateUser({ email: newEmail });
      if (error) throw error;
      setSecurityMsg({ type: 'success', text: `✓ Confirmation sent to ${newEmail}. Check your inbox.` });
      setNewEmail('');
      setModalStatus('idle');
    } catch (err: unknown) {
      const e = err instanceof Error ? err : new Error((err instanceof Error ? err.message : String(err)));
      setSecurityMsg({ type: 'error', text: e.message || 'Could not update email' });
      setModalStatus('idle');
    } finally {
      isProcessingRef.current = false;
    }
  };

  // ── Sign Out ──────────────────────────────────────────────────────────────

  const handleSignOut = async () => {
    const doSignOut = async () => {
      if (isProcessingRef.current) return;
      isProcessingRef.current = true;
      try {
        await authSignOut();
        onSignOut();
        onClose();
      } catch (err: unknown) {
        AppLogger.error('Sign out failed', err instanceof Error ? err : new Error(String(err)), { category: 'ACCOUNT_MGMT', payload_size: 0, ssi: 0 });
      } finally {
        isProcessingRef.current = false;
      }
    };

    const action = Platform.select({
      web: () => {
        void doSignOut();
      },
      default: () => {
        Alert.alert('Sign Out', 'Sign out of your SK8Lytz account?', [
          { text: 'Cancel', style: 'cancel' },
          {
            text: 'Sign Out', style: 'destructive',
            onPress: doSignOut,
          },
        ]);
      }
    });
    if (action) action();
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
              'Tap Delete to permanently remove your account.',
              [
                { text: 'Cancel', style: 'cancel' },
                {
                  text: 'Confirm Delete', style: 'destructive',
                  onPress: async () => {
                    try {
                      setModalStatus('deleting');
                      const { error } = await supabase.rpc('delete_account');
                      if (error) {
                        AppLogger.error(
                          'delete_account_rpc_failed',
                          error instanceof Error ? error : new Error(String(error)),
                          { category: 'ACCOUNT_MGMT', payload_size: 0, ssi: 0 }
                        );
                        throw new Error('Database rejection. Please contact support.');
                      }

                      Alert.alert(
                        'Account Deleted',
                        'Your account and all associated data have been permanently erased.',
                      );
                      await authSignOut();
                      onSignOut();
                      onClose();
                    } catch (err: unknown) {
                      const e = err instanceof Error ? err : new Error((err instanceof Error ? err.message : String(err)));
                      Alert.alert('Deletion Failed', e.message);
                      setModalStatus('idle');
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
          <TouchableOpacity style={[styles.closeBtn, { top: Math.max(insets.top + 16, 16) }]} onPress={onClose}>
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
                  name={t.icon as keyof typeof MaterialCommunityIcons.glyphMap}
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
              currentPwd, setCurrentPwd, showCurrentPwd, setShowCurrentPwd,
              newPwd, setNewPwd, confirmPwd, setConfirmPwd, showNewPwd, setShowNewPwd, savingPwd,
              handleChangePassword, newEmail, setNewEmail, savingEmail, handleChangeEmail,
              crewStep, setCrewStep, crewError, setCrewError, newCrewName, setNewCrewName, crewLoading,
              handleCreateCrew, joinCode, setJoinCode, handleJoinCrew, handleDeleteCrew, handleLeaveCrew,
              groupedDevices, editingGroupId, groupNewName, setGroupNewName, handleRenameGroup, setEditingGroupId,
              handleForgetGroup, editingDeviceId, deviceNewName, setDeviceNewName, handleRenameDevice, setEditingDeviceId,
              handleForgetDevice,
              notifCrewInvites, setNotifCrewInvites, notifSessionReminders, setNotifSessionReminders,
              notifLeaderHandoff, setNotifLeaderHandoff, saveNotifPrefs, isDark, toggleTheme, setShowEula,
              handleSignOut, handleDeleteAccount, statsLoading, lifetimeStats, recentSessions,
              healthSyncEnabled, handleToggleHealthSync: (val: boolean) => handleToggleHealthSync(val),
              autoPauseEnabled, handleToggleAutoPause,
            };

            if (accountLoading) return <AccountModalSkeleton />;
            if (accountError) return <View style={{ padding: Spacing.xl }}><ErrorCard message={accountError} onRetry={loadData} /></View>;
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
    </Modal>
  );
}

// ─── Styles ───────────────────────────────────────────────────────────────────





