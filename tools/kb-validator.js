#!/usr/bin/env node
/**
 * KB Validator — tools/kb-validator.js
 * 
 * Reads tools/knowledge-base/INDEX.md, parses all entry Re-Validate By dates,
 * and outputs a JSON staleness report.
 * 
 * Exit codes:
 *   0 = All entries current (or never-expire)
 *   1 = One or more STALE entries (expired within last 30 days)
 *   2 = One or more CRITICAL entries (expired > 30 days ago)
 * 
 * Called by:
 *   - /hello Step 0.7b
 *   - /kb-refresh Phase 1
 *   - verifiable-check-runner.js (optional gate)
 * 
 * Usage:
 *   node tools/kb-validator.js
 *   node tools/kb-validator.js --json       (machine-readable output only)
 *   node tools/kb-validator.js --summary    (one-line summary for /hello)
 */

const fs = require('fs');
const path = require('path');

const INDEX_PATH = path.join(__dirname, 'knowledge-base', 'INDEX.md');
const JSON_FLAG = process.argv.includes('--json');
const SUMMARY_FLAG = process.argv.includes('--summary');
const CRITICAL_THRESHOLD_DAYS = 30;

// ─────────────────────────────────────────────────────────────────────────────
// Parse INDEX.md
// ─────────────────────────────────────────────────────────────────────────────

