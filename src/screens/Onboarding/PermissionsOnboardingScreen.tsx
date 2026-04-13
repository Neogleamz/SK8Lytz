import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView, Animated } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../../context/ThemeContext';
import { Layout, ThemePalette } from '../../theme/theme';
import { requestPermission, checkPermission, PermissionType } from '../../services/PermissionService';

interface PermissionsOnboardingScreenProps {
  onComplete: () => void;
}

interface PermissionItem {
  id: PermissionType;
  title: string;
  icon: keyof typeof MaterialCommunityIcons.glyphMap;
  description: string;
  required: boolean;
  disabledFeature: string;
}

const PERMISSIONS_LIST: PermissionItem[] = [
  {
    id: 'BLUETOOTH',
    title: 'Heartbeat (Bluetooth)',
    icon: 'bluetooth',
    description: 'Connects the app to your skates. The app cannot function without this.',
    required: true,
    disabledFeature: 'Full app lockout',
  },
  {
    id: 'NOTIFICATIONS',
    title: 'Stay Connected',
    icon: 'bell-outline',
    description: 'Get alerts when your crew starts a live session near you.',
    required: false,
    disabledFeature: 'Crew Session Invites',
  },
  {
    id: 'LOCATION',
    title: 'The Roll Call',
    icon: 'map-marker-outline',
    description: 'Find nearby skate spots and discover local crews. (Only used while using the app)',
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
  }
];

export default function PermissionsOnboardingScreen({ onComplete }: PermissionsOnboardingScreenProps) {
  const { Colors, isDark } = useTheme();
  const insets = useSafeAreaInsets();
  const styles = createStyles(Colors, insets);

  const [statuses, setStatuses] = useState<Record<PermissionType, boolean | null>>({
    BLUETOOTH: null,
    NOTIFICATIONS: null,
    LOCATION: null,
    CAMERA: null,
    MIC: null,
  });

  const [loading, setLoading] = useState(true);

  // Check initial statuses
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

  const handleRequest = async (type: PermissionType) => {
    const isGranted = await requestPermission(type);
    setStatuses(prev => ({ ...prev, [type]: isGranted }));
  };

  const allRequiredGranted = PERMISSIONS_LIST.filter(p => p.required).every(p => statuses[p.id] === true);

  return (
    <View style={styles.container}>
      <ScrollView contentContainerStyle={styles.scrollContent} showsVerticalScrollIndicator={false}>
        
        {/* Header */}
        <View style={styles.header}>
          <MaterialCommunityIcons name="shield-check" size={48} color={Colors.primary} style={{ marginBottom: 16 }} />
          <Text style={styles.title}>Power Up SK8Lytz</Text>
          <Text style={styles.subtitle}>
            We just make cool skate apps. You have full control over what this app can access.
          </Text>
          
          <View style={styles.privacyHero}>
            <MaterialCommunityIcons name="lock-outline" size={16} color={Colors.textMuted} />
            <Text style={styles.privacyText}>
              No data sold. No trackers. Just SK8Lytz. We only use telemetry to make your lights smarter.
            </Text>
          </View>
        </View>

        {loading ? (
          <View style={styles.loadingContainer}>
            <Text style={{ color: Colors.textMuted }}>Checking permissions...</Text>
          </View>
        ) : (
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
                      <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
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
                  
                  <TouchableOpacity 
                    style={[styles.toggleBtn, granted ? styles.toggleBtnGranted : styles.toggleBtnPending]}
                    onPress={() => !granted && handleRequest(item.id)}
                    activeOpacity={granted ? 1 : 0.7}
                  >
                    <Text style={[styles.toggleBtnText, granted && styles.toggleBtnTextGranted]}>
                      {granted ? '✓ GRANTED' : 'ALLOW ACCESS'}
                    </Text>
                  </TouchableOpacity>
                </View>
              );
            })}
          </View>
        )}

      </ScrollView>

      {/* Footer / Continue */}
      <View style={styles.footer}>
        {!allRequiredGranted && (
          <Text style={styles.footerWarning}>
            You must grant required permissions (Bluetooth) to continue.
          </Text>
        )}
        <TouchableOpacity 
          style={[styles.continueButton, !allRequiredGranted && styles.continueButtonDisabled]}
          disabled={!allRequiredGranted || loading}
          onPress={onComplete}
        >
          <Text style={[styles.continueButtonText, !allRequiredGranted && styles.continueButtonTextDisabled]}>
            Continue to App
          </Text>
          <MaterialCommunityIcons name="arrow-right" size={20} color={allRequiredGranted ? '#000' : Colors.textMuted} />
        </TouchableOpacity>
      </View>
    </View>
  );
}

