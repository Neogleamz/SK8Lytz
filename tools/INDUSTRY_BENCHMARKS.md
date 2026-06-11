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

### OS-Specific BLE Payload & MTU Mandates (2026-06-07)
- **Problem:** Cross-platform payload truncation and unpredictable GATT 133 errors when transmitting payloads larger than 20 bytes.
- **Analyzed Giants:** Apple CoreBluetooth limits, Android AOSP, Nanoleaf.
- **Key Findings:**
  - **Android MTU Negotiation:** Android strictly defaults to the BLE 4.0 legacy MTU (20 bytes + 3 byte header). If a payload > 20 bytes is sent without explicitly calling `requestMTU()`, Android will silently truncate it or throw a GATT 133 error. MTU negotiation is asynchronous and must block writes until resolved.
  - **iOS MTU Negotiation:** Apple strictly prohibits manual MTU requests. CoreBluetooth negotiates MTU dynamically under the hood (typically up to 185 bytes).
  - **Connection Intervals:** Apple enforces strict boundaries on connection intervals (min 15ms, max 30ms). Fast-firing payloads faster than 15ms on iOS causes the stack to drop packets at the OS level.
- **Adopted Gold Standard:** Platform-aware payload chunking. On Android, `BleConnectionManager` MUST await `requestMTU(512)` before transitioning state to `READY`. On iOS, we rely on `device.mtu` but enforce a strict 15ms inter-packet delay in `BleWriteQueue` to prevent OS-level dropping.

### OS Background Execution Mandates (2026-06-07)
- **Problem:** App termination when running background lighting patterns or reconnecting devices while the phone is locked.
- **Analyzed Giants:** Strava, Philips Hue, Tile.
- **Key Findings:**
  - **iOS Restrictions:** iOS suspends BLE background processing entirely unless `UIBackgroundModes` includes `bluetooth-central`. Even then, iOS will aggressively kill the app if memory pressure spikes. Recovery requires `State Preservation and Restoration` (`CBCentralManagerOptionRestoreIdentifierKey`), which saves the pending connection state so iOS can wake the app when the device comes in range.
  - **Android 14+ Restrictions:** Android kills background Bluetooth operations unless the app is running a bound Foreground Service of type `connectedDevice`. This requires a persistent status bar notification and explicit `<uses-permission android:name="android.permission.FOREGROUND_SERVICE_CONNECTED_DEVICE" />` in the manifest.
- **Adopted Gold Standard:** iOS State Restoration MUST be implemented in the native initialization phase. Android Foreground Services MUST be orchestrated via Expo Config Plugins to ensure permission parity and persistent notification generation during group runs.
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
  - **Auto-Reconnects:** Naive OS \ utoConnect=true\ uses slow background scanning. Use a custom state machine with **Exponential Backoff and Jitter** (1s, 2s, 4s, 8s).
  - **Group Connectivity:** Mobile OS restricts standard BLE to ~4-7 parallel connections. Without a Mesh layer, commands must use a strict fast round-robin execution queue.
  - **Command Queuing:** Android BLE is single-threaded. Firing concurrent reads/writes causes \status 133\. Must use a strict FIFO serial queue with timeout mechanisms and stale command eviction.
  - **Telemetry:** Log \status 133\ with context (payload size, operation type, RSSI) to identify environmental vs. protocol failures.
- **Adopted Gold Standard:** We are enforcing a strict FIFO Command Queue for all hardware communications, transitioning to \WRITE_TYPE_NO_RESPONSE\ for UI controls, and implementing custom exponential backoff for our AutoRecovery manager.

### OS-Specific BLE Payload & MTU Mandates (2026-06-07)
- **Problem:** Cross-platform payload truncation and unpredictable GATT 133 errors when transmitting payloads larger than 20 bytes.
- **Analyzed Giants:** Apple CoreBluetooth limits, Android AOSP, Nanoleaf.
- **Key Findings:**
  - **Android MTU Negotiation:** Android strictly defaults to the BLE 4.0 legacy MTU (20 bytes + 3 byte header). If a payload > 20 bytes is sent without explicitly calling `requestMTU()`, Android will silently truncate it or throw a GATT 133 error. MTU negotiation is asynchronous and must block writes until resolved.
  - **iOS MTU Negotiation:** Apple strictly prohibits manual MTU requests. CoreBluetooth negotiates MTU dynamically under the hood (typically up to 185 bytes).
  - **Connection Intervals:** Apple enforces strict boundaries on connection intervals (min 15ms, max 30ms). Fast-firing payloads faster than 15ms on iOS causes the stack to drop packets at the OS level.
