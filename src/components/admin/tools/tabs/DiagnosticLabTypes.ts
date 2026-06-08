export interface DiagnosticDevice {
  id: string;
  name: string | null;
}

export interface DiagnosticHwSettings {
  ledPoints?: number;
  colorSorting?: number;
  colorSortingName?: string;
  icName?: string;
  segments?: number;
  detected?: boolean;
}

export interface DiagnosticLogEvent {
  timestamp: number;
  type: 'TX' | 'RX' | 'INFO';
  deviceId?: string;
  payloadHex?: string;
  raw?: number[];
  note?: string;
}

export interface DeviceHardwareConfig {
  points?: number;
  segments?: number;
  sorting?: number | string;
  colorSortingName?: string;
  stripType?: string;
  icName?: string;
}
