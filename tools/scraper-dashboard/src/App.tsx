import { useState, useEffect, useRef } from 'react';
import USAMap from './USMap';
import './App.css';

const API_BASE = 'http://localhost:5999';

// --- Countdown Timer Component ---
const PulseTimer = ({ nextRunAt }: { nextRunAt: string | null }) => {
  const [timeLeft, setTimeLeft] = useState<number | null>(null);

  useEffect(() => {
    if (!nextRunAt) {
      setTimeLeft(null);
      return;
    }

    const interval = setInterval(() => {
      const now = Date.now();
      const target = new Date(nextRunAt).getTime();
      const diff = Math.max(0, Math.floor((target - now) / 1000));
      setTimeLeft(diff);
    }, 1000);

    return () => clearInterval(interval);
  }, [nextRunAt]);

  if (timeLeft === null) return <div style={{color: 'rgba(255,255,255,0.3)', fontSize: '0.8rem'}}>IDLE</div>;
  if (timeLeft === 0) return <div style={{color: 'var(--success)', fontWeight: 800, fontSize: '0.8rem'}}>RUNNING...</div>;

  return (
    <div style={{ textAlign: 'center' }}>
      <div style={{ fontSize: '1.2rem', fontWeight: 800, color: '#fff' }}>{timeLeft}s</div>
      <div style={{ fontSize: '0.6rem', color: 'rgba(255,255,255,0.5)', textTransform: 'uppercase' }}>Next Run</div>
    </div>
  );
};

const US_STATES = [
  'AL', 'AK', 'AZ', 'AR', 'CA', 'CO', 'CT', 'DE', 'FL', 'GA',
  'HI', 'ID', 'IL', 'IN', 'IA', 'KS', 'KY', 'LA', 'ME', 'MD',
  'MA', 'MI', 'MN', 'MS', 'MO', 'MT', 'NE', 'NV', 'NH', 'NJ',
  'NM', 'NY', 'NC', 'ND', 'OH', 'OK', 'OR', 'PA', 'RI', 'SC',
  'SD', 'TN', 'TX', 'UT', 'VT', 'VA', 'WA', 'WV', 'WI', 'WY', 'DC'
];

