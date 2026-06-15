import { STORAGE_SESSION_PHASE, STORAGE_SESSION_PAUSE_TIME, STORAGE_SESSION_ACTIVE } from '../../constants/storageKeys';
import { setup, assign } from 'xstate';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { WatchBridge } from 'sk8lytz-watch-bridge';
import { AppLogger } from '../appLogger';
import { SessionMachineContext, SessionMachineEvent, SessionPhase } from './SessionMachine.types';
import { sensorService } from './SensorService';
import { autoPauseService } from './AutoPauseService';
import { healthService } from './HealthService';
import { sessionCommitService } from './SessionCommitService';
import { notificationService } from './NotificationService';

const SESSION_PHASE_KEY = STORAGE_SESSION_PHASE;
const SESSION_PAUSE_TIME_KEY = STORAGE_SESSION_PAUSE_TIME;

async function persistSessionPhase(phase: 'active' | 'paused' | 'idle', pauseTimeMs?: number): Promise<void> {
  try {
    const pairs: [string, string][] = [
      [SESSION_PHASE_KEY, phase],
      [STORAGE_SESSION_ACTIVE, phase === 'active' || phase === 'paused' ? 'true' : 'false'],
    ];
    if (phase === 'paused' && pauseTimeMs) {
      pairs.push([SESSION_PAUSE_TIME_KEY, String(pauseTimeMs)]);
    } else if (phase !== 'paused') {
      pairs.push([SESSION_PAUSE_TIME_KEY, '']);
    }
    await AsyncStorage.multiSet(pairs);
  } catch (err: unknown) {
    AppLogger.error('Failed to persist session phase', err instanceof Error ? err.message : String(err), { payload_size: 0, ssi: 0 });
  }
}

