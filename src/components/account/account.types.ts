import React from 'react';
import { PermanentCrew, UserProfile } from '../../services/ProfileService';
import { ILifetimeStats, ISkateSession } from '../../services/SpeedTrackingService';
import { SessionHistoryItem } from '../../services/ProfileService';

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

export interface BaseTabProps {
  Colors: {
    background: string;
    surface: string;
    surfaceHighlight: string;
    primary: string;
    secondary: string;
    accent: string;
    text: string;
    textMuted: string;
    textDim: string;
    border: string;
    success: string;
    error: string;
    warning: string;
    isDark: boolean;
  };
  styles: Record<string, import('react-native').ViewStyle | import('react-native').TextStyle | import('react-native').ImageStyle>;
}

export interface AccountTabProfileProps extends BaseTabProps {
  profilePhotoUri: string | null;
  profile: UserProfile | null;
  setProfile: React.Dispatch<React.SetStateAction<UserProfile | null>>;
  avatarHue: number;
  setAvatarHue: (val: number) => void;
  userEmail: string;
  editName: string;
  setEditName: (val: string) => void;
  editUsername: string;
  setEditUsername: (val: string) => void;
  savingProfile: boolean;
  handleSaveProfile: () => Promise<void>;
  handlePickProfilePhoto: () => Promise<void>;
  crews: PermanentCrew[];
  history: SessionHistoryItem[];
  devices: StoredDevice[];
  setShowEula: (val: boolean) => void;
  handleSignOut: () => Promise<void>;
}

export interface AccountTabCrewzProps extends BaseTabProps {
  crewStep: 'list' | 'create' | 'join' | 'manage';
  setCrewStep: React.Dispatch<React.SetStateAction<'list' | 'create' | 'join' | 'manage'>>;
  crewError: string;
  setCrewError: (val: string) => void;
  newCrewName: string;
  setNewCrewName: (val: string) => void;
  crewLoading: boolean;
  handleCreateCrew: () => Promise<void>;
  joinCode: string;
  setJoinCode: (val: string) => void;
  handleJoinCrew: () => Promise<void>;
  crews: PermanentCrew[];
  handleDeleteCrew: (crew: PermanentCrew) => void;
  handleLeaveCrew: (crew: PermanentCrew) => void;
}

export interface AccountTabDevicesProps extends BaseTabProps {
  devices: StoredDevice[];
  groupedDevices: Record<string, StoredDevice[]>;
  editingGroupId: string | null;
  groupNewName: string;
  setGroupNewName: (val: string) => void;
  handleRenameGroup: (oldName: string) => void;
  setEditingGroupId: (val: string | null) => void;
  handleForgetGroup: (groupName: string) => void;
  editingDeviceId: string | null;
  deviceNewName: string;
  setDeviceNewName: (val: string) => void;
  handleRenameDevice: (device: StoredDevice) => void;
  setEditingDeviceId: (val: string | null) => void;
  handleForgetDevice: (device: StoredDevice) => void;
}

export interface AccountTabStatsProps extends BaseTabProps {
  statsLoading: boolean;
  statsError?: string | null;
  lifetimeStats: ILifetimeStats | null;
  recentSessions: ISkateSession[];
  crews: PermanentCrew[];
  history: SessionHistoryItem[];
  devices: StoredDevice[];
}

export interface AccountTabSettingsProps extends BaseTabProps {
  notifCrewInvites: boolean;
  setNotifCrewInvites: (val: boolean) => void;
  notifSessionReminders: boolean;
  setNotifSessionReminders: (val: boolean) => void;
  notifLeaderHandoff: boolean;
  setNotifLeaderHandoff: (val: boolean) => void;
  saveNotifPrefs: (prefs: { crewInvites: boolean; sessionReminders: boolean; leaderHandoff: boolean }) => Promise<void>;
  isDark: boolean;
  toggleTheme: () => void;
  healthSyncEnabled: boolean;
  handleToggleHealthSync: (val: boolean) => void;
  autoPauseEnabled: boolean;
  handleToggleAutoPause: (val: boolean) => void;
  setShowEula: (val: boolean) => void;
  handleDeleteAccount: () => void;
}

export interface AccountTabSecurityProps extends BaseTabProps {
  currentPwd?: string;
  setCurrentPwd: (val: string) => void;
  showCurrentPwd: boolean;
  setShowCurrentPwd: (val: boolean) => void;
  newPwd?: string;
  setNewPwd: (val: string) => void;
  confirmPwd?: string;
  setConfirmPwd: (val: string) => void;
  showNewPwd: boolean;
  setShowNewPwd: (val: boolean) => void;
  savingPwd: boolean;
  handleChangePassword: () => Promise<void>;
  userEmail: string;
  newEmail?: string;
  setNewEmail: (val: string) => void;
  savingEmail: boolean;
  handleChangeEmail: () => Promise<void>;
}
