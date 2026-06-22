# Implementation Plan

**Slug:** `fix-crew-broadcast-scene`
**Author:** Quinn (TPM)
**Date:** 2026-06-22
**Risk:** 🔴 H-RISK — touches the live crew light-sync feature end-to-end (leader broadcast + member receive).
**Decision:** Scope A (full repair) — Option B deletion + leader wire + member receiver fix, in one task.
**Authoritative investigation:** `docs/analysis/crew-broadcast-scene-redundancy.md` (Reyes, VERIFIED).

---

## 0. Context & Verified Diagnosis

Crew light-sync (leader → member) is broken in **three independent ways**:

1. **Dead path exists.** `broadcastScene` is a no-op stub (`CrewRealtime.ts:106-108`) wired through `onCrewSceneChange` → `useCrewLeaderBroadcast` → `crewService.broadcastScene`. It transmits nothing. (Verified: `CrewRealtime.ts:106-108`, `CrewService.ts:61,87`, `useDashboardController.tsx:203`, `DockedController.tsx:109,151,467`, `useCrewLeaderBroadcast.ts:14-24`.)
2. **Leader live path has no caller.** `CrewRealtime.broadcastPayload(payload: number[])` (`CrewRealtime.ts:84-102`) is the working implementation but is NOT exposed on `CrewService` and is never called. (Verified: `CrewService.ts:58-61` delegated block lacks it; ripgrep `broadcastPayload` → only definition + this plan.)
3. **Member receiver routes bytes to the wrong sink.** `subscribeAsMember` (`CrewRealtime.ts:57-59`) emits a `number[]` byte array, but every member callsite forwards it to `applyCloudScene` (`useDockedControllerState.ts:140-165`), which expects a structured `CloudScenePayload` object — every field check silently fails. (Verified: `DashboardScreen.tsx:356`, `DashboardScreen.tsx:601-603`, `DashboardCrewPanel.tsx:117-119,126-128`.)

### Source of Truth — crew protocol claims (P1)
- **Wire format is a raw `number[]` byte array.** `broadcastPayload(payload: number[], userId?)` sends `event: 'scene_update'` with `payload` = byte array (`CrewRealtime.ts:84-102`). Member reads `crewPayload.payload` (`CrewRealtime.ts:57-59`). **Do NOT change this wire format.**
- **The byte array is produced upstream.** `writeToDevice(payload: number[], ...)` (`DockedController.tsx:235-259`) receives an already-compiled BLE byte array and forwards it to `optimisticWrite`. No opcode/byte-math is constructed in UI/hooks — HAL Parity is intact and MUST stay intact.
- **`broadcastPayload` is leader-gated + debounced** at the service layer (`CrewRealtime.ts:85` role check, `:88,101` 150ms debounce). The UI layer must NOT re-implement gating/byte-math — it only routes the existing `number[]`.

### ⚠️ Divergences from Reyes's line numbers (live source confirmed this session)
| Reyes claim | Live source | Status |
|---|---|---|
| `CrewRealtime.ts:104-108` stub | `104-108` (TODO 104-105, body 106-108) | ✅ accurate |
| `CrewService.ts:61` field / `:87` binding | `61` / `87` | ✅ accurate |
| `useDashboardController.tsx:203` prop | `203` | ✅ accurate |
| `DockedController.tsx:109` prop type | `109` | ✅ accurate |
| `DockedController.tsx:151` destructure | `151` (single-line destructure incl. `onCrewSceneChange`) | ✅ accurate |
| `DockedController.tsx:467` hook call | `467-471` | ✅ accurate |
| `useCrewLeaderBroadcast.ts` sole consumer | also imported at `DockedController.tsx:74` (import line) | ⚠️ **ADDITION** — the import statement at L74 must also be deleted. Reyes listed only the call site. |
| `types.ts:18` `scene` field | `types.ts:17-21` interface; `scene` at `18` | ✅ accurate, BUT see Step 9 caveat |
| Member sink "`writeToDevice(payload)`" | `writeToDevice` lives INSIDE DockedController (`L235`); member callsites are in **DashboardScreen** (`L356`, `L601-603`) via `dockedControllerRef.current?.applyCloudScene` | ⚠️ **DESIGN CORRECTION** — see Step 10. `writeToDevice` is not directly reachable from DashboardScreen; we add a new imperative handle method `applyCrewPayload`. |
| Member receiver "one place" | **TWO** sinks: `DashboardScreen.tsx:356` (auto-rejoin path) AND `DashboardScreen.tsx:601-603` (`handleCrewHubApplyCloudScene`, used by panel) | ⚠️ **ADDITION** — both must be fixed. |
| `last_scene` rejoin also mis-typed | `useDashboardCrew.ts:98-100` + `DashboardCrewPanel.tsx:126-128` route persisted `number[]` to the same sink | ✅ accurate — fixed automatically once both sinks route to `applyCrewPayload` (Step 10). |

