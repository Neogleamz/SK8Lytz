import { autoPauseService } from '../AutoPauseService';

describe('AutoPauseService test suite', () => {
  let sendBackMock: jest.Mock;
  let gpsSpeedRef: { current: number };

  beforeEach(() => {
    sendBackMock = jest.fn();
    gpsSpeedRef = { current: 0 };
    jest.useFakeTimers();
  });

  afterEach(() => {
    jest.useRealTimers();
  });

  const getCallback = () => (autoPauseService as any).config;

  it('1. Triggers AUTO_PAUSE in ACTIVE state when speed remains low for 10 seconds (20 ticks)', () => {
    const callback = getCallback();
    const cleanup = callback({
      sendBack: sendBackMock,
      input: {
        autoPauseEnabled: true,
        gpsSpeedRef,
        sessionPhase: 'ACTIVE',
      },
    });

    // Advance 5 seconds (10 ticks) -> should not trigger
    jest.advanceTimersByTime(5000);
    expect(sendBackMock).not.toHaveBeenCalled();

    // Advance another 5 seconds (10 ticks) -> 10s total -> triggers AUTO_PAUSE
    jest.advanceTimersByTime(5000);
    expect(sendBackMock).toHaveBeenCalledWith({ type: 'AUTO_PAUSE' });

    cleanup();
  });

  it('2. Does not trigger AUTO_PAUSE if autoPauseEnabled is false', () => {
    const callback = getCallback();
    const cleanup = callback({
      sendBack: sendBackMock,
      input: {
        autoPauseEnabled: false,
        gpsSpeedRef,
        sessionPhase: 'ACTIVE',
      },
    });

    jest.advanceTimersByTime(10000);
    expect(sendBackMock).not.toHaveBeenCalled();

    cleanup();
  });

  it('3. Resets low speed counter if speed goes above 0.2 before 10s', () => {
    const callback = getCallback();
    const cleanup = callback({
      sendBack: sendBackMock,
      input: {
        autoPauseEnabled: true,
        gpsSpeedRef,
        sessionPhase: 'ACTIVE',
      },
    });

    // Low speed for 8 seconds
    jest.advanceTimersByTime(8000);
    expect(sendBackMock).not.toHaveBeenCalled();

    // Speed goes up
    gpsSpeedRef.current = 1.5;
    jest.advanceTimersByTime(1000);

    // Speed goes down again
    gpsSpeedRef.current = 0;
    jest.advanceTimersByTime(8000); // 8s down again (counter reset should mean no pause yet)
    expect(sendBackMock).not.toHaveBeenCalled();

    // Advance 2s more -> triggers
    jest.advanceTimersByTime(2000);
    expect(sendBackMock).toHaveBeenCalledWith({ type: 'AUTO_PAUSE' });

    cleanup();
  });

  it('4. Triggers AUTO_RESUME in PAUSED state when speed goes above 0.2', () => {
    const callback = getCallback();
    gpsSpeedRef.current = 0;

    const cleanup = callback({
      sendBack: sendBackMock,
      input: {
        autoPauseEnabled: true,
        gpsSpeedRef,
        sessionPhase: 'PAUSED',
      },
    });

    jest.advanceTimersByTime(1000);
    expect(sendBackMock).not.toHaveBeenCalled();

    // Speed goes above 0.2 -> triggers immediately on next tick
    gpsSpeedRef.current = 0.5;
    jest.advanceTimersByTime(500);
    expect(sendBackMock).toHaveBeenCalledWith({ type: 'AUTO_RESUME' });

    cleanup();
  });
});
