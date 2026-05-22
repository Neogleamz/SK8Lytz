import AsyncStorage from '@react-native-async-storage/async-storage';
import * as ImagePicker from 'expo-image-picker';
import { useCallback, useEffect, useState } from 'react';
import { Alert } from 'react-native';
import { STORAGE_PREFIX } from '../constants/AppConstants';
import { AppLogger } from '../services/AppLogger';
import { PermanentCrew, profileService, SessionHistoryItem, UserProfile } from '../services/ProfileService';
import { supabase } from '../services/supabaseClient';
import * as FileSystem from 'expo-file-system';
import { decode } from 'base64-arraybuffer';
import { checkPermission, requestPermission, setPermissionOptOut } from '../services/PermissionService';

const NOTIF_PREF_KEY = `${STORAGE_PREFIX}notif_prefs`;

function hexToHue(hex: string | null | undefined): number {
  if (!hex) return 30; // default orange
  let r = 0, g = 0, b = 0;
  if (hex.length === 4) {
    r = parseInt(hex[1] + hex[1], 16);
    g = parseInt(hex[2] + hex[2], 16);
    b = parseInt(hex[3] + hex[3], 16);
  } else if (hex.length === 7) {
    r = parseInt(hex.substring(1, 3), 16);
    g = parseInt(hex.substring(3, 5), 16);
    b = parseInt(hex.substring(5, 7), 16);
  }
  r /= 255; g /= 255; b /= 255;
  const max = Math.max(r, g, b), min = Math.min(r, g, b);
  if (max === min) return 0;
  let h = 0;
  if (max === r) { h = (60 * ((g - b) / (max - min)) + 360) % 360; }
  else if (max === g) { h = (60 * ((b - r) / (max - min)) + 120) % 360; }
  else if (max === b) { h = (60 * ((r - g) / (max - min)) + 240) % 360; }
  return h;
}

