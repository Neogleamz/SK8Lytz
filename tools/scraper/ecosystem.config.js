module.exports = {
  apps: [
    {
      name: 'sk8lytz-cctower',
      script: 'CCTower.ts',
      interpreter: 'node',
      interpreter_args: '--import tsx',
      cwd: 'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/scraper',
      watch: false,
      env: { NODE_ENV: 'production' },
      log_date_format: 'YYYY-MM-DD HH:mm:ss',
      error_file: './logs/api-error.log',
      out_file: './logs/api-out.log'
    },
    {
      name: 'scraper-indexer',
      script: 'Indexer.ts',
      interpreter: 'node',
      interpreter_args: '--import tsx',
      cwd: 'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/scraper',
      watch: false,
      env: { NODE_ENV: 'production' },
      log_date_format: 'YYYY-MM-DD HH:mm:ss',
      error_file: './logs/indexer-error.log',
      out_file: './logs/indexer-out.log'
    },
    {
      name: 'scraper-photographer',
      script: 'Photographer.ts',
      interpreter: 'node',
      interpreter_args: '--import tsx',
      cwd: 'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/scraper',
      watch: false,
      env: { NODE_ENV: 'production' },
      log_date_format: 'YYYY-MM-DD HH:mm:ss',
      error_file: './logs/photographer-error.log',
      out_file: './logs/photographer-out.log'
    },
    {
      name: 'scraper-publisher',
      script: 'Publisher.ts',
      interpreter: 'node',
      interpreter_args: '--import tsx',
      cwd: 'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/scraper',
      watch: false,
      env: { NODE_ENV: 'production' },
      log_date_format: 'YYYY-MM-DD HH:mm:ss',
      error_file: './logs/publisher-error.log',
      out_file: './logs/publisher-out.log'
    },
    {
      name: 'discord-bridge',
      script: 'index.js',
      cwd: 'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/discord-bridge',
      watch: false,
      log_date_format: 'YYYY-MM-DD HH:mm:ss'
    },
    {
      name: 'scraper-dashboard',
      script: 'node_modules/vite/bin/vite.js',
      cwd: 'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/scraper-dashboard',
      watch: false,
      log_date_format: 'YYYY-MM-DD HH:mm:ss'
    }
  ]
};

