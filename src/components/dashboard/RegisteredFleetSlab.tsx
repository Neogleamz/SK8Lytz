/**
 * RegisteredFleetSlab.tsx — "REGISTERED DEVICES" Fleet Slab
 *
 * SLAB 4 of the disconnected dashboard view. Collapsible list of all
 * registered devices, rendered via the parent's `renderItem` callback.
 *
 * Extracted from DashboardScreen.tsx (chore/refactor-dashboard-monolith).
 */
import { MaterialCommunityIcons } from '@expo/vector-icons';
import React from 'react';
import { Text, TouchableOpacity, View } from 'react-native';

interface RegisteredFleetSlabProps {
  registeredDevices: any[];
  isRegisteredCollapsed: boolean;
  onToggleCollapse: () => void;
  onSetupWizard: () => void;
  renderItem: (info: { item: any }) => React.ReactElement | null;
  Colors: any;
  styles: any;
}

const RegisteredFleetSlab = React.memo(({
  registeredDevices,
  isRegisteredCollapsed,
  onToggleCollapse,
  onSetupWizard,
  renderItem,
  Colors,
  styles,
}: RegisteredFleetSlabProps) => (
  <View style={styles.slabContainer}>
    {/* Header — tappable to collapse/expand */}
    <TouchableOpacity
      style={styles.slabHeader}
      onPress={onToggleCollapse}
      activeOpacity={0.7}
    >
      <View style={{ flexDirection: 'row', alignItems: 'center', gap: 6 }}>
        <MaterialCommunityIcons
          name={isRegisteredCollapsed ? 'chevron-down' : 'chevron-up'}
          size={16}
          color={Colors.textMuted}
        />
        <Text style={styles.slabTitle}>REGISTERED DEVICES</Text>
      </View>
      <TouchableOpacity
        onPress={onSetupWizard}
        style={{ flexDirection: 'row', alignItems: 'center', gap: 4 }}
      >
        <MaterialCommunityIcons name="plus-circle-outline" size={14} color={Colors.primary} />
        <Text style={[styles.slabActionText, { color: Colors.primary }]}>ADD DEVICE</Text>
      </TouchableOpacity>
    </TouchableOpacity>

    {!isRegisteredCollapsed && (
      registeredDevices.length > 0 ? (
        <View style={styles.deviceListFixed}>
          {registeredDevices.map((d: any) => (
            <View key={d.id || d.device_mac} style={{ marginBottom: 8 }}>
              {renderItem({ item: d })}
            </View>
          ))}
        </View>
      ) : (
        <View style={[styles.glassSlab, { alignItems: 'center', paddingVertical: 32 }]}>
          <MaterialCommunityIcons name="bluetooth-connect" size={32} color={Colors.textMuted} style={{ marginBottom: 12 }} />
          <Text style={styles.slabEmptyText}>No registered skates found.</Text>
          <TouchableOpacity
            onPress={onSetupWizard}
            style={[styles.scanButton, { marginTop: 16, width: '60%' }]}
          >
            <Text style={styles.scanButtonText}>START SETUP</Text>
          </TouchableOpacity>
        </View>
      )
    )}
  </View>
));

export default RegisteredFleetSlab;
