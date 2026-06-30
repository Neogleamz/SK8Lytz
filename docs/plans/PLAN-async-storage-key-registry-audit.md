# Implementation Plan

**Slug:** `async-storage-key-registry-audit`
**Author:** 📐 Quinn (TPM)
**Date:** 2026-06-30
**Wave:** 7 — ASYNC_STORAGE cluster (deep-dive audit)
**Risk class:** Mixed — one HIGH (R-24 UI logic), two MEDIUM (key centralization), two SPIKE (audit claims unverified against live code)

---

## Goal

Fix AsyncStorage key split-brain issues and the Group Connection Ground Truth (R-24) violation surfaced by the Wave 7 deep-dive audit.

Two of the six audit findings could NOT be reproduced against the live code (see **Audit Reconciliation** below) and are demoted to verification spikes rather than blind edits. Per P1 (Evidence Before Action), we do not author edits against claims we cannot reproduce in the source.

---

## Audit Reconciliation (P1 — Evidence vs. Audit Claims)

Before any edit, every finding was checked against the current source. Results:

| # | Audit claim | Verdict | Evidence |
|---|---|---|---|
| 1 | `DashboardHeader.tsx:106` R-24 violation | **CONFIRMED — FIX** | `src/components/dashboard/DashboardHeader.tsx:106` reads `if (firstDevice?.grouped && firstDevice?.groupId)` to gate grouped-session detection. |
| 2 | `DeviceStorage.ts` split-brain with InterrogatorService writing `@Sk8lytz_registered_devices` | **NOT REPRODUCED — SPIKE** | `DeviceStorage.ts:13` already imports `STORAGE_REGISTERED_DEVICES` from the central registry. `InterrogatorService.ts` has **zero** references to `registered_devices` (grep `@sk8_hw_\|HW_CACHE_KEY` returns only `@sk8_hw_` keys). No second writer found. |
| 3 | `InterrogatorService.ts:141` `@sk8_hw_<MAC>` case mismatch | **CONFIRMED — FIX** | `InterrogatorService.ts:25` builds key with `mac.toUpperCase()`; `storageKeys.ts:55` `getHardwareConfigKey` does NOT uppercase; `HardwareSetupWizardScreen.tsx:258` writes via `getHardwareConfigKey` (no uppercase). Two writers, divergent casing → split-brain. |
| 4 | `SpeedTrackingService.ts:423` recent_sessions split-brain with CrewAutoRejoin | **NOT REPRODUCED — SPIKE** | `SpeedTrackingService.ts:28` already imports `STORAGE_RECENT_SESSIONS_PREFIX` from the registry; both L423 (write) and L440 (read) use it. Grep for `recent_sessions` returns **no** `CrewAutoRejoin` hit. No divergent format found. |
| 5 | `useBLE.ts:250` blacklist local-const split-brain | **ALREADY FIXED — NO-OP** | `useBLE.ts:247` is `const CACHE_KEY = STORAGE_HARDWARE_BLACKLIST;` (imported at L21). It is an alias of the central key, NOT a divergent literal. No edit required; optional cleanup only (Step 5). |
| 6 | `BleCharacteristicCache.ts:23` `@sk8_gatt_` not centralized | **CONFIRMED (path corrected) — FIX** | Real path is `src/services/BleCharacteristicCache.ts` (NOT `src/services/ble/`). `CACHE_PREFIX = '@sk8_gatt_'` at L15 is a local literal not exported from `storageKeys.ts`. Comment at L14 falsely claims "Registered in storageKeys.ts registry" — it is not. |

**Net authorized work:** 3 fixes (Findings 1, 3, 6) + 1 registry addition + 1 optional cleanup (Finding 5) + 2 spikes (Findings 2, 4).

---

## Files to Modify

