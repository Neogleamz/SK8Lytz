const fs = require('fs');
const path = require('path');
const { createClient } = require('@supabase/supabase-js');

const envContent = fs.readFileSync(path.resolve(__dirname, '.env'), 'utf8');
const envLines = envContent.split('\n');
const envMap = {};
envLines.forEach(line => {
  const [key, ...val] = line.split('=');
  if (key && val) {
    envMap[key.trim()] = val.join('=').trim().replace(/['"]/g, '');
  }
});

const supabaseUrl = envMap.EXPO_PUBLIC_SUPABASE_URL;
const supabaseKey = envMap.EXPO_PUBLIC_SUPABASE_ANON_KEY;

const supabase = createClient(supabaseUrl, supabaseKey);

async function check() {
  const { data: rawDevices, error: devError } = await supabase
      .from('registered_devices')
      .select('id, user_id, custom_name, product_type, firmware_ver, is_pending_sync, ic_type, device_mac, registered_at, points');

  console.log("Error:", JSON.stringify(devError));
  console.log("Data length:", rawDevices?.length);
  
  if (devError) console.error(devError);
}

check();
