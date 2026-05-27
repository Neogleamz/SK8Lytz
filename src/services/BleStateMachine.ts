import { AppLogger } from './AppLogger';

export type BLEPhase =
  | { tag: 'IDLE' }
  | { tag: 'SCANNING'; sweeperId?: number }
  | { tag: 'CONNECTING'; targetMacs?: string[] }
  | { tag: 'DISCONNECTING' }
  | { tag: 'RECOVERING'; ghostedMacs?: string[] };

export type BLEPhaseTag = BLEPhase['tag'];

export class BleStateMachine {
  private _phase: BLEPhase = { tag: 'IDLE' };
  private _listeners: ((phase: BLEPhase) => void)[] = [];

  constructor(initialPhase: BLEPhase = { tag: 'IDLE' }) {
    this._phase = initialPhase;
  }

  get phase(): BLEPhase {
    return this._phase;
  }

  get tag(): BLEPhaseTag {
    return this._phase.tag;
  }

  addListener(listener: (phase: BLEPhase) => void) {
    this._listeners.push(listener);
    return () => {
      this._listeners = this._listeners.filter(l => l !== listener);
    };
  }

  transitionTo(nextPhase: BLEPhase, reason: string): boolean {
    const from = this._phase.tag;
    const to = nextPhase.tag;

    if (from === to) return true;

    const isValid = this.validateTransition(from, to);
    if (!isValid) {
      AppLogger.warn(`[BleStateMachine] INVALID TRANSITION: ${from} -> ${to} (Reason: ${reason})`);
      return false;
    }

    AppLogger.log('BLE_STATE_CHANGE', {
      event: 'fsm_transition',
      from,
      to,
      reason,
      metadata: nextPhase,
    });

    this._phase = nextPhase;
    this.notify();
    return true;
  }

  private notify() {
    for (const listener of this._listeners) {
      try {
        listener(this._phase);
      } catch (e) {
        AppLogger.error('[BleStateMachine] Listener error', e);
      }
    }
  }

  private validateTransition(from: BLEPhaseTag, to: BLEPhaseTag): boolean {
    switch (from) {
      case 'IDLE':
        return to === 'SCANNING' || to === 'CONNECTING' || to === 'DISCONNECTING' || to === 'RECOVERING';
      case 'SCANNING':
        return to === 'IDLE' || to === 'CONNECTING';
      case 'CONNECTING':
        return to === 'IDLE' || to === 'RECOVERING' || to === 'DISCONNECTING';
      case 'DISCONNECTING':
        return to === 'IDLE';
      case 'RECOVERING':
        return to === 'IDLE' || to === 'CONNECTING';
      default:
        return false;
    }
  }
}
