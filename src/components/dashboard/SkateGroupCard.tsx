/* eslint-disable unused-imports/no-unused-vars */
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { LinearGradient } from 'expo-linear-gradient';
import React from 'react';
import { Text, TouchableOpacity, View } from 'react-native';
import { webStyle } from '../../utils/webStyles';
import type { ThemePalette } from '../../theme/theme';
import type { CustomGroup } from '../../types/dashboard.types';
import type { UserProfile } from '../../services/ProfileService';

interface SkateGroupCardProps {
  group: CustomGroup;
  onPress: () => void;
  onLongPress: () => void;
  colors: string[];
  lastPattern?: string;
  isActive?: boolean;
  rssiMap?: Record<string, number>;
  userProfile: UserProfile | null;
  powerStates: Record<string, boolean>;
  Colors: ThemePalette;
  styles: Record<string, import('react-native').StyleProp<import('react-native').ViewStyle | import('react-native').TextStyle>>;
  onPowerPress?: () => void;
  onMusicPress?: () => void;
  onCameraPress?: () => void;
  onFavoritePress?: () => void;
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
  styles,
  onPowerPress,
  onMusicPress,
  onCameraPress,
  onFavoritePress,
}: SkateGroupCardProps) => {
  const onCount = group.deviceIds.filter(id => powerStates[id] !== false).length;
  const total = group.deviceIds.length;
  const isAllOn = onCount === total;
  const isAllOff = onCount === 0;
  const isMixed = onCount > 0 && onCount < total;
  const isPoweredOn = onCount > 0;

  return (
    <TouchableOpacity
      onPress={onPress}
      onLongPress={onLongPress}
      activeOpacity={0.85}
      style={styles.skateCardWrapper}
    >
      <LinearGradient
        colors={isPoweredOn ? (colors as unknown as readonly [string, string, ...string[]]) : ['#333', '#1a1a1a']}
        start={{ x: 0, y: 0 }}
        end={{ x: 1, y: 1 }}
        style={[styles.skateCardGradient, isActive && { borderColor: Colors.primary, borderWidth: 1.5 }, { overflow: 'hidden' }]}
      >
        <View style={{ flexDirection: 'row', flex: 1 }}>
          
          {/* Main Left Content Area */}
          <View style={[styles.skateCardInner, { flex: 1, minHeight: 130, justifyContent: 'center', position: 'relative' }]}>
            
            {/* Glassmorphism Refraction */}
            <View style={styles.skateCardRefraction} />

            {/* TOP BAR: Skates & Power */}
            <View style={[styles.skateCardHeader, { position: 'absolute', top: 10, left: 12, right: 12, zIndex: 10, marginBottom: 0 }]}>
              <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                
                {/* Stacked Skates in a fixed 3D box (Smaller) */}
                <View style={{ width: 28, height: 22, position: 'relative' }}>
                  {group.deviceIds.map((id, index) => {
                    const isDeviceOn = powerStates[id] !== false;
                    return (
                      <MaterialCommunityIcons 
                        key={`icon-${id}`} 
                        name="roller-skate" 
                        size={16} 
                        color={isDeviceOn ? Colors.success : Colors.error} 
                        style={{ 
                          position: 'absolute',
                          bottom: index > 0 ? 6 : 0,
                          left: index > 0 ? 10 : 0,
                          zIndex: group.deviceIds.length - index 
                        }} 
                      />
                    );
                  })}
                </View>

                {/* Vertical Stack of RSSI Meters (Smaller) */}
                <View style={{ flexDirection: 'column', gap: 5, marginLeft: 6, height: 22, justifyContent: 'center' }}>
                  {[...group.deviceIds].reverse().map((id) => {
                    const isDeviceOn = powerStates[id] !== false;
                    const rssi = rssiMap[id] || -100;
                    const activeColor = isDeviceOn ? Colors.success : Colors.error;
                    return (
                      <View 
                        key={`rssi-${id}`} 
                        style={{ flexDirection: 'row', alignItems: 'flex-end', gap: 1, height: 8 }}
                      >
                        <View style={{ width: 2, height: 3, backgroundColor: rssi >= -90 ? activeColor : '#555' }} />
                        <View style={{ width: 2, height: 5, backgroundColor: rssi >= -75 ? activeColor : '#555' }} />
                        <View style={{ width: 2, height: 8, backgroundColor: rssi >= -60 ? activeColor : '#555' }} />
                      </View>
                    );
                  })}
                </View>

              </View>
            </View>

            {/* MAIN CONTENT: Name Only (Perfect Center relative to whole card) */}
            <View style={[styles.skateCardContent, { marginBottom: 0, marginLeft: 24, alignItems: 'center', justifyContent: 'center' }]}>
              <Text 
                style={[
                  styles.skateCardGroupName, 
                  { 
                    textAlign: 'center', 
                    fontSize: 24,
                    ...require('react-native').Platform.select({
                      web: webStyle({ textShadow: `0px 0px 10px ${isPoweredOn ? colors[0] : 'rgba(255,255,255,0.2)'}` }), // MIGRATION-SHIM
                      default: {
                        textShadowColor: isPoweredOn ? colors[0] : 'rgba(255,255,255,0.2)',
                        textShadowOffset: { width: 0, height: 0 },
                        textShadowRadius: 10
                      }
                    })
                  }
                ]} 
                numberOfLines={1}
              >
                {group.name.toUpperCase()}
              </Text>
            </View>

            {/* BOTTOM BAR: Pattern Pill */}
            <View style={[styles.patternPill, { position: 'absolute', bottom: 16, alignSelf: 'center' }]}>
              <View style={[styles.patternDot, { backgroundColor: isAllOn ? colors[0] : isMixed ? Colors.warning : '#555' }]} />
              <Text style={styles.patternName} numberOfLines={1}>
                {isAllOn ? (lastPattern || 'ACTIVE') : isMixed ? 'MIXED - TAP TO SYNC' : 'POWERED OFF'}
              </Text>
            </View>

          </View>
          
          {/* Right-Side Quick Launch Strip */}
          <View style={{ 
            width: 48, 
            backgroundColor: 'rgba(0,0,0,0.6)', 
            borderLeftWidth: 1, 
            borderLeftColor: 'rgba(255,255,255,0.1)', 
            alignItems: 'center', 
            justifyContent: 'space-evenly',
            paddingVertical: 8
          }}>
            <TouchableOpacity activeOpacity={0.7} onPress={onPowerPress}>
              <View style={[styles.powerIconCircle, { backgroundColor: isPoweredOn ? 'rgba(255, 255, 255, 0.15)' : 'transparent', width: 34, height: 34 }]}>
                <MaterialCommunityIcons 
                  name="power" 
                  size={18} 
                  color={isAllOn ? '#FFF' : isMixed ? Colors.warning : '#666'} 
                  style={isPoweredOn ? require('react-native').Platform.select({
                    web: webStyle({ textShadow: `0px 0px 8px ${isMixed ? Colors.warning : Colors.primary}` }), // MIGRATION-SHIM
                    default: { textShadowColor: isMixed ? Colors.warning : Colors.primary, textShadowOffset: {width: 0, height: 0}, textShadowRadius: 8 }
                  }) : undefined}
                />
              </View>
            </TouchableOpacity>

            <TouchableOpacity activeOpacity={0.7} onPress={onMusicPress}>
              <View style={[styles.powerIconCircle, { backgroundColor: 'transparent', width: 34, height: 34 }]}>
                <MaterialCommunityIcons name="music-note" size={18} color="#FFF" />
              </View>
            </TouchableOpacity>

            <TouchableOpacity activeOpacity={0.7} onPress={onCameraPress}>
              <View style={[styles.powerIconCircle, { backgroundColor: 'transparent', width: 34, height: 34 }]}>
                <MaterialCommunityIcons name="camera" size={18} color="#FFF" />
              </View>
            </TouchableOpacity>

            <TouchableOpacity activeOpacity={0.7} onPress={onFavoritePress}>
              <View style={[styles.powerIconCircle, { backgroundColor: 'transparent', width: 34, height: 34 }]}>
                <MaterialCommunityIcons name="heart" size={16} color="#FFF" />
              </View>
            </TouchableOpacity>
          </View>

        </View>
      </LinearGradient>
    </TouchableOpacity>
  );
};
