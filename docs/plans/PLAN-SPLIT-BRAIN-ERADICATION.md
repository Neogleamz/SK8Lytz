# Implementation Plan
# PLAN-SPLIT-BRAIN-ERADICATION

## Summary
Three confirmed split-brain state duplication patterns found across the codebase:

1. **ZenggeAdapter Sequence Counter Split-Brain** — Instance counter created but static methods always used, corrupting per-device sequence numbers
2. **CrewSession Dual State** — `CrewService` singleton state is duplicated in both `useDashboardCrew` and `useCrewSession` React state without event wiring, causing dashboard desync
3. **Session Tracking Dual Queue** — `useSessionTracking` and `useGlobalTelemetry` maintain competing offline queues for session data under different AsyncStorage keys

**Batch:** `BATCH:split-brain-eradication`
**Status:** `[✅ VERIFIED]`

---

## Source of Truth Files
- `src/protocols/ZenggeAdapter.ts` — Line 167 (PROTOCOL_CORE-004)
- `src/hooks/useDashboardCrew.ts` — Line 39 (R-21-001)
- `src/hooks/useCrewSession.ts` — Line (R-21-001)
- `src/hooks/useSessionTracking.ts` — Line 24 (R-21-002)
- `src/hooks/useGlobalTelemetry.ts` — (R-21-002 related)
- `src/services/SpeedTrackingService.ts` — Line 56 (R-24-003)
- Raw audit: `R-21_findings.json`, `DOMAIN_PROTOCOL_CORE_findings.json`

---

## Findings

### CRITICAL — ZenggeAdapter Sequence Counter Corruption (PROTOCOL_CORE-004)
**File:** `src/protocols/ZenggeAdapter.ts:167`
```typescript
// CURRENT — instance created but never used
private readonly protocol = new ZenggeProtocol();
// All commands call static methods, bypassing instance counter
return this.toResult(ZenggeProtocol.setMultiColor(...), false);
```
**Impact:** Per-device sequence numbers are globally shared via static methods. Multiple connected devices will get colliding sequence numbers, causing the controller to reject commands.

**Fix:** Refactor `ZenggeProtocol` command methods to expose instance variants. Update `ZenggeAdapter` to call `this.protocol.setMultiColor(...)`.

> [!CAUTION]
> This is a BLE protocol correctness bug, not a code style issue. Multi-device setups with ZenggeAdapter may be experiencing silent command drops right now.

### HIGH — CrewSession Dual State (R-21-001)
**Files:** `src/hooks/useDashboardCrew.ts`, `src/hooks/useCrewSession.ts`
Both hooks maintain local `useState` for `crewSession` and `crewRole`. When `CrewService` mutations occur, neither hook receives the update.

**Fix:** Remove `useState` from both hooks. Replace `CrewService` state management with an `EventEmitter`-based external store pattern. Both hooks subscribe to a single `CrewService` event stream.

### HIGH — Session Tracking Dual Queue (R-21-002)
**Files:** `src/hooks/useSessionTracking.ts`, `src/services/SpeedTrackingService.ts`
- `useSessionTracking` uses key `@sk8lytz_pending_sessions`
- `SpeedTrackingService` uses key `@SK8Lytz_PendingSession_Queue`
Two separate offline queues for the same data → duplicate database inserts.

**Fix:** Remove `useSessionTracking.ts` entirely. Route all session persistence through `useGlobalTelemetry` → `SpeedTrackingService` → single queue key.

---

## Implementation Steps (Ordered by Risk)

### Phase 1 — ZenggeAdapter Sequence Counter (Highest Risk, Smallest Scope)
1. Read `src/protocols/ZenggeProtocol.ts` — identify which methods are currently static vs instance
2. Add instance method variants for `setMultiColor`, `setPower`, `setEffect`, `setSpeed`, `setBrightness`
3. Update `ZenggeAdapter` to call `this.protocol.<method>()` instead of `ZenggeProtocol.<method>()`
4. Run BLE lab test: confirm sequence numbers increment correctly per adapter instance

### Phase 2 — Session Tracking Consolidation
1. Read `src/hooks/useSessionTracking.ts` — audit all callers (grep for `useSessionTracking`)
2. Ensure `useGlobalTelemetry` + `SpeedTrackingService` covers all tracked metrics
3. Remove `useSessionTracking.ts`
4. Remove `@sk8lytz_pending_sessions` key reference from all files
5. Update `R-24-003`: remove duplicate key from `SpeedTrackingService`, import from `storageKeys.ts`

### Phase 3 — CrewSession Event Store (Largest Scope — requires architecture)
1. Add `EventEmitter` (Node native) or simple pub/sub to `CrewService`
2. Emit events on: `sessionStarted`, `sessionEnded`, `roleChanged`, `leadershipTransferred`
3. Refactor `useDashboardCrew` + `useCrewSession` to subscribe to events
4. Remove duplicate `useState` declarations from both hooks
5. Verify `DashboardScreen` updates correctly after `CrewService` mutations

---

## Verification
- `npm run verify` (TSC must pass)
- BLE Lab: Send multi-device pattern — confirm sequence numbers in packets are distinct per device
- Crew session test: transfer leadership → confirm `DashboardScreen` updates within 1 render cycle
- AsyncStorage audit: `grep -rn "pending_sessions\|PendingSession" src/` — should show single key

## Kanban Task Tags
- `[Status: 🔥 ON DECK]`
- `[Verification Status: ✅ VERIFIED]`
- `[Layer: BLE]`
- `[Risk: H-RISK]`
- `[Size: Banquet]`
- `[Cognitive Load: High]`
- `[BATCH: split-brain-eradication]`
