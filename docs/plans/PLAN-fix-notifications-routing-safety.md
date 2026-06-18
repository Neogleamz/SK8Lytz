# Implementation Plan: fix/notifications-routing-safety

## Goal
Fix floating promises, missing error handling, and telemetry context gaps in notification/routing services.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Cluster NOTIFICATIONS_&_ROUTING

## Findings to Resolve
1. R-11: LocationService.ts L123 — Unprotected Supabase query
2. R-11: NotificationService.ts L68,70 — Unprotected channel setup and permission request
3. R-11: BluetoothGuard.tsx L47 — try/finally without catch
4. R-11: BluetoothGuard.tsx L57 — Linking.openSettings() unawaited
5. R-04: NotificationService.ts L90,146,196,221 — Missing payload_size/ssi context
6. R-04: LocationService.ts L71,178 — Missing telemetry context
7. R-04: BluetoothGuard.tsx L24 — Missing telemetry context
8. R-04: App.tsx L59,172 — Missing telemetry context in console.error monkey-patch and HEALTH_CONNECT

## Files to Create/Modify

### [MODIFY] [NotificationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/NotificationService.ts)
### [MODIFY] [LocationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/LocationService.ts)
### [MODIFY] [BluetoothGuard.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/providers/BluetoothGuard.tsx)
### [MODIFY] [App.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/App.tsx)

## Verification
- `npm run verify`

## Out of Scope
- All other providers and services
