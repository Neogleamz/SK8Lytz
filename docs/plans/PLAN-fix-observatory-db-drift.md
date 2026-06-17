# Implementation Plan

## Goal
Resolve 3 critical database errors identified by the self-healing observatory: schema drift on `label_designs`, telemetry constraint violations, and smallint overflows.

## Proposed Changes

### Supabase Migrations
#### [NEW] supabase/migrations/20260617_observatory_fixes.sql
- Add `ALTER TABLE label_designs ADD COLUMN IF NOT EXISTS product_name text;`
- Add `ALTER TABLE crash_telemetry ALTER COLUMN [column_name] TYPE integer;` (to fix smallint overflow).
- Source: [observatory-report](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/observatory/reports/2026-06-17/report.md)

### App Telemetry Service
#### [MODIFY] src/services/TelemetryService.ts (or equivalent)
- Ensure the `severity` passed to `crash_telemetry` strictly matches the Postgres check constraint (`INFO`, `WARNING`, `ERROR`, `FATAL`).
- Normalize uppercase string before insert.

## Verification Plan
### Automated Tests
- Run `npm run verify`
### Manual Verification
- Apply the Supabase migration locally and verify the insert constraints via Supabase dashboard or MCP tool.
