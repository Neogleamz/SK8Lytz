import { setup, assign } from 'xstate';
import type { Device } from 'react-native-ble-plx';
import { BleMachineContext, BleMachineEvent } from './BleMachine.types';
import { AppLogger } from '../appLogger';
import { connectService } from './ConnectService';
import { recoveryService } from './RecoveryService';
import { heartbeatService } from './HeartbeatService';
export const bleMachine = setup({
  types: {
    context: {} as BleMachineContext,
    events: {} as BleMachineEvent,
    input: {} as Omit<BleMachineContext, 'connectedDevices' | 'ghostedDeviceIds'>,
  },
  actors: { connectService, recoveryService, heartbeatService },
  actions: {
    setDeviceUnreachable: () => {},
    notifyUserDeviceFailed: () => {},
    logTransition: (_, params: { from: string; to: string; reason?: string }) => {
      AppLogger.log('BLE_STATE_CHANGE', {
        event: 'fsm_transition',
        from: params.from,
        to: params.to,
        reason: params.reason || 'xstate_transition',
      });
    },
    setConnectedDevices: assign({
      connectedDevices: ({ event }) => {
        if (event.type === 'CONNECT_SUCCESS' || event.type === 'UPDATE_CONNECTED_DEVICES') {
          return event.devices;
        }
        if (event.type === 'xstate.done.actor.connectService' && event.output?.devices) {
          return event.output.devices;
        }
        return [];
      }
    }),
    clearConnectedDevices: assign({
      connectedDevices: () => []
    }),
    setSweeperId: assign({
      sweeperId: ({ event }) => event.type === 'SCAN_START' ? event.sweeperId : undefined
    }),
    clearSweeperId: assign({
      sweeperId: () => undefined
    }),
    setTargetMacs: assign({
      targetMacs: ({ event }: any) => {
        if (event.type === 'CONNECT_REQUEST') return event.targetMacs;
        if (event.type === 'RECOVERY_START') return event.ghostedMacs;
        if (event.type === 'RESTORE_PERIPHERALS') return event.peripherals.map((p: any) => p.id);
        return undefined;
      }
    }),
    setGhostedMacs: assign({
      ghostedDeviceIds: ({ event }) => {
        if (event.type === 'RECOVERY_START' && event.ghostedMacs) return event.ghostedMacs;
        if (event.type === 'HEARTBEAT_FAIL') return [event.deviceId];
        return [];
      }
    }),
    clearGhostedMacs: assign({
      ghostedDeviceIds: () => []
    })
  }
}).createMachine({
  id: 'bleMachine',
  initial: 'IDLE',
  context: ({ input }) => ({
    ...input,
    connectedDevices: [],
    ghostedDeviceIds: [],
  }),
  states: {
    IDLE: {
      on: {
        SCAN_START: {
          target: 'SCANNING',
          actions: ['setSweeperId', { type: 'logTransition', params: { from: 'IDLE', to: 'SCANNING' } }]
        },
        CONNECT_REQUEST: {
          target: 'CONNECTING',
          actions: ['setTargetMacs', { type: 'logTransition', params: { from: 'IDLE', to: 'CONNECTING' } }]
        },
        DISCONNECT_REQUEST: {
          target: 'DISCONNECTING',
          actions: [{ type: 'logTransition', params: { from: 'IDLE', to: 'DISCONNECTING' } }]
        },
        RECOVERY_START: {
          target: 'RECOVERING',
          actions: ['setGhostedMacs', { type: 'logTransition', params: { from: 'IDLE', to: 'RECOVERING' } }]
        },
        RESTORE_PERIPHERALS: {
          target: 'RESTORING',
          actions: ['setTargetMacs', { type: 'logTransition', params: { from: 'IDLE', to: 'RESTORING' } }]
        },
        UPDATE_CONNECTED_DEVICES: {
          actions: ['setConnectedDevices']
        }
      }
    },
    SCANNING: {
      entry: [
        ({ context }) => {
          context.bleManager?.startDeviceScan(
            context.scanServiceUUIDs,
            { allowDuplicates: false, scanMode: context.scanMode },
            context.scanCallback
          );
        },
        { type: 'logTransition', params: { from: 'ANY', to: 'SCANNING' } }
      ],
      exit: [
        ({ context }) => {
          context.bleManager?.stopDeviceScan();
        }
      ],
      on: {
        SCAN_STOP: {
          target: 'IDLE',
          actions: ['clearSweeperId', { type: 'logTransition', params: { from: 'SCANNING', to: 'IDLE' } }]
        },
        CONNECT_REQUEST: {
          target: 'CONNECTING',
          actions: ['clearSweeperId', 'setTargetMacs', { type: 'logTransition', params: { from: 'SCANNING', to: 'CONNECTING' } }]
        },
        SCAN_PAUSE: {
          actions: ({ context }) => context.bleManager?.stopDeviceScan()
        },
        SCAN_RESUME: {
          actions: ({ context }) => context.bleManager?.startDeviceScan(
            context.scanServiceUUIDs,
            { allowDuplicates: false, scanMode: context.scanMode },
            context.scanCallback
          )
        },
        DISCONNECT_REQUEST: {
          target: 'DISCONNECTING',
          actions: ['clearSweeperId', { type: 'logTransition', params: { from: 'SCANNING', to: 'DISCONNECTING' } }]
        },
        RECOVERY_START: {
          target: 'RECOVERING',
          actions: ['setGhostedMacs', { type: 'logTransition', params: { from: 'SCANNING', to: 'RECOVERING' } }]
        }
      }
    },
    RESTORING: {
      after: {
        1000: {
          target: 'CONNECTING',
          actions: [{ type: 'logTransition', params: { from: 'RESTORING', to: 'CONNECTING', reason: 'restore_delay_elapsed' } }]
        }
      }
    },
    CONNECTING: {
      invoke: {
        id: 'connectService',
        src: 'connectService',
        input: ({ context }) => ({
          bleManager: context.bleManager,
          targetMacs: context.targetMacs ?? [],
          connectedDevicesRef: { current: context.connectedDevices },
          adapterMapRef: context.adapterMapRef,
          mtuMapRef: context.mtuMapRef,
          disconnectListeners: context.disconnectListeners,
          blacklistedMacsRef: context.blacklistedMacsRef,
          handleOrganicDisconnect: context.handleOrganicDisconnect,
          onOrganicDisconnect: context.onOrganicDisconnect,
          handleNotification: context.handleNotification,
          enqueueWrite: context.enqueueWrite,
        }),
        onDone: {
          target: 'READY',
          actions: ['setConnectedDevices', { type: 'logTransition', params: { from: 'CONNECTING', to: 'READY' } }]
        },
        onError: {
          target: 'IDLE',
          actions: [{ type: 'logTransition', params: { from: 'CONNECTING', to: 'IDLE', reason: 'connect_failed' } }]
        }
      },
      on: {
        RECOVERY_START: {
          target: 'RECOVERING',
          actions: ['setGhostedMacs', { type: 'logTransition', params: { from: 'CONNECTING', to: 'RECOVERING' } }]
        },
        DISCONNECT_REQUEST: {
          target: 'DISCONNECTING',
          actions: [{ type: 'logTransition', params: { from: 'CONNECTING', to: 'DISCONNECTING' } }]
        }
      }
    },
    READY: {
      invoke: [{
        src: 'heartbeatService',
        input: ({ context }) => ({
          bleManager: context.bleManager,
          connectedDevices: context.connectedDevices,
          adapterMap: context.adapterMapRef.current,
        })
      }],
      on: {
        HEARTBEAT_FAIL: {
          target: 'RECOVERING',
          actions: ['setGhostedMacs', { type: 'logTransition', params: { from: 'READY', to: 'RECOVERING', reason: 'heartbeat_fail' } }]
        },
        CONNECT_REQUEST: {
          target: 'CONNECTING',
          actions: ['setTargetMacs', { type: 'logTransition', params: { from: 'READY', to: 'CONNECTING' } }]
        },
        DISCONNECT_REQUEST: {
          target: 'DISCONNECTING',
          actions: [{ type: 'logTransition', params: { from: 'READY', to: 'DISCONNECTING' } }]
        },
        RECOVERY_START: [
          {
            guard: ({ event }) => event.type === 'RECOVERY_START' && (event.ghostedMacs?.length ?? 0) >= 2,
            target: 'CONNECTING',
            actions: ['setTargetMacs', { type: 'logTransition', params: { from: 'READY', to: 'CONNECTING', reason: 'group_recovery' } }]
          },
          {
            target: 'RECOVERING',
            actions: ['setGhostedMacs', { type: 'logTransition', params: { from: 'READY', to: 'RECOVERING' } }]
          }
        ],
        UPDATE_CONNECTED_DEVICES: [
          {
            guard: ({ event }) => event.type === 'UPDATE_CONNECTED_DEVICES' && event.devices.length === 0,
            target: 'IDLE',
            actions: ['setConnectedDevices', { type: 'logTransition', params: { from: 'READY', to: 'IDLE', reason: 'zero_devices' } }]
          },
          {
            actions: ['setConnectedDevices']
          }
        ]
      }
    },
    DISCONNECTING: {
      on: {
        DISCONNECT_COMPLETE: {
          target: 'IDLE',
          actions: ['clearConnectedDevices', 'clearGhostedMacs', { type: 'logTransition', params: { from: 'DISCONNECTING', to: 'IDLE' } }]
        }
      }
    },
    RECOVERING: {
      invoke: {
        src: 'recoveryService',
        input: ({ context }) => ({
          bleManager: context.bleManager,
          ghostedDeviceIds: context.ghostedDeviceIds ?? [],
          adapterMapRef: context.adapterMapRef,
          mtuMapRef: context.mtuMapRef,
          disconnectListeners: context.disconnectListeners,
          handleOrganicDisconnect: context.handleOrganicDisconnect,
          onOrganicDisconnect: context.onOrganicDisconnect,
          handleNotification: context.handleNotification,
        }),
        onDone: undefined,
        onError: undefined,
      },
      on: {
        RECOVERY_COMPLETE: [
          {
            target: 'READY',
            actions: [
              assign({
                connectedDevices: ({ context, event }) => {
                  const newDevices = event.devices || [];
                  const existingIds = newDevices.map((d: Device) => d.id);
                  const filtered = context.connectedDevices.filter(d => !existingIds.includes(d.id));
                  return [...filtered, ...newDevices];
                }
              }),
              'clearGhostedMacs',
              { type: 'logTransition', params: { from: 'RECOVERING', to: 'READY' } }
            ]
          }
        ],
        CONNECT_REQUEST: {
          target: 'CONNECTING',
          actions: ['setTargetMacs', { type: 'logTransition', params: { from: 'RECOVERING', to: 'CONNECTING' } }]
        },
        RECOVERY_PERMANENTLY_FAILED: {
          target: 'IDLE',
          actions: ['setDeviceUnreachable', 'notifyUserDeviceFailed', 'clearGhostedMacs', { type: 'logTransition', params: { from: 'RECOVERING', to: 'IDLE', reason: 'permanent_fail' } }]
        },
        RECOVERY_FAIL: {
          target: 'IDLE',
          actions: ['clearGhostedMacs', { type: 'logTransition', params: { from: 'RECOVERING', to: 'IDLE' } }]
        },
        DISCONNECT_REQUEST: {
          target: 'DISCONNECTING',
          actions: [{ type: 'logTransition', params: { from: 'RECOVERING', to: 'DISCONNECTING' } }]
        }
      }
    }
  },
  on: {
    FORCE_IDLE: {
      target: '.IDLE',
      // NOTE: clearConnectedDevices intentionally omitted here.
      // FORCE_IDLE resets the activity gate (SCANNING/CONNECTING) back to IDLE,
      // NOT the device connection list. Devices are removed only on DISCONNECT_COMPLETE.
      // Adding clearConnectedDevices here caused: mock web connection → CONNECTING →
      // setConnectedDevices(devices) → FORCE_IDLE clears them → isActuallyConnected=false
      // → blank blue screen. Also broke the keepalive cache-hit path.
      actions: ['clearSweeperId', 'clearGhostedMacs', { type: 'logTransition', params: { from: 'ANY', to: 'IDLE', reason: 'forced' } }]
    }
  }
});
