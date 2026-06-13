# Implementation Plan

## Goal
Show a user-visible "Your session expired — please sign in again" message when `getSession()` returns null due to an expired refresh token, instead of silently routing to AuthScreen.

## Source of Truth
- `App.tsx`:151-175 — `init()` function containing the `getSession()` call and the null-path routing logic
- `App.tsx`:179-213 — `onAuthStateChange` listener and `AppContent` state declarations
- `src/components/auth/AuthScreen.tsx` — prop surface for receiving `sessionExpired` signal and rendering the banner

## Steps

### Step 1 — Read App.tsx auth init block
- Action: `view_file App.tsx L147-213` — map `init()` exactly: what happens when `getSession()` returns null, what state changes are triggered, how `AppContent` passes data to `AuthScreen`, and the full `onAuthStateChange` handler
- Source: `App.tsx`:147-213
- Verify: Know the exact lines where the null-session path routes to AuthScreen before writing any code

### Step 2 — Add sessionExpired state
- Action: In `AppContent`, add `const [sessionExpired, setSessionExpired] = useState(false)` alongside the existing `session`/`sessionLoaded`/`offlineMode` state declarations
- Source: `App.tsx`:109-111 — insertion point next to existing state
- Verify: State variable declared; TypeScript compiles; no other state variable renamed or removed

### Step 3 — Detect prior session evidence and set sessionExpired in init()
- Action: In `init()`, in the branch where `getSession()` returns null AND offline skip is not set: call `AsyncStorage.getItem('@Sk8lytz_auth_last_email')` — if a non-null value is returned (evidence of a prior sign-in), call `setSessionExpired(true)`. Do not call `setSessionExpired(true)` on a genuinely fresh install (no prior email key)
- Source: `App.tsx`:151-175 — null-session branch
- Verify: `sessionExpired` is only `true` when there is evidence of a prior session; fresh installs see `false`

### Step 4 — Detect session expiry via onAuthStateChange
- Action: In the `onAuthStateChange` handler — if `event === 'SIGNED_OUT'` AND `session` (the previous React state value) was non-null at the time of the event (indicating an in-app expiry, not a user-initiated sign-out from the account modal), call `setSessionExpired(true)`
- Source: `App.tsx`:179-213 — `onAuthStateChange` handler block
- Verify: Simulated token expiry (clear token mid-session) triggers `sessionExpired = true`; user-initiated sign-out does not

### Step 5 — Pass sessionExpired to AuthScreen and render banner
- Action: Pass `sessionExpired` prop to `AuthScreen` component; in `AuthScreen`, if `sessionExpired` is true, render an amber `View` banner above the sign-in form with text: "Your session expired. Please sign in again." styled with amber background (`#FFA500` or design token equivalent), white text, padding 12
- Source: `src/components/auth/AuthScreen.tsx` — prop interface and JSX render
- Verify: With `sessionExpired = true`, amber banner appears above sign-in fields; with `false`, no banner rendered

### Step 6 — Clear sessionExpired on successful sign-in
- Action: In the `onAuthStateChange` handler — if `event === 'SIGNED_IN'`, call `setSessionExpired(false)`
- Source: `App.tsx`:179-213 — `SIGNED_IN` event branch
- Verify: After successful sign-in, banner does not reappear on subsequent AuthScreen visits

### Step 7 — Simulate and verify
- Action: Manually clear `supabase.auth.token` (or equivalent SecureStore key) from device storage; relaunch app; confirm amber banner appears on AuthScreen. Run `npm run verify`
- Source: `App.tsx` — full auth flow under test
- Verify: Banner appears after simulated expiry; `npm run verify` exits 0

## Out of Scope
- Token refresh mechanism changes (proactive refresh, silent refresh)
- SecureStore migration (see PLAN-fix-auth-tokens-secure-store)
- Email/password reset flow
- Any BLE or device layer changes
