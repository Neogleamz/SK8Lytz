# EPIC-003: 0xA3 Protocol True-Up — Linked Plan

> **Status**: ACTIVE — Lab-first strategy. Verify before implement.
> **APK Source**: `ZENGGE_APK/ZENGGE_DECOMPILED/` (ZENGGE 1.5.0 decompiled)
> **Protocol Bible**: `tools/ZENGGE_PROTOCOL_BIBLE.md`
> **Master Reference**: `tools/SK8Lytz_App_Master_Reference.md` §BLE Protocol Library
> **Hardware Confirmed**: All 3 SK8Lytz controllers = `Ctrl_Mini_RGB_Symphony_new_0xA3` (product_id=163)

---

## Strategy: Test Before Fix

Every production change MUST be preceded by a passing hardware verification test in the
`Sk8LytzDiagnosticLab`. No protocol byte changes ship without a ✅ logged verdict entry.

---

## PHASE 0 — Lab Infrastructure

### 0-A: Quick Test Palette (Tab or collapsible section in Diag Lab)

One pre-wired button per critical opcode. No configuration — fixed known-good payloads.
Each button renders the exact hex it will send beneath it. Tap → observe → annotate.

**Buttons to implement:**
```
[🔴 PWR OFF]       0x71 24 0F A4
[🟢 PWR ON]        0x71 23 0F A3
[🎨 RBM #1]        0x42 01 32 64 D3
[🎨 RBM #50]       0x42 32 32 64 04
[🎨 RBM #100]      0x42 64 32 64 36
[🎨 RBM #101]      0x42 65 32 64 37  ← expect undefined/glitch
[🌈 SETTLED-1]     0x41 01 FF 00 00 00 00 FF 32 00 00 F0 XX  (red/blue effect 1)
[🎵 MIC=0x26]      0x73 01 26 01 FF 00 00 00 FF 00 80 64 XX  ← APK app mic
[🎵 MIC=0x27]      0x73 01 27 01 FF 00 00 00 FF 00 80 64 XX  ← APK device mic
[🎵 MIC=0x00]      0x73 00 01 FF 00 00 00 FF 00 80 64 XX    ← old wrong format
[📦 SCENE #0]      0x57 00 32 64 XX
[📦 SCENE ALL]     0x57 FF 32 64 XX
[🗑️ DELETE #0]    0x56 00 00...×12 XX
[❓ SCENE QUERY]   0x58 F0 XX
```

### 0-B: TX/RX Byte Inspector

Bidirectional raw byte display. For each command fired:
- **TX row**: `→ [opcode] [byte0] [byte1] ... [checksum]` with opcode label
- **RX row**: `← [byte0] [byte1] ...` with parse hints for known response formats

State-querying commands (`0x63`, `0x58`, `0x47`) MUST parse and display field breakdowns.

### 0-C: Test Session Log with Verdict Annotations

Each test fired appends a row with:
- Timestamp
- Opcode + label
- Raw hex sent
- Raw hex received (if any)
- **Verdict tap control**: ✅ PASS | ❌ FAIL | ⚠️ AMBIGUOUS

Persisted to `AsyncStorage` key `@sk8lytz_diag_test_log`. Max 200 entries, FIFO evict.

### 0-D: Opcode Coverage Matrix

Header grid in Diag Lab showing per-opcode test status pulled from AsyncStorage:

```
0x41 ⬜  0x42 ⬜  0x43 ⬜  0x51 ⬜
0x53 ⬜  0x56 ⬜  0x57 ⬜  0x58 ⬜
0x59 ⬜  0x62 ⬜  0x63 ⬜  0x71 ⬜
0x73 ⬜  0x74 ⬜
```

Colors: `⬜` = untested, `🟩` = PASS, `🟥` = FAIL, `🟨` = AMBIGUOUS.
Tapping a cell deep-links into the relevant test panel.

---

## PHASE 1 — Smoking Gun Hardware Tests

### 1-A: 0x73 Mic Source Shootout (MOST CRITICAL)

**Goal**: Determine exact valid byte for app mic vs device mic.

**Test sequence** (use Quick Test Palette + auto 0x74 stream):
1. Fire `[🎵 MIC=0x00]` → stream `0x74 magnitude=200` every 100ms for 3s → observe LEDs
2. Fire `[🎵 MIC=0x26]` → stream same → observe LEDs
3. Fire `[🎵 MIC=0x27]` → NO 0x74 stream → observe if device mic triggers LEDs
4. Try old 12B format (no isOn byte) vs 13B format (with isOn byte)

**Expected verdict**:
- `0x26` + active `0x74` stream = LEDs pulse → CONFIRMED app mic byte ✅
- `0x27` + no `0x74` stream = LEDs react to ambient sound → CONFIRMED device mic byte ✅
- `0x00` / `0x01` = no response → CONFIRMED old values are wrong ✅

