/**
 * CrewMemberDashboard.tsx
 *
 * Full-screen overlay shown to crew MEMBERS when they are connected to their
 * devices AND inside an active crew session. Replaces the normal controller
 * view so the member can see exactly what's happening in the session.
 *
 * Displays:
 *   • Session header  — name, status badge, active duration timer
 *   • Location card   — venue name + GPS coords (if available)
 *   • Live mode card  — current mode / pattern / color the leader is broadcasting
 *   • Mini visualizer — locked to leader's current scene (read-only)
 *   • Member list     — who's in the session with online dots
 *   • Stats row       — elapsed time, member count, session start
 *   • Footer          — Leave Session button
 */

import React, { useState, useEffect, useRef } from 'react';
import {
  View, Text, ScrollView, TouchableOpacity, StyleSheet,
  Animated, Dimensions,
} from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../context/ThemeContext';
import { CrewSession, CrewRole } from '../services/CrewService';
import { supabase } from '../services/supabaseClient';
import { shareSessionInvite } from '../services/SessionShareService';
import { Spacing } from '../theme/theme';

// ─── Types ────────────────────────────────────────────────────────────────────

interface CrewMember {
  user_id: string;
  display_name: string | null;
  avatar_color: string | null;
  role: 'leader' | 'member';
  joined_at: string;
}

interface Props {
  session: CrewSession;
  role: CrewRole;
  /** The last scene broadcast from the leader (raw payload from cloud) */
  currentScene: Record<string, any> | null;
  onLeave: () => void;
}

// ─── Helpers ──────────────────────────────────────────────────────────────────

function formatDuration(startIso: string): string {
  const diffMs = Date.now() - new Date(startIso).getTime();
  const totalSec = Math.floor(diffMs / 1000);
  const h = Math.floor(totalSec / 3600);
  const m = Math.floor((totalSec % 3600) / 60);
  const s = totalSec % 60;
  if (h > 0) return `${h}h ${m}m`;
  if (m > 0) return `${m}m ${s}s`;
  return `${s}s`;
}

function formatTime(iso: string): string {
  return new Date(iso).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}

function sceneModeName(scene: Record<string, any> | null): string {
  if (!scene) return 'Waiting for leader...';
  const mode = scene.mode as string | undefined;
  if (!mode) return 'Unknown';
  const map: Record<string, string> = {
    solid: 'Solid Color',
    music: 'Music Sync',
    multimode: 'Multi Mode',
    fixed: 'Fixed Pattern',
    street: 'Street Mode',
    camera: 'Camera Color',
  };
  return map[mode.toLowerCase()] ?? mode;
}

function scenePatternName(scene: Record<string, any> | null): string {
  if (!scene) return '—';
  return scene.patternName ?? scene.pattern ?? scene.effect ?? '—';
}

function initials(name: string | null): string {
  if (!name) return '?';
  return name.split(' ').map(w => w[0]).join('').slice(0, 2).toUpperCase();
}

const { width: _SCREEN_W } = Dimensions.get('window');

// ─── Mini Visualizer ─────────────────────────────────────────────────────────

function MiniVisualizer({ scene }: { scene: Record<string, any> | null }) {
  const colors: string[] = scene?.colors ?? (scene?.color ? [scene.color] : ['#FF6B00', '#FFAA00', '#FF3300']);
  const pulseAnim = useRef(new Animated.Value(1)).current;

  useEffect(() => {
    Animated.loop(
      Animated.sequence([
        Animated.timing(pulseAnim, { toValue: 1.12, duration: 600, useNativeDriver: true }),
        Animated.timing(pulseAnim, { toValue: 1, duration: 600, useNativeDriver: true }),
      ])
    ).start();
  }, []);

  return (
    <View style={viStyles.container}>
      {colors.slice(0, 6).map((c, i) => (
        <Animated.View
          key={i}
          style={[viStyles.bar, {
            backgroundColor: c,
            height: 24 + (i % 3) * 12,
            transform: [{ scaleY: i % 2 === 0 ? pulseAnim : Animated.divide(new Animated.Value(1), pulseAnim) }],
            shadowColor: c,
            shadowOpacity: 0.9,
            shadowRadius: 8,
            elevation: 4,
          }]}
        />
      ))}
    </View>
  );
}

const viStyles = StyleSheet.create({
  container: { flexDirection: 'row', alignItems: 'flex-end', gap: Spacing.sm, height: 60, justifyContent: 'center', paddingVertical: Spacing.sm },
  bar: { width: 18, borderRadius: 4 },
});

