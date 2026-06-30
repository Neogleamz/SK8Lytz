# Implementation Plan

**Slug:** `reentrancy-guards`
**Author:** 📐 Quinn (TPM)
**Date:** 2026-06-30
**Source Audit:** `artifacts/system_audit_report.md` — Wave 9 REENTRANCY cluster (0H, 9M, 3L)
**Worktree:** `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\reentrancy-guards\`

---

## ⚠️ EVIDENCE-FIRST PREFLIGHT — AUDIT/TREE DISCREPANCY (P1 Conflict Halt)

Quinn read every cited target **before** writing this plan. The audit's file paths and line numbers are **stale against the current master tree**. The following discrepancies are confirmed by direct file reads (cited inline below) and Sage MUST treat the *live evidence* — not the audit text — as the Source of Truth:

| Audit citation | Reality (verified) | Disposition |
|---|---|---|
| `src/hooks/useVisualizerLeds.ts:142` (O(n²) loop) | File does NOT exist. The real hook is `useVisualizerLeds` inside **`src/components/visualizer/VisualizerHooks.ts`**. The O(n²) inner loop is at **L198–L205**, not L142. | **FIX** (real finding) |
| `src/services/ble/HeartbeatService.ts:106` (async setInterval, NO guard) | File exists at `src/services/ble/HeartbeatService.ts` but is only 89 lines (no L106). The interval callback at **L23** is **ALREADY GUARDED** by `let isRunning = false` (L21), checked at L24, set at L28, reset in `finally` at L83. | **VERIFY-ONLY** (audit was wrong; no code change) |
| `src/services/ble/MusicModeService.ts:69` (guarded reference) | File does NOT exist anywhere in the tree. The music hook is `src/hooks/useMusicMode.ts` — a 0x73 config-dispatch hook with **NO setInterval**. | **N/A** (phantom file — ignore) |
| `src/protocols/SymphonyEngine.ts:100` (20Hz async setInterval, guarded ref) | File exists but contains **NO setInterval, no async polling** — it is a pure synchronous pixel-frame builder. | **VERIFY-ONLY** (no streaming loop present) |
| `src/screens/DashboardScreen.tsx:249` (async `recover()` in useEffect, NO guard) | L249 is a destructuring assignment (`setDeviceConfigs,`). There is **no `recover()` function** in the file. The closest real async-in-useEffect is `checkNewDevice` at **L452–L478**, which ALREADY has an `isMounted` guard (L456/L465/L476) but **lacks a re-entrancy flag**. | **FIX** (harden the real effect) |

**Net actionable scope after evidence reconciliation:**
- **1 real code fix:** O(n²) LED loop in `VisualizerHooks.ts`.
- **1 hardening fix:** re-entrancy flag on the `checkNewDevice` effect in `DashboardScreen.tsx`.
- **3 verify-only items:** HeartbeatService (already guarded), SymphonyEngine (no loop), MusicModeService (phantom).

> If the user wants the audit re-run against the current tree to regenerate accurate line numbers for the full 9M/3L set, that is a separate `[🕵️ SPIKE]` task — out of scope here.

---

## Goal

Add a re-entrancy guard to the one async `useEffect` callback that lacks one, fix the O(n²) per-render LED placement loop, and formally verify that the already-guarded interval callbacks (HeartbeatService) and the non-existent loops (SymphonyEngine, MusicModeService) require no change.

---

## Files to Modify

| # | File | Lines | Change |
|---|---|---|---|
| 1 | `src/components/visualizer/VisualizerHooks.ts` | L198–L205 (+ L142, L188) | Precompute a `targetLength → sample index` lookup so the inner `for j` scan is removed; document residual complexity. |
| 2 | `src/screens/DashboardScreen.tsx` | L452–L478 | Add `isCheckingRef` re-entrancy flag alongside the existing `isMounted` guard. |

## Files to VERIFY ONLY (read, confirm, do NOT edit)

| File | Lines | What to confirm |
|---|---|---|
| `src/services/ble/HeartbeatService.ts` | L21, L24, L28, L83 | `isRunning` guard present and correctly bracketed (check → set true → `finally` reset). |
| `src/protocols/SymphonyEngine.ts` | whole file | No `setInterval` / async streaming loop exists. |

---

## Step-by-Step

### STEP 0 — Worktree & branch setup

```powershell
cd C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz
git worktree list
git worktree add ..\SK8Lytz-worktrees\reentrancy-guards -b reentrancy-guards
New-Item -ItemType Junction -Path ..\SK8Lytz-worktrees\reentrancy-guards\node_modules -Target C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\node_modules
cd ..\SK8Lytz-worktrees\reentrancy-guards
git branch --show-current
```

**Verify:** Run `git branch --show-current` — expect output `reentrancy-guards` (NOT `master`). If `master` → HALT (S1).

---

### STEP 1 — VERIFY-ONLY: confirm HeartbeatService is already guarded (NO EDIT)

**Source of Truth:** `src/services/ble/HeartbeatService.ts` L19–L89.
**KB:** N/A (internal code; no external API assertion).

Read the file. Confirm the following four anchors exist exactly:

- L21: `let isRunning = false;`
- L24: `if (isRunning) return;` (first statement inside the `setInterval` async callback at L23)
- L28: `isRunning = true;` (set before the first `await`)
- L82–L84: `} finally {` / `isRunning = false;` / `}`

This is the canonical re-entrancy pattern. The audit's claim that L106 lacks a guard is **false** (the file has no L106). **Make NO change to this file.**

**Verify:** Run `git diff HEAD src/services/ble/HeartbeatService.ts` — expect **empty output** (zero changes). If any diff appears → `git checkout -- src/services/ble/HeartbeatService.ts`.

---

### STEP 2 — VERIFY-ONLY: confirm SymphonyEngine has no streaming loop (NO EDIT)

**Source of Truth:** `src/protocols/SymphonyEngine.ts` (full file).
**KB:** N/A.

Confirm there is no `setInterval`, no `requestAnimationFrame`, no async polling callback in the file. It is a synchronous frame builder consuming a `magnitude` argument (see L61, L68). The audit's "20Hz async setInterval at L100" does not exist. **Make NO change.**

**Verify:** Run `git diff HEAD src/protocols/SymphonyEngine.ts` — expect **empty output**.

---

### STEP 3 — FIX: re-entrancy flag on the `checkNewDevice` effect

**Source of Truth:** `src/screens/DashboardScreen.tsx` L452–L478 (the `useEffect` whose deps are `[pendingRegistrations, checkDeviceClaimed, viewState]`).
**KB:** N/A (internal effect; `checkDeviceClaimed` is a project hook, not an external API).

**Current state (verified, L452–L478):**
```tsx
  useEffect(() => {
    if (pendingRegistrations.length === 0) return;
    if (viewState === 'SETUP_WIZARD') return; // Ignore if wizard is already active

    let isMounted = true;
    // Only check if we are in dashboard mode and a new untracked device appears
    const checkNewDevice = async () => {
      const first = pendingRegistrations[0];
      if (!first) return;
      const status = await checkDeviceClaimed(first.device_mac, {
        firmwareVer: first.firmware_ver,
        productId:   first.product_id,
      });
      if (!isMounted) return;
      ...
    };

    checkNewDevice();
    return () => {
      isMounted = false;
    };
  }, [pendingRegistrations, checkDeviceClaimed, viewState]);
