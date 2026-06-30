# Implementation Plan

**Slug:** `ble-stability-hardening`
**Wave:** 3 — `BLE_STABILITY` cluster (2H, 9M, 19L) from `artifacts/system_audit_report.md`
**Author:** Quinn (TPM)
**Date:** 2026-06-30
**Risk Tier:** MEDIUM
**KB:** `capture required` — `tools/knowledge-base/INDEX.md` has no entry for `BleWriteQueue.enqueueDelay`, R-01 (write-queue gating), or R-16 (no-hardcoded-delays). Reyes must run `/kb-capture` for the BleWriteQueue delay-gating contract during execution.

---

## ⚠️ CRITICAL PRE-FLIGHT — AUDIT LINE-CITATION CORRECTION (P1 Evidence)

The Wave 3 audit findings cite `src/services/ble/BackgroundBLEService.ts:201` and `:255` as the home of "BLE device auto-connect retry backoff" and "reconnect retry with computed backoff." **This is incorrect and verified false by reading the file.**

- `BackgroundBLEService.ts` is **49 lines total** (verified `Read`, full file). It is a thin `react-native-background-actions` foreground-service wrapper. It contains exactly one `setTimeout` — the `sleep` helper on line 5 — and **zero** retry/backoff logic.
- The actual auto-connect retry-backoff `setTimeout` violations live in:
  - `src/services/ble/ConnectService.ts:210` — GATT-133 transient connect retry (`jitteredDelay(baseDelay, 500)` via raw `setTimeout`).
  - `src/services/ble/ConnectService.ts:246` and `:257` — MTU-glitch retry backoff (`jitteredDelay(backoffMs, 50)` via raw `setTimeout`).
  - `src/services/ble/RecoveryService.ts:78` — Phase 1/2 reconnect backoff (`getRecoveryBackoffMs` via raw `setTimeout`).
- The audit also claimed "hardcoded backoff caps at 10000ms." **False.** The cap is `BLE_TIMING.RECOVERY_MAX_MS = 30_000` (`src/constants/bleTimingConstants.ts:155`), already a named constant. There is no `10000` literal in the recovery path.

**This plan retargets the H/PROBABLE findings to their true source lines.** It does NOT modify `BackgroundBLEService.ts` (nothing to fix there).

---

## Goal

Harden BLE stability by routing the remaining raw `setTimeout` retry-backoff delays through the existing `BleWriteQueue.enqueueDelay()` gate (R-01 compliance), and by replacing the one remaining hardcoded XState delay literal (`1000`) in `BleMachine.ts` with a named constant (R-16 compliance). The wizard keep-alive timer is documented and deliberately deferred.

---

## Source of Truth (verified line citations)

