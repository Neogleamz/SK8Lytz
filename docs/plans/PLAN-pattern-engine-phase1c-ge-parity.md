# PLAN: feat/pattern-engine-phase1c-ge-parity

## Goal
Achieve 100% hardware+visualizer parity for all 61 patterns.

`generatePhase1aArray()` already has complete, dedicated math for every ge.* pattern (IDs 29-61). The ONLY gap is `getPatternTransitionType()` which currently returns `0x02` (Running) for ALL of them, including effects that are named Breathe, Jump, Strobe, Twinkle, etc. This sends the wrong commandType byte to the hardware, causing silent mismatches.

## Source of Truth
- APK: `StaticColorfulMode.java` — confirmed hardware enum
- `0x01` = Static (freeze)
- `0x02` = Running (continuous scroll)
- `0x03` = Strobe (flash on/off)
- `0x04` = Jump (hard cut)
- `0x05` = Breathe (pulse brightness)
- **NOTE: `0x06` (Twinkle) is documented in our JSDoc but NOT confirmed from APK decompile. Use `0x02` Running for all twinkle-style patterns until Oracle validation.**

## Target File
**WORKTREE ONLY:**
`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\feat\pattern-engine-phase1c-ge-parity\src\protocols\PatternEngine.ts`

> ⛔ DO NOT edit the master fortress copy. TSC is run from master as a READ tool only.

## The Single Edit Required

### FIND this exact block in `getPatternTransitionType()` (currently lines ~1074-1086):

```typescript
export function getPatternTransitionType(patternId: PatternId): number {
  // Group 1 (IDs 1–5): Solid & Static — freeze in place
  if (patternId >= 1 && patternId <= 5) return 0x01; // Static

  // Group 5a (IDs 20–22): Hardware-native effects — pixel array sent, hardware does the animation
  // APK source: StaticColorfulMode.java — commandType enum
  if (patternId === 20) return 0x05; // Breathe  — hardware pulses the array in/out
  if (patternId === 21) return 0x04; // Jump     — hardware hard-jumps between states
  if (patternId === 22) return 0x03; // Strobe   — hardware flashes the array on/off

  // All other animated groups (2–4, 5b, 6, 7 — IDs 6–19, 23–61): Running = continuous scroll
  return 0x02; // Running
}
```

### REPLACE with this exact block:

```typescript
export function getPatternTransitionType(patternId: PatternId): number {
  // ── GROUP 1: STATIC (IDs 1–5) ──────────────────────────────────────────────
  if (patternId >= 1 && patternId <= 5) return 0x01; // Static — hardware freezes array

  // ── GROUP 5a: HARDWARE TEMPORAL (IDs 20–22) ────────────────────────────────
  // APK source: StaticColorfulMode.java — verified commandType enum
  if (patternId === 20) return 0x05; // Breathe  — hardware pulses brightness
  if (patternId === 21) return 0x04; // Jump     — hardware hard-cuts between states
  if (patternId === 22) return 0x03; // Strobe   — hardware flashes on/off

  // ── GROUP 7: ge.* BREATHE EFFECTS (IDs 30, 39, 40, 47, 55) ───────────────
  // Hardware receives the pixel array and pulses its brightness autonomously.
  // Visualizer uses buildColorBreathing / buildSmoothBreath / buildRainbowBreathing / buildNeonPulse at animTick.
  if (patternId === 30) return 0x05; // Color Breathing   — FG_ONLY
  if (patternId === 39) return 0x05; // Candle Flicker    — FG_ONLY (breathe ≈ flicker)
  if (patternId === 40) return 0x05; // Heartbeat Pulse   — FG_ONLY (breathe pulse)
  if (patternId === 47) return 0x05; // Rainbow Breathing — GENERATIVE
  if (patternId === 55) return 0x05; // Neon Pulse        — FG_BG (NeonPulse math uses buildNeonPulse)

  // ── GROUP 7: ge.* JUMP EFFECTS (IDs 31, 46, 54) ──────────────────────────
  // Hardware receives the pixel array and hard-cuts states autonomously.
  // Visualizer uses buildColorJump / buildPoliceLights / buildCyberGlitch at animTick.
  if (patternId === 31) return 0x04; // Color Jump        — FG_BG hard cut
  if (patternId === 46) return 0x04; // Police Lights     — GENERATIVE, hardcoded red/blue
  if (patternId === 54) return 0x04; // Cyber Glitch      — GENERATIVE, hardcoded cyan/magenta

  // ── GROUP 7: ge.* STROBE EFFECTS (IDs 33, 37) ────────────────────────────
  // Hardware receives the pixel array and flashes it on/off autonomously.
  // Visualizer uses buildStrobe / buildLightning at animTick.
  if (patternId === 33) return 0x03; // Strobe Flash      — FG_ONLY
  if (patternId === 37) return 0x03; // Lightning Strike  — FG_ONLY (random flash)

  // ── ALL OTHER ge.* SCROLL EFFECTS (IDs 29, 32, 34–36, 38, 41–45, 48–53, 56–61) ──
  // Hardware scrolls the pre-computed pixel array continuously via 0x02 Running.
  // Visualizer generates tick-based frames matching the scroll animation.
  // NOTE: Twinkle-style patterns (35, 45, 49, 50, 58, 60) intentionally use 0x02 (Running)
  // because 0x06 (Twinkle) is NOT yet confirmed from APK decompile.
  // Log this for Oracle validation: fix/oracle-confirm-0x06-twinkle
  return 0x02; // Running — continuous hardware scroll
}
```

