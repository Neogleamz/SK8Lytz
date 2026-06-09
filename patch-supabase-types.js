const fs = require('fs');
const paths = [
  'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz-worktrees/feat-crash-telemetry/tools/command-center/src/types/supabase.ts',
  'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz-worktrees/feat-crash-telemetry/src/types/supabase.ts'
];

const newTableString = `
      crash_telemetry: {
        Row: {
          id: string
          user_id: string | null
          error_signature: string
          stack_trace: string | null
          breadcrumbs: Json | null
          environment_state: Json | null
          severity: string | null
          status: string | null
          app_version: string | null
          created_at: string | null
          resolved_at: string | null
          resolved_by: string | null
        }
        Insert: {
          id?: string
          user_id?: string | null
          error_signature: string
          stack_trace?: string | null
          breadcrumbs?: Json | null
          environment_state?: Json | null
          severity?: string | null
          status?: string | null
          app_version?: string | null
          created_at?: string | null
          resolved_at?: string | null
          resolved_by?: string | null
        }
        Update: {
          id?: string
          user_id?: string | null
          error_signature?: string
          stack_trace?: string | null
          breadcrumbs?: Json | null
          environment_state?: Json | null
          severity?: string | null
          status?: string | null
          app_version?: string | null
          created_at?: string | null
          resolved_at?: string | null
          resolved_by?: string | null
        }
        Relationships: []
      }`;

paths.forEach(p => {
  try {
    let content = fs.readFileSync(p, 'utf8');
    if (content.includes('crash_telemetry: {')) {
      console.log(p + ' already updated');
      return;
    }
    const replaceStr = "Tables: {\n" + newTableString;
    content = content.replace("Tables: {", replaceStr);
    fs.writeFileSync(p, content, 'utf8');
    console.log("Patched " + p);
  } catch (e) {
    console.error("Error patching " + p + ": " + e.message);
  }
});
