### Design Decisions & Rationale

We are implementing a targeted codebase-wide sweep to eliminate 79 TypeScript compilation errors. Most of these errors are minor typings rot caused by recent architectural changes (e.g. `AppLogger` adoption, Dynamic Product Catalog, and `EventType` dictionary expansions). Rather than forcing drastic refactoring, we will surgically apply correct, explicit typing to appease strict mode, delete dead temp scripts, and cast API error objects explicitly as `Error` or `any` rather than `unknown` in `catch` blocks.

## Proposed Changes

### `App.tsx`

- **[MODIFY]** Define `global.ErrorUtils` via an ambient declaration wrapper for React Native's global error handler.
- **[MODIFY]** Update import/usage of `EventType` to include "AUTH_ERROR" or gracefully cast.

### `temp_telemetry_test.ts`

- **[DELETE]** Delete this leftover script that is throwing `dotenv` resolution errors.

### `src/components/AccountModal.tsx`

- **[MODIFY]** Add missing `step?: number` definition to `CustomSliderProps`.
- **[MODIFY]** Change `styles.textPrimary` to the proper fallback (e.g. `Typography.body`).

### `src/components/AdminToolsModal.tsx`

- **[MODIFY]** Use `Partial<Record<EventType, {...}>>` to avoid TS complaining that not _all_ events from the global enum are mapped in the admin tool icons payload.

### `src/components/DockedController.tsx`

- **[MODIFY]** Provide fallback defaults for state setters: `setState(val ?? 0)`, `setMode(val ?? "FOREGROUND")`.
- **[MODIFY]** Add `!` or `??` fallbacks where `fav.color` is assumed to be defined.
- **[MODIFY]** Remove `isActive` property check on `ProductProfile` (it is not part of the active interface).
- **[MODIFY]** Force specific casting `as readonly [ColorValue, ColorValue, ...ColorValue[]]` for `LinearGradient` colors.

### `src/hooks/useRegistration.ts`

- **[MODIFY]** Add `import { AppLogger } from '../services/AppLogger'` to fix the missing identifier.

### `src/hooks/useBLE.ts` & `src/screens/DashboardScreen.tsx`

- **[MODIFY]** Explicitly cast `catch (error: any)` or check `(error as Error).message` instead of passing `unknown` objects to the telemetry logger or state handlers.
- **[MODIFY]** Remove `styles.typography` reference in DashboardScreen and use `Typography` from the theme directly.

### `src/hooks/useProductCatalog.ts`

- **[MODIFY]** Add `batteryCapacityMilliAmpereHour` optional field or default to `ProductProfile` type if it's strictly required by dummy data.

### `src/screens/Onboarding/HardwareSetupWizardScreen.tsx`

- **[MODIFY]** Fix `scanForPeripherals({ keepAlive: true })` call (update the hook's signature to accept the object).

### `src/screens/SkateMapScreen.tsx`

- **[MODIFY]** Remove `backdropFilter` from styles (invalid RN style) and fix the nullable coalescing for Supabase row inserts.

### `src/services/SpeedTrackingService.ts`

- **[MODIFY]** Explicitly type the accumulator and item parameters inside array `reduce` iteration `.reduce((s: number, r: number) => s + r, 0)`.

## Verification Plan

1. Delete `temp_telemetry_test.ts`.
2. Apply changes sequentially using editing tools.
3. Rerun `npx tsc --noEmit`. We will consider the audit successful when 0 errors are returned.
