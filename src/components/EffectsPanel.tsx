import { Spacing } from '../theme/theme';
/**
 * EffectsPanel.tsx — SK8Lytz Pro Effects Panel
 *
 * A complete, standalone Effects mode panel for the 33 built-in custom effects.
 * Inspired by the Zengge APK's step editor (kd/t0.java).
 * Fires 0x51 payloads — NOT 0x41 Symphony (0x41 is deprecated for production use).
 *
 * Displays all 33 custom effects in a scrollable grid. Each card shows:
 *  - Animated LED strip preview (StripView equivalent via CustomEffectVisualizer)
 *  - Effect name + ID badge
 *  - FG/BG color capability indicators
 *
 * Fires 0x51 payload via ZenggeProtocol.setCustomMode() on selection.
 * FG/BG color pickers are conditionally shown per effect capability.
 *
 * Protocol source: com/zengge/wifi/COMM/Protocol/x.java
 * Effect list source: kd/t0.java → q() method (33 effects, index = ID - 1)
 * FG/BG flags source: ed/c.java → n(), l() methods
 *
 * Platform: React Native (Android + iOS)
 */
import React, { useCallback, useEffect, useRef, useState } from 'react';
import {
    Animated,
    ScrollView, StyleSheet,
    Text, TouchableOpacity,
    View,
} from 'react-native';
import { SK8LYTZ_TEMPLATES } from '../constants/CustomEffects';
import { useTheme } from '../context/ThemeContext';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import CustomEffectVisualizer from './CustomEffectVisualizer';

// ─── Effect default BG colours (from kd/t0.java line 142) ────────────────────
// Effects 1,2,4,25,31,32 default BG to blue; all others to black
const EFFECTS_WITH_BLUE_DEFAULT_BG = new Set([1, 2, 4, 25, 31, 32]);

function getDefaultBgColor(effectId: number): string {
  return EFFECTS_WITH_BLUE_DEFAULT_BG.has(effectId) ? '#0000FF' : '#000000';
}

// ─── Hex → RGB helper ────────────────────────────────────────────────────────
function hexToRgb(hex: string): { r: number; g: number; b: number } {
  const h = (hex || '#000000').replace('#', '');
  return {
    r: parseInt(h.substring(0, 2), 16) || 0,
    g: parseInt(h.substring(2, 4), 16) || 0,
    b: parseInt(h.substring(4, 6), 16) || 0,
  };
}

// ─── HSL hue → hex color ─────────────────────────────────────────────────────
function hueToHex(hue: number, sat = 1, lit = 0.5): string {
  const k = (n: number) => (n + hue / 30) % 12;
  const col = (n: number) => lit - sat * Math.min(lit, 1 - lit) * Math.max(-1, Math.min(k(n) - 3, Math.min(9 - k(n), 1)));
  const toHex = (v: number) => Math.round(v * 255).toString(16).padStart(2, '0');
  return `#${toHex(col(0))}${toHex(col(8))}${toHex(col(4))}`;
}

// ─── Animated effect card ──────────────────────────────────────────────────────
const CARD_PULSE = new Animated.Value(1);

interface EffectCardProps {
  effect: typeof SK8LYTZ_TEMPLATES[0];
  isSelected: boolean;
  fgColor: string;
  bgColor: string;
  speed: number;
  points: number;
  segments: number;
  onSelect: () => void;
  Colors: any;
}