- **Adopted Gold Standard:** Platform-aware payload chunking. On Android, `BleConnectionManager` MUST await `requestMTU(512)` before transitioning state to `READY`. On iOS, we rely on `device.mtu` but enforce a strict 15ms inter-packet delay in `BleWriteQueue` to prevent OS-level dropping.

### OS Background Execution Mandates (2026-06-07)
- **Problem:** App termination when running background lighting patterns or reconnecting devices while the phone is locked.
- **Analyzed Giants:** Strava, Philips Hue, Tile.
- **Key Findings:**
  - **iOS Restrictions:** iOS suspends BLE background processing entirely unless `UIBackgroundModes` includes `bluetooth-central`. Even then, iOS will aggressively kill the app if memory pressure spikes. Recovery requires `State Preservation and Restoration` (`CBCentralManagerOptionRestoreIdentifierKey`), which saves the pending connection state so iOS can wake the app when the device comes in range.
  - **Android 14+ Restrictions:** Android kills background Bluetooth operations unless the app is running a bound Foreground Service of type `connectedDevice`. This requires a persistent status bar notification and explicit `<uses-permission android:name="android.permission.FOREGROUND_SERVICE_CONNECTED_DEVICE" />` in the manifest.
- **Adopted Gold Standard:** iOS State Restoration MUST be implemented in the native initialization phase. Android Foreground Services MUST be orchestrated via Expo Config Plugins to ensure permission parity and persistent notification generation during group runs.

### High-Density Grouping Standards (2026-06-07)
- **Problem:** Dispatching payloads to 5+ controllers simultaneously causes immediate GATT saturation and rolling disconnects.
- **Analyzed Giants:** LIFX, Govee.
- **Key Findings:**
  - Mobile Bluetooth chips are fundamentally incapable of executing parallel writes to 5+ peripherals without a physical Mesh network (e.g., Thread, Zigbee, or Bluetooth Mesh).
  - Govee bypasses this by using a Wi-Fi sync box as a hardware hub.
  - LIFX relies on UDP broadcasts over Wi-Fi, completely avoiding BLE for group sync.
- **Adopted Gold Standard:** Since we do not use hardware hubs or Wi-Fi, we MUST enforce **Software-Side Fast Round Robin (FRR)**. `Promise.all` is strictly forbidden for group GATT operations. All group payloads must be serialized through the `GroupRepository` using a sub-10ms sequential dispatch to mimic concurrency without saturating the baseband chip.

### Hardware Mocks & Dev Sandboxes (2026-06-09)
- **Problem:** Exposing powerful developer tools (Virtual Skates, Mock Devices) locally without polluting the production binary or UI logic.
- **Analyzed Giants:** Govee, Sonos, Philips Hue
- **Key Findings:**
  - **Compile-Time Elimination:** Giants use global flags (like React Native's `__DEV__`) to physically remove sandbox code from the production compiler path.
  - **Dependency Injection:** Rather than littering components with `if (mock)`, they use DI to inject a `MockBleService` at the highest layer when the Dev Mode flag is active.
- **Adopted Gold Standard:** We will use the Metro bundler's `__DEV__` flag to strip the UI, pair it with `AsyncStorage` for the runtime flag, and use Dependency Injection at the `useBLEScanner.ts` level to yield virtual devices instead of interacting with the physical Bluetooth radio.

### Database PII Encryption (TCE vs Application-Level) (2026-06-10)
- **Problem:** Encrypting PII (MAC addresses, locations) in Supabase/PostgreSQL to prevent database-level leaks without creating operational fragility.
- **Analyzed Giants:** Supabase Official Guidelines, Strava, Standard SOC2 Compliance Patterns.
- **Key Findings:**
  - **The `pgsodium` Trap:** While Supabase offers `pgsodium` for Transparent Column Encryption (TCE), it is actively discouraged for high-volume PII due to high operational risk, trigger conflicts, and unrecoverable data if keys are mismanaged.
  - **RLS First:** The industry standard for DB security is robust Row-Level Security (`auth.uid() = user_id`) and default encryption-at-rest.
  - **Application-Level Encryption:** If data must be obscured from DB admins, giants use client-side (Application-Level) encryption (e.g., `libsodium-wrappers` or `react-native-quick-crypto`) *before* sending to the database, maintaining key control entirely outside the DB.
- **Adopted Gold Standard:** We will explicitly AVOID `pgsodium` TCE for our PII task. Instead, we will implement robust Row-Level Security and investigate Application-Level Encryption (React Native client-side) for critical PII fields before syncing to Supabase.
