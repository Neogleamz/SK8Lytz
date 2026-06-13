# SK8Lytz Picks Admin Scheduler

This plan implements the SK8Lytz Picks Admin Scheduler (#28) per the bucket list.

## Design Decisions & Rationale

To keep the public UI clean, we will embed the SK8Lytz Picks Admin Scheduler deep inside the Analytics `LogViewerModal` under the Admin Tools tab. We will build a dedicated, modular `AdminPicksScheduler.tsx` component that queries the `sk8lytz_picks` table and provides toggle switches and native date pickers to establish automatic, zero-maintenance presentation schedules.

## Proposed Changes

### Component Architecture

#### [NEW] `src/components/AdminPicksScheduler.tsx`

- Create a dedicated modal component for editing `sk8lytz_picks`.
- On load, it fetches `SELECT * FROM sk8lytz_picks ORDER BY sort_order ASC`.
- Renders a list where each item has:
  - **Status Toggle**: A Switch bridging to `is_active`.
  - **Date Boundaries**: Two buttons for `active_from` and `active_until` that invoke `@react-native-community/datetimepicker`.
  - **Save Action**: Instantly mutates the database via `supabase.from('sk8lytz_picks').update(...)`.

#### [MODIFY] `src/components/LogViewerModal.tsx`

- Integrate `AdminPicksScheduler` alongside the existing Diagnostic Lab and Programmer tools on the Admin tab.
- Add a new "🗓️ SK8Lytz Picks Scheduler" button that sets a boolean flag to render the `AdminPicksScheduler` modal.

## Verification Plan

### Manual Verification

- Open the LogViewerModal (long-press Analytics or equivalent trigger).
- Navigate to the Admin Tab.
- Open the "Picks Scheduler".
- Toggle a pick's `is_active` state and adjust the `active_from` / `active_until` dates.
- Observe real-time success/error popups and verify the dates successfully update within the Supabase Dashboard.
