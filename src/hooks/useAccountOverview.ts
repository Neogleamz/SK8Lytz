import AsyncStorage from '@react-native-async-storage/async-storage';
import * as ImagePicker from 'expo-image-picker';
import { useCallback, useEffect, useState } from 'react';
import { Alert } from 'react-native';
import { STORAGE_PREFIX } from '../constants/AppConstants';
import { AppLogger } from '../services/AppLogger';
import { PermanentCrew, profileService, SessionHistoryItem, UserProfile } from '../services/ProfileService';
import { supabase } from '../services/supabaseClient';

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

export function useAccountOverview(visible: boolean) {
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

  const loadData = useCallback(async () => {
    setLoading(true);
    try {
      AppLogger.log('ACCOUNT_MODAL_LOAD_START');
      const { data: { user } } = await supabase.auth.getUser();
      if (user) setUserEmail(user.email ?? '');

      // Pass the cached user/userId to avoid redundant auth token lookups
      const [p, c, h] = await Promise.all([
        profileService.fetchOrCreateProfile(user),
        profileService.getMyCrew(undefined, user?.id),
        profileService.getSessionHistory(user?.id),
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

      const raw = await AsyncStorage.getItem(NOTIF_PREF_KEY);
      if (raw) {
        const prefs = JSON.parse(raw);
        setNotifCrewInvites(prefs.crewInvites ?? true);
        setNotifSessionReminders(prefs.sessionReminders ?? true);
        setNotifLeaderHandoff(prefs.leaderHandoff ?? true);
      }
    } catch (e) {
      console.warn('[useAccountOverview] loadData error:', e);
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

      const response = await fetch(asset.uri);
      const blob = await response.blob();
      const ext = asset.uri.split('.').pop()?.toLowerCase() ?? 'jpg';
      const path = `${user.id}/avatar.${ext}`;

      const { error: uploadError } = await supabase.storage
        .from('avatars')
        .upload(path, blob, { contentType: `image/${ext}`, upsert: true });

      if (uploadError) throw uploadError;

      const { data: { publicUrl } } = supabase.storage.from('avatars').getPublicUrl(path);

      await profileService.updateProfile({ avatar_url: publicUrl });
      setProfile(p => p ? { ...p, avatar_url: publicUrl } : p);
      AppLogger.log('PROFILE_UPDATED', { field: 'photo', bucket: 'avatars', path });
    } catch (e: any) {
      Alert.alert('Upload failed', e.message ?? 'Could not upload photo. Try again.');
    }
  };

  const saveNotifPrefs = async (prefs: { crewInvites: boolean; sessionReminders: boolean; leaderHandoff: boolean }) => {
    await AsyncStorage.setItem(NOTIF_PREF_KEY, JSON.stringify(prefs)).catch(() => {});
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

  const handleLeaveCrew = async (crewId: string) => {
    try {
      await profileService.leavePermanentCrew(crewId);
      setCrews(prev => prev.filter(c => c.id !== crewId));
      AppLogger.log('CREW_PERMANENT_LEFT', { crewId });
    } catch (e: any) { Alert.alert('Error', e.message); }
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
  };
}
