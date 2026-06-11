/**
 * useProtocolDispatch.ts — Protocol-Aware Command Dispatch
 *
 * This hook acts as the translation layer between UI intents (e.g. "turn red")
 * and hardware-specific byte arrays. It supports mixed-protocol groups by
 * iterating over connected devices and resolving the correct HAL adapter for each.
 */
import { useCallback, useContext } from 'react';
import { BLEContext } from '../context/BLEContext';
import type { CustomModeStep, MusicConfig, RGB, ProtocolResult } from '../protocols/IControllerProtocol';
import { AppLogger } from '../services/AppLogger';

export function useProtocolDispatch() {
  const context = useContext(BLEContext);
  if (!context) {
    throw new Error('useProtocolDispatch must be used within a BLEProvider');
  }
  const { connectedDevices, getAdapterForDevice, executeProtocolResults, writeChunked } = context;

  const _dispatchToDevices = useCallback(
    (
      builder: (adapter: ReturnType<typeof getAdapterForDevice>) => ProtocolResult,
      targetDeviceId?: string,
      opts?: { lowPriority?: boolean }
    ) => {
      const targets = targetDeviceId 
        ? connectedDevices.filter(d => d.id === targetDeviceId)
        : connectedDevices;

      if (targets.length === 0) {
        AppLogger.error('[useProtocolDispatch] _dispatchToDevices targets empty', new Error('connectedDevices or targets empty'), { payload_size: 0, ssi: 0 });
        return Promise.resolve(true);
      }

      const payloads = targets.map(device => {
        const adapter = getAdapterForDevice(device.id);
        const result = builder(adapter);
        return { targetDeviceId: device.id, result };
      });

      return executeProtocolResults(payloads, opts);
    },
    [connectedDevices, getAdapterForDevice, executeProtocolResults]
  );

  const setPower = useCallback((isOn: boolean, targetDeviceId?: string, opts?: { lowPriority?: boolean }) => {
    return _dispatchToDevices(adapter => isOn ? adapter.buildPowerOn() : adapter.buildPowerOff(), targetDeviceId, opts);
  }, [_dispatchToDevices]);

  const setSolidColor = useCallback((r: number, g: number, b: number, targetDeviceId?: string, opts?: { lowPriority?: boolean }) => {
    return _dispatchToDevices(adapter => adapter.buildSolidColor(r, g, b), targetDeviceId, opts);
  }, [_dispatchToDevices]);

  const setMultiColor = useCallback((colors: RGB[], ledPoints: number, speed: number, direction: number, transitionType?: number, targetDeviceId?: string, opts?: { lowPriority?: boolean }) => {
    return _dispatchToDevices(adapter => adapter.buildMultiColor(colors, ledPoints, speed, direction, transitionType), targetDeviceId, opts);
  }, [_dispatchToDevices]);

  const setEffect = useCallback((effectId: number, speed: number, brightness: number, targetDeviceId?: string, opts?: { lowPriority?: boolean }) => {
    return _dispatchToDevices(adapter => adapter.buildEffect(effectId, speed, brightness), targetDeviceId, opts);
  }, [_dispatchToDevices]);

  const setCustomMode = useCallback((steps: CustomModeStep[], targetDeviceId?: string, opts?: { lowPriority?: boolean }) => {
    return _dispatchToDevices(adapter => adapter.buildCustomMode(steps), targetDeviceId, opts);
  }, [_dispatchToDevices]);

  const setCustomModeExtended = useCallback((steps: CustomModeStep[], direction?: number, targetDeviceId?: string, opts?: { lowPriority?: boolean }) => {
    return _dispatchToDevices(adapter => adapter.buildCustomModeExtended(steps, direction), targetDeviceId, opts);
  }, [_dispatchToDevices]);

  const setCandleMode = useCallback((r: number, g: number, b: number, speed: number, brightness: number, amplitude: number, targetDeviceId?: string, opts?: { lowPriority?: boolean }) => {
    return _dispatchToDevices(adapter => adapter.buildCandleMode(r, g, b, speed, brightness, amplitude), targetDeviceId, opts);
  }, [_dispatchToDevices]);

  const streamPixelFrame = useCallback((pixels: RGB[], targetDeviceId?: string, opts?: { lowPriority?: boolean }) => {
    return _dispatchToDevices(adapter => adapter.buildStreamPixelFrame(pixels), targetDeviceId, opts);
  }, [_dispatchToDevices]);

  const setMusicConfig = useCallback((config: MusicConfig, targetDeviceId?: string, opts?: { lowPriority?: boolean }) => {
    return _dispatchToDevices(adapter => adapter.buildMusicConfig(config), targetDeviceId, opts);
  }, [_dispatchToDevices]);

  const setMusicMagnitude = useCallback((magnitude: number, targetDeviceId?: string, opts?: { lowPriority?: boolean }) => {
    return _dispatchToDevices(adapter => adapter.buildMusicMagnitude(magnitude), targetDeviceId, opts);
  }, [_dispatchToDevices]);

  const queryHardwareSettings = useCallback((hasMic?: boolean, targetDeviceId?: string, opts?: { lowPriority?: boolean }) => {
    return _dispatchToDevices(adapter => adapter.buildQuerySettings(hasMic), targetDeviceId, opts);
  }, [_dispatchToDevices]);

  const writeSettings = useCallback((points: number, segments: number, icType: number, sorting: number, targetDeviceId?: string, opts?: { lowPriority?: boolean }) => {
    return _dispatchToDevices(adapter => adapter.buildWriteSettings(points, segments, icType, sorting), targetDeviceId, opts);
  }, [_dispatchToDevices]);

  const writeSettingsByName = useCallback((points: number, segments: number, stripTypeName: string, sortingName: string, targetDeviceId?: string, opts?: { lowPriority?: boolean }) => {
    return _dispatchToDevices(adapter => adapter.buildWriteSettingsByName(points, segments, stripTypeName, sortingName), targetDeviceId, opts);
  }, [_dispatchToDevices]);

  const queryRfRemoteState = useCallback((targetDeviceId?: string, opts?: { lowPriority?: boolean }) => {
    return _dispatchToDevices(adapter => adapter.buildQueryRfRemoteState(), targetDeviceId, opts);
  }, [_dispatchToDevices]);

  const setRfRemoteState = useCallback((mode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED', autoSave: boolean, targetDeviceId?: string, opts?: { lowPriority?: boolean }) => {
    return _dispatchToDevices(adapter => adapter.buildSetRfRemoteState(mode, autoSave), targetDeviceId, opts);
  }, [_dispatchToDevices]);

  const clearRfRemotes = useCallback((mode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED', targetDeviceId?: string, opts?: { lowPriority?: boolean }) => {
    return _dispatchToDevices(adapter => adapter.buildClearRfRemotes(mode), targetDeviceId, opts);
  }, [_dispatchToDevices]);

  const executeRawPayload = useCallback((payload: number[], targetDeviceId?: string, opts?: { lowPriority?: boolean }) => {
    // Route 0x51 extended payloads (323B) through writeChunked (0x40 framing).
    // These are too large for a single BLE write and will be silently dropped
    // by the BLE stack if sent through executeProtocolResults as a single packet.
    const cmdByte = payload[0];
    if (cmdByte === 0x51 && payload.length > 200) {
      return writeChunked(payload, targetDeviceId ?? undefined).then(() => true as const);
    }

    // For all other raw payloads, wrap in a ProtocolResult and dispatch normally.
    const result: ProtocolResult = {
      packets: [payload],
      interPacketDelayMs: 0,
      isRateLimited: cmdByte === 0x59 || cmdByte === 0x51 || cmdByte === 0x40
    };

    const targets = targetDeviceId
      ? connectedDevices.filter(d => d.id === targetDeviceId)
      : connectedDevices;

    if (targets.length === 0) {
      AppLogger.error('[useProtocolDispatch] executeRawPayload targets empty', new Error('connectedDevices or targets empty'), { payload_size: 0, ssi: 0 });
      return Promise.resolve(true);
    }

    const payloads = targets.map(device => {
      return { targetDeviceId: device.id, result };
    });

    return executeProtocolResults(payloads, opts);
  }, [connectedDevices, executeProtocolResults, writeChunked]);

  return {
    setPower,
    setSolidColor,
    setMultiColor,
    setEffect,
    setCustomMode,
    setCustomModeExtended,
    setCandleMode,
    streamPixelFrame,
    setMusicConfig,
    setMusicMagnitude,
    queryHardwareSettings,
    writeSettings,
    writeSettingsByName,
    queryRfRemoteState,
    setRfRemoteState,
    clearRfRemotes,
    executeRawPayload,
  };
}
