const db = require('better-sqlite3')('../.scraper-data/scraper.db');

const rows = db.prepare('SELECT id, street_address, zip FROM local_spots').all();
let updated = 0;
let failed = 0;

const updateStmt = db.prepare('UPDATE local_spots SET zip = ? WHERE id = ?');

// Regex matches "State ZIP, USA" or "State ZIP" at the end of the string
// Match 1: State code
// Match 2: ZIP code (5 or 9 digits)
const zipRegex = /\b([A-Z]{2})\s+(\d{5}(?:-\d{4})?)(?:,\s*USA)?\s*$/i;

db.transaction(() => {
  for (const row of rows) {
    if (!row.street_address) {
      failed++;
      continue;
    }

    const match = row.street_address.match(zipRegex);
    if (match) {
      const parsedZip = match[2];
      if (row.zip !== parsedZip) {
        updateStmt.run(parsedZip, row.id);
        updated++;
      }
    } else {
      failed++;
    }
  }
})();

console.log(`Successfully parsed and updated ${updated} records.`);
console.log(`Failed to parse ${failed} records.`);
