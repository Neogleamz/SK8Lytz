# Crew Broadcast Scene Redundancy Analysis

> Analyst: Reyes (Scout)
> Date: 2026-06-22
> Task: Verify safety of Option B deletion — remove the `broadcastScene`/`onCrewSceneChange` dead path
> Confidence: VERIFIED (all claims backed by file:line citations)

---

## Summary Verdict

**VERIFIED: Deleting the `broadcastScene`/`onCrewSceneChange` path loses ZERO currently delivered functionality.**

However, there is a critical second finding that Option B alone does not address: **`broadcastPayload` is itself never called from anywhere in the source tree.** The live diagram in `docs/SK8Lytz_App_Master_Reference.md:L1717` (`DC->>CS: broadcastPayload`) is aspirational — it describes the intended architecture from `PLAN-feat-crewz-resilience.md`, not the current reality. As of today, **crew leader broadcasts are completely broken** regardless of which path is wired. The dead path (`broadcastScene`) calls a no-op; the working implementation (`broadcastPayload`) has no caller.

Option B (deletion) is safe and correct. But completing it without also wiring `broadcastPayload` into the leader flow will leave crew sync still non-functional. The plan should be scoped as: DELETE the dead path AND WIRE the live path in a single task.

---

## 1. The Live Path — `onPatternChanged → broadcastPayload`

**Finding: `broadcastPayload` has no caller. The live path diagram is aspirational, not real.**

- `CrewRealtime.broadcastPayload` is defined at `src/services/CrewService/CrewRealtime.ts:84-102`.
- `CrewService.ts` does NOT expose `broadcastPayload` as a public delegation — it is absent from the Delegated Methods block (`src/services/CrewService/CrewService.ts:58-64`).
- A full ripgrep of the entire `src/` tree for `broadcastPayload` returns exactly one hit: the definition site. No caller exists anywhere.
- `DockedController.tsx:235-259` (`writeToDevice`) calls only `optimisticWrite` — which goes to BLE hardware. It does not call `crewService.broadcastPayload`.
- `useDashboardController.tsx:215-241` (`onPatternChanged`) saves to ledger and updates dashboard group state. It does not call `crewService.broadcastPayload`.

**Confidence: VERIFIED** — `src/services/CrewService/CrewRealtime.ts:84`, `src/services/CrewService/CrewService.ts:58-64`, `src/components/DockedController.tsx:235-259`, `src/hooks/useDashboardController.tsx:215-241`.

---

## 2. Coverage Proof — What `captureEntireState()` Captures vs. What `broadcastPayload` Would Send

`captureEntireState` is defined at `src/hooks/useDockedControllerState.ts:177-191`. It returns a `CapturedControllerState` with:

- `activeMode`, `fixedSubMode`, `fixedModePattern`
- `selectedColor`, `selectedPatternId`, `brightness`, `speed`
- `multiColors`, `multiLength`, `multiTransition`
- `musicPatternId`, `musicPrimaryColor`, `musicSecondaryColor`, `micSensitivity`, `micSource`, `musicMatrixStyle`
- `streetSensitivity`, `streetCruiseColor`, `streetBrakeColor`

`broadcastPayload` accepts `number[]` — a compiled BLE byte array already sent to hardware.

**Key architectural asymmetry:** `captureEntireState` is a UI state snapshot (mode + color parameters). `broadcastPayload` sends a raw BLE byte array. These are different abstraction levels.

The member-side receiver (`subscribeAsMember`, `CrewRealtime.ts:57-59`) calls `onSceneOrPayload(crewPayload.payload)` where `payload` is a `number[]`. That `number[]` is passed to `onApplyCloudScene` / `applyCloudScene` (`useDockedControllerState.ts:140-165`), which expects a `CloudScenePayload` (a structured object of mode/color fields). **A raw `number[]` byte array passed to `applyCloudScene` will silently no-op on every field check** (`if (scenePayload.activeMode)`, etc.) because none of the numeric index keys match.

This means the member sync is also broken in a second way: even if `broadcastPayload` had a caller, the member would receive bytes and pass them to a JSON scene handler that ignores them.

**No state is "lost" by deleting `broadcastScene`** because neither path currently delivers anything to members. But the fix requires resolving the two independent broken paths together (leader no caller, member wrong handler type).

