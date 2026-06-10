import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import crypto from 'crypto';
import { spawnSync } from 'child_process';
import { BaseCollector } from './BaseCollector.mjs';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const ROOT_DIR = path.resolve(__dirname, '../../../');

export class TscErrorsCollector extends BaseCollector {
  async collect() {
    const result = spawnSync('npx', ['tsc', '--noEmit'], {
      cwd: ROOT_DIR,
      encoding: 'utf-8',
      shell: true
    });

    const output = (result.stdout || '') + '\n' + (result.stderr || '');
    const records = [];
    
    const tscRegex = /(.+?)\((\d+),(\d+)\):\s+error\s+(TS\d+):\s+(.+)/g;
    let match;

    while ((match = tscRegex.exec(output)) !== null) {
      const filePathStr = match[1];
      const file = path.isAbsolute(filePathStr) ? filePathStr : path.resolve(ROOT_DIR, filePathStr);
      const line = parseInt(match[2], 10);
      const column = parseInt(match[3], 10);
      const errorCode = match[4];
      const message = match[5];

      const fingerprint = `${file}::${errorCode}`;
      const id = crypto.createHash('sha256').update(`${file}::${line}::${message}`).digest('hex');

      records.push({
        id,
        fingerprint,
        source: 'TSC_ERRORS',
        sourceFile: 'tsc_errors.mjs',
        collectedAt: new Date().toISOString(),
        file,
        line,
        column,
        errorCode,
        message,
        severity: 'HIGH',
        domain: 'CORE',
        errorType: 'TYPE_ERROR',
        occurrences: 1,
        firstSeen: new Date().toISOString(),
        lastSeen: new Date().toISOString(),
        trend: 'NEW',
        urgencyScore: 60,
        autoHealCandidate: false,
        requiredExpertise: ['Dev'],
        rawPayload: match[0]
      });
    }

    return records;
  }
}

if (process.argv[1] === fileURLToPath(import.meta.url)) {
  const collector = new TscErrorsCollector();
  collector.collect().then(records => {
    collector.writeFindings('tsc_errors', records);
  }).catch(err => console.error(err));
}
