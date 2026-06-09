import React, { useEffect, useState, useMemo, useRef, useCallback } from 'react';
import { supabase } from '../../services/supabase';
import USAMap from './USMap';
import { AgGridReact } from 'ag-grid-react';
import { ModuleRegistry, ClientSideRowModelModule } from 'ag-grid-community';
import type { ColDef } from 'ag-grid-community';

// AG-Grid > 31 requires manual module registration
ModuleRegistry.registerModules([ClientSideRowModelModule]);

import 'ag-grid-community/styles/ag-grid.css';
import 'ag-grid-community/styles/ag-theme-alpine.css';

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
    // 1. Fetch devices
    const { data: rawDevices, error: devError } = await supabase
      .from('registered_devices')
      .select('id, user_id, custom_name, product_type, firmware_ver, is_pending_sync, ic_type, last_lat, last_lng, device_mac, registered_at, points');

    if (devError || !rawDevices) return;

    // 2. Identify devices needing fallback coordinates
    const usersNeedingFallback = Array.from(new Set(
      rawDevices.filter(d => d.last_lat === null || d.last_lng === null).map(d => d.user_id)
    ));

    // 3. Fetch latest active or recent sessions for those users (Fallback to Option A)
    const fallbackMap: Record<string, { lat: number, lng: number }> = {};
    if (usersNeedingFallback.length > 0) {
      const { data: sessions } = await supabase
        .from('crew_sessions')
        .select('user_id, location_coords, updated_at')
        .not('location_coords', 'is', null)
        .in('user_id', usersNeedingFallback)
        .order('updated_at', { ascending: false }) as any;

      if (sessions) {
        sessions.forEach((sess: any) => {
          // Since it's ordered by updated_at descending, we only grab the first (latest)
          if (!fallbackMap[sess.user_id] && sess.location_coords) {
            // @ts-ignore
            fallbackMap[sess.user_id] = {
              lat: sess.location_coords.lat,
              lng: sess.location_coords.lng
            };
          }
        });
      }
    }

    // 4. Merge coordinates (Option B falls back to Option A)
    const mergedDevices = rawDevices.map(d => {
      let lat = d.last_lat;
      let lng = d.last_lng;
      if (lat === null || lng === null) {
        if (fallbackMap[d.user_id]) {
          lat = fallbackMap[d.user_id].lat;
          lng = fallbackMap[d.user_id].lng;
        }
      }
      return { ...d, last_lat: lat, last_lng: lng };
    });

    setDevices(mergedDevices as DeviceData[]);
    setFilteredDevices(mergedDevices as DeviceData[]);
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

  const [colDefs] = useState<ColDef<DeviceData>[]>([
    { field: 'custom_name', headerName: 'Name', filter: 'agTextColumnFilter', flex: 1 },
    { field: 'device_mac', headerName: 'MAC Address', filter: 'agTextColumnFilter' },
    { field: 'product_type', headerName: 'Product', filter: 'agTextColumnFilter' },
    { field: 'firmware_ver', headerName: 'FW Ver', filter: 'agNumberColumnFilter' },
    { field: 'ic_type', headerName: 'IC Type', filter: 'agTextColumnFilter' },
    { field: 'points', headerName: 'LEDs', filter: 'agNumberColumnFilter' },
    { field: 'is_pending_sync', headerName: 'Syncing', filter: 'agSetColumnFilter' },
    { 
      field: 'registered_at', 
      headerName: 'Registered', 
      filter: 'agDateColumnFilter',
      valueFormatter: (p) => p.value ? new Date(p.value).toLocaleDateString() : '' 
    }
  ]);

  const defaultColDef = useMemo(() => ({
    sortable: true,
    filter: true,
    resizable: true,
  }), []);

  const activeCount = filteredDevices.length;
  const mapDevices = filteredDevices.filter(d => d.last_lat !== null && d.last_lng !== null);

  return (
    <div className="h-full flex flex-col gap-6 overflow-y-auto pr-2 pb-10">
      
      {/* Top Header */}
      <div className="flex justify-between items-center shrink-0">
        <h2 className="text-2xl font-bold text-white">Dynamic Fleet Ops</h2>
        <div className="glass-panel px-4 py-2 rounded text-cyan-400 font-medium border border-cyan-500/30 bg-cyan-500/10">
          {activeCount} Devices Found
        </div>
      </div>
      
      {/* Map Section */}
      <div className="glass-panel rounded-xl relative overflow-hidden border border-slate-800 bg-[#0f172a] flex items-center justify-center p-4 shrink-0 min-h-[400px]">
        <div className="relative w-full max-w-4xl opacity-80" style={{ aspectRatio: '959/593' }}>
          
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
                <div className="w-3 h-3 rounded-full bg-cyan-400 border border-[#0f172a] shadow-[0_0_8px_rgba(34,211,238,0.8)] cursor-pointer" />
                
                {/* Tooltip */}
                <div className="absolute bottom-full left-1/2 -translate-x-1/2 mb-2 w-48 bg-slate-900 border border-slate-700 rounded-lg p-3 opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none shadow-xl z-50">
                  <div className="text-sm font-bold text-white mb-1">
                    {dev.custom_name}
                  </div>
                  <div className="text-xs text-slate-400 leading-relaxed">
                    Product: <span className="text-cyan-400">{dev.product_type}</span><br/>
                    FW: {dev.firmware_ver}<br/>
                    LEDs: {dev.points}<br/>
                    MAC: {dev.device_mac}
                  </div>
                </div>
              </div>
            );
          })}
        </div>
        
        {mapDevices.length === 0 && (
          <div className="absolute inset-0 flex items-center justify-center bg-slate-900/50 backdrop-blur-sm z-20">
            <div className="text-slate-400 flex flex-col items-center gap-2">
              <span className="text-3xl">📡</span>
              <span className="font-medium">No Coordinates Found for Active Filter</span>
            </div>
          </div>
        )}
      </div>

      {/* AG-Grid Databank Section */}
      <div className="flex flex-col flex-1 shrink-0 min-h-[500px]">
        <h3 className="text-xl font-semibold text-white mb-4">Fleet Databank</h3>
        <div className="ag-theme-alpine-dark flex-1 w-full rounded-xl overflow-hidden border border-slate-800" style={{ '--ag-background-color': '#1e293b', '--ag-header-background-color': '#0f172a', '--ag-border-color': '#334155' } as any}>
          <AgGridReact
            ref={gridRef}
            rowData={devices}
            columnDefs={colDefs}
            defaultColDef={defaultColDef}
            onFilterChanged={onFilterChanged}
            animateRows={true}
            rowSelection="multiple"
          />
        </div>
      </div>

    </div>
  );
}
