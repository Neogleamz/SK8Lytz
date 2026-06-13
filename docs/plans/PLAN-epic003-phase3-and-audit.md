# PLAN: EPIC-003 Phase 3 Remaining — Persist Product ID

**Slug**: `feat/persist-product-id-supabase-hw-screen`
**Created**: 2026-04-22
**Status**: 🔲 Not Started
**Risk**: L-RISK (additive — new column write + display only)
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Background

Every SK8Lytz controller is confirmed `product_id = 0xA3 = 163`. The BLE handshake already queries this value during device pairing. But the result is never stored persistently or shown in the UI.

Storing `product_id` enables per-device protocol routing in future builds — if we ever ship a device with a different IC (0x07, 0x33, etc.), the app can auto-detect and route to the correct protocol handler.

---

## Step 1: Store product_id in Supabase

### Where it gets queried
Find the location in `useBLE.ts` or `useBLEScanner.ts` where the device response to the product query (`0x71`) is parsed.

```typescript
// After parsing BLE response and extracting productId:
const productId = parseProductIdFromResponse(response); // returns 163 for 0xA3

// Write to registered_devices row for this device
await supabase
  .from('registered_devices')
  .update({ product_id: productId, product_id_confirmed_at: new Date().toISOString() })
  .eq('mac_address', device.macAddress);

AppLogger.log('PRODUCT_ID_STORED', { mac: device.macAddress, productId });
```

### Supabase Migration Required
```sql
-- Run via MCP apply_migration
ALTER TABLE registered_devices
  ADD COLUMN IF NOT EXISTS product_id INTEGER,
  ADD COLUMN IF NOT EXISTS product_id_confirmed_at TIMESTAMPTZ;
```

---

## Step 2: Display in DeviceSettingsModal

**File**: `src/components/DeviceSettingsModal.tsx`

Find the firmware/hardware info section. Add a product ID chip next to the firmware version badge:

```tsx
{/* Product ID chip — shown when available */}
{device.product_id && (
  <View style={styles.infoBadge}>
    <MaterialCommunityIcons name="chip" size={12} color={Colors.textMuted} />
    <Text style={styles.badgeText}>
      PRODUCT ID: 0x{device.product_id.toString(16).toUpperCase()} ({device.product_id})
    </Text>
  </View>
)}
```

For our hardware this will always display: **PRODUCT ID: 0xA3 (163)**

---

## Files To Touch

| File | Change |
|------|--------|
| `src/hooks/ble/useBLE.ts` or `useBLEScanner.ts` | Write product_id to Supabase after query |
| `src/components/DeviceSettingsModal.tsx` | Display product ID chip |
| Supabase migration | Add product_id + confirmed_at columns to registered_devices |

---

## Test Criteria

- [ ] Pair a HALOZ device → check `registered_devices` table in Supabase → `product_id = 163`
- [ ] Open DeviceSettingsModal → "PRODUCT ID: 0xA3 (163)" chip visible
- [ ] product_id column is NULL for old paired devices until they reconnect (graceful degradation)
- [ ] Chip hidden when `product_id` is null (not yet queried)
- [ ] `npx tsc --noEmit` — zero errors

---

## PLAN: PatternEngine Wiring Audit

**Slug**: `audit/verify-pattern-engine-wiring`
**Status**: 🔲 Not Started
**Risk**: L-RISK (read-only audit — no code changes expected)

---

## Purpose

Confirm end-to-end that a user tapping a pattern in the UI results in a correct `0x59` hardware write — no broken links, no stale closures, no wrong `numLEDs` value.

## Trace the Pipeline

### Step 1: Confirm `applyFixedPattern` dispatch chain
```
User taps pattern card in EffectsPanel/MultiModePanel
  → applyFixedPattern(patternId, fgHex, bgHex, speed)  [useControllerDispatch.ts:58]
  → buildPatternPayload(patternId, fgRGB, bgRGB, numLEDs, speed)  [PatternEngine.ts:302]
  → getHardwarePixelArray() OR buildCustomModePayload()  [PatternEngine.ts]
  → ZenggeProtocol.setMultiColor(pixels, speed, dir, transition)  [ZenggeProtocol.ts]
  → writeToDevice(payload)  [useBLE.ts]
```

### Step 2: Verify `numLEDs` source
Check `useControllerDispatch.ts:84`:
```typescript
const payload = buildPatternPayload(
  patternId, fgRaw, bgRaw,
  numLEDs,  // ← THIS: must come from hwSettings.ledPoints, NOT hardcoded
  clampSpeed(currentSpeed ?? 50), 1
);
```

Run in `__DEV__` mode and log the value:
```typescript
if (__DEV__) AppLogger.log('PATTERN_DISPATCH', { patternId, numLEDs, fg: fgRaw, bg: bgRaw });
```

Expected: HALOZ logs `numLEDs: 8`, SOULZ logs `numLEDs: 43`.

### Step 3: Confirm FG/BG re-fires on color change
Check `UniversalSlidersFooter.tsx:246` — when FG color picker changes, does it call `applyFixedPattern` with new color immediately?

### Step 4: Confirm Visualizer matches
- `ProductVisualizer.tsx` calls `getVisualizerFrame(patternId, fgRGB, bgRGB, numLEDs, animTick)`
- `CustomEffectVisualizer.tsx` also uses `getVisualizerFrame()`
- Both should use the same `fixedPatternId` and `numLEDs` as the hardware dispatch — NO discrepancy

## Document Results

After audit, add a brief result note to `tools/ZENGGE_PROTOCOL_BIBLE.md`:
```markdown
## Pattern Engine Wiring Audit (YYYY-MM-DD)
- numLEDs source confirmed: hwSettings.ledPoints ✅ / hardcoded 16 ❌
- FG/BG re-fires confirmed: ✅ / ❌
- Visualizer in sync with hardware: ✅ / ❌
- All 28 patterns produce valid arrays: ✅ / ❌
```
