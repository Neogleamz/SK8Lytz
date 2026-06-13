# EPIC: The "DJ Override" Hardware Sync (Tier 3)
**Status:** ❄️ ICEBOX
**Author:** Architect
**Goal:** Enable a massive, synchronized light show across an entire roller rink by allowing a central "DJ" or automated system to temporarily hijack the BLE hardware state of all skaters who check into the venue.

## ⚠️ Architectural Risks & Constraints
1. **Bluetooth Storm (The Latency Risk):**
   If 100 skaters are connected to the same Supabase Realtime channel, and the DJ sends a single color update, 100 smartphones will simultaneously attempt to blast a BLE write to their respective skate controllers. This is safe because each phone only talks to its own skates, but we must ensure the React Native UI thread doesn't choke on processing the Realtime payload.
2. **Opt-in Privacy:**
   Yielding control of physical hardware to a 3rd party is a massive security boundary. Users MUST explicitly tap "Join Rink Feed & Sync Lights". If they don't opt-in, they only get location tagging (Tier 1), not hardware sync.
3. **The Disconnect Deadlock:**
   If the DJ sets the room to "Strobe Red", and a user walks out the door and loses WiFi/Cell service, their skates might get stuck in Strobe Red. The app MUST have a failsafe timeout: if the user's GPS leaves the geofence, or the channel drops, `useBLE` automatically reverts to the skater's last personal preset.

## Proposed Implementation Plan

### 1. The Rink Dashboard (Venue Side)
- Create a web-only or admin-only "Cockpit" dashboard for Rink Managers.
- The UI contains macro buttons: "Couples Skate (Blue/Pink)", "Wipeout (Flashing Red)", "Neon Party (Pulse Green)".
- When clicked, it publishes a standardized payload to Supabase Realtime:
  ```json
  { "channel": "rink_sync_<venue_id>", "action": "set_color", "hex": "#FF0080", "mode": "BREATH", "duration_ms": 300000 }
  ```

### 2. The Skater Payload Receiver (Client Side)
- Modify `useBLE` or `PatternEngine.ts` to expose an `overrideState` capability.
- When the user taps the banner to join the rink, initialize a Supabase Realtime listener.
- **The Yield Lock:** When a payload is received, `PatternEngine.ts` pauses all local user inputs (disabling the color wheel) and forwards the DJ's hex math directly to the `ZenggeProtocol` queue.

### 3. Failsafes & Exit Strategies
- **Geofence Break:** Constantly poll location in the background (or using `expo-location` geofence). If `ST_Distance(user, rink) > 200m`, immediately trigger `disconnectRinkSync()` and restore the local hardware state.
- **User Override:** Add a massive "Leave Sync" button to the Dashboard so a skater can instantly reclaim their hardware.

## Required Setup
- Define the payload schemas for `RinkSyncEvent`.
- No new OS permissions needed, purely Supabase Realtime + existing BLE.
