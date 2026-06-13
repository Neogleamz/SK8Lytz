# Account Devices Management

This plan outlines the architecture for the "Registered Device Groups" feature within the `AccountModal`, addressing the bucket list requirement to accurately group, edit, and purge devices across both Supabase and local `AsyncStorage`.

## Design Decisions & Rationale

- **Single Truth for Deletions:** Currently, `AccountModal` performs direct `supabase.from('registered_devices').delete()` calls, which skips the local `AsyncStorage` cleanup managed by the `useRegistration` hook. This causes offline devices or disconnected devices to instantly reappear on reboot. We will refactor this to route all deletions strictly through `useRegistration`'s proven `deregisterDevice` method via `DashboardScreen` props.
- **Group-First Hierarchy:** The `Devices` tab inside `AccountModal` will be completely overhauled. Instead of a flat list, devices will be aggregated by their `group_name`. We will render high-level Group Cards that allow users to rename or forget the _entire group_ at once, with child elements for individual devices underneath them.
- **Data Model:** Since `group_name` inherently defines the group logic (and is parsed into `customName`), renaming the group propagates to all children.

## Proposed Changes

### `src/screens/DashboardScreen.tsx`

#### [MODIFY] `DashboardScreen.tsx`

We will destructure the lifecycle methods from the existing `useRegistration` hook and implement group-level management actions that pass down into `AccountModal`.

- Extract `saveRegisteredDevice` and `deregisterDevice` from `useRegistration()`.
- **Modify `onDeviceRenamed`:** Call `saveRegisteredDevice({ ...dev, device_name: newName })` so that individual hardware renames sync properly.
- **Modify `onDeviceForgotten`:** Invoke `await deregisterDevice(deviceId)` so both cloud and `AsyncStorage` purges happen gracefully.
- **Inject New Props:**
  - `onGroupRenamed`: Loops through all `registeredDevices` matching the group name and invokes `saveRegisteredDevice` with the updated group string.
  - `onGroupForgotten`: Loops through all devices in the group and calls `deregisterDevice`.

### `src/components/AccountModal.tsx`

#### [MODIFY] `AccountModal.tsx`

Refactor the "DEVICES" tab rendering logic (`renderDevices` function).

- **Remove Raw Supabase Deletions:** Delete the `supabase.from(...).delete()` logic from `handleForgetDevice` to respect the hook boundary.
- **Data Aggregation:** Write a `useMemo` block that aggregates `registeredDevices` into groups based on their `customName` (mapped from `group_name`).
- **Expand the UI:**
  - **Group Card:** Renders the group's name, a count of active child devices, and group-level actions ("Rename Group" / "Forget Group").
  - **Expand/Collapse or Flat Child List:** Render the child devices indented underneath their respective group card.
  - **Child Actions:** Keep the single-device Forget action to allow picking off individual broken devices from a group.

## UI Craftsmanship & Platform Strategy

- **4-State Matrix:**
  - **Empty:** Continue utilizing the existing visual empty state `MaterialCommunityIcons name="bluetooth-off"`.
  - **Success:** Expand the aesthetic using soft-rounded container blocks to visually distinctify groups vs nested children.
- **Responsiveness:** All margins and padding will inherently use an 8-point scaling system (`marginVertical: 8`, `gap: 16`) to maintain bounds on iOS/Android.

## Verification Plan

### Manual Verification

1. Open the Account Modal and tap the "Devices" tab.
2. Verify devices properly stack underneath their shared `Group Name`.
3. Press "Rename" on a Group and ensure renaming cascade updates all nested devices instantaneously.
4. Press "Forget Group" while offline (disable internet) and verify the group completely disappears from both the UI. Follow up by restarting the app (simulating `AsyncStorage` boot) and ensuring the ghost devices do not resurrect.