```

**Problem:** `checkNewDevice` awaits an async claim check. If `pendingRegistrations` / `viewState` change rapidly during BLE state transitions, the effect re-fires and a second `checkNewDevice` can run while the first's `await` is in flight — concurrent `checkDeviceClaimed` calls interleave.

**3.1 — Add a module-stable re-entrancy ref near the existing dashboard refs.**

Locate the ref cluster around L240 (`const retriggerAutoConnectRef = React.useRef<() => void>(() => {});`). Immediately after L240, add:

```tsx
  // Re-entrancy guard for the async checkNewDevice effect (Wave 9 REENTRANCY).
  // Prevents overlapping checkDeviceClaimed() calls when pendingRegistrations/
  // viewState churn during BLE state transitions.
  const isCheckingNewDeviceRef = React.useRef(false);
```

**3.2 — Guard the async callback.** Replace the effect body at L452–L478. The `isMounted` local stays; add the flag check at the top of `checkNewDevice`, set it true before the `await`, and clear it in a `finally`:

```tsx
  useEffect(() => {
    if (pendingRegistrations.length === 0) return;
    if (viewState === 'SETUP_WIZARD') return; // Ignore if wizard is already active

    let isMounted = true;
    // Only check if we are in dashboard mode and a new untracked device appears
    const checkNewDevice = async () => {
      if (isCheckingNewDeviceRef.current) return; // re-entrancy guard
      const first = pendingRegistrations[0];
      if (!first) return;
      isCheckingNewDeviceRef.current = true;
      try {
        const status = await checkDeviceClaimed(first.device_mac, {
          firmwareVer: first.firmware_ver,
          productId:   first.product_id,
        });
        if (!isMounted) return;
        // Allow registration for both unclaimed devices AND when we can't verify
        // claim status due to being offline (BUG: offline_unknown was blocking post-FTUE device adds)
        if (status === 'unclaimed' || status === 'offline_unknown') {
          // Wait, pendingNewDevice was used for the "New Device Found" modal. Let's just log it.
          AppLogger.log('BLE_STATE_CHANGE', { event: 'new_unclaimed_device_found', deviceId: scrubPII(first.device_mac) });
        }
      } finally {
        isCheckingNewDeviceRef.current = false;
      }
    };

    checkNewDevice();
    return () => {
      isMounted = false;
    };
  }, [pendingRegistrations, checkDeviceClaimed, viewState]);
