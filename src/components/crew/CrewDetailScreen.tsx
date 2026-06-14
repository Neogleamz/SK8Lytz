import { MaterialCommunityIcons } from '@expo/vector-icons';
import * as Clipboard from 'expo-clipboard';
import * as Linking from 'expo-linking';
import React from 'react';
import { ActivityIndicator, Alert, Image, ScrollView, Share, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { useTheme } from '../../context/ThemeContext';
import { AppLogger } from '../../services/AppLogger';
import { PermanentCrew, profileService } from '../../services/ProfileService';
import { Spacing } from '../../theme/theme';

import { useCrewContext } from '../../context/CrewContext';
import { createStyles } from './CrewStyles';
import { CrewDetailStats } from './CrewDetailStats';
import { CrewDetailEditForm } from './CrewDetailEditForm';


function timeAgo(iso: string): string {
  const diff = Date.now() - new Date(iso).getTime();
  const m = Math.floor(diff / 60000);
  if (m < 1) return 'just now';
  if (m < 60) return `${m}m ago`;
  return `${Math.floor(m / 60)}h ago`;
}

export function CrewDetailScreen() {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const context = useCrewContext();
  const { hub, manage, session, setStep, step, confirmAction, setConfirmAction, currentUserId, displayName, errorMsg, setErrorMsg, isLoading, setIsLoading, showCodeEntry, setShowCodeEntry, formState } = context;
  const { activeSessions, myCrews, permanentCrews, isLoadingNearby, refreshNearby, nearbySessions, discoverRadiusMi, setDiscoverRadiusMi, locationLabel, handleDetectLocation, isGettingLocation, crewMemberCounts, setCrewMemberCounts } = hub;
  const { 
    selectedCrewDetail, setSelectedCrewDetail, expandedCrewId, setExpandedCrewId, 
    cardMembers, setCardMembers, loadingCardMembersFor, makingOwnerFor, setMakingOwnerFor, 
    confirmingDeleteCrewId, setConfirmingDeleteCrewId, confirmingLeaveCrewId, setConfirmingLeaveCrewId, 
    crewStats, setCrewStats, loadCrewMembers,
    userSearchQuery, setUserSearchQuery, userSearchResults, setUserSearchResults, selectedMembers, setSelectedMembers,
    isRemovingUserFor, setIsRemovingUserFor, isAddingMembersTo, setIsAddingMembersTo,
    editingCrewId, setEditingCrewId, editCrewName, setEditCrewName, editCrewIsPublic, setEditCrewIsPublic, 
    editCrewCity, setEditCrewCity, editCrewState, setEditCrewState, editCrewDesc, setEditCrewDesc, isSavingCrew, setIsSavingCrew 
  } = manage;
  const { currentSession, isHandoffMode, executeLeaveSession, executeEndSession, handleHandoffLeadership } = session;
  
  // NOTE: You will need to bring in local state that wasn't context-ified
  // or convert them. For now, the structure guarantees safe parsing context injection.

  const executeLeaveCrew = async (crew: PermanentCrew) => {
    if (!currentUserId) return;
    try {
      await profileService.leavePermanentCrew(crew.id, currentUserId);
      AppLogger.log('CREW_PERMANENT_LEFT', { crewId: crew.id, method: 'leave_btn' });
      hub.setMyCrews(prev => prev.filter(c => c.id !== crew.id));
      hub.setPermanentCrews(prev => prev.filter((c) => c.id !== crew.id));
      setSelectedCrewDetail(null);
      setConfirmingLeaveCrewId(null);
    } catch (err: unknown) { const e = err instanceof Error ? err : new Error((err instanceof Error ? err.message : String(err))); Alert.alert('Error', e.message); }
  };

  const executeDeleteCrew = async (crew: PermanentCrew) => {
    if (!currentUserId) return;
    try {
      await profileService.deleteCrew(crew.id, currentUserId);
      AppLogger.log('CREW_PERMANENT_DELETED', { crewId: crew.id });
      hub.setMyCrews(prev => prev.filter(c => c.id !== crew.id));
      hub.setPermanentCrews(prev => prev.filter((c) => c.id !== crew.id));
      setSelectedCrewDetail(null);
      setConfirmingDeleteCrewId(null);
    } catch (err: unknown) { const e = err instanceof Error ? err : new Error((err instanceof Error ? err.message : String(err))); Alert.alert('Error', e.message); }
  };

  const handleStartEdit = (crew: PermanentCrew) => {
    setEditCrewName(crew.name);
    setEditCrewIsPublic(crew.is_public ?? false);
    setEditCrewCity(crew.city ?? '');
    setEditCrewState(crew.state ?? '');
    setEditCrewDesc(crew.description ?? '');
    setEditingCrewId(crew.id);
    setSelectedMembers([]);
    setUserSearchQuery('');
  };

  const handleAddMembersToCrew = async (crewId: string) => {
    if (selectedMembers.length === 0) return;
    setIsAddingMembersTo(crewId);
    try {
      const userIdsToAdd = selectedMembers.map(m => m.user_id);
      await profileService.addCrewMembers(crewId, userIdsToAdd);
      AppLogger.log('CREW_MEMBERS_ADDED', { crewId, count: userIdsToAdd.length });
      Alert.alert('Success', `Added ${selectedMembers.length} skater(s) to the crew!`);
      setSelectedMembers([]);
      setUserSearchQuery('');
      setCardMembers(prev => { const next = { ...prev }; delete next[crewId]; return next; });
      loadCrewMembers(crewId);
    } catch (err: unknown) {
      const e = err instanceof Error ? err : new Error((err instanceof Error ? err.message : String(err)));
      Alert.alert('Error', e.message ?? 'Could not add members to crew.');
    } finally {
      setIsAddingMembersTo(null);
    }
  };

  const handleSaveCrew = async (crew: PermanentCrew) => {
    if (!editCrewName.trim()) return;
    if (!currentUserId) return;
    setIsSavingCrew(true);
    try {
      await profileService.updateCrew(crew.id, {
        name: editCrewName.trim(),
        isPublic: editCrewIsPublic,
        city: editCrewCity.trim() || undefined,
        state: editCrewState.trim() || undefined,
        description: editCrewDesc.trim() || undefined,
      }, currentUserId);
      const updated = {
        ...crew, name: editCrewName.trim(), is_public: editCrewIsPublic,
        city: editCrewCity.trim() || undefined, state: editCrewState.trim() || undefined,
        description: editCrewDesc.trim() || undefined
      };
      hub.setMyCrews(prev => prev.map(c => c.id === crew.id ? updated : c));
      setSelectedCrewDetail(updated);
      setEditingCrewId(null);
      AppLogger.log('CREW_PERMANENT_UPDATED', { crewId: crew.id, crewName: editCrewName.trim(), isPublic: editCrewIsPublic });
    } catch (err: unknown) {
      const e = err instanceof Error ? err : new Error((err instanceof Error ? err.message : String(err)));
      AppLogger.log('CREW_ERROR', { action: 'save_crew', crewId: crew.id, error: e instanceof Error ? e.message : String(e)  });
      Alert.alert('Save failed', e.message ?? 'Unknown error');
    } finally {
      setIsSavingCrew(false);
    }
  };

  const crew = selectedCrewDetail;
    if (!crew) return null;
    const info = crewMemberCounts[crew.id];
    return (
      <ScrollView contentContainerStyle={{ padding: Spacing.xl, paddingBottom: Spacing.giant }} showsVerticalScrollIndicator={false}>
        <TouchableOpacity
          onPress={() => { setSelectedCrewDetail(null); setEditingCrewId(null); setStep('landing'); }}
          style={styles.backBtn}
        >
          <MaterialCommunityIcons name="chevron-left" size={22} color={Colors.textMuted} />
          <Text style={styles.backText}>Back</Text>
        </TouchableOpacity>

        {/* Crew avatar */}
        {editingCrewId !== crew.id && (
          <>
            <View style={{ alignItems: 'center', marginVertical: Spacing.lg }}>
          <View style={{ position: 'relative' }}>
            {crew.avatar_url
              ? <Image source={{ uri: crew.avatar_url }} style={[styles.mgAvatarImg, { width: 72, height: 72, borderRadius: 36 }]} />
              : <View style={[styles.mgAvatar, { width: 72, height: 72, borderRadius: 36, backgroundColor: crew.avatar_color ?? '#FFAA00' }]}>
                <MaterialCommunityIcons name={(crew.avatar_icon ?? 'account-group') as keyof typeof MaterialCommunityIcons.glyphMap} size={32} color="#000" />
              </View>}
            {!crew.is_public && (
              <MaterialCommunityIcons name="lock" size={18} color="#FFF" style={{ position: 'absolute', top: -2, right: -2, backgroundColor: '#000', borderRadius: 10, padding: Spacing.xxs, overflow: 'hidden', borderWidth: 2, borderColor: '#1C1C1E' }} />
            )}
          </View>
          <Text style={[styles.titleLarge, { marginTop: Spacing.md, marginBottom: Spacing.xxs }]}>{crew.name}</Text>
          {(crew.city || crew.state) && (
            <Text style={styles.mgCrewSub}>
              <MaterialCommunityIcons name="map-marker" size={12} color={Colors.textMuted} />
              {' '}{[crew.city, crew.state].filter(Boolean).join(', ')}
            </Text>
          )}
          {crew.description && <Text style={[styles.mgHint, { textAlign: 'center', marginTop: Spacing.sm }]}>{crew.description}</Text>}
        </View>

        {/* Members */}
        {editingCrewId !== crew.id && (
          <>
            <Text style={styles.label}>MEMBERS</Text>
            <View style={styles.mgAvatarRow}>
              {(info?.avatarColors ?? []).slice(0, 8).map((c, i) => (
                <View key={i} style={[styles.mgMemberDot, { width: 32, height: 32, borderRadius: 16, backgroundColor: c, marginLeft: i > 0 ? -10 : 0, borderWidth: 2, borderColor: '#111' }]} />
              ))}
              <Text style={[styles.mgMemberCount, { marginLeft: Spacing.sm }]}>{info?.count ?? '?'} member{(info?.count ?? 0) !== 1 ? 's' : ''}</Text>
            </View>
          </>
        )}
        {/* Session stats */}
        <CrewDetailStats stats={crewStats[crew.id]} styles={styles} Colors={Colors} />

        {/* Invite code */}
        <Text style={styles.label}>INVITE CODE</Text>
        <TouchableOpacity
          style={styles.mgCodeBox}
          onPress={async () => {
            try {
              await Clipboard.setStringAsync(crew.invite_code ?? '');
              Alert.alert('Copied!', 'Invite code copied to clipboard.');
            } catch (err) {
              Alert.alert('Error', 'Could not copy to clipboard.');
            }
          }}
          activeOpacity={0.7}
        >
          <Text style={styles.mgCodeText}>{crew.invite_code}</Text>
          <Text style={styles.mgHint}>Tap to copy · share with friends to join</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.shareBtn}
          onPress={() => {
            const deepLink = Linking.createURL('crew/join', { queryParams: { code: crew.invite_code || '' } });
            Share.share({
              message: `Rolling with ${crew.name}! Join the crew: ${deepLink}`,
              title: `Join ${crew.name} on SK8Lytz`,
            });
          }}
        >
          <MaterialCommunityIcons name="share-variant" size={17} color="#000" />
          <Text style={styles.shareBtnText}>Share Invite Code</Text>
        </TouchableOpacity>

        {/* Status */}
        <View style={{ flexDirection: 'row', gap: Spacing.md, marginTop: Spacing.lg }}>
          {crew.is_owner && <View style={styles.mgOwnerBadge}><Text style={styles.mgBadgeText}>👑 Owner</Text></View>}
          {crew.is_public
            ? <View style={[styles.mgOwnerBadge, { backgroundColor: 'rgba(0,200,100,0.15)' }]}><Text style={[styles.mgBadgeText, { color: '#00C864' }]}>🌍 Public</Text></View>
            : <View style={[styles.mgOwnerBadge, { backgroundColor: 'rgba(255,255,255,0.06)' }]}><Text style={[styles.mgBadgeText, { color: Colors.textMuted }]}>🔒 Private</Text></View>}
        </View>

        {/* Actions */}
        <View style={{ marginTop: Spacing.xl, gap: Spacing.md }}>
          {crew.is_owner && editingCrewId !== crew.id && (
            <TouchableOpacity style={styles.editBtn} onPress={() => handleStartEdit(crew)}>
              <MaterialCommunityIcons name="pencil" size={17} color={Colors.primary} />
              <Text style={styles.editBtnText}>Edit CREW Settings</Text>
            </TouchableOpacity>
          )}
        </View>
          </>
        )}

        {/* Inline edit form */}
        {crew.is_owner && editingCrewId === crew.id && (
          <CrewDetailEditForm
            crew={crew}
            styles={styles}
            Colors={Colors}
            editCrewName={editCrewName}
            setEditCrewName={setEditCrewName}
            editCrewDesc={editCrewDesc}
            setEditCrewDesc={setEditCrewDesc}
            editCrewCity={editCrewCity}
            setEditCrewCity={setEditCrewCity}
            editCrewState={editCrewState}
            setEditCrewState={setEditCrewState}
            editCrewIsPublic={editCrewIsPublic}
            setEditCrewIsPublic={setEditCrewIsPublic}
            loadingCardMembersFor={loadingCardMembersFor}
            cardMembers={cardMembers}
            currentUserId={currentUserId}
            makingOwnerFor={makingOwnerFor}
            setMakingOwnerFor={setMakingOwnerFor}
            profileService={profileService}
            setCardMembers={setCardMembers}
            isRemovingUserFor={isRemovingUserFor}
            setIsRemovingUserFor={setIsRemovingUserFor}
            setCrewMemberCounts={setCrewMemberCounts}
            selectedMembers={selectedMembers}
            setSelectedMembers={setSelectedMembers}
            userSearchQuery={userSearchQuery}
            setUserSearchQuery={setUserSearchQuery}
            userSearchResults={userSearchResults}
            handleAddMembersToCrew={handleAddMembersToCrew}
            isAddingMembersTo={isAddingMembersTo}
            handleSaveCrew={handleSaveCrew}
            isSavingCrew={isSavingCrew}
            setEditingCrewId={setEditingCrewId}
          />
        )}

          {crew.is_owner
            ? confirmingDeleteCrewId === crew.id ? (
              <View style={{ flexDirection: 'row', gap: Spacing.md }}>
                <TouchableOpacity style={[styles.dangerBtn, { flex: 1, backgroundColor: 'rgba(255,68,68,0.3)' }]} onPress={() => executeDeleteCrew(crew)}>
                  <MaterialCommunityIcons name="check" size={17} color="#FFF" />
                  <Text style={[styles.dangerBtnText, { color: '#FFF' }]}>Confirm Delete</Text>
                </TouchableOpacity>
                <TouchableOpacity style={[styles.dangerBtn, { flex: 1 }]} onPress={() => setConfirmingDeleteCrewId(null)}>
                  <Text style={styles.dangerBtnText}>Cancel</Text>
                </TouchableOpacity>
              </View>
            ) : (
              <TouchableOpacity style={styles.dangerBtn} onPress={() => setConfirmingDeleteCrewId(crew.id)}>
                <MaterialCommunityIcons name="delete-forever" size={17} color="#FF4444" />
                <Text style={styles.dangerBtnText}>Delete CREW</Text>
              </TouchableOpacity>
            )
            : confirmingLeaveCrewId === crew.id ? (
              <View style={{ flexDirection: 'row', gap: Spacing.md }}>
                <TouchableOpacity style={[styles.dangerBtn, { flex: 1, backgroundColor: 'rgba(255,107,0,0.3)' }]} onPress={() => executeLeaveCrew(crew)}>
                  <MaterialCommunityIcons name="check" size={17} color="#FFF" />
                  <Text style={[styles.dangerBtnText, { color: '#FFF' }]}>Confirm Leave</Text>
                </TouchableOpacity>
                <TouchableOpacity style={[styles.dangerBtn, { flex: 1 }]} onPress={() => setConfirmingLeaveCrewId(null)}>
                  <Text style={styles.dangerBtnText}>Cancel</Text>
                </TouchableOpacity>
              </View>
            ) : (
              <TouchableOpacity style={styles.dangerBtn} onPress={() => setConfirmingLeaveCrewId(crew.id)}>
                <MaterialCommunityIcons name="exit-run" size={17} color="#FF6B00" />
                <Text style={[styles.dangerBtnText, { color: '#FF6B00' }]}>Leave CREW</Text>
              </TouchableOpacity>
            )}
      </ScrollView>
    );
  
}
