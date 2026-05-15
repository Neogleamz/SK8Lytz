import React from 'react';
import { UnifiedPatternPicker } from '../patterns/UnifiedPatternPicker';
import type { DockedBus } from '../../types/dashboard.types';

interface ProEffectsPanelProps {
  bus: DockedBus;
}

/**
 * ProEffectsPanel — renders the MULTIMODE / Pro Effects pattern picker.
 *
 * Pure consumer of the DockedBus contract. Holds no local state and
 * performs no direct BLE access — all writes flow through `bus.writeToDevice`
 * which is mutex-guarded and debounced inside DockedController.
 *
 * React.memo ensures this panel only re-renders when its bus slice changes,
 * preventing cascade re-renders when STREET or MUSIC mode state updates.
 */
const ProEffectsPanel = React.memo(({ bus }: ProEffectsPanelProps) => {
  return (
    <UnifiedPatternPicker
      selectedPatternId={bus.fixedPatternId}
      speed={bus.speed}
      brightness={bus.brightness}
      hwSettings={bus.hwSettings}
      points={bus.points}
      fgColor={bus.fixedFgColor}
      bgColor={bus.fixedBgColor}
      direction={bus.fixedDirection}
      writeToDevice={bus.writeToDevice}
      applyFixedPattern={bus.applyFixedPattern}
      onStateChange={(id: number) => {
        bus.setFixedPatternId(id);
      }}
    />
  );
});

ProEffectsPanel.displayName = 'ProEffectsPanel';

export default ProEffectsPanel;
