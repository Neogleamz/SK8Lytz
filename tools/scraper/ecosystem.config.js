module.exports = {
  apps: [
    {
      name: 'sk8lytz-cctower',
      script: 'node_modules/tsx/dist/cli.cjs',
      args: 'CCTower.ts',
      cwd: 'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/scraper',
      watch: false,
      env: { NODE_ENV: 'production' },
      log_date_format: 'YYYY-MM-DD HH:mm:ss',
      error_file: './logs/api-error.log',
      out_file: './logs/api-out.log'
    },
    {
      name: 'scraper-operator',
      script: 'node_modules/tsx/dist/cli.cjs',
      args: 'Operator.ts',
      cwd: 'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/scraper',
      watch: false,
      env: { NODE_ENV: 'production' },
      log_date_format: 'YYYY-MM-DD HH:mm:ss',
      error_file: './logs/operator-error.log',
      out_file: './logs/operator-out.log'
    },
    {
      name: 'scraper-indexer',
      script: 'node_modules/tsx/dist/cli.cjs',
      args: 'Indexer.ts',
      cwd: 'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/scraper',
      watch: false,
      env: { NODE_ENV: 'production' },
      log_date_format: 'YYYY-MM-DD HH:mm:ss',
      error_file: './logs/indexer-error.log',
      out_file: './logs/indexer-out.log'
    },
    {
      name: 'scraper-photographer',
      script: 'node_modules/tsx/dist/cli.cjs',
      args: 'Photographer.ts',
      cwd: 'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/scraper',
      watch: false,
      env: { NODE_ENV: 'production' },
      log_date_format: 'YYYY-MM-DD HH:mm:ss',
      error_file: './logs/photographer-error.log',
      out_file: './logs/photographer-out.log'
    },
    {
      name: 'ollama-daemon',
      script: 'ollama',
      args: 'serve',
      cwd: 'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/scraper',
      watch: false,
      env: { OLLAMA_HOST: '127.0.0.1:11434' },
      log_date_format: 'YYYY-MM-DD HH:mm:ss',
      error_file: './logs/ollama-error.log',
      out_file: './logs/ollama-out.log'
    }
  ]
};

