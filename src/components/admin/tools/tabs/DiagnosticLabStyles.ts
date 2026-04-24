import { Platform, StyleSheet } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useTheme } from '../../../../context/ThemeContext';
import { Spacing, Typography } from '../../../../theme/theme';

export function useDiagnosticLabStyles() {
  const { Colors, isDark } = useTheme();
  const insets = useSafeAreaInsets();
  
  const bg      = Colors.background;
  const cardBg  = Colors.surface;
  const txtPri  = Colors.text;
  const txtMuted= Colors.textMuted;
  const border  = Colors.surfaceHighlight;
  const cyan    = '#00f0ff';

  const styles = StyleSheet.create({
    root: { flex: 1 },
    header: { 
      flexDirection: 'row', 
      alignItems: 'center', 
      justifyContent: 'space-between', 
      paddingHorizontal: Spacing.lg, 
      paddingBottom: Spacing.lg, 
      borderBottomWidth: 1 
    },
    backBtn: { 
      marginRight: Spacing.lg,
      padding: Spacing.xs,
    },
    title: { color: '#FFF', fontSize: 18, fontWeight: '900', letterSpacing: 1.5 },
    tabBar: { flexDirection: 'row', borderBottomWidth: 1 },
    tabBtn: { 
      flex: 1, 
      paddingVertical: Spacing.lg, 
      alignItems: 'center', 
      borderBottomWidth: 2, 
      borderBottomColor: 'transparent' 
    },
    tabBtnActive: { borderBottomColor: cyan },
    tabBtnTxt: { fontSize: 10, fontWeight: '900', letterSpacing: 0.5 },
    tabBtnTxtActive: { color: cyan },
    content: { flex: 1, padding: Spacing.lg },
    sectionTitle: { fontSize: 12, fontWeight: '900', letterSpacing: 1, marginBottom: Spacing.sm },
    subTitle: { fontSize: 10, fontWeight: 'bold', letterSpacing: 1, marginBottom: Spacing.md, marginTop: Spacing.xl },
    hint: { fontSize: 11, lineHeight: 16, marginBottom: Spacing.md },
    hwBadge: { 
      flexDirection: 'row', 
      flexWrap: 'wrap', 
      backgroundColor: cardBg, 
      padding: Spacing.md, 
      borderRadius: 12, 
      marginBottom: Spacing.xl, 
      borderWidth: 1, 
      borderColor: border, 
      alignItems: 'center' 
    },
    hwBadgeLabel: { color: txtMuted, fontSize: 11 },
    hwBadgeVal: { fontSize: 11, fontWeight: 'bold' },
    colorBtnRow: { flexDirection: 'row', gap: Spacing.md, marginBottom: Spacing.xl },
    bigColorBtn: { 
      flex: 1, 
      paddingVertical: Spacing.xl, 
      alignItems: 'center', 
      justifyContent: 'center', 
      borderRadius: 12, 
      borderWidth: 1.5 
    },
    diagBox: { 
      backgroundColor: cardBg, 
      borderWidth: 1, 
      borderColor: border, 
      borderRadius: 12, 
      padding: Spacing.lg, 
      marginBottom: Spacing.md 
    },
    diagLine: { fontSize: 11, lineHeight: 20 },
    sentBox: { 
      backgroundColor: isDark ? '#05070a' : '#f9fafb', 
      borderWidth: 1, 
      borderColor: border, 
      borderRadius: 12, 
      padding: Spacing.lg, 
      marginTop: Spacing.sm 
    },
    transBtn: { 
      flexDirection: 'row', 
      alignItems: 'center', 
      backgroundColor: cardBg, 
      borderWidth: 1, 
      borderColor: border, 
      borderRadius: 12, 
      padding: Spacing.lg, 
      marginBottom: Spacing.md 
    },
    transByteBadge: { 
      width: 52, 
      height: 52, 
      alignItems: 'center', 
      justifyContent: 'center', 
      borderRadius: 10, 
      borderWidth: 1 
    },
    annotationsBox: { 
      backgroundColor: isDark ? '#05070a' : '#f9fafb', 
      borderWidth: 1, 
      borderColor: border, 
      borderRadius: 12, 
      padding: Spacing.lg, 
      marginBottom: Spacing.md 
    },
    numInput: { 
      backgroundColor: cardBg, 
      color: txtPri, 
      borderWidth: 1, 
      borderColor: border, 
      borderRadius: 10, 
      padding: Spacing.md, 
      fontSize: 14, 
      textAlign: 'center' 
    },
    hexInput: { 
      backgroundColor: isDark ? '#05070a' : '#f9fafb', 
      color: cyan, 
      borderWidth: 1, 
      borderColor: border, 
      borderRadius: 12, 
      padding: Spacing.lg, 
      fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', 
      fontSize: 11, 
      minHeight: 100 
    },
    chip: { 
      paddingHorizontal: Spacing.lg, 
      paddingVertical: Spacing.sm, 
      borderRadius: 20, 
      backgroundColor: cardBg, 
      borderWidth: 1, 
      borderColor: border 
    },
    chipActive: { 
      backgroundColor: cyan + '22', 
      borderColor: cyan 
    },
    txBtn: { 
      backgroundColor: cyan, 
      justifyContent: 'center', 
      alignItems: 'center', 
      paddingVertical: Spacing.lg, 
      borderRadius: 12, 
      marginTop: Spacing.md 
    },
    presetBtn: { 
      backgroundColor: cardBg, 
      borderWidth: 1, 
      borderColor: border, 
      borderRadius: 12, 
      padding: Spacing.lg 
    },
  });

  return { Colors, isDark, insets, bg, cardBg, txtPri, txtMuted, border, cyan, S: styles };
}
