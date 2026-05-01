import React, { useState, useEffect } from 'react';
import { useFieldRegistry } from '../hooks/useFieldRegistry';
const API_BASE = 'http://localhost:5999';

interface DrawerProps {
  phaseId: number;
  isOpen: boolean;
  onClose: () => void;
  colColor: string;
}

const TagInput = ({ label, tags = [], setTags, colColor }: { label: string, tags: string[], setTags: (t: string[]) => void, colColor: string }) => {
  const [val, setVal] = useState('');
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
      <label style={{ color: '#fff', fontSize: '0.75rem', fontWeight: 600, letterSpacing: '0.05em', display: 'flex', alignItems: 'center', gap: '6px' }}>
        <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: colColor, boxShadow: `0 0 8px ${colColor}` }} />
        {label}
      </label>
      <div style={{ display: 'flex', gap: '8px' }}>
        <input 
          type="text" 
          value={val}
          onChange={e => setVal(e.target.value)}
          placeholder="Type and press Enter..." 
          onKeyDown={e => {
            if (e.key === 'Enter' && val.trim()) {
              e.preventDefault();
              if (!tags.includes(val.trim())) setTags([...tags, val.trim()]);
              setVal('');
            }
          }}
          style={{ 
            flex: 1, 
            background: 'rgba(20, 20, 30, 0.6)', 
            border: `1px solid rgba(255, 255, 255, 0.1)`, 
            borderBottom: `2px solid ${colColor}`,
            color: '#fff', 
            padding: '10px 14px', 
            borderRadius: '6px',
            fontSize: '0.75rem',
            fontFamily: 'Outfit, sans-serif',
            outline: 'none',
            transition: 'all 0.2s ease',
            boxShadow: 'inset 0 2px 4px rgba(0,0,0,0.3)'
          }}
          onFocus={(e) => e.currentTarget.style.boxShadow = `0 0 12px ${colColor}44, inset 0 2px 4px rgba(0,0,0,0.3)`}
          onBlur={(e) => e.currentTarget.style.boxShadow = `inset 0 2px 4px rgba(0,0,0,0.3)`}
        />
        <button onClick={(e) => {
          e.preventDefault();
          if (val.trim() && !tags.includes(val.trim())) { setTags([...tags, val.trim()]); setVal(''); }
        }} style={{ 
          background: `linear-gradient(135deg, ${colColor} 0%, ${colColor}aa 100%)`, 
          color: '#000', 
          border: 'none', 
          padding: '0 20px', 
          borderRadius: '6px',
          cursor: 'pointer', 
          fontWeight: 800,
          fontFamily: 'Outfit, sans-serif',
          letterSpacing: '0.05em',
          boxShadow: `0 4px 10px ${colColor}44`,
          transition: 'all 0.2s ease'
        }}
        onMouseOver={(e) => e.currentTarget.style.transform = 'translateY(-1px)'}
        onMouseOut={(e) => e.currentTarget.style.transform = 'none'}
        >ADD</button>
      </div>
      <div style={{ 
        display: 'flex', flexWrap: 'wrap', gap: '8px', 
        background: 'rgba(0,0,0,0.2)', padding: '12px', 
        border: `1px solid rgba(255,255,255,0.05)`, 
        borderRadius: '8px',
        minHeight: '45px' 
      }}>
        {tags.map((t) => (
          <div key={t} style={{ 
            display: 'flex', alignItems: 'center', gap: '6px', 
            background: `rgba(255,255,255,0.05)`, 
            border: `1px solid ${colColor}55`, 
            padding: '4px 10px', 
            borderRadius: '16px', 
            color: '#fff',
            fontSize: '0.7rem',
            boxShadow: `0 2px 6px rgba(0,0,0,0.3)`
          }}>
            <span>{t}</span>
            <button onClick={() => setTags(tags.filter(x => x !== t))} style={{ 
              background: 'none', border: 'none', color: colColor, 
              cursor: 'pointer', fontSize: '0.8rem', fontWeight: 'bold', padding: '0 2px' 
            }}>×</button>
          </div>
        ))}
        {tags.length === 0 && <span style={{ color: 'rgba(255,255,255,0.2)', fontStyle: 'italic', fontSize: '0.7rem' }}>No items added yet.</span>}
      </div>
    </div>
  );
};

const ToggleSwitch = ({ label, checked, onChange, colColor }: { label: string, checked: boolean, onChange: (v: boolean) => void, colColor: string }) => (
  <div style={{ 
    display: 'flex', alignItems: 'center', justifyContent: 'space-between', 
    padding: '12px 16px', background: 'rgba(20,20,30,0.5)', 
    border: `1px solid rgba(255,255,255,0.05)`, 
    borderRadius: '8px',
    boxShadow: 'inset 0 2px 4px rgba(0,0,0,0.2)'
  }}>
    <label style={{ color: '#fff', fontSize: '0.75rem', fontWeight: 500, fontFamily: 'Outfit, sans-serif' }}>{label}</label>
    <div 
      onClick={() => onChange(!checked)}
      style={{ 
        width: '44px', height: '24px', 
        background: checked ? colColor : 'rgba(255,255,255,0.1)', 
        borderRadius: '12px', position: 'relative', cursor: 'pointer', 
        transition: 'all 0.3s cubic-bezier(0.4, 0.0, 0.2, 1)',
        boxShadow: checked ? `0 0 10px ${colColor}66` : 'none'
      }}
    >
      <div style={{ 
        position: 'absolute', top: '2px', left: checked ? '22px' : '2px', 
        width: '20px', height: '20px', 
        background: '#fff', borderRadius: '50%', 
        transition: 'all 0.3s cubic-bezier(0.4, 0.0, 0.2, 1)',
        boxShadow: '0 2px 4px rgba(0,0,0,0.3)'
      }} />
    </div>
  </div>
);

