/**
 * useAppMicrophone.ts — App-side audio recording bridge.
 *
 * Manages the expo-av Audio.Recording lifecycle for APP MIC mode.
 * Streams normalized magnitude (0–1) and dispatches 0x74 music magnitude
 * commands to hardware.
 *
 * Extracted from DockedController.tsx to isolate platform-specific
 * audio recording from LED control state.
 */
import * as FileSystem from 'expo-file-system';
import { AppLogger } from '../services/AppLogger';
import { useAudioRecorder, setAudioModeAsync, RecordingPresets } from 'expo-audio';
import { useEffect, useRef, useState, useMemo } from 'react';
import { Platform } from 'react-native';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import type { ModeType } from '../types/dashboard.types';

interface UseAppMicrophoneParams {
  activeMode: ModeType;
  micSource: 'APP' | 'DEVICE';
  isPoweredOn: boolean;
  writeToDevice?: (payload: number[]) => Promise<void | boolean | 'partial'>;
}

/**
 * Manages the device microphone recording lifecycle and magnitude streaming.
 * Returns the current normalized audio magnitude (0–1) for visualizers.
 */
import { checkPermission, openGlobalPermissionsModal } from '../services/PermissionService';

export function useAppMicrophone({
  activeMode,
  micSource,
  isPoweredOn,
  writeToDevice,
}: UseAppMicrophoneParams) {
  const recorderConfig = useMemo(() => ({
    ...RecordingPresets.LOW_QUALITY,
    isMeteringEnabled: true,
  }), []);
  const recorder = useAudioRecorder(recorderConfig);
  const [audioMagnitude, setAudioMagnitude] = useState<number>(0);
  const [hasMicPermission, setHasMicPermission] = useState<boolean>(true);
  const magnitudeInterval = useRef<NodeJS.Timeout | null>(null);
  const isCapturingRef = useRef(false);
  const isMountedRef = useRef(true);

  useEffect(() => {
    isMountedRef.current = true;
    return () => {
      isMountedRef.current = false;
    };
  }, []);

  const writeToDeviceRef = useRef(writeToDevice);
  writeToDeviceRef.current = writeToDevice;

  const startRecording = async () => {
    if (isCapturingRef.current) return;
    isCapturingRef.current = true;
    try {
      const isGranted = await checkPermission('MIC');
      if (!isMountedRef.current) return;
      setHasMicPermission(isGranted);
      if (!isGranted) {
        await openGlobalPermissionsModal();
        const reG = await checkPermission('MIC');
        if (!isMountedRef.current) return;
        setHasMicPermission(reG);
        if (!reG) {
          isCapturingRef.current = false;
          return;
        }
      }

      await setAudioModeAsync({
        allowsRecording: true,
        playsInSilentMode: true,
      });

      if (!isMountedRef.current) return;
      await recorder.prepareToRecordAsync();
      recorder.record();

      // Start magnitude stream — firehose to hardware at 20Hz.
      // BIBLE §11: ZENGGE app streams 0x74 continuously. Hardware switches from
      // built-in mic to app mic ONLY when it receives a steady 0x74 stream.
      // Too-slow streaming (10Hz with delta gate) looks like silence → hardware falls back to device mic.
      const prevMagRef = { current: -1 };
      magnitudeInterval.current = setInterval(() => {
        const stats = recorder.getStatus();
        if (stats.canRecord && stats.isRecording) {
          const metering = stats.metering ?? -160;
          // Map -100..0 dBFS → 0..1 (covers full Android expo-audio metering range)
          const raw = Math.max(0, Math.min(1, (metering + 100) / 100));

          // Exponential moving average — smooths noisy mic readings so the EQ
          // glides instead of jumping. alpha=0.4: snappy but not jittery.
          const smoothed = prevMagRef.current < 0
            ? raw
            : prevMagRef.current + 0.4 * (raw - prevMagRef.current);
          prevMagRef.current = smoothed;

          if (isMountedRef.current) {
            setAudioMagnitude(smoothed);
          }

          // Send to hardware — 0x74 expects 0-150 (ZENGGE §11 decompiler limit)
          const deviceMag = Math.floor(smoothed * 150);
          if (writeToDeviceRef.current) {
            const payload = ZenggeProtocol.sendMusicMagnitude(deviceMag);
            writeToDeviceRef.current(payload).catch(e => {
              AppLogger.error('[useAppMicrophone] Failed to stream magnitude', e instanceof Error ? e.message : String(e), { payload_size: payload.length, ssi: 0 });
            });
          }
        }
      }, 50); // 20Hz — hardware needs continuous stream to stay in app-mic mode
    } catch (err: unknown) {
      if (isMountedRef.current) {
        AppLogger.error('Failed to start recording', err instanceof Error ? err.message : String(err), { payload_size: 0, ssi: 0 });
        isCapturingRef.current = false;
      }
    }
  };

  const stopRecording = async () => {
    if (!isCapturingRef.current) return;
    if (magnitudeInterval.current) {
      clearInterval(magnitudeInterval.current);
      magnitudeInterval.current = null;
    }
    try {
      if (recorder.isRecording) {
        await recorder.stop();
      }
    } catch (_e: unknown) {
      AppLogger.debug('[useAppMicrophone] recorder.stop() failed — already stopped or recorder torn down', {
        error: _e instanceof Error ? _e.message : String(_e),
      });
    } finally {
      isCapturingRef.current = false;
    }
  };

  // Lifecycle: auto-start/stop recording based on Music mode + APP mic
  useEffect(() => {
    if (Platform.OS === 'web') return; // expo-av Audio Recording not supported on web
    const isMusicActive = activeMode === 'MUSIC';
    if (isMusicActive && micSource === 'APP' && isPoweredOn) {
      startRecording();
    } else {
      stopRecording();
    }
    return () => {
      stopRecording();
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activeMode, micSource, isPoweredOn]);

  const requestMicPermission = async () => {
    await openGlobalPermissionsModal();
    const reG = await checkPermission('MIC');
    if (!isMountedRef.current) return;
    setHasMicPermission(reG);
    if (reG && activeMode === 'MUSIC' && micSource === 'APP' && isPoweredOn) {
      startRecording();
    }
  };

  return { audioMagnitude, hasMicPermission, requestMicPermission, recording: recorder.isRecording ? recorder : null };
}
