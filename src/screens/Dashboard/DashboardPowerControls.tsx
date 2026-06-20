import { useCallback } from 'react';
import { CustomGroup, DisplayDevice, IFavoriteState } from '../../types/dashboard.types';
import { useProtocolDispatch } from '../../hooks/useProtocolDispatch';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { STORAGE_FAVORITES } from '../../constants/storageKeys';
import { Alert, InteractionManager } from 'react-native';
import { startTransition } from 'react';
import { DockedControllerHandle } from '../../components/DockedController';

interface UseDashboardPowerControlsProps {
  powerStates: Record<string, boolean>;
  setPowerState: (macs: string[], state: boolean) => void;
  connectedDevices: DisplayDevice[];
  allDevices: DisplayDevice[];
  connectToDevices: (devices: DisplayDevice[]) => void;
  isSkateSessionActive: boolean;
  startSession: () => void;
  setIsControllerOpen: (isOpen: boolean) => void;
  dockedControllerRef: React.RefObject<DockedControllerHandle | null>;
  retriggerAutoConnect: () => void;
}

export function useDashboardPowerControls({
  powerStates,
  setPowerState,
  connectedDevices,
  allDevices,
  connectToDevices,
  isSkateSessionActive,
  startSession,
  setIsControllerOpen,
  dockedControllerRef,
  retriggerAutoConnect,
}: UseDashboardPowerControlsProps) {
  const dispatch = useProtocolDispatch();

  const handlePowerToggle = useCallback(async (deviceIds: string[], forceState?: boolean) => {
    const targetState = forceState !== undefined ? forceState : !(powerStates[deviceIds[0]] ?? true);
    setPowerState(deviceIds, targetState);
    const connectedMacs = deviceIds.filter(mac => connectedDevices.some(d => d.id.toUpperCase() === mac.toUpperCase()));
    for (const mac of connectedMacs) {
      await dispatch.setPower(targetState, mac);
    }
  }, [powerStates, setPowerState, dispatch, connectedDevices]);

  const handleGroupPowerPress = useCallback((group: CustomGroup) => {
    handlePowerToggle(group.deviceIds);
  }, [handlePowerToggle]);

  const handleGroupMusicPress = useCallback((group: CustomGroup) => {
    const devicesToConnect = allDevices.filter(d => group.deviceIds.includes(d.id.toUpperCase()));
    if (devicesToConnect.length > 0) {
      connectToDevices(devicesToConnect);
      if (!isSkateSessionActive) startSession();
      startTransition(() => {
        setIsControllerOpen(true);
      });
      InteractionManager.runAfterInteractions(() => {
        dockedControllerRef.current?.setActiveMode('MUSIC');
      });
    } else {
      retriggerAutoConnect();
    }
  }, [allDevices, connectToDevices, isSkateSessionActive, startSession, setIsControllerOpen, dockedControllerRef, retriggerAutoConnect]);

  const handleGroupCameraPress = useCallback((group: CustomGroup) => {
    const devicesToConnect = allDevices.filter(d => group.deviceIds.includes(d.id.toUpperCase()));
    if (devicesToConnect.length > 0) {
      connectToDevices(devicesToConnect);
      if (!isSkateSessionActive) startSession();
      startTransition(() => {
        setIsControllerOpen(true);
      });
      InteractionManager.runAfterInteractions(() => {
        dockedControllerRef.current?.setActiveMode('CAMERA');
      });
    } else {
      retriggerAutoConnect();
    }
  }, [allDevices, connectToDevices, isSkateSessionActive, startSession, setIsControllerOpen, dockedControllerRef, retriggerAutoConnect]);

  const handleGroupFavoritePress = useCallback(async (group: CustomGroup, _snapshot: unknown) => {
    let lastFav: IFavoriteState | null = null;
    try {
      const raw = await AsyncStorage.getItem(STORAGE_FAVORITES);
      if (raw) {
        const favs = JSON.parse(raw) as IFavoriteState[];
        if (Array.isArray(favs) && favs.length > 0) {
          lastFav = favs[favs.length - 1];
        }
      }
    } catch (_e: unknown) {}

    if (!lastFav) {
      Alert.alert('No Favorites', 'You haven\'t saved any favorites yet. Open the controller and tap the ❤️ to save one.');
      return;
    }

    const devicesToConnect = allDevices.filter(d => group.deviceIds.includes(d.id.toUpperCase()));
    if (devicesToConnect.length > 0) {
      connectToDevices(devicesToConnect);
      if (!isSkateSessionActive) startSession();
      startTransition(() => {
        setIsControllerOpen(true);
      });
      InteractionManager.runAfterInteractions(() => {
        dockedControllerRef.current?.loadFavorite(lastFav as IFavoriteState);
      });
    } else {
      retriggerAutoConnect();
    }
  }, [allDevices, connectToDevices, isSkateSessionActive, startSession, setIsControllerOpen, dockedControllerRef, retriggerAutoConnect]);

  return {
    handlePowerToggle,
    handleGroupPowerPress,
    handleGroupMusicPress,
    handleGroupCameraPress,
    handleGroupFavoritePress,
  };
}
