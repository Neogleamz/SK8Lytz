import { useEffect, useState } from 'react';
import { supabase } from '../../services/supabase';
import { Shield, ShieldAlert } from 'lucide-react';

interface UserProfile {
  user_id: string;
  role: string;
  username?: string;
  created_at?: string;
}

export default function UserManagementWidget() {
  const [users, setUsers] = useState<UserProfile[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    setLoading(true);
    const { data } = await supabase.from('user_profiles').select('user_id, role, username, created_at').order('created_at', { ascending: false }).limit(50);
    if (data) setUsers(data as UserProfile[]);
    setLoading(false);
  };

  const toggleAdminRole = async (userId: string, currentRole: string) => {
    const newRole = currentRole === 'admin' ? 'user' : 'admin';
    const { error } = await supabase
      .from('user_profiles')
      .update({ role: newRole })
      .eq('user_id', userId);
      
    if (!error) {
      setUsers(users.map(u => u.user_id === userId ? { ...u, role: newRole } : u));
    }
  };

  if (loading) return <div className="text-cyan-400">Loading User Management...</div>;

  return (
    <div className="flex flex-col gap-6">
      <h2 className="text-2xl font-bold text-white">Global User Management</h2>
      <p className="text-slate-400">Manage registered SK8Lytz app users. Assigning an 'admin' role grants them access to this Command Center.</p>

      <div className="glass-panel rounded-xl border border-slate-800 overflow-hidden mt-4">
        <table className="w-full text-left border-collapse">
          <thead>
            <tr className="bg-slate-900/50 border-b border-slate-800">
              <th className="p-4 text-slate-300 font-medium">Username</th>
              <th className="p-4 text-slate-300 font-medium">User ID</th>
              <th className="p-4 text-slate-300 font-medium">Registered On</th>
              <th className="p-4 text-slate-300 font-medium">Role</th>
              <th className="p-4 text-right">Actions</th>
            </tr>
          </thead>
          <tbody>
            {users.length === 0 ? (
              <tr><td colSpan={5} className="p-4 text-slate-500 text-center">No users found.</td></tr>
            ) : users.map((user, i) => (
              <tr key={user.user_id} className={`border-b border-slate-800/50 ${i % 2 === 0 ? 'bg-slate-900/20' : ''}`}>
                <td className="p-4 text-white font-medium">{user.username || 'Anonymous'}</td>
                <td className="p-4 font-mono text-xs text-slate-500">{user.user_id}</td>
                <td className="p-4 text-slate-400">{user.created_at ? new Date(user.created_at).toLocaleDateString() : 'Unknown'}</td>
                <td className="p-4">
                  <span className={`px-2 py-1 rounded text-xs font-medium ${user.role === 'admin' ? 'bg-cyan-900/50 text-cyan-400 border border-cyan-800' : 'bg-slate-800 text-slate-400 border border-slate-700'}`}>
                    {user.role}
                  </span>
                </td>
                <td className="p-4 text-right">
                  <button 
                    onClick={() => toggleAdminRole(user.user_id, user.role)} 
                    className="glass-btn px-3 py-1 rounded text-xs text-white flex items-center gap-2 ml-auto"
                  >
                    {user.role === 'admin' ? <><Shield size={14} /> Revoke Admin</> : <><ShieldAlert size={14} /> Grant Admin</>}
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
