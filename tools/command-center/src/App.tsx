import React, { useEffect, useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate, Link } from 'react-router-dom';
import { supabase, checkAdminStatus } from './services/supabase';
import { LayoutDashboard, Users, ShieldAlert, Activity, Settings, Map, Bug } from 'lucide-react';
import MapWidget from './components/widgets/MapWidget';
import FleetHealthWidget from './components/widgets/FleetHealthWidget';
import AppPerformanceWidget from './components/widgets/AppPerformanceWidget';
import ControlTowerWidget from './components/widgets/ControlTowerWidget';
import HardwareBanWidget from './components/widgets/HardwareBanWidget';
import UserManagementWidget from './components/widgets/UserManagementWidget';
import { LiveDebuggerWidget } from './components/widgets/LiveDebuggerWidget';
import './index.css';

const AdminLayout = ({ children }: { children: React.ReactNode }) => (
  <div className="admin-layout">
    <aside className="sidebar">
      <h1 className="brand-title">SK8Lytz Command Center</h1>
      <nav className="nav-menu">
        <Link to="/" className="nav-link"><Map size={18} /> Fleet Map</Link>
        <Link to="/fleet-health" className="nav-link"><Activity size={18} /> Fleet Health</Link>
        <Link to="/performance" className="nav-link"><LayoutDashboard size={18} /> App Performance</Link>
        <Link to="/live-debugger" className="nav-link text-red-400"><Bug size={18} /> Live Debugger</Link>
        <Link to="/settings" className="nav-link"><Settings size={18} /> Control Tower</Link>
        <Link to="/hardware" className="nav-link"><ShieldAlert size={18} /> Hardware Ban</Link>
        <Link to="/users" className="nav-link"><Users size={18} /> User Mgmt</Link>
      </nav>
      <div className="sidebar-footer">
        <button onClick={() => supabase.auth.signOut()} className="signout-btn">Sign Out</button>
      </div>
    </aside>
    <main className="main-content">
      {children}
    </main>
  </div>
);

const AuthScreen = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    await supabase.auth.signInWithPassword({ email, password });
  };

  return (
    <div className="auth-container">
      <form onSubmit={handleLogin} className="auth-form glass-panel">
        <h2 className="auth-title">Admin Login</h2>
        <input type="email" placeholder="Email" className="auth-input" value={email} onChange={e => setEmail(e.target.value)} />
        <input type="password" placeholder="Password" className="auth-input" value={password} onChange={e => setPassword(e.target.value)} />
        <button type="submit" className="auth-button glass-btn">Authenticate</button>
      </form>
    </div>
  );
};

export default function App() {
  const [session, setSession] = useState<any>(null);
  const [isAdmin, setIsAdmin] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    supabase.auth.getSession().then(({ data: { session } }) => {
      setSession(session);
      if (session) checkAdminStatus().then(setIsAdmin).finally(() => setLoading(false));
      else setLoading(false);
    });

    supabase.auth.onAuthStateChange((_event, session) => {
      setSession(session);
      if (session) checkAdminStatus().then(setIsAdmin);
    });
  }, []);

  if (loading) return <div className="loading-screen">Loading Command Center...</div>;

  if (!session || !isAdmin) {
    return <AuthScreen />;
  }

  return (
    <BrowserRouter>
      <AdminLayout>
        <Routes>
          <Route path="/" element={<MapWidget />} />
          <Route path="/fleet-health" element={<FleetHealthWidget />} />
          <Route path="/performance" element={<AppPerformanceWidget />} />
          <Route path="/live-debugger" element={<div className="p-6"><LiveDebuggerWidget /></div>} />
          <Route path="/settings" element={<ControlTowerWidget />} />
          <Route path="/hardware" element={<HardwareBanWidget />} />
          <Route path="/users" element={<UserManagementWidget />} />
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </AdminLayout>
    </BrowserRouter>
  );
}
