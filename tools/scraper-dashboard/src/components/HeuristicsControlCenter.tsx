import React, { useState, useEffect, useRef } from 'react';

const API = 'http://localhost:5999';

export const HeuristicsControlCenter: React.FC = () => {
  const [ledger, setLedger] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [saveStatus, setSaveStatus] = useState<'idle' | 'saving' | 'saved' | 'error'>('idle');
  const [jsonText, setJsonText] = useState('');
  
  // Weights adjustments
  const [tempWeights, setTempWeights] = useState<Record<string, number>>({});
  
  // Real-time trace console
  const [traceLogs, setTraceLogs] = useState<string[]>([]);
  const consoleEndRef = useRef<HTMLDivElement>(null);
  const esRef = useRef<EventSource | null>(null);

  // Load heuristics from API
  const fetchHeuristics = async () => {
    try {
      setLoading(true);
      const res = await fetch(`${API}/api/heuristics`);
      const data = await res.json();
      if (res.ok && data.success) {
        setLedger(data.ledger);
        setJsonText(JSON.stringify(data.ledger, null, 2));
        setTempWeights(data.ledger.path_priorities || {});
        setError(null);
      } else {
        throw new Error(data.error || 'Failed to load ledger');
      }
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchHeuristics();
    
    // Connect to the CCTower EventSource log stream to extract heuristics events
    const es = new EventSource(`${API}/api/logs/stream`);
    esRef.current = es;

    es.onmessage = (event) => {
      try {
        const logData = JSON.parse(event.data);
        // We capture any messages from AI Detective (Phase 2) containing [HEURISTIC] or related details
        if (logData.message && (logData.message.includes('[HEURISTIC]') || logData.message.includes('🧬') || logData.message.includes('✂️') || logData.message.includes('Early-Check'))) {
          setTraceLogs(prev => {
            const next = [...prev, `[${new Date().toLocaleTimeString()}] ${logData.message}`];
            // Keep logs capped at 100 entries to prevent memory leak
            if (next.length > 100) next.shift();
            return next;
          });
          setTimeout(() => consoleEndRef.current?.scrollIntoView({ behavior: 'smooth' }), 50);
        }
      } catch (e) {}
    };

    return () => {
      if (esRef.current) {
        esRef.current.close();
      }
    };
  }, []);

  const handleWeightChange = (key: string, value: number) => {
    setTempWeights(prev => ({
      ...prev,
      [key]: value
    }));
  };

  const saveWeights = async () => {
    if (!ledger) return;
    setSaveStatus('saving');
    try {
      const updatedLedger = {
        ...ledger,
        path_priorities: tempWeights
      };
      const res = await fetch(`${API}/api/heuristics`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedLedger)
      });
      const data = await res.json();
      if (res.ok && data.success) {
        setLedger(updatedLedger);
        setJsonText(JSON.stringify(updatedLedger, null, 2));
        setSaveStatus('saved');
        setTimeout(() => setSaveStatus('idle'), 2000);
      } else {
        throw new Error(data.error || 'Failed to save weights');
      }
    } catch (err: any) {
      setSaveStatus('error');
      setTimeout(() => setSaveStatus('idle'), 3000);
    }
  };

  const handleJsonSave = async () => {
    setSaveStatus('saving');
    try {
      const parsed = JSON.parse(jsonText);
      const res = await fetch(`${API}/api/heuristics`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(parsed)
      });
      const data = await res.json();
      if (res.ok && data.success) {
        setLedger(parsed);
        setTempWeights(parsed.path_priorities || {});
        setSaveStatus('saved');
        setTimeout(() => setSaveStatus('idle'), 2000);
      } else {
        throw new Error(data.error || 'Failed to save heuristics config');
      }
    } catch (err: any) {
      setSaveStatus('error');
      alert(`Invalid JSON format: ${err.message}`);
      setTimeout(() => setSaveStatus('idle'), 3000);
    }
  };

  const clearConsole = () => {
    setTraceLogs([]);
  };

  if (loading) {
    return (
      <div style={{ padding: '3rem', color: '#fff', fontSize: '1rem', fontFamily: 'Outfit, sans-serif' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          <div style={{ width: '1.5rem', height: '1.5rem', border: '2px solid #38bdf8', borderTopColor: 'transparent', borderRadius: '50%', animation: 'spin 1s linear infinite' }} />
          <span>Synchronizing with Heuristics Ledger...</span>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div style={{ padding: '3rem', color: '#f43f5e', fontFamily: 'Outfit, sans-serif' }}>
        <h3 style={{ margin: '0 0 10px 0' }}>❌ Connection Failure</h3>
        <p style={{ margin: 0, fontSize: '0.85rem', color: 'rgba(255,255,255,0.6)' }}>{error}</p>
        <button onClick={fetchHeuristics} style={{ marginTop: '1.5rem', padding: '8px 16px', background: '#38bdf8', border: 'none', borderRadius: '4px', color: '#000', fontWeight: 'bold', cursor: 'pointer' }}>
          Retry Synchronization
        </button>
      </div>
    );
  }

  return (
    <div style={{ display: 'flex', height: '100%', width: '100%', background: '#0B0D11', color: '#e2e8f0', overflow: 'hidden', fontFamily: 'Outfit, sans-serif' }}>
      
      {/* ── LEFT PANEL: WEIGHTS & SELECTORS ── */}
      <div style={{ width: '50%', display: 'flex', flexDirection: 'column', borderRight: '1px solid rgba(255,255,255,0.1)', background: '#0F1218', overflow: 'auto' }}>
        
        {/* Header */}
        <div style={{ padding: '1.25rem 1.5rem', borderBottom: '1px solid rgba(255,255,255,0.08)', flexShrink: 0 }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '6px' }}>
            <span style={{ fontSize: '1.25rem' }}>🧠</span>
            <h2 style={{ fontSize: '1.1rem', fontWeight: 'bold', color: '#fff', margin: 0 }}>Heuristics Control Center</h2>
            <span style={{ marginLeft: 'auto', fontSize: '0.62rem', fontWeight: 800, color: '#38bdf8', background: 'rgba(56,189,248,0.08)', border: '1px solid rgba(56,189,248,0.25)', padding: '2px 8px', borderRadius: '4px' }}>
              SELF-EVOLVING ACTIVE
            </span>
          </div>
          <p style={{ fontSize: '0.72rem', color: 'rgba(255,255,255,0.35)', margin: 0, lineHeight: 1.5 }}>
            Optimize crawl queues dynamically by boosting path priorities and managing CMS structural templates. Updates apply instantly without container rebuilds.
          </p>
        </div>

        {/* Path Priorities Sliders */}
        <div style={{ padding: '1.5rem' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '1rem' }}>
            <h3 style={{ fontSize: '0.82rem', fontWeight: 800, color: '#60a5fa', textTransform: 'uppercase', letterSpacing: '0.06em', margin: 0 }}>
              Sitemap Path Weights
            </h3>
            <button 
              onClick={saveWeights}
              disabled={saveStatus === 'saving'}
              style={{
                fontSize: '0.72rem',
                fontWeight: 'bold',
                padding: '4px 12px',
                borderRadius: '4px',
                border: 'none',
                cursor: 'pointer',
                background: saveStatus === 'saved' ? '#4ade80' : '#38bdf8',
                color: '#000',
                transition: 'all 0.2s'
              }}
            >
              {saveStatus === 'saving' ? 'Saving...' : saveStatus === 'saved' ? 'Saved! ✓' : 'Save Weights'}
            </button>
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '14px', background: 'rgba(255,255,255,0.02)', padding: '1.25rem', borderRadius: '8px', border: '1px solid rgba(255,255,255,0.05)' }}>
            {Object.entries(tempWeights)
              .sort((a, b) => b[1] - a[1])
              .map(([key, val]) => (
                <div key={key} style={{ display: 'flex', flexDirection: 'column', gap: '5px' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.75rem', fontWeight: 600 }}>
                    <span style={{ color: 'rgba(255,255,255,0.85)', fontFamily: 'monospace' }}>/{key}*</span>
                    <span style={{ color: '#38bdf8', fontWeight: 'bold' }}>{(val * 100).toFixed(0)}%</span>
                  </div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                    <input 
                      type="range" 
                      min="0" 
                      max="1.0" 
                      step="0.01" 
                      value={val}
                      onChange={e => handleWeightChange(key, parseFloat(e.target.value))}
                      style={{ flex: 1, accentColor: '#38bdf8', cursor: 'pointer', height: '4px' }}
                    />
                  </div>
                </div>
              ))}
          </div>
        </div>

        {/* Selectors Registry (CMS Mappings) */}
        <div style={{ padding: '0 1.5rem 1.5rem 1.5rem' }}>
          <h3 style={{ fontSize: '0.82rem', fontWeight: 800, color: '#fb7185', textTransform: 'uppercase', letterSpacing: '0.06em', margin: '0 0 1rem 0' }}>
            CMS Selectors Registry
          </h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
            {Object.entries(ledger?.selectors_registry || {}).map(([cms, selectors]: any) => (
              <div key={cms} style={{ background: 'rgba(255,255,255,0.02)', border: '1px solid rgba(255,255,255,0.06)', borderRadius: '8px', overflow: 'hidden' }}>
                <div style={{ background: 'rgba(255,255,255,0.04)', padding: '8px 12px', fontSize: '0.72rem', fontWeight: 800, textTransform: 'uppercase', color: '#e2e8f0', borderBottom: '1px solid rgba(255,255,255,0.06)' }}>
                  🌐 {cms} engine
                </div>
                <div style={{ padding: '10px 12px', display: 'flex', flexDirection: 'column', gap: '8px' }}>
                  {Object.entries(selectors).map(([field, selector]: any) => (
                    <div key={field} style={{ display: 'flex', fontSize: '0.72rem', alignItems: 'center' }}>
                      <span style={{ color: 'rgba(255,255,255,0.4)', minWidth: '95px', flexShrink: 0 }}>{field}</span>
                      <pre style={{ margin: 0, fontFamily: 'monospace', color: '#a7f3d0', background: 'rgba(255,255,255,0.03)', padding: '3px 8px', borderRadius: '4px', border: '1px solid rgba(255,255,255,0.02)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', flex: 1 }}>{selector}</pre>
                    </div>
                  ))}
                </div>
              </div>
            ))}
          </div>
        </div>

      </div>

      {/* ── RIGHT PANEL: RAW LEDGER EDITOR & REAL-TIME NEURAL DECISION TRACE ── */}
      <div style={{ width: '50%', display: 'flex', flexDirection: 'column', background: '#0B0D11', overflow: 'hidden' }}>
        
        {/* Real-time Neural Decision Trace (SSE Stream) */}
        <div style={{ height: '55%', display: 'flex', flexDirection: 'column', borderBottom: '1px solid rgba(255,255,255,0.1)' }}>
          <div style={{ padding: '1rem 1.5rem', background: '#0F1218', borderBottom: '1px solid rgba(255,255,255,0.08)', display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexShrink: 0 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <span style={{ fontSize: '0.9rem' }}>🧬</span>
              <h3 style={{ fontSize: '0.82rem', fontWeight: 800, color: '#38bdf8', textTransform: 'uppercase', letterSpacing: '0.06em', margin: 0 }}>
                Neural Decision console
              </h3>
            </div>
            <button 
              onClick={clearConsole}
              style={{ fontSize: '0.65rem', background: 'rgba(255,255,255,0.05)', color: 'rgba(255,255,255,0.5)', border: '1px solid rgba(255,255,255,0.1)', padding: '3px 8px', borderRadius: '4px', cursor: 'pointer' }}
            >
              Clear Log
            </button>
          </div>
          
          <div style={{ flex: 1, overflow: 'auto', padding: '1rem', background: '#070a0e', fontFamily: 'JetBrains Mono, monospace', fontSize: '0.7rem', color: '#38bdf8' }}>
            <div style={{ color: 'rgba(255,255,255,0.25)', marginBottom: '8px', fontSize: '0.65rem' }}>// Real-time trace showing URL priority boosts, dynamic HTML segmentation, and early exit triggers</div>
            {traceLogs.length === 0 && (
              <div style={{ color: 'rgba(255,255,255,0.2)', fontStyle: 'italic', padding: '10px 0' }}>
                Awaiting active scraping runs. Trigger a crawl to stream sitemap priority and semantic slicing metrics here...
              </div>
            )}
            {traceLogs.map((log, idx) => {
              let color = '#38bdf8';
              if (log.includes('Triggered') || log.includes('successful')) color = '#4ade80';
              else if (log.includes('Warning') || log.includes('⚠️')) color = '#fbbf24';
              else if (log.includes('❌') || log.includes('Failed')) color = '#f97316';
              return (
                <div key={idx} style={{ lineHeight: 1.7, color, whiteSpace: 'pre-wrap', wordBreak: 'break-word', marginBottom: '4px' }}>
                  {log}
                </div>
              );
            })}
            <div ref={consoleEndRef} />
          </div>
        </div>

        {/* Global Configuration Ledger Editor */}
        <div style={{ height: '45%', display: 'flex', flexDirection: 'column', background: '#0F1218' }}>
          <div style={{ padding: '0.8rem 1.5rem', borderBottom: '1px solid rgba(255,255,255,0.06)', display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexShrink: 0 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <span style={{ fontSize: '0.9rem' }}>⚙️</span>
              <h3 style={{ fontSize: '0.82rem', fontWeight: 800, color: 'rgba(255,255,255,0.7)', textTransform: 'uppercase', letterSpacing: '0.06em', margin: 0 }}>
                Global Ledger Configuration
              </h3>
            </div>
            <button 
              onClick={handleJsonSave}
              disabled={saveStatus === 'saving'}
              style={{
                fontSize: '0.68rem',
                fontWeight: 'bold',
                padding: '4px 12px',
                borderRadius: '4px',
                border: 'none',
                cursor: 'pointer',
                background: '#4ade80',
                color: '#000',
                transition: 'all 0.2s'
              }}
            >
              {saveStatus === 'saving' ? 'Saving...' : 'Apply Config'}
            </button>
          </div>
          
          <div style={{ flex: 1, padding: '0.75rem 1.5rem 1.5rem 1.5rem', display: 'flex', flexDirection: 'column' }}>
            <textarea
              value={jsonText}
              onChange={e => setJsonText(e.target.value)}
              spellCheck={false}
              style={{
                flex: 1,
                width: '100%',
                background: '#070a0e',
                border: '1px solid rgba(255,255,255,0.1)',
                borderRadius: '6px',
                padding: '10px',
                color: '#e2e8f0',
                fontFamily: 'JetBrains Mono, monospace',
                fontSize: '0.72rem',
                lineHeight: 1.5,
                outline: 'none',
                resize: 'none',
                boxSizing: 'border-box'
              }}
            />
          </div>
        </div>

      </div>

    </div>
  );
};
