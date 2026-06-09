export const requestForegroundPermissionsAsync = jest.fn().mockResolvedValue({ status: 'granted' });
export const getForegroundPermissionsAsync = jest.fn().mockResolvedValue({ status: 'granted' });
export const getCurrentPositionAsync = jest.fn().mockResolvedValue({
  coords: { latitude: 38.9, longitude: -94.6, accuracy: 10 }
});
export const getLastKnownPositionAsync = jest.fn().mockResolvedValue({
  coords: { latitude: 38.9, longitude: -94.6, accuracy: 10 }
});
export const reverseGeocodeAsync = jest.fn().mockResolvedValue([
  { city: 'Overland Park', region: 'KS', name: 'SkateCity OP' }
]);
export const Accuracy = { Balanced: 3, High: 4 };
