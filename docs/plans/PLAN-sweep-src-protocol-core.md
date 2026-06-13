# Implementation Plan: sweep-src-protocol-core

This is a synthesized sweep plan addressing all rule violations identified in the **PROTOCOL_CORE** domain cluster.

## User Review Required

> [!IMPORTANT]
> Verify that the files modified match your expectations and that you've approved the wave ordering before commencing.

## Open Questions

None.

## Proposed Changes

### PROTOCOL_CORE Domain File Sector Sweep

Grouped by affected files:

#### [MODIFY] [useProtocolBuilder.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useProtocolBuilder.ts)
- **Line 14 [MEDIUM]:** TransitionType mapping mismatch in TRANSITION_TYPES. The hook maps 0x00 to 'CASCADE', which is invalid on Zengge hardware (comment in ZenggeProtocol.ts: '0x00 is NOT valid - sends undefined commandType to hardware'). It also maps 0x02 to 'STROBE' (actually Running Water on physical hardware) and 0x03 to 'TRIGGER' (actually Strobe on physical hardware). This results in incorrect bytes generated during custom testing. (Rule: R-21)
- **Line 119 [MEDIUM]:** Logical conflation of matrixStyle and bldMic in 0x73 Music Config builder. The hook evaluates the matrixStyle parameter as 'bldMic ? 0x27 : 0x26', where bldMic represents device/app microphone state. This conflation is incorrect because matrixStyle governs hardware display configuration (0x26 = Light Bar, 0x27 = Light Screen), blocking valid test combinations like Light Bar with Device mic or Light Screen with App mic. (Rule: R-21)

#### [MODIFY] [BanlanxAdapter.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/BanlanxAdapter.ts)
- **Line 98 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)

#### [MODIFY] [ZenggeAdapter.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeAdapter.ts)
- **Line 260 [HIGH]:** Split-brain/inconsistent implementation of ZENGGE BLE 0x40 chunking. ZenggeAdapter.prepareForTransmission implements a naive [0x40, chunkIndex, totalChunks, ...chunkData] header, whereas ZenggeProtocol.buildChunkedFrames and BleWriteDispatcher implement the official Protocol Bible structure with sequence numbers, offset words, and 0x0B opcode wrapper. This naive chunking in the adapter will fail on physical 0xA3 hardware. (Rule: R-21)

#### [MODIFY] [ZenggeProtocol.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeProtocol.ts)
- **Line 96 [HIGH]:** Handles serialization and packet construction for the Zengge Bluetooth protocol, exceeding the size limit at 53.7KB, violating R-23. (Rule: R-23)
- **Line 609 [LOW]:** Hardcoded maximum pixel length in streamPixelFrame. The method clamps input pixels to 54 under the assumption of a static default MTU. In systems with dynamic MTU negotiation, this hardcoded threshold either risks GATT write failures (if MTU is low) or under-utilizes the link budget (if MTU is high). (Rule: R-19)
- **Line 983 [LOW]:** Inconsistent packet wrapping and checksum calculations for diagnostic/oracle helper methods. Methods such as oracleSceneQuery, oracleSceneActivate, and oracleSceneDelete return raw un-checksummed and un-wrapped bytes. The caller (DiagnosticLabOracleTab.tsx) is forced to perform wrapping and checksum calculation, violating encapsulation boundaries. (Rule: R-21)

## Verification Plan

### Automated Tests
- Run `npm run verify` to verify TSC, Jest, AST constraints, type-safety, and workflow validations.
