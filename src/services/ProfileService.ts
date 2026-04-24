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
  username: string | null;     // added by migration 006
  avatar_url?: string | null;  // added: profile photo in Supabase Storage
  created_at: string;
  updated_at: string;
  is_banned?: boolean;
  ban_reason?: string | null;
  role?: 'user' | 'moderator' | 'admin';
}

export interface PermanentCrew {
  id: string;
  name: string;
  owner_id: string | null;
  invite_code: string;
  created_at: string;
  // Migration 008 enrichment
  is_public?:    boolean;
  avatar_color?: string;
  avatar_icon?:  string;
  avatar_url?:   string | null;
  city?:         string | null;
  state?:        string | null;
  description?:  string | null;
  /** member_count is joined/computed client-side */
  member_count?: number;
  /** true if the current user is the owner */
  is_owner?: boolean;
}

export interface CrewMemberDisplay {
  user_id: string;
  display_name: string | null;
  avatar_color: string;
}

export interface CrewMemberFull {
  membership_id: string;
  user_id: string;
  display_name: string | null;
  avatar_color: string;
  role: 'owner' | 'member';
  joined_at: string;
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
   * Automatically self-heals missing display names or usernames from Auth metadata
   * if the database trigger failed to set them during signup.
   * @param cachedUser Optional pre-fetched user object to avoid redundant network calls
   */
  async fetchOrCreateProfile(cachedUser?: any): Promise<UserProfile | null> {
    let user = cachedUser;
    if (!user) {
      const { data: authData } = await supabase.auth.getUser();
      user = authData.user;
    }
    
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
  }

  /**
   * Update display name and/or avatar color for the current user.
   */
  async updateProfile(fields: { display_name?: string | null; avatar_color?: string; username?: string | null; avatar_url?: string | null }): Promise<void> {
    const { data: { user } } = await supabase.auth.getUser();
    if (!user) throw new Error('Not authenticated');

    // Strip null/undefined values to avoid overwriting with null
    const cleanFields: Record<string, any> = {};
    if (fields.display_name != null) cleanFields.display_name = fields.display_name;
    if (fields.avatar_color != null) cleanFields.avatar_color = fields.avatar_color;
    if (fields.username != null) cleanFields.username = fields.username.toLowerCase();
    if (fields.avatar_url != null) cleanFields.avatar_url = fields.avatar_url;

    const { error } = await supabase
      .from('user_profiles')
      .upsert({ user_id: user.id, ...cleanFields }, { onConflict: 'user_id' });

    if (error) throw error;
  }

  // ── Session History ──────────────────────────────────────

  /**
   * Return the last 20 crew sessions the current user was part of.
   * @param cachedUserId Optional pre-fetched user ID to avoid redundant network calls
   */
  async getSessionHistory(cachedUserId?: string): Promise<SessionHistoryItem[]> {
    let userId = cachedUserId;
    if (!userId) {
      const { data: { user } } = await supabase.auth.getUser();
      if (!user) return [];
      userId = user.id;
    }

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

    return data.map((row: any) => {
      const session = row.crew_sessions;
      return {
        session_id:   session?.id ?? '',
        session_name: session?.name ?? 'Crew Session',
        crew_name:    session?.crews?.name ?? null,
        role:         session?.leader_user_id === userId ? 'leader' : 'member',
        joined_at:    row.joined_at,
        expires_at:   session?.expires_at ?? '',
      } as SessionHistoryItem;
    });
  }

  // ── Permanent Crews ──────────────────────────────────────

