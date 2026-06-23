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

**Fix Applied**: NOT YET APPLIED. Task fix/ble-disconnect-service is blocked at QA gate on this issue.

- File: `src/hooks/ble/useBLEScanner.ts` L246-247 and L260
- Required: `hasFef3Service` must be gated on at least one secondary Zengge signal (name prefix OR manufacturer data presence). Sage must first confirm whether manufacturer data is present in pre-GATT FEF3 advertisement packets before choosing the discriminator.
- Recommended change: require `isKnownPrefix || !!device.manufacturerData` in addition to the UUID/serviceData match.

**Date**: 2026-06-23
**Task**: fix/ble-disconnect-service
**Severity**: Medium (user-visible wizard noise; not a crash or data loss)

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

**Fix Applied**: NOT YET APPLIED — gap identified at QA gate.

- File: `src/services/ble/DisconnectService.ts` L27-46
- Required: Move `if (isDestroyable(bleManager)) { bleManager.destroyClient(); }` to AFTER the `for` loop, not inside it. Only call it once, after all per-device GATT teardown is complete.

**Date**: 2026-06-23
**Task**: fix/ble-disconnect-service
**Severity**: High (multi-device disconnect leaves GATT handles and listener subscriptions leaked; reproducible in every group session teardown)