| Ref | File | Lines | Verified Fact |
|---|---|---|---|
| SoT-1 | `src/services/ble/ConnectService.ts` | 203–214 | GATT-133 transient retry block. `delay = jitteredDelay(baseDelay, 500)` then `await new Promise(resolve => setTimeout(resolve, delay))` (L210). Not queue-gated. |
| SoT-2 | `src/services/ble/ConnectService.ts` | 244–258 | MTU-glitch retry. Two raw `setTimeout(res, jitteredDelay(backoffMs, 50))` at L246 and L257. Not queue-gated. |
| SoT-3 | `src/services/ble/RecoveryService.ts` | 73–78 | Phase 1/2 reconnect backoff. `await new Promise(r => setTimeout(r, backoff))` (L78). Not queue-gated. Cap = `RECOVERY_MAX_MS` 30_000 (already constant). |
| SoT-4 | `src/services/ble/BleMachine.ts` | 150–157 | `RESTORING` state `after: { 1000: { target: 'CONNECTING' ... } }` — hardcoded `1000` literal (R-16 violation). |
| SoT-5 | `src/screens/Onboarding/HardwareSetupWizardScreen.tsx` | 162–177 | Keep-alive polling `setTimeout(..., BLE_TIMING.PROBE_QUEUE_DELAY_MS)` (L169). Already uses a named constant; timer is cleaned up in the effect's return. UI timer, not a BLE write path. |
| SoT-6 | `src/services/BleWriteQueue.ts` | 226–237 | `enqueueDelay(priority: WritePriority, delayMs: number, generation?: number): Promise<boolean \| 'partial'>` — existing exported delay-gating primitive. |
| SoT-7 | `src/services/ble/ConnectService.ts` | 13 | `enqueueDelay` is ALREADY imported: `import { WritePriority, enqueueDelay, setConnectionPriorityCallbacks } from '../BleWriteQueue';` |
| SoT-8 | `src/services/ble/ConnectService.ts` | 308 | Existing precedent: `await enqueueDelay('critical', handshake.interPacketDelayMs);` — confirms the call shape used elsewhere in this file. |
| SoT-9 | `src/services/ble/RecoveryService.ts` | 10 | RecoveryService imports `{ enqueueWrite, clearWriteQueue }` from `../BleWriteQueue` — `enqueueDelay` is NOT yet imported here. |
| SoT-10 | `src/constants/bleTimingConstants.ts` | 137, 149–185 | `GATT_CONNECT_BACKOFF_MS = [500,1500,4000]`, `RECOVERY_MAX_MS = 30_000`, `MTU_RETRY_SETTLE_MS = 200`. All named constants — no cap change needed. |

---

## File Size / Risk Scan (S4 Monolith Gate)

Run this BEFORE any edit:

```powershell
Get-Item src\services\ble\ConnectService.ts, src\services\ble\RecoveryService.ts, src\services\ble\BleMachine.ts, src\screens\Onboarding\HardwareSetupWizardScreen.tsx | Select-Object Name, @{N='KB';E={[math]::Round($_.Length/1KB,1)}}
```

- If any file reports **> 20KB** → flag `[HIGH RISK]` and snapshot via `git` before editing that file: `git diff HEAD <file>` baseline noted, edit, then `git diff HEAD <file>` again.
- If any file reports **> 30KB** → S4 HARD STOP. Halt and report to the user. Do not edit; extract first.

---

## Files to Create / Modify

1. `src/services/ble/ConnectService.ts` — L210, L246, L257: replace raw `setTimeout` retry delays with `enqueueDelay('critical', delay)`.
2. `src/services/ble/RecoveryService.ts` — L10 import + L78: import `enqueueDelay`, replace raw `setTimeout` backoff with `enqueueDelay('critical', backoff)`.
3. `src/services/ble/BleMachine.ts` — top-of-file constant + L152: introduce `BLE_RESTORING_TIMEOUT_MS = 1000` and reference it in the `after` key.
4. `src/screens/Onboarding/HardwareSetupWizardScreen.tsx` — L168 area: add a deferral `// TODO` comment only. No behavioral change. LOW priority.

---

## Execution Steps

### STEP 1 — ConnectService: queue-gate the GATT-133 transient retry delay (SoT-1)

**File:** `src/services/ble/ConnectService.ts`
**Target:** L210 (inside the `if (isTransient && attempt < 3)` block, lines 203–214).

`enqueueDelay` is already imported at L13 (SoT-7), so no import change is needed in this file.

- **Look-before-leap:** `Read` L203–214 to confirm the exact text before editing.
- **Replace** exactly:
  ```ts
            await new Promise(resolve => setTimeout(resolve, delay));
  ```
  **with:**
  ```ts
            // R-01: route the retry backoff through the BLE write queue instead of a raw timer.
            await enqueueDelay('critical', delay);
  ```
- Leave the `jitteredDelay(baseDelay, 500)` computation on L205 UNCHANGED. Only the wait mechanism changes.

**Verify (STEP 1):**
- Run `git diff HEAD src/services/ble/ConnectService.ts` and confirm exactly ONE line removed (the `setTimeout` wait) and TWO added (comment + `enqueueDelay`). No other lines changed.
- Confirm L205 (`jitteredDelay`) is untouched in the diff.

