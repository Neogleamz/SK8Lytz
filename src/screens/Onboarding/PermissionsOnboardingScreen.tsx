import { MaterialCommunityIcons } from '@expo/vector-icons';
import React, { useCallback, useEffect, useRef, useState } from 'react';
import { ScrollView, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useTheme } from '../../context/ThemeContext';
import { requestPermission } from '../../services/PermissionService';
import { Layout, Spacing, ThemePalette } from '../../theme/theme';
import { AppLogger } from '../../services/AppLogger';

interface PermissionsOnboardingScreenProps {
  onComplete: () => void;
}

import GranularPermissionsList from '../../components/permissions/GranularPermissionsList';

export default function PermissionsOnboardingScreen({ onComplete }: PermissionsOnboardingScreenProps) {
  const { Colors, isDark } = useTheme();
  const insets = useSafeAreaInsets();
  const styles = createStyles(Colors, insets);

  const [allRequiredGranted, setAllRequiredGranted] = useState(false);
  // Ref used to trigger a re-check in GranularPermissionsList after auto-grant
  const [bleAutoRequestKey, setBleAutoRequestKey] = useState(0);
  const bleAutoFired = useRef(false);

  // ── Screen Navigation Telemetry + BLE Auto-Request ─────────────────────────
  useEffect(() => {
    AppLogger.log('SCREEN_OPENED', { screen: 'PermissionsOnboarding' });

    // Auto-fire the native BLE permission dialog on first mount.
    // BLE is the core required permission — removing manual friction is critical.
    if (!bleAutoFired.current) {
      bleAutoFired.current = true;
      requestPermission('BLUETOOTH')
        .then((granted) => {
          AppLogger.log('BLE_AUTO_REQUEST_RESULT', { granted });
          // Bump the key so GranularPermissionsList re-checks all statuses.
          setBleAutoRequestKey((k) => k + 1);
        })
        .catch(() => {
          // Silently fail — the list will show the manual CTA as fallback.
        });
    }
  }, []);

  return (
    <View style={styles.container}>
      <ScrollView contentContainerStyle={styles.scrollContent} showsVerticalScrollIndicator={false}>
        
        {/* Header */}
        <View style={styles.header}>
          <MaterialCommunityIcons name="shield-check" size={48} color={Colors.primary} style={{ marginBottom: Spacing.lg }} />
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

        <GranularPermissionsList
          onAllRequiredGranted={setAllRequiredGranted}
          refreshKey={bleAutoRequestKey}
        />

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
          disabled={!allRequiredGranted}
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
    paddingHorizontal: Spacing.xl,
    paddingBottom: Spacing.xxxl,
  },
  header: { marginBottom: Spacing.xxl },
  title: { fontSize: 32, fontWeight: '900', color: Colors.text, marginBottom: Spacing.sm, letterSpacing: -0.5 },
  subtitle: { fontSize: 16, color: Colors.textMuted, lineHeight: 24, marginBottom: Spacing.xl },
  privacyHero: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.04)',
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
    padding: Spacing.lg,
    borderRadius: Layout.borderRadius,
    gap: Spacing.md,
  },
  privacyText: { flex: 1, fontSize: 13, color: Colors.textMuted, lineHeight: 20 },
  // Removed legacy styles
  footer: {
    backgroundColor: Colors.background,
    borderTopWidth: 1, borderTopColor: Colors.surfaceHighlight,
    paddingHorizontal: Spacing.xl,
    paddingTop: Spacing.lg,
    paddingBottom: Math.max(insets.bottom + 16, 24),
  },
  footerWarning: {
    fontSize: 12, color: '#FF6B6B', textAlign: 'center', marginBottom: Spacing.lg,
  },
  continueButton: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: Spacing.md,
    backgroundColor: Colors.primary,
    paddingVertical: Spacing.lg,
    borderRadius: Layout.borderRadius,
  },
  continueButtonDisabled: {
    backgroundColor: Colors.surface,
    borderWidth: 1, borderColor: Colors.surfaceHighlight,
  },
  continueButtonText: { fontSize: 16, fontWeight: 'bold', color: '#000' },
  continueButtonTextDisabled: { color: Colors.textMuted },
});
