import fs from 'fs';
import path from 'path';

export interface HeuristicLedger {
  path_priorities: Record<string, number>;
  selectors_registry: Record<string, Record<string, string>>;
  min_confidence_to_bypass_llm: number;
  early_termination_enabled: boolean;
  semantic_buffer_characters: number;
}

const DEFAULT_LEDGER: HeuristicLedger = {
  path_priorities: {
    "rates": 0.94,
    "pricing": 0.89,
    "admission": 0.82,
    "schedule": 0.78,
    "hours": 0.76,
    "session": 0.72,
    "times": 0.70,
    "calendar": 0.65,
    "tickets": 0.60
  },
  selectors_registry: {
    "wix": {
      "skate_rental": "div.pricing-list span.skate-rental-price",
      "opening_hours": "div#comp-lh9sk8lz p"
    },
    "wordpress": {
      "pricing_data": "table.rates-table tr",
      "opening_hours": "div.hours-widget"
    }
  },
  min_confidence_to_bypass_llm: 0.85,
  early_termination_enabled: true,
  semantic_buffer_characters: 800
};

export class HeuristicsEngine {
  private static ledgerPath = path.resolve(__dirname, '../../.scraper-data/global-heuristics.json');
  private static cachedLedger: HeuristicLedger | null = null;

  public static load(): HeuristicLedger {
    if (this.cachedLedger) return this.cachedLedger;
    try {
      const parentDir = path.dirname(this.ledgerPath);
      if (!fs.existsSync(parentDir)) {
        fs.mkdirSync(parentDir, { recursive: true });
      }
      if (!fs.existsSync(this.ledgerPath)) {
        fs.writeFileSync(this.ledgerPath, JSON.stringify(DEFAULT_LEDGER, null, 2), 'utf8');
        this.cachedLedger = { ...DEFAULT_LEDGER };
        return this.cachedLedger;
      }
      const raw = fs.readFileSync(this.ledgerPath, 'utf8');
      const loaded = JSON.parse(raw);
      // Merge with defaults to ensure schema completeness
      this.cachedLedger = { ...DEFAULT_LEDGER, ...loaded };
      return this.cachedLedger!;
    } catch (e: any) {
      console.error(`[HEURISTIC] ❌ Failed to load global-heuristics.json, falling back to defaults:`, e.message);
      return DEFAULT_LEDGER;
    }
  }

  public static save(ledger: HeuristicLedger): boolean {
    try {
      const parentDir = path.dirname(this.ledgerPath);
      if (!fs.existsSync(parentDir)) {
        fs.mkdirSync(parentDir, { recursive: true });
      }
      fs.writeFileSync(this.ledgerPath, JSON.stringify(ledger, null, 2), 'utf8');
      this.cachedLedger = { ...ledger };
      this.log(`✅ Saved global-heuristics.json successfully!`);
      return true;
    } catch (e: any) {
      console.error(`[HEURISTIC] ❌ Failed to save global-heuristics.json:`, e.message);
      return false;
    }
  }

  public static getPathPriorities(): Array<{ key: string; weight: number }> {
    const ledger = this.load();
    return Object.entries(ledger.path_priorities)
      .map(([key, weight]) => ({ key, weight }))
      .sort((a, b) => b.weight - a.weight);
  }

  public static getPriorityRegex(): RegExp {
    const priorities = this.getPathPriorities().map(p => p.key);
    if (priorities.length === 0) return /schedule|pricing|hours|admission|rates/i;
    // Returns a combined case-insensitive regex pattern
    return new RegExp(priorities.join('|'), 'i');
  }

  /**
   * Extracts semantic slices of text around high-value headers
   * like Pricing, Rates, Hours, Concessions, or Schedules.
   */
  public static getSemanticSlice(htmlText: string): { slice: string; ratio: number } {
    if (!htmlText || htmlText.trim().length === 0) return { slice: '', ratio: 1.0 };
    
    this.log(`🧬 Running Semantic DOM Slicing on ${htmlText.length} character source...`);
    const ledger = this.load();
    const buffer = ledger.semantic_buffer_characters || 800;

    // Compile regex of semantic targets
    const targetKeywords = ['price', 'pricing', 'rates', 'admission', 'fee', 'hours', 'schedule', 'session', 'times', 'concession', 'cafe', 'food'];
    const headingRegex = new RegExp(`<h[1-6]\\b[^>]*>([\\s\\S]*?${targetKeywords.join('|')}[\\s\\S]*?)<\\/h[1-6]>`, 'i');

    let matchedSlice = '';
    let match;
    const workingText = htmlText;
    
    // Scan text for heading matches
    const searchRegex = new RegExp(headingRegex.source, 'gi');
    let slicesCount = 0;

    while ((match = searchRegex.exec(workingText)) !== null && slicesCount < 4) {
      const matchIndex = match.index;
      const start = Math.max(0, matchIndex - 100); // 100 chars head buffer
      const end = Math.min(workingText.length, matchIndex + match[0].length + buffer);
      
      matchedSlice += `\n--- SEMANTIC SEGMENT [${match[1].trim().replace(/<[^>]*>/g, '').slice(0, 30)}] ---\n`;
      matchedSlice += workingText.slice(start, end) + '\n';
      slicesCount++;
    }

    if (matchedSlice.trim().length <= 50) {
      this.log(`🧬 HTML headings did not match. Running Plain Text keyword window scanner...`);
      let ptSlicesCount = 0;
      const keywordRegex = new RegExp(`\\b(${targetKeywords.join('|')})\\b`, 'i');
      const lines = workingText.split('\n');
      
      for (let i = 0; i < lines.length && ptSlicesCount < 6; i++) {
        const line = lines[i].trim();
        if (keywordRegex.test(line)) {
          const startLine = Math.max(0, i - 3);
          const endLine = Math.min(lines.length, i + 12);
          const segmentText = lines.slice(startLine, endLine).join('\n');
          
          matchedSlice += `\n--- SEMANTIC WINDOW [${line.slice(0, 35)}] ---\n`;
          matchedSlice += segmentText + '\n';
          ptSlicesCount++;
          i = endLine - 1; // Skip ahead
        }
      }
    }

    if (matchedSlice.trim().length > 50) {
      const originalLen = htmlText.length;
      const slicedLen = matchedSlice.length;
      const ratio = Math.round((slicedLen / originalLen) * 100) / 100;
      this.log(`✂️ Slicing successful! Reduced context text from ${originalLen} to ${slicedLen} characters (Ratio: ${ratio}).`);
      return { slice: matchedSlice, ratio };
    }

    this.log(`⚠️ No specific semantic blocks matched — falling back to full-text delivery.`);
    return { slice: htmlText, ratio: 1.0 };
  }

  private static log(msg: string) {
    console.log(`[HEURISTIC] ${msg}`);
  }
}
