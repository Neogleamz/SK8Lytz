import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const ROOT_DIR = path.resolve(__dirname, '../../../');

export async function collectRemoteErrors() {
  const records = [];
  const dumpFile = path.join(ROOT_DIR, 'tools', 'observatory', 'raw', 'remote_errors_dump.json');
  if (fs.existsSync(dumpFile)) {
    try {
      const data = JSON.parse(fs.readFileSync(dumpFile, 'utf-8'));
      for (const err of data) {
        records.push({
          id: `remote-${Date.now()}-${Math.random()}`,
          fingerprint: err.message || 'unknown_remote',
          source: 'SUPABASE_ERRORS',
          collectedAt: new Date().toISOString(),
          file: err.file || 'unknown',
          message: err.message || 'Unknown remote error',
          severity: 'HIGH',
          domain: 'CLOUD',
          errorType: 'RUNTIME',
          occurrences: 1,
          firstSeen: new Date().toISOString(),
          lastSeen: new Date().toISOString(),
          trend: 'NEW',
          urgencyScore: 0,
          autoHealCandidate: false,
          requiredExpertise: ['River', 'Sage'],
          rawPayload: err
        });
      }
    } catch (e) {}
  }
  return records;
}

if (process.argv[1] === __filename) {
  collectRemoteErrors().then(res => console.log(JSON.stringify(res, null, 2)));
}
