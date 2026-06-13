# Implementation Plan — `fix/pii-scrub-sweep`

## Goal
Fix the 4 indefensible PII leaks in AppLogger calls: 1 full user profile object and 3 raw user ID references. MAC addresses in BLE controller logs are explicitly **out of scope** (local-only debug telemetry, device MACs are not user-linkable in practice).

## Decision Log
2026-06-08: User confirmed MAC addresses are out of scope. AppLogger telemetry is local-only. Only user emails, names, and user IDs are indefensible PII. Scope reduced from [Meal] → [Snack].

## Source of Truth
- `artifacts/deepdive_raw/R-09_findings.json` — full findings inventory
- `artifacts/system_audit_report.md` §R-09

## Out of Scope
- MAC address scrubbing (`BleConnectionManager`, `BleWriteDispatcher`, `useBLEScanner`, `useBLEAutoRecovery`) — **explicitly excluded by user decision 2026-06-08**
- Changing AppLogger internals
- Removing logs entirely

---

## Step 1 — Fix `UserManagementPanel.tsx:222` (CRITICAL — full profile object)
- **Action:** `view_file src/components/admin/tools/UserManagementPanel.tsx` lines 215–230
- **Finding:** Full `data` object (containing email + display name) passed to AppLogger
- **Fix:** Replace the log of the full `data` object with only non-PII fields:
  ```typescript
  // BEFORE
  AppLogger.info('UserManagement', 'Export complete', data);
  // AFTER
  AppLogger.info('UserManagement', 'Export complete', { count: data.length });
  ```
- **Verify:** No `email`, `name`, `display_name` fields in the log payload at line 222.

## Step 2 — Fix `CrewService.ts:375` (user ID)
- **Action:** `view_file src/services/CrewService.ts` lines 370–380
- **Finding:** Raw `userId` passed to AppLogger
- **Fix:** Remove `userId` from the log payload or replace with a scrubbed reference:
  ```typescript
  // BEFORE
  AppLogger.info('Crew', 'Session joined', { userId, crewId });
  // AFTER
  AppLogger.info('Crew', 'Session joined', { crewId }); // userId not needed for debugging
  ```
- **Verify:** No `userId` / `user_id` in AppLogger call at line 375.

## Step 3 — Fix `useCrewSession.ts:98` (user ID)
- **Action:** `view_file src/hooks/useCrewSession.ts` lines 93–103
- **Finding:** Raw `userId` in log payload
- **Fix:** Remove `userId` from the log — the crew/session IDs provide sufficient debug context
- **Verify:** No user ID in AppLogger call at line 98.

## Step 4 — Fix `DeviceRepository.ts:358` (user ID)
- **Action:** `view_file src/services/DeviceRepository.ts` lines 353–363
- **Finding:** `user.id` logged during device sync
- **Fix:** Remove `user.id` from the log payload — device MAC (already acceptable per decision) provides sufficient debug context
- **Verify:** No `user.id` in AppLogger call at line 358.

## Step 5 — Post-edit verification
- **Action:** `grep -rn "AppLogger" src/ | grep -E "(email|display_name|user\.id|userId|user_id)" | grep -v "scrubPII\|// ok\|MAC"`
- **Verify:** Zero results — no user account identifiers in any AppLogger call.

## Step 6 — TSC check
- **Action:** `npx tsc --noEmit`
- **Verify:** Zero new errors introduced.
