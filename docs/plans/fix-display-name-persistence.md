# Fix Display Name Persistence

Fix the `display_name` persistence issue where new users signing up are not having their display name properly propagated from Supabase Auth metadata into the `user_profiles` table.

### Design Decisions & Rationale
We currently capture `username` on the signup screen but only pass `{ username }` in the Auth metadata. The Postgres database trigger relies heavily on `display_name` to populate the `user_profiles.display_name` column securely. This fix injects `display_name` into the `supabase.auth.signUp()` payload directly and hardens the `ProfileService` self-healing logic to pull from both `meta.username` and `meta.display_name`.

## User Review Required
No major architectural changes or breaking risks are introduced. This only patches metadata flow on signup and enhances the profile fetch healing process.

## Proposed Changes

### Frontend / Auth Components
#### [MODIFY] [AuthScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/AuthScreen.tsx)
- Update the `handleSignUp` method's `supabase.auth.signUp` payload.
- Inject `display_name: username.trim()` into `options.data` alongside the `username` field to ensure triggers properly pick it up upon insertion.

### Service Layer
#### [MODIFY] [ProfileService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ProfileService.ts)
- Update the `fetchOrCreateProfile` auto-healing block.
- Read `user.user_metadata?.display_name` in addition to `username`.
- Apply robust fallback defaulting if either is missing in the `user_profiles` database, ensuring we attempt to pull `display_name` first before defaulting to `username`.

## Verification Plan

### Automated Tests
- Run `npx tsc --noEmit` to ensure no typing breaking changes.

### Manual Verification
- The user can run standard signup in Sandbox vs Local testing, ensuring that `display_name` does not appear as `null` and propagates immediately to the User Profile view dashboard.
