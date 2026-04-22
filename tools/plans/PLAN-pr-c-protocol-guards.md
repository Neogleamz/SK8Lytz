# PLAN: PR-C — Protocol Guards & 0x41 Settled Mode

**Slug**: `fix/pr-c-protocol-guards`
**Commit Group**: PR-C — isolated fixes, each touches a different file
**Created**: 2026-04-22
**Status**: 🔲 Not Started
**Risk**: M-RISK (guarding dangerous opcodes + format correction)
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Covers These Tasks

1. `fix/dangerous-opcode-0x43-guard` — guard 0x43 which crashes hardware
2. `fix/rbm-effect-id-ceiling-0x42` — clamp RBM effect IDs to confirmed range
3. `fix/legacy-0x38-opcode-audit` — confirm 0x38 is not used as effect opcode
4. `fix/0x41-settled-mode-correct-format` — fix 0x41 payload format (BLOCKS Symphony Engine)

---

## Fix 1: `fix/dangerous-opcode-0x43-guard`

**File**: `src/protocols/ZenggeProtocol.ts`
**Severity**: H-RISK — sending 0x43 to 0xA3 hardware causes firmware fault / strip goes dark

### Oracle Lab Confirmed Result
Physical hardware test 2026-04-22: `0x43` packet sent → LED strip immediately goes dark, does not recover until power cycle. **DO NOT SEND IN PRODUCTION.**

### The Fix
Find `ZenggeProtocol.setCustomModeSequence()` or whatever method builds `0x43` packets. Add a hard guard:

```typescript
/**
 * @HARDWARE-DANGER: 0x43 CONDEMNED — causes firmware fault on 0xA3 hardware.
 * Oracle Lab confirmed 2026-04-22: strip goes dark on receipt, requires power cycle.
 * This method MUST NOT be called outside of explicit diagnostic mode.
 * @see tools/ZENGGE_PROTOCOL_BIBLE.md — 0x43 section
 */
static setMultiSequence(...): number[] {
  if (!__DEV__ || !ZenggeProtocol.DIAGNOSTIC_MODE_ENABLED) {
    throw new Error('0x43 HARDWARE-DANGER: Condemned opcode. Only callable in diagnostic mode.');
  }
  // ... existing implementation
}
```

Also add `DIAGNOSTIC_MODE_ENABLED` static flag:
```typescript
static DIAGNOSTIC_MODE_ENABLED = false; // Set to true only in Sk8LytzDiagnosticLab
```

Ensure `Sk8LytzDiagnosticLab.tsx` sets `ZenggeProtocol.DIAGNOSTIC_MODE_ENABLED = true` before using it, and resets to false on unmount.

### Additional ESLint Guard (Optional)
Add to `.eslintrc.js` or `eslint.config.js`:
```javascript
// Prevent accidental 0x43 usage
'no-restricted-syntax': ['error', {
  selector: 'CallExpression[callee.property.name="setMultiSequence"]',
  message: '0x43 is condemned for 0xA3 hardware. Use ZenggeProtocol.DIAGNOSTIC_MODE_ENABLED guard.'
}]
```

---

## Fix 2: `fix/rbm-effect-id-ceiling-0x42`

**File**: `src/protocols/ZenggeProtocol.ts` — `setCustomRbm()` method
**Severity**: M-RISK — IDs >100 play undocumented hardware effects, outside confirmed range

