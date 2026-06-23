import type { Tables } from '../../types/supabase';

// Use the generated Supabase Row type directly — stays in sync with schema automatically.
// member_count is injected dynamically by fetchActiveSessions via a join on crew_members(count).
export type CrewSession = Tables<'crew_sessions'> & {
  member_count?: number;
};

export interface CrewMember {
  id: string;
  session_id: string;
  user_id: string;
  display_name: string;
  joined_at: string;
}

export interface CrewScenePayload {
  payload: number[];
  leader_id: string;
  ts: number;
}

export type CrewRole = 'leader' | 'member' | null;

export interface SessionTelemetryData {
  distanceMiles: number;
  topSpeedMph: number;
  avgSpeedSamples: number[];
}
