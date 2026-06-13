# Implementation Plan: BATCH:sweep-utils

## Proposed Changes

### Domain: utils

#### [MODIFY] src/utils/FlightRecorder.ts
- Line 5 (R-08): any type used for Breadcrumb data property
- Line 12 (R-08): any type used for leaveBreadcrumb data parameter

#### [MODIFY] src/utils/migrateAuthTokens.ts
- Line 4 (R-24): Hardcoded AsyncStorage key instead of centralized constants
- Line 5 (R-24): Hardcoded SecureStore/AsyncStorage key instead of centralized constants
- Line 21 (R-04): Bypassing AppLogger telemetry context and formatting by using raw console.info
- Line 27 (R-04): Bypassing AppLogger telemetry context and formatting by using raw console.error
- Line 27 (R-04): Error logged without payload_size or ssi context

#### [MODIFY] src/utils/CrashReporter.ts
- Line 4 (R-04): Error logged without payload_size or ssi context
