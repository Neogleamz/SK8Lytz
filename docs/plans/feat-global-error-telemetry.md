# Global Error Telemetry & Bucket List Sync

We are tasked with capturing unhandled crashes remotely and converting them directly into actionable tasks on our local bucket list without incurring the dependency weight of Sentry. 

> [!TIP]
> Good news: Our native codebase (`App.tsx` and `AppLogger.ts`) *already* hooks into `global.ErrorUtils` and instances a `SafeErrorBoundary`. Any fatal JS breakdown or React component crash is already silently logged to `parsed_logs` as `ERROR_CAUGHT` via our Supabase backend. We don't need Sentry!

To fulfill the specific request ("this should add items to bucket list for us to review and fix"), we will construct an automated utility that bridges the gap between our production telemetry and our local PM workflow.

## Proposed Changes

### `tools/sync_remote_errors.mjs` (NEW SCRIPT)
- We will build a lightweight, local NodeJS script using our existing `@supabase/supabase-js` dependency.
- This script will query the `parsed_session_stats` / `sk8lytz-logs` storage bucket or database for recent `ERROR_CAUGHT` events.
- It will parse these events and automatically append them formatted as `- [ ] \`fix/crash-<id>\` : [Crash] <Message>` directly into the `## 🔴 High Priority / Next Up` section of `tools/SK8Lytz_Bucket_List.md`.

### `tools/SK8Lytz_Bucket_List.md`
- We will insert a persistent anchor (e.g. `<!-- AUTO_SYNC_ERRORS_START -->`) so that the script can safely inject bugs without overwriting manual notes.

### `.agents/rules/tech-debt-janitor.md`
- We will update the PM rule so that whenever using the "clean the house" command, the AI automatically executes `node tools/sync_remote_errors.mjs` to fetch production crashes and build the queue locally.

## Design Decisions & Rationale
By relying on our already-established, 100% native telemetry engine (`AppLogger`) and simply bridging the gap with a Node.js sync script, we adhere forcefully to the **Dependency Diet & Anti-Bloat Protocol**. By wiring it into the Tech Debt Janitor rule, we get automated crash triaging into our bucket list without ever pushing remote code that would try to touch our local hard drive!

## Verification Plan
We will run `node tools/sync_remote_errors.mjs` and verify it successfully identifies test error logs and appropriately pushes a `- [ ]` markdown item into our bucket list. We don't strictly require simulated crashes since we can just mock an error payload on Supabase.
