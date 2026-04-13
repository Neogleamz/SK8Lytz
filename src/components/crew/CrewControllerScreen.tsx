import { Spacing } from '../../theme/theme';
import React, { useRef, useEffect } from 'react';
import { View, Text, TouchableOpacity, ScrollView, Animated, ActivityIndicator, Alert, Share, TextInput, Image, RefreshControl } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../../context/ThemeContext';
import * as Clipboard from 'expo-clipboard';
import { profileService, PermanentCrew } from '../../services/ProfileService';
import { crewService, CrewSession, CrewMember } from '../../services/CrewService';
import { locationService } from '../../services/LocationService';
import { AppLogger } from '../../services/AppLogger';
import DateTimePicker from '@react-native-community/datetimepicker';
import * as ImagePicker from 'expo-image-picker';

import { createStyles } from './CrewStyles';
import { useCrewContext } from '../../context/CrewContext';
import CrewMemberDashboard from '../CrewMemberDashboard';

function timeAgo(iso: string): string {
  const diff = Date.now() - new Date(iso).getTime();
  const m = Math.floor(diff / 60000);
  if (m < 1) return 'just now';
  if (m < 60) return `${m}m ago`;
  return `${Math.floor(m / 60)}h ago`;
}

export interface CrewControllerScreenProps {
  onClose: () => void;
  currentModeSummary?: string;
  lastLeaderScene?: Record<string, any> | null;
}

