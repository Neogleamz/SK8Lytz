# Epic: Local AI Detective Pipeline (Ollama)

This epic replaces the brittle RegExp-based extraction in the Phase 2/3 scraper pipeline with a dynamic, local Large Language Model (Llama 3/3.1 via Ollama). It introduces a new "Detective Lab" UI to the Scraper Dashboard, giving operators absolute control over the extraction schema, prompts, and toxicity blocking, while strictly maintaining the existing Phase 1 ➔ Phase 6 workflow boundaries.

## Target Architecture

### Supabase Database Migration
- Add `ai_system_prompt` (text) to `scraper_config`.
- Add `ai_exclusion_keywords` (text array) to `scraper_config`.
- Add `ai_target_vectors` (jsonb) to `scraper_config`.
- Add `ai_metadata` (jsonb) to `skate_spots` to catch dynamic custom vectors that don't fit the rigid schema.

### CCTower API (Backend)
- Update the `/config` GET and POST routes to handle `ai_system_prompt`, `ai_exclusion_keywords`, and `ai_target_vectors`.
- Create a new `/api/sandbox` POST route that takes a URL, spins up a headless Puppeteer instance, cleans the DOM, hits the local Ollama API (`http://localhost:11434/api/generate`), and returns the raw JSON to the dashboard.

### Scraper Dashboard (Frontend)
- Build **Zone 1: Engine Room** (Config & Toxicity Keywords).
- Build **Zone 2: Schema Builder** (Dynamic Target Vectors & System Prompt IDE).
- Build **Zone 3: Sandbox Playground** (Test URL input, split-pane view for Scraped Text vs AI JSON).
- Integrate these views cleanly into a new dedicated "Detective Lab" tab.

### Indexer Daemon (The Brain)
- **Remove**: Delete all legacy regex functions (`parseHoursFromText`, `parseAdultNightSchedule`, `parseSpecialEvents`, `parsePricing`, `checkToxicity`).
- **DOM Pre-Processing**: Update the Puppeteer evaluation script to proactively `document.querySelectorAll('nav, footer, script, style, header, iframe').forEach(el => el.remove())`.
- **Dynamic Prompt Injection**: Fetch `scraper_config`. Dynamically loop over `ai_target_vectors` to build the required JSON schema structure to append to the system prompt.
- **Ollama Execution**: Send the sanitized `bodyText` and Prompt to `http://localhost:11434/api/generate` requesting `format: "json"`.
- **Parsing & Safety**: Parse the JSON. If the toxicity bouncer triggers (`is_roller_rink: false`), mark as `REJECTED`. If parsing fails or Ollama timeouts, mark as `AI_FAILED`. Otherwise, map to the DB columns (`opening_hours`, `pricing_data`, etc.) and set `is_deep_crawled = true`.
