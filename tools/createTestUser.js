import { createClient } from '@supabase/supabase-js';

const SUPABASE_URL = 'https://qefmeivpjyaukbwadgaz.supabase.co';
const SUPABASE_ANON_KEY = process.env.SUPABASE_TEST_USER_JWT ?? (() => { throw new Error('SUPABASE_TEST_USER_JWT env var required'); })();

const supabase = createClient(SUPABASE_URL, SUPABASE_ANON_KEY);

async function createTestUser() {
  console.log("Attempting to create test user...");
  const { data, error } = await supabase.auth.signUp({
    email: 'testuser@sk8lytz.com',
    password: process.env.TEST_USER_PASSWORD ?? 'ChangeMe!LocalOnly',
    options: {
      data: {
        username: 'TestSkater'
      }
    }
  });

  if (error) {
    console.error("Failed to create test user:", error);
  } else {
    console.log("Test user created successfully!", data);
  }
}

createTestUser();