export const sessionMachine = setup({
  types: {
    context: {} as SessionMachineContext,
    events: {} as SessionMachineEvent,
    input: {} as Omit<SessionMachineContext, 'sessionPhase' | 'startTimeMs' | 'pauseStartTimeMs' | 'pausedMsAccum'>,
  },
  actors: {
    sensorService,
    autoPauseService,
    healthService,
    sessionCommitService,
    notificationService,
  },
  actions: {
    logTransition: (_, params: { from: string; to: string; reason?: string }) => {
      AppLogger.log('APP_LOG', {
        event: 'session_fsm_transition',
        from: params.from,
        to: params.to,
        reason: params.reason || 'xstate_transition',
      });
    },
    recordStartTime: assign({
      startTimeMs: ({ event }) => {
        if (event.type === 'START' && event.externalStartTimeMs) {
          return event.externalStartTimeMs;
        }
        return Date.now();
      },
      pausedMsAccum: () => 0,
      pauseStartTimeMs: () => null,
      sessionPhase: () => 'ACTIVE' as SessionPhase,
    }),
    recordPauseStartTime: assign({
      pauseStartTimeMs: () => Date.now(),
      sessionPhase: () => 'PAUSED' as SessionPhase,
    }),
    accumulatePausedMs: assign({
      pausedMsAccum: ({ context }) => {
        if (context.pauseStartTimeMs) {
          return context.pausedMsAccum + (Date.now() - context.pauseStartTimeMs);
        }
        return context.pausedMsAccum;
      },
      pauseStartTimeMs: () => null,
    }),
    setPhaseIdle: assign({
      sessionPhase: () => 'IDLE' as SessionPhase,
      startTimeMs: () => null,
      pauseStartTimeMs: () => null,
      pausedMsAccum: () => 0,
    }),
    setPhaseEnding: assign({
      sessionPhase: () => 'ENDING' as SessionPhase,
    }),
    setPhaseActive: assign({
      sessionPhase: () => 'ACTIVE' as SessionPhase,
    }),
    setPhasePaused: assign({
      sessionPhase: () => 'PAUSED' as SessionPhase,
    }),
    persistPhaseActive: () => {
      persistSessionPhase('active');
    },
    persistPhasePaused: () => {
      persistSessionPhase('paused', Date.now());
    },
    persistPhaseIdle: () => {
      persistSessionPhase('idle');
    },
    syncWatchActive: ({ context }) => {
      const startTime = context.startTimeMs || Date.now();
      const isoStart = new Date(startTime).toISOString();
      WatchBridge.syncSessionState({
        status: 'ACTIVE',
        startTime: isoStart,
      }).catch((err: unknown) =>
        AppLogger.warn('WATCH_BRIDGE', {
          event: 'sync_failed_on_start',
          error: err instanceof Error ? err.message : String(err),
          payload_size: 0,
          ssi: 0,
        })
      );
    },
    syncWatchPaused: () => {
      WatchBridge.syncSessionState({ status: 'PAUSED' }).catch((err: unknown) =>
        AppLogger.warn('WATCH_BRIDGE', {
          event: 'sync_failed_on_pause',
          error: err instanceof Error ? err.message : String(err),
          payload_size: 0,
          ssi: 0,
        })
      );
    },
    syncWatchStopped: () => {
      WatchBridge.syncSessionState({ status: 'STOPPED' }).catch(() => {});
    },
  },
}).createMachine({
  id: 'sessionMachine',
  initial: 'IDLE',
  context: ({ input }) => ({
    ...input,
    sessionPhase: 'IDLE',
    startTimeMs: null,
    pauseStartTimeMs: null,
    pausedMsAccum: 0,
  }),
  states: {
    IDLE: {
      entry: ['setPhaseIdle'],
      on: {
        START: {
          target: 'ACTIVE',
          actions: [
            'recordStartTime',
            'persistPhaseActive',
            'syncWatchActive',
            { type: 'logTransition', params: { from: 'IDLE', to: 'ACTIVE' } },
          ],
        },
      },
    },
    ACTIVE: {
      entry: ['setPhaseActive'],
      invoke: [
        {
          id: 'sensorService',
          src: 'sensorService',
          input: ({ context }) => ({
            onTelemetryUpdate: context.onTelemetryUpdate,
            gpsSpeedRef: context.gpsSpeedRef,
            startTimeMs: context.startTimeMs,
          }),
        },
        {
          id: 'autoPauseService',
          src: 'autoPauseService',
          input: ({ context }) => ({
            autoPauseEnabled: context.autoPauseEnabled,
            gpsSpeedRef: context.gpsSpeedRef,
            sessionPhase: 'ACTIVE' as const,
          }),
        },
        {
          id: 'healthService',
          src: 'healthService',
          input: ({ context }) => ({
            onHealthUpdate: context.onHealthUpdate,
          }),
        },
        {
          id: 'notificationService',
          src: 'notificationService',
          input: ({ context }) => ({
            sessionPhase: context.sessionPhase,
            startTimeMs: context.startTimeMs,
            pausedMsAccum: context.pausedMsAccum,
            telemetryRef: context.telemetryRef,
          }),
        },
      ],
      on: {
        PAUSE: {
          target: 'PAUSED',
          actions: [
            'recordPauseStartTime',
            'persistPhasePaused',
            'syncWatchPaused',
            { type: 'logTransition', params: { from: 'ACTIVE', to: 'PAUSED' } },
          ],
        },
        AUTO_PAUSE: {
          target: 'PAUSED',
          actions: [
            'recordPauseStartTime',
            'persistPhasePaused',
            'syncWatchPaused',
            { type: 'logTransition', params: { from: 'ACTIVE', to: 'PAUSED', reason: 'auto_pause' } },
          ],
        },
        END: {
          target: 'ENDING',
          actions: [
            'setPhaseEnding',
            { type: 'logTransition', params: { from: 'ACTIVE', to: 'ENDING' } },
          ],
        },
      },
    },
    PAUSED: {
      entry: ['setPhasePaused'],
      exit: ['accumulatePausedMs'],
      invoke: [
        {
          id: 'autoPauseService',
          src: 'autoPauseService',
          input: ({ context }) => ({
            autoPauseEnabled: context.autoPauseEnabled,
            gpsSpeedRef: context.gpsSpeedRef,
            sessionPhase: 'PAUSED' as const,
          }),
        },
        {
          id: 'notificationService',
          src: 'notificationService',
          input: ({ context }) => ({
            sessionPhase: context.sessionPhase,
            startTimeMs: context.startTimeMs,
            pausedMsAccum: context.pausedMsAccum,
            telemetryRef: context.telemetryRef,
          }),
        },
      ],
      on: {
        RESUME: {
          target: 'ACTIVE',
          actions: [
            'persistPhaseActive',
            'syncWatchActive',
            { type: 'logTransition', params: { from: 'PAUSED', to: 'ACTIVE' } },
          ],
        },
        AUTO_RESUME: {
          target: 'ACTIVE',
          actions: [
            'persistPhaseActive',
            'syncWatchActive',
            { type: 'logTransition', params: { from: 'PAUSED', to: 'ACTIVE', reason: 'auto_resume' } },
          ],
        },
        END: {
          target: 'ENDING',
          actions: [
            'setPhaseEnding',
            { type: 'logTransition', params: { from: 'PAUSED', to: 'ENDING' } },
          ],
        },
      },
    },
    ENDING: {
      entry: ['persistPhaseIdle'],
      invoke: {
        id: 'sessionCommitService',
        src: 'sessionCommitService',
        input: ({ context }) => ({
          startTimeMs: context.startTimeMs,
          pausedMsAccum: context.pausedMsAccum,
          onSessionSaved: context.onSessionSaved,
          telemetryRef: context.telemetryRef,
          healthRef: context.healthRef,
          userIdRef: context.userIdRef,
        }),
        onDone: {
          target: 'IDLE',
          actions: [
            'syncWatchStopped',
            { type: 'logTransition', params: { from: 'ENDING', to: 'IDLE' } },
          ],
        },
        onError: {
          target: 'IDLE',
          actions: [
            'syncWatchStopped',
            { type: 'logTransition', params: { from: 'ENDING', to: 'IDLE', reason: 'commit_failed' } },
          ],
        },
      },
    },
  },
});
