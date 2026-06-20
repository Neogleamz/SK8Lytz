# Implementation Plan: C1 — BLE Write Queue Centralization

## Goal
Route all direct `writeCharacteristicWithoutResponseForDevice` calls through `BleWriteQueue.enqueueWrite()`.

## Source Analysis
system_audit_report.md — deep-dive code hunt flagged 7 `writeCharacteristic` call sites across 3 files.

## Rules Addressed
- **R-01**: All BLE writes must pass through the centralized write queue (backpressure, retry, priority).

## Status: ✅ ALL 7 FINDINGS ARE FALSE POSITIVES — NO CODE CHANGES REQUIRED

The audit tool flagged raw `writeCharacteristicWithoutResponseForDevice` calls, but every single one already lives **inside** an `enqueueWrite()` closure. The `writeCharacteristic` call is the *execute callback* that the queue invokes — not a bypass of it.

### Evidence (file → line → wrapper)

| # | File | Write Line | enqueueWrite Wrapper Line | Priority |
|---|------|-----------|--------------------------|----------|
| 1 | `HeartbeatService.ts` | L46 | L45 `enqueueWrite('normal', async () => {` | normal |
| 2 | `InterrogatorService.ts` | L119 | L118 `enqueueWrite('normal', async () => {` | normal |
| 3 | `InterrogatorService.ts` | L132 | L131 `enqueueWrite('normal', async () => {` | normal |
| 4 | `BlePingService.ts` | L57 | L56 `enqueueWrite('critical', async () => {` | critical |
| 5 | `BlePingService.ts` | L123 | L122 `enqueueWrite('critical', async () => {` | critical |
| 6 | `BlePingService.ts` | L136 | L135 `enqueueWrite('critical', async () => {` | critical |
| 7 | `BlePingService.ts` | L157 | L156 `enqueueWrite('critical', async () => {` | critical |

### Root Cause of False Positive
The audit grep matched `writeCharacteristicWithoutResponseForDevice` without inspecting the enclosing scope. Every hit is the inner body of an `enqueueWrite()` callback — which is exactly the correct pattern. The queue owns the actual GATT call; the caller owns the queue entry.

## Files to Create/Modify
None. Zero code changes.

## Implementation Steps
1. **Verify each finding** — Read the enclosing 3-line context of every flagged line.
   - Verify: Confirm `enqueueWrite(` appears 1–2 lines above each `writeCharacteristic` call.
2. **Close the cluster** — Mark R-01 as fully compliant in the audit tracker.
   - Verify: `npm run verify` passes (no regression from zero changes).

## Out of Scope
- `BleWriteQueue.ts` — the queue itself; not modified
- `BleMachine.ts` — state machine; not modified
- `ConnectService.ts` — connection logic; not modified

## Verification
- `npm run verify` (TSC + Jest) — confirms no regression
- `git diff` — expected empty (no changes)
- Manual: re-run the grep that produced the audit findings and annotate each as "inner closure — compliant"
