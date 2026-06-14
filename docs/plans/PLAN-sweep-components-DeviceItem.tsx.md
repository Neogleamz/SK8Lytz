# Implementation Plan: sweep-components-DeviceItem.tsx

## Goal
Fix static audit findings for the `sweep-components-DeviceItem.tsx` domain cluster.

## Proposed Changes

### [MODIFY] [DeviceItem.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DeviceItem.tsx)
- **Line:** 25
- **Rule:** R-07
- **Severity:** HIGH | **Confidence:** CONFIRMED
- **Description:** DeviceItem component is used as a list item in FlatLists but is not memoized using React.memo. This causes all items in the list to re-render when any list item changes, which is a major performance bottleneck during high-frequency Bluetooth scan updates.
- **Suggested Fix:** Wrap the DeviceItem export with React.memo, e.g. export default React.memo(DeviceItem);

### [MODIFY] [DeviceItem.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DeviceItem.tsx)
- **Line:** 28
- **Rule:** R-21
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Violates Master Reference 'Hardcoded Hardware Heuristics' by hardcoding strings ('HALOZ', 'SOULZ', 'led', 'zengge', 'magic') to check if a device is custom Neogleamz hardware, instead of using catalog-derived type matching.
- **Suggested Fix:** Determine if the device is a generic controller by resolving its type/profile from the ProductCatalog.

### [MODIFY] [DeviceItem.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DeviceItem.tsx)
- **Line:** 106
- **Rule:** R-14
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** The nested power toggle button TouchableOpacity is missing accessibilityRole='button' and accessibilityLabel, making it inaccessible to screen readers inside the parent button item.
- **Suggested Fix:** Add accessibilityRole="button" and accessibilityLabel="Toggle device power" to the child TouchableOpacity.

### [MODIFY] [DeviceItem.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DeviceItem.tsx)
- **Line:** 92
- **Rule:** R-07
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Inline style object evaluated inside standard Array.map in list item component creates garbage collection overhead and prevents React from optimizing styles dynamically.
- **Suggested Fix:** Extract dynamic styles or static style components into createStyles stylesheet and apply dynamically by condition.

### [MODIFY] [DeviceItem.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DeviceItem.tsx)
- **Line:** 111
- **Rule:** R-07
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Inline arrow function passed to child button onPress inside a list item component causes recreation of callback functions on every render pass, triggering unnecessary child re-renders.
- **Suggested Fix:** Extract the inline handler to a useCallback wrapper or pass the ID up and handle propagation in the parent.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
