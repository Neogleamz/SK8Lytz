import crypto from 'crypto';

function generateHash(str) {
  return crypto.createHash('sha256').update(str).digest('hex');
}

export class DedupEngine {
  constructor() {
    this.exactMap = new Map();
    this.fuzzyMap = new Map();
    this.rootCauseMap = new Map();
  }

  process(records) {
    const scrubbed = this.pass4Scrubbing(records);
    const exactMerged = this.pass1ExactMatch(scrubbed);
    const fuzzyMerged = this.pass2FuzzyMatch(exactMerged);
    const clustered = this.pass3RootCause(fuzzyMerged);
    return clustered;
  }

  pass1ExactMatch(records) {
    for (const r of records) {
      const key = `${r.file}:${r.line}:${r.message}`;
      const id = generateHash(key);
      if (this.exactMap.has(id)) {
        const existing = this.exactMap.get(id);
        existing.occurrences += (r.occurrences || 1);
        existing.lastSeen = r.collectedAt > existing.lastSeen ? r.collectedAt : existing.lastSeen;
      } else {
        r.id = id;
        r.occurrences = r.occurrences || 1;
        this.exactMap.set(id, r);
      }
    }
    return Array.from(this.exactMap.values());
  }

  pass2FuzzyMatch(records) {
    for (const r of records) {
      const msgPrefix = r.message ? r.message.substring(0, 30) : '';
      const fingerprint = `${r.file}:${r.errorCode || msgPrefix}`;
      r.fingerprint = generateHash(fingerprint);

      if (this.fuzzyMap.has(r.fingerprint)) {
        const existing = this.fuzzyMap.get(r.fingerprint);
        existing.occurrences += r.occurrences;
        if (!existing.relatedErrors) existing.relatedErrors = [];
        existing.relatedErrors.push(r.id);
        if (this.severityToNum(r.severity) > this.severityToNum(existing.severity)) {
          existing.severity = r.severity;
          existing.message = r.message;
        }
      } else {
        this.fuzzyMap.set(r.fingerprint, r);
      }
    }
    return Array.from(this.fuzzyMap.values());
  }

  pass3RootCause(records) {
    for (const r of records) {
      const rootCause = r.errorCode ? `Cluster: ${r.errorCode}` : `Cluster: ${pathBasename(r.file)}`;
      r.rootCause = rootCause;
      this.rootCauseMap.set(r.id, r);
    }
    return Array.from(this.rootCauseMap.values());
  }

  pass4Scrubbing(records) {
    return records.filter(r => {
      if (r.file && (r.file.includes('__tests__') || r.file.includes('.test.'))) {
        if (r.source !== 'JEST_FAILURES') return false; 
      }
      return true;
    });
  }

  severityToNum(sev) {
    switch(sev) {
      case 'CRITICAL': return 100;
      case 'HIGH': return 75;
      case 'MEDIUM': return 50;
      case 'LOW': return 25;
      case 'INFO': return 10;
      default: return 0;
    }
  }
}

function pathBasename(p) {
  if (!p) return 'Unknown';
  const parts = p.split(/[/\\]/);
  return parts[parts.length - 1];
}