const EffectCard: React.FC<EffectCardProps> = React.memo(({
  effect, isSelected, fgColor, bgColor, speed, points, segments, onSelect, Colors
}) => {
  const pulseAnim = useRef(new Animated.Value(1)).current;

  useEffect(() => {
    if (isSelected) {
      Animated.loop(
        Animated.sequence([
          Animated.timing(pulseAnim, { toValue: 1.04, duration: 600, useNativeDriver: true }),
          Animated.timing(pulseAnim, { toValue: 1, duration: 600, useNativeDriver: true }),
        ])
      ).start();
    } else {
      pulseAnim.stopAnimation();
      pulseAnim.setValue(1);
    }
  }, [isSelected, pulseAnim]);

  return (
    <Animated.View style={{ transform: [{ scale: pulseAnim }], width: '48%', marginBottom: Spacing.sm }}>
      <TouchableOpacity
        id={`fx-card-${effect.id}`}
        activeOpacity={0.75}
        onPress={onSelect}
        style={[
          styles.effectCard,
          isSelected && {
            borderColor: '#00F0FF',
            backgroundColor: 'rgba(0,240,255,0.08)',
            shadowColor: '#00F0FF',
            shadowOpacity: 0.6,
            shadowRadius: 12,
            elevation: 10,
          }
        ]}
      >
        {/* Header row: ID badge + FG/BG dots */}
        <View style={styles.cardHeader}>
          <View style={[styles.idBadge, isSelected && { backgroundColor: '#00F0FF' }]}>
            <Text style={[styles.idText, isSelected && { color: '#000' }]}>{effect.id}</Text>
          </View>
          <View style={{ flexDirection: 'row', gap: Spacing.xxs }}>
            {effect.requiresForeground && (
              <View style={[styles.capDot, { backgroundColor: fgColor, borderColor: 'rgba(255,255,255,0.3)' }]} />
            )}
            {effect.requiresBackground && (
              <View style={[styles.capDot, { backgroundColor: bgColor, borderColor: 'rgba(255,255,255,0.3)' }]} />
            )}
          </View>
        </View>

        {/* Effect name */}
        <Text
          style={[styles.effectName, isSelected && { color: '#00F0FF' }]}
          numberOfLines={2}
          allowFontScaling={false}
        >
          {effect.name}
        </Text>

        {/* Live LED strip preview — the StripView equivalent */}
        <View style={styles.stripWrapper}>
          <CustomEffectVisualizer
            effectId={effect.id}
            speed={speed}
            fgColorHex={effect.requiresForeground ? fgColor : '#FF4400'}
            bgColorHex={effect.requiresBackground ? bgColor : '#000000'}
            points={points}
            segments={segments}
            direction={true}
          />
        </View>
      </TouchableOpacity>
    </Animated.View>
  );
});

// ─── Color Swatch Row ─────────────────────────────────────────────────────────
interface HueBarProps {
  label: string;
  hue: number;
  color: string;
  onHueChange: (h: number) => void;
  Colors: any;
}

const HUE_SWATCHES = [
  { h: 0,   hex: '#FF0000' },
  { h: 30,  hex: '#FF8000' },
  { h: 60,  hex: '#FFFF00' },
  { h: 120, hex: '#00FF00' },
  { h: 165, hex: '#00FF99' },
  { h: 180, hex: '#00FFFF' },
  { h: 210, hex: '#0066FF' },
  { h: 240, hex: '#0000FF' },
  { h: 270, hex: '#6600FF' },
  { h: 300, hex: '#FF00FF' },
  { h: 330, hex: '#FF0066' },
  { h: -1,  hex: '#FFFFFF' }, // White
  { h: -2,  hex: '#000000' }, // Black
];

const ColorSwatchRow: React.FC<HueBarProps> = ({ label, hue, color, onHueChange, Colors }) => (
  <View style={{ marginBottom: Spacing.sm }}>
    <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: Spacing.xs }}>
      <View style={{ width: 14, height: 14, borderRadius: 7, backgroundColor: color, marginRight: Spacing.sm, borderWidth: 1, borderColor: 'rgba(255,255,255,0.3)' }} />
      <Text style={{ color: Colors.textMuted, fontSize: 10, fontWeight: '700', letterSpacing: 1.5, textTransform: 'uppercase' }}>{label}</Text>
    </View>
    <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={{ gap: Spacing.sm, paddingVertical: Spacing.xxs }}>
      {HUE_SWATCHES.map((s) => {
        const isSelected = (s.h === -1 && color === '#FFFFFF') || (s.h === -2 && color === '#000000') || (s.h >= 0 && Math.abs(hue - s.h) < 20);
        return (
          <TouchableOpacity
            key={s.hex}
            onPress={() => onHueChange(s.h)}
            style={[
              styles.swatch,
              { backgroundColor: s.hex },
              isSelected && { borderWidth: 2.5, borderColor: '#FFFFFF', transform: [{ scale: 1.2 }] },
            ]}
          />
        );
      })}
    </ScrollView>
  </View>
);

