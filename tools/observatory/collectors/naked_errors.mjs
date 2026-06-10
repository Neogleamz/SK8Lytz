import { BaseCollector } from './BaseCollector.mjs';
import { execSync } from 'child_process';
import crypto from 'crypto';
import path from 'path';

export class NakedErrorsCollector extends BaseCollector {
  async collect() {
    let output = '';
    try {
      // First try grep -rn as specified
      output = execSync('grep -rn "console.error" src/').toString();
    } catch (e) {
      if (e.stdout) {
        output = e.stdout.toString();
      } else {
        // Fallback to git grep if grep is not found
        try {
          output = execSync('git grep -n "console.error" src/').toString();
        } catch (err2) {
          if (err2.stdout) output = err2.stdout.toString();
        }
      }
    }
    
    const records = [];
    const lines = output.split('\n');
    for (const line of lines) {
      if (!line.trim() || line.includes('__DEV__')) continue;
      
      const match = line.match(/^([^:]+):(\d+):(.*)$/);
      if (match) {
        const [_, file, lineNum, message] = match;
        const now = new Date().toISOString();
        const msgStr = message.trim();
        const id = crypto.createHash('sha256').update(`${file}:${lineNum}:${msgStr}`).digest('hex');
        
        records.push({
          id,
          fingerprint: crypto.createHash('sha256').update(`${file}:PATTERN_VIOLATION:console.error`).digest('hex'),
          source: 'NAKED_CONSOLE_ERROR',
          collectedAt: now,
          file,
          line: parseInt(lineNum, 10),
          message: `Naked console.error found: ${msgStr}`,
          severity: 'LOW',
          domain: 'CORE',
          errorType: 'PATTERN_VIOLATION',
          occurrences: 1,
          firstSeen: now,
          lastSeen: now,
          trend: 'NEW',
          urgencyScore: 10,
          autoHealCandidate: true,
          requiredExpertise: ['⚒️ Sage'],
          rawPayload: line
        });
      }
    }
    this.writeFindings('naked_errors', records);
  }
}

if (process.argv[1] && process.argv[1].endsWith('naked_errors.mjs')) {
  new NakedErrorsCollector().collect().catch(console.error);
}
