# Implementation Plan: feat/smart-group-health

## Problem
When 1 of N devices in a group drops, the whole group appears broken. No per-device health indicator, no degraded mode, no way for users to see WHICH skate is the problem.

Source: [SkateGroupCard.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/SkateGroupCard.tsx#L51-L56) — group state is derived from `powerStates` (on/off) but has no concept of per-device connection state. [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx) dispatches BLE commands to ALL group devices without checking which ones are actually connected.

Industry standard: Show individual device status within the group. Allow group to operate in "degraded mode" — send commands to N-1 connected devices while the Nth reconnects.

## Files to Create/Modify

#### [MODIFY] [SkateGroupCard.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/SkateGroupCard.tsx)
- Accept `connectionStates?: Record<string, DeviceConnectionState>` prop (from PLAN-feat-connection-state-badges)
- Show per-device connection dot (green/red/yellow) next to each RSSI meter (L107-L124)
- Add degraded mode banner: "1/2 skates connected — Left reconnecting..." when partial group
- Visual: red dot on disconnected device's skate icon, pulsing yellow for reconnecting

#### [MODIFY] [MySkatesSlab.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/MySkatesSlab.tsx)
- Pass `connectionStates` map to SkateGroupCard
- Build the map from connectedDevices array

#### [MODIFY] [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- Modify group command dispatch to filter to connected-only devices
- When dispatching a BLE command to a group, check each device's connection state and only send to connected devices
- Build and pass the `connectionStates` map down to MySkatesSlab

#### [MODIFY] [dashboard.types.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/types/dashboard.types.ts)
- Extend `CustomGroup` or add `GroupHealthSummary` type with per-device connection state, last-seen timestamp

## Steps

### Step 1: Add per-device connection dot to SkateGroupCard
- Next to each RSSI bar row (L107-L124), add a 6px colored dot
- Green = connected, Red = disconnected, Yellow pulse = reconnecting
- Source: SkateGroupCard.tsx L107-L124
- Verify: Visual test — group card shows per-device dots

### Step 2: Add degraded mode banner
- When `connectedCount < total && connectedCount > 0`, show "1/2 skates connected"
- Identify which device is down by name from the group config
- Verify: Banner appears when 1 device is manually disconnected

### Step 3: Filter group command dispatch to connected-only
- Source: DashboardScreen.tsx — group tap handler
- Before sending BLE command, filter `group.deviceIds` to only include MACs in `connectedDevices`
- Verify: Disconnected device doesn't receive commands, connected devices still work

### Step 4: Add last-seen tracking
- Track `lastSeen: Record<string, number>` per device (timestamp of last successful GATT interaction)
- Show relative time on disconnected devices: "Last seen 2m ago"
- Verify: Timestamp updates on BLE activity

## Out of Scope
- BleMachine internals — state transitions stay as-is
- ConnectService — connection logic stays as-is
- Background mode
- GATT operation queue
