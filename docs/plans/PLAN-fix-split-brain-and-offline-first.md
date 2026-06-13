# Implementation Plan: Fix Split-Brain and Offline-First Violations

## Goal
Eradicate duplicated state architectures (Split-Brain) and ensure Offline-First mandates are respected globally.

## Source of Truth
📖 [system_audit_report.md](../../C:/Users/Magma/.gemini/antigravity/brain/79cf6856-67a1-49d0-aadc-9079eee6c7ae/system_audit_report.md)

## Identified Issues
1. **[R-21] Split-Brain:** Parallel existences of `useBLEScanner` vs `useBLESweeper`.
2. **[R-21] Dual-State Persistence:** `useDeviceStateLedger` vs `useControllerPersistence`.
3. **[R-05] Offline-First Bypass:** `useProductCatalog` and `ScenesService` block on network calls before rendering AsyncStorage fallbacks.

## Proposed Changes
- **`src/hooks/ble/useBLESweeper.ts`**: Archive and migrate all remaining logic directly into `useBLEScanner.ts` to centralize device discovery.
- **`src/hooks/useControllerPersistence.ts`**: Condemn and delete. Route all persistence operations to `useDeviceStateLedger`.
- **`src/hooks/useProductCatalog.ts` & `src/services/ScenesService.ts`**: Invert data loading. Load from AsyncStorage first, emit to UI, then fetch remote in background and apply delta.
