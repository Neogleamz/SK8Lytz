/**
 * ProfileModal — User profile, saved crews, and session history
 *
 * 3-tab bottom sheet:
 *   • PROFILE — display name, avatar, stats
 *   • MY CREWS — permanent saved crews, create/join/leave
 *   • HISTORY — last 20 session appearances
 */

import React, { useState, useEffect, useCallback } from 'react';
import {
  View, Text, Modal, TouchableOpacity, TextInput,
  StyleSheet, ScrollView, ActivityIndicator, Alert, Platform,
} from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../context/ThemeContext';
import {
  profileService, UserProfile, PermanentCrew, SessionHistoryItem,
} from '../services/ProfileService';

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// Props
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

type Tab = 'profile' | 'crews' | 'history';

interface ProfileModalProps {
  visible: boolean;
  onClose: () => void;
  /** Called when user taps Join next to an active crew session */
  onJoinCrewSession?: (crewId: string) => void;
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// Avatar helper
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

const AVATAR_COLORS = [
  '#FFAA00','#FF6B6B','#4ECDC4','#45B7D1','#96CEB4',
  '#DDA0DD','#98D8C8','#F7DC6F','#BB8FCE','#85C1E9',
];

function initials(name: string | null): string {
  if (!name) return '?';
  return name.split(' ').map(w => w[0]).join('').slice(0, 2).toUpperCase();
}

function formatDate(iso: string): string {
  if (!iso) return '';
  const d = new Date(iso);
  return d.toLocaleDateString(undefined, { month: 'short', day: 'numeric', year: 'numeric' });
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// Component
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

export default function ProfileModal({ visible, onClose, onJoinCrewSession }: ProfileModalProps) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);

  const [tab, setTab]               = useState<Tab>('profile');
  const [loading, setLoading]       = useState(false);
  const [profile, setProfile]       = useState<UserProfile | null>(null);
  const [crews, setCrews]           = useState<PermanentCrew[]>([]);
  const [history, setHistory]       = useState<SessionHistoryItem[]>([]);

  // Profile edit state
  const [editName, setEditName]     = useState('');
  const [savingName, setSavingName] = useState(false);

  // Crew management state
  const [crewStep, setCrewStep]     = useState<'list' | 'create' | 'join'>('list');
  const [newCrewName, setNewCrewName] = useState('');
  const [joinCode, setJoinCode]     = useState('');
  const [crewLoading, setCrewLoading] = useState(false);
  const [crewError, setCrewError]   = useState('');

  // ── Load data ────────────────────────────────────────────

  const loadData = useCallback(async () => {
    if (!visible) return;
    setLoading(true);
    try {
      const [p, c, h] = await Promise.all([
        profileService.fetchOrCreateProfile(),
        profileService.getMyCrew(),
        tab === 'history' ? profileService.getSessionHistory() : Promise.resolve([]),
      ]);
      if (p) { setProfile(p); setEditName(p.display_name ?? ''); }
      setCrews(c);
      if (tab === 'history') setHistory(h);
    } catch (e) {
      console.warn('[ProfileModal] loadData error:', e);
    } finally {
      setLoading(false);
    }
  }, [visible, tab]);

  useEffect(() => { loadData(); }, [loadData]);

  // Also load history when switching to that tab
  useEffect(() => {
    if (tab === 'history' && visible) {
      profileService.getSessionHistory().then(setHistory);
    }
  }, [tab, visible]);

  // ── Handlers ─────────────────────────────────────────────

  const handleSaveName = async () => {
    if (!editName.trim()) return;
    setSavingName(true);
    try {
      await profileService.updateProfile({ display_name: editName.trim() });
      setProfile(p => p ? { ...p, display_name: editName.trim() } : p);
    } catch (e) {
      Alert.alert('Error', 'Could not save name');
    } finally {
      setSavingName(false);
    }
  };

  const handleCreateCrew = async () => {
    if (!newCrewName.trim()) { setCrewError('Enter a crew name'); return; }
    setCrewLoading(true); setCrewError('');
    try {
      const crew = await profileService.createPermanentCrew(newCrewName.trim());
      setCrews(prev => [...prev, crew]);
      setNewCrewName('');
      setCrewStep('list');
    } catch (e: any) {
      setCrewError(e.message ?? 'Failed to create crew');
    } finally {
      setCrewLoading(false);
    }
  };

