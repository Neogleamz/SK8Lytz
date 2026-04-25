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
  writeToDevice?: (payload: number[]) => Promise<void | boolean | 'partial'>;
  activeMode: ModeType;
  micSource: 'APP' | 'DEVICE';
  isPoweredOn: boolean;
}

/**
 * Manages the device microphone recording lifecycle and magnitude streaming.
 * Returns the current normalized audio magnitude (0–1) for visualizers.
 */
import { checkPermission, openGlobalPermissionsModal } from '../services/PermissionService';

export function useAppMicrophone({
  writeToDevice,
  activeMode,
  micSource,
  isPoweredOn,
}: UseAppMicrophoneParams) {
  const recorderConfig = useMemo(() => ({
    ...RecordingPresets.LOW_QUALITY,
    isMeteringEnabled: true,
  }), []);
  const recorder = useAudioRecorder(recorderConfig);
  const [audioMagnitude, setAudioMagnitude] = useState<number>(0);
  const [hasMicPermission, setHasMicPermission] = useState<boolean>(true);
  const magnitudeInterval = useRef<NodeJS.Timeout | null>(null);

  const startRecording = async () => {
    try {
      const isGranted = await checkPermission('MIC');
      setHasMicPermission(isGranted);
      if (!isGranted) {
        await openGlobalPermissionsModal();
        const reG = await checkPermission('MIC');
        setHasMicPermission(reG);
        if (!reG) return;
      }

      await setAudioModeAsync({
        allowsRecording: true,
        playsInSilentMode: true,
      });

      await recorder.prepareToRecordAsync();
      recorder.record();

      // Start magnitude stream
      // prevMagRef: stores last-written normalized magnitude to gate redundant BLE writes.
      // Interval at 10Hz (100ms) — hardware smooths between values; 20Hz is imperceptible.
      const prevMagRef = { current: -1 };
      magnitudeInterval.current = setInterval(() => {
        if (!writeToDevice) return;
        const stats = recorder.getStatus();
        if (stats.canRecord && stats.isRecording) {
          const metering = stats.metering ?? -160;
          // Map -60...0 to 0...1 for usable visualization
          const normalized = Math.max(0, Math.min(1, (metering + 60) / 60));
          // Delta guard: skip BLE write if magnitude hasn't changed by >5%
          if (Math.abs(normalized - prevMagRef.current) < 0.05) return;
          prevMagRef.current = normalized;
          setAudioMagnitude(normalized);

          // Send to physical device (0x74 music magnitude command expects 0-255)
          const deviceMag = Math.floor(normalized * 255);
          writeToDevice(ZenggeProtocol.sendMusicMagnitude(deviceMag));
        }
      }, 100); // 10Hz — saves ~50% BLE writes vs 20Hz with no perceptible visual difference
    } catch (err) {
      AppLogger.error('Failed to start recording', err);
    }
  };

  const stopRecording = async () => {
    if (magnitudeInterval.current) {
      clearInterval(magnitudeInterval.current);
      magnitudeInterval.current = null;
    }
    try {
      if (recorder.isRecording) {
        await recorder.stop();
      }
    } catch (_e) { /* swallow */ }
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
    setHasMicPermission(reG);
    if (reG && activeMode === 'MUSIC' && micSource === 'APP' && isPoweredOn) {
      startRecording();
    }
  };

  return { audioMagnitude, hasMicPermission, requestMicPermission, recording: recorder.isRecording ? recorder : null };
}
