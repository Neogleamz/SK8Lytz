module.exports = {
  apps: [
    {
      name: 'mobile-web-demo',
      script: 'node_modules/serve/build/main.js',
      args: ['dist', '-p', '8081'],
      env: {
        NODE_ENV: 'production',
      },
    },
  ],
};
