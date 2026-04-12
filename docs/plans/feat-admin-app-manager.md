# feat/admin-app-manager (Governance & Control)

### Design Decisions & Rationale
We are building a **Master Governance Engine**. The app will no longer be a collection of "dumb" screens; it will be a context-aware fleet. By centralizing these controls, you (the admin) can react to production issues or legal requirements in real-time.

## User Review Required
> [!IMPORTANT]
> **Consolidated Scope**: Per user request, secondary ops (Firmware, Labs, Maintenance) have been removed. The focus is now solely on Feature Governance and Legal Integrity.
> [!NOTE]
> Maps and Future Skate Maps are explicitly included in this governance engine.

## Proposed Changes

### [Component Name] Feature Governance Matrix

#### [MODIFY] [AppSettingsService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppSettingsService.ts)
- Update to support a `PolicyRecord` structure (ID, Name, CurrentStrategy).
- Categories: `MODULE` (Crew Hub, Community, Maps) and `SYSTEM` (EULA, Telemetry).

#### [MODIFY] [AdminToolsModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AdminToolsModal.tsx)
- Redesign the "App Settings" view into the **App Manager Dashboard**.
- Features included:
  - **Crew Hub** (Global Lock / Hide Offline)
  - **Community Favorites** (Global Lock / Gated Offline)
  - **SK8Lytz Picks** (Global Lock)
  - **Skate Maps & Places** (Global Lock / Hide Offline) 
  - **Telemetry Uploads** (Admin Toggle)
  - **Required EULA Version** (Global enforcement lever)
- Implement **Safety Confirmation Modal** for all policy changes.

## Open Questions
- None. Confirmation received on UI residency, safety locks, and removal of secondary ops.

## Verification Plan
1. **Safety Test**: Attempt to toggle a flag and verify the "Are you sure?" modal blocks the write until confirmed.
2. **Registry Test**: Verify that adding a new feature ID to the service automatically makes it appear in the App Manager UI.
3. **Map Policy Test**: Set the Skate Map to `HIDDEN_OFFLINE`, go offline, and verify the map button vanishes from the Hub.
