import { useEffect, useRef } from 'react';

export function useCrewLeaderBroadcast(
  crewRole: 'leader' | 'member' | null | undefined,
  onCrewSceneChange: ((scene: Record<string, unknown>) => void) | undefined,
  captureEntireState: () => Record<string, unknown>,
  deps: unknown[]
) {
  const crewBroadcastTimer = useRef<ReturnType<typeof setTimeout> | null>(null);
  
  useEffect(() => {
    if (crewRole !== 'leader' || !onCrewSceneChange) return;
    if (crewBroadcastTimer.current) clearTimeout(crewBroadcastTimer.current);
    crewBroadcastTimer.current = setTimeout(() => {
      onCrewSceneChange(captureEntireState());
    }, 200);
    return () => {
      if (crewBroadcastTimer.current) clearTimeout(crewBroadcastTimer.current);
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [crewRole, onCrewSceneChange, captureEntireState, ...deps]);
}
