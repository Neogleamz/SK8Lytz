import { MaterialCommunityIcons } from '@expo/vector-icons';
import React, { useEffect, useState } from 'react';
import { Alert, Linking, StyleSheet, Switch, Text, TouchableOpacity, View } from 'react-native';
import { useTheme } from '../../context/ThemeContext';
import { checkPermission, PermissionType, requestPermission, setPermissionOptOut } from '../../services/PermissionService';
import { Layout, Spacing, ThemePalette } from '../../theme/theme';

export interface PermissionItem {
  id: PermissionType;
  title: string;
  icon: keyof typeof MaterialCommunityIcons.glyphMap;
  description: string;
  required: boolean;
  disabledFeature: string;
}

export const PERMISSIONS_LIST: PermissionItem[] = [
  {
    id: 'BLUETOOTH',
    title: 'Heartbeat (Bluetooth)',
    icon: 'bluetooth',
    description: 'Connects the app to your skates. The app cannot function without this.',
    required: true,
    disabledFeature: 'Full app lockout',
  },
  {
    id: 'LOCATION',
    title: 'The Roll Call',
    icon: 'map-marker-outline',
    description: 'Find nearby skate spots and discover local crews.',
    required: false,
    disabledFeature: 'Discover Feed / Skate Map',
  },
  {
    id: 'CAMERA',
    title: 'Match your Fit',
    icon: 'camera-outline',
    description: 'Unlock Camera Mode to apply real-world colors to your skates.',
    required: false,
    disabledFeature: 'Camera Mode',
  },
  {
    id: 'MIC',
    title: 'Feel the Beat',
    icon: 'microphone-outline',
    description: 'Allows Music Mode to sync your lights to the ambient room audio or DJ.',
    required: false,
    disabledFeature: 'Live Music Sync',
  },
  {
    id: 'NOTIFICATIONS',
    title: 'Stay Connected',
    icon: 'bell-outline',
    description: 'Get alerts when your crew starts a live session near you.',
    required: false,
    disabledFeature: 'Crew Session Invites',
  }
];

interface GranularPermissionsListProps {
  onAllRequiredGranted?: (granted: boolean) => void;
  readOnly?: boolean;
}

