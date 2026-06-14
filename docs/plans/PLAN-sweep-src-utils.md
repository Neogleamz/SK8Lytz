# Implementation Plan: sweep-src-utils

## Goal
Fix bugs and rule violations identified during the deep-dive code hunt for the `sweep-src-utils` domain cluster.

## Batch & Wave
- **Wave:** 1
- **Prerequisite:** None

## Proposed Changes
### [MODIFY] migrateAuthTokens.ts
- Line 25: Fix `R-06` violation. Passes the raw caught error e: unknown directly to AppLogger.error without standard e instanceof Error unwrapping/normalizing. (Suggested: AppLogger.error('Failed to migrate auth tokens', e instanceof Error ? e : new Error(String(e)), { payload_size: 0, ssi: 0 });)
- Line 20: Fix `R-04` violation. AppLogger.info call is missing telemetry context parameters: payload_size, ssi. Rule R-04 mandate that payload_size and ssi must be provided for all telemetry entries. (Suggested: AppLogger.info(message, { ...context, payload_size: 0, ssi: 0 });)
### [MODIFY] BlePayloadParser.ts
- Line 83: Fix `R-04` violation. AppLogger.warn call is missing telemetry context parameters: payload_size, ssi. Rule R-04 mandate that payload_size and ssi must be provided for all telemetry entries. (Suggested: AppLogger.warn(message, { ...context, payload_size: 0, ssi: 0 });)
- Line 105: Fix `R-04` violation. AppLogger.warn call is missing telemetry context parameters: payload_size, ssi. Rule R-04 mandate that payload_size and ssi must be provided for all telemetry entries. (Suggested: AppLogger.warn(message, { ...context, payload_size: 0, ssi: 0 });)

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.
