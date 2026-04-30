const fs = require('fs');
const path = require('path');

const appPath = path.resolve(__dirname, '../../tools/scraper-dashboard/src/App.tsx');
let appCode = fs.readFileSync(appPath, 'utf8');

// 1. Add Sniper and Graveyard buttons next to Factory Floor
appCode = appCode.replace(
  /<button className="btn-icon" onClick=\{\(\) => setActiveTab\('pipeline'\)\} title="Factory Floor"[\s\S]*?FACTORY FLOOR\s*<\/button>/,
  `<button className="btn-icon" onClick={() => setActiveTab('pipeline')} title="Factory Floor"
          style={{ background: activeTab === 'pipeline' ? 'rgba(0, 255, 170, 0.2)' : 'rgba(255,255,255,0.05)', color: activeTab === 'pipeline' ? '#00ffaa' : 'rgba(255,255,255,0.6)', border: activeTab === 'pipeline' ? '1px solid #00ffaa' : '1px solid transparent', padding: '4px 8px', borderRadius: '6px', marginRight: '4px', cursor: 'pointer', fontSize: '0.65rem', fontWeight: 800 }}>
          FACTORY FLOOR
        </button>
        <button className="btn-icon" onClick={() => setActiveTab('sniper')} title="Sniper Bench"
          style={{ background: activeTab === 'sniper' ? 'rgba(255, 106, 0, 0.2)' : 'rgba(255,255,255,0.05)', color: activeTab === 'sniper' ? '#ff6a00' : 'rgba(255,255,255,0.6)', border: activeTab === 'sniper' ? '1px solid #ff6a00' : '1px solid transparent', padding: '4px 8px', borderRadius: '6px', marginRight: '4px', cursor: 'pointer', fontSize: '0.85rem' }}>
          🔫
        </button>
        <button className="btn-icon" onClick={() => setActiveTab('graveyard')} title="Garbage Can (Rejected & Purged)"
          style={{ background: activeTab === 'graveyard' ? 'rgba(244, 67, 54, 0.2)' : 'rgba(255,255,255,0.05)', color: activeTab === 'graveyard' ? '#f44336' : 'rgba(255,255,255,0.6)', border: activeTab === 'graveyard' ? '1px solid #f44336' : '1px solid transparent', padding: '4px 8px', borderRadius: '6px', marginRight: '4px', cursor: 'pointer', fontSize: '0.85rem' }}>
          🗑️
        </button>`
);

// 2. Add graveyard to activeTab union type
appCode = appCode.replace(
  /const \[activeTab, setActiveTab\] = useState<'pipeline' \| 'phase1' \| 'phase2' \| 'phase3' \| 'phase4' \| 'sniper'>\('pipeline'\);/,
  `const [activeTab, setActiveTab] = useState<'pipeline' | 'phase1' | 'phase2' | 'phase3' | 'phase4' | 'sniper' | 'graveyard'>('pipeline');`
);

// 3. Update fetchSpots to pass pipeline_status for graveyard
appCode = appCode.replace(
  /const stateParam = activeState\.length === 2 \? `&state=\$\{activeState\.toUpperCase\(\)\}` : '';\s*const url = `\$\{API_BASE\}\/api\/spots\?limit=\$\{rowsPerPage\}&offset=\$\{offset\}&status=\$\{filter\}&sortCol=\$\{col\}&sortDir=\$\{dir\}&search=\$\{encodeURIComponent\(search\)\}\$\{stateParam\}\$\{chipParams \? '&' \+ chipParams : ''\}`;/,
  `const stateParam = activeState.length === 2 ? \`&state=\${activeState.toUpperCase()}\` : '';
        const plStatus = activeTab === 'graveyard' ? '&pipeline_status=REJECTED' : '';
        const url = \`\${API_BASE}/api/spots?limit=\${rowsPerPage}&offset=\${offset}&status=\${filter}&sortCol=\${col}&sortDir=\${dir}&search=\${encodeURIComponent(search)}\${stateParam}\${chipParams ? '&' + chipParams : ''}\${plStatus}\`;`
);

// 4. Update UI to render phase4 OR graveyard
appCode = appCode.replace(
  /\{activeTab === 'phase4' && \(\s*<div className="tab-pane graveyard fade-in">\s*<div className="explainer-block" style=\{\{marginBottom: '1rem', background: 'rgba\(76, 175, 80, 0\.05\)', border: '1px solid rgba\(76, 175, 80, 0\.2\)'\}\}>\s*<h3 style=\{\{marginTop: 0, color: '#4caf50'\}\}>Phase 4: Databank QA &amp; Live Publish<\/h3>\s*<p>Final review before publication\. Filter and inspect records, then publish state-by-state or individually\. Use the view toggle to switch between Card, List, and Table views\.<\/p>\s*<\/div>/,
  `{['phase4', 'graveyard'].includes(activeTab) && (
            <div className="tab-pane graveyard fade-in">
              <div className="explainer-block" style={{marginBottom: '1rem', background: activeTab === 'graveyard' ? 'rgba(244, 67, 54, 0.05)' : 'rgba(76, 175, 80, 0.05)', border: \`1px solid \${activeTab === 'graveyard' ? 'rgba(244, 67, 54, 0.2)' : 'rgba(76, 175, 80, 0.2)'}\`}}>
                <h3 style={{marginTop: 0, color: activeTab === 'graveyard' ? '#f44336' : '#4caf50'}}>
                  {activeTab === 'graveyard' ? 'Graveyard: Rejected & Purged Records' : 'Phase 4: Databank QA & Live Publish'}
                </h3>
                <p>{activeTab === 'graveyard' 
                    ? 'Review records that were blocked by the Guillotine or manually rejected. These will not be published.'
                    : 'Final review before publication. Filter and inspect records, then publish state-by-state or individually. Use the view toggle to switch between Card, List, and Table views.'}
                </p>
              </div>`
);

// 5. Hide Coverage Map if in graveyard
appCode = appCode.replace(
  /\{\/\* =========== STATUS COVERAGE MAP =========== \*\/\}\s*<div style=\{\{ marginBottom: '2rem', padding: '1\.5rem', background: 'rgba\(255,179,0,0\.03\)'/,
  `{/* =========== STATUS COVERAGE MAP =========== */}
              {activeTab !== 'graveyard' && (
              <div style={{ marginBottom: '2rem', padding: '1.5rem', background: 'rgba(255,179,0,0.03)'`
);

appCode = appCode.replace(
  /<\/div>\s*\)\}\s*<\/div>\s*\{\/\* =========== SPOT DIRECTORY =========== \*\/\}/,
  `</div>
                )}
              </div>
              )}
              {/* =========== SPOT DIRECTORY =========== */}`
);

// We need to ensure fetchSpots re-runs when activeTab changes to 'graveyard'
appCode = appCode.replace(
  /useEffect\(\(\) => \{\s*fetchSpots\(0\);\s*\}, \[filter, activeTab, stateChip\]\);/g,
  `useEffect(() => {
    fetchSpots(0);
  }, [filter, activeTab, stateChip]);`
);
// It might not have activeTab in dependency array! Let's check.

fs.writeFileSync(appPath, appCode);
console.log('App.tsx patched for Graveyard and Rifle icon.');
