import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import crypto from 'crypto';
import { BaseCollector } from './BaseCollector.mjs';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const ROOT_DIR = path.resolve(__dirname, '../../../');
const JEST_RESULTS_PATH = path.join(ROOT_DIR, 'jest_results.json');

export class JestFailuresCollector extends BaseCollector {
  async collect() {
    if (!fs.existsSync(JEST_RESULTS_PATH)) {
      return [];
    }

    let jestData;
    try {
      jestData = JSON.parse(fs.readFileSync(JEST_RESULTS_PATH, 'utf-8'));
    } catch (err) {
      return [];
    }

    if (!jestData.testResults) {
      return [];
    }

    const records = [];

    for (const testSuite of jestData.testResults) {
      if (testSuite.status === 'failed') {
        const file = testSuite.name;
        for (const assertion of testSuite.assertionResults) {
          if (assertion.status === 'failed') {
            const message = assertion.failureMessages ? assertion.failureMessages.join('\n') : assertion.title;
            const functionName = assertion.ancestorTitles.join(' > ') + ' > ' + assertion.title;

            const fingerprint = `${file}::${functionName}::${message.slice(0, 100)}`;
            const id = crypto.createHash('sha256').update(fingerprint).digest('hex');

            records.push({
              id,
              fingerprint,
              source: 'JEST_FAILURES',
              sourceFile: 'jest_failures.mjs',
              collectedAt: new Date().toISOString(),
              file,
              functionName,
              message,
              severity: 'HIGH',
              domain: 'CORE',
              errorType: 'TEST_FAILURE',
              occurrences: 1,
              firstSeen: new Date().toISOString(),
              lastSeen: new Date().toISOString(),
              trend: 'NEW',
              urgencyScore: 50,
              autoHealCandidate: false,
              requiredExpertise: ['QA', 'Dev'],
              rawPayload: assertion
            });
          }
        }
      }
    }

    return records;
  }
}

if (process.argv[1] === fileURLToPath(import.meta.url)) {
  const collector = new JestFailuresCollector();
  collector.collect().then(records => {
    collector.writeFindings('jest_failures', records);
  }).catch(err => console.error(err));
}
