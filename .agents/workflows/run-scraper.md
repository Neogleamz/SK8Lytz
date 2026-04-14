---
description: Run the Skate Spot Background Scraper and Push Pipeline
---

# Background Scraper Trigger (`/run-scraper`)

This workflow initiates the Zero-API Skatespot crawler locally to harvest location details natively.

## Phase 1: Pre-Flight Check
1. Target Directory: `tools/scraper`
2. Check if `.env` exists in `tools/scraper`. If missing, warn the user.
3. Check if `seed.json` has data.

## Phase 2: Crawler Launch
1. Ensure Node modules are installed.
2. Auto-run the `npm run start:bg` command on behalf of the user to start Stealth Crawling.
// turbo
`cd tools/scraper && npm run start:bg`

## Phase 3: Push Execution (Manual Sync)
- Ask the user if they'd prefer to automatically push what has already been crawled directly to Supabase (`npm run push`), or leave it running silently in the background.
