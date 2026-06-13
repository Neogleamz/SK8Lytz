---
description: Perform a read-only semantic audit of the SK8Lytz BLE XState pipeline using 9 parallel Mapper agents.
persona_entry: "🕵️ Scout — Reyes"
team_roster: .agents/team-roster.md
---

# The BLE Pipeline Audit Engine

> **📍 WHEN TO USE:** When you want to deeply verify the correctness of the BLE XState machine and its surrounding services, ensuring no architecture violations, `any` casts, or missed state handlers have crept in.

> **🕵️ Scout — Reyes | BLE Audit Active**
> *Reyes verifies truth. No codebase changes are made during this audit. Everything is mapped and written to disk.*

---

### ⚡ Step 0 — Reyes Knowledge-First (MANDATORY, NO SKIP)
Check if this audit was already run recently.
Read `docs/SESSION_LOG.md` — search for `[EVENT]` entries mentioning `BLE XState Pipeline Audit Completed`. If found recently, warn the user before proceeding to avoid redundant cycles.

---

### ⚡ Step 1 — The Map-Reduce Launch
When the user asks to "audit the BLE pipeline", execute this sweep by leveraging the Sub-Agent Swarm Protocol (`invoke_subagent`).

1. Ensure the directory `tools/BLE_AUDIT_2/` exists (create a dummy `README.md` via `write_to_file` if needed).
2. Spawn 9 parallel sub-agents with `Workspace: "inherit"` and `TypeName: "self"`. Assign them the following specific roles and prompts to read files in full via `view_file` and write their findings to disk. 

*Note: The subagents must be instructed to set `IsArtifact: false` when using `write_to_file` for these findings.*

**Agent 1 — Role: "BleMachine Auditor"**
Prompt:
Read in full `src/services/ble/BleMachine.ts` and `src/services/ble/BleMachine.types.ts`. Write findings to `tools/BLE_AUDIT_2/01_bleMachine.md`.
Answer:
1. List every state in the machine (IDLE, SCANNING, CONNECTING, READY, DISCONNECTING, RECOVERING).
2. For each state: what actors are invoked? What events does it accept? What transitions exist?
3. Does READY invoke heartbeatService?
4. Does CONNECTING invoke connectService?
5. Does RECOVERING invoke recoveryService?
6. Is rssiService or interrogatorService invoked anywhere in the machine?
7. Any `any` casts?
8. Does the context contain bleManager, scanCallback, adapterMapRef, mtuMapRef, disconnectListeners, blacklistedMacsRef, handleOrganicDisconnect, handleNotification, enqueueWrite?
9. Is SCAN_PAUSE / SCAN_RESUME handled? Which states?
10. What happens on FORCE_IDLE? Does it clear connectedDevices?

**Agent 2 — Role: "ConnectService Auditor"**
Prompt:
Read `src/services/ble/ConnectService.ts`. Write to `tools/BLE_AUDIT_2/02_connectService.md`.
Answer:
1. What XState actor type does it use?
2. Does it call bleManager.connectToDevice() or bleManager.connectToDeviceById()?
3. Group connect handling? Sequential or parallel?
4. Service/Characteristic discovery logic?
5. GATT notification registration method?
6. Protocol adapter mapping (adapterMapRef)?
7. MTU Negotiation (mtuMapRef)?
8. Success transmission payload?
9. Failure/cleanup flow?
10. Any `any` casts?

**Agent 3 — Role: "RecoveryService Auditor"**
Prompt:
Read `src/services/ble/RecoveryService.ts`. Write to `tools/BLE_AUDIT_2/03_recoveryService.md`.
Answer:
1. What actor type?
2. Recovery attempts count and backoff delays?
3. JitteredDelay vs fixed?
4. Reconnect mechanism (bleManager or event)?
5. Exhaustion event?
6. Success event?
7. GATT re-registration logic?
8. Protocol adapter re-mapping?
9. Write queue clearing?
10. Any `any` casts?

