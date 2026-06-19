# Implementation Plan: fix/group-connect-stale-devices

> **Priority:** P1 | **Risk:** H-RISK | **Size:** Meal | **Layer:** BLE | **Domain:** [BLE]

## Problem

Three interacting bugs cause group connections to fail and device card taps to feel "stale":

1. **`seenMacsRef` one-shot gate** in `useBLEScanner.ts` prevents device objects in `allDevices` from refreshing after first scan. After 15 seconds, the staleness pruner removes them, but the scanner never re-stages them — so `allDevices.filter(...)` at group-tap time returns 0 or 1 devices instead of 2.

2. **MAC case mismatch** in `buildOfflineGroupMap` at `useDashboardAutoConnect.ts:93` — pushes `d.device_mac` without `.toUpperCase()`, violating the uppercase invariant. Auto-connect observer at L171 does `autoConnectIdsRef.current.includes(d.id)` where `d.id` is uppercase on Android.

3. **Wizard `group_ids` slug vs `CustomGroup.id` timestamp mismatch** — Setup Wizard generates `group_ids: [fleetName.toLowerCase().replace(/\s+/g, '-')]` (e.g., `"my-skates"`) at `HardwareSetupWizardScreen.tsx:719`, but `useDashboardGroups.saveGroup` creates groups with `group-${Date.now()}` IDs. These never match, breaking group-to-device resolution.

### Prior Art (Already Merged)
- ✅ RC-07 single-group auto-connect fix — MERGED (Session Log 2026-06-xx). The current code already aggregates all groups.
- ✅ RC-02 drain-permanent fix — MERGED. Failed MACs now re-queue with jittered backoff.
- ✅ fix/stale-flush-group-kill — MERGED. ConnectService retains all connected devices during incremental assembly.

The bugs in this plan are the **remaining** issues not addressed by prior fixes.

## Files to Create/Modify

| # | File | Action | Change |
|---|------|--------|--------|
| 1 | `src/hooks/ble/useBLEScanner.ts` | MODIFY | Allow `seenMacsRef` guard to update `allDevices` with fresh `Device` objects for already-seen MACs |
| 2 | `src/hooks/useDashboardAutoConnect.ts` | MODIFY | Add `.toUpperCase()` to `buildOfflineGroupMap` at L93 |
| 3 | `src/screens/Onboarding/HardwareSetupWizardScreen.tsx` | MODIFY | Use group ID format consistent with `useDashboardGroups.saveGroup` |

## Proposed Changes

---

### Component 1: Scanner Device Freshness (Bug 1)

#### [MODIFY] [useBLEScanner.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts)

**Problem:** The `scanCallback` at L273 drops all re-discovered devices:
```typescript
// L273-275 — CURRENT (blocks all refreshes for seen MACs)
if (!seenMacsRef.current.has(mac)) {
    seenMacsRef.current.add(mac);
    // ... stages device, queues interrogation
}
```

**Fix:** Split the gate into two concerns:
1. **First-time discovery** (telemetry, interrogation, classification) → still gated by `seenMacsRef`
2. **Device object refresh** (update the `Device` handle in `allDevices`) → runs for ALL seen MACs

```typescript
// L270 onwards — PROPOSED
const mac = device.id.toUpperCase();
lastSeenRef.current.set(mac, Date.now());

if (!seenMacsRef.current.has(mac)) {
    seenMacsRef.current.add(mac);
    // ... first-time: telemetry, firmware parse, interrogation (unchanged)
    pendingStagedRef.current.push(device);
    scheduleFlush();
    queueDeviceForInterrogation(mac);
} else {
    // REFRESH: update the Device handle for already-known MACs
    // This ensures allDevices always has a fresh BLE peripheral reference
    pendingStagedRef.current.push(device);
    scheduleFlush();
}
```

The `flushStagedDevices` function at L204 already handles deduplication (`if (!merged.some(p => p.id === d.id))`), but we need to change this to an **upsert** pattern that replaces stale objects:

```typescript
// L204-214 — PROPOSED (replace identity merge with upsert)
setAllDevices(prev => {
    const live = prev.filter(d => {
        const lastSeen = lastSeenRef.current.get((d.id || '').toUpperCase());
        return lastSeen !== undefined && lastSeen > staleThreshold;
    });
    const merged = [...live];
    for (const d of staged) {
        const existingIdx = merged.findIndex(p => p.id === d.id);
        if (existingIdx >= 0) {
            merged[existingIdx] = d;  // UPSERT: replace stale Device object
        } else {
            merged.push(d);
        }
    }
    return merged;
});
```

