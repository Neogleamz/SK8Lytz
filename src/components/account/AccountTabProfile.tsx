import { AccountTabProfileProps } from './types';
import React from 'react';
import { View, Text, ScrollView, TouchableOpacity, Image, TextInput, ActivityIndicator } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import CustomSlider from '../CustomSlider';
import { Spacing } from '../../theme/theme';

function initials(name: string | null) {
  if (!name) return '?';
  return name.split(' ').map(w => w[0]).join('').slice(0, 2).toUpperCase();
}

function formatDate(iso: string) {
  if (!iso) return '';
  return new Date(iso).toLocaleDateString(undefined, { month: 'short', day: 'numeric', year: 'numeric' });
}

export default function AccountTabProfile({
  Colors,
  styles,
  profilePhotoUri,
  profile,
  setProfile,
  avatarHue,
  setAvatarHue,
  userEmail,
  editName,
  setEditName,
  editUsername,
  setEditUsername,
  savingProfile,
  handleSaveProfile,
  handlePickProfilePhoto,
  crews,
  history,
  devices,
  setShowEula,
  handleSignOut,
}: AccountTabProfileProps) {
  return (
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
            setProfile((p: import('../../types/supabase').Tables<'profile'> | null) => p ? { ...p, avatar_color: newHex } : p);
          }}
          // onSlidingComplete: no-op — color is already staged in profile.avatar_color
          // via onValueChange → setProfile above. Persisted to Supabase on "Save Profile" press.
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



      {/* Legal & Compliance */}
      <Text style={[styles.sectionHeader, { marginTop: Spacing.xl }]}>LEGAL</Text>

      <TouchableOpacity style={styles.signOutBtn} onPress={() => setShowEula(true)}>
        <MaterialCommunityIcons name="file-document-outline" size={18} color={Colors.textMuted} />
        <Text style={[styles.signOutText, { color: Colors.text }]}>Review EULA</Text>
      </TouchableOpacity>

      {/* Sign Out */}
      <TouchableOpacity style={[styles.primaryBtn, { backgroundColor: Colors.error, marginTop: Spacing.xl }]}
        onPress={handleSignOut}>
        <MaterialCommunityIcons name="logout" size={16} color="#FFF" />
        <Text style={[styles.primaryBtnText, { color: '#FFF' }]}>Sign Out</Text>
      </TouchableOpacity>

      <View style={{ height: 20 }} />
    </ScrollView>
  );
}
