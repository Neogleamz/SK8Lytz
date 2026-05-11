# 🚨 TRIPWIRE: The Hardware Layer (BLE)

> **CRITICAL AI CONTEXT**: You have entered the BLE Host Connection Domain. 
> You are strictly **FORBIDDEN** from performing unsolicited architectural refactors here.

## The Co-Location Law & Architecture
Before you modify anything related to BLE connections, MTU chunking (`0x40`), or Auto-Recovery:
1. Execute `grep_search` or `view_file` on `tools/SK8Lytz_App_Master_Reference.md`.
2. Read the sections on the "Hollow Shell" pattern and the Co-Location Law.
3. In your Implementation Plan, include a `# Cited Truth` section quoting the exact architectural rule from the Master Reference that justifies your change.

If your proposed change violates the Master Reference (e.g., trying to move `writeToDevice` out of `DashboardScreen`), you must HALT and ask the user for an override. **DO NOT GUESS.**
