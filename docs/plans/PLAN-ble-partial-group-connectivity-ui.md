# Partial Group Connectivity UI

Provides progressive disclosure of group health for Bluetooth connections. When a group of skates is partially connected, the app will no longer silently ghost the missing devices. Instead, it will display a row of colored roller skate icons in the DockedController header to visually represent the status of each device in the group.

## User Review Required

> [!IMPORTANT]
> The user explicitly rejected the "Skip" button. The backend `writeToDevice` function already implicitly skips disconnected devices, so no logical change to the write path is necessary. The focus is entirely on UI visibility and providing a fast path to reconnect a dropped device.
> 
> Also, because `DockedController.tsx` is large, we must ensure we extract or inject the header component carefully without causing excessive re-renders.

## Open Questions

> [!WARNING]
> Do we want to limit the number of skate icons displayed in the header if the group has more than 4 devices (e.g. `+3 more` badge), or do we expect groups to rarely exceed 4 devices?

## Proposed Changes

### `src/components/DockedController.tsx`

Summary of changes to the DockedController to render the health banner.

#### [MODIFY] [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- Extract or augment the Header component within the DockedController.
- Import the existing colored roller skate icon component (currently used in GroupCard/DeviceCard).
- Query the `activeGroup`'s devices and map them to a row of skate icons next to the title.
- Map the BLE connection state of each device to the icon's color:
  - **Green:** Connected
  - **Grey:** Disconnected
  - **Orange/Pulsing:** Reconnecting
- Attach an `onPress` handler to the grey icons that fires `reconnectDevice(device.mac)` natively.

## Verification Plan

### Manual Verification
- Group 4 devices. Turn off 1 device.
- Open the group in the app.
- Ensure the controller opens (instead of blocking), and the header shows 3 Green skates and 1 Grey skate.
- Verify that changing colors/patterns successfully writes to the 3 connected skates.
- Tap the Grey skate icon while the device is turned off. Verify it pulses Orange and returns to Grey.
- Turn on the missing device, tap the Grey skate icon again. Verify it pulses Orange and then turns Green, joining the active group.
