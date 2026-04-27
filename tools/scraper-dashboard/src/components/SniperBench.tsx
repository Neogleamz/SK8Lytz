import React, { useState, useEffect, useRef } from 'react';
import { useFieldRegistry } from '../hooks/useFieldRegistry';

export const SniperBench: React.FC = () => {
  const { fields, loading: fieldsLoading } = useFieldRegistry();
  const [url, setUrl] = useState('');
  const [spotName, setSpotName] = useState('');
  const [spotCity, setSpotCity] = useState('');
  const [loading, setLoading] = useState(false);
  const [results, setResults] = useState<any>(null);
  const [executionLog, setExecutionLog] = useState<string[]>([]);
  const [cleanText, setCleanText] = useState<string>('');
  const pollRef = useRef<ReturnType<typeof setInterval> | null>(null);

  const log = (msg: string) => setExecutionLog(prev => [...prev, `[${new Date().toLocaleTimeString()}] ${msg}`]);

  const handleFire = async () => {
    if (!url) return alert('URL is required for Sniper test');
    setLoading(true);
    setResults(null);
    setExecutionLog([]);
    setCleanText('');

    log(`Initializing End-to-End Pipeline Tracer for ${url}`);

    try {
      // 1. Seed the record with priority
      const seedRes = await fetch('http://localhost:5999/api/sniper/seed', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ url, spot_name: spotName, spot_city: spotCity })
      });
      
      const seedData = await seedRes.json();
      if (!seedData.success) throw new Error(seedData.error || 'Failed to seed record');
      
      const spotId = seedData.spot_id;
      log(`✅ Seeded to DB [ID: ${spotId}]. Waiting for Operator...`);

      // 2. Poll the pipeline
      let currentStatus = 'SEEDED';
      let retries = 0;

      pollRef.current = setInterval(async () => {
        try {
          const pollRes = await fetch(`http://localhost:5999/api/sniper/poll/${spotId}`);
          const pollData = await pollRes.json();
          
          if (pollData.success && pollData.spot) {
            const spot = pollData.spot;
            
            if (spot.verification_status !== currentStatus) {
              currentStatus = spot.verification_status;
              
              if (currentStatus === 'ENRICHED') log('🕷️ Operator finished (Spidering complete). Waiting for Indexer (Detective)...');
              else if (currentStatus === 'MEDIA_READY') log('🕵️ Indexer finished (AI Extraction complete). Waiting for Photographer...');
              else if (currentStatus === 'REVIEW_PENDING') log('📸 Photographer finished. Pipeline complete! Gathering results...');
              else if (currentStatus === 'PUBLISHED') log('✅ Record automatically published!');
              else if (currentStatus === 'REJECTED') log('❌ Record REJECTED by Gatekeeper.');
              else if (currentStatus === 'STALLED') log('⚠️ Record STALLED in pipeline.');
              else log(`Transitioned to: ${currentStatus}`);
            }

            // Check if we hit a terminal state
            if (['REVIEW_PENDING', 'PUBLISHED', 'REJECTED', 'STALLED'].includes(currentStatus)) {
              clearInterval(pollRef.current!); pollRef.current = null;
              
              if (spot.raw_ai_payload) {
                log(`Successfully retrieved AI Payload from DB.`);
                setResults(spot.raw_ai_payload);
              } else {
                log(`Pipeline finished, but no AI payload was generated.`);
              }
              
              setCleanText(JSON.stringify(spot.candidate_links || {}, null, 2));
              setLoading(false);
            }
          }
        } catch (e) {
          retries++;
          if (retries > 30) {
            clearInterval(pollRef.current!); pollRef.current = null;
            log('Critical Timeout: Pipeline polling failed.');
            setLoading(false);
          }
        }
      }, 2000);

    } catch (err: any) {
      log(`Critical Failure: ${err.message}`);
      setLoading(false);
    }
  };

  const handleCancel = () => {
    if (pollRef.current) { clearInterval(pollRef.current); pollRef.current = null; }
    log('⛔ Cancelled by user.');
    setLoading(false);
  };

  if (fieldsLoading) return <div className="p-8 text-white">Loading Field Registry...</div>;

  const getPhaseName = (id: number) => {
    const map: Record<number, string> = {
      1: 'Phase 1: Scout (Google Places)',
      2: 'Phase 2: Spider (Social & URLs)',
      3: 'Phase 3: Detective (AI Extraction)',
      4: 'Phase 4: Photographer (Media)',
      5: 'Phase 5: Publisher (Final)'
    };
    return map[id] || `Phase ${id}`;
  };

  return (
    <div style={{ display: 'flex', height: '100%', width: '100%', background: '#0B0D11', color: '#e2e8f0', overflow: 'hidden', fontFamily: 'Outfit, sans-serif' }}>
      {/* LEFT: Controls & Logs */}
      <div style={{ width: '33.33%', display: 'flex', flexDirection: 'column', borderRight: '1px solid rgba(255,255,255,0.1)', background: '#0F1218' }}>
        <div style={{ padding: '1.5rem', flexShrink: 0 }}>
          <h2 style={{ fontSize: '1.25rem', fontWeight: 'bold', color: '#fff', marginBottom: '0.5rem', display: 'flex', alignItems: 'center', gap: '8px', margin: '0 0 0.5rem 0' }}>
            <span style={{ color: '#f43f5e' }}>🎯</span> Sniper Test Bench
          </h2>
          <p style={{ fontSize: '0.75rem', color: 'rgba(255,255,255,0.4)', margin: '0 0 1.5rem 0' }}>
            Bypass the bulk queues. Run a synchronous, single-record test through the Brute Force AI Detective to see exactly what passes and fails.
          </p>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 'bold', color: 'rgba(255,255,255,0.4)', marginBottom: '4px' }}>Target URL (Required)</label>
              <input 
                type="text" 
                value={url}
                onChange={e => setUrl(e.target.value)}
                placeholder="https://sk8away.net/hours-pricing/"
                style={{ width: '100%', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '4px', padding: '8px 12px', fontSize: '0.875rem', color: '#fff', outline: 'none' }}
              />
            </div>
            <div style={{ display: 'flex', gap: '1rem' }}>
              <div style={{ flex: 1 }}>
                <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 'bold', color: 'rgba(255,255,255,0.4)', marginBottom: '4px' }}>Spot Name (Optional)</label>
                <input 
                  type="text" 
                  value={spotName}
                  onChange={e => setSpotName(e.target.value)}
                  placeholder="Sk8away"
                  style={{ width: '100%', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '4px', padding: '8px 12px', fontSize: '0.875rem', color: '#fff', outline: 'none' }}
                />
              </div>
              <div style={{ flex: 1 }}>
                <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 'bold', color: 'rgba(255,255,255,0.4)', marginBottom: '4px' }}>City (Optional)</label>
                <input 
                  type="text" 
                  value={spotCity}
                  onChange={e => setSpotCity(e.target.value)}
                  placeholder="Topeka"
                  style={{ width: '100%', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '4px', padding: '8px 12px', fontSize: '0.875rem', color: '#fff', outline: 'none' }}
                />
              </div>
            </div>

            <div style={{ display: 'flex', gap: '0.5rem', marginTop: '1rem' }}>
              <button
                onClick={handleFire}
                disabled={loading}
                style={{
                  flex: 1, padding: '12px', borderRadius: '4px', fontSize: '0.875rem', fontWeight: 'bold', cursor: loading ? 'not-allowed' : 'pointer',
                  background: loading ? 'rgba(255,255,255,0.07)' : '#f43f5e',
                  color: loading ? 'rgba(255,255,255,0.3)' : '#fff',
                  border: 'none',
                  boxShadow: loading ? 'none' : '0 0 15px rgba(244,63,94,0.3)',
                  transition: 'all 0.2s'
                }}
              >
                {loading ? '⏳ Running...' : '🎯 Fire Sniper'}
              </button>
              {loading && (
                <button
                  onClick={handleCancel}
                  style={{
                    padding: '12px 16px', borderRadius: '4px', fontSize: '0.875rem', fontWeight: 'bold', cursor: 'pointer',
                    background: 'rgba(255,100,50,0.15)',
                    color: '#f97316',
                    border: '1px solid rgba(255,100,50,0.3)',
                    transition: 'all 0.2s'
                  }}
                >
                  ⛔ Cancel
                </button>
              )}
            </div>
          </div>
        </div>

        {/* Console / Text Dump */}
        <div style={{ flex: 1, overflow: 'auto', borderTop: '1px solid rgba(255,255,255,0.1)', padding: '1rem', background: '#0a0c10', fontFamily: 'JetBrains Mono, monospace', fontSize: '0.75rem', color: '#4ade80' }}>
          <div style={{ marginBottom: '1rem' }}>
            <h3 style={{ color: 'rgba(255,255,255,0.4)', marginBottom: '0.5rem', margin: 0 }}>// Execution Log</h3>
            {executionLog.map((l, i) => <div key={i}>{l}</div>)}
          </div>
          
          {cleanText && (
            <div style={{ marginTop: '1.5rem', borderTop: '1px solid rgba(255,255,255,0.1)', paddingTop: '1rem' }}>
              <h3 style={{ color: 'rgba(255,255,255,0.4)', marginBottom: '0.5rem', margin: 0 }}>// Raw Extracted Text (DOM + OCR)</h3>
              <div style={{ whiteSpace: 'pre-wrap', wordBreak: 'break-word', opacity: 0.8, lineHeight: 1.5 }}>
                {cleanText}
              </div>
            </div>
          )}
        </div>
      </div>

      {/* RIGHT: 68-Field Report Card */}
      <div style={{ width: '66.66%', display: 'flex', flexDirection: 'column', background: '#0B0D11', overflow: 'hidden' }}>
        <div style={{ padding: '1rem', borderBottom: '1px solid rgba(255,255,255,0.1)', background: '#0F1218', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <h3 style={{ fontSize: '0.875rem', fontWeight: 'bold', color: 'rgba(255,255,255,0.7)', textTransform: 'uppercase', letterSpacing: '0.05em', margin: 0 }}>68-Field Report Card</h3>
          {results && (
            <div style={{ fontSize: '0.75rem', background: 'rgba(74, 222, 128, 0.1)', color: '#4ade80', padding: '4px 12px', borderRadius: '9999px', border: '1px solid rgba(74, 222, 128, 0.2)' }}>
              Test Completed
            </div>
          )}
        </div>

        <div style={{ flex: 1, overflow: 'auto', padding: '1.5rem' }}>
          {loading && (
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#f43f5e', gap: '1rem', marginBottom: '1.5rem', background: 'rgba(244,63,94,0.05)', padding: '1rem', borderRadius: '8px', border: '1px solid rgba(244,63,94,0.1)' }}>
              <div style={{ width: '1.5rem', height: '1.5rem', border: '2px solid #f43f5e', borderTopColor: 'transparent', borderRadius: '50%', animation: 'spin 1s linear infinite' }}></div>
              <style>{`@keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }`}</style>
              <p style={{ fontSize: '0.875rem', fontFamily: 'JetBrains Mono, monospace', margin: 0 }}>Running Brute Force Extractor...</p>
            </div>
          )}

          <div style={{ background: '#0F1218', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '8px', overflow: 'hidden' }}>
            <div style={{ background: '#151921', padding: '12px 16px', borderBottom: '1px solid rgba(255,255,255,0.1)', display: 'flex', alignItems: 'center' }}>
              <h4 style={{ fontSize: '0.875rem', fontWeight: 'bold', color: '#60a5fa', textTransform: 'uppercase', margin: 0 }}>Omni-Field Validation Record</h4>
              <span style={{ marginLeft: 'auto', fontSize: '0.75rem', color: 'rgba(255,255,255,0.5)', fontWeight: 'bold' }}>Total Fields Evaluated: {fields.length}</span>
            </div>
            <div style={{ display: 'flex', flexDirection: 'column' }}>
              <div style={{ display: 'flex', padding: '8px 16px', background: 'rgba(0,0,0,0.2)', borderBottom: '1px solid rgba(255,255,255,0.1)', fontSize: '0.7rem', fontWeight: 'bold', color: 'rgba(255,255,255,0.5)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>
                <div style={{ width: '25%' }}>Schema Target</div>
                <div style={{ width: '15%' }}>Status</div>
                <div style={{ width: '40%' }}>Extracted Value</div>
                <div style={{ width: '20%' }}>Pipeline Source / Method</div>
              </div>
              {fields.sort((a,b) => a.phase_id - b.phase_id || a.sort_order - b.sort_order).map((f, i) => {
                
                let hasValue = false;
                let value: any = null;
                
                if (results) {
                  value = results[f.field_name];
                  const isMissing = value === undefined || value === null || value === '' || (Array.isArray(value) && value.length === 0) || (typeof value === 'object' && Object.keys(value).length === 0);
                  hasValue = !isMissing || value === false;
                }

                const methodText = f.phase_id === 1 ? 'Google Places API (Direct)' : 
                                    f.phase_id === 2 ? 'DOM Crawler (Homepage Links)' :
                                    f.phase_id === 3 ? 'AI Detective (DOM + OCR Infer)' :
                                    f.phase_id === 4 ? 'Vision API (Gallery Scraping)' :
                                    'Pipeline Aggregation';

                return (
                  <div key={f.id} style={{ display: 'flex', padding: '12px 16px', borderBottom: i < fields.length - 1 ? '1px solid rgba(255,255,255,0.05)' : 'none', transition: 'background 0.2s', cursor: 'default' }} onMouseEnter={e => e.currentTarget.style.background = '#151921'} onMouseLeave={e => e.currentTarget.style.background = 'transparent'}>
                    <div style={{ width: '25%', display: 'flex', flexDirection: 'column', paddingRight: '10px' }}>
                      <span style={{ fontSize: '0.875rem', fontWeight: 500, color: 'rgba(255,255,255,0.9)' }}>{f.display_label}</span>
                      <span style={{ fontSize: '0.625rem', fontFamily: 'JetBrains Mono, monospace', color: 'rgba(255,255,255,0.4)', marginTop: '2px' }}>{f.field_name}</span>
                    </div>
                    <div style={{ width: '15%', display: 'flex', alignItems: 'center' }}>
                      {!results ? (
                        <div style={{ display: 'flex', alignItems: 'center', gap: '6px', background: 'rgba(255, 255, 255, 0.05)', border: '1px solid rgba(255,255,255,0.1)', padding: '4px 8px', borderRadius: '4px' }}>
                          <span style={{ color: '#94a3b8', fontSize: '0.75rem' }}>⏳</span>
                          <span style={{ fontSize: '0.75rem', fontWeight: 'bold', color: '#94a3b8' }}>AWAITING</span>
                        </div>
                      ) : hasValue ? (
                        <div style={{ display: 'flex', alignItems: 'center', gap: '6px', background: 'rgba(74, 222, 128, 0.1)', border: '1px solid rgba(74,222,128,0.3)', padding: '4px 8px', borderRadius: '4px' }}>
                          <span style={{ color: '#4ade80', fontSize: '0.75rem' }}>🟢</span>
                          <span style={{ fontSize: '0.75rem', fontWeight: 'bold', color: '#4ade80' }}>PASS</span>
                        </div>
                      ) : (
                        <div style={{ display: 'flex', alignItems: 'center', gap: '6px', background: 'rgba(244, 63, 94, 0.1)', border: '1px solid rgba(244,63,94,0.3)', padding: '4px 8px', borderRadius: '4px' }}>
                          <span style={{ color: '#f43f5e', fontSize: '0.75rem' }}>🔴</span>
                          <span style={{ fontSize: '0.75rem', fontWeight: 'bold', color: '#f43f5e' }}>FAIL</span>
                        </div>
                      )}
                    </div>
                    <div style={{ width: '40%', display: 'flex', alignItems: 'center', paddingRight: '10px' }}>
                      {!results ? (
                         <span style={{ fontSize: '0.75rem', color: 'rgba(255,255,255,0.2)', fontFamily: 'JetBrains Mono, monospace', fontStyle: 'italic' }}>Pending Sniper Fire...</span>
                      ) : hasValue ? (
                        <pre style={{ fontSize: '0.75rem', color: '#e2e8f0', fontFamily: 'JetBrains Mono, monospace', background: 'rgba(255,255,255,0.03)', padding: '6px 10px', borderRadius: '6px', whiteSpace: 'pre-wrap', wordBreak: 'break-word', maxWidth: '100%', margin: 0, border: '1px solid rgba(255,255,255,0.05)' }}>
                          {typeof value === 'object' ? JSON.stringify(value, null, 2) : String(value)}
                        </pre>
                      ) : (
                        <span style={{ fontSize: '0.75rem', color: 'rgba(255,255,255,0.3)', fontFamily: 'JetBrains Mono, monospace', fontStyle: 'italic' }}>NULL</span>
                      )}
                    </div>
                    <div style={{ width: '20%', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
                      <span style={{ fontSize: '0.7rem', fontWeight: 'bold', color: 'rgba(255,255,255,0.6)' }}>Phase {f.phase_id}</span>
                      <span style={{ fontSize: '0.65rem', color: 'rgba(255,255,255,0.4)', marginTop: '2px' }}>{methodText}</span>
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

