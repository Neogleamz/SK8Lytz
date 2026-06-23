/**
 * Jest mock for react-native-mmkv (v4 JSI / Nitro module).
 *
 * MMKV relies on JSI (C++ turbo-module) which cannot run in Node/Jest.
 * This mock provides a synchronous in-memory Map-backed implementation
 * that mirrors the MMKV interface used by AppLoggerStorage.
 *
 * Place: src/__mocks__/react-native-mmkv.ts
 * Auto-loaded: via moduleNameMapper in jest.config.js
 */

class MockMMKV {
  private store = new Map<string, string>();

  getString(key: string): string | undefined {
    return this.store.get(key);
  }

  set(key: string, value: string): void {
    this.store.set(key, value);
  }

  remove(key: string): boolean {
    return this.store.delete(key);
  }

  contains(key: string): boolean {
    return this.store.has(key);
  }

  getAllKeys(): string[] {
    return Array.from(this.store.keys());
  }

  clearAll(): void {
    this.store.clear();
  }
}

export const createMMKV = jest.fn((_config?: { id?: string }) => new MockMMKV());
