import React, { useState, useEffect } from 'react';

interface RecordEditModalProps {
  spot: any;
  onSave: (updates: any) => Promise<void>;
  onClose: () => void;
}

export const RecordEditModal: React.FC<RecordEditModalProps> = ({ spot, onSave, onClose }) => {
  const [tab, setTab] = useState<'basic' | 'json'>('basic');
  const [form, setForm] = useState<any>({});
  const [jsonText, setJsonText] = useState<{ [key: string]: string }>({});
  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  const [isSaving, setIsSaving] = useState(false);

  useEffect(() => {
    setForm({ ...spot });
    setJsonText({
      opening_hours: spot.opening_hours ? JSON.stringify(spot.opening_hours, null, 2) : '',
      pricing_data: spot.pricing_data ? JSON.stringify(spot.pricing_data, null, 2) : '',
      social_links: spot.social_links ? JSON.stringify(spot.social_links, null, 2) : '',
      raw_knowledge_panel: spot.raw_knowledge_panel ? JSON.stringify(spot.raw_knowledge_panel, null, 2) : '',
    });
  }, [spot]);

  const handleBasicChange = (field: string, val: any) => {
    setForm((prev: any) => ({ ...prev, [field]: val }));
  };

  const handleJsonChange = (field: string, val: string) => {
    setJsonText(prev => ({ ...prev, [field]: val }));
    setErrorMsg(null);
  };

  const attemptSave = async () => {
    setErrorMsg(null);
    setIsSaving(true);
    
    const updates = { ...form };
    let hasError = false;

    // Validate JSON fields
    ['opening_hours', 'pricing_data', 'social_links', 'raw_knowledge_panel'].forEach(field => {
      const text = jsonText[field];
      if (!text || text.trim() === '') {
        updates[field] = null;
      } else {
        try {
          updates[field] = JSON.parse(text);
        } catch (e: any) {
          setErrorMsg(`Invalid JSON in ${field}: ${e.message}`);
          hasError = true;
        }
      }
    });

    if (hasError) {
      setIsSaving(false);
      return;
    }

    try {
      await onSave(updates);
      onClose();
    } catch (e: any) {
      setErrorMsg(`Save failed: ${e.message}`);
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <div style={{
      position: 'fixed', top: 0, left: 0, right: 0, bottom: 0,
      background: 'rgba(0,0,0,0.85)', backdropFilter: 'blur(4px)',
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      zIndex: 9999, fontFamily: 'Outfit, sans-serif'
    }}>
      <div style={{
        background: '#151921', border: '1px solid rgba(255,255,255,0.1)',
        borderRadius: '12px', width: '900px', maxWidth: '95vw', height: '80vh',
        display: 'flex', flexDirection: 'column', overflow: 'hidden',
        boxShadow: '0 20px 40px rgba(0,0,0,0.5)'
      }}>
        {/* HEADER */}
        <div style={{ padding: '1rem 1.5rem', borderBottom: '1px solid rgba(255,255,255,0.08)', display: 'flex', alignItems: 'center', justifyContent: 'space-between', background: '#1c212b' }}>
          <div>
            <h2 style={{ margin: 0, fontSize: '1.2rem', fontWeight: 800, color: '#fff' }}>Edit Record: {spot.name}</h2>
            <div style={{ fontSize: '0.7rem', color: 'rgba(255,255,255,0.4)', fontFamily: 'JetBrains Mono, monospace', marginTop: '4px' }}>{spot.id}</div>
          </div>
          <button onClick={onClose} style={{ background: 'transparent', border: 'none', color: 'rgba(255,255,255,0.5)', fontSize: '1.5rem', cursor: 'pointer' }}>&times;</button>
        </div>

        {/* TABS */}
        <div style={{ display: 'flex', borderBottom: '1px solid rgba(255,255,255,0.08)', background: '#11141a' }}>
          <button 
            onClick={() => setTab('basic')}
            style={{ flex: 1, padding: '10px', background: tab === 'basic' ? 'rgba(255,255,255,0.05)' : 'transparent', border: 'none', borderBottom: tab === 'basic' ? '2px solid #60a5fa' : '2px solid transparent', color: tab === 'basic' ? '#60a5fa' : 'rgba(255,255,255,0.4)', fontWeight: 800, cursor: 'pointer', transition: 'all 0.2s' }}
          >
            BASIC INFO
          </button>
          <button 
            onClick={() => setTab('json')}
            style={{ flex: 1, padding: '10px', background: tab === 'json' ? 'rgba(255,255,255,0.05)' : 'transparent', border: 'none', borderBottom: tab === 'json' ? '2px solid #f43f5e' : '2px solid transparent', color: tab === 'json' ? '#f43f5e' : 'rgba(255,255,255,0.4)', fontWeight: 800, cursor: 'pointer', transition: 'all 0.2s' }}
          >
            JSON DATA & META
          </button>
        </div>

        {/* CONTENT */}
        <div style={{ flex: 1, overflowY: 'auto', padding: '1.5rem', background: '#0B0D11' }}>
          
          {tab === 'basic' && (
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem' }}>
              <div>
                <label style={{ display: 'block', fontSize: '0.7rem', fontWeight: 800, color: 'rgba(255,255,255,0.4)', marginBottom: '5px' }}>NAME</label>
                <input value={form.name || ''} onChange={e => handleBasicChange('name', e.target.value)} style={{ width: '100%', padding: '8px 12px', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '6px', color: '#fff' }} />
              </div>
              <div>
                <label style={{ display: 'block', fontSize: '0.7rem', fontWeight: 800, color: 'rgba(255,255,255,0.4)', marginBottom: '5px' }}>VERIFICATION STATUS</label>
                <select value={form.verification_status || 'PENDING'} onChange={e => handleBasicChange('verification_status', e.target.value)} style={{ width: '100%', padding: '8px 12px', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '6px', color: '#fff' }}>
                  <option value="SEEDED">SEEDED</option>
                  <option value="DEEP_CRAWLED">DEEP_CRAWLED</option>
                  <option value="MEDIA_READY">MEDIA_READY</option>
                  <option value="REJECTED">REJECTED (Graveyard)</option>
                  <option value="DEPRECATED">DEPRECATED</option>
                </select>
              </div>
              
              <div style={{ gridColumn: '1 / -1', display: 'grid', gridTemplateColumns: '2fr 1fr', gap: '1.5rem' }}>
                <div>
                  <label style={{ display: 'block', fontSize: '0.7rem', fontWeight: 800, color: 'rgba(255,255,255,0.4)', marginBottom: '5px' }}>STREET ADDRESS</label>
                  <input value={form.street_address || ''} onChange={e => handleBasicChange('street_address', e.target.value)} style={{ width: '100%', padding: '8px 12px', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '6px', color: '#fff' }} />
                </div>
                <div>
                  <label style={{ display: 'block', fontSize: '0.7rem', fontWeight: 800, color: 'rgba(255,255,255,0.4)', marginBottom: '5px' }}>CITY</label>
                  <input value={form.city || ''} onChange={e => handleBasicChange('city', e.target.value)} style={{ width: '100%', padding: '8px 12px', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '6px', color: '#fff' }} />
                </div>
              </div>

              <div>
                <label style={{ display: 'block', fontSize: '0.7rem', fontWeight: 800, color: 'rgba(255,255,255,0.4)', marginBottom: '5px' }}>WEBSITE</label>
                <input value={form.website || ''} onChange={e => handleBasicChange('website', e.target.value)} style={{ width: '100%', padding: '8px 12px', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '6px', color: '#fff' }} />
              </div>
              <div>
                <label style={{ display: 'block', fontSize: '0.7rem', fontWeight: 800, color: 'rgba(255,255,255,0.4)', marginBottom: '5px' }}>PHONE NUMBER</label>
                <input value={form.phone_number || ''} onChange={e => handleBasicChange('phone_number', e.target.value)} style={{ width: '100%', padding: '8px 12px', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '6px', color: '#fff' }} />
              </div>

              <div>
                <label style={{ display: 'block', fontSize: '0.7rem', fontWeight: 800, color: 'rgba(255,255,255,0.4)', marginBottom: '5px' }}>SURFACE QUALITY</label>
                <input value={form.surface_quality || ''} onChange={e => handleBasicChange('surface_quality', e.target.value)} style={{ width: '100%', padding: '8px 12px', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '6px', color: '#fff' }} placeholder="e.g. Buttery, Rough" />
              </div>
              <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', marginTop: '1rem' }}>
                <label style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '0.8rem', color: '#fff', cursor: 'pointer' }}>
                  <input type="checkbox" checked={form.has_adult_night || false} onChange={e => handleBasicChange('has_adult_night', e.target.checked)} style={{ width: '16px', height: '16px', accentColor: '#4caf50' }} />
                  18+ ADULT NIGHT
                </label>
                <label style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '0.8rem', color: '#fff', cursor: 'pointer' }}>
                  <input type="checkbox" checked={form.is_published || false} onChange={e => handleBasicChange('is_published', e.target.checked)} style={{ width: '16px', height: '16px', accentColor: '#4caf50' }} />
                  APP PUBLISHED
                </label>
              </div>
            </div>
          )}

          {tab === 'json' && (
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem' }}>
              {['opening_hours', 'pricing_data', 'social_links', 'raw_knowledge_panel'].map(field => (
                <div key={field} style={{ display: 'flex', flexDirection: 'column' }}>
                  <label style={{ display: 'block', fontSize: '0.7rem', fontWeight: 800, color: '#f43f5e', marginBottom: '5px', textTransform: 'uppercase' }}>{field.replace('_', ' ')} (JSON)</label>
                  <textarea
                    value={jsonText[field]}
                    onChange={e => handleJsonChange(field, e.target.value)}
                    style={{ 
                      width: '100%', height: '180px', padding: '10px', 
                      background: 'rgba(0,0,0,0.3)', border: '1px solid rgba(244,63,94,0.3)', 
                      borderRadius: '6px', color: '#e2e8f0', fontFamily: 'JetBrains Mono, monospace',
                      fontSize: '0.75rem', resize: 'vertical'
                    }}
                    placeholder={`[ ... ] or { ... }`}
                  />
                </div>
              ))}
            </div>
          )}

        </div>

        {/* FOOTER */}
        <div style={{ padding: '1rem 1.5rem', borderTop: '1px solid rgba(255,255,255,0.08)', background: '#1c212b', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <div style={{ flex: 1 }}>
            {errorMsg && (
              <div style={{ color: '#ef4444', fontSize: '0.8rem', fontWeight: 800, display: 'flex', alignItems: 'center', gap: '8px' }}>
                <span>⚠️</span> {errorMsg}
              </div>
            )}
          </div>
          <div style={{ display: 'flex', gap: '10px' }}>
            <button onClick={onClose} style={{ padding: '8px 16px', borderRadius: '6px', border: '1px solid rgba(255,255,255,0.2)', background: 'transparent', color: '#fff', fontWeight: 800, cursor: 'pointer' }}>
              CANCEL
            </button>
            <button 
              onClick={attemptSave} 
              disabled={isSaving}
              style={{ padding: '8px 24px', borderRadius: '6px', border: 'none', background: '#4ade80', color: '#000', fontWeight: 800, cursor: 'pointer', boxShadow: '0 0 15px rgba(74,222,128,0.2)', opacity: isSaving ? 0.7 : 1 }}
            >
              {isSaving ? 'SAVING...' : 'SAVE RECORD'}
            </button>
          </div>
        </div>

      </div>
    </div>
  );
};
