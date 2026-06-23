import { ThemePalette } from '../../theme/theme';
import React from 'react';
import { Alert } from 'react-native';
import { CrewRole, CrewSession, crewService } from '../../services/CrewService';
import CrewHubSlab from './CrewHubSlab';
import { CrewModal } from '../CrewModal';
import { useCrewProximityRadar, RadarAlert } from '../../hooks/useCrewProximityRadar';

interface DashboardCrewPanelProps {
  crewSession: CrewSession | null;
  crewRole: CrewRole;
  isCrewModalVisible: boolean;
  setIsCrewModalVisible: (v: boolean) => void;
  crewModeSummary: string | undefined;
  setCrewModeSummary: (s: string | undefined) => void;
  lastLeaderScene: Record<string, unknown> | null;
  setLastLeaderScene: (s: Record<string, unknown> | null) => void;
  initialDeepLinkCode: string | null;
  
  isOfflineMode: boolean;
  appSettings: Record<string, string | boolean>;
  windowHeight: number;
  Colors: ThemePalette;
  styles: Record<string, import('react-native').StyleProp<import('react-native').ViewStyle | import('react-native').TextStyle>>;
  
  onApplyCloudScene: (scene: Record<string, unknown>) => void;
  crewInitialStep: 'landing' | 'join' | 'create' | 'map';
  setCrewInitialStep: (step: 'landing' | 'join' | 'create' | 'map') => void;
  isCrewHubCollapsed: boolean;
  toggleCrewHubCollapse: () => void;
}

export default React.memo(function DashboardCrewPanel({
  crewSession,
  crewRole,
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
  const crewUnsubRef = React.useRef<(() => void) | null>(null);
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

  React.useEffect(() => {
    return () => {
      if (crewUnsubRef.current) crewUnsubRef.current();
      crewService.unsubscribe();
    };
  }, []);

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
          onSessionReady={(session: CrewSession, role: CrewRole, lastScene: Record<string, unknown> | null) => {
            if (crewUnsubRef.current) {
              crewUnsubRef.current();
              crewUnsubRef.current = null;
            }
            if (role === 'leader') {
              crewUnsubRef.current = crewService.subscribeAsLeader(session.id, (members) => {
                crewService.emitMembers(members);
              });
            } else {
              crewUnsubRef.current = crewService.subscribeAsMember(session.id, (scene) => {
                onApplyCloudScene(scene);
                setLastLeaderScene(scene); // track for member dashboard
              }, () => {
                // session_ended callback - leader ended the session
                setCrewModeSummary(undefined);
                setIsCrewModalVisible(false);
                Alert.alert('Session Ended', 'The crew leader has ended this session. Your skates will keep the current pattern.');
              });
              if (lastScene) {
                setLastLeaderScene(lastScene); // seed dashboard immediately from persisted DB scene
                onApplyCloudScene(lastScene);
              }
            }
          }}
          onSessionLeft={() => {
            // Member left voluntarily - clear session and return to hub landing
            setCrewModeSummary(undefined);
            // Don't close modal — CrewModal will reset step to 'landing' via its useEffect
            // so the user lands back at the hub page naturally
          }}
          onSessionEnded={() => {
            // Leader ended the session - clear all session state
            setCrewModeSummary(undefined);
            // Don't force-close — CrewModal resets step to 'landing' via activeSession→null
          }}
        />
      )}
    </>
  );
});
