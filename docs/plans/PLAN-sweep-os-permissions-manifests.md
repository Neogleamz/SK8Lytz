# Implementation Plan

## Task: sweep-os-permissions-manifests
**Slug:** sweep-os-permissions-manifests
**Wave:** [WAVE:1] — Parallel-safe with other Wave 1 clusters
**Size:** [Snack] — 2 files
**Risk:** [H-RISK] — Android manifest and app.config; affects build and runtime permissions
**Status:** [✅ READY]
**Source of Truth:** `artifacts/system_audit_report.md` + `artifacts/deepdive_raw/DOMAIN_OS_PERMISSIONS_findings.json`

## Goal
Fix 8 findings in OS permission declarations and build manifests. Critical: the Google Maps API key is hardcoded as a plaintext string in `AndroidManifest.xml` — it must be moved to an environment variable injected at build time. Fix the HealthKit activity type mismatch in `app.config.js`. Trim the excessive `foregroundServiceType` combo on the foreground service declaration.

## Decision Log
- **Maps API key in manifest (CONFIRMED — R-09 HIGH PII leak)**: Hardcoded secrets in manifests are committed to git history and visible to anyone with repo access. Must move to `EXPO_PUBLIC_GOOGLE_MAPS_API_KEY` or equivalent env var injected via `app.config.js` `android.config.googleMaps.apiKey`.
- **HealthKit activity type**: Master Reference mandates inline skating activity type. `expo-target.config.js` declares `com.apple.developer.healthkit.background-delivery` for the wrong activity — must align.
- **Foreground service types**: Declaring `health|dataSync|shortService` alongside `connectedDevice|location` is over-broad. Android 14+ enforces type-to-permission matching strictly.

## Files to Create/Modify

### [MODIFY] android/app/src/main/AndroidManifest.xml
- Replace hardcoded Google Maps API key string at L29 with `${GOOGLE_MAPS_API_KEY}` manifest placeholder
- Remove `health` and `dataSync` from `foregroundServiceType` at L33 — keep only `connectedDevice|location` (or `connectedDevice` only if location is covered by the permission)

### [MODIFY] app.config.js
- Add `android.config.googleMaps.apiKey: process.env.EXPO_PUBLIC_GOOGLE_MAPS_API_KEY` to inject the API key at build time instead of hardcoding in manifest
- Fix `bluetoothAlwaysPermission` — ensure it is paired with a matching `NSBluetoothAlwaysUsageDescription` (L75)
- Fix `NSLocationWhenInUseUsageDescription` — add `NSLocationAlwaysAndWhenInUseUsageDescription` for background location (L22)
- Fix HealthKit usage strings at L15 to align with inline skating activity type per Master Reference

## Out of Scope
- No changes to Wear OS manifest (separate cluster `sweep-native-watch`)
- No changes to `targets/watch/Info.plist` (empty stub, deferred)
- No React Native source code changes

## Verification Plan
- `npm run verify` — TSC must pass on `app.config.js`
- Confirm `EXPO_PUBLIC_GOOGLE_MAPS_API_KEY` is documented in `.env.example`
- Manual review that manifest placeholder is correctly wired
