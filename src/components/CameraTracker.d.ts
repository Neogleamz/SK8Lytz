import { FC } from 'react';
import { RGB } from '../utils/kMeansPalette';

export interface CameraTrackerProps {
  onColorDetected: (hex: string) => void;
  onVibePaletteDetected?: (colors: RGB[]) => void;
  subMode: 'SNIPER' | 'VIBE';
  isActive: boolean;
}

const CameraTracker: FC<CameraTrackerProps>;
export default CameraTracker;
