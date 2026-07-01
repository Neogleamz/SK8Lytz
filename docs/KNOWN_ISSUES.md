# Known Issues & Victory Snapshots

This file is a reference document — NOT always-on. Read it when you hit a specific failure pattern.
Moved from `safety-protocol.md` to reduce ambient context overhead.

---

## 🏆 VS-001: Parallel Worktree Gatekeeper Divergence (2026-05-26)

**Symptom**: Running the fortress-gatekeeper with 2+ active worktrees causes the second worktree's branch to be silently deleted even when its merge fails. The commit becomes an orphan recoverable only via `git reflog` + `cherry-pick`.

**Root Cause**: The old gatekeeper's `foreach` loop merged worktrees sequentially. After worktree 1 merged, master moved ahead by 1 commit. Worktree 2's branch still had the OLD parent, so `--ff-only` failed. The script then called `git worktree remove` and `git branch -D` **regardless of merge success**.

**Fix Applied**: `tools/fortress-gatekeeper.ps1` now:

1. Rebases each branch onto current master HEAD before merging
2. Captures the `--ff-only` exit code — on failure: HALT + preserve branch + skip via `continue`
3. Only tears down worktree/branch if merge **succeeded**

**⛔ Operational Rule**: NEVER create two worktrees from the same base commit and run the gatekeeper expecting both to merge cleanly in one pass. Create worktree 1 → merge it → THEN create worktree 2.

---

## 🏆 VS-002: Gitignore Shadow Zone — Lost Wear OS Files (2026-06-03)

**Symptom**: Files created in a worktree pass all tooling. But `git add .` silently ignores them. After the gatekeeper merges and deletes the worktree, files are permanently lost — zero trace in git history.

**Root Cause**: Root `.gitignore` line 44 contained `/android` — ignoring the entire `android/` directory. Git negation rules (`!android/sk8lytzWear/`) didn't override this because the parent was already excluded.

**Fix Applied**:

1. Added `!/android/sk8lytzWear/` negation to root `.gitignore`
2. Used `git add --force android/sk8lytzWear/` for the initial track
3. Future commits track normally after force-add

**⛔ Operational Rule**: Before committing in ANY worktree, ALWAYS run `git status --short` and verify file count matches changes. If files are missing, run `git check-ignore -v <path>`. NEVER assume `git add .` captured everything.

---

## 🏆 VS-003: Documentation Drift — 16 Commits of Silent Staleness (2026-06-06)

**Symptom**: `SK8Lytz_App_Master_Reference.md` declared heartbeat "DELETED" while live code had a new `useBLEHeartbeat` hook. Hook Registry claimed "18 Hooks" when 24+ existed. Auto-Recovery documented wrong backoff values.

**Root Cause**: Zero documentation gates existed in the pipeline. Every checkpoint verified CODE correctness but NONE checked documentation correctness. 16 consecutive commits of architectural changes landed with zero docs updates.

**Fix Applied**:

1. Kanban Constitution Rule 6 — docs parity pointer to `/start-task` Phase 6
2. `/start-task` Phase 5.5 — explicit documentation parity check
3. `/diff-review` — added "📖 Documentation Parity" row
4. `agent-behavior.md` — VS-003 evolved rule
5. `/scaffold-hook` — Hook Registry update step added

**⛔ Operational Rule**: When completing ANY task that creates new hooks, services, components, or modifies BLE architecture/protocol/types, update `tools/SK8Lytz_App_Master_Reference.md` BEFORE running the gatekeeper. State: `"Documentation parity check: [sections updated]"` or `"no architectural changes — docs gate skipped"`.

---

## 🏆 VS-004: ProGuard Stripping of Native Modules (2026-06-08)

**Symptom**: The Android release build compiles perfectly and installs, but native features like BLE scanning silently fail. The OS registers the hardware events, but the callbacks to JavaScript are never fired.

**Root Cause**: Release builds use ProGuard/R8 to minify the app. If a native module uses reflection or passes callbacks to JS dynamically without explicitly declaring them, R8 strips them as `unused` code.

