import { MaterialCommunityIcons } from '@expo/vector-icons';
import React from 'react';
import { ActivityIndicator, Alert, FlatList, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { useTheme } from '../../context/ThemeContext';
import { AppLogger } from '../../services/AppLogger';
import { crewService, CrewSession } from '../../services/CrewService';
import { profileService } from '../../services/ProfileService';
import { Spacing } from '../../theme/theme';

import { useCrewContext } from '../../context/CrewContext';
import { createStyles } from './CrewStyles';

export function CrewJoinScreen() {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const context = useCrewContext();
  const { hub, manage, session, setStep, step, confirmAction, setConfirmAction, currentUserId, displayName, setDisplayName, errorMsg, setErrorMsg, isLoading, setIsLoading, showCodeEntry, setShowCodeEntry, formState } = context;
  const { activeSessions, myCrews, permanentCrews, isLoadingNearby, refreshNearby, nearbySessions, discoverRadiusMi, setDiscoverRadiusMi, locationLabel, handleDetectLocation, isGettingLocation, loadActiveSessions, isLoadingSessions } = hub;
  const { selectedCrewDetail, setSelectedCrewDetail, expandedCrewId, setExpandedCrewId, cardMembers, setCardMembers, loadingCardMembersFor, makingOwnerFor, setMakingOwnerFor, confirmingDeleteCrewId, setConfirmingDeleteCrewId, confirmingLeaveCrewId, setConfirmingLeaveCrewId, createCrewError, setCreateCrewError, isCreatingCrew, newCrewName, setNewCrewName, newCrewDescription, setNewCrewDescription, newCrewIsPublic, setNewCrewIsPublic, newCrewCity, setNewCrewCity, newCrewState, setNewCrewState } = manage;
  const { currentSession, isHandoffMode, executeLeaveSession, executeEndSession, handleHandoffLeadership, handleSessionJoined } = session;
  
  const [inviteCode, setInviteCode] = React.useState('');

  const handleJoinByCode = async () => {
    if (inviteCode.trim().length < 6) { setErrorMsg('Enter the 6-character crew invite code'); return; }
    setIsLoading(true); setErrorMsg('');
    try {
      const crew = await profileService.joinPermanentCrew(inviteCode.trim());
      AppLogger.log('CREW_SESSION_JOINED', { crewId: crew.id, crewName: crew.name, method: 'permanent_code' });
      const updatedCrews = await profileService.getMyCrew();
      hub.setMyCrews(updatedCrews);
      hub.setPermanentCrews(updatedCrews.map((c: any) => ({ id: c.id, name: c.name })));
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

  const handleJoinById = async (sessionId: string) => {
    setIsLoading(true); setErrorMsg('');
    try {
      const sess = await crewService.joinSessionById(sessionId, displayName.trim());
      AppLogger.log('CREW_SESSION_JOINED', { sessionId: sess.id, crewName: sess.name, method: 'browse' });
      await handleSessionJoined(sess);
      setStep('controller');
    } catch (e: any) {
      AppLogger.log('CREW_ERROR', { action: 'join_id', error: e.message });
      setErrorMsg(e.message || 'Failed to join session');
    } finally { setIsLoading(false); }
  };

  const renderActiveSessionCard = ({ item }: { item: CrewSession }) => {
    const isOwn = item.leader_user_id === currentUserId;
    return (
      <TouchableOpacity style={styles.sessionCard} onPress={() => handleJoinById(item.id)}
        disabled={isLoading} activeOpacity={0.75}>
        <View style={{ flex: 1 }}>
          <Text style={styles.sessionCardName}>{item.name}</Text>
          <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, marginTop: Spacing.xxs }}>
            {item.location_label && (
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.xxs }}>
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
          {isOwn && <Text style={{ fontSize: 14 }}>👑</Text>}
          <View style={styles.joinPill}>
            <Text style={styles.joinPillText}>{isOwn ? 'Rejoin' : 'Join'}</Text>
          </View>
        </View>
      </TouchableOpacity>
    );
  };

  return (
<View style={{ flex: 1 }}>
      <View style={styles.body}>
        <TouchableOpacity onPress={() => setStep('landing')} style={styles.backBtn}>
          <MaterialCommunityIcons name="chevron-left" size={22} color={Colors.textMuted} />
          <Text style={styles.backText}>Back</Text>
        </TouchableOpacity>
        <Text style={styles.titleLarge}>Join a CREWZ</Text>

        <Text style={styles.label}>YOUR NAME IN CREW</Text>
        <TextInput style={styles.input} value={displayName} onChangeText={setDisplayName}
          placeholder="Display name" placeholderTextColor={Colors.textMuted} maxLength={24} />

        {/* Live sessions browser */}
        <View style={{ width: '100%', marginTop: Spacing.lg }}>
          <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: Spacing.sm }}>
            <Text style={styles.label}>🟢 CREWS SKATING NOW</Text>
            <TouchableOpacity onPress={loadActiveSessions}>
              <MaterialCommunityIcons name="refresh" size={15} color={Colors.textMuted} />
            </TouchableOpacity>
          </View>
          {isLoadingSessions ? (
            <ActivityIndicator color={Colors.primary} style={{ marginVertical: Spacing.lg }} />
          ) : activeSessions.length === 0 ? (
            <Text style={[styles.subtitle, { marginTop: 0, fontSize: 12 }]}>No active crews right now. Be the first!</Text>
          ) : (
            <FlatList data={activeSessions} keyExtractor={s => s.id}
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
        {errorMsg ? <Text style={styles.errorText}>{errorMsg}</Text> : null}

        <TouchableOpacity style={[styles.primaryBtn, isLoading && { opacity: 0.5 }]}
          onPress={handleJoinByCode} disabled={isLoading}>
          {isLoading ? <ActivityIndicator color="#000" /> : <Text style={styles.primaryBtnText}>Join with Code</Text>}
        </TouchableOpacity>
      </View>
    </View>
  

  );
}