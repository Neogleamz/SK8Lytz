/**
 * Web stub for react-native-background-actions.
 *
 * react-native-background-actions registers a native foreground service module
 * that is unavailable on web, crashing Metro at bundle time with an
 * UnableToResolveError. BackgroundBLEService guards all calls with
 * Platform.OS !== 'android', so this stub is never actually invoked on web.
 *
 * Aliased by metro.config.js when platform === 'web'.
 */
const BackgroundService = {
  isRunning: () => false,
  start: async () => {},
  stop: async () => {},
};

export default BackgroundService;
