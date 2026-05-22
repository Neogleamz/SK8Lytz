const { spawn, execSync } = require('child_process');
const http = require('http');
const fs = require('fs');
const path = require('path');
const WebSocket = require('ws');

const CHROME_PATH = 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe';
const TARGET_URL = 'http://localhost:8081/auth';
const DEBUG_PORT = 9222;
const MOCK_MUTE_WARNINGS = true; // Set to true to ignore warnings and only crash on Errors/Uncaught exceptions

let chromeProcess = null;
let wsConnection = null;
const collectedErrors = [];
const collectedWarnings = [];
const collectedLogs = [];

function logMsg(prefix, text) {
  console.log(`[CDP-Sniffer] ${prefix}: ${text}`);
}

// 1. Fetch JSON target list from Chrome debugger
function fetchTargets() {
  return new Promise((resolve, reject) => {
    http.get(`http://127.0.0.1:${DEBUG_PORT}/json/list`, (res) => {
      let data = '';
      res.on('data', chunk => data += chunk);
      res.on('end', () => {
        try {
          resolve(JSON.parse(data));
        } catch (e) {
          reject(e);
        }
      });
    }).on('error', reject);
  });
}

// 2. Poll for debugger availability
function waitForDebugger(timeoutMs = 5000) {
  const start = Date.now();
  return new Promise((resolve, reject) => {
    function check() {
      fetchTargets()
        .then(resolve)
        .catch(() => {
          if (Date.now() - start > timeoutMs) {
            reject(new Error(`Timed out waiting for Chrome Debugger on port ${DEBUG_PORT}`));
          } else {
            setTimeout(check, 250);
          }
        });
    }
    check();
  });
}

