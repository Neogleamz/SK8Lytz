# SK8Lytz Scraper Pipeline Bible

> **STATUS**: Living document. Last updated 2026-05-24.
> **RULE**: Any agent MUST read this file COMPLETELY before modifying scraper code. No exceptions.

---

## §1 — Architecture Overview

The scraper is a **4-phase conveyor belt pipeline** that discovers roller rinks nationwide, extracts their data, photographs them, and publishes them to Supabase.

```
Phase 1 (Scout) → Phase 2 (Detective) → Phase 3 (Photographer) → Phase 4 (Publisher)
   SEEDED     →    DEEP_CRAWLED      →     MEDIA_READY        →    PUBLISHED
```

Each phase is an independent daemon process managed by CCTower (the master HTTP API server). Phases communicate via the SQLite database — the `verification_status` column is the pipeline's state machine.

### Verification Status Flow
```
NULL/PENDING → PENDING_WEBSITE → SEEDED → DEEP_CRAWLED → MEDIA_READY → PUBLISHED
                    ↓                ↑ restart              ↓
             WEBSITE_STALLED    (via /restart)          REJECTED  ← TOXICITY_ABORT or guillotine
                                                        ON_HOLD   ← manually frozen
                                                        STALLED   ← retry exhausted
```

---

## §2 — File Map

### Core Engine Files (`tools/scraper/core/`)
| File | ~Size | Purpose |
|------|-------|---------|
| `LocalDB.ts` | ~60KB | SQLite schema, migrations, CRUD, config, field registry, DPPOS tier seeding, DEFAULT_EXCLUSION_KEYWORDS |
| `DetectiveEngine.ts` | ~95KB | AI extraction engine: Puppeteer, Sitemap, 7-pass LLM schema, pre-LLM toxicity bouncer, HeuristicsEngine |
| `SitemapParser.ts` | ~20KB | Sitemap/robots.txt parser, URL classification |
| `WebsiteResolver.ts` | ~10KB | Google search fallback for spots missing websites |
| `HeuristicsEngine.ts` | ~6KB | Regex-based extraction (phone, email, hours) before LLM |
| `MigrationEngine.ts` | ~3KB | DB schema migration runner |

### Daemon Processes (`tools/scraper/`)
| File | Phase | Status Consumed | Status Produced |
|------|-------|----------------|-----------------|
| `GoogleSweep.ts` | 1 (Scout) | — | `SEEDED` or `PENDING_WEBSITE` |
| `WebsiteResolverDaemon.ts` | 1.5 | `PENDING_WEBSITE` | `SEEDED` or `WEBSITE_STALLED` |
| `Indexer.ts` | 2 (Detective) | `SEEDED` | `DEEP_CRAWLED`, `STALLED`, or `REJECTED` |
| `Photographer.ts` | 3 | `DEEP_CRAWLED` | `MEDIA_READY` |
| `Publisher.ts` | 4 | `MEDIA_READY` | `PUBLISHED` or `REJECTED` |
| `CCTower.ts` | Master | — | Manages all daemons, serves API |

### Dashboard (`tools/scraper-dashboard/src/components/`)
| File | Purpose |
|------|---------|
| `ScraperPipeline.tsx` | Main pipeline orchestrator, state management, SSE log stream |
| `BeltNode.tsx` | Per-phase conveyor belt UI with machine, queues, cards |
| `PhaseControlDrawer.tsx` | Config drawer per phase (AI vectors, sandbox, registry, Guillotine UI) |
| `DatabankCard.tsx` | Spot detail card (polaroid/expanded views) |

---

## §3 — Database Schema

### `local_spots` (Main Table)
The single source of truth for all spot data. ~60 columns covering identity, enrichment, photos, and pipeline state.

**Phase 1 Seeded Fields** (from Google Places API — NOT detective-extracted):
- `name`, `lat`, `lng`, `city`, `state`, `zip`, `street_address`
- `phone_number`, `website`, `google_place_id`, `google_maps_url`
- `business_status`, `rating`, `user_ratings_total`
- `candidate_links` (JSON: discovered URLs)