| File | Lines | Change | Finding |
|---|---|---|---|
| `src/components/dashboard/DashboardHeader.tsx` | 100–112 | R-24: gate grouped detection on `connectedCount > 1`, keep groupId lookup for render metadata | 1 |
| `src/constants/storageKeys.ts` | 55, +new | Add `getGattCacheKey()` export; make `getHardwareConfigKey` the canonical HW-cache builder (uppercase MAC) | 3, 6 |
| `src/services/ble/InterrogatorService.ts` | 25, 38, 43, 141 | Replace local `HW_CACHE_KEY` with imported `getHardwareConfigKey`; align read/write/scan casing | 3 |
| `src/services/BleCharacteristicCache.ts` | 15, 23, 29, 46 | Replace local `CACHE_PREFIX` literal with `getGattCacheKey()` import; fix stale comment | 6 |
| `src/screens/Onboarding/HardwareSetupWizardScreen.tsx` | 258 | Verify casing parity (read-only check; edit only if Step 2 changes the builder signature) | 3 |
| `src/hooks/useBLE.ts` | 247 | OPTIONAL cleanup: inline the redundant alias (no behavior change) | 5 |

---

## Source of Truth

- **R-24 rule:** `.claude/rules/prime-directive.md` — Hard Onboarding & BLE Invariants table, `[R-24] Group Connection Ground Truth`: *"Evaluate `isGrouped` sessions purely by checking `connectedDevices.length > 1` (do not rely on DisplayDevice fields)."*
- **Central key registry:** `src/constants/storageKeys.ts` (full file read 2026-06-30).
- **KB:** `KB: not applicable` — all changes target first-party code (AsyncStorage usage is internal; no external API behavior is asserted).

---

## Pre-Flight (Sage runs first, before any edit)

1. Confirm branch: `git branch --show-current` → MUST NOT be `master` (S1). Expected worktree: `async-storage-key-registry-audit`.
2. Confirm this plan was read in full and quote the "Files to Modify" table in the first message (S8).
3. Size check on each target file (S4 / Monolith Scan):
   - `DashboardHeader.tsx`, `storageKeys.ts`, `InterrogatorService.ts`, `BleCharacteristicCache.ts`, `useBLE.ts`.
   - **Verify:** none exceed 30KB. If any does → HALT and report. (`useBLE.ts` is the likely candidate — see Step 5 `[HIGH RISK]` note.)

---

## Step 1 — R-24 fix in DashboardHeader (Finding 1, HIGH)

**File:** `src/components/dashboard/DashboardHeader.tsx`
**Target lines:** 100–112 (read them immediately before editing per Look-Before-Leap).

**Problem (cited):** L106 `if (firstDevice?.grouped && firstDevice?.groupId)` decides grouped-vs-solo by inspecting `DisplayDevice` fields — a direct R-24 violation. Ground truth for "grouped" is the count of connected devices.

**The constraint that makes this surgical (not a rewrite):** the block at L107–110 still needs `group.deviceIds` to populate `expectedCount` and `groupIds`, which feed the inline skate-icon render at L128–134. So we change the **grouped decision** to the count-based ground truth while **retaining** the `firstDevice.groupId → customGroups` lookup purely to fetch render metadata.

**Edit — replace L100–112:**

```tsx
              const connectedCount = displayConnectedDevices.length;
              let expectedCount = 1;
              const firstDevice = displayConnectedDevices[0];
              let groupIds: string[] = [];

              // R-24: grouped session is ground-truthed by connected count, NOT DisplayDevice fields.
              if (connectedCount > 1) {
                // Resolve group metadata only for rendering the inline skate icons.
                const group = firstDevice?.groupId
                  ? customGroups.find(g => g.id === firstDevice.groupId)
                  : undefined;
                if (group) {
                  expectedCount = group.deviceIds.length;
                  groupIds = group.deviceIds;
                } else {
                  // Connected to >1 device with no resolvable custom group:
                  // fall back to the live connected set so the group view still renders.
                  expectedCount = connectedCount;
                  groupIds = displayConnectedDevices.map(d => d.id);
                }
              }
```

**Rationale for the `else` fallback:** under the old code, a multi-device session whose `groupId` did not resolve would have `expectedCount = 1` and silently render as Solo — masking real connected skates. The R-24 ground truth (`connectedCount > 1`) means we must render the group view even when no custom group is found; the live connected MACs are the safe fallback for `groupIds`.

