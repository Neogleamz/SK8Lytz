import React, { useState } from 'react';
import { PhaseControlDrawer } from './PhaseControlDrawer';

interface OutCard {
  title: string;
  status: string;
  type: 'success' | 'rejected';
  data: [string, React.ReactNode, string][];
  spotId?: string;
  spotName?: string;
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
  onPhaseNav?: () => void;
  // Daemon controls
  daemonActive?: boolean;
  onDaemonStart?: () => void;
  onDaemonStop?: () => void;
  hasDaemon?: boolean; // false = manual phase (Publisher)
  daemonStatus?: string; // live status string e.g. "PROCESSING: target.com"
  inputStatus: string;
  outputStatus: string;
  countBadges?: {label: string; value: string}[];
  onBlockSpot?: (spotId: string, spotName: string) => void;
  onRestartSpot?: (spotId: string) => void;
  onFreezeSpot?: (spotId: string) => void;
}

// Shared card dimensions
const PENDING_W = 136;
const SUCCESS_W = 250;
const MACHINE_W = 310;
const ARROW_W = 44;
const LEFT_W = PENDING_W * 2 + 8; // 280px — two cards + gap

const DataCard: React.FC<{
  title: string;
  badge: string;
  badgeColor: string;
  data: [string, React.ReactNode, string][];
  colColor: string;
  borderColor: string;
  bgColor: string;
  minW: number;
  maxW: number;
  onBlock?: () => void;
  onRestart?: () => void;
  onFreeze?: () => void;
}> = ({ title, badge, badgeColor, data, colColor, borderColor, bgColor, minW, maxW, onBlock, onRestart, onFreeze }) => (
  <div style={{
    minWidth: minW, maxWidth: maxW, flexShrink: 0,
    background: bgColor,
    border: `1px solid ${borderColor}`,
    borderRadius: 9, padding: '9px 12px',
    boxShadow: `0 8px 24px rgba(0,0,0,0.45)`,
    position: 'relative', overflow: 'hidden',
    alignSelf: 'stretch',
    display: 'flex', flexDirection: 'column',
  }}>
    <div style={{
      fontSize: '0.62rem', fontWeight: 900, color: '#fff', textTransform: 'uppercase',
      borderBottom: '1px solid rgba(255,255,255,0.07)', paddingBottom: 6, marginBottom: 7,
      display: 'flex', justifyContent: 'space-between', alignItems: 'center',
    }}>
      <span style={{ overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', maxWidth: '65%' }}>{title}</span>
      <span style={{ color: badgeColor, fontSize: '0.54rem', flexShrink: 0, marginLeft: 4, fontFamily: 'JetBrains Mono, monospace' }}>{badge}</span>
    </div>
    <div style={{ display: 'grid', gridTemplateColumns: '72px 1fr', gap: '3px 5px', fontFamily: 'JetBrains Mono, monospace', fontSize: '0.56rem', flex: 1 }}>
      {data.map(([k, v, cls], j) => (
        <React.Fragment key={j}>
          <div style={{ color: 'rgba(255,255,255,0.3)', textTransform: 'uppercase', fontWeight: 700, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{k}</div>
          <div style={{
            wordBreak: 'break-all',
            color: cls === 'missing' ? '#ff3366' : cls === 'success' ? colColor : 'rgba(255,255,255,0.82)',
            fontStyle: cls === 'missing' ? 'italic' : 'normal',
            fontWeight: cls === 'success' ? 700 : 400,
          }}>{v}</div>
        </React.Fragment>
      ))}
    </div>
    {onBlock && (
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: 4, marginTop: 7 }}>
        <button
          onClick={(e) => { e.stopPropagation(); onRestart?.(); }}
          style={{
            padding: '4px 0', background: 'rgba(0, 255, 150, 0.08)', border: '1px solid rgba(0, 255, 150, 0.4)',
            borderRadius: 5, cursor: 'pointer', color: '#00ff96', fontSize: '0.45rem', fontWeight: 900,
            fontFamily: 'JetBrains Mono, monospace', textTransform: 'uppercase', transition: 'all 0.15s',
          }}
          onMouseEnter={e => { (e.currentTarget as HTMLElement).style.background = 'rgba(0,255,150,0.22)'; }}
          onMouseLeave={e => { (e.currentTarget as HTMLElement).style.background = 'rgba(0,255,150,0.08)'; }}
        >
          [RESTART]
        </button>
        <button
          onClick={(e) => { e.stopPropagation(); onFreeze?.(); }}
          style={{
            padding: '4px 0', background: 'rgba(0, 200, 255, 0.08)', border: '1px solid rgba(0, 200, 255, 0.4)',
            borderRadius: 5, cursor: 'pointer', color: '#00c8ff', fontSize: '0.45rem', fontWeight: 900,
            fontFamily: 'JetBrains Mono, monospace', textTransform: 'uppercase', transition: 'all 0.15s',
          }}
          onMouseEnter={e => { (e.currentTarget as HTMLElement).style.background = 'rgba(0,200,255,0.22)'; }}
          onMouseLeave={e => { (e.currentTarget as HTMLElement).style.background = 'rgba(0,200,255,0.08)'; }}
        >
          [FREEZE]
        </button>
        <button
          onClick={(e) => { e.stopPropagation(); onBlock(); }}
          style={{
            padding: '4px 0', background: 'rgba(255, 51, 102, 0.08)', border: '1px solid rgba(255, 51, 102, 0.4)',
            borderRadius: 5, cursor: 'pointer', color: '#ff3366', fontSize: '0.45rem', fontWeight: 900,
            fontFamily: 'JetBrains Mono, monospace', textTransform: 'uppercase', transition: 'all 0.15s',
          }}
          onMouseEnter={e => { (e.currentTarget as HTMLElement).style.background = 'rgba(255,51,102,0.22)'; }}
          onMouseLeave={e => { (e.currentTarget as HTMLElement).style.background = 'rgba(255,51,102,0.08)'; }}
        >
          [BLOCK]
        </button>
      </div>
    )}
  </div>
);

export const BeltNode: React.FC<BeltProps> = ({
  id, name, color, rgb, job, daemon, target, inQ, status, gatekeeper, attempting, outCards,
  onPhaseNav, daemonActive = false, onDaemonStart, onDaemonStop, hasDaemon = true, daemonStatus, inputStatus, outputStatus, countBadges = [],
  onBlockSpot, onRestartSpot, onFreezeSpot,
}) => {
  const [isConfigOpen, setConfigOpen] = useState(false);
  const successCards = outCards.filter(c => c.type === 'success').slice(0, 2);
  const rejectedCards = outCards.filter(c => c.type === 'rejected').slice(0, 2);
  // Always show 2 slots in each lane
  const pendingSlots: (string | null)[] = [inQ[0] ?? null, inQ[1] ?? null];
  const rejectedSlots = rejectedCards.length > 0 ? rejectedCards : null;

  const colVar = `var(${color})`;
  const colRgb = rgb;

  return (
    <div style={{
      width: '100%',
      background: `linear-gradient(160deg, rgba(${colRgb},0.04) 0%, rgba(0,0,0,0.35) 100%)`,
      border: `1px solid rgba(${colRgb}, 0.22)`,
      borderRadius: 16,
      boxShadow: `0 0 40px rgba(${colRgb},0.06), inset 0 1px 0 rgba(255,255,255,0.04)`,
      overflow: 'hidden',
      marginBottom: 10,
      position: 'relative',
    }}>

      {/* ── PHASE HEADER BAR ── */}
      <div style={{
        width: '100%',
        background: `linear-gradient(90deg, rgba(${colRgb},0.18) 0%, rgba(${colRgb},0.06) 50%, rgba(${colRgb},0.18) 100%)`,
        borderBottom: `1px solid rgba(${colRgb}, 0.18)`,
        padding: '9px 20px',
        display: 'flex', alignItems: 'center',
        position: 'relative',
        gap: 12,
        overflow: 'hidden',
        boxSizing: 'border-box',
      }}>
        {/* left/right accent lines */}
        <div style={{ position: 'absolute', left: 0, top: 0, bottom: 0, width: 3, background: colVar, boxShadow: `0 0 12px ${colVar}` }} />
        <div style={{ position: 'absolute', right: 0, top: 0, bottom: 0, width: 3, background: colVar, boxShadow: `0 0 12px ${colVar}` }} />

        {/* Phase name — clickable */}
        <button onClick={() => setConfigOpen(!isConfigOpen)} style={{
          background: 'none', border: 'none', cursor: 'pointer',
          display: 'flex', alignItems: 'center', gap: 10, padding: 0,
          flexShrink: 0,
        }}>
          <div style={{ width: 8, height: 8, borderRadius: '50%', background: colVar, boxShadow: `0 0 12px ${colVar}, 0 0 24px ${colVar}`, flexShrink: 0 }} />
          <span style={{
            fontSize: '0.85rem', fontWeight: 900, textTransform: 'uppercase',
            letterSpacing: '0.22em', color: '#fff',
            textShadow: `0 0 20px ${colVar}, 0 0 40px rgba(${colRgb},0.5)`,
          }}>{name}</span>
          <span style={{ fontSize: '0.5rem', color: 'rgba(255,255,255,0.22)', fontFamily: 'JetBrains Mono, monospace', marginLeft: 6 }}>
            {isConfigOpen ? '▼ CLOSE' : '↗ CONFIGURE'}
          </span>
        </button>

        {/* Count badges: pipeline throughput metrics */}
        {countBadges.length > 0 && (
          <div style={{ display: 'flex', alignItems: 'center', gap: 5, flexShrink: 0 }}>
            {countBadges.map((badge, i) => (
              <div key={i} style={{
                display: 'flex', alignItems: 'center', gap: 4,
                padding: '2px 8px', borderRadius: 10,
                background: `rgba(${colRgb},0.1)`,
                border: `1px solid rgba(${colRgb},0.2)`,
              }}>
                <span style={{ fontSize: '0.48rem', fontWeight: 900, color: `rgba(${colRgb},0.7)`, textTransform: 'uppercase', letterSpacing: '0.1em', fontFamily: 'JetBrains Mono, monospace' }}>{badge.label}</span>
                <span style={{ fontSize: '0.56rem', fontWeight: 800, color: '#fff', fontFamily: 'JetBrains Mono, monospace' }}>{badge.value}</span>
              </div>
            ))}
          </div>
        )}

        {/* Live daemon status ticker — center, must shrink not push buttons off */}
        <div style={{ flex: 1, minWidth: 0, display: 'flex', alignItems: 'center', justifyContent: 'center', overflow: 'hidden', padding: '0 8px' }}>
          {daemonStatus ? (
            <div style={{
              display: 'flex', alignItems: 'center', gap: 8,
              padding: '3px 12px', borderRadius: 20,
              background: daemonActive ? `rgba(${colRgb},0.12)` : 'rgba(255,255,255,0.04)',
              border: `1px solid ${daemonActive ? `rgba(${colRgb},0.25)` : 'rgba(255,255,255,0.07)'}`,
            }}>
              {daemonActive && (
                <div style={{ width: 5, height: 5, borderRadius: '50%', background: colVar, boxShadow: `0 0 6px ${colVar}`, flexShrink: 0, animation: 'blink 1s step-end infinite' }} />
              )}
              <span style={{
                fontSize: '0.6rem', fontFamily: 'JetBrains Mono, monospace', fontWeight: 700,
                color: daemonActive ? colVar : 'rgba(255,255,255,0.3)',
                textTransform: 'uppercase', letterSpacing: '0.08em',
                overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', maxWidth: 480,
              }}>{daemonStatus}</span>
            </div>
          ) : (
            <span style={{ fontSize: '0.55rem', color: 'rgba(255,255,255,0.12)', fontFamily: 'JetBrains Mono, monospace', textTransform: 'uppercase', letterSpacing: '0.1em' }}>
              — no telemetry —
            </span>
          )}
        </div>

        {/* Daemon START / STOP pill */}
        {hasDaemon && (
          <div style={{ display: 'flex', alignItems: 'center', gap: 6, flexShrink: 0 }}>
            {/* Live status dot */}
            <div style={{
              width: 7, height: 7, borderRadius: '50%',
              background: daemonActive ? colVar : 'rgba(255,255,255,0.18)',
              boxShadow: daemonActive ? `0 0 8px ${colVar}` : 'none',
              animation: daemonActive ? 'blink 1.5s step-end infinite' : 'none',
            }} />
            <span style={{ fontSize: '0.52rem', color: daemonActive ? colVar : 'rgba(255,255,255,0.3)', fontFamily: 'JetBrains Mono, monospace', fontWeight: 800, textTransform: 'uppercase', letterSpacing: '0.1em' }}>
              {daemonActive ? 'RUNNING' : 'IDLE'}
            </span>
            {!daemonActive ? (
              <button onClick={onDaemonStart} style={{
                padding: '3px 10px', borderRadius: 12, border: 'none', cursor: 'pointer',
                background: `rgba(${colRgb},0.25)`, color: colVar,
                fontSize: '0.58rem', fontWeight: 900, letterSpacing: '0.08em',
                boxShadow: `0 0 8px rgba(${colRgb},0.2)`,
              }}>▶ START</button>
            ) : (
              <button onClick={onDaemonStop} style={{
                padding: '3px 10px', borderRadius: 12, border: 'none', cursor: 'pointer',
                background: 'rgba(255,60,60,0.2)', color: '#ff6b6b',
                fontSize: '0.58rem', fontWeight: 900, letterSpacing: '0.08em',
              }}>■ STOP</button>
            )}
          </div>
        )}
        {!hasDaemon && (
          <span style={{ fontSize: '0.52rem', color: 'rgba(255,255,255,0.2)', fontFamily: 'JetBrains Mono, monospace', flexShrink: 0 }}>MANUAL QA GATE</span>
        )}
      </div>

      <PhaseControlDrawer phaseId={id} isOpen={isConfigOpen} onClose={() => setConfigOpen(false)} colColor={colVar} />

      {/* ── BELT CONTENT ── */}
      <div style={{ padding: '10px 14px 12px' }}>

      {/*
        GRID LAYOUT (5 columns, 2 rows):
        Col 1: LEFT_W  — pending (row1) / rejected (row2)
        Col 2: ARROW_W — ▶ in (row1) / ◀ reject (row2)
        Col 3: MACHINE_W — machine (spans rows 1+2)
        Col 4: ARROW_W — ▶ out (row1) / empty (row2)
        Col 5: 1fr      — success cards (row1) / empty (row2)
      */}
      <div style={{
        display: 'grid',
        gridTemplateColumns: `${LEFT_W}px ${ARROW_W}px ${MACHINE_W}px ${ARROW_W}px 1fr`,
        gridTemplateRows: 'auto auto',
        gap: '6px 0',
        alignItems: 'stretch',
        position: 'relative',
      }}>

        {/* ── Belt track lines ── */}
        {/* Top lane track */}
        <div style={{
          position: 'absolute',
          top: '25%', left: 0, right: 0, height: 1,
          borderTop: `1px dashed rgba(${colRgb}, 0.2)`,
          background: `rgba(${colRgb}, 0.06)`,
          zIndex: 0, pointerEvents: 'none',
          transform: 'translateY(-50%)',
        }} />
        {/* Bottom lane track (reject) */}
        {rejectedSlots && (
          <div style={{
            position: 'absolute',
            top: '75%', left: 0, width: `${LEFT_W + ARROW_W + MACHINE_W}px`, height: 1,
            borderTop: '1px dashed rgba(255,51,102,0.2)',
            zIndex: 0, pointerEvents: 'none',
            transform: 'translateY(-50%)',
          }} />
        )}

        {/* ═══ COL 1, ROW 1: PENDING CARDS ═══ */}
        <div style={{
          gridColumn: 1, gridRow: 1,
          display: 'flex', alignItems: 'center', justifyContent: 'flex-end',
          gap: 8, paddingRight: 0, zIndex: 1, minHeight: 80,
        }}>
          {pendingSlots.map((iq, i) => (
            <div key={`p${i}`} style={{
              width: PENDING_W, minHeight: 72,
              background: iq ? 'rgba(20,20,35,0.95)' : 'rgba(10,10,15,0.85)',
              border: iq ? '1px solid rgba(255,255,255,0.18)' : '1px solid rgba(255,255,255,0.06)',
              borderRadius: 8, padding: '0 12px',
              display: 'flex', flexDirection: 'column', justifyContent: 'center',
              opacity: iq ? 0.92 : 0.18,
              flexShrink: 0,
            }}>
              <div style={{ fontSize: '0.55rem', fontWeight: 900, color: iq ? '#aaffdd' : '#fff', textTransform: 'uppercase', letterSpacing: '0.1em', fontFamily: 'JetBrains Mono, monospace' }}>
                {iq ? 'QUEUED' : '— IDLE —'}
              </div>
              {iq && (
                <div style={{ fontSize: '0.56rem', color: 'rgba(255,255,255,0.72)', marginTop: 4, fontFamily: 'JetBrains Mono, monospace', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', fontWeight: 600 }}>
                  {iq}
                </div>
              )}
            </div>
          ))}
        </div>

        {/* ═══ COL 1, ROW 2: REJECTED CARDS (flowing back ←) ═══ */}
        <div style={{
          gridColumn: 1, gridRow: 2,
          display: 'flex', alignItems: 'center', justifyContent: 'flex-end',
          gap: 8, paddingRight: 0, zIndex: 1, minHeight: 72,
        }}>
          {rejectedSlots ? rejectedSlots.map((rc, i) => (
            <div key={`rej${i}`} style={{
              width: PENDING_W, minHeight: 72,
              background: 'rgba(255,20,60,0.06)',
              border: '1px solid rgba(255,51,102,0.35)',
              borderRadius: 8, padding: '0 12px',
              display: 'flex', flexDirection: 'column', justifyContent: 'center',
              flexShrink: 0, opacity: 0.88,
            }}>
              <div style={{ fontSize: '0.54rem', fontWeight: 900, color: '#ff3366', textTransform: 'uppercase', letterSpacing: '0.1em', fontFamily: 'JetBrains Mono, monospace', marginBottom: 3, display: 'flex', alignItems: 'center', gap: 4 }}>
                <span>✗</span> REJECTED
              </div>
              <div style={{ fontSize: '0.52rem', color: 'rgba(255,120,140,0.7)', fontFamily: 'JetBrains Mono, monospace', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', marginBottom: 2 }}>{rc.title}</div>
              {rc.data[0] && (
                <div style={{ fontSize: '0.5rem', color: 'rgba(255,80,100,0.7)', fontFamily: 'JetBrains Mono, monospace', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                  {rc.data[0][0]}: {rc.data[0][1]}
                </div>
              )}
            </div>
          )) : (
            // empty bottom-left when no rejects — same size as pending for alignment
            <div style={{ width: PENDING_W * 2 + 8, minHeight: 72 }} />
          )}
        </div>

        {/* ═══ COL 2, ROW 1: ARROW → into machine ═══ */}
        <div style={{
          gridColumn: 2, gridRow: 1,
          display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
          zIndex: 1, gap: 2
        }}>
          <div style={{ fontSize: '0.45rem', fontWeight: 900, color: colVar, textTransform: 'uppercase', fontFamily: 'JetBrains Mono, monospace', textAlign: 'center', lineHeight: 1 }}>{inputStatus}</div>
          <div style={{
            fontSize: '1.3rem', color: colVar,
            textShadow: `0 0 12px ${colVar}`,
            animation: 'beltFlowRight 1.5s infinite linear',
            lineHeight: 0.8
          }}>▶</div>
        </div>

        {/* ═══ COL 2, ROW 2: ARROW ◀ reject out of machine ═══ */}
        {rejectedSlots && (
          <div style={{
            gridColumn: 2, gridRow: 2,
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            zIndex: 1,
            fontSize: '1.3rem', color: '#ff3366',
            textShadow: '0 0 12px #ff3366',
            animation: 'beltFlowLeft 1.5s infinite linear',
            opacity: 0.75,
          }}>◀</div>
        )}

        {/* ═══ COL 3, ROWS 1+2: MACHINE ═══ */}
        <div style={{
          gridColumn: 3, gridRow: '1 / 3',
          zIndex: 10,
          background: 'linear-gradient(180deg, rgba(20,20,30,0.98) 0%, rgba(10,10,15,0.99) 100%)',
          border: `1px solid ${colVar}`,
          borderRadius: 14,
          boxShadow: `0 0 30px rgba(${colRgb},0.18), inset 0 0 18px rgba(${colRgb},0.07)`,
          position: 'relative',
          display: 'flex', flexDirection: 'column',
        }}>
          {/* top glow bar */}
          <div style={{ position: 'absolute', top: -1, left: '25%', right: '25%', height: 2, background: colVar, boxShadow: `0 0 12px ${colVar}`, borderRadius: 2 }} />

          {/* header */}
          <div style={{
            padding: '10px 16px', borderBottom: '1px solid rgba(255,255,255,0.08)',
            display: 'flex', justifyContent: 'space-between', alignItems: 'center',
            background: 'rgba(0,0,0,0.2)', borderRadius: '14px 14px 0 0',
          }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
              <div style={{
                width: 24, height: 24, borderRadius: '50%',
                border: `2px dashed ${colVar}`,
                animation: 'spin 4s linear infinite',
                display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0,
              }}>
                <div style={{ width: 5, height: 5, borderRadius: '50%', background: colVar, boxShadow: `0 0 7px ${colVar}` }} />
              </div>
              <div>
                <div style={{ fontSize: '0.56rem', fontWeight: 700, color: 'rgba(255,255,255,0.38)', textTransform: 'uppercase', letterSpacing: '0.2em' }}>Active Job</div>
                <div style={{ fontSize: '0.72rem', fontWeight: 900, color: colVar }}>{job}</div>
              </div>
            </div>
            <div style={{ fontSize: '0.55rem', fontFamily: 'JetBrains Mono, monospace', color: 'rgba(255,255,255,0.3)' }}>{daemon}</div>
          </div>

          {/* body */}
          <div style={{ padding: '10px 16px', flex: 1, display: 'flex', flexDirection: 'column', gap: 7 }}>
            <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
              <span style={{ fontSize: '0.58rem', color: 'rgba(255,255,255,0.32)', textTransform: 'uppercase', fontWeight: 700, width: 48, flexShrink: 0 }}>Target</span>
              <span style={{ fontSize: '0.68rem', fontWeight: 700, color: '#fff', fontFamily: 'JetBrains Mono, monospace' }}>{target}</span>
            </div>

            {gatekeeper && gatekeeper.length > 0 && (
              <div style={{ background: 'rgba(255,51,102,0.05)', border: '1px solid rgba(255,51,102,0.18)', borderRadius: 6, padding: '5px 9px' }}>
                <div style={{ fontSize: '0.52rem', fontWeight: 900, color: '#ff3366', textTransform: 'uppercase', letterSpacing: '0.1em', marginBottom: 3 }}>GATEKEEPER:</div>
                {gatekeeper.map((r, i) => (
                  <div key={i} style={{ fontSize: '0.56rem', color: 'rgba(255,255,255,0.55)', display: 'flex', alignItems: 'center', gap: 5 }}>
                    <span style={{ color: '#ff3366', fontSize: 6 }}>■</span>{r}
                  </div>
                ))}
              </div>
            )}

            {attempting && attempting.length > 0 && (
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '2px 8px', fontFamily: 'JetBrains Mono, monospace', fontSize: '0.56rem' }}>
                {attempting.map(([tname, tstatus], i) => (
                  <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 4, color: tstatus === 'done' ? colVar : tstatus === 'fail' ? '#ff3366' : 'rgba(255,255,255,0.35)' }}>
                    <span>{tstatus === 'done' ? '[✓]' : tstatus === 'fail' ? '[✗]' : '[ ]'}</span>
                    <span>{tname}</span>
                  </div>
                ))}
              </div>
            )}

            <div style={{
              padding: '5px 10px', background: 'rgba(0,0,0,0.5)',
              border: `1px solid rgba(${colRgb},0.2)`, borderRadius: 6,
              display: 'flex', justifyContent: 'space-between',
              fontFamily: 'JetBrains Mono, monospace', fontSize: '0.6rem',
              color: colVar, marginTop: 'auto',
            }}>
              <span>STATUS: <span style={{ animation: 'blink 1.2s step-end infinite' }}>{status}</span></span>
              <span style={{ color: 'rgba(255,255,255,0.22)' }}>[live]</span>
            </div>
          </div>
        </div>

        {/* ═══ COL 4, ROW 1: ARROW → out of machine ═══ */}
        <div style={{
          gridColumn: 4, gridRow: 1,
          display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
          zIndex: 1, gap: 2
        }}>
          <div style={{ fontSize: '0.45rem', fontWeight: 900, color: colVar, textTransform: 'uppercase', fontFamily: 'JetBrains Mono, monospace', textAlign: 'center', lineHeight: 1 }}>{outputStatus}</div>
          <div style={{
            fontSize: '1.3rem', color: colVar,
            textShadow: `0 0 12px ${colVar}`,
            animation: 'beltFlowRight 1.5s infinite linear',
            lineHeight: 0.8
          }}>▶</div>
        </div>

        {/* ═══ COL 5, ROW 1: SUCCESS CARDS ═══ */}
        <div style={{
          gridColumn: 5, gridRow: 1,
          display: 'flex', alignItems: 'center',
          gap: 8, paddingLeft: 4, zIndex: 1,
        }}>
          {successCards.length === 0 ? (
            <div style={{
              width: SUCCESS_W, minHeight: 72,
              background: 'rgba(10,10,15,0.4)',
              border: '1px dashed rgba(255,255,255,0.05)',
              borderRadius: 8, display: 'flex', alignItems: 'center', justifyContent: 'center',
            }}>
              <span style={{ fontSize: '0.52rem', color: 'rgba(255,255,255,0.18)', fontFamily: 'JetBrains Mono, monospace', textTransform: 'uppercase' }}>awaiting...</span>
            </div>
          ) : successCards.map((sc, i) => (
            <DataCard
              key={i}
              title={sc.title}
              badge={sc.status}
              badgeColor={colVar}
              data={sc.data}
              colColor={colVar}
              borderColor={colVar}
              bgColor="rgba(10,10,15,0.95)"
              minW={SUCCESS_W}
              maxW={300}
              onBlock={onBlockSpot && sc.spotId ? () => onBlockSpot(sc.spotId!, sc.spotName ?? sc.title) : undefined}
              onRestart={onRestartSpot && sc.spotId ? () => onRestartSpot(sc.spotId!) : undefined}
              onFreeze={onFreezeSpot && sc.spotId ? () => onFreezeSpot(sc.spotId!) : undefined}
            />
          ))}
        </div>

        {/* ═══ COL 4+5, ROW 2: empty spacer ═══ */}
        <div style={{ gridColumn: '4 / 6', gridRow: 2 }} />

      </div>
      </div>  {/* end belt content padding */}

      <style>{`
        @keyframes beltFlowRight { 0%{transform:translateX(-4px);opacity:0} 50%{opacity:1} 100%{transform:translateX(4px);opacity:0} }
        @keyframes beltFlowLeft  { 0%{transform:translateX(4px);opacity:0}  50%{opacity:1} 100%{transform:translateX(-4px);opacity:0} }
        @keyframes spin  { 100%{transform:rotate(360deg)} }
        @keyframes blink { 0%,100%{opacity:1} 50%{opacity:0.3} }
      `}</style>
    </div>
  );
};

export default BeltNode;
