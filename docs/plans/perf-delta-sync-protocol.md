# ⚡ Flash-Executable Implementation Plan: perf/delta-sync-protocol

> **WARNING TO AUTHOR (THINK MODEL)**: This plan is designed to be executed blindly by a `[🤖 FLASH]` or pure execution model in a future session. 
> Do NOT use line numbers (`Replace lines 45-50`). The codebase may have drifted between plan creation and execution.
> You MUST use **Semantic Anchors** (e.g. `Find the entire useBLE hook`, `Search for the exact phrase: return <View>`).
> All code snippets must be 100% complete, fully typed, and ready to be copy-pasted via the `replace_file_content` tool.

---

## 1. Pre-Flight Context Check (Drift Verification)

Before executing any file modifications, execute the following strict checks using `view_file` or `grep_search`. Do NOT guess if the file looks different.

*   [ ] **Check 1:** Open `src/services/CrewService.ts`. Search for semantic anchor: `async fetchActiveSessions()`. 
    *   *Expected state:* The function should exist without a parameter.
*   [ ] **Check 2:** Open `src/hooks/useDeviceFleet.ts`. Search for semantic anchor: `const loadDevices = useCallback(async ()`.
    *   *Expected state:* The function should exist and build the Supabase query.

---

## 2. Step-by-Step Execution Strict Instructions

### Step 2.1: Supabase Database Migration
- **Target:** Supabase Database
- **Action:** Execute the `mcp_supabase-mcp-server_apply_migration` tool to add `updated_at` columns and triggers to `crew_sessions`, `crews`, and `registered_devices`. 
- **Migration SQL:**
```sql
ALTER TABLE crew_sessions ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ DEFAULT NOW();
ALTER TABLE crews ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ DEFAULT NOW();
ALTER TABLE registered_devices ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ DEFAULT NOW();

CREATE OR REPLACE FUNCTION trigger_set_timestamp()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS set_timestamp_crew_sessions ON crew_sessions;
CREATE TRIGGER set_timestamp_crew_sessions
BEFORE UPDATE ON crew_sessions
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

DROP TRIGGER IF EXISTS set_timestamp_crews ON crews;
CREATE TRIGGER set_timestamp_crews
BEFORE UPDATE ON crews
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

DROP TRIGGER IF EXISTS set_timestamp_registered_devices ON registered_devices;
CREATE TRIGGER set_timestamp_registered_devices
BEFORE UPDATE ON registered_devices
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();
```
- **Sync Action:** Proceed to run `mcp_supabase-mcp-server_generate_typescript_types` to refresh local types per `[supabase-sync.md]` rule.

### Step 2.2: Add Delta Sync to CrewService.ts for Active Sessions
- **Target File:** `src/services/CrewService.ts`
- **Semantic Anchor / Target Content:** 
  The `fetchActiveSessions()` function until the return map loop.
**Exact Replacement Snippet:**
```typescript
  /**
   * Fetch all currently active crew sessions (for browse UI).
   * Also returns location_label and status with each session.
   * Delta Sync: Supply updatedSince to only fetch modified sessions.
   */
  async fetchActiveSessions(updatedSince?: string): Promise<CrewSession[]> {
    // Proactively clean up expired records first
    await this.cleanupExpiredSessions();

    let query = supabase
      .from('crew_sessions')
      .select('*, crew_members(count)')
      .eq('is_active', true)
      .gt('expires_at', new Date().toISOString());

    if (updatedSince) {
      query = query.gt('updated_at', updatedSince);
    }

    const { data, error } = await query
      .order('created_at', { ascending: false })
      .limit(20);

    if (error || !data) return [];
```

### Step 2.3: Add Delta Sync to CrewService.ts for Public Sessions
- **Target File:** `src/services/CrewService.ts`
- **Semantic Anchor / Target Content:** 
  The entire `fetchPublicSessions()` function.
