# Implementation Plan: C14 — Split-Brain Consolidation

## Goal
Refactor useDashboardGroups to read through GroupRepository; remove duplicate lifetime stats.

## Rules Addressed
- R-21: Split-Brain (3 HIGH findings)

## Files to Create/Modify
- `src/services/GroupRepository.ts` — Add read methods used by useDashboardGroups
- `src/hooks/useCrewSession.ts` — Remove duplicate lifetime_distance/speed updates

## Implementation Steps
1. View GroupRepository.ts. Add missing read methods.
2. View useCrewSession.ts. Remove duplicate stat updates that bypass SpeedTrackingService.
3. Verify: git diff shows consolidation, no new split-brain introduced.
4. Run npm run verify.

## Out of Scope
- useDashboardGroups.ts (C9), DockedController.tsx (C4)
