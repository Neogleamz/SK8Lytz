import React from 'react';
import { View, Text, TouchableOpacity, Modal } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Spacing } from '../../theme/theme';

export interface ConfirmDeleteModalProps {
  visible: boolean;
  onClose: () => void;
  onConfirm: () => void;
  isDark: boolean;
}

export const ConfirmDeleteModal = React.memo(({ visible, onClose, onConfirm, isDark }: ConfirmDeleteModalProps) => {
  return (
    <Modal visible={visible} transparent animationType="fade">
      <View style={{ flex: 1, backgroundColor: 'rgba(0,0,0,0.6)', justifyContent: 'center', alignItems: 'center', padding: Spacing.xl }}>
        <View style={{ backgroundColor: isDark ? '#1A1A1A' : '#FFF', padding: Spacing.xl, borderRadius: 12, width: '100%', maxWidth: 400, borderColor: '#ff4040', borderWidth: 1 }}>
          <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: Spacing.lg }}>
            <MaterialCommunityIcons name="alert" size={24} color="#ff4040" />
            <Text style={{ fontSize: 18, fontWeight: '800', color: isDark ? '#FFF' : '#000', marginLeft: Spacing.sm }}>Purge Telemetry Logs</Text>
          </View>
          <Text style={{ fontSize: 14, color: isDark ? '#CCC' : '#444', marginBottom: Spacing.xl, lineHeight: 20 }}>
            Are you sure you want to completely erase all timeline, device, and analytics stats from local memory? This action cannot be reversed.
          </Text>
          <View style={{ flexDirection: 'row', justifyContent: 'flex-end', gap: Spacing.md }}>
            <TouchableOpacity onPress={onClose} style={{ paddingVertical: Spacing.md, paddingHorizontal: Spacing.lg, borderRadius: 6, backgroundColor: isDark ? '#333' : '#EEE' }}>
              <Text style={{ fontWeight: '700', color: isDark ? '#FFF' : '#000' }}>Cancel</Text>
            </TouchableOpacity>
            <TouchableOpacity onPress={onConfirm} style={{ paddingVertical: Spacing.md, paddingHorizontal: Spacing.lg, borderRadius: 6, backgroundColor: '#ff4040' }}>
              <Text style={{ fontWeight: '700', color: '#FFF' }}>Erase Everything</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    </Modal>
  );
});
