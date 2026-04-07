/**
 * CrewModal.tsx — SK8Lytz Crew Hub
 *
 * Unified single-page crew experience:
 *   Hub        → My Crews (inline live session + one-tap join), Live Near You (public sessions), Schedule
 *   Create     → Crew picker (existing crews) or new name, public/private, location
 *   Schedule   → Crew picker, DateTimePicker calendar, public/private, location
 *   Controller → Leader OR Member session card (what/where/when/who)
 *                Leader: End Session, Hand Off, invite code copy
 *                Member: live mode/color sync status, Leave
 *   Manage     → Create/edit/delete permanent crews (accessible from Hub)
 *   Crew Detail → Members, invite code, stats
 */

import React, { useState, useEffect, useCallback, useRef } from 'react';
import {
  View, Text, StyleSheet, TouchableOpacity, Modal, TextInput,
  ActivityIndicator, FlatList, Alert, Platform, ScrollView, Animated, Image,
  KeyboardAvoidingView, Share,
} from 'react-native';
import * as Clipboard from 'expo-clipboard';
import DateTimePicker from '@react-native-community/datetimepicker';
import * as ImagePicker from 'expo-image-picker';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../context/ThemeContext';
import CustomSlider from './CustomSlider';
import { crewService, CrewSession, CrewMember, CrewRole } from '../services/CrewService';
import { supabase } from '../services/supabaseClient';
import { profileService, PermanentCrew, CrewMemberFull } from '../services/ProfileService';
import { locationService, NearbySession } from '../services/LocationService';
import { AppLogger } from '../services/AppLogger';
import { notificationService } from '../services/NotificationService';

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

