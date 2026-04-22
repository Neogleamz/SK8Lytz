# PLAN: PR-A Protocol Bug Fixes

**Slug**: `fix/pr-a-protocol-bugs`
**Commit Group**: PR-A — touches `ZenggeProtocol.ts`, `useMusicMode.ts`, `useControllerDispatch.ts`, `useBLEScanner.ts`
**Created**: 2026-04-22
**Status**: 🔲 Not Started
**Risk**: H-RISK (protocol correctness — wrong bytes = broken hardware behavior)
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Covers These Tasks

1. `fix/mock-product-id-ble-scanner` — hardcoded wrong product_id in BLE scanner
2. `fix/music-mic-source-wrong-values` — wrong 0x73 mic source bytes
3. `fix/music-mode-exit-packet` — no isOn=false exit packet on mode switch
4. `fix/emergency-pattern-led-count` — hardcoded 16 LEDs in emergency pattern

Do all 4 in one PR since they touch overlapping files.

---

## Fix 1: `fix/mock-product-id-ble-scanner`

**File**: `src/hooks/ble/useBLEScanner.ts:313`
**Severity**: H-RISK — every device hitting fallback path is misidentified

### The Bug
```typescript
// CURRENT — WRONG: 0x73 = 115 decimal, identifies device as wrong family
product_id: 115
```

### The Fix
```typescript
// CORRECT: 0xA3 = 163 decimal — ALL SK8Lytz controllers confirmed 0xA3
product_id: 163
```

**One line. Ship immediately. No side effects.**

---

## Fix 2: `fix/music-mic-source-wrong-values`

**Files**: `src/protocols/ZenggeProtocol.ts`, `src/hooks/useMusicMode.ts`
**Severity**: H-RISK — OLD values cause hardware to return blank/no response

### The Bug
Current code uses legacy values (`0x00` = App mic, `0x01` = Device mic) — these are WRONG for 0xA3 hardware.

### APK Ground Truth (`MusicModeFragment.java:752`)
```
38 (0x26) = App/phone microphone
39 (0x27) = Device/hardware microphone
```

### Oracle Lab Confirmation
Sending `MIC=0x00` causes blank hardware response. `MIC=0x26` fires correctly.

### Fix in `ZenggeProtocol.ts`
Search for mic source constants and update:
```typescript
// WRONG (old)
static readonly MIC_APP = 0x00;
static readonly MIC_DEVICE = 0x01;

// CORRECT (APK confirmed)
static readonly MIC_APP = 0x26;    // 38 decimal — phone/app mic
static readonly MIC_DEVICE = 0x27; // 39 decimal — on-device mic
```

### Fix in `useMusicMode.ts`
Any hardcoded `0x00` or `0x01` mic references must use the new constants. Grep check:
```powershell
Get-ChildItem src -Recurse -Include *.ts,*.tsx | Select-String "0x00|0x01" | Where-Object { $_.Line -match "mic|Mic|MIC" }
```

---

## Fix 3: `fix/music-mode-exit-packet`

**File**: `src/hooks/useControllerDispatch.ts:184`
**Severity**: H-RISK — hardware stays in music-reactive mode after mode switch

### The Bug
When user switches away from music mode, the hardware is never told to stop. It stays internally in music-reactive mode — ambient sound still triggers LED flashes even though the UI has moved on.

### Root Cause
`useMusicMode.ts:72` and `useControllerDispatch.ts:184` only ever call `setMusicConfig(..., isOn: true)`. There is no exit path that sends `isOn: false`.

### The Fix
In `useControllerDispatch.ts`, find the mode change handler or the music mode cleanup effect, and add:

```typescript
// When leaving MUSIC mode, send an explicit exit packet
const exitMusicMode = useCallback(() => {
  if (!writeToDevice) return;
  const exitPayload = ZenggeProtocol.setMusicConfig(
    currentMusicMode,    // keep current mode ID
    micSource,           // keep current mic source
    false,               // isOn: FALSE — this is the key change
    musicPrimaryColor,
    musicSecondaryColor,
    micSensitivity,
    brightness
  );
  writeToDevice(exitPayload);
}, [writeToDevice, currentMusicMode, micSource, musicPrimaryColor, musicSecondaryColor, micSensitivity, brightness]);

// Call exitMusicMode() when activeMode changes away from 'MUSIC'
useEffect(() => {
  if (previousActiveMode === 'MUSIC' && activeMode !== 'MUSIC') {
    exitMusicMode();
  }
}, [activeMode]);
```

