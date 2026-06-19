# Implementation Plan: fix/session-context-safety

## Goal
Fix re-entrancy races, floating promises, error swallowing, and type safety in session tracking.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Cluster SESSION_TRACKING

## Findings to Resolve
1. R-26: SessionContext.tsx L239 — recover() async without re-entrancy guard
2. R-11: SessionContext.tsx L348,353 — Floating promises from dynamic imports
3. R-06: SessionContext.tsx L58 — Swallowed error without AppLogger
4. R-08: SessionContext.tsx L185 — `as` cast on XState snapshot.value
5. R-11: useTelemetryLedger.ts L144 — Empty catch on fatal storage error
6. R-11: useTelemetryLedger.ts L117 — Empty catch on parse error
7. R-16: SessionContext.tsx L191 — Hardcoded setInterval

## Files to Create/Modify

### [MODIFY] [SessionContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx)
### [MODIFY] [useTelemetryLedger.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useTelemetryLedger.ts) // SKIPPED: Empty catch blocks already fixed in master

## Verification
- `npm run verify`

## Out of Scope
- SessionMachine.ts
- Session services (Wave 4 test suite)
