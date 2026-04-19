module.exports = {
  apps: [
    {
      name: 'sk8lytz-cctower',
      script: 'node_modules/tsx/dist/cli.cjs',
      args: 'CCTower.ts',
      cwd: 'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/scraper',
      watch: false,
      env: {
        NODE_ENV: 'production'
      },
      log_date_format: 'YYYY-MM-DD HH:mm:ss',
      error_file: './logs/daemon-error.log',
      out_file: './logs/daemon-out.log'
    }
  ]
};
