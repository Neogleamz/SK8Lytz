# Implementation Plan
# PLAN-BLE-PIXEL-BUFFER-CLAMP

## Summary
Five diagnostic lab components construct raw `0x59` Static Colorful payloads without enforcing the 12-pixel minimum, risking EEPROM buffer lockouts on the 0xA3 hardware chipset. Additionally, the same five components construct raw byte arrays outside `src/protocols/` in violation of the HAL Enclosure rule. Both defects are co-located and can be fixed in a single surgical pass.

**Batch:** `BATCH:ble-gatt-hardening`
**Status:** `[✅ VERIFIED]`

---

## Source of Truth Files
- `src/components/admin/tools/Sk8LytzDiagnosticLab.tsx` — Line 178 (R-10-001, R-19-001)
- `src/components/admin/tools/tabs/DiagnosticLabBuilderTab.tsx` — Line 73 (R-10-002, R-19-002)
- `src/components/admin/tools/tabs/DiagnosticLabColorTab.tsx` — Line 25 (R-10-003, R-19-003)
- `src/components/admin/tools/tabs/DiagnosticLabTransitionTab.tsx` — Line 64 (R-10-004, R-19-005)
- `src/components/admin/tools/tabs/DiagnosticLabOracleTab.tsx` — Line 441 (R-10-005, R-19-004)
- Raw audit: `R-10_findings.json`, `R-19_findings.json`

---

## Findings

### HIGH — 0x59 Pixel Buffer Under-Clamp (R-10) [Hardware Safety Rule]
| ID | File | Line | pts variable |
|----|------|------|-------------|
| R-10-001 | `Sk8LytzDiagnosticLab.tsx` | 178 | `pts` |
| R-10-002 | `DiagnosticLabBuilderTab.tsx` | 73 | `pts` |
| R-10-003 | `DiagnosticLabColorTab.tsx` | 25 | `pts` |
| R-10-004 | `DiagnosticLabTransitionTab.tsx` | 64 | `hwPts` |
| R-10-005 | `DiagnosticLabOracleTab.tsx` | 441 | `hwPts` |

### HIGH — Raw Byte Construction Outside HAL (R-19)
Same five files. The `raw[0] = 0x59` construction must route through `ZenggeProtocol.setMultiColor()` or `adapter.buildMultiColor()`.

> [!CAUTION]
> The 12-pixel minimum is a hardware safety rule (evolved rule in agent-behavior.md §10). Missing it causes physical controller EEPROM buffer lockouts on 0xA3 chipsets. This is not a code quality issue — it is a device damage risk.

---

## Implementation Steps

### Step 1 — Add pixel clamp guard (all 5 files)
For files using `pts`:
```typescript
// BEFORE
const pixels = Array(pts).fill({ r, g, b });
// AFTER
const safePts = Math.max(12, pts);
const pixels = Array(safePts).fill({ r, g, b });
```

For files using `hwPts`:
```typescript
// BEFORE
const numPoints = hwPts;
// AFTER
const numPoints = Math.max(12, hwPts);
```

### Step 2 — Evaluate HAL refactor feasibility
Check if `ZenggeProtocol.setMultiColor()` or `adapter.buildMultiColor()` accepts the same `{r, g, b}[]` pixel array format used in these diagnostic files.

**If YES:** Replace manual `raw[0] = 0x59...` construction with the protocol method call.
**If NO (signature mismatch):** Apply Step 1 clamp only. File a follow-up task to align protocol HAL interface with diagnostic needs. Do NOT force a broken refactor.

> [!NOTE]
> Diagnostic lab files are special-case low-level tools. The HAL violation (R-19) in test/diagnostic context carries HIGH false-positive risk for the test files. Only production diagnostic lab files (not `ble-simulator.test.ts`) require the HAL refactor.

---

## Verification
- `npm run verify` (TSC must pass)
- Manual: Set `pts=5` in DiagnosticLab → confirm payload sends with `numPoints=12`
- Check `raw.length === 12*3+9 = 45` minimum

## Kanban Task Tags
- `[Status: 🔥 ON DECK]`
- `[Verification Status: ✅ VERIFIED]`
- `[Layer: BLE]`
- `[Risk: H-RISK]`
- `[Size: Snack]`
- `[Cognitive Load: Low]`
- `[BATCH: ble-gatt-hardening]`
