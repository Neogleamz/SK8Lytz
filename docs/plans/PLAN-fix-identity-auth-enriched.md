# Implementation Plan: fix/identity-auth-enriched

> **Source:** Deep-Dive Code Synthesis v2 — Enriched IDENTITY Domain Audit (2026-06-18)
> **Batch:** `[BATCH:deepdive-audit-mega-sweep]`
> **Wave:** `[WAVE:1]` (no file collisions with other Wave 1 tasks)
> **Risk:** `[L-RISK]` (identity UI layer, no BLE)
> **Size:** `[Snack]` (4 files, 4 surgical edits)

---

## Files to Create/Modify

### [MODIFY] [account.types.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/account/account.types.ts)
- **Line 37:** Tighten `styles: Record<string, object>` → `styles: Record<string, import('react-native').ViewStyle | import('react-native').TextStyle | import('react-native').ImageStyle>`
- **Rule:** R-08 | **Severity:** LOW

### [MODIFY] [AuthFormForgotPassword.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/auth/AuthFormForgotPassword.tsx)
- **Line 51:** Remove redundant nested ternary
- **Before:** `const msg = e instanceof Error ? e.message : (e instanceof Error ? e.message : String(e));`
- **After:** `const msg = e instanceof Error ? e.message : String(e);`
- **Rule:** R-21 | **Severity:** LOW

### [MODIFY] [useDashboardProfile.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardProfile.ts)
- **Lines 149-166:** Add `isRefreshingRef` re-entrancy guard to `refreshProfile()` async function
- **Before:** Direct async execution with no guard
- **After:** `if (isRefreshingRef.current) return; isRefreshingRef.current = true;` with `finally { isRefreshingRef.current = false; }`
- **Rule:** R-26 | **Severity:** MEDIUM

### [MODIFY] [DevSandboxDrawer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/auth/DevSandboxDrawer.tsx)
- **Lines 41-44:** Replace dead-code if-block with proper `__DEV__` guard
- **Before:** `if (!__DEV__ && typeof window === 'undefined') { /* comment */ }`
- **After:** `if (!__DEV__) return null;`
- **Rule:** R-14 | **Severity:** MEDIUM | **⚠️ PRODUCTION EXPOSURE**

---

## Verification Plan

### Automated Tests
```powershell
npm run verify
```

### Manual Verification
- Confirm DevSandboxDrawer is invisible in release builds
- Confirm refreshProfile does not fire concurrent API calls when AppState changes rapidly

---

## Out of Scope
- AuthContext.tsx (offline-first flow is correct per audit)
- SkaterStatsPanel.tsx (R-04 telemetry context — already captured in R-04 mega-sweep)
- AccountTabCrewz.tsx (R-14 state matrix — implicit success state is acceptable)
