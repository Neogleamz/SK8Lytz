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

  const { commit, timestamp, tscStatus, jestStatus, browserConsoleStatus, astStatus, stdoutHash, signature } = attestation;

  // 1. Recalculate signature
  const dataToSign = `${commit}:${timestamp}:${tscStatus}:${jestStatus}:${browserConsoleStatus || 'FAILED'}:${astStatus || 'FAILED'}:${stdoutHash}`;
  const expectedSignature = crypto.createHmac('sha256', salt).update(dataToSign).digest('hex');

  if (signature !== expectedSignature) {
    console.error('🚨 Cryptographic Tampering Detected! Attestation signature is invalid.');
    process.exit(1);
  }

  // 2. Verify status values
  if (tscStatus !== 'SUCCESS' || jestStatus !== 'SUCCESS' || browserConsoleStatus !== 'SUCCESS' || astStatus !== 'SUCCESS') {
    console.error('❌ Error: Stored attestation indicates failed checks (TSC/Jest/BrowserConsole).');
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

// Ensure node_modules directory junction exists if inside a worktree
let fortressRoot = WORKTREE_ROOT;
if (gitCommonDir !== '.git') {
  fortressRoot = path.dirname(gitCommonDir);
  if (!fs.existsSync(path.join(WORKTREE_ROOT, 'node_modules'))) {
    console.log('[Verify] Re-linking node_modules directory junction...');
    execSync(`cmd.exe /c "mklink /j node_modules \\"${fortressRoot}\\node_modules\\""`);
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

// Create cryptographic package
const combinedOutput = tscOutput + jestOutput + browserConsoleOutput + astOutput;
const stdoutHash = crypto.createHash('sha256').update(combinedOutput).digest('hex');

const dataToSign = `${currentCommit}:${timestamp}:${tscStatus}:${jestStatus}:${browserConsoleStatus}:${astStatus}:${stdoutHash}`;
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
  stdoutHash,
  signature
};

fs.writeFileSync(attestationPath, JSON.stringify(attestationData, null, 2), 'utf8');

if (tscStatus === 'SUCCESS' && jestStatus === 'SUCCESS' && browserConsoleStatus === 'SUCCESS' && astStatus === 'SUCCESS') {
  console.log('\n🔒 Cryptographic Attestation written to .test-attestation.json successfully!');
  console.log('✅ QA Hardening checks passed cleanly.');
  process.exit(0);
} else {
  console.error('\n🚨 QA Hardening checks FAILED. Attestation saved, but signature gates will remain closed.');
  process.exit(1);
}
