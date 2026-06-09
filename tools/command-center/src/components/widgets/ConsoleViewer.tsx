import React, { useEffect, useState, useRef } from 'react';
import { Terminal, Trash2 } from 'lucide-react';

interface LogEntry {
  id: string;
  timestamp: Date;
  level: 'log' | 'warn' | 'error';
  data: string[];
}

export const ConsoleViewer = () => {
  const [logs, setLogs] = useState<LogEntry[]>([]);
  const scrollRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleMessage = (event: MessageEvent) => {
      if (event.data && event.data.source === 'sk8lytz-demo') {
        setLogs(prev => {
          const newLogs = [...prev, {
            id: Math.random().toString(36).substr(2, 9),
            timestamp: new Date(),
            level: event.data.level,
            data: event.data.data
          }];
          // Keep last 100 logs
          return newLogs.slice(-100);
        });
      }
    };

    window.addEventListener('message', handleMessage);
    return () => window.removeEventListener('message', handleMessage);
  }, []);

  useEffect(() => {
    if (scrollRef.current) {
      scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
    }
  }, [logs]);

  return (
    <div className="flex flex-col h-full w-full bg-[#1e1e1e] rounded-lg border border-gray-800 overflow-hidden font-mono text-sm">
      <div className="flex items-center justify-between px-4 py-2 border-b border-gray-800 bg-[#252526]">
        <div className="flex items-center gap-2 text-gray-300">
          <Terminal size={16} className="text-gray-400" />
          <span>Live Console</span>
        </div>
        <button 
          onClick={() => setLogs([])}
          className="text-gray-500 hover:text-red-400 transition-colors"
          title="Clear Console"
        >
          <Trash2 size={16} />
        </button>
      </div>
      <div ref={scrollRef} className="flex-1 overflow-y-auto p-4 space-y-2">
        {logs.length === 0 ? (
          <div className="text-gray-600 italic">Waiting for logs from Simulator...</div>
        ) : (
          logs.map(log => (
            <div 
              key={log.id} 
              className={`flex gap-3 pb-2 border-b border-gray-800/50 ${
                log.level === 'error' ? 'text-red-400 bg-red-900/10' :
                log.level === 'warn' ? 'text-yellow-400 bg-yellow-900/10' :
                'text-gray-300'
              }`}
            >
              <span className="text-gray-600 shrink-0">
                {log.timestamp.toLocaleTimeString([], { hour12: false, hour: '2-digit', minute: '2-digit', second: '2-digit' })}
              </span>
              <div className="break-words w-full">
                {log.data.join(' ')}
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};
