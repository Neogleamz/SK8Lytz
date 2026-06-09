import React, { useEffect, useRef, useState } from 'react';
import mapboxgl from 'mapbox-gl';
import 'mapbox-gl/dist/mapbox-gl.css';
import { supabase } from '../../services/supabase';

// Use environment variable for token
mapboxgl.accessToken = import.meta.env.VITE_MAPBOX_TOKEN || 'pk.eyJ1IjoibW9ja3Rva2VuIiwiYSI6ImNtb2NrdG9rZW4ifQ.mocktoken';

interface CrewSession {
  session_id: string;
  is_active: boolean;
  location_coords: { lat: number; lng: number } | null;
  top_speed_mph: number;
  total_distance_miles: number;
}

export default function MapWidget() {
  const mapContainer = useRef<HTMLDivElement>(null);
  const map = useRef<mapboxgl.Map | null>(null);
  const [activeSessions, setActiveSessions] = useState<CrewSession[]>([]);
  const markersRef = useRef<{ [key: string]: mapboxgl.Marker }>({});

  useEffect(() => {
    if (map.current) return; // initialize map only once

    if (mapContainer.current) {
      map.current = new mapboxgl.Map({
        container: mapContainer.current,
        style: 'mapbox://styles/mapbox/dark-v11',
        center: [-98.5795, 39.8283], // Center of US
        zoom: 3
      });
    }

    // Fetch initial data
    fetchActiveSessions();

    // Subscribe to realtime changes
    const subscription = supabase
      .channel('public:crew_sessions')
      .on('postgres_changes', { event: '*', schema: 'public', table: 'crew_sessions' }, payload => {
        fetchActiveSessions(); // naive reload, optimize later
      })
      .subscribe();

    return () => {
      subscription.unsubscribe();
    };
  }, []);

  const fetchActiveSessions = async () => {
    const { data, error } = await supabase
      .from('crew_sessions')
      .select('session_id, is_active, location_coords, top_speed_mph, total_distance_miles')
      .eq('is_active', true);
      
    if (data && !error) {
      setActiveSessions(data as CrewSession[]);
    }
  };

  useEffect(() => {
    if (!map.current) return;

    // Remove old markers
    Object.values(markersRef.current).forEach(marker => marker.remove());
    markersRef.current = {};

    // Add new markers
    activeSessions.forEach(session => {
      if (session.location_coords) {
        const el = document.createElement('div');
        el.className = 'w-4 h-4 bg-cyan-400 rounded-full border-2 border-slate-900 shadow-[0_0_10px_rgba(34,211,238,0.8)]';

        const popup = new mapboxgl.Popup({ offset: 25 }).setHTML(
          `<div class="text-slate-900 font-sans p-1">
            <strong>Active Session</strong><br/>
            Speed: ${session.top_speed_mph} mph<br/>
            Distance: ${session.total_distance_miles} mi
          </div>`
        );

        const marker = new mapboxgl.Marker(el)
          .setLngLat([session.location_coords.lng, session.location_coords.lat])
          .setPopup(popup)
          .addTo(map.current!);
          
        markersRef.current[session.session_id] = marker;
      }
    });
  }, [activeSessions]);

  return (
    <div className="h-full flex flex-col gap-4">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-white">Global Fleet Ops Map</h2>
        <div className="glass-panel px-4 py-2 rounded text-cyan-400 font-medium">
          {activeSessions.length} Active Sessions
        </div>
      </div>
      <div className="glass-panel flex-1 rounded-xl overflow-hidden relative border border-slate-800">
        <div ref={mapContainer} className="absolute inset-0" />
      </div>
    </div>
  );
}
