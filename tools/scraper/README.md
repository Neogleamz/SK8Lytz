# 🛡️ Micro-Doc: The Scraper Engine

> **CRITICAL AI CONTEXT**: If you are editing files in `tools/scraper/`, you MUST read this first.

## The State Machine (SQLite -> Supabase)
The scraper operates on a strict one-way pipeline powered by PM2 background daemons.
Do not manually flip states unless via the Scraper Dashboard UI.

1. `QUEUED`: Newly discovered or manually reset URLs.
2. `EXTRACTING`: Claimed by `scraper-indexer` or `scraper-photographer`. LLM is analyzing it.
3. `SEEDED`: Basic text data extracted, awaiting photo download.
4. `MEDIA_READY`: Photos downloaded, awaiting manual human review of the 'Hero Image'.
5. `PUBLISHED`: Pushed to Supabase. Now visible in the mobile app.

## The Detective Engine
- DO NOT use Regex to parse websites. 
- We use LM Studio / Ollama via `DetectiveEngine.ts` to parse raw HTML blocks into strictly typed JSON (Zod schema).
- If extraction is failing (e.g., missing social media links), you must modify the Zod schema and system prompt in `DetectiveEngine.ts`, do NOT write procedural string parsing.

## The Local SQLite Truth
- The master data lives in `scraper.db` locally.
- Supabase is ONLY a downstream replica. 
- Never write scripts that bypass SQLite to write directly to Supabase. The `Publisher` daemon handles all syncing.
