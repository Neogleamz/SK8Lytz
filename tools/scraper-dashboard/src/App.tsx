import { useState, useEffect, useRef } from 'react';
import USAMap from './USMap';
import ScraperPipeline from './components/ScraperPipeline';
import DetectiveLab from './DetectiveLab';
import { SniperBench } from './components/SniperBench';
import './App.css';

const API_BASE = 'http://localhost:5999';

// Route external photo URLs through CCTower proxy to avoid referrer/CORS blocks on localhost
const proxyImg = (url: string | null) => {
  if (!url) return null;
  // Supabase CDN URLs are already ours — serve directly
  if (url.includes('supabase')) return url;
  // Everything else (googleapis Street View, etc.) goes through the proxy
  return `${API_BASE}/api/img-proxy?url=${encodeURIComponent(url)}`;
};

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
  const [activeTab, setActiveTab] = useState<'pipeline' | 'phase1' | 'phase2' | 'phase3' | 'phase4' | 'sniper' | 'graveyard'>('pipeline');
  const [seedProvider, setSeedProvider] = useState<'osm'|'google'>('google');
  const [resettingIds, setResettingIds] = useState<Record<string, 'loading' | 'success'>>({});


  // --- Sys Dashboard States ---
  const [status, setStatus] = useState<any>(null);
  const [targetFacilities, setTargetFacilities] = useState<string[]>([]);
  const [stateOverride, setStateOverride] = useState<string[]>([]);
  const [pipelineStats, setPipelineStats] = useState<{ summary: any; stats: any[] } | null>(null);

  // --- Collapsible sections: persisted to localStorage ---
  const [collapsedSections, setCollapsedSections] = useState<Record<string, boolean>>(() => {
    try { return JSON.parse(localStorage.getItem('sk8_collapsed') || '{}'); } catch { return {}; }
  });
  const toggleSection = (key: string) => setCollapsedSections(prev => {
    const next = { ...prev, [key]: !prev[key] };
    localStorage.setItem('sk8_collapsed', JSON.stringify(next));
    return next;
  });
  const isCollapsed = (key: string) => collapsedSections[key] === true;
  // Helper: clickable section header bar
  const SectionHdr = ({ id, label, color = 'rgba(255,255,255,0.5)', right }: { id: string; label: React.ReactNode; color?: string; right?: React.ReactNode }) => (
    <div onClick={() => toggleSection(id)} style={{ display:'flex', alignItems:'center', justifyContent:'space-between', cursor:'pointer', userSelect:'none', padding:'6px 0', marginBottom: isCollapsed(id) ? 0 : '0.5rem' }}>
      <span style={{ color, fontWeight:800, fontSize:'0.85rem', textTransform:'uppercase', letterSpacing:'0.05em' }}>{label}</span>
      <span style={{ display:'flex', alignItems:'center', gap:'10px' }}>
        {right}
        <span style={{ fontSize:'0.65rem', color:'rgba(255,255,255,0.25)', fontWeight:700 }}>{isCollapsed(id) ? '▼ show' : '▲ hide'}</span>
      </span>
    </div>
  );
  const [sleepInterval, setSleepInterval] = useState<number>(5000);
  const [isHeadless, setIsHeadless] = useState<boolean>(true);
  const [identityRotation, setIdentityRotation] = useState<boolean>(true);
  const [randomizeViewport, setRandomizeViewport] = useState<boolean>(true);
  
  // --- Evasion Tactics States ---
  const [cooldownBase, setCooldownBase] = useState<number>(300000);
  const [cooldownJitter, setCooldownJitter] = useState<number>(20);
  const [maxStrikes, setMaxStrikes] = useState<number>(3);
  const [autoResume, setAutoResume] = useState<boolean>(true);

  // --- AI Config States ---
  const [aiSystemPrompt, setAiSystemPrompt] = useState<string>('');
  const [aiExclusionKeywords, setAiExclusionKeywords] = useState<string[]>([]);
  const [aiTargetVectors, setAiTargetVectors] = useState<any[]>([]);

  // --- Logs & Queue States ---
  const [logs, setLogs] = useState<{type: string, message: string, source?: string}[]>([]);
  const logsRef = useRef<HTMLDivElement>(null);
  const activeTabRef = useRef<typeof activeTab>('phase1');
  const stateOverrideRef = useRef<string[]>([]); // mirrors stateOverride but readable in stale closures
  const fetchPipelineStatsRef = useRef<() => void>(() => {}); // ref to latest fetchPipelineStats

  // --- Harvest Manager States ---
  const [harvestData, setHarvestData] = useState<{seededStates: string[], stateCounts: Record<string, number>, allStates: string[]}>({ seededStates: [], stateCounts: {}, allStates: [] });
  const [coverageStats, setCoverageStats] = useState<any[]>([]);
  const [databankCoverage, setDatabankCoverage] = useState<any[]>([]);
  const [historyLogs, setHistoryLogs] = useState<string[]>([]);

  // --- Databank Map Mode ---
  const [mapMode, setMapMode] = useState<'quality' | 'published'>('quality');
  const [activeStateFilter, setActiveStateFilter] = useState<string | null>(null);

  // --- Dynamic Blocklist UI ---
  const [uiBlocklist, setUiBlocklist] = useState<any[]>([]);
  const [newKeyword, setNewKeyword] = useState('');

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

  // --- Phase 6 Filter Chips (server-side) ---
  const [chips, setChips] = useState<Record<string,boolean>>({
    has_photos: false, has_hours: false, has_website: false,
    has_adult_night: false, has_pro_shop: false, is_published: false,
    is_deep_crawled: false,
  });
  const [stateChip, setStateChip] = useState('');

  useEffect(() => {
    fetchSystemStatus();
    fetchBlocklist();      // Initial load of keywords
    fetchQueue();          // Full initial load — all phases
    fetchHarvestStatus();
    fetchHistory();
    fetchCoverage();
    fetchPipelineStatsRef.current(); // Initial Region Pulse load
    
    const interval = setInterval(() => {
      fetchSystemStatus();
      fetchCoverage();
      // Only re-fetch queue for the currently visible tab (not all 6 every 5s)
      // When viewing the pipeline belt, refresh all phase queues so every belt has live data
      const phasesToRefresh = activeTabRef.current === 'pipeline'
        ? ['phase1', 'phase2', 'phase3', 'phase4', 'detective-recent', 'recent']
        : [activeTabRef.current, 'recent'];
      fetchQueue(phasesToRefresh);

    }, 5000);

    // Region Pulse refreshes on a slower 15s cycle (heavier query)
    const statsInterval = setInterval(() => {
      fetchPipelineStatsRef.current();
    }, 15000);

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
      clearInterval(statsInterval);
      es.close();
    };
  }, []);

  // Keep activeTabRef in sync so the polling interval always reads current tab
  useEffect(() => { activeTabRef.current = activeTab; }, [activeTab]);
  // Keep stateOverrideRef in sync so stale closures (setInterval) always read live value
  useEffect(() => { stateOverrideRef.current = stateOverride; }, [stateOverride]);

  // Re-fetch queues immediately when active region priority changes
  useEffect(() => {
    fetchQueue();
  }, [stateOverride]); // eslint-disable-line react-hooks/exhaustive-deps

  // Re-fetch pipeline health stats when region changes
  const fetchPipelineStats = async () => {
    const activeStates = stateOverrideRef.current; // use ref — safe in stale useEffect
    const statesParam = activeStates.length > 0 ? `?states=${activeStates.join(',')}` : '';
    try {
      const res = await fetch(`${API_BASE}/api/pipeline-stats${statesParam}`);
      const data = await res.json();
      if (data.summary) setPipelineStats(data);
    } catch (e) { console.error('pipeline-stats fetch error:', e); }
  };
  // Keep fetchPipelineStatsRef in sync so the polling interval always calls the latest version
  useEffect(() => { fetchPipelineStatsRef.current = fetchPipelineStats; });
  useEffect(() => { fetchPipelineStats(); }, [stateOverride]); // eslint-disable-line react-hooks/exhaustive-deps


  useEffect(() => {
    if (logsRef.current) {
      logsRef.current.scrollTop = logsRef.current.scrollHeight;
    }
  }, [logs]);

  useEffect(() => {
    if (activeTab === 'phase1') {
      fetchDatabankCoverage(); // Phase 1 map uses same source — Google record density per state
    }
    
  }, [activeTab, gridFilter, sortCol, sortDir, searchQuery, chips, stateChip]);

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
            setAiSystemPrompt(config.ai_system_prompt || '');
            setAiExclusionKeywords(config.ai_exclusion_keywords || []);
            setAiTargetVectors(config.ai_target_vectors || []);
         }
      }
    } catch {
      setStatus({ isRunning: false, currentTarget: 'API OFFLINE', processedCount: 0, enrichedCount: 0, publishedCount: 0, errorCount: 0, lastError: 'Could not connect.' });
    }
  };

  const fetchBlocklist = async () => {
    try {
      const res = await fetch(`${API_BASE}/api/scraper/blocklist`);
      const data = await res.json();
      if (data.keywords) setUiBlocklist(data.keywords);
    } catch (e) {}
  };

  const addKeyword = async () => {
    console.log('[DEBUG] addKeyword clicked. newKeyword:', newKeyword);
    if (!newKeyword.trim()) {
      alert('Please enter a keyword to block.');
      return;
    }
    console.log('[DEBUG] Sending fetch request for:', newKeyword);
    try {
      const res = await fetch(`${API_BASE}/api/scraper/blocklist`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ keyword: newKeyword })
      });
      const data = await res.json();
      if (data.success) {
        setNewKeyword('');
        fetchBlocklist();
        // Immediately refresh the grid to show records were dropped
        fetchSpots(page, gridFilter);
        fetchPipelineStatsRef.current();
        alert(`Successfully blocked "${newKeyword}" and purged ${data.count} matching records from the database!`);
      } else {
        alert('Failed to add keyword: ' + JSON.stringify(data));
      }
    } catch (e: any) {
      alert('Error connecting to CCTower: ' + e.message);
    }
  };

  const removeKeyword = async (kw: string) => {
    if (!confirm(`Remove "${kw}" from blocklist?`)) return;
    try {
      await fetch(`${API_BASE}/api/scraper/blocklist/${encodeURIComponent(kw)}`, { method: 'DELETE' });
      fetchBlocklist();
    } catch (e) {}
  };

  const [phaseQueues, setPhaseQueues] = useState<Record<string, any[]>>({});

  const fetchQueue = async (only?: string[]) => {
    // Phases now: phase1, phase3 (Detective), phase4 (Photographer), phase6 (Publisher)
    const phasesToFetch = only ?? ['phase1', 'phase2', 'phase3', 'phase4', 'detective-recent', 'recent'];
    // Read from ref so this always has the live value even inside stale interval closures
    const activeStates = stateOverrideRef.current;
    const statesParam = activeStates.length > 0 ? `&states=${activeStates.join(',')}` : '';
    try {
      const results = await Promise.all(
         phasesToFetch.map(phase =>
            phase === 'recent'
              ? fetch(`${API_BASE}/api/recent-spots${statesParam ? '?' + statesParam.slice(1) : ''}`).then(r => r.json())
              : fetch(`${API_BASE}/api/queue?phase=${phase}${statesParam}`).then(r => r.json())
         )
      );
      // Merge into existing state so unloaded phases retain last known values
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

  const fetchSpots = async (pageIdx: number, filter: string, col = sortCol, dir = sortDir, search = searchQuery, activeChips = chips, activeState = stateChip) => {
    try {
      const offset = pageIdx * rowsPerPage;
      const chipParams = Object.entries(activeChips)
        .filter(([, v]) => v)
        .map(([k]) => `${k}=true`)
        .join('&');
      const stateParam = activeState.length === 2 ? `&state=${activeState.toUpperCase()}` : '';
        const plStatus = activeTab === 'graveyard' ? '&pipeline_status=REJECTED' : '';
        const url = `${API_BASE}/api/spots?limit=${rowsPerPage}&offset=${offset}&status=${filter}&sortCol=${col}&sortDir=${dir}&search=${encodeURIComponent(search)}${stateParam}${chipParams ? '&' + chipParams : ''}${plStatus}`;
      const res = await fetch(url);
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
     } else if (field === 'ai_system_prompt') {
       setAiSystemPrompt(value);
     } else if (field === 'ai_exclusion_keywords') {
       setAiExclusionKeywords(value);
     } else if (field === 'ai_target_vectors') {
       setAiTargetVectors(value);
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
          randomize_viewport_enabled: field === 'randomize_viewport_enabled' ? value : randomizeViewport,
          ai_system_prompt: field === 'ai_system_prompt' ? value : aiSystemPrompt,
          ai_exclusion_keywords: field === 'ai_exclusion_keywords' ? value : aiExclusionKeywords,
          ai_target_vectors: field === 'ai_target_vectors' ? value : aiTargetVectors
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

  const restoreSpot = async (id: string, name: string) => {
    if (!confirm(`Restore record "${name}" to PENDING status?`)) return;
    try {
      await fetch(`${API_BASE}/api/spots/${id}`, { 
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ pipeline_status: 'PENDING' })
      });
      fetchSpots(page, gridFilter);
    } catch (e) {}
  };

  const deleteSpot = async (id: string, name: string) => {
    if (!confirm(`PERMANENTLY delete record "${name}"?`)) return;
    try {
      await fetch(`${API_BASE}/api/spots/${id}`, { method: 'DELETE' });
      fetchSpots(page, gridFilter);
    } catch (e) {}
  };

  // Reset a single spot back to SEEDED so it re-enters the Detective queue
  const resetSpotToSeeded = async (id: string, name: string) => {
    if (!confirm(`Reset "${name}" back to SEEDED? The AI Detective will re-crawl it.`)) return;
    setResettingIds(prev => ({ ...prev, [id]: 'loading' }));
    try {
      await fetch(`/api/spots/`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ verification_status: 'SEEDED', retry_count: 0, last_attempted_at: null })
      });
      setResettingIds(prev => ({ ...prev, [id]: 'success' }));
      setTimeout(() => {
        setResettingIds(prev => { const next = {...prev}; delete next[id]; return next; });
        fetchSpots(page, gridFilter);
      }, 1500);
    } catch (e) {
      setResettingIds(prev => { const next = {...prev}; delete next[id]; return next; });
    }
  };

  // Bulk reset all filtered records back to SEEDED (respects global state + facility filters)
  const [isBulkResetting, setIsBulkResetting] = useState(false);
  const bulkResetToSeeded = async () => {
    const stateLabel = stateOverride.length > 0 ? stateOverride.join(', ') : 'ALL STATES';
    const facLabel = targetFacilities.length > 0 ? targetFacilities.join(', ') : 'ALL TYPES';
    if (!confirm(`Reset ALL records back to SEEDED?\n\nFilters: ${stateLabel} / ${facLabel}\n\nThis will clear retry counts and requeue everything for the AI Detective.`)) return;
    setIsBulkResetting(true);
    try {
      const res = await fetch(`${API_BASE}/api/bulk-reset-to-seeded`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ states: stateOverride, facility_types: targetFacilities })
      });
      const data = await res.json();
      if (data.success) {
        alert(`✅ Reset ${data.reset_count} records to SEEDED.\nFilters: ${stateLabel} / ${facLabel}`);
        fetchSpots(page, gridFilter);
      } else {
        alert(`Error: ${data.error}`);
      }
    } catch (e) {
      alert('Bulk reset failed — check CCTower logs.');
    } finally {
      setIsBulkResetting(false);
    }
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
      fetchPipelineStatsRef.current();
    } catch (e) {}
  };

  const bulkPromote = async () => {
    if (!confirm('Promote ALL ENRICHED and MEDIA_READY records to the public Skate Map?')) return;
    try {
      await fetch(`${API_BASE}/api/promote-all`, { method: 'POST' });
      alert('Bulk promotion complete!');
      fetchSpots(page, gridFilter);
      fetchDatabankCoverage();
      fetchPipelineStatsRef.current();
    } catch (e) {}
  };

  const promoteState = async (state: string) => {
    if (!state || state.length !== 2) return;
    if (!confirm(`Publish ALL eligible records in ${state} to the live app map?`)) return;
    try {
      const res = await fetch(`${API_BASE}/api/promote-state/${state}`, { method: 'POST' });
      const data = await res.json();
      alert(` Published ${data.promoted ?? 0} records in ${state}!`);
      fetchSpots(page, gridFilter);
      fetchDatabankCoverage();
      fetchPipelineStatsRef.current();
    } catch (e) {}
  };

  const unpublishState = async (state: string) => {
    if (!state || state.length !== 2) return;
    if (!confirm(`️ Retract ALL published records in ${state} from the live app map?`)) return;
    try {
      const res = await fetch(`${API_BASE}/api/unpublish-state/${state}`, { method: 'POST' });
      const data = await res.json();
      alert(`Retracted ${data.unpublished ?? 0} records in ${state}.`);
      fetchSpots(page, gridFilter);
      fetchDatabankCoverage();
      fetchPipelineStatsRef.current();
    } catch (e) {}
  };

  

  // ── REGION PULSE — single source of truth, used in both tab layouts ──
  const regionPulseEl = pipelineStats ? (() => {
    const s = pipelineStats.summary;
    const rows = pipelineStats.stats || [];
    const label = stateOverride.length > 0 ? stateOverride.join(' · ') : 'NATIONWIDE';
    const C = { scout: '#00ffaa', detective: '#ff6a00', photo: '#ff007f', pub: '#00d4ff' };
    const row = (k: string, v: any, color = '#fff') => (
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'baseline' }}>
        <span style={{ fontSize: '0.62rem', color: 'rgba(255,255,255,0.4)' }}>{k}</span>
        <span style={{ fontSize: '0.7rem', fontWeight: 800, color }}>{typeof v === 'number' ? v.toLocaleString() : v}</span>
      </div>
    );
    return (
      <div style={{ background: 'rgba(0,0,0,0.35)', border: '1px solid rgba(255,255,255,0.06)', overflow: 'hidden' }}>
        <div onClick={() => toggleSection('pulse')} style={{ display: 'flex', alignItems: 'center', gap: '10px', padding: '5px 14px', borderBottom: isCollapsed('pulse') ? 'none' : '1px solid rgba(255,255,255,0.05)', background: 'rgba(255,255,255,0.03)', cursor: 'pointer', userSelect: 'none' as const }}>
          <span style={{ fontSize: '0.56rem', fontWeight: 900, color: 'rgba(255,255,255,0.3)', textTransform: 'uppercase' as const, letterSpacing: '0.12em' }}>Region Pulse</span>
          <span style={{ fontSize: '0.65rem', fontWeight: 800, color: '#8a2be2' }}>{label}</span>
          <span style={{ fontSize: '0.55rem', color: 'rgba(255,255,255,0.2)', marginLeft: 'auto' }}>{s.total?.toLocaleString()} records · {isCollapsed('pulse') ? '▼' : '▲'}</span>
        </div>
        {!isCollapsed('pulse') && (
          <>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '1px', background: 'rgba(255,255,255,0.04)' }}>
              {/* ① SCOUT — IN: PENDING  OUT: SEEDED */}
              <div style={{ background: 'rgba(12,12,20,0.95)', padding: '8px 12px' }}>
                <div style={{ fontSize: '0.57rem', fontWeight: 900, color: C.scout, textTransform: 'uppercase' as const, letterSpacing: '0.08em', marginBottom: '5px', borderBottom: `1px solid ${C.scout}33`, paddingBottom: '4px' }}>① Scout  IN:PENDING → OUT:SEEDED</div>
                <div style={{ display: 'flex', flexDirection: 'column' as const, gap: '3px' }}>
                  {row('Total in DB', s.total, '#fff')}
                  {row('SEEDED (awaiting Det.)', s.seeded, C.scout)}
                  {row('Has Website', s.has_website, 'rgba(255,255,255,0.6)')}
                  {row('No Website', (s.total||0)-(s.has_website||0), 'rgba(255,255,255,0.25)')}
                </div>
              </div>
              {/* ② DETECTIVE — IN: SEEDED  OUT: DEEP_CRAWLED */}
              <div style={{ background: 'rgba(12,12,20,0.95)', padding: '8px 12px' }}>
                <div style={{ fontSize: '0.57rem', fontWeight: 900, color: C.detective, textTransform: 'uppercase' as const, letterSpacing: '0.08em', marginBottom: '5px', borderBottom: `1px solid ${C.detective}33`, paddingBottom: '4px' }}>② Detective  IN:SEEDED → OUT:DEEP_CRAWLED</div>
                <div style={{ display: 'flex', flexDirection: 'column' as const, gap: '3px' }}>
                  {row('AI Queue (SEEDED+site)', s.detective_queue, s.detective_queue > 0 ? '#ffb300' : 'rgba(255,255,255,0.3)')}
                  {row('AI Done (DEEP_CRAWLED)', s.deep_crawled_count, C.detective)}
                  {row('Has Photo Candidates', s.has_candidates, 'rgba(255,255,255,0.6)')}
                  {row('No Candidates', (s.deep_crawled_count||0)-(s.has_candidates||0), 'rgba(255,255,255,0.25)')}
                </div>
              </div>
              {/* ③ PHOTOGRAPHER — IN: DEEP_CRAWLED  OUT: MEDIA_READY */}
              <div style={{ background: 'rgba(12,12,20,0.95)', padding: '8px 12px' }}>
                <div style={{ fontSize: '0.57rem', fontWeight: 900, color: C.photo, textTransform: 'uppercase' as const, letterSpacing: '0.08em', marginBottom: '5px', borderBottom: `1px solid ${C.photo}33`, paddingBottom: '4px' }}>③ Photographer  IN:DEEP_CRAWLED → OUT:MEDIA_READY</div>
                <div style={{ display: 'flex', flexDirection: 'column' as const, gap: '3px' }}>
                  {row('Photo Queue (DEEP_CRAWLED)', s.photographer_queue, s.photographer_queue > 0 ? '#ffb300' : 'rgba(255,255,255,0.3)')}
                  {row('Photo Candidates', s.has_candidates, 'rgba(255,255,255,0.6)')}
                  {row('Photographed', s.has_photos, '#fff')}
                  {row('MEDIA_READY', s.media_ready, C.photo)}
                </div>
              </div>
              {/* ④ PUBLISHER — IN: MEDIA_READY  OUT: PUBLISHED */}
              <div style={{ background: 'rgba(12,12,20,0.95)', padding: '8px 12px' }}>
                <div style={{ fontSize: '0.57rem', fontWeight: 900, color: C.pub, textTransform: 'uppercase' as const, letterSpacing: '0.08em', marginBottom: '5px', borderBottom: `1px solid ${C.pub}33`, paddingBottom: '4px' }}>④ Publisher  IN:MEDIA_READY → OUT:PUBLISHED</div>
                <div style={{ display: 'flex', flexDirection: 'column' as const, gap: '3px' }}>
                  {row('Pub Queue (MEDIA_READY)', s.publisher_queue, s.publisher_queue > 0 ? '#ffb300' : C.pub)}
                  {row('Live on App', s.published, '#4ade80')}
                  {row('Pipeline %', `${s.total > 0 ? Math.round((s.deep_crawled_count/s.total)*100) : 0}%`, 'rgba(255,255,255,0.5)')}
                  {row('Published %', `${s.total > 0 ? Math.round((s.published/s.total)*100) : 0}%`, '#4ade80')}
                </div>
              </div>
            </div>
            {/* Per-state chips */}
            {rows.length > 1 && (
              <div style={{ padding: '5px 14px 7px', borderTop: '1px solid rgba(255,255,255,0.04)' }}>
                <div style={{ fontSize: '0.54rem', fontWeight: 900, color: 'rgba(255,255,255,0.18)', textTransform: 'uppercase' as const, letterSpacing: '0.1em', marginBottom: '4px' }}>Per State</div>
                <div style={{ display: 'flex', gap: '5px', flexWrap: 'wrap' as const }}>
                  {rows.slice(0, 20).map((r: any) => (
                    <div key={r.state} style={{ display: 'inline-flex', gap: '4px', alignItems: 'center', padding: '2px 7px', borderRadius: '8px', background: 'rgba(255,255,255,0.04)', border: '1px solid rgba(255,255,255,0.06)' }}>
                      <span style={{ fontSize: '0.64rem', fontWeight: 800, color: 'rgba(255,255,255,0.6)' }}>{r.state}</span>
                      <span style={{ fontSize: '0.58rem', color: '#00ffaa' }} title="Total">{r.total}</span>
                      {r.detective_queue  > 0 && <span style={{ fontSize: '0.58rem', color: '#ff6a00' }} title="AI queue">🔎{r.detective_queue}</span>}
                      {r.photographer_queue > 0 && <span style={{ fontSize: '0.58rem', color: '#ff007f' }} title="Photo queue">📸{r.photographer_queue}</span>}
                      {r.publisher_queue > 0 && <span style={{ fontSize: '0.58rem', color: '#00d4ff' }} title="Publisher queue">🚀{r.publisher_queue}</span>}
                      {r.published        > 0 && <span style={{ fontSize: '0.58rem', color: '#4ade80' }} title="Published">✓{r.published}</span>}
                    </div>
                  ))}
                </div>
              </div>
            )}
          </>
        )}
      </div>
    );
  })() : null;

  const headerControls = (
    <>
      {/* Logo */}
      <div style={{ display: 'flex', flexDirection: 'column', lineHeight: 1.1, flexShrink: 0, marginRight: '6px' }}>
        <span style={{ fontSize: '0.95rem', fontWeight: 900, background: 'linear-gradient(90deg,#8a2be2,#e91e63)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent', letterSpacing: '-0.02em' }}>SK8Lytz</span>
        <span style={{ fontSize: '0.52rem', fontWeight: 700, color: 'rgba(255,255,255,0.3)', textTransform: 'uppercase', letterSpacing: '0.1em' }}>SK8Spotz</span>
      </div>
      <div style={{ width: 1, height: 22, background: 'rgba(255,255,255,0.08)', flexShrink: 0 }} />
      {/* Daemon pills */}
      {[
        { id: 'operator',     label: 'Phase 2: Spider',    color: '#9d4edd', onKey: 'Operator: online' },
        { id: 'indexer',      label: 'Phase 3: Detective', color: '#ff5a00', onKey: 'Indexer: online' },
        { id: 'photographer', label: 'Phase 4: Photo',     color: '#e91e63', onKey: 'Photographer: online' },
        { id: 'ollama-daemon',label: 'Ollama Engine',      color: '#00d4ff', onKey: 'Ollama: online' },
      ].map(d => {
        const isOn = status?.currentTarget?.includes(d.onKey);
        return (
          <div key={d.id} style={{ display: 'flex', alignItems: 'center', gap: '4px', background: isOn ? `${d.color}12` : 'rgba(255,255,255,0.03)', padding: '3px 8px', borderRadius: '20px', border: `1px solid ${isOn ? d.color + '55' : 'rgba(255,255,255,0.08)'}`, flexShrink: 0 }}>
            <div className={`status-dot ${isOn ? 'online' : 'offline'}`} style={{ background: isOn ? d.color : '', boxShadow: isOn ? `0 0 5px ${d.color}` : '', width: '6px', height: '6px' }} />
            <span style={{ fontSize: '0.6rem', color: isOn ? d.color : 'rgba(255,255,255,0.25)', fontWeight: 700 }}>{d.label.toUpperCase()}</span>
            <button onClick={() => triggerSpecificDaemon(d.id, isOn ? 'stop' : 'start')}
              style={{ fontSize: '0.58rem', fontWeight: 800, padding: '1px 5px', borderRadius: '8px', border: 'none', cursor: 'pointer', background: isOn ? 'rgba(255,60,60,0.25)' : `${d.color}33`, color: isOn ? '#ff6b6b' : d.color }}>
              {isOn ? 'STOP' : 'GO'}
            </button>
          </div>
        );
      })}
      <div style={{ width: 1, height: 22, background: 'rgba(255,255,255,0.08)', flexShrink: 0 }} />
      {/* Active Region chips */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '4px', flexShrink: 0 }}>
        <span style={{ fontSize: '0.56rem', fontWeight: 800, color: '#8a2be2', textTransform: 'uppercase', letterSpacing: '0.08em' }}>Region:</span>
        <button onClick={() => updateGlobalStrategy('state_override', 'ALL')}
          style={{ fontSize: '0.58rem', fontWeight: 700, padding: '2px 7px', borderRadius: '10px', border: 'none', cursor: 'pointer',
            background: stateOverride.length === 0 ? '#8a2be2' : 'rgba(255,255,255,0.06)',
            color: stateOverride.length === 0 ? '#fff' : 'rgba(255,255,255,0.3)' }}>ALL</button>
        {stateOverride.map(st => (
          <button key={st} onClick={() => updateGlobalStrategy('state_override', st)}
            style={{ fontSize: '0.6rem', fontWeight: 800, padding: '2px 7px', borderRadius: '10px', border: 'none', cursor: 'pointer', background: '#8a2be2', color: '#fff' }}
          >{st} x</button>
        ))}
        <select 
          value="" 
          onChange={(e) => {
            const st = e.target.value;
            if (st) updateGlobalStrategy('state_override', st);
          }}
          style={{ 
            fontSize: '0.58rem', padding: '2px 5px', borderRadius: '10px', 
            border: '1px dashed rgba(138,43,226,0.4)', background: 'transparent', 
            color: '#8a2be2', cursor: 'pointer', outline: 'none' 
          }}
        >
          <option value="" disabled>+</option>
          {['AL','AK','AZ','AR','CA','CO','CT','DE','FL','GA','HI','ID','IL','IN','IA','KS','KY','LA','ME','MD','MA','MI','MN','MS','MO','MT','NE','NV','NH','NJ','NM','NY','NC','ND','OH','OK','OR','PA','RI','SC','SD','TN','TX','UT','VT','VA','WA','WV','WI','WY'].filter(s => !stateOverride.includes(s)).map(st => (
            <option key={st} value={st} style={{ background: '#111', color: '#fff' }}>{st}</option>
          ))}
        </select>
      </div>
      <div style={{ width: 1, height: 22, background: 'rgba(255,255,255,0.08)', flexShrink: 0 }} />
      {/* Target Facilities */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '4px', flexShrink: 0 }}>
        <span style={{ fontSize: '0.56rem', fontWeight: 800, color: '#00ffaa', textTransform: 'uppercase', letterSpacing: '0.08em' }}>Targets:</span>
        {['roller_rink', 'skate_park', 'skate_shop'].map(fac => {
          const isActive = targetFacilities?.includes(fac);
          return (
            <button key={fac} onClick={() => updateGlobalStrategy('target_facilities', fac)}
              style={{ fontSize: '0.58rem', fontWeight: 700, padding: '2px 7px', borderRadius: '10px', border: isActive ? '1px solid #00ffaa' : '1px solid rgba(255,255,255,0.1)', cursor: 'pointer',
                background: isActive ? 'rgba(0,255,170,0.15)' : 'rgba(255,255,255,0.02)',
                color: isActive ? '#00ffaa' : 'rgba(255,255,255,0.4)' }}>{fac.replace('_', ' ').toUpperCase()}</button>
          );
        })}
      </div>
      <div style={{ width: 1, height: 22, background: 'rgba(255,255,255,0.08)', flexShrink: 0 }} />
      {/* Stealth toggles */}
      <label style={{ display: 'flex', alignItems: 'center', gap: '4px', cursor: 'pointer', flexShrink: 0 }}>
        <span style={{ fontSize: '0.58rem', color: 'rgba(255,255,255,0.35)', fontWeight: 700 }}>Headless</span>
        <label className="switch mini"><input type="checkbox" checked={isHeadless} onChange={e => toggleHeadless(e.target.checked)} /><span className="slider round" /></label>
      </label>
      <label style={{ display: 'flex', alignItems: 'center', gap: '4px', cursor: 'pointer', flexShrink: 0 }}>
        <span style={{ fontSize: '0.58rem', color: 'rgba(255,255,255,0.35)', fontWeight: 700 }}>Spoof</span>
        <label className="switch mini"><input type="checkbox" checked={identityRotation} onChange={e => updateGlobalStrategy('identity_rotation_enabled', e.target.checked)} /><span className="slider round" /></label>
      </label>
      <div style={{ width: 1, height: 22, background: 'rgba(255,255,255,0.08)', flexShrink: 0 }} />
      {/* Timing */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '4px', flexShrink: 0 }}>
        <span style={{ fontSize: '0.56rem', color: 'rgba(255,255,255,0.3)' }}>Cooldown</span>
        <input type="number" className="mini-input" style={{ width: '58px' }} value={cooldownBase} onChange={e => updateGlobalStrategy('cooldown_base_ms', parseInt(e.target.value))} />
        <span style={{ fontSize: '0.54rem', color: 'rgba(255,255,255,0.18)' }}>ms</span>
        <span style={{ fontSize: '0.56rem', color: 'rgba(255,255,255,0.3)', marginLeft: '3px' }}>Jitter</span>
        <input type="number" className="mini-input" style={{ width: '40px' }} value={cooldownJitter} onChange={e => updateGlobalStrategy('cooldown_jitter_pct', parseInt(e.target.value))} />
        <span style={{ fontSize: '0.54rem', color: 'rgba(255,255,255,0.18)' }}>%</span>
        <span style={{ fontSize: '0.56rem', color: 'rgba(255,255,255,0.3)', marginLeft: '3px' }}>Throttle</span>
        <input type="number" className="mini-input" style={{ width: '58px' }} value={sleepInterval} onChange={e => updateGlobalStrategy('sleep_interval', parseInt(e.target.value))} />
        <span style={{ fontSize: '0.54rem', color: 'rgba(255,255,255,0.18)' }}>ms</span>
      </div>
      {/* Power — pushed right */}
      <div style={{ marginLeft: 'auto', display: 'flex', gap: '6px', flexShrink: 0, alignItems: 'center' }}>
        <button className="btn-icon" onClick={() => setActiveTab('pipeline')} title="Factory Floor"
          style={{ background: activeTab === 'pipeline' ? 'rgba(0, 255, 170, 0.2)' : 'rgba(255,255,255,0.05)', color: activeTab === 'pipeline' ? '#00ffaa' : 'rgba(255,255,255,0.6)', border: activeTab === 'pipeline' ? '1px solid #00ffaa' : '1px solid transparent', padding: '4px 8px', borderRadius: '6px', marginRight: '4px', cursor: 'pointer', fontSize: '0.65rem', fontWeight: 800 }}>
          FACTORY FLOOR
        </button>
        {/* Bulk Reset to SEEDED — respects global state + facility filters */}
                <button
          onClick={bulkResetToSeeded}
          disabled={isBulkResetting}
          title={`Reset all ${stateOverride.length > 0 ? stateOverride.join('/') : 'ALL'} ${targetFacilities.length > 0 ? targetFacilities.join('/') : 'ALL TYPE'} records back to SEEDED`}
          style={{ background: isBulkResetting ? 'rgba(255, 179, 0, 0.4)' : 'rgba(255, 179, 0, 0.15)', color: isBulkResetting ? '#fff' : '#ffb300', border: '1px solid rgba(255, 179, 0, 0.3)', padding: '4px 10px', borderRadius: '6px', cursor: isBulkResetting ? 'not-allowed' : 'pointer', fontSize: '0.8rem', fontWeight: 800, textTransform: 'uppercase', display: 'flex', alignItems: 'center', gap: '6px' }}
        >
          {isBulkResetting ? '? RESETTING...' : '?? FORCE RESET TO SEEDED'}
        </button>
        <button className="btn-icon" onClick={() => setActiveTab('sniper')} title="Sniper Bench"
          style={{ background: activeTab === 'sniper' ? 'rgba(255, 106, 0, 0.2)' : 'rgba(255,255,255,0.05)', color: activeTab === 'sniper' ? '#ff6a00' : 'rgba(255,255,255,0.6)', border: activeTab === 'sniper' ? '1px solid #ff6a00' : '1px solid transparent', padding: '4px 8px', borderRadius: '6px', marginRight: '4px', cursor: 'pointer', fontSize: '0.85rem' }}>
          🔫
        </button>
        <button className="btn-icon" onClick={() => setActiveTab('graveyard')} title="Garbage Can (Rejected & Purged)"
          style={{ background: activeTab === 'graveyard' ? 'rgba(244, 67, 54, 0.2)' : 'rgba(255,255,255,0.05)', color: activeTab === 'graveyard' ? '#f44336' : 'rgba(255,255,255,0.6)', border: activeTab === 'graveyard' ? '1px solid #f44336' : '1px solid transparent', padding: '4px 8px', borderRadius: '6px', marginRight: '4px', cursor: 'pointer', fontSize: '0.85rem' }}>
          🗑️
        </button>
        <button className="btn btn-start" onClick={handleSysStart} disabled={status?.isRunning}
          style={{ padding: '4px 12px', fontSize: '0.62rem', fontWeight: 800 }}>BOOT ALL</button>
        <button className="btn btn-stop" onClick={handleSysStop} disabled={!status?.isRunning}
          style={{ padding: '4px 12px', fontSize: '0.62rem', fontWeight: 800 }}>HALT ALL</button>
      </div>
    </>
  );

  return (
    <div className="dashboard-container">
      {/* ======= COMPACT STICKY COMMAND BAR ======= */}
      {activeTab !== 'pipeline' && (
        <div style={{ display: 'flex', gap: '8px', alignItems: 'center', flexWrap: 'wrap', padding: '8px 20px', background: 'rgba(12,12,20,0.97)', borderBottom: '1px solid rgba(138,43,226,0.2)', position: 'sticky', top: 0, zIndex: 50, backdropFilter: 'blur(10px)' }}>
          {headerControls}
        </div>
      )}

      

      {/* ======= REGION PULSE: 5-phase live breakdown ======= */}
      {activeTab !== 'pipeline' && regionPulseEl && (
        <div style={{ margin: '0 0 1rem 0', borderRadius: '10px', overflow: 'hidden' }}>
          {regionPulseEl}
        </div>
      )}

      {activeTab === 'pipeline' && (
        <div className="fade-in w-full">
           <ScraperPipeline
              headerControls={headerControls}
              belowHeader={regionPulseEl ?? undefined}
              pipelineStats={pipelineStats}
              phaseQueues={phaseQueues}
              onPhaseNav={(tab) => setActiveTab(tab as any)}
              status={status}
              triggerSpecificDaemon={triggerSpecificDaemon}
              triggerHarvest={triggerHarvest}
            />
        </div>
      )}

      {activeTab !== 'pipeline' && (
      <>
      <div className="content-area fade-in">
        {/* =========== PHASE 1: GLOBAL STRATEGY & INTAKE =========== */}
        {activeTab === 'phase1' && (
          <div className="tab-pane phase-1">
            <div className="explainer-block" style={{marginBottom: '1rem'}}>
              <SectionHdr id="phase1_explainer" label="The Scout: GIS Ingestion Engine" color="#8a2be2" />
              {!isCollapsed('phase1_explainer') && <p>Phase 1 uses the <strong style={{color:'#ffb300'}}>Google Places API</strong> to seed the entire pipeline — querying by state for roller rinks and skate shops. Each result is written directly as an <strong>ENRICHED</strong> record with full coordinates, phone, hours, rating, and website. The OSM fallback mode is available for legacy re-harvests but is <em style={{color:'rgba(255,255,255,0.4)'}}>not recommended</em> — Google data is always higher quality.</p>}
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
               <h3 style={{ marginTop: 0, color: '#fff', fontSize: 18 }}> State Re-Sweep Controls</h3>
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

            {/* DYNAMIC BLOCKLIST UI */}
            <div className="dynamic-blocklist-panel" style={{ background: 'linear-gradient(135deg, rgba(233,30,99,0.1) 0%, rgba(0,0,0,0.3) 100%)', padding: '1.5rem', borderRadius: '12px', border: '1px solid rgba(233, 30, 99, 0.3)', marginBottom: '2rem', position: 'relative', zIndex: 50 }}>
               <div style={{ position: 'absolute', top: '-10px', right: '20px', background: '#e91e63', color: '#fff', fontSize: '0.6rem', padding: '2px 8px', borderRadius: '4px', fontWeight: 800 }}>LIVE GUILLOTINE</div>
               <h3 style={{ marginTop: 0, color: '#fff', fontSize: 18 }}>Dynamic Retail Blocklist</h3>
               <p style={{ fontSize: '0.8rem', color: 'rgba(255,255,255,0.6)', marginBottom: '1.5rem' }}>Add toxic keywords here. Submitting instantly blacklists the term for future Google Sweeps AND executes a SQL Guillotine to delete any currently matching records in the database.</p>
               
               <div style={{ display: 'flex', gap: '8px', marginBottom: '1.5rem' }}>
                 <input className="form-input" placeholder="e.g. 'trek', 'hockey', 'camping'"
                   value={newKeyword} onChange={e => setNewKeyword(e.target.value)}
                   onKeyDown={e => e.key === 'Enter' && addKeyword()}
                   style={{ flex: 1, zIndex: 50 }} />
                 <button className="btn-primary" style={{ background: '#e91e63', border: 'none', cursor: 'pointer', zIndex: 50 }} onClick={addKeyword}>Block & Purge Database</button>
               </div>

               <div style={{ background: 'rgba(0,0,0,0.4)', borderRadius: '8px', border: '1px solid rgba(255,255,255,0.05)', padding: '1rem', maxHeight: '180px', overflowY: 'auto' }}>
                 {uiBlocklist.length === 0 ? (
                   <div style={{ fontSize: '0.75rem', color: 'rgba(255,255,255,0.3)', textAlign: 'center' }}>No dynamic keywords configured.</div>
                 ) : (
                   <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px' }}>
                     {uiBlocklist.map((item: any) => (
                       <div key={item.keyword} style={{ display: 'flex', alignItems: 'center', gap: '6px', background: 'rgba(233,30,99,0.15)', border: '1px solid rgba(233,30,99,0.3)', padding: '4px 10px', borderRadius: '20px' }}>
                         <span style={{ fontSize: '0.75rem', fontWeight: 600, color: '#f48fb1' }}>{item.keyword}</span>
                         <button onClick={() => removeKeyword(item.keyword)} style={{ background: 'none', border: 'none', color: 'rgba(255,255,255,0.5)', cursor: 'pointer', fontSize: '0.7rem', padding: '0 4px' }}>x</button>
                       </div>
                     ))}
                   </div>
                 )}
               </div>
            </div>

            <div className="omni-grid tri-grid phase-1-controls" style={{ display: 'grid', gap: '1.5rem', gridTemplateColumns: 'minmax(250px, 1fr) 1.5fr', marginBottom: '2rem' }}>
               <div className="facility-switches-panel" style={{ background: 'rgba(255,255,255,0.02)', padding: '1.5rem', borderRadius: '12px', border: '1px solid rgba(255,255,255,0.05)' }}>
                  <h3 style={{ fontSize: '0.9rem', textTransform: 'uppercase', color: 'var(--text-secondary)', marginBottom: '1.5rem', marginTop: 0 }}>Target Facilities</h3>
                  <div className="facility-switches">
                     {['roller_rink', 'skate_park', 'skate_shop'].map(f => (
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
                <h2 className="panel-header" style={{ margin: 0 }}>Google Places Coverage — State Density</h2>
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
                          const tier = row.total >= 100 ? { color: '#4caf50', label: '' }
                            : row.total >= 50 ? { color: '#ff6b00', label: '' }
                            : { color: '#f5a623', label: '' };
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
                                >{stateOverride.includes(row.state) ? ' TARGETED' : 'TARGET'}</button>
                              </td>
                            </tr>
                          );
                        })}
                    </tbody>
                  </table>
                </div>
              )}
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
                              {spot.verification_status === 'ENRICHED' ? ' ENRICHED (Google)' : '⏳ RAW SEED'}
                            </span>
                          </div>
                        </div>
                      ))}
                    </div>
                )}
            </div>
          </div>
        )}

        {/* =========== DAEMON CONTROL CENTER (PHASE 2-4) =========== */}
        {(['phase2', 'phase3', 'phase4'].includes(activeTab)) && (
          <div className="tab-pane daemon-center">
             <div className="explainer-block" style={{marginBottom: '1rem'}}>
               <SectionHdr
                 id={`daemon_explainer_${activeTab}`}
                 label={<span style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <button className="btn-mini" onClick={(e) => { e.stopPropagation(); setActiveTab('pipeline'); }} style={{ background: '#00ffaa', color: '#000', border: 'none' }}>← BACK TO FACTORY</button>
                    Phase Databank
                 </span>}
                 color={'#fff'}
               />
               {!isCollapsed(`daemon_explainer_${activeTab}`) && (
                 <>
                   
                   {activeTab === 'phase2' && <p>The AI Detective visits the <strong>website</strong> collected by the Indexer in Phase 1 — no regex crawl needed. Runs <strong>Ollama Llama-3</strong> across the combined page text to extract hours, adult night schedules, pricing, events, social links, and photo candidates. Promotes records to <strong>INDEXED</strong>.</p>}
                   {activeTab === 'phase3' && <p>The Photographer daemon reads <code>candidate_photos</code> written by the Indexer — downloading OG images and DOM media as binary uploads to Supabase Storage. Falls back to Google Street View Static as a guaranteed photo source. Promotes records to <strong>MEDIA_READY</strong> on success.</p>}
                   {activeTab === 'phase4' && <p>The Publisher Gate is the final human-approved release step. Only records with <strong style={{color:'#4caf50'}}>is_published = true</strong> are visible on the live SK8Lytz app map. Bulk-promote all pipeline-complete records (ENRICHED + MEDIA_READY) below, or use the Databank QA tab to approve individual spots.</p>}
                 </>
               )}
             </div>
             
             

             
             {activeTab === 'phase2' && (
                <div className="flow-visualizer" style={{ padding: '3rem 2rem', background: 'rgba(0,0,0,0.2)', borderRadius: '12px', marginTop: '1rem' }}>
                  <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '2rem' }}>
                    {/* input: SEEDED feeds into Detective */}
                    <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem', alignItems: 'flex-end', minWidth: '120px' }}>
                      <div style={{ textAlign: 'center' }}>
                        <div style={{ fontSize: '1.8rem', fontWeight: 800, color: '#00ffaa' }}>{status?.seededCount || 0}</div>
                        <div style={{ fontSize: '0.7rem', color: 'var(--text-secondary)' }}>SEEDED</div>
                      </div>
                    </div>
                    <div style={{ flex: 1, height: '4px', background: 'rgba(255,255,255,0.1)', position: 'relative' }}>
                       <div style={{ position: 'absolute', top: '-40px', left: '50%', transform: 'translateX(-50%)', display: 'flex', gap: '10px' }}>
                          <button className="btn-mini start" onClick={() => triggerSpecificDaemon('indexer', 'start')} disabled={status?.currentTarget?.includes('Indexer: online')}>▶ Phase 2: Detective (Indexer)</button>
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


                          {activeTab === 'phase2' && (
                <div style={{ marginTop: '2rem' }}>
                  <DetectiveLab
                    aiSystemPrompt={aiSystemPrompt}
                    setAiSystemPrompt={setAiSystemPrompt}
                    aiTargetVectors={aiTargetVectors}
                    setAiTargetVectors={setAiTargetVectors}
                    aiExclusionKeywords={aiExclusionKeywords}
                    setAiExclusionKeywords={setAiExclusionKeywords}
                    updateGlobalStrategy={updateGlobalStrategy}
                  />
                </div>
             )}

                          {activeTab === 'phase3' && (
                <div className="flow-visualizer" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '2rem', padding: '3rem 2rem', background: 'rgba(0,0,0,0.2)', borderRadius: '12px', marginTop: '1rem' }}>
                   <div style={{ textAlign: 'center', minWidth: '100px' }}>
                      <div style={{ fontSize: '2.5rem', fontWeight: 800, color: '#e91e63' }}>{status?.candidatesReadyCount || 0}</div>
                      <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>CANDIDATES</div>
                   </div>
                   <div style={{ flex: 1, height: '4px', background: 'rgba(255,255,255,0.1)', position: 'relative' }}>
                      <div style={{ position: 'absolute', top: '-40px', left: '50%', transform: 'translateX(-50%)', display: 'flex', gap: '10px' }}>
                         <button className="btn-mini start" onClick={() => triggerSpecificDaemon('photographer', 'start')} disabled={status?.currentTarget?.includes('Photographer: online')}>▶ Phase 3: Photographer</button>
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

                          {activeTab === 'phase4' && (
                <div style={{ padding: '3rem 2rem', background: 'rgba(0,0,0,0.2)', borderRadius: '12px', marginTop: '1rem' }}>
                  <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '4rem', marginBottom: '2rem' }}>
                    <div style={{ textAlign: 'center' }}>
                      <div style={{ fontSize: '3rem', fontWeight: 800, color: '#e91e63' }}>{status?.mediaReadyCount || 0}</div>
                      <div style={{ fontSize: '0.9rem', color: 'var(--text-secondary)' }}>MEDIA_READY</div>
                    </div>
                    <div style={{ textAlign: 'center' }}>
                      <div style={{ fontSize: '3rem', fontWeight: 800, color: '#ff5a00' }}>{status?.deepCrawledCount || 0}</div>
                      <div style={{ fontSize: '0.9rem', color: 'var(--text-secondary)' }}>DEEP_CRAWLED</div>
                    </div>
                    <div style={{ textAlign: 'center' }}>
                      <div style={{ fontSize: '3rem', fontWeight: 800, color: '#4caf50' }}>{status?.publishedCount || 0}</div>
                      <div style={{ fontSize: '0.9rem', color: 'var(--text-secondary)' }}>LIVE ON MAP</div>
                    </div>
                  </div>
                  <div style={{ textAlign: 'center', padding: '1.5rem', background: 'rgba(76,175,80,0.05)', border: '1px solid rgba(76,175,80,0.3)', borderRadius: '8px' }}>
                    <p style={{ margin: '0 0 1rem', color: 'rgba(255,255,255,0.7)', fontSize: '0.9rem' }}>The Publisher Gate controls which records are visible to users on the SK8Lytz app map. Use the Databank QA tab below to review individual records and toggle <strong style={{color:'#4caf50'}}>APP_LIVE</strong>, or bulk-promote all ready records.</p>
                    <button className="btn btn-start" style={{background: '#4caf50', border: 'none'}} onClick={bulkPromote}> BULK PUBLISH ALL READY → APP MAP</button>
                  </div>
                </div>
             )}

             {/* Mini Data Bank & Evasion Audit */}
             {(['phase2', 'phase3', 'phase4'].includes(activeTab)) && (() => {
                const queue = phaseQueues[activeTab] || [];
                let hydratingFields: string[] = [];
                if (activeTab === 'phase2') hydratingFields = ['Pricing', 'Adult Night', 'Hours', ' Photo Candidates'];
                if (activeTab === 'phase3') hydratingFields = ['OG Photo', 'DOM Images', 'Street View', 'Facebook OG'];
                if (activeTab === 'phase4') hydratingFields = ['Media URLs', 'Thumbnails'];

                const activeLabel = `Phase ${activeTab.replace('phase','')}`;
                const activeColor = '#fff';

                return (
                  <div style={{marginTop: '20px'}}>

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

        {/* =========== PHASE 4: DATABANK QA =========== */}
        {['phase4', 'graveyard'].includes(activeTab) && (
            <div className="tab-pane graveyard fade-in">
              <div className="explainer-block" style={{marginBottom: '1rem', background: activeTab === 'graveyard' ? 'rgba(244, 67, 54, 0.05)' : 'rgba(76, 175, 80, 0.05)', border: `1px solid ${activeTab === 'graveyard' ? 'rgba(244, 67, 54, 0.2)' : 'rgba(76, 175, 80, 0.2)'}`}}>
                <h3 style={{marginTop: 0, color: activeTab === 'graveyard' ? '#f44336' : '#4caf50'}}>
                  {activeTab === 'graveyard' ? 'Graveyard: Rejected & Purged Records' : 'Phase 4: Databank QA & Live Publish'}
                </h3>
                <p>{activeTab === 'graveyard' 
                    ? 'Review records that were blocked by the Guillotine or manually rejected. These will not be published.'
                    : 'Final review before publication. Filter and inspect records, then publish state-by-state or individually. Use the view toggle to switch between Card, List, and Table views.'}
                </p>
              </div>


             {/* =========== STATUS COVERAGE MAP =========== */}
              {activeTab !== 'graveyard' && (
              <div style={{ marginBottom: '2rem', padding: '1.5rem', background: 'rgba(255,179,0,0.03)', border: '1px solid rgba(255,179,0,0.2)', borderRadius: '12px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: isCollapsed('coverage_map') ? 0 : '0.5rem' }}>
                <SectionHdr id="coverage_map" label="Pipeline Coverage Map" color="#ffb300" right={
                  !isCollapsed('coverage_map') && (
                    <div style={{ display: 'flex', gap: '6px', background: 'rgba(255,255,255,0.05)', padding: '4px', borderRadius: '8px' }} onClick={e => e.stopPropagation()}>
                      <button onClick={() => setMapMode('quality')} style={{ padding: '5px 14px', borderRadius: '6px', border: 'none', cursor: 'pointer', fontSize: '0.75rem', fontWeight: 700, background: mapMode === 'quality' ? '#ffb300' : 'transparent', color: mapMode === 'quality' ? '#000' : 'rgba(255,255,255,0.5)' }}>Quality</button>
                      <button onClick={() => setMapMode('published')} style={{ padding: '5px 14px', borderRadius: '6px', border: 'none', cursor: 'pointer', fontSize: '0.75rem', fontWeight: 700, background: mapMode === 'published' ? '#4caf50' : 'transparent', color: mapMode === 'published' ? '#000' : 'rgba(255,255,255,0.5)' }}>Published</button>
                    </div>
                  )
                } />
              </div>
              {!isCollapsed('coverage_map') && (<>
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
                  ['PENDING','ENRICHED','MEDIA_READY'].forEach(s => {
                    if (row[s]) totals[s] = (totals[s] || 0) + row[s];
                  });
                  totalPublished += (row.published || 0);
                });
                const totalRecords = databankCoverage.reduce((a: number, r: any) => a + (r.total || 0), 0);
                const STATUS_META: Record<string, {color: string; label: string}> = {
                  PENDING:     { color: '#8a2be2', label: 'Pending' },
                  ENRICHED:    { color: '#ff9800', label: 'Enriched' },
                  MEDIA_READY: { color: '#e91e63', label: 'Media Ready' },
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
                              <span style={{ fontSize: '0.75rem', color: '#4caf50', fontWeight: 700 }}> Published</span>
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
                      const STATUS_PRIORITY = ['MEDIA_READY','ENRICHED','PENDING'];
                      const STATUS_COLOR: Record<string, string> = {
                        MEDIA_READY: '#e91e63', ENRICHED: '#ff9800', PENDING: '#8a2be2',
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
              </>)}

            </div>
            )}

            {/* ======= DATABANK GRID ======= */}
            <div style={{ marginBottom: '1rem' }}>
              <SectionHdr id="databank_grid" label="Databank QA Grid" color="rgba(255,255,255,0.6)" />
            </div>
            {!isCollapsed('databank_grid') && <div>
              {/* Row 1: View toggle + Search + Status + Sort */}
              <div style={{ display: 'flex', gap: '8px', alignItems: 'center', flexWrap: 'wrap', marginBottom: '8px' }}>
                {/* View Mode Toggle */}
                <div style={{ display: 'flex', gap: '3px', background: 'rgba(255,255,255,0.05)', padding: '3px', borderRadius: '8px', flexShrink: 0 }}>
                  {(['card', 'list', 'table'] as const).map(mode => (
                    <button key={mode} onClick={() => setViewMode(mode)}
                      style={{ padding: '5px 11px', borderRadius: '6px', border: 'none', cursor: 'pointer', fontSize: '0.68rem', fontWeight: 700, textTransform: 'uppercase',
                        background: viewMode === mode ? '#8a2be2' : 'transparent',
                        color: viewMode === mode ? '#fff' : 'rgba(255,255,255,0.35)' }}
                    >{mode === 'card' ? 'Cards' : mode === 'list' ? 'List' : 'Table'}</button>
                  ))}
                </div>
                {/* Search */}
                <input className="form-input search-bar" style={{ flex: 1, minWidth: '160px' }}
                  placeholder="Search name / city / state..."
                  value={searchQuery}
                  onChange={e => { setSearchQuery(e.target.value); setActiveStateFilter(e.target.value.trim().length === 2 ? e.target.value.trim().toUpperCase() : null); }}
                />
                {/* Status filter */}
                <select className="form-input" style={{ width: 'auto', fontSize: '0.75rem' }} value={gridFilter} onChange={e => setGridFilter(e.target.value)}>
                  <option value="ALL">All Status</option>
                  <option value="SEEDED">SEEDED</option>
                  <option value="DEEP_CRAWLED">DEEP_CRAWLED</option>
                  <option value="MEDIA_READY">MEDIA_READY</option>
                  <option value="ON_HOLD">ON_HOLD</option>
                  <option value="REJECTED">REJECTED</option>
                </select>
                {/* State filter */}
                <input className="form-input" style={{ width: '68px', fontSize: '0.75rem', textTransform: 'uppercase', textAlign: 'center' }}
                  placeholder="State" maxLength={2}
                  value={stateChip}
                  onChange={e => setStateChip(e.target.value.toUpperCase())}
                />
                {/* Sort col */}
                <select className="form-input" style={{ width: 'auto', fontSize: '0.75rem' }}
                  value={sortCol}
                  onChange={e => { setSortCol(e.target.value); fetchSpots(0, gridFilter, e.target.value, sortDir); }}
                >
                  <option value="last_attempted_at">Recent</option>
                  <option value="rating">Rating DESC</option>
                  <option value="user_ratings_total">Reviews</option>
                  <option value="name">Name A-Z</option>
                  <option value="state">State</option>
                  <option value="city">City</option>
                  <option value="verification_status">Status</option>
                  <option value="last_enriched_at">Enriched Date</option>
                </select>
                <button onClick={() => { const nd = sortDir === 'asc' ? 'desc' : 'asc'; setSortDir(nd); fetchSpots(0, gridFilter, sortCol, nd); }}
                  style={{ padding: '6px 10px', borderRadius: '6px', border: '1px solid rgba(255,255,255,0.15)', background: 'transparent', color: 'rgba(255,255,255,0.7)', cursor: 'pointer', fontSize: '0.72rem', fontWeight: 700, flexShrink: 0 }}
                >{sortDir === 'asc' ? 'ASC ^' : 'DESC v'}</button>
                {/* State publish actions */}
                {activeStateFilter && activeStateFilter.length === 2 && (
                  <>
                    <button className="btn-primary"
                      style={{ background: '#4caf50', border: 'none', padding: '7px 14px', borderRadius: '6px', fontWeight: 700, fontSize: '0.75rem', cursor: 'pointer' }}
                      onClick={() => promoteState(activeStateFilter)}
                    >Publish {activeStateFilter}</button>
                    <button style={{ background: 'rgba(255,59,48,0.15)', border: '1px solid rgba(255,59,48,0.4)', padding: '7px 14px', borderRadius: '6px', fontWeight: 700, fontSize: '0.75rem', cursor: 'pointer', color: '#ff3b30' }}
                      onClick={() => unpublishState(activeStateFilter)}
                    >Retract {activeStateFilter}</button>
                  </>
                )}
                <button className="btn-primary" onClick={bulkPromote} style={{ fontSize: '0.75rem', padding: '7px 14px', flexShrink: 0 }}>Bulk Publish</button>
                {/* Pagination */}
                <div style={{ display: 'flex', gap: '6px', alignItems: 'center', marginLeft: 'auto', flexShrink: 0 }}>
                  <button disabled={page === 0} onClick={() => fetchSpots(page - 1, gridFilter)}
                    style={{ padding: '5px 10px', borderRadius: '6px', border: '1px solid rgba(255,255,255,0.15)', background: 'transparent', color: page === 0 ? 'rgba(255,255,255,0.2)' : '#fff', cursor: page === 0 ? 'default' : 'pointer', fontSize: '0.72rem' }}>Prev</button>
                  <span style={{ fontSize: '0.68rem', color: 'rgba(255,255,255,0.4)', whiteSpace: 'nowrap' }}>{page + 1} / {Math.ceil(totalSpots/rowsPerPage)||1} ({totalSpots})</span>
                  <button disabled={(page+1)*rowsPerPage >= totalSpots} onClick={() => fetchSpots(page + 1, gridFilter)}
                    style={{ padding: '5px 10px', borderRadius: '6px', border: '1px solid rgba(255,255,255,0.15)', background: 'transparent', color: (page+1)*rowsPerPage >= totalSpots ? 'rgba(255,255,255,0.2)' : '#fff', cursor: (page+1)*rowsPerPage >= totalSpots ? 'default' : 'pointer', fontSize: '0.72rem' }}>Next</button>
                </div>
              </div>
              {/* Row 2: Enrichment Chip Filters */}
              <div style={{ display: 'flex', gap: '6px', flexWrap: 'wrap', alignItems: 'center' }}>
                <span style={{ fontSize: '0.6rem', color: 'rgba(255,255,255,0.3)', textTransform: 'uppercase', letterSpacing: '0.1em', marginRight: '2px' }}>Filter:</span>
                {([
                  { key: 'has_photos',      label: 'Has Photos' },
                  { key: 'has_hours',       label: 'Has Hours' },
                  { key: 'has_website',     label: 'Has Website' },
                  { key: 'has_adult_night', label: '18+ Night' },
                  { key: 'has_pro_shop',    label: 'Pro Shop' },
                  { key: 'is_deep_crawled', label: 'Deep Crawled' },
                  { key: 'is_published',    label: 'Live on App' },
                ] as const).map(({ key, label }) => (
                  <button key={key}
                    onClick={() => {
                      const next = { ...chips, [key]: !chips[key] };
                      setChips(next);
                      fetchSpots(0, gridFilter, sortCol, sortDir, searchQuery, next, stateChip);
                    }}
                    style={{
                      padding: '4px 11px', borderRadius: '20px', fontSize: '0.67rem', fontWeight: 700, cursor: 'pointer', transition: 'all 0.15s',
                      border: chips[key] ? 'none' : '1px solid rgba(255,255,255,0.15)',
                      background: chips[key] ? '#8a2be2' : 'transparent',
                      color: chips[key] ? '#fff' : 'rgba(255,255,255,0.4)',
                    }}
                  >{label}{chips[key] ? '  x' : ''}</button>
                ))}
                {Object.values(chips).some(Boolean) && (
                  <button
                    onClick={() => { const r = Object.fromEntries(Object.keys(chips).map(k => [k, false])) as Record<string,boolean>; setChips(r); fetchSpots(0, gridFilter, sortCol, sortDir, searchQuery, r, stateChip); }}
                    style={{ padding: '4px 10px', borderRadius: '20px', fontSize: '0.65rem', border: '1px solid rgba(255,59,48,0.3)', background: 'rgba(255,59,48,0.1)', color: '#ff3b30', cursor: 'pointer', fontWeight: 700 }}
                  >Clear All</button>
                )}
              </div>


                        {/* =========== CARD VIEW =========== */}
            {viewMode === 'card' && (() => {
              // ---- helpers scoped to card render ----
              const toHoursArr = (h: any): string[] | null => {
                if (!h) return null;
                if (Array.isArray(h)) return h;
                if (typeof h === 'string') { try { const p = JSON.parse(h); return Array.isArray(p) ? p : null; } catch { return null; } }
                return null;
              };
              const isOpenNow = (hours: string[] | null): boolean | null => {
                if (!hours || !hours.length) return null;
                const DAYS = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];
                const todayName = DAYS[new Date().getDay()];
                const todayEntry = hours.find(h => h.startsWith(todayName));
                if (!todayEntry) return null;
                if (todayEntry.toLowerCase().includes('closed')) return false;
                const p = (s: string): number => {
                  const m = s.trim().match(/(\d+):(\d+)\s*(AM|PM)?/i);
                  if (!m) return 0;
                  let h = parseInt(m[1]), min = parseInt(m[2]);
                  const per = (m[3] || '').toUpperCase();
                  if (per === 'PM' && h !== 12) h += 12;
                  if (per === 'AM' && h === 12) h = 0;
                  return h * 60 + min;
                };
                const cur = new Date().getHours() * 60 + new Date().getMinutes();
                const segments = todayEntry.replace(/^[^:]+:\s*/, '').split(',');
                return segments.some(seg => {
                  const rng = seg.match(/(.+?)\s*[\u2013\u2014\-]\s*(.+)/);
                  if (!rng) return false;
                  return cur >= p(rng[1]) && cur <= p(rng[2]);
                });
              };
              const stars = (r: number) => {
                const full = Math.floor(r); const half = r - full >= 0.5;
                return Array.from({length:5},(_,i)=>
                  i < full ? '&#9733;' : (i === full && half ? '&#11240;' : '&#9734;')
                ).join('');
              };
              const todayHours = (hours: string[] | null): string => {
                if (!hours) return '';
                const days = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];
                const entry = hours.find(h => h.startsWith(days[new Date().getDay()]));
                return entry ? entry.split(': ').slice(1).join(': ') : '';
              };
              const domain = (url: string | null) => {
                if (!url) return null;
                try { return new URL(url).hostname.replace(/^www\./, ''); } catch { return url; }
              };

              return (
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(340px, 1fr))', gap: '1.25rem', padding: '0.5rem 0' }}>
                  {spots.map(spot => {
              const _ph = spot.photos as any[] | null; const _cd = spot.candidate_photos as any;
              const rawPhoto = (typeof _ph?.[0] === 'string' ? _ph[0] : _ph?.[0]?.url) ?? (_cd?.street_view_url ?? (_cd?.[0]?.url ?? null));
              const photo = proxyImg(rawPhoto);
                    const openStatus = isOpenNow(toHoursArr(spot.opening_hours));
                    const ratingNum  = spot.rating ? parseFloat(String(spot.rating)) : null;
                    const proShop    = spot.has_pro_shop || (spot as any).has_proshop;
                    const adultNight = spot.has_adult_night;
                    const photoCount = (_ph?.length ?? 0);
                    const candCount  = (_cd?.street_view_url ? 1 : 0);
                    const igUrl      = (spot as any).instagram_url;
                    const fbUrl      = (spot as any).facebook_url;
                    const ttUrl      = (spot as any).tiktok_url;
                    const hours      = toHoursArr(spot.opening_hours);
                    const adultSched = spot.adult_night_schedule;

                    return (
                      <div key={spot.id} style={{
                        background: 'rgba(30,30,40,0.95)', borderRadius: '14px', overflow: 'hidden',
                        border: `1px solid ${spot.is_published ? 'rgba(76,175,80,0.4)' : 'rgba(255,255,255,0.08)'}`,
                        display: 'flex', flexDirection: 'column', transition: 'transform 0.18s, box-shadow 0.18s',
                      }}
                        onMouseEnter={e => { (e.currentTarget as HTMLElement).style.transform='translateY(-3px)'; (e.currentTarget as HTMLElement).style.boxShadow='0 12px 40px rgba(0,0,0,0.5)'; }}
                        onMouseLeave={e => { (e.currentTarget as HTMLElement).style.transform=''; (e.currentTarget as HTMLElement).style.boxShadow=''; }}
                      >
                        {/* Hero image */}
                        <div style={{ position: 'relative', height: '180px', background: 'rgba(255,255,255,0.03)', flexShrink: 0 }}>
                          {photo
                            ? <img src={photo} alt={spot.name} style={{ width:'100%', height:'100%', objectFit:'cover' }} onError={e => { (e.target as HTMLImageElement).style.display='none'; }} />
                            : <div style={{ width:'100%', height:'100%', display:'flex', alignItems:'center', justifyContent:'center', flexDirection:'column', gap:'6px' }}>
                                <span style={{ fontSize:'2rem', opacity:0.15 }}>[ IMG ]</span>
                                <span style={{ fontSize:'0.65rem', color:'rgba(255,255,255,0.2)' }}>{candCount > 0 ? `${candCount} candidate(s) queued` : 'No photo'}</span>
                              </div>
                          }
                          {/* Status badge */}
                          <span style={{ position:'absolute', top:10, left:10, padding:'3px 8px', borderRadius:'6px', fontSize:'0.6rem', fontWeight:800, letterSpacing:'0.05em',
                            background: spot.verification_status === 'MEDIA_READY' ? '#e91e63' : spot.verification_status === 'ENRICHED' ? '#ff9800' : spot.verification_status === 'INDEXED' ? '#2196f3' : 'rgba(0,0,0,0.6)',
                            color: '#fff' }}>{spot.verification_status || 'PENDING'}</span>
                          {/* Open Now badge */}
                          {openStatus !== null && (
                            <span style={{ position:'absolute', top:10, right:10, padding:'3px 8px', borderRadius:'6px', fontSize:'0.6rem', fontWeight:800,
                              background: openStatus ? 'rgba(76,175,80,0.9)' : 'rgba(244,67,54,0.85)', color:'#fff' }}>
                              {openStatus ? 'OPEN NOW' : 'CLOSED'}
                            </span>
                          )}
                          {/* Photo count */}
                          {photoCount > 0 && (
                            <span style={{ position:'absolute', bottom:8, right:8, padding:'2px 7px', borderRadius:'4px', fontSize:'0.58rem', fontWeight:700, background:'rgba(0,0,0,0.65)', color:'#fff' }}>
                              {photoCount} photo{photoCount > 1 ? 's' : ''}
                            </span>
                          )}
                          {/* LIVE badge */}
                          {spot.is_published && (
                            <span style={{ position:'absolute', bottom:8, left:8, padding:'2px 8px', borderRadius:'4px', fontSize:'0.58rem', fontWeight:800, background:'#4caf50', color:'#fff', letterSpacing:'0.06em' }}>LIVE</span>
                          )}
                        </div>

                        {/* Card body */}
                        <div style={{ padding:'14px 16px', flex:1, display:'flex', flexDirection:'column', gap:'10px' }}>

                          {/* Name + location */}
                          <div>
                            <div style={{ fontWeight:800, fontSize:'1rem', lineHeight:1.2, marginBottom:'3px' }}>{spot.name}</div>
                            <div style={{ fontSize:'0.72rem', color:'rgba(255,255,255,0.45)' }}>
                              {[spot.street_address || spot.address, spot.city, spot.state, spot.zip].filter(Boolean).join(', ')}
                            </div>
                          </div>

                          {/* Rating row */}
                          {ratingNum && (
                            <div style={{ display:'flex', alignItems:'center', gap:'6px' }}>
                              <span style={{ color:'#ffc107', fontSize:'0.85rem' }} dangerouslySetInnerHTML={{ __html: stars(ratingNum) }} />
                              <span style={{ fontWeight:700, fontSize:'0.82rem' }}>{ratingNum.toFixed(1)}</span>
                              {spot.user_ratings_total && <span style={{ fontSize:'0.68rem', color:'rgba(255,255,255,0.35)' }}>({spot.user_ratings_total.toLocaleString()} reviews)</span>}
                            </div>
                          )}

                          {/* Facility chips */}
                          <div style={{ display:'flex', gap:'5px', flexWrap:'wrap' }}>
                            {spot.facility_type && <span style={{ padding:'2px 8px', borderRadius:'12px', fontSize:'0.6rem', fontWeight:700, background:'rgba(138,43,226,0.2)', border:'1px solid rgba(138,43,226,0.4)', color:'#c084fc' }}>{spot.facility_type}</span>}
                            {adultNight && <span style={{ padding:'2px 8px', borderRadius:'12px', fontSize:'0.6rem', fontWeight:700, background:'rgba(233,30,99,0.15)', border:'1px solid rgba(233,30,99,0.35)', color:'#f48fb1' }}>18+ Night</span>}
                            {proShop && <span style={{ padding:'2px 8px', borderRadius:'12px', fontSize:'0.6rem', fontWeight:700, background:'rgba(255,152,0,0.15)', border:'1px solid rgba(255,152,0,0.35)', color:'#ffcc80' }}>Pro Shop</span>}
                            {spot.has_rental && <span style={{ padding:'2px 8px', borderRadius:'12px', fontSize:'0.6rem', fontWeight:700, background:'rgba(33,150,243,0.15)', border:'1px solid rgba(33,150,243,0.3)', color:'#90caf9' }}>Rentals</span>}
                            {(spot as any).hosts_derby && <span style={{ padding:'2px 8px', borderRadius:'12px', fontSize:'0.6rem', fontWeight:700, background:'rgba(76,175,80,0.15)', border:'1px solid rgba(76,175,80,0.3)', color:'#a5d6a7' }}>Derby</span>}
                            {spot.surface_type && <span style={{ padding:'2px 8px', borderRadius:'12px', fontSize:'0.6rem', fontWeight:700, background:'rgba(255,255,255,0.05)', border:'1px solid rgba(255,255,255,0.12)', color:'rgba(255,255,255,0.5)' }}>{String(spot.surface_type)}</span>}
                          </div>

                          {/* Today's hours + Open Now */}
                          {hours && (
                            <div style={{ fontSize:'0.72rem', color:'rgba(255,255,255,0.55)' }}>
                              <span style={{ color:'rgba(255,255,255,0.3)', marginRight:'4px' }}>Today:</span>
                              <span style={{ color: openStatus === true ? '#4caf50' : openStatus === false ? '#f44336' : 'rgba(255,255,255,0.55)', fontWeight:600 }}>{todayHours(hours) || 'Hours available'}</span>
                            </div>
                          )}

                          {/* Full hours (collapsed by default - show on card if small list, else skip) */}
                          {hours && hours.length > 0 && (
                            <details style={{ fontSize:'0.68rem' }}>
                              <summary style={{ color:'rgba(255,255,255,0.3)', cursor:'pointer', fontSize:'0.65rem', userSelect:'none', marginBottom:'4px' }}>All hours ({hours.length} days)</summary>
                              {hours.map((h, i) => (
                                <div key={i} style={{ display:'flex', justifyContent:'space-between', padding:'1px 0', color:'rgba(255,255,255,0.45)' }}>
                                  <span style={{ color:'rgba(255,255,255,0.25)', marginRight:'8px' }}>{h.split(':')[0]}</span>
                                  <span>{h.split(': ').slice(1).join(': ')}</span>
                                </div>
                              ))}
                            </details>
                          )}

                          {/* Adult night — show on flag or details */}
                          {(spot.has_adult_night || adultSched || (spot as any).adult_night_details) && (
                            <div style={{ borderRadius: '6px', background: 'rgba(233,30,99,0.08)', border: '1px solid rgba(233,30,99,0.2)', overflow: 'hidden' }}>
                              <div style={{ padding: '5px 8px', background: 'rgba(233,30,99,0.14)', display: 'flex', alignItems: 'center', gap: '5px' }}>
                                <span style={{ color: '#f48fb1', fontWeight: 800, fontSize: '0.63rem', letterSpacing: '0.05em' }}>18+ ADULT NIGHT</span>
                              </div>
                              {(spot as any).adult_night_details && (
                                <div style={{ padding: '5px 8px', color: 'rgba(255,255,255,0.5)', fontSize: '0.66rem', lineHeight: 1.4 }}>{(spot as any).adult_night_details}</div>
                              )}
                              {adultSched && !(spot as any).adult_night_details && (
                                <div style={{ padding: '5px 8px', color: 'rgba(255,255,255,0.4)', fontSize: '0.66rem' }}>{typeof adultSched === 'object' ? JSON.stringify(adultSched) : String(adultSched)}</div>
                              )}
                            </div>
                          )}

                          {/* Website */}
                          {spot.website && (
                            <a href={spot.website} target="_blank" rel="noreferrer"
                              style={{ fontSize:'0.7rem', color:'#64b5f6', textDecoration:'none', wordBreak:'break-all', display:'block' }}
                              title={spot.website}
                            >{spot.website}</a>
                          )}

                          {/* Phone */}
                          {(spot.phone || (spot as any).phone_number) && (
                            <div style={{ fontSize:'0.72rem', color:'rgba(255,255,255,0.45)' }}>
                              <span style={{ color:'rgba(255,255,255,0.25)', marginRight:'4px' }}>Ph:</span>
                              {spot.phone || (spot as any).phone_number}
                            </div>
                          )}

                          {/* Social links */}
                          {(igUrl || fbUrl || ttUrl) && (
                            <div style={{ display:'flex', gap:'8px', flexWrap:'wrap' }}>
                              {igUrl && <a href={igUrl} target="_blank" rel="noreferrer" style={{ fontSize:'0.65rem', color:'#c13584', textDecoration:'none', padding:'2px 8px', borderRadius:'10px', border:'1px solid rgba(193,53,132,0.35)', background:'rgba(193,53,132,0.1)' }}>Instagram</a>}
                              {fbUrl && <a href={fbUrl} target="_blank" rel="noreferrer" style={{ fontSize:'0.65rem', color:'#1877f2', textDecoration:'none', padding:'2px 8px', borderRadius:'10px', border:'1px solid rgba(24,119,242,0.35)', background:'rgba(24,119,242,0.1)' }}>Facebook</a>}
                              {ttUrl && <a href={ttUrl} target="_blank" rel="noreferrer" style={{ fontSize:'0.65rem', color:'#ff0050', textDecoration:'none', padding:'2px 8px', borderRadius:'10px', border:'1px solid rgba(255,0,80,0.35)', background:'rgba(255,0,80,0.1)' }}>TikTok</a>}
                            </div>
                          )}

                          {/* Pipeline health row */}
                          <div style={{ display:'flex', gap:'6px', flexWrap:'wrap', marginTop:'auto', paddingTop:'8px', borderTop:'1px solid rgba(255,255,255,0.06)' }}>
                            <span style={{ fontSize:'0.58rem', fontWeight:700, padding:'2px 6px', borderRadius:'4px',
                              background: spot.is_deep_crawled ? 'rgba(76,175,80,0.15)' : 'rgba(255,255,255,0.05)',
                              border: `1px solid ${spot.is_deep_crawled ? 'rgba(76,175,80,0.3)' : 'rgba(255,255,255,0.1)'}`,
                              color: spot.is_deep_crawled ? '#81c784' : 'rgba(255,255,255,0.25)' }}>
                              {spot.is_deep_crawled ? 'DEEP CRAWLED' : 'NOT CRAWLED'}
                            </span>
                            {photoCount > 0 && <span style={{ fontSize:'0.58rem', fontWeight:700, padding:'2px 6px', borderRadius:'4px', background:'rgba(233,30,99,0.1)', border:'1px solid rgba(233,30,99,0.25)', color:'#f48fb1' }}>{photoCount} Photo{photoCount>1?'s':''}</span>}
                            {spot.google_place_id && <span style={{ fontSize:'0.58rem', fontWeight:700, padding:'2px 6px', borderRadius:'4px', background:'rgba(66,133,244,0.1)', border:'1px solid rgba(66,133,244,0.25)', color:'#90caf9' }}>Google ID</span>}
                            {spot.last_enriched_at && <span style={{ fontSize:'0.56rem', color:'rgba(255,255,255,0.2)', marginLeft:'auto' }}>Enriched: {new Date(spot.last_enriched_at).toLocaleDateString()}</span>}
                          </div>

                          {/* Publish toggle + quick actions */}
                          <div style={{ display:'flex', gap:'8px', alignItems:'center', marginTop:'4px' }}>
                            <label style={{ display:'flex', alignItems:'center', gap:'6px', cursor:'pointer', fontSize:'0.7rem', fontWeight:700,
                              color: spot.is_published ? '#4caf50' : 'rgba(255,255,255,0.4)' }}>
                              <input type="checkbox" checked={!!spot.is_published}
                                onChange={async () => {
                                  await fetch(`${API_BASE}/api/skate_spots/${spot.id}`, { method:'PUT', headers:{'Content-Type':'application/json'}, body: JSON.stringify({ is_published: !spot.is_published }) });
                                  fetchSpots(page, gridFilter);
                                }} style={{ accentColor:'#4caf50', width:'14px', height:'14px' }} />
                              {spot.is_published ? 'LIVE' : 'Publish'}
                            </label>
                            <button onClick={() => { setEditingId(spot.id); setEditForm(spot); }}
                              style={{ marginLeft:'auto', padding:'4px 10px', borderRadius:'6px', border:'1px solid rgba(255,255,255,0.15)', background:'transparent', color:'rgba(255,255,255,0.5)', cursor:'pointer', fontSize:'0.65rem' }}>Edit</button>
                            <button onClick={() => resetSpotToSeeded(spot.id, spot.name)} title="Reset to SEEDED"
                              style={{ padding:'4px 10px', borderRadius:'6px', border:'1px solid rgba(255,179,0,0.25)', background:'rgba(255,179,0,0.08)', color:'#ffb300', cursor:'pointer', fontSize:'0.65rem', fontWeight:700 }}>🔄 Reset</button>
                            <button onClick={async () => { if(confirm(`Purge AND permanently block ${spot.name}?`)) { await fetch(`${API_BASE}/api/skate_spots/${spot.id}?blacklist=true`,{method:'DELETE'}); fetchSpots(page,gridFilter); } }}
                              style={{ padding:'4px 10px', borderRadius:'6px', border:'1px solid rgba(255,59,48,0.25)', background:'rgba(255,59,48,0.1)', color:'rgba(255,59,48,0.8)', cursor:'pointer', fontSize:'0.65rem', fontWeight:700 }}>Purge</button>
                          </div>
                        </div>
                      </div>
                    );
                  })}
                </div>
              );
            })()}

                        {/* =========== LIST VIEW =========== */}
            {viewMode === 'list' && (() => {
              const toHoursArr2 = (h: any): string[] | null => {
                if (!h) return null;
                if (Array.isArray(h)) return h;
                if (typeof h === 'string') { try { const p = JSON.parse(h); return Array.isArray(p) ? p : null; } catch { return null; } }
                return null;
              };
              const isOpenNow = (hours: string[] | null): boolean | null => {
                if (!hours || !hours.length) return null;
                const DAYS = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];
                const todayName = DAYS[new Date().getDay()];
                const todayEntry = hours.find(h => h.startsWith(todayName));
                if (!todayEntry) return null;
                if (todayEntry.toLowerCase().includes('closed')) return false;
                const p = (s: string): number => {
                  const m = s.trim().match(/(\d+):(\d+)\s*(AM|PM)?/i);
                  if (!m) return 0;
                  let h = parseInt(m[1]), min = parseInt(m[2]);
                  const per = (m[3] || '').toUpperCase();
                  if (per === 'PM' && h !== 12) h += 12;
                  if (per === 'AM' && h === 12) h = 0;
                  return h * 60 + min;
                };
                const cur = new Date().getHours() * 60 + new Date().getMinutes();
                const segments = todayEntry.replace(/^[^:]+:\s*/, '').split(',');
                return segments.some(seg => {
                  const rng = seg.match(/(.+?)\s*[\u2013\u2014\-]\s*(.+)/);
                  if (!rng) return false;
                  return cur >= p(rng[1]) && cur <= p(rng[2]);
                });
              };
              const todayHours = (hours: string[] | null) => {
                if (!hours) return null;
                const days = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];
                const e = hours.find(h => h.startsWith(days[new Date().getDay()]));
                return e ? e.split(': ').slice(1).join(': ') : null;
              };

              return (
                <div style={{ display:'flex', flexDirection:'column', gap:'6px' }}>
                  {spots.map(spot => {
                    const _ph = spot.photos as any[] | null; const _cd = spot.candidate_photos as any;
                    const rawPhoto = (typeof _ph?.[0] === 'string' ? _ph[0] : _ph?.[0]?.url) ?? (_cd?.street_view_url ?? (_cd?.[0]?.url ?? null));
                    const photo = proxyImg(rawPhoto);
                    const openSt   = isOpenNow(toHoursArr2(spot.opening_hours));
                    const ratingN  = spot.rating ? parseFloat(String(spot.rating)) : null;
                    const proShop  = spot.has_pro_shop || (spot as any).has_proshop;
                    const hours    = toHoursArr2(spot.opening_hours);
                    const igUrl    = (spot as any).instagram_url;
                    const fbUrl    = (spot as any).facebook_url;
                    const ttUrl    = (spot as any).tiktok_url;
                    const photoCount = (_ph?.length ?? 0);
                    const STATUS_COLOR: Record<string,string> = {
                      MEDIA_READY:'#e91e63', ENRICHED:'#ff9800', INDEXED:'#2196f3',
                      IDENTITY_ESTABLISHED:'#9c27b0', PENDING:'rgba(255,255,255,0.3)', REJECTED:'#f44336'
                    };
                    const sColor = STATUS_COLOR[spot.verification_status ?? 'PENDING'] ?? 'rgba(255,255,255,0.3)';

                    return (
                      <div key={spot.id} style={{
                        display:'grid', gridTemplateColumns: '88px 1fr auto',
                        background:'rgba(25,25,35,0.98)', borderRadius:'10px', overflow:'hidden',
                        border:`1px solid ${spot.is_published ? 'rgba(76,175,80,0.3)' : 'rgba(255,255,255,0.07)'}`,
                        borderLeft:`4px solid ${sColor}`,
                        transition:'background 0.15s',
                      }}
                        onMouseEnter={e => (e.currentTarget as HTMLElement).style.background='rgba(40,40,55,0.98)'}
                        onMouseLeave={e => (e.currentTarget as HTMLElement).style.background='rgba(25,25,35,0.98)'}
                      >
                        {/* Thumbnail */}
                        <div style={{ position:'relative', height:'88px',  background:'rgba(255,255,255,0.03)', flexShrink:0 }}>
                          {photo
                            ? <img src={photo} alt="" style={{ width:'100%', height:'100%', objectFit:'cover' }} onError={e => { (e.target as HTMLImageElement).style.display='none'; }} />
                            : <div style={{ width:'100%', height:'100%', display:'flex', alignItems:'center', justifyContent:'center', fontSize:'0.6rem', color:'rgba(255,255,255,0.15)' }}>NO IMG</div>
                          }
                          {/* Open now dot */}
                          {openSt !== null && (
                            <span style={{ position:'absolute', bottom:4, right:4, width:9, height:9, borderRadius:'50%', background: openSt ? '#4caf50' : '#f44336', display:'block', boxShadow: openSt ? '0 0 6px #4caf50' : 'none' }} title={openSt ? 'Open Now' : 'Closed'} />
                          )}
                        </div>

                        {/* Main content */}
                        <div style={{ padding:'8px 12px', overflow:'hidden', display:'flex', flexDirection:'column', gap:'4px' }}>
                          {/* Name row */}
                          <div style={{ display:'flex', alignItems:'baseline', gap:'8px', flexWrap:'wrap' }}>
                            <span style={{ fontWeight:800, fontSize:'0.88rem', whiteSpace:'nowrap', overflow:'hidden', textOverflow:'ellipsis', maxWidth:'260px' }}>{spot.name}</span>
                            {spot.is_published && <span style={{ fontSize:'0.55rem', fontWeight:800, color:'#4caf50', border:'1px solid rgba(76,175,80,0.5)', padding:'1px 5px', borderRadius:'4px', flexShrink:0 }}>LIVE</span>}
                            <span style={{ fontSize:'0.6rem', fontWeight:700, color:sColor, flexShrink:0 }}>{spot.verification_status || 'PENDING'}</span>
                          </div>

                          {/* Address */}
                          <div style={{ fontSize:'0.68rem', color:'rgba(255,255,255,0.35)', whiteSpace:'nowrap', overflow:'hidden', textOverflow:'ellipsis' }}>
                            {[spot.city, spot.state].filter(Boolean).join(', ')}
                            {spot.street_address ? ` — ${spot.street_address}` : ''}
                          </div>

                          {/* Data row: rating | hours | website | socials */}
                          <div style={{ display:'flex', gap:'10px', alignItems:'center', flexWrap:'wrap', marginTop:'2px' }}>
                            {ratingN && (
                              <span style={{ fontSize:'0.68rem', color:'#ffc107', fontWeight:700 }}>
                                {'*'.repeat(Math.round(ratingN))} {ratingN.toFixed(1)}
                                {spot.user_ratings_total && <span style={{ color:'rgba(255,255,255,0.25)', fontWeight:400 }}> ({spot.user_ratings_total.toLocaleString()})</span>}
                              </span>
                            )}
                            {hours && (
                              <span style={{ fontSize:'0.65rem', color: openSt === true ? '#4caf50' : openSt === false ? '#f44336' : 'rgba(255,255,255,0.35)', fontWeight: openSt !== null ? 700 : 400 }}>
                                {openSt === true ? 'Open: ' : openSt === false ? 'Closed ' : ''}{todayHours(hours) || 'Hours on file'}
                              </span>
                            )}
                            {spot.website && (
                              <a href={spot.website} target="_blank" rel="noreferrer"
                                style={{ fontSize:'0.65rem', color:'#64b5f6', textDecoration:'none', maxWidth:'180px', overflow:'hidden', textOverflow:'ellipsis', whiteSpace:'nowrap', display:'block' }}
                                title={spot.website}>{spot.website.replace(/^https?:\/\/(www\.)?/,'')}</a>
                            )}
                          </div>

                          {/* Badges row */}
                          <div style={{ display:'flex', gap:'4px', flexWrap:'wrap', alignItems:'center' }}>
                            {spot.has_adult_night && <span style={{ fontSize:'0.56rem', fontWeight:700, padding:'1px 6px', borderRadius:'8px', background:'rgba(233,30,99,0.15)', color:'#f48fb1', border:'1px solid rgba(233,30,99,0.25)' }}>18+ Night</span>}
                            {proShop && <span style={{ fontSize:'0.56rem', fontWeight:700, padding:'1px 6px', borderRadius:'8px', background:'rgba(255,152,0,0.12)', color:'#ffcc80', border:'1px solid rgba(255,152,0,0.25)' }}>Pro Shop</span>}
                            {spot.is_deep_crawled && <span style={{ fontSize:'0.56rem', fontWeight:700, padding:'1px 6px', borderRadius:'8px', background:'rgba(76,175,80,0.1)', color:'#81c784', border:'1px solid rgba(76,175,80,0.2)' }}>Deep Crawled</span>}
                            {photoCount > 0 && <span style={{ fontSize:'0.56rem', fontWeight:700, padding:'1px 6px', borderRadius:'8px', background:'rgba(233,30,99,0.1)', color:'#f06292', border:'1px solid rgba(233,30,99,0.2)' }}>{photoCount} Photo{photoCount>1?'s':''}</span>}
                            {/* Social indicators */}
                            {igUrl && <span style={{ fontSize:'0.56rem', fontWeight:700, padding:'1px 6px', borderRadius:'8px', background:'rgba(193,53,132,0.12)', color:'#c13584', border:'1px solid rgba(193,53,132,0.25)' }}>IG</span>}
                            {fbUrl && <span style={{ fontSize:'0.56rem', fontWeight:700, padding:'1px 6px', borderRadius:'8px', background:'rgba(24,119,242,0.12)', color:'#1877f2', border:'1px solid rgba(24,119,242,0.25)' }}>FB</span>}
                            {ttUrl && <span style={{ fontSize:'0.56rem', fontWeight:700, padding:'1px 6px', borderRadius:'8px', background:'rgba(255,0,80,0.1)', color:'#ff0050', border:'1px solid rgba(255,0,80,0.2)' }}>TT</span>}
                          </div>
                        </div>

                        {/* Actions column */}
                        <div style={{ display:'flex', flexDirection:'column', alignItems:'center', justifyContent:'center', gap:'6px', padding:'0 12px', borderLeft:'1px solid rgba(255,255,255,0.05)', flexShrink:0 }}>
                          <label style={{ display:'flex', flexDirection:'column', alignItems:'center', gap:'2px', cursor:'pointer', fontSize:'0.55rem', fontWeight:800, color: spot.is_published ? '#4caf50' : 'rgba(255,255,255,0.3)' }}>
                            <input type="checkbox" checked={!!spot.is_published}
                              onChange={async () => {
                                await fetch(`${API_BASE}/api/skate_spots/${spot.id}`,{method:'PUT',headers:{'Content-Type':'application/json'},body:JSON.stringify({is_published:!spot.is_published})});
                                fetchSpots(page,gridFilter);
                              }} style={{ accentColor:'#4caf50', width:'16px', height:'16px' }} />
                            {spot.is_published ? 'LIVE' : 'Publish'}
                          </label>
                          <button onClick={() => { setEditingId(spot.id); setEditForm(spot); }}
                            style={{ padding:'3px 10px', borderRadius:'5px', border:'1px solid rgba(255,255,255,0.12)', background:'transparent', color:'rgba(255,255,255,0.45)', cursor:'pointer', fontSize:'0.6rem' }}>Edit</button>
                          <button onClick={() => resetSpotToSeeded(spot.id, spot.name)} title="Reset to SEEDED"
                            style={{ padding:'3px 10px', borderRadius:'5px', border:'1px solid rgba(255,179,0,0.2)', background:'rgba(255,179,0,0.08)', color:'#ffb300', cursor:'pointer', fontSize:'0.6rem', fontWeight:700 }}>🔄</button>
                          <button onClick={async () => { if(confirm(`Purge AND permanently block ${spot.name}?`)) { await fetch(`${API_BASE}/api/skate_spots/${spot.id}?blacklist=true`,{method:'DELETE'}); fetchSpots(page,gridFilter); } }}
                            style={{ padding:'3px 10px', borderRadius:'5px', border:'1px solid rgba(255,59,48,0.2)', background:'rgba(255,59,48,0.1)', color:'rgba(255,59,48,0.8)', cursor:'pointer', fontSize:'0.6rem', fontWeight:700 }}>Purge</button>
                        </div>
                      </div>
                    );
                  })}
                </div>
              );
            })()}

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
                              <option value="PENDING"> PENDING (PH_1)</option>
                              <option value="IDENTITY_ESTABLISHED">️ IDENTIFIED (PH_2)</option>
                              <option value="INDEXED">️ INDEXED (PH_3)</option>
                              <option value="ENRICHED"> ENRICHED (Gold Standard)</option>
                              <option value="MEDIA_READY"> MEDIA_READY (PH_5)</option>
                              <option value="DEPRECATED">️ Deprecated</option>
                              <option value="REJECTED"> Graveyard</option>
                           </select>
                        </td>
                        <td>
                           {row.rating ? <span style={{color: '#ffd700', fontWeight:'bold', textShadow: '0 0 5px rgba(255,215,0,0.5)'}}>{row.rating} <span style={{fontSize: '0.7em', color: 'gray'}}>({row.user_ratings_total || 0})</span></span> : <span style={{color:'gray'}}>-</span>}
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
                               {row.has_adult_night ? '' : ''}
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
                              <button className="btn-icon btn-save-inline" onClick={saveEdit}></button>
                            ) : (
                              <button className="btn-icon" onClick={() => startEdit(row)}>️</button>
                            )}
                            {activeTab === 'graveyard' ? (
                                <button className="btn-icon" onClick={() => restoreSpot(row.id, row.name)} title="Restore" style={{color: '#4caf50'}}>♻️</button>
                              ) : (
                                <>
                                  <button
                                    className="btn-icon"
                                    onClick={() => resetSpotToSeeded(row.id, row.name)}
                                    title="Reset to SEEDED — re-enter Detective queue"
                                    disabled={resettingIds[row.id] === 'loading' || resettingIds[row.id] === 'success'}
                                    style={{ color: resettingIds[row.id] === 'success' ? '#4caf50' : '#ffb300', fontSize: '0.85rem', cursor: resettingIds[row.id] ? 'default' : 'pointer', opacity: resettingIds[row.id] === 'loading' ? 0.5 : 1 }}
                                  >
                                    {resettingIds[row.id] === 'loading' ? '⏳' : resettingIds[row.id] === 'success' ? '✅' : '🔄'}
                                  </button>
                                  <button className="btn-icon btn-delete" onClick={() => deleteSpot(row.id, row.name)}>🗑️</button>
                                </>
                              )}
                          </div>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
            )}
            </div>}  {/* end databank_grid collapsible */}
          </div>
        )}
      </div>

      {activeTab === 'sniper' && (
        <div className="tab-pane phase-sniper" style={{ height: '80vh', width: '100%', borderRadius: '12px', overflow: 'hidden', border: '1px solid #2D3340', boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.25)' }}>
          <SniperBench />
        </div>
      )}

      <div className="log-panel panel">
        <div className="log-header">
           <h2 className="panel-header" style={{margin:0}}>
             {activeTab === 'phase1' ? 'Phase 1: Seed Engine Logs' :
              activeTab === 'phase2' ? 'Phase 2: Detective Logs' :
              activeTab === 'phase3' ? 'Phase 3: Photographer Logs' :
              activeTab === 'phase4' ? 'Phase 4: Publisher Logs' :
              'Omni-Terminal (Restricted Access)'}
           </h2>
           <button className="btn-mini" onClick={fetchHistory}>PERSISTENT HISTORY</button>
        </div>
        <div className="log-container" ref={logsRef}>
          {logs
             .filter(log => {
                if (activeTab === 'phase1') return log.source === 'Phase 1' || log.source === 'System';
                if (activeTab === 'phase2') return log.source === 'Phase 3' || log.source === 'System';
             if (activeTab === 'phase3') return log.source === 'Photographer' || log.source === 'System';
             if (activeTab === 'phase4') return log.source === 'System';
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
             if (activeTab === 'phase2') return log.source === 'Phase 3' || log.source === 'System';
             if (activeTab === 'phase3') return log.source === 'Photographer' || log.source === 'System';
             if (activeTab === 'phase4') return log.source === 'System';
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
      </>
      )}
    </div>
  );
}

export default App;





