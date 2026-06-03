# Fix Visualizer Breathing Speed

Adjust the animation tick speed for all breathing effects in the visualizers to match the slow, natural breathing rate of the physical hardware instead of strobing.

## User Review Required

> [!NOTE]
> This change only affects the visual rendering speed of the app visualizers (both the main product visualizer and the mini preview stripes). It has zero impact on the BLE opcodes or hardware settings dispatched to the controller (which already handles breathing timing autonomously).

## Open Questions

None.

## Proposed Changes

### UI Visualizers

#### [MODIFY] [ProductVisualizer.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/ProductVisualizer.tsx#L62-L70)
Modify the animation loop duration calculation when `mode === 'MULTIMODE'` to scale the `baseDuration` up significantly for breathing patterns (IDs 17, 24, 36, 207, 208, 209, 211):
- Single-color breath: 4,000ms base (gives ~2.7s per breathe at speed 50)
- 3-Color breath: 12,000ms base (~2.7s per breathe)
- Rainbow/4-Color breath: 16,000ms base (~2.7s per breathe)
- 7-Color breath: 28,000ms base (~2.7s per breathe)

```typescript
      let baseDuration = 1500;
      if (mode === 'MUSIC') {
        baseDuration = 800;
      } else if (mode === 'BUILDER') {
        baseDuration = builderTransitionType === 0x03 ? 350 : 1500;
      } else if (mode === 'STREET') {
        baseDuration = 1400;
      } else if (mode === 'MULTIMODE') {
        const isBreathe = [17, 24, 36, 207, 208, 209, 211].includes(patternId);
        if (isBreathe) {
          if (patternId === 17 || patternId === 24 || patternId === 207) {
            baseDuration = 4000;
          } else if (patternId === 208) {
            baseDuration = 12000;
          } else if (patternId === 209 || patternId === 36) {
            baseDuration = 16000;
          } else if (patternId === 211) {
            baseDuration = 28000;
          }
        } else {
          baseDuration = 1500;
        }
      }
```

#### [MODIFY] [CustomEffectVisualizer.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CustomEffectVisualizer.tsx#L45-L50)
Apply a speed divisor to `tickIncrement` in `CustomEffectVisualizer` for the breathing pattern IDs to slow down the frame-rate accumulation and match the main visualizer:
```typescript
    const frameRate = Math.max(16, 200 - (speed * 1.8)); 
    
    let speedDivisor = 1;
    const isBreathe = [17, 24, 36, 207, 208, 209, 211].includes(effectId);
    if (isBreathe) {
      if (effectId === 17 || effectId === 24 || effectId === 207) speedDivisor = 2.5;
      else if (effectId === 208) speedDivisor = 7.5;
      else if (effectId === 209 || effectId === 36) speedDivisor = 10;
      else if (effectId === 211) speedDivisor = 17.5;
    }
    const tickIncrement = (speed / 2000) / speedDivisor;
```

## Verification Plan

### Automated Tests
- Run `npm run verify` to confirm there are no compile-time regressions.

### Manual Verification
- Go to the fixed patterns / Symphony tab, select "Smooth Breath" or "7-Color Breathing".
- Observe the main visualizer and assert that the breathing rate is slow, smooth, and matches a real physical fade rate.
- Open the pattern list and confirm the preview stripe animations are also breathing at a slow, matching rate.
