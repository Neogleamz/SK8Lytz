import React, { useEffect, useState } from 'react';
import { supabase } from '../../services/supabase';
import { ToggleLeft, ToggleRight, Save } from 'lucide-react';

interface AppSetting {
  setting_key: string;
  setting_value: string;
  description: string;
}

interface FeatureFlag {
  flag_key: string;
  is_enabled: boolean;
  description: string;
}

export default function ControlTowerWidget() {
  const [settings, setSettings] = useState<AppSetting[]>([]);
  const [flags, setFlags] = useState<FeatureFlag[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchControls();
  }, []);

  const fetchControls = async () => {
    setLoading(true);
    const [settingsRes, flagsRes] = await Promise.all([
      supabase.from('app_settings').select('*'),
      supabase.from('feature_flags').select('*')
    ]);
    
    if (settingsRes.data) setSettings(settingsRes.data as AppSetting[]);
    if (flagsRes.data) setFlags(flagsRes.data as FeatureFlag[]);
    setLoading(false);
  };

  const toggleFlag = async (flag: FeatureFlag) => {
    const newVal = !flag.is_enabled;
    const { error } = await supabase
      .from('feature_flags')
      .update({ is_enabled: newVal })
      .eq('flag_key', flag.flag_key);
      
    if (!error) {
      setFlags(flags.map(f => f.flag_key === flag.flag_key ? { ...f, is_enabled: newVal } : f));
    }
  };

  const updateSetting = async (key: string, value: string) => {
    const { error } = await supabase
      .from('app_settings')
      .update({ setting_value: value })
      .eq('setting_key', key);
      
    if (!error) {
      setSettings(settings.map(s => s.setting_key === key ? { ...s, setting_value: value } : s));
    }
  };

  if (loading) return <div className="text-cyan-400">Loading Control Tower...</div>;

  return (
    <div className="flex flex-col gap-8">
      <div>
        <h2 className="text-2xl font-bold text-white mb-4">Feature Flags (Real-time)</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {flags.map(flag => (
            <div key={flag.flag_key} className="glass-panel p-4 rounded-xl border border-slate-800 flex justify-between items-center">
              <div>
                <div className="font-semibold text-white">{flag.flag_key}</div>
                <div className="text-xs text-slate-400 mt-1">{flag.description || 'No description'}</div>
              </div>
              <button 
                onClick={() => toggleFlag(flag)}
                className={`transition-colors ${flag.is_enabled ? 'text-cyan-400' : 'text-slate-500 hover:text-slate-400'}`}
              >
                {flag.is_enabled ? <ToggleRight size={32} /> : <ToggleLeft size={32} />}
              </button>
            </div>
          ))}
        </div>
      </div>

      <div>
        <h2 className="text-2xl font-bold text-white mb-4">Global App Settings</h2>
        <div className="flex flex-col gap-4">
          {settings.map(setting => (
            <div key={setting.setting_key} className="glass-panel p-4 rounded-xl border border-slate-800 flex flex-col md:flex-row md:items-center gap-4">
              <div className="flex-1">
                <div className="font-semibold text-white">{setting.setting_key}</div>
                <div className="text-xs text-slate-400 mt-1">{setting.description || 'No description'}</div>
              </div>
              <div className="flex gap-2 w-full md:w-auto">
                <input 
                  type="text" 
                  className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full md:w-64"
                  defaultValue={setting.setting_value}
                  onBlur={(e) => {
                    if (e.target.value !== setting.setting_value) {
                      updateSetting(setting.setting_key, e.target.value);
                    }
                  }}
                />
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
