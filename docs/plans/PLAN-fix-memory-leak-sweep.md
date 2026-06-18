# Implementation Plan: fix/memory-leak-sweep

## Goal
Fix useEffect cleanup omissions, stale closures, and OS variance issues across UI components.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Clusters R-22 + R-12

## Findings to Resolve
1. R-22: AccountModal.tsx L84 — useEffect without cleanup
2. R-22: ProductVisualizer.tsx L77 — useEffect without cleanup
3. R-22: PatternCard.tsx L37 — useEffect without cleanup
4. R-22: MarqueeText.tsx L17 — useEffect without cleanup
5. R-22: CommunityModal.tsx L33 — useEffect without cleanup
6. R-20: SessionSummaryModal.tsx L207 — Platform.OS boxShadow instead of Platform.select
7. R-12: useAppMicrophone.ts L98 — Stale closure in setInterval

## Files to Create/Modify

### [MODIFY] [AccountModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AccountModal.tsx)
### [MODIFY] [ProductVisualizer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/ProductVisualizer.tsx)
### [MODIFY] [PatternCard.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/PatternCard.tsx)
### [MODIFY] [MarqueeText.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/MarqueeText.tsx)
### [MODIFY] [CommunityModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CommunityModal.tsx)
### [MODIFY] [SessionSummaryModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/SessionSummaryModal.tsx)
### [MODIFY] [useAppMicrophone.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useAppMicrophone.ts)

## Verification
- `npm run verify`

## Out of Scope
- DockedController (Wave 3)
- CrewMemberDashboard (Wave 4)
