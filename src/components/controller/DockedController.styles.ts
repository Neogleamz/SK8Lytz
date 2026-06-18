/**
 * DockedController.styles.ts — StyleSheet for DockedController.tsx.
 *
 * Extracted from DockedController.tsx (Phase 1 S4 extraction, 2026-06-18).
 */
import { StyleSheet } from 'react-native';
import { Layout, Spacing, Typography } from '../../theme/theme';
import type { ThemePalette } from '../../theme/theme';

export const createDockedControllerStyles = (Colors: ThemePalette) => StyleSheet.create({
  container: {
    flex: 1,
    paddingHorizontal: 0,
    paddingBottom: 0,
    paddingTop: 0,
  },
  visualizerWrapper: {
    width: '100%',
    alignItems: 'stretch',
    marginVertical: Spacing.xxs,
  },
  tabContainer: {
    flexDirection: 'row',
    backgroundColor: Colors.surfaceHighlight,
    borderRadius: Layout.borderRadius,
    padding: Spacing.sm,
    marginBottom: Spacing.sm,
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.08)',
  },
  tab: {
    flex: 1,
    paddingVertical: Spacing.sm,
    alignItems: 'center',
    borderRadius: Layout.borderRadius - 6,
    overflow: 'hidden',
  },
  activeTab: {
    backgroundColor: 'transparent',
    borderWidth: 0,
  },
  tabText: {
    ...Typography.body,
    color: Colors.textMuted,
    fontWeight: '800',
    letterSpacing: 1,
    zIndex: 2,
  },
  activeTabText: {
    color: Colors.isDark ? '#FFF' : Colors.accent,
  },
  controlsContainer: {
    flex: 1,
    padding: Spacing.md,
    backgroundColor: Colors.isDark ? 'rgba(21, 25, 40, 0.7)' : Colors.surface,
    borderRadius: Layout.borderRadius + 4,
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255, 255, 255, 0.1)' : 'rgba(0,0,0,0.08)',
  },
  activeModeContainer: {
    flex: 1,
    overflow: 'hidden',
  },
  blePendingIndicator: {
    width: 6, height: 6, borderRadius: 3, backgroundColor: '#00F0FF', opacity: 0.7, position: 'absolute', right: 8, top: 8, zIndex: 10
  },
  bleReconciledIndicator: {
    width: 6, height: 6, borderRadius: 3, backgroundColor: '#FF4444', position: 'absolute', right: 8, top: 8, zIndex: 10
  },
  visualizerContent: {
    marginBottom: Spacing.sm, width: '100%'
  },
  powerBtn: {
    position: 'absolute', top: 12, left: 16, zIndex: 100, padding: Spacing.sm, borderRadius: 20, borderWidth: 1
  },
  powerBtnOn: {
    backgroundColor: 'rgba(0, 240, 255, 0.15)', borderColor: 'rgba(0, 240, 255, 0.3)'
  },
  powerBtnOff: {
    backgroundColor: 'rgba(255, 68, 68, 0.15)', borderColor: 'rgba(255, 68, 68, 0.3)'
  },
  favoriteBtn: {
    position: 'absolute', top: 12, right: 16, zIndex: 100, backgroundColor: 'rgba(255,255,255,0.1)', padding: Spacing.sm, borderRadius: 20
  },
  controlsContainerPadding: {
    padding: Spacing.xs, overflow: 'hidden'
  },
  activeModeFlex: {
    flex: 1, justifyContent: 'space-evenly'
  }
});