export const PhaseControlDrawer: React.FC<DrawerProps> = ({ phaseId, isOpen, onClose, colColor }) => {
  const { fields, loading: registryLoading, toggleImportance } = useFieldRegistry();
  const [config, setConfig] = useState<any>(null);
  const [blocklist, setBlocklist] = useState<any[]>([]);
  const [sandboxUrl, setSandboxUrl] = useState('');
  const [sandboxSpotName, setSandboxSpotName] = useState('');
  const [sandboxSpotCity, setSandboxSpotCity] = useState('');
  const [sandboxResult, setSandboxResult] = useState<any>(null);
  const [isSandboxRunning, setIsSandboxRunning] = useState(false);
  const [saveStatus, setSaveStatus] = useState<'idle' | 'saving' | 'success' | 'error'>('idle');
  const [loading, setLoading] = useState(true);

  const fetchConfig = async () => {
    try {
      const res = await fetch(`${API_BASE}/config`);
      const data = await res.json();
      setConfig(data.config || {});
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const fetchBlocklist = async () => {
    try {
      const res = await fetch(`${API_BASE}/api/scraper/blocklist`);
      const data = await res.json();
      if (data.keywords) setBlocklist(data.keywords);
    } catch (err) {
      console.error('Failed to fetch blocklist', err);
    }
  };

  useEffect(() => {
    if (isOpen) {
      setLoading(true);
      fetchConfig();
      if (phaseId === 1) fetchBlocklist();
    }
  }, [isOpen, phaseId]);

  const handleSave = async () => {
    setSaveStatus('saving');
    try {
      await fetch(`${API_BASE}/config`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(config)
      });
      setSaveStatus('success');
      setTimeout(() => setSaveStatus('idle'), 2000);
    } catch (err) {
      console.error(err);
      setSaveStatus('error');
      setTimeout(() => setSaveStatus('idle'), 3000);
    }
  };

  const handleUpdate = (key: string, value: any) => {
    setConfig((prev: any) => ({ ...prev, [key]: value }));
  };

  if (!isOpen) return null;

  return (
    <div style={{
      width: '100%',
      background: 'rgba(15, 15, 20, 0.85)',
      backdropFilter: 'blur(20px)',
      WebkitBackdropFilter: 'blur(20px)',
      border: `1px solid ${colColor}44`,
      borderTop: `3px solid ${colColor}`,
      borderRadius: '12px',
      padding: '24px',
      marginTop: '12px',
      marginBottom: '24px',
      boxShadow: `0 10px 40px rgba(0,0,0,0.5), 0 0 20px ${colColor}15`,
      display: 'flex',
      flexDirection: 'column',
      gap: '16px',
      fontFamily: 'Outfit, sans-serif',
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: `1px solid rgba(255,255,255,0.1)`, paddingBottom: '16px' }}>
        <h3 style={{ margin: 0, color: '#fff', textTransform: 'uppercase', letterSpacing: '0.15em', display: 'flex', alignItems: 'center', gap: '10px', fontSize: '1rem', fontWeight: 800 }}>
          <div style={{ width: '12px', height: '12px', background: colColor, borderRadius: '2px', boxShadow: `0 0 10px ${colColor}` }} />
          Phase {phaseId} Configuration
        </h3>
        <button onClick={onClose} style={{ 
          background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', color: '#fff', 
          cursor: 'pointer', fontSize: '0.7rem', fontWeight: 600, padding: '6px 12px', borderRadius: '20px',
          transition: 'all 0.2s', letterSpacing: '0.05em'
        }}
        onMouseOver={(e) => { e.currentTarget.style.background = 'rgba(255,255,255,0.1)'; e.currentTarget.style.borderColor = 'rgba(255,255,255,0.3)'; }}
        onMouseOut={(e) => { e.currentTarget.style.background = 'rgba(255,255,255,0.05)'; e.currentTarget.style.borderColor = 'rgba(255,255,255,0.1)'; }}
        >✖ CLOSE</button>
      </div>

      {loading ? (
        <div style={{ color: 'rgba(255,255,255,0.5)', padding: '40px', textAlign: 'center', fontFamily: 'JetBrains Mono, monospace', fontSize: '0.8rem' }}>
          <div style={{ width: '20px', height: '20px', border: `2px solid ${colColor}`, borderTopColor: 'transparent', borderRadius: '50%', animation: 'spin 1s linear infinite', margin: '0 auto 10px' }} />
          Loading secure configuration...
        </div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '24px' }}>
          
          {/* Phase 1: SCOUT */}
          {phaseId === 1 && (
            <>
              <div style={{ gridColumn: '1 / -1', background: 'rgba(0,255,170,0.05)', border: `1px solid ${colColor}44`, padding: '10px', borderRadius: '6px', marginBottom: '8px' }}>
                <h4 style={{ margin: '0 0 6px 0', color: colColor, textTransform: 'uppercase', fontSize: '0.65rem' }}>Data Acquisition Mapping</h4>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', color: 'rgba(255,255,255,0.7)', fontSize: '0.6rem' }}>
                  {fields.filter(f => f.phase_id === 1).map(f => {
  const isReq = f.importance_level === 2;
  const isPri = f.importance_level === 1;
  const icon = isReq ? '🛑' : isPri ? '⭐' : '⚪';
  const color = isReq ? '#f44336' : isPri ? '#ffeb3b' : 'rgba(255,255,255,0.7)';
  return (
    <button key={f.id || f.field_name} onClick={() => toggleImportance(f.id, f.importance_level)} style={{ background: 'rgba(255,255,255,0.05)', padding: '2px 6px', borderRadius: '4px', border: 'none', color, cursor: 'pointer', fontSize: '0.65rem' }}>
      {icon} {f.field_name}
    </button>
  );
})}
                  {registryLoading && <span>Loading registry...</span>}
                </div>
              </div>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                <div style={{ background: 'rgba(138,43,226,0.06)', border: '1px solid rgba(138,43,226,0.2)', borderRadius: '6px', padding: '10px 14px', display: 'flex', alignItems: 'center', gap: '10px' }}>
                  <span style={{ fontSize: '1rem' }}>⚙️</span>
                  <div>
                    <div style={{ color: '#8a2be2', fontSize: '0.68rem', fontWeight: 800, textTransform: 'uppercase', letterSpacing: '0.05em' }}>Global Controls</div>
                    <div style={{ color: 'rgba(255,255,255,0.45)', fontSize: '0.62rem', marginTop: '2px' }}>Target Facilities & Priority States are managed in the <strong style={{ color: 'rgba(255,255,255,0.7)' }}>⚙ Global Controls</strong> bar at the top of the page.</div>
                  </div>
                </div>
              </div>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                <div>
                  <label style={{ color: '#fff', fontSize: '0.75rem', fontWeight: 600, letterSpacing: '0.05em', display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '8px' }}>
  <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: colColor, boxShadow: `0 0 8px ${colColor}` }} />
  Scout Rate Limit (RPM)
</label>
                  <input type="number" value={config?.scout_rate_limit_rpm || 12} onChange={(e) => handleUpdate('scout_rate_limit_rpm', parseInt(e.target.value))} style={{ width: '100%', background: 'rgba(20, 20, 30, 0.6)', border: '1px solid rgba(255, 255, 255, 0.1)', borderBottom: `2px solid ${colColor}`, color: '#fff', padding: '10px 14px', borderRadius: '6px', fontSize: '0.75rem', fontFamily: 'Outfit, sans-serif', outline: 'none', transition: 'all 0.2s ease', boxShadow: 'inset 0 2px 4px rgba(0,0,0,0.3)', boxSizing: 'border-box' }} />
                </div>
                <div>
                  <label style={{ color: '#fff', fontSize: '0.75rem', fontWeight: 600, letterSpacing: '0.05em', display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '8px' }}>
  <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: colColor, boxShadow: `0 0 8px ${colColor}` }} />
  Gatekeeper Rules (JSON)
</label>
                  <textarea rows={4} value={JSON.stringify(config?.scout_gatekeeper_rules, null, 2)} onChange={(e) => {
                    try { handleUpdate('scout_gatekeeper_rules', JSON.parse(e.target.value)) } catch (e) {}
                  }} style={{ width: '100%', background: 'rgba(20, 20, 30, 0.6)', border: '1px solid rgba(255, 255, 255, 0.1)', borderBottom: `2px solid ${colColor}`, color: '#fff', padding: '10px 14px', borderRadius: '6px', fontSize: '0.75rem', fontFamily: 'Outfit, sans-serif', outline: 'none', transition: 'all 0.2s ease', boxShadow: 'inset 0 2px 4px rgba(0,0,0,0.3)', boxSizing: 'border-box' }} />
                </div>
              </div>
              <div style={{ gridColumn: '1 / -1', borderTop: `1px dashed ${colColor}55`, paddingTop: '12px' }}>
                <label style={{ color: '#ff3366', display: 'block', marginBottom: '4px', fontWeight: 'bold' }}>BLOCKLIST GUILLOTINE</label>
                <div style={{ display: 'flex', gap: '8px', marginBottom: '8px' }}>
                  <input type="text" id="new-blocklist-kw" placeholder="Enter keyword/pattern to purge & block..." style={{ flex: 1, background: 'rgba(20, 20, 30, 0.6)', border: '1px solid rgba(255, 255, 255, 0.1)', borderBottom: `2px solid #ff3366`, color: '#fff', padding: '10px 14px', borderRadius: '6px', fontSize: '0.75rem', fontFamily: 'Outfit, sans-serif', outline: 'none', transition: 'all 0.2s ease', boxShadow: 'inset 0 2px 4px rgba(0,0,0,0.3)', boxSizing: 'border-box' }} />
                  <button onClick={async (e) => {
                    const el = document.getElementById('new-blocklist-kw') as HTMLInputElement;
                    if (!el.value) return;
                    const origText = el.value;
                    const btn = e.currentTarget;
                    btn.innerText = 'PURGING...';
                    btn.style.opacity = '0.5';
                    try {
                      const res = await fetch(`${API_BASE}/api/scraper/blocklist`, {
                        method: 'POST', headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ keyword: origText, match_type: 'name' })
                      });
                      const data = await res.json();
                      el.value = '';
                      btn.innerText = `✓ PURGED ${data.count || 0} SPOTS`;
                      btn.style.background = '#4caf50';
                      btn.style.boxShadow = '0 4px 10px #4caf5044';
                      btn.style.opacity = '1';
                      fetchBlocklist();
                      setTimeout(() => {
                        btn.innerText = 'BLOCK & PURGE';
                        btn.style.background = `linear-gradient(135deg, #ff3366 0%, #ff3366aa 100%)`;
                        btn.style.boxShadow = `0 4px 10px #ff336644`;
                      }, 3000);
                    } catch (err) {
                      btn.innerText = '✖ FAILED';
                      btn.style.background = '#f44336';
                      btn.style.opacity = '1';
                      setTimeout(() => {
                        btn.innerText = 'BLOCK & PURGE';
                        btn.style.background = `linear-gradient(135deg, #ff3366 0%, #ff3366aa 100%)`;
                      }, 3000);
                    }
                  }} style={{ background: `linear-gradient(135deg, #ff3366 0%, #ff3366aa 100%)`, color: '#fff', border: 'none', padding: '0 20px', borderRadius: '6px', cursor: 'pointer', fontWeight: 800, fontFamily: 'Outfit, sans-serif', letterSpacing: '0.05em', boxShadow: `0 4px 10px #ff336644`, transition: 'all 0.2s ease' }}>BLOCK & PURGE</button>
                </div>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px', background: 'rgba(0,0,0,0.3)', padding: '8px', border: `1px solid #ff336655`, minHeight: '40px' }}>
                  {blocklist.map((item: any) => (
                    <div key={item.id} style={{ display: 'flex', alignItems: 'center', gap: '4px', background: `rgba(255,0,0,0.1)`, border: `1px solid rgba(255,0,0,0.3)`, padding: '2px 8px', borderRadius: '12px' }}>
                      <span style={{ color: '#ff6b6b' }}>{item.pattern}</span>
                      <button onClick={async () => {
                        await fetch(`${API_BASE}/api/scraper/blocklist/${item.id}`, { method: 'DELETE' });
                        fetchBlocklist();
                      }} style={{ background: 'none', border: 'none', color: 'rgba(255,255,255,0.4)', cursor: 'pointer', fontSize: '0.6rem' }}>x</button>
                    </div>
                  ))}
                  {blocklist.length === 0 && <span style={{ color: 'rgba(255,255,255,0.3)' }}>No blocklist rules found.</span>}
                </div>
              </div>
            </>
          )}


          {/* Phase 2: DETECTIVE */}
          {phaseId === 2 && (
            <>
              <div style={{ gridColumn: '1 / -1', background: 'rgba(255,106,0,0.05)', border: `1px solid ${colColor}44`, padding: '10px', borderRadius: '6px', marginBottom: '8px' }}>
                <h4 style={{ margin: '0 0 6px 0', color: colColor, textTransform: 'uppercase', fontSize: '0.65rem' }}>Data Acquisition Mapping</h4>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', color: 'rgba(255,255,255,0.7)', fontSize: '0.6rem' }}>
                  {fields.filter(f => f.phase_id === 2).map(f => {
  const isReq = f.importance_level === 2;
  const isPri = f.importance_level === 1;
  const icon = isReq ? '🛑' : isPri ? '⭐' : '⚪';
  const color = isReq ? '#f44336' : isPri ? '#ffeb3b' : 'rgba(255,255,255,0.7)';
  return (
    <button key={f.id || f.field_name} onClick={() => toggleImportance(f.id, f.importance_level)} style={{ background: 'rgba(255,255,255,0.05)', padding: '2px 6px', borderRadius: '4px', border: 'none', color, cursor: 'pointer', fontSize: '0.65rem' }}>
      {icon} {f.field_name}
    </button>
  );
})}
                  {config?.ai_target_vectors?.map((v: any, i: number) => (
                    <span key={`vec-${i}`} style={{ color: colColor, background: 'rgba(255,255,255,0.05)', padding: '2px 6px', borderRadius: '4px' }}>✓ {v.key || v}</span>
                  ))}
                  {registryLoading && <span>Loading registry...</span>}
                </div>
              </div>

              {/* Phase 3 Layout: 2-Column Grid */}
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px', alignItems: 'start' }}>
                
                {/* Left Column: AI Configuration */}
                <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                  <div>
                    <label style={{ color: '#fff', fontSize: '0.75rem', fontWeight: 600, letterSpacing: '0.05em', display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '8px' }}>
                      <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: colColor, boxShadow: `0 0 8px ${colColor}` }} />
                      Model Engine
                    </label>
                    <input type="text" value={config?.detective_model || 'Llama3.2-8b'} onChange={(e) => handleUpdate('detective_model', e.target.value)} style={{ width: '100%', background: 'rgba(20, 20, 30, 0.6)', border: '1px solid rgba(255, 255, 255, 0.1)', borderBottom: `2px solid ${colColor}`, color: '#fff', padding: '10px 14px', borderRadius: '6px', fontSize: '0.75rem', fontFamily: 'Outfit, sans-serif', outline: 'none', transition: 'all 0.2s ease', boxShadow: 'inset 0 2px 4px rgba(0,0,0,0.3)', boxSizing: 'border-box' }} />
                  </div>
                  <div style={{ display: 'flex', gap: '10px' }}>
                    <div style={{ flex: 1 }}>
                      <label style={{ color: '#fff', fontSize: '0.75rem', fontWeight: 600, letterSpacing: '0.05em', display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '8px' }}>
                        <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: colColor, boxShadow: `0 0 8px ${colColor}` }} />
                        Confidence Threshold
                      </label>
                      <input type="number" value={config?.detective_confidence_min || 80} onChange={(e) => handleUpdate('detective_confidence_min', parseInt(e.target.value))} style={{ width: '100%', background: 'rgba(20, 20, 30, 0.6)', border: '1px solid rgba(255, 255, 255, 0.1)', borderBottom: `2px solid ${colColor}`, color: '#fff', padding: '10px 14px', borderRadius: '6px', fontSize: '0.75rem', fontFamily: 'Outfit, sans-serif', outline: 'none', transition: 'all 0.2s ease', boxShadow: 'inset 0 2px 4px rgba(0,0,0,0.3)', boxSizing: 'border-box' }} />
                    </div>
                    <div style={{ flex: 1 }}>
                      <label style={{ color: '#fff', fontSize: '0.75rem', fontWeight: 600, letterSpacing: '0.05em', display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '8px' }}>
                        <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: colColor, boxShadow: `0 0 8px ${colColor}` }} />
                        Temperature
                      </label>
                      <input type="number" step="0.1" value={config?.detective_temperature || 0.1} onChange={(e) => handleUpdate('detective_temperature', parseFloat(e.target.value))} style={{ width: '100%', background: 'rgba(20, 20, 30, 0.6)', border: '1px solid rgba(255, 255, 255, 0.1)', borderBottom: `2px solid ${colColor}`, color: '#fff', padding: '10px 14px', borderRadius: '6px', fontSize: '0.75rem', fontFamily: 'Outfit, sans-serif', outline: 'none', transition: 'all 0.2s ease', boxShadow: 'inset 0 2px 4px rgba(0,0,0,0.3)', boxSizing: 'border-box' }} />
                    </div>
                  </div>
                  <div>
                    <label style={{ color: '#fff', fontSize: '0.75rem', fontWeight: 600, letterSpacing: '0.05em', display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '8px' }}>
                      <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: colColor, boxShadow: `0 0 8px ${colColor}` }} />
                      AI Target Vectors & Prompts
                    </label>
                    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                      {(config?.ai_target_vectors || []).map((vec: any, idx: number) => (
                        <div key={idx} style={{ display: 'flex', gap: '8px', background: 'rgba(20, 20, 30, 0.6)', border: '1px solid rgba(255, 255, 255, 0.1)', padding: '8px', borderRadius: '6px' }}>
                          <input type="text" value={vec.key || (typeof vec === 'string' ? vec : '')} onChange={e => {
                            const newArr = [...(config?.ai_target_vectors || [])];
                            if (typeof newArr[idx] === 'string') {
                              newArr[idx] = { key: e.target.value, prompt: '' };
                            } else {
                              newArr[idx] = { ...newArr[idx], key: e.target.value };
                            }
                            handleUpdate('ai_target_vectors', newArr);
                          }} style={{ width: '100px', background: 'rgba(0,0,0,0.5)', border: `1px solid ${colColor}44`, color: colColor, padding: '6px', borderRadius: '4px', fontSize: '0.7rem', outline: 'none' }} placeholder="Vector Key" />
                          <input type="text" value={vec.prompt || ''} onChange={e => {
                            const newArr = [...(config?.ai_target_vectors || [])];
                            if (typeof newArr[idx] === 'string') {
                              newArr[idx] = { key: newArr[idx], prompt: e.target.value };
                            } else {
                              newArr[idx] = { ...newArr[idx], prompt: e.target.value };
                            }
                            handleUpdate('ai_target_vectors', newArr);
                          }} style={{ flex: 1, background: 'rgba(0,0,0,0.5)', border: `1px solid rgba(255,255,255,0.1)`, color: '#fff', padding: '6px', borderRadius: '4px', fontSize: '0.7rem', outline: 'none' }} placeholder="Extraction Prompt Instructions..." />
                          <button onClick={() => {
                            const newArr = [...(config?.ai_target_vectors || [])];
                            newArr.splice(idx, 1);
                            handleUpdate('ai_target_vectors', newArr);
                          }} style={{ background: 'transparent', border: 'none', color: '#ff3366', cursor: 'pointer', fontWeight: 800 }}>×</button>
                        </div>
                      ))}
                      <button onClick={() => {
                        const newArr = [...(config?.ai_target_vectors || [])];
                        newArr.push({ key: 'new_vector', prompt: 'New extraction prompt...' });
                        handleUpdate('ai_target_vectors', newArr);
                      }} style={{ alignSelf: 'flex-start', background: `linear-gradient(135deg, ${colColor} 0%, ${colColor}aa 100%)`, color: '#fff', border: 'none', padding: '6px 12px', borderRadius: '4px', cursor: 'pointer', fontSize: '0.65rem', fontWeight: 800 }}>+ ADD VECTOR PROMPT</button>
                    </div>
                  </div>
                  <TagInput label="Toxicity Bouncer (Exclusion Keywords)" tags={config?.ai_exclusion_keywords || []} setTags={(t: any) => handleUpdate('ai_exclusion_keywords', t)} colColor={colColor} />
                  <div>
                    <label style={{ color: '#fff', fontSize: '0.75rem', fontWeight: 600, letterSpacing: '0.05em', display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '8px' }}>
                      <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: colColor, boxShadow: `0 0 8px ${colColor}` }} />
                      System Prompt
                    </label>
                    <textarea value={config?.ai_system_prompt || ''} onChange={(e) => handleUpdate('ai_system_prompt', e.target.value)} style={{ width: '100%', height: '100px', background: 'rgba(20, 20, 30, 0.6)', border: '1px solid rgba(255, 255, 255, 0.1)', borderBottom: `2px solid ${colColor}`, color: '#fff', padding: '10px 14px', borderRadius: '6px', fontSize: '0.75rem', fontFamily: 'Outfit, sans-serif', outline: 'none', transition: 'all 0.2s ease', boxShadow: 'inset 0 2px 4px rgba(0,0,0,0.3)', boxSizing: 'border-box' }} />
                  </div>
                </div>
                
                {/* Right Column: Sandbox Playground */}
                <div style={{ background: 'rgba(20,20,30,0.9)', border: `1px solid ${colColor}`, borderRadius: '8px', padding: '16px', display: 'flex', flexDirection: 'column', height: '100%' }}>
                  <h4 style={{ margin: '0 0 12px 0', color: colColor, textTransform: 'uppercase', fontSize: '0.75rem', display: 'flex', alignItems: 'center', gap: '6px' }}>
                    <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: colColor, boxShadow: `0 0 8px ${colColor}` }} />
                    Sandbox Playground (Live Test)
                  </h4>
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', marginBottom: '16px' }}>
                    <div style={{ display: 'flex', gap: '8px' }}>
                      <input type="text" value={sandboxSpotName} onChange={e => setSandboxSpotName(e.target.value)} placeholder="Spot Name (e.g. Skate City)" style={{ flex: 1, background: 'rgba(0,0,0,0.5)', border: `1px solid rgba(255,255,255,0.1)`, color: '#fff', padding: '8px', borderRadius: '4px', fontSize: '0.7rem' }} />
                      <input type="text" value={sandboxSpotCity} onChange={e => setSandboxSpotCity(e.target.value)} placeholder="City (e.g. Aurora)" style={{ flex: 1, background: 'rgba(0,0,0,0.5)', border: `1px solid rgba(255,255,255,0.1)`, color: '#fff', padding: '8px', borderRadius: '4px', fontSize: '0.7rem' }} />
                    </div>
                    <input type="text" value={sandboxUrl} onChange={e => setSandboxUrl(e.target.value)} placeholder="Target URL: https://target-site.com" style={{ width: '100%', background: 'rgba(0,0,0,0.5)', border: `1px solid rgba(255,255,255,0.1)`, color: '#fff', padding: '8px', borderRadius: '4px', fontSize: '0.7rem', boxSizing: 'border-box' }} />
                    <button onClick={async () => {
                      if (!sandboxUrl) return alert('Enter a URL');
                      setIsSandboxRunning(true);
                      setSandboxResult(null);
                      try {
                        const res = await fetch(`${API_BASE}/api/sandbox`, {
                          method: 'POST', headers: { 'Content-Type': 'application/json' },
                          body: JSON.stringify({
                            url: sandboxUrl, spot_name: sandboxSpotName, spot_city: sandboxSpotCity,
                            ai_system_prompt: config?.ai_system_prompt, ai_target_vectors: config?.ai_target_vectors,
                            detective_model: config?.detective_model
                          })
                        });
                        const data = await res.json();
                        setSandboxResult(data);
                      } catch (err: any) {
                        setSandboxResult({ error: err.message });
                      }
                      setIsSandboxRunning(false);
                    }} disabled={isSandboxRunning} style={{ width: '100%', background: `linear-gradient(135deg, ${colColor} 0%, ${colColor}aa 100%)`, color: '#fff', border: 'none', padding: '8px 16px', borderRadius: '4px', cursor: 'pointer', fontSize: '0.75rem', fontWeight: 800, opacity: isSandboxRunning ? 0.5 : 1 }}>
                      {isSandboxRunning ? 'RUNNING INJECTION...' : 'TEST INJECTION'}
                    </button>
                  </div>
                  
                  {sandboxResult && (
                    <div style={{ background: '#000', border: `1px solid ${colColor}44`, borderRadius: '6px', padding: '12px', overflowY: 'auto', flex: 1, display: 'flex', flexDirection: 'column', gap: '12px' }}>
                      {sandboxResult.aiResponse && (
                        <div>
                          <h5 style={{ margin: '0 0 6px 0', color: colColor, fontSize: '0.65rem', textTransform: 'uppercase' }}>Ollama JSON Output</h5>
                          <pre style={{ margin: 0, color: '#fff', fontSize: '0.65rem', whiteSpace: 'pre-wrap', wordBreak: 'break-all', background: 'rgba(255,255,255,0.05)', padding: '8px', borderRadius: '4px', border: '1px solid rgba(255,255,255,0.1)' }}>{JSON.stringify(sandboxResult.aiResponse, null, 2)}</pre>
                        </div>
                      )}
                      {sandboxResult.cleanText && (
                        <div>
                          <h5 style={{ margin: '0 0 6px 0', color: 'rgba(255,255,255,0.5)', fontSize: '0.65rem', textTransform: 'uppercase' }}>Puppeteer Cleaned DOM</h5>
                          <div style={{ color: 'rgba(255,255,255,0.4)', fontSize: '0.6rem', lineHeight: 1.4, maxHeight: '200px', overflowY: 'auto', background: 'rgba(255,255,255,0.02)', padding: '8px', borderRadius: '4px' }}>{sandboxResult.cleanText}</div>
                        </div>
                      )}
                      {sandboxResult.error && (
                        <div style={{ color: '#ff3366', fontSize: '0.75rem', padding: '8px', background: 'rgba(255,51,102,0.1)', borderRadius: '4px', border: '1px solid rgba(255,51,102,0.3)' }}>Error: {sandboxResult.error}</div>
                      )}
                    </div>
                  )}
                </div>
              </div>
            </>
          )}

          {/* Phase 3: PHOTOGRAPHER */}
          {phaseId === 3 && (
            <>
              <div style={{ gridColumn: '1 / -1', background: 'rgba(255,0,127,0.05)', border: `1px solid ${colColor}44`, padding: '10px', borderRadius: '6px', marginBottom: '8px' }}>
                <h4 style={{ margin: '0 0 6px 0', color: colColor, textTransform: 'uppercase', fontSize: '0.65rem' }}>Data Acquisition Mapping</h4>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', color: 'rgba(255,255,255,0.7)', fontSize: '0.6rem' }}>
                  {fields.filter(f => f.phase_id === 3).map(f => {
  const isReq = f.importance_level === 2;
  const isPri = f.importance_level === 1;
  const icon = isReq ? '🛑' : isPri ? '⭐' : '⚪';
  const color = isReq ? '#f44336' : isPri ? '#ffeb3b' : 'rgba(255,255,255,0.7)';
  return (
    <button key={f.id || f.field_name} onClick={() => toggleImportance(f.id, f.importance_level)} style={{ background: 'rgba(255,255,255,0.05)', padding: '2px 6px', borderRadius: '4px', border: 'none', color, cursor: 'pointer', fontSize: '0.65rem' }}>
      {icon} {f.field_name}
    </button>
  );
})}
                  {config?.photo_categories?.map((c: string, i: number) => (
                    <span key={`cat-${i}`} style={{ color: colColor, background: 'rgba(255,255,255,0.05)', padding: '2px 6px', borderRadius: '4px' }}>✓ {c}</span>
                  ))}
                  {registryLoading && <span>Loading registry...</span>}
                </div>
              </div>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                <div>
                  <label style={{ color: '#fff', fontSize: '0.75rem', fontWeight: 600, letterSpacing: '0.05em', display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '8px' }}>
  <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: colColor, boxShadow: `0 0 8px ${colColor}` }} />
  Vision API Engine
