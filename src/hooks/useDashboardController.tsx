import React, { useState, useMemo } from 'react';
import { Animated } from 'react-native';
import CrewMemberDashboard from '../components/CrewMemberDashboard';
import { BLEErrorBoundary } from '../components/shared/BLEErrorBoundary';
import DockedController, { DockedControllerHandle } from '../components/DockedController';
import { getLocalProfileByPoints } from '../constants/ProductCatalog';
import { crewService } from '../services/CrewService';
import { normalizeMac } from '../hooks/useDeviceStateLedger';
import { useDashboardDeviceConfig } from './useDashboardDeviceConfig';
import type { DisplayDevice, IDeviceState, GroupPatternSnapshot, BleConnectionState, DevicePatternState, ModeType } from '../types/dashboard.types';
import type { CrewSession } from '../services/CrewService';
import { useAuth } from '../context/AuthContext';
import { PanResponderInstance } from 'react-native';
import { AppLogger } from '../services/AppLogger';
import { RegisteredDevice } from './useRegistration';
import type { SessionPhase } from '../services/session/SessionMachine.types';

export interface UseDashboardControllerProps {
  isOfflineMode: boolean;
  isActuallyConnected: boolean;
  isTestModeActive: boolean;
  crewSession: CrewSession | null;
  crewRole: 'leader' | 'member' | null;
  lastLeaderScene: Record<string, unknown> | null;
  setLastLeaderScene: (s: Record<string, unknown> | null) => void;
  setCrewModeSummary: (s: string | undefined) => void;
  dockedControllerRef: React.RefObject<DockedControllerHandle | null>;
  displayConnectedDevices: DisplayDevice[];
  deviceConfigs: Record<string, Record<string, unknown>>;
  isGrouped: boolean;
  powerStates: Record<string, boolean>;
  handlePowerToggle: (macs: string[]) => void;
  handleDisconnect: () => void;
  appSettings: Record<string, string | boolean>;
  bleState: BleConnectionState | string;
  gpsSpeed: number;
  peakGForce: number;
  sessionDistanceMiles: number;
  sessionDurationSec: number;
  sessionAvgSpeed: number;
  sessionPeakSpeed: number;
  sessionPhase: SessionPhase;
  sessionActive: boolean;
  startSession: () => void;
  stopSessionRecording: () => void;
  customGroups: { id: string; name: string; deviceIds: string[] }[];
  lastGroupPatterns: Record<string, GroupPatternSnapshot>;
  setLastGroupPattern: (groupId: string, snapshot: GroupPatternSnapshot) => void;
  ledgerSave: (id: string, payload: DevicePatternState) => void;
  writeToDevice: (payload: number[], targetDeviceId?: string, opts?: { lowPriority?: boolean }) => Promise<boolean | 'partial'>;
  edgePanResponder: PanResponderInstance;
  
  // Dependencies for useDashboardDeviceConfig
  allDevices: DisplayDevice[];
  setAllDevices: React.Dispatch<React.SetStateAction<DisplayDevice[]>>;
  allDevicesRef: React.MutableRefObject<DisplayDevice[]>;
  registeredDevices: RegisteredDevice[];
  saveRegisteredDevice: (device: Partial<RegisteredDevice> & { device_mac: string }) => Promise<boolean>;
  setUpdateTrigger: React.Dispatch<React.SetStateAction<number>>;
}

