import { Spacing , ThemePalette } from '../../theme/theme';
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
import { Text, TouchableOpacity, View, Animated } from 'react-native';
import type { CrewRole, CrewSession } from '../../services/CrewService';
import type { RadarAlert } from '../../hooks/useCrewProximityRadar';
import { useOptionalCrewContext } from '../../context/CrewContext';
import { useAppConfig } from '../../context/AppConfigContext';
import { CrewLandingMap } from '../crew/CrewLandingMap';

interface CrewHubSlabProps {
  crewSession: CrewSession | null;
  crewRole: CrewRole;
  isOfflineMode: boolean;
  appSettings: Record<string, any>;
  windowHeight: number;
  onOpenHub: () => void;
  onOpenMap?: () => void;
  isCrewHubCollapsed: boolean;
  onToggleCollapse: () => void;
  radarAlert?: RadarAlert | null;
  onRadarAction?: (alert: RadarAlert) => void;
  Colors: ThemePalette;
  styles: Record<string, import('react-native').StyleProp<import('react-native').ViewStyle | import('react-native').TextStyle>>;
}

const CrewHubSlab = React.memo(({
  crewSession,
  crewRole,
  isOfflineMode,
  appSettings,
  windowHeight,
  onOpenHub,
  onOpenMap,
  isCrewHubCollapsed,
  onToggleCollapse,
  radarAlert,
  onRadarAction,
  Colors,
  styles,
}: CrewHubSlabProps) => {
  const context = useOptionalCrewContext();
  const pulseAnim = React.useRef(new Animated.Value(1)).current;
  const hub = context?.hub;
  const { isVisibilityAllowed } = useAppConfig();
  const showMap = isVisibilityAllowed('visibility_maps_tab');

  return (
  <View style={[styles.slabContainer, { marginTop: Spacing.md }]}>
    <View style={[styles.glassSlab, {
      borderColor: isOfflineMode ? 'rgba(255,255,255,0.05)' : 'rgba(255,170,0,0.2)',
      paddingVertical: isCrewHubCollapsed ? 12 : (isOfflineMode ? 16 : (windowHeight < 720 ? 16 : 40)),
    }]}>
      {/* Header row */}
      <View style={[styles.slabHeader, isOfflineMode && { marginBottom: Spacing.sm }]}>
        <TouchableOpacity style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, flex: 1 }} onPress={onToggleCollapse}>
          <MaterialCommunityIcons
            name={isCrewHubCollapsed ? 'chevron-down' : 'chevron-up'}
            size={18}
            color={isOfflineMode ? Colors.textMuted : '#FFAA00'}
          />
          <MaterialCommunityIcons
            name={isOfflineMode ? 'cloud-off-outline' : 'account-group'}
            size={18}
            color={isOfflineMode ? Colors.textMuted : '#FFAA00'}
          />
          <Text style={[styles.slabTitle, { color: isOfflineMode ? Colors.textMuted : '#FFAA00' }]}>
            CREWZ HUB
          </Text>
        </TouchableOpacity>
        {!crewSession && !isOfflineMode && (
          <TouchableOpacity onPress={onOpenHub} style={styles.slabAction}>
            <Text style={styles.slabActionText}>OPEN HUB</Text>
          </TouchableOpacity>
        )}
      </View>

      {/* Body — 4-state switch */}
      {!isCrewHubCollapsed && (
        <React.Fragment>
          {appSettings['global_crew_hub_locked'] ? (
            /* State 1: Admin-locked */
            <View style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: 'rgba(255,255,255,0.02)', padding: Spacing.md, borderRadius: 8, borderWidth: 1, borderColor: 'rgba(255,255,255,0.05)' }}>
              <MaterialCommunityIcons name="lock-outline" size={16} color={Colors.textMuted} style={{ marginRight: Spacing.sm }} />
              <Text style={[styles.slabEmptyText, { color: Colors.textMuted, flex: 1, fontSize: 11 }]}>
                FEATURE TEMPORARILY DISABLED BY ADMIN
              </Text>
            </View>
          ) : isOfflineMode ? (
            /* State 2: Offline */
            <View style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: 'rgba(255,255,255,0.02)', padding: Spacing.md, borderRadius: 8, borderWidth: 1, borderColor: 'rgba(255,255,255,0.05)' }}>
              <MaterialCommunityIcons name="cloud-off-outline" size={16} color={Colors.textMuted} style={{ marginRight: Spacing.sm }} />
              <Text style={[styles.slabEmptyText, { color: Colors.textMuted, flex: 1, fontSize: 11 }]}>
                Go online to sync lights with nearby skaters.
              </Text>
            </View>
          ) : crewSession ? (
            /* State 3: Active session */
            <TouchableOpacity
              style={[styles.activeCrewPill, { paddingVertical: Spacing.xl }]}
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
          ) : radarAlert && radarAlert.matchType !== 'NONE' ? (
            /* State 4: Radar Alert (Proximity detected) */
            <View style={{ gap: Spacing.sm }}>
              <Text style={{ color: Colors.textMuted, fontSize: 11, fontWeight: '700', letterSpacing: 0.5 }}>
                📍 {radarAlert.venueName.toUpperCase()} ({radarAlert.distanceMi < 0.1 ? 'Here now' : `${radarAlert.distanceMi.toFixed(1)}mi`})
              </Text>

              {radarAlert.matchType === 'PRIVATE_CREW' && (
                <View style={{ gap: Spacing.sm }}>
                  <Text style={[styles.slabEmptyText, { color: '#FFF' }]}>
                    🔥 Your Crew <Text style={{ color: '#FFAA00', fontWeight: 'bold' }}>[{radarAlert.crewName}]</Text> is rolling right now!
                  </Text>
                  <TouchableOpacity
                    style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'center', backgroundColor: '#FFAA00', paddingVertical: Spacing.md, borderRadius: 8, marginTop: Spacing.xs }}
                    onPress={() => onRadarAction?.(radarAlert)}
                  >
                    <MaterialCommunityIcons name="account-group" size={18} color="#000" style={{ marginRight: Spacing.sm }} />
                    <Text style={{ color: '#000', fontWeight: '800', letterSpacing: 1, fontSize: 13 }}>CHECK IN & SYNC</Text>
                  </TouchableOpacity>
                </View>
              )}

              {radarAlert.matchType === 'PUBLIC_SESSION' && (
                <View style={{ gap: Spacing.sm }}>
                  <Text style={[styles.slabEmptyText, { color: '#FFF' }]}>
                    🛼 A public session is live with {radarAlert.memberCount} skaters.
                  </Text>
                  <TouchableOpacity
                    style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'center', backgroundColor: '#00AAFF', paddingVertical: Spacing.md, borderRadius: 8, marginTop: Spacing.xs }}
                    onPress={() => onRadarAction?.(radarAlert)}
                  >
                    <MaterialCommunityIcons name="broadcast" size={18} color="#000" style={{ marginRight: Spacing.sm }} />
                    <Text style={{ color: '#000', fontWeight: '800', letterSpacing: 1, fontSize: 13 }}>JOIN PUBLIC SESSION</Text>
                  </TouchableOpacity>
                </View>
              )}

              {radarAlert.matchType === 'EMPTY_RINK' && (
                <View style={{ gap: Spacing.sm }}>
                  <Text style={styles.slabEmptyText}>
                    No active sessions here. Be the first to start one.
                  </Text>
                  <TouchableOpacity
                    style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'center', backgroundColor: 'rgba(255,170,0,0.1)', paddingVertical: Spacing.md, borderRadius: 8, borderWidth: 1, borderColor: '#FFAA00', marginTop: Spacing.xs }}
                    onPress={() => onRadarAction?.(radarAlert)}
                  >
                    <MaterialCommunityIcons name="flag-triangle" size={18} color="#FFAA00" style={{ marginRight: Spacing.sm }} />
                    <Text style={{ color: '#FFAA00', fontWeight: '800', letterSpacing: 1, fontSize: 13 }}>START CREW SESSION</Text>
                  </TouchableOpacity>
                </View>
              )}
            </View>
          ) : (
            /* State 5: Empty (Not near a rink) */
            <View style={{ gap: Spacing.lg }}>
              <Text style={styles.slabEmptyText}>No active sessions nearby. Launch a crew to sync lights.</Text>
              {showMap && (
                <View style={{ height: 120, width: '100%', borderRadius: 12, overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,170,0,0.3)', marginTop: Spacing.sm }}>
                  <CrewLandingMap
                    nearbySpots={hub?.nearbySpots || []}
                    nearbySessions={[]}
                    pulseAnim={pulseAnim}
                    handleJoinById={() => {}}
                    locationCoords={hub?.locationCoords ?? null}
                    discoverRadiusMi={hub?.discoverRadiusMi ?? 10}
                  />
                  <TouchableOpacity
                    style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0 }}
                    onPress={onOpenMap}
                    activeOpacity={0.7}
                  />
                </View>
              )}
            </View>
          )}
        </React.Fragment>
      )}
    </View>
  </View>
  );
});

export default CrewHubSlab;