**Phase 2 Detective-Extracted Fields** (from LLM + regex + OCR):
- Session Hours (Pass 1A): `opening_hours`
- Pricing & Fees (Pass 1B): `pricing_data`, `has_fee`, `has_rental`, `price_range`
- Adult Night (Pass 1C): `has_adult_night`, `adult_night_schedule`, `adult_night_details`
- Floor & Vibe (Pass 2A): `surface_type`, `surface_quality`, `vibe_score`
- Amenities (Pass 2B): `is_indoor`, `has_pro_shop`, `has_food`, `has_lights`, `has_lockers`, `has_ac`, `has_wifi`, `has_toilets`, `capacity`
- Identity & Culture (Pass 2C): `operator_name`, `operator_description`, `is_wheelchair_accessible`, `hosts_derby`, `special_events`, `cultural_metadata`
- Contacts & Socials (Pass 2D): `email_addresses`, `instagram_url`, `facebook_url`, `tiktok_url`, `schedule_url`, `yelp_url`, `logo_url`

**Phase 3 Photographer Fields**:
- `candidate_photos` (JSON), `photos` (JSON), `logo_url`, `cover_photo_url`

**Pipeline State Fields**:
- `verification_status`, `is_published`, `is_deep_crawled`, `is_verified`
- `last_attempted_at`, `last_enriched_at`, `retry_count`
- `field_confidence` (JSON: `{ field_name: { source, confidence, extracted_at } }`)
- `ai_metadata` (JSON: `{ TOXICITY_ABORT: bool, rejection_reason: string, quality_note: string }`)
- `sync_required` (auto-set by trigger on any update)

### `scraper_config` (Global Configuration)
Single row (id=1) containing a `config_json` TEXT blob. Key properties:

```json
{
  "scout_search_queries": ["roller rink", "..."],
  "target_facilities": ["roller_rink", "skatepark", "hybrid"],
  "state_override": ["OH", "KY"],
  "detective_model": "Llama3.2-8b",
  "detective_confidence_min": 80,
  "detective_temperature": 0.1,
  "ai_system_prompt": "You are an elite data extraction agent...",
  "ai_target_vectors": [
    { "key": "opening_hours", "prompt": "Strict JSON map of public session times..." },
    { "key": "pricing_data", "prompt": "{ adult: number|null, child: number|null, ... }" }
  ],
  "ai_exclusion_keywords": ["ice rink", "hockey", "curling", "...28 total"],
  "scrape_scope": "gap-fill",
  "photo_sources": ["google_places", "facebook", "..."],
  "publisher_auto_publish_threshold": 80,
  "publisher_required_fields": ["name", "lat", "lng", "..."]
}
```

**CRITICAL FORMAT NOTE**: `ai_target_vectors` can appear in 3 formats:
1. **Dashboard format**: `{ key: string, prompt: string }` — canonical format
2. **Sandbox/legacy format**: `{ key: string, type: string }` — old CCTower sandbox
3. **Flat string array**: `["pricing", "hours", ...]` — ancient seed script, ignored by engine

The DetectiveEngine handles all 3 via: `vec.prompt || vec.type` (see §6).

**CRITICAL: `ai_exclusion_keywords` is PERMANENT.**
The 28 default keywords are hardcoded in `LocalDB.ts` as `DEFAULT_EXCLUSION_KEYWORDS`. On every container boot, LocalDB restores any missing defaults. This list can never be fully wiped — adding keywords via any UI is always safe. Do NOT create alternate exclusion lists anywhere.

### `pipeline_field_registry` (Field Metadata)
Per-field configuration controlling extraction priority and publication gates.

| Column | Type | Purpose |
|--------|------|---------|
| `id` | TEXT PK | e.g., `opening_hours`, `p3_photos` |
| `field_name` | TEXT | DB column name |
| `phase_id` | INT | 1=Scout, 2=Detective, 3=Photographer, 4=Publisher |
| `display_label` | TEXT | Human-readable name |
| `data_type` | TEXT | `boolean`, `text`, `json`, `float`, etc. |
| `sort_order` | INT | Visual ordering |
| `importance_level` | INT | 0=normal, 1=priority, 2=required |
| `priority_group` | INT | DPPOS tier (0-7). See §4. |
| `is_hard_gate` | INT | 1 = Publisher rejects if null |
| `visual_glow` | INT | 1 = field glows in dashboard |

