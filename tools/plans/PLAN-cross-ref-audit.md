# PLAN: Cross-Reference Audit — Existing 28 vs ge.* 33 vs Programs List

**Slug**: `audit/cross-ref-existing-28-vs-source`
**Created**: 2026-04-22
**Status**: 🔲 Not Started — MUST run before Phase 1A or 1B coding begins
**Risk**: L-RISK (read-only audit — no code changes)
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Why This Is The Critical First Step

We have **28 existing PatternEngine patterns**. We are about to implement:
- **33 ge.* Settled Mode effects** (reverse-engineered from Java decompilation)
- **~33 Programs Mode effects** (reverse-engineered from hardware programs list)

Without this audit, we will:
- Duplicate patterns that already exist (waste work)
- Miss patterns that are partially wrong (silent parity bugs)
- End up with overlapping IDs and naming confusion

This audit produces a **definitive implementation manifest** that drives all Phase 1A and 1B work.

---

## Step 1: Enumerate All Existing 28 Patterns

Open `src/constants/CustomEffects.ts` and `src/protocols/PatternEngine.ts`.

Build this mapping table for each existing pattern:

```
ID | Name | colorMode | Algorithm Summary | ge.* Match? | Programs Match?
---|------|-----------|-------------------|-------------|----------------
 1 | ... | FG_BG     | ...               | ge.X?       | Programs:X?
...
28 | ... | ...       | ...               | ...         | ...
```

**For each pattern, answer:**
1. What is the actual math in `PatternEngine.ts`? (describe in 1 sentence)
2. Is the math ground-truth sourced from ge.* or Programs, or is it invented?
3. Does it correctly match the source effect's algorithm?

---

## Step 2: Map Against ge.* Settled Mode Effects

Cross-reference against the 33 ge.* effects documented in `tools/ZENGGE_PROTOCOL_BIBLE.md`.

For each ge.* effect, determine:

| ge.* Effect | ge.* ID | Already in our 28? | Math correct? | Action |
|---|---|---|---|---|
| `ge.RainbowEffect` | 1 | Yes (ID 12 Rainbow Flow) | ✅ Correct | SKIP |
| `ge.BreathingEffect` | 2 | Yes (ID 5 Breathing) | ⚠️ Check | VERIFY |
| `ge.OceanWaveEffect` | 14 | No | — | ADD in Phase 1A |
| `ge.FireworksEffect` | 18 | No | — | ADD in Phase 1A |
| ... | ... | ... | ... | ... |

**Action codes:**
- `SKIP` — already implemented correctly
- `VERIFY` — exists but math needs hardware comparison
- `CORRECT` — exists but math is wrong, needs fix
- `ADD` — does not exist, must be implemented in Phase 1A

---

## Step 3: Map Against Programs Effects

Cross-reference against the Programs list:

| Programs Effect | Already in our 28? | Math correct? | Action |
|---|---|---|---|
| Solid & Static | Yes? | ✅ | SKIP |
| Split Colors | No | — | ADD in Phase 1B |
| Trisection | No | — | ADD in Phase 1B |
| Quartered | No | — | ADD in Phase 1B |
| Center Accent | No | — | ADD in Phase 1B |
| Single Dot Chase | Yes? | ✅ | SKIP |
| Reflected Dot Chase | No | — | ADD in Phase 1B |
| Comet Chase | Yes (ID 3 Comet?) | ⚠️ | VERIFY |
| Meteor Shower | No | — | ADD in Phase 1B |
| Micro Ants | No | — | ADD in Phase 1B |
| Theater Chase | No | — | ADD in Phase 1B |
| Dashed Marquee | No | — | ADD in Phase 1B |
| Barber Pole | No | — | ADD in Phase 1B |
| Bold Stripes | No | — | ADD in Phase 1B |
| Sine Pulse Wave | No | — | ADD in Phase 1B |
| Wave Pinch | No | — | ADD in Phase 1B |
| Breathing Wave | No | — | ADD in Phase 1B |
| Center-Out Comet | No | — | ADD in Phase 1B |
| Center-Out Marquee | No | — | ADD in Phase 1B |
| Smooth Breath | Yes (ID 4 Breathing?) | ⚠️ | VERIFY |
| Hard Jump Flash | No | — | ADD in Phase 1B |
| Strobe | Yes | ✅ | SKIP |
| Wipe Fill | No | — | ADD in Phase 1B |
| Wipe Center-Out | No | — | ADD in Phase 1B |
| True Rainbow Flow | Yes? | ⚠️ | VERIFY |
| Rainbow Marquee | No | — | ADD in Phase 1B |
| Rainbow Comet | No | — | ADD in Phase 1B |
| Cyberpunk Shift | No | — | ADD in Phase 1B |

---

## Step 4: Build the Implementation Manifest

Output of this audit:

```markdown
## Phase 1A Manifest (ge.* → PatternEngine TypeScript)
IDs to implement: [list of ge.* effects not in existing 28]
IDs to correct: [list of existing patterns with wrong math]
Starting pattern ID: 29 (next after existing 28)

## Phase 1B Manifest (Programs → PatternEngine TypeScript)
IDs to implement: [list of Programs effects not in existing 28]
Starting pattern ID: [29 + Phase 1A count]

## Existing 28: Corrections Needed
[list any patterns with identified math errors]

## Final Pattern Count Projection
28 (existing) + [1A adds] + [1B adds] = [total]
```

---

## Step 5: Document Results

Record the full mapping table in `tools/ZENGGE_PROTOCOL_BIBLE.md` under a new section:
```
## Pattern Cross-Reference Audit (YYYY-MM-DD)
[paste the full tables]
```

---

## Test Criteria (Audit Complete When:)
- [ ] All 28 existing patterns have an algorithm description
- [ ] All 28 checked against ge.* 33 list
- [ ] All 28 checked against Programs 33 list
- [ ] Implementation manifest written with exact IDs for Phase 1A and 1B
- [ ] `tools/ZENGGE_PROTOCOL_BIBLE.md` updated with audit results
- [ ] `npx tsc --noEmit` — zero errors (no code changes expected)
