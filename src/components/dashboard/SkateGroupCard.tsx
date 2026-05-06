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
                {group.deviceIds.map((id, index) => (
                  <MaterialCommunityIcons 
                    key={`icon-${id}`} 
                    name="roller-skate" 
                    size={16} 
                    color="#FFF" 
                    style={{ 
                      position: 'absolute',
                      bottom: index > 0 ? 6 : 0,
                      left: index > 0 ? 10 : 0,
                      zIndex: group.deviceIds.length - index 
                    }} 
                  />
                ))}
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
          
          {/* Right-Side Quick Launch Strip */}
          <View style={{ 
            width: 48, 
            backgroundColor: 'rgba(0,0,0,0.25)', 
            borderLeftWidth: 1, 
            borderLeftColor: 'rgba(255,255,255,0.05)', 
            alignItems: 'center', 
            justifyContent: 'space-evenly',
            paddingVertical: 8
          }}>
            <TouchableOpacity activeOpacity={0.7} onPress={() => {/* Handle Power */}}>
              <View style={[styles.powerIconCircle, { backgroundColor: isPoweredOn ? 'rgba(0, 240, 255, 0.1)' : 'transparent', width: 34, height: 34 }]}>
                <MaterialCommunityIcons name="power" size={18} color={isPoweredOn ? Colors.primary : '#666'} />
              </View>
            </TouchableOpacity>

            <TouchableOpacity activeOpacity={0.7} onPress={() => {/* Handle Music */}}>
              <View style={[styles.powerIconCircle, { backgroundColor: 'transparent', width: 34, height: 34 }]}>
                <MaterialCommunityIcons name="music-note" size={18} color="#AAA" />
              </View>
            </TouchableOpacity>

            <TouchableOpacity activeOpacity={0.7} onPress={() => {/* Handle Camera */}}>
              <View style={[styles.powerIconCircle, { backgroundColor: 'transparent', width: 34, height: 34 }]}>
                <MaterialCommunityIcons name="camera" size={18} color="#AAA" />
              </View>
            </TouchableOpacity>

            <TouchableOpacity activeOpacity={0.7} onPress={() => {/* Handle Favorite */}}>
              <View style={[styles.powerIconCircle, { backgroundColor: 'transparent', width: 34, height: 34 }]}>
                <MaterialCommunityIcons name="heart" size={16} color="#AAA" />
              </View>
            </TouchableOpacity>
          </View>
        </View>
      </LinearGradient>
    </TouchableOpacity>
  );
};
