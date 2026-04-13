# [PLAN] refactor/ble-notification-mailroom

## Design Decisions & Rationale

The BLE notification handler is currently a monolithic callback that does too many things at once: raw logging, Supabase upload, payload parsing, and state writes are all inlined in a single "God Callback." This creates tight coupling that makes it impossible to test, debug, or extend individual concerns in isolation. The goal is to decompose it into four distinct, single-responsibility handlers that the notification hook orchestrates linearly — a "mailroom" pattern where each clerk has exactly one job.

**Key question before execution:** Should the diagnostics upload to Supabase be fire-and-forget (current assumption) or should parse failures gate the upload? This needs to be settled in the discussion phase.

---

## Scope

**Target file(s):** `src/hooks/useBLE.ts` (primary), possibly `src/services/DiagnosticsService.ts`

**Problem Statement:**

When a BLE characteristic notification arrives, a single inline callback currently handles all four of these responsibilities:

1. 📬 **Raw Stamp** — stores the hex bytes in `lastRawNotification` (for the Protocol Sniffer UI)
2. 📤 **Diagnostics Upload** — fire-and-forget to Supabase device diagnostics table
3. 🔍 **LED Config Parser** — interprets payload to extract `points`, `sortOrder`, `icType`
4. 🗂️ **State Writer** — commits parsed config into `allDevices`, `deviceConfigs`, and `AsyncStorage`

The question is whether these 4 intercepts all need to live inline, or whether they can be decomposed into isolated, testable units.

---

## Discussion Topics (Requires Human Input)

Before writing any code, the following architectural questions must be resolved:

| # | Question | Options |
|---|----------|---------|
| 1 | Should the raw stamp happen **before** or **after** error validation? | Before (always log), After (only log valid packets) |
| 2 | Should Supabase upload be **blocked by** parse failure? | Fire-and-forget regardless, or gate on parse success |
| 3 | Should the parser be **extracted** into a pure utility function? | Inline in hook vs. `src/utils/BlePayloadParser.ts` |
| 4 | Should state writes use **React state, Zustand, or just AsyncStorage**? | Clarify the canonical state authority for device configs |
| 5 | Is there a risk of **notification flooding** (too many events)? | Consider debounce/throttle at the mailroom entry point |

---

## Proposed Architecture (Pending Discussion)

```
onBLENotification(rawBytes)
  │
  ├─ 1. stampRawNotification(rawBytes)          // Pure, sync — no side effects, just state
  │
  ├─ 2. uploadDiagnosticsTelemetry(rawBytes)    // Fire-and-forget async — does NOT block
  │
  ├─ 3. ledConfig = parseLedPayload(rawBytes)   // Pure utility fn — returns typed result or null
  │
  └─ 4. if (ledConfig) writeLedConfigToState(ledConfig)  // Async — allDevices + AsyncStorage
```

---

## Proposed File Changes

### [MODIFY] `src/hooks/useBLE.ts`
- Extract the notification callback body into 4 named sub-functions
- Orchestrate them sequentially in the main `onCharacteristicValueChanged` handler

### [NEW] `src/utils/BlePayloadParser.ts`
- Pure, stateless function: `parseLedPayload(rawBytes: Uint8Array): LedConfig | null`
- Fully unit-testable with no BLE or React dependencies

### [MODIFY] `src/services/DiagnosticsService.ts` (if it exists)
- Ensure the upload function has a proper try/catch and never throws — callers should not need to guard it

---

## Verification Plan

### Automated Tests
- `npx jest BlePayloadParser.test.ts` — verify pure parser with known hex fixtures
- `npx tsc --noEmit` — confirm no TypeScript regressions

### Manual Verification
- Protocol Sniffer UI: confirm `lastRawNotification` updates immediately on receipt
- Supabase dashboard: verify telemetry rows are created after a successful connect
- LED config: confirm `allDevices` and `deviceConfigs` update correctly after reconnect
- AsyncStorage: verify device config persists across app restart

---

*Status: NEEDS DISCUSSION — Do not execute until architectural questions are resolved.*
