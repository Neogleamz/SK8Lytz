const fs = require('fs');
const path = require('path');

const domains = [
  { marker: 'IDENTITY', file: 'IDENTITY_cartography.md' },
  { marker: 'BLE_CORE', file: 'BLE_CORE_cartography.md' },
  { marker: 'GROUP_SYNC', file: 'GROUP_SYNC_cartography.md' },
  { marker: 'UI_SCREENS', file: 'UI_SCREENS_cartography.md' },
  { marker: 'UI_DOCKED_CONTROLLER', file: 'UI_DOCKED_CONTROLLER_cartography.md' },
  { marker: 'UI_MODALS', file: 'UI_MODALS_cartography.md' },
  { marker: 'UI_VISUALIZER', file: 'UI_VISUALIZER_cartography.md' },
  { marker: 'DATA_LAYER', file: 'DATA_LAYER_cartography.md' },
  { marker: 'UTILS', file: 'UTILS_cartography.md' },
  { marker: 'NATIVE_&_WATCH', file: 'NATIVE_&_WATCH_cartography.md' },
  { marker: 'NOTIFICATIONS_&_ROUTING', file: 'NOTIFICATIONS_&_ROUTING_cartography.md' },
  { marker: 'SESSION_TRACKING', file: 'SESSION_TRACKING_cartography.md' },
  { marker: 'PROTOCOL_CORE', file: 'PROTOCOL_CORE_cartography.md' },
  { marker: 'PATTERN_ENGINE', file: 'PATTERN_ENGINE_cartography.md' },
  { marker: 'CLOUD_FUNCTIONS', file: 'CLOUD_FUNCTIONS_cartography.md' },
  { marker: 'THEME_&_ASSETS', file: 'THEME_&_ASSETS_cartography.md' },
  { marker: 'SIMULATION_&_MOCKS', file: 'SIMULATION_&_MOCKS_cartography.md' },
  { marker: 'BUILD_CONFIG', file: 'BUILD_CONFIG_cartography.md' },
  { marker: 'OS_PERMISSIONS', file: 'OS_PERMISSIONS_cartography.md' },
  { marker: 'ADMIN_&_TELEMETRY', file: 'ADMIN_&_TELEMETRY_cartography.md' },
  { marker: 'DEPENDENCY_AUDIT', file: 'DEPENDENCY_AUDIT_cartography.md' }
];

const masterRefPath = path.join(__dirname, '../docs/SK8Lytz_App_Master_Reference.md');
const cartographyDir = path.join(__dirname, '../artifacts/deepdive_docs');

function main() {
  console.log('Starting Master Reference Synthesis...');
  let masterContent = fs.readFileSync(masterRefPath, 'utf8');

  let graveyardAdditions = [];

  for (const domain of domains) {
    const filePath = path.join(cartographyDir, domain.file);
    if (!fs.existsSync(filePath)) {
      console.warn(`Warning: Cartography file not found for ${domain.marker}: ${filePath}`);
      continue;
    }

    let cartContent = fs.readFileSync(filePath, 'utf8');

    // Extract [MOVE_TO_ARCHIVE] lines/sections
    const lines = cartContent.split('\n');
    let inArchivalInstruction = false;
    let archivalBlock = [];
    
    for (let i = 0; i < lines.length; i++) {
      const line = lines[i];
      if (line.includes('[MOVE_TO_ARCHIVE]') || line.includes('ARCHIVAL INSTRUCTION')) {
        inArchivalInstruction = true;
      }
      
      if (inArchivalInstruction) {
        // Collect lines until next major header or separator
        if (line.trim().startsWith('---') || (line.trim().startsWith('#') && !line.includes('ARCHIVAL'))) {
          inArchivalInstruction = false;
        } else {
          archivalBlock.push(line);
        }
      }
    }

    if (archivalBlock.length > 0) {
      const cleanedBlock = archivalBlock
        .join('\n')
        .replace(/###\s*🗄️\s*ARCHIVAL INSTRUCTION/g, '')
        .replace(/ARCHIVAL INSTRUCTION/g, '')
        .trim();
      if (cleanedBlock) {
        graveyardAdditions.push(`- **From ${domain.marker}**:\n${cleanedBlock}`);
      }
    }

    // Inject payload between boundary markers in Master Reference
    const startMarker = `<!-- CARTOGRAPHER_START: ${domain.marker} -->`;
    const endMarker = `<!-- CARTOGRAPHER_END: ${domain.marker} -->`;

    const startIndex = masterContent.indexOf(startMarker);
    const endIndex = masterContent.indexOf(endMarker);

    if (startIndex === -1 || endIndex === -1) {
      console.error(`Error: Boundary markers not found in Master Reference for domain ${domain.marker}`);
      process.exit(1);
    }

    const before = masterContent.substring(0, startIndex + startMarker.length);
    const after = masterContent.substring(endIndex);

    // Clean up cartContent formatting for injection
    masterContent = before + '\n\n' + cartContent.trim() + '\n\n' + after;
    console.log(`Successfully injected ${domain.marker} cartography.`);
  }

  // Inject graveyard additions
  if (graveyardAdditions.length > 0) {
    const graveyardMarker = '## 13. Historical Archive (The Graveyard)';
    const graveyardIndex = masterContent.indexOf(graveyardMarker);
    if (graveyardIndex !== -1) {
      const insertionPoint = graveyardIndex + graveyardMarker.length;
      const beforeGraveyard = masterContent.substring(0, insertionPoint);
      const afterGraveyard = masterContent.substring(insertionPoint);
      masterContent = beforeGraveyard + '\n\n' + graveyardAdditions.join('\n\n') + '\n\n' + afterGraveyard;
      console.log(`Successfully appended ${graveyardAdditions.length} items to the Graveyard.`);
    } else {
      console.warn('Warning: Graveyard header not found. Appending additions to the end of the file.');
      masterContent += '\n\n## 13. Historical Archive (The Graveyard)\n\n' + graveyardAdditions.join('\n\n');
    }
  }

  fs.writeFileSync(masterRefPath, masterContent, 'utf8');
  console.log('Synthesis completed successfully!');
}

main();