  /**
   * List all permanent crews the current user is a member of.
   * Delta Sync: Optionally supply updatedSince to limit fetching.
   * @param updatedSince Timestamp string for delta sync
   * @param cachedUserId Optional pre-fetched user ID to avoid redundant network calls
   */
  async getMyCrew(updatedSince?: string, cachedUserId?: string): Promise<PermanentCrew[]> {
    let userId = cachedUserId;
    if (!userId) {
      const { data: { user } } = await supabase.auth.getUser();
      if (!user) return [];
      userId = user.id;
    }

    const query = supabase
      .from('crew_memberships')
      // Note: For delta sync on inner joins, we'd apply it on the root table or standard table
      // To preserve stability without custom RPC, we perform standard select and filter locally.
      .select(`crews ( id, name, owner_id, invite_code, created_at, updated_at, is_public, avatar_color, avatar_icon, avatar_url, city, state, description )`)
      .eq('user_id', userId);

    const { data, error } = await query.order('joined_at', { ascending: true });

    if (error || !data) return [];

    const crews = data
      .map((row: any) => row.crews)
      .filter(Boolean);
      
    const filteredCrews = updatedSince 
      ? crews.filter((c: any) => c.updated_at && (new Date(c.updated_at) > new Date(updatedSince)))
      : crews;

    return filteredCrews.map((crew: any) => ({
      ...crew,
      is_owner: crew.owner_id === userId,
    })) as PermanentCrew[];
  }

