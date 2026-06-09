import React from 'react';

export default function AuditLogsWidget() {
  return (
    <div className="glass-panel p-6 rounded-2xl w-full max-w-4xl mx-auto mt-8">
      <h2 className="text-2xl font-bold text-[var(--accent)] mb-4">Audit Logs</h2>
      <p className="text-[var(--text-muted)] mb-6">Track command center actions and admin history.</p>
      
      <div className="flex items-center justify-center h-64 border border-dashed border-[var(--border-color)] rounded-xl">
        <span className="text-[var(--text-muted)]">Audit Module Under Construction</span>
      </div>
    </div>
  );
}
