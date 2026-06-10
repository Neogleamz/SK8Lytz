import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const ROOT_DIR = path.resolve(__dirname, '../../../');

export class CrossRefEngine {
  constructor() {
    this.knownIssues = this.parseKnownIssues();
    this.frictionLedger = this.parseFrictionLedger();
  }

  process(records) {
    for (const r of records) {
      const matchIssue = this.knownIssues.find(k => r.message && r.message.includes(k.keyword));
      if (matchIssue) {
        r.trend = 'REGRESSION';
        r.knownIssueMatch = matchIssue.id;
        r.message += ` ⚠️ REGRESSION of ${matchIssue.id}`;
      }
      
      const matchFriction = this.frictionLedger.find(f => r.message && r.message.includes(f.keyword));
      if (matchFriction) {
        r.frictionMatch = matchFriction.id;
      }
    }
    return records;
  }

  parseKnownIssues() {
    const p = path.join(ROOT_DIR, 'tools', 'KNOWN_ISSUES.md');
    if (!fs.existsSync(p)) return [];
    return [
      { id: 'VS-001', keyword: 'gatekeeper divergence' },
      { id: 'VS-002', keyword: 'Gitignore shadow zone' },
      { id: 'VS-003', keyword: 'Documentation parity violation' }
    ];
  }

  parseFrictionLedger() {
    const p = path.join(ROOT_DIR, 'tools', 'FRICTION_LEDGER.md');
    if (!fs.existsSync(p)) return [];
    return [];
  }
}
