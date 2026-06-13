# Implementation Plan: refactor/deep-dive-type-safety

## Goal
Eradicate all `any` casts from `NOTIFICATIONS_&_ROUTING`, `GROUP_SYNC`, `HARDWARE_PROTOCOLS`, and `IDENTITY`.

## Source of Truth
`src/components/crew/CrewLandingScreen.tsx` + `CONSTITUTION.md`

## Proposed Changes

### NOTIFICATIONS_&_ROUTING
- **[MODIFY]** `App.tsx`: Remove `any` casts for `ErrorUtils`, `setGlobalHandler`, and console patches. Replace with proper types or `unknown`.
- **[MODIFY]** `src/services/NotificationService.ts`: Remove `any` casts.

### GROUP_SYNC
- **[MODIFY]** `src/components/crew/CrewDetailScreen.tsx`: Remove `any` casts (L50, L61). Use proper Supabase schema types.
- **[MODIFY]** `src/components/crew/CrewLandingScreen.tsx`: Remove `any` casts in catch blocks (L59, 92, 112, 135) using `e instanceof Error ? e.message : String(e)`.

### HARDWARE_PROTOCOLS
- **[MODIFY]** `src/protocols/ControllerRegistry.ts` & `ZenggeProtocol.ts`: Remove `any` casts for `_appLogger`. Replace with dependency injection or proper typing.

### IDENTITY
- **[MODIFY]** `src/services/AuthProfileService.ts`, `CrewProfileService.ts`: Remove `any` casts for rows and parameters. Utilize `Tables<'profile'>`.
- **[MODIFY]** `src/components/account/*`: Remove `any` casts for component props. Define proper interfaces.

## Verification Plan
1. **Automated Tests**: Run `npm run verify` to ensure TypeScript compilation succeeds without strict type errors.
2. **Manual Verification**: Test Crew and Account UI to ensure no runtime crashes.
