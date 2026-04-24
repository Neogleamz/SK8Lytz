import { StyleSheet } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useTheme } from '../../context/ThemeContext';
import { Layout, Spacing, ThemePalette } from '../../theme/theme';

export function useAuthStyles() {
  const { Colors } = useTheme();
  const insets = useSafeAreaInsets();
  
  return StyleSheet.create({
    container: { flex: 1, backgroundColor: Colors.background },
    scrollContent: {
      flex: 1, justifyContent: 'center',
      paddingHorizontal: Spacing.xl,
      paddingTop: Math.max(insets.top + 60, 80),
      paddingBottom: Spacing.xl,
    },
    topButtons: {
      position: 'absolute', top: Math.max(insets.top + 10, 20), right: 16,
      flexDirection: 'row', gap: Spacing.sm, zIndex: 10,
    },
    topLeftButtons: {
      position: 'absolute', top: Math.max(insets.top + 10, 20), left: 16,
      flexDirection: 'row', gap: Spacing.sm, zIndex: 10,
    },
    topLeftBtn: {
      flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: Spacing.sm,
      paddingVertical: Spacing.sm, paddingHorizontal: Spacing.md, borderRadius: 20, borderWidth: 1,
    },
    topBtn: {
      width: 34, height: 34, borderRadius: 17,
      backgroundColor: 'rgba(255,255,255,0.07)',
      borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)',
      alignItems: 'center', justifyContent: 'center',
    },
    headerContainer: { alignItems: 'center', marginBottom: Spacing.xxxl },
    title: { fontSize: 42, fontWeight: '900', color: Colors.text, letterSpacing: -1, marginBottom: Spacing.sm },
    subtitle: { color: Colors.textMuted, fontSize: 14, textAlign: 'center' },
    formContainer: { marginBottom: Spacing.xl },
    input: {
      backgroundColor: Colors.surface,
      color: Colors.text,
      padding: Spacing.lg,
      borderRadius: Layout.borderRadius,
      fontSize: 15,
      marginBottom: Spacing.md,
      borderWidth: 1,
      borderColor: Colors.surfaceHighlight,
    },
    forgotPasswordContainer: { alignItems: 'flex-end', marginBottom: Spacing.lg, marginTop: -4 },
    forgotPasswordText: { color: Colors.primary, fontSize: 13 },
    checkbox: {
      width: 18, height: 18, borderRadius: 4,
      borderWidth: 1.5, borderColor: Colors.textMuted,
      alignItems: 'center', justifyContent: 'center',
      backgroundColor: 'transparent',
    },
    errorBanner: {
      flexDirection: 'row',
      alignItems: 'flex-start',
      backgroundColor: 'rgba(255, 107, 107, 0.12)',
      borderWidth: 1,
      borderColor: 'rgba(255, 107, 107, 0.3)',
      borderRadius: 10,
      paddingHorizontal: Spacing.md,
      paddingVertical: Spacing.md,
      marginBottom: Spacing.md,
    },
    errorBannerText: {
      color: '#FF6B6B',
      fontSize: 13,
      lineHeight: 18,
      flex: 1,
    },
    successBanner: {
      flexDirection: 'row',
      alignItems: 'flex-start',
      backgroundColor: 'rgba(74, 222, 128, 0.12)',
      borderWidth: 1,
      borderColor: 'rgba(74, 222, 128, 0.3)',
      borderRadius: 10,
      paddingHorizontal: Spacing.md,
      paddingVertical: Spacing.md,
      marginBottom: Spacing.md,
    },
    successBannerText: {
      color: '#4ADE80',
      fontSize: 13,
      lineHeight: 18,
      flex: 1,
    },
    primaryButton: {
      backgroundColor: Colors.primary,
      padding: Spacing.lg,
      borderRadius: Layout.borderRadius,
      alignItems: 'center',
      marginBottom: Spacing.md,
    },
    primaryButtonText: { color: '#000', fontWeight: 'bold', fontSize: 16 },
    secondaryButton: {
      borderWidth: 1,
      borderColor: Colors.surfaceHighlight,
      padding: Spacing.lg,
      borderRadius: Layout.borderRadius,
      alignItems: 'center',
    },
    secondaryButtonText: { color: Colors.text, fontWeight: 'bold', fontSize: 15 },
    magicLinkButton: {
      alignItems: 'center',
      paddingVertical: Spacing.md,
      marginBottom: Spacing.xs,
    },
    magicLinkText: { color: Colors.primary, fontSize: 13, fontWeight: '600' },
    toggleContainer: { flexDirection: 'row', justifyContent: 'center', marginTop: Spacing.sm },
    toggleText: { color: Colors.textMuted, fontSize: 14 },
    toggleLink: { color: Colors.primary, fontSize: 14, fontWeight: 'bold' },
    offlineButton: {
      alignSelf: 'center',
      alignItems: 'center',
      paddingVertical: Spacing.sm,
      paddingHorizontal: Spacing.xl,
      borderRadius: Layout.borderRadius,
      borderWidth: 1,
      borderColor: 'rgba(255,255,255,0.1)',
      backgroundColor: 'rgba(255,255,255,0.04)',
    },
    offlineButtonText: { color: Colors.textMuted, fontSize: 13, fontWeight: '600' },
  });
}
