### Design Decisions & Rationale

The root cause of the Hardware Setup loop is that the `hasCloudRegistrations()` function immediately forces a network request and explicitly returns `false` if the user is offline or not signed in—totally ignoring locally cached devices in `AsyncStorage`. We will rename this function to `hasAnyRegistrations()` and adjust its flow to check the local (instant) cache first. If devices exist locally, the setup wizard will be skipped, ensuring seamless offline and persistent logins.

## Proposed Changes

### `src/hooks/useRegistration.ts`

- Rename `hasCloudRegistrations` to `hasAnyRegistrations`.
- Adjust the internal logic to:
  1. **Fast Path**: Check `getLocalDevices()`. If `length > 0`, return `true` instantly.
  2. **Network Path**: If local misses, perform the `supabase` count check.
  3. Ensure it degrades gracefully to `false` without throwing an uncaught error.

### `src/screens/DashboardScreen.tsx`

- Update the destructured import from `useRegistration` to use `hasAnyRegistrations`.
- Update the `useEffect` that checks the FTUE state on mount to consume this new function name.

## Verfication Plan

- Log out or log into the app without network. 
- Force quit and reopen. 
- Ensure the app directly loads the Dashboard instead of forcing the Hardware Setup Wizard if devices exist in local storage.
