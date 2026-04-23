---
description: Structured BLE protocol testing — read Master Reference, build hex payload, verify byte math, and log results
---

1. **Parse the command** from the user prompt (e.g., "ble lab 0x62 color write" → target command `0x62`).

2. **Read the Master Reference** — open and search `tools/SK8Lytz_App_Master_Reference.md` for the target command byte to retrieve:
   - Command structure and byte layout
   - Expected payload format (Big-Endian/Little-Endian)
   - Expected notification response format

3. **Build the hex payload** — physically write out the complete byte array in chat:
   ```
   Payload: [0x7E, 0x07, 0x05, 0x03, <R>, <G>, <B>, 0x00, 0xEF]
   ```
   Show the math for each calculated byte. Verify checksum if applicable.

4. **Cross-reference with source code** — use `grep_search` to find where this command is constructed in the codebase (likely in `src/protocols/` or `src/services/`). Verify the implementation matches the Master Reference.

5. **Report discrepancies** — if the source code and Master Reference disagree, **HALT** and flag the anomaly per the Anti-Hallucination Rule. Do not proceed until the user declares the Source of Truth.

6. **Log the test** — output a structured lab report:
   ```
   ## 🧪 BLE Lab Report
   - Command: 0x<XX>
   - Purpose: <description>
   - Payload: [<hex bytes>]
   - Source File: <file path>
   - Master Ref Match: ✅/❌
   - Notes: <any observations>
   ```

7. If the user confirms the test was successful, offer to update the Master Reference with any new findings.
