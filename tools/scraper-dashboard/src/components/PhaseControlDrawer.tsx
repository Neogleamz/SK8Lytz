import React, { useState, useEffect } from 'react';
import { useFieldRegistry } from '../hooks/useFieldRegistry';
const API_BASE = 'http://127.0.0.1:5999';

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
  const { fields, loading: registryLoading, toggleImportance, updateFieldConfig, resetRegistry, getFieldsForPhase } = useFieldRegistry();
  const [config, setConfig] = useState<any>(null);
  const [blocklist, setBlocklist] = useState<any[]>([]);
  const [rejectedStats, setRejectedStats] = useState<any>(null);
  const [newKw, setNewKw] = useState('');
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

  const fetchRejectedStats = async () => {
    try {
      const res = await fetch(`${API_BASE}/api/rejected-stats`);
      const data = await res.json();
      setRejectedStats(data);
    } catch {}
  };

  useEffect(() => {
    if (isOpen) {
      setLoading(true);
      fetchConfig();
      if (phaseId === 1) fetchBlocklist();
      if (phaseId === 2) fetchRejectedStats();
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
                <label style={{ color: '#ff3366', display: 'block', marginBottom: '4px', fontWeight: 'bold' }}>GUILLOTINE — BLOCK & PURGE <span style={{ fontSize: '0.65rem', fontWeight: 400, color: 'rgba(255,255,255,0.4)' }}>(syncs to unified keyword list — active on all phases)</span></label>
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
              <div style={{ gridColumn: '1 / -1', background: 'rgba(20,20,30,0.5)', border: `1px solid ${colColor}33`, padding: '20px', borderRadius: '8px', marginBottom: '8px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
                  <h4 style={{ margin: 0, color: colColor, textTransform: 'uppercase', fontSize: '0.8rem', fontWeight: 800 }}>Pipeline Field Priority & Gates Manager</h4>
                  <button onClick={async () => {
                    if (confirm('Reset all field priorities, hard gates, and glows back to system skater defaults?')) {
                      await resetRegistry();
                    }
                  }} style={{ background: 'rgba(255,51,102,0.1)', border: '1px solid rgba(255,51,102,0.3)', color: '#ff3366', padding: '4px 10px', borderRadius: '4px', fontSize: '0.65rem', fontWeight: 'bold', cursor: 'pointer' }}>
                    RESET TO DEFAULTS
                  </button>
                </div>
                
                <div style={{ maxHeight: '250px', overflowY: 'auto', paddingRight: '8px' }} className="custom-scroll">
                  <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '0.7rem', color: 'rgba(255,255,255,0.8)' }}>
                    <thead>
                      <tr style={{ borderBottom: '1px solid rgba(255,255,255,0.1)', textAlign: 'left', color: 'rgba(255,255,255,0.4)' }}>
                        <th style={{ padding: '8px' }}>Field</th>
                        <th style={{ padding: '8px' }}>Priority Group (Tier)</th>
                        <th style={{ padding: '8px', textAlign: 'center' }}>Hard Gate?</th>
                        <th style={{ padding: '8px', textAlign: 'center' }}>Validation Rule</th>
                        <th style={{ padding: '8px', textAlign: 'center' }}>Visual Glow?</th>
                      </tr>
                    </thead>
                    <tbody>
                      {getFieldsForPhase(phaseId).map(f => (
                        <tr key={f.id} style={{ borderBottom: '1px solid rgba(255,255,255,0.03)', height: '36px' }}>
                          <td style={{ padding: '8px', fontWeight: 'bold', color: '#fff' }}>{f.field_name}</td>
                          <td style={{ padding: '8px' }}>
                            <select 
                              value={f.priority_group || 10} 
                              onChange={(e) => updateFieldConfig(f.id, { priority_group: parseInt(e.target.value) })}
                              style={{ background: '#111', border: '1px solid rgba(255,255,255,0.1)', color: '#fff', fontSize: '0.65rem', padding: '2px 6px', borderRadius: '4px' }}
                            >
                              {[
                                { v: 0, l: 'T0: Phase 1 (Seeded)' },
                                { v: 1, l: 'T1: 🕐 Session Hours' },
                                { v: 2, l: 'T2: 💰 Pricing & Fees' },
                                { v: 3, l: 'T3: 🌙 Adult Night' },
                                { v: 4, l: 'T4: 🛹 Floor & Vibe' },
                                { v: 5, l: 'T5: 🏢 Amenities' },
                                { v: 6, l: 'T6: 🎭 Identity & Culture' },
                                { v: 7, l: 'T7: 📱 Contacts & Socials' },
                                { v: 10, l: 'T10: Unassigned' },
                              ].map(t => (
                                <option key={t.v} value={t.v}>{t.l}</option>
                              ))}
                            </select>
                          </td>
                          <td style={{ padding: '8px', textAlign: 'center' }}>
                            <input 
                              type="checkbox" 
                              checked={f.is_hard_gate === 1}
                              onChange={(e) => updateFieldConfig(f.id, { is_hard_gate: e.target.checked ? 1 : 0 })}
                              style={{ cursor: 'pointer', accentColor: colColor }}
                            />
                          </td>
                          <td style={{ padding: '8px', textAlign: 'center' }}>
                            <select 
                              value={f.validation_rule || 'NONE'} 
                              onChange={(e) => updateFieldConfig(f.id, { validation_rule: e.target.value })}
                              style={{ background: '#111', border: '1px solid rgba(255,255,255,0.1)', color: '#fff', fontSize: '0.65rem', padding: '2px 6px', borderRadius: '4px', maxWidth: '90px' }}
                            >
                              <option value="NONE">NONE</option>
                              <option value="PHONE">PHONE</option>
                              <option value="URL">URL</option>
                              <option value="JSON_ARRAY">JSON_ARRAY</option>
                              <option value="STATE_CODE">STATE_CODE</option>
                              <option value="ZIP_CODE">ZIP_CODE</option>
                            </select>
                          </td>
                          <td style={{ padding: '8px', textAlign: 'center' }}>
                            <input 
                              type="checkbox" 
                              checked={f.visual_glow === 1}
                              onChange={(e) => updateFieldConfig(f.id, { visual_glow: e.target.checked ? 1 : 0 })}
                              style={{ cursor: 'pointer', accentColor: colColor }}
                            />
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
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
                  {/* ── Toxicity Bouncer & Guillotine Panel ── */}
                  <div style={{ background: 'rgba(255,20,20,0.06)', border: '1px solid rgba(255,51,51,0.3)', borderRadius: '8px', padding: '14px', display: 'flex', flexDirection: 'column', gap: '10px' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                        <span style={{ fontSize: '1rem' }}>☠️</span>
                        <span style={{ color: '#ff4444', fontWeight: 800, fontSize: '0.75rem', textTransform: 'uppercase', letterSpacing: '0.08em' }}>Toxicity Bouncer & Guillotine</span>
                        <span style={{ background: 'rgba(255,68,68,0.15)', border: '1px solid rgba(255,68,68,0.4)', color: '#ff6666', padding: '2px 8px', borderRadius: '10px', fontSize: '0.65rem', fontWeight: 700 }}>{(config?.ai_exclusion_keywords || []).length} keywords</span>
                        {rejectedStats && <span style={{ background: 'rgba(255,68,68,0.15)', border: '1px solid rgba(255,68,68,0.4)', color: '#ff8888', padding: '2px 8px', borderRadius: '10px', fontSize: '0.65rem' }}>☠ {rejectedStats.total} rejected</span>}
                      </div>
                      <button onClick={async () => {
                        if (!confirm('Restore all default exclusion keywords? Your custom additions will be preserved.')) return;
                        const res = await fetch(`${API_BASE}/config`);
                        const d = await res.json();
                        const defaults = ['ice rink','ice skating','ice skating rink','ice arena','ice complex','ice palace','ice center','ice centre','ice sport','hockey rink','hockey arena','hockey complex','curling','curling club','curling rink','trampoline park','bounce house','jump zone','sky zone','altitude trampoline','bmx','bike park','mountain bike park','laser tag','mini golf','go-kart','go kart','bowling alley'];
                        const existing: string[] = d.config?.ai_exclusion_keywords || [];
                        const seen = new Set(existing.map((k: string) => k.toLowerCase()));
                        const merged = [...existing, ...defaults.filter(k => !seen.has(k.toLowerCase()))];
                        handleUpdate('ai_exclusion_keywords', merged);
                      }} style={{ background: 'rgba(255,68,68,0.1)', border: '1px solid rgba(255,68,68,0.3)', color: '#ff6666', padding: '3px 10px', borderRadius: '4px', fontSize: '0.6rem', fontWeight: 700, cursor: 'pointer', letterSpacing: '0.05em' }}>⟳ RELOAD DEFAULTS</button>
                    </div>

                    {/* Categorized keyword chips */}
                    {(() => {
                      const kws: string[] = config?.ai_exclusion_keywords || [];
                      const cats: {label: string; emoji: string; patterns: string[]}[] = [
                        { label: 'Ice / Hockey', emoji: '🧊', patterns: ['ice','hockey','curling','iceplex','polar','figure skat'] },
                        { label: 'Bikes / BMX', emoji: '🚲', patterns: ['bmx','bike','bicycle','mountain bike'] },
                        { label: 'Trampoline', emoji: '🤸', patterns: ['trampoline','bounce','jump zone','sky zone','altitude'] },
                        { label: 'Other', emoji: '🎮', patterns: ['laser','golf','kart','bowling','bowling alley'] },
                      ];
                      return cats.map(cat => {
                        const catKws = kws.filter(k => cat.patterns.some(p => k.toLowerCase().includes(p)));
                        const uncatKws = cat.label === 'Other' ? kws.filter(k => !cats.slice(0,3).flatMap(c => c.patterns).some(p => k.toLowerCase().includes(p))) : catKws;
                        const displayKws = cat.label === 'Other' ? uncatKws : catKws;
                        if (displayKws.length === 0) return null;
                        return (
                          <div key={cat.label}>
                            <div style={{ color: 'rgba(255,255,255,0.4)', fontSize: '0.6rem', fontWeight: 600, marginBottom: '4px', textTransform: 'uppercase', letterSpacing: '0.06em' }}>{cat.emoji} {cat.label}</div>
                            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '5px' }}>
                              {displayKws.map(k => (
                                <div key={k} style={{ display: 'flex', alignItems: 'center', gap: '4px', background: 'rgba(255,40,40,0.1)', border: '1px solid rgba(255,40,40,0.25)', padding: '3px 8px', borderRadius: '12px' }}>
                                  <span style={{ color: '#ff8888', fontSize: '0.65rem' }}>{k}</span>
                                  <button onClick={() => handleUpdate('ai_exclusion_keywords', kws.filter(x => x !== k))} style={{ background: 'none', border: 'none', color: 'rgba(255,100,100,0.6)', cursor: 'pointer', fontSize: '0.7rem', padding: 0, lineHeight: 1 }}>×</button>
                                </div>
                              ))}
                            </div>
                          </div>
                        );
                      });
                    })()}

                    {/* Add keyword input */}
                    <div style={{ display: 'flex', gap: '8px', marginTop: '4px' }}>
                      <input value={newKw} onChange={e => setNewKw(e.target.value)}
                        placeholder="Add exclusion keyword (e.g. 'ice palace')..."
                        onKeyDown={e => { if (e.key === 'Enter' && newKw.trim()) { const kws = config?.ai_exclusion_keywords || []; if (!kws.includes(newKw.trim())) handleUpdate('ai_exclusion_keywords', [...kws, newKw.trim().toLowerCase()]); setNewKw(''); } }}
                        style={{ flex: 1, background: 'rgba(10,0,0,0.4)', border: '1px solid rgba(255,40,40,0.3)', borderBottom: '2px solid #ff4444', color: '#fff', padding: '8px 12px', borderRadius: '6px', fontSize: '0.72rem', outline: 'none', fontFamily: 'Outfit, sans-serif' }} />
                      <button onClick={() => { if (newKw.trim()) { const kws = config?.ai_exclusion_keywords || []; if (!kws.includes(newKw.trim())) handleUpdate('ai_exclusion_keywords', [...kws, newKw.trim().toLowerCase()]); setNewKw(''); } }}
                        style={{ background: 'linear-gradient(135deg, #ff3333 0%, #cc0000 100%)', color: '#fff', border: 'none', padding: '0 16px', borderRadius: '6px', cursor: 'pointer', fontWeight: 800, fontSize: '0.7rem', letterSpacing: '0.05em' }}>+ ADD</button>
                    </div>

                    {/* Rejection audit log */}
                    {rejectedStats?.recent?.length > 0 && (
                      <div style={{ marginTop: '4px', borderTop: '1px solid rgba(255,40,40,0.15)', paddingTop: '10px' }}>
                        <div style={{ color: 'rgba(255,255,255,0.35)', fontSize: '0.6rem', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.06em', marginBottom: '6px' }}>Recent Rejections</div>
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '4px', maxHeight: '120px', overflowY: 'auto' }}>
                          {rejectedStats.recent.map((r: any, i: number) => (
                            <div key={i} style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '0.65rem', color: 'rgba(255,255,255,0.5)', padding: '3px 6px', background: 'rgba(255,0,0,0.05)', borderRadius: '4px' }}>
                              <span style={{ color: '#ff5555' }}>☠</span>
                              <span style={{ color: 'rgba(255,255,255,0.75)', fontWeight: 600 }}>{r.name}</span>
                              <span>·</span><span>{r.city}, {r.state}</span>
                              <span>·</span><span style={{ color: '#ff7777', fontStyle: 'italic' }}>"{r.reason}"</span>
                            </div>
                          ))}
                        </div>
                      </div>
                    )}
                  </div>
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
                          <h5 style={{ margin: '0 0 6px 0', color: colColor, fontSize: '0.65rem', textTransform: 'uppercase' }}>LM Studio JSON Output</h5>
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

          {/* Phase 4: PUBLISHER — 3-Pass Pipeline */}
          {phaseId === 4 && (
            <>
              {/* Data Acquisition Mapping (same as other phases) */}
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

              {/* ═══════════════════════════════════════════════════════════════
                  PASS 1: VISION LLM — Photo Curation & Scoring
                  ═══════════════════════════════════════════════════════════════ */}
              <div style={{ gridColumn: '1 / -1', background: 'linear-gradient(135deg, rgba(168,85,247,0.08) 0%, rgba(139,92,246,0.04) 100%)', border: '1px solid rgba(168,85,247,0.35)', borderRadius: '10px', padding: '16px', display: 'flex', flexDirection: 'column', gap: '12px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                    <div style={{ background: 'linear-gradient(135deg, #a855f7 0%, #7c3aed 100%)', width: '28px', height: '28px', borderRadius: '8px', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '0.85rem', boxShadow: '0 0 12px rgba(168,85,247,0.4)' }}>👁️</div>
                    <div>
                      <div style={{ color: '#a855f7', fontWeight: 800, fontSize: '0.75rem', textTransform: 'uppercase', letterSpacing: '0.1em' }}>Pass 1 — Vision LLM</div>
                      <div style={{ color: 'rgba(255,255,255,0.4)', fontSize: '0.6rem' }}>AI photo curation, category tagging & quality scoring</div>
                    </div>
                  </div>
                  <ToggleSwitch label="" checked={config?.publisher_vision_enabled ?? true} onChange={(v) => handleUpdate('publisher_vision_enabled', v)} colColor="#a855f7" />
                </div>

                {config?.publisher_vision_enabled !== false && (
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                    {/* Row 1: Model ID + Temperature + Min Score */}
                    <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr 1fr', gap: '10px' }}>
                      <div>
                        <label style={{ color: 'rgba(255,255,255,0.6)', fontSize: '0.65rem', fontWeight: 600, marginBottom: '4px', display: 'block' }}>Vision Model ID</label>
                        <input type="text" value={config?.publisher_vision_model || 'qwen2.5-vl-7b-instruct'} onChange={(e) => handleUpdate('publisher_vision_model', e.target.value)} style={{ width: '100%', background: 'rgba(10,5,20,0.6)', border: '1px solid rgba(168,85,247,0.25)', borderBottom: '2px solid #a855f7', color: '#fff', padding: '8px 12px', borderRadius: '6px', fontSize: '0.72rem', fontFamily: 'Outfit, sans-serif', outline: 'none', boxSizing: 'border-box' }} />
                      </div>
                      <div>
                        <label style={{ color: 'rgba(255,255,255,0.6)', fontSize: '0.65rem', fontWeight: 600, marginBottom: '4px', display: 'block' }}>Temperature</label>
                        <input type="number" step={0.1} min={0} max={2} value={config?.publisher_vision_temperature ?? 0.1} onChange={(e) => handleUpdate('publisher_vision_temperature', parseFloat(e.target.value))} style={{ width: '100%', background: 'rgba(10,5,20,0.6)', border: '1px solid rgba(168,85,247,0.25)', borderBottom: '2px solid #a855f7', color: '#fff', padding: '8px 12px', borderRadius: '6px', fontSize: '0.72rem', fontFamily: 'Outfit, sans-serif', outline: 'none', boxSizing: 'border-box' }} />
                      </div>
                      <div>
                        <label style={{ color: 'rgba(255,255,255,0.6)', fontSize: '0.65rem', fontWeight: 600, marginBottom: '4px', display: 'block' }}>Min Score (1-10)</label>
                        <input type="number" min={1} max={10} value={config?.publisher_vision_min_score ?? 3} onChange={(e) => handleUpdate('publisher_vision_min_score', parseInt(e.target.value))} style={{ width: '100%', background: 'rgba(10,5,20,0.6)', border: '1px solid rgba(168,85,247,0.25)', borderBottom: '2px solid #a855f7', color: '#fff', padding: '8px 12px', borderRadius: '6px', fontSize: '0.72rem', fontFamily: 'Outfit, sans-serif', outline: 'none', boxSizing: 'border-box' }} />
                      </div>
                    </div>

                    {/* Row 2: System Prompt */}
                    <div>
                      <label style={{ color: 'rgba(255,255,255,0.6)', fontSize: '0.65rem', fontWeight: 600, marginBottom: '4px', display: 'flex', alignItems: 'center', gap: '6px' }}>
                        <span style={{ color: '#a855f7' }}>🧠</span> Vision Analysis Prompt
                        <span style={{ color: 'rgba(255,255,255,0.3)', fontWeight: 400, fontStyle: 'italic' }}>— tells the LLM exactly what to look for</span>
                      </label>
                      <textarea
                        rows={6}
                        value={config?.publisher_vision_prompt || `You are a photo analyst for a roller skating rink directory. Analyze this image and classify it.\n\nRespond with ONLY valid JSON:\n{\n  "category": "exterior|interior|floor|pro_shop|action|logo|flyer|reject",\n  "score": 1-10\n}\n\nCategories:\n- exterior: Outside view of the building, parking lot, entrance, signage\n- interior: Inside the rink, seating areas, arcade, party rooms\n- floor: The skating surface itself (wood, concrete, sport court)\n- pro_shop: Retail area selling skates, gear, accessories\n- action: Real people actively skating at THIS rink (not stock photos)\n- logo: Business logo, branding, mascot\n- flyer: Event poster, schedule, promotional material, menu\n- reject: Blurry, irrelevant, stock photo, unrelated to skating\n\nScore guide: 10=perfect hero shot, 7-9=great quality, 4-6=acceptable, 1-3=poor quality`}
                        onChange={(e) => handleUpdate('publisher_vision_prompt', e.target.value)}
                        style={{ width: '100%', background: 'rgba(10,5,20,0.6)', border: '1px solid rgba(168,85,247,0.25)', borderBottom: '2px solid #a855f7', color: '#fff', padding: '10px 14px', borderRadius: '6px', fontSize: '0.7rem', fontFamily: "'Fira Code', 'Consolas', monospace", outline: 'none', boxSizing: 'border-box', lineHeight: '1.5', resize: 'vertical' }}
                      />
                    </div>

                    {/* Row 3: Active Categories (toggleable) */}
                    <div>
                      <label style={{ color: 'rgba(255,255,255,0.6)', fontSize: '0.65rem', fontWeight: 600, marginBottom: '6px', display: 'block' }}>Active Categories (click to toggle)</label>
                      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px' }}>
                        {['exterior', 'interior', 'floor', 'pro_shop', 'action', 'logo', 'flyer'].map(cat => {
                          const activeCats: string[] = config?.publisher_vision_categories || ['exterior', 'interior', 'floor', 'pro_shop', 'action', 'logo', 'flyer'];
                          const isActive = activeCats.includes(cat);
                          const emoji = cat === 'exterior' ? '🏢' : cat === 'interior' ? '🏟️' : cat === 'floor' ? '🛹' : cat === 'pro_shop' ? '🛒' : cat === 'action' ? '⚡' : cat === 'logo' ? '🎨' : '📄';
                          return (
                            <button
                              key={cat}
                              onClick={() => {
                                const next = isActive ? activeCats.filter(c => c !== cat) : [...activeCats, cat];
                                handleUpdate('publisher_vision_categories', next);
                              }}
                              style={{
                                background: isActive ? 'rgba(168,85,247,0.2)' : 'rgba(255,255,255,0.03)',
                                border: `1px solid ${isActive ? 'rgba(168,85,247,0.5)' : 'rgba(255,255,255,0.1)'}`,
                                color: isActive ? '#c4b5fd' : 'rgba(255,255,255,0.3)',
                                padding: '4px 12px', borderRadius: '14px', fontSize: '0.65rem', fontWeight: 600,
                                cursor: 'pointer', transition: 'all 0.15s ease',
                                textDecoration: isActive ? 'none' : 'line-through',
                              }}
                            >
                              {emoji} {cat}
                            </button>
                          );
                        })}
                        <span style={{ background: 'rgba(255,60,60,0.12)', border: '1px solid rgba(255,60,60,0.3)', color: '#f87171', padding: '4px 12px', borderRadius: '14px', fontSize: '0.65rem', fontWeight: 600 }}>
                          🚫 reject (always active)
                        </span>
                      </div>
                    </div>
                  </div>
                )}
              </div>

              {/* ═══════════════════════════════════════════════════════════════
                  PASS 2: GUILLOTINE — Validation & Auto-Publish Gates
                  ═══════════════════════════════════════════════════════════════ */}
              <div style={{ gridColumn: '1 / -1', background: 'linear-gradient(135deg, rgba(245,158,11,0.08) 0%, rgba(234,88,12,0.04) 100%)', border: '1px solid rgba(245,158,11,0.35)', borderRadius: '10px', padding: '16px', display: 'flex', flexDirection: 'column', gap: '12px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                    <div style={{ background: 'linear-gradient(135deg, #f59e0b 0%, #ea580c 100%)', width: '28px', height: '28px', borderRadius: '8px', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '0.85rem', boxShadow: '0 0 12px rgba(245,158,11,0.4)' }}>⚖️</div>
                    <div>
                      <div style={{ color: '#f59e0b', fontWeight: 800, fontSize: '0.75rem', textTransform: 'uppercase', letterSpacing: '0.1em' }}>Pass 2 — Guillotine</div>
                      <div style={{ color: 'rgba(255,255,255,0.4)', fontSize: '0.6rem' }}>Field validation, required data gates & auto-publish rules</div>
                    </div>
                  </div>
                  <ToggleSwitch label="" checked={config?.publisher_auto_publish_enabled} onChange={(v) => handleUpdate('publisher_auto_publish_enabled', v)} colColor="#f59e0b" />
                </div>

                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '14px' }}>
                  {/* Left: Required Fields Gate — ALL PHASES */}
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                    <div style={{ color: '#fbbf24', fontWeight: 700, fontSize: '0.7rem', textTransform: 'uppercase', letterSpacing: '0.06em', display: 'flex', alignItems: 'center', gap: '6px' }}>
                      <span>📋</span> Required Fields
                      <span style={{ background: 'rgba(245,158,11,0.15)', border: '1px solid rgba(245,158,11,0.3)', color: '#fbbf24', padding: '1px 8px', borderRadius: '10px', fontSize: '0.6rem', fontWeight: 700 }}>
                        {fields.filter(f => f.is_hard_gate === 1).length} gates
                      </span>
                    </div>
                    <div style={{ background: 'rgba(10,5,0,0.3)', border: '1px solid rgba(245,158,11,0.15)', borderRadius: '6px', padding: '10px', maxHeight: '320px', overflowY: 'auto', display: 'flex', flexDirection: 'column', gap: '2px' }}>
                      {(() => {
                        const phaseNames: Record<number, { label: string; emoji: string }> = {
                          1: { label: 'Scout', emoji: '🔍' },
                          2: { label: 'Detective', emoji: '🕵️' },
                          3: { label: 'Photographer', emoji: '📸' },
                          4: { label: 'Publisher', emoji: '🚀' },
                        };
                        const phaseIds = [...new Set(fields.map(f => f.phase_id))].sort();
                        return phaseIds.map(pid => {
                          const phaseFields = fields.filter(f => f.phase_id === pid);
                          if (phaseFields.length === 0) return null;
                          const info = phaseNames[pid] || { label: `Phase ${pid}`, emoji: '⚙️' };
                          return (
                            <div key={pid} style={{ marginBottom: '6px' }}>
                              <div style={{ color: 'rgba(255,255,255,0.35)', fontSize: '0.58rem', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.08em', padding: '4px 0 2px 0', borderBottom: '1px solid rgba(255,255,255,0.06)', marginBottom: '3px' }}>
                                {info.emoji} {info.label}
                              </div>
                              {phaseFields.map(f => (
                                <label key={f.id || f.field_name} style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer', padding: '3px 6px', borderRadius: '4px', background: f.is_hard_gate === 1 ? 'rgba(245,158,11,0.08)' : 'transparent', transition: 'background 0.15s' }}>
                                  <input
                                    type="checkbox"
                                    checked={f.is_hard_gate === 1}
                                    onChange={(e) => updateFieldConfig(f.id, { is_hard_gate: e.target.checked ? 1 : 0 })}
                                    style={{ accentColor: '#f59e0b', cursor: 'pointer' }}
                                  />
                                  <span style={{ color: f.is_hard_gate === 1 ? '#fbbf24' : 'rgba(255,255,255,0.5)', fontSize: '0.68rem', fontWeight: f.is_hard_gate === 1 ? 700 : 400 }}>{f.field_name}</span>
                                  {f.validation_rule && f.validation_rule !== 'NONE' && (
                                    <span style={{ background: 'rgba(245,158,11,0.12)', color: '#fbbf24', padding: '1px 6px', borderRadius: '8px', fontSize: '0.55rem', fontWeight: 700 }}>{f.validation_rule}</span>
                                  )}
                                </label>
                              ))}
                            </div>
                          );
                        });
                      })()}
                    </div>
                  </div>

                  {/* Right: Required Photo Tags Gate */}
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                    <div style={{ color: '#fbbf24', fontWeight: 700, fontSize: '0.7rem', textTransform: 'uppercase', letterSpacing: '0.06em', display: 'flex', alignItems: 'center', gap: '6px' }}>
                      <span>📸</span> Required Photo Tags
                      <span style={{ background: 'rgba(245,158,11,0.15)', border: '1px solid rgba(245,158,11,0.3)', color: '#fbbf24', padding: '1px 8px', borderRadius: '10px', fontSize: '0.6rem', fontWeight: 700 }}>
                        {(config?.publisher_required_photo_tags || []).length} required
                      </span>
                    </div>
                    <div style={{ background: 'rgba(10,5,0,0.3)', border: '1px solid rgba(245,158,11,0.15)', borderRadius: '6px', padding: '10px', display: 'flex', flexDirection: 'column', gap: '6px' }}>
                      {['exterior', 'interior', 'floor', 'pro_shop', 'action', 'logo', 'flyer'].map(tag => {
                        const requiredTags: string[] = config?.publisher_required_photo_tags || [];
                        const isChecked = requiredTags.includes(tag);
                        const emoji = tag === 'exterior' ? '🏢' : tag === 'interior' ? '🏟️' : tag === 'floor' ? '🛹' : tag === 'pro_shop' ? '🛒' : tag === 'action' ? '⚡' : tag === 'logo' ? '🎨' : '📄';
                        return (
                          <label key={tag} style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer', padding: '4px 8px', borderRadius: '6px', background: isChecked ? 'rgba(245,158,11,0.08)' : 'transparent', transition: 'background 0.15s' }}>
                            <input
                              type="checkbox"
                              checked={isChecked}
                              onChange={() => {
                                const next = isChecked ? requiredTags.filter(t => t !== tag) : [...requiredTags, tag];
                                handleUpdate('publisher_required_photo_tags', next);
                              }}
                              style={{ accentColor: '#f59e0b', cursor: 'pointer' }}
                            />
                            <span style={{ fontSize: '0.75rem' }}>{emoji}</span>
                            <span style={{ color: isChecked ? '#fbbf24' : 'rgba(255,255,255,0.5)', fontSize: '0.7rem', fontWeight: isChecked ? 700 : 400, textTransform: 'capitalize' }}>{tag.replace('_', ' ')}</span>
                          </label>
                        );
                      })}
                    </div>

                    <div>
                      <label style={{ color: 'rgba(255,255,255,0.6)', fontSize: '0.65rem', fontWeight: 600, marginBottom: '4px', display: 'block' }}>Auto-Publish Threshold (%)</label>
                      <input type="number" min={0} max={100} value={config?.publisher_auto_publish_threshold || 80} onChange={(e) => handleUpdate('publisher_auto_publish_threshold', parseInt(e.target.value))} style={{ width: '100%', background: 'rgba(10,5,0,0.4)', border: '1px solid rgba(245,158,11,0.25)', borderBottom: '2px solid #f59e0b', color: '#fff', padding: '8px 12px', borderRadius: '6px', fontSize: '0.72rem', fontFamily: 'Outfit, sans-serif', outline: 'none', boxSizing: 'border-box' }} />
                    </div>
                  </div>
                </div>
              </div>

              {/* ═══════════════════════════════════════════════════════════════
                  PASS 3: UPLINK — Sync to Production
                  ═══════════════════════════════════════════════════════════════ */}
              <div style={{ gridColumn: '1 / -1', background: 'linear-gradient(135deg, rgba(34,197,94,0.08) 0%, rgba(16,185,129,0.04) 100%)', border: '1px solid rgba(34,197,94,0.35)', borderRadius: '10px', padding: '16px', display: 'flex', flexDirection: 'column', gap: '12px' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                  <div style={{ background: 'linear-gradient(135deg, #22c55e 0%, #10b981 100%)', width: '28px', height: '28px', borderRadius: '8px', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '0.85rem', boxShadow: '0 0 12px rgba(34,197,94,0.4)' }}>🚀</div>
                  <div>
                    <div style={{ color: '#22c55e', fontWeight: 800, fontSize: '0.75rem', textTransform: 'uppercase', letterSpacing: '0.1em' }}>Pass 3 — Uplink</div>
                    <div style={{ color: 'rgba(255,255,255,0.4)', fontSize: '0.6rem' }}>Sync validated records to Supabase production</div>
                  </div>
                </div>

                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '10px' }}>
                  <div>
                    <label style={{ color: 'rgba(255,255,255,0.6)', fontSize: '0.65rem', fontWeight: 600, marginBottom: '4px', display: 'block' }}>Upsert Strategy</label>
                    <select value={config?.publisher_upsert_strategy || 'merge'} onChange={(e) => handleUpdate('publisher_upsert_strategy', e.target.value)} style={{ width: '100%', background: 'rgba(5,15,5,0.6)', border: '1px solid rgba(34,197,94,0.25)', borderBottom: '2px solid #22c55e', color: '#fff', padding: '8px 12px', borderRadius: '6px', fontSize: '0.72rem', fontFamily: 'Outfit, sans-serif', outline: 'none', boxSizing: 'border-box' }}>
                      <option value="merge" style={{ background: '#111' }}>MERGE (Preserve existing)</option>
                      <option value="overwrite" style={{ background: '#111' }}>OVERWRITE (Force update)</option>
                    </select>
                  </div>
                  <div>
                    <label style={{ color: 'rgba(255,255,255,0.6)', fontSize: '0.65rem', fontWeight: 600, marginBottom: '4px', display: 'block' }}>Webhook Sync URL (Optional)</label>
                    <input type="text" value={config?.publisher_webhook_url || ''} onChange={(e) => handleUpdate('publisher_webhook_url', e.target.value)} placeholder="https://hooks.example.com/..." style={{ width: '100%', background: 'rgba(5,15,5,0.6)', border: '1px solid rgba(34,197,94,0.25)', borderBottom: '2px solid #22c55e', color: '#fff', padding: '8px 12px', borderRadius: '6px', fontSize: '0.72rem', fontFamily: 'Outfit, sans-serif', outline: 'none', boxSizing: 'border-box' }} />
                  </div>
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
