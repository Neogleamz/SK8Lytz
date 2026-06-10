import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import crypto from 'crypto';
import { spawnSync } from 'child_process';
import { BaseCollector } from './BaseCollector.mjs';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const ROOT_DIR = path.resolve(__dirname, '../../../');
const HARVESTER_PATH = path.join(ROOT_DIR, 'tools', 'web-console-harvester.js');
const RESULTS_PATH = path.join(ROOT_DIR, '.system_generated', 'runtime-console-failures.json');

export class WebConsoleCollector extends BaseCollector {
  async collect() {
    spawnSync('node', [HARVESTER_PATH], {
      cwd: ROOT_DIR,
      stdio: 'ignore'
    });

    if (!fs.existsSync(RESULTS_PATH)) {
      return [];
    }

    let resultsData;
    try {
      resultsData = JSON.parse(fs.readFileSync(RESULTS_PATH, 'utf-8'));
    } catch (err) {
      return [];
    }

    if (!resultsData.errors || !Array.isArray(resultsData.errors)) {
      return [];
    }

    const records = [];

    for (const error of resultsData.errors) {
      const message = error.text || 'Unknown Error';
      const sourceKey = error.source || 'unknown';
      const fingerprint = `WEB_CONSOLE::${sourceKey}::${message.slice(0, 100)}`;
      const id = crypto.createHash('sha256').update(fingerprint).digest('hex');

      records.push({
        id,
        fingerprint,
        source: 'WEB_CONSOLE',
        sourceFile: 'web_console.mjs',
        collectedAt: new Date().toISOString(),
        file: 'WEB_RUNTIME',
        message,
        severity: 'HIGH',
        domain: 'UI',
        errorType: 'RUNTIME',
        occurrences: 1,
        firstSeen: new Date().toISOString(),
        lastSeen: new Date().toISOString(),
        trend: 'NEW',
        urgencyScore: 50,
        autoHealCandidate: false,
        requiredExpertise: ['Dev', 'UI'],
        rawPayload: error
      });
    }

    return records;
  }
}

if (process.argv[1] === fileURLToPath(import.meta.url)) {
  const collector = new WebConsoleCollector();
  collector.collect().then(records => {
    collector.writeFindings('web_console', records);
  }).catch(err => console.error(err));
}
