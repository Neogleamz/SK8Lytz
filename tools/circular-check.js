const fs = require('fs');
const path = require('path');

const servicesDir = path.join(__dirname, '../src/services');
const importMap = {};
let fileCount = 0;

function getAllTsFiles(dir) {
  const files = [];
  function walk(d) {
    const entries = fs.readdirSync(d);
    entries.forEach(entry => {
      const fullPath = path.join(d, entry);
      const stat = fs.statSync(fullPath);
      if (stat.isDirectory()) {
        walk(fullPath);
      } else if (entry.endsWith('.ts') && !entry.includes('.test') && !entry.includes('.types')) {
        files.push(fullPath);
      }
    });
  }
  walk(dir);
  return files;
}

function extractImports(filePath) {
  const content = fs.readFileSync(filePath, 'utf8');
  const regex = /import\s+.*?from\s+['"]([^'"]+)['"]/g;
  const imports = [];
  let match;
  while ((match = regex.exec(content)) !== null) {
    imports.push(match[1]);
  }
  return imports;
}

function resolveImport(fromFile, importPath) {
  const baseDir = path.dirname(fromFile);
  let resolved = path.normalize(path.join(baseDir, importPath));

  // Try different extensions
  const exts = ['.ts', '/index.ts', '.tsx', '/index.tsx'];
  for (const ext of exts) {
    const candidate = resolved + ext;
    if (fs.existsSync(candidate)) {
      return path.resolve(candidate);
    }
  }

  // Also try without adding extension if it's already a path
  if (fs.existsSync(resolved)) {
    return path.resolve(resolved);
  }

  return null;
}

// Build import map
const files = getAllTsFiles(servicesDir);
files.forEach(file => {
  const normalized = path.resolve(file);
  const imports = extractImports(file);
  importMap[normalized] = imports;
  fileCount++;
});

console.log(`Scanned ${fileCount} service files\n`);

// Find cycles
const cycles = [];
const visited = new Set();
const recursionStack = new Set();

function hasCycle(file, path1 = []) {
  if (recursionStack.has(file)) {
    return true;
  }
  if (visited.has(file)) {
    return false;
  }

  visited.add(file);
  recursionStack.add(file);

  if (importMap[file]) {
    for (const importPath of importMap[file]) {
      const resolved = resolveImport(file, importPath);
      if (resolved && resolved !== file && importMap[resolved]) {
        if (hasCycle(resolved, [...path1, file])) {
          return true;
        }
      }
    }
  }

  recursionStack.delete(file);
  return false;
}

// Check for direct cycles only (simpler detection)
// Skip self-imports (where file imports itself via re-export)
const findings = [];
for (const file of Object.keys(importMap)) {
  for (const importStr of importMap[file]) {
    const resolved = resolveImport(file, importStr);
    if (resolved && resolved !== file && importMap[resolved]) {
      // Check if resolved imports back to file
      for (const revImport of importMap[resolved]) {
        const revResolved = resolveImport(resolved, revImport);
        if (revResolved === file) {
          findings.push({
            file1: file.replace(servicesDir, '.'),
            imports: importStr,
            file2: resolved.replace(servicesDir, '.'),
            importsBack: revImport
          });
        }
      }
    }
  }
}

if (findings.length > 0) {
  console.log('⚠️  CIRCULAR DEPENDENCIES DETECTED:\n');
  findings.forEach(f => {
    console.log(`${f.file1}`);
    console.log(`  └─> imports: ${f.imports}`);
    console.log(`${f.file2}`);
    console.log(`  └─> imports back: ${f.importsBack}\n`);
  });
  process.exit(1);
} else {
  console.log('✅ No circular dependencies detected');
  process.exit(0);
}
