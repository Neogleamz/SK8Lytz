import React, { useEffect, useState } from 'react';
import { KeyboardAvoidingView, Modal, Platform, View } from 'react-native';
import { CrewProvider, useCrewContext } from '../context/CrewContext';
import { useTheme } from '../context/ThemeContext';
import { CrewRole, CrewSession } from '../services/CrewService';
import { supabase } from '../services/supabaseClient';
import { createStyles } from './crew/CrewStyles';

import { useCrewHub } from '../hooks/useCrewHub';
import { useCrewManage } from '../hooks/useCrewManage';
import { useCrewSession } from '../hooks/useCrewSession';

import { CrewControllerScreen } from './crew/CrewControllerScreen';
import { CrewCreateScreen } from './crew/CrewCreateScreen';
import { CrewDetailScreen } from './crew/CrewDetailScreen';
import { CrewJoinScreen } from './crew/CrewJoinScreen';
import { CrewLandingScreen } from './crew/CrewLandingScreen';
import { CrewManageScreen } from './crew/CrewManageScreen';
import { CrewScheduleScreen } from './crew/CrewScheduleScreen';

type ModalStep = 'landing' | 'create' | 'schedule' | 'join' | 'controller' | 'manage' | 'crew-detail' | 'map';

interface CrewModalProps {
  visible: boolean;
  onClose: () => void;
  onSessionReady: (session: CrewSession, role: CrewRole, lastScene: Record<string, any> | null) => void;
  onSessionLeft: () => void;
  onSessionEnded: () => void;
  activeSession: CrewSession | null;
  activeRole: CrewRole;
  currentModeSummary?: string;
  lastLeaderScene?: Record<string, any> | null;
  initialStep?: ModalStep;
}

function CrewModalRouter({ onClose, currentModeSummary, lastLeaderScene }: { onClose: () => void, currentModeSummary?: string, lastLeaderScene?: Record<string, any> | null }) {
  const { step } = useCrewContext();
  const { Colors } = useTheme();
  const styles = createStyles(Colors);

  const renderComponent = () => {
    switch (step) {
      case 'landing': return <CrewLandingScreen onClose={onClose} />;
      case 'create': return <CrewCreateScreen />;
      case 'schedule': return <CrewScheduleScreen />;
      case 'join': return <CrewJoinScreen />;
      case 'controller': return <CrewControllerScreen onClose={onClose} currentModeSummary={currentModeSummary} lastLeaderScene={lastLeaderScene} />;
      case 'manage': return <CrewManageScreen />;
      case 'crew-detail': return <CrewDetailScreen />;
      case 'map': return <CrewLandingScreen onClose={onClose} showOnlyMap />;
      default: return <CrewLandingScreen />;
    }
  };

  return (
    <KeyboardAvoidingView 
      style={styles.overlay} 
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}
    >
      <View style={styles.sheet}>
        {renderComponent()}
      </View>
    </KeyboardAvoidingView>
  );
}

export function CrewModal({
  visible, onClose, onSessionReady, onSessionLeft, onSessionEnded,
  activeSession, activeRole, currentModeSummary, lastLeaderScene, initialStep
}: CrewModalProps) {

  const [step, setStep] = useState<ModalStep>(activeSession ? 'controller' : (initialStep || 'landing'));
  const [showCodeEntry, setShowCodeEntry] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');
  const [confirmAction, setConfirmAction] = useState<'end' | 'leave' | null>(null);

  const [currentUserId, setCurrentUserId] = useState('');
  const [displayName, setDisplayName] = useState('');

  const [crewName, setCrewName] = useState('');
  const [selectedCrewId, setSelectedCrewId] = useState<string | null>(null);
  const [isPublic, setIsPublic] = useState(false);
  const [schedDateTime, setSchedDateTime] = useState(new Date());
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [showTimePicker, setShowTimePicker] = useState(false);
  const [joiningCrewId, setJoiningCrewId] = useState<string | null>(null);

  useEffect(() => {
    supabase.auth.getUser().then((res: any) => {
      if (res?.data?.user) {
        setCurrentUserId(res.data.user.id);
        setDisplayName(res.data.user.user_metadata?.display_name || '?');
      }
    });
  }, []);

  useEffect(() => {
    if (visible && activeSession) setStep('controller');
    else if (visible && initialStep) setStep(initialStep);
  }, [visible, activeSession, initialStep]);

  const hub = useCrewHub(visible, step);
  const manage = useCrewManage(hub.myCrews);
  const session = useCrewSession(
    activeSession, activeRole, currentUserId,
    onSessionReady, onSessionLeft, onSessionEnded,
    hub.refreshNearby, () => setStep('landing'), setErrorMsg
  );

  const contextValue = {
    step, setStep,
    showCodeEntry, setShowCodeEntry,
    isLoading, setIsLoading,
    errorMsg, setErrorMsg,
    confirmAction, setConfirmAction,
    currentUserId,
    displayName, setDisplayName,
    formState: {
      crewName, setCrewName,
      selectedCrewId, setSelectedCrewId,
      isPublic, setIsPublic,
      schedDateTime, setSchedDateTime,
      showDatePicker, setShowDatePicker,
      showTimePicker, setShowTimePicker,
      joiningCrewId, setJoiningCrewId
    },
    hub,
    manage,
    session
  };

  return (
    <Modal visible={visible} animationType="slide" transparent>
      <CrewProvider value={contextValue}>
        <CrewModalRouter 
          onClose={onClose} 
          currentModeSummary={currentModeSummary} 
          lastLeaderScene={lastLeaderScene} 
        />
      </CrewProvider>
    </Modal>
  );
}
