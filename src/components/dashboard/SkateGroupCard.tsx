import { MaterialCommunityIcons } from '@expo/vector-icons';
import { LinearGradient } from 'expo-linear-gradient';
import React from 'react';
import { Text, TouchableOpacity, View } from 'react-native';
import type { ThemePalette } from '../../theme/theme';
import type { CustomGroup } from '../../types/dashboard.types';

interface SkateGroupCardProps {
  group: CustomGroup;
  onPress: () => void;
  onLongPress: () => void;
  colors: string[];
  lastPattern?: string;
  isActive?: boolean;
  rssiMap?: Record<string, number>;
  userProfile: any;
  powerStates: Record<string, boolean>;
  Colors: ThemePalette;
  styles: any;
}

/**
 * SkateGroupCard Helper Component
 * Renders a premium, telemetry-rich group card with morphing gradients.
 * Extracted from DashboardScreen.tsx (Phase 6).
 */
export const SkateGroupCard = ({
  group,
  onPress,
  onLongPress,
  colors,
  lastPattern,
  isActive,
  rssiMap = {},
  userProfile,
  powerStates,
  Colors,
  styles
}: SkateGroupCardProps) => {
  const isPoweredOn = group.deviceIds.every(id => powerStates[id] !== false);

  return (
    <TouchableOpacity
      onPress={onPress}
      onLongPress={onLongPress}
      activeOpacity={0.85}
      style={[styles.skateCardWrapper, { opacity: isActive ? 1 : 0.65 }]}
    >
      <LinearGradient
        colors={isPoweredOn ? (colors as any) : ['#333', '#1a1a1a']}
        start={{ x: 0, y: 0 }}
        end={{ x: 1, y: 1 }}
        style={[styles.skateCardGradient, isActive && { borderColor: Colors.primary, borderWidth: 1.5 }]}
      >
        <View style={[styles.skateCardInner, { minHeight: 130, justifyContent: 'center', position: 'relative' }]}>
          {/* Glassmorphism Refraction */}
          <View style={styles.skateCardRefraction} />

          {/* TOP BAR: Skates & Power */}
          <View style={[styles.skateCardHeader, { position: 'absolute', top: 16, left: 16, right: 16, zIndex: 10, marginBottom: 0 }]}>
            <View style={styles.avatarPill}>
              {/* Stacked Skate+Meter Clusters */}
              <View style={{ flexDirection: 'row' }}>
                {group.deviceIds.map((id, index) => {
                  const isDeviceOn = powerStates[id] !== false;
                  const rssi = rssiMap[id] || -100;
                  const activeColor = isDeviceOn ? Colors.success : Colors.error;
                  
                  return (
                    <View 
                      key={`cluster-${id}`} 
                      style={{ 
                        flexDirection: 'row', 
                        alignItems: 'center', 
                        marginLeft: index > 0 ? -12 : 0, 
                        transform: [{ translateY: index > 0 ? -10 : 0 }],
                        zIndex: group.deviceIds.length - index 
                      }}
                    >
                      <MaterialCommunityIcons 
                        name="roller-skate" 
                        size={18} 
                        color="#FFF" 
                      />
                      <View style={{ flexDirection: 'row', alignItems: 'flex-end', marginLeft: 4, gap: 1, height: 10 }}>
                        <View style={{ width: 2, height: 4, backgroundColor: rssi >= -90 ? activeColor : '#555' }} />
                        <View style={{ width: 2, height: 7, backgroundColor: rssi >= -75 ? activeColor : '#555' }} />
                        <View style={{ width: 2, height: 10, backgroundColor: rssi >= -60 ? activeColor : '#555' }} />
                      </View>
                    </View>
                  );
                })}
              </View>
            </View>

            {/* Power Button moved from footer */}
            <View style={[styles.powerIconCircle, { backgroundColor: isPoweredOn ? 'rgba(0, 240, 255, 0.1)' : 'rgba(255,255,255,0.05)', marginLeft: 'auto' }]}>
              <MaterialCommunityIcons 
                name="power" 
                size={16} 
                color={isPoweredOn ? Colors.primary : '#666'} 
              />
            </View>
          </View>

          {/* MAIN CONTENT: Name Only (Perfect Center) */}
          <View style={[styles.skateCardContent, { marginBottom: 0, marginTop: 10, alignItems: 'center', justifyContent: 'center' }]}>
            <Text style={[styles.skateCardGroupName, { textAlign: 'center' }]} numberOfLines={1}>
              {group.name.toUpperCase()}
            </Text>
          </View>

          {/* BOTTOM BAR: Pattern Pill */}
          <View style={[styles.patternPill, { position: 'absolute', bottom: 16, alignSelf: 'center' }]}>
            <View style={[styles.patternDot, { backgroundColor: isPoweredOn ? colors[0] : '#555' }]} />
            <Text style={styles.patternName} numberOfLines={1}>
              {isPoweredOn ? (lastPattern || 'ACTIVE') : 'POWERED OFF'}
            </Text>
          </View>
        </View>
      </LinearGradient>
    </TouchableOpacity>
  );
};
