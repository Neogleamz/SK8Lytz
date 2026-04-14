const fs = require('fs');

const env = fs.readFileSync('.env', 'utf8');
const urlMatch = env.match(/EXPO_PUBLIC_SUPABASE_URL=(.*)/);
const keyMatch = env.match(/EXPO_PUBLIC_SUPABASE_ANON_KEY=(.*)/);

const url = urlMatch ? urlMatch[1].trim() : '';
const key = keyMatch ? keyMatch[1].trim() : '';

async function fetchErrors() {
  console.log("Fetching latest unhandled telemetry errors from Supabase...");
  const res = await fetch(`${url}/rest/v1/telemetry_errors?select=*&order=created_at.desc&limit=10`, {
    method: 'GET',
    headers: {
      'apikey': key,
      'Authorization': `Bearer ${key}`
    }
  });

  if (res.ok) {
     console.log("\n--- TELEMETRY ERRORS ---");
     const d3 = await res.json();
     d3.forEach(e => console.log(JSON.stringify(e)));
  }

  const res2 = await fetch(`${url}/rest/v1/remote_debug_logs?select=*&order=created_at.desc&limit=5`, {
    method: 'GET',
    headers: {
      'apikey': key,
      'Authorization': `Bearer ${key}`
    }
  });
  
  if (res2.ok) {
     console.log("\n--- REMOTE DEBUG LOGS ---");
     const d4 = await res2.json();
     d4.forEach(e => console.log(JSON.stringify(e)));
  }
}

fetchErrors();
