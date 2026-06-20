import React, { useCallback } from 'react';
import { View } from 'react-native';
import DeviceItem from '../../components/DeviceItem';
import { RegisteredDevice } from '../../hooks/useRegistration';
import { DisplayDevice, DeviceSettings, DevicePatternState, DeviceConnectionState } from '../../types/dashboard.types';
import { normalizeMac } from '../../hooks/useDeviceStateLedger';
import { Layout } from '../../theme/theme';

export interface MemoizedDeviceItemProps {
  device: DisplayDevice;
  isConnected: boolean;
  isSelectionMode: boolean;
  isSelected: boolean;
  ledgerState?: DevicePatternState;
  connectionState?: DeviceConnectionState;
  isPoweredOn: boolean;
  onPress: (mac: string) => void;
  onLongPress: (device: DisplayDevice) => void;
  onPowerToggle: (mac: string) => void;
}

export const MemoizedDeviceItem = React.memo(({
  device,
  isConnected,
  isSelectionMode,
  isSelected,
  ledgerState,
  connectionState,
  isPoweredOn,
  onPress,
  onLongPress,
  onPowerToggle,
}: MemoizedDeviceItemProps) => {
  const handlePress = useCallback(() => {
    onPress(device.id);
  }, [onPress, device.id]);

  const handleLongPress = useCallback(() => {
    onLongPress(device);
  }, [onLongPress, device]);

  const handlePowerToggle = useCallback(() => {
    onPowerToggle(device.id);
  }, [onPowerToggle, device.id]);

  return (
    <DeviceItem
      device={device}
      isConnected={isConnected}
      isSelectionMode={isSelectionMode}
      isSelected={isSelected}
      ledgerState={ledgerState}
      onPress={handlePress}
      onLongPress={handleLongPress}
      showGroupIcon={false}
      isPoweredOn={isPoweredOn}
      onPowerToggle={handlePowerToggle}
      connectionState={connectionState}
    />
  );
});

export function useDashboardDeviceList(props: {
  displayConnectedDevices: DisplayDevice[];
  isSelectionMode: boolean;
  selectedIds: string[];
  powerStates: Record<string, boolean>;
  deviceConfigs: Record<string, DeviceSettings>;
  ledgerLoadSync: (mac: string) => DevicePatternState | null;
  rssiMap: Record<string, number>;
  connectionStates: Record<string, DeviceConnectionState>;
  handleDeviceItemPress: (mac: string) => void;
  openSettings: (device: DisplayDevice) => void;
  handleDeviceItemPowerToggle: (mac: string) => void;
}) {
  const renderItem = useCallback(({ item }: { item: RegisteredDevice }) => {
    const mac = (item.device_mac || item.id || '').toUpperCase();
    const cachedConfig = (props.deviceConfigs[mac] || {}) as Partial<DeviceSettings>;
    const mergedItem = {
      ...item,
      ...cachedConfig,
      id: mac,
      name: (item.device_name || cachedConfig.name) as string | null,
      rssi: props.rssiMap[mac] ?? item.rssi_at_register ?? null,
    };
    const ledgerState = props.ledgerLoadSync(normalizeMac(mac));

    return (
      <View style={{ paddingHorizontal: Layout.padding }}>
        <MemoizedDeviceItem
          device={mergedItem as unknown as DisplayDevice}
          isConnected={props.displayConnectedDevices.some(d => d.id.toUpperCase() === mac)}
          isSelectionMode={props.isSelectionMode}
          isSelected={props.selectedIds.includes(mac)}
          ledgerState={ledgerState ?? undefined}
          isPoweredOn={props.powerStates[mac] ?? true}
          connectionState={props.connectionStates[mac]}
          onPress={props.handleDeviceItemPress}
          onLongPress={props.openSettings}
          onPowerToggle={props.handleDeviceItemPowerToggle}
        />
      </View>
    );
  }, [props]);

  return { renderItem };
}
