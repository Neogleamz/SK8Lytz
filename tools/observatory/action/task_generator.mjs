import crypto from 'crypto';

export class TaskGenerator {
  generateTasks(clusteredRecords) {
    return clusteredRecords.map(cluster => this.generateKanbanBlock(cluster));
  }

  generateKanbanBlock(cluster) {
    const slug = `fix/${this.generateSlug(cluster.rootCause)}`;
    const tags = this.generateTags(cluster);
    const dateStr = new Date().toISOString().split('T')[0];
    
    const source = cluster.source || 'UNKNOWN';
    const urgency = cluster.urgencyScore || 0;
    const msg = cluster.message || 'Unknown error';
    const file = cluster.file || 'Unknown file';
    const impact = cluster.errorType === 'CRASH' ? 'Fatal Crash' : 'Degraded UX';
    const fix = cluster.proposedFix || 'Investigate root cause and implement fix';

    return `- [ ] **\`${slug}\`**
  - **Tags:** \`[✅ READY]\` \`[✅ VERIFIED]\` \`[${tags.domain}]\` \`[${tags.risk}]\` \`[${tags.size}]\` \`[🤖 MODEL]\` \`[BATCH:self-heal-${dateStr}]\`
  - **Goal:** Resolve error cluster: ${cluster.rootCause}
  - **Decision Log:** Discovered by \`/self-heal\` observatory on ${dateStr}. Urgency score: ${urgency}/100.
  - **Analysis:** Observatory report \`tools/observatory/reports/${dateStr}/report.md\` — ${msg}
  - **Source of Truth:** 📖 [${file}](file:///${file.replace(/\\/g, '/')})
  - **Details:**
    - **Error Count:** ${cluster.occurrences || 1} unique errors in this cluster
    - **Error Sources:** ${source}
    - **Frequency:** ${cluster.occurrences || 1} occurrences
    - **Impact:** ${impact}
    - **Proposed Fix:** ${fix}
    - **Verification:** Run \`npm run verify\` and check if ${source} reports clean.`;
  }

  generateSlug(rootCause) {
    if (!rootCause) return 'unknown-error';
    const clean = rootCause.replace(/[^a-zA-Z0-9]/g, '-').toLowerCase();
    const finalSlug = clean.replace(/-+/g, '-').substring(0, 40).replace(/^-|-$/g, '');
    return finalSlug || 'unknown-error';
  }

  generateTags(cluster) {
    const domain = cluster.domain || 'CORE';
    const risk = (cluster.urgencyScore >= 70) ? '⚠️ H-RISK' : '✅ L-RISK';
    const size = (cluster.urgencyScore >= 90) ? '🥩 Feast' : (cluster.urgencyScore >= 50 ? '🍱 Meal' : '🍪 Snack');
    return { domain, risk, size };
  }
}
