# Implementation Plan

## Task: `docs/xstate-v5-kb-capture`
**Goal:** Read the live `BleMachine.ts` and `sessionMachine.ts` implementations, synthesize the XState v5 patterns SK8Lytz actually uses, and write a KB entry so future agents stop re-deriving these patterns from first principles.

---

## Files to Create/Modify

- **[NEW]** `tools/knowledge-base/patterns/xstate-v5-patterns.md` — new KB entry for XState v5 patterns used in this codebase
- **[MODIFY]** `tools/knowledge-base/INDEX.md` — add entry for `xstate-v5-patterns`

---

## Execution Steps

**Step 1 — Read Live Implementations**
- `view_file` `src/services/ble/BleMachine.ts` in full (existing pattern reference)
- `view_file` `src/services/session/sessionMachine.ts` in full (new Wave 1 implementation)
- Source: Both files (read before writing)

**Step 2 — Read XState v5 Version**
- `grep "xstate" package.json` → confirm exact installed version
- Source: `package.json`

**Step 3 — Write KB Entry**
- Create `tools/knowledge-base/patterns/xstate-v5-patterns.md` with:
  - **Scope:** Patterns SK8Lytz uses only (not full XState v5 API)
  - **Key facts to document:**
    - `createMachine` + `setup()` pattern for typed actors
    - `useMachine()` hook for React context integration
    - `assign()` action for context mutations
    - `fromPromise()` for async transitions (if used)
    - `sendTo()` / actor system for cross-machine communication (SessionBridge pattern)
    - Guard syntax (`guard: ({context}) => ...`)
    - How `BleMachine.ts` uses `interpret()` vs how `sessionMachine.ts` uses `useMachine()`
    - The `SessionBridge.ts` actor sender pattern (unique to this codebase)
  - Staleness window: 180 days (pattern synthesis)
  - Source: Reverse engineering of live codebase + XState v5 changelog

**Step 4 — Update KB INDEX**
- Append entry to `tools/knowledge-base/INDEX.md` under `## 🎨 Patterns` section
- Format: per INDEX.md schema
- Feeds into: `tools/SK8Lytz_App_Master_Reference.md` §3 State Machine Library

**Step 5 — Write SESSION_LOG [ARTIFACT] entry**

---

## Out of Scope
- Full XState v5 API documentation (too broad)
- XState v4 patterns (not used in this codebase)
- Any changes to `.ts`/`.tsx` source files
