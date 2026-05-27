import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';
import { LinearGradient } from 'expo-linear-gradient';
import * as Haptics from 'expo-haptics';
import React, { useState, useCallback, useRef } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, Platform, ScrollView } from 'react-native';
import CameraTracker from '../CameraTracker';
import { Spacing, Colors } from '../../theme/theme';
import { RGB } from '../../utils/kMeansPalette';

interface CameraPanelProps {
  onColorDetected: (hex: string) => void;
  onVibeApply?: (colors: RGB[], isFlow: boolean) => void;
  onVibePaletteChange?: (colors: RGB[]) => void;
  onSubModeChange?: (mode: 'SNIPER' | 'VIBE') => void;
}

const MAX_SWATCHES = 5;

// Helper to convert RGB to Hex
function rgbToHexStr(color: RGB): string {
  const toHex = (v: number) => {
    const s = Math.round(v).toString(16);
    return s.length === 1 ? '0' + s : s;
  };
  return ('#' + toHex(color.r) + toHex(color.g) + toHex(color.b)).toUpperCase();
}

const CameraPanel = React.memo(({ onColorDetected, onVibeApply, onVibePaletteChange, onSubModeChange }: CameraPanelProps) => {
  const [subMode, setSubMode] = useState<'SNIPER' | 'VIBE'>('SNIPER');
  
  // SNIPER Mode State
  const [liveHex, setLiveHex] = useState<string>('#FFFFFF');
  const [swatches, setSwatches] = useState<string[]>([]);
  const [activeSwatch, setActiveSwatch] = useState<string | null>(null);

  // VIBE Mode State
  const [vibePalette, setVibePalette] = useState<RGB[]>([
    { r: 255, g: 0, b: 0 },
    { r: 0, g: 255, b: 0 },
    { r: 0, g: 0, b: 255 },
  ]);
  const [isFlow, setIsFlow] = useState<boolean>(true);

  // References to callbacks
  const onVibePaletteChangeRef = useRef(onVibePaletteChange);
  onVibePaletteChangeRef.current = onVibePaletteChange;

  const handleLiveColorDetected = useCallback((hex: string) => {
    setLiveHex(hex);
  }, []);

  const handleLiveVibePaletteDetected = useCallback((colors: RGB[]) => {
    setVibePalette(colors);
    if (onVibePaletteChangeRef.current) {
      onVibePaletteChangeRef.current(colors);
    }
  }, []);

  const handleCapture = useCallback(() => {
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);
    
    // Add to swatches list (shift oldest out if max reached)
    setSwatches(prev => {
      const newSwatches = [liveHex, ...prev.filter(c => c !== liveHex)].slice(0, MAX_SWATCHES);
      return newSwatches;
    });
    
    // Auto-select the newly captured color and dispatch BLE
    setActiveSwatch(liveHex);
    onColorDetected(liveHex);
  }, [liveHex, onColorDetected]);

  const handleSelectSwatch = useCallback((hex: string) => {
    Haptics.selectionAsync();
    setActiveSwatch(hex);
    onColorDetected(hex);
  }, [onColorDetected]);

  const handleModeToggle = useCallback((mode: 'SNIPER' | 'VIBE') => {
    Haptics.selectionAsync();
    setSubMode(mode);
    if (onSubModeChange) {
      onSubModeChange(mode);
    }
  }, [onSubModeChange]);

  const handleVibeApplyPress = useCallback(() => {
    Haptics.notificationAsync(Haptics.NotificationFeedbackType.Success);
    if (onVibeApply) {
      onVibeApply(vibePalette, isFlow);
    }
  }, [onVibeApply, vibePalette, isFlow]);

  const toggleFlow = useCallback(() => {
    Haptics.selectionAsync();
    setIsFlow(prev => !prev);
  }, []);

  return (
    <View style={styles.container}>
      {/* Dynamic unified Camera Viewport */}
      <CameraTracker
        isActive
        subMode={subMode}
        onColorDetected={handleLiveColorDetected}
        onVibePaletteDetected={handleLiveVibePaletteDetected}
      />

      <View style={styles.controlPanel}>
        {/* iOS-Style Glassmorphic Segmented Toggle Switch */}
        <View style={styles.subModeSwitchContainer}>
          <TouchableOpacity
            style={[styles.subModePill, subMode === 'SNIPER' && styles.subModePillActive]}
            onPress={() => handleModeToggle('SNIPER')}
            activeOpacity={0.8}
          >
            <MaterialCommunityIcons name="target" size={18} color={subMode === 'SNIPER' ? '#FFF' : 'rgba(255,255,255,0.4)'} />
            <Text style={[styles.subModeText, subMode === 'SNIPER' && styles.subModeTextActive]}>SNIPER</Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[styles.subModePill, subMode === 'VIBE' && styles.subModePillActive]}
            onPress={() => handleModeToggle('VIBE')}
            activeOpacity={0.8}
          >
            <MaterialCommunityIcons name="palette-swatch" size={18} color={subMode === 'VIBE' ? '#FFF' : 'rgba(255,255,255,0.4)'} />
            <Text style={[styles.subModeText, subMode === 'VIBE' && styles.subModeTextActive]}>VIBE CATCHER</Text>
          </TouchableOpacity>
        </View>

        {subMode === 'SNIPER' ? (
          /* ================== SNIPER CONTROLS ================== */
          <View style={styles.modeContainer}>
            <Text style={styles.instructionText}>
              Point reticle at a color, then tap capture to lock it in. Tap swatches to send to skates.
            </Text>

            <View style={styles.swatchRow}>
              {/* iOS Camera-Style Concentric Shutter / Capture Button */}
              <TouchableOpacity
                activeOpacity={0.7}
                onPress={handleCapture}
                style={styles.shutterOuterRing}
              >
                <View style={[styles.shutterInnerButton, { backgroundColor: liveHex }]} />
              </TouchableOpacity>

              <View style={styles.divider} />

              {/* Saved Swatches History */}
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
        ) : (
          /* ================== VIBE CATCHER CONTROLS ================== */
          <View style={styles.modeContainer}>
            <Text style={styles.instructionText}>
              Analyzing live frame. Dominant vibe palette extracted automatically below.
            </Text>

            {/* Liquid Gradient Preview Strip */}
            <View style={styles.gradientPreviewWrapper}>
              <LinearGradient
                colors={vibePalette.map(rgbToHexStr) as [string, string, ...string[]]}
                start={{ x: 0, y: 0 }}
                end={{ x: 1, y: 0 }}
                style={styles.gradientPreviewStrip}
              />
            </View>

            {/* Glassmorphic 3-Swatch Color Cards */}
            <View style={styles.vibeCardRow}>
              {vibePalette.map((color, index) => {
                const label = index === 0 ? 'FG' : index === 1 ? 'BG' : 'ACCENT';
                const hexStr = rgbToHexStr(color);
                return (
                  <View key={`vibe-card-${index}`} style={styles.vibeCard}>
                    <View style={[styles.vibeCardSwatch, { backgroundColor: hexStr }]} />
                    <Text style={styles.vibeCardLabel}>{label}</Text>
                    <Text style={styles.vibeCardHex}>{hexStr}</Text>
                  </View>
                );
              })}
            </View>

            <View style={styles.vibeFooterRow}>
              {/* Premium Dial Style Flow/Static Toggle */}
              <TouchableOpacity
                style={[styles.flowTogglePill, isFlow ? styles.flowTogglePillActive : styles.flowTogglePillStatic]}
                onPress={toggleFlow}
                activeOpacity={0.8}
              >
                <MaterialCommunityIcons
                  name={isFlow ? 'swap-horizontal' : 'play-pause'}
                  size={16}
                  color="#FFF"
                />
                <Text style={styles.flowToggleText}>
                  {isFlow ? 'FLOWING GRADIENT' : 'STATIC FREEZE'}
                </Text>
              </TouchableOpacity>

              {/* Shimmering "APPLY VIBE" Button */}
              <TouchableOpacity
                style={styles.applyButton}
                onPress={handleVibeApplyPress}
                activeOpacity={0.8}
              >
                <MaterialCommunityIcons name="flash-outline" size={20} color="#FFF" />
                <Text style={styles.applyButtonText}>APPLY VIBE</Text>
              </TouchableOpacity>
            </View>
          </View>
        )}
      </View>
    </View>
  );
});

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  controlPanel: {
    paddingVertical: Spacing.md,
    paddingHorizontal: Spacing.lg,
    backgroundColor: '#050505',
    alignItems: 'center',
    borderTopWidth: 1,
    borderTopColor: '#1A1A1A',
  },
  subModeSwitchContainer: {
    flexDirection: 'row',
    backgroundColor: 'rgba(255,255,255,0.04)',
    borderRadius: 24,
    padding: 4,
    width: '100%',
    marginBottom: Spacing.md,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.06)',
  },
  subModePill: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    paddingVertical: 10,
    borderRadius: 20,
    gap: 8,
  },
  subModePillActive: {
    backgroundColor: 'rgba(255,255,255,0.08)',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.15)',
    ...Platform.select({
      web: { boxShadow: '0px 2px 6px rgba(0,0,0,0.4)' },
      default: { shadowColor: '#000', shadowOffset: { width: 0, height: 2 }, shadowOpacity: 0.4, shadowRadius: 4, elevation: 4 }
    })
  },
  subModeText: {
    color: 'rgba(255,255,255,0.4)',
    fontSize: 12,
    fontFamily: 'Righteous',
    letterSpacing: 1,
  },
  subModeTextActive: {
    color: '#FFF',
  },
  modeContainer: {
    width: '100%',
    alignItems: 'center',
  },
  instructionText: {
    color: 'rgba(255,255,255,0.5)',
    fontSize: 11,
    fontFamily: 'Inter-Medium',
    textAlign: 'center',
    marginBottom: Spacing.md,
    lineHeight: 16,
  },
  swatchRow: {
    flexDirection: 'row',
    alignItems: 'center',
    width: '100%',
    marginBottom: Spacing.md,
  },
  shutterOuterRing: {
    width: 68,
    height: 68,
    borderRadius: 34,
    borderWidth: 4,
    borderColor: '#FFF',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'transparent',
    ...Platform.select({
      web: { boxShadow: '0px 0px 10px rgba(255,255,255,0.3)' },
      default: { shadowColor: '#FFF', shadowOffset: { width: 0, height: 0 }, shadowOpacity: 0.3, shadowRadius: 8 }
    })
  },
  shutterInnerButton: {
    width: 52,
    height: 52,
    borderRadius: 26,
    borderWidth: 2,
    borderColor: '#000',
  },
  divider: {
    width: 1,
    height: 48,
    backgroundColor: '#222',
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
    borderColor: '#181818',
    borderStyle: 'dashed',
    backgroundColor: '#0C0C0C',
  },
  hexPill: {
    backgroundColor: 'rgba(255,255,255,0.06)',
    paddingHorizontal: Spacing.lg,
    paddingVertical: 8,
    borderRadius: 20,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.05)',
  },
  hexText: {
    color: '#FFF',
    fontSize: 11,
    fontFamily: 'Inter-Bold',
    letterSpacing: 1,
  },
  gradientPreviewWrapper: {
    width: '100%',
    height: 36,
    borderRadius: 18,
    overflow: 'hidden',
    marginBottom: Spacing.md,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
  },
  gradientPreviewStrip: {
    flex: 1,
  },
  vibeCardRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    width: '100%',
    gap: Spacing.sm,
    marginBottom: Spacing.md,
  },
  vibeCard: {
    flex: 1,
    backgroundColor: 'rgba(255,255,255,0.03)',
    borderRadius: 16,
    padding: Spacing.sm,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.06)',
  },
  vibeCardSwatch: {
    width: 40,
    height: 40,
    borderRadius: 20,
    marginBottom: Spacing.xs,
    borderWidth: 2,
    borderColor: 'rgba(255,255,255,0.08)',
  },
  vibeCardLabel: {
    fontSize: 10,
    fontFamily: 'Inter-Bold',
    color: 'rgba(255,255,255,0.4)',
    marginBottom: 2,
  },
  vibeCardHex: {
    fontSize: 10,
    fontFamily: 'Righteous',
    color: '#FFF',
  },
  vibeFooterRow: {
    flexDirection: 'row',
    width: '100%',
    gap: Spacing.sm,
    marginTop: Spacing.xs,
  },
  flowTogglePill: {
    flex: 1.2,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 12,
    borderRadius: 24,
    gap: 8,
    borderWidth: 1,
  },
  flowTogglePillActive: {
    backgroundColor: 'rgba(0,255,128,0.1)',
    borderColor: 'rgba(0,255,128,0.2)',
  },
  flowTogglePillStatic: {
    backgroundColor: 'rgba(255,255,255,0.04)',
    borderColor: 'rgba(255,255,255,0.08)',
  },
  flowToggleText: {
    color: '#FFF',
    fontSize: 10,
    fontFamily: 'Righteous',
    letterSpacing: 0.5,
  },
  applyButton: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: Colors.primary,
    paddingVertical: 12,
    borderRadius: 24,
    gap: 8,
    ...Platform.select({
      web: { boxShadow: '0px 4px 10px rgba(255,0,128,0.3)' },
      default: { shadowColor: Colors.primary, shadowOffset: { width: 0, height: 4 }, shadowOpacity: 0.3, shadowRadius: 8, elevation: 8 }
    })
  },
  applyButtonText: {
    color: '#FFF',
    fontSize: 11,
    fontFamily: 'Righteous',
    letterSpacing: 1,
  },
});

export default CameraPanel;
