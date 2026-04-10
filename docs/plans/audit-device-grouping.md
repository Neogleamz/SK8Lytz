# Feature Implementation Plan: audit-device-grouping

## Target
`epic/device-management` -> `audit-device-grouping`

### Design Decisions & Rationale
We discovered that when a custom group is deleted, it is removed from `ng_custom_groups`, but the individual devices within that group still retain `groupId` and `grouped: true` in their `ng_device_configs` cache. This out-of-sync cache later resurrects the group visually in the UI and causes ghost grouping. Furthermore, the Supabase-backed `useRegistration` hook saves the `group_name`, which is never cleared upon local group deletion. 

To resolve this, we will enhance `handleGroupDelete` and `saveGroup` in `DashboardScreen.tsx` to automatically scrub `groupId` from `deviceConfigs` and update `registeredDevices` with a `null` group name, ensuring data consistency across all layers of state persistence.

## Proposed Changes

### [MODIFY] `DashboardScreen.tsx`
- **`handleGroupDelete`**: Capture the group being deleted. After removing it from `customGroups`, iterate through the `groupToDelete.deviceIds`. For each device, clear `groupId`, `groupName`, and `grouped` from `ng_device_configs` and `setDeviceConfigs`. Also call `saveRegisteredDevice` to set `group_name: null` for cloud consistency.
- **`saveGroup`**: When devices are added to or removed from a group during creation or renaming, ensure their `ng_device_configs` and `registeredDevices` are synchronously updated with the correct `groupName` or cleared.

### UI & Platform Strategy
These changes solely affect AsyncStorage and React state mutations without requiring additional native bridges. The fix inherently supports both iOS and Android natively.
