const fs = require('fs');
const path = require('path');

const args = process.argv.slice(2);
let taskSlug = null;
let commitHash = null;

for (let i = 0; i < args.length; i++) {
  if (args[i] === '--task' && i + 1 < args.length) taskSlug = args[++i];
  if (args[i] === '--commit' && i + 1 < args.length) commitHash = args[++i];
}

if (!taskSlug) {
  console.error("Usage: node auto-archiver.js --task <slug> [--commit <hash>]");
  process.exit(1);
}

const rootDir = path.resolve(__dirname, '..');
const bucketListPath = path.join(rootDir, 'tools', 'SK8Lytz_Bucket_List.md');
const archivePath = path.join(rootDir, 'tools', 'SK8Lytz_Bucket_List_ARCHIVE.md');

if (!fs.existsSync(bucketListPath)) {
  console.error(`Bucket List not found at ${bucketListPath}`);
  process.exit(1);
}

const content = fs.readFileSync(bucketListPath, 'utf8');
const lines = content.split('\n');

let taskStartIndex = -1;
let taskEndIndex = -1;
let inTask = false;

// Search for the task block
for (let i = 0; i < lines.length; i++) {
  const line = lines[i];
  // Match optional backticks and optional namespace prefix (e.g. fix/, test/) before taskSlug
  const pattern = new RegExp(`^- \\[ ?[x ]? ?\\] \\*\\*(?:\`|)(?:[a-z0-9-]+\\/|)${taskSlug}(?:\`|)\\*\\*`);
  if (!inTask && line.match(pattern)) {
    inTask = true;
    taskStartIndex = i;
  } else if (inTask) {
    // A task block ends when we hit another task, an empty line, or a horizontal rule
    if (line.match(/^- \[ ?[x ]? ?\] \*\*/) || line.match(/^---/)) {
      taskEndIndex = i;
      break;
    }
  }
}

if (inTask && taskEndIndex === -1) {
  taskEndIndex = lines.length; // Ends at EOF
}

if (taskStartIndex === -1) {
  console.warn(`[Auto-Archiver] Task '${taskSlug}' not found in Bucket List. It may already be archived.`);
  process.exit(0);
}

// Extract the task block
const taskBlockLines = lines.slice(taskStartIndex, taskEndIndex);

// Modify the first line to be marked as [x] and add commit hash if provided
let firstLine = taskBlockLines[0];
firstLine = firstLine.replace(/^- \[ ?[x ]? ?\]/, '- [x]');
if (commitHash && !firstLine.includes('Merged in')) {
  firstLine += ` 🚀 Merged in ${commitHash}`;
}
taskBlockLines[0] = firstLine;

// Remove the task block from the original lines
lines.splice(taskStartIndex, taskEndIndex - taskStartIndex);

// Clean up any stray "Active Tasks" references for this slug
for (let i = 0; i < lines.length; i++) {
  if (lines[i].startsWith('- **Active Tasks**:')) {
    lines[i] = lines[i].replace(new RegExp(`(^|,\\s*)${taskSlug}($|,|\\s)`), '$2').replace(/- \*\*Active Tasks\*\*: , /, '- **Active Tasks**: ').replace(/, $/, '').trim();
    if (lines[i] === '- **Active Tasks**:') {
      lines[i] = '- **Active Tasks**: None';
    }
  }
}

// Write back to Bucket List
fs.writeFileSync(bucketListPath, lines.join('\n'));
console.log(`[Auto-Archiver] Removed '${taskSlug}' from Bucket List.`);

// Append to Archive
const taskBlockStr = '\n\n' + taskBlockLines.join('\n');
if (fs.existsSync(archivePath)) {
  fs.appendFileSync(archivePath, taskBlockStr);
} else {
  fs.writeFileSync(archivePath, '# Archived Tasks\n' + taskBlockStr);
}
console.log(`[Auto-Archiver] Successfully archived '${taskSlug}' to SK8Lytz_Bucket_List_ARCHIVE.md.`);
