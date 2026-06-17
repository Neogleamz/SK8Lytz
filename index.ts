/* eslint-disable no-undef, unused-imports/no-unused-vars */
import 'react-native-worklets-core';
import { registerRootComponent } from 'expo';
import notifee, { EventType } from '@notifee/react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Platform } from 'react-native';

import App from './App';

import { WatchBridge } from 'sk8lytz-watch-bridge';

// Register Notifee background event handler
notifee.onBackgroundEvent(async ({ type, detail }) => {
  if (type === EventType.ACTION_PRESS) {
    const actionId = detail.pressAction?.id;

    if (actionId === 'end-session') {
      try {
        await AsyncStorage.multiSet([
          ['@sk8lytz_session_active', 'false'],
          ['@sk8lytz_pending_bg_end', 'true'],
        ]);
      } catch (e) {
        console.error('Failed to save session state in background handler', e);
      }
      WatchBridge.syncSessionState({ status: 'STOPPED' }).catch(() => {});
      if (detail.notification?.id) {
        await notifee.cancelNotification(detail.notification.id);
      }
      await notifee.stopForegroundService();
    } else if (actionId === 'toggle-music') {
      import('react-native').then(({ DeviceEventEmitter }) => {
        DeviceEventEmitter.emit('BACKGROUND_ACTION_TOGGLE_MUSIC');
      });
    } else if (actionId === 'fire-favorite') {
      import('react-native').then(({ DeviceEventEmitter }) => {
        DeviceEventEmitter.emit('BACKGROUND_ACTION_FIRE_FAVORITE');
      });
    }
  }
});

// Register a headless task loop so Android doesn't kill the foreground service after 5 seconds
if (Platform.OS === 'android') {
  notifee.registerForegroundService(() => new Promise(() => {}));
}

// Register global unhandled error interceptor for non-React crashes (event handlers, async)
if (typeof ErrorUtils !== 'undefined') {
  const previousGlobalHandler = ErrorUtils.getGlobalHandler();
  ErrorUtils.setGlobalHandler(async (error: any, isFatal?: boolean) => {
    try {
      const { logFatalCrash } = require('./src/utils/CrashReporter');
      await logFatalCrash(
        error instanceof Error ? error : new Error(String(error)),
        error?.stack || 'No stack trace available'
      );
    } catch (_e) {
      console.error('Failed to report fatal crash globally', _e);
    }
    // Call the original handler (which might show the redbox in dev)
    if (previousGlobalHandler) {
      previousGlobalHandler(error, isFatal);
    }
  });
} else if (Platform.OS === 'web' && typeof window !== 'undefined') {
  window.addEventListener('error', async (event: any) => {
    try {
      const { logFatalCrash } = require('./src/utils/CrashReporter');
      await logFatalCrash(event.error, event.error?.stack || 'No stack trace available');
    } catch (_e) {
      // Ignore
    }
  });
  window.addEventListener('unhandledrejection', async (event: any) => {
    try {
      const { logFatalCrash } = require('./src/utils/CrashReporter');
      const err = event.reason instanceof Error ? event.reason : new Error(String(event.reason));
      await logFatalCrash(err, err.stack || 'No stack trace available');
    } catch (_e) {
      // Ignore
    }
  });
}

// registerRootComponent calls AppRegistry.registerComponent('main', () => App);
// It also ensures that whether you load the app in Expo Go or in a native build,
// the environment is set up appropriately
registerRootComponent(App);
