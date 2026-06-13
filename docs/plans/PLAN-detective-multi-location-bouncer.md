# Detective Multi-Location Bouncer Plan

This plan details the implementation of a self-learning, adaptive Sitemap Sibling Location Bouncer inside `DetectiveEngine.ts` to cleanly isolate and prioritize sitemap paths belonging to the target city/location, while completely filtering out sibling branch paths on shared multi-location root domains.

---

## User Review Required

> [!IMPORTANT]
> - **Self-Inductive Branch Discovery:** The scraper will automatically extract all branch path directories (e.g., `/aurora`, `/arvada`, `/littleton`) from the discovered sitemap URLs.
> - **Pruning Sibling Locations:** Sibling branch paths that do not match the current spot's `city` or `name` (e.g., Aurora, Arvada paths when processing a Littleton spot) will be completely excluded from all sitemap queues before crawling.
> - **Zero-Config/Offline-First:** This requires no hardcoded list of networks or configuration files, runs 100% offline, and works perfectly on shared multi-location websites (e.g. `skatecitycolorado.com`, `sparklesrinks.com`).

---

## Proposed Changes

### Phase 2 AI Detective Engine

#### [MODIFY] [DetectiveEngine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz-worktrees/detective-multi-location-bouncer/tools/scraper/core/DetectiveEngine.ts)

1. **Implement `filterMultiLocationUrls` Helper:**
   Surgically construct the sitemap bouncer right after `sitemap = await parseSitemap(spotContext.website);` is resolved:
   - Identify branch pages in the sitemap by scanning for top-level subdirectories consisting of a single alphanumeric word.
   - Filter out sibling branches that do not match the lowercase target `city` or `name` of the current record.
   - Prune all matching sibling branches cleanly from `sitemap.schedule_urls`, `pricing_urls`, `contact_urls`, `gallery_urls`, `about_urls`, and `all_urls`.

---

## Verification Plan

### Automated Tests
- Run `npm run verify` in the repository root to ensure all Jest unit tests (84/84) and TypeScript compilation gates pass.

### Manual Verification
1. Rebuild and launch the docker scraper stack:
   ```powershell
   docker compose up -d --build
   ```
2. Reset `Skate City Littleton` (which uses the shared root domain `https://www.skatecitycolorado.com/home`) back to `PENDING_WEBSITE` or `SEEDED` status in the DB.
3. Start the harvester and watch the live telemetry log overlay.
4. Verify that the crawler detects the sibling branches (`aurora`, `arvada`, `academy`, `woodmen`, `westminster`) and successfully prunes all their pages from the Littleton crawling queue.