## Execution Steps

### Step 1 — Open worktree file
Open: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\feat\pattern-engine-phase1c-ge-parity\src\protocols\PatternEngine.ts`

### Step 2 — Apply the single replacement
Use `replace_file_content` tool. Replace the old `getPatternTransitionType` function with the new version above. No other lines in the file should be touched.

### Step 3 — Run TSC from master (READ only, never write to master)
```powershell
# Cwd: C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz
npx tsc --noEmit 2>&1 | Select-String "error TS" | Select-Object -First 20
```
Expected result: 0 errors.

### Step 4 — Commit from inside worktree dir
```powershell
# Cwd: C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\feat\pattern-engine-phase1c-ge-parity
git add src/protocols/PatternEngine.ts
git commit -m "feat(protocol): wire correct commandType for all ge.* patterns (IDs 29-61)

getPatternTransitionType() now maps per APK StaticColorfulMode.java:
  0x05 Breathe  → IDs 30, 39, 40, 47, 55 (Color Breathing, Candle, Heartbeat, Rainbow Breath, Neon)
  0x04 Jump     → IDs 31, 46, 54 (Color Jump, Police Lights, Cyber Glitch)
  0x03 Strobe   → IDs 33, 37 (Strobe Flash, Lightning Strike)
  0x02 Running  → all remaining scroll/flow/chase/twinkle patterns

Note: 0x06 Twinkle unconfirmed from APK. Twinkle-style patterns (35,45,49,50,58,60)
use 0x02 Running until Oracle lab validation. Logged as fix/oracle-confirm-0x06-twinkle."
```

### Step 5 — Merge to master + cleanup
```powershell
# Cwd: C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz
git merge feat/pattern-engine-phase1c-ge-parity --ff-only
git status -s
git worktree remove ../SK8Lytz-worktrees/feat/pattern-engine-phase1c-ge-parity
git branch -d feat/pattern-engine-phase1c-ge-parity
git log -1 --format="%h"
```

### Step 6 — Update Bucket List
Mark `feat/pattern-engine-phase1c-ge-parity` as `[x]` with the 7-char commit hash and this outcome summary:
> `getPatternTransitionType` correctly maps all ge.* patterns (29-61) to APK-verified commandTypes. Breathe=0x05 (5 patterns), Jump=0x04 (3), Strobe=0x03 (2), Running=0x02 (rest). 0x06 Twinkle logged for Oracle validation.`

---

## Complete commandType Map — All 61 Patterns

