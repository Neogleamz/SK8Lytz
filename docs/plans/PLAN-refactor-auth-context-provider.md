# Implementation Plan

## Goal
Extract auth state from `App.tsx` local state into a proper `AuthContext` / `useAuth()` hook to eliminate N parallel `supabase.auth.getUser()` calls across services.

## Source of Truth
- `App.tsx`:109-111 — `session`, `sessionLoaded`, `offlineMode` declared as local state in `AppContent`
- `App.tsx`:151-175 — `init()` function with `getSession()` call and `onAuthStateChange` listener setup
- `src/hooks/useAccountOverview.ts`:77 — independent `supabase.auth.getUser()` call
- `src/hooks/useDashboardProfile.ts`:101 — independent `supabase.auth.getUser()` call
- `src/services/AuthProfileService.ts`:23 — independent `supabase.auth.getUser()` call

## Steps

### Step 1 — Read App.tsx auth state block
- Action: `view_file App.tsx L107-213` — map every state variable, the `init()` async function, the `supabase.auth.onAuthStateChange` listener, and how `session`/`sessionLoaded`/`offlineMode` are passed to child components
- Source: `App.tsx`:107-213
- Verify: Full picture of what must move into AuthProvider before any code is written

### Step 2 — Create AuthContext.tsx
- Action: Create `src/context/AuthContext.tsx` — define `AuthContextValue` interface `{ session: Session | null; user: User | null; isOfflineMode: boolean; isAuthenticated: boolean; sessionLoaded: boolean; }`. Export `AuthContext = createContext<AuthContextValue>()`, `AuthProvider` component containing all moved state + `init()` + `onAuthStateChange` listener, and `useAuth()` hook with invariant guard
- Source: `App.tsx`:109-111, 151-175 — exact state and logic to migrate
- Verify: File compiles with `tsc --noEmit`; no `any` casts; `useAuth()` throws if called outside `AuthProvider`

### Step 3 — Move state and listener into AuthProvider
- Action: Cut `session`, `sessionLoaded`, `offlineMode` state declarations from `AppContent`; paste into `AuthProvider`. Move `init()` and `supabase.auth.onAuthStateChange` subscription into `AuthProvider` `useEffect`. Derive `user = session?.user ?? null` and `isAuthenticated = !!session && !isOfflineMode` inside provider
- Source: `App.tsx`:109-175 — exact lines being relocated
- Verify: `App.tsx` no longer declares `session`/`sessionLoaded`/`offlineMode` local state; AuthProvider renders children with all values

### Step 4 — Wrap AppContent in AuthProvider
- Action: `view_file App.tsx` — find the provider hierarchy (QueryClient, GestureHandler, etc.); add `<AuthProvider>` as the innermost wrapper around `<AppContent>` (or outermost depending on dependency ordering — verify no provider depends on auth)
- Source: `App.tsx` — existing provider hierarchy
- Verify: App renders without crash; `useAuth()` returns non-null values in child components

### Step 5 — Replace getUser() calls in hooks and services
- Action: In `useAccountOverview.ts` L77 — replace `supabase.auth.getUser()` with `const { user } = useAuth()`. In `useDashboardProfile.ts` L101 — same replacement. In `AuthProfileService.ts` L23 — services cannot use hooks; instead accept `userId` as a parameter from the calling hook rather than calling `getUser()` internally; update callers to pass `useAuth().user?.id`
- Source: `src/hooks/useAccountOverview.ts`:77, `src/hooks/useDashboardProfile.ts`:101, `src/services/AuthProfileService.ts`:23
- Verify: Zero calls to `supabase.auth.getUser()` remain in hooks or components; only legitimate service-layer calls remain

### Step 6 — Update ComplianceGate to use useAuth()
- Action: `view_file src/providers/ComplianceGate.tsx L24-60` — locate where `isOfflineMode` enters (prop or internal state); replace with `const { isOfflineMode } = useAuth()` and remove any prop drilling of offline mode from parent
- Source: `src/providers/ComplianceGate.tsx`:29-31
- Verify: `ComplianceGate` renders correctly in offline mode without prop; parent no longer passes `isOfflineMode` prop

### Step 7 — Verify and grep
- Action: Run `npm run verify`; then run `grep -rn "supabase.auth.getUser" src/` and review each remaining call — confirm only service-layer calls that genuinely need a fresh server-side auth check remain (none in hooks or components)
- Source: All files under `src/`
- Verify: `npm run verify` exits 0; grep returns only service-layer files with justified usage

## Out of Scope
- Auth flow changes (sign-in, sign-up, magic link)
- Token refresh mechanism
- Biometric auth
- Session timeout logic
- Any BLE or device layer changes
