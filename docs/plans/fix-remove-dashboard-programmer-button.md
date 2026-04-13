# Remove Dashboard Programmer Button

This plan resolves the user's request to remove the `Batch Programmer` shortcut icon from the top of the `DashboardScreen`. The button currently resides next to the Support and Theme toggles. Because the user intends to consolidate features and explicitly denied this button on the dashboard, we will remove it from the header.

## Proposed Changes

### `src/screens/DashboardScreen.tsx`

#### [MODIFY] DashboardScreen.tsx

- Locate the `<TouchableOpacity>` linking to `setIsProgrammerVisible` in the header (around line 1420).
- Delete the `developer-board` icon shortcut from the `RIGHT: grouped icons` section of the dashboard header.
- _Note:_ The actual `Sk8LytzProgrammerModal` component and `isProgrammerVisible` state will remain in the file because it is still triggered by the Device Settings modal (`onOpenProgrammer()`), preserving the underlying functionality while just removing the redundant visual clutter from the dashboard header.

## Design Decisions & Rationale

We are removing the global dashboard entry point to the Programmer Modal because it clutters the UI and the feature is currently better served contextually when viewing a specific device or group setting. The overarching philosophy of "glanceable UI" requires us to prune buttons that aren't strictly daily drivers.

## Verification Plan

We will delete the button and ensure the TypeScript compiler (`npx tsc`) is clean, avoiding any orphaned variables. We will then ask the user to verify the cleaner dashboard header is aligned with their expectations.
