# PLAN-chore-audit-supabase-security: Supabase Security and RLS Hardening

Hardening the database security posture for the SK8Lytz application by securing PL/pgSQL function search paths, dropping permissive RLS policies, fixing the broken administrative read access for telemetry logs, and enabling RLS on spatial tables—while explicitly respecting system boundaries to avoid collateral damage to other apps (like the Global Command Center and Inventory apps).

## User Review Required

We have carefully isolated the scope based on our collaborative review:
*   **DO NOT TOUCH**: `product_catalog` (Finding 4 is excluded as it is managed by the inventory app).
*   **DO NOT TOUCH**: Any tables or functions associated with the logistics/manufacturing system (`raw_orders`, `production_sops`, `inventory_snapshots`, `app_settings`, etc.).
*   **SK8Lytz Functions (Finding 1)**: We will secure all SK8Lytz PL/pgSQL functions. For the legacy scraper functions (`get_databank_coverage`, `get_next_spot_for_indexer`, `get_next_spot_for_operator`), since we migrated scraper logic to localdb, securing their search paths with `SET search_path = public` on Supabase remains 100% safe and guarantees they won't execute unauthorized code if called.

---

## Proposed Database Migrations

All SQL commands will be bundled into a fresh, chronological migration file:
`supabase/migrations/20260526190000_supabase_security_hardening.sql`.

### [Component: PL/pgSQL Function Hardening]
Secures 11 functions against mutable search path vulnerabilities by pinning their search paths.

#### [NEW] [20260526190000_supabase_security_hardening.sql](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/supabase/migrations/20260526190000_supabase_security_hardening.sql)

```sql
-- Secure SK8Lytz administrative and trigger functions
ALTER FUNCTION public.admin_add_hardware_blacklist(text, text) SET search_path = public;
ALTER FUNCTION public.admin_export_user_data(uuid) SET search_path = public;
ALTER FUNCTION public.admin_get_global_telemetry() SET search_path = public;
ALTER FUNCTION public.admin_remove_hardware_blacklist(text) SET search_path = public;
ALTER FUNCTION public.admin_revoke_admin_role(uuid) SET search_path = public;
ALTER FUNCTION public.admin_revoke_sessions(uuid) SET search_path = public, auth;
ALTER FUNCTION public.handle_auto_promotion() SET search_path = public;
ALTER FUNCTION public.handle_new_user() SET search_path = public;

-- Secure legacy/ETL scraper helper functions (100% backward-compatible)
ALTER FUNCTION public.get_databank_coverage() SET search_path = public;
ALTER FUNCTION public.get_next_spot_for_indexer(text[]) SET search_path = public;
ALTER FUNCTION public.get_next_spot_for_operator(text[]) SET search_path = public;
```

---

### [Component: Row Level Security Hardening]
Fixes broken, permissive, and disabled RLS policies across the SK8Lytz table registry.

```sql
-- Finding 2: Fix broken SELECT policy on discovered_devices_telemetry for admins
DROP POLICY IF EXISTS "Only admins can select telemetry" ON public.discovered_devices_telemetry;
CREATE POLICY "Only admins can select telemetry" ON public.discovered_devices_telemetry
  FOR SELECT
  TO authenticated
  USING (
    EXISTS (
      SELECT 1 FROM public.user_profiles
      WHERE user_profiles.user_id = auth.uid()
        AND (role = 'admin'::public.user_role OR role = 'moderator'::public.user_role)
    )
  );

-- Finding 3: Drop the permissive authenticated_full_access policy on sk8lytz_app_settings
DROP POLICY IF EXISTS "authenticated_full_access" ON public.sk8lytz_app_settings;

-- Finding 5: Drop anonymous write policies on skate_spots (Scraper uses service_role key)
DROP POLICY IF EXISTS "Allow public insert for scraper" ON public.skate_spots;
DROP POLICY IF EXISTS "Allow public update for scraper" ON public.skate_spots;

-- Finding 6: Enable RLS on spatial_ref_sys PostGIS system table
ALTER TABLE public.spatial_ref_sys ENABLE ROW LEVEL SECURITY;
```

---

## Verification Plan

### Automated Tests
1. **Migration Application**:
   Apply the migration safely using the Supabase MCP server tools:
   * Execute the hardened SQL commands using `execute_sql` tool.
2. **Security Advisor Scan**:
   * Run the Supabase security linter tool `get_advisors` with `type: 'security'` after applying the migrations to verify that these findings have been successfully cleared.
3. **Local Checks**:
   * Run `npm run verify` in the isolated worktree to ensure there are no compilation errors or broken Jest test suites.

### Manual Verification
1. **Admin Telemetry Select Test**:
   * Run a mock query against `discovered_devices_telemetry` masquerading as an admin to verify they can successfully query the table under the new SELECT policy.
2. **Settings Access Test**:
   * Confirm that non-admin authenticated users are blocked from writing or updating the `sk8lytz_app_settings` table.
