# Implementation Plan: sweep-hooks-ble

## Goal
Fix static audit findings for the `sweep-hooks-ble` domain cluster.

## Proposed Changes

### [MODIFY] [useBLEScanner.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts)
- **Line:** 267
- **Rule:** R-09, R-04
- **Severity:** HIGH | **Confidence:** CONFIRMED
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [ble-simulator.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/__tests__/ble-simulator.test.ts)
- **Line:** 82
- **Rule:** R-06
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** console.error logs raw error object in a test file without standard unwrapping.
- **Suggested Fix:** console.error('...', error instanceof Error ? error.message : String(error));

### [MODIFY] [ble-simulator.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/__tests__/ble-simulator.test.ts)
- **Line:** 92
- **Rule:** R-06
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** console.error logs raw error object in a test file without standard unwrapping.
- **Suggested Fix:** console.error('...', error instanceof Error ? error.message : String(error));

### [MODIFY] [ble-simulator.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/__tests__/ble-simulator.test.ts)
- **Line:** 138
- **Rule:** R-19
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Literal raw power command byte array [0x71, 0x23, 0x0F, 0xA3] constructed outside src/protocols/ for a simulator request.
- **Suggested Fix:** Import or delegate to ZenggeProtocol.turnOn() to generate the expected byte array.

### [MODIFY] [ble-simulator.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/__tests__/ble-simulator.test.ts)
- **Line:** 144
- **Rule:** R-19
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Literal raw power-on command byte array [0x71, 0x23, 0x0f] constructed outside src/protocols/ for simulator state validation.
- **Suggested Fix:** Import and reference bytes from ZenggeProtocol or use a mock protocol helper inside src/protocols/.

### [MODIFY] [ble-simulator.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/__tests__/ble-simulator.test.ts)
- **Line:** 156
- **Rule:** R-19
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Literal raw power-off command byte array [0x71, 0x24, 0x0f] constructed outside src/protocols/ for simulator state validation.
- **Suggested Fix:** Import and reference bytes from ZenggeProtocol or use a mock protocol helper inside src/protocols/.

### [MODIFY] [ble-simulator.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/__tests__/ble-simulator.test.ts)
- **Line:** 167
- **Rule:** R-19
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Literal raw hardware query settings command byte array [0x63, 0x12, 0x21, 0x0F] constructed outside src/protocols/ for simulator EEPROM validation.
- **Suggested Fix:** Import and reference bytes from ZenggeProtocol or use a mock protocol helper inside src/protocols/.

### [MODIFY] [ble-simulator.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/__tests__/ble-simulator.test.ts)
- **Line:** 201
- **Rule:** R-19
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Raw byte array constructed outside src/protocols/ and manually assigned opcode 0x59 to test static colorful lockout risk warning.
- **Suggested Fix:** Import and delegate to ZenggeProtocol to build multi-color payloads, or establish mock helpers inside src/protocols/.

### [MODIFY] [ble-simulator.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/__tests__/ble-simulator.test.ts)
- **Line:** 226
- **Rule:** R-19
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Raw byte array constructed outside src/protocols/ and manually assigned opcode 0x59 to verify safe static colorful array boundary check.
- **Suggested Fix:** Import and delegate to ZenggeProtocol to build multi-color payloads, or establish mock helpers inside src/protocols/.

### [MODIFY] [useBLEScanner.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts)
- **Line:** 77
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Unawaited floating promise for storage/network IO without chained .catch: "AsyncStorage.getItem(STORAGE_APP_SETTINGS).then(cached => {
      if (cached) {
        try {
          const parsed = JSON.parse(cached);
          if (parsed.hw_setup_rssi_threshold !== undefined) {
             setupRssiThresholdRef.current = parseInt(String(parsed.hw_setup_rssi_threshold), 10);
          }
        } catch {}
      }
    })"
- **Suggested Fix:** Append .catch() or await/wrap with try-catch.

### [MODIFY] [useBLEScanner.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts)
- **Line:** 125
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** Scanner warnings for telemetry flush failures and scan errors are logged without payload_size or ssi.
- **Suggested Fix:** Include payload_size and ssi parameters.

### [MODIFY] [useBLEBatterySweep.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEBatterySweep.ts)
- **Line:** 118
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [useBLEBatterySweep.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEBatterySweep.ts)
- **Line:** 183
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [useBLEInterrogator.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEInterrogator.ts)
- **Line:** 37
- **Rule:** R-17
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Discards cleanup function from createProbeQueue. The useBLEInterrogator hook instantiates createProbeQueue, which returns queueDeviceForInterrogation and a cleanup function. However, useBLEInterrogator does not capture or call this cleanup function on unmount. This leads to a minor memory/timer leak if the hook unmounts while a probe is pending or queued, leaving the internal queue timer active.
- **Suggested Fix:** Extract the cleanup function returned from createProbeQueue and execute it inside a useEffect cleanup function within useBLEInterrogator.

### [MODIFY] [useBLEScanner.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts)
- **Line:** 133
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [useBLEScanner.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts)
- **Line:** 205
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [useBLEScanner.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts)
- **Line:** 222
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
