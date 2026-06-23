module.exports = {
  preset: 'jest-expo',
  testEnvironment: 'node',
  modulePathIgnorePatterns: ["<rootDir>/.local-builder/", "<rootDir>/e2e/"],
  testPathIgnorePatterns: ["<rootDir>/e2e/", "<rootDir>/src/__tests__/test-env.d.ts"],
  transformIgnorePatterns: [
    "node_modules/(?!(react-native|@react-native|expo|@expo|expo-battery|expo-device|expo-location|expo-audio|expo-modules-core|expo-status-bar|expo-splash-screen|expo-linking|@react-native-async-storage|react-native-reanimated|react-native-ble-plx)/)"
  ],
  moduleNameMapper: {
    // Resolve the local Expo module without npm install (mirrors tsconfig paths)
    "^sk8lytz-watch-bridge$": "<rootDir>/src/__mocks__/sk8lytz-watch-bridge.ts",
    "^expo-location$": "<rootDir>/src/__mocks__/expo-location.ts",
    "^expo-audio$": "<rootDir>/src/__mocks__/expo-audio.ts",
    // MMKV is a JSI/Nitro native module — cannot run in Node/Jest; use in-memory mock
    "^react-native-mmkv$": "<rootDir>/src/__mocks__/react-native-mmkv.ts"
  }
};