---

### 1-B: 0x51 Format Shootout (HIGH CRITICAL)

**Goal**: Confirm whether 0xA3 requires 291B (9B/slot) or 323B (10B/slot).

**Test sequence**:
1. Build 2-slot scene: Effect ID 1 in slot 0, Effect ID 5 in slot 1
2. Send as **291B** (9B/slot, old format) → observe: does it cycle? does it corrupt?
3. Send as **323B** (10B/slot, 0xA3 extended format) → observe same
4. The format that cleanly cycles between 2 distinct visible effects = truth

**Expected verdict**: 323B cycles cleanly, 291B either ignores or garbles. If both work, 323B wins as it's APK-authoritative.

---

### 1-C: 0x71 Power Verification

**Goal**: Confirm our existing power bytes are correct (should pass).
- Fire `[🔴 PWR OFF]` → skates go dark ✅
- Fire `[🟢 PWR ON]` → skates light up ✅
- Confirm checksum math: `0x71 + 0x23 + 0x0F = 0xA3` ← visual verify in TX inspector

---

### 1-D: 0x42 Ceiling Test

**Goal**: Confirm effectId cap is 100 for 0xA3.
- Fire `[🎨 RBM #100]` → effect plays normally ✅
- Fire `[🎨 RBM #101]` → observe: undefined behavior / nothing / crash?
- Log verdict to coverage matrix

---

## PHASE 2 — Full Opcode Coverage

### 2-A: 0x41 Settled Mode Panel

UI: effectId picker (1–33) + FG color swatch + BG color swatch + speed slider + direction toggle.
Tests: run effect 1, effect 17, effect 33. Verify FG/BG colors appear correctly.

### 2-B: 0x43 Multi-Effect Sequence Panel

UI: multi-select list of effect IDs (up to 50) + speed/brightness sliders.
Test: select 3 effects → send → verify hardware cycles through them.

### 2-C: 0x53 Live Pixel Stream Panel

UI: load a simple gradient array + "Stream Frames" button at configurable FPS.
Test: verify LEDs render the streaming color rows in real-time.

### 2-D: 0x56/0x57/0x58 Scene Management Panels

Minimal inputs: slot index picker + confirm button.
Tests: delete slot 0, activate slot 0, run scene query.
Verify via RX observer: 0x58 should return current scene state bytes.

---

## PHASE 3 — Protocol Implementation (POST-VERIFICATION ONLY)

> **Gate**: ZERO production code changes until Phase 0+1 verdicts are logged.

### 3-A: Fix `ZenggeProtocol.setMusicConfig()` [0x73]
- Add `isOn` parameter (default `1`)
- Change mic source constants from `0x00`/`0x01` to `0x26`/`0x27`
- Update callers: `useMusicMode.ts`, `useAppMicrophone.ts`
- Files: `src/protocols/ZenggeProtocol.ts`, `src/hooks/useMusicMode.ts`,
  `src/hooks/useAppMicrophone.ts`

### 3-B: Fix `ZenggeProtocol.setCustomMode()` [0x51]
- Detect device as 0xA3 and emit 323B (10B/slot) extended format
- Add `directionFlags` byte per slot using verified APK field mapping
- Files: `src/protocols/ZenggeProtocol.ts`

### 3-C: Implement New Protocol Builders
- `setSettledMode(effectId, fg, bg, speed, dir)` → `0x41`
- `setEffectSequence(effectIds[], speed, brightness)` → `0x43`
- `streamPixelFrame(pixels[], numLEDs)` → `0x53`
- `deleteSceneSlot(slotIndex)` → `0x56`
- `activateSceneSlot(slotIndex, speed, brightness)` → `0x57`
- `querySceneState(active)` → `0x58`
- Files: `src/protocols/ZenggeProtocol.ts`

### 3-D: Hardening
- Add `ZENGGE_PRODUCT_ID_0xA3 = 163` exported constant
- Cap `setCustomRbm` effectId at 100 for 0xA3
- Fix mock `product_id: 115` → `163` in `useBLEScanner.ts`

### 3-E: `useProtocolBuilder` + Diag Lab Wiring
- Wire all new builders into `useProtocolBuilder.ts`
- Expose all new opcodes in `Sk8LytzDiagnosticLab.tsx`
- (Note: Lab is already >56KB — scope any extraction pass as a separate refactor item)

---

## Verification Checklist (before closing epic)

- [ ] Coverage matrix shows ≥ 10 opcodes PASS ✅
- [ ] 0x73 mic source verdict logged with hardware evidence
- [ ] 0x51 format verdict logged with hardware evidence
- [ ] TSC `--noEmit` exits 0 after Phase 3 changes
- [ ] Push only with explicit `/health-sweep` clearance
