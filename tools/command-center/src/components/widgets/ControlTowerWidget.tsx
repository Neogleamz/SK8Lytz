import { useEffect, useState } from 'react';
import { supabase } from '../../services/supabase';

interface AppSetting {
  setting_key: string;
  setting_value: any;
  description: string;
  is_enabled: boolean;
}

interface FeatureFlag {
  flag_key: string;
  is_enabled: boolean;
  description: string;
}

const ModernToggle = ({ checked, onChange }: { checked: boolean; onChange: () => void }) => (
  <div 
    onClick={onChange}
    style={{
      width: '56px',
      height: '30px',
      backgroundColor: checked ? '#10b981' : '#334155',
      borderRadius: '30px',
      cursor: 'pointer',
      position: 'relative',
      transition: 'background-color 0.3s ease',
      display: 'flex',
      alignItems: 'center',
      padding: '2px',
      boxShadow: checked ? '0 0 12px rgba(16, 185, 129, 0.4)' : 'inset 0 2px 4px rgba(0,0,0,0.3)',
      boxSizing: 'border-box'
    }}
  >
    <div 
      style={{
        width: '26px',
        height: '26px',
        backgroundColor: '#ffffff',
        borderRadius: '50%',
        transform: checked ? 'translateX(26px)' : 'translateX(0)',
        transition: 'transform 0.3s cubic-bezier(0.4, 0.0, 0.2, 1)',
        boxShadow: '0 2px 5px rgba(0,0,0,0.3)'
      }}
    />
  </div>
);

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
      supabase.from('sk8lytz_app_settings').select('*').order('setting_key'),
      supabase.from('feature_flags').select('*').order('flag_key')
    ]);
    
    if (settingsRes.data) setSettings(settingsRes.data as unknown as AppSetting[]);
    if (flagsRes.data) setFlags(flagsRes.data as unknown as FeatureFlag[]);
    setLoading(false);
  };

  const toggleFlag = async (flag: FeatureFlag) => {
    const newVal = !flag.is_enabled;
    const { error } = await supabase
      .from('feature_flags')
      .update({ is_enabled: newVal } as any)
      .eq('flag_key', flag.flag_key);
      
    if (!error) {
      setFlags(flags.map(f => f.flag_key === flag.flag_key ? { ...f, is_enabled: newVal } : f));
    }
  };

  const toggleSettingEnabled = async (setting: AppSetting) => {
    const newVal = !setting.is_enabled;
    const { error } = await supabase
      .from('sk8lytz_app_settings')
      .update({ is_enabled: newVal } as any)
      .eq('setting_key', setting.setting_key);
      
    if (!error) {
      setSettings(settings.map(s => s.setting_key === setting.setting_key ? { ...s, is_enabled: newVal } : s));
    }
  };

  const updateSettingValue = async (key: string, value: string) => {
    const { error } = await supabase
      .from('sk8lytz_app_settings')
      .update({ setting_value: value } as any)
      .eq('setting_key', key);
      
    if (!error) {
      setSettings(settings.map(s => s.setting_key === key ? { ...s, setting_value: value } : s));
    }
  };

  const formatKeyName = (key: string) => {
    return key.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase());
  };

  // Utility to clean up the description text
  const cleanDescription = (desc: string) => {
    if (!desc) return '';
    let cleaned = desc.replace(/[\s,\(]*visible_all,\s*online_only,\s*hidden_all[\)]*/gi, '');
    return cleaned.trim().replace(/^,|,$/g, '').trim();
  };

  if (loading) {
    return <div className="p-8 text-white text-lg">Loading Settings...</div>;
  }

  // Hardcode inline styles to absolutely guarantee rendering without Tailwind interference
  const tdStyle = { padding: '24px 16px', borderBottom: '1px solid #334155', verticalAlign: 'middle' as const };
  const thStyle = { padding: '16px', borderBottom: '2px solid #475569', backgroundColor: '#1e293b', color: '#94a3b8', textTransform: 'uppercase' as const, fontSize: '12px' };

  return (
    <div className="flex flex-col gap-10 pb-10">
      
      {/* FEATURE FLAGS */}
      <section>
        <h2 className="text-xl font-bold text-white mb-4">Feature Flags</h2>
        <div className="bg-slate-900 border border-slate-700 rounded-lg overflow-x-auto">
          <table className="w-full text-left" style={{ borderCollapse: 'collapse' }}>
            <thead>
              <tr>
                <th style={{ ...thStyle, width: '80px', textAlign: 'center' }}>Enable</th>
                <th style={{ ...thStyle, width: '250px' }}>Name</th>
                <th style={thStyle}>Description</th>
                <th style={{ ...thStyle, width: '250px', textAlign: 'right' }}>Value</th>
              </tr>
            </thead>
            <tbody>
              {flags.map(flag => (
                <tr key={flag.flag_key} className="hover:bg-slate-800/40 transition-colors">
                  <td style={{ ...tdStyle, textAlign: 'center' }}>
                    <div className="flex justify-center w-full">
                      <ModernToggle checked={flag.is_enabled} onChange={() => toggleFlag(flag)} />
                    </div>
                  </td>
                  <td style={tdStyle} className="font-semibold text-white whitespace-nowrap">
                    {formatKeyName(flag.flag_key)}
                  </td>
                  <td style={tdStyle} className="text-sm text-slate-400">
                    {flag.description || 'System flag'}
                  </td>
                  <td style={tdStyle}>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>

      {/* GLOBAL SETTINGS */}
      <section>
        <h2 className="text-xl font-bold text-white mb-4">App Settings</h2>
        <div className="bg-slate-900 border border-slate-700 rounded-lg overflow-x-auto">
          <table className="w-full text-left min-w-[800px]" style={{ borderCollapse: 'collapse' }}>
            <thead>
              <tr>
                <th style={{ ...thStyle, width: '80px', textAlign: 'center' }}>Enable</th>
                <th style={{ ...thStyle, width: '250px' }}>Name</th>
                <th style={thStyle}>Description</th>
                <th style={{ ...thStyle, width: '300px', textAlign: 'right' }}>Settings</th>
              </tr>
            </thead>
            <tbody>
              {settings.map(setting => {
                const isVisibility = setting.setting_key.startsWith('visibility_');
                const val = typeof setting.setting_value === 'string' ? setting.setting_value.replace(/"/g, '') : setting.setting_value;
                const title = formatKeyName(setting.setting_key.replace('visibility_', '').replace('global_', ''));

                return (
                  <tr key={setting.setting_key} className={`hover:bg-slate-800/40 transition-opacity ${setting.is_enabled ? 'opacity-100' : 'opacity-40'}`}>
                    {/* Col 1: Enable/Disable */}
                    <td style={{ ...tdStyle, textAlign: 'center' }}>
                      <div className="flex justify-center w-full">
                        <ModernToggle checked={setting.is_enabled} onChange={() => toggleSettingEnabled(setting)} />
                      </div>
                    </td>
                    
                    {/* Col 2: Name */}
                    <td style={tdStyle} className={`font-semibold whitespace-nowrap ${setting.is_enabled ? 'text-white' : 'text-slate-500 line-through'}`}>
                      {title}
                    </td>

                    {/* Col 3: Description */}
                    <td style={tdStyle} className="text-sm text-slate-400">
                      {cleanDescription(setting.description)}
                    </td>

                    {/* Col 4: Value / Toggles */}
                    <td style={{ ...tdStyle, textAlign: 'right' }}>
                      {isVisibility ? (
                        <div style={{ display: 'inline-flex', backgroundColor: '#020617', padding: '4px', border: '1px solid #334155', borderRadius: '6px', justifyContent: 'flex-end', gap: '4px' }}>
                          {(['visible_all', 'online_only', 'hidden_all'] as const).map(option => {
                            const isActive = val === option;
                            
                            let bgColor = 'transparent';
                            let textColor = '#94a3b8'; // slate-400
                            let borderLine = '1px solid transparent';
                            let shadowStr = 'none';

                            if (isActive) {
                              if (option === 'visible_all') {
                                bgColor = 'rgba(16, 185, 129, 0.15)';
                                textColor = '#34d399'; // emerald-400
                                borderLine = '1px solid rgba(16, 185, 129, 0.5)';
                                shadowStr = '0 0 10px rgba(16, 185, 129, 0.2)';
                              } else if (option === 'hidden_all') {
                                bgColor = 'rgba(244, 63, 94, 0.15)';
                                textColor = '#fb7185'; // rose-400
                                borderLine = '1px solid rgba(244, 63, 94, 0.5)';
                                shadowStr = '0 0 10px rgba(244, 63, 94, 0.2)';
                              } else {
                                bgColor = 'rgba(245, 158, 11, 0.15)';
                                textColor = '#fbbf24'; // amber-400
                                borderLine = '1px solid rgba(245, 158, 11, 0.5)';
                                shadowStr = '0 0 10px rgba(245, 158, 11, 0.2)';
                              }
                            }

                            return (
                              <button
                                key={option}
                                onClick={() => updateSettingValue(setting.setting_key, option)}
                                disabled={!setting.is_enabled}
                                style={{
                                  padding: '8px 12px',
                                  fontSize: '12px',
                                  fontWeight: '600',
                                  textTransform: 'uppercase',
                                  borderRadius: '4px',
                                  cursor: setting.is_enabled ? 'pointer' : 'not-allowed',
                                  backgroundColor: bgColor,
                                  color: textColor,
                                  border: borderLine,
                                  boxShadow: shadowStr,
                                  transition: 'all 0.2s ease',
                                  opacity: setting.is_enabled ? 1 : 0.5
                                }}
                              >
                                {option.replace('_', ' ')}
                              </button>
                            );
                          })}
                        </div>
                      ) : (val === 'true' || val === 'false' || setting.setting_key.includes('_enabled')) ? (
                        <div style={{ display: 'flex', justifyContent: 'flex-end', width: '100%' }}>
                          <div 
                            onClick={!setting.is_enabled ? undefined : () => updateSettingValue(setting.setting_key, val === 'true' ? 'false' : 'true')}
                            style={{
                              width: '50px',
                              height: '26px',
                              backgroundColor: val === 'true' ? '#0ea5e9' : '#334155', // sky-500
                              borderRadius: '24px',
                              cursor: setting.is_enabled ? 'pointer' : 'not-allowed',
                              position: 'relative',
                              transition: 'background-color 0.3s ease',
                              display: 'inline-flex',
                              alignItems: 'center',
                              padding: '2px',
                              boxShadow: (val === 'true' && setting.is_enabled) ? '0 0 10px rgba(14, 165, 233, 0.4)' : 'inset 0 2px 4px rgba(0,0,0,0.3)',
                              boxSizing: 'border-box',
                              opacity: setting.is_enabled ? 1 : 0.4
                            }}
                          >
                            <div 
                              style={{
                                width: '22px',
                                height: '22px',
                                backgroundColor: '#ffffff',
                                borderRadius: '50%',
                                transform: val === 'true' ? 'translateX(24px)' : 'translateX(0)',
                                transition: 'transform 0.3s cubic-bezier(0.4, 0.0, 0.2, 1)',
                                boxShadow: '0 2px 5px rgba(0,0,0,0.3)'
                              }}
                            />
                          </div>
                        </div>
                      ) : (
                        <input 
                          type="text" 
                          disabled={!setting.is_enabled}
                          style={{
                            backgroundColor: '#020617', // slate-950
                            border: '1px solid #334155', // slate-700
                            borderRadius: '4px',
                            padding: '8px 12px',
                            color: '#ffffff',
                            width: '100%',
                            maxWidth: '120px',
                            fontSize: '14px',
                            outline: 'none',
                            float: 'right',
                            opacity: setting.is_enabled ? 1 : 0.5,
                            textAlign: 'right'
                          }}
                          defaultValue={val}
                          onBlur={(e) => {
                            if (e.target.value !== val) {
                              updateSettingValue(setting.setting_key, e.target.value);
                            }
                          }}
                        />
                      )}
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </section>
    </div>
  );
}
