# Implementation Plan: refactor/ble-typed-fsm-gate

## Goal
Replace the weakly-typed `bleGateRef: MutableRefObject<string>` guard with a compile-time-enforced discriminated union FSM. The current string-ref approach allows any code to set `bleGateRef.current = 'IDLE'` at any time, bypassing all gate checks. A typed FSM makes illegal state transitions impossible at the TypeScript level.

## Background (Cited Truth)

Current state management in `useBLE.ts`:
```typescript
// useBLE.ts L61-62 (CURRENT)
const bleGateRef = useRef<'IDLE' | 'SCANNING' | 'CONNECTING' | 'DISCONNECTING' | 'RECOVERING'>('IDLE');
const [bleGateState, setBleGateState] = useState<typeof bleGateRef.current>('IDLE');
```

Both the ref AND the state track the same value — dual-tracking that creates drift opportunities. The ref is passed to child hooks (`useBLEAutoRecovery`, `useBLESweeper`, `useDashboardAutoConnect`) as a raw `MutableRefObject<string>`, losing all type safety.

## Proposed Architecture

### [NEW] `src/services/BleStateMachine.ts`

```typescript
type BLEPhase =
  | { tag: 'IDLE' }
  | { tag: 'SCANNING'; sweeperId: number }
  | { tag: 'CONNECTING'; targetMacs: string[] }
  | { tag: 'CONNECTED'; sessionCount: number }
  | { tag: 'DISCONNECTING' }
  | { tag: 'RECOVERING'; ghostedMacs: string[] };

// Legal transitions — the compiler enforces these
type BLETransition =
  | { from: 'IDLE'; to: 'SCANNING' | 'CONNECTING' }
  | { from: 'SCANNING'; to: 'IDLE' | 'CONNECTING' }
  | { from: 'CONNECTING'; to: 'CONNECTED' | 'IDLE' }
  | { from: 'CONNECTED'; to: 'DISCONNECTING' | 'RECOVERING' }
  | { from: 'DISCONNECTING'; to: 'IDLE' }
  | { from: 'RECOVERING'; to: 'CONNECTED' | 'IDLE' };

function transition(current: BLEPhase, action: BLEAction): BLEPhase;
```

### Key Benefits
1. **Impossible to write to a device if not CONNECTED** — the `sessions` Map only exists on the `CONNECTED` variant.
2. **No dual-tracking** — single source of truth replaces both `bleGateRef` and `bleGateState`.
3. **Auditable transitions** — every state change logs `from → to` with metadata.
4. **Child hooks receive typed phase** — `useBLEAutoRecovery` can pattern-match on `phase.tag === 'RECOVERING'` instead of checking a string.

## Dependencies
- This refactor depends on `refactor/ble-session-factory` (Phase 2) and should be done AFTER `refactor/useBLE-god-object-surgery` (Phase 4).
- The FSM becomes the backbone of the refactored `useBLE.ts` orchestrator.

## Risk Assessment
- **H-RISK**: Changes the fundamental state management contract across 5+ files.
- **Mitigation**: The FSM exposes the same gate values for backward compatibility. Old `bleGateRef.current === 'IDLE'` checks can be migrated incrementally by reading `phase.tag === 'IDLE'`.

## Verification Plan

### Automated Tests
- `npm run verify` — TypeScript enforcement is the primary verification (compile errors = illegal transitions caught).
- Unit test the `transition()` function with exhaustive state × action matrix.

### Manual Verification
- Full BLE lifecycle smoke test on physical hardware.
- Verify logcat shows clean `IDLE → CONNECTING → CONNECTED → DISCONNECTING → IDLE` transitions.
