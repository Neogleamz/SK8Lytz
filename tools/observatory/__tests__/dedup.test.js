const fs = require('fs');
const path = require('path');
const crypto = require('crypto');

describe('Deduplication Engine', () => {
  let DedupEngine;
  let dedup;

  beforeAll(() => {
    const code = fs.readFileSync(path.resolve(__dirname, '../action/dedup.mjs'), 'utf8')
      .replace(/export class/g, 'class')
      .replace(/import [^;]+;/g, '')
      + '\n;(() => DedupEngine)();';
    DedupEngine = eval(code);
  });

  beforeEach(() => {
    dedup = new DedupEngine();
  });

  test('Pass 1: Exact Match (file:line:message)', () => {
    const input = [
      { file: 'App.tsx', line: 10, message: 'Crash', occurrences: 1 },
      { file: 'App.tsx', line: 10, message: 'Crash', occurrences: 1 },
    ];
    const result = dedup.pass1ExactMatch(input);
    expect(result.length).toBe(1);
    expect(result[0].occurrences).toBe(2);
  });

  test('Pass 2: Fuzzy Match (file + 5 lines + errorCode)', () => {
    const input = [
      { id: '1', file: 'App.tsx', message: 'Unknown error', severity: 'LOW', occurrences: 1 },
      { id: '2', file: 'App.tsx', message: 'Unknown error', severity: 'HIGH', occurrences: 1 },
    ];
    const result = dedup.pass2FuzzyMatch(input);
    expect(result.length).toBe(1);
    expect(result[0].occurrences).toBe(2);
    expect(result[0].severity).toBe('HIGH');
  });

  test('Pass 3: Root Cause Clustering', () => {
    const input = [
      { id: '1', file: 'src/App.tsx', errorCode: 'TS2322' }
    ];
    const result = dedup.pass3RootCause(input);
    expect(result[0].rootCause).toBe('Cluster: TS2322');
  });

  test('Pass 4: False Positive Scrubbing', () => {
    const input = [
      { file: 'src/__tests__/App.test.tsx', source: 'TSC_ERRORS' },
      { file: 'src/__tests__/App.test.tsx', source: 'JEST_FAILURES' }
    ];
    const result = dedup.pass4Scrubbing(input);
    expect(result.length).toBe(1);
    expect(result[0].source).toBe('JEST_FAILURES');
  });
});
