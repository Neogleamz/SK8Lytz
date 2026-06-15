import { MaterialCommunityIcons } from '@expo/vector-icons';
import React, { useState } from 'react';
import { ActivityIndicator, Alert, FlatList, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { useTheme } from '../../context/ThemeContext';
import { AppLogger } from '../../services/appLogger';
import { crewService, CrewSession } from '../../services/CrewService';
import { profileService, PermanentCrew } from '../../services/ProfileService';
import { Spacing } from '../../theme/theme';

import { useCrewContext } from '../../context/CrewContext';
import { createStyles } from './CrewStyles';
import { ErrorCard } from '../ErrorCard';
import { EmptyState } from '../EmptyState';

export function CrewJoinScreen() {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const context = useCrewContext();
  const { hub, manage, session, setStep, step, confirmAction, setConfirmAction, currentUserId, displayName, setDisplayName, errorMsg, setErrorMsg, isLoading, setIsLoading, showCodeEntry, setShowCodeEntry, formState } = context;
  const { activeSessions, myCrews, permanentCrews, isLoadingNearby, refreshNearby, nearbySessions, discoverRadiusMi, setDiscoverRadiusMi, locationLabel, handleDetectLocation, isGettingLocation, loadActiveSessions, sessionsStatus } = hub;
  const { selectedCrewDetail, setSelectedCrewDetail, expandedCrewId, setExpandedCrewId, cardMembers, setCardMembers, loadingCardMembersFor, makingOwnerFor, setMakingOwnerFor, confirmingDeleteCrewId, setConfirmingDeleteCrewId, confirmingLeaveCrewId, setConfirmingLeaveCrewId, createCrewError, setCreateCrewError, isCreatingCrew, newCrewName, setNewCrewName, newCrewDescription, setNewCrewDescription, newCrewIsPublic, setNewCrewIsPublic, newCrewCity, setNewCrewCity, newCrewState, setNewCrewState } = manage;
  const { currentSession, isHandoffMode, executeLeaveSession, executeEndSession, handleHandoffLeadership, handleSessionJoined } = session;
  
  const { inviteCode, setInviteCode } = formState;
  const [error, setError] = useState<string | null>(null);

  const keyExtractor = React.useCallback((s: CrewSession) => s.id, []);

  const handleJoinByCode = async () => {
    if (inviteCode.trim().length < 6) { setErrorMsg('Enter the 6-character crew invite code'); return; }
    if (!currentUserId) { setErrorMsg('Not logged in'); return; }
    setIsLoading(true); setErrorMsg('');
    try {
      const crew = await profileService.joinPermanentCrew(inviteCode.trim(), currentUserId);
      AppLogger.log('CREW_SESSION_JOINED', { crewId: crew.id, method: 'permanent_code' });
      const updatedCrews = await profileService.getMyCrew(undefined, currentUserId);
      hub.setMyCrews(updatedCrews);
      hub.setPermanentCrews(updatedCrews.map((c: PermanentCrew) => ({ id: c.id, name: c.name })));
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

  const handleJoinById = React.useCallback(async (sessionId: string) => {
    setIsLoading(true); setErrorMsg('');
    try {
      const sess = await crewService.joinSessionById(sessionId, displayName.trim(), currentUserId ?? undefined);
      AppLogger.log('CREW_SESSION_JOINED', { sessionId: sess.id, method: 'browse' });
      await handleSessionJoined(sess);
      setStep('controller');
    } catch (err: unknown) {
      const e = err instanceof Error ? err : new Error((err instanceof Error ? err.message : String(err)));
      AppLogger.log('CREW_ERROR', { action: 'join_id', error: e instanceof Error ? e.message : String(e)  });
      setErrorMsg(e.message || 'Failed to join session');
      setError('Failed to load. Tap to retry.');
    } finally { setIsLoading(false); }
  }, [currentUserId, displayName, handleSessionJoined, setErrorMsg, setIsLoading, setStep]);

  const renderActiveSessionCard = React.useCallback(({ item }: { item: CrewSession }) => {
    const isOwn = item.leader_user_id === currentUserId;
    return (
      <TouchableOpacity style={styles.sessionCard} onPress={() => handleJoinById(item.id)}
        disabled={isLoading} activeOpacity={0.75}>
        <View style={localStyles.flex1}>
          <Text style={styles.sessionCardName}>{item.name}</Text>
          <View style={localStyles.sessionMetaContainer}>
            {item.location_label && (
              <View style={localStyles.locationContainer}>
                <MaterialCommunityIcons name="map-marker" size={12} color={Colors.textMuted} />
                <Text style={styles.sessionCardMeta} numberOfLines={1}>{item.location_label}</Text>
              </View>
            )}
            <Text style={styles.sessionCardMeta}>
              {item.member_count ?? 0} {(item.member_count ?? 0) === 1 ? 'skater' : 'skaters'}
            </Text>
          </View>
        </View>
        <View style={styles.sessionCardRight}>
          {isOwn && <Text style={localStyles.isOwnEmoji}>👑</Text>}
          <View style={styles.joinPill}>
            <Text style={styles.joinPillText}>{isOwn ? 'Rejoin' : 'Join'}</Text>
          </View>
        </View>
      </TouchableOpacity>
    );
  }, [currentUserId, handleJoinById, isLoading, Colors.textMuted, styles]);

  return (
<View style={{ flex: 1 }}>
      <View style={styles.body}>
        <TouchableOpacity onPress={() => setStep('landing')} style={styles.backBtn}>
          <MaterialCommunityIcons name="chevron-left" size={22} color={Colors.textMuted} />
          <Text style={styles.backText}>Back</Text>
        </TouchableOpacity>
        <Text style={styles.titleLarge}>Join a CREW</Text>

        <Text style={styles.label}>YOUR NAME IN CREW</Text>
        <TextInput style={styles.input} value={displayName} onChangeText={setDisplayName}
          placeholder="Display name" placeholderTextColor={Colors.textMuted} maxLength={24} />

        {/* Live sessions browser */}
        <View style={localStyles.browserContainer}>
          <View style={localStyles.browserHeader}>
            <Text style={styles.label}>🟢 CREWS SKATING NOW</Text>
            <TouchableOpacity onPress={loadActiveSessions}>
              <MaterialCommunityIcons name="refresh" size={15} color={Colors.textMuted} />
            </TouchableOpacity>
          </View>
          {sessionsStatus === 'loading' ? (
            <ActivityIndicator color={Colors.primary} style={localStyles.spinner} />
          ) : sessionsStatus === 'error' ? (
            <ErrorCard message="Failed to load active crews. Tap refresh to try again." onRetry={loadActiveSessions} />
          ) : activeSessions.length === 0 ? (
            <EmptyState message="No active crews right now. Be the first!" />
          ) : (
            <FlatList removeClippedSubviews={true} initialNumToRender={12} windowSize={5} data={activeSessions} keyExtractor={keyExtractor}
              renderItem={renderActiveSessionCard} scrollEnabled={false} />
          )}
        </View>

        {/* Divider */}
        <View style={styles.divider}>
          <View style={styles.dividerLine} />
          <Text style={styles.dividerText}>OR ENTER CODE</Text>
          <View style={styles.dividerLine} />
        </View>

        <TextInput style={[styles.input, styles.codeInput]}
          value={inviteCode} onChangeText={t => setInviteCode(t.toUpperCase())}
          placeholder="ABC123" placeholderTextColor={Colors.textMuted}
          maxLength={6} autoCapitalize="characters" />
        {errorMsg ? <ErrorCard message={errorMsg} onRetry={handleJoinByCode} /> : null}

        <TouchableOpacity style={[styles.primaryBtn, isLoading && { opacity: 0.5 }]}
          onPress={handleJoinByCode} disabled={isLoading}>
          {isLoading ? <ActivityIndicator color="#000" /> : <Text style={styles.primaryBtnText}>Join with Code</Text>}
        </TouchableOpacity>
      </View>
    </View>
  

  );
}

const localStyles = {
  flex1: { flex: 1 },
  sessionMetaContainer: { flexDirection: 'row' as const, alignItems: 'center' as const, gap: Spacing.sm, marginTop: Spacing.xxs },
  locationContainer: { flexDirection: 'row' as const, alignItems: 'center' as const, gap: Spacing.xxs },
  isOwnEmoji: { fontSize: 14 },
  browserContainer: { width: '100%' as const, marginTop: Spacing.lg },
  browserHeader: { flexDirection: 'row' as const, alignItems: 'center' as const, justifyContent: 'space-between' as const, marginBottom: Spacing.sm },
  spinner: { marginVertical: Spacing.lg },
  emptyText: { marginTop: 0, fontSize: 12 }
};