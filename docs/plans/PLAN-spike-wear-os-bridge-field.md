# Implementation Plan
# spike/wear-os-bridge-field — Wear OS Bridge Distance Field Verification

**Wave:** 0 (prerequisite for all other waves)
**Type:** Spike — read-only, no code changes

## Objective
Confirm the exact field name used in the WatchBridge message payload for session distance, so `SessionCommitService.ts` and the Wear OS distance bug fix use the correct key.

## Steps

### Step 1 — Read Wear OS Kotlin bridge files
- Read `android/sk8lytzWear/` — find the data class or map that receives WatchBridge messages
- Source: `android/sk8lytzWear/` directory
- Verify: Locate the field name used for distance (e.g. `distance`, `distanceMiles`, `sessionDistance`)

### Step 2 — Read Swift watch bridge files
- Read `targets/watch/` — find the WatchConnectivitySession handler
- Source: `targets/watch/` directory
- Verify: Confirm same field name used on iOS watch side

### Step 3 — Read `sk8lytz-watch-bridge` package source
- Read `node_modules/sk8lytz-watch-bridge/` or `packages/sk8lytz-watch-bridge/` for the payload type definition
- Source: WatchBridge package
- Verify: Typed interface confirms `distance: number` field name

### Step 4 — Document finding
- Write finding to `tools/SESSION_LOG.md` as `[DECISION]` entry
- Confirm field name for `PLAN-session-services-layer.md` Step 6 (SessionCommitService input)

## Out of Scope
- Any code changes
- WatchBridge protocol modifications
