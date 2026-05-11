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

class PushTokenService {

  /**
   * Store or update the device's Expo push token in Supabase.
   * Called on app launch after notification permission is granted.
   */
  async registerPushToken(token: string, platform: 'ios' | 'android' | 'web'): Promise<void> {
    const { data: { user } } = await supabase.auth.getUser();
    if (!user) return; // silently skip if not logged in

    await supabase
      .from('push_tokens')
      .upsert(
        { user_id: user.id, token, platform, updated_at: new Date().toISOString() },
        { onConflict: 'user_id,token' }
      );
  }

  /**
   * Remove a push token (e.g., on logout or permission revoke).
   */
  async unregisterPushToken(token: string): Promise<void> {
    const { data: { user } } = await supabase.auth.getUser();
    if (!user) return;

    await supabase
      .from('push_tokens')
      .delete()
      .eq('user_id', user.id)
      .eq('token', token);
  }
}

export const pushTokenService = new PushTokenService();
