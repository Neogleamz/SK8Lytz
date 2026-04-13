# Implementation Plan: Protocol Integration Cleanup (remove-protocol-setcolor)

### Design Decisions & Rationale

To fulfill Bucket List items #23 and #24 under `epic/protocol-integration`, we must scrub the codebase of legacy protocol artifacts.
First, `setColor()` in `ZenggeProtocol.ts` passes raw colors to `setMultiColor()` without running them through the hardware's mapped color sorting constants (`applyColorSorting`). A codebase audit confirms `setColor()` is 100% dead code and fully unused, making it safe to completely delete to prevent future regressions.
Second, the legacy `0x81` hardware config write command has already been functionally replaced by `0x62` (EEPROM write) across the core engines, but the proxy method `setHardwareConfig` still exists and is used by the `Diagnostic Lab`. We will remove `setHardwareConfig` from the protocol, migrate the Lab entirely to `writeHardwareSettingsByName`, and update legacy documentation comments referencing `0x81`.

### Proposed Changes

#### `src/protocols/ZenggeProtocol.ts`

- **DELETE:** Remove `setColor()` entirely since it bypasses color mapping sequences.
- **DELETE:** Remove the deprecated `setHardwareConfig()` proxy method.
- **MODIFY:** Update `parseHardwareConfig` deprecation comments to reflect `0x62` standard.

#### `src/components/Sk8LytzDiagnosticLab.tsx`

- **MODIFY:** Update line 226 from `ZenggeProtocol.setHardwareConfig(pts, bldOrder, bldIc, seg)` to `ZenggeProtocol.writeHardwareSettingsByName(pts, seg, bldIc, bldOrder)`.

#### `src/protocols/PatternEngine.ts`

#### `src/components/DockedController.tsx`

- **MODIFY:** Revise inline comments from "via 0x81 config" to "via 0x62 config" globally for absolute accuracy.

### UI & Platform Strategy

These changes constitute pure business logic and static protocol generation logic, with zero UI impact. The payload byte lengths exactly match the standard Bluetooth LE MTU without chunking.

### Verification Plan

- **Manual Verification:** No compilation errors on strict TypeScript checks, and verify that the Diagnostic Lab still compiles successfully with the 0x62 target generator.
