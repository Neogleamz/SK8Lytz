# Implementation Plan
## fix/hw-settings-segments-haloz — Fix Hardcoded `segments: 1` in Classic 0x63 Response Parser

**Severity:** CRITICAL  
**Source:** Defect Audit 2026-06-24 — F-001  
**Auditor note:** User has directed a full audit of the hwSettings pipeline before code changes.
Phase 1 of this plan IS the audit. Phase 2 is the surgical fix.

---

## Context

`parseHardwareSettingsResponse` in `hardwareSettingsHandler.ts` has two branches for parsing
the 0x63 EEPROM query response:

1. **JSON-inner format** (newer firmware) — correctly reads `segments` from `payload[5]` after JSON unwrap.
   This is confirmed as the format HALOZ (productId=163) devices use (Protocol Bible §confirmed 2026-04-25).
2. **Classic 12-byte binary format** (older firmware) — returns `segments: 1` hardcoded (line 124).

The code's own comment for the classic binary format shows `[5]=?` — the segment byte position is
**unverified** by Protocol Bible or hardware capture for the classic binary path.

Downstream pipeline (audit-confirmed 2026-06-24):

```
BLE 0x63 notification
  → useHardwareNotifications.onDataReceived
  → BlePayloadParser.parseLedPayload (BlePayloadParser.ts:46)
  → ZenggeAdapter.parseSettingsResponse (ZenggeAdapter.ts:102-103)
  → ZenggeProtocol.parseHardwareSettingsResponse (ZenggeProtocol.ts:167-168)
  → hardwareSettingsHandler.parseHardwareSettingsResponse (the buggy function)
  → segments=1 propagates to DeviceRepository.updateConfig
  → DeviceStorage.saveDevices (MMKV/AsyncStorage)
  → useDashboardGroups.deviceConfigs (canSyncSegments:true if segments changed)
  → VisualizerUnit.tsx:98 — deviceSegments = hwSettings?.segments (1)
```

`VisualizerUnit.tsx:98`: `const deviceSegments = hwSettings?.segments || device?.segments || productProfile.defaultSegments`
— since `1` is truthy, the device-catalog fallback `productProfile.defaultSegments` (2 for HALOZ) never fires.

**Who is affected:** Devices running older firmware that send the classic binary 0x63 response.
HALOZ with current firmware sends JSON-inner format → already correct → NOT currently broken.

**User directive:** "nothing should be hardcoded anywhere — always use stored hwsettings from
identification and registered device's current hardware settings."

**⚠️ PLAN AMENDMENT (audit-corrected 2026-06-24):**

- The original Phase 2 fix used `payload[5]` for segments in the classic binary format.
  `payload[5]` is MARKED `?` in the code's comment — its meaning is UNVERIFIED.
- The original fallback `HW_CONSTRAINTS.defaultSegments` = 10 is WRONG for most real devices
  (HALOZ needs 2; `defaultSegments=10` is a strip controller default).
- Phase 1 MUST verify the classic binary byte layout before Phase 2 edits any code.

---

## Files to Create / Modify

### Phase 1 — Audit (READ-ONLY before any edit)

These files must be read and their segment/ledPoints usage catalogued before touching anything:

| File | What to audit |
|------|---------------|
| `src/protocols/handlers/hardwareSettingsHandler.ts` | All return statements — check segments, ledPoints for hardcodes |
| `src/services/deviceRepository/DeviceRepositoryService.ts` | How hwSettings are stored/merged on EEPROM response |
| `src/services/deviceRepository/DeviceStorage.ts` | AsyncStorage read/write — does segments survive serialization? |
| `src/hooks/useDashboardGroups.ts` | How hwSettings flows into DockedController / VisualizerUnit |
| `src/components/VisualizerUnit.tsx` | The `deviceSegments` fallback chain — all paths |
| `src/hooks/useControllerDispatch.ts` | `numLEDs` resolution — `hwSettings?.ledPoints || points` |
| `src/protocols/PatternEngine.ts` | `numLEDs` / `hardwareLedPoints` usage |
| `src/screens/Onboarding/HardwareSetupWizardScreen.tsx` | Any hardcoded segment assumptions during registration |

### Phase 2 — Surgical Fix

| File | Change |
|------|--------|
| `src/protocols/handlers/hardwareSettingsHandler.ts` | Fix line 124: replace `segments: 1` with `segments: (payload[5] > 0) ? payload[5] : HW_CONSTRAINTS.defaultSegments` |

If Phase 1 audit reveals additional hardcoded assumptions, each must be logged as a
`// TODO AUDIT-F001: <description>` comment and added to a follow-up task. Do NOT fix
out-of-scope files in this worktree.

---

## Step-by-Step Execution

### Phase 1 — Pre-Execution Audit (MANDATORY — do not skip)

**1.1 — Read `hardwareSettingsHandler.ts` in full (all lines)**
- Verify: All three return statements identified (JSON-inner, JSON fallback, classic binary)
- Check: Does `HW_CONSTRAINTS.defaultSegments` exist? (It must be used in the fix)
- Check: Is there any other hardcoded `segments: 1` anywhere in this file?
- Cite line numbers for all findings before proceeding.

**1.2 — Read `DeviceRepositoryService.ts` — trace EEPROM response write path**
- Find: Where is the result of `parseHardwareSettingsResponse` stored?
- Check: Is `segments` preserved in the serialized device record? (AsyncStorage schema)
- Check: Is there a merge strategy (e.g., "local wins" / "EEPROM wins") and does it preserve segments?
- Cite: The exact function and line where hwSettings is persisted.

