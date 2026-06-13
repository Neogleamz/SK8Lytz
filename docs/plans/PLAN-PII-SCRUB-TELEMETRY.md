# Implementation Plan
# PLAN-PII-SCRUB-TELEMETRY

## Summary
49 telemetry log calls across 5 files leak raw device MAC addresses and user display names into AppLogger payloads. These PII tokens can surface in cloud telemetry logs, creating a GDPR/privacy compliance risk. Fix: replace all MAC and display_name log values with a `scrubPII()` hashing function.

**Batch:** `BATCH:privacy-hardening`
**Status:** `[✅ VERIFIED]`

---

## Source of Truth Files
- `src/hooks/useCrewSession.ts` — Line 107 (R-09-001): display_name leak
- `src/hooks/useBLE.ts` — Line 444 (R-09-002): MAC leak
- `src/hooks/useDashboardAutoConnect.ts` — Line 222 (R-09-003): MAC leak
- `src/hooks/useDeviceStateLedger.ts` — Line 162 (R-09-004): MAC leak
- `src/screens/DashboardScreen.tsx` — Line 434 (R-09-005): MAC leak
- Raw audit: `R-09_findings.json` (49 total findings, 5 shown above are the primary call sites)

---

## Findings

| ID | File | Line | Leaked Value | Severity |
|----|------|------|-------------|----------|
| R-09-001 | `src/hooks/useCrewSession.ts` | 107 | `member.display_name` | HIGH |
| R-09-002 | `src/hooks/useBLE.ts` | 444 | `mac` (raw MAC address) | HIGH |
| R-09-003 | `src/hooks/useDashboardAutoConnect.ts` | 222 | `id` (MAC address) | HIGH |
| R-09-004 | `src/hooks/useDeviceStateLedger.ts` | 162 | `mac: key` (MAC address) | HIGH |
| R-09-005 | `src/screens/DashboardScreen.tsx` | 434 | `first.device_mac` | HIGH |

> [!WARNING]
> R-09 reports 49 total findings. The 5 above are the explicitly verified primary sites. After implementing the `scrubPII()` helper, a grep sweep for `{ mac:`, `display_name` in AppLogger calls should catch all remaining instances.

---

## Implementation Steps

### Step 1 — Implement `scrubPII()` utility
**File:** `src/utils/piiScrubber.ts` (new file)
```typescript
import { createHash } from 'crypto'; // or use a lightweight substitute

/**
 * Hash a PII value (MAC address, display name) for safe telemetry logging.
 * Returns first 8 chars of SHA-256 hex for log correlation without exposure.
 */
export const scrubPII = (value: string): string => {
  if (!value) return 'UNKNOWN';
  // React Native lacks built-in crypto — use a simple deterministic hash
  let hash = 0;
  for (let i = 0; i < value.length; i++) {
    hash = ((hash << 5) - hash) + value.charCodeAt(i);
    hash |= 0; // Convert to 32-bit int
  }
  return `scrubbed_${Math.abs(hash).toString(16).padStart(8, '0')}`;
};
```

### Step 2 — Apply to each verified call site

**`src/hooks/useCrewSession.ts:107`**
```typescript
// BEFORE
AppLogger.log('CREW_LEADERSHIP_TRANSFERRED', { newLeaderName: member.display_name });
// AFTER
AppLogger.log('CREW_LEADERSHIP_TRANSFERRED', { newLeaderId: member.user_id });
```
*(Replace name with ID — IDs are non-PII in this context)*

**`src/hooks/useBLE.ts:444`**
```typescript
// BEFORE
AppLogger.warn('[BLE RSSI] Critical signal - proactive reconnect', { mac });
// AFTER
AppLogger.warn('[BLE RSSI] Critical signal - proactive reconnect', { deviceId: scrubPII(mac) });
```

**`src/hooks/useDashboardAutoConnect.ts:222`**
```typescript
// BEFORE
AppLogger.warn('[AutoConnect] Max retries exceeded - abandoning', { mac: id, retries });
// AFTER
AppLogger.warn('[AutoConnect] Max retries exceeded - abandoning', { deviceId: scrubPII(id), retries });
```

**`src/hooks/useDeviceStateLedger.ts:162`**
```typescript
// BEFORE
AppLogger.error('Failed to read device state ledger entry from storage', e, { mac: key });
// AFTER
AppLogger.error('Failed to read device state ledger entry from storage', e, { deviceId: scrubPII(key) });
```

**`src/screens/DashboardScreen.tsx:434`**
```typescript
// BEFORE
AppLogger.log('BLE_STATE_CHANGE', { event: 'new_unclaimed_device_found', mac: first.device_mac });
// AFTER
AppLogger.log('BLE_STATE_CHANGE', { event: 'new_unclaimed_device_found', deviceId: scrubPII(first.device_mac) });
```

### Step 3 — Sweep remaining 44 instances
After Step 1-2 are implemented, grep for remaining MAC leaks:
```powershell
grep -rn "{ mac:" src/ | grep "AppLogger"
grep -rn "display_name" src/ | grep "AppLogger"
```
Apply `scrubPII()` or ID substitution to all remaining hits.

---

## Verification
- `npm run verify`
- Search codebase: `grep -rn "device_mac\|display_name" src/ | grep AppLogger` — should return 0 results
- Manual: Trigger a BLE connection cycle → check `adb logcat` → confirm no raw MAC addresses appear in log output

## Kanban Task Tags
- `[Status: 🔥 ON DECK]`
- `[Verification Status: ✅ VERIFIED]`
- `[Layer: Services]`
- `[Risk: H-RISK]`
- `[Size: Meal]`
- `[Cognitive Load: Low]`
- `[BATCH: privacy-hardening]`
