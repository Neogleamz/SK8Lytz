import React from 'react';
import { DeviceSimulator } from './DeviceSimulator';
import { ConsoleViewer } from './ConsoleViewer';

export default function AppManagerWidget() {
  return (
    <div className="flex flex-col md:flex-row gap-6 p-6 h-[calc(100vh-60px)] w-full max-w-7xl mx-auto">
      <div className="flex-1 min-w-[300px] h-full">
        <DeviceSimulator />
      </div>
      <div className="w-full md:w-[400px] h-[300px] md:h-full">
        <ConsoleViewer />
      </div>
    </div>
  );
}