**1.3 — Read `DeviceStorage.ts` — verify segments survives AsyncStorage round-trip**
- Check: Is `IHardwareSettings` serialized in full, or is `segments` dropped?
- Cite: The read and write function signatures.

**1.4 — Read `useDashboardGroups.ts` — trace hwSettings delivery to DockedController**
- Find: How `hwSettings` is populated from the device repository and passed to children
- Check: Any transformation that might zero out or default segments
- Cite: The hook's return type and how `hwSettings` flows to `DockedController`

**1.5 — Read `VisualizerUnit.tsx` line ~40-60 — full deviceSegments chain**
- Verify: Exact fallback chain: `hwSettings?.segments || device?.segments || productProfile.defaultSegments`
- Check: Are there other segment-dependent calculations (ring gap pixel positions)?
- Document: All consequences of segments=1 vs segments=2 in the renderer

**1.6 — Read `useControllerDispatch.ts` line ~45-80 — numLEDs resolution**
- Check: `const numLEDs = Math.max(1, (hwSettings?.ledPoints as number | undefined) || points || 16);`
- Note: Any fallback that ignores hwSettings

**1.7 — Read `HardwareSetupWizardScreen.tsx` — registration path**
- Check: Are `segments` and `ledPoints` ever hardcoded during the wizard registration flow?
- Check: Does the wizard EEPROM probe complete and store segments before registration commits?

**1.8 — MANDATORY: Verify classic binary 0x63 response segment byte position**

The code comment for the classic format marks `payload[5]` as `?` (unknown).
Before Phase 2 can assign any byte to `segments`, this must be resolved:

- Search `docs/ZENGGE_PROTOCOL_BIBLE.md` for the full 0x63 response byte layout
- Search `docs/SESSION_LOG.md` and `docs/analysis/` for any prior hardware captures
- If no documented source: compare the 0x62 write format byte layout against any
  available EEPROM read-back captures to infer the response structure
- If STILL unresolved → **HALT Phase 2. File a SPIKE task** to obtain a hardware capture
  of the classic 0x63 binary response from an affected controller.

Cite the specific Protocol Bible or capture source before proceeding to Phase 2.

**Output of Phase 1:** A written list of every hardcoded assumption found. Each must be either:

- Fixed in Phase 2 if it touches `hardwareSettingsHandler.ts`, OR
- Filed as `// TODO AUDIT-F001:` if it's in a different file (follow-up task)

### Phase 2 — Surgical Fix

> ⛔ **BLOCKED until Phase 1.8 is resolved.** Do not edit line 124 until the segment byte
> position in the classic binary format is verified from a documented source.

**2.1 — Confirm `HW_CONSTRAINTS` import is available at fix site**

```
Read: src/protocols/handlers/hardwareSettingsHandler.ts, lines 1-3
```

Confirm `HW_CONSTRAINTS` is already imported from `ZenggeProtocol`.

**2.2 — Read the exact target lines (look-before-leap)**

```
Read: src/protocols/handlers/hardwareSettingsHandler.ts, lines 111–131
```

Confirm line 124 is `segments: 1,` in the classic binary return block.

**2.3 — Apply surgical edit: hardwareSettingsHandler.ts line 124**

The correct fix depends on the byte position verified in Phase 1.8:

**Option A** — If `payload[N]` verified as segments in classic binary format:
```typescript
segments: (payload[N] > 0 && payload[N] <= 2048) ? payload[N] : 1,
```
Use `1` as the fallback (not `HW_CONSTRAINTS.defaultSegments=10`). Devices with unknown
segment count should default to 1 (single segment, safest behavior) — do NOT use 10.

**Option B** — If classic binary format does NOT carry a segment field:
Leave `segments: 1,` as-is (it's the correct behavior for single-segment classic devices).
Update the comment to document WHY it is 1 for the classic binary path.

**⛔ DO NOT use `HW_CONSTRAINTS.defaultSegments` (= 10) as the fallback.** That constant
is sized for strip controllers and is incorrect for ring devices like HALOZ.

**2.4 — Update the comment on the classic binary branch**

The comment above the classic binary block (`[5]=?`) must be updated to reflect
the verified byte layout from Phase 1.8, whatever it is.

**2.5 — Post-edit diff**

```
git diff HEAD src/protocols/handlers/hardwareSettingsHandler.ts
```

Verify: Only line 124 (and the comment) changed. No other lines modified.

### Phase 3 — Verify

**3.1 — Run the full verification suite**
```
npm run verify
```
Expected: TSC ✅ Jest ✅ AST ✅ TypeSafety ✅ WorkflowValidator ✅

**3.2 — Jest specific test check**
```
npx jest --testPathPattern="hardwareSettings|ZenggeProtocol|parseHardware" --no-coverage 2>&1
```
If no test exists for the classic binary 0x63 path → file a TODO for Blake.

**3.3 — Manual validation path (dev build only)**
On a HALOZ device, trigger an EEPROM probe (0x63 query via InterrogatorService).
Observe: VisualizerUnit must show 2-segment ring gap after probe completes.
Note: This is a device-required test; log results in SESSION_LOG if hardware is available.

---

## Out of Scope

- Modifying `VisualizerUnit.tsx` — the fallback chain is correct; the bug is upstream in the parser
- Modifying `DeviceRepositoryService.ts` (unless Phase 1 audit reveals it drops `segments`)
- Adding `defaultSegments` constant if it doesn't exist (file separate task, use literal `2` as interim)
- Fixing hardcoded segment assumptions in screens other than `hardwareSettingsHandler.ts`
  (these become `// TODO AUDIT-F001:` comments)
- Adding new EEPROM probe tests (file for Blake)
- Changing the JSON-inner format branch (already correct)
- Any BanlanX adapter changes (different protocol stack)