```

> Note: the early `if (!first) return;` is placed BEFORE setting the flag so an empty queue never strands the flag in `true`. The flag is only set once we are committed to the `await`, and the `try/finally` guarantees it is always cleared even on throw.

**Verify (3.x):**
- Run `git diff HEAD src/screens/DashboardScreen.tsx`. Expect exactly two hunks: (a) the new `isCheckingNewDeviceRef` declaration after L240, (b) the rewritten effect at L452–L478. If any other line changed → `git checkout -- src/screens/DashboardScreen.tsx` and redo surgically.
- Run `node tools/verifiable-check-runner.js` — expect TSC 0 errors. The `try/finally` introduces no new types.
- Manual reasoning check: confirm the flag is set inside `try` and cleared in `finally`, and that the `if (!first) return;` sits above the flag-set line.

---

### STEP 4 — FIX: eliminate the O(n²) LED placement loop

**Source of Truth:** `src/components/visualizer/VisualizerHooks.ts` — `useVisualizerLeds`, outer loop L188 (`for (let i = 0; i < renderLeds; i++)`), inner scan L198–L205.
**KB:** N/A (pure JS/math; no external library).

**Current state (verified, L188–L205):**
```tsx
    for (let i = 0; i < renderLeds; i++) {
      ...
      const targetLength = (i / renderLeds) * totalLength;
      let p1 = pathSamples[lastSampleIdx];
      let p2 = pathSamples[lastSampleIdx + 1] || p1;

      for (let j = lastSampleIdx; j < pathSamples.length - 1; j++) {
        if (pathSamples[j + 1].length >= targetLength) {
          p1 = pathSamples[j];
          p2 = pathSamples[j + 1];
          lastSampleIdx = j;
          break;
        }
      }
      ...
```

**Complexity reality (verified):** `pathSamples` has `numSamples + 1 = 5001` entries (`useVisualizerPath`, L64). `renderLeds` is up to `numLeds * 2 = 86` for SOULZ (L150). The inner loop resumes from `lastSampleIdx` and `pathSamples[].length` is monotonically non-decreasing (built cumulatively at L130/L133), so the scan **advances monotonically** and never rewinds. Across the full outer loop the inner pointer sweeps `pathSamples` at most **once total** — the *real* aggregate cost is **O(renderLeds + numSamples)**, not O(n²).

> The audit labeled this O(n²). Evidence shows it is effectively O(n + m) due to the monotonic `lastSampleIdx` resume. The fix is therefore **documentation + a correctness guard**, not a rewrite. Per P4 (Surgical Before Heroic) we do NOT introduce a precomputed Map — it would add allocation and dependency churn for zero measurable gain at n=86. The audit's "extract into useMemo Map" suggestion is explicitly rejected here on evidence grounds.

**4.1 — Add a `PERF:` comment documenting the verified complexity.** Immediately above L188 (`for (let i = 0; i < renderLeds; i++) {`), insert:

```tsx
    // PERF: outer×inner looks O(n²) but `lastSampleIdx` resumes monotonically and
    // pathSamples[].length is non-decreasing, so the inner scan sweeps pathSamples
    // at most once total across all LEDs → aggregate O(renderLeds + numSamples).
    // n ≤ 86 LEDs, numSamples = 5001. Acceptable; no Map precompute needed (Wave 9 REENTRANCY).
```

**4.2 — Add a defensive clamp so the inner scan never re-scans from a stale index if `targetLength` ever decreases (it should not, but this makes the monotonic assumption explicit and crash-safe).** Replace the inner loop at L198–L205:

Current:
```tsx
      for (let j = lastSampleIdx; j < pathSamples.length - 1; j++) {
        if (pathSamples[j + 1].length >= targetLength) {
          p1 = pathSamples[j];
          p2 = pathSamples[j + 1];
          lastSampleIdx = j;
          break;
        }
      }
```

Replacement:
```tsx
      // Advance the resume pointer forward only; pathSamples[].length is monotonic.
      while (
        lastSampleIdx < pathSamples.length - 2 &&
        pathSamples[lastSampleIdx + 1].length < targetLength
      ) {
        lastSampleIdx++;
      }
      p1 = pathSamples[lastSampleIdx];
      p2 = pathSamples[lastSampleIdx + 1] || p1;
```

> This is behaviorally identical to the `for/break` (it stops at the first sample whose forward length ≥ `targetLength`) but is unambiguously single-pass and removes the dead `p1/p2` pre-assignment at L195–L196 (those are now assigned once after the `while`). The Boy Scout cleanup here is in-scope because we are already editing these exact lines.

**4.3 — Remove the now-redundant pre-assignment.** The lines immediately above the loop (L195–L196):
```tsx
      let p1 = pathSamples[lastSampleIdx];
      let p2 = pathSamples[lastSampleIdx + 1] || p1;
```
…become unused-before-reassignment after 4.2. Convert them so `p1`/`p2` are declared (with `let`) at the point of the post-`while` assignment instead. Final shape of the block (L194 onward):

```tsx
      const targetLength = (i / renderLeds) * totalLength;

      // Advance the resume pointer forward only; pathSamples[].length is monotonic.
      while (
        lastSampleIdx < pathSamples.length - 2 &&
        pathSamples[lastSampleIdx + 1].length < targetLength
      ) {
        lastSampleIdx++;
      }
      const p1 = pathSamples[lastSampleIdx];
      const p2 = pathSamples[lastSampleIdx + 1] || p1;
```

> Net: two `let` declarations + a `for/break` collapse into one `while` + two `const`. `p1`/`p2` are read at L207–L210 (`segmentLength`, `left`, `top`) — confirm those reads still compile against `const`.

**Verify (4.x):**
- Run `git diff HEAD src/components/visualizer/VisualizerHooks.ts`. Expect exactly: (a) the `PERF:` comment block above the outer loop, (b) the `for/break` → `while` conversion, (c) the `let p1/p2` → `const p1/p2` relocation. No other hunks. If anything else changed → `git checkout -- src/components/visualizer/VisualizerHooks.ts` and redo.
- Run `node tools/verifiable-check-runner.js` — expect TSC 0 errors (verifies `const p1/p2` are not reassigned downstream).
- **Behavioral equivalence check (mandatory):** confirm that for a monotonic `targetLength` sequence the `while` stops at the same index the `for/break` would have. Reason it through against L207–L210: `segmentLength = p2.length - p1.length`, then `t` interpolation. Same `p1`/`p2` ⇒ identical render output.

---

### STEP 5 — Full verification gate

```powershell
cd C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\reentrancy-guards
npm run verify
```

**Verify:** Expect `npm run verify` to report TSC ✅, Jest ✅, AST ✅, TypeSafety ✅, WorkflowValidator ✅ — 0 failures. If the `check-any-cast` hook fires → no `as any` was introduced (none should be); investigate and fix the type properly. Do NOT proceed to gatekeeper on any red gate.

---

### STEP 6 — QA 5-case checklist (Blake)

Before commit, run the `/qa-tester` 5-case checklist. Minimum cases to assert for this change:

1. **Re-entrancy (DashboardScreen):** simulate `pendingRegistrations` changing twice while `checkDeviceClaimed` is mid-`await` → second invocation returns early via `isCheckingNewDeviceRef.current`; flag is cleared in `finally`.
2. **Empty-queue flag safety:** `pendingRegistrations` becomes `[{...}]` then the effect's `first` is falsy on a transient → confirm flag is NOT left `true` (early return sits above flag-set).
3. **Unmount during await:** unmount mid-`await` → `isMounted` false short-circuits the log, `finally` still clears the flag.
4. **LED render parity:** render `useVisualizerLeds` for SOULZ (numLeds high, vizShape default, renderLeds=86) and confirm the `list` length and first/last LED `position` match pre-change output (the `while` must produce identical `p1/p2`).
5. **LED edge case:** `totalLength === 0` (degenerate path) → `targetLength` is 0 for all i, `while` never advances, no crash, `p2` falls back to `p1`.

**Verify:** Blake returns PASS ✅ with edge-case report. Any FAIL → route back to Sage, do NOT commit.

---

### STEP 7 — Docs parity (Avery)

This change adds **no new hook, service, component, BLE opcode, or type**. It is an internal hardening + perf-doc change. **No Master Reference §3/§4 update is required.** Avery confirms parity is trivially clear.

**Verify:** Avery confirms "no new exports in diff" via `git diff HEAD --stat` showing only `VisualizerHooks.ts` and `DashboardScreen.tsx`.

---

### STEP 8 — Commit, SESSION_LOG, gatekeeper

```powershell
git status --short
git add src/components/visualizer/VisualizerHooks.ts src/screens/DashboardScreen.tsx
git commit -m "fix(reentrancy): guard checkNewDevice effect + single-pass LED scan (Wave 9)"
```

Append a `[MERGE READY]` entry to `docs/SESSION_LOG.md` per the Surgeon Enforcement Contract (files touched, TSC ✅, Jest ✅).

Then hand to Taylor for `fortress-gatekeeper.ps1` (fast-forward merge only). After merge: `[MERGE]` SESSION_LOG entry → `node tools/auto-archiver.js --task reentrancy-guards` → ACTIVE SPRINT header sync (Rule 12.2) → Discord notify.

**Verify:** `git log --oneline -3` on master shows the merge commit; `npm run verify` was the last attestation before any push.

---

## Out of Scope (HARD BOUNDARY — do NOT open or edit)

- **`src/context/SessionContext.tsx`** — already correctly guarded (audit reference-only). Do not touch.
- **`src/hooks/useMusicMode.ts`** — the audit's `MusicModeService.ts` is a phantom file; the real music hook has no interval to guard. Do not touch.
- **`src/protocols/SymphonyEngine.ts`** — verify-only (Step 2); no streaming loop exists. Do not edit.
- **`src/services/ble/HeartbeatService.ts`** — verify-only (Step 1); already guarded. Do not edit.
- **The O(n²) → Map refactor suggested by the audit** — explicitly rejected on evidence (loop is already single-pass via monotonic `lastSampleIdx`). Do not build a `Map<ledIndex, segment>`.
- **Re-running the full Wave 9 audit / regenerating accurate line numbers for the remaining 9M/3L items** — separate `[🕵️ SPIKE]` task. Do not start it here.
- **Any unrelated `as any` / dead-import cleanup outside the two touched files** — Boy Scout applies only within `VisualizerHooks.ts` and `DashboardScreen.tsx`, and only on lines this plan already edits.
