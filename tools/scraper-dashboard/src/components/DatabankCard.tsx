import React, { useState } from 'react';
import { useFieldRegistry } from '../hooks/useFieldRegistry';

export interface ScheduledService {
  '@type'?: string;
  dayOfWeek?: string | string[];
  timeRange?: string;
  opens?: string;
}

export interface PhotoItem {
  url: string;
  type?: string;
  [key: string]: unknown;
}

export interface CandidatePhotos {
  street_view_url?: string | null;
  [key: string]: unknown;
}

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
  facility_type?: string | null;
  has_rental?: boolean | null;
  hosts_derby?: boolean | null;
  surface_type?: string | null;
  adult_night_schedule?: string | object | null;
  adult_night_details?: string | null;
  price_range?: string | null;
  has_fee?: boolean | null;
  phone?: string | null;
  rating?: string | number | null;
  has_pro_shop?: boolean | null;
  has_proshop?: boolean | null;
  instagram_url?: string | null;
  facebook_url?: string | null;
  tiktok_url?: string | null;
  photos?: unknown;
  candidate_photos?: unknown;
  logo_url?: string | null;
  photo_coverage?: {
    logo?: boolean;
    exterior?: boolean;
    interior?: boolean;
    floor?: boolean;
    pro_shop?: boolean;
    action?: boolean;
  } | null;
  user_ratings_total?: number | null;
  google_place_id?: string | null;
  last_enriched_at?: string | number | Date | null;
  is_deep_crawled?: boolean | null;
  candidate_links?: Record<string, string> | null;
  [key: string]: unknown;
}

// Helpers
const toHoursArr = (h: unknown): string[] | null => {
  if (!h) return null;
  if (Array.isArray(h)) return h.map(String);
  if (typeof h === 'string') {
    try {
      const p = JSON.parse(h);
      if (Array.isArray(p)) return p.map(String);
      if (p && typeof p === 'object') {
        const service = p as ScheduledService;
        if (service['@type'] === 'ScheduledService' && service.dayOfWeek) {
          const days = Array.isArray(service.dayOfWeek) ? service.dayOfWeek : [service.dayOfWeek];
          return days.map((d: string) => `${d}: ${service.timeRange || service.opens || '?'}`);
        }
      }
      return null;
    } catch { return null; }
  }
  if (h && typeof h === 'object') {
    const service = h as ScheduledService;
    if (service['@type'] === 'ScheduledService' && service.dayOfWeek) {
      const days = Array.isArray(service.dayOfWeek) ? service.dayOfWeek : [service.dayOfWeek];
      return days.map((d: string) => `${d}: ${service.timeRange || service.opens || '?'}`);
    }
  }
  return null;
};

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


const isOpenNow = (hours: string[] | null): boolean | null => {
  if (!hours || !hours.length) return null;
  const DAYS = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];
  const todayName = DAYS[new Date().getDay()];
  const todayEntry = hours.find(h => h.startsWith(todayName));
  if (!todayEntry) return null;
  if (todayEntry.toLowerCase().includes('closed')) return false;
  
  const p = (s: string): number => {
    const m = s.trim().match(/(\d+):(\d+)\s*(AM|PM)?/i);
    if (!m) {
      const m24 = s.trim().match(/^(\d{1,2}):(\d{2})$/);
      if (m24) {
        return parseInt(m24[1]) * 60 + parseInt(m24[2]);
      }
      return 0;
    }
    let h = parseInt(m[1]);
    const min = parseInt(m[2]);
    const per = (m[3] || '').toUpperCase();
    if (per === 'PM' && h !== 12) h += 12;
    if (per === 'AM' && h === 12) h = 0;
    return h * 60 + min;
  };
  
  const cur = new Date().getHours() * 60 + new Date().getMinutes();
  const segments = todayEntry.replace(/^[^:]+:\s*/, '').split(',');
  
  return segments.some(seg => {
    const rng = seg.match(/(.+?)\s*[\u2013\u2014\-]\s*(.+)/);
    if (!rng) return false;
    
    let startStr = rng[1].trim();
    let endStr = rng[2].trim();
    
    const startHasPeriod = /[AP]M/i.test(startStr);
    const endMatch = endStr.match(/([AP]M)/i);
    if (!startHasPeriod && endMatch) {
      startStr = `${startStr} ${endMatch[1]}`;
    }
    
    const start = p(startStr);
    const end = p(endStr);
    
    if (end < start) {
      return cur >= start || cur <= end;
    }
    
    return cur >= start && cur <= end;
  });
};

const stars = (r: number) => {
  const full = Math.floor(r); const half = r - full >= 0.5;
  return Array.from({length:5},(_,i)=>
    i < full ? '&#9733;' : (i === full && half ? '&#11240;' : '&#9734;')
  ).join('');
};

const todayHours = (hours: string[] | null): string => {
  if (!hours) return '';
  const days = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];
  const entry = hours.find(h => h.startsWith(days[new Date().getDay()]));
  return entry ? entry.split(': ').slice(1).join(': ') : '';
};

export interface DatabankCardProps {
  spot: SpotRecord;
  variant?: 'detailed' | 'polaroid';
  readOnly?: boolean;
  onEdit?: (spot: SpotRecord) => void;
  onReset?: (id: string, name: string) => void;
  onPurge?: (id: string, name: string) => void;
  onBlock?: (id: string, name: string) => void;
  onSetHero?: (spotId: string, photoIndex: number) => void;
  onDeletePhoto?: (spotId: string, photoIndex: number) => void;
  onAssignPhotoType?: (spotId: string, photoIndex: number, fieldType: string) => void;
  onUploadPhoto?: (spotId: string, file: File) => void;
  onPublishToggle?: (spot: SpotRecord) => void;
  proxyImg: (url: string | null) => string | null;
}