**Verify:**
- `git diff HEAD src/components/dashboard/DashboardHeader.tsx` — confirm ONLY lines 100–112 region changed; no other JSX touched.
- `node tools/verifiable-check-runner.js` (or `npm run verify`) — expect 0 TypeScript errors. Specifically confirm no error on `firstDevice?.groupId` (optional chain is valid since `firstDevice` may be `undefined`).
- Logic trace (manual): solo session (`connectedCount === 1`) → `expectedCount` stays 1 → Solo view (L115 branch). Two+ devices → group view. Confirm against L115 `if (expectedCount <= 1)`.

---

## Step 2 — Canonicalize the HW-cache key builder in the registry (Findings 3 + 6)

**File:** `src/constants/storageKeys.ts`
**Target line:** 55 (read immediately before editing).

**Problem (cited):** Two HW-cache key builders exist with divergent casing:
- `storageKeys.ts:55` → `` `@sk8_hw_${mac}` `` (no case normalization)
- `InterrogatorService.ts:25` → `` `@sk8_hw_${mac.toUpperCase()}` ``

A device whose MAC arrives lowercase from one path and uppercase from another writes to two different keys → the interrogator's read at `InterrogatorService.ts:43` (which uppercases) misses the wizard's lowercase write → silent re-probe / data loss.

**Decision — canonical form is UPPERCASE.** Justification: the interrogator's read/scan path (`InterrogatorService.ts:38,43`) and `BleCharacteristicCache` (L23,29,46) already normalize to uppercase. Uppercase is the dominant existing convention; aligning the wizard builder to uppercase is the minimal-blast-radius choice.

**Edit A — replace L55:**

```ts
// Canonical HW-cache key builder. MAC is normalized to UPPERCASE so all
// writers (InterrogatorService, HardwareSetupWizardScreen) hit one key. (Wave 7 split-brain fix)
export const getHardwareConfigKey = (mac: string) => `@sk8_hw_${mac.toUpperCase()}`;
```

**Edit B — add a GATT-cache key builder (Finding 6) immediately after L55:**

```ts
// Canonical GATT adapter-cache key builder (used by BleCharacteristicCache).
// MAC normalized to UPPERCASE to match existing on-device entries. (Wave 7 centralization)
export const getGattCacheKey = (mac: string) => `@sk8_gatt_${mac.toUpperCase()}`;
```

**Verify:**
- `git diff HEAD src/constants/storageKeys.ts` — confirm only L55 changed + 2 lines added; no other key altered.
- `npm run verify` — expect 0 errors.
- Grep guard: `node tools/verifiable-check-runner.js` then manually confirm `getGattCacheKey` is exported (it must resolve as an import in Step 4).

---

## Step 3 — Align InterrogatorService onto the canonical builder (Finding 3, MEDIUM)

**File:** `src/services/ble/InterrogatorService.ts`
**Target lines:** 25 (definition), 38 + 43 (scan/load), 141 (write). Read each immediately before editing.

**Edit A — remove the local builder at L25 and import the canonical one.** Add to the import block (the existing `storageKeys`-free import region around L23):

```ts
import { getHardwareConfigKey } from '../../constants/storageKeys';
```

Then delete L25 (`const HW_CACHE_KEY = (mac: string) => ...;`).

**Edit B — L141 write call.** Replace `HW_CACHE_KEY(mac)` with `getHardwareConfigKey(mac)`:

```ts
      await AsyncStorage.setItem(getHardwareConfigKey(mac), JSON.stringify(hwConfig))
```

**Edit C — L38 + L43 scan path.** These use the raw prefix `'@sk8_hw_'`. The canonical builder produces `@sk8_hw_<UPPERCASE>`, so the prefix filter at L38 (`k.startsWith('@sk8_hw_')`) and the strip at L43 (`key.replace('@sk8_hw_', '').toUpperCase()`) remain correct — **leave L38 and L43 unchanged.** (They already uppercase on read, which now matches the canonical write.)

> Note: Do NOT introduce a `storageKeys` constant for the bare prefix; the scan needs the string-prefix form and the builder needs the full-key form. Keeping the prefix literal local to the scan loop is the surgical choice. Leave a clarifying comment above L38 only if Boy-Scout-cleaning the surrounding lines anyway.

