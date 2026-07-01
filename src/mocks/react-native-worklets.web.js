/**
 * Web stub for react-native-worklets.
 *
 * react-native-worklets calls TurboModuleRegistry.getEnforcing('NativeWorklets')
 * which is undefined on web, crashing the app at module load time.
 *
 * This no-op shim is aliased by metro.config.js when platform === 'web' so the
 * native module is never loaded in the browser bundle. All worklets-dependent
 * features (e.g. Vision Camera frame processors) are already gated to native-only
 * code paths, so this stub is never actually called.
 *
 * Storage: AsyncStorage + Supabase only (no SQLite in the app).
 */
export default {};
export const useSharedValue = () => ({ value: null });
export const useAnimatedStyle = () => ({});
export const runOnJS = (fn) => fn;
export const runOnUI = (fn) => fn;
