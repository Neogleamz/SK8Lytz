# SK8Lytz Music Integration — Master Implementation Plan
_Status: Recovered from Corporate Memory | Phase: Research & Architectural Draft_

## 🎯 End Goal

Transform the SK8Lytz Music tab from a microphone-reactive visualizer into a **full music intelligence layer** that:

1. Knows **what song is playing** — from your phone or at the rink
2. Knows the **exact BPM** of that song from verified music databases
3. **Maps music properties** (BPM, energy, mood) to LED behavior in real time
4. Lets the entire crew **pulse in sync** on every beat — driven by the leader's music
5. Works when **headphones/BT speakers** are in use (mic hears room noise, not your music)
6. Works at the **rink** — continuous "Live Rink Mode" detects what the DJ is playing every 30–60s

---

## ✅ Phased Roadmap

### Phase 1 — Spotify Sync (Local Playback)
- **Tech**: `react-native-spotify-remote`, Spotify Web API.
- **Features**: OAuth2 Login, BPM/Energy/Valence extraction, Album Art color extraction.
- **Logic**: Maps `BPM` to flash speed and `Valence` (mood) to color warmth.

### Phase 2 — Android Cross-App Detection (MediaSession)
- **Tech**: Expo Native Android Module wrapping `MediaController`.
- **Features**: Detect metadata from ANY app (YouTube Music, Amazon, Pandora, etc.).
- **BPM Source**: GetSongBPM API lookup based on Track + Artist.

### Phase 3 — Live Rink Mode (ShazamKit + ACRCloud)
- **The Experience**: Tap "Live Rink Mode." Phone listens for 8s, identifies the DJ track, and syncs the crew instantly.
- **Logic**: Background rescans every 45s to handle track changes automatically.

### Phase 4 — Apple Music & Extended Support
- **Tech**: MusicKit for exact Apple-sourced BPM.

### Phase 5 — BPM Choreography Engine & Crew Party Sync
- **The Engine**: A drift-corrected scheduler for perfect beat-alignment.
- **7 Sub-Modes**: Beat Flash, Color Cycle, Pattern Jump, Strobe, Bass Pulse, Color Sweep, Alternating.
- **Crew Sync**: Leader's BPM/Phase broadcast via Supabase Realtime for zero-delay hardware sync.

---

## UI Components
- **Now Playing Banner**: Album art, title, artist, live BPM badge.
- **Choreography Tab**: Live BPM dial, beat visualizer, 7-mode selector chips.
- **Rink Mode Overlay**: Waveform animation during scan + Countdown timer for next scan.
