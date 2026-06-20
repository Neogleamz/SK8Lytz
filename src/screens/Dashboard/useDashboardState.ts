import { useState } from 'react';
import { DashboardViewState } from '../../types/dashboard.types';

export type DiagnosticFsmState = 'IDLE' | 'TEST_MODE' | 'DIAGNOSTICS';

export function useDashboardState() {
  const [viewState, setViewState] = useState<DashboardViewState>('LOADING_REGS');
  const [isRefreshing, setIsRefreshing] = useState(false);
  const [diagnosticState, setDiagnosticState] = useState<DiagnosticFsmState>('IDLE');
  const [isControllerOpen, setIsControllerOpen] = useState(false);

  return {
    viewState, setViewState,
    isRefreshing, setIsRefreshing,
    diagnosticState, setDiagnosticState,
    isTestModeActive: diagnosticState === 'TEST_MODE',
    isDiagnosticsMode: diagnosticState === 'DIAGNOSTICS',
    isControllerOpen, setIsControllerOpen,
  };
}
