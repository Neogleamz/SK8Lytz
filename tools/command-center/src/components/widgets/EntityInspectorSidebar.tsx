import React, { useEffect, useState } from 'react';
import { supabase } from '../../services/supabase';

interface EntityInspectorProps {
  userId: string | null;
  onClose: () => void;
}

export const EntityInspectorSidebar: React.FC<EntityInspectorProps> = ({ userId, onClose }) => {
  const [data, setData] = useState<{
    user: any;
    devices: any[];
    sessions: any[];
    crew: any;
  } | null>(null);

  useEffect(() => {
    if (!userId) {
      setData(null);
      return;
    }

    const fetchData = async () => {
      // 1. Fetch user basics
      const { data: userData } = await supabase
        .from('user_profiles')
        .select('*')
        .eq('user_id', userId)
        .single();

      // 2. Fetch devices
      const { data: devicesData } = await supabase
        .from('registered_devices')
        .select('*')
        .eq('user_id', userId);

      // 3. Fetch recent skate sessions
      const { data: sessionData } = await supabase
        .from('skate_sessions')
        .select('*')
        .eq('user_id', userId)
        .order('session_date', { ascending: false })
        .limit(5);

      // 4. Fetch crew memberships
      const { data: crewMemberData } = await supabase
        .from('crew_memberships')
        .select('crew_id, role, crews(*)')
        .eq('user_id', userId)
        .limit(1)
        .single();

      setData({
        user: userData || { display_name: 'Unknown User' },
        devices: devicesData || [],
        sessions: sessionData || [],
        crew: crewMemberData ? crewMemberData.crews : null
      });
    };

    fetchData();
  }, [userId]);

  if (!userId) return null;

  return (
    <div style={{
      position: 'absolute',
      right: 0,
      top: 0,
      bottom: 0,
      width: '320px',
      backgroundColor: 'rgba(15, 23, 42, 0.95)',
      backdropFilter: 'blur(12px)',
      borderLeft: '1px solid rgba(51, 65, 85, 0.5)',
      boxShadow: '-8px 0 32px rgba(0,0,0,0.5)',
      zIndex: 100,
      padding: '20px',
      display: 'flex',
      flexDirection: 'column',
      color: '#e2e8f0',
      transition: 'transform 0.3s ease',
      transform: userId ? 'translateX(0)' : 'translateX(100%)',
      overflowY: 'auto'
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h3 style={{ margin: 0, fontSize: '1.2rem', color: '#22d3ee' }}>Entity Inspector</h3>
        <button 
          onClick={onClose}
          style={{ background: 'transparent', border: 'none', color: '#94a3b8', cursor: 'pointer', fontSize: '1.2rem' }}
        >
          ×
        </button>
      </div>

      {!data ? (
        <div style={{ textAlign: 'center', color: '#64748b', marginTop: '40px' }}>Loading Relational Graph...</div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          
          {/* User Section */}
          <div style={{ background: 'rgba(255,255,255,0.05)', padding: '12px', borderRadius: '8px' }}>
            <div style={{ fontSize: '0.8rem', textTransform: 'uppercase', color: '#94a3b8', marginBottom: '4px' }}>User Profile</div>
            <div style={{ fontWeight: 'bold', fontSize: '1.1rem' }}>{data.user.display_name}</div>
            <div style={{ fontSize: '0.8rem', color: '#64748b', fontFamily: 'monospace' }}>{userId}</div>
          </div>

          {/* Crew Section */}
          {data.crew && (
            <div style={{ background: 'rgba(192,132,252,0.1)', border: '1px solid rgba(192,132,252,0.3)', padding: '12px', borderRadius: '8px' }}>
              <div style={{ fontSize: '0.8rem', textTransform: 'uppercase', color: '#c084fc', marginBottom: '4px' }}>Active Crew</div>
              <div style={{ fontWeight: 'bold', display: 'flex', alignItems: 'center', gap: '8px' }}>
                <span style={{ fontSize: '1.2rem' }}>{data.crew.avatar_icon || '🛹'}</span>
                {data.crew.name}
              </div>
            </div>
          )}

          {/* Hardware Fleet */}
          <div>
            <div style={{ fontSize: '0.8rem', textTransform: 'uppercase', color: '#22d3ee', marginBottom: '8px', borderBottom: '1px solid rgba(34,211,238,0.2)', paddingBottom: '4px' }}>
              Hardware Fleet ({data.devices.length})
            </div>
            {data.devices.map(d => (
              <div key={d.id} style={{ background: 'rgba(255,255,255,0.02)', padding: '8px', borderRadius: '6px', marginBottom: '8px', fontSize: '0.85rem' }}>
                <div style={{ fontWeight: 'bold' }}>{d.custom_name || d.device_name}</div>
                <div style={{ color: '#64748b', display: 'flex', justifyContent: 'space-between' }}>
                  <span>{d.product_type}</span>
                  <span>{d.led_points} LEDs</span>
                </div>
              </div>
            ))}
          </div>

          {/* Skate Sessions */}
          <div>
            <div style={{ fontSize: '0.8rem', textTransform: 'uppercase', color: '#4ade80', marginBottom: '8px', borderBottom: '1px solid rgba(74,222,128,0.2)', paddingBottom: '4px' }}>
              Recent Sessions ({data.sessions.length})
            </div>
            {data.sessions.map(s => (
              <div key={s.id} style={{ background: 'rgba(255,255,255,0.02)', padding: '8px', borderRadius: '6px', marginBottom: '8px', fontSize: '0.85rem' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '4px' }}>
                  <span style={{ fontWeight: 'bold' }}>{s.distance_miles.toFixed(1)} mi</span>
                  <span style={{ color: '#fbbf24' }}>{s.peak_speed_mph.toFixed(1)} mph</span>
                </div>
                <div style={{ color: '#64748b', fontSize: '0.75rem' }}>
                  {new Date(s.session_date).toLocaleDateString()} • {Math.floor(s.duration_sec / 60)} mins
                </div>
              </div>
            ))}
          </div>

        </div>
      )}
    </div>
  );
};
