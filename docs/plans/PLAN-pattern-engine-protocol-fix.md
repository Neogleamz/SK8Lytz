# Implementation Plan

## Task: feat/pattern-engine-protocol-fix
## Cluster: TC-04
## Risk: [H-RISK] | Size: [Snack] | Layer: [BLE]
## Status: [⚪ READY]

---

## Problem Statement

`PatternEngine.ts` uses `ZenggeProtocol.setCustomModeExtendedCompact` (10B payload) for two code paths:
1. **Native temporal test patterns** (line 222)
2. **Legacy 0x51 interception patterns** (line 245)

Per `ZENGGE_PROTOCOL_BIBLE.md §11`, the 10B extended format **does nothing on 0xA3 hardware** without a preceding chunked framing header. The correct call is `ZenggeProtocol.setCustomModeCompact` (9B format) and the `dir` property must be removed.

This means any pattern dispatched through these two code paths is silently broken on all 0xA3 controllers today.

---

## Source of Truth

- **`tools/ZENGGE_PROTOCOL_BIBLE.md §11`** — "10B Extended Compact format: requires chunked framing header on 0xA3 chip. Without it, the packet is ignored. Use 9B setCustomModeCompact for direct fire-and-forget."
- **`src/protocols/PatternEngine.ts:222`** — `ZenggeProtocol.setCustomModeExtendedCompact(...)` (native temporal)
- **`src/protocols/PatternEngine.ts:245`** — `ZenggeProtocol.setCustomModeExtendedCompact(...)` (legacy 0x51 interception)
- **`src/protocols/ZenggeProtocol.ts`** — defines both `setCustomModeCompact` (9B) and `setCustomModeExtendedCompact` (10B)

---

## Audit Trail
- `DOMAIN_PATTERN_ENGINE_findings.json` → PE-001 (line 222, HIGH) + PE-002 (line 245, HIGH)
- Confidence: 🔴 CONFIRMED

---

## Proposed Changes

### [MODIFY] `src/protocols/PatternEngine.ts`

**Line 222 (native temporal test patterns):**
```diff
- return ZenggeProtocol.setCustomModeExtendedCompact([{
-   mode: hardwareModeId,
-   speed,
-   color1: fg,
-   color2: bg,
-   dir: hwFlags
- }]);
+ return ZenggeProtocol.setCustomModeCompact([{
+   mode: hardwareModeId,
+   speed,
+   color1: fg,
+   color2: bg,
+ }]);
```

**Line 245 (legacy 0x51 interception patterns):**
```diff
- return ZenggeProtocol.setCustomModeExtendedCompact([{
-   mode: modeId,
-   speed,
-   color1: fg,
-   color2: bg,
-   dir: hwFlags
- }]);
+ return ZenggeProtocol.setCustomModeCompact([{
+   mode: modeId,
+   speed,
+   color1: fg,
+   color2: bg,
+ }]);
```

**Boy Scout**: Remove any `dir` / `hwFlags` local variable that becomes dead code after this change.

---

## Verification Plan

### Automated
- `npm run verify` — TSC must pass (removing `dir` from `setCustomModeCompact` call must be type-clean)

### Manual (Device)
1. Open Docked Controller → switch to a native temporal pattern (e.g., strobe, fade)
2. Confirm pattern actually plays on 0xA3 hardware (LED strips light up with effect)
3. Switch to a legacy 0x51 pattern override
4. Confirm pattern plays on hardware

### Before/After Signal
- Before fix: Pattern commands silently no-op on hardware
- After fix: Hardware responds with correct LED effect

---

## Worktree
`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\pattern-engine-protocol-fix\`
Branch: `feat/pattern-engine-protocol-fix`
