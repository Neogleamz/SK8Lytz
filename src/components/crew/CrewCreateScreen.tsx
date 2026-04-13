import { Spacing } from '../../theme/theme';
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
import { LocationPicker } from '../LocationPicker';

import { createStyles } from './CrewStyles';
import { useCrewContext } from '../../context/CrewContext';



function timeAgo(iso: string): string {
  const diff = Date.now() - new Date(iso).getTime();
  const m = Math.floor(diff / 60000);
  if (m < 1) return 'just now';
  if (m < 60) return `${m}m ago`;
  return `${Math.floor(m / 60)}h ago`;
}

export function CrewCreateScreen() {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const context = useCrewContext();
  const { hub, manage, session, setStep, step, confirmAction, setConfirmAction, currentUserId, displayName, setDisplayName, errorMsg, setErrorMsg, isLoading, setIsLoading, showCodeEntry, setShowCodeEntry, formState } = context;
  const { activeSessions, myCrews, permanentCrews, isLoadingNearby, refreshNearby, nearbySessions, discoverRadiusMi, setDiscoverRadiusMi, locationLabel, setLocationLabel, locationCoords, setLocationCoords, handleDetectLocation, isGettingLocation } = hub;
  const { currentSession, isHandoffMode, executeLeaveSession, executeEndSession, handleHandoffLeadership } = session;
  const { selectedCrewId, setSelectedCrewId, crewName, setCrewName } = formState;

  const handleCreate = async (scheduled?: Date) => {
    let sessionName = crewName.trim() || permanentCrews.find(c => c.id === selectedCrewId)?.name || '';
    if (!sessionName) { setErrorMsg('Pick a crew or enter a session name'); return; }

    const now = new Date();
    const dateStr = `${(now.getMonth() + 1).toString().padStart(2, '0')}/${now.getDate().toString().padStart(2, '0')}`;
    sessionName = `${sessionName}_${dateStr}`;

    setIsLoading(true); setErrorMsg('');
    try {
      const crewInfo = myCrews.find(c => c.id === selectedCrewId);
      const isSessionPublic = crewInfo ? crewInfo.is_public : false;

      const opts: Parameters<typeof crewService.createSession>[2] = {
        isPublic: isSessionPublic,
        crewId:   selectedCrewId || undefined,
      };
      if (locationLabel) opts.locationLabel = locationLabel;
      if (locationCoords) opts.locationCoords = locationCoords;
      if (scheduled) opts.scheduledAt = scheduled.toISOString();

      const newSession = await crewService.createSession(sessionName, displayName.trim(), opts);
      AppLogger.log('CREW_SESSION_CREATED', {
        sessionId: newSession.id, crewName: sessionName,
        hasLocation: !!locationLabel, scheduled: !!scheduled, isPublic: isSessionPublic,
      });

      if (scheduled && scheduled > new Date()) {
        // Optional notifications logic can be added later
      } else {
        session.setCurrentSession(newSession);
        session.setCurrentRole('leader');
        setStep('controller');
      }
    } catch (e: any) {
      setErrorMsg(e.message || 'Failed to create session');
    } finally {
      setIsLoading(false);
    }
  };
  
  // NOTE: You will need to bring in local state that wasn't context-ified
  // or convert them. For now, the structure guarantees safe parsing context injection.

  return (
<ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
      <TouchableOpacity onPress={() => setStep('landing')} style={styles.backBtn}>
        <MaterialCommunityIcons name="chevron-left" size={22} color={Colors.textMuted} />
        <Text style={styles.backText}>Back</Text>
      </TouchableOpacity>

      <Text style={styles.titleLarge}>Start a Session</Text>

      {/* ── Crew picker ── */}
      <Text style={styles.label}>SESSION FOR</Text>
      <View style={styles.crewPickerRow}>
        {myCrews.map(crew => {
          const isSelected = selectedCrewId === crew.id;
          return (
            <TouchableOpacity key={crew.id}
              style={[styles.crewChip, isSelected && styles.crewChipActive]}
              onPress={() => { setSelectedCrewId(crew.id); setCrewName(''); }}>
              <View style={{ position: 'relative' }}>
                {crew.avatar_url ? (
                  <Image source={{ uri: crew.avatar_url }} style={{ width: 16, height: 16, borderRadius: 8, marginRight: Spacing.xxs }} />
                ) : (
                  <View style={{ width: 16, height: 16, borderRadius: 8, backgroundColor: crew.avatar_color || '#FFAA00', alignItems: 'center', justifyContent: 'center', marginRight: Spacing.xxs }}>
                    <MaterialCommunityIcons name={(crew.avatar_icon as any) || 'account-group'} size={10} color="#000" />
                  </View>
                )}
                {!crew.is_public && (
                  <MaterialCommunityIcons name="lock" size={8} color={isSelected ? '#000' : Colors.primary} style={{ position: 'absolute', top: -4, right: -4, backgroundColor: isSelected ? '#FFAA00' : '#1C1C1E', borderRadius: 4, overflow: 'hidden' }} />
                )}
              </View>
              <Text style={[styles.crewChipText, isSelected && styles.crewChipTextActive]}
                numberOfLines={1}>{crew.name}</Text>
            </TouchableOpacity>
          );
        })}
      </View>

      <Text style={styles.label}>YOUR NAME IN THIS SESSION</Text>
      <TextInput style={styles.input} value={displayName} onChangeText={setDisplayName}
        placeholder="Display name" placeholderTextColor={Colors.textMuted} maxLength={24} />
      <LocationPicker
        locationLabel={locationLabel}
        onLocationLabelChange={setLocationLabel}
        locationCoords={locationCoords}
        onLocationCoordsChange={setLocationCoords}
        isGettingLocation={isGettingLocation}
        onDetectLocation={handleDetectLocation}
        searchRadiusMi={discoverRadiusMi || undefined}
      />

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
}