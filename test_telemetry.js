const fs = require('fs');

const env = fs.readFileSync('.env', 'utf8');
const urlMatch = env.match(/EXPO_PUBLIC_SUPABASE_URL=(.*)/);
const keyMatch = env.match(/EXPO_PUBLIC_SUPABASE_ANON_KEY=(.*)/);

const url = urlMatch ? urlMatch[1].trim() : '';
const key = keyMatch ? keyMatch[1].trim() : '';

if (!url || !key) {
  console.error("Missing config");
  process.exit(1);
}

async function runTest() {
  console.log("Attempting to insert telemetry error...");
  const res = await fetch(`${url}/rest/v1/device_diagnostics`, {
    method: 'POST',
    headers: {
      'apikey': key,
      'Authorization': `Bearer ${key}`,
      'Content-Type': 'application/json',
      'Prefer': 'return=representation'
    },
    body: JSON.stringify({
      device_id: "00:11:22:33:44:55",
      device_name: "Test Mac",
      payload_hex: "0x00",
      payload_bytes: 1,
      parsed_ok: true
    })
  });

  if (!res.ok) {
    console.error("❌ FAILED to insert into telemetry_errors:", res.status, res.statusText);
    const text = await res.text();
    console.error(text);
  } else {
    const data = await res.json();
    console.log("✅ SUCCESSFULLY inserted into telemetry_errors:", data);
  }
}

runTest();
