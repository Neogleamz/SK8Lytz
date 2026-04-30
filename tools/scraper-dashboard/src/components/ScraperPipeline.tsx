import React from 'react';
import BeltNode from './BeltNode';
import { useScraperTelemetry } from '../hooks/useScraperTelemetry';
import { useFieldRegistry } from '../hooks/useFieldRegistry';

const CCTOWER = 'http://localhost:5999';

const STYLES = `
/* ... styles remain the same ... */
.pipeline-dashboard-container {

    --neon-scout: #00ffaa;
    --neon-crawl: #9d4edd;
    --neon-detective: #ff6a00;
    --neon-photo: #ff007f;
    --neon-publish: #00d4ff;
    font-family: 'Outfit', sans-serif; 
}
.pipeline-dashboard-container .glass-panel { 
    background: linear-gradient(145deg, rgba(255,255,255,0.03) 0%, rgba(255,255,255,0.01) 100%);
    border: 1px solid rgba(255, 255, 255, 0.05); 
    box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5), inset 0 1px 0 rgba(255,255,255,0.1);
    border-radius: 20px; backdrop-filter: blur(20px);
}
.pipeline-dashboard-container .data-font { font-family: 'JetBrains Mono', monospace; }

.pipeline-dashboard-container .assembly-line { display: grid; grid-template-columns: 1fr 60px auto 60px 1fr 300px; align-items: center; justify-items: center; width: 100%; padding: 10px 0 30px; position: relative; gap: 0 20px; }

.pipeline-dashboard-container .mini-card { min-width: 160px; max-width: 160px; height: 60px; background: rgba(10,10,15,0.9); border: 1px solid rgba(255,255,255,0.1); border-radius: 8px; z-index: 1; display: flex; flex-direction: column; justify-content: center; padding: 0 16px; opacity: 0.5; box-shadow: 0 4px 10px rgba(0,0,0,0.5); }
.pipeline-dashboard-container .mini-card .title { font-size: 0.6rem; font-weight: 900; color: #fff; text-transform: uppercase; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.pipeline-dashboard-container .mini-card .subtitle { font-size: 0.55rem; font-family: 'JetBrains Mono', monospace; color: rgba(255,255,255,0.4); margin-top: 4px; }

/* Detailed Output Card */
.pipeline-dashboard-container .detailed-card { min-width: 320px; max-width: 360px; background: rgba(10,10,15,0.95); border: 1px solid var(--col-color); border-radius: 12px; z-index: 1; display: flex; flex-direction: column; padding: 16px; box-shadow: 0 15px 35px rgba(0,0,0,0.6), inset 0 0 20px rgba(var(--col-color-rgb), 0.15); flex-shrink: 0; position: relative; overflow: hidden; }
.pipeline-dashboard-container .detailed-card::before { content: '✓'; position: absolute; top: -10px; right: -10px; width: 24px; height: 24px; background: var(--col-color); color: #000; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-weight: 900; font-size: 12px; box-shadow: 0 0 15px var(--col-color); }
.pipeline-dashboard-container .detailed-card-header { font-size: 0.75rem; font-weight: 900; color: #fff; text-transform: uppercase; border-bottom: 1px solid rgba(255,255,255,0.1); padding-bottom: 10px; margin-bottom: 12px; display: flex; justify-content: space-between; position: relative; z-index: 2; }

.pipeline-dashboard-container .data-grid { display: grid; grid-template-columns: 100px 1fr; gap: 6px; font-family: 'JetBrains Mono', monospace; font-size: 0.65rem; position: relative; z-index: 2; }
.pipeline-dashboard-container .data-key { color: rgba(255,255,255,0.4); text-transform: uppercase; font-weight: bold; }
.pipeline-dashboard-container .data-val { color: rgba(255,255,255,0.9); word-break: break-all; }
.pipeline-dashboard-container .data-val.missing { color: #ff3366; font-style: italic; opacity: 0.8; }
.pipeline-dashboard-container .data-val.success { color: var(--col-color); font-weight: bold; }

/* Active Machine Node */
.pipeline-dashboard-container .active-machine-node { width: 380px; flex-shrink: 0; background: linear-gradient(180deg, rgba(20,20,30,0.95) 0%, rgba(10,10,15,0.98) 100%); border: 1px solid var(--col-color); border-radius: 16px; position: relative; z-index: 10; box-shadow: 0 0 40px rgba(var(--col-color-rgb), 0.2), inset 0 0 20px rgba(var(--col-color-rgb), 0.1); display: flex; flex-direction: column; }
.pipeline-dashboard-container .active-machine-node::before { content: ''; position: absolute; top: -1px; left: 50%; transform: translateX(-50%); width: 50%; height: 2px; background: var(--col-color); box-shadow: 0 0 15px var(--col-color); }
.pipeline-dashboard-container .machine-header { padding: 14px 20px; border-bottom: 1px solid rgba(255,255,255,0.1); display: flex; justify-content: space-between; align-items: center; background: rgba(0,0,0,0.2); border-radius: 16px 16px 0 0; }
.pipeline-dashboard-container .machine-body { padding: 20px; flex: 1; display: flex; flex-direction: column; justify-content: space-between; }
.pipeline-dashboard-container .processing-indicator { display: flex; align-items: center; justify-content: center; width: 30px; height: 30px; border-radius: 50%; border: 2px dashed var(--col-color); animation: spin 4s linear infinite; }
@keyframes spin { 100% { transform: rotate(360deg); } }

/* Flow Animations */
@keyframes flowRight { 0% { transform: translateX(-5px); opacity: 0; } 50% { opacity: 1; } 100% { transform: translateX(5px); opacity: 0; } }
.pipeline-dashboard-container .flow-arrow-right { animation: flowRight 1.5s infinite linear; font-size: 2rem; color: var(--col-color); opacity: 0.6; text-shadow: 0 0 15px var(--col-color); display: flex; align-items: center; justify-content: center; width: 60px; }

@keyframes flowDown { 0% { transform: translateY(-3px); opacity: 0; } 50% { opacity: 1; } 100% { transform: translateY(3px); opacity: 0; } }
.pipeline-dashboard-container .flow-arrow-down { animation: flowDown 1.5s infinite linear; font-size: 1.5rem; color: var(--col-color); opacity: 0.6; text-shadow: 0 0 10px var(--col-color); text-align: center; line-height: 1; }

/* Checklist */
.pipeline-dashboard-container .targeting-list { margin-top: 10px; margin-bottom: 15px; background: rgba(0,0,0,0.4); border: 1px solid rgba(255,255,255,0.05); padding: 12px; border-radius: 8px; font-family: 'JetBrains Mono', monospace; font-size: 0.65rem; color: #a8b2d1; display: grid; grid-template-columns: 1fr 1fr; gap: 4px; }
.pipeline-dashboard-container .targeting-title { font-size: 0.65rem; text-transform: uppercase; font-weight: 900; color: var(--col-color); margin-bottom: 8px; letter-spacing: 0.1em; grid-column: 1 / -1; display: flex; justify-content: space-between; align-items: center; }

/* Gatekeeper Rules */
.pipeline-dashboard-container .gatekeeper-rules { margin-top: 10px; background: rgba(255,51,102,0.05); border: 1px solid rgba(255,51,102,0.2); padding: 12px; border-radius: 8px; font-family: 'JetBrains Mono', monospace; font-size: 0.65rem; display: flex; flex-direction: column; gap: 4px; }
.pipeline-dashboard-container .gatekeeper-title { font-size: 0.65rem; text-transform: uppercase; font-weight: 900; color: #ff3366; margin-bottom: 4px; letter-spacing: 0.1em; display: flex; align-items: center; gap: 4px; }
.pipeline-dashboard-container .rule-item { color: rgba(255,255,255,0.7); display: flex; align-items: center; gap: 6px; }
.pipeline-dashboard-container .rule-item::before { content: '■'; color: #ff3366; font-size: 8px; }

.pipeline-dashboard-container .target-item { display: flex; align-items: center; gap: 6px; }
.pipeline-dashboard-container .target-item::before { content: '[ ]'; color: rgba(255,255,255,0.3); font-weight: bold; }
.pipeline-dashboard-container .target-item.done::before { content: '[✓]'; color: var(--col-color); }
.pipeline-dashboard-container .target-item.fail::before { content: '[✗]'; color: #ff3366; }
.pipeline-dashboard-container .target-item.done { color: #fff; }

.pipeline-dashboard-container .phase-map-container { background: rgba(10,10,15,0.8); border: 1px solid rgba(255,255,255,0.05); border-radius: 16px; display: flex; flex-direction: column; align-items: center; justify-content: center; position: relative; overflow: hidden; margin-left: 20px; box-shadow: inset 50px 0 50px -50px var(--col-color), 0 10px 30px rgba(0,0,0,0.5); }
.pipeline-dashboard-container .phase-map-container::before { content: ''; position: absolute; top: 0; left: 0; width: 2px; height: 100%; background: var(--col-color); box-shadow: 0 0 20px var(--col-color); }
.pipeline-dashboard-container .map-svg path { fill: rgba(255,255,255,0.02); stroke: var(--col-color); stroke-width: 0.5; stroke-opacity: 0.3; transition: all 0.3s; }
.pipeline-dashboard-container .map-svg path.active { fill: var(--col-color); fill-opacity: 0.4; stroke-opacity: 1; }
.pipeline-dashboard-container .belt-wrapper { display: flex; gap: 20px; align-items: stretch; position: relative; padding: 20px 0; border-bottom: 1px dashed rgba(255,255,255,0.05); }
.pipeline-dashboard-container .belt-flow-area { flex: 1; overflow: hidden; position: relative; display: flex; flex-direction: column; }
`;

