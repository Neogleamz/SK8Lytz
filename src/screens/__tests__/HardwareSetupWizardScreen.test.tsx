import React from 'react';
import { create, act } from 'react-test-renderer';
import { Text } from 'react-native';
import HardwareSetupWizardScreen from '../Onboarding/HardwareSetupWizardScreen';

// ── Module Mocks ─────────────────────────────────────────────────────────────
jest.mock('react-native', () => ({
  View: 'View',
  Text: 'Text',
  TouchableOpacity: 'TouchableOpacity',
  TextInput: 'TextInput',
  ScrollView: 'ScrollView',
  SafeAreaView: 'SafeAreaView',
  KeyboardAvoidingView: 'KeyboardAvoidingView',
  ActivityIndicator: 'ActivityIndicator',
  StyleSheet: { create: (s: any) => s },
  Platform: { OS: 'ios' },
  Linking: { openURL: jest.fn() },
}));

jest.mock('@react-native-async-storage/async-storage', () => ({
  setItem: jest.fn().mockResolvedValue(undefined),
  getItem: jest.fn().mockResolvedValue(null),
}));

jest.mock('../../services/AppLogger', () => ({
  AppLogger: { log: jest.fn(), warn: jest.fn(), error: jest.fn() },
  getAppLogger: () => ({ log: jest.fn(), warn: jest.fn(), error: jest.fn() }),
}));

jest.mock('../../components/dashboard/HardwareStatusPills', () => ({
  HardwareStatusPills: () => null,
}));

jest.mock('@expo/vector-icons', () => ({
  MaterialCommunityIcons: () => null,
}));

jest.mock('../../context/ThemeContext', () => ({
  useTheme: () => ({
    Colors: {
      background: '#0D0D0D', text: '#fff', textMuted: '#888', surface: '#111', surfaceHighlight: '#222', primary: '#00f0ff'
    }
  })
}));

// Helper to find text nodes even if children is an array
const isTextNodeMatch = (node: any, matchStr: string) => {
  if (typeof node.props.children === 'string') {
    return node.props.children.includes(matchStr);
  }
  if (Array.isArray(node.props.children)) {
    return node.props.children.join('').includes(matchStr);
  }
  return false;
};

// ── Tests ────────────────────────────────────────────────────────────────────
describe('HardwareSetupWizardScreen Integration', () => {
  it('should yield a device with group_ids and group_names when registration completes', async () => {
    const mockOnSetupComplete = jest.fn().mockResolvedValue(undefined);
    const mockScanForPeripherals = jest.fn();
    const mockRequestPermissions = jest.fn().mockResolvedValue(true);
    const mockSetPendingRegistrations = jest.fn();
    const mockPingDevice = jest.fn().mockResolvedValue({ ledPoints: 100 });

    const pendingRegistrations = [
      {
        device_mac: 'AA:BB:CC:DD:EE:A3',
        device_name: 'SK8-Test',
        rssi: -50,
        product_type: 'HALOZ',
        led_points: 60,
        segments: 1,
        ic_type: 'WS2812B',
        color_sorting: 'GRB',
        rf_mode: 'BLE'
      }
    ];

    let root: any;
    await act(async () => {
      root = create(
        <HardwareSetupWizardScreen
          onSetupComplete={mockOnSetupComplete}
          scanForPeripherals={mockScanForPeripherals}
          bleState="IDLE"
          requestPermissions={mockRequestPermissions}
          isBluetoothSupported={true}
          isBluetoothEnabled={true}
          pendingRegistrations={pendingRegistrations as any}
          setPendingRegistrations={mockSetPendingRegistrations}
          pingDevice={mockPingDevice}
        />
      );
    });

    // STEP 1: Click Next
    const nextButtonText = root.root.findAllByType(Text).find((t: any) => isTextNodeMatch(t, 'NEXT: REVIEW'));
    expect(nextButtonText).toBeTruthy();
    
    const nextTouchable = nextButtonText.parent.parent;
    await act(async () => {
      nextTouchable.props.onPress();
    });

    // STEP 2: Select Device & Assign Settings
    const deviceNameText = root.root.findAllByType(Text).find((t: any) => isTextNodeMatch(t, 'SK8-Test'));
    expect(deviceNameText).toBeTruthy();
    
    const deviceRowTouchable = deviceNameText.parent.parent; 
    await act(async () => {
      deviceRowTouchable.props.onPress();
    });

    const assignBtnText = root.root.findAllByType(Text).find((t: any) => isTextNodeMatch(t, 'ASSIGN SKATE SETTINGS'));
    expect(assignBtnText).toBeTruthy();
    
    // Sometimes the button text is wrapped in a View or directly in TouchableOpacity
    const assignTouchable = assignBtnText.parent;
    await act(async () => {
      assignTouchable.props.onPress();
    });

    // STEP 3: Complete Setup
    const completeBtnText = root.root.findAllByType(Text).find((t: any) => isTextNodeMatch(t, 'COMPLETE SETUP'));
    expect(completeBtnText).toBeTruthy();
    
    const completeTouchable = completeBtnText.parent;
    await act(async () => {
      await completeTouchable.props.onPress();
    });

    // ASSERTION: Verify the payload shape conforms to the new group_ids architecture
    expect(mockOnSetupComplete).toHaveBeenCalledTimes(1);
    
    const finalizedDevices = mockOnSetupComplete.mock.calls[0][0];
    expect(finalizedDevices).toHaveLength(1);
    
    const registeredDevice = finalizedDevices[0];
    
    // Arrays must exist and be populated
    expect(registeredDevice).toHaveProperty('group_ids');
    expect(registeredDevice).toHaveProperty('group_names');
    expect(registeredDevice.group_ids.length).toBeGreaterThan(0);
    expect(registeredDevice.group_names.length).toBeGreaterThan(0);
    
    // Legacy scalar should not be populated via the FTUE anymore
    expect(registeredDevice.group_id).toBeUndefined();
  });
});
