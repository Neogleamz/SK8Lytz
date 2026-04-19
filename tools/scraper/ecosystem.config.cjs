module.exports = {
  apps: [{
    name: 'sk8lytz-cctower',
    script: 'node_modules/tsx/dist/cli.mjs',
    args: 'CCTower.ts',
    cwd: './',
    env: {
      NODE_ENV: 'development'
    }
  }]
};
