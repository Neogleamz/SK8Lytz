import { fromCallback } from 'xstate';

export interface AutoPauseServiceInput {
  autoPauseEnabled: boolean;
  gpsSpeedRef: { current: number };
  sessionPhase: 'ACTIVE' | 'PAUSED';
}

export const autoPauseService = fromCallback<any, AutoPauseServiceInput>(({ sendBack, input }) => {
  let lowSpeedTicks = 0;

  const timer = setInterval(() => {
    const speed = input.gpsSpeedRef.current;

    if (input.sessionPhase === 'ACTIVE') {
      if (speed < 0.2) {
        lowSpeedTicks++;
        if (lowSpeedTicks >= 20 && input.autoPauseEnabled) {
          sendBack({ type: 'AUTO_PAUSE' });
          lowSpeedTicks = 0;
        }
      } else {
        lowSpeedTicks = 0;
      }
    } else if (input.sessionPhase === 'PAUSED') {
      if (speed >= 0.2) {
        sendBack({ type: 'AUTO_RESUME' });
      }
    }
  }, 500);

  return () => {
    clearInterval(timer);
  };
});
