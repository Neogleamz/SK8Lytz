# Implementation Plan

## Task: fix/hal-enclosure-oracle-tab
## Cluster: TC-02
## Risk: [H-RISK] | Size: [Meal] | Layer: [BLE]
## Status: [⚪ READY]

---

## Problem Statement

`DiagnosticLabOracleTab.tsx` contains 11 raw byte array literals (e.g., `[0x73, 0x01, 0x26, ...]`) hardcoded inline within a production admin UI component. Per the HAL Enclosure rule (R-19), all raw BLE byte constructs must live exclusively in `src/protocols/`. This is a H-RISK violation because:

1. The oracle tab grows protocol bytes independently of `ZENGGE_PROTOCOL_BIBLE.md`
2. Future protocol changes require hunting the UI file, not the protocol layer
3. If byte values drift from the spec, there's zero single source of truth

---

## Source of Truth

- **Agent Report**: `artifacts/deepdive_raw/R-19_findings.json` → R-19-001 through R-19-011 (lines 165, 174, 183, 192, 201, 244, 253, 262, 722, 731, 740)
- **`tools/ZENGGE_PROTOCOL_BIBLE.md`** — Section on raw opcode definitions; all mentioned byte sequences map to documented opcodes
- **`src/protocols/ZenggeProtocol.ts`** — The correct home for all raw byte factory functions

---

## Proposed Changes

### [MODIFY] `src/protocols/ZenggeProtocol.ts`
Extract each raw byte array into a named static factory function or exported constant. Examples:
- `oracleQueryConfig()` → `[0x73, 0x01, 0x26, ...]`
- `oracleQueryRFMode()` → `[0x73, 0x01, 0x27, ...]`
- `oracleSceneSlot(slot: number)` → `[0x56, slot, ...]`
- `oracleScenePlay(slot: number)` → `[0x57, slot, ...]`
- `oraclePowerProbe()` → `[0x58, 0xF0]`

**Boy Scout**: Verify matching functions don't already exist under different names before adding.

### [MODIFY] `src/components/admin/tools/tabs/DiagnosticLabOracleTab.tsx`
Replace all 11 inline byte array literals with calls to the named ZenggeProtocol functions above:
- Lines 165, 174, 183, 192, 201, 244, 253, 262, 722, 731, 740

---

## Verification Plan

### Automated
- `npm run verify` — TSC clean after import changes

### Manual
- Open Diagnostic Lab Oracle tab on device
- Verify all oracle commands still trigger correct hardware responses
- Confirm no GATT 133 errors introduced (same payloads, just refactored)

---

## Worktree
`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\hal-enclosure-oracle-tab\`
Branch: `fix/hal-enclosure-oracle-tab`
