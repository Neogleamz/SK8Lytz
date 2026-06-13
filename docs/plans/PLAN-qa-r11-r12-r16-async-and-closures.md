# Implementation Plan: Async & Closure Safety Sweep (R-11/R-12/R-16)

## 📖 Source of Truth
Source: `docs/audits/system_audit_report_2026_06_07.md`:[Line 22, 51]

## 🎯 Goal
Eliminate unhandled promises (R-11), stale closures in intervals (R-12), and hardcoded timeout traps (R-16).

## 🛠️ Proposed Changes

### [MODIFY] `src/hooks/useAppMicrophone.ts` & `src/hooks/useStreetMode.ts`
- Introduce `useRef` to capture the latest state callback to prevent stale closures inside `setInterval`.
- Wrap `useEffect` cleanup blocks to explicitly call `clearInterval()`.

### [MODIFY] `src/hooks/ble/useBLE.ts` & `src/hooks/ble/useBLEScanner.ts`
- Attach `.catch(e => AppLogger.error('ASYNC_STORAGE_FAIL', e))` to all floating `AsyncStorage.getItem().then(...)` chains.

### [MODIFY] `App.tsx`
- Add a top-level `.catch` handler to the initial hydration boot sequence.

## ✅ Verify
- Search codebase for `.then` without `.catch` to confirm isolation.
- Test background mic toggle to ensure the callback doesn't execute against an old state snapshot.

## 🛑 Out of Scope
- Refactoring `AsyncStorage` to a different persistence layer (e.g., MMKV).
