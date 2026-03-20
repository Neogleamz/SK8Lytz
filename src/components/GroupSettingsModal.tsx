import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, Modal, TouchableOpacity, TextInput, ScrollView } from 'react-native';
import { Colors, Typography, Layout } from '../theme/theme';

interface GroupSettingsModalProps {
  isVisible: boolean;
  onClose: () => void;
  onSave: (name: string, deviceIds: string[]) => void;
  onDelete?: () => void;
  initialName?: string;
  initialDeviceIds?: string[];
  allDevices?: any[];
}

export default function GroupSettingsModal({ isVisible, onClose, onSave, onDelete, initialName = '', initialDeviceIds = [], allDevices = [] }: GroupSettingsModalProps) {
  const [name, setName] = useState(initialName);
  const [selectedIds, setSelectedIds] = useState<string[]>(initialDeviceIds);

  useEffect(() => {
    setName(initialName);
    setSelectedIds(initialDeviceIds);
  }, [initialName, initialDeviceIds, isVisible]);

  const toggleDevice = (id: string) => {
    if (selectedIds.includes(id)) {
       setSelectedIds(selectedIds.filter(x => x !== id));
    } else {
       setSelectedIds([...selectedIds, id]);
    }
  };

  return (
    <Modal visible={isVisible} animationType="fade" transparent={true} onRequestClose={onClose}>
      <View style={styles.overlay}>
        <View style={styles.card}>
          <Text style={Typography.title}>{initialName ? 'Rename Group' : 'Create Group'}</Text>
          <Text style={[Typography.body, { marginTop: 8, color: Colors.textMuted }]}>
            Enter a unique name for this device group.
          </Text>
          <TextInput
            style={styles.input}
            value={name}
            onChangeText={setName}
            placeholder="e.g. My Skates left/right"
            placeholderTextColor={Colors.textMuted}
            autoFocus
          />
          <Text style={[Typography.caption, { color: Colors.textMuted, marginBottom: 8 }]}>Devices in Group ({selectedIds.length})</Text>
          <ScrollView style={styles.deviceList}>
            {allDevices.map(d => {
               const isSelected = selectedIds.includes(d.id);
               return (
                  <TouchableOpacity key={d.id} onPress={() => toggleDevice(d.id)} style={[styles.deviceRow, isSelected && styles.deviceRowSelected]}>
                     <Text style={{ color: Colors.text }}>{d.name}</Text>
                     <View style={[styles.checkbox, isSelected && styles.checkboxSelected]} />
                  </TouchableOpacity>
               );
            })}
          </ScrollView>

          {onDelete && (
             <TouchableOpacity style={styles.deleteBtn} onPress={onDelete}>
                <Text style={styles.deleteText}>DELETE GROUP</Text>
             </TouchableOpacity>
          )}

          <View style={styles.actions}>
            <TouchableOpacity style={styles.cancelBtn} onPress={onClose}>
              <Text style={styles.cancelText}>CANCEL</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={styles.saveBtn}
              onPress={() => {
                if (name.trim()) {
                  onSave(name.trim(), selectedIds);
                  onClose();
                }
              }}
            >
              <Text style={styles.saveText}>SAVE</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    </Modal>
  );
}

const styles = StyleSheet.create({
  overlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.7)',
    justifyContent: 'center',
    padding: 24,
  },
  card: {
    backgroundColor: Colors.surface,
    padding: 24,
    borderRadius: Layout.borderRadius,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
  },
  input: {
    backgroundColor: Colors.background,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
    borderRadius: 8,
    padding: 12,
    color: Colors.text,
    fontSize: 16,
    marginTop: 16,
    marginBottom: 24,
  },
  actions: {
    flexDirection: 'row',
    justifyContent: 'flex-end',
    gap: 12,
  },
  cancelBtn: {
    paddingVertical: 10,
    paddingHorizontal: 16,
    borderRadius: 8,
    backgroundColor: Colors.surfaceHighlight,
  },
  cancelText: {
    color: Colors.textMuted,
    fontWeight: 'bold',
  },
  saveBtn: {
    paddingVertical: 10,
    paddingHorizontal: 16,
    borderRadius: 8,
    backgroundColor: Colors.primary,
  },
  saveText: {
    color: Colors.text,
    fontWeight: 'bold',
  },
  deviceList: {
    maxHeight: 150,
    marginBottom: 16,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
    borderRadius: 8,
    backgroundColor: 'rgba(0,0,0,0.3)'
  },
  deviceRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    padding: 12,
    borderBottomWidth: 1,
    borderBottomColor: Colors.surfaceHighlight,
  },
  deviceRowSelected: {
    backgroundColor: Colors.primary + '22',
  },
  checkbox: {
    width: 20,
    height: 20,
    borderRadius: 4,
    borderWidth: 2,
    borderColor: Colors.textMuted,
  },
  checkboxSelected: {
    backgroundColor: Colors.primary,
    borderColor: Colors.primary,
  },
  deleteBtn: {
    backgroundColor: Colors.error + '22',
    borderWidth: 1,
    borderColor: Colors.error + '44',
    padding: 12,
    borderRadius: 8,
    alignItems: 'center',
    marginBottom: 16,
  },
  deleteText: {
    color: Colors.error,
    fontWeight: 'bold',
  }
});