| ID | Name | colorMode | commandType | Hardware Effect |
|:---|:-----|:----------|:------------|:----------------|
| 1–5 | Solid/Static | FG_ONLY/FG_BG | `0x01` | Freeze |
| 6 | Single Dot Chase | FG_BG | `0x02` | Scroll |
| 7 | Reflected Dot Chase | FG_BG | `0x02` | Scroll |
| 8 | Comet Chase | FG_BG | `0x02` | Scroll |
| 9 | Meteor Shower | FG_BG | `0x02` | Scroll |
| 10 | Micro Ants | FG_BG | `0x02` | Scroll |
| 11 | Theater Chase | FG_BG | `0x02` | Scroll |
| 12 | Dashed Marquee | FG_BG | `0x02` | Scroll |
| 13 | Barber Pole | FG_BG | `0x02` | Scroll |
| 14 | Bold Stripes | FG_BG | `0x02` | Scroll |
| 15 | Sine Pulse Wave | FG_BG | `0x02` | Scroll |
| 16 | Wave Pinch | FG_BG | `0x02` | Scroll |
| 17 | Breathing Wave | FG_BG | `0x02` | Scroll (spatial sine, not hardware breathe) |
| 18 | Center-Out Comet | FG_BG | `0x02` | Scroll |
| 19 | Center-Out Marquee | FG_BG | `0x02` | Scroll |
| 20 | Smooth Breath | FG_BG | `0x05` | Breathe |
| 21 | Hard Jump Flash | FG_BG | `0x04` | Jump |
| 22 | Strobe | FG_BG | `0x03` | Strobe |
| 23 | Wipe / Fill | FG_BG | `0x02` | Scroll |
| 24 | Wipe Center-Out | FG_BG | `0x02` | Scroll |
| 25 | True Rainbow Flow | GENERATIVE | `0x02` | Scroll |
| 26 | Rainbow Marquee | GENERATIVE | `0x02` | Scroll |
| 27 | Rainbow Comet | GENERATIVE | `0x02` | Scroll |
| 28 | Cyberpunk Shift | FG_BG | `0x02` | Scroll |
| 29 | Color Flow | GENERATIVE | `0x02` | Scroll |
| **30** | **Color Breathing** | FG_ONLY | **`0x05`** | **Breathe** |
| **31** | **Color Jump** | FG_BG | **`0x04`** | **Jump** |
| 32 | Running Water | FG_BG | `0x02` | Scroll |
| **33** | **Strobe Flash** | FG_ONLY | **`0x03`** | **Strobe** |
| 34 | Color Wipe | FG_BG | `0x02` | Scroll |
| 35 | Fireworks | FG_BG | `0x02` | Scroll (0x06 unconfirmed) |
| 36 | Ocean Wave | FG_BG | `0x02` | Scroll |
| **37** | **Lightning Strike** | FG_ONLY | **`0x03`** | **Strobe** |
| 38 | Snowfall | FG_BG | `0x02` | Scroll |
| **39** | **Candle Flicker** | FG_ONLY | **`0x05`** | **Breathe** |
| **40** | **Heartbeat Pulse** | FG_ONLY | **`0x05`** | **Breathe** |
| 41 | Meteor | FG_BG | `0x02` | Scroll |
| 42 | Aurora Borealis | GENERATIVE | `0x02` | Scroll |
| 43 | Lava Lamp | FG_BG | `0x02` | Scroll |
| 44 | Plasma Wave | FG_BG | `0x02` | Scroll |
| 45 | Star Cluster | FG_BG | `0x02` | Scroll (0x06 unconfirmed) |
| **46** | **Police Lights** | GENERATIVE | **`0x04`** | **Jump** |
| **47** | **Rainbow Breathing** | GENERATIVE | **`0x05`** | **Breathe** |
| 48 | Color Burst | FG_BG | `0x02` | Scroll |
| 49 | Twinkle | FG_BG | `0x02` | Scroll (0x06 unconfirmed) |
| 50 | Crystal Shimmer | GENERATIVE | `0x02` | Scroll (0x06 unconfirmed) |
| 51 | Gradient Chase | FG_BG | `0x02` | Scroll |
| 52 | Comet Duo | FG_BG | `0x02` | Scroll |
| 53 | Fire Flame | FG_BG | `0x02` | Scroll |
| **54** | **Cyber Glitch** | GENERATIVE | **`0x04`** | **Jump** |
| **55** | **Neon Pulse** | FG_BG | **`0x05`** | **Breathe** |
| 56 | Rainbow Chaser | GENERATIVE | `0x02` | Scroll |
| 57 | Matrix Rain | FG_BG | `0x02` | Scroll |
| 58 | Sparkle Fade | FG_BG | `0x02` | Scroll (0x06 unconfirmed) |
| 59 | Dual Scan | FG_BG | `0x02` | Scroll |
| 60 | Starlight | FG_BG | `0x02` | Scroll (0x06 unconfirmed) |
| 61 | Hyperspace | FG_BG | `0x02` | Scroll |

---

## Follow-up Tech Debt to Log
After merging, log this task in the Bucket List TECH DEBT section:

`fix/oracle-confirm-0x06-twinkle` — Oracle Lab physical test: send 0x59 with commandType=0x06 to 0xA3 hardware for a single-color array. Patterns affected if confirmed: IDs 35, 45, 49, 50, 58, 60.
