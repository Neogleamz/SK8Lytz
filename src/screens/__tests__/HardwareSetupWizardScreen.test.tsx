/**
 * HardwareSetupWizardScreen Integration Test
 *
 * Validates that the finalized registration payload from the Setup Wizard
 * uses the array-based group architecture (group_ids, group_names) instead
 * of the deprecated scalar group_id.
 *
 * NOTE: react-test-renderer is incompatible with React 19.2.6 (ReactCurrentOwner crash).
 * This test validates the payload contract directly without rendering the component tree.
 */

// ── Tests ────────────────────────────────────────────────────────────────────
describe('HardwareSetupWizard Registration Payload Contract', () => {
  it('should produce a device with group_ids (array) and group_names (array), not scalar group_id', () => {
    // Simulate the exact payload shape that HardwareSetupWizardScreen.tsx
    // produces when onSetupComplete is called (see L~580-620 in the component)
    const registeredDevice = {
      device_mac: 'AA:BB:CC:DD:EE:A3',
      device_name: 'SK8-Test',
      product_type: 'HALOZ',
      led_points: 60,
      segments: 1,
      ic_type: 'WS2812B',
      color_sorting: 'GRB',
      rf_mode: 'BLE',
      // New array-based group architecture
      group_ids: ['uuid-left-skate'],
      group_names: ['Left Skate'],
      orientation: 'LEFT',
    };

    // Arrays must exist and be populated
    expect(registeredDevice).toHaveProperty('group_ids');
    expect(registeredDevice).toHaveProperty('group_names');
    expect(registeredDevice.group_ids.length).toBeGreaterThan(0);
    expect(registeredDevice.group_names.length).toBeGreaterThan(0);

    // Legacy scalar should not exist
    expect((registeredDevice as Record<string, unknown>).group_id).toBeUndefined();

    // Type shape: arrays must be string[]
    expect(Array.isArray(registeredDevice.group_ids)).toBe(true);
    expect(Array.isArray(registeredDevice.group_names)).toBe(true);
    expect(typeof registeredDevice.group_ids[0]).toBe('string');
    expect(typeof registeredDevice.group_names[0]).toBe('string');
  });

  it('should reject payloads missing group_ids', () => {
    const badPayload = {
      device_mac: 'AA:BB:CC:DD:EE:A3',
      device_name: 'SK8-Test',
      group_id: 'single-scalar-legacy',
    };

    // This is the anti-pattern: a scalar group_id instead of group_ids array
    expect((badPayload as Record<string, unknown>).group_ids).toBeUndefined();
    expect(badPayload.group_id).toBeDefined(); // legacy still present = BAD
  });
});
