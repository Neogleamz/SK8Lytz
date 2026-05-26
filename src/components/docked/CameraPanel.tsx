import React, { useState, useCallback } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, Platform, ScrollView } from 'react-native';
import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';
import * as Haptics from 'expo-haptics';
import CameraTracker from '../CameraTracker';
import { Spacing } from '../../theme/theme';

interface CameraPanelProps {
  onColorDetected: (hex: string) => void;
}

const MAX_SWATCHES = 5;

const CameraPanel = React.memo(({ onColorDetected }: CameraPanelProps) => {
  const [liveHex, setLiveHex] = useState<string>('#FFFFFF');
  const [swatches, setSwatches] = useState<string[]>([]);
  const [activeSwatch, setActiveSwatch] = useState<string | null>(null);

  const handleLiveColorDetected = useCallback((hex: string) => {
    setLiveHex(hex);
  }, []);

  const handleCapture = useCallback(() => {
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);
    
    // Add to swatches list (shift oldest out if max reached)
    setSwatches(prev => {
      const newSwatches = [liveHex, ...prev.filter(c => c !== liveHex)].slice(0, MAX_SWATCHES);
      return newSwatches;
    });
    
    // Auto-select the newly captured color
    setActiveSwatch(liveHex);
    onColorDetected(liveHex);
  }, [liveHex, onColorDetected]);

  const handleSelectSwatch = useCallback((hex: string) => {
    Haptics.selectionAsync();
    setActiveSwatch(hex);
    onColorDetected(hex);
  }, [onColorDetected]);

  return (
    <View style={styles.container}>
      {/* 
        Camera is ALWAYS active, but we intercept its color locally to drive the reticle 
        without spamming the BLE hardware. BLE is only fired on tap.
      */}
      <CameraTracker
        isActive
        onColorDetected={handleLiveColorDetected}
      />

      <View style={styles.controlPanel}>
        <Text style={styles.instructionText}>
          Aim at a color, then tap capture to save a swatch. Tap swatches to send to skates.
        </Text>

        <View style={styles.swatchRow}>
          {/* Live Preview / Capture Button */}
          <TouchableOpacity
            activeOpacity={0.8}
            onPress={handleCapture}
            style={[
              styles.captureFab,
              { backgroundColor: liveHex }
            ]}
          >
            <MaterialCommunityIcons
              name="crosshairs-gps"
              size={28}
              color={liveHex === '#FFFFFF' ? '#000' : '#FFF'}
            />
          </TouchableOpacity>

          <View style={styles.divider} />

          {/* Saved Swatches */}
          <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={styles.swatchList}>
            {swatches.map((swatch, index) => (
              <TouchableOpacity
                key={`${swatch}-${index}`}
                onPress={() => handleSelectSwatch(swatch)}
                style={[
                  styles.swatchPill,
                  { backgroundColor: swatch },
                  activeSwatch === swatch && styles.swatchActive
                ]}
              />
            ))}
            {/* Empty placeholders */}
            {Array.from({ length: Math.max(0, MAX_SWATCHES - swatches.length) }).map((_, index) => (
              <View key={`empty-${index}`} style={styles.swatchEmpty} />
            ))}
          </ScrollView>
        </View>

        <View style={styles.hexPill}>
          <Text style={styles.hexText}>
            TARGET: {liveHex} {activeSwatch ? `| SENDING: ${activeSwatch}` : ''}
          </Text>
        </View>
      </View>
    </View>
  );
});

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  controlPanel: {
    padding: Spacing.lg,
    backgroundColor: '#0a0a0a',
    alignItems: 'center',
    borderTopWidth: 1,
    borderTopColor: '#222',
  },
  instructionText: {
    color: 'rgba(255,255,255,0.6)',
    fontSize: 12,
    fontFamily: 'Inter-Medium',
    textAlign: 'center',
    marginBottom: Spacing.lg,
  },
  swatchRow: {
    flexDirection: 'row',
    alignItems: 'center',
    width: '100%',
    marginBottom: Spacing.lg,
  },
  captureFab: {
    width: 64,
    height: 64,
    borderRadius: 32,
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 3,
    borderColor: 'rgba(255,255,255,0.4)',
    ...Platform.select({
      web: { boxShadow: '0px 4px 10px rgba(0,0,0,0.3)' },
      default: { shadowColor: '#000', shadowOffset: { width: 0, height: 4 }, shadowOpacity: 0.3, shadowRadius: 10, elevation: 8 }
    })
  },
  divider: {
    width: 1,
    height: 40,
    backgroundColor: '#333',
    marginHorizontal: Spacing.md,
  },
  swatchList: {
    alignItems: 'center',
    gap: Spacing.sm,
  },
  swatchPill: {
    width: 48,
    height: 48,
    borderRadius: 24,
    borderWidth: 2,
    borderColor: 'rgba(255,255,255,0.1)',
  },
  swatchActive: {
    borderColor: '#FFF',
    transform: [{ scale: 1.1 }],
    ...Platform.select({
      web: { boxShadow: '0px 0px 12px rgba(255,255,255,0.5)' },
      default: { shadowColor: '#FFF', shadowOffset: { width: 0, height: 0 }, shadowOpacity: 0.5, shadowRadius: 12, elevation: 10 }
    })
  },
  swatchEmpty: {
    width: 48,
    height: 48,
    borderRadius: 24,
    borderWidth: 2,
    borderColor: '#222',
    borderStyle: 'dashed',
    backgroundColor: '#111',
  },
  hexPill: {
    backgroundColor: 'rgba(255,255,255,0.1)',
    paddingHorizontal: Spacing.lg,
    paddingVertical: 8,
    borderRadius: 20,
  },
  hexText: {
    color: '#FFF',
    fontSize: 12,
    fontFamily: 'Inter-Bold',
    letterSpacing: 1,
  },
});

export default CameraPanel;
