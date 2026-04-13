# Admin Tools Reorganization & Renaming

Rename and reorganize the `LogViewerModal` to become the primary `AdminToolsModal` hub, providing a unified entry point for telemetry, statistics, device management, and hardware diagnostic tools.

## Design Decisions & Rationale

We are unifying all administrative and diagnostic features into a single, high-fidelity hub. This reduces UI clutter in the main Dashboard and establishes a clear separation between end-user features and low-level hardware tools.

## User Review Required

> [!IMPORTANT]
> The logo-tap entry point (10 taps + '0000' passcode) will now launch the **Admin Tools** hub by default, rather than jumping directly to the Diagnostic Lab. The Lab and Programmer will be accessible from the "Tools" tab within this hub.

## Proposed Changes

### [Admin Tools Component]

#### [MODIFY] [AdminToolsModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AdminToolsModal.tsx) (Renamed from LogViewerModal.tsx)

- Rename component and export to `AdminToolsModal`.
- Update `Tab` type to `'timeline' | 'stats' | 'device' | 'tools'`.
- Reorder the tab rendering to match the requested hierarchy: **Timeline, Stats, Device, Tools**.
- Update UI labels and icons (e.g., "Devices" → "Device", "Admin" → "Tools").
- Ensure the "Tools" tab houses the `Sk8Lytz Programmer` and `LED Diagnostic Lab` launch buttons.

### [Dashboard Integration]

#### [MODIFY] [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)

- Update import from `LogViewerModal` to `AdminToolsModal`.
- Rename state `isSnifferVisible` (if applicable) or add `isAdminToolsVisible` to control the modal.
- Update the logo-passcode logic: `0000` should successfully trigger `setIsAdminToolsVisible(true)`.
- Render `<AdminToolsModal>` in the main JSX tree with appropriate props.

### [Diagnostic Lab Integration]

#### [MODIFY] [Sk8LytzDiagnosticLab.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/Sk8LytzDiagnosticLab.tsx)

- No functional logic changes, but ensure the "Exit" behavior remains cohesive with returning to the Admin hub.

## Open Questions

- Should the "Tools" tab buttons (Lab/Programmer) close the `AdminToolsModal` when launched, or should they overlay it? (Assuming overlay/nested for now as they are separate full-screen modals).

## Verification Plan

### Automated Tests

- Run `npx tsc` to verify all imports and prop types are correctly updated across the project.

### Manual Verification

- **Entry Flow**: Tap logo 10 times, enter `0000`, and verify the **Admin Tools** modal opens to the **Timeline** tab by default.
- **Navigation**: Verify switching between **Timeline, Stats, Device, and Tools** tabs works flawlessly.
- **Deep Link**: Verify the "Diagnostic Lab" and "Programmer" can be launched from the "Tools" tab.
- **UI Audit**: Check labels and icons for "Device" vs "Devices" and "Tools" vs "Admin".
