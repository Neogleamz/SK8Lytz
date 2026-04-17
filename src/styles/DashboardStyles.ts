import { StyleSheet } from 'react-native';
import type { ThemePalette } from '../theme/theme';
import { Layout, Spacing } from '../theme/theme';

/**
 * Utility to generate premium gradient colors based on pattern name/state
 */
export const getPatternColors = (patternName?: string, Colors?: ThemePalette) => {
  if (!patternName) return [Colors?.primary || '#00F0FF', Colors?.secondary || '#7000FF'];
  
  const name = patternName.toLowerCase();
  // ... (Implementation remains the same)
  if (name.includes('fire') || name.includes('flame')) return ['#FF4D00', '#FF9E00'];
  if (name.includes('water') || name.includes('ocean')) return ['#00B2FF', '#00FFF0'];
  if (name.includes('forest') || name.includes('nature')) return ['#00FF85', '#00A3FF'];
  if (name.includes('sunset') || name.includes('gold')) return ['#FFD600', '#FF00E5'];
  if (name.includes('nebula') || name.includes('space')) return ['#7000FF', '#00FFFF'];
  if (name.includes('neon') || name.includes('cyber')) return ['#FF00E5', '#00F0FF'];
  if (name.includes('police')) return ['#FF0000', '#0000FF'];
  if (name.includes('matrix')) return ['#00FF00', '#003300'];
  
  // Default to branding colors
  return [Colors?.primary || '#00F0FF', Colors?.secondary || '#7000FF'];
};