**Fix Applied**:

1. Created a root `proguard-rules.pro` file containing explicit `-keep` directives for `react-native-ble-plx`, `RxAndroidBle`, `reanimated`, `vision-camera`, and `nitro-modules`.
2. Added `"android": { "extraProguardRules": "./proguard-rules.pro" }` to `app.config.js` to ensure Expo injects it during builds.

**⛔ Operational Rule**: Whenever introducing a new native module (especially hardware or background services), ALWAYS check if it requires ProGuard rules. Do NOT assume it will work in release just because it works in dev mode. Add its rules to `proguard-rules.pro`.

---

## 🏆 VS-005: React Native Web SafeArea Context Crash (2026-06-15)

**Symptom**: Web Demo crashes to a black screen with SafeAreaContext.js:95 Uncaught Error: No safe area value available.
**Root Cause**: <SafeAreaProvider> measures the DOM asynchronously. On web, initialWindowMetrics is null. Any child calling useSafeAreaInsets() on the first render frame crashes because no metrics exist yet.
**Fix Applied**: Injected a zeroed-out initialMetrics fallback in App.tsx conditionally for Platform.OS === 'web'.
**⛔ Operational Rule**: Never mount <SafeAreaProvider> without web fallback metrics if children use safe area hooks.

---

## VS-006: FEF3 Scan Filter — Tile Beacon False Positives (2026-06-23)

**Symptom**: During hardware setup wizard scans, UNKNOWN phantom devices appear in the candidate list that users cannot register. Frequency increases in dense BLE environments (skating rinks, gyms, public spaces).

**Root Cause**: UUID `0000fef3-0000-1000-8000-00805f9b34fb` is in the Bluetooth SIG assigned number space and is registered to Tile, Inc. (tracker beacons). The FEF3 filter added in `useBLEScanner.ts` (commit b7a23639) correctly allows Zengge pre-GATT-connection scan advertisement detection but admits ALL `0xFEF3`-advertising devices — including Tile — because no secondary Zengge discriminator (name prefix or manufacturer data check) is required on the FEF3 branch. `classifyBLEDevice.resolveProductType` returns `'UNKNOWN'` for these devices (no hwPoints, no product_type), and they surface in the wizard `pendingRegistrations` state as unregisterable entries.

**Fix Applied**: `hasFef3NameGuard` secondary discriminator added in `useBLEScanner.ts` — FEF3 branch now requires `isKnownPrefix || !localName` to pass. Nameless devices (empty localName) pass unconditionally (Zengge controllers). Named devices only pass if name matches ZENGGE_NAME_PREFIXES (blocks Tile, which advertises "Tile" as localName).

**Date**: 2026-06-23
**Task**: fix/ble-disconnect-service
**Severity**: Medium (user-visible wizard noise; not a crash or data loss)

---

### [VS-008] FEF3 Name-Guard Limitation

- **Risk:** Zengge controllers advertising with a non-empty name not in ZENGGE_NAME_PREFIXES will not be caught by the FEF3 pre-GATT filter.
- **Mitigation:** Add new prefixes to ZENGGE_NAME_PREFIXES as new firmware variants are discovered. Nameless Zengge controllers (empty localName/name) are always caught.
- **Status:** DOCUMENTED — acceptable tradeoff vs. admitting all FEF3 devices including Tile trackers.

---

## VS-007: DisconnectService Null bleManager — Silent Success Mislead (2026-06-23)

**Symptom**: If `bleManager` is ever null when DISCONNECTING state is entered, `cancelDeviceConnection` throws a TypeError. The catch block in DisconnectService absorbs it, the actor returns `{ success: true }`, and the machine transitions to IDLE — but no actual GATT teardown occurred.

**Root Cause**: `DisconnectService.ts` L29 calls `bleManager.cancelDeviceConnection(device.id)` without a null guard. The catch block at L33-39 is intentionally broad (it handles real GATT errors) but also swallows the null-pointer case, causing the actor to return success when no work was done.

