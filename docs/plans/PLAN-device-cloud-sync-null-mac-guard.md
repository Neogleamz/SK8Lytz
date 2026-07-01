# Implementation Plan — fix/device-cloud-sync-null-mac-guard

## Summary
`DeviceCloudSync.mergeCloudAndLocal` crashes with `TypeError: Cannot read properties of null (reading 'toUpperCase')` on any `registered_devices` row whose `device_mac` is null. The tombstone filter at L19-21 lets null-MAC rows survive (`row.device_mac?.toUpperCase?.() ?? ''` → `''`, which is not in tombstones), then L33 calls `cloud.device_mac.toUpperCase()` unguarded. The Wave 1 type-safety fix removed the old `any` cast that hid this; the crash path is **pre-existing**, not newly introduced.

## Source of Truth
- `src/services/deviceRepository/DeviceCloudSync.ts:L13-34`
  - L6-7: `type CloudDeviceRow = Tables<'registered_devices'>` — `device_mac` is nullable per DB schema.
  - L19-21: tombstone filter — null MAC coerces to `''`, passes the filter.
  - L30-31: `const cloud = { ...row, is_pending_sync: false } as RegisteredDevice` — the cast hides the null.
  - L33: `cloud.device_mac.toUpperCase()` — throws on null.
- Corroborating evidence: `docs/SESSION_LOG.md` `[DECISION] wiring-check investigation — 2026-06-30`, Finding 2 (Reyes, VERIFIED).

## Root Cause
A null `device_mac` = corrupt DB row (no legitimate device exists without a MAC). It should be excluded from the merge entirely, not coerced and passed downstream.

## Files to Create/Modify
- `src/services/deviceRepository/DeviceCloudSync.ts` — MODIFY (`mergeCloudAndLocal` only).

## Steps

### Step 1 — Add type-narrowing null-MAC filter at the top of `mergeCloudAndLocal`
Before the tombstone filter (currently L18-21), introduce:

```ts
const validCloud = cloudRows.filter(
  (row): row is CloudDeviceRow & { device_mac: string } => row.device_mac != null
);
```

This narrows the row type so `device_mac` is `string` (non-null) for all downstream use, eliminating the need for the `?.` / `?? ''` coercion and the downstream crash.

### Step 2 — Rewire the tombstone filter to consume `validCloud`
Replace the tombstone filter so it iterates `validCloud` and can safely call `.toUpperCase()` directly (no optional chaining needed now that `device_mac` is narrowed to `string`):

```ts
const filteredCloud = validCloud.filter(
  (row) => !tombstones.includes(row.device_mac.toUpperCase())
);
```

### Step 3 — Emit an `AppLogger.warn` when null-MAC rows were filtered
Immediately after the `validCloud` filter (Step 1), before the tombstone filter, add:

```ts
if (validCloud.length < cloudRows.length) {
  AppLogger.warn('[DeviceCloudSync] mergeCloudAndLocal filtered null-mac rows', {
    null_mac_filtered: cloudRows.length - validCloud.length,
  });
}
```

The existing tombstone-skipped warn (currently L23-27) stays, but its comparison base becomes `validCloud.length` vs `filteredCloud.length` so the two counts do not overlap:

```ts
if (filteredCloud.length < validCloud.length) {
  AppLogger.warn('[DeviceCloudSync] syncFromCloud tombstone filtered', {
    skipped: validCloud.length - filteredCloud.length,
  });
}
```

### Notes for the implementer
- The merge loop at L30-77 continues to consume `filteredCloud` unchanged — no edits needed there. Because `filteredCloud` now derives from `validCloud`, every row it contains has a non-null `device_mac`, so L33 `cloud.device_mac.toUpperCase()` can no longer throw.
- The `as RegisteredDevice` cast at L31 is out of scope for this fix (the local `RegisteredDevice.device_mac` is already `string` non-nullable per `types.ts:L10`, and the narrowing guarantees the field is present). Leave it; do not attempt to remove it in this snack.
- The `offlineLocalOnly` block (L79-87) already uses optional chaining (`cloudD.device_mac?.toUpperCase()`) — no change required.

## Verify
1. `npm run verify` passes (TSC + Jest + AST + TypeSafety + WorkflowValidator).
2. The type predicate `row is CloudDeviceRow & { device_mac: string }` compiles with no `any` cast and no `@ts-ignore`.
3. Manual reasoning check: a `registered_devices` row with `device_mac === null` is excluded by `validCloud` and therefore never reaches the `.toUpperCase()` call at the merge loop.

## Out of Scope
- DB schema changes (making `device_mac` non-nullable at the column level).
- RLS policy changes.
- Any other `DeviceCloudSync` method (`checkDeviceClaimed`, `hasRegistrations`).
- Removing the `as RegisteredDevice` cast at L31.