**Confidence: VERIFIED** — `src/hooks/useDockedControllerState.ts:177-191` (captureEntireState), `src/services/CrewService/CrewRealtime.ts:57-59` (member receiver), `src/hooks/useDockedControllerState.ts:140-165` (applyCloudScene expects CloudScenePayload), `src/components/dashboard/DashboardCrewPanel.tsx:117-119` (member calls onApplyCloudScene with scene).

---

## 3. Member-Side Receiver — Dead/Orphaned Code After Deletion?

`subscribeAsMember` (`CrewRealtime.ts:46-82`) handles:
- `scene_update` event → checks `crewPayload?.payload` → calls `onSceneOrPayload(crewPayload.payload)` (line 59)
- `session_ended` event → cleanup (lines 61-78)

The `scene_update` handler only reads `crewPayload.payload` (the `number[]` field). It never reads `crewPayload.scene` (the `Record<string,any>` field from `CrewScenePayload`). The `broadcastScene` no-op never sent anything on the Supabase channel, so no member ever received a `scene`-only event. The member receiver has therefore never processed a non-payload `scene_update` in production.

**After Option B deletion:** `subscribeAsMember` remains intact and correct. No orphaned receiver code. The `scene` field in `CrewScenePayload` type (`src/services/CrewService/types.ts:17-21`) is defined but the receiver ignores it — that field is vestigial from the original JSON-broadcast plan.

**Confidence: VERIFIED** — `src/services/CrewService/CrewRealtime.ts:57-59`, `src/services/CrewService/types.ts:17-21`.

---

## 4. Full Deletion Surface for Option B

Every file:line that must change, with annotation on whether it is safe to remove:

| File | Line(s) | What to Remove | Safe to Remove? |
|---|---|---|---|
| `src/services/CrewService/CrewRealtime.ts` | 104-108 | `broadcastScene` method stub + TODO comment | YES — no-op, zero effect |
| `src/services/CrewService/CrewService.ts` | 61 | `public broadcastScene: CrewRealtime['broadcastScene'];` field declaration | YES |
| `src/services/CrewService/CrewService.ts` | 87 | `this.broadcastScene = this.realtime.broadcastScene.bind(this.realtime);` binding | YES |
| `src/hooks/useDashboardController.tsx` | 203 | `onCrewSceneChange={(scene: Record<string, unknown>) => crewService.broadcastScene(scene, userId)}` prop | YES — calls no-op |
| `src/components/DockedController.tsx` | 109 | `onCrewSceneChange?: (scene: Record<string, unknown>) => void;` prop type | YES |
| `src/components/DockedController.tsx` | 151 | `onCrewSceneChange` destructured from props | YES |
| `src/components/DockedController.tsx` | 467 | `useCrewLeaderBroadcast(crewRole, onCrewSceneChange, captureEntireState, [...])` call | YES — entire dead broadcast call |
| `src/hooks/useCrewLeaderBroadcast.ts` | entire file | The hook itself — only consumer is DockedController L467 | YES — confirm no other consumers first |
| `src/services/CrewService/types.ts` | 18 | `scene: Record<string, any>;` field in `CrewScenePayload` | CAUTION — see note below |

**CAUTION on `CrewScenePayload.scene`:** This field is in the type but the receiver ignores it. `broadcastPayload` at `CrewRealtime.ts:94` sends `scene: {}` as a dummy to satisfy the strict type. If `CrewScenePayload` is removed/changed, the send call must be updated too. Safe to remove `scene` field IF the `as unknown as CrewScenePayload` cast at L98 is also cleaned up. Not a hard dependency.

**Do NOT remove:**
- `broadcastPayload` definition (`CrewRealtime.ts:84-102`) — this is the working implementation that must be wired up
- `subscribeAsMember` receiver (`CrewRealtime.ts:46-82`) — live member subscription
- `captureEntireState` — still used by `writeToDevice` reconcile path and `lastConfirmedStateRef`
- `useCrewLeaderBroadcast.ts` deps array state — those states are also tracked for other reasons

**Confirm before deletion:** grep for any other consumers of `onCrewSceneChange` or `useCrewLeaderBroadcast` outside `DockedController.tsx`.

---

## 5. Original Intent — Git History and SESSION_LOG

`PLAN-feat-crewz-resilience.md:26` explicitly states the intent:
> "Refactor `broadcastScene` to `broadcastPayload`. Instead of broadcasting massive JSON objects, broadcast compiled byte arrays."

