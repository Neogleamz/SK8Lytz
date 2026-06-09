import React, { useEffect, useState, useMemo } from "react";
import { supabase } from "../../services/supabase";
import { AgGridReact } from "ag-grid-react";
import { themeQuartz, colorSchemeDark } from "ag-grid-community";

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

const SectionHdr = ({ label, color }: { label: string; color: string }) => (
  <div style={{ display: "flex", alignItems: "center", gap: "8px", marginBottom: "8px", marginTop: "16px" }}>
    <div style={{ width: "8px", height: "8px", borderRadius: "50%", backgroundColor: color }} />
    <h3 style={{ margin: 0, color: "#fff", fontSize: "14px", textTransform: "uppercase", letterSpacing: "0.05em" }}>{label}</h3>
  </div>
);


export interface FilteredIds {
  users: Set<string>;
  devices: Set<string>;
  sessions: Set<string>;
  crews: Set<string>;
  crewSessions: Set<string>;
}

export interface RelationalDataBankWidgetProps {
  activeEntity: { id: string, type: string } | null;
  onEntitySelected: (entity: { id: string, type: "users" | "crews" | "registeredDevices" | "skateSessions" | "crewSessions" } | null) => void;
  onFilteredIdsChange?: (ids: { devices: string[], users: string[], sessions: string[], crews: string[], crewSessions: string[] }) => void;
}

export interface RelationalDataBankProps {
  activeEntity: { id: string; type: string } | null;
  onEntitySelected: (entity: { id: string; type: "users" | "crews" | "registeredDevices" | "skateSessions" | "crewSessions" } | null) => void;
  onFilteredIdsChange: (ids: FilteredIds) => void;
}

