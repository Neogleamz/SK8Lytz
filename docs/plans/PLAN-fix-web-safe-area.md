# Implementation Plan: Fix Web Demo SafeArea Crash

## Goal
Resolve the `SafeAreaContext.js:95 Uncaught Error: No safe area value available` crash occurring on React Native Web by providing a zeroed-out `initialMetrics` fallback to the `<SafeAreaProvider>` in `App.tsx`.

## Proposed Changes

### App.tsx
- **[MODIFY]** `App.tsx`
  - Define `INITIAL_METRICS` with zeroed out frame and insets for `Platform.OS === 'web'`, otherwise `undefined`.
  - Pass `INITIAL_METRICS` to the `initialMetrics` prop of `<SafeAreaProvider>`.

## Files to Create/Modify
- `App.tsx`

## Verification Plan
1. Run `npm run verify` to ensure TypeScript compilation remains strict and no tests are broken.
2. Web Demo will automatically hot-reload and pass the initial frame without crashing.
