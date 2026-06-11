# SK8Lytz Architecture Decision Records (ADRs)

> **Audience:** Entire Team (Devs, Product, Management).
> **Purpose:** A historical log of *why* we made massive, expensive, or unusual technical decisions. When a new PM or engineer asks, "Why didn't we just use Firebase?", point them here.

---

## ADR-001: The Offline-First Mandate

**Date:** June 2026
**Status:** 🔒 Locked & Active

### The Context
Skaters often ride in parking garages, deep concrete skateparks, or rural trails where cell service is non-existent. Traditional apps query the cloud every time you click a button.

### The Decision
We chose to build an **Offline-First Architecture**. 
- The app must assume it has zero internet connection by default.
- All patterns, crews, and user profiles are cached permanently on the device's hard drive (`AsyncStorage` / `MMKV`).
- When a user changes a setting, we update the local cache instantly (Optimistic UI) so the app feels lightning fast, and we put the cloud update into a "Sync Queue" to execute silently in the background whenever Wi-Fi returns.

### The Trade-off (What Stakeholders Must Know)
- **Pro:** The app is incredibly fast and never shows a "Must connect to internet" error for core skate features.
- **Con:** It takes developers 30% longer to build new features because they have to write the logic twice (once for local storage, once for the cloud) and manage conflict resolution if two devices edit the same thing offline.

---

## ADR-002: Supabase over Firebase

**Date:** May 2026
**Status:** 🔒 Locked & Active

### The Context
We needed a backend database to store user profiles, telemetry (speed stats), and shared lighting patterns. Google Firebase is the industry standard for mobile apps, but it uses a NoSQL document structure.

### The Decision
We chose **Supabase**, an open-source alternative built on top of traditional PostgreSQL (a relational database).

### The Trade-off (What Stakeholders Must Know)
- **Pro:** Relational data. Skaters belong to Crews, Patterns belong to Skaters. SQL is incredibly powerful for doing complex Leaderboards and analytics (e.g., "Show me the top 10 fastest skaters in Chicago who use the Neon Pulse pattern"). Doing that in Firebase NoSQL is a nightmare.
- **Con:** Supabase's offline caching SDK is not as mature as Firebase's. We had to build our own custom offline sync engine (see ADR-001) to compensate.

---

## ADR-003: Software "Fast Round Robin" over Hardware Mesh

**Date:** June 2026
**Status:** 🔒 Locked & Active

### The Context
Crew Leaders need to press one button and instantly sync the lighting patterns on 5-10 pairs of skates around them. High-end IoT companies (like Philips Hue) solve this by using a physical "Hub" plugged into the wall, or by using a "Mesh Network" where the skates talk to each other.

### The Decision
We do not have a hardware hub, and the Zengge/Banlanx LED chips on the skates do not support Mesh Networking. We chose to build a **Software-Side Fast Round Robin (FRR)** queue. The phone connects to Skate A, sends the command, disconnects, connects to Skate B, sends the command, etc., in milliseconds.

### The Trade-off (What Stakeholders Must Know)
- **Pro:** We don't have to manufacture expensive custom hardware hubs. It works with cheap, off-the-shelf LED controllers.
- **Con:** Mobile phones are not built to maintain 10 simultaneous Bluetooth connections. The larger the Crew, the more noticeable the "lag" will be between the first person's skates changing colors and the 10th person's skates changing colors. We are physically limited by the phone's Bluetooth antenna.

---

## ADR-004: FSM Theme Mode Refactoring

**Date:** June 2026
**Status:** 🔒 Locked & Active

### The Context
The application utilizes various state indicators and UI themes. To conform to strict Finite State Machine (FSM) naming rules, the UI state should be defined via explicit string unions rather than generic Boolean toggles like `isDark`.

### The Decision
We refactored the theme state management in `ThemeContext.tsx`:
- Replaced the `isDark: boolean` state with a strict `themeMode: 'dark' | 'light'` string union.
- Exposed a computed `isDark` getter (`themeMode === 'dark'`) inside the Context to prevent breaking downstream screens and layout providers that read it.

### The Trade-off
- **Pro:** Ensures compile-time safety and alignment with the project's state machine modeling conventions.
- **Con:** Requires a sync wrapper for components expecting simple Boolean states.

---

## ADR-005: Retention of BleWriteQueue as Imperative Serialization Layer

