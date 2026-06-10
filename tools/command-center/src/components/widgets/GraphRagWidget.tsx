import React from 'react';

export default function GraphRagWidget() {
  return (
    <div className="w-full h-full bg-gray-900 rounded-lg overflow-hidden flex flex-col">
      <div className="p-4 border-b border-gray-800 flex justify-between items-center bg-gray-950">
        <h2 className="text-xl font-bold text-white flex items-center gap-2">
          GraphRAG UI
        </h2>
        <a href="http://localhost:8505/" target="_blank" rel="noreferrer" className="text-sm text-blue-400 hover:underline">
          Open in New Tab
        </a>
      </div>
      <div className="flex-1 w-full relative">
        <iframe 
          src="http://localhost:8505/" 
          title="GraphRAG UI"
          className="absolute inset-0 w-full h-full border-0"
          style={{ width: '100%', height: '100%', minHeight: 'calc(100vh - 120px)' }}
        />
      </div>
    </div>
  );
}
