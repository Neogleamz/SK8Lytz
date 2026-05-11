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

---

## The Detective Engine (Zod / LLM)
- **DO NOT** use Regex to parse websites. 
- We use LM Studio / Ollama via `DetectiveEngine.ts` to parse raw HTML blocks into strictly typed JSON.
- We enforce extraction using Zod Schemas.
  
**Example Schema Constraint**:
```typescript
z.object({
  weekly_hours: z.array(z.string()).describe("Strictly format as 'Mon: 10AM-10PM'. Null if missing."),
  social_links: z.array(z.string().url()).describe("Must be valid URLs. Extract from href tags.")
})
```

If extraction is failing, you must modify the **Zod schema and system prompt** in `DetectiveEngine.ts`. Do NOT write procedural string parsing or Regex cleanup scripts.

---

## The Local SQLite Truth
- The master data lives in `scraper.db` locally.
- Supabase is ONLY a downstream replica. 
- Never write scripts that bypass SQLite to write directly to Supabase. The `Publisher` daemon (`scraper-publisher.ts`) handles all syncing using the `is_published` toggle.
