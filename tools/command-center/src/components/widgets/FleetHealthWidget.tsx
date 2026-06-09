import { useEffect, useState } from 'react';
import { supabase } from '../../services/supabase';
import { PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer } from 'recharts';

interface DeviceData {
  firmware_ver: string | null;
  ble_version: string | null;
  product_type: string | null;
}

const COLORS = ['#22d3ee', '#3b82f6', '#6366f1', '#a855f7', '#ec4899', '#f43f5e'];

export default function FleetHealthWidget() {
  const [data, setData] = useState<DeviceData[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDeviceData();
  }, []);

  const fetchDeviceData = async () => {
    setLoading(true);
    // In reality, this would group by on the server, but for simplicity we fetch and aggregate
    const { data, error } = await supabase
      .from('discovered_devices_telemetry')
      .select('firmware_ver, ble_version, product_type');
      
    if (data && !error) {
      setData(data as DeviceData[]);
    }
    setLoading(false);
  };

  const aggregateData = (key: keyof DeviceData) => {
    const counts = data.reduce((acc, curr) => {
      const val = curr[key] || 'Unknown';
      acc[val] = (acc[val] || 0) + 1;
      return acc;
    }, {} as Record<string, number>);

    return Object.keys(counts).map(name => ({
      name,
      value: counts[name]
    })).sort((a, b) => b.value - a.value); // sort descending
  };

  const firmwareData = aggregateData('firmware_ver');
  const bleData = aggregateData('ble_version');
  const productData = aggregateData('product_type');

  if (loading) {
    return <div className="text-cyan-400">Loading Fleet Telemetry...</div>;
  }

  const renderChart = (title: string, chartData: any[]) => (
    <div className="glass-panel p-4 rounded-xl border border-slate-800 flex flex-col items-center relative">
      <h3 className="text-lg font-semibold text-white mb-4">{title}</h3>
      <div className="w-full h-64">
        {chartData.length > 0 ? (
          <ResponsiveContainer width="100%" height="100%">
            <PieChart>
              <Pie
                data={chartData}
                innerRadius={60}
                outerRadius={80}
                paddingAngle={5}
                dataKey="value"
              >
                {chartData.map((_, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip 
                contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155', color: '#fff', borderRadius: '8px' }}
                itemStyle={{ color: '#22d3ee' }}
              />
              <Legend wrapperStyle={{ color: '#94a3b8' }} />
            </PieChart>
          </ResponsiveContainer>
        ) : (
          <div className="absolute inset-0 flex items-center justify-center flex-col text-slate-500 mt-8">
            <span className="text-2xl mb-2">📊</span>
            <span>Awaiting Scan Data</span>
          </div>
        )}
      </div>
    </div>
  );

  return (
    <div className="flex flex-col gap-6">
      <div className="flex justify-between items-center mb-2">
        <h2 className="text-2xl font-bold text-white">Device Fleet Health & Fragmentation</h2>
        <div className="glass-panel px-4 py-2 rounded text-cyan-400 font-medium">
          {data.length} Devices Scanned
        </div>
      </div>
      
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {renderChart('Firmware Versions', firmwareData)}
        {renderChart('BLE Chipsets', bleData)}
        {renderChart('Product Types', productData)}
      </div>
    </div>
  );
}
