# Implementation Plan: fix/ui-misc-safety

## Goal
Fix type safety, hardcoded delays, and fire-and-forget BLE streaming violations across miscellaneous UI components.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Clusters UI_MISC + R-02

## Findings to Resolve
1. R-02: Oracle53LiveStream.tsx — Fire-and-forget 0x53 pixel frame streaming via setInterval
2. R-08: useWebDemoConsoleBridge.ts L25 — `any[]` type annotation
3. R-16: DeviceSettingsModal.tsx L233, LocationPicker.tsx L98, PositionalGradientBuilder.tsx L47, VerticalPatternDrum.tsx L66 — Hardcoded timeouts
4. R-16: DiagnosticLabOracleTab.tsx L50, Oracle53LiveStream.tsx L35 — Hardcoded setInterval
5. R-16: useOfflineSyncWorker.ts L51 — Hardcoded setInterval

## Files to Create/Modify

### [MODIFY] [DeviceSettingsModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DeviceSettingsModal.tsx)
### [MODIFY] [LocationPicker.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/LocationPicker.tsx)
### [MODIFY] [PositionalGradientBuilder.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/PositionalGradientBuilder.tsx)
### [MODIFY] [VerticalPatternDrum.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/VerticalPatternDrum.tsx)
### [MODIFY] [useWebDemoConsoleBridge.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/dev/useWebDemoConsoleBridge.ts)
### [MODIFY] [useOfflineSyncWorker.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/cloud/useOfflineSyncWorker.ts)
### [MODIFY] [DiagnosticLabOracleTab.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/tabs/DiagnosticLabOracleTab.tsx)
### [MODIFY] [Oracle53LiveStream.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/tabs/oracle/Oracle53LiveStream.tsx)

## Verification
- `npm run verify`

## Out of Scope
- All other components
