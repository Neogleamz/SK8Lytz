/**
 * PushTokenService — Expo push token registration & revocation
 *
 * Handles storing and removing device push tokens in Supabase.
 * Called on app launch after notification permission is granted,
 * and on logout / permission revoke.
 *
 * Part of epic/god-object-decomposition — Meal 1: ProfileService split.
 */

import { supabase } from './supabaseClient';
import { AppLogger } from './AppLogger';

class PushTokenService {

  /**
   * Store or update the device's Expo push token in Supabase.
   * Called on app launch after notification permission is granted.
   */
  async registerPushToken(token: string, platform: 'ios' | 'android' | 'web', userId: string | null): Promise<void> {
    if (!userId) return; // silently skip if not logged in

    try {
      await supabase
        .from('push_tokens')
        .upsert(
          { user_id: userId, token, platform, updated_at: new Date().toISOString() },
          { onConflict: 'user_id,token' }
        );
    } catch (e: unknown) {
      AppLogger.error('PushTokenService', 'registerPushToken failed', { error: e instanceof Error ? e.message : String(e) , payload_size: 0, ssi: 0 });
    }
  }

  /**
   * Remove a push token (e.g., on logout or permission revoke).
   */
  async unregisterPushToken(token: string, userId: string | null): Promise<void> {
    if (!userId) return;

    try {
      await supabase
        .from('push_tokens')
        .delete()
        .eq('user_id', userId)
        .eq('token', token);
    } catch (e: unknown) {
      AppLogger.error('PushTokenService', 'unregisterPushToken failed', { error: e instanceof Error ? e.message : String(e) , payload_size: 0, ssi: 0 });
    }
  }
}

export const pushTokenService = new PushTokenService();
