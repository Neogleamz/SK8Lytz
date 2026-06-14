const fs = require('fs');
const path = require('path');

const CARTOGRAPHY_DIR = path.join(__dirname, '../artifacts/deepdive_docs');
const MASTER_REF_PATH = path.join(__dirname, '../docs/SK8Lytz_App_Master_Reference.md');

let masterRef = fs.readFileSync(MASTER_REF_PATH, 'utf-8');

const domains = [
    'IDENTITY', 'BLE_CORE', 'GROUP_SYNC', 'UI_SCREENS', 'UI_DOCKED_CONTROLLER',
    'UI_MODALS', 'UI_VISUALIZER', 'DATA_LAYER', 'UTILS', 'NATIVE_&_WATCH',
    'NOTIFICATIONS_&_ROUTING', 'SESSION_TRACKING', 'PROTOCOL_CORE', 'PATTERN_ENGINE',
    'CLOUD_FUNCTIONS', 'THEME_&_ASSETS', 'SIMULATION_&_MOCKS', 'BUILD_CONFIG',
    'OS_PERMISSIONS', 'ADMIN_&_TELEMETRY', 'DEPENDENCY_AUDIT'
];

let archiveEntries = [];

domains.forEach(domain => {
    const filePath = path.join(CARTOGRAPHY_DIR, `${domain}_cartography.md`);
    if (!fs.existsSync(filePath)) {
        console.warn(`Missing file for domain: ${domain}`);
        return;
    }

    let content = fs.readFileSync(filePath, 'utf-8');

    // Extract [MOVE_TO_ARCHIVE] lines/blocks
    const archiveRegex = /\[MOVE_TO_ARCHIVE\](.*?)(?=\n\n|\n\[|$)/gs;
    let match;
    while ((match = archiveRegex.exec(content)) !== null) {
        archiveEntries.push(`- **From ${domain}**: ` + match[1].trim());
    }

    // Attempt to inject content between markers
    const startMarker = `<!-- CARTOGRAPHER_START: ${domain} -->`;
    const endMarker = `<!-- CARTOGRAPHER_END: ${domain} -->`;
    
    const startIndex = masterRef.indexOf(startMarker);
    const endIndex = masterRef.indexOf(endMarker);

    if (startIndex !== -1 && endIndex !== -1 && endIndex > startIndex) {
        masterRef = masterRef.substring(0, startIndex + startMarker.length) + '\n\n' + content + '\n\n' + masterRef.substring(endIndex);
    } else {
        console.warn(`Markers not found for domain: ${domain}`);
    }
});

// Append to Graveyard
if (archiveEntries.length > 0) {
    const graveyardMarker = '## 13. Historical Archive (The Graveyard)';
    const graveyardIndex = masterRef.indexOf(graveyardMarker);
    if (graveyardIndex !== -1) {
        masterRef = masterRef.substring(0, graveyardIndex + graveyardMarker.length) + '\n\n' + archiveEntries.join('\n') + '\n\n' + masterRef.substring(graveyardIndex + graveyardMarker.length);
    } else {
        masterRef += '\n\n## 13. Historical Archive (The Graveyard)\n\n' + archiveEntries.join('\n');
    }
}

fs.writeFileSync(MASTER_REF_PATH, masterRef);
console.log('Synthesis complete.');
