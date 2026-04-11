### Design Decisions & Rationale
Optimizing RLS policies to use `(select auth.uid())` instead of `auth.uid()` avoids Postgres re-evaluating the function per row, which drastically improves query performance at scale. Adding covering indexes to unindexed foreign keys avoids full table scans during joins and cascades. Memoizing `AnalogGauge` in the Dashboard will prevent continuous heavy SVG recalculations, ensuring 60fps renders even during active Bluetooth telemetry updates or slider interactions.

## Proposed Changes

### Supabase Performance Adjustments (Migration)
A single Supabase Migration via `apply_migration` will be created natively against the MCP server to execute:
1. **RLS InitPlan Optimization:** Recreate the 20+ identified RLS policies (e.g., `shared_scenes`, `registered_groups`, `crew_members`, `user_profiles`) swapping `auth.uid()` with `(select auth.uid())`. 
2. **Indexing:** Create essential missing indexes on foreign key columns such as `crew_members.user_id`, `crew_sessions.leader_user_id`, `crews.owner_id`, `custom_builder_presets.user_id`, `registered_devices.group_id`, `registered_groups.user_id`, and `shared_scenes.author_id`.
3. **Deduplication:** Clean up duplicate configurations identified by the advisor (e.g., dropping `idx_crews_public` and resolving multiple permissive policies on `registered_devices` and `print_queue`).

### React Native Optimizations
- **`src/components/DockedController.tsx`**
  - Extract the internal SVG components of `AnalogGauge` into a highly optimized structure.
  - Wrap the `AnalogGauge` component in `React.memo()` with a custom equality checker if needed, preventing the SVG parser from firing hundreds of times per second when unrelated state changes in the `DockedController` loop.