function _formatScheduled(iso: string): string {
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

  type ModalStep = 'landing' | 'create' | 'schedule' | 'join' | 'controller' | 'manage' | 'crew-detail';
  const [step, setStep] = useState<ModalStep>(activeSession ? 'controller' : 'landing');
  const [showCodeEntry, setShowCodeEntry] = useState(false);
  // null = show all; number = filter to within that many miles
  const [discoverRadiusMi, setDiscoverRadiusMi] = useState<number | null>(50);
  // Maps crewId → its currently live session (populated when hub loads)
  const [_crewActiveSessions, _setCrewActiveSessions] = useState<Record<string, CrewSession | null>>({});
  const [isLoading, setIsLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');

  // ── Create / Schedule form state ───────────────────────────────────────────
  const [crewName, setCrewName] = useState('');
  const [displayName, setDisplayName] = useState('');
  const [isGettingLocation, setIsGettingLocation] = useState(false);
  const [locationLabel, setLocationLabel] = useState('');
  const [locationCoords, setLocationCoords] = useState<{ lat: number; lng: number } | undefined>();
  const [_scheduledDate, _setScheduledDate] = useState<Date | null>(null);

  // Crew picker + session visibility
  const [permanentCrews, setPermanentCrews] = useState<{ id: string; name: string }[]>([]);
  const [selectedCrewId, setSelectedCrewId] = useState<string | null>(null); // null = new session name
  const [isPublic, setIsPublic] = useState(false);

  // Native date/time picker for schedule form
  const [schedDateTime, setSchedDateTime] = useState<Date>(() => {
    const d = new Date();
    d.setDate(d.getDate() + 1);
    d.setHours(19, 0, 0, 0);
    return d;
  });
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [showTimePicker, setShowTimePicker] = useState(false);

  // Join form
  const [inviteCode, setInviteCode] = useState('');
  const [activeSessions, setActiveSessions] = useState<CrewSession[]>([]);
  const [isLoadingSessions, setIsLoadingSessions] = useState(false);

  // ── Manage Crews Hub state ────────────────────────────────────────────────
  const [_manageTab, _setManageTab] = useState<'mycrews' | 'discover' | 'create'>('mycrews');
  const [myCrews, setMyCrews] = useState<PermanentCrew[]>([]);
  const [publicCrews, setPublicCrews] = useState<PermanentCrew[]>([]);
  const [selectedCrewDetail, setSelectedCrewDetail] = useState<PermanentCrew | null>(null);
  const [isLoadingCrews, setIsLoadingCrews] = useState(false);
  const [crewMemberCounts, setCrewMemberCounts] = useState<Record<string, { count: number; avatarColors: string[] }>>({});
  const [nearbySessions, setNearbySessions] = useState<NearbySession[]>([]);
  const [isLoadingNearby, setIsLoadingNearby] = useState(false);
  const [joiningSessionId, setJoiningSessionId] = useState<string | null>(null);
  // Create crew form
  const [newCrewName, setNewCrewName] = useState('');
  const [newCrewIsPublic, setNewCrewIsPublic] = useState(false);
  const [newCrewColor, setNewCrewColor] = useState('#FFAA00');
  const [newCrewIcon, setNewCrewIcon] = useState('roller-skate');
  const [newCrewCity, setNewCrewCity] = useState('');
  const [newCrewState, setNewCrewState] = useState('');
  const [newCrewDescription, setNewCrewDescription] = useState('');
  const [newCrewPhotoUri, setNewCrewPhotoUri] = useState<string | null>(null);
  const [isCreatingCrew, setIsCreatingCrew] = useState(false);
  const [createCrewError, setCreateCrewError] = useState('');
  const [confirmingDeleteCrewId, setConfirmingDeleteCrewId] = useState<string | null>(null);
  const [confirmingLeaveCrewId, setConfirmingLeaveCrewId] = useState<string | null>(null);
  const [_discoverRadius, setDiscoverRadius] = useState(50);
  const [_discoverSearch, setDiscoverSearch] = useState('');
  const [joiningCrewId, setJoiningCrewId] = useState<string | null>(null);
  const [editingCrewId, setEditingCrewId] = useState<string | null>(null);
  const [editCrewName, setEditCrewName] = useState('');
  const [editCrewIsPublic, setEditCrewIsPublic] = useState(false);
  const [editCrewCity, setEditCrewCity] = useState('');
  const [editCrewState, setEditCrewState] = useState('');
  const [editCrewDesc, setEditCrewDesc] = useState('');
  const [isSavingCrew, setIsSavingCrew] = useState(false);
  const [crewStats, setCrewStats] = useState<Record<string, { sessionCount: number; lastActive: string | null; topScene: string | null }>>({});
  const [expandedCrewId, setExpandedCrewId] = useState<string | null>(null);
  const [newCrewCode, setNewCrewCode] = useState(() => Math.random().toString(36).substring(2, 8).toUpperCase());
  const [userSearchQuery, setUserSearchQuery] = useState('');
  const [userSearchResults, setUserSearchResults] = useState<{ user_id: string, username: string | null, display_name: string | null }[]>([]);
  const [selectedMembers, setSelectedMembers] = useState<{ user_id: string, username: string | null, display_name: string | null }[]>([]);
  const [newCrewHue, setNewCrewHue] = useState(40);
  const [cardMembers, setCardMembers] = useState<Record<string, CrewMemberFull[]>>({});
  const [loadingCardMembersFor, setLoadingCardMembersFor] = useState<string | null>(null);
  const [makingOwnerFor, setMakingOwnerFor] = useState<string | null>(null);
  const [isRemovingUserFor, setIsRemovingUserFor] = useState<string | null>(null);
  const [isAddingMembersTo, setIsAddingMembersTo] = useState<string | null>(null);
  const [showPublicCrewsOnHub, setShowPublicCrewsOnHub] = useState(false);

  // Helper to load full member details into cardMembers
  const loadCrewMembers = (crewId: string) => {
    if (cardMembers[crewId]) return;
    setLoadingCardMembersFor(crewId);
    profileService.getCrewMembersWithNames(crewId)
      .then(members => setCardMembers(prev => ({ ...prev, [crewId]: members })))
      .catch(() => { })
      .finally(() => setLoadingCardMembersFor(null));
  };

  // Fetch crew stats when detail view opens
  useEffect(() => {
    if (!selectedCrewDetail) return;
    const id = selectedCrewDetail.id;
    if (crewStats[id]) return; // already loaded
    profileService.getCrewStats(id).then(stats =>
      setCrewStats(prev => ({ ...prev, [id]: stats }))
    );
  }, [selectedCrewDetail]);

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

  // Load permanent crews when hub, create, schedule, or manage opens
  useEffect(() => {
    if (!visible) return;
    if (step !== 'landing' && step !== 'create' && step !== 'schedule' && step !== 'manage') return;
    profileService.getMyCrew().then((crews: PermanentCrew[]) => {
      setMyCrews(crews);
      setPermanentCrews(crews.map(c => ({ id: c.id, name: c.name })));
    }).catch((e) => { console.warn('[CrewModal] Failed to load my crews:', e); });
  }, [visible, step]);

  // Load public crews when hub opens
  useEffect(() => {
    if (!visible) return;
    const shouldLoad = step === 'landing';
    if (!shouldLoad) return;
    setIsLoadingCrews(true);
    profileService.getPublicCrews().then(crews => {
      setPublicCrews(crews);
    }).catch((e) => { console.warn('[CrewModal] Failed to load public crews:', e); }).finally(() => setIsLoadingCrews(false));
  }, [visible, step]);

  // Load nearby live sessions when hub opens
  useEffect(() => {
    if (!visible) return;
    const shouldLoad = step === 'landing';
    if (!shouldLoad) return;
    setIsLoadingNearby(true);
    locationService.getNearbyPublicSessions(discoverRadiusMi)
      .then(sessions => setNearbySessions(sessions))
      .catch((e) => { console.warn('[CrewModal] Failed to load nearby sessions:', e); })
      .finally(() => setIsLoadingNearby(false));
  }, [visible, step, discoverRadiusMi]);

  // Search users for invite
  useEffect(() => {
    if (!userSearchQuery.trim()) {
      setUserSearchResults([]);
      return;
    }
    const timer = setTimeout(() => {
      profileService.searchUsers(userSearchQuery).then(setUserSearchResults);
    }, 300);
    return () => clearTimeout(timer);
  }, [userSearchQuery]);

  // Load active sessions for hub → map them to their crew
  useEffect(() => {
    if (!visible || step !== 'landing') return;
    crewService.fetchActiveSessions().then(sessions => {
      // Build a map from session name → session so crew cards can show inline status
      const map: Record<string, CrewSession> = {};
      sessions.forEach(s => { map[s.id] = s; });
      setActiveSessions(sessions);
    }).catch((e) => { console.warn('[CrewModal] Failed to load active sessions:', e); });
  }, [visible, step]);

  // Fetch member counts for My Crews on hub landing
  useEffect(() => {
    if (!visible || step !== 'landing' || myCrews.length === 0) return;
    myCrews.forEach(crew => {
      if (crewMemberCounts[crew.id]) return;
      profileService.getCrewMembersForDisplay(crew.id).then(info => {
        setCrewMemberCounts(prev => ({ ...prev, [crew.id]: info }));
      }).catch((e) => { console.warn('[CrewModal] Failed to load member counts:', e); });
    });
  }, [visible, step, myCrews]);

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
    const sessionName = crewName.trim() || permanentCrews.find(c => c.id === selectedCrewId)?.name || '';
    if (!sessionName) { setErrorMsg('Pick a crew or enter a session name'); return; }
    setIsLoading(true); setErrorMsg('');
    try {
      const crewInfo = myCrews.find(c => c.id === selectedCrewId);
      const isSessionPublic = crewInfo ? crewInfo.is_public : false;

      const opts: Parameters<typeof crewService.createSession>[2] = { isPublic: isSessionPublic };
      if (locationLabel) opts.locationLabel = locationLabel;
      if (locationCoords) opts.locationCoords = locationCoords;
      if (scheduled) opts.scheduledAt = scheduled.toISOString();

      const session = await crewService.createSession(sessionName, displayName.trim(), opts);
      AppLogger.log('CREW_SESSION_CREATED', {
        sessionId: session.id, crewName: sessionName,
        hasLocation: !!locationLabel, scheduled: !!scheduled, isPublic,
      });

      // Schedule a local push notification 15 min before if this is a future session
      if (scheduled && scheduled > new Date()) {
        const crewLabel = permanentCrews.find(c => c.id === selectedCrewId)?.name ?? sessionName;
        notificationService.sendSessionStartingSoon({
          sessionId: session.id,
          sessionName,
          crewName: crewLabel,
          scheduledAt: scheduled,
        }).catch(() => { }); // fire-and-forget, non-critical
      }

      await handleSessionJoined(session);
    } catch (e: any) {
      AppLogger.log('CREW_ERROR', { action: 'create', error: e.message });
      setErrorMsg(e.message || 'Failed to create session');
    } finally { setIsLoading(false); }
  };

  // ── Join ───────────────────────────────────────────────────────────────────

  const handleJoinByCode = async () => {
    if (inviteCode.trim().length < 6) { setErrorMsg('Enter the 6-character crew invite code'); return; }
    setIsLoading(true); setErrorMsg('');
    try {
      const crew = await profileService.joinPermanentCrew(inviteCode.trim());
      AppLogger.log('CREW_SESSION_JOINED', { crewId: crew.id, crewName: crew.name, method: 'permanent_code' });
      // Refresh my crews list so the new crew appears immediately
      const updatedCrews = await profileService.getMyCrew();
      setMyCrews(updatedCrews);
      setPermanentCrews(updatedCrews.map(c => ({ id: c.id, name: c.name })));
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
              await crewService.endSession(currentSession?.id);
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
  // RENDER — Crew Hub (unified single-page home)
  // ═══════════════════════════════════════════════════════════════════════════

  const renderLanding = () => {
    // Find the live session that matches a given crew (by name match or crew membership)
    const getLiveSessionForCrew = (crew: PermanentCrew): CrewSession | null => {
      return activeSessions.find(s =>
        s.name === crew.name ||
        s.name.startsWith(crew.name + ' ')
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
                  <View style={[styles.hubCrewAvatar, { backgroundColor: crew.avatar_color || '#FFAA00' }]}>
                    <MaterialCommunityIcons
                      name={(crew.avatar_icon as any) || 'account-group'}
                      size={20}
                      color="#000"
                    />
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
                    <Text style={styles.hubCrewMeta}>
                      {memberInfo ? `${memberInfo.count} member${memberInfo.count !== 1 ? 's' : ''}` : 'Loading...'}
                      {crew.city ? ` · ${crew.city}` : ''}
                    </Text>
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
                          setSelectedCrewId(crew.id);
                          setCrewName(crew.name);
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
                          setSelectedCrewId(crew.id);
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
          onPress={() => setShowCodeEntry(v => !v)}
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
          <TouchableOpacity onPress={() => {
            setIsLoadingNearby(true);
            locationService.getNearbyPublicSessions(discoverRadiusMi)
              .then(s => setNearbySessions(s))
              .catch(() => { })
              .finally(() => setIsLoadingNearby(false));
          }}>
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
                ? `No public sessions within ${discoverRadiusMi} mi`
                : 'No public sessions near you right now'}
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

        {/* ── PUBLIC CREWS ── */}
        <View style={[styles.hubSectionRow, { marginTop: 16 }]}>
          <Text style={styles.hubSectionLabel}>🌍 PUBLIC CREWS</Text>
          <TouchableOpacity
            onPress={() => setShowPublicCrewsOnHub(v => !v)}
            style={[styles.hubActionChip, { borderColor: 'rgba(255,255,255,0.15)' }]}
          >
            <MaterialCommunityIcons
              name={showPublicCrewsOnHub ? 'eye-off-outline' : 'eye-outline'}
              size={12} color={Colors.textMuted}
            />
            <Text style={[styles.hubActionChipText, { color: Colors.textMuted }]}>
              {showPublicCrewsOnHub ? 'Hide' : 'Browse'}
            </Text>
          </TouchableOpacity>
        </View>

        {showPublicCrewsOnHub && (
          isLoadingCrews ? (
            <ActivityIndicator color={Colors.primary} style={{ marginVertical: 12 }} />
          ) : publicCrews.length === 0 ? (
            <View style={styles.hubEmptyCard}>
              <Text style={styles.hubEmptyText}>No public crews found yet — create one!</Text>
            </View>
          ) : (
            publicCrews.map(crew => {
              const alreadyMember = myCrews.some(c => c.id === crew.id);
              const isJoining = joiningCrewId === crew.id;
              return (
                <View key={crew.id} style={[styles.hubCrewCard, { flexDirection: 'row', alignItems: 'center', paddingVertical: 10 }]}>
                  <View style={[styles.hubCrewAvatar, { backgroundColor: crew.avatar_color || '#FFAA00' }]}>
                    <MaterialCommunityIcons name={(crew.avatar_icon as any) || 'account-group'} size={18} color="#000" />
                  </View>
                  <View style={{ flex: 1, marginLeft: 10 }}>
                    <Text style={styles.hubCrewName} numberOfLines={1}>{crew.name}</Text>
                    <Text style={styles.hubCrewMeta}>
                      {crew.city ? `📍 ${crew.city}` : '🌍 Public crew'}
                    </Text>
                  </View>
                  {alreadyMember ? (
                    <View style={[styles.hubJoinPill, { backgroundColor: 'rgba(0,200,100,0.15)' }]}>
                      <Text style={[styles.hubJoinPillText, { color: '#00C864' }]}>✓ Member</Text>
                    </View>
                  ) : (
                    <TouchableOpacity
                      style={styles.hubJoinPill}
                      onPress={() => handleJoinPublicCrew(crew)}
                      disabled={isJoining}
                    >
                      {isJoining
                        ? <ActivityIndicator size="small" color="#000" />
                        : <Text style={styles.hubJoinPillText}>JOIN</Text>}
                    </TouchableOpacity>
                  )}
                </View>
              );
            })
          )
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
  };

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

      {/* ── Crew picker ── */}
      <Text style={styles.label}>SESSION FOR</Text>
      <View style={styles.crewPickerRow}>
        {permanentCrews.map(crew => (
          <TouchableOpacity key={crew.id}
            style={[styles.crewChip, selectedCrewId === crew.id && styles.crewChipActive]}
            onPress={() => { setSelectedCrewId(crew.id); setCrewName(''); }}>
            <MaterialCommunityIcons name="account-group" size={13}
              color={selectedCrewId === crew.id ? '#000' : Colors.primary} />
            <Text style={[styles.crewChipText, selectedCrewId === crew.id && styles.crewChipTextActive]}
              numberOfLines={1}>{crew.name}</Text>
          </TouchableOpacity>
        ))}
        {/* 'New Crew' — navigates to Manage Crews hub to create a permanent crew */}
        <TouchableOpacity
          style={[styles.crewChip, { borderStyle: 'dashed' }]}
          onPress={() => setStep('manage')}>
          <MaterialCommunityIcons name="plus" size={13} color={Colors.primary} />
          <Text style={styles.crewChipText}>New Crew</Text>
        </TouchableOpacity>

      </View>

      <Text style={styles.label}>YOUR NAME IN THIS SESSION</Text>
      <TextInput style={styles.input} value={displayName} onChangeText={setDisplayName}
        placeholder="Display name" placeholderTextColor={Colors.textMuted} maxLength={24} />

      {/* ── Public / Private ── */}
      <View style={styles.visibilityRow}>
        <TouchableOpacity style={[styles.visibilityBtn, !isPublic && styles.visibilityBtnActive]}
          onPress={() => setIsPublic(false)}>
          <MaterialCommunityIcons name="lock" size={15} color={!isPublic ? '#000' : Colors.textMuted} />
          <Text style={[styles.visibilityBtnText, !isPublic && styles.visibilityBtnTextActive]}>Private</Text>
        </TouchableOpacity>
        <TouchableOpacity style={[styles.visibilityBtn, isPublic && styles.visibilityBtnPublic]}
          onPress={() => setIsPublic(true)}>
          <MaterialCommunityIcons name="earth" size={15} color={isPublic ? '#000' : Colors.textMuted} />
          <Text style={[styles.visibilityBtnText, isPublic && styles.visibilityBtnTextActive]}>Public</Text>
        </TouchableOpacity>
      </View>
      <Text style={styles.hintText}>
        {isPublic ? '🌍 Anyone nearby can find & join.' : '🔒 Crew code required to join.'}
      </Text>

      {/* ── Location ── */}
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
            <Text style={styles.primaryBtnText}>Create &amp; Start</Text>
          </>
        )}
      </TouchableOpacity>
    </ScrollView>
  );

  // ═══════════════════════════════════════════════════════════════════════════
  // RENDER — Schedule
  // ═══════════════════════════════════════════════════════════════════════════

  const renderSchedule = () => (
    <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
      <TouchableOpacity onPress={() => setStep('landing')} style={styles.backBtn}>
        <MaterialCommunityIcons name="chevron-left" size={22} color={Colors.textMuted} />
        <Text style={styles.backText}>Back</Text>
      </TouchableOpacity>

      <Text style={styles.titleLarge}>Schedule a Session</Text>
      <Text style={[styles.subtitle, { marginBottom: 16 }]}>
        Your crew gets a push notification immediately and a 15-min reminder.
      </Text>

      {/* ── Crew picker ── */}
      <Text style={styles.label}>SESSION FOR</Text>
      <View style={styles.crewPickerRow}>
        {myCrews.map(crew => {
          const isSelected = selectedCrewId === crew.id;
          return (
            <TouchableOpacity key={crew.id}
              style={[styles.crewChip, isSelected && styles.crewChipActive]}
              onPress={() => { setSelectedCrewId(crew.id); setCrewName(''); }}>
              {crew.avatar_url ? (
                <Image source={{ uri: crew.avatar_url }} style={{ width: 16, height: 16, borderRadius: 8, marginRight: 2 }} />
              ) : (
                <View style={{ width: 16, height: 16, borderRadius: 8, backgroundColor: crew.avatar_color || '#FFAA00', alignItems: 'center', justifyContent: 'center', marginRight: 2 }}>
                  <MaterialCommunityIcons name={(crew.avatar_icon as any) || 'account-group'} size={10} color="#000" />
                </View>
              )}
              <Text style={[styles.crewChipText, isSelected && styles.crewChipTextActive]}
                numberOfLines={1}>{crew.name}</Text>
            </TouchableOpacity>
          );
        })}
      </View>

      <Text style={styles.label}>YOUR NAME IN THIS SESSION</Text>
      <TextInput style={styles.input} value={displayName} onChangeText={setDisplayName}
        placeholder="Display name" placeholderTextColor={Colors.textMuted} maxLength={24} />

      <Text style={[styles.label, { marginTop: 16 }]}>DATE &amp; TIME</Text>
      <TouchableOpacity style={styles.datePickerBtn} onPress={() => setShowDatePicker(true)}>
        <MaterialCommunityIcons name="calendar" size={18} color={Colors.primary} />
        <Text style={styles.datePickerBtnText}>
          {schedDateTime.toLocaleDateString('en-US', { weekday: 'long', month: 'long', day: 'numeric', year: 'numeric' })}
        </Text>
        <MaterialCommunityIcons name="chevron-right" size={18} color={Colors.textMuted} />
      </TouchableOpacity>
      <TouchableOpacity style={styles.datePickerBtn} onPress={() => setShowTimePicker(true)}>
        <MaterialCommunityIcons name="clock-outline" size={18} color={Colors.primary} />
        <Text style={styles.datePickerBtnText}>
          {schedDateTime.toLocaleTimeString('en-US', { hour: 'numeric', minute: '2-digit', hour12: true })}
        </Text>
        <MaterialCommunityIcons name="chevron-right" size={18} color={Colors.textMuted} />
      </TouchableOpacity>

      {showDatePicker && (
        <DateTimePicker value={schedDateTime} mode="date"
          display={Platform.OS === 'android' ? 'calendar' : 'spinner'}
          minimumDate={new Date()}
          onChange={(_evt: any, d?: Date) => {
            setShowDatePicker(false);
            if (d) {
              const m = new Date(d);
              m.setHours(schedDateTime.getHours(), schedDateTime.getMinutes());
              setSchedDateTime(m);
            }
          }} />
      )}
      {showTimePicker && (
        <DateTimePicker value={schedDateTime} mode="time"
          display={Platform.OS === 'android' ? 'clock' : 'spinner'}
          onChange={(_evt: any, d?: Date) => {
            setShowTimePicker(false);
            if (d) {
              const m = new Date(schedDateTime);
              m.setHours(d.getHours(), d.getMinutes());
              setSchedDateTime(m);
            }
          }} />
      )}

      {/* ── Location ── */}
      <Text style={styles.label}>LOCATION (OPTIONAL)</Text>
      <View style={{ flexDirection: 'row', gap: 8, alignItems: 'center', marginBottom: 16 }}>
        <TextInput
          style={[styles.input, { flex: 1, marginBottom: 0 }]}
          value={locationLabel}
          onChangeText={(txt) => { setLocationLabel(txt); setLocationCoords(undefined); }}
          placeholder="Enter address or park name"
          placeholderTextColor={Colors.textMuted}
        />
        <TouchableOpacity
          style={{ width: 48, height: 48, borderRadius: 12, backgroundColor: 'rgba(255,255,255,0.04)', justifyContent: 'center', alignItems: 'center', borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}
          onPress={handleDetectLocation}
          disabled={isGettingLocation}
        >
          {isGettingLocation ? (
            <ActivityIndicator size="small" color={Colors.primary} />
          ) : (
            <MaterialCommunityIcons name="crosshairs-gps" size={20} color={locationCoords ? Colors.primary : Colors.textMuted} />
          )}
        </TouchableOpacity>
      </View>
      {locationCoords && (
        <Text style={{ color: Colors.primary, fontSize: 10, marginTop: -12, marginBottom: 16, paddingHorizontal: 4, fontWeight: '700' }}>✓ Exact GPS coordinates attached</Text>
      )}

      {errorMsg ? <Text style={styles.errorText}>{errorMsg}</Text> : null}

      <TouchableOpacity style={[styles.primaryBtn, isLoading && { opacity: 0.5 }]}
        onPress={() => handleCreate(schedDateTime)} disabled={isLoading}>
        {isLoading ? <ActivityIndicator color="#000" /> : (
          <>
            <MaterialCommunityIcons name="calendar-check" size={18} color="#000" />
            <Text style={styles.primaryBtnText}>Schedule &amp; Notify Crew</Text>
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
    const isMe = item.user_id === currentUserId;
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
  // RENDER — Manage Crews Hub
  // ═══════════════════════════════════════════════════════════════════════════

  // Extended sporty & trendy icons
  const AVATAR_ICONS = ['roller-skate', 'account-group', 'skateboarding', 'shoe-sneaker', 'lightning-bolt', 'fire', 'star-shooting', 'crown', 'skull', 'headphones', 'rocket', 'flag-checkered', 'trophy', 'emoticon-cool-outline'];

  const handlePickCrewPhoto = async () => {
    const { status } = await ImagePicker.requestMediaLibraryPermissionsAsync();
    if (status !== 'granted') { Alert.alert('Permission needed', 'Enable photo library access in Settings.'); return; }
    const result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.Images,
      allowsEditing: true, aspect: [1, 1], quality: 0.7,
    });
    if (!result.canceled && result.assets[0]) setNewCrewPhotoUri(result.assets[0].uri);
  };

  const handleCreateCrew = async () => {
    if (!newCrewName.trim()) { setCreateCrewError('Enter a crew name'); return; }
    setIsCreatingCrew(true); setCreateCrewError('');
    try {
      const crew = await profileService.createPermanentCrew(newCrewName.trim(), {
        isPublic: newCrewIsPublic,
        avatarColor: newCrewColor,
        avatarIcon: newCrewIcon,
        city: newCrewCity || undefined,
        state: newCrewState || undefined,
        description: newCrewDescription || undefined,
        inviteCode: !newCrewIsPublic ? newCrewCode : undefined,
        members: selectedMembers.map(m => m.user_id)
      });
      setMyCrews(prev => [...prev, crew]);
      setPermanentCrews(prev => [...prev, { id: crew.id, name: crew.name }]);
      setNewCrewName(''); setNewCrewCity(''); setNewCrewState(''); setNewCrewDescription('');
      setNewCrewPhotoUri(null); setCreateCrewError('');
      setSelectedMembers([]); setUserSearchQuery('');
      setNewCrewCode(Math.random().toString(36).substring(2, 8).toUpperCase());
      Alert.alert('Crew Created! 🎉', `"${crew.name}" is ready. Pick it when starting a session.`);
      setStep('landing');  // Return to hub to show the new crew
    } catch (e: any) {
      setCreateCrewError(e.message ?? 'Failed to create crew');
    } finally { setIsCreatingCrew(false); }
  };

  const executeLeaveCrew = async (crew: PermanentCrew) => {
    try {
      await profileService.leavePermanentCrew(crew.id);
      setMyCrews(prev => prev.filter(c => c.id !== crew.id));
      setPermanentCrews(prev => prev.filter(c => c.id !== crew.id));
      setSelectedCrewDetail(null);
      setConfirmingLeaveCrewId(null);
    } catch (e: any) { Alert.alert('Error', e.message); }
  };

  const executeDeleteCrew = async (crew: PermanentCrew) => {
    try {
      await profileService.deleteCrew(crew.id);
      setMyCrews(prev => prev.filter(c => c.id !== crew.id));
      setPermanentCrews(prev => prev.filter(c => c.id !== crew.id));
      setSelectedCrewDetail(null);
      setConfirmingDeleteCrewId(null);
    } catch (e: any) { Alert.alert('Error', e.message); }
  };

  // ── Join public crew directly from Discover tab ─────────────────────────────
  const handleJoinPublicCrew = async (crew: PermanentCrew) => {
    setJoiningCrewId(crew.id);
    try {
      const joined = await profileService.joinPublicCrewById(crew.id);
      // Add to myCrews if not already there
      setMyCrews(prev => prev.find(c => c.id === joined.id) ? prev : [...prev, joined]);
      Alert.alert('Joined!', `You joined ${joined.name}. Find it in My Crews.`);
    } catch (e: any) {
      Alert.alert('Could not join', e.message ?? 'Unknown error');
    } finally {
      setJoiningCrewId(null);
    }
  };

  // ── Start editing a crew (owner only) ────────────────────────────────────────
  const handleStartEdit = (crew: PermanentCrew) => {
    setEditCrewName(crew.name);
    setEditCrewIsPublic(crew.is_public ?? false);
    setEditCrewCity(crew.city ?? '');
    setEditCrewState(crew.state ?? '');
    setEditCrewDesc(crew.description ?? '');
    setEditingCrewId(crew.id);
    setSelectedMembers([]);
    setUserSearchQuery('');
    loadCrewMembers(crew.id);
  };

  const handleAddMembersToCrew = async (crewId: string) => {
    if (selectedMembers.length === 0) return;
    setIsAddingMembersTo(crewId);
    try {
      const userIdsToAdd = selectedMembers.map(m => m.user_id);
      await profileService.addCrewMembers(crewId, userIdsToAdd);
      Alert.alert('Success', `Added ${selectedMembers.length} skater(s) to the crew!`);
      // Clear selection
      setSelectedMembers([]);
      setUserSearchQuery('');
      // Force refresh member lists
      setCardMembers(prev => ({ ...prev, [crewId]: undefined as any }));
      loadCrewMembers(crewId);
    } catch (e: any) {
      Alert.alert('Error', e.message ?? 'Could not add members to crew.');
    } finally {
      setIsAddingMembersTo(null);
    }
  };

  const handleSaveCrew = async (crew: PermanentCrew) => {
    if (!editCrewName.trim()) return;
    setIsSavingCrew(true);
    try {
      await profileService.updateCrew(crew.id, {
        name: editCrewName.trim(),
        isPublic: editCrewIsPublic,
        city: editCrewCity.trim() || undefined,
        state: editCrewState.trim() || undefined,
        description: editCrewDesc.trim() || undefined,
      });
      // Update local state
      const updated = {
        ...crew, name: editCrewName.trim(), is_public: editCrewIsPublic,
        city: editCrewCity.trim() || undefined, state: editCrewState.trim() || undefined,
        description: editCrewDesc.trim() || undefined
      };
      setMyCrews(prev => prev.map(c => c.id === crew.id ? updated : c));
      setSelectedCrewDetail(updated);
      setEditingCrewId(null);
    } catch (e: any) {
      Alert.alert('Save failed', e.message ?? 'Unknown error');
    } finally {
      setIsSavingCrew(false);
    }
  };

  const renderCrewCard = (crew: PermanentCrew, onTap: () => void, showJoinBtn = false) => {
    const info = crewMemberCounts[crew.id];
    const alreadyMember = myCrews.some(c => c.id === crew.id);
    const isJoining = joiningCrewId === crew.id;
    return (
      <TouchableOpacity key={crew.id} style={styles.mgCrewCard} onPress={onTap}>
        {/* Avatar */}
        {crew.avatar_url
          ? <Image source={{ uri: crew.avatar_url }} style={styles.mgAvatarImg} />
          : <View style={[styles.mgAvatar, { backgroundColor: crew.avatar_color ?? '#FFAA00' }]}>
            <MaterialCommunityIcons name={(crew.avatar_icon ?? 'account-group') as any} size={22} color="#000" />
          </View>}
        {/* Info */}
        <View style={{ flex: 1 }}>
          <Text style={styles.mgCrewName} numberOfLines={1}>{crew.name}</Text>
          {(crew.city || crew.state) && (
            <Text style={styles.mgCrewSub} numberOfLines={1}>
              <MaterialCommunityIcons name="map-marker" size={11} color={Colors.textMuted} />
              {' '}{[crew.city, crew.state].filter(Boolean).join(', ')}
            </Text>
          )}
          {info && (
            <View style={styles.mgAvatarRow}>
              {info.avatarColors.slice(0, 5).map((c, i) => (
                <View key={i} style={[styles.mgMemberDot, { backgroundColor: c, marginLeft: i > 0 ? -6 : 0 }]} />
              ))}
              <Text style={styles.mgMemberCount}>{info.count} member{info.count !== 1 ? 's' : ''}</Text>
            </View>
          )}
        </View>
        {/* Badges + Join */}
        <View style={{ alignItems: 'flex-end', gap: 4 }}>
          {crew.is_owner && <View style={styles.mgOwnerBadge}><Text style={styles.mgBadgeText}>Owner</Text></View>}
          {crew.is_public
            ? <View style={[styles.mgOwnerBadge, { backgroundColor: 'rgba(0,200,100,0.15)' }]}><Text style={[styles.mgBadgeText, { color: '#00C864' }]}>Public</Text></View>
            : <View style={[styles.mgOwnerBadge, { backgroundColor: 'rgba(255,255,255,0.06)' }]}><Text style={[styles.mgBadgeText, { color: Colors.textMuted }]}>Private</Text></View>}
          {showJoinBtn && !alreadyMember && (
            <TouchableOpacity
              style={[styles.mgOwnerBadge, { backgroundColor: Colors.primary, paddingHorizontal: 10, paddingVertical: 5 }]}
              onPress={(e) => { e.stopPropagation?.(); handleJoinPublicCrew(crew); }}
              disabled={isJoining}
            >
              <Text style={[styles.mgBadgeText, { color: '#000', fontWeight: '800' }]}>
                {isJoining ? '…' : 'Join'}
              </Text>
            </TouchableOpacity>
          )}
          {showJoinBtn && alreadyMember && (
            <View style={[styles.mgOwnerBadge, { backgroundColor: 'rgba(0,200,100,0.12)' }]}>
              <Text style={[styles.mgBadgeText, { color: '#00C864' }]}>✓ Joined</Text>
            </View>
          )}
        </View>
      </TouchableOpacity>
    );
  };

  const renderManage = () => {
    return (
      <View style={{ flex: 1 }}>
        {/* Header */}
        <TouchableOpacity onPress={() => setStep('landing')} style={styles.backBtn}>
          <MaterialCommunityIcons name="chevron-left" size={22} color={Colors.textMuted} />
          <Text style={styles.backText}>Back</Text>
        </TouchableOpacity>
        <Text style={styles.titleLarge}>Create a Crew</Text>

        <ScrollView showsVerticalScrollIndicator={false} keyboardShouldPersistTaps="handled"
          contentContainerStyle={{ padding: 16, paddingBottom: 60 }}>

          <Text style={styles.label}>CREW NAME</Text>
          <TextInput style={styles.input} value={newCrewName} onChangeText={setNewCrewName}
            placeholder="e.g. Friday Night Shredders" placeholderTextColor={Colors.textMuted} maxLength={40} />

          <Text style={styles.label}>DESCRIPTION (OPTIONAL)</Text>
          <TextInput style={[styles.input, { height: 72, textAlignVertical: 'top' }]}
            value={newCrewDescription} onChangeText={setNewCrewDescription} multiline
            placeholder="What's this crew about?" placeholderTextColor={Colors.textMuted} maxLength={120} />

          {/* Unified Avatar Selection */}
          <View style={{ marginBottom: 16 }}>
            <Text style={styles.label}>CREW AVATAR</Text>

            {newCrewPhotoUri ? (
              <View style={{ alignItems: 'center', marginBottom: 8, padding: 16, backgroundColor: 'rgba(255,255,255,0.02)', borderRadius: 12, borderWidth: 1, borderColor: 'rgba(255,255,255,0.05)' }}>
                <TouchableOpacity style={[styles.mgPhotoBtn, { height: 120, width: 120, borderRadius: 60, marginBottom: 12 }]} onPress={handlePickCrewPhoto}>
                  <Image source={{ uri: newCrewPhotoUri }} style={styles.mgPhotoBtnImg} />
                </TouchableOpacity>
                <TouchableOpacity onPress={() => setNewCrewPhotoUri(null)} style={{ paddingVertical: 8, paddingHorizontal: 16, backgroundColor: 'rgba(255,68,68,0.1)', borderRadius: 20 }}>
                  <Text style={{ color: '#FF4444', fontWeight: 'bold' }}>Remove Photo</Text>
                </TouchableOpacity>
              </View>
            ) : (
              <View style={{ backgroundColor: 'rgba(255,255,255,0.02)', padding: 16, borderRadius: 12, borderWidth: 1, borderColor: 'rgba(255,255,255,0.05)' }}>

                <View style={{ flexDirection: 'row', alignItems: 'center', gap: 12, marginBottom: 16 }}>
                  {/* Live Avatar Preview */}
                  <View style={{ width: 60, height: 60, borderRadius: 30, backgroundColor: newCrewColor, alignItems: 'center', justifyContent: 'center', borderWidth: 2, borderColor: 'rgba(255,255,255,0.2)' }}>
                    <MaterialCommunityIcons name={newCrewIcon as any} size={38} color="#000" />
                  </View>

                  {/* Photo Upload Option */}
                  <TouchableOpacity style={[styles.mgPhotoBtn, { height: 60, flex: 1, marginBottom: 0 }]} onPress={handlePickCrewPhoto}>
                    <MaterialCommunityIcons name="camera-plus" size={22} color={Colors.primary} />
                    <Text style={styles.mgPhotoBtnText}>Upload Photo instead</Text>
                  </TouchableOpacity>
                </View>

                <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 16 }}>
                  <View style={{ flex: 1, height: 1, backgroundColor: 'rgba(255,255,255,0.1)' }} />
                  <Text style={[styles.label, { marginHorizontal: 10, marginTop: 0, color: 'rgba(255,255,255,0.3)' }]}>OR PICK AN ICON</Text>
                  <View style={{ flex: 1, height: 1, backgroundColor: 'rgba(255,255,255,0.1)' }} />
                </View>

                {/* Avatar Icon */}
                <View style={styles.mgIconRow}>
                  {AVATAR_ICONS.map(ic => (
                    <TouchableOpacity key={ic} onPress={() => setNewCrewIcon(ic)}
                      style={[styles.mgIconBtn, newCrewIcon === ic && { backgroundColor: newCrewColor }]}>
                      <MaterialCommunityIcons name={ic as any} size={22} color={newCrewIcon === ic ? '#000' : Colors.textMuted} />
                    </TouchableOpacity>
                  ))}
                </View>

                {/* Hue Slider for Icon Color */}
                <Text style={[styles.label, { marginTop: 16, marginBottom: 8 }]}>ICON COLOR</Text>
                <View style={[styles.controlRow, { flexShrink: 0, minHeight: 40 }]}>
                  <CustomSlider
                    gradientTrack={true}
                    value={newCrewHue}
                    onValueChange={(hue) => {
                      setNewCrewHue(hue);
                      const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                      const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0").toUpperCase()).join("");
                      setNewCrewColor(rgb2hex(f(5), f(3), f(1)));
                    }}
                    minimumValue={0}
                    maximumValue={360}
                    style={{ flex: 1 }}
                  />
                </View>
              </View>
            )}
          </View>

          {/* Public / Private */}
          <Text style={styles.label}>VISIBILITY</Text>
          <View style={styles.visibilityRow}>
            <TouchableOpacity style={[styles.visibilityBtn, !newCrewIsPublic && styles.visibilityBtnActive]}
              onPress={() => setNewCrewIsPublic(false)}>
              <MaterialCommunityIcons name="lock" size={15} color={!newCrewIsPublic ? '#000' : Colors.textMuted} />
              <Text style={[styles.visibilityBtnText, !newCrewIsPublic && styles.visibilityBtnTextActive]}>Private</Text>
            </TouchableOpacity>
            <TouchableOpacity style={[styles.visibilityBtn, newCrewIsPublic && styles.visibilityBtnPublic]}
              onPress={() => setNewCrewIsPublic(true)}>
              <MaterialCommunityIcons name="earth" size={15} color={newCrewIsPublic ? '#000' : Colors.textMuted} />
              <Text style={[styles.visibilityBtnText, newCrewIsPublic && styles.visibilityBtnTextActive]}>Public</Text>
            </TouchableOpacity>
          </View>
          <Text style={[styles.hintText, { marginBottom: 16 }]}>
            {newCrewIsPublic ? '🌍 Anyone can find & join via Discover.' : '🔒 Invite code required to join.'}
          </Text>

          {/* Home City */}
          <Text style={styles.label}>HOME CITY (OPTIONAL)</Text>
          <View style={{ flexDirection: 'row', gap: 8, marginBottom: 16 }}>
            <TextInput style={[styles.input, { flex: 2 }]} value={newCrewCity} onChangeText={setNewCrewCity}
              placeholder="City" placeholderTextColor={Colors.textMuted} />
            <TextInput style={[styles.input, { flex: 1 }]} value={newCrewState} onChangeText={setNewCrewState}
              placeholder="State" placeholderTextColor={Colors.textMuted} />
          </View>

          {/* Add Members Section */}
          <Text style={styles.label}>ADD MEMBERS</Text>
          <View style={{ backgroundColor: 'rgba(255,255,255,0.02)', padding: 12, borderRadius: 12, borderWidth: 1, borderColor: 'rgba(255,255,255,0.05)', marginBottom: 12 }}>
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
            <TextInput style={[styles.input, { marginBottom: 0 }]} value={userSearchQuery} onChangeText={setUserSearchQuery} placeholder="Search username..." placeholderTextColor={Colors.textMuted} />
            {userSearchResults.length > 0 && (
              <View style={{ marginTop: 8, gap: 4 }}>
                {userSearchResults.map(u => (
                  !selectedMembers.find(m => m.user_id === u.user_id) && (
                    <TouchableOpacity key={u.user_id} onPress={() => { setSelectedMembers(prev => [...prev, u]); setUserSearchQuery(''); }} style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: 'rgba(255,255,255,0.04)', padding: 8, borderRadius: 8 }}>
                      <MaterialCommunityIcons name="account" size={16} color={Colors.textMuted} style={{ marginRight: 6 }} />
                      <Text style={{ color: '#FFF', fontSize: 13, flex: 1 }}>{u.display_name} <Text style={{ color: Colors.textMuted }}>@{u.username}</Text></Text>
                      <MaterialCommunityIcons name="plus" size={18} color={Colors.primary} />
                    </TouchableOpacity>
                  )
                ))}
              </View>
            )}
          </View>

          {/* Render private code preview if private */}
          {!newCrewIsPublic && (
            <View style={{ backgroundColor: 'rgba(255,170,0,0.05)', borderRadius: 12, borderWidth: 1, borderColor: 'rgba(255,170,0,0.2)', padding: 16, alignItems: 'center', marginTop: 12, marginBottom: 8 }}>
              <Text style={{ color: 'rgba(255,170,0,0.8)', fontSize: 10, fontWeight: '800', letterSpacing: 1.5, marginBottom: 6 }}>PRIVATE INVITE CODE</Text>
              <Text style={{ color: Colors.primary || '#FFAA00', fontSize: 26, fontWeight: '900', letterSpacing: 6, fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace' }}>
                {newCrewCode}
              </Text>
              <Text style={{ color: Colors.textMuted, fontSize: 11, marginTop: 6, textAlign: 'center' }}>
                Share this code with friends so they can join your crew.
              </Text>
            </View>
          )}

          {createCrewError ? <Text style={{ color: '#FF4444', marginVertical: 8 }}>{createCrewError}</Text> : null}

          <TouchableOpacity style={[styles.primaryBtn, { marginTop: 20 }]} onPress={handleCreateCrew} disabled={isCreatingCrew}>
            {isCreatingCrew ? <ActivityIndicator size="small" color="#000" /> : <MaterialCommunityIcons name="check" size={18} color="#000" />}
            <Text style={styles.primaryBtnText}>Create Crew</Text>
          </TouchableOpacity>
        </ScrollView>
      </View>
    );
  };


  // ── Crew Detail sheet ──────────────────────────────────────────────────────────────
  const renderCrewDetail = () => {
    const crew = selectedCrewDetail;
    if (!crew) return null;
    const info = crewMemberCounts[crew.id];
    return (
      <ScrollView contentContainerStyle={{ padding: 20, paddingBottom: 60 }} showsVerticalScrollIndicator={false}>
        <TouchableOpacity onPress={() => setSelectedCrewDetail(null)} style={styles.backBtn}>
          <MaterialCommunityIcons name="chevron-left" size={22} color={Colors.textMuted} />
          <Text style={styles.backText}>Back</Text>
        </TouchableOpacity>

        {/* Crew avatar */}
        <View style={{ alignItems: 'center', marginVertical: 16 }}>
          {crew.avatar_url
            ? <Image source={{ uri: crew.avatar_url }} style={[styles.mgAvatarImg, { width: 72, height: 72, borderRadius: 36 }]} />
            : <View style={[styles.mgAvatar, { width: 72, height: 72, borderRadius: 36, backgroundColor: crew.avatar_color ?? '#FFAA00' }]}>
              <MaterialCommunityIcons name={(crew.avatar_icon ?? 'account-group') as any} size={32} color="#000" />
            </View>}
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

          {/* Inline edit form */}
          {crew.is_owner && editingCrewId === crew.id && (
            <View style={{ gap: 10 }}>
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
                      <Text style={styles.primaryBtnText}>Invite & Add Sktaters</Text>
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
        </View>
      </ScrollView>
    );
  };

  // ═══════════════════════════════════════════════════════════════════════════
  // MAIN RENDER
  // ═══════════════════════════════════════════════════════════════════════════

  return (
    <Modal visible={visible} animationType="slide" transparent statusBarTranslucent>
      <KeyboardAvoidingView
        style={styles.overlay}
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        keyboardVerticalOffset={Platform.OS === 'ios' ? 0 : 24}>
        <View style={styles.sheet}>
          <TouchableOpacity style={styles.closeBtn} onPress={onClose}>
            <MaterialCommunityIcons name="close" size={20} color={Colors.textMuted} />
          </TouchableOpacity>

          {step === 'landing' && renderLanding()}
          {step === 'create' && renderCreate()}
          {step === 'schedule' && renderSchedule()}
          {step === 'join' && renderJoin()}
          {step === 'controller' && renderController()}
          {step === 'manage' && !selectedCrewDetail && renderManage()}
          {step === 'manage' && !!selectedCrewDetail && renderCrewDetail()}
        </View>
      </KeyboardAvoidingView>
    </Modal>
  );
}

// ─── Styles ───────────────────────────────────────────────────────────────────

const createStyles = (Colors: any) => StyleSheet.create({
  overlay: { flex: 1, backgroundColor: 'rgba(0,0,0,0.8)', justifyContent: 'flex-end' },
  sheet: {
    backgroundColor: Colors.background || '#0D0D0D',
    borderTopLeftRadius: 26, borderTopRightRadius: 26,
    maxHeight: '92%', minHeight: '55%', paddingTop: 10,
  },
  closeBtn: { position: 'absolute', top: 10, right: 16, zIndex: 10, padding: 8 },
  body: { alignItems: 'center', paddingHorizontal: 24, paddingBottom: 32, paddingTop: 6 },

  // Typography
  titleLarge: { color: Colors.text || '#FFF', fontSize: 24, fontWeight: '800', marginBottom: 8, textAlign: 'center' },
  subtitle: { color: Colors.textMuted || '#888', fontSize: 13, textAlign: 'center', lineHeight: 19, marginBottom: 20 },

  // ── Crew Hub styles ──────────────────────────────────────────────────────
  hubHeader: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between',
    width: '100%', marginBottom: 18, paddingTop: 0,
  },
  hubTitle: { color: Colors.text || '#FFF', fontSize: 22, fontWeight: '900' },
  hubSub: { color: Colors.textMuted || '#888', fontSize: 11, marginTop: 2 },
  hubStartBtn: {
    flexDirection: 'row', alignItems: 'center', gap: 5,
    backgroundColor: Colors.primary || '#FFAA00',
    borderRadius: 20, paddingHorizontal: 14, paddingVertical: 9,
  },
  hubStartBtnText: { color: '#000', fontSize: 13, fontWeight: '800' },

  hubSectionLabel: {
    color: Colors.textMuted || '#888', fontSize: 10, fontWeight: '700',
    letterSpacing: 1.2, alignSelf: 'flex-start', marginBottom: 8, marginTop: 4,
  },
  hubSectionRow: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between',
    width: '100%', marginTop: 20, marginBottom: 8,
  },

  hubEmptyCard: {
    width: '100%', alignItems: 'center', padding: 20,
    backgroundColor: 'rgba(255,255,255,0.04)', borderRadius: 16,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)', marginBottom: 8,
  },
  hubEmptyText: { color: Colors.textMuted || '#888', fontSize: 13, textAlign: 'center' },

  hubCrewCard: {
    width: '100%', backgroundColor: 'rgba(255,255,255,0.05)',
    borderRadius: 16, padding: 14, marginBottom: 10,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.09)',
  },
  hubCrewCardTop: { flexDirection: 'row', alignItems: 'center' },
  hubCrewAvatar: {
    width: 40, height: 40, borderRadius: 20,
    alignItems: 'center', justifyContent: 'center',
  },
  hubCrewName: { color: Colors.text || '#FFF', fontSize: 15, fontWeight: '700' },
  hubCrewMeta: { color: Colors.textMuted || '#888', fontSize: 11, marginTop: 2 },
  hubOwnerBadge: { backgroundColor: 'rgba(255,170,0,0.15)', borderRadius: 6, paddingHorizontal: 6, paddingVertical: 2 },
  hubOwnerBadgeText: { color: Colors.primary || '#FFAA00', fontSize: 9, fontWeight: '800' },
  hubCrewManageBtn: { padding: 6 },

  hubLiveSessionRow: {
    flexDirection: 'row', alignItems: 'center', gap: 10,
    marginTop: 10, padding: 10,
    backgroundColor: 'rgba(0,230,118,0.08)',
    borderRadius: 12, borderWidth: 1, borderColor: 'rgba(0,230,118,0.2)',
  },
  hubLiveDot: { width: 8, height: 8, borderRadius: 4, backgroundColor: '#00E676' },
  hubLiveSessionName: { color: Colors.text || '#FFF', fontSize: 13, fontWeight: '700' },
  hubLiveSessionMeta: { color: Colors.textMuted || '#888', fontSize: 10, marginTop: 1 },
  hubJoinPill: {
    flexDirection: 'row', alignItems: 'center', gap: 3,
    backgroundColor: Colors.primary || '#FFAA00',
    borderRadius: 20, paddingHorizontal: 10, paddingVertical: 6,
  },
  hubJoinPillText: { color: '#000', fontSize: 11, fontWeight: '900' },

  hubNoSessionRow: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between',
    marginTop: 8, paddingTop: 8,
    borderTopWidth: 1, borderTopColor: 'rgba(255,255,255,0.06)',
  },
  hubNoSessionText: { color: Colors.textMuted || '#888', fontSize: 12 },

  hubCrewActions: {
    flexDirection: 'row', gap: 8, width: '100%', marginTop: 4, marginBottom: 4,
  },
  hubCrewActionBtn: {
    flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 5,
    borderWidth: 1.5, borderColor: 'rgba(255,255,255,0.12)',
    borderRadius: 12, paddingVertical: 10,
  },
  hubCrewActionBtnText: { color: Colors.primary || '#FFAA00', fontSize: 11, fontWeight: '700' },

  hubActionChip: {
    flexDirection: 'row', alignItems: 'center', gap: 4,
    borderWidth: 1.5, borderColor: 'rgba(255,170,0,0.35)',
    borderRadius: 20, paddingHorizontal: 10, paddingVertical: 5,
  },
  hubActionChipText: { color: Colors.primary || '#FFAA00', fontSize: 11, fontWeight: '700' },

  hubCodeEntry: {
    width: '100%', marginTop: 8, marginBottom: 4,
    backgroundColor: 'rgba(255,255,255,0.04)',
    borderRadius: 14, padding: 14,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)',
  },
  label: { color: Colors.textMuted || '#888', fontSize: 10, fontWeight: '700', letterSpacing: 1.2, alignSelf: 'flex-start', marginBottom: 5, marginTop: 12 },

  // Buttons
  primaryBtn: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 8,
    backgroundColor: Colors.primary || '#FFAA00',
    borderRadius: 14, paddingVertical: 14, paddingHorizontal: 24, width: '100%', marginTop: 16,
  },
  primaryBtnText: { color: '#000', fontSize: 15, fontWeight: '800' },
  secondaryBtn: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 8,
    borderWidth: 1.5, borderColor: Colors.primary || '#FFAA00',
    borderRadius: 14, paddingVertical: 13, paddingHorizontal: 24, width: '100%', marginTop: 10,
  },
  secondaryBtnText: { color: Colors.primary || '#FFAA00', fontSize: 15, fontWeight: '700' },
  backBtn: { flexDirection: 'row', alignItems: 'center', alignSelf: 'flex-start', marginBottom: 10, gap: 2 },
  backText: { color: Colors.textMuted || '#888', fontSize: 14 },

  // Input
  input: {
    backgroundColor: 'rgba(255,255,255,0.07)', borderRadius: 12,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.12)',
    color: Colors.text || '#FFF', fontSize: 15,
    paddingHorizontal: 14, paddingVertical: 11, width: '100%', marginBottom: 2,
  },
  codeInput: {
    fontSize: 26, fontWeight: '900', letterSpacing: 8, textAlign: 'center',
    fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace'
  },
  errorText: { color: '#FF4444', fontSize: 12, marginTop: 6, textAlign: 'center' },

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

  // Crew picker chips
  crewPickerRow: { flexDirection: 'row', flexWrap: 'wrap', gap: 8, marginBottom: 14, alignSelf: 'flex-start' },
  crewChip: { flexDirection: 'row', alignItems: 'center', gap: 5, borderWidth: 1.5, borderColor: 'rgba(255,255,255,0.18)', borderRadius: 20, paddingHorizontal: 12, paddingVertical: 7, maxWidth: 160 },
  crewChipActive: { backgroundColor: '#FFAA00', borderColor: '#FFAA00' },
  crewChipText: { color: '#bbb', fontSize: 12, fontWeight: '700', flexShrink: 1 },
  crewChipTextActive: { color: '#000' },

  // Public/Private visibility toggle
  visibilityRow: { flexDirection: 'row', gap: 10, marginBottom: 6, width: '100%' },
  visibilityBtn: { flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 6, borderWidth: 1.5, borderColor: 'rgba(255,255,255,0.18)', borderRadius: 12, paddingVertical: 10 },
  visibilityBtnActive: { backgroundColor: 'rgba(255,255,255,0.12)', borderColor: 'rgba(255,255,255,0.5)' },
  visibilityBtnPublic: { backgroundColor: '#00CC66', borderColor: '#00CC66' },
  visibilityBtnText: { color: '#999', fontSize: 13, fontWeight: '700' },
  visibilityBtnTextActive: { color: '#fff' },
  hintText: { color: '#777', fontSize: 12, marginBottom: 12, alignSelf: 'flex-start' },

  // Calendar date/time picker buttons
  datePickerBtn: { flexDirection: 'row', alignItems: 'center', gap: 10, width: '100%', borderWidth: 1.5, borderColor: 'rgba(255,255,255,0.14)', borderRadius: 12, paddingVertical: 12, paddingHorizontal: 14, marginBottom: 10 },
  datePickerBtnText: { flex: 1, color: '#fff', fontSize: 14, fontWeight: '600' },

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
  divider: { flexDirection: 'row', alignItems: 'center', marginVertical: 14, width: '100%', gap: 8 },
  dividerLine: { flex: 1, height: 1, backgroundColor: 'rgba(255,255,255,0.08)' },
  dividerText: { color: Colors.textMuted || '#888', fontSize: 10, fontWeight: '700', letterSpacing: 1 },

  // Controller card
  controllerCard: {
    margin: 16,
    backgroundColor: 'rgba(255,255,255,0.04)',
    borderRadius: 18, padding: 16,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)',
  },
  controllerCrewName: { color: Colors.text || '#FFF', fontSize: 20, fontWeight: '900', flex: 1 },
  controllerRole: { color: Colors.textMuted || '#888', fontSize: 12, marginBottom: 10 },
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
  inviteCode: { color: '#FFD700', fontSize: 22, fontWeight: '900', fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace', letterSpacing: 4, flex: 1 },
  syncRow: {
    flexDirection: 'row', alignItems: 'center', gap: 8, marginTop: 8,
    backgroundColor: 'rgba(0,170,255,0.08)', borderRadius: 10, padding: 10,
  },
  syncDot: { width: 8, height: 8, borderRadius: 4, backgroundColor: '#00AAFF' },
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
  memberAvatarText: { color: '#FFF', fontSize: 15, fontWeight: '700' },
  memberName: { color: Colors.text || '#FFF', fontSize: 14, fontWeight: '600' },
  memberLeaderBadge: { color: '#FFD700', fontSize: 10, marginTop: 1 },
  handoffToggle: {
    flexDirection: 'row', alignItems: 'center', gap: 7,
    borderWidth: 1.5, borderColor: '#FFD700', borderRadius: 10,
    paddingVertical: 9, paddingHorizontal: 14,
  },
  handoffToggleActive: { backgroundColor: '#FFD700' },
  handoffToggleText: { color: '#FFD700', fontSize: 13, fontWeight: '700' },
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
  doneBtn: { flex: 2, backgroundColor: Colors.primary || '#FFAA00', borderRadius: 14, paddingVertical: 14, alignItems: 'center' },
  doneBtnText: { color: '#000', fontSize: 14, fontWeight: '800' },
  endBtn: { flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 4, borderWidth: 1.5, borderColor: '#FF4444', borderRadius: 14, paddingVertical: 14 },
  endBtnText: { color: '#FF4444', fontSize: 13, fontWeight: '700' },

  // ── Manage Crews Hub styles ────────────────────────────────────────────────
  mgTabBar: { flexDirection: 'row', paddingHorizontal: 12, gap: 6, marginBottom: 4 },
  mgTab: { flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 4, paddingVertical: 8, borderRadius: 10, backgroundColor: 'rgba(255,255,255,0.06)' },
  mgTabActive: { backgroundColor: Colors.primary || '#FFAA00' },
  mgTabText: { fontSize: 11, fontWeight: '700', color: Colors.textMuted || '#888' },
  mgEmptyText: { color: Colors.textMuted || '#888', fontSize: 14, textAlign: 'center', marginTop: 40, lineHeight: 22 },
  mgSearchInput: { backgroundColor: 'rgba(255,255,255,0.07)', borderRadius: 10, borderWidth: 1, borderColor: 'rgba(255,255,255,0.12)', color: Colors.text || '#FFF', fontSize: 14, paddingHorizontal: 12, paddingVertical: 9, marginBottom: 6 },
  mgHint: { color: Colors.textMuted || '#888', fontSize: 11, lineHeight: 16 },

  // Crew card
  mgCrewCard: { flexDirection: 'row', alignItems: 'center', gap: 12, backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14, padding: 14, marginBottom: 10 },
  mgAvatar: { width: 44, height: 44, borderRadius: 22, alignItems: 'center', justifyContent: 'center' },
  mgAvatarImg: { width: 44, height: 44, borderRadius: 22 },
  mgCrewName: { color: Colors.text || '#FFF', fontSize: 15, fontWeight: '700' },
  mgCrewSub: { color: Colors.textMuted || '#888', fontSize: 11, marginTop: 2 },
  mgAvatarRow: { flexDirection: 'row', alignItems: 'center', marginTop: 4 },
  mgMemberDot: { width: 18, height: 18, borderRadius: 9 },
  mgMemberCount: { color: Colors.textMuted || '#888', fontSize: 11, marginLeft: 6 },
  mgOwnerBadge: { backgroundColor: 'rgba(255,170,0,0.15)', borderRadius: 6, paddingHorizontal: 8, paddingVertical: 3 },
  mgBadgeText: { color: Colors.primary || '#FFAA00', fontSize: 10, fontWeight: '800' },

  // Create crew form
  mgPhotoBtn: { height: 80, borderRadius: 12, borderWidth: 1.5, borderStyle: 'dashed', borderColor: 'rgba(255,255,255,0.2)', alignItems: 'center', justifyContent: 'center', gap: 6, marginBottom: 4, overflow: 'hidden' },
  mgPhotoBtnImg: { width: '100%', height: '100%', resizeMode: 'cover' },
  mgPhotoBtnText: { color: Colors.textMuted || '#888', fontSize: 13 },
  mgColorRow: { flexDirection: 'row', flexWrap: 'wrap', gap: 10, marginBottom: 4 },
  mgColorSwatch: { width: 32, height: 32, borderRadius: 16 },
  mgColorActive: { borderWidth: 3, borderColor: '#FFF' },
  mgIconRow: { flexDirection: 'row', flexWrap: 'wrap', gap: 8, marginBottom: 4 },
  mgIconBtn: { width: 44, height: 44, borderRadius: 10, alignItems: 'center', justifyContent: 'center', backgroundColor: 'rgba(255,255,255,0.07)' },

  // Crew detail
  mgCodeBox: { backgroundColor: 'rgba(255,255,255,0.06)', borderRadius: 12, padding: 14, alignItems: 'center', gap: 4 },
  mgCodeText: { color: Colors.text || '#FFF', fontSize: 28, fontWeight: '900', letterSpacing: 6, fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace' },

  // Danger actions
  dangerBtn: { flexDirection: 'row', alignItems: 'center', gap: 8, borderWidth: 1.5, borderColor: 'rgba(255,68,68,0.4)', borderRadius: 12, padding: 14 },
  dangerBtnText: { color: '#FF4444', fontSize: 14, fontWeight: '700' },

  // Edit actions
  editBtn: { flexDirection: 'row', alignItems: 'center', gap: 8, borderWidth: 1.5, borderColor: 'rgba(255,170,0,0.35)', borderRadius: 12, padding: 14 },
  editBtnText: { color: Colors.primary || '#FFAA00', fontSize: 14, fontWeight: '700' },

  // Share invite
  shareBtn: { flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 8, backgroundColor: Colors.primary || '#FFAA00', borderRadius: 12, paddingVertical: 13, marginTop: 8 },
  shareBtnText: { color: '#000', fontSize: 14, fontWeight: '800' },

  // Crew stats row
  statsRow: { flexDirection: 'row', gap: 8, marginTop: 14, marginBottom: 4 },
  statCard: { flex: 1, backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 12, padding: 12, alignItems: 'center' },
  statNum: { color: Colors.text || '#FFF', fontSize: 16, fontWeight: '800', textAlign: 'center' },
  statLabel: { color: Colors.textMuted || '#888', fontSize: 10, fontWeight: '600', marginTop: 2, textAlign: 'center' },

  // Nearby sessions
  nearbySessionCard: { flexDirection: 'row', alignItems: 'center', gap: 10, backgroundColor: 'rgba(255,255,255,0.06)', borderRadius: 12, padding: 12, marginBottom: 8 },
  nearbySessionName: { color: Colors.text || '#FFF', fontSize: 14, fontWeight: '700' },
  nearbySessionSub: { color: Colors.textMuted || '#888', fontSize: 11, marginTop: 2 },
  nearbyJoinBtn: { backgroundColor: Colors.primary || '#FFAA00', borderRadius: 10, paddingHorizontal: 14, paddingVertical: 8, minWidth: 56, alignItems: 'center' },
  nearbyJoinText: { color: '#000', fontSize: 13, fontWeight: '900' },

  // Radius pill selector
  radiusPillRow: { flexDirection: 'row', gap: 6, paddingHorizontal: 2, paddingBottom: 2 },
  radiusPill: { borderWidth: 1.5, borderColor: 'rgba(255,255,255,0.15)', borderRadius: 20, paddingHorizontal: 12, paddingVertical: 6 },
  radiusPillActive: { backgroundColor: Colors.primary || '#FFAA00', borderColor: Colors.primary || '#FFAA00' },
  radiusPillText: { color: Colors.textMuted || '#888', fontSize: 12, fontWeight: '700' },
  radiusPillTextActive: { color: '#000', fontSize: 12, fontWeight: '700' },
});