### Oracle Lab Finding
Effect ID 101 is soft-accepted by hardware (doesn't crash) but is outside the confirmed range `1–100` for 0xA3. Behavior is undefined.

### The Fix
```typescript
static setCustomRbm(effectId: number, speed: number, brightness: number): number[] {
  // Clamp to confirmed 0xA3 hardware range
  const clampedId = Math.min(100, Math.max(1, Math.round(effectId)));
  if (clampedId !== effectId) {
    AppLogger.warn('ZenggeProtocol.setCustomRbm: effectId clamped', { original: effectId, clamped: clampedId });
  }
  // ... rest of existing implementation using clampedId
}
```

---

## Fix 3: `fix/legacy-0x38-opcode-audit`

**Action**: Grep audit only — no code change expected
**Severity**: M-RISK — if 0x38 IS found as an effect command, it's wrong for 0xA3 hardware

### What 0x38 Is
From APK: `0x38` appears ONLY in `C14187d.m4724d()` which routes to **legacy/non-0xA3 device types**. The correct opcode for 0xA3 effect patterns is `0x42`.

### The Audit
```powershell
# Run from project root — should return ZERO results for effect usage
Get-ChildItem -Path src -Recurse -Include *.ts,*.tsx |
  Select-String "0x38" |
  Where-Object { $_.Line -notmatch "comment|//" } |
  ForEach-Object { "$($_.Filename):$($_.LineNumber): $($_.Line.Trim())" }
```

**If zero results**: Log the audit result in `ZENGGE_PROTOCOL_BIBLE.md` as confirmed clean. Task done.

**If any results found**: STOP. Flag immediately. Do not merge PR-C until resolved.

---

## Fix 4: `fix/0x41-settled-mode-correct-format`

**File**: `src/protocols/ZenggeProtocol.ts`
**Severity**: M-RISK — BLOCKS all of EPIC-004 Phase 2 (Symphony Effect Engine)

### The Bug
Oracle Lab Phase 2 panel test failed — the `0x41` payload sent was wrong format and hardware did not respond with the expected Symphony effect.

### Correct 13-Byte Format (from `C7775l.java` APK)
```
[0x41, effectId, FG.R, FG.G, FG.B, BG.R, BG.G, BG.B, speed, dir, 0x00, 0xF0, checksum]
```

Byte breakdown:
- `0x41` — opcode
- `effectId` — 1–33 (Symphony effect ID)
- `FG.R, FG.G, FG.B` — foreground color (0–255 each)
- `BG.R, BG.G, BG.B` — background color (0–255 each)
- `speed` — 1–255
- `dir` — `0x00` = forward, `0x01` = reverse
- `0x00, 0xF0` — **static trailer bytes** (confirmed from APK — do not change)
- `checksum` — `ZenggeProtocol.calculateChecksum(raw[0..11])`

### Implementation

```typescript
/**
 * 0x41 Settled Mode — dual-color animated Symphony effects.
 * Used by the Symphony tab (33 built-in hardware effects).
 * 
 * Hardware truth from C7775l.java (APK 2026-04-21):
 * Format: [0x41, effectId, FG.R, FG.G, FG.B, BG.R, BG.G, BG.B, speed, dir, 0x00, 0xF0, checksum]
 * 
 * @param effectId - Symphony effect 1–33
 * @param fg - Foreground color (for effects that use FG+BG or FG-only)
 * @param bg - Background color (only used by effects with FG_BG color gate)
 * @param speed - 1–255
 * @param direction - 0=forward, 1=reverse
 */
static setSettledMode(
  effectId: number,
  fg: { r: number; g: number; b: number },
  bg: { r: number; g: number; b: number },
  speed: number,
  direction: 0 | 1 = 0
): number[] {
  const raw = [
    0x41,
    effectId & 0xFF,
    Math.min(255, Math.max(0, fg.r | 0)),
    Math.min(255, Math.max(0, fg.g | 0)),
    Math.min(255, Math.max(0, fg.b | 0)),
    Math.min(255, Math.max(0, bg.r | 0)),
    Math.min(255, Math.max(0, bg.g | 0)),
    Math.min(255, Math.max(0, bg.b | 0)),
    Math.max(1, Math.min(255, speed | 0)),
    direction & 0x01,
    0x00,  // static trailer byte 1 — DO NOT CHANGE
    0xF0,  // static trailer byte 2 — DO NOT CHANGE
  ];
  raw.push(ZenggeProtocol.calculateChecksum(raw));
  return ZenggeProtocol.wrapCommand(raw);
}
```

### Verify via Oracle Lab BEFORE Merging
1. Open `Sk8LytzDiagnosticLab` → Phase 2 tab → `0x41` panel
2. Set `effectId=1`, `FG=red`, `BG=blue`, `speed=50`, `dir=forward`
3. Send to HALOZ
4. **Expected**: Hardware animates Symphony effect 1 with red foreground on blue background
5. Record result in `ZENGGE_PROTOCOL_BIBLE.md` before merging

---

## Execution Order Within PR-C

```
1. Fix 3 (0x38 audit) — run grep, document result, zero code change
2. Fix 2 (RBM ceiling) — add input clamp, 3 lines
3. Fix 1 (0x43 guard) — add runtime guard + JSDoc warning
4. Fix 4 (0x41 format) — implement/replace setSettledMode(), Oracle Lab verify
```

---

## Files To Touch

| File | Change |
|------|--------|
| `src/protocols/ZenggeProtocol.ts` | All 4 fixes — guard 0x43, clamp RBM, implement setSettledMode() |
| `tools/ZENGGE_PROTOCOL_BIBLE.md` | Record 0x38 audit result + 0x41 verified format |

---

## Test Criteria

- [ ] `ZenggeProtocol.setMultiSequence()` throws Error if called outside diagnostic mode
- [ ] `Sk8LytzDiagnosticLab` can still call `setMultiSequence()` (DIAGNOSTIC_MODE_ENABLED flag works)
- [ ] `setCustomRbm(150, ...)` logs warning and clamps to 100
- [ ] `setCustomRbm(0, ...)` clamps to 1
- [ ] `0x38` audit returns zero production usage results
- [ ] Oracle Lab: `setSettledMode(1, red, blue, 50, 0)` → hardware plays Symphony effect 1 with colors
- [ ] `npx tsc --noEmit` — zero errors
