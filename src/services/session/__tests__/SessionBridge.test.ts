import { SessionBridge } from '../SessionBridge';
import { SessionMachineEvent } from '../SessionMachine.types';

describe('SessionBridge test suite', () => {
  let mockListener: jest.Mock;

  beforeEach(() => {
    mockListener = jest.fn();
    SessionBridge.unregister();
  });

  it('1. Does not throw if send is called when unregistered', () => {
    expect(() => {
      SessionBridge.send({ type: 'START' });
    }).not.toThrow();
    expect(mockListener).not.toHaveBeenCalled();
  });

  it('2. Routes events to the registered callback function', () => {
    SessionBridge.register(mockListener);

    const event: SessionMachineEvent = { type: 'START', externalStartTimeMs: 123456 };
    SessionBridge.send(event);

    expect(mockListener).toHaveBeenCalledWith(event);
  });

  it('3. Ceases routing events after unregister is called', () => {
    SessionBridge.register(mockListener);
    SessionBridge.send({ type: 'PAUSE' });
    expect(mockListener).toHaveBeenCalledTimes(1);

    SessionBridge.unregister();
    SessionBridge.send({ type: 'RESUME' });
    expect(mockListener).toHaveBeenCalledTimes(1); // Still 1
  });
});
