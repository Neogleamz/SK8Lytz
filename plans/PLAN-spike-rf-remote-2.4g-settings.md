# Implementation Plan: spike/rf-remote-2.4g-settings

## Goal
Reverse engineer and discover the proprietary BLE opcodes/payloads sent by the ZENGGE app when configuring 2.4G RF Remote Settings: **Allow All**, **Don't Allow**, **Paired Only**, and **Clear Pairing** on our `0xA3` controller. Then, document these opcodes in `tools/ZENGGE_PROTOCOL_BIBLE.md` and design an implementation plan to integrate these controls into the SK8Lytz App.

## Proposed Steps

### Phase 1: Environment Provisioning (DONE)
- [x] Verified physical Android device `27131JEGR40625` is connected via ADB.
- [x] Cleaned/uninstalled existing `com.zengge.blev2` app to avoid split-apk parse/downgrade issues.
- [x] Installed ZENGGE 1.5.0 Split APKs onto the device successfully.

### Phase 2: Active Interception Spike
1. **Launch ZENGGE App**: Launch the ZENGGE app via monkey command on ADB.
2. **Connect to Skate Controller**: Connect the ZENGGE app to our physical `0xA3` skate controller (`Ctrl_Mini_RGB_Symphony_new_0xA3`).
3. **Capture ADB Logcat Logs**:
   Stream logs filtering for Flutter method calls and BLE write characteristics to capture the commands:
   ```powershell
   & $ADB logcat -s "BluetoothGatt:D BluetoothLeScanner:D ReactNativeJS:V FlutterBleControl:D FlutterNewControlPlugin:D"
   ```
4. **HCI Snoop Interception (Gold Standard)**:
   - Navigate to the 2.4G Remote settings screen in the ZENGGE app.
   - Toggle each of the 4 remote settings:
     - **Allow All**
     - **Don't Allow**
     - **Paired Only**
     - **Clear Pairing**
   - Pull the HCI logcat or the physical `/sdcard/btsnoop_hci.log` if needed to extract the exact hexadecimal BLE packets.
5. **Verify Byte Math**: Confirm the checksum and structure of each intercepted packet using the standard V2 Zengge wrap command algorithm:
   `[0x00, Seq, 0x80, 0x00, LenHi, LenLo, Len+1, 0x0B, ...innerPayload]`

### Phase 3: Documentation & Master Reference Update
- Update `tools/ZENGGE_PROTOCOL_BIBLE.md` with a new section §RF Remote Settings containing the reverse-engineered opcodes and byte mappings.
- Outline the implementation path to expose these settings in our SK8Lytz App's hardware programmer modal (`Sk8LytzProgrammerModal.tsx`).

## User Review Required

> [!IMPORTANT]
> This is a discovery spike. **NO source code changes** will be made to the app itself during this task.
> All work is purely investigative and documentation-focused.

## Verification Plan

### Automated Verification
- We will verify the checksum of each discovered byte sequence against the `C14184b.m4780b` simple SUM algorithm.

### Manual Verification
- We will physically toggle the options in the ZENGGE app while streaming logs via ADB to verify repeatable, precise byte dispatches for each option.
