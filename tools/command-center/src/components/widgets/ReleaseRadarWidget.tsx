import React from 'react';

export default function ReleaseRadarWidget() {
  return (
    <div className="glass-panel p-6 rounded-2xl w-full max-w-4xl mx-auto mt-8">
      <h2 className="text-2xl font-bold text-[var(--accent)] mb-4">Release Radar</h2>
      <p className="text-[var(--text-muted)] mb-6">Track OTA Expo updates and app store rollout metrics.</p>
      
      <div className="flex items-center justify-center h-64 border border-dashed border-[var(--border-color)] rounded-xl">
        <span className="text-[var(--text-muted)]">Radar Module Under Construction</span>
      </div>
    </div>
  );
}
