### Design Decisions & Rationale
We are implementing a client-side database reconciliation strategy in `ProfileService.ts` to actively synchronize the Supabase Auth `user_metadata.username` field into the `user_profiles` table upon session initialization. This provides immediate resilience against database trigger delays/failures and features a self-healing patch that checks existing rows with a `null` display name, patching them on the fly. 

## Proposed Changes

### `src/services/ProfileService.ts`
- **[MODIFY]** `fetchOrCreateProfile`
  - Modify the `existing` profile return branch to inspect for a missing `display_name` or `username`. If `user_metadata.username` exists in the auth session payload, autonomously execute a targeted SQL `update` to inject the missing fields into `user_profiles`.
  - Update the fallback insertion logic for brand new users to pull directly from `user.user_metadata?.username` rather than immediately falling back to stripping the email suffix, and properly save the normalized `username` to the database payload.

## Open Questions
> [!WARNING]
> Is it acceptable to automatically lowercase the `username` field as a standard form of canonicalization for database storage while simultaneously passing the raw `username` value string into the `display_name` column to preserve case-sensitivity?

## Verification Plan

### Automated Tests
- Run `npx tsc --noEmit` to verify type safety when accessing `user_metadata` and updating `user_profiles`.

### Manual Verification
1. Sign up a new user and ensure they hit the dashboard. Validate via `console.log` (or Supabase remote inspection) that their `display_name` matches the authored text, rather than extracting their email address suffix.
2. Sign in as an affected user that currently has `null` display data. Verify the dashboard safely initializes and self-heals their session data without a secondary modal prompt.
