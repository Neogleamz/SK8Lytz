/**
 * CrewModal.tsx — SK8Lytz Crew Sync
 *
 * Handles entire crew lifecycle:
 *   Landing   → Create / Join (with active sessions browser + code entry)
 *   Lobby     → Crew info, member list, visible leader handoff, invite code
 *   On join   → Fetches last_scene for immediate late-arrival sync
 */

import React, { useState, useEffect, useCallback } from 'react';
import {
  View, Text, StyleSheet, TouchableOpacity, Modal, TextInput,
  ActivityIndicator, FlatList, Clipboard, Alert, Platform, ScrollView,
} from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../context/ThemeContext';
import { crewService, CrewSession, CrewMember, CrewRole } from '../services/CrewService';
import { supabase } from '../services/supabaseClient';

// ─── Props ────────────────────────────────────────────────────────────────────

interface CrewModalProps {
  visible: boolean;
  onClose: () => void;
  /** Called when user has joined/created — parent subscribes Realtime and applies last_scene */
  onSessionReady: (session: CrewSession, role: CrewRole, lastScene: Record<string, any> | null) => void;
  onSessionLeft: () => void;
  activeSession: CrewSession | null;
  activeRole: CrewRole;
}

// ─── Component ────────────────────────────────────────────────────────────────

