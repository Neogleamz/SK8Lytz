import React, { createContext, useContext, ReactNode } from 'react';

import { useCrewHub } from '../hooks/useCrewHub';
import { useCrewManage } from '../hooks/useCrewManage';
import { useCrewSession } from '../hooks/useCrewSession';

type ModalStep = 'landing' | 'create' | 'schedule' | 'join' | 'controller' | 'manage' | 'crew-detail';

interface CrewContextValue {
  step: ModalStep;
  setStep: (step: ModalStep) => void;
  showCodeEntry: boolean;
  setShowCodeEntry: (show: boolean) => void;
  isLoading: boolean;
  setIsLoading: (loading: boolean) => void;
  errorMsg: string;
  setErrorMsg: (msg: string) => void;
  confirmAction: 'end' | 'leave' | null;
  setConfirmAction: (action: 'end' | 'leave' | null) => void;

  currentUserId: string;
  displayName: string;
  setDisplayName: (name: string) => void;

  formState: {
    crewName: string;
    setCrewName: (name: string) => void;
    selectedCrewId: string | null;
    setSelectedCrewId: (id: string | null) => void;
    isPublic: boolean;
    setIsPublic: (isPub: boolean) => void;
    schedDateTime: Date;
    setSchedDateTime: (date: Date) => void;
    showDatePicker: boolean;
    setShowDatePicker: (show: boolean) => void;
    showTimePicker: boolean;
    setShowTimePicker: (show: boolean) => void;
    joiningCrewId: string | null;
    setJoiningCrewId: (id: string | null) => void;
  };

  hub: ReturnType<typeof useCrewHub>;
  manage: ReturnType<typeof useCrewManage>;
  session: ReturnType<typeof useCrewSession>;
}

const CrewContext = createContext<CrewContextValue | undefined>(undefined);

interface CrewProviderProps {
  children: ReactNode;
  value: CrewContextValue;
}

export function CrewProvider({ children, value }: CrewProviderProps) {
  return (
    <CrewContext.Provider value={value}>
      {children}
    </CrewContext.Provider>
  );
}

export function useCrewContext() {
  const context = useContext(CrewContext);
  if (context === undefined) {
    throw new Error('useCrewContext must be used within a CrewProvider');
  }
  return context;
}
