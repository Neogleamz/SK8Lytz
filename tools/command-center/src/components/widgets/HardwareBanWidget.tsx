import React, { useEffect, useState } from 'react';
import { supabase } from '../../services/supabase';
import { Trash2, Plus } from 'lucide-react';

interface HardwareBan {
  mac_address: string;
  reason: string;
  created_at?: string;
}

export default function HardwareBanWidget() {
  const [bans, setBans] = useState<HardwareBan[]>([]);
  const [loading, setLoading] = useState(true);
  const [newMac, setNewMac] = useState('');
  const [newReason, setNewReason] = useState('');

  useEffect(() => {
    fetchBans();
  }, []);

  const fetchBans = async () => {
    setLoading(true);
    const { data } = await supabase.from('hardware_blacklist').select('*').order('created_at', { ascending: false });
    if (data) setBans(data as HardwareBan[]);
    setLoading(false);
  };

  const addBan = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newMac || !newReason) return;
    
    const { data, error } = await supabase
      .from('hardware_blacklist')
      .insert([{ mac_address: newMac, reason: newReason }])
      .select()
      .single();
      
    if (data && !error) {
      setBans([data as HardwareBan, ...bans]);
      setNewMac('');
      setNewReason('');
    }
  };

  const removeBan = async (mac: string) => {
    const { error } = await supabase.from('hardware_blacklist').delete().eq('mac_address', mac);
    if (!error) {
      setBans(bans.filter(b => b.mac_address !== mac));
    }
  };

  if (loading) return <div className="text-cyan-400">Loading Hardware Ban List...</div>;

  return (
    <div className="flex flex-col gap-6">
      <h2 className="text-2xl font-bold text-white">Hardware Access Control (Ban-Hammer)</h2>
      
      <form onSubmit={addBan} className="glass-panel p-4 rounded-xl border border-slate-800 flex flex-col md:flex-row gap-4 items-end">
        <div className="flex-1 w-full">
          <label className="block text-sm text-slate-400 mb-1">MAC Address</label>
          <input 
            type="text" 
            placeholder="00:11:22:33:44:55"
            className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full"
            value={newMac}
            onChange={e => setNewMac(e.target.value.toUpperCase())}
          />
        </div>
        <div className="flex-[2] w-full">
          <label className="block text-sm text-slate-400 mb-1">Reason</label>
          <input 
            type="text" 
            placeholder="e.g. Cloned controller ID"
            className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full"
            value={newReason}
            onChange={e => setNewReason(e.target.value)}
          />
        </div>
        <button type="submit" className="glass-btn px-4 py-2 rounded text-white flex items-center gap-2 w-full md:w-auto justify-center">
          <Plus size={18} /> Ban Device
        </button>
      </form>

      <div className="glass-panel rounded-xl border border-slate-800 overflow-hidden">
        <table className="w-full text-left border-collapse">
          <thead>
            <tr className="bg-slate-900/50 border-b border-slate-800">
              <th className="p-4 text-slate-300 font-medium">MAC Address</th>
              <th className="p-4 text-slate-300 font-medium">Reason</th>
              <th className="p-4 text-slate-300 font-medium">Date Banned</th>
              <th className="p-4 text-right">Actions</th>
            </tr>
          </thead>
          <tbody>
            {bans.length === 0 ? (
              <tr><td colSpan={4} className="p-4 text-slate-500 text-center">No devices currently banned.</td></tr>
            ) : bans.map((ban, i) => (
              <tr key={ban.mac_address} className={`border-b border-slate-800/50 ${i % 2 === 0 ? 'bg-slate-900/20' : ''}`}>
                <td className="p-4 font-mono text-cyan-400">{ban.mac_address}</td>
                <td className="p-4 text-slate-300">{ban.reason}</td>
                <td className="p-4 text-slate-400">{ban.created_at ? new Date(ban.created_at).toLocaleDateString() : 'Unknown'}</td>
                <td className="p-4 text-right">
                  <button onClick={() => removeBan(ban.mac_address)} className="text-slate-500 hover:text-red-400 transition-colors">
                    <Trash2 size={18} />
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
