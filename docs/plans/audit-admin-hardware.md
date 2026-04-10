### Design Decisions & Rationale
We are fully deprecating `applyColorSorting` from the software layer. As proven in Epic #31, the physical controllers process the `0x62` configuration byte and natively remap standard RGB arrays to GRB format on the IC hardware. Any software preprocessing in `DockedController.tsx` is inherently flawed and causes a double-inversion of the array payload. This plan completely purges `applyColorSorting` and resolves `#7` since the Admin Tester itself was already migrated.

## Proposed Changes

### `src/protocols/ZenggeProtocol.ts`
- [DELETE] Remove `applyColorSorting` function entirely. This function no longer serves a purpose with correctly programmed hardware.

### `src/components/DockedController.tsx`
- [MODIFY] **Line 776-781**: Refactor the Favorites layout color generator so that it simply returns the parsed hex into `r, g, b` without sorting.
- [MODIFY] **Line 805-812**: Refactor `generateSortedColors` into `generatePristineColors`, returning mapped RGB objects verbatim.
- [MODIFY] **Line 1279-1280**: Remove sorting overrides from `Dual Color Mode` payloads, passing straight RGB into the payload array.
- [MODIFY] **Line 2236**: Remove sorting logic from the `BUILDER` multi-color dispatch loop.

## Open Questions
- None. This is a strict removal of deprecated regression logic that fights the device's native hardware EEPROM.

## Verification Plan
1. Send a multi-color builder combination to the device that contrasts Red & Green heavily.
2. Confirm that the pure `R` and `G` values hit the diagnostic sniffer cleanly as pure RGB without any matrix scrambling.
