import React, { useEffect, useState } from 'react';
import { supabase } from '../../services/supabase';

interface EntityCardOverlayProps {
  activeEntity: { id: string; type: string };
  onClose: () => void;
}

export const EntityCardOverlay: React.FC<EntityCardOverlayProps> = ({ activeEntity, onClose }) => {
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<any>(null);
  const [activeTab, setActiveTab] = useState<'users' | 'devices' | 'sessions' | 'crews' | 'crew_sessions'>('users');

  // Reset tab when a new user is selected
  useEffect(() => {
    if (activeEntity?.type === 'users') {
      setActiveTab('users');
    }
  }, [activeEntity]);

  useEffect(() => {
    if (!activeEntity) return;
    
    const fetchRelationalData = async () => {
      setLoading(true);
      try {
        // Strip prefixes added by MapWidget (e.g. 'user_', 'skate_', 'crew_')
        const rawId = activeEntity.id.replace(/^(user_|skate_|crew_session_|crew_)/, '');

        if (activeEntity.type === 'users') {
          const { data: user } = await supabase.from('user_profiles').select('*').eq('user_id', rawId).single();
          const { data: devs } = await supabase.from('registered_devices').select('*').eq('user_id', rawId);
          const { data: sess } = await supabase.from('skate_sessions').select('*').eq('user_id', rawId).order('session_date', { ascending: false }).limit(10);
          const { data: crews } = await supabase.from('crew_memberships').select('*, crews(*)').eq('user_id', rawId);
          
          let cSess: any[] = [];
          if (crews && crews.length > 0) {
             const crewIds = crews.map(c => c.crew_id);
             const { data: cs } = await supabase.from('crew_sessions').select('*').in('crew_id', crewIds).order('created_at', { ascending: false }).limit(10);
             if (cs) cSess = cs;
          }

          setData({ user, devices: devs || [], sessions: sess || [], crews: crews || [], crewSessions: cSess });
        }
        else if (activeEntity.type === 'registeredDevices') {
          const { data: dev } = await supabase.from('registered_devices').select('*').eq('id', rawId).single();
          const { data: user } = await supabase.from('user_profiles').select('*').eq('user_id', (dev?.user_id || '') as string).single();
          setData({ device: dev, user });
        }
        else if (activeEntity.type === 'skateSessions') {
          const { data: sess } = await supabase.from('skate_sessions').select('*').eq('id', rawId).single();
          const { data: user } = await supabase.from('user_profiles').select('*').eq('user_id', (sess?.user_id || '') as string).single();
          setData({ session: sess, user });
        }
        else if (activeEntity.type === 'crews') {
          const { data: crew } = await supabase.from('crews').select('*').eq('id', rawId).single();
          const { data: members } = await supabase.from('crew_memberships').select('*, user_profiles(*)').eq('crew_id', rawId);
          setData({ crew, members: members || [] });
        }
        else if (activeEntity.type === 'crewSessions') {
          const { data: cSess } = await supabase.from('crew_sessions').select('*').eq('id', rawId).single();
          const { data: cMembers } = await supabase.from('crew_members').select('*').eq('session_id', rawId);
          setData({ crewSession: cSess, members: cMembers || [] });
        }
      } catch (err) {
        console.error('Failed to fetch entity data:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchRelationalData();
  }, [activeEntity]);

  if (!activeEntity) return null;

  const tabs = [
    { id: 'users', label: 'User' },
    { id: 'devices', label: 'Devices' },
    { id: 'sessions', label: 'Skate Sessions' },
    { id: 'crews', label: 'Crews' },
    { id: 'crew_sessions', label: 'Crew Sessions' }
  ] as const;

  const renderHeader = () => {
    if (activeEntity.type === 'users') {
      return (
        <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
          <div style={{ width: '56px', height: '56px', borderRadius: '50%', border: '2px solid #22d3ee', boxShadow: '0 0 15px rgba(34,211,238,0.4)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '24px', fontWeight: 'bold', backgroundColor: data?.user?.avatar_color || '#1e293b', color: '#fff' }}>
            {data?.user?.display_name?.charAt(0)?.toUpperCase() || '?'}
          </div>
          <div>
            <h2 style={{ margin: 0, fontSize: '24px', fontWeight: 'bold', color: '#fff', letterSpacing: '0.025em' }}>{data?.user?.display_name || 'Unknown User'}</h2>
            <p style={{ margin: '4px 0 0 0', fontSize: '12px', color: '#22d3ee', fontFamily: 'monospace' }}>ID: {activeEntity.id}</p>
          </div>
        </div>
      );
    }
    if (activeEntity.type === 'registeredDevices') {
      return (
        <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
          <div style={{ width: '56px', height: '56px', borderRadius: '12px', border: '2px solid #22d3ee', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '24px', backgroundColor: '#1e293b', color: '#22d3ee' }}>
            ⚡
          </div>
          <div>
            <h2 style={{ margin: 0, fontSize: '24px', fontWeight: 'bold', color: '#fff' }}>{data?.device?.custom_name || 'Hardware Device'}</h2>
            <p style={{ margin: '4px 0 0 0', fontSize: '12px', color: '#22d3ee', fontFamily: 'monospace' }}>MAC: {data?.device?.device_mac || activeEntity.id}</p>
          </div>
        </div>
      );
    }
    if (activeEntity.type === 'skateSessions') {
      return (
        <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
          <div style={{ width: '56px', height: '56px', borderRadius: '12px', border: '2px solid #facc15', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '24px', backgroundColor: '#1e293b', color: '#facc15' }}>
            🛹
          </div>
          <div>
            <h2 style={{ margin: 0, fontSize: '24px', fontWeight: 'bold', color: '#fff' }}>Skate Session</h2>
            <p style={{ margin: '4px 0 0 0', fontSize: '12px', color: '#facc15', fontFamily: 'monospace' }}>ID: {activeEntity.id}</p>
          </div>
        </div>
      );
    }
    if (activeEntity.type === 'crews') {
      return (
        <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
          <div style={{ width: '56px', height: '56px', borderRadius: '12px', border: '2px solid #c084fc', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '24px', backgroundColor: '#1e293b', color: '#c084fc' }}>
            👥
          </div>
          <div>
            <h2 style={{ margin: 0, fontSize: '24px', fontWeight: 'bold', color: '#fff' }}>{data?.crew?.name || 'Skate Crew'}</h2>
            <p style={{ margin: '4px 0 0 0', fontSize: '12px', color: '#c084fc', fontFamily: 'monospace' }}>ID: {activeEntity.id}</p>
          </div>
        </div>
      );
    }
    if (activeEntity.type === 'crewSessions') {
      return (
        <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
          <div style={{ width: '56px', height: '56px', borderRadius: '12px', border: '2px solid #f472b6', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '24px', backgroundColor: '#1e293b', color: '#f472b6' }}>
            🔥
          </div>
          <div>
            <h2 style={{ margin: 0, fontSize: '24px', fontWeight: 'bold', color: '#fff' }}>{data?.crewSession?.name || 'Crew Session'}</h2>
            <p style={{ margin: '4px 0 0 0', fontSize: '12px', color: '#f472b6', fontFamily: 'monospace' }}>ID: {activeEntity.id}</p>
          </div>
        </div>
      );
    }
  };

  const renderContent = () => {
    if (loading) {
      return (
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '100%' }}>
          <div style={{ width: '32px', height: '32px', borderRadius: '50%', border: '2px solid #22d3ee', borderTopColor: 'transparent' }} className="animate-spin"></div>
        </div>
      );
    }

    if (!data) return <p style={{ color: '#94a3b8' }}>No data found.</p>;

    if (activeEntity.type === 'users') {
      return (
        <div>
          {activeTab === 'users' && (
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
              <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
                <div style={{ fontSize: '12px', color: '#94a3b8', fontWeight: 'bold', textTransform: 'uppercase', marginBottom: '4px' }}>Lifetime Distance</div>
                <div style={{ fontSize: '20px', color: '#fff', fontFamily: 'monospace' }}>{data.user?.lifetime_distance_miles || 0} mi</div>
              </div>
              <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
                <div style={{ fontSize: '12px', color: '#94a3b8', fontWeight: 'bold', textTransform: 'uppercase', marginBottom: '4px' }}>Top Speed</div>
                <div style={{ fontSize: '20px', color: '#fff', fontFamily: 'monospace' }}>{data.user?.lifetime_top_speed_mph || 0} mph</div>
              </div>
              <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
                <div style={{ fontSize: '12px', color: '#94a3b8', fontWeight: 'bold', textTransform: 'uppercase', marginBottom: '4px' }}>Joined</div>
                <div style={{ fontSize: '20px', color: '#fff', fontFamily: 'monospace' }}>{data.user?.created_at ? new Date(data.user.created_at).toLocaleDateString() : 'N/A'}</div>
              </div>
              <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
                <div style={{ fontSize: '12px', color: '#94a3b8', fontWeight: 'bold', textTransform: 'uppercase', marginBottom: '4px' }}>Role</div>
                <div style={{ fontSize: '20px', color: '#22d3ee', fontFamily: 'monospace', textTransform: 'uppercase' }}>{data.user?.role || 'user'}</div>
              </div>
            </div>
          )}

          {activeTab === 'devices' && (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
              {data.devices?.length === 0 ? <p style={{ color: '#94a3b8', fontStyle: 'italic' }}>No registered devices.</p> : data.devices?.map((d: any) => (
                <div key={d.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
                  <div>
                    <div style={{ color: '#67e8f9', fontWeight: 'bold', fontSize: '18px' }}>{d.custom_name || 'Unnamed Board'}</div>
                    <div style={{ fontSize: '12px', color: '#94a3b8', fontFamily: 'monospace', marginTop: '4px' }}>{d.device_mac}</div>
                  </div>
                  <div style={{ textAlign: 'right' }}>
                    <div style={{ fontSize: '12px', color: '#94a3b8', textTransform: 'uppercase', fontWeight: 'bold' }}>Type</div>
                    <div style={{ fontSize: '14px', color: '#cbd5e1' }}>{d.product_type || 'Unknown'}</div>
                  </div>
                </div>
              ))}
            </div>
          )}

          {activeTab === 'sessions' && (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
              {data.sessions?.length === 0 ? <p style={{ color: '#94a3b8', fontStyle: 'italic' }}>No recent sessions.</p> : data.sessions?.map((s: any) => (
                <div key={s.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
                  <div>
                    <div style={{ color: '#e2e8f0', fontWeight: 'bold' }}>{new Date(s.session_date || s.created_at).toLocaleDateString()}</div>
                    <div style={{ fontSize: '12px', color: '#94a3b8', marginTop: '4px' }}>{s.location_label || 'Unknown Location'}</div>
                  </div>
                  <div style={{ display: 'flex', gap: '16px', textAlign: 'right' }}>
                    <div>
                      <div style={{ fontSize: '12px', color: '#94a3b8', textTransform: 'uppercase', fontWeight: 'bold' }}>Duration</div>
                      <div style={{ fontSize: '14px', color: '#4ade80', fontFamily: 'monospace', fontWeight: 'bold' }}>{s.duration_sec ? `${Math.floor(s.duration_sec/60)}m` : 'Live'}</div>
                    </div>
                    <div>
                      <div style={{ fontSize: '12px', color: '#94a3b8', textTransform: 'uppercase', fontWeight: 'bold' }}>Distance</div>
                      <div style={{ fontSize: '14px', color: '#fff', fontFamily: 'monospace' }}>{s.distance_miles || 0} mi</div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}

          {activeTab === 'crews' && (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
              {data.crews?.length === 0 ? <p style={{ color: '#94a3b8', fontStyle: 'italic' }}>No active crews.</p> : data.crews?.map((cm: any) => (
                <div key={cm.id} style={{ display: 'flex', alignItems: 'center', gap: '16px', backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
                  <div style={{ width: '48px', height: '48px', borderRadius: '8px', backgroundColor: 'rgba(168, 85, 247, 0.2)', border: '1px solid rgba(168, 85, 247, 0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#c084fc', fontSize: '20px', fontWeight: 'bold' }}>
                    {cm.crews?.name?.charAt(0)?.toUpperCase() || 'C'}
                  </div>
                  <div>
                    <div style={{ color: '#d8b4fe', fontWeight: 'bold', fontSize: '18px' }}>{cm.crews?.name || 'Unknown Crew'}</div>
                    <div style={{ fontSize: '12px', color: '#94a3b8', fontFamily: 'monospace', marginTop: '4px' }}>Joined {new Date(cm.joined_at).toLocaleDateString()}</div>
                  </div>
                </div>
              ))}
            </div>
          )}

          {activeTab === 'crew_sessions' && (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
              {data.crewSessions?.length === 0 ? <p style={{ color: '#94a3b8', fontStyle: 'italic' }}>No recent crew sessions.</p> : data.crewSessions?.map((cs: any) => {
                const durationStr = (cs.created_at && cs.ended_at) ? `${Math.floor((new Date(cs.ended_at).getTime() - new Date(cs.created_at).getTime()) / 60000)}m` : 'Live';
                return (
                  <div key={cs.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(168, 85, 247, 0.3)', borderLeft: '4px solid #a855f7' }}>
                    <div>
                      <div style={{ color: '#e2e8f0', fontWeight: 'bold' }}>{cs.name || 'Unnamed Session'}</div>
                      <div style={{ fontSize: '12px', color: '#94a3b8', marginTop: '4px' }}>{new Date(cs.created_at).toLocaleDateString()}</div>
                    </div>
                    <div style={{ display: 'flex', gap: '16px', textAlign: 'right' }}>
                      <div>
                        <div style={{ fontSize: '12px', color: '#94a3b8', textTransform: 'uppercase', fontWeight: 'bold' }}>Status</div>
                        <div style={{ fontSize: '14px', fontFamily: 'monospace', fontWeight: 'bold', color: !cs.ended_at ? '#f43f5e' : '#94a3b8' }}>
                          {durationStr}
                        </div>
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>
      );
    }

    if (activeEntity.type === 'registeredDevices') {
      return (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
            <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
              <div style={{ fontSize: '12px', color: '#94a3b8', fontWeight: 'bold', textTransform: 'uppercase', marginBottom: '4px' }}>Firmware</div>
              <div style={{ fontSize: '20px', color: '#fff', fontFamily: 'monospace' }}>v{data.device?.firmware_ver || '?.?'}</div>
            </div>
            <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
              <div style={{ fontSize: '12px', color: '#94a3b8', fontWeight: 'bold', textTransform: 'uppercase', marginBottom: '4px' }}>LED Points</div>
              <div style={{ fontSize: '20px', color: '#4ade80', fontFamily: 'monospace' }}>{data.device?.led_points || 0}</div>
            </div>
            <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
              <div style={{ fontSize: '12px', color: '#94a3b8', fontWeight: 'bold', textTransform: 'uppercase', marginBottom: '4px' }}>BLE Version</div>
              <div style={{ fontSize: '16px', color: '#4ade80', fontWeight: 'bold' }}>
                {data.device?.ble_version || 'Unknown'}
              </div>
            </div>
            <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
              <div style={{ fontSize: '12px', color: '#94a3b8', fontWeight: 'bold', textTransform: 'uppercase', marginBottom: '4px' }}>Product Type</div>
              <div style={{ fontSize: '16px', color: '#fff', fontWeight: 'bold' }}>{data.device?.product_type || 'N/A'}</div>
            </div>
            <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
              <div style={{ fontSize: '12px', color: '#94a3b8', fontWeight: 'bold', textTransform: 'uppercase', marginBottom: '4px' }}>MAC Address</div>
              <div style={{ fontSize: '14px', color: '#fff', fontFamily: 'monospace' }}>{data.device?.device_mac || 'Unknown'}</div>
            </div>
            <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
              <div style={{ fontSize: '12px', color: '#94a3b8', fontWeight: 'bold', textTransform: 'uppercase', marginBottom: '4px' }}>Registered At</div>
              <div style={{ fontSize: '14px', color: '#fff' }}>{data.device?.registered_at ? new Date(data.device.registered_at).toLocaleString() : 'N/A'}</div>
            </div>
          </div>
          
          <h3 style={{ margin: '8px 0 0', color: '#94a3b8', fontSize: '14px', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Linked Operator</h3>
          {data.user ? (
            <div style={{ display: 'flex', alignItems: 'center', gap: '12px', backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '12px', borderRadius: '12px', border: '1px solid rgba(74, 222, 128, 0.3)' }}>
               <div style={{ width: '32px', height: '32px', borderRadius: '50%', backgroundColor: data.user.avatar_color || '#1e293b', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff', fontWeight: 'bold' }}>
                 {data.user.display_name?.charAt(0) || '?'}
               </div>
               <div style={{ color: '#4ade80', fontWeight: 'bold' }}>{data.user.display_name}</div>
            </div>
          ) : (
            <div style={{ color: '#f43f5e', fontSize: '14px', fontStyle: 'italic' }}>Unclaimed Device</div>
          )}
        </div>
      );
    }

    if (activeEntity.type === 'skateSessions') {
      return (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
            <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
              <div style={{ fontSize: '12px', color: '#94a3b8', fontWeight: 'bold', textTransform: 'uppercase', marginBottom: '4px' }}>Distance</div>
              <div style={{ fontSize: '20px', color: '#fff', fontFamily: 'monospace' }}>{data.session?.distance_miles || 0} mi</div>
            </div>
            <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
              <div style={{ fontSize: '12px', color: '#94a3b8', fontWeight: 'bold', textTransform: 'uppercase', marginBottom: '4px' }}>Top Speed</div>
              <div style={{ fontSize: '20px', color: '#fff', fontFamily: 'monospace' }}>{data.session?.peak_speed_mph || 0} mph</div>
            </div>
            <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
              <div style={{ fontSize: '12px', color: '#94a3b8', fontWeight: 'bold', textTransform: 'uppercase', marginBottom: '4px' }}>Avg Speed</div>
              <div style={{ fontSize: '20px', color: '#fff', fontFamily: 'monospace' }}>{data.session?.avg_speed_mph || 0} mph</div>
            </div>
            <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
              <div style={{ fontSize: '12px', color: '#94a3b8', fontWeight: 'bold', textTransform: 'uppercase', marginBottom: '4px' }}>Duration</div>
              <div style={{ fontSize: '20px', color: '#fff', fontFamily: 'monospace' }}>{data.session?.duration_sec ? `${Math.floor(data.session.duration_sec/60)} min` : 'Live'}</div>
            </div>
          </div>
          
          <h3 style={{ margin: '8px 0 0', color: '#94a3b8', fontSize: '14px', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Rider</h3>
          {data.user ? (
            <div style={{ display: 'flex', alignItems: 'center', gap: '12px', backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '12px', borderRadius: '12px', border: '1px solid rgba(74, 222, 128, 0.3)' }}>
               <div style={{ width: '32px', height: '32px', borderRadius: '50%', backgroundColor: data.user.avatar_color || '#1e293b', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff', fontWeight: 'bold' }}>
                 {data.user.display_name?.charAt(0) || '?'}
               </div>
               <div style={{ color: '#4ade80', fontWeight: 'bold' }}>{data.user.display_name}</div>
            </div>
          ) : (
            <div style={{ color: '#94a3b8', fontSize: '14px', fontStyle: 'italic' }}>Anonymous</div>
          )}
        </div>
      );
    }

    if (activeEntity.type === 'crews') {
      return (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
            <div style={{ fontSize: '12px', color: '#94a3b8', fontWeight: 'bold', textTransform: 'uppercase', marginBottom: '4px' }}>Description</div>
            <div style={{ fontSize: '14px', color: '#cbd5e1' }}>{data.crew?.description || 'No description provided.'}</div>
          </div>
          
          <h3 style={{ margin: '8px 0 0', color: '#94a3b8', fontSize: '14px', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Members ({data.members?.length || 0})</h3>
          {data.members?.slice().sort((a: any, b: any) => (a.user_id === data.crew?.owner_id ? -1 : b.user_id === data.crew?.owner_id ? 1 : 0)).map((m: any) => {
            const isOwner = m.user_id === data.crew?.owner_id;
            return (
              <div key={m.id} style={{ display: 'flex', alignItems: 'center', gap: '12px', backgroundColor: isOwner ? 'rgba(168, 85, 247, 0.15)' : 'rgba(30, 41, 59, 0.4)', padding: '12px', borderRadius: '12px', border: isOwner ? '1px solid rgba(168, 85, 247, 0.6)' : '1px solid rgba(192, 132, 252, 0.3)' }}>
                 <div style={{ width: '32px', height: '32px', borderRadius: '50%', backgroundColor: m.user_profiles?.avatar_color || '#1e293b', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff', fontWeight: 'bold' }}>
                   {m.user_profiles?.display_name?.charAt(0) || '?'}
                 </div>
                 <div style={{ flex: 1 }}>
                   <div style={{ color: '#c084fc', fontWeight: 'bold', display: 'flex', alignItems: 'center', gap: '8px' }}>
                     {m.user_profiles?.display_name || 'Unknown'}
                     {isOwner && <span style={{ fontSize: '10px', backgroundColor: '#a855f7', color: '#fff', padding: '2px 6px', borderRadius: '4px', textTransform: 'uppercase' }}>Owner</span>}
                   </div>
                   <div style={{ fontSize: '10px', color: '#94a3b8' }}>{m.role || 'Member'}</div>
                 </div>
              </div>
            );
          })}
        </div>
      );
    }

    if (activeEntity.type === 'crewSessions') {
      return (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
            <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
              <div style={{ fontSize: '12px', color: '#94a3b8', fontWeight: 'bold', textTransform: 'uppercase', marginBottom: '4px' }}>Status</div>
              <div style={{ fontSize: '20px', color: data.crewSession?.is_active ? '#4ade80' : '#f43f5e', fontFamily: 'monospace' }}>{data.crewSession?.is_active ? 'LIVE' : 'ENDED'}</div>
            </div>
            <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
              <div style={{ fontSize: '12px', color: '#94a3b8', fontWeight: 'bold', textTransform: 'uppercase', marginBottom: '4px' }}>Avg Speed</div>
              <div style={{ fontSize: '20px', color: '#fff', fontFamily: 'monospace' }}>{data.crewSession?.avg_speed_mph || 0} mph</div>
            </div>
            <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
              <div style={{ fontSize: '12px', color: '#94a3b8', fontWeight: 'bold', textTransform: 'uppercase', marginBottom: '4px' }}>Top Speed</div>
              <div style={{ fontSize: '20px', color: '#fff', fontFamily: 'monospace' }}>{data.crewSession?.top_speed_mph || 0} mph</div>
            </div>
            <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(51, 65, 85, 0.5)' }}>
              <div style={{ fontSize: '12px', color: '#94a3b8', fontWeight: 'bold', textTransform: 'uppercase', marginBottom: '4px' }}>Total Distance</div>
              <div style={{ fontSize: '20px', color: '#fff', fontFamily: 'monospace' }}>{data.crewSession?.total_distance_miles || 0} mi</div>
            </div>
          </div>
          
          <h3 style={{ margin: '8px 0 0', color: '#94a3b8', fontSize: '14px', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Participants ({data.members?.length || 0})</h3>
          {data.members?.slice().sort((a: any, b: any) => (a.user_id === data.crewSession?.leader_user_id ? -1 : b.user_id === data.crewSession?.leader_user_id ? 1 : 0)).map((m: any) => {
            const isLeader = m.user_id === data.crewSession?.leader_user_id;
            return (
              <div key={m.id} style={{ display: 'flex', alignItems: 'center', gap: '12px', backgroundColor: isLeader ? 'rgba(244, 114, 182, 0.15)' : 'rgba(30, 41, 59, 0.4)', padding: '12px', borderRadius: '12px', border: isLeader ? '1px solid rgba(244, 114, 182, 0.6)' : '1px solid rgba(244, 114, 182, 0.3)' }}>
                 <div style={{ flex: 1 }}>
                   <div style={{ color: '#f472b6', fontWeight: 'bold', display: 'flex', alignItems: 'center', gap: '8px' }}>
                     {m.display_name || m.user_id || 'Unknown'}
                     {isLeader && <span style={{ fontSize: '10px', backgroundColor: '#f472b6', color: '#fff', padding: '2px 6px', borderRadius: '4px', textTransform: 'uppercase' }}>Leader</span>}
                   </div>
                   <div style={{ fontSize: '10px', color: '#94a3b8' }}>Joined: {new Date(m.joined_at).toLocaleTimeString()}</div>
                 </div>
              </div>
            );
          })}
        </div>
      );
    }
  };

  // Set proper container width based on type
  const containerWidth = activeEntity.type === 'users' ? '600px' : '500px';

  return (
    <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 50, pointerEvents: 'none', padding: '16px' }}>
      <div className="glass-panel" style={{ width: '100%', maxWidth: containerWidth, backgroundColor: 'rgba(15, 23, 42, 0.95)', border: '1px solid rgba(34, 211, 238, 0.3)', boxShadow: '0 0 50px rgba(0, 0, 0, 0.5)', borderRadius: '16px', overflow: 'hidden', pointerEvents: 'auto', display: 'flex', flexDirection: 'column' }}>
        
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '24px', borderBottom: '1px solid rgba(51, 65, 85, 0.5)', background: 'linear-gradient(90deg, rgba(15,23,42,0.9), rgba(30,41,59,0.9))' }}>
          {renderHeader()}
          <button onClick={onClose} style={{ width: '32px', height: '32px', borderRadius: '50%', backgroundColor: '#1e293b', border: '1px solid #334155', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#cbd5e1', cursor: 'pointer', fontSize: '18px' }}>
            &times;
          </button>
        </div>

        {/* Restore Tabs exclusively for Users layer */}
        {activeEntity.type === 'users' && !loading && data && (
          <div style={{ display: 'flex', padding: '16px 24px 0', borderBottom: '1px solid #1e293b', backgroundColor: 'rgba(15, 23, 42, 0.5)', gap: '24px', overflowX: 'auto' }}>
            {tabs.map(tab => (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id as any)}
                style={{
                  paddingBottom: '12px',
                  fontSize: '14px',
                  fontWeight: 'bold',
                  textTransform: 'uppercase',
                  letterSpacing: '0.05em',
                  whiteSpace: 'nowrap',
                  background: 'none',
                  border: 'none',
                  borderBottom: `2px solid ${activeTab === tab.id ? '#22d3ee' : 'transparent'}`,
                  color: activeTab === tab.id ? '#22d3ee' : '#94a3b8',
                  cursor: 'pointer',
                  transition: 'all 0.2s'
                }}
              >
                {tab.label}
              </button>
            ))}
          </div>
        )}

        <div style={{ padding: '24px', maxHeight: activeEntity.type === 'users' ? '320px' : '60vh', overflowY: 'auto', backgroundColor: activeEntity.type === 'users' ? 'rgba(15, 23, 42, 0.3)' : 'rgba(15, 23, 42, 0.5)' }}>
          {renderContent()}
        </div>
      </div>
    </div>
  );
};
