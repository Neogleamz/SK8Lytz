# Implementation Plan: BLE Buffer Lockout Fix

## Overview
The deep-dive code synthesis identified a critical violation of the Evolved Rule: Surgical Buffer Overflow Defense (12-Pixel 0x59 Rule). `BleWriteDispatcher.ts` fails to enforce a minimum length constraint for 0x59 static colorful payloads. Payloads below 10 pixels cause physical controller EEPROM buffer lockouts on the 0xA3 chipset.

## Affected Files
- `src/services/BleWriteDispatcher.ts`

## Rule Violations
- Evolved Rules (SDE Closed-Loop Friction Feedback) - Rule 10: Surgical Buffer Overflow Defense.

## Proposed Changes
1. Update `BleWriteDispatcher.ts` `executeWriteToDevice` payload builder to validate payload length for 0x59 commands.
   - **Source:** `src/services/BleWriteDispatcher.ts:107`
   - **Verify:** Run isolated tests to ensure that a 5-pixel 0x59 command is padded to 12 pixels before transmission.
2. If the command is 0x59 and the pixel count is < 12, pad the payload or clamp the input to ensure minimum 12 RGB pixels are sent.
   - **Source:** `.agents/rules/agent-behavior.md` Rule 10
   - **Verify:** Ensure byte length is `(12 * 3) + header_size` minimum.

## Out of Scope
- Modifying other BLE dispatch commands (e.g., 0x40 chunked payloads or 0x74 music sync).
- Refactoring the entire BleWriteDispatcher class.

## Kanban Tags
- [Status]: `[🔥 ON DECK]`
- [Verification Status]: `[✅ VERIFIED]`
- [Layer]: `[DOMAIN_BLE_CORE]`
- [Risk]: `[H-RISK]`
- [Size]: `[Snack]`
- [Cognitive Load]: `[LOW]`
- Source of Truth: `.agents/rules/agent-behavior.md` Rule 10
