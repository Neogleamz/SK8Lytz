/**
 * MusicPanel.tsx — Music mode control panel.
 *
 * Renders the matrix style selector (Light Screen / Light Bar),
 * pattern navigator, mic source toggle (APP/DEVICE), and play button.
 *
 * Extracted from DockedController.tsx (Phase 3).
 */
import { MaterialCommunityIcons } from '@expo/vector-icons';
import React from 'react';
import { Text, TouchableOpacity, View } from 'react-native';
import { getMusicPatternLabel, MUSIC_PATTERNS } from '../../hooks/useMusicMode';
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
  musicSecondaryColor: string;
  handleMusicChange: (...args: any[]) => void;
  Colors: any;
  styles: any;
}

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
  musicSecondaryColor,
  handleMusicChange,
  Colors,
  styles,
}: MusicPanelProps) => {
  return (
    <View style={{ flex: 1, paddingHorizontal: Spacing.xs, paddingTop: Spacing.xs, overflow: 'hidden' }}>
      {/* Matrix Style Selector: Light Screen (0x27) vs Light Bar (0x26) */}
      <View style={{ flexDirection: 'row', gap: Spacing.sm, paddingHorizontal: Spacing.xs, marginTop: Spacing.xxs, marginBottom: Spacing.md, flexShrink: 0 }}>
        <TouchableOpacity
          onPress={() => {
            setMusicMatrixStyle(0x27);
            handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, musicSecondaryColor, 0x27);
          }}
          style={{
            flex: 1, paddingVertical: Spacing.md, borderRadius: 10, alignItems: 'center',
            backgroundColor: musicMatrixStyle === 0x27 ? Colors.primary + '33' : 'rgba(255,255,255,0.05)',
            borderWidth: 1.5, borderColor: musicMatrixStyle === 0x27 ? Colors.primary : 'rgba(255,255,255,0.1)'
          }}
        >
          <Text style={{ color: musicMatrixStyle === 0x27 ? '#FFF' : Colors.textMuted, fontWeight: '900', fontSize: 10, letterSpacing: 1 }}>LIGHT SCREEN</Text>
          <Text style={{ color: musicMatrixStyle === 0x27 ? Colors.primary : Colors.textMuted, fontSize: 8, opacity: 0.8 }}>0x27 (DENSE)</Text>
        </TouchableOpacity>
        <TouchableOpacity
          onPress={() => {
            setMusicMatrixStyle(0x26);
            handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, musicSecondaryColor, 0x26);
          }}
          style={{
            flex: 1, paddingVertical: Spacing.md, borderRadius: 10, alignItems: 'center',
            backgroundColor: musicMatrixStyle === 0x26 ? Colors.accent + '33' : 'rgba(255,255,255,0.05)',
            borderWidth: 1.5, borderColor: musicMatrixStyle === 0x26 ? Colors.accent : 'rgba(255,255,255,0.1)'
          }}
        >
          <Text style={{ color: musicMatrixStyle === 0x26 ? '#FFF' : Colors.textMuted, fontWeight: '900', fontSize: 10, letterSpacing: 1 }}>LIGHT BAR</Text>
          <Text style={{ color: musicMatrixStyle === 0x26 ? Colors.accent : Colors.textMuted, fontSize: 8, opacity: 0.8 }}>0x26 (BAR)</Text>
        </TouchableOpacity>
      </View>

      <View style={{ flex: 1, justifyContent: 'space-evenly' }}>
        {/* Pattern navigator */}
        <View style={[styles.musicToggleHeader, { justifyContent: 'center' }]}>
          <View style={[styles.musicModeIndicator, { alignItems: 'center' }]}>
            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
              <TouchableOpacity onPress={() => {
                const pid = musicPatternId > 1 ? musicPatternId - 1 : MUSIC_PATTERNS.length;
                setMusicPatternId(pid);
                handleMusicChange(pid);
              }} style={{ paddingHorizontal: Spacing.md }}>
                <Text style={{ color: '#FFF', fontSize: 20, fontWeight: 'bold' }}>{'<'}</Text>
              </TouchableOpacity>
              <View style={[styles.musicModeCircle, { width: 32, height: 32, borderRadius: 16 }]}>
                <Text style={[styles.musicModeNumber, { fontSize: 14 }]}>{musicPatternId}</Text>
              </View>
              <TouchableOpacity onPress={() => {
                const pid = musicPatternId < MUSIC_PATTERNS.length ? musicPatternId + 1 : 1;
                setMusicPatternId(pid);
                handleMusicChange(pid);
              }} style={{ paddingHorizontal: Spacing.md }}>
                <Text style={{ color: '#FFF', fontSize: 20, fontWeight: 'bold' }}>{'>'}</Text>
              </TouchableOpacity>
            </View>
            <Text style={[Typography.caption, { marginTop: Spacing.xs, color: Colors.primary, fontWeight: 'bold', fontSize: 13 }]}>
              {getMusicPatternLabel(musicPatternId)}
            </Text>
          </View>
        </View>

        {/* Mic source toggle */}
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
