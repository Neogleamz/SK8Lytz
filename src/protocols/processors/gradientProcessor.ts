import type { RGB, PatternId, PatternOptions } from '../shared/engineTypes';
import { dim, lerpRGB, hsvToRgb, blendRGB } from '../shared/spatialMath';

export function buildTheaterChase(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const PERIOD = 3; // every 3rd LED lights
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * PERIOD) % PERIOD;
  return Array.from({ length: numLEDs }, (_, i) => (i + offset) % PERIOD === 0 ? fg : bg);
}

export function buildDashedMarquee(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const DASH = 4; const GAP = 3; // 4 on, 3 off
  const PERIOD = DASH + GAP;
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * PERIOD) % PERIOD;
  return Array.from({ length: numLEDs }, (_, i) => (i + offset) % PERIOD < DASH ? fg : bg);
}

export function buildBarberPole(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const STRIPE = Math.max(2, Math.floor(numLEDs / 6));
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * STRIPE * 2) % (STRIPE * 2);
  return Array.from({ length: numLEDs }, (_, i) => {
    // Diagonal stripe: position + offset mod period
    return (i + offset) % (STRIPE * 2) < STRIPE ? fg : bg;
  });
}

export function buildBoldStripes(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const STRIPE = Math.max(3, Math.floor(numLEDs / 5));
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * STRIPE * 2) % (STRIPE * 2); // scroll one full period
  return Array.from({ length: numLEDs }, (_, i) =>
    Math.floor((i + offset) / STRIPE) % 2 === 0 ? fg : bg
  );
}

// ---------------------------------------------
// Group D: Math Waves (IDs 15-19)
// ---------------------------------------------


export function buildSinePulseWave(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const wave = Math.sin((i / numLEDs) * Math.PI * 4 + phase * Math.PI * 2) * 0.5 + 0.5;
    return blendRGB(fg, bg, wave);
  });
}

export function buildWavePinch(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? 1 - tick : tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const posNorm = i / (numLEDs - 1);
    const waveL = Math.sin(posNorm * Math.PI * 3 + phase * Math.PI * 2);
    const waveR = Math.sin((1 - posNorm) * Math.PI * 3 + phase * Math.PI * 2);
    const combined = (waveL + waveR) / 2 * 0.5 + 0.5;
    return blendRGB(fg, bg, combined);
  });
}

export function buildBreathingWave(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const wave = Math.sin((i / numLEDs) * Math.PI * 2 + phase * Math.PI * 2) * 0.5 + 0.5;
    return blendRGB(fg, bg, wave);
  });
}

export function buildCenterOutComet(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const center = Math.floor(numLEDs / 2);
  const TAIL = Math.floor(numLEDs * 0.15);
  const head = Math.floor(tick * (center + TAIL));
  return Array.from({ length: numLEDs }, (_, i) => {
    // Mirror: two comets going from center to edges
    const distL = head - (center - i);
    const distR = head - (i - center);
    const dist = Math.min(Math.abs(distL), Math.abs(distR));
    if (i < center) {
      return (head - (center - i) >= 0 && head - (center - i) < TAIL)
        ? blendRGB(fg, bg, 1 - dist / TAIL)
        : bg;
    } else {
      return (head - (i - center) >= 0 && head - (i - center) < TAIL)
        ? blendRGB(fg, bg, 1 - dist / TAIL)
        : bg;
    }
  });
}

export function buildCenterOutMarquee(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const center = Math.floor(numLEDs / 2);
  const PERIOD = 6;
  const offset = Math.floor(tick * PERIOD) % PERIOD;
  return Array.from({ length: numLEDs }, (_, i) => {
    const distFromCenter = Math.abs(i - center);
    return (distFromCenter + offset) % PERIOD < 3 ? fg : bg;
  });
}