**Verify:** After fix, `allDevices` should always contain fresh `Device` objects for any MAC seen in the last 15 seconds. Group card press → `allDevices.filter(...)` should find all group members.

**Source:** [useBLEScanner.ts L270-315](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts#L270-L315)

---

### Component 2: MAC Case Normalization (Bug 2)

#### [MODIFY] [useDashboardAutoConnect.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts)

**Problem:** `buildOfflineGroupMap` at L93 pushes `d.device_mac` as-is:
```typescript
// L93 — CURRENT
entry.deviceIds.push(d.device_mac);
```

**Fix:** Normalize to uppercase to match BLE `Device.id` convention:
```typescript
// L93 — PROPOSED
entry.deviceIds.push(d.device_mac.toUpperCase());
```

**Verify:** `autoConnectIdsRef.current` entries should always be uppercase. Observer at L171 `includes(d.id)` should match when `d.id` is uppercase (Android) or any case (iOS with toUpperCase normalization).

**Source:** [useDashboardAutoConnect.ts L93](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L93)

---

### Component 3: Wizard Group ID Consistency (Bug 5)

#### [MODIFY] [HardwareSetupWizardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)

**Problem:** The wizard generates `group_ids` at L719 using a slug format:
```typescript
// L719 — CURRENT
group_ids: [fleetName.toLowerCase().replace(/\s+/g, '-')],  // e.g., "my-skates"
```

But `useDashboardGroups.saveGroup` at L401 creates groups with timestamp IDs:
```typescript
// useDashboardGroups.ts L401
let finalGroupId = `group-${Date.now()}`;  // e.g., "group-1750309427000"
```

**Fix:** Generate the `group_ids` in the wizard using the same `group-${Date.now()}` format, or better — generate the group ID once and pass it to both `group_ids` on the device AND use it as the group key:
```typescript
// L710-726 — PROPOSED
const groupId = `group-${Date.now()}`;  // Generate canonical ID once

const finalizedDevices = selected.map(device => {
    const cfg = deviceConfigsState[device.device_mac];
    const fleetName = groupName.trim() || 'My Skates';
    return {
        ...device,
        group_names: [fleetName],
        device_name: cfg?.name.trim() || device.device_name,
        product_type: (cfg?.type || LOCAL_PRODUCT_CATALOG[0].id),
        position: cfg?.position || null,
        group_ids: [groupId],           // ← Use canonical ID, not slug
        led_points: cfg?.points || device.led_points,
        segments: device.segments ?? 1,
        ic_type: device.ic_type ?? 'WS2812B',
        color_sorting: device.color_sorting ?? 'GRB',
        s4_monolith_acknowledged: true
    };
});
```

**Verify:** After wizard completion, `registeredDevices[].group_ids[0]` should match the `CustomGroup.id` stored by `GroupRepository`. Both `useDashboardGroups` derivation and `buildOfflineGroupMap` should resolve the same group.

**Source:** [HardwareSetupWizardScreen.tsx L710-726](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx#L710-L726)

---

## Out of Scope

- `ConnectService.ts` — retained-device logic is correct per `fix/stale-flush-group-kill` merge
- `BleMachine.ts` — XState FSM transitions are working correctly
- Device card additive connection behavior (Bug 4 from audit) — requires UX decision
- `MySkatesSlab.tsx` `isActive` check (Bug 6 from audit) — cosmetic, low priority
- `DashboardScreen.tsx` config lookup key mismatch (Bug 3 from audit) — separate cosmetic fix

## Verification Plan

### Automated Tests
- `npm run verify` — TSC + Jest + AST + TypeSafety pass
- Unit test: `buildOfflineGroupMap` with mixed-case MACs → all output uppercase
- Unit test: `flushStagedDevices` with repeat MAC → `allDevices` contains fresh `Device` object

### Manual Verification
1. Cold start with 2 registered devices → both auto-connect within 5s
2. Tap group card after 30+ seconds idle → both devices connect
3. Complete Setup Wizard → `registeredDevices[].group_ids[0]` matches `customGroups[0].id`
4. App resume from background → both devices reconnect via `retriggerAutoConnect`
