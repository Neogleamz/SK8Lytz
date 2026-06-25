# Implementation Plan
## fix/settled-mode-direction — Fix Direction Byte Inversion in `setSettledMode`

**Severity:** MEDIUM  
**Source:** Defect Audit 2026-06-24 — F-005

---

## Context

`dynamicEffectHandler.setSettledMode` (line 52) constructs the direction byte with:
```typescript
const dirByte = (direction === 1 ? 0 : 1) & 0x01;
```

This inverts the caller's intent. Protocol Bible §0x41: direction byte `0` = forward, `1` = reverse.
A caller passing `direction=1` (meaning "reverse") will have the hardware receive `0x00` (forward).

The function is marked `@DEPRECATED` and is only reachable from DiagnosticLab tooling.
No production screen calls it. Risk is limited to incorrect DiagnosticLab behavior.

---

## Files to Create / Modify

| File | Lines | Change |
|------|-------|--------|
| `src/protocols/handlers/dynamicEffectHandler.ts` | 52 | Fix direction byte construction |

---

## Step-by-Step Execution

**1 — Look before leap: read setSettledMode**
```
Read: src/protocols/handlers/dynamicEffectHandler.ts, lines 40–70
```
Confirm: Line 52 is `const dirByte = (direction === 1 ? 0 : 1) & 0x01;`

**2 — Verify Protocol Bible citation**
Source: `docs/ZENGGE_PROTOCOL_BIBLE.md` §0x41 — direction byte 0=forward, 1=reverse.
Confirm this before editing.

**3 — Surgical edit: fix direction byte**

Change line 52:
```typescript
const dirByte = (direction === 1 ? 0 : 1) & 0x01;
```
To:
```typescript
const dirByte = (direction === 1 ? 1 : 0) & 0x01;  // Protocol Bible §0x41: 1=reverse, 0=forward
```

Alternative (simpler):
```typescript
const dirByte = direction & 0x01;  // Protocol Bible §0x41: 1=reverse, 0=forward
```
Prefer the simpler form if `direction` is always 0 or 1. Read the function signature at line ~40
to confirm parameter type before choosing.

**4 — Post-edit diff**
```
git diff HEAD src/protocols/handlers/dynamicEffectHandler.ts
```
Verify: Only line 52 changed.

**5 — Verify**
```
npm run verify
```
Expected: TSC ✅ Jest ✅

---

## Out of Scope

- Un-deprecating `setSettledMode` (it remains deprecated; this is correctness-only)
- Modifying the 0x41 opcode payload structure
- Modifying any other direction bytes in other handlers
- Fixing callers of setSettledMode (DiagnosticLab only)
