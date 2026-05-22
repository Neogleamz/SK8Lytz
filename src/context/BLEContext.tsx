import React, { createContext, useContext, useMemo } from 'react';
import useBLE, { BluetoothLowEnergyApi } from '../hooks/useBLE';
import { useRegistration } from '../hooks/useRegistration';

export const BLEContext = createContext<BluetoothLowEnergyApi | null>(null);

export const BLEProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { registeredDevices } = useRegistration();
  const registeredMacs = useMemo(
    () => registeredDevices.map(d => d.device_mac || ''),
    [registeredDevices]
  );
  const ble = useBLE(registeredMacs);

  return <BLEContext.Provider value={ble}>{children}</BLEContext.Provider>;
};

export function useSharedBLE(): BluetoothLowEnergyApi {
  const context = useContext(BLEContext);
  if (!context) {
    throw new Error('useSharedBLE must be used within a BLEProvider');
  }
  return context;
}