export default function CrewModal({
  visible, onClose, onSessionReady, onSessionLeft, activeSession, activeRole,
}: CrewModalProps) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);

  type ModalStep = 'landing' | 'create' | 'join' | 'lobby';
  const [step, setStep] = useState<ModalStep>(activeSession ? 'lobby' : 'landing');
  const [isLoading, setIsLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');

  // Create form
  const [crewName, setCrewName] = useState('');
  const [displayName, setDisplayName] = useState('');

  // Join form
  const [inviteCode, setInviteCode] = useState('');
  const [activeSessions, setActiveSessions] = useState<CrewSession[]>([]);
  const [isLoadingSessions, setIsLoadingSessions] = useState(false);

  // Lobby
  const [currentSession, setCurrentSession] = useState<CrewSession | null>(activeSession);
  const [currentRole, setCurrentRole] = useState<CrewRole>(activeRole);
  const [members, setMembers] = useState<CrewMember[]>([]);
  const [currentUserId, setCurrentUserId] = useState('');
  const [isHandoffMode, setIsHandoffMode] = useState(false);

  // ── Effects ────────────────────────────────────────────────────────────────

  useEffect(() => {
    const loadUser = async () => {
      const { data: { user } } = await supabase.auth.getUser();
      if (!user) return;
      setCurrentUserId(user.id);
      setDisplayName(user.email?.split('@')[0] || 'Skater');
    };
    loadUser();
  }, []);

  useEffect(() => {
    if (activeSession) {
      setCurrentSession(activeSession);
      setCurrentRole(activeRole);
      setStep('lobby');
    } else {
      setStep('landing');
    }
  }, [activeSession, activeRole]);

  const loadMembers = useCallback(async () => {
    if (!currentSession) return;
    const m = await crewService.fetchMembers(currentSession.id).catch(() => []);
    setMembers(m);
  }, [currentSession]);

  useEffect(() => {
    if (step === 'lobby') loadMembers();
  }, [step, loadMembers]);

  const loadActiveSessions = useCallback(async () => {
    setIsLoadingSessions(true);
    const sessions = await crewService.fetchActiveSessions().catch(() => []);
    setActiveSessions(sessions);
    setIsLoadingSessions(false);
  }, []);

  useEffect(() => {
    if (step === 'join') loadActiveSessions();
  }, [step, loadActiveSessions]);

  // ── Handlers ───────────────────────────────────────────────────────────────

  const handleSessionJoined = async (session: CrewSession) => {
    const role: CrewRole = session.leader_user_id === currentUserId ? 'leader' : 'member';
    setCurrentSession(session);
    setCurrentRole(role);
    setStep('lobby');
    loadMembers();

    // Fetch last_scene for immediate late-arrival sync
    const lastScene = role === 'member'
      ? await crewService.fetchLastScene(session.id).catch(() => null)
      : null;

    onSessionReady(session, role, lastScene);
  };

  const handleCreate = async () => {
    if (!crewName.trim()) { setErrorMsg('Enter a crew name'); return; }
    setIsLoading(true); setErrorMsg('');
    try {
      const session = await crewService.createSession(crewName.trim(), displayName.trim());
      await handleSessionJoined(session);
    } catch (e: any) {
      setErrorMsg(e.message || 'Failed to create crew');
    } finally { setIsLoading(false); }
  };

  const handleJoinByCode = async () => {
    if (inviteCode.trim().length < 6) { setErrorMsg('Enter the 6-character crew code'); return; }
    setIsLoading(true); setErrorMsg('');
    try {
      const session = await crewService.joinSession(inviteCode.trim(), displayName.trim());
      await handleSessionJoined(session);
    } catch (e: any) {
      setErrorMsg(e.message || 'Could not find that crew. Check the code.');
    } finally { setIsLoading(false); }
  };

  const handleJoinById = async (sessionId: string) => {
    setIsLoading(true); setErrorMsg('');
    try {
      const session = await crewService.joinSessionById(sessionId, displayName.trim());
      await handleSessionJoined(session);
    } catch (e: any) {
      setErrorMsg(e.message || 'Could not join that crew');
    } finally { setIsLoading(false); }
  };

  const handleLeave = () => {
    Alert.alert('Leave Crew', `Leave "${currentSession?.name}"?`, [
      { text: 'Cancel', style: 'cancel' },
      {
        text: 'Leave', style: 'destructive',
        onPress: async () => {
          await crewService.leaveSession();
          setCurrentSession(null);
          setCurrentRole(null);
          setStep('landing');
          setIsHandoffMode(false);
          onSessionLeft();
          onClose();
        },
      },
    ]);
  };

  const handleHandoffLeadership = async (member: CrewMember) => {
    Alert.alert(
      'Pass the Leader Hat 👑',
      `Make ${member.display_name} the new crew leader? They'll control the light show for everyone.`,
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Hand Off', style: 'default',
          onPress: async () => {
            try {
              await crewService.transferLeadership(member.user_id);
              setCurrentRole('member');
              setIsHandoffMode(false);
              // Refresh member list to show new leader
              setTimeout(loadMembers, 500);
            } catch (e: any) {
              Alert.alert('Error', e.message);
            }
          },
        },
      ],
    );
  };

  const copyInviteCode = () => {
    if (!currentSession) return;
    Clipboard.setString(currentSession.invite_code);
    Alert.alert('Copied!', `Code ${currentSession.invite_code} copied. Share it with your crew!`);
  };

  // ── Render helpers ─────────────────────────────────────────────────────────

  const renderActiveSessionCard = ({ item }: { item: CrewSession }) => {
    const isOwnSession = item.leader_user_id === currentUserId;
    return (
      <TouchableOpacity
        style={styles.sessionCard}
        onPress={() => handleJoinById(item.id)}
        disabled={isLoading}
        activeOpacity={0.75}
      >
        <View style={{ flex: 1 }}>
          <Text style={styles.sessionCardName}>{item.name}</Text>
          <Text style={styles.sessionCardMeta}>
            {item.member_count ?? 0} {(item.member_count ?? 0) === 1 ? 'skater' : 'skaters'} · Live now
          </Text>
        </View>
        <View style={styles.sessionCardRight}>
          {isOwnSession && (
            <Text style={{ fontSize: 16, marginRight: 4 }}>👑</Text>
          )}
          <View style={styles.joinPill}>
            <Text style={styles.joinPillText}>{isOwnSession ? 'Rejoin' : 'Join'}</Text>
          </View>
        </View>
      </TouchableOpacity>
    );
  };

  const renderMemberRow = ({ item }: { item: CrewMember }) => {
    const isLeader   = item.user_id === currentSession?.leader_user_id;
    const isMe       = item.user_id === currentUserId;
    const canHandoff = currentRole === 'leader' && !isLeader && isHandoffMode;

    return (
      <View style={styles.memberRow}>
        <View style={[styles.memberAvatar, isLeader && { borderColor: '#FFD700', borderWidth: 2 }]}>
          <Text style={styles.memberAvatarText}>
            {(item.display_name?.[0] ?? '?').toUpperCase()}
          </Text>
        </View>
        <View style={{ flex: 1, marginLeft: 10 }}>
          <Text style={styles.memberName}>{item.display_name}{isMe ? ' (you)' : ''}</Text>
          {isLeader && <Text style={styles.memberLeaderBadge}>👑 Leading the crew</Text>}
        </View>
        {canHandoff && !isMe && (
          <TouchableOpacity
            style={styles.handoffBtn}
            onPress={() => handleHandoffLeadership(item)}
          >
            <MaterialCommunityIcons name="crown" size={14} color="#000" />
            <Text style={styles.handoffBtnText}>Make Leader</Text>
          </TouchableOpacity>
        )}
      </View>
    );
  };

  // ── Screens ────────────────────────────────────────────────────────────────

  const renderLanding = () => (
    <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
      <MaterialCommunityIcons name="account-group" size={52} color={Colors.primary} style={{ marginBottom: 12 }} />
      <Text style={styles.titleLarge}>Crew Sync</Text>
      <Text style={styles.subtitle}>
        Skate together. One leader controls the show — every crew member's skates sync instantly.
      </Text>

      <TouchableOpacity style={styles.primaryBtn} onPress={() => { setStep('create'); setErrorMsg(''); }}>
        <MaterialCommunityIcons name="plus-circle-outline" size={20} color="#000" />
        <Text style={styles.primaryBtnText}>Create a Crew</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.secondaryBtn} onPress={() => { setStep('join'); setErrorMsg(''); }}>
        <MaterialCommunityIcons name="pound" size={20} color={Colors.primary} />
        <Text style={styles.secondaryBtnText}>Join a Crew</Text>
      </TouchableOpacity>
    </ScrollView>
  );

  const renderCreate = () => (
    <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
      <TouchableOpacity onPress={() => setStep('landing')} style={styles.backBtn}>
        <MaterialCommunityIcons name="chevron-left" size={24} color={Colors.textMuted} />
        <Text style={styles.backText}>Back</Text>
      </TouchableOpacity>

      <Text style={styles.titleLarge}>Create Crew</Text>

      <Text style={styles.label}>CREW NAME</Text>
      <TextInput style={styles.input} value={crewName} onChangeText={setCrewName}
        placeholder="e.g. Friday Night Crew" placeholderTextColor={Colors.textMuted}
        maxLength={32} autoFocus />

      <Text style={styles.label}>YOUR NAME IN CREW</Text>
      <TextInput style={styles.input} value={displayName} onChangeText={setDisplayName}
        placeholder="Display name" placeholderTextColor={Colors.textMuted} maxLength={24} />

      {errorMsg ? <Text style={styles.errorText}>{errorMsg}</Text> : null}

      <TouchableOpacity style={[styles.primaryBtn, isLoading && { opacity: 0.5 }]}
        onPress={handleCreate} disabled={isLoading}>
        {isLoading ? <ActivityIndicator color="#000" /> : <Text style={styles.primaryBtnText}>Create & Start</Text>}
      </TouchableOpacity>
    </ScrollView>
  );

  const renderJoin = () => (
    <View style={{ flex: 1 }}>
      <View style={styles.body}>
        <TouchableOpacity onPress={() => setStep('landing')} style={styles.backBtn}>
          <MaterialCommunityIcons name="chevron-left" size={24} color={Colors.textMuted} />
          <Text style={styles.backText}>Back</Text>
        </TouchableOpacity>
        <Text style={styles.titleLarge}>Join a Crew</Text>

        {/* Your display name */}
        <Text style={styles.label}>YOUR NAME IN CREW</Text>
        <TextInput style={styles.input} value={displayName} onChangeText={setDisplayName}
          placeholder="Display name" placeholderTextColor={Colors.textMuted} maxLength={24} />

        {/* Active sessions browser */}
        <View style={{ width: '100%', marginTop: 18 }}>
          <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: 8 }}>
            <Text style={styles.label}>🟢 CREWS SKATING NOW</Text>
            <TouchableOpacity onPress={loadActiveSessions}>
              <MaterialCommunityIcons name="refresh" size={16} color={Colors.textMuted} />
            </TouchableOpacity>
          </View>

          {isLoadingSessions ? (
            <ActivityIndicator color={Colors.primary} style={{ marginVertical: 16 }} />
          ) : activeSessions.length === 0 ? (
            <Text style={[styles.subtitle, { marginTop: 0, fontSize: 13 }]}>
              No active crews right now. Be the first!
            </Text>
          ) : (
            <FlatList
              data={activeSessions}
              keyExtractor={s => s.id}
              renderItem={renderActiveSessionCard}
              scrollEnabled={false}
            />
          )}
        </View>

        {/* Divider */}
        <View style={styles.divider}>
          <View style={styles.dividerLine} />
          <Text style={styles.dividerText}>OR ENTER CODE</Text>
          <View style={styles.dividerLine} />
        </View>

        {/* Code input */}
        <TextInput
          style={[styles.input, styles.codeInput]}
          value={inviteCode}
          onChangeText={t => setInviteCode(t.toUpperCase())}
          placeholder="ABC123"
          placeholderTextColor={Colors.textMuted}
          maxLength={6}
          autoCapitalize="characters"
        />
        {errorMsg ? <Text style={styles.errorText}>{errorMsg}</Text> : null}

        <TouchableOpacity style={[styles.primaryBtn, isLoading && { opacity: 0.5 }]}
          onPress={handleJoinByCode} disabled={isLoading}>
          {isLoading ? <ActivityIndicator color="#000" /> : <Text style={styles.primaryBtnText}>Join with Code</Text>}
        </TouchableOpacity>
      </View>
    </View>
  );

  const renderLobby = () => (
    <View style={{ flex: 1 }}>
      {/* Header */}
      <View style={styles.lobbyHeader}>
        <View style={{ flex: 1 }}>
          <Text style={styles.lobbyCrewName}>{currentSession?.name}</Text>
          <Text style={styles.lobbyRoleBadge}>
            {currentRole === 'leader' ? '👑 You are leading this crew' : '👥 Syncing with crew leader'}
          </Text>
        </View>
      </View>

      <ScrollView style={{ flex: 1 }} showsVerticalScrollIndicator={false}>
        {/* Invite code (leader only) */}
        {currentRole === 'leader' && currentSession && (
          <TouchableOpacity style={styles.inviteCodeCard} onPress={copyInviteCode} activeOpacity={0.7}>
            <Text style={styles.inviteCodeLabel}>CREW CODE — TAP TO COPY</Text>
            <Text style={styles.inviteCode}>{currentSession.invite_code}</Text>
            <Text style={styles.inviteCodeHint}>Share this with friends at the rink</Text>
          </TouchableOpacity>
        )}

        {/* Member connect prompt */}
        {currentRole === 'member' && (
          <View style={styles.connectPrompt}>
            <MaterialCommunityIcons name="bluetooth-connect" size={22} color={Colors.primary} />
            <Text style={styles.connectPromptText}>
              Connect your skates using the{' '}
              <Text style={{ color: Colors.primary, fontWeight: '700' }}>DEVICES</Text>
              {' '}tab. Once connected, your skates automatically follow the crew leader.
            </Text>
          </View>
        )}

        {/* Leader handoff section */}
        {currentRole === 'leader' && (
          <View style={{ marginHorizontal: 16, marginTop: 8 }}>
            <TouchableOpacity
              style={[styles.handoffToggle, isHandoffMode && styles.handoffToggleActive]}
              onPress={() => setIsHandoffMode(!isHandoffMode)}
            >
              <MaterialCommunityIcons
                name={isHandoffMode ? 'crown' : 'crown-outline'}
                size={16}
                color={isHandoffMode ? '#000' : '#FFD700'}
              />
              <Text style={[styles.handoffToggleText, isHandoffMode && { color: '#000' }]}>
                {isHandoffMode ? 'Select who gets the crown ↓' : 'Hand Off Leadership'}
              </Text>
            </TouchableOpacity>
          </View>
        )}

        {/* Member list */}
        <Text style={[styles.label, { marginHorizontal: 20, marginTop: 16 }]}>
          CREW MEMBERS ({members.length})
        </Text>
        {members.length === 0 ? (
          <Text style={[styles.subtitle, { margin: 16, marginTop: 8 }]}>Waiting for skaters...</Text>
        ) : (
          members.map(m => (
            <View key={m.id} style={{ paddingHorizontal: 16 }}>
              {renderMemberRow({ item: m })}
            </View>
          ))
        )}

        <View style={{ height: 120 }} />
      </ScrollView>

      {/* Footer actions */}
      <View style={styles.lobbyFooter}>
        <TouchableOpacity style={styles.doneBtn} onPress={onClose}>
          <Text style={styles.doneBtnText}>
            {currentRole === 'leader' ? '✓ Start Skating' : '✓ Got It'}
          </Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.leaveBtn} onPress={handleLeave}>
          <Text style={styles.leaveBtnText}>Leave</Text>
        </TouchableOpacity>
      </View>
    </View>
  );

  // ── Main render ────────────────────────────────────────────────────────────

  return (
    <Modal visible={visible} animationType="slide" transparent statusBarTranslucent>
      <View style={styles.overlay}>
        <View style={styles.sheet}>
          <TouchableOpacity style={styles.closeBtn} onPress={onClose}>
            <MaterialCommunityIcons name="close" size={22} color={Colors.textMuted} />
          </TouchableOpacity>

          {step === 'landing' && renderLanding()}
          {step === 'create'  && renderCreate()}
          {step === 'join'    && renderJoin()}
          {step === 'lobby'   && renderLobby()}
        </View>
      </View>
    </Modal>
  );
}

