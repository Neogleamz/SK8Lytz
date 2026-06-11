#!/usr/bin/env node
/**
 * tools/ast-parser.js
 * Reusable AST import-tree analyzer for parallel wave collision detection.
 *
 * Usage:
 *   node tools/ast-parser.js --files src/services/AppLogger.ts src/context/SessionContext.tsx
 *   node tools/ast-parser.js --domain-map <domain_clusters.json>
 *   node tools/ast-parser.js --collision-matrix <domain_clusters.json>
 *
 * Output: JSON to stdout
 */

const fs = require('fs');
const path = require('path');

const ROOT = path.resolve(__dirname, '../src');
const IMPORT_RE = /from\s+['"]([^'"]+)['"]/g;

function getAllTsFiles(dir) {
  let results = [];
  if (!fs.existsSync(dir)) return results;
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    const full = path.join(dir, entry.name);
    if (entry.isDirectory()) {
      results = results.concat(getAllTsFiles(full));
    } else if (entry.name.match(/\.(ts|tsx)$/) && !entry.name.includes('.d.ts')) {
      results.push(full.replace(/\\/g, '/'));
    }
  }
  return results;
}

function resolveImport(fromFile, importPath) {
  if (!importPath.startsWith('.')) return null;
  const base = path.dirname(fromFile);
  const resolved = path.resolve(base, importPath).replace(/\\/g, '/');
  const extensions = ['', '.ts', '.tsx', '/index.ts', '/index.tsx'];
  for (const ext of extensions) {
    const candidate = resolved + ext;
    if (fs.existsSync(candidate)) return candidate;
  }
  return null;
}

function buildImportGraph() {
  const allFiles = getAllTsFiles(ROOT);
  // Maps file -> set of files it imports
  const imports = {};
  // Maps file -> set of files that import it (reverse index)
  const importedBy = {};

  for (const file of allFiles) {
    imports[file] = new Set();
    importedBy[file] = new Set();
  }

  for (const file of allFiles) {
    let content;
    try { content = fs.readFileSync(file, 'utf-8'); } catch { continue; }
    let match;
    IMPORT_RE.lastIndex = 0;
    while ((match = IMPORT_RE.exec(content)) !== null) {
      const resolved = resolveImport(file, match[1]);
      if (resolved && imports[resolved] !== undefined) {
        imports[file].add(resolved);
        importedBy[resolved].add(file);
      }
    }
  }
  return { imports, importedBy };
}

function computeCollisionMatrix(domainClusters) {
  const { imports, importedBy } = buildImportGraph();

  // Build file -> domain index
  const fileToDomain = {};
  for (const [domain, items] of Object.entries(domainClusters)) {
    for (const item of items) {
      const fp = item.file ? path.resolve(__dirname, '..', item.file).replace(/\\/g, '/') : null;
      if (fp) fileToDomain[fp] = domain;
    }
  }

  const domains = Object.keys(domainClusters);
  const adjacency = {};
  for (const d of domains) adjacency[d] = new Set();

  // For each file in domain A: if it imports a file in domain B -> collision
  for (const [file, domainA] of Object.entries(fileToDomain)) {
    const importsOfFile = imports[file] || new Set();
    for (const imported of importsOfFile) {
      const domainB = fileToDomain[imported];
      if (domainB && domainB !== domainA) {
        adjacency[domainA].add(domainB);
        adjacency[domainB].add(domainA);
      }
    }
  }

  // Greedy graph coloring -> wave assignment
  const color = {};
  for (const d of domains) {
    const neighborColors = new Set([...adjacency[d]].filter(n => color[n]).map(n => color[n]));
    let wave = 1;
    while (neighborColors.has(wave)) wave++;
    color[d] = wave;
  }

  // Group by wave
  const waves = {};
  for (const [d, w] of Object.entries(color)) {
    if (!waves[w]) waves[w] = [];
    waves[w].push(d);
  }

  // Enforce swarm cap of 8 per wave
  const finalWaves = {};
  let waveOffset = 0;
  for (const waveNum of Object.keys(waves).sort((a, b) => a - b)) {
    const tasks = waves[waveNum];
    for (let i = 0; i < tasks.length; i += 8) {
      const actualWave = parseInt(waveNum) + waveOffset;
      finalWaves[actualWave] = tasks.slice(i, i + 8);
      if (i + 8 < tasks.length) waveOffset++;
    }
  }

  const collisionPairs = [];
  const seen = new Set();
  for (const [d, neighbors] of Object.entries(adjacency)) {
    for (const n of neighbors) {
      const key = [d, n].sort().join('|');
      if (!seen.has(key)) { seen.add(key); collisionPairs.push([d, n]); }
    }
  }

  return {
    collision_pairs: collisionPairs,
    wave_assignments: color,
    waves: finalWaves,
    total_waves: Object.keys(finalWaves).length,
    total_collisions: collisionPairs.length,
  };
}

// --- CLI Entry Point ---
const args = process.argv.slice(2);
const mode = args[0];

if (mode === '--collision-matrix') {
  const clusterFile = args[1];
  if (!clusterFile || !fs.existsSync(clusterFile)) {
    console.error('Usage: node tools/ast-parser.js --collision-matrix <domain_clusters.json>');
    process.exit(1);
  }
  const clusters = JSON.parse(fs.readFileSync(clusterFile, 'utf-8'));
  const result = computeCollisionMatrix(clusters);
  console.log(JSON.stringify(result, null, 2));

} else if (mode === '--files') {
  // Single file import analysis
  const { imports, importedBy } = buildImportGraph();
  const targetFiles = args.slice(1).map(f => path.resolve(f).replace(/\\/g, '/'));
  const result = {};
  for (const f of targetFiles) {
    result[f] = {
      imports: [...(imports[f] || [])],
      imported_by: [...(importedBy[f] || [])],
    };
  }
  console.log(JSON.stringify(result, null, 2));

} else {
  console.log(JSON.stringify({
    usage: [
      'node tools/ast-parser.js --files <file1> <file2>',
      'node tools/ast-parser.js --collision-matrix <domain_clusters.json>',
    ]
  }, null, 2));
}
