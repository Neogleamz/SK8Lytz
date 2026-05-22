### evolved rules
- **Rule: Surgical Buffer Overflow Defense**: The agent must enforce a minimum length of 12 RGB pixels for all `0x59` Static Colorful payload dispatches. Payloads below 10 pixels cause physical controller EEPROM buffer lockouts on the `0xA3` chipset.
