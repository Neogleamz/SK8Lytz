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
  const [seedProvider, setSeedProvider] = useState<'osm'|'google'>('google');


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

  // --- Databank Map Mode ---
  const [mapMode, setMapMode] = useState<'quality' | 'published'>('quality');
  const [activeStateFilter, setActiveStateFilter] = useState<string | null>(null);

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

  // --- Phase 6 View Mode ('card' | 'list' | 'table') ---
  const [viewMode, setViewMode] = useState<'card' | 'list' | 'table'>('table');

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
    if (activeTab === 'phase1') {
      fetchDatabankCoverage(); // Phase 1 map uses same source — Google record density per state
    }
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

  // --- Priority States: Global Active Region ---
  // All daemon phases (Ph2-Ph5) read this via /api/priority-states.
  // Empty array = nationwide (no filter).
  const setPriorityStates = async (newStates: string[]) => {
    setStateOverride(newStates);
    await fetch(`${API_BASE}/api/priority-states`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ states: newStates })
    }).catch(e => console.error('Priority state write failed:', e));
  };

  const togglePriorityState = async (st: string) => {
    const next = stateOverride.includes(st)
      ? stateOverride.filter(s => s !== st)
      : [...stateOverride, st];
    await setPriorityStates(next);
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
      fetchDatabankCoverage();
    } catch (e) {}
  };

  const bulkPromote = async () => {
    if (!confirm('Promote ALL ENRICHED and MEDIA_READY records to the public Skate Map?')) return;
    try {
      await fetch(`${API_BASE}/api/promote-all`, { method: 'POST' });
      alert('Bulk promotion complete!');
      fetchSpots(page, gridFilter);
      fetchDatabankCoverage();
    } catch (e) {}
  };

  const promoteState = async (state: string) => {
    if (!state || state.length !== 2) return;
    if (!confirm(`Publish ALL eligible records in ${state} to the live app map?`)) return;
    try {
      const res = await fetch(`${API_BASE}/api/promote-state/${state}`, { method: 'POST' });
      const data = await res.json();
      alert(`✅ Published ${data.promoted ?? 0} records in ${state}!`);
      fetchSpots(page, gridFilter);
      fetchDatabankCoverage();
    } catch (e) {}
  };

  const unpublishState = async (state: string) => {
    if (!state || state.length !== 2) return;
    if (!confirm(`⚠️ Retract ALL published records in ${state} from the live app map?`)) return;
    try {
      const res = await fetch(`${API_BASE}/api/unpublish-state/${state}`, { method: 'POST' });
      const data = await res.json();
      alert(`Retracted ${data.unpublished ?? 0} records in ${state}.`);
      fetchSpots(page, gridFilter);
      fetchDatabankCoverage();
    } catch (e) {}
  };

  const PIPELINE_PHASES = [
    { id: '1', title: 'The Scout', sub: 'Google Places Sweep — Nationwide Seeding', route: 'phase1', color: '#8a2be2', 
      target: 'Google API → ENRICHED', 
      metric: (status?.enrichedCount || 0) + (status?.pendingCount || 0), metricLabel: 'Total Seeded', isDaemon: false, 
      statusActive: status?.isHarvestingActive || status?.isGoogleSweepActive },
    { id: '2', title: 'The Operator', sub: 'Identity Resolution (Website + Phone)', route: 'phase2', color: '#5d78ff', 
      target: 'PENDING → IDENTITY_ESTABLISHED', 
      metric: status?.identityCount || 0, metricLabel: 'Identities Resolved', isDaemon: true, 
      statusActive: status?.currentTarget?.includes('Operator: online') },
    { id: '3', title: 'The Detective', sub: 'Website Deep Crawl + Photo Candidates', route: 'phase3', color: '#ff5a00', 
      target: 'ENRICHED → is_deep_crawled', 
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
      target: 'QA Review -> Selective Publish',
      metric: (status?.enrichedCount || 0) + (status?.mediaReadyCount || 0) + (status?.verifiedCount || 0),
      metricLabel: 'Total Pipeline Records', isDaemon: false,
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

      {/* === ACTIVE REGION: Global Priority Filter === */}
      <div style={{ marginBottom: '2rem', padding: '1rem 1.5rem', background: 'rgba(138,43,226,0.06)', borderRadius: '10px', border: '1px solid rgba(138,43,226,0.25)' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.75rem' }}>
          <span style={{ fontSize: '0.7rem', textTransform: 'uppercase', fontWeight: 800, color: '#8a2be2', letterSpacing: '0.1em' }}>
            ACTIVE REGION
            <span style={{ color: 'rgba(255,255,255,0.35)', fontWeight: 400, textTransform: 'none', fontSize: '0.7rem', marginLeft: '6px' }}>-- all daemon phases prioritize selected states</span>
          </span>
          <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
            {stateOverride.length > 0 && (
              <span style={{ fontSize: '0.7rem', color: '#8a2be2', fontWeight: 700 }}>{stateOverride.length} state{stateOverride.length !== 1 ? 's' : ''} targeted</span>
            )}
            <button onClick={() => setPriorityStates([])}
              style={{ fontSize: '0.65rem', padding: '4px 14px', borderRadius: '12px', border: '1px solid rgba(255,255,255,0.15)', background: stateOverride.length === 0 ? '#8a2be2' : 'transparent', color: stateOverride.length === 0 ? '#fff' : 'rgba(255,255,255,0.4)', cursor: 'pointer', fontWeight: 700 }}
            >NATIONWIDE</button>
          </div>
        </div>
        {stateOverride.length > 0 ? (
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px' }}>
            {stateOverride.map(st => (
              <button key={st} onClick={() => togglePriorityState(st)}
                style={{ padding: '4px 12px', fontSize: '0.75rem', borderRadius: '12px', border: 'none', background: '#8a2be2', color: '#fff', cursor: 'pointer', fontWeight: 700 }}
              >{st} x</button>
            ))}
            <button
              onClick={() => { const s = prompt('Add state (2-letter code):'); if (s && s.trim().length >= 2) togglePriorityState(s.trim().toUpperCase().slice(0, 2)); }}
              style={{ padding: '4px 12px', fontSize: '0.75rem', borderRadius: '12px', border: '1px dashed rgba(138,43,226,0.5)', background: 'transparent', color: '#8a2be2', cursor: 'pointer' }}
            >+ Add State</button>
          </div>
        ) : (
          <div style={{ fontSize: '0.75rem', color: 'rgba(255,255,255,0.3)', fontStyle: 'italic' }}>
            Nationwide -- all daemons process states equally. Click a state on the Phase 1 map or leaderboard to target a region.
          </div>
        )}
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
              <p>Phase 1 uses the <strong style={{color:'#ffb300'}}>Google Places API</strong> to seed the entire pipeline — querying by state for roller rinks and skate shops. Each result is written directly as an <strong>ENRICHED</strong> record with full coordinates, phone, hours, rating, and website. The OSM fallback mode is available for legacy re-harvests but is <em style={{color:'rgba(255,255,255,0.4)'}}>not recommended</em> — Google data is always higher quality.</p>
            </div>

            <div className="flow-visualizer" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '2rem', padding: '3rem 2rem', background: 'rgba(0,0,0,0.2)', borderRadius: '12px', marginTop: '1rem', marginBottom: '2rem' }}>
               <div style={{ textAlign: 'center', minWidth: '120px' }}>
                 <div style={{ fontSize: '2rem', fontWeight: 800, color: '#ffb300' }}>Google</div>
                 <div style={{ fontSize: '0.75rem', color: '#ffb300', textTransform: 'uppercase', fontWeight: 700 }}>Places API (Primary)</div>
                 <div style={{ fontSize: '0.7rem', color: 'rgba(255,255,255,0.3)', marginTop: '4px' }}>writes ENRICHED directly</div>
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
               <div style={{ textAlign: 'center', minWidth: '120px' }}>
                 <div style={{ fontSize: '2.5rem', fontWeight: 800, color: '#ff9800' }}>{status?.enrichedCount || 0}</div>
                 <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', textTransform: 'uppercase' }}>ENRICHED in DB</div>
                 <div style={{ fontSize: '0.7rem', color: 'rgba(255,255,255,0.3)', marginTop: '4px' }}>Google-sourced records</div>
               </div>
            </div>

            {/* OMNI-NET DISCOVERY PANEL */}
            <div className="discovery-net-panel" style={{ background: 'linear-gradient(135deg, rgba(138,43,226,0.1) 0%, rgba(0,0,0,0.3) 100%)', padding: '1.5rem', borderRadius: '12px', border: '1px solid rgba(138, 43, 226, 0.3)', marginBottom: '2rem', position: 'relative' }}>
               <div style={{ position: 'absolute', top: '-10px', right: '20px', background: '#8a2be2', color: '#fff', fontSize: '0.6rem', padding: '2px 8px', borderRadius: '4px', fontWeight: 800 }}>STATE TARGETING</div>
               <h3 style={{ marginTop: 0, color: '#fff', fontSize: 18 }}>📡 State Re-Sweep Controls</h3>
               <p style={{ fontSize: '0.8rem', color: 'rgba(255,255,255,0.6)', marginBottom: '1.5rem' }}>Manually trigger a Google Places sweep for a specific state, or switch the seeding provider. Useful for topping up states with low record counts.</p>
               
               <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                  <div style={{ background: 'rgba(255,255,255,0.03)', padding: '1rem', borderRadius: '8px' }}>
                     <h4 style={{ margin: '0 0 10px 0', fontSize: '0.8rem', color: '#8a2be2' }}>Re-Sweep State</h4>
                     <p style={{ fontSize: '0.7rem', color: 'rgba(255,255,255,0.4)', marginBottom: '1rem' }}>Force a fresh Google Places sweep for a single state. Respects the dedup logic — existing records are refreshed, not duplicated.</p>
                     <div style={{ display: 'flex', gap: '8px' }}>
                       <select className="mini-input" style={{ flex: 1, background: '#1a1a1a' }} id="force-state-sel">
                         {US_STATES.map(s => <option key={s} value={s}>{s}</option>)}
                       </select>
                       <button className="btn-mini" onClick={() => triggerForceHarvest((document.getElementById('force-state-sel') as HTMLSelectElement).value)}>FORCE RELOAD</button>
                     </div>
                  </div>
                  
                  <div style={{ background: 'rgba(255,255,255,0.03)', padding: '1rem', borderRadius: '8px' }}>
                     <h4 style={{ margin: '0 0 10px 0', fontSize: '0.8rem', color: '#ffb300' }}>Origin Provider</h4>
                     <p style={{ fontSize: '0.7rem', color: 'rgba(255,255,255,0.4)', marginBottom: '1rem' }}>Select the data source for the GLOBAL SEED button. Google Places is the primary source and writes ENRICHED records directly. OSM is a legacy fallback that writes PENDING skeletons.</p>
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

            {/* ========= GOOGLE COVERAGE DENSITY MAP ========= */}
            <div className="panel coverage-panel" style={{ marginTop: '2rem' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.5rem' }}>
                <h2 className="panel-header" style={{ margin: 0 }}>📡 Google Places Coverage — State Density</h2>
                <span style={{ fontSize: '0.75rem', color: 'rgba(255,255,255,0.4)', fontStyle: 'italic' }}>
                  {databankCoverage.reduce((a: number, r: any) => a + (r.total || 0), 0).toLocaleString()} records across {databankCoverage.filter((r: any) => r.total > 0).length} states
                </span>
              </div>
              <p style={{ fontSize: '0.8rem', color: 'rgba(255,255,255,0.4)', margin: '0 0 1rem' }}>
                Record density per state from Google Places. Click any state to target it for the next sweep.
              </p>

              {/* Density legend */}
              <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap', marginBottom: '1.5rem' }}>
                {[
                  { color: 'rgba(255,255,255,0.07)', label: 'Untouched',       range: '0 records' },
                  { color: '#f5a623cc',               label: 'Early Coverage', range: '1–49' },
                  { color: '#ff6b00cc',               label: 'Well Seeded',    range: '50–99' },
                  { color: '#4caf50cc',               label: 'Saturated',      range: '100+' },
                ].map(t => (
                  <div key={t.label} style={{ display: 'flex', alignItems: 'center', gap: '6px', background: 'rgba(255,255,255,0.03)', padding: '5px 12px', borderRadius: '20px', border: '1px solid rgba(255,255,255,0.08)' }}>
                    <span style={{ width: '10px', height: '10px', borderRadius: '2px', background: t.color, display: 'inline-block', border: '1px solid rgba(255,255,255,0.15)' }}></span>
                    <span style={{ fontSize: '0.75rem', color: 'rgba(255,255,255,0.7)', fontWeight: 700 }}>{t.label}</span>
                    <span style={{ fontSize: '0.7rem', color: 'rgba(255,255,255,0.35)' }}>{t.range}</span>
                  </div>
                ))}
              </div>

              <div style={{ display: 'flex', justifyContent: 'center', width: '100%', maxWidth: '800px', margin: '0 auto' }}>
                {/* @ts-ignore */}
                <USAMap
                  defaultFill="rgba(255,255,255,0.05)"
                  customize={(() => {
                    const colors: Record<string, any> = {};
                    databankCoverage.forEach((row: any) => {
                      if (!row.state || row.state === 'UNKNOWN') return;
                      const n = row.total || 0;
                      if (n === 0) return;
                      // Density tiers: amber → orange → green
                      const fill = n >= 100 ? '#4caf50cc'
                        : n >= 50  ? '#ff6b00cc'
                        : '#f5a623cc';
                      colors[row.state] = { fill, customText: n.toString() };
                    });
                    return colors;
                  })()}
                  onClick={(e: any) => {
                    const st = e.target?.dataset?.name || e.target?.id;
                    if (st && st.length === 2) updateGlobalStrategy('state_override', st);
                  }}
                />
              </div>

              {/* State leaderboard table */}
              {databankCoverage.length > 0 && (
                <div style={{ marginTop: '2rem', maxHeight: '260px', overflowY: 'auto' }}>
                  <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '0.8rem' }}>
                    <thead>
                      <tr style={{ borderBottom: '1px solid rgba(255,255,255,0.1)', color: 'rgba(255,255,255,0.4)', textTransform: 'uppercase', fontSize: '0.7rem' }}>
                        <th style={{ textAlign: 'left', padding: '6px 8px' }}>State</th>
                        <th style={{ textAlign: 'right', padding: '6px 8px' }}>Records</th>
                        <th style={{ textAlign: 'right', padding: '6px 8px' }}>ENRICHED</th>
                        <th style={{ textAlign: 'right', padding: '6px 8px' }}>MEDIA_READY</th>
                        <th style={{ textAlign: 'right', padding: '6px 8px' }}>Published</th>
                        <th style={{ textAlign: 'center', padding: '6px 8px' }}>Action</th>
                      </tr>
                    </thead>
                    <tbody>
                      {[...databankCoverage]
                        .filter((r: any) => r.total > 0)
                        .sort((a: any, b: any) => b.total - a.total)
                        .map((row: any) => {
                          const tier = row.total >= 100 ? { color: '#4caf50', label: '🟢' }
                            : row.total >= 50 ? { color: '#ff6b00', label: '🟠' }
                            : { color: '#f5a623', label: '🟡' };
                          return (
                            <tr key={row.state} style={{ borderBottom: '1px solid rgba(255,255,255,0.05)' }}>
                              <td style={{ padding: '6px 8px', fontWeight: 700, color: tier.color }}>
                                {tier.label} {row.state}
                              </td>
                              <td style={{ padding: '6px 8px', textAlign: 'right', fontWeight: 800 }}>{(row.total || 0).toLocaleString()}</td>
                              <td style={{ padding: '6px 8px', textAlign: 'right', color: '#ff9800' }}>{(row.ENRICHED || 0).toLocaleString()}</td>
                              <td style={{ padding: '6px 8px', textAlign: 'right', color: '#e91e63' }}>{(row.MEDIA_READY || 0).toLocaleString()}</td>
                              <td style={{ padding: '6px 8px', textAlign: 'right', color: '#4caf50' }}>{(row.published || 0).toLocaleString()}</td>
                              <td style={{ padding: '6px 8px', textAlign: 'center' }}>
                                <button
                                  style={{ fontSize: '0.65rem', padding: '3px 10px', borderRadius: '10px', border: 'none', background: stateOverride.includes(row.state) ? '#8a2be2' : 'rgba(255,255,255,0.08)', color: stateOverride.includes(row.state) ? '#fff' : 'rgba(255,255,255,0.6)', cursor: 'pointer', fontWeight: 700 }}
                                  onClick={() => updateGlobalStrategy('state_override', row.state)}
                                >{stateOverride.includes(row.state) ? '✓ TARGETED' : 'TARGET'}</button>
                              </td>
                            </tr>
                          );
                        })}
                    </tbody>
                  </table>
                </div>
              )}
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
                        No records harvested yet. Click [GLOBAL SEED] to run the Google Places sweep.
                    </div>
                ) : (
                    <div className="mini-data-bank" style={{marginTop: '10px'}}>
                      {(phaseQueues['recent'] || []).map(spot => (
                        <div key={spot.id} className="queue-card active" style={{ borderColor: spot.verification_status === 'ENRICHED' ? '#ff5a00' : 'rgba(255,255,255,0.1)' }}>
                          <div className="queue-card-title">{spot.name}</div>
                          <div className="queue-card-loc">{spot.city}, {spot.state}</div>
                          <div className="queue-tags">
                            <span className="queue-badge" style={{ background: spot.verification_status === 'ENRICHED' ? 'rgba(255,90,0,0.1)' : 'rgba(255,255,255,0.05)', color: spot.verification_status === 'ENRICHED' ? '#ff5a00' : 'var(--text-secondary)' }}>
                              {spot.verification_status === 'ENRICHED' ? '✨ ENRICHED (Google)' : '⏳ RAW SEED'}
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
               {activeTab === 'phase2' && <p>Targets <strong>PENDING</strong> records and resolves their real-world identity — finding the business website and phone number via web search heuristics. Graduates records to <strong>IDENTITY_ESTABLISHED</strong> when found. <em style={{color:'rgba(255,255,255,0.4)'}}>Note: Since the pipeline now uses Google Places as the primary seeder, PENDING records are rare. This daemon handles any OSM legacy records or manually added entries.</em></p>}
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
              <h3 style={{marginTop: 0, color: '#4caf50'}}>Phase 6: Databank QA &amp; Live Publish</h3>
              <p>Final review before publication. Filter and inspect records, then publish state-by-state or individually. Use the view toggle to switch between Card, List, and Table views.</p>

              <div style={{ display: 'grid', gridTemplateColumns: 'minmax(0, 1fr) 2fr', gap: '10px', marginTop: '1rem', background: '#000', padding: '15px', borderRadius: '8px' }}>
                 <div style={{marginBottom: '10px'}}>
                    <span className="status-pill enriched" style={{marginBottom: 5, display: 'inline-block', border: '1px solid #ff9800', background:'rgba(255,152,0,0.1)'}}>ENRICHED</span><br/>
                    <span style={{fontSize: '0.8rem', color:'var(--text-secondary)'}}>Google Places data -- high-fidelity coordinates, hours, ratings. Priority publish candidates.</span>
                 </div>
                 <div style={{marginBottom: '10px'}}>
                    <span className="status-pill" style={{marginBottom: 5, display: 'inline-block', border: '1px solid #e91e63', background:'rgba(233,30,99,0.1)', color: '#e91e63'}}>MEDIA_READY</span><br/>
                    <span style={{fontSize: '0.8rem', color:'var(--text-secondary)'}}>Photos collected and uploaded to CDN. Full card view available with image preview.</span>
                 </div>
                 <div>
                    <span style={{fontSize: '0.85rem', fontWeight: 800, color:'#4caf50', border: '1px solid #4caf50', padding: '4px 8px', borderRadius: '4px', display:'inline-block', marginBottom: 5}}>APP_LIVE TOGGLE</span><br/>
                    <span style={{fontSize: '0.8rem', color:'var(--text-secondary)'}}>Toggle the APP_LIVE checkbox to instantly push or retract a record from the public SK8Lytz map.</span>
                 </div>
                 <div>
                    <span style={{fontSize: '0.85rem', fontWeight: 800, color:'#8a2be2', border: '1px solid #8a2be2', padding: '4px 8px', borderRadius: '4px', display:'inline-block', marginBottom: 5}}>PENDING</span><br/>
                    <span style={{fontSize: '0.8rem', color:'var(--text-secondary)'}}>Legacy OSM records -- lower fidelity. Review before publishing.</span>
                 </div>
              </div>
            </div>

            {/* =========== STATUS COVERAGE MAP =========== */}
            <div style={{ marginBottom: '2rem', padding: '1.5rem', background: 'rgba(255,179,0,0.03)', border: '1px solid rgba(255,179,0,0.2)', borderRadius: '12px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.5rem' }}>
                <h3 style={{ margin: 0, color: '#ffb300', fontSize: '0.95rem', textTransform: 'uppercase' }}>📡 Pipeline Coverage Map</h3>
                {/* Map Mode Toggle */}
                <div style={{ display: 'flex', gap: '6px', background: 'rgba(255,255,255,0.05)', padding: '4px', borderRadius: '8px' }}>
                  <button
                    onClick={() => setMapMode('quality')}
                    style={{ padding: '5px 14px', borderRadius: '6px', border: 'none', cursor: 'pointer', fontSize: '0.75rem', fontWeight: 700,
                      background: mapMode === 'quality' ? '#ffb300' : 'transparent',
                      color: mapMode === 'quality' ? '#000' : 'rgba(255,255,255,0.5)' }}
                  >📊 Quality</button>
                  <button
                    onClick={() => setMapMode('published')}
                    style={{ padding: '5px 14px', borderRadius: '6px', border: 'none', cursor: 'pointer', fontSize: '0.75rem', fontWeight: 700,
                      background: mapMode === 'published' ? '#4caf50' : 'transparent',
                      color: mapMode === 'published' ? '#000' : 'rgba(255,255,255,0.5)' }}
                  >🚀 Published</button>
                </div>
              </div>
              <p style={{ margin: '0 0 1.5rem', color: 'rgba(255,255,255,0.5)', fontSize: '0.8rem' }}>
                {mapMode === 'quality'
                  ? 'Dominant pipeline status per state. Click a state to filter the grid.'
                  : 'Live app coverage — green = fully published, grey = not yet live. Click to filter.'}
              </p>
              
              {/* Nationwide status totals */}
              {(() => {
                const totals: Record<string, number> = {};
                let totalPublished = 0;
                databankCoverage.forEach((row: any) => {
                  ['PENDING','IDENTITY_ESTABLISHED','INDEXED','ENRICHED','MEDIA_READY','VERIFIED'].forEach(s => {
                    if (row[s]) totals[s] = (totals[s] || 0) + row[s];
                  });
                  totalPublished += (row.published || 0);
                });
                const totalRecords = databankCoverage.reduce((a: number, r: any) => a + (r.total || 0), 0);
                const STATUS_META: Record<string, {color: string; label: string}> = {
                  PENDING:              { color: '#8a2be2', label: 'Pending' },
                  IDENTITY_ESTABLISHED: { color: '#5d78ff', label: 'Identified' },
                  INDEXED:              { color: '#ff5a00', label: 'Indexed' },
                  ENRICHED:             { color: '#ff9800', label: 'Enriched' },
                  MEDIA_READY:          { color: '#e91e63', label: 'Media Ready' },
                  VERIFIED:             { color: '#4caf50', label: 'Verified' },
                };
                return (
                  <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap', marginBottom: '1.5rem', alignItems: 'center' }}>
                    {mapMode === 'quality'
                      ? Object.entries(STATUS_META).map(([s, meta]) => (
                          <div key={s} style={{ display: 'flex', alignItems: 'center', gap: '6px', background: 'rgba(255,255,255,0.04)', padding: '6px 12px', borderRadius: '20px', border: `1px solid ${meta.color}44` }}>
                            <span style={{ width: '10px', height: '10px', borderRadius: '2px', background: meta.color, display: 'inline-block' }}></span>
                            <span style={{ fontSize: '0.75rem', color: meta.color, fontWeight: 700 }}>{meta.label}</span>
                            <span style={{ fontSize: '0.75rem', color: 'rgba(255,255,255,0.5)' }}>{(totals[s] || 0).toLocaleString()}</span>
                          </div>
                        ))
                      : (
                          <>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', background: 'rgba(76,175,80,0.08)', padding: '8px 16px', borderRadius: '20px', border: '1px solid #4caf5044' }}>
                              <span style={{ width: '10px', height: '10px', borderRadius: '2px', background: '#4caf50', display: 'inline-block' }}></span>
                              <span style={{ fontSize: '0.75rem', color: '#4caf50', fontWeight: 700 }}>🚀 Published</span>
                              <span style={{ fontSize: '0.85rem', color: '#4caf50', fontWeight: 800 }}>{totalPublished.toLocaleString()}</span>
                            </div>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', background: 'rgba(255,255,255,0.04)', padding: '8px 16px', borderRadius: '20px', border: '1px solid rgba(255,255,255,0.1)' }}>
                              <span style={{ width: '10px', height: '10px', borderRadius: '2px', background: 'rgba(255,255,255,0.2)', display: 'inline-block' }}></span>
                              <span style={{ fontSize: '0.75rem', color: 'rgba(255,255,255,0.5)', fontWeight: 700 }}>Unpublished</span>
                              <span style={{ fontSize: '0.85rem', color: 'rgba(255,255,255,0.5)', fontWeight: 800 }}>{(totalRecords - totalPublished).toLocaleString()}</span>
                            </div>
                            <div style={{ fontSize: '0.75rem', color: 'rgba(255,255,255,0.3)', marginLeft: 'auto' }}>
                              {totalRecords > 0 ? `${Math.round((totalPublished / totalRecords) * 100)}% live` : '0% live'}
                            </div>
                          </>
                        )
                    }
                  </div>
                );
              })()}

              <div style={{ display: 'flex', justifyContent: 'center', width: '100%', maxWidth: '800px', margin: '0 auto' }}>
                {/* @ts-ignore */}
                <USAMap
                  defaultFill="rgba(255,255,255,0.05)"
                  customize={(() => {
                    const colors: Record<string, any> = {};

                    if (mapMode === 'quality') {
                      const STATUS_PRIORITY = ['VERIFIED','MEDIA_READY','ENRICHED','INDEXED','IDENTITY_ESTABLISHED','PENDING'];
                      const STATUS_COLOR: Record<string, string> = {
                        VERIFIED: '#4caf50', MEDIA_READY: '#e91e63', ENRICHED: '#ff9800',
                        INDEXED: '#ff5a00', IDENTITY_ESTABLISHED: '#5d78ff', PENDING: '#8a2be2',
                      };
                      databankCoverage.forEach((row: any) => {
                        if (!row.state || row.state === 'UNKNOWN') return;
                        const total = row.total || 0;
                        if (total === 0) return;
                        const dominant = STATUS_PRIORITY.find(s => (row[s] || 0) > 0) || 'PENDING';
                        const baseColor = STATUS_COLOR[dominant] || '#8a2be2';
                        const density = Math.min(total / 40, 1);
                        const opacity = Math.max(density, 0.35);
                        colors[row.state] = {
                          fill: baseColor + Math.round(opacity * 255).toString(16).padStart(2, '0'),
                          customText: total.toString()
                        };
                      });
                    } else {
                      // Published mode: green gradient by % published
                      databankCoverage.forEach((row: any) => {
                        if (!row.state || row.state === 'UNKNOWN') return;
                        const total = row.total || 0;
                        const published = row.published || 0;
                        if (total === 0) return;
                        const pct = published / total;
                        // 0% = faint purple, 100% = vivid green
                        const color = pct === 0 ? '#ffffff14'
                          : pct < 0.25 ? '#ff980060'
                          : pct < 0.75 ? '#4caf5088'
                          : '#4caf50dd';
                        colors[row.state] = {
                          fill: color,
                          customText: `${published}/${total}`
                        };
                      });
                    }
                    return colors;
                  })()}
                  onClick={(e: any) => {
                    const st = e.target?.dataset?.name || e.target?.id;
                    if (st && st.length === 2) {
                      setSearchQuery(st);
                      setActiveStateFilter(st);
                    }
                  }}
                />
              </div>
            </div>

            <div className="grid-toolbar">
              {/* View Mode Toggle: Cards | List | Table */}
              <div style={{ display: 'flex', gap: '4px', background: 'rgba(255,255,255,0.05)', padding: '4px', borderRadius: '8px', marginRight: '8px', flexShrink: 0 }}>
                {(['card', 'list', 'table'] as const).map(mode => (
                  <button key={mode} onClick={() => setViewMode(mode)}
                    style={{ padding: '5px 12px', borderRadius: '6px', border: 'none', cursor: 'pointer', fontSize: '0.7rem', fontWeight: 700, textTransform: 'uppercase',
                      background: viewMode === mode ? '#8a2be2' : 'transparent',
                      color: viewMode === mode ? '#fff' : 'rgba(255,255,255,0.4)' }}
                  >{mode === 'card' ? 'Cards' : mode === 'list' ? 'List' : 'Table'}</button>
                ))}
              </div>
              <input
                className="form-input search-bar"
                placeholder="Search Location / City / State..."
                value={searchQuery}
                onChange={e => { setSearchQuery(e.target.value); setActiveStateFilter(e.target.value.trim().length === 2 ? e.target.value.trim().toUpperCase() : null); }}
              />
               <select className="form-input filter-dropdown" value={gridFilter} onChange={e => setGridFilter(e.target.value)}>
                  <option value="ALL">All Records</option>
                  <option value="MEDIA_READY">MEDIA_READY -- Photo Ready (Priority)</option>
                  <option value="ENRICHED">ENRICHED -- Google Seeded</option>
                  <option value="PENDING">PENDING -- Legacy</option>
                  <option value="IDENTITY_ESTABLISHED">IDENTIFIED</option>
                  <option value="INDEXED">WEB CRAWLED</option>
                  <option value="REJECTED">Graveyard / Rejected</option>
               </select>
              {/* State-scoped publish / unpublish — appears when a 2-letter state filter is active */}
              {activeStateFilter && activeStateFilter.length === 2 && (
                <>
                  <button
                    className="btn-primary"
                    style={{ background: '#4caf50', border: 'none', padding: '8px 16px', borderRadius: '6px', fontWeight: 700, fontSize: '0.8rem', cursor: 'pointer' }}
                    onClick={() => promoteState(activeStateFilter)}
                  >🚀 Publish {activeStateFilter}</button>
                  <button
                    style={{ background: 'rgba(255,59,48,0.15)', border: '1px solid rgba(255,59,48,0.4)', padding: '8px 16px', borderRadius: '6px', fontWeight: 700, fontSize: '0.8rem', cursor: 'pointer', color: '#ff3b30' }}
                    onClick={() => unpublishState(activeStateFilter)}
                  >↩ Retract {activeStateFilter}</button>
                </>
              )}
              <button className="btn-primary" onClick={bulkPromote}>🚀 Bulk App Promote</button>
              <div className="pagination">
                <button disabled={page === 0} onClick={() => fetchSpots(page - 1, gridFilter)}>Prev</button>
                <span className="page-indicator">Page {page + 1} of {Math.ceil(totalSpots/rowsPerPage) || 1} ({totalSpots} total)</span>
                <button disabled={(page+1)*rowsPerPage >= totalSpots} onClick={() => fetchSpots(page + 1, gridFilter)}>Next</button>
              </div>
            </div>

            {/* =========== CARD VIEW =========== */}
            {viewMode === 'card' && (
              <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: '1.5rem', padding: '0.5rem 0' }}>
                {spots.map(row => {
                  const photos: string[] = row.photos ? (typeof row.photos === 'string' ? JSON.parse(row.photos) : row.photos) : [];
                  const hours: string[] = row.hours ? (typeof row.hours === 'string' ? JSON.parse(row.hours) : row.hours) : [];
                  const S: Record<string,string> = { ENRICHED:'#ff9800', MEDIA_READY:'#e91e63', PENDING:'#8a2be2', IDENTITY_ESTABLISHED:'#5d78ff', INDEXED:'#ff5a00', REJECTED:'#666' };
                  const sc = S[row.verification_status] || '#666';
                  return (
                    <div key={row.id} style={{ background:'rgba(255,255,255,0.03)', border:'1px solid rgba(255,255,255,0.08)', borderRadius:'16px', overflow:'hidden', display:'flex', flexDirection:'column', transition:'transform 0.2s, box-shadow 0.2s' }}
                      onMouseEnter={e => { const el = e.currentTarget as HTMLElement; el.style.transform='translateY(-3px)'; el.style.boxShadow='0 12px 40px rgba(0,0,0,0.4)'; }}
                      onMouseLeave={e => { const el = e.currentTarget as HTMLElement; el.style.transform='none'; el.style.boxShadow='none'; }}
                    >
                      <div style={{ height:'180px', background:'rgba(0,0,0,0.3)', position:'relative', overflow:'hidden' }}>
                        {photos.length > 0
                          ? <img src={photos[0]} alt={row.name} style={{ width:'100%', height:'100%', objectFit:'cover' }} />
                          : <div style={{ width:'100%', height:'100%', display:'flex', alignItems:'center', justifyContent:'center', color:'rgba(255,255,255,0.15)', fontSize:'2rem' }}>No Photo</div>
                        }
                        <div style={{ position:'absolute', top:'10px', left:'10px', background:sc, color:'#fff', padding:'3px 10px', borderRadius:'20px', fontSize:'0.6rem', fontWeight:800 }}>{row.verification_status||'PENDING'}</div>
                        {row.is_published && <div style={{ position:'absolute', top:'10px', right:'10px', background:'#4caf50', color:'#fff', padding:'3px 10px', borderRadius:'20px', fontSize:'0.6rem', fontWeight:800 }}>LIVE</div>}
                        {photos.length > 1 && <div style={{ position:'absolute', bottom:'8px', right:'10px', background:'rgba(0,0,0,0.7)', color:'#fff', padding:'2px 8px', borderRadius:'10px', fontSize:'0.6rem' }}>{photos.length} photos</div>}
                      </div>
                      <div style={{ padding:'1rem', flex:1, display:'flex', flexDirection:'column', gap:'0.5rem' }}>
                        <div>
                          <div style={{ fontWeight:800, fontSize:'1rem', color:'#fff', marginBottom:'2px' }}>{row.name}</div>
                          <div style={{ fontSize:'0.8rem', color:'rgba(255,255,255,0.5)' }}>{row.street_address||'No address'} &bull; {row.city}, {row.state}</div>
                        </div>
                        {row.rating && (
                          <div style={{ display:'flex', alignItems:'center', gap:'6px' }}>
                            <span style={{ color:'#ffd700', fontWeight:800 }}>{row.rating}</span>
                            <span style={{ color:'#ffd700', fontSize:'0.8rem' }}>{'★'.repeat(Math.round(row.rating))}{'☆'.repeat(5-Math.round(row.rating))}</span>
                            <span style={{ color:'rgba(255,255,255,0.4)', fontSize:'0.75rem' }}>({(row.user_ratings_total||0).toLocaleString()})</span>
                          </div>
                        )}
                        <div style={{ display:'flex', flexWrap:'wrap', gap:'6px' }}>
                          {row.facility_type && <span style={{ background:'rgba(138,43,226,0.2)', border:'1px solid rgba(138,43,226,0.3)', color:'#b06bff', padding:'2px 8px', borderRadius:'10px', fontSize:'0.65rem', fontWeight:700 }}>{row.facility_type.replace(/_/g,' ').toUpperCase()}</span>}
                          {row.has_adult_night && <span style={{ background:'rgba(233,30,99,0.15)', border:'1px solid #e91e63', color:'#e91e63', padding:'2px 8px', borderRadius:'10px', fontSize:'0.65rem', fontWeight:700 }}>18+ NIGHT</span>}
                          {row.surface_quality && <span style={{ background:'rgba(255,152,0,0.1)', border:'1px solid rgba(255,152,0,0.3)', color:'#ff9800', padding:'2px 8px', borderRadius:'10px', fontSize:'0.65rem' }}>{row.surface_quality}</span>}
                        </div>
                        {hours.length > 0 && <div style={{ fontSize:'0.7rem', color:'rgba(255,255,255,0.4)', borderTop:'1px solid rgba(255,255,255,0.06)', paddingTop:'0.5rem' }}>{hours[0]}</div>}
                        <div style={{ display:'flex', gap:'8px', fontSize:'0.75rem', marginTop:'auto' }}>
                          {row.website && <a href={row.website} target="_blank" rel="noreferrer" style={{ color:'#8a2be2', fontWeight:600 }}>Website</a>}
                          {row.phone_number && <span style={{ color:'rgba(255,255,255,0.5)' }}>{row.phone_number}</span>}
                        </div>
                      </div>
                      <div style={{ padding:'0.75rem 1rem', borderTop:'1px solid rgba(255,255,255,0.06)', display:'flex', alignItems:'center', justifyContent:'space-between', background:'rgba(0,0,0,0.2)' }}>
                        <label style={{ display:'flex', alignItems:'center', gap:'6px', cursor:'pointer' }}>
                          <input type="checkbox" checked={row.is_published} onChange={e => promoteSpot(row.id, e.target.checked)} />
                          <span style={{ fontSize:'0.7rem', fontWeight:800, color:row.is_published?'#4caf50':'rgba(255,255,255,0.4)' }}>{row.is_published?'LIVE ON APP':'PUBLISH'}</span>
                        </label>
                        <div style={{ display:'flex', gap:'6px' }}>
                          <button onClick={() => startEdit(row)} style={{ padding:'4px 10px', borderRadius:'6px', border:'1px solid rgba(255,255,255,0.1)', background:'transparent', color:'rgba(255,255,255,0.6)', cursor:'pointer', fontSize:'0.7rem' }}>Edit</button>
                          <button onClick={() => deleteSpot(row.id, row.name)} style={{ padding:'4px 10px', borderRadius:'6px', border:'1px solid rgba(255,59,48,0.3)', background:'rgba(255,59,48,0.1)', color:'#ff3b30', cursor:'pointer', fontSize:'0.7rem' }}>Delete</button>
                        </div>
                      </div>
                    </div>
                  );
                })}
              </div>
            )}

            {/* =========== LIST VIEW (eBay-style) =========== */}
            {viewMode === 'list' && (
              <div style={{ display:'flex', flexDirection:'column', gap:'0.75rem' }}>
                {spots.map(row => {
                  const photos: string[] = row.photos ? (typeof row.photos === 'string' ? JSON.parse(row.photos) : row.photos) : [];
                  const S: Record<string,string> = { ENRICHED:'#ff9800', MEDIA_READY:'#e91e63', PENDING:'#8a2be2', IDENTITY_ESTABLISHED:'#5d78ff', INDEXED:'#ff5a00', REJECTED:'#444' };
                  const sc = S[row.verification_status] || '#666';
                  return (
                    <div key={row.id} style={{ display:'flex', background:'rgba(255,255,255,0.025)', border:'1px solid rgba(255,255,255,0.07)', borderRadius:'12px', overflow:'hidden', transition:'background 0.2s', alignItems:'stretch' }}
                      onMouseEnter={e => (e.currentTarget as HTMLElement).style.background='rgba(255,255,255,0.045)'}
                      onMouseLeave={e => (e.currentTarget as HTMLElement).style.background='rgba(255,255,255,0.025)'}
                    >
                      <div style={{ width:'120px', minWidth:'120px', background:'rgba(0,0,0,0.3)', position:'relative', flexShrink:0 }}>
                        {photos.length > 0
                          ? <img src={photos[0]} alt={row.name} style={{ width:'120px', height:'100%', objectFit:'cover', display:'block' }} />
                          : <div style={{ width:'100%', height:'100%', minHeight:'80px', display:'flex', alignItems:'center', justifyContent:'center', color:'rgba(255,255,255,0.15)', fontSize:'1.5rem' }}>?</div>
                        }
                        <div style={{ position:'absolute', bottom:0, left:0, right:0, background:sc, padding:'2px 0', textAlign:'center', fontSize:'0.5rem', fontWeight:800, color:'#fff' }}>{row.verification_status||'PENDING'}</div>
                      </div>
                      <div style={{ flex:1, padding:'0.75rem', display:'flex', gap:'1rem', alignItems:'center', flexWrap:'wrap' }}>
                        <div style={{ minWidth:'180px', flex:2 }}>
                          <div style={{ fontWeight:700, fontSize:'0.9rem', color:'#fff' }}>{row.name}</div>
                          <div style={{ fontSize:'0.75rem', color:'rgba(255,255,255,0.45)' }}>{row.city}, {row.state}</div>
                          {row.street_address && <div style={{ fontSize:'0.7rem', color:'rgba(255,255,255,0.3)', marginTop:'2px' }}>{row.street_address}</div>}
                        </div>
                        <div style={{ display:'flex', gap:'1rem', flex:3, flexWrap:'wrap', alignItems:'center' }}>
                          <div style={{ minWidth:'90px', textAlign:'center' }}>
                            {row.rating
                              ? <><div style={{ color:'#ffd700', fontWeight:800, fontSize:'1rem' }}>{row.rating}</div><div style={{ color:'#ffd700', fontSize:'0.65rem' }}>{'★'.repeat(Math.round(row.rating))}{'☆'.repeat(5-Math.round(row.rating))}</div><div style={{ color:'rgba(255,255,255,0.3)', fontSize:'0.65rem' }}>{(row.user_ratings_total||0).toLocaleString()} reviews</div></>
                              : <span style={{ color:'rgba(255,255,255,0.2)', fontSize:'0.75rem' }}>No rating</span>
                            }
                          </div>
                          <div style={{ display:'flex', flexDirection:'column', gap:'4px', minWidth:'100px' }}>
                            {row.facility_type && <span style={{ fontSize:'0.65rem', color:'#b06bff', fontWeight:700 }}>{row.facility_type.replace(/_/g,' ').toUpperCase()}</span>}
                            {row.has_adult_night && <span style={{ fontSize:'0.65rem', color:'#e91e63', fontWeight:700 }}>18+ NIGHT</span>}
                            {row.phone_number && <span style={{ fontSize:'0.65rem', color:'rgba(255,255,255,0.4)' }}>{row.phone_number}</span>}
                          </div>
                          <div style={{ minWidth:'80px' }}>
                            {row.website ? <a href={row.website} target="_blank" rel="noreferrer" style={{ color:'#8a2be2', fontWeight:600, fontSize:'0.75rem' }}>Website</a> : <span style={{ color:'rgba(255,255,255,0.2)', fontSize:'0.75rem' }}>No website</span>}
                          </div>
                        </div>
                        <div style={{ display:'flex', flexDirection:'column', gap:'6px', alignItems:'flex-end', minWidth:'90px' }}>
                          <label style={{ display:'flex', alignItems:'center', gap:'4px', cursor:'pointer' }}>
                            <input type="checkbox" checked={row.is_published} onChange={e => promoteSpot(row.id, e.target.checked)} />
                            <span style={{ fontSize:'0.65rem', fontWeight:800, color:row.is_published?'#4caf50':'rgba(255,255,255,0.35)' }}>{row.is_published?'LIVE':'PUBLISH'}</span>
                          </label>
                          <button onClick={() => startEdit(row)} style={{ padding:'3px 8px', borderRadius:'5px', border:'1px solid rgba(255,255,255,0.1)', background:'transparent', color:'rgba(255,255,255,0.5)', cursor:'pointer', fontSize:'0.65rem' }}>Edit</button>
                          <button onClick={() => deleteSpot(row.id, row.name)} style={{ padding:'3px 8px', borderRadius:'5px', border:'1px solid rgba(255,59,48,0.2)', background:'transparent', color:'#ff3b30', cursor:'pointer', fontSize:'0.65rem' }}>Del</button>
                        </div>
                      </div>
                    </div>
                  );
                })}
              </div>
            )}

            {/* =========== TABLE VIEW =========== */}
            {viewMode === 'table' && (
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
            )}
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
