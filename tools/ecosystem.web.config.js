// PM2 ecosystem config for SK8Lytz web demo
// Usage: pm2 start tools\ecosystem.web.config.js
// Keeps the Expo/Metro web bundler alive persistently with auto-restart on crash.
module.exports = {
  apps: [
    {
      name: 'sk8lytz-web',
      script: 'node_modules\\expo\\bin\\cli',
      interpreter: 'node',
      args: 'start --web --clear',
      cwd: 'C:\\Neogleamz\\AG_SK8Lytz_App\\SK8Lytz',
      autorestart: true,
      watch: false,
      max_restarts: 10,
      restart_delay: 3000,
      env: {
        EXPO_NO_TELEMETRY: '1',
        NODE_ENV: 'development',
      },
    },
  ],
};
