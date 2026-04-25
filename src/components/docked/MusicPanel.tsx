/**
 * MusicPanel.tsx — Music mode control panel.
 *
 * Renders the matrix style selector (Light Screen / Light Bar),
 * pattern navigator (16 or 30 patterns depending on matrix),
 * conditional FG/BG color pickers (per MusicDictionary colorMode gate),
 * mic source toggle (APP/DEVICE), and play button.
 *
 * Extracted from DockedController.tsx (Phase 3).
 * Updated: Full 46-profile support (16 Light Bar + 30 Light Screen).
 */
import { MaterialCommunityIcons } from '@expo/vector-icons';
import React, { useCallback } from 'react';
import { Text, TouchableOpacity, View } from 'react-native';
import {
  getActiveMusicProfile,
  getMusicPatternLabel,
  getMusicPatternMax,
} from '../../hooks/useMusicMode';
import { Spacing, Typography } from '../../theme/theme';

interface MusicPanelProps {
  musicPatternId: number;
  setMusicPatternId: (id: number) => void;
  micSource: 'APP' | 'DEVICE';
  setMicSource: (src: 'APP' | 'DEVICE') => void;
  musicMatrixStyle: number;
  setMusicMatrixStyle: (style: number) => void;
  micSensitivity: number;
  brightness: number;
  musicPrimaryColor: string;
  setMusicPrimaryColor: (hex: string) => void;
  musicSecondaryColor: string;
  setMusicSecondaryColor: (hex: string) => void;
  speed: number;
  setSpeed: (v: number) => void;
  handleMusicChange: (...args: any[]) => void;
  Colors: any;
  styles: any;
}

const ColorSwatch = React.memo(({
  color, label, onPress, Colors,
}: { color: string; label: string; onPress: () => void; Colors: any }) => (
  <TouchableOpacity
    onPress={onPress}
    style={{
      flexDirection: 'row', alignItems: 'center', gap: Spacing.xs,
      backgroundColor: 'rgba(255,255,255,0.06)', borderRadius: 10,
      paddingVertical: Spacing.sm, paddingHorizontal: Spacing.md,
      flex: 1, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)',
    }}
  >
    <View style={{
      width: 22, height: 22, borderRadius: 11,
      backgroundColor: color,
      borderWidth: 2, borderColor: 'rgba(255,255,255,0.3)',
    }} />
    <Text style={{ color: Colors.textMuted, fontSize: 10, fontWeight: '700', letterSpacing: 0.5 }}>
      {label}
    </Text>
  </TouchableOpacity>
));

