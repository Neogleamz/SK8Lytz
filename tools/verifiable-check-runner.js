const { execSync } = require('child_process');
const fs = require('fs');
const path = require('path');
const crypto = require('crypto');

const args = process.argv.slice(2);
const isVerifyMode = args.includes('--verify');

// Resolve project roots and git paths
const WORKTREE_ROOT = process.cwd();
let gitCommonDir;
try {
  gitCommonDir = execSync('git rev-parse --git-common-dir', { stdio: 'pipe', encoding: 'utf8' }).trim();
} catch (e) {
  console.error('❌ Error: Must be run inside a Git repository.');
  process.exit(1);
}

const gitDir = path.resolve(gitCommonDir);
const saltPath = path.join(gitDir, 'attestation-salt');
const attestationPath = path.join(WORKTREE_ROOT, '.test-attestation.json');

// Ensure attestation salt exists in the shared .git repository
if (!fs.existsSync(saltPath)) {
  const newSalt = crypto.randomBytes(32).toString('hex');
  fs.writeFileSync(saltPath, newSalt, 'utf8');
}
const salt = fs.readFileSync(saltPath, 'utf8').trim();

// -------------------------------------------------------------
// VERIFICATION MODE (--verify)
// -------------------------------------------------------------
if (isVerifyMode) {
  console.log('🔒 Verifying QA Test Attestation...');
  
  if (!fs.existsSync(attestationPath)) {
    console.error('❌ Error: No test attestation file (.test-attestation.json) found.');
    console.error('👉 Run "npm run verify" to execute tests and generate a valid attestation.');
    process.exit(1);
  }

  let attestation;
  try {
    attestation = JSON.parse(fs.readFileSync(attestationPath, 'utf8'));
  } catch (e) {
    console.error('❌ Error: Failed to parse .test-attestation.json.');
    process.exit(1);
  }

  const { commit, timestamp, tscStatus, jestStatus, browserConsoleStatus, astStatus, typeSafetyStatus, stdoutHash, signature } = attestation;

  // 1. Recalculate signature
  const dataToSign = `${commit}:${timestamp}:${tscStatus}:${jestStatus}:${browserConsoleStatus || 'FAILED'}:${astStatus || 'FAILED'}:${typeSafetyStatus || 'FAILED'}:${stdoutHash}`;
  const expectedSignature = crypto.createHmac('sha256', salt).update(dataToSign).digest('hex');

  if (signature !== expectedSignature) {
    console.error('🚨 Cryptographic Tampering Detected! Attestation signature is invalid.');
    process.exit(1);
  }

  // 2. Verify status values
  if (tscStatus !== 'SUCCESS' || jestStatus !== 'SUCCESS' || browserConsoleStatus !== 'SUCCESS' || astStatus !== 'SUCCESS' || (typeSafetyStatus && typeSafetyStatus !== 'SUCCESS')) {
    console.error('❌ Error: Stored attestation indicates failed checks (TSC/Jest/BrowserConsole/AST/TypeSafety).');
    process.exit(1);
  }

  // 3. Verify commit correlation
  let currentCommit;
  try {
    currentCommit = execSync('git rev-parse HEAD', { stdio: 'pipe', encoding: 'utf8' }).trim();
  } catch (e) {
    console.error('❌ Failed to retrieve current commit hash.');
    process.exit(1);
  }

  if (commit !== currentCommit) {
    console.error('❌ Error: Attestation is anchored to a different commit.');
    console.error(`👉 Attested: ${commit.substring(0, 7)}`);
    console.error(`👉 Current:  ${currentCommit.substring(0, 7)}`);
    console.error('👉 Please run "npm run verify" to test the current commit.');
    process.exit(1);
  }

  // 4. Verify freshness (Max 15 minutes)
  const ageMs = Date.now() - Date.parse(timestamp);
  const maxAgeMs = 15 * 60 * 1000;
  if (ageMs > maxAgeMs) {
    console.error(`❌ Error: Attestation is stale (${Math.round(ageMs / 1000 / 60)} minutes old).`);
    console.error('👉 Run "npm run verify" to generate a fresh attestation.');
    process.exit(1);
  }

  console.log('✅ Attestation Verified: Cryptographically authentic, fresh, and matching commit!');
  process.exit(0);
}