  const handleJoinCrew = async () => {
    if (joinCode.trim().length < 4) { setCrewError('Enter the 6-char invite code'); return; }
    setCrewLoading(true); setCrewError('');
    try {
      const crew = await profileService.joinPermanentCrew(joinCode.trim());
      setCrews(prev => {
        if (prev.find(c => c.id === crew.id)) return prev;
        return [...prev, crew];
      });
      setJoinCode('');
      setCrewStep('list');
    } catch (e: any) {
      setCrewError(e.message ?? 'Failed to join crew');
    } finally {
      setCrewLoading(false);
    }
  };

  const handleLeaveCrew = (crew: PermanentCrew) => {
    Alert.alert(
      `Leave "${crew.name}"?`,
      "You'll stop receiving session notifications for this crew.",
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Leave', style: 'destructive',
          onPress: async () => {
            await profileService.leavePermanentCrew(crew.id);
            setCrews(prev => prev.filter(c => c.id !== crew.id));
          },
        },
      ]
    );
  };

  // ── Render: Profile tab ───────────────────────────────────

  const renderProfile = () => (
    <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
      {/* Avatar */}
      <View style={[styles.avatarCircle, { backgroundColor: profile?.avatar_color ?? '#FF8C00' }]}>
        <Text style={styles.avatarText}>{initials(profile?.display_name ?? null)}</Text>
      </View>

      {/* Display name editor */}
      <Text style={styles.label}>DISPLAY NAME</Text>
      <View style={styles.nameRow}>
        <TextInput
          style={[styles.input, styles.nameInput]}
          value={editName}
          onChangeText={setEditName}
          placeholder="Your skater name"
          placeholderTextColor={Colors.textMuted ?? '#888'}
          maxLength={32}
          returnKeyType="done"
          onSubmitEditing={handleSaveName}
        />
        <TouchableOpacity
          style={[styles.saveBtn, savingName && { opacity: 0.5 }]}
          onPress={handleSaveName}
          disabled={savingName}
        >
          {savingName
            ? <ActivityIndicator size="small" color="#000" />
            : <Text style={styles.saveBtnText}>Save</Text>
          }
        </TouchableOpacity>
      </View>

      {/* Avatar color picker */}
      <Text style={styles.label}>AVATAR COLOR</Text>
      <View style={styles.colorRow}>
        {AVATAR_COLORS.map(color => (
          <TouchableOpacity
            key={color}
            style={[
              styles.colorSwatch,
              { backgroundColor: color },
              profile?.avatar_color === color && styles.colorSwatchActive,
            ]}
            onPress={async () => {
              await profileService.updateProfile({ avatar_color: color });
              setProfile(p => p ? { ...p, avatar_color: color } : p);
            }}
          />
        ))}
      </View>

      {/* Stats summary */}
      <View style={styles.statsRow}>
        <View style={styles.statCard}>
          <Text style={styles.statNum}>{crews.length}</Text>
          <Text style={styles.statLabel}>Crews</Text>
        </View>
        <View style={styles.statCard}>
          <Text style={styles.statNum}>{history.length || '—'}</Text>
          <Text style={styles.statLabel}>Sessions</Text>
        </View>
      </View>
    </ScrollView>
  );

  // ── Render: My Crews tab ──────────────────────────────────

  const renderCrews = () => {
    if (crewStep === 'create') return (
      <View style={styles.body}>
        <TouchableOpacity style={styles.backBtn} onPress={() => { setCrewStep('list'); setCrewError(''); }}>
          <MaterialCommunityIcons name="chevron-left" size={20} color={Colors.textMuted ?? '#888'} />
          <Text style={styles.backText}>My Crews</Text>
        </TouchableOpacity>
        <Text style={styles.titleMd}>Create a Crew</Text>
        <Text style={styles.label}>CREW NAME</Text>
        <TextInput
          style={styles.input}
          value={newCrewName}
          onChangeText={setNewCrewName}
          placeholder="e.g. Rink Riders"
          placeholderTextColor={Colors.textMuted ?? '#888'}
          maxLength={40}
          autoFocus
        />
        {!!crewError && <Text style={styles.errorText}>{crewError}</Text>}
        <TouchableOpacity
          style={[styles.primaryBtn, crewLoading && { opacity: 0.6 }]}
          onPress={handleCreateCrew}
          disabled={crewLoading}
        >
          {crewLoading
            ? <ActivityIndicator size="small" color="#000" />
            : <Text style={styles.primaryBtnText}>Create</Text>
          }
        </TouchableOpacity>
      </View>
    );

    if (crewStep === 'join') return (
      <View style={styles.body}>
        <TouchableOpacity style={styles.backBtn} onPress={() => { setCrewStep('list'); setCrewError(''); }}>
          <MaterialCommunityIcons name="chevron-left" size={20} color={Colors.textMuted ?? '#888'} />
          <Text style={styles.backText}>My Crews</Text>
        </TouchableOpacity>
        <Text style={styles.titleMd}>Join a Crew</Text>
        <Text style={styles.label}>INVITE CODE</Text>
        <TextInput
          style={[styles.input, styles.codeInput]}
          value={joinCode}
          onChangeText={t => setJoinCode(t.toUpperCase())}
          placeholder="ABC123"
          placeholderTextColor={Colors.textMuted ?? '#888'}
          maxLength={6}
          autoCapitalize="characters"
          autoFocus
        />
        {!!crewError && <Text style={styles.errorText}>{crewError}</Text>}
        <TouchableOpacity
          style={[styles.primaryBtn, crewLoading && { opacity: 0.6 }]}
          onPress={handleJoinCrew}
          disabled={crewLoading}
        >
          {crewLoading
            ? <ActivityIndicator size="small" color="#000" />
            : <Text style={styles.primaryBtnText}>Join</Text>
          }
        </TouchableOpacity>
      </View>
    );

    return (
      <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
        {crews.length === 0 ? (
          <View style={styles.emptyState}>
            <MaterialCommunityIcons name="account-group-outline" size={48} color={Colors.textMuted ?? '#555'} />
            <Text style={styles.emptyTitle}>No Saved Crews</Text>
            <Text style={styles.emptySubtitle}>
              Create a permanent crew to get notified when sessions start
            </Text>
          </View>
        ) : (
          crews.map(crew => (
            <View key={crew.id} style={styles.crewCard}>
              <View style={styles.crewCardLeft}>
                <Text style={styles.crewCardName}>{crew.name}</Text>
                <Text style={styles.crewCardCode}>
                  Code: <Text style={{ color: '#FFAA00', fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace' }}>
                    {crew.invite_code}
                  </Text>
                  {crew.is_owner && '  👑 Owner'}
                </Text>
              </View>
              <TouchableOpacity
                style={styles.leaveCrewBtn}
                onPress={() => handleLeaveCrew(crew)}
              >
                <Text style={styles.leaveCrewText}>Leave</Text>
              </TouchableOpacity>
            </View>
          ))
        )}

        {/* CTA buttons */}
        <TouchableOpacity style={styles.primaryBtn} onPress={() => { setCrewStep('create'); setCrewError(''); }}>
          <MaterialCommunityIcons name="plus" size={18} color="#000" />
          <Text style={styles.primaryBtnText}>Create a Crew</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.secondaryBtn} onPress={() => { setCrewStep('join'); setCrewError(''); }}>
          <MaterialCommunityIcons name="account-plus-outline" size={18} color={Colors.primary ?? '#FFAA00'} />
          <Text style={styles.secondaryBtnText}>Join by Code</Text>
        </TouchableOpacity>
      </ScrollView>
    );
  };

  // ── Render: History tab ───────────────────────────────────

  const renderHistory = () => (
    <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
      {history.length === 0 ? (
        <View style={styles.emptyState}>
          <MaterialCommunityIcons name="history" size={48} color={Colors.textMuted ?? '#555'} />
          <Text style={styles.emptyTitle}>No Sessions Yet</Text>
          <Text style={styles.emptySubtitle}>Your crew session history will appear here</Text>
        </View>
      ) : (
        history.map((item, i) => (
          <View key={`${item.session_id}-${i}`} style={styles.historyRow}>
            <View style={[
              styles.historyRoleDot,
              { backgroundColor: item.role === 'leader' ? '#FFAA00' : '#00AAFF' }
            ]} />
            <View style={{ flex: 1 }}>
              <Text style={styles.historySessionName}>{item.session_name}</Text>
              {item.crew_name && (
                <Text style={styles.historyCrewName}>{item.crew_name}</Text>
              )}
              <Text style={styles.historyDate}>{formatDate(item.joined_at)}</Text>
            </View>
            <View style={[
              styles.historyRoleBadge,
              { backgroundColor: item.role === 'leader' ? 'rgba(255,170,0,0.12)' : 'rgba(0,170,255,0.1)' }
            ]}>
              <Text style={[
                styles.historyRoleBadgeText,
                { color: item.role === 'leader' ? '#FFAA00' : '#00AAFF' }
              ]}>
                {item.role === 'leader' ? '👑 Leader' : '⚡ Member'}
              </Text>
            </View>
          </View>
        ))
      )}
    </ScrollView>
  );

  // ── Main render ───────────────────────────────────────────

  return (
    <Modal visible={visible} animationType="slide" transparent statusBarTranslucent>
      <View style={styles.overlay}>
        <View style={styles.sheet}>
          <TouchableOpacity style={styles.closeBtn} onPress={onClose}>
            <MaterialCommunityIcons name="close" size={22} color={Colors.textMuted ?? '#888'} />
          </TouchableOpacity>

          <Text style={styles.sheetTitle}>Profile</Text>

          {/* Tab bar */}
          <View style={styles.tabBar}>
            {(['profile', 'crews', 'history'] as Tab[]).map(t => (
              <TouchableOpacity
                key={t}
                style={[styles.tabBtn, tab === t && styles.tabBtnActive]}
                onPress={() => { setTab(t); setCrewStep('list'); }}
              >
                <Text style={[styles.tabBtnText, tab === t && styles.tabBtnTextActive]}>
                  {t === 'profile' ? '👤 Profile'
                    : t === 'crews' ? '🛼 Crews'
                    : '📋 History'}
                </Text>
              </TouchableOpacity>
            ))}
          </View>

          {loading
            ? <ActivityIndicator style={{ marginTop: 40 }} size="large" color={Colors.primary ?? '#FFAA00'} />
            : tab === 'profile' ? renderProfile()
            : tab === 'crews'   ? renderCrews()
            : renderHistory()
          }
        </View>
      </View>
    </Modal>
  );
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// Styles
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

const createStyles = (Colors: any) => StyleSheet.create({
  overlay: { flex: 1, backgroundColor: 'rgba(0,0,0,0.75)', justifyContent: 'flex-end' },
  sheet: {
    backgroundColor: Colors.background ?? '#0D0D0D',
    borderTopLeftRadius: 24, borderTopRightRadius: 24,
    maxHeight: '90%', minHeight: '70%', paddingTop: 20,
  },
  closeBtn:    { position: 'absolute', top: 16, right: 16, zIndex: 10, padding: 8 },
  sheetTitle:  { color: Colors.text ?? '#FFF', fontSize: 22, fontWeight: '800', textAlign: 'center', marginBottom: 16 },

  // Tab bar
  tabBar:         { flexDirection: 'row', marginHorizontal: 16, marginBottom: 4, gap: 8 },
  tabBtn:         { flex: 1, paddingVertical: 8, borderRadius: 10, alignItems: 'center', backgroundColor: 'rgba(255,255,255,0.05)' },
  tabBtnActive:   { backgroundColor: Colors.primary ?? '#FFAA00' },
  tabBtnText:     { color: Colors.textMuted ?? '#888', fontSize: 12, fontWeight: '700' },
  tabBtnTextActive: { color: '#000' },

  // Body
  body:           { padding: 20, paddingBottom: 40 },
  label:          { color: Colors.textMuted ?? '#888', fontSize: 11, fontWeight: '700', letterSpacing: 1, marginTop: 16, marginBottom: 6 },
  input:          {
    backgroundColor: 'rgba(255,255,255,0.07)',
    borderRadius: 12, borderWidth: 1, borderColor: 'rgba(255,255,255,0.12)',
    color: Colors.text ?? '#FFF', fontSize: 16,
    paddingHorizontal: 16, paddingVertical: 12,
    width: '100%', marginBottom: 4,
  },
  codeInput:      { fontSize: 28, fontWeight: '800', letterSpacing: 8, textAlign: 'center', fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace' },
  errorText:      { color: '#FF4444', fontSize: 13, marginTop: 6, textAlign: 'center' },

  // Avatar
  avatarCircle:   { width: 80, height: 80, borderRadius: 40, alignItems: 'center', justifyContent: 'center', alignSelf: 'center', marginBottom: 8 },
  avatarText:     { color: '#000', fontSize: 28, fontWeight: '900' },

  // Name row
  nameRow:        { flexDirection: 'row', gap: 8, alignItems: 'center' },
  nameInput:      { flex: 1, marginBottom: 0 },
  saveBtn:        { backgroundColor: Colors.primary ?? '#FFAA00', borderRadius: 10, paddingHorizontal: 16, paddingVertical: 12 },
  saveBtnText:    { color: '#000', fontWeight: '800', fontSize: 14 },

  // Color swatches
  colorRow:         { flexDirection: 'row', flexWrap: 'wrap', gap: 10, marginBottom: 8 },
  colorSwatch:      { width: 32, height: 32, borderRadius: 16 },
  colorSwatchActive: { borderWidth: 3, borderColor: '#FFF' },

  // Stats
  statsRow:       { flexDirection: 'row', gap: 12, marginTop: 20 },
  statCard:       { flex: 1, backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14, padding: 16, alignItems: 'center' },
  statNum:        { color: Colors.text ?? '#FFF', fontSize: 28, fontWeight: '900' },
  statLabel:      { color: Colors.textMuted ?? '#888', fontSize: 12, marginTop: 2 },

  // Buttons
  primaryBtn:     { flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 8, backgroundColor: Colors.primary ?? '#FFAA00', borderRadius: 14, paddingVertical: 15, width: '100%', marginTop: 20 },
  primaryBtnText: { color: '#000', fontSize: 16, fontWeight: '800' },
  secondaryBtn:   { flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 8, borderWidth: 1.5, borderColor: Colors.primary ?? '#FFAA00', borderRadius: 14, paddingVertical: 14, width: '100%', marginTop: 12 },
  secondaryBtnText: { color: Colors.primary ?? '#FFAA00', fontSize: 16, fontWeight: '700' },
  backBtn:        { flexDirection: 'row', alignItems: 'center', alignSelf: 'flex-start', marginBottom: 12, gap: 4 },
  backText:       { color: Colors.textMuted ?? '#888', fontSize: 14 },
  titleMd:        { color: Colors.text ?? '#FFF', fontSize: 22, fontWeight: '800', marginBottom: 8 },

  // Crew cards
  crewCard:       { flexDirection: 'row', alignItems: 'center', backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14, padding: 14, marginBottom: 10, borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)' },
  crewCardLeft:   { flex: 1 },
  crewCardName:   { color: Colors.text ?? '#FFF', fontSize: 16, fontWeight: '700' },
  crewCardCode:   { color: Colors.textMuted ?? '#888', fontSize: 12, marginTop: 4 },
  leaveCrewBtn:   { paddingHorizontal: 14, paddingVertical: 7, borderRadius: 20, borderWidth: 1, borderColor: '#FF4444' },
  leaveCrewText:  { color: '#FF4444', fontSize: 13, fontWeight: '700' },

  // Empty state
  emptyState:     { alignItems: 'center', paddingVertical: 32 },
  emptyTitle:     { color: Colors.text ?? '#FFF', fontSize: 18, fontWeight: '700', marginTop: 12 },
  emptySubtitle:  { color: Colors.textMuted ?? '#888', fontSize: 14, textAlign: 'center', marginTop: 6, lineHeight: 20 },

  // History rows
  historyRow:          { flexDirection: 'row', alignItems: 'center', gap: 12, paddingVertical: 12, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.05)' },
  historyRoleDot:      { width: 10, height: 10, borderRadius: 5 },
  historySessionName:  { color: Colors.text ?? '#FFF', fontSize: 15, fontWeight: '600' },
  historyCrewName:     { color: Colors.textMuted ?? '#888', fontSize: 12, marginTop: 1 },
  historyDate:         { color: Colors.textMuted ?? '#555', fontSize: 11, marginTop: 2 },
  historyRoleBadge:    { borderRadius: 20, paddingHorizontal: 10, paddingVertical: 4 },
  historyRoleBadgeText: { fontSize: 12, fontWeight: '700' },
});
