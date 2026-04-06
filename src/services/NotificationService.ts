/**
 * NotificationService — Expo push notification wrapper
 *
 * Handles:
 *   - Permission request + Expo push token retrieval
 *   - Foreground notification display (banner while app is open)
 *   - Notification response handler (tap → join crew session)
 *   - Platform-specific Android notification channel setup
 *
 * Usage in DashboardScreen:
 *   useEffect(() => {
 *     NotificationService.init(profile?.display_name ?? 'Sk8r');
 *     NotificationService.setJoinHandler((crewId, sessionId) => { ... });
 *     return () => NotificationService.cleanup();
 *   }, []);
 */

import * as Notifications from 'expo-notifications';
import { Platform } from 'react-native';
import { profileService } from './ProfileService';
import { AppLogger } from './AppLogger';

// Show push notifications as banners even when app is in foreground
Notifications.setNotificationHandler({
  handleNotification: async () => ({
    shouldShowAlert: true,
    shouldShowBanner: true,
    shouldShowList: true,
    shouldPlaySound: true,
    shouldSetBadge: false,
  }),
});

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// Types
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

export type JoinHandler = (crewId: string, sessionId: string) => void;

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// NotificationService
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

class NotificationService {
  private token: string | null = null;
  private joinHandler: JoinHandler | null = null;
  private foregroundSub: Notifications.Subscription | null = null;
  private responseSub: Notifications.Subscription | null = null;

  /**
   * Full initialization:
   *   1. Set up Android notification channel
   *   2. Request permissions
   *   3. Get Expo push token
   *   4. Register token with Supabase
   *   5. Wire foreground + response handlers
   */
  async init(): Promise<string | null> {
    await this._setupAndroidChannel();

    const granted = await this._requestPermissions();
    if (!granted) {
      console.log('[NotificationService] Permission denied');
      return null;
    }

    try {
      const tokenObj = await Notifications.getExpoPushTokenAsync({
        projectId: process.env.EXPO_PUBLIC_PROJECT_ID,
      });
      this.token = tokenObj.data;
      console.log('[NotificationService] Token:', this.token);

      const platform = Platform.OS as 'ios' | 'android' | 'web';
      await profileService.registerPushToken(this.token, platform);
      AppLogger.log('PUSH_TOKEN_REGISTERED', { platform, tokenPrefix: this.token.slice(0, 12) });
    } catch (err) {
      console.log('[NotificationService] Push token unavailable (simulator?):', err);
      return null;
    }

    this._wireForegroundHandler();
    this._wireResponseHandler();

    return this.token;
  }

  /** Set the handler called when user taps a crew-invite notification. */
  setJoinHandler(handler: JoinHandler): void {
    this.joinHandler = handler;
  }

  /** Remove all listeners and unregister token from Supabase. */
  async cleanup(): Promise<void> {
    this.foregroundSub?.remove();
    this.responseSub?.remove();
    this.foregroundSub = null;
    this.responseSub = null;

    if (this.token) {
      await profileService.unregisterPushToken(this.token);
      this.token = null;
    }
  }

  get pushToken() { return this.token; }

  // ── Private ──────────────────────────────────────────────

  private async _requestPermissions(): Promise<boolean> {
    if (Platform.OS === 'web') return false; // Web push not supported in this flow

    const { status: existing } = await Notifications.getPermissionsAsync();
    if (existing === 'granted') return true;

    const { status } = await Notifications.requestPermissionsAsync();
    return status === 'granted';
  }

  private async _setupAndroidChannel(): Promise<void> {
    if (Platform.OS !== 'android') return;

    await Notifications.setNotificationChannelAsync('crew-alerts', {
      name: 'Crew Alerts',
      importance: Notifications.AndroidImportance.HIGH,
      vibrationPattern: [0, 250, 250, 250],
      lightColor: '#FFAA00',
      description: 'Notifications when your crew starts skating',
    });
  }

  private _wireForegroundHandler(): void {
    this.foregroundSub = Notifications.addNotificationReceivedListener(notification => {
      // Already handled by setNotificationHandler above (shows banner)
      console.log('[NotificationService] Foreground notification:', notification.request.identifier);
    });
  }

  private _wireResponseHandler(): void {
    this.responseSub = Notifications.addNotificationResponseReceivedListener(response => {
      const data = response.notification.request.content.data as Record<string, string>;
      const crewId    = data?.crewId;
      const sessionId = data?.sessionId;

      console.log('[NotificationService] Tapped notification:', { crewId, sessionId });
      AppLogger.log('PUSH_NOTIFICATION_TAPPED', { crewId, sessionId });

      if (crewId && sessionId && this.joinHandler) {
        this.joinHandler(crewId, sessionId);
      }
    });
  }
}

export const notificationService = new NotificationService();
