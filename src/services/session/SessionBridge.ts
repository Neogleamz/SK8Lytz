import { SessionMachineEvent } from './SessionMachine.types';

let _send: ((event: SessionMachineEvent) => void) | null = null;

export const SessionBridge = {
  register: (fn: (event: SessionMachineEvent) => void) => {
    _send = fn;
  },
  unregister: () => {
    _send = null;
  },
  send: (event: SessionMachineEvent) => {
    _send?.(event);
  },
};
