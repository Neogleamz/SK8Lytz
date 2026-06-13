# Implementation Plan

## Task: fix/ble-programmer-gatt-delays
**Cluster:** G — Hardcoded Timing Delays (HIGH severity)  
**Batch:** `[BATCH:hardcoded-delay-sweep]`  
**Rule Violated:** R-16  
**Severity:** HIGH × 2, MEDIUM × 8  
**Risk:** M-RISK  
**Size:** Meal (this plan covers the 2 HIGH cases only)

## Cited Truth
- Audit R16 HIGH #1: `src/components/admin/tools/Sk8LytzProgrammer.tsx` — 600ms/400ms `setTimeout` GATT settle delays in batch flash
- Audit R16 HIGH #2: `src/hooks/useCrewSession.ts` — 1500ms auth state settle delay
- Source: `artifacts/system_audit_report.md` Cluster G lines 268–288

## Source of Truth
- Audit: `artifacts/system_audit_report.md` — Cluster G, HIGH findings
- Files to read: `src/components/admin/tools/Sk8LytzProgrammer.tsx`, `src/hooks/useCrewSession.ts`
- Protocol Bible: `tools/ZENGGE_PROTOCOL_BIBLE.md` — GATT timing guidance

## Problem
**Programmer:** On slow Android BLE devices, a 600ms/400ms static GATT settle delay is insufficient, causing flash operations to fail silently. On fast devices, it adds unnecessary latency (users waiting 600ms for no reason).

**CrewSession:** A 1500ms auth settle delay is a fixed guess at how long Supabase auth propagation takes. Under slow networks, it's too short. Under fast networks, it's dead time.

## Implementation Steps

### Fix 1 — Sk8LytzProgrammer.tsx GATT delays

**Step 1:** `view_file` `src/components/admin/tools/Sk8LytzProgrammer.tsx` — find the 600ms/400ms setTimeout patterns (lines approximate: ~350–450)

**Step 2:** Replace each `await new Promise(resolve => setTimeout(resolve, 600))` with a GATT-state-driven wait:
- Option A: Await the `connectToDevice` promise resolution which indicates GATT is settled
- Option B: Use `BleWriteQueue.enqueueWrite()` which internally handles GATT serialization, making the static delay unnecessary
- Option C (safe default): Replace with a configurable `BLE_TIMING.FLASH_SETTLE_MS` constant (in `src/constants/bleTiming.ts`) defaulting to 600, making the delay tunable without a code change

**Decision:** Use Option C (least risk) as a first step — adds tunability. File a follow-up tech debt note to migrate to BleWriteQueue serialization in a future sprint.

### Fix 2 — useCrewSession.ts auth settle delay

**Step 1:** `view_file` `src/hooks/useCrewSession.ts` — find the 1500ms delay

**Step 2:** Replace with a reactive approach:
- Listen to `supabase.auth.onAuthStateChange` callback completion instead of guessing timing
- OR use AuthContext's `sessionLoaded` state as the gate (poll with a `useEffect` watching `sessionLoaded` instead of waiting 1500ms)

**Implementation:** Change the delay to use a `useEffect` that watches `isAuthenticated` from `useAuth()` — fire the crew session setup only after `isAuthenticated` transitions to `true`.

## Verification Plan
- `view_file` confirms exact lines before any edit
- `git diff HEAD` post-edit: only the setTimeout → constant/reactive change, no surrounding logic changed
- Manual: Run batch flash on a slow Android device. Verify flash completes without timing errors.
- `npm run verify`