> **Pattern**: Use a `useRef` to track `previousActiveMode` if not already available.

---

## Fix 4: `fix/emergency-pattern-led-count`

**File**: `src/hooks/useControllerDispatch.ts:142-160`
**Severity**: M-RISK — DEPENDS ON hardware mirror verification from `fix/led-count-segments-consistency`
**Do NOT merge this until** the hardware test confirms 8-element arrays auto-mirror on HALOZ.

### The Bug
Emergency pattern (hazard lighting) hardcodes 16 LED elements for HALOZ and 16 for SOULZ, ignoring actual `ledPoints`.

### Current (WRONG) pattern for HALOZ:
```typescript
const frame8 = [RED, RED, RED, RED, AMBER, AMBER, AMBER, AMBER];
const mirror8 = [...frame8]; // Manual mirror — BYPASSES hardware segment engine
const fullFrame = [...frame8, ...mirror8]; // 16 elements
```

### Fixed HALOZ:
```typescript
// Hardware truth: HALOZ ledPoints=8, segments=2 → send 8, hardware mirrors
const halfLen = Math.floor(hwSettings.ledPoints / 2);
const frame = [
  ...Array(halfLen).fill({ r: 255, g: 0, b: 0 }),   // RED: half strip
  ...Array(hwSettings.ledPoints - halfLen).fill({ r: 255, g: 165, b: 0 }) // AMBER: rest
];
const payload = ZenggeProtocol.setMultiColor(frame, speed, 1, 0x01); // FREEZE
writeToDevice(payload);
```

### Fixed SOULZ and all other products:
```typescript
// Use hwSettings.ledPoints — respects cut-to-length customization
const third = Math.floor(hwSettings.ledPoints / 3);
const frame = [
  ...Array(third).fill({ r: 255, g: 0, b: 0 }),
  ...Array(third).fill({ r: 255, g: 165, b: 0 }),
  ...Array(hwSettings.ledPoints - (third * 2)).fill({ r: 255, g: 0, b: 0 }),
];
const payload = ZenggeProtocol.setMultiColor(frame, speed, 1, 0x01);
writeToDevice(payload);
```

---

## Execution Order Within PR-A

```
1. Fix 1 (product_id) — 1 line, zero risk, do first
2. Fix 2 (mic source values) — update constants, grep all usages
3. Fix 3 (music exit packet) — add useEffect in useControllerDispatch
4. Fix 4 (emergency LED count) — ONLY after hardware mirror test passes
```

---

## Files To Touch

| File | Fix |
|------|-----|
| `src/hooks/ble/useBLEScanner.ts:313` | Fix 1: product_id 115 → 163 |
| `src/protocols/ZenggeProtocol.ts` | Fix 2: MIC_APP/MIC_DEVICE constants |
| `src/hooks/useMusicMode.ts` | Fix 2: any hardcoded mic values |
| `src/hooks/useControllerDispatch.ts` | Fix 3: exit packet + Fix 4: emergency pattern |

---

## Test Criteria

- [ ] Scanner identifies HALOZ as `product_id: 163` in device list debug info
- [ ] Tapping "Device Mic" in music mode fires `MIC=0x27` (verify via BLE sniffer or Oracle Lab)
- [ ] Tapping "App Mic" fires `MIC=0x26`
- [ ] Switch from Music → Fixed Pattern: hardware stops reacting to ambient sound within 1–2 seconds
- [ ] Emergency pattern on HALOZ: 8 elements sent, both physical segments illuminate (after mirror test)
- [ ] Emergency pattern on SOULZ: `ledPoints`-length array, correct Red/Amber split
- [ ] `npx tsc --noEmit` — zero errors
