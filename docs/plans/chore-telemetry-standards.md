# [PLAN] chore/telemetry-standards (The Black Box Standard)

### Design Decisions & Rationale

We are standardizing the `AppLogger` service to meet industry "First Responder" levels. This ensures that every event is structured, contextualized by hardware data, and respects user privacy while maintaining a lean local storage footprint.

## Proposed Changes

### [Component Name] AppLogger Service

#### [MODIFY] [AppLogger.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppLogger.ts)

- **Standard 1: Structured JSON Parsing**: Enforce JSON schema for all log inputs.
- **Standard 2: Hardware Injection**: Automatically inject `rssi`, `mtu`, and `battery` into metadata for all BLE-context logs.
- **Standard 3: PII Scrubbing**: Implement an `obfuscate()` helper to mask emails/names.
- **Standard 4: Log Levels**: Implementation of `debug()`, `info()`, `warn()`, `error()` with conditional filtering.
- **Standard 5: Circular FIFO Buffer**: Limit `AsyncStorage` to 500 entries with automatic pruning.

## Verification Plan

1. **Schema Check**: Trigger a log and verify the record in `AsyncStorage` matches the required JSON structure.
2. **PII Masking**: Log a message containing a dummy email; verify the email is masked in the output.
3. **Overflow Test**: Force 600 logs in a loop; verify `@sk8lytz_logs` contains exactly 500 items and the oldest were pruned.