</label>
                  <input type="text" value={config?.photo_vision_api || 'google_cloud_vision'} onChange={(e) => handleUpdate('photo_vision_api', e.target.value)} style={{ width: '100%', background: 'rgba(20, 20, 30, 0.6)', border: '1px solid rgba(255, 255, 255, 0.1)', borderBottom: `2px solid ${colColor}`, color: '#fff', padding: '10px 14px', borderRadius: '6px', fontSize: '0.75rem', fontFamily: 'Outfit, sans-serif', outline: 'none', transition: 'all 0.2s ease', boxShadow: 'inset 0 2px 4px rgba(0,0,0,0.3)', boxSizing: 'border-box' }} />
                </div>
                <TagInput label="Photo Sources (google, instagram)" tags={config?.photo_sources || []} setTags={(t) => handleUpdate('photo_sources', t)} colColor={colColor} />
                <TagInput label="Desired Categories (facade, floor)" tags={config?.photo_categories || []} setTags={(t) => handleUpdate('photo_categories', t)} colColor={colColor} />
              </div>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                <div style={{ display: 'flex', gap: '10px' }}>
                  <div style={{ flex: 1 }}>
                    <label style={{ color: '#fff', fontSize: '0.75rem', fontWeight: 600, letterSpacing: '0.05em', display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '8px' }}>
  <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: colColor, boxShadow: `0 0 8px ${colColor}` }} />
  Min Photo Count
