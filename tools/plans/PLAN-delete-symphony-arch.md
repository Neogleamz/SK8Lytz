# PLAN: Delete Symphony / 0x41 Architecture Traces (AI-First Upgrade)

**Slug**: `refactor/delete-symphony-arch`  
**Created**: 2026-04-22 | **Upgraded**: 2026-04-23 (AI-First Mandate)  
**Status**: 🟡 In Progress  
**Risk**: M-RISK  
**Batch**: P0  
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Problem Summary

Symphony/`0x41` sends a 13-byte config and the hardware computes animation internally — the visualizer cannot know what the hardware is doing. This breaks visualizer parity by design. Our PatternEngine (`0x59`) achieves everything Symphony does with full parity. Symphony is architecturally dead.

---

## Grep Results (2026-04-23 Audit)

| File | Line | Finding | Action |
|------|------|---------|--------|
| `Sk8LytzDiagnosticLab.tsx` | 537, 697, 1274 | `0x73 Symphony` label + `setSettledMode()` call | **KEEP** — DiagnosticLab is protocol research only |
| `EffectsPanel.tsx` | 6, 8, 224 | JSDoc comments referencing Symphony | **UPDATE JSDoc** only — add `@deprecated` note |
| `ProductVisualizer.tsx` | 679 | Comment: "Auto-strip Symphony V2 Protocol Wrapping" | **KEEP** — this is a comment describing 0x00/0x80 detection, not a Symphony feature |
| `useBLEScanner.ts` | 350-361 | `isSymphony` variable for device detection | **KEEP** — BLE scanner needs this to identify Zengge devices |
| `useAppMicrophone.ts` | 5, 73 | JSDoc comments only | **UPDATE JSDoc** — rename to "music mode `0x74`" |
| `useProtocolBuilder.ts` | 135 | String `wrapped` in result — unrelated to Symphony | **KEEP** — false positive |
| `ZenggeProtocol.ts` | 3, 47, 256, 365, 368, 375, 384, 390, 687 | `setSymphonyColor()` method + `setSettledMode()` | **DEPRECATE both** with JSDoc `@deprecated` — do NOT delete (needed by DiagnosticLab) |
| `RbmSimulator.ts` | 5, 11, 275, 281 | JSDoc comments | **KEEP** — RbmSimulator is being retired separately in BATCH:P1 |

---

## Target Files

| File | Absolute Path | Change |
|------|--------------|--------|
| `ZenggeProtocol.ts` | `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\...\src\protocols\ZenggeProtocol.ts` | Add `@deprecated` JSDoc to `setSymphonyColor()` and `setSettledMode()` |
| `EffectsPanel.tsx` | `...\src\components\EffectsPanel.tsx` | Update header JSDoc to remove misleading Symphony references |
| `useAppMicrophone.ts` | `...\src\hooks\useAppMicrophone.ts` | Update JSDoc comment on line 5 and 73 |
| `tools/ZENGGE_PROTOCOL_BIBLE.md` | `tools/ZENGGE_PROTOCOL_BIBLE.md` | Add 0x41 architectural decision note |

---

## Collateral Damage Locks

> ⛔ The Developer MUST NOT touch any of these files.

| File | Why It's Locked |
|------|----------------|
| `Sk8LytzDiagnosticLab.tsx` | Symphony calls in DiagnosticLab are INTENTIONAL — research/oracle use only |
| `useBLEScanner.ts` | `isSymphony` is device detection logic — removing it breaks scanner |
| `ProductVisualizer.tsx` | The Symphony V2 comment is describing protocol detection, not implementing it |
| `RbmSimulator.ts` | Being retired in a separate BATCH:P1 task |
| `useProtocolBuilder.ts` | False positive — unrelated string |

---

## Execution Checklist

### Step 1 — Create Worktree
```powershell
git worktree add ../SK8Lytz-worktrees/refactor/delete-symphony-arch -b refactor/delete-symphony-arch
```

### Step 2 — Deprecate `ZenggeProtocol.ts` methods
Add `@deprecated` JSDoc above `setSymphonyColor()` (line ~368) and `setSettledMode()` (line ~390):
```typescript
/**
 * @deprecated — Symphony (0x41) is NOT used in production.
 * Architecture decision 2026-04-22: Hardware controls animation internally.
 * Visualizer parity is impossible with this opcode.
 * Available in DiagnosticLab for protocol debugging only.
 * Use PatternEngine (0x59) for all production pattern delivery.
 */
```

### Step 3 — Update `EffectsPanel.tsx` header JSDoc
Lines 6-8: Replace `Symphony` references with neutral language describing what the component actually does.

### Step 4 — Update `useAppMicrophone.ts` JSDoc
Lines 5, 73: Replace "Symphony 0x74" with "music mode 0x74".

### Step 5 — Update `ZENGGE_PROTOCOL_BIBLE.md`
Add to the `0x41` section:
```
**PRODUCTION STATUS: NOT USED**
Architecture decision 2026-04-22: Hardware controls animation internally.
Visualizer parity impossible. Use PatternEngine (0x59) instead.
Available in DiagnosticLab for protocol debugging only.
```

### Step 6 — TSC check (from master)
```powershell
npx tsc --noEmit 2>&1 | Select-String "error TS" | Select-Object -First 30
```

### Step 7 — Commit
```powershell
git add .
git commit -m "refactor(protocols): deprecate Symphony/0x41 architecture, update JSDoc [BATCH:P0]"
```

---

## Rollback Strategy
```powershell
git reset --hard HEAD
git clean -fd
```
No destructive deletions in this task — all changes are additive JSDoc deprecation markers. Rollback risk is near-zero.

---

## Test Criteria
- [ ] `grep "setSymphonyColor|setSettledMode" src/` — only hits in DiagnosticLab and ZenggeProtocol (now deprecated)
- [ ] Zero production UI components calling `setSettledMode()` outside DiagnosticLab
- [ ] `npx tsc --noEmit` — zero new errors
- [ ] `ZENGGE_PROTOCOL_BIBLE.md` updated with architectural rationale
- [ ] No changes to `Sk8LytzDiagnosticLab.tsx`, `useBLEScanner.ts`, `RbmSimulator.ts`
