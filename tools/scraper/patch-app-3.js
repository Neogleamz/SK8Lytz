const fs = require('fs');
const path = require('path');

const appPath = path.resolve(__dirname, '../../tools/scraper-dashboard/src/App.tsx');
let appCode = fs.readFileSync(appPath, 'utf8');

appCode = appCode.replace(
  /const deleteSpot = async \(id: string, name: string\) => \{/,
  `const restoreSpot = async (id: string, name: string) => {
    if (!confirm(\`Restore record "\${name}" to PENDING status?\`)) return;
    try {
      await fetch(\`\${API_BASE}/api/spots/\${id}\`, { 
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ pipeline_status: 'PENDING' })
      });
      fetchSpots(page, gridFilter);
    } catch (e) {}
  };

  const deleteSpot = async (id: string, name: string) => {`
);

appCode = appCode.replace(
  /<button className="btn-icon btn-delete" onClick=\{.*\}>.*<\/button>/g,
  `{activeTab === 'graveyard' ? (
                                <button className="btn-icon" onClick={() => restoreSpot(row.id, row.name)} title="Restore" style={{color: '#4caf50'}}>♻️</button>
                              ) : (
                                <button className="btn-icon btn-delete" onClick={() => deleteSpot(row.id, row.name)}>🗑️</button>
                              )}`
);

fs.writeFileSync(appPath, appCode);
console.log('App.tsx restoreSpot patched.');