</label>
                    <input type="number" value={config?.photo_min_count || 1} onChange={(e) => handleUpdate('photo_min_count', parseInt(e.target.value))} style={{ width: '100%', background: 'rgba(20, 20, 30, 0.6)', border: '1px solid rgba(255, 255, 255, 0.1)', borderBottom: `2px solid ${colColor}`, color: '#fff', padding: '10px 14px', borderRadius: '6px', fontSize: '0.75rem', fontFamily: 'Outfit, sans-serif', outline: 'none', transition: 'all 0.2s ease', boxShadow: 'inset 0 2px 4px rgba(0,0,0,0.3)', boxSizing: 'border-box' }} />
                  </div>
                  <div style={{ flex: 1 }}>
                    <label style={{ color: '#fff', fontSize: '0.75rem', fontWeight: 600, letterSpacing: '0.05em', display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '8px' }}>
  <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: colColor, boxShadow: `0 0 8px ${colColor}` }} />
  Min Size (KB)
</label>
                    <input type="number" value={config?.photo_min_size_kb || 50} onChange={(e) => handleUpdate('photo_min_size_kb', parseInt(e.target.value))} style={{ width: '100%', background: 'rgba(20, 20, 30, 0.6)', border: '1px solid rgba(255, 255, 255, 0.1)', borderBottom: `2px solid ${colColor}`, color: '#fff', padding: '10px 14px', borderRadius: '6px', fontSize: '0.75rem', fontFamily: 'Outfit, sans-serif', outline: 'none', transition: 'all 0.2s ease', boxShadow: 'inset 0 2px 4px rgba(0,0,0,0.3)', boxSizing: 'border-box' }} />
                  </div>
                </div>
                <div>
                  <label style={{ color: '#fff', fontSize: '0.75rem', fontWeight: 600, letterSpacing: '0.05em', display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '8px' }}>
  <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: colColor, boxShadow: `0 0 8px ${colColor}` }} />
  Gatekeeper Rules (JSON)
