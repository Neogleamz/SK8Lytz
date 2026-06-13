# [PLAN] feat/sentinel-telemetry (The Sentinel Standard)

### Design Decisions & Rationale

We are moving from passive error logging to **Proactive Observability**. Every crash or GATT exception will be accompanied by a "Breadcrumb" trail (the last 10 actions) and a "Fingerprint" (Device State, MTU, BT Adapter status). This reduces debug time from hours to minutes and provides a "Black Box" recorder for hardware failures.

## Proposed Changes

### [Component Name] Telemetry Engine

#### [MODIFY] [AppLogger.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppLogger.ts)

- Implement a `breadcrumbBuffer`: A circular queue (size 10) storing `{ timestamp, action, component }`.
- Update `logError`: Automatically attach the `breadcrumbBuffer` to the payload sent to Supabase.
- Include Hardware Fingerprint: `adapterState`, `mtuSize`, and `activeConnectionsCount`.

#### [NEW] [TelemetryBoundary.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/TelemetryBoundary.tsx)

- Use a React Error Boundary to catch UI-level crashes.
- Dispatches a "Last Breath" telemetry event to Supabase before displaying the Error state to the user.

### [Component Name] Database Sync

#### [MODIFY] [Supabase Migration]

```sql
ALTER TABLE telemetry_errors
ADD COLUMN IF NOT EXISTS breadcrumbs JSONB,
ADD COLUMN IF NOT EXISTS hw_fingerprint JSONB;
```

## Verification Plan

1. **Breadcrumb Test**: Perform a series of actions (Open Modal -> Set Color -> Close), then trigger a manual error. Verify the Supabase row contains the correct action trail.
2. **Fingerprint Test**: Verify that the MTU and adapter state are correctly captured.
3. **Recovery Test**: Ensure the UI-level Error Boundary displays a "Restart App" button that clears the state.