export default function GranularPermissionsList({ onAllRequiredGranted, readOnly = false }: GranularPermissionsListProps) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);

  const [statuses, setStatuses] = useState<Record<PermissionType, boolean | null>>({
    BLUETOOTH: null,
    NOTIFICATIONS: null,
    LOCATION: null,
    CAMERA: null,
    MIC: null,
  });

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let active = true;
    const init = async () => {
      const results: Record<PermissionType, boolean | null> = { ...statuses };
      for (const item of PERMISSIONS_LIST) {
        results[item.id] = await checkPermission(item.id);
      }
      if (active) {
        setStatuses(results);
        setLoading(false);
      }
    };
    init();
    return () => { active = false; };
  }, []);

  useEffect(() => {
    if (onAllRequiredGranted && !loading) {
      const allRequiredGranted = PERMISSIONS_LIST.filter(p => p.required).every(p => statuses[p.id] === true);
      onAllRequiredGranted(allRequiredGranted);
    }
  }, [statuses, loading, onAllRequiredGranted]);

  const handleToggle = async (type: PermissionType, newValue: boolean) => {
    if (readOnly) return;
    
    // Optimistic UI
    setStatuses(prev => ({ ...prev, [type]: newValue }));

    if (newValue) {
      // User is turning it ON
      // Try to request native permission. The OS will handle the actual prompt.
      const isGrantedNative = await requestPermission(type);
      
      if (isGrantedNative) {
        await setPermissionOptOut(type, false);
      } else {
        // The OS violently denied it
        setStatuses(prev => ({ ...prev, [type]: false }));
        Alert.alert(
          'Permission Denied',
          'This permission was permanently disabled in your device settings. You must enable it manually to use this feature.',
          [
            { text: 'Cancel', style: 'cancel' },
            { text: 'Open Settings', onPress: () => Linking.openSettings() }
          ]
        );
      }
    } else {
      // User is turning it OFF
      // This is our soft-revoke opt-out
      await setPermissionOptOut(type, true);
    }
  };

  if (loading) {
    return (
      <View style={styles.loadingContainer}>
        <Text style={{ color: Colors.textMuted }}>Checking permissions...</Text>
      </View>
    );
  }

  return (
    <View style={styles.listContainer}>
      {PERMISSIONS_LIST.map((item) => {
        const granted = statuses[item.id] === true;
        
        return (
          <View key={item.id} style={[styles.card, granted && styles.cardGranted]}>
            <View style={styles.cardHeader}>
              <View style={styles.iconBox}>
                <MaterialCommunityIcons name={item.icon} size={24} color={granted ? Colors.primary : Colors.text} />
              </View>
              <View style={styles.cardInfo}>
                <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm }}>
                  <Text style={styles.cardTitle}>{item.title}</Text>
                  {item.required && <Text style={styles.badgeRequired}>REQUIRED</Text>}
                </View>
                <Text style={styles.cardDesc}>{item.description}</Text>
                {!granted && !item.required && (
                  <Text style={styles.disabledWarning}>
                    <MaterialCommunityIcons name="minus-circle-outline" size={10} /> Opting out disables: {item.disabledFeature}
                  </Text>
                )}
              </View>
            </View>
            
            <View style={styles.toggleRow}>
              <Text style={[styles.toggleBtnText, granted && styles.toggleBtnTextGranted]}>
                {granted ? '✓ GRANTED' : 'ALLOW ACCESS'}
              </Text>
              
              <Switch
                trackColor={{ false: Colors.surfaceHighlight, true: Colors.primary }}
                thumbColor={granted ? '#fff' : '#f4f3f4'}
                ios_backgroundColor={Colors.surfaceHighlight}
                onValueChange={(val) => handleToggle(item.id, val)}
                value={granted}
                disabled={readOnly || (item.required && granted)} // Prevent turning off required
              />
            </View>
          </View>
        );
      })}
    </View>
  );
}

const createStyles = (Colors: ThemePalette) => StyleSheet.create({
  listContainer: { gap: Spacing.lg },
  loadingContainer: { height: 200, justifyContent: 'center', alignItems: 'center' },
  card: {
    backgroundColor: Colors.surface,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
    borderRadius: Layout.borderRadius,
    padding: Spacing.lg,
  },
  cardGranted: {
    borderColor: 'rgba(74, 222, 128, 0.3)',
    backgroundColor: 'rgba(74, 222, 128, 0.03)',
  },
  cardHeader: { flexDirection: 'row', gap: Spacing.lg, marginBottom: Spacing.md },
  iconBox: {
    width: 48, height: 48,
    borderRadius: 24,
    backgroundColor: 'rgba(255,255,255,0.05)',
    justifyContent: 'center', alignItems: 'center'
  },
  cardInfo: { flex: 1 },
  cardTitle: { fontSize: 16, fontWeight: 'bold', color: Colors.text, marginBottom: Spacing.xs },
  cardDesc: { fontSize: 13, color: Colors.textMuted, lineHeight: 18, marginBottom: Spacing.sm },
  badgeRequired: {
    fontSize: 10, fontWeight: '800',
    backgroundColor: 'rgba(255, 107, 107, 0.2)', color: '#FF6B6B',
    paddingHorizontal: Spacing.sm, paddingVertical: Spacing.xxs, borderRadius: 4, overflow: 'hidden'
  },
  disabledWarning: {
    fontSize: 11, color: '#FFB86C', fontStyle: 'italic',
  },
  toggleRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    backgroundColor: 'rgba(255,255,255,0.03)',
    padding: Spacing.md,
    borderRadius: 8,
    marginTop: Spacing.xs
  },
  toggleBtnText: { fontSize: 13, fontWeight: 'bold', color: Colors.text, letterSpacing: 1 },
  toggleBtnTextGranted: { color: '#4ADE80' },
});
