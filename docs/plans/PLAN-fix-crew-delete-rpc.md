# Implementation Plan

## Goal
Create a separate `delete_crew` Supabase RPC that hard-deletes a crew and removes all member associations, distinct from `leave_crew`.

## Source of Truth
- `src/components/account/AccountModal.tsx`:205-211 — crew delete UI trigger
- `src/hooks/useAccountOverview.ts`:268 — delete path currently calls `leavePermanentCrew()` regardless of ownership
- `src/services/AuthProfileService.ts` — `leavePermanentCrew()` and `createPermanentCrew()` implementations — reveal `crews` and `crew_memberships` table schema
- `src/types/supabase.ts` — `crews` and `crew_memberships` table definitions — exact column names for SQL

## Steps

### Step 1 — Read AuthProfileService crew methods
- Action: `view_file src/services/AuthProfileService.ts` — locate `leavePermanentCrew()`, `createPermanentCrew()`, and any existing `deletePermanentCrew()` method; map the table names, column names, and Supabase call patterns used
- Source: `src/services/AuthProfileService.ts`:1-end
- Verify: Know exact table names (`crews`, `crew_memberships` or equivalent), column names (`id`, `owner_id`, `crew_id`, etc.) before writing SQL

### Step 2 — Read crews and crew_memberships types
- Action: `grep -n "crew_memberships\|crews" src/types/supabase.ts` then `view_file src/types/supabase.ts` at the located lines — confirm exact column names, foreign key relationships, and whether a cascade delete is already configured on `crew_memberships`
- Source: `src/types/supabase.ts` — `crews` and `crew_memberships` table blocks
- Verify: Have the exact SQL schema before writing the migration; no guessing of column names

### Step 3 — Write and run delete_crew RPC migration
- Action: Via Supabase MCP `apply_migration` — SQL: `CREATE OR REPLACE FUNCTION delete_crew(p_crew_id UUID) RETURNS void LANGUAGE plpgsql SECURITY DEFINER AS $$ BEGIN IF NOT EXISTS (SELECT 1 FROM crews WHERE id = p_crew_id AND owner_id = auth.uid()) THEN RAISE EXCEPTION 'Not authorized'; END IF; DELETE FROM crew_memberships WHERE crew_id = p_crew_id; DELETE FROM crews WHERE id = p_crew_id AND owner_id = auth.uid(); END; $$;` — name the migration `create_delete_crew_rpc`
- Source: `src/types/supabase.ts` — table/column names verified in Step 2
- Verify: Migration applies; Supabase Studio shows `delete_crew` function listed under Database Functions; calling it as a non-owner raises an exception

### Step 4 — Regenerate TypeScript types
- Action: Run `/db-sync` to regenerate `src/types/supabase.ts` so `delete_crew` appears in the RPC type surface
- Source: `src/types/supabase.ts`
- Verify: `supabase.rpc('delete_crew', ...)` resolves without TypeScript error

### Step 5 — Add deletePermanentCrew() to AuthProfileService
- Action: In `src/services/AuthProfileService.ts`, add `async deletePermanentCrew(crewId: string): Promise<void>` — calls `supabase.rpc('delete_crew', { p_crew_id: crewId })`; throws on error with a user-friendly message
- Source: `src/services/AuthProfileService.ts` — insertion point alongside `leavePermanentCrew()`
- Verify: Method compiles; parameter name matches the RPC argument exactly (`p_crew_id`)

### Step 6 — Update useAccountOverview to differentiate owner vs member
- Action: `view_file src/hooks/useAccountOverview.ts L260-280` — locate the crew delete handler at L268; replace the single `leavePermanentCrew()` call with: `if (crew.owner_id === user?.id) { await authProfileService.deletePermanentCrew(crew.id) } else { await authProfileService.leavePermanentCrew(crew.id) }`
- Source: `src/hooks/useAccountOverview.ts`:268
- Verify: `git diff` shows only the branching logic change; no other handler code altered

### Step 7 — Verify owner delete vs member leave
- Action: Run `npm run verify`; manual test A — as crew owner: trigger delete → confirm crew row is gone from Supabase `crews` table and all `crew_memberships` rows for that crew are gone. Manual test B — as crew member (non-owner): trigger leave → confirm member row gone but crew row persists
- Source: `src/hooks/useAccountOverview.ts`:268, Supabase dashboard
- Verify: `npm run verify` exits 0; both scenarios produce correct database state

## Out of Scope
- Crew invites system
- Crew realtime channel teardown on delete
- Crew leaderboard data cleanup
- Any BLE or device layer changes
