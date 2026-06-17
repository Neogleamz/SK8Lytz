# Implementation Plan: fix/group-concurrent-write

## Goal
Replace every `Promise.all(targets.map(async ...))` pattern in group BLE dispatch with sequential `for...of` loops to eliminate GATT write collisions when multiple devices are connected. The BLE write queue (`BleWriteQueue.ts`) serializes per-device, but callers bypass that safety by blasting all devices in parallel via `Promise.all`.

## Source Analysis Link
- [system_audit_report.md — CLUSTER 1](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) (lines 49–66)

## Files to Modify

| # | File | Lines | Instances | Description |
|---|------|-------|-----------|-------------|
| 1 | `src/hooks/useControllerDispatch.ts` | 94, 145, 207, 280, 325, 368, 390 | **7** | All group dispatch callbacks |
| 2 | `src/screens/Onboarding/HardwareSetupWizardScreen.tsx` | 106, 672 | **2** | Orientation test + FTUE hardware loop |
| 3 | `src/services/ble/HeartbeatService.ts` | 37 | **1** | Heartbeat polling loop |
| 4 | `src/services/ble/RSSIService.ts` | 74 | **1** | RSSI polling loop |

**Files to Create:** None.

## Steps

### Step 1 — useControllerDispatch.ts: 7 × Promise.all → for...of

Replace each `await Promise.all(targets.map(async (device) => { ... }));` with a `for...of` loop that awaits each device sequentially.

**Locations (all confirmed via `view_file`):**

1. **`sendColor`** — `Source: useControllerDispatch.ts:94-104`
   - `await Promise.all(targets.map(async (device) => { ... }));` → `for (const device of targets) { ... }`

2. **`applyFixedPattern`** — `Source: useControllerDispatch.ts:145-177`
   - Same transformation. Cache logic per-device stays intact.

3. **`applyStaticModePattern`** — `Source: useControllerDispatch.ts:207-231`
   - Same transformation. Inner `if/else if` branches are unchanged.

4. **`applyEmergencyPattern`** — `Source: useControllerDispatch.ts:280-288`
   - Same transformation.

5. **`handleMusicChange`** — `Source: useControllerDispatch.ts:325-354`
   - Same transformation. The existing `INTER_DEVICE_WRITE_GAP_MS` delay inside the loop is preserved.

6. **`setPower`** — `Source: useControllerDispatch.ts:368-376`
   - Same transformation.

7. **`setMultiColor`** — `Source: useControllerDispatch.ts:390-398`
   - Same transformation.

**Pattern for all 7:**
```typescript
// BEFORE
await Promise.all(targets.map(async (device) => {
  // ...device logic...
}));

// AFTER
for (const device of targets) {
  // ...device logic (unchanged)...
}
```

- **Verify:** `git diff HEAD src/hooks/useControllerDispatch.ts` — confirm exactly 7 `Promise.all` removals. Zero `Promise.all(targets` remaining. No lines outside the 7 call sites changed.

### Step 2 — HardwareSetupWizardScreen.tsx: 2 × Promise.all → for...of

> ⚠️ S4 Monolith Acknowledgement: This file is 43KB. Surgical edits only — two isolated `Promise.all` blocks.

1. **`fireOrientationTest`** — `Source: HardwareSetupWizardScreen.tsx:106-121`
   - `await Promise.all(selected.map(async (device) => { ... }));` → `for (const device of selected) { ... }`

2. **FTUE hardware loop (Phase 4 completion)** — `Source: HardwareSetupWizardScreen.tsx:672-699`
   - `await Promise.all(selected.map(async (device) => { ... }));` → `for (const device of selected) { ... }`

- **Verify:** `git diff HEAD src/screens/Onboarding/HardwareSetupWizardScreen.tsx` — confirm exactly 2 `Promise.all` removals. No JSX, hooks, or state variables deleted.

### Step 3 — HeartbeatService.ts: 1 × Promise.all → for...of

**`heartbeatService` interval callback** — `Source: HeartbeatService.ts:37-80`
- `await Promise.all(currentDevices.map(async (device) => { ... }));` → `for (const device of currentDevices) { ... }`
- The per-device try/catch inside the loop body is preserved unchanged.

- **Verify:** `git diff HEAD src/services/ble/HeartbeatService.ts` — confirm exactly 1 `Promise.all` removal. `sendBack({ type: 'HEARTBEAT_FAIL' })` call preserved.

### Step 4 — RSSIService.ts: 1 × Promise.all → for...of

**`startRSSIPolling` interval callback** — `Source: RSSIService.ts:74-88`
- `await Promise.all(devices.map(async (device) => { ... }));` → `for (const device of devices) { ... }`
- Threshold checks (`onCriticalSignal`, `onWeakSignal`) preserved unchanged.

- **Verify:** `git diff HEAD src/services/ble/RSSIService.ts` — confirm exactly 1 `Promise.all` removal. Threshold callbacks intact.

### Step 5 — Full Verification

- **Verify:** `npm run verify` — TSC clean, Jest green, no regressions.
- **Verify:** `grep -rn "Promise.all" src/hooks/useControllerDispatch.ts src/screens/Onboarding/HardwareSetupWizardScreen.tsx src/services/ble/HeartbeatService.ts src/services/ble/RSSIService.ts` — returns **0 matches** across all 4 files.

## Verification Plan

| Check | Command | Expected |
|-------|---------|----------|
| No remaining `Promise.all` in targets | `grep -rn "Promise.all" <4 files>` | 0 matches |
| TypeScript compiles | `npm run verify` | TSC exit 0 |
| Jest passes | `npm run verify` | Jest exit 0 |
| Post-diff audit (per file) | `git diff HEAD <file>` | Only `Promise.all` → `for...of` changes |
| No `as any` introduced | `grep -n "as any" <4 files>` | 0 new matches |

## Out of Scope

- **`src/services/BleWriteQueue.ts`** — Queue internals are not modified. The queue already serializes per-device; this fix ensures callers respect that serialization.
- **Protocol payload changes** — Zero byte-level changes. Only the dispatch iteration pattern changes.
- **Test file modifications** — No test files are created or modified in this task.
- **Other `Promise.all` usage outside these 4 files** — Only the 11 instances identified in CLUSTER 1 are addressed.
- **`docs/SK8Lytz_App_Master_Reference.md`** — No architectural or API surface changes; doc update not required.