### `blocklist` (Legacy — Phase 1 Name Filter)
Secondary table for Phase 1 name-based spot blocking. When a keyword is added via the Phase 1 "BLOCK & PURGE" UI, it writes to BOTH this table AND `ai_exclusion_keywords` in `scraper_config`. The blocklist table is kept for backwards compatibility only. `ai_exclusion_keywords` is the source of truth.

---

## §4 — DPPOS Priority Tier System

The Dynamic Priority-Based Pass Ordering System controls which fields the DetectiveEngine sends to the LLM.

### Tier Map — 7 Tiers, each maps 1:1 to a DetectiveEngine pass
| Tier | Pass | Name | Fields | Hard Gate? |
|------|------|------|--------|-----------|
| **0** | — | Phase 1 Seeded | `name`, `street_address`, `lat`, `lng`, `city`, `state`, `zip`, `phone_number` | name/address only |
| **1** | 1A | 🕐 Session Hours | `opening_hours` | ✅ |
| **2** | 1B | 💰 Pricing & Fees | `pricing_data`, `has_fee`, `has_rental`, `price_range` | ✅ |
| **3** | 1C | 🌙 Adult Night | `has_adult_night`, `adult_night_schedule`, `adult_night_details` | ❌ |
| **4** | 2A | 🛹 Floor & Vibe | `surface_type`, `surface_quality`, `vibe_score` | ❌ |
| **5** | 2B | 🏢 Amenities | `is_indoor`, `has_pro_shop`, `has_food`, `has_lights`, `has_lockers`, `has_ac`, `has_wifi`, `has_toilets`, `capacity` | ❌ |
| **6** | 2C | 🎭 Identity & Culture | `is_wheelchair_accessible`, `hosts_derby`, `special_events`, `operator_name`, `operator_description`, `cultural_metadata` | ❌ |
| **7** | 2D | 📱 Contacts & Socials | `email_addresses`, `instagram_url`, `facebook_url`, `tiktok_url`, `schedule_url`, `yelp_url`, `logo_url` | ❌ |

**RULE**: Tier 0 fields are NEVER sent to the LLM — they come from Google Places in Phase 1.
**Scope Dropdown**: `gap-fill` (all tiers), `hours` (Pass 1A), `pricing` (Pass 1B), `tier-N` (fields ≤ tier N).

### Tier Seeding Locations (MUST stay in sync)
1. `LocalDB.ts` L332-362 — runs on every DB boot
2. `CCTower.ts` `/api/field-registry/reset` endpoint — manual reset button

---

## §5 — DetectiveEngine Pass Architecture

The engine runs up to **7 LLM passes** per spot, each with a laser-focused field schema.

### Field-to-Pass Map (`FIELD_PASS_MAP`)
```
pass1A: opening_hours                           (SOLO — most complex extraction)
pass1B: pricing_data, has_fee, has_rental, price_range  (all money-related)
pass1C: has_adult_night, adult_night_schedule, adult_night_details
pass2A: surface_type, surface_quality, vibe_score
pass2B: is_indoor, has_pro_shop, has_food, has_lights, has_lockers, has_ac, has_wifi, has_toilets, capacity
pass2C: is_wheelchair_accessible, hosts_derby, special_events, operator_name, operator_description, cultural_metadata
pass2D: instagram_url, facebook_url, tiktok_url, schedule_url, yelp_url, logo_url, email_addresses
```

### JIT Schema Compilation (`buildPassSchema`)
For each pass, only fields that are **empty OR below confidence threshold** are included in the schema.

Priority chain for extraction prompts:
```
1. configVectorMap[key]     ← from scraper_config.ai_target_vectors (DB)
2. FIELD_DESCRIPTIONS[key]  ← hardcoded fallback in DetectiveEngine.ts L219-272
3. Generic fallback         ← "${display_label} (${data_type} or null)."
```