export const DatabankCard: React.FC<DatabankCardProps> = ({ 
  spot, variant = 'detailed', readOnly = false,
  onEdit, onReset, onBlock, onSetHero, onDeletePhoto, onAssignPhotoType, onUploadPhoto, onPublishToggle, proxyImg
}) => {
  const { fields } = useFieldRegistry();
  const [photoIndex, setPhotoIndex] = useState(0);
  const [showTypeMenu, setShowTypeMenu] = useState(false);
  const [isCardHovered, setIsCardHovered] = useState(false);
  const fileInputRef = React.useRef<HTMLInputElement>(null);

  const isFieldEmpty = (fieldName: string): boolean => {
    switch (fieldName) {
      case 'name':
        return !spot.name;
      case 'street_address':
      case 'address':
        return !spot.street_address && !spot.address;
      case 'city':
        return !spot.city;
      case 'state':
        return !spot.state;
      case 'zip':
        return !spot.zip;
      case 'email_addresses': {
        const emails = getEmails(spot.email_addresses);
        return emails.length === 0;
      }
      case 'phone_number':
      case 'phone':
        return !spot.phone_number && !spot.phone;
      case 'opening_hours':
        return !spot.opening_hours || (Array.isArray(spot.opening_hours) && spot.opening_hours.length === 0);
      case 'pricing_data':
      case 'price_range':
      case 'has_fee':
        return !spot.price_range && !spot.pricing_data && spot.has_fee === null;
      case 'has_pro_shop':
      case 'has_proshop':
        return spot.has_pro_shop === null || spot.has_pro_shop === undefined;
      case 'has_adult_night':
        return spot.has_adult_night === null || spot.has_adult_night === undefined;
      case 'instagram_url':
        return !spot.instagram_url || spot.instagram_url === 'null';
      case 'facebook_url':
        return !spot.facebook_url || spot.facebook_url === 'null';
      case 'tiktok_url':
        return !spot.tiktok_url || spot.tiktok_url === 'null';
      case 'surface_type':
        return !spot.surface_type;
      case 'surface_quality':
        return !spot.surface_quality;
      case 'has_rental':
        return spot.has_rental === null || spot.has_rental === undefined;
      default:
        return !spot[fieldName];
    }
  };

  const isGlowEmpty = (fieldName: string): boolean => {
    const config = fields.find(f => f.field_name === fieldName);
    return !!(config && config.visual_glow === 1 && isFieldEmpty(fieldName));
  };

  const anyGlowEmpty = fields.some(f => f.visual_glow === 1 && isFieldEmpty(f.field_name));

  const handleUploadClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    fileInputRef.current?.click();
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      onUploadPhoto?.(spot.id, e.target.files[0]);
    }
    if (fileInputRef.current) fileInputRef.current.value = '';
  };

  // Reset photo index if spot changes to avoid out-of-bounds
  React.useEffect(() => {
    setPhotoIndex(0);
  }, [spot.id]);

  const _ph = spot.photos as unknown; 
  const _cd = spot.candidate_photos as unknown;
  
  let safePh: (string | PhotoItem)[] = [];
  if (Array.isArray(_ph)) {
    safePh = _ph as (string | PhotoItem)[];
  } else if (typeof _ph === 'string' && _ph.trim()) {
    try {
      const parsed = JSON.parse(_ph);
      if (Array.isArray(parsed)) safePh = parsed;
    } catch {}
  }

  let safeCd: CandidatePhotos | PhotoItem[] | null = null;
  if (_cd) {
    if (typeof _cd === 'string' && _cd.trim()) {
      try {
        safeCd = JSON.parse(_cd);
      } catch {}
    } else {
      safeCd = _cd as CandidatePhotos | PhotoItem[];
    }
  }
  
  const validPhotoIndex = photoIndex >= safePh.length ? 0 : photoIndex;
  const rawPhoto = (typeof safePh[validPhotoIndex] === 'string' ? safePh[validPhotoIndex] : (safePh[validPhotoIndex] as PhotoItem)?.url) ?? 
    ((safeCd as CandidatePhotos)?.street_view_url ?? (Array.isArray(safeCd) ? (safeCd[0] as PhotoItem)?.url : null));
  const photo = proxyImg(rawPhoto);
  
  const openStatus = isOpenNow(toHoursArr(spot.opening_hours));
  const ratingNum  = spot.rating ? parseFloat(String(spot.rating)) : null;
  const proShop    = spot.has_pro_shop || spot.has_proshop;
  const adultNight = spot.has_adult_night;
  const photoCount = (safePh.length);
  const candCount  = ((safeCd as CandidatePhotos)?.street_view_url ? 1 : 0);

  // Auto-rotate photos if photoCount > 1 and not card hovered
  React.useEffect(() => {
    if (photoCount <= 1) return;
    if (isCardHovered) return;
    const interval = setInterval(() => {
      setPhotoIndex(prev => (prev + 1) % photoCount);
    }, 3000);
    return () => clearInterval(interval);
  }, [photoCount, isCardHovered]);
  // Guard against literal "null" strings stored by the AI
  const nullGuard = (v: unknown): string | null => (!v || v === 'null' || v === 'NULL') ? null : String(v);
  const igUrl      = nullGuard(spot.instagram_url);
  const fbUrl      = nullGuard(spot.facebook_url);
  const ttUrl      = nullGuard(spot.tiktok_url);
  const hours      = toHoursArr(spot.opening_hours);
  const adultSched = spot.adult_night_schedule;

  const PHOTO_TYPES = [
    'exterior', 'interior', 'floor', 'pro_shop', 'action', 'logo', 'unknown',
  ];

  const PHOTO_TYPE_LABELS: Record<string, { icon: string; color: string }> = {
    exterior:  { icon: '🏢', color: '#64b5f6' },
    interior:  { icon: '🏟️', color: '#81c784' },
    floor:     { icon: '🛹', color: '#ffb74d' },
    pro_shop:  { icon: '🛒', color: '#f06292' },
    action:    { icon: '🎉', color: '#ce93d8' },
    logo:      { icon: '🏷️', color: '#4fc3f7' },
    unknown:   { icon: '❓', color: 'rgba(255,255,255,0.3)' },
  };

  // Helper to render the type menu
  const renderTypeMenu = () => {
    if (!showTypeMenu) return null;
    return (
      <div style={{ position: 'absolute', top: '10px', left: '10px', right: '10px', bottom: '10px', background: 'rgba(20,20,30,0.95)', backdropFilter: 'blur(10px)', borderRadius: '8px', zIndex: 10, display: 'flex', flexDirection: 'column', padding: '10px', overflowY: 'auto', border: '1px solid rgba(255,255,255,0.1)' }} onClick={e => e.stopPropagation()}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
          <span style={{ fontSize: '0.7rem', fontWeight: 800, color: '#fff' }}>ASSIGN CATEGORY</span>
          <button onClick={() => setShowTypeMenu(false)} style={{ background: 'transparent', border: 'none', color: 'rgba(255,255,255,0.5)', cursor: 'pointer', fontSize: '0.8rem', padding: '0 4px' }}>✕</button>
        </div>
        <div style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
          {PHOTO_TYPES.map(t => (
            <label key={t} style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '0.7rem', color: '#ccc', cursor: 'pointer', padding: '4px 6px', borderRadius: '4px', background: 'rgba(255,255,255,0.05)' }}
              onMouseEnter={e => (e.currentTarget as HTMLElement).style.background = 'rgba(255,255,255,0.1)'}
              onMouseLeave={e => (e.currentTarget as HTMLElement).style.background = 'rgba(255,255,255,0.05)'}>
              <input type="radio" name={`type_${spot.id}_${validPhotoIndex}`} checked={false} onChange={() => {
                onAssignPhotoType?.(spot.id, validPhotoIndex, t);
                setShowTypeMenu(false);
              }} style={{ cursor: 'pointer', accentColor: '#8a2be2' }} />
              {t}
            </label>
          ))}
        </div>
      </div>
    );
  };

  // Current photo type label
  const currentPhotoObj = safePh[validPhotoIndex] as PhotoItem | undefined;
  const currentPhotoType = typeof currentPhotoObj === 'object' ? (currentPhotoObj?.type || 'unknown') : 'unknown';
  const currentTypeLabel = PHOTO_TYPE_LABELS[currentPhotoType] || PHOTO_TYPE_LABELS.unknown;

  // Parse photo_coverage
  let photoCoverage: Record<string, boolean> = {};
  try {
    const raw = spot.photo_coverage;
    if (raw && typeof raw === 'object') photoCoverage = raw as Record<string, boolean>;
    else if (typeof raw === 'string') photoCoverage = JSON.parse(raw);
  } catch {}
  const coverageCategories = ['exterior', 'interior', 'floor', 'pro_shop', 'action'];

  if (variant === 'polaroid') {
    return (
      <div 
        onMouseEnter={() => setIsCardHovered(true)}
        onMouseLeave={() => setIsCardHovered(false)}
        style={{
          background: 'rgba(30,30,40,0.95)', borderRadius: '10px', overflow: 'hidden',
          border: anyGlowEmpty 
            ? '1.5px solid rgba(255, 51, 102, 0.8)' 
            : `1px solid ${spot.is_published ? 'rgba(76,175,80,0.4)' : 'rgba(255,255,255,0.08)'}`,
          display: 'flex', flexDirection: 'column', width: '100%', height: '100%',
          boxShadow: anyGlowEmpty 
            ? '0 0 15px rgba(255, 51, 102, 0.4), inset 0 0 10px rgba(255, 51, 102, 0.15)' 
            : '0 4px 15px rgba(0,0,0,0.5)'
        }}>
        <div style={{ position: 'relative', height: '120px', background: 'rgba(255,255,255,0.03)', flexShrink: 0 }}>
          {renderTypeMenu()}
          {photo
            ? <img src={photo} alt={spot.name} style={{ width:'100%', height:'100%', objectFit:'cover' }} onError={e => { (e.target as HTMLImageElement).style.display='none'; }} />
            : <div style={{ width:'100%', height:'100%', display:'flex', alignItems:'center', justifyContent:'center', flexDirection:'column', gap:'6px' }}>
                <span style={{ fontSize:'1.5rem', opacity:0.15 }}>[ IMG ]</span>
                {!readOnly && <button onClick={handleUploadClick} style={{ padding:'3px 8px', background:'rgba(76,175,80,0.2)', color:'#4caf50', border:'1px solid rgba(76,175,80,0.5)', borderRadius:'5px', cursor:'pointer', fontSize:'0.62rem' }}>Upload</button>}
              </div>
          }
          {photoCount > 1 && (
            <>
              <button onClick={(e) => { e.stopPropagation(); setPhotoIndex(prev => (prev - 1 + photoCount) % photoCount); }} 
                style={{ position:'absolute', top:'50%', left:2, transform:'translateY(-50%)', background:'rgba(0,0,0,0.6)', color:'white', border:'none', borderRadius:'50%', width:'20px', height:'20px', cursor:'pointer', display:'flex', alignItems:'center', justifyContent:'center', fontSize:'10px' }}>&#10094;</button>
              <button onClick={(e) => { e.stopPropagation(); setPhotoIndex(prev => (prev + 1) % photoCount); }} 
                style={{ position:'absolute', top:'50%', right:2, transform:'translateY(-50%)', background:'rgba(0,0,0,0.6)', color:'white', border:'none', borderRadius:'50%', width:'20px', height:'20px', cursor:'pointer', display:'flex', alignItems:'center', justifyContent:'center', fontSize:'10px' }}>&#10095;</button>
            </>
          )}
          {/* Status badge */}
          <span style={{ position:'absolute', top:8, left:8, padding:'2px 6px', borderRadius:'4px', fontSize:'0.55rem', fontWeight:800, letterSpacing:'0.05em',
            background: spot.verification_status === 'MEDIA_READY' ? '#e91e63' : spot.verification_status === 'ENRICHED' ? '#ff9800' : spot.verification_status === 'INDEXED' ? '#2196f3' : 'rgba(0,0,0,0.6)',
            color: '#fff' }}>{spot.verification_status || 'PENDING'}</span>
          
          {/* Quick Actions overlay */}
          {!readOnly && (
            <div style={{ position:'absolute', bottom:4, left:4, right:4, display:'flex', gap:'4px', justifyContent:'space-between', zIndex: 5 }}>
              <div style={{ display:'flex', gap:'4px' }}>
                <button onClick={(e) => { e.stopPropagation(); onSetHero?.(spot.id, validPhotoIndex); setPhotoIndex(0); }} style={{ background:'rgba(0,0,0,0.7)', color: validPhotoIndex === 0 ? '#ffb300' : 'rgba(255,255,255,0.4)', border:'none', borderRadius:'4px', padding:'2px 6px', fontSize:'0.75rem', cursor:'pointer' }} title="Set Hero">{validPhotoIndex === 0 ? '★' : '☆'}</button>
                <button onClick={(e) => { e.stopPropagation(); onDeletePhoto?.(spot.id, validPhotoIndex); }} style={{ background:'rgba(0,0,0,0.7)', color:'#ff3b30', border:'none', borderRadius:'4px', padding:'2px 6px', fontSize:'0.55rem', cursor:'pointer' }} title="Delete Photo">🗑</button>
                <button onClick={(e) => { e.stopPropagation(); setShowTypeMenu(true); }} style={{ background:'rgba(0,0,0,0.7)', color:'#64b5f6', border:'none', borderRadius:'4px', padding:'2px 6px', fontSize:'0.6rem', cursor:'pointer', fontWeight: 800 }} title="Assign Type">TAG</button>
                <button onClick={handleUploadClick} style={{ background:'rgba(0,0,0,0.7)', color:'#4caf50', border:'none', borderRadius:'4px', padding:'2px 6px', fontSize:'0.6rem', cursor:'pointer', fontWeight: 800 }} title="Upload Photo">UP</button>
              </div>
              <div style={{ display:'flex', gap:'4px' }}>
                <button onClick={(e) => { e.stopPropagation(); onBlock?.(spot.id, spot.name); }} style={{ background:'rgba(0,0,0,0.7)', color:'#ff3b30', border:'none', borderRadius:'4px', padding:'2px 6px', fontSize:'0.55rem', cursor:'pointer', fontWeight: 800 }} title="Block">BLOCK 🚫</button>
              </div>
            </div>
          )}
        </div>
        <div style={{ padding:'8px 10px', flex:1, display:'flex', flexDirection:'column', justifyContent:'center' }}>
          <div style={{ fontWeight:800, fontSize:'0.8rem', lineHeight:1.1, marginBottom:'2px', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{spot.name}</div>
          <div style={{ fontSize:'0.6rem', color:'rgba(255,255,255,0.45)', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis', marginBottom: getEmails(spot.email_addresses).length > 0 ? '4px' : '0' }}>
            {[spot.city, spot.state].filter(Boolean).join(', ')}
          </div>
          {(() => {
            const emails = getEmails(spot.email_addresses);
            if (emails.length === 0) return null;
            const firstEmail = emails[0];
            const extraCount = emails.length - 1;
            return (
              <div style={{ 
                fontSize: '0.58rem', 
                color: '#c084fc', 
                textShadow: '0 0 4px rgba(168,85,247,0.4)',
                display: 'flex', 
                alignItems: 'center', 
                gap: '3px',
                whiteSpace: 'nowrap',
                overflow: 'hidden',
                textOverflow: 'ellipsis'
              }} title={emails.join(', ')}>
                <span>📧</span>
                <span style={{ overflow: 'hidden', textOverflow: 'ellipsis' }}>{firstEmail}</span>
                {extraCount > 0 && <span style={{ opacity: 0.6 }}>({extraCount}+)</span>}
              </div>
            );
          })()}
        </div>
      </div>
    );
  }

  return (
    <div style={{
      background: 'rgba(30,30,40,0.95)', borderRadius: '14px', overflow: 'hidden',
      border: anyGlowEmpty 
        ? '1.5px solid rgba(255, 51, 102, 0.8)' 
        : `1px solid ${spot.is_published ? 'rgba(76,175,80,0.4)' : 'rgba(255,255,255,0.08)'}`,
      display: 'flex', flexDirection: 'column', transition: 'transform 0.18s, box-shadow 0.18s',
      height: '100%',
      boxShadow: anyGlowEmpty ? '0 0 15px rgba(255, 51, 102, 0.3)' : ''
    }}
      onMouseEnter={e => {
        setIsCardHovered(true);
        (e.currentTarget as HTMLElement).style.transform='translateY(-3px)';
        (e.currentTarget as HTMLElement).style.boxShadow= anyGlowEmpty 
          ? '0 12px 40px rgba(255, 51, 102, 0.5), 0 0 20px rgba(255, 51, 102, 0.4)' 
          : '0 12px 40px rgba(0,0,0,0.5)';
      }}
      onMouseLeave={e => {
        setIsCardHovered(false);
        (e.currentTarget as HTMLElement).style.transform='';
        (e.currentTarget as HTMLElement).style.boxShadow= anyGlowEmpty ? '0 0 15px rgba(255, 51, 102, 0.3)' : '';
      }}
    >
      {/* Hero image */}
      <div style={{ position: 'relative', height: '180px', background: 'rgba(255,255,255,0.03)', flexShrink: 0 }}>
        {renderTypeMenu()}
        {photo
          ? <img src={photo} alt={spot.name} style={{ width:'100%', height:'100%', objectFit:'cover' }} onError={e => { (e.target as HTMLImageElement).style.display='none'; }} />
          : <div style={{ width:'100%', height:'100%', display:'flex', alignItems:'center', justifyContent:'center', flexDirection:'column', gap:'6px' }}>
              <span style={{ fontSize:'2rem', opacity:0.15 }}>[ IMG ]</span>
              <span style={{ fontSize:'0.65rem', color:'rgba(255,255,255,0.2)' }}>{candCount > 0 ? `${candCount} candidate(s) queued` : 'No photo'}</span>
              {!readOnly && (
                <button onClick={handleUploadClick} style={{ padding:'4px 10px', background:'rgba(76,175,80,0.2)', color:'#4caf50', border:'1px solid rgba(76,175,80,0.5)', borderRadius:'6px', cursor:'pointer', fontSize:'0.7rem', marginTop:'8px' }}>
                  Upload Photo
                </button>
              )}
            </div>
        }
        {photoCount > 1 && (
          <>
            <button onClick={(e) => { e.stopPropagation(); setPhotoIndex(prev => (prev - 1 + photoCount) % photoCount); }} 
              style={{ position:'absolute', top:'50%', left:5, transform:'translateY(-50%)', background:'rgba(0,0,0,0.5)', color:'white', border:'none', borderRadius:'50%', width:'28px', height:'28px', cursor:'pointer', display:'flex', alignItems:'center', justifyContent:'center', fontSize:'14px' }}>&#10094;</button>
            <button onClick={(e) => { e.stopPropagation(); setPhotoIndex(prev => (prev + 1) % photoCount); }} 
              style={{ position:'absolute', top:'50%', right:5, transform:'translateY(-50%)', background:'rgba(0,0,0,0.5)', color:'white', border:'none', borderRadius:'50%', width:'28px', height:'28px', cursor:'pointer', display:'flex', alignItems:'center', justifyContent:'center', fontSize:'14px' }}>&#10095;</button>
          </>
        )}
        {/* Status badge */}
        <span style={{ position:'absolute', top:10, left:10, padding:'3px 8px', borderRadius:'6px', fontSize:'0.6rem', fontWeight:800, letterSpacing:'0.05em',
          background: spot.verification_status === 'MEDIA_READY' ? '#e91e63' : spot.verification_status === 'ENRICHED' ? '#ff9800' : spot.verification_status === 'INDEXED' ? '#2196f3' : 'rgba(0,0,0,0.6)',
          color: '#fff' }}>{spot.verification_status || 'PENDING'}</span>
        {/* Photo type badge — shows what category the current photo is */}
        {photoCount > 0 && currentPhotoType !== 'unknown' && (
          <span style={{ position:'absolute', bottom:8, left:8, padding:'2px 7px', borderRadius:'4px', fontSize:'0.58rem', fontWeight:800, letterSpacing:'0.05em',
            background: 'rgba(0,0,0,0.75)', color: currentTypeLabel.color, border: `1px solid ${currentTypeLabel.color}40` }}>
            {currentTypeLabel.icon} {currentPhotoType.replace('_',' ').toUpperCase()}
          </span>
        )}
        {/* Open Now badge */}
        {openStatus !== null && (
          <span style={{ position:'absolute', top:10, right:10, padding:'3px 8px', borderRadius:'6px', fontSize:'0.6rem', fontWeight:800,
            background: openStatus ? 'rgba(76,175,80,0.9)' : 'rgba(244,67,54,0.85)', color:'#fff' }}>
            {openStatus ? 'OPEN NOW' : 'CLOSED'}
          </span>
        )}
        {/* Quick Media Actions */}
        <div style={{ position:'absolute', bottom:8, right:8, display:'flex', alignItems:'center', gap:'6px' }}>
          {/* LIVE badge */}
          {spot.is_published && (
            <span style={{ padding:'2px 8px', borderRadius:'4px', fontSize:'0.58rem', fontWeight:800, background:'#4caf50', color:'#fff', letterSpacing:'0.06em' }}>LIVE</span>
          )}
          {photoCount > 0 && (
            <span style={{ padding:'2px 7px', borderRadius:'4px', fontSize:'0.58rem', fontWeight:700, background:'rgba(0,0,0,0.65)', color:'#fff' }}>
              {validPhotoIndex + 1} / {photoCount}
            </span>
          )}
          {!readOnly && photoCount > 0 && (
            <div style={{ display:'flex', gap:'4px', background:'rgba(0,0,0,0.7)', padding:'2px', borderRadius:'6px', zIndex: 5 }}>
              <button onClick={(e) => { e.stopPropagation(); onSetHero?.(spot.id, validPhotoIndex); setPhotoIndex(0); }} style={{ background:'transparent', color: validPhotoIndex === 0 ? '#ffb300' : 'rgba(255,255,255,0.4)', border:'none', borderRadius:'4px', padding:'2px 6px', fontSize:'1rem', cursor:'pointer' }} title="Set Hero Image">{validPhotoIndex === 0 ? '★' : '☆'}</button>
              <button onClick={(e) => { e.stopPropagation(); onDeletePhoto?.(spot.id, validPhotoIndex); }} style={{ background:'transparent', color:'#ff3b30', border:'none', borderRadius:'4px', padding:'2px 6px', fontSize:'0.65rem', cursor:'pointer' }} title="Delete Image">🗑</button>
              <button onClick={(e) => { e.stopPropagation(); setShowTypeMenu(true); }} style={{ background:'transparent', color:'#64b5f6', border:'none', borderRadius:'4px', padding:'2px 6px', fontSize:'0.65rem', cursor:'pointer', fontWeight: 800 }} title="Assign Type">TAG</button>
              <button onClick={handleUploadClick} style={{ background:'transparent', color:'#4caf50', border:'none', borderRadius:'4px', padding:'2px 6px', fontSize:'0.65rem', cursor:'pointer', fontWeight: 800 }} title="Upload Photo">UP</button>
            </div>
          )}
          <input type="file" ref={fileInputRef} style={{ display: 'none' }} accept="image/jpeg, image/png, image/webp" onChange={handleFileChange} />
        </div>
      </div>

      {/* Photo coverage chips */}
      {Object.keys(photoCoverage).length > 0 && (
        <div style={{ display:'flex', gap:'4px', padding:'6px 10px', flexWrap:'wrap', borderBottom:'1px solid rgba(255,255,255,0.05)', background:'rgba(0,0,0,0.2)' }}>
          {coverageCategories.map(cat => {
            const has = photoCoverage[cat];
            const label = PHOTO_TYPE_LABELS[cat];
            return (
              <span key={cat} style={{ padding:'1px 6px', borderRadius:'3px', fontSize:'0.52rem', fontWeight:700, letterSpacing:'0.04em',
                background: has ? `${label.color}18` : 'rgba(255,255,255,0.03)',
                border: `1px solid ${has ? label.color + '40' : 'rgba(255,255,255,0.08)'}`,
                color: has ? label.color : 'rgba(255,255,255,0.2)' }}>
                {has ? '✓' : '✗'} {label.icon} {cat.replace('_',' ')}
              </span>
            );
          })}
          {spot.logo_url && (
            <span style={{ padding:'1px 6px', borderRadius:'3px', fontSize:'0.52rem', fontWeight:700, background:'rgba(79,195,247,0.1)', border:'1px solid rgba(79,195,247,0.3)', color:'#4fc3f7' }}>✓ 🏷️ logo</span>
          )}
        </div>
      )}

      {/* Card body */}
      <div style={{ padding:'14px 16px', flex:1, display:'flex', flexDirection:'column', gap:'10px' }}>

        {/* Name + location + logo avatar */}
        <div style={{ display:'flex', alignItems:'flex-start', gap:'10px' }}>
          {spot.logo_url && (
            <img
              src={proxyImg(spot.logo_url) || ''}
              alt={`${spot.name} logo`}
              style={{ width:'36px', height:'36px', borderRadius:'8px', objectFit:'contain', background:'rgba(255,255,255,0.06)', border:'1px solid rgba(255,255,255,0.1)', flexShrink:0 }}
              onError={e => { (e.target as HTMLImageElement).style.display='none'; }}
            />
          )}
          <div style={{ flex:1, minWidth:0 }}>
          <div style={{ fontWeight:800, fontSize:'1rem', lineHeight:1.2, marginBottom:'3px' }}>{spot.name}</div>
            {isGlowEmpty('street_address') ? (
              <div style={{
                border: '1px dashed rgba(255, 51, 102, 0.6)',
                boxShadow: '0 0 10px rgba(255, 51, 102, 0.35), inset 0 0 5px rgba(255, 51, 102, 0.15)',
                background: 'rgba(255, 51, 102, 0.05)',
                padding: '5px 9px',
                borderRadius: '6px',
                fontSize: '0.68rem',
                color: '#ff3366',
                fontWeight: 'bold',
                display: 'inline-flex',
                alignItems: 'center',
                gap: '4px',
                marginTop: '4px'
              }}>
                <span>📍</span> MISSING HIGH-PRIORITY STREET ADDRESS!
              </div>
            ) : (
              <div style={{ fontSize:'0.72rem', color:'rgba(255,255,255,0.45)' }}>
                {[spot.street_address || spot.address, spot.city, spot.state, spot.zip].filter(Boolean).join(', ')}
              </div>
            )}
          </div>
        </div>

        {/* Rating row */}
        {ratingNum && (
          <div style={{ display:'flex', alignItems:'center', gap:'6px' }}>
            <span style={{ color:'#ffc107', fontSize:'0.85rem' }} dangerouslySetInnerHTML={{ __html: stars(ratingNum) }} />
            <span style={{ fontWeight:700, fontSize:'0.82rem' }}>{ratingNum.toFixed(1)}</span>
            {spot.user_ratings_total && <span style={{ fontSize:'0.68rem', color:'rgba(255,255,255,0.35)' }}>({spot.user_ratings_total.toLocaleString()} reviews)</span>}
          </div>
        )}

        {/* Facility chips */}
        <div style={{ display:'flex', gap:'5px', flexWrap:'wrap' }}>
          {spot.facility_type && <span style={{ padding:'2px 8px', borderRadius:'12px', fontSize:'0.6rem', fontWeight:700, background:'rgba(138,43,226,0.2)', border:'1px solid rgba(138,43,226,0.4)', color:'#c084fc' }}>{spot.facility_type}</span>}
          {adultNight && <span style={{ padding:'2px 8px', borderRadius:'12px', fontSize:'0.6rem', fontWeight:700, background:'rgba(233,30,99,0.15)', border:'1px solid rgba(233,30,99,0.35)', color:'#f48fb1' }}>18+ Night</span>}
          {proShop && <span style={{ padding:'2px 8px', borderRadius:'12px', fontSize:'0.6rem', fontWeight:700, background:'rgba(255,152,0,0.15)', border:'1px solid rgba(255,152,0,0.35)', color:'#ffcc80' }}>Pro Shop</span>}
          {spot.has_rental && <span style={{ padding:'2px 8px', borderRadius:'12px', fontSize:'0.6rem', fontWeight:700, background:'rgba(33,150,243,0.15)', border:'1px solid rgba(33,150,243,0.3)', color:'#90caf9' }}>Rentals</span>}
          {spot.hosts_derby && <span style={{ padding:'2px 8px', borderRadius:'12px', fontSize:'0.6rem', fontWeight:700, background:'rgba(76,175,80,0.15)', border:'1px solid rgba(76,175,80,0.3)', color:'#a5d6a7' }}>Derby</span>}
          {spot.surface_type && <span style={{ padding:'2px 8px', borderRadius:'12px', fontSize:'0.6rem', fontWeight:700, background:'rgba(255,255,255,0.05)', border:'1px solid rgba(255,255,255,0.12)', color:'rgba(255,255,255,0.5)' }}>{String(spot.surface_type)}</span>}
        </div>

        {/* Today's hours + Open Now */}
        {(hours || isGlowEmpty('opening_hours')) && (
          <div style={{ fontSize:'0.72rem', color:'rgba(255,255,255,0.55)' }}>
            {isGlowEmpty('opening_hours') ? (
              <div style={{
                border: '1px dashed rgba(255, 51, 102, 0.6)',
                boxShadow: '0 0 10px rgba(255, 51, 102, 0.35), inset 0 0 5px rgba(255, 51, 102, 0.15)',
                background: 'rgba(255, 51, 102, 0.05)',
                padding: '5px 9px',
                borderRadius: '6px',
                fontSize: '0.68rem',
                color: '#ff3366',
                fontWeight: 'bold',
                display: 'inline-flex',
                alignItems: 'center',
                gap: '4px',
                marginTop: '2px'
              }}>
                <span>⏱️</span> MISSING HIGH-PRIORITY HOURS!
              </div>
            ) : (
              <>
                <span style={{ color:'rgba(255,255,255,0.3)', marginRight:'4px' }}>Today:</span>
                <span style={{ color: openStatus === true ? '#4caf50' : openStatus === false ? '#f44336' : 'rgba(255,255,255,0.55)', fontWeight:600 }}>{todayHours(hours) || 'Hours available'}</span>
              </>
            )}
          </div>
        )}

        {/* Full hours */}
        {hours && hours.length > 0 && (
          <details style={{ fontSize:'0.68rem' }}>
            <summary style={{ color:'rgba(255,255,255,0.3)', cursor:'pointer', fontSize:'0.65rem', userSelect:'none', marginBottom:'4px' }}>All hours ({hours.length} days)</summary>
            {hours.map((h, i) => (
              <div key={i} style={{ display:'flex', justifyContent:'space-between', padding:'1px 0', color:'rgba(255,255,255,0.45)' }}>
                <span style={{ color:'rgba(255,255,255,0.25)', marginRight:'8px' }}>{h.split(':')[0]}</span>
                <span>{h.split(': ').slice(1).join(': ')}</span>
              </div>
            ))}
          </details>
        )}

        {/* Adult night */}
        {(spot.has_adult_night || adultSched || spot.adult_night_details || isGlowEmpty('has_adult_night')) && (
          <div style={{
            borderRadius: '6px',
            background: isGlowEmpty('has_adult_night') ? 'rgba(255, 51, 102, 0.05)' : 'rgba(233,30,99,0.08)',
            border: isGlowEmpty('has_adult_night') ? '1px dashed rgba(255, 51, 102, 0.6)' : '1px solid rgba(233,30,99,0.2)',
            overflow: 'hidden',
            boxShadow: isGlowEmpty('has_adult_night') ? '0 0 10px rgba(255, 51, 102, 0.35)' : 'none'
          }}>
            <div style={{
              padding: '5px 8px',
              background: isGlowEmpty('has_adult_night') ? 'rgba(255, 51, 102, 0.1)' : 'rgba(233,30,99,0.14)',
              display: 'flex',
              alignItems: 'center',
              gap: '5px'
            }}>
              <span style={{
                color: isGlowEmpty('has_adult_night') ? '#ff3366' : '#f48fb1',
                fontWeight: 800,
                fontSize: '0.63rem',
                letterSpacing: '0.05em'
              }}>
                {isGlowEmpty('has_adult_night') ? '⚠️ MISSING HIGH-PRIORITY 18+ ADULT NIGHT STATUS' : '18+ ADULT NIGHT'}
              </span>
            </div>
            {spot.adult_night_details && (
              <div style={{ padding: '5px 8px', color: 'rgba(255,255,255,0.5)', fontSize: '0.66rem', lineHeight: 1.4 }}>{spot.adult_night_details}</div>
            )}
            {adultSched && !spot.adult_night_details && (
              <div style={{ padding: '5px 8px', color: 'rgba(255,255,255,0.4)', fontSize: '0.66rem' }}>{typeof adultSched === 'object' ? JSON.stringify(adultSched) : String(adultSched)}</div>
            )}
          </div>
        )}

        {/* Pricing — only render if we have CONFIRMED data, never assume free */}
        {(() => {
          let priceLabel = spot.price_range || '';
          if (!priceLabel && spot.pricing_data) {
            try {
              const pd = (typeof spot.pricing_data === 'string' ? JSON.parse(spot.pricing_data) : spot.pricing_data) as Record<string, unknown> | null;
              if (pd?.priceAmount) priceLabel = `$${pd.priceAmount} admission`;
              else if (pd?.adult) priceLabel = `Adult: $${pd.adult}`;
            } catch {
              /* Ignore pricing JSON parsing error */
            }
          }
          // 3-state guard: null = unknown (don't render), false = confirmed free, true = confirmed paid
          const feeStatus =
            spot.has_fee === true  ? 'Paid admission' :
            spot.has_fee === false ? 'Free entry' :
            null;
          
          const isGlow = isGlowEmpty('pricing_data');
          if (!priceLabel && !feeStatus && !isGlow) return null; // Unknown — show nothing
          return (
            <div style={{ fontSize:'0.7rem', color:'rgba(255,255,255,0.55)', display:'flex', alignItems:'center', gap:'6px' }}>
              {isGlow ? (
                <div style={{
                  border: '1px dashed rgba(255, 51, 102, 0.6)',
                  boxShadow: '0 0 10px rgba(255, 51, 102, 0.35), inset 0 0 5px rgba(255, 51, 102, 0.15)',
                  background: 'rgba(255, 51, 102, 0.05)',
                  padding: '5px 9px',
                  borderRadius: '6px',
                  fontSize: '0.68rem',
                  color: '#ff3366',
                  fontWeight: 'bold',
                  display: 'inline-flex',
                  alignItems: 'center',
                  gap: '4px',
                  marginTop: '2px'
                }}>
                  <span>💰</span> MISSING HIGH-PRIORITY PRICING DATA / FEES!
                </div>
              ) : (
                <>
                  <span style={{ color:'rgba(255,255,255,0.25)' }}>💰</span>
                  <span>{priceLabel || feeStatus}</span>
                </>
              )}
            </div>
          );
        })()}

        {/* Website */}
        {spot.website && (
          <a href={spot.website} target="_blank" rel="noreferrer"
            style={{ fontSize:'0.7rem', color:'#64b5f6', textDecoration:'none', wordBreak:'break-all', display:'block' }}
            title={spot.website}
          >{spot.website}</a>
        )}

        {/* Phone */}
        {(spot.phone || spot.phone_number || isGlowEmpty('phone_number')) && (
          <div style={{ fontSize:'0.72rem', color:'rgba(255,255,255,0.45)' }}>
            {isGlowEmpty('phone_number') ? (
              <div style={{
                border: '1px dashed rgba(255, 51, 102, 0.6)',
                boxShadow: '0 0 10px rgba(255, 51, 102, 0.35), inset 0 0 5px rgba(255, 51, 102, 0.15)',
                background: 'rgba(255, 51, 102, 0.05)',
                padding: '5px 9px',
                borderRadius: '6px',
                fontSize: '0.68rem',
                color: '#ff3366',
                fontWeight: 'bold',
                display: 'inline-flex',
                alignItems: 'center',
                gap: '4px',
                marginTop: '2px'
              }}>
                <span>📞</span> MISSING HIGH-PRIORITY PHONE NUMBER!
              </div>
            ) : (
              <>
                <span style={{ color:'rgba(255,255,255,0.25)', marginRight:'4px' }}>Ph:</span>
                {spot.phone || spot.phone_number}
              </>
            )}
          </div>
        )}

        {/* Emails */}
        {(() => {
          const emails = getEmails(spot.email_addresses);
          const isGlow = isGlowEmpty('email_addresses');
          if (emails.length === 0 && !isGlow) return null;
          return (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '5px' }}>
              <span style={{ fontSize: '0.62rem', fontWeight: 700, color: isGlow ? '#ff3366' : 'rgba(255,255,255,0.3)', letterSpacing: '0.05em' }}>EMAILS</span>
              <div style={{ display: 'flex', gap: '6px', flexWrap: 'wrap' }}>
                {isGlow ? (
                  <div style={{
                    border: '1px dashed rgba(255, 51, 102, 0.6)',
                    boxShadow: '0 0 10px rgba(255, 51, 102, 0.35), inset 0 0 5px rgba(255, 51, 102, 0.15)',
                    background: 'rgba(255, 51, 102, 0.05)',
                    padding: '5px 9px',
                    borderRadius: '6px',
                    fontSize: '0.68rem',
                    color: '#ff3366',
                    fontWeight: 'bold',
                    display: 'inline-flex',
                    alignItems: 'center',
                    gap: '4px'
                  }}>
                    <span>📧</span> MISSING HIGH-PRIORITY EMAIL ADDRESSES!
                  </div>
                ) : (
                  emails.map((email) => (
                    <a
                      key={email}
                      href={`mailto:${email}`}
                      style={{
                        fontSize: '0.65rem',
                        color: '#c084fc',
                        textDecoration: 'none',
                        padding: '3px 8px',
                        borderRadius: '10px',
                        border: '1px solid rgba(168,85,247,0.35)',
                        background: 'rgba(168,85,247,0.1)',
                        display: 'inline-flex',
                        alignItems: 'center',
                        gap: '4px',
                        transition: 'all 0.2s',
                        boxShadow: '0 0 6px rgba(168,85,247,0.15)',
                      }}
                      onMouseEnter={e => {
                        (e.currentTarget as HTMLElement).style.background = 'rgba(168,85,247,0.2)';
                        (e.currentTarget as HTMLElement).style.boxShadow = '0 0 10px rgba(168,85,247,0.4)';
                        (e.currentTarget as HTMLElement).style.borderColor = 'rgba(168,85,247,0.6)';
                      }}
                      onMouseLeave={e => {
                        (e.currentTarget as HTMLElement).style.background = 'rgba(168,85,247,0.1)';
                        (e.currentTarget as HTMLElement).style.boxShadow = '0 0 6px rgba(168,85,247,0.15)';
                        (e.currentTarget as HTMLElement).style.borderColor = 'rgba(168,85,247,0.35)';
                      }}
                    >
                      <span>📧</span>
                      <span>{email}</span>
                    </a>
                  ))
                )}
              </div>
            </div>
          );
        })()}

        {/* Social links */}
        {(igUrl || fbUrl || ttUrl || isGlowEmpty('instagram_url')) && (
          <div style={{ display:'flex', gap:'8px', flexWrap:'wrap', flexDirection: isGlowEmpty('instagram_url') ? 'column' : 'row' }}>
            {isGlowEmpty('instagram_url') ? (
              <div style={{
                border: '1px dashed rgba(255, 51, 102, 0.6)',
                boxShadow: '0 0 10px rgba(255, 51, 102, 0.35), inset 0 0 5px rgba(255, 51, 102, 0.15)',
                background: 'rgba(255, 51, 102, 0.05)',
                padding: '5px 9px',
                borderRadius: '6px',
                fontSize: '0.68rem',
                color: '#ff3366',
                fontWeight: 'bold',
                display: 'inline-flex',
                alignItems: 'center',
                gap: '4px',
                marginTop: '2px'
              }}>
                <span>🌐</span> MISSING HIGH-PRIORITY SOCIALS!
              </div>
            ) : (
              <>
                {igUrl && <a href={igUrl} target="_blank" rel="noreferrer" style={{ fontSize:'0.65rem', color:'#c13584', textDecoration:'none', padding:'2px 8px', borderRadius:'10px', border:'1px solid rgba(193,53,132,0.35)', background:'rgba(193,53,132,0.1)' }}>Instagram</a>}
                {fbUrl && <a href={fbUrl} target="_blank" rel="noreferrer" style={{ fontSize:'0.65rem', color:'#1877f2', textDecoration:'none', padding:'2px 8px', borderRadius:'10px', border:'1px solid rgba(24,119,242,0.35)', background:'rgba(24,119,242,0.1)' }}>Facebook</a>}
                {ttUrl && <a href={ttUrl} target="_blank" rel="noreferrer" style={{ fontSize:'0.65rem', color:'#ff0050', textDecoration:'none', padding:'2px 8px', borderRadius:'10px', border:'1px solid rgba(255,0,80,0.35)', background:'rgba(255,0,80,0.1)' }}>TikTok</a>}
              </>
            )}
          </div>
        )}

        {/* Pipeline health row */}
        <div style={{ display:'flex', gap:'6px', flexWrap:'wrap', marginTop:'auto', paddingTop:'8px', borderTop:'1px solid rgba(255,255,255,0.06)' }}>
          <span style={{ fontSize:'0.58rem', fontWeight:700, padding:'2px 6px', borderRadius:'4px',
            background: spot.is_deep_crawled ? 'rgba(76,175,80,0.15)' : 'rgba(255,255,255,0.05)',
            border: `1px solid ${spot.is_deep_crawled ? 'rgba(76,175,80,0.3)' : 'rgba(255,255,255,0.1)'}`,
            color: spot.is_deep_crawled ? '#81c784' : 'rgba(255,255,255,0.25)' }}>
            {spot.is_deep_crawled ? 'DEEP CRAWLED' : 'NOT CRAWLED'}
          </span>
          {photoCount > 0 && <span style={{ fontSize:'0.58rem', fontWeight:700, padding:'2px 6px', borderRadius:'4px', background:'rgba(233,30,99,0.1)', border:'1px solid rgba(233,30,99,0.25)', color:'#f48fb1' }}>{photoCount} Photo{photoCount>1?'s':''}</span>}
          {spot.google_place_id && <span style={{ fontSize:'0.58rem', fontWeight:700, padding:'2px 6px', borderRadius:'4px', background:'rgba(66,133,244,0.1)', border:'1px solid rgba(66,133,244,0.25)', color:'#90caf9' }}>Google ID</span>}
          {spot.last_enriched_at && <span style={{ fontSize:'0.56rem', color:'rgba(255,255,255,0.2)', marginLeft:'auto' }}>Enriched: {new Date(spot.last_enriched_at).toLocaleDateString()}</span>}
        </div>

        {/* Publish toggle + quick actions */}
        {!readOnly && (
          <div style={{ display:'flex', gap:'8px', alignItems:'center', marginTop:'4px' }}>
            <label style={{ display:'flex', alignItems:'center', gap:'6px', cursor:'pointer', fontSize:'0.7rem', fontWeight:700,
              color: spot.is_published ? '#4caf50' : 'rgba(255,255,255,0.4)' }}>
              <input type="checkbox" checked={!!spot.is_published}
                onChange={() => onPublishToggle?.(spot)} 
                style={{ accentColor:'#4caf50', width:'14px', height:'14px' }} />
              {spot.is_published ? 'LIVE' : 'Publish'}
            </label>
            <button onClick={(e) => { e.stopPropagation(); onEdit?.(spot); }}
              style={{ marginLeft:'auto', padding:'4px 10px', borderRadius:'6px', border:'1px solid rgba(255,255,255,0.15)', background:'transparent', color:'rgba(255,255,255,0.5)', cursor:'pointer', fontSize:'0.65rem' }}>Edit</button>
            <button onClick={(e) => { e.stopPropagation(); onReset?.(spot.id, spot.name); }} title="Reset to SEEDED"
              style={{ padding:'4px 10px', borderRadius:'6px', border:'1px solid rgba(255,179,0,0.25)', background:'rgba(255,179,0,0.08)', color:'#ffb300', cursor:'pointer', fontSize:'0.65rem', fontWeight:700 }}>&#8635; Reset</button>
            <button onClick={(e) => { e.stopPropagation(); onBlock?.(spot.id, spot.name); }}
              style={{ padding:'4px 10px', borderRadius:'6px', border:'1px solid rgba(255,152,0,0.25)', background:'rgba(255,152,0,0.1)', color:'rgba(255,152,0,0.8)', cursor:'pointer', fontSize:'0.65rem', fontWeight:700 }}>Block</button>
          </div>
        )}
      </div>
    </div>
  );
};

