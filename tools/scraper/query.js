const db = require('better-sqlite3')('../.scraper-data/scraper.db');

// 1. Revert test record
db.prepare('UPDATE local_spots SET is_published = 0 WHERE id = ?').run('05b948fc-b57b-4c9e-aa94-8b327dfa4ddf');

// 2. Purge all records that match the blocklist
const bl = db.prepare('SELECT pattern FROM scraper_blocklist').all();
const blkw = db.prepare('SELECT keyword FROM scraper_blocklist_keywords').all();

const allKeywords = new Set([
  ...bl.map(b => b.pattern.toLowerCase().trim()), 
  ...blkw.map(b => b.keyword.toLowerCase().trim())
]);

let totalPurged = 0;
for (const kw of allKeywords) {
  if (!kw) continue;
  const info = db.prepare("UPDATE local_spots SET verification_status = 'REJECTED', is_published = 0 WHERE name LIKE ? COLLATE NOCASE AND verification_status != 'REJECTED'").run('%' + kw + '%');
  if (info.changes > 0) {
    console.log('Purged ' + info.changes + ' records for keyword: ' + kw);
    totalPurged += info.changes;
  }
}
console.log('Total purged:', totalPurged);
