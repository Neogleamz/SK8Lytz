// Supabase type stub for SK8Lytz
// Permissive stub covering all known tables and RPCs.
// To regenerate with full fidelity: set SUPABASE_ACCESS_TOKEN and run /db-sync.

export type Json =
  | string
  | number
  | boolean
  | null
  | { [key: string]: Json | undefined }
  | Json[];

type AnyRow = Record<string, any>;
type AnyTable = {
  Row: AnyRow;
  Insert: AnyRow;
  Update: AnyRow;
  Relationships: never[];
};
type AnyFn = { Args: Record<string, any>; Returns: any };

export interface Database {
  public: {
    Tables: {
      skate_spots: AnyTable;
      user_profiles: AnyTable;
      app_logs: AnyTable;
      app_settings: AnyTable;
      crews: AnyTable;
      crew_members: AnyTable;
      sessions: AnyTable;
      product_catalog: AnyTable;
      compliance_records: AnyTable;
      [key: string]: AnyTable;
    };
    Views: Record<string, { Row: AnyRow }>;
    Functions: {
      admin_ban_user: AnyFn;
      admin_revoke_ban: AnyFn;
      admin_force_password_reset: AnyFn;
      admin_soft_delete_user: AnyFn;
      register_user: AnyFn;
      get_crew_leaderboard: AnyFn;
      log_app_event: AnyFn;
      upsert_session: AnyFn;
      [key: string]: AnyFn;
    };
    Enums: Record<string, string>;
    CompositeTypes: Record<string, AnyRow>;
  };
}