**Fix Applied**: Theoretical gap only — `BleMachineContext` types `bleManager` as `BleManager` (non-nullable) and the machine initializer enforces this. No production path today passes a null bleManager. Logged for defensive hardening if the type contract is ever relaxed. Adding `if (!bleManager) return { success: false }` before the loop at DisconnectService.ts L27 would close this gap without breaking the current onError→IDLE path.

**Date**: 2026-06-23
**Task**: fix/ble-disconnect-service
**Severity**: Low (theoretical — type contract prevents today; not actionable until type widens)

---

## VS-009: DisconnectService destroyClient Called Inside Per-Device Loop (2026-06-23)

**Symptom**: With 2+ connected devices (group skate session), disconnecting causes the second (and subsequent) device's `cancelDeviceConnection` to throw because the BLE manager's native stack has already been torn down by `destroyClient()` during the first device's iteration. The catch block absorbs the secondary errors, so the actor returns `{ success: true }` and the machine transitions to IDLE — but the second device's GATT connection and its disconnect listener are not properly cleaned up. The listener leaks until the next app lifecycle event.

**Root Cause**: `DisconnectService.ts` L30-32 calls `bleManager.destroyClient()` inside the `for (const device of connectedDevices)` loop. `destroyClient()` on `react-native-ble-plx`'s `BleManager` destroys the entire native BLE stack instance. After it fires on iteration 0, the manager is dead; iteration 1's `cancelDeviceConnection` hits a destroyed manager. The correct placement is a single call AFTER the loop completes (all GATT connections cancelled first).

**Fix Applied**: `destroyClient()` moved to after the `for` loop in `DisconnectService.ts`. Called once post-loop, after all per-device GATT cancellations complete.

**Date**: 2026-06-23
**Task**: fix/ble-disconnect-service
**Severity**: High (multi-device disconnect leaves GATT handles and listener subscriptions leaked; reproducible in every group session teardown)

---

## VS-010: Organic BLE Disconnect Does Not Flush BleWriteQueue (2026-06-23)

**Symptom**: On organic (non-recovery) BLE disconnect — device vanishes without triggering the auto-recovery path — any `enqueueDelay` + `safeWrite` pairs still in the `BleWriteQueue` singleton drain against a dead connection. `safeWrite` catches the resulting GATT error, so no crash occurs. However, the queue's single transient-retry logic (`BleWriteQueue.ts:210-222`) fires once per queued entry, adding ~100-150ms of unnecessary retry overhead per entry before resolving `false`.

**Root Cause**: `clearWriteQueue()` is called in `RecoveryService.ts:57` at the entry point of recovery, but is NOT called in the `onDeviceDisconnected` callback registered by `ConnectService.ts:222`. Organic disconnects (device battery dies, skater goes out of range) bypass the recovery path, leaving the queue to drain naturally.

**Fix Applied**: Not applied — pre-existing gap discovered during QA of `fix/controller-dispatch-safety`. The `enqueueDelay` change in `useControllerDispatch.ts:356` surfaces this pre-existing behavior but does not introduce it. No crash or data loss occurs; the retry overhead is bounded by `MAX_QUEUE_DEPTH=8` (`BleWriteQueue.ts:38`) and the `isMusicBusyRef` guard at `useControllerDispatch.ts:314` prevents re-entrant calls while the queue drains.

**Recommended Fix**: Call `clearWriteQueue()` inside the `onDeviceDisconnected` callback in `ConnectService.ts:222` (or wherever organic disconnects are first handled), guarded by a check that recovery is NOT already in progress (to avoid double-clearing).

**Date**: 2026-06-23
**Task**: fix/controller-dispatch-safety (discovered during QA)
**Severity**: Low — no crash, no data corruption. Bounded GATT retry overhead on organic disconnect only.

---

## VS-011: Singleton Protocol Adapter — Stale JSDoc Describing Pre-Fix Behavior (2026-06-23)

