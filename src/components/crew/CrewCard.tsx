import { MaterialCommunityIcons } from '@expo/vector-icons';
import * as Clipboard from 'expo-clipboard';
import React from 'react';
import { ActivityIndicator, Alert, Animated, Image, Platform, Share, Text, TouchableOpacity, View } from 'react-native';
import { CrewSession } from '../../services/CrewService';
import { PermanentCrew } from '../../services/ProfileService';
import { Spacing } from '../../theme/theme';

export interface CrewCardProps {
  crew: PermanentCrew;
  styles: any;
  Colors: any;
  liveSession: CrewSession | null;
  memberInfo: { count: number } | null;
  cardMembers: Record<string, any[]>;
  isOwner: boolean;
  expandedCrewId: string | null;
  setExpandedCrewId: (id: string | null) => void;
  loadCrewMembers: (id: string) => void;
  handleJoinById: (id: string) => void;
  isLoading: boolean;
  pulseAnim: Animated.Value;
  formState: any;
  setStep: any; // Using any to avoid importing ModalStep if it's not exported
  setErrorMsg: (msg: string) => void;
  loadingCardMembersFor: string | null;
  currentUserId: string | null;
  makingOwnerFor: string | null;
  setMakingOwnerFor: (id: string | null) => void;
  profileService: any;
  setCardMembers: React.Dispatch<React.SetStateAction<Record<string, any[]>>>;
  handleStartEdit: (crew: PermanentCrew) => void;
  setSelectedCrewDetail: (crew: PermanentCrew) => void;
  handleLeaveCrew: (crew: PermanentCrew) => void;
  confirmingDeleteCrewId: string | null;
  setConfirmingDeleteCrewId: (id: string | null) => void;
  executeDeleteCrew: (crew: PermanentCrew) => void;
  timeAgo: (iso: string) => string;
}