export function CrewControllerScreen({ onClose, currentModeSummary, lastLeaderScene }: CrewControllerScreenProps) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const context = useCrewContext();
  const pulseAnim = React.useRef(new Animated.Value(1)).current;
  const { hub, manage, session, setStep, step, confirmAction, setConfirmAction, currentUserId, displayName, errorMsg, setErrorMsg, isLoading, setIsLoading, showCodeEntry, setShowCodeEntry, formState } = context;
  const { activeSessions, myCrews, permanentCrews, isLoadingNearby, refreshNearby, nearbySessions, discoverRadiusMi, setDiscoverRadiusMi, locationLabel, handleDetectLocation, isGettingLocation } = hub;
  const { selectedCrewDetail, setSelectedCrewDetail, expandedCrewId, setExpandedCrewId, cardMembers, setCardMembers, loadingCardMembersFor, makingOwnerFor, setMakingOwnerFor, confirmingDeleteCrewId, setConfirmingDeleteCrewId, confirmingLeaveCrewId, setConfirmingLeaveCrewId, createCrewError, setCreateCrewError, isCreatingCrew, newCrewName, setNewCrewName, newCrewDescription, setNewCrewDescription, newCrewIsPublic, setNewCrewIsPublic, newCrewCity, setNewCrewCity, newCrewState, setNewCrewState } = manage;
  const { currentSession, isHandoffMode, setIsHandoffMode, currentRole, members, executeLeaveSession, executeEndSession, handleHandoffLeadership } = session;
  
  const handleLeave = async () => {
    try { await executeLeaveSession(); } catch (e) { console.warn(e); }
  };
  
  const handleEndSession = async () => {
    try { await executeEndSession(); } catch (e) { console.warn(e); }
  };

  const renderMemberRow = ({ item }: { item: CrewMember }) => {
    const isLeader = item.user_id === currentSession?.leader_user_id;
    const isMe = item.user_id === currentUserId;
    const canHandoff = currentRole === 'leader' && !isLeader && isHandoffMode;
    return (
      <View style={styles.memberRow}>
        <View style={[styles.memberAvatar, isLeader && { borderColor: '#FFD700', borderWidth: 2 }]}>
          <Text style={styles.memberAvatarText}>{(item.display_name?.[0] ?? '?').toUpperCase()}</Text>
        </View>
        <View style={{ flex: 1, marginLeft: Spacing.md }}>
          <Text style={styles.memberName}>{item.display_name}{isMe ? ' (you)' : ''}</Text>
          {isLeader && <Text style={styles.memberLeaderBadge}>👑 Leader</Text>}
        </View>
        {canHandoff && !isMe && (
          <TouchableOpacity style={styles.handoffBtn} onPress={() => handleHandoffLeadership(item)}>
            <MaterialCommunityIcons name="crown" size={12} color="#000" />
            <Text style={styles.handoffBtnText}>Make Leader</Text>
          </TouchableOpacity>
        )}
      </View>
    );
  };

  if (!currentSession) return null;
    // Triple-check to be resilient against async/singleton state desync:
    // 1. React state currentRole (set by handleSessionJoined)
    // 2. Service singleton currentRole (set by createSession/joinSession)
    // 3. Direct leader_user_id comparison (works even on modal reopen)
    const isLeader =
      currentRole === 'leader' ||
      crewService.currentRole === 'leader' ||
      currentSession.leader_user_id === currentUserId;
    const hasLocation = !!currentSession.location_label;
    const startedAt = timeAgo(currentSession.created_at);

    return (
      <View style={{ flex: 1, overflow: 'hidden' }}>
        {/* ─── Session Card Header ─── */}
        <View style={styles.controllerCard}>
          {/* Crew name + live pill */}
          <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: Spacing.sm }}>
            <Text style={styles.controllerCrewName} numberOfLines={1}>{currentSession.name}</Text>
            <View style={styles.livePill}>
              <Animated.View style={[styles.liveDot, { opacity: pulseAnim }]} />
              <Text style={styles.liveText}>LIVE</Text>
            </View>
          </View>

          {/* Role */}
          <Text style={styles.controllerRole}>
            {isLeader ? '👑 You are leading this crew' : `👑 Led by crew leader`}
          </Text>

          {/* What/Where/When row */}
          <View style={styles.metaRow}>
            {hasLocation && (
              <View style={styles.metaChip}>
                <MaterialCommunityIcons name="map-marker" size={13} color={Colors.primary} />
                <Text style={styles.metaChipText} numberOfLines={1}>{currentSession.location_label}</Text>
              </View>
            )}
            <View style={styles.metaChip}>
              <MaterialCommunityIcons name="clock-outline" size={13} color={Colors.textMuted} />
              <Text style={styles.metaChipText}>Started {startedAt}</Text>
            </View>
            <View style={styles.metaChip}>
              <MaterialCommunityIcons name="account-group" size={13} color={Colors.textMuted} />
              <Text style={styles.metaChipText}>{members.length} skater{members.length !== 1 ? 's' : ''}</Text>
            </View>
          </View>

          {/* Current mode (if known) */}
          {currentModeSummary && (
            <View style={styles.modeRow}>
              <MaterialCommunityIcons name="led-strip-variant" size={14} color={Colors.primary} />
              <Text style={styles.modeText}>{currentModeSummary}</Text>
            </View>
          )}

          {/* Leader: invite code */}
          {isLeader && (
            <TouchableOpacity style={styles.inviteCodeRow} onPress={() => {
              Clipboard.setStringAsync(currentSession.invite_code);
              Alert.alert('Copied!', `Code ${currentSession.invite_code} copied. Share it with your crew!`);
            }}>
              <Text style={styles.inviteCodeLabel}>CREW CODE</Text>
              <Text style={styles.inviteCode}>{currentSession.invite_code}</Text>
              <MaterialCommunityIcons name="content-copy" size={14} color={Colors.textMuted} />
            </TouchableOpacity>
          )}

          {/* Member: sync status */}
          {!isLeader && (
            <View style={styles.syncRow}>
              <Animated.View style={[styles.syncDot, { opacity: pulseAnim }]} />
              <Text style={styles.syncText}>Syncing with leader · skates follow automatically</Text>
            </View>
          )}
        </View>

        {/* ─── Member Dashboard (member role only) ─── */}
        {!isLeader && (
          <CrewMemberDashboard
            session={currentSession}
            role={currentRole}
            currentScene={lastLeaderScene ?? null}
            onLeave={handleLeave}
          />
        )}

        {/* ─── Leader: Member List + Handoff ─── */}
        {isLeader && (
          <ScrollView style={{ flex: 1 }} showsVerticalScrollIndicator={false}>
            {/* Leader handoff toggle */}
            <View style={{ marginHorizontal: Spacing.lg, marginTop: Spacing.md }}>
              <TouchableOpacity
                style={[styles.handoffToggle, isHandoffMode && styles.handoffToggleActive]}
                onPress={() => setIsHandoffMode(!isHandoffMode)}>
                <MaterialCommunityIcons name={isHandoffMode ? 'crown' : 'crown-outline'}
                  size={15} color={isHandoffMode ? '#000' : '#FFD700'} />
                <Text style={[styles.handoffToggleText, isHandoffMode && { color: '#000' }]}>
                  {isHandoffMode ? 'Select who gets the crown ↓' : 'Hand Off Leadership'}
                </Text>
              </TouchableOpacity>
            </View>

            <Text style={[styles.label, { marginHorizontal: Spacing.xl, marginTop: Spacing.lg }]}>
              CREW MEMBERS ({members.length})
            </Text>
            {members.length === 0
              ? <Text style={[styles.subtitle, { margin: Spacing.lg, marginTop: Spacing.xs, fontSize: 13 }]}>Waiting for skaters…</Text>
              : members.map(m => (
                <View key={m.id} style={{ paddingHorizontal: Spacing.lg }}>
                  {renderMemberRow({ item: m })}
                </View>
              ))
            }
            <View style={{ height: 16 }} />
          </ScrollView>
        )}

        {/* ─── Inline Confirm Banner (replaces Alert.alert for web+native compat) ─── */}
        {confirmAction !== null && (
          <View style={{
            marginHorizontal: Spacing.lg, marginBottom: Spacing.sm,
            backgroundColor: 'rgba(255,40,40,0.12)',
            borderWidth: 1.5, borderColor: '#FF4444',
            borderRadius: 14, padding: Spacing.lg,
          }}>
            <Text style={{ color: '#FFF', fontSize: 14, fontWeight: '700', textAlign: 'center', marginBottom: Spacing.xs }}>
              {confirmAction === 'end' ? '⚠️ End Session?' : '⚠️ Leave Session?'}
            </Text>
            <Text style={{ color: 'rgba(255,255,255,0.65)', fontSize: 12, textAlign: 'center', marginBottom: Spacing.md }}>
              {confirmAction === 'end'
                ? `All members will be notified and the session will close. Their skates keep the current pattern.`
                : `You'll leave "${currentSession?.name}". Your skates keep the current pattern.`}
            </Text>
            
            {confirmAction === 'end' && (
              <View style={{ marginBottom: Spacing.md, padding: Spacing.md, backgroundColor: 'rgba(0,0,0,0.4)', borderRadius: 10 }}>
                <Text style={{ color: '#00F0FF', fontSize: 11, fontWeight: '800', textAlign: 'center', marginBottom: Spacing.sm, letterSpacing: 1 }}>📈 SESSION SUMMARY</Text>
                <View style={{ flexDirection: 'row', justifyContent: 'space-around' }}>
                  <View style={{ alignItems: 'center' }}>
                    <Text style={{ color: '#FFF', fontSize: 22, fontWeight: '900' }}>{crewService.sessionTelemetry.distanceMiles.toFixed(1)}</Text>
                    <Text style={{ color: 'rgba(255,255,255,0.4)', fontSize: 9, fontWeight: '800', letterSpacing: 1 }}>MILES</Text>
                  </View>
                  <View style={{ alignItems: 'center' }}>
                    <Text style={{ color: '#FFD700', fontSize: 22, fontWeight: '900' }}>{crewService.sessionTelemetry.topSpeedMph.toFixed(1)}</Text>
                    <Text style={{ color: 'rgba(255,255,255,0.4)', fontSize: 9, fontWeight: '800', letterSpacing: 1 }}>TOP MPH</Text>
                  </View>
                </View>
              </View>
            )}

            <View style={{ flexDirection: 'row', gap: Spacing.md }}>
              <TouchableOpacity
                onPress={() => setConfirmAction(null)}
                style={{ flex: 1, paddingVertical: Spacing.md, borderRadius: 10, backgroundColor: 'rgba(255,255,255,0.08)', alignItems: 'center' }}
              >
                <Text style={{ color: '#aaa', fontWeight: '700' }}>Cancel</Text>
              </TouchableOpacity>
              <TouchableOpacity
                onPress={confirmAction === 'end' ? executeEndSession : executeLeaveSession}
                style={{ flex: 1, paddingVertical: Spacing.md, borderRadius: 10, backgroundColor: '#FF4444', alignItems: 'center' }}
              >
                <Text style={{ color: '#FFF', fontWeight: '800' }}>
                  {confirmAction === 'end' ? 'End Session' : 'Leave'}
                </Text>
              </TouchableOpacity>
            </View>
          </View>
        )}

        {/* ─── Footer Actions ─── */}
        <View style={styles.controllerFooter}>
          {/* ← Hub: return to hub landing without closing the modal or ending the session */}
          <TouchableOpacity
            style={[styles.endBtn, { borderColor: 'rgba(255,255,255,0.2)', paddingHorizontal: Spacing.md }]}
            onPress={() => setStep('landing')}
          >
            <MaterialCommunityIcons name="arrow-left" size={15} color="#aaa" />
            <Text style={[styles.endBtnText, { color: '#aaa' }]}> Hub</Text>
          </TouchableOpacity>

          <TouchableOpacity style={styles.doneBtn} onPress={onClose}>
            <Text style={styles.doneBtnText}>{'✓ Skate'}</Text>
          </TouchableOpacity>

          {isLeader ? (
            <TouchableOpacity style={styles.endBtn} onPress={handleEndSession}>
              <MaterialCommunityIcons name="stop-circle" size={16} color="#FF4444" />
              <Text style={styles.endBtnText}>End</Text>
            </TouchableOpacity>
          ) : (
            <TouchableOpacity style={styles.endBtn} onPress={handleLeave}>
              <Text style={styles.endBtnText}>Leave</Text>
            </TouchableOpacity>
          )}
        </View>
      </View>
    );
  
}
