# Plan: `fix-music-mode-color`

### Design Decisions & Rationale
Music mode color changes for "Sound Column" and "Drop" patterns are dispatched via `0x73` but not being applied by the hardware. According to Master Reference Section 3, the `0x73` payload sends pure RGB — the hardware applies color sorting internally. This bug is likely a payload construction issue: the color bytes in the 0x73 packet may be swapped or zero-filled for these specific pattern IDs.

---

## Proposed Changes

### [MODIFY] `src/protocols/ZenggeProtocol.ts`
- Locate `buildMusicModeCommand()` (or equivalent 0x73 builder).
- Add debug logging for the full outgoing payload for "Sound Column" (likely patternId ~0x27) vs. "Drop" effect.
- Verify `c1.R/G/B` and `c2.R/G/B` bytes are being passed in correctly for these specific pattern IDs.

### [MODIFY] `src/components/DockedController.tsx`
- In the Music Mode tab color picker handler, verify the `c1` and `c2` color arguments are being passed to `ZenggeProtocol.buildMusicModeCommand()` — check that no stale closure is returning default colors.

---

## Open Questions
- **Q:** Do "Sound Column" and "Drop" use a separate characteristic UUID or a different payload flag that bypasses color? Need a nRF Connect packet capture to confirm.
- **Q:** Is this affecting ONLY those two patterns, or all patterns in music mode?

## Verification Plan
1. In the LED Diagnostic Lab, manually build a `0x73` payload with a known pattern ID (Sound Column) and a vivid red color.
2. Dispatch it and observe if the hardware applies the color.
3. Compare with a pattern that works (e.g. "Fade") using the same color bytes.
