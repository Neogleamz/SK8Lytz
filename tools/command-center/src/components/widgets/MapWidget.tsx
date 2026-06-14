import React, { useEffect, useState, useMemo, useRef, useCallback } from 'react';
import { supabase } from '../../services/supabase';
import USAMap from './USMap';
import { EntityCardOverlay } from './EntityCardOverlay';
import RelationalDataBankWidget from './RelationalDataBankWidget';
import type { FilteredIds } from './RelationalDataBankWidget';
import { AgGridReact } from 'ag-grid-react';
import { ModuleRegistry, AllCommunityModule, themeQuartz, colorSchemeDark } from 'ag-grid-community';
import type { ColDef } from 'ag-grid-community';

// AG Grid v35+ strict module registration
ModuleRegistry.registerModules([AllCommunityModule]);

// Force Dark Mode via JS Theme API with SK8Lytz Glassmorphism Cyan styling
const myTheme = themeQuartz.withPart(colorSchemeDark).withParams({
  backgroundColor: 'transparent',
  foregroundColor: '#cbd5e1', // slate-300
  headerBackgroundColor: 'rgba(15, 23, 42, 0.4)', // slate-900 transparent
  headerTextColor: '#22d3ee', // cyan-400
  borderColor: 'rgba(51, 65, 85, 0.5)', // slate-700
  accentColor: '#22d3ee', // cyan-400
  fontFamily: '"Inter", sans-serif',
  rowHoverColor: 'rgba(34, 211, 238, 0.1)', // cyan-400 with opacity
  selectedRowBackgroundColor: 'rgba(34, 211, 238, 0.2)',
});

export interface MapPoint {
  id: string;
  lat: number;
  lng: number;
  label?: string;
  type: 'users' | 'registeredDevices' | 'skateSessions' | 'crews' | 'crewSessions';
  user_id?: string;
}

interface DeviceData {
  id: string;
  user_id: string;
  device_mac: string;
  custom_name: string;
  firmware_version: string;
  battery_level: number;
  last_lat: number | null;
  last_lng: number | null;
  last_long?: number | null; // for fallback
  is_online: boolean;
  last_seen: string;
}

