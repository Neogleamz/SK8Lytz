# Implementation Plan

## Task: sweep-native-watch
**Slug:** sweep-native-watch
**Wave:** [WAVE:1] — Parallel-safe with other Wave 1 clusters
**Size:** [Snack] — 4 files
**Risk:** [M-RISK] — Native Swift/Kotlin; no TypeScript changes
**Status:** [✅ READY]
**Source of Truth:** `artifacts/system_audit_report.md` + `artifacts/deepdive_raw/DOMAIN_NATIVE_AND_WATCH_findings.json`

## Goal
Fix 10 findings in the native Watch and Wear OS layers. Critical: `WatchConnectivityManager.swift` modifies `@Published` properties on the WCSession background queue — this is a guaranteed crash on iOS (must dispatch to `DispatchQueue.main`). Fix the non-atomic SharedPreferences read-modify-write in `WearMessageSender.kt`. Fix the Wear OS exercise type mismatch. Align iOS watch bridge event payload with the Master Reference schema.

## Decision Log
- **`@Published` on background thread (CONFIRMED — R-20 HIGH crash risk)**: WCSession delegates fire on a background serial queue. Updating `@Published`/`@ObservedObject` properties off main thread causes `SwiftUI` rendering crashes. All `@Published` mutations must be wrapped in `DispatchQueue.main.async { }`.
- **SharedPreferences non-atomic write (R-11 HIGH)**: The Wear OS `WearMessageSender` reads, modifies, and writes the SharedPreferences buffer in three separate non-atomic operations. Under concurrent health data delivery this corrupts the buffer. Must use `edit().apply()` with an atomic transformation or `commit()` inside a `synchronized` block.
- **Exercise type**: `ExerciseType.INLINE_SKATING` is the correct Wear OS type. `HealthTracker.kt` currently uses a mismatched type.

## Files to Create/Modify

### [MODIFY] targets/watch/WatchConnectivityManager.swift
- Wrap all `@Published` property mutations in `DispatchQueue.main.async { }` inside WCSession delegate callbacks (L105)
- Route `print` error output at L57 through proper Swift logging (OSLog)

### [MODIFY] targets/watch/HealthManager.swift
- Change `HKWorkoutSession` activity type at L52 to `.inlineSkating` (per Master Reference mandate)
- Fix background delivery entitlement declaration in `expo-target.config.js` to match

### [MODIFY] android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/WearMessageSender.kt
- Replace non-atomic SharedPreferences read-modify-write at L85 with a single atomic `edit().apply()` operation inside a `synchronized(this)` block

### [MODIFY] android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/services/HealthTracker.kt
- Change `ExerciseType` at L64 to `ExerciseType.INLINE_SKATING`

## Out of Scope
- `WearableCommunicationService.kt` listener iteration fix (LOW — deferred)
- `Sk8lytzTileService.kt` raw thread fix (LOW — deferred)
- No TypeScript or React Native changes

## Verification Plan
- Manual review of Swift diff — confirm all `@Published` mutations are on main thread
- Manual review of Kotlin diff — confirm atomicity of SharedPreferences write
- `npm run verify` for any JS/TS touch points
