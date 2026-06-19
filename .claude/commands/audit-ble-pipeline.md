# /audit-ble-pipeline — BLE Pipeline Audit Engine

**Description:** Perform a read-only semantic audit of the SK8Lytz BLE XState pipeline using 9 parallel Mapper agents.
**Persona:** 🕵️ Scout — Reyes

> **📍 WHEN TO USE:** When you want to deeply verify the correctness of the BLE XState machine and its surrounding services, ensuring no architecture violations, `any` casts, or missed state handlers have crept in.

> **🕵️ Scout — Reyes | BLE Audit Active**
> *Reyes verifies truth. No codebase changes are made during this audit. Everything is mapped and written to disk.*

---

### Step 0 — Reyes Knowledge-First (MANDATORY, NO SKIP)
Check if this audit was already run recently.
Read `docs/SESSION_LOG.md` — search for `[EVENT]` entries mentioning `BLE XState Pipeline Audit Completed`. If found recently, warn the user before proceeding to avoid redundant cycles.

---

### Step 1 — The Map-Reduce Launch
When the user asks to "audit the BLE pipeline", execute this sweep by leveraging parallel sub-agents.

1. Ensure the directory `tools/BLE_AUDIT_2/` exists (create a dummy `README.md` if needed).
2. Spawn 9 parallel sub-agents. Assign them the following specific roles to read files and write their findings to disk.

**Agent 1 — Role: "BleMachine Auditor"**
Read in full `src/services/ble/BleMachine.ts` and `src/services/ble/BleMachine.types.ts`. Write findings to `tools/BLE_AUDIT_2/01_bleMachine.md`.
Answer:
1. List every state in the machine (IDLE, SCANNING, CONNECTING, READY, DISCONNECTING, RECOVERING).
2. For each state: what actors are invoked? What events does it accept? What transitions exist?
3. Does READY invoke heartbeatService? Does CONNECTING invoke connectService? Does RECOVERING invoke recoveryService?
4. Is rssiService or interrogatorService invoked anywhere in the machine?
5. Any `any` casts?
6. Does the context contain bleManager, scanCallback, adapterMapRef, mtuMapRef, disconnectListeners, blacklistedMacsRef, handleOrganicDisconnect, handleNotification, enqueueWrite?
7. Is SCAN_PAUSE / SCAN_RESUME handled? What happens on FORCE_IDLE?

**Agent 2 — Role: "ConnectService Auditor"**
Read `src/services/ble/ConnectService.ts`. Write to `tools/BLE_AUDIT_2/02_connectService.md`.
Answer: Actor type, bleManager.connectToDevice() vs connectToDeviceById(), group connect handling, GATT notification registration, protocol adapter mapping (adapterMapRef), MTU Negotiation (mtuMapRef), success/failure flows, any `any` casts.

**Agent 3 — Role: "RecoveryService Auditor"**
Read `src/services/ble/RecoveryService.ts`. Write to `tools/BLE_AUDIT_2/03_recoveryService.md`.
Answer: Actor type, recovery attempts count and backoff delays, reconnect mechanism, exhaustion/success events, GATT re-registration logic, protocol adapter re-mapping, write queue clearing, any `any` casts.

**Agent 4 — Role: "HeartbeatService Auditor"**
Read `src/services/ble/HeartbeatService.ts`. Write to `tools/BLE_AUDIT_2/04_heartbeatService.md`.
Answer: Actor type, heartbeat interval, payload/opcode, failure detection, failure event, enqueueWrite vs direct write, cleanup mechanism, simultaneous device handling, any `any` casts.

**Agent 5 — Role: "RSSIService Auditor"**
Read `src/services/ble/RSSIService.ts` and `src/hooks/ble/useBLERSSIMonitor.ts`. Write to `tools/BLE_AUDIT_2/05_rssiService.md`.
Answer: React hook imports, structural interface vs direct BleManager import, poll interval, cleanup function return, polling logic ownership, stale MAC pruning, onCriticalSignal RECOVERY_START wiring, any `any` casts.

**Agent 6 — Role: "InterrogatorService Auditor"**
Read `src/services/ble/InterrogatorService.ts` and `src/hooks/ble/useBLEInterrogator.ts`. Write to `tools/BLE_AUDIT_2/06_interrogatorService.md`.
Answer: React hooks imported, cancelDeviceConnection in finally block, enqueueWrite for 0x63 query, AsyncStorage parse error handling, FTUE delay vs standard delay, hwCacheRef syncing, any `any` casts.

**Agent 7 — Role: "Scanner Auditor"**
Read `src/hooks/ble/useBLEScanner.ts` and `src/hooks/ble/useBLEBatterySweep.ts`. Write to `tools/BLE_AUDIT_2/07_scanner.md`.
Answer: bleManager.startDeviceScan() direct calls, SCAN_STOP events, THROTTLED battery duty-cycle logic, Android scan budget handling, scanCallback wiring, any `any` casts.

**Agent 8 — Role: "Write Pipeline Auditor"**
Read `src/services/BleWriteQueue.ts` and `src/services/BleWriteDispatcher.ts`. Write to `tools/BLE_AUDIT_2/08_writePipeline.md`.
Answer: 3 priority tiers and opcode mapping, MAX_QUEUE_DEPTH and drop strategy, _drain() sequential 1-at-a-time gate, transient GATT error retries, clearWriteQueue promise resolution, stale-write pruning, enqueueWrite routing exclusively, any `any` casts.

**Agent 9 — Role: "useBLE Thin Orchestrator Auditor"**
Read `src/hooks/useBLE.ts` and `src/context/BLEContext.tsx`. Write to `tools/BLE_AUDIT_2/09_useBLE.md`.
Answer: useMachine input fields correctness, scanCallback ref-forwarding, direct bleManager start/stop scan calls, connectToDevices GATT handling, disconnectFromDevice GATT handling, forceDisconnect GATT handling, handleOrganicDisconnect machine delegation, sweeper control exposure, any `any` casts (non Platform.OS='web'), useBLE.ts line count.

---

### Step 3 — Await & Synthesize
- Wait for all 9 agents to complete and report back.
- Generate `tools/BLE_AUDIT_2/00_INDEX.md` containing a table of the files generated, their sizes, a timestamp, and the `git rev-parse HEAD` output.
- Print a completion summary to the user.
- Add an `[EVENT]` entry to `docs/SESSION_LOG.md` detailing the successful audit.
