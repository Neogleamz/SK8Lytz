/**
 * DashboardStyles — static StyleSheet definitions.
 *
 * PERF FIX (THEME-001): All styles are now created ONCE at module load time
 * via a top-level StyleSheet.create call. The former createDashboardStyles()
 * factory was called on every render cycle (up to 20Hz during telemetry),
 * causing severe GC pressure. Static creation eliminates that entirely.
 *
 * NOTE on dimension-dependent styles: A handful of styles need runtime window
 * dimensions (slabContainer, glassSlab, skateCardGroupName, deviceListFixed,
 * flexibleSpacer). These are kept as inline-style helpers exported below
 * (getDimensionStyles) so only those values are computed per render, not the
 * entire StyleSheet. All other ~40 style keys are static.
 *
 * THEME-003 FIX: getPatternColors has been moved to src/utils/patternColors.ts.
 * It is re-exported here for backward compatibility.
 */

import { StyleSheet } from 'react-native';
import type { ThemePalette } from '../theme/theme';
import { Colors, Layout, Spacing, Shadows } from '../theme/theme';

// Re-export for backward compatibility — consumers should import from utils/patternColors
export { getPatternColors } from '../utils/patternColors';

// ─────────────────────────────────────────────────────────────────────────────
// Static styles — created ONCE at module load (THEME-001 fix)
// ─────────────────────────────────────────────────────────────────────────────
export const DashboardStyles = StyleSheet.create({
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
    // THEME-002 fix: Platform-aware shadow via Shadows token
    ...Shadows.soft,
  },
  scanButton: {
    marginTop: Spacing.xl,
    backgroundColor: Colors.primary,
    paddingVertical: Spacing.lg,
    borderRadius: Layout.borderRadius,
    alignItems: 'center',
    justifyContent: 'center',
    // Glow shadow applied via Shadows.glow(Colors.primary) at call site
    // (Shadows.glow is a function taking a color arg — cannot be static spread)
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
    elevation: 8,
  },
  groupButtonText: {
    color: Colors.isDark ? '#FFF' : Colors.background,
    fontSize: 16,
    fontWeight: '800',
    letterSpacing: 1,
  },
  // THEME-004 fix: replaced '#FF7000' with Colors.warning token
  countdownBadge: {
    position: 'absolute',
    right: -10,
    top: -10,
    width: 26,
    height: 26,
    borderRadius: 13,
    backgroundColor: Colors.warning,
    alignItems: 'center',
    justifyContent: 'center',
    zIndex: 50,
    elevation: 4,
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
    borderColor: Colors.error,
  },
  /* ──── 4-SLAB DASHBOARD STYLES ──── */
  headerSlab: {
    paddingBottom: Spacing.xs,
    backgroundColor: 'rgba(0,0,0,0.5)',
    borderBottomWidth: 1,
    borderBottomColor: 'rgba(0,240,255,0.1)',
    zIndex: 100,
    elevation: 100,
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
  // THEME-004 fix: replaced hardcoded rgba(255,170,0,...) / '#FFAA00' with Colors.warning token
  slabAction: {
    paddingHorizontal: Spacing.md,
    paddingVertical: Spacing.xs,
    borderRadius: 4,
    backgroundColor: Colors.warning + '1A', // 10% opacity
    borderWidth: 1,
    borderColor: Colors.warning + '4D',    // 30% opacity
  },
  slabActionText: {
    fontSize: 10,
    fontWeight: '900',
    color: Colors.warning,
    letterSpacing: 0.5,
  },
  glassSlab: {
    backgroundColor: 'rgba(255,255,255,0.03)',
    borderRadius: 16,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.08)',
    // Padding is dimension-dependent — see getDimensionStyles()
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
  // THEME-002 fix: replaced raw shadow props with Shadows.medium token
  skateCardWrapper: {
    marginBottom: Spacing.lg,
    borderRadius: 20,
    ...Shadows.medium,
  },
  skateCardGradient: {
    borderRadius: 20,
    padding: Spacing.xxs,
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
  // skateCardGroupName fontSize is dimension-dependent — see getDimensionStyles()
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
  // deviceListFixed padding is dimension-dependent — see getDimensionStyles()
  loadingContainer: {
    backgroundColor: Colors.background,
    justifyContent: 'center',
    alignItems: 'center',
  },
  btBanner: {
    backgroundColor: Colors.error,
    padding: Spacing.lg,
    alignItems: 'center',
    justifyContent: 'center',
    flexDirection: 'row',
    gap: Spacing.md,
    borderBottomWidth: 1,
    borderBottomColor: 'rgba(255,255,255,0.2)',
  },
  btBannerText: {
    color: '#FFF',
    fontWeight: '900',
    fontSize: 14,
    textTransform: 'uppercase',
    letterSpacing: 0.5,
  },
  controllerWrap: { flex: 1 },
  controllerHeaderWrap: { paddingBottom: Spacing.lg, zIndex: 100, elevation: 100 },
  controllerBodyWrap: { flex: 1 },
  dashboardWrap: { flex: 1, backgroundColor: Colors.background },
  scrollView: { flex: 1 },
});

// ─────────────────────────────────────────────────────────────────────────────
// Dimension-dependent style helpers — computed per render ONLY when dimensions
// change. These are the only styles that needed runtime window sizing.
// ─────────────────────────────────────────────────────────────────────────────
export interface DimensionStyles {
  slabContainer: { paddingHorizontal: number; marginBottom: number };
  glassSlab: { padding: number };
  skateCardGroupName: { fontSize: number; fontWeight: '900'; color: string; fontFamily: string; letterSpacing: number };
  deviceListFixed: { backgroundColor: string; borderRadius: number; padding: number };
  flexibleSpacer: { flex: number; minHeight: number };
}

export const getDimensionStyles = (windowHeight: number, windowWidth: number): DimensionStyles => {
  const isShort = windowHeight < 720;
  const isVeryShort = windowHeight < 640;

  return {
    slabContainer: {
      paddingHorizontal: Layout.padding,
      marginBottom: isVeryShort ? 8 : isShort ? 16 : 24,
    },
    glassSlab: {
      padding: isShort ? 12 : 16,
    },
    skateCardGroupName: {
      fontSize: isVeryShort ? 18 : isShort ? 20 : 22,
      fontWeight: '900',
      color: Colors.text,
      fontFamily: 'Righteous',
      letterSpacing: 0.5,
    },
    deviceListFixed: {
      backgroundColor: 'rgba(255,255,255,0.02)',
      borderRadius: 16,
      padding: isShort ? 4 : 8,
    },
    flexibleSpacer: {
      flex: 1,
      minHeight: windowHeight < 720 ? 0 : 20,
    },
  };
};

// ─────────────────────────────────────────────────────────────────────────────
// Backward-compatibility shim (Wave 1 bridge → Wave 2 will remove this)
//
// DashboardScreen.tsx calls createDashboardStyles(Colors, w, h) and is listed
// as Out of Scope for Wave 1 (DashboardScreen.tsx update is Wave 2).
// This shim keeps the public API intact so TSC passes without touching the
// monolith. Wave 2 will remove this shim and update DashboardScreen.tsx to
// use `DashboardStyles` + `getDimensionStyles` directly.
// ─────────────────────────────────────────────────────────────────────────────

/** @deprecated Use DashboardStyles + getDimensionStyles directly. Removed in Wave 2. */
export const createDashboardStyles = (
  _Colors: ThemePalette,
  windowHeight: number = 800,
  windowWidth: number = 400,
) => {
  const dim = getDimensionStyles(windowHeight, windowWidth);
  return {
    ...DashboardStyles,
    slabContainer: dim.slabContainer,
    glassSlab: { ...DashboardStyles.glassSlab, ...dim.glassSlab },
    skateCardGroupName: dim.skateCardGroupName,
    deviceListFixed: dim.deviceListFixed,
    flexibleSpacer: dim.flexibleSpacer,
  };
};
