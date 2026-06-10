import { BaseCollector } from './BaseCollector.mjs';
import fs from 'fs';
import path from 'path';
import crypto from 'crypto';

export class KnownIssuesCollector extends BaseCollector {
  async collect() {
    const knownIssuesPath = path.resolve(process.cwd(), 'tools/KNOWN_ISSUES.md');
    let content = '';
    try {
      content = fs.readFileSync(knownIssuesPath, 'utf8');
    } catch (e) {
      // file might not exist
    }

    const records = [];
    // Extract VS-XXX patterns
    const issueRegex = /###\s+(VS-\d{3}):?\s+([^\n]+)/g;
    let match;
    while ((match = issueRegex.exec(content)) !== null) {
      const vsId = match[1];
      const title = match[2];
      const now = new Date().toISOString();
      const id = crypto.createHash('sha256').update(`${vsId}:${title}`).digest('hex');

      records.push({
        id,
        fingerprint: crypto.createHash('sha256').update(`KNOWN_ISSUE:${vsId}`).digest('hex'),
        source: 'KNOWN_ISSUES',
        collectedAt: now,
        file: 'tools/KNOWN_ISSUES.md',
        message: `${vsId}: ${title}`,
        severity: 'MEDIUM',
        domain: 'CORE',
        errorType: 'PATTERN_VIOLATION',
        occurrences: 1,
        firstSeen: now,
        lastSeen: now,
        trend: 'RECURRING',
        urgencyScore: 20,
        autoHealCandidate: false,
        requiredExpertise: ['🔬 Blake'],
        rawPayload: match[0]
      });
    }

    this.writeFindings('known_issues', records);
  }
}

if (process.argv[1] && process.argv[1].endsWith('known_issues.mjs')) {
  new KnownIssuesCollector().collect().catch(console.error);
}
