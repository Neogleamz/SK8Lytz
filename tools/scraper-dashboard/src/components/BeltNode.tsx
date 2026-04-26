import React from 'react';
import USAMap from '../USMap';

interface OutCard {
  title: string;
  status: string;
  type: 'success' | 'rejected';
  data: [string, string, string][];
}

interface BeltProps {
  id: number;
  name: string;
  color: string;
  rgb: string;
  activeStates: string[];
  job: string;
  daemon: string;
  target: string;
  inQ: string[];
  status: string;
  gatekeeper?: string[];
  attempting?: [string, string][];
  outCards: OutCard[];
}

export const BeltNode: React.FC<BeltProps> = ({
  name, color, rgb, activeStates, job, daemon, target, inQ, status, gatekeeper, attempting, outCards
}) => {
  const successCards = outCards.filter(c => c.type === 'success');
  const rejectedCards = outCards.filter(c => c.type === 'rejected');

  return (
    <>
      <div className="flex items-center gap-2 mt-4 ml-5">
        <div className="w-2 h-2 rounded-full" style={{ background: `var(${color})`, boxShadow: `0 0 10px var(${color})` }}></div>
        <div className="text-[0.65rem] font-black uppercase tracking-widest" style={{ color: `var(${color})` }}>{name}</div>
      </div>
      
      <div className="belt-wrapper">
        <div className="belt-flow-area">
          <div className="assembly-line" style={{ '--col-color': `var(${color})`, '--col-color-rgb': rgb } as React.CSSProperties}>
              
            {/* BELT TRACK */}
            <div className="absolute top-1/2 left-0 right-[350px] h-[2px] border-t border-dashed border-white/20 bg-white/5 z-0" style={{ boxShadow: `0 0 10px rgba(${rgb}, 0.2)`, transform: 'translateY(-50%)' }}></div>

            {/* GRID COL 1: Pending Flow & Rejected Flow */}
            <div style={{ gridColumn: 1, gridRow: 1, justifySelf: 'end', zIndex: 2 }} className="flex flex-col justify-center h-full gap-4 pt-8 pb-4">
                {/* Pending Flow (Top) */}
                <div className="flex items-center justify-end gap-4">
                    {inQ.map((iq, i) => (
                      <div key={`pending-${i}`} className="mini-card">
                          <div className="title">PENDING</div>
                          <div className="subtitle">{iq}</div>
                      </div>
                    ))}
                </div>
                
                {/* Rejected Flow (Bottom) */}
                {rejectedCards.length > 0 && (
                  <div className="flex items-center justify-end gap-4 mt-8 opacity-90">
                    {rejectedCards.map((rc, i) => (
                      <div key={`reject-${i}`} className="mini-card" style={{ borderColor: '#ff3366', background: 'rgba(255,51,102,0.1)', opacity: 0.8, boxShadow: '0 5px 15px rgba(255,51,102,0.3)' }}>
                          <div className="title" style={{ color: '#ff3366' }}>REJECTED</div>
                          <div className="subtitle" style={{ color: 'rgba(255,51,102,0.7)' }}>{rc.title}</div>
                      </div>
                    ))}
                  </div>
                )}
            </div>
            
            {/* GRID COL 2: Flow Arrows into/out of Machine */}
            <div style={{ gridColumn: 2, gridRow: 1, justifySelf: 'center', zIndex: 2 }} className="flex flex-col justify-center h-full gap-4 pt-8 pb-4">
                <div className="flow-arrow-right">▶</div>
                {rejectedCards.length > 0 && (
                  <div className="flow-arrow-right" style={{ color: '#ff3366', textShadow: '0 0 15px #ff3366', transform: 'scaleX(-1)', marginTop: '3.5rem' }}>▶</div>
                )}
            </div>
            
            {/* GRID COL 3: MACHINE */}
            <div style={{ gridColumn: 3, gridRow: 1, zIndex: 2 }} className="active-machine-node relative bg-black">
                <div className="machine-header">
                    <div className="flex items-center gap-3">
                        <div className="processing-indicator"><div className="w-2 h-2 rounded-full" style={{ background: `var(${color})`, boxShadow: `0 0 10px var(${color})` }}></div></div>
                        <div><div className="text-[0.65rem] font-bold text-white/50 tracking-[0.2em] uppercase">Active Job</div><div className="text-sm font-black" style={{ color: `var(${color})` }}>{job}</div></div>
                    </div>
                    <div className="text-[0.65rem] data-font text-white/40">{daemon}</div>
                </div>
                <div className="machine-body">
                    <div className="flex"><span className="w-24 text-white/40 text-xs uppercase tracking-wider font-bold">Target</span><span className="text-white font-bold text-sm">{target}</span></div>
                    
                    {gatekeeper && gatekeeper.length > 0 && (
                      <div className="gatekeeper-rules">
                          <div className="gatekeeper-title">GATEKEEPER RULES:</div>
                          {gatekeeper.map((r, i) => <div key={`gk-${i}`} className="rule-item">{r}</div>)}
                      </div>
                    )}
                    
                    {attempting && attempting.length > 0 && (
                      <div className="targeting-list">
                          <div className="targeting-title">Columns Target:</div>
                          {attempting.map(([tname, tstatus], i) => <div key={`att-${i}`} className={`target-item ${tstatus}`}>{tname}</div>)}
                      </div>
                    )}
                    
                    <div className="px-3 py-2 bg-black/50 border rounded text-[0.7rem] data-font flex justify-between" style={{ borderColor: `rgba(${rgb},0.2)`, color: `var(${color})` }}>
                        <span>STATUS: <span className="animate-pulse">{status}</span></span><span className="text-white/30">[42ms]</span>
                    </div>
                </div>
            </div>
            
            {/* GRID COL 4: Flow Arrow Right from Machine */}
            <div style={{ gridColumn: 4, gridRow: 1, justifySelf: 'center', zIndex: 2 }} className="flow-arrow-right">▶</div>
            
            {/* GRID COL 5: Successful Output Lane (Right) */}
            <div style={{ gridColumn: 5, gridRow: 1, justifySelf: 'start', zIndex: 2 }} className="flex gap-4">
                {successCards.map((sc, i) => (
                  <div key={`succ-${i}`} className="detailed-card" style={{ borderColor: `rgba(${rgb},0.6)` }}>
                      <div className="detailed-card-header">
                          <span style={{ color: `var(${color})` }}>{sc.title}</span>
                          <span style={{ color: 'rgba(255,255,255,0.6)' }}>{sc.status}</span>
                      </div>
                      <div className="data-grid">
                          {sc.data.map(([k, v, cls], j) => (
                            <React.Fragment key={`dg-${j}`}>
                              <div className="data-key">{k}</div>
                              <div className={`data-val ${cls}`}>{v}</div>
                            </React.Fragment>
                          ))}
                      </div>
                  </div>
                ))}
            </div>
            
            {/* GRID COL 6: MAP */}
            <div style={{ gridColumn: 6, gridRow: 1, zIndex: 2, '--col-color': `var(${color})`, alignSelf: 'center' } as React.CSSProperties} className="phase-map-container w-[300px] h-[250px] relative ml-5">
                <div className="absolute top-3 left-4 text-[0.6rem] font-black uppercase tracking-widest text-white/40 z-10">{name.split('(')[0].trim()} Saturation</div>
                <div style={{width: '100%', height: 'auto', opacity: 0.8, filter: `drop-shadow(0 0 10px var(${color}))`, padding: 20}}>
                  <USAMap 
                    width="100%" 
                    height="100%"
                    defaultFill="rgba(255,255,255,0.02)"
                    customize={
                      activeStates.reduce((acc, st) => {
                        acc[st] = { fill: `rgba(${rgb},0.8)` };
                        return acc;
                      }, {} as Record<string, any>)
                    }
                  />
                </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default BeltNode;
