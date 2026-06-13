# Implementation Plan: BATCH:sweep-services-ble

## Proposed Changes

### Domain: services-ble

#### [MODIFY] src/services/ble/BleMachine.ts
- Line 250 (R-08): any cast when mapping connected devices

#### [MODIFY] src/services/ble/BleMachine.types.ts
- Line 13 (R-08): adapterMapRef and handleNotification parameters typed as any

#### [MODIFY] src/services/ble/ConnectService.ts
- Line 208 (R-03): MTU negotiation retry loop lacks jitter and exponential backoff, using static MTU_RETRY_SETTLE_MS.
- Line 178 (R-04): Error logged without payload_size or ssi context
- Line 277 (R-04): Error logged without payload_size or ssi context
- Line 306 (R-04): Error logged without payload_size or ssi context
- Line 178 (R-04): Error logged without payload_size or ssi context
- Line 277 (R-04): Error logged without payload_size or ssi context
- Line 306 (R-04): Error logged without payload_size or ssi context

#### [MODIFY] src/services/ble/InterrogatorService.ts
- Line 148 (R-09): MAC address is interpolated directly into the AppLogger string message, bypassing the PII scrubber.

#### [MODIFY] src/services/ble/HeartbeatService.ts
- Line 30 (R-10): Sequential group writes during heartbeat instead of concurrent mapped writes
