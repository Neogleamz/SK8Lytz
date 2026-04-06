/**
 * CrewModal.tsx — SK8Lytz Crew Sync 2.0
 *
 * Full crew lifecycle modal with:
 *   Landing    → Create / Schedule / Join / Saved Crews
 *   Create     → Crew name, display name, geo-tag (optional), schedule (optional)
 *   Join       → Browse live sessions + code entry
 *   Controller → Leader OR Member session card (what/where/when/who)
 *                Leader: End Session, Hand Off, invite code copy
 *                Member: live mode/color sync status, Leave
 */

import React, { useState, useEffect, useCallback, useRef } from 'react';
import {
  View, Text, StyleSheet, TouchableOpacity, Modal, TextInput,
  ActivityIndicator, FlatList, Alert, Platform, ScrollView, Animated,
} from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../context/ThemeContext';
import { crewService, CrewSession, CrewMember, CrewRole } from '../services/CrewService';
import { supabase } from '../services/supabaseClient';
import { profileService } from '../services/ProfileService';
import { locationService } from '../services/LocationService';
import { AppLogger } from '../services/AppLogger';

// ─── Props ────────────────────────────────────────────────────────────────────

interface CrewModalProps {
  visible: boolean;
  onClose: () => void;
  onSessionReady: (session: CrewSession, role: CrewRole, lastScene: Record<string, any> | null) => void;
  onSessionLeft: () => void;
  onSessionEnded: () => void;
  activeSession: CrewSession | null;
  activeRole: CrewRole;
  /** Current mode/color being broadcast (leader's active state, for display in controller) */
  currentModeSummary?: string;
}

// ─── Helpers ──────────────────────────────────────────────────────────────────

function timeAgo(iso: string): string {
  const diff = Date.now() - new Date(iso).getTime();
  const m = Math.floor(diff / 60000);
  if (m < 1) return 'just now';
  if (m < 60) return `${m}m ago`;
  return `${Math.floor(m / 60)}h ago`;
}

function formatScheduled(iso: string): string {
  const d = new Date(iso);
  return d.toLocaleDateString('en-US', { weekday: 'short', month: 'short', day: 'numeric' })
    + ' · ' + d.toLocaleTimeString('en-US', { hour: 'numeric', minute: '2-digit' });
}

// ─── Component ────────────────────────────────────────────────────────────────

