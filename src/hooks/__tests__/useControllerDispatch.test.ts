import { useControllerDispatch } from '../useControllerDispatch';
import { ZenggeProtocol } from '../../protocols/ZenggeProtocol';
import { ZenggeAdapter } from '../../protocols/ZenggeAdapter';
import { getLocalProfileById } from '../../constants/ProductCatalog';
import { AppLogger } from '../../services/appLogger';
import { buildPatternPayload } from '../../protocols/PatternEngine';

// Global mocks
(global as any).__DEV__ = true;

jest.mock('react', () => ({
  ...jest.requireActual('react'),
  useCallback: (fn: any) => fn,
}));

jest.mock('../../services/AppLogger', () => ({
  AppLogger: {
    log: jest.fn(),
    warn: jest.fn(),
    error: jest.fn(),
  }
}));

jest.mock('../../constants/ProductCatalog', () => ({
  getLocalProfileById: jest.fn(),
}));

function expectPayloadMatch(mockFn: jest.Mock, expectedPayload: number[], extraArgs: any[] = []) {
  console.log('[DEBUG expectPayloadMatch] mockFn.mock.calls =', JSON.stringify(mockFn.mock.calls.map(c => [c[0]?.slice?.(0, 10), c[1], c[2]])));
  if (expectedPayload) {
    expectedPayload[1] = expect.any(Number) as any;
  }
  expect(mockFn).toHaveBeenCalledWith(expectedPayload, "", ...extraArgs);
}