**Verify:**
- `git diff HEAD src/services/ble/InterrogatorService.ts` — confirm: L25 removed, one import added, L141 changed to `getHardwareConfigKey`. L38/L43 UNCHANGED.
- `npm run verify` — expect 0 errors.
- Run the existing interrogator test: it lives at `src/services/ble/__tests__/InterrogatorService.test.ts` (uses keys `@sk8_hw_AA:BB:...` and `@sk8_hw_11:22:...` at L375–376). Confirm `npm run verify`'s Jest stage passes these — they assert uppercase keys, which the canonical builder now guarantees.

---

## Step 4 — Centralize BleCharacteristicCache prefix (Finding 6, LOW)

**File:** `src/services/BleCharacteristicCache.ts` (NOTE: `src/services/`, not `src/services/ble/`)
**Target lines:** 15 (prefix const + L12–14 comment), 23, 29, 46. Read immediately before editing.

**Edit A — L15.** Remove the local `const CACHE_PREFIX = '@sk8_gatt_';` and import the builder. Add to the import block (after L3):

```ts
import { getGattCacheKey } from '../constants/storageKeys';
```

**Edit B — fix the stale comment L12–14.** The current comment falsely claims the prefix is "Registered in storageKeys.ts registry." Replace L12–15 with:

```ts
// R-24: GATT adapter cache key. Built via getGattCacheKey() from the central
// storageKeys.ts registry. Format: '@sk8_gatt_<MAC_UPPERCASE>'.
```

**Edit C — L23, L29, L46.** Replace each `` `${CACHE_PREFIX}${mac.toUpperCase()}` `` with `getGattCacheKey(mac)` (the builder already uppercases, so drop the inline `.toUpperCase()`):
- L23: `const val = await AsyncStorage.getItem(getGattCacheKey(mac));`
- L29: `await AsyncStorage.removeItem(getGattCacheKey(mac));`
- L46: `await AsyncStorage.setItem(getGattCacheKey(mac), JSON.stringify(entry));`

> Leave L42 (`mac: mac.toUpperCase()` inside the stored `entry` object) UNCHANGED — that is the payload field, not the key.

**Verify:**
- `git diff HEAD src/services/BleCharacteristicCache.ts` — confirm L15 const removed, import added, comment fixed, 3 call sites use `getGattCacheKey(mac)`, L42 untouched.
- `npm run verify` — expect 0 errors.
- Confirm no other file imports `CACHE_PREFIX` from this module: grep `CACHE_PREFIX` returns hits only inside this file (already verified by Quinn: only `BleCharacteristicCache.ts` references it).

---

## Step 5 — OPTIONAL: useBLE blacklist alias cleanup (Finding 5)

**Status:** Finding 5 is **already correct** — `useBLE.ts:247` aliases the central key. This step is Boy-Scout cleanup ONLY, authorized but not required for correctness.

**File:** `src/hooks/useBLE.ts`
**Target line:** 247.

**`[HIGH RISK]` — `useBLE.ts` is a large, central hook.** Before touching it:
- Run the size check from Pre-Flight. If `useBLE.ts` > 30KB → **SKIP this step entirely** and append `// SKIPPED: useBLE.ts > 30KB monolith, S4 guard (Plan Step 5 optional cleanup)` to this plan file. Do not open it for a cosmetic change.
- If under 30KB: snapshot via `git diff HEAD src/hooks/useBLE.ts` (clean baseline) before editing.

**Edit (only if size check passes):** at L247, the local alias `const CACHE_KEY = STORAGE_HARDWARE_BLACKLIST;` may be inlined. Replace the three uses (L250, L268) of `CACHE_KEY` with `STORAGE_HARDWARE_BLACKLIST` directly and delete L247.

> If the inline change touches more than the alias declaration + its 2 usages, REVERT (`git checkout -- src/hooks/useBLE.ts`) and leave the alias in place. The alias is harmless.

**Verify:**
- `git diff HEAD src/hooks/useBLE.ts` — confirm ONLY L247 removed and L250/L268 use the constant directly. Zero other lines.
- `npm run verify` — expect 0 errors.

---

## Step 6 — SPIKE: verify Findings 2 & 4 (no edits)

These two audit claims were NOT reproduced. Sage performs a read-only confirmation and records the result; **no code edits.**

