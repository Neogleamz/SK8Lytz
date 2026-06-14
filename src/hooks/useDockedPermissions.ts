import { useState, useCallback, useEffect } from 'react';
import { AppState, DeviceEventEmitter } from 'react-native';
import { checkPermission, PERMISSION_STATUS_CHANGED_EVENT, openGlobalPermissionsModal } from '../services/PermissionService';
import { AppLogger } from '../services/AppLogger';

export function useDockedPermissions(isVisibilityAllowed: (key: string) => boolean) {
  const [hiddenModes, setHiddenModes] = useState<readonly string[]>([]);

  const recheckPermissions = useCallback(async () => {
    const hidden: string[] = [];
    const hasCam = await checkPermission('CAMERA');
    const hasLoc = await checkPermission('LOCATION');
    if (!hasCam) hidden.push('CAMERA');
    
    if (!hasLoc) hidden.push('STREET');
    
    if (!isVisibilityAllowed('visibility_street_mode') && !hidden.includes('STREET')) hidden.push('STREET');
    
    setHiddenModes(hidden);
  }, [isVisibilityAllowed]);

  useEffect(() => {
    recheckPermissions();
  }, [recheckPermissions]);

  useEffect(() => {
    const sub = AppState.addEventListener('change', (state) => {
      if (state === 'active') recheckPermissions();
    });
    return () => sub.remove();
  }, [recheckPermissions]);

  useEffect(() => {
    const sub = DeviceEventEmitter.addListener(PERMISSION_STATUS_CHANGED_EVENT, () => {
      recheckPermissions();
    });
    return () => sub.remove();
  }, [recheckPermissions]);

  const requestModePermission = useCallback(async (mode: 'CAMERA' | 'STREET'): Promise<boolean> => {
    const permType = mode === 'CAMERA' ? 'CAMERA' : 'LOCATION';
    try {
      let granted = await checkPermission(permType);
      if (!granted) {
        await openGlobalPermissionsModal();
        granted = await checkPermission(permType);
      }
      return granted;
    } catch (e: unknown) {
      AppLogger.warn(`[DockedController] ${permType} permission request failed`, { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
      return false;
    }
  }, []);

  return { hiddenModes, recheckPermissions, requestModePermission };
}
