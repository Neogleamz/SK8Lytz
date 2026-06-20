import type { RGB, PatternId, PatternOptions } from '../shared/engineTypes';
import { dim, lerpRGB, hsvToRgb, blendRGB } from '../shared/spatialMath';

// ---------------------------------------------
// Group E: Temporal (IDs 20-24)
// ---------------------------------------------


export function buildSmoothBreath(fg: RGB, numLEDs: number, tick: number): RGB[] {
  const brightness = Math.sin(tick * Math.PI) ** 2; // smooth power curve
  return Array(numLEDs).fill({
    r: Math.round(fg.r * brightness),
    g: Math.round(fg.g * brightness),
    b: Math.round(fg.b * brightness),
  });
}

export function buildHardJumpFlash(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  // Hard binary alternation - no crossfade
  return Array(numLEDs).fill(tick < 0.5 ? fg : bg);
}

export function buildTemporalStrobe(fg: RGB, numLEDs: number, tick: number): RGB[] {
  // High-frequency flash (10x per cycle)
  const on = (tick * 10) % 1 < 0.2; // 20% duty cycle = sharp strobe
  return Array(numLEDs).fill(on ? fg : { r: 0, g: 0, b: 0 });
}

export function buildWipeFill(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const t = direction === 0 ? tick : 1 - tick;
  // Fill from one end, then wipe back - forward half fills, back half wipes
  const progress = t < 0.5 ? t * 2 : (1 - t) * 2;
  const fillCount = Math.floor(progress * numLEDs);
  return Array.from({ length: numLEDs }, (_, i) => i < fillCount ? fg : bg);
}

export function buildWipeCenterOut(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const center = Math.floor(numLEDs / 2);
  // progress from 0 to 1 back to 0
  const progress = tick < 0.5 ? tick * 2 : (1 - tick) * 2;
  const radius = Math.floor(progress * center);
  return Array.from({ length: numLEDs }, (_, i) =>
    Math.abs(i - center) <= radius ? fg : bg
  );
}

// ---------------------------------------------
// Group F: Generative / Rainbow (IDs 25-28)
// ---------------------------------------------


export function buildTrueRainbowFlow(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const hue = ((i / numLEDs) + phase) % 1.0;
    return hsvToRgb(hue, 1.0, 1.0);
  });
}

export function buildRainbowMarquee(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const PERIOD = 3; // grouped blocks with rainbow hue
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * PERIOD) % PERIOD;
  return Array.from({ length: numLEDs }, (_, i) => {
    if ((i + offset) % PERIOD !== 0) return { r: 0, g: 0, b: 0 }; // gap
    const hue = (i / numLEDs + t) % 1.0;
    return hsvToRgb(hue, 1.0, 1.0);
  });
}

export function buildRainbowComet(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const TAIL = Math.floor(numLEDs * 0.3);
  const t = direction === 0 ? tick : 1 - tick;
  const head = Math.floor(t * (numLEDs + TAIL)) - TAIL;
  return Array.from({ length: numLEDs }, (_, i) => {
    const dist = head - i;
    if (dist < 0 || dist >= TAIL) return { r: 0, g: 0, b: 0 };
    const brightness = 1 - (dist / TAIL);
    const hue = (i / numLEDs + t) % 1.0; // rainbow position along strip
    return hsvToRgb(hue, 1.0, brightness);
  });
}

export function buildCyberpunkShift(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const posNorm = i / numLEDs;
    // Sharp digital glitch band scrolling
    const band = (posNorm + phase * 2) % 1;
    if (band < 0.08) return fg; // bright FG slice
    if (band < 0.12) return { r: 255, g: 255, b: 255 }; // white edge flash
    // Sub-bands of bg with slight brightness modulation
    const bgBrightness = 0.3 + Math.sin(posNorm * Math.PI * 8 + phase * Math.PI * 4) * 0.2;
    return blendRGB(bg, { r: 0, g: 0, b: 0 }, bgBrightness);
  });
}

