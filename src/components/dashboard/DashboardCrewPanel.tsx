import React, { useState } from 'react';
import { View, Alert } from 'react-native';
import { CrewRole, CrewSession, crewService } from '../../services/CrewService';
import CrewHubSlab from './CrewHubSlab';
import { CrewModal } from '../CrewModal';
import { useCrewProximityRadar, RadarAlert } from '../../hooks/useCrewProximityRadar';

interface DashboardCrewPanelProps {
  crewSession: CrewSession | null;
  setCrewSession: (s: CrewSession | null) => void;
  crewRole: CrewRole;
  setCrewRole: (r: CrewRole) => void;
  isCrewModalVisible: boolean;
  setIsCrewModalVisible: (v: boolean) => void;
  crewModeSummary: string | undefined;
  setCrewModeSummary: (s: string | undefined) => void;
  lastLeaderScene: Record<string, any> | null;
  setLastLeaderScene: (s: Record<string, any> | null) => void;
  initialDeepLinkCode: string | null;
  
  isOfflineMode: boolean;
  appSettings: any;
  windowHeight: number;
  Colors: any;
  styles: any;
  
  onApplyCloudScene: (scene: Record<string, any>) => void;
  crewInitialStep: any;
  setCrewInitialStep: (step: any) => void;
  isCrewHubCollapsed: boolean;
  toggleCrewHubCollapse: () => void;
}

export default React.memo(function DashboardCrewPanel({
  crewSession,
  setCrewSession,
  crewRole,
  setCrewRole,
  isCrewModalVisible,
  setIsCrewModalVisible,
  crewModeSummary,
  setCrewModeSummary,
  lastLeaderScene,
  setLastLeaderScene,
  initialDeepLinkCode,
  isOfflineMode,
  appSettings,
  windowHeight,
  Colors,
  styles,
  onApplyCloudScene,
  crewInitialStep,
  setCrewInitialStep,
  isCrewHubCollapsed,
  toggleCrewHubCollapse
}: DashboardCrewPanelProps) {
  // State lifted to DashboardScreen
  const radarAlert = useCrewProximityRadar();

  const handleRadarAction = (alert: RadarAlert) => {
    if (alert.matchType === 'PRIVATE_CREW') {
      // Direct them to the hub landing where their crew session will be visible
      setCrewInitialStep('landing');
      setIsCrewModalVisible(true);
    } else if (alert.matchType === 'PUBLIC_SESSION') {
      // Direct them to the join screen (or hub) where they can see the public session
      setCrewInitialStep('landing'); 
      setIsCrewModalVisible(true);
    } else if (alert.matchType === 'EMPTY_RINK') {
      // Prompt them to start a session for their crew
      setCrewInitialStep('create');
      setIsCrewModalVisible(true);
    }
  };

  return (
    <>
      <CrewHubSlab
        crewSession={crewSession}
        crewRole={crewRole}
        isOfflineMode={isOfflineMode}
        appSettings={appSettings}
        windowHeight={windowHeight}
        onOpenHub={() => { setCrewInitialStep('landing'); setIsCrewModalVisible(true); }}
        onOpenMap={() => { setCrewInitialStep('map'); setIsCrewModalVisible(true); }}
        isCrewHubCollapsed={isCrewHubCollapsed}
        onToggleCollapse={toggleCrewHubCollapse}
        radarAlert={radarAlert}
        onRadarAction={handleRadarAction}
        Colors={Colors}
        styles={styles}
      />

      {isCrewModalVisible && (
        <CrewModal
          visible={isCrewModalVisible}
          onClose={() => setIsCrewModalVisible(false)}
          initialStep={crewInitialStep}
          initialInviteCode={initialDeepLinkCode}
          activeSession={crewSession}
          activeRole={crewRole}
          currentModeSummary={crewModeSummary}
          lastLeaderScene={lastLeaderScene}
          onSessionReady={(session: CrewSession, role: CrewRole, lastScene: Record<string, any> | null) => {
            setCrewSession(session);
            setCrewRole(role);
            if (role === 'leader') {
              crewService.subscribeAsLeader(session.id, () => {});
            } else {
              crewService.subscribeAsMember(session.id, (scene) => {
                onApplyCloudScene(scene);
                setLastLeaderScene(scene); // track for member dashboard
              }, () => {
                // session_ended callback — leader ended the session
                setCrewSession(null);
                setCrewRole(null);
                setCrewModeSummary(undefined);
                setIsCrewModalVisible(false);
                Alert.alert('Session Ended', 'The crew leader has ended this session. Your skates will keep the current pattern.');
              });
              if (lastScene) {
                setLastLeaderScene(lastScene); // seed dashboard immediately from persisted DB scene
                setTimeout(() => onApplyCloudScene(lastScene), 300);
              }
            }
          }}
          onSessionLeft={() => {
            // Member left voluntarily — clear session and return to hub landing
            setCrewSession(null);
            setCrewRole(null);
            setCrewModeSummary(undefined);
            // Don't close modal — CrewModal will reset step to 'landing' via its useEffect
            // so the user lands back at the hub page naturally
          }}
          onSessionEnded={() => {
            // Leader ended the session — clear all session state
            setCrewSession(null);
            setCrewRole(null);
            setCrewModeSummary(undefined);
            // Don't force-close — CrewModal resets step to 'landing' via activeSession→null
          }}
        />
      )}
    </>
  );
});
