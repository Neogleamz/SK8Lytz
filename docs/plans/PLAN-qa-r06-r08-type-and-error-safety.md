# Implementation Plan: Type & Error Safety Sweep (R-06/R-08)

## 📖 Source of Truth
Source: `docs/audits/system_audit_report_2026_06_07.md`:[Line 35-41]

## 🎯 Goal
Resolve all [R-06] (Missing `e instanceof Error`) and [R-08] (Type Safety `any` casts) violations globally across the `src/` directory.

## 🛠️ Proposed Changes

### [MODIFY] `src/types/ble.types.ts` & `src/types/dashboard.types.ts`
- Remove `Record<string, any>` and `[key: string]: any` interfaces.
- Replace with strictly typed unions or `unknown` where generics apply.

### [MODIFY] `src/services/DeviceRepository.ts` & `src/services/CrewProfileService.ts`
- Replace all instances of `catch (e: any)` and `String(e)` with standard unwrapping: `const errorMsg = e instanceof Error ? e.message : String(e);`

### [MODIFY] `src/services/TelemetryService.ts`
- Ensure error payloads explicitly type `e` and attach `rssi` from context.

## ✅ Verify
- Run `npx tsc --noEmit` and confirm 0 errors related to implicit `any` or `catch` bindings.
- Confirm `npm run verify` succeeds.

## 🛑 Out of Scope
- Modifying UI components or introducing new error boundary React components.