### Data Collection Order
1. Puppeteer opens homepage → extracts DOM text + links + emails
2. SitemapParser classifies URLs (schedule, pricing, about, events, contact, gallery)
3. Puppeteer opens priority sub-pages (pricing → schedule → about → events → contact)
4. HeuristicsEngine runs regex extraction (phone, email, hours)
5. **Pre-LLM Toxicity Bouncer #1** — raw text regex scan (see §8)
6. Yelp review mining (if social links found)
7. **Pre-LLM Toxicity Bouncer #2** — semantic slice regex scan (see §8)
8. **Pass 1A**: Session Hours LLM call
9. **Pass 1B**: Pricing & Fees LLM call
10. **Pass 1C**: Adult Night LLM call
11. Escalation Protocol: if hours/pricing missing → OCR flyers + deep crawl → retry 1A/1B
12. **Pass 2A-2D**: Focused amenity/social/vibe passes (each independently skippable)
13. **Pass 3 (Synthesis)**: Merges all passes for quality score
14. Indexer checks `aiMetadata.TOXICITY_ABORT` → REJECTED if true

### Scope Modes
| Scope Value | Pass 1A | Pass 1B | Pass 1C | Pass 2A-2D |
|-------------|---------|---------|---------|------------|
| `gap-fill` | ✅ (if gaps) | ✅ (if gaps) | ✅ (if gaps) | ✅ (if gaps) |
| `hours` | ✅ | ❌ | ❌ | ❌ |
| `pricing` | ❌ | ✅ | ❌ | ❌ |
| `amenities` | ❌ | ❌ | ❌ | ✅ (if gaps) |
| `tier-N` | filtered | filtered | filtered | filtered |

---

## §6 — Data Flow: Config → Engine → DB

```
scraper_config.config_json (SQLite)
    ↓ getConfig() (LocalDB.ts)
    ↓
CCTower GET /config → { config: {...} }
    ↓ HTTP GET from Indexer
    ↓
Indexer.ts: configResGlobal = safeGet('/config')
Indexer.ts: aiConfig = configResGlobal.config
    ↓
Indexer.ts: executeDetective(target, aiConfig, ...)
    ↓
DetectiveEngine: exclusionKw = aiConfig.ai_exclusion_keywords
DetectiveEngine: usp = aiConfig.ai_system_prompt
DetectiveEngine: configVectorMap from aiConfig.ai_target_vectors
    ↓
Pre-LLM bouncer (regex) → early return TOXICITY_ABORT if keyword hit
    ↓
buildPassSchema → schema sent to LLM via buildSystem()
LLM system prompt includes: "TOXICITY: if PRIMARY business matches [...] return {TOXICITY_ABORT:true}"
    ↓
Each pass checks response.TOXICITY_ABORT === true → early return
    ↓
Indexer: result.aiMetadata.TOXICITY_ABORT === true → updateLocalSpot(REJECTED, rejection_reason)
         else → updateLocalSpot(DEEP_CRAWLED, enriched fields)
```

---

## §7 — Source Tiering (Winner-Takes-All)

When the Indexer writes detective results, it checks existing `field_confidence` before overwriting:

| Tier | Source | Trust Level |
|------|--------|-------------|
| 4 | `user_manual` | Highest — NEVER overwritten by AI |
| 3 | `json_ld` | Structured data from LD+JSON |
| 2 | `regex_extraction`, `ocr` | Pattern-matched from text |
| 1 | `llm_pass_*` | LLM extraction |
| 0 | `unknown` | No provenance |

**Rule**: New data only overwrites existing data if `newTier >= existingTier`.

---

## §8 — Toxicity Bouncer & Guillotine System (UNIFIED)

### The Problem This Solves
Ice rinks, hockey arenas, trampoline parks, and bike shops frequently appear in Google Places searches for roller rinks. Without the bouncer, these pollute the dataset.

### Single Source of Truth
`scraper_config.ai_exclusion_keywords` — a string array of 28 canonical keywords, hardcoded as `DEFAULT_EXCLUSION_KEYWORDS` in `LocalDB.ts` and restored on every boot.

**DO NOT create any other exclusion list anywhere. One list. One source.**

