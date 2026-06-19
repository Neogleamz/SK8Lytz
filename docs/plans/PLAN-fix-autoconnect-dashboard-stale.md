# Implementation Plan — Fix AutoConnect Dashboard Stale Data

> **Slug:** `fix-autoconnect-dashboard-stale`
> **Batch:** connection-pipeline-fix
> **Wave:** 1
> **Risk:** M-RISK
> **Size:** Snack
> **Cognitive Load:** Low

---

## Problem Statement

`useDashboardAutoConnect` has a MAC case mismatch that silently prevents cloud-synced devices from auto-connecting, and `DashboardScreen` has a stale `allDevicesRef` and an incorrect lookup key in `renderItem` that causes device card data mismatches.

### Audit Findings Covered

| ID | Severity | Summary | File | Lines |
|----|----------|---------|------|-------|
| **H6** | HIGH | Cloud sync MAC case mismatch — `d.device_mac` not normalized to uppercase | [useDashboardAutoConnect.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L396-L405) | 396, 402–405 |
| **M1** | MEDIUM | `allDevicesRef` in DashboardScreen is never synced after init | [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx#L200) | 200 |
| **L3** | LOW | Cloud sync doesn't trigger observer re-fire (ref mutation ≠ re-render) | [useDashboardAutoConnect.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L401-L418) | 401–418 |
| **L4** | LOW | `renderItem` uses `item.id` (composite key) instead of MAC for `deviceConfigs` lookup | [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx#L892) | 892 |

---

## Files to Create/Modify

| File | Action | Reason |
|------|--------|--------|
| `src/hooks/useDashboardAutoConnect.ts` | MODIFY | Fix H6 (normalize MACs), L3 (force observer re-fire) |
| `src/screens/DashboardScreen.tsx` | MODIFY | Fix M1 (sync allDevicesRef), L4 (renderItem MAC lookup) |

---

## Step-by-Step Fix

### Step 1: Normalize cloud sync MACs to `.toUpperCase()` (H6)

**Source:** [useDashboardAutoConnect.ts L396](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L396) — The cloud sync path builds `cloudMacSet` from raw Supabase data:
```typescript
cloudMacSet.add(d.device_mac || d.id);
```
This does NOT normalize to uppercase. Meanwhile, `buildOfflineGroupMap` at [L93–94](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L93-L94) normalizes with `.toUpperCase()`. The observer at [L170–172](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L170-L172) matches `autoConnectIdsRef.current.includes(d.id)` — BLE `Device.id` is uppercase on Android. If Supabase stores lowercase, the `includes` check fails silently.

**Change at L396:**
```diff
-                    cloudMacSet.add(d.device_mac || d.id);
+                    cloudMacSet.add((d.device_mac || d.id).toUpperCase());
```

**Also at L405** — the `currentSet.has(id)` comparison uses the raw `cloudIds` value:
```diff
                  for (const id of cloudIds) {
-                    if (!currentSet.has(id)) {
-                      currentSet.add(id);
+                    const normalizedId = id.toUpperCase();
+                    if (!currentSet.has(normalizedId)) {
+                      currentSet.add(normalizedId);
```

**Verify:** Cloud-synced devices with lowercase MACs in Supabase now correctly match BLE `Device.id` and trigger auto-connection.

---

### Step 2: Add `useEffect` to sync `allDevicesRef.current` on `allDevices` change (M1)

**Source:** [DashboardScreen.tsx L200](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx#L200) — `allDevicesRef` is initialized with `useRef(allDevices)` but never updated:
```typescript
const allDevicesRef = React.useRef(allDevices);
```

The ref value stays frozen at the initial render's `allDevices`. Downstream consumers (like `useDashboardController` via `allDevicesRef` prop at [L846](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx#L846)) read stale data.

> **Note:** `useBLE.ts` has its own `allDevicesRef` at [L148](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L148) which IS properly synced at [L290–292](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L290-L292). This fix is for the SEPARATE ref declared in DashboardScreen.

**Change:** Add a sync `useEffect` immediately after the ref declaration (near L200):
```typescript
const allDevicesRef = React.useRef(allDevices);
useEffect(() => {
  allDevicesRef.current = allDevices;
}, [allDevices]);
```

**Verify:** `allDevicesRef.current` always reflects the latest `allDevices` state. Log `allDevicesRef.current.length` in `useDashboardController` and confirm it updates when new devices are discovered.

---

### Step 3: Fix `renderItem` `deviceConfigs` lookup to use MAC instead of `item.id` (L4)

**Source:** [DashboardScreen.tsx L892](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx#L892) — The `renderItem` callback looks up config with `item.id`:
```typescript
const cachedConfig = (deviceConfigs[item.id as string] || {}) as Partial<DeviceSettings>;
```

`RegisteredDevice.id` is a Supabase composite key (format: `MAC_ADDRESS::USER_ID`), NOT a BLE MAC address. `deviceConfigs` is keyed by uppercase MAC. The lookup always misses, returning `{}`.

Meanwhile, `mac` is correctly derived just one line above at [L891](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx#L891):
```typescript
const mac = (item.device_mac || item.id || '').toUpperCase();
```

**Change at L892:**
```diff
-    const cachedConfig = (deviceConfigs[item.id as string] || {}) as Partial<DeviceSettings>;
+    const cachedConfig = (deviceConfigs[mac] || {}) as Partial<DeviceSettings>;
```

**Verify:** Device cards in the Registered Fleet slab now display correct names, types, and settings from `deviceConfigs`.

---

### Step 4: Force observer re-fire after cloud sync (L3)

**Source:** [useDashboardAutoConnect.ts L401–418](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L401-L418) — When cloud sync adds new MACs to `autoConnectIdsRef.current`, it mutates a ref. The observer `useEffect` at [L166–282](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L166-L282) depends on `[allDevices, connectedDevices, isWizardActive]`, NOT on the ref. If no new BLE devices appear after the cloud sync, the observer never re-evaluates the updated ref.

**Change:** After mutating `autoConnectIdsRef.current` with cloud MACs, fire a burst scan to force new `allDevices` entries and trigger the observer:
```diff
                  if (addedNew) {
                    autoConnectIdsRef.current = Array.from(currentSet);
                    AppLogger.log('BLE_STATE_CHANGE', {
                      event: 'auto_connect_cloud_synced',
                      count: autoConnectIdsRef.current.length,
                      payload_size: 0,
                      ssi: 0,
                    });
+                   // L3 FIX: Ref mutation doesn't trigger React re-render.
+                   // Fire a burst scan so new devices flow through allDevices → observer.
+                   if (burstScan) {
+                     try {
+                       const scanResult = burstScan(5000);
+                       if (scanResult && typeof scanResult.catch === 'function') {
+                         scanResult.catch(() => {});
+                       }
+                     } catch { /* non-critical */ }
+                   }
                  }
```

**Verify:** Cloud-only MACs that are already physically nearby get connected after the burst scan fires.

---

## Out of Scope

- **BleMachine.ts** — state machine fixes are in Plan 1
- **ConnectService.ts** — internal fixes are in Plan 2
- **RecoveryService.ts** — internal fixes are in Plan 2
- **useBLE.ts** — wiring changes are in Plans 1 and 2
- **L5 (edgePanResponder stale closures)** — cosmetic, deferred
- **L6 (background notification default group)** — edge case, deferred

---

## Verification Matrix

| Step | What to verify | Method |
|------|----------------|--------|
| 1 | Lowercase MAC from Supabase matches BLE Device.id and triggers auto-connect | Manual test: insert lowercase MAC in Supabase → observe auto-connect |
| 2 | `allDevicesRef.current.length` increases when new devices are scanned | Add temporary log, verify in dev mode |
| 3 | Device cards show correct config from `deviceConfigs` map | Visual inspection in Registered Fleet slab |
| 4 | Cloud-synced MACs trigger burst scan → observer matches → auto-connect | Log audit: `auto_connect_cloud_synced` followed by `auto_connect_observer` |
