# PLAN: perf/pattern-payload-memoize

## Goal
Memoize the output of `buildPatternPayload` (called inside `applyFixedPattern` in `useControllerDispatch.ts`) keyed on `(patternId, fgColor, bgColor, speed, brightness)` so that repeat taps on the same pattern return a cached byte array instead of recomputing the full PatternEngine pipeline (~30-80ms on complex patterns).

## Problem
Every pattern tap calls `applyFixedPattern` → `buildPatternPayload()` synchronously on the JS thread before the BLE debounce timer even starts. `PatternEngine.ts` is 92KB / 1,464 lines. For SOULZ at 43 LEDs with a complex multi-segment pattern, this is a meaningful synchronous CPU spike that runs in addition to (not instead of) the 50ms write debounce.

## Root Cause
- `buildPatternPayload(patternId, fgHex, bgHex, ledCount, speed, brightness)` → pure function, no caching
- Same inputs always produce identical output → perfect memoization candidate
- Called on every tap even if user taps the same pattern multiple times

## Target File
`src/hooks/useControllerDispatch.ts` (or wherever `buildPatternPayload` is called within the dispatch hook)

## Execution Checklist

### Step 1: Read useControllerDispatch.ts
Locate exactly where `buildPatternPayload` is called. Confirm the call signature and all input parameters.

### Step 2: Add a module-level LRU cache (size 8 is sufficient)
```ts
// Outside the hook — module-level cache
const payloadCache = new Map<string, number[]>();
const MAX_CACHE = 8;

function getCachedPayload(
  patternId: number, fg: string, bg: string, ledCount: number, speed: number, brightness: number
): number[] {
  const key = `${patternId}|${fg}|${bg}|${ledCount}|${speed}|${brightness}`;
  if (payloadCache.has(key)) return payloadCache.get(key)!;
  const payload = buildPatternPayload(patternId, fg, bg, ledCount, speed, brightness);
  if (payloadCache.size >= MAX_CACHE) {
    // Evict oldest entry
    payloadCache.delete(payloadCache.keys().next().value);
  }
  payloadCache.set(key, payload);
  return payload;
}
```

### Step 3: Replace `buildPatternPayload(...)` call with `getCachedPayload(...)`
Surgical replacement — one line.

### Step 4: Verify cache hit rate
Add a temporary log: `AppLogger.log('PAYLOAD_CACHE', { hit: payloadCache.has(key) })`. Confirm repeated taps show `hit: true`.

### Step 5: TSC from master

## Rollback
Replace `getCachedPayload` call back to `buildPatternPayload`. Cache is module-level — it's simply ignored.

## Collateral Damage Locks
- DO NOT modify PatternEngine.ts
- DO NOT modify buildPatternPayload's signature
- ONLY add the cache wrapper at the call site in useControllerDispatch.ts
- Cache size MUST be capped — do NOT use unbounded Map growth
