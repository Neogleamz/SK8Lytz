/**
 * AuthProfileService — User profile CRUD and session history
 *
 * Owns: fetchOrCreateProfile, updateProfile, getSessionHistory
 *
 * Part of epic/god-object-decomposition — Meal 1: ProfileService split.
 */

import { supabase } from './supabaseClient';
import type { UserProfile, SessionHistoryItem } from './ProfileService.types';
import { AppLogger } from './AppLogger';
import type { User } from '@supabase/supabase-js';

class AuthProfileService {

  /**
   * Fetch or auto-create a profile for the currently logged-in user.
   * Automatically self-heals missing display names or usernames from Auth metadata
   * if the database trigger failed to set them during signup.
   * @param cachedUser Optional pre-fetched user object to avoid redundant network calls
   */
  async fetchOrCreateProfile(user?: User | null): Promise<UserProfile | null> {
    try {
      if (!user) return null;

      const { data: existing } = await supabase
        .from('user_profiles')
        .select('*')
        .eq('user_id', user.id)
        .single();

      if (existing) {
        // Self-heal: If display_name or username is missing, patch from auth metadata
        const profile = existing as UserProfile;
        const metaUsername = user.user_metadata?.username;
        const metaDisplayName = user.user_metadata?.display_name;
        
        if ((metaUsername || metaDisplayName) && (!profile.display_name || !profile.username)) {
          const updateData: Partial<UserProfile> = {};
          
          if (!profile.display_name && (metaDisplayName || metaUsername)) {
            updateData.display_name = metaDisplayName || metaUsername;
          }
          
          if (!profile.username && metaUsername) {
            updateData.username = metaUsername.toLowerCase();
          }
          
          if (Object.keys(updateData).length > 0) {
            await supabase
              .from('user_profiles')
              .update(updateData)
              .eq('user_id', user.id);
              
            return { ...profile, ...updateData };
          }
        }
        return profile;
      }

      // Auto-create using auth metadata username, falling back to email prefix
      const metaUsername = user.user_metadata?.username;
      const metaDisplayName = user.user_metadata?.display_name;
      const defaultName = metaDisplayName ?? metaUsername ?? user.email?.split('@')[0] ?? 'Sk8r';
      
      const { data: created } = await supabase
        .from('user_profiles')
        .insert({ 
          user_id: user.id, 
          display_name: defaultName,
          username: metaUsername ? metaUsername.toLowerCase() : undefined
        })
        .select()
        .single();

      return created as UserProfile | null;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[AuthProfileService] fetchOrCreateProfile failed', { error: msg , payload_size: 0, ssi: 0 });
      throw new Error(msg);
    }
  }

  /**
   * Update display name and/or avatar color for the current user.
   */
  async updateProfile(userId: string | undefined, fields: { display_name?: string | null; avatar_color?: string; username?: string | null; avatar_url?: string | null; notif_preferences?: Record<string, boolean> }): Promise<void> {
    if (!userId) throw new Error('Not authenticated');

    // Strip null/undefined values to avoid overwriting with null
    const cleanFields: Partial<UserProfile> = {};
    if (fields.display_name != null) cleanFields.display_name = fields.display_name;
    if (fields.avatar_color != null) cleanFields.avatar_color = fields.avatar_color;
    if (fields.username != null) cleanFields.username = fields.username.toLowerCase();
    if (fields.avatar_url != null) cleanFields.avatar_url = fields.avatar_url;
    if (fields.notif_preferences !== undefined) cleanFields.notif_preferences = fields.notif_preferences;

    if (Object.keys(cleanFields).length === 0) return;

    try {
      const { error } = await supabase
        .from('user_profiles')
        .update(cleanFields)
        .eq('user_id', userId);

      if (error) throw error;
    } catch (e: unknown) {
      if (typeof e === 'object' && e !== null && 'code' in e && (e as { code: string }).code === '23505') throw new Error('Username already taken');
      throw e;
    }
  }

  /**
   * Return the last 20 crew sessions the current user was part of.
   * @param cachedUserId Optional pre-fetched user ID to avoid redundant network calls
   */
  async getSessionHistory(userId?: string): Promise<SessionHistoryItem[]> {
    try {
      if (!userId) return [];

      const { data, error } = await supabase
        .from('crew_members')
        .select(`
          joined_at,
          crew_sessions (
            id,
            name,
            expires_at,
            leader_user_id,
            crews ( name )
          )
        `)
        .eq('user_id', userId)
        .order('joined_at', { ascending: false })
        .limit(20);

      if (error || !data) return [];

      return data.map((row: unknown) => {
        const r = row as { joined_at: string; crew_sessions: { id: string; name: string; expires_at: string; leader_user_id: string; crews: { name: string } | null } | null };
        const session = r.crew_sessions;
        return {
          session_id:   session?.id ?? '',
          session_name: session?.name ?? 'Crew Session',
          crew_name:    session?.crews?.name ?? null,
          role:         session?.leader_user_id === userId ? 'leader' : 'member',
          joined_at:    r.joined_at,
          expires_at:   session?.expires_at ?? '',
        } as SessionHistoryItem;
      });
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[AuthProfileService] getSessionHistory failed', { error: msg });
      return [];
    }
  }
}

export const authProfileService = new AuthProfileService();
