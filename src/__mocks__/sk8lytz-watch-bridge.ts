/**
 * Jest mock for sk8lytz-watch-bridge.
 *
 * This mock replaces the native Expo module in unit tests so that:
 *  - Tests that call WatchBridge.syncSessionState() don't crash on missing native module
 *  - We can assert the bridge was called with the correct payload
 *  - CI (npm run verify) passes without a physical device
 *
 * Place: src/__mocks__/sk8lytz-watch-bridge.ts
 * Auto-loaded: via moduleNameMapper in jest.config.js
 */

type WatchSessionState = any;
type WatchCommand = any;
type WatchHealthUpdate = any;

const mockSyncSessionState = jest.fn().mockResolvedValue(undefined);
const mockSendMetricUpdate = jest.fn().mockResolvedValue(undefined);
const mockIsWatchReachable = jest.fn().mockResolvedValue(false);
const mockAddWatchCommandListener = jest.fn().mockReturnValue(() => {});
const mockAddWatchHealthListener = jest.fn().mockReturnValue(() => {});

export const WatchBridge = {
  syncSessionState:        mockSyncSessionState,
  sendMetricUpdate:        mockSendMetricUpdate,
  isWatchReachable:        mockIsWatchReachable,
  addWatchCommandListener: mockAddWatchCommandListener,
  addWatchHealthListener:  mockAddWatchHealthListener,
};

export type { WatchSessionState, WatchCommand, WatchHealthUpdate };
