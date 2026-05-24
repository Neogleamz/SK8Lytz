import { db, updateLocalSpot } from './LocalDB';

export interface ResolutionResult {
  success: boolean;
  website: string | null;
  score: number;
  source: 'osm' | 'ddg' | null;
  reason: string;
}

export class WebsiteResolver {
  private static log(msg: string) {
    console.log(`[WEBSITE_RESOLVER] ${msg}`);
  }

  /**
   * Main resolve entry point for a single spot.
   * Runs OSM spatial search first, then DuckDuckGo HTML metasearch fallback.
   */
  public static async resolveWebsite(spot: any): Promise<ResolutionResult> {
    if (!spot || !spot.name) {
      return { success: false, website: null, score: 0, source: null, reason: 'Invalid spot record' };
    }

    this.log(`🔍 Resolving website for: ${spot.name} in ${spot.city || 'unknown'}, ${spot.state || 'unknown'}...`);

    // --- Step 1: OpenStreetMap Overpass Spatial Search ---
    if (spot.lat && spot.lng) {
      try {
        this.log(`🌐 Querying OpenStreetMap spatial nodes in a 2000m radius...`);
        const osmUrl = `https://overpass-api.de/api/interpreter`;
        // Query for roller_skating leisure or sport nodes within 2000m
        const osmQuery = `[out:json][timeout:15];(node["leisure"="roller_skating"](around:2000,${spot.lat},${spot.lng});way["leisure"="roller_skating"](around:2000,${spot.lat},${spot.lng});node["sport"="roller_skating"](around:2000,${spot.lat},${spot.lng}););out tags;`;
        
        const res = await fetch(osmUrl, {
          method: 'POST',
          headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
          body: `data=${encodeURIComponent(osmQuery)}`,
          signal: AbortSignal.timeout(10000)
        });

        if (res.ok) {
          const data: any = await res.json();
          if (data && Array.isArray(data.elements)) {
            for (const el of data.elements) {
              const website = el.tags?.website || el.tags?.["contact:website"];
              if (website && website.startsWith('http')) {
                this.log(`✅ OSM spatial match successful! Found website: ${website}`);
                return {
                  success: true,
                  website,
                  score: 1.0,
                  source: 'osm',
                  reason: `OSM matched within 2000m radius of coordinate (Node: ${el.id})`
                };
              }
            }
          }
        }
      } catch (err: any) {
        this.log(`⚠️ OSM Overpass query failed or timed out: ${err.message}`);
      }
    }

    // --- Step 2: DuckDuckGo HTML Lite Metasearch ---
    try {
      this.log(`🦆 Launching DuckDuckGo keyless metasearch fallback...`);
      const query = `${spot.name} ${spot.city || ''} ${spot.state || ''} website`;
      const searchUrl = `https://html.duckduckgo.com/html/?q=${encodeURIComponent(query)}`;

      const res = await fetch(searchUrl, {
        headers: {
          'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36',
          'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8'
        },
        signal: AbortSignal.timeout(12000)
      });

      if (!res.ok) {
        throw new Error(`DDG returned HTTP ${res.status}`);
      }

      const html = await res.text();
      const candidates = this.extractUrlsFromDdgHtml(html);

      if (candidates.length === 0) {
        return { success: false, website: null, score: 0, source: null, reason: 'No organic search results found' };
      }

      this.log(`🦆 Retrieved ${candidates.length} organic links. Running Relevance Validation...`);

      // Run relevance evaluation
      for (const cand of candidates) {
        const evaluation = this.evaluateRelevance(cand.url, cand.title, cand.snippet, spot);
        this.log(`   - Evaluating: ${cand.url} | Score: ${(evaluation.score * 100).toFixed(0)}% (${evaluation.reason})`);
        
        if (evaluation.score >= 0.75) {
          this.log(`🎯 Match approved! URL: ${cand.url}`);
          return {
            success: true,
            website: cand.url,
            score: evaluation.score,
            source: 'ddg',
            reason: evaluation.reason
          };
        }
      }

      return { 
        success: false, 
        website: null, 
        score: 0, 
        source: null, 
        reason: 'Search returned results but none met the 0.75 relevance threshold' 
      };

    } catch (err: any) {
      this.log(`❌ DuckDuckGo metasearch failed: ${err.message}`);
      return { success: false, website: null, score: 0, source: null, reason: `Search failure: ${err.message}` };
    }
  }

