/**
 * ProfileService — user profiles, persistent crews, session history, push tokens
 *
 * Tables required (run migrations 003–005):
 *   public.user_profiles     — display_name, avatar_color
 *   public.crews             — permanent crew entities
 *   public.crew_memberships  — permanent follows (crew_id, user_id)
 *   public.push_tokens       — Expo push tokens
 */

import { supabase } from './supabaseClient';

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// Types
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

export interface UserProfile {
  user_id: string;
  display_name: string | null;
  avatar_color: string;
  created_at: string;
  updated_at: string;
}

export interface PermanentCrew {
  id: string;
  name: string;
  owner_id: string | null;
  invite_code: string;
  created_at: string;
  /** member_count is joined/computed client-side */
  member_count?: number;
  /** true if the current user is the owner */
  is_owner?: boolean;
}

export interface SessionHistoryItem {
  session_id: string;
  session_name: string;
  crew_name: string | null;
  role: 'leader' | 'member';
  joined_at: string;
  expires_at: string;
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// ProfileService
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

class ProfileService {

  // ── Profile ─────────────────────────────────────────────

  /**
   * Fetch or auto-create a profile for the currently logged-in user.
   */
  async fetchOrCreateProfile(): Promise<UserProfile | null> {
    const { data: { user } } = await supabase.auth.getUser();
    if (!user) return null;

    const { data: existing } = await supabase
      .from('user_profiles')
      .select('*')
      .eq('user_id', user.id)
      .single();

    if (existing) return existing as UserProfile;

    // Auto-create with email prefix as default display name
    const defaultName = user.email?.split('@')[0] ?? 'Sk8r';
    const { data: created } = await supabase
      .from('user_profiles')
      .insert({ user_id: user.id, display_name: defaultName })
      .select()
      .single();

    return created as UserProfile | null;
  }

  /**
   * Update display name and/or avatar color for the current user.
   */
  async updateProfile(fields: { display_name?: string; avatar_color?: string }): Promise<void> {
    const { data: { user } } = await supabase.auth.getUser();
    if (!user) throw new Error('Not authenticated');

    const { error } = await supabase
      .from('user_profiles')
      .upsert({ user_id: user.id, ...fields }, { onConflict: 'user_id' });

    if (error) throw error;
  }

  // ── Session History ──────────────────────────────────────

  /**
   * Return the last 20 crew sessions the current user was part of.
   */
  async getSessionHistory(): Promise<SessionHistoryItem[]> {
    const { data: { user } } = await supabase.auth.getUser();
    if (!user) return [];

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
      .eq('user_id', user.id)
      .order('joined_at', { ascending: false })
      .limit(20);

    if (error || !data) return [];

    return data.map((row: any) => {
      const session = row.crew_sessions;
      return {
        session_id:   session?.id ?? '',
        session_name: session?.name ?? 'Crew Session',
        crew_name:    session?.crews?.name ?? null,
        role:         session?.leader_user_id === user.id ? 'leader' : 'member',
        joined_at:    row.joined_at,
        expires_at:   session?.expires_at ?? '',
      } as SessionHistoryItem;
    });
  }

  // ── Permanent Crews ──────────────────────────────────────

  /**
   * List all permanent crews the current user is a member of.
   */
  async getMyCrew(): Promise<PermanentCrew[]> {
    const { data: { user } } = await supabase.auth.getUser();
    if (!user) return [];

    const { data, error } = await supabase
      .from('crew_memberships')
      .select(`crews ( id, name, owner_id, invite_code, created_at )`)
      .eq('user_id', user.id)
      .order('joined_at', { ascending: true });

    if (error || !data) return [];

    return data
      .map((row: any) => row.crews)
      .filter(Boolean)
      .map((crew: any) => ({
        ...crew,
        is_owner: crew.owner_id === user.id,
      })) as PermanentCrew[];
  }

  /**
   * Create a new permanent crew and auto-join the creator as member.
   */
  async createPermanentCrew(name: string): Promise<PermanentCrew> {
    const { data: { user } } = await supabase.auth.getUser();
    if (!user) throw new Error('Not authenticated');

    const { data: crew, error: crewErr } = await supabase
      .from('crews')
      .insert({ name, owner_id: user.id })
      .select()
      .single();

    if (crewErr || !crew) throw crewErr ?? new Error('Failed to create crew');

    // Auto-join creator as first member
    await supabase
      .from('crew_memberships')
      .insert({ crew_id: crew.id, user_id: user.id });

    return { ...crew, is_owner: true } as PermanentCrew;
  }

  /**
   * Join a permanent crew by its 6-char invite code.
   */
  async joinPermanentCrew(inviteCode: string): Promise<PermanentCrew> {
    const { data: { user } } = await supabase.auth.getUser();
    if (!user) throw new Error('Not authenticated');

    const { data: crew, error: findErr } = await supabase
      .from('crews')
      .select('*')
      .eq('invite_code', inviteCode.toUpperCase().trim())
      .single();

    if (findErr || !crew) throw new Error('Crew not found — check the invite code');

    const { error: joinErr } = await supabase
      .from('crew_memberships')
      .upsert({ crew_id: crew.id, user_id: user.id }, { onConflict: 'crew_id,user_id' });

    if (joinErr) throw joinErr;

    return { ...crew, is_owner: crew.owner_id === user.id } as PermanentCrew;
  }

  /**
   * Leave a permanent crew (removes membership; doesn't delete crew).
   */
  async leavePermanentCrew(crewId: string): Promise<void> {
    const { data: { user } } = await supabase.auth.getUser();
    if (!user) return;

    await supabase
      .from('crew_memberships')
      .delete()
      .eq('crew_id', crewId)
      .eq('user_id', user.id);
  }

  /**
   * Fetch members of a permanent crew (for display in crew card).
   */
  async getCrewMemberCount(crewId: string): Promise<number> {
    const { count } = await supabase
      .from('crew_memberships')
      .select('id', { count: 'exact', head: true })
      .eq('crew_id', crewId);
    return count ?? 0;
  }

  // ── Push Tokens ──────────────────────────────────────────

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

export const profileService = new ProfileService();
