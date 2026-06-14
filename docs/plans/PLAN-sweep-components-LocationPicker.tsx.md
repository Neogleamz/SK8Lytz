# Implementation Plan: sweep-components-LocationPicker.tsx

## Goal
Fix static audit findings for the `sweep-components-LocationPicker.tsx` domain cluster.

## Proposed Changes

### [MODIFY] [LocationPicker.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/LocationPicker.tsx)
- **Line:** 138
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Geocoding search and OSM suggestions fetch calls lack high-level error handling inside callbacks, potentially failing or crashing the app flow on general Network timeout or storage errors.
- **Suggested Fix:** Ensure all network requests and storage updates are wrapped cleanly in try/catch blocks with proper error diagnostics.

### [MODIFY] [LocationPicker.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/LocationPicker.tsx)
- **Line:** 58
- **Rule:** R-07
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Calls createStyles(Colors) dynamically inside the component body on every single render.
- **Suggested Fix:** Memoize styles using useMemo or move stylesheet compilation outside the component.

### [MODIFY] [LocationPicker.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/LocationPicker.tsx)
- **Line:** 140
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** AppLogger.warn call for OSM fetch errors lacks the required payload_size and ssi context properties in the options metadata.
- **Suggested Fix:** Pass { payload_size: 0, ssi: 0 } in the metadata payload options.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