This plan was partially executed: `broadcastPayload` was created, heartbeat was implemented, but the wiring from DockedController to `broadcastPayload` was never completed. The TODO comment at `CrewRealtime.ts:104-105` confirms this was a deliberate deferral: "The plan removed broadcastScene, but unlisted caller CrewService & useDashboardController still require it. Leaving this stub to prevent compiler failure (S4 rule enforcement)."

SESSION_LOG has no entry for this specific decision. The Bucket List task `fix/crew-broadcast-scene-noop` (added 2026-06-22 by the GROUP_SYNC cartographer) records the discovery but does not resolve Option A vs B.

`docs/SK8Lytz_App_Master_Reference.md:L1734` explicitly flags this as known debt: "the `crewService.broadcastScene(scene)` call from `useDashboardController.tsx:203` lands in the no-op stub and transmits nothing."

**Confidence: VERIFIED** — `docs/plans/PLAN-feat-crewz-resilience.md:26`, `src/services/CrewService/CrewRealtime.ts:104-105`, `docs/SK8Lytz_App_Master_Reference.md:1734`.

---

## 6. Verdict

**"Deleting the broadcastScene path loses zero functionality."**

**VERIFIED. Confidence: HIGH.**

Rationale: `broadcastScene` is a no-op stub confirmed at `CrewRealtime.ts:106-108`. It has never transmitted a single byte on the Supabase channel. Every caller — `useDashboardController.tsx:203` → `useCrewLeaderBroadcast.ts:18` → `captureEntireState()` — sends data into a void. No member has ever received crew leader state through this path.

**Residual risk: LOW but present.** The member-side `applyCloudScene` type mismatch (expects `CloudScenePayload` object but receives `number[]` from `subscribeAsMember`) means that wiring `broadcastPayload` alone will produce a second silent failure on the member end. The complete fix requires:
1. DELETE the `broadcastScene` dead path (Option B — confirmed safe).
2. WIRE `broadcastPayload` as a public method on `CrewService` and call it from `writeToDevice` in `DockedController`.
3. FIX the member receiver to forward `payload: number[]` to `writeToDevice` (direct BLE write), NOT to `applyCloudScene` (which expects a structured object).

Without all three steps, crew sync remains non-functional even after the deletion.

---

## Deletion Surface Checklist

| # | File | Line(s) | Action |
|---|---|---|---|
| 1 | `src/services/CrewService/CrewRealtime.ts` | 104-108 | Remove `broadcastScene` stub + TODO comment |
| 2 | `src/services/CrewService/CrewService.ts` | 61 | Remove `public broadcastScene` field |
| 3 | `src/services/CrewService/CrewService.ts` | 87 | Remove `this.broadcastScene = ...` binding |
| 4 | `src/hooks/useDashboardController.tsx` | 203 | Remove `onCrewSceneChange` prop |
| 5 | `src/components/DockedController.tsx` | 109 | Remove `onCrewSceneChange?` from props type |
| 6 | `src/components/DockedController.tsx` | 151 | Remove `onCrewSceneChange` from destructure |
| 7 | `src/components/DockedController.tsx` | 467 | Remove `useCrewLeaderBroadcast(...)` call |
| 8 | `src/hooks/useCrewLeaderBroadcast.ts` | entire file | Delete file (confirm zero other consumers) |
| 9 | `src/services/CrewService/types.ts` | 18 | Remove `scene` field from `CrewScenePayload` (+ fix L98 cast in `broadcastPayload`) |

**Do NOT remove:** `broadcastPayload` (L84-102), `subscribeAsMember` (L46-82), `captureEntireState`, any BLE write infrastructure.

---

## Residual Risks

| Risk | Severity | Notes |
|---|---|---|
| Member `applyCloudScene` receives `number[]` not `CloudScenePayload` | HIGH | Live bug independent of Option B. Member sync silently no-ops. Fix: member receiver must call `writeToDevice(payload)` not `applyCloudScene(payload)`. |
| `broadcastPayload` never called — leader sync dead | HIGH | `CrewService` does not expose it as a delegation. Must add `public broadcastPayload` and wire it in `DockedController.writeToDevice`. |
| `fetchLastScene` returns `last_scene` column (persisted by `_persistLastPayload` as `number[]`) but is passed to `applyCloudScene` | MEDIUM | `useDashboardCrew.ts:98-100` and `DashboardCrewPanel.tsx:126-128`. Same type mismatch — `last_scene` is a byte array, not a `CloudScenePayload`. |
| `useCrewLeaderBroadcast` deps array may be imported elsewhere | LOW | Confirm with ripgrep before file deletion. Current evidence shows single consumer. |
