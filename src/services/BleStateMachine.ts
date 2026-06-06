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
      const msg = `[BleStateMachine] INVALID TRANSITION: ${from} -> ${to} (Reason: ${reason})`;
      AppLogger.error(msg);
      AppLogger.log('BLE_STATE_CHANGE', {
        event: 'fsm_invalid_transition',
        from,
        to,
        reason,
      });
      if (typeof __DEV__ !== 'undefined' && __DEV__) {
        throw new Error(msg);
      }
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
        return to === 'IDLE' || to === 'CONNECTING' || to === 'DISCONNECTING';
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

  /**
   * Force-transitions to the given phase WITHOUT validation.
   * Use ONLY in error recovery paths where the gate is known to be in an
   * inconsistent state (e.g., connection failure cleanup).
   */
  forceTransitionTo(nextPhase: BLEPhase, reason: string): void {
    const from = this._phase.tag;
    AppLogger.warn(`[BleStateMachine] FORCE TRANSITION: ${from} -> ${nextPhase.tag} (Reason: ${reason})`);
    AppLogger.log('BLE_STATE_CHANGE', {
      event: 'fsm_force_transition',
      from,
      to: nextPhase.tag,
      reason,
    });
    this._phase = nextPhase;
    this.notify();
  }
}
