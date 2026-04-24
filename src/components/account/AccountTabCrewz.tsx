import React from 'react';
import { View, Text, ScrollView, TouchableOpacity, TextInput, ActivityIndicator, Platform } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Spacing } from '../../theme/theme';

export default function AccountTabCrewz({
  Colors,
  styles,
  crewStep,
  setCrewStep,
  crewError,
  setCrewError,
  newCrewName,
  setNewCrewName,
  crewLoading,
  handleCreateCrew,
  joinCode,
  setJoinCode,
  handleJoinCrew,
  crews,
  handleDeleteCrew,
  handleLeaveCrew,
}: any) {
  if (crewStep === 'create') return (
    <View style={styles.body}>
      <TouchableOpacity style={styles.backBtn} onPress={() => { setCrewStep('list'); setCrewError(''); }}>
        <MaterialCommunityIcons name="chevron-left" size={20} color={Colors.textMuted} />
        <Text style={styles.backText}>My CREWZ</Text>
      </TouchableOpacity>
      <Text style={styles.sectionHeader}>CREATE A CREW</Text>
      <Text style={styles.label}>CREW NAME</Text>
      <TextInput style={styles.input} value={newCrewName} onChangeText={setNewCrewName}
        placeholder="e.g. Rink Riders" placeholderTextColor={Colors.textMuted}
        maxLength={40} autoFocus />
      {!!crewError && <Text style={styles.errorText}>{crewError}</Text>}
      <TouchableOpacity style={[styles.primaryBtn, crewLoading && { opacity: 0.6 }]}
        onPress={handleCreateCrew} disabled={crewLoading}>
        {crewLoading ? <ActivityIndicator color="#000" /> : <Text style={styles.primaryBtnText}>Create CREW</Text>}
      </TouchableOpacity>
    </View>
  );

  if (crewStep === 'join') return (
    <View style={styles.body}>
      <TouchableOpacity style={styles.backBtn} onPress={() => { setCrewStep('list'); setCrewError(''); }}>
        <MaterialCommunityIcons name="chevron-left" size={20} color={Colors.textMuted} />
        <Text style={styles.backText}>My CREWZ</Text>
      </TouchableOpacity>
      <Text style={styles.sectionHeader}>JOIN PRIVATE CREW</Text>
      <Text style={{ color: Colors.textMuted, fontSize: 12, marginBottom: Spacing.md, lineHeight: 17 }}>
        Enter the 6-character invite code from a private CREW. Public CREWZ don't need a code — browse them from the CREWZ HUB.
      </Text>
      <Text style={styles.label}>PRIVATE INVITE CODE</Text>
      <TextInput style={[styles.input, styles.codeInput]}
        value={joinCode} onChangeText={t => setJoinCode(t.toUpperCase())}
        placeholder="ABC123" placeholderTextColor={Colors.textMuted}
        maxLength={6} autoCapitalize="characters" autoFocus />
      {!!crewError && <Text style={styles.errorText}>{crewError}</Text>}
      <TouchableOpacity style={[styles.primaryBtn, crewLoading && { opacity: 0.6 }]}
        onPress={handleJoinCrew} disabled={crewLoading}>
        {crewLoading ? <ActivityIndicator color="#000" /> : <Text style={styles.primaryBtnText}>Join Private CREW</Text>}
      </TouchableOpacity>
    </View>
  );

  return (
    <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
      {crews.length === 0 ? (
        <View style={styles.emptyState}>
          <MaterialCommunityIcons name="account-group-outline" size={52} color={Colors.textMuted} />
          <Text style={styles.emptyTitle}>No Saved CREWZ</Text>
          <Text style={styles.emptySubtitle}>Create or join a permanent crew to get notified when sessions start.</Text>
        </View>
      ) : (
        crews.map((crew: any) => (
          <View key={crew.id} style={styles.crewCard}>
            <View style={styles.crewCardIcon}>
              <MaterialCommunityIcons
                name={crew.is_owner ? 'crown' : 'account-group'}
                size={20} color={crew.is_owner ? '#FFD700' : Colors.primary} />
            </View>
            <View style={{ flex: 1, marginLeft: Spacing.md }}>
              <Text style={styles.crewCardName}>{crew.name}</Text>
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, marginTop: Spacing.xxs }}>
                {crew.is_public ? (
                  // Public crew — no code needed, show badge instead
                  <Text style={[styles.crewCardCode, { color: '#00C853' }]}>🌍 Public</Text>
                ) : (
                  // Private crew — show invite code (owner shares it to invite others)
                  <Text style={styles.crewCardCode}>
                    Code: <Text style={{ color: '#FFAA00', fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace' }}>{crew.invite_code}</Text>
                  </Text>
                )}
                {crew.is_owner && <Text style={styles.ownerBadge}>OWNER</Text>}
              </View>
            </View>
            <TouchableOpacity
              style={styles.crewActionBtn}
              onPress={() => crew.is_owner ? handleDeleteCrew(crew) : handleLeaveCrew(crew)}>
              <Text style={[styles.crewActionText, crew.is_owner && { color: '#FF4444' }]}>
                {crew.is_owner ? 'Delete' : 'Leave'}
              </Text>
            </TouchableOpacity>
          </View>
        ))
      )}

      <TouchableOpacity style={styles.primaryBtn} onPress={() => { setCrewStep('create'); setCrewError(''); }}>
        <MaterialCommunityIcons name="plus" size={18} color="#000" />
        <Text style={styles.primaryBtnText}>Create a CREW</Text>
      </TouchableOpacity>
      <TouchableOpacity style={styles.secondaryBtn} onPress={() => { setCrewStep('join'); setCrewError(''); }}>
        <MaterialCommunityIcons name="lock-outline" size={18} color={Colors.textMuted} />
        <Text style={[styles.secondaryBtnText, { color: Colors.textMuted }]}>Join Private CREW (by Code)</Text>
      </TouchableOpacity>
      <View style={{ height: 20 }} />
    </ScrollView>
  );
}
