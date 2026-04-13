import React from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import type { CustomGroup } from '../../types/dashboard.types';
import type { ThemePalette } from '../../theme/theme';

interface SkateGroupCardProps {
  group: CustomGroup;
  onPress: () => void;
  onLongPress: () => void;
  colors: string[];
  lastPattern?: string;
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
      style={styles.skateCardWrapper}
    >
      <LinearGradient
        colors={isPoweredOn ? (colors as any) : ['#333', '#1a1a1a']}
        start={{ x: 0, y: 0 }}
        end={{ x: 1, y: 1 }}
        style={styles.skateCardGradient}
      >
        <View style={styles.skateCardInner}>
          {/* Glassmorphism Refraction */}
          <View style={styles.skateCardRefraction} />

          {/* TOP BAR: Avatar & Status */}
          <View style={styles.skateCardHeader}>
            <View style={styles.avatarPill}>
              {userProfile && userProfile.avatar_url ? (
                <Text>😎</Text> // Fallback until avatar config is wired deeply
              ) : (
                <MaterialCommunityIcons name="skate" size={14} color="#FFF" />
              )}
              <View style={[styles.avatarStatusDot, { backgroundColor: isPoweredOn ? Colors.success : '#555' }]} />
            </View>
            <View style={styles.telemetryContainer}>
              <View style={styles.telemetryItem}>
                <MaterialCommunityIcons name="bluetooth" size={12} color="#AAA" />
                <View style={styles.rssiBars}>
                  <View style={[styles.rssiBar, { height: 4, backgroundColor: Colors.success }]} />
                  <View style={[styles.rssiBar, { height: 7, backgroundColor: Colors.success }]} />
                  <View style={[styles.rssiBar, { height: 10, backgroundColor: Colors.success }]} />
                </View>
              </View>
              <View style={styles.telemetryItem}>
                <MaterialCommunityIcons name="battery" size={14} color="#00FF85" />
                <Text style={{ fontSize: 10, color: '#AAA', fontWeight: '800' }}>92%</Text>
              </View>
            </View>
          </View>

          {/* MAIN CONTENT: Name & Pattern */}
          <View style={styles.skateCardContent}>
            <Text style={styles.skateCardGroupName}>
              {group.name.toUpperCase()}
            </Text>
            
            <View style={styles.patternPill}>
              <View style={[styles.patternDot, { backgroundColor: isPoweredOn ? colors[0] : '#555' }]} />
              <Text style={styles.patternName}>
                {isPoweredOn ? (lastPattern || 'ACTIVE') : 'POWERED OFF'}
              </Text>
            </View>
          </View>

          {/* BOTTOM DECOR: Device Count */}
          <View style={styles.skateCardFooter}>
             <Text style={styles.deviceCountText}>
               {group.deviceIds.length} {group.deviceIds.length === 1 ? 'DEVICE' : 'DEVICES'} PAIRED
             </Text>
             <View style={[styles.powerIconCircle, { backgroundColor: isPoweredOn ? 'rgba(0, 240, 255, 0.1)' : 'rgba(255,255,255,0.05)' }]}>
               <MaterialCommunityIcons 
                 name="power" 
                 size={16} 
                 color={isPoweredOn ? Colors.primary : '#666'} 
               />
             </View>
          </View>
        </View>
      </LinearGradient>
    </TouchableOpacity>
  );
};
