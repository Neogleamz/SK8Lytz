import React, { useEffect, useState, useMemo } from 'react';
import { supabase } from '../../services/supabase';
import type { Database } from '../../types/supabase';
import { AlertTriangle, Activity, Users, CheckCircle2, ShieldAlert, TerminalSquare } from 'lucide-react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';

type TabType = 'FATAL_CRASHES' | 'HANDLED_ERRORS';

interface CrashAggregate {
  error_signature: string;
  status: string;
  crash_count: number;
  affected_users: number;
  first_seen: string;
  last_seen: string;
}

export const LiveDebuggerWidget: React.FC = () => {
  const [activeTab, setActiveTab] = useState<TabType>('FATAL_CRASHES');

  // Autopsy Data
  const [autopsyData, setAutopsyData] = useState<CrashAggregate[]>([]);
  const [selectedSignature, setSelectedSignature] = useState<string | null>(null);
  
  // Selected Signature details
  const [signatureInstances, setSignatureInstances] = useState<any[]>([]);
  
  // Non-Fatal Telemetry
  const [telemetryFeed, setTelemetryFeed] = useState<any[]>([]);

  // Derived KPIs
  const totalCrashes = autopsyData.reduce((sum, item) => sum + item.crash_count, 0);
  const totalAffected = new Set(autopsyData.map(a => a.affected_users)).size; // Rough proxy
  const openCriticals = autopsyData.filter(a => a.status !== 'RESOLVED').length;
  const resolutionRate = autopsyData.length > 0 
    ? Math.round((autopsyData.filter(a => a.status === 'RESOLVED').length / autopsyData.length) * 100)
    : 100;

  // Mock Trend Data for the chart
  const trendData = useMemo(() => {
    return Array.from({ length: 7 }).map((_, i) => ({
      name: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'][(new Date().getDay() - 6 + i + 7) % 7],
      crashes: Math.floor(Math.random() * 20) + (i === 6 ? openCriticals : 0),
    }));
  }, [openCriticals]);

  const fetchData = async () => {
    // 1. Fetch Aggregates
    const viewName = activeTab === 'FATAL_CRASHES' ? 'view_crash_aggregates' : 'view_telemetry_errors_aggregates';
    const { data: aggs } = await supabase
      .from(viewName)
      .select('*')
      .order('status', { ascending: true }) // OPEN before RESOLVED
      .order('crash_count', { ascending: false }) // Most frequent first
      .order('last_seen', { ascending: false }); // Most recent first
    if (aggs) {
      setAutopsyData(aggs as CrashAggregate[]);
      if (aggs.length > 0 && !selectedSignature) {
        setSelectedSignature((aggs[0] as CrashAggregate).error_signature);
      } else if (aggs.length === 0) {
        setSelectedSignature(null);
      }
    }

    // 2. Fetch Terminal Feed
    const tableName = activeTab === 'FATAL_CRASHES' ? 'crash_telemetry' : 'telemetry_errors';
    const feedSelect = activeTab === 'FATAL_CRASHES' 
      ? 'created_at, severity, error_signature, app_version'
      : 'created_at, event_type, error_message, operation_type';

    const { data: feed } = await supabase
      .from(tableName)
      .select(feedSelect)
      .order('created_at', { ascending: false })
      .limit(50);
    if (feed) setTelemetryFeed(feed);
  };

  useEffect(() => {
    fetchData();

    // Subscribe to new crashes
    const sub = supabase
      .channel('live-debugger')
      .on('postgres_changes', { event: 'INSERT', schema: 'public', table: 'crash_telemetry' }, () => {
        if (activeTab === 'FATAL_CRASHES') fetchData();
      })
      .on('postgres_changes', { event: 'INSERT', schema: 'public', table: 'telemetry_errors' }, () => {
        if (activeTab === 'HANDLED_ERRORS') fetchData();
      })
      .subscribe();

    return () => {
      supabase.removeChannel(sub);
    };
  }, [activeTab]);

  // Fetch specific instances when an autopsy item is selected
  useEffect(() => {
    if (!selectedSignature) {
      setSignatureInstances([]);
      return;
    }

    const fetchInstances = async () => {
      const tableName = activeTab === 'FATAL_CRASHES' ? 'crash_telemetry' : 'telemetry_errors';
      const colName = activeTab === 'FATAL_CRASHES' ? 'error_signature' : 'error_message';

      const { data } = await supabase
        .from(tableName)
        .select('*')
        .eq(colName, selectedSignature)
        .order('created_at', { ascending: false })
        .limit(10);
      if (data) setSignatureInstances(data);
    };
    fetchInstances();
  }, [selectedSignature, activeTab]);

  const resolveSignature = async (signature: string) => {
    const rpcName = activeTab === 'FATAL_CRASHES' ? 'resolve_crash_signature' : 'resolve_telemetry_error';
    const { error } = await supabase.rpc(rpcName, {
      target_signature: signature,
    });
    if (!error) {
      fetchData();
      if (selectedSignature === signature) {
        setSelectedSignature(null);
      }
    }
  };

  const selectedAgg = autopsyData.find(a => a.error_signature === selectedSignature);
  const primaryInstance = signatureInstances[0];

  return (
    <div className="bg-[#0b0f19] border border-gray-800 rounded-xl shadow-2xl flex flex-col h-[90vh] overflow-hidden font-sans text-gray-300">
      
      {/* 1. Header & KPIs */}
      <div className="p-5 border-b border-gray-800 bg-[#0f1523] shrink-0">
        <div className="flex justify-between items-center mb-6">
          <div className="flex items-center gap-6">
            <h2 className="text-2xl font-bold text-white flex items-center gap-3">
              <ShieldAlert className="text-red-500 w-7 h-7" /> 
              Live Debugger Suite
            </h2>
            <div className="flex bg-gray-900 rounded-lg p-1 border border-gray-800">
              <button 
                onClick={() => { setActiveTab('FATAL_CRASHES'); setSelectedSignature(null); }}
                className={`px-4 py-1.5 rounded-md text-sm font-bold transition-all ${activeTab === 'FATAL_CRASHES' ? 'bg-red-500/20 text-red-400' : 'text-gray-500 hover:text-gray-300'}`}
              >
                Fatal Crashes
              </button>
              <button 
                onClick={() => { setActiveTab('HANDLED_ERRORS'); setSelectedSignature(null); }}
                className={`px-4 py-1.5 rounded-md text-sm font-bold transition-all ${activeTab === 'HANDLED_ERRORS' ? 'bg-yellow-500/20 text-yellow-400' : 'text-gray-500 hover:text-gray-300'}`}
              >
                Handled Errors
              </button>
            </div>
          </div>
          <div className="flex items-center gap-2 text-xs font-mono text-emerald-400 bg-emerald-400/10 px-3 py-1.5 rounded-full border border-emerald-400/20">
            <span className="relative flex h-2 w-2">
              <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span>
              <span className="relative inline-flex rounded-full h-2 w-2 bg-emerald-500"></span>
            </span>
            SYSTEM ONLINE
          </div>
        </div>

        <div className="flex gap-4">
          <div className="flex-1 grid grid-cols-4 gap-4">
            <div className="bg-[#151b2b] border border-gray-700/50 p-4 rounded-lg flex flex-col">
              <div className="text-gray-500 text-xs font-bold tracking-wider mb-2 flex items-center gap-2"><Activity size={14}/> TOTAL ISSUES</div>
              <div className="text-3xl font-light text-white">{totalCrashes}</div>
            </div>
            <div className="bg-[#151b2b] border border-gray-700/50 p-4 rounded-lg flex flex-col">
              <div className="text-gray-500 text-xs font-bold tracking-wider mb-2 flex items-center gap-2"><Users size={14}/> AFFECTED USERS</div>
              <div className="text-3xl font-light text-white">{totalAffected}</div>
            </div>
            <div className={`bg-${activeTab === 'FATAL_CRASHES' ? 'red' : 'yellow'}-500/5 border border-${activeTab === 'FATAL_CRASHES' ? 'red' : 'yellow'}-500/20 p-4 rounded-lg flex flex-col`}>
              <div className={`text-${activeTab === 'FATAL_CRASHES' ? 'red' : 'yellow'}-400/70 text-xs font-bold tracking-wider mb-2 flex items-center gap-2`}><AlertTriangle size={14}/> OPEN CRITICALS</div>
              <div className={`text-3xl font-light text-${activeTab === 'FATAL_CRASHES' ? 'red' : 'yellow'}-400`}>{openCriticals}</div>
            </div>
            <div className="bg-emerald-500/5 border border-emerald-500/20 p-4 rounded-lg flex flex-col">
              <div className="text-emerald-400/70 text-xs font-bold tracking-wider mb-2 flex items-center gap-2"><CheckCircle2 size={14}/> RESOLUTION RATE</div>
              <div className="text-3xl font-light text-emerald-400">{resolutionRate}%</div>
            </div>
          </div>
          
          <div className="w-64 bg-[#151b2b] border border-gray-700/50 p-4 rounded-lg hidden lg:block">
            <div className="text-gray-500 text-xs font-bold tracking-wider mb-2">7-DAY TREND</div>
            <div className="h-12">
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={trendData}>
                  <Tooltip contentStyle={{ backgroundColor: '#1f2937', border: 'none', borderRadius: '8px' }} cursor={{fill: '#374151', opacity: 0.4}}/>
                  <Bar dataKey="crashes" fill={activeTab === 'FATAL_CRASHES' ? '#f87171' : '#fbbf24'} radius={[2, 2, 0, 0]} />
                </BarChart>
              </ResponsiveContainer>
            </div>
          </div>
        </div>
      </div>

      {/* 2. Main Content Split */}
      <div className="flex flex-1 overflow-hidden">
        
        {/* Left Rail: Autopsy List */}
        <div className="w-1/3 border-r border-gray-800 bg-[#0b0f19] flex flex-col overflow-hidden">
          <div className="p-3 border-b border-gray-800 bg-[#0f1523] text-xs font-bold text-gray-400 tracking-wider flex justify-between">
            <span>{activeTab === 'FATAL_CRASHES' ? 'CRITICAL FINDINGS' : 'HANDLED ERRORS'}</span>
            <span>{autopsyData.length} ISSUES</span>
          </div>
          <div className="flex-1 overflow-y-auto custom-scrollbar p-2 space-y-2">
            {autopsyData.length === 0 ? (
              <div className="text-center p-8 text-gray-600 text-sm">No findings recorded.</div>
            ) : (
              autopsyData.map((item) => (
                <div 
                  key={item.error_signature}
                  onClick={() => setSelectedSignature(item.error_signature)}
                  className={`p-3 rounded-lg border cursor-pointer transition-all ${
                    selectedSignature === item.error_signature 
                      ? 'bg-blue-500/10 border-blue-500/50 shadow-[0_0_15px_rgba(59,130,246,0.1)]' 
                      : 'bg-[#151b2b] border-gray-800 hover:border-gray-600'
                  }`}
                >
                  <div className="flex justify-between items-start mb-2">
                    <span className={`text-[10px] px-2 py-0.5 rounded font-bold tracking-wider ${
                      item.status === 'RESOLVED' ? 'bg-emerald-500/10 text-emerald-400' : 'bg-red-500/10 text-red-400'
                    }`}>
                      {item.status}
                    </span>
                    <span className="text-xs text-gray-500 font-mono">
                      {new Date(item.last_seen).toLocaleDateString()}
                    </span>
                  </div>
                  <div className="text-sm font-semibold text-gray-200 mb-2 line-clamp-2 leading-snug">
                    {item.error_signature}
                  </div>
                  <div className="flex items-center gap-4 text-xs text-gray-500 font-mono">
                    <span className="flex items-center gap-1"><Activity size={12}/> {item.crash_count}</span>
                    <span className="flex items-center gap-1"><Users size={12}/> {item.affected_users}</span>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>

        {/* Right Panel: Deep Dive */}
        <div className="flex-1 bg-[#0b0f19] flex flex-col overflow-hidden relative">
          {!selectedSignature || !selectedAgg ? (
            <div className="flex-1 flex flex-col items-center justify-center text-gray-600">
              <Activity className="w-16 h-16 mb-4 opacity-20" />
              <p>Select a finding to view details</p>
            </div>
          ) : (
            <>
              {/* Deep Dive Header */}
              <div className="p-5 border-b border-gray-800 bg-[#0f1523] shrink-0">
                <div className="flex justify-between items-start mb-4">
                  <h3 className={`text-lg font-bold ${activeTab === 'FATAL_CRASHES' ? 'text-red-400' : 'text-yellow-400'} break-words pr-4`}>{selectedAgg.error_signature}</h3>
                  <div className="shrink-0 flex gap-2">
                    {selectedAgg.status !== 'RESOLVED' && (
                      <button 
                        onClick={() => resolveSignature(selectedAgg.error_signature)}
                        className="px-4 py-2 bg-emerald-500/10 text-emerald-400 hover:bg-emerald-500/20 border border-emerald-500/30 rounded-lg text-sm font-bold transition-colors"
                      >
                        Resolve Issue
                      </button>
                    )}
                  </div>
                </div>
                <div className="flex gap-6 text-sm text-gray-400 font-mono bg-gray-900/50 p-3 rounded-lg border border-gray-800">
                  <div><span className="text-gray-500">Events:</span> {selectedAgg.crash_count}</div>
                  <div><span className="text-gray-500">Users:</span> {selectedAgg.affected_users}</div>
                  <div><span className="text-gray-500">First Seen:</span> {new Date(selectedAgg.first_seen).toLocaleString()}</div>
                  <div><span className="text-gray-500">Last Seen:</span> {new Date(selectedAgg.last_seen).toLocaleString()}</div>
                </div>
              </div>

              {/* Deep Dive Content (Scrollable) */}
              <div className="flex-1 overflow-y-auto custom-scrollbar p-5 space-y-6">
                
                {/* Stack Trace */}
                <div>
                  <h4 className="text-xs font-bold text-gray-500 tracking-wider mb-2 uppercase flex items-center gap-2">
                    <TerminalSquare size={14}/> Stack Trace
                  </h4>
                  <div className="bg-[#151b2b] p-4 rounded-lg border border-gray-800 font-mono text-sm text-red-300/90 overflow-x-auto whitespace-pre-wrap leading-relaxed">
                    {primaryInstance?.stack_trace || 'No stack trace available for this signature.'}
                  </div>
                </div>

                {/* Context Matrix */}
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <h4 className="text-xs font-bold text-gray-500 tracking-wider mb-2 uppercase">Environment (Latest)</h4>
                    <div className="bg-[#151b2b] p-4 rounded-lg border border-gray-800 font-mono text-xs text-blue-300/80 overflow-x-auto whitespace-pre-wrap h-40">
                      {JSON.stringify(activeTab === 'FATAL_CRASHES' ? primaryInstance?.environment_state : primaryInstance?.raw_context || {}, null, 2)}
                    </div>
                  </div>
                  <div>
                    <h4 className="text-xs font-bold text-gray-500 tracking-wider mb-2 uppercase">{activeTab === 'FATAL_CRASHES' ? 'Flight Breadcrumbs' : 'Session Info'}</h4>
                    <div className="bg-[#151b2b] rounded-lg border border-gray-800 overflow-y-auto custom-scrollbar h-40">
                      {activeTab === 'FATAL_CRASHES' ? (
                        Array.isArray(primaryInstance?.breadcrumbs) && primaryInstance.breadcrumbs.length > 0 ? (
                          <div className="divide-y divide-gray-800/50">
                            {[...primaryInstance.breadcrumbs].reverse().map((crumb: any, idx: number) => (
                              <div key={idx} className="p-3 hover:bg-gray-800/50 transition-colors flex gap-3 text-xs">
                                <div className="w-16 shrink-0 text-gray-600 font-mono">{new Date(crumb.timestamp || Date.now()).toLocaleTimeString([], { hour12: false })}</div>
                                <div className="w-16 shrink-0">
                                  <span className={`text-[9px] px-1.5 py-0.5 rounded font-bold tracking-wider ${
                                    crumb.category === 'BLE' ? 'bg-blue-500/10 text-blue-400' :
                                    crumb.category === 'NETWORK' ? 'bg-yellow-500/10 text-yellow-400' :
                                    'bg-gray-700/50 text-gray-400'
                                  }`}>{crumb.category}</span>
                                </div>
                                <div className="flex-1 text-gray-300 truncate">{crumb.message}</div>
                              </div>
                            ))}
                          </div>
                        ) : (
                          <div className="p-4 text-gray-600 text-xs flex items-center justify-center h-full">No breadcrumbs recorded.</div>
                        )
                      ) : (
                        <div className="p-4 text-xs text-gray-300 space-y-3 font-mono">
                          <div className="flex flex-col"><span className="text-gray-500 font-bold uppercase text-[10px] tracking-wider">Session ID</span> {primaryInstance?.session_id || 'N/A'}</div>
                          <div className="flex flex-col"><span className="text-gray-500 font-bold uppercase text-[10px] tracking-wider">Event Type</span> {primaryInstance?.event_type || 'N/A'}</div>
                          <div className="flex flex-col"><span className="text-gray-500 font-bold uppercase text-[10px] tracking-wider">Operation Type</span> {primaryInstance?.operation_type || 'N/A'}</div>
                          <div className="flex flex-col"><span className="text-gray-500 font-bold uppercase text-[10px] tracking-wider">Payload Size</span> {primaryInstance?.payload_size ? `${primaryInstance.payload_size} bytes` : 'N/A'}</div>
                        </div>
                      )}
                    </div>
                  </div>
                </div>
              </div>
            </>
          )}
        </div>
      </div>

      {/* 3. Terminal Feed (Bottom docked) */}
      <div className="h-48 border-t border-gray-800 bg-[#080b12] shrink-0 flex flex-col">
        <div className="p-2 px-4 border-b border-gray-800 bg-[#0a0f18] text-xs font-bold text-gray-500 tracking-wider flex items-center justify-between">
          <div className="flex items-center gap-2"><TerminalSquare size={12}/> {activeTab === 'FATAL_CRASHES' ? 'FATAL CRASH STREAM' : 'HANDLED ERROR STREAM'}</div>
          <span className="text-[10px] text-emerald-500">Live</span>
        </div>
        <div className="flex-1 overflow-y-auto custom-scrollbar p-3 font-mono text-xs space-y-1">
          {telemetryFeed.length === 0 ? (
            <div className="text-gray-600 text-center mt-4">Awaiting telemetry...</div>
          ) : (
            telemetryFeed.map((log, i) => {
              if (activeTab === 'FATAL_CRASHES') {
                return (
                  <div key={i} className="flex gap-4 hover:bg-white/[0.02] px-2 py-1 rounded transition-colors">
                    <span className="text-gray-600 shrink-0 w-20">{new Date(log.created_at || '').toLocaleTimeString()}</span>
                    <span className={`shrink-0 w-12 font-bold ${log.severity === 'WARN' ? 'text-yellow-500' : 'text-blue-400'}`}>
                      [{log.severity}]
                    </span>
                    <span className="text-gray-400 truncate flex-1">{log.error_signature}</span>
                    <span className="text-gray-600 shrink-0 w-12 text-right">v{log.app_version}</span>
                  </div>
                );
              } else {
                return (
                  <div key={i} className="flex gap-4 hover:bg-white/[0.02] px-2 py-1 rounded transition-colors">
                    <span className="text-gray-600 shrink-0 w-20">{new Date(log.created_at || '').toLocaleTimeString()}</span>
                    <span className="shrink-0 w-32 font-bold text-yellow-500 truncate">
                      [{log.event_type}]
                    </span>
                    <span className="text-gray-400 truncate flex-1">{log.error_message}</span>
                    <span className="text-gray-600 shrink-0 w-32 text-right truncate">{log.operation_type}</span>
                  </div>
                );
              }
            })
          )}
        </div>
      </div>
    </div>
  );
};

