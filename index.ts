import 'react-native-worklets';
import { registerRootComponent } from 'expo';
import notifee, { EventType } from '@notifee/react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Platform } from 'react-native';

import App from './App';

import { WatchBridge } from 'sk8lytz-watch-bridge';

// Register Notifee background event handler
notifee.onBackgroundEvent(async ({ type, detail }) => {
  if (type === EventType.ACTION_PRESS && detail.pressAction?.id === 'end-session') {
    try {
      // Mark session as ended + flag for deferred full teardown on foreground
      await AsyncStorage.multiSet([
        ['@sk8lytz_session_active', 'false'],
        ['@sk8lytz_pending_bg_end', 'true'],
      ]);
    } catch (e) {
      console.error('Failed to save session state in background handler', e);
    }

    // Push STOPPED to watch immediately — WatchBridge works outside React
    WatchBridge.syncSessionState({ status: 'STOPPED' }).catch(() => {});

    if (detail.notification?.id) {
      await notifee.cancelNotification(detail.notification.id);
    }

    await notifee.stopForegroundService();
  }
});

// Register a headless task loop so Android doesn't kill the foreground service after 5 seconds
if (Platform.OS === 'android') {
  notifee.registerForegroundService(() => new Promise(() => {}));
}

// registerRootComponent calls AppRegistry.registerComponent('main', () => App);
// It also ensures that whether you load the app in Expo Go or in a native build,
// the environment is set up appropriately
registerRootComponent(App);
