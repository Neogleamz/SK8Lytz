# Implementation Plan: sweep-components-dashboard

## Goal
Fix static audit findings for the `sweep-components-dashboard` domain cluster.

## Proposed Changes

### [MODIFY] [HardwareStatusPills.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/HardwareStatusPills.tsx)
- **Line:** 18
- **Rule:** R-21
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Violates Master Reference 'Hardcoded Hardware Heuristics' by hardcoding defaults for per-segment LED points and segments based on name-matching 'soul', instead of deriving this information from the centralized ProductCatalog.
- **Suggested Fix:** Derive device default metadata dynamically from ProductCatalog using the device's type/model identifier rather than name string matching.

### [MODIFY] [SupportModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/SupportModal.tsx)
- **Line:** 41
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Linking.openURL is called without error handling or a .catch block on the promise, which will cause unhandled promise rejections if the device fails to open the browser or mail app.
- **Suggested Fix:** Wrap the Linking.openURL calls in error catching blocks or chain .catch(...).

### [MODIFY] [SkateGroupCard.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/SkateGroupCard.tsx)
- **Line:** 66
- **Rule:** R-08
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Launders array of colors string[] to a strict read-only tuple structure.
- **Suggested Fix:** Refactor component signature or define colors array with the target tuple type initially.

### [MODIFY] [SkateGroupCard.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/SkateGroupCard.tsx)
- **Line:** 71
- **Rule:** R-07
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Component creates numerous inline style objects and dynamically instantiates them inside map loops while rendering, causing rendering performance overhead inside the parent slab lists.
- **Suggested Fix:** Refactor inline styles to statically compiled stylesheet definitions and optimize dynamic layout logic.

### [MODIFY] [DashboardCrewPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/DashboardCrewPanel.tsx)
- **Line:** 122
- **Rule:** R-16
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Uses a hardcoded setTimeout delay of 300ms to dispatch apply cloud scene instead of utilizing a queue or check.
- **Suggested Fix:** Consolidate scene application within the connection ready state transition rather than using arbitrary timeouts.

### [MODIFY] [HardwareStatusPills.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/HardwareStatusPills.tsx)
- **Line:** 10
- **Rule:** R-08
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Use of the 'any' type in the component's props definition (device is typed Record<string, any>), bypassing strict type safety.
- **Suggested Fix:** Replace Record<string, any> with a concrete device/profile interface type.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