  /**
   * Create a new permanent crew and auto-join the creator as member.
   */
  async createPermanentCrew(name: string, opts?: { isPublic?: boolean; avatarColor?: string; avatarIcon?: string; city?: string; state?: string; description?: string; inviteCode?: string; members?: string[] }): Promise<PermanentCrew> {
    const { data: { user } } = await supabase.auth.getUser();
    if (!user) throw new Error('Not authenticated');

    const insertData: any = { name, owner_id: user.id };
    if (opts?.isPublic    !== undefined) insertData.is_public    = opts.isPublic;
    if (opts?.avatarColor)               insertData.avatar_color = opts.avatarColor;
    if (opts?.avatarIcon)                insertData.avatar_icon  = opts.avatarIcon;
    if (opts?.city)                      insertData.city         = opts.city;
    if (opts?.state)                     insertData.state        = opts.state;
    if (opts?.description)               insertData.description  = opts.description;
    if (opts?.inviteCode)                insertData.invite_code  = opts.inviteCode;

    const { data: crew, error: crewErr } = await supabase
      .from('crews')
      .insert(insertData)
      .select()
      .single();

    if (crewErr || !crew) throw crewErr ?? new Error('Failed to create crew');

    // Auto-join creator as first member
    const memberships = [{ crew_id: crew.id, user_id: user.id }];
    
    // Add additional members if provided
    if (opts?.members && opts.members.length > 0) {
      opts.members.forEach(memberId => {
        if (memberId !== user.id) {
          memberships.push({ crew_id: crew.id, user_id: memberId });
        }
      });
    }

    await supabase
      .from('crew_memberships')
      .insert(memberships);

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
   * Join a PUBLIC permanent crew directly by its crew ID (no invite code needed).
   * Only works if is_public = true. Throws if crew is private or not found.
   */
  async joinPublicCrewById(crewId: string): Promise<PermanentCrew> {
    const { data: { user } } = await supabase.auth.getUser();
    if (!user) throw new Error('Not authenticated');

    const { data: crew, error: findErr } = await supabase
      .from('crews')
      .select('*')
      .eq('id', crewId)
      .eq('is_public', true)
      .single();

    if (findErr || !crew) throw new Error('Crew not found or is private');

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

  /**
   * Get member count + avatar colors for display (privacy-safe — no names).
   */
  async getCrewMembersForDisplay(crewId: string): Promise<{ count: number; avatarColors: string[] }> {
    const { data, error } = await supabase
      .from('crew_memberships')
      .select('user_profiles ( avatar_color )')
      .eq('crew_id', crewId)
      .limit(12);

    if (error || !data) return { count: 0, avatarColors: [] };
    const colors = data
      .map((row: any) => row.user_profiles?.avatar_color)
      .filter(Boolean) as string[];
    const { count } = await supabase
      .from('crew_memberships')
      .select('id', { count: 'exact', head: true })
      .eq('crew_id', crewId);
    return { count: count ?? colors.length, avatarColors: colors };
  }

  /**
   * Update crew details — owner only.
   */
  async updateCrew(crewId: string, fields: {
    name?: string; isPublic?: boolean; avatarColor?: string;
    avatarIcon?: string; avatarUrl?: string | null;
    city?: string; state?: string; description?: string;
  }): Promise<void> {
    const { data: { user } } = await supabase.auth.getUser();
    if (!user) throw new Error('Not authenticated');

    const updates: any = {};
    if (fields.name        !== undefined) updates.name         = fields.name;
    if (fields.isPublic    !== undefined) updates.is_public    = fields.isPublic;
    if (fields.avatarColor !== undefined) updates.avatar_color = fields.avatarColor;
    if (fields.avatarIcon  !== undefined) updates.avatar_icon  = fields.avatarIcon;
    if (fields.avatarUrl   !== undefined) updates.avatar_url   = fields.avatarUrl;
    if (fields.city        !== undefined) updates.city         = fields.city;
    if (fields.state       !== undefined) updates.state        = fields.state;
    if (fields.description !== undefined) updates.description  = fields.description;

    const { error } = await supabase
      .from('crews')
      .update(updates)
      .eq('id', crewId)
      .eq('owner_id', user.id);  // owner-only guard

    if (error) throw error;
  }

  /**
   * Fetch all public crews (for Discover tab), optionally filtered.
   */
  async getPublicCrews(): Promise<PermanentCrew[]> {
    const { data, error } = await supabase
      .from('crews')
      .select('id, name, owner_id, invite_code, created_at, is_public, avatar_color, avatar_icon, avatar_url, city, state, description')
      .eq('is_public', true)
      .order('created_at', { ascending: false })
      .limit(50);

    if (error || !data) return [];
    const { data: { user } } = await supabase.auth.getUser();
    return data.map((crew: any) => ({ ...crew, is_owner: crew.owner_id === user?.id })) as PermanentCrew[];
  }

  /**
   * Delete a crew — owner only. Cascades memberships via DB FK.
   * Safely drops any active Realtime sessions tied to this crew before deletion.
   */
  async deleteCrew(crewId: string): Promise<void> {
    const { data: { user } } = await supabase.auth.getUser();
    if (!user) throw new Error('Not authenticated');

    // 1. Proactively end any active sessions for this crew so users aren't left in a ghost state
    const { data: activeSessions } = await supabase
      .from('crew_sessions')
      .select('id')
      .eq('crew_id', crewId)
      .eq('is_active', true);

    if (activeSessions && activeSessions.length > 0) {
      for (const s of activeSessions) {
        // Broadcast session_ended to force all connected clients to revert to solo mode
        const tempChannel = supabase.channel(`crew:${s.id}`, { config: { broadcast: { self: true } } });
        await tempChannel.subscribe(async (status) => {
          if (status === 'SUBSCRIBED') {
            await tempChannel.send({
              type: 'broadcast',
              event: 'session_ended',
              payload: { sessionId: s.id },
            });
            setTimeout(() => supabase.removeChannel(tempChannel), 500);
          }
        });

        // Mark the session as ended in the database
        await supabase
          .from('crew_sessions')
          .update({
            is_active: false,
            status: 'ended',
            ended_at: new Date().toISOString(),
          })
          .eq('id', s.id);
      }
    }

    // 2. Delete the crew
    const { error } = await supabase
      .from('crews')
      .delete()
      .eq('id', crewId);

    if (error) throw error;
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

  // ── Crew Stats ─────────────────────────────────────────────

  /**
   * Fetch session stats for a permanent crew.
   * Returns total sessions, last active timestamp, and most-used scene name.
   */
  async getCrewStats(crewId: string): Promise<{
    sessionCount: number;
    lastActive: string | null;
    topScene: string | null;
  }> {
    const { data, error } = await supabase
      .from('crew_sessions')
      .select('id, created_at, ended_at, last_scene')
      .eq('crew_id', crewId)
      .order('created_at', { ascending: false });

    if (error || !data) return { sessionCount: 0, lastActive: null, topScene: null };

    const sessionCount = data.length;
    const lastActive = data[0]?.ended_at ?? data[0]?.created_at ?? null;

    // Tally scene names from last_scene JSONB { modeName, patternName, etc. }
    const tally: Record<string, number> = {};
    for (const s of data) {
      const scene = s.last_scene as any;
      const label: string | undefined =
        scene?.modeName ?? scene?.patternName ?? scene?.activeMode ?? scene?.mode;
      if (label) tally[label] = (tally[label] ?? 0) + 1;
    }
    const topScene = Object.keys(tally).length
      ? Object.keys(tally).reduce((a, b) => tally[a] > tally[b] ? a : b)
      : null;

    return { sessionCount, lastActive, topScene };
  }
  /**
   * Fetch all members of a permanent crew with their display names and roles.
   * Used to render the expandable member list on the hub crew card.
   */
  async getCrewMembersWithNames(crewId: string): Promise<CrewMemberFull[]> {
    const { data, error } = await supabase
      .from('crew_memberships')
      .select(`
        id,
        user_id,
        role,
        joined_at,
        user_profiles ( display_name, avatar_color )
      `)
      .eq('crew_id', crewId)
      .order('role', { ascending: false })   // owners first
      .order('joined_at', { ascending: true });

    if (error || !data) return [];

    return data.map((row: any) => ({
      membership_id: row.id,
      user_id:      row.user_id,
      display_name: row.user_profiles?.display_name ?? null,
      avatar_color: row.user_profiles?.avatar_color ?? '#888',
      role:         (row.role ?? 'member') as 'owner' | 'member',
      joined_at:    row.joined_at,
    })) as CrewMemberFull[];
  }

  /**
   * Assign owner role to a crew member (multi-owner support — requires migration 009).
   * Only existing owners can call this.
   */
  async assignCrewOwner(crewId: string, targetUserId: string): Promise<void> {
    const { error } = await supabase
      .from('crew_memberships')
      .update({ role: 'owner' })
      .eq('crew_id', crewId)
      .eq('user_id', targetUserId);

    if (error) throw error;
  }

  /**
   * Revoke owner role from a crew member (demote to regular member).
   * Only other owners can call this; cannot demote yourself if sole owner.
   */
  async revokeCrewOwner(crewId: string, targetUserId: string): Promise<void> {
    const { error } = await supabase
      .from('crew_memberships')
      .update({ role: 'member' })
      .eq('crew_id', crewId)
      .eq('user_id', targetUserId);

    if (error) throw error;
  }

  /**
   * Remove a member from a crew.
   * Only owners can call this.
   */
  async removeCrewMember(crewId: string, targetUserId: string): Promise<void> {
    const { error } = await supabase
      .from('crew_memberships')
      .delete()
      .eq('crew_id', crewId)
      .eq('user_id', targetUserId);

    if (error) throw error;
  }

  /**
   * Add multiple members to an existing crew.
   */
  async addCrewMembers(crewId: string, userIds: string[]): Promise<void> {
    if (!userIds.length) return;
    const memberships = userIds.map(id => ({ crew_id: crewId, user_id: id }));
    
    const { error } = await supabase
      .from('crew_memberships')
      .insert(memberships);

    if (error) throw error;
  }

  async searchUsers(query: string): Promise<{user_id: string, username: string | null, display_name: string | null}[]> {
    if (!query.trim()) return [];
    const searchTerms = `%${query.trim()}%`;
    const { data, error } = await supabase
      .from('user_profiles')
      .select('user_id, username, display_name')
      .or(`username.ilike.${searchTerms},display_name.ilike.${searchTerms}`)
      .limit(10);
    if (error) {
      console.warn('Search users error:', error);
      return [];
    }
    return data ?? [];
  }
}

export const profileService = new ProfileService();
