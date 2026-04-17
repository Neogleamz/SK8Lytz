const fs = require('fs');
const path = require('path');
const chokidar = require('chokidar');

const BRAIN_DIR = path.join(process.env.USERPROFILE || process.env.HOME, '.gemini', 'antigravity', 'brain');

/**
 * Finds the most recently modified overview.txt in the active brain session.
 */
function findLatestLogFile() {
  if (!fs.existsSync(BRAIN_DIR)) {
    throw new Error(`Brain directory not found at ${BRAIN_DIR}`);
  }

  const sessions = fs.readdirSync(BRAIN_DIR, { withFileTypes: true })
    .filter(dirent => dirent.isDirectory());

  let latestFile = null;
  let maxTime = 0;

  for (const session of sessions) {
    const overviewPath = path.join(BRAIN_DIR, session.name, '.system_generated', 'logs', 'overview.txt');
    if (fs.existsSync(overviewPath)) {
      const stats = fs.statSync(overviewPath);
      if (stats.mtimeMs > maxTime) {
        maxTime = stats.mtimeMs;
        latestFile = overviewPath;
      }
    }
  }

  return latestFile;
}

/**
 * Tails the given file and emits new chunks to the callback.
 * Checks for size increases and only reads the diff.
 */
function watchLogFile(filePath, onChuckedData) {
  let lastSize = 0;

  if (fs.existsSync(filePath)) {
    const initialStats = fs.statSync(filePath);
    lastSize = initialStats.size;
  }

  const watcher = chokidar.watch(filePath, {
    persistent: true,
    alwaysStat: true,
    usePolling: true, // safe on windows
    interval: 500,
  });

  watcher.on('change', (path, stats) => {
    if (stats.size > lastSize) {
      const stream = fs.createReadStream(filePath, {
        start: lastSize,
        end: stats.size - 1,
        encoding: 'utf8'
      });

      let newChunks = '';
      stream.on('data', (chunk) => {
        newChunks += chunk;
      });

      stream.on('end', () => {
        if (newChunks.trim().length > 0) {
          onChuckedData(newChunks);
        }
      });

      lastSize = stats.size;
    } else if (stats.size < lastSize) {
      // File was truncated or rotated
      lastSize = stats.size;
    }
  });

  return watcher;
}

/**
 * Basic formatter. 
 * Prevents Discord from rejecting 2000+ char dumps by tearing down long JSON objects and truncating.
 */
function formatForDiscord(text) {
  // Strip overly long base64 or massive object payloads that agent tools output
  const truncateThreshold = 1800;
  
  // Clean up excessive whitespace
  let cleanTxt = text.replace(/\\n{3,}/g, '\\n\\n').trim();

  // Highlight tool action markers
  cleanTxt = cleanTxt.replace(/<tool_call_result>/g, '🛠️ **Tool Finished**');
  cleanTxt = cleanTxt.replace(/<tool_call>/g, '⚙️ **Calling Tool**');

  if (cleanTxt.length > truncateThreshold) {
    cleanTxt = cleanTxt.substring(0, truncateThreshold) + '... \\n*[Output heavily truncated to comply with Discord limits]*';
  }

  return cleanTxt;
}

module.exports = {
  findLatestLogFile,
  watchLogFile,
  formatForDiscord
};
