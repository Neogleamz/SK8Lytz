# ⚡ Flash-Executable Implementation Plan: Fix Sync State Regressions

> **WARNING TO AUTHOR (THINK MODEL)**: This plan is designed to be executed blindly by a `[🤖 FLASH]` or pure execution model in a future session.
> Do NOT use line numbers (`Replace lines 45-50`). The codebase may have drifted between plan creation and execution.
> You MUST use **Semantic Anchors** (e.g. `Find the entire useBLE hook`, `Search for the exact phrase: return <View>`).
> All code snippets must be 100% complete, fully typed, and ready to be copy-pasted via the `replace_file_content` tool.

---

## 1. Pre-Flight Context Check (Drift Verification)

- [ ] **Check 1:** Open `src/services/DeviceRepository.ts`. Search for semantic anchor: `async saveDevice(device: Partial<RegisteredDevice> & { device_mac: string }): Promise<boolean> {`.
- [ ] **Check 2:** Open `src/services/DeviceRepository.ts`. Search for semantic anchor: `async syncFromCloud(): Promise<RegisteredDevice[]> {`.

---

## 2. Step-by-Step Execution Strict Instructions

### Step 2.1: Fix Permanent Tombstone Lock (saveDevice)

- **Target File:** `src/services/DeviceRepository.ts`
- **Semantic Anchor / Target Content:** Find the following block inside `saveDevice`:

```typescript
      // 1. Update in-memory state
      const idx = this.devices.findIndex(d => d.device_mac.toUpperCase() === normalizedMac);
      if (idx >= 0) this.devices[idx] = fullDevice;
      else this.devices.push(fullDevice);

      // 2. Persist to AsyncStorage
```

**Exact Replacement Snippet:**

```typescript
      // 1. Update in-memory state
      const idx = this.devices.findIndex(d => d.device_mac.toUpperCase() === normalizedMac);
      if (idx >= 0) this.devices[idx] = fullDevice;
      else this.devices.push(fullDevice);

      // 1.5 Purge from Tombstone (BUG-14 Fix)
      if (this.tombstones.includes(normalizedMac)) {
        this.tombstones = this.tombstones.filter(t => t !== normalizedMac);
        await AsyncStorage.setItem(TOMBSTONE_KEY, JSON.stringify(this.tombstones)).catch(() => {});
      }

      // 2. Persist to AsyncStorage
```

### Step 2.2: Fix Offline Device Wiping (`syncFromCloud`)

- **Target File:** `src/services/DeviceRepository.ts`
- **Semantic Anchor / Target Content:** Find the following block near the end of `syncFromCloud`:

```typescript
      // Update in-memory state
      this.devices = merged;
      await AsyncStorage.setItem(DEVICES_KEY, JSON.stringify(merged));
      this._notifyListeners();
```

**Exact Replacement Snippet:**

```typescript
      // Pure-local preservation (BUG-13 Fix)
      // Retain offline devices that haven't hit the cloud yet
      const offlineLocalOnly = this.devices.filter(
        localD => 
          localD.is_pending_sync && 
          !cloudRows.some((cloudD: any) => cloudD.device_mac.toUpperCase() === localD.device_mac.toUpperCase())
      );
      
      const finalDevices = [...merged, ...offlineLocalOnly];

      // Update in-memory state
      this.devices = finalDevices;
      await AsyncStorage.setItem(DEVICES_KEY, JSON.stringify(finalDevices));
      this._notifyListeners();
```

---

## 3. Post-Execution Verification

- [ ] **Command:** `npx tsc --noEmit`
  - _Expected Output:_ Clean exit (0 errors) relating to the modified files.
- [ ] **Manual Step:** Tombstone loop test (Delete device -> Re-add -> Reboot App -> Verify it stays).

---

**Completion:** Once all checks pass, proceed to Commit Phase using the semantic message: `fix(data): resolve offline wiping and tombstone locking regressions in DeviceRepository`
