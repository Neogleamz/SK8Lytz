# Goal Description

Integrate the Diagnostic Lab with the `led_diagnostics` Postgres telemetry table. To fulfill the requirement of keeping logs globally stored and regularly synced—while maintaining extreme efficiency to prevent database bloat—we will establish a dedicated "Telemetry Fast-Track" specifically for `RAW_PAYLOAD` Diagnostic TX events. RX packet spam (ping/pongs) will not be uploaded, ensuring our database remains crisp and focused on actionable testing metrics.

## Design Decisions & Rationale

- **Anti-Bloat Strategy (TX Only)**: Hardware pushes `RX` data (like low-level hardware state) every 1 second per connected skate. Capturing this constantly would explode Postgres tables in production. We will explicitly route _only_ manual `TX` (Diagnostic Testing) hardware pushes into `AppLogger`.
- **Dedicated Partition (`led_diagnostics`)**: To prevent `parsed_logs` from getting bloated with lab noise (Hex Bytes, builder annotations), `RAW_PAYLOAD` events will continue to be explicitly ignored in the main metric pipeline, but completely re-routed into `led_diagnostics` safely.
- **Local Caching / Auto Sync**: By passing this through `AppLogger.log`, the local device automatically buffers the testing events in `AsyncStorage` and flushes them as part of our normal `<AppLifecycle>` tracking exactly when they are confirmed inserted, ensuring no data loss if the device drops connection.

## Proposed Changes

### `src/components/Sk8LytzDiagnosticLab.tsx`

#### [MODIFY] Sk8LytzDiagnosticLab.tsx

- In the `transmit()` callback, instantly push a `RAW_PAYLOAD` log to `AppLogger` whenever the user executes a diagnostic command.
- Capture payload specific metadata (`deviceId`, `hexStr`, `note`).
- **Safety check**: Keep the local UI `setLogs` 200-item array untouched so the Sniffer UI remains real-time.

---

### `src/services/AppLogger.ts`

#### [MODIFY] AppLogger.ts

- In `uploadLogsToSupabase()`, locate where `RAW_PAYLOAD` events are pulled from `this.buffer`.
- Filter the buffer for `RAW_PAYLOAD` events. Group them into an `insert` block targeting the `led_diagnostics` table.
- Transform the raw hex payload into the `led_diagnostics` columns: `protocol` (the 0x prefix byte), `payload_hex`, `test_label` (the user note), and `device_id`.
- Ensure it runs successfully before clearing the buffer rotation.
- Update the documentation comment at the top of the file to reflect that `led_diagnostics` is now actively syncing.

## Verification Plan

### Automated Tests

- N/A

### Manual Verification

1. I will boot the Dev Server.
2. I will send a 0x59 RED COLOR TEST payload via the Diagnostic Lab.
3. I will trigger a background AppLogger sync to Supabase.
4. I will query the `led_diagnostics` table via MCP SQL tools to objectively prove the payload arrived unharmed.
