# Live Debugger: Error Review & Resolution System

This plan outlines the architecture and interface design for a robust, production-grade error review and resolution system within the Command Center's **Live Debugger** module. It unifies data from `crash_telemetry` (fatal crashes) and `telemetry_errors` (non-fatal errors/warnings).

## Proposed Changes

### Data Retention & Alerting
*   **Data Retention:** 90 days for resolved crash reports and breadcrumbs. (Pending future cron job, not in scope for UI work).
*   **Alerting Thresholds:** Visually highlight an `error_signature` if it spikes suddenly (e.g., >10 crashes in 1 hour).
*   **Resolution Flow:** Simply update the DB status for now (no Jira/Ticket generation).

We will evolve the `LiveDebuggerWidget.tsx` from a simple live-feed into a multi-pane **Diagnostic Suite**.

### 1. Interface Architecture (Tabs)

The Live Debugger will be split into three core views:

*   **Tab 1: Live Stream (The Pulse)**
    *   **Purpose:** Real-time tailing of both `crash_telemetry` and `telemetry_errors`.
    *   **UI:** A fast-scrolling terminal-like feed or a dense Ag-Grid table.
    *   **Features:** Pause/Resume stream, filter by severity (Fatal, Error, Warn), text-search.
*   **Tab 2: Crash Autopsy (Aggregated Fatalities)**
    *   **Purpose:** Triage and resolve fatal crashes grouped by `error_signature`.
    *   **UI:** An AG Grid view grouped by `error_signature`. Columns: `Signature`, `Count`, `Affected Users`, `First Seen`, `Last Seen`, `Status`.
    *   **Drill-Down (The Autopsy Pane):** Clicking a group opens the detailed autopsy for a specific instance, showing the **Environment State**, **Stack Trace**, and a dedicated **Breadcrumb Timeline** (T-minus 50 events).
*   **Tab 3: Non-Fatal Diagnostics**
    *   **Purpose:** Review transient issues, BLE drops, or failed syncs from `telemetry_errors`.
    *   **UI:** Ag-Grid view with filtering by `event_type` and `operation_type`.

### 2. Resolution Workflow (The Loop)

*   **Grouping & Triaging:** Admins review crash groups (signatures).
*   **Status Management:** A crash signature can be transitioned from `OPEN` -> `IN PROGRESS` -> `RESOLVED` -> `IGNORED`.
*   **Bulk Updating:** Resolving a signature will fire a Supabase RPC to `UPDATE crash_telemetry SET status = 'RESOLVED' WHERE error_signature = X`, instantly clearing it from the active queue.
*   **Regression Tracking:** If a crash with a `RESOLVED` signature occurs on a *newer* `app_version`, it automatically re-opens (Regression).

### 3. Database & Schema Updates

To support fast aggregation without locking the frontend:
*   **[NEW] RPC / Database View:** Create a Supabase View `view_crash_aggregates` to efficiently group crashes by signature, count them, and find the latest timestamp.
*   **[MODIFY] `crash_telemetry`:** Ensure indexes exist on `error_signature` and `status` for fast querying.

## Verification Plan

### Automated Tests
*   Trigger a simulated fatal crash and verify it streams into the Live tab instantly.
*   Verify the Supabase bulk-resolve RPC correctly updates all instances of a specific error signature.

### Manual Verification
*   Verify the AG Grid grouping functions correctly when hundreds of crashes exist.
*   Verify the Breadcrumb Timeline accurately parses the JSONB array and renders BLE, ACTION, and NETWORK events with distinct color coding.