export const MapWidget: React.FC = () => {
  const [devices, setDevices] = useState<DeviceData[]>([]);
  const [filteredDevices, setFilteredDevices] = useState<DeviceData[]>([]);
  const [colDefs, setColDefs] = useState<ColDef<any>[]>([]);
  const [activeEntity, setActiveEntity] = useState<{ id: string; type: MapPoint['type'] } | null>(null);

  // Separate Map Layers
  const [userPoints, setUserPoints] = useState<MapPoint[]>([]);
  const [devicePoints, setDevicePoints] = useState<MapPoint[]>([]);
  const [skatePoints, setSkatePoints] = useState<MapPoint[]>([]);
  const [crewPoints, setCrewPoints] = useState<MapPoint[]>([]);
  const [crewSessionPoints, setCrewSessionPoints] = useState<MapPoint[]>([]);

  const [layers, setLayers] = useState({
    users: true,
    registeredDevices: true,
    skateSessions: true,
    crews: true,
    crewSessions: true,
  });

  const [zoom, setZoom] = useState({ scale: 1, x: 0, y: 0 });
  const [filteredIds, setFilteredIds] = useState<FilteredIds | null>(null);

  const [selectedCluster, setSelectedCluster] = useState<MapPoint[] | null>(null);
  const mapRef = useRef<HTMLDivElement>(null);

  // Helper for Section Headers (Reused from FleetDashboard)
  const SectionHdr = ({ label, color, right }: { label: string, color: string, right?: React.ReactNode }) => (
    <div style={{ display:'flex', justifyContent:'space-between', alignItems:'center', borderBottom:`1px solid ${color}40`, paddingBottom:'6px', marginBottom:'16px' }}>
      <span style={{ color, fontWeight:800, fontSize:'0.85rem', textTransform:'uppercase', letterSpacing:'0.05em' }}>{label}</span>
      <span style={{ display:'flex', alignItems:'center', gap:'10px' }}>
        {right}
        <span style={{ fontSize:'0.65rem', color:'rgba(255,255,255,0.25)', fontWeight:700 }}>{isMapCollapsed ? '▼ show' : '▲ hide'}</span>
      </span>
    </div>
  );

  const [isMapCollapsed, setIsMapCollapsed] = useState(() => {
    return localStorage.getItem('map_collapsed') === 'true';
  });

  const [hoveredCluster, setHoveredCluster] = useState<number | null>(null);
  const [isDragging, setIsDragging] = useState(false);
  const dragStartRef = useRef({ x: 0, y: 0 });
  const containerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    localStorage.setItem('map_collapsed', isMapCollapsed.toString());
  }, [isMapCollapsed]);

  useEffect(() => {
    const el = containerRef.current;
    if (!el) return;
    const onWheel = (e: WheelEvent) => {
      e.preventDefault();
      const delta = e.deltaY > 0 ? 0.85 : 1.15;
      setZoom(z => ({ ...z, scale: Math.max(1, Math.min(z.scale * delta, 64)) }));
    };
    el.addEventListener('wheel', onWheel, { passive: false });
    return () => el.removeEventListener('wheel', onWheel);
  }, [containerRef.current]);

  // JSON location parsing helper
  const parseJsonLocation = (locInput: any): { lat: number, lng: number } | null => {
    if (!locInput) return null;
    let loc = locInput;
    if (typeof loc === 'string') {
      try { loc = JSON.parse(loc); } catch (e) { return null; }
    }
    let lat = null, lng = null;
    if (Array.isArray(loc)) {
      lng = loc[0];
      lat = loc[1];
    } else if (loc.type === 'Point' && Array.isArray(loc.coordinates)) {
      lng = loc.coordinates[0];
      lat = loc.coordinates[1];
    } else if (loc.lat !== undefined || loc.latitude !== undefined) {
      lat = loc.lat !== undefined ? loc.lat : loc.latitude;
      lng = loc.lng !== undefined ? loc.lng : (loc.lon !== undefined ? loc.lon : (loc.long !== undefined ? loc.long : loc.longitude));
    }
    if (lat !== null && lng !== null && !isNaN(lat) && !isNaN(lng)) {
      return { lat: Number(lat), lng: Number(lng) };
    }
    return null;
  };

  const parseStringLocation = (locStr: string): { lat: number, lng: number } | null => {
    if (typeof locStr === 'string') {
      if (locStr.includes('POINT')) {
        const match = locStr.match(/POINT\s*\(\s*([^\s]+)\s+([^\s]+)\s*\)/i);
        if (match) {
          const lng = parseFloat(match[1]);
          const lat = parseFloat(match[2]);
          if (!isNaN(lat) && !isNaN(lng)) return { lat, lng };
        }
      } else {
        const parts = locStr.split(',');
        if (parts.length === 2) {
          const lat = parseFloat(parts[0].trim());
          const lng = parseFloat(parts[1].trim());
          if (!isNaN(lat) && !isNaN(lng)) return { lat, lng };
        }
      }
    }
    return null;
  };

  const [debugData, setDebugData] = useState<any>(null);

  const fetchData = async () => {

  const jitter = () => (Math.random() - 0.5) * 0.0005;
    const uPoints: MapPoint[] = [];
    const seenUsers = new Set();
    
    // Default US Center for missing coordinates
    const DEFAULT_LAT = 39.8283;
    const DEFAULT_LNG = -98.5795;

    // 1. Fetch Registered Devices
    const { data: rawDevices } = await supabase.from('registered_devices').select('*');
    if (rawDevices) {
      const devs = rawDevices as unknown as DeviceData[];
      setDevices(devs);
      setFilteredDevices(devs);
      
      const dPoints: MapPoint[] = [];
      devs.forEach((d: any) => {
        let latVal = DEFAULT_LAT;
        let lngVal = DEFAULT_LNG;
        if (d.last_lat !== undefined && d.last_lat !== null) latVal = Number(d.last_lat);
        if (d.last_lng !== undefined && d.last_lng !== null) lngVal = Number(d.last_lng);
        
        if (!isNaN(latVal) && !isNaN(lngVal)) {
          dPoints.push({
            id: d.id,
            lat: latVal + jitter() * 20, // Spread them out more if they are stacked
            lng: lngVal + jitter() * 20,
            label: `Device: ${d.custom_name || d.device_mac || d.id}`,
            type: 'registeredDevices',
            user_id: d.user_id
          });

          if (d.user_id && !seenUsers.has(d.user_id)) {
            seenUsers.add(d.user_id);
            uPoints.push({
              id: `user_${d.user_id}`,
              lat: latVal + jitter() * 20,
              lng: lngVal + jitter() * 20,
              label: `User: ${d.user_id}`,
              type: 'users',
              user_id: d.user_id
            });
          }
        }
      });
      setDevicePoints(dPoints);
      
      if (devs.length > 0) {
        const keys = Object.keys(devs[0]);
        const dynamicCols: ColDef<any>[] = keys.map(k => ({
          field: k,
          headerName: k.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase()),
          filter: true,
          sortable: true
        }));
        setColDefs(dynamicCols);
      }
    }

    // 2. Fetch Skate Sessions (proxy for Users as well)
    const { data: sSessions, error: sError } = await supabase.from('skate_sessions').select('*').limit(200);
    if (sSessions && sSessions.length > 0) {
      setDebugData(sSessions[0]);
    }
    const sPoints: MapPoint[] = [];
    if (sSessions) {
      sSessions.forEach((s: any) => {
        const coords = parseJsonLocation(s.start_coords) || parseJsonLocation(s.location_coords) || parseStringLocation(s.location_coords) || { lat: DEFAULT_LAT, lng: DEFAULT_LNG };
        if (coords) {
          sPoints.push({
            id: `skate_${s.id}`,
            lat: coords.lat + jitter() * 20,
            lng: coords.lng + jitter() * 20,
            label: `Skate Session: ${s.user_id}`,
            type: 'skateSessions',
            user_id: s.user_id
          });
          
          if (s.user_id && !seenUsers.has(s.user_id)) {
            seenUsers.add(s.user_id);
            uPoints.push({
              id: `user_${s.user_id}`,
              lat: coords.lat + jitter() * 20,
              lng: coords.lng + jitter() * 20,
              label: `User: ${s.user_id}`,
              type: 'users',
              user_id: s.user_id
            });
          }
        }
      });
    }
    setSkatePoints(sPoints);
    setUserPoints(uPoints);

    // 3. Fetch Crew Sessions (proxy for Crews as well)
    const { data: cSessions } = await supabase.from('crew_sessions').select('id, crew_id, leader_user_id, name, location_coords').order('created_at', { ascending: false }).limit(100);
    if (cSessions) {
      const cPoints: MapPoint[] = [];
      const crewPts: MapPoint[] = [];
      const seenCrews = new Set();

      cSessions.forEach((c: any) => {
        const coords = parseJsonLocation(c.location_coords) || parseStringLocation(c.location_coords) || { lat: DEFAULT_LAT, lng: DEFAULT_LNG };
        if (coords) {
          cPoints.push({
            id: `crew_session_${c.id}`,
            lat: coords.lat + jitter() * 20,
            lng: coords.lng + jitter() * 20,
            label: `Crew Session: ${c.name || c.id}`,
            type: 'crewSessions',
            user_id: c.leader_user_id
          });
          
          if (c.crew_id && !seenCrews.has(c.crew_id)) {
            seenCrews.add(c.crew_id);
            crewPts.push({
              id: `crew_${c.crew_id}`,
              lat: coords.lat + jitter() * 20,
              lng: coords.lng + jitter() * 20,
              label: `Crew: ${c.crew_id}`,
              type: 'crews',
              user_id: c.leader_user_id
            });
          }
        }
      });
      setCrewSessionPoints(cPoints);
      setCrewPoints(crewPts);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const visibleUserPoints = useMemo(() => filteredIds ? userPoints.filter(p => p.user_id && filteredIds.users.has(p.user_id)) : userPoints, [userPoints, filteredIds]);
  const visibleDevicePoints = useMemo(() => filteredIds ? devicePoints.filter(p => filteredIds.devices.has(p.id)) : devicePoints, [devicePoints, filteredIds]);
  const visibleSkatePoints = useMemo(() => filteredIds ? skatePoints.filter(p => filteredIds.sessions.has(p.id.replace('skate_', ''))) : skatePoints, [skatePoints, filteredIds]);
  const visibleCrewPoints = useMemo(() => filteredIds ? crewPoints.filter(p => filteredIds.crews.has(p.id.replace('crew_', ''))) : crewPoints, [crewPoints, filteredIds]);
  const visibleCrewSessionPoints = useMemo(() => filteredIds ? crewSessionPoints.filter(p => filteredIds.crewSessions.has(p.id.replace('crew_session_', ''))) : crewSessionPoints, [crewSessionPoints, filteredIds]);

  const activeMapPoints: MapPoint[] = [];
  if (layers.users) activeMapPoints.push(...visibleUserPoints);
  if (layers.registeredDevices) activeMapPoints.push(...visibleDevicePoints);
  if (layers.skateSessions) activeMapPoints.push(...visibleSkatePoints);
  if (layers.crews) activeMapPoints.push(...visibleCrewPoints);
  if (layers.crewSessions) activeMapPoints.push(...visibleCrewSessionPoints);

  const GRID_SIZE = Math.floor(30 * zoom.scale);
  const clusteredPoints = useMemo(() => {
    if (activeMapPoints.length === 0) return [];
    
    let minLat = Infinity, maxLat = -Infinity;
    let minLng = Infinity, maxLng = -Infinity;
    
    activeMapPoints.forEach(p => {
      if (p.lat < minLat) minLat = p.lat;
      if (p.lat > maxLat) maxLat = p.lat;
      if (p.lng < minLng) minLng = p.lng;
      if (p.lng > maxLng) maxLng = p.lng;
    });

    if (minLat === Infinity) return [];
    
    const latStep = (maxLat - minLat) / GRID_SIZE || 0.1;
    const lngStep = (maxLng - minLng) / GRID_SIZE || 0.1;

    const clusters = new Map<string, { count: number; lat: number; lng: number; types: Set<string>; users: Set<string>; id: string; user_id?: string; points: MapPoint[] }>();

    activeMapPoints.forEach(p => {
      const gridX = Math.floor((p.lng - minLng) / lngStep);
      const gridY = Math.floor((p.lat - minLat) / latStep);
      const key = `${gridX},${gridY}`;
      
      if (!clusters.has(key)) {
        clusters.set(key, { count: 0, lat: 0, lng: 0, types: new Set(), users: new Set(), id: p.id, user_id: p.user_id, points: [] });
      }
      const cluster = clusters.get(key)!;
      cluster.count += 1;
      cluster.lat += p.lat;
      cluster.lng += p.lng;
      cluster.types.add(p.type);
      cluster.points.push(p);
      if (p.user_id) cluster.users.add(p.user_id);
    });

    return Array.from(clusters.values()).map(c => ({
      count: c.count,
      lat: c.lat / c.count,
      lng: c.lng / c.count,
      types: Array.from(c.types),
      users: Array.from(c.users),
      id: c.id,
      user_id: c.user_id,
      points: c.points
    }));
  }, [activeMapPoints, zoom.scale]);

  const handleZoomIn = useCallback((e: React.MouseEvent | React.TouchEvent, cluster: any) => {
    if (e) {
      e.stopPropagation();
      e.preventDefault();
    }
    
    if (zoom.scale >= 32 || cluster.points.length > 15) {
      setSelectedCluster(cluster.points);
      return;
    }
    
    if (cluster.points.length === 1) {
      setSelectedCluster(cluster.points);
      const pX = (cluster.lng + 125) * (100 / 58);
      const pY = (50 - cluster.lat) * (100 / 25);
      setZoom({ scale: 64, x: (50 - pX) * 9.59, y: (50 - pY) * 5.93 });
      return;
    }

    const lats = cluster.points.map((p:any) => p.lat);
    const lngs = cluster.points.map((p:any) => p.lng);
    const minLat = Math.min(...lats), maxLat = Math.max(...lats);
    const minLng = Math.min(...lngs), maxLng = Math.max(...lngs);
    
    const dLat = maxLat - minLat;
    const dLng = maxLng - minLng;
    const targetScale = Math.min(64, Math.max(zoom.scale * 2.5, 2 / Math.max(dLat, dLng, 0.0001)));
    
    const cLng = (minLng + maxLng) / 2;
    const cLat = (minLat + maxLat) / 2;
    const pX = (cLng + 125) * (100 / 58);
    const pY = (50 - cLat) * (100 / 25);
    
    setZoom({ scale: targetScale, x: (50 - pX) * 9.59, y: (50 - pY) * 5.93 });
  }, [zoom.scale]);

  const handleMouseDown = useCallback((e: React.MouseEvent) => {
    setIsDragging(true);
    dragStartRef.current = { x: e.clientX, y: e.clientY };
  }, []);

  const handleMouseMove = useCallback((e: React.MouseEvent) => {
    if (!isDragging) return;
    const dx = e.clientX - dragStartRef.current.x;
    const dy = e.clientY - dragStartRef.current.y;
    dragStartRef.current = { x: e.clientX, y: e.clientY };
    setZoom(z => ({ ...z, x: z.x + dx / z.scale, y: z.y + dy / z.scale }));
  }, [isDragging]);

  const handleMouseUp = useCallback(() => {
    setIsDragging(false);
  }, []);

  const handleResetZoom = useCallback((e: React.MouseEvent | React.TouchEvent) => {
    e.stopPropagation();
    setZoom({ scale: 1, x: 0, y: 0 });
    setActiveEntity(null);
  }, []);

  const getMarkerColor = (types: string[]) => {
    if (types.length > 1) return '#e2e8f0'; 
    if (types.includes('users')) return '#4ade80';
    if (types.includes('registeredDevices')) return '#22d3ee';
    if (types.includes('skateSessions')) return '#facc15';
    if (types.includes('crews')) return '#c084fc';
    if (types.includes('crewSessions')) return '#f472b6';
    return '#94a3b8';
  };

  const handleEntityClick = (id: string, type: MapPoint['type']) => {
    setActiveEntity({ id, type });
  };

  const paginationPageSizeSelector = useMemo(() => [10, 20, 50, 100], []);

  return (
    <div className="h-full flex flex-col gap-6 overflow-y-auto pr-2 pb-10">
      <SectionHdr 
        label="Dynamic Fleet Ops" 
        color="#22d3ee" 
        right={
          <>
            <div className="flex items-center gap-2 mr-4 bg-slate-800 px-2 py-1 rounded border border-slate-600">
              <span className="text-[10px] text-slate-400">ZOOM</span>
              <input 
                type="range" 
                min="1" 
                max="64" 
                step="0.5" 
                value={zoom.scale} 
                onChange={(e) => setZoom(z => ({ ...z, scale: parseFloat(e.target.value) }))}
                className="w-20 h-1 bg-slate-600 rounded-lg appearance-none cursor-pointer"
              />
            </div>
            {zoom.scale > 1 && (
              <button 
                onClick={handleResetZoom}
                className="bg-slate-800 text-slate-300 border border-slate-600 px-2 py-1 rounded text-xs hover:bg-slate-700 transition-colors z-50 mr-4"
              >
                Reset Zoom
              </button>
            )}
          </>
        } 
      />
      
      
      {selectedCluster && (
        <div className="absolute top-0 right-0 w-80 h-full bg-slate-900/95 backdrop-blur border-l border-slate-700 z-50 p-4 overflow-y-auto shadow-2xl flex flex-col animate-in slide-in-from-right-8 duration-300">
          <div className="flex justify-between items-center mb-4 border-b border-slate-700 pb-2">
            <h3 className="text-white font-bold text-sm">Cluster Contents ({selectedCluster.length})</h3>
            <button onClick={() => setSelectedCluster(null)} className="text-slate-400 hover:text-white">&times;</button>
          </div>
          <div className="flex flex-col gap-2">
            {selectedCluster.map((p, i) => (
              <div 
                key={i} 
                className="p-3 bg-slate-800 rounded border border-slate-700 hover:border-cyan-500 cursor-pointer transition-colors"
                onClick={() => {
                  setSelectedCluster(null);
                  setActiveEntity({ id: p.id, type: p.type });
                }}
              >
                <div className="text-xs text-cyan-400 font-mono mb-1">{p.type.toUpperCase()}</div>
                <div className="text-sm text-white font-semibold truncate">{p.label}</div>
                <div className="text-[10px] text-slate-400 mt-1">Lat: {p.lat.toFixed(4)} | Lng: {p.lng.toFixed(4)}</div>
              </div>
            ))}
          </div>
        </div>
      )}
      
      {!isMapCollapsed && (

        <div className="flex flex-row w-full min-h-[400px] gap-4">
          <div className="glass-panel flex-1 rounded-xl relative overflow-hidden border border-slate-800/50 bg-[#0f172a]/80 shadow-[0_8px_32px_rgba(0,0,0,0.4)] flex flex-col shrink-0">
            
            {/* Pill Layer Controls */}
            <div 
              style={{ 
                position: 'absolute', 
                top: '16px', 
                left: '50%', 
                transform: 'translateX(-50%)', 
                zIndex: 30, 
                display: 'flex', 
                gap: '12px', 
                flexDirection: 'row',
                justifyContent: 'center',
                alignItems: 'center',
                backgroundColor: 'rgba(15, 23, 42, 0.7)',
                backdropFilter: 'blur(8px)',
                padding: '8px 16px',
                borderRadius: '9999px',
                border: '1px solid rgba(51, 65, 85, 0.5)',
                boxShadow: '0 8px 32px rgba(0,0,0,0.4)',
                width: 'max-content'
              }}
            >
              <div 
                onClick={() => setLayers(l => ({...l, users: !l.users}))}
                style={{
                  backgroundColor: layers.users ? '#4ade80' : 'transparent',
                  borderColor: '#4ade80', borderWidth: '1px', borderStyle: 'solid',
                  color: layers.users ? '#0f172a' : '#4ade80',
                  opacity: layers.users ? 1 : 0.6, cursor: 'pointer',
                  padding: '6px 16px', borderRadius: '9999px', fontSize: '13px', fontWeight: 'bold', transition: 'all 0.2s',
                  display: 'flex', alignItems: 'center', justifyContent: 'center', whiteSpace: 'nowrap'
                }}
              >
                Users ({visibleUserPoints.length === userPoints.length ? userPoints.length : `${visibleUserPoints.length}/${userPoints.length}`})
              </div>
              <div 
                onClick={() => setLayers(l => ({...l, registeredDevices: !l.registeredDevices}))}
                style={{
                  backgroundColor: layers.registeredDevices ? '#22d3ee' : 'transparent',
                  borderColor: '#22d3ee', borderWidth: '1px', borderStyle: 'solid',
                  color: layers.registeredDevices ? '#0f172a' : '#22d3ee',
                  opacity: layers.registeredDevices ? 1 : 0.6, cursor: 'pointer',
                  padding: '6px 16px', borderRadius: '9999px', fontSize: '13px', fontWeight: 'bold', transition: 'all 0.2s',
                  display: 'flex', alignItems: 'center', justifyContent: 'center', whiteSpace: 'nowrap'
                }}
              >
                 Devices ({visibleDevicePoints.length === devicePoints.length ? devicePoints.length : `${visibleDevicePoints.length}/${devicePoints.length}`})
              </div>
              <div 
                onClick={() => setLayers(l => ({...l, skateSessions: !l.skateSessions}))}
                style={{
                  backgroundColor: layers.skateSessions ? '#facc15' : 'transparent',
                  borderColor: '#facc15', borderWidth: '1px', borderStyle: 'solid',
                  color: layers.skateSessions ? '#0f172a' : '#facc15',
                  opacity: layers.skateSessions ? 1 : 0.6, cursor: 'pointer',
                  padding: '6px 16px', borderRadius: '9999px', fontSize: '13px', fontWeight: 'bold', transition: 'all 0.2s',
                  display: 'flex', alignItems: 'center', justifyContent: 'center', whiteSpace: 'nowrap'
                }}
              >
                 Sessions ({visibleSkatePoints.length === skatePoints.length ? skatePoints.length : `${visibleSkatePoints.length}/${skatePoints.length}`})
              </div>
              <div 
                onClick={() => setLayers(l => ({...l, crews: !l.crews}))}
                style={{
                  backgroundColor: layers.crews ? '#c084fc' : 'transparent',
                  borderColor: '#c084fc', borderWidth: '1px', borderStyle: 'solid',
                  color: layers.crews ? '#0f172a' : '#c084fc',
                  opacity: layers.crews ? 1 : 0.6, cursor: 'pointer',
                  padding: '6px 16px', borderRadius: '9999px', fontSize: '13px', fontWeight: 'bold', transition: 'all 0.2s',
                  display: 'flex', alignItems: 'center', justifyContent: 'center', whiteSpace: 'nowrap'
                }}
              >
                Crews ({visibleCrewPoints.length === crewPoints.length ? crewPoints.length : `${visibleCrewPoints.length}/${crewPoints.length}`})
              </div>
              <div 
                onClick={() => setLayers(l => ({...l, crewSessions: !l.crewSessions}))}
                style={{
                  backgroundColor: layers.crewSessions ? '#f472b6' : 'transparent',
                  borderColor: '#f472b6', borderWidth: '1px', borderStyle: 'solid',
                  color: layers.crewSessions ? '#0f172a' : '#f472b6',
                  opacity: layers.crewSessions ? 1 : 0.6, cursor: 'pointer',
                  padding: '6px 16px', borderRadius: '9999px', fontSize: '13px', fontWeight: 'bold', transition: 'all 0.2s',
                  display: 'flex', alignItems: 'center', justifyContent: 'center', whiteSpace: 'nowrap'
                }}
              >
                Crew Sessions ({visibleCrewSessionPoints.length === crewSessionPoints.length ? crewSessionPoints.length : `${visibleCrewSessionPoints.length}/${crewSessionPoints.length}`})
              </div>
            </div>

            <div 
              ref={containerRef}
              onMouseDown={handleMouseDown}
              onMouseMove={handleMouseMove}
              onMouseUp={handleMouseUp}
              onMouseLeave={handleMouseUp}
              style={{ position: 'relative', width: '100%', flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '1rem', overflow: 'hidden', cursor: isDragging ? 'grabbing' : 'grab' }}
            >
              <div 
                style={{ 
                  position: 'relative', 
                  width: '100%', 
                  maxWidth: '56rem', 
                  opacity: 0.9, 
                  aspectRatio: '959/593',
                  transform: `scale(${zoom.scale}) translate(${zoom.x}px, ${zoom.y}px)`,
                  transformOrigin: 'center center',
                  transition: isDragging ? 'none' : 'transform 0.4s cubic-bezier(0.16, 1, 0.3, 1)'
                }}
                ref={mapRef}
              >
                <USAMap onClick={() => {}} />
                
                {clusteredPoints.map((cluster, i) => {
                  const x = (cluster.lng + 125) * (100 / 58);
                  const y = (50 - cluster.lat) * (100 / 25);
                  
                  if (x < 0 || x > 100 || y < 0 || y > 100) return null;

                  return (
                    <div
                      key={i}
                      onMouseEnter={() => setHoveredCluster(i)}
                      onMouseLeave={() => setHoveredCluster(null)}
                      onClick={(e) => {
                        e.stopPropagation();
                        if (cluster.count > 1) {
                          handleZoomIn(e, cluster);
                        } else {
                          const p = cluster.points[0];
                          if (p) handleEntityClick(p.id, p.type);
                        }
                      }}
                      style={{
                        position: 'absolute',
                        left: `${x}%`,
                        top: `${y}%`,
                        transform: `translate(-50%, -50%) scale(${1 / zoom.scale})`,
                        width: cluster.count > 1 ? '24px' : '12px',
                        height: cluster.count > 1 ? '24px' : '12px',
                        backgroundColor: getMarkerColor(cluster.types),
                        borderRadius: '50%',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        color: '#0f172a',
                        fontWeight: 800,
                        fontSize: cluster.count > 1 ? '10px' : '0px',
                        cursor: 'pointer',
                        boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.5), 0 2px 4px -1px rgba(0, 0, 0, 0.3)',
                        border: '1.5px solid rgba(255,255,255,0.4)',
                        zIndex: hoveredCluster === i ? 50 : 10
                      }}
                    >
                      {cluster.count > 1 && cluster.count}
                      
                      {hoveredCluster === i && (
                        <div style={{
                          position: 'absolute',
                          bottom: '100%',
                          left: '50%',
                          transform: 'translate(-50%, -6px)',
                          background: 'rgba(15, 23, 42, 0.95)',
                          border: '1px solid #334155',
                          color: '#f8fafc',
                          padding: '4px 8px',
                          borderRadius: '4px',
                          fontSize: '11px',
                          whiteSpace: 'nowrap',
                          pointerEvents: 'none',
                          boxShadow: '0 4px 12px rgba(0,0,0,0.5)'
                        }}>
                          {cluster.count > 1 ? `${cluster.count} Items` : (cluster.points?.[0]?.label || 'Item')}
                        </div>
                      )}
                    </div>
                  );
                })}
              </div>
              
              {activeEntity && (
                <EntityCardOverlay activeEntity={activeEntity} onClose={() => setActiveEntity(null)} />
              )}
            </div>
          </div>
        </div>
      )}

      {/* Relational DataBank Section */}
      <div className="flex flex-col gap-2 mt-4" style={{ paddingLeft: '1rem', paddingRight: '1rem' }}>
        <RelationalDataBankWidget 
          activeEntity={activeEntity}
          onEntitySelected={setActiveEntity}
          onFilteredIdsChange={setFilteredIds}
        />
      </div>
    </div>
  );
};

export default MapWidget;
