import { FC } from 'react';

export interface CameraTrackerProps {
  onColorDetected: (hex: string) => void;
  isActive: boolean;
}

const CameraTracker: FC<CameraTrackerProps>;
export default CameraTracker;
