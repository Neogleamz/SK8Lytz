# Supabase Auth Reconciliation: Removing Plaintext Local Passwords

The initial audit of the authentication flow revealed a critical architectural security flaw: the application is currently mixing standard Supabase JWT session management with a homegrown "remember me" logic that saves the user's raw, plaintext password into `AsyncStorage`.

On every app launch, `App.tsx` attempts to read this plaintext password and perform a manual `signInWithPassword()` API call over the network to recreate the session. Supabase natively persists the JWT securely via the `persistSession: true` configuration in `supabaseClient.ts` and manages its own refresh lifecycle, making this manual plaintext storage highly vulnerable and completely redundant.

### Design Decisions & Rationale

To align with standard and secure Supabase Auth patterns, we will rip out all homegrown, plaintext password storage logic. From now on, `App.tsx` will rely exclusively on the built-in `supabase.auth.getSession()` and `onAuthStateChange` listeners to detect and restore authenticated JWT sessions. We will refactor the UI's "Remember me" functionality so that it serves only as a convenience feature to pre-fill the user's email address if they get logged out, ensuring passwords are never stored on the device.

### Proposed Changes

#### [MODIFY] `App.tsx`

- Remove the block in the initialization sequence (`App.init()`) that attempts to manually read `@Sk8lytz_remember_creds` and execute `supabase.auth.signInWithPassword`.
- Retain the `supabase.auth.getSession()` block as the sole truth for session restoration.

#### [MODIFY] `src/screens/AuthScreen.tsx`

- Change the `STORAGE_REMEMBER_CREDS` type definition and comments to reflect it only stores `{ email, rememberMe }`.
- Modify `handleSignIn()` to stop writing the user's `password` into `AsyncStorage`.
- Ensure the initialization `useEffect` no longer attempts to auto-populate the password field.

### Open Questions

None. The native Supabase Auth methods will smoothly handle users who had a previously valid session.

### Verification Plan

**Automated Tests**:

- npx tsc to verify TypeScript is sound after removing the `password` property.

**Manual Verification**:

1. Log into the app, checking the "Remember me" box.
2. Force quit the app and reopen it.
3. Verify that the app bypasses the Auth Screen and directly enters the Dashboard, smoothly restoring the JWT session without needing a password.
4. Log out manually. Verify that the Auth Screen correctly remembers and pre-fills the email address, but the password field is left completely blank.
