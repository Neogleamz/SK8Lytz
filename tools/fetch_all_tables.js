const fs = require('fs');

const env = fs.readFileSync('.env', 'utf8');
const urlMatch = env.match(/EXPO_PUBLIC_SUPABASE_URL=(.*)/);
const keyMatch = env.match(/EXPO_PUBLIC_SUPABASE_ANON_KEY=(.*)/);

const url = urlMatch ? urlMatch[1].trim() : '';
const key = keyMatch ? keyMatch[1].trim() : '';

async function fetchTables() {
  const res = await fetch(`${url}/rest/v1/`, {
    headers: {
      'apikey': key,
      'Authorization': `Bearer ${key}`
    }
  });
  const data = await res.json();
  console.log("TABLES IN DB:", Object.keys(data.paths).filter(k => k !== '/'));

  // Let's query app_logs, telemetry_errors, or anything similar
  const tables = ['app_logs', 'app_errors', 'diagnostics', 'telemetry', 'telemetry_errors', 'device_diagnostics'];
  
  for (const t of tables) {
    if (data.paths[`/${t}`]) {
      const tRes = await fetch(`${url}/rest/v1/${t}?select=*&limit=3`, {
        headers: { 'apikey': key, 'Authorization': `Bearer ${key}` }
      });
      if (tRes.ok) {
        console.log(`\n--- TABLE: ${t} ---`);
        console.log(await tRes.json());
      }
    }
  }
}
fetchTables();
