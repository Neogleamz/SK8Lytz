# Implementation Plan

## fix/session-ui-cleanup

### Goal
Three low-risk cleanup items found in the post-merge audit:
1. `StreetPanel.tsx` — `pulseAnim` animation loop is orphaned dead code (runs but targets a View only shown when IDLE where sessionActive=false)
2. `DockedController.tsx` — 4 comments still reference deleted `useGlobalTelemetry` hook
3. `SessionCommitService.test.ts` — `let mockInput: any` violates the No `any` Cast Law (S3)

### Source of Truth
- `src/components/docked/StreetPanel.tsx` — `pulseAnim` L70–84; `sessionActive` state L37
- `src/components/DockedController.tsx` — stale comments L64, L66, L155, L279
- `src/services/session/__tests__/SessionCommitService.test.ts` — `let mockInput: any` L26

---

## Files to Create/Modify

### [MODIFY] `src/components/docked/StreetPanel.tsx`
**Step 1 — Remove orphaned pulseAnim dead code**
- Read lines L37 and L70–84 first (view_file)
- Remove `const [sessionActive, setSessionActive] = useState(false)` if `sessionActive` is only used by `pulseAnim` — check for other usages first
- Remove `const pulseAnim = useRef(new Animated.Value(1)).current` (L~71)
- Remove the `useEffect` that runs the pulse animation loop (L72–84)
- If `sessionActive` is used elsewhere → keep the state; only remove the animation
- Verify: `git diff` shows only animation lines removed; component still renders correctly

### [MODIFY] `src/components/DockedController.tsx`
**Step 2 — Update 4 stale comments**
- Line 64: Replace `// NOTE: useGlobalTelemetry intentionally NOT imported here.` with `// NOTE: Telemetry values received via props from DashboardScreen through useSession().`
- Line 66: Replace reference to `useGlobalTelemetry(isSkateSessionActive)` in comment with `useSession()` pattern
- Line 155: Update stale comment about `DashboardScreen's single useGlobalTelemetry instance` → reference `SessionContext`/`useSession()`
- Line 279: Update `// REMOVED: useGlobalTelemetry(true) — values now received as props from DashboardScreen.` → make accurate to current architecture
- View exact lines before editing each one
- Verify: `git diff` shows exactly 4 comment line changes, nothing else

### [MODIFY] `src/services/session/__tests__/SessionCommitService.test.ts`
**Step 3 — Fix `any` cast**
- View line L26 and surrounding context first
- `let mockInput: any` → type to `SessionCommitServiceInput` or create a typed interface
- Import `SessionCommitServiceInput` from `'../SessionCommitService'` (or wherever it's exported)
- Verify: TSC clean after change

---

## Verification Plan

### Automated
```
npm run verify
```
TSC must be clean. Jest 28/28 suites must pass.

### Manual Verification
- `StreetPanel.tsx` — no `pulseAnim` or dead animation useEffect remains
- `DockedController.tsx` — grep for `useGlobalTelemetry` returns only zero results (comments updated)
- `SessionCommitService.test.ts` — no `any` keyword in `mockInput` declaration

### Out of Scope
- No changes to any session service files (those are in fix/session-machine-actor-types)
- No new tests to be added here (test coverage gaps are tracked separately)
- No logic changes — comments and dead code only
