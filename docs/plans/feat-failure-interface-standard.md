# [PLAN] feat/failure-interface-standard (Standardized Failure)

### Design Decisions & Rationale
To ensure a consistent user experience during errors, we are implementing the **AppFailure Registry**. This system maps every potential code failure to a specific `SeverityCode`, ensuring that the user always gets the right level of feedback (Silent, Toast, or Modal) and that we never "swallow" an error without telemetry.

## Proposed Changes

### [Component Name] Error Orchestration

#### [NEW] [services/ErrorHandler.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ErrorHandler.ts)
- Implement `processFailure(error: SK8Error)`:
    - **SILENT**: Log to `AppLogger` breadcrumbs only.
    - **WARN**: Trigger a haptic feedback and a `Toast` message.
    - **BLOCKING**: Trigger the `GlobalErrorModal` on the Dashboard.

#### [NEW] [types/ErrorTypes.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/types/ErrorTypes.ts)
- Define `SK8Error` interface: `{ code, severity, humanMsg, technicalDetails, action? }`.

## Verification Plan
1. **Toast Test**: Trigger a low-severity "GATT Timeout"; verify a Toast appears without blocking the UI.
2. **Modal Test**: Inject a "Authentication Failure"; verify the blocking Modal appears with a "Retry Login" button.
3. **Telemetry Parity**: Verify that every failure (regardless of severity) generates a corresponding JSON entry in the `telemetry_errors` table.
