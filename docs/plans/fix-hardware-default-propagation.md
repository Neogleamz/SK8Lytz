# 🛠️ Implementation Plan: Hardware Default Propagation
  
## Objective
The system is currently failing to accurately propagate the hardware defaults (specifically the 2-segment configuration for HALOZ) defined in the Product Catalog / Product Manager into the live runtime environment. This plan will correctly bridge the `ProductCatalog` definitions with the `ZenggeController` connection payload to ensure 100% parity between the Admin Hub UI configuration and the physical LED segments lit up on connection.

## Design Decisions & Rationale
We are defining the source of truth for all hardware configurations as the `ZenggeController`/`useProductCatalog` synergy. We cannot allow default heuristics (like an arbitrary "1 segment" assumption) to run un-checked during connection. We will intercept the connection phase and strictly read `vizStripCount` and `ledPoints` from the fetched `ProductProfile` to build the `0x62` (or relevant) settings payload.

## ⚠️ User Review Required
> [!IMPORTANT]
> The exact payload command for setting the IC segments differs by LED controller version but typically involves a multi-byte array where we specify the chip type and string length. Is there a specific protocol byte sequence (e.g. `[0x81, etc...]`) defined in the Master Reference that establishes strip counts? If this is documented there, we will abide by it completely.

## Proposed Changes

### `src/hooks/useDeviceSync.ts` or `src/services/ZenggeController.ts`
- **[MODIFY]**: Inject the `ProductProfile` lookup at the point of connection. When resolving the MAC address, we will fetch the product definition and inject `vizStripCount` (Defaulting to 2 for HALOZ) and `ledPoints` directly into the LED string length command sent immediately after Handshake.

### `src/components/AdminToolsModal.tsx`
- **[MODIFY]**: Change the label for `Strip Count (Railz)` to abstract it to `Segment Strip Count` so that the UI stops forcing the assumption that only Railz have 2 strips. We will establish that HALOZ natively have 2 physical strips wired together. 

## Open Questions
> [!WARNING]
> Do HALOZ and RAILZ use the identical byte command to configure strip count? We currently know the Zengge controller can configure X segments and Y LEDs per segment. When we send the setup payload, should HALOZ be assigned `(Segments = 2, LEDs/Seg = N)` or does it behave differently at a byte-protocol level?

## Verification Plan

### Automated Tests
- Run `npx tsc --noEmit` to ensure type bindings for product profiles pass through the BLE hooks accurately.

### Manual Verification
- Connect to a HALOZ controller.
- Verify via ADB logcat that the setup payload successfully transmits `segments: 2`.
- Check physical HALOZ hardware to confirm that both rings mirror colors correctly and aren't dropping 50% of the payload.
