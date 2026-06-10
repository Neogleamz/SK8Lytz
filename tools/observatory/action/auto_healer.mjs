import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const ROOT_DIR = path.resolve(__dirname, '../../../');

export const AUTO_HEAL_LIBRARY = {
  'AH-001': {
    id: 'AH-001',
    description: 'Naked console.error bypassing AppLogger',
    confidence: 'HIGH',
    match: (error) => error.source === 'NAKED_CONSOLE_ERROR' || error.message.includes('console.error'),
    proposeFix: (error) => `Replace console.error with AppLogger.error(eventType, { context }) in ${error.file}`
  },
  'AH-002': {
    id: 'AH-002',
    description: 'as any cast in production code',
    confidence: 'MEDIUM',
    match: (error) => error.message.includes('as any') || error.errorType === 'PATTERN_VIOLATION',
    proposeFix: (error) => `Provide correct type signature in ${error.file}`
  },
  'AH-003': {
    id: 'AH-003',
    description: 'Missing useEffect dependency',
    confidence: 'MEDIUM',
    match: (error) => error.message.includes('React Hook useEffect has a missing dependency'),
    proposeFix: (error) => `Add missing dependencies to useEffect array in ${error.file}`
  },
  'AH-004': {
    id: 'AH-004',
    description: 'Dead import (imported but unused)',
    confidence: 'HIGH',
    match: (error) => error.errorCode === 'TS6133' && error.message.includes('is declared but its value is never read'),
    proposeFix: (error) => `Remove unused import in ${error.file}`
  },
  'AH-005': {
    id: 'AH-005',
    description: '__DEV__ not defined in test',
    confidence: 'HIGH',
    match: (error) => error.source === 'JEST_FAILURES' && error.message.includes('__DEV__ is not defined'),
    proposeFix: (error) => `Add (global as unknown as { __DEV__: boolean }).__DEV__ = true; to test setup in ${error.file}`
  },
  'AH-006': {
    id: 'AH-006',
    description: 'Jest timer leak (setInterval not cleaned)',
    confidence: 'HIGH',
    match: (error) => error.source === 'JEST_FAILURES' && (error.message.includes('timer') || error.message.includes('A worker process has failed to exit')),
    proposeFix: (error) => `Add jest.advanceTimersByTime() + flushAsyncQueue() pattern in ${error.file}`
  },
  'AH-007': {
    id: 'AH-007',
    description: 'AppLogger ENOBUFS in test',
    confidence: 'HIGH',
    match: (error) => error.source === 'JEST_FAILURES' && error.message.includes('ENOBUFS'),
    proposeFix: (error) => `Add AppLogger silence mock to test setup in ${error.file}`
  },
  'AH-008': {
    id: 'AH-008',
    description: 'Missing ProGuard -keep for native module',
    confidence: 'HIGH',
    match: (error) => error.source === 'ADB_LOGCAT' && error.message.includes('ClassNotFoundException'),
    proposeFix: (error) => `Add keep directive to android/app/proguard-rules.pro`
  },
  'AH-009': {
    id: 'AH-009',
    description: 'Stale KNOWN_ISSUES reference',
    confidence: 'LOW',
    match: (error) => error.source === 'KNOWN_ISSUES' && error.trend === 'RESOLVED',
    proposeFix: (error) => `Update VS-XXX entry status in tools/KNOWN_ISSUES.md`
  }
};

export class AutoHealer {
  evaluate(error) {
    for (const pattern of Object.values(AUTO_HEAL_LIBRARY)) {
      if (pattern.match(error)) {
        if (pattern.confidence === 'HIGH' && error.domain !== 'BLE' && error.domain !== 'CORE') {
          error.autoHealCandidate = true;
          error.proposedFix = `[🔧 AUTO-HEAL PROPOSED: ${pattern.id}] ` + pattern.proposeFix(error);
          return pattern;
        } else {
          // Add proposed fix but do not flag for direct auto heal
          error.proposedFix = pattern.proposeFix(error);
          return pattern;
        }
      }
    }
    return null;
  }

  processAnalyzedRecords(dateStr = new Date().toISOString().split('T')[0]) {
    const processedDir = path.join(ROOT_DIR, 'tools', 'observatory', 'processed');
    const analyzedFile = path.join(processedDir, `${dateStr}_analyzed.json`);
    
    if (!fs.existsSync(analyzedFile)) return [];

    const data = JSON.parse(fs.readFileSync(analyzedFile, 'utf-8'));
    
    for (const record of data) {
      this.evaluate(record);
    }

    fs.writeFileSync(analyzedFile, JSON.stringify(data, null, 2));
    console.log(`[AutoHealer] Processed auto-heal evaluation for ${data.length} records.`);
    return data;
  }
}

if (process.argv[1] === fileURLToPath(import.meta.url)) {
  const healer = new AutoHealer();
  healer.processAnalyzedRecords();
}