---

## Files to Create/Modify

> This list is the contract checked against the final diff (Plan Completeness Gate). Every file below MUST appear in the diff or carry a `// SKIPPED: <reason>` addendum in this plan.

| # | File | Nature of Change |
|---|---|---|
| 1 | `src/services/CrewService/CrewRealtime.ts` | **DELETE** `broadcastScene` stub + TODO (L104-108). **MODIFY** `broadcastPayload` send object (L90-99) to stop casting through `CrewScenePayload` (consequence of Step 9). |
| 2 | `src/services/CrewService/CrewService.ts` | **DELETE** `broadcastScene` field (L61) + binding (L87). **ADD** `broadcastPayload` public delegation field + binding (mirror pattern). |
| 3 | `src/services/CrewService/types.ts` | **MODIFY** `CrewScenePayload` (L17-21) — remove vestigial `scene` field, add `payload: number[]` as a first-class field (replaces the runtime cast). |
| 4 | `src/hooks/useCrewLeaderBroadcast.ts` | **DELETE entire file** (sole consumer removed). |
| 5 | `src/components/DockedController.tsx` | **DELETE** import (L74), prop type `onCrewSceneChange` (L109), destructure (L151), `useCrewLeaderBroadcast(...)` call (L467-471). **ADD** new prop `onCrewBroadcast?: (payload: number[]) => void` + destructure + leader broadcast call inside `writeToDevice` (L235-259). **ADD** `applyCrewPayload` to `DockedControllerHandle` type (L136-144) and `useImperativeHandle` (L444-459). |
| 6 | `src/hooks/useDashboardController.tsx` | **DELETE** `onCrewSceneChange` prop wiring (L203). **ADD** `onCrewBroadcast` prop wiring → `crewService.broadcastPayload(payload, userId)`. |
| 7 | `src/screens/DashboardScreen.tsx` | **MODIFY** both member sinks: L356 (`onApplyScene`) and L601-603 (`handleCrewHubApplyCloudScene`) to call `applyCrewPayload(payload)` instead of `applyCloudScene(scene)`. |

> **Out-of-scope dependents intentionally NOT changed:** `useDashboardCrew.ts`, `DashboardCrewPanel.tsx`, `CommunityModal.tsx`, `ScenesService.ts`. See Out of Scope section for why.

---

## Implementation Steps

> Global verify command (S7): **`npm run verify`** (TSC + Jest + AST + TypeSafety + WorkflowValidator). NEVER run raw `tsc`/`jest`.
> After EVERY edit: run `git diff HEAD <file>` and confirm only planned lines changed (POST-DIFF MANDATE).

### PART 1 — DELETE THE DEAD PATH (Option B)

#### Step 1 — Remove `broadcastScene` stub from CrewRealtime
- **File:** `src/services/CrewService/CrewRealtime.ts`
- **Lines:** 104-108
- **Exact edit:** Delete these lines in full:
  ```ts
  // TODO: The plan removed broadcastScene, but unlisted caller CrewService & useDashboardController still require it.
  // Leaving this stub to prevent compiler failure (S4 rule enforcement).
  broadcastScene(_scene: Record<string, unknown>, _userId?: string): void {
    // Cannot compile to byte array here without unlisted refactors. Dummy op.
  }
  ```
