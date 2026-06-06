import { setup, assign } from 'xstate';
import { BleMachineContext, BleMachineEvent } from './BleMachine.types';
import { AppLogger } from '../AppLogger';

export const bleMachine = setup({
  types: {
    context: {} as BleMachineContext,
    events: {} as BleMachineEvent,
  },
  actions: {
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
      targetMacs: ({ event }) => event.type === 'CONNECT_REQUEST' ? event.targetMacs : undefined
    }),
    setGhostedMacs: assign({
      ghostedDeviceIds: ({ event }) => event.type === 'RECOVERY_START' && event.ghostedMacs ? event.ghostedMacs : []
    }),
    clearGhostedMacs: assign({
      ghostedDeviceIds: () => []
    })
  }
}).createMachine({
  id: 'bleMachine',
  initial: 'IDLE',
  context: {
    connectedDevices: [],
    ghostedDeviceIds: [],
  },
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
        UPDATE_CONNECTED_DEVICES: {
          actions: ['setConnectedDevices']
        }
      }
    },
    SCANNING: {
      on: {
        SCAN_STOP: {
          target: 'IDLE',
          actions: ['clearSweeperId', { type: 'logTransition', params: { from: 'SCANNING', to: 'IDLE' } }]
        },
        CONNECT_REQUEST: {
          target: 'CONNECTING',
          actions: ['clearSweeperId', 'setTargetMacs', { type: 'logTransition', params: { from: 'SCANNING', to: 'CONNECTING' } }]
        },
        DISCONNECT_REQUEST: {
          target: 'DISCONNECTING',
          actions: ['clearSweeperId', { type: 'logTransition', params: { from: 'SCANNING', to: 'DISCONNECTING' } }]
        }
      }
    },
    CONNECTING: {
      on: {
        CONNECT_SUCCESS: {
          target: 'READY',
          actions: ['setConnectedDevices', { type: 'logTransition', params: { from: 'CONNECTING', to: 'READY' } }]
        },
        CONNECT_FAIL: {
          target: 'IDLE',
          actions: [{ type: 'logTransition', params: { from: 'CONNECTING', to: 'IDLE' } }]
        },
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
      on: {
        DISCONNECT_REQUEST: {
          target: 'DISCONNECTING',
          actions: [{ type: 'logTransition', params: { from: 'READY', to: 'DISCONNECTING' } }]
        },
        RECOVERY_START: {
          target: 'RECOVERING',
          actions: ['setGhostedMacs', { type: 'logTransition', params: { from: 'READY', to: 'RECOVERING' } }]
        },
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
      on: {
        RECOVERY_COMPLETE: [
          {
            guard: ({ context }) => context.connectedDevices.length > 0,
            target: 'READY',
            actions: ['clearGhostedMacs', { type: 'logTransition', params: { from: 'RECOVERING', to: 'READY' } }]
          },
          {
            target: 'IDLE',
            actions: ['clearGhostedMacs', { type: 'logTransition', params: { from: 'RECOVERING', to: 'IDLE' } }]
          }
        ],
        CONNECT_REQUEST: {
          target: 'CONNECTING',
          actions: ['setTargetMacs', { type: 'logTransition', params: { from: 'RECOVERING', to: 'CONNECTING' } }]
        },
        RECOVERY_FAIL: {
          target: 'IDLE',
          actions: ['clearGhostedMacs', { type: 'logTransition', params: { from: 'RECOVERING', to: 'IDLE' } }]
        }
      }
    }
  },
  on: {
    FORCE_IDLE: {
      target: '.IDLE',
      actions: ['clearConnectedDevices', 'clearSweeperId', 'clearGhostedMacs', { type: 'logTransition', params: { from: 'ANY', to: 'IDLE', reason: 'forced' } }]
    }
  }
});
