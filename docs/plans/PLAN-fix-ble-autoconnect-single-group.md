# Implementation Plan: fix-ble-autoconnect-single-group

> **RC-07** | Priority: P3 | Risk: LOW | Size: Meal | Layer: BLE/Cloud

## Problem

`useDashboardAutoConnect` only auto-connects devices from the **most recently created group**. Users with devices across multiple groups find that only the newest group auto-connects on app launch — older groups require manual connection.

In [useDashboardAutoConnect.ts:270-282](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L270-L282) (cloud path):

```typescript
// L270-282 — sorts by created_at DESC, picks FIRST group only
presentGroups = groupsToProcess.sort(
  (a, b) => new Date(b.created_at).getTime() - new Date(a.created_at).getTime()
);
if (presentGroups.length > 0) {
  const targetGroupId = presentGroups[0].id;  // ← ONLY the newest group
  idsToConnect = (devices as RegisteredDeviceRow[])
    .filter((d) => {
      const dGroupIds: string[] = d.group_ids || (d.group_id ? [d.group_id] : []);
      return dGroupIds.includes(targetGroupId);  // ← filters to single group
    })
    .map((d) => d.device_mac || d.id);
}
```

And the identical pattern in the offline fallback at [L285-291](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L285-L291):

```typescript
// L285-291 — offline path, same single-group behavior
presentGroups = groupsToProcess.sort(
  (a, b) => new Date(b.created_at).getTime() - new Date(a.created_at).getTime()
);
if (presentGroups.length > 0) {
  idsToConnect = presentGroups[0].deviceIds ?? [];  // ← ONLY the newest group
}
```

**Impact**: A user with "Left Skate" in Group A (created Jan) and "Right Skate" in Group B (created Feb) only auto-connects the right skate. The left skate sits dark until manually connected.

## Root Cause

The original design assumed one group per user (the "Fleet" concept). After the many-to-many group migration, users can have multiple groups, but the auto-connect logic was never updated to aggregate across all groups.

## Proposed Fix

**Strategy: Collect ALL registered device MACs across ALL user groups. Deduplicate by MAC address.**

### Step 1: Update the cloud path to aggregate all groups

```diff
 // useDashboardAutoConnect.ts L264-283 — cloud path
 if (!isOffline && supabase && cloudUserId) {
   const { data: devices } = await supabase
     .from('registered_devices')
     .select('*')
     .eq('user_id', cloudUserId);
   if (devices) {
-    presentGroups = groupsToProcess.sort(
-      (a, b) => new Date(b.created_at).getTime() - new Date(a.created_at).getTime()
-    );
-    if (presentGroups.length > 0) {
-      const targetGroupId = presentGroups[0].id;
-      idsToConnect = (devices as RegisteredDeviceRow[])
-        .filter((d) => {
-          const dGroupIds: string[] = d.group_ids || (d.group_id ? [d.group_id] : []);
-          return dGroupIds.includes(targetGroupId);
-        })
-        .map((d) => d.device_mac || d.id);
-    }
+    presentGroups = groupsToProcess.sort(
+      (a, b) => new Date(b.created_at).getTime() - new Date(a.created_at).getTime()
+    );
+    // Aggregate ALL device MACs across ALL groups (deduplicated)
+    const allGroupIds = new Set(presentGroups.map(g => g.id));
+    const macSet = new Set<string>();
+    for (const d of devices as RegisteredDeviceRow[]) {
+      const dGroupIds: string[] = d.group_ids || (d.group_id ? [d.group_id] : []);
+      if (dGroupIds.some(gId => allGroupIds.has(gId))) {
+        macSet.add(d.device_mac || d.id);
+      }
+    }
+    idsToConnect = Array.from(macSet);
   }
```

### Step 2: Update the offline path to aggregate all groups

```diff
 // useDashboardAutoConnect.ts L284-291 — offline path
 } else {
-  presentGroups = groupsToProcess.sort(
-    (a, b) => new Date(b.created_at).getTime() - new Date(a.created_at).getTime()
-  );
-  if (presentGroups.length > 0) {
-    idsToConnect = presentGroups[0].deviceIds ?? [];
-  }
+  presentGroups = groupsToProcess.sort(
+    (a, b) => new Date(b.created_at).getTime() - new Date(a.created_at).getTime()
+  );
+  // Aggregate ALL device MACs across ALL offline groups (deduplicated)
+  const macSet = new Set<string>();
+  for (const group of presentGroups) {
+    for (const mac of group.deviceIds ?? []) {
+      macSet.add(mac);
+    }
+  }
+  idsToConnect = Array.from(macSet);
 }
```

### Step 3: Update the log to include all groups, not just `presentGroups[0]`

```diff
 // useDashboardAutoConnect.ts L294-298
 AppLogger.log('BLE_STATE_CHANGE', {
   event: 'auto_connect_queued',
-  fleet: presentGroups[0]?.group_name,
+  fleets: presentGroups.map(g => g.group_name),
+  groupCount: presentGroups.length,
   count: idsToConnect.length,
 });
```

### Step 4: Verify `buildOfflineGroupMap` already supports multi-group

Looking at [useDashboardAutoConnect.ts:57-85](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L57-L85), `buildOfflineGroupMap` already iterates over all `group_ids` per device and returns all groups. No changes needed here — it already provides the full group list. The single-group filtering only happens downstream in the `syncCloudAndAutoConnect` function.

## Files Modified

| File | Change |
|------|--------|
| `src/hooks/useDashboardAutoConnect.ts` | Replace single-group MAC selection with all-group aggregation (cloud + offline paths), update log entry |

## Verification

1. **Unit test for `buildOfflineGroupMap`**: Already returns all groups — verify no regression.
2. **Integration test**: Create 2 groups with different devices → app launch → assert `autoConnectIdsRef.current` contains MACs from BOTH groups.
3. **Manual test**: Register left skate in Group A, right skate in Group B → cold start → verify both devices auto-connect.
4. **Edge case — shared device**: A device in multiple groups should only appear once in `idsToConnect` (deduplicated by `Set`).
5. **Edge case — empty group**: A group with zero devices should contribute zero MACs (no crash).
6. **`npm run verify`**: TSC + Jest + AST pass.

## Risk Assessment

| Risk | Mitigation |
|------|------------|
| Auto-connecting too many devices simultaneously could overwhelm the BLE stack | `connectToDevices` already serializes GATT connections and has exponential backoff. The auto-connect observer debounces with 500ms batching ([L185](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L185)). No additional concurrency risk. |
| Users may not want all groups to auto-connect (e.g., archived groups) | Currently there's no "archived" or "inactive" group concept. When introduced, add a `is_active` flag on `registered_groups` and filter here. For now, all groups are active. |
| Larger `autoConnectIdsRef` array could slow the observer filter | The observer at L135-138 is `O(n×m)` where n = allDevices and m = autoConnectIds. With typical fleet sizes (2-8 devices), this is negligible. |
| Cloud query returns devices from deleted groups | The query filters by `user_id` and the group list is pre-filtered by `groupsToProcess`. Tombstoned devices are filtered upstream by DeviceRepository. |

**Rollback**: Revert single file. Returns to single-group auto-connect behavior.

> [!NOTE]
> **Design decision**: This plan aggregates ALL groups rather than introducing a "primary group" setting. The primary-group approach would require a new UI component (group selector), a new AsyncStorage key, and Supabase schema changes — significantly more scope. Aggregating all groups is the correct default behavior for a skating app where users want all their gear active.
