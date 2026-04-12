# [AUDIT] RLS Performance & Security

## Goal
Routine RLS audit on Supabase queries; optimize React Native render cycles for dashboard gauges.

## Proposed Changes
- Audit all `ENABLE POLICY` statements in Supabase for `authenticated` vs `anon`.
- Profile `parsed_session_stats` query performance.
- Implement `React.memo` for SVG gauges in `DashboardScreen`.

## Verification Plan
- Run `explain analyze` on critical SQL.
- Monitor JS Thread FPS during high-speed telemetry simulation.
