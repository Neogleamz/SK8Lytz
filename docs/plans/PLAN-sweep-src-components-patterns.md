# Implementation Plan: sweep-src-components-patterns

## Goal
Fix bugs and rule violations identified during the deep-dive code hunt for the `sweep-src-components-patterns` domain cluster.

## Batch & Wave
- **Wave:** 1
- **Prerequisite:** None

## Proposed Changes
### [MODIFY] PatternCard.tsx
- Line 53: Fix `R-25` violation. Touchable element has no accessibility properties, preventing screen reader interaction. (Suggested: Add accessibilityRole="button", accessibilityState={{ selected: isSelected }} and accessibilityLabel={effect.name} to the TouchableOpacity.)
- Line 56: Fix `R-07` violation. An inline arrow function is passed directly to the `onPress` prop of `TouchableOpacity` in `PatternCard` which is rendered inside the `PatternPickerTab`'s `FlatList` rendering hotpath. Recreating the callback on every render can cause rendering updates and GC overhead in large list rendering loops. (Suggested: Define a memoized local handler inside `PatternCard` using `useCallback`: `const handlePress = useCallback(() => { onSelect(effect.id); }, [onSelect, effect.id]);`)
### [MODIFY] UnifiedPatternPicker.tsx
- Line 68: Fix `R-04` violation. AppLogger.error call is missing telemetry context parameters: payload_size, ssi. Rule R-04 mandate that payload_size and ssi must be provided for all telemetry entries. (Suggested: AppLogger.error(message, errorObj, { ...context, payload_size: 0, ssi: 0 });)

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.
