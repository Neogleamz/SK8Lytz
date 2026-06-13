# [PLAN] feat/spatial-beat-mapping (Sound-to-Light Spatialization)

### Design Decisions & Rationale

We are evolving "Music Mode" from a simple full-strip flash to a **Spatial Audio Experience**. By analyzing the high-frequency FFT (Fast Fourier Transform) data from the phone's microphone, we can map different sonic frequencies to specific physical pixel locations on the skates (Bass to Front, Treble to Back), creating a "3D Visual Equalizer."

## Proposed Changes

### [Component Name] Audio Processor

#### [MODIFY] [AudioService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AudioService.ts) (or create new `SpatialAudioService.ts`)

- Implement a 3-lane frequency parser:
  - **Bass Lane (20Hz - 250Hz)** -> Triggers Front Pixels.
  - **Mid Lane (250Hz - 4kHz)** -> Triggers Middle Pixels.
  - **Treble Lane (4kHz - 20kHz)** -> Triggers Back Pixels.

### [Component Name] Hardware Dispatcher

#### [MODIFY] [ZenggeController.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ZenggeController.ts)

- Implement `dispatchSpatialPalette`: Converting the 3-lane magnitude data into a 0x59 color array in real-time.

## Verification Plan

1. **Dynamic Frequency Test**: Play a bass-heavy track vs. a high-frequency acoustic track; verify the pixel "hot spots" shift between the front and back of the strip physically.
2. **Latency Audit**: Ensure the FFT-to-BLE dispatch loop remains under 50ms to maintain the "Feeling" of the beat.
3. **Hardware Stress**: Test on a full 300-LED strip to ensure the `totalLen` calculation for the 0x59 array is handled without buffer overflow.