</label>
                  <textarea rows={4} value={JSON.stringify(config?.photo_gatekeeper_rules, null, 2)} onChange={(e) => {
                    try { handleUpdate('photo_gatekeeper_rules', JSON.parse(e.target.value)) } catch (e) {}
                  }} style={{ width: '100%', background: 'rgba(20, 20, 30, 0.6)', border: '1px solid rgba(255, 255, 255, 0.1)', borderBottom: `2px solid ${colColor}`, color: '#fff', padding: '10px 14px', borderRadius: '6px', fontSize: '0.75rem', fontFamily: 'Outfit, sans-serif', outline: 'none', transition: 'all 0.2s ease', boxShadow: 'inset 0 2px 4px rgba(0,0,0,0.3)', boxSizing: 'border-box' }} />
                </div>
              </div>
            </>
          )}

          {/* Phase 4: PUBLISHER */}
          {phaseId === 4 && (
            <>
              <div style={{ gridColumn: '1 / -1', background: 'rgba(0,212,255,0.05)', border: `1px solid ${colColor}44`, padding: '10px', borderRadius: '6px', marginBottom: '8px' }}>
                <h4 style={{ margin: '0 0 6px 0', color: colColor, textTransform: 'uppercase', fontSize: '0.65rem' }}>Data Acquisition Mapping</h4>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', color: 'rgba(255,255,255,0.7)', fontSize: '0.6rem' }}>
                  {fields.filter(f => f.phase_id === 4).map(f => {
  const isReq = f.importance_level === 2;
  const isPri = f.importance_level === 1;
  const icon = isReq ? '🛑' : isPri ? '⭐' : '⚪';
  const color = isReq ? '#f44336' : isPri ? '#ffeb3b' : 'rgba(255,255,255,0.7)';
  return (
    <button key={f.id || f.field_name} onClick={() => toggleImportance(f.id, f.importance_level)} style={{ background: 'rgba(255,255,255,0.05)', padding: '2px 6px', borderRadius: '4px', border: 'none', color, cursor: 'pointer', fontSize: '0.65rem' }}>
      {icon} {f.field_name}
    </button>
  );
})}
                  {registryLoading && <span>Loading registry...</span>}
                </div>
              </div>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                <ToggleSwitch label="Master Auto-Publish Enabled" checked={config?.publisher_auto_publish_enabled} onChange={(v) => handleUpdate('publisher_auto_publish_enabled', v)} colColor={colColor} />
                <div>
                  <label style={{ color: '#fff', fontSize: '0.75rem', fontWeight: 600, letterSpacing: '0.05em', display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '8px' }}>
  <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: colColor, boxShadow: `0 0 8px ${colColor}` }} />
  Auto Publish Threshold
