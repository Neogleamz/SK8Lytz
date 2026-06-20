const fs = require('fs');
const path = require('path');

const DOMAIN_FILES = [
  "src/services/ble/BackgroundBLEService.ts",
  "src/services/ble/BleMachine.ts",
  "src/services/ble/BleMachine.types.ts",
  "src/services/ble/ConnectService.ts",
  "src/services/ble/HeartbeatService.ts",
  "src/services/ble/InterrogatorService.ts",
  "src/services/ble/RecoveryService.ts",
  "src/services/ble/RSSIService.ts",
  "src/services/BleCharacteristicCache.ts",
  "src/services/BlePingService.ts",
  "src/services/BleSessionFactory.ts",
  "src/services/BleWriteDispatcher.ts",
  "src/services/BleWriteQueue.ts",
  "src/hooks/ble/useBLEBatterySweep.ts",
  "src/hooks/ble/useBLEInterrogator.ts",
  "src/hooks/ble/useBLERSSIMonitor.ts",
  "src/hooks/ble/useBLEScanner.ts",
  "src/hooks/useBLE.ts",
  "src/hooks/useOptimisticBLE.ts",
  "src/context/BLEContext.tsx"
];

const ROOT_DIR = 'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz';

const findings = [];
let findingIdCounter = 1;

function addFinding(file, line, rule, severity, desc, snippet) {
  findings.push({
    id: `BLE_CORE-${String(findingIdCounter++).padStart(3, '0')}`,
    file: file,
    line: line,
    rule_violated: rule,
    severity: severity,
    description: desc,
    code_snippet: snippet.trim()
  });
}

for (const relPath of DOMAIN_FILES) {
  const fullPath = path.join(ROOT_DIR, relPath);
  if (!fs.existsSync(fullPath)) continue;
  
  const content = fs.readFileSync(fullPath, 'utf8');
  const lines = content.split('\n');
  const fileSize = Buffer.byteLength(content, 'utf8');
  
  if (fileSize > 30000) {
    addFinding(relPath, 1, 'R-23', 'HIGH', `File exceeds 30KB limit (${fileSize} bytes) - flag for mandatory component extraction`, '// Monolith exceeding 30KB');
  }

  let inUseEffect = false;
  let useEffectStart = 0;
  let useEffectHasReturn = false;

  for (let i = 0; i < lines.length; i++) {
    const line = lines[i];
    const lineNum = i + 1;
    
    if (line.match(/\.(writeCharacteristic|writeCharacteristicWithoutResponse(ForDevice)?)\(/) && !relPath.includes('BleWriteQueue')) {
      addFinding(relPath, lineNum, 'R-01', 'HIGH', 'Bypassing BleWriteQueue - direct writeCharacteristic calls', line);
    }
    
    if (line.match(/catch\s*\(\s*([a-zA-Z0-9_]+)\s*\)/)) {
      const eVar = line.match(/catch\s*\(\s*([a-zA-Z0-9_]+)\s*\)/)[1];
      let hasInstanceOf = false;
      for (let j = i; j < Math.min(i + 5, lines.length); j++) {
        if (lines[j].includes(`${eVar} instanceof Error`)) hasInstanceOf = true;
      }
      if (!hasInstanceOf && (lines[i+1] && lines[i+1].includes(`${eVar}.`))) {
         addFinding(relPath, lineNum, 'R-06', 'MEDIUM', `Missing standard ${eVar} instanceof Error unwrapping in catch block`, line);
      }
    }
    
    if (line.includes(' as any') || line.includes('@ts-ignore') || line.includes('as unknown as')) {
      addFinding(relPath, lineNum, 'R-08', 'HIGH', 'Type bypass detected (any cast or @ts-ignore)', line);
    }
    
    if (line.match(/for\s*\(.*(of|in)\s*devices\)/) && content.slice(content.indexOf(line)).indexOf('await write') < 200) {
      addFinding(relPath, lineNum, 'R-10', 'MEDIUM', 'Sequential group writes instead of concurrent Promise.all', line);
    }
    
    if (line.match(/Promise\.all\(\s*.*connectToDevice/)) {
      addFinding(relPath, lineNum, 'R-13', 'HIGH', 'Promise.all used on sequential device connections (must be serial)', line);
    }
    
    if (line.match(/setTimeout\s*\(\s*.*,\s*[0-9]{3,4}\s*\)/) && !line.includes('BLE_TIMING')) {
      addFinding(relPath, lineNum, 'R-16', 'LOW', 'Hardcoded delay literal in setTimeout (use named constant)', line);
    }
    
    if (line.includes('useEffect(')) {
      inUseEffect = true;
      useEffectStart = i;
      useEffectHasReturn = false;
    }
    if (inUseEffect) {
      if (line.includes('return () =>') || line.includes('return function cleanup()')) {
        useEffectHasReturn = true;
      }
      if (line.trim() === '},' || line.trim() === '});' || line.trim() === '}') {
         const block = lines.slice(useEffectStart, i).join('\n');
         if ((block.includes('addListener') || block.includes('subscribe')) && !useEffectHasReturn) {
             addFinding(relPath, useEffectStart + 1, 'R-17', 'HIGH', 'Missing useEffect cleanup for subscriptions/listeners', lines[useEffectStart]);
         }
         inUseEffect = false;
      }
    }
    
    if (line.includes('[0x59') || line.includes('[0x55') || line.includes('[0x42')) {
      if (!relPath.includes('protocols')) {
        addFinding(relPath, lineNum, 'R-19', 'HIGH', 'Raw byte array constructed outside protocols folder', line);
      }
    }
    
    if (line.includes("Platform.OS ===") && !line.includes("Platform.select")) {
      addFinding(relPath, lineNum, 'R-20', 'MEDIUM', 'Missing Platform.select(), blind cross-platform assumptions', line);
    }
    
    if (line.includes('setInterval') && !content.includes('clearInterval')) {
      addFinding(relPath, lineNum, 'R-22', 'HIGH', 'setInterval used without matching clearInterval', line);
    }
    
    if (line.match(/(requestMTU|requestConnectionPriority)/)) {
      let guarded = false;
      for (let j = Math.max(0, i - 5); j < Math.min(lines.length, i + 5); j++) {
        if (lines[j].includes('Platform.OS') || lines[j].includes('Platform.select')) guarded = true;
      }
      if (!guarded) {
        addFinding(relPath, lineNum, 'R-25', 'HIGH', 'Unguarded platform-specific BLE API', line);
      }
    }
  }
}

const report = {
  agent_id: "QA_AUDITOR",
  agent_type: "auditor",
  domain_or_rule: "BLE_CORE",
  files_scanned: DOMAIN_FILES.length,
  completion_status: "COMPLETE",
  error_details: null,
  findings: findings
};

fs.writeFileSync('C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/deepdive_raw/DOMAIN_BLE_CORE_findings.json', JSON.stringify(report, null, 2));
console.log('Wrote findings!');
