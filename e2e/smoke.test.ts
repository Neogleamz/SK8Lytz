import { by, device, element, expect } from 'detox';

describe('App Smoke Test', () => {
  beforeAll(async () => {
    await device.launchApp();
  });

  beforeEach(async () => {
    await device.reloadReactNative();
  });

  it('should show the dashboard screen upon launch', async () => {
    await expect(element(by.id('dashboard-screen'))).toBeVisible();
  });
});