**Date:** June 2026
**Status:** 🔒 Locked & Active

### The Context
A previous audit proposed deleting `BleWriteQueue.ts` and moving raw Bluetooth characteristic writes into XState state machine transitions.

### The Decision
We rejected the proposal and retained `BleWriteQueue.ts` as a priority FIFO write serialization queue:
- High-frequency commands (G-force updates, color arrays, heartbeat checks) require backpressure queueing and priority execution.
- Storing these raw write operations as XState states would clutter the lifecycle machine with ephemeral state transitions and lead to Android GATT buffer lockouts.

### The Trade-off
- **Pro:** Keeps the XState machine focused purely on macro-lifecycle states (Connected, Scanning, Reconnecting) while ensuring safe, serialized write queues.
- **Con:** Relies on a singleton class outside the core XState service logic.

---

## ADR-006: Dual-Callback Organic Disconnect Recovery Trigger

**Date:** June 2026
**Status:** 🔒 Locked & Active

### The Context
When a Bluetooth device drops connection organically (due to range or battery), the app must trigger auto-recovery. Initial implementations had a single callback that only logged the drop, leaving the device permanently disconnected.

### The Decision
We established a strict separation of concerns for disconnect subscriptions in `ConnectService`:
- `handleOrganicDisconnect`: Dedicated strictly to logging and telemetry dispatches.
- `onOrganicDisconnect`: Direct connection hook that dispatches `RECOVERY_START` to the BleMachine with a `DISCONNECTING` guard.

### The Trade-off
- **Pro:** Fully automated, reliable background reconnection for dropped devices without locking the UI.
- **Con:** Maintains two concurrent callbacks for a single event, which must be carefully unmounted to prevent memory leaks.

---

## ADR-007: XState v5 Migration

**Date:** June 2026
**Status:** 🔒 Locked & Active

### The Context
The application's core logic (BLE connection lifecycle, session tracking) was originally managed via disparate React hooks and boolean flags (`isConnecting`, `isScanning`, etc.). This led to race conditions, overlapping effects, and 'impossible' UI states (e.g., scanning while already connected).

### The Decision
We migrated the entire core architecture to **XState v5**. All lifecycle logic was extracted out of React components into strict, deterministic Finite State Machines (`BleMachine`, `sessionMachine`).

### The Trade-off
- **Pro:** Mathematical certainty against impossible states. State transitions are predictable, testable, and completely decoupled from React's render cycle.
- **Con:** Steep learning curve. Boilerplate increases, and engineers must think in statecharts rather than imperative boolean toggles.

---

## ADR-008: BLE Payload Queue Isolation

**Date:** June 2026
**Status:** 🔒 Locked & Active

### The Context
Simultaneously writing complex color arrays, handling organic disconnects, and dispatching high-frequency telemetry (G-force/speed) was causing severe Android GATT buffer lockouts (Error 133). 

### The Decision
We implemented a strict **BLE Payload Queue Isolation** pattern. The state machine (ADR-007) is strictly forbidden from directly calling BLE characteristic writes. Instead, the machine fires events to an isolated, imperative priority queue that manages backpressure, enforces 12-pixel minimums (Rule 10), and handles serialization.

### The Trade-off
- **Pro:** Complete eradication of buffer overflows and physical controller EEPROM lockouts.
- **Con:** Introduces a singleton imperative queue outside the declarative state machine boundaries, creating a hybrid architectural pattern.

---

## ADR-009: WatchBridge Telemetry Sync Protocol

**Date:** June 2026
**Status:** 🔒 Locked & Active

### The Context
The companion watch application needs to sync bi-directional commands (Start/Stop) and stream high-frequency health telemetry (Heart Rate, Calories) back to the phone, even when the phone app is in the background or screen-locked.

### The Decision
We implemented the **WatchBridge Telemetry Sync Protocol** using native OS data layers (WCSession on iOS, DataClient on Android). The phone acts as the Source of Truth via the `sessionMachine`. The watch streams telemetry which is ingested into a ref-backed cache to prevent React re-render thrashing, and only flushed to the UI on a 1-second interval tick.

### The Trade-off
- **Pro:** Ensures reliable background sync and maintains 60fps UI performance despite high-frequency incoming BLE health streams.
- **Con:** Requires maintaining complex native Swift/Kotlin bridging code, and the UI state is slightly eventual-consistent (max 1s delay).
