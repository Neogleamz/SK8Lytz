import React, { useEffect, useState } from 'react';
import { supabase } from '../../services/supabaseClient';
import type { Database } from '../../types/supabase';

type CrashReport = Database['public']['Tables']['crash_telemetry']['Row'];

export const LiveDebuggerWidget: React.FC = () => {
  const [reports, setReports] = useState<CrashReport[]>([]);
  const [selectedReport, setSelectedReport] = useState<CrashReport | null>(null);

  useEffect(() => {
    // Initial fetch of recent crashes
    const fetchRecent = async () => {
      const { data } = await supabase
        .from('crash_telemetry')
        .select('*')
        .order('created_at', { ascending: false })
        .limit(10);
      if (data) setReports(data);
    };

    fetchRecent();

    // Subscribe to live crash events
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

  const resolveReport = async (id: string) => {
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

  return (
    <div className="bg-gray-900 border border-red-500/30 rounded-xl p-6 shadow-2xl flex flex-col h-[600px] overflow-hidden">
      <div className="flex justify-between items-center mb-4 shrink-0">
        <h2 className="text-xl font-bold text-white flex items-center gap-2">
          <span className="text-red-500">🚨</span> Live Flight Recorder
        </h2>
        <div className="flex items-center gap-2 text-sm text-gray-400">
          <span className="relative flex h-3 w-3">
            <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-red-400 opacity-75"></span>
            <span className="relative inline-flex rounded-full h-3 w-3 bg-red-500"></span>
          </span>
          Streaming Live
        </div>
      </div>

      <div className="flex flex-1 gap-4 overflow-hidden">
        {/* Left pane: Feed */}
        <div className="w-1/3 border-r border-gray-800 pr-4 overflow-y-auto custom-scrollbar">
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
                      : 'bg-gray-800 border-gray-700 hover:border-gray-500'
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

        {/* Right pane: Details */}
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
                    onClick={() => resolveReport(selectedReport.id)}
                    className="px-3 py-1 bg-green-500/20 text-green-400 hover:bg-green-500/30 rounded text-sm transition-colors"
                  >
                    Mark Resolved
                  </button>
                )}
              </div>

              <div className="grid grid-cols-2 gap-4 mb-6 shrink-0">
                <div className="bg-gray-800 p-3 rounded-lg border border-gray-700">
                  <div className="text-xs text-gray-500 uppercase tracking-wider mb-2">Environment State</div>
                  <pre className="text-xs text-green-400 overflow-x-auto">
                    {JSON.stringify(selectedReport.environment_state, null, 2)}
                  </pre>
                </div>
                <div className="bg-gray-800 p-3 rounded-lg border border-gray-700">
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
                <div className="text-xs text-gray-500 uppercase tracking-wider mb-2">Flight Breadcrumbs (T-minus 50)</div>
                <div className="bg-gray-800 rounded-lg border border-gray-700 overflow-hidden flex flex-col">
                  {Array.isArray(selectedReport.breadcrumbs) && selectedReport.breadcrumbs.length > 0 ? (
                    <div className="divide-y divide-gray-700">
                      {[...selectedReport.breadcrumbs].reverse().map((crumb: any, idx: number) => (
                        <div key={idx} className="p-3 hover:bg-gray-700/50 transition-colors flex gap-4 items-start">
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
                              <div className="mt-1 text-xs text-gray-400 font-mono truncate bg-gray-900/50 p-1 rounded">
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
    </div>
  );
};
