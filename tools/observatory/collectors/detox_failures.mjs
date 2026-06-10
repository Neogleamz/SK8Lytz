import { BaseCollector } from './BaseCollector.mjs';
import fs from 'fs';
import path from 'path';
import crypto from 'crypto';

export class DetoxFailuresCollector extends BaseCollector {
  async collect() {
    const detoxPaths = [
      path.resolve(process.cwd(), 'detox.json'),
      path.resolve(process.cwd(), 'artifacts/detox_report.json')
    ];
    let content = '';
    let usedPath = '';
    for (const p of detoxPaths) {
      if (fs.existsSync(p)) {
        content = fs.readFileSync(p, 'utf8');
        usedPath = p;
        break;
      }
    }

    const records = [];
    if (content) {
      try {
        const report = JSON.parse(content);
        const failures = Array.isArray(report) ? report.filter(r => r.status === 'failed') : (report.failures || []);
        
        for (const failure of failures) {
          const now = new Date().toISOString();
          const title = failure.title || failure.name || 'Unknown detox failure';
          const id = crypto.createHash('sha256').update(title).digest('hex');
          
          records.push({
            id,
            fingerprint: crypto.createHash('sha256').update(`DETOX:${title}`).digest('hex'),
            source: 'DETOX_E2E',
            collectedAt: now,
            file: usedPath,
            message: `Detox test failed: ${title}`,
            severity: 'HIGH',
            domain: 'UI',
            errorType: 'TEST_FAILURE',
            occurrences: 1,
            firstSeen: now,
            lastSeen: now,
            trend: 'NEW',
            urgencyScore: 50,
            autoHealCandidate: false,
            requiredExpertise: ['🔬 Blake'],
            rawPayload: failure
          });
        }
      } catch (e) {
        // parse error
      }
    }

    this.writeFindings('detox_failures', records);
  }
}

if (process.argv[1] && process.argv[1].endsWith('detox_failures.mjs')) {
  new DetoxFailuresCollector().collect().catch(console.error);
}
