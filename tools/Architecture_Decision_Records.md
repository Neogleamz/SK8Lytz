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