// -------------------------------------------------------------
// EXECUTION & RUNNER MODE
// -------------------------------------------------------------
console.log('🚀 Starting Unified Verifiable QA Suite...');

// Ensure clean status or warn
const gitStatus = execSync('git status --short', { encoding: 'utf8' }).trim();
if (gitStatus !== '') {
  console.log('⚠️ Warning: You have uncommitted changes. Attestation will be anchored to the HEAD commit.');
}

const currentCommit = execSync('git rev-parse HEAD', { encoding: 'utf8' }).trim();
const timestamp = new Date().toISOString();

// Ensure node_modules directory junction exists if inside a worktree.
// fortressRoot is resolved to an absolute path so the mklink target is always correct
// regardless of whether gitCommonDir is returned as a relative or absolute path.
let fortressRoot = WORKTREE_ROOT;
if (gitCommonDir !== '.git') {
  // Resolve to absolute path — gitCommonDir can be relative (e.g. "../../SK8Lytz/.git")
  // which makes path.dirname() produce a wrong fortressRoot when used as an mklink target.
  fortressRoot = path.resolve(gitCommonDir, '..');
  const nodeModulesTarget = path.join(WORKTREE_ROOT, 'node_modules');
  if (!fs.existsSync(nodeModulesTarget)) {
    console.log('[Verify] Linking node_modules directory junction...');
    try {
      execSync(`cmd.exe /c "mklink /j "${nodeModulesTarget}" "${path.join(fortressRoot, 'node_modules')}""`);
      console.log('[Verify] node_modules junction created.');
    } catch (junctionErr) {
      // "Cannot create a file when that file already exists" — junction was created between
      // the existsSync check and the mklink call (race), or it is a real directory.
      // Either way the junction is present; log and continue rather than crashing the suite.
      console.log('[Verify] node_modules junction already present — skipping relink.');
    }
  }
}

let tscOutput = '';
let tscStatus = 'FAILED';
const tscStart = Date.now();
console.log('⏳ Running TypeScript compiler check (noEmit)...');
try {
  tscOutput = execSync('npx tsc --noEmit', { stdio: 'pipe', encoding: 'utf8' });
  tscStatus = 'SUCCESS';
  console.log('✅ TypeScript compiled clean!');
} catch (e) {
  tscOutput = e.stdout || e.message;
  console.error('❌ TypeScript compilation FAILED:');
  console.error(tscOutput);
}

let jestOutput = '';
let jestStatus = 'FAILED';
const jestStart = Date.now();
console.log('⏳ Running Jest unit tests...');
try {
  jestOutput = execSync('npx jest --watchAll=false --passWithNoTests', { stdio: 'pipe', encoding: 'utf8' });
  jestStatus = 'SUCCESS';
  console.log('✅ All Unit Tests Passed!');
} catch (e) {
  jestOutput = e.stdout || e.message;
  console.error('❌ Jest Unit Tests FAILED:');
  console.error(jestOutput);
}

let browserConsoleOutput = '';
let browserConsoleStatus = 'FAILED';
const browserConsoleStart = Date.now();
console.log('⏳ Running Headless Browser Console Quality Gate...');
try {
  browserConsoleOutput = execSync('node tools/web-console-harvester.js', { stdio: 'pipe', encoding: 'utf8' });
  browserConsoleStatus = 'SUCCESS';
  console.log('✅ Browser console logs are clean!');
} catch (e) {
  browserConsoleOutput = e.stdout || e.message;
  console.error('❌ Browser console validation FAILED:');
  console.error(browserConsoleOutput);
}

let astOutput = '';
let astStatus = 'FAILED';
const astStart = Date.now();
console.log('⏳ Running Static Code Quality Guards for OP_0x59...');
try {
  astOutput = execSync('node tools/sentinel/ast_knowledge_compiler.js --check', { stdio: 'pipe', encoding: 'utf8' });
  astStatus = 'SUCCESS';
  console.log('✅ Static OP_0x59 guards passed clean!');
} catch (e) {
  astOutput = e.stdout || e.message;
  console.error('❌ Static OP_0x59 validation FAILED:');
  console.error(astOutput);
}

