const Database = require('better-sqlite3');
const path = require('path');

const DB_PATH = path.resolve(__dirname, '../../.scraper-data/scraper.db');
const db = new Database(DB_PATH);

// Show current state
const before = db.prepare('SELECT phase_id, COUNT(*) as cnt FROM pipeline_field_registry GROUP BY phase_id ORDER BY phase_id').all();
console.log('BEFORE:', before);

// Migrate high→low to avoid ID clashes (5→4 first, then 4→3, then 3→2)
const r5 = db.prepare('UPDATE pipeline_field_registry SET phase_id = 4 WHERE phase_id = 5').run();
const r4 = db.prepare('UPDATE pipeline_field_registry SET phase_id = 3 WHERE phase_id = 4').run();
const r3 = db.prepare('UPDATE pipeline_field_registry SET phase_id = 2 WHERE phase_id = 3').run();

console.log(`Migrated: phase5→4: ${r5.changes}, phase4→3: ${r4.changes}, phase3→2: ${r3.changes}`);

// Verify
const after = db.prepare('SELECT phase_id, COUNT(*) as cnt FROM pipeline_field_registry GROUP BY phase_id ORDER BY phase_id').all();
console.log('AFTER:', after);

db.close();
