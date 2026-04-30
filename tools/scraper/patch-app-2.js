const fs = require('fs');
const path = require('path');

const appPath = path.resolve(__dirname, '../../tools/scraper-dashboard/src/App.tsx');
let appCode = fs.readFileSync(appPath, 'utf8');

appCode = appCode.replace(
  /if \(activeTab === 'phase4'\) \{\s*fetchSpots\(0, gridFilter, sortCol, sortDir, searchQuery\);\s*fetchDatabankCoverage\(\);\s*\}/,
  `if (['phase4', 'graveyard'].includes(activeTab)) {
        fetchSpots(0, gridFilter, sortCol, sortDir, searchQuery);
        fetchDatabankCoverage();
      }`
);

fs.writeFileSync(appPath, appCode);
console.log('App.tsx fetchSpots patched.');