**Symptom**: `ZenggeAdapter.ts:13-15` contains a block comment stating "ZenggeAdapter uses its OWN instance so adapter writes have an independent sequence counter." After PROTOCOL_CORE-004 (commit on `fix/protocol-core-integrity`), this is factually inverted — the adapter now uses the SHARED singleton, not an independent instance. Any developer reading the comment will incorrectly believe the adapter has an isolated counter.

**Root Cause**: The JSDoc block was written to describe the OLD design (per-adapter `new ZenggeProtocol()`). The fix at `ZenggeAdapter.ts:47` replaced `private readonly protocol = new ZenggeProtocol()` with `private get protocol() { return ZenggeProtocol.sharedInstance; }` but did not update the file-level class comment that described the rationale for the old design.

**Fix Applied**: Non-blocking documentation gap flagged to Sage during QA of `fix/protocol-core-integrity`. The comment at `ZenggeAdapter.ts:13-15` must be updated to reflect the new design: adapter uses sharedInstance to share ONE monotonic counter with legacy namespace callers, preventing split-brain SeqNum divergence.

**Date**: 2026-06-23
**Task**: fix/protocol-core-integrity (discovered during QA)
**Severity**: Low — no runtime defect. Documentation accuracy liability for future maintainers only.

---

## VS-012: Watch Bridge iOS startListening — Missing Native Function Definition (2026-06-24)

**Symptom**: On iOS, any component calling `WatchBridge.addWatchCommandListener()` or `WatchBridge.addWatchHealthListener()` crashes at runtime with a NativeModule function-not-found exception before the listener subscription is returned. The crash occurs because `nativeModule.startListening()` is called unconditionally in the JS layer for both methods, but the iOS Swift module does not declare a `startListening` function in its `ModuleDefinition`.

**Root Cause**: `modules/sk8lytz-watch-bridge/src/index.ts:118` and `:162` call `nativeModule.startListening()` unconditionally on any non-web platform. The Kotlin (Android) module at `Sk8lytzWatchBridgeModule.kt:53` defines `Function("startListening")` because Android requires explicit MessageClient listener registration. The Swift (iOS) module at `Sk8lytzWatchBridgeModule.swift:17-54` does NOT define `startListening` — it is not needed on iOS because `WCSession.default.activate()` in `OnCreate` automatically arms the delegate callbacks. The JS layer does not distinguish platforms when calling `startListening`, so iOS receives a call to an undefined native function.

**Fix Applied**: Not yet applied — discovered during QA of `spike/watch-bridge-clean-install`. Two fix paths exist:

- Option A (preferred): Add `Function("startListening") {}` as a no-op to `Sk8lytzWatchBridgeModule.swift` inside `definition()`. Satisfies the cross-platform JS contract without any behavioral change on iOS.
- Option B: Wrap `nativeModule.startListening()` in `src/index.ts` with `if (Platform.OS === 'android')`. Lower blast radius but hides the contract asymmetry.

**Date**: 2026-06-24
**Task**: spike/watch-bridge-clean-install (discovered during QA)
**Severity**: High — confirmed runtime crash on iOS for any consumer of `addWatchCommandListener` or `addWatchHealthListener`. Affects all watch-bridge-enabled sessions on iOS devices.

---

## 🏆 VS-013: OS-Level UUID Scan Filter Drops Pre-GATT Controllers — "Searching Forever" (2026-07-01)

**Symptom**: During hardware setup (FTUE / fresh install), the wizard spins in "SEARCHING FOR SKATES…" indefinitely. No devices are ever surfaced. `scanCallback`'s FEF3/FCF1/name-prefix filtering never runs — the callback is never invoked at all.

**Root Cause**: `BleMachine.ts` SCANNING-entry and SCAN_RESUME hardcoded an OS-level Service-UUID scan filter — `startDeviceScan([ZENGGE_SERVICE_UUID, BANLANX_SERVICE_UUID], …)` — ignoring the `context.scanServiceUUIDs` field (which `useBLE.ts` deliberately sets to `null` for an unfiltered scan). Fresh Zengge/FCF1 controllers broadcast their identifying UUID in `mServiceData`, NOT `mServiceUuids`, and only expose `FFFF` post-GATT-connection. With no cached GATT service on a fresh install, an OS-level UUID filter drops every controller before the JS `scanCallback` fires. This contradicts VS-006 and the documented unfiltered-scan requirement. The change was introduced by commit `3d422cb7` (bundled into an unrelated "connection state badges" commit, per `PLAN-feat-ble-scan-filter-uuid.md`), which is why it slipped past review.