let typeSafetyOutput = '';
let typeSafetyStatus = 'FAILED';
const typeSafetyStart = Date.now();
console.log('⏳ Running Production Type Safety Guard (no as any)...');
try {
  // Scan all production .ts/.tsx files (exclude __tests__ directories and .d.ts files)
  const srcDir = path.join(WORKTREE_ROOT, 'src');
  const FORBIDDEN_PATTERNS = [/ as any[^[]/, /as any$/, /as any,/, /as any\)/, /as any;/, /<any>/];
  const WHITELIST_TOKENS = ['// MIGRATION-SHIM', '// eslint-disable', 'as unknown as', '// TYPE-OVERRIDE:'];

  function scanDir(dir) {
    const entries = fs.readdirSync(dir, { withFileTypes: true });
    const violations = [];
    for (const entry of entries) {
      const fullPath = path.join(dir, entry.name);
      if (entry.isDirectory()) {
        if (entry.name === '__tests__' || entry.name === 'node_modules') continue;
        violations.push(...scanDir(fullPath));
      } else if (entry.isFile() && /\.(tsx?|)$/.test(entry.name) && !entry.name.endsWith('.d.ts') && (entry.name.endsWith('.ts') || entry.name.endsWith('.tsx'))) {
        const lines = fs.readFileSync(fullPath, 'utf8').split('\n');
        lines.forEach((line, idx) => {
          const hasWhitelist = WHITELIST_TOKENS.some(token => line.includes(token));
          if (hasWhitelist) return;
          const hasForbidden = FORBIDDEN_PATTERNS.some(pat => pat.test(line));
          if (hasForbidden) {
            const rel = path.relative(WORKTREE_ROOT, fullPath).replace(/\\/g, '/');
            violations.push(`  ${rel}:${idx + 1}  ${line.trim()}`);
          }
        });
      }
    }
    return violations;
  }

  const violations = scanDir(srcDir);
  if (violations.length === 0) {
    typeSafetyStatus = 'SUCCESS';
    typeSafetyOutput = '';
    console.log('✅ Production type safety guard clean!');
  } else {
    typeSafetyOutput = violations.join('\n');
    console.error(`❌ Production Type Safety FAILED — ${violations.length} forbidden \`as any\` cast(s) found:`);
    console.error(typeSafetyOutput);
    console.error('\n👉 Fix: use explicit types, optional chaining, or add // MIGRATION-SHIM comment if it is a known shim.');
  }
} catch (e) {
  typeSafetyOutput = e.message;
  console.error('❌ Type safety scan failed to run:', e.message);
}

// Create cryptographic package
const combinedOutput = tscOutput + jestOutput + browserConsoleOutput + astOutput + typeSafetyOutput;
const stdoutHash = crypto.createHash('sha256').update(combinedOutput).digest('hex');

const dataToSign = `${currentCommit}:${timestamp}:${tscStatus}:${jestStatus}:${browserConsoleStatus}:${astStatus}:${typeSafetyStatus}:${stdoutHash}`;
const signature = crypto.createHmac('sha256', salt).update(dataToSign).digest('hex');

const attestationData = {
  commit: currentCommit,
  timestamp,
  tscStatus,
  tscDurationMs: Date.now() - tscStart,
  jestStatus,
  jestDurationMs: Date.now() - jestStart,
  browserConsoleStatus,
  browserConsoleDurationMs: Date.now() - browserConsoleStart,
  astStatus,
  astDurationMs: Date.now() - astStart,
  typeSafetyStatus,
  typeSafetyDurationMs: Date.now() - typeSafetyStart,
  stdoutHash,
  signature
};

fs.writeFileSync(attestationPath, JSON.stringify(attestationData, null, 2), 'utf8');

if (tscStatus === 'SUCCESS' && jestStatus === 'SUCCESS' && browserConsoleStatus === 'SUCCESS' && astStatus === 'SUCCESS' && typeSafetyStatus === 'SUCCESS') {
  console.log('\n🔒 Cryptographic Attestation written to .test-attestation.json successfully!');
  console.log('✅ QA Hardening checks passed cleanly.');
  process.exit(0);
} else {
  console.error('\n🚨 QA Hardening checks FAILED. Attestation saved, but signature gates will remain closed.');
  process.exit(1);
}
