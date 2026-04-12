# Plan: `integrate-builder-presets`

### Design Decisions & Rationale
The Community Hub already has a `shared_scenes` Supabase table and `CommunityModal.tsx`. This task wires `PositionalGradientBuilder.tsx` presets to that table: users can submit their current builder array as a named "scene" and pull community presets directly into the builder. We reuse the existing `Favorites` AsyncStorage pattern for local caching.

---

## Proposed Changes

### [MODIFY] Supabase: `shared_scenes` table
- Confirm columns: `id`, `user_id`, `name`, `payload` (JSON array of RGB values), `likes`, `created_at`.
- Add `scene_type: 'BUILDER_PRESET'` enum value to distinguish from other scene types.

### [MODIFY] `src/components/PositionalGradientBuilder.tsx`
- Add a "Share to Community" button in the builder toolbar.
- On tap: prompt for scene name → upsert to `shared_scenes` with `scene_type: 'BUILDER_PRESET'`.

### [MODIFY] `src/components/CommunityModal.tsx`
- Add a "Builder Presets" filter tab.
- Fetches `shared_scenes` where `scene_type = 'BUILDER_PRESET'`.
- Each card shows a preview strip (the RGB array rendered as a mini gradient bar) + Like button.
- "Use This" button: deep-links into the builder with the payload pre-loaded.

---

## Open Questions
- **Q:** Does the `shared_scenes` table already exist in Supabase? (Check current schema before writing a migration.)
- **Q:** Should community presets be filterable by product type (e.g. HALOZ-only)? They have different pixel counts.

## Verification Plan
1. Build a custom gradient array in the builder.
2. Tap "Share" and set a name.
3. Open Community Hub > Builder Presets and verify the preset appears.
4. Tap "Use This" and confirm the builder loads the correct array.