---

### STEP 2 — ConnectService: queue-gate the two MTU-glitch retry delays (SoT-2)

**File:** `src/services/ble/ConnectService.ts`
**Target:** L246 and L257 (inside the MTU negotiation loop, lines 238–259).

There are TWO identical waits. Both must change. Because the lines are identical (`await new Promise(res => setTimeout(res, jitteredDelay(backoffMs, 50)));`), edit them ONE AT A TIME using surrounding context to disambiguate (do NOT use `replace_all` — the surrounding `backoffMs` declarations differ in their preceding lines and the diff must be reviewable per-site).

- **Look-before-leap:** `Read` L244–258 to confirm both sites.
- **Site A (success-path glitch, after L245 `backoffMs` declaration):** replace
  ```ts
              const backoffMs = BLE_TIMING.MTU_RETRY_SETTLE_MS * Math.pow(2, mtuAttempt - 1);
              await new Promise(res => setTimeout(res, jitteredDelay(backoffMs, 50)));
  ```
  with
  ```ts
              const backoffMs = BLE_TIMING.MTU_RETRY_SETTLE_MS * Math.pow(2, mtuAttempt - 1);
              // R-01: route the MTU retry backoff through the BLE write queue.
              await enqueueDelay('critical', jitteredDelay(backoffMs, 50));
  ```
  (This pairs the comment+enqueue with the `AppLogger.warn(... MTU glitch (23) ...)` branch context above it at L244.)
- **Site B (catch-path, after L256 `backoffMs` declaration):** apply the identical replacement. Disambiguate by including the preceding catch-block context (the `AppLogger.warn('[ConnectService] MTU negotiation attempt failed', ...)` block at L249–255) in the `old_string` so the match is unique.

**Verify (STEP 2):**
- Run `git diff HEAD src/services/ble/ConnectService.ts`. Confirm BOTH MTU sites now call `enqueueDelay('critical', jitteredDelay(backoffMs, 50))` and neither retains `setTimeout`.
- Confirm the `Math.pow(2, mtuAttempt - 1)` backoff math on L245/L256 is unchanged.
- Confirm no `setTimeout` remains anywhere in the MTU loop: `Grep` `setTimeout` in `ConnectService.ts` — expect only the unrelated L178 settle (out of scope, see below).

---

### STEP 3 — RecoveryService: import + queue-gate the reconnect backoff (SoT-3, SoT-9)

**File:** `src/services/ble/RecoveryService.ts`

**3a — Import:** `enqueueDelay` is NOT yet imported here (SoT-9, current import is `{ enqueueWrite, clearWriteQueue }` at L10).
- **Look-before-leap:** `Read` L10.
- Replace:
  ```ts
  import { enqueueWrite, clearWriteQueue } from '../BleWriteQueue';
  ```
  with:
  ```ts
  import { enqueueWrite, clearWriteQueue, enqueueDelay } from '../BleWriteQueue';
  ```

**3b — Backoff wait:** Target L78 inside the Phase 1/2 loop (lines 73–78).
- **Look-before-leap:** `Read` L73–82.
- Replace:
  ```ts
        await new Promise(r => setTimeout(r, backoff));
  ```
  with:
  ```ts
        // R-01: route the reconnect backoff through the BLE write queue.
        await enqueueDelay('critical', backoff);
  ```