</label>
                  <input type="number" value={config?.publisher_auto_publish_threshold || 80} onChange={(e) => handleUpdate('publisher_auto_publish_threshold', parseInt(e.target.value))} style={{ width: '100%', background: 'rgba(20, 20, 30, 0.6)', border: '1px solid rgba(255, 255, 255, 0.1)', borderBottom: `2px solid ${colColor}`, color: '#fff', padding: '10px 14px', borderRadius: '6px', fontSize: '0.75rem', fontFamily: 'Outfit, sans-serif', outline: 'none', transition: 'all 0.2s ease', boxShadow: 'inset 0 2px 4px rgba(0,0,0,0.3)', boxSizing: 'border-box' }} />
                </div>
                <TagInput label="Required Schema Fields" tags={config?.publisher_required_fields || []} setTags={(t) => handleUpdate('publisher_required_fields', t)} colColor={colColor} />
              </div>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                <div>
                  <label style={{ color: '#fff', fontSize: '0.75rem', fontWeight: 600, letterSpacing: '0.05em', display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '8px' }}>
  <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: colColor, boxShadow: `0 0 8px ${colColor}` }} />
  Upsert Strategy
</label>
                  <select value={config?.publisher_upsert_strategy || 'merge'} onChange={(e) => handleUpdate('publisher_upsert_strategy', e.target.value)} style={{ width: '100%', background: 'rgba(20, 20, 30, 0.6)', border: '1px solid rgba(255, 255, 255, 0.1)', borderBottom: `2px solid ${colColor}`, color: '#fff', padding: '10px 14px', borderRadius: '6px', fontSize: '0.75rem', fontFamily: 'Outfit, sans-serif', outline: 'none', transition: 'all 0.2s ease', boxShadow: 'inset 0 2px 4px rgba(0,0,0,0.3)', boxSizing: 'border-box' }}>
  <option value="merge" style={{ background: '#111' }}>MERGE (Preserve existing)</option>
  <option value="overwrite" style={{ background: '#111' }}>OVERWRITE (Force update)</option>
