# Implementation Plan: BATCH:sweep-context

## Proposed Changes

### Domain: context

#### [MODIFY] src/context/AuthContext.tsx
- Line 208 (R-11): Missing try/catch on async network operations (signIn, signUp, resetPassword calls to Supabase)
- Line 235 (R-04): Error logged without payload_size or ssi context
- Line 210 (R-11): Missing try/catch on async network/storage operation
- Line 220 (R-11): Missing try/catch on async network/storage operation
- Line 226 (R-11): Missing try/catch on async network/storage operation

#### [MODIFY] src/context/SessionContext.tsx
- Line 332 (R-22): Async race condition causing setInterval leak. setupNotification is an async function. If the effect unmounts during an await (e.g., displayNotification), the cleanup runs while updateInterval is null. The 5-second interval is then created and runs forever.
- Line 204 (R-22): Async race condition in checkAutoPause. If the effect unmounts during await AsyncStorage.getItem, the cleanup function runs while timer is null. The setTimeout is then created and can fire 10s later, erroneously changing sessionPhase after the session is stopped.
- Line 223 (R-04): Error logged without payload_size or ssi context
- Line 322 (R-04): Error logged without payload_size or ssi context
- Line 192 (R-24): AsyncStorage key collision for undocumented key '@Sk8lytz_auto_pause_enabled'. Key is duplicated across 2 files (SessionContext.tsx, useAccountOverview.ts).

#### [MODIFY] C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\context\SessionContext.tsx
- Line 55 (R-16): Hardcoded delay using setTimeout detected
- Line 186 (R-16): Hardcoded delay using setTimeout detected
- Line 204 (R-16): Hardcoded delay using setTimeout detected
- Line 416 (R-16): Hardcoded delay using setTimeout detected

#### [MODIFY] src/context/ThemeContext.tsx
- Line 26 (R-18): Single boolean state which may need FSM refactoring if complexity grows