// ─── Main Component ───────────────────────────────────────────────────────────

export default function CrewMemberDashboard({ session, role, currentScene, onLeave }: Props) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);

  const [members, setMembers] = useState<CrewMember[]>([]);
  const [elapsed, setElapsed] = useState('');
  const timerRef = useRef<ReturnType<typeof setInterval> | null>(null);

  // ── Load members ────────────────────────────────────────────────────────────
  useEffect(() => {
    const loadMembers = async () => {
      const { data } = await supabase
        .from('crew_members')
        .select(`
          user_id, role, joined_at,
          user_profiles ( display_name, avatar_color )
        `)
        .eq('session_id', session.id);

      if (data) {
        setMembers(data.map((r: any) => ({
          user_id: r.user_id,
          role: r.role ?? 'member',
          joined_at: r.joined_at,
          display_name: r.user_profiles?.display_name ?? null,
          avatar_color: r.user_profiles?.avatar_color ?? null,
        })));
      }
    };
    loadMembers();
    // Refresh every 30s (lightweight polling for member list)
    const interval = setInterval(loadMembers, 30000);
    return () => clearInterval(interval);
  }, [session.id]);

  // ── Live elapsed timer ───────────────────────────────────────────────────────
  useEffect(() => {
    const update = () => setElapsed(formatDuration(session.created_at));
    update();
    timerRef.current = setInterval(update, 1000);
    return () => { if (timerRef.current) clearInterval(timerRef.current); };
  }, [session.created_at]);

  // ── Derive color from scene ─────────────────────────────────────────────────
  const primaryColor: string = currentScene?.color ?? currentScene?.colors?.[0] ?? '#FFAA00';
  const isLeader = role === 'leader';

  return (
    <View style={styles.root}>
      {/* ── Header ── */}
      <View style={styles.header}>
        <View style={styles.statusDot} />
        <View style={{ flex: 1 }}>
          <Text style={styles.sessionName} numberOfLines={1}>{session.name}</Text>
          <Text style={styles.sessionSub}>
            {isLeader ? '👑 You are leading' : '⚡ Synced to leader'} · {elapsed}
          </Text>
        </View>
        <View style={[styles.statusBadge, { backgroundColor: 'rgba(0,230,118,0.15)', borderColor: '#00E676' }]}>
          <Text style={[styles.statusBadgeText, { color: '#00E676' }]}>● LIVE</Text>
        </View>
        <TouchableOpacity
          style={styles.shareHeaderBtn}
          onPress={() => shareSessionInvite({
            name: session.name,
            location_label: session.location_label,
            invite_code: (session as any).invite_code,
            scheduled_at: session.scheduled_at,
            status: session.status,
          })}
        >
          <MaterialCommunityIcons name="share-variant" size={18} color={Colors.primary} />
        </TouchableOpacity>
      </View>

      <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>

        {/* ── Location card ── */}
        {(session.location_label || (session as any).location_coords) && (
          <View style={styles.card}>
            <View style={styles.cardRow}>
              <MaterialCommunityIcons name="map-marker" size={18} color="#FFAA00" />
              <View style={{ flex: 1, marginLeft: Spacing.md }}>
                <Text style={styles.cardLabel}>SESSION LOCATION</Text>
                <Text style={styles.cardValue}>
                  {session.location_label ?? 'Location detected'}
                </Text>
                {(session as any).location_coords && (
                  <Text style={styles.cardSub}>
                    {(session as any).location_coords.lat?.toFixed(4)}°, {(session as any).location_coords.lng?.toFixed(4)}°
                  </Text>
                )}
              </View>
            </View>
          </View>
        )}

        {/* ── Current mode card + visualizer ── */}
        <View style={[styles.card, { borderColor: `${primaryColor}40` }]}>
          <Text style={styles.cardLabel}>CURRENT MODE</Text>
          <View style={styles.modeRow}>
            <View style={[styles.modeColorSwatch, { backgroundColor: primaryColor, shadowColor: primaryColor }]} />
            <View style={{ flex: 1, marginLeft: Spacing.md }}>
              <Text style={[styles.modeTitle, { color: primaryColor }]}>
                {sceneModeName(currentScene)}
              </Text>
              <Text style={styles.modeSub}>{scenePatternName(currentScene)}</Text>
            </View>
            {isLeader && (
              <View style={styles.leaderTag}>
                <Text style={styles.leaderTagText}>YOU CONTROL</Text>
              </View>
            )}
          </View>

          {/* Visualizer */}
          <MiniVisualizer scene={currentScene} />

          {!currentScene && (
            <Text style={[styles.cardSub, { textAlign: 'center', marginTop: Spacing.xs }]}>
              Waiting for leader to send a pattern…
            </Text>
          )}
        </View>

        {/* ── Scheduled info if applicable ── */}
        {session.scheduled_at && (
          <View style={styles.card}>
            <View style={styles.cardRow}>
              <MaterialCommunityIcons name="calendar-clock" size={18} color={Colors.primary} />
              <View style={{ marginLeft: Spacing.md }}>
                <Text style={styles.cardLabel}>STARTED</Text>
                <Text style={styles.cardValue}>{formatTime(session.created_at)}</Text>
              </View>
              <View style={{ marginLeft: Spacing.xl }}>
                <Text style={styles.cardLabel}>SCHEDULED FOR</Text>
                <Text style={styles.cardValue}>{formatTime(session.scheduled_at)}</Text>
              </View>
            </View>
          </View>
        )}

        {/* ── Stats row ── */}
        <View style={styles.statsRow}>
          <View style={styles.statCard}>
            <Text style={styles.statNum}>{members.length || '—'}</Text>
            <Text style={styles.statLabel}>Members</Text>
          </View>
          <View style={styles.statCard}>
            <Text style={styles.statNum}>{elapsed || '0s'}</Text>
            <Text style={styles.statLabel}>Elapsed</Text>
          </View>
          <View style={styles.statCard}>
            <Text style={styles.statNum}>{formatTime(session.created_at)}</Text>
            <Text style={styles.statLabel}>Started</Text>
          </View>
        </View>

        {/* ── Members list ── */}
        {members.length > 0 && (
          <View style={styles.card}>
            <Text style={styles.cardLabel}>MEMBERS ({members.length})</Text>
            {members.map(m => (
              <View key={m.user_id} style={styles.memberRow}>
                <View style={[styles.memberAvatar, { backgroundColor: m.avatar_color ?? '#FF8C00' }]}>
                  <Text style={styles.memberAvatarText}>{initials(m.display_name)}</Text>
                </View>
                <View style={{ flex: 1, marginLeft: Spacing.md }}>
                  <Text style={styles.memberName}>
                    {m.display_name ?? 'Skater'}
                    {m.role === 'leader' && ' 👑'}
                  </Text>
                  <Text style={styles.memberSub}>
                    {m.role === 'leader' ? 'Leader' : 'Member'} · joined {formatTime(m.joined_at)}
                  </Text>
                </View>
                <View style={styles.onlineDot} />
              </View>
            ))}
          </View>
        )}

        {/* ── Action buttons ── */}
        <View style={{ flexDirection: 'row', gap: Spacing.md, marginTop: Spacing.xs }}>
          <TouchableOpacity
            style={styles.shareBtn}
            onPress={() => shareSessionInvite({
              name: session.name,
              location_label: session.location_label,
              invite_code: (session as any).invite_code,
              scheduled_at: session.scheduled_at,
              status: session.status,
            })}
          >
            <MaterialCommunityIcons name="share-variant" size={16} color={Colors.primary} />
            <Text style={styles.shareBtnText}>Share Session</Text>
          </TouchableOpacity>

          <TouchableOpacity style={[styles.leaveBtn, { flex: 1 }]} onPress={onLeave}>
            <MaterialCommunityIcons name={isLeader ? 'stop-circle-outline' : 'exit-to-app'} size={16} color="#FF4444" />
            <Text style={styles.leaveBtnText}>
              {isLeader ? 'End Session' : 'Leave'}
            </Text>
          </TouchableOpacity>
        </View>

        <View style={{ height: 30 }} />
      </ScrollView>
    </View>
  );
}

