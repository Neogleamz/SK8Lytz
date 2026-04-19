import React, { useState, useEffect, useRef } from 'react';
import './App.css';

const API_BASE = 'http://localhost:5999';

const US_STATES = [
  'AL', 'AK', 'AZ', 'AR', 'CA', 'CO', 'CT', 'DE', 'FL', 'GA',
  'HI', 'ID', 'IL', 'IN', 'IA', 'KS', 'KY', 'LA', 'ME', 'MD',
  'MA', 'MI', 'MN', 'MS', 'MO', 'MT', 'NE', 'NV', 'NH', 'NJ',
  'NM', 'NY', 'NC', 'ND', 'OH', 'OK', 'OR', 'PA', 'RI', 'SC',
  'SD', 'TN', 'TX', 'UT', 'VT', 'VA', 'WA', 'WV', 'WI', 'WY', 'DC'
];

function App() {
  const [activeTab, setActiveTab] = useState<'phase1' | 'phase2' | 'phase3'>('phase1');

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
  const [logs, setLogs] = useState<{type: string, message: string}[]>([]);
  const [queueSpots, setQueueSpots] = useState<any[]>([]);
  const logsRef = useRef<HTMLDivElement>(null);

  // --- Harvest Manager States ---
  const [harvestData, setHarvestData] = useState<{seededStates: string[], stateCounts: Record<string, number>, allStates: string[]}>({ seededStates: [], stateCounts: {}, allStates: [] });
  const [isHarvesting, setIsHarvesting] = useState<string | null>(null);
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
      if (activeTab === 'phase2') fetchQueue();
      fetchCoverage(); // Refresh coverage stats periodically
    }, 5000);

    // SSE Log Hook
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
    if (activeTab === 'phase3') {
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
            // Support both array and scalar migrations gracefully
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

  const fetchQueue = async () => {
    try {
      const res = await fetch(`${API_BASE}/api/queue`);
      if (res.ok) setQueueSpots((await res.json()).spots);
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

  // --- Handlers ---
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
    } finally {
      setIsHarvesting(null);
    }
  };

  const handleSysStart = async () => { await fetch(`${API_BASE}/start`, { method: 'POST' }); fetchSystemStatus(); };
  const handleSysStop = async () => { await fetch(`${API_BASE}/stop`, { method: 'POST' }); fetchSystemStatus(); };
  
  const toggleHeadless = async (newVal: boolean) => {
    setIsHeadless(newVal);
    await fetch(`${API_BASE}/api/headless`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ isHeadless: newVal })
    });
  };

  // Rest of Grid CRUD handlers
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
      <header className="header" style={{ alignItems: 'flex-start', flexWrap: 'wrap' }}>
        <div style={{width: '100%'}}>
          <h1 className="title">SK8Lytz Global Extraction Pipeline</h1>
          <p style={{marginTop: 0, color: 'var(--text-secondary)', fontSize: '0.9rem'}}>Decoupled 6-Phase Micro-Scraper Architecture</p>
        </div>
      </header>

      {/* =========== 6-PHASE UNIFORM PIPELINE GRID =========== */}
      <div className="pipeline-grid fade-in">
         {PIPELINE_PHASES.map((phase) => (
            <div 
               key={phase.id} 
               className={`pipeline-card ${activeTab === phase.route ? 'active' : ''}`}
               onClick={() => setActiveTab(phase.route)}
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
        {activeTab === 'phase1' && (() => {
          const totalNodes = Object.values(harvestData.stateCounts).reduce((a, b) => a + b, 0);
          return (
            <div className="tab-pane phase-1">
              <div className="explainer-block" style={{marginBottom: '1rem'}}>
              <h3 style={{marginTop: 0, color: '#8a2be2'}}>The Scout: Polygon Infrastructure</h3>
              <div className="ghost-badge">🛡️ GHOST ENCRYPTED PIPELINE ACTIVE</div>
              <p>This engine interfaces directly with OpenStreetMap's Overpass API. It extracts raw GIS locations (longitude/latitude) and establishes baseline row injection into Supabase. Real data validation occurs downstream.</p>
              </div>

              <div className="omni-grid tri-grid">
                <div className="controls-section">
                  <div className="panel intake-panel" style={{marginBottom: '1rem'}}>
                    <h2 className="panel-header">Execution Matrix</h2>
                    <div className="btn-group-vertical">
                      <button className="btn btn-start" onClick={() => triggerHarvest('start-all', stateOverride)} disabled={status?.isHarvestingActive}>
                          {stateOverride.length > 0 ? `🚀 Seed ${stateOverride.join(', ')}` : '🌎 Global Harvest'}
                      </button>
                      <button className="btn btn-stop" onClick={() => triggerHarvest('stop-all')} disabled={!status?.isHarvestingActive}>
                        🛑 STOP HARVEST
                      </button>
                    </div>
                  </div>

                  <div className="panel strategy-panel">
                    <h2 className="panel-header">1. Target Facilities</h2>
                    <div className="facility-switches">
                       {['skatepark', 'roller_rink', 'skate_shop'].map(f => (
                         <label key={f} className="switch-row mini">
                            <span>{f.replace('_', ' ').toUpperCase()}</span>
                            <label className="switch mini">
                              <input type="checkbox" checked={targetFacilities.includes(f)} onChange={() => updateGlobalStrategy('facility', f)} />
                              <span className="slider round"></span>
                            </label>
                         </label>
                       ))}
                    </div>
                  </div>
                </div>

                <div className="panel strategy-panel">
                  <h2 className="panel-header">2. Pipeline Targets</h2>
                  <div className="state-pill-container" style={{maxHeight: '400px'}}>
                     <button className={`state-pill ${stateOverride.length === 0 ? 'active' : ''}`} onClick={() => updateGlobalStrategy('state_override', 'ALL')}>
                       ALL STATES
                     </button>
                     {US_STATES.map(st => {
                        const count = harvestData.stateCounts[st] || 0;
                        return (
                          <button key={st} className={`state-pill ${stateOverride.includes(st) ? 'active' : ''}`} onClick={() => updateGlobalStrategy('state_override', st)}>
                            {st} {count > 0 && <span className="pill-dot"></span>}
                          </button>
                        )
                     })}
                  </div>
                </div>

                <div className="panel coverage-panel">
                  <h2 className="panel-header">3. Intake Leaderboard</h2>
                  <div className="coverage-list">
                     {coverageStats.slice(0, 10).map(stat => {
                        const enrichedPct = Math.min(100, Math.round((stat.enriched / stat.total) * 100) || 0);
                        return (
                          <div key={stat.state} className="coverage-item">
                             <div className="coverage-meta">
                                <strong>{stat.state}</strong>
                                <span>{stat.total}</span>
                             </div>
                             <div className="coverage-progress-bg">
                                <div className="progress-bar enriched" style={{width: `${enrichedPct}%`}}></div>
                             </div>
                          </div>
                        );
                     })}
                  </div>
                </div>
              </div>
            </div>
          );
        })()}

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

             {/* SHARED SECURITY CONTROL BAR */}
            <div className="security-control-bar fade-in" style={{marginBottom: '1.5rem'}}>
              <div className="security-label">
                <span className="ghost-badge" style={{margin:0}}>🛡️ GLOBAL DAEMON TACTICS</span>
              </div>
              
              <div className="security-inputs">
                <div className="input-group-inline">
                  <label>Base Cooldown</label>
                  <input type="number" className="mini-input" value={cooldownBase} onChange={e => updateGlobalStrategy('cooldown_base_ms', parseInt(e.target.value))} />
                </div>
                <div className="input-group-inline">
                  <label>Jitter</label>
                  <input type="number" className="mini-input" value={cooldownJitter} onChange={e => updateGlobalStrategy('cooldown_jitter_pct', parseInt(e.target.value))} />
                  <span>%</span>
                </div>
                
                <div className="v-divider"></div>

                <label className="security-switch">
                  <span>Stealth Headless Mode</span>
                  <label className="switch mini">
                    <input type="checkbox" checked={isHeadless} onChange={e => toggleHeadless(e.target.checked)} />
                    <span className="slider round"></span>
                  </label>
                </label>

                <label className="security-switch">
                  <span>Identity Rotation</span>
                  <label className="switch mini">
                    <input type="checkbox" checked={identityRotation} onChange={e => updateGlobalStrategy('identity_rotation_enabled', e.target.checked)} />
                    <span className="slider round"></span>
                  </label>
                </label>
              </div>
            </div>

            <div className="omni-grid" style={{display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem'}}>
              <div className="panel enrichment-panel">
                 <h2 className="panel-header">Cluster Master Matrix</h2>
                 <div className="btn-group">
                   <button className="btn btn-start" onClick={handleSysStart} disabled={status?.isRunning}>Boot PM2 Daemon Cluster</button>
                   <button className="btn btn-stop" onClick={handleSysStop} disabled={!status?.isRunning}>Emergency Halt All</button>
                 </div>
                 
                 <div className="form-group" style={{marginTop: '1.5rem'}}>
                    <label className="form-label">Global Throttle Sleep Interval (ms)</label>
                    <input className="form-input" type="number" value={sleepInterval} onChange={e => updateGlobalStrategy('sleep_interval', parseInt(e.target.value))} />
                 </div>
              </div>

              <div className="panel queue-panel">
                 <h2 className="panel-header">Daemon Network Status</h2>
                 <p className="text-secondary" style={{fontSize: '0.8rem', paddingBottom:'0.5rem'}}>Targeting: NATIONWIDE | Pipeline Engine Status</p>
                 <div style={{ background: 'rgba(0,0,0,0.2)', padding: '1rem', borderRadius: '8px' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.5rem' }}>
                       <span>Operator Daemon:</span>
                       <strong style={{ color: status?.currentTarget?.includes('Operator: online') ? '#4caf50' : '#ff5a00' }}>{status?.currentTarget?.includes('Operator: online') ? 'ONLINE' : 'OFFLINE'}</strong>
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.5rem' }}>
                       <span>Indexer Daemon:</span>
                       <strong style={{ color: status?.currentTarget?.includes('Indexer: online') ? '#4caf50' : '#ff5a00' }}>{status?.currentTarget?.includes('Indexer: online') ? 'ONLINE' : 'OFFLINE'}</strong>
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.5rem', opacity: 0.5 }}>
                       <span>Specialist Daemon:</span>
                       <strong>MOCKED</strong>
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', opacity: 0.5 }}>
                       <span>Photographer Daemon:</span>
                       <strong>MOCKED</strong>
                    </div>
                 </div>
              </div>
            </div>
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
                        <td>
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

      {/* Global Terminal Wrapper - Persists across all tabs */}
      <div className="log-panel panel">
        <div className="log-header">
           <h2 className="panel-header" style={{margin:0}}>Omni-Terminal (Real-time Logs)</h2>
           <button className="btn-mini" onClick={fetchHistory}>PERSISTENT HISTORY</button>
        </div>
        <div className="log-container" ref={logsRef}>
          {logs.map((log, i) => (
            <div key={i} className={`log-entry ${log.type === 'ERROR' ? 'error' : ''}`}>
              <span className="log-timestamp">[{new Date().toLocaleTimeString()}]</span> {log.message}
            </div>
          ))}
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


