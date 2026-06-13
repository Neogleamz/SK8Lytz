# PLAN: Hardware Segment Mirror Verification & Emergency Pattern Fix

**Slug**: `fix/led-count-segments-consistency`
**Created**: 2026-04-22
**Status**: 🔲 Not Started — waiting on hardware test
**Risk**: H-RISK (protocol correctness — wrong array = wrong hardware behavior)
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Background: The Three-Layer LED Model

Established 2026-04-22 in hardware brainstorming session. Full table in `SK8Lytz_App_Master_Reference.md §1`.

| Layer | Term | HALOZ | SOULZ |
|-------|------|-------|-------|
| Canvas | `ledPoints` | **8** | **43** |
| Hardware Mirror | `segments` | **2** | **1** |
| Physical LEDs | `physicalTotal` | **16** | **86 (Y-wired)** |

**HALOZ key insight**: The controller receives an 8-LED pixel array and auto-mirrors it to its second segment. Sending 16 elements bypasses the mirroring engine entirely — each segment gets its own independent half of the 16, which is NOT the same as a mirror.

**SOULZ key insight**: 43 `ledPoints` → 86 physical LEDs via Y-wire (two strips soldered to same data pin). Controller only knows 43. App uses `ledPoints` as-is. Cut-to-length users can reduce via HW Settings.

---

## Step 1: Hardware Mirror Verification Test (GATE — DO THIS FIRST)

This is a **hardware-gated** decision. We cannot change `applyEmergencyPattern` until we confirm the auto-mirror behavior.

### Test Procedure
1. Build a debug APK with the Oracle Lab accessible
2. In `Sk8LytzDiagnosticLab`, use the `0x59` builder tab
3. Set `numPoints = 8`, fill with: `[RED, RED, RED, RED, BLUE, BLUE, BLUE, BLUE]` (first half RED, second half BLUE)
4. Set `transitionType = 0x01` (FREEZE — static hold)
5. Send to connected HALOZ device

**Expected result if auto-mirror is correct**: ALL 16 physical LEDs show the same 8-pixel pattern mirrored — i.e., first 8 physical LEDs = `[R,R,R,R,B,B,B,B]` AND second 8 physical LEDs = same `[R,R,R,R,B,B,B,B]`.

**If old 16-element behavior was happening instead**: The second 8 LEDs would be independent BG fill or different.

### Oracle Lab Builder Config
```
Protocol: 0x59
numPoints: 8
Pixels: RED(FF0000) x4, BLUE(0000FF) x4
transitionType: 0x01 (FREEZE)
speed: 50
direction: 1
```

**Record result in `ZENGGE_PROTOCOL_BIBLE.md`** under segment mirror section before proceeding.

---

## Step 2: Fix `applyEmergencyPattern` HALOZ Path

**File**: `src/hooks/useControllerDispatch.ts`
**Lines**: ~142–160 (the emergency pattern builder)

### Current (WRONG) code:
```typescript
// HALOZ path — WRONG: builds 16 elements, bypasses segment engine
const frame8 = [RED, RED, RED, RED, AMBER, AMBER, AMBER, AMBER];
const mirror8 = [...frame8];
const fullFrame = [...frame8, ...mirror8]; // 16 elements total
```

### Fixed code (after hardware confirmation):
```typescript
// HALOZ path — CORRECT: send 8 elements, hardware mirrors to segment 2
// Hardware truth confirmed 2026-04-22: HALOZ segments=2 auto-mirrors 8-point array
const frame = Array(hwSettings.ledPoints).fill(null).map((_, i) => {
  const half = Math.floor(hwSettings.ledPoints / 2);
  return i < half ? RED : AMBER;  // First half red, second half amber
});
const payload = ZenggeProtocol.setMultiColor(frame, speed, 1, 0x01); // FREEZE
writeToDevice(payload);
```

---

## Step 3: Fix SOULZ Path

SOULZ currently hardcodes 16 elements for emergency pattern too. Fix:
```typescript
// SOULZ path — use hwSettings.ledPoints (default 43, user-adjustable)
const frame = Array(hwSettings.ledPoints).fill(null).map((_, i) => {
  const third = Math.floor(hwSettings.ledPoints / 3);
  if (i < third) return RED;
  if (i < third * 2) return AMBER;
  return RED;
});
```

---

## Step 4: Verify `numLEDs` Source in `useControllerDispatch`

Confirm `numLEDs` at `useControllerDispatch.ts:84` comes from `hwSettings.ledPoints` not a hardcoded constant.

```powershell
# Grep check
Get-ChildItem src -Recurse -Include *.ts,*.tsx | Select-String "numLEDs" | Select-Object Filename, LineNumber, Line
```

---

## Files To Touch

| File | Change |
|------|--------|
| `src/hooks/useControllerDispatch.ts` | Fix HALOZ + SOULZ emergency pattern pixel builders |
| `tools/ZENGGE_PROTOCOL_BIBLE.md` | Record segment mirror hardware test result |
| `tools/SK8Lytz_TEST_PLAN.md` | Add manual test case for emergency pattern on HALOZ |

---

## Test Criteria

- [ ] Hardware test: 8-element 0x59 FREEZE sent to HALOZ → both physical segments show identical pattern
- [ ] Emergency pattern fires on HALOZ: 8 RED dots + 8 AMBER dots (via mirror)
- [ ] Emergency pattern fires on SOULZ: full `ledPoints`-length array, no hardcodes
- [ ] Cut-SOULZ user (e.g. `ledPoints=36`): emergency pattern fills 36 LEDs correctly
- [ ] No change to normal (non-emergency) pattern dispatch
- [ ] `npx tsc --noEmit` zero errors after change