**Agent 4 — Role: "HeartbeatService Auditor"**
Prompt:
Read `src/services/ble/HeartbeatService.ts`. Write to `tools/BLE_AUDIT_2/04_heartbeatService.md`.
Answer:
1. Actor type?
2. Heartbeat interval?
3. Payload sent/opcode?
4. Failure detection method?
5. Failure event?
6. enqueueWrite vs direct write?
7. Cleanup mechanism?
8. Simultaneous device handling?
9. Any `any` casts?

**Agent 5 — Role: "RSSIService Auditor"**
Prompt:
Read `src/services/ble/RSSIService.ts` and `src/hooks/ble/useBLERSSIMonitor.ts`. Write to `tools/BLE_AUDIT_2/05_rssiService.md`.
Answer:
1. React hook imports in RSSIService?
2. Structural interface vs direct BleManager import?
3. Poll interval?
4. Cleanup function return?
5. Polling logic ownership?
6. useBLERSSIMonitor re-exports?
7. Stale MAC pruning?
8. onCriticalSignal RECOVERY_START wiring?
9. Any `any` casts?

**Agent 6 — Role: "InterrogatorService Auditor"**
Prompt:
Read `src/services/ble/InterrogatorService.ts` and `src/hooks/ble/useBLEInterrogator.ts`. Write to `tools/BLE_AUDIT_2/06_interrogatorService.md`.
Answer:
1. React hooks imported?
2. cancelDeviceConnection in finally block?
3. enqueueWrite for 0x63 query?
4. AsyncStorage parse error handling?
5. FTUE delay vs standard delay?
6. Logic delegation ownership?
7. hwCacheRef syncing?
8. Memoization of exported callbacks?
9. Any `any` casts?

**Agent 7 — Role: "Scanner Auditor"**
Prompt:
Read `src/hooks/ble/useBLEScanner.ts` and `src/hooks/ble/useBLEBatterySweep.ts`. Write to `tools/BLE_AUDIT_2/07_scanner.md`.
Answer:
1. bleManager.startDeviceScan() direct calls?
2. SCAN_STOP events?
3. THROTTLED battery duty-cycle logic?
4. Android scan budget handling?
5. scanCallback wiring?
6. scanForPeripherals non-sweeper paths?
7. useBLEInterrogator imports?
8. useBLEBatterySweep imports?
9. Any `any` casts?

**Agent 8 — Role: "Write Pipeline Auditor"**
Prompt:
Read `src/services/BleWriteQueue.ts` and `src/services/BleWriteDispatcher.ts`. Write to `tools/BLE_AUDIT_2/08_writePipeline.md`.
Answer:
1. 3 priority tiers and opcode mapping?
2. MAX_QUEUE_DEPTH and drop strategy?
3. _drain() sequential 1-at-a-time gate?
4. Transient GATT error retries?
5. clearWriteQueue promise resolution?
6. Stale-write pruning via generations?
7. enqueueWrite routing exclusively?
8. resolveWritePriority usage?
9. Imports tracking?
10. Any `any` casts?

**Agent 9 — Role: "useBLE Thin Orchestrator Auditor"**
Prompt:
Read `src/hooks/useBLE.ts` and `src/context/BLEContext.tsx`. Write to `tools/BLE_AUDIT_2/09_useBLE.md`.
Answer:
1. useMachine input fields correctness?
2. scanCallback ref-forwarding?
3. Direct bleManager start/stop scan calls?
4. connectToDevices GATT handling?
5. disconnectFromDevice GATT handling?
6. forceDisconnect GATT handling?
7. handleOrganicDisconnect machine delegation?
8. Sweeper control exposure?
9. BluetoothLowEnergyApi interface provision?
10. scanForPeripherals delegation?
11. `any` casts (non Platform.OS='web')?
12. useBLE.ts line count?

---

### ⚡ Step 3 — Await & Synthesize
- Wait for all 9 agents to complete and report back via messages.
- Generate `tools/BLE_AUDIT_2/00_INDEX.md` containing a table of the files generated, their sizes, a timestamp, and the `git rev-parse HEAD` output.
- Print a completion summary to the user.
- Add an `[EVENT]` entry to `docs/SESSION_LOG.md` detailing the successful audit.
