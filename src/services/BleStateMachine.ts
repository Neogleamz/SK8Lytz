export type BLEPhase =
  | { tag: 'IDLE' }
  | { tag: 'SCANNING'; sweeperId?: number }
  | { tag: 'CONNECTING'; targetMacs?: string[] }
  | { tag: 'DISCONNECTING' }
  | { tag: 'RECOVERING'; ghostedMacs?: string[] };

export type BLEPhaseTag = BLEPhase['tag'];
