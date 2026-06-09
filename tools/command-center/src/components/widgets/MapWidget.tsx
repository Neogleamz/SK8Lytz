import { useEffect, useState } from 'react';
import { supabase } from '../../services/supabase';
import USAMap from './USMap';

interface CrewSession {
  id: string;
  is_active: boolean;
  location_coords: { lat: number; lng: number } | null;
  top_speed_mph: string;
  total_distance_miles: string;
  location_label?: string;
}

// Very naive projection to map lat/lng to percentage across the SVG bounding box
function latLngToXY(lat: number, lng: number) {
  // Approximate US Bounding Box: Lng -125 to -66, Lat 24 to 50
  const xPercent = ((lng - -125) / (-66 - -125)) * 100;
  const yPercent = ((50 - lat) / (50 - 24)) * 100;
  
  return { 
    left: `${Math.max(0, Math.min(100, xPercent))}%`, 
    top: `${Math.max(0, Math.min(100, yPercent))}%` 
  };
}

export default function MapWidget() {
  const [sessions, setSessions] = useState<CrewSession[]>([]);

  useEffect(() => {
    fetchSessions();

    const subscription = supabase
      .channel('public:crew_sessions')
      .on('postgres_changes', { event: '*', schema: 'public', table: 'crew_sessions' }, () => {
        fetchSessions();
      })
      .subscribe();

    return () => {
      subscription.unsubscribe();
    };
  }, []);

  const fetchSessions = async () => {
    // We'll fetch all sessions for visual purposes, but highlight active ones
    const { data, error } = await supabase
      .from('crew_sessions')
      .select('id, is_active, location_coords, top_speed_mph, total_distance_miles, location_label')
      .not('location_coords', 'is', null);
      
    if (data && !error) {
      setSessions(data as unknown as CrewSession[]);
    }
  };

  const activeCount = sessions.filter(s => s.is_active).length;

  return (
    <div className="h-full flex flex-col gap-4">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-white">Global Fleet Ops Map</h2>
        <div className="glass-panel px-4 py-2 rounded text-cyan-400 font-medium border border-cyan-500/30 bg-cyan-500/10">
          {activeCount} Active Sessions
        </div>
      </div>
      
      <div className="glass-panel flex-1 rounded-xl relative overflow-hidden border border-slate-800 bg-[#0f172a] flex items-center justify-center p-4">
        <div className="relative w-full max-w-4xl opacity-80" style={{ aspectRatio: '959/593' }}>
          
          <USAMap 
            width="100%" 
            height="100%" 
            defaultFill="#1e293b" 
            customize={{}} 
            onClick={() => {}} 
          />

          {/* Overlay Pings */}
          {sessions.map(session => {
            if (!session.location_coords) return null;
            const pos = latLngToXY(session.location_coords.lat, session.location_coords.lng);
            
            return (
              <div 
                key={session.id}
                className="absolute transform -translate-x-1/2 -translate-y-1/2 group z-10"
                style={pos}
              >
                {/* Ping Dot */}
                <div className={`w-3 h-3 rounded-full ${session.is_active ? 'bg-cyan-400 animate-pulse' : 'bg-slate-500'} border border-[#0f172a] shadow-[0_0_8px_rgba(34,211,238,0.8)]`} />
                
                {/* Tooltip */}
                <div className="absolute bottom-full left-1/2 -translate-x-1/2 mb-2 w-48 bg-slate-900 border border-slate-700 rounded-lg p-2 opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none shadow-xl z-50">
                  <div className="text-xs font-bold text-white mb-1">
                    {session.location_label || 'Unknown Location'}
                  </div>
                  <div className="text-[10px] text-slate-400">
                    Status: <span className={session.is_active ? 'text-cyan-400' : 'text-slate-500'}>{session.is_active ? 'ACTIVE' : 'OFFLINE'}</span><br/>
                    Speed: {session.top_speed_mph} mph<br/>
                    Distance: {session.total_distance_miles} mi
                  </div>
                </div>
              </div>
            );
          })}
        </div>
        
        {sessions.length === 0 && (
          <div className="absolute inset-0 flex items-center justify-center bg-slate-900/50 backdrop-blur-sm z-20">
            <div className="text-slate-400 flex flex-col items-center gap-2">
              <span className="text-3xl">📡</span>
              <span className="font-medium">Awaiting Telemetry Pings...</span>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
