const path = require('path');
const Database = require('better-sqlite3');

const dbPath = path.resolve(__dirname, '../../.scraper-data/scraper.db');
const db = new Database(dbPath);

try {
  const info = db.prepare('DELETE FROM scraper_blocklist WHERE id NOT IN (SELECT MIN(id) FROM scraper_blocklist GROUP BY pattern)').run();
  console.log(`Deduplicated blocklist. Removed ${info.changes} duplicates.`);
} catch (e) {
  console.error('Error deduplicating:', e);
}
