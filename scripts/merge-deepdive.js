const fs = require('fs');
const path = require('path');

const dir = path.join(__dirname, '..', 'artifacts', 'deepdive_raw');
const files = fs.readdirSync(dir).filter(f => f.endsWith('_findings.json'));

let totalIngested = 0;
let malformed = [];
let allFindings = [];

// Phase 0: Schema Validation & Phase 1: Ingestion
for (const file of files) {
  const filePath = path.join(dir, file);
  const content = fs.readFileSync(filePath, 'utf8');
  let data;
  try {
    data = JSON.parse(content);
  } catch {
    malformed.push({ file, error: 'Invalid JSON' });
    continue;
  }

  // Basic schema check
  if (!data.agent_id || !data.agent_type || !Array.isArray(data.findings) || !data.summary) {
    malformed.push({ file, error: 'Missing required fields' });
    continue;
  }

  if (data.findings && Array.isArray(data.findings)) {
    for (const f of data.findings) {
      if (!f.file) continue; // skip totally empty
      
      const finding = {
        file: f.file,
        line: f.line || 0,
        rule_violated: f.rule_violated || data.domain_or_rule || data.agent_id || 'Unknown',
        severity: f.severity || 'low',
        description: f.description || f.context || f.message || f.details || 'No description provided',
        suggested_fix: f.suggested_fix || null,
        _source_agent: data.agent_id || file
      };
      
      allFindings.push(finding);
      totalIngested++;
    }
  } else {
    malformed.push({ file, error: 'No findings array' });
  }
}

// Phase 2 Step 1: Exact-Match Dedup
const grouped = {};
for (const f of allFindings) {
  const key = `${f.file}:${f.line}`;
  if (!grouped[key]) {
    grouped[key] = {
      file: f.file,
      line: f.line,
      rules_violated: [f.rule_violated],
      severity: f.severity, // taking first seen severity
      descriptions: [f._source_agent + ': ' + f.description],
      suggested_fixes: f.suggested_fix ? [f.suggested_fix] : [],
      agents: [f._source_agent],
      hit_count: 1
    };
  } else {
    if (!grouped[key].rules_violated.includes(f.rule_violated)) {
      grouped[key].rules_violated.push(f.rule_violated);
    }
    grouped[key].descriptions.push(f._source_agent + ': ' + f.description);
    if (f.suggested_fix && !grouped[key].suggested_fixes.includes(f.suggested_fix)) {
      grouped[key].suggested_fixes.push(f.suggested_fix);
    }
    if (!grouped[key].agents.includes(f._source_agent)) {
      grouped[key].agents.push(f._source_agent);
    }
    grouped[key].hit_count++;
  }
}

const exactMatchDedup = Object.values(grouped);

const output = {
  stats: {
    total_files: files.length,
    malformed,
    raw_findings: totalIngested,
    exact_match_findings: exactMatchDedup.length
  },
  findings: exactMatchDedup
};

fs.writeFileSync(path.join(dir, 'merged.json'), JSON.stringify(output, null, 2));
console.log('Merge complete. Wrote to merged.json');
