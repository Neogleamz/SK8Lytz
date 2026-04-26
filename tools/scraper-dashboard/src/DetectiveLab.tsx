import { useState } from 'react';

const API_BASE = 'http://localhost:5999';

export default function DetectiveLab({
  aiSystemPrompt,
  setAiSystemPrompt,
  aiTargetVectors,
  setAiTargetVectors,
  aiExclusionKeywords,
  setAiExclusionKeywords,
  updateGlobalStrategy
}: any) {
  const [sandboxUrl, setSandboxUrl] = useState('');
  const [sandboxResult, setSandboxResult] = useState<any>(null);
  const [isSandboxRunning, setIsSandboxRunning] = useState(false);
  const [newKeyword, setNewKeyword] = useState('');
  const [newVectorKey, setNewVectorKey] = useState('');
  const [newVectorType, setNewVectorType] = useState('string');
  const [newVectorInstruction, setNewVectorInstruction] = useState('');

  const runSandbox = async () => {
    if (!sandboxUrl) return alert('Enter a URL');
    setIsSandboxRunning(true);
    setSandboxResult(null);
    try {
      const res = await fetch(`${API_BASE}/api/sandbox`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          url: sandboxUrl,
          ai_system_prompt: aiSystemPrompt,
          ai_target_vectors: aiTargetVectors
        })
      });
      const data = await res.json();
      setSandboxResult(data);
    } catch (err: any) {
      setSandboxResult({ error: err.message });
    }
    setIsSandboxRunning(false);
  };

  const addKeyword = () => {
    if (!newKeyword.trim()) return;
    const next = [...aiExclusionKeywords, newKeyword.trim().toLowerCase()];
    setAiExclusionKeywords(next);
    updateGlobalStrategy('ai_exclusion_keywords', next);
    setNewKeyword('');
  };

  const removeKeyword = (kw: string) => {
    const next = aiExclusionKeywords.filter((k: string) => k !== kw);
    setAiExclusionKeywords(next);
    updateGlobalStrategy('ai_exclusion_keywords', next);
  };

  const addVector = () => {
    if (!newVectorKey.trim()) return;
    const next = [...aiTargetVectors, {
      key: newVectorKey.trim(),
      type: newVectorType,
      instruction: newVectorInstruction.trim()
    }];
    setAiTargetVectors(next);
    updateGlobalStrategy('ai_target_vectors', next);
    setNewVectorKey('');
    setNewVectorInstruction('');
  };

  const removeVector = (idx: number) => {
    const next = [...aiTargetVectors];
    next.splice(idx, 1);
    setAiTargetVectors(next);
    updateGlobalStrategy('ai_target_vectors', next);
  };

  const savePrompt = () => {
    updateGlobalStrategy('ai_system_prompt', aiSystemPrompt);
    alert('System prompt saved.');
  };

  return (
    <div style={{ padding: '20px', display: 'flex', gap: '20px', alignItems: 'flex-start' }}>
      
      {/* Left Column: Config */}
      <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: '20px' }}>
        
        <div className="panel" style={{ background: 'rgba(20,20,30,0.95)' }}>
          <h2 className="panel-header" style={{ color: '#00d2ff' }}>Engine Room (Ollama)</h2>
          <div style={{ padding: '15px' }}>
             <label style={{ display: 'block', fontSize: '0.8rem', color: '#888', marginBottom: '5px' }}>System Prompt (Model Instructions)</label>
             <textarea 
               style={{ width: '100%', height: '120px', background: 'rgba(0,0,0,0.3)', border: '1px solid #333', color: '#fff', padding: '10px', fontSize: '0.8rem' }}
               value={aiSystemPrompt}
               onChange={e => setAiSystemPrompt(e.target.value)}
             />
             <button onClick={savePrompt} className="btn-primary" style={{ marginTop: '10px', padding: '5px 15px', fontSize: '0.8rem', background: '#00d2ff', color: '#000' }}>Save Prompt</button>
          </div>
        </div>

        <div className="panel" style={{ background: 'rgba(20,20,30,0.95)' }}>
          <h2 className="panel-header" style={{ color: '#ff5a00' }}>Toxicity Bouncer (Exclusions)</h2>
          <div style={{ padding: '15px' }}>
             <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap', marginBottom: '15px' }}>
                {aiExclusionKeywords.map((kw: string) => (
                  <span key={kw} style={{ background: 'rgba(255,90,0,0.2)', border: '1px solid #ff5a00', color: '#ff5a00', padding: '4px 10px', borderRadius: '15px', fontSize: '0.75rem', display: 'flex', alignItems: 'center', gap: '5px' }}>
                    {kw} <button onClick={() => removeKeyword(kw)} style={{ background: 'transparent', border: 'none', color: '#ff5a00', cursor: 'pointer', padding: 0 }}>&times;</button>
                  </span>
                ))}
             </div>
             <div style={{ display: 'flex', gap: '10px' }}>
               <input className="table-input" style={{ flex: 1 }} value={newKeyword} onChange={e => setNewKeyword(e.target.value)} placeholder="e.g. bicycle" />
               <button onClick={addKeyword} className="btn-primary" style={{ background: '#ff5a00', color: '#fff' }}>Add Keyword</button>
             </div>
          </div>
        </div>

        <div className="panel" style={{ background: 'rgba(20,20,30,0.95)' }}>
          <h2 className="panel-header" style={{ color: '#8a2be2' }}>Schema Builder (Target Vectors)</h2>
          <div style={{ padding: '15px' }}>
             <table style={{ width: '100%', marginBottom: '15px', fontSize: '0.8rem' }}>
               <thead>
                 <tr><th style={{textAlign:'left', color:'#888', paddingBottom:'5px'}}>Key</th><th style={{textAlign:'left', color:'#888'}}>Type</th><th style={{textAlign:'left', color:'#888'}}>Instruction</th><th style={{textAlign:'right'}}></th></tr>
               </thead>
               <tbody>
                 {aiTargetVectors.map((vec: any, idx: number) => (
                   <tr key={idx} style={{ borderBottom: '1px solid rgba(255,255,255,0.05)' }}>
                     <td style={{ padding: '8px 0', color: '#8a2be2', fontWeight: 800 }}>{vec.key}</td>
                     <td style={{ color: 'var(--success)' }}>{vec.type}</td>
                     <td style={{ color: 'rgba(255,255,255,0.7)' }}>{vec.instruction}</td>
                     <td style={{ textAlign: 'right' }}><button onClick={() => removeVector(idx)} className="btn-icon btn-delete" style={{ fontSize: '0.7rem' }}>❌</button></td>
                   </tr>
                 ))}
               </tbody>
             </table>
             <div style={{ display: 'flex', gap: '10px', flexDirection: 'column' }}>
               <div style={{ display: 'flex', gap: '10px' }}>
                 <input className="table-input" style={{ flex: 1 }} value={newVectorKey} onChange={e => setNewVectorKey(e.target.value)} placeholder="JSON Key (e.g. has_adult_night)" />
                 <select className="table-input" style={{ width: '120px' }} value={newVectorType} onChange={e => setNewVectorType(e.target.value)}>
                   <option value="string">string</option>
                   <option value="boolean">boolean</option>
                   <option value="array">array</option>
                   <option value="JSON">JSON</option>
                 </select>
               </div>
               <div style={{ display: 'flex', gap: '10px' }}>
                 <input className="table-input" style={{ flex: 1 }} value={newVectorInstruction} onChange={e => setNewVectorInstruction(e.target.value)} placeholder="Instruction for the LLM..." />
                 <button onClick={addVector} className="btn-primary" style={{ background: '#8a2be2', color: '#fff', whiteSpace: 'nowrap' }}>+ Vector</button>
               </div>
             </div>
          </div>
        </div>

      </div>

      {/* Right Column: Sandbox */}
      <div className="panel" style={{ flex: 1, background: 'rgba(20,20,30,0.95)', minHeight: 'calc(100vh - 100px)', display: 'flex', flexDirection: 'column' }}>
        <h2 className="panel-header" style={{ color: '#4caf50' }}>Sandbox Playground</h2>
        <div style={{ padding: '15px', display: 'flex', flexDirection: 'column', flex: 1 }}>
           <div style={{ display: 'flex', gap: '10px', marginBottom: '20px' }}>
             <input className="table-input" style={{ flex: 1 }} value={sandboxUrl} onChange={e => setSandboxUrl(e.target.value)} placeholder="https://example.com/target-site" />
             <button onClick={runSandbox} disabled={isSandboxRunning} className="btn-primary" style={{ background: '#4caf50', color: '#fff', opacity: isSandboxRunning ? 0.5 : 1 }}>
               {isSandboxRunning ? 'Extracting...' : 'Run Extraction'}
             </button>
           </div>
           
           <div style={{ flex: 1, background: '#0a0a0a', border: '1px solid #333', borderRadius: '8px', padding: '15px', overflowY: 'auto' }}>
             {sandboxResult?.aiResponse && (
               <div style={{ marginBottom: '20px' }}>
                 <h4 style={{ color: '#4caf50', marginTop: 0, marginBottom: '10px', fontSize: '0.8rem', textTransform: 'uppercase' }}>Ollama JSON Output:</h4>
                 <pre style={{ color: '#00d2ff', fontSize: '0.8rem', margin: 0, whiteSpace: 'pre-wrap', wordBreak: 'break-all' }}>
                   {JSON.stringify(sandboxResult.aiResponse, null, 2)}
                 </pre>
               </div>
             )}
             {sandboxResult?.cleanText && (
               <div>
                 <h4 style={{ color: '#888', marginTop: 0, marginBottom: '10px', fontSize: '0.8rem', textTransform: 'uppercase' }}>Puppeteer Cleaned DOM (Sent to LLM):</h4>
                 <div style={{ color: 'rgba(255,255,255,0.4)', fontSize: '0.7rem', lineHeight: 1.4 }}>
                   {sandboxResult.cleanText}
                 </div>
               </div>
             )}
             {sandboxResult?.error && (
               <div style={{ color: '#ff3b30', fontSize: '0.85rem' }}>
                 <strong>Error:</strong> {sandboxResult.error}
               </div>
             )}
             {!sandboxResult && !isSandboxRunning && (
               <div style={{ color: 'rgba(255,255,255,0.2)', fontSize: '0.85rem', textAlign: 'center', marginTop: '100px' }}>
                 Enter a URL and click Run to test the pipeline against the Local Ollama instance.
               </div>
             )}
           </div>
        </div>
      </div>

    </div>
  );
}
