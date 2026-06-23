# Implementation Plan: feat/applogger-mmkv-storage

## Goal
Replace AsyncStorage with MMKV as the synchronous native storage backend in `AppLoggerStorage.ts`. Eliminates async bridge overhead during BLE event bursts, increases telemetry capacity from 500 to 5000 entries, and adds one-time migration from legacy AsyncStorage data.

## Source of Truth
- `src/services/appLogger/AppLoggerStorage.ts:1-end` — current implementation (primary target)
- `src/services/appLogger/types.ts` — TelemetryEntry type shape
- `src/constants/storageKeys.ts` — existing storage key constants (append new MMKV key here)
- Reference implementation: `feat/telemetry-mmkv-upgrade` branch (architecturally outdated but valid MMKV API reference)

## Dependency Proposal (user approval required before `npm install`)
| | |
|---|---|
| **Package** | `react-native-mmkv` |
| **Version** | `^4.3.1` |
| **Weight** | ~2.8MB native (JSI-based, no JS bridge) |
| **Activity** | 8.9k ⭐, v4.3.0 released 2026-05-12 — actively maintained |
| **Necessity** | AsyncStorage bridge is async — under BLE event bursts (20+ writes/sec in music mode) queue backs up and drops entries. MMKV is synchronous JSI with ~10µs write latency. |
| **Native Alternative** | None — Expo SecureStore and FileSystem are not suitable for high-frequency append; MMKV is the accepted gold standard. |

**Do not run `npm install` until user explicitly approves this proposal.**

## Prerequisite
None — `AppLoggerStorage.ts` has no import overlap with any active worktree or other intaked task.
This task may be executed in parallel with `refactor/burn-down-audit-failures` if desired.

## Files to Create / Modify

### [MODIFY] `package.json`
- Add `"react-native-mmkv": "^4.3.1"` to `dependencies`
- Run: `npm install` (after approval)

### [MODIFY] `src/constants/storageKeys.ts`
- Add `TELEMETRY_MMKV_ID = 'sk8lytz_telemetry'` constant (MMKV instance ID)

### [MODIFY] `src/services/appLogger/AppLoggerStorage.ts`
Current state: AsyncStorage-only, 500-entry hard cap.

Changes (surgical — all within this file):

**Step 1 — Add storage abstraction layer:**
```typescript
// Platform-conditional MMKV (JSI) or AsyncStorage mock (web)
const mmkvInstance = Platform.OS !== 'web'
  ? new (require('react-native-mmkv').MMKV)({ id: STORAGE_KEYS.TELEMETRY_MMKV_ID })
  : null;

const store = {
  get: (key: string): string | undefined =>
    mmkvInstance ? mmkvInstance.getString(key) : undefined,
  set: (key: string, value: string): void => {
    if (mmkvInstance) mmkvInstance.set(key, value);
  },
  delete: (key: string): void => {
    if (mmkvInstance) mmkvInstance.delete(key);
  },
};
```

**Step 2 — Update `MAX_ENTRIES`:** `500` → `5000`

**Step 3 — Update `readEntries()`:**
- Primary read: `store.get(ENTRIES_KEY)` (synchronous on native)
- Migration fallback: if MMKV empty AND `AsyncStorage.getItem(LEGACY_KEY)` has data → migrate, delete legacy
- Return parsed array or `[]`

**Step 4 — Update `writeEntries()`:**
- Use `store.set(ENTRIES_KEY, JSON.stringify(entries))` (synchronous)
- No behavioral changes to trimming / circular buffer logic

## Verify Steps
1. After dependency install: `npm run verify` — no import errors, TSC clean
2. After storage abstraction: `grep -n "mmkvInstance" src/services/appLogger/AppLoggerStorage.ts` — present and gated by `Platform.OS !== 'web'`
3. Android smoke test: open app → trigger 10 log events → kill app → reopen → entries present (MMKV persistence)
4. Web smoke test: `npm run web` → no crash on MMKV require (fallback active)
5. Migration test: manually seed AsyncStorage with test entries, launch fresh → entries migrated, AsyncStorage key deleted

## Out of Scope
- Jittered sync backoff — belongs in `AppLoggerService.ts` cloud upload logic, not storage layer
- `AppLoggerService.ts`, `AppLoggerCloud.ts` — do not touch
- Any BLE-specific changes
- Crash reporting / CrashReporter — separate system
