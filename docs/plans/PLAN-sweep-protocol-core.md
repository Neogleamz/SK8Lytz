# Implementation Plan

## Task: sweep-protocol-core
**Slug:** sweep-protocol-core  
**Wave:** [WAVE:1] — Parallel-safe with other Wave 1 clusters  
**Size:** [Snack] — 3 files  
**Risk:** [H-RISK] — Core BLE protocol; byte-level bugs  
**Status:** [✅ READY]  
**Source of Truth:** `artifacts/system_audit_report.md` + `artifacts/deepdive_raw/DOMAIN_PROTOCOL_CORE_findings.json` + `docs/ZENGGE_PROTOCOL_BIBLE.md`

## Goal
Fix 5 protocol-level inconsistencies. Critical: resolve the split-brain 0x40 chunking implementation between `ZenggeAdapter` and `BleWriteDispatcher`. Fix the `TransitionType` mapping (0x00 → 'CASCADE' is wrong). Fix the `matrixStyle`/`bldMic` conflation in the 0x73 music config builder. Remove the hardcoded 54-pixel max in `streamPixelFrame`. Fix inconsistent checksum calculations in diagnostic helpers.

## Decision Log
- **Split-brain chunking (CONFIRMED — 2 agents flagged)**: `ZenggeAdapter.prepareForTransmission` chunks differently from `BleWriteDispatcher`. One source of truth must own chunking. Per Protocol Bible: chunking belongs in the write dispatcher. `ZenggeAdapter` must emit unchunked payloads.
- **TransitionType 0x00**: Must cross-reference Protocol Bible §3 for correct transition code mapping before changing.
- **54-pixel hardcode**: Per Protocol Bible, the max frame length is protocol-negotiated, not hardcoded.

## Files to Create/Modify

### [MODIFY] src/protocols/ZenggeAdapter.ts
- Remove duplicate chunking logic from `prepareForTransmission` at L260
- Ensure `ZenggeAdapter` emits flat (unchunked) payloads — chunking responsibility moves fully to `BleWriteDispatcher`
- Cross-reference Protocol Bible §4 chunking spec before editing

### [MODIFY] src/hooks/useProtocolBuilder.ts
- Fix `TRANSITION_TYPES` mapping at L14: verify 0x00 value against `docs/ZENGGE_PROTOCOL_BIBLE.md` and correct
- Fix `matrixStyle`/`bldMic` conflation at L119: ensure they are evaluated independently in the 0x73 music config byte construction

### [MODIFY] src/protocols/ZenggeProtocol.ts
- Fix inconsistent checksum calculations in diagnostic/oracle helper methods at L983
- Remove hardcoded `54` pixel max in `streamPixelFrame` at L609; replace with protocol-negotiated constant or parameter

## Cited Truth Requirements
Before editing any byte values, agent MUST cite the exact Protocol Bible section and line. No payload edits from memory.

## Out of Scope
- No changes to `BleWriteDispatcher.ts` (Wave 2 scope)
- No changes to `SpatialEngine.ts` (different cluster)
- No changes to test files

## Verification Plan
- `npm run verify` — TSC + Jest must pass
- `/ble-lab` protocol verification for 0x40 chunking output
- Manual diff review of ZenggeAdapter output before/after
