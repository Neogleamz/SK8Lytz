import React, { useEffect, useState } from 'react';
import { supabase } from '../../services/supabase';
import { Edit2, Plus, X, Save, Box } from 'lucide-react';

export type ProductCatalogRow = {
  id: string;
  display_name: string;
  default_led_points: number;
  default_segments: number;
  default_ic_type: number;
  default_color_sorting: number;
  hardware_allows_custom_points: boolean;
  detect_min_points: number;
  detect_max_points: number;
  viz_shape: string;
  viz_default_points: number;
  viz_blob_diameter_mm: number;
  viz_base_width: number;
  viz_base_height: number;
  viz_strip_count?: number | null;
  viz_strip_separation?: number | null;
  viz_strip_orientation?: string | null;
  viz_is_mirrored?: boolean | null;
  battery_capacity_milli_ampere_hour?: number | null;
  viz_theme_color?: string | null;
  brand_icon?: string | null;
  is_active: boolean;
};

const DEFAULT_PROFILE: Partial<ProductCatalogRow> = {
  id: '',
  display_name: '',
  default_led_points: 16,
  default_segments: 1,
  default_ic_type: 1,
  default_color_sorting: 2,
  hardware_allows_custom_points: false,
  detect_min_points: 1,
  detect_max_points: 99,
  viz_shape: 'OVAL',
  viz_default_points: 16,
  viz_blob_diameter_mm: 5.7,
  viz_base_width: 55,
  viz_base_height: 115,
  viz_strip_count: 2,
  viz_strip_separation: 32,
  viz_strip_orientation: 'VERTICAL',
  viz_is_mirrored: false,
  battery_capacity_milli_ampere_hour: 3000,
  viz_theme_color: '#FF5A00',
  brand_icon: 'circle-double',
  is_active: true,
};

