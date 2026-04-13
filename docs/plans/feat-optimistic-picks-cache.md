# feat/optimistic-picks-cache

Implement 'Stale-While-Revalidate' (SWR) caching for SK8Lytz Picks and optimize Favorites loading to eliminate UI stutters in the DockedController.

## Design Decisions & Rationale

- **Instant UI via Local Cache**: SK8Lytz Picks are currently 100% network-dependent. I will implement an `AsyncStorage` cache (`@Sk8lytz_Picks_Cache`) to allow the UI to render picks immediately on mount using the last-fetched data.
- **SWR (Stale-While-Revalidate)**: The application will display the cached data instantly while simultaneously firing a background request to Supabase. If the remote data differs from the cache, the UI will update silently, and the cache will be refreshed.
- **Flicker Reduction**: Favorites (local) are currently loaded via a `useEffect` which causes a frames-long "empty" state. I will optimize the loading sequence to ensure that if data exists in memory or can be read quickly, it populates the UI before the user notices a gap.
- **Data Integrity**: I will include a version/timestamp check to ensure the cache doesn't grow stale indefinitely or conflict with schema updates.

## Proposed Changes

### [DockedController](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)

#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- Implement `AsyncStorage` reading for Picks during the initialization `useEffect`.
- Modify `fetchPicks` to compare remote data with the cached version before updating state (to prevent unnecessary re-renders).
- Persist successfully fetched Picks to `@Sk8lytz_Picks_Cache`.
- Ensure Favorites loading from `@Sk8lytz_Favorites` happens in parallel with Picks initialization to minimize mount layout shifts.

## Verification Plan

### Automated Tests
- N/A (UI-centric behavior)

### Manual Verification
1. Open the app (first run): Verify Picks load from Supabase and show the loading state.
2. Close and re-open the app: Verify Picks appear **instantly** without a visible loading spinner.
3. Modify a Pick in the Admin Dashboard: Open the app and verify it shows the "old" Pick briefly before "silently" updating to the new one once the network request completes.
4. Verify Favorites still persist and load correctly after the optimization.
