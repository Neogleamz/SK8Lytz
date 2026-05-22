const path = require('path');

module.exports = {
  apps: [
    {
      name: 'sk8lytz-cctower',
      script: path.resolve(__dirname, 'CCTower.ts'),
      interpreter: 'node',
      interpreter_args: path.resolve(__dirname, 'node_modules/tsx/dist/cli.mjs'),
      cwd: __dirname,
      watch: false,
      env: { NODE_ENV: 'production' },
      log_date_format: 'YYYY-MM-DD HH:mm:ss',
      error_file: path.resolve(__dirname, 'logs/api-error.log'),
      out_file: path.resolve(__dirname, 'logs/api-out.log')
    },
    {
      name: 'scraper-indexer',
      script: path.resolve(__dirname, 'Indexer.ts'),
      interpreter: 'node',
      interpreter_args: path.resolve(__dirname, 'node_modules/tsx/dist/cli.mjs'),
      cwd: __dirname,
      watch: false,
      autorestart: false,
      max_restarts: 0,
      env: { NODE_ENV: 'production' },
      log_date_format: 'YYYY-MM-DD HH:mm:ss',
      error_file: path.resolve(__dirname, 'logs/indexer-error.log'),
      out_file: path.resolve(__dirname, 'logs/indexer-out.log')
    },
    {
      name: 'scraper-photographer',
      script: path.resolve(__dirname, 'Photographer.ts'),
      interpreter: 'node',
      interpreter_args: path.resolve(__dirname, 'node_modules/tsx/dist/cli.mjs'),
      cwd: __dirname,
      watch: false,
      autorestart: false,
      max_restarts: 0,
      env: { NODE_ENV: 'production' },
      log_date_format: 'YYYY-MM-DD HH:mm:ss',
      error_file: path.resolve(__dirname, 'logs/photographer-error.log'),
      out_file: path.resolve(__dirname, 'logs/photographer-out.log')
    },
    {
      name: 'scraper-publisher',
      script: path.resolve(__dirname, 'Publisher.ts'),
      interpreter: 'node',
      interpreter_args: path.resolve(__dirname, 'node_modules/tsx/dist/cli.mjs'),
      cwd: __dirname,
      watch: false,
      autorestart: false,
      max_restarts: 0,
      env: { NODE_ENV: 'production' },
      log_date_format: 'YYYY-MM-DD HH:mm:ss',
      error_file: path.resolve(__dirname, 'logs/publisher-error.log'),
      out_file: path.resolve(__dirname, 'logs/publisher-out.log')
    },
    {
      name: 'discord-bridge',
      script: 'index.js',
      cwd: path.resolve(__dirname, '../discord-bridge'),
      watch: false,
      log_date_format: 'YYYY-MM-DD HH:mm:ss'
    },
    {
      name: 'scraper-dashboard',
      script: 'node_modules/vite/bin/vite.js',
      cwd: path.resolve(__dirname, '../scraper-dashboard'),
      watch: false,
      log_date_format: 'YYYY-MM-DD HH:mm:ss'
    }
  ]
};
