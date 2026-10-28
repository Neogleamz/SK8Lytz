const { spawn, execSync } = require('child_process');
const http = require('http');
const fs = require('fs');
const path = require('path');
const net = require('net');
const os = require('os');
const WebSocket = require('ws');

const CHROME_PATH = 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe';
const REPO_ROOT = path.resolve(__dirname, '..');
const BUNDLE_TIMEOUT_MS = 240000; // first Metro web bundle of a cold cache can take minutes
const IS_WIN = process.platform === 'win32';
// Ports are picked free at run time: 8081 belongs to whatever else runs on the PC (e.g. the
// retired sk8lytz-webdemo Docker container), and this gate must only judge THIS checkout.
let DEBUG_PORT = 0;
let TARGET_URL = '';
let SERVER_PORT = 0;
let serverProcess = null;
let userDataDir = null;
const MOCK_MUTE_WARNINGS = true; // Set to true to ignore warnings and only crash on Errors/Uncaught exceptions

let chromeProcess = null;
let wsConnection = null;
const collectedErrors = [];
const collectedWarnings = [];
const collectedLogs = [];

function logMsg(prefix, text) {
  console.log(`[CDP-Sniffer] ${prefix}: ${text}`);
}

function getFreePort() {
  return new Promise((resolve, reject) => {
    const srv = net.createServer();
    srv.unref();
    srv.on('error', reject);
    srv.listen(0, '127.0.0.1', () => {
      const { port } = srv.address();
      srv.close(() => resolve(port));
    });
  });
}

function httpGetText(url, timeoutMs = 10000) {
  return new Promise((resolve, reject) => {
    const req = http.get(url, (res) => {
      let data = '';
      res.setEncoding('utf8');
      res.on('data', chunk => data += chunk);
      res.on('end', () => resolve({ status: res.statusCode, body: data }));
    });
    req.setTimeout(timeoutMs, () => req.destroy(new Error(`Timed out after ${timeoutMs} ms: ${url}`)));
    req.on('error', reject);
  });
}

// Stops the whole process tree (npx -> node -> metro workers); on Windows child.kill() leaves them running.
function killTree(child) {
  if (!child || !child.pid) return;
  try {
    if (IS_WIN) {
      execSync(`taskkill /pid ${child.pid} /T /F`, { stdio: 'ignore' });
    } else {
      process.kill(-child.pid, 'SIGKILL');
    }
  } catch (e) {}
}

// Starts `expo start --web` from this checkout on a free port and returns once the web bundle builds.
async function startOwnServer() {
  SERVER_PORT = await getFreePort();
  TARGET_URL = `http://localhost:${SERVER_PORT}/auth`;
  logMsg('INFO', `Starting expo web from ${REPO_ROOT} on free port ${SERVER_PORT}...`);
  let serverLog = '';
  serverProcess = spawn(`npx expo start --web --port ${SERVER_PORT}`, {
    cwd: REPO_ROOT,
    shell: true,
    detached: !IS_WIN,
    stdio: ['ignore', 'pipe', 'pipe'],
    env: { ...process.env, CI: '1', BROWSER: 'none', EXPO_NO_TELEMETRY: '1' }
  });
  const onData = (d) => { serverLog = (serverLog + d.toString()).slice(-4000); };
  serverProcess.stdout.on('data', onData);
  serverProcess.stderr.on('data', onData);
  let exited = false;
  serverProcess.on('exit', (code) => { exited = true; serverLog += `
[expo exited with code ${code}]`; });

  const fail = (msg) => {
    console.error(`❌ ${msg}`);
    console.log(`--- expo output (tail) ---
${serverLog}`);
    cleanupAndExit(1);
  };

  const start = Date.now();
  const bundleUrl = `http://localhost:${SERVER_PORT}/index.ts.bundle?platform=web&dev=true&minify=false`;
  let bundle = null;
  while (Date.now() - start < BUNDLE_TIMEOUT_MS) {
    if (exited) return fail('expo start --web exited before serving a bundle. Nothing healthy answered.');
    try {
      const res = await httpGetText(bundleUrl, 120000);
      if (res.status === 200) { bundle = res.body; break; }
      // Metro answers 500 with a TransformError when the checkout cannot build (missing module etc.)
      return fail(`Bundle request returned HTTP ${res.status} (this checkout does not build for web):
${res.body.slice(0, 1500)}`);
    } catch (e) {
      await new Promise(r => setTimeout(r, 1000)); // server not listening yet
    }
  }
  if (!bundle) return fail(`No healthy web bundle from this checkout within ${BUNDLE_TIMEOUT_MS / 1000}s.`);

  // The server is ours (spawned from REPO_ROOT on a port that was free a moment ago); also refuse a
  // bundle that was built from another workspace.
  if (/AG_SK8Lytz_App/i.test(bundle)) {
    return fail('Served bundle references the retired AG_SK8Lytz_App workspace, not this checkout. Refusing.');
  }
  logMsg('INFO', `Bundle served from this checkout (${Math.round(bundle.length / 1024)} KB, port ${SERVER_PORT}).`);
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

  await startOwnServer();

  // Always a fresh, isolated Chrome on its own debug port and profile, so no other session's page is judged.
  DEBUG_PORT = await getFreePort();
  userDataDir = fs.mkdtempSync(path.join(os.tmpdir(), 'sk8-console-'));
  let targets = [];
  logMsg('INFO', 'Launching fresh headless Chrome session...');
  try {
    chromeProcess = spawn(CHROME_PATH, [
      '--headless=new',
      `--remote-debugging-port=${DEBUG_PORT}`,
      `--user-data-dir=${userDataDir}`,
      '--disable-gpu',
      '--no-sandbox',
      '--disable-dev-shm-usage',
      'about:blank'
    ], { detached: true, stdio: 'ignore' });
    chromeProcess.unref();

    targets = await waitForDebugger();
  } catch (err) {
    console.error(`❌ Failed to launch Chrome: ${err.message}`);
    cleanupAndExit(1);
  }

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
      logMsg('INFO', `All CDP domains enabled. Loading ${TARGET_URL} ...`);
      sendCommand('Page.navigate', { url: TARGET_URL });
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
      const { level, text, source, url } = msg.params.entry;
      if (level === 'error') {
        const errorText = url ? `${text} (URL: ${url})` : text;
        // Ignore external URL errors to avoid external site script/asset noise failing the quality gate
        if (url && !url.includes('localhost') && !url.includes('127.0.0.1')) {
          return;
        }
        // Ignore image proxy 404 errors as they depend on external content viability
        if (url && url.includes('/api/img-proxy')) {
          return;
        }
        // Ignore favicon.ico default browser requests
        if (url && url.includes('favicon.ico')) {
          return;
        }
        collectedErrors.push({ source: `network.${source}`, text: errorText });
        console.error(`  🔴 [Network/Log Error] ${errorText}`);
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

  killTree(serverProcess);
  if (userDataDir) {
    try { fs.rmSync(userDataDir, { recursive: true, force: true }); } catch (e) {}
  }
  
  process.exit(exitCode);
}

run().catch((err) => {
  console.error(`❌ Harvester crashed: ${err.message}`);
  cleanupAndExit(1);
});
