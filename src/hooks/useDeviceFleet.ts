import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { Alert } from 'react-native';
import { AppLogger } from '../services/AppLogger';
import { supabase } from '../services/supabaseClient';

export type StoredDevice = {
  id: string;
  mac?: string;
  name: string;
  customName?: string;
  groupName?: string;
  type?: string;
  registeredAt?: string;
};

interface UseDeviceFleetProps {
  visible: boolean;
  initialDevices: StoredDevice[];
  onDeviceRenamed?: (deviceId: string, newName: string) => void;
  onDeviceForgotten?: (deviceId: string) => void;
  onGroupRenamed?: (oldGroupName: string, newGroupName: string) => void;
  onGroupForgotten?: (groupName: string) => void;
}

export function useDeviceFleet({
  visible,
  initialDevices,
  onDeviceRenamed,
  onDeviceForgotten,
  onGroupRenamed,
  onGroupForgotten,
}: UseDeviceFleetProps) {
  const [devices, setDevices] = useState<StoredDevice[]>([]);
  const [editingDeviceId, setEditingDeviceId] = useState<string | null>(null);
  const [deviceNewName, setDeviceNewName] = useState('');
  const [editingGroupId, setEditingGroupId] = useState<string | null>(null);
  const [groupNewName, setGroupNewName] = useState('');

  const initialDevicesRef = React.useRef(initialDevices);
  useEffect(() => {
    initialDevicesRef.current = initialDevices;
  }, [initialDevices]);

  const loadDevices = useCallback(async (updatedSince?: string) => {
    try {
      const { data: { user } } = await supabase.auth.getUser();
      // If offline/unauthenticated, fallback to initial devices
      if (!user) {
        if (initialDevicesRef.current.length > 0) setDevices(initialDevicesRef.current);
        return;
      }

      let query = supabase
        .from('registered_devices')
        .select('device_mac, device_name, custom_name, product_type, position, group_name, created_at, updated_at')
        .eq('user_id', user.id);
        
      if (updatedSince) {
        query = query.gt('updated_at', updatedSince);
      }

      const { data: dbDevices, error } = await query.order('created_at', { ascending: false });

      if (error) throw error;

      if (dbDevices && dbDevices.length > 0) {
        setDevices(prev => {
           // For Delta Sync, we need to merge the new devices into the prev state if updatedSince is provided
           const patched: StoredDevice[] = updatedSince ? [...prev] : [];
           
           dbDevices.forEach((d: any) => {
             const newDev = {
               id: d.device_mac,
               name: d.device_name ?? d.device_mac,
               customName: d.custom_name ?? undefined,
               groupName: d.group_name ?? undefined,
               type: d.product_type ?? undefined,
               registeredAt: d.created_at,
             };
             
             if (updatedSince) {
               const existIdx = patched.findIndex(p => p.id === newDev.id);
               if (existIdx >= 0) patched[existIdx] = newDev;
               else patched.push(newDev);
             } else {
               patched.push(newDev);
             }
           });
           return patched;
        });
      } else if (initialDevicesRef.current.length > 0 && !updatedSince) {
        setDevices(initialDevicesRef.current);
      }
    } catch (err) {
      console.warn('[useDeviceFleet] Could not fetch cloud devices:', err);
      if (initialDevicesRef.current.length > 0) setDevices(initialDevicesRef.current);
    }
  }, []); // Empty dependencies to prevent re-render loop

  useEffect(() => {
    if (visible) {
      loadDevices();
    }
  }, [visible, loadDevices]);

  const handleRenameDevice = async (device: StoredDevice) => {
    if (!deviceNewName.trim()) return;
    const newName = deviceNewName.trim();
    onDeviceRenamed?.(device.id, newName);
    setDevices(prev => prev.map(d => d.id === device.id ? { ...d, customName: newName } : d));
    setEditingDeviceId(null);
    setDeviceNewName('');
    AppLogger.log('DEVICE_RENAMED', { deviceId: device.id, oldName: device.name, newName: newName });
  };

  const handleForgetDevice = (device: StoredDevice) => {
    Alert.alert(
      `Forget "${device.customName || device.name}"?`,
      'This removes it from your registered devices. You can always re-pair later.',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Forget', style: 'destructive',
          onPress: async () => {
            onDeviceForgotten?.(device.id);
            setDevices(prev => prev.filter(d => d.id !== device.id));
          },
        },
      ]
    );
  };

  const handleRenameGroup = (oldName: string) => {
    if (!groupNewName.trim()) return;
    const newName = groupNewName.trim();
    onGroupRenamed?.(oldName, newName);
    setDevices(prev => prev.map(d => d.groupName === oldName ? { ...d, groupName: newName } : d));
    setEditingGroupId(null);
    setGroupNewName('');
  };

  const handleForgetGroup = (groupName: string) => {
    Alert.alert(
      `Forget Group "${groupName}"?`,
      'This removes all devices within this group from your registered devices.',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Forget All', style: 'destructive',
          onPress: async () => {
            onGroupForgotten?.(groupName);
            setDevices(prev => prev.filter(d => d.groupName !== groupName));
          },
        },
      ]
    );
  };

  const groupedDevices = useMemo(() => {
    const groups: { [key: string]: StoredDevice[] } = { "_Ungrouped": [] };
    devices.forEach(d => {
      const gName = d.groupName || '';
      if (gName) {
        if (!groups[gName]) groups[gName] = [];
        groups[gName].push(d);
      } else {
        groups["_Ungrouped"].push(d);
      }
    });
    return groups;
  }, [devices]);

  return {
    devices,
    editingDeviceId,
    setEditingDeviceId,
    deviceNewName,
    setDeviceNewName,
    editingGroupId,
    setEditingGroupId,
    groupNewName,
    setGroupNewName,
    groupedDevices,
    handleRenameDevice,
    handleForgetDevice,
    handleRenameGroup,
    handleForgetGroup,
  };
}
