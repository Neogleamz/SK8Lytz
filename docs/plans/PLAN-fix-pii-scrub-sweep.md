# Implementation Plan: fix/pii-scrub-sweep

## Goal
Wrap all unscrubbed PII (`deviceId`, `name`, `crewName`) in `AppLogger` calls with `scrubPII()` utility. 16 instances across 8 files. Rule R-09 compliance.

## Source Analysis Link
- [System Audit Report — CLUSTER 7](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) (lines 170–176)
- Utility: `scrubPII` exported from `src/utils/piiScrubber.ts:5`

## Files to Create/Modify

| # | File | Instances | Import Needed | Notes |
|---|------|-----------|---------------|-------|
| 1 | `src/services/ble/RecoveryService.ts` | 6 | ❌ (already L6) | Lines 115, 157, 167, 177, 221, 224 — raw `deviceId` in object literals |
| 2 | `src/components/crew/CrewLandingScreen.tsx` | 2 | ✅ | Lines 116, 137 — raw `crew.name` as `crewName` |
| 3 | `src/components/DockedController.tsx` | 2 | ✅ | Lines 498, 499 — raw `deviceId` in log + catch |
| 4 | `src/services/ble/ConnectService.ts` | 1 | ❌ (already L9) | Line 277 — raw `conn.id` and `conn.name` |
| 5 | `src/components/crew/CrewDetailScreen.tsx` | 1 | ✅ | Line 121 — raw `editCrewName.trim()` as `crewName` |
| 6 | `src/components/crew/CrewManageScreen.tsx` | 1 | ✅ | Line 66 — raw `crew.name` as `crewName` |
| 7 | `src/components/admin/tools/Sk8LytzProgrammer.tsx` | 1 | ✅ | Line 221 — raw `id` as `deviceId` |

> [!NOTE]
> **Dropped from scope:** `useControllerDispatch.ts:95` and `useControllerAnalytics.ts:78`. Line 95 is `__DEV__`-gated (out of scope per task definition). Line 78 is `name` referring to a pattern label string ("Pattern 5"), not PII — false positive. Net count: **14 fixes across 7 files**.

## Steps

### Step 1 — RecoveryService.ts (6 fixes)
Wrap raw `deviceId` with `scrubPII(deviceId)` at 6 locations. Import already exists at line 6.
- **L115**: `{ deviceId, error: ... }` → `{ deviceId: scrubPII(deviceId), error: ... }`
  - Source: `RecoveryService.ts:115`
- **L157**: `{ deviceId, attempts }` → `{ deviceId: scrubPII(deviceId), attempts }`
  - Source: `RecoveryService.ts:157`
- **L167**: `{ phase: 3, deviceId, event: ... }` → `{ phase: 3, deviceId: scrubPII(deviceId), event: ... }`
  - Source: `RecoveryService.ts:167`
- **L177**: Same pattern as L167
  - Source: `RecoveryService.ts:177`
- **L221**: `{ deviceId, phase: 3 }` → `{ deviceId: scrubPII(deviceId), phase: 3 }`
  - Source: `RecoveryService.ts:221`
- **L224**: `{ deviceId, error: ... }` → `{ deviceId: scrubPII(deviceId), error: ... }`
  - Source: `RecoveryService.ts:224`
- **Verify:** `grep -n "deviceId," src/services/ble/RecoveryService.ts` — every `deviceId` in an AppLogger call must be wrapped in `scrubPII()`.

### Step 2 — CrewLandingScreen.tsx (2 fixes + import)
Add `import { scrubPII } from '../../utils/piiScrubber';` after AppLogger import (L11).
- **L116**: `crewName: crew.name` → `crewName: scrubPII(crew.name)`
  - Source: `CrewLandingScreen.tsx:116`
- **L137**: `crewName: crew.name` → `crewName: scrubPII(crew.name)`
  - Source: `CrewLandingScreen.tsx:137`
- **Verify:** `grep -n "crew.name" src/components/crew/CrewLandingScreen.tsx` — AppLogger calls must show `scrubPII(crew.name)`.

### Step 3 — DockedController.tsx (2 fixes + import)
Add `import { scrubPII } from '../utils/piiScrubber';` in the import block.
- **L498**: `{ deviceId, payloadLen: ... }` → `{ deviceId: scrubPII(deviceId), payloadLen: ... }`
  - Source: `DockedController.tsx:498`
- **L499**: `deviceId,` in the `.catch()` → `deviceId: scrubPII(deviceId),`
  - Source: `DockedController.tsx:499`
- **Verify:** `grep -n "deviceId" src/components/DockedController.tsx` — all AppLogger calls (non-`__DEV__`) must be scrubbed.

### Step 4 — ConnectService.ts (1 fix)
Import already exists at L9. Fix unscrubbed final DEVICE_CONNECTED log.
- **L277**: `{ id: conn.id, name: conn.name }` → `{ id: scrubPII(conn.id), name: scrubPII(conn.name ?? '') }`
  - Source: `ConnectService.ts:277`
- **Verify:** `grep -n "conn.name" src/services/ble/ConnectService.ts` — must be wrapped.

### Step 5 — CrewDetailScreen.tsx (1 fix + import)
Add `import { scrubPII } from '../../utils/piiScrubber';` after AppLogger import (L7).
- **L121**: `crewName: editCrewName.trim()` → `crewName: scrubPII(editCrewName.trim())`
  - Source: `CrewDetailScreen.tsx:121`
- **Verify:** `grep -n "crewName" src/components/crew/CrewDetailScreen.tsx` — AppLogger calls must show `scrubPII(...)`.

### Step 6 — CrewManageScreen.tsx (1 fix + import)
Add `import { scrubPII } from '../../utils/piiScrubber';` after AppLogger import (L6).
- **L66**: `crewName: crew.name` → `crewName: scrubPII(crew.name)`
  - Source: `CrewManageScreen.tsx:66`
- **Verify:** `grep -n "crewName" src/components/crew/CrewManageScreen.tsx` — AppLogger calls must show `scrubPII(...)`.

### Step 7 — Sk8LytzProgrammer.tsx (1 fix + import)
Add `import { scrubPII } from '../../../utils/piiScrubber';` after AppLogger import (L22).
- **L221**: `deviceId: id` → `deviceId: scrubPII(id)`
  - Source: `Sk8LytzProgrammer.tsx:221`
- **Verify:** `grep -n "deviceId:" src/components/admin/tools/Sk8LytzProgrammer.tsx` — AppLogger calls must show `scrubPII(...)`.

### Step 8 — Verification
- Run `npm run verify` (TSC + Jest). Zero new errors.
- Run `grep -rn "AppLogger\.\(log\|warn\|error\)" src/ | grep -E "(deviceId|conn\.id|conn\.name|crew\.name|crewName)" | grep -v "scrubPII" | grep -v "__DEV__"` — expect zero results from the 7 target files.

## Verification Plan

| Gate | Command | Pass Criteria |
|------|---------|---------------|
| TSC | `npm run verify` | 0 errors |
| Grep | See Step 8 grep | 0 unscrubbed PII in target files |
| Post-diff | `git diff HEAD` after each file | Only planned lines changed |

## Out of Scope
- `scrubPII` implementation changes (already exists at `src/utils/piiScrubber.ts`)
- Test files
- `__DEV__`-gated `console.log` / `AppLogger` calls (e.g., `useControllerDispatch.ts:95`)
- `useControllerAnalytics.ts:78` — `name` field is a pattern label, not PII (false positive)
- Master Reference docs update (no new hooks/services/components created)
