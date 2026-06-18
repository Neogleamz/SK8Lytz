/**
 * test-env.d.ts — Global type augmentations for the Jest test environment.
 *
 * Provides typed global declarations so test files can reference React Native
 * globals (__DEV__, Platform) without falling back to `as any` casts.
 * This file is automatically picked up by TypeScript via tsconfig.json includes.
 *
 * Pattern approved by team-roster.md (Sage) on 2026-06-18.
 * Eliminates the `(global as any).__DEV__` anti-pattern across 28 test files.
 */

/**
 * Extends the global namespace so `(global as GlobalWithDev).__DEV__` compiles
 * without casting to `any`.
 */
export type GlobalWithDev = typeof globalThis & { __DEV__: boolean };

/**
 * Typed alias for mutable Platform.OS mutation in beforeEach blocks.
 * Usage:
 *   (Platform as MutablePlatform).OS = 'ios';
 */
export type MutablePlatform = { OS: string };

/**
 * Typed alias for XState `fromCallback` actor internals, extracted for test
 * helpers that reach into `.config` without going through `createActor`.
 *
 * Usage:
 *   const getCallback = () => (myService as CallbackServiceActor).config;
 *
 * The `.config` property is the raw `CallbackLogicFunction` that XState stores
 * on the result of `fromCallback(...)`. Typed to return `unknown` input params
 * to avoid leaking full generic signatures into every test helper.
 */
export type CallbackServiceActor = {
  /** The raw callback function stored by XState's `fromCallback`. */
  config: (params: { input: unknown; sendBack?: (event: unknown) => void }) => (() => void) | void;
};

/**
 * Typed alias for XState `fromPromise` actor internals, extracted for test
 * helpers that extract the `.config` promise creator.
 *
 * Usage:
 *   const getPromise = (input: SomeInput) => (myService as PromiseServiceActor).config({ input });
 */
export type PromiseServiceActor = {
  /** The raw promise creator function stored by XState's `fromPromise`. */
  config: (params: { input: unknown }) => Promise<unknown>;
};
