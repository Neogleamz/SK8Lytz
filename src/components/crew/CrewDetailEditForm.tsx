import { MaterialCommunityIcons } from '@expo/vector-icons';
import React from 'react';
import { ActivityIndicator, Alert, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { Spacing } from '../../theme/theme';

export interface CrewDetailEditFormProps {
  crew: any;
  styles: any;
  Colors: any;
  editCrewName: string;
  setEditCrewName: (v: string) => void;
  editCrewDesc: string;
  setEditCrewDesc: (v: string) => void;
  editCrewCity: string;
  setEditCrewCity: (v: string) => void;
  editCrewState: string;
  setEditCrewState: (v: string) => void;
  editCrewIsPublic: boolean;
  setEditCrewIsPublic: (v: boolean) => void;
  loadingCardMembersFor: string | null;
  cardMembers: Record<string, any[]>;
  currentUserId: string | null;
  makingOwnerFor: string | null;
  setMakingOwnerFor: (id: string | null) => void;
  profileService: any;
  setCardMembers: React.Dispatch<React.SetStateAction<Record<string, any[]>>>;
  isRemovingUserFor: string | null;
  setIsRemovingUserFor: (id: string | null) => void;
  setCrewMemberCounts: React.Dispatch<React.SetStateAction<Record<string, any>>>;
  selectedMembers: any[];
  setSelectedMembers: React.Dispatch<React.SetStateAction<any[]>>;
  userSearchQuery: string;
  setUserSearchQuery: (v: string) => void;
  userSearchResults: any[];
  handleAddMembersToCrew: (crewId: string) => void;
  isAddingMembersTo: string | null;
  handleSaveCrew: (crew: any) => void;
  isSavingCrew: boolean;
  setEditingCrewId: (id: string | null) => void;
}

export const CrewDetailEditForm = ({
  crew, styles, Colors, editCrewName, setEditCrewName, editCrewDesc, setEditCrewDesc,
  editCrewCity, setEditCrewCity, editCrewState, setEditCrewState, editCrewIsPublic, setEditCrewIsPublic,
  loadingCardMembersFor, cardMembers, currentUserId, makingOwnerFor, setMakingOwnerFor, profileService,
  setCardMembers, isRemovingUserFor, setIsRemovingUserFor, setCrewMemberCounts, selectedMembers,
  setSelectedMembers, userSearchQuery, setUserSearchQuery, userSearchResults, handleAddMembersToCrew,
  isAddingMembersTo, handleSaveCrew, isSavingCrew, setEditingCrewId
}: CrewDetailEditFormProps) => {
  return (
    <View style={{ gap: Spacing.md, marginTop: Spacing.md }}>
      <Text style={[styles.titleLarge, { marginBottom: Spacing.md }]}>Edit CREW Settings</Text>
      <Text style={styles.label}>CREW NAME</Text>
      <TextInput style={styles.input} value={editCrewName} onChangeText={setEditCrewName}
        placeholder="Crew name" placeholderTextColor={Colors.textMuted} maxLength={40} />

      <Text style={styles.label}>DESCRIPTION</Text>
      <TextInput style={[styles.input, { height: 64, textAlignVertical: 'top' }]}
        value={editCrewDesc} onChangeText={setEditCrewDesc} multiline
        placeholder="What's this crew about?" placeholderTextColor={Colors.textMuted} maxLength={120} />

      <Text style={styles.label}>LOCATION</Text>
      <View style={{ flexDirection: 'row', gap: Spacing.sm }}>
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
      <View style={{ marginTop: Spacing.md, borderTopWidth: 1, borderTopColor: 'rgba(255,255,255,0.07)', paddingTop: Spacing.lg }}>
        <Text style={styles.label}>MANAGE MEMBERS</Text>
        {loadingCardMembersFor === crew.id ? (
          <ActivityIndicator size="small" color={Colors.primary} style={{ marginVertical: Spacing.md }} />
        ) : (cardMembers[crew.id] ?? []).length === 0 ? (
          <Text style={[styles.mgHint, { marginBottom: Spacing.md }]}>No members loaded.</Text>
        ) : (
          (cardMembers[crew.id] ?? []).map(member => {
            const memberIsOwner = member.role === 'owner';
            const isMakingThisOwner = makingOwnerFor === member.user_id;
            const isRemovingThis = isRemovingUserFor === member.user_id;
            return (
              <View key={member.user_id} style={{ flexDirection: 'row', alignItems: 'center', paddingVertical: Spacing.sm, gap: Spacing.md, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.03)' }}>
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
                  <View style={{ flexDirection: 'row', gap: Spacing.sm }}>
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
                        } catch (err: unknown) {
                          const e = err instanceof Error ? err : new Error((err instanceof Error ? err.message : String(err)));
                          Alert.alert('Error', e.message ?? 'Could not update role');
                        } finally {
                          setMakingOwnerFor(null);
                        }
                      }}
                      style={{ paddingHorizontal: Spacing.md, paddingVertical: Spacing.sm, borderRadius: 8, backgroundColor: memberIsOwner ? 'rgba(255,68,68,0.12)' : 'rgba(255,208,0,0.12)', borderWidth: 1, borderColor: memberIsOwner ? 'rgba(255,68,68,0.3)' : 'rgba(255,208,0,0.3)' }}
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
                              } catch (err: unknown) {
                                const e = err instanceof Error ? err : new Error((err instanceof Error ? err.message : String(err)));
                                Alert.alert('Error', e.message ?? 'Could not remove member');
                              } finally {
                                setIsRemovingUserFor(null);
                              }
                            }
                          }
                        ]);
                      }}
                      style={{ paddingHorizontal: Spacing.md, paddingVertical: Spacing.sm, borderRadius: 8, backgroundColor: 'rgba(255,68,68,0.12)', borderWidth: 1, borderColor: 'rgba(255,68,68,0.3)' }}
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
      <View style={{ marginTop: Spacing.md }}>
        <Text style={styles.label}>ADD NEW SKATERS</Text>
        <View style={{ backgroundColor: 'rgba(255,255,255,0.02)', padding: Spacing.md, borderRadius: 12, borderWidth: 1, borderColor: 'rgba(255,255,255,0.05)', marginBottom: Spacing.sm }}>
          <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.sm, marginBottom: selectedMembers.length > 0 ? 8 : 0 }}>
            {selectedMembers.map(member => (
              <View key={member.user_id} style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: Colors.primary || '#FFAA00', borderRadius: 16, paddingHorizontal: Spacing.md, paddingVertical: Spacing.xs, gap: Spacing.xs }}>
                <Text style={{ fontSize: 12, fontWeight: '700', color: '#000' }}>@{member.username || member.display_name}</Text>
                <TouchableOpacity onPress={() => setSelectedMembers(prev => prev.filter(m => m.user_id !== member.user_id))}>
                  <MaterialCommunityIcons name="close-circle" size={16} color="rgba(0,0,0,0.6)" />
                </TouchableOpacity>
              </View>
            ))}
          </View>
          <View style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: 'rgba(255,255,255,0.04)', borderRadius: 12, paddingHorizontal: Spacing.md, paddingVertical: Spacing.md, borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)' }}>
            <MaterialCommunityIcons name="magnify" size={18} color={Colors.textMuted} />
            <TextInput style={{ flex: 1, color: '#fff', fontSize: 13, marginLeft: Spacing.sm }} value={userSearchQuery} onChangeText={setUserSearchQuery} placeholder="Search exact username..." placeholderTextColor={Colors.textMuted} autoCapitalize="none" autoCorrect={false} />
            {userSearchQuery.length > 0 && (
              <TouchableOpacity onPress={() => setUserSearchQuery('')}>
                <MaterialCommunityIcons name="close-circle" size={16} color={Colors.textMuted} />
              </TouchableOpacity>
            )}
          </View>
          {userSearchResults.length > 0 && userSearchQuery.trim().length > 0 && (
            <View style={{ marginTop: Spacing.sm, gap: Spacing.xs }}>
              {userSearchResults.map(u =>
                !cardMembers[crew.id]?.some(mem => mem.user_id === u.user_id) &&
                !selectedMembers.some(sm => sm.user_id === u.user_id) && (
                  <TouchableOpacity key={u.user_id} onPress={() => { setSelectedMembers(prev => [...prev, u]); setUserSearchQuery(''); }} style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: 'rgba(255,255,255,0.04)', padding: Spacing.sm, borderRadius: 8 }}>
                    <MaterialCommunityIcons name="account" size={16} color={Colors.textMuted} style={{ marginRight: Spacing.sm }} />
                    <Text style={{ color: '#FFF', fontSize: 13, flex: 1 }}>{u.display_name} <Text style={{ color: Colors.textMuted }}>@{u.username}</Text></Text>
                    <MaterialCommunityIcons name="plus" size={18} color={Colors.primary} />
                  </TouchableOpacity>
                ))}
            </View>
          )}
          {selectedMembers.length > 0 && (
            <TouchableOpacity
              style={[styles.primaryBtn, { marginTop: Spacing.md }]}
              onPress={() => handleAddMembersToCrew(crew.id)}
              disabled={isAddingMembersTo === crew.id}
            >
              {isAddingMembersTo === crew.id ? <ActivityIndicator size="small" color="#000" /> : <MaterialCommunityIcons name="account-plus" size={18} color="#000" />}
              <Text style={styles.primaryBtnText}>Invite & Add Skaters</Text>
            </TouchableOpacity>
          )}
        </View>
      </View>

      <View style={{ flexDirection: 'row', gap: Spacing.md, marginTop: Spacing.lg, paddingTop: Spacing.lg, borderTopWidth: 1, borderTopColor: 'rgba(255,255,255,0.07)' }}>
        <TouchableOpacity style={[styles.primaryBtn, { flex: 1, marginTop: 0 }]} onPress={() => handleSaveCrew(crew)} disabled={isSavingCrew}>
          {isSavingCrew ? <ActivityIndicator size="small" color="#000" /> : <MaterialCommunityIcons name="check" size={17} color="#000" />}
          <Text style={styles.primaryBtnText}>Save Changes</Text>
        </TouchableOpacity>
        <TouchableOpacity style={{ flex: 0.5, backgroundColor: 'rgba(255,255,255,0.08)', borderRadius: 12, justifyContent: 'center', alignItems: 'center', borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)', marginTop: 0 }} onPress={() => setEditingCrewId(null)}>
          <Text style={{ color: '#fff', fontSize: 14, fontWeight: '700' }}>Cancel</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
};
