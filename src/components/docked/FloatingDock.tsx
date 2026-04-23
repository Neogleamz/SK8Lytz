/**
 * FloatingDock.tsx — Bottom navigation dock for DockedController modes.
 *
 * Renders the 6 dock items: HOME, FAVORITES, MULTI, MUSIC, STREET, CAMERA.
 *
 * Extracted from DockedController.tsx (Phase 3).
 */
import { MaterialCommunityIcons } from '@expo/vector-icons';
import React from 'react';
import { TouchableOpacity, View } from 'react-native';
import { Spacing } from '../../theme/theme';
import type { ModeType } from '../../types/dashboard.types';

const DOCK_ITEMS = [
  { id: 'HOME',      icon: 'home-outline'        },
  { id: 'FAVORITES', icon: 'cards-heart-outline'  },
  { id: 'MULTI',     icon: 'palette'              },
  { id: 'MUSIC',     icon: 'music'                },
  { id: 'STREET',    icon: 'run-fast'             },
  { id: 'CAMERA',    icon: 'camera'               },
] as const;

interface FloatingDockProps {
  activeMode: ModeType;
  onModeChange: (mode: ModeType) => void;
  onSetLastOperatingMode: (mode: ModeType) => void;
  onSetFixedSubMode: (subMode: 'PATTERN' | 'BUILDER') => void;
  onDisconnect?: () => void;
  Colors: any;
  styles: {
    floatingDock: any;
    dockIconCont: any;
    dockIconActive: any;
  };
}

const FloatingDock = React.memo(({
  activeMode,
  onModeChange,
  onSetLastOperatingMode,
  onSetFixedSubMode,
  onDisconnect,
  Colors,
  styles,
}: FloatingDockProps) => {
  const handleDockPress = (dockId: string) => {
    if (dockId === 'HOME') {
      if (onDisconnect) onDisconnect();
    } else if (dockId === 'FAVORITES') {
      onModeChange('FAVORITES');
    } else if (dockId === 'STREET') {
      onModeChange('STREET');
      onSetLastOperatingMode('STREET');
    } else if (dockId === 'MUSIC') {
      onModeChange('MUSIC');
      onSetLastOperatingMode('MUSIC');
    } else if (dockId === 'CAMERA') {
      onModeChange('CAMERA');
      onSetLastOperatingMode('CAMERA');
    } else {
      // MULTI -> MULTIMODE (restores to PATTERN submode)
      onModeChange('MULTIMODE');
      onSetLastOperatingMode('MULTIMODE');
      onSetFixedSubMode('PATTERN');
    }
  };

  return (
    <View style={{ marginBottom: Spacing.xs }}>
      <View style={[styles.floatingDock, { marginBottom: 0 }]}>
        {DOCK_ITEMS.map(dockItem => {
          const isActive =
            dockItem.id === 'MULTI' ? activeMode === 'MULTIMODE' : activeMode === dockItem.id;
          return (
            <TouchableOpacity
              key={dockItem.id}
              onPress={() => handleDockPress(dockItem.id)}
              style={[styles.dockIconCont, isActive && styles.dockIconActive]}
            >
              <MaterialCommunityIcons
                name={dockItem.icon as any}
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

export default FloatingDock;
