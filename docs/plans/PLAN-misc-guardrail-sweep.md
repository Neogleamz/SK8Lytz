# Implementation Plan

## PLAN-misc-guardrail-sweep — Misc Guardrails (R-09, R-20, R-21, R-22, R-26, R-27, R-12, R-17)
*Source: `/deepdive-code-hunt` fleet | Rules: R-09, R-20, R-21, R-22, R-26, R-27, R-12, R-17 | Date: 2026-06-10*

### Source of Truth
- `artifacts/system_audit_report.md` — CLUSTER-09

### R-09 — PII Scrubbing (MAC Address Leaks)
| File | Line | Issue |
|---|---|---|
| `src/services/DeviceRepository.ts:793` | 793 | MAC via string concatenation in telemetry |
| `src/services/ble/ConnectService.ts:167` | 167 | MAC via template literal |

**Fix:** Replace with `scrubPII(mac)` or pass `mac` strictly as a key in the context object.

### R-20 — OS Variance (HIGH PRIORITY)
| File | Line | Issue |
|---|---|---|
| `android/app/src/main/AndroidManifest.xml:8` | 8 | BLUETOOTH_SCAN missing `neverForLocation` attribute |
| `src/services/SessionShareService.ts:17` | 17 | Blind `Platform.OS === 'ios'` ternary, no web case |
| `src/components/admin/tools/AdminPicksScheduler.tsx:187` | 187 | `Platform.OS !== 'ios'` assumes Android |
| `src/screens/DashboardScreen.tsx:372` | 372 | Blind cross-platform assumption |
| `src/screens/AuthScreen.tsx:30` | 30 | Direct `Platform.OS` string comparison |

**Fix AndroidManifest.xml:** `<uses-permission android:name="android.permission.BLUETOOTH_SCAN" android:usesPermissionFlags="neverForLocation"/>`
**Fix JS files:** Use `Platform.select({ ios: ..., android: ..., default: ... })`

### R-21 — Split Brain / Duplication
| File | Issue |
|---|---|
| `src/hooks/useBLE.ts` + `src/hooks/useControllerDispatch.ts` | Duplicated dispatch logic — extract to shared utility |
| `android/app/src/main/AndroidManifest.xml` | Duplicate permission declarations |

### R-22 — Memory Leaks
| File | Issue |
|---|---|
| `src/hooks/useDeviceStateLedger.ts` | Global object mutation without cleanup |
| `src/components/dashboard/DashboardCrewPanel.tsx` | Subscription without cleanup |

**Fix:** Add `useEffect` cleanup function that removes global refs / unsubscribes.

### R-26 — Re-entrancy Races
| File | Issue |
|---|---|
| `src/hooks/useAppMicrophone.ts` | Concurrent mic access without `isCapturing` guard |
| `src/hooks/useGlobalTelemetry.ts` | Race on flush + new write |

**Fix:** Add `useRef` guard flag to block re-entrant calls until previous operation completes.

### R-27 — Context Consumer Depth
| File | Issue |
|---|---|
| `src/screens/DashboardScreen.tsx` | Consuming 5+ contexts — contributes to monolith |
| `src/components/DockedController.tsx` | Consuming 4+ contexts |

**Fix:** Evaluate creating a `useDashboardComposite` hook that aggregates the required context slices.

### R-17 — Event Listener Leak
| File | Line | Issue |
|---|---|---|
| `src/hooks/useDashboardCrew.ts:70` | 70 | `crewService.subscribeAsMember` with no `useEffect` cleanup |

**Fix:** Capture the channel/subscription return value and call unsubscribe in the cleanup function.

### R-12 — Stale Closure
| File | Line | Issue |
|---|---|---|
| `src/components/CustomEffectVisualizer.tsx:52` | 52 | `setInterval` without `useRef` to hold stable callback |

**Fix:** Use `useCallback` + `useRef` pattern for the interval callback.

### Implementation Steps
1. Fix `AndroidManifest.xml` BLUETOOTH_SCAN permission (highest impact, 1 line)
2. Fix PII leaks in `ConnectService` and `DeviceRepository`
3. Fix memory leaks in `useDeviceStateLedger` and `DashboardCrewPanel`
4. Fix R-17 listener leak in `useDashboardCrew`
5. Fix R-12 stale closure in `CustomEffectVisualizer`
6. Fix R-26 re-entrancy guards
7. Fix R-20 Platform.select() in JS files
8. Investigate R-21 duplication

### Verify
- `npm run verify`
- Manual: install on Android 12+ device, verify BLE scan does NOT prompt for Location permission
