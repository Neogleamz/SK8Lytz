# Photographer Premium Harvest Plan

This plan details the re-engineering of the photo selection heuristics, OCR flyer gates, image deduplication, and photo cap expansion from 10 to 20 inside `Photographer.ts` to capture high-fidelity rink visual assets.

---

## User Review Required

> [!IMPORTANT]
> - **Capping File Size Bias:** We will clamp the base size contribution of each candidate image at `250KB`. This prevents massive, unoptimized background files (e.g., 2MB-3MB) from winning purely due to raw size, allowing optimized rink photos to rank at the top.
> - **Google Places Priority:** Google Places photo references (`google_refs`) are verified user-submitted physical photos of the rinks themselves. We will give them a massive `+1000KB` (+1MB) boost to prioritize them over raw website assets.
> - **Flyer OCR Relaxation:** We will expand the Tesseract OCR flyer threshold from a restrictive `100` characters to `350` characters. This permits beautiful rink photos with wall text, logos, or neon signs to pass while still filtering out massive calendar flyers and coupons.
> - **Zero OCR Latency for Google Place Refs:** Since Google Places photos represent physical rink images and are never coupons, we will bypass the slow Tesseract OCR engine for all `google_refs`, accelerating processing speeds.
> - **Expanded Collection Cap:** We are raising `MAX_PHOTOS` from `10` to `20` per spot.
> - **Wix & Squarespace CDN Footprints:** We will expand the Puppeteer lazy-load parser to capture images with Wix or Squarespace CDN footprints even when their DOM dimensions are `0` on load.

---

## Proposed Changes

### Phase 3 AI Photo Harvest Daemon

#### [MODIFY] [Photographer.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz-worktrees/photographer-premium-harvest/tools/scraper/Photographer.ts)

1. **Up the Photo Cap:**
   - Increase `MAX_PHOTOS` from `10` to `20` (around line 38).

2. **Re-engineer `calculateCandidateScore`:**
   - Clamp the raw `size` parameter to a maximum of `250 * 1024` (250KB).
   - If the candidate is a Google Place reference (`c.pageSource === 'google_refs'` or URL contains `maps.googleapis.com` or `googleusercontent.com`), add a massive `+1000 * 1024` boost.
   - Multiply parent-class gallery/carousel boosts (`+500 * 1024` instead of `+150 * 1024`).
   - Multiply good keyword alt/URL boosts (`+400 * 1024` instead of `+200 * 1024`).
   - Scan `c.parentClass` for good keywords in addition to `c.url` and `c.alt`.

3. **Optimize `estimateImageQuality` and Relax OCR gates:**
   - Increase OCR flyer bouncer text threshold from `100` to `350` characters.
   - Skip Tesseract OCR recognition for all Google Places photo references (`c.pageSource === 'google_refs'` or URL matches Google Maps Place photo signatures), saving CPU time and ensuring high-quality images skip bouncer checks.
   - Ensure the bouncer processes up to `MAX_PHOTOS * 2.5` (50 URLs) but terminates as soon as we acquire `MAX_PHOTOS` (20) valid URLs.

4. **Expand Lazy-load CDN foot-printing:**
   - Inside Puppeteer `crawlForImages`, add Wix/Squarespace CDN string checks to `isHighResCandidate`:
     ```typescript
     const isHighResCandidate = /uploads|gallery|media|photos|rink|skate|original/i.test(src) || 
                                src.includes('places/photo') ||
                                src.includes('wixstatic.com/media') ||
                                src.includes('squarespace-cdn.com');
     ```

---

## Verification Plan

### Automated Tests
- Run `npm run verify` in the repository root to ensure that all Jest unit tests (84/84) and TypeScript compilation gates pass.

### Manual Verification
1. Rebuild and launch the docker scraper stack:
   ```powershell
   docker compose up -d --build
   ```
2. Reset a spot to `DEEP_CRAWLED` status in the DB and watch the Photographer daemon ingest it.
3. Verify that the photographer captures up to 20 unique images, prioritizing Google Places and high-res interiors over low-res or layout graphics.
4. Verify that the Vite dashboard displays the 20 polaroids accurately under the Photographer card.
