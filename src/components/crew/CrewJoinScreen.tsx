import React, { useRef, useEffect } from 'react';
import { View, Text, TouchableOpacity, ScrollView, Animated, ActivityIndicator, Alert, Share, TextInput, Image, RefreshControl } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../../context/ThemeContext';
import * as Clipboard from 'expo-clipboard';
import { profileService, PermanentCrew } from '../../services/ProfileService';
import { crewService, CrewSession } from '../../services/CrewService';
import { locationService } from '../../services/LocationService';
import { AppLogger } from '../../services/AppLogger';
import DateTimePicker from '@react-native-community/datetimepicker';
import * as ImagePicker from 'expo-image-picker';

import { createStyles } from './CrewStyles';
import { useCrewContext } from '../../context/CrewContext';
import { profileService } from '../../services/profileService';
// TODO: any other specific imports check manually
import { Picker } from '@react-native-picker/picker';

const styles = createStyles(Colors);


function timeAgo(iso: string): string {
  const diff = Date.now() - new Date(iso).getTime();
  const m = Math.floor(diff / 60000);
  if (m < 1) return 'just now';
  if (m < 60) return `${m}m ago`;
  return `${Math.floor(m / 60)}h ago`;
}

export function CrewJoinScreen() {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const context = useCrewContext();
  const { hub, manage, session, setStep, step, confirmAction, setConfirmAction, currentUserId, displayName, errorMsg, setErrorMsg, isLoading, setIsLoading, showCodeEntry, setShowCodeEntry, formState } = context;
  const { activeSessions, myCrews, permanentCrews, isLoadingNearby, refreshNearby, nearbySessions, discoverRadiusMi, setDiscoverRadiusMi, locationLabel, handleDetectLocation, isGettingLocation } = hub;
  const { selectedCrewDetail, setSelectedCrewDetail, expandedCrewId, setExpandedCrewId, cardMembers, setCardMembers, loadingCardMembersFor, makingOwnerFor, setMakingOwnerFor, confirmingDeleteCrewId, setConfirmingDeleteCrewId, confirmingLeaveCrewId, setConfirmingLeaveCrewId, createCrewError, setCreateCrewError, isCreatingCrew, newCrewName, setNewCrewName, newCrewDesc, newCrewIsPublic, setNewCrewIsPublic, newCrewCity, setNewCrewCity, newCrewState, setNewCrewState } = manage;
  const { currentSession, isHandoffMode, executeLeaveSession, executeEndSession, handleHandoffLeadership } = session;
  
  // NOTE: You will need to bring in local state that wasn't context-ified
  // or convert them. For now, the structure guarantees safe parsing context injection.

  return (
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
}