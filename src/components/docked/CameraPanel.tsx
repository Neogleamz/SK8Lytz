import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';
import { LinearGradient } from 'expo-linear-gradient';
import * as Haptics from 'expo-haptics';
import React, { useState, useCallback, useRef } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, Platform } from 'react-native';
import CameraTracker from '../CameraTracker';
import { Spacing, Colors } from '../../theme/theme';
import { RGB } from '../../utils/kMeansPalette';
import { rgbToHex } from '../../utils/ColorUtils';

interface CameraPanelProps {
  onColorDetected: (hex: string) => void;
  onVibeApply?: (colors: RGB[], isFlow: boolean) => void;
  onVibePaletteChange?: (colors: RGB[]) => void;
  onSubModeChange?: (mode: 'SNIPER' | 'VIBE') => void;
  swatches?: string[];
  onSwatchesChange?: (swatches: string[]) => void;
}

const MAX_SWATCHES = 5;

const CameraPanel = React.memo(({ onColorDetected, onVibeApply, onVibePaletteChange, onSubModeChange, swatches: externalSwatches, onSwatchesChange }: CameraPanelProps) => {
  const [subMode, setSubMode] = useState<'SNIPER' | 'VIBE'>('SNIPER');

  // SNIPER Mode State
  const [liveHex, setLiveHex] = useState<string>('#FFFFFF');
  const [localSwatches, setLocalSwatches] = useState<string[]>([]);
  const swatches = externalSwatches ?? localSwatches;
  
  const handleSetSwatches = useCallback((newSwatches: string[] | ((prev: string[]) => string[])) => {
    setLocalSwatches(prev => {
      const next = typeof newSwatches === 'function' ? newSwatches(prev) : newSwatches;
      if (onSwatchesChange) onSwatchesChange(next);
      return next;
    });
  }, [onSwatchesChange]);

  const [activeSwatch, setActiveSwatch] = useState<string | null>(null);
  const liveColorRef = useRef<string>('#FFFFFF');

  // VIBE Mode State
  const [vibePalette, setVibePalette] = useState<RGB[]>([
    { r: 255, g: 0, b: 0 },
    { r: 0, g: 255, b: 0 },
    { r: 0, g: 0, b: 255 },
  ]);
  const [isFlow, setIsFlow] = useState<boolean>(true);

  // Update the liveHex state so the capture button previews the live color
  const handleLiveColorDetected = useCallback((hex: string) => {
    setLiveHex(hex);
  }, []);

  const handleLiveVibePaletteDetected = useCallback((colors: RGB[]) => {
    setVibePalette(colors);
    if (onVibePaletteChange) onVibePaletteChange(colors);
  }, [onVibePaletteChange]);

  const handleCapture = useCallback(() => {
    if (Platform.OS !== 'web') { Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium).catch(() => {}); }
    const capturedColor = liveColorRef.current;
    setLiveHex(capturedColor);
    
    // Calculate new swatches based on the current PROPS swatches, not just local state
    const currentSwatches = externalSwatches ?? localSwatches;
    const newSwatches = [capturedColor, ...currentSwatches.filter(c => c !== capturedColor)].slice(0, MAX_SWATCHES);
    handleSetSwatches(newSwatches);
    
    setActiveSwatch(capturedColor);
    onColorDetected(capturedColor);
  }, [onColorDetected, externalSwatches, localSwatches, handleSetSwatches]);

  const handleSelectSwatch = useCallback((hex: string) => {
    if (Platform.OS !== 'web') { Haptics.selectionAsync().catch(() => {}); }
    setActiveSwatch(hex);
    onColorDetected(hex);
  }, [onColorDetected]);

  const handleModeToggle = useCallback((mode: 'SNIPER' | 'VIBE') => {
    if (Platform.OS !== 'web') { Haptics.selectionAsync().catch(() => {}); }
    setSubMode(mode);
    if (onSubModeChange) onSubModeChange(mode);
  }, [onSubModeChange]);

  const handleVibeApplyPress = useCallback(() => {
    if (Platform.OS !== 'web') { Haptics.notificationAsync(Haptics.NotificationFeedbackType.Success).catch(() => {}); }
    if (onVibeApply) onVibeApply(vibePalette, isFlow);
  }, [onVibeApply, vibePalette, isFlow]);

  const toggleFlow = useCallback(() => {
    if (Platform.OS !== 'web') { Haptics.selectionAsync().catch(() => {}); }
    setIsFlow(prev => !prev);
  }, []);

  const renderSwatch = useCallback((swatch: string, index: number) => (
    <TouchableOpacity
      key={`swatch-${swatch}-${index}`}
      onPress={() => handleSelectSwatch(swatch)}
      style={[
        styles.swatchDot,
        { backgroundColor: swatch },
        activeSwatch === swatch && styles.swatchDotActive,
      ]}
    />
  ), [handleSelectSwatch, activeSwatch]);

  return (
    <View style={styles.container}>
      {/* ── Full-bleed Camera Viewfinder ─────────────────────────────── */}
      <CameraTracker
        isActive
        subMode={subMode}
        onColorDetected={handleLiveColorDetected}
        onVibePaletteDetected={handleLiveVibePaletteDetected}
        liveColorRef={liveColorRef}
      />

      {/* ── Floating Overlay — sits over the camera at full canvas size ── */}
      {/* pointerEvents="box-none" lets untouched camera areas remain interactive */}
      <View style={styles.overlayLayer} pointerEvents="box-none">

        {/* ══ TOP CENTER: SNIPER / VIBE toggle ════════════════════════ */}
        <View style={styles.topBar} pointerEvents="box-none">
          <View style={styles.togglePill}>
            <TouchableOpacity
              style={[styles.toggleOption, subMode === 'SNIPER' && styles.toggleOptionActive]}
              onPress={() => handleModeToggle('SNIPER')}
              activeOpacity={0.8}
            >
              <MaterialCommunityIcons
                name="target"
                size={15}
                color={subMode === 'SNIPER' ? '#FFF' : 'rgba(255,255,255,0.5)'}
              />
              <Text style={[styles.toggleText, subMode === 'SNIPER' && styles.toggleTextActive]}>
                SNIPER
              </Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={[styles.toggleOption, subMode === 'VIBE' && styles.toggleOptionActive]}
              onPress={() => handleModeToggle('VIBE')}
              activeOpacity={0.8}
            >
              <MaterialCommunityIcons
                name="palette-swatch"
                size={15}
                color={subMode === 'VIBE' ? '#FFF' : 'rgba(255,255,255,0.5)'}
              />
              <Text style={[styles.toggleText, subMode === 'VIBE' && styles.toggleTextActive]}>
                VIBE
              </Text>
            </TouchableOpacity>
          </View>
        </View>

        {subMode === 'SNIPER' ? (
          /* ══ SNIPER BOTTOM ROW: Shutter (left) + Swatches (right) ══ */
          <View style={styles.sniperBottomRow} pointerEvents="box-none">

            {/* Bottom-Left: Concentric shutter button */}
            <TouchableOpacity
              activeOpacity={0.75}
              onPress={handleCapture}
              style={styles.shutterOuter}
            >
              {/* Inner circle shows the live captured color */}
              <View style={[styles.shutterInner, { backgroundColor: liveHex }]} />
            </TouchableOpacity>

            {/* Flex spacer — pushes swatches to the right */}
            <View style={{ flex: 1 }} pointerEvents="none" />

            {/* Bottom-Right: Swatch dots, newest rightmost */}
            <View style={styles.swatchStrip} pointerEvents="box-none">
              {swatches.map(renderSwatch)}
              {/* Empty placeholder dots */}
              {Array.from({ length: Math.max(0, MAX_SWATCHES - swatches.length) }).map((_, i) => (
                <View key={`empty-${i}`} style={styles.swatchDotEmpty} />
              ))}
            </View>
          </View>
        ) : (
          /* ══ VIBE BOTTOM: Cards + gradient strip + apply button ════ */
          <View style={styles.vibeBottomStack} pointerEvents="box-none">

            {/* Semi-transparent scrim so cards are legible over camera */}
            <View style={styles.vibeScrim} pointerEvents="none" />

            {/* Gradient preview strip */}
            <View style={styles.gradientBar} pointerEvents="none">
              <LinearGradient
                colors={vibePalette.map(c => rgbToHex(c.r, c.g, c.b)) as [string, string, ...string[]]}
                start={{ x: 0, y: 0 }}
                end={{ x: 1, y: 0 }}
                style={StyleSheet.absoluteFillObject}
              />
            </View>

            {/* 3 color cards: FG / BG / ACCENT */}
            <View style={styles.vibeCardRow} pointerEvents="none">
              {vibePalette.map((color, index) => {
                const label = ['FG', 'BG', 'ACCENT'][index];
                const hex = rgbToHex(color.r, color.g, color.b);
                return (
                  <View key={`vcard-${index}`} style={styles.vibeCard}>
                    <View style={[styles.vibeCardSwatch, { backgroundColor: hex }]} />
                    <Text style={styles.vibeCardLabel}>{label}</Text>
                    <Text style={styles.vibeCardHex}>{hex}</Text>
                  </View>
                );
              })}
            </View>

            {/* Flow toggle + Apply Vibe button */}
            <View style={styles.vibeActionRow} pointerEvents="box-none">
              <TouchableOpacity
                style={[styles.flowPill, isFlow ? styles.flowPillOn : styles.flowPillOff]}
                onPress={toggleFlow}
                activeOpacity={0.8}
              >
                <MaterialCommunityIcons
                  name={isFlow ? 'swap-horizontal' : 'play-pause'}
                  size={13}
                  color="#FFF"
                />
                <Text style={styles.flowPillText}>
                  {isFlow ? 'FLOW' : 'FREEZE'}
                </Text>
              </TouchableOpacity>

              <TouchableOpacity
                style={styles.applyVibeBtn}
                onPress={handleVibeApplyPress}
                activeOpacity={0.8}
              >
                <MaterialCommunityIcons name="flash-outline" size={16} color="#FFF" />
                <Text style={styles.applyVibeBtnText}>APPLY VIBE</Text>
              </TouchableOpacity>
            </View>
          </View>
        )}
      </View>
    </View>
  );
});

