# Implementation Plan: sweep-components-patterns

## Goal
Fix static audit findings for the `sweep-components-patterns` domain cluster.

## Proposed Changes

### [MODIFY] [GradientLibraryTab.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/patterns/GradientLibraryTab.tsx)
- **Line:** 40
- **Rule:** R-07
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** FlatList renderItem (renderGradientCard) contains multiple inline function declarations (onPress, onLongPress) and a nested dynamic style mapping array that allocates new style objects inside every item container, which degrades scrolling performance.
- **Suggested Fix:** Create a memoized GradientCard component, pass the preset object and base callbacks, and handle internal mapping/callbacks within that component to avoid recreating function/style allocations during list render.

### [MODIFY] [PatternPickerTab.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/patterns/PatternPickerTab.tsx)
- **Line:** 66
- **Rule:** R-08
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** Type laundering ('as unknown as') to bypass LinearGradient type system requirements. CATEGORY_STYLES colors array should be declared as a tuple rather than generic string[].
- **Suggested Fix:** Update CATEGORY_STYLES typing to define colors as a strict tuple: readonly [string, string, ...string[]].

### [MODIFY] [UnifiedPatternPicker.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/patterns/UnifiedPatternPicker.tsx)
- **Line:** 68
- **Rule:** R-06, R-04
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** Catch block logs BLE write error via AppLogger.error but passes raw error object directly without err instanceof Error type checking or message unwrapping.
- **Suggested Fix:** Unwrap error message: AppLogger.error('UnifiedPatternPicker writeToDevice failed', err instanceof Error ? err.message : String(err));

### [MODIFY] [PatternPickerTab.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/patterns/PatternPickerTab.tsx)
- **Line:** 160
- **Rule:** R-07
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Inline styles passed to FlatList props contentContainerStyle and columnWrapperStyle cause unnecessary component re-renders.
- **Suggested Fix:** Move these style declarations outside the component render function or define them inside the StyleSheet.create object.

### [MODIFY] [UnifiedPatternPicker.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/patterns/UnifiedPatternPicker.tsx)
- **Line:** 67
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Passes raw error object directly to AppLogger.error. While AppLogger.error performs standard unwrapping internally, call-site unwrapping is preferred for consistency and safety if AppLogger.error API changes.
- **Suggested Fix:** AppLogger.error('...', err instanceof Error ? err : new Error(String(err)));

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
