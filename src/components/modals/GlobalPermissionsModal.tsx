import React, { useEffect, useState } from 'react';
import { DeviceEventEmitter, Modal } from 'react-native';
import PermissionsOnboardingScreen from '../../screens/Onboarding/PermissionsOnboardingScreen';
import { AppLogger } from '../../services/AppLogger';

import { SHOW_GLOBAL_PERMISSIONS_EVENT, GLOBAL_PERMISSIONS_CLOSED_EVENT } from '../../services/PermissionService';

export function GlobalPermissionsModal() {
  const [visible, setVisible] = useState(false);

  useEffect(() => {
    const sub = DeviceEventEmitter.addListener(SHOW_GLOBAL_PERMISSIONS_EVENT, () => {
      AppLogger.log('GLOBAL_MODAL_MOUNTED', { type: 'permissions_onboarding' });
      setVisible(true);
    });
    return () => sub.remove();
  }, []);

  const handleComplete = () => {
    setVisible(false);
    DeviceEventEmitter.emit(GLOBAL_PERMISSIONS_CLOSED_EVENT);
  };

  return (
    <Modal visible={visible} animationType="slide" presentationStyle="pageSheet">
      <PermissionsOnboardingScreen onComplete={handleComplete} />
    </Modal>
  );
}