export default function RelationalDataBankWidget({ activeEntity, onEntitySelected, onFilteredIdsChange }: RelationalDataBankProps) {
  const [loading, setLoading] = useState(true);

  const [rawUsers, setRawUsers] = useState<any[]>([]);
  const [rawDevices, setRawDevices] = useState<any[]>([]);
  const [rawSessions, setRawSessions] = useState<any[]>([]);
  const [rawCrews, setRawCrews] = useState<any[]>([]);
  const [rawCrewSessions, setRawCrewSessions] = useState<any[]>([]);
  
  const [crewMemberships, setCrewMemberships] = useState<any[]>([]);
  const [crewSessionMembers, setCrewSessionMembers] = useState<any[]>([]);

  // Filter State
  const [filter, setFilter] = useState<{
    userId: string | null;
    deviceId: string | null;
    sessionId: string | null;
    crewId: string | null;
    crewSessionId: string | null;
  }>({
    userId: null,
    deviceId: null,
    sessionId: null,
    crewId: null,
    crewSessionId: null,
  });

  useEffect(() => {
    const fetchAll = async () => {
      setLoading(true);
      const [
        usersRes, devicesRes, sessionsRes, crewsRes, crewSessionsRes, membershipsRes, sessionMembersRes
      ] = await Promise.all([
        supabase.from("user_profiles").select("*"),
        supabase.from("registered_devices").select("*"),
        supabase.from("skate_sessions").select("*").limit(500),
        supabase.from("crews").select("*"),
        supabase.from("crew_sessions").select("*"),
        supabase.from("crew_memberships").select("*"),
        supabase.from("crew_members").select("*"),
      ]);

      setRawUsers(usersRes.data || []);
      setRawDevices(devicesRes.data || []);
      setRawSessions(sessionsRes.data || []);
      setRawCrews(crewsRes.data || []);
      setRawCrewSessions(crewSessionsRes.data || []);
      setCrewMemberships(membershipsRes.data || []);
      setCrewSessionMembers(sessionMembersRes.data || []);
      
      setLoading(false);
    };
    fetchAll();
  }, []);


  useEffect(() => {
    if (!activeEntity) {
      setFilter({ userId: null, deviceId: null, sessionId: null, crewId: null, crewSessionId: null });
      return;
    }
    const rawId = activeEntity.id.replace(/^(user_|skate_|crew_session_|crew_)/, '');
    if (activeEntity.type === 'users') setFilter(f => ({ ...f, userId: rawId }));
    else if (activeEntity.type === 'registeredDevices') setFilter(f => ({ ...f, deviceId: rawId }));
    else if (activeEntity.type === 'skateSessions') setFilter(f => ({ ...f, sessionId: rawId }));
    else if (activeEntity.type === 'crews') setFilter(f => ({ ...f, crewId: rawId }));
    else if (activeEntity.type === 'crewSessions') setFilter(f => ({ ...f, crewSessionId: rawId }));
  }, [activeEntity]);


  const handleReset = () => {
    setFilter({
      userId: null,
      deviceId: null,
      sessionId: null,
      crewId: null,
      crewSessionId: null,
    });
  };

  // Derived Filtered Data
  const fUsers = useMemo(() => {
    let u = rawUsers;
    if (filter.deviceId) {
      const dev = rawDevices.find(d => d.id === filter.deviceId);
      if (dev?.user_id) u = u.filter(user => user.user_id === dev.user_id);
    }
    if (filter.sessionId) {
      const sess = rawSessions.find(s => s.id === filter.sessionId);
      if (sess?.user_id) u = u.filter(user => user.user_id === sess.user_id);
    }
    if (filter.crewId) {
      const memberUserIds = crewMemberships.filter(m => m.crew_id === filter.crewId).map(m => m.user_id);
      u = u.filter(user => memberUserIds.includes(user.user_id));
    }
    if (filter.crewSessionId) {
      const participantIds = crewSessionMembers.filter(m => m.session_id === filter.crewSessionId).map(m => m.user_id);
      u = u.filter(user => participantIds.includes(user.user_id));
    }
    return u;
  }, [rawUsers, rawDevices, rawSessions, crewMemberships, crewSessionMembers, filter]);

  const fDevices = useMemo(() => {
    let d = rawDevices;
    if (filter.userId) d = d.filter(dev => dev.user_id === filter.userId);
    return d;
  }, [rawDevices, filter]);

  const fSessions = useMemo(() => {
    let s = rawSessions;
    if (filter.userId) s = s.filter(sess => sess.user_id === filter.userId);
    if (filter.deviceId) {
      const dev = rawDevices.find(d => d.id === filter.deviceId);
      if (dev?.user_id) s = s.filter(sess => sess.user_id === dev.user_id);
    }
    if (filter.crewSessionId) s = s.filter(sess => sess.crew_session_id === filter.crewSessionId);
    return s;
  }, [rawSessions, rawDevices, filter]);

  const fCrews = useMemo(() => {
    let c = rawCrews;
    if (filter.userId) {
      const userCrewIds = crewMemberships.filter(m => m.user_id === filter.userId).map(m => m.crew_id);
      c = c.filter(crew => userCrewIds.includes(crew.id));
    }
    if (filter.crewSessionId) {
      const cs = rawCrewSessions.find(s => s.id === filter.crewSessionId);
      if (cs?.crew_id) c = c.filter(crew => crew.id === cs.crew_id);
    }
    return c;
  }, [rawCrews, rawCrewSessions, crewMemberships, filter]);

  const fCrewSessions = useMemo(() => {
    let cs = rawCrewSessions;
    if (filter.userId) {
      const userSessionIds = crewSessionMembers.filter(m => m.user_id === filter.userId).map(m => m.session_id);
      cs = cs.filter(sess => userSessionIds.includes(sess.id));
    }
    if (filter.crewId) cs = cs.filter(sess => sess.crew_id === filter.crewId);
    return cs;
  }, [rawCrewSessions, crewSessionMembers, filter]);

  useEffect(() => {
    onFilteredIdsChange({
      users: new Set(fUsers.map(u => u.user_id)),
      devices: new Set(fDevices.map(d => d.id)),
      sessions: new Set(fSessions.map(s => s.id)),
      crews: new Set(fCrews.map(c => c.id)),
      crewSessions: new Set(fCrewSessions.map(cs => cs.id))
    });
  }, [fUsers, fDevices, fSessions, fCrews, fCrewSessions]); // omit onFilteredIdsChange to prevent loops if it lacks useCallback in parent

  const gridStyle = { height: 250, width: "100%", border: "1px solid rgba(51,65,85,0.5)", borderRadius: "8px", overflow: "hidden" };
  const getRowStyle = (params: any, idField: string, selectedId: string | null) => {
    if (selectedId && params.data[idField] === selectedId) {
      return { background: "rgba(56, 189, 248, 0.2)" };
    }
    return undefined;
  };

  if (loading) return <div className="text-cyan-400 p-8 animate-pulse text-center">Loading Relational DataBank...</div>;

  return (
    <div className="flex flex-col gap-4">
      <div className="flex justify-between items-center bg-[#0f172a] p-4 rounded-xl border border-slate-800 shrink-0">
        <div>
          <h2 className="text-xl font-bold text-white tracking-wide">Relational DataBank</h2>
          <p className="text-slate-400 text-xs mt-1">Cross-filtering active: click any row to isolate relationships.</p>
        </div>
        <button 
          onClick={handleReset}
          className="px-4 py-2 bg-slate-800 hover:bg-slate-700 text-white rounded-lg text-sm font-bold transition-colors border border-slate-700"
        >
          Reset Filters
        </button>
      </div>

      <div className="overflow-y-auto flex flex-col gap-4 pb-12 pr-2">
        <SectionHdr label={`Users (${fUsers.length})`} color="#38bdf8" />
        <div className="glass-panel" style={gridStyle}>
          <AgGridReact
            theme={myTheme}
            rowData={fUsers}
            columnDefs={[
              { field: "user_id", headerName: "ID", hide: true },
              { field: "display_name", headerName: "Name", flex: 1 },
              { field: "email", flex: 1 },
              { field: "role", width: 100 },
              { field: "created_at", headerName: "Joined", valueFormatter: p => new Date(p.value).toLocaleDateString() }
            ]}
            rowSelection={{ mode: "singleRow" }}
            onSelectionChanged={e => {
              const row = e.api.getSelectedRows()[0];
              if (row) {
                setFilter(f => ({ ...f, userId: row.user_id }));
                onEntitySelected({ id: row.user_id, type: 'users' });
              }
            }}
            getRowStyle={p => getRowStyle(p, "user_id", filter.userId)}
          />
        </div>

        <SectionHdr label={`Registered Devices (${fDevices.length})`} color="#10b981" />
        <div className="glass-panel" style={gridStyle}>
          <AgGridReact
            theme={myTheme}
            rowData={fDevices}
            columnDefs={[
              { field: "custom_name", headerName: "Name", flex: 1 },
              { field: "device_mac", headerName: "MAC", flex: 1 },
              { field: "product_type", headerName: "Product", width: 120 },
              { field: "firmware_ver", headerName: "FW", width: 80 },
              { field: "ble_version", headerName: "BLE", width: 80 }
            ]}
            rowSelection={{ mode: "singleRow" }}
            onSelectionChanged={e => {
              const row = e.api.getSelectedRows()[0];
              if (row) {
                setFilter(f => ({ ...f, deviceId: row.id }));
                onEntitySelected({ id: row.id, type: 'registeredDevices' });
              }
            }}
            getRowStyle={p => getRowStyle(p, "id", filter.deviceId)}
          />
        </div>

        <SectionHdr label={`Skate Sessions (${fSessions.length})`} color="#f59e0b" />
        <div className="glass-panel" style={gridStyle}>
          <AgGridReact
            theme={myTheme}
            rowData={fSessions}
            columnDefs={[
              { field: "session_date", headerName: "Date", valueFormatter: p => new Date(p.value).toLocaleDateString() },
              { field: "distance_miles", headerName: "Dist (mi)", flex: 1 },
              { field: "peak_speed_mph", headerName: "Top Spd", flex: 1 },
              { field: "avg_speed_mph", headerName: "Avg Spd", flex: 1 },
              { field: "duration_sec", headerName: "Duration", valueFormatter: p => p.value ? Math.floor(p.value/60)+"m" : "Live" }
            ]}
            rowSelection={{ mode: "singleRow" }}
            onSelectionChanged={e => {
              const row = e.api.getSelectedRows()[0];
              if (row) {
                setFilter(f => ({ ...f, sessionId: row.id }));
                onEntitySelected({ id: row.id, type: 'skateSessions' });
              }
            }}
            getRowStyle={p => getRowStyle(p, "id", filter.sessionId)}
          />
        </div>

        <SectionHdr label={`Crews (${fCrews.length})`} color="#a855f7" />
        <div className="glass-panel" style={gridStyle}>
          <AgGridReact
            theme={myTheme}
            rowData={fCrews}
            columnDefs={[
              { field: "name", flex: 1 },
              { field: "city", flex: 1 },
              { field: "state", flex: 1 },
              { field: "is_public", headerName: "Public", width: 100 },
              { field: "created_at", headerName: "Created", valueFormatter: p => new Date(p.value).toLocaleDateString() }
            ]}
            rowSelection={{ mode: "singleRow" }}
            onSelectionChanged={e => {
              const row = e.api.getSelectedRows()[0];
              if (row) {
                setFilter(f => ({ ...f, crewId: row.id }));
                onEntitySelected({ id: row.id, type: 'crews' });
              }
            }}
            getRowStyle={p => getRowStyle(p, "id", filter.crewId)}
          />
        </div>

        <SectionHdr label={`Crew Sessions (${fCrewSessions.length})`} color="#ec4899" />
        <div className="glass-panel" style={gridStyle}>
          <AgGridReact
            theme={myTheme}
            rowData={fCrewSessions}
            columnDefs={[
              { field: "name", flex: 1 },
              { field: "status", width: 120 },
              { field: "total_distance_miles", headerName: "Dist", flex: 1 },
              { field: "top_speed_mph", headerName: "Top Spd", flex: 1 },
              { field: "created_at", headerName: "Started", valueFormatter: p => new Date(p.value).toLocaleString() }
            ]}
            rowSelection={{ mode: "singleRow" }}
            onSelectionChanged={e => {
              const row = e.api.getSelectedRows()[0];
              if (row) {
                setFilter(f => ({ ...f, crewSessionId: row.id }));
                onEntitySelected({ id: row.id, type: 'crewSessions' });
              }
            }}
            getRowStyle={p => getRowStyle(p, "id", filter.crewSessionId)}
          />
        </div>

      </div>
    </div>
  );
}
