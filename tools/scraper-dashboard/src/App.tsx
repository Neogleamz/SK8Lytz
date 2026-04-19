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

  return (
    <div className="dashboard-container">
      <header className="header" style={{ alignItems: 'flex-start' }}>
        <div>
          <h1 className="title">SK8Lytz Skate Spots</h1>
          <div className="tab-bar">
            <button className={`tab-btn ${activeTab === 'phase1' ? 'active' : ''}`} onClick={() => setActiveTab('phase1')}>Phase 1: OSM Intake</button>
            <button className={`tab-btn ${activeTab === 'phase2' ? 'active' : ''}`} onClick={() => setActiveTab('phase2')}>Phase 2: Google Enrichment</button>
            <button className={`tab-btn ${activeTab === 'phase3' ? 'active' : ''}`} onClick={() => setActiveTab('phase3')}>Phase 3: Databank QA</button>
          </div>
        </div>
        <div style={{display: 'flex', gap: '1rem', marginTop: '0.5rem'}}>
          <div className={`status-badge ${status?.isHarvestingActive ? 'status-online' : 'status-offline'}`}>
               PHASE 1 HARVEST {status?.isHarvestingActive ? '● ON' : '○ OFF'}
          </div>
          <div className={`status-badge ${status?.isRunning ? 'status-online' : 'status-offline'}`}>
               PHASE 2 ENRICHMENT {status?.isRunning ? '● ON' : '○ OFF'}
          </div>
        </div>
      </header>

      {/* =========== GLOBAL SECURITY CONTROL BAR (Shared by Phase 1 & 2) =========== */}
      {(activeTab === 'phase1' || activeTab === 'phase2') && (
        <div className="security-control-bar fade-in">
          <div className="security-label">
            <span className="ghost-badge" style={{margin:0}}>🛡️ GHOST ENCRYPTED</span>
            <strong>GLOBAL SECURITY TACTICS</strong>
          </div>
          
          <div className="security-inputs">
            <div className="input-group-inline">
              <label>Cooldown Base</label>
              <input type="number" className="mini-input" value={cooldownBase} onChange={e => updateGlobalStrategy('cooldown_base_ms', parseInt(e.target.value))} />
              <span>ms</span>
            </div>
            <div className="input-group-inline">
              <label>Jitter</label>
              <input type="number" className="mini-input" value={cooldownJitter} onChange={e => updateGlobalStrategy('cooldown_jitter_pct', parseInt(e.target.value))} />
              <span>%</span>
            </div>
            
            <div className="v-divider"></div>

            <label className="security-switch">
              <span>Identity Rotation</span>
              <label className="switch mini">
                <input type="checkbox" checked={identityRotation} onChange={e => updateGlobalStrategy('identity_rotation_enabled', e.target.checked)} />
                <span className="slider round"></span>
              </label>
            </label>

            <label className="security-switch">
              <span>Random Viewports</span>
              <label className="switch mini">
                <input type="checkbox" checked={randomizeViewport} onChange={e => updateGlobalStrategy('randomize_viewport_enabled', e.target.checked)} />
                <span className="slider round"></span>
              </label>
            </label>

            <label className="security-switch">
              <span>Auto-Resume</span>
              <label className="switch mini">
                <input type="checkbox" checked={autoResume} onChange={e => updateGlobalStrategy('auto_resume_enabled', e.target.checked)} />
                <span className="slider round"></span>
              </label>
            </label>
          </div>
        </div>
      )}

      {/* =========== PHASE 1: GLOBAL STRATEGY & INTAKE =========== */}
      {activeTab === 'phase1' && (() => {
        const totalNodes = Object.values(harvestData.stateCounts).reduce((a, b) => a + b, 0);
        return (
          <div className="tab-pane phase-1 fade-in">
            <div className="explainer-block">
            <h3 style={{marginTop: 0, color: 'var(--primary-color)'}}>Phase 1: OpenStreetMap (OSM) Extraction</h3>
            <div className="ghost-badge">🛡️ GHOST ENCRYPTED PIPELINE ACTIVE</div>
            <p>This engine interfaces directly with OpenStreetMap's Overpass API. It isolates geographical boundaries for the targeted US State and extracts raw, unverified polygons based on your strategy toggles (e.g. leisure=skatepark, sport=roller_skating). Upon extraction, the backend pipeline runs reverse-geocoding over raw GPS coordinate data to establish a sterile, structural record in the Supabase Databank. No human concepts like "vibes" or "pro shops" are gathered at this phase—this step solely establishes ground truth location data.</p>
            </div>
            
            <div className="hero-grid">
              <div className="hero-card">
                <h3 className="card-title">Total Extracted Nodes</h3>
                <p className="card-value">{totalNodes.toLocaleString()}</p>
              </div>
              <div className="hero-card">
                <h3 className="card-title">Enriched Nodes</h3>
                <p className="card-value">{harvestData.seededStates.length} / 50</p>
              </div>
              <div className="hero-card" style={{ gridColumn: 'span 2' }}>
                <h3 className="card-title">Harvester Engine Block</h3>
                <p className="card-value card-target">
                  {status?.isHarvestingActive ? (stateOverride.length > 0 ? `Harvesting Target: ${stateOverride.join(', ')}...` : 'Running National Sequence...') : 'Idle'}
                </p>
              </div>
            </div>

            <div className="omni-grid tri-grid">
              {/* Column 1: Control & Facilities */}
              <div className="controls-section">
                <div className="panel intake-panel" style={{marginBottom: '1rem'}}>
                  <h2 className="panel-header">Execution</h2>
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

              {/* Column 2: State Selection */}
              <div className="panel strategy-panel">
                <h2 className="panel-header">2. Select State Target</h2>
                <div className="state-pill-container" style={{maxHeight: '400px'}}>
                   <button 
                      className={`state-pill ${stateOverride.length === 0 ? 'active' : ''}`}
                      onClick={() => updateGlobalStrategy('state_override', 'ALL')}
                   >
                     ALL STATES
                   </button>
                   {US_STATES.map(st => {
                      const count = harvestData.stateCounts[st] || 0;
                      return (
                        <button 
                          key={st} 
                          className={`state-pill ${stateOverride.includes(st) ? 'active' : ''} ${status?.isHarvestingActive && stateOverride.includes(st) ? 'pulse-border' : ''}`}
                          onClick={() => updateGlobalStrategy('state_override', st)}
                        >
                          {st} {count > 0 && <span className="pill-dot"></span>}
                        </button>
                      )
                   })}
                </div>
              </div>

              {/* Column 3: Coverage Leaderboard */}
              <div className="panel coverage-panel">
                <h2 className="panel-header">3. Coverage Leaderboard</h2>
                <div className="coverage-list">
                   {coverageStats.slice(0, 10).map(stat => {
                      const enrichedPct = Math.min(100, Math.round((stat.enriched / stat.total) * 100) || 0);
                      const verifiedPct = Math.min(100, Math.round((stat.verified / stat.total) * 100) || 0);
                      return (
                        <div key={stat.state} className="coverage-item">
                           <div className="coverage-meta">
                              <strong>{stat.state}</strong>
                              <span>{stat.total}</span>
                           </div>
                           <div className="coverage-progress-bg">
                              <div className="progress-bar enriched" style={{width: `${enrichedPct}%`}}></div>
                              <div className="progress-bar verified" style={{width: `${verifiedPct}%`}}></div>
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

      {/* =========== PHASE 2: CULTURAL ENRICHMENT =========== */}
      {activeTab === 'phase2' && (
        <div className="tab-pane phase-2 fade-in">
          <div className="explainer-block">
            <h3 style={{marginTop: 0, color: 'var(--primary-color)'}}>Phase 2: Google Maps Cultural Enrichment</h3>
            <div className="ghost-badge">🛡️ GHOST IDENTITY SPOOFING ACTIVE</div>
            <p>The Cultural Enrichment Daemon operates as a persistent queue-worker utilizing a headless Chromium instance via Puppeteer. Upon activation, it continuously polls the Databank for "UNVERIFIED" geographical records created during Phase 1. Bypassing rigid APIs, the robot navigates raw internet search algorithms to emulate human discovery. It scrapes business logic seeking "adult nights", analyzes live user reviews to calculate a median Vibe Rating, and verifies true operational capacity. Culturally enriched data is then written back to Supabase.</p>
          </div>

          <div className="hero-grid">
            <div className="hero-card">
              <h3 className="card-title">Processed Attempts</h3>
              <p className="card-value">{status?.processedCount || 0}</p>
            </div>
            <div className="hero-card">
              <h3 className="card-title">Enriched Nodes</h3>
              <p className="card-value" style={{ color: 'var(--primary-color)' }}>{status?.enrichedCount || 0}</p>
            </div>
            <div className="hero-card">
              <h3 className="card-title">Verified (Gold)</h3>
              <p className="card-value" style={{ color: 'var(--success)' }}>{status?.verifiedCount || 0}</p>
            </div>
            <div className="hero-card">
              <h3 className="card-title">Errors Encountered</h3>
              <p className="card-value" style={{ color: 'var(--danger)' }}>{status?.errorCount || 0}</p>
            </div>
            <div className="hero-card" style={{ gridColumn: 'span 2' }}>
              <h3 className="card-title">Current Engine Block</h3>
              <p className="card-value card-target">{status?.currentTarget || 'Waiting...'}</p>
              {status?.lastError && <div className="error-box"><p className="error-text">⚠️ {status.lastError}</p></div>}
            </div>
          </div>
          
          <div className="omni-grid" style={{display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginTop: '1rem'}}>
            <div className="panel enrichment-panel">
               <h2 className="panel-header">GHOST Security Profile</h2>
               <div className="ghost-info-card">
                  <div className="info-row"><strong>Global Mode:</strong> <span style={{color: 'var(--primary-color)'}}>ACTIVE ✅</span></div>
                  <div className="info-row"><strong>Phase 1 (OSM):</strong> <span style={{color: 'var(--primary-color)'}}>Spoofed / Synced</span></div>
                  <div className="info-row"><strong>Phase 2 (Google):</strong> <span style={{color: 'var(--primary-color)'}}>Spoofed / Synced</span></div>
                  <hr style={{borderColor: 'rgba(255,255,255,0.1)', margin: '15px 0'}} />
                  <div className="info-row"><strong>Active Agent:</strong> <span className="mono-text">{identityRotation ? 'Rotating...' : 'Default (Legacy)'}</span></div>
                  <div className="info-row"><strong>Resolution:</strong> <span className="mono-text">{randomizeViewport ? 'Randomized' : 'Fixed 1280x800'}</span></div>
               </div>
            </div>

            <div className="controls-section">
              <div className="panel">
                <h2 className="panel-header">Execution Control</h2>
                <div className="btn-group">
                  <button className="btn btn-start" onClick={handleSysStart} disabled={status?.isRunning}>Force Start Engine</button>
                  <button className="btn btn-stop" onClick={handleSysStop} disabled={!status?.isRunning}>Halt Process</button>
                </div>
                <div className="switch-row" style={{marginTop: '1.5rem'}}>
                  <span>Crawler Headless Mode</span>
                  <label className="switch">
                    <input type="checkbox" checked={isHeadless} onChange={e => toggleHeadless(e.target.checked)} />
                    <span className="slider round"></span>
                  </label>
                </div>
                <div className="form-group" style={{marginTop: '1.5rem'}}>
                  <label className="form-label">Throttle Sleep Interval (ms)</label>
                  <input className="form-input" type="number" value={sleepInterval} onChange={e => updateGlobalStrategy('sleep_interval', parseInt(e.target.value))} />
                </div>
              </div>

              <div className="panel" style={{marginTop: '1rem'}}>
                <h2 className="panel-header" style={{color: 'var(--success)'}}>Engine Config</h2>
                <div className="form-group">
                  <label className="form-label" style={{marginBottom: '0.5rem', display: 'block'}}>Strike Threshold (Circuit Breaker)</label>
                  <select className="form-input" value={maxStrikes} onChange={e => updateGlobalStrategy('max_consecutive_errors', parseInt(e.target.value))}>
                    <option value={1}>1 Strike (Paranoid)</option>
                    <option value={3}>3 Strikes (Balanced)</option>
                    <option value={5}>5 Strikes (Aggressive)</option>
                    <option value={10}>10 Strikes (Dangerous)</option>
                  </select>
                  <p className="text-secondary" style={{fontSize: '0.8rem', marginTop: '0.5rem'}}>
                    Number of consecutive cultural scraping failures before the engine enters GATED safety mode.
                  </p>
                </div>
              </div>
            </div>

            <div className="panel queue-panel">
               <h2 className="panel-header">Next Up (Queue Preview)</h2>
               <p className="text-secondary" style={{fontSize: '0.8rem', paddingBottom:'0.5rem'}}>Targeting: {stateOverride || 'NATIONWIDE'} | Facilities: {targetFacilities.length ? targetFacilities.join(', ') : 'ALL'}</p>
               <div className="queue-list">
                 {queueSpots.length === 0 && <p className="text-secondary">Queue empty for current strategy.</p>}
                 {queueSpots.map(s => (
                   <div key={s.id} className="queue-item">
                     <strong>{s.name}</strong>
                     <span className="queue-meta">{s.city}, {s.state} · {s.facility_type}</span>
                   </div>
                 ))}
               </div>
            </div>
          </div>
        </div>
      )}

      {/* =========== PHASE 3: DATABANK =========== */}
      {activeTab === 'phase3' && (
        <div className="tab-pane graveyard fade-in">
          <div className="explainer-block" style={{marginBottom: '1rem'}}>
            <h3 style={{marginTop: 0, color: 'var(--primary-color)'}}>Phase 3: Human Verification & Quality Assurance</h3>
            <p>This represents the final staging server before datasets are exposed to the live SK8Lytz userbase. Because autonomous machine scraping encounters edge cases (e.g., closed businesses, incorrect geo-fencing), Phase 3 acts as the master review grid. Administrators can monitor the exact output of the Phase 2 bot, execute precise inline modifications, flag garbage data as "REJECTED" to prevent future scrapers from touching it, or authorize clean rows into "VERIFIED" status.</p>
          </div>

          <div className="grid-toolbar">
            <input className="form-input" style={{width: '250px'}} placeholder="Search Location / City / State..." value={searchQuery} onChange={e => setSearchQuery(e.target.value)} />
             <select className="form-input filter-dropdown" value={gridFilter} onChange={e => setGridFilter(e.target.value)}>
                <option value="ALL">All Validations</option>
                <option value="PENDING">Pending (Queue)</option>
                <option value="ENRICHED">Enriched (Partial)</option>
                <option value="VERIFIED">Verified Culturally</option>
                <option value="REJECTED">Graveyard / Rejected</option>
                <option value="DEPRECATED">Deprecated (Tombstone)</option>
             </select>
            <button className="btn-primary" onClick={bulkPromote}>🚀 Bulk Promote</button>
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
                  <th onClick={() => toggleSort('facility_type')} style={{cursor:'pointer'}}>Type {sortCol==='facility_type' ? (sortDir==='asc'?'↑':'↓') : ''}</th>
                  <th onClick={() => toggleSort('verification_status')} style={{cursor:'pointer'}}>Status {sortCol==='verification_status' ? (sortDir==='asc'?'↑':'↓') : ''}</th>
                  <th>Vibe</th>
                  <th>Surface</th>
                  <th onClick={() => toggleSort('website')} style={{cursor:'pointer'}}>Website {sortCol==='website' ? (sortDir==='asc'?'↑':'↓') : ''}</th>
                  <th onClick={() => toggleSort('phone_number')} style={{cursor:'pointer'}}>Phone {sortCol==='phone_number' ? (sortDir==='asc'?'↑':'↓') : ''}</th>
                  <th onClick={() => toggleSort('has_pro_shop')} style={{cursor:'pointer'}}>Shop {sortCol==='has_pro_shop' ? (sortDir==='asc'?'↑':'↓') : ''}</th>
                   <th onClick={() => toggleSort('has_adult_night')} style={{cursor:'pointer'}}>18+ {sortCol==='has_adult_night' ? (sortDir==='asc'?'↑':'↓') : ''}</th>
                   <th onClick={() => toggleSort('retry_count')} style={{cursor:'pointer'}}>Retries {sortCol==='retry_count' ? (sortDir==='asc'?'↑':'↓') : ''}</th>
                   <th onClick={() => toggleSort('last_enriched_at')} style={{cursor:'pointer'}}>Enriched At {sortCol==='last_enriched_at' ? (sortDir==='asc'?'↑':'↓') : ''}</th>
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
                        {isEditing ? <input className="table-input" value={editForm.facility_type} onChange={e => setEditForm({...editForm, facility_type: e.target.value})} /> : row.facility_type}
                      </td>
                      <td>
                        {isEditing ? (
                          <select className="table-input" value={editForm.verification_status || 'PENDING'} onChange={e => setEditForm({...editForm, verification_status: e.target.value})}>
                            <option value="PENDING">PENDING</option>
                            <option value="ENRICHED">ENRICHED</option>
                            <option value="VERIFIED">VERIFIED</option>
                            <option value="DEPRECATED">DEPRECATED</option>
                            <option value="REJECTED">REJECTED</option>
                          </select>
                        ) : (
                          <span className={`status-pill ${row.verification_status?.toLowerCase() || 'pending'}`}>
                            {row.verification_status || 'UNVERIFIED'}
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
                       <td>{isEditing ? <input type="checkbox" checked={editForm.has_pro_shop} onChange={e => setEditForm({...editForm, has_pro_shop: e.target.checked})} /> : (row.has_pro_shop ? '✅' : '❌')}</td>
                       <td>{isEditing ? <input type="checkbox" checked={editForm.has_adult_night} onChange={e => setEditForm({...editForm, has_adult_night: e.target.checked})} /> : (row.has_adult_night ? '✅' : '❌')}</td>
                       <td>
                         {isEditing ? (
                           <input type="number" className="table-input" value={editForm.retry_count || 0} onChange={e => setEditForm({...editForm, retry_count: parseInt(e.target.value) || 0})} />
                         ) : (
                           <span style={{ color: row.retry_count >= 8 ? 'var(--danger)' : 'inherit' }}>{row.retry_count || 0}/10</span>
                         )}
                       </td>
                      <td style={{fontSize: '0.75rem', color: 'var(--text-secondary)'}}>
                        {row.last_enriched_at ? new Date(row.last_enriched_at).toLocaleString() : 'Never'}
                      </td>
                      <td>
                        <div className="action-row">
                          {row.is_published ? (
                             <button className="btn-icon" onClick={() => promoteSpot(row.id, false)} title="Unpublish from Map">🛑</button>
                          ) : (
                             <button className="btn-icon" onClick={() => promoteSpot(row.id, true)} disabled={row.verification_status !== 'VERIFIED'} title="Promote to Map">🚀</button>
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
      {/* Global Terminal Wrapper - Persists across all tabs */}
      <div className="log-panel panel">
        <div className="log-header">
           <h2 className="panel-header" style={{margin:0}}>Omni-Terminal (Real-time)</h2>
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
