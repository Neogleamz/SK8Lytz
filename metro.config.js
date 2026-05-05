// metro.config.js — SK8Lytz
//
// Adds a web-platform resolver that aliases native-only modules to no-op stubs
// so the Expo web build doesn't white-screen on modules that call
// TurboModuleRegistry.getEnforcing() at import time.
//
// Pattern: create src/mocks/<package-name>.web.js for any future native-only
// module that causes a similar crash on web.

const { getDefaultConfig } = require('expo/metro-config');
const path = require('path');

const config = getDefaultConfig(__dirname);

// ── Web-platform no-op shims ──────────────────────────────────────────────────
// Keys: the npm package name that crashes on web.
// Values: absolute path to the stub file in src/mocks/.
const WEB_SHIMS = {
  'react-native-worklets': path.resolve(
    __dirname,
    'src/mocks/react-native-worklets.web.js'
  ),
  'react-native-vision-camera-worklets': path.resolve(
    __dirname,
    'src/mocks/react-native-vision-camera-worklets.web.js'
  ),
};

const originalResolveRequest = config.resolver.resolveRequest;

config.resolver.resolveRequest = (context, moduleName, platform) => {
  // Only apply shims on the web platform
  if (platform === 'web' && WEB_SHIMS[moduleName]) {
    return {
      filePath: WEB_SHIMS[moduleName],
      type: 'sourceFile',
    };
  }

  // Fall through to the default resolver for everything else
  if (originalResolveRequest) {
    return originalResolveRequest(context, moduleName, platform);
  }
  return context.resolveRequest(context, moduleName, platform);
};

module.exports = config;
