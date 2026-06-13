# Architect Scraper Control Plane & Auto-Healing Pipeline

We are extracting the background "Cultural Enrichment Daemon" entirely out of the SK8Lytz admin app and building an isolated, ultra-powerful local Control Plane. This provides deep visibility, dynamic queue unblocking, and a robust "Auto-Healing" verification pipeline.

## 1. Main Application Cleanup
We will remove the existing "Cultural Daemon Pulse" from `AdminToolsModal.tsx` and delete `CulturalDaemonMonitor.tsx`. The main mobile/web app bundle will no longer carry scraper administrative UI.

## 2. Command & Control (C&C) API
We will add `express` to `tools/scraper` and build `CCTower.ts`. This acts as the daemon's manager, exposing an API on `http://localhost:5999`:
- `GET /status`, `POST /start`, `POST /stop`
- `POST /config` (Update active states & facility types)

## 3. Database: Auto-Healing Pipeline & Metrics
We will run a schema update to support the new Validator workflow:
- Add `last_attempted_at` (TIMESTAMP)
- Add `verification_status` (ENUM: `PENDING`, `VERIFIED`, `REJECTED`)
- Add `scraper_config` table for the API to write to globally.

The **Auto-Healing Pipeline**:
Instead of searching Google for a generic name ("Community Roller Rink"), the new Puppeteer script will search exactly by `latitude, longitude` or `street_address`. It will pull the verified business name and overwrite the generic OSM skeleton data, fixing "Dirty Data".

## 4. Database: Priority Target RPC
We will rewrite `get_next_spot_to_enrich` to read the `scraper_config`. It will dynamically drop irrelevant states and sort targets based on priority (e.g., `roller_rink` first, `pro_shop` second).

## 5. Local Vite Web Dashboard
We will spin up a sleek `tools/scraper-dashboard` React app running on `http://localhost:5173`.
**Features:**
- **Hero Metrics:** "Records Processed", "Enriched", "Remaining in Queue".
- **Execution:** Force Start, Halt, Sleep Slider.
- **Targeting:** State overrides and Facility Checkboxes.
- **Reporting:** "Ghost Town" Data Grid (missing address), and "Disconnected" Data Grid (missing phone/site).
- **Graveyard Tab:** Inspect and restore `REJECTED` nodes.

## Next Steps
Waiting for execution sequence to begin!