### 6 Rejection Checkpoints (All Phases)
| # | Where | When | How |
|---|-------|------|-----|
| 1 | `GoogleSweep.ts` | Sweep startup | Injects keywords into `BLOCKED_NAME_KEYWORDS` via `injectDynamicBlocklist()`. Name-match blocks before DB insert. |
| 2 | `GooglePlacesProvider.ts` | Every search result | `BLOCKED_PLACE_TYPES`, `BLOCKED_NAME_KEYWORDS`, `RETAIL_BLOCKLIST` static filters on Google type array + name |
| 3 | `DetectiveEngine.ts` | After raw page text collected | Regex word-boundary scan of ALL crawled text against exclusionKw. Zero LLM cost. |
| 4 | `DetectiveEngine.ts` | After semantic slice, before Pass 1A | Second regex scan of compressed text slice. |
| 5 | `DetectiveEngine.ts` | Inside every LLM pass system prompt | `"TOXICITY: If PRIMARY business matches [...] return {TOXICITY_ABORT: true}"` |
| 6 | `Indexer.ts` | After engine returns | `result.aiMetadata.TOXICITY_ABORT === true` → `verification_status = 'REJECTED'`, `rejection_reason` written to `ai_metadata` |

### TOXICITY_ABORT Return Shape
```ts
{
  aiMetadata: { TOXICITY_ABORT: true, reason: 'ice rink' },
  mappedFields: {},
  combinedText: '[TOXICITY REJECTED: "ice rink"]',
  qualityScore: 0,
  passedQualityGate: false,
  candidatePhotos: null,
  socialLinks: { instagram_url: null, facebook_url: null, tiktok_url: null, schedule_url: null },
  flyerUrls: [],
  fieldConfidence: {}
}
```

### Indexer Detection (CRITICAL — was broken before 2026-05-24)
```ts
// CORRECT:
const wasToxicityAborted = result.aiMetadata?.TOXICITY_ABORT === true;

// WRONG (old, broken — aiMetadata is { TOXICITY_ABORT: true }, NOT empty):
// const wasToxicityAborted = Object.keys(result.aiMetadata).length === 0;
```

### Adding/Removing Keywords
- **Phase 1 BLOCK & PURGE UI** → writes to `ai_exclusion_keywords` config + legacy `blocklist` table + runs SQL Guillotine (purges matching spots immediately)
- **Phase 2 Toxicity Bouncer UI** → writes directly to `ai_exclusion_keywords` config
- **Both UIs share the same list.** Adding from either location updates both phases.
- **⟳ RELOAD DEFAULTS** button in Phase 2 UI restores the 28 canonical keywords without removing custom additions.

### Default Keyword Categories (28 total)
| Category | Keywords |
|----------|---------|
| 🧊 Ice venues | `ice rink`, `ice skating`, `ice skating rink`, `ice arena`, `ice complex`, `ice palace`, `ice center`, `ice centre`, `ice sport` |
| 🏒 Hockey | `hockey rink`, `hockey arena`, `hockey complex` |
| 🥌 Curling | `curling`, `curling club`, `curling rink` |
| 🤸 Trampoline | `trampoline park`, `bounce house`, `jump zone`, `sky zone`, `altitude trampoline` |
| 🚲 Bikes/BMX | `bmx`, `bike park`, `mountain bike park` |
| 🎮 Other | `laser tag`, `mini golf`, `go-kart`, `go kart`, `bowling alley` |

### Rejection Audit Trail
Rejected spots store `rejection_reason` in `ai_metadata`. Query via:
- `GET /api/rejected-stats` → `{ total: N, recent: [...], breakdown: {...} }`
- Phase 2 config drawer → Recent Rejections audit log (last 15)

---

## §9 — Publisher Hard Gates

The Publisher reads `is_hard_gate` from `pipeline_field_registry`. If any hard-gated field is null/empty, the spot is REJECTED.

Current hard-gated fields:
- `name` (tier 0) — always present from Phase 1
- `street_address`, `lat`, `lng`, `city`, `state`, `zip` (tier 0)
- `opening_hours`, `pricing_data`, `has_fee`, `has_rental` (tier 1-2) — detective-extracted

