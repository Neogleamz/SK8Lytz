# Implementation Plan
## fix/adapter-chunking-comment — Correct Stale `prepareForTransmission` Auto-Chunk Comment in ZenggeAdapter

**Severity:** HIGH  
**Source:** Defect Audit 2026-06-24 — F-004

---

## Context

`ZenggeAdapter.buildCustomModeExtended` (line 185) has a JSDoc comment at line 182–183:
> "This REQUIRES chunked transmission — prepareForTransmission() will automatically apply
>  0x40 fragmentation when the packet exceeds MTU."

This is **false**. `prepareForTransmission` (line 250) takes `_mtu` as a parameter
(underscore-prefixed = intentionally unused) and returns `result` unchanged. No chunking occurs.
The comment actively misleads developers into believing there is a downstream safety net when
there is not — this is the primary reason F-003 (the actual unguarded path) can survive code review.

Fix: Replace the stale comment with an accurate description of chunking responsibilities.

---

## Files to Create / Modify

| File | Lines | Change |
|------|-------|--------|
| `src/protocols/ZenggeAdapter.ts` | 182–183 | Replace stale JSDoc comment |

---

## Step-by-Step Execution

**1 — Look before leap: read buildCustomModeExtended comment block**
```
Read: src/protocols/ZenggeAdapter.ts, lines 178–192
```
Confirm: Lines 182–183 contain the stale "prepareForTransmission() will automatically apply
0x40 fragmentation" text.

**2 — Read `prepareForTransmission` to confirm it is a pass-through**
```
Read: src/protocols/ZenggeAdapter.ts, lines 248–260
```
Confirm: `_mtu` is unused, result is returned unchanged.

**3 — Surgical edit: replace stale comment**

Replace lines 182–183:
```typescript
   * This REQUIRES chunked transmission — prepareForTransmission() will
   * automatically apply 0x40 fragmentation when the packet exceeds MTU.
```
With:
```typescript
   * Returns a raw 323-byte 0x51 extended payload (NOT V2-wrapped, NOT 0x40-chunked).
   * Chunking responsibility by caller:
   *   - writeToDevice path → _executeWriteToDeviceInternal:155 detects 0x51 + length > 200
   *     and routes to executeWriteChunked automatically. ✅ SAFE.
   *   - executeProtocolResults path (_dispatchToDevices) → has MTU guard added in F-003.
   *     ✅ SAFE after fix/protocol-dispatch-mtu-guard merged.
   *   - prepareForTransmission() is a PASS-THROUGH — it does NOT chunk. Do not rely on it.
```

**4 — Post-edit diff**
```
git diff HEAD src/protocols/ZenggeAdapter.ts
```
Verify: Only the comment lines changed. No logic changes.

**5 — Verify**
```
npm run verify
```
Expected: TSC ✅ (comment-only change; no behavioral impact)

---

## Out of Scope

- Implementing actual chunking in `prepareForTransmission` (it is intentionally a pass-through;
  the chunking lives at the dispatcher layer where MTU context is available)
- Modifying `buildCustomModeExtended` logic
- Any other ZenggeAdapter methods
