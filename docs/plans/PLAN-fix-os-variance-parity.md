# Implementation Plan: fix/os-variance-parity

> **Rule:** R-20 (OS Variance Parity) | **Risk:** L-RISK | **Size:** Snack | **Cognitive Load:** Low

## Goal

Fix 3 verified OS variance parity violations from CLUSTER 10 of the system audit:
1. Missing Android `elevation` on `countdownBadge` (has iOS shadow props but no Android equivalent).
2. Web-specific styles (`touchAction`, `userSelect`, `cursor`) applied unconditionally on all platforms in `CustomSlider.tsx`.
3. Same web-specific style leak in `TacticalSlider.tsx`.

## Source Analysis Link

[system_audit_report.md — CLUSTER 10](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) — Lines 198-202.

## Files to Create/Modify

| Action | File | Lines |
|--------|------|-------|
| Modify | `src/styles/DashboardStyles.ts` | 105-119 |
| Modify | `src/components/CustomSlider.tsx` | 95-104 |
| Modify | `src/components/TacticalSlider.tsx` | 116-124 |

---

## Steps

### Step 1 — Add `elevation` to `countdownBadge` in DashboardStyles.ts

**What:** The `countdownBadge` style (line 105) defines `shadowColor`, `shadowRadius`, and `shadowOpacity` for iOS but has no `elevation` property for Android. Nearby comparable styles (`card`:49, `scanButton`:62, `statusDot`:201) all include `elevation`.
Source: `src/styles/DashboardStyles.ts:105-118`

**Edit:** Add `elevation: 4` after the `zIndex: 50` line (line 118). Value `4` matches the shadow intensity (`shadowRadius: 8, shadowOpacity: 1`) and is consistent with `statusDot` which uses `elevation: 4` for a similar small-badge pattern.
Source: `src/styles/DashboardStyles.ts:201` (statusDot precedent)

**Verify:** Run `npm run verify`. Confirm `elevation` appears in the `countdownBadge` style block via `git diff HEAD src/styles/DashboardStyles.ts`. No other styles in the file were modified.

---

### Step 2 — Wrap `webStyle` in `Platform.select` in CustomSlider.tsx

**What:** Lines 95-100 define a `WebStyle` interface with `touchAction`, `userSelect`, and `cursor`, then unconditionally merge it into the container View at line 104. These CSS properties are web-only and should be gated.
Source: `src/components/CustomSlider.tsx:95-104`

**Edit:**
1. Add `Platform` to the existing `react-native` import on line 3.
2. Replace the `webStyle` constant (line 100) to use `Platform.select`:
   ```ts
   const webStyle = Platform.select<WebStyle>({
     web: { touchAction: 'none', userSelect: 'none', cursor: 'pointer' },
     default: {},
   });
   ```
3. Line 104 (`style={[styles.container, style, webStyle]}`) remains unchanged — it already merges the variable.

**Verify:** Run `npm run verify`. Confirm via `git diff` that only the `webStyle` assignment and import line changed. The `WebStyle` interface declaration remains untouched. No JSX was modified.

---

### Step 3 — Wrap `webStyle` in `Platform.select` in TacticalSlider.tsx

**What:** Lines 116-120 define the same `WebStyle` interface and unconditionally assign `touchAction`/`userSelect` to `webStyle`, which is merged into the container at line 124. `Platform` is already imported on line 3 (confirmed: `Platform` appears in the existing import).
Source: `src/components/TacticalSlider.tsx:3,116-124`

**Edit:** Replace the `webStyle` constant (line 120) to use `Platform.select`:
   ```ts
   const webStyle = Platform.select<WebStyle>({
     web: { touchAction: 'none', userSelect: 'none' },
     default: {},
   });
   ```
No import change needed — `Platform` is already imported.
Source: `src/components/TacticalSlider.tsx:3` (existing import confirmed)

**Verify:** Run `npm run verify`. Confirm via `git diff` that only the `webStyle` assignment changed. Line 124 JSX remains unchanged.

---

## Verification Plan

1. **TypeScript compilation:** `npm run verify` passes with zero new errors.
2. **Git diff audit:** Each file's diff shows ONLY the planned lines changed — no collateral edits.
3. **Manual spot-check (optional):** On Android, the countdown badge should now render with a visible shadow (elevation). On iOS/web, no visual change expected.
4. **Regression:** No functional behavior changes — these are style-only fixes. No hooks, state, or BLE logic touched.

---

## Out of Scope

- Other `Platform.OS === 'web'` checks in `DashboardScreen.tsx` — those ARE the platform guard (not violations).
- Other `Platform.OS` conditional checks that correctly gate platform-specific code.
- The `cursor: 'pointer'` in `TacticalSlider.tsx:182` — already correctly wrapped in `Platform.select`.
- Any BLE, protocol, or architecture changes.
- Boy Scout cleanup — these 3 files are small and clean; no dead imports or `any` casts observed.
