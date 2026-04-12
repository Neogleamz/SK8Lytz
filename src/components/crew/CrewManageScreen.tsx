import React, { useRef, useEffect } from 'react';
import { View, Text, TouchableOpacity, ScrollView, Animated, ActivityIndicator, Alert, Share, TextInput, Image, RefreshControl, Platform } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../../context/ThemeContext';
import * as Clipboard from 'expo-clipboard';
import { profileService, PermanentCrew } from '../../services/ProfileService';
import { crewService, CrewSession } from '../../services/CrewService';
import { locationService } from '../../services/LocationService';
import { AppLogger } from '../../services/AppLogger';
import DateTimePicker from '@react-native-community/datetimepicker';
import * as ImagePicker from 'expo-image-picker';
import CustomSlider from '../CustomSlider';

import { createStyles } from './CrewStyles';
import { useCrewContext } from '../../context/CrewContext';
// TODO: any other specific imports check manually

const AVATAR_ICONS = ['account-group', 'star', 'fire', 'lightning-bolt', 'skull', 'heart', 'basketball', 'music-note', 'rocket', 'ghost', 'robot', 'alien'];

function timeAgo(iso: string): string {
  const diff = Date.now() - new Date(iso).getTime();
  const m = Math.floor(diff / 60000);
  if (m < 1) return 'just now';
  if (m < 60) return `${m}m ago`;
  return `${Math.floor(m / 60)}h ago`;
}

