# Implementation Plan
## fix/static-color-handler-cleanup — Fix Stale Speed Comment + `any` Cast in `staticColorHandler.ts`

**Severity:** MEDIUM (F-006) + LOW (F-008) — combined into one worktree (same file)  
**Source:** Defect Audit 2026-06-24 — F-006, F-008

---

## Context

**F-006 — Stale speed-range comment (MEDIUM):**
`staticColorHandler.ts` lines 51–53 state: "Speed mapping: user-facing 0–100 → hardware 1–31."
The Protocol Bible oracle (2026-04-23) confirmed 0xA3 chipset accepts 0x59 speed 1–100.
`ANIM_SPEED_MAX = 100` in the code is correct. The comment describes a previous incorrect mapping
and will mislead any debugging session looking for animation speed issues.

**F-008 — `any` cast (LOW):**
`staticColorHandler.ts` line 3: `let _appLogger: any;`
This violates the No-`any` Law (S3, enforced by `check-any-cast` hook). The pattern is a
lazy-load workaround to avoid circular imports. The type must be narrowed.

---

## Files to Create / Modify

| File | Lines | Change |
|------|-------|--------|
| `src/protocols/handlers/staticColorHandler.ts` | 51–53 | Update stale speed comment |
| `src/protocols/handlers/staticColorHandler.ts` | 3 | Fix `any` type |

---

## Step-by-Step Execution

### F-006 — Stale Comment Fix

**1 — Look before leap: read speed comment block**
```
Read: src/protocols/handlers/staticColorHandler.ts, lines 49–56
```
Confirm: Lines 51–53 contain the stale "0–100 → hardware 1–31" text.
Confirm: Line 54 uses `ANIM_SPEED_MAX` (= 100) — the correct value.

**2 — Surgical edit: update comment**

Replace lines 51–53:
```typescript
    // Speed mapping: user-facing 0–100 → hardware 1–31.
    // Previously 0x00 (CASCADE) was HARDCODED to speed=1 — that was wrong, made animations look frozen.
    // Fix: pass speed through for ALL transition types, properly clamped to 1–31.
```
With:
```typescript
    // Speed: hardware range 1–100 on 0xA3 chipset (oracle-confirmed 2026-04-23, Protocol Bible §0x59).
    // Clamp applied below — ANIM_SPEED_MIN=1, ANIM_SPEED_MAX=100.
```

### F-008 — `any` Cast Fix

**3 — Look before leap: read the logger lazy-load pattern**
```
Read: src/protocols/handlers/staticColorHandler.ts, lines 1–10
```
Confirm: Line 3 is `let _appLogger: any;` and line 4 defines `getAppLogger()`.
Confirm: What import would cause circularity if imported at the top level.

**4 — Read `AppLogger` type from the service**
```
Grep: "export.*AppLogger|class AppLogger|const AppLogger" src/services/appLogger/index.ts
```
Find the exported type of `AppLogger` to use in the narrowed type annotation.

**5 — Surgical edit: narrow the `any` type**

If `AppLogger` is an object with a known interface, type as:
```typescript
let _appLogger: typeof import('../services/appLogger').AppLogger | typeof console | null = null;
```

If that causes a circular import error at the type level, use the structural type:
```typescript
type AppLoggerLike = {
  log: (...args: unknown[]) => void;
  warn: (...args: unknown[]) => void;
  error: (...args: unknown[]) => void;
};
let _appLogger: AppLoggerLike | null = null;
```
The `AppLoggerLike` type definition goes at the top of the file before `_appLogger`.

**Do NOT use `// eslint-disable` or `@ts-ignore`** — fix the type properly.

**6 — Update the null check in `getAppLogger`**
After typing `_appLogger` as `AppLoggerLike | null`, the `if (!_appLogger)` guard at line 5
remains valid. The `try { ... } catch { _appLogger = console; }` assignment at line 6 must
be typed — `console` satisfies `AppLoggerLike`.

**7 — Post-edit diff**
```
git diff HEAD src/protocols/handlers/staticColorHandler.ts
```
Verify: Only lines 3 (type), 51–53 (comment) changed. No logic changes.

**8 — Verify**
```
npm run verify
```
Expected: TSC ✅ (narrowed type must compile cleanly) · `check-any-cast` hook: 0 `any` violations

---

## Out of Scope

- Refactoring the lazy-load pattern itself (just fix the type)
- Modifying `setMultiColor` logic
- Removing the circular import structurally (separate refactor task)
- Any other handler files
