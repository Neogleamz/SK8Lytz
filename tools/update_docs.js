const fs = require('fs');
const path = require('path').join(__dirname, '..', 'docs', 'SK8Lytz_App_Master_Reference.md');
let content = fs.readFileSync(path, 'utf8');

const insertion = `

### Hardware Identification & Connection Routing (Identity Architecture)

1. **Hardware Identity (MAC over UUID)**: The single source of truth for connecting to and identifying hardware is the **BLE MAC address** (\`device_mac\`). The Supabase DB UUID (\`id\`) MUST NEVER be used to route BLE connections or resolve live components. DB UUIDs change upon un-sync/re-sync, whereas the MAC address is immutable hardware truth.
2. **Group Connection Ground Truth**: The authoritative state for whether the app is controlling a grouped session is \`connectedDevices.length > 1\`. Checking \`DisplayDevice.groupId\` or \`grouped\` flags is strictly forbidden, as it relies on secondary lookups and fragile optional typings. \`deviceConfigs\` stores \`groupIds\` (plural array) for many-to-many associations, but active BLE command routing relies purely on the array size of live GATT connections in the \`BleMachine\`.
`;

content = content.replace(
  '> - ~~`ng_processed_devices`~~ → DELETED (one-shot cleanup on boot)',
  '> - ~~`ng_processed_devices`~~ → DELETED (one-shot cleanup on boot)' + insertion
);

fs.writeFileSync(path, content, 'utf8');
console.log('Successfully updated Master Reference');
