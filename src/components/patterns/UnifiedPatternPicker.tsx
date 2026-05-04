import React, { useCallback, useEffect, useRef } from 'react';
import { StyleSheet, View } from 'react-native';
import { buildPatternPayload } from '../../protocols/PatternEngine';
import { useTheme } from '../../context/ThemeContext';
import { hexToRgb } from '../../utils/ColorUtils';
import { PatternPickerTab } from './PatternPickerTab';

interface UnifiedPatternPickerProps {
  writeToDevice?: (payload: number[]) => Promise<void | boolean | 'partial'>;
  points?: number;
  segments?: number;
  speed: number;
  brightness?: number;
  direction?: number;
  hwSettings?: any;
  /** FG color — owned by DockedController via fixedFgColor. Do NOT shadow with local state. */
  fgColor: string;
  /** BG color — owned by DockedController via fixedBgColor. Do NOT shadow with local state. */
  bgColor: string;
  /** The active pattern selected by the user. Owned by DockedController. */
  selectedPatternId?: number;
  /** Callback fired when an effect is successfully dispatched. */
  onStateChange?: (id: number) => void;
}

export const UnifiedPatternPicker: React.FC<UnifiedPatternPickerProps> = ({
  writeToDevice, points = 16, segments = 1, speed, brightness = 100, direction = 1,
  hwSettings, onStateChange, fgColor, bgColor,
  selectedPatternId = 1,
}) => {
  const { Colors } = useTheme();
  const devicePoints = hwSettings?.ledPoints || points || 16;

  // Track volatile callbacks in refs to avoid breaking React.memo downstream
  const onStateChangeRef = useRef(onStateChange);
  const writeToDeviceRef = useRef(writeToDevice);
  useEffect(() => {
    onStateChangeRef.current = onStateChange;
    writeToDeviceRef.current = writeToDevice;
  }, [onStateChange, writeToDevice]);

  // Dispatch logic for PATTERNS tab — uses PatternEngine → 0x59 pipeline.
  // buildPatternPayload() generates our math-synthesized pixel arrays and
  // wraps them in the correct 0x59 opcode with transition type (FREEZE/CASCADE).
  // This is the ONLY correct dispatch path. Do NOT use 0x51 setCustomModeCompact
  // here — that sends firmware symphony effect IDs, not our pixel math.
  const dispatchEffect = useCallback((effectId: number, fg: string, bg: string, spd: number, dir: number, brt: number) => {
    if (!writeToDeviceRef.current) return;
    const fgRgb = hexToRgb(fg);
    const bgRgb = hexToRgb(bg);
    const payload = buildPatternPayload(
      effectId, fgRgb, bgRgb, devicePoints,
      Math.max(1, Math.min(100, Math.round(spd))), dir, brt
    );
    if (payload) writeToDeviceRef.current(payload);
    onStateChangeRef.current?.(effectId);
  }, [devicePoints]);

  const handleSelectPattern = useCallback((effectId: number) => {
    dispatchEffect(effectId, fgColor, bgColor, speed, direction, brightness);
  }, [dispatchEffect, fgColor, bgColor, speed, direction, brightness]);

  // Sync speed/color changes to PATTERNS tab hardware.
  // NOTE: dispatchEffect is intentionally omitted from deps — it is stable via useCallback
  // ([writeToDevice, onStateChange]). Including it causes an infinite loop because
  // DockedController passes onStateChange as an inline arrow (new ref every render),
  // which recreates dispatchEffect, which re-fires this effect, which calls writeToDevice,
  // which calls setLastSentPayload in DockedController, which re-renders, which ... loops.
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(() => {
    if (selectedPatternId) {
      dispatchEffect(selectedPatternId, fgColor, bgColor, speed, direction, brightness);
    }
  }, [speed, brightness, direction, selectedPatternId, fgColor, bgColor]);

  return (
    <View style={{ flex: 1 }}>
      <PatternPickerTab
        selectedEffectId={selectedPatternId}
        fgColor={fgColor}
        bgColor={bgColor}
        speed={speed}
        brightness={brightness ?? 100}
        direction={direction}
        points={devicePoints}
        onSelect={handleSelectPattern}
        Colors={Colors}
      />
    </View>
  );
};
