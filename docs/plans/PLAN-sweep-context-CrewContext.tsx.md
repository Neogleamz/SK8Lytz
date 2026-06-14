# Implementation Plan: sweep-context-CrewContext.tsx

## Goal
Fix static audit findings for the `sweep-context-CrewContext.tsx` domain cluster.

## Proposed Changes

### [MODIFY] [CrewContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/CrewContext.tsx)
- **Line:** 14
- **Rule:** R-27
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** CrewProvider aggregates three heavy custom hooks (useCrewHub, useCrewManage, useCrewSession) into a single context. This causes deep consumer components to re-render on any unrelated state update.
- **Suggested Fix:** Partition CrewContext into smaller specialized contexts (e.g. CrewHubContext, CrewSessionContext) or wrap consumer sub-trees in useMemo.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
