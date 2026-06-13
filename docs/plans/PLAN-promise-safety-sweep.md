# Implementation Plan

## PLAN-promise-safety-sweep — Unhandled Promise / IO Safety
*Source: `/deepdive-code-hunt` fleet | Rules: R-11 | Date: 2026-06-10*

### Problem
23 async operations across 13 files have no `try/catch`, no `.catch()` handler, or are called fire-and-forget with no error boundary. On BLE or network failure, these throw unhandled promise rejections that crash the wizard flow or silently swallow errors.

### Source of Truth
- `artifacts/system_audit_report.md` — CLUSTER-02
- `tools/SK8Lytz_App_Master_Reference.md` §Hooks

### Affected Files
| File | Violation | Severity |
|---|---|---|
| `src/screens/Onboarding/HardwareSetupWizardScreen.tsx:64` | `fireOrientationTest` async with no catch | HIGH |
| `src/screens/Onboarding/HardwareSetupWizardScreen.tsx:601` | Step 3 handler has try/finally but no catch | HIGH |
| `src/context/SessionContext.tsx:240` | `setupNotification()` fire-and-forget | HIGH |
| `src/hooks/useAdminTelemetry.ts:49` | `clearLogs` AsyncStorage without try/catch | HIGH |
| `src/hooks/useAdminTelemetry.ts:55` | `exportJSON()` outside try block | HIGH |
| `src/services/PushTokenService.ts:22,36` | Two Supabase writes with no error handling | HIGH |
| `src/hooks/useDashboardGroups.ts:349` | Deregister onPress with no catch | HIGH |
| `src/components/docked/QuickPresetModal.tsx:75,89,108` | Three fire-and-forget AsyncStorage + network calls | MEDIUM |
| `src/context/AuthContext.tsx:119` | `Linking.getInitialURL()` no `.catch` | MEDIUM |
| `src/context/SessionContext.tsx:406` | `notifee.stopForegroundService()` unawaited | MEDIUM |
| `src/screens/AuthScreen.tsx:57,73` | AsyncStorage `.then` without `.catch` | MEDIUM |
| `src/hooks/useFavorites.ts:42,97` | Two AsyncStorage `.then` chains without `.catch` | MEDIUM |
| `src/services/AppLogger.ts:460` | Supabase VIP fast-lane inserts without `.catch` | LOW |
| `src/context/ThemeContext.tsx:29-32,40,48` | Three uncaught AsyncStorage calls | LOW |
| `src/services/LocationService.ts:261` | getCachedSpots without try/catch | MEDIUM |
| `src/components/auth/DevSandboxDrawer.tsx:19` | AsyncStorage.getItem no `.catch` | LOW |

### Implementation Steps
1. Wrap `fireOrientationTest` body in `HardwareSetupWizardScreen` in try/catch
2. Add catch block to Step 3 completion handler
3. Change `setupNotification()` call site to `setupNotification().catch(err => AppLogger.error(...))`
4. Wrap `clearLogs` and `exportLogs` body fully in try/catch in `useAdminTelemetry`
5. Add try/catch to both Supabase writes in `PushTokenService`
6. Add `.catch` to all AsyncStorage fire-and-forget calls in `QuickPresetModal`, `AuthScreen`, `useFavorites`, `ThemeContext`, `DevSandboxDrawer`
7. Add `.catch` to `Linking.getInitialURL()` in `AuthContext`
8. Fix `notifee.stopForegroundService()` in `SessionContext:406` to await inside try/catch
9. Add `.catch(() => {})` to AppLogger's Supabase fast-lane inserts (silent suppression is acceptable for logger)

### Verify
- `npm run verify`
- Manual: trigger BLE disconnection during wizard flow — should surface error, not crash
