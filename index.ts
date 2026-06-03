import 'react-native-worklets';
import { registerRootComponent } from 'expo';
import notifee, { EventType } from '@notifee/react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';

import App from './App';

// Register Notifee background event handler
notifee.onBackgroundEvent(async ({ type, detail }) => {
  if (type === EventType.ACTION_PRESS && detail.pressAction?.id === 'end-session') {
    try {
      await AsyncStorage.setItem('@sk8lytz_session_active', 'false');
    } catch (e) {
      console.error('Failed to save session active state in background handler', e);
    }

    if (detail.notification?.id) {
      await notifee.cancelNotification(detail.notification.id);
    }

    await notifee.stopForegroundService();
  }
});

// Register a headless task loop so Android doesn't kill the foreground service after 5 seconds
notifee.registerForegroundService(() => new Promise(() => {}));

// registerRootComponent calls AppRegistry.registerComponent('main', () => App);
// It also ensures that whether you load the app in Expo Go or in a native build,
// the environment is set up appropriately
registerRootComponent(App);
