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
\`;

// Uniform data generation to ensure every belt has exactly 2 pending, 2 success, 1 rejected
const generateUniformBelt = (idx: number, id: number, name: string, color: string, rgb: string, daemon: string, job: string, target: string, status: string, states: string[]) => ({
  id, name, color, rgb, activeStates: states, job, daemon, target, status,
  inQ: [\`Query: Batch \${idx}A\`, \`Query: Batch \${idx}B\`],
  gatekeeper: ['MUST BE ESTABLISHMENT', 'NOT IN BLOCKLIST'],
  attempting: [
      ['name', 'done'], ['address', 'done'], ['phone', 'done'], ['website', 'fail'], ['rating', 'done']
  ] as [string, string][],
  outCards: [
      {
          title: \`Target \${idx} Alpha\`, status: 'PROCESSED', type: 'success' as const,
          data: [
              ['name', \`Target \${idx} Alpha\`, 'val'],
              ['address', '1000 Main St', 'val'],
              ['phone', '555-0100', 'success'],
              ['website', 'NULL', 'missing'],
              ['rating', '4.5', 'success']
          ] as [string, string, string][]
      },
      {
          title: \`Target \${idx} Beta\`, status: 'PROCESSED', type: 'success' as const,
          data: [
              ['name', \`Target \${idx} Beta\`, 'val'],
              ['address', '200 Oak Ave', 'val'],
              ['phone', '555-0200', 'success'],
              ['website', 'NULL', 'missing'],
              ['rating', '4.2', 'success']
          ] as [string, string, string][]
      },
      {
          title: \`Target \${idx} Blocked\`, status: 'BLOCKED', type: 'rejected' as const,
          data: [
              ['name', \`Target \${idx} Blocked\`, 'val'],
              ['block_reason', '"skate shop" match', 'missing']
          ] as [string, string, string][]
      }
  ]
});

const mockBelts = [
    generateUniformBelt(1, 1, 'Scout Phase (Google Places Seed)', '--neon-scout', '0, 255, 170', 'Daemon_v2', 'PROCESSING SEED...', 'Roller City', 'EVALUATING BLOCKLIST', ['KS', 'MO', 'CA', 'TX', 'NY', 'FL', 'IL', 'WA', 'MI', 'GA']),
    generateUniformBelt(2, 2, 'Crawl Phase (Spider Engine)', '--neon-crawl', '157, 78, 221', 'Spider_v3', 'DEEP CRAWLING DOM...', 'Skate City Shawnee', 'FETCHING /schedule', ['KS', 'MO', 'CA', 'TX', 'FL', 'WA']),
    generateUniformBelt(3, 3, 'Detective Phase (Llama-3.2 Extraction)', '--neon-detective', '255, 106, 0', 'Llama3.2-8b', 'INFERENCING JSON...', 'Skate City OP', 'STREAMING TO DB', ['KS', 'MO', 'TX', 'FL']),
    generateUniformBelt(4, 4, 'Photographer (Cloud Vision)', '--neon-photo', '255, 0, 127', 'Vision_v1', 'ANALYZING IMAGES...', 'Oaks Park Roller Skating Rink', 'DETECTING HARDWOOD', ['WA', 'CA', 'NY', 'FL']),
    generateUniformBelt(5, 5, 'Publisher (DB Sync)', '--neon-publish', '0, 212, 255', 'Sync_v4', 'UPSERTING BATCH...', 'Skate City Aurora', 'COMMITTING TRANSACTION', ['KS', 'MO', 'TX', 'CA'])
];

export const ScraperPipeline: React.FC = () => {
    const { telemetry, loading } = useScraperTelemetry(2000);

    const mergedBelts = mockBelts.map(belt => {
        let liveData: any = null;
        if (belt.id === 1) liveData = telemetry.scout;
        if (belt.id === 2) liveData = telemetry.spider;
        if (belt.id === 3) liveData = telemetry.detective;
        if (belt.id === 4) liveData = telemetry.photographer;
        if (belt.id === 5) liveData = telemetry.publisher;

        if (liveData) {
            return {
                ...belt,
                job: liveData.active_job ? 'PROCESSING LIVE...' : 'IDLE',
                target: liveData.target || 'WAITING...',
                inQ: liveData.in_q && liveData.in_q.length > 0 ? liveData.in_q.slice(0, 3) : belt.inQ,
                outCards: [
                    ...(liveData.success || []),
                    ...(liveData.rejected || [])
                ].length > 0 ? [
                    ...(liveData.success || []),
                    ...(liveData.rejected || [])
                ].slice(0, 3) : belt.outCards
            };
        }
        return belt;
    });

    return (
        <div className="pipeline-dashboard-container max-w-[1920px] mx-auto flex flex-col gap-8 text-white relative">
            <style>{STYLES}</style>
            
            <div className="glass-panel p-5 flex items-center justify-between sticky top-[20px] z-[100]">
                <h1 className="text-3xl font-black tracking-[0.2em]" style={{ textShadow: '0 0 20px rgba(255,255,255,0.5)' }}>
                    SK8<span style={{ color: '#00ffaa', textShadow: '0 0 20px #00ffaa' }}>LYTZ</span> <span className="text-white/20 font-light ml-2 text-xl">PIPELINE (PHASE SLICING)</span>
                </h1>
                
                <div className="flex items-center gap-6">
                    <div className="flex items-center gap-4 px-4 py-2 bg-black/40 border border-white/5 rounded-xl data-font">
                        <div className="text-xs">
                            <span className="text-white/40 uppercase tracking-widest mr-2">Seeds</span>
                            <span className="text-[#00ffaa] font-bold">14,204</span>
                        </div>
                        <div className="w-px h-4 bg-white/10"></div>
                        <div className="text-xs">
                            <span className="text-white/40 uppercase tracking-widest mr-2">Throughput</span>
                            <span className="text-white font-bold">12 rec/m</span>
                        </div>
                        <div className="w-px h-4 bg-white/10"></div>
                        <div className="text-xs">
                            <span className="text-white/40 uppercase tracking-widest mr-2">Daemon Health</span>
                            <span className="text-[#00ffaa] font-bold animate-pulse">{loading ? 'SYNCING...' : 'OPTIMAL'}</span>
                        </div>
                    </div>
                    
                    <div className="flex items-center gap-2">
                        <button className="px-5 py-2.5 bg-[#00ffaa]/10 hover:bg-[#00ffaa]/20 border border-[#00ffaa]/50 rounded-lg text-[#00ffaa] text-xs font-black uppercase tracking-widest transition-all hover:shadow-[0_0_15px_rgba(0,255,170,0.5)]">
                            ▶ Start All
                        </button>
                        <button className="px-5 py-2.5 bg-red-500/10 hover:bg-red-500/20 border border-red-500/50 rounded-lg text-red-500 text-xs font-black uppercase tracking-widest transition-all">
                            ❚❚ Pause
                        </button>
                        <button className="w-10 h-10 flex items-center justify-center bg-white/5 hover:bg-white/10 border border-white/10 rounded-lg text-white/50 hover:text-white transition-all ml-2" title="Global Settings">
                            ⚙️
                        </button>
                    </div>
                </div>
            </div>
            
            <div className="flex flex-col mt-4">
                {mergedBelts.map(b => <BeltNode key={b.id} {...b} />)}
            </div>
        </div>
    );
}

export default ScraperPipeline;
