# Implementation Plan: sweep-protocols-ZenggeProtocol.ts

## Goal
Fix static audit findings for the `sweep-protocols-ZenggeProtocol.ts` domain cluster.

## Proposed Changes

### [MODIFY] [ZenggeProtocol.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeProtocol.ts)
- **Line:** 1
- **Rule:** R-23
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** Core low-level Zengge hardware controller protocol handler. Implements binary packet compilation, mode command formatting, and static facade methods.
- **Suggested Fix:** Split custom mode byte generators, music packet buffers, and static controller facades into separate modular compilation files.

### [MODIFY] [ZenggeProtocol.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeProtocol.ts)
- **Line:** 384
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Warning logs in parseFirmwareFromAdvertisement catch block and line 508 critical hardware warning lack telemetry context keys payload_size and ssi.
- **Suggested Fix:** Add payload_size: 0, ssi: 0 keys to the log context parameter.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
