# Implementation Plan

## PLAN-hal-enclosure-sweep — HAL Enclosure + BLE Protocol Safety
*Source: `/deepdive-code-hunt` fleet | Rules: R-19, R-10, R-04 | Date: 2026-06-10*

### Problem
Raw BLE byte arrays are being constructed outside `src/protocols/`, MAC addresses are leaking into telemetry as raw strings bypassing the PII scrubber, and group BLE writes iterate sequentially over targets instead of dispatching concurrently.

### Rules Violated
- **R-19 (HAL Enclosure):** Protocol-specific byte construction must live inside `src/protocols/` adapters only.
- **R-10 (Group Connectivity):** Sequential `await` loops over multiple BLE targets. Use `Promise.all`.
- **R-04 (Telemetry Logging):** MAC addresses interpolated directly into log message strings, bypassing `AppLogger.formatPayload` scrubber.

### Source of Truth
- `tools/ZENGGE_PROTOCOL_BIBLE.md` — `0x59`, `0x40`, `0x71`, `0x63` opcode specs
- `tools/SK8Lytz_App_Master_Reference.md` §BLE Core
- `artifacts/system_audit_report.md` — CLUSTER-07

### Affected Files
| File | Violation | Severity |
|---|---|---|
| `src/services/BleWriteDispatcher.ts:41` | Raw `0x59` bytes in dispatcher (CONFIRMED) | HIGH |
| `src/services/BleWriteDispatcher.ts:261` | Raw `0x40` chunking framing in dispatcher (CONFIRMED) | HIGH |
| `src/services/BleWriteDispatcher.ts:193,290,388` | Sequential `await` loops over group targets | HIGH |
| `src/components/patterns/UnifiedPatternPicker.tsx:46-48` | Raw byte array outside protocol (CONFIRMED) | HIGH |
| `src/components/CustomEffectVisualizer.tsx:43` | Raw byte array outside protocol | HIGH |
| `src/services/ble/ConnectService.ts:178-179` | MAC via template literal (CONFIRMED) | MEDIUM |
| `src/services/ble/ConnectService.ts:275-277` | MAC via template literal (CONFIRMED) | MEDIUM |
| `src/services/DeviceRepository.ts:793` | MAC via string concatenation | HIGH |
| `src/hooks/useBLE.ts:293,304-305` | Missing `payload_size`/`ssi` in error logs | MEDIUM |
| `src/hooks/useOptimisticBLE.ts:98` | `payloadLen` instead of `payload_size` in log | HIGH |
| `src/utils/CrashReporter.ts:2` | `console.error` bypasses `AppLogger` | MEDIUM |

### Implementation Steps
1. Move `0x59` buffer lockout padding logic into `ZenggeProtocol` adapter
2. Move `0x40` chunking framing into `ZenggeProtocol.prepareForTransmission()`
3. Replace sequential `await` loops in `BleWriteDispatcher` with `Promise.all` mapped writes
4. Fix MAC interpolation in `ConnectService` — use `scrubPII()` or pass as context object key
5. Fix MAC concatenation in `DeviceRepository:793`
6. Fix missing log context fields in `useBLE.ts` errors
7. Replace `console.error` in `CrashReporter.ts` with `AppLogger.error`
8. Move raw byte arrays in `UnifiedPatternPicker` and `CustomEffectVisualizer` to protocol builders

### Verify
- `npm run verify` (TSC + Jest green)
- BLE-lab smoke test: send `0x59` static color payload, verify byte math unchanged
- Grep check: `grep -r "0x59\|0x40\|0x71" src/components/ src/services/BleWriteDispatcher.ts` → 0 hits
