import { MaterialCommunityIcons } from '@expo/vector-icons';
import DateTimePicker from '@react-native-community/datetimepicker';
import React from 'react';
import { ActivityIndicator, Image, ScrollView, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { useTheme } from '../../context/ThemeContext';
import { AppLogger } from '../../services/AppLogger';
import { crewService } from '../../services/CrewService';
import { Spacing } from '../../theme/theme';
import { LocationPicker } from '../LocationPicker';

import { Platform } from 'react-native';
import { useCrewContext } from '../../context/CrewContext';
import { createStyles } from './CrewStyles';

export function CrewScheduleScreen() {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const context = useCrewContext();
  const { hub, manage, session, setStep, step, confirmAction, setConfirmAction, currentUserId, displayName, setDisplayName, errorMsg, setErrorMsg, isLoading, setIsLoading, showCodeEntry, setShowCodeEntry, formState } = context;
  const { activeSessions, myCrews, permanentCrews, isLoadingNearby, refreshNearby, nearbySessions, discoverRadiusMi, setDiscoverRadiusMi, locationLabel, setLocationLabel, locationCoords, setLocationCoords, handleDetectLocation, isGettingLocation } = hub;
  const { currentSession, isHandoffMode, executeLeaveSession, executeEndSession, handleHandoffLeadership } = session;
  const { selectedCrewId, setSelectedCrewId, crewName, setCrewName, schedDateTime, setSchedDateTime, showDatePicker, setShowDatePicker, showTimePicker, setShowTimePicker } = formState;

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
      AppLogger.log('CREW_SESSION_SCHEDULED', {
        sessionId: newSession.id, crewName: sessionName,
        hasLocation: !!locationLabel, scheduled: !!scheduled, isPublic: isSessionPublic,
      });

      if (scheduled && scheduled > new Date()) {
        // Optional notifications logic can be added later
        setStep('landing');
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

      <Text style={styles.titleLarge}>Schedule a Session</Text>
      <Text style={[styles.subtitle, { marginBottom: Spacing.lg }]}>
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

      <Text style={[styles.label, { marginTop: Spacing.lg }]}>DATE &amp; TIME</Text>
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
}