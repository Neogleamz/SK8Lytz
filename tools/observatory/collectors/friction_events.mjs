import { BaseCollector } from './BaseCollector.mjs';
import fs from 'fs';
import path from 'path';
import crypto from 'crypto';

export class FrictionEventsCollector extends BaseCollector {
  async collect() {
    const ledgerPath = path.resolve(process.cwd(), 'tools/FRICTION_LEDGER.md');
    let content = '';
    try {
      content = fs.readFileSync(ledgerPath, 'utf8');
    } catch (e) {}

    const records = [];
    const issueRegex = /###\s+\[(FRICTION-\d{3})\]\s+([^\n]+)/g;
    let match;
    while ((match = issueRegex.exec(content)) !== null) {
      const frictionId = match[1];
      const title = match[2];
      const now = new Date().toISOString();
      const id = crypto.createHash('sha256').update(`${frictionId}:${title}`).digest('hex');

      records.push({
        id,
        fingerprint: crypto.createHash('sha256').update(`FRICTION:${frictionId}`).digest('hex'),
        source: 'FRICTION_LEDGER',
        collectedAt: now,
        file: 'tools/FRICTION_LEDGER.md',
        message: `${frictionId}: ${title}`,
        severity: 'MEDIUM',
        domain: 'CORE',
        errorType: 'OPERATIONAL',
        occurrences: 1,
        firstSeen: now,
        lastSeen: now,
        trend: 'RECURRING',
        urgencyScore: 20,
        autoHealCandidate: false,
        requiredExpertise: ['🕵️ Reyes', '📋 Scrum'],
        rawPayload: match[0]
      });
    }

    this.writeFindings('friction_events', records);
  }
}

if (process.argv[1] && process.argv[1].endsWith('friction_events.mjs')) {
  new FrictionEventsCollector().collect().catch(console.error);
}
