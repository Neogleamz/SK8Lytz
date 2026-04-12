import React, { useRef, useEffect } from 'react';
import { View, Text, TouchableOpacity, ScrollView, Animated, ActivityIndicator, Alert, Share, TextInput, Image, RefreshControl } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../../context/ThemeContext';
import * as Clipboard from 'expo-clipboard';
import { profileService, PermanentCrew } from '../../services/ProfileService';
import { crewService, CrewSession } from '../../services/CrewService';
import { locationService } from '../../services/LocationService';
import { AppLogger } from '../../services/AppLogger';
import DateTimePicker from '@react-native-community/datetimepicker';
import * as ImagePicker from 'expo-image-picker';

import { createStyles } from './CrewStyles';
import { useCrewContext } from '../../context/CrewContext';
import { profileService } from '../../services/profileService';
// TODO: any other specific imports check manually
import { Picker } from '@react-native-picker/picker';

const styles = createStyles(Colors);


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
  const { activeSessions, myCrews, permanentCrews, isLoadingNearby, refreshNearby, nearbySessions, discoverRadiusMi, setDiscoverRadiusMi, locationLabel, handleDetectLocation, isGettingLocation } = hub;
  const { selectedCrewDetail, setSelectedCrewDetail, expandedCrewId, setExpandedCrewId, cardMembers, setCardMembers, loadingCardMembersFor, makingOwnerFor, setMakingOwnerFor, confirmingDeleteCrewId, setConfirmingDeleteCrewId, confirmingLeaveCrewId, setConfirmingLeaveCrewId, createCrewError, setCreateCrewError, isCreatingCrew, newCrewName, setNewCrewName, newCrewDesc, newCrewIsPublic, setNewCrewIsPublic, newCrewCity, setNewCrewCity, newCrewState, setNewCrewState } = manage;
  const { currentSession, isHandoffMode, executeLeaveSession, executeEndSession, handleHandoffLeadership } = session;
  
  // NOTE: You will need to bring in local state that wasn't context-ified
  // or convert them. For now, the structure guarantees safe parsing context injection.

  const crew = selectedCrewDetail;
    if (!crew) return null;
    const info = crewMemberCounts[crew.id];
    return (
      <ScrollView contentContainerStyle={{ padding: 20, paddingBottom: 60 }} showsVerticalScrollIndicator={false}>
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
            <View style={{ alignItems: 'center', marginVertical: 16 }}>
          <View style={{ position: 'relative' }}>
            {crew.avatar_url
              ? <Image source={{ uri: crew.avatar_url }} style={[styles.mgAvatarImg, { width: 72, height: 72, borderRadius: 36 }]} />
              : <View style={[styles.mgAvatar, { width: 72, height: 72, borderRadius: 36, backgroundColor: crew.avatar_color ?? '#FFAA00' }]}>
                <MaterialCommunityIcons name={(crew.avatar_icon ?? 'account-group') as any} size={32} color="#000" />
              </View>}
            {!crew.is_public && (
              <MaterialCommunityIcons name="lock" size={18} color="#FFF" style={{ position: 'absolute', top: -2, right: -2, backgroundColor: '#000', borderRadius: 10, padding: 2, overflow: 'hidden', borderWidth: 2, borderColor: '#1C1C1E' }} />
            )}
          </View>
          <Text style={[styles.titleLarge, { marginTop: 10, marginBottom: 2 }]}>{crew.name}</Text>
          {(crew.city || crew.state) && (
            <Text style={styles.mgCrewSub}>
              <MaterialCommunityIcons name="map-marker" size={12} color={Colors.textMuted} />
              {' '}{[crew.city, crew.state].filter(Boolean).join(', ')}
            </Text>
          )}
          {crew.description && <Text style={[styles.mgHint, { textAlign: 'center', marginTop: 8 }]}>{crew.description}</Text>}
        </View>

        {/* Members */}
        {editingCrewId !== crew.id && (
          <>
            <Text style={styles.label}>MEMBERS</Text>
            <View style={styles.mgAvatarRow}>
              {(info?.avatarColors ?? []).slice(0, 8).map((c, i) => (
                <View key={i} style={[styles.mgMemberDot, { width: 32, height: 32, borderRadius: 16, backgroundColor: c, marginLeft: i > 0 ? -10 : 0, borderWidth: 2, borderColor: '#111' }]} />
              ))}
              <Text style={[styles.mgMemberCount, { marginLeft: 8 }]}>{info?.count ?? '?'} member{(info?.count ?? 0) !== 1 ? 's' : ''}</Text>
            </View>
          </>
        )}
        {/* Session stats */}
        {(() => {
          const stats = crewStats[crew.id];
          const lastActiveStr = stats?.lastActive
            ? new Date(stats.lastActive).toLocaleDateString(undefined, { month: 'short', day: 'numeric', year: 'numeric' })
            : null;
          return (
            <View style={styles.statsRow}>
              <View style={styles.statCard}>
                <Text style={styles.statNum}>{stats?.sessionCount ?? '—'}</Text>
                <Text style={styles.statLabel}>Sessions</Text>
              </View>
              <View style={styles.statCard}>
                <Text style={styles.statNum} numberOfLines={1}>{lastActiveStr ?? '—'}</Text>
                <Text style={styles.statLabel}>Last Active</Text>
              </View>
              <View style={[styles.statCard, { flex: 1.4 }]}>
                <Text style={styles.statNum} numberOfLines={1}>{stats?.topScene ?? '—'}</Text>
                <Text style={styles.statLabel}>Top Scene</Text>
              </View>
            </View>
          );
        })()}

        {/* Invite code */}
        <Text style={styles.label}>INVITE CODE</Text>
        <TouchableOpacity
          style={styles.mgCodeBox}
          onPress={() => {
            Clipboard.setStringAsync(crew.invite_code ?? '');
            Alert.alert('Copied!', 'Invite code copied to clipboard.');
          }}
          activeOpacity={0.7}
        >
          <Text style={styles.mgCodeText}>{crew.invite_code}</Text>
          <Text style={styles.mgHint}>Tap to copy · share with friends to join</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.shareBtn}
          onPress={() => Share.share({
            message: `Join my SK8Lytz crew "${crew.name}"! Use code: ${crew.invite_code} in the SK8Lytz app.`,
            title: `Join ${crew.name} on SK8Lytz`,
          })}
        >
          <MaterialCommunityIcons name="share-variant" size={17} color="#000" />
          <Text style={styles.shareBtnText}>Share Invite Code</Text>
        </TouchableOpacity>

        {/* Status */}
        <View style={{ flexDirection: 'row', gap: 10, marginTop: 16 }}>
          {crew.is_owner && <View style={styles.mgOwnerBadge}><Text style={styles.mgBadgeText}>👑 Owner</Text></View>}
          {crew.is_public
            ? <View style={[styles.mgOwnerBadge, { backgroundColor: 'rgba(0,200,100,0.15)' }]}><Text style={[styles.mgBadgeText, { color: '#00C864' }]}>🌍 Public</Text></View>
            : <View style={[styles.mgOwnerBadge, { backgroundColor: 'rgba(255,255,255,0.06)' }]}><Text style={[styles.mgBadgeText, { color: Colors.textMuted }]}>🔒 Private</Text></View>}
        </View>

        {/* Actions */}
        <View style={{ marginTop: 24, gap: 12 }}>
          {crew.is_owner && editingCrewId !== crew.id && (
            <TouchableOpacity style={styles.editBtn} onPress={() => handleStartEdit(crew)}>
              <MaterialCommunityIcons name="pencil" size={17} color={Colors.primary} />
              <Text style={styles.editBtnText}>Edit Crew Settings</Text>
            </TouchableOpacity>
          )}
        </View>
          </>
        )}

        {/* Inline edit form */}
        {crew.is_owner && editingCrewId === crew.id && (
          <View style={{ gap: 10, marginTop: 12 }}>
            <Text style={[styles.titleLarge, { marginBottom: 12 }]}>Edit Crew Settings</Text>
            <Text style={styles.label}>CREW NAME</Text>
            <TextInput style={styles.input} value={editCrewName} onChangeText={setEditCrewName}
              placeholder="Crew name" placeholderTextColor={Colors.textMuted} maxLength={40} />

              <Text style={styles.label}>DESCRIPTION</Text>
              <TextInput style={[styles.input, { height: 64, textAlignVertical: 'top' }]}
                value={editCrewDesc} onChangeText={setEditCrewDesc} multiline
                placeholder="What's this crew about?" placeholderTextColor={Colors.textMuted} maxLength={120} />

              <Text style={styles.label}>LOCATION</Text>
              <View style={{ flexDirection: 'row', gap: 8 }}>
                <TextInput style={[styles.input, { flex: 2 }]} value={editCrewCity} onChangeText={setEditCrewCity}
                  placeholder="City" placeholderTextColor={Colors.textMuted} />
                <TextInput style={[styles.input, { flex: 1 }]} value={editCrewState} onChangeText={setEditCrewState}
                  placeholder="State" placeholderTextColor={Colors.textMuted} />
              </View>

              <Text style={styles.label}>VISIBILITY</Text>
              <View style={styles.visibilityRow}>
                <TouchableOpacity style={[styles.visibilityBtn, !editCrewIsPublic && styles.visibilityBtnActive]}
                  onPress={() => setEditCrewIsPublic(false)}>
                  <MaterialCommunityIcons name="lock" size={15} color={!editCrewIsPublic ? '#000' : Colors.textMuted} />
                  <Text style={[styles.visibilityBtnText, !editCrewIsPublic && styles.visibilityBtnTextActive]}>Private</Text>
                </TouchableOpacity>
                <TouchableOpacity style={[styles.visibilityBtn, editCrewIsPublic && styles.visibilityBtnPublic]}
                  onPress={() => setEditCrewIsPublic(true)}>
                  <MaterialCommunityIcons name="earth" size={15} color={editCrewIsPublic ? '#000' : Colors.textMuted} />
                  <Text style={[styles.visibilityBtnText, editCrewIsPublic && styles.visibilityBtnTextActive]}>Public</Text>
                </TouchableOpacity>
              </View>

              {/* Edit Current Members List */}
              <View style={{ marginTop: 12, borderTopWidth: 1, borderTopColor: 'rgba(255,255,255,0.07)', paddingTop: 16 }}>
                <Text style={styles.label}>MANAGE MEMBERS</Text>
                {loadingCardMembersFor === crew.id ? (
                  <ActivityIndicator size="small" color={Colors.primary} style={{ marginVertical: 10 }} />
                ) : (cardMembers[crew.id] ?? []).length === 0 ? (
                  <Text style={[styles.mgHint, { marginBottom: 12 }]}>No members loaded.</Text>
                ) : (
                  (cardMembers[crew.id] ?? []).map(member => {
                    const memberIsOwner = member.role === 'owner';
                    const isMakingThisOwner = makingOwnerFor === member.user_id;
                    const isRemovingThis = isRemovingUserFor === member.user_id;
                    return (
                      <View key={member.user_id} style={{ flexDirection: 'row', alignItems: 'center', paddingVertical: 8, gap: 10, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.03)' }}>
                        <View style={{ width: 34, height: 34, borderRadius: 17, backgroundColor: member.avatar_color, alignItems: 'center', justifyContent: 'center' }}>
                          <Text style={{ color: '#000', fontWeight: '800', fontSize: 13 }}>{(member.display_name?.[0] ?? '?').toUpperCase()}</Text>
                        </View>
                        <View style={{ flex: 1 }}>
                          <Text style={{ color: Colors.text, fontSize: 15, fontWeight: '700' }}>
                            {member.display_name ?? 'Unknown'}
                            {member.user_id === currentUserId ? ' (you)' : ''}
                          </Text>
                          <Text style={{ color: memberIsOwner ? '#FFD700' : Colors.textMuted, fontSize: 12, fontWeight: '600' }}>
                            {memberIsOwner ? '👑 Owner' : 'Skater'}
                          </Text>
                        </View>
                        {/* Owner actions */}
                        {crew.is_owner && member.user_id !== currentUserId && (
                          <View style={{ flexDirection: 'row', gap: 6 }}>
                            {/* Make/Revoke Owner */}
                            <TouchableOpacity
                              disabled={isMakingThisOwner}
                              onPress={async () => {
                                setMakingOwnerFor(member.user_id);
                                try {
                                  if (memberIsOwner) await profileService.revokeCrewOwner(crew.id, member.user_id);
                                  else await profileService.assignCrewOwner(crew.id, member.user_id);
                                  const updated = await profileService.getCrewMembersWithNames(crew.id);
                                  setCardMembers(prev => ({ ...prev, [crew.id]: updated }));
                                } catch (e: any) {
                                  Alert.alert('Error', e.message ?? 'Could not update role');
                                } finally {
                                  setMakingOwnerFor(null);
                                }
                              }}
                              style={{ paddingHorizontal: 12, paddingVertical: 6, borderRadius: 8, backgroundColor: memberIsOwner ? 'rgba(255,68,68,0.12)' : 'rgba(255,208,0,0.12)', borderWidth: 1, borderColor: memberIsOwner ? 'rgba(255,68,68,0.3)' : 'rgba(255,208,0,0.3)' }}
                            >
                              {isMakingThisOwner ? <ActivityIndicator size="small" color={memberIsOwner ? '#FF4444' : '#FFD700'} /> : <Text style={{ fontSize: 11, fontWeight: '800', color: memberIsOwner ? '#FF4444' : '#FFD700' }}>{memberIsOwner ? 'Revoke' : '+ Owner'}</Text>}
                            </TouchableOpacity>
                            {/* Remove Member */}
                            <TouchableOpacity
                              disabled={isRemovingThis}
                              onPress={() => {
                                Alert.alert('Remove Skater', `Are you sure you want to remove ${member.display_name} from the crew?`, [
                                  { text: 'Cancel', style: 'cancel' },
                                  {
                                    text: 'Remove', style: 'destructive', onPress: async () => {
                                      setIsRemovingUserFor(member.user_id);
                                      try {
                                        await profileService.removeCrewMember(crew.id, member.user_id);
                                        const updated = await profileService.getCrewMembersWithNames(crew.id);
                                        setCardMembers(prev => ({ ...prev, [crew.id]: updated }));
                                        // Update overall counts natively if we can
                                        setCrewMemberCounts(prev => {
                                          const prevCount = prev[crew.id];
                                          if (!prevCount) return prev;
                                          return { ...prev, [crew.id]: { ...prevCount, count: Math.max(0, prevCount.count - 1) } };
                                        });
                                      } catch (e: any) {
                                        Alert.alert('Error', e.message ?? 'Could not remove member');
                                      } finally {
                                        setIsRemovingUserFor(null);
                                      }
                                    }
                                  }
                                ]);
                              }}
                              style={{ paddingHorizontal: 10, paddingVertical: 6, borderRadius: 8, backgroundColor: 'rgba(255,68,68,0.12)', borderWidth: 1, borderColor: 'rgba(255,68,68,0.3)' }}
                            >
                              {isRemovingThis ? <ActivityIndicator size="small" color="#FF4444" /> : <MaterialCommunityIcons name="account-remove" size={14} color="#FF4444" />}
                            </TouchableOpacity>
                          </View>
                        )}
                      </View>
                    );
                  })
                )}
              </View>

              {/* Add Members Section */}
              <View style={{ marginTop: 12 }}>
                <Text style={styles.label}>ADD NEW SKATERS</Text>
                <View style={{ backgroundColor: 'rgba(255,255,255,0.02)', padding: 12, borderRadius: 12, borderWidth: 1, borderColor: 'rgba(255,255,255,0.05)', marginBottom: 8 }}>
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
                  <View style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: 'rgba(255,255,255,0.04)', borderRadius: 12, paddingHorizontal: 12, paddingVertical: 10, borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)' }}>
                    <MaterialCommunityIcons name="magnify" size={18} color={Colors.textMuted} />
                    <TextInput style={{ flex: 1, color: '#fff', fontSize: 13, marginLeft: 8 }} value={userSearchQuery} onChangeText={setUserSearchQuery} placeholder="Search exact username..." placeholderTextColor={Colors.textMuted} autoCapitalize="none" autoCorrect={false} />
                    {userSearchQuery.length > 0 && (
                      <TouchableOpacity onPress={() => setUserSearchQuery('')}>
                        <MaterialCommunityIcons name="close-circle" size={16} color={Colors.textMuted} />
                      </TouchableOpacity>
                    )}
                  </View>
                  {userSearchResults.length > 0 && userSearchQuery.trim().length > 0 && (
                    <View style={{ marginTop: 8, gap: 4 }}>
                      {userSearchResults.map(u =>
                        !cardMembers[crew.id]?.some(mem => mem.user_id === u.user_id) &&
                        !selectedMembers.some(sm => sm.user_id === u.user_id) && (
                          <TouchableOpacity key={u.user_id} onPress={() => { setSelectedMembers(prev => [...prev, u]); setUserSearchQuery(''); }} style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: 'rgba(255,255,255,0.04)', padding: 8, borderRadius: 8 }}>
                            <MaterialCommunityIcons name="account" size={16} color={Colors.textMuted} style={{ marginRight: 6 }} />
                            <Text style={{ color: '#FFF', fontSize: 13, flex: 1 }}>{u.display_name} <Text style={{ color: Colors.textMuted }}>@{u.username}</Text></Text>
                            <MaterialCommunityIcons name="plus" size={18} color={Colors.primary} />
                          </TouchableOpacity>
                        ))}
                    </View>
                  )}
                  {selectedMembers.length > 0 && (
                    <TouchableOpacity
                      style={[styles.primaryBtn, { marginTop: 12 }]}
                      onPress={() => handleAddMembersToCrew(crew.id)}
                      disabled={isAddingMembersTo === crew.id}
                    >
                      {isAddingMembersTo === crew.id ? <ActivityIndicator size="small" color="#000" /> : <MaterialCommunityIcons name="account-plus" size={18} color="#000" />}
                      <Text style={styles.primaryBtnText}>Invite & Add Skaters</Text>
                    </TouchableOpacity>
                  )}
                </View>
              </View>

              <View style={{ flexDirection: 'row', gap: 10, marginTop: 16, paddingTop: 16, borderTopWidth: 1, borderTopColor: 'rgba(255,255,255,0.07)' }}>
                <TouchableOpacity style={[styles.primaryBtn, { flex: 1, marginTop: 0 }]} onPress={() => handleSaveCrew(crew)} disabled={isSavingCrew}>
                  {isSavingCrew ? <ActivityIndicator size="small" color="#000" /> : <MaterialCommunityIcons name="check" size={17} color="#000" />}
                  <Text style={styles.primaryBtnText}>Save Changes</Text>
                </TouchableOpacity>
                <TouchableOpacity style={{ flex: 0.5, backgroundColor: 'rgba(255,255,255,0.08)', borderRadius: 12, justifyContent: 'center', alignItems: 'center', borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)', marginTop: 0 }} onPress={() => setEditingCrewId(null)}>
                  <Text style={{ color: '#fff', fontSize: 14, fontWeight: '700' }}>Cancel</Text>
                </TouchableOpacity>
              </View>
            </View>
          )}

          {crew.is_owner
            ? confirmingDeleteCrewId === crew.id ? (
              <View style={{ flexDirection: 'row', gap: 10 }}>
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
                <Text style={styles.dangerBtnText}>Delete Crew</Text>
              </TouchableOpacity>
            )
            : confirmingLeaveCrewId === crew.id ? (
              <View style={{ flexDirection: 'row', gap: 10 }}>
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
                <Text style={[styles.dangerBtnText, { color: '#FF6B00' }]}>Leave Crew</Text>
              </TouchableOpacity>
            )}
      </ScrollView>
    );
  
}
