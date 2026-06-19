# Implementation Plan: feat/connection-state-badges

## Problem
We have live RSSI signal bars on device cards ([DeviceItem.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DeviceItem.tsx#L96-L115)) and group cards ([SkateGroupCard.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/SkateGroupCard.tsx#L107-L124)), but NO connection state indicator. Users see "connected" or nothing — no visual for "Connecting...", "Reconnecting...", or "Out of Range".

Industry standard (Govee, Hue): explicit connection state labels with color coding per device.

## Files to Create/Modify

#### [NEW] [ConnectionStateBadge.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/ConnectionStateBadge.tsx)
- New component showing connection state as a colored dot + label
- States: 🟢 Connected | 🟡 Connecting | 🔵 Reconnecting | 🔴 Disconnected | ⚪ Out of Range
- Props: `state: DeviceConnectionState` (from dashboard.types.ts)

#### [MODIFY] [dashboard.types.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/types/dashboard.types.ts)
- Add `DeviceConnectionState` union type: `'connected' | 'connecting' | 'reconnecting' | 'disconnected' | 'out_of_range'`

#### [MODIFY] [SkateGroupCard.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/SkateGroupCard.tsx)
- Accept `connectionStates?: Record<string, DeviceConnectionState>` prop
- Show per-device state dot next to each RSSI meter (L107-L124)

#### [MODIFY] [DeviceItem.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DeviceItem.tsx)
- Accept `connectionState?: DeviceConnectionState` prop
- Show state badge next to RSSI icon area (L96-L115)

#### [MODIFY] [MySkatesSlab.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/MySkatesSlab.tsx)
- Pass `connectionStates` map down to SkateGroupCard (map from connectedDevices + bleState)

#### [MODIFY] [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- Build `connectionStates` map from BLE machine state + connectedDevices
- Pass to MySkatesSlab and DeviceItem

## Steps

### Step 1: Add DeviceConnectionState type
- Source: dashboard.types.ts
- Verify: TSC clean

### Step 2: Create ConnectionStateBadge component
- Colored dot (6px circle) + tiny label text
- Verify: Component renders in isolation

### Step 3: Wire into SkateGroupCard per-device RSSI section
- Source: SkateGroupCard.tsx L107-L124
- Verify: Group cards show state dots

### Step 4: Wire into DeviceItem
- Source: DeviceItem.tsx L96-L115
- Verify: Device cards show state badge

### Step 5: Build connectionStates map in DashboardScreen
- Derive from BLE machine state + connectedDevices array
- Pass down through MySkatesSlab → SkateGroupCard

## Out of Scope
- BleWriteQueue / GATT operation queue
- ConnectService internals
- Background mode
- Toast/hint for "Move closer" (deferred to smart group health)
