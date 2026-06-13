# Implementation Plan — Harden BLE Regression Shields

Build an automated regression protection shield for the three critical BLE and onboarding wizard scanning bugs to ensure they never recur in future agent sessions.

## User Review Required

> [!NOTE]
> This task focuses purely on regression testing, documentation rules, and Master Reference invariants. It does not introduce any changes to the production runtime application logic, making it extremely low risk.

## Open Questions

None.

## Proposed Changes

---

### 🛡️ Rules and Guardrails

Summary: Codify the three regression rules into the project's permanent behavior constraints and reference files.

#### [MODIFY] [21_GUARDRAILS.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/21_GUARDRAILS.md)
- Add explicit entries for:
  - `[R-22] FTUE Background Scan Idempotency`: Scan triggers must run persistent sweeps directly when `registeredMacs.length === 0`.
  - `[R-23] Wizard Scanning Non-Blocking Next`: Next button must never lockout transition when scanning is active.
  - `[R-24] Group Ground Truth`: Multi-device states must rely strictly on `connectedDevices.length > 1`.

#### [MODIFY] [prime-directive.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.agents/rules/prime-directive.md)
- Add a new table section "🛡️ Hard Onboarding & BLE Invariants" specifying the 3 invariants to prevent future regressions during JIT micro-reads.

#### [MODIFY] [SK8Lytz_App_Master_Reference.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SK8Lytz_App_Master_Reference.md)
- Update §3 to explicitly document the FTUE scan idempotency, the non-blocking next button, and group connection length ground truth as non-negotiable architectural constraints.

---

### 🧪 Regression Unit Tests

Summary: Implement tests to verify that these behaviors remain intact.

#### [NEW] [useBLEScanner.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/__tests__/useBLEScanner.test.ts)
- Create unit tests for `useBLEScanner` hook asserting:
  - When `registeredMacs.length === 0`, `scanForPeripherals` calls `startSweeper` to execute persistent FTUE scanning.
  - Verification that sandbox devices are properly mocked when sandbox is enabled.

#### [MODIFY] [HardwareSetupWizardScreen.test.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/__tests__/HardwareSetupWizardScreen.test.tsx)
- Add contract tests checking:
  - Next button condition: button is enabled whenever `pendingRegistrations.length > 0` (even when `bleState === 'SCANNING'`).

#### [MODIFY] [ConnectService.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/__tests__/ConnectService.test.ts)
- Verify group connection logic and confirm group check relies on `connectedDevices.length > 1` (ground truth validation).

---

## Verification Plan

### Automated Tests
- Run the verify runner to ensure everything builds and tests pass:
  ```powershell
  npm run verify
  ```
