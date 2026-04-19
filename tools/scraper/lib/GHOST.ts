import { createClient, SupabaseClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';
import path from 'path';

dotenv.config({ path: path.resolve(__dirname, '../../.env') });

// Lazy-init: only create the client when actually needed, after .env is loaded
let _supabase: SupabaseClient | null = null;
function getSupabase(): SupabaseClient {
  if (!_supabase) {
    const url = process.env.EXPO_PUBLIC_SUPABASE_URL || '';
    const key = process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY || '';
    _supabase = createClient(url, key);
  }
  return _supabase;
}

export interface GhostIdentity {
  userAgent: string;
  viewport: { width: number; height: number };
  platform: string;
  languages: string[];
}

const USER_AGENTS = [
  'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36',
  'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
  'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36',
  'Mozilla/5.0 (iPhone; CPU iPhone OS 17_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4 Mobile/15E148 Safari/604.1',
  'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:124.0) Gecko/20100101 Firefox/124.0'
];

const VIEWPORTS = [
  { width: 1920, height: 1080 },
  { width: 1440, height: 900 },
  { width: 1536, height: 864 },
  { width: 1366, height: 768 },
  { width: 1280, height: 720 }
];

export const GHOST = {
  /**
   * Generates a randomized browser identity for spoofing.
   */
  generateIdentity(): GhostIdentity {
    const ua = USER_AGENTS[Math.floor(Math.random() * USER_AGENTS.length)];
    const vp = VIEWPORTS[Math.floor(Math.random() * VIEWPORTS.length)];
    
    return {
      userAgent: ua,
      viewport: {
        width: vp.width + (Math.floor(Math.random() * 40) - 20),
        height: vp.height + (Math.floor(Math.random() * 40) - 20)
      },
      platform: ua.includes('Windows') ? 'Win32' : (ua.includes('Macintosh') ? 'MacIntel' : 'Linux x86_64'),
      languages: ['en-US', 'en']
    };
  },

  /**
   * Calculates a jittered delay based on the central scraper configuration.
   * Optimized for SK8Lytz production pace (45s - 90s average).
   */
  async getAdaptiveDelay(type: 'OSM' | 'GOOGLE' | 'NOMINATIM'): Promise<number> {
    const { data: config } = await getSupabase().from('scraper_config').select('*').eq('id', 1).single();
    
    let base = 30000; // Average 30s base
    let jitter = 35; // 35% Jitter

    if (config) {
      // If dashboard has values, use them, otherwise use our optimized defaults
      base = config.cooldown_base_ms || 30000;
      jitter = config.cooldown_jitter_pct || 35;
    }

    // Apply specific scaling per service
    let factor = 1.0;
    if (type === 'OSM') factor = 0.5; // OSM batch is fast (15s)
    if (type === 'GOOGLE') factor = 2.0; // Google (Search) is sensitive (60s)
    if (type === 'NOMINATIM') factor = 0.1; // Nominatim is strictly 3s/hit

    const target = base * factor;
    const jitterAmt = target * (jitter / 100) * (Math.random() * 2 - 1);
    
    // Safety minimums to prevent hard bans
    const finalDelay = Math.max(target + jitterAmt, 2000);
    
    return finalDelay;
  }
};