export function useDashboardController({
  isOfflineMode,
  isActuallyConnected,
  isTestModeActive,
  crewSession,
  crewRole,
  lastLeaderScene,
  setLastLeaderScene,
  setCrewModeSummary,
  dockedControllerRef,
  displayConnectedDevices,
  deviceConfigs,
  isGrouped,
  powerStates,
  handlePowerToggle,
  handleDisconnect,
  appSettings,
  bleState,
  gpsSpeed,
  peakGForce,
  sessionDistanceMiles,
  sessionDurationSec,
  sessionAvgSpeed,
  sessionPeakSpeed,
  sessionPhase,
  sessionActive,
  startSession,
  stopSessionRecording,
  customGroups,
  lastGroupPatterns,
  setLastGroupPattern,
  ledgerSave,
  writeToDevice,
  edgePanResponder,
  
  allDevices,
  setAllDevices,
  allDevicesRef,
  registeredDevices,
  saveRegisteredDevice,
  setUpdateTrigger
}: UseDashboardControllerProps) {
  const { session } = useAuth();
  const userId = session?.user?.id;

  // ── Settings Modal State ──
  const [isSettingsVisible, setIsSettingsVisible] = useState(false);
  const [selectedDeviceForSettingsId, setSelectedDeviceForSettingsId] = useState<string | null>(null);

  const selectedDeviceForSettings = useMemo(() => {
    if (!selectedDeviceForSettingsId) return null;
    const device = allDevices.find(d => d.id === selectedDeviceForSettingsId);
    if (!device) return null;
    // Merge EEPROM config data (deviceConfigs) into the BLE device object.
    // deviceConfigs is the SSOT for all polled hardware fields — points, segments,
    // sorting, stripType, rfMode, rfRemotes. allDevices only carries BLE scan fields.
    // Spread order: cfg first, device second so BLE identity fields (id, name) always win.
    const cfg = deviceConfigs[selectedDeviceForSettingsId] || {};
    return { ...cfg, ...device };
  }, [selectedDeviceForSettingsId, allDevices, deviceConfigs]);

  const openSettings = React.useCallback((device: { device_mac?: string; id?: string }) => {
    setSelectedDeviceForSettingsId(device.device_mac || device.id || null);
    setIsSettingsVisible(true);
  }, []);

  const { saveSettings } = useDashboardDeviceConfig({
    selectedDeviceForSettings,
    customGroups,
    registeredDevices,
    saveRegisteredDevice,
    setAllDevices,
    allDevicesRef,
    setUpdateTrigger,
    setIsSettingsVisible,
  });

  // ── Hardware Settings Derivation ──
  const activeHwSettings = useMemo(() => {
    const raw = displayConnectedDevices[0];
    const cached = raw?.id ? (deviceConfigs[raw.id] || {}) : {};
    const d = { ...raw, ...cached };
    const s = d?.sorting || d?.colorSortingName || 'GRB';
    const sortingIdx = s === 'RGB' ? 0 : s === 'RBG' ? 1 : s === 'GRB' ? 2 : s === 'GBR' ? 3 : s === 'BRG' ? 4 : s === 'BGR' ? 5 : 2;
    const colorSortingFinal = d?.detected ? (d.colorSorting ?? sortingIdx) : sortingIdx;
    return {
      ledPoints: d?.points || d?.ledPoints ||
        getLocalProfileByPoints(d?.points ?? 0).defaultLedPoints,
      segments:  d?.segments || 1,
      icType:    d?.icType || 1,
      icName:    d?.icName || d?.stripType || 'WS2812B',
      colorSorting: colorSortingFinal,
      colorSortingName: s,
      detected:  d?.detected || false,
    };
  }, [displayConnectedDevices, deviceConfigs]);

  // ── Controller Memoization ──
  const MemoizedSk8lytzController = useMemo(() => {
    if (!isActuallyConnected) return null;
    if (isTestModeActive) return null;

    if (crewSession && crewRole === 'member') {
      return (
        <CrewMemberDashboard
          session={crewSession}
          role={crewRole}
          currentScene={lastLeaderScene}
          onLeave={async () => {
            await crewService.leaveSession(userId).catch((err: unknown) => {
              AppLogger.error('Leave crew session failed', err instanceof Error ? err.message : String(err), { payload_size: 0, ssi: 0 });
            });
            setLastLeaderScene(null);
            setCrewModeSummary(undefined);
          }}
        />
      );
    }

    return (
      <Animated.View {...edgePanResponder.panHandlers} style={{ flex: 1, backgroundColor: 'transparent' }}>
          <BLEErrorBoundary componentName="DockedController">
          <DockedController
            isOfflineMode={isOfflineMode}
            ref={dockedControllerRef}
            hwSettings={activeHwSettings}
            lockedProduct={
              displayConnectedDevices[0]?.type ||
              getLocalProfileByPoints(displayConnectedDevices[0]?.points ?? 0).id
            }
            isPaired={isGrouped}
            points={activeHwSettings.ledPoints}
            devices={displayConnectedDevices as IDeviceState[]}
            onLongPressDevice={openSettings}
            writeToDevice={writeToDevice}
            isPoweredOn={displayConnectedDevices.some(d => powerStates[d.id] ?? true)}
            onPowerToggle={() => handlePowerToggle(displayConnectedDevices.map(d => d.id))}
            onDisconnect={handleDisconnect}
            crewRole={crewRole}
            appSettings={appSettings}
            onCrewSceneChange={(scene: Record<string, unknown>) => crewService.broadcastScene(scene, userId)}
            bleState={bleState as BleConnectionState}
            gpsSpeed={gpsSpeed}
            peakGForce={peakGForce}
            sessionDistanceMiles={sessionDistanceMiles}
            sessionDurationSec={sessionDurationSec}
            sessionAvgSpeed={sessionAvgSpeed}
            sessionPeakSpeed={sessionPeakSpeed}
            sessionPhase={sessionPhase}
            sessionActive={sessionActive}
            startSession={startSession}
            stopSessionRecording={stopSessionRecording}
            onPatternChanged={(patternName: string, snapshot: GroupPatternSnapshot, lastPayload?: number[]) => {
              const deviceMac = displayConnectedDevices[0]?.id?.toUpperCase();
              const matchingGroup = customGroups.find(g => g.deviceIds.includes(deviceMac ?? ''));
              const targetGroupId = matchingGroup?.id || deviceMac;
              if (targetGroupId) {
                const currentSnapshot = lastGroupPatterns[targetGroupId];
                if (!currentSnapshot || currentSnapshot.patternLabel !== patternName) {
                  setLastGroupPattern(targetGroupId, snapshot);
                }
                if (lastPayload && lastPayload.length > 0) {
                  displayConnectedDevices.forEach(d => {
                    ledgerSave(d.id, {
                      deviceMac: normalizeMac(d.id),
                      groupId: targetGroupId,
                      mode: snapshot.mode as ModeType,
                      patternLabel: patternName,
                      fgColor: snapshot.fgColor,
                      bgColor: snapshot.bgColor,
                      speed: 50,
                      brightness: 90,
                      rawPayload: lastPayload,
                      ts: Date.now(),
                    });
                  });
                }
              }
            }}
          />
          </BLEErrorBoundary>
      </Animated.View>
    );
  }, [
    isActuallyConnected, isGrouped, displayConnectedDevices, writeToDevice, powerStates, 
    isTestModeActive, activeHwSettings, crewRole, crewSession, lastLeaderScene, bleState, 
    ledgerSave, gpsSpeed, peakGForce, sessionDistanceMiles, sessionDurationSec, sessionAvgSpeed, sessionPeakSpeed, sessionPhase, sessionActive, startSession, stopSessionRecording,
    appSettings, customGroups, dockedControllerRef, edgePanResponder, handleDisconnect, 
    handlePowerToggle, lastGroupPatterns, setCrewModeSummary,
    setLastGroupPattern, setLastLeaderScene, userId, openSettings
  ]);

  return {
    MemoizedSk8lytzController,
    activeHwSettings,
    isSettingsVisible,
    setIsSettingsVisible,
    selectedDeviceForSettings,
    openSettings,
    saveSettings
  };
}