function App() {
  const [activeTab, setActiveTab] = useState<'phase1' | 'phase2' | 'phase3' | 'phase4' | 'phase5' | 'phase6'>('phase1');
  const [seedProvider, setSeedProvider] = useState<'osm'|'google'>('osm');


  // --- Sys Dashboard States ---
  const [status, setStatus] = useState<any>(null);
  const [targetFacilities, setTargetFacilities] = useState<string[]>([]);
  const [stateOverride, setStateOverride] = useState<string[]>([]);
  const [sleepInterval, setSleepInterval] = useState<number>(5000);
  const [isHeadless, setIsHeadless] = useState<boolean>(true);
  const [identityRotation, setIdentityRotation] = useState<boolean>(true);
  const [randomizeViewport, setRandomizeViewport] = useState<boolean>(true);
  
  // --- Evasion Tactics States ---
  const [cooldownBase, setCooldownBase] = useState<number>(300000);
  const [cooldownJitter, setCooldownJitter] = useState<number>(20);
  const [maxStrikes, setMaxStrikes] = useState<number>(3);
  const [autoResume, setAutoResume] = useState<boolean>(true);

  // --- Logs & Queue States ---
  const [logs, setLogs] = useState<{type: string, message: string, source?: string}[]>([]);
  const logsRef = useRef<HTMLDivElement>(null);
  const activeTabRef = useRef<typeof activeTab>('phase1');

  // --- Harvest Manager States ---
  const [harvestData, setHarvestData] = useState<{seededStates: string[], stateCounts: Record<string, number>, allStates: string[]}>({ seededStates: [], stateCounts: {}, allStates: [] });
  const [coverageStats, setCoverageStats] = useState<any[]>([]);
  const [databankCoverage, setDatabankCoverage] = useState<any[]>([]);
  const [historyLogs, setHistoryLogs] = useState<string[]>([]);

  // --- Graveyard Grid States ---
  const [spots, setSpots] = useState<any[]>([]);
  const [totalSpots, setTotalSpots] = useState(0);
  const [page, setPage] = useState(0);
  const [gridFilter, setGridFilter] = useState('ALL');
  const [sortCol, setSortCol] = useState('last_attempted_at');
  const [sortDir, setSortDir] = useState<'asc'|'desc'>('desc');
  const [searchQuery, setSearchQuery] = useState('');
  const [editingId, setEditingId] = useState<string | null>(null);
  const [editForm, setEditForm] = useState<any>({});
  const [isStarting, setIsStarting] = useState(false);
  const rowsPerPage = 50;

  useEffect(() => {
    fetchSystemStatus();
    fetchQueue();          // Full initial load — all phases
    fetchHarvestStatus();
    fetchHistory();
    fetchCoverage();
    
    const interval = setInterval(() => {
      fetchSystemStatus();
      fetchCoverage();
      // Only re-fetch queue for the currently visible tab (not all 6 every 5s)
      fetchQueue([activeTabRef.current, 'recent']);
    }, 5000);

    const es = new EventSource(`${API_BASE}/api/logs/stream`);
    es.onmessage = (event) => {
      const data = JSON.parse(event.data);
      setLogs(prev => {
        const newLogs = [...prev, data];
        return newLogs.slice(-50); 
      });
    };

    return () => {
      clearInterval(interval);
      es.close();
    };
  }, []);

  // Keep activeTabRef in sync so the polling interval always reads current tab
  useEffect(() => { activeTabRef.current = activeTab; }, [activeTab]);

  useEffect(() => {
    if (logsRef.current) {
      logsRef.current.scrollTop = logsRef.current.scrollHeight;
    }
  }, [logs]);

  useEffect(() => {
    if (activeTab === 'phase6') {
      fetchSpots(0, gridFilter, sortCol, sortDir, searchQuery);
      fetchDatabankCoverage();
    }
  }, [activeTab, gridFilter, sortCol, sortDir, searchQuery]);

  // --- Data Fetchers ---
  const fetchSystemStatus = async () => {
    try {
      const res = await fetch(`${API_BASE}/status`);
      if (res.ok) {
        const data = await res.json();
        setStatus(data);
        setIsHeadless(data.isHeadless ?? true);
      }
      
      const configRes = await fetch(`${API_BASE}/config`);
      if (configRes.ok) {
         const { config } = await configRes.json();
         if (config) {
            setTargetFacilities(config.target_facilities || []);
            setStateOverride(Array.isArray(config.state_override) ? config.state_override : (config.state_override ? [config.state_override] : []));
            setSleepInterval(config.sleep_interval_ms || 10000);
            setCooldownBase(config.cooldown_base_ms || 300000);
            setCooldownJitter(config.cooldown_jitter_pct || 20);
            setMaxStrikes(config.max_consecutive_errors || 3);
            setAutoResume(config.auto_resume_enabled ?? true);
            setIdentityRotation(config.identity_rotation_enabled ?? true);
            setRandomizeViewport(config.randomize_viewport_enabled ?? true);
         }
      }
    } catch {
      setStatus({ isRunning: false, currentTarget: 'API OFFLINE', processedCount: 0, enrichedCount: 0, verifiedCount: 0, errorCount: 0, lastError: 'Could not connect.' });
    }
  };

  const [phaseQueues, setPhaseQueues] = useState<Record<string, any[]>>({});

  const fetchQueue = async (only?: string[]) => {
    // If `only` is provided, fetch just those phases; otherwise fetch all (initial load)
    const phasesToFetch = only ?? ['phase1', 'phase2', 'phase3', 'phase4', 'phase5', 'recent'];
    try {
      const results = await Promise.all(
         phasesToFetch.map(phase => 
            phase === 'recent' 
              ? fetch(`${API_BASE}/api/recent-spots`).then(r => r.json())
              : fetch(`${API_BASE}/api/queue?phase=${phase}`).then(r => r.json())
         )
      );
      
      // Merge into existing state so unloaded phases retain their last known values
      const updates: Record<string, any[]> = {};
      phasesToFetch.forEach((phase, idx) => {
         updates[phase] = results[idx]?.spots || [];
      });
      setPhaseQueues(prev => ({ ...prev, ...updates }));
    } catch (e) {
      console.error('Queue fetch error:', e);
    }
  }

  const fetchHarvestStatus = async () => {
    try {
      const res = await fetch(`${API_BASE}/api/harvest/status`);
      if (res.ok) setHarvestData(await res.json());
    } catch {}
  };

  const fetchHistory = async () => {
    try {
      const res = await fetch(`${API_BASE}/api/logs/history`);
      if (res.ok) {
        const data = await res.json();
        setHistoryLogs(data.history || []);
      }
    } catch {}
  };

  const fetchCoverage = async () => {
    try {
      const res = await fetch(`${API_BASE}/api/stats/coverage`);
      if (res.ok) {
        const data = await res.json();
        setCoverageStats(data.stats || []);
      }
    } catch {}
  };

  const fetchDatabankCoverage = async () => {
    try {
      const res = await fetch(`${API_BASE}/api/stats/databank-coverage`);
      if (res.ok) {
        const data = await res.json();
        setDatabankCoverage(data.rows || []);
      }
    } catch {}
  };

  const fetchSpots = async (pageIdx: number, filter: string, col = sortCol, dir = sortDir, search = searchQuery) => {
    try {
      const offset = pageIdx * rowsPerPage;
      const res = await fetch(`${API_BASE}/api/spots?limit=${rowsPerPage}&offset=${offset}&status=${filter}&sortCol=${col}&sortDir=${dir}&search=${search}`);
      if (res.ok) {
        const data = await res.json();
        setSpots(data.spots);
        setTotalSpots(data.total);
        setPage(pageIdx);
      }
    } catch (e) {
      console.error(e);
    }
  };

  // --- Config Managers ---
  const updateGlobalStrategy = async (field: string, value: any) => {
     let newOverride = stateOverride;
     let newFacilities = [...targetFacilities];
     let newSleep = sleepInterval;
     let newCooldown = cooldownBase;
     let newJitter = cooldownJitter;
     let newStrikes = maxStrikes;
     let newAutoResume = autoResume;

     if (field === 'state_override') {
       if (value === 'ALL') {
         newOverride = [];
       } else {
         if (newOverride.includes(value)) {
           newOverride = newOverride.filter(s => s !== value);
         } else {
           newOverride = [...newOverride, value];
         }
       }
       setStateOverride(newOverride);
     } else if (field === 'sleep_interval') {
       newSleep = value;
       setSleepInterval(value);
     } else if (field === 'facility' || field === 'target_facilities') {
       if (newFacilities.includes(value)) {
         newFacilities = newFacilities.filter(f => f !== value);
       } else {
         newFacilities.push(value);
       }
       setTargetFacilities(newFacilities);
     } else if (field === 'cooldown_base_ms') {
       newCooldown = value;
       setCooldownBase(value);
     } else if (field === 'cooldown_jitter_pct') {
       newJitter = value;
       setCooldownJitter(value);
     } else if (field === 'max_consecutive_errors') {
       newStrikes = value;
       setMaxStrikes(value);
     } else if (field === 'auto_resume_enabled') {
       newAutoResume = value;
       setAutoResume(value);
     } else if (field === 'identity_rotation_enabled') {
       setIdentityRotation(value);
     } else if (field === 'randomize_viewport_enabled') {
       setRandomizeViewport(value);
     }

     await fetch(`${API_BASE}/config`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ 
          state_override: newOverride, 
          target_facilities: newFacilities, 
          sleep_interval_ms: newSleep,
          cooldown_base_ms: newCooldown,
          cooldown_jitter_pct: newJitter,
          max_consecutive_errors: newStrikes,
          auto_resume_enabled: newAutoResume,
          identity_rotation_enabled: field === 'identity_rotation_enabled' ? value : identityRotation,
          randomize_viewport_enabled: field === 'randomize_viewport_enabled' ? value : randomizeViewport
        })
     });
  };

  const triggerHarvest = async (type: string, states: string[] = []) => {
    setIsStarting(true);
    try {
       await fetch(`${API_BASE}/api/harvest/${type}`, {
         method: 'POST',
         headers: { 'Content-Type': 'application/json' },
         body: JSON.stringify({ target_facilities: targetFacilities, target_states: states, provider: seedProvider })
       });
       fetchSystemStatus();
    } catch (e) {
       alert('Harvest failed to start.');
    }
    setTimeout(() => setIsStarting(false), 1000);
  };

  const triggerForceHarvest = async (state: string) => {
    if (!confirm(`FORCE re-harvest of US-${state}? This will bypass the local cache.`)) return;
    try {
      await fetch(`${API_BASE}/api/harvest/force`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ state })
      });
      alert(`Forced re-harvest initiated for ${state}.`);
    } catch (e) {
      alert('Failed to trigger force harvest.');
    }
  };

  const triggerDiscovery = async (state: string) => {
    const stateFull = prompt('Enter FULL state name for Google Maps discovery (e.g., "Missouri"):');
    if (!stateFull) return;
    try {
      await fetch(`${API_BASE}/api/discover`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ stateFull })
      });
      alert(`Direct Discovery initiated for ${stateFull}. Check logs.`);
    } catch (e) {
      alert('Discovery failed to start.');
    }
  };

  const handleSysStart = async () => { await fetch(`${API_BASE}/start`, { method: 'POST' }); fetchSystemStatus(); };
  const handleSysStop = async () => { await fetch(`${API_BASE}/stop`, { method: 'POST' }); fetchSystemStatus(); };
  
  const triggerSpecificDaemon = async (name: string, action: 'start' | 'stop') => {
    try {
      await fetch(`${API_BASE}/api/daemons/${name}/${action}`, { method: 'POST' });
      fetchSystemStatus();
    } catch (e) {
      alert(`Failed to ${action} ${name} daemon.`);
    }
  };
  
  const toggleHeadless = async (newVal: boolean) => {
    setIsHeadless(newVal);
    await fetch(`${API_BASE}/api/headless`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ isHeadless: newVal })
    });
  };

  const toggleSort = (col: string) => {
     if (sortCol === col) setSortDir(sortDir === 'asc' ? 'desc' : 'asc');
     else { setSortCol(col); setSortDir('asc'); }
  };

  const deleteSpot = async (id: string, name: string) => {
    if (!confirm(`PERMANENTLY delete record "${name}"?`)) return;
    try {
      await fetch(`${API_BASE}/api/spots/${id}`, { method: 'DELETE' });
      fetchSpots(page, gridFilter);
    } catch (e) {}
  };

  const startEdit = (spot: any) => { setEditingId(spot.id); setEditForm({ ...spot }); };
  const saveEdit = async () => {
    try {
      await fetch(`${API_BASE}/api/spots/${editingId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(editForm)
      });
      setEditingId(null);
      fetchSpots(page, gridFilter);
    } catch (e) {}
  };

  const updateSpotStatus = async (id: string, status: string) => {
    try {
      await fetch(`${API_BASE}/api/spots/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ verification_status: status })
      });
      fetchSpots(page, gridFilter);
    } catch (e) {}
  };

  const promoteSpot = async (id: string, published: boolean) => {
    try {
      await fetch(`${API_BASE}/api/spots/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ is_published: published })
      });
      fetchSpots(page, gridFilter);
    } catch (e) {}
  };

  const bulkPromote = async () => {
    if (!confirm(`Are you sure you want to promote ALL Gold-Standard Verified spots to the public Skate Map?`)) return;
    try {
      await fetch(`${API_BASE}/api/promote-all`, { method: 'POST' });
      alert('Bulk promotion complete!');
      fetchSpots(page, gridFilter);
    } catch (e) {}
  };

  const PIPELINE_PHASES = [
    { id: '1', title: 'The Scout', sub: 'Google Places (Primary) / OSM (Fallback)', route: 'phase1', color: '#8a2be2', 
      target: 'Nationwide → ENRICHED / PENDING', 
      metric: (status?.enrichedCount || 0) + (status?.pendingCount || 0), metricLabel: 'Total Seeded', isDaemon: false, 
      statusActive: status?.isHarvestingActive || status?.isGoogleSweepActive },
    { id: '2', title: 'The Operator', sub: 'Identity & Contact (OSM Fallback only)', route: 'phase2', color: '#5d78ff', 
      target: 'PENDING → IDENTITY_ESTABLISHED', 
      metric: status?.identityCount || 0, metricLabel: 'Identities Resolved', isDaemon: true, 
      statusActive: status?.currentTarget?.includes('Operator: online') },
    { id: '3', title: 'The Detective', sub: 'Website Deep Crawl + Photo Candidates', route: 'phase3', color: '#ff5a00', 
      target: 'ENRICHED / IDENTITY_ESTABLISHED → is_deep_crawled', 
      metric: status?.indexedCount || 0, metricLabel: 'Websites Crawled', isDaemon: true, 
      statusActive: status?.currentTarget?.includes('Indexer: online') },
    { id: '4', title: 'The Photographer', sub: 'Free Photo Harvest (OG + Street View)', route: 'phase4', color: '#e91e63', 
      target: 'ENRICHED → MEDIA_READY', 
      metric: status?.mediaReadyCount || 0, metricLabel: 'Galleries Built', isDaemon: true, 
      statusActive: status?.currentTarget?.includes('Photographer: online') },
    { id: '5', title: 'The Publisher', sub: 'App Release Gate', route: 'phase5', color: '#4caf50', 
      target: 'MEDIA_READY / ENRICHED → is_published', 
      metric: status?.verifiedCount || 0, metricLabel: 'Live on App Map', isDaemon: false, 
      statusActive: true },
    { id: '6', title: 'Databank QA', sub: 'Master Review Grid + Coverage Map', route: 'phase6', color: '#ffb300', 
      target: 'ALL → VERIFIED', 
      metric: (status?.enrichedCount || 0) + (status?.mediaReadyCount || 0) + (status?.verifiedCount || 0), 
      metricLabel: 'Gold Standard', isDaemon: false, 
      statusActive: true }
  ];

  return (
    <div className="dashboard-container">
      <header className="header" style={{ alignItems: 'center', flexWrap: 'nowrap', display: 'flex', justifyContent: 'space-between' }}>
        <div>
          <h1 className="title">SK8Lytz Global Extraction Pipeline</h1>
          <p style={{marginTop: 0, color: 'var(--text-secondary)', fontSize: '0.9rem'}}>Decoupled 6-Phase Micro-Scraper Architecture</p>
        </div>
        <div className="daemon-status-header" style={{ display: 'flex', gap: '8px', flexWrap: 'wrap', justifyContent: 'flex-end', alignItems: 'center' }}>
           {/* Fleet Control — always-visible per-daemon start/stop */}
           {[
             { id: 'operator',     label: 'Operator',     color: '#4caf50',  phase: '2', onKey: 'Operator: online' },
             { id: 'indexer',      label: 'Indexer',      color: '#ff5a00',  phase: '3', onKey: 'Indexer: online' },
             { id: 'photographer', label: 'Photographer', color: '#e91e63',  phase: '4', onKey: 'Photographer: online' },
           ].map(d => {
             const isOn = status?.currentTarget?.includes(d.onKey);
             return (
               <div key={d.id} style={{ display: 'flex', alignItems: 'center', gap: '6px', background: isOn ? `${d.color}15` : 'rgba(255,255,255,0.04)', padding: '5px 10px', borderRadius: '20px', border: `1px solid ${isOn ? d.color + '66' : 'rgba(255,255,255,0.1)'}`, transition: 'all 0.3s' }}>
                 <div className={`status-dot ${isOn ? 'online' : 'offline'}`} style={{ background: isOn ? d.color : '', boxShadow: isOn ? `0 0 8px ${d.color}` : '' }}></div>
                 <span style={{ fontSize: '0.7rem', color: isOn ? d.color : 'var(--text-secondary)', fontWeight: 700 }}>PH{d.phase} {d.label.toUpperCase()}</span>
                 <button
                   onClick={() => triggerSpecificDaemon(d.id, isOn ? 'stop' : 'start')}
                   style={{ fontSize: '0.65rem', fontWeight: 800, padding: '2px 8px', borderRadius: '10px', border: 'none', cursor: 'pointer', background: isOn ? 'rgba(255,60,60,0.3)' : `${d.color}33`, color: isOn ? '#ff6b6b' : d.color, letterSpacing: '0.05em', transition: 'all 0.2s' }}
                 >
                   {isOn ? '■ STOP' : '▶ START'}
                 </button>
               </div>
             );
           })}
           <button
             onClick={handleSysStop}
             title="Emergency stop all daemons"
             style={{ fontSize: '0.65rem', fontWeight: 800, padding: '5px 12px', borderRadius: '20px', border: '1px solid rgba(255,60,60,0.3)', cursor: 'pointer', background: 'rgba(255,60,60,0.1)', color: '#ff6b6b', letterSpacing: '0.05em' }}
           >
             ⛔ STOP ALL
           </button>
        </div>
      </header>


      {/* =========== OMNI CONTROL CENTER (GLOBAL CONTROLS) =========== */}
      <div className="omni-control-center fade-in" style={{ marginBottom: '2rem', background: 'var(--surface)', padding: '1.5rem', borderRadius: '12px', border: '1px solid var(--surface-highlight)' }}>
         <div className="security-control-bar" style={{ position: 'relative', background: 'transparent', padding: '0 0 1rem 0', borderBottom: '1px solid rgba(255,255,255,0.05)', marginBottom: '1rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <span className="ghost-badge mini" style={{position: 'absolute', top: -35, right: 0}}>🛡️ GHOST ENCRYPTED PIPELINE ACTIVE</span>
            <div className="security-label">
                <span style={{fontSize: '0.8rem', color: 'var(--text-secondary)', textTransform: 'uppercase', fontWeight: 800}}>🛡️ UNIVERSAL TACTICS & SPOOFING</span>
            </div>
            <div className="security-inputs" style={{ display: 'flex', gap: '20px', alignItems: 'center' }}>
                <div className="input-group-inline">
                  <label>Base Cooldown (ms)</label>
                  <input type="number" className="mini-input" value={cooldownBase} onChange={e => updateGlobalStrategy('cooldown_base_ms', parseInt(e.target.value))} />
                </div>
                <div className="input-group-inline">
                  <label>Jitter Entropy (%)</label>
                  <input type="number" className="mini-input" value={cooldownJitter} onChange={e => updateGlobalStrategy('cooldown_jitter_pct', parseInt(e.target.value))} />
                </div>
                <div className="v-divider" style={{ width: '1px', height: '20px', background: 'rgba(255, 255, 255, 0.1)' }}></div>
                <label className="input-group-inline" style={{cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '8px'}}>
                  <span>Headless Stealth</span>
                  <label className="switch mini">
                    <input type="checkbox" checked={isHeadless} onChange={e => toggleHeadless(e.target.checked)} />
                    <span className="slider round"></span>
                  </label>
                </label>
                <label className="input-group-inline" style={{cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '8px'}}>
                  <span>Identity Spoofing</span>
                  <label className="switch mini">
                    <input type="checkbox" checked={identityRotation} onChange={e => updateGlobalStrategy('identity_rotation_enabled', e.target.checked)} />
                    <span className="slider round"></span>
                  </label>
                </label>
            </div>
         </div>

         <div className="omni-grid" style={{ display: 'flex', gap: '1.5rem', alignItems: 'center' }}>
            <div className="master-power-panel" style={{ flex: 1 }}>
               <h3 style={{ fontSize: '0.85rem', textTransform: 'uppercase', color: 'var(--text-secondary)', marginBottom: '1rem', marginTop: 0 }}>Omni-Engine Power Grid</h3>
               
               <div style={{ display: 'grid', gridTemplateColumns: 'minmax(200px, 1fr) 1fr', gap: '1rem' }}>
                  <div className="btn-group-vertical" style={{ display: 'flex', gap: '0.5rem' }}>
                     <button className="btn btn-start" onClick={handleSysStart} disabled={status?.isRunning}>
                       🔥 BOOT ALL DAEMONS (Ph2+)
                     </button>
                     <button className="btn btn-stop" onClick={handleSysStop} disabled={!status?.isRunning}>
                       🛑 HALT ALL
                     </button>
                  </div>
                  
                  <div className="input-group-inline" style={{ background: 'rgba(0,0,0,0.2)', padding: '0.8rem', borderRadius: '8px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                     <label style={{fontSize: '0.8rem', color: 'var(--text-secondary)'}}>Global Throttle Interval (ms)</label>
                     <input type="number" className="mini-input" style={{width: '90px'}} value={sleepInterval} onChange={e => updateGlobalStrategy('sleep_interval', parseInt(e.target.value))} />
                  </div>
               </div>
            </div>
         </div>
      </div>

      {/* =========== 6-PHASE UNIFORM PIPELINE GRID =========== */}
      <div className="pipeline-grid fade-in">
         {PIPELINE_PHASES.map((phase) => (
            <div 
               key={phase.id} 
               className={`pipeline-card ${activeTab === phase.route ? 'active' : ''}`}
               onClick={() => setActiveTab(phase.route as any)}
               style={{ borderTop: `4px solid ${phase.color}` }}
            >
               <div className="pipeline-card-header">
                  <span className="phase-id">Phase {phase.id}</span>
                  <div className={`status-dot ${phase.statusActive ? 'online' : 'offline'}`}></div>
               </div>
               <h3 style={{ color: phase.color }}>{phase.title}</h3>
               <p className="phase-sub">{phase.sub}</p>
               <p className="phase-target">{phase.target}</p>
               
               <div className="mini-stat-box" style={{marginTop: 'auto'}}>
                  <span className="stat-label">{phase.metricLabel}</span>
                  <div className="stat-value" style={{ color: phase.color }}>{phase.metric?.toLocaleString()}</div>
               </div>
            </div>
         ))}
      </div>

      <div className="content-area fade-in">
        {/* =========== PHASE 1: GLOBAL STRATEGY & INTAKE =========== */}
        {activeTab === 'phase1' && (
          <div className="tab-pane phase-1">
            <div className="explainer-block" style={{marginBottom: '1rem'}}>
              <h3 style={{marginTop: 0, color: '#8a2be2'}}>The Scout: GIS Ingestion Engine</h3>
              <p>Dual-mode Phase 1 seeder. <strong style={{color:'#ffb300'}}>Google Places (Primary)</strong> writes <strong>ENRICHED</strong> records directly — high-fidelity data including coordinates, phone, hours, rating, and website. Toggle to <strong>OSM Mode</strong> to fall back to OpenStreetMap Overpass API, which writes raw <strong>PENDING</strong> skeletons for the Operator to resolve. Google Mode is recommended for all new harvests.</p>
            </div>

            <div className="flow-visualizer" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '2rem', padding: '3rem 2rem', background: 'rgba(0,0,0,0.2)', borderRadius: '12px', marginTop: '1rem', marginBottom: '2rem' }}>
               <div style={{ textAlign: 'center', minWidth: '100px' }}>
                  <div style={{ fontSize: '2.5rem', fontWeight: 800, color: '#8a2be2' }}>∞</div>
                  <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', textTransform: 'uppercase' }}>OSM Dataset</div>
               </div>
               <div style={{ flex: 1, height: '4px', background: 'rgba(255,255,255,0.1)', position: 'relative' }}>
                  <div style={{ position: 'absolute', top: '-40px', left: '50%', transform: 'translateX(-50%)', display: 'flex', gap: '10px' }}>
                     <button className={`btn-mini start ${isStarting ? 'pulsing' : ''}`} onClick={() => triggerHarvest('start-all', stateOverride)} disabled={status?.isHarvestingActive || status?.isGoogleSweepActive || isStarting}>
                        ▶ {isStarting ? 'INITIATING...' : stateOverride.length > 0 ? `SEED ${stateOverride.join(', ')}` : 'GLOBAL SEED'}
                     </button>
                     <button className="btn-mini stop" onClick={() => triggerHarvest('stop-all')} disabled={!status?.isHarvestingActive && !status?.isGoogleSweepActive}>■ STOP</button>
                  </div>
                  {(status?.isHarvestingActive || status?.isGoogleSweepActive) && <div className="flow-animation"></div>}
               </div>
               <div style={{ textAlign: 'center', minWidth: '100px' }}>
                  <div style={{ fontSize: '2.5rem', fontWeight: 800, color: '#8a2be2' }}>{status?.pendingCount || 0}</div>
                  <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>PENDING DB</div>
               </div>
            </div>

            {/* OMNI-NET DISCOVERY PANEL */}
            <div className="discovery-net-panel" style={{ background: 'linear-gradient(135deg, rgba(138,43,226,0.1) 0%, rgba(0,0,0,0.3) 100%)', padding: '1.5rem', borderRadius: '12px', border: '1px solid rgba(138, 43, 226, 0.3)', marginBottom: '2rem', position: 'relative' }}>
               <div style={{ position: 'absolute', top: '-10px', right: '20px', background: '#8a2be2', color: '#fff', fontSize: '0.6rem', padding: '2px 8px', borderRadius: '4px', fontWeight: 800 }}>OMNI-NET DISCOVERY v2.0</div>
               <h3 style={{ marginTop: 0, color: '#fff', fontSize: 18 }}>📡 Omni-Net Discovery & Re-seeding</h3>
               <p style={{ fontSize: '0.8rem', color: 'rgba(255,255,255,0.6)', marginBottom: '1.5rem' }}>Use these controls to bypass OpenStreetMap limitations or manually target "Stealth" rinks that are under-mapped in GIS datasets.</p>
               
               <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                  <div style={{ background: 'rgba(255,255,255,0.03)', padding: '1rem', borderRadius: '8px' }}>
                     <h4 style={{ margin: '0 0 10px 0', fontSize: '0.8rem', color: '#8a2be2' }}>Wide-Net Re-harvest</h4>
                     <p style={{ fontSize: '0.7rem', color: 'rgba(255,255,255,0.4)', marginBottom: '1rem' }}>Force-refresh OSM GIS data using the new naming heuristic net. Captures rinks tagged only as generic buildings.</p>
                     <div style={{ display: 'flex', gap: '8px' }}>
                       <select className="mini-input" style={{ flex: 1, background: '#1a1a1a' }} id="force-state-sel">
                         {US_STATES.map(s => <option key={s} value={s}>{s}</option>)}
                       </select>
                       <button className="btn-mini" onClick={() => triggerForceHarvest((document.getElementById('force-state-sel') as HTMLSelectElement).value)}>FORCE RELOAD</button>
                     </div>
                  </div>
                  
                  <div style={{ background: 'rgba(255,255,255,0.03)', padding: '1rem', borderRadius: '8px' }}>
                     <h4 style={{ margin: '0 0 10px 0', fontSize: '0.8rem', color: '#ffb300' }}>Origin Provider</h4>
                     <p style={{ fontSize: '0.7rem', color: 'rgba(255,255,255,0.4)', marginBottom: '1rem' }}>Switch between OpenStreetMap (Discovery) and Google Places (Premium). Affects the SEED button above.</p>
                     <div className="provider-toggle switch-row" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', background: 'rgba(0,0,0,0.5)', padding: '8px', borderRadius: '6px' }}>
                         <span style={{ fontSize: '0.8rem', fontWeight: 600, color: seedProvider === 'google' ? '#ffb300' : '#8a2be2' }}>{seedProvider === 'osm' ? 'OSM Mode' : 'Google Mode'}</span>
                         <label className="switch">
                            <input type="checkbox" checked={seedProvider === 'google'} onChange={e => setSeedProvider(e.target.checked ? 'google' : 'osm')} />
                            <span className="slider round"></span>
                         </label>
                     </div>
                  </div>
               </div>
            </div>

            <div className="omni-grid tri-grid phase-1-controls" style={{ display: 'grid', gap: '1.5rem', gridTemplateColumns: 'minmax(250px, 1fr) 1.5fr', marginBottom: '2rem' }}>
               <div className="facility-switches-panel" style={{ background: 'rgba(255,255,255,0.02)', padding: '1.5rem', borderRadius: '12px', border: '1px solid rgba(255,255,255,0.05)' }}>
                  <h3 style={{ fontSize: '0.9rem', textTransform: 'uppercase', color: 'var(--text-secondary)', marginBottom: '1.5rem', marginTop: 0 }}>Target Facilities</h3>
                  <div className="facility-switches">
                     {['skatepark', 'roller_rink', 'skate_shop'].map(f => (
                       <label key={f} className="switch-row mini" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.2rem', padding: '4px 0' }}>
                          <span style={{ fontSize: '0.9rem', fontWeight: 600 }}>{f.replace('_', ' ').toUpperCase()}</span>
                          <label className="switch">
                            <input type="checkbox" checked={targetFacilities.includes(f)} onChange={() => updateGlobalStrategy('facility', f)} />
                            <span className="slider round"></span>
                          </label>
                       </label>
                     ))}
                  </div>
               </div>
               
               <div className="state-targets-panel" style={{ background: 'rgba(255,255,255,0.02)', padding: '1.5rem', borderRadius: '12px', border: '1px solid rgba(255,255,255,0.05)' }}>
                  <h3 style={{ fontSize: '0.9rem', textTransform: 'uppercase', color: 'var(--text-secondary)', marginBottom: '1rem', marginTop: 0, display: 'flex', justifyContent: 'space-between' }}>
                    Target States
                    <span className="btn-mini" onClick={() => updateGlobalStrategy('state_override', 'ALL')} style={{ cursor: 'pointer' }}>ALL</span>
                  </h3>
                  <div className="state-pill-container" style={{maxHeight: '180px', overflowY: 'auto', gap: '6px', display: 'flex', flexWrap: 'wrap'}}>
                     {US_STATES.map(st => {
                        const count = harvestData.stateCounts[st] || 0;
                        return (
                          <button key={st} className={`state-pill mini ${stateOverride.includes(st) ? 'active' : ''}`} onClick={() => updateGlobalStrategy('state_override', st)} style={{ padding: '6px 12px', fontSize: '0.8rem', margin: '2px', background: stateOverride.includes(st) ? 'var(--primary-color)' : 'rgba(255,255,255,0.1)', color: stateOverride.includes(st) ? '#000' : '#fff', border: 'none', borderRadius: '12px', cursor: 'pointer' }}>
                            {st} {count > 0 && <span style={{opacity: 0.6, fontSize: '0.7em', marginLeft: '4px'}}>({count})</span>}
                          </button>
                        )
                     })}
                  </div>
               </div>
            </div>

            <div className="panel coverage-panel" style={{marginTop: '2rem', textAlign: 'center'}}>
              <h2 className="panel-header">GIS Intake Leaderboard (State Coverage)</h2>
              <div style={{ marginTop: '2rem', display: 'flex', justifyContent: 'center', width: '100%', maxWidth: '800px', margin: '0 auto' }}>
                 {/* @ts-ignore */}
                 <USAMap 
                    defaultFill="rgba(255,255,255,0.05)"
                    customize={(() => {
                       const colors: Record<string, any> = {};
                       coverageStats.forEach((stat: any) => {
                          if (stat.total === 0) return;
                          const density = Math.min(stat.total / 30, 1);
                          const opacity = Math.max(density, 0.3); // Min 30% visibility so 1 record is visible
                          
                          let color = `rgba(138,43,226,${opacity})`; 
                          const enrichedRatio = stat.enriched / stat.total;
                          if (enrichedRatio >= 0.5 || stat.enriched > 5) color = `rgba(255,90,0,${opacity})`;
                          const verifiedRatio = stat.verified / stat.total;
                          if (verifiedRatio >= 0.5) color = `rgba(76,175,80,${opacity})`;
                          
                          colors[stat.state] = { fill: color, customText: stat.total.toString() };
                       });
                       return colors;
                    })()}
                    onClick={(e: any) => updateGlobalStrategy('state_override', e.target.dataset.name)}
                 />
              </div>
              <div style={{ display: 'flex', justifyContent: 'center', gap: '2rem', marginTop: '1.5rem', fontSize: '0.8rem', color: 'var(--text-secondary)' }}>
                 <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}><span style={{ width: '12px', height: '12px', background: 'rgba(255,255,255,0.05)', borderRadius: '2px' }}></span> Empty / No Target</div>
                 <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}><span style={{ width: '12px', height: '12px', background: '#8a2be2', borderRadius: '2px' }}></span> In Processing Pipeline</div>
                 <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}><span style={{ width: '12px', height: '12px', background: '#ff5a00', borderRadius: '2px' }}></span> Deep Enriched</div>
                 <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}><span style={{ width: '12px', height: '12px', background: '#4caf50', borderRadius: '2px' }}></span> Final Verified</div>
              </div>
            </div>
            
            <div className="evasion-audit-card" style={{ marginTop: '2rem', padding: '1.5rem', background: 'rgba(0,0,0,0.3)', borderRadius: '12px', border: '1px solid rgba(138, 43, 226, 0.2)' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
                 <h3 style={{ margin: 0, color: '#8a2be2', fontSize: '0.9rem', textTransform: 'uppercase' }}>🛡️ Phase 1 Evasion Audit</h3>
                 <div className="pulse-badge">LAST: {status?.pulseRegistry?.['Phase 1']?.lastRunAt ? new Date(status.pulseRegistry['Phase 1'].lastRunAt).toLocaleTimeString() : 'NEVER'}</div>
              </div>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                 <div className="audit-stat">
                    <label>SPOOFED IDENTITY</label>
                    <div className="audit-val" style={{ fontSize: '0.75rem', opacity: 0.8, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                       {status?.pulseRegistry?.['Phase 1']?.ghost?.userAgent || 'ROTATING...'}
                    </div>
                 </div>
                 <div className="audit-stat" style={{ textAlign: 'center' }}>
                    <PulseTimer nextRunAt={status?.pulseRegistry?.['Phase 1']?.nextRunAt} />
                 </div>
              </div>
            </div>

            <div style={{marginTop: '2rem'}}>
                <h4 style={{fontSize: '0.8rem', textTransform:'uppercase', color:'var(--text-secondary)', marginBottom: 0}}>Recently Harvested Seeds (Live)</h4>
                {(phaseQueues['recent'] || []).length === 0 ? (
                    <div style={{ background: 'rgba(255,255,255,0.02)', padding: '2rem', textAlign: 'center', borderRadius: '8px', color: 'var(--text-secondary)', fontSize: '0.9rem', fontStyle: 'italic', marginTop: '10px' }}>
                        Vault is empty. Click [GLOBAL SEED] to spawn targets from OSM or Google API.
                    </div>
                ) : (
                    <div className="mini-data-bank" style={{marginTop: '10px'}}>
                      {(phaseQueues['recent'] || []).map(spot => (
                        <div key={spot.id} className="queue-card active" style={{ borderColor: spot.verification_status === 'ENRICHED' ? '#ff5a00' : 'rgba(255,255,255,0.1)' }}>
                          <div className="queue-card-title">{spot.name}</div>
                          <div className="queue-card-loc">{spot.city}, {spot.state}</div>
                          <div className="queue-tags">
                            <span className="queue-badge" style={{ background: spot.verification_status === 'ENRICHED' ? 'rgba(255,90,0,0.1)' : 'rgba(255,255,255,0.05)', color: spot.verification_status === 'ENRICHED' ? '#ff5a00' : 'var(--text-secondary)' }}>
                              {spot.verification_status === 'ENRICHED' ? '✨ GOLDEN SEED' : '⏳ RAW SEED'}
                            </span>
                          </div>
                        </div>
                      ))}
                    </div>
                )}
            </div>
          </div>
        )}

        {/* =========== DAEMON CONTROL CENTER (PHASE 2-5) =========== */}
        {(['phase2', 'phase3', 'phase4', 'phase5'].includes(activeTab)) && (
          <div className="tab-pane daemon-center">
             <div className="explainer-block" style={{marginBottom: '1rem'}}>
               <h3 style={{marginTop: 0, color: PIPELINE_PHASES.find(p=>p.route===activeTab)?.color}}>
                   {PIPELINE_PHASES.find(p=>p.route===activeTab)?.title}: {PIPELINE_PHASES.find(p=>p.route===activeTab)?.sub}
               </h3>
               {activeTab === 'phase2' && <p>Targets <strong>PENDING</strong> OSM-sourced records and resolves their real-world identity — finding the business website and phone number via web search heuristics. Graduates records to <strong>IDENTITY_ESTABLISHED</strong> when found.</p>}
               {activeTab === 'phase3' && <p>The Detective deep-crawls each spot's website using Puppeteer with GHOST identity spoofing. Extracts operating hours, 18+ adult night schedules, pricing, event listings, social links, and photo candidates (OG image, DOM images, Facebook OG). Writes <code>candidate_photos</code> for the Photographer to harvest.</p>}
               {activeTab === 'phase4' && <p>The Photographer daemon reads <code>candidate_photos</code> written by the Indexer — downloading OG images and DOM media as binary uploads to Supabase Storage. Falls back to Google Street View Static as a guaranteed photo source. Promotes records to <strong>MEDIA_READY</strong> on success.</p>}
               {activeTab === 'phase5' && <p>The Publisher Gate is the final human-approved release step. Only records with <strong style={{color:'#4caf50'}}>is_published = true</strong> are visible on the live SK8Lytz app map. Bulk-promote all pipeline-complete records (ENRICHED + MEDIA_READY) below, or use the Databank QA tab to approve individual spots.</p>}
             </div>
             
             {activeTab === 'phase2' && (
                <div className="flow-visualizer" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '2rem', padding: '3rem 2rem', background: 'rgba(0,0,0,0.2)', borderRadius: '12px', marginTop: '1rem' }}>
                   <div style={{ textAlign: 'center', minWidth: '100px' }}>
                      <div style={{ fontSize: '2.5rem', fontWeight: 800, color: '#8a2be2' }}>{status?.pendingCount || 0}</div>
                      <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>PENDING</div>
                   </div>
                   <div style={{ flex: 1, height: '4px', background: 'rgba(255,255,255,0.1)', position: 'relative' }}>
                      <div style={{ position: 'absolute', top: '-40px', left: '50%', transform: 'translateX(-50%)', display: 'flex', gap: '10px' }}>
                         <button className="btn-mini start" onClick={() => triggerSpecificDaemon('operator', 'start')} disabled={status?.currentTarget?.includes('Operator: online')}>▶ START OPERATOR</button>
                         <button className="btn-mini stop" onClick={() => triggerSpecificDaemon('operator', 'stop')} disabled={!status?.currentTarget?.includes('Operator: online')}>■ STOP</button>
                      </div>
                      <div style={{ position: 'absolute', top: '15px', left: '50%', transform: 'translateX(-50%)', background: 'rgba(255,255,255,0.05)', padding: '2px 8px', borderRadius: '4px', fontSize: '11px', whiteSpace: 'nowrap', color: 'var(--text-secondary)' }}>
                         {status?.identityCount || 0} / {(status?.pendingCount || 0) + (status?.identityCount || 0)} Completed
                      </div>
                      {status?.currentTarget?.includes('Operator: online') && <div className="flow-animation"></div>}
                   </div>
                   <div style={{ textAlign: 'center', minWidth: '100px' }}>
                      <div style={{ fontSize: '2.5rem', fontWeight: 800, color: '#5d78ff' }}>{status?.identityCount || 0}</div>
                      <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>IDENTIFIED</div>
                   </div>
                </div>
             )}

             {activeTab === 'phase3' && (
                <div className="flow-visualizer" style={{ padding: '3rem 2rem', background: 'rgba(0,0,0,0.2)', borderRadius: '12px', marginTop: '1rem' }}>
                  <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '2rem' }}>
                    {/* Dual input: both ENRICHED and IDENTITY_ESTABLISHED feed into Indexer */}
                    <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem', alignItems: 'flex-end', minWidth: '120px' }}>
                      <div style={{ textAlign: 'center' }}>
                        <div style={{ fontSize: '1.8rem', fontWeight: 800, color: '#ff5a00' }}>{status?.enrichedCount || 0}</div>
                        <div style={{ fontSize: '0.7rem', color: 'var(--text-secondary)' }}>ENRICHED</div>
                        <div style={{ fontSize: '0.6rem', color: 'rgba(255,90,0,0.6)' }}>Google Primary</div>
                      </div>
                      <div style={{ textAlign: 'center' }}>
                        <div style={{ fontSize: '1.8rem', fontWeight: 800, color: '#5d78ff' }}>{status?.identityCount || 0}</div>
                        <div style={{ fontSize: '0.7rem', color: 'var(--text-secondary)' }}>IDENTITY_EST.</div>
                        <div style={{ fontSize: '0.6rem', color: 'rgba(93,120,255,0.6)' }}>OSM Fallback</div>
                      </div>
                    </div>
                    <div style={{ flex: 1, height: '4px', background: 'rgba(255,255,255,0.1)', position: 'relative' }}>
                       <div style={{ position: 'absolute', top: '-40px', left: '50%', transform: 'translateX(-50%)', display: 'flex', gap: '10px' }}>
                          <button className="btn-mini start" onClick={() => triggerSpecificDaemon('indexer', 'start')} disabled={status?.currentTarget?.includes('Indexer: online')}>▶ START DETECTIVE</button>
                          <button className="btn-mini stop" onClick={() => triggerSpecificDaemon('indexer', 'stop')} disabled={!status?.currentTarget?.includes('Indexer: online')}>■ STOP</button>
                       </div>
                       <div style={{ position: 'absolute', top: '15px', left: '50%', transform: 'translateX(-50%)', background: 'rgba(255,255,255,0.05)', padding: '2px 8px', borderRadius: '4px', fontSize: '11px', whiteSpace: 'nowrap', color: 'var(--text-secondary)' }}>
                          {status?.indexedCount || 0} websites crawled (+ photo candidates)
                       </div>
                       {status?.currentTarget?.includes('Indexer: online') && <div className="flow-animation"></div>}
                    </div>
                    <div style={{ textAlign: 'center', minWidth: '100px' }}>
                       <div style={{ fontSize: '2.5rem', fontWeight: 800, color: '#e91e63' }}>{status?.candidatesReadyCount || 0}</div>
                       <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>PHOTO CANDIDATES</div>
                       <div style={{ fontSize: '0.65rem', color: 'rgba(255,255,255,0.3)', marginTop: '4px' }}>Awaiting harvest</div>
                    </div>
                  </div>
                </div>
             )}

             {activeTab === 'phase4' && (
                <div className="flow-visualizer" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '2rem', padding: '3rem 2rem', background: 'rgba(0,0,0,0.2)', borderRadius: '12px', marginTop: '1rem' }}>
                   <div style={{ textAlign: 'center', minWidth: '100px' }}>
                      <div style={{ fontSize: '2.5rem', fontWeight: 800, color: '#e91e63' }}>{status?.candidatesReadyCount || 0}</div>
                      <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>CANDIDATES</div>
                   </div>
                   <div style={{ flex: 1, height: '4px', background: 'rgba(255,255,255,0.1)', position: 'relative' }}>
                      <div style={{ position: 'absolute', top: '-40px', left: '50%', transform: 'translateX(-50%)', display: 'flex', gap: '10px' }}>
                         <button className="btn-mini start" onClick={() => triggerSpecificDaemon('photographer', 'start')} disabled={status?.currentTarget?.includes('Photographer: online')}>📸 START PHOTOGRAPHER</button>
                         <button className="btn-mini stop" onClick={() => triggerSpecificDaemon('photographer', 'stop')} disabled={!status?.currentTarget?.includes('Photographer: online')}>■ STOP</button>
                      </div>
                      <div style={{ position: 'absolute', top: '15px', left: '50%', transform: 'translateX(-50%)', background: 'rgba(255,255,255,0.05)', padding: '2px 8px', borderRadius: '4px', fontSize: '11px', whiteSpace: 'nowrap', color: 'var(--text-secondary)' }}>
                         OG Image → DOM Images → Street View → Facebook OG
                      </div>
                      {status?.currentTarget?.includes('Photographer: online') && <div className="flow-animation"></div>}
                   </div>
                   <div style={{ textAlign: 'center', minWidth: '100px' }}>
                      <div style={{ fontSize: '2.5rem', fontWeight: 800, color: '#e91e63' }}>{status?.mediaReadyCount || 0}</div>
                      <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>MEDIA_READY</div>
                   </div>
                </div>
             )}

             {activeTab === 'phase5' && (
                <div style={{ padding: '3rem 2rem', background: 'rgba(0,0,0,0.2)', borderRadius: '12px', marginTop: '1rem' }}>
                  <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '4rem', marginBottom: '2rem' }}>
                    <div style={{ textAlign: 'center' }}>
                      <div style={{ fontSize: '3rem', fontWeight: 800, color: '#e91e63' }}>{status?.mediaReadyCount || 0}</div>
                      <div style={{ fontSize: '0.9rem', color: 'var(--text-secondary)' }}>MEDIA_READY</div>
                    </div>
                    <div style={{ textAlign: 'center' }}>
                      <div style={{ fontSize: '3rem', fontWeight: 800, color: '#ff5a00' }}>{status?.enrichedCount || 0}</div>
                      <div style={{ fontSize: '0.9rem', color: 'var(--text-secondary)' }}>ENRICHED</div>
                    </div>
                    <div style={{ textAlign: 'center' }}>
                      <div style={{ fontSize: '3rem', fontWeight: 800, color: '#4caf50' }}>{status?.verifiedCount || 0}</div>
                      <div style={{ fontSize: '0.9rem', color: 'var(--text-secondary)' }}>LIVE ON MAP</div>
                    </div>
                  </div>
                  <div style={{ textAlign: 'center', padding: '1.5rem', background: 'rgba(76,175,80,0.05)', border: '1px solid rgba(76,175,80,0.3)', borderRadius: '8px' }}>
                    <p style={{ margin: '0 0 1rem', color: 'rgba(255,255,255,0.7)', fontSize: '0.9rem' }}>The Publisher Gate controls which records are visible to users on the SK8Lytz app map. Use the Databank QA tab to review individual records and toggle <strong style={{color:'#4caf50'}}>APP_LIVE</strong>, or bulk-promote all ENRICHED records below.</p>
                    <button className="btn btn-start" style={{background: '#4caf50', border: 'none'}} onClick={bulkPromote}>🚀 BULK PUBLISH ALL ENRICHED → APP MAP</button>
                  </div>
                </div>
             )}

             {/* Mini Data Bank & Evasion Audit */}
             {(['phase2', 'phase3', 'phase4', 'phase5'].includes(activeTab)) && (() => {
                const queue = phaseQueues[activeTab] || [];
                let hydratingFields: string[] = [];
                if (activeTab === 'phase2') hydratingFields = ['Website', 'Phone Number'];
                if (activeTab === 'phase3') hydratingFields = ['Pricing', 'Adult Night', 'Hours', '📸 Photo Candidates'];
                if (activeTab === 'phase4') hydratingFields = ['OG Photo', 'DOM Images', 'Street View', 'Facebook OG'];
                if (activeTab === 'phase5') hydratingFields = ['Media URLs', 'Thumbnails'];

                const activeLabel = `Phase ${activeTab.replace('phase','')}`;
                const activeColor = PIPELINE_PHASES.find(p=>p.route===activeTab)?.color || '#fff';

                return (
                  <div style={{marginTop: '20px'}}>
                     <div className="evasion-audit-card" style={{ marginTop: '2rem', padding: '1.5rem', background: 'rgba(0,0,0,0.3)', borderRadius: '12px', border: `1px solid ${activeColor}44` }}>
                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
                           <h3 style={{ margin: 0, color: activeColor, fontSize: '0.9rem', textTransform: 'uppercase' }}>🛡️ {activeLabel} Evasion Audit</h3>
                           <div className="pulse-badge" style={{fontSize: '0.7rem', color: 'rgba(255,255,255,0.5)'}}>LAST: {status?.pulseRegistry?.[activeLabel]?.lastRunAt ? new Date(status.pulseRegistry[activeLabel].lastRunAt).toLocaleTimeString() : 'NEVER'}</div>
                        </div>
                        <div style={{ display: 'grid', gridTemplateColumns: 'minmax(0, 1.5fr) 1fr', gap: '1.5rem', alignItems: 'center' }}>
                           <div className="audit-details">
                              <div className="audit-stat" style={{marginBottom: '0.8rem'}}>
                                 <label style={{display:'block', fontSize:'0.6rem', color:'rgba(255,255,255,0.4)', marginBottom:'4px'}}>BROWSER FINGERPRINT</label>
                                 <div className="audit-val" style={{ fontSize: '0.75rem', opacity: 0.8, wordBreak: 'break-all' }}>
                                    {status?.pulseRegistry?.[activeLabel]?.ghost?.userAgent || 'STABILIZING IDENTITY...'}
                                 </div>
                              </div>
                              <div className="audit-stat">
                                 <label style={{display:'block', fontSize:'0.6rem', color:'rgba(255,255,255,0.4)', marginBottom:'4px'}}>RANDOMIZED VIEWPORT</label>
                                 <div className="audit-val" style={{ color: activeColor, fontWeight: 700 }}>
                                    {status?.pulseRegistry?.[activeLabel]?.ghost?.viewport ? `${status.pulseRegistry[activeLabel].ghost.viewport.width}x${status.pulseRegistry[activeLabel].ghost.viewport.height}` : 'SCALING...'}
                                 </div>
                              </div>
                           </div>
                           <div className="audit-countdown" style={{ display: 'flex', justifyContent: 'center', background: 'rgba(255,255,255,0.05)', padding: '10px', borderRadius: '50%', width: '60px', height: '60px', alignItems: 'center', border: `1px solid ${activeColor}44` }}>
                              <PulseTimer nextRunAt={status?.pulseRegistry?.[activeLabel]?.nextRunAt} />
                           </div>
                        </div>
                      </div>

                     <h4 style={{fontSize: '0.8rem', textTransform:'uppercase', color:'var(--text-secondary)', marginBottom: 0, marginTop: '2rem'}}>Processing Queue (Top 10)</h4>
                     {queue.length === 0 ? (
                        <div style={{ background: 'rgba(255,255,255,0.02)', padding: '2rem', textAlign: 'center', borderRadius: '8px', color: 'var(--text-secondary)', fontSize: '0.9rem', fontStyle: 'italic', marginTop: '10px' }}>
                            Queue is empty. Awaiting spots from the previous phase.
                        </div>
                     ) : (
                        <div className="mini-data-bank" style={{ marginTop: '10px' }}>
                          {queue.map(spot => (
                            <div key={spot.id} className="queue-card active">
                              <div className="queue-card-title">{spot.name}</div>
                              <div className="queue-card-loc">{spot.city}, {spot.state}</div>
                              <div className="queue-tags">
                                {hydratingFields.map(f => <span key={f} className="queue-badge" style={{color: activeColor}}>⏳ {f}</span>)}
                              </div>
                            </div>
                          ))}
                        </div>
                     )}
                  </div>
                );
             })()}
          </div>
        )}

        {/* =========== PHASE 6: DATABANK QA =========== */}
        {activeTab === 'phase6' && (
          <div className="tab-pane graveyard fade-in">
            <div className="explainer-block" style={{marginBottom: '1rem', background: 'rgba(76, 175, 80, 0.05)', border: '1px solid rgba(76, 175, 80, 0.2)'}}>
              <h3 style={{marginTop: 0, color: '#4caf50'}}>Phase 6: Databank QA & Live Sync</h3>
              <p>This is the final human-in-the-loop verification protocol. Filter by pipeline status down below to intercept spot identities parsed by the Operator/Detective heuristics.</p>
              
              <div style={{ display: 'grid', gridTemplateColumns: 'minmax(0, 1fr) 2fr', gap: '10px', marginTop: '1rem', background: '#000', padding: '15px', borderRadius: '8px' }}>
                 <div style={{marginBottom: '10px'}}>
                    <span className="status-pill pending" style={{marginBottom: 5, display: 'inline-block'}}>PENDING (Phase 1/2)</span><br/>
                    <span style={{fontSize: '0.8rem', color:'var(--text-secondary)'}}>Raw OpenStreetMap (OSM) extractions. Needs human or daemon review to find website/phone.</span>
                 </div>
                 <div style={{marginBottom: '10px'}}>
                    <span className="status-pill enriched" style={{marginBottom: 5, display: 'inline-block', border: '1px solid #ff5a00', background:'rgba(255,90,0,0.1)'}}>ENRICHED (Google Premium)</span><br/>
                    <span style={{fontSize: '0.8rem', color:'var(--text-secondary)'}}>Scraped directly via Google Places. Assumed high fidelity (Hours, Coordinates). "Gold Standard".</span>
                 </div>
                 <div>
                    <span className="status-pill verified" style={{marginBottom: 5, display: 'inline-block'}}>VERIFIED</span><br/>
                    <span style={{fontSize: '0.8rem', color:'var(--text-secondary)'}}>Manually inspected by a human admin and confirmed 100% accurate.</span>
                 </div>
                 <div>
                    <span style={{fontSize: '0.85rem', fontWeight: 800, color:'#4caf50', border: '1px solid #4caf50', padding: '4px 8px', borderRadius: '4px', display:'inline-block', marginBottom: 5}}>LIVE APP TOGGLE</span><br/>
                    <span style={{fontSize: '0.8rem', color:'var(--text-secondary)'}}>Toggling the <q>LIVE APP</q> Checkbox instantly pushes the record to the public-facing SK8Lytz iOS/Android app map!</span>
                 </div>
              </div>
            </div>

            {/* =========== STATUS COVERAGE MAP =========== */}
            <div style={{ marginBottom: '2rem', padding: '1.5rem', background: 'rgba(255,179,0,0.03)', border: '1px solid rgba(255,179,0,0.2)', borderRadius: '12px' }}>
              <h3 style={{ margin: '0 0 0.5rem', color: '#ffb300', fontSize: '0.95rem', textTransform: 'uppercase' }}>📡 Pipeline Coverage Map</h3>
              <p style={{ margin: '0 0 1.5rem', color: 'rgba(255,255,255,0.5)', fontSize: '0.8rem' }}>Spot status distribution by state. Click a state to filter the grid below. Color shows the dominant verification status in that state.</p>
              
              {/* Nationwide status totals */}
              {(() => {
                const totals: Record<string, number> = {};
                databankCoverage.forEach((row: any) => {
                  ['PENDING','IDENTITY_ESTABLISHED','INDEXED','ENRICHED','MEDIA_READY','VERIFIED'].forEach(s => {
                    if (row[s]) totals[s] = (totals[s] || 0) + row[s];
                  });
                });
                const STATUS_META: Record<string, {color: string; label: string}> = {
                  PENDING:              { color: '#8a2be2', label: 'Pending' },
                  IDENTITY_ESTABLISHED: { color: '#5d78ff', label: 'Identified' },
                  INDEXED:              { color: '#ff5a00', label: 'Indexed' },
                  ENRICHED:             { color: '#ff9800', label: 'Enriched' },
                  MEDIA_READY:          { color: '#e91e63', label: 'Media Ready' },
                  VERIFIED:             { color: '#4caf50', label: 'Verified' },
                };
                return (
                  <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap', marginBottom: '1.5rem' }}>
                    {Object.entries(STATUS_META).map(([s, meta]) => (
                      <div key={s} style={{ display: 'flex', alignItems: 'center', gap: '6px', background: 'rgba(255,255,255,0.04)', padding: '6px 12px', borderRadius: '20px', border: `1px solid ${meta.color}44` }}>
                        <span style={{ width: '10px', height: '10px', borderRadius: '2px', background: meta.color, display: 'inline-block' }}></span>
                        <span style={{ fontSize: '0.75rem', color: meta.color, fontWeight: 700 }}>{meta.label}</span>
                        <span style={{ fontSize: '0.75rem', color: 'rgba(255,255,255,0.5)' }}>{(totals[s] || 0).toLocaleString()}</span>
                      </div>
                    ))}
                  </div>
                );
              })()}

              <div style={{ display: 'flex', justifyContent: 'center', width: '100%', maxWidth: '800px', margin: '0 auto' }}>
                {/* @ts-ignore */}
                <USAMap
                  defaultFill="rgba(255,255,255,0.05)"
                  customize={(() => {
                    const colors: Record<string, any> = {};
                    const STATUS_PRIORITY = ['VERIFIED','MEDIA_READY','ENRICHED','INDEXED','IDENTITY_ESTABLISHED','PENDING'];
                    const STATUS_COLOR: Record<string, string> = {
                      VERIFIED: '#4caf50',
                      MEDIA_READY: '#e91e63',
                      ENRICHED: '#ff9800',
                      INDEXED: '#ff5a00',
                      IDENTITY_ESTABLISHED: '#5d78ff',
                      PENDING: '#8a2be2',
                    };
                    databankCoverage.forEach((row: any) => {
                      if (!row.state || row.state === 'UNKNOWN') return;
                      const total = row.total || 0;
                      if (total === 0) return;
                      // Dominant status = highest-trust status with >0 records
                      const dominant = STATUS_PRIORITY.find(s => (row[s] || 0) > 0) || 'PENDING';
                      const baseColor = STATUS_COLOR[dominant] || '#8a2be2';
                      const density = Math.min(total / 40, 1);
                      const opacity = Math.max(density, 0.35);
                      colors[row.state] = {
                        fill: baseColor + Math.round(opacity * 255).toString(16).padStart(2, '0'),
                        customText: total.toString()
                      };
                    });
                    return colors;
                  })()}
                  onClick={(e: any) => {
                    const st = e.target?.dataset?.name || e.target?.id;
                    if (st) setSearchQuery(st);
                  }}
                />
              </div>
            </div>

            <div className="grid-toolbar">
              <input className="form-input search-bar" placeholder="Search Location / City / State..." value={searchQuery} onChange={e => setSearchQuery(e.target.value)} />
               <select className="form-input filter-dropdown" value={gridFilter} onChange={e => setGridFilter(e.target.value)}>
                  <option value="ALL">All Grid Records</option>
                  <option value="PENDING">Phase 1 Pending (Queue)</option>
                  <option value="IDENTITY_ESTABLISHED">Phase 2 Identified</option>
                  <option value="INDEXED">Phase 3 Web-Crawled</option>
                  <option value="ENRICHED">Phase 4 Golden Seed (Google Premium)</option>
                  <option value="MEDIA_READY">Phase 5 Media Prepped</option>
                  <option value="VERIFIED">Phase 6 Verified (Gold Standard)</option>
                  <option value="REJECTED">Graveyard / Rejected</option>
               </select>
              <button className="btn-primary" onClick={bulkPromote}>🚀 Bulk App Promote</button>
              <div className="pagination">
                <button disabled={page === 0} onClick={() => fetchSpots(page - 1, gridFilter)}>Prev</button>
                <span className="page-indicator">Page {page + 1} of {Math.ceil(totalSpots/rowsPerPage) || 1} ({totalSpots} total)</span>
                <button disabled={(page+1)*rowsPerPage >= totalSpots} onClick={() => fetchSpots(page + 1, gridFilter)}>Next</button>
              </div>
            </div>

            <div className="table-container">
              <table className="databank-table">
                <thead>
                  <tr>
                    <th onClick={() => toggleSort('name')} style={{cursor:'pointer'}}>Location {sortCol==='name' ? (sortDir==='asc'?'↑':'↓') : ''}</th>
                    <th onClick={() => toggleSort('street_address')} style={{cursor:'pointer'}}>Address {sortCol==='street_address' ? (sortDir==='asc'?'↑':'↓') : ''}</th>
                    <th onClick={() => toggleSort('verification_status')} style={{cursor:'pointer'}}>Current Phase {sortCol==='verification_status' ? (sortDir==='asc'?'↑':'↓') : ''}</th>
                    <th onClick={() => toggleSort('rating')} style={{cursor:'pointer'}}>Rating {sortCol==='rating' ? (sortDir==='asc'?'▲':'▼') : ''}</th>
                    <th>Surface</th>
                    <th onClick={() => toggleSort('website')} style={{cursor:'pointer'}}>Website {sortCol==='website' ? (sortDir==='asc'?'↑':'↓') : ''}</th>
                    <th onClick={() => toggleSort('phone_number')} style={{cursor:'pointer'}}>Phone {sortCol==='phone_number' ? (sortDir==='asc'?'↑':'↓') : ''}</th>
                    <th onClick={() => toggleSort('has_adult_night')} style={{cursor:'pointer'}}>18+ {sortCol==='has_adult_night' ? (sortDir==='asc'?'↑':'↓') : ''}</th>
                    <th onClick={() => toggleSort('retry_count')} style={{cursor:'pointer'}}>Retries {sortCol==='retry_count' ? (sortDir==='asc'?'↑':'↓') : ''}</th>
                    <th onClick={() => toggleSort('last_attempted_at')} style={{cursor:'pointer'}}>Last Ping {sortCol==='last_attempted_at' ? (sortDir==='asc'?'↑':'↓') : ''}</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {spots.map(row => {
                    const isEditing = editingId === row.id;
                    return (
                      <tr key={row.id}>
                        <td>
                          {isEditing ? (
                            <>
                              <input className="table-input" value={editForm.name} onChange={e => setEditForm({...editForm, name: e.target.value})} placeholder="Name" />
                              <input className="table-input" value={editForm.city} onChange={e => setEditForm({...editForm, city: e.target.value})} placeholder="City" />
                            </>
                          ) : (
                            <>
                              <div style={{fontWeight: 'bold', color: 'var(--text-primary)'}}>{row.name}</div>
                              <div style={{fontSize: '0.8rem', color: 'var(--text-secondary)'}}>{row.city}, {row.state}</div>
                            </>
                          )}
                        </td>
                        <td>
                          {isEditing ? <input className="table-input" value={editForm.street_address || ''} onChange={e => setEditForm({...editForm, street_address: e.target.value})} placeholder="Street Address" /> : <div style={{fontSize: '0.85rem', color: 'var(--text-secondary)'}}>{row.street_address || '-'}</div>}
                        </td>
                        <td className="status-cell">
                           <select 
                             className={`table-input status-pill ${row.verification_status?.toLowerCase() || 'pending'}`} 
                             value={isEditing ? (editForm.verification_status || 'PENDING') : (row.verification_status || 'PENDING')} 
                             onChange={e => {
                                if (isEditing) setEditForm({...editForm, verification_status: e.target.value});
                                else updateSpotStatus(row.id, e.target.value);
                             }}
                             style={{ padding: '6px 8px', borderRadius: '6px', cursor: 'pointer', appearance: 'menulist' }}
                           >
                              <option value="PENDING">🕒 PENDING (PH_1)</option>
                              <option value="IDENTITY_ESTABLISHED">🕵️ IDENTIFIED (PH_2)</option>
                              <option value="INDEXED">🕸️ INDEXED (PH_3)</option>
                              <option value="ENRICHED">✨ ENRICHED (Gold Standard)</option>
                              <option value="MEDIA_READY">📸 MEDIA_READY (PH_5)</option>
                              <option value="VERIFIED">✅ VERIFIED (Gold Standard)</option>
                              <option value="DEPRECATED">⚰️ Deprecated</option>
                              <option value="REJECTED">🚫 Graveyard</option>
                           </select>
                        </td>
                        <td>
                           {row.rating ? <span style={{color: '#ffd700', fontWeight:'bold', textShadow: '0 0 5px rgba(255,215,0,0.5)'}}>{row.rating}★ <span style={{fontSize: '0.7em', color: 'gray'}}>({row.user_ratings_total || 0})</span></span> : <span style={{color:'gray'}}>-</span>}
                        </td>
                        <td>
                           {isEditing ? (
                             <input className="table-input" value={editForm.surface_quality || ''} onChange={e => setEditForm({...editForm, surface_quality: e.target.value})} placeholder="Buttery..." />
                           ) : (
                             <span className={`surface-tag ${row.surface_quality?.toLowerCase()}`}>{row.surface_quality || '-'}</span>
                           )}
                        </td>
                        <td>
                          {isEditing ? <input className="table-input" value={editForm.website || ''} onChange={e => setEditForm({...editForm, website: e.target.value})} /> : (
                            row.website ? <a href={row.website} target="_blank" rel="noreferrer" style={{color: 'var(--success)', fontWeight: 600}}>Visit ↗</a> : '-'
                          )}
                        </td>
                        <td>
                          {isEditing ? <input className="table-input" value={editForm.phone_number || ''} onChange={e => setEditForm({...editForm, phone_number: e.target.value})} /> : (
                            row.phone_number || '-'
                          )}
                        </td>
                        <td>
                          {isEditing ? <input type="checkbox" checked={editForm.has_adult_night} onChange={e => setEditForm({...editForm, has_adult_night: e.target.checked})} /> : (
                            <div style={{display:'flex', alignItems: 'center', gap: '5px', justifyContent: 'center'}}>
                               {row.has_adult_night ? '✅' : '❌'}
                               {row.adult_night_details && <span title={row.adult_night_details} style={{cursor: 'help'}}>ℹ️</span>}
                            </div>
                          )}
                        </td>
                        <td>
                          {isEditing ? (
                            <input type="number" className="table-input" value={editForm.retry_count || 0} onChange={e => setEditForm({...editForm, retry_count: parseInt(e.target.value) || 0})} />
                          ) : (
                            <span style={{ color: row.retry_count >= 8 ? 'var(--danger)' : 'var(--text-secondary)' }}>{row.retry_count || 0}/10</span>
                          )}
                        </td>
                        <td style={{fontSize: '0.75rem', color: 'var(--text-secondary)'}}>
                          {row.last_attempted_at ? new Date(row.last_attempted_at).toLocaleString() : 'Never'}
                        </td>
                        <td>
                          <div className="action-row" style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
                            <label className="checkbox-row" style={{ display: 'flex', alignItems: 'center', gap: '4px', cursor: 'pointer', background: row.is_published ? 'rgba(76, 175, 80, 0.1)' : 'rgba(255,255,255,0.05)', padding: '5px 8px', borderRadius: '4px', border: row.is_published ? '1px solid #4caf50' : '1px solid transparent' }}>
                               <input type="checkbox" checked={row.is_published} onChange={e => promoteSpot(row.id, e.target.checked)} />
                               <span style={{ fontSize: '0.7rem', fontWeight: 800, color: row.is_published ? '#4caf50' : 'var(--text-secondary)', userSelect:'none' }}>APP_LIVE</span>
                            </label>
                            {isEditing ? (
                              <button className="btn-icon btn-save-inline" onClick={saveEdit}>💾</button>
                            ) : (
                              <button className="btn-icon" onClick={() => startEdit(row)}>✏️</button>
                            )}
                            <button className="btn-icon btn-delete" onClick={() => deleteSpot(row.id, row.name)}>🗑️</button>
                          </div>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        )}
      </div>

      <div className="log-panel panel">
        <div className="log-header">
           <h2 className="panel-header" style={{margin:0}}>
             {activeTab === 'phase1' ? 'Phase 1: Seed Engine Logs' :
              activeTab === 'phase2' ? 'Phase 2: Operator Logs' :
              activeTab === 'phase3' ? 'Phase 3: Indexer Logs' :
              'Omni-Terminal (Restricted Access)'}
           </h2>
           <button className="btn-mini" onClick={fetchHistory}>PERSISTENT HISTORY</button>
        </div>
        <div className="log-container" ref={logsRef}>
          {logs
             .filter(log => {
                if (activeTab === 'phase1') return log.source === 'Phase 1' || log.source === 'System';
                if (activeTab === 'phase2') return log.source === 'Phase 2' || log.source === 'System';
                if (activeTab === 'phase3') return log.source === 'Phase 3' || log.source === 'System';
                if (activeTab === 'phase4') return log.source === 'Photographer' || log.source === 'System';
                return true; 
             })
             .map((log, i) => (
               <div key={i} className={`log-entry ${log.type === 'ERROR' ? 'error' : ''}`}>
                 <span className="log-timestamp">[{new Date().toLocaleTimeString()}]</span>
                 <span style={{ color: 'var(--primary-color)', fontWeight: 'bold', marginRight: '8px' }}>
                    [{log.source || 'UNKNOWN'}]
                 </span> 
                 {log.message}
               </div>
          ))}
          {logs.filter(log => {
             if (activeTab === 'phase1') return log.source === 'Phase 1' || log.source === 'System';
             if (activeTab === 'phase2') return log.source === 'Phase 2' || log.source === 'System';
             if (activeTab === 'phase3') return log.source === 'Phase 3' || log.source === 'System';
             if (activeTab === 'phase4') return log.source === 'Photographer' || log.source === 'System';
             return true;
          }).length === 0 && (
             <div style={{ color: 'var(--text-secondary)', fontStyle: 'italic', padding: '1rem' }}>No active telemetry signals detected for this phase.</div>
          )}
        </div>
        
        {historyLogs.length > 0 && (
          <div className="history-preview">
             <h4 className="panel-sub-header">Recent History Snippet</h4>
             <div className="log-container mini">
                {historyLogs.slice(-10).map((line, i) => <div key={i} className="log-entry history">{line}</div>)}
             </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
