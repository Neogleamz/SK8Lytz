# Implementation Plan: Database Schema Type Parity (Phase 2)

This plan formalizes the steps required to achieve 100% TypeScript parity with the hardened Supabase schema, specifically addressing the mandatory `type` column in `registered_groups` and other related mismatches.

## User Review Required

> [!IMPORTANT]
> This task is currently deferred to the Bucket List. It requires a high-reasoning model (e.g., Gemini 3.1 Pro High or Claude 4.6 Thinking) to safely navigate the complex state interactions in `DashboardScreen` and `useCrewManage`.

## Proposed Changes

### [Component] Database Hardening

#### [MODIFY] [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- [ ] Inject `type: 'device-fleet'` into the `registered_groups` upsert at line 899.
- [ ] Audit surrounding mutations for similar type-safety gaps.

#### [MODIFY] [useCrewManage.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewManage.ts)
- [ ] Fix `Parameter of type...` mismatch in `registered_groups` mutations.
- [ ] Ensure all crew sessions are typed correctly before DB insertion.

#### [MODIFY] [useDiagnosticLog.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDiagnosticLog.ts)
- [ ] Update `device_diagnostics` or `led_diagnostics` mutations to include mandatory columns.

#### [MODIFY] [DiagnosticsScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DiagnosticsScreen.tsx)
- [ ] Resolve inline query type errors.

## Verification Plan

### Automated Tests
- [ ] Run `npx tsc --noEmit --skipLibCheck` and ensure 0 errors.
- [ ] Verify Supabase client `Database` generic is properly propagated.

### Manual Verification
- [ ] Test device registration flow on Web/Android.
- [ ] Verify Crew Hub session creation and persistence.
