# Implementation Plan: sweep-providers-BluetoothGuard.tsx

## Goal
Fix static audit findings for the `sweep-providers-BluetoothGuard.tsx` domain cluster.

## Proposed Changes

### [MODIFY] [BluetoothGuard.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/providers/BluetoothGuard.tsx)
- **Line:** 18
- **Rule:** R-26
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Potential state updates on unmounted component during async permission checks. If the BluetoothGuard component is unmounted while checkPermission is pending, calls to setHasPermission and setStatus will trigger React state warnings.
- **Suggested Fix:** Use a local ref tracker (e.g. isMountedRef) to guard state updates inside asynchronous resolves.

### [MODIFY] [BluetoothGuard.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/providers/BluetoothGuard.tsx)
- **Line:** 57
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Floating promise call on Linking.openSettings(). The async operation is invoked within an Alert button callback without a catch handler or await statement.
- **Suggested Fix:** Add a catch block or wrap in an async handler, e.g. Linking.openSettings().catch(err => AppLogger.warn('Linking error', err))

### [MODIFY] [BluetoothGuard.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/providers/BluetoothGuard.tsx)
- **Line:** 24
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [BluetoothGuard.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/providers/BluetoothGuard.tsx)
- **Line:** 133
- **Rule:** R-25
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Missing accessibility properties on the interactive TouchableOpacity button that grants Bluetooth access. It lacks accessible={true}, accessibilityRole, and accessibilityLabel.
- **Suggested Fix:** Add accessible={true} accessibilityRole="button" accessibilityLabel="Grant Access" to the TouchableOpacity component.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