---

## §10 — API Endpoints (CCTower)

### Global Config
| Method | Path | Purpose |
|--------|------|---------|
| GET | `/config` | Returns `{ config: {...} }` — full scraper_config JSON |
| POST | `/config` | Merges payload into scraper_config |
| GET | `/status` | Pipeline counts, daemon status, LMS telemetry |

### Field Registry
| Method | Path | Purpose |
|--------|------|---------|
| GET | `/api/field-registry` | All fields sorted by priority_group, sort_order |
| PUT | `/api/field-registry/:id` | Update priority_group, is_hard_gate, visual_glow |
| POST | `/api/field-registry/reset` | Reset all fields to system defaults |

### Pipeline Operations
| Method | Path | Purpose |
|--------|------|---------|
| POST | `/start` | Start all daemon processes |
| POST | `/stop` | Stop all daemon processes |
| POST | `/api/harvest/start-all` | Start Phase 1 sweep (Google or OSM) |
| POST | `/api/sandbox` | Run isolated Detective test on a URL |
| GET | `/api/pipeline-stats` | Aggregate pipeline statistics |
| GET | `/api/queue?phase=X` | Get spots in a specific phase queue |
| POST | `/api/bulk-reset-to-seeded` | Reset spots back to SEEDED status |

### Blocklist / Toxicity Bouncer
| Method | Path | Purpose |
|--------|------|---------|
| GET | `/api/scraper/blocklist` | Legacy blocklist table entries |
| POST | `/api/scraper/blocklist` | Add keyword → writes to BOTH blocklist table AND ai_exclusion_keywords + purges matching spots |
| DELETE | `/api/scraper/blocklist/:id` | Remove from legacy blocklist table only |
| GET | `/api/rejected-stats` | `{ total, recent[15], breakdown }` — rejection audit log |

### Spot CRUD
| Method | Path | Purpose |
|--------|------|---------|
| GET | `/api/spots` | Query spots with filters |
| PUT | `/api/spots/:id` | Update spot (triggers field_correction logging) |
| DELETE | `/api/spots/:id` | Soft-delete (marks REJECTED) |
| POST | `/api/skate_spots/:id/restart` | Re-enter detective queue |
| POST | `/api/skate_spots/:id/freeze` | Set ON_HOLD |

---

## §11 — Dashboard → Backend Data Contract

### BeltNode Scope Dropdown → Config
```
BeltNode.tsx onChange → ScraperPipeline.tsx onScopeChange
    → POST /config { scrape_scope: 'tier-3' }
    → scraper_config.config_json.scrape_scope = 'tier-3'
    → DetectiveEngine reads aiConfig.scrape_scope
    → maxTier = parseInt('tier-3'.split('-')[1])
    → buildPassSchema filters: f.priority_group > maxTier → skip
```

### PhaseControlDrawer → Config
- `ai_system_prompt` → stored as string in config JSON
- `ai_target_vectors` → stored as `[{key, prompt}]` array in config JSON
- `ai_exclusion_keywords` → stored as string array in config JSON (UNIFIED — applies to ALL phases)
- `detective_model` → stored as string in config JSON
- Field registry changes → direct PUT to `/api/field-registry/:id`

---

## §12 — Common Mistakes & Anti-Patterns

### ❌ DON'T: Create a second exclusion keyword list anywhere
`ai_exclusion_keywords` in `scraper_config` is the ONE source of truth. There used to be hardcoded ice/hockey arrays in `DetectiveEngine.ts` and separate `blocklist` table usage. These are replaced. Never add a new static keyword array in engine code.

### ❌ DON'T: Check toxicity by testing `Object.keys(aiMetadata).length === 0`
The old Indexer bug. TOXICITY_ABORT returns `{ TOXICITY_ABORT: true }` — NOT an empty object. Always check `aiMetadata?.TOXICITY_ABORT === true`.

### ❌ DON'T: Put Phase 1 seeded fields in the DPPOS tier dropdown
Phase 1 fields (name, address, lat/lng, phone) come from Google Places. Never extract them via LLM. They are tier 0.

