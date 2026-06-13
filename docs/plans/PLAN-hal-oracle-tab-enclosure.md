# Implementation Plan

## Task: fix/hal-oracle-tab-enclosure
**Cluster:** H — HAL Enclosure Violations (Raw Bytes in UI)  
**Batch:** `[BATCH:hal-enclosure-sweep]`  
**Rule Violated:** R-19  
**Severity:** MEDIUM × 11  
**Risk:** L-RISK  
**Size:** Meal (11 raw byte array → protocol method call replacements)

## Cited Truth
- Audit R19: `src/components/admin/tools/tabs/DiagnosticLabOracleTab.tsx` (48KB file, 833 lines) — confirmed raw byte construction at:
  - Line 167: `[0x73, 0x01, 0x26, 0x01, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x80, 0x64]` (Music Config)
  - Line 176: `[0x73, 0x01, 0x27, 0x01, ...]` (Music Config variant)
  - Line 185: `[0x73, 0x01, 0x26, 0x00, ...]` (Music Config off)
  - Line 194: `[0x73, 0x01, 0x00, 0x01, ...]` (Music Config old format)
  - Line 203: `[0x73, 0x00, 0x01, ...]` (Music Config 12-byte old)
  - Lines 246-247, 255, 264: `[0x58, 0xF0]`, `[0x57, ...]`, `[0x56, ...]` (Scene Mgmt)
  - Lines 724, 733, 742: Dynamic slot-parameterized 0x58/0x57/0x56 (Scene Mgmt panel)
- `ZenggeProtocol.ts` is already imported at OracleTab.tsx:8 — encapsulation is available
- Source: `artifacts/system_audit_report.md` Cluster H lines 291–308

## Source of Truth
- Audit: `artifacts/system_audit_report.md` — Cluster H
- Files to read: `src/components/admin/tools/tabs/DiagnosticLabOracleTab.tsx`, `src/protocols/ZenggeProtocol.ts`

## IMPORTANT: This is a Diagnostic Lab — NOT production app code
`DiagnosticLabOracleTab` is a hardware testing tool only accessible to admins. It intentionally tests raw byte variants that don't (yet) have production equivalents in `ZenggeProtocol`. The raw bytes at lines 167–203 are **hypothesis tests** for the 0x73 Music Config opcode, testing different mic byte values and isOn states.

**The correct fix is NOT to add 5 new ZenggeProtocol methods for hypothesis variants** — it is to:
1. For the **3 dynamic Scene Management blocks** (lines 724, 733, 742): Extract to `ZenggeProtocol` methods since these are already in production use
2. For the **5 static 0x73 hypothesis tests** (lines 167–203): Leave as raw bytes with a `// HAL-EXEMPT: Diagnostic hypothesis test` comment and document in ZENGGE_PROTOCOL_BIBLE

## Implementation Steps

### Phase 1 — Read Both Files (P1 mandatory)
- `view_file` `src/protocols/ZenggeProtocol.ts` — identify what methods already exist for 0x56/0x57/0x58
- Confirm lines 724/733/742 in OracleTab construct the same payload already covered by a ZenggeProtocol method

### Phase 2 — Add Protocol Methods (if not already present)
If `ZenggeProtocol.ts` does not have `activateScene(slot, speed, brightness)`, `deleteScene(slot)`, `querySceneState()` — add them.

### Phase 3 — Replace Dynamic Scene Constructions
At lines 724, 733, 742: replace raw `[0x58, 0xF0]`, `[0x57, slot, 0x32, 0x64]`, `[0x56, slot, 0,0,...]` with:
```ts
transmit(ZenggeProtocol.querySceneState(), '0x58 QUERY scene state', '0x58');
transmit(ZenggeProtocol.activateScene(sceneSlot, 50, 100), `0x57 ACTIVATE slot=${sceneSlot}`, '0x57');
transmit(ZenggeProtocol.deleteScene(sceneSlot), `0x56 DELETE slot=${sceneSlot}`, '0x56');
```

### Phase 4 — Document Hypothesis Exemptions
For lines 167–203 (0x73 hypothesis tests), add:
```ts
// HAL-EXEMPT: Diagnostic hypothesis test — byte variant not yet confirmed by hardware.
// Do not move to ZenggeProtocol until hardware result verified and logged in ZENGGE_PROTOCOL_BIBLE.
```

### Phase 5 — Quick Test Block (lines 246–264)
Replace the 3 static raw arrays in `QUICK_TESTS` with method calls matching the dynamic panel, now that ZenggeProtocol methods exist.

## Verification Plan
- `view_file` before every line replacement
- `git diff HEAD` — only the raw array replacements, no JSX or state changes
- TSC passes: new ZenggeProtocol method signatures are typed
- `npm run verify`
- Manual: Open Diagnostic Lab in admin mode → run SCENE MGMT tests → confirm same byte payloads transmitted (check ADB logcat for raw TX bytes)
