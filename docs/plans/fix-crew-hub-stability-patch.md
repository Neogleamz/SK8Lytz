# Implementation Plan: Crew Hub Stability Patch (Revised)

> **Audit Date:** 2026-04-12 | Re-audited against live codebase before execution.

## Background

The original April 7th audit identified 3 concerns: JSX stacking, a privacy leak in
`getNearbyPublicSessions`, and a session-matching logic gap. A re-audit of the live
codebase on 2026-04-12 confirms that **all three original items were already resolved**
in a prior commit. One new type-gap was discovered during the audit.

## Proposed Changes

### [MODIFY] LocationService.ts — NearbySession Interface Gap

The `getNearbyPublicSessions()` mapper at line 174 correctly sets `isPublic: s.is_public ?? true`
on each result, but the `NearbySession` export interface does not declare the `isPublic` field.
This means any consumer using `NearbySession` with strict typing cannot safely access `.isPublic`.

**Fix:** Add `isPublic: boolean` to the `NearbySession` interface.

### [MODIFY] LocationService.ts — Boy Scout: raw console calls

Lines 38, 67, and 125 use raw `console.log`/`console.warn` instead of structured `AppLogger`.
Replacing these brings the file in line with the telemetry standards established by `chore/telemetry-standards`.

---

## Verification Plan

### Automated

- `npx tsc --noEmit` — zero errors expected (the new field is already mapped, adding it to the interface is additive-only).

### Manual

1. Open Crew Hub → Live Near You section.
2. Confirm public sessions show "🌍 Public" label.
3. Confirm private sessions (user is member) show "🔒 Private" label.

## Status of Original Items (Resolved Prior)

| Original Item                                            | Resolution                                                                        |
| -------------------------------------------------------- | --------------------------------------------------------------------------------- |
| JSX stacking in CrewModal ~line 1888                     | Resolved — `<>` fragment wraps all detail-view elements intentionally; tsc passes |
| Privacy leak in `getNearbyPublicSessions`                | Resolved — dual-query: public filter + member-gated private filter                |
| Session matching by `crew_id` in `getLiveSessionForCrew` | Resolved — `crew_id` match first, name fallback for legacy sessions               |
| "Invite & Add Sktaters" typo                             | Resolved — reads "Add Skaters" in current code                                    |
| `createSession` crewId parameter                         | Resolved — `opts.crewId` flows through to DB insert                               |
