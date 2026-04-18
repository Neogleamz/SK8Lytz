/**
 * ProgramsPanel.tsx — RBM Programs mode panel.
 *
 * Renders when activeMode === 'PROGRAMS'. Wraps VerticalPatternDrum
 * with the pattern dispatch logic (RBM write + emergency pattern).
 *
 * Extracted from DockedController.tsx (Phase 4 — decompose-docked-controller-jsx).
 */
import React from 'react';
import { View } from 'react-native';
import { getRbmPatternName } from '../../constants/RbmPatterns';
import { ZenggeProtocol } from '../../protocols/ZenggeProtocol';
import { Spacing } from '../../theme/theme';
import VerticalPatternDrum from '../VerticalPatternDrum';

interface ProgramsPanelProps {
  selectedPatternId: number;
  setSelectedPatternId: (id: number) => void;
  speed: number;
  brightness: number;
  writeToDevice?: (payload: number[]) => Promise<void | boolean | 'partial'>;
  applyEmergencyPattern: (speed: number, brightness: number) => void;
}

const ProgramsPanel = React.memo(({
  selectedPatternId,
  setSelectedPatternId,
  speed,
  brightness,
  writeToDevice,
  applyEmergencyPattern,
}: ProgramsPanelProps) => {
  return (
    <View style={{ flex: 1, paddingHorizontal: Spacing.xs, paddingTop: Spacing.xs }}>
      <VerticalPatternDrum
        value={selectedPatternId}
        onValueChange={(id: number) => {
          setSelectedPatternId(id);
          if (writeToDevice) {
            if (id === 100) {
              applyEmergencyPattern(speed, brightness);
            } else {
              writeToDevice(ZenggeProtocol.setCustomRbm(id, speed, brightness));
            }
          }
        }}
        itemLabel={(id) => getRbmPatternName(id)}
      />
    </View>
  );
});

export default ProgramsPanel;