</select>
                </div>
                <div>
                  <label style={{ color: '#fff', fontSize: '0.75rem', fontWeight: 600, letterSpacing: '0.05em', display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '8px' }}>
  <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: colColor, boxShadow: `0 0 8px ${colColor}` }} />
  Webhook Sync URL (Optional)
</label>
                  <input type="text" value={config?.publisher_webhook_url || ''} onChange={(e) => handleUpdate('publisher_webhook_url', e.target.value)} style={{ width: '100%', background: 'rgba(20, 20, 30, 0.6)', border: '1px solid rgba(255, 255, 255, 0.1)', borderBottom: `2px solid ${colColor}`, color: '#fff', padding: '10px 14px', borderRadius: '6px', fontSize: '0.75rem', fontFamily: 'Outfit, sans-serif', outline: 'none', transition: 'all 0.2s ease', boxShadow: 'inset 0 2px 4px rgba(0,0,0,0.3)', boxSizing: 'border-box' }} />
                </div>
                <div>
                  <label style={{ color: '#fff', fontSize: '0.75rem', fontWeight: 600, letterSpacing: '0.05em', display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '8px' }}>
  <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: colColor, boxShadow: `0 0 8px ${colColor}` }} />
  Gatekeeper Rules (JSON)
</label>
                  <textarea rows={3} value={JSON.stringify(config?.publisher_gatekeeper_rules, null, 2)} onChange={(e) => {
                    try { handleUpdate('publisher_gatekeeper_rules', JSON.parse(e.target.value)) } catch (e) {}
                  }} style={{ width: '100%', background: 'rgba(20, 20, 30, 0.6)', border: '1px solid rgba(255, 255, 255, 0.1)', borderBottom: `2px solid ${colColor}`, color: '#fff', padding: '10px 14px', borderRadius: '6px', fontSize: '0.75rem', fontFamily: 'Outfit, sans-serif', outline: 'none', transition: 'all 0.2s ease', boxShadow: 'inset 0 2px 4px rgba(0,0,0,0.3)', boxSizing: 'border-box' }} />
                </div>
              </div>
            </>
          )}
        </div>
      )}

      <div style={{ marginTop: 'auto', paddingTop: '16px', display: 'flex', justifyContent: 'flex-end', borderTop: `1px solid rgba(255,255,255,0.1)` }}>
        <button onClick={handleSave} disabled={saveStatus === 'saving'} style={{ 
          background: saveStatus === 'success' ? '#4caf50' : saveStatus === 'error' ? '#f44336' : `linear-gradient(135deg, ${colColor} 0%, ${colColor}aa 100%)`, 
          color: '#fff', border: '1px solid rgba(255,255,255,0.3)', padding: '12px 24px', borderRadius: '8px', 
          cursor: saveStatus === 'saving' ? 'wait' : 'pointer', fontWeight: 800, fontFamily: 'Outfit, sans-serif', 
          letterSpacing: '0.1em', boxShadow: `0 4px 15px ${saveStatus === 'success' ? '#4caf50' : colColor}55`, transition: 'all 0.2s ease',
          fontSize: '0.85rem', textTransform: 'uppercase', textShadow: '0 1px 3px rgba(0,0,0,0.8)'
        }}
        onMouseOver={(e) => { if(saveStatus === 'idle') e.currentTarget.style.transform = 'translateY(-2px)' }}
        onMouseOut={(e) => e.currentTarget.style.transform = 'none'}
        >
          {saveStatus === 'saving' ? 'SAVING...' : saveStatus === 'success' ? '✓ SAVED!' : saveStatus === 'error' ? '✖ FAILED' : 'SAVE CONFIGURATION'}
        </button>
      </div>
    </div>
  );
};