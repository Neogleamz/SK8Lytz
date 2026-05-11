import React, { useState, useMemo } from 'react';
import { Animated } from 'react-native';
import CrewMemberDashboard from '../components/CrewMemberDashboard';
import { BLEErrorBoundary } from '../components/shared/BLEErrorBoundary';
import DockedController, { DockedControllerHandle } from '../components/DockedController';
import { getLocalProfileByPoints } from '../constants/ProductCatalog';
import { crewService } from '../services/CrewService';
import { normalizeMac } from '../hooks/useDeviceStateLedger';
import { useDashboardDeviceConfig } from './useDashboardDeviceConfig';
import type { DisplayDevice, IDeviceState, GroupPatternSnapshot, BleConnectionState } from '../types/dashboard.types';

export interface UseDashboardControllerProps {
  isActuallyConnected: boolean;
  isTestModeActive: boolean;
  crewSession: any;
  crewRole: any;
  lastLeaderScene: any;
  setCrewSession: (s: any) => void;
  setCrewRole: (r: any) => void;
  setLastLeaderScene: (s: any) => void;
  setCrewModeSummary: (s: any | undefined) => void;
  dockedControllerRef: React.RefObject<DockedControllerHandle | null>;
  displayConnectedDevices: DisplayDevice[];
  deviceConfigs: Record<string, any>;
  isGrouped: boolean;
  powerStates: Record<string, boolean>;
  handlePowerToggle: (macs: string[]) => void;
  handleDisconnect: () => void;
  appSettings: any;
  bleState: BleConnectionState | string;
  gpsSpeed: number;
  peakGForce: number;
  sessionDistanceMiles: number;
  sessionDurationSec: number;
  customGroups: any[];
  lastGroupPatterns: Record<string, any>;
  setLastGroupPattern: (groupId: string, snapshot: GroupPatternSnapshot) => void;
  ledgerSave: (id: string, payload: any) => void;
  writeToDevice: (payload: number[], targetDeviceId?: string, opts?: { lowPriority?: boolean }) => Promise<boolean | 'partial'>;
  edgePanResponder: any;
  
  // Dependencies for useDashboardDeviceConfig
  allDevices: any[];
  setAllDevices: any;
  allDevicesRef: any;
  registeredDevices: any[];
  saveRegisteredDevice: any;
  setUpdateTrigger: any;
}

export function useDashboardController({
  isActuallyConnected,
  isTestModeActive,
  crewSession,
  crewRole,
  lastLeaderScene,
  setCrewSession,
  setCrewRole,
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

  // ── Settings Modal State ──
  const [isSettingsVisible, setIsSettingsVisible] = useState(false);
  const [selectedDeviceForSettingsId, setSelectedDeviceForSettingsId] = useState<string | null>(null);

  const selectedDeviceForSettings = useMemo(() => {
    if (!selectedDeviceForSettingsId) return null;
    return allDevices.find(d => d.id === selectedDeviceForSettingsId) || null;
  }, [selectedDeviceForSettingsId, allDevices]);

  const openSettings = (device: any) => {
    setSelectedDeviceForSettingsId(device.id);
    setIsSettingsVisible(true);
  };

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
      ledPoints: d?.ledPoints || d?.points ||
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
            await crewService.leaveSession().catch(() => {});
            setCrewSession(null);
            setCrewRole(null);
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
            onCrewSceneChange={(scene: Record<string, any>) => crewService.broadcastScene(scene)}
            bleState={bleState as BleConnectionState}
            gpsSpeed={gpsSpeed}
            peakGForce={peakGForce}
            sessionDistanceMiles={sessionDistanceMiles}
            sessionDurationSec={sessionDurationSec}
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
                      mode: snapshot.mode as any,
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
    ledgerSave, gpsSpeed, peakGForce, sessionDistanceMiles, sessionDurationSec,
    appSettings, customGroups, dockedControllerRef, edgePanResponder, handleDisconnect, 
    handlePowerToggle, lastGroupPatterns, setCrewModeSummary, setCrewRole, setCrewSession, 
    setLastGroupPattern, setLastLeaderScene
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