export default function ProductCatalogWidget() {
  const [profiles, setProfiles] = useState<ProductCatalogRow[]>([]);
  const [loading, setLoading] = useState(true);
  const [editingProfile, setEditingProfile] = useState<Partial<ProductCatalogRow> | null>(null);

  useEffect(() => {
    fetchProfiles();
  }, []);

  const fetchProfiles = async () => {
    setLoading(true);
    const { data } = await supabase
      .from('product_catalog')
      .select('*')
      .order('id', { ascending: true });
    if (data) setProfiles(data as unknown as ProductCatalogRow[]);
    setLoading(false);
  };

  const saveProfile = async (profileToSave: Partial<ProductCatalogRow>) => {
    if (!profileToSave.id?.trim()) {
      alert('Product ID is required.');
      return;
    }

    const isExisting = profiles.some(p => p.id === profileToSave.id);

    const dbPayload = {
      id: profileToSave.id,
      display_name: profileToSave.display_name,
      default_led_points: profileToSave.default_led_points,
      default_segments: profileToSave.default_segments,
      default_ic_type: profileToSave.default_ic_type,
      default_color_sorting: profileToSave.default_color_sorting,
      detect_min_points: profileToSave.detect_min_points,
      detect_max_points: profileToSave.detect_max_points,
      viz_shape: profileToSave.viz_shape,
      viz_default_points: profileToSave.viz_default_points,
      viz_blob_diameter_mm: profileToSave.viz_blob_diameter_mm,
      viz_base_width: profileToSave.viz_base_width,
      viz_base_height: profileToSave.viz_base_height,
      viz_strip_count: profileToSave.viz_strip_count ?? null,
      viz_strip_separation: profileToSave.viz_strip_separation ?? null,
      viz_strip_orientation: profileToSave.viz_strip_orientation ?? null,
      is_active: profileToSave.is_active ?? true
    };

    if (isExisting) {
      const { data, error } = await supabase
        .from('product_catalog')
        .update(dbPayload as any)
        .eq('id', profileToSave.id)
        .select()
        .single();
      if (!error && data) {
        setProfiles(profiles.map(p => p.id === data.id ? { ...profileToSave, ...data } as unknown as ProductCatalogRow : p));
        setEditingProfile(null);
      } else {
        alert('Failed to save: ' + error?.message);
      }
    } else {
      const { data, error } = await supabase
        .from('product_catalog')
        .insert([dbPayload as any])
        .select()
        .single();
      if (!error && data) {
        setProfiles([...profiles, { ...profileToSave, ...data } as unknown as ProductCatalogRow]);
        setEditingProfile(null);
      } else {
        alert('Failed to create: ' + error?.message);
      }
    }
  };

  if (loading) return <div className="text-[#9D4EFF]">Loading Catalog...</div>;

  return (
    <div className="flex flex-col gap-6 relative p-6 max-w-7xl mx-auto w-full">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-white flex items-center gap-2">
          <Box size={24} className="text-[#9D4EFF]" />
          Product Catalog Manager
        </h2>
        <button 
          onClick={() => setEditingProfile(DEFAULT_PROFILE)}
          className="bg-[#9D4EFF]/20 border border-[#9D4EFF] hover:bg-[#9D4EFF]/30 transition-colors px-4 py-2 rounded text-[#9D4EFF] flex items-center gap-2 font-semibold"
        >
          <Plus size={18} /> Add New Profile
        </button>
      </div>

      <div className="glass-panel rounded-xl border border-slate-800 overflow-hidden bg-slate-900/40">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse whitespace-nowrap">
            <thead>
              <tr className="bg-slate-900/80 border-b border-slate-800">
                <th className="p-4 text-slate-300 font-medium">Product ID</th>
                <th className="p-4 text-slate-300 font-medium">Display Name</th>
                <th className="p-4 text-slate-300 font-medium">Detect Range</th>
                <th className="p-4 text-slate-300 font-medium">Shape</th>
                <th className="p-4 text-slate-300 font-medium text-right">Actions</th>
              </tr>
            </thead>
            <tbody>
              {profiles.length === 0 ? (
                <tr><td colSpan={5} className="p-4 text-slate-500 text-center">No hardware profiles found.</td></tr>
              ) : profiles.map((p, i) => (
                <tr key={p.id} className={`border-b border-slate-800/50 ${i % 2 === 0 ? 'bg-slate-900/20' : ''}`}>
                  <td className="p-4 font-mono text-[#9D4EFF] font-semibold">{p.id}</td>
                  <td className="p-4">
                    <div className="flex items-center gap-2 text-white">
                      {p.display_name}
                    </div>
                  </td>
                  <td className="p-4 text-slate-400">{p.detect_min_points} - {p.detect_max_points} LEDs</td>
                  <td className="p-4 text-slate-400">{p.viz_shape}</td>
                  <td className="p-4 text-right">
                    <button onClick={() => setEditingProfile(p)} className="p-2 text-slate-400 hover:text-[#9D4EFF] transition-colors">
                      <Edit2 size={18} />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {editingProfile && (
        <EditorModal 
          profile={editingProfile} 
          isNew={!profiles.some(p => p.id === editingProfile.id)}
          onClose={() => setEditingProfile(null)} 
          onSave={saveProfile} 
        />
      )}
    </div>
  );
}

function EditorModal({ profile, isNew, onClose, onSave }: { profile: Partial<ProductCatalogRow>, isNew: boolean, onClose: () => void, onSave: (p: Partial<ProductCatalogRow>) => void }) {
  const [formData, setFormData] = useState<Partial<ProductCatalogRow>>(profile);
  const [validationError, setValidationError] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value, type } = e.target;
    setValidationError('');
    if (type === 'number') {
      setFormData({ ...formData, [name]: value === '' ? null : Number(value) });
    } else if (type === 'checkbox') {
      const checked = (e.target as HTMLInputElement).checked;
      setFormData({ ...formData, [name]: checked });
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSave(formData);
  };

  const renderField = (label: string, name: keyof ProductCatalogRow, type: 'text' | 'number' = 'text', placeholder?: string, isRequired: boolean = false) => (
    <div>
      <label className="block text-xs font-bold text-slate-400 uppercase tracking-wider mb-1">{label}</label>
      <input 
        required={isRequired} 
        type={type} 
        name={name} 
        value={(formData[name] as string | number) ?? ''} 
        onChange={handleChange} 
        className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full font-semibold focus:border-[#9D4EFF] outline-none transition-colors" 
        placeholder={placeholder}
      />
    </div>
  );

  const [uploadingImage, setUploadingImage] = useState(false);
  const handleImageUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    setUploadingImage(true);
    setValidationError('');
    try {
      const ext = file.name.split('.').pop();
      const fileName = `product-icon-${Date.now()}.${ext}`;
      const filePath = `product-icons/${fileName}`;

      const { error: uploadError } = await supabase.storage
        .from('avatars')
        .upload(filePath, file);

      if (uploadError) {
        throw uploadError;
      }

      const { data: { publicUrl } } = supabase.storage.from('avatars').getPublicUrl(filePath);
      
      setFormData(prev => ({ ...prev, brand_icon: publicUrl }));
    } catch (err: any) {
      setValidationError('Failed to upload image: ' + err.message);
    } finally {
      setUploadingImage(false);
    }
  };

  const renderIconField = () => (
    <div>
      <label className="block text-xs font-bold text-slate-400 uppercase tracking-wider mb-1">Brand Icon</label>
      <div className="flex gap-2">
        <input 
          type="text" 
          name="brand_icon" 
          value={(formData.brand_icon as string) ?? ''} 
          onChange={handleChange} 
          className="bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white flex-1 font-semibold focus:border-[#9D4EFF] outline-none transition-colors" 
          placeholder="Icon name or URL"
        />
        <label className={`bg-slate-800 hover:bg-slate-700 border border-slate-700 rounded px-4 py-2 cursor-pointer transition-colors flex items-center justify-center ${uploadingImage ? 'opacity-50 pointer-events-none' : ''}`}>
          <span className="text-sm font-bold text-slate-300">{uploadingImage ? '...' : 'Upload'}</span>
          <input type="file" accept="image/*" className="hidden" onChange={handleImageUpload} />
        </label>
      </div>
      {formData.brand_icon && formData.brand_icon.startsWith('http') && (
        <div className="mt-2 p-2 bg-slate-900/50 rounded border border-slate-800 flex items-center gap-3">
          <img src={formData.brand_icon} alt="Preview" className="w-8 h-8 object-contain" />
          <span className="text-xs text-slate-400 truncate flex-1">{formData.brand_icon}</span>
        </div>
      )}
    </div>
  );

  const renderToggle = (label: string, name: keyof ProductCatalogRow, desc: string) => (
    <div className="flex items-center justify-between bg-slate-900/50 p-3 rounded border border-slate-800">
      <div>
        <label className="block text-xs font-bold text-slate-400 uppercase tracking-wider">{label}</label>
        <span className="text-xs text-slate-500">{desc}</span>
      </div>
      <input 
        type="checkbox" 
        name={name} 
        checked={(formData[name] as boolean) || false} 
        onChange={handleChange} 
        className="w-5 h-5 accent-[#9D4EFF] cursor-pointer"
      />
    </div>
  );

  return (
    <div className="fixed inset-0 bg-black/80 backdrop-blur-sm z-50 flex justify-center p-4 overflow-y-auto items-start pt-16 pb-16">
      <div className="bg-[#0f172a] border-l-4 border-[#9D4EFF] border-y border-r border-slate-700 rounded-xl w-full max-w-3xl shadow-2xl relative">
        <button type="button" onClick={onClose} className="absolute top-4 right-4 text-slate-400 hover:text-white z-10">
          <X size={24} />
        </button>
        
        <div className="p-6 border-b border-slate-800">
          <h3 className="text-xl font-bold text-white flex items-center gap-2">
            <Box size={20} className="text-[#9D4EFF]" />
            {isNew ? 'NEW HARDWARE PROFILE' : 'EDIT PROFILE'}
          </h3>
          <p className="text-xs text-slate-400 mt-1 uppercase tracking-widest">Edit hardware product catalog entries</p>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="p-6 grid grid-cols-1 md:grid-cols-2 gap-6">
            
            {validationError && (
              <div className="col-span-full bg-red-900/50 border border-red-500/50 text-red-200 p-3 rounded">
                {validationError}
              </div>
            )}

            {/* Core Settings */}
            <div className="col-span-full mb-[-10px]">
              <h4 className="text-slate-200 font-bold border-b border-slate-800 pb-2 text-sm tracking-widest uppercase">Core Identification</h4>
            </div>
            
            <div>
              <label className="block text-xs font-bold text-slate-400 uppercase tracking-wider mb-1">Product ID (Hardware Ref)</label>
              <input 
                required 
                type="text" 
                name="id" 
                value={formData.id || ''} 
                onChange={handleChange} 
                disabled={!isNew}
                className={`bg-slate-900 border border-slate-700 rounded px-3 py-2 text-white w-full font-semibold focus:border-[#9D4EFF] outline-none transition-colors ${!isNew ? 'opacity-50 cursor-not-allowed' : ''}`} 
                placeholder="e.g. SK8LYTZ_V2" 
              />
            </div>
            {renderField('Display Name', 'display_name', 'text', 'e.g. SK8Lytz Pro', true)}
            {renderIconField()}
            {renderField('Brand Color Hex', 'viz_theme_color', 'text', 'e.g. #FF5A00', false)}

            {/* Hardware Defaults */}
            <div className="col-span-full mt-4 mb-[-10px]">
              <h4 className="text-slate-200 font-bold border-b border-slate-800 pb-2 text-sm tracking-widest uppercase">Hardware Defaults</h4>
            </div>
            {renderField('Default LEDs', 'default_led_points', 'number', undefined, true)}
            {renderField('Virtual Segments', 'default_segments', 'number', undefined, true)}
            {renderField('IC Type (1=WS2812B)', 'default_ic_type', 'number', undefined, true)}
            {renderField('Color Sorting (2=GRB)', 'default_color_sorting', 'number', undefined, true)}
            {renderField('Battery Capacity (mAh)', 'battery_capacity_milli_ampere_hour', 'number', undefined, false)}
            
            <div className="col-span-full">
              {renderToggle('Customizable Profile', 'hardware_allows_custom_points', 'Allow user to cut and set custom length')}
            </div>

            {/* Autodetect Thresholds */}
            <div className="col-span-full mt-4 mb-[-10px]">
              <h4 className="text-slate-200 font-bold border-b border-slate-800 pb-2 text-sm tracking-widest uppercase">Autodetect Thresholds</h4>
            </div>
            {renderField('Min HW Points', 'detect_min_points', 'number', undefined, true)}
            {renderField('Max HW Points', 'detect_max_points', 'number', undefined, true)}

            {/* Visualizer Geometry */}
            <div className="col-span-full mt-4 mb-[-10px]">
              <h4 className="text-slate-200 font-bold border-b border-slate-800 pb-2 text-sm tracking-widest uppercase">Visualizer Geometry</h4>
            </div>
            {renderField('Canvas Shape', 'viz_shape', 'text', 'OVAL | RING | DUAL_STRIP', true)}
            {renderField('Visualizer Default Points', 'viz_default_points', 'number', undefined, true)}
            {renderField('Base Width', 'viz_base_width', 'number', undefined, true)}
            {renderField('Base Height', 'viz_base_height', 'number', undefined, true)}
            {renderField('Blob Diameter (mm)', 'viz_blob_diameter_mm', 'number', undefined, true)}
            {renderField('Strip Count (Railz)', 'viz_strip_count', 'number', undefined, false)}
            {renderField('Strip Separation (Railz)', 'viz_strip_separation', 'number', undefined, false)}
            {renderField('Strip Orientation', 'viz_strip_orientation', 'text', 'VERTICAL | HORIZONTAL', false)}

            <div className="col-span-full">
              {renderToggle('Mirrored Render', 'viz_is_mirrored', 'Mirror Seg2 over Seg1')}
            </div>

            {/* Status */}
            <div className="col-span-full mt-4">
              {renderToggle('Active Status', 'is_active', 'Profile is visible and syncs to mobile app')}
            </div>
            
          </div>
          <div className="p-4 border-t border-slate-800 bg-slate-900/50 flex justify-end gap-3 rounded-b-xl">
            <button type="button" onClick={onClose} className="px-4 py-2 rounded text-slate-400 hover:text-white font-semibold transition-colors">
              Cancel
            </button>
            <button type="submit" className="bg-[#9D4EFF] hover:bg-[#8637e6] transition-colors px-6 py-2 rounded text-white flex items-center gap-2 font-bold shadow-lg shadow-[#9D4EFF]/20">
              <Save size={18} /> {isNew ? 'SAVE TO CATALOG' : 'UPDATE PROFILE'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
