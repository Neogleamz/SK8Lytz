import React, { useEffect, useState, useMemo } from 'react';
import { supabase } from '../../services/supabase';
import { AgGridReact } from 'ag-grid-react';
import { themeQuartz, colorSchemeDark } from 'ag-grid-community';
import type { Database } from '../../types/supabase';

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

type CrashReport = Database['public']['Tables']['crash_telemetry']['Row'];

interface CrashAggregate {
  error_signature: string;
  status: string;
  crash_count: number;
  affected_users: number;
  first_seen: string;
  last_seen: string;
}

export const LiveDebuggerWidget: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'stream' | 'autopsy' | 'telemetry'>('stream');
  
  // Tab 1: Live Stream
  const [reports, setReports] = useState<CrashReport[]>([]);
  const [selectedReport, setSelectedReport] = useState<CrashReport | null>(null);

  // Tab 2: Crash Autopsy
  const [autopsyData, setAutopsyData] = useState<CrashAggregate[]>([]);
  const [loadingAutopsy, setLoadingAutopsy] = useState(false);

  // Tab 3: Non-Fatal Telemetry
  const [errorTelemetry, setErrorTelemetry] = useState<CrashReport[]>([]);
  const [loadingTelemetry, setLoadingTelemetry] = useState(false);

  // Initial Fetch for Live Stream
  useEffect(() => {
    const fetchRecent = async () => {
      const { data } = await supabase
        .from('crash_telemetry')
        .select('*')
        .order('created_at', { ascending: false })
        .limit(20);
      if (data) setReports(data);
    };

    fetchRecent();

    const subscription = supabase
      .channel('crash_telemetry_changes')
      .on(
        'postgres_changes',
        { event: 'INSERT', schema: 'public', table: 'crash_telemetry' },
        (payload) => {
          setReports((prev) => [payload.new as CrashReport, ...prev].slice(0, 50));
        }
      )
      .subscribe();

    return () => {
      supabase.removeChannel(subscription);
    };
  }, []);

  // Fetch Autopsy Data
  const fetchAutopsyData = async () => {
    setLoadingAutopsy(true);
    const { data } = await supabase.from('view_crash_aggregates').select('*');
    if (data) setAutopsyData(data as CrashAggregate[]);
    setLoadingAutopsy(false);
  };

  // Fetch Telemetry Data
  const fetchTelemetryData = async () => {
    setLoadingTelemetry(true);
    const { data } = await supabase
      .from('crash_telemetry')
      .select('*')
      .neq('severity', 'FATAL')
      .order('created_at', { ascending: false })
      .limit(200);
    if (data) setErrorTelemetry(data as CrashReport[]);
    setLoadingTelemetry(false);
  };

  useEffect(() => {
    if (activeTab === 'autopsy') fetchAutopsyData();
    if (activeTab === 'telemetry') fetchTelemetryData();
  }, [activeTab]);

  const resolveSingleReport = async (id: string) => {
    const { error } = await supabase
      .from('crash_telemetry')
      .update({ status: 'RESOLVED', resolved_at: new Date().toISOString() })
      .eq('id', id);

    if (!error) {
      setReports((prev) => prev.map((r) => r.id === id ? { ...r, status: 'RESOLVED' } : r));
      if (selectedReport?.id === id) {
        setSelectedReport({ ...selectedReport, status: 'RESOLVED' });
      }
    }
  };

  const resolveSignature = async (signature: string) => {
    const { error } = await supabase.rpc('resolve_crash_signature', {
      target_signature: signature,
    });
    if (!error) {
      fetchAutopsyData(); // Refresh autopsy list
      // Also optimistically update the live stream if there are matching signatures
      setReports((prev) => prev.map((r) => r.error_signature === signature ? { ...r, status: 'RESOLVED' } : r));
      if (selectedReport?.error_signature === signature) {
        setSelectedReport({ ...selectedReport, status: 'RESOLVED' });
      }
    }
  };

  // AG Grid Column Defs
  const autopsyColDefs: any[] = useMemo(() => [
    { field: "error_signature", headerName: "Signature", flex: 2, filter: true },
    { field: "status", headerName: "Status", width: 120, cellRenderer: (p: any) => (
      <span className={p.value === 'RESOLVED' ? 'text-green-400' : 'text-red-400 font-bold'}>{p.value}</span>
    )},
    { field: "crash_count", headerName: "Crashes", width: 100, sortable: true },
    { field: "affected_users", headerName: "Users", width: 100, sortable: true },
    { field: "last_seen", headerName: "Last Seen", width: 180, valueFormatter: (p: any) => new Date(p.value).toLocaleString() },
    { 
      headerName: "Actions", 
      width: 120, 
      cellRenderer: (p: any) => {
        if (p.data.status === 'RESOLVED') return null;
        return (
          <button
            onClick={() => resolveSignature(p.data.error_signature)}
            className="mt-1 px-3 py-1 bg-green-500/20 text-green-400 hover:bg-green-500/30 rounded text-xs transition-colors border border-green-500/50"
          >
            Resolve All
          </button>
        );
      } 
    }
  ], []);

  const telemetryColDefs: any[] = useMemo(() => [
    { field: "created_at", headerName: "Time", width: 180, valueFormatter: (p: any) => new Date(p.value).toLocaleString() },
    { field: "severity", headerName: "Severity", width: 120, filter: true },
    { field: "status", headerName: "Status", width: 120, filter: true },
    { field: "error_signature", headerName: "Signature", flex: 2, tooltipField: "error_signature" },
    { field: "app_version", headerName: "Version", width: 120 },
    { field: "user_id", headerName: "User ID", width: 150 }
  ], []);

  const gridStyle = { height: "100%", width: "100%" };

  return (
    <div className="bg-gray-900 border border-red-500/30 rounded-xl shadow-2xl flex flex-col h-[700px] overflow-hidden">
      {/* Header and Tabs */}
      <div className="p-4 border-b border-gray-800 bg-gray-950 shrink-0">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-bold text-white flex items-center gap-2">
            <span className="text-red-500">🚨</span> Live Debugger Suite
          </h2>
          <div className="flex items-center gap-2 text-sm text-gray-400">
            <span className="relative flex h-3 w-3">
              <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-red-400 opacity-75"></span>
              <span className="relative inline-flex rounded-full h-3 w-3 bg-red-500"></span>
            </span>
            Telemetry Active
          </div>
        </div>
        
        <div className="flex space-x-2">
          <button 
            onClick={() => setActiveTab('stream')}
            className={`px-4 py-2 rounded-t-lg text-sm font-medium transition-colors ${activeTab === 'stream' ? 'bg-gray-800 text-red-400 border-t border-l border-r border-gray-700' : 'text-gray-500 hover:text-gray-300'}`}
          >
            Live Stream
          </button>
          <button 
            onClick={() => setActiveTab('autopsy')}
            className={`px-4 py-2 rounded-t-lg text-sm font-medium transition-colors ${activeTab === 'autopsy' ? 'bg-gray-800 text-cyan-400 border-t border-l border-r border-gray-700' : 'text-gray-500 hover:text-gray-300'}`}
          >
            Crash Autopsy
          </button>
          <button 
            onClick={() => setActiveTab('telemetry')}
            className={`px-4 py-2 rounded-t-lg text-sm font-medium transition-colors ${activeTab === 'telemetry' ? 'bg-gray-800 text-yellow-400 border-t border-l border-r border-gray-700' : 'text-gray-500 hover:text-gray-300'}`}
          >
            Non-Fatal Telemetry
          </button>
        </div>
      </div>

      {/* Content Area */}
      <div className="flex-1 overflow-hidden bg-gray-800 p-4">
        {/* Tab 1: Live Stream */}
        {activeTab === 'stream' && (
          <div className="flex h-full gap-4 overflow-hidden">
            <div className="w-1/3 border-r border-gray-700 pr-4 overflow-y-auto custom-scrollbar">
              {reports.length === 0 ? (
                <div className="text-gray-500 text-center mt-10">No recent crashes</div>
              ) : (
                <div className="flex flex-col gap-2">
                  {reports.map((report) => (
                    <div 
                      key={report.id}
                      onClick={() => setSelectedReport(report)}
                      className={`p-3 rounded-lg border cursor-pointer transition-colors ${
                        selectedReport?.id === report.id 
                          ? 'bg-red-500/20 border-red-500' 
                          : 'bg-gray-900 border-gray-700 hover:border-gray-500'
                      }`}
                    >
                      <div className="flex justify-between items-start mb-2">
                        <span className={`text-xs px-2 py-0.5 rounded font-bold ${
                          report.status === 'RESOLVED' ? 'bg-green-500/20 text-green-400' : 'bg-red-500/20 text-red-400'
                        }`}>
                          {report.status}
                        </span>
                        <span className="text-xs text-gray-500">
                          {new Date(report.created_at || '').toLocaleTimeString()}
                        </span>
                      </div>
                      <div className="text-sm font-medium text-white truncate" title={report.error_signature}>
                        {report.error_signature}
                      </div>
                      <div className="text-xs text-gray-400 mt-1 truncate">
                        User: {report.user_id || 'Anonymous'}
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>

            <div className="w-2/3 pl-2 overflow-y-auto custom-scrollbar">
              {selectedReport ? (
                <div className="flex flex-col h-full">
                  <div className="flex justify-between items-start mb-4">
                    <div>
                      <h3 className="text-lg font-bold text-red-400 mb-1">{selectedReport.error_signature}</h3>
                      <div className="text-xs text-gray-500 font-mono">ID: {selectedReport.id}</div>
                    </div>
                    {selectedReport.status !== 'RESOLVED' && (
                      <button 
                        onClick={() => resolveSingleReport(selectedReport.id)}
                        className="px-3 py-1 bg-green-500/20 text-green-400 hover:bg-green-500/30 rounded text-sm transition-colors border border-green-500/50"
                      >
                        Mark Resolved
                      </button>
                    )}
                  </div>

                  <div className="grid grid-cols-2 gap-4 mb-6 shrink-0">
                    <div className="bg-gray-900 p-3 rounded-lg border border-gray-700">
                      <div className="text-xs text-gray-500 uppercase tracking-wider mb-2">Environment State</div>
                      <pre className="text-xs text-green-400 overflow-x-auto">
                        {JSON.stringify(selectedReport.environment_state, null, 2)}
                      </pre>
                    </div>
                    <div className="bg-gray-900 p-3 rounded-lg border border-gray-700">
                      <div className="text-xs text-gray-500 uppercase tracking-wider mb-2">App Info</div>
                      <div className="text-sm text-gray-300">
                        <div>Version: {selectedReport.app_version || 'N/A'}</div>
                        <div>Severity: <span className="text-red-400 font-bold">{selectedReport.severity}</span></div>
                      </div>
                    </div>
                  </div>

                  <div className="mb-6 shrink-0">
                    <div className="text-xs text-gray-500 uppercase tracking-wider mb-2">Stack Trace</div>
                    <div className="bg-gray-950 p-4 rounded-lg border border-red-900/50 text-red-300 text-xs font-mono overflow-x-auto">
                      {selectedReport.stack_trace || 'No stack trace available.'}
                    </div>
                  </div>

                  <div className="flex-1">
                    <div className="text-xs text-gray-500 uppercase tracking-wider mb-2">Flight Breadcrumbs</div>
                    <div className="bg-gray-900 rounded-lg border border-gray-700 overflow-hidden flex flex-col">
                      {Array.isArray(selectedReport.breadcrumbs) && selectedReport.breadcrumbs.length > 0 ? (
                        <div className="divide-y divide-gray-800">
                          {[...selectedReport.breadcrumbs].reverse().map((crumb: any, idx: number) => (
                            <div key={idx} className="p-3 hover:bg-gray-800 transition-colors flex gap-4 items-start">
                              <div className="w-20 shrink-0 text-xs text-gray-500 font-mono pt-0.5">
                                {new Date(crumb.timestamp).toLocaleTimeString([], { hour12: false, fractionalSecondDigits: 3 })}
                              </div>
                              <div className="w-24 shrink-0">
                                <span className={`text-[10px] px-2 py-0.5 rounded font-bold tracking-wider ${
                                  crumb.category === 'BLE' ? 'bg-blue-500/20 text-blue-400' :
                                  crumb.category === 'ACTION' ? 'bg-purple-500/20 text-purple-400' :
                                  crumb.category === 'NETWORK' ? 'bg-yellow-500/20 text-yellow-400' :
                                  crumb.category === 'ERROR' ? 'bg-red-500/20 text-red-400' :
                                  'bg-gray-600/50 text-gray-300'
                                }`}>
                                  {crumb.category}
                                </span>
                              </div>
                              <div className="flex-1 min-w-0">
                                <div className="text-sm text-gray-200 font-medium truncate">{crumb.message}</div>
                                {crumb.data && Object.keys(crumb.data).length > 0 && (
                                  <div className="mt-1 text-xs text-gray-400 font-mono truncate bg-gray-950 p-1 rounded">
                                    {JSON.stringify(crumb.data)}
                                  </div>
                                )}
                              </div>
                            </div>
                          ))}
                        </div>
                      ) : (
                        <div className="p-6 text-center text-gray-500 text-sm">No breadcrumbs recorded.</div>
                      )}
                    </div>
                  </div>
                </div>
              ) : (
                <div className="h-full flex items-center justify-center text-gray-500">
                  Select a crash report to view autopsy details.
                </div>
              )}
            </div>
          </div>
        )}

        {/* Tab 2: Crash Autopsy */}
        {activeTab === 'autopsy' && (
          <div className="h-full w-full rounded-lg overflow-hidden border border-gray-700">
            {loadingAutopsy ? (
              <div className="text-cyan-400 p-8 animate-pulse text-center">Crunching Crash Aggregates...</div>
            ) : (
              <div style={gridStyle}>
                <AgGridReact
                  theme={myTheme}
                  rowData={autopsyData}
                  columnDefs={autopsyColDefs}
                  rowSelection={{ mode: "singleRow" }}
                />
              </div>
            )}
          </div>
        )}

        {/* Tab 3: Non-Fatal Telemetry */}
        {activeTab === 'telemetry' && (
          <div className="h-full w-full rounded-lg overflow-hidden border border-gray-700">
            {loadingTelemetry ? (
              <div className="text-yellow-400 p-8 animate-pulse text-center">Loading Telemetry Feed...</div>
            ) : (
              <div style={gridStyle}>
                <AgGridReact
                  theme={myTheme}
                  rowData={errorTelemetry}
                  columnDefs={telemetryColDefs}
                  rowSelection={{ mode: "singleRow" }}
                />
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};
