const fs = require('fs');
const file = 'src/components/dashboard/SkateGroupCard.tsx';
let data = fs.readFileSync(file, 'utf8');

if (!data.includes('lastSeen?: Record<string, number>')) {
  data = data.replace('connectionStates?: Record<string, DeviceConnectionState>;', `connectionStates?: Record<string, DeviceConnectionState>;
  lastSeen?: Record<string, number>;`);
  fs.writeFileSync(file, data);
}
