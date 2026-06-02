# Extremely Detailed Implementation Plan: Pattern Registry Reorganization

This plan addresses the full re-categorization of patterns from `multimode_patterns_new_categories.xls`, hiding deprecated patterns, and ensuring absolute visualizer safety across the application.

## 1. Core Registry Update (`src/protocols/PatternEngine.ts`)

The `PatternEngine.ts` is the single source of truth (SSOT). Changing properties here safely propagates to the rest of the application without breaking math functions, because the mathematical visualizer relies strictly on `patternId`, not `group`.

*   **Update `SK8LytzTemplate` Interface**:
    *   Add the optional property `isHidden?: boolean;`
*   **Dissolve `Test` Category (IDs 201-233)**:
    *   Change `group` from `'Test'` to their new functional categories (`Chase`, `Rainbow`, `Breathe`, `Marquee`, `Sparkle`, `SK8Lytz`) exactly as mapped in the spreadsheet.
*   **Reassign Existing Patterns**:
    *   `[ID 23] Color Flow` -> `group: 'SK8Lytz'`
    *   `[ID 32] Aurora Borealis` -> `group: 'SK8Lytz'`
*   **Deprecate (Hide) 8 Legacy Patterns**:
    *   Target IDs: `17, 18, 24, 26, 30, 36, 40, 72`
    *   Set `isHidden: true`
    *   Set `group: 'N/A'`
    *   *(Note: These remain in the `SK8LYTZ_TEMPLATES` array so that if a user has a saved preset or hardware state requesting one of these IDs, the `SpatialEngine.ts` and `VisualizerEngine.ts` mathematical builders will still find and render them safely, preventing crashes.)*

## 2. UI Pattern Picker Update (`src/components/patterns/PatternPickerTab.tsx`)

The Picker UI must respect the new categories and safely filter out hidden patterns.

*   **Update Category Array**:
    *   Remove `'Test'` from the `CATEGORIES` array.
    *   Add `'SK8Lytz'` to the `CATEGORIES` array.
    *   New array: `['Solid', 'Rainbow', 'Sparkle', 'Chase', 'Marquee', 'Wave', 'Breathe', 'SK8Lytz']`
*   **Add UI Styling for New Category**:
    *   Add a new entry in `CATEGORY_STYLES` for `SK8Lytz`.
    *   `icon: 'skateboard'` (or `star-shooting`)
    *   `colors: ['#FF00FF', '#00FFFF', '#FF0055']` (a dynamic, premium gradient)
*   **Enforce Hidden Filtering**:
    *   In the `filteredTemplates` logic, explicitly filter out any pattern where `effect.isHidden === true`.
    ```tsx
    const filteredTemplates = SK8LYTZ_TEMPLATES.filter((effect) => {
      if (effect.group === 'Street') return false;
      if (effect.isHidden) return false;
      return effect.group === activeCategory;
    });
    ```

## 3. Visualizer Alignment Verification (`VisualizerUnit.tsx` & `ProductVisualizer.tsx`)

To ensure the pattern and product visualizers stay in alignment:
*   **Architectural Validation**: The visualizers receive `patternId`, which is passed to `getVisualizerFrame()` and `getHardwarePixelArray()`. Because we are *not* changing the IDs or deleting the items from the array, the mathematical rendering pipeline is 100% untouched.
*   **Fallback Safety**: The visualizers default to a solid fill if a `patternId` is unrecognized. Because the 8 hidden patterns still exist in the registry, they will NOT fall back; they will render correctly if invoked by legacy presets.

## User Review Required

> [!WARNING]  
> Deprecating IDs 17, 18, 24, 26, 30, 36, 40, and 72 will remove them from the UI. If a user previously had one of these selected as a favorite, the UI might need to handle a case where a favorite pattern doesn't show up in the picker list but still renders in the visualizer.

> [!NOTE]  
> The new `SK8Lytz` category will be given a sleek `#FF00FF` -> `#00FFFF` -> `#FF0055` gradient with a `star-shooting` icon in the picker.

---

## 4. Bucket List Addition

As requested, this task will be appended to the `tools/SK8Lytz_Bucket_List.md` Kanban board under the `## 🚧 ACTIVE SPRINT` section so it can be executed systematically.
