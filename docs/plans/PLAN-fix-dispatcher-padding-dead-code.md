# Implementation Plan
## fix/dispatcher-padding-dead-code — Remove Dead `padStaticColorfulPayload` Call from BleWriteDispatcher

**Severity:** HIGH  
**Source:** Defect Audit 2026-06-24 — F-002

---

## Context

`BleWriteDispatcher.executeWriteToDevice` calls `ZenggeProtocol.padStaticColorfulPayload(payload)`
at line 51, documented as "BUFFER LOCKOUT DEFENSE (R-19)." This call is dead code:

Every payload arriving at `executeWriteToDevice` is already V2-wrapped by `protocol.wrapCommand()`
inside `setMultiColor` (staticColorHandler.ts:74). The wrapped payload starts with `0x00`
(V2 frame header). `padStaticColorfulPayload` guards with `if (payload[0] !== 0x59 || ...)` —
this condition is always true for wrapped payloads, so the function returns immediately without
any padding enforcement.

The REAL buffer lockout defense IS working — it lives at `setMultiColor`:44:
`const numPoints = Math.max(12, Math.min(MAX_PIXELS, colors.length));`

The dispatcher call (a) never fires, (b) creates false confidence that there's a second safety net,
and (c) would silently fail to protect any future code path that calls `writeToDevice` with a
raw unwrapped 0x59 payload shorter than 12 pixels.

Fix: Remove the dead call. Update the comment to correctly describe where the defense lives.

---

## Files to Create / Modify

| File | Lines | Change |
|------|-------|--------|
| `src/services/BleWriteDispatcher.ts` | 49–51 | Remove dead call, update comment |

---

## Step-by-Step Execution

**1 — Look before leap: read target lines**
```
Read: src/services/BleWriteDispatcher.ts, lines 38–60
```
Confirm: line 49 is the `// BUFFER LOCKOUT DEFENSE` comment, line 51 is the dead call.

**2 — Surgical edit: remove dead call and fix comment**

Replace the existing block (lines 49–51):
```typescript
  // BUFFER LOCKOUT DEFENSE (R-19): delegated to ZenggeProtocol.padStaticColorfulPayload()
  // Source: tools/ZENGGE_PROTOCOL_BIBLE.md §0x59 MINIMUM PIXELS
  payload = ZenggeProtocol.padStaticColorfulPayload(payload);
```

With:
```typescript
  // BUFFER LOCKOUT DEFENSE (R-19): enforced upstream in setMultiColor():44 (staticColorHandler.ts)
  // before wrapCommand() is called. Payloads here are already V2-wrapped (payload[0]=0x00),
  // so padStaticColorfulPayload() would be a no-op. The defense is NOT re-applied here.
  // Source: Protocol Bible §0x59 MINIMUM PIXELS — minimum 12 pixels, enforced at construction.
```

**3 — Post-edit diff**
```
git diff HEAD src/services/BleWriteDispatcher.ts
```
Verify: Only lines 49–51 changed (comment replacement). No logic changes. No imports removed
unless `ZenggeProtocol` is now unused — check for any other usages of `ZenggeProtocol` in this
file before removing the import.

**4 — Check ZenggeProtocol import still needed**
```
Verify: grep -n "ZenggeProtocol\." src/services/BleWriteDispatcher.ts
```
If no other usages → remove the import. If other usages → leave import in place.

**5 — Verify**
```
npm run verify
```
Expected: TSC ✅ Jest ✅ (no logic change — all behavioral tests should pass unchanged)

---

## Out of Scope

- Modifying `staticColorHandler.ts` (the real defense is already correct there)
- Modifying `padStaticColorfulPayload` itself (it works correctly when called with raw 0x59 payloads)
- Adding a pre-wrap padding call to `setMultiColor` (already done at line 44)
- Any other BleWriteDispatcher changes
