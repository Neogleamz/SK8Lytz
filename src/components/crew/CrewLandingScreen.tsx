import React, { useRef, useEffect, useState } from 'react';
import { View, Text, TouchableOpacity, ScrollView, Animated, ActivityIndicator, Alert, Share, TextInput, Image, RefreshControl, Platform } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import * as Clipboard from 'expo-clipboard';
import { useTheme } from '../../context/ThemeContext';
import { createStyles } from './CrewStyles';
import { useCrewContext } from '../../context/CrewContext';
import { profileService, PermanentCrew } from '../../services/ProfileService';
import { crewService, CrewSession } from '../../services/CrewService';
import { locationService } from '../../services/LocationService';
import { AppLogger } from '../../services/AppLogger';

function timeAgo(iso: string): string {
  const diff = Date.now() - new Date(iso).getTime();
  const m = Math.floor(diff / 60000);
  if (m < 1) return 'just now';
  if (m < 60) return `${m}m ago`;
  return `${Math.floor(m / 60)}h ago`;
}

export function CrewLandingScreen() {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  
  const context = useCrewContext();
  const { hub, manage, session, setStep, step, confirmAction, setConfirmAction, currentUserId, displayName, errorMsg, setErrorMsg, isLoading, setIsLoading, showCodeEntry, setShowCodeEntry, formState } = context;
  const { activeSessions, myCrews, permanentCrews, isLoadingNearby, refreshNearby, nearbySessions, discoverRadiusMi, setDiscoverRadiusMi, locationLabel, handleDetectLocation, isGettingLocation, crewMemberCounts } = hub;
  const { selectedCrewDetail, setSelectedCrewDetail, expandedCrewId, setExpandedCrewId, cardMembers, setCardMembers, loadingCardMembersFor, makingOwnerFor, setMakingOwnerFor, confirmingDeleteCrewId, setConfirmingDeleteCrewId, confirmingLeaveCrewId, setConfirmingLeaveCrewId, createCrewError, setCreateCrewError, isCreatingCrew, newCrewName, setNewCrewName, newCrewIsPublic, setNewCrewIsPublic, newCrewCity, setNewCrewCity, newCrewState, setNewCrewState, loadCrewMembers } = manage;
  const { currentSession, isHandoffMode, executeLeaveSession, executeEndSession, handleHandoffLeadership, handleSessionJoined } = session;
  
  const [inviteCode, setInviteCode] = useState('');
  const [joiningSessionId, setJoiningSessionId] = useState<string | null>(null);

  const pulseAnim = useRef(new Animated.Value(1)).current;

  useEffect(() => {
    const pulse = Animated.loop(
      Animated.sequence([
        Animated.timing(pulseAnim, { toValue: 0.4, duration: 800, useNativeDriver: true }),
        Animated.timing(pulseAnim, { toValue: 1, duration: 800, useNativeDriver: true }),
      ])
    );
    pulse.start();
    return () => pulse.stop();
  }, []);

  const handleJoinById = async (sessionId: string) => {
    setIsLoading(true); setErrorMsg('');
    setJoiningSessionId(sessionId);
    try {
      const sessionData = await crewService.joinSessionById(sessionId, displayName.trim());
      AppLogger.log('CREW_SESSION_JOINED', { sessionId: sessionData.id, crewName: sessionData.name, method: 'browse' });
      await handleSessionJoined(sessionData);
    } catch (e: any) {
      AppLogger.log('CREW_ERROR', { action: 'join_id', error: e.message });
      setErrorMsg(e.message || 'Could not join that crew');
    } finally { 
      setIsLoading(false); 
      setJoiningSessionId(null);
    }
  };

  const handleStartEdit = (crew: PermanentCrew) => {
    manage.setEditingCrewId(crew.id);
    manage.setEditCrewName(crew.name);
    manage.setEditCrewIsPublic(crew.is_public);
    manage.setEditCrewCity(crew.city || '');
    manage.setEditCrewState(crew.state || '');
    manage.setEditCrewDesc(crew.description || '');
  };

  const handleLeaveCrew = (crew: PermanentCrew) => {
    Alert.alert(
      'Leave Crew?',
      `Are you sure you want to leave ${crew.name}?`,
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Leave', style: 'destructive', onPress: async () => {
            setIsLoading(true);
            try {
              await profileService.leavePermanentCrew(crew.id);
              AppLogger.log('CREW_LEFT', { crewId: crew.id, crewName: crew.name });
              const updated = await profileService.getMyCrew();
              hub.setMyCrews(updated);
              hub.setPermanentCrews(updated.map(c => ({ id: c.id, name: c.name })));
            } catch (e: any) {
              Alert.alert('Error', e.message || 'Could not leave crew');
            } finally {
              setIsLoading(false);
            }
          }
        }
      ]
    );
  };

  const executeDeleteCrew = async (crew: PermanentCrew) => {
    setIsLoading(true);
    try {
      await profileService.deleteCrew(crew.id);
      AppLogger.log('CREW_DELETED', { crewId: crew.id, crewName: crew.name });
      const updated = await profileService.getMyCrew();
      hub.setMyCrews(updated);
      hub.setPermanentCrews(updated.map(c => ({ id: c.id, name: c.name })));
      manage.setConfirmingDeleteCrewId(null);
    } catch (e: any) {
      Alert.alert('Error', e.message || 'Could not delete crew');
    } finally {
      setIsLoading(false);
    }
  };

  const handleJoinByCode = async () => {
    if (inviteCode.trim().length < 6) { setErrorMsg('Enter the 6-character crew invite code'); return; }
    setIsLoading(true); setErrorMsg('');
    try {
      const crew = await profileService.joinPermanentCrew(inviteCode.trim());
      AppLogger.log('CREW_SESSION_JOINED', { crewId: crew.id, crewName: crew.name, method: 'permanent_code' });
      const updatedCrews = await profileService.getMyCrew();
      hub.setMyCrews(updatedCrews);
      hub.setPermanentCrews(updatedCrews.map(c => ({ id: c.id, name: c.name })));
      setShowCodeEntry(false);
      setInviteCode('');
      Alert.alert(
        '🛹 Joined!',
        `You're now a member of "${crew.name}". When they start a session you'll see it under My Crews.`,
        [{ text: 'Nice!' }]
      );
    } catch (e: any) {
      AppLogger.log('CREW_ERROR', { action: 'join_crew_by_code', error: e.message });
      setErrorMsg(e.message || 'Crew not found — check the code and try again.');
    } finally { setIsLoading(false); }
  };

  // Find the live session that belongs to a given permanent crew.
    // Match by crew_id first (reliable), fall back to name-string match for older sessions
    // that pre-date the crew_id field.
    const getLiveSessionForCrew = (crew: PermanentCrew): CrewSession | null => {
      return activeSessions.find(s =>
        (s.crew_id && s.crew_id === crew.id) ||
        (!s.crew_id && (s.name === crew.name || s.name.startsWith(crew.name + ' ')))
      ) ?? null;
    };

    return (
      <ScrollView contentContainerStyle={[styles.body, { paddingTop: 4 }]} showsVerticalScrollIndicator={false}>
        {/* ── Header ── */}
        <View style={styles.hubHeader}>
          <View>
            <Text style={styles.hubTitle}>Crew Hub</Text>
            <Text style={styles.hubSub}>Skate together · sync your light show</Text>
          </View>
        </View>

        {/* ── MY CREWS ── */}
        <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8, marginTop: 4, width: '100%' }}>
          <Text style={[styles.hubSectionLabel, { marginBottom: 0, marginTop: 0 }]}>MY CREWS</Text>
          {myCrews.length > 0 && (
            <TouchableOpacity onPress={() => setStep('manage')} style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: 'rgba(255,170,0,0.1)', paddingHorizontal: 10, paddingVertical: 4, borderRadius: 12 }}>
              <MaterialCommunityIcons name="plus" size={14} color={Colors.primary} />
              <Text style={{ color: Colors.primary, fontSize: 11, fontWeight: '700', marginLeft: 4 }}>New Crew</Text>
            </TouchableOpacity>
          )}
        </View>

        {myCrews.length === 0 ? (
          <View style={styles.hubEmptyCard}>
            <MaterialCommunityIcons name="account-group-outline" size={32} color={Colors.textMuted} />
            <Text style={styles.hubEmptyText}>You haven't joined any crews yet</Text>
            <TouchableOpacity
              style={[styles.hubActionChip, { marginTop: 10 }]}
              onPress={() => setStep('manage')}
            >
              <MaterialCommunityIcons name="plus" size={14} color={Colors.primary} />
              <Text style={styles.hubActionChipText}>Create a Crew</Text>
            </TouchableOpacity>
          </View>
        ) : (
          myCrews.map(crew => {
            const liveSession = getLiveSessionForCrew(crew);
            const memberInfo = crewMemberCounts[crew.id];
            const isOwner = crew.is_owner;
            return (
              <View key={crew.id} style={styles.hubCrewCard}>
                {/* Crew avatar + name */}
                <View style={styles.hubCrewCardTop}>
                  <View style={{ position: 'relative' }}>
                    {crew.avatar_url ? (
                      <Image source={{ uri: crew.avatar_url }} style={[styles.hubCrewAvatar, { width: 36, height: 36, borderRadius: 18 }]} />
                    ) : (
                      <View style={[styles.hubCrewAvatar, { backgroundColor: crew.avatar_color || '#FFAA00' }]}>
                        <MaterialCommunityIcons name={(crew.avatar_icon as any) || 'account-group'} size={20} color="#000" />
                      </View>
                    )}
                    {!crew.is_public && (
                      <MaterialCommunityIcons name="lock" size={12} color="#FFF" style={{ position: 'absolute', top: -3, right: -3, backgroundColor: '#000', borderRadius: 6, overflow: 'hidden' }} />
                    )}
                  </View>
                  <View style={{ flex: 1, marginLeft: 10 }}>
                    <View style={{ flexDirection: 'row', alignItems: 'center', gap: 6 }}>
                      <Text style={styles.hubCrewName} numberOfLines={1}>{crew.name}</Text>
                      {isOwner && (
                        <View style={styles.hubOwnerBadge}>
                          <Text style={styles.hubOwnerBadgeText}>👑 OWNER</Text>
                        </View>
                      )}
                    </View>

                    {/* Row 2: Member count + Public/Private badge */}
                    <View style={{ flexDirection: 'row', alignItems: 'center', gap: 6, marginTop: 2, flexWrap: 'wrap' }}>
                      <Text style={styles.hubCrewMeta}>
                        {memberInfo ? `${memberInfo.count} member${memberInfo.count !== 1 ? 's' : ''}` : 'Loading...'}
                        {crew.city ? ` · ${crew.city}` : ''}
                      </Text>
                      <View style={{
                        flexDirection: 'row', alignItems: 'center', gap: 3,
                        backgroundColor: crew.is_public ? 'rgba(0,200,100,0.12)' : 'rgba(255,255,255,0.07)',
                        paddingHorizontal: 6, paddingVertical: 2, borderRadius: 8,
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
                        onPress={() => Clipboard.setStringAsync(crew.invite_code)}
                        style={{
                          flexDirection: 'row', alignItems: 'center', gap: 4, marginTop: 5,
                          backgroundColor: 'rgba(255,255,255,0.05)',
                          paddingHorizontal: 8, paddingVertical: 4, borderRadius: 8,
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
                  {/* Expand/collapse chevron — replaces old ··· Manage nav */}
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
                    <View style={{ flexDirection: 'row', gap: 8 }}>
                      <TouchableOpacity
                        style={[styles.hubActionChip, { backgroundColor: Colors.primary }]}
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
                  <View style={{ borderTopWidth: 1, borderTopColor: 'rgba(255,255,255,0.07)', marginTop: 10, paddingTop: 12 }}>

                    {/* Member list */}
                    <Text style={[styles.hubCrewMeta, { fontWeight: '700', marginBottom: 8, letterSpacing: 0.5, color: Colors.textMuted }]}>
                      MEMBERS
                    </Text>
                    {loadingCardMembersFor === crew.id ? (
                      <ActivityIndicator size="small" color={Colors.primary} style={{ marginBottom: 10 }} />
                    ) : (cardMembers[crew.id] ?? []).length === 0 ? (
                      <Text style={[styles.hubCrewMeta, { marginBottom: 8 }]}>No members loaded yet.</Text>
                    ) : (
                      (cardMembers[crew.id] ?? []).map(member => {
                        const memberIsOwner = member.role === 'owner';
                        const isMakingThisOwner = makingOwnerFor === member.user_id;
                        return (
                          <View key={member.user_id} style={{ flexDirection: 'row', alignItems: 'center', paddingVertical: 5, gap: 10 }}>
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
                            {/* Make/Revoke Owner — only visible to current user if they are owner, and not for themselves */}
                            {isOwner && member.user_id !== currentUserId && (
                              <TouchableOpacity
                                disabled={isMakingThisOwner}
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
                                  } catch (e: any) {
                                    Alert.alert('Error', e.message ?? 'Could not update role');
                                  } finally {
                                    setMakingOwnerFor(null);
                                  }
                                }}
                                style={{
                                  paddingHorizontal: 10, paddingVertical: 4,
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
                    <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 8, marginTop: 14 }}>
                      {isOwner && (
                        <TouchableOpacity
                          style={[styles.hubActionChip]}
                          onPress={() => { handleStartEdit(crew); setSelectedCrewDetail(crew); setStep('manage'); }}
                        >
                          <MaterialCommunityIcons name="pencil" size={12} color={Colors.primary} />
                          <Text style={styles.hubActionChipText}>Edit Crew</Text>
                        </TouchableOpacity>
                      )}
                      {!crew.is_public && (
                        <TouchableOpacity
                          style={styles.hubActionChip}
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
                          onPress={() => handleLeaveCrew(crew)}
                        >
                          <MaterialCommunityIcons name="exit-run" size={12} color="#FF6666" />
                          <Text style={[styles.hubActionChipText, { color: '#FF6666' }]}>Leave</Text>
                        </TouchableOpacity>
                      )}
                      {isOwner && (
                        confirmingDeleteCrewId === crew.id ? (
                          <View style={{ flexDirection: 'row', gap: 4 }}>
                            <TouchableOpacity style={[styles.hubActionChip, { borderColor: '#FF4444', backgroundColor: 'rgba(255,68,68,0.2)' }]} onPress={() => executeDeleteCrew(crew)}>
                              <Text style={[styles.hubActionChipText, { color: '#FFF' }]}>Confirm?</Text>
                            </TouchableOpacity>
                            <TouchableOpacity style={styles.hubActionChip} onPress={() => setConfirmingDeleteCrewId(null)}>
                              <Text style={styles.hubActionChipText}>X</Text>
                            </TouchableOpacity>
                          </View>
                        ) : (
                          <TouchableOpacity
                            style={[styles.hubActionChip, { borderColor: 'rgba(255,68,68,0.3)' }]}
                            onPress={() => setConfirmingDeleteCrewId(crew.id)}
                          >
                            <MaterialCommunityIcons name="trash-can-outline" size={12} color="#FF4444" />
                            <Text style={[styles.hubActionChipText, { color: '#FF4444' }]}>Delete Crew</Text>
                          </TouchableOpacity>
                        )
                      )}
                    </View>
                  </View>
                )}
              </View>
            );
          })
        )}

        {/* Single full-width private code join button */}
        <TouchableOpacity
          style={[styles.hubCrewActionBtn, { width: '100%', marginTop: 4 }]}
          onPress={() => setShowCodeEntry(!showCodeEntry)}
          activeOpacity={0.75}
        >
          <MaterialCommunityIcons name="lock-outline" size={16} color={showCodeEntry ? Colors.primary : Colors.textMuted} />
          <Text style={[styles.hubCrewActionBtnText, showCodeEntry && { color: Colors.primary }]}>
            Join Private Crew with Code
          </Text>
          <MaterialCommunityIcons
            name={showCodeEntry ? 'chevron-up' : 'chevron-down'}
            size={16} color={Colors.textMuted}
            style={{ marginLeft: 'auto' }}
          />
        </TouchableOpacity>

        {/* Inline private code entry — join a private crew by invite code */}
        {showCodeEntry && (
          <View style={styles.hubCodeEntry}>
            <Text style={{ color: Colors.textMuted, fontSize: 11, marginBottom: 6 }}>
              🔒 Enter the 6-character invite code for the private crew
            </Text>
            <TextInput
              style={[styles.input, styles.codeInput, { marginBottom: 8 }]}
              value={inviteCode}
              onChangeText={t => setInviteCode(t.toUpperCase())}
              placeholder="ABC123"
              placeholderTextColor={Colors.textMuted}
              maxLength={6}
              autoCapitalize="characters"
              autoFocus
            />
            {errorMsg ? <Text style={styles.errorText}>{errorMsg}</Text> : null}
            <TouchableOpacity
              style={[styles.primaryBtn, isLoading && { opacity: 0.5 }, { marginTop: 4 }]}
              onPress={handleJoinByCode}
              disabled={isLoading}
            >
              {isLoading
                ? <ActivityIndicator color="#000" />
                : <Text style={styles.primaryBtnText}>Join Crew</Text>
              }
            </TouchableOpacity>
          </View>
        )}

        {/* ── LIVE NEAR YOU ── */}
        <View style={styles.hubSectionRow}>
          <Text style={styles.hubSectionLabel}>🔴 LIVE NEAR YOU</Text>
          <TouchableOpacity onPress={() => refreshNearby()}>
            <MaterialCommunityIcons name="refresh" size={15} color={Colors.textMuted} />
          </TouchableOpacity>
        </View>

        {/* Radius pills */}
        <ScrollView horizontal showsHorizontalScrollIndicator={false}
          style={{ width: '100%', marginBottom: 10 }}
          contentContainerStyle={styles.radiusPillRow}>
          {([20, 50, 100, 250, null] as (number | null)[]).map(r => {
            const label = r === null ? 'Show All' : `${r} mi`;
            const active = discoverRadiusMi === r;
            return (
              <TouchableOpacity
                key={String(r)}
                style={[styles.radiusPill, active && styles.radiusPillActive]}
                onPress={() => setDiscoverRadiusMi(r)}
              >
                <Text style={[styles.radiusPillText, active && styles.radiusPillTextActive]}>{label}</Text>
              </TouchableOpacity>
            );
          })}
        </ScrollView>

        {isLoadingNearby ? (
          <ActivityIndicator color={Colors.primary} style={{ marginVertical: 16 }} />
        ) : nearbySessions.length === 0 ? (
          <View style={styles.hubEmptyCard}>
            <Text style={styles.hubEmptyText}>
              {discoverRadiusMi != null
                ? `No live sessions within ${discoverRadiusMi} mi`
                : 'No live sessions near you right now'}
            </Text>
            <Text style={[styles.hubEmptyText, { fontSize: 11, marginTop: 4 }]}>Start one and skaters nearby will see it!</Text>
          </View>
        ) : (
          nearbySessions.map(s => (
            <TouchableOpacity
              key={s.id}
              style={styles.nearbySessionCard}
              onPress={() => handleJoinById(s.id)}
              disabled={!!joiningSessionId}
              activeOpacity={0.75}
            >
              <View style={{ flex: 1 }}>
                <Text style={styles.nearbySessionName}>{s.crewName || s.name}</Text>
                <Text style={styles.nearbySessionSub}>
                  {s.crewName && s.crewName !== s.name ? `${s.name} · ` : ''}
                  {s.memberCount} skater{s.memberCount !== 1 ? 's' : ''}
                  {s.distanceLabel ? ` · ${s.distanceLabel}` : ''}
                </Text>
              </View>
              {joiningSessionId === s.id ? (
                <ActivityIndicator size="small" color="#000" />
              ) : (
                <View style={styles.nearbyJoinBtn}>
                  <Text style={styles.nearbyJoinText}>JOIN ▶</Text>
                </View>
              )}
            </TouchableOpacity>
          ))
        )}


        {/* ── Schedule ── */}
        <TouchableOpacity
          style={[styles.secondaryBtn, { marginTop: 20 }]}
          onPress={() => { setStep('schedule'); setErrorMsg(''); }}
        >
          <MaterialCommunityIcons name="calendar-clock" size={18} color={Colors.primary} />
          <Text style={styles.secondaryBtnText}>📅 Schedule a Session</Text>
        </TouchableOpacity>

        <View style={{ height: 32 }} />
      </ScrollView>
    );
  
}