// ─── Main EffectsPanel ────────────────────────────────────────────────────────
interface EffectsPanelProps {
  writeToDevice?: (payload: number[]) => Promise<void | boolean | 'partial'>;
  points?: number;
  segments?: number;
  speed: number;
  hwSettings?: any;
  /** Called on any effect/color change so DockedController speed slider can re-dispatch. */
  onStateChange?: (id: number, fg: string, bg: string) => void;
}

const EffectsPanel: React.FC<EffectsPanelProps> = ({
  writeToDevice,
  points = 16,
  segments = 1,
  speed,
  hwSettings,
  onStateChange,
}) => {
  const { Colors } = useTheme();

  // ── State — mirrors Zengge's step editor (kd/t0.java) ──────────────────────
  const [selectedEffectId, setSelectedEffectId] = useState<number>(1);
  const [fgHue, setFgHue] = useState<number>(180);
  const [bgHue, setBgHue] = useState<number>(240);
  const [fgColor, setFgColor] = useState<string>('#00FFFF');
  const [bgColor, setBgColor] = useState<string>(getDefaultBgColor(1));
  const [showColors, setShowColors] = useState<boolean>(true);

  const devicePoints = hwSettings?.ledPoints || points || 16;
  const deviceSegments = hwSettings?.segments || segments || 1;

  // ── Dispatch 0x51 on state change ─────────────────────────────────────────
  const dispatchEffect = useCallback((
    effectId: number,
    fg: string,
    bg: string,
    spd: number
  ) => {
    if (!writeToDevice) return;
    const fgRgb = hexToRgb(fg);
    const bgRgb = hexToRgb(bg);
    // Use compact format (1 active step = 20 bytes wrapped) to test if hardware
    // accepts variable-length 0x51 vs requiring the full 32-slot 291-byte format.
    const payload = ZenggeProtocol.setCustomModeCompact([{
      mode: effectId,
      speed: Math.max(1, Math.min(100, Math.round(spd))),
      color1: fgRgb,
      color2: bgRgb,
    }]);
    writeToDevice(payload);
    onStateChange?.(effectId, fg, bg);
  }, [writeToDevice, onStateChange]);

  // ── Effect selection handler ───────────────────────────────────────────────
  const handleSelectEffect = useCallback((effectId: number) => {
    const effect = SK8LYTZ_TEMPLATES.find(e => e.id === effectId);
    if (!effect) return;

    // Apply Zengge default BG color (blue for certain effects, black otherwise)
    const newBg = getDefaultBgColor(effectId);
    setBgColor(newBg);
    setBgHue(EFFECTS_WITH_BLUE_DEFAULT_BG.has(effectId) ? 240 : -2);
    setSelectedEffectId(effectId);
    dispatchEffect(effectId, fgColor, newBg, speed);
  }, [fgColor, speed, dispatchEffect]);

  // ── FG Color hue change ────────────────────────────────────────────────────
  const handleFgHueChange = useCallback((h: number) => {
    setFgHue(h);
    const hex = h === -1 ? '#FFFFFF' : h === -2 ? '#000000' : hueToHex(h);
    setFgColor(hex);
    // dispatchEffect already calls onStateChange internally
    dispatchEffect(selectedEffectId, hex, bgColor, speed);
  }, [selectedEffectId, bgColor, speed, dispatchEffect]);

  // ── BG Color hue change ────────────────────────────────────────────────────
  const handleBgHueChange = useCallback((h: number) => {
    setBgHue(h);
    const hex = h === -1 ? '#FFFFFF' : h === -2 ? '#000000' : hueToHex(h);
    setBgColor(hex);
    // dispatchEffect already calls onStateChange internally
    dispatchEffect(selectedEffectId, fgColor, hex, speed);
  }, [selectedEffectId, fgColor, speed, dispatchEffect]);

  // ── Speed change: EffectsPanel doesn't own the slider, but re-dispatches
  // when the DockedController speed slider fires. Trigger via useEffect:
  useEffect(() => {
    if (selectedEffectId) dispatchEffect(selectedEffectId, fgColor, bgColor, speed);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [speed]);

  // Fire immediately on mount so device reacts when user taps the Effects tab
  useEffect(() => {
    dispatchEffect(selectedEffectId, fgColor, bgColor, speed);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const selectedEffect = SK8LYTZ_TEMPLATES.find(e => e.id === selectedEffectId);

  return (
    <View style={{ flex: 1 }}>
      {/* ── Color controls (conditional per effect capability) ────── */}
      {selectedEffect && (selectedEffect.requiresForeground || selectedEffect.requiresBackground) && (
        <View style={{ paddingHorizontal: Spacing.xs, paddingBottom: Spacing.xs }}>
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: Spacing.sm }}>
            <Text style={{ color: Colors.textMuted, fontSize: 11, fontWeight: '800', letterSpacing: 1.5 }}>COLOR SETTINGS</Text>
            <TouchableOpacity onPress={() => setShowColors(!showColors)} style={{ paddingHorizontal: Spacing.md, paddingVertical: Spacing.xs, backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 12 }}>
              <Text style={{ color: Colors.primary, fontSize: 10, fontWeight: '800' }}>{showColors ? 'HIDE' : 'SHOW'}</Text>
            </TouchableOpacity>
          </View>
          {showColors && (
            <View>
              {selectedEffect.requiresForeground && (
                <ColorSwatchRow
                  label="Foreground"
                  hue={fgHue}
                  color={fgColor}
                  onHueChange={handleFgHueChange}
                  Colors={Colors}
                />
              )}
              {selectedEffect.requiresBackground && (
                <ColorSwatchRow
                  label="Background"
                  hue={bgHue}
                  color={bgColor}
                  onHueChange={handleBgHueChange}
                  Colors={Colors}
                />
              )}
            </View>
          )}
        </View>
      )}

      {/* ── Effect grid — mirrors Zengge's scrollable list ─────────── */}
      <ScrollView
        style={{ flex: 1 }}
        contentContainerStyle={{
          flexDirection: 'row',
          flexWrap: 'wrap',
          justifyContent: 'space-between',
          paddingHorizontal: Spacing.xs,
          paddingBottom: Spacing.md,
        }}
        showsVerticalScrollIndicator={false}
        keyboardShouldPersistTaps="handled"
      >
        {SK8LYTZ_TEMPLATES.map((effect) => (
          <EffectCard
            key={effect.id}
            effect={effect}
            isSelected={selectedEffectId === effect.id}
            fgColor={fgColor}
            bgColor={bgColor}
            speed={speed}
            points={devicePoints}
            segments={deviceSegments}
            onSelect={() => handleSelectEffect(effect.id)}
            Colors={Colors}
          />
        ))}
      </ScrollView>
    </View>
  );
};

// ─── Styles ──────────────────────────────────────────────────────────────────
const styles = StyleSheet.create({
  effectCard: {
    backgroundColor: 'rgba(255,255,255,0.04)',
    borderRadius: 12,
    borderWidth: 1.5,
    borderColor: 'rgba(255,255,255,0.08)',
    padding: Spacing.sm,
    overflow: 'hidden',
  },
  cardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: Spacing.xs,
  },
  idBadge: {
    backgroundColor: 'rgba(255,255,255,0.1)',
    borderRadius: 6,
    paddingHorizontal: Spacing.xs,
    paddingVertical: 1,
    minWidth: 22,
    alignItems: 'center',
  },
  idText: {
    color: 'rgba(255,255,255,0.5)',
    fontSize: 9,
    fontWeight: '900',
  },
  capDot: {
    width: 8,
    height: 8,
    borderRadius: 4,
    borderWidth: 1,
  },
  effectName: {
    color: 'rgba(255,255,255,0.75)',
    fontSize: 10,
    fontWeight: '700',
    lineHeight: 13,
    marginBottom: Spacing.xs,
    minHeight: 26,
  },
  stripWrapper: {
    height: 9,
    borderRadius: 5,
    overflow: 'hidden',
  },
  swatch: {
    width: 26,
    height: 26,
    borderRadius: 13,
    borderWidth: 1.5,
    borderColor: 'rgba(255,255,255,0.15)',
  },
});

export default EffectsPanel;
