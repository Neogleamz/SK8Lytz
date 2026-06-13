# ⚡ Flash-Executable Implementation Plan: Device Specific Picks

> **WARNING TO AUTHOR (THINK MODEL)**: This plan is designed to be executed blindly by a `[🤖 FLASH]` or pure execution model in a future session.
> Do NOT use line numbers. The codebase may have drifted between plan creation and execution.
> You MUST use **Semantic Anchors**.
> All code snippets must be 100% complete, fully typed, and ready to be copy-pasted via the `replace_file_content` tool.

---

## 1. Pre-Flight Context Check (Drift Verification)

Before executing any file modifications, execute the following strict checks using `view_file` or `grep_search`. Do NOT guess if the file looks different.

*   [ ] **Check 1:** Open `src/components/AdminPicksScheduler.tsx`. Search for semantic anchor: `export default function AdminPicksScheduler`.
    *   *Expected state:* The file should contain the administrative tools for publishing SK8Lytz Picks.
*   [ ] **Check 2:** Open `src/types/supabase.ts`. 
    *   *Expected state:* Ensure `sk8lytz_picks` exists in the database types interface.
*   [ ] **Check 3:** Open `src/hooks/useAdminSettings.ts` or `useProductManager.ts` to locate `allProfiles` which will provide the dynamic catalog IDs for the checkboxes.

---

## 2. Step-by-Step Execution Strict Instructions

### Step 2.1: Apply Supabase Database Migration
- **Action:** Add the targeted_hardware column to `sk8lytz_picks`.
- **Instruction:** Use the `mcp_supabase-mcp-server_apply_migration` tool.
- **Payload:**
```json
{
  "project_id": "<call list_projects first to obtain this>",
  "name": "add_picks_target_hardware",
  "query": "ALTER TABLE sk8lytz_picks ADD COLUMN targeted_hardware text[] DEFAULT NULL;"
}
```
**Post-Action:** Immediately run the `mcp_supabase-mcp-server_generate_typescript_types` tool and use `write_to_file` with `Overwrite: true` to save the output to `src/types/supabase.ts` (strictly abiding by the `RULE[supabase-sync.md]`).

### Step 2.2: Expand the Admin Form State
- **Target File:** `src/components/AdminPicksScheduler.tsx`
- **Semantic Anchor / Target Content:** The `PickData` interface definition.
- **Instruction:** Use `replace_file_content` to add `targeted_hardware` to the form state.

**Exact Replacement Snippet:**
```typescript
interface PickData {
  id?: string;
  name: string;
  mode: string;
  // ... Keep all existing fields ...
  speed: number;
  brightness: number;
  color?: string;
  fixed_fg_color?: string;
  fixed_bg_color?: string;
  fixed_color_mode?: string;
  music_matrix_style?: number;
  music_primary_color?: string;
  music_secondary_color?: string;
  multi_colors?: any;
  multi_transition?: number;
  multi_length?: number;
  mic_sensitivity?: number;
  mic_source?: string;
  sort_order: number;
  is_active: boolean;
  active_from?: string | null;
  active_until?: string | null;
  custom_name?: string;
  pattern_id?: number;
  targeted_hardware?: string[] | null;
}
```

### Step 2.3: Add Selector UI to AdminPicksScheduler
- **Target File:** `src/components/AdminPicksScheduler.tsx`
- **Semantic Anchor / Target Content:** Look for the form rendering block inside `renderForm()` (typically near `<Text style={styles.label}>Pick Name / Title</Text>`).
- **Instruction:** Use `replace_file_content` to inject the hardware target selector chips so admins can check which devices this pick is optimized for.
- **Note:** Retrieve `allProfiles` using `useProductManager()` at the top of the component to drive the available pill options dynamically. If `targeted_hardware` is empty/null, it means "All Devices".

**Exact Replacement Snippet:**
```typescript
{/* Insert this section inside renderForm() just below the Basic Info fields */}
<View style={{ marginBottom: 16 }}>
  <Text style={styles.label}>Targeted Hardware Profiles</Text>
  <Text style={{ color: '#888', fontSize: 11, marginBottom: 8 }}>If none selected, the Pick applies to all connected devices.</Text>
  <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={{ gap: 8 }}>
    {allProfiles.map((p: any) => {
      const isSelected = form.targeted_hardware?.includes(p.id);
      return (
        <TouchableOpacity key={p.id} onPress={() => {
          setForm(prev => {
            const current = prev.targeted_hardware || [];
            if (isSelected) {
              return { ...prev, targeted_hardware: current.filter(id => id !== p.id) };
            } else {
              return { ...prev, targeted_hardware: [...current, p.id] };
            }
          });
        }} style={{ paddingHorizontal: 12, paddingVertical: 8, borderRadius: 16, backgroundColor: isSelected ? '#FF5A00' : '#333' }}>
          <Text style={{ color: '#FFF', fontSize: 12, fontWeight: '700' }}>{p.displayName || p.id}</Text>
        </TouchableOpacity>
      )
    })}
  </ScrollView>
</View>
```

### Step 2.4: Sync Dashboard Client-Side Filtering
- **Target File:** `src/components/DashboardScreen.tsx` or `src/hooks/usePicks.ts` (wherever the fetching of SK8Lytz Picks logic resides).
- **Semantic Anchor / Target Content:** The data array mapping logic (`picks.map(...)`).
- **Instruction:** Ensure the UI filters out Picks that have a strict `targeted_hardware` array that *doesn't* intersect with the user's currently active mapped devices. Add a warning indicator or completely hide incompatible Picks depending on the selected active group.

---

## 3. Post-Execution Verification

*   [ ] **Command:** `npx tsc --noEmit`
    *   *Expected Output:* The compilation succeeds. `targeted_hardware` is safely recognized by Supabase types.
*   [ ] **Manual Step:** Open the Admin Tools → Picks Scheduler. Validate that the horizontal profile selector pills render properly.
*   [ ] **Manual Step:** Create a test Pick and target exclusively `HALOZ`. Verify it writes the array properly to the database. Verify the Dashboard view adapts visibility.

---

**Completion:** Once all checks pass, proceed to Commit Phase using the semantic message: `feat(picks): add device-specific hardware targeting to SK8Lytz Picks schedule`