export const createDashboardStyles = (Colors: ThemePalette, windowHeight: number = 800, windowWidth: number = 400) => {
  const isShort = windowHeight < 720;
  const isVeryShort = windowHeight < 640;
  const isNarrow = windowWidth < 360;

  return StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: Colors.background,
  },
  container: {
    flex: 1,
  },
  card: {
    backgroundColor: Colors.surface,
    borderRadius: Layout.borderRadius,
    padding: Spacing.lg,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.1,
    shadowRadius: 12,
    elevation: 10,
  },
  scanButton: {
    marginTop: Spacing.xl,
    backgroundColor: Colors.primary,
    paddingVertical: Spacing.lg,
    borderRadius: Layout.borderRadius,
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.6,
    shadowRadius: 8,
    elevation: 8,
  },
  scanButtonText: {
    color: Colors.isDark ? Colors.text : '#FFF',
    fontSize: 16,
    fontWeight: '800',
    letterSpacing: 1,
  },
  emptyStateContainer: {
    marginTop: Spacing.xxxl,
    alignItems: 'center',
  },
  disconnectButtonSmall: {
    backgroundColor: Colors.surfaceHighlight,
    paddingVertical: Spacing.sm,
    paddingHorizontal: Spacing.md,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: Colors.error + '44',
  },
  disconnectButtonTextSmall: {
    color: Colors.error,
    fontSize: 12,
    fontWeight: 'bold',
  },
  groupButton: {
    backgroundColor: Colors.secondary,
    paddingVertical: Spacing.lg,
    borderRadius: Layout.borderRadius,
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: Colors.secondary,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.6,
    shadowRadius: 8,
    elevation: 8,
  },
  groupButtonText: {
    color: Colors.isDark ? '#FFF' : Colors.background,
    fontSize: 16,
    fontWeight: '800',
    letterSpacing: 1,
  },
  countdownBadge: {
    position: 'absolute',
    right: -10,
    top: -10,
    width: 26,
    height: 26,
    borderRadius: 13,
    backgroundColor: '#FF7000',
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: '#FF7000',
    shadowRadius: 8,
    shadowOpacity: 1,
    zIndex: 50,
  },
  countdownText: {
    color: '#FFFFFF',
    fontSize: 14,
    fontWeight: '900',
  },
  errorContainer: {
    marginTop: Spacing.lg,
    padding: Spacing.md,
    backgroundColor: 'rgba(255, 61, 0, 0.1)',
    borderRadius: 8,
    borderWidth: 1,
    borderColor: Colors.error
  },
  /* ──── 4-SLAB DASHBOARD STYLES ──── */
  headerSlab: {
    paddingBottom: Spacing.xs,
    backgroundColor: 'rgba(0,0,0,0.5)',
    borderBottomWidth: 1,
    borderBottomColor: 'rgba(0,240,255,0.1)',
  },
  slabContainer: {
    paddingHorizontal: Layout.padding,
    marginBottom: isVeryShort ? 8 : isShort ? 16 : 24,
  },
  slabHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: Spacing.md,
    paddingHorizontal: Spacing.xs,
  },
  slabTitle: {
    fontSize: 13,
    fontWeight: '900',
    color: 'rgba(255,255,255,0.6)',
    letterSpacing: 1.5,
    fontFamily: 'Righteous',
  },
  slabAction: {
    paddingHorizontal: Spacing.md,
    paddingVertical: Spacing.xs,
    borderRadius: 4,
    backgroundColor: 'rgba(255,170,0,0.1)',
    borderWidth: 1,
    borderColor: 'rgba(255,170,0,0.3)',
  },
  slabActionText: {
    fontSize: 10,
    fontWeight: '900',
    color: '#FFAA00',
    letterSpacing: 0.5,
  },
  glassSlab: {
    backgroundColor: 'rgba(255,255,255,0.03)',
    borderRadius: 16,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.08)',
    padding: isShort ? 12 : 16,
  },
  slabEmptyText: {
    fontSize: 12,
    color: 'rgba(255,255,255,0.4)',
    textAlign: 'center',
    lineHeight: 18,
  },
  activeCrewPill: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.05)',
    borderRadius: 12,
    padding: Spacing.md,
    gap: Spacing.md,
  },
  statusDot: {
    width: 8,
    height: 8,
    borderRadius: 4,
    shadowOpacity: 1,
    shadowRadius: 4,
    elevation: 4,
  },
  activeCrewText: {
    fontSize: 13,
    fontWeight: '800',
    flex: 1,
    letterSpacing: 0.5,
  },
  skateCardWrapper: {
    marginBottom: Spacing.lg,
    borderRadius: 20,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 10 },
    shadowOpacity: 0.3,
    shadowRadius: 15,
    elevation: 12,
  },
  skateCardGradient: {
    borderRadius: 20,
    padding: Spacing.xxs, // Border thickness
  },
  skateCardInner: {
    backgroundColor: Colors.isDark ? 'rgba(35, 42, 55, 0.85)' : 'rgba(255, 255, 255, 0.95)',
    borderRadius: 18,
    padding: Spacing.lg,
    overflow: 'hidden',
  },
  skateCardRefraction: {
    position: 'absolute',
    top: -50,
    left: -50,
    width: 200,
    height: 200,
    backgroundColor: 'rgba(255, 255, 255, 0.03)',
    transform: [{ rotate: '45deg' }],
  },
  skateCardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: Spacing.lg,
  },
  avatarPill: {
    backgroundColor: 'rgba(255,255,255,0.05)',
    paddingHorizontal: Spacing.sm,
    paddingVertical: Spacing.xs,
    borderRadius: 20,
    flexDirection: 'row',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
  },
  avatarPillImage: {
    width: 20,
    height: 20,
  },
  avatarStatusDot: {
    width: 6,
    height: 6,
    borderRadius: 3,
    backgroundColor: Colors.success,
    marginLeft: Spacing.sm,
    borderWidth: 1,
    borderColor: Colors.background,
  },
  telemetryContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Spacing.md,
  },
  telemetryItem: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Spacing.xs,
  },
  rssiBars: {
    flexDirection: 'row',
    alignItems: 'flex-end',
    gap: Spacing.xxs,
  },
  rssiBar: {
    width: 3,
    borderRadius: 1,
  },
  skateCardContent: {
    marginBottom: Spacing.lg,
  },
  skateCardGroupName: {
    fontSize: isVeryShort ? 18 : isShort ? 20 : 22,
    fontWeight: '900',
    color: Colors.text,
    fontFamily: 'Righteous',
    letterSpacing: 0.5,
  },
  patternPill: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: Spacing.xs,
    backgroundColor: 'rgba(255,255,255,0.03)',
    alignSelf: 'flex-start',
    paddingHorizontal: Spacing.sm,
    paddingVertical: Spacing.xxs,
    borderRadius: 10,
  },
  patternDot: {
    width: 6,
    height: 6,
    borderRadius: 3,
    marginRight: Spacing.sm,
  },
  patternName: {
    fontSize: 10,
    fontWeight: '800',
    color: Colors.textMuted,
    letterSpacing: 1,
    textTransform: 'uppercase',
  },
  skateCardFooter: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    borderTopWidth: 1,
    borderTopColor: 'rgba(255,255,255,0.05)',
    paddingTop: Spacing.md,
  },
  deviceCountText: {
    fontSize: 10,
    fontWeight: '700',
    color: Colors.textMuted,
    letterSpacing: 1,
  },
  powerIconCircle: {
    width: 28,
    height: 28,
    borderRadius: 14,
    alignItems: 'center',
    justifyContent: 'center',
  },
  deviceListFixed: {
    backgroundColor: 'rgba(255,255,255,0.02)',
    borderRadius: 16,
    padding: isShort ? 4 : 8,
  }
});
};
