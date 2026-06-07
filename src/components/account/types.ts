import { Tables } from '../../types/supabase';
import { PermanentCrew } from '../../services/ProfileService';
import { SessionData } from '../../services/TelemetryService.types';
import { RegisteredDeviceRow } from '../../types/ble.types';

export interface BaseTabProps {
  Colors: Record<string, string>;
  styles: Record<string, object>;
}

export interface AccountTabProfileProps extends BaseTabProps {
  profilePhotoUri: string | null;
  profile: Tables<'profile'> | null;
  setProfile: (update: (prev: Tables<'profile'> | null) => Tables<'profile'> | null) => void;
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
  history: SessionData[];
  devices: RegisteredDeviceRow[];
  setShowEula: (val: boolean) => void;
  handleSignOut: () => Promise<void>;
}

export interface AccountTabCrewzProps extends BaseTabProps {
  crewStep: 'list' | 'create' | 'join' | 'manage';
  setCrewStep: (val: 'list' | 'create' | 'join' | 'manage') => void;
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
  handleDeleteCrew: (crew: PermanentCrew) => Promise<void>;
  handleLeaveCrew: (crew: PermanentCrew) => Promise<void>;
}

export interface AccountTabDevicesProps extends BaseTabProps {
  devices: RegisteredDeviceRow[];
  groupedDevices: Record<string, RegisteredDeviceRow[]>;
  editingGroupId: string | null;
  groupNewName: string;
  setGroupNewName: (val: string) => void;
  handleRenameGroup: (oldName: string) => Promise<void>;
  setEditingGroupId: (val: string | null) => void;
  handleForgetGroup: (groupName: string) => Promise<void>;
  editingDeviceId: string | null;
  deviceNewName: string;
  setDeviceNewName: (val: string) => void;
  handleRenameDevice: (device: RegisteredDeviceRow) => Promise<void>;
  setEditingDeviceId: (val: string | null) => void;
  setAdvancedModalDevice: (val: RegisteredDeviceRow | null) => void;
  setAdvancedModalVisible: (val: boolean) => void;
  handleForgetDevice: (device: RegisteredDeviceRow) => Promise<void>;
}

export interface AccountTabStatsProps extends BaseTabProps {
  loadingStats: boolean;
  history: SessionData[];
  formatDate: (iso: string) => string;
}

export interface AccountTabSettingsProps extends BaseTabProps {
  isPushEnabled: boolean;
  handleTogglePush: () => Promise<void>;
  hasHealthConnect: boolean;
  isHealthEnabled: boolean;
  toggleHealthConnect: () => Promise<void>;
  isDark: boolean;
  toggleTheme: () => void;
}

export interface AccountTabSecurityProps extends BaseTabProps {
  currentPwd?: string;
  setCurrentPwd: (val: string) => void;
  showCurrentPwd?: boolean;
  setShowCurrentPwd: (val: boolean) => void;
  newPwd?: string;
  setNewPwd: (val: string) => void;
  confirmPwd?: string;
  setConfirmPwd: (val: string) => void;
  showNewPwd?: boolean;
  setShowNewPwd: (val: boolean) => void;
  savingPwd?: boolean;
  handleChangePassword: () => Promise<void>;
  userEmail?: string;
  newEmail?: string;
  setNewEmail: (val: string) => void;
  savingEmail?: boolean;
  handleChangeEmail?: () => Promise<void>;
}
