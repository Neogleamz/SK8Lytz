import { useState, useEffect, useRef } from 'react';
import USAMap from './USMap';
import ScraperPipeline from './components/ScraperPipeline';
import type { PipelineStats } from './components/ScraperPipeline';
import { OmniTerminal } from './components/OmniTerminal';
import DetectiveLab from './DetectiveLab';
import { SniperBench } from './components/SniperBench';
import { HeuristicsControlCenter } from './components/HeuristicsControlCenter';
import { DatabankCard } from './components/DatabankCard';
import { RecordEditModal } from './components/RecordEditModal';
import './App.css';

const API_BASE = 'http://localhost:5999';

// Route external photo URLs through CCTower proxy to avoid referrer/CORS blocks on localhost
const proxyImg = (url: string | null) => {
  if (!url) return null;
  // Supabase CDN URLs are already ours — serve directly
  if (url.includes('supabase')) return url;
  // Local bucket relative URLs — append API base and serve directly
  if (url.startsWith('/local-bucket')) {
    return `${API_BASE}${url}`;
  }
  // Local backend URLs (localhost:5999) — serve directly without proxying
  if (url.includes('localhost:5999') || url.includes('127.0.0.1:5999')) {
    return url;
  }
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

const getEmails = (emails: unknown): string[] => {
  if (!emails) return [];
  if (Array.isArray(emails)) return emails.filter(Boolean).map(String);
  if (typeof emails === 'string') {
    try {
      const parsed = JSON.parse(emails);
      if (Array.isArray(parsed)) return parsed.filter(Boolean).map(String);
      return [emails].filter(Boolean);
    } catch {
      return emails.split(',').map((e: string) => e.trim()).filter(Boolean);
    }
  }
  return [];
};

function App() {
  const [activeTab, setActiveTab] = useState<'pipeline' | 'phase1' | 'phase2' | 'phase3' | 'phase4' | 'sniper' | 'heuristics' | 'graveyard'>('pipeline');
  const [seedProvider, setSeedProvider] = useState<'osm'|'google'|'website-resolver'>('google');
  const [resettingIds, setResettingIds] = useState<Record<string, 'loading' | 'success'>>({});


  // --- Sys Dashboard States ---
  const [status, setStatus] = useState<any>(null);
  const [targetFacilities, setTargetFacilities] = useState<string[]>([]);
  const [stateOverride, setStateOverride] = useState<string[]>([]);
  const [pipelineStats, setPipelineStats] = useState<PipelineStats | undefined>(undefined);
  const [showBulkResetModal, setShowBulkResetModal] = useState<boolean>(false);
  const [resetToTarget, setResetToTarget] = useState<'SEEDED' | 'DEEP_CRAWLED' | 'MEDIA_READY' | 'PENDING_WEBSITE'>('SEEDED');
  const [resetStatuses, setResetStatuses] = useState<Record<string, boolean>>({
    PENDING_WEBSITE: true,
    WEBSITE_STALLED: true,
    DEEP_CRAWLED: true,
    MEDIA_READY: true,
    STALLED: true,
    REJECTED: false
  });

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
  const [liveStreamText, setLiveStreamText] = useState<string>('');
  const [isStreaming, setIsStreaming] = useState<boolean>(false);
  
  const [currentAnalyzingSpot, setCurrentAnalyzingSpot] = useState<string>('');




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
        ? ['pending_website', 'stalled_website', 'phase1', 'phase2', 'phase3', 'phase4', 'detective-recent', 'recent']
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
      if (data.type === 'LLM_STREAM') {
        setIsStreaming(true);
        setLiveStreamText(prev => prev + data.message);
        return;
      }
      if (data.type === 'INFO' && data.message.includes('LM Studio Pass') && !data.message.includes('complete')) {
        setLiveStreamText('');
      }
      if (data.type === 'INFO' && data.message.includes('[Detective] Analyzing:')) {
        const match = data.message.match(/Analyzing:\s*(.+)/);
        if (match && match[1]) {
          setCurrentAnalyzingSpot(match[1].trim());
        }
      }
      if (data.type === 'INFO' && data.message.includes('complete. Keys:')) {
        setTimeout(() => {
          setIsStreaming(false);
          setCurrentAnalyzingSpot('');
        }, 8000);
      }
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
    if (activeTab === 'pipeline') {
      fetchDatabankCoverage(); // Map uses same source
    }
    if (['pipeline', 'graveyard'].includes(activeTab)) {
      fetchSpots(0, gridFilter, sortCol, sortDir, searchQuery, chips, stateChip);
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
    // Phases now: pending_website, stalled_website, phase1, phase3 (Detective), phase4 (Photographer), phase6 (Publisher)
    const phasesToFetch = only ?? ['pending_website', 'stalled_website', 'phase1', 'phase2', 'phase3', 'phase4', 'detective-recent', 'published', 'recent'];
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
       if (seedProvider === 'website-resolver') {
         const action = type === 'start-all' ? 'start' : 'stop';
         await fetch(`${API_BASE}/api/daemons/website-resolver/${action}`, {
           method: 'POST'
         });
       } else {
         await fetch(`${API_BASE}/api/harvest/${type}`, {
           method: 'POST',
           headers: { 'Content-Type': 'application/json' },
           body: JSON.stringify({ target_facilities: targetFacilities, target_states: states, provider: seedProvider })
         });
       }
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
    setResettingIds(prev => ({ ...prev, [id]: 'loading' }));
    try {
      await fetch(`${API_BASE}/api/skate_spots/${id}/restart`, {
        method: 'POST'
      });
      setResettingIds(prev => ({ ...prev, [id]: 'success' }));
      setTimeout(() => {
        setResettingIds(prev => { const next = {...prev}; delete next[id]; return next; });
        fetchSpots(page, gridFilter);
        fetchQueue();
      }, 1500);
    } catch (e) {
      setResettingIds(prev => { const next = {...prev}; delete next[id]; return next; });
    }
  };

  // Bulk reset all filtered records back to target phase (respects global state + facility filters + selected statuses)
  const [isBulkResetting, setIsBulkResetting] = useState(false);
  const bulkResetToSeeded = async () => {
    const activeStatuses = Object.entries(resetStatuses)
      .filter(([_, enabled]) => enabled)
      .map(([status]) => status);

    if (activeStatuses.length === 0) {
      alert('⚠️ Please select at least one status stage to reset.');
      return;
    }

    const stateLabel = stateOverride.length > 0 ? stateOverride.join(', ') : 'ALL STATES';
    const facLabel = targetFacilities.length > 0 ? targetFacilities.join(', ') : 'ALL TYPES';
    const statusLabel = activeStatuses.join(', ');

    setIsBulkResetting(true);
    try {
      const res = await fetch(`${API_BASE}/api/bulk-reset-to-seeded`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ 
          states: stateOverride, 
          facility_types: targetFacilities,
          target_statuses: activeStatuses,
          reset_to: resetToTarget
        })
      });
      const data = await res.json();
      if (data.success) {
        alert(`✅ Reset ${data.reset_count} records to ${resetToTarget}.\nFilters: ${stateLabel} / ${facLabel}\nStatuses Reset: ${statusLabel}`);
        setShowBulkResetModal(false);
        fetchSpots(page, gridFilter);
        fetchQueue();
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
  const handleModalSave = async (updates: any) => {
    const res = await fetch(`${API_BASE}/api/spots/${editingId}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(updates)
    });
    if (!res.ok) {
      const data = await res.json().catch(() => ({}));
      throw new Error(data.error || 'Network error or server failed to update record.');
    }
    setEditingId(null);
    fetchSpots(page, gridFilter);
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

  const updateSpot = async (id: string, updates: any) => {
    try {
      await fetch(`${API_BASE}/api/spots/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updates)
      });
      fetchSpots(page, gridFilter);
    } catch (e) {}
  };

  const deleteImage = async (spotId: string, photoIndex: number) => {
    try {
      await fetch(`${API_BASE}/api/skate_spots/${spotId}/photos/${photoIndex}`, { method: 'DELETE' });
      fetchSpots(page, gridFilter);
      fetchPipelineStatsRef.current();
      fetchQueue();
    } catch(e) {}
  };

  const setHeroImage = async (spotId: string, photoIndex: number) => {
    if (photoIndex === 0) return; // already hero
    try {
      await fetch(`${API_BASE}/api/skate_spots/${spotId}/photos/hero`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ photoIndex })
      });
      fetchSpots(page, gridFilter);
      fetchPipelineStatsRef.current();
      fetchQueue();
    } catch(e) {}
  };

  const blockSpot = async (spotId: string, name: string) => {
    try {
      await fetch(`${API_BASE}/api/scraper/blocklist`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ keyword: name, match_type: 'name', reason: 'Manual Block' })
      });
      fetchSpots(page, gridFilter);
      fetchPipelineStatsRef.current();
      fetchQueue();
    } catch (e) {}
  };
  
  const uploadPhoto = async (spotId: string, file: File) => {
    return new Promise<void>((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = async () => {
        try {
          await fetch(`${API_BASE}/api/skate_spots/${spotId}/photos/upload`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ image: reader.result })
          });
          fetchSpots(page, gridFilter);
          fetchQueue();
          resolve();
        } catch(e) { reject(e); }
      };
      reader.onerror = () => reject(reader.error);
      reader.readAsDataURL(file);
    });
  };

  const assignPhotoType = async (spotId: string, photoIndex: number, fieldType: string) => {
    try {
      await fetch(`${API_BASE}/api/skate_spots/${spotId}/photos/tag`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ photoIndex, fieldType })
      });
      fetchSpots(page, gridFilter);
      fetchPipelineStatsRef.current();
      fetchQueue();
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
    if (!confirm('Promote ALL MEDIA_READY records to the public Skate Map?')) return;
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
    if (!s) return null;
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
          <span style={{ fontSize: '0.55rem', color: 'rgba(255,255,255,0.2)', marginLeft: 'auto' }}>{(s.total ?? 0).toLocaleString()} records · {isCollapsed('pulse') ? '▼' : '▲'}</span>
        </div>
        {!isCollapsed('pulse') && (
          <>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '1px', background: 'rgba(255,255,255,0.04)' }}>
              {/* ① SCOUT — IN: PENDING  OUT: SEEDED */}
              <div style={{ background: 'rgba(12,12,20,0.95)', padding: '8px 12px' }}>
                <div style={{ fontSize: '0.57rem', fontWeight: 900, color: C.scout, textTransform: 'uppercase' as const, letterSpacing: '0.08em', marginBottom: '5px', borderBottom: `1px solid ${C.scout}33`, paddingBottom: '4px' }}>① Scout  IN:PENDING → OUT:SEEDED</div>
                <div style={{ display: 'flex', flexDirection: 'column' as const, gap: '3px' }}>
                  {row('Total in DB', s.total ?? 0, '#fff')}
                  {row('SEEDED (awaiting Det.)', s.seeded ?? 0, C.scout)}
                  {row('Has Website', s.has_website ?? 0, 'rgba(255,255,255,0.6)')}
                  {row('No Website', (s.total || 0) - (s.has_website || 0), 'rgba(255,255,255,0.25)')}
                </div>
              </div>
              {/* ② DETECTIVE — IN: SEEDED  OUT: DEEP_CRAWLED */}
              <div style={{ background: 'rgba(12,12,20,0.95)', padding: '8px 12px' }}>
                <div style={{ fontSize: '0.57rem', fontWeight: 900, color: C.detective, textTransform: 'uppercase' as const, letterSpacing: '0.08em', marginBottom: '5px', borderBottom: `1px solid ${C.detective}33`, paddingBottom: '4px' }}>② Detective  IN:SEEDED → OUT:DEEP_CRAWLED</div>
                <div style={{ display: 'flex', flexDirection: 'column' as const, gap: '3px' }}>
                  {row('AI Queue (SEEDED+site)', s.detective_queue ?? 0, (s.detective_queue ?? 0) > 0 ? '#ffb300' : 'rgba(255,255,255,0.3)')}
                  {row('AI Done (DEEP_CRAWLED)', s.deep_crawled_count ?? 0, C.detective)}
                  {row('Has Photo Candidates', s.has_candidates ?? 0, 'rgba(255,255,255,0.6)')}
                  {row('No Candidates', (s.deep_crawled_count || 0) - (s.has_candidates || 0), 'rgba(255,255,255,0.25)')}
                </div>
              </div>
              {/* ③ PHOTOGRAPHER — IN: DEEP_CRAWLED  OUT: MEDIA_READY */}
              <div style={{ background: 'rgba(12,12,20,0.95)', padding: '8px 12px' }}>
                <div style={{ fontSize: '0.57rem', fontWeight: 900, color: C.photo, textTransform: 'uppercase' as const, letterSpacing: '0.08em', marginBottom: '5px', borderBottom: `1px solid ${C.photo}33`, paddingBottom: '4px' }}>③ Photographer  IN:DEEP_CRAWLED → OUT:MEDIA_READY</div>
                <div style={{ display: 'flex', flexDirection: 'column' as const, gap: '3px' }}>
                  {row('Photo Queue (DEEP_CRAWLED)', s.photographer_queue ?? 0, (s.photographer_queue ?? 0) > 0 ? '#ffb300' : 'rgba(255,255,255,0.3)')}
                  {row('Photo Candidates', s.has_candidates ?? 0, 'rgba(255,255,255,0.6)')}
                  {row('Photographed', s.has_photos ?? 0, '#fff')}
                  {row('MEDIA_READY', s.media_ready ?? 0, C.photo)}
                </div>
              </div>
              {/* ④ PUBLISHER — IN: MEDIA_READY  OUT: PUBLISHED */}
              <div style={{ background: 'rgba(12,12,20,0.95)', padding: '8px 12px' }}>
                <div style={{ fontSize: '0.57rem', fontWeight: 900, color: C.pub, textTransform: 'uppercase' as const, letterSpacing: '0.08em', marginBottom: '5px', borderBottom: `1px solid ${C.pub}33`, paddingBottom: '4px' }}>④ Publisher  IN:MEDIA_READY → OUT:PUBLISHED</div>
                <div style={{ display: 'flex', flexDirection: 'column' as const, gap: '3px' }}>
                  {row('Pub Queue (MEDIA_READY)', s.publisher_queue ?? 0, (s.publisher_queue ?? 0) > 0 ? '#ffb300' : C.pub)}
                  {row('Live on App', s.published ?? 0, '#4ade80')}
                  {row('Pipeline %', `${(s.total ?? 0) > 0 ? Math.round(((s.deep_crawled_count ?? 0) / (s.total ?? 0)) * 100) : 0}%`, 'rgba(255,255,255,0.5)')}
                  {row('Published %', `${(s.total ?? 0) > 0 ? Math.round(((s.published ?? 0) / (s.total ?? 0)) * 100) : 0}%`, '#4ade80')}
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
    <div style={{
      display: 'flex',
      flexDirection: 'column',
      gap: '16px',
      width: '100%',
      padding: '4px 0',
    }}>
      {/* TOP BAR: Header Navigation / Action Deck */}
      <div style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        width: '100%',
        flexWrap: 'wrap',
        gap: '12px',
        borderBottom: '1px solid rgba(255, 255, 255, 0.05)',
        paddingBottom: '12px'
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <span style={{
            fontSize: '0.85rem',
            fontWeight: 900,
            background: 'linear-gradient(90deg, #c084fc, #00d4ff)',
            WebkitBackgroundClip: 'text',
            WebkitTextFillColor: 'transparent',
            textTransform: 'uppercase',
            letterSpacing: '0.15em'
          }}>
            🛰️ Neogleamz Grid Control Deck
          </span>
        </div>

        {/* Action Deck (Factory Floor, Reset, Sniper, Graveyard, Engine Brain) */}
        <div style={{ display: 'flex', gap: '6px', flexShrink: 0, alignItems: 'center', flexWrap: 'wrap' }}>
          <button className="btn-icon" onClick={() => setActiveTab('pipeline')} title="Factory Floor"
            style={{ background: activeTab === 'pipeline' ? 'rgba(59, 130, 246, 0.2)' : 'rgba(255,255,255,0.05)', color: activeTab === 'pipeline' ? '#3b82f6' : 'rgba(255,255,255,0.6)', border: activeTab === 'pipeline' ? '1px solid #3b82f6' : '1px solid transparent', padding: '4px 8px', borderRadius: '6px', cursor: 'pointer', fontSize: '0.65rem', fontWeight: 800 }}>
            FACTORY FLOOR
          </button>
          
          <button
             onClick={() => setShowBulkResetModal(true)}
             disabled={isBulkResetting}
             title={`Reset matching records back to a specific phase (Seeded, Deep Crawled, or Media Ready)`}
             style={{ background: isBulkResetting ? 'rgba(255, 90, 0, 0.4)' : 'rgba(255, 90, 0, 0.15)', color: isBulkResetting ? '#fff' : '#ff5a00', border: '1px solid rgba(255, 90, 0, 0.3)', padding: '4px 10px', borderRadius: '6px', cursor: isBulkResetting ? 'not-allowed' : 'pointer', fontSize: '0.72rem', fontWeight: 800, textTransform: 'uppercase', display: 'flex', alignItems: 'center', gap: '6px' }}
          >
            {isBulkResetting ? '⏳ RESETTING...' : '⚡ Reset Records'}
          </button>
          
          <button className="btn-icon" onClick={() => setActiveTab('sniper')} title="Sniper Bench"
            style={{ background: activeTab === 'sniper' ? 'rgba(255, 90, 0, 0.2)' : 'rgba(255,255,255,0.05)', color: activeTab === 'sniper' ? '#ff5a00' : 'rgba(255,255,255,0.6)', border: activeTab === 'sniper' ? '1px solid #ff5a00' : '1px solid transparent', padding: '4px 8px', borderRadius: '6px', cursor: 'pointer', fontSize: '0.85rem' }}>
            🔫
          </button>
          <button className="btn-icon" onClick={() => setActiveTab('graveyard')} title="Garbage Can (Rejected & Purged)"
            style={{ background: activeTab === 'graveyard' ? 'rgba(244, 67, 54, 0.2)' : 'rgba(255,255,255,0.05)', color: activeTab === 'graveyard' ? '#f44336' : 'rgba(255,255,255,0.6)', border: activeTab === 'graveyard' ? '1px solid #f44336' : '1px solid transparent', padding: '4px 8px', borderRadius: '6px', cursor: 'pointer', fontSize: '0.85rem' }}>
            🗑️
          </button>

          <button className="btn btn-start" onClick={handleSysStart} disabled={status?.isRunning}
            style={{ padding: '4px 12px', fontSize: '0.62rem', fontWeight: 800 }}>BOOT ALL</button>
          <button className="btn btn-stop" onClick={handleSysStop} disabled={!status?.isRunning}
            style={{ padding: '4px 12px', fontSize: '0.62rem', fontWeight: 800 }}>HALT ALL</button>
        </div>
      </div>

      {/* GRID CONTROL DECK (3 Columns) */}
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))',
        gap: '16px',
        width: '100%',
      }}>
        {/* ================= COLUMN 1: TARGET STRATEGY ================= */}
        <div className="hud-panel" style={{
          background: 'rgba(255,255,255,0.01)',
          border: '1px solid rgba(138,43,226,0.18)',
          borderRadius: '12px',
          padding: '16px',
          boxShadow: 'inset 0 0 15px rgba(138,43,226,0.05)',
          display: 'flex',
          flexDirection: 'column',
          gap: '12px',
        }}>
          <h3 style={{ fontSize: '0.72rem', fontWeight: 900, color: '#c084fc', textTransform: 'uppercase', letterSpacing: '0.12em', margin: 0, display: 'flex', alignItems: 'center', gap: '6px' }}>
            <span>🎯</span> Target Strategy
          </h3>
          
          {/* Region Override */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
            <span style={{ fontSize: '0.56rem', fontWeight: 800, color: 'rgba(255,255,255,0.4)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Region Coverage Override</span>
            <div style={{ display: 'flex', alignItems: 'center', gap: '6px', flexWrap: 'wrap' }}>
              <button onClick={() => updateGlobalStrategy('state_override', 'ALL')}
                style={{ fontSize: '0.58rem', fontWeight: 700, padding: '2px 7px', borderRadius: '10px', border: 'none', cursor: 'pointer',
                  background: stateOverride.length === 0 ? '#8a2be2' : 'rgba(255,255,255,0.06)',
                  color: stateOverride.length === 0 ? '#fff' : 'rgba(255,255,255,0.3)' }}>ALL</button>
              {stateOverride.map(st => (
                <button key={st} onClick={() => updateGlobalStrategy('state_override', st)}
                  style={{ fontSize: '0.6rem', fontWeight: 800, padding: '2px 7px', borderRadius: '10px', border: 'none', cursor: 'pointer', background: '#8a2be2', color: '#fff' }}
                >{st} ✕</button>
              ))}
              <select 
                value="" 
                onChange={(e) => {
                  const st = e.target.value;
                  if (st) updateGlobalStrategy('state_override', st);
                }}
                className="custom-dark"
                style={{ padding: '2px 8px', borderRadius: '10px', fontSize: '0.58rem' }}
              >
                <option value="" disabled>+</option>
                {['AL','AK','AZ','AR','CA','CO','CT','DE','FL','GA','HI','ID','IL','IN','IA','KS','KY','LA','ME','MD','MA','MI','MN','MS','MO','MT','NE','NV','NH','NJ','NM','NY','NC','ND','OH','OK','OR','PA','RI','SC','SD','TN','TX','UT','VT','VA','WA','WV','WI','WY'].filter(s => !stateOverride.includes(s)).map(st => (
                  <option key={st} value={st}>{st}</option>
                ))}
              </select>
            </div>
          </div>

          {/* Target Facilities */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '6px', marginTop: '4px' }}>
            <span style={{ fontSize: '0.56rem', fontWeight: 800, color: 'rgba(255,255,255,0.4)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Target Facilities</span>
            <div style={{ display: 'flex', gap: '6px', flexWrap: 'wrap' }}>
              {['roller_rink', 'skate_park', 'skate_shop'].map(fac => {
                const isActive = targetFacilities?.includes(fac);
                return (
                  <button key={fac} onClick={() => updateGlobalStrategy('target_facilities', fac)}
                    style={{ fontSize: '0.58rem', fontWeight: 700, padding: '3px 8px', borderRadius: '10px', border: isActive ? '1px solid #00ffaa' : '1px solid rgba(255,255,255,0.1)', cursor: 'pointer',
                      background: isActive ? 'rgba(0,255,170,0.15)' : 'rgba(255,255,255,0.02)',
                      color: isActive ? '#00ffaa' : 'rgba(255,255,255,0.4)',
                      transition: 'all 0.2s ease' }}>
                    {fac.replace('_', ' ').toUpperCase()}
                  </button>
                );
              })}
            </div>
          </div>
        </div>

        {/* ================= COLUMN 2: AI & COMPUTE GEAR ================= */}
        <div className="hud-panel" style={{
          background: 'rgba(255,255,255,0.01)',
          border: '1px solid rgba(162,0,255,0.18)',
          borderRadius: '12px',
          padding: '16px',
          boxShadow: 'inset 0 0 15px rgba(162,0,255,0.05)',
          display: 'flex',
          flexDirection: 'column',
          gap: '12px',
        }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', width: '100%', flexWrap: 'wrap', gap: '6px' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <h3 style={{ fontSize: '0.72rem', fontWeight: 900, color: '#d8b4fe', textTransform: 'uppercase', letterSpacing: '0.12em', margin: 0, display: 'flex', alignItems: 'center', gap: '6px' }}>
                <span>🤖</span> AI & Compute Gear
              </h3>
              {(() => {
                const dockerMem = (status as any)?.gpuTelemetry?.dockerMemory;
                const dockerUsed = dockerMem?.usedMB ?? 0;
                const dockerLimit = dockerMem?.limitMB ?? 4096;
                if (dockerUsed <= 0) return null;
                return (
                  <span title="Docker Container Memory Usage" style={{
                    background: 'rgba(162, 0, 255, 0.15)',
                    border: '1px solid rgba(162, 0, 255, 0.3)',
                    color: '#c77dff',
                    padding: '2px 7px',
                    borderRadius: '12px',
                    fontSize: '0.5rem',
                    fontWeight: 900,
                    letterSpacing: '0.05em',
                    boxShadow: '0 0 8px rgba(162, 0, 255, 0.1)'
                  }}>
                    🐳 DOCKER: {dockerUsed}MB / {Math.round(dockerLimit / 1024 * 10) / 10}GB
                  </span>
                );
              })()}
            </div>
            
            <button onClick={() => setActiveTab('heuristics')} title="Heuristics Control Center"
              style={{
                background: activeTab === 'heuristics' ? 'rgba(162, 0, 255, 0.2)' : 'rgba(255,255,255,0.05)',
                color: activeTab === 'heuristics' ? '#c77dff' : 'rgba(255,255,255,0.6)',
                border: activeTab === 'heuristics' ? '1px solid #c77dff' : '1px solid rgba(255,255,255,0.1)',
                padding: '3px 8px', borderRadius: '6px', cursor: 'pointer',
                fontSize: '0.55rem', fontWeight: 800, textTransform: 'uppercase', letterSpacing: '0.05em',
                transition: 'all 0.2s ease', display: 'flex', alignItems: 'center', gap: '4px'
              }}
            >
              🧠 Engine Brain
            </button>
          </div>

          {(() => {
            const lms = (status as any)?.lmsStatus;
            const gpu = (status as any)?.gpuTelemetry;
            const serverOn = lms?.serverStatus === 'ON';
            const serverMissing = lms?.serverStatus === 'MISSING';
            const loadedModels: string[] = lms?.loadedModels || [];
            const loadedModel = loadedModels[0] || null;
            const availModels: { key: string; arch: string; size: string; loaded: boolean }[] = lms?.availableModels || [];
            const capsuleColor = serverOn ? '#c77dff' : serverMissing ? '#555' : '#ff2d55';
            const glowColor = serverOn ? (loadedModel ? '#00ffaa' : '#ffb300') : '#ff2d55';
            const vramPct = gpu?.vramPercent ?? 0;
            const vramUsed = gpu?.vramUsedMB ?? 0;
            const vramTotal = gpu?.vramTotalMB ?? 8192;
            const gpuUtil = gpu?.gpuUtilPercent ?? 0;
            const cpuUtil = gpu?.cpuUtilPercent ?? 0;
            const ramPercent = gpu?.ramPercent ?? 0;
            const ramUsed = gpu?.ramUsedGB ?? 0;
            const ramTotal = gpu?.ramTotalGB ?? 16;
            const dockerMem = gpu?.dockerMemory;
            const dockerUsed = dockerMem?.usedMB ?? 0;
            const dockerLimit = dockerMem?.limitMB ?? 4096;

            const vramBarColor = vramPct > 90 ? '#ff2d55' : vramPct > 70 ? '#ffb300' : '#c77dff';
            const cpuBarColor = cpuUtil > 80 ? '#ff5a00' : cpuUtil > 40 ? '#ffb300' : '#c77dff';
            const ramBarColor = ramPercent > 90 ? '#ff2d55' : ramPercent > 70 ? '#ffb300' : '#c77dff';
            return (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '10px', width: '100%' }}>
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: '8px', width: '100%' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                    <div style={{
                      width: '6px', height: '6px', borderRadius: '50%', flexShrink: 0,
                      background: glowColor,
                      boxShadow: `0 0 6px ${glowColor}`,
                    }} />
                    <span style={{ fontSize: '0.58rem', fontWeight: 800, color: capsuleColor, letterSpacing: '0.05em' }}>
                      LM STUDIO
                    </span>
                  </div>
                  
                  <button
                    onClick={async () => {
                      try {
                        await fetch(`${API_BASE}/api/llm/server/${serverOn ? 'stop' : 'start'}`, { method: 'POST' });
                        fetchSystemStatus();
                      } catch (e) { /* swallow */ }
                    }}
                    style={{
                      fontSize: '0.52rem', fontWeight: 800, padding: '2px 6px', borderRadius: '6px',
                      border: 'none', cursor: 'pointer',
                      background: serverOn ? 'rgba(255,45,85,0.15)' : 'rgba(162,0,255,0.2)',
                      color: serverOn ? '#ff3b30' : '#c77dff',
                      marginLeft: 'auto',
                      transition: 'all 0.2s ease',
                    }}
                  >
                    {serverMissing ? 'N/A' : serverOn ? 'SHUTDOWN' : 'ACTIVATE'}
                  </button>
                </div>

                {serverOn && (
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
                    <select
                      value={loadedModel || ''}
                      onChange={async (e) => {
                        const modelKey = e.target.value;
                        if (!modelKey) return;
                        try {
                          await fetch(`${API_BASE}/api/llm/model/load`, {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({ modelKey, contextLength: 8192, gpuOffload: 'max' }),
                          });
                          fetchSystemStatus();
                        } catch (e) { /* swallow */ }
                      }}
                      title="Select model to load (auto-sets as detective model)"
                      className="custom-dark"
                      style={{ width: '100%', padding: '4px 8px' }}
                    >
                      {!loadedModel && <option value="">Load LLM Model...</option>}
                      {availModels.map((m) => (
                        <option key={m.key} value={m.key}>
                          {m.loaded ? '● ' : ''}{m.key} ({m.size})
                        </option>
                      ))}
                    </select>

                    {/* Loaded model badges */}
                    {loadedModels.map((m: string) => (
                      <span key={m} style={{
                        display: 'inline-flex', alignItems: 'center', justifyContent: 'space-between',
                        fontSize: '0.54rem', fontWeight: 700, padding: '3px 8px', borderRadius: '6px',
                        background: 'rgba(162,0,255,0.12)', color: '#d8b4fe', border: '1px solid rgba(162,0,255,0.2)',
                      }}>
                        <span style={{ overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', maxWidth: '200px' }}>
                          🧠 {m.replace(/llama-|mistralai\/|qwen|phi-/gi, '').replace('-instruct', '')}
                        </span>
                        <button
                          onClick={async (e) => {
                            e.stopPropagation();
                            try {
                              await fetch(`${API_BASE}/api/llm/model/unload`, {
                                method: 'POST',
                                headers: { 'Content-Type': 'application/json' },
                                body: JSON.stringify({ identifier: m }),
                              });
                              fetchSystemStatus();
                            } catch (e) { /* swallow */ }
                          }}
                          title={`Unload ${m}`}
                          style={{
                            fontSize: '0.52rem', fontWeight: 900, padding: '0 4px', borderRadius: '4px',
                            border: 'none', cursor: 'pointer', background: 'rgba(255,60,60,0.2)', color: '#ff6b6b',
                            marginLeft: 'auto'
                          }}
                        >✕</button>
                      </span>
                    ))}
                  </div>
                )}

                {/* 🖥️ HOST PC PERFORMANCE */}
                <div style={{ display: 'flex', flexDirection: 'column', gap: '6px', borderTop: '1px solid rgba(255,255,255,0.04)', paddingTop: '8px' }}>
                  <div style={{ fontSize: '0.52rem', fontWeight: 900, color: 'rgba(255,255,255,0.25)', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: '2px' }}>
                    🖥️ Host PC Performance
                  </div>
                  
                  {/* CPU Metrics */}
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', fontSize: '0.56rem' }}>
                    <span style={{ color: 'rgba(255,255,255,0.4)' }}>CPU Total Load</span>
                    <span style={{ color: cpuBarColor, fontWeight: 800 }}>
                      {cpuUtil}%
                    </span>
                  </div>
                  <div style={{
                    width: '100%', height: '5px', borderRadius: '3px',
                    background: 'rgba(255,255,255,0.06)', overflow: 'hidden',
                    border: '1px solid rgba(255,255,255,0.08)',
                  }}>
                    <div style={{
                      width: `${cpuUtil}%`, height: '100%', borderRadius: '3px',
                      background: `linear-gradient(90deg, ${cpuBarColor}aa, ${cpuBarColor})`,
                      transition: 'width 0.4s ease',
                    }} />
                  </div>

                  {/* RAM Metrics */}
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', fontSize: '0.56rem', marginTop: '2px' }}>
                    <span style={{ color: 'rgba(255,255,255,0.4)' }}>System RAM (Memory)</span>
                    <span style={{ color: ramBarColor, fontWeight: 800 }}>
                      {ramUsed}GB / {ramTotal}GB ({ramPercent}%)
                    </span>
                  </div>
                  <div style={{
                    width: '100%', height: '5px', borderRadius: '3px',
                    background: 'rgba(255,255,255,0.06)', overflow: 'hidden',
                    border: '1px solid rgba(255,255,255,0.08)',
                  }}>
                    <div style={{
                      width: `${ramPercent}%`, height: '100%', borderRadius: '3px',
                      background: `linear-gradient(90deg, ${ramBarColor}aa, ${ramBarColor})`,
                      transition: 'width 0.4s ease',
                    }} />
                  </div>
                </div>

                {/* ⚡ GPU & LLM ORCHESTRATION */}
                <div style={{ display: 'flex', flexDirection: 'column', gap: '6px', borderTop: '1px solid rgba(255,255,255,0.04)', paddingTop: '8px' }}>
                  <div style={{ fontSize: '0.52rem', fontWeight: 900, color: 'rgba(255,255,255,0.25)', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: '2px' }}>
                    ⚡ GPU & LLM Orchestration
                  </div>

                  {/* GPU Load Metrics */}
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', fontSize: '0.56rem' }}>
                    <span style={{ color: 'rgba(255,255,255,0.4)' }}>Core GPU Load</span>
                    <span style={{
                      fontWeight: 800,
                      color: gpuUtil > 80 ? '#ff5a00' : gpuUtil > 15 ? '#00ffaa' : 'rgba(255,255,255,0.4)',
                    }}>
                      {gpuUtil}% Load
                    </span>
                  </div>

                  {/* GPU VRAM Metrics */}
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', fontSize: '0.56rem', marginTop: '2px' }}>
                    <span style={{ color: 'rgba(255,255,255,0.4)' }}>Dedicated VRAM</span>
                    <span style={{ color: vramBarColor, fontWeight: 800 }}>
                      {Math.round(vramUsed)}MB / {vramTotal}MB ({vramPercent}%)
                    </span>
                  </div>
                  <div style={{
                    width: '100%', height: '5px', borderRadius: '3px',
                    background: 'rgba(255,255,255,0.06)', overflow: 'hidden',
                    border: '1px solid rgba(255,255,255,0.08)',
                  }}>
                    <div style={{
                      width: `${vramPercent}%`, height: '100%', borderRadius: '3px',
                      background: `linear-gradient(90deg, ${vramBarColor}aa, ${vramBarColor})`,
                      transition: 'width 0.4s ease',
                    }} />
                  </div>

                  {/* Database Sync */}
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', fontSize: '0.56rem', marginTop: '4px', borderTop: '1px dashed rgba(255,255,255,0.06)', paddingTop: '4px' }}>
                    <span style={{ color: 'rgba(255,255,255,0.4)', textTransform: 'uppercase' }}>Database Sync</span>
                    <span style={{
                      fontWeight: 800,
                      color: status?.currentTarget === 'API OFFLINE' ? '#ff2d55' : '#00ffaa',
                      textTransform: 'uppercase'
                    }}>
                      {status?.currentTarget === 'API OFFLINE' ? 'OFFLINE' : 'LIVE'}
                    </span>
                  </div>
                </div>
              </div>
            );
          })()}
        </div>

        {/* ================= COLUMN 3: PIPELINE & STEALTH ================= */}
        <div className="hud-panel" style={{
          background: 'rgba(255,255,255,0.01)',
          border: '1px solid rgba(0,212,255,0.18)',
          borderRadius: '12px',
          padding: '16px',
          boxShadow: 'inset 0 0 15px rgba(0,212,255,0.05)',
          display: 'flex',
          flexDirection: 'column',
          gap: '12px',
        }}>
          <h3 style={{ fontSize: '0.72rem', fontWeight: 900, color: '#00d4ff', textTransform: 'uppercase', letterSpacing: '0.12em', margin: 0, display: 'flex', alignItems: 'center', gap: '6px' }}>
            <span>⚙️</span> Pipeline & Stealth
          </h3>

          {/* Daemons list */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
            <span style={{ fontSize: '0.56rem', fontWeight: 800, color: 'rgba(255,255,255,0.4)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Active Phase Daemons</span>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '5px' }}>
              {[
                { id: 'indexer',          label: 'Phase 2: Detective', color: '#ff5a00', onKey: 'Indexer: online' },
                { id: 'photographer',     label: 'Phase 3: Photo',     color: '#e91e63', onKey: 'Photographer: online' },
                { id: 'publisher',        label: 'Phase 4: Publish',   color: '#00d4ff', onKey: 'Publisher: online' },
              ].map(d => {
                const isOn = status?.currentTarget?.includes(d.onKey);
                return (
                  <div key={d.id} style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', background: isOn ? 'rgba(255,255,255,0.02)' : 'rgba(0,0,0,0.15)', padding: '4px 8px', borderRadius: '6px', border: `1px solid ${isOn ? 'rgba(255,255,255,0.05)' : 'rgba(255,255,255,0.02)'}` }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                      <div style={{ background: isOn ? d.color : 'rgba(255,255,255,0.15)', boxShadow: isOn ? `0 0 6px ${d.color}` : '', width: '6px', height: '6px', borderRadius: '50%' }} />
                      <span style={{ fontSize: '0.56rem', color: isOn ? '#fff' : 'rgba(255,255,255,0.3)', fontWeight: 700 }}>{d.label.toUpperCase()}</span>
                    </div>
                    <button onClick={() => triggerSpecificDaemon(d.id, isOn ? 'stop' : 'start')}
                      style={{ fontSize: '0.52rem', fontWeight: 800, padding: '1px 5px', borderRadius: '4px', border: 'none', cursor: 'pointer', background: isOn ? 'rgba(255,60,60,0.2)' : `${d.color}22`, color: isOn ? '#ff6b6b' : d.color }}>
                      {isOn ? 'STOP' : 'GO'}
                    </button>
                  </div>
                );
              })}
            </div>
          </div>

          {/* Stealth switches */}
          <div style={{ display: 'flex', alignItems: 'center', gap: '14px', borderTop: '1px solid rgba(255,255,255,0.04)', paddingTop: '8px', paddingBottom: '2px' }}>
            <label style={{ display: 'flex', alignItems: 'center', gap: '4px', cursor: 'pointer' }}>
              <span style={{ fontSize: '0.56rem', color: 'rgba(255,255,255,0.4)', fontWeight: 700, textTransform: 'uppercase' }}>Headless</span>
              <label className="switch mini"><input type="checkbox" checked={isHeadless} onChange={e => toggleHeadless(e.target.checked)} /><span className="slider round" /></label>
            </label>
            <label style={{ display: 'flex', alignItems: 'center', gap: '4px', cursor: 'pointer' }}>
              <span style={{ fontSize: '0.56rem', color: 'rgba(255,255,255,0.4)', fontWeight: 700, textTransform: 'uppercase' }}>Spoofing</span>
              <label className="switch mini"><input type="checkbox" checked={identityRotation} onChange={e => updateGlobalStrategy('identity_rotation_enabled', e.target.checked)} /><span className="slider round" /></label>
            </label>
          </div>

          {/* Timing parameters */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '6px', borderTop: '1px solid rgba(255,255,255,0.04)', paddingTop: '8px' }}>
            <span style={{ fontSize: '0.56rem', fontWeight: 800, color: 'rgba(255,255,255,0.4)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Stealth Timing Parameters</span>
            
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '8px' }}>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '4px', alignItems: 'center' }}>
                <span style={{ fontSize: '0.5rem', color: 'rgba(255,255,255,0.3)', textTransform: 'uppercase' }}>Cooldown</span>
                <div style={{ display: 'flex', alignItems: 'center', gap: '3px' }}>
                  <input type="number" className="console-readout" style={{ width: '58px' }} value={cooldownBase} onChange={e => updateGlobalStrategy('cooldown_base_ms', parseInt(e.target.value))} />
                  <span style={{ fontSize: '0.5rem', color: 'rgba(255,255,255,0.3)' }}>ms</span>
                </div>
              </div>

              <div style={{ display: 'flex', flexDirection: 'column', gap: '4px', alignItems: 'center' }}>
                <span style={{ fontSize: '0.5rem', color: 'rgba(255,255,255,0.3)', textTransform: 'uppercase' }}>Jitter</span>
                <div style={{ display: 'flex', alignItems: 'center', gap: '3px' }}>
                  <input type="number" className="console-readout" style={{ width: '40px' }} value={cooldownJitter} onChange={e => updateGlobalStrategy('cooldown_jitter_pct', parseInt(e.target.value))} />
                  <span style={{ fontSize: '0.5rem', color: 'rgba(255,255,255,0.3)' }}>%</span>
                </div>
              </div>

              <div style={{ display: 'flex', flexDirection: 'column', gap: '4px', alignItems: 'center' }}>
                <span style={{ fontSize: '0.5rem', color: 'rgba(255,255,255,0.3)', textTransform: 'uppercase' }}>Throttle</span>
                <div style={{ display: 'flex', alignItems: 'center', gap: '3px' }}>
                  <input type="number" className="console-readout" style={{ width: '58px' }} value={sleepInterval} onChange={e => updateGlobalStrategy('sleep_interval', parseInt(e.target.value))} />
                  <span style={{ fontSize: '0.5rem', color: 'rgba(255,255,255,0.3)' }}>ms</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
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
              onBlockSpot={blockSpot}
              onSetHero={setHeroImage}
              onDeletePhoto={deleteImage}
              onAssignPhotoType={assignPhotoType}
              onUploadPhoto={uploadPhoto}
              seedProvider={seedProvider}
              onProviderChange={setSeedProvider}
              liveStreamText={liveStreamText}
              isStreaming={isStreaming}
              currentAnalyzingSpot={currentAnalyzingSpot}
              logs={logs}
              historyLogs={historyLogs}
              fetchHistory={fetchHistory}
            />

              <div style={{marginTop: '2rem'}}>
                <h4 style={{fontSize: '0.8rem', textTransform:'uppercase', color:'#ffc107', marginBottom: 0, fontWeight: 800, letterSpacing: '0.05em'}}>?? PUBLISHED TROPHY CASE (LIVE)</h4>
                  {(phaseQueues['published'] || []).length === 0 ? (
                      <div style={{ background: 'rgba(255,255,255,0.02)', padding: '2rem', textAlign: 'center', borderRadius: '8px', color: 'var(--text-secondary)', fontSize: '0.9rem', fontStyle: 'italic', marginTop: '10px' }}>
                          No published records yet. Push records through the pipeline to see them here.
                      </div>
                  ) : (
                      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(340px, 1fr))', gap: '1.25rem', padding: '0.5rem 0', marginTop: '10px' }}>
                          {(phaseQueues['published'] || []).map((spot: any) => (
                              <DatabankCard
                                  key={spot.id}
                                  spot={spot}
                                  variant="detailed"
                                  proxyImg={proxyImg}
                                  onEdit={(s) => { setEditingId(s.id); setEditForm(s); }}
                                  onReset={(id, name) => resetSpotToSeeded(id, name)}
                                  onBlock={blockSpot}
                                  onSetHero={setHeroImage}
                                  onDeletePhoto={deleteImage}
                                  onAssignPhotoType={assignPhotoType}
                                  onUploadPhoto={uploadPhoto}
                                  onPublishToggle={async (s) => { await fetch(`${API_BASE}/api/skate_spots/${s.id}`, { method:'PUT', headers:{'Content-Type':'application/json'}, body: JSON.stringify({ is_published: !s.is_published }) }); fetchSpots(page, gridFilter); fetchPipelineStatsRef.current(); }}
                              />
                          ))}
                      </div>
                  )}
            </div>

        </div>
      )}

      {true && (
      <>
      <div className="content-area fade-in">
        {/* =========== DAEMON CONTROL CENTER (PHASE 2-3) =========== */}
        {/* =========== PHASE 4: DATABANK QA =========== */}
        {['pipeline', 'graveyard'].includes(activeTab) && (
            <div className="tab-pane graveyard fade-in">
              <div style={{ marginBottom: '1rem' }}>
                <SectionHdr 
                  id="publisher_explainer" 
                  label={activeTab === 'graveyard' ? 'Graveyard: Rejected & Purged Records' : 'Publisher: Live Publish'} 
                  color={activeTab === 'graveyard' ? '#f44336' : '#4caf50'} 
                />
              </div>
              {activeTab === 'graveyard' && !isCollapsed('publisher_explainer') && (
                <div className="explainer-block" style={{marginBottom: '1rem', background: 'rgba(244, 67, 54, 0.05)', border: '1px solid rgba(244, 67, 54, 0.2)'}}>
                  <p style={{ margin: 0 }}>Review records that were blocked by the Guillotine or manually rejected. These will not be published.</p>
                </div>
              )}


              {/* =========== STATUS COVERAGE MAP =========== */}
              {activeTab !== 'graveyard' && (
                <div style={{ marginBottom: '2rem' }}>
                  <div style={{ marginBottom: isCollapsed('coverage_map') ? 0 : '0.5rem' }}>
                    <SectionHdr id="coverage_map" label="Pipeline Coverage Map" color="#ffb300" right={
                      !isCollapsed('coverage_map') && (
                        <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
                          <span style={{ fontSize: '0.65rem', color: 'rgba(255,255,255,0.4)', fontWeight: 700 }}>Priority:</span>
                          <span style={{ fontSize: '0.65rem', color: '#e91e63', fontWeight: 800 }}>■ MEDIA_READY</span>
                          <span style={{ fontSize: '0.65rem', color: '#ff9800', fontWeight: 800 }}>■ DEEP_CRAWLED</span>
                          <span style={{ fontSize: '0.65rem', color: '#8a2be2', fontWeight: 800 }}>■ SEEDED</span>
                        </div>
                      )
                    }/>
                  </div>
                  {!isCollapsed('coverage_map') && (
                    <div style={{ padding: '1.5rem', background: 'rgba(255,179,0,0.03)', border: '1px solid rgba(255,179,0,0.2)', borderRadius: '12px' }}>
                      <div style={{ display: 'flex', justifyContent: 'center', width: '100%', maxWidth: '800px', margin: '0 auto' }}>
                        {/* @ts-ignore */}
                        <USAMap
                          defaultFill="rgba(255,255,255,0.05)"
                          customize={(() => {
                            const colors: Record<string, any> = {};
                            const STATUS_PRIORITY = ['MEDIA_READY', 'DEEP_CRAWLED', 'SEEDED'];
                            const STATUS_COLOR: Record<string, string> = {
                              MEDIA_READY: '#e91e63', DEEP_CRAWLED: '#ff9800', SEEDED: '#8a2be2',
                            };
                            databankCoverage.forEach((row: any) => {
                              if (!row.state || row.state === 'UNKNOWN') return;
                              const total = row.total || 0;
                              if (total === 0) return;
                              const dominant = STATUS_PRIORITY.find(s => (row[s] || 0) > 0) || 'SEEDED';
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
                            if (st && st.length === 2) {
                              setStateChip(st);
                              fetchSpots(0, gridFilter, sortCol, sortDir, searchQuery, chips, st);
                            }
                          }}
                        />
                      </div>
                    </div>
                  )}
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
                <select className="custom-dark" style={{ width: 'auto', fontSize: '0.75rem' }} value={gridFilter} onChange={e => setGridFilter(e.target.value)}>
                  <option value="ALL">All Statuses</option>
                  <option value="PENDING_WEBSITE">PENDING_WEBSITE (Resolving)</option>
                  <option value="STALLED_WEBSITE">STALLED_WEBSITE (Stalled Resolver)</option>
                  <option value="SEEDED">SEEDED (Seeded)</option>
                  <option value="DEEP_CRAWLED">DEEP_CRAWLED (Deep Crawled)</option>
                  <option value="MEDIA_READY">MEDIA_READY (Media Ready)</option>
                  <option value="PUBLISHED">PUBLISHED (Published)</option>
                  <option value="STALLED">STALLED (Stalled)</option>
                  <option value="REJECTED">REJECTED (Graveyard)</option>
                </select>
                {/* State filter */}
                <input className="form-input" style={{ width: '68px', fontSize: '0.75rem', textTransform: 'uppercase', textAlign: 'center' }}
                  placeholder="State" maxLength={2}
                  value={stateChip}
                  onChange={e => setStateChip(e.target.value.toUpperCase())}
                />
                {/* Sort col */}
                <select className="custom-dark" style={{ width: 'auto', fontSize: '0.75rem' }}
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
            {viewMode === 'card' && (
              <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(340px, 1fr))', gap: '1.25rem', padding: '0.5rem 0' }}>
                {spots.map(spot => (
                  <DatabankCard
                    key={spot.id}
                    spot={spot}
                    variant="detailed"
                    proxyImg={proxyImg}
                    onEdit={(s) => { setEditingId(s.id); setEditForm(s); }}
                    onReset={(id, name) => resetSpotToSeeded(id, name)}
                    onBlock={blockSpot}
                    onSetHero={setHeroImage}
                    onDeletePhoto={deleteImage}
                    onAssignPhotoType={assignPhotoType}
                    onUploadPhoto={uploadPhoto}
                    onPublishToggle={async (s) => { await fetch(`${API_BASE}/api/skate_spots/${s.id}`, { method:'PUT', headers:{'Content-Type':'application/json'}, body: JSON.stringify({ is_published: !s.is_published }) }); fetchSpots(page, gridFilter); }}
                  />
                ))}
              </div>
            )}

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
                  if (!m) {
                    const m24 = s.trim().match(/^(\d{1,2}):(\d{2})$/);
                    if (m24) {
                      return parseInt(m24[1]) * 60 + parseInt(m24[2]);
                    }
                    return 0;
                  }
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
                  
                  let startStr = rng[1].trim();
                  let endStr = rng[2].trim();
                  
                  const startHasPeriod = /[AP]M/i.test(startStr);
                  const endMatch = endStr.match(/([AP]M)/i);
                  if (!startHasPeriod && endMatch) {
                    startStr = `${startStr} ${endMatch[1]}`;
                  }
                  
                  const start = p(startStr);
                  const end = p(endStr);
                  
                  if (end < start) {
                    return cur >= start || cur <= end;
                  }
                  
                  return cur >= start && cur <= end;
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
                      MEDIA_READY:'#e91e63', DEEP_CRAWLED:'#ff9800', SEEDED:'rgba(255,255,255,0.3)', REJECTED:'#f44336', STALLED:'#9c27b0'
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
                            {(() => {
                              const emails = getEmails(spot.email_addresses);
                              if (emails.length === 0) return null;
                              const firstEmail = emails[0];
                              const remaining = emails.length - 1;
                              return (
                                <a
                                  href={`mailto:${firstEmail}`}
                                  style={{
                                    fontSize: '0.56rem',
                                    fontWeight: 700,
                                    padding: '1px 6px',
                                    borderRadius: '8px',
                                    background: 'rgba(168,85,247,0.15)',
                                    color: '#c084fc',
                                    border: '1px solid rgba(168,85,247,0.25)',
                                    textDecoration: 'none',
                                    display: 'inline-flex',
                                    alignItems: 'center',
                                    gap: '2px',
                                    transition: 'all 0.2s',
                                  }}
                                  onMouseEnter={e => {
                                    e.currentTarget.style.background = 'rgba(168,85,247,0.25)';
                                    e.currentTarget.style.boxShadow = '0 0 6px rgba(168,85,247,0.4)';
                                  }}
                                  onMouseLeave={e => {
                                    e.currentTarget.style.background = 'rgba(168,85,247,0.15)';
                                    e.currentTarget.style.boxShadow = 'none';
                                  }}
                                  title={emails.join(', ')}
                                >
                                  ✉️ {firstEmail}
                                  {remaining > 0 ? ` (+${remaining})` : ''}
                                </a>
                              );
                            })()}
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
                    <th onClick={() => toggleSort('email_addresses')} style={{cursor:'pointer'}}>Emails {sortCol==='email_addresses' ? (sortDir==='asc'?'↑':'↓') : ''}</th>
                    <th onClick={() => toggleSort('has_adult_night')} style={{cursor:'pointer'}}>18+ {sortCol==='has_adult_night' ? (sortDir==='asc'?'↑':'↓') : ''}</th>
                    <th onClick={() => toggleSort('retry_count')} style={{cursor:'pointer'}}>Retries {sortCol==='retry_count' ? (sortDir==='asc'?'↑':'↓') : ''}</th>
                    <th onClick={() => toggleSort('last_attempted_at')} style={{cursor:'pointer'}}>Last Ping {sortCol==='last_attempted_at' ? (sortDir==='asc'?'↑':'↓') : ''}</th>
                  </tr>
                </thead>
                <tbody>
                  {spots.map(row => {
                    return (
                      <tr key={row.id}>
                        <td>
                          <div style={{fontWeight: 'bold', color: 'var(--text-primary)'}}>{row.name}</div>
                          <div style={{fontSize: '0.8rem', color: 'var(--text-secondary)'}}>{row.city}, {row.state}</div>
                        </td>
                        <td>
                          <div style={{fontSize: '0.85rem', color: 'var(--text-secondary)'}}>{row.street_address || '-'}</div>
                        </td>
                        <td className="status-cell">
                           <select 
                             className={`custom-dark status-pill ${row.verification_status?.toLowerCase() || 'pending'}`} 
                             value={row.verification_status || 'PENDING'} 
                             onChange={e => updateSpotStatus(row.id, e.target.value)}
                             style={{ padding: '6px 8px', borderRadius: '6px', cursor: 'pointer', appearance: 'menulist' }}
                           >
                             <option value="PENDING_WEBSITE">PENDING_WEBSITE (Resolving)</option>
                             <option value="STALLED_WEBSITE">STALLED_WEBSITE (Stalled Resolver)</option>
                             <option value="SEEDED">SEEDED (Seeded)</option>
                             <option value="DEEP_CRAWLED">DEEP_CRAWLED (Deep Crawled)</option>
                             <option value="MEDIA_READY">MEDIA_READY (Media Ready)</option>
                             <option value="PUBLISHED">PUBLISHED (Published)</option>
                             <option value="STALLED">STALLED (Stalled)</option>
                             <option value="REJECTED">REJECTED (Graveyard)</option>
                           </select>
                        </td>
                        <td>
                           {row.rating ? <span style={{color: '#ffd700', fontWeight:'bold', textShadow: '0 0 5px rgba(255,215,0,0.5)'}}>{row.rating} <span style={{fontSize: '0.7em', color: 'gray'}}>({row.user_ratings_total || 0})</span></span> : <span style={{color:'gray'}}>-</span>}
                        </td>
                        <td>
                           <span className={`surface-tag ${row.surface_quality?.toLowerCase()}`}>{row.surface_quality || '-'}</span>
                        </td>
                        <td>
                          {row.website ? <a href={row.website} target="_blank" rel="noreferrer" style={{color: 'var(--success)', fontWeight: 600}}>Visit ↗</a> : '-'}
                        </td>
                        <td>
                          {row.phone_number || '-'}
                        </td>
                        <td>
                          {(() => {
                            const emails = getEmails(row.email_addresses);
                            if (emails.length === 0) return '-';
                            return (
                              <div style={{ display: 'flex', gap: '4px', flexWrap: 'wrap', justifyContent: 'center' }}>
                                {emails.map((email, idx) => (
                                  <a
                                    key={idx}
                                    href={`mailto:${email}`}
                                    style={{
                                      fontSize: '0.75rem',
                                      fontWeight: 600,
                                      padding: '2px 8px',
                                      borderRadius: '12px',
                                      background: 'rgba(168,85,247,0.15)',
                                      color: '#c084fc',
                                      border: '1px solid rgba(168,85,247,0.25)',
                                      textDecoration: 'none',
                                      display: 'inline-flex',
                                      alignItems: 'center',
                                      gap: '4px',
                                      transition: 'all 0.2s',
                                    }}
                                    onMouseEnter={e => {
                                      e.currentTarget.style.background = 'rgba(168,85,247,0.25)';
                                      e.currentTarget.style.boxShadow = '0 0 6px rgba(168,85,247,0.4)';
                                    }}
                                    onMouseLeave={e => {
                                      e.currentTarget.style.background = 'rgba(168,85,247,0.15)';
                                      e.currentTarget.style.boxShadow = 'none';
                                    }}
                                  >
                                    ✉️ {email}
                                  </a>
                                ))}
                              </div>
                            );
                          })()}
                        </td>
                        <td>
                          <div style={{display:'flex', alignItems: 'center', gap: '5px', justifyContent: 'center'}}>
                             {row.has_adult_night ? '✅' : ''}
                             {row.adult_night_details && <span title={row.adult_night_details} style={{cursor: 'help'}}>ℹ️</span>}
                          </div>
                        </td>
                        <td>
                          <span style={{ color: row.retry_count >= 8 ? 'var(--danger)' : 'var(--text-secondary)' }}>{row.retry_count || 0}/10</span>
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
                            
                            <button className="btn-icon" onClick={() => startEdit(row)}>✏️</button>
                            
                            {activeTab === 'graveyard' ? (
                                <button className="btn-icon" onClick={() => restoreSpot(row.id, row.name)} title="Restore" style={{color: '#4caf50'}}>🔄</button>
                              ) : (
                                <>
                                  <button
                                    className="btn-icon"
                                    onClick={() => resetSpotToSeeded(row.id, row.name)}
                                    title="Reset to SEEDED ↺ re-enter Detective queue"
                                    disabled={resettingIds[row.id] === 'loading' || resettingIds[row.id] === 'success'}
                                    style={{ color: resettingIds[row.id] === 'success' ? '#4caf50' : '#ffb300', fontSize: '0.85rem', cursor: resettingIds[row.id] ? 'default' : 'pointer', opacity: resettingIds[row.id] === 'loading' ? 0.5 : 1 }}
                                  >
                                    {resettingIds[row.id] === 'loading' ? '⏳' : resettingIds[row.id] === 'success' ? '✓' : '↺'}
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

            {activeTab === 'pipeline' && logs && historyLogs && fetchHistory && (
              <div style={{ marginTop: '2rem' }}>
                <SectionHdr id="omni_terminal" label="Omni-Terminal (Global Scope)" color="#00ffaa" />
                {!isCollapsed('omni_terminal') && (
                  <OmniTerminal logs={logs} historyLogs={historyLogs} fetchHistory={fetchHistory} />
                )}
              </div>
            )}
          </div>
        )}
      </div>

      {activeTab === 'phase2' && (
        <div className="tab-pane phase-detective" style={{ width: '100%', marginBottom: '1.5rem' }}>
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

      {activeTab === 'sniper' && (
        <div className="tab-pane phase-sniper" style={{ height: '80vh', width: '100%', borderRadius: '12px', overflow: 'hidden', border: '1px solid #2D3340', boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.25)' }}>
          <SniperBench />
        </div>
      )}

      {activeTab === 'heuristics' && (
        <div className="tab-pane phase-heuristics" style={{ height: '82vh', width: '100%', borderRadius: '12px', overflow: 'hidden', border: '1px solid #2D3340', boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.25)' }}>
          <HeuristicsControlCenter />
        </div>
      )}





      {/* Record Edit Modal Overlay */}
      {editingId && editForm && (
        <RecordEditModal 
          spot={editForm} 
          onSave={handleModalSave} 
          onClose={() => { setEditingId(null); setEditForm({}); }} 
        />
      )}

      {/* Bulk Reset / Requeue Wizard Modal Overlay */}
      {showBulkResetModal && (
        <div style={{
          position: 'fixed',
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          background: 'rgba(5, 5, 8, 0.85)',
          backdropFilter: 'blur(8px)',
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          zIndex: 10000,
          animation: 'fadeIn 0.2s ease-out'
        }}>
          <div style={{
            width: '100%',
            maxWidth: '520px',
            background: 'rgba(18, 18, 28, 0.96)',
            border: '1px solid rgba(255, 179, 0, 0.35)',
            boxShadow: '0 24px 64px rgba(0, 0, 0, 0.7), 0 0 20px rgba(255, 179, 0, 0.1)',
            borderRadius: '16px',
            overflow: 'hidden',
            display: 'flex',
            flexDirection: 'column',
          }}>
            {/* Header */}
            <div style={{
              background: 'linear-gradient(90deg, rgba(255, 179, 0, 0.15) 0%, rgba(138, 43, 226, 0.15) 100%)',
              padding: '16px 20px',
              borderBottom: '1px solid rgba(255, 255, 255, 0.08)',
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center'
            }}>
              <h3 style={{ margin: 0, fontSize: '1.05rem', fontWeight: 900, color: '#ffb300', textTransform: 'uppercase', letterSpacing: '0.08em', display: 'flex', alignItems: 'center', gap: '8px' }}>
                ⚙️ Pipeline Requeue Wizard
              </h3>
              <button 
                onClick={() => setShowBulkResetModal(false)}
                style={{ background: 'none', border: 'none', color: 'rgba(255,255,255,0.4)', fontSize: '1.4rem', cursor: 'pointer', outline: 'none' }}
              >
                ×
              </button>
            </div>

            {/* Content */}
            <div style={{ padding: '20px', display: 'flex', flexDirection: 'column', gap: '18px', maxHeight: '70vh', overflowY: 'auto' }}>
              
              {/* Target Filters Indicator */}
              <div style={{ background: 'rgba(255, 255, 255, 0.03)', border: '1px solid rgba(255, 255, 255, 0.06)', borderRadius: '8px', padding: '12px 14px' }}>
                <div style={{ fontSize: '0.65rem', fontWeight: 800, color: 'rgba(255,255,255,0.3)', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: '8px' }}>Active Selection Filters</div>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '12px' }}>
                  <div>
                    <span style={{ fontSize: '0.7rem', color: 'rgba(255,255,255,0.5)', marginRight: '6px' }}>States:</span>
                    <span style={{ fontSize: '0.75rem', fontWeight: 800, color: '#00ffaa' }}>
                      {stateOverride.length > 0 ? stateOverride.join(', ') : 'ALL STATES'}
                    </span>
                  </div>
                  <div style={{ width: 1, background: 'rgba(255,255,255,0.1)', height: '14px' }} />
                  <div>
                    <span style={{ fontSize: '0.7rem', color: 'rgba(255,255,255,0.5)', marginRight: '6px' }}>Facilities:</span>
                    <span style={{ fontSize: '0.75rem', fontWeight: 800, color: '#00ffaa' }}>
                      {targetFacilities.length > 0 ? targetFacilities.map(f => f.replace('_', ' ').toUpperCase()).join(', ') : 'ALL TYPES'}
                    </span>
                  </div>
                </div>
              </div>

              {/* Target Reset-To Phase Selector */}
              <div>
                <div style={{ fontSize: '0.65rem', fontWeight: 800, color: 'rgba(255,255,255,0.3)', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: '10px' }}>Choose Target Phase (Reset To)</div>
                
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '8px' }}>
                  
                  {/* PENDING_WEBSITE Card */}
                  <div 
                    onClick={() => setResetToTarget('PENDING_WEBSITE')}
                    style={{
                      padding: '14px 6px',
                      borderRadius: '12px',
                      background: resetToTarget === 'PENDING_WEBSITE' ? 'rgba(59, 130, 246, 0.12)' : 'rgba(255,255,255,0.02)',
                      border: resetToTarget === 'PENDING_WEBSITE' ? '2px solid #3b82f6' : '1px solid rgba(255,255,255,0.06)',
                      boxShadow: resetToTarget === 'PENDING_WEBSITE' ? '0 0 16px rgba(59, 130, 246, 0.35)' : 'none',
                      cursor: 'pointer',
                      textAlign: 'center',
                      transition: 'all 0.25s cubic-bezier(0.4, 0, 0.2, 1)',
                      display: 'flex',
                      flexDirection: 'column',
                      gap: '5px',
                      alignItems: 'center',
                      justifyContent: 'center',
                      transform: resetToTarget === 'PENDING_WEBSITE' ? 'scale(1.03)' : 'scale(1)',
                    }}
                  >
                    <span style={{ fontSize: '1.4rem' }}>🔍</span>
                    <span style={{ fontSize: '0.7rem', fontWeight: 900, color: resetToTarget === 'PENDING_WEBSITE' ? '#3b82f6' : 'rgba(255,255,255,0.6)', letterSpacing: '0.01em', whiteSpace: 'nowrap' }}>WEBSITE</span>
                    <span style={{ fontSize: '0.5rem', color: 'rgba(255,255,255,0.35)', lineHeight: 1.2 }}>Phase 1: Resolver</span>
                  </div>

                  {/* SEEDED Card */}
                  <div 
                    onClick={() => setResetToTarget('SEEDED')}
                    style={{
                      padding: '14px 6px',
                      borderRadius: '12px',
                      background: resetToTarget === 'SEEDED' ? 'rgba(138, 43, 226, 0.12)' : 'rgba(255,255,255,0.02)',
                      border: resetToTarget === 'SEEDED' ? '2px solid #8a2be2' : '1px solid rgba(255,255,255,0.06)',
                      boxShadow: resetToTarget === 'SEEDED' ? '0 0 16px rgba(138, 43, 226, 0.35)' : 'none',
                      cursor: 'pointer',
                      textAlign: 'center',
                      transition: 'all 0.25s cubic-bezier(0.4, 0, 0.2, 1)',
                      display: 'flex',
                      flexDirection: 'column',
                      gap: '5px',
                      alignItems: 'center',
                      justifyContent: 'center',
                      transform: resetToTarget === 'SEEDED' ? 'scale(1.03)' : 'scale(1)',
                    }}
                  >
                    <span style={{ fontSize: '1.4rem' }}>🔎</span>
                    <span style={{ fontSize: '0.7rem', fontWeight: 900, color: resetToTarget === 'SEEDED' ? '#a255ff' : 'rgba(255,255,255,0.6)', letterSpacing: '0.01em', whiteSpace: 'nowrap' }}>SEEDED</span>
                    <span style={{ fontSize: '0.5rem', color: 'rgba(255,255,255,0.35)', lineHeight: 1.2 }}>Phase 2: Detective</span>
                  </div>

                  {/* DEEP_CRAWLED Card */}
                  <div 
                    onClick={() => setResetToTarget('DEEP_CRAWLED')}
                    style={{
                      padding: '14px 6px',
                      borderRadius: '12px',
                      background: resetToTarget === 'DEEP_CRAWLED' ? 'rgba(255, 152, 0, 0.12)' : 'rgba(255,255,255,0.02)',
                      border: resetToTarget === 'DEEP_CRAWLED' ? '2px solid #ff9800' : '1px solid rgba(255,255,255,0.06)',
                      boxShadow: resetToTarget === 'DEEP_CRAWLED' ? '0 0 16px rgba(255, 152, 0, 0.35)' : 'none',
                      cursor: 'pointer',
                      textAlign: 'center',
                      transition: 'all 0.25s cubic-bezier(0.4, 0, 0.2, 1)',
                      display: 'flex',
                      flexDirection: 'column',
                      gap: '5px',
                      alignItems: 'center',
                      justifyContent: 'center',
                      transform: resetToTarget === 'DEEP_CRAWLED' ? 'scale(1.03)' : 'scale(1)',
                    }}
                  >
                    <span style={{ fontSize: '1.4rem' }}>📸</span>
                    <span style={{ fontSize: '0.7rem', fontWeight: 900, color: resetToTarget === 'DEEP_CRAWLED' ? '#ffb74d' : 'rgba(255,255,255,0.6)', letterSpacing: '0.01em', whiteSpace: 'nowrap' }}>CRAWLED</span>
                    <span style={{ fontSize: '0.5rem', color: 'rgba(255,255,255,0.35)', lineHeight: 1.2 }}>Phase 3: Photo</span>
                  </div>

                  {/* MEDIA_READY Card */}
                  <div 
                    onClick={() => setResetToTarget('MEDIA_READY')}
                    style={{
                      padding: '14px 6px',
                      borderRadius: '12px',
                      background: resetToTarget === 'MEDIA_READY' ? 'rgba(233, 30, 99, 0.12)' : 'rgba(255,255,255,0.02)',
                      border: resetToTarget === 'MEDIA_READY' ? '2px solid #e91e63' : '1px solid rgba(255,255,255,0.06)',
                      boxShadow: resetToTarget === 'MEDIA_READY' ? '0 0 16px rgba(233, 30, 99, 0.35)' : 'none',
                      cursor: 'pointer',
                      textAlign: 'center',
                      transition: 'all 0.25s cubic-bezier(0.4, 0, 0.2, 1)',
                      display: 'flex',
                      flexDirection: 'column',
                      gap: '5px',
                      alignItems: 'center',
                      justifyContent: 'center',
                      transform: resetToTarget === 'MEDIA_READY' ? 'scale(1.03)' : 'scale(1)',
                    }}
                  >
                    <span style={{ fontSize: '1.4rem' }}>🚀</span>
                    <span style={{ fontSize: '0.7rem', fontWeight: 900, color: resetToTarget === 'MEDIA_READY' ? '#f06292' : 'rgba(255,255,255,0.6)', letterSpacing: '0.01em', whiteSpace: 'nowrap' }}>READY</span>
                    <span style={{ fontSize: '0.5rem', color: 'rgba(255,255,255,0.35)', lineHeight: 1.2 }}>Phase 4: Publish</span>
                  </div>

                </div>
              </div>

              {/* Status Toggles */}
              <div>
                <div style={{ fontSize: '0.65rem', fontWeight: 800, color: 'rgba(255,255,255,0.3)', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: '10px' }}>Select Pipeline Stages to Re-Seed</div>
                
                <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                  
                  {/* PENDING_WEBSITE */}
                  <label style={{
                    display: 'flex',
                    alignItems: 'flex-start',
                    gap: '12px',
                    padding: '10px 12px',
                    borderRadius: '8px',
                    background: resetStatuses.PENDING_WEBSITE ? 'rgba(59, 130, 246, 0.06)' : 'transparent',
                    border: resetStatuses.PENDING_WEBSITE ? '1px solid rgba(59, 130, 246, 0.2)' : '1px solid rgba(255,255,255,0.05)',
                    cursor: 'pointer',
                    transition: 'all 0.2s'
                  }}>
                    <input 
                      type="checkbox" 
                      style={{ marginTop: '3px', cursor: 'pointer' }}
                      checked={resetStatuses.PENDING_WEBSITE}
                      onChange={e => setResetStatuses(prev => ({ ...prev, PENDING_WEBSITE: e.target.checked }))}
                    />
                    <div>
                      <div style={{ fontSize: '0.78rem', fontWeight: 800, color: '#3b82f6' }}>PENDING_WEBSITE</div>
                      <div style={{ fontSize: '0.68rem', color: 'rgba(255,255,255,0.5)', marginTop: '2px' }}>Spots currently waiting in queue for website discovery (Phase 1).</div>
                    </div>
                  </label>

                  {/* WEBSITE_STALLED */}
                  <label style={{
                    display: 'flex',
                    alignItems: 'flex-start',
                    gap: '12px',
                    padding: '10px 12px',
                    borderRadius: '8px',
                    background: resetStatuses.WEBSITE_STALLED ? 'rgba(255, 90, 0, 0.06)' : 'transparent',
                    border: resetStatuses.WEBSITE_STALLED ? '1px solid rgba(255, 90, 0, 0.2)' : '1px solid rgba(255,255,255,0.05)',
                    cursor: 'pointer',
                    transition: 'all 0.2s'
                  }}>
                    <input 
                      type="checkbox" 
                      style={{ marginTop: '3px', cursor: 'pointer' }}
                      checked={resetStatuses.WEBSITE_STALLED}
                      onChange={e => setResetStatuses(prev => ({ ...prev, WEBSITE_STALLED: e.target.checked }))}
                    />
                    <div>
                      <div style={{ fontSize: '0.78rem', fontWeight: 800, color: '#ff5a00' }}>WEBSITE_STALLED</div>
                      <div style={{ fontSize: '0.68rem', color: 'rgba(255,255,255,0.5)', marginTop: '2px' }}>Spots that stalled during website discovery (Phase 1).</div>
                    </div>
                  </label>

                  {/* DEEP_CRAWLED */}
                  <label style={{
                    display: 'flex',
                    alignItems: 'flex-start',
                    gap: '12px',
                    padding: '10px 12px',
                    borderRadius: '8px',
                    background: resetStatuses.DEEP_CRAWLED ? 'rgba(255, 152, 0, 0.06)' : 'transparent',
                    border: resetStatuses.DEEP_CRAWLED ? '1px solid rgba(255, 152, 0, 0.2)' : '1px solid rgba(255,255,255,0.05)',
                    cursor: 'pointer',
                    transition: 'all 0.2s'
                  }}>
                    <input 
                      type="checkbox" 
                      style={{ marginTop: '3px', cursor: 'pointer' }}
                      checked={resetStatuses.DEEP_CRAWLED}
                      onChange={e => setResetStatuses(prev => ({ ...prev, DEEP_CRAWLED: e.target.checked }))}
                    />
                    <div>
                      <div style={{ fontSize: '0.78rem', fontWeight: 800, color: '#ff9800' }}>DEEP_CRAWLED</div>
                      <div style={{ fontSize: '0.68rem', color: 'rgba(255,255,255,0.5)', marginTop: '2px' }}>Spots successfully analyzed by AI Detective but not yet photographed/published.</div>
                    </div>
                  </label>

                  {/* MEDIA_READY */}
                  <label style={{
                    display: 'flex',
                    alignItems: 'flex-start',
                    gap: '12px',
                    padding: '10px 12px',
                    borderRadius: '8px',
                    background: resetStatuses.MEDIA_READY ? 'rgba(233, 30, 99, 0.06)' : 'transparent',
                    border: resetStatuses.MEDIA_READY ? '1px solid rgba(233, 30, 99, 0.2)' : '1px solid rgba(255,255,255,0.05)',
                    cursor: 'pointer',
                    transition: 'all 0.2s'
                  }}>
                    <input 
                      type="checkbox" 
                      style={{ marginTop: '3px', cursor: 'pointer' }}
                      checked={resetStatuses.MEDIA_READY}
                      onChange={e => setResetStatuses(prev => ({ ...prev, MEDIA_READY: e.target.checked }))}
                    />
                    <div>
                      <div style={{ fontSize: '0.78rem', fontWeight: 800, color: '#e91e63' }}>MEDIA_READY</div>
                      <div style={{ fontSize: '0.68rem', color: 'rgba(255,255,255,0.5)', marginTop: '2px' }}>Spots with all media successfully downloaded and ready for live publication.</div>
                    </div>
                  </label>

                  {/* STALLED */}
                  <label style={{
                    display: 'flex',
                    alignItems: 'flex-start',
                    gap: '12px',
                    padding: '10px 12px',
                    borderRadius: '8px',
                    background: resetStatuses.STALLED ? 'rgba(156, 39, 176, 0.06)' : 'transparent',
                    border: resetStatuses.STALLED ? '1px solid rgba(156, 39, 176, 0.2)' : '1px solid rgba(255,255,255,0.05)',
                    cursor: 'pointer',
                    transition: 'all 0.2s'
                  }}>
                    <input 
                      type="checkbox" 
                      style={{ marginTop: '3px', cursor: 'pointer' }}
                      checked={resetStatuses.STALLED}
                      onChange={e => setResetStatuses(prev => ({ ...prev, STALLED: e.target.checked }))}
                    />
                    <div>
                      <div style={{ fontSize: '0.78rem', fontWeight: 800, color: '#9c27b0' }}>STALLED</div>
                      <div style={{ fontSize: '0.68rem', color: 'rgba(255,255,255,0.5)', marginTop: '2px' }}>Spots that failed in the crawler (timeouts, network errors) or ran out of retries.</div>
                    </div>
                  </label>

                  {/* REJECTED */}
                  <label style={{
                    display: 'flex',
                    alignItems: 'flex-start',
                    gap: '12px',
                    padding: '10px 12px',
                    borderRadius: '8px',
                    background: resetStatuses.REJECTED ? 'rgba(244, 67, 54, 0.06)' : 'transparent',
                    border: resetStatuses.REJECTED ? '1px solid rgba(244, 67, 54, 0.3)' : '1px solid rgba(255,255,255,0.05)',
                    cursor: 'pointer',
                    transition: 'all 0.2s'
                  }}>
                    <input 
                      type="checkbox" 
                      style={{ marginTop: '3px', cursor: 'pointer' }}
                      checked={resetStatuses.REJECTED}
                      onChange={e => setResetStatuses(prev => ({ ...prev, REJECTED: e.target.checked }))}
                    />
                    <div>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                        <div style={{ fontSize: '0.78rem', fontWeight: 800, color: '#f44336' }}>REJECTED</div>
                        <span style={{ fontSize: '0.55rem', background: '#f4433622', color: '#f44336', padding: '1px 5px', borderRadius: '4px', fontWeight: 900 }}>⚠️ DANGER</span>
                      </div>
                      <div style={{ fontSize: '0.68rem', color: 'rgba(255,255,255,0.5)', marginTop: '2px' }}>
                        Records filtered by early content/name bouncers (e.g. municipal ice rinks). 
                        <strong style={{ color: 'rgba(255,255,255,0.85)', display: 'block', marginTop: '2px' }}>
                          Warning: Keeping this unchecked prevents re-evaluating known invalid targets.
                        </strong>
                      </div>
                    </div>
                  </label>

                </div>
              </div>

            </div>

            {/* Actions */}
            <div style={{
              background: 'rgba(0, 0, 0, 0.2)',
              padding: '16px 20px',
              borderTop: '1px solid rgba(255, 255, 255, 0.06)',
              display: 'flex',
              justifyContent: 'flex-end',
              gap: '10px'
            }}>
              <button
                onClick={() => setShowBulkResetModal(false)}
                style={{
                  background: 'rgba(255,255,255,0.05)',
                  color: 'rgba(255,255,255,0.7)',
                  border: '1px solid rgba(255,255,255,0.1)',
                  padding: '6px 14px',
                  borderRadius: '6px',
                  cursor: 'pointer',
                  fontSize: '0.75rem',
                  fontWeight: 700
                }}
              >
                CANCEL
              </button>
              <button
                onClick={bulkResetToSeeded}
                disabled={isBulkResetting || !Object.values(resetStatuses).some(Boolean)}
                style={{
                  background: isBulkResetting ? 'rgba(255, 179, 0, 0.4)' : '#ffb300',
                  color: isBulkResetting ? 'rgba(255,255,255,0.6)' : '#0c0c14',
                  border: 'none',
                  padding: '6px 16px',
                  borderRadius: '6px',
                  cursor: (isBulkResetting || !Object.values(resetStatuses).some(Boolean)) ? 'not-allowed' : 'pointer',
                  fontSize: '0.75rem',
                  fontWeight: 900,
                  textTransform: 'uppercase'
                }}
              >
                {isBulkResetting ? '🔄 Resetting...' : '↺ Confirm Requeue'}
              </button>
            </div>

          </div>
        </div>
      )}

      </>
      )}
    </div>
  );
}

export default App;