**Spike 2 (DeviceStorage registered-devices single-writer):**
- Command: `git grep -n "registered_devices\|STORAGE_REGISTERED_DEVICES"` across `src/`.
- **Expected:** writers are `DeviceStorage.ts` (`saveDevices` L34) only; any other hit is a read or the central constant. Confirm InterrogatorService does NOT appear.
- **Verify / record:** if a second writer IS found → HALT, do not fix, append a new `[FRICTION]` note and a follow-up `[🕵️ SPIKE]` task; this plan does not authorize the fix (P4 scope). If confirmed single-writer → record "Finding 2: single-writer confirmed, no split-brain" in the SESSION_LOG `[MERGE READY]` entry.

**Spike 4 (recent_sessions format parity):**
- Command: `git grep -n "recent_sessions\|STORAGE_RECENT_SESSIONS_PREFIX\|CrewAutoRejoin"` across `src/`.
- **Expected:** only `SpeedTrackingService.ts` (L28 import, L423 write, L440 read) via the central prefix; no `CrewAutoRejoin` consumer of this key.
- **Verify / record:** if a divergent format or second consumer IS found → HALT, append follow-up spike task, do not edit. If confirmed → record "Finding 4: single centralized format confirmed" in SESSION_LOG.

---

## Final Verification (all steps)

1. `npm run verify` — full suite (TSC + Jest + AST + TypeSafety + WorkflowValidator). **Expected: all gates green, 0 errors.**
2. `git diff HEAD --stat` — confirm ONLY these files changed: `DashboardHeader.tsx`, `storageKeys.ts`, `InterrogatorService.ts`, `BleCharacteristicCache.ts`, and (if Step 5 ran) `useBLE.ts`. No others.
3. No `as any` / `@ts-ignore` introduced (the `check-any-cast` hook will flag; expect zero new hits).
4. Append `[MERGE READY]` entry to `docs/SESSION_LOG.md` per sub-agent contract, listing every touched file, TSC ✅/Jest ✅, and the two spike results from Step 6.

---

## Docs Gate (P3 / VS-003)

This plan modifies BLE-adjacent AsyncStorage key construction. Before the gatekeeper, Avery must update `docs/SK8Lytz_App_Master_Reference.md`:
- **§2 AsyncStorage Key Registry:** confirm `getHardwareConfigKey` is documented as UPPERCASE-normalized and add `getGattCacheKey` (`@sk8_gatt_<MAC_UPPERCASE>`).
- **§4 Hook & Service Registry:** note `BleCharacteristicCache` and `InterrogatorService` now consume the centralized key builders.

---

## Out of Scope (HARD BOUNDARY — Sage must NOT open or modify these)

- **Finding 2 fix** — `DeviceStorage.ts` is NOT to be edited. It already uses the central key. Spike 6 only verifies; it does not fix.
- **Finding 4 fix** — `SpeedTrackingService.ts` is NOT to be edited. Spike 6 only verifies.
- **Any data migration** — we are NOT writing a migration to move existing lowercase `@sk8_hw_<mac>` entries to uppercase. Canonicalizing the builder means a one-time stale-key orphan on already-affected devices; the interrogator simply re-probes (self-healing). A migration would be a separate `[🕵️ SPIKE]` + task if the orphaned-key footprint is deemed worth reclaiming.
- **`HardwareSetupWizardScreen.tsx`** — read-only verification in Step 3 only. It consumes `getHardwareConfigKey`; since Step 2 changes the builder body (not its signature), the wizard needs NO edit. Do not modify it.
- **Test files** — `src/services/ble/__tests__/InterrogatorService.test.ts` is NOT to be modified; it already asserts uppercase keys, which the fix satisfies. If a test fails, the FIX is wrong, not the test — HALT and report.
- **`src/domain_files_dump.txt`** — generated artifact. Never touch.
- **Any other `storageKeys.ts` entry** — only L55 + 2 new lines. All other keys are frozen.
- **Refactoring the DashboardHeader render tree** beyond the L100–112 logic block. The JSX at L114–159 stays byte-identical except as a downstream consequence of the corrected `groupIds`/`expectedCount` values.
