import { Database } from 'bun:sqlite';
import * as fs from 'fs';

const db = new Database('C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/.scraper-data/scraper.db');
const rows = db.query("SELECT operator_name, google_maps_url, ai_metadata FROM local_spots WHERE verification_status = 'REJECTED' AND is_deep_crawled = 1").all();

let md = '# Deep-Crawled Rejections Summary\n\n';
md += 'Here are all 103 spots that were rejected during the deep crawl, along with the exact keyword/reason that triggered the rejection.\n\n';
md += '| Name | Reason | Maps URL |\n';
md += '| --- | --- | --- |\n';

rows.forEach(r => {
  let reason = 'Unknown';
  try {
    reason = JSON.parse(r.ai_metadata).rejection_reason || 'Unknown';
  } catch(e) {}
  const name = r.operator_name || 'Unknown';
  const url = r.google_maps_url ? `[Link](${r.google_maps_url})` : 'N/A';
  md += `| ${name} | **${reason}** | ${url} |\n`;
});

fs.writeFileSync('C:/Users/Magma/.gemini/antigravity/brain/e64c0f35-e4e4-4b73-940e-a7d3bf07479a/rejected_spots.md', md);
