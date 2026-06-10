const fs = require('fs');
const path = require('path');
const crypto = require('crypto');

describe('Task Generator', () => {
  let TaskGenerator;
  let generator;

  beforeAll(() => {
    const code = fs.readFileSync(path.resolve(__dirname, '../action/task_generator.mjs'), 'utf8')
      .replace(/export class/g, 'class')
      .replace(/import [^;]+;/g, '')
      + '\n;(() => TaskGenerator)();';
    TaskGenerator = eval(code);
  });

  beforeEach(() => {
    generator = new TaskGenerator();
  });

  test('formats markdown string strictly according to SK8Lytz Kanban Schema', () => {
    const cluster = { rootCause: 'Cluster: TS1001', urgencyScore: 80, domain: 'CORE', message: 'Test error', file: 'Test.ts' };
    const block = generator.generateKanbanBlock(cluster);
    expect(block).toContain('- [ ] **`fix/cluster-ts1001`**');
    expect(block).toContain('**Goal:** Resolve error cluster: Cluster: TS1001');
    expect(block).toContain('**Tags:** `[✅ READY]` `[✅ VERIFIED]` `[CORE]` `[⚠️ H-RISK]` `[🍱 Meal]`');
  });

  test('assigns correct tags based on cluster attributes', () => {
    const cluster = { urgencyScore: 95, domain: 'BLE' };
    const tags = generator.generateTags(cluster);
    expect(tags.risk).toBe('⚠️ H-RISK');
    expect(tags.size).toBe('🥩 Feast');
    expect(tags.domain).toBe('BLE');
  });

  test('generates deterministic slug for error cluster', () => {
    const slug = generator.generateSlug('Cluster: Crash in App.tsx');
    expect(slug).toBe('cluster-crash-in-app-tsx');
  });
});
