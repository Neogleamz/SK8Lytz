module.exports = {
  preset: 'ts-jest',
  testEnvironment: 'node',
  modulePathIgnorePatterns: ["<rootDir>/.local-builder/", "<rootDir>/e2e/"],
  testPathIgnorePatterns: ["<rootDir>/e2e/"],
  transformIgnorePatterns: [
    "node_modules/(?!(react-native|@react-native|expo|@expo|react-native-reanimated)/)"
  ]
};
