import React, { useState } from 'react';
import { Smartphone, Monitor, Tablet } from 'lucide-react';

const DEVICES = {
  'iphone-15': { name: 'iPhone 15 Pro', width: 393, height: 852, borderRadius: 48, padding: 16 },
  'pixel-8': { name: 'Pixel 8', width: 412, height: 892, borderRadius: 36, padding: 12 },
  'ipad-mini': { name: 'iPad Mini', width: 744, height: 1133, borderRadius: 24, padding: 16 },
  'desktop': { name: 'Desktop', width: '100%', height: '100%', borderRadius: 8, padding: 0 }
};

export const DeviceSimulator = () => {
  const [selectedDevice, setSelectedDevice] = useState<keyof typeof DEVICES>('iphone-15');
  const device = DEVICES[selectedDevice];

  return (
    <div className="flex flex-col h-full w-full bg-gray-900 rounded-lg overflow-hidden border border-gray-800">
      <div className="flex items-center justify-between px-4 py-3 border-b border-gray-800 bg-gray-950">
        <div className="flex items-center gap-2 text-gray-300">
          <Smartphone size={18} className="text-blue-400" />
          <span className="font-semibold tracking-wide text-sm uppercase">Device Simulator</span>
        </div>
        <div className="flex bg-gray-900 rounded-md p-1 border border-gray-800">
          {Object.entries(DEVICES).map(([key, config]) => (
            <button
              key={key}
              onClick={() => setSelectedDevice(key as keyof typeof DEVICES)}
              className={`px-3 py-1 text-xs rounded-md transition-colors ${selectedDevice === key ? 'bg-blue-600 text-white' : 'text-gray-400 hover:text-gray-200'}`}
            >
              {config.name}
            </button>
          ))}
        </div>
      </div>
      
      <div className="flex-1 flex items-center justify-center p-6 bg-[radial-gradient(ellipse_at_center,_var(--tw-gradient-stops))] from-gray-800 via-gray-900 to-black overflow-hidden">
        <div 
          className="relative bg-black transition-all duration-500 ease-in-out border-gray-700 overflow-hidden shadow-2xl"
          style={{
            width: device.width,
            height: device.height,
            borderRadius: device.borderRadius,
            borderWidth: selectedDevice === 'desktop' ? 1 : 8,
            boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.5)'
          }}
        >
          <iframe 
            src="http://localhost:8081" 
            className="w-full h-full border-none bg-black"
            title="SK8Lytz Web Demo"
            sandbox="allow-scripts allow-same-origin allow-forms allow-modals"
          />
        </div>
      </div>
    </div>
  );
};
