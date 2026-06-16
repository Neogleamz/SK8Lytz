const { Client } = require('pg');
process.loadEnvFile('.env');
async function run() {
  const client = new Client({ connectionString: `postgresql://postgres:${process.env.SUPABASE_DB_PASSWORD}@db.qefmeivpjyaukbwadgaz.supabase.co:5432/postgres` });
  await client.connect();
  const res = await client.query(`SELECT count(*) FROM telemetry_snapshots WHERE event_type = 'anonymous_performance_dump'`);
  console.log('Before count:', res.rows[0].count);
  
  // Call the function
  await client.query(`SELECT public.flush_telemetry('{"total_app_time_sec": 100}'::jsonb)`);
  
  const res2 = await client.query(`SELECT count(*) FROM telemetry_snapshots WHERE event_type = 'anonymous_performance_dump'`);
  console.log('After count:', res2.rows[0].count);
  await client.end();
}
run();
