import { BaseCollector } from './BaseCollector.mjs';
import fs from 'fs';
import path from 'path';
import crypto from 'crypto';

export class SessionErrorsCollector extends BaseCollector {
  async collect() {
    const logPath = path.resolve(process.cwd(), 'tools/SESSION_LOG.md');
    let content = '';
    try {
      content = fs.readFileSync(logPath, 'utf8');
    } catch (e) {}

    const records = [];
    const entries = content.split(/(?=###\s+\[)/).filter(Boolean);
    const last10 = entries.slice(-10);

    const errorKeywords = ['error', 'crash', 'fail', 'bug'];
    
    for (const entry of last10) {
      if (entry.includes('[DECISION]') || entry.includes('[EVENT]')) {
        const lowerEntry = entry.toLowerCase();
        if (errorKeywords.some(kw => lowerEntry.includes(kw))) {
          const now = new Date().toISOString();
          const firstLine = entry.split('\n')[0].trim();
          const id = crypto.createHash('sha256').update(entry).digest('hex');
          
          records.push({
            id,
            fingerprint: crypto.createHash('sha256').update(`SESSION_LOG:${firstLine}`).digest('hex'),
            source: 'SESSION_LOG',
            collectedAt: now,
            file: 'tools/SESSION_LOG.md',
            message: `Error keyword found in session log: ${firstLine}`,
            severity: 'LOW',
            domain: 'CORE',
            errorType: 'OPERATIONAL',
            occurrences: 1,
            firstSeen: now,
            lastSeen: now,
            trend: 'NEW',
            urgencyScore: 10,
            autoHealCandidate: false,
            requiredExpertise: ['🕵️ Reyes'],
            rawPayload: entry
          });
        }
      }
    }

    this.writeFindings('session_errors', records);
  }
}

if (process.argv[1] && process.argv[1].endsWith('session_errors.mjs')) {
  new SessionErrorsCollector().collect().catch(console.error);
}