export function useAccountOverview(visible: boolean, onProfileUpdated?: () => void) {
  const [loading, setLoading] = useState(false);
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [editName, setEditName] = useState('');
  const [editUsername, setEditUsername] = useState('');
  const [savingProfile, setSavingProfile] = useState(false);
  const [profilePhotoUri, setProfilePhotoUri] = useState<string | null>(null);
  const [avatarHue, setAvatarHue] = useState<number>(30);
  const [userEmail, setUserEmail] = useState('');

  // Crews state
  const [crews, setCrews] = useState<PermanentCrew[]>([]);
  const [crewStep, setCrewStep] = useState<'list' | 'create' | 'join'>('list');
  const [newCrewName, setNewCrewName] = useState('');
  const [joinCode, setJoinCode] = useState('');
  const [crewLoading, setCrewLoading] = useState(false);
  const [crewError, setCrewError] = useState('');

  // History
  const [history, setHistory] = useState<SessionHistoryItem[]>([]);

  // Notifications
  const [notifCrewInvites, setNotifCrewInvites] = useState(true);
  const [notifSessionReminders, setNotifSessionReminders] = useState(true);
  const [notifLeaderHandoff, setNotifLeaderHandoff] = useState(true);

  // Health Sync
  const [healthSyncEnabled, setHealthSyncEnabled] = useState(false);

  const loadData = useCallback(async () => {
    setLoading(true);
    try {
      AppLogger.log('ACCOUNT_MODAL_LOAD_START');

      // ── Phase A: Run auth lookup + notif prefs in parallel ──────────────────
      // Neither depends on the other, so fire both immediately.
      const [authResult, rawNotifPrefs, hasHealth] = await Promise.all([
        supabase.auth.getUser(),
        AsyncStorage.getItem(NOTIF_PREF_KEY).catch(e => {
          AppLogger.warn('Failed to load notification preferences from cache', e);
          return null;
        }),
        checkPermission('HEALTH'),
      ]);

      const user = authResult.data?.user ?? null;
      if (user) setUserEmail(user.email ?? '');

      // Apply notif prefs as soon as they resolve (Phase A complete)
      if (rawNotifPrefs) {
        const prefs = JSON.parse(rawNotifPrefs);
        setNotifCrewInvites(prefs.crewInvites ?? true);
        setNotifSessionReminders(prefs.sessionReminders ?? true);
        setNotifLeaderHandoff(prefs.leaderHandoff ?? true);
      }
      setHealthSyncEnabled(hasHealth);

      // ── Phase B: Fan-out all profile service calls in parallel ───────────────
      // Pass the already-resolved user/userId to avoid redundant auth token lookups.
      // Isolation: each query has its own .catch() so a single failure (e.g. profile
      // not yet created) doesn't nuke crew/history loading (mirrors useCrewHub fix).
      const [p, c, h] = await Promise.all([
        profileService.fetchOrCreateProfile(user).catch((e) => {
          AppLogger.warn('[AccountOverview] fetchOrCreateProfile failed', { error: String(e) });
          return null;
        }),
        profileService.getMyCrew(undefined, user?.id).catch((e) => {
          AppLogger.warn('[AccountOverview] getMyCrew failed', { error: String(e) });
          return [] as import('../services/ProfileService').PermanentCrew[];
        }),
        profileService.getSessionHistory(user?.id).catch((e) => {
          AppLogger.warn('[AccountOverview] getSessionHistory failed', { error: String(e) });
          return [] as import('../services/ProfileService').SessionHistoryItem[];
        }),
      ]);
      AppLogger.log('ACCOUNT_MODAL_DATA_RESOLVED', { hasProfile: !!p, crewCount: c?.length || 0, historyCount: h?.length || 0 });
      if (p) {
        setProfile(p);
        setEditName(p.display_name ?? '');
        setEditUsername(p.username ?? '');
        if (p.avatar_url) setProfilePhotoUri(p.avatar_url);
        if (p.avatar_color) setAvatarHue(hexToHue(p.avatar_color));
      }
      setCrews(c);
      setHistory(h);
    } catch (e) {
      AppLogger.warn('[AccountOverview] loadData error', { error: String(e) });
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    if (visible) {
      loadData();
    }
  }, [visible, loadData]);

  const handleSaveProfile = async () => {
    if (!editName.trim()) return;
    setSavingProfile(true);
    try {
      const updates: Partial<UserProfile> = { display_name: editName.trim() };
      if (editUsername.trim()) updates.username = editUsername.trim().toLowerCase();
      // Persist staged avatar_color from the slider (set locally via setProfile during drag)
      if (profile?.avatar_color) updates.avatar_color = profile.avatar_color;
      await profileService.updateProfile(updates);
      setProfile(p => p ? { ...p, ...updates } : p);
      AppLogger.log('PROFILE_UPDATED', { fields: Object.keys(updates) });
      onProfileUpdated?.();
      Alert.alert('Saved', 'Profile updated successfully.');
    } catch (e: any) {
      AppLogger.error('[AccountOverview] handleSaveProfile failed', { error: String(e) });
      Alert.alert('Error', e.message || 'Could not save profile');
    } finally {
      setSavingProfile(false);
    }
  };


  const handlePickProfilePhoto = async () => {
    const { status } = await ImagePicker.requestMediaLibraryPermissionsAsync();
    if (status !== 'granted') {
      Alert.alert('Permission needed', 'Enable photo library access in Settings to set a profile photo.');
      return;
    }
    const result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.Images,
      allowsEditing: true, aspect: [1, 1], quality: 0.7,
    });
    if (result.canceled || !result.assets[0]) return;

    const asset = result.assets[0];
    setProfilePhotoUri(asset.uri);

    try {
      const { data: { user } } = await supabase.auth.getUser();
      if (!user) throw new Error('Not authenticated');

      const ext = asset.uri.split('.').pop()?.toLowerCase() ?? 'jpg';
      const path = `${user.id}/avatar.${ext}`;

      const base64 = await FileSystem.readAsStringAsync(asset.uri, { encoding: 'base64' });

      const { error: uploadError } = await supabase.storage
        .from('avatars')
        .upload(path, decode(base64), { contentType: `image/${ext}`, upsert: true });

      if (uploadError) throw uploadError;

      const { data: { publicUrl } } = supabase.storage.from('avatars').getPublicUrl(path);

      await profileService.updateProfile({ avatar_url: publicUrl });
      setProfile(p => p ? { ...p, avatar_url: publicUrl } : p);
      AppLogger.log('PROFILE_UPDATED', { field: 'photo', bucket: 'avatars', path });
      onProfileUpdated?.();
    } catch (e: any) {
      AppLogger.error('[AccountOverview] handlePickProfilePhoto failed', { error: String(e) });
      Alert.alert('Upload failed', e.message ?? 'Could not upload photo. Try again.');
    }
  };

  const saveNotifPrefs = async (prefs: { crewInvites: boolean; sessionReminders: boolean; leaderHandoff: boolean }) => {
    await AsyncStorage.setItem(NOTIF_PREF_KEY, JSON.stringify(prefs)).catch(e => AppLogger.warn('Failed to persist notification preferences', e));
  };

  const handleToggleHealthSync = async (enabled: boolean) => {
    if (enabled) {
      const granted = await requestPermission('HEALTH');
      if (granted) {
        await setPermissionOptOut('HEALTH', false);
        setHealthSyncEnabled(true);
      } else {
        // Automatically flips back to false if denied
        setHealthSyncEnabled(false);
      }
    } else {
      await setPermissionOptOut('HEALTH', true);
      setHealthSyncEnabled(false);
    }
  };

  // Crew handlers
  const handleCreateCrew = async () => {
    if (!newCrewName.trim()) { setCrewError('Enter a crew name'); return; }
    setCrewLoading(true); setCrewError('');
    try {
      const crew = await profileService.createPermanentCrew(newCrewName.trim());
      setCrews(prev => [...prev, crew]);
      setNewCrewName(''); setCrewStep('list');
      AppLogger.log('CREW_PERMANENT_CREATED', { crewName: newCrewName.trim() });
    } catch (e: any) {
      AppLogger.warn('[AccountOverview] handleCreateCrew failed', { error: String(e) });
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
      AppLogger.warn('[AccountOverview] handleJoinCrew failed', { error: String(e) });
      setCrewError(e.message ?? 'Failed to join crew');
    } finally { setCrewLoading(false); }
  };

  const handleLeaveCrew = async (crewId: string) => {
    try {
      await profileService.leavePermanentCrew(crewId);
      setCrews(prev => prev.filter(c => c.id !== crewId));
      AppLogger.log('CREW_PERMANENT_LEFT', { crewId });
    } catch (e: any) { 
      AppLogger.error('[AccountOverview] handleLeaveCrew failed', { crewId, error: String(e) });
      Alert.alert('Error', e.message); 
    }
  };

  return {
    loading,
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
    handleLeaveCrew,
    healthSyncEnabled,
    handleToggleHealthSync,
  };
}
