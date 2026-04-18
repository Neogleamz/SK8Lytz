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
import { AppLogger } from './AppLogger';
import { profileService } from './ProfileService';

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
  async init(autoRequest: boolean = false): Promise<string | null> {
    await this._setupAndroidChannel();

    const granted = await this._requestPermissions(autoRequest);
    if (!granted) {
      console.log('[NotificationService] Permission denied (or undetermined)');
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

  // ── Send notifications ────────────────────────────────────

  /**
   * Fire a local notification when a crew member joins or an invite is accepted.
   * Call this after joinPublicCrewById / joinSession succeeds on the LEADER's device.
   */
  async sendCrewInviteNotification(opts: {
    joinerName: string;
    crewName: string;
    crewId: string;
    sessionId?: string;
  }): Promise<void> {
    try {
      await Notifications.scheduleNotificationAsync({
        content: {
          title: `🛼 ${opts.joinerName} joined your crew!`,
          body: `${opts.joinerName} has joined "${opts.crewName}". Time to skate!`,
          data: { crewId: opts.crewId, sessionId: opts.sessionId ?? '' },
          sound: true,
          ...(Platform.OS === 'android' ? { channelId: 'crew-alerts' } : {}),
        },
        trigger: null, // immediate
      });
      AppLogger.log('PUSH_NOTIFICATION_SENT', { type: 'crew_invite', crewId: opts.crewId });
    } catch (err) {
      console.warn('[NotificationService] sendCrewInviteNotification failed:', err);
    }
  }

  /**
   * Schedule a local "starting soon" reminder 15 minutes before a session.
   * Returns the notification identifier so it can be cancelled if the session is cancelled.
   */
  async sendSessionStartingSoon(opts: {
    sessionId: string;
    sessionName: string;
    crewName: string;
    scheduledAt: Date;
  }): Promise<string | null> {
    const triggerDate = new Date(opts.scheduledAt.getTime() - 15 * 60 * 1000);
    if (triggerDate <= new Date()) {
      // Session starts in under 15 min — fire immediately
      return this._scheduleSessionAlert(opts, null);
    }
    return this._scheduleSessionAlert(opts, { date: triggerDate });
  }

  /** Cancel a previously scheduled session reminder by its notification ID. */
  async cancelSessionReminder(notificationId: string): Promise<void> {
    try {
      await Notifications.cancelScheduledNotificationAsync(notificationId);
    } catch { /* already fired or didn't exist */ }
  }

  /** Send an immediate "session is starting now" local notification to all crew members. */
  async sendSessionLiveAlert(opts: {
    sessionId: string;
    sessionName: string;
    crewName: string;
    locationLabel: string;
  }): Promise<void> {
    try {
      await Notifications.scheduleNotificationAsync({
        content: {
          title: `🛼 "${opts.sessionName}" is LIVE!`,
          body: `Your ${opts.crewName} crew session is starting now at ${opts.locationLabel}. Let's skate!`,
          data: { sessionId: opts.sessionId, crewId: '' },
          sound: true,
          ...(Platform.OS === 'android' ? { channelId: 'session-reminders' } : {}),
        },
        trigger: null,
      });
    } catch (err) {
      console.warn('[NotificationService] sendSessionLiveAlert failed:', err);
    }
  }

  // ── Private ──────────────────────────────────────────────

  private async _scheduleSessionAlert(
    opts: { sessionId: string; sessionName: string; crewName: string; scheduledAt: Date },
    trigger: { date: Date } | null,
  ): Promise<string | null> {
    try {
      const id = await Notifications.scheduleNotificationAsync({
        content: {
          title: `⏰ Session starting in 15 min!`,
          body: `"${opts.sessionName}" with ${opts.crewName} starts soon — get your skates on!`,
          data: { sessionId: opts.sessionId, crewId: '' },
          sound: true,
          ...(Platform.OS === 'android' ? { channelId: 'session-reminders' } : {}),
        },
        trigger: trigger as any,
      });
      AppLogger.log('PUSH_NOTIFICATION_SENT', { type: 'session_reminder', subtype: 'scheduled', sessionId: opts.sessionId, trigger: trigger?.date?.toISOString() });
      return id;
    } catch (err) {
      console.warn('[NotificationService] _scheduleSessionAlert failed:', err);
      return null;
    }
  }


  private async _requestPermissions(autoRequest: boolean): Promise<boolean> {
    if (Platform.OS === 'web') return false; // Web push not supported in this flow

    const { status: existing } = await Notifications.getPermissionsAsync();
    if (existing === 'granted') return true;

    if (autoRequest) {
      const { status } = await Notifications.requestPermissionsAsync();
      return status === 'granted';
    }
    
    return false;
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
    await Notifications.setNotificationChannelAsync('session-reminders', {
      name: 'Session Reminders',
      importance: Notifications.AndroidImportance.HIGH,
      vibrationPattern: [0, 300, 200, 300],
      lightColor: '#00C8FF',
      description: 'Reminders before your scheduled skate sessions',
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
