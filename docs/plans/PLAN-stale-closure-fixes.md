# Implementation Plan

## Task: fix/stale-closure-fixes
## Cluster: TC-14
## Risk: [M-RISK] | Size: [Snack] | Layer: [HOOK]
## Status: [⚪ READY]

---

## Problem Statement

Three stale closure bugs confirmed by the deep-dive fleet:

**Bug 1 (HIGH):** `useGlobalTelemetry.ts:143` — `commitSession` has empty dep array `[]`. Every session is committed with a stale `user?.id = undefined` even after auth resolves. All auto-saved telemetry sessions are incorrectly stored as anonymous.

**Bug 2 (MEDIUM):** `CustomEffectVisualizer.tsx:58` — `effectId` is omitted from the `useEffect` dependency array. If the user switches effects without changing speed, the old tick rate continues. Visual frame rate is wrong for the new effect.

**Bug 3 (HIGH):** `SessionContext.tsx:319` — `setInterval` is recreated on every telemetry tick because live values are in the dependency array. This causes the notification interval to thrash every second while skating.

---

## Source of Truth

- **`artifacts/deepdive_raw/R-12_findings.json`** → R12-001 (`useGlobalTelemetry.ts:143`, HIGH), R12-002 (`CustomEffectVisualizer.tsx:58`, MEDIUM)
- **`artifacts/deepdive_raw/DOMAIN_NATIVE_&_WATCH_findings.json`** → NATIVE_WATCH-002 (`SessionContext.tsx:319`, HIGH)
- **`src/hooks/useGlobalTelemetry.ts:143`** — `const commitSession = useCallback(async () => { ... await SpeedTrackingService.saveSession(snapshot, user?.id || null); }, []);`
- **`src/components/CustomEffectVisualizer.tsx:58`** — `}, [speed, autoPlay]);`
- **`src/context/SessionContext.tsx:319`** — interval recreated with telemetry values in deps

---

## Proposed Changes

### [MODIFY] `src/hooks/useGlobalTelemetry.ts`

**Fix R12-001 — Use userRef to avoid stale closure without recreating callback:**
```typescript
const userIdRef = useRef<string | null>(null);
useEffect(() => { userIdRef.current = user?.id ?? null; }, [user]);

const commitSession = useCallback(async () => {
  // ... snapshot logic ...
  await SpeedTrackingService.saveSession(snapshot, userIdRef.current);
}, []);  // No change to deps — reads from ref instead
```

### [MODIFY] `src/components/CustomEffectVisualizer.tsx`

**Fix R12-002 — Add effectId to dependency array:**
```diff
- }, [speed, autoPlay]);
+ }, [speed, autoPlay, effectId]);
```

### [MODIFY] `src/context/SessionContext.tsx`

**Fix NATIVE_WATCH-002 — Move interval to stable ref pattern:**
```typescript
const displayNotificationRef = useRef(displayNotification);
useEffect(() => { displayNotificationRef.current = displayNotification; });

useEffect(() => {
  updateInterval = setInterval(() => { displayNotificationRef.current(); }, 5000);
  return () => clearInterval(updateInterval);
}, []); // Stable — reads latest via ref
```

---

## Verification Plan

### Automated
- `npm run verify` — TSC clean

### Manual
1. **R12-001**: Start a session logged in → commit → check `skate_sessions` table — session must have `user_id` populated (not null)
2. **R12-002**: Load CustomEffectVisualizer with a breathing effect → switch to strobe without changing speed → verify strobe tick rate (faster)
3. **NATIVE_WATCH-002**: Start a session → watch React DevTools or ADB logcat — interval should not recreate every second

---

## Worktree
`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\stale-closure-fixes\`
Branch: `fix/stale-closure-fixes`
