# Implementation Plan: sweep-screens-Onboarding

## Goal
Fix static audit findings for the `sweep-screens-Onboarding` domain cluster.

## Proposed Changes

### [MODIFY] [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
- **Line:** 1
- **Rule:** R-23
- **Severity:** HIGH | **Confidence:** CONFIRMED
- **Description:** Screen file size is 42.4KB, exceeding the 30KB limit. Should be refactored to extract step-specific content or visualizer code.
- **Suggested Fix:** Extract Step-specific sub-components into separate files (e.g. WizardStepOne, WizardStepTwo).

### [MODIFY] [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
- **Line:** 200
- **Rule:** R-26
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Handles async BLE connections inside wizard setup callbacks without a re-entrancy locking guard, which could lead to multiple concurrent connection requests if tapped repeatedly.
- **Suggested Fix:** Wrap the async body in a re-entrancy check utilizing a local isProcessing ref/state.

### [MODIFY] [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
- **Line:** 72
- **Rule:** R-18
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Uses multiple independent boolean variables (isScanning, isPairing, loading, error) instead of a single FSM state union, increasing vulnerability to split-brain states where two mutually exclusive operations appear active.
- **Suggested Fix:** Consolidate status booleans into a single state union type SetupStepState = 'IDLE' | 'SCANNING' | 'PAIRING' | 'LOADING' | 'ERROR';

### [MODIFY] [PermissionsOnboardingScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/PermissionsOnboardingScreen.tsx)
- **Line:** 19
- **Rule:** R-07
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** StyleSheet is compiled dynamically inside the component body on every render by calling createStyles(Colors, insets).
- **Suggested Fix:** Define the stylesheet statically outside of the component or wrap it in a useMemo hook to prevent recompilation on every render.

### [MODIFY] [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
- **Line:** 106
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Passes raw error object directly to AppLogger.error. While AppLogger.error performs standard unwrapping internally, call-site unwrapping is preferred for consistency and safety if AppLogger.error API changes.
- **Suggested Fix:** AppLogger.error('...', err instanceof Error ? err : new Error(String(err)));

### [MODIFY] [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
- **Line:** 231
- **Rule:** R-04, R-24
- **Severity:** LOW | **Confidence:** CONFIRMED
- **Description:** Dynamic key construction using local prefix literal '@sk8_hw_' to cache probed configs, creating a duplication and out-of-sync risk with InterrogatorService.ts which also constructs keys using the same pattern.
- **Suggested Fix:** Move key prefix definition to storageKeys.ts and export a shared key generator function.

### [MODIFY] [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
- **Line:** 235
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
- **Line:** 670
- **Rule:** R-16
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Hardcoded raw setTimeout delay used to stagger EEPROM persistence write (EEPROM_PERSIST_DELAY_MS) during hardware wizard configuration complete stage.
- **Suggested Fix:** Refactor settings writing and validation into a queue-managed connection lifecycle or let the underlying connection class handle verification instead of raw timeout delays in the UI controller.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
