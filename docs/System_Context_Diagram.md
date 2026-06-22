# SK8Lytz System Context Diagram

> **Audience:** Product Managers, Investors, Designers, and non-technical stakeholders.
> **Purpose:** To understand what SK8Lytz is, who uses it, and what external systems it relies on to function.

## What is this?
This is a Level 1 System Context Diagram (based on the C4 Model). It treats the SK8Lytz App as a single "Black Box" in the center. It does not show code, databases, or APIs. It only shows the **Actors** (the humans using the app) and the **External Dependencies** (the hardware and cloud systems we don't build but rely on).

## System Context Diagram

```mermaid
C4Context
    title System Context Diagram for SK8Lytz App Ecosystem

    %% Actors
    Person(skater, "Skater", "A user who wants to customize their skate lights, track stats, and sync with their crew.")
    Person(crew_leader, "Crew Leader", "A skater who hosts a group session and pushes lighting sync to other members.")
    Person(admin, "Neogleamz Admin", "Manages the curated pattern catalog, monitors system health, and pushes global updates.")

    %% The System
    System(sk8lytz_app, "SK8Lytz Mobile App", "The core product. Provides UI for BLE control, pattern creation, offline caching, and group synchronization.")

    %% External Systems (Hardware)
    System_Ext(ble_skate, "Zengge/Banlanx LED Controller", "The physical hardware on the skates (e.g. 0xA3 chip). Receives BLE hex payloads to change colors.")
    System_Ext(phone_os, "Mobile OS (iOS / Android)", "Provides Bluetooth Radio, Location/GPS, and Background Execution permissions.")
    System_Ext(wearable, "Wearable Companion (Apple Watch / Wear OS)", "Syncs session state and streams real-time heart rate and calorie telemetry to the phone app.")

    %% External Systems (Cloud)
    System_Ext(supabase, "Supabase (Cloud)", "Handles user authentication, database storage for crews/spots, and telemetry/crash logging.")
    System_Ext(expo_eas, "Expo EAS", "Provides Over-The-Air (OTA) updates and handles push notifications.")

    %% External Systems (Platform Services)
    System_Ext(health, "Health Platform (Apple HealthKit / Android Health Connect)", "Provides heart rate, steps, and workout/exercise read-write for session telemetry.")
    System_Ext(maps, "Map Provider (react-native-maps)", "Renders skate spots and route maps; supplies map tiles and clustering.")

    %% Relationships
    Rel(skater, sk8lytz_app, "Customizes lights, tracks speed, views spots using")
    Rel(crew_leader, sk8lytz_app, "Creates crews, broadcasts 'Hive Mind' patterns using")
    Rel(admin, sk8lytz_app, "Monitors telemetry and curates catalog via")

    Rel(sk8lytz_app, ble_skate, "Sends RGB commands & patterns via", "Bluetooth Low Energy (BLE)")
    Rel(sk8lytz_app, wearable, "Syncs session state, pushes speed to", "WatchBridge (WCSession / DataLayer)")
    Rel(wearable, sk8lytz_app, "Streams heart rate & calories to", "WatchBridge")
    Rel(sk8lytz_app, phone_os, "Requests Bluetooth access, reads GPS data from", "Native OS APIs")
    Rel(sk8lytz_app, supabase, "Syncs profiles, crews, and telemetry to", "HTTPS / WebSocket")
    Rel(sk8lytz_app, expo_eas, "Receives OTA updates and Push Notifications from", "HTTPS")
    Rel(sk8lytz_app, health, "Reads heart rate/steps, writes workouts to", "HealthKit / Health Connect APIs")
    Rel(sk8lytz_app, maps, "Renders skate spots & routes using", "Map SDK")
```

## How to Read This Diagram

1. **The Core System (Blue):** `SK8Lytz Mobile App` is what we build. It's the central hub.
2. **The People (Dark Blue):** These are the users. The app must serve the `Skater` (individual experience), the `Crew Leader` (social/multiplayer experience), and the `Admin` (curation/support).
3. **The Dependencies (Gray):**
   - If **Supabase** goes down: Skaters can still ride and change lights (Offline-First), but they cannot sync new crews or save cloud backups.
   - If the **Mobile OS** revokes Bluetooth permissions: The app is completely paralyzed.
   - If the **BLE Skate** hardware is out of range: The app queues the command and waits for the hardware to return (Auto-Recovery).

<!-- Last Validated against Master Cartography Rebuild: 2026-06-22 (added Health Platform + Map Provider external systems per DEPENDENCY_AUDIT C4 impact) -->