**Exact Replacement Snippet:**
```typescript
  /**
   * Fetch PUBLIC sessions only (open to anyone, no invite code needed).
   * Used for location-based discovery browse. Requires migration 007.
   * Delta Sync: Supply updatedSince to only fetch modified sessions.
   */
  async fetchPublicSessions(updatedSince?: string): Promise<CrewSession[]> {
    try {
      // Try the public_sessions view first (migration 007)
      let pubQuery = supabase
        .from('public_sessions')
        .select('*');
        
      if (updatedSince) {
        pubQuery = pubQuery.gt('updated_at', updatedSince);
      }
      
      const { data, error } = await pubQuery.limit(30);

      if (!error && data) {
        return data.map((s) => ({
          ...s,
          is_public: true,
        })) as unknown as CrewSession[];
      }
    } catch {}

    // Fallback: filter from crew_sessions directly
    let query = supabase
      .from('crew_sessions')
      .select('*, crew_members(count)')
      .eq('is_active', true)
      .eq('is_public', true)
      .gt('expires_at', new Date().toISOString());

    if (updatedSince) {
      query = query.gt('updated_at', updatedSince);
    }

    const { data } = await query
      .order('created_at', { ascending: false })
      .limit(20);

    return (data ?? []).map((s: any) => ({
      ...s,
      member_count: s.crew_members?.[0]?.count ?? 0,
    } as CrewSession));
  }
```

### Step 2.4: Add Delta Sync to ProfileService.ts for Permanent Crews
- **Target File:** `src/services/ProfileService.ts`
- **Semantic Anchor / Target Content:** 
  The `getMyCrew()` function up to the mapped return statement.
**Exact Replacement Snippet:**
```typescript
  /**
   * List all permanent crews the current user is a member of.
   * Delta Sync: Optionally supply updatedSince to limit fetching.
   */
  async getMyCrew(updatedSince?: string): Promise<PermanentCrew[]> {
    const { data: { user } } = await supabase.auth.getUser();
    if (!user) return [];

    const query = supabase
      .from('crew_memberships')
      // Note: For delta sync on inner joins, we'd apply it on the root table or standard table
      // To preserve stability without custom RPC, we perform standard select and filter locally.
      .select(`crews ( id, name, owner_id, invite_code, created_at, updated_at, is_public, avatar_color, avatar_icon, avatar_url, city, state, description )`)
      .eq('user_id', user.id);

    const { data, error } = await query.order('joined_at', { ascending: true });

    if (error || !data) return [];

    const crews = data
      .map((row: any) => row.crews)
      .filter(Boolean);
      
    const filteredCrews = updatedSince 
      ? crews.filter((c: any) => c.updated_at && (new Date(c.updated_at) > new Date(updatedSince)))
      : crews;

    return filteredCrews.map((crew: any) => ({
```

### Step 2.5: Add Delta Sync to useDeviceFleet.ts for Device Lists
- **Target File:** `src/hooks/useDeviceFleet.ts`
- **Semantic Anchor / Target Content:** 
  The parameter list for `loadDevices` and the inner `supabase.from('registered_devices')` query up to the `setDevices` check.
**Exact Replacement Snippet:**
```typescript
  const loadDevices = useCallback(async (updatedSince?: string) => {
    try {
      const { data: { user } } = await supabase.auth.getUser();
      if (!user) return;

      let query = supabase
        .from('registered_devices')
        .select('device_mac, device_name, custom_name, product_type, position, group_name, created_at, updated_at')
        .eq('user_id', user.id);
        
      if (updatedSince) {
        query = query.gt('updated_at', updatedSince);
      }

      const { data: dbDevices, error } = await query.order('created_at', { ascending: false });

      if (error) throw error;

      if (dbDevices && dbDevices.length > 0) {
        setDevices(prev => {
           // For Delta Sync, we need to merge the new devices into the prev state if updatedSince is provided
           const patched = updatedSince ? [...prev] : [];
           
           dbDevices.forEach((d: any) => {
             const newDev = {
               id: d.device_mac,
               name: d.device_name ?? d.device_mac,
               customName: d.custom_name ?? undefined,
               groupName: d.group_name ?? undefined,
               type: d.product_type ?? undefined,
               registeredAt: d.created_at,
             };
             
             if (updatedSince) {
               const existIdx = patched.findIndex(p => p.id === newDev.id);
               if (existIdx >= 0) patched[existIdx] = newDev;
               else patched.push(newDev);
             } else {
               patched.push(newDev);
             }
           });
           return patched;
        });
      } else if (initialDevices.length > 0 && !updatedSince) {
```

---

## 3. Post-Execution Verification

*   [ ] **Command:** `npx tsc --noEmit`
    *   *Expected Output:* Clean exit (0 errors) relating to the modified files.
*   [ ] **Manual Step:** Ask user to load `localhost:8081` and navigate to the Crew Hub and Dashboard, verifying that device and crew arrays still display populated state correctly.

---

**Completion:** Once all checks pass, proceed to Commit Phase using the semantic message: `feat(perf): implement differential fetching for crews and fleet via updated_at`
