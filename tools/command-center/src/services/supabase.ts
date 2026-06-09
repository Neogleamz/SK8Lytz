import { createClient } from '@supabase/supabase-js';
import { Database } from '../types/supabase';

// Use standard Vite environment variables
const supabaseUrl = import.meta.env.VITE_SUPABASE_URL || '';
const supabaseAnonKey = import.meta.env.VITE_SUPABASE_ANON_KEY || '';

export const supabase = createClient<Database>(supabaseUrl, supabaseAnonKey);

export async function checkAdminStatus(): Promise<boolean> {
  const { data: { user } } = await supabase.auth.getUser();
  if (!user) return false;
  
  // In the SK8Lytz schema, check admin_audit_logs or user_profiles for admin role
  const { data } = await supabase
    .from('user_profiles')
    .select('role')
    .eq('user_id', user.id)
    .single();
    
  return data?.role === 'admin';
}
