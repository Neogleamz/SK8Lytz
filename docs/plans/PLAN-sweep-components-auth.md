# Implementation Plan: BATCH:sweep-components-auth

## Proposed Changes

### Domain: components-auth

#### [MODIFY] src/components/auth/AuthFormForgotPassword.tsx
- Line 50 (R-04): Error logged without payload_size or ssi context

#### [MODIFY] src/components/auth/AuthFormSignIn.tsx
- Line 67 (R-04): Error logged without payload_size or ssi context
- Line 94 (R-04): Error logged without payload_size or ssi context
- Line 99 (R-04): Error logged without payload_size or ssi context
- Line 87 (R-24): AsyncStorage key collision for undocumented key '@Sk8lytz_remember_creds'. Key is duplicated across 3 files (AuthFormSignIn.tsx, DevSandboxDrawer.tsx, AuthScreen.tsx).

#### [MODIFY] src/components/auth/AuthFormSignUp.tsx
- Line 101 (R-04): Error logged without payload_size or ssi context
- Line 127 (R-04): Error logged without payload_size or ssi context

#### [MODIFY] src/components/auth/DevSandboxDrawer.tsx
- Line 97 (R-15): Direct supabase.auth.signOut() bypasses AuthContext's signOut method.
- Line 16 (R-18): Single boolean state which may need FSM refactoring if complexity grows
- Line 19 (R-24): AsyncStorage key collision for undocumented key '@Sk8lytz_demo_mode'. Key is duplicated across 3 files (DevSandboxDrawer.tsx, useBLE.ts, ConnectService.ts).
- Line 100 (R-24): AsyncStorage key collision for undocumented key '@Sk8lytz_auth_last_email'. Key is duplicated across 3 files (DevSandboxDrawer.tsx, AuthContext.tsx, AuthScreen.tsx).

#### [MODIFY] C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\components\auth\AuthFormSignUp.tsx
- Line 123 (R-16): Hardcoded delay using setTimeout detected

#### [MODIFY] src/components/auth/AuthFooterActions.tsx
- Line 31 (R-24): AsyncStorage key collision for undocumented key '@Sk8lytz_offline_skip'. Key is duplicated across 3 files (AuthFooterActions.tsx, AuthFormSignIn.tsx, AuthContext.tsx).
