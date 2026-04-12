# SK8Lytz Picks Admin Scheduler & Builder

This plan implements the "feat/picks-scheduler-builder" task to complete the SK8Lytz Picks administrative tools.

### Design Decisions & Rationale
To empower admins to create community picks entirely within the app, we will embed the existing `PositionalGradientBuilder` directly into the `AdminPicksScheduler` modal. Instead of building a complex new UI, the admin can simply toggle between "Create Array Pick" and "Create Pattern Pick". When generating an Array pick, the builder nodes are converted to a hex array and pushed to the `sk8lytz_picks` Supabase table under the `MULTIMODE` configuration. This avoids duplicating UI logic while providing powerful on-the-fly pick generation.

## Proposed Changes

### [MODIFY] `src/components/AdminPicksScheduler.tsx`
- **Pick Creation Modal**: Add a "Create New Pick" floating action button (FAB) or header button.
- **Mode Selection**: Inside the creation modal, allow the admin to select either **"Array Builder"** (`MULTIMODE`) or **"Program Pattern"** (`PROGRAMS`).
- **Array Builder Flow**:
  - Dynamically render `PositionalGradientBuilder` inside the modal.
  - When the admin clicks "Publish Pick", extract the active builder `nodes` and map them to standard hex strings (`multi_colors`).
  - Send an `insert()` query to Supabase `sk8lytz_picks` containing the generated JSON array, speed, brightness, and `mode: 'MULTIMODE'`.
- **Program Pattern Flow**:
  - Provide a simple numeric/selection input for `pattern_id` (1-100+) for RBM programs.
  - Insert into Supabase with `mode: 'PROGRAMS'` and the associated `pattern_id`.
- **Soft Delete / Hide**: Include a mechanism to delete or permanently deactivate picks from the scheduler list.

### [MODIFY] `src/components/DockedController.tsx` (Verification only)
- Ensure that the generated JSON payload from the Builder matches the structure `DockedController.loadFavorite` expects for `MULTIMODE / BUILDER` presets (`multiColors` string array).

## Verification Plan

### Database & Payload Verification
- Open the Admin Tools -> Picks Scheduler.
- Click "Create New Pick" -> Select "Array Builder".
- Design a blue/pink gradient and set speed to 80.
- Click Publish and check the Supabase `sk8lytz_picks` table to verify `multi_colors` is populated with valid hex strings.
- Go to the Dashboard UI, observe the new Pick appearing in the carousel.
- Tap the pick and verify the `PositionalMathBuffer` logic successfully parses it and pushes to the hardware.
