import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const RAW_DIR = path.resolve(__dirname, '../raw');

export class BaseCollector {
  async collect() {
    throw new Error('collect() must be implemented by subclass');
  }

  writeFindings(agentId, records) {
    if (!Array.isArray(records)) {
      throw new Error('Records must be an array');
    }

    const requiredFields = [
      'id',
      'fingerprint',
      'source',
      'collectedAt',
      'file',
      'message',
      'severity',
      'domain',
      'errorType',
      'occurrences',
      'firstSeen',
      'lastSeen',
      'trend',
      'urgencyScore',
      'autoHealCandidate',
      'requiredExpertise',
      'rawPayload',
    ];

    for (const record of records) {
      for (const field of requiredFields) {
        if (record[field] === undefined) {
          throw new Error(`Validation failed: Missing required field '${field}' in record`);
        }
      }
    }

    if (!fs.existsSync(RAW_DIR)) {
      fs.mkdirSync(RAW_DIR, { recursive: true });
    }

    const outputPath = path.join(RAW_DIR, `${agentId}_findings.json`);
    fs.writeFileSync(outputPath, JSON.stringify(records, null, 2), 'utf-8');
    console.log(`[BaseCollector] Wrote ${records.length} findings to ${outputPath}`);
  }
}
