# Industry Benchmarks & Gold Standards

> **The Giant-First Rule**: We do not build blindly. Before architecting any major feature, we research how the top 5 companies in that domain solved the problem. This ledger permanently stores those findings so the team never has to re-derive world-class patterns.

## 🗂️ Benchmark Vault

*(This vault is currently empty. The next time the `/intake` workflow is run, Scout — Reyes will automatically run a 5-company competitive analysis using `search_web` and log the findings here before proceeding.)*

---

### Format Template for New Entries

```markdown
### [Feature/Domain Name] (Date)
- **Problem:** [What were we trying to solve?]
- **Analyzed Giants:** [Company A], [Company B], [Company C], [Company D], [Company E]
- **Key Findings:**
  - **[Company A]:** [How they do it]
  - **[Company B]:** [How they do it]
- **Adopted Gold Standard:** [Which approach we are adopting and why]
```

### BLE Connectivity Architecture & Best Practices (2026-06-07)
- **Problem:** Resolving BLE dropping, group connectivity limitations, auto-reconnect failures, and GATT buffer saturation.
- **Analyzed Giants:** Govee, LIFX, Philips Hue, Nanoleaf
- **Key Findings:**
  - **Payload Delivery:** Use \WRITE_TYPE_NO_RESPONSE\ (Fire-and-Forget) for rapid UI interactions to prevent EEPROM locking. Acknowledged writes are strictly for critical configs.
  - **Auto-Reconnects:** Naive OS \utoConnect=true\ uses slow background scanning. Use a custom state machine with **Exponential Backoff and Jitter** (1s, 2s, 4s, 8s).
  - **Group Connectivity:** Mobile OS restricts standard BLE to ~4-7 parallel connections. Without a Mesh layer, commands must use a strict fast round-robin execution queue.
  - **Command Queuing:** Android BLE is single-threaded. Firing concurrent reads/writes causes \status 133\. Must use a strict FIFO serial queue with timeout mechanisms and stale command eviction.
  - **Telemetry:** Log \status 133\ with context (payload size, operation type, RSSI) to identify environmental vs. protocol failures.
- **Adopted Gold Standard:** We are enforcing a strict FIFO Command Queue for all hardware communications, transitioning to \WRITE_TYPE_NO_RESPONSE\ for UI controls, and implementing custom exponential backoff for our AutoRecovery manager.

