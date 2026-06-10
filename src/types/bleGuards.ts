import type { Device } from 'react-native-ble-plx';

export const isDevice = (obj: unknown): obj is Device => {
  if (typeof obj !== 'object' || obj === null) return false;
  const d = obj as Record<string, unknown>;
  return typeof d.id === 'string' && (typeof d.name === 'string' || d.name === null) && typeof d.rssi === 'number';
};