const MusicPanel = React.memo(({
  musicPatternId,
  setMusicPatternId,
  micSource,
  setMicSource,
  musicMatrixStyle,
  setMusicMatrixStyle,
  micSensitivity,
  brightness,
  musicPrimaryColor,
  setMusicPrimaryColor,
  musicSecondaryColor,
  setMusicSecondaryColor,
  speed,
  setSpeed,
  handleMusicChange,
  Colors,
  styles,
}: MusicPanelProps) => {

  // Active profile — resolves colorMode, name, etc.
  const activeProfile = getActiveMusicProfile(musicMatrixStyle, musicPatternId);
  const patternMax = getMusicPatternMax(musicMatrixStyle);

  const onPrev = useCallback(() => {
    const pid = musicPatternId > 1 ? musicPatternId - 1 : patternMax;
    setMusicPatternId(pid);
    handleMusicChange(pid);
  }, [musicPatternId, patternMax, setMusicPatternId, handleMusicChange]);

  const onNext = useCallback(() => {
    const pid = musicPatternId < patternMax ? musicPatternId + 1 : 1;
    setMusicPatternId(pid);
    handleMusicChange(pid);
  }, [musicPatternId, patternMax, setMusicPatternId, handleMusicChange]);

  const onMatrixSwitch = useCallback((matrix: number) => {
    // Reset to ID 1 when switching matrices to prevent out-of-range sends.
    // Light Bar max = 16, Light Screen max = 30. IDs > 16 are invalid on 0x26.
    setMusicMatrixStyle(matrix);
    setMusicPatternId(1);
    handleMusicChange(1, micSensitivity, brightness, micSource, musicPrimaryColor, musicSecondaryColor, matrix);
  }, [setMusicMatrixStyle, setMusicPatternId, handleMusicChange, micSensitivity, brightness, micSource, musicPrimaryColor, musicSecondaryColor]);

  return (
    <View style={{ flex: 1, paddingHorizontal: Spacing.xs, paddingTop: Spacing.xs, overflow: 'hidden' }}>

      {/* ── Pattern Navigator (Pill) with Matrix Toggles ───────────────── */}

        <View style={[styles.musicToggleHeader, { justifyContent: 'space-between', paddingHorizontal: Spacing.xs }]}>
          
          {/* Light Screen Toggle */}
          <TouchableOpacity
            onPress={() => onMatrixSwitch(0x27)}
            style={{
              paddingVertical: Spacing.xs, paddingHorizontal: Spacing.sm, borderRadius: 12, alignItems: 'center',
              backgroundColor: musicMatrixStyle === 0x27 ? Colors.primary + '33' : 'transparent',
              minWidth: 75
            }}
          >
            <MaterialCommunityIcons name="monitor" size={24} color={musicMatrixStyle === 0x27 ? '#FFF' : Colors.textMuted} />
            <Text style={{ color: musicMatrixStyle === 0x27 ? '#FFF' : Colors.textMuted, fontWeight: '900', fontSize: 8, marginTop: 2 }}>LED Screen</Text>
          </TouchableOpacity>

          {/* Pattern Navigator */}
          <View style={[styles.musicModeIndicator, { alignItems: 'center', flex: 1, marginHorizontal: Spacing.xxs }]}>
            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
              <TouchableOpacity onPress={onPrev} style={{ paddingHorizontal: Spacing.md }}>
                <Text style={{ color: '#FFF', fontSize: 26, fontWeight: 'bold' }}>{'<'}</Text>
              </TouchableOpacity>
              <View style={[styles.musicModeCircle, { width: 44, height: 44, borderRadius: 22 }]}>
                <Text style={[styles.musicModeNumber, { fontSize: 18 }]}>{musicPatternId}</Text>
              </View>
              <TouchableOpacity onPress={onNext} style={{ paddingHorizontal: Spacing.md }}>
                <Text style={{ color: '#FFF', fontSize: 26, fontWeight: 'bold' }}>{'>'}</Text>
              </TouchableOpacity>
            </View>
            <Text style={[Typography.caption, { marginTop: Spacing.xs, color: Colors.primary, fontWeight: 'bold', fontSize: 12, textAlign: 'center' }]} numberOfLines={1}>
              {getMusicPatternLabel(musicMatrixStyle, musicPatternId)}
            </Text>
            <Text style={{ color: Colors.textMuted, fontSize: 9, marginTop: 2, opacity: 0.6 }}>
              {musicPatternId} / {patternMax}
            </Text>
          </View>

          {/* Light Bar Toggle */}
          <TouchableOpacity
            onPress={() => onMatrixSwitch(0x26)}
            style={{
              paddingVertical: Spacing.xs, paddingHorizontal: Spacing.sm, borderRadius: 12, alignItems: 'center',
              backgroundColor: musicMatrixStyle === 0x26 ? Colors.accent + '33' : 'transparent',
              minWidth: 75
            }}
          >
            <MaterialCommunityIcons name="led-strip-variant" size={24} color={musicMatrixStyle === 0x26 ? '#FFF' : Colors.textMuted} />
            <Text style={{ color: musicMatrixStyle === 0x26 ? '#FFF' : Colors.textMuted, fontWeight: '900', fontSize: 8, marginTop: 2 }}>LED Bar</Text>
          </TouchableOpacity>
          
        </View>

      <View style={{ flex: 1, justifyContent: 'space-evenly' }}>



        {/* ── Mic Source Toggle ─────────────────────────────────────── */}
        <View style={styles.micControlSection}>
          <TouchableOpacity
            style={[styles.micIconBtn, micSource === 'APP' && styles.micBtnActive]}
            onPress={() => {
              setMicSource('APP');
              handleMusicChange(musicPatternId, micSensitivity, brightness, 'APP');
            }}
          >
            <View style={[styles.micIconCircle, micSource === 'APP' && { backgroundColor: Colors.primary }]}>
              <MaterialCommunityIcons name="microphone-outline" size={20} color={micSource === 'APP' ? '#FFF' : Colors.textMuted} />
            </View>
            <Text style={[styles.micSubText, micSource === 'APP' && { color: Colors.primary, fontWeight: 'bold' }]}>APP MIC</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={styles.playButtonMain}
            onPress={() => handleMusicChange()}
          >
            <View style={styles.playIconInner}>
              <MaterialCommunityIcons name="play" size={32} color="#FFF" />
            </View>
          </TouchableOpacity>

          <TouchableOpacity
            style={[styles.micIconBtn, micSource === 'DEVICE' && styles.micBtnActive]}
            onPress={() => {
              setMicSource('DEVICE');
              handleMusicChange(musicPatternId, micSensitivity, brightness, 'DEVICE');
            }}
          >
            <View style={[styles.micIconCircle, micSource === 'DEVICE' && { backgroundColor: Colors.primary }]}>
              <MaterialCommunityIcons name="bluetooth-audio" size={20} color={micSource === 'DEVICE' ? '#FFF' : Colors.textMuted} />
            </View>
            <Text style={[styles.micSubText, micSource === 'DEVICE' && { color: Colors.primary, fontWeight: 'bold' }]}>DEVICE MIC</Text>
          </TouchableOpacity>
        </View>

      </View>
    </View>
  );
});

export default MusicPanel;
