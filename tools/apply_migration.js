const { Client } = require('pg');
const fs = require('fs');

try {
  process.loadEnvFile('.env');
} catch (e) {
  console.log("Could not load .env file");
}

async function run() {
  const file = process.argv[2] || 'supabase/migrations/20260609130000_app_settings_visibility.sql';
  console.log("Applying migration:", file);
  const connectionString = `postgresql://postgres:${process.env.SUPABASE_DB_PASSWORD}@db.qefmeivpjyaukbwadgaz.supabase.co:5432/postgres`;
  const client = new Client({ connectionString });
  
  try {
    await client.connect();
    console.log("Connected to Supabase PostgreSQL.");
    const sql = fs.readFileSync(file, 'utf8');
    await client.query(sql);
    console.log("Migration applied successfully!");
  } catch (err) {
    console.error("Error applying migration:", err);
  } finally {
    await client.end();
  }
}

run();

run();