- **Verify:** `git diff HEAD src/services/CrewService/CrewRealtime.ts` shows ONLY this block removed (plus Step 8 changes if batched). Grep `broadcastScene` in this file returns 0 hits.

#### Step 2 — Remove `broadcastScene` delegation from CrewService
- **File:** `src/services/CrewService/CrewService.ts`
- **Line 61:** Delete `  public broadcastScene: CrewRealtime['broadcastScene'];`
- **Line 87:** Delete `    this.broadcastScene = this.realtime.broadcastScene.bind(this.realtime);`
- **Verify:** Grep `broadcastScene` across `src/` returns 0 hits. `git diff HEAD src/services/CrewService/CrewService.ts` shows only these two removals (plus Step 6's additions).

#### Step 3 — Remove the `onCrewSceneChange` prop wiring in useDashboardController
- **File:** `src/hooks/useDashboardController.tsx`
- **Line 203:** Delete:
  ```tsx
  onCrewSceneChange={(scene: Record<string, unknown>) => crewService.broadcastScene(scene, userId)}
  ```
  *(This line is replaced by the new `onCrewBroadcast` wiring in Step 7 — net effect is a swap, but make the deletion explicit first.)*
- **Verify:** Grep `onCrewSceneChange` in this file returns 0 hits.

#### Step 4 — Remove `onCrewSceneChange` prop type from DockedController
- **File:** `src/components/DockedController.tsx`
- **Lines 108-109:** Delete the JSDoc comment + prop type:
  ```tsx
  /** Called with full scene snapshot whenever any mode/color changes (leader only) */
  onCrewSceneChange?: (scene: Record<string, unknown>) => void;
  ```
- **Verify:** included in the file-level grep at Step 8.

#### Step 5 — Remove `onCrewSceneChange` from the props destructure
- **File:** `src/components/DockedController.tsx`
- **Line 151:** In the destructure list, remove the token `onCrewSceneChange, ` (it sits between `crewRole, ` and `onPatternChanged, `).
- **Verify:** included in the file-level grep at Step 8.

#### Step 6 — Remove the `useCrewLeaderBroadcast` call AND its import
- **File:** `src/components/DockedController.tsx`
- **Lines 466-471:** Delete the comment + call:
  ```tsx
  // ── Crew Leader Broadcast ────────────────────────────────────────────────
  useCrewLeaderBroadcast(crewRole, onCrewSceneChange, captureEntireState, [
    activeMode, fixedSubMode, selectedColor, selectedPatternId,
    brightness, speed, multiColors,
    streetSensitivity, streetCruiseColor, streetBrakeColor,
  ]);
  ```
- **Line 74:** Delete the now-orphaned import:
  ```tsx
  import { useCrewLeaderBroadcast } from '../hooks/useCrewLeaderBroadcast';
  ```
- **Verify:** Grep `useCrewLeaderBroadcast` across `src/` returns 0 hits (confirming the file can be deleted in Step 8).

#### Step 7 — Delete the orphaned hook file
- **File:** `src/hooks/useCrewLeaderBroadcast.ts`
- **Action:** Delete the entire file (`git rm src/hooks/useCrewLeaderBroadcast.ts`).
- **Pre-condition (HARD):** Step 6's grep MUST show 0 remaining references before deletion. Sole consumers were `DockedController.tsx:74` (import) and `:467` (call), both removed in Step 6.
- **Verify:** `git status` shows the file deleted. `npm run verify` (run after all source steps) reports 0 TSC errors for missing-module.

#### Step 8 — File-level confirmation of dead-path removal in DockedController
- **File:** `src/components/DockedController.tsx`
- **Verify:** `git diff HEAD src/components/DockedController.tsx` for Steps 4-6 shows ONLY the listed deletions. Grep `onCrewSceneChange` + `useCrewLeaderBroadcast` in this file = 0 hits.

---

### PART 2 — WIRE THE LEADER BROADCAST

> **Design note (HAL Parity):** `crewService` is a service singleton; per HAL Parity Mandate the UI component (`DockedController`) must not import it. We pass a new callback prop `onCrewBroadcast?: (payload: number[]) => void` (mirroring the deleted `onCrewSceneChange` pattern) and bind it to `crewService.broadcastPayload` in `useDashboardController`. The component only routes the already-compiled `number[]` — no byte-math, no opcodes.

#### Step 9 — Make `CrewScenePayload` carry `payload` as a first-class field (remove vestigial `scene`)
- **File:** `src/services/CrewService/types.ts`
- **Lines 17-21:** Replace:
  ```ts
  export interface CrewScenePayload {
    scene: Record<string, any>;
    leader_id: string;
    ts: number;
  }
  ```
  with:
  ```ts
  export interface CrewScenePayload {
    payload: number[];
    leader_id: string;
    ts: number;
  }
  ```
  **Rationale (P1):** The receiver only ever reads `crewPayload.payload` (`CrewRealtime.ts:59`); `scene` was never read by any consumer (Reyes §3, verified). Making `payload` first-class removes the need for the `as unknown as CrewScenePayload` cast.
- **File:** `src/services/CrewService/CrewRealtime.ts`
- **Lines 90-99:** Update the `broadcastPayload` send to drop the dummy `scene` and the double-cast:
  ```ts
      this.service.channel?.send({
        type: 'broadcast',
        event: 'scene_update',
        payload: {
          payload,            // runtime byte array
          leader_id: leaderId,
          ts: Date.now(),
        } satisfies CrewScenePayload,
      });
  ```
  Also update the `subscribeAsMember` handler signature at `CrewRealtime.ts:57` — the inline type `{ payload: CrewScenePayload & { payload?: any } }` may now be simplified to `{ payload: CrewScenePayload }` (the `& { payload?: any }` augmentation is no longer needed since `payload` is first-class). Keep the `eslint-disable` comment only if the lint rule still fires; remove it if it no longer does.
- **Verify:** `npm run verify` → 0 TSC errors. Grep `as unknown as CrewScenePayload` in `src/` = 0 hits. Grep `\.scene\b` inside `CrewRealtime.ts` = 0 hits.
- **⚠️ Note:** If TSC reveals any OTHER reader of `CrewScenePayload.scene` not found in this analysis, HALT and report — do not invent a field. (Pre-verified: ripgrep `CrewScenePayload` shows only `CrewRealtime.ts` imports/uses it.)

#### Step 10 — Expose `broadcastPayload` as a public delegation on CrewService
- **File:** `src/services/CrewService/CrewService.ts`
- **Line 60 (after `subscribeAsMember`, where `broadcastScene` was at L61):** Add the field declaration:
  ```ts
  public broadcastPayload: CrewRealtime['broadcastPayload'];
  ```
- **Line 86 (after the `subscribeAsMember` bind, where `broadcastScene` bind was at L87):** Add the binding:
  ```ts
  this.broadcastPayload = this.realtime.broadcastPayload.bind(this.realtime);
  ```
  *(This directly mirrors the deleted `broadcastScene` field+binding pattern from Step 2.)*
- **Verify:** `npm run verify` → 0 TSC errors. Grep `broadcastPayload` in `CrewService.ts` shows exactly 2 hits (field + binding).

#### Step 11 — Add `onCrewBroadcast` prop to DockedController + call it from `writeToDevice`
- **File:** `src/components/DockedController.tsx`
- **At L108 (where `onCrewSceneChange` prop type was deleted in Step 4):** Add the replacement prop type:
  ```tsx
  /** Routes the compiled BLE byte payload to the crew broadcast service (leader only). */
  onCrewBroadcast?: (payload: number[]) => void;
  ```
- **At L151 destructure (where `onCrewSceneChange` was removed in Step 5):** Add `onCrewBroadcast` to the destructured props (e.g., place it after `crewRole, `).
- **Inside `writeToDevice`, `src/components/DockedController.tsx:255-257`** — immediately after the existing `await optimisticWrite(payload, undefined, targetDeviceId);` line (L257), add the leader broadcast routing **before** the closing of the callback:
  ```tsx
      // Crew leader: mirror the compiled byte payload to crew members.
      // Service layer is leader-gated + debounced (CrewRealtime.ts:85,88).
      if (crewRole === 'leader') onCrewBroadcast?.(payload);
  ```
  **Important:** Place this AFTER `optimisticWrite` so the leader's own hardware write is never delayed by broadcast logic. `crewRole` and `onCrewBroadcast` are both props in scope. Do NOT construct or inspect bytes — `payload` is already the compiled `number[]`.
- **Dependency array (L259):** Add `crewRole` and `onCrewBroadcast` to the `useCallback` deps: `[parentWriteToDevice, optimisticWrite, crewRole, onCrewBroadcast]`. (The existing `eslint-disable-next-line react-hooks/exhaustive-deps` at L258 may now be removable; remove it ONLY if lint passes without it — verify via `npm run verify`.)
- **Verify:** `npm run verify` → 0 TSC + 0 lint errors. `git diff HEAD src/components/DockedController.tsx` shows the prop add, destructure add, the 1-line broadcast call, and the deps update — nothing else.

#### Step 12 — Bind `onCrewBroadcast` in useDashboardController
- **File:** `src/hooks/useDashboardController.tsx`
- **At L203 (where `onCrewSceneChange` was deleted in Step 3):** Add the new prop wiring on the `<DockedController .../>` element:
  ```tsx
  onCrewBroadcast={(payload: number[]) => crewService.broadcastPayload(payload, userId)}
  ```
  `userId` is already in scope (`useDashboardController.tsx:106`). `crewService` is already imported (it was used by the deleted `broadcastScene` call).
- **Verify:** `npm run verify` → 0 TSC errors. Grep `broadcastPayload` in `useDashboardController.tsx` shows 1 hit. Grep `crewService` import still present.

---

### PART 3 — FIX THE MEMBER RECEIVER

> **Design note (sink correction):** `subscribeAsMember` emits a `number[]` (`CrewRealtime.ts:59`). It currently flows into `dockedControllerRef.current?.applyCloudScene(scene)` at TWO points in `DashboardScreen.tsx` (L356 auto-rejoin, L602 panel handler). `applyCloudScene` expects a `CloudScenePayload` object and ignores byte arrays. The correct sink is `writeToDevice(payload)` inside `DockedController` — but that is not reachable from `DashboardScreen`. Therefore we expose a new imperative handle method `applyCrewPayload(payload: number[])` that routes to `writeToDevice`, and switch both sinks to it.

#### Step 13 — Add `applyCrewPayload` to the imperative handle
- **File:** `src/components/DockedController.tsx`
- **Type (`DockedControllerHandle`, L136-144):** After `applyCloudScene: (scenePayload: Record<string, unknown>) => void;` (L137) add:
  ```tsx
  applyCrewPayload: (payload: number[]) => void;
  ```
- **Implementation (`useImperativeHandle`, L444-459):** After `applyCloudScene,` (L445) add:
  ```tsx
  applyCrewPayload: (payload: number[]) => {
    if (!payload || payload.length === 0) return;
    // Member receive: write the leader's compiled byte payload straight to BLE.
    // Wire format is a raw number[] (CrewRealtime.ts:59) — NOT a CloudScenePayload.
    void writeToDevice(payload);
  },
  ```
  `writeToDevice` is already in the `useImperativeHandle` deps array (L459) — no deps change needed. (Confirm: L459 currently `[speed, brightness, writeToDevice, optimisticWrite]`.)
- **Verify:** `npm run verify` → 0 TSC errors (the `DockedControllerHandle` type now matches the implementation object). Grep `applyCrewPayload` in `DockedController.tsx` = 2 hits (type + impl).

#### Step 14 — Route the auto-rejoin member sink to `applyCrewPayload`
- **File:** `src/screens/DashboardScreen.tsx`
- **Line 356:** Change:
  ```tsx
  onApplyScene: (scene) => dockedControllerRef.current?.applyCloudScene(scene),
  ```
  to:
  ```tsx
  onApplyScene: (payload: number[]) => dockedControllerRef.current?.applyCrewPayload(payload),
  ```
  **Note:** `onApplyScene` type comes from `useDashboardCrew.ts:18` (`(scene: Record<string, any>) => void`). A `number[]` is assignable to `Record<string, any>` at the param position is NOT guaranteed — if TSC complains about the narrowed `number[]` param, keep the param untyped (`(payload) =>`) and let inference flow, OR cast at the call boundary as `payload as number[]` is forbidden (`as` on a value is fine but prefer untyped param). **Preferred:** `onApplyScene: (payload) => dockedControllerRef.current?.applyCrewPayload(payload as number[])` is forbidden by S3 only for `as any`; `as number[]` is permitted but discouraged. Cleanest: leave param untyped and the runtime value IS a `number[]`. If TSC errors, HALT and report the exact error rather than forcing a cast.
- **Verify:** `npm run verify` → 0 TSC errors. `git diff HEAD src/screens/DashboardScreen.tsx` shows only L356 (and L601-603 from Step 15) changed.

#### Step 15 — Route the Crew Hub panel member sink to `applyCrewPayload`
- **File:** `src/screens/DashboardScreen.tsx`
- **Lines 601-603:** Change:
  ```tsx
  const handleCrewHubApplyCloudScene = useCallback((scene: Record<string, unknown>) => {
    dockedControllerRef.current?.applyCloudScene(scene);
  }, []);
  ```
  to:
  ```tsx
  const handleCrewHubApplyCloudScene = useCallback((payload: number[]) => {
    dockedControllerRef.current?.applyCrewPayload(payload);
  }, []);
  ```
  **Note:** This handler is passed to `DashboardCrewPanel` prop `onApplyCloudScene` (`DashboardScreen.tsx:997`), whose type is `(scene: Record<string, unknown>) => void` (`DashboardCrewPanel.tsx:26`). If TSC flags the param-type narrowing, leave the param untyped (`(payload) =>`) — the runtime value from `subscribeAsMember` and `fetchLastScene` is a `number[]`. Do NOT widen `applyCrewPayload`'s signature; the byte-array contract is intentional.
- **Verify:** `npm run verify` → 0 TSC errors. Grep `applyCloudScene` in `DashboardScreen.tsx` = 0 hits (both member sinks now route to `applyCrewPayload`). Note: `applyCloudScene` legitimately remains in `DockedController.tsx` (used by reconcile + Voice/CommunityModal paths) — that is out of scope and must stay.

---

## Final Verification Gate

1. **Full verify:** `npm run verify` → expect TSC ✅, Jest ✅, AST ✅, TypeSafety ✅, WorkflowValidator ✅ (0 errors).
2. **Dead-path eradication:** `git grep -n "broadcastScene"` → 0 hits. `git grep -n "useCrewLeaderBroadcast"` → 0 hits. `git grep -n "onCrewSceneChange"` → 0 hits.
3. **Leader wire present:** `git grep -n "broadcastPayload"` → hits at `CrewRealtime.ts` (def), `CrewService.ts` (field+bind), `useDashboardController.tsx` (call). 
4. **Member sink corrected:** `git grep -n "applyCrewPayload"` → hits at `DockedController.tsx` (type+impl), `DashboardScreen.tsx` (×2). `git grep -n "applyCloudScene" src/screens/DashboardScreen.tsx` → 0 hits.
5. **Plan Completeness:** every file in "Files to Create/Modify" appears in `git diff HEAD --stat`. Any skipped file has a `// SKIPPED:` addendum appended to THIS plan.
6. **Manual smoke (device, post-merge — Blake):** Two devices, one leader + one member in the same crew session. Leader changes color/mode → member's skates mirror within ~350ms (200ms not applicable now — service debounce is 150ms). Verify via `adb logcat -s "ReactNativeJS:V"` showing a `scene_update` broadcast on leader and a `writeToDevice` on member.

---

## Risk & Rollback

**Risk level: 🔴 HIGH.** This is a live, user-facing social feature (crew light-sync). Failure modes:
- A bad member sink could blast malformed bytes to BLE hardware. Mitigation: `applyCrewPayload` guards `payload.length === 0` and reuses the existing, battle-tested `writeToDevice` → `optimisticWrite` path (no new BLE code).
- Changing `CrewScenePayload` (Step 9) is a typed contract change. Mitigation: ripgrep confirms `CrewRealtime.ts` is the sole consumer; TSC will catch any missed reader.
- `DockedController.tsx` is a large monolith (>30KB → S4 territory). **[HIGH RISK]** Steps 4-6, 11, 13 touch it.

**Backup / snapshot (MANDATORY before first edit):**
- Work in an isolated worktree per Safety Protocol Rule 2: `git worktree add ../SK8Lytz-worktrees/fix-crew-broadcast-scene -b fix/crew-broadcast-scene`.
- Before editing `DockedController.tsx`, confirm a clean baseline: `git status --short` must be empty in the worktree. The git history IS the snapshot — every step ends with `git diff HEAD <file>` so any stray change is reverted via `git checkout -- <file>`.

**Rollback procedure (if Final Verification Gate fails and 3-strike is hit):**
- `git reset --hard HEAD` in the worktree → returns to pre-task master HEAD.
- Crew sync was already 100% broken before this task (Reyes VERIFIED), so a rollback restores the prior (broken-but-stable) state with zero regression risk to other features.
- Enter consultative mode (River) — do not attempt a 4th fix.

---

## Out of Scope (HARD BOUNDARY — Sage must NOT touch)

- **Do NOT redesign the crew protocol.** The wire format (`scene_update` event carrying `payload: number[]`) is fixed. `broadcastPayload`'s existing leader-gating (`CrewRealtime.ts:85`), 150ms debounce (`:88,101`), and last-scene persistence (`:100,128-144`) MUST remain byte-for-byte unchanged.
- **Do NOT alter `applyCloudScene`'s internals** (`useDockedControllerState.ts:140-165` / `DockedController.tsx:382-385`). We are ceasing to *misuse* it for crew bytes; its body, signature, and its other consumers (Ghost reconcile L391-396, Voice, `CommunityModal`, `ScenesService`) stay untouched.
- **Do NOT change `CommunityModal.tsx`, `ScenesService.ts`** — they legitimately use `CloudScenePayload` for cloud scene objects (a different feature). Found a bug there? Leave a `// TODO:` comment; do not fix.
- **Do NOT modify `useDashboardCrew.ts` or `DashboardCrewPanel.tsx` logic** beyond what TSC may require for the prop param types. Their subscription/closure structure (`useDashboardCrew.ts:44-47,87-88`; `DashboardCrewPanel.tsx:117-128`) is correct — they forward the value; only the DashboardScreen *sink* changes. If a param-type annotation must change for TSC, that is the ONLY permitted edit in those files.
- **Do NOT add any opcode tables, byte-math, or checksum logic** in UI/hooks (HAL Parity Mandate). `payload` is an opaque `number[]` produced upstream; you only route it.
- **Do NOT touch `captureEntireState`** — it remains used by `writeToDevice`'s reconcile snapshot (`DockedController.tsx:250`) and `lastConfirmedStateRef`.
- **Do NOT edit `.git/hooks/`, `package.json` versions, or the Master Reference** in this plan's scope — Master Reference §3/§4 updates are a separate DOCS GATE step handled by Avery during execution (the docs parity update for the new `applyCrewPayload` handle + `broadcastPayload` delegation is REQUIRED before gatekeeper, per VS-003, but is a post-code step, not a Sage source edit).