- Leave the `backoff` computation on L75–77 UNCHANGED (the cap is already `RECOVERY_MAX_MS = 30_000`, a named constant — SoT-10; the audit's "10000ms cap" claim was false, no change required).

**Verify (STEP 3):**
- Run `git diff HEAD src/services/ble/RecoveryService.ts`. Confirm: one import edited (adds `enqueueDelay`), one `setTimeout` wait replaced by `enqueueDelay('critical', backoff)`.
- Confirm L75–77 backoff math untouched.
- `Grep` `setTimeout` in `RecoveryService.ts` — the remaining hits at L150 (`RECOVERY_PING_SETTLE_MS`) and L183 (`RECOVERY_PHASE_3_POLL_INTERVAL_MS`) are OUT OF SCOPE for this task (settle/poll timers, not retry backoff). Do NOT touch them. Confirm they still use named constants.

---

### STEP 4 — BleMachine: replace the hardcoded `1000` XState delay literal (SoT-4, R-16)

**File:** `src/services/ble/BleMachine.ts`
**Target:** L152 (`RESTORING` state, `after: { 1000: {...} }`).

XState `after` keys must be a numeric literal or a string key referencing a `delays` config. A bare named constant CANNOT be inlined as an object key. Use the named constant as a **computed property key** so the literal `1000` is eliminated while preserving XState semantics.

**4a — Declare the constant.** At the top of `BleMachine.ts`, immediately after the existing import block (Sage: `Read` the top 20 lines first to find the exact insertion point after the last import). Add:
```ts
/** R-16: named delay for the RESTORING → CONNECTING settle window (replaces inline 1000ms literal). */
const BLE_RESTORING_TIMEOUT_MS = 1000;
```

**4b — Reference it as a computed key.** `Read` L150–157 first. Replace:
```ts
    RESTORING: {
      after: {
        1000: {
          target: 'CONNECTING',
          actions: [{ type: 'logTransition', params: { from: 'RESTORING', to: 'CONNECTING', reason: 'restore_delay_elapsed' } }]
        }
      }
    },
```
with:
```ts
    RESTORING: {
      after: {
        [BLE_RESTORING_TIMEOUT_MS]: {
          target: 'CONNECTING',
          actions: [{ type: 'logTransition', params: { from: 'RESTORING', to: 'CONNECTING', reason: 'restore_delay_elapsed' } }]
        }
      }
    },
```

**Verify (STEP 4):**
- Run `git diff HEAD src/services/ble/BleMachine.ts`. Confirm: one constant added near the top, and the `after` key changed from `1000` to `[BLE_RESTORING_TIMEOUT_MS]`. No other state nodes changed.
- `Grep` `\b1000\b` in `BleMachine.ts` — confirm the only remaining `1000` is in the new constant declaration, not in any `after` block.
- TypeScript note for Sage: a computed numeric property key is legal in an object literal and resolves to the same `after` delay. If `tsc` complains about the computed key type, do NOT cast to `any` (S3) — the constant is already typed `number`; report to Quinn for plan revision instead.

---

### STEP 5 — Wizard keep-alive timer: document + defer (SoT-5, LOW priority)

**File:** `src/screens/Onboarding/HardwareSetupWizardScreen.tsx`
**Target:** L162–172 (the keep-alive polling `useEffect`).

This is a UI-layer polling timer, not a BLE write path. It already uses the named constant `BLE_TIMING.PROBE_QUEUE_DELAY_MS` (L171) and is correctly cleaned up in the effect's return (L173–175). Migrating it to the write queue is NOT appropriate here — the queue gates BLE writes/delays on the transport, while this timer schedules a UI-triggered re-scan. Document the rationale and defer.

- **Look-before-leap:** `Read` L162–172.
- Insert a comment directly ABOVE the `timer = setTimeout(...)` line at L169 (do not alter the timer behavior):
  ```ts
      // TODO (LOW / deferred — ble-stability-hardening): UI-layer keep-alive re-scan timer.
      // Intentionally NOT routed through BleWriteQueue.enqueueDelay — this schedules a UI
      // re-scan trigger, not a BLE write/transport delay. Uses named constant
      // BLE_TIMING.PROBE_QUEUE_DELAY_MS and is cleaned up in the effect return. Revisit only
      // if scan scheduling moves into the transport layer.
  ```

**Verify (STEP 5):**
- Run `git diff HEAD src/screens/Onboarding/HardwareSetupWizardScreen.tsx`. Confirm the diff is COMMENT-ONLY — zero executable lines changed. The `setTimeout(...)` call, its argument, and the cleanup return must be byte-identical to before.

---

### STEP 6 — Full verification gate (S7)

Run the pre-installed suite ONLY. Do NOT run raw `tsc`/`jest` (S7 hard ban).

```
npm run verify
```

**Verify (STEP 6):**
- Expect: TSC `0 errors`, Jest all green, AST/TypeSafety/WorkflowValidator pass.
- Targeted regression focus — these tests mock `enqueueDelay` and exercise the changed paths (confirm they pass):
  - `src/services/ble/__tests__/RecoveryService.test.ts` (mocks `enqueueDelay` at L47; backoff/cancel cases at L266, L328–336).
  - `src/services/ble/__tests__/ConnectService.test.ts` (GATT retry Group B at L281).
  - `src/services/ble/__tests__/InterrogatorService.test.ts` (mocks `enqueueDelay` at L58 — unchanged, sanity only).
- If `RecoveryService.test.ts` "cancel during Phase 1 backoff" (L328) fails: the cancel path previously awaited a raw `setTimeout`; it now awaits `enqueueDelay`. Confirm the `cancelled`/`abortController.abort()` flag is still checked AFTER the await (L80 `if (cancelled) break;`). If the test mock's `enqueueDelay` resolves synchronously vs. the real timer, the cancel ordering may shift — if it fails, HALT and report to Quinn; do NOT re-architect the cancel logic ad hoc (this risks an S5 strike loop).

---

### STEP 7 — Docs parity (Avery / VS-003) + KB capture (Reyes)

Per the Documentation Parity Gate (VS-003) — this task modifies BLE retry/recovery behavior:
- Update `docs/SK8Lytz_App_Master_Reference.md` §3 (BLE Protocol Library) to note that GATT-133 transient retry, MTU-glitch retry, and recovery Phase 1/2 backoff delays are now queue-gated via `enqueueDelay('critical', ...)`.
- Reyes runs `/kb-capture` for the `BleWriteQueue.enqueueDelay` delay-gating contract (KB had no entry — see header `KB: capture required`).

**Verify (STEP 7):**
- `git diff HEAD docs/SK8Lytz_App_Master_Reference.md` shows the §3 note added.
- `node tools/kb-validator.js --summary` runs clean.

---

## Out of Scope (HARD BOUNDARY — Sage must NOT open or modify these)

- `src/services/ble/BackgroundBLEService.ts` — audit mis-cited this file (49 lines, no retry/backoff logic). Nothing to change. Do NOT edit.
- **Watch target Swift code** — `ios/...targets/watch:128` (`Timer.scheduledTimer(withTimeInterval: 10.0, ...)`). Native watch auto-dismiss timer. Out of scope for JS changes. Documented only.
- **Test helper `setTimeout`** — `__tests__/test helpers:39` (`setTimeout(done, 500)` in `beforeAll`). Test-isolation timing. Do NOT touch (changing it risks flaky test runs).
- **iOS TimedScheduler / any iOS-native scheduling.**
- `ConnectService.ts:178` — the Android-12 `connectedDevices` settle `setTimeout(r, 200)`. This is a hardware-race settle, not a retry backoff. Leave it. (If a future task wants it queue-gated, file separately.)
- `RecoveryService.ts:150` (`RECOVERY_PING_SETTLE_MS`) and `:183` (`RECOVERY_PHASE_3_POLL_INTERVAL_MS`) — settle/poll timers, already named constants, not retry backoff. Do NOT touch.
- `src/constants/bleTimingConstants.ts` — NO cap changes. The audit's "10000ms cap" claim was false; `RECOVERY_MAX_MS` is already `30_000`. Do NOT edit constants.
- Any refactor of `jitteredDelay` (`src/utils/backoff.ts`), the `BleWriteQueue` internals, or the XState machine structure beyond the single `after` key in STEP 4.
- No Boy Scout cleanup outside the exact lines listed in Steps 1–5 (P4). Found a defect in an out-of-scope line? Leave a `// TODO:` comment; do not fix.
