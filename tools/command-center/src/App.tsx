import React, { useEffect, useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate, Link } from 'react-router-dom';
import { supabase, checkAdminStatus } from './services/supabase';
import { LayoutDashboard, Users, ShieldAlert, Activity, Settings, Map, Bug, Database, Palette, ChevronDown, ChevronRight, Radar, Megaphone, ScrollText, Truck, Smartphone, BookOpen, Box } from 'lucide-react';
import ScraperApp from './scraper/ScraperApp';
import MapWidget from './components/widgets/MapWidget';
import FleetHealthWidget from './components/widgets/FleetHealthWidget';
import AppPerformanceWidget from './components/widgets/AppPerformanceWidget';
import ControlTowerWidget from './components/widgets/ControlTowerWidget';
import HardwareBanWidget from './components/widgets/HardwareBanWidget';
import UserManagementWidget from './components/widgets/UserManagementWidget';
import { LiveDebuggerWidget } from './components/widgets/LiveDebuggerWidget';
import PicksManagerWidget from './components/widgets/PicksManagerWidget';
import ReleaseRadarWidget from './components/widgets/ReleaseRadarWidget';
import AuditLogsWidget from './components/widgets/AuditLogsWidget';
import PromoBlastWidget from './components/widgets/PromoBlastWidget';
import CheatSheetWidget from './components/widgets/CheatSheetWidget';
import AppManagerWidget from './components/widgets/AppManagerWidget';
import ProductCatalogWidget from './components/widgets/ProductCatalogWidget';
import './index.css';

const NavGroup = ({ title, icon: Icon, children }: { title: string; icon: any; children: React.ReactNode }) => {
  const [isOpen, setIsOpen] = useState(true);
  return (
    <div className="nav-group mb-2">
      <div className="nav-group-title" onClick={() => setIsOpen(!isOpen)}>
        <div className="group-icon-title">
          <Icon size={18} />
          <span>{title}</span>
        </div>
        {isOpen ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
      </div>
      {isOpen && (
        <div className="nav-group-children">
          {children}
        </div>
      )}
    </div>
  );
};

const AdminLayout = ({ children }: { children: React.ReactNode }) => (
  <div className="admin-layout">
    <aside className="sidebar">
      <h1 className="brand-title">SK8Lytz Command Center</h1>
      <nav className="nav-menu">
        <NavGroup title="Fleet Manager" icon={Truck}>
          <Link to="/" className="nav-link"><Map size={16} /> Fleet Map</Link>
          <Link to="/fleet-health" className="nav-link"><Activity size={16} /> Fleet Health</Link>
          <Link to="/hardware" className="nav-link"><ShieldAlert size={16} /> Hardware Ban</Link>
        </NavGroup>

        <NavGroup title="App Manager" icon={Smartphone}>
          <Link to="/app-simulator" className="nav-link text-blue-400"><Smartphone size={16} /> SK8Lytz APP</Link>
          <Link to="/performance" className="nav-link"><LayoutDashboard size={16} /> App Performance</Link>
          <Link to="/live-debugger" className="nav-link text-red-400"><Bug size={16} /> Live Debugger</Link>
          <Link to="/settings" className="nav-link"><Settings size={16} /> Control Tower</Link>
          <Link to="/release-radar" className="nav-link"><Radar size={16} /> Release Radar</Link>
          <Link to="/picks-manager" className="nav-link"><Palette size={16} /> Picks Manager</Link>
          <Link to="/product-catalog" className="nav-link"><Box size={16} /> Product Catalog</Link>
          <Link to="/promo-blast" className="nav-link"><Megaphone size={16} /> Promo Blast</Link>
        </NavGroup>

        <NavGroup title="User Manager" icon={Users}>
          <Link to="/users" className="nav-link"><Users size={16} /> User Mgmt</Link>
          <Link to="/audit-logs" className="nav-link"><ScrollText size={16} /> Audit Logs</Link>
        </NavGroup>

        <NavGroup title="Content & Data" icon={Database}>
          <Link to="/scraper" className="nav-link"><Database size={16} /> Scraper Dashboard</Link>
          <Link to="/cheat-sheet" className="nav-link"><BookOpen size={16} /> Protocol Cheat Sheet</Link>
        </NavGroup>
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
          <Route path="/picks-manager" element={<div className="p-6"><PicksManagerWidget /></div>} />
          <Route path="/product-catalog" element={<div className="p-6"><ProductCatalogWidget /></div>} />
          <Route path="/users" element={<UserManagementWidget />} />
          <Route path="/release-radar" element={<ReleaseRadarWidget />} />
          <Route path="/audit-logs" element={<AuditLogsWidget />} />
          <Route path="/promo-blast" element={<PromoBlastWidget />} />
          <Route path="/cheat-sheet" element={<div className="p-6 h-full"><CheatSheetWidget /></div>} />
          <Route path="/app-simulator" element={<AppManagerWidget />} />
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </AdminLayout>
    </BrowserRouter>
  );
}
