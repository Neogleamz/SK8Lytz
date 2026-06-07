const { execSync } = require('child_process');
const fs = require('fs');
const path = require('path');

const mapPath = path.join(__dirname, 'ARCH_DEPENDENCY_MAP.json');
let ruleMap = { rules: [] };
try {
  ruleMap = JSON.parse(fs.readFileSync(mapPath, 'utf8'));
} catch (e) {
  console.error("Failed to load ARCH_DEPENDENCY_MAP.json", e.message);
  process.exit(1);
}

const args = process.argv.slice(2);
if (args.includes('--ignore-blast')) {
  console.log("⚠️ Blast Radius Scanner bypassed via --ignore-blast");
  process.exit(0);
}

let diffCommand = 'git diff HEAD~1 HEAD --name-only'; // default fallback
if (args.includes('--cached')) {
  diffCommand = 'git diff --cached --name-only';
} else if (args.includes('--worktree')) {
  diffCommand = 'git diff master...HEAD --name-only';
} else {
  const branchIdx = args.indexOf('--branch');
  if (branchIdx !== -1 && args[branchIdx + 1]) {
    const branch = args[branchIdx + 1];
    diffCommand = `git diff master...${branch} --name-only`;
  }
}

try {
  const diffOutput = execSync(diffCommand, { encoding: 'utf8' });
  const changedFiles = diffOutput.split('\n').map(f => f.trim().replace(/\\/g, '/')).filter(f => f.length > 0);

  let violations = [];

  for (const rule of ruleMap.rules) {
    const triggers = Array.isArray(rule.trigger) ? rule.trigger : [rule.trigger];
    const requires = Array.isArray(rule.requires) ? rule.requires : [rule.requires];

    // Check if any trigger file matches the changed files (substring match)
    const triggerHit = changedFiles.some(file => triggers.some(t => file.includes(t)));

    if (triggerHit) {
      // Check if ALL required files are present in the diff
      const missing = requires.filter(req => !changedFiles.some(file => file.includes(req)));
      if (missing.length > 0) {
        violations.push({
          trigger: triggers.join(', '),
          missing: missing.join(', '),
          description: rule.description || "No description provided."
        });
      }
    }
  }

  if (violations.length > 0) {
    console.error("\n🚨 BLAST RADIUS VIOLATION 🚨");
    console.error("You modified files that have hardcoded architectural dependencies, but missed the required updates.\n");
    violations.forEach(v => {
      console.error(`- Trigger Modified: ${v.trigger}`);
      console.error(`  Missing Dependent: ${v.missing}`);
      console.error(`  Reason: ${v.description}\n`);
    });
    console.error("If this was intentional, you must bypass with --ignore-blast.\n");
    process.exit(1);
  }

  console.log("✅ Blast Radius Scan passed. No missing dependencies.");
} catch (e) {
  console.error("Failed to run git diff for blast radius scanner:");
  console.error(e.message);
  process.exit(1);
}
