# Goal Description

Fix the Supabase Auth Redirect issue where email confirmation links point to a dead `localhost` address. We will configure real deep linking so that tapping the verification link seamlessly opens the Sk8Lytz app and authenticates the user on both iOS and Android.

### Design Decisions & Rationale

We are using deep linking with the custom `sk8lytz://` scheme and `expo-linking` since React Native relies on native intercept handlers for custom schemes, and Supabase specifically emits standard hash-parameter URLs after token verification. This avoids the need for heavy external libraries and leverages our existing single-entry `App.tsx` context correctly.

### UI & Platform Strategy

The scheme registration in `app.json` will ensure both the iOS intent system and Android manifest filter natively handle `sk8lytz://`. Since this is a background intercept, no new UI is required—the app simply swallows the token and logs the user directly into the Dashboard.

## Proposed Changes

### Configuration

#### [MODIFY] `app.json`

- Add the `"scheme": "sk8lytz"` property under the `"expo"` block to register our custom URL scheme natively for both platforms.

### Frontend Application

#### [MODIFY] `src/screens/AuthScreen.tsx`

- Ensure the `supabase.auth.signUp()` call explicitly sets `options: { emailRedirectTo: 'sk8lytz://auth' }` (similar to the Magic Link logic) to command Supabase to redirect back to the app after verification.

#### [MODIFY] `App.tsx`

- Import `* as Linking from 'expo-linking'`.
- Implement a deep link handler inside `AppContent`'s `useEffect`.
- Intercept incoming links using `Linking.getInitialURL()` (for cold starts) and `Linking.addEventListener('url', ...)` (for foreground/background wake).
- Parse the `access_token` and `refresh_token` from the URL hash fragments.
- Call `supabase.auth.setSession({ access_token, refresh_token })` to finalize the session seamlessly.

## User Action Required

> [!IMPORTANT]
> You must manually add `sk8lytz://**` to your Supabase project's **Additional Redirect URLs**.
>
> 1. Go to your Supabase Dashboard.
> 2. Navigate to **Authentication > URL Configuration**.
> 3. Under **Redirect URLs**, click "Add URL" and enter `sk8lytz://**`.

## Verification Plan

### Automated Tests

- Syntax validation on `app.json` and React code.

### Manual Verification

- Attempt a brand-new user sign-up via the UI.
- Open the email on the test device.
- Tap the verification link.
- Witness the app foreground dynamically and log in automatically without any "Invalid token" or white-screen errors.
