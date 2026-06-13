# Fix: AdminToolsModal TS Debt & Metadata Sync

Resolve 4 specific TypeScript compilation errors in `AdminToolsModal.tsx` and synchronize the `EVENT_META` map with recent additions to `AppLogger.ts`.

## User Review Required

> [!NOTE]
> This is a low-risk cleanup focused solely on type safety and documentation. No business logic or BLE protocol flows are affected.

## Proposed Changes

### [Admin Architecture]

#### [MODIFY] [AdminToolsModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AdminToolsModal.tsx)

- **Import Sync**: Add `import { supabase } from '../services/supabaseClient';` to resolve the undefined `supabase` reference in `handleSaveProfile`.
- **Metadata Sync**: Add entries to `EVENT_META` for all missing `EventType` keys:
  - `MOUNT`, `UNMOUNT`, `SYNC`, `REJOIN`, `FTUE`
  - `SESSION_SAVED`, `SPEED_REACTIVE_ENABLED`, `SPEED_REACTIVE_DISABLED`
- **Type Guard Fix**: Add `batteryCapacityMilliAmpereHour: 3000` to the `blankProfile()` generator to satisfy the `ProductProfile` interface.
- **Dead Code Removal**: Delete the `tab === 'products'` conditional branch (line 914) as the Product Manager has been extracted to a sub-modal.
- **UI Enhancement**: Add a quantitative `batteryCapacityMilliAmpereHour` input field to the `renderProductsTab` form so admins can edit the new capacity field.

## Verification Plan

### Automated Tests

- `npx tsc` to verify that `AdminToolsModal.tsx` no longer throws compilation errors.
- Visual inspection of the Admin Hub `Timeline` tab to ensure the new icons/labels render correctly for the synced event types.

### Manual Verification

1. Open Admin Tools.
2. Navigate to **Tools → Product Manager**.
3. Verify the new **Battery Capacity (mAh)** field appears and persists to Supabase.
4. Verify the **Timeline** tab correctly labels any `SESSION_SAVED` or `STATUS` events.
