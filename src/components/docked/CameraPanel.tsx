/**
 * CameraPanel.tsx — Camera color-pick mode panel.
 *
 * Renders when activeMode === 'CAMERA'. Wraps CameraTracker
 * and exposes the detected color via `onColorDetected`.
 *
 * Extracted from DockedController.tsx (Phase 4 — decompose-docked-controller-jsx).
 */
import React from 'react';
import { View } from 'react-native';
import CameraTracker from '../CameraTracker';

interface CameraPanelProps {
  onColorDetected: (hex: string) => void;
}

const CameraPanel = React.memo(({ onColorDetected }: CameraPanelProps) => {
  return (
    <View style={{ flex: 1 }}>
      <CameraTracker
        isActive
        onColorDetected={onColorDetected}
      />
    </View>
  );
});

export default CameraPanel;