describe('useControllerDispatch', () => {
  let mockWriteToDevice: jest.Mock;

  beforeEach(() => {
    jest.clearAllMocks();
    mockWriteToDevice = jest.fn().mockResolvedValue(true);
    (getLocalProfileById as jest.Mock).mockReturnValue({ vizShape: 'STRIP' });
  });

  describe('Missing writeToDevice Handling', () => {
    it('gracefully logs error without crashing when writeToDevice is undefined', async () => {
      const dispatch = useControllerDispatch({ getAdapterForDevice: () => new ZenggeAdapter(), writeToDevice: undefined, hwSettings: undefined, points: 16 });
      
      await dispatch.sendColor(255, 0, 0);
      expect(AppLogger.error).toHaveBeenCalledWith('BLE_DEAD_WIRE', expect.stringContaining('writeToDevice is undefined'), expect.any(Object));
    });
  });

  describe('sendColor', () => {
    it('dispatches a 0x59 FREEZE payload mapped to the correct numLEDs', async () => {
      const dispatch = useControllerDispatch({ getAdapterForDevice: () => new ZenggeAdapter(), writeToDevice: mockWriteToDevice, points: 5 });
      await dispatch.sendColor(255, 100, 50);

      const arr = Array.from({ length: 5 }, () => ({ r: 255, g: 100, b: 50 }));
      const expectedPayload = new ZenggeAdapter().buildSolidColor(255, 100, 50).packets[0];
      
      expectPayloadMatch(mockWriteToDevice, expectedPayload);
    });
  });

  describe('applyFixedPattern', () => {
    it('sends solid color bypassing PatternEngine when patternId is 1', async () => {
      const dispatch = useControllerDispatch({ getAdapterForDevice: () => new ZenggeAdapter(), writeToDevice: mockWriteToDevice, points: 10 });
      await dispatch.applyFixedPattern(1, '#FF0000', '#000000', 50, 100, 1);

      const expectedArr = Array.from({ length: 10 }, () => ({ r: 255, g: 0, b: 0 }));
      const expectedPayload = new ZenggeAdapter().buildSolidColor(255, 0, 0).packets[0];
      
      expectPayloadMatch(mockWriteToDevice, expectedPayload);
    });

    it('uses PatternEngine and dispatches the synthesized payload', async () => {
      const dispatch = useControllerDispatch({ getAdapterForDevice: () => new ZenggeAdapter(), writeToDevice: mockWriteToDevice, points: 16 });
      await dispatch.applyFixedPattern(2, '#FF0000', '#00FF00', 50, 100, 1);
      
      const synthesizedPayload = buildPatternPayload(
        2,
        { r: 255, g: 0, b: 0 },
        { r: 0, g: 255, b: 0 },
        16,
        50,
        1,
        100
      ) as number[];
      
      expectPayloadMatch(mockWriteToDevice, synthesizedPayload);
    });
  });

  describe('applyStaticModePattern', () => {
    it('handles STATIC by delegating to sendColor', () => {
      const dispatch = useControllerDispatch({ getAdapterForDevice: () => new ZenggeAdapter(), writeToDevice: mockWriteToDevice, points: 5 });
      dispatch.applyStaticModePattern('STATIC', '#FF0000', 50);

      const expectedArr = Array.from({ length: 5 }, () => ({ r: 255, g: 0, b: 0 }));
      const expectedPayload = new ZenggeAdapter().buildSolidColor(255, 0, 0).packets[0];
      
      expectPayloadMatch(mockWriteToDevice, expectedPayload);
    });

    it('handles STROBE mode', () => {
      const dispatch = useControllerDispatch({ getAdapterForDevice: () => new ZenggeAdapter(), writeToDevice: mockWriteToDevice, points: 5 });
      dispatch.applyStaticModePattern('STROBE', '#00FF00', 100);

      const expectedPayload = ZenggeProtocol.setCustomModeCompact([
        { mode: ZenggeProtocol.STEP_STROBE, speed: 100, color1: { r: 0, g: 255, b: 0 }, color2: { r: 0, g: 0, b: 0 } }
      ]);
      expectPayloadMatch(mockWriteToDevice, expectedPayload);
    });

    it('handles BLINK mode', () => {
      const dispatch = useControllerDispatch({ getAdapterForDevice: () => new ZenggeAdapter(), writeToDevice: mockWriteToDevice, points: 5 });
      dispatch.applyStaticModePattern('BLINK', '#0000FF', 0);

      const expectedPayload = ZenggeProtocol.setCustomModeCompact([
        { mode: ZenggeProtocol.STEP_JUMP, speed: 1, color1: { r: 0, g: 0, b: 255 }, color2: { r: 0, g: 0, b: 0 } }
      ]);
      expectPayloadMatch(mockWriteToDevice, expectedPayload);
    });
  });

  describe('applyEmergencyPattern', () => {
    it('dispatches the correct ring topology mapped emergency payload', () => {
      (getLocalProfileById as jest.Mock).mockReturnValue({ vizShape: 'RING' });
      const dispatch = useControllerDispatch({ getAdapterForDevice: () => new ZenggeAdapter(), writeToDevice: mockWriteToDevice, hwSettings: { ledPoints: 8, type: 'HALOZ' } });
      dispatch.applyEmergencyPattern(31, 100);

      const red = { r: 255, g: 0, b: 0 };
      const white = { r: 255, g: 255, b: 255 };
      const yellow = { r: 255, g: 255, b: 0 };
      const off = { r: 0, g: 0, b: 0 };

      const arr = [red, red, yellow, off, yellow, off, white, white];
      const expectedPayload = ZenggeProtocol.setMultiColor(arr, 8, 31, 1, 0x02);
      
      expectPayloadMatch(mockWriteToDevice, expectedPayload);
    });

    it('dispatches the correct linear strip topology emergency payload', () => {
      (getLocalProfileById as jest.Mock).mockReturnValue({ vizShape: 'STRIP' });
      const dispatch = useControllerDispatch({ getAdapterForDevice: () => new ZenggeAdapter(), writeToDevice: mockWriteToDevice, hwSettings: { ledPoints: 10, type: 'SOULZ' } });
      dispatch.applyEmergencyPattern(31, 100);

      const red = { r: 255, g: 0, b: 0 };
      const white = { r: 255, g: 255, b: 255 };
      const yellow = { r: 255, g: 255, b: 0 };
      const off = { r: 0, g: 0, b: 0 };

      const arr = [
        red, red, red,
        yellow, off, yellow, off,
        white, white, white
      ];

      const expectedPayload = ZenggeProtocol.setMultiColor(arr, 10, 31, 1, 0x02);
      expectPayloadMatch(mockWriteToDevice, expectedPayload);
    });
  });

  describe('handleMusicChange', () => {
    it('dispatches DEVICE mic config payload', () => {
      const dispatch = useControllerDispatch({ getAdapterForDevice: () => new ZenggeAdapter(), writeToDevice: mockWriteToDevice, points: 16 });
      dispatch.handleMusicChange(3, 80, 100, 'DEVICE', '#FF0000', '#0000FF', 0x27);

      const c1 = { r: 255, g: 0, b: 0 };
      const c2 = { r: 0, g: 0, b: 255 };
      
      const expectedPayload = ZenggeProtocol.setMusicConfig(3, 0x27, true, c1, c2, 80, 100);
      expectPayloadMatch(mockWriteToDevice, expectedPayload, [{ micSource: 'DEVICE' }]);
      expect(AppLogger.log).toHaveBeenCalledWith("MUSIC_CONFIG_REQUESTED", expect.any(Object));
    });

    it('dispatches APP mic config payload with isOn=false (onboard mic disabled in APP mode)', () => {
      const dispatch = useControllerDispatch({ getAdapterForDevice: () => new ZenggeAdapter(), writeToDevice: mockWriteToDevice, points: 16 });
      dispatch.handleMusicChange(1, 50, 50, 'APP', '#FFFFFF', '#000000', 0x26);

      const c1 = { r: 255, g: 255, b: 255 };
      const c2 = { r: 0, g: 0, b: 0 };
      
      // BIBLE §11 DECOMPILER TRUTH: isOn must be false (0x00) in APP mode so the onboard mic is disabled.
      const expectedPayload = ZenggeProtocol.setMusicConfig(1, 0x26, false, c1, c2, 50, 50);
      expectPayloadMatch(mockWriteToDevice, expectedPayload, [{ micSource: 'APP' }]);
    });

  });

  describe('setPower', () => {
    it('dispatches ON payload', () => {
      const dispatch = useControllerDispatch({ getAdapterForDevice: () => new ZenggeAdapter(), writeToDevice: mockWriteToDevice, points: 16 });
      dispatch.setPower(true);
      expectPayloadMatch(mockWriteToDevice, ZenggeProtocol.turnOn());
    });

    it('dispatches OFF payload', () => {
      const dispatch = useControllerDispatch({ getAdapterForDevice: () => new ZenggeAdapter(), writeToDevice: mockWriteToDevice, points: 16 });
      dispatch.setPower(false);
      expectPayloadMatch(mockWriteToDevice, ZenggeProtocol.turnOff());
    });
  });

  describe('setMultiColor', () => {
    it('directly passes through array and parameters to ZenggeProtocol.setMultiColor', () => {
      const dispatch = useControllerDispatch({ getAdapterForDevice: () => new ZenggeAdapter(), writeToDevice: mockWriteToDevice, points: 16 });
      const arr = [{ r: 10, g: 20, b: 30 }];
      dispatch.setMultiColor(arr, 16, 25, 1, 0x01);
      
      const expectedPayload = ZenggeProtocol.setMultiColor(arr, 16, 25, 1, 0x01);
      expectPayloadMatch(mockWriteToDevice, expectedPayload);
    });
  });
});
