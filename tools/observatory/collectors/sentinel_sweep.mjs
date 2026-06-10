import { BaseCollector } from './BaseCollector.mjs';
import fs from 'fs';
import path from 'path';
import crypto from 'crypto';
import { execSync } from 'child_process';

export class SentinelSweepCollector extends BaseCollector {
  async collect() {
    const sentinelDir = path.resolve(process.cwd(), 'tools/sentinel');
    const records = [];
    
    if (fs.existsSync(sentinelDir)) {
      const files = fs.readdirSync(sentinelDir);
      for (const file of files) {
        if (file.endsWith('.js') || file.endsWith('.py')) {
          const cmd = file.endsWith('.js') ? `node ${path.join(sentinelDir, file)}` : `python ${path.join(sentinelDir, file)}`;
          try {
            const output = execSync(cmd, { stdio: 'pipe' }).toString();
            const lines = output.split('\n').filter(l => l.trim());
            for (const line of lines) {
               const now = new Date().toISOString();
               const id = crypto.createHash('sha256').update(`${file}:${line}`).digest('hex');
               records.push({
                 id,
                 fingerprint: crypto.createHash('sha256').update(`SENTINEL:${file}`).digest('hex'),
                 source: 'SENTINEL',
                 collectedAt: now,
                 file: `tools/sentinel/${file}`,
                 message: `Sentinel finding: ${line.substring(0, 100)}`,
                 severity: 'MEDIUM',
                 domain: 'CORE',
                 errorType: 'PATTERN_VIOLATION',
                 occurrences: 1,
                 firstSeen: now,
                 lastSeen: now,
                 trend: 'NEW',
                 urgencyScore: 15,
                 autoHealCandidate: false,
                 requiredExpertise: ['🔬 Blake'],
                 rawPayload: line
               });
            }
          } catch (e) {
            const now = new Date().toISOString();
            const id = crypto.createHash('sha256').update(`${file}:exec_failed`).digest('hex');
            records.push({
               id,
               fingerprint: crypto.createHash('sha256').update(`SENTINEL:${file}:crash`).digest('hex'),
               source: 'SENTINEL',
               collectedAt: now,
               file: `tools/sentinel/${file}`,
               message: `Sentinel script execution failed: ${e.message}`,
               severity: 'HIGH',
               domain: 'CORE',
               errorType: 'CRASH',
               occurrences: 1,
               firstSeen: now,
               lastSeen: now,
               trend: 'NEW',
               urgencyScore: 40,
               autoHealCandidate: false,
               requiredExpertise: ['🔬 Blake'],
               rawPayload: e.stdout ? e.stdout.toString() : e.message
            });
          }
        }
      }
    }

    this.writeFindings('sentinel_sweep', records);
  }
}

if (process.argv[1] && process.argv[1].endsWith('sentinel_sweep.mjs')) {
  new SentinelSweepCollector().collect().catch(console.error);
}
