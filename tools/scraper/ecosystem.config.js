module.exports = {
  apps: [
    {
      name: 'sk8lytz-cctower',
      script: 'node_modules/tsx/dist/cli.cjs',
      args: 'CCTower.ts',
      cwd: 'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz-worktrees/feat-scraper-control-plane/tools/scraper',
      watch: false,
      env: {
        NODE_ENV: 'production'
      },
      log_date_format: 'YYYY-MM-DD HH:mm:ss',
      error_file: './logs/daemon-error.log',
      out_file: './logs/daemon-out.log'
    },
    {
      name: 'sk8lytz-dashboard',
      script: 'node_modules/vite/bin/vite.js',
      args: '--port 5173',
      cwd: 'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz-worktrees/feat-scraper-control-plane/tools/scraper-dashboard',
      env: {
        VITE_API_BASE: 'http://localhost:5999'
      },
      log_date_format: 'YYYY-MM-DD HH:mm:ss'
    }
  ]
};