  /**
   * Parsed DuckDuckGo Lite page to extract URLs, titles, and snippets.
   */
  private static extractUrlsFromDdgHtml(html: string): Array<{ url: string; title: string; snippet: string }> {
    const results: Array<{ url: string; title: string; snippet: string }> = [];
    
    // DDG HTML structures organic results inside links with class "result__url" or redirect tags
    // Let's use robust regex to parse out result links and block out common social media pages
    const resultRegex = /<a\s+class="result__snippet"[^>]*href="([^"]+)"[^>]*>([\s\S]*?)<\/a>|<a\s+class="result__url"[^>]*href="([^"]+)"[^>]*>/gi;
    const urlRegex = /class="result__url"\s+href="([^"]+)"/gi;

    // Direct parser of standard anchors containing ddg redirect links
    const anchorRegex = /<a\s+[^>]*href=["']([^"']+)["'][^>]*>([\s\S]*?)<\/a>/gi;
    let match;

    const blocklistedDomains = [
      'facebook.com', 'instagram.com', 'yelp.com', 'tripadvisor.com', 
      'youtube.com', 'twitter.com', 'x.com', 'tiktok.com', 'mapquest.com',
      'yellowpages.com', 'local.yahoo.com', 'groupon.com', 'wikipedia.org',
      'wixpress.com', 'squarespace.com', 'wordpress.com'
    ];

    while ((match = anchorRegex.exec(html)) !== null) {
      const href = match[1];
      const text = match[2].replace(/<[^>]+>/g, '').trim();

      let targetUrl = '';
      if (href.includes('uddg=')) {
        const uddgMatch = href.match(/[?&]uddg=([^&]+)/);
        if (uddgMatch) {
          try {
            targetUrl = decodeURIComponent(uddgMatch[1]);
          } catch {}
        }
      } else if (href.startsWith('http')) {
        targetUrl = href;
      }

      if (targetUrl) {
        try {
          const parsed = new URL(targetUrl);
          const hostname = parsed.hostname.toLowerCase();
          
          // Skip blocklisted directory domains
          const isBlocked = blocklistedDomains.some(d => hostname === d || hostname.endsWith('.' + d));
          if (isBlocked) continue;

          // Skip trailing slashes and normalize
          const normalized = parsed.origin + parsed.pathname.replace(/\/+$/, '');
          if (!results.some(r => r.url === normalized)) {
            results.push({
              url: normalized,
              title: text || '',
              snippet: ''
            });
          }
        } catch {}
      }
    }

    return results.slice(0, 5); // Target top 5 results
  }

  /**
   * Runs the 4-part Relevance Scoring rules.
   */
  private static evaluateRelevance(url: string, title: string, snippet: string, spot: any): { score: number; reason: string } {
    let score = 0;
    const reasons: string[] = [];

    const spotName = spot.name.toLowerCase();
    const city = (spot.city || '').toLowerCase();
    const state = (spot.state || '').toLowerCase();
    
    let hostname = '';
    try {
      hostname = new URL(url).hostname.toLowerCase().replace('www.', '');
    } catch {
      return { score: 0, reason: 'Invalid URL hostname' };
    }

    // 1. Name Match (Up to 0.40 weight)
    const spotNameClean = spotName.replace(/skate|rink|roller|skateland|center|arena/g, '').trim();
    const cleanTokens = spotNameClean.split(/\s+/).filter((t: string) => t.length > 2);
    
    let matchesName = false;
    for (const token of cleanTokens) {
      if (hostname.includes(token)) {
        score += 0.40;
        matchesName = true;
        reasons.push(`Brand matched token: "${token}"`);
        break;
      }
    }
    if (!matchesName) {
      // General similarity fallback
      if (hostname.includes(spotName.slice(0, 6))) {
        score += 0.25;
        reasons.push('Partial brand name matching prefix');
      }
    }

    // 2. Location Match (Up to 0.30 weight)
    let matchedLocation = false;
    if (city && (hostname.includes(city) || title.toLowerCase().includes(city) || snippet.toLowerCase().includes(city))) {
      score += 0.20;
      matchedLocation = true;
      reasons.push(`Location matched city: "${spot.city}"`);
    }
    if (state && (hostname.includes(state) || title.toLowerCase().includes(state) || snippet.toLowerCase().includes(state))) {
      score += 0.10;
      matchedLocation = true;
      reasons.push(`Location matched state: "${spot.state}"`);
    }

    // 3. Topic Keywords Match (Up to 0.30 weight)
    const topicKeywords = ['skate', 'rink', 'roller', 'skateland', 'wheels', 'arena', 'center', 'palace', 'sessions', 'skating'];
    let matchedTopic = false;
    for (const kw of topicKeywords) {
      if (hostname.includes(kw)) {
        score += 0.30;
        matchedTopic = true;
        reasons.push(`Topic keyword matched in domain: "${kw}"`);
        break;
      }
    }
    if (!matchedTopic) {
      for (const kw of topicKeywords) {
        if (title.toLowerCase().includes(kw) || snippet.toLowerCase().includes(kw)) {
          score += 0.15;
          reasons.push(`Topic keyword matched in content: "${kw}"`);
          break;
        }
      }
    }

    return {
      score: Math.min(1.0, score),
      reason: reasons.length > 0 ? reasons.join('; ') : 'No strong signals matched'
    };
  }
}
