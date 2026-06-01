import React, { useState } from 'react';

const getEmails = (emails: unknown): string[] => {
  if (!emails) return [];
  if (Array.isArray(emails)) return emails.filter(Boolean).map(String);
  if (typeof emails === 'string') {
    try {
      const parsed = JSON.parse(emails);
      if (Array.isArray(parsed)) return parsed.filter(Boolean).map(String);
      return [emails].filter(Boolean);
    } catch {
      return emails.split(',').map((e: string) => e.trim()).filter(Boolean);
    }
  }
  return [];
};

export interface SpotRecord {
  id: string;
  name: string;
  verification_status: string;
  street_address?: string | null;
  city?: string | null;
  state?: string | null;
  status?: string | null;
  website?: string | null;
  phone_number?: string | null;
  email_addresses?: string | string[] | null;
  surface_quality?: string | null;
  has_adult_night?: boolean | null;
  is_published?: boolean | null;
  opening_hours?: unknown;
  pricing_data?: unknown;
  social_links?: unknown;
  raw_knowledge_panel?: unknown;
  email_input?: string;
  candidate_links?: Record<string, string> | null;
  [key: string]: unknown;
}

interface RecordEditModalProps {
  spot: SpotRecord;
  onSave: (updates: Partial<SpotRecord>) => Promise<void>;
  onClose: () => void;
}

