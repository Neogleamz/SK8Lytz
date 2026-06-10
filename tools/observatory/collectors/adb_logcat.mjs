import { execSync } from 'child_process';
import crypto from 'crypto';

export async function collectAdbLogcat() {
  const records = [];
  try {
    const devices = execSync('adb devices').toString();
    if (!devices.includes('\\tdevice')) return records;
    
    const logcat = execSync('adb logcat -d -s "ReactNativeJS:V" "AndroidRuntime:E"').toString();
    const lines = logcat.split('\\n');
    
    for (const line of lines) {
      if (line.includes('FATAL EXCEPTION') || line.includes('Error:')) {
        records.push({
          id: `adb-${Date.now()}-${Math.random()}`,
          fingerprint: line.substring(0, 50),
          source: 'ADB_LOGCAT',
          collectedAt: new Date().toISOString(),
          file: 'adb-logcat',
          message: line.substring(0, 100),
          severity: line.includes('FATAL') ? 'CRITICAL' : 'HIGH',
          domain: 'CORE',
          errorType: 'CRASH',
          occurrences: 1,
          firstSeen: new Date().toISOString(),
          lastSeen: new Date().toISOString(),
          trend: 'NEW',
          urgencyScore: 0,
          autoHealCandidate: false,
          requiredExpertise: ['River'],
          rawPayload: line
        });
      }
    }
  } catch(e) {}
  return records;
}

if (process.argv[1] === import.meta.url) {
  collectAdbLogcat().then(res => console.log(JSON.stringify(res, null, 2)));
}
