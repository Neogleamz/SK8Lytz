# Universal Favorites Engine (Cloud Sync)

Migrates the legacy global "Heart" button from local `AsyncStorage` to the Supabase `user_saved_presets` cloud table. Unifies the save architecture across Patterns, Custom Gradients, and Scene Sequences into a single context-aware Styles tray.

## User Review Required
None. Pre-approved via Brainstorming session.

## Proposed Changes

### Data Layer (`src/hooks/useFavorites.ts`)
#### [MODIFY] `useFavorites.ts`
- **Migration**: Refactor initialization to fetch from the Supabase `user_saved_presets` table where `type='favorite'`.
- **Offline Mode**: Retain `AsyncStorage` for offline caching and fallback.
- **Save Logic**: Update `saveFavorite` to execute an `upsert` to Supabase, injecting `capturedState` into the JSON payload with `type='favorite'`.

### UI Components (`src/components/docked/`)
#### [MODIFY] `FavoritePromptModal.tsx`
- **Context Chips**: Add a row of 3-4 suggestion chips below the text input (e.g., `[ Crimson Glow ]`, `[ Fast Rainbow Chase ]`).
- **Logic**: Tapping a chip auto-fills the text input.

#### [MODIFY] `DockedController.tsx`
- **Naming Engine**: Enhance `handleSaveFavoriteClick` to generate context-aware names instead of generic fallbacks.
- **Restore Logic (`loadFavorite`)**: Fix the "Pattern 1 bug". Ensure it maps the saved `patternId` directly to the `UnifiedPatternPicker` and injects exact `speed`, `brightness`, and `colors` payloads.

### Builder Integrations (`src/components/patterns/` & `src/components/scenes/`)
#### [MODIFY] `GradientBuilderModal.tsx`
- Add a Heart (🤍) button next to the standard Save button.
- Action: Pushes the current node array/transition directly to `useFavorites` array so it appears on the Dashboard Styles tray.

#### [MODIFY] `SceneBuilderModal.tsx`
- Add a Heart (🤍) button next to the standard Save button.
- Action: Pushes the current multi-step sequence directly to `useFavorites` array so it appears on the Dashboard Styles tray.

## Verification Plan
### Automated Tests
- TSC No-Emit check to ensure `IFavoriteState` typing remains unbroken across Supabase mappings.
### Manual Verification
- Tap a solid color, click Heart, verify Context Chip says "Solid [ColorName]". Save.
- Verify the favorite appears in the Styles tray with the correct CSS gradient rendering.
- Tap the favorite from a different mode and verify it perfectly restores the exact color, speed, and mode.