const styles = StyleSheet.create({
  // ── Root container — gives camera ALL available flex space ──────────────
  container: {
    flex: 1,
  },

  // ── Overlay layer — floats over the camera at full canvas size ──────────
  overlayLayer: {
    ...StyleSheet.absoluteFillObject,
    justifyContent: 'space-between',
    paddingTop: Spacing.md,
    paddingBottom: Spacing.lg,
    paddingHorizontal: Spacing.md,
  },

  // ── TOP BAR: mode toggle ────────────────────────────────────────────────
  topBar: {
    alignItems: 'center',
  },
  togglePill: {
    flexDirection: 'row',
    backgroundColor: 'rgba(0,0,0,0.5)',
    borderRadius: 22,
    padding: 3,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.14)',
    alignSelf: 'center',
    ...Platform.select({
      web: { backdropFilter: 'blur(12px)', boxShadow: '0 4px 16px rgba(0,0,0,0.5)' },
      default: {
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 4 },
        shadowOpacity: 0.5,
        shadowRadius: 10,
        elevation: 10,
      },
    }),
  },
  toggleOption: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 8,
    paddingHorizontal: 18,
    borderRadius: 19,
    gap: 6,
  },
  toggleOptionActive: {
    backgroundColor: 'rgba(255,255,255,0.18)',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.22)',
  },
  toggleText: {
    color: 'rgba(255,255,255,0.5)',
    fontSize: 11,
    fontFamily: 'Righteous',
    letterSpacing: 1,
  },
  toggleTextActive: {
    color: '#FFF',
  },

  // ── SNIPER BOTTOM ROW ───────────────────────────────────────────────────
  sniperBottomRow: {
    flexDirection: 'row',
    alignItems: 'flex-end',
  },

  // Shutter button — bottom-left, iOS camera style
  shutterOuter: {
    width: 64,
    height: 64,
    borderRadius: 32,
    borderWidth: 4,
    borderColor: '#FFF',
    backgroundColor: 'rgba(0,0,0,0.15)',
    justifyContent: 'center',
    alignItems: 'center',
    ...Platform.select({
      web: { boxShadow: '0 0 16px rgba(255,255,255,0.45)' },
      default: {
        shadowColor: '#FFF',
        shadowOffset: { width: 0, height: 0 },
        shadowOpacity: 0.45,
        shadowRadius: 10,
        elevation: 12,
      },
    }),
  },
  shutterInner: {
    width: 48,
    height: 48,
    borderRadius: 24,
    borderWidth: 2,
    borderColor: 'rgba(0,0,0,0.35)',
  },

  // Swatch dots — bottom-right
  swatchStrip: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Spacing.xs,
  },
  swatchDot: {
    width: 40,
    height: 40,
    borderRadius: 20,
    borderWidth: 2,
    borderColor: 'rgba(255,255,255,0.2)',
    ...Platform.select({
      web: { boxShadow: '0 2px 8px rgba(0,0,0,0.5)' },
      default: {
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.5,
        shadowRadius: 6,
        elevation: 8,
      },
    }),
  },
  swatchDotActive: {
    borderColor: '#FFF',
    transform: [{ scale: 1.15 }],
    ...Platform.select({
      web: { boxShadow: '0 0 14px rgba(255,255,255,0.65)' },
      default: {
        shadowColor: '#FFF',
        shadowOffset: { width: 0, height: 0 },
        shadowOpacity: 0.65,
        shadowRadius: 12,
        elevation: 14,
      },
    }),
  },
  swatchDotEmpty: {
    width: 40,
    height: 40,
    borderRadius: 20,
    borderWidth: 2,
    borderColor: 'rgba(255,255,255,0.13)',
    borderStyle: 'dashed',
    backgroundColor: 'rgba(0,0,0,0.2)',
  },

  // ── VIBE MODE BOTTOM STACK ──────────────────────────────────────────────
  vibeBottomStack: {
    gap: Spacing.xs,
  },
  vibeScrim: {
    position: 'absolute',
    bottom: -Spacing.lg,
    left: -Spacing.md,
    right: -Spacing.md,
    height: 240,
    backgroundColor: 'rgba(0,0,0,0.52)',
  },
  gradientBar: {
    height: 26,
    borderRadius: 13,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.12)',
  },
  vibeCardRow: {
    flexDirection: 'row',
    gap: Spacing.xs,
  },
  vibeCard: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.42)',
    borderRadius: 12,
    padding: Spacing.xs,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.08)',
  },
  vibeCardSwatch: {
    width: 30,
    height: 30,
    borderRadius: 15,
    marginBottom: 3,
    borderWidth: 1.5,
    borderColor: 'rgba(255,255,255,0.12)',
  },
  vibeCardLabel: {
    fontSize: 9,
    fontFamily: 'Inter-Bold',
    color: 'rgba(255,255,255,0.45)',
    letterSpacing: 0.5,
  },
  vibeCardHex: {
    fontSize: 8,
    fontFamily: 'Righteous',
    color: 'rgba(255,255,255,0.8)',
  },
  vibeActionRow: {
    flexDirection: 'row',
    gap: Spacing.sm,
    alignItems: 'center',
  },
  flowPill: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 9,
    paddingHorizontal: 14,
    borderRadius: 18,
    gap: 5,
    borderWidth: 1,
  },
  flowPillOn: {
    backgroundColor: 'rgba(0,255,128,0.15)',
    borderColor: 'rgba(0,255,128,0.3)',
  },
  flowPillOff: {
    backgroundColor: 'rgba(255,255,255,0.06)',
    borderColor: 'rgba(255,255,255,0.12)',
  },
  flowPillText: {
    color: '#FFF',
    fontSize: 10,
    fontFamily: 'Righteous',
    letterSpacing: 0.5,
  },
  applyVibeBtn: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: Colors.primary,
    paddingVertical: 10,
    borderRadius: 18,
    gap: 6,
    ...Platform.select({
      web: { boxShadow: '0 4px 14px rgba(255,0,128,0.45)' },
      default: {
        shadowColor: Colors.primary,
        shadowOffset: { width: 0, height: 4 },
        shadowOpacity: 0.45,
        shadowRadius: 10,
        elevation: 10,
      },
    }),
  },
  applyVibeBtnText: {
    color: '#FFF',
    fontSize: 11,
    fontFamily: 'Righteous',
    letterSpacing: 1,
  },
});

export default CameraPanel;