export const CrewCard = ({
  crew, styles, Colors, liveSession, memberInfo, cardMembers, isOwner,
  expandedCrewId, setExpandedCrewId, loadCrewMembers, handleJoinById,
  isLoading, pulseAnim, formState, setStep, setErrorMsg, loadingCardMembersFor,
  currentUserId, makingOwnerFor, setMakingOwnerFor, profileService, setCardMembers,
  handleStartEdit, setSelectedCrewDetail, handleLeaveCrew,
  confirmingDeleteCrewId, setConfirmingDeleteCrewId, executeDeleteCrew, timeAgo
}: CrewCardProps) => {
  return (
    <View key={crew.id} style={styles.hubCrewCard}>
      {/* Crew avatar + name */}
      <View style={styles.hubCrewCardTop}>
        <View style={{ position: 'relative' }}>
          {crew.avatar_url ? (
            <Image source={{ uri: crew.avatar_url }} style={[styles.hubCrewAvatar, { width: 36, height: 36, borderRadius: 18 }]} />
          ) : (
            <View style={[styles.hubCrewAvatar, { backgroundColor: crew.avatar_color || '#FFAA00' }]}>
              <MaterialCommunityIcons name={(crew.avatar_icon as keyof typeof MaterialCommunityIcons.glyphMap) || 'account-group'} size={20} color="#000" />
            </View>
          )}
          {!crew.is_public && (
            <MaterialCommunityIcons name="lock" size={12} color="#FFF" style={{ position: 'absolute', top: -3, right: -3, backgroundColor: '#000', borderRadius: 6, overflow: 'hidden' }} />
          )}
        </View>
        <View style={{ flex: 1, marginLeft: Spacing.md }}>
          <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm }}>
            <Text style={styles.hubCrewName} numberOfLines={1}>{crew.name}</Text>
            {isOwner && (
              <View style={styles.hubOwnerBadge}>
                <Text style={styles.hubOwnerBadgeText}>👑 OWNER</Text>
              </View>
            )}
          </View>

          {/* Row 2: Member count + Public/Private badge */}
          <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, marginTop: Spacing.xxs, flexWrap: 'wrap' }}>
            <Text style={styles.hubCrewMeta}>
              {memberInfo ? `${memberInfo.count} member${memberInfo.count !== 1 ? 's' : ''}` : 'Loading...'}
              {crew.city ? ` · ${crew.city}` : ''}
            </Text>
            <View style={{
              flexDirection: 'row', alignItems: 'center', gap: Spacing.xxs,
              backgroundColor: crew.is_public ? 'rgba(0,200,100,0.12)' : 'rgba(255,255,255,0.07)',
              paddingHorizontal: Spacing.sm, paddingVertical: Spacing.xxs, borderRadius: 8,
            }}>
              <MaterialCommunityIcons
                name={crew.is_public ? 'earth' : 'lock'}
                size={10}
                color={crew.is_public ? '#00C864' : Colors.textMuted}
              />
              <Text style={{ color: crew.is_public ? '#00C864' : Colors.textMuted, fontSize: 10, fontWeight: '600' }}>
                {crew.is_public ? 'Public' : 'Private'}
              </Text>
            </View>
          </View>

          {/* Row 3: Leader */}
          {(() => {
            const leaderMember = cardMembers[crew.id]?.find(m => m.role === 'owner');
            const leaderName = isOwner
              ? 'You'
              : leaderMember?.display_name ?? null;
            return leaderName ? (
              <Text style={[styles.hubCrewMeta, { marginTop: 1 }]}>
                {isOwner ? '👑' : '🎯'} Leader: {leaderName}
              </Text>
            ) : null;
          })()}

          {/* Row 4: Invite code chip — private crews only */}
          {!crew.is_public && (
            <TouchableOpacity
              onPress={async () => {
                try {
                  await Clipboard.setStringAsync(crew.invite_code);
                  Alert.alert('Copied!', 'Invite code copied to clipboard.');
                } catch (err) {
                  Alert.alert('Error', 'Could not copy to clipboard.');
                }
              }}
              style={{
                flexDirection: 'row', alignItems: 'center', gap: Spacing.xs, marginTop: Spacing.xs,
                backgroundColor: 'rgba(255,255,255,0.05)',
                paddingHorizontal: Spacing.sm, paddingVertical: Spacing.xs, borderRadius: 8,
                borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)',
                alignSelf: 'flex-start',
              }}
            >
              <MaterialCommunityIcons name="key-variant" size={11} color={Colors.textMuted} />
              <Text style={{ color: Colors.textMuted, fontSize: 11, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', letterSpacing: 1 }}>
                {crew.invite_code}
              </Text>
              <MaterialCommunityIcons name="content-copy" size={10} color={Colors.textMuted} />
            </TouchableOpacity>
          )}
        </View>
        {/* Expand/collapse chevron */}
        <TouchableOpacity
          style={styles.hubCrewManageBtn}
          onPress={() => {
            const next = expandedCrewId === crew.id ? null : crew.id;
            setExpandedCrewId(next);
            if (next) loadCrewMembers(crew.id);
          }}
        >
          <MaterialCommunityIcons
            name={expandedCrewId === crew.id ? 'chevron-up' : 'chevron-down'}
            size={20} color={Colors.textMuted}
          />
        </TouchableOpacity>
      </View>

      {/* Live session status */}
      {liveSession ? (
        <TouchableOpacity
          style={styles.hubLiveSessionRow}
          onPress={() => handleJoinById(liveSession.id)}
          disabled={isLoading}
          activeOpacity={0.75}
        >
          <Animated.View style={[styles.hubLiveDot, { opacity: pulseAnim }]} />
          <View style={{ flex: 1 }}>
            <Text style={styles.hubLiveSessionName}>{liveSession.name}</Text>
            <Text style={styles.hubLiveSessionMeta}>
              {liveSession.member_count ?? 0} skater{(liveSession.member_count ?? 0) !== 1 ? 's' : ''}
              {' · '}{liveSession.is_public ? '🌍 Public' : '🔒 Private'}
              {' · '}{timeAgo(liveSession.created_at)}
            </Text>
          </View>
          {isLoading ? (
            <ActivityIndicator size="small" color="#000" />
          ) : (
            <View style={styles.hubJoinPill}>
              <MaterialCommunityIcons name="lightning-bolt" size={13} color="#000" />
              <Text style={styles.hubJoinPillText}>JOIN</Text>
            </View>
          )}
        </TouchableOpacity>
      ) : (
        <View style={styles.hubNoSessionRow}>
          <Text style={styles.hubNoSessionText}>No active session</Text>
          <View style={{ flexDirection: 'row', gap: Spacing.sm }}>
            <TouchableOpacity
              style={[styles.hubActionChip, { backgroundColor: Colors.primary }]}
              disabled={isLoading}
              onPress={() => {
                formState.setSelectedCrewId(crew.id);
                formState.setCrewName(crew.name);
                setStep('create');
                setErrorMsg('');
              }}
            >
              <MaterialCommunityIcons name="lightning-bolt" size={12} color="#000" />
              <Text style={[styles.hubActionChipText, { color: '#000', fontWeight: '700' }]}>Start Now</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={styles.hubActionChip}
              disabled={isLoading}
              onPress={() => {
                formState.setSelectedCrewId(crew.id);
                setStep('schedule');
                setErrorMsg('');
              }}
            >
              <MaterialCommunityIcons name="calendar-plus" size={12} color={Colors.primary} />
              <Text style={styles.hubActionChipText}>Schedule</Text>
            </TouchableOpacity>
          </View>
        </View>
      )}

      {/* ── Expandable: member list + actions ── */}
      {expandedCrewId === crew.id && (
        <View style={{ borderTopWidth: 1, borderTopColor: 'rgba(255,255,255,0.07)', marginTop: Spacing.md, paddingTop: Spacing.md }}>

          {/* Member list */}
          <Text style={[styles.hubCrewMeta, { fontWeight: '700', marginBottom: Spacing.sm, letterSpacing: 0.5, color: Colors.textMuted }]}>
            MEMBERS
          </Text>
          {loadingCardMembersFor === crew.id ? (
            <ActivityIndicator size="small" color={Colors.primary} style={{ marginBottom: Spacing.md }} />
          ) : (cardMembers[crew.id] ?? []).length === 0 ? (
            <Text style={[styles.hubCrewMeta, { marginBottom: Spacing.sm }]}>No members loaded yet.</Text>
          ) : (
            (cardMembers[crew.id] ?? []).map(member => {
              const memberIsOwner = member.role === 'owner';
              const isMakingThisOwner = makingOwnerFor === member.user_id;
              return (
                <View key={member.user_id} style={{ flexDirection: 'row', alignItems: 'center', paddingVertical: Spacing.xs, gap: Spacing.md }}>
                  {/* Avatar dot */}
                  <View style={{ width: 30, height: 30, borderRadius: 15, backgroundColor: member.avatar_color, alignItems: 'center', justifyContent: 'center' }}>
                    <Text style={{ color: '#000', fontWeight: '800', fontSize: 12 }}>
                      {(member.display_name?.[0] ?? '?').toUpperCase()}
                    </Text>
                  </View>
                  <View style={{ flex: 1 }}>
                    <Text style={{ color: Colors.text, fontSize: 13, fontWeight: '600' }}>
                      {member.display_name ?? 'Unknown'}
                      {member.user_id === currentUserId ? ' (you)' : ''}
                    </Text>
                    {memberIsOwner && (
                      <Text style={{ color: '#FFD700', fontSize: 11 }}>👑 Owner</Text>
                    )}
                  </View>
                  {/* Make/Revoke Owner */}
                  {isOwner && member.user_id !== currentUserId && (
                    <TouchableOpacity
                      disabled={isMakingThisOwner || isLoading}
                      onPress={async () => {
                        setMakingOwnerFor(member.user_id);
                        try {
                          if (memberIsOwner) {
                            await profileService.revokeCrewOwner(crew.id, member.user_id);
                          } else {
                            await profileService.assignCrewOwner(crew.id, member.user_id);
                          }
                          // Refresh member list
                          const updated = await profileService.getCrewMembersWithNames(crew.id);
                          setCardMembers(prev => ({ ...prev, [crew.id]: updated }));
                        } catch (err: unknown) {
                          const e = err instanceof Error ? err : new Error((err instanceof Error ? err.message : String(err)));
                          alert(e.message ?? 'Could not update role');
                        } finally {
                          setMakingOwnerFor(null);
                        }
                      }}
                      style={{
                        paddingHorizontal: Spacing.md, paddingVertical: Spacing.xs,
                        borderRadius: 8,
                        backgroundColor: memberIsOwner ? 'rgba(255,68,68,0.12)' : 'rgba(255,208,0,0.12)',
                        borderWidth: 1,
                        borderColor: memberIsOwner ? 'rgba(255,68,68,0.3)' : 'rgba(255,208,0,0.3)',
                      }}
                    >
                      {isMakingThisOwner
                        ? <ActivityIndicator size="small" color={memberIsOwner ? '#FF4444' : '#FFD700'} />
                        : <Text style={{ fontSize: 11, fontWeight: '700', color: memberIsOwner ? '#FF4444' : '#FFD700' }}>
                          {memberIsOwner ? 'Revoke' : '+ Owner'}
                        </Text>
                      }
                    </TouchableOpacity>
                  )}
                </View>
              );
            })
          )}

          {/* Inline actions: Edit / Share Code / Leave / Delete */}
          <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.sm, marginTop: Spacing.lg }}>
            {isOwner && (
              <TouchableOpacity
                style={[styles.hubActionChip]}
                disabled={isLoading}
                onPress={() => { handleStartEdit(crew); setSelectedCrewDetail(crew); setStep('manage'); }}
              >
                <MaterialCommunityIcons name="pencil" size={12} color={Colors.primary} />
                <Text style={styles.hubActionChipText}>Edit CREW</Text>
              </TouchableOpacity>
            )}
            {!crew.is_public && (
              <TouchableOpacity
                style={styles.hubActionChip}
                disabled={isLoading}
                onPress={() => Share.share({
                  message: `Join my SK8Lytz crew "${crew.name}"! Use code: ${crew.invite_code} in the SK8Lytz app.`,
                  title: `Join ${crew.name} on SK8Lytz`,
                })}
              >
                <MaterialCommunityIcons name="share-variant" size={12} color={Colors.primary} />
                <Text style={styles.hubActionChipText}>Share Code</Text>
              </TouchableOpacity>
            )}
            {!isOwner && (
              <TouchableOpacity
                style={[styles.hubActionChip, { borderColor: 'rgba(255,68,68,0.3)' }]}
                disabled={isLoading}
                onPress={() => handleLeaveCrew(crew)}
              >
                <MaterialCommunityIcons name="exit-run" size={12} color="#FF6666" />
                <Text style={[styles.hubActionChipText, { color: '#FF6666' }]}>Leave</Text>
              </TouchableOpacity>
            )}
            {isOwner && (
              confirmingDeleteCrewId === crew.id ? (
                <View style={{ flexDirection: 'row', gap: Spacing.xs }}>
                  <TouchableOpacity disabled={isLoading} style={[styles.hubActionChip, { borderColor: '#FF4444', backgroundColor: 'rgba(255,68,68,0.2)' }]} onPress={() => executeDeleteCrew(crew)}>
                    <Text style={[styles.hubActionChipText, { color: '#FFF' }]}>Confirm?</Text>
                  </TouchableOpacity>
                  <TouchableOpacity disabled={isLoading} style={styles.hubActionChip} onPress={() => setConfirmingDeleteCrewId(null)}>
                    <Text style={styles.hubActionChipText}>X</Text>
                  </TouchableOpacity>
                </View>
              ) : (
                <TouchableOpacity
                  style={[styles.hubActionChip, { borderColor: 'rgba(255,68,68,0.3)' }]}
                  disabled={isLoading}
                  onPress={() => setConfirmingDeleteCrewId(crew.id)}
                >
                  <MaterialCommunityIcons name="trash-can-outline" size={12} color="#FF4444" />
                  <Text style={[styles.hubActionChipText, { color: '#FF4444' }]}>Delete CREW</Text>
                </TouchableOpacity>
              )
            )}
          </View>
        </View>
      )}
    </View>
  );
};
