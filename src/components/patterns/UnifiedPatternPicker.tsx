import React, { useCallback, useEffect, useRef } from 'react';
import { StyleSheet, View } from 'react-native';
import { buildPatternPayload } from '../../protocols/PatternEngine';
import { useTheme } from '../../context/ThemeContext';
import { hexToRgb } from '../../utils/ColorUtils';
import { PatternPickerTab } from './PatternPickerTab';
import { IHardwareSettings } from '../../types/dashboard.types';
import { AppLogger } from '../../services/appLogger';

interface UnifiedPatternPickerProps {
  writeToDevice?: (payload: number[]) => Promise<void | boolean | 'partial'>;
  points?: number;
  segments?: number;
  speed: number;
  brightness?: number;
  direction?: number;
  hwSettings?: IHardwareSettings;
  /** FG color — owned by DockedController via fixedFgColor. Do NOT shadow with local state. */
  fgColor: string;
  /** BG color — owned by DockedController via fixedBgColor. Do NOT shadow with local state. */
  bgColor: string;
  /** The active pattern selected by the user. Owned by DockedController. */
  selectedPatternId?: number;
  /** Callback fired when an effect is successfully dispatched. */
  onStateChange?: (id: number) => void;
  applyFixedPattern?: (patternId: number, fg: string, bg: string, spd?: number, brt?: number, dir?: number) => void;
}

export const UnifiedPatternPicker: React.FC<UnifiedPatternPickerProps> = ({
  writeToDevice, points = 16, segments = 1, speed, brightness = 100, direction = 1,
  hwSettings, onStateChange, fgColor, bgColor,
  selectedPatternId = 1, applyFixedPattern
}) => {
  const { Colors } = useTheme();
  const devicePoints = hwSettings?.ledPoints || points || 16;

  // Track volatile callbacks in refs to avoid breaking React.memo downstream
  const onStateChangeRef = useRef(onStateChange);
  const writeToDeviceRef = useRef(writeToDevice);
  const applyFixedPatternRef = useRef(applyFixedPattern);
  useEffect(() => {
    onStateChangeRef.current = onStateChange;
    writeToDeviceRef.current = writeToDevice;
    applyFixedPatternRef.current = applyFixedPattern;
  }, [onStateChange, writeToDevice, applyFixedPattern]);

  // Dispatch logic for PATTERNS tab — uses PatternEngine → 0x59 pipeline.
  // buildPatternPayload() generates our math-synthesized pixel arrays and
  // wraps them in the correct 0x59 opcode with transition type (FREEZE/CASCADE).
  // This is the ONLY correct dispatch path. Do NOT use 0x51 setCustomModeCompact
  // here — that sends firmware symphony effect IDs, not our pixel math.
  const dispatchEffect = useCallback((effectId: number, fg: string, bg: string, spd: number, dir: number, brt: number) => {
    if (applyFixedPatternRef.current) {
      applyFixedPatternRef.current(effectId, fg, bg, spd, brt, dir);
    } else {
      if (!writeToDeviceRef.current) return;
      const fgRgb = hexToRgb(fg);
      const bgRgb = hexToRgb(bg);
      const payload = buildPatternPayload(
        effectId, fgRgb, bgRgb, devicePoints,
        Math.max(1, Math.min(100, Math.round(spd))), dir, brt
      );
      if (payload) {
        (async () => {
          try {
            if (writeToDeviceRef.current) await writeToDeviceRef.current(payload);
          } catch (err) {
            AppLogger.error('UnifiedPatternPicker writeToDevice failed', err instanceof Error ? err : new Error(String(err)), { payload_size: payload ? payload.length : 0, ssi: 0 });
          }
        })();
      }
    }
    onStateChangeRef.current?.(effectId);
  }, [devicePoints]);

  const handleSelectPattern = useCallback((effectId: number) => {
    dispatchEffect(effectId, fgColor, bgColor, speed, direction, brightness);
  }, [dispatchEffect, fgColor, bgColor, speed, direction, brightness]);

  // Reactive useEffect removed to prevent dispatch race conditions and clobbering.
  // Slider and state changes are now explicitly handled by UniversalSlidersFooter via applyFixedPattern.

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
