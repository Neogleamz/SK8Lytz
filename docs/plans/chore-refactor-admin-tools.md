# Refactor Admin Tools Modal

The `AdminToolsModal.tsx` has grown to over 800 lines and is containing multiple distinct responsibilities: telemetry timeline, device tracking, App governance hooks, and the Product Manager editor. This plan aims to decompose this "God Object" into isolated modules within a dedicated `admin` subdirectory.

## User Review Required

> [!NOTE]
> This is a structural refactoring task. There will be **no changes to the underlying business logic**. The goal is strictly code hygiene and modularity to adhere to the SK8Lytz engineering standards. 

## Proposed Changes

We will create a new directory `src/components/admin/` to logically group these tools.

### `src/components/admin/`

#### [NEW] [DeviceTab.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/DeviceTab.tsx)
- Extracts the `DeviceTab` functional component.
- Handles the display of the BLE target history and diagnostic tracking.

#### [NEW] [StatsTab.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/StatsTab.tsx)
- Extracts the `StatsTab` component.
- Renders the `StatRow` and analytical aggregations derived from `useAdminTelemetry`.

#### [NEW] [AdminTab.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/AdminTab.tsx)
- Extracts the gateway buttons to `Diagnostic Lab`, `Firmware Programmer`, `App Manager`, and `Product Manager`.

#### [NEW] [AppManagerModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/AppManagerModal.tsx)
- Extracts the remote governance policy switches and EULA enforcement logic.

#### [NEW] [ProductManagerModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/ProductManagerModal.tsx)
- Unifies the `ProductManager` overlay logic and the massive `ProductsTab` editor interface.
- Handles creation and patching of hardware profiles.

#### [NEW] [ConfirmDeleteModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/ConfirmDeleteModal.tsx)
- Extracts the destructive action confirmation UI.

### `src/components/`

#### [MODIFY] [AdminToolsModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AdminToolsModal.tsx)
- Offloads all presentation components to the new `admin/` module directory.
- Acts solely as the Controller: invoking `useAdminTelemetry`, `useProductManager`, and `useAdminSettings` hooks, and orchestrating state to the child components.
- Move it to `src/components/admin/AdminToolsModal.tsx` and fix imports app-wide to clear out the main components folder.

## Verification Plan

### Automated Tests
- Run `npx tsc --noEmit` to mathematically guarantee that all props, imports, and component signatures resolve correctly across the isolated files.

### Manual Verification
- Review the `Admin Tools` tab in the web visualizer or the iOS/Android app to ensure all 4 tabs (Timeline, Stats, Device, Tools) render flawlessly and the nested modals open properly.
