# PLAN-pii-logger-scrubber

**Status:** READY
**Generated:** 2026-06-10
**Source Report:** [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) — Cluster A (PII Leakage)
**Supersedes / Incorporates:** PLAN-PII-SCRUB-TELEMETRY.md, PLAN-pii-scrubber-hardening.md

---

## Goal

Add `mac`, `deviceId`, `deviceMac`, `peripheral_id`, and `address` to `AppLogger.PII_KEY_PATTERNS` so raw MAC addresses are scrubbed before reaching Supabase telemetry. Wrap the VIP Fast-Lane `telemetry_errors` insert in `.catch()` to prevent an unhandled promise rejection on Supabase write failure. Add a Jest test asserting MAC keys are redacted.

## Industry Standard

OWASP MASVS L1: No device identifiers or MAC addresses in logs. PII scrubbing must happen at the logger layer, not at call sites. Centralizing the fix in `PII_KEY_PATTERNS` eliminates 47+ call sites as a risk surface.

## Source of Truth

- **Primary:** `artifacts/system_audit_report.md` — Cluster A, Findings R09-001 through R09-005
- **Secondary:** `src/services/AppLogger.ts:398-402` (PII_KEY_PATTERNS array — verified live)

```
# Cited Truth (P1)
# AppLogger.ts:398-401 — current PII_KEY_PATTERNS (VERIFIED 2026-06-10):
const PII_KEY_PATTERNS = [
  'email', 'name', 'password', 'token', 'phone', 'address', 'fullname',
  'lat', 'lng', 'latitude', 'longitude', 'label',
  'auth', 'refresh', 'access', 'secret', 'credential',
];
# Missing: 'mac', 'deviceId', 'deviceMac', 'peripheral_id'
```

---

## Implementation Steps

### Step 1: Expand PII_KEY_PATTERNS

- **File:** `src/services/AppLogger.ts`
- **Action:** Locate `PII_KEY_PATTERNS` at line 398. Add five entries:
  `'mac'`, `'deviceId'` (catches `deviceId`, `deviceMac` via substring match), `'peripheral_id'`, and `'address'` (already present — confirm no duplication).
- **Exact target (view before edit):** Lines 398–402
- **After edit the array should read:**
  ```typescript
  const PII_KEY_PATTERNS = [
    'email', 'name', 'password', 'token', 'phone', 'address', 'fullname',
    'lat', 'lng', 'latitude', 'longitude', 'label',
    'auth', 'refresh', 'access', 'secret', 'credential',
    'mac', 'deviceid', 'peripheral_id',
  ];
  ```
  > Note: `'deviceid'` (lowercase) matches `deviceId` and `deviceMac` via `.toLowerCase().includes()` — the existing substring match logic on line 409 handles this.
- **Source:** `src/services/AppLogger.ts:398-402` (cited above)
- **Verify:** `grep -r "mac.*logger\|logger.*mac" src/` returns zero unmasked hits. TSC compiles clean.

### Step 2: Wrap VIP Fast-Lane insert in .catch()

- **File:** `src/services/AppLogger.ts`
- **Action:** Find the `supabase.from('telemetry_errors').insert(...)` block at line 460. It currently ends with `.then(({ error }) => { if (error) console.warn(...) })`. This `.then()` has no `.catch()`, meaning a network error or thrown exception produces an unhandled promise rejection.
- **Change:** After the `.then(...)` call on the `telemetry_errors` insert, chain `.catch((e: unknown) => console.warn('[AppLogger] VIP insert failed (network):', e instanceof Error ? e.message : String(e)))`.
- **Source:** `src/services/AppLogger.ts:460-473` (VIP Fast-Lane block, verified live)
- **Verify:** TypeScript compiles clean. The catch handler is typed as `(e: unknown)` — no `any` cast.

### Step 3: Add MAC scrub Jest test

- **File:** `src/services/__tests__/AppLogger.test.ts` (create if not present)
- **Action:** Add a test that passes a payload containing `{ mac: 'AA:BB:CC:DD:EE:FF', deviceId: 'abc123', message: 'test' }` to `AppLogger.log` and asserts the structured log entry stored in the buffer contains `[REDACTED]` for both `mac` and `deviceId` fields.
- **Verify:** `jest --testPathPattern AppLogger` passes with the new test.

---

## Out of Scope

- Do NOT change the VIP Fast-Lane logic itself — only wrap the existing `.then()` with `.catch()`.
- Do NOT add new PII patterns beyond the 3 added above (`mac`, `deviceid`, `peripheral_id`).
- Do NOT modify any call sites in BLE services — fix only the central logger gate.
- Do NOT touch `PLAN-PII-SCRUB-TELEMETRY.md` or `PLAN-pii-scrubber-hardening.md` — they are superseded and should be referenced only.

---

## Risk Assessment

- **Risk Level:** M-RISK
- **Size:** Meal
- **Layer:** SERVICE
- **Blast Radius:** `src/services/AppLogger.ts` only (additive change to array). One new test file.
- **Rollback:** `git checkout -- src/services/AppLogger.ts`

---

## Verification Checklist

1. `npm run verify` → TSC ✅ Jest ✅ all gates green ✅
2. `grep -rn "'mac'" src/services/AppLogger.ts` → confirms new entry in `PII_KEY_PATTERNS`
3. New Jest test asserts `{ mac: 'AA:BB:CC:DD:EE:FF' }` → `{ mac: '[REDACTED]' }` ✅
4. `grep -r "supabase.from('telemetry_errors')" src/services/AppLogger.ts` → confirm `.catch()` present ✅
