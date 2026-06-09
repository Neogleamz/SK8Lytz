const fs = require('fs');
const path = require('path');

const rootDir = path.join(__dirname, '..');
const mergedPath = path.join(rootDir, 'artifacts', 'deepdive_raw', 'merged.json');
const knownIssuesPath = path.join(rootDir, 'tools', 'KNOWN_ISSUES.md');
const reportPath = path.join(rootDir, 'artifacts', 'system_audit_report.md');

const data = JSON.parse(fs.readFileSync(mergedPath, 'utf8'));
let knownIssuesRaw = '';
if (fs.existsSync(knownIssuesPath)) {
  knownIssuesRaw = fs.readFileSync(knownIssuesPath, 'utf8');
}

// 1. KNOWN_ISSUES Extraction (naive regex matching file names or VS-XXX)
const knownIssueLines = knownIssuesRaw.split('\n');
const knownIssueKeywords = [];
for (const line of knownIssueLines) {
  if (line.includes('VS-') || line.includes('.ts') || line.includes('.tsx')) {
    // Extract anything that looks like a file path or VS- tag
    const matches = line.match(/(VS-\d{3}|[\w-]+\.tsx?)/g);
    if (matches) knownIssueKeywords.push(...matches);
  }
}

// 2. Fuzzy Dedup
data.findings.sort((a, b) => {
  if (a.file !== b.file) return a.file.localeCompare(b.file);
  return a.line - b.line;
});

const fuzzyDeduped = [];
let fuzzyCount = 0;

for (const f of data.findings) {
  let merged = false;
  for (const existing of fuzzyDeduped) {
    if (existing.file === f.file && Math.abs(existing.line - f.line) <= 5) {
      // Check if rules are similar
      const sharedRules = existing.rules_violated.some(r => f.rules_violated.includes(r));
      if (sharedRules || existing.rules_violated.length === 0) {
        existing.hit_count += f.hit_count;
        existing.rules_violated = [...new Set([...existing.rules_violated, ...f.rules_violated])];
        existing.descriptions.push(...f.descriptions);
        existing.agents = [...new Set([...existing.agents, ...f.agents])];
        merged = true;
        fuzzyCount++;
        break;
      }
    }
  }
  if (!merged) {
    fuzzyDeduped.push({ ...f });
  }
}

// 3. Scrub False Positives
const scrubbed = [];
let scrubCount = 0;
let knownIssueMatches = 0;

for (const f of fuzzyDeduped) {
  let isFalsePositive = false;
  const lowerDesc = f.descriptions.join(' ').toLowerCase();
  
  // Rule: any casts in test/mock files
  if ((f.file.includes('__tests__') || f.file.includes('__mocks__')) && lowerDesc.includes('any')) {
    isFalsePositive = true;
  }
  // Rule: Platform.OS checks in platform specific files
  if ((f.file.endsWith('.android.ts') || f.file.endsWith('.ios.ts')) && lowerDesc.includes('platform')) {
    isFalsePositive = true;
  }
  
  if (isFalsePositive) {
    scrubCount++;
    continue;
  }

  // Known Issues matching
  const hasKnownIssue = knownIssueKeywords.some(kw => f.file.includes(kw) || lowerDesc.includes(kw));
  if (hasKnownIssue) {
    f.is_known_issue = true;
    knownIssueMatches++;
  } else {
    f.is_known_issue = false;
  }
  
  // Confidence
  if (f.hit_count >= 2) {
    f.confidence = 'CONFIRMED';
  } else if (f.descriptions.some(d => d.includes('context:'))) {
    f.confidence = 'PROBABLE';
  } else {
    f.confidence = 'UNCERTAIN';
  }

  scrubbed.push(f);
}

// 4. Metrics & Report Generation
const confCounts = { CONFIRMED: 0, PROBABLE: 0, UNCERTAIN: 0 };
const ruleCounts = {};

for (const f of scrubbed) {
  confCounts[f.confidence]++;
  for (const r of f.rules_violated) {
    if (!ruleCounts[r]) ruleCounts[r] = { total: 0, high: 0, medium: 0, low: 0 };
    ruleCounts[r].total++;
    const sev = f.severity.toLowerCase();
    if (sev.includes('high')) ruleCounts[r].high++;
    else if (sev.includes('medium')) ruleCounts[r].medium++;
    else ruleCounts[r].low++;
  }
}

const sortedRules = Object.entries(ruleCounts).sort((a, b) => b[1].total - a[1].total).slice(0, 5);

let md = `## 📊 Deep-Dive Synthesis Metrics

| Metric | Value |
|---|---|
| Agent reports ingested | ${data.stats.total_files} / 48 |
| Malformed reports | ${data.stats.malformed.length} |
| Raw findings (pre-dedup) | ${data.stats.raw_findings} |
| Exact-match duplicates removed | ${data.stats.raw_findings - data.stats.exact_match_findings} |
| Fuzzy duplicates merged | ${fuzzyCount} |
| False positives scrubbed | ${scrubCount} |
| KNOWN_ISSUES matches tagged | ${knownIssueMatches} |
| **Unique verified findings** | **${scrubbed.length}** |
| Confidence: CONFIRMED | ${confCounts.CONFIRMED} |
| Confidence: PROBABLE | ${confCounts.PROBABLE} |
| Confidence: UNCERTAIN | ${confCounts.UNCERTAIN} |
| **False positive rate** | **${((scrubCount / data.stats.raw_findings) * 100).toFixed(1)}%** |
| **Dedup ratio** | **${(((data.stats.raw_findings - scrubbed.length) / data.stats.raw_findings) * 100).toFixed(1)}%** |

### Top 5 Most-Violated Rules
| Rule | Count | Severity Breakdown |
|---|---|---|
`;

for (const [r, c] of sortedRules) {
  md += `| ${r} | ${c.total} | H:${c.high} M:${c.medium} L:${c.low} |\n`;
}

md += `\n### 📋 Verified Findings\n\n`;

const groupedBySeverity = { high: [], medium: [], low: [] };
for (const f of scrubbed) {
  const s = f.severity.toLowerCase();
  if (s.includes('high')) groupedBySeverity.high.push(f);
  else if (s.includes('medium')) groupedBySeverity.medium.push(f);
  else groupedBySeverity.low.push(f);
}

for (const sev of ['high', 'medium', 'low']) {
  if (groupedBySeverity[sev].length > 0) {
    md += `#### ${sev.toUpperCase()} SEVERITY\n`;
    for (const f of groupedBySeverity[sev]) {
      md += `- **${f.file}:${f.line}** [${f.rules_violated.join(', ')}] (Confidence: ${f.confidence})\n`;
      md += `  - ${f.descriptions[0]}\n`;
      if (f.is_known_issue) md += `  - *Tagged as KNOWN_ISSUE*\n`;
    }
    md += `\n`;
  }
}

fs.writeFileSync(reportPath, md);
console.log('Synthesis complete. Wrote to system_audit_report.md');
