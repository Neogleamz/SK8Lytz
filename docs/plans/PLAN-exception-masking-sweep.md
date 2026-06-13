# Implementation Plan

## PLAN-exception-masking-sweep — Exception Masking / Empty Catch Blocks
*Source: `/deepdive-code-hunt` fleet | Rules: R-06 | Date: 2026-06-10*

### Problem
17 catch blocks either swallow errors silently or use `String(e)` directly without the proper `e instanceof Error ? e.message : String(e)` unwrapping pattern. Protocol files (`ZenggeProtocol.ts`, `BanlanxAdapter.ts`) are silently swallowing errors that should be logged via AppLogger.

### Fix Pattern
```typescript
// Before
} catch (e) { }  // silent

// After
} catch (e: unknown) {
  AppLogger.warn('CONTEXT_NAME', 'operation failed', {
    error: e instanceof Error ? e.message : String(e)
  });
}
```

### Source of Truth
- `artifacts/system_audit_report.md` — CLUSTER-04

### Affected Files
| File | Line | Severity | Action |
|---|---|---|---|
| `src/protocols/ZenggeProtocol.ts` | 18, 192, 393 | LOW | Add proper unwrapping + AppLogger.warn |
| `src/protocols/BanlanxAdapter.ts` | 95 | LOW | Add unwrapping, log at debug/trace level |
| `src/protocols/ControllerRegistry.ts` | 21 | LOW | Add unwrapping or explicit ignore comment |
| `src/context/SessionContext.tsx` | 366, 399 | LOW | Fix `error: String(err)` → `error: err instanceof Error ? err.message : String(err)` |
| `src/services/SpeedTrackingService.ts` | 307, 343 | MEDIUM | Fix `safeErr` unused + catch param |
| `src/components/DockedController.tsx` | 438, 470 | LOW | Add proper unwrapping |
| `src/hooks/useRecentSpots.ts` | 25 | LOW | Add catch (e: unknown) + AppLogger.warn |
| `src/services/ScenesService.ts` | 66 | LOW | Add error unwrapping and logging |
| `App.tsx` | 145 | LOW | Add proper unwrapping |
| `src/services/LocationService.ts` | 92 | LOW | Log at trace level |
| `src/hooks/useAppMicrophone.ts` | 109 | LOW | Add proper error logging |
| `src/components/SkateSpotBottomSheet.tsx` | 43 | MEDIUM | Add e instanceof Error unwrapping |

### Implementation Steps
1. Fix protocol files first (`ZenggeProtocol.ts`, `BanlanxAdapter.ts`, `ControllerRegistry.ts`)
2. Fix `SessionContext.tsx` error payloads
3. Fix remaining service and component files
4. Ensure no `String(e)` remains in any catch block — replace with the canonical pattern

### Verify
- `npm run verify`
- Grep: `grep -n "catch (e)" src/protocols/` → zero bare untyped catches remaining
