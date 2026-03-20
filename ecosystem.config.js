module.exports = {
  apps: [
    {
      name: 'mobile-web-demo',
      script: 'npx',
      args: 'serve dist -p 8081',
      interpreter: 'none',
      env: {
        NODE_ENV: 'production',
      },
    },
  ],
};
