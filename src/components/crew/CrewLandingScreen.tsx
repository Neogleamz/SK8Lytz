import { MaterialCommunityIcons } from '@expo/vector-icons';
import * as Clipboard from 'expo-clipboard';
import React, { useEffect, useRef, useState } from 'react';
import { ActivityIndicator, Alert, Animated, Image, Platform, ScrollView, Share, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { CrewLandingMap } from './CrewLandingMap';
import { MapFiltersTray } from './MapFiltersTray';
import { useMapFilters } from '../../hooks/useMapFilters';
import { useCrewContext } from '../../context/CrewContext';
import { useTheme } from '../../context/ThemeContext';
import { useAppConfig } from '../../context/AppConfigContext';
import { AppLogger } from '../../services/appLogger';
import { crewService, CrewSession } from '../../services/CrewService';
import { PermanentCrew, profileService } from '../../services/ProfileService';
import { Spacing } from '../../theme/theme';
import { createStyles } from './CrewStyles';
import { ErrorCard } from '../ErrorCard';
import { EmptyState } from '../EmptyState';

import { CrewCard } from './CrewCard';

function timeAgo(iso: string): string {
  const diff = Date.now() - new Date(iso).getTime();
  const m = Math.floor(diff / 60000);
  if (m < 1) return 'just now';
  if (m < 60) return `${m}m ago`;
  return `${Math.floor(m / 60)}h ago`;
}

export function CrewLandingScreen({ onClose, showOnlyMap }: { onClose?: () => void, showOnlyMap?: boolean }) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const { isVisibilityAllowed } = useAppConfig();
  const showMap = isVisibilityAllowed('visibility_maps_tab');
  const { filters, toggleFilter, applyFilters } = useMapFilters();
  
  const context = useCrewContext();
  const { hub, manage, session, setStep, step, confirmAction, setConfirmAction, currentUserId, displayName, errorMsg, setErrorMsg, isLoading, setIsLoading, showCodeEntry, setShowCodeEntry, formState } = context;
  const { activeSessions, myCrews, permanentCrews, isLoadingNearby, refreshNearby, nearbySessions, nearbySpots, discoverRadiusMi, setDiscoverRadiusMi, locationLabel, locationCoords, crewMemberCounts, nearbyStatus } = hub;
  const { selectedCrewDetail, setSelectedCrewDetail, expandedCrewId, setExpandedCrewId, cardMembers, setCardMembers, loadingCardMembersFor, makingOwnerFor, setMakingOwnerFor, confirmingDeleteCrewId, setConfirmingDeleteCrewId, confirmingLeaveCrewId, setConfirmingLeaveCrewId, createCrewError, setCreateCrewError, isCreatingCrew, newCrewName, setNewCrewName, newCrewIsPublic, setNewCrewIsPublic, newCrewCity, setNewCrewCity, newCrewState, setNewCrewState, loadCrewMembers } = manage;
  const { currentSession, isHandoffMode, executeLeaveSession, executeEndSession, handleHandoffLeadership, handleSessionJoined } = session;
  
  const [inviteCode, setInviteCode] = useState('');
  const [joiningSessionId, setJoiningSessionId] = useState<string | null>(null);
  const [showRadiusPicker, setShowRadiusPicker] = useState(false);
  const [error, setError] = useState<string | null>(null);

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
      const sessionData = await crewService.joinSessionById(sessionId, displayName.trim(), currentUserId ?? undefined);
      AppLogger.log('CREW_SESSION_JOINED', { sessionId: sessionData.id, method: 'browse' });
      await handleSessionJoined(sessionData);
    } catch (err: unknown) {
      const e = err instanceof Error ? err : new Error((err instanceof Error ? err.message : String(err)));
      AppLogger.log('CREW_ERROR', { action: 'join_id', error: e instanceof Error ? e.message : String(e)  });
      setErrorMsg(e.message || 'Could not join that crew');
      setError('Failed to load. Tap to retry.');
    } finally { 
      setIsLoading(false); 
      setJoiningSessionId(null);
    }
  };

  const handleStartEdit = (crew: PermanentCrew) => {
    manage.setEditingCrewId(crew.id);
    manage.setEditCrewName(crew.name);
    manage.setEditCrewIsPublic(crew.is_public ?? false);
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
            if (!currentUserId) return;
            setIsLoading(true);
            try {
              await profileService.leavePermanentCrew(crew.id, currentUserId);
              AppLogger.log('CREW_LEFT', { crewId: crew.id, crewName: crew.name });
              const updated = await profileService.getMyCrew(undefined, currentUserId);
              hub.setMyCrews(updated);
              hub.setPermanentCrews(updated.map(c => ({ id: c.id, name: c.name })));
            } catch (err: unknown) {
              const e = err instanceof Error ? err : new Error((err instanceof Error ? err.message : String(err)));
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
    if (!currentUserId) return;
    setIsLoading(true);
    try {
      await profileService.deleteCrew(crew.id, currentUserId);
      AppLogger.log('CREW_DELETED', { crewId: crew.id, crewName: crew.name });
      const updated = await profileService.getMyCrew(undefined, currentUserId);
      hub.setMyCrews(updated);
      hub.setPermanentCrews(updated.map(c => ({ id: c.id, name: c.name })));
      manage.setConfirmingDeleteCrewId(null);
    } catch (err: unknown) {
      const e = err instanceof Error ? err : new Error((err instanceof Error ? err.message : String(err)));
      Alert.alert('Error', e.message || 'Could not delete crew');
    } finally {
      setIsLoading(false);
    }
  };

  const handleJoinByCode = async () => {
    if (inviteCode.trim().length < 6) { setErrorMsg('Enter the 6-character crew invite code'); return; }
    if (!currentUserId) { setErrorMsg('Not logged in'); return; }
    setIsLoading(true); setErrorMsg('');
    try {
      const crew = await profileService.joinPermanentCrew(inviteCode.trim(), currentUserId);
      AppLogger.log('CREW_SESSION_JOINED', { crewId: crew.id, method: 'permanent_code' });
      const updatedCrews = await profileService.getMyCrew(undefined, currentUserId);
      hub.setMyCrews(updatedCrews);
      hub.setPermanentCrews(updatedCrews.map(c => ({ id: c.id, name: c.name })));
      setShowCodeEntry(false);
      setInviteCode('');
      Alert.alert(
        '🛹 Joined!',
        `You're now a member of "${crew.name}". When they start a session you'll see it under My Crews.`,
        [{ text: 'Nice!' }]
      );
    } catch (err: unknown) {
      const e = err instanceof Error ? err : new Error((err instanceof Error ? err.message : String(err)));
      AppLogger.log('CREW_ERROR', { action: 'join_crew_by_code', error: e instanceof Error ? e.message : String(e)  });
      setErrorMsg(e.message || 'Crew not found — check the code and try again.');
      setError('Failed to load. Tap to retry.');
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
      <ScrollView contentContainerStyle={[styles.body, { paddingTop: Spacing.xs }]} showsVerticalScrollIndicator={false}>
        {/* ── Header ── */}
        <View style={[styles.hubHeader, { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' }]}>
          <View>
            <Text style={styles.hubTitle}>{showOnlyMap ? 'Explore Skate Map' : 'CREWZ HUB'}</Text>
            <Text style={styles.hubSub}>{showOnlyMap ? 'Discover spots and live sessions nearby' : 'Skate together · sync your light show'}</Text>
          </View>
          {onClose && (
            <TouchableOpacity onPress={onClose} style={{ padding: Spacing.sm, backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 20 }}>
              <MaterialCommunityIcons name="close" size={20} color={Colors.textMuted} />
            </TouchableOpacity>
          )}
        </View>

        {error && <ErrorCard message={error} onRetry={() => setError(null)} />}

        {/* ── MY CREWS ── */}
        {!showOnlyMap && (
          <>
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: Spacing.sm, marginTop: Spacing.xs, width: '100%' }}>
          <Text style={[styles.hubSectionLabel, { marginBottom: 0, marginTop: 0 }]}>MY CREWS</Text>
          {myCrews.length > 0 && (
            <TouchableOpacity onPress={() => setStep('manage')} style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: 'rgba(255,170,0,0.1)', paddingHorizontal: Spacing.md, paddingVertical: Spacing.xs, borderRadius: 12 }}>
              <MaterialCommunityIcons name="plus" size={14} color={Colors.primary} />
              <Text style={{ color: Colors.primary, fontSize: 11, fontWeight: '700', marginLeft: Spacing.xs }}>New CREW</Text>
            </TouchableOpacity>
          )}
        </View>

        {myCrews.length === 0 ? (
          <View style={styles.hubEmptyCard}>
            <MaterialCommunityIcons name="account-group-outline" size={32} color={Colors.textMuted} />
            <Text style={styles.hubEmptyText}>You haven't joined any crews yet</Text>
            <TouchableOpacity
              style={[styles.hubActionChip, { marginTop: Spacing.md }]}
              onPress={() => setStep('manage')}
            >
              <MaterialCommunityIcons name="plus" size={14} color={Colors.primary} />
              <Text style={styles.hubActionChipText}>Create a CREW</Text>
            </TouchableOpacity>
          </View>
        ) : (
          myCrews.map(crew => {
            const liveSession = getLiveSessionForCrew(crew);
            const memberInfo = crewMemberCounts[crew.id];
            const isOwner = crew.is_owner;
            return (
              <CrewCard
                key={crew.id}
                crew={crew}
                styles={styles}
                Colors={Colors}
                liveSession={liveSession}
                memberInfo={memberInfo}
                cardMembers={cardMembers}
                isOwner={isOwner ?? false}
                expandedCrewId={expandedCrewId}
                setExpandedCrewId={setExpandedCrewId}
                loadCrewMembers={loadCrewMembers}
                handleJoinById={handleJoinById}
                isLoading={isLoading}
                pulseAnim={pulseAnim}
                formState={formState}
                setStep={setStep}
                setErrorMsg={setErrorMsg}
                loadingCardMembersFor={loadingCardMembersFor}
                currentUserId={currentUserId}
                makingOwnerFor={makingOwnerFor}
                setMakingOwnerFor={setMakingOwnerFor}
                profileService={profileService}
                setCardMembers={setCardMembers}
                handleStartEdit={handleStartEdit}
                setSelectedCrewDetail={setSelectedCrewDetail}
                handleLeaveCrew={handleLeaveCrew}
                confirmingDeleteCrewId={confirmingDeleteCrewId}
                setConfirmingDeleteCrewId={setConfirmingDeleteCrewId}
                executeDeleteCrew={executeDeleteCrew}
                timeAgo={timeAgo}
              />
            );
          })
        )}

        {/* Single full-width private code join button */}
        <TouchableOpacity
          style={[styles.hubCrewActionBtn, { width: '100%', marginTop: Spacing.xs }]}
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
            <Text style={{ color: Colors.textMuted, fontSize: 11, marginBottom: Spacing.sm }}>
              🔒 Enter the 6-character invite code for the private crew
            </Text>
            <TextInput
              style={[styles.input, styles.codeInput, { marginBottom: Spacing.sm }]}
              value={inviteCode}
              onChangeText={t => setInviteCode(t.toUpperCase())}
              placeholder="ABC123"
              placeholderTextColor={Colors.textMuted}
              maxLength={6}
              autoCapitalize="characters"
              autoFocus
            />
            {errorMsg ? <ErrorCard message={errorMsg} onRetry={handleJoinByCode} /> : null}
            <TouchableOpacity
              style={[styles.primaryBtn, isLoading && { opacity: 0.5 }, { marginTop: Spacing.xs }]}
              onPress={handleJoinByCode}
              disabled={isLoading}
            >
              {isLoading
                ? <ActivityIndicator color="#000" />
                : <Text style={styles.primaryBtnText}>Join CREW</Text>
              }
            </TouchableOpacity>
          </View>
        )}
        </>
        )}

        {/* ── LIVE NEAR YOU MAP ── */}
        <View style={styles.hubSectionRow}>
          <Text style={styles.hubSectionLabel}>🔴 LIVE NEAR YOU MAP</Text>
          <TouchableOpacity onPress={() => refreshNearby()} disabled={isLoadingNearby}>
            {isLoadingNearby ? <ActivityIndicator size={15} color={Colors.textMuted} /> : <MaterialCommunityIcons name="refresh" size={15} color={Colors.textMuted} />}
          </TouchableOpacity>
        </View>

        {/* Unified Map Filters Bar */}
        {showMap && (
          <View
            style={{ width: '100%', marginBottom: Spacing.sm, flexDirection: 'row', alignItems: 'center', gap: 6 }}>
            
            {/* Radius Dropdown — Option C: inline absolute dropdown */}
            <View style={{ position: 'relative' }}>
              <TouchableOpacity 
                style={[styles.radiusPill, { flexDirection: 'row', alignItems: 'center', backgroundColor: 'rgba(255,255,255,0.1)', borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)', paddingVertical: 6, paddingHorizontal: 10, gap: 4 }]} 
                onPress={() => setShowRadiusPicker(p => !p)}>
                <MaterialCommunityIcons name="radar" size={14} color="#FFF" />
                <Text style={[styles.radiusPillTextActive, { fontSize: 11, fontWeight: '700' }]}>
                  {discoverRadiusMi === null ? 'All' : `${discoverRadiusMi} mi`}
                </Text>
                <MaterialCommunityIcons name={showRadiusPicker ? 'chevron-up' : 'chevron-down'} size={11} color="#FFAA00" />
              </TouchableOpacity>

              {showRadiusPicker && (
                <View style={{
                  position: 'absolute',
                  top: 38,
                  left: 0,
                  backgroundColor: '#1A1A1A',
                  borderRadius: 10,
                  borderWidth: 1,
                  borderColor: 'rgba(255,170,0,0.35)',
                  zIndex: 200,
                  minWidth: 110,
                  overflow: 'hidden',
                }}>
                  {([10, 20, 50, 100, 250, null] as (number | null)[]).map((r, idx) => {
                    const isActive = discoverRadiusMi === r;
                    const label = r != null ? `${r} mi` : 'All';
                    return (
                      <TouchableOpacity
                        key={String(r)}
                        style={[
                          { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', paddingHorizontal: 14, paddingVertical: 9 },
                          isActive && { backgroundColor: 'rgba(255,170,0,0.1)' },
                          idx < 5 && { borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.05)' },
                        ]}
                        onPress={() => { setDiscoverRadiusMi(r); setShowRadiusPicker(false); }}
                      >
                        <Text style={{ color: isActive ? '#FFAA00' : '#CCC', fontSize: 13, fontWeight: '600' }}>{label}</Text>
                        {isActive && <MaterialCommunityIcons name="check" size={12} color="#FFAA00" />}
                      </TouchableOpacity>
                    );
                  })}
                </View>
              )}
            </View>
            
            {/* Map Filters */}
            <MapFiltersTray filters={filters} toggleFilter={toggleFilter} />

          </View>
        )}

        {showMap ? (
          <View style={{ height: 350, width: '100%', borderRadius: 16, overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)', marginBottom: Spacing.md }}>
            {isLoadingNearby ? (
               <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
                 <ActivityIndicator color={Colors.primary} />
               </View>
            ) : (
              <>
                <CrewLandingMap
                   nearbySpots={applyFilters(nearbySpots)}
                   nearbySessions={filters.showCrewSessions ? nearbySessions : []}
                   pulseAnim={pulseAnim}
                   handleJoinById={handleJoinById}
                   locationCoords={locationCoords ?? null}
                   discoverRadiusMi={discoverRadiusMi}
                 />
              </>
            )}
          </View>
        ) : (
          <View style={{ marginBottom: Spacing.md, padding: Spacing.lg, backgroundColor: 'rgba(255, 255, 255, 0.05)', borderRadius: 16, alignItems: 'center' }}>
            <MaterialCommunityIcons name="map-marker-off-outline" size={32} color={Colors.textMuted} style={{ marginBottom: Spacing.sm }} />
            <Text style={{ color: Colors.textMuted, fontSize: 13, fontWeight: '600', textAlign: 'center' }}>Map View Disabled</Text>
          </View>
        )}

        {nearbyStatus === 'error' && (
          <View style={[styles.hubEmptyCard, { marginTop: 0 }]}>
            <ErrorCard message="Failed to load live sessions near you." onRetry={refreshNearby} />
          </View>
        )}

        {nearbyStatus === 'success' && nearbySessions.length === 0 && (
          <View style={[styles.hubEmptyCard, { marginTop: 0 }]}>
            <EmptyState message={
              discoverRadiusMi != null
                ? `No live sessions within ${discoverRadiusMi} mi`
                : 'No live sessions near you right now'
            } />
            <Text style={[styles.hubEmptyText, { fontSize: 11, marginTop: Spacing.xs }]}>Start one and skaters nearby will see it!</Text>
          </View>
        )}


        {/* ── Schedule ── */}
        {!showOnlyMap && (
           <TouchableOpacity
             style={[styles.secondaryBtn, { marginTop: Spacing.xl }]}
             onPress={() => { setStep('schedule'); setErrorMsg(''); }}
           >
             <MaterialCommunityIcons name="calendar-clock" size={18} color={Colors.primary} />
             <Text style={styles.secondaryBtnText}>📅 Schedule a Session</Text>
           </TouchableOpacity>
        )}

        <View style={{ height: 32 }} />
      </ScrollView>
    );
  
}
