export const locationService = {
  getSilentLocation: jest.fn().mockResolvedValue(null),
  requestLocationPermissions: jest.fn().mockResolvedValue(false),
};
