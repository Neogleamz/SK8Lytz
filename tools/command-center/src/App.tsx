import React, { useEffect, useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate, Link } from 'react-router-dom';
import { supabase, checkAdminStatus } from './services/supabase';
import { LayoutDashboard, Users, ShieldAlert, Activity, Settings, Map, Bug, Database } from 'lucide-react';
import ScraperApp from './scraper/ScraperApp';
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
        <Link to="/scraper" className="nav-link"><Database size={18} /> Scraper Dashboard</Link>
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
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);

  const displayMessage = (msg: string, error: boolean = false) => {
    setMessage(msg);
    setIsError(error);
  };

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    displayMessage('');
    if (!email || !password) return displayMessage('Email and password required', true);
    const { error } = await supabase.auth.signInWithPassword({ email, password });
    if (error) displayMessage(error.message, true);
  };

  const handleSignUp = async () => {
    displayMessage('');
    if (!email || !password) return displayMessage('Email and password required', true);
    const { error } = await supabase.auth.signUp({ email, password });
    if (error) displayMessage(error.message, true);
    else displayMessage('Sign up successful! You may now log in.');
  };

  const handleResetPassword = async () => {
    displayMessage('');
    if (!email) return displayMessage('Enter your email first to reset password', true);
    const { error } = await supabase.auth.resetPasswordForEmail(email, {
      redirectTo: window.location.origin + '/',
    });
    if (error) displayMessage(error.message, true);
    else displayMessage('Password reset email sent! Check your inbox.');
  };

  return (
    <div className="auth-container">
      <form className="auth-form glass-panel">
        <h2 className="auth-title">Admin Login</h2>
        
        {message && (
          <div style={{ color: isError ? '#ff6b6b' : '#51cf66', marginBottom: '15px', fontSize: '14px', textAlign: 'center' }}>
            {message}
          </div>
        )}

        <input type="email" placeholder="Email" className="auth-input" value={email} onChange={e => setEmail(e.target.value)} />
        <input type="password" placeholder="Password" className="auth-input" value={password} onChange={e => setPassword(e.target.value)} />
        
        <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
          <button type="button" onClick={handleLogin} className="auth-button glass-btn">Login</button>
          <div style={{ display: 'flex', gap: '10px' }}>
            <button type="button" onClick={handleSignUp} className="auth-button glass-btn" style={{ flex: 1, background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)' }}>Sign Up</button>
            <button type="button" onClick={handleResetPassword} className="auth-button glass-btn" style={{ flex: 1, background: 'transparent', border: '1px solid rgba(255,255,255,0.1)', color: '#aaa' }}>Reset Password</button>
          </div>
        </div>
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
          <Route path="/scraper" element={<div className="scraper-legacy-root w-full h-full"><ScraperApp /></div>} />
          <Route path="/settings" element={<ControlTowerWidget />} />
          <Route path="/hardware" element={<HardwareBanWidget />} />
          <Route path="/users" element={<UserManagementWidget />} />
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </AdminLayout>
    </BrowserRouter>
  );
}