const createStyles = (Colors: ThemePalette, insets: any) => StyleSheet.create({
  container: { flex: 1, backgroundColor: Colors.background },
  scrollContent: {
    paddingTop: Math.max(insets.top + 24, 48),
    paddingHorizontal: 24,
    paddingBottom: 40,
  },
  header: { marginBottom: 32 },
  title: { fontSize: 32, fontWeight: '900', color: Colors.text, marginBottom: 8, letterSpacing: -0.5 },
  subtitle: { fontSize: 16, color: Colors.textMuted, lineHeight: 24, marginBottom: 24 },
  privacyHero: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.04)',
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
    padding: 16,
    borderRadius: Layout.borderRadius,
    gap: 12,
  },
  privacyText: { flex: 1, fontSize: 13, color: Colors.textMuted, lineHeight: 20 },
  listContainer: { gap: 16 },
  loadingContainer: { height: 200, justifyContent: 'center', alignItems: 'center' },
  card: {
    backgroundColor: Colors.surface,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
    borderRadius: Layout.borderRadius,
    padding: 16,
  },
  cardGranted: {
    borderColor: 'rgba(74, 222, 128, 0.3)',
    backgroundColor: 'rgba(74, 222, 128, 0.03)',
  },
  cardHeader: { flexDirection: 'row', gap: 16, marginBottom: 16 },
  iconBox: {
    width: 48, height: 48,
    borderRadius: 24,
    backgroundColor: 'rgba(255,255,255,0.05)',
    justifyContent: 'center', alignItems: 'center'
  },
  cardInfo: { flex: 1 },
  cardTitle: { fontSize: 16, fontWeight: 'bold', color: Colors.text, marginBottom: 4 },
  cardDesc: { fontSize: 13, color: Colors.textMuted, lineHeight: 18, marginBottom: 8 },
  badgeRequired: {
    fontSize: 10, fontWeight: '800',
    backgroundColor: 'rgba(255, 107, 107, 0.2)', color: '#FF6B6B',
    paddingHorizontal: 6, paddingVertical: 2, borderRadius: 4, overflow: 'hidden'
  },
  disabledWarning: {
    fontSize: 11, color: '#FFB86C', fontStyle: 'italic',
  },
  toggleBtn: {
    width: '100%', paddingVertical: 12, borderRadius: 8, alignItems: 'center', borderWidth: 1,
  },
  toggleBtnPending: {
    backgroundColor: 'rgba(255,255,255,0.05)',
    borderColor: Colors.surfaceHighlight,
  },
  toggleBtnGranted: {
    backgroundColor: 'rgba(74, 222, 128, 0.1)',
    borderColor: 'transparent',
  },
  toggleBtnText: { fontSize: 13, fontWeight: 'bold', color: Colors.text, letterSpacing: 1 },
  toggleBtnTextGranted: { color: '#4ADE80' },
  footer: {
    backgroundColor: Colors.background,
    borderTopWidth: 1, borderTopColor: Colors.surfaceHighlight,
    paddingHorizontal: 24,
    paddingTop: 16,
    paddingBottom: Math.max(insets.bottom + 16, 24),
  },
  footerWarning: {
    fontSize: 12, color: '#FF6B6B', textAlign: 'center', marginBottom: 16,
  },
  continueButton: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 12,
    backgroundColor: Colors.primary,
    paddingVertical: 16,
    borderRadius: Layout.borderRadius,
  },
  continueButtonDisabled: {
    backgroundColor: Colors.surface,
    borderWidth: 1, borderColor: Colors.surfaceHighlight,
  },
  continueButtonText: { fontSize: 16, fontWeight: 'bold', color: '#000' },
  continueButtonTextDisabled: { color: Colors.textMuted },
});
