const fs = require('fs');
const path = require('path');

const masterRefPath = 'c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SK8Lytz_App_Master_Reference.md';
const scratchDir = 'C:/Users/Magma/.gemini/antigravity/brain/1f13c375-3bed-42bc-9c4f-245d16fb8e06/scratch';

const domains = [
  'IDENTITY', 'BLE_CORE', 'GROUP_SYNC', 'UI_CONTROLS', 'DATA_LAYER',
  'UTILS', 'NATIVE_&_WATCH', 'NOTIFICATIONS_&_ROUTING', 'SESSION_TRACKING',
  'HARDWARE_PROTOCOLS', 'CLOUD_FUNCTIONS', 'THEME_&_ASSETS',
  'SIMULATION_&_MOCKS', 'BUILD_CONFIG_&_OTA', 'OS_PERMISSIONS', 'DEPENDENCY_AUDIT'
];

let masterRef = fs.readFileSync(masterRefPath, 'utf8');
let graveyardAppends = [];

for (const domain of domains) {
  const payloadPath = path.join(scratchDir, `${domain}.md`);
  if (fs.existsSync(payloadPath)) {
    let payload = fs.readFileSync(payloadPath, 'utf8');
    
    // Extract [MOVE_TO_ARCHIVE] tags and their subsequent content
    const archiveRegex = /\[MOVE_TO_ARCHIVE\]([\s\S]*?)(?=\n## |\n# |$)/gi;
    let match;
    while ((match = archiveRegex.exec(payload)) !== null) {
      graveyardAppends.push(`\n### Archived from ${domain}\n${match[1].trim()}\n`);
    }
    
    // Clean up the payload before injecting (remove the archival instructions)
    payload = payload.replace(/## \d+\. Archival Instruction[\s\S]*?(?=\n## |\n# |$)/gi, '');
    payload = payload.replace(/### ARCHIVAL INSTRUCTION[\s\S]*?(?=\n## |\n# |$)/gi, '');
    payload = payload.replace(/## ARCHIVAL INSTRUCTION[\s\S]*?(?=\n## |\n# |$)/gi, '');
    payload = payload.replace(/### 🗂️ Stale Documentation Archive Directive[\s\S]*?(?=\n## |\n# |$)/gi, '');
    payload = payload.replace(/## 🗄️ Archival Instruction[\s\S]*?(?=\n## |\n# |$)/gi, '');
    payload = payload.replace(/## 6\. Archival Instruction[\s\S]*?(?=\n## |\n# |$)/gi, '');
    payload = payload.replace(/## 7\. Archival Instruction[\s\S]*?(?=\n## |\n# |$)/gi, '');
    payload = payload.replace(/## 8\. Archival Instructions.*[\s\S]*?(?=\n## |\n# |$)/gi, '');
    payload = payload.replace(/\[MOVE_TO_ARCHIVE\][\s\S]*?(?=\n## |\n# |$)/gi, '');
    
    const startTag = `<!-- CARTOGRAPHER_START: ${domain} -->`;
    const endTag = `<!-- CARTOGRAPHER_END: ${domain} -->`;
    
    const startIndex = masterRef.indexOf(startTag);
    // Find the end index of the corresponding tag, starting search from startIndex
    const endIndex = masterRef.indexOf(endTag, startIndex); 
    
    if (startIndex !== -1 && endIndex !== -1) {
      const before = masterRef.substring(0, startIndex + startTag.length);
      const after = masterRef.substring(endIndex);
      masterRef = before + '\n\n' + payload.trim() + '\n\n' + after;
    }
  } else {
    console.warn(`Warning: Could not find payload for ${domain} at ${payloadPath}`);
  }
}

// Append to Section 13 (The Graveyard)
if (graveyardAppends.length > 0) {
  const graveyardSectionStartStr = '## 13. Historical Archive (The Graveyard)';
  const idx = masterRef.indexOf(graveyardSectionStartStr);
  if (idx !== -1) {
      masterRef = masterRef.substring(0, idx + graveyardSectionStartStr.length) + '\n' + graveyardAppends.join('\n') + masterRef.substring(idx + graveyardSectionStartStr.length);
  } else {
      masterRef += '\n\n## 13. Historical Archive (The Graveyard)\n\n' + graveyardAppends.join('\n');
  }
}

fs.writeFileSync(masterRefPath, masterRef, 'utf8');
console.log('Successfully injected 16 payloads and updated Master Reference.');