export default function CrewModal({
  visible, onClose, onSessionReady, onSessionLeft, onSessionEnded,
  activeSession, activeRole, currentModeSummary,
}: CrewModalProps) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);

  type ModalStep = 'landing' | 'create' | 'schedule' | 'join' | 'controller';
  const [step, setStep] = useState<ModalStep>(activeSession ? 'controller' : 'landing');
  const [isLoading, setIsLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');

  // Create / Schedule form
  const [crewName, setCrewName] = useState('');
  const [displayName, setDisplayName] = useState('');
  const [isGettingLocation, setIsGettingLocation] = useState(false);
  const [locationLabel, setLocationLabel] = useState('');
  const [locationCoords, setLocationCoords] = useState<{ lat: number; lng: number } | undefined>();
  const [scheduledDate, setScheduledDate] = useState<Date | null>(null);

  // Join form
  const [inviteCode, setInviteCode] = useState('');
  const [activeSessions, setActiveSessions] = useState<CrewSession[]>([]);
  const [isLoadingSessions, setIsLoadingSessions] = useState(false);

  // Controller
  const [currentSession, setCurrentSession] = useState<CrewSession | null>(activeSession);
  const [currentRole, setCurrentRole] = useState<CrewRole>(activeRole);
  const [members, setMembers] = useState<CrewMember[]>([]);
  const [currentUserId, setCurrentUserId] = useState('');
  const [isHandoffMode, setIsHandoffMode] = useState(false);

  // Pulse animation for LIVE indicator
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

  // ── Load profile display name ───────────────────────────────────────────────

  useEffect(() => {
    if (!visible) return;
    const loadUser = async () => {
      const { data: { user } } = await supabase.auth.getUser();
      if (!user) return;
      setCurrentUserId(user.id);
      try {
        const profile = await profileService.fetchOrCreateProfile();
        setDisplayName(profile?.display_name || user.email?.split('@')[0] || 'Skater');
      } catch {
        setDisplayName(user.email?.split('@')[0] || 'Skater');
      }
    };
    loadUser();
  }, [visible]);

  // ── Sync props → state ─────────────────────────────────────────────────────

  useEffect(() => {
    if (activeSession) {
      setCurrentSession(activeSession);
      setCurrentRole(activeRole);
      setStep('controller');
    } else {
      setStep('landing');
    }
  }, [activeSession, activeRole]);

  // ── Members list ───────────────────────────────────────────────────────────

  const loadMembers = useCallback(async () => {
    if (!currentSession) return;
    const m = await crewService.fetchMembers(currentSession.id).catch(() => []);
    setMembers(m);
  }, [currentSession]);

  useEffect(() => {
    if (step === 'controller') loadMembers();
  }, [step, loadMembers]);

  // ── Active sessions browser ────────────────────────────────────────────────

  const loadActiveSessions = useCallback(async () => {
    setIsLoadingSessions(true);
    const sessions = await crewService.fetchActiveSessions().catch(() => []);
    setActiveSessions(sessions);
    setIsLoadingSessions(false);
  }, []);

  useEffect(() => {
    if (step === 'join') loadActiveSessions();
  }, [step, loadActiveSessions]);

  // ── Location ───────────────────────────────────────────────────────────────

  const handleDetectLocation = async () => {
    setIsGettingLocation(true);
    const loc = await locationService.getSessionLocation();
    setIsGettingLocation(false);
    if (loc) {
      setLocationLabel(loc.label);
      setLocationCoords(loc.coords);
    } else {
      Alert.alert('Location Unavailable', 'Could not detect location. You can still create the session without it.');
    }
  };

  // ── Session joined helper ──────────────────────────────────────────────────

  const handleSessionJoined = async (session: CrewSession) => {
    const role: CrewRole = session.leader_user_id === currentUserId ? 'leader' : 'member';
    setCurrentSession(session);
    setCurrentRole(role);
    setStep('controller');
    loadMembers();

    const lastScene = role === 'member'
      ? await crewService.fetchLastScene(session.id).catch(() => null)
      : null;

    onSessionReady(session, role, lastScene);
  };

  // ── Create ─────────────────────────────────────────────────────────────────

  const handleCreate = async (scheduled?: Date) => {
    if (!crewName.trim()) { setErrorMsg('Enter a crew name'); return; }
    setIsLoading(true); setErrorMsg('');
    try {
      const opts: Parameters<typeof crewService.createSession>[2] = {};
      if (locationLabel) opts.locationLabel = locationLabel;
      if (locationCoords) opts.locationCoords = locationCoords;
      if (scheduled) opts.scheduledAt = scheduled.toISOString();

      const session = await crewService.createSession(crewName.trim(), displayName.trim(), opts);
      AppLogger.log('CREW_SESSION_CREATED', {
        sessionId: session.id,
        crewName: crewName.trim(),
        hasLocation: !!locationLabel,
        scheduled: !!scheduled,
      });
      await handleSessionJoined(session);
    } catch (e: any) {
      AppLogger.log('CREW_ERROR', { action: 'create', error: e.message });
      setErrorMsg(e.message || 'Failed to create crew');
    } finally { setIsLoading(false); }
  };

  // ── Join ───────────────────────────────────────────────────────────────────

  const handleJoinByCode = async () => {
    if (inviteCode.trim().length < 6) { setErrorMsg('Enter the 6-character crew code'); return; }
    setIsLoading(true); setErrorMsg('');
    try {
      const session = await crewService.joinSession(inviteCode.trim(), displayName.trim());
      AppLogger.log('CREW_SESSION_JOINED', { sessionId: session.id, crewName: session.name, method: 'code' });
      await handleSessionJoined(session);
    } catch (e: any) {
      AppLogger.log('CREW_ERROR', { action: 'join_code', error: e.message });
      setErrorMsg(e.message || 'Could not find that crew. Check the code.');
    } finally { setIsLoading(false); }
  };

  const handleJoinById = async (sessionId: string) => {
    setIsLoading(true); setErrorMsg('');
    try {
      const session = await crewService.joinSessionById(sessionId, displayName.trim());
      AppLogger.log('CREW_SESSION_JOINED', { sessionId: session.id, crewName: session.name, method: 'browse' });
      await handleSessionJoined(session);
    } catch (e: any) {
      AppLogger.log('CREW_ERROR', { action: 'join_id', error: e.message });
      setErrorMsg(e.message || 'Could not join that crew');
    } finally { setIsLoading(false); }
  };

  // ── Leave ──────────────────────────────────────────────────────────────────

  const handleLeave = () => {
    Alert.alert('Leave Crew', `Leave "${currentSession?.name}"?`, [
      { text: 'Cancel', style: 'cancel' },
      {
        text: 'Leave', style: 'destructive',
        onPress: async () => {
          AppLogger.log('CREW_SESSION_LEFT', { sessionId: currentSession?.id, role: currentRole });
          await crewService.leaveSession();
          setCurrentSession(null); setCurrentRole(null);
          setStep('landing'); setIsHandoffMode(false);
          onSessionLeft(); onClose();
        },
      },
    ]);
  };

  // ── End Session (leader) ────────────────────────────────────────────────────

  const handleEndSession = () => {
    Alert.alert(
      'End Session',
      `End "${currentSession?.name}"? All members will be notified and the session will close. Their skates will keep the current pattern.`,
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'End Session', style: 'destructive',
          onPress: async () => {
            try {
              await crewService.endSession();
              setCurrentSession(null); setCurrentRole(null);
              setStep('landing'); setIsHandoffMode(false);
              onSessionEnded(); onClose();
            } catch (e: any) {
              Alert.alert('Error', e.message);
            }
          },
        },
      ]
    );
  };

  // ── Handoff ────────────────────────────────────────────────────────────────

  const handleHandoffLeadership = async (member: CrewMember) => {
    Alert.alert(
      'Pass the Leader Hat 👑',
      `Make ${member.display_name} the new crew leader? They'll control the light show for everyone.`,
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Hand Off',
          onPress: async () => {
            try {
              await crewService.transferLeadership(member.user_id);
              AppLogger.log('CREW_LEADERSHIP_TRANSFERRED', { newLeaderName: member.display_name });
              setCurrentRole('member');
              setIsHandoffMode(false);
              setTimeout(loadMembers, 500);
            } catch (e: any) { Alert.alert('Error', e.message); }
          },
        },
      ]
    );
  };

  // ═══════════════════════════════════════════════════════════════════════════
  // RENDER — Landing
  // ═══════════════════════════════════════════════════════════════════════════

  const renderLanding = () => (
    <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
      <MaterialCommunityIcons name="account-group" size={48} color={Colors.primary} style={{ marginBottom: 10 }} />
      <Text style={styles.titleLarge}>Crew Sync</Text>
      <Text style={styles.subtitle}>
        Skate together. One leader controls the light show — every crew member's skates sync instantly.
      </Text>

      <TouchableOpacity style={styles.primaryBtn} onPress={() => { setStep('create'); setErrorMsg(''); }}>
        <MaterialCommunityIcons name="lightning-bolt" size={18} color="#000" />
        <Text style={styles.primaryBtnText}>Start a Session Now</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.secondaryBtn} onPress={() => { setStep('schedule'); setErrorMsg(''); }}>
        <MaterialCommunityIcons name="calendar-clock" size={18} color={Colors.primary} />
        <Text style={styles.secondaryBtnText}>Schedule a Session</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.secondaryBtn} onPress={() => { setStep('join'); setErrorMsg(''); }}>
        <MaterialCommunityIcons name="pound" size={18} color={Colors.primary} />
        <Text style={styles.secondaryBtnText}>Join a Crew</Text>
      </TouchableOpacity>
    </ScrollView>
  );

  // ═══════════════════════════════════════════════════════════════════════════
  // RENDER — Create (Start Now)
  // ═══════════════════════════════════════════════════════════════════════════

  const renderCreate = () => (
    <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
      <TouchableOpacity onPress={() => setStep('landing')} style={styles.backBtn}>
        <MaterialCommunityIcons name="chevron-left" size={22} color={Colors.textMuted} />
        <Text style={styles.backText}>Back</Text>
      </TouchableOpacity>

      <Text style={styles.titleLarge}>Start a Session</Text>

      <Text style={styles.label}>CREW NAME</Text>
      <TextInput style={styles.input} value={crewName} onChangeText={setCrewName}
        placeholder="e.g. Friday Night Crew" placeholderTextColor={Colors.textMuted}
        maxLength={32} autoFocus />

      <Text style={styles.label}>YOUR NAME IN CREW</Text>
      <TextInput style={styles.input} value={displayName} onChangeText={setDisplayName}
        placeholder="Display name" placeholderTextColor={Colors.textMuted} maxLength={24} />

      {/* Location (optional) */}
      <Text style={styles.label}>SESSION LOCATION (OPTIONAL)</Text>
      {locationLabel ? (
        <View style={styles.locationChip}>
          <MaterialCommunityIcons name="map-marker" size={16} color={Colors.primary} />
          <Text style={styles.locationChipText} numberOfLines={1}>{locationLabel}</Text>
          <TouchableOpacity onPress={() => { setLocationLabel(''); setLocationCoords(undefined); }}>
            <MaterialCommunityIcons name="close-circle" size={16} color={Colors.textMuted} />
          </TouchableOpacity>
        </View>
      ) : (
        <TouchableOpacity style={styles.locationBtn} onPress={handleDetectLocation} disabled={isGettingLocation}>
          {isGettingLocation
            ? <ActivityIndicator size="small" color={Colors.primary} />
            : <MaterialCommunityIcons name="crosshairs-gps" size={16} color={Colors.primary} />}
          <Text style={styles.locationBtnText}>
            {isGettingLocation ? 'Detecting location…' : 'Detect my location'}
          </Text>
        </TouchableOpacity>
      )}

      {errorMsg ? <Text style={styles.errorText}>{errorMsg}</Text> : null}

      <TouchableOpacity style={[styles.primaryBtn, isLoading && { opacity: 0.5 }]}
        onPress={() => handleCreate()} disabled={isLoading}>
        {isLoading ? <ActivityIndicator color="#000" /> : (
          <>
            <MaterialCommunityIcons name="lightning-bolt" size={18} color="#000" />
            <Text style={styles.primaryBtnText}>Create & Start</Text>
          </>
        )}
      </TouchableOpacity>
    </ScrollView>
  );

  // ═══════════════════════════════════════════════════════════════════════════
  // RENDER — Schedule
  // ═══════════════════════════════════════════════════════════════════════════

  const [schedHour, setSchedHour] = useState('9');
  const [schedMin, setSchedMin] = useState('00');
  const [schedAmPm, setSchedAmPm] = useState<'AM' | 'PM'>('PM');
  const [schedDayOffset, setSchedDayOffset] = useState(0); // 0=today, 1=tomorrow, etc.

  const buildScheduledDate = (): Date => {
    const d = new Date();
    d.setDate(d.getDate() + schedDayOffset);
    let h = parseInt(schedHour, 10) % 12;
    if (schedAmPm === 'PM') h += 12;
    d.setHours(h, parseInt(schedMin, 10), 0, 0);
    return d;
  };

  const getDayLabel = (offset: number) => {
    if (offset === 0) return 'Today';
    if (offset === 1) return 'Tomorrow';
    const d = new Date(); d.setDate(d.getDate() + offset);
    return d.toLocaleDateString('en-US', { weekday: 'short', month: 'short', day: 'numeric' });
  };

  const renderSchedule = () => (
    <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
      <TouchableOpacity onPress={() => setStep('landing')} style={styles.backBtn}>
        <MaterialCommunityIcons name="chevron-left" size={22} color={Colors.textMuted} />
        <Text style={styles.backText}>Back</Text>
      </TouchableOpacity>

      <Text style={styles.titleLarge}>Schedule a Session</Text>
      <Text style={[styles.subtitle, { marginBottom: 16 }]}>
        Your crew will get a push notification right away, and a reminder 15 minutes before.
      </Text>

      <Text style={styles.label}>CREW NAME</Text>
      <TextInput style={styles.input} value={crewName} onChangeText={setCrewName}
        placeholder="e.g. Weekend Warriors" placeholderTextColor={Colors.textMuted} maxLength={32} autoFocus />

      <Text style={styles.label}>YOUR NAME IN CREW</Text>
      <TextInput style={styles.input} value={displayName} onChangeText={setDisplayName}
        placeholder="Display name" placeholderTextColor={Colors.textMuted} maxLength={24} />

      {/* Day picker */}
      <Text style={styles.label}>DAY</Text>
      <ScrollView horizontal showsHorizontalScrollIndicator={false} style={{ width: '100%' }}>
        {[0, 1, 2, 3, 4, 5, 6].map(offset => (
          <TouchableOpacity
            key={offset}
            style={[styles.dayChip, schedDayOffset === offset && styles.dayChipActive]}
            onPress={() => setSchedDayOffset(offset)}
          >
            <Text style={[styles.dayChipText, schedDayOffset === offset && { color: '#000' }]}>
              {getDayLabel(offset)}
            </Text>
          </TouchableOpacity>
        ))}
      </ScrollView>

      {/* Time picker */}
      <Text style={[styles.label, { marginTop: 16 }]}>TIME</Text>
      <View style={styles.timeRow}>
        <TextInput
          style={styles.timeInput} value={schedHour}
          onChangeText={v => setSchedHour(v.replace(/[^0-9]/g, '').slice(0, 2))}
          keyboardType="number-pad" maxLength={2} placeholder="9"
          placeholderTextColor={Colors.textMuted}
        />
        <Text style={styles.timeColon}>:</Text>
        <TextInput
          style={styles.timeInput} value={schedMin}
          onChangeText={v => setSchedMin(v.replace(/[^0-9]/g, '').slice(0, 2))}
          keyboardType="number-pad" maxLength={2} placeholder="00"
          placeholderTextColor={Colors.textMuted}
        />
        <View style={styles.ampmRow}>
          {(['AM', 'PM'] as const).map(val => (
            <TouchableOpacity key={val}
              style={[styles.ampmBtn, schedAmPm === val && styles.ampmBtnActive]}
              onPress={() => setSchedAmPm(val)}>
              <Text style={[styles.ampmText, schedAmPm === val && { color: '#000' }]}>{val}</Text>
            </TouchableOpacity>
          ))}
        </View>
      </View>

      {/* Location optional */}
      <Text style={styles.label}>LOCATION (OPTIONAL)</Text>
      {locationLabel ? (
        <View style={styles.locationChip}>
          <MaterialCommunityIcons name="map-marker" size={16} color={Colors.primary} />
          <Text style={styles.locationChipText} numberOfLines={1}>{locationLabel}</Text>
          <TouchableOpacity onPress={() => { setLocationLabel(''); setLocationCoords(undefined); }}>
            <MaterialCommunityIcons name="close-circle" size={16} color={Colors.textMuted} />
          </TouchableOpacity>
        </View>
      ) : (
        <TouchableOpacity style={styles.locationBtn} onPress={handleDetectLocation} disabled={isGettingLocation}>
          {isGettingLocation
            ? <ActivityIndicator size="small" color={Colors.primary} />
            : <MaterialCommunityIcons name="crosshairs-gps" size={16} color={Colors.primary} />}
          <Text style={styles.locationBtnText}>
            {isGettingLocation ? 'Detecting…' : 'Detect my location'}
          </Text>
        </TouchableOpacity>
      )}

      {errorMsg ? <Text style={styles.errorText}>{errorMsg}</Text> : null}

      <TouchableOpacity style={[styles.primaryBtn, isLoading && { opacity: 0.5 }]}
        onPress={() => handleCreate(buildScheduledDate())} disabled={isLoading}>
        {isLoading ? <ActivityIndicator color="#000" /> : (
          <>
            <MaterialCommunityIcons name="calendar-check" size={18} color="#000" />
            <Text style={styles.primaryBtnText}>Schedule & Notify Crew</Text>
          </>
        )}
      </TouchableOpacity>
    </ScrollView>
  );

  // ═══════════════════════════════════════════════════════════════════════════
  // RENDER — Join
  // ═══════════════════════════════════════════════════════════════════════════

  const renderActiveSessionCard = ({ item }: { item: CrewSession }) => {
    const isOwn = item.leader_user_id === currentUserId;
    return (
      <TouchableOpacity style={styles.sessionCard} onPress={() => handleJoinById(item.id)}
        disabled={isLoading} activeOpacity={0.75}>
        <View style={{ flex: 1 }}>
          <Text style={styles.sessionCardName}>{item.name}</Text>
          <View style={{ flexDirection: 'row', alignItems: 'center', gap: 6, marginTop: 3 }}>
            {item.location_label && (
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: 3 }}>
                <MaterialCommunityIcons name="map-marker" size={12} color={Colors.textMuted} />
                <Text style={styles.sessionCardMeta} numberOfLines={1}>{item.location_label}</Text>
              </View>
            )}
            <Text style={styles.sessionCardMeta}>
              {item.member_count ?? 0} {(item.member_count ?? 0) === 1 ? 'skater' : 'skaters'}
            </Text>
            <Text style={styles.sessionCardMeta}>· {timeAgo(item.created_at)}</Text>
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

  const renderJoin = () => (
    <View style={{ flex: 1 }}>
      <View style={styles.body}>
        <TouchableOpacity onPress={() => setStep('landing')} style={styles.backBtn}>
          <MaterialCommunityIcons name="chevron-left" size={22} color={Colors.textMuted} />
          <Text style={styles.backText}>Back</Text>
        </TouchableOpacity>
        <Text style={styles.titleLarge}>Join a Crew</Text>

        <Text style={styles.label}>YOUR NAME IN CREW</Text>
        <TextInput style={styles.input} value={displayName} onChangeText={setDisplayName}
          placeholder="Display name" placeholderTextColor={Colors.textMuted} maxLength={24} />

        {/* Live sessions browser */}
        <View style={{ width: '100%', marginTop: 16 }}>
          <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: 8 }}>
            <Text style={styles.label}>🟢 CREWS SKATING NOW</Text>
            <TouchableOpacity onPress={loadActiveSessions}>
              <MaterialCommunityIcons name="refresh" size={15} color={Colors.textMuted} />
            </TouchableOpacity>
          </View>
          {isLoadingSessions ? (
            <ActivityIndicator color={Colors.primary} style={{ marginVertical: 14 }} />
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

  // ═══════════════════════════════════════════════════════════════════════════
  // RENDER — Session Controller Card
  // ═══════════════════════════════════════════════════════════════════════════

  const renderMemberRow = ({ item }: { item: CrewMember }) => {
    const isLeader = item.user_id === currentSession?.leader_user_id;
    const isMe     = item.user_id === currentUserId;
    const canHandoff = currentRole === 'leader' && !isLeader && isHandoffMode;
    return (
      <View style={styles.memberRow}>
        <View style={[styles.memberAvatar, isLeader && { borderColor: '#FFD700', borderWidth: 2 }]}>
          <Text style={styles.memberAvatarText}>{(item.display_name?.[0] ?? '?').toUpperCase()}</Text>
        </View>
        <View style={{ flex: 1, marginLeft: 10 }}>
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

  const renderController = () => {
    if (!currentSession) return null;
    const isLeader = currentRole === 'leader';
    const hasLocation = !!currentSession.location_label;
    const startedAt = timeAgo(currentSession.created_at);

    return (
      <View style={{ flex: 1 }}>
        {/* ─── Session Card Header ─── */}
        <View style={styles.controllerCard}>
          {/* Crew name + live pill */}
          <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: 6 }}>
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
              const { Clipboard } = require('react-native');
              Clipboard.setString(currentSession.invite_code);
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

        {/* ─── Member List ─── */}
        <ScrollView style={{ flex: 1 }} showsVerticalScrollIndicator={false}>
          {/* Leader handoff toggle */}
          {isLeader && (
            <View style={{ marginHorizontal: 16, marginTop: 12 }}>
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
          )}

          <Text style={[styles.label, { marginHorizontal: 20, marginTop: 14 }]}>
            CREW MEMBERS ({members.length})
          </Text>
          {members.length === 0
            ? <Text style={[styles.subtitle, { margin: 16, marginTop: 4, fontSize: 13 }]}>Waiting for skaters…</Text>
            : members.map(m => (
              <View key={m.id} style={{ paddingHorizontal: 16 }}>
                {renderMemberRow({ item: m })}
              </View>
            ))
          }
          <View style={{ height: 110 }} />
        </ScrollView>

        {/* ─── Footer Actions ─── */}
        <View style={styles.controllerFooter}>
          <TouchableOpacity style={styles.doneBtn} onPress={onClose}>
            <Text style={styles.doneBtnText}>{isLeader ? '✓ Back to Skating' : '✓ Got It'}</Text>
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
  };

  // ═══════════════════════════════════════════════════════════════════════════
  // MAIN RENDER
  // ═══════════════════════════════════════════════════════════════════════════

  return (
    <Modal visible={visible} animationType="slide" transparent statusBarTranslucent>
      <View style={styles.overlay}>
        <View style={styles.sheet}>
          <TouchableOpacity style={styles.closeBtn} onPress={onClose}>
            <MaterialCommunityIcons name="close" size={20} color={Colors.textMuted} />
          </TouchableOpacity>

          {step === 'landing'    && renderLanding()}
          {step === 'create'     && renderCreate()}
          {step === 'schedule'   && renderSchedule()}
          {step === 'join'       && renderJoin()}
          {step === 'controller' && renderController()}
        </View>
      </View>
    </Modal>
  );
}

// ─── Styles ───────────────────────────────────────────────────────────────────

const createStyles = (Colors: any) => StyleSheet.create({
  overlay:  { flex: 1, backgroundColor: 'rgba(0,0,0,0.8)', justifyContent: 'flex-end' },
  sheet: {
    backgroundColor: Colors.background || '#0D0D0D',
    borderTopLeftRadius: 26, borderTopRightRadius: 26,
    maxHeight: '92%', minHeight: '55%', paddingTop: 22,
  },
  closeBtn: { position: 'absolute', top: 16, right: 16, zIndex: 10, padding: 8 },
  body:     { alignItems: 'center', paddingHorizontal: 24, paddingBottom: 32, paddingTop: 8 },

  // Typography
  titleLarge: { color: Colors.text || '#FFF', fontSize: 24, fontWeight: '800', marginBottom: 8, textAlign: 'center' },
  subtitle:   { color: Colors.textMuted || '#888', fontSize: 13, textAlign: 'center', lineHeight: 19, marginBottom: 20 },
  label:      { color: Colors.textMuted || '#888', fontSize: 10, fontWeight: '700', letterSpacing: 1.2, alignSelf: 'flex-start', marginBottom: 5, marginTop: 12 },

  // Buttons
  primaryBtn: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 8,
    backgroundColor: Colors.primary || '#FFAA00',
    borderRadius: 14, paddingVertical: 14, paddingHorizontal: 24, width: '100%', marginTop: 16,
  },
  primaryBtnText:   { color: '#000', fontSize: 15, fontWeight: '800' },
  secondaryBtn: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 8,
    borderWidth: 1.5, borderColor: Colors.primary || '#FFAA00',
    borderRadius: 14, paddingVertical: 13, paddingHorizontal: 24, width: '100%', marginTop: 10,
  },
  secondaryBtnText: { color: Colors.primary || '#FFAA00', fontSize: 15, fontWeight: '700' },
  backBtn:    { flexDirection: 'row', alignItems: 'center', alignSelf: 'flex-start', marginBottom: 10, gap: 2 },
  backText:   { color: Colors.textMuted || '#888', fontSize: 14 },

  // Input
  input: {
    backgroundColor: 'rgba(255,255,255,0.07)', borderRadius: 12,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.12)',
    color: Colors.text || '#FFF', fontSize: 15,
    paddingHorizontal: 14, paddingVertical: 11, width: '100%', marginBottom: 2,
  },
  codeInput: { fontSize: 26, fontWeight: '900', letterSpacing: 8, textAlign: 'center',
    fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace' },
  errorText:  { color: '#FF4444', fontSize: 12, marginTop: 6, textAlign: 'center' },

  // Location
  locationBtn: {
    flexDirection: 'row', alignItems: 'center', gap: 8, width: '100%',
    borderWidth: 1, borderColor: 'rgba(255,170,0,0.3)', borderRadius: 10, borderStyle: 'dashed',
    paddingVertical: 10, paddingHorizontal: 14, marginBottom: 2,
  },
  locationBtnText: { color: Colors.primary || '#FFAA00', fontSize: 13, fontWeight: '600' },
  locationChip: {
    flexDirection: 'row', alignItems: 'center', gap: 6, width: '100%',
    backgroundColor: 'rgba(255,170,0,0.1)', borderRadius: 10,
    padding: 10, borderWidth: 1, borderColor: 'rgba(255,170,0,0.25)',
  },
  locationChipText: { flex: 1, color: Colors.primary || '#FFAA00', fontSize: 13 },

  // Schedule
  dayChip: {
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.12)',
    borderRadius: 20, paddingHorizontal: 14, paddingVertical: 7, marginRight: 8,
  },
  dayChipActive: { backgroundColor: Colors.primary || '#FFAA00', borderColor: 'transparent' },
  dayChipText:  { color: Colors.text || '#FFF', fontSize: 13, fontWeight: '600' },
  timeRow:      { flexDirection: 'row', alignItems: 'center', width: '100%', gap: 8 },
  timeInput:    {
    backgroundColor: 'rgba(255,255,255,0.07)', borderRadius: 10,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.12)',
    color: Colors.text || '#FFF', fontSize: 28, fontWeight: '800', textAlign: 'center',
    width: 62, paddingVertical: 8,
  },
  timeColon:    { color: Colors.text || '#FFF', fontSize: 28, fontWeight: '800' },
  ampmRow:      { flexDirection: 'row', gap: 6, marginLeft: 4 },
  ampmBtn:      {
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.12)',
    borderRadius: 8, paddingHorizontal: 12, paddingVertical: 8,
  },
  ampmBtnActive: { backgroundColor: Colors.primary || '#FFAA00', borderColor: 'transparent' },
  ampmText:     { color: Colors.text || '#FFF', fontSize: 14, fontWeight: '700' },

  // Join — sessions browser
  sessionCard: {
    flexDirection: 'row', alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.05)',
    borderRadius: 14, padding: 12, marginBottom: 8,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)',
  },
  sessionCardName: { color: Colors.text || '#FFF', fontSize: 14, fontWeight: '700' },
  sessionCardMeta: { color: Colors.textMuted || '#888', fontSize: 11 },
  sessionCardRight: { flexDirection: 'row', alignItems: 'center', gap: 6 },
  joinPill: { backgroundColor: Colors.primary || '#FFAA00', borderRadius: 20, paddingHorizontal: 12, paddingVertical: 5 },
  joinPillText: { color: '#000', fontSize: 12, fontWeight: '800' },
  divider:      { flexDirection: 'row', alignItems: 'center', marginVertical: 14, width: '100%', gap: 8 },
  dividerLine:  { flex: 1, height: 1, backgroundColor: 'rgba(255,255,255,0.08)' },
  dividerText:  { color: Colors.textMuted || '#888', fontSize: 10, fontWeight: '700', letterSpacing: 1 },

  // Controller card
  controllerCard: {
    margin: 16,
    backgroundColor: 'rgba(255,255,255,0.04)',
    borderRadius: 18, padding: 16,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)',
  },
  controllerCrewName: { color: Colors.text || '#FFF', fontSize: 20, fontWeight: '900', flex: 1 },
  controllerRole:     { color: Colors.textMuted || '#888', fontSize: 12, marginBottom: 10 },
  livePill: {
    flexDirection: 'row', alignItems: 'center', gap: 5,
    backgroundColor: 'rgba(0,230,118,0.12)',
    borderRadius: 20, paddingHorizontal: 10, paddingVertical: 4,
  },
  liveDot: { width: 7, height: 7, borderRadius: 4, backgroundColor: '#00E676' },
  liveText: { color: '#00E676', fontSize: 11, fontWeight: '800', letterSpacing: 1 },
  metaRow: { flexDirection: 'row', flexWrap: 'wrap', gap: 6, marginBottom: 10 },
  metaChip: {
    flexDirection: 'row', alignItems: 'center', gap: 4,
    backgroundColor: 'rgba(255,255,255,0.06)', borderRadius: 20,
    paddingHorizontal: 10, paddingVertical: 4,
  },
  metaChipText: { color: Colors.textMuted || '#888', fontSize: 11 },
  modeRow: {
    flexDirection: 'row', alignItems: 'center', gap: 6,
    backgroundColor: 'rgba(255,170,0,0.08)', borderRadius: 10,
    paddingHorizontal: 10, paddingVertical: 8, marginBottom: 6,
  },
  modeText: { color: Colors.primary || '#FFAA00', fontSize: 12, fontWeight: '700' },
  inviteCodeRow: {
    flexDirection: 'row', alignItems: 'center', gap: 8, marginTop: 8,
    backgroundColor: 'rgba(255,170,0,0.08)',
    borderRadius: 10, padding: 10,
    borderWidth: 1, borderColor: 'rgba(255,170,0,0.2)',
  },
  inviteCodeLabel: { color: 'rgba(255,170,0,0.6)', fontSize: 10, fontWeight: '700', letterSpacing: 1 },
  inviteCode:      { color: '#FFD700', fontSize: 22, fontWeight: '900', fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace', letterSpacing: 4, flex: 1 },
  syncRow: {
    flexDirection: 'row', alignItems: 'center', gap: 8, marginTop: 8,
    backgroundColor: 'rgba(0,170,255,0.08)', borderRadius: 10, padding: 10,
  },
  syncDot:  { width: 8, height: 8, borderRadius: 4, backgroundColor: '#00AAFF' },
  syncText: { color: '#00AAFF', fontSize: 11, fontWeight: '600', flex: 1 },

  // Member rows
  memberRow: {
    flexDirection: 'row', alignItems: 'center', paddingVertical: 9,
    borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.04)',
  },
  memberAvatar: {
    width: 36, height: 36, borderRadius: 18, backgroundColor: 'rgba(255,255,255,0.1)',
    alignItems: 'center', justifyContent: 'center',
  },
  memberAvatarText:   { color: '#FFF', fontSize: 15, fontWeight: '700' },
  memberName:         { color: Colors.text || '#FFF', fontSize: 14, fontWeight: '600' },
  memberLeaderBadge:  { color: '#FFD700', fontSize: 10, marginTop: 1 },
  handoffToggle: {
    flexDirection: 'row', alignItems: 'center', gap: 7,
    borderWidth: 1.5, borderColor: '#FFD700', borderRadius: 10,
    paddingVertical: 9, paddingHorizontal: 14,
  },
  handoffToggleActive: { backgroundColor: '#FFD700' },
  handoffToggleText:   { color: '#FFD700', fontSize: 13, fontWeight: '700' },
  handoffBtn: {
    flexDirection: 'row', alignItems: 'center', gap: 4,
    backgroundColor: '#FFD700', borderRadius: 20, paddingHorizontal: 10, paddingVertical: 5,
  },
  handoffBtnText: { color: '#000', fontSize: 11, fontWeight: '800' },

  // Footer
  controllerFooter: {
    flexDirection: 'row', gap: 10, paddingHorizontal: 16, paddingBottom: 28, paddingTop: 8,
    borderTopWidth: 1, borderTopColor: 'rgba(255,255,255,0.06)',
  },
  doneBtn:    { flex: 2, backgroundColor: Colors.primary || '#FFAA00', borderRadius: 14, paddingVertical: 14, alignItems: 'center' },
  doneBtnText:{ color: '#000', fontSize: 14, fontWeight: '800' },
  endBtn:     { flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 4, borderWidth: 1.5, borderColor: '#FF4444', borderRadius: 14, paddingVertical: 14 },
  endBtnText: { color: '#FF4444', fontSize: 13, fontWeight: '700' },
});
