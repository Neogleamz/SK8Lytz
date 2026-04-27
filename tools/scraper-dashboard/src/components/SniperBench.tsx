import React, { useState, useRef } from 'react';
import { useFieldRegistry } from '../hooks/useFieldRegistry';

// ─── Types ───────────────────────────────────────────────────────────────────
type RunState = 'idle' | 'running' | 'done' | 'error';

export const SniperBench: React.FC = () => {
  const { fields, loading: fieldsLoading } = useFieldRegistry();
  const [spotId, setSpotId] = useState('');
  const [runState, setRunState] = useState<RunState>('idle');
  const [executionLog, setExecutionLog] = useState<string[]>([]);
  const [liveSpot, setLiveSpot] = useState<Record<string, any> | null>(null);
  const [sniperResult, setSniperResult] = useState<any>(null);
  const esRef = useRef<EventSource | null>(null);
  const logEndRef = useRef<HTMLDivElement>(null);

  const log = (msg: string) => {
    setExecutionLog(prev => {
      const next = [...prev, `[${new Date().toLocaleTimeString()}] ${msg}`];
      // Auto-scroll after state update
      setTimeout(() => logEndRef.current?.scrollIntoView({ behavior: 'smooth' }), 50);
      return next;
    });
  };

  const handleFire = () => {
    if (!spotId.trim()) return alert('Enter a Spot ID to target.');
    if (esRef.current) { esRef.current.close(); esRef.current = null; }

    setRunState('running');
    setSniperResult(null);
    setLiveSpot(null);
    setExecutionLog([]);

    log(`[Sniper] 🎯 Connecting to isolated pipeline stream for spot: ${spotId}`);

    const es = new EventSource(`http://localhost:5999/api/sniper/stream?spot_id=${encodeURIComponent(spotId.trim())}`);
    esRef.current = es;

    es.onmessage = (event) => {
      try {
        const { type, payload } = JSON.parse(event.data);
        if (type === 'log') {
          log(payload);
        } else if (type === 'result') {
          setSniperResult(payload);
          // Populate the 68-field report card using mappedFields
          if (payload.detectiveResult?.mappedFields) {
            setLiveSpot(payload.detectiveResult.mappedFields);
          }
        } else if (type === 'error') {
          log(`❌ ERROR: ${payload}`);
          setRunState('error');
        } else if (type === 'done') {
          es.close();
          esRef.current = null;
          setRunState(prev => prev === 'error' ? 'error' : 'done');
          log('[Sniper] 🔒 Stream closed. Pipeline run complete.');
        }
      } catch (e) {
        log(`[Sniper] ⚠️ Failed to parse SSE event: ${event.data}`);
      }
    };

    es.onerror = () => {
      log('[Sniper] ❌ SSE connection lost. Is CCTower running on port 5999?');
      es.close();
      esRef.current = null;
      setRunState('error');
    };
  };

  const handleCancel = () => {
    if (esRef.current) { esRef.current.close(); esRef.current = null; }
    log('[Sniper] ⛔ Cancelled by user — NO database changes were made.');
    setRunState('idle');
  };

  const handleReset = () => {
    if (esRef.current) { esRef.current.close(); esRef.current = null; }
    setRunState('idle');
    setSniperResult(null);
    setLiveSpot(null);
    setExecutionLog([]);
  };

  if (fieldsLoading) return <div className="p-8 text-white">Loading Field Registry...</div>;

  const statusColor: Record<RunState, string> = {
    idle: '#94a3b8', running: '#f43f5e', done: '#4ade80', error: '#f97316'
  };
  const statusLabel: Record<RunState, string> = {
    idle: '⏸ READY', running: '⚡ RUNNING', done: '✅ COMPLETE', error: '❌ ERROR'
  };

  return (
    <div style={{ display: 'flex', height: '100%', width: '100%', background: '#0B0D11', color: '#e2e8f0', overflow: 'hidden', fontFamily: 'Outfit, sans-serif' }}>

      {/* LEFT: Controls & Logs */}
      <div style={{ width: '36%', display: 'flex', flexDirection: 'column', borderRight: '1px solid rgba(255,255,255,0.1)', background: '#0F1218' }}>

        {/* Header */}
        <div style={{ padding: '1.25rem 1.5rem', borderBottom: '1px solid rgba(255,255,255,0.08)', flexShrink: 0 }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '6px' }}>
            <span style={{ color: '#f43f5e', fontSize: '1.25rem' }}>🎯</span>
            <h2 style={{ fontSize: '1.1rem', fontWeight: 'bold', color: '#fff', margin: 0 }}>Sniper Bench</h2>
            <span style={{ marginLeft: 'auto', fontSize: '0.65rem', fontWeight: 800, letterSpacing: '0.05em', color: statusColor[runState], background: `${statusColor[runState]}18`, border: `1px solid ${statusColor[runState]}44`, padding: '3px 8px', borderRadius: '4px' }}>
              {statusLabel[runState]}
            </span>
          </div>
          <p style={{ fontSize: '0.72rem', color: 'rgba(255,255,255,0.35)', margin: 0, lineHeight: 1.5 }}>
            Isolated QA lab. Targets a single DB record by ID and runs the full Spider → Detective pipeline in memory. <strong style={{ color: '#f43f5e' }}>Zero database writes.</strong>
          </p>
        </div>

        {/* Target Input */}
        <div style={{ padding: '1.25rem 1.5rem', borderBottom: '1px solid rgba(255,255,255,0.06)', flexShrink: 0 }}>
          <label style={{ display: 'block', fontSize: '0.7rem', fontWeight: 'bold', color: 'rgba(255,255,255,0.4)', marginBottom: '6px', textTransform: 'uppercase', letterSpacing: '0.06em' }}>
            🎯 Target Spot UUID
          </label>
          <input
            type="text"
            value={spotId}
            onChange={e => setSpotId(e.target.value)}
            onKeyDown={e => e.key === 'Enter' && runState === 'idle' && handleFire()}
            placeholder="e.g. 3c5a2f91-... (from queue/databank)"
            disabled={runState === 'running'}
            style={{
              width: '100%', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(244,63,94,0.3)',
              borderRadius: '6px', padding: '9px 12px', fontSize: '0.8rem', color: '#fff', outline: 'none',
              boxSizing: 'border-box', fontFamily: 'JetBrains Mono, monospace'
            }}
          />
          <p style={{ fontSize: '0.65rem', color: 'rgba(255,255,255,0.25)', margin: '6px 0 0 0' }}>
            Copy any spot ID from the Queue or Databank view. The record will be read-only.
          </p>
        </div>

        {/* Controls */}
        <div style={{ padding: '1rem 1.5rem', borderBottom: '1px solid rgba(255,255,255,0.06)', display: 'flex', gap: '8px', flexShrink: 0 }}>
          {runState !== 'running' ? (
            <button
              onClick={runState !== 'idle' ? handleReset : handleFire}
              style={{
                flex: 1, padding: '10px', borderRadius: '6px', fontSize: '0.82rem', fontWeight: 800, cursor: 'pointer',
                background: runState !== 'idle' ? 'rgba(255,255,255,0.07)' : '#f43f5e',
                color: runState !== 'idle' ? 'rgba(255,255,255,0.5)' : '#fff',
                border: runState !== 'idle' ? '1px solid rgba(255,255,255,0.1)' : 'none',
                boxShadow: runState === 'idle' ? '0 0 20px rgba(244,63,94,0.3)' : 'none',
                transition: 'all 0.2s'
              }}
            >
              {runState === 'idle' ? '🎯 FIRE' : '↺ RESET'}
            </button>
          ) : (
            <button
              onClick={handleCancel}
              style={{
                flex: 1, padding: '10px', borderRadius: '6px', fontSize: '0.82rem', fontWeight: 800, cursor: 'pointer',
                background: 'rgba(249,115,22,0.1)', color: '#f97316',
                border: '1px solid rgba(249,115,22,0.3)', transition: 'all 0.2s'
              }}
            >
              ⛔ ABORT
            </button>
          )}
        </div>

        {/* Spider Result Mini-Card */}
        {sniperResult?.spiderResult && (
          <div style={{ margin: '0 1.5rem 0 1.5rem', padding: '0.75rem', background: 'rgba(99,102,241,0.08)', border: '1px solid rgba(99,102,241,0.25)', borderRadius: '8px', flexShrink: 0 }}>
            <div style={{ fontSize: '0.65rem', fontWeight: 800, color: '#818cf8', marginBottom: '6px', textTransform: 'uppercase', letterSpacing: '0.06em' }}>⟦ Spider Links ⟧</div>
            {Object.entries(sniperResult.spiderResult.candidateLinks || {}).map(([k, v]) => (
              <div key={k} style={{ display: 'flex', gap: '6px', fontSize: '0.68rem', marginBottom: '3px' }}>
                <span style={{ color: '#818cf8', fontWeight: 700, minWidth: '60px', fontFamily: 'JetBrains Mono, monospace' }}>{k}</span>
                <span style={{ color: 'rgba(255,255,255,0.5)', wordBreak: 'break-all', fontFamily: 'JetBrains Mono, monospace' }}>{String(v)}</span>
              </div>
            ))}
          </div>
        )}

        {/* Quality Score Badge */}
        {sniperResult?.detectiveResult && (
          <div style={{ margin: '0.75rem 1.5rem 0 1.5rem', padding: '0.75rem', background: sniperResult.detectiveResult.passedQualityGate ? 'rgba(74,222,128,0.08)' : 'rgba(244,63,94,0.08)', border: `1px solid ${sniperResult.detectiveResult.passedQualityGate ? 'rgba(74,222,128,0.3)' : 'rgba(244,63,94,0.3)'}`, borderRadius: '8px', display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexShrink: 0 }}>
            <div>
              <div style={{ fontSize: '0.65rem', fontWeight: 800, color: 'rgba(255,255,255,0.4)', textTransform: 'uppercase', letterSpacing: '0.06em' }}>Quality Score</div>
              <div style={{ fontSize: '1.25rem', fontWeight: 900, color: sniperResult.detectiveResult.passedQualityGate ? '#4ade80' : '#f43f5e' }}>
                {sniperResult.detectiveResult.qualityScore}<span style={{ fontSize: '0.75rem', opacity: 0.5 }}>/17</span>
              </div>
            </div>
            <div style={{ textAlign: 'right' }}>
              <div style={{ fontSize: '0.65rem', fontWeight: 800, color: 'rgba(255,255,255,0.4)', textTransform: 'uppercase', letterSpacing: '0.06em' }}>Simulated Status</div>
              <div style={{ fontSize: '0.8rem', fontWeight: 800, color: sniperResult.detectiveResult.passedQualityGate ? '#4ade80' : '#f43f5e', fontFamily: 'JetBrains Mono, monospace' }}>
                {sniperResult.detectiveResult.simulatedStatus}
              </div>
            </div>
          </div>
        )}

        {/* Execution Log Console */}
        <div style={{ flex: 1, overflow: 'auto', borderTop: '1px solid rgba(255,255,255,0.06)', padding: '1rem', background: '#080a0e', fontFamily: 'JetBrains Mono, monospace', fontSize: '0.7rem', color: '#4ade80', marginTop: '0.75rem' }}>
          <div style={{ color: 'rgba(255,255,255,0.25)', marginBottom: '6px', fontSize: '0.65rem' }}>// Execution Log</div>
          {executionLog.length === 0 && (
            <div style={{ color: 'rgba(255,255,255,0.2)', fontStyle: 'italic' }}>Waiting for FIRE command...</div>
          )}
          {executionLog.map((l, i) => (
            <div key={i} style={{ lineHeight: 1.7, color: l.includes('ERROR') || l.includes('❌') ? '#f97316' : l.includes('✅') || l.includes('✓') ? '#4ade80' : l.includes('⚠️') ? '#fbbf24' : '#4ade80' }}>
              {l}
            </div>
          ))}
          <div ref={logEndRef} />
        </div>
      </div>

      {/* RIGHT: 68-Field Report Card */}
      <div style={{ width: '64%', display: 'flex', flexDirection: 'column', background: '#0B0D11', overflow: 'hidden' }}>
        <div style={{ padding: '1rem 1.5rem', borderBottom: '1px solid rgba(255,255,255,0.1)', background: '#0F1218', display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexShrink: 0 }}>
          <h3 style={{ fontSize: '0.82rem', fontWeight: 800, color: 'rgba(255,255,255,0.7)', textTransform: 'uppercase', letterSpacing: '0.06em', margin: 0 }}>
            68-Field Report Card
          </h3>
          <div style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
            {liveSpot && (
              <>
                <span style={{ fontSize: '0.7rem', color: '#4ade80' }}>
                  {Object.values(liveSpot).filter(v => v !== null && v !== undefined && v !== '' && v !== false).length} / {fields.length} populated
                </span>
              </>
            )}
            {runState === 'done' && (
              <div style={{ fontSize: '0.72rem', background: 'rgba(74,222,128,0.1)', color: '#4ade80', padding: '3px 10px', borderRadius: '9999px', border: '1px solid rgba(74,222,128,0.2)' }}>
                Simulation Complete
              </div>
            )}
          </div>
        </div>

        <div style={{ flex: 1, overflow: 'auto', padding: '1.5rem' }}>
          {runState === 'running' && (
            <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', marginBottom: '1.5rem', background: 'rgba(244,63,94,0.05)', padding: '1rem', borderRadius: '8px', border: '1px solid rgba(244,63,94,0.1)' }}>
              <div style={{ width: '1.25rem', height: '1.25rem', border: '2px solid #f43f5e', borderTopColor: 'transparent', borderRadius: '50%', animation: 'spin 1s linear infinite', flexShrink: 0 }} />
              <style>{`@keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }`}</style>
              <p style={{ fontSize: '0.82rem', fontFamily: 'JetBrains Mono, monospace', margin: 0, color: '#f43f5e' }}>
                Isolated pipeline running — watch the execution log ←
              </p>
            </div>
          )}

          <div style={{ background: '#0F1218', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '8px', overflow: 'hidden' }}>
            <div style={{ background: '#151921', padding: '10px 16px', borderBottom: '1px solid rgba(255,255,255,0.1)', display: 'flex', alignItems: 'center' }}>
              <h4 style={{ fontSize: '0.82rem', fontWeight: 800, color: '#60a5fa', textTransform: 'uppercase', margin: 0 }}>Omni-Field Validation Record</h4>
              <span style={{ marginLeft: 'auto', fontSize: '0.72rem', color: 'rgba(255,255,255,0.4)', fontWeight: 'bold' }}>Total Fields: {fields.length}</span>
            </div>

            {/* Column Headers */}
            <div style={{ display: 'flex', padding: '8px 16px', background: 'rgba(0,0,0,0.2)', borderBottom: '1px solid rgba(255,255,255,0.1)', fontSize: '0.65rem', fontWeight: 800, color: 'rgba(255,255,255,0.4)', textTransform: 'uppercase', letterSpacing: '0.06em' }}>
              <div style={{ width: '28%' }}>Schema Target</div>
              <div style={{ width: '13%' }}>Status</div>
              <div style={{ width: '39%' }}>Extracted Value</div>
              <div style={{ width: '20%' }}>Source</div>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column' }}>
              {fields
                .sort((a, b) => a.phase_id - b.phase_id || a.sort_order - b.sort_order)
                .map((f, i) => {
                  const rawVal = liveSpot ? liveSpot[f.field_name] : undefined;
                  const isMissing = rawVal === undefined || rawVal === null || rawVal === '' ||
                    (Array.isArray(rawVal) && rawVal.length === 0) ||
                    (typeof rawVal === 'object' && !Array.isArray(rawVal) && Object.keys(rawVal).length === 0);
                  const hasValue = !isMissing || rawVal === false;

                  const methodText = f.phase_id === 1 ? 'Google Places API' :
                    f.phase_id === 2 ? 'DOM Crawler / Spider' :
                    f.phase_id === 3 ? 'AI Detective (Ollama)' :
                    f.phase_id === 4 ? 'Photographer / Vision' :
                    'Pipeline';

                  const phaseColor = f.phase_id === 1 ? '#fbbf24' : f.phase_id === 2 ? '#818cf8' : f.phase_id === 3 ? '#38bdf8' : f.phase_id === 4 ? '#fb7185' : '#94a3b8';

                  return (
                    <div
                      key={f.id}
                      style={{ display: 'flex', padding: '10px 16px', borderBottom: i < fields.length - 1 ? '1px solid rgba(255,255,255,0.04)' : 'none', transition: 'background 0.15s', cursor: 'default' }}
                      onMouseEnter={e => e.currentTarget.style.background = '#151921'}
                      onMouseLeave={e => e.currentTarget.style.background = 'transparent'}
                    >
                      {/* Field Name */}
                      <div style={{ width: '28%', display: 'flex', flexDirection: 'column', paddingRight: '10px', justifyContent: 'center' }}>
                        <span style={{ fontSize: '0.82rem', fontWeight: 500, color: 'rgba(255,255,255,0.88)' }}>{f.display_label}</span>
                        <span style={{ fontSize: '0.6rem', fontFamily: 'JetBrains Mono, monospace', color: 'rgba(255,255,255,0.3)', marginTop: '2px' }}>{f.field_name}</span>
                      </div>

                      {/* Status Pill */}
                      <div style={{ width: '13%', display: 'flex', alignItems: 'center' }}>
                        {!liveSpot ? (
                          <div style={{ display: 'flex', alignItems: 'center', gap: '5px', background: 'rgba(255,255,255,0.04)', border: '1px solid rgba(255,255,255,0.08)', padding: '3px 7px', borderRadius: '4px' }}>
                            <span style={{ color: '#94a3b8', fontSize: '0.7rem' }}>⏳</span>
                            <span style={{ fontSize: '0.65rem', fontWeight: 800, color: '#94a3b8' }}>AWAIT</span>
                          </div>
                        ) : hasValue ? (
                          <div style={{ display: 'flex', alignItems: 'center', gap: '5px', background: 'rgba(74,222,128,0.1)', border: '1px solid rgba(74,222,128,0.25)', padding: '3px 7px', borderRadius: '4px' }}>
                            <span style={{ color: '#4ade80', fontSize: '0.7rem' }}>●</span>
                            <span style={{ fontSize: '0.65rem', fontWeight: 800, color: '#4ade80' }}>PASS</span>
                          </div>
                        ) : (
                          <div style={{ display: 'flex', alignItems: 'center', gap: '5px', background: 'rgba(244,63,94,0.08)', border: '1px solid rgba(244,63,94,0.2)', padding: '3px 7px', borderRadius: '4px' }}>
                            <span style={{ color: '#f43f5e', fontSize: '0.7rem' }}>●</span>
                            <span style={{ fontSize: '0.65rem', fontWeight: 800, color: '#f43f5e' }}>NULL</span>
                          </div>
                        )}
                      </div>

                      {/* Value */}
                      <div style={{ width: '39%', display: 'flex', alignItems: 'center', paddingRight: '10px' }}>
                        {!liveSpot ? (
                          <span style={{ fontSize: '0.7rem', color: 'rgba(255,255,255,0.15)', fontFamily: 'JetBrains Mono, monospace', fontStyle: 'italic' }}>Pending fire...</span>
                        ) : hasValue ? (
                          <pre style={{ fontSize: '0.7rem', color: '#e2e8f0', fontFamily: 'JetBrains Mono, monospace', background: 'rgba(255,255,255,0.02)', padding: '5px 8px', borderRadius: '4px', whiteSpace: 'pre-wrap', wordBreak: 'break-word', maxWidth: '100%', margin: 0, border: '1px solid rgba(255,255,255,0.04)' }}>
                            {typeof rawVal === 'object' ? JSON.stringify(rawVal, null, 2) : String(rawVal)}
                          </pre>
                        ) : (
                          <span style={{ fontSize: '0.7rem', color: 'rgba(255,255,255,0.2)', fontFamily: 'JetBrains Mono, monospace', fontStyle: 'italic' }}>null</span>
                        )}
                      </div>

                      {/* Source */}
                      <div style={{ width: '20%', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
                        <span style={{ fontSize: '0.65rem', fontWeight: 800, color: phaseColor }}>Phase {f.phase_id}</span>
                        <span style={{ fontSize: '0.6rem', color: 'rgba(255,255,255,0.35)', marginTop: '2px' }}>{methodText}</span>
                      </div>
                    </div>
                  );
                })}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
