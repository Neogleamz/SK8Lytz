# Implementation Plan

## Goal
Ensure devices registered in offline mode get `user_id` stamped from the authenticated user at flush time, not at registration time (which may have no user).

## Source of Truth
- `src/services/DeviceRepository.ts` — `registerDevice()`, `saveRegisteredDevice()`, and `_flushPendingSync()` — the exact methods that control when `user_id` is set on the pending record

## Steps

### Step 1 — Read DeviceRepository fully
- Action: `view_file src/services/DeviceRepository.ts` — read the entire file; map `registerDevice()` signature, `saveRegisteredDevice()` call and what `user_id` value is passed (or omitted), the pending sync record structure written to AsyncStorage, and `_flushPendingSync()` — where the Supabase insert occurs
- Source: `src/services/DeviceRepository.ts`:1-end
- Verify: Know exactly when and where `user_id` is set on the record before writing any code

### Step 2 — Identify the user_id stamping defect
- Action: In the code read from Step 1, identify: (a) what type `RegisteredDevice` or equivalent pending record uses for `user_id` — is it required or optional? (b) Is `user_id` set in `registerDevice()` / `saveRegisteredDevice()` where `getUser()` may return null? (c) Is `user_id` overwritten or preserved in `_flushPendingSync()`?
- Source: `src/services/DeviceRepository.ts` — type definitions and function bodies
- Verify: Can state definitively whether the defect exists (null `user_id` stored at registration time) before implementing fix

### Step 3 — Stamp user_id at flush time in _flushPendingSync()
- Action: In `_flushPendingSync()`, before the Supabase insert loop, call `const { data: { user } } = await supabase.auth.getUser()`. For each pending record, overwrite/set `record.user_id = user?.id ?? record.user_id` — if auth user is available, stamp it; if not, preserve whatever was stored (null or existing value)
- Source: `src/services/DeviceRepository.ts` — `_flushPendingSync()` function body
- Verify: `git diff` shows only the addition of the auth fetch and the stamp assignment; no Supabase insert logic altered

### Step 4 — Add null user guard in flush
- Action: In `_flushPendingSync()`, after the `getUser()` call — if `user` is null, log `AppLogger.warn('[DeviceRepository] _flushPendingSync: user null during flush — skipping all records')` and `return` early without attempting any Supabase inserts (leave records in queue for next worker cycle)
- Source: `src/services/DeviceRepository.ts` — `_flushPendingSync()` function body
- Verify: When `getUser()` returns null during flush, no Supabase insert is attempted; queue is left intact

### Step 5 — Add flush-time stamp log
- Action: In `_flushPendingSync()`, after successfully stamping `user_id` for a record that previously had a null/undefined value, log: `AppLogger.warn('[DeviceRepository] _flushPendingSync: stamping user_id from auth at flush time for offline-registered device')`
- Source: `src/services/DeviceRepository.ts` — `_flushPendingSync()` loop body
- Verify: Log appears in ADB logcat output when an offline-registered device is flushed with a newly authenticated user

### Step 6 — Verify end-to-end offline registration to Supabase
- Action: Run `npm run verify`; manual test: launch app offline (airplane mode) → pair a device (registration queued with null user_id) → disable airplane mode → sign in → wait 60 seconds for sync worker → check Supabase `registered_devices` table — device row should have correct `user_id`
- Source: `src/services/DeviceRepository.ts` — `_flushPendingSync()` end-to-end
- Verify: `npm run verify` exits 0; device row in Supabase has the authenticated user's UUID as `user_id`

## Out of Scope
- Device configuration sync (EEPROM settings, effect config)
- BLE pairing flow changes
- Device rename or delete operations
- Any scene or session layer changes