// ─── Styles ───────────────────────────────────────────────────────────────────

const createStyles = (Colors: any) => StyleSheet.create({
  overlay: { flex: 1, backgroundColor: 'rgba(0,0,0,0.75)', justifyContent: 'flex-end' },
  sheet: {
    backgroundColor: Colors.background || '#0D0D0D',
    borderTopLeftRadius: 24, borderTopRightRadius: 24,
    maxHeight: '90%', minHeight: '60%', paddingTop: 20,
  },
  closeBtn: { position: 'absolute', top: 16, right: 16, zIndex: 10, padding: 8 },
  body: { alignItems: 'center', paddingHorizontal: 24, paddingBottom: 32, paddingTop: 8 },
  titleLarge: {
    color: Colors.text || '#FFF',
    fontSize: 26, fontWeight: '800', marginBottom: 10, textAlign: 'center',
  },
  subtitle: {
    color: Colors.textMuted || '#888',
    fontSize: 14, textAlign: 'center', lineHeight: 20, marginBottom: 24,
  },
  label: {
    color: Colors.textMuted || '#888',
    fontSize: 11, fontWeight: '700', letterSpacing: 1,
    alignSelf: 'flex-start', marginBottom: 6, marginTop: 12,
  },
  input: {
    backgroundColor: 'rgba(255,255,255,0.07)',
    borderRadius: 12, borderWidth: 1, borderColor: 'rgba(255,255,255,0.12)',
    color: Colors.text || '#FFF', fontSize: 16,
    paddingHorizontal: 16, paddingVertical: 12,
    width: '100%', marginBottom: 4,
  },
  codeInput: {
    fontSize: 28, fontWeight: '800', letterSpacing: 8,
    textAlign: 'center',
    fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace',
  },
  primaryBtn: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 8,
    backgroundColor: Colors.primary || '#FFAA00',
    borderRadius: 14, paddingVertical: 15, paddingHorizontal: 28,
    width: '100%', marginTop: 20,
  },
  primaryBtnText: { color: '#000', fontSize: 16, fontWeight: '800' },
  secondaryBtn: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 8,
    borderWidth: 1.5, borderColor: Colors.primary || '#FFAA00',
    borderRadius: 14, paddingVertical: 14, paddingHorizontal: 28,
    width: '100%', marginTop: 12,
  },
  secondaryBtnText: { color: Colors.primary || '#FFAA00', fontSize: 16, fontWeight: '700' },
  errorText: { color: '#FF4444', fontSize: 13, marginTop: 8, textAlign: 'center' },
  backBtn: { flexDirection: 'row', alignItems: 'center', alignSelf: 'flex-start', marginBottom: 12, gap: 4 },
  backText: { color: Colors.textMuted || '#888', fontSize: 14 },
  // Active session cards
  sessionCard: {
    flexDirection: 'row', alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.05)',
    borderRadius: 14, padding: 14, marginBottom: 8,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)',
  },
  sessionCardName: { color: Colors.text || '#FFF', fontSize: 15, fontWeight: '700' },
  sessionCardMeta: { color: Colors.textMuted || '#888', fontSize: 12, marginTop: 2 },
  sessionCardRight: { flexDirection: 'row', alignItems: 'center', gap: 6 },
  joinPill: {
    backgroundColor: Colors.primary || '#FFAA00',
    borderRadius: 20, paddingHorizontal: 14, paddingVertical: 6,
  },
  joinPillText: { color: '#000', fontSize: 13, fontWeight: '800' },
  // Divider
  divider: { flexDirection: 'row', alignItems: 'center', marginVertical: 16, width: '100%', gap: 8 },
  dividerLine: { flex: 1, height: 1, backgroundColor: 'rgba(255,255,255,0.08)' },
  dividerText: { color: Colors.textMuted || '#888', fontSize: 11, fontWeight: '700', letterSpacing: 1 },
  // Lobby
  lobbyHeader: {
    flexDirection: 'row', alignItems: 'center',
    paddingHorizontal: 20, paddingTop: 8, paddingBottom: 12,
    borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.06)',
  },
  lobbyCrewName: { color: Colors.text || '#FFF', fontSize: 22, fontWeight: '800' },
  lobbyRoleBadge: { color: Colors.textMuted || '#888', fontSize: 13, marginTop: 2 },
  inviteCodeCard: {
    margin: 16,
    backgroundColor: 'rgba(255,170,0,0.08)',
    borderWidth: 1.5, borderColor: 'rgba(255,170,0,0.4)',
    borderRadius: 16, padding: 16, alignItems: 'center',
  },
  inviteCodeLabel: { color: 'rgba(255,170,0,0.7)', fontSize: 11, fontWeight: '700', letterSpacing: 1 },
  inviteCode: {
    color: '#FFD700', fontSize: 42, fontWeight: '900', letterSpacing: 12,
    fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace', marginVertical: 8,
  },
  inviteCodeHint: { color: 'rgba(255,255,255,0.4)', fontSize: 12 },
  connectPrompt: {
    flexDirection: 'row', alignItems: 'flex-start', gap: 10,
    margin: 16, padding: 14,
    backgroundColor: 'rgba(255,255,255,0.04)',
    borderRadius: 12, borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)',
  },
  connectPromptText: { flex: 1, color: Colors.textMuted || '#888', fontSize: 13, lineHeight: 19 },
  // Leader handoff
  handoffToggle: {
    flexDirection: 'row', alignItems: 'center', gap: 8,
    borderWidth: 1.5, borderColor: '#FFD700',
    borderRadius: 10, paddingVertical: 10, paddingHorizontal: 14,
  },
  handoffToggleActive: { backgroundColor: '#FFD700' },
  handoffToggleText: { color: '#FFD700', fontSize: 14, fontWeight: '700' },
  handoffBtn: {
    flexDirection: 'row', alignItems: 'center', gap: 4,
    backgroundColor: '#FFD700', borderRadius: 20,
    paddingHorizontal: 10, paddingVertical: 5,
  },
  handoffBtnText: { color: '#000', fontSize: 12, fontWeight: '800' },
  // Member row
  memberRow: {
    flexDirection: 'row', alignItems: 'center',
    paddingVertical: 10,
    borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.04)',
  },
  memberAvatar: {
    width: 38, height: 38, borderRadius: 19,
    backgroundColor: 'rgba(255,255,255,0.1)',
    alignItems: 'center', justifyContent: 'center',
  },
  memberAvatarText: { color: '#FFF', fontSize: 16, fontWeight: '700' },
  memberName: { color: Colors.text || '#FFF', fontSize: 15, fontWeight: '600' },
  memberLeaderBadge: { color: '#FFD700', fontSize: 11, marginTop: 2 },
  // Footer
  lobbyFooter: {
    flexDirection: 'row', gap: 10,
    paddingHorizontal: 16, paddingBottom: 28, paddingTop: 8,
    borderTopWidth: 1, borderTopColor: 'rgba(255,255,255,0.06)',
  },
  doneBtn: {
    flex: 2, backgroundColor: Colors.primary || '#FFAA00',
    borderRadius: 14, paddingVertical: 14, alignItems: 'center',
  },
  doneBtnText: { color: '#000', fontSize: 15, fontWeight: '800' },
  leaveBtn: {
    flex: 1, borderWidth: 1.5, borderColor: '#FF4444',
    borderRadius: 14, paddingVertical: 14, alignItems: 'center',
  },
  leaveBtnText: { color: '#FF4444', fontSize: 14, fontWeight: '700' },
});
