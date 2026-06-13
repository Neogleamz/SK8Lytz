/**
 * CrewProfileService — Permanent crew management, stats, and member operations
 *
 * Owns: getMyCrew, createPermanentCrew, joinPermanentCrew, joinPublicCrewById,
 *       leavePermanentCrew, getCrewMemberCount, getCrewMembersForDisplay,
 *       updateCrew, getPublicCrews, deleteCrew, getCrewStats,
 *       getCrewMembersWithNames, assignCrewOwner, revokeCrewOwner,
 *       removeCrewMember, addCrewMembers, searchUsers
 *
 * Part of epic/god-object-decomposition — Meal 1: ProfileService split.
 */

import { supabase } from './supabaseClient';
import type { Database } from '../types/supabase';
import type { PermanentCrew, CrewMemberDisplay, CrewMemberFull } from './ProfileService.types';
import { AppLogger } from './AppLogger';

class CrewProfileService {

  // ── Permanent Crews ──────────────────────────────────────

  /**
   * List all permanent crews the current user is a member of.
   * Delta Sync: Optionally supply updatedSince to limit fetching.
   * @param updatedSince Timestamp string for delta sync
   * @param cachedUserId Optional pre-fetched user ID to avoid redundant network calls
   */
  async getMyCrew(updatedSince: string | undefined, userId: string): Promise<PermanentCrew[]> {
    try {
      if (!userId) return [];

      const query = supabase
        .from('crew_memberships')
        // Note: For delta sync on inner joins, we'd apply it on the root table or standard table
        // To preserve stability without custom RPC, we perform standard select and filter locally.
        .select(`crews ( id, name, owner_id, invite_code, created_at, updated_at, is_public, avatar_color, avatar_icon, avatar_url, city, state, description )`)
        .eq('user_id', userId);

      const { data, error } = await query.order('joined_at', { ascending: true });

      if (error || !data) return [];

      const crews = data
        .map((row: unknown) => (row as { crews: unknown }).crews)
        .filter((c) => c !== null);
        
      const filteredCrews = updatedSince 
        ? crews.filter((c: unknown) => { const cr = c as { updated_at: string }; return cr.updated_at && (new Date(cr.updated_at) > new Date(updatedSince)); })
        : crews;

      return filteredCrews.map((crew: unknown) => {
        const c = crew as PermanentCrew & { owner_id: string };
        return {
          ...c,
          is_owner: c.owner_id === userId,
        } as PermanentCrew;
      });
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewProfileService] getMyCrew failed', { error: msg , payload_size: 0, ssi: 0 });
      return [];
    }
  }

  /**
   * Create a new permanent crew and auto-join the creator as member.
   */
  async createPermanentCrew(name: string, opts: { isPublic?: boolean; avatarColor?: string; avatarIcon?: string; avatarUrl?: string | null; city?: string; state?: string; description?: string; inviteCode?: string; members?: string[] } | undefined, userId: string): Promise<PermanentCrew> {
    try {
      if (!userId) throw new Error('Not authenticated');

      const insertData = { name, owner_id: userId } as Record<string, unknown>;
      if (opts?.isPublic    !== undefined) insertData.is_public    = opts.isPublic;
      if (opts?.avatarColor)               insertData.avatar_color = opts.avatarColor;
      if (opts?.avatarIcon)                insertData.avatar_icon  = opts.avatarIcon;
      if (opts?.avatarUrl !== undefined)   insertData.avatar_url   = opts.avatarUrl;
      if (opts?.city)                      insertData.city         = opts.city;
      if (opts?.state)                     insertData.state        = opts.state;
      if (opts?.description)               insertData.description  = opts.description;
      if (opts?.inviteCode)                insertData.invite_code  = opts.inviteCode;

      const { data: crew, error: crewErr } = await supabase
        .from('crews')
        .insert(insertData as Database['public']['Tables']['crews']['Insert'])
        .select()
        .single();

      if (crewErr || !crew) throw crewErr ?? new Error('Failed to create crew');

      // Auto-join creator as first member
      const memberships = [{ crew_id: crew.id, user_id: userId, role: 'owner' }];
      
      // Add additional members if provided
      if (opts?.members && opts.members.length > 0) {
        opts.members.forEach(memberId => {
          if (memberId !== userId) {
            memberships.push({ crew_id: crew.id, user_id: memberId, role: 'member' });
          }
        });
      }

      await supabase
        .from('crew_memberships')
        .insert(memberships);

      return { ...crew, is_owner: true } as PermanentCrew;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewProfileService] createPermanentCrew failed', { error: msg , payload_size: 0, ssi: 0 });
      throw new Error(msg);
    }
  }

  /**
   * Join a permanent crew by its 6-char invite code.
   */
  async joinPermanentCrew(inviteCode: string, userId: string): Promise<PermanentCrew> {
    try {
      if (!userId) throw new Error('Not authenticated');

      const { data: crew, error: findErr } = await supabase
        .from('crews')
        .select('*')
        .eq('invite_code', inviteCode.toUpperCase().trim())
        .single();

      if (findErr || !crew) throw new Error('Crew not found — check the invite code');

      const { error: joinErr } = await supabase
        .from('crew_memberships')
        .upsert({ crew_id: crew.id, user_id: userId }, { onConflict: 'crew_id,user_id' });

      if (joinErr) throw joinErr;

      return { ...crew, is_owner: crew.owner_id === userId } as PermanentCrew;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewProfileService] joinPermanentCrew failed', { error: msg , payload_size: 0, ssi: 0 });
      throw new Error(msg);
    }
  }

  /**
   * Join a PUBLIC permanent crew directly by its crew ID (no invite code needed).
   * Only works if is_public = true. Throws if crew is private or not found.
   */
  async joinPublicCrewById(crewId: string, userId: string): Promise<PermanentCrew> {
    try {
      if (!userId) throw new Error('Not authenticated');

      const { data: crew, error: findErr } = await supabase
        .from('crews')
        .select('*')
        .eq('id', crewId)
        .eq('is_public', true)
        .single();

      if (findErr || !crew) throw new Error('Crew not found or is private');

      const { error: joinErr } = await supabase
        .from('crew_memberships')
        .upsert({ crew_id: crew.id, user_id: userId }, { onConflict: 'crew_id,user_id' });

      if (joinErr) throw joinErr;

      return { ...crew, is_owner: crew.owner_id === userId } as PermanentCrew;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewProfileService] joinPublicCrewById failed', { error: msg , payload_size: 0, ssi: 0 });
      throw new Error(msg);
    }
  }

  /**
   * Leave a permanent crew (removes membership; doesn't delete crew).
   */
  async leavePermanentCrew(crewId: string, userId: string): Promise<void> {
    try {
      if (!userId) return;

      await supabase
        .from('crew_memberships')
        .delete()
        .eq('crew_id', crewId)
        .eq('user_id', userId);
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewProfileService] leavePermanentCrew failed', { error: msg , payload_size: 0, ssi: 0 });
    }
  }

  /**
   * Fetch member count of a permanent crew.
   */
  async getCrewMemberCount(crewId: string): Promise<number> {
    try {
      const { count } = await supabase
        .from('crew_memberships')
        .select('id', { count: 'exact', head: true })
        .eq('crew_id', crewId);
      return count ?? 0;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewProfileService] getCrewMemberCount failed', { error: msg , payload_size: 0, ssi: 0 });
      return 0;
    }
  }

  /**
   * Get member count + avatar colors for display (privacy-safe — no names).
   */
  async getCrewMembersForDisplay(crewId: string): Promise<{ count: number; avatarColors: string[] }> {
    try {
      const { data, error } = await supabase
        .from('crew_memberships')
        .select('user_profiles ( avatar_color )')
        .eq('crew_id', crewId)
        .limit(12);

      if (error || !data) return { count: 0, avatarColors: [] };
      const colors = data
        .map((row: unknown) => (row as { user_profiles: { avatar_color: string | null } | null }).user_profiles?.avatar_color)
        .filter(Boolean) as string[];
      const { count } = await supabase
        .from('crew_memberships')
        .select('id', { count: 'exact', head: true })
        .eq('crew_id', crewId);
      return { count: count ?? colors.length, avatarColors: colors };
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewProfileService] getCrewMembersForDisplay failed', { error: msg , payload_size: 0, ssi: 0 });
      return { count: 0, avatarColors: [] };
    }
  }

  /**
   * Update crew details — owner only.
   */
  async updateCrew(crewId: string, fields: {
    name?: string; isPublic?: boolean; avatarColor?: string;
    avatarIcon?: string; avatarUrl?: string | null;
    city?: string; state?: string; description?: string;
  }, userId: string): Promise<void> {
    try {
      if (!userId) throw new Error('Not authenticated');

      const updates = {} as Record<string, unknown>;
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
        .update(updates as Database['public']['Tables']['crews']['Update'])
        .eq('id', crewId)
        .eq('owner_id', userId);  // owner-only guard

      if (error) throw error;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewProfileService] updateCrew failed', { error: msg , payload_size: 0, ssi: 0 });
      throw new Error(msg);
    }
  }

  /**
   * Fetch all public crews (for Discover tab), optionally filtered.
   */
  async getPublicCrews(userId?: string): Promise<PermanentCrew[]> {
    try {
      const { data, error } = await supabase
        .from('crews')
        .select('id, name, owner_id, invite_code, created_at, is_public, avatar_color, avatar_icon, avatar_url, city, state, description')
        .eq('is_public', true)
        .order('created_at', { ascending: false })
        .limit(50);

      if (error || !data) return [];
      
      return data.map((crew: unknown) => {
        const c = crew as PermanentCrew & { owner_id: string };
        return { ...c, is_owner: c.owner_id === userId } as PermanentCrew;
      });
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewProfileService] getPublicCrews failed', { error: msg , payload_size: 0, ssi: 0 });
      return [];
    }
  }

  /**
   * Delete a crew — owner only. Cascades memberships via DB FK.
   * Safely drops any active Realtime sessions tied to this crew before deletion.
   */
  async deleteCrew(crewId: string, userId: string): Promise<void> {
    try {
      if (!userId) throw new Error('Not authenticated');

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
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewProfileService] deleteCrew failed', { error: msg , payload_size: 0, ssi: 0 });
      throw new Error(msg);
    }
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
    totalDistanceMiles: number;
    avgSpeedMph: number;
    peakSpeedMph: number;
    peakGForce: number;
    totalDurationSec: number;
  }> {
    try {
      const { data, error } = await supabase
        .from('crew_sessions')
        .select('id, created_at, ended_at, last_scene')
        .eq('crew_id', crewId)
        .order('created_at', { ascending: false });

      if (error || !data) return { sessionCount: 0, lastActive: null, topScene: null, totalDistanceMiles: 0, avgSpeedMph: 0, peakSpeedMph: 0, peakGForce: 0, totalDurationSec: 0 };

      const sessionCount = data.length;
      const lastActive = data[0]?.ended_at ?? data[0]?.created_at ?? null;

      // Tally scene names from last_scene JSONB { modeName, patternName, etc. }
      const tally: Record<string, number> = {};
      for (const s of data) {
        const scene = s.last_scene as Record<string, unknown> | null;
        const label: string | undefined =
          (typeof scene?.modeName === 'string' ? scene.modeName : undefined) ??
          (typeof scene?.patternName === 'string' ? scene.patternName : undefined) ??
          (typeof scene?.activeMode === 'string' ? scene.activeMode : undefined) ??
          (typeof scene?.mode === 'string' ? scene.mode : undefined);
        if (label) tally[label] = (tally[label] ?? 0) + 1;
      }
      const topScene = Object.keys(tally).length
        ? Object.keys(tally).reduce((a, b) => tally[a] > tally[b] ? a : b)
        : null;

      let totalDistanceMiles = 0;
      let avgSpeedMph = 0;
      let peakSpeedMph = 0;
      let peakGForce = 0;
      let totalDurationSec = 0;

      const sessionIds = data.map(s => s.id);
      if (sessionIds.length > 0) {
        const { data: skateData } = await supabase
          .from('skate_sessions')
          .select('distance_miles, duration_sec, avg_speed_mph, peak_speed_mph, peak_gforce')
          .in('crew_session_id', sessionIds);

        if (skateData && skateData.length > 0) {
          totalDistanceMiles = skateData.reduce((s: number, r: unknown) => s + Number((r as {distance_miles: number}).distance_miles ?? 0), 0);
          totalDurationSec = skateData.reduce((s: number, r: unknown) => s + Number((r as {duration_sec: number}).duration_sec ?? 0), 0);
          peakSpeedMph = Math.max(0, ...skateData.map((r: unknown) => Number((r as {peak_speed_mph: number}).peak_speed_mph ?? 0)));
          peakGForce = Math.max(0, ...skateData.map((r: unknown) => Number((r as {peak_gforce: number}).peak_gforce ?? 0)));
          avgSpeedMph = skateData.reduce((s: number, r: unknown) => s + Number((r as {avg_speed_mph: number}).avg_speed_mph ?? 0), 0) / skateData.length;
        }
      }

      return { 
        sessionCount, 
        lastActive, 
        topScene,
        totalDistanceMiles: parseFloat(totalDistanceMiles.toFixed(2)),
        avgSpeedMph: parseFloat(avgSpeedMph.toFixed(1)),
        peakSpeedMph: parseFloat(peakSpeedMph.toFixed(1)),
        peakGForce: parseFloat(peakGForce.toFixed(1)),
        totalDurationSec
      };
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewProfileService] getCrewStats failed', { error: msg , payload_size: 0, ssi: 0 });
      return { sessionCount: 0, lastActive: null, topScene: null, totalDistanceMiles: 0, avgSpeedMph: 0, peakSpeedMph: 0, peakGForce: 0, totalDurationSec: 0 };
    }
  }

  /**
   * Fetch all members of a permanent crew with their display names and roles.
   * Used to render the expandable member list on the hub crew card.
   */
  async getCrewMembersWithNames(crewId: string): Promise<CrewMemberFull[]> {
    try {
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

      return data.map((row: unknown) => {
        const r = row as { id: string; user_id: string; role: string; joined_at: string; user_profiles: { display_name: string | null; avatar_color: string | null } | null };
        return {
          membership_id: r.id,
          user_id:      r.user_id,
          display_name: r.user_profiles?.display_name ?? null,
          avatar_color: r.user_profiles?.avatar_color ?? '#888',
          role:         (r.role ?? 'member') as 'owner' | 'member',
          joined_at:    r.joined_at,
        } as CrewMemberFull;
      });
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewProfileService] getCrewMembersWithNames failed', { error: msg , payload_size: 0, ssi: 0 });
      return [];
    }
  }

  /**
   * Assign owner role to a crew member (multi-owner support — requires migration 009).
   * Only existing owners can call this.
   */
  async assignCrewOwner(crewId: string, targetUserId: string): Promise<void> {
    try {
      const { error } = await supabase
        .from('crew_memberships')
        .update({ role: 'owner' })
        .eq('crew_id', crewId)
        .eq('user_id', targetUserId);

      if (error) throw error;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewProfileService] assignCrewOwner failed', { error: msg, payload_size: 0, ssi: 0 });
      throw new Error(msg);
    }
  }

  /**
   * Revoke owner role from a crew member (demote to regular member).
   * Only other owners can call this; cannot demote yourself if sole owner.
   */
  async revokeCrewOwner(crewId: string, targetUserId: string): Promise<void> {
    try {
      const { error } = await supabase
        .from('crew_memberships')
        .update({ role: 'member' })
        .eq('crew_id', crewId)
        .eq('user_id', targetUserId);

      if (error) throw error;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewProfileService] revokeCrewOwner failed', { error: msg , payload_size: 0, ssi: 0 });
      throw new Error(msg);
    }
  }

  /**
   * Remove a member from a crew.
   * Only owners can call this.
   */
  async removeCrewMember(crewId: string, targetUserId: string): Promise<void> {
    try {
      const { error } = await supabase
        .from('crew_memberships')
        .delete()
        .eq('crew_id', crewId)
        .eq('user_id', targetUserId);

      if (error) throw error;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewProfileService] removeCrewMember failed', { error: msg , payload_size: 0, ssi: 0 });
      throw new Error(msg);
    }
  }

  /**
   * Add multiple members to an existing crew.
   */
  async addCrewMembers(crewId: string, userIds: string[]): Promise<void> {
    try {
      if (!userIds.length) return;
      const memberships = userIds.map(id => ({ crew_id: crewId, user_id: id, role: 'member' }));
      
      const { error } = await supabase
        .from('crew_memberships')
        .insert(memberships);

      if (error) throw error;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewProfileService] addCrewMembers failed', { error: msg , payload_size: 0, ssi: 0 });
      throw new Error(msg);
    }
  }

  /**
   * Hand off session leadership.
   * Promotes new leader and demotes old leader (unless they are the owner).
   */
  async transferSessionLeadership(crewId: string, newLeaderUserId: string, oldLeaderUserId: string): Promise<void> {
    try {
      const { error: err1 } = await supabase
        .from('crew_memberships')
        .update({ role: 'leader' })
        .eq('crew_id', crewId)
        .eq('user_id', newLeaderUserId);
      if (err1) throw err1;

      const { error: err2 } = await supabase
        .from('crew_memberships')
        .update({ role: 'member' })
        .eq('crew_id', crewId)
        .eq('user_id', oldLeaderUserId)
        .neq('role', 'owner');
      if (err2) throw err2;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewProfileService] transferSessionLeadership failed', { error: msg, payload_size: 0, ssi: 0 });
      throw new Error(msg);
    }
  }

  /**
   * Search users by username or display_name (partial match).
   */
  async searchUsers(query: string): Promise<{user_id: string, username: string | null, display_name: string | null}[]> {
    try {
      if (!query.trim()) return [];
      const searchTerms = `%${query.trim()}%`;
      const { data, error } = await supabase
        .from('user_profiles')
        .select('user_id, username, display_name')
        .or(`username.ilike.${searchTerms},display_name.ilike.${searchTerms}`)
        .limit(10);
      if (error) {
        AppLogger.warn('CREW_PROFILE', { event: 'search_users_failed', query: '[REDACTED]', error: String(error) });
        return [];
      }
      return data ?? [];
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewProfileService] searchUsers failed', { error: msg , payload_size: 0, ssi: 0 });
      return [];
    }
  }
}

export const crewProfileService = new CrewProfileService();
