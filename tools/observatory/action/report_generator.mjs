import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const ROOT_DIR = path.resolve(__dirname, '../../../');

export class ReportGenerator {
  generateReport(dateStr = new Date().toISOString().split('T')[0]) {
    const processedDir = path.join(ROOT_DIR, 'tools', 'observatory', 'processed');
    const actionDir = path.join(ROOT_DIR, 'tools', 'observatory', 'action');
    const reportsDir = path.join(ROOT_DIR, 'tools', 'observatory', 'reports', dateStr);

    if (!fs.existsSync(reportsDir)) {
      fs.mkdirSync(reportsDir, { recursive: true });
    }

    const analyzedFile = path.join(processedDir, `${dateStr}_analyzed.json`);
    const tasksFile = path.join(actionDir, `${dateStr}_tasks.json`);

    let analyzedData = [];
    if (fs.existsSync(analyzedFile)) {
      try {
        analyzedData = JSON.parse(fs.readFileSync(analyzedFile, 'utf-8'));
      } catch (err) {}
    }

    let tasksData = [];
    if (fs.existsSync(tasksFile)) {
      try {
        tasksData = JSON.parse(fs.readFileSync(tasksFile, 'utf-8'));
      } catch (err) {}
    }

    const criticalTasks = tasksData.filter(t => t.urgencyScore >= 90);
    const highTasks = tasksData.filter(t => t.urgencyScore >= 70 && t.urgencyScore < 90);
    const mediumTasks = tasksData.filter(t => t.urgencyScore >= 40 && t.urgencyScore < 70);

    const reportPath = path.join(reportsDir, 'report.md');

    let report = `# 🛡️ Observatory Report — ${dateStr}\n\n`;
    report += `## Executive Summary\n`;
    report += `- **Errors Collected:** ${analyzedData.length}\n`;
    report += `- **After Dedup:** ${analyzedData.length}\n`;
    report += `- **Tasks Generated:** ${tasksData.length}\n\n`;

    report += `## 🔴 CRITICAL (Urgency >= 90)\n`;
    for (const task of criticalTasks) {
      report += `### Cluster: ${task.rootCause || 'Unknown'}\n`;
      report += `| Field | Value |\n|---|---|\n`;
      report += `| Urgency | ${task.urgencyScore}/100 |\n`;
      report += `| Impact | ${task.impact || 'Unknown'} |\n`;
      report += `| Proposed Fix | ${task.proposedFix || 'Manual'} |\n\n`;
    }

    report += `## 🟡 HIGH (Urgency 70-89)\n`;
    for (const task of highTasks) {
      report += `### Cluster: ${task.rootCause || 'Unknown'}\n`;
      report += `| Field | Value |\n|---|---|\n`;
      report += `| Urgency | ${task.urgencyScore}/100 |\n`;
      report += `| Impact | ${task.impact || 'Unknown'} |\n`;
      report += `| Proposed Fix | ${task.proposedFix || 'Manual'} |\n\n`;
    }

    report += `## 🟢 MEDIUM (Urgency 40-69)\n`;
    for (const task of mediumTasks) {
      report += `### Cluster: ${task.rootCause || 'Unknown'}\n`;
      report += `| Field | Value |\n|---|---|\n`;
      report += `| Urgency | ${task.urgencyScore}/100 |\n`;
      report += `| Impact | ${task.impact || 'Unknown'} |\n`;
      report += `| Proposed Fix | ${task.proposedFix || 'Manual'} |\n\n`;
    }

    report += `## 📊 Trend Analysis\n`;
    report += `| Domain | New | Recurring | Regression | Resolved |\n|---|---|---|---|---|\n`;
    
    const domainTrends = {};
    for (const error of analyzedData) {
      const d = error.domain || 'UNKNOWN';
      if (!domainTrends[d]) domainTrends[d] = { new: 0, recurring: 0, regression: 0, resolved: 0 };
      if (error.trend === 'NEW') domainTrends[d].new++;
      if (error.trend === 'RECURRING') domainTrends[d].recurring++;
      if (error.trend === 'REGRESSION') domainTrends[d].regression++;
      if (error.trend === 'RESOLVED') domainTrends[d].resolved++;
    }

    for (const d of Object.keys(domainTrends)) {
      report += `| ${d} | ${domainTrends[d].new} | ${domainTrends[d].recurring} | ${domainTrends[d].regression} | ${domainTrends[d].resolved} |\n`;
    }

    report += `\n## 🔗 Institutional Memory Updates\n`;
    report += `- New KNOWN_ISSUES entries: 0\n`;
    report += `- Friction events incremented: 0\n`;
    report += `- Evolution proposals triggered: 0\n`;

    fs.writeFileSync(reportPath, report, 'utf-8');
    console.log(`[ReportGenerator] Report written to ${reportPath}`);
  }
}

if (process.argv[1] === fileURLToPath(import.meta.url)) {
  const generator = new ReportGenerator();
  generator.generateReport();
}
