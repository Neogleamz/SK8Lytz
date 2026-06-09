import React, { useEffect, useState, useMemo, useRef, useCallback } from 'react';
import { supabase } from '../../services/supabase';
import USAMap from './USMap';
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
  headerFontSize: '14px',
  wrapperBorderRadius: '8px',
});

interface DeviceData {
  id: string;
  user_id: string;
  custom_name: string;
  product_type: string | null;
  firmware_ver: number | null;
  is_pending_sync: boolean | null;
  ic_type: string | null;
  last_lat: number | null;
  last_lng: number | null;
  device_mac: string | null;
  registered_at: string | null;
  points: number;
}

interface ClusterNode {
  id: string;
  lat: number;
  lng: number;
  count: number;
  type: string;
  types: string[];
}

interface MapPoint {
  id: string;
  lat: number;
  lng: number;
  label: string;
  type: 'skate' | 'crew' | 'device' | 'telemetry';
}

function latLngToXY(lat: number, lng: number) {
  const xPercent = ((lng - -125) / (-66 - -125)) * 100;
  const yPercent = ((50 - lat) / (50 - 24)) * 100;
  return { 
    left: `${Math.max(0, Math.min(100, xPercent))}%`, 
    top: `${Math.max(0, Math.min(100, yPercent))}%` 
  };
}

