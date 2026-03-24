const { createClient } = require('@supabase/supabase-js');
const fs = require('fs');
const path = require('path');

// Read .env file manually since dotenv might not be installed
const envPath = path.join(__dirname, '.env');
const envContent = fs.readFileSync(envPath, 'utf8');
const envVars = {};
envContent.split('\n').forEach(line => {
  const match = line.match(/^([^=]+)=(.*)$/);
  if (match) envVars[match[1].trim()] = match[2].trim();
});

const supabaseUrl = envVars['EXPO_PUBLIC_SUPABASE_URL'];
const supabaseKey = envVars['EXPO_PUBLIC_SUPABASE_ANON_KEY'];

if (!supabaseUrl || !supabaseKey) {
  console.error('Missing Supabase credentials in .env');
  process.exit(1);
}

const supabase = createClient(supabaseUrl, supabaseKey);

async function testUpload() {
  console.log('Testing upload to Supabase bucket: sk8lytz-logs...');
  
  const dummyContent = JSON.stringify({ test: "This is a dummy upload test", timestamp: new Date().toISOString() });
  const filename = `dummy_${Date.now()}.json`;

  const { data, error } = await supabase.storage
    .from('sk8lytz-logs')
    .upload(filename, dummyContent, {
      contentType: 'application/json',
      upsert: true,
    });

  if (error) {
    console.error('❌ Upload failed:', error.message);
    if (error.message.includes('Bucket not found')) {
      console.error('Make sure the bucket "sk8lytz-logs" exists and is public.');
    } else if (error.message.includes('row-level security')) {
      console.error('Make sure the bucket has an INSERT policy for anon users.');
    }
  } else {
    console.log('✅ Upload successful! File path:', data.path);
  }
}

testUpload();
