module.exports = {
  preset: 'ts-jest',
  testEnvironment: 'node',
  modulePathIgnorePatterns: ["<rootDir>/.local-builder/", "<rootDir>/e2e/"],
  testPathIgnorePatterns: ["<rootDir>/e2e/"],
  transformIgnorePatterns: [
    "node_modules/(?!(react-native|@react-native|expo|@expo|expo-battery|expo-device|expo-location|expo-modules-core|expo-status-bar|expo-splash-screen|expo-linking|@react-native-async-storage|react-native-reanimated|react-native-ble-plx)/)"
  ],
  moduleNameMapper: {
    // Resolve the local Expo module without npm install (mirrors tsconfig paths)
    "^sk8lytz-watch-bridge$": "<rootDir>/src/__mocks__/sk8lytz-watch-bridge.ts"
  }
};