export const RecordEditModal: React.FC<RecordEditModalProps> = ({ spot, onSave, onClose }) => {
  const [tab, setTab] = useState<'basic' | 'json' | 'ai_copypasta'>('basic');
  const [form, setForm] = useState<Partial<SpotRecord>>(() => {
    const emailList = getEmails(spot.email_addresses).join(', ');
    return { ...spot, email_input: emailList };
  });
  const [jsonText, setJsonText] = useState<{ [key: string]: string }>(() => ({
    opening_hours: spot.opening_hours ? JSON.stringify(spot.opening_hours, null, 2) : '',
    pricing_data: spot.pricing_data ? JSON.stringify(spot.pricing_data, null, 2) : '',
    social_links: spot.social_links ? JSON.stringify(spot.social_links, null, 2) : '',
    raw_knowledge_panel: spot.raw_knowledge_panel ? JSON.stringify(spot.raw_knowledge_panel, null, 2) : '',
  }));
  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  const [isSaving, setIsSaving] = useState(false);

  // AI Copypasta States
  const [copypastaText, setCopypastaText] = useState('');
  const [isAnalyzing, setIsAnalyzing] = useState(false);
  const [extractedData, setExtractedData] = useState<Record<string, any> | null>(null);
  const [selectedFields, setSelectedFields] = useState<Record<string, boolean>>({});
  const [injectSuccessMsg, setInjectSuccessMsg] = useState<string | null>(null);

  const handleBasicChange = (field: keyof SpotRecord, val: unknown) => {
    setForm((prev) => ({ ...prev, [field]: val }));
  };

  const handleJsonChange = (field: string, val: string) => {
    setJsonText(prev => ({ ...prev, [field]: val }));
    setErrorMsg(null);
  };

  const attemptSave = async () => {
    setErrorMsg(null);
    setIsSaving(true);
    
    const updates: Partial<SpotRecord> = { ...form };
    
    if (typeof updates.email_input === 'string') {
      const parsedEmails = updates.email_input.split(',').map((e: string) => e.trim()).filter(Boolean);
      updates.email_addresses = parsedEmails;
    }
    delete updates.email_input;

    let hasError = false;

    // Validate JSON fields
    ['opening_hours', 'pricing_data', 'social_links', 'raw_knowledge_panel'].forEach(field => {
      const text = jsonText[field];
      if (!text || text.trim() === '') {
        updates[field] = null;
      } else {
        try {
          updates[field] = JSON.parse(text);
        } catch (e) {
          setErrorMsg(`Invalid JSON in ${field}: ${(e as Error).message}`);
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
    } catch (e) {
      setErrorMsg(`Save failed: ${(e as Error).message}`);
    } finally {
      setIsSaving(false);
    }
  };

  const handleAnalyzeText = async () => {
    if (!copypastaText.trim()) return;
    setIsAnalyzing(true);
    setErrorMsg(null);
    setInjectSuccessMsg(null);
    setExtractedData(null);

    try {
      const response = await fetch('http://127.0.0.1:5999/api/llm/parse-copypasta', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          text: copypastaText,
          spot_name: spot.name
        })
      });

      if (!response.ok) {
        const err = await response.json();
        throw new Error(err.error || 'Server parsing error.');
      }

      const res = await response.json();
      if (res.success && res.parsed) {
        setExtractedData(res.parsed);
        // Pre-select all non-null fields
        const initialSelected: Record<string, boolean> = {};
        Object.entries(res.parsed).forEach(([k, v]) => {
          if (v !== null && v !== undefined && v !== '') {
            initialSelected[k] = true;
          }
        });
        setSelectedFields(initialSelected);
      } else {
        throw new Error('LLM failed to return structured results.');
      }
    } catch (e: any) {
      setErrorMsg(`AI Analysis failed: ${e.message}. Ensure LM Studio is active on host.`);
    } finally {
      setIsAnalyzing(false);
    }
  };

  const handleInjectData = () => {
    if (!extractedData) return;
    
    let injectCount = 0;
    const jsonFields = ['opening_hours', 'pricing_data', 'social_links', 'raw_knowledge_panel'];
    
    const updatedForm = { ...form };
    const updatedJsonText = { ...jsonText };

    Object.entries(extractedData).forEach(([k, v]) => {
      if (!selectedFields[k]) return;
      if (v === null || v === undefined || v === '') return;

      injectCount++;
      if (jsonFields.includes(k)) {
        updatedJsonText[k] = JSON.stringify(v, null, 2);
      } else {
        // Map any legacy fields to correct form fields
        if (k === 'has_proshop') {
          updatedForm.has_pro_shop = !!v;
        } else if (k === 'has_adult_night') {
          updatedForm.has_adult_night = !!v;
        } else if (k === 'is_published') {
          updatedForm.is_published = !!v;
        } else if (k === 'email_addresses') {
          if (Array.isArray(v)) {
            updatedForm.email_input = v.join(', ');
          } else if (typeof v === 'string') {
            updatedForm.email_input = v;
          }
        } else {
          updatedForm[k] = v;
        }
      }
    });

    setForm(updatedForm);
    setJsonText(updatedJsonText);
    setInjectSuccessMsg(`Successfully injected ${injectCount} fields! Switch to BASIC INFO or JSON DATA to verify.`);
    setTimeout(() => setInjectSuccessMsg(null), 10000);
  };

  const renderFieldPreview = (key: string, val: any) => {
    if (val === null || val === undefined || val === '') return <span style={{ color: 'rgba(255,255,255,0.2)' }}>None</span>;
    
    if (key === 'opening_hours' || key === 'adult_night_schedule') {
      return (
        <div style={{ display: 'grid', gridTemplateColumns: '80px 1fr', gap: '4px', fontSize: '0.75rem', fontFamily: 'JetBrains Mono, monospace', background: 'rgba(0,0,0,0.2)', padding: '6px', borderRadius: '4px' }}>
          {Object.entries(val).map(([day, time]) => (
            <React.Fragment key={day}>
              <span style={{ color: '#60a5fa', fontWeight: 'bold' }}>{day.slice(0, 3)}:</span>
              <span style={{ color: '#fff' }}>{String(time)}</span>
            </React.Fragment>
          ))}
        </div>
      );
    }

    if (key === 'pricing_data') {
      return (
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px' }}>
          {Object.entries(val).map(([type, price]) => (
            price !== null && (
              <span key={type} style={{ fontSize: '0.7rem', padding: '2px 6px', background: 'rgba(244,63,94,0.1)', border: '1px solid rgba(244,63,94,0.3)', borderRadius: '4px', color: '#f43f5e' }}>
                {type}: <strong style={{ color: '#fff' }}>${price}</strong>
              </span>
            )
          ))}
        </div>
      );
    }

    if (typeof val === 'boolean') {
      return <span style={{ color: val ? '#4ade80' : '#ef4444', fontWeight: 'bold', fontSize: '0.75rem' }}>{val ? '✓ TRUE' : '✗ FALSE'}</span>;
    }

    if (Array.isArray(val)) {
      return <span style={{ color: '#e2e8f0', fontSize: '0.75rem' }}>{val.join(', ')}</span>;
    }

    return <span style={{ color: '#fff', fontSize: '0.75rem' }}>{String(val)}</span>;
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
        borderRadius: '12px', width: '1000px', maxWidth: '95vw', height: '85vh',
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
            style={{ flex: 1, padding: '12px', background: tab === 'basic' ? 'rgba(255,255,255,0.05)' : 'transparent', border: 'none', borderBottom: tab === 'basic' ? '2px solid #60a5fa' : '2px solid transparent', color: tab === 'basic' ? '#60a5fa' : 'rgba(255,255,255,0.4)', fontWeight: 800, cursor: 'pointer', transition: 'all 0.2s' }}
          >
            BASIC INFO
          </button>
          <button 
            onClick={() => setTab('json')}
            style={{ flex: 1, padding: '12px', background: tab === 'json' ? 'rgba(255,255,255,0.05)' : 'transparent', border: 'none', borderBottom: tab === 'json' ? '2px solid #f43f5e' : '2px solid transparent', color: tab === 'json' ? '#f43f5e' : 'rgba(255,255,255,0.4)', fontWeight: 800, cursor: 'pointer', transition: 'all 0.2s' }}
          >
            JSON DATA & META
          </button>
          <button 
            onClick={() => setTab('ai_copypasta')}
            style={{ 
              flex: 1, padding: '12px', 
              background: tab === 'ai_copypasta' ? 'rgba(168,85,247,0.1)' : 'transparent', 
              border: 'none', 
              borderBottom: tab === 'ai_copypasta' ? '2px solid #a855f7' : '2px solid transparent', 
              color: tab === 'ai_copypasta' ? '#a855f7' : 'rgba(255,255,255,0.4)', 
              fontWeight: 800, cursor: 'pointer', transition: 'all 0.2s',
              display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '6px'
            }}
          >
            <span>⚡</span> AI COPYPASTA ASSISTANT
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
                <select 
                  className="custom-dark" 
                  value={form.verification_status || 'PENDING'} 
                  onChange={e => handleBasicChange('verification_status', e.target.value)} 
                  style={{ width: '100%', padding: '8px 12px', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '6px', color: '#fff' }}
                >
                  <option value="PENDING_WEBSITE">PENDING_WEBSITE (Resolving)</option>
                  <option value="STALLED_WEBSITE">STALLED_WEBSITE (Stalled Resolver)</option>
                  <option value="SEEDED">SEEDED (Seeded)</option>
                  <option value="DEEP_CRAWLED">DEEP_CRAWLED (Deep Crawled)</option>
                  <option value="MEDIA_READY">MEDIA_READY (Media Ready)</option>
                  <option value="PUBLISHED">PUBLISHED (Published)</option>
                  <option value="STALLED">STALLED (Stalled)</option>
                  <option value="REJECTED">REJECTED (Graveyard)</option>
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
                <label style={{ display: 'block', fontSize: '0.7rem', fontWeight: 800, color: 'rgba(255,255,255,0.4)', marginBottom: '5px' }}>EMAIL ADDRESSES (COMMA-SEPARATED)</label>
                <input value={form.email_input || ''} onChange={e => handleBasicChange('email_input', e.target.value)} style={{ width: '100%', padding: '8px 12px', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '6px', color: '#fff' }} placeholder="e.g. hello@skatespot.com, info@skatespot.com" />
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

          {tab === 'ai_copypasta' && (
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem', height: '100%', minHeight: '480px' }}>
              {/* LEFT COLUMN: Input Text Box */}
              <div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
                <div style={{ marginBottom: '12px' }}>
                  <h3 style={{ margin: 0, fontSize: '0.95rem', fontWeight: 800, color: '#fff' }}>Copypasta Parser Box</h3>
                  <p style={{ margin: '4px 0 0 0', fontSize: '0.75rem', color: 'rgba(255,255,255,0.4)', lineHeight: '1.3' }}>
                    Copy unstructured text (schedules, pricing, floor descriptions, rules) directly from the spot's website and paste it below. The AI will structure it and map it to your database schema.
                  </p>
                </div>
                
                <textarea
                  value={copypastaText}
                  onChange={e => setCopypastaText(e.target.value)}
                  style={{
                    flex: 1, minHeight: '300px', width: '100%', padding: '14px',
                    background: 'rgba(255,255,255,0.02)', border: '1px solid rgba(168,85,247,0.3)',
                    borderRadius: '8px', color: '#e2e8f0', fontFamily: 'Outfit, sans-serif',
                    fontSize: '0.85rem', resize: 'none', outline: 'none', lineHeight: '1.4'
                  }}
                  placeholder="Example: We are open Saturday 1pm to 4pm for family skate ($10 admission) and 7pm to 11pm for cosmic glow ($12 entry). Quad rentals are $4. Our floor is pristine, smooth polished concrete. Come skate!"
                />
                
                <button
                  onClick={handleAnalyzeText}
                  disabled={isAnalyzing || !copypastaText.trim()}
                  style={{
                    marginTop: '12px', padding: '12px', borderRadius: '8px', border: 'none',
                    background: 'linear-gradient(135deg, #a855f7 0%, #7c3aed 100%)',
                    color: '#fff', fontWeight: 800, cursor: 'pointer',
                    boxShadow: '0 4px 15px rgba(168,85,247,0.3)', transition: 'all 0.2s',
                    opacity: isAnalyzing || !copypastaText.trim() ? 0.6 : 1,
                    display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '8px'
                  }}
                >
                  {isAnalyzing ? (
                    <>
                      <div className="spinner-mini" style={{ width: '16px', height: '16px', border: '2px solid rgba(255,255,255,0.3)', borderTopColor: '#fff', borderRadius: '50%' }}></div>
                      AI DETECTIVE PARSING TEXT...
                    </>
                  ) : (
                    <>
                      <span>⚡</span> RUN AI EXTRACTION PASS
                    </>
                  )}
                </button>
              </div>

              {/* RIGHT COLUMN: Structured Result Preview */}
              <div style={{ display: 'flex', flexDirection: 'column', height: '100%', background: 'rgba(0,0,0,0.2)', border: '1px solid rgba(255,255,255,0.05)', borderRadius: '8px', padding: '1rem', overflowY: 'auto' }}>
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', borderBottom: '1px solid rgba(255,255,255,0.08)', paddingBottom: '8px', marginBottom: '12px' }}>
                  <h3 style={{ margin: 0, fontSize: '0.9rem', fontWeight: 800, color: '#a855f7' }}>Extracted Schema Attributes</h3>
                  {extractedData && (
                    <button
                      onClick={handleInjectData}
                      style={{
                        padding: '4px 12px', background: '#a855f7', border: 'none', borderRadius: '4px',
                        color: '#fff', fontWeight: 800, fontSize: '0.75rem', cursor: 'pointer',
                        display: 'flex', alignItems: 'center', gap: '4px'
                      }}
                    >
                      <span>📥</span> INJECT CHOSEN FIELDS
                    </button>
                  )}
                </div>

                {injectSuccessMsg && (
                  <div style={{ padding: '8px 12px', background: 'rgba(74,222,128,0.1)', border: '1px solid rgba(74,222,128,0.3)', borderRadius: '6px', color: '#4ade80', fontSize: '0.8rem', fontWeight: 800, marginBottom: '12px' }}>
                    {injectSuccessMsg}
                  </div>
                )}

                {/* Extracted Fields List */}
                {!extractedData ? (
                  <div style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', color: 'rgba(255,255,255,0.3)', textAlign: 'center', padding: '2rem' }}>
                    <div style={{ fontSize: '2.5rem', marginBottom: '10px' }}>🤖</div>
                    <h4 style={{ margin: '0 0 6px 0', fontSize: '0.85rem', color: '#fff', fontWeight: 800 }}>Awaiting Text Submission</h4>
                    <p style={{ margin: 0, fontSize: '0.7rem', lineHeight: '1.4' }}>
                      Once you click "Run AI Extraction Pass", the structured database output will render here for schema injection validation.
                    </p>
                  </div>
                ) : (
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                    {Object.entries(extractedData).map(([key, val]) => {
                      const hasVal = val !== null && val !== undefined && val !== '' && !(typeof val === 'object' && Object.keys(val).length === 0);
                      if (!hasVal) return null;

                      return (
                        <div 
                          key={key} 
                          style={{ 
                            padding: '10px', background: 'rgba(255,255,255,0.02)', 
                            border: '1px solid rgba(255,255,255,0.05)', borderRadius: '6px',
                            display: 'flex', alignItems: 'flex-start', gap: '10px',
                            transition: 'border-color 0.2s'
                          }}
                        >
                          <input
                            type="checkbox"
                            checked={selectedFields[key] || false}
                            onChange={e => setSelectedFields(prev => ({ ...prev, [key]: e.target.checked }))}
                            style={{ marginTop: '3px', width: '15px', height: '15px', accentColor: '#a855f7', cursor: 'pointer' }}
                          />
                          <div style={{ flex: 1 }}>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '4px' }}>
                              <span style={{ fontSize: '0.75rem', fontWeight: 800, color: '#e2e8f0', textTransform: 'uppercase' }}>
                                {key.replace('_', ' ')}
                              </span>
                              <span style={{ fontSize: '0.6rem', padding: '1px 5px', background: 'rgba(255,255,255,0.08)', borderRadius: '3px', color: 'rgba(255,255,255,0.4)', fontFamily: 'JetBrains Mono, monospace' }}>
                                {typeof val === 'object' ? 'JSON' : typeof val}
                              </span>
                            </div>
                            <div>
                              {renderFieldPreview(key, val)}
                            </div>
                          </div>
                        </div>
                      );
                    })}

                    {Object.values(extractedData).every(v => v === null || v === undefined || v === '' || (typeof v === 'object' && Object.keys(v).length === 0)) && (
                      <div style={{ padding: '2rem', textAlign: 'center', color: 'rgba(255,255,255,0.4)', fontSize: '0.8rem' }}>
                        ⚠️ AI ran the extraction pass but found no attributes matching the database schema. Try pasting a larger block of raw site text.
                      </div>
                    )}
                  </div>
                )}
              </div>
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
