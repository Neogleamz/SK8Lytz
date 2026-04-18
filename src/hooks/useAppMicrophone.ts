/**
 * useAppMicrophone.ts — App-side audio recording bridge.
 *
 * Manages the expo-av Audio.Recording lifecycle for APP MIC mode.
 * Streams normalized magnitude (0–1) and dispatches 0x74 Symphony
 * commands to hardware.
 *
 * Extracted from DockedController.tsx to isolate platform-specific
 * audio recording from LED control state.
 */
import * as FileSystem from 'expo-file-system';
import { AppLogger } from '../services/AppLogger';
import { Audio } from 'expo-av';
import { useEffect, useRef, useState } from 'react';
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
  const [recording, setRecording] = useState<Audio.Recording | null>(null);
  const [audioMagnitude, setAudioMagnitude] = useState<number>(0);
  const magnitudeInterval = useRef<NodeJS.Timeout | null>(null);

  const startRecording = async () => {
    try {
      const isGranted = await checkPermission('MIC');
      if (!isGranted) {
        await openGlobalPermissionsModal();
        const reG = await checkPermission('MIC');
        if (!reG) return;
      }

      await Audio.setAudioModeAsync({
        allowsRecordingIOS: true,
        playsInSilentModeIOS: true,
      });

      const { recording: newRecording } = await Audio.Recording.createAsync(
        {
          ...Audio.RecordingOptionsPresets.LOW_QUALITY,
          isMeteringEnabled: true, // REQUIRED: enables stats.metering
          android: {
            ...Audio.RecordingOptionsPresets.LOW_QUALITY.android,
            extension: '.m4a',
            outputFormat: Audio.AndroidOutputFormat.MPEG_4,
            audioEncoder: Audio.AndroidAudioEncoder.AAC,
            sampleRate: 44100,
            numberOfChannels: 1,
            bitRate: 128000,
          },
          ios: {
            ...Audio.RecordingOptionsPresets.LOW_QUALITY.ios,
            extension: '.m4a',
            outputFormat: Audio.IOSOutputFormat.MPEG4AAC,
            audioQuality: Audio.IOSAudioQuality.MIN,
            sampleRate: 44100,
            numberOfChannels: 1,
            bitRate: 128000,
            linearPCMBitDepth: 16,
            linearPCMIsBigEndian: false,
            linearPCMIsFloat: false,
          },
        },
        null, // initialStatus
        50   // progressUpdateIntervalMillis
      );

      await newRecording.setProgressUpdateInterval(50);
      setRecording(newRecording);

      // Start magnitude stream
      magnitudeInterval.current = setInterval(async () => {
        if (!writeToDevice) return;
        const stats = await newRecording.getStatusAsync();
        if (stats.canRecord && stats.isRecording) {
          const metering = stats.metering ?? -160;
          // Map -60...0 to 0...1 for usable visualization
          const normalized = Math.max(0, Math.min(1, (metering + 60) / 60));
          setAudioMagnitude(normalized);

          // Send to physical device (Symphony 0x74 command expects 0-255)
          const deviceMag = Math.floor(normalized * 255);
          writeToDevice(ZenggeProtocol.sendMusicMagnitude(deviceMag));
        }
      }, 50);
    } catch (err) {
      AppLogger.error('Failed to start recording', err);
    }
  };

  const stopRecording = async () => {
    if (magnitudeInterval.current) {
      clearInterval(magnitudeInterval.current);
      magnitudeInterval.current = null;
    }
    if (recording) {
      try {
        await recording.stopAndUnloadAsync();
      } catch (_e) { /* swallow — recording may already be stopped */ }
      setRecording(null);
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

  return { audioMagnitude, recording };
}