### ❌ DON'T: Assume ai_target_vectors format
Three formats exist (§3). Always use `vec.prompt || vec.type` to be safe.

### ❌ DON'T: Use vector keys that don't match `field_name` in the registry
The `configVectorMap` is keyed by `vec.key`. Keys MUST be exact DB column names (e.g. `pricing_data`, not `pricing`).

### ❌ DON'T: Hardcode field lists in DetectiveEngine
Use `pipeline_field_registry` via `getFieldRegistry()`. `FIELD_DESCRIPTIONS` are fallbacks only.

### ❌ DON'T: Overwrite user_manual corrections
The Indexer's winner-takes-all tiering (§7) prevents this. Never bypass it.

### ❌ DON'T: Edit CCTower.ts tier reset without updating LocalDB.ts
The tier seeding SQL exists in TWO places (§4). Both MUST stay identical.

### ❌ DON'T: Run temp DB scripts with raw `new Database(path)`
Always import from `LocalDB.ts` or run through `bun run` from the scraper directory.

### ❌ DON'T: Use old PM2/PowerShell startup scripts
The stack runs in Docker. Old scripts (`start-fleet.ps1`, `ecosystem.config.cjs`) are deleted.

### ✅ DO: Read this bible before making scraper changes
### ✅ DO: Rebuild Docker after code changes (`docker compose up -d --build`)
### ✅ DO: Test live API endpoints to verify functional correctness, not just TypeScript compilation
### ✅ DO: Verify `ai_exclusion_keywords` count via `GET /config` after any bouncer change

---

## §13 — Docker Deployment

The entire scraper stack runs in a single Docker container.

### Container: `sk8lytz-scraper-stack`
- **Image**: `oven/bun:1` with Puppeteer Chrome + system deps
- **Ports**: `5999` (CCTower API), `5998` (Vite Dashboard)
- **Volumes**:
  - `./tools/scraper/logs` → `/app/tools/scraper/logs`
  - `./tools/.scraper-data` → `/app/tools/.scraper-data` (SQLite DB — persists across rebuilds)
  - `./.scraper-data/photos` → `/app/.scraper-data/photos`
  - `./.scraper-data/bucket` → `/app/.scraper-data/bucket`
- **Env**: `.env` file at project root

### Commands
```bash
# Start / rebuild after code changes
docker compose up -d --build

# View logs
docker logs sk8lytz-scraper-stack --tail 100 -f

# Stop
docker compose down

# Verify keywords are live after rebuild
curl http://localhost:5999/config | jq '.config.ai_exclusion_keywords | length'

# Check rejection stats
curl http://localhost:5999/api/rejected-stats | jq '{total: .total, recent: .recent[:3]}'
```

### ⚠️ CRITICAL: Code changes require rebuild
The Docker image copies source at build time (`COPY . .`). Edit any `.ts` file → `docker compose up -d --build`. The SQLite DB is volume-mounted and persists.

---

## §14 — What This Bible Was Missing (Added 2026-05-24)

The following were absent before this session and caused regressions:

1. **Toxicity bouncer documented as non-unified** — there were 3 separate lists. Now 1.
2. **`TOXICITY_ABORT` detection bug** — Indexer was checking `aiMetadata length === 0` (wrong). Now correctly checks `aiMetadata.TOXICITY_ABORT === true`.
3. **`DEFAULT_EXCLUSION_KEYWORDS` in LocalDB** — keywords were wipeable. Now hardcoded + boot-restored.
4. **`ai_metadata.rejection_reason`** — no audit trail existed for why spots were REJECTED. Now stored.
5. **`/api/rejected-stats` endpoint** — no way to query rejection audit log. Now exists.
6. **GoogleSweep unified injection** — Phase 1 was using only the legacy blocklist table, ignoring `ai_exclusion_keywords`. Now merged at sweep startup.
7. **Phase 1 BLOCK & PURGE write path** — was writing to legacy table only. Now syncs to `ai_exclusion_keywords` simultaneously.
8. **Pre-LLM regex bouncer was hardcoded** — 12 ice/hockey terms, score threshold of 3. Now uses the dynamic `ai_exclusion_keywords` list from config.
