import { useEffect, useState } from 'react';
import { supabase } from '../../services/supabase';
import { Edit2, Trash2, Plus, X, Save, ToggleLeft, ToggleRight, List } from 'lucide-react';

export interface Sk8LytzPick {
  id: string;
  sort_order: number;
  name: string;
  custom_name: string | null;
  mode: string;
  color: string;
  pattern_id: number;
  speed: number;
  brightness: number;
  fixed_color_mode: string | null;
  fixed_fg_color: string | null;
  fixed_bg_color: string | null;
  fixed_hue: number | null;
  multi_colors: string[] | null;
  multi_transition: number | null;
  multi_length: number | null;
  music_primary_color: string | null;
  music_secondary_color: string | null;
  mic_sensitivity: number | null;
  mic_source: string | null;
  music_matrix_style: number | null;
  is_active: boolean;
}

const DEFAULT_PICK: Partial<Sk8LytzPick> = {
  sort_order: 100,
  name: 'New Pick',
  mode: 'FIXED',
  color: '#FFFFFF',
  pattern_id: 1,
  speed: 80,
  brightness: 100,
  is_active: true,
};

export default function PicksManagerWidget() {
  const [picks, setPicks] = useState<Sk8LytzPick[]>([]);
  const [loading, setLoading] = useState(true);
  const [editingPick, setEditingPick] = useState<Partial<Sk8LytzPick> | null>(null);

  useEffect(() => {
    fetchPicks();
  }, []);

  const fetchPicks = async () => {
    setLoading(true);
    const { data } = await supabase
      .from('sk8lytz_picks')
      .select('*')
      .order('sort_order', { ascending: true });
    if (data) setPicks(data as Sk8LytzPick[]);
    setLoading(false);
  };

  const deletePick = async (id: string) => {
    if (!confirm('Are you sure you want to delete this preset?')) return;
    const { error } = await supabase.from('sk8lytz_picks').delete().eq('id', id);
    if (!error) setPicks(picks.filter(p => p.id !== id));
  };

  const toggleActive = async (pick: Sk8LytzPick) => {
    const { error } = await supabase
      .from('sk8lytz_picks')
      .update({ is_active: !pick.is_active })
      .eq('id', pick.id);
    if (!error) {
      setPicks(picks.map(p => p.id === pick.id ? { ...p, is_active: !pick.is_active } : p));
    }
  };

  const savePick = async (pickToSave: Partial<Sk8LytzPick>) => {
    if (pickToSave.id) {
      // Update
      const { data, error } = await supabase
        .from('sk8lytz_picks')
        .update(pickToSave)
        .eq('id', pickToSave.id)
        .select()
        .single();
      if (!error && data) {
        setPicks(picks.map(p => p.id === data.id ? (data as Sk8LytzPick) : p));
        setEditingPick(null);
      } else {
        alert('Failed to save: ' + error?.message);
      }
    } else {
      // Insert
      const { data, error } = await supabase
        .from('sk8lytz_picks')
        .insert([pickToSave as unknown as Sk8LytzPick])
        .select()
        .single();
      if (!error && data) {
        setPicks([...picks, data as Sk8LytzPick].sort((a,b) => a.sort_order - b.sort_order));
        setEditingPick(null);
      } else {
        alert('Failed to create: ' + error?.message);
      }
    }
  };

  if (loading) return <div className="text-cyan-400">Loading Picks...</div>;

  return (
    <div className="flex flex-col gap-6 relative">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-white flex items-center gap-2">
          <List size={24} className="text-cyan-400" />
          SK8Lytz Picks Manager
        </h2>
        <button 
          onClick={() => setEditingPick(DEFAULT_PICK)}
          className="glass-btn px-4 py-2 rounded text-white flex items-center gap-2"
        >
          <Plus size={18} /> New Pick
        </button>
      </div>

      <div className="glass-panel rounded-xl border border-slate-800 overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse whitespace-nowrap">
            <thead>
              <tr className="bg-slate-900/50 border-b border-slate-800">
                <th className="p-4 text-slate-300 font-medium w-16">Active</th>
                <th className="p-4 text-slate-300 font-medium w-16">Order</th>
                <th className="p-4 text-slate-300 font-medium">Name</th>
                <th className="p-4 text-slate-300 font-medium">Mode</th>
                <th className="p-4 text-slate-300 font-medium">Color</th>
                <th className="p-4 text-slate-300 font-medium">Spd/Brt</th>
                <th className="p-4 text-right">Actions</th>
              </tr>
            </thead>
            <tbody>
              {picks.length === 0 ? (
                <tr><td colSpan={7} className="p-4 text-slate-500 text-center">No picks available.</td></tr>
              ) : picks.map((pick, i) => (
                <tr key={pick.id} className={`border-b border-slate-800/50 ${i % 2 === 0 ? 'bg-slate-900/20' : ''}`}>
                  <td className="p-4">
                    <button 
                      onClick={() => toggleActive(pick)}
                      className={`transition-colors ${pick.is_active ? 'text-cyan-400' : 'text-slate-500'}`}
                    >
                      {pick.is_active ? <ToggleRight size={24} /> : <ToggleLeft size={24} />}
                    </button>
                  </td>
                  <td className="p-4 font-mono text-slate-400">{pick.sort_order}</td>
                  <td className="p-4">
                    <div className="font-semibold text-white">{pick.name}</div>
                    {pick.custom_name && <div className="text-xs text-slate-400">"{pick.custom_name}"</div>}
                  </td>
                  <td className="p-4 text-slate-300">{pick.mode}</td>
                  <td className="p-4">
                    <div className="flex items-center gap-2">
                      <div className="w-4 h-4 rounded-full border border-slate-600" style={{ backgroundColor: pick.color || 'transparent' }}></div>
                      <span className="text-sm font-mono text-slate-400">{pick.color}</span>
                    </div>
                  </td>
                  <td className="p-4 text-slate-400 text-sm">{pick.speed} / {pick.brightness}</td>
                  <td className="p-4 text-right flex justify-end gap-2">
                    <button onClick={() => setEditingPick(pick)} className="p-2 text-slate-400 hover:text-cyan-400 transition-colors">
                      <Edit2 size={18} />
                    </button>
                    <button onClick={() => deletePick(pick.id)} className="p-2 text-slate-500 hover:text-red-400 transition-colors">
                      <Trash2 size={18} />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {editingPick && (
        <EditorModal 
          pick={editingPick} 
          onClose={() => setEditingPick(null)} 
          onSave={savePick} 
        />
      )}
    </div>
  );
}

function EditorModal({ pick, onClose, onSave }: { pick: Partial<Sk8LytzPick>, onClose: () => void, onSave: (p: Partial<Sk8LytzPick>) => void }) {
  const [formData, setFormData] = useState<Partial<Sk8LytzPick>>(pick);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value, type } = e.target;
    if (type === 'number') {
      setFormData({ ...formData, [name]: value === '' ? null : Number(value) });
    } else if (type === 'checkbox') {
      const checked = (e.target as HTMLInputElement).checked;
      setFormData({ ...formData, [name]: checked });
    } else {
      setFormData({ ...formData, [name]: value === '' ? null : value });
    }
  };

  const handleArrayChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    // expect comma separated hex codes
    const arr = value.split(',').map(s => s.trim()).filter(Boolean);
    setFormData({ ...formData, [name]: arr.length > 0 ? arr : null });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSave(formData);
  };

  return (
    <div className="fixed inset-0 bg-black/80 backdrop-blur-sm z-50 flex items-center justify-center p-4 overflow-y-auto">
      <div className="bg-[#0f172a] border border-slate-700 rounded-2xl w-full max-w-4xl shadow-2xl relative my-8">
        <button onClick={onClose} className="absolute top-4 right-4 text-slate-400 hover:text-white">
          <X size={24} />
        </button>
        
        <div className="p-6 border-b border-slate-800">
          <h3 className="text-xl font-bold text-white">{formData.id ? 'Edit Preset' : 'New Preset'}</h3>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="p-6 grid grid-cols-1 md:grid-cols-2 gap-6 max-h-[70vh] overflow-y-auto custom-scrollbar">
            {/* Core Fields */}
            <div className="col-span-full mb-2">
              <h4 className="text-cyan-400 font-semibold border-b border-slate-800 pb-2">Core Configuration</h4>
            </div>
            
            <div>
              <label className="block text-sm text-slate-400 mb-1">Internal Name</label>
              <input required type="text" name="name" value={formData.name || ''} onChange={handleChange} className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full" />
            </div>
            <div>
              <label className="block text-sm text-slate-400 mb-1">Custom Display Name</label>
              <input type="text" name="custom_name" value={formData.custom_name || ''} onChange={handleChange} className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full" placeholder="Optional" />
            </div>
            
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm text-slate-400 mb-1">Sort Order</label>
                <input required type="number" name="sort_order" value={formData.sort_order ?? 100} onChange={handleChange} className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full" />
              </div>
              <div>
                <label className="block text-sm text-slate-400 mb-1">Pattern ID (0-255)</label>
                <input required type="number" name="pattern_id" value={formData.pattern_id ?? 1} onChange={handleChange} className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full" />
              </div>
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm text-slate-400 mb-1">Base Mode</label>
                <select name="mode" value={formData.mode || 'FIXED'} onChange={handleChange} className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full">
                  <option value="FIXED">FIXED</option>
                  <option value="GENERATIVE">GENERATIVE</option>
                  <option value="MULTIMODE">MULTIMODE</option>
                  <option value="MUSIC">MUSIC</option>
                  <option value="CUSTOM">CUSTOM</option>
                </select>
              </div>
              <div>
                <label className="block text-sm text-slate-400 mb-1">Base Color (Hex)</label>
                <div className="flex gap-2">
                  <input type="color" value={formData.color || '#000000'} onChange={(e) => setFormData({...formData, color: e.target.value.toUpperCase()})} className="h-10 w-12 bg-transparent rounded cursor-pointer" />
                  <input type="text" name="color" value={formData.color || ''} onChange={handleChange} className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full font-mono uppercase" placeholder="#FFFFFF" />
                </div>
              </div>
            </div>

            <div className="grid grid-cols-2 gap-4 col-span-1 md:col-span-2">
              <div>
                <label className="block text-sm text-slate-400 mb-1">Speed (0-100)</label>
                <input type="range" name="speed" min="0" max="100" value={formData.speed ?? 80} onChange={handleChange} className="w-full accent-cyan-400" />
                <div className="text-right text-xs text-slate-400">{formData.speed}</div>
              </div>
              <div>
                <label className="block text-sm text-slate-400 mb-1">Brightness (0-100)</label>
                <input type="range" name="brightness" min="0" max="100" value={formData.brightness ?? 100} onChange={handleChange} className="w-full accent-cyan-400" />
                <div className="text-right text-xs text-slate-400">{formData.brightness}</div>
              </div>
            </div>

            {/* Fixed Mode Overrides */}
            <div className="col-span-full mt-4 mb-2">
              <h4 className="text-purple-400 font-semibold border-b border-slate-800 pb-2">Fixed / Generative Specifics</h4>
            </div>
            <div>
              <label className="block text-sm text-slate-400 mb-1">Fixed Color Mode</label>
              <select name="fixed_color_mode" value={formData.fixed_color_mode || ''} onChange={handleChange} className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full">
                <option value="">-- None --</option>
                <option value="SOLID">SOLID</option>
                <option value="GENERATIVE">GENERATIVE</option>
              </select>
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm text-slate-400 mb-1">Foreground Color</label>
                <input type="text" name="fixed_fg_color" value={formData.fixed_fg_color || ''} onChange={handleChange} className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full font-mono" placeholder="#FF0000" />
              </div>
              <div>
                <label className="block text-sm text-slate-400 mb-1">Background Color</label>
                <input type="text" name="fixed_bg_color" value={formData.fixed_bg_color || ''} onChange={handleChange} className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full font-mono" placeholder="#000000" />
              </div>
            </div>

            {/* Multimode Fields */}
            <div className="col-span-full mt-4 mb-2">
              <h4 className="text-pink-400 font-semibold border-b border-slate-800 pb-2">Multimode Sequence</h4>
            </div>
            <div className="col-span-full">
              <label className="block text-sm text-slate-400 mb-1">Multi Colors (Comma separated hex codes)</label>
              <input type="text" name="multi_colors" value={(formData.multi_colors || []).join(', ')} onChange={handleArrayChange} className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full font-mono" placeholder="#FF0000, #00FF00, #0000FF" />
            </div>
            <div>
              <label className="block text-sm text-slate-400 mb-1">Multi Transition Type</label>
              <input type="number" name="multi_transition" value={formData.multi_transition ?? ''} onChange={handleChange} className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full" placeholder="e.g. 1" />
            </div>
            <div>
              <label className="block text-sm text-slate-400 mb-1">Multi Segment Length</label>
              <input type="number" name="multi_length" value={formData.multi_length ?? ''} onChange={handleChange} className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full" placeholder="e.g. 5" />
            </div>

            {/* Music Mode Fields */}
            <div className="col-span-full mt-4 mb-2">
              <h4 className="text-green-400 font-semibold border-b border-slate-800 pb-2">Music Sync Specifics</h4>
            </div>
            <div>
              <label className="block text-sm text-slate-400 mb-1">Matrix Style (0-255)</label>
              <input type="number" name="music_matrix_style" value={formData.music_matrix_style ?? ''} onChange={handleChange} className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full" />
            </div>
            <div>
              <label className="block text-sm text-slate-400 mb-1">Mic Source</label>
              <select name="mic_source" value={formData.mic_source || ''} onChange={handleChange} className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full">
                <option value="">-- None --</option>
                <option value="INTERNAL">INTERNAL</option>
                <option value="EXTERNAL">EXTERNAL</option>
                <option value="PHONE">PHONE</option>
              </select>
            </div>
            <div>
              <label className="block text-sm text-slate-400 mb-1">Primary Music Color</label>
              <input type="text" name="music_primary_color" value={formData.music_primary_color || ''} onChange={handleChange} className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full font-mono" />
            </div>
            <div>
              <label className="block text-sm text-slate-400 mb-1">Secondary Music Color</label>
              <input type="text" name="music_secondary_color" value={formData.music_secondary_color || ''} onChange={handleChange} className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full font-mono" />
            </div>

            {/* Status */}
            <div className="col-span-full mt-4 mb-2">
              <h4 className="text-slate-200 font-semibold border-b border-slate-800 pb-2">Status</h4>
            </div>
            <div className="col-span-full flex items-center gap-3 bg-slate-900/50 p-4 rounded-lg border border-slate-800">
              <input 
                type="checkbox" 
                id="is_active" 
                name="is_active" 
                checked={formData.is_active || false} 
                onChange={handleChange} 
                className="w-5 h-5 accent-cyan-400"
              />
              <label htmlFor="is_active" className="text-white font-medium cursor-pointer">
                Preset is Active (Visible to users)
              </label>
            </div>
            
          </div>
          <div className="p-4 border-t border-slate-800 bg-slate-900/50 flex justify-end gap-3 rounded-b-2xl">
            <button type="button" onClick={onClose} className="px-4 py-2 rounded text-slate-400 hover:text-white transition-colors">
              Cancel
            </button>
            <button type="submit" className="glass-btn px-6 py-2 rounded text-white flex items-center gap-2">
              <Save size={18} /> Save Preset
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