export function CrewManageScreen() {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const context = useCrewContext();
  const { hub, manage, session, setStep, step, confirmAction, setConfirmAction, currentUserId, displayName, errorMsg, setErrorMsg, isLoading, setIsLoading, showCodeEntry, setShowCodeEntry, formState } = context;
  const { activeSessions, myCrews, permanentCrews, isLoadingNearby, refreshNearby, nearbySessions, discoverRadiusMi, setDiscoverRadiusMi, locationLabel, handleDetectLocation, isGettingLocation } = hub;
  const { selectedCrewDetail, setSelectedCrewDetail, expandedCrewId, setExpandedCrewId, cardMembers, setCardMembers, loadingCardMembersFor, makingOwnerFor, setMakingOwnerFor, confirmingDeleteCrewId, setConfirmingDeleteCrewId, confirmingLeaveCrewId, setConfirmingLeaveCrewId, createCrewError, setCreateCrewError, isCreatingCrew, setIsCreatingCrew, newCrewName, setNewCrewName, newCrewDescription, setNewCrewDescription, newCrewIsPublic, setNewCrewIsPublic, newCrewCity, setNewCrewCity, newCrewState, setNewCrewState, newCrewPhotoUri, setNewCrewPhotoUri, newCrewIcon, setNewCrewIcon, newCrewColor, setNewCrewColor, newCrewHue, setNewCrewHue, newCrewCode, setNewCrewCode, selectedMembers, setSelectedMembers, userSearchQuery, setUserSearchQuery, userSearchResults, setUserSearchResults } = manage;
  const { currentSession, isHandoffMode, executeLeaveSession, executeEndSession, handleHandoffLeadership } = session;
  
  const handlePickCrewPhoto = async () => {
    let result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.Images,
      allowsEditing: true,
      aspect: [1, 1],
      quality: 0.5,
    });
    if (!result.canceled && result.assets[0]) {
      setNewCrewPhotoUri(result.assets[0].uri);
    }
  };

  const handleCreateCrew = async () => {
    if (!newCrewName.trim()) { setCreateCrewError('Enter a crew name'); return; }
    setIsCreatingCrew(true); setCreateCrewError('');
    try {
      const crew = await profileService.createPermanentCrew(newCrewName.trim(), {
        isPublic: newCrewIsPublic,
        avatarColor: newCrewColor,
        avatarIcon: newCrewIcon,
        city: newCrewCity || undefined,
        state: newCrewState || undefined,
        description: newCrewDescription || undefined,
        inviteCode: !newCrewIsPublic ? newCrewCode : undefined,
        members: selectedMembers.map((m: any) => m.user_id)
      });
      AppLogger.log('CREW_PERMANENT_CREATED', {
        crewId: crew.id, crewName: crew.name, isPublic: newCrewIsPublic,
        city: newCrewCity || null
      });
      const updated = await profileService.getMyCrew();
      hub.setMyCrews(updated);
      hub.setPermanentCrews(updated.map((c: any) => ({ id: c.id, name: c.name })));
      
      formState.setSelectedCrewId(crew.id);
      formState.setCrewName(crew.name);
      setStep('landing');
    } catch (e: any) {
      setCreateCrewError(e.message || 'Failed to create crew');
    } finally {
      setIsCreatingCrew(false);
    }
  };

  return (
      <View style={{ flex: 1 }}>
        {/* Header */}
        <TouchableOpacity onPress={() => setStep('landing')} style={styles.backBtn}>
          <MaterialCommunityIcons name="chevron-left" size={22} color={Colors.textMuted} />
          <Text style={styles.backText}>Back</Text>
        </TouchableOpacity>
        <Text style={styles.titleLarge}>Create a Crew</Text>

        <ScrollView showsVerticalScrollIndicator={false} keyboardShouldPersistTaps="handled"
          contentContainerStyle={{ padding: 16, paddingBottom: 60 }}>

          <Text style={styles.label}>CREW NAME</Text>
          <TextInput style={styles.input} value={newCrewName} onChangeText={setNewCrewName}
            placeholder="e.g. Friday Night Shredders" placeholderTextColor={Colors.textMuted} maxLength={40} />

          <Text style={styles.label}>DESCRIPTION (OPTIONAL)</Text>
          <TextInput style={[styles.input, { height: 72, textAlignVertical: 'top' }]}
            value={newCrewDescription} onChangeText={setNewCrewDescription} multiline
            placeholder="What's this crew about?" placeholderTextColor={Colors.textMuted} maxLength={120} />

          {/* Unified Avatar Selection */}
          <View style={{ marginBottom: 16 }}>
            <Text style={styles.label}>CREW AVATAR</Text>

            {newCrewPhotoUri ? (
              <View style={{ alignItems: 'center', marginBottom: 8, padding: 16, backgroundColor: 'rgba(255,255,255,0.02)', borderRadius: 12, borderWidth: 1, borderColor: 'rgba(255,255,255,0.05)' }}>
                <TouchableOpacity style={[styles.mgPhotoBtn, { height: 120, width: 120, borderRadius: 60, marginBottom: 12 }]} onPress={handlePickCrewPhoto}>
                  <Image source={{ uri: newCrewPhotoUri }} style={styles.mgPhotoBtnImg} />
                </TouchableOpacity>
                <TouchableOpacity onPress={() => setNewCrewPhotoUri(null)} style={{ paddingVertical: 8, paddingHorizontal: 16, backgroundColor: 'rgba(255,68,68,0.1)', borderRadius: 20 }}>
                  <Text style={{ color: '#FF4444', fontWeight: 'bold' }}>Remove Photo</Text>
                </TouchableOpacity>
              </View>
            ) : (
              <View style={{ backgroundColor: 'rgba(255,255,255,0.02)', padding: 16, borderRadius: 12, borderWidth: 1, borderColor: 'rgba(255,255,255,0.05)' }}>

                <View style={{ flexDirection: 'row', alignItems: 'center', gap: 12, marginBottom: 16 }}>
                  {/* Live Avatar Preview */}
                  <View style={{ width: 60, height: 60, borderRadius: 30, backgroundColor: newCrewColor, alignItems: 'center', justifyContent: 'center', borderWidth: 2, borderColor: 'rgba(255,255,255,0.2)' }}>
                    <MaterialCommunityIcons name={newCrewIcon as any} size={38} color="#000" />
                  </View>

                  {/* Photo Upload Option */}
                  <TouchableOpacity style={[styles.mgPhotoBtn, { height: 60, flex: 1, marginBottom: 0 }]} onPress={handlePickCrewPhoto}>
                    <MaterialCommunityIcons name="camera-plus" size={22} color={Colors.primary} />
                    <Text style={styles.mgPhotoBtnText}>Upload Photo instead</Text>
                  </TouchableOpacity>
                </View>

                <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 16 }}>
                  <View style={{ flex: 1, height: 1, backgroundColor: 'rgba(255,255,255,0.1)' }} />
                  <Text style={[styles.label, { marginHorizontal: 10, marginTop: 0, color: 'rgba(255,255,255,0.3)' }]}>OR PICK AN ICON</Text>
                  <View style={{ flex: 1, height: 1, backgroundColor: 'rgba(255,255,255,0.1)' }} />
                </View>

                {/* Avatar Icon */}
                <View style={styles.mgIconRow}>
                  {AVATAR_ICONS.map(ic => (
                    <TouchableOpacity key={ic} onPress={() => setNewCrewIcon(ic)}
                      style={[styles.mgIconBtn, newCrewIcon === ic && { backgroundColor: newCrewColor }]}>
                      <MaterialCommunityIcons name={ic as any} size={22} color={newCrewIcon === ic ? '#000' : Colors.textMuted} />
                    </TouchableOpacity>
                  ))}
                </View>

                {/* Hue Slider for Icon Color */}
                <Text style={[styles.label, { marginTop: 16, marginBottom: 8 }]}>ICON COLOR</Text>
                <View style={[styles.controlRow, { flexShrink: 0, minHeight: 40 }]}>
                  <CustomSlider
                    gradientTrack={true}
                    value={newCrewHue}
                    onValueChange={(hue) => {
                      setNewCrewHue(hue);
                      const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                      const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0").toUpperCase()).join("");
                      setNewCrewColor(rgb2hex(f(5), f(3), f(1)));
                    }}
                    minimumValue={0}
                    maximumValue={360}
                    style={{ flex: 1 }}
                  />
                </View>
              </View>
            )}
          </View>

          {/* Public / Private */}
          <Text style={styles.label}>VISIBILITY</Text>
          <View style={styles.visibilityRow}>
            <TouchableOpacity style={[styles.visibilityBtn, !newCrewIsPublic && styles.visibilityBtnActive]}
              onPress={() => setNewCrewIsPublic(false)}>
              <MaterialCommunityIcons name="lock" size={15} color={!newCrewIsPublic ? '#000' : Colors.textMuted} />
              <Text style={[styles.visibilityBtnText, !newCrewIsPublic && styles.visibilityBtnTextActive]}>Private</Text>
            </TouchableOpacity>
            <TouchableOpacity style={[styles.visibilityBtn, newCrewIsPublic && styles.visibilityBtnPublic]}
              onPress={() => setNewCrewIsPublic(true)}>
              <MaterialCommunityIcons name="earth" size={15} color={newCrewIsPublic ? '#000' : Colors.textMuted} />
              <Text style={[styles.visibilityBtnText, newCrewIsPublic && styles.visibilityBtnTextActive]}>Public</Text>
            </TouchableOpacity>
          </View>
          <Text style={[styles.hintText, { marginBottom: 16 }]}>
            {newCrewIsPublic ? '🌍 Anyone can find & join via Discover.' : '🔒 Invite code required to join.'}
          </Text>

          {/* Home City */}
          <Text style={styles.label}>HOME CITY (OPTIONAL)</Text>
          <View style={{ flexDirection: 'row', gap: 8, marginBottom: 16 }}>
            <TextInput style={[styles.input, { flex: 2 }]} value={newCrewCity} onChangeText={setNewCrewCity}
              placeholder="City" placeholderTextColor={Colors.textMuted} />
            <TextInput style={[styles.input, { flex: 1 }]} value={newCrewState} onChangeText={setNewCrewState}
              placeholder="State" placeholderTextColor={Colors.textMuted} />
          </View>

          {/* Add Members Section */}
          <Text style={styles.label}>ADD MEMBERS</Text>
          <View style={{ backgroundColor: 'rgba(255,255,255,0.02)', padding: 12, borderRadius: 12, borderWidth: 1, borderColor: 'rgba(255,255,255,0.05)', marginBottom: 12 }}>
            <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 6, marginBottom: selectedMembers.length > 0 ? 8 : 0 }}>
              {selectedMembers.map(member => (
                <View key={member.user_id} style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: Colors.primary || '#FFAA00', borderRadius: 16, paddingHorizontal: 10, paddingVertical: 4, gap: 4 }}>
                  <Text style={{ fontSize: 12, fontWeight: '700', color: '#000' }}>@{member.username || member.display_name}</Text>
                  <TouchableOpacity onPress={() => setSelectedMembers(prev => prev.filter(m => m.user_id !== member.user_id))}>
                    <MaterialCommunityIcons name="close-circle" size={16} color="rgba(0,0,0,0.6)" />
                  </TouchableOpacity>
                </View>
              ))}
            </View>
            <TextInput style={[styles.input, { marginBottom: 0 }]} value={userSearchQuery} onChangeText={setUserSearchQuery} placeholder="Search username..." placeholderTextColor={Colors.textMuted} />
            {userSearchResults.length > 0 && (
              <View style={{ marginTop: 8, gap: 4 }}>
                {userSearchResults.map(u => (
                  !selectedMembers.find(m => m.user_id === u.user_id) && (
                    <TouchableOpacity key={u.user_id} onPress={() => { setSelectedMembers(prev => [...prev, u]); setUserSearchQuery(''); }} style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: 'rgba(255,255,255,0.04)', padding: 8, borderRadius: 8 }}>
                      <MaterialCommunityIcons name="account" size={16} color={Colors.textMuted} style={{ marginRight: 6 }} />
                      <Text style={{ color: '#FFF', fontSize: 13, flex: 1 }}>{u.display_name} <Text style={{ color: Colors.textMuted }}>@{u.username}</Text></Text>
                      <MaterialCommunityIcons name="plus" size={18} color={Colors.primary} />
                    </TouchableOpacity>
                  )
                ))}
              </View>
            )}
          </View>

          {/* Render private code preview if private */}
          {!newCrewIsPublic && (
            <View style={{ backgroundColor: 'rgba(255,170,0,0.05)', borderRadius: 12, borderWidth: 1, borderColor: 'rgba(255,170,0,0.2)', padding: 16, alignItems: 'center', marginTop: 12, marginBottom: 8 }}>
              <Text style={{ color: 'rgba(255,170,0,0.8)', fontSize: 10, fontWeight: '800', letterSpacing: 1.5, marginBottom: 6 }}>PRIVATE INVITE CODE</Text>
              <Text style={{ color: Colors.primary || '#FFAA00', fontSize: 26, fontWeight: '900', letterSpacing: 6, fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace' }}>
                {newCrewCode}
              </Text>
              <Text style={{ color: Colors.textMuted, fontSize: 11, marginTop: 6, textAlign: 'center' }}>
                Share this code with friends so they can join your crew.
              </Text>
            </View>
          )}

          {createCrewError ? <Text style={{ color: '#FF4444', marginVertical: 8 }}>{createCrewError}</Text> : null}

          <TouchableOpacity style={[styles.primaryBtn, { marginTop: 20 }]} onPress={handleCreateCrew} disabled={isCreatingCrew}>
            {isCreatingCrew ? <ActivityIndicator size="small" color="#000" /> : <MaterialCommunityIcons name="check" size={18} color="#000" />}
            <Text style={styles.primaryBtnText}>Create Crew</Text>
          </TouchableOpacity>
        </ScrollView>
      </View>
    );
  }
