import React, { useEffect, useState } from 'react';
import { supabase } from '../../services/supabase';

interface EntityInspectorSidebarProps {
  activeUserId: string | null;
  onClose: () => void;
}

export const EntityInspectorSidebar: React.FC<EntityInspectorSidebarProps> = ({ activeUserId, onClose }) => {
  const [loading, setLoading] = useState(false);
  const [userProfile, setUserProfile] = useState<any>(null);
  const [devices, setDevices] = useState<any[]>([]);
  const [sessions, setSessions] = useState<any[]>([]);
  const [crewMemberships, setCrewMemberships] = useState<any[]>([]);
  const [crewSessions, setCrewSessions] = useState<any[]>([]);

  useEffect(() => {
    if (!activeUserId) return;
    
    const fetchRelationalData = async () => {
      setLoading(true);
      try {
        // Fetch User Profile
        const { data: user } = await supabase.from('user_profiles').select('*').eq('user_id', activeUserId).single();
        setUserProfile(user);

        // Fetch Devices
        const { data: devs } = await supabase.from('registered_devices').select('*').eq('user_id', activeUserId);
        setDevices(devs || []);

        // Fetch Skate Sessions
        const { data: sess } = await supabase.from('skate_sessions').select('*').eq('user_id', activeUserId).order('session_date', { ascending: false }).limit(5);
        setSessions(sess || []);

        // Fetch Crew Memberships
        const { data: crews } = await supabase.from('crew_memberships')
          .select('*, crews(*)')
          .eq('user_id', activeUserId);
        setCrewMemberships(crews || []);

        if (crews && crews.length > 0) {
           const crewIds = crews.map(c => c.crew_id);
           const { data: cSess } = await supabase.from('crew_sessions').select('*').in('crew_id', crewIds).order('created_at', { ascending: false }).limit(5);
           setCrewSessions(cSess || []);
        } else {
           setCrewSessions([]);
        }
        
      } catch (err) {
        console.error('Failed to fetch entity data:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchRelationalData();
  }, [activeUserId]);

  if (!activeUserId) return null;

  return (
    <div className="glass-panel w-80 h-full border border-slate-700/50 bg-slate-900/90 shadow-2xl flex flex-col z-40 transition-all duration-300 rounded-xl overflow-hidden shrink-0">
      <div className="p-4 border-b border-slate-700/50 flex justify-between items-center bg-slate-800/80">
        <h2 className="text-cyan-400 font-bold text-sm tracking-widest uppercase">Entity Inspector</h2>
        <button onClick={onClose} className="text-slate-400 hover:text-white transition-colors p-1">
          <span className="text-xl leading-none">&times;</span>
        </button>
      </div>

      <div className="flex-1 overflow-y-auto p-4 flex flex-col gap-4 custom-scrollbar">
        {loading ? (
          <div className="flex items-center justify-center h-32">
            <div className="w-6 h-6 border-2 border-cyan-500 border-t-transparent rounded-full animate-spin"></div>
          </div>
        ) : (
          <>
            {/* User Profile Card */}
            <div className="bg-slate-800/40 border border-slate-700/50 rounded-lg p-3 shadow-sm">
              <div className="text-xs text-slate-500 font-bold uppercase tracking-wider mb-2">User Profile</div>
              <div className="font-medium text-slate-200">{userProfile?.display_name || 'Unknown User'}</div>
              <div className="text-xs text-slate-400 mt-1 font-mono break-all">{activeUserId}</div>
            </div>

            {/* Crew Memberships */}
            <div className="bg-slate-800/40 border border-slate-700/50 rounded-lg p-3 shadow-sm">
              <div className="text-xs text-slate-500 font-bold uppercase tracking-wider mb-2">Active Crews ({crewMemberships.length})</div>
              {crewMemberships.length === 0 ? (
                <div className="text-xs text-slate-500 italic">No crew affiliations</div>
              ) : (
                <div className="flex flex-col gap-2">
                  {crewMemberships.map(cm => (
                    <div key={cm.id} className="text-sm text-slate-300 flex items-center gap-2">
                      <div className="w-2 h-2 rounded-full bg-purple-400 shadow-[0_0_8px_rgba(192,132,252,0.8)]"></div>
                      <span className="font-medium">{cm.crews?.name || 'Unknown Crew'}</span>
                    </div>
                  ))}
                </div>
              )}
            </div>

            {/* Devices */}
            <div className="bg-slate-800/40 border border-slate-700/50 rounded-lg p-3 shadow-sm">
              <div className="text-xs text-slate-500 font-bold uppercase tracking-wider mb-2">Hardware ({devices.length})</div>
              {devices.length === 0 ? (
                <div className="text-xs text-slate-500 italic">No registered devices</div>
              ) : (
                <div className="flex flex-col gap-2">
                  {devices.map(d => (
                    <div key={d.id} className="text-sm bg-[#0f172a] p-2 rounded border border-slate-700/50">
                      <div className="text-cyan-300 font-medium">{d.custom_name || 'Unnamed Board'}</div>
                      <div className="text-xs text-slate-500 font-mono mt-1">{d.device_mac}</div>
                    </div>
                  ))}
                </div>
              )}
            </div>

            {/* Recent Sessions */}
            <div className="bg-slate-800/40 border border-slate-700/50 rounded-lg p-3 shadow-sm">
              <div className="text-xs text-slate-500 font-bold uppercase tracking-wider mb-2">Recent Skate Sessions</div>
              {sessions.length === 0 ? (
                <div className="text-xs text-slate-500 italic">No recent sessions</div>
              ) : (
                <div className="flex flex-col gap-2">
                  {sessions.map(s => (
                    <div key={s.id} className="text-xs flex justify-between items-center text-slate-300 bg-[#0f172a]/50 p-2 rounded">
                      <span>{new Date(s.session_date || s.created_at).toLocaleDateString()}</span>
                      <span className="text-[#4ade80] font-mono font-bold">{s.duration_sec ? `${Math.floor(s.duration_sec/60)}m` : 'Live'}</span>
                    </div>
                  ))}
                </div>
              )}
            </div>

            {/* Crew Sessions */}
            {crewMemberships.length > 0 && (
              <div className="bg-slate-800/40 border border-slate-700/50 rounded-lg p-3 shadow-sm">
                <div className="text-xs text-slate-500 font-bold uppercase tracking-wider mb-2">Recent Crew Sessions</div>
                {crewSessions.length === 0 ? (
                  <div className="text-xs text-slate-500 italic">No recent crew sessions</div>
                ) : (
                  <div className="flex flex-col gap-2">
                    {crewSessions.map(cs => {
                      const durationStr = (cs.created_at && cs.ended_at) ? `${Math.floor((new Date(cs.ended_at).getTime() - new Date(cs.created_at).getTime()) / 60000)}m` : 'Live';
                      return (
                      <div key={cs.id} className="text-xs flex justify-between items-center text-slate-300 bg-[#0f172a]/50 p-2 rounded border-l-2 border-purple-400">
                        <span className="truncate max-w-[120px]">{cs.name || 'Unnamed Session'}</span>
                        <span className="text-[#f43f5e] font-mono font-bold">{durationStr}</span>
                      </div>
                      );
                    })}
                  </div>
                )}
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
};
