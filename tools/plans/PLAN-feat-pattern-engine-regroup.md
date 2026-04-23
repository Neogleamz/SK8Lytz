# Plan: Pattern Engine Regroup & UI Category Wheel

## Goal
Regroup patterns in PatternEngine, prune redundant/broken patterns, and implement a horizontal navigation wheel in PatternPickerTab to filter pattern cards by category.

## Proposed Changes

### 1. Prune & Regroup `PatternEngine.ts`
- **Delete 17 patterns:** 13, 18, 19, 21, 22, 24, 31, 34, 35, 46, 48, 49, 52, 54, 58, 59, 61.
- **Rename:** Pattern 7 to "Double Dot Chase".
- **Regroup Surviving Patterns:**
  - `Static`: 1, 2, 3, 4, 5, 39
  - `Breathe`: 17, 20, 30, 40, 47, 55
  - `Chase`: 6, 7, 8, 9, 32, 38, 41, 51, 57
  - `Marquee`: 10, 11, 12, 14, 23
  - `Wave`: 15, 16, 36, 43, 44, 53
  - `Rainbow`: 25, 26, 27, 28, 29, 42, 56
  - `Sparkle & Flash`: 33, 37, 45, 50, 60
  - `Street`: 101, 102, 103, 104, 105 (Hidden from UI)

### 2. Add Horizontal Wheel to `PatternPickerTab.tsx`
- Add state `activeCategory` (default `'All'`).
- Add a horizontal `ScrollView` rendering category pills (`All`, `Static`, `Breathe`, `Chase`, `Marquee`, `Wave`, `Rainbow`, `Sparkle & Flash`).
- Filter `SK8LYTZ_TEMPLATES` before mapping to `PatternCard`s:
  - Exclude `Street` group.
  - If `activeCategory !== 'All'`, only include patterns matching `activeCategory`.

## Verification
- TSC type check passes cleanly.
- Visualizer accurately renders the horizontal wheel.
- Selecting a pill accurately filters the cards.
- Deleted patterns no longer appear.
