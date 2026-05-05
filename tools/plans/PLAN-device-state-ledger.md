# PLAN: feat/device-state-ledger

## Goal
Implement a `useDeviceStateLedger` hook as the **single source of truth** for per-device pattern state, replacing the current fragmented dual-storage system with a unified, structured ledger.

## Problem Statement

The current system stores pattern state in three disconnected silos:

| Silo | What It Stores | Problem |
|---|---|---|
| `lastSentPayload` (React state) | Raw BLE bytes | Volatile — dies on app kill |
| `@Sk8lytz_last_pattern_{id}` (AsyncStorage) | Raw BLE bytes + timestamp | Key mismatch: DashboardScreen uses Supabase composite ID, useBLE uses MAC |
| `lastGroupPatterns` (DashboardScreen state) | Pattern name string only | UI-only, no hardware payload |

**Result:** On reconnect, the restore silently fails due to key mismatch. The UI starts at default state (pattern 1) while hardware may be playing the cached pattern → user must "click twice" to get first pattern firing.

## Deliverables (3 phases, same worktree)

### Phase 1 — `useDeviceStateLedger` Hook (New File)
**File:** `src/hooks/useDeviceStateLedger.ts`

```ts
interface DevicePatternState {
  // Identity
  deviceMac: string;       // Always: MAC.toUpperCase(), no composite keys
  groupId?: string;

  // Structured (for UI pre-warm and dashboard preview)
  mode: ControllerMode;
  patternId?: number;
  patternLabel: string;    // e.g. "Pro Effects – Crimson"
  fgColor?: string;
  bgColor?: string;
  speed: number;
  brightness: number;
  builderNodes?: BuilderNode[];
  builderFillMode?: 'GRADIENT' | 'SOLID';
  builderTransitionType?: number;

  // Raw (for immediate BLE hardware replay)
  rawPayload: number[];

  // Metadata
  ts: number;              // Date.now() of last write
}
```

**Storage key:** `@SK8Lytz_DeviceState_v2_{MAC}` (new key, avoids collision with old entries)

**Hook API:**
```ts
const ledger = useDeviceStateLedger();

ledger.save(mac: string, state: DevicePatternState): Promise<void>
ledger.load(mac: string): Promise<DevicePatternState | null>
ledger.loadSync(mac: string): DevicePatternState | null  // from in-memory cache
ledger.clear(mac: string): Promise<void>
```

**Implementation notes:**
- In-memory `Map<string, DevicePatternState>` cache for synchronous reads (dashboard cards)
- Debounced AsyncStorage write (500ms) — rapid slider changes don't hammer storage
- No TTL — state is valid indefinitely (the hardware just might be off)
- Add `isStale(ts: number): boolean` helper: > 24h returns true, used for UI badge only

---

### Phase 2 — DockedController Boot Pre-warm (Fixes "Click Twice")
**File:** `src/components/DockedController.tsx`

**Current boot flow (broken):**
```
Controller mounts → default state → BLE fires cached payload → UI/HW out of sync
User taps pattern A → DockedController state already AT pattern A → no effect!
User taps pattern A again → now it's a state change → fires ✓
```

**New boot flow:**
```
Controller mounts
  → ledger.load(mac) → DevicePatternState
  → Initialize useState with restored mode/patternId/colors/speed/brightness
  → UI and hardware start aligned
  → useEffect([isConnected]): when isPaired flips true → fire rawPayload once (300ms delay)
```

**Key constraint:** The init-from-ledger MUST happen in `useState` initializers (lazy init), NOT in a `useEffect` — otherwise there's still one render with default state before the effect fires.

```ts
// CORRECT pattern: lazy useState initializer
const [selectedPatternId, setSelectedPatternId] = useState<number>(
  () => ledger.loadSync(mac)?.patternId ?? 1
);
```

**Collateral Damage Lock:** Do NOT touch `useControllerPersistence`, `useControllerDispatch`, or `loadFavorite`. Only the `useState` initializers and the connect `useEffect` are in scope.

---

### Phase 3 — Dashboard Card Pattern Preview (Visual)
**Files:**
- `src/screens/DashboardScreen.tsx` (read from ledger for each card)
- Device/Group card render component (add mini-preview swatch)

**Implementation:**
- Each device card reads `ledger.loadSync(mac)` on render
- If state exists: show `patternLabel` text + a 20px color swatch (fgColor or gradient preview)
- If `isStale(ts)`: show with reduced opacity
- If no state: show "—" placeholder

**Zero-collateral rule:** Do NOT modify `renderItem` logic beyond adding the ledger read. Do NOT touch BLE connection code.

---

## Key Architecture Decision: MAC Normalization

**The Golden Rule:** All storage keys MUST use the raw BLE MAC address, uppercase, stripped of any composite suffixes.

```ts
// ALWAYS normalize before any ledger operation
const normalizeMac = (rawId: string): string =>
  rawId.split('_')[0].toUpperCase().replace(/[^A-F0-9:]/g, '');
```

This kills Gap 1 permanently: write and read paths will always produce identical keys regardless of what format the device ID arrives in from DashboardScreen vs useBLE.

---

## Migration

- Old `@Sk8lytz_last_pattern_*` keys in AsyncStorage: leave them in place (no migration script). The new `_v2_` suffix naturally coexists. Old keys expire passively over time.
- `useBLE.ts` PATTERN_CACHE_KEY: leave the existing reconnect restore for now as a fallback. Phase 2 renders it redundant, but removing it is a separate chore.

---

## Verification Plan

1. **Unit smoke test:** `ledger.save(mac, state)` → `ledger.load(mac)` → assert roundtrip equality
2. **Key normalization test:** `normalizeMac('AABBCCDDEE_userId123')` === `'AABBCCDDEE'`
3. **Controller pre-warm:** Open controller for previously-connected device → verify UI shows last mode/pattern without touching anything
4. **Click-twice regression:** Tap a pattern immediately on first controller open → verify it fires first time, every time
5. **Dashboard preview:** Disconnect device → return to dashboard → verify card shows last patternLabel + color swatch
6. **Stale state:** Manually set `ts` to 25h ago → verify card shows reduced opacity label
