const fs = require('fs');
const path = require('path');

describe('Urgency Scoring Algorithm', () => {
  let ScoringEngine;
  let scoring;

  beforeAll(() => {
    const code = fs.readFileSync(path.resolve(__dirname, '../action/scoring.mjs'), 'utf8')
      .replace(/export class/g, 'class')
      .replace(/import [^;]+;/g, '')
      + '\n;(() => ScoringEngine)();';
    ScoringEngine = eval(code);
  });

  beforeEach(() => {
    scoring = new ScoringEngine();
  });

  test('calculates correct score based on severity weight (30%)', () => {
    expect(scoring.scoreSeverity('CRITICAL')).toBe(100);
  });

  test('calculates correct score based on frequency weight (20%)', () => {
    expect(scoring.scoreFrequency(50)).toBe(80);
  });

  test('calculates correct score based on trend weight (20%)', () => {
    expect(scoring.scoreTrend('REGRESSION')).toBe(100);
  });

  test('calculates correct score based on domain weight (15%)', () => {
    expect(scoring.scoreDomain('BLE')).toBe(90);
  });

  test('calculates correct score based on user impact weight (15%)', () => {
    expect(scoring.scoreImpact('CRASH')).toBe(100);
  });

  test('calculates composite score', () => {
    const error = { severity: 'CRITICAL', occurrences: 100, trend: 'REGRESSION', domain: 'BLE', errorType: 'CRASH' };
    const score = scoring.calculateScore(error);
    expect(score).toBeGreaterThan(90);
  });
});
