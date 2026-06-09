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
  rowBorderColor: 'rgba(51, 65, 85, 0.3)',
  rowHoverColor: 'rgba(34, 211, 238, 0.1)', // cyan-400 transparent
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

// Very naive projection to map lat/lng to percentage across the SVG bounding box
function latLngToXY(lat: number, lng: number) {
  const xPercent = ((lng - -125) / (-66 - -125)) * 100;
  const yPercent = ((50 - lat) / (50 - 24)) * 100;
  return { 
    left: `${Math.max(0, Math.min(100, xPercent))}%`, 
    top: `${Math.max(0, Math.min(100, yPercent))}%` 
  };
}

export default function MapWidget() {
  const [devices, setDevices] = useState<DeviceData[]>([]);
  const [filteredDevices, setFilteredDevices] = useState<DeviceData[]>([]);
  const gridRef = useRef<AgGridReact>(null);

  const fetchDevices = async () => {
    // 1. Fetch devices (ALL fields)
    const { data: rawDevices, error: devError } = await supabase
      .from('registered_devices')
      .select('*');

    if (devError) {
      console.error("MapWidget devError:", devError);
      setFilteredDevices([{ custom_name: `Error: ${devError.message}`, id: 'error', user_id: 'error' } as any]);
      return;
    }
    if (!rawDevices) return;

    // Satisfy TypeScript that rawDevices is definitely not null here
    const validDevices = rawDevices as any[];

    // 2. Identify devices needing fallback coordinates
    const usersNeedingFallback = Array.from(new Set(
      validDevices.map(d => d.user_id)
    ));

    // 3. Fetch latest active or recent sessions for those users
    const fallbackMap: Record<string, { lat: number, lng: number }> = {};
    if (usersNeedingFallback.length > 0) {
      const { data: sessions } = await supabase
        .from('crew_sessions')
        .select('leader_user_id, location_coords, updated_at')
        .not('location_coords', 'is', null)
        .in('leader_user_id', usersNeedingFallback)
        .order('updated_at', { ascending: false }) as any;

      if (sessions) {
        sessions.forEach((sess: any) => {
          if (!fallbackMap[sess.leader_user_id] && sess.location_coords) {
            fallbackMap[sess.leader_user_id] = {
              lat: sess.location_coords.lat,
              lng: sess.location_coords.lng
            };
          }
        });
      }
    }

    // 3.5 Fallback to discovered_devices_telemetry using MAC Address
    const macsNeedingFallback = validDevices
      .filter(d => !fallbackMap[d.user_id] && d.device_mac)
      .map(d => d.device_mac) as string[];
    if (macsNeedingFallback.length > 0) {
      const { data: telemetry } = await supabase
        .from('discovered_devices_telemetry')
        .select('device_mac, location')
        .not('location', 'is', null)
        .in('device_mac', macsNeedingFallback) as any;

      if (telemetry) {
        telemetry.forEach((t: any) => {
          if (t.location) {
            let lat = null;
            let lng = null;
            if (t.location.lat !== undefined) {
              lat = t.location.lat;
              lng = t.location.lng !== undefined ? t.location.lng : t.location.lon;
            } else if (t.location.coordinates && Array.isArray(t.location.coordinates)) {
              lng = t.location.coordinates[0];
              lat = t.location.coordinates[1];
            } else if (typeof t.location === 'string' && t.location.includes('POINT')) {
               const match = t.location.match(/POINT\(([^ ]+)\s+([^ ]+)\)/);
               if (match) {
                 lng = parseFloat(match[1]);
                 lat = parseFloat(match[2]);
               }
            }

            if (lat !== null && lng !== null && !isNaN(lat) && !isNaN(lng)) {
              const owner = validDevices.find(d => d.device_mac === t.device_mac);
              if (owner) {
                fallbackMap[owner.user_id] = { lat, lng };
              }
            }
          }
        });
      }
    }

    // 4. Merge coordinates
    const mergedDevices = validDevices.map(d => {
      let lat = null;
      let lng = null;
      if (fallbackMap[d.user_id]) {
        lat = fallbackMap[d.user_id].lat;
        lng = fallbackMap[d.user_id].lng;
      }
      return { ...d, last_lat: lat, last_lng: lng };
    });

    setDevices(mergedDevices as DeviceData[]);
    setFilteredDevices(mergedDevices as DeviceData[]);

    // Dynamically set ALL columns if we have data
    if (mergedDevices.length > 0) {
      const keys = Object.keys(mergedDevices[0]).filter(k => k !== 'last_lat' && k !== 'last_lng');
      const dynamicCols: ColDef<any>[] = keys.map(k => ({
        field: k,
        headerName: k.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase()),
        filter: true,
        sortable: true
      }));
      setColDefs(dynamicCols);
    }
  };

  useEffect(() => {
    fetchDevices();
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
  const mapDevices = filteredDevices.filter(d => d.last_lat !== null && d.last_lng !== null);

  return (
    <div className="h-full flex flex-col gap-6 overflow-y-auto pr-2 pb-10">

      {/* Top Header */}
      <div className="flex justify-between items-center shrink-0">
        <h2 className="text-2xl font-bold text-white">Dynamic Fleet Ops</h2>
        <div className="glass-panel px-4 py-2 rounded text-cyan-400 font-medium border border-cyan-500/30 bg-cyan-500/10 shadow-[0_0_15px_rgba(34,211,238,0.15)]">
          {activeCount} Devices Found
        </div>
      </div>
      
      {/* Map Section */}
      <div className="glass-panel rounded-xl relative overflow-hidden border border-slate-800/50 bg-[#0f172a]/80 shadow-[0_8px_32px_rgba(0,0,0,0.4)] flex items-center justify-center p-4 shrink-0 min-h-[400px]">
        <div className="relative w-full max-w-4xl opacity-90" style={{ aspectRatio: '959/593' }}>
          
          <USAMap 
            width="100%" 
            height="100%" 
            defaultFill="#1e293b" 
            customize={{}} 
            onClick={() => {}} 
          />

          {/* Overlay Pings */}
          {mapDevices.map(dev => {
            const pos = latLngToXY(dev.last_lat!, dev.last_lng!);
            return (
              <div 
                key={dev.id}
                className="absolute transform -translate-x-1/2 -translate-y-1/2 group z-10"
                style={pos}
              >
                {/* Ping Dot */}
                <div className="w-3 h-3 rounded-full bg-cyan-400 border border-[#0f172a] shadow-[0_0_12px_rgba(34,211,238,0.9)] cursor-pointer hover:scale-150 transition-transform duration-200" />
              </div>
            );
          })}
        </div>
      </div>

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
              rowSelection="multiple"
              suppressHorizontalScroll={false}
              pagination={true}
              paginationPageSize={10}
            />
          </div>
        </div>
      </div>

    </div>
  );
}
