# [PLAN] feat/eternal-glow-persistence (The Resume Standard)

### Design Decisions & Rationale

To ensure the app feels like an integrated hardware component, we are implementing **Session State Resumption**. The app will maintain a "Hot Cache" of the active lighting state. If the app is killed or the phone reboots, it will automatically "Re-attach" to the hardware and restore the UI to the last known active mode without user intervention.

## Proposed Changes

### [Component Name] Persistence Layer

#### [MODIFY] [ThemeContext.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/ThemeContext.tsx) (or a new PersistenceContext)

- Implement `last_active_state`: Storing `{ modeId, palette, speed, brightness, timestamp }`.
- Auto-save this state on every successful BLE dispatch (debounced).

### [Component Name] Lifecycle Management

#### [MODIFY] [App.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/App.tsx)

- On `useEffect` (mount), check if the `last_active_state` is less than 4 hours old.
- If valid, bypass the initial Dashboard state and trigger an "Auto-Connect & Restore" sequence.

## Verification Plan

1. **App Kill Test**: Set a specific pattern, force-close the app, and reopen. Verify the app immediately shows the active pattern in the DockedController.
2. **Offline Integrity**: Ensure the cache doesn't try to restore a cloud-based Pick if the device has no internet (fallback to local Favorites).
3. **Stale State Check**: Verify that states older than 4 hours are ignored to prevent "Ghost" sessions from confusing the user.
