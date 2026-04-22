# PLAN: Delete Symphony / 0x41 Architecture Traces

**Slug**: `refactor/delete-symphony-arch`
**Created**: 2026-04-22
**Status**: 🔲 Not Started — execute NOW (nothing is built yet, cost = zero)
**Risk**: L-RISK (removal only — Symphony UI was never shipped to production)
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Why

**Architecture decision 2026-04-22**: The app MUST own every payload byte to achieve visualizer parity. Symphony/`0x41` sends a 13-byte config and the hardware computes everything internally — the visualizer cannot know what the hardware is doing. This breaks parity by definition.

Our PatternEngine (`0x59` pixel arrays) achieves everything Symphony does, and MORE, with complete parity. Symphony is not needed.

---

## What To Remove

### Step 1: Bucket List / Planning Cleanup
- ~~Phase 2 section~~ ✅ Removed from Bucket List 2026-04-22
- Delete `tools/plans/PLAN-symphony-effect-engine.md`

### Step 2: Code Audit (grep for Symphony traces)
```powershell
# Find any Symphony-related code
Get-ChildItem -Path src -Recurse -Include *.ts,*.tsx |
  Select-String "symphony|Symphony|SYMPHONY|setSettledMode|SettledMode|0x41" |
  Select-Object Filename, LineNumber, Line
```

**Expected results**:
- If `setSettledMode` was implemented in PR-C: add `@DEPRECATED — 0x41 condemned for UI use. See ZENGGE_PROTOCOL_BIBLE.md.` JSDoc. DO NOT delete — may be useful for future diagnostic lab.
- Any Symphony tab UI components found → delete
- Any `SymphonyEffects.ts` constants file → delete
- Any `SymphonyEffectSimulator.ts` → delete (never written, but check)

### Step 3: Update `ZENGGE_PROTOCOL_BIBLE.md`
Add note to the `0x41` section:
```markdown
## 0x41 — Settled Mode
**STATUS: PROTOCOL-KNOWN, NOT USED IN PRODUCTION**
Payload format confirmed: [0x41, effectId, FG.R, FG.G, FG.B, BG.R, BG.G, BG.B, speed, dir, 0x00, 0xF0, checksum]
Architecture decision 2026-04-22: This opcode is NOT used in the SK8Lytz app.
Reason: Hardware controls animation internally — visualizer parity is impossible.
PatternEngine (0x59) achieves all animation goals with full parity.
Available in DiagnosticLab for protocol debugging only.
```

---

## Files To Touch

| File | Action |
|------|--------|
| `tools/plans/PLAN-symphony-effect-engine.md` | DELETE |
| `src/protocols/ZenggeProtocol.ts` | Add @DEPRECATED JSDoc to setSettledMode if it exists |
| Any Symphony UI components (grep first) | DELETE |
| `tools/ZENGGE_PROTOCOL_BIBLE.md` | Add 0x41 architecture decision note |

---

## Test Criteria
- [ ] grep returns zero production Symphony UI references
- [ ] `npx tsc --noEmit` — zero errors after removal
- [ ] ZENGGE_PROTOCOL_BIBLE.md updated with architectural rationale
