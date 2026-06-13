# Implementation Plan: Fix OS Parity and Build Configs

## Goal
Resolve iOS/Android parity differences, fix missing OS permissions, and scrub hardcoded PII from build targets.

## Source of Truth
📖 [system_audit_report.md](../../C:/Users/Magma/.gemini/antigravity/brain/79cf6856-67a1-49d0-aadc-9079eee6c7ae/system_audit_report.md)

## Identified Issues
1. **[R-20] Build Config Drift:** Missing Android 14+ foreground service types and missing iOS `NSLocationWhenInUseUsageDescription`.
2. **[R-09] PII Scrubbing:** Hardcoded Google Maps API keys left in `app.config.js`.
3. **[R-20] Style Variance:** `Platform.OS === 'ios'` checks bypassing standard responsive UI structures.

## Proposed Changes
- **`app.config.js`**: 
  - Add `FOREGROUND_SERVICE_CONNECTED_DEVICE` under Android permissions.
  - Add `NSLocationWhenInUseUsageDescription` under iOS infoPlist.
  - Extract Google Maps API keys into `.env` references.
- **UI Components**: Scan for `Platform.OS` shadow/elevation checks and extract them into a centralized `theme.shadows` utility.
