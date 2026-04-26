import React from 'react';
import BeltNode from './BeltNode';
import { useScraperTelemetry } from '../hooks/useScraperTelemetry';

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
  gatekeeper: [],
  attempting: [] as [string, string][],
  outCards: []
});

// Belt id → app tab mapping
const BELT_TAB: Record<number, string> = { 1: 'phase1', 2: 'phase2', 3: 'phase3', 4: 'phase4', 5: 'phase5' };

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
}> = ({ headerControls, belowHeader, pipelineStats, phaseQueues, onPhaseNav, status, triggerSpecificDaemon, triggerHarvest }) => {
    const { telemetry, config, loading } = useScraperTelemetry(2000);

    const getSpotsForPhase = (beltId: number, count: number = 2) => {
        let spots = [];
        if (beltId === 1) spots = phaseQueues?.phase1 || [];
        else if (beltId === 2) spots = phaseQueues?.phase3 || []; // Crawl is phase 3 in API
        else if (beltId === 3) spots = phaseQueues?.phase4 || []; // Detective is phase 4
        else if (beltId === 4) spots = phaseQueues?.phase6 || []; // Photographer is phase 6
        else if (beltId === 5) spots = phaseQueues?.recent || [];
        return spots.slice(0, count);
    };

    const getQueueNames = (phase: string, count: number = 3) => (phaseQueues?.[phase] || []).slice(0, count).map((s: any) => s.name || s.url || s.target);

    const buildPhaseCards = (phaseId: number, spots: any[]) => {
        return spots.map(spot => {
            const data: [string, string, string][] = [];
            const bool = (v: any) => (v === true ? 'YES' : v === false ? 'NO' : 'N/A');
            const val  = (v: any, fallback = 'NULL') => v != null ? String(v) : fallback;
            const ok   = (v: any): string => (v != null && v !== '' && v !== false) ? 'success' : 'missing';
            const boolOk = (v: any): string => (v === true ? 'success' : 'missing');

            if (phaseId === 1) { // ── SCOUT: Google Places seed ──
                data.push(['name',             val(spot.name),                                                        ok(spot.name)]);
                data.push(['facility_type',    val(spot.facility_type, 'UNKNOWN'),                                    ok(spot.facility_type)]);
                data.push(['address',          val(spot.street_address || spot.address),                              ok(spot.street_address || spot.address)]);
                data.push(['city',             val(spot.city),                                                        ok(spot.city)]);
                data.push(['state',            val(spot.state),                                                       ok(spot.state)]);
                data.push(['zip',              val(spot.zip),                                                         ok(spot.zip)]);
                data.push(['lat',              spot.lat != null ? Number(spot.lat).toFixed(5) : 'NULL',               ok(spot.lat)]);
                data.push(['lng',              spot.lng != null ? Number(spot.lng).toFixed(5) : 'NULL',               ok(spot.lng)]);
                data.push(['phone',            val(spot.phone || spot.phone_number),                                  ok(spot.phone || spot.phone_number)]);
                data.push(['website',          val(spot.website),                                                     ok(spot.website)]);
                data.push(['rating',           spot.rating != null ? `★ ${spot.rating}` : 'N/A',                     ok(spot.rating)]);
                data.push(['reviews',          val(spot.user_ratings_total, '0'),                                     ok(spot.user_ratings_total)]);
                data.push(['google_place_id',  spot.google_place_id ? spot.google_place_id.slice(0, 12) + '…' : 'NULL', ok(spot.google_place_id)]);
                data.push(['source',           val(spot.source, 'GOOGLE'),                                            'val']);
                data.push(['is_indoor',        bool(spot.is_indoor),                                                  'val']);
                data.push(['verification_status', val(spot.verification_status, 'PENDING'),                           ok(spot.verification_status)]);
                data.push(['created_at',       spot.created_at ? new Date(spot.created_at).toLocaleDateString() : 'NOW', 'val']);

            } else if (phaseId === 2) { // ── CRAWL: Website deep crawl ──
                data.push(['website',          val(spot.website),                                                     ok(spot.website)]);
                data.push(['is_deep_crawled',  bool(spot.is_deep_crawled),                                            boolOk(spot.is_deep_crawled)]);
                data.push(['operator_name',    val(spot.operator_name),                                               ok(spot.operator_name)]);
                data.push(['operator_desc',    spot.operator_description ? `${String(spot.operator_description).slice(0, 30)}…` : 'NULL', ok(spot.operator_description)]);
                data.push(['opening_hours',    spot.opening_hours ? 'PARSED' : 'NULL',                                boolOk(spot.opening_hours)]);
                data.push(['schedule_url',     spot.schedule_url ? 'FOUND' : 'NULL',                                  boolOk(spot.schedule_url)]);
                data.push(['has_fee',          bool(spot.has_fee),                                                    'val']);
                data.push(['pricing_data',     spot.pricing_data ? 'EXTRACTED' : 'NULL',                              boolOk(spot.pricing_data)]);
                data.push(['facebook_url',     spot.facebook_url ? 'FOUND' : 'NULL',                                  boolOk(spot.facebook_url)]);
                data.push(['instagram_url',    spot.instagram_url ? 'FOUND' : 'NULL',                                 boolOk(spot.instagram_url)]);
                data.push(['tiktok_url',       spot.tiktok_url ? 'FOUND' : 'NULL',                                    boolOk(spot.tiktok_url)]);
                data.push(['socials',          spot.socials ? `${Object.keys(spot.socials).length} platforms` : '0', boolOk(spot.socials)]);
                data.push(['special_events',   spot.special_events ? 'DETECTED' : 'NULL',                             boolOk(spot.special_events)]);
                data.push(['raw_kp',           spot.raw_knowledge_panel ? 'CAPTURED' : 'NULL',                        boolOk(spot.raw_knowledge_panel)]);
                data.push(['photo_candidates', spot.candidate_photos ? `${Array.isArray(spot.candidate_photos) ? spot.candidate_photos.length : '?'} imgs` : '0', boolOk(spot.candidate_photos)]);
                data.push(['last_attempted',   spot.last_attempted_at ? new Date(spot.last_attempted_at).toLocaleDateString() : 'NEVER', 'val']);
                data.push(['retry_count',      val(spot.retry_count, '0'),                                            spot.retry_count > 2 ? 'missing' : 'val']);

            } else if (phaseId === 3) { // ── DETECTIVE: Llama-3.2 AI extraction ──
                data.push(['surface_type',     val(spot.surface_type, 'UNKNOWN'),                                     ok(spot.surface_type)]);
                data.push(['surface_quality',  val(spot.surface_quality, 'N/A'),                                      ok(spot.surface_quality)]);
                data.push(['is_indoor',        bool(spot.is_indoor),                                                  'val']);
                data.push(['capacity',         val(spot.capacity, 'N/A'),                                             ok(spot.capacity)]);
                data.push(['has_rental',       bool(spot.has_rental),                                                 boolOk(spot.has_rental)]);
                data.push(['has_pro_shop',     bool(spot.has_pro_shop || spot.has_proshop),                           boolOk(spot.has_pro_shop || spot.has_proshop)]);
                data.push(['has_food',         bool(spot.has_food),                                                   boolOk(spot.has_food)]);
                data.push(['has_lights',       bool(spot.has_lights),                                                 boolOk(spot.has_lights)]);
                data.push(['has_lockers',      bool(spot.has_lockers),                                                boolOk(spot.has_lockers)]);
                data.push(['has_ac',           bool(spot.has_ac),                                                     boolOk(spot.has_ac)]);
                data.push(['has_wifi',         bool(spot.has_wifi),                                                   boolOk(spot.has_wifi)]);
                data.push(['has_toilets',      bool(spot.has_toilets),                                                boolOk(spot.has_toilets)]);
                data.push(['wheelchair',       bool(spot.is_wheelchair_accessible),                                   boolOk(spot.is_wheelchair_accessible)]);
                data.push(['has_adult_night',  bool(spot.has_adult_night),                                            boolOk(spot.has_adult_night)]);
                data.push(['adult_night_det',  spot.adult_night_details ? `${String(spot.adult_night_details).slice(0, 20)}…` : 'N/A', ok(spot.adult_night_details)]);
                data.push(['hosts_derby',      bool(spot.hosts_derby),                                                boolOk(spot.hosts_derby)]);
                data.push(['vibe_score',       spot.vibe_score != null ? `${spot.vibe_score}/100` : 'N/A',            ok(spot.vibe_score)]);
                data.push(['vibe_rating',      spot.vibe_rating != null ? `${spot.vibe_rating}★` : 'N/A',            ok(spot.vibe_rating)]);
                data.push(['cultural_meta',    spot.cultural_metadata ? 'ENRICHED' : 'NULL',                          boolOk(spot.cultural_metadata)]);
                data.push(['adult_schedule',   spot.adult_night_schedule ? 'PARSED' : 'NULL',                         boolOk(spot.adult_night_schedule)]);

            } else if (phaseId === 4) { // ── PHOTOGRAPHER: Image harvest ──
                const photoCount = Array.isArray(spot.photos) ? spot.photos.length : spot.photos ? '?' : 0;
                const candCount  = Array.isArray(spot.candidate_photos) ? spot.candidate_photos.length : 0;
                data.push(['photos',           `${photoCount} imgs`,                                                  Number(photoCount) > 0 ? 'success' : 'missing']);
                data.push(['candidate_photos', `${candCount} queued`,                                                  candCount > 0 ? 'success' : 'missing']);
                data.push(['last_enriched_at', spot.last_enriched_at ? new Date(spot.last_enriched_at).toLocaleDateString() : 'NEVER', ok(spot.last_enriched_at)]);
                data.push(['last_attempted',   spot.last_attempted_at ? new Date(spot.last_attempted_at).toLocaleTimeString() : 'NEVER', 'val']);
                data.push(['retry_count',      val(spot.retry_count, '0'),                                            spot.retry_count > 2 ? 'missing' : 'val']);
                data.push(['facebook_url',     spot.facebook_url ? 'SCRAPED' : 'PENDING',                             boolOk(spot.facebook_url)]);
                data.push(['instagram_url',    spot.instagram_url ? 'SCRAPED' : 'PENDING',                            boolOk(spot.instagram_url)]);
                data.push(['website',          spot.website ? 'SCRAPED' : 'N/A',                                      boolOk(spot.website)]);
                data.push(['name',             val(spot.name),                                                        ok(spot.name)]);
                data.push(['state',            val(spot.state),                                                       ok(spot.state)]);
                data.push(['is_deep_crawled',  bool(spot.is_deep_crawled),                                            boolOk(spot.is_deep_crawled)]);

            } else if (phaseId === 5) { // ── PUBLISHER: QA gate + DB sync ──
                data.push(['id',               spot.id ? spot.id.slice(0, 12) + '…' : 'N/A',                         ok(spot.id)]);
                data.push(['name',             val(spot.name),                                                        ok(spot.name)]);
                data.push(['is_published',     bool(spot.is_published),                                               boolOk(spot.is_published)]);
                data.push(['is_verified',      bool(spot.is_verified),                                                boolOk(spot.is_verified)]);
                data.push(['is_featured',      bool(spot.is_featured),                                                boolOk(spot.is_featured)]);
                data.push(['verification_status', val(spot.verification_status, 'PENDING'),                           ok(spot.verification_status)]);
                data.push(['state',            val(spot.state),                                                       ok(spot.state)]);
                data.push(['facility_type',    val(spot.facility_type, 'UNKNOWN'),                                    ok(spot.facility_type)]);
                data.push(['surface_type',     val(spot.surface_type, 'N/A'),                                         ok(spot.surface_type)]);
                data.push(['photos',           `${Array.isArray(spot.photos) ? spot.photos.length : 0} imgs`,         Array.isArray(spot.photos) && spot.photos.length > 0 ? 'success' : 'missing']);
                data.push(['has_rental',       bool(spot.has_rental),                                                 boolOk(spot.has_rental)]);
                data.push(['has_pro_shop',     bool(spot.has_pro_shop || spot.has_proshop),                           boolOk(spot.has_pro_shop || spot.has_proshop)]);
                data.push(['vibe_score',       spot.vibe_score != null ? `${spot.vibe_score}/100` : 'N/A',            ok(spot.vibe_score)]);
                data.push(['updated_at',       spot.updated_at ? new Date(spot.updated_at).toLocaleDateString() : 'N/A', ok(spot.updated_at)]);
                data.push(['updated_by',       val(spot.updated_by, 'AUTO'),                                          'val']);
                data.push(['source',           val(spot.source, 'GOOGLE'),                                            'val']);
            }

            return {
                title: spot.name,
                status: spot.status || spot.verification_status || 'PROCESSED',
                type: 'success' as const,
                data
            };
        });
    };


    const baseBelts = [
        generateUniformBelt(1, 1, 'Phase 1: The Scout (Google Sweep)', '--neon-scout', '0, 255, 170', 'Daemon_v2', 'PROCESSING...', 'Waiting', 'PENDING', 'SEEDED', getQueueNames('phase1')),
        generateUniformBelt(2, 2, 'Phase 2: The Spider (Operator)', '--neon-crawl', '157, 78, 221', 'Spider_v3', 'PROCESSING...', 'Waiting', 'SEEDED', 'ENRICHED', getQueueNames('phase3')),
        generateUniformBelt(3, 3, 'Phase 3: The Detective (Indexer)', '--neon-detective', '255, 106, 0', 'Llama3.2-8b', 'PROCESSING...', 'Waiting', 'ENRICHED', 'DEEP_CRAWLED', getQueueNames('phase4')),
        generateUniformBelt(4, 4, 'Phase 4: The Photographer', '--neon-photo', '255, 0, 127', 'Vision_v1', 'PROCESSING...', 'Waiting', 'DEEP_CRAWLED', 'MEDIA_READY', getQueueNames('phase6')),
        generateUniformBelt(5, 5, 'Phase 5: The Publisher', '--neon-publish', '0, 212, 255', 'Sync_v4', 'PROCESSING...', 'Waiting', 'MEDIA_READY', 'PUBLISHED', getQueueNames('recent'))
    ];

    // Restore the technical target collection checklists on the Active Job cards with EVERY field
    baseBelts[0].attempting = [['name', 'pending'], ['facility_type', 'pending'], ['address', 'pending'], ['city', 'pending'], ['state', 'pending'], ['zip', 'pending'], ['lat', 'pending'], ['lng', 'pending'], ['phone', 'pending'], ['website', 'pending'], ['rating', 'pending'], ['reviews', 'pending'], ['place_id', 'pending'], ['is_indoor', 'pending']];
    
    // Add custom paths to Crawl:
    const crawlPaths = config?.crawl_priority_paths || [];
    baseBelts[1].attempting = [
      ['website', 'pending'], ['operator_name', 'pending'], ['operator_desc', 'pending'], ['opening_hours', 'pending'], ['schedule_url', 'pending'], ['pricing_data', 'pending'], ['has_fee', 'pending'], ['facebook_url', 'pending'], ['instagram_url', 'pending'], ['tiktok_url', 'pending'], ['special_events', 'pending'], ['raw_kp', 'pending'],
      ...crawlPaths.map((p: string) => [p, 'pending'] as [string, string])
    ];

    // Add AI target vectors to Detective:
    const aiVectors = config?.ai_target_vectors || [];
    baseBelts[2].attempting = [
      ['surface_type', 'pending'], ['surface_quality', 'pending'], ['is_indoor', 'pending'], ['capacity', 'pending'], ['has_rental', 'pending'], ['has_pro_shop', 'pending'], ['has_food', 'pending'], ['has_lights', 'pending'], ['has_lockers', 'pending'], ['has_ac', 'pending'], ['has_wifi', 'pending'], ['has_toilets', 'pending'], ['wheelchair', 'pending'], ['adult_night', 'pending'], ['derby', 'pending'], ['vibe_score', 'pending'], ['cultural_meta', 'pending'],
      ...aiVectors.map((v: any) => [(v.key || v), 'pending'] as [string, string])
    ];

    // Add Photo categories to Photographer:
    const photoCategories = config?.photo_categories || [];
    baseBelts[3].attempting = [
      ['photos', 'pending'], ['candidate_photos', 'pending'], ['facebook_url', 'pending'], ['instagram_url', 'pending'], ['website', 'pending'], ['is_deep_crawled', 'pending'],
      ...photoCategories.map((c: string) => [c, 'pending'] as [string, string])
    ];

    // Add Publisher required fields:
    const pubFields = config?.publisher_required_fields || [];
    baseBelts[4].attempting = [
      ['id', 'pending'], ['is_published', 'pending'], ['is_verified', 'pending'], ['is_featured', 'pending'], ['verification_status', 'pending'], ['surface_type', 'pending'], ['has_rental', 'pending'], ['has_pro_shop', 'pending'], ['vibe_score', 'pending'], ['updated_at', 'pending'], ['source', 'pending'],
      ...pubFields.map((f: string) => [f, 'pending'] as [string, string])
    ];

    const mergedBelts = baseBelts.map(belt => {
        let liveData: any = null;
        if (belt.id === 1) liveData = telemetry.scout;
        if (belt.id === 2) liveData = telemetry.spider;
        if (belt.id === 3) liveData = telemetry.detective;
        if (belt.id === 4) liveData = telemetry.photographer;
        if (belt.id === 5) liveData = telemetry.publisher;

        // Populate fields from real DB spots
        const specificSpots = getSpotsForPhase(belt.id, 2);
        const dynamicCards = buildPhaseCards(belt.id, specificSpots);

        if (liveData) {
            return {
                ...belt,
                job: liveData.active_job ? 'PROCESSING LIVE...' : 'IDLE',
                target: liveData.target || 'WAITING...',
                status: liveData.active_job ? 'PROCESSING' : 'WAITING',
                inQ: liveData.in_q && liveData.in_q.length > 0 ? liveData.in_q.slice(0, 3) : belt.inQ,
                outCards: dynamicCards.length > 0 ? dynamicCards : belt.outCards
            };
        }
        return {
            ...belt,
            outCards: dynamicCards.length > 0 ? dynamicCards : belt.outCards
        };
    });

    // Per-belt daemon control mappings
    const beltDaemon: Record<number, { active: boolean; hasDaemon: boolean; onStart: () => void; onStop: () => void }> = {
        1: { hasDaemon: true,  active: !!(status?.isHarvestingActive || status?.isGoogleSweepActive), onStart: () => triggerHarvest?.('start-all'), onStop: () => triggerHarvest?.('stop-all') },
        2: { hasDaemon: true,  active: !!(status?.currentTarget?.includes('Operator: online')),    onStart: () => triggerSpecificDaemon?.('operator', 'start'),    onStop: () => triggerSpecificDaemon?.('operator', 'stop') },
        3: { hasDaemon: true,  active: !!(status?.currentTarget?.includes('Indexer: online')),     onStart: () => triggerSpecificDaemon?.('indexer', 'start'),     onStop: () => triggerSpecificDaemon?.('indexer', 'stop') },
        4: { hasDaemon: true,  active: !!(status?.currentTarget?.includes('Photographer: online')), onStart: () => triggerSpecificDaemon?.('photographer', 'start'), onStop: () => triggerSpecificDaemon?.('photographer', 'stop') },
        5: { hasDaemon: false, active: false, onStart: () => {}, onStop: () => {} },
    };


    const [controlsOpen, setControlsOpen] = React.useState(false);

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
                            { label: 'Seeds',      value: pipelineStats?.summary?.total_seeded?.toLocaleString() ?? '\u2014', color: '#00ffaa' },
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
                        />
                    );
                })}
            </div>
        </div>
    );
}

export default ScraperPipeline;
