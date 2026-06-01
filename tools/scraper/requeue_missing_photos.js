import { Database } from "bun:sqlite";
const db = new Database('/app/tools/.scraper-data/scraper.db');

const updateStmt = db.prepare(`
  UPDATE local_spots 
  SET verification_status = 'DEEP_CRAWLED', retry_count = 0 
  WHERE candidate_photos LIKE '%gallery_urls%' 
    AND (photos IS NULL OR photos = '[]')
`);

const result = updateStmt.run();
console.log(`Updated ${result.changes} spots back to DEEP_CRAWLED queue for the Photographer.`);
