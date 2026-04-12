import { createClient } from '@supabase/supabase-js';
import * as dotenv from 'dotenv';
import * as path from 'path';

// Load the local .env to get Supabase URL and anon key
dotenv.config({ path: path.resolve(__dirname, '.env') });

const supabaseUrl = process.env.EXPO_PUBLIC_SUPABASE_URL || '';
const supabaseAnonKey = process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY || '';

if (!supabaseUrl || !supabaseAnonKey) {
  console.error("Missing supabase config in .env");
  process.exit(1);
}

const supabase = createClient(supabaseUrl, supabaseAnonKey);

async function testTelemetryInsert() {
  console.log("Attempting to insert into telemetry_errors...");
  const { data, error } = await supabase
    .from('telemetry_errors')
    .insert([
      { 
        error_type: "TestLog",
        message: "This is a test of the permissive RLS policy",
        stack_trace: "No stack",
        component: "AutomatedTest",
        app_version: "1.0.0",
        os_version: "windows",
        device_model: "test_runner",
        is_fatal: false
      }
    ])
    .select();

  if (error) {
    console.error("❌ FAILED to insert into telemetry_errors:", error.message);
    process.exit(1);
  } else {
    console.log("✅ SUCCESSFULLY inserted into telemetry_errors:", data);
  }

  console.log("\nAttempting to insert into device_diagnostics...");
  const { data: diagData, error: diagError } = await supabase
    .from('device_diagnostics')
    .insert([
      {
        device_mac: "00:11:22:33:44:55",
        event_type: "BLE_TEST",
        event_data: { test: "success" }
      }
    ])
    .select();

  if (diagError) {
    console.error("❌ FAILED to insert into device_diagnostics:", diagError.message);
    process.exit(1);
  } else {
    console.log("✅ SUCCESSFULLY inserted into device_diagnostics:", diagData);
  }
}

testTelemetryInsert();