const generateUniformBelt = (idx: number, id: number, name: string, color: string, rgb: string, daemon: string, job: string, target: string, inputStatus: string, outputStatus: string, states: string[]) => ({
  id, name, color, rgb, activeStates: states, job, daemon, target, inputStatus, outputStatus,
  inQ: [],
  status: 'IDLE',
  gatekeeper: [],
  attempting: [] as [string, string, string?][],
  outCards: []
});

// Belt id → app tab mapping
const BELT_TAB: Record<number, string> = { 1: 'phase1', 2: 'phase2', 3: 'phase3', 4: 'phase4', 5: 'sniper' };

export const ScraperPipeline: React.FC<{
    headerControls?: React.ReactNode;
    belowHeader?: React.ReactNode;
    pipelineStats?: any;
    phaseQueues?: any;
    onPhaseNav?: (tab: string) => void;
    // Daemon control props
    status?: any;
    triggerSpecificDaemon?: (name: string, action: 'start' | 'stop') => void;
    triggerHarvest?: (type: string, states?: string[]) => void;
    onBlockSpot?: () => void; // optional refresh callback after block
}> = ({ headerControls, belowHeader, pipelineStats, phaseQueues, onPhaseNav, status, triggerSpecificDaemon, triggerHarvest, onBlockSpot }) => {
    const { telemetry, config, loading, pulse } = useScraperTelemetry(2000);
    const { fields } = useFieldRegistry();

    // outCards show OUTPUT (completed) records for each belt.
    // The output of phase N = the input of phase N+1, so we read the next phase queue.
    const getSpotsForPhase = (beltId: number, count: number = 2) => {
        let spots: any[] = [];
        if (beltId === 1) spots = phaseQueues?.phase1 || [];              // Sweep out  = SEEDED (Detective input)
        else if (beltId === 2) spots = phaseQueues?.['detective-recent'] || []; // Detective out = DEEP_CRAWLED (Photographer input)
        else if (beltId === 3) spots = phaseQueues?.phase4 || [];              // Photographer out = MEDIA_READY (Publisher input)
        else if (beltId === 4) spots = phaseQueues?.recent || [];              // Publisher out = PUBLISHED
        return spots.slice(0, count);
    };

    const getQueueNames = (phase: string, count: number = 3) => (phaseQueues?.[phase] || []).slice(0, count).map((s: any) => s.name || s.url || s.target);

    // Fields to NEVER show in belt output cards — too bulky, use Phase drawer instead
    const CARD_SKIP_FIELDS = new Set([
        'candidate_photos', 'photos', 'ai_metadata', 'raw_data',
        'candidate_links', 'raw_knowledge_panel', 'flyer_urls', 'dom_images'
    ]);

    const buildPhaseCards = (phaseId: number, spots: any[]) => {
        return spots.map(spot => {
            const data: [string, React.ReactNode, string][] = [];
            const bool = (v: any) => (v === true ? 'YES' : v === false ? 'NO' : 'N/A');
            const val  = (v: any, fallback = 'NULL') => v != null ? String(v) : fallback;
            const ok   = (v: any): string => (v != null && v !== '' && v !== false) ? 'success' : 'missing';
            const boolOk = (v: any): string => (v === true ? 'success' : 'missing');

            // ▶ CURRENT STATUS badge
            if (phaseId === 1) {
                const s1 = spot.verification_status || 'SEEDED';
                data.push(['\u25b6 CURRENT STATUS', s1, s1 === 'SEEDED' ? 'success' : 'warning']);
            } else if (phaseId === 2) {
                const s3 = spot.verification_status || 'DEEP_CRAWLED';
                data.push(['\u25b6 CURRENT STATUS', s3, s3 === 'DEEP_CRAWLED' ? 'success' : 'warning']);
            } else if (phaseId === 3) {
                const s4 = spot.verification_status || 'MEDIA_READY';
                data.push(['\u25b6 CURRENT STATUS', s4, s4 === 'MEDIA_READY' ? 'success' : 'warning']);
            } else if (phaseId === 4) {
                const s5 = spot.verification_status || 'PUBLISHED';
                data.push(['\u25b6 CURRENT STATUS', s5, s5 === 'PUBLISHED' ? 'success' : 'warning']);
            }

            // DYNAMIC FIELDS — skip bloat fields, compact all values to single lines
            const phaseFields = fields.filter(f => f.phase_id === phaseId && !CARD_SKIP_FIELDS.has(f.field_name));
            phaseFields.forEach(f => {
                let displayVal: React.ReactNode = 'NULL';
                let status = 'missing';

                const rawVal = spot[f.field_name];
                const clVal = spot.candidate_links?.[f.field_name];

                if (f.data_type === 'boolean') {
                    displayVal = bool(rawVal);
                    status = boolOk(rawVal);
                } else if (f.data_type === 'number') {
                    displayVal = val(rawVal);
                    status = ok(rawVal);
                } else if (f.data_type === 'date') {
                    displayVal = rawVal ? new Date(rawVal).toLocaleDateString() : 'NEVER';
                    status = rawVal ? 'val' : 'missing';
                } else if (f.data_type === 'json') {
                    // Compact: show key count + first key preview, not a full pre block
                    if (rawVal && typeof rawVal === 'object') {
                        const keys = Object.keys(rawVal).filter(k => rawVal[k] != null && rawVal[k] !== '');
                        displayVal = keys.length > 0
                            ? `${keys.length} fields · ${keys.slice(0, 2).join(', ')}${keys.length > 2 ? '…' : ''}`
                            : 'EMPTY';
                        status = keys.length > 0 ? 'success' : 'missing';
                    } else if (typeof rawVal === 'string' && rawVal.startsWith('{')) {
                        try {
                            const parsed = JSON.parse(rawVal);
                            const keys = Object.keys(parsed).filter(k => parsed[k] != null && parsed[k] !== '');
                            displayVal = keys.length > 0 ? `${keys.length} fields · ${keys.slice(0,2).join(', ')}` : 'EMPTY';
                            status = keys.length > 0 ? 'success' : 'missing';
                        } catch { displayVal = 'PARSE ERR'; status = 'missing'; }
                    } else {
                        displayVal = rawVal ? String(rawVal).slice(0, 60) : 'NULL';
                        status = rawVal ? 'success' : 'missing';
                    }
                } else if (f.data_type === 'url_check') {
                    const target = clVal || rawVal;
                    displayVal = target ? <a href={target} target="_blank" rel="noreferrer" style={{color: 'inherit', textDecoration: 'underline'}}>{String(target).replace(/^https?:\/\//, '').slice(0, 40)}</a> : 'NULL';
                    status = ok(target);
                } else if (f.data_type === 'json_array') {
                    // Compact: show count + preview of first item only
                    const arr = Array.isArray(rawVal) ? rawVal
                        : rawVal && typeof rawVal === 'object' ? Object.values(rawVal)
                        : typeof rawVal === 'string' ? (() => { try { const p = JSON.parse(rawVal); return Array.isArray(p) ? p : []; } catch { return []; } })()
                        : [];
                    if (arr.length > 0) {
                        const first = String(arr[0]).slice(0, 50);
                        displayVal = arr.length === 1 ? first : `${arr.length} items · ${first}${first.length === 50 ? '…' : ''}`;
                        status = 'success';
                    } else {
                        displayVal = 'NULL'; status = 'missing';
                    }
                } else {
                    // String: truncate to 60 chars
                    const target = clVal || rawVal;
                    if (target && typeof target === 'string' && target.startsWith('http')) {
                        displayVal = <a href={target} target="_blank" rel="noreferrer" style={{color: 'inherit', textDecoration: 'underline'}}>{target.replace(/^https?:\/\//, '').slice(0, 40)}</a>;
                    } else {
                        displayVal = target ? String(target).slice(0, 80) : 'NULL';
                    }
                    status = ok(target);
                }
                data.push([f.display_label, displayVal, status]);
            });

            return {
                title: spot.name,
                status: spot.status || spot.verification_status || 'PROCESSED',
                type: 'success' as const,
                spotId: spot.id,
                spotName: spot.name,
                data
            };
        });
    };


    const baseBelts = [
        generateUniformBelt(1, 1, 'Phase 1 │ Scout (Seed Engine)  IN: null → OUT: SEEDED', '--neon-scout', '0, 255, 170', 'Daemon_v2', 'PROCESSING...', 'Waiting', 'PENDING', 'SEEDED', getQueueNames('phase1')),
        generateUniformBelt(2, 2, 'Phase 2 │ Detective (AI Crawl)  IN: SEEDED → OUT: DEEP_CRAWLED', '--neon-detective', '255, 106, 0', 'Llama3.2-8b', 'PROCESSING...', 'Waiting', 'SEEDED', 'DEEP_CRAWLED', getQueueNames('phase2')),
        generateUniformBelt(3, 3, 'Phase 3 │ Photographer  IN: DEEP_CRAWLED → OUT: MEDIA_READY', '--neon-photo', '255, 0, 127', 'Vision_v1', 'PROCESSING...', 'Waiting', 'DEEP_CRAWLED', 'MEDIA_READY', getQueueNames('phase3')),
        generateUniformBelt(4, 4, 'Phase 4 │ Publisher  IN: MEDIA_READY → OUT: PUBLISHED', '--neon-publish', '0, 212, 255', 'Sync_v4', 'PROCESSING...', 'Waiting', 'MEDIA_READY', 'PUBLISHED', getQueueNames('phase4'))
    ];

    // Dynamically pull attempting checkmarks from the field_registry
    baseBelts[0].attempting = fields.filter(f => f.phase_id === 1).map(f => [f.field_name, 'pending', f.importance_level === 2 ? '🛑' : f.importance_level === 1 ? '⭐' : '⚪'] as [string, string, string]);
    
    // Phase 2 Detective: Registry fields + dynamic AI vectors
    const aiVectors = config?.ai_target_vectors || [];
    baseBelts[1].attempting = [
      ...fields.filter(f => f.phase_id === 2).map(f => [f.field_name, 'pending', f.importance_level === 2 ? '🛑' : f.importance_level === 1 ? '⭐' : '⚪'] as [string, string, string]),
      ...aiVectors.map((v: any) => [(v.key || v), 'pending', '⚪'] as [string, string, string])
    ];

    // Phase 3 Photographer: Registry fields + photo categories
    const photoCategories = config?.photo_categories || [];
    baseBelts[2].attempting = [
      ...fields.filter(f => f.phase_id === 3).map(f => [f.field_name, 'pending', f.importance_level === 2 ? '🛑' : f.importance_level === 1 ? '⭐' : '⚪'] as [string, string, string]),
      ...photoCategories.map((c: string) => [c, 'pending', '⚪'] as [string, string, string])
    ];

    // Phase 4 Publisher: Registry fields
    baseBelts[3].attempting = fields.filter(f => f.phase_id === 4).map(f => [f.field_name, 'pending', f.importance_level === 2 ? '🛑' : f.importance_level === 1 ? '⭐' : '⚪'] as [string, string, string]);

    const mergedBelts = baseBelts.map(belt => {
        let liveData: any = null;
        if (belt.id === 1) liveData = telemetry.scout;

        if (belt.id === 2) liveData = telemetry.detective;
        if (belt.id === 3) liveData = telemetry.photographer;
        if (belt.id === 4) liveData = telemetry.publisher;

        // Populate fields from real DB spots
        const specificSpots = getSpotsForPhase(belt.id, 2);
        const dynamicCards = buildPhaseCards(belt.id, specificSpots);

        // Per-belt count badges from live telemetry + pipelineStats
        const inQCount = liveData?.in_q?.length ?? 0;
        const countBadges: {label: string; value: string}[] = [];
        if (belt.id === 1) {
            countBadges.push({ label: 'OUT', value: `${(pipelineStats?.summary?.seeded ?? 0).toLocaleString()} SEEDED` });
        } else if (belt.id === 2) {
            countBadges.push({ label: 'IN', value: `${(pipelineStats?.summary?.enriched ?? 0)} ENRICHED` });
            countBadges.push({ label: 'OUT', value: `${(pipelineStats?.summary?.deep_crawled_count ?? 0)} DEEP_CRAWLED` });
        } else if (belt.id === 3) {
            countBadges.push({ label: 'IN', value: `${(pipelineStats?.summary?.deep_crawled_count ?? 0)} DEEP_CRAWLED` });
            countBadges.push({ label: 'OUT', value: `${(pipelineStats?.summary?.media_ready ?? 0)} MEDIA_READY` });
        } else if (belt.id === 4) {
            countBadges.push({ label: 'IN', value: `${(pipelineStats?.summary?.media_ready ?? 0)} MEDIA_READY` });
            countBadges.push({ label: 'PUB', value: `${(pipelineStats?.summary?.published ?? 0)} PUBLISHED` });
        }

        if (liveData) {
            const activeRecord = liveData.active_record;
            let dynamicAttempting = belt.attempting;
            
            if (activeRecord) {
                dynamicAttempting = belt.attempting.map(([fieldName, oldStatus, oldIcon], i) => {
                    const rawVal = activeRecord[fieldName];
                    let clVal = null;
                    try {
                        clVal = typeof activeRecord.candidate_links === 'string' 
                            ? JSON.parse(activeRecord.candidate_links)?.[fieldName] 
                            : activeRecord.candidate_links?.[fieldName];
                    } catch(e) {}
                    
                    const target = clVal || rawVal;
                    let newStatus = 'missing';
                    if (target != null && target !== '' && target !== false && target !== '[]' && target !== '{}') {
                        newStatus = 'success';
                    }
                    return [fieldName, newStatus, oldIcon] as [string, string, string?];
                });
            }

            return {
                ...belt,
                attempting: dynamicAttempting,
                job: liveData.active_job || 'IDLE',
                target: liveData.target || (liveData.in_q?.[0] ? `Next: ${liveData.in_q[0]}` : 'WAITING...'),
                status: liveData.active_job ? 'PROCESSING' : (liveData.alive ? 'WAITING' : 'OFFLINE'),
                inQ: liveData.in_q && liveData.in_q.length > 0 ? liveData.in_q.slice(0, 3) : belt.inQ,
                outCards: dynamicCards.length > 0 ? dynamicCards : belt.outCards,
                countBadges,
            };
        }
        return {
            ...belt,
            outCards: dynamicCards.length > 0 ? dynamicCards : belt.outCards,
            countBadges,
        };
    });

    // Per-belt daemon control mappings
    const beltDaemon: Record<number, { active: boolean; hasDaemon: boolean; onStart: () => void; onStop: () => void }> = {
        1: { hasDaemon: true,  active: !!(status?.isHarvestingActive || status?.isGoogleSweepActive), onStart: () => triggerHarvest?.('start-all'), onStop: () => triggerHarvest?.('stop-all') },
        2: { hasDaemon: true,  active: !!(status?.currentTarget?.includes('Indexer: online')),     onStart: () => triggerSpecificDaemon?.('indexer', 'start'),     onStop: () => triggerSpecificDaemon?.('indexer', 'stop') },
        3: { hasDaemon: true,  active: !!(status?.currentTarget?.includes('Photographer: online')), onStart: () => triggerSpecificDaemon?.('photographer', 'start'), onStop: () => triggerSpecificDaemon?.('photographer', 'stop') },
        4: { hasDaemon: false, active: false, onStart: () => {}, onStop: () => {} },
        5: { hasDaemon: false, active: false, onStart: () => {}, onStop: () => {} },
    };


    const [controlsOpen, setControlsOpen] = React.useState(false);

    // ☠ Block & Purge — called from any belt card button
    const handleBlockSpot = async (spotId: string, spotName: string) => {
        if (!window.confirm(`☠ Block & purge "${spotName}" from the entire pipeline?\n\nThis will:\n• Delete all matching records from the database\n• Prevent future re-ingestion\n\nThis cannot be undone.`)) return;
        try {
            const res = await fetch(`${CCTOWER}/api/scraper/blocklist`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ keyword: spotName })
            });
            const data = await res.json();
            if (data.success) {
                alert(`✅ Blocked "${spotName}" — purged ${data.count ?? 0} matching record(s) from the database.`);
                pulse();
                onBlockSpot?.(); // trigger parent refresh if provided
            } else {
                alert('Block failed: ' + JSON.stringify(data));
            }
        } catch (e: any) {
            alert('CCTower unreachable: ' + e.message);
        }
    };

    const handleRestartSpot = async (spotId: string) => {
        if (!window.confirm(`Force restart this spot to SEEDED?`)) return;
        try {
            const res = await fetch(`${CCTOWER}/api/skate_spots/${spotId}/restart`, { method: 'POST' });
            if ((await res.json()).success) {
                pulse();
                onBlockSpot?.(); // trigger refresh
            }
        } catch (e) { console.error(e); }
    };

    const handleFreezeSpot = async (spotId: string) => {
        if (!window.confirm(`Freeze this spot to ON_HOLD?`)) return;
        try {
            const res = await fetch(`${CCTOWER}/api/skate_spots/${spotId}/freeze`, { method: 'POST' });
            if ((await res.json()).success) {
                pulse();
                onBlockSpot?.(); // trigger refresh
            }
        } catch (e) { console.error(e); }
    };

    return (
        <div className="pipeline-dashboard-container w-full min-h-screen flex flex-col text-white relative" style={{ gap: 0 }}>
            <style>{STYLES}</style>

            {/* ── SLIM MISSION CONTROL HUD ── */}
            <div style={{
                position: 'sticky', top: 0, zIndex: 100,
                background: 'rgba(8,8,14,0.97)',
                borderBottom: '1px solid rgba(0,255,170,0.12)',
                backdropFilter: 'blur(20px)',
                boxShadow: '0 4px 30px rgba(0,0,0,0.6)',
            }}>
                {/* Top row: branding + stats + controls toggle */}
                <div style={{ display: 'flex', alignItems: 'center', gap: 0, padding: '10px 20px', minHeight: 52 }}>
                    <div style={{ display: 'flex', flexDirection: 'column', lineHeight: 1.1, flexShrink: 0, marginRight: 18 }}>
                        <span style={{ fontSize: '1.05rem', fontWeight: 900, background: 'linear-gradient(90deg,#00ffaa,#00d4ff)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent', letterSpacing: '0.05em' }}>SK8LYTZ</span>
                        <span style={{ fontSize: '0.48rem', fontWeight: 800, color: 'rgba(0,255,170,0.45)', textTransform: 'uppercase', letterSpacing: '0.2em' }}>FACTORY FLOOR</span>
                    </div>
                    <div style={{ width: 1, height: 28, background: 'rgba(255,255,255,0.07)', flexShrink: 0, marginRight: 18 }} />
                    <div style={{ display: 'flex', alignItems: 'center', gap: 6, flexShrink: 0 }}>
                        {[
                            { label: 'Seeds',      value: pipelineStats?.summary?.seeded?.toLocaleString() ?? '\u2014', color: '#00ffaa' },
                            { label: 'Published',  value: pipelineStats?.summary?.published?.toLocaleString()    ?? '\u2014', color: '#4ade80' },
                            { label: 'Throughput', value: `${pipelineStats?.summary?.throughput ?? 0}/m`,                    color: '#fff' },
                            { label: 'DB Sync',    value: loading ? 'SYNCING' : 'LIVE',                                      color: loading ? '#ffb300' : '#00ffaa' },
                        ].map(s => (
                            <div key={s.label} style={{ display: 'flex', alignItems: 'center', gap: 6, padding: '3px 10px', borderRadius: 20, background: 'rgba(255,255,255,0.04)', border: '1px solid rgba(255,255,255,0.07)', flexShrink: 0 }}>
                                <span style={{ fontSize: '0.5rem', color: 'rgba(255,255,255,0.35)', textTransform: 'uppercase', fontWeight: 800, letterSpacing: '0.1em', fontFamily: 'JetBrains Mono, monospace' }}>{s.label}</span>
                                <span style={{ fontSize: '0.68rem', fontWeight: 900, color: s.color, fontFamily: 'JetBrains Mono, monospace' }}>{s.value}</span>
                            </div>
                        ))}
                    </div>
                    <div style={{ flex: 1 }} />
                    <button
                        onClick={() => setControlsOpen(o => !o)}
                        style={{
                            display: 'flex', alignItems: 'center', gap: 6,
                            padding: '5px 14px', borderRadius: 20,
                            border: `1px solid ${controlsOpen ? 'rgba(0,255,170,0.4)' : 'rgba(255,255,255,0.1)'}`,
                            background: controlsOpen ? 'rgba(0,255,170,0.08)' : 'rgba(255,255,255,0.04)',
                            color: controlsOpen ? '#00ffaa' : 'rgba(255,255,255,0.5)',
                            cursor: 'pointer', fontSize: '0.6rem', fontWeight: 800,
                            textTransform: 'uppercase', letterSpacing: '0.1em',
                            transition: 'all 0.2s', flexShrink: 0, marginLeft: 12,
                        }}
                    >
                        <span>⚙</span> Global Controls <span style={{ fontSize: '0.65rem', opacity: 0.6 }}>{controlsOpen ? '▲' : '▼'}</span>
                    </button>
                </div>

                {/* Collapsible Global Controls drawer */}
                {controlsOpen && (
                    <div style={{
                        borderTop: '1px solid rgba(255,255,255,0.06)',
                        background: 'rgba(4,4,10,0.98)',
                        padding: '10px 20px',
                        display: 'flex', alignItems: 'center', flexWrap: 'wrap', gap: '8px 14px',
                    }}>
                        <span style={{ fontSize: '0.5rem', fontWeight: 900, color: 'rgba(0,255,170,0.5)', textTransform: 'uppercase', letterSpacing: '0.2em', fontFamily: 'JetBrains Mono, monospace', flexShrink: 0 }}>
                            ⚡ MISSION CONTROLS
                        </span>
                        <div style={{ width: 1, height: 20, background: 'rgba(255,255,255,0.07)', flexShrink: 0 }} />
                        {headerControls}
                    </div>
                )}
            </div>

            {/* Region Pulse — below sticky HUD */}
            {belowHeader}

            {/* ── BELT ROWS ── */}
            <div style={{ display: 'flex', flexDirection: 'column', gap: 10, padding: '14px 16px' }}>
                {mergedBelts.map(b => {
                    const dc = beltDaemon[b.id];
                    // Build live status string from belt telemetry
                    const isActive = dc?.active;
                    const statusLabel = b.status === 'PROCESSING' ? 'PROCESSING' : 'IDLE';
                    const daemonStatus = b.target && b.target !== 'WAITING...'
                        ? `${statusLabel}: ${b.target}`
                        : b.job && b.job !== 'IDLE'
                            ? b.job
                            : isActive ? 'RUNNING \u2014 WAITING FOR QUEUE' : 'IDLE \u2014 AWAITING JOB';
                    return (
                        <BeltNode
                            key={b.id}
                            {...b}
                            onPhaseNav={onPhaseNav ? () => onPhaseNav(BELT_TAB[b.id] ?? 'phase1') : undefined}
                            daemonActive={dc?.active}
                            hasDaemon={dc?.hasDaemon ?? true}
                            onDaemonStart={dc?.onStart}
                            onDaemonStop={dc?.onStop}
                            daemonStatus={daemonStatus}
                            onBlockSpot={handleBlockSpot}
                            onRestartSpot={handleRestartSpot}
                            onFreezeSpot={handleFreezeSpot}
                        />
                    );
                })}
            </div>
        </div>
    );
}

export default ScraperPipeline;
