const { Client } = require('pg');
const fs = require('fs');
require('dotenv').config();

async function run() {
  const connectionString = `postgresql://postgres:${process.env.SUPABASE_DB_PASSWORD}@db.qefmeivpjyaukbwadgaz.supabase.co:5432/postgres`;
  const client = new Client({ connectionString });
  
  try {
    await client.connect();
    console.log("Connected to Supabase PostgreSQL.");
    const sql = fs.readFileSync('supabase/migrations/20260609130000_app_settings_visibility.sql', 'utf8');
    await client.query(sql);
    console.log("Migration applied successfully!");
  } catch (err) {
    console.error("Error applying migration:", err);
  } finally {
    await client.end();
  }
}

run();
