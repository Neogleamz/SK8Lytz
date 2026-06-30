# Implementation Plan

## PLAN-state-matrix-sweep — UI State Matrix + AsyncStorage + Hardcoded Delays
*Source: `/deepdive-code-hunt` fleet | Rules: R-14, R-24, R-16 | Date: 2026-06-10*

### Problem
Three related quality issues bundled together:
- **R-14:** Hooks that expose no `error` or `isLoading` state — consumers must render blindly
- **R-24:** AsyncStorage keys without namespace prefixes — risk of cross-feature collisions
- **R-16:** Hardcoded `setTimeout` delays in BLE and UI flows instead of queue/lifecycle events

### Source of Truth
- `artifacts/system_audit_report.md` — CLUSTER-08
- `src/constants/storageKeys.ts` — canonical key definitions

### R-14 Affected Files
| File | Missing State | Action |
|---|---|---|
| `src/hooks/useCuratedPicks.ts:112` | `error` state swallowed (CONFIRMED) | Add `error` state, expose in return object |
| `src/components/docked/FavoritesPanel.tsx:100` | `picksLoading` prop declared but ignored | Destructure + render ActivityIndicator |
| `src/providers/ComplianceGate.tsx:137` | No error state — fails open | Add explicit error boundary |
| `src/hooks/useRecentSpots.ts:14` | Missing `isLoading` + `error` | Add both to hook return |
| `src/hooks/useFavorites.ts:211` | Missing `isLoading` + `error` | Add both to hook return |

### R-24 AsyncStorage Key Audit
| File | Key | Issue |
|---|---|---|
| `src/hooks/useFavorites.ts:42` | `@Sk8lytz_Favorites` | OK — has prefix |
| `src/hooks/useFavorites.ts:97` | `@Sk8lytz_QuickPresets` | OK — has prefix |
| `src/context/ThemeContext.tsx` | Verify key | Check against `storageKeys.ts` |
| `src/utils/migrateAuthTokens.ts` | Verify key | Check for raw string vs constant |
| `src/services/AppSettingsService.ts` | Verify key | Check for constant usage |
| `src/components/auth/DevSandboxDrawer.tsx` | Verify key | Check `STORAGE_DEMO_MODE` |
| `src/constants/storageKeys.ts` | All keys | Audit: all keys must be namespaced with `@SK8Lytz_` or `@Sk8lytz_` prefix (normalize casing) |

### R-16 Hardcoded Delays
| File | Line | Context | Action |
|---|---|---|---|
| `src/screens/DashboardScreen.tsx:197` | 197 | State initialization | Replace with layout event or `InteractionManager.runAfterInteractions` |
| `src/hooks/useStreetMode.ts:158` | 158 | Pattern application delay | Rely on `BleWriteQueue` internal timing |
| `App.tsx:112` | 112 | SplashScreen hide | Use `SplashScreen.hideAsync()` after layout completes, not arbitrary delay |
| `src/components/PositionalGradientBuilder.tsx:44` | 44 | Animation | Leave as LOW — UI animation context, downgrade to LOW |
| `src/components/VerticalPatternDrum.tsx:37,73` | 37, 73 | Animation | Leave as LOW — UI animation context |

### Implementation Steps
1. Fix all R-14 hooks — add `error` state and expose in return objects
2. Audit `storageKeys.ts` and normalize all key naming to `@SK8Lytz_` prefix
3. Replace `setTimeout` in DashboardScreen and App.tsx with proper lifecycle events
4. Fix `useStreetMode` to rely on queue timing

### Verify
- `npm run verify`
- Manual: trigger load error in `useCuratedPicks` — confirm error renders in UI

---

## Wave 8 Verification — 2026-06-30

*Verifier: ⚒️ Sage | Method: Read exact target lines for all 13 audit items*

### Status: ALL PRE-EXISTING ✅

**Group A — R-14 Error States (6 targets):** All 6 hooks/components already implement full 4-state FSM (`viewState`, `errorMsg`, `error`, `isLoading`). No edits required.
- SkaterStatsPanel.tsx, useScenes.ts, useGradients.ts, useCuratedPicks.ts, useRecentSpots.ts, useFavorites.ts — all ✅

**Group B — R-16 Hardcoded Delays (3 targets):** All 3 already fixed.
- useStreetMode.ts:158 — no setTimeout, relies on BleWriteQueue internally ✅
- App.tsx:112 — uses InteractionManager.runAfterInteractions → SplashScreen.hideAsync() ✅
- DashboardScreen.tsx:197 — S4 monolith (>30KB) + line is a useState init, not a setTimeout ✅

**Group C — R-24 Key Audit (4 targets):** All 4 files use centralized storageKeys.ts constants ✅

All 7 verify gates passed: TSC ✅ Jest ✅ Browser ✅ OP_0x59 ✅ BLE Invariants ✅ Organic Disconnect ✅ Type Safety ✅ Workflow Refs ✅
