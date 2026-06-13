# Implementation Plan

## Task: sweep-ui-docked-controller
**Slug:** sweep-ui-docked-controller
**Wave:** [WAVE:6] — Prerequisite: Wave 5 fully merged
**Size:** [Feast] — 8 files
**Risk:** [H-RISK] — DockedController is the primary BLE dispatch UI; unawaited writes here silently drop commands
**Status:** [✅ READY]
**Source of Truth:** `artifacts/system_audit_report.md` + `artifacts/deepdive_raw/DOMAIN_UI_DOCKED_CONTROLLER_findings.json` + `artifacts/deepdive_raw/R-11_findings.json`
**Prerequisite:** Wave 5 fully merged (and by extension all prior waves merged, ensuring BLE types are stable)

## Goal
Fix 25 findings in the docked controller UI layer. Critical: `writeToDevice` is called without `await` or `.catch()` in 5+ locations in `UniversalSlidersFooter` alone — these are fire-and-forget BLE writes that silently fail with no user feedback. Fix the stale closure in `useStreetMode` where `deviceContext` is captured at mount time and never updated. Fix the re-render instability in `useDashboardController` where `MemoizedSk8lytzController` takes non-memoized dependencies. Fix the `DockedController` context over-consumption (4 contexts). Note: `DockedController.tsx` monolith extraction is out of scope for this sweep.

## Decision Log
- **Unawaited `writeToDevice` (CONFIRMED — R-11 HIGH, 5+ locations)**: In `UniversalSlidersFooter.tsx`, `writeToDevice` is declared as returning `void` in the props type but actually returns a `Promise` at runtime. Every call in slider change handlers, Camera Vibe mode, and multi-color preset updates is fire-and-forget. BLE errors are silently swallowed. All must be wrapped in async handlers with `AppLogger.error` on catch.
- **Stale closure in `useStreetMode` (R-12 HIGH)**: `deviceContext` is used inside an Accelerometer listener that captures it at registration time. If the device context changes (new device connected, existing disconnected), the listener still holds the stale reference. Must restructure with a `useRef` that is kept current.
- **`MemoizedSk8lytzController` re-render storm (R-21 HIGH)**: The memoized controller takes `useDashboardController` output directly. The hook returns new object references on every call, breaking memoization. Output must be stabilized with `useMemo`/`useCallback`.
- **4-context consumption in `DockedController` (R-27 MEDIUM)**: `DockedController` directly imports and consumes `AppConfigContext`, `BLEContext`, `FavoritesContext`, and `ThemeContext`. Should be refactored to accept props from a parent container that does context selection. Deferred — too architectural for this sweep; logged as a TODO.
- **`DockedController.tsx` monolith (~35KB)**: Extraction deferred — only surgical fixes inside it.

## Files to Create/Modify

### [MODIFY] src/components/docked/UniversalSlidersFooter.tsx
- L393: Fix `writeToDevice` prop type — update from `void` return to `Promise<void>` return
- L393: Wrap call in async handler with `.catch(e => AppLogger.error(...))`
- L401: Wrap unawaited `writeToDevice` call in async handler with catch
- L412: Wrap Camera Vibe mode `writeToDevice` call in async handler with catch
- L501: Wrap slider change handler `writeToDevice` call in async handler with catch

### [MODIFY] src/components/docked/BuilderPanel.tsx
- L77: Wrap unawaited `writeToDevice` call in async handler with catch (R-11)

### [MODIFY] src/hooks/useStreetMode.ts
- L188: Replace captured `deviceContext` in Accelerometer listener with a `useRef` that is updated on every render, referenced inside the listener via `.current` (R-12)

### [MODIFY] src/hooks/useDashboardController.tsx
- L245: Stabilize `MemoizedSk8lytzController` dependencies — wrap hook outputs that are objects/functions in `useMemo`/`useCallback` (R-21)

### [MODIFY] src/hooks/useDockedControllerState.ts
- L201: Fix unstable `applySpatialSegments` dynamic ID generation — use a stable key derivation strategy that doesn't create new IDs on every render (R-07)

### [MODIFY] src/components/DockedController.tsx
- L157: Verify `AppState` change listener removal uses the correct unsubscribe pattern (subscription-based vs. event-emitter-based) — align with React Native current API (R-22)
- Add `// TODO: extract contexts to parent container (R-27)` comment at context consumption lines (L194)

### [MODIFY] src/components/docked/FavoritesPanel.tsx
- L100: Add full 4-state UI matrix for the `SK8Lytz Picks` and favorites sections (R-14)
- L89: Fix `null as IFavoriteState` double cast — use a proper nullable type `IFavoriteState | null` (R-08)

### [MODIFY] src/components/docked/CameraPanel.tsx
- L175: Extract inline arrow function in swatches `map` to a stable `useCallback` (R-07)
- L22: Replace local `rgbToHexStr` with import from `src/utils/` canonical `rgbToHex` (R-21)

## Out of Scope
- `DockedController.tsx` full extraction into sub-components (separate Feast task)
- `DockedController.tsx` 4-context refactor to container pattern (deferred — architectural)
- No BLE core service changes (handled in Wave 2)

## Verification Plan
- `npm run verify` — TSC must pass; `writeToDevice` prop type change must propagate correctly
- `grep 'writeToDevice(' src/components/docked/UniversalSlidersFooter.tsx` must show only awaited / catch-wrapped calls
- Verify `useStreetMode` stale closure fix by confirming `deviceContext.current` is accessed inside listener, not the outer captured variable