**Fix Applied**: Reverted both call sites to `startDeviceScan(context.scanServiceUUIDs, …)`. The context field resolves to `null`, restoring the mandatory unfiltered scan. Device filtering remains in `scanCallback` (JS layer), never at the OS layer. Removed now-dead `ZENGGE_SERVICE_UUID` / `BANLANX_SERVICE_UUID` imports from `BleMachine.ts`. `PLAN-feat-ble-scan-filter-uuid.md` annotated as HARMFUL — DO NOT RE-APPLY.

**⛔ Operational Rule**: NEVER pass a hardcoded Service-UUID array to `startDeviceScan`. The scan MUST stay unfiltered (`scanServiceUUIDs: null`) because Zengge/FCF1 controllers advertise via `mServiceData`. If iOS background scanning ever requires a UUID filter, set it conditionally via the `scanServiceUUIDs` context input in `useBLE.ts` — never hardcode it in the machine.

**Date**: 2026-07-01
**Task**: fix/ble-scan-filter-regression
**Severity**: Critical (fresh-install hardware setup completely non-functional; app unusable for every new user)

---

## 🏆 VS-014: FTUE Completion Never Persists the Group Entity — No Groups + Devices Disconnected (2026-07-01)

**Symptom**: After completing the hardware-setup wizard, the dashboard shows NO groups, and the just-registered devices appear disconnected (never auto-connect).

**Root Cause**: `handleRegistrationComplete` (`useDashboardGroups.ts`) called `saveAllRegisteredDevices(devices)` — which persists DEVICES only (with `group_ids` stamped) — and then relied on a comment claiming "groups are derived automatically from registeredDevices." That is false: the group-derivation effect (`useDashboardGroups.ts` Pass 1) reads exclusively from `GroupRepository.getGroups()`, and `GroupRepository.groups` is only ever populated by `saveGroupTransactional`/`setGroups`/storage-load. The FTUE path never called any of them, so the wizard's `group-<ts>` entity was never created — not in local RAM, not in the cloud `registered_groups` table. Consequences: (1) `customGroups` is empty → no groups shown; (2) auto-connect's cloud resolution requires a `registered_groups` row (`useDashboardAutoConnect.ts`) which never existed → device MACs never queued → devices stay disconnected. A secondary defect: `syncCloudAndAutoConnect` is captured under a `[isBluetoothSupported, isBluetoothEnabled]` effect, so its `registeredDevices` closure went stale, defeating the offline `buildOfflineGroupMap` fallback after `retriggerAutoConnect()`. Introduced when `runAutoProvisioning` was deleted ("the Setup Wizard now owns all device claiming", `DashboardScreen.tsx`) without moving the group-creation responsibility into the new path.

**Fix Applied**: (1) `handleRegistrationComplete` now builds a group→MAC map from the finalized devices' `group_ids`/`group_names` and calls `GroupRepository.saveGroupTransactional(gId, name, macs, 'device-fleet', user?.id)` for each after saving devices — creating the group locally (repopulates `customGroups`) and in the cloud (`registered_groups` row that auto-connect keys off). (2) `useDashboardAutoConnect` now reads `registeredDevices` via a live `registeredDevicesRef` so a post-registration retrigger sees freshly-saved devices instead of a stale empty list.

**⛔ Operational Rule**: The FTUE completion path MUST persist the GROUP entity to `GroupRepository`, not just the devices. `customGroups` derives from `GroupRepository`, and cloud auto-connect keys off `registered_groups` — stamping `group_ids` onto devices alone is insufficient.

**Date**: 2026-07-01
**Task**: fix/ftue-group-not-persisted
**Severity**: High (every new user finishes setup to an empty, disconnected dashboard)
