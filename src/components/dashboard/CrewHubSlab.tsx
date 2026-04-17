/**
 * CrewHubSlab.tsx — SLAB 2: Crew Hub 4-State UI
 *
 * Renders the Crew Hub section of the disconnected dashboard view. Handles
 * all four display states: admin-locked, offline, active-session, and empty.
 *
 * Extracted from DashboardScreen.tsx (chore/refactor-dashboard-monolith).
 */
import { MaterialCommunityIcons } from '@expo/vector-icons';
import React from 'react';
import { Alert, Text, TouchableOpacity, View } from 'react-native';
import type { CrewRole, CrewSession } from '../../services/CrewService';

interface CrewHubSlabProps {
  crewSession: CrewSession | null;
  crewRole: CrewRole;
  isOfflineMode: boolean;
  appSettings: Record<string, any>;
  windowHeight: number;
  onOpenHub: () => void;
  Colors: any;
  styles: any;
}

const CrewHubSlab = React.memo(({
  crewSession,
  crewRole,
  isOfflineMode,
  appSettings,
  windowHeight,
  onOpenHub,
  Colors,
  styles,
}: CrewHubSlabProps) => (
  <View style={[styles.slabContainer, { marginTop: 12 }]}>
    <View style={[styles.glassSlab, {
      borderColor: isOfflineMode ? 'rgba(255,255,255,0.05)' : 'rgba(255,170,0,0.2)',
      paddingVertical: isOfflineMode ? 16 : (windowHeight < 720 ? 16 : 40),
    }]}>
      {/* Header row */}
      <View style={[styles.slabHeader, isOfflineMode && { marginBottom: 8 }]}>
        <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
          <MaterialCommunityIcons
            name={isOfflineMode ? 'cloud-off-outline' : 'account-group'}
            size={18}
            color={isOfflineMode ? Colors.textMuted : '#FFAA00'}
          />
          <Text style={[styles.slabTitle, { color: isOfflineMode ? Colors.textMuted : '#FFAA00' }]}>
            CREW HUB
          </Text>
        </View>
        {!crewSession && !isOfflineMode && (
          <TouchableOpacity onPress={onOpenHub} style={styles.slabAction}>
            <Text style={styles.slabActionText}>OPEN HUB</Text>
          </TouchableOpacity>
        )}
      </View>

      {/* Body — 4-state switch */}
      {appSettings['global_crew_hub_locked'] ? (
        /* State 1: Admin-locked */
        <View style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: 'rgba(255,255,255,0.02)', padding: 12, borderRadius: 8, borderWidth: 1, borderColor: 'rgba(255,255,255,0.05)' }}>
          <MaterialCommunityIcons name="lock-outline" size={16} color={Colors.textMuted} style={{ marginRight: 8 }} />
          <Text style={[styles.slabEmptyText, { color: Colors.textMuted, flex: 1, fontSize: 11 }]}>
            FEATURE TEMPORARILY DISABLED BY ADMIN
          </Text>
        </View>
      ) : isOfflineMode ? (
        /* State 2: Offline */
        <View style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: 'rgba(255,255,255,0.02)', padding: 12, borderRadius: 8, borderWidth: 1, borderColor: 'rgba(255,255,255,0.05)' }}>
          <MaterialCommunityIcons name="cloud-off-outline" size={16} color={Colors.textMuted} style={{ marginRight: 8 }} />
          <Text style={[styles.slabEmptyText, { color: Colors.textMuted, flex: 1, fontSize: 11 }]}>
            Go online to sync lights with nearby skaters.
          </Text>
        </View>
      ) : crewSession ? (
        /* State 3: Active session */
        <TouchableOpacity
          style={[styles.activeCrewPill, { paddingVertical: 24 }]}
          onPress={onOpenHub}
        >
          <View style={[styles.statusDot, { backgroundColor: crewRole === 'leader' ? '#FFAA00' : '#00AAFF' }]} />
          <Text style={[styles.activeCrewText, { color: crewRole === 'leader' ? '#FFAA00' : '#00AAFF' }]}>
            {crewSession.name.toUpperCase()} 🔴 LIVE
          </Text>
          <MaterialCommunityIcons
            name="chevron-right"
            size={16}
            color={crewRole === 'leader' ? '#FFAA00' : '#00AAFF'}
          />
        </TouchableOpacity>
      ) : (
        /* State 4: Empty — no sessions */
        <View style={{ gap: 16 }}>
          <Text style={styles.slabEmptyText}>No active sessions nearby. Launch a crew to sync lights.</Text>
          <TouchableOpacity
            style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'center', backgroundColor: 'rgba(255,170,0,0.1)', paddingVertical: 12, borderRadius: 8, borderWidth: 1, borderColor: '#FFAA00', marginTop: 8 }}
            onPress={() => Alert.alert('Coming Soon', 'The Interactive Skate Spot Map is currently in development!')}
          >
            <MaterialCommunityIcons name="map-marker-radius" size={18} color="#FFAA00" style={{ marginRight: 8 }} />
            <Text style={{ color: '#FFAA00', fontWeight: '800', letterSpacing: 1, fontSize: 13 }}>EXPLORE SKATE MAP</Text>
          </TouchableOpacity>
        </View>
      )}
    </View>
  </View>
));

export default CrewHubSlab;
