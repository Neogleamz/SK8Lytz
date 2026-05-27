jest.mock('expo-battery', () => ({
  BatteryState: {},
}));
jest.mock('expo-device', () => ({}));
jest.mock('../AppLogger', () => ({
  AppLogger: {
    log: jest.fn(),
    warn: jest.fn(),
    error: jest.fn(),
    info: jest.fn(),
  },
}));

import { BleStateMachine } from '../BleStateMachine';

describe('BleStateMachine', () => {
  it('should initialize to IDLE phase', () => {
    const fsm = new BleStateMachine();
    expect(fsm.tag).toBe('IDLE');
    expect(fsm.phase).toEqual({ tag: 'IDLE' });
  });

  it('should support legal transitions', () => {
    const fsm = new BleStateMachine();

    // IDLE -> CONNECTING
    let success = fsm.transitionTo({ tag: 'CONNECTING', targetMacs: ['AA:BB:CC:DD:EE:FF'] }, 'connecting to skates');
    expect(success).toBe(true);
    expect(fsm.tag).toBe('CONNECTING');
    expect(fsm.phase).toEqual({ tag: 'CONNECTING', targetMacs: ['AA:BB:CC:DD:EE:FF'] });

    // CONNECTING -> IDLE
    success = fsm.transitionTo({ tag: 'IDLE' }, 'connect completed');
    expect(success).toBe(true);
    expect(fsm.tag).toBe('IDLE');

    // IDLE -> SCANNING
    success = fsm.transitionTo({ tag: 'SCANNING', sweeperId: 42 }, 'passive scan active');
    expect(success).toBe(true);
    expect(fsm.tag).toBe('SCANNING');

    // SCANNING -> IDLE
    success = fsm.transitionTo({ tag: 'IDLE' }, 'scan stopped');
    expect(success).toBe(true);
    expect(fsm.tag).toBe('IDLE');
  });

  it('should reject illegal transitions and warn without throwing', () => {
    const fsm = new BleStateMachine();

    // IDLE -> RECOVERING is valid, wait, is SCANNING -> RECOVERING valid? No!
    fsm.transitionTo({ tag: 'SCANNING' }, 'start scanning');
    const success = fsm.transitionTo({ tag: 'RECOVERING' }, 'attempt recovery');
    expect(success).toBe(false); // Rejected!
    expect(fsm.tag).toBe('SCANNING'); // State unchanged!
  });

  it('should notify listeners upon successful transition', () => {
    const fsm = new BleStateMachine();
    const listener = jest.fn();

    const unsubscribe = fsm.addListener(listener);

    fsm.transitionTo({ tag: 'CONNECTING' }, 'testing listener');
    expect(listener).toHaveBeenCalledTimes(1);
    expect(listener).toHaveBeenCalledWith({ tag: 'CONNECTING' });

    unsubscribe();
    fsm.transitionTo({ tag: 'IDLE' }, 'testing unsubscribe');
    expect(listener).toHaveBeenCalledTimes(1); // No new call!
  });
});
