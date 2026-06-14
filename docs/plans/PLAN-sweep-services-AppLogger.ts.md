# Implementation Plan: sweep-services-AppLogger.ts

## Goal
Fix static audit findings for the `sweep-services-AppLogger.ts` domain cluster.

## Proposed Changes

### [MODIFY] [AppLogger.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppLogger.ts)
- **Line:** 1
- **Rule:** R-23
- **Severity:** HIGH | **Confidence:** CONFIRMED
- **Description:** Centralized diagnostic logging engine that buffers, processes, and uploads system telemetry, performance snapshots, and crash records.
- **Suggested Fix:** Extract local log buffer rotation, background ingestion sync loops, and telemetry usage calculators into isolated service files.

### [MODIFY] [AppLogger.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppLogger.ts)
- **Line:** 289
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Awaited promise for storage/network IO without try-catch block or chained .catch: "await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(this.buffer))"
- **Suggested Fix:** Wrap the call in a try-catch block or append .catch() to handle rejection.

### [MODIFY] [AppLogger.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppLogger.ts)
- **Line:** 40
- **Rule:** R-24
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** AppLogger uses local string literal key '@Sk8lytz_logs' for legacy migration, bypassing storageKeys.ts.
- **Suggested Fix:** Move LEGACY_KEY to storageKeys.ts and import.

### [MODIFY] [AppLogger.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppLogger.ts)
- **Line:** 424
- **Rule:** R-08
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Launders type to array of unknown to safely perform mapping operation on dynamic logging properties.
- **Suggested Fix:** Check for array type explicitly at runtime and cast to Array/unknown[] safely.

### [MODIFY] [AppLogger.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppLogger.ts)
- **Line:** 471
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** Unawaited floating promise for storage/network IO without chained .catch: "supabase.from('telemetry_errors').insert({
          session_id: this.sessionId,
          event_type: event,
          error_message: safeErrorString.substring(0, 500),
          stack_trace: payload.stack || payload.stackTrace || null,
          raw_context: {
            ...payload,
            host_device_id: Device.osInternalBuildId || Device.modelId || 'unknown'
          },
          payload_size: payload.payload_size || null,
          operation_type: payload.operation_type || null
        }).then(
          ({ error }) => {
            if (error) console.warn('[AppLogger] VIP Fast-Lane failed:', error.message);
          },
          (e: unknown) => console.warn('[AppLogger] VIP insert failed (network):', e instanceof Error ? e.message : String(e))
        )"
- **Suggested Fix:** Append .catch() or await/wrap with try-catch.

### [MODIFY] [AppLogger.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppLogger.ts)
- **Line:** 489
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** Unawaited floating promise for storage/network IO without chained .catch: "supabase.from('crash_telemetry').insert({
          error_signature: safeErrorString.substring(0, 500),
          stack_trace: payload.stack || payload.stackTrace || null,
          // R-08: Supabase Json type requires this cast — breadcrumbs is a plain
          // object array that is structurally compatible with Json but TS cannot
          // verify the recursive constraint without the intermediate unknown step.
          breadcrumbs: FlightRecorder.getBreadcrumbs() as unknown as import('../types/supabase').Json,
          environment_state: {
            ...payload,
            host_device_id: Device.osInternalBuildId || Device.modelId || 'unknown',
            session_id: this.sessionId,
            event_type: event
            // R-08: same Json structural cast — required by Supabase schema types.
          } as unknown as import('../types/supabase').Json,
          severity: 'CRITICAL',
          app_version: Device.osVersion || null
        }).then(({ error }) => {
          if (error && __DEV__) console.warn('[AppLogger] Crash Telemetry dual-write failed:', error.message);
        }, (e: unknown) => {
          if (__DEV__) console.warn('[AppLogger] Crash Telemetry insert rejected (network):', e instanceof Error ? e.message : String(e));
        })"
- **Suggested Fix:** Append .catch() or await/wrap with try-catch.

### [MODIFY] [AppLogger.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppLogger.ts)
- **Line:** 495
- **Rule:** R-08
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Launders raw breadcrumbs array to Supabase DB's strict Json type structure.
- **Suggested Fix:** Explicitly match target interface structure or use safe Json helpers.

### [MODIFY] [AppLogger.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppLogger.ts)
- **Line:** 502
- **Rule:** R-08
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Launders metadata payload object to Supabase Json type structure.
- **Suggested Fix:** Utilize typed serialization format or map properties properly.

### [MODIFY] [AppLogger.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppLogger.ts)
- **Line:** 670
- **Rule:** R-15
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** AppLogger contains a legacy auth bypass call (TODO) referencing direct supabase.auth.getUser() instead of injecting the user ID via a context setter.
- **Suggested Fix:** Utilize the already available AppLogger.setCurrentUser() setter on AuthContext change, and fully eliminate any internal direct supabase.auth references.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
