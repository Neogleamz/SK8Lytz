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

  // --- Harvest Manager States ---
  const [harvestData, setHarvestData] = useState<{seededStates: string[], stateCounts: Record<string, number>, allStates: string[]}>({ seededStates: [], stateCounts: {}, allStates: [] });
  const [coverageStats, setCoverageStats] = useState<any[]>([]);
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
  const rowsPerPage = 50;

  useEffect(() => {
    fetchSystemStatus();
    fetchQueue();
    fetchHarvestStatus();
    fetchHistory();
    fetchCoverage();
    
    const interval = setInterval(() => {
      fetchSystemStatus();
      fetchQueue();
      fetchCoverage(); 
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

  useEffect(() => {
    if (logsRef.current) {
      logsRef.current.scrollTop = logsRef.current.scrollHeight;
    }
  }, [logs]);

  useEffect(() => {
    if (activeTab === 'phase6') {
      fetchSpots(0, gridFilter, sortCol, sortDir, searchQuery);
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

  const fetchQueue = async () => {
    try {
      const phasesToFetch = ['phase1', 'phase2', 'phase3', 'phase4', 'phase5'];
      const results = await Promise.all(
         phasesToFetch.map(phase => fetch(`${API_BASE}/api/queue?phase=${phase}`).then(r => r.json()))
      );
      
      const newQueues: Record<string, any[]> = {};
      phasesToFetch.forEach((phase, idx) => {
         newQueues[phase] = results[idx]?.spots || [];
      });
      setPhaseQueues(newQueues);
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
    try {
       await fetch(`${API_BASE}/api/harvest/${type}`, {
         method: 'POST',
         headers: { 'Content-Type': 'application/json' },
         body: JSON.stringify({ target_facilities: targetFacilities, target_states: states })
       });
       fetchSystemStatus();
    } catch (e) {
       alert('Harvest failed to start.');
    }
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
    { id: '1', title: 'The Scout', sub: 'GIS Ingestion / OSM', route: 'phase1', color: '#8a2be2', target: 'Nationwide -> PENDING', 
      metric: status?.processedCount || harvestData.seededStates?.length || 0, metricLabel: 'Extracted Nodes', isDaemon: false, 
      statusActive: status?.isHarvestingActive },
    { id: '2', title: 'The Operator', sub: 'Identity & Contact', route: 'phase2', color: '#5d78ff', target: 'PENDING -> IDENTIFIED', 
      metric: status?.identityCount || 0, metricLabel: 'Identities Found', isDaemon: true, 
      statusActive: status?.currentTarget?.includes('Operator: online') },
    { id: '3', title: 'The Detective', sub: 'Hours & Pricing Engine', route: 'phase3', color: '#ff5a00', target: 'IDENTIFIED -> INDEXED', 
      metric: status?.indexedCount || 0, metricLabel: 'Websites Indexed', isDaemon: true, 
      statusActive: status?.currentTarget?.includes('Indexer: online') },
    { id: '4', title: 'Specialist Daemons', sub: 'Yelp & Social APIs', route: 'phase4', color: '#ffb300', target: 'INDEXED -> ENRICHED', 
      metric: status?.enrichedCount || 0, metricLabel: 'Enriched Spots', isDaemon: true, 
      statusActive: false },
    { id: '5', title: 'The Photographer', sub: 'Media Storage Pipeline', route: 'phase5', color: '#e91e63', target: 'ENRICHED -> MEDIA_READY', 
      metric: status?.mediaReadyCount || 0, metricLabel: 'Galleries Built', isDaemon: true, 
      statusActive: false },
    { id: '6', title: 'Databank QA', sub: 'Master Review Grid', route: 'phase6', color: '#4caf50', target: 'ALL -> VERIFIED', 
      metric: status?.verifiedCount || 0, metricLabel: 'Gold Standard', isDaemon: false, 
      statusActive: true }
  ];

  return (
    <div className="dashboard-container">
      <header className="header" style={{ alignItems: 'center', flexWrap: 'nowrap', display: 'flex', justifyContent: 'space-between' }}>
        <div>
          <h1 className="title">SK8Lytz Global Extraction Pipeline</h1>
          <p style={{marginTop: 0, color: 'var(--text-secondary)', fontSize: '0.9rem'}}>Decoupled 6-Phase Micro-Scraper Architecture</p>
        </div>
        <div className="daemon-status-header" style={{ display: 'flex', gap: '8px', flexWrap: 'wrap', justifyContent: 'flex-end' }}>
           <div style={{ background: 'rgba(255,255,255,0.05)', padding: '6px 12px', borderRadius: '20px', fontSize: '0.75rem', display: 'flex', alignItems: 'center', gap: '6px', border: `1px solid ${status?.currentTarget?.includes('Operator: online') ? '#4caf50' : 'rgba(255,255,255,0.1)'}` }}>
              <div className={`status-dot ${status?.currentTarget?.includes('Operator: online') ? 'online' : 'offline'}`}></div>
              <span style={{color: 'var(--text-secondary)'}}>Operator:</span> <strong style={{color: status?.currentTarget?.includes('Operator: online') ? '#4caf50' : 'var(--text-secondary)'}}>{status?.currentTarget?.includes('Operator: online') ? 'ONLINE' : 'OFFLINE'}</strong>
           </div>
           <div style={{ background: 'rgba(255,255,255,0.05)', padding: '6px 12px', borderRadius: '20px', fontSize: '0.75rem', display: 'flex', alignItems: 'center', gap: '6px', border: `1px solid ${status?.currentTarget?.includes('Indexer: online') ? '#ff5a00' : 'rgba(255,255,255,0.1)'}` }}>
              <div className={`status-dot ${status?.currentTarget?.includes('Indexer: online') ? 'online' : 'offline'}`} style={{ background: status?.currentTarget?.includes('Indexer: online') ? '#ff5a00' : '', boxShadow: status?.currentTarget?.includes('Indexer: online') ? '0 0 8px #ff5a00' : '' }}></div>
              <span style={{color: 'var(--text-secondary)'}}>Indexer:</span> <strong style={{color: status?.currentTarget?.includes('Indexer: online') ? '#ff5a00' : 'var(--text-secondary)'}}>{status?.currentTarget?.includes('Indexer: online') ? 'ONLINE' : 'OFFLINE'}</strong>
           </div>
           <div style={{ background: 'rgba(255,255,255,0.05)', padding: '6px 12px', borderRadius: '20px', fontSize: '0.75rem', display: 'flex', alignItems: 'center', gap: '6px', opacity: 0.5 }}>
              <div className="status-dot offline"></div>
              <span style={{color: 'var(--text-secondary)'}}>Specialists:</span> <strong>MOCKED</strong>
           </div>
           <div style={{ background: 'rgba(255,255,255,0.05)', padding: '6px 12px', borderRadius: '20px', fontSize: '0.75rem', display: 'flex', alignItems: 'center', gap: '6px', opacity: 0.5 }}>
              <div className="status-dot offline"></div>
              <span style={{color: 'var(--text-secondary)'}}>Photographer:</span> <strong>MOCKED</strong>
           </div>
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
              <h3 style={{marginTop: 0, color: '#8a2be2'}}>The Scout: Polygon Infrastructure</h3>
              <p>This engine interfaces directly with OpenStreetMap's Overpass API. It extracts raw GIS locations (longitude/latitude) and establishes baseline row injection into Supabase. Real data validation occurs downstream.</p>
            </div>

            <div className="flow-visualizer" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '2rem', padding: '3rem 2rem', background: 'rgba(0,0,0,0.2)', borderRadius: '12px', marginTop: '1rem', marginBottom: '2rem' }}>
               <div style={{ textAlign: 'center', minWidth: '100px' }}>
                  <div style={{ fontSize: '2.5rem', fontWeight: 800, color: '#8a2be2' }}>∞</div>
                  <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', textTransform: 'uppercase' }}>OSM Dataset</div>
               </div>
               <div style={{ flex: 1, height: '4px', background: 'rgba(255,255,255,0.1)', position: 'relative' }}>
                  <div style={{ position: 'absolute', top: '-40px', left: '50%', transform: 'translateX(-50%)', display: 'flex', gap: '10px' }}>
                     <button className="btn-mini start" onClick={() => triggerHarvest('start-all', stateOverride)} disabled={status?.isHarvestingActive}>
                        ▶ {stateOverride.length > 0 ? `SEED ${stateOverride.join(', ')}` : 'GLOBAL SEED'}
                     </button>
                     <button className="btn-mini stop" onClick={() => triggerHarvest('stop-all')} disabled={!status?.isHarvestingActive}>■ STOP</button>
                  </div>
                  {status?.isHarvestingActive && <div className="flow-animation"></div>}
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
                     <h4 style={{ margin: '0 0 10px 0', fontSize: '0.8rem', color: '#ffb300' }}>Direct Google Discovery</h4>
                     <p style={{ fontSize: '0.7rem', color: 'rgba(255,255,255,0.4)', marginBottom: '1rem' }}>Trigger the Google Maps "Phase 0" crawler. Scrapes business listings directly from Google for maximum fidelity.</p>
                     <button className="btn-mini" style={{ width: '100%', borderColor: '#ffb300', color: '#ffb300' }} onClick={() => triggerDiscovery('Missouri')}>LAUNCH STALKER</button>
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
                            {st} {count > 0 && <span className="pill-dot" style={{display:'inline-block', width:'6px', height:'6px', background:'var(--success)', borderRadius:'50%', marginLeft:'4px'}}></span>}
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
                          let color = '#8a2be2'; 
                          const enrichedRatio = stat.enriched / stat.total;
                          if (enrichedRatio > 0.5) color = '#ff5a00';
                          const verifiedRatio = stat.verified / stat.total;
                          if (verifiedRatio > 0.5) color = '#4caf50';
                          colors[stat.state] = { fill: color };
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
                <h4 style={{fontSize: '0.8rem', textTransform:'uppercase', color:'var(--text-secondary)', marginBottom: 0}}>Newly Spawned Targets (Unprocessed)</h4>
                {(phaseQueues['phase1'] || []).length === 0 ? (
                    <div style={{ background: 'rgba(255,255,255,0.02)', padding: '2rem', textAlign: 'center', borderRadius: '8px', color: 'var(--text-secondary)', fontSize: '0.9rem', fontStyle: 'italic', marginTop: '10px' }}>
                        Vault is empty. Click [START NATIONAL HARVEST] to spawn targets from OSM.
                    </div>
                ) : (
                    <div className="mini-data-bank" style={{marginTop: '10px'}}>
                      {(phaseQueues['phase1'] || []).map(spot => (
                        <div key={spot.id} className="queue-card active">
                          <div className="queue-card-title">{spot.name}</div>
                          <div className="queue-card-loc">{spot.city}, {spot.state}</div>
                          <div className="queue-tags">
                            <span className="queue-badge">⏳ RAW SEED</span>
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
               {activeTab === 'phase2' && <p>Combats Google's Captcha to establish physical existence via search queries. Injects legitimate websites and phone numbers, resolving PENDING data debris.</p>}
               {activeTab === 'phase3' && <p>Autonomous Heuristic Search algorithm. Bypasses Google entirely; crawls targeted websites searching context strings for 18+ Adult Nights, Operating Hours, and Schedule structures to extract real Roller Rink economy data.</p>}
               {activeTab === 'phase4' && <p>Cluster of specialized scrapers (Instagram, Yelp API). Fetches live Vibe Ratings, user engagement reviews, and secondary source verifications.</p>}
               {activeTab === 'phase5' && <p>A high-throughput media engine mapping Google Place photos and social gallery artifacts directly into our automated WebP CDN to power client-side mobile rendering.</p>}
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
                <div className="flow-visualizer" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '2rem', padding: '3rem 2rem', background: 'rgba(0,0,0,0.2)', borderRadius: '12px', marginTop: '1rem' }}>
                   <div style={{ textAlign: 'center', minWidth: '100px' }}>
                      <div style={{ fontSize: '2.5rem', fontWeight: 800, color: '#5d78ff' }}>{status?.identityCount || 0}</div>
                      <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>IDENTIFIED</div>
                   </div>
                   <div style={{ flex: 1, height: '4px', background: 'rgba(255,255,255,0.1)', position: 'relative' }}>
                      <div style={{ position: 'absolute', top: '-40px', left: '50%', transform: 'translateX(-50%)', display: 'flex', gap: '10px' }}>
                         <button className="btn-mini start" onClick={() => triggerSpecificDaemon('indexer', 'start')} disabled={status?.currentTarget?.includes('Indexer: online')}>▶ START DETECTIVE</button>
                         <button className="btn-mini stop" onClick={() => triggerSpecificDaemon('indexer', 'stop')} disabled={!status?.currentTarget?.includes('Indexer: online')}>■ STOP</button>
                      </div>
                      <div style={{ position: 'absolute', top: '15px', left: '50%', transform: 'translateX(-50%)', background: 'rgba(255,255,255,0.05)', padding: '2px 8px', borderRadius: '4px', fontSize: '11px', whiteSpace: 'nowrap', color: 'var(--text-secondary)' }}>
                         {status?.indexedCount || 0} / {(status?.identityCount || 0) + (status?.indexedCount || 0)} Completed
                      </div>
                      {status?.currentTarget?.includes('Indexer: online') && <div className="flow-animation"></div>}
                   </div>
                   <div style={{ textAlign: 'center', minWidth: '100px' }}>
                      <div style={{ fontSize: '2.5rem', fontWeight: 800, color: '#ff5a00' }}>{status?.indexedCount || 0}</div>
                      <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>INDEXED</div>
                   </div>
                </div>
             )}

             {activeTab === 'phase4' && (
                <div className="flow-visualizer" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '2rem', padding: '3rem 2rem', background: 'rgba(0,0,0,0.2)', borderRadius: '12px', marginTop: '1rem', opacity: 0.5 }}>
                   <div style={{ textAlign: 'center', minWidth: '100px' }}>
                      <div style={{ fontSize: '2.5rem', fontWeight: 800, color: '#ff5a00' }}>{status?.indexedCount || 0}</div>
                      <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>INDEXED</div>
                   </div>
                   <div style={{ flex: 1, height: '4px', background: 'rgba(255,255,255,0.1)', position: 'relative' }}>
                      <div style={{ position: 'absolute', top: '-40px', left: '50%', transform: 'translateX(-50%)', display: 'flex', gap: '10px' }}>
                         <button className="btn-mini stop" disabled>MOCKED</button>
                      </div>
                      <div style={{ position: 'absolute', top: '15px', left: '50%', transform: 'translateX(-50%)', background: 'rgba(255,255,255,0.05)', padding: '2px 8px', borderRadius: '4px', fontSize: '11px', whiteSpace: 'nowrap', color: 'var(--text-secondary)' }}>
                         {status?.enrichedCount || 0} / {(status?.indexedCount || 0) + (status?.enrichedCount || 0)} Completed
                      </div>
                   </div>
                   <div style={{ textAlign: 'center', minWidth: '100px' }}>
                      <div style={{ fontSize: '2.5rem', fontWeight: 800, color: '#ffb300' }}>{status?.enrichedCount || 0}</div>
                      <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>ENRICHED</div>
                   </div>
                </div>
             )}

             {activeTab === 'phase5' && (
                <div className="flow-visualizer" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '2rem', padding: '3rem 2rem', background: 'rgba(0,0,0,0.2)', borderRadius: '12px', marginTop: '1rem', opacity: 0.5 }}>
                   <div style={{ textAlign: 'center', minWidth: '100px' }}>
                      <div style={{ fontSize: '2.5rem', fontWeight: 800, color: '#ffb300' }}>{status?.enrichedCount || 0}</div>
                      <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>ENRICHED</div>
                   </div>
                   <div style={{ flex: 1, height: '4px', background: 'rgba(255,255,255,0.1)', position: 'relative' }}>
                      <div style={{ position: 'absolute', top: '-40px', left: '50%', transform: 'translateX(-50%)', display: 'flex', gap: '10px' }}>
                         <button className="btn-mini stop" disabled>MOCKED</button>
                      </div>
                   </div>
                   <div style={{ textAlign: 'center', minWidth: '100px' }}>
                      <div style={{ fontSize: '2.5rem', fontWeight: 800, color: '#e91e63' }}>{status?.mediaReadyCount || 0}</div>
                      <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>MEDIA_READY</div>
                   </div>
                </div>
             )}

             {/* Mini Data Bank & Evasion Audit */}
             {(() => {
                const queue = phaseQueues[activeTab] || [];
                let hydratingFields: string[] = [];
                if (activeTab === 'phase2') hydratingFields = ['Website', 'Phone Number'];
                if (activeTab === 'phase3') hydratingFields = ['Pricing', 'Adult Night', 'Hours'];
                if (activeTab === 'phase4') hydratingFields = ['Vibe Score', 'Description'];
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
            <div className="explainer-block" style={{marginBottom: '1rem'}}>
              <h3 style={{marginTop: 0, color: '#4caf50'}}>Phase 6: Databank QA Staging Grid</h3>
              <p>This is the final human-in-the-loop verification protocol. Filter by pipeline status down below to intercept spot identities parsed by the Operator/Detective heuristics. Utilize inline editing to clean Context Snippets into exact integers, delete garbage ghosts, or execute bulk promotions sending `VERIFIED` data directly into the live mobile application.</p>
            </div>

            <div className="grid-toolbar">
              <input className="form-input search-bar" placeholder="Search Location / City / State..." value={searchQuery} onChange={e => setSearchQuery(e.target.value)} />
               <select className="form-input filter-dropdown" value={gridFilter} onChange={e => setGridFilter(e.target.value)}>
                  <option value="ALL">All Grid Records</option>
                  <option value="PENDING">Phase 1 Pending (Queue)</option>
                  <option value="IDENTITY_ESTABLISHED">Phase 2 Identified</option>
                  <option value="INDEXED">Phase 3 Web-Crawled</option>
                  <option value="ENRICHED">Phase 4 Deep Enriched</option>
                  <option value="MEDIA_READY">Phase 5 Media Prepped</option>
                  <option value="VERIFIED">Phase 6 Verified & Published</option>
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
                    <th>Vibe</th>
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
                          {isEditing ? (
                            <select className="table-input" value={editForm.verification_status || 'PENDING'} onChange={e => setEditForm({...editForm, verification_status: e.target.value})}>
                              <option value="PENDING">PHASE 1 PENDING</option>
                              <option value="IDENTITY_ESTABLISHED">PHASE 2 IDENTIFIED</option>
                              <option value="INDEXED">PHASE 3 INDEXED</option>
                              <option value="ENRICHED">PHASE 4 ENRICHED</option>
                              <option value="MEDIA_READY">PHASE 5 MEDIA</option>
                              <option value="VERIFIED">PHASE 6 VERIFIED</option>
                              <option value="DEPRECATED">Tombstone (Deleted)</option>
                              <option value="REJECTED">Graveyard (Blacklist)</option>
                            </select>
                          ) : (
                            <span className={`status-pill ${row.verification_status?.toLowerCase() || 'pending'}`}>
                              {row.verification_status || 'PHASE_1_PENDING'}
                            </span>
                          )}
                        </td>
                        <td>
                           {row.vibe_score ? <span style={{color: 'var(--primary-color)', fontWeight:'bold'}}>{row.vibe_score}★</span> : '-'}
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
                        <td>{isEditing ? <input type="checkbox" checked={editForm.has_adult_night} onChange={e => setEditForm({...editForm, has_adult_night: e.target.checked})} /> : (row.has_adult_night ? '✅' : '❌')}</td>
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
                          <div className="action-row">
                            {row.is_published ? (
                               <button className="btn-icon" onClick={() => promoteSpot(row.id, false)} title="Unpublish from App">🛑</button>
                            ) : (
                               <button className="btn-icon" onClick={() => promoteSpot(row.id, true)} disabled={row.verification_status !== 'VERIFIED'} title="Promote to Live Database">🚀</button>
                            )}
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
