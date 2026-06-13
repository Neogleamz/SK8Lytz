/**
 * ProfileService.types.ts — Shared type contracts for the Profile domain
 *
 * All interfaces live here to prevent circular imports between the three
 * focused service files (AuthProfileService, CrewProfileService, PushTokenService).
 *
 * Part of epic/god-object-decomposition — Meal 1: ProfileService split.
 */

export interface NotifPreferences {
  mute?: boolean;
  push?: boolean;
  email?: boolean;
  crewInvites?: boolean;
  sessionReminders?: boolean;
  leaderHandoff?: boolean;
  [key: string]: boolean | undefined;
}

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
  notif_preferences?: NotifPreferences;
  accepted_eula_version?: number;
  lifetime_distance_miles?: number;
  lifetime_top_speed_mph?: number;
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
