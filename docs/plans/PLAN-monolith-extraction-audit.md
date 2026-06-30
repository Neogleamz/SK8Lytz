# Implementation Plan

## PLAN-monolith-extraction-audit — Monolithic File Audit
*Source: `/deepdive-code-hunt` fleet | Rules: R-23 | Date: 2026-06-10*

### Problem
14 files are flagged as likely exceeding the 30KB safe-edit threshold. These files are collision zones — multiple unrelated features packed into single files. Editing them risks destroying unrelated functionality. This is an **AUDIT CLUSTER** — all sizes must be verified before any surgery begins.

### ⛔ Component Extraction Escape Hatch (Mandatory)
Per `agent-behavior.md` Rule 2: "If a monolithic file is too complex (>30KB and dozens of hooks), you must pause and tell the user: 'This file is too large to safely edit. We must extract this component/logic first before modifying it.'"

**This plan's Phase 1 is audit-only. No edits until sizes are confirmed and extraction strategy is approved.**

### Source of Truth
- `artifacts/system_audit_report.md` — CLUSTER-06

### Suspected Monoliths (Multi-Rule Violations = High Likelihood)

| File | Rules Violated | Risk Level |
|---|---|---|
| `src/screens/DashboardScreen.tsx` | R-07, R-08, R-16, R-18, R-23, R-27 (6 rules!) | 🔴 CRITICAL |
| `src/components/DockedController.tsx` | R-06, R-18, R-23, R-27 (4 rules) | 🔴 HIGH |
| `src/services/DeviceRepository.ts` | R-09, R-23, R-24 | 🟠 HIGH |
| `src/protocols/ZenggeProtocol.ts` | R-06, R-23 | 🟠 HIGH |
| `src/services/AppLogger.ts` | R-11, R-23 | 🟡 MEDIUM |
| `src/screens/Onboarding/HardwareSetupWizardScreen.tsx` | R-11, R-18, R-23 | 🟠 HIGH |
| `src/types/supabase.ts` | R-23 (generated file) | 🟢 LOW (don't edit) |
| `src/components/crew/CrewLandingScreen.tsx` | R-23 | 🟡 MEDIUM |
| `src/components/crew/CrewDetailScreen.tsx` | R-23 | 🟡 MEDIUM |
| `src/services/CrewService.ts` | R-23 | 🟡 MEDIUM |
| `src/protocols/SpatialEngine.ts` | R-23 | 🟡 MEDIUM |
| `src/components/docked/UniversalSlidersFooter.tsx` | R-23 | 🟡 MEDIUM |
| `src/components/admin/tools/tabs/DiagnosticLabBuilderTab.tsx` | R-23 | 🟡 MEDIUM |
| `src/components/admin/tools/tabs/DiagnosticLabOracleTab.tsx` | R-23 | 🟡 MEDIUM |

### Phase 1 — Size Audit (Run First)
```powershell
Get-ChildItem src/screens/DashboardScreen.tsx, src/components/DockedController.tsx, src/services/DeviceRepository.ts, src/protocols/ZenggeProtocol.ts, src/services/AppLogger.ts | Select-Object Name, @{N="KB";E={[math]::Round($_.Length/1KB,1)}}
```

### Phase 2 — Extraction Strategy (Per File)
For each confirmed monolith (>30KB):
1. Identify the natural "seams" — groups of related hooks/state that belong together
2. Extract into sub-components or domain hooks in a dedicated directory
3. Replace extraction site with the new import
4. Run `npm run verify` after each extraction

### Verify
- Post-audit: all files under 30KB OR have extraction PRs filed
- `npm run verify` after each extraction

---

## Audit Results — 2026-06-10

*Auditor: 🔬 QA — Blake | Method: PowerShell file measurement + `view_file` deep-read of confirmed monoliths*

### Size Table — All 14 Files

| File | KB | Lines | Status | Hook Count (useState / useEffect / useCallback / useMemo) |
|---|---|---|---|---|
| `src/screens/DashboardScreen.tsx` | 50.1 | 1,215 | 🔴 MONOLITH | 11 / 19 / 20 / 6 |
| `src/components/DockedController.tsx` | 65.6 | 1,448 | 🔴 MONOLITH | 11 / 14 / 14 / 11 |
| `src/services/DeviceRepository.ts` | 38.4 | 844 | 🔴 MONOLITH | 0 / 0 / 0 / 0 (singleton class) |
| `src/protocols/ZenggeProtocol.ts` | 46.7 | 933 | 🔴 MONOLITH | 0 / 0 / 0 / 0 (static class) |
| `src/services/AppLogger.ts` | 32.4 | 727 | 🔴 MONOLITH | 0 / 0 / 0 / 0 (singleton class) |
| `src/screens/Onboarding/HardwareSetupWizardScreen.tsx` | 39.7 | 755 | 🔴 MONOLITH | 10 / 4 / 0 / 0 |
| `src/types/supabase.ts` | 147.1 | 4,565 | 🔴 MONOLITH | 0 (auto-generated — DO NOT EDIT) |
| `src/components/crew/CrewLandingScreen.tsx` | 35.8 | 639 | 🔴 MONOLITH | 5 / 2 / 0 / 0 |
| `src/components/crew/CrewDetailScreen.tsx` | 31.4 | 476 | 🔴 MONOLITH | 0 / 0 / 0 / 0 (likely class-like patterns) |
| `src/services/CrewService.ts` | 29.9 | 689 | ✅ OK (just under 30KB threshold) | 0 (service class) |
| `src/protocols/SpatialEngine.ts` | 59.1 | 1,260 | 🔴 MONOLITH | 0 (pure math engine) |
| `src/components/docked/UniversalSlidersFooter.tsx` | 32.9 | 561 | 🔴 MONOLITH | 0 / 0 / 0 / 0 (memo-heavy) |
| `src/components/admin/tools/tabs/DiagnosticLabBuilderTab.tsx` | 41.3 | 587 | 🔴 MONOLITH | 4 / 2 / 0 / 0 |
| `src/components/admin/tools/tabs/DiagnosticLabOracleTab.tsx` | 47.1 | 833 | 🔴 MONOLITH | 20 / 4 / 0 / 0 |

**Summary: 13 of 14 confirmed MONOLITH. 1 OK (CrewService.ts at 29.9KB).**

---

### Deep-Read Analysis — Confirmed Monoliths

#### `src/screens/DashboardScreen.tsx` — 50.1KB / 1,215 lines
**Hook inventory:** 11 useState, 19 useEffect, 20 useCallback, 6 useMemo = **56 hook calls total**
**Logical seams:**
- **BLE lifecycle** (scan, connect, disconnect, sweeper, ghosted devices, rssiMap) → STAYS HERE per existing docblock mandate
- **Profile/modal flags** → already extracted to `useDashboardProfile` ✅
- **Fleet groups/device configs/power states** → already extracted to `useDashboardGroups` ✅
- **Crew session state** → already extracted to `useDashboardCrew` ✅
- **Auto-connect/cloud sync** → already extracted to `useDashboardAutoConnect` ✅
- **Deep-link handling** (useEffect lines 308-327) → INLINE, could extract to `useDeepLinkHandler`
- **AppState/sweeper lifecycle** (useEffects lines 354-399) → INLINE, tightly coupled to BLE
- **Back-press & edge pan responder** (lines 683-727) → INLINE, UI navigation
- **Telemetry session auto-start** (useEffect line 488) → INLINE
- **Controller integration** → already extracted to `useDashboardController` ✅
- **Inline JSX sections**: `SETUP_WIZARD`, `DASHBOARD` view, modals

**Assessment — PHASE 1 EXTRACTION ALREADY IN PROGRESS.** The file has already been refactored with domain hooks in Phase 1. Remaining inline logic (deep link handler, back-press, sweeper lifecycle) is BLE-coupling glue. The file is large but most hooks are delegation calls, not inline logic.

**Safe to edit in place for Wave 2-6 surgical 3-10 line edits?** ⚠️ **CONDITIONAL YES** — with strict constraints:
- DO NOT touch any `useEffect` that manages sweeper, AppState, or BLE lifecycle callbacks
- DO NOT modify hook call order (hook count = 56, any removal/reorder causes render crash)
- Target only `useState` initializations, `useCallback` return bodies, or JSX leaf nodes
- Post-edit diff MUST be reviewed — this file has the highest collateral damage risk

**Extraction effort (if needed):** Feast — but extraction is lower priority than surgical fixes given Phase 1 is already partially complete.

---

#### `src/components/DockedController.tsx` — 65.6KB / 1,448 lines ⚠️ WORST OFFENDER
**Hook inventory:** 11 useState, 14 useEffect, 14 useCallback, 11 useMemo = **50 hook calls total**
**Logical seams:**
- **BLE write bus + optimistic write** (`useOptimisticBLE`, `writeToDevice` wrapper) → routing shell, STAYS HERE
- **Controller state** (activeMode, selectedColor, brightness, speed, fixedSubMode, builder nodes, music params, camera, etc.) → already extracted to `useDockedControllerState` ✅
- **Street mode** → already extracted to `useStreetMode` ✅
- **Favorites** → already extracted to `useSharedFavorites` ✅
- **Analytics** → already extracted to `useControllerAnalytics` ✅
- **Microphone** → already extracted to `useAppMicrophone` ✅
- **Curated picks** → already extracted to `useCuratedPicks` ✅
- **Remaining inline state**: `cameraSubMode`, `cameraVibePalette`, `cameraSwatches`, `isBuildingCustom`, `isDiyBuilderExpanded`, `hiddenModes`
- **Remaining inline effects**: Permission gating (3 effects), music mode fire (2 effects), MULTIMODE entry (1 effect), ledger replay (2 effects), crew broadcast (1 effect), lockedProduct sync (1 effect)
- **`FixedPatternPreviewRow`** — top-level component defined inline at lines 79-121

**Assessment:** Like DashboardScreen, this file has been partially refactored. The remaining hooks are legitimate routing-shell concerns (permission gating, BLE replay, crew broadcast). However at 65.6KB it is the single most dangerous file to edit.

**Safe to edit in place for Wave 2-6 surgical 3-10 line edits?** ⚠️ **CONDITIONAL YES** — same constraints as Dashboard + additional rule:
- The `FixedPatternPreviewRow` inline component at lines 79-121 MUST NOT be touched unless extracted first
- Any edit near the hook declaration zone (lines 205-400) is EXTREME RISK — 50 hooks in dependency chain
- `loadFavorite` useCallback (lines 572-693) is 120+ lines — do NOT enter this function without the Extraction Escape Hatch

**Extraction effort (if needed):** Feast — but again, already partially done.

---

#### `src/services/DeviceRepository.ts` — 38.4KB / 844 lines
**Hook inventory:** 0 React hooks (pure singleton class)
**Logical seams:**
- Device CRUD (saveDevice, saveAllDevices, deleteDevice) → primary domain
- Config mutations (updateConfig, setConfigs, deleteConfig) → secondary domain, could split to `ConfigRepository`
- Cloud sync (syncFromCloud, flushPendingSync) → cloud layer
- Tombstone management → anti-resurrection layer
- Subscriber/notify pattern → infrastructure

**Safe to edit in place?** ✅ **YES** — No React hooks, no render cycle risk. Singleton class. Surgical edits to individual methods are safe. The file is long but each method is independent.
**Extraction effort (if needed):** Meal — split into `DeviceRepository` (CRUD) + `ConfigRepository` + `SyncQueue`.

---

#### `src/protocols/ZenggeProtocol.ts` — 46.7KB / 933 lines
**Hook inventory:** 0 React hooks (static class, pure functions)
**Logical seams:**
- BLE packet builders (setCustomMode, setMultiColor, setPower, etc.) → primary domain
- Oracle/diagnostic methods (oracleMusicMic26, oracleSceneQuery, etc.) → diagnostic layer
- Checksum & wrap utilities → low-level layer

**Safe to edit in place?** ✅ **YES** — Pure static class. No React coupling. Individual static methods are fully independent. Surgical edits to protocol methods are safe.
**Extraction effort (if needed):** Meal — split into `ZenggeProtocol` (core) + `ZenggeDiagnosticProtocol` (oracle methods).

---

#### `src/services/AppLogger.ts` — 32.4KB / 727 lines
**Hook inventory:** 0 React hooks (singleton class)
**Safe to edit in place?** ✅ **YES** — Logger singleton. No React coupling. All methods are independent log dispatchers.
**Extraction effort (if needed):** Snack — low priority, the logger is already well-structured.

---

#### `src/screens/Onboarding/HardwareSetupWizardScreen.tsx` — 39.7KB / 755 lines
**Hook inventory:** 10 useState, 4 useEffect, 0 useCallback, 0 useMemo = **14 hook calls total**
**Safe to edit in place?** ⚠️ **CONDITIONAL YES** — 14 hooks is well below the danger threshold. Wizard screen but not a rendering hot path. Surgical edits to individual wizard step handlers are safe.
**Extraction effort (if needed):** Meal.

---

#### `src/types/supabase.ts` — 147.1KB / 4,565 lines
**⛔ DO NOT EDIT MANUALLY** — auto-generated by Supabase CLI via `/db-sync` workflow. Any manual edits will be overwritten on next sync. This is correctly categorized as 🟢 LOW (generated file).
**Safe to edit in place?** ❌ **NO** — Use `/db-sync` workflow exclusively.

---

#### `src/components/crew/CrewLandingScreen.tsx` — 35.8KB / 639 lines
**Hook inventory:** 5 useState, 2 useEffect, 0 useCallback, 0 useMemo = **7 hook calls total**
**Safe to edit in place?** ✅ **YES** — Low hook count. Crew UI screen. Surgical edits are safe.
**Extraction effort (if needed):** Meal.

---

#### `src/components/crew/CrewDetailScreen.tsx` — 31.4KB / 476 lines
**Hook inventory:** 0 React hooks detected (likely uses class patterns or context only)
**Safe to edit in place?** ✅ **YES** — No hook complexity. Surgical edits are safe.
**Extraction effort (if needed):** Meal.

---

#### `src/protocols/SpatialEngine.ts` — 59.1KB / 1,260 lines
**Hook inventory:** 0 React hooks (pure math/algorithm engine)
**Safe to edit in place?** ✅ **YES** — Pure computational engine. No React coupling. Individual functions are independent. The file is large but well-segmented by algorithm domain.
**Extraction effort (if needed):** Feast — but low urgency since no React hook risk.

---

#### `src/components/docked/UniversalSlidersFooter.tsx` — 32.9KB / 561 lines
**Hook inventory:** 0 React hooks (memo-heavy component, likely pure rendering)
**Safe to edit in place?** ✅ **YES** — No hook complexity detected. Surgical edits to slider rendering logic are safe.
**Extraction effort (if needed):** Meal.

---

#### `src/components/admin/tools/tabs/DiagnosticLabBuilderTab.tsx` — 41.3KB / 587 lines
**Hook inventory:** 4 useState, 2 useEffect, 0 useCallback, 0 useMemo = **6 hook calls total**
**Safe to edit in place?** ✅ **YES** — Low hook count. Admin-only tab. Surgical edits are safe.
**Extraction effort (if needed):** Meal.

---

#### `src/components/admin/tools/tabs/DiagnosticLabOracleTab.tsx` — 47.1KB / 833 lines
**Hook inventory:** 20 useState, 4 useEffect, 0 useCallback, 0 useMemo = **24 hook calls total**
**Key concern:** 20 useState calls is high. However, each state variable is an isolated diagnostic panel control (opcode test parameters, sweep results, stream toggles). There are NO cross-hook dependencies that could trigger render cascades.
**Safe to edit in place?** ✅ **YES** — Despite high useState count, all states are leaf-level diagnostic controls. No BLE write coupling. Surgical edits to individual panel sections are safe.
**Extraction effort (if needed):** Feast — but admin-only tool, low priority.

---

### CRITICAL ADVISORY — DashboardScreen.tsx & DockedController.tsx for Waves 2-6

> **Q: Can Wave 2-6 agents (fsm-boolean, type-safety, state-matrix, misc-guardrail) safely make surgical 3-10 line edits to these files?**

**DashboardScreen.tsx — CONDITIONAL SAFE ⚠️**
- ✅ Safe: editing `useState` initializer values (e.g., changing `false` → FSM string state)
- ✅ Safe: editing individual `useCallback` return-body logic (NOT the dep array)
- ✅ Safe: editing JSX leaf nodes in the render section (lines 900+)
- ❌ UNSAFE: adding or removing any hook call (causes hook count mismatch = render crash)
- ❌ UNSAFE: reordering hook declarations
- ❌ UNSAFE: editing lines 17 (eslint-disable) without full audit of which hooks it covers
- **Mandate:** Wave agents MUST run `view_file` on exact target lines before any edit. MUST run `git diff HEAD` after. File has `/* eslint-disable unused-imports/no-unused-vars, react-hooks/exhaustive-deps */` at line 17 — this masks real problems. Do not add new hooks with this suppressor active.

**DockedController.tsx — HIGH RISK, MUST EXTRACT FIRST for ANY hook-touching task ⚠️⚠️**
- ✅ Safe: editing JSX rendering logic in sub-panel sections (lines 900+)
- ✅ Safe: editing `FixedPatternPreviewRow` ONLY if extracted to its own file first
- ⚠️ Risky: editing `useCallback` bodies — most callbacks have 15+ items in dependency arrays
- ❌ UNSAFE: any edit to lines 205-500 (hook declaration zone with complex ref/state interplay)
- ❌ UNSAFE: touching `loadFavorite` (lines 572-693) — 120-line inline useCallback with 35 dep items
- ❌ UNSAFE: touching `writeToDevice` wrapper (lines 247-263) — BLE write bus, any regression crashes all devices
- **Mandate:** For fsm-boolean or type-safety tasks touching DockedController, extract the target logic to a helper function or sub-hook FIRST, then edit the extracted file. Do not attempt inline surgery on the hook declaration zone.

---

### Extraction Tasks to Add Before Waves 2-6

The following extractions are **PREREQUISITES** for safe Wave 2-6 surgery on the two critical files:

1. **`FixedPatternPreviewRow` → `src/components/docked/FixedPatternPreviewRow.tsx`**
   - Size: ~45 lines
   - Why: Inline component inside `DockedController.tsx` at lines 79-121. Any wave agent touching DockedController render output risks accidentally deleting it.
   - Effort: Snack

2. **`useDeepLinkHandler` → `src/hooks/useDeepLinkHandler.ts`**
   - Size: ~25 lines extracted from DashboardScreen lines 308-327
   - Why: Reduces DashboardScreen surface area for safer wave edits
   - Effort: Snack

These are OPTIONAL but RECOMMENDED before Waves 2-6 if tasks touch the above files. They are not blocking unless a wave task explicitly modifies the zones identified as UNSAFE above.

---

## Wave 5 Verification — 2026-06-30

*Verifier: ⚒️ Sage | Method: git log inspection + live file read of DockedController.tsx and DashboardScreen.tsx*

### Status: PRE-EXISTING ✅

Both extraction tasks were completed in prior sessions before Wave 5 executed:

| Extraction | Status | Evidence |
|---|---|---|
| `FixedPatternPreviewRow` → `src/components/docked/FixedPatternPreviewRow.tsx` | ✅ PRE-EXISTING | Landed in commit `68ab1708`. DockedController.tsx:83 has comment: `// FixedPatternPreviewRow extracted to src/components/docked/FixedPatternPreviewRow.tsx (Phase 1)`. Inline component gone. |
| `useDeepLinkHandler` → `src/hooks/useDeepLinkHandler.ts` | ✅ PRE-EXISTING | Deep-link logic extracted as `useCrewDeepLink()` in `src/screens/Dashboard/DashboardCrewHub.tsx` (commit `49ddd601`). DashboardScreen.tsx has zero remaining `Linking.*` calls. |

All 7 verify gates passed: TSC ✅ Jest ✅ Browser ✅ OP_0x59 ✅ BLE Invariants ✅ Organic Disconnect ✅ Type Safety ✅ Workflow Refs ✅
