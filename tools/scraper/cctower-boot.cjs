// PM2-safe bootstrap for CCTower.ts on Windows.
// Uses tsx CJS API register hook — avoids the --import ESM loader
// that causes 0x800700E8 broken pipe with PM2's ProcessContainerFork.
const { register } = require('./node_modules/tsx/dist/cjs/api/index.cjs');
register();
require('./CCTower.ts');
