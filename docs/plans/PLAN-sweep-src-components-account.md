# Implementation Plan: sweep-src-components-account

## Goal
Fix bugs and rule violations identified during the deep-dive code hunt for the `sweep-src-components-account` domain cluster.

## Batch & Wave
- **Wave:** 1
- **Prerequisite:** None

## Proposed Changes
### [MODIFY] account.types.ts
- Line 111: Fix `R-08` violation. The property saveNotifPrefs in the AccountTabSettingsProps interface is typed with the explicit 'any' type for its parameter 'prefs'. (Suggested: Change the parameter type of 'prefs' to a strict type or union matching the notification preferences format, e.g., 'prefs: { crewInvites: boolean; sessionReminders: boolean; leaderHandoff: boolean }'.)
### [MODIFY] SkaterStatsPanel.tsx
- Line 44: Fix `R-04` violation. AppLogger.warn call is missing telemetry context parameters: payload_size, ssi. Rule R-04 mandate that payload_size and ssi must be provided for all telemetry entries. (Suggested: AppLogger.warn(message, { ...context, payload_size: 0, ssi: 0 });)
- Line 62: Fix `R-04` violation. AppLogger.warn call is missing telemetry context parameters: payload_size, ssi. Rule R-04 mandate that payload_size and ssi must be provided for all telemetry entries. (Suggested: AppLogger.warn(message, { ...context, payload_size: 0, ssi: 0 });)
- Line 41: Fix `R-24` violation. AsyncStorage key collision and privacy leak. SkaterStatsPanel read/write is bound to a single static cache key (@sk8lytz_lifetime_stats_cache) via STORAGE_LIFETIME_STATS_CACHE, while SpeedTrackingService manages user-specific cached lifetime stats using a dynamic prefix key (@sk8lytz_lifetime_stats_${userId}). This causes a split-brain where user-specific stats are never shown in SkaterStatsPanel, and switching users leaks the previous skater's stats to the new user. (Suggested: Update SkaterStatsPanel to construct the cache key dynamically using the current user's ID (similar to SpeedTrackingService) instead of using the static global STORAGE_LIFETIME_STATS_CACHE key.)
### [MODIFY] AccountTabStats.tsx
- Line 13: Fix `R-14` violation. AccountTabStats displays user skate stats and accepts a statsLoading boolean prop, but lacks custom UI states for data fetching errors or empty stat history (renders zero metrics instead of an empty state screen). (Suggested: Add a statsError prop to render an ErrorCard on failure, and render EmptyState when skated statistics are entirely empty.)
### [MODIFY] types.ts
- Line 3: Fix `R-21` violation. Deprecated re-export file: 'src/components/account/types.ts' exists solely to re-export all from './account.types' to prevent breaking legacy imports and circular dependencies. Having two files named 'types.ts' and 'account.types.ts' in the same folder causes redundant redirection. (Suggested: Migrate any remaining consumers referencing 'components/account/types' to import directly from 'components/account/account.types' and delete 'components/account/types.ts'.)

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.