// 3. Main execution sequence
async function run() {
  console.log('🌐 Starting headlessly checking React Native Web runtime console...');

  // Check if Chrome is already debugging
  let targets = [];
  try {
    targets = await fetchTargets();
    logMsg('INFO', 'Connected to existing debugging instance.');
  } catch (e) {
    logMsg('INFO', 'Launching fresh headless Chrome session...');
    try {
      chromeProcess = spawn(CHROME_PATH, [
        '--headless=new',
        `--remote-debugging-port=${DEBUG_PORT}`,
        '--disable-gpu',
        '--no-sandbox',
        '--disable-dev-shm-usage',
        TARGET_URL
      ], { detached: true, stdio: 'ignore' });
      chromeProcess.unref();
      
      targets = await waitForDebugger();
    } catch (err) {
      console.error(`❌ Failed to launch Chrome: ${err.message}`);
      process.exit(1);
    }
  }

  // Find the page target
  const pageTarget = targets.find(t => t.type === 'page');
  if (!pageTarget) {
    console.error('❌ Error: No page target found in browser JSON list.');
    cleanupAndExit(1);
  }

  const { webSocketDebuggerUrl } = pageTarget;
  logMsg('INFO', `Connecting to page target: ${pageTarget.url}`);

  // Connect to Chrome WebSocket Debugger
  wsConnection = new WebSocket(webSocketDebuggerUrl);

  let messageId = 1;
  const pendingRequests = new Map();
  function sendCommand(method, params = {}) {
    const id = messageId++;
    const payload = JSON.stringify({ id, method, params });
    pendingRequests.set(id, method);
    wsConnection.send(payload);
  }

  let enabledDomains = 0;
  const totalDomains = 4;
  function onDomainEnabled() {
    enabledDomains++;
    if (enabledDomains === totalDomains) {
      logMsg('INFO', 'All CDP domains enabled. Triggering hard page reload...');
      sendCommand('Page.reload', { ignoreCache: true });
    }
  }

  wsConnection.on('open', () => {
    logMsg('INFO', 'WebSocket debugger link established. Initializing CDP domains...');
    sendCommand('Console.enable');
    sendCommand('Runtime.enable');
    sendCommand('Log.enable');
    sendCommand('Page.enable');
  });

  wsConnection.on('message', (data) => {
    let msg;
    try {
      msg = JSON.parse(data.toString());
    } catch (e) {
      return;
    }



    // Intercept responses to enable commands
    if (msg.id && pendingRequests.has(msg.id)) {
      const method = pendingRequests.get(msg.id);
      pendingRequests.delete(msg.id);
      if (method && method.endsWith('.enable')) {
        onDomainEnabled();
      }
    }

    // Intercept Console APIs
    if (msg.method === 'Runtime.consoleAPICalled') {
      const { type, args } = msg.params;
      const text = args.map(a => a.value || JSON.stringify(a)).join(' ');
      
      if (type === 'error') {
        collectedErrors.push({ source: 'console.error', text });
        console.error(`  🔴 [Browser Error] ${text}`);
      } else if (type === 'warning') {
        collectedWarnings.push({ source: 'console.warn', text });
        if (!MOCK_MUTE_WARNINGS) {
          console.warn(`  🟡 [Browser Warn]  ${text}`);
        }
      } else {
        collectedLogs.push(text);
      }
    }

    // Intercept Uncaught Exceptions
    if (msg.method === 'Runtime.exceptionThrown') {
      const { exceptionDetails } = msg.params;
      const text = exceptionDetails.exception 
        ? (exceptionDetails.exception.description || exceptionDetails.exception.value) 
        : exceptionDetails.text;
      collectedErrors.push({ source: 'uncaught.exception', text });
      console.error(`  🚨 [Browser Crash] Uncaught Exception: ${text}`);
    }

    // Intercept Net/Log errors
    if (msg.method === 'Log.entryAdded') {
      const { level, text, source } = msg.params.entry;
      if (level === 'error') {
        collectedErrors.push({ source: `network.${source}`, text });
        console.error(`  🔴 [Network/Log Error] ${text}`);
      } else if (level === 'warning') {
        collectedWarnings.push({ source: `network.${source}`, text });
      }
    }
  });

  wsConnection.on('error', (err) => {
    console.error(`❌ WebSocket Connection Error: ${err.message}`);
    cleanupAndExit(1);
  });

  // Let the page boot and run for 5 seconds
  setTimeout(() => {
    logMsg('INFO', 'Verification window elapsed. Running checks...');
    evaluateResults();
  }, 5000);
}

function evaluateResults() {
  const reportsDir = path.join(__dirname, '..', '.system_generated');
  if (!fs.existsSync(reportsDir)) {
    fs.mkdirSync(reportsDir, { recursive: true });
  }

  const summary = {
    timestamp: new Date().toISOString(),
    errorsCount: collectedErrors.length,
    warningsCount: collectedWarnings.length,
    errors: collectedErrors,
    warnings: collectedWarnings
  };

  fs.writeFileSync(
    path.join(reportsDir, 'runtime-console-failures.json'),
    JSON.stringify(summary, null, 2),
    'utf8'
  );

  console.log(`\n📊 Sniffer Metrics: ${collectedErrors.length} Errors | ${collectedWarnings.length} Warnings detected.`);

  if (collectedErrors.length > 0) {
    console.error('❌ HEADLESS BROWSER RUNTIME VERIFICATION FAILED!');
    cleanupAndExit(1);
  } else {
    console.log('✅ Headless browser runtime console verification passed cleanly.');
    cleanupAndExit(0);
  }
}

function cleanupAndExit(exitCode) {
  if (wsConnection && wsConnection.readyState === WebSocket.OPEN) {
    try {
      // Send Browser.close command to cleanly exit Chrome
      wsConnection.send(JSON.stringify({ id: 999, method: 'Browser.close' }));
      wsConnection.close();
    } catch (e) {}
  }
  
  if (chromeProcess) {
    try {
      // Direct process kill as safety fallback
      chromeProcess.kill('SIGKILL');
    } catch (e) {}
  }
  
  process.exit(exitCode);
}

run();
