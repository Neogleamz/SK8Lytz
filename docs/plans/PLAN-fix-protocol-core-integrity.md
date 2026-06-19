# Implementation Plan: fix/protocol-core-integrity

## Goal
Fix critical ZenggeAdapter sequence counter corruption and protocol correctness issues.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Cluster PROTOCOL_CORE

## Findings to Resolve
1. PROTOCOL_CORE-004: ZenggeAdapter.ts L167 — CRITICAL sequence counter corruption
2. PROTOCOL_CORE-001: ZenggeAdapter.ts L260 — Split-brain 0x40 chunking between ZenggeAdapter and BleWriteDispatcher
3. PROTOCOL_CORE-002: useProtocolBuilder.ts L14 — TransitionType mapping (0x00 → CASCADE) needs Protocol Bible verification
4. PROTOCOL_CORE-003: useProtocolBuilder.ts L119 — matrixStyle/bldMic conflation in 0x73 music config
5. PROTOCOL_CORE-005: ZenggeProtocol.ts L653 — Remove hardcoded 54-pixel max in streamPixelFrame

## Files to Create/Modify

### [MODIFY] [ZenggeAdapter.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeAdapter.ts)
- Fix sequence counter corruption at L167
- Resolve split-brain 0x40 chunking at L260

### [MODIFY] [useProtocolBuilder.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useProtocolBuilder.ts)
- Verify and fix TransitionType mapping at L14 against ZENGGE_PROTOCOL_BIBLE.md
- Fix matrixStyle/bldMic conflation at L119

### [MODIFY] [ZenggeProtocol.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeProtocol.ts)
- Remove hardcoded 54-pixel max in streamPixelFrame at L653

## Verification
- `npm run verify`
- Cross-check all byte values against `docs/ZENGGE_PROTOCOL_BIBLE.md`

## Out of Scope
- BleWriteDispatcher.ts (Wave 2)
- SpatialEngine.ts (Wave 5)

// SKIPPED: useProtocolBuilder.ts — Already fixed in previous commit 3d6104f9.
// SKIPPED: ZenggeProtocol.ts — Already fixed in previous commit 3d6104f9.
