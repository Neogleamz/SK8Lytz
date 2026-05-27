/**
 * DockedDock.tsx — SK8Lytz Floating Mode Navigation Dock
 *
 * Owns: Floating dock icon bar + horizontal swipe gesture (PanResponder).
 * Extracted from DockedController.tsx (Hollow Shell v3 — Meal 4).
 *
 * Receives `activeModeRef` as a stable React.RefObject so the PanResponder
 * closure always reads the current mode without stale capture or re-creation.
 *
 * Platform: React Native (Android + Web)
 */
import React from 'react';
import { PanResponder, Platform, StyleSheet, TouchableOpacity, View } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Spacing } from '../../theme/theme';
import type { ThemePalette } from '../../theme/theme';
import type { ModeType } from '../../types/dashboard.types';

// The canonical mode order for swipe navigation.
// 'HOME' at index 0 triggers disconnect, not a real mode.
const MODE_ORDER = ['HOME', 'FAVORITES', 'MULTIMODE', 'BUILDER', 'MUSIC', 'STREET', 'CAMERA'] as const;
type DockMode = typeof MODE_ORDER[number];

const DOCK_ITEMS: { id: DockMode; icon: string }[] = [
  { id: 'HOME',      icon: 'home-outline'        },
  { id: 'FAVORITES', icon: 'cards-heart-outline' },
  { id: 'MULTIMODE', icon: 'lightning-bolt'      },
  { id: 'BUILDER',   icon: 'palette'             },
  { id: 'MUSIC',     icon: 'music'               },
  { id: 'STREET',    icon: 'run-fast'            },
  { id: 'CAMERA',    icon: 'camera'              },
];

interface DockedDockProps {
  /** Stable ref to current active mode — read by PanResponder to avoid stale closures. */
  activeModeRef: React.RefObject<ModeType | string>;
  activeMode: ModeType | string;
  onModeChange: (mode: ModeType | string) => void;
  onDisconnect?: () => void;
  /** Mode IDs that should be hidden from the dock (permission-denied modes). */
  hiddenModes?: readonly string[];
  Colors: ThemePalette;
}

const DockedDock = React.memo(function DockedDock({
  activeModeRef,
  activeMode,
  onModeChange,
  onDisconnect,
  hiddenModes = [],
  Colors,
}: DockedDockProps) {
  const styles = React.useMemo(() => createStyles(Colors), [Colors]);

  // Stable ref so PanResponder closure reads current hidden modes without recreation
  const hiddenModesRef = React.useRef(hiddenModes);
  hiddenModesRef.current = hiddenModes;

  // Filter visible dock items (reactive to permission changes)
  const visibleDockItems = React.useMemo(
    () => DOCK_ITEMS.filter(item => !hiddenModes.includes(item.id)),
    [hiddenModes]
  );

  // Filtered mode order for swipe navigation (skip hidden modes)
  const visibleModeOrder = React.useMemo(
    () => MODE_ORDER.filter(m => !hiddenModes.includes(m)),
    [hiddenModes]
  );

  // ── Gesture Navigation: Horizontal swipe to change modes ──────────────────
  // PanResponder reads activeModeRef.current so the closure stays stable
  // without needing activeMode as a dependency (avoids recreating on every render).
  const swipePanResponder = React.useRef(
    PanResponder.create({
      onMoveShouldSetPanResponder: (_evt, gestureState) => {
        return (
          Math.abs(gestureState.dx) > 30 &&
          Math.abs(gestureState.dx) > Math.abs(gestureState.dy) * 1.5
        );
      },
      onPanResponderRelease: (_evt, gestureState) => {
        const currentModeStr =
          activeModeRef.current === ('MULTI' as string) ? 'MULTIMODE' : activeModeRef.current;
        // Use visible order so swipe skips hidden (permission-denied) modes
        const currentHidden = hiddenModesRef.current;
        const filteredOrder = MODE_ORDER.filter(m => !currentHidden.includes(m));
        const currentModeIdx = filteredOrder.indexOf(currentModeStr as DockMode);
        if (currentModeIdx === -1) return;

        if (gestureState.dx > 50) {
          // Swipe Right → go to previous mode
          if (currentModeIdx > 0) {
            const prevMode = filteredOrder[currentModeIdx - 1];
            if (prevMode === 'HOME') {
              onDisconnect?.();
            } else {
              onModeChange(prevMode);
            }
          }
        } else if (gestureState.dx < -50) {
          // Swipe Left → go to next mode
          if (currentModeIdx < filteredOrder.length - 1) {
            const nextMode = filteredOrder[currentModeIdx + 1];
            onModeChange(nextMode);
          }
        }
      },
    })
  ).current;

  return (
    <View {...swipePanResponder.panHandlers} style={{ marginBottom: Spacing.xs }}>
      <View style={styles.floatingDock}>
        {visibleDockItems.map(dockItem => {
          const isActive = activeMode === dockItem.id;
          return (
            <TouchableOpacity
              key={dockItem.id}
              onPress={() => {
                if (dockItem.id === 'HOME') {
                  onDisconnect?.();
                } else {
                  onModeChange(dockItem.id);
                }
              }}
              style={[styles.dockIconCont, isActive && styles.dockIconActive]}
            >
              <MaterialCommunityIcons
                name={dockItem.icon as keyof typeof MaterialCommunityIcons.glyphMap}
                size={22}
                color={isActive ? '#000000' : Colors.textMuted}
              />
            </TouchableOpacity>
          );
        })}
      </View>
    </View>
  );
});

export default DockedDock;

const createStyles = (Colors: ThemePalette) =>
  StyleSheet.create({
    floatingDock: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
      backgroundColor: Colors.isDark
        ? 'rgba(8, 10, 16, 0.98)'
        : 'rgba(255, 255, 255, 0.98)',
      borderWidth: 1,
      borderColor: Colors.isDark
        ? 'rgba(0, 240, 255, 0.35)'
        : 'rgba(0, 200, 255, 0.4)',
      borderRadius: 30,
      paddingVertical: Spacing.md,
      paddingHorizontal: Spacing.xl,
      marginHorizontal: Spacing.lg,
      marginBottom: Spacing.sm,
      marginTop: Spacing.sm,
      ...(Platform.OS === 'web'
        ? { boxShadow: `0px 8px 20px ${Colors.primary}99` } as unknown as import('react-native').ViewStyle
        : {
            shadowColor: Colors.primary,
            shadowOffset: { width: 0, height: 8 },
            shadowOpacity: 0.6,
            shadowRadius: 20,
          }),
      elevation: 15,
    },
    dockIconCont: {
      width: 44,
      height: 44,
      borderRadius: 22,
      backgroundColor: Colors.isDark
        ? 'rgba(255,255,255,0.03)'
        : 'rgba(0,0,0,0.05)',
      alignItems: 'center',
      justifyContent: 'center',
      borderWidth: 1,
      borderColor: Colors.isDark
        ? 'rgba(255,255,255,0.05)'
        : 'rgba(0,0,0,0.08)',
    },
    dockIconActive: {
      backgroundColor: Colors.primary,
      borderColor: Colors.primary,
      ...(Platform.OS === 'web'
        ? { boxShadow: `0px 0px 12px ${Colors.primary}e6` } as unknown as import('react-native').ViewStyle
        : {
            shadowColor: Colors.primary,
            shadowOffset: { width: 0, height: 0 },
            shadowOpacity: 0.9,
            shadowRadius: 12,
          }),
      elevation: 8,
      transform: [{ scale: 1.15 }],
    },
  });
