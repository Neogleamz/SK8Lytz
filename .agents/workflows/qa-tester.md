---
description: QA Edge-Case Hunter — 5-point structured checklist for BLE app hardening before commit
---

# QA Edge-Case Hunter — "/qa-tester"

When invoked via `/qa-tester` or triggered during `/start-task` Phase 5, adopt the QA Engineer persona and hunt for 5 weird, rare edge cases in the code you just wrote. This is a **mandatory pre-commit gate**.

For EACH of the 5 case categories below, explicitly state:
- 🔍 **What could go wrong** — one specific failure scenario for THIS task's code
- ✅ **How the code handles it** — cite the exact line/function that defends against it
- ⚠️ **If unhandled** — mark it as a TODO and flag it before committing

---

### Case 1: The BLE Drop / Connection Loss
What happens if the Bluetooth connection drops mid-operation?
- Does the code handle `DEVICE_DISCONNECTED` state while a write/read is in flight?
- Does it clean up locks, timers, and subscriptions properly?
- Does it trigger auto-recovery or just silently fail?

### Case 2: App Backgrounding / Foreground Resume
What happens if the user backgrounds the app mid-operation?
- Does the hook clean up properly in `useEffect` return / `AppState` change?
- Does it resume correctly when foregrounded?
- iOS: Does it respect background BLE limitations (UUID filter requirement)?

### Case 3: Null / Undefined State Race Condition
What happens if this feature renders before its async data is ready?
- Is there a loading state? An empty state?
- Can `undefined` or `null` propagate to a crash (`.map()` on null, optional chain missing)?
- Does the UI show a skeleton/empty state or crash?

### Case 4: Concurrent Writes / Double-Tap
What happens if the user triggers this action twice rapidly?
- Does the GATT mutex protect against simultaneous writes?
- Is there a debounce or `isProcessing` guard?
- Can the write queue handle burst inputs without reordering?

### Case 5: Task-Specific Edge Case
Identify ONE edge case unique to THIS task's domain:
- **[LAB]** tasks: What happens at the hardware boundary? (MTU overflow, EEPROM buffer, opcode timeout)
- **[UI]** tasks: What happens on a tiny screen (SE) or in landscape? Font scaling? Safe area?
- **[CORE]** tasks: What happens with zero devices, 5+ devices, or a device with corrupted state?
- **[CLOUD]** tasks: What happens when Supabase is offline? Is optimistic UI applied?

---

### Output Format

After evaluating all 5 cases, output this table in chat:

```
## 🔬 QA Edge-Case Report

| # | Case | Status | Notes |
|---|------|--------|-------|
| 1 | BLE Drop | ✅ Handled / ⚠️ Gap | <brief explanation> |
| 2 | Backgrounding | ✅ Handled / ⚠️ Gap | <brief explanation> |
| 3 | Null Race | ✅ Handled / ⚠️ Gap | <brief explanation> |
| 4 | Concurrent Write | ✅ Handled / ⚠️ Gap | <brief explanation> |
| 5 | Domain-Specific | ✅ Handled / ⚠️ Gap | <brief explanation> |

**QA Verdict: PASS ✅ / NEEDS FIX ⚠️**
```

- If ALL 5 are ✅: Proceed to Phase 5.5 (Documentation Parity Check).
- If ANY are ⚠️ Gap: Fix the gap BEFORE committing. Do not skip.
