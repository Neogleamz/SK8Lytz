import { createClient } from '@supabase/supabase-js';

const SUPABASE_URL = 'https://qefmeivpjyaukbwadgaz.supabase.co';
const SUPABASE_ANON_KEY = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFlZm1laXZwanlhdWtid2FkZ2F6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzM0MzUyMjAsImV4cCI6MjA4OTAxMTIyMH0.TtBAAL7RPk-w8Q_IGbhPouBjcdjyCRXKy_D5YS4FQss';

const supabase = createClient(SUPABASE_URL, SUPABASE_ANON_KEY);

async function createTestUser() {
  console.log("Attempting to create test user...");
  const { data, error } = await supabase.auth.signUp({
    email: 'testuser@sk8lytz.com',
    password: 'Password!2026',
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
