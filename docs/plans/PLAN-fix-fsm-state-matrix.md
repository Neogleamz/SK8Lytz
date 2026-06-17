# Implementation Plan: fix/fsm-state-matrix

## Goal
Replace disjoint `loading`/`error`/`success` boolean state variables with unified FSM string union types across all data-fetching components and hooks, enforcing the 4-state matrix (`loading | error | empty | success`) per R-14.

## Source Analysis Link
- Cluster 8 in [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md#L184-L188) — 38 R-14 violations
- Rule: R-14 (State Matrix — use FSM string unions, not booleans)
- Existing FSM exemplar: `useGradients.ts:15` — `type GradientStatus = 'idle' | 'loading' | 'error' | 'success'`

## Files to Create/Modify

### New File
| File | Purpose |
|------|---------|
| `src/types/ViewState.ts` | Shared `ViewState` union type + `ViewStateInfo` discriminated struct |

### Tier 1 — Full Boolean→FSM Conversion (10 files)
| # | File | Current Pattern | Line Refs |
|---|------|----------------|-----------|
| 1 | `src/components/account/SkaterStatsPanel.tsx` | `loading` (L23) + `error` (L24) | Source: SkaterStatsPanel.tsx:23-24 |
| 2 | `src/components/admin/tools/GlobalAnalyticsPanel.tsx` | `loading` (L16) + `error` (L17) | Source: GlobalAnalyticsPanel.tsx:16-17 |
| 3 | `src/components/auth/AuthFormForgotPassword.tsx` | `loading` (L22) + `errorMessage` (L23) + `successMessage` (L24) | Source: AuthFormForgotPassword.tsx:22-24 |
| 4 | `src/components/auth/AuthFormSignIn.tsx` | `loading` (L34) + `errorMessage` (L35) | Source: AuthFormSignIn.tsx:34-35 |
| 5 | `src/components/CommunityModal.tsx` | `loading` (L201) + `error` (L202) | Source: CommunityModal.tsx:201-202 |
| 6 | `src/components/CrewMemberDashboard.tsx` | `isLoading` (L166) + `error` (L167) | Source: CrewMemberDashboard.tsx:166-167 |
| 7 | `src/hooks/useCuratedPicks.ts` | `picksLoading` (L23) + `error` (L24) | Source: useCuratedPicks.ts:23-24 |
| 8 | `src/hooks/useRecentSpots.ts` | `isLoading` (L18) + `error` (L19) | Source: useRecentSpots.ts:18-19 |
| 9 | `src/hooks/useSkateStats.ts` | `statsLoading` (L11), no error state | Source: useSkateStats.ts:11 |
| 10 | `src/hooks/useRegistration.ts` | `isLoading` (L79), no error state | Source: useRegistration.ts:79 |

### Tier 2 — Hybrid Cleanup (existing FSM + redundant error side-variable, 4 files)
| # | File | Has FSM | Redundant Side-Var | Line Refs |
|---|------|---------|--------------------|-----------|
| 11 | `src/hooks/useAccountOverview.ts` | `OverviewStatus` (L40) | `accountError` (L42) + `crewError` (L64) | Source: useAccountOverview.ts:40-64 |
| 12 | `src/hooks/useCrewManage.ts` | `ManageStatus` (L41) | `createCrewError` (L44) + `loadingCardMembersFor` (L18) | Source: useCrewManage.ts:18-44 |
| 13 | `src/hooks/useGradients.ts` | `GradientStatus` (L15) | `error` (L17) | Source: useGradients.ts:15-17 |
| 14 | `src/hooks/useScenes.ts` | status union (L9) | `errorMsg` (L10) | Source: useScenes.ts:9-10 |

### Tier 3 — Error-Only Normalization (6 files)
| # | File | Current Pattern | Line Refs |
|---|------|----------------|-----------|
| 15 | `src/components/crew/CrewJoinScreen.tsx` | `error` only (L25) | Source: CrewJoinScreen.tsx:25 |
| 16 | `src/components/crew/CrewLandingScreen.tsx` | `error` only (L48) | Source: CrewLandingScreen.tsx:48 |
| 17 | `src/hooks/useFavorites.ts` | status union (L17) + `errorMsg` (L18) | Source: useFavorites.ts:17-18 |
| 18 | `src/screens/AuthScreen.tsx` | `errorMessage` (L51) | Source: AuthScreen.tsx:51 |
| 19 | `src/screens/Onboarding/HardwareSetupWizardScreen.tsx` | `setupError` (L91) | Source: HardwareSetupWizardScreen.tsx:91 |
| 20 | `src/providers/ComplianceGate.tsx` | `ComplianceStatus` (L20) + `error` (L23) | Source: ComplianceGate.tsx:20-23 |

## Steps

### Step 1 — Create shared `ViewState` type
Create `src/types/ViewState.ts` exporting:
```typescript
export type ViewState = 'idle' | 'loading' | 'error' | 'empty' | 'success';
export interface ViewStateInfo {
  viewState: ViewState;
  errorMsg: string;
}
```
- **Verify:** `npm run verify` passes. New file has no `any` casts.

### Step 2 — Tier 1: Full boolean→FSM conversion (files 1–10)
For each file, apply the pattern:
1. Remove `loading`/`isLoading`/`error`/`errorMessage`/`successMessage` boolean `useState` declarations.
2. Add `import type { ViewState } from '../types/ViewState';` (adjust path).
3. Add `const [viewState, setViewState] = useState<ViewState>('loading');` and `const [errorMsg, setErrorMsg] = useState('');`.
4. Replace all `setLoading(true)` → `setViewState('loading')`, `setLoading(false)` after success → `setViewState('success')` or `setViewState(data.length ? 'success' : 'empty')`.
5. Replace all `setError(msg)` → `setErrorMsg(msg); setViewState('error');`.
6. Update conditional rendering: `if (loading)` → `if (viewState === 'loading')`, `if (error)` → `if (viewState === 'error')`.
7. Update return type / exposed properties to use `viewState` + `errorMsg` instead of `loading` + `error`.
- **Verify:** After each file: `git diff HEAD <filename>` — confirm only state declarations, setters, and conditionals changed. No JSX layout drift. `npm run verify` passes after all 10 files.

### Step 3 — Tier 2: Hybrid cleanup (files 11–14)
These files already have a status FSM union but carry redundant `error`/`errorMsg` side-variables:
1. Fold `errorMsg`/`accountError`/`crewError`/`createCrewError` into a single `errorMsg: string` if not already present.
2. For `useCrewManage.ts`: convert `loadingCardMembersFor` from `string | null` to a status enum value if it's only used as a loading guard. If it carries a crew ID, keep it but add a `cardMembersState: ViewState` companion.
3. Ensure `'error'` is in the existing status union if missing.
4. Remove the redundant boolean and update all consumers.
- **Verify:** `git diff HEAD <filename>` per file. `npm run verify` passes. No `loading` or `isLoading` boolean remains in these 4 files.

### Step 4 — Tier 3: Error-only normalization (files 15–20)
These files have standalone `error`/`errorMessage`/`setupError` strings without a loading boolean:
1. Where the component also fetches data (e.g., `CrewLandingScreen`), add a `ViewState` FSM.
2. Where the error is form-submission feedback only (e.g., `CrewJoinScreen`, `AuthScreen`), keep `errorMsg: string` but rename to `errorMsg` for consistency.
3. For `ComplianceGate.tsx` and `useFavorites.ts`: fold `error` into the existing status union's `'error'` state.
- **Verify:** `npm run verify` passes. Grep for `useState<string | null>(null)` named `error` across all 20 files returns 0 hits.

### Step 5 — Boy Scout cleanup across touched files
In each modified file only:
1. Remove any dead imports left behind (old `useState` count may drop).
2. Remove stale comments referencing `// loading state` or `// error state`.
3. Add JSDoc to the ViewState variable: `/** 4-state FSM: idle → loading → success/empty/error */`.
- **Verify:** `git diff HEAD` — confirm no files outside the 20-file scope were touched.

### Step 6 — Final verification
1. `npm run verify` (TSC + Jest + AST + TypeSafety).
2. Grep validation: `Select-String -Path "src/**/*.ts","src/**/*.tsx" -Pattern "useState\(true\)|useState\(false\)" | Where-Object { $_.Line -match "loading|isLoading" }` → 0 hits in the 20 target files.
- **Verify:** All gates green. No R-14 violations remain in the 20 target files.

## Verification Plan

| Gate | Command | Success Condition |
|------|---------|-------------------|
| TSC | `npm run verify` | Zero type errors |
| Grep — no boolean loading | `rg "useState\((true\|false)\)" <file>` per target | 0 matches for loading/isLoading patterns |
| Grep — no `string \| null` error | `rg "useState<string \| null>" <file>` per target | 0 matches for error-named variables |
| Post-diff scope check | `git diff HEAD --stat` | Only the 20 target files + `ViewState.ts` appear |
| Jest | `npm run verify` | All existing tests pass |

## Out of Scope
- Components using a single boolean for genuine toggle state (e.g., `showPassword`, `rememberMe`, `showRadiusPicker`) — these are UI toggles, not data-fetching state.
- Admin tools beyond `GlobalAnalyticsPanel.tsx` — per user direction.
- BLE hooks/services — different FSM domain (XState machine), not R-14 boolean traps.
- Refactoring component JSX rendering logic beyond conditional guards — layout changes are out of scope.
- Creating a shared `useViewState()` custom hook — a possible future optimization, not required for R-14 compliance.
- `docs/SK8Lytz_App_Master_Reference.md` update — will be handled by the Docs Gate (§6) during `/start-task` execution, not pre-planned here.