function parseIndex(content) {
  const entries = [];
  // Match each ## heading block (entry slug) and its fields
  const entryPattern = /^## (.+)$/gm;
  const fieldPattern = /- \*\*([\w\/ ]+)\*\*: (.+)$/;

  let match;
  const sections = [];
  
  // Split by ## headings, collect entries
  const lines = content.split('\n');
  let currentSlug = null;
  let currentFields = {};
  let insideCodeBlock = false;

  for (const line of lines) {
    // Skip content inside fenced code blocks (``` ... ```)
    if (line.trim().startsWith('```')) {
      insideCodeBlock = !insideCodeBlock;
      continue;
    }
    if (insideCodeBlock) continue;

    const headingMatch = line.match(/^## (.+)$/);
    if (headingMatch) {
      // Save previous entry if it had a Re-Validate By field
      if (currentSlug && currentFields['File']) {
        entries.push({ slug: currentSlug, ...currentFields });
      }
      currentSlug = headingMatch[1].trim();
      currentFields = {};
      continue;
    }

    const fieldMatch = line.match(/^- \*\*(.+?)\*\*: (.+)$/);
    if (fieldMatch && currentSlug) {
      const key = fieldMatch[1].trim();
      const value = fieldMatch[2].trim();
      currentFields[key] = value;
    }
  }

  // Don't forget the last entry
  if (currentSlug && currentFields['File']) {
    entries.push({ slug: currentSlug, ...currentFields });
  }

  return entries;
}

// ─────────────────────────────────────────────────────────────────────────────
// Classify entries
// ─────────────────────────────────────────────────────────────────────────────

function classifyEntries(entries) {
  const today = new Date();
  today.setHours(0, 0, 0, 0);

  const result = {
    current: [],
    stale: [],
    critical: [],
    neverExpire: [],
    errors: [],
  };

  for (const entry of entries) {
    const reValidateBy = entry['Re-Validate By'];
    const window = entry['Staleness Window'];

    // Skip index schema header entry (no File field) and category headers
    if (!entry['File']) continue;

    // Never-expire entries
    if (!reValidateBy || reValidateBy === 'N/A' || window === 'never') {
      result.neverExpire.push({
        slug: entry.slug,
        domain: entry['Domain Tags'] || '',
        file: entry['File'] || '',
        source: entry['Source'] || '',
        feedsInto: entry['Feeds Into'] || 'standalone',
      });
      continue;
    }

    // Parse the Re-Validate By date
    const expiry = new Date(reValidateBy);
    if (isNaN(expiry.getTime())) {
      result.errors.push({ slug: entry.slug, error: `Invalid Re-Validate By date: "${reValidateBy}"` });
      continue;
    }

    expiry.setHours(0, 0, 0, 0);
    const daysDiff = Math.floor((today - expiry) / (1000 * 60 * 60 * 24));

    const entryReport = {
      slug: entry.slug,
      domain: entry['Domain Tags'] || '',
      file: entry['File'] || '',
      source: entry['Source'] || '',
      reValidateBy: reValidateBy,
      lastValidated: entry['Last Validated'] || 'unknown',
      status: entry['Status'] || 'unknown',
      feedsInto: entry['Feeds Into'] || 'standalone',
      daysPastExpiry: daysDiff > 0 ? daysDiff : 0,
    };

    if (daysDiff <= 0) {
      result.current.push(entryReport);
    } else if (daysDiff <= CRITICAL_THRESHOLD_DAYS) {
      result.stale.push(entryReport);
    } else {
      result.critical.push(entryReport);
    }
  }

  return result;
}

// ─────────────────────────────────────────────────────────────────────────────
// Output formatting
// ─────────────────────────────────────────────────────────────────────────────

function formatSummary(report) {
  const totalStale = report.stale.length + report.critical.length;
  if (report.critical.length > 0) {
    return `🔴 KB Health: ${report.critical.length} CRITICAL, ${report.stale.length} stale, ${report.current.length} current, ${report.neverExpire.length} never-expire | Run /kb-refresh`;
  }
  if (report.stale.length > 0) {
    return `⚠️ KB Health: ${report.stale.length} stale, ${report.current.length} current, ${report.neverExpire.length} never-expire | Run /kb-refresh`;
  }
  return `✅ KB Health: All ${report.current.length + report.neverExpire.length} entries current`;
}

function formatHuman(report) {
  const lines = [];
  lines.push('');
  lines.push('🗄️  KB Staleness Report');
  lines.push('─'.repeat(65));

  if (report.neverExpire.length > 0) {
    lines.push(`♾️  NEVER-EXPIRE (${report.neverExpire.length}): ${report.neverExpire.map(e => e.slug).join(', ')}`);
  }

  if (report.current.length > 0) {
    lines.push(`✅ CURRENT (${report.current.length}): ${report.current.map(e => e.slug).join(', ')}`);
  }

  if (report.stale.length > 0) {
    lines.push('');
    lines.push(`⚠️  STALE (${report.stale.length} entries):`);
    for (const e of report.stale) {
      lines.push(`  - ${e.slug} — expired ${e.daysPastExpiry} day(s) ago`);
      lines.push(`    Domain: ${e.domain} | Re-Validate By: ${e.reValidateBy}`);
      lines.push(`    Source: ${e.source}`);
    }
  }

  if (report.critical.length > 0) {
    lines.push('');
    lines.push(`🔴 CRITICAL (${report.critical.length} entries — expired >${CRITICAL_THRESHOLD_DAYS} days):`);
    for (const e of report.critical) {
      lines.push(`  - ${e.slug} — expired ${e.daysPastExpiry} day(s) ago`);
      lines.push(`    Domain: ${e.domain} | Re-Validate By: ${e.reValidateBy}`);
      lines.push(`    Source: ${e.source}`);
      lines.push(`    Feeds Into: ${e.feedsInto}`);
    }
  }

  if (report.errors.length > 0) {
    lines.push('');
    lines.push(`❌ PARSE ERRORS (${report.errors.length}):`);
    for (const e of report.errors) {
      lines.push(`  - ${e.slug}: ${e.error}`);
    }
  }

  lines.push('─'.repeat(65));

  if (report.critical.length > 0) {
    lines.push('→ Run /kb-refresh to address CRITICAL entries before sprint work.');
  } else if (report.stale.length > 0) {
    lines.push('→ Run /kb-refresh to re-validate stale entries (optional, not blocking).');
  } else {
    lines.push('→ All entries within staleness windows. No action required.');
  }
  lines.push('');

  return lines.join('\n');
}

// ─────────────────────────────────────────────────────────────────────────────
// Main
// ─────────────────────────────────────────────────────────────────────────────

function main() {
  if (!fs.existsSync(INDEX_PATH)) {
    const errorReport = {
      error: 'INDEX.md not found. Run /kb-capture to seed the knowledge base.',
      path: INDEX_PATH,
      current: [], stale: [], critical: [], neverExpire: [], errors: [],
    };
    if (JSON_FLAG) {
      console.log(JSON.stringify(errorReport, null, 2));
    } else {
      console.log('⚠️  KB Health: INDEX.md not found at', INDEX_PATH);
      console.log('   Run /kb-capture to seed the knowledge base.');
    }
    process.exit(0); // Not a failure — just uninitialized
  }

  const content = fs.readFileSync(INDEX_PATH, 'utf-8');
  const entries = parseIndex(content);
  const report = classifyEntries(entries);

  if (JSON_FLAG) {
    console.log(JSON.stringify(report, null, 2));
  } else if (SUMMARY_FLAG) {
    console.log(formatSummary(report));
  } else {
    console.log(formatHuman(report));
  }

  // Exit code reflects worst-case severity
  if (report.critical.length > 0) {
    process.exit(2);
  } else if (report.stale.length > 0) {
    process.exit(1);
  } else {
    process.exit(0);
  }
}

main();
