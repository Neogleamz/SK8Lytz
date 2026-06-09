import React, { useEffect, useState } from 'react';
import { supabase } from '../../services/supabase';
import { AgGridReact } from 'ag-grid-react';
import { themeQuartz, colorSchemeDark } from 'ag-grid-community';

const myTheme = themeQuartz.withPart(colorSchemeDark).withParams({
  backgroundColor: "transparent",
  foregroundColor: "#cbd5e1",
  headerBackgroundColor: "rgba(15, 23, 42, 0.4)",
  headerTextColor: "#38bdf8",
  rowHoverColor: "rgba(34, 211, 238, 0.1)",
  selectedRowBackgroundColor: "rgba(56, 189, 248, 0.15)",
  borderColor: "rgba(51, 65, 85, 0.5)",
  fontFamily: '"Inter", sans-serif',
});

interface AuditLog {
  id: string;
  admin_id: string;
  target_user_id: string;
  action: string;
  reason: string | null;
  created_at: string;
}

interface UserProfile {
  user_id: string;
  username: string | null;
  display_name: string | null;
}

export default function AuditLogsWidget() {
  const [logs, setLogs] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchLogs();
  }, []);

  const fetchLogs = async () => {
    setLoading(true);
    // Fetch logs and profiles manually since FKs point to auth.users, not public.user_profiles
    const [{ data: auditData }, { data: profilesData }] = await Promise.all([
      supabase.from('admin_audit_logs').select('*').order('created_at', { ascending: false }).limit(500),
      supabase.from('user_profiles').select('user_id, username, display_name')
    ]);

    if (auditData && profilesData) {
      const profilesMap = new Map((profilesData as UserProfile[]).map(p => [p.user_id, p]));
      
      const enrichedLogs = (auditData as AuditLog[]).map(log => {
        const admin = profilesMap.get(log.admin_id);
        const target = log.target_user_id ? profilesMap.get(log.target_user_id) : null;
        
        return {
          ...log,
          admin_name: admin?.display_name || admin?.username || log.admin_id,
          target_name: target?.display_name || target?.username || log.target_user_id || 'System/Global',
        };
      });
      setLogs(enrichedLogs);
    }
    setLoading(false);
  };

  const gridStyle = { height: 500, width: "100%", border: "1px solid rgba(51,65,85,0.5)", borderRadius: "8px", overflow: "hidden" };

  return (
    <div className="glass-panel p-6 rounded-2xl w-full max-w-5xl mx-auto mt-8">
      <h2 className="text-2xl font-bold text-[var(--accent)] mb-4">Audit Logs</h2>
      <p className="text-[var(--text-muted)] mb-6">Track command center actions and admin history.</p>
      
      {loading ? (
        <div className="text-cyan-400 p-8 animate-pulse text-center">Loading Audit Logs...</div>
      ) : (
        <div className="glass-panel" style={gridStyle}>
          <AgGridReact
            theme={myTheme}
            rowData={logs}
            columnDefs={[
              { field: "created_at", headerName: "Timestamp", width: 220, valueFormatter: p => new Date(p.value).toLocaleString() },
              { field: "admin_name", headerName: "Admin", flex: 1 },
              { field: "action", headerName: "Action", width: 180 },
              { field: "target_name", headerName: "Target User", flex: 1 },
              { field: "reason", headerName: "Reason", flex: 1 }
            ]}
            rowSelection={{ mode: "singleRow" }}
          />
        </div>
      )}
    </div>
  );
}