// ─── Styles ───────────────────────────────────────────────────────────────────

const createStyles = (Colors: any) => StyleSheet.create({
  root: { flex: 1, backgroundColor: Colors.background ?? '#0D0D0D' },

  header: {
    flexDirection: 'row', alignItems: 'center', gap: Spacing.md,
    paddingHorizontal: Spacing.lg, paddingVertical: Spacing.lg,
    borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.06)',
    backgroundColor: 'rgba(255,255,255,0.02)',
  },
  statusDot: {
    width: 10, height: 10, borderRadius: 5,
    backgroundColor: '#00E676',
    shadowColor: '#00E676', shadowOpacity: 1, shadowRadius: 6, elevation: 4,
  },
  sessionName:   { color: Colors.text ?? '#FFF', fontSize: 16, fontWeight: '800' },
  sessionSub:    { color: Colors.textMuted ?? '#888', fontSize: 11, marginTop: 1 },
  statusBadge:   { borderRadius: 8, borderWidth: 1, paddingHorizontal: Spacing.sm, paddingVertical: Spacing.xxs },
  statusBadgeText: { fontSize: 10, fontWeight: '800', letterSpacing: 0.5 },

  body: { padding: Spacing.lg, gap: Spacing.md },

  card: {
    backgroundColor: 'rgba(255,255,255,0.04)', borderRadius: 16,
    padding: Spacing.lg, borderWidth: 1, borderColor: 'rgba(255,255,255,0.07)',
  },
  cardRow:    { flexDirection: 'row', alignItems: 'flex-start' },
  cardLabel:  { color: Colors.textMuted ?? '#888', fontSize: 10, fontWeight: '700', letterSpacing: 1.2, marginBottom: Spacing.xs },
  cardValue:  { color: Colors.text ?? '#FFF', fontSize: 15, fontWeight: '700' },
  cardSub:    { color: Colors.textMuted ?? '#888', fontSize: 11, marginTop: Spacing.xxs },

  modeRow:   { flexDirection: 'row', alignItems: 'center', marginBottom: Spacing.sm },
  modeColorSwatch: {
    width: 36, height: 36, borderRadius: 18,
    shadowOpacity: 0.8, shadowRadius: 8, elevation: 4,
  },
  modeTitle: { fontSize: 18, fontWeight: '800' },
  modeSub:   { color: Colors.textMuted ?? '#888', fontSize: 12, marginTop: Spacing.xxs },
  leaderTag: {
    backgroundColor: 'rgba(255,170,0,0.12)', borderRadius: 6,
    paddingHorizontal: Spacing.sm, paddingVertical: Spacing.xxs, borderWidth: 1, borderColor: 'rgba(255,170,0,0.25)',
  },
  leaderTagText: { fontSize: 9, fontWeight: '800', color: '#FFAA00', letterSpacing: 0.5 },

  statsRow: { flexDirection: 'row', gap: Spacing.md },
  statCard: {
    flex: 1, backgroundColor: 'rgba(255,255,255,0.04)', borderRadius: 14,
    padding: Spacing.md, alignItems: 'center', borderWidth: 1, borderColor: 'rgba(255,255,255,0.06)',
  },
  statNum:   { color: Colors.text ?? '#FFF', fontSize: 16, fontWeight: '900' },
  statLabel: { color: Colors.textMuted ?? '#888', fontSize: 10, marginTop: Spacing.xxs },

  memberRow: {
    flexDirection: 'row', alignItems: 'center',
    paddingVertical: Spacing.md, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.04)',
  },
  memberAvatar:     { width: 36, height: 36, borderRadius: 18, alignItems: 'center', justifyContent: 'center' },
  memberAvatarText: { color: '#000', fontSize: 13, fontWeight: '900' },
  memberName:  { color: Colors.text ?? '#FFF', fontSize: 13, fontWeight: '700' },
  memberSub:   { color: Colors.textMuted ?? '#888', fontSize: 11, marginTop: 1 },
  onlineDot:   { width: 8, height: 8, borderRadius: 4, backgroundColor: '#00E676', shadowColor: '#00E676', shadowOpacity: 0.9, shadowRadius: 4, elevation: 2 },

  leaveBtn: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: Spacing.sm,
    borderWidth: 1.5, borderColor: '#FF4444', borderRadius: 14,
    paddingVertical: Spacing.md,
  },
  leaveBtnText: { color: '#FF4444', fontSize: 14, fontWeight: '800' },

  shareHeaderBtn: { padding: Spacing.sm, borderRadius: 20 },
  shareBtn: {
    flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: Spacing.sm,
    borderWidth: 1.5, borderColor: 'rgba(255,170,0,0.4)', borderRadius: 14, paddingVertical: Spacing.md,
  },
  shareBtnText: { color: '#FFAA00', fontSize: 14, fontWeight: '700' },
});