export default function MapWidget() {
  // Grid Data
  const [devices, setDevices] = useState<DeviceData[]>([]);
  const [filteredDevices, setFilteredDevices] = useState<DeviceData[]>([]);
  const gridRef = useRef<AgGridReact>(null);

  // Map Data
  const [skatePoints, setSkatePoints] = useState<MapPoint[]>([]);
  const [crewPoints, setCrewPoints] = useState<MapPoint[]>([]);
  const [devicePoints, setDevicePoints] = useState<MapPoint[]>([]);
  const [telemetryPoints, setTelemetryPoints] = useState<MapPoint[]>([]);

  // Toggles
  const [layers, setLayers] = useState({
    skate: true,
    crew: true,
    device: true,
    telemetry: true,
  });

  
  const [zoom, setZoom] = useState({ scale: 1, x: 50, y: 50 });

  const handleZoom = (lat: number, lng: number) => {
    const pos = latLngToXY(lat, lng);
    setZoom({ scale: 4, x: parseFloat(pos.left), y: parseFloat(pos.top) });
  };

  const handleResetZoom = (e: React.MouseEvent) => {
    e.stopPropagation();
    setZoom({ scale: 1, x: 50, y: 50 });
  };

  const SectionHdr = ({ label, color = 'rgba(255,255,255,0.5)', right }: { label: React.ReactNode; color?: string; right?: React.ReactNode }) => (
    <div onClick={() => setIsMapCollapsed(!isMapCollapsed)} style={{ display:'flex', alignItems:'center', justifyContent:'space-between', cursor:'pointer', userSelect:'none', padding:'6px 0', marginBottom: isMapCollapsed ? 0 : '0.5rem' }}>
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

  useEffect(() => {
    localStorage.setItem('map_collapsed', isMapCollapsed.toString());
  }, [isMapCollapsed]);

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
    } else if (loc.lat !== undefined) {
      lat = loc.lat;
      lng = loc.lng !== undefined ? loc.lng : (loc.lon !== undefined ? loc.lon : loc.long);
    }
    if (lat !== null && lng !== null && !isNaN(lat) && !isNaN(lng)) {
      return { lat: Number(lat), lng: Number(lng) };
    }
    return null;
  };

  const parseStringLocation = (locStr: string): { lat: number, lng: number } | null => {
    if (typeof locStr === 'string') {
      if (locStr.includes('POINT')) {
        const match = locStr.match(/POINT\(([^ ]+)\s+([^ ]+)\)/);
        if (match) {
          const lng = parseFloat(match[1]);
          const lat = parseFloat(match[2]);
          if (!isNaN(lat) && !isNaN(lng)) return { lat, lng };
        }
      } else {
        // Fallback for raw "lat, lng" string inputs
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

  const fetchData = async () => {
    // 1. Fetch Registered Devices (for Grid + Map)
    const { data: rawDevices, error: devError } = await supabase
      .from('registered_devices')
      .select('*');

    if (devError) {
      console.error("MapWidget devError:", devError);
      setDevices([]);
      setFilteredDevices([]);
    } else if (rawDevices) {
      const devs = rawDevices as DeviceData[];
      setDevices(devs);
      setFilteredDevices(devs);
      
      const parseCoordinate = (val: any) => {
        if (val === null || val === undefined || val === '') return null;
        if (typeof val === 'number') return !isNaN(val) ? val : null;
        if (typeof val === 'string') {
          const cleaned = val.replace(',', '.').trim();
          const parsed = parseFloat(cleaned);
          return !isNaN(parsed) ? parsed : null;
        }
        return null;
      };

      const dPoints: MapPoint[] = [];
      devs.forEach((d: any) => {
        const latVal = parseCoordinate(d.last_lat);
        const lngVal = parseCoordinate(d.last_lng !== undefined && d.last_lng !== null ? d.last_lng : d.last_long);
        
        if (latVal !== null && lngVal !== null) {
          dPoints.push({
            id: d.id,
            lat: latVal,
            lng: lngVal,
            label: `Device: ${d.custom_name || d.device_mac || d.id}`,
            type: 'device'
          });
        }
      });
      setDevicePoints(dPoints);
      
      // Dynamic columns for Grid
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

    // 2. Fetch Skate Sessions
    const { data: sSessions } = await supabase
      .from('skate_sessions')
      .select('id, user_id, location_coords')
      .not('location_coords', 'is', null) as any;
    
    if (sSessions) {
      const sPoints: MapPoint[] = [];
      sSessions.forEach((s: any) => {
        const coords = parseJsonLocation(s.location_coords) || parseStringLocation(s.location_coords);
        if (coords) {
          sPoints.push({
            id: `skate_${s.id}`,
            lat: coords.lat,
            lng: coords.lng,
            label: `User Session: ${s.user_id}`,
            type: 'skate'
          });
        }
      });
      setSkatePoints(sPoints);
    }

    // 3. Fetch Crew Sessions
    const { data: cSessions } = await supabase
      .from('crew_sessions')
      .select('id, name, location_coords')
      .not('location_coords', 'is', null) as any;
      
    if (cSessions) {
      const cPoints: MapPoint[] = [];
      cSessions.forEach((c: any) => {
        const coords = parseJsonLocation(c.location_coords) || parseStringLocation(c.location_coords);
        if (coords) {
          cPoints.push({
            id: `crew_${c.id}`,
            lat: coords.lat,
            lng: coords.lng,
            label: `Crew: ${c.name || c.id}`,
            type: 'crew'
          });
        }
      });
      setCrewPoints(cPoints);
    }

    // 4. Fetch Telemetry
    const { data: telemetry } = await supabase
      .from('discovered_devices_telemetry')
      .select('id, device_mac, location')
      .not('location', 'is', null) as any;
      
    if (telemetry) {
      const tPoints: MapPoint[] = [];
      telemetry.forEach((t: any) => {
        let coords = parseJsonLocation(t.location) || parseStringLocation(t.location);
        if (!coords && t.location?.coordinates) {
            coords = parseJsonLocation(t.location.coordinates);
        }
        if (coords) {
          tPoints.push({
            id: `tel_${t.id}`,
            lat: coords.lat,
            lng: coords.lng,
            label: `Ping: ${t.device_mac}`,
            type: 'telemetry'
          });
        }
      });
      setTelemetryPoints(tPoints);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const onFilterChanged = useCallback(() => {
    if (gridRef.current) {
      const filtered: DeviceData[] = [];
      gridRef.current.api.forEachNodeAfterFilter((node) => {
        if (node.data) filtered.push(node.data);
      });
      setFilteredDevices(filtered);
    }
  }, []);

  const [colDefs, setColDefs] = useState<ColDef<any>[]>([]);

  const defaultColDef = useMemo(() => ({
    sortable: true,
    filter: true,
    floatingFilter: true, // Show filter inputs under headers
    resizable: true,
    minWidth: 150, // Prevents squishing, enables horizontal scroll
  }), []);

  const activeCount = filteredDevices.length;
  
  const activeMapPoints: MapPoint[] = [];
  if (layers.device) activeMapPoints.push(...devicePoints);
  if (layers.skate) activeMapPoints.push(...skatePoints);
  if (layers.crew) activeMapPoints.push(...crewPoints);
  if (layers.telemetry) activeMapPoints.push(...telemetryPoints);



  
  const GRID_SIZE = 30; // 30x30 grid
  const clusters = useMemo(() => {
    const bins = new Map<string, MapPoint[]>();
    activeMapPoints.forEach(pt => {
      const pos = latLngToXY(pt.lat, pt.lng);
      const px = parseFloat(pos.left); 
      const py = parseFloat(pos.top);  
      
      const gridX = Math.floor((px / 100) * GRID_SIZE);
      const gridY = Math.floor((py / 100) * GRID_SIZE);
      const key = `${gridX}_${gridY}`;
      
      if (!bins.has(key)) bins.set(key, []);
      bins.get(key)!.push(pt);
    });

    const result: ClusterNode[] = [];
    bins.forEach((points, key) => {
      if (points.length === 1) {
        result.push({
          id: points[0].id,
          lat: points[0].lat,
          lng: points[0].lng,
          count: 1,
          type: points[0].type,
          types: [points[0].type]
        });
      } else {
        const avgLat = points.reduce((s, p) => s + p.lat, 0) / points.length;
        const avgLng = points.reduce((s, p) => s + p.lng, 0) / points.length;
        const types = points.map(p => p.type);
        const typeCounts = types.reduce((acc, t) => ({...acc, [t]: (acc[t] || 0) + 1}), {} as Record<string, number>);
        const uniqueTypes = Object.keys(typeCounts);
        
        result.push({
          id: `cluster_${key}`,
          lat: avgLat,
          lng: avgLng,
          count: points.length,
          type: uniqueTypes.length === 1 ? uniqueTypes[0] : 'mixed',
          types: uniqueTypes
        });
      }
    });
    return result;
  }, [activeMapPoints, GRID_SIZE]);

  const rowSelectionConfig = useMemo(() => ({ mode: 'multiRow' as const }), []);
  const paginationPageSizeSelector = useMemo(() => [10, 20, 50, 100], []);

  return (
    <div className="h-full flex flex-col gap-6 overflow-y-auto pr-2 pb-10">

      {/* Top Header via SectionHdr */}
      <SectionHdr 
        label="Dynamic Fleet Ops" 
        color="#22d3ee" 
        right={
          zoom.scale > 1 && (
            <button 
              onClick={handleResetZoom}
              className="bg-slate-800 text-slate-300 border border-slate-600 px-2 py-1 rounded text-xs hover:bg-slate-700 transition-colors z-50 mr-4"
            >
              Reset Zoom
            </button>
          )
        } 
      />
      
      {/* Map Section */}
      {!isMapCollapsed && (
        <div className="glass-panel rounded-xl relative overflow-hidden border border-slate-800/50 bg-[#0f172a]/80 shadow-[0_8px_32px_rgba(0,0,0,0.4)] flex flex-col shrink-0 min-h-[400px]">
          
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
              onClick={() => setLayers(l => ({...l, skate: !l.skate}))}
              style={{
                backgroundColor: layers.skate ? '#4ade80' : 'transparent',
                borderColor: '#4ade80', borderWidth: '1px', borderStyle: 'solid',
                color: layers.skate ? '#0f172a' : '#4ade80',
                opacity: layers.skate ? 1 : 0.6, cursor: 'pointer',
                padding: '6px 16px', borderRadius: '9999px', fontSize: '13px', fontWeight: 'bold', transition: 'all 0.2s',
                display: 'flex', alignItems: 'center', justifyContent: 'center', whiteSpace: 'nowrap'
              }}
            >
              Skate ({skatePoints.length})
            </div>
            <div 
              onClick={() => setLayers(l => ({...l, crew: !l.crew}))}
              style={{
                backgroundColor: layers.crew ? '#c084fc' : 'transparent',
                borderColor: '#c084fc', borderWidth: '1px', borderStyle: 'solid',
                color: layers.crew ? '#0f172a' : '#c084fc',
                opacity: layers.crew ? 1 : 0.6, cursor: 'pointer',
                padding: '6px 16px', borderRadius: '9999px', fontSize: '13px', fontWeight: 'bold', transition: 'all 0.2s',
                display: 'flex', alignItems: 'center', justifyContent: 'center', whiteSpace: 'nowrap'
              }}
            >
              Crew ({crewPoints.length})
            </div>
            <div 
              onClick={() => setLayers(l => ({...l, device: !l.device}))}
              style={{
                backgroundColor: layers.device ? '#22d3ee' : 'transparent',
                borderColor: '#22d3ee', borderWidth: '1px', borderStyle: 'solid',
                color: layers.device ? '#0f172a' : '#22d3ee',
                opacity: layers.device ? 1 : 0.6, cursor: 'pointer',
                padding: '6px 16px', borderRadius: '9999px', fontSize: '13px', fontWeight: 'bold', transition: 'all 0.2s',
                display: 'flex', alignItems: 'center', justifyContent: 'center', whiteSpace: 'nowrap'
              }}
            >
              Devices ({devicePoints.length})
            </div>
            <div 
              onClick={() => setLayers(l => ({...l, telemetry: !l.telemetry}))}
              style={{
                backgroundColor: layers.telemetry ? '#f43f5e' : 'transparent',
                borderColor: '#f43f5e', borderWidth: '1px', borderStyle: 'solid',
                color: layers.telemetry ? '#0f172a' : '#f43f5e',
                opacity: layers.telemetry ? 1 : 0.6, cursor: 'pointer',
                padding: '6px 16px', borderRadius: '9999px', fontSize: '13px', fontWeight: 'bold', transition: 'all 0.2s',
                display: 'flex', alignItems: 'center', justifyContent: 'center', whiteSpace: 'nowrap'
              }}
            >
              Telemetry ({telemetryPoints.length})
            </div>
          </div>

          {/* Map Pan/Zoom Container */}
          <div style={{ position: 'relative', width: '100%', flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '1rem', overflow: 'hidden' }}>
            <div 
              style={{ 
                position: 'relative', 
                width: '100%', 
                maxWidth: '56rem', 
                opacity: 0.9, 
                aspectRatio: '959/593',
                transform: `scale(${zoom.scale})`,
                transformOrigin: `${zoom.x}% ${zoom.y}%`,
                transition: 'transform 0.5s cubic-bezier(0.25, 1, 0.5, 1)'
              }}
            >
              <USAMap 
                width="100%" 
                height="100%" 
                defaultFill="#1e293b" 
                customize={{}} 
                onClick={() => {}} 
              />

              {/* Overlay Clusters */}
              {clusters.map((pt, i) => {
                const pos = latLngToXY(pt.lat, pt.lng);
                
                // Colors
                let bgValue = '#22d3ee';
                let shadowColor = 'rgba(34,211,238,0.9)';
                if (pt.type === 'mixed') {
                  const colorMap: Record<string, string> = { skate: '#4ade80', crew: '#c084fc', device: '#22d3ee', telemetry: '#f43f5e' };
                  const gradientColors = pt.types.map(t => colorMap[t] || '#fbbf24').join(', ');
                  bgValue = `linear-gradient(135deg, ${gradientColors})`;
                  shadowColor = 'rgba(255,255,255,0.4)';
                } else {
                  if (pt.type === 'skate') { bgValue = '#4ade80'; shadowColor = 'rgba(74,222,128,0.9)'; }
                  else if (pt.type === 'crew') { bgValue = '#c084fc'; shadowColor = 'rgba(192,132,252,0.9)'; }
                  else if (pt.type === 'telemetry') { bgValue = '#f43f5e'; shadowColor = 'rgba(244,63,94,0.9)'; }
                  else if (pt.type === 'device') { bgValue = '#22d3ee'; shadowColor = 'rgba(34,211,238,0.9)'; }
                }

                // Scale sizes and blur based on density to mimic heatmap
                const baseSize = 16;
                const sizeBonus = Math.min(24, pt.count * 2);
                const finalSize = baseSize + sizeBonus;
                const opacity = pt.count > 1 ? 0.8 : 1;

                return (
                  <div 
                    key={`${pt.id}_${i}`}
                    style={{ 
                      position: 'absolute',
                      transform: 'translate(-50%, -50%)',
                      zIndex: pt.type === 'telemetry' ? 40 : 50,
                      ...pos 
                    }}
                    title={`${pt.type} - ${pt.count} points`}
                    onClick={(e) => {
                      e.stopPropagation();
                      handleZoom(pt.lat, pt.lng);
                    }}
                  >
                    {/* Ping Node / Heatmap Blended */}
                    <div 
                      style={{ 
                        width: `${finalSize}px`, 
                        height: `${finalSize}px`, 
                        borderRadius: '50%', 
                        backgroundColor: pt.type !== 'mixed' ? bgValue : undefined,
                        backgroundImage: pt.type === 'mixed' ? bgValue : undefined,
                        border: '1px solid rgba(15,23,42,0.5)',
                        cursor: 'pointer',
                        opacity: opacity,
                        boxShadow: `0 0 ${12 + sizeBonus}px ${shadowColor}`,
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        transition: 'all 0.3s',
                        color: '#0f172a',
                        fontWeight: 'bold',
                        fontSize: pt.count > 1 ? '10px' : '0px'
                      }}
                      onMouseOver={(e) => e.currentTarget.style.transform = 'scale(1.2)'}
                      onMouseOut={(e) => e.currentTarget.style.transform = 'scale(1)'}
                    >
                      {pt.count > 1 ? pt.count : ''}
                    </div>
                  </div>
                );
              })}
              
              {activeMapPoints.length === 0 && (
                <div className="absolute inset-0 flex items-center justify-center bg-slate-900/50 backdrop-blur-sm z-30 rounded-xl pointer-events-none">
                  <div className="text-slate-400 flex flex-col items-center gap-2">
                    <span className="text-3xl">📡</span>
                    <span className="font-medium text-sm">No Active Coordinates in Selected Layers</span>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      )}

      {/* AG-Grid Databank Section */}
      <div className="flex flex-col flex-1 shrink-0 min-h-[500px]">
        <h3 className="text-xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-white to-slate-400 mb-4 tracking-wide">Fleet Databank</h3>
        <div className="glass-panel p-1 rounded-xl border border-cyan-900/40 shadow-2xl bg-[#0f172a]/60 backdrop-blur-xl">
          <div className="rounded-lg overflow-hidden" style={{ height: 500, width: '100%' }}>
            <AgGridReact
              ref={gridRef}
              theme={myTheme}
              rowData={devices}
              columnDefs={colDefs}
              defaultColDef={defaultColDef}
              onFilterChanged={onFilterChanged}
              animateRows={true}
              rowSelection={rowSelectionConfig}
              suppressHorizontalScroll={false}
              pagination={true}
              paginationPageSize={10}
              paginationPageSizeSelector={paginationPageSizeSelector}
            />
          </div>
        </div>
      </div>

    </div>
  );
}
