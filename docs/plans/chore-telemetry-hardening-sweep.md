# chore/telemetry-hardening-sweep

**Post-Refactor Telemetry & Error Capture Hardening**

📅 2026-04-17 | ☁️ CLOUD | ⚠️ H-RISK | 🍱 Meal | 🤖 PRO-HIGH | ⏱️ ~3h | 🪙 ~50k

---

## Motivation

After the major domain-hook extraction and DashboardScreen decomposition, many error paths were refactored or introduced. Several `catch` blocks are swallowing errors silently, and key screen transitions have zero telemetry. The existing `SafeErrorBoundary` is a single global wrapper — a crash in any subtree (e.g., CrewHub, SkateMap) kills the entire app UX instead of isolating to that module.

---

## Gap Analysis (Evidence from Source Audit)

### 1. Silent `catch` Blocks (Error Swallowing)
The following hooks have `catch (() => {})` or `catch (e) {}` blocks that discard errors without logging:
- `useMapFilters.ts` — L34, L48
- `useFavorites.ts` — L57, L66
- `useDashboardVoice.ts` — L60, L68, L70, L119
- `useDashboardProfile.ts` — L94, L107, L108, L115, L124
- `useDashboardGroups.ts` — L121, L123, L146, L148, L154
- `useProductCatalog.ts` — L71, L95, L149
- `useVoiceControl.ts` — L11, L50

**Action**: Replace every silent catch with `AppLogger.warn('[Hook] Operation failed', err)`.

### 2. Missing Screen-Level Error Boundaries
Only `App.tsx` has a `SafeErrorBoundary`. No per-screen isolation exists.

**Action**: Create a reusable `<ScreenErrorBoundary>` component and wrap each major screen:
- `DashboardScreen.tsx` (inner content)
- `SkateMapScreen.tsx`
- `AuthScreen.tsx`
- Crew flow screens (`CrewHub`, `CrewCreate`, `CrewManage`, etc.)
- Docked Controller / Modals

### 3. Unhandled Promise Rejection Handler
No global `unhandledrejection` or `rejectionhandled` listener exists.

**Action**: Add to `App.tsx`:
```typescript
// Global unhandled promise rejection capture
if (typeof globalThis !== 'undefined') {
  const OriginalPromise = globalThis.Promise;
  // React Native uses a polyfill — hook into the rejection tracking
  if (typeof (global as any).HermesInternal !== 'undefined') {
    // Hermes engine — use ErrorUtils pattern
    // Already covered by ErrorUtils handler
  }
}
// For web builds:
if (typeof window !== 'undefined') {
  window.addEventListener('unhandledrejection', (event) => {
    AppLogger.error('[UnhandledPromise]', event.reason);
  });
}
```

### 4. Screen Navigation Telemetry
`SCREEN_OPENED` event type exists in `AppLogger` but is never called.

**Action**: Add `AppLogger.log('SCREEN_OPENED', { screen: 'ScreenName' })` to each screen's mount `useEffect`:
- `DashboardScreen`
- `SkateMapScreen`
- `AuthScreen`
- All Crew screens
- All onboarding screens

### 5. AppState Foreground Re-Sync
`App.tsx` L210-212 has a dead `// Reserved for future active-state syncs` comment.

**Action**: Log `APP_FOREGROUNDED` event with session/BLE state context:
```typescript
if (nextAppState === 'active') {
  AppLogger.log('APP_FOREGROUNDED', { timestamp: Date.now() });
}
```

### 6. Console.error Override for Third-Party Libraries
Third-party libraries (maps, voice, push) may `console.error` without throwing.

**Action**: Monkey-patch `console.error` in `App.tsx` to pipe to AppLogger:
```typescript
const originalConsoleError = console.error;
console.error = (...args: any[]) => {
  AppLogger.warn('[console.error]', { args: args.map(a => String(a)).join(' ') });
  originalConsoleError.apply(console, args);
};
```

### 7. New EventType Additions
Add missing events for full coverage:
- `SCREEN_ERROR` — for per-screen ErrorBoundary catches
- `PROMISE_REJECTION` — for unhandled async errors
- `NAVIGATION_TRANSITION` — screen change tracking (future nav library integration)

---

## File Change Matrix

| File | Action |
|------|--------|
| `App.tsx` | Add unhandled promise handler, console.error override, APP_FOREGROUNDED log |
| `src/services/AppLogger.ts` | Add `SCREEN_ERROR`, `PROMISE_REJECTION` to EventType union |
| `src/components/ScreenErrorBoundary.tsx` | **[NEW]** Reusable per-screen error boundary |
| `src/hooks/useMapFilters.ts` | Replace silent catches with AppLogger.warn |
| `src/hooks/useFavorites.ts` | Replace silent catches with AppLogger.warn |
| `src/hooks/useDashboardVoice.ts` | Replace silent catches with AppLogger.warn |
| `src/hooks/useDashboardProfile.ts` | Replace silent catches with AppLogger.warn |
| `src/hooks/useDashboardGroups.ts` | Replace silent catches with AppLogger.warn |
| `src/hooks/useProductCatalog.ts` | Replace silent catches with AppLogger.warn |
| `src/hooks/useVoiceControl.ts` | Replace silent catches with AppLogger.warn |
| `src/screens/DashboardScreen.tsx` | Wrap content with ScreenErrorBoundary, add SCREEN_OPENED |
| `src/screens/SkateMapScreen.tsx` | Wrap with ScreenErrorBoundary, add SCREEN_OPENED |
| `src/screens/AuthScreen.tsx` | Add SCREEN_OPENED logging |
| Crew screens (6 files) | Add SCREEN_OPENED logging |

---

## Verification Plan

### Automated
- `npx tsc --noEmit` — confirm zero new type errors
- Grep audit: `grep -r "catch.*{}" src/hooks/` should return 0 silent catches post-fix

### Manual
- Build debug APK and trigger known error scenarios
- Verify `telemetry_errors` Supabase table receives VIP Fast-Lane entries
- Confirm ScreenErrorBoundary isolates a forced crash without killing the whole app
- Check `telemetry_snapshots` for `SCREEN_OPENED` and `APP_FOREGROUNDED` events
