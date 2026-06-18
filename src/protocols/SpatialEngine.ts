/**
 * SK8Lytz Spatial Engine (Orchestrator)
 *
 * Thin orchestrator: dispatches pattern IDs to builder functions via generateArray.
 * All builder functions live in ../spatial/effectProcessors.ts.
 * All color math lives in ../shared/spatialMath.ts.
 * Shared types (RGB, PatternId, PatternOptions) live in ../shared/engineTypes.ts.
 *
 * CIRCULAR DEP RESOLUTION (Phase 1):
 *   Before: SpatialEngine imported from PatternEngine (RGB, PatternId, PatternOptions)
 *           PatternEngine imported from SpatialEngine (getPatternTransitionType, getHardwarePixelArray)
 *           → CYCLE
 *   After:  SpatialEngine imports from shared/engineTypes (no engine-to-engine import)
 *           PatternEngine imports from shared/engineTypes (same source, no cycle)
 *           → RESOLVED
 *
 * BYTE INVARIANT: All BLE payload values are byte-for-byte identical to the original.
 * Cited: docs/ZENGGE_PROTOCOL_BIBLE.md — commandType byte table (0x01/0x02/0x03/0x04/0x05/0x06)
 */

import type { RGB, PatternId, PatternOptions } from './shared/engineTypes';

// Re-export types for backwards compatibility — consumers that imported from SpatialEngine continue to work
export type { RGB, PatternId, PatternOptions };


// Re-export math helpers for backwards compatibility
export { dim, lerpRGB, hueToRGB, blendRGB, hsvToRgb } from './shared/spatialMath';
// Direct import for use inside generateArray inline cases
import { blendRGB, hsvToRgb } from './shared/spatialMath';

// Import all builder functions from effectProcessors
import {
  buildNativeBreathe,
  buildNativeSweep,
  buildNativeCenterOut,
  buildColorFlow,
  buildColorBreathing,
  buildColorJump,
  buildRunningWater,
  buildStrobe,
  buildColorWipe,
  buildFireworks,
  buildOceanWave,
  buildLightning,
  buildSnowfall,
  buildCandle,
  buildHeartbeat,
  buildMeteor,
  buildAurora,
  buildLava,
  buildPlasma,
  buildStarCluster,
  buildPoliceLights,
  buildRainbowBreathing,
  buildColorBurst,
  buildTwinkle,
  buildCrystalShimmer,
  buildGradientChase,
  buildCometDuo,
  buildFireFlame,
  buildCyberGlitch,
  buildNeonPulse,
  buildRainbowChaser,
  buildMatrixRain,
  buildSparkleFade,
  buildDualScan,
  buildStarlight,
  buildHyperspace,
  buildSolid,
  buildSplitColors,
  buildTrisection,
  buildQuartered,
  buildCenterAccent,
  buildSingleDotChase,
  buildTwinDotChase,
  buildCometChase,
  buildMeteorShower,
  buildMicroAnts,
  buildTheaterChase,
  buildDashedMarquee,
  buildBarberPole,
  buildBoldStripes,
  buildSinePulseWave,
  buildWavePinch,
  buildBreathingWave,
  buildCenterOutComet,
  buildCenterOutMarquee,
  buildSmoothBreath,
  buildHardJumpFlash,
  buildTemporalStrobe,
  buildWipeFill,
  buildWipeCenterOut,
  buildTrueRainbowFlow,
  buildRainbowMarquee,
  buildRainbowComet,
  buildCyberpunkShift,
  buildStreetMode,
  buildLargeChunkScroll,
  buildGradientChunk,
  buildPingPongFill,
  buildPingPongMarquee,
  buildRandomStrobe,
  buildStaticPartialRainbow,
  buildTetrisStacker,
  buildAlternatingComet,
  buildPingPongCenterFill,
  buildCustomArrayScroll,
  buildGlitchMarquee,
} from './spatial/effectProcessors';

// Re-export all builders for backwards compatibility (consumers importing from SpatialEngine continue to work)
export {
  buildNativeBreathe,
  buildNativeSweep,
  buildNativeCenterOut,
  buildColorFlow,
  buildColorBreathing,
  buildColorJump,
  buildRunningWater,
  buildStrobe,
  buildColorWipe,
  buildFireworks,
  buildOceanWave,
  buildLightning,
  buildSnowfall,
  buildCandle,
  buildHeartbeat,
  buildMeteor,
  buildAurora,
  buildLava,
  buildPlasma,
  buildStarCluster,
  buildPoliceLights,
  buildRainbowBreathing,
  buildColorBurst,
  buildTwinkle,
  buildCrystalShimmer,
  buildGradientChase,
  buildCometDuo,
  buildFireFlame,
  buildCyberGlitch,
  buildNeonPulse,
  buildRainbowChaser,
  buildMatrixRain,
  buildSparkleFade,
  buildDualScan,
  buildStarlight,
  buildHyperspace,
  buildSolid,
  buildSplitColors,
  buildTrisection,
  buildQuartered,
  buildCenterAccent,
  buildSingleDotChase,
  buildTwinDotChase,
  buildCometChase,
  buildMeteorShower,
  buildMicroAnts,
  buildTheaterChase,
  buildDashedMarquee,
  buildBarberPole,
  buildBoldStripes,
  buildSinePulseWave,
  buildWavePinch,
  buildBreathingWave,
  buildCenterOutComet,
  buildCenterOutMarquee,
  buildSmoothBreath,
  buildHardJumpFlash,
  buildTemporalStrobe,
  buildWipeFill,
  buildWipeCenterOut,
  buildTrueRainbowFlow,
  buildRainbowMarquee,
  buildRainbowComet,
  buildCyberpunkShift,
  buildStreetMode,
  buildLargeChunkScroll,
  buildGradientChunk,
  buildPingPongFill,
  buildPingPongMarquee,
  buildRandomStrobe,
  buildStaticPartialRainbow,
  buildTetrisStacker,
  buildAlternatingComet,
  buildPingPongCenterFill,
  buildCustomArrayScroll,
  buildGlitchMarquee,
};


/**
 * Generate a full pixel array for the given pattern ID at the given animation tick.
 *
 * This is the MAIN DISPATCH function. All 61 patterns + hardware parity modes (201-233)
 * + Street Modes (101-105) route through here.
 */
export function generateArray(patternId: PatternId, fg: RGB, bg: RGB, n: number, tick: number = 0, direction: 0 | 1 = 1, options?: PatternOptions): RGB[] {
  const _arr: RGB[] = Array(n).fill(bg);

  switch (patternId) {
    // ── GROUP 9: NATIVE TEMPORAL ──
    case 70: return buildNativeBreathe(fg, bg, n, tick);
    case 71: return buildNativeSweep(fg, bg, n, tick, direction);
    case 72: return buildNativeCenterOut(fg, bg, n, tick);

    // ── GROUP 1: SOLID & STATIC ──
    case 1: return buildSolid(fg, n);
    case 2: return buildSplitColors(fg, bg, n);
    case 3: return buildTrisection(fg, bg, n);
    case 4: return buildQuartered(fg, bg, n);
    case 5: return buildCenterAccent(fg, bg, n);

    // ── GROUP 2: CHASES & METEORS ──
    case 6: return buildSingleDotChase(fg, bg, n, tick, direction);
    case 7: return buildTwinDotChase(fg, bg, n, tick, direction);
    case 8: return buildCometChase(fg, bg, n, tick, direction);
    case 9: return buildMeteorShower(fg, bg, n, tick, direction);

    // ── GROUP 3: MARQUEES & BANDS ──
    case 10: return buildMicroAnts(fg, bg, n, tick, direction);
    case 11: return buildTheaterChase(fg, bg, n, tick, direction);
    case 12: return buildDashedMarquee(fg, bg, n, tick, direction);
    case 13: return buildBoldStripes(fg, bg, n, tick, direction);     // ID 13 = BoldStripes (BarberPole was removed)

    // ── GROUP 4: MATH WAVES ──
    case 14: return buildSinePulseWave(fg, bg, n, tick, direction);   // was 15
    case 15: return buildWavePinch(fg, bg, n, tick, direction);                  // was 16
    case 16: return buildBreathingWave(fg, bg, n, tick, direction);   // was 17

    // ── GROUP 5a: TEMPORAL FULL-STRIP (0x51) ──
    case 17: return buildSmoothBreath(fg, n, tick);                   // was 20

    // ── GROUP 5b: WIPE / FILL ──
    case 18: return buildWipeFill(fg, bg, n, tick, direction);        // was 23

    // ── GROUP 6: GENERATIVE RAINBOWS ──
    case 19: return buildTrueRainbowFlow(n, tick, direction);         // was 25
    case 20: return buildRainbowMarquee(n, tick, direction);          // was 26
    case 21: return buildRainbowComet(n, tick, direction);            // was 27
    case 22: return buildCyberpunkShift(fg, bg, n, tick, direction);  // was 28

    // ── GROUP 7: ge.* PHASE 1A REVERSALS ──
    case 23: return buildColorFlow(n, tick, direction);               // ge.ColorFlowEffect — was 29
    case 24: return buildColorBreathing(fg, n, tick);                 // ge.BreathEffect    — was 30
    case 25: return buildRunningWater(fg, bg, n, tick, direction);    // ge.RunningWaterEffect — was 32
    case 26: return buildStrobe(fg, n, tick);                         // ge.StrobeEffect    — was 33
    case 27: return buildOceanWave(fg, bg, n, tick, direction);       // ge.OceanWaveEffect — was 36
    case 28: return buildLightning(fg, n, tick);                      // ge.LightningEffect — was 37
    case 29: return buildSnowfall(fg, bg, n, tick, direction);                   // ge.SnowfallEffect  — was 38
    case 30: return buildHeartbeat(fg, n, tick);                      // ge.HeartbeatEffect — was 40
    case 31: return buildMeteor(fg, bg, n, tick, direction);          // ge.MeteorEffect    — was 41
    case 32: return buildAurora(n, tick, direction);                  // ge.AuroraEffect    — was 42
    case 33: return buildLava(fg, bg, n, tick, direction);                       // ge.LavaEffect      — was 43
    case 34: return buildPlasma(fg, bg, n, tick, direction);          // ge.PlasmaEffect    — was 44
    case 35: return buildStarCluster(fg, bg, n, tick, direction);               // ge.StarClusterEffect — was 45
    case 36: return buildRainbowBreathing(n, tick);                   // was 47
    case 37: return buildCrystalShimmer(n, tick, direction);                    // was 50
    case 38: return buildGradientChase(fg, bg, n, tick, direction);   // was 51
    case 39: return buildFireFlame(fg, bg, n, tick, direction);                  // was 53
    case 40: return buildNeonPulse(fg, bg, n, tick);                  // was 55
    case 41: return buildRainbowChaser(n, tick, direction);           // was 56
    case 42: return buildMatrixRain(fg, bg, n, tick, direction);      // was 57
    case 43: return buildStarlight(fg, bg, n, tick, direction);                  // was 60

    // ── 0x41 Hardware Parity (Modes 201-233) ──
    case 201: {
      // Large FG chunk scrolling back to front (~50% width) | FG only (BG is hardcoded White)
      const offset = Math.floor(tick * n);
      const chunk = Math.max(1, Math.floor(n / 2));
      return Array.from({ length: n }, (_, i) => {
        const pos = direction === 1 ? (i + offset) % n : (i - offset + n) % n;
        return pos < chunk ? fg : { r: 255, g: 255, b: 255 };
      });
    }
    case 202: {
      // Scrolling chunk (FG tips, BG center) over Black background
      const offset = Math.floor(tick * n);
      const chunk = Math.max(1, Math.floor(n / 2));
      return Array.from({ length: n }, (_, i) => {
        const pos = direction === 1 ? (i + offset) % n : (i - offset + n) % n;
        if (pos >= chunk) return { r: 0, g: 0, b: 0 }; 
        const t = Math.abs((pos / (chunk - 1)) * 2 - 1); 
        return blendRGB(fg, bg, t);
      });
    }
    case 203: {
      return buildSingleDotChase(fg, bg, n, tick, direction);
    }
    case 204: {
      // Alternating Bounce Fill (FG fills Fwd, BG fills Rev)
      const isFwd = tick < 0.5;
      const fillLevel = isFwd ? tick * 2 : (1 - tick) * 2;
      const cutoff = Math.floor(fillLevel * n);
      return Array.from({ length: n }, (_, i) => {
        if (isFwd) return i <= cutoff ? fg : bg;
        return i >= n - 1 - cutoff ? bg : fg;
      });
    }
    case 205: {
      // Single FG dot bouncing back and forth over solid BG
      const maxPos = n - 1;
      const pos = Math.floor((tick < 0.5 ? tick * 2 : (1 - tick) * 2) * maxPos);
      return Array.from({ length: n }, (_, i) => (i === pos ? fg : bg));
    }
    case 206: {
      // Marching Ants (Alternating FG/BG pixels flowing forward)
      const offset = Math.floor(tick * n * 2); 
      return Array.from({ length: n }, (_, i) => ((i + offset) % 2 === 0 ? fg : bg));
    }
    case 207: {
      // Breathing (Fade In/Out) | FG only
      return buildNativeBreathe(fg, { r: 0, g: 0, b: 0 }, n, tick);
    }
    case 208: {
      // 3-Color RGB Breathing Cycle (R -> G -> B)
      const colors = [{r:255,g:0,b:0}, {r:0,g:255,b:0}, {r:0,g:0,b:255}];
      const c = colors[Math.floor(tick * 3) % 3];
      const subTick = (tick * 3) % 1;
      const brightness = Math.sin(subTick * Math.PI) ** 2;
      return Array(n).fill({ r: Math.round(c.r * brightness), g: Math.round(c.g * brightness), b: Math.round(c.b * brightness) });
    }
    case 209: {
      return buildRainbowBreathing(n, tick);
    }
    case 210: {
      // 3-Color RGB Strobe (Jump)
      const colors = [{r:255,g:0,b:0}, {r:0,g:255,b:0}, {r:0,g:0,b:255}];
      const phase = (tick * 6) % 1; 
      const isOn = phase < 0.5;
      const c = colors[Math.floor(tick * 3) % 3];
      return Array(n).fill(isOn ? c : {r:0,g:0,b:0});
    }
    case 211: {
      // 7-Color Breathing Cycle
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const c = colors[Math.floor(tick * 7) % 7];
      const subTick = (tick * 7) % 1;
      const brightness = Math.sin(subTick * Math.PI) ** 2;
      return Array(n).fill({ r: Math.round(c.r * brightness), g: Math.round(c.g * brightness), b: Math.round(c.b * brightness) });
    }
    case 212: {
      // 7-Color Crossfade
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const idx = Math.floor(tick * 7) % 7;
      const nextIdx = (idx + 1) % 7;
      const t = (tick * 7) % 1;
      return Array(n).fill(blendRGB(colors[nextIdx], colors[idx], t)); 
    }
    case 213: {
      // 7-Color Hard Jump
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      return Array(n).fill(colors[Math.floor(tick * 7) % 7]);
    }
    case 214: {
      // Irregular Flashing Strobe (Jumps between colors) | FG + BG
      const isFlash = (Math.sin(tick * 50) * 43758) % 1 > 0.5;
      const isBg = (Math.cos(tick * 30) * 12345) % 1 > 0.5;
      return Array(n).fill(isFlash ? (isBg ? bg : fg) : {r:0,g:0,b:0});
    }
    case 215: {
      // 3-Color RGB Strobe | Hardcoded
      const colors = [{r:255,g:0,b:0}, {r:0,g:255,b:0}, {r:0,g:0,b:255}];
      const isOn = (tick * 10) % 1 < 0.5;
      return Array(n).fill(isOn ? colors[Math.floor(tick * 3) % 3] : {r:0,g:0,b:0});
    }
    case 216: {
      // 7-Color Strobe | Hardcoded
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const isOn = (tick * 14) % 1 < 0.5;
      return Array(n).fill(isOn ? colors[Math.floor(tick * 7) % 7] : {r:0,g:0,b:0});
    }
    case 217:
    case 218: {
      return buildCometChase(fg, bg, n, tick, direction);
    }
    case 219: {
      return buildSingleDotChase(fg, bg, n, (tick * 2) % 1, direction);
    }
    case 220: {
      // Static Partial Rainbow Gradient (Blue ends, rainbow center)
      return Array.from({ length: n }, (_, i) => {
        const t = Math.abs((i / (n - 1)) * 2 - 1); 
        const hue = 0.66 * t; 
        return hsvToRgb(hue, 1.0, 1.0);
      });
    }
    case 221: {
      // 7-Color Multi-Comet Flow (Train of fading chunks)
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const COMET_LEN = Math.min(n, 8); // Bible: fixed 8px comet chunks for ID 221
      const totalOffset = tick * n * 7;
      const offset = Math.floor(totalOffset) % n;
      const loopCount = Math.floor(totalOffset / n);
      return Array.from({ length: n }, (_, i) => {
        const pos = (i + offset) % n;
        const cometIdx = Math.floor(pos / COMET_LEN);
        const wrapped = (i + offset) >= n ? 1 : 0;
        const c = colors[(cometIdx + loopCount + wrapped) % 7];
        const dist = pos % COMET_LEN;
        const brightness = dist / (COMET_LEN - 1);
        return blendRGB(c, {r:0,g:0,b:0}, brightness);
      });
    }
    case 222:
    case 223: {
      // 7-Color Sweep / Wipe (Sequential fills from back to front)
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const colorPhase = Math.floor(tick * 7);
      const activeColor = colors[colorPhase % 7];
      const prevColor = colors[(colorPhase + 6) % 7];
      const wipeProgress = (tick * 7) % 1;
      const fillCount = Math.floor(wipeProgress * n);
      return Array.from({ length: n }, (_, i) => i <= fillCount ? activeColor : prevColor);
    }
    case 224: {
      // 7-Color Stacker / Building Blocks (Drops one full color stack, then next)
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const colorPhase = Math.floor(tick * 7);
      const activeColor = colors[colorPhase % 7];
      const prevColor = colors[(colorPhase + 6) % 7];
      const stackProgress = (tick * 7) % 1;
      const stackLevel = Math.floor(stackProgress * n); 
      const dropPos = Math.floor(((stackProgress * n) % 1) * (n - stackLevel)); 
      return Array.from({ length: n }, (_, i) => {
        if (i >= n - stackLevel) return activeColor; 
        if (i === dropPos) return activeColor; 
        return prevColor; // Background is the previous stacked color
      });
    }
    case 225: {
      // Alternating Fading Chunks (FG chunk then BG chunk flowing forward)
      const CHUNK = Math.max(3, Math.floor(n / 4));
      const offset = Math.floor(tick * n);
      return Array.from({ length: n }, (_, i) => {
        const pos = (i + offset) % n;
        const chunkIdx = Math.floor(pos / CHUNK);
        const activeColor = chunkIdx % 2 === 0 ? fg : bg;
        const dist = pos % CHUNK;
        const brightness = dist / (CHUNK - 1);
        return blendRGB(activeColor, {r:0,g:0,b:0}, brightness);
      });
    }
    case 226: {
      // 7-Color Center-In Fill (Fills from edges to center, collapses, changes color)
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const colorPhase = Math.floor(tick * 7);
      const c = colors[colorPhase % 7];
      const prevColor = colors[(colorPhase + 6) % 7];
      const progress = (tick * 7) % 1;
      const fillLen = Math.floor(progress * (n / 2));
      return Array.from({ length: n }, (_, i) => {
        const distFromEdge = Math.min(i, n - 1 - i);
        return distFromEdge <= fillLen ? c : prevColor;
      });
    }
    case 227: {
      // 7-Color Large Multi-Comet Flow
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const COMET_LEN = Math.max(5, Math.floor(n * 0.3)); // Bible: ~30% large comet chunks for ID 227
      const totalOffset = tick * n * 7;
      const offset = Math.floor(totalOffset) % n;
      const loopCount = Math.floor(totalOffset / n);
      return Array.from({ length: n }, (_, i) => {
        const pos = (i + offset) % n;
        const cometIdx = Math.floor(pos / COMET_LEN);
        const wrapped = (i + offset) >= n ? 1 : 0;
        const c = colors[(cometIdx + loopCount + wrapped) % 7];
        const dist = pos % COMET_LEN;
        const brightness = dist / (COMET_LEN - 1);
        return blendRGB(c, {r:0,g:0,b:0}, brightness);
      });
    }
    case 228: {
      // Flowing Fire Effect (White -> Yellow -> Orange -> Red gradient flow)
      const colors = [
        {r:255,g:255,b:255}, 
        {r:255,g:255,b:0},   
        {r:255,g:100,b:0},   
        {r:255,g:0,b:0}      
      ];
      const offset = tick * 4;
      return Array.from({ length: n }, (_, i) => {
        const t = (i / n * 4 - offset) % 4;
        const p = t < 0 ? t + 4 : t;
        const idx1 = Math.floor(p);
        const idx2 = (idx1 + 1) % 4;
        return blendRGB(colors[idx2], colors[idx1], p % 1);
      });
    }
    case 229: {
      // 7-Color Large Block Flow (50% block of color flows from back to front)
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const BLOCK = Math.floor(n / 2);
      const totalOffset = tick * n * 7;
      const offset = Math.floor(totalOffset) % n;
      const loopCount = Math.floor(totalOffset / n);
      return Array.from({ length: n }, (_, i) => {
        const pos = (i + offset) % n;
        const isBlock = pos < BLOCK;
        const wrapped = (i + offset) >= n ? 1 : 0;
        const colorIdx = (loopCount + wrapped) % 7;
        return isBlock ? colors[colorIdx] : {r:0,g:0,b:0};
      });
    }
    case 230: {
      // 7-Color Alternating Center Fill (Fills Center-Out, then fills Edges-In)
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const colorPhase = Math.floor(tick * 7);
      const c = colors[colorPhase % 7];
      const prevColor = colors[(colorPhase + 6) % 7];
      const subTick = (tick * 7) % 1;
      const isCenterOut = subTick < 0.5;
      const progress = isCenterOut ? subTick * 2 : (subTick - 0.5) * 2;
      const fillLen = Math.floor(progress * (n / 2));
      return Array.from({ length: n }, (_, i) => {
        const distFromCenter = Math.abs(i - Math.floor(n / 2));
        const distFromEdge = Math.min(i, n - 1 - i);
        if (isCenterOut) return distFromCenter <= fillLen ? c : prevColor;
        else return distFromEdge <= fillLen ? c : prevColor;
      });
    }
    case 231: {
      // Custom Marquee Chunk ([FG, BG, Black, BG, FG] scrolling forward over BG)
      const offset = Math.floor(tick * n);
      return Array.from({ length: n }, (_, i) => {
        const pos = (i + offset) % n;
        if (pos === 0) return fg;
        if (pos === 1) return bg;
        if (pos === 2) return {r:0,g:0,b:0};
        if (pos === 3) return bg;
        if (pos === 4) return fg;
        return bg;
      });
    }
    case 232: {
      // Glitch / Twitch Marquee (Alternates small ants and chunky twitches)
      const isTwitch = (Math.sin(tick * 15) * 43758) % 1 > 0.5;
      const offset = Math.floor(tick * n * (isTwitch ? 3 : 1));
      return Array.from({ length: n }, (_, i) => {
        const pos = (i + offset) % n;
        if (isTwitch) return pos % 8 < 4 ? fg : bg;
        return pos % 2 === 0 ? fg : bg;
      });
    }
    case 233: {
      // 7-Color Dot Stream over custom BG (Dots separated by BG spaces)
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const totalOffset = tick * n * 7;
      const offset = Math.floor(totalOffset) % n;
      const loopCount = Math.floor(totalOffset / n);
      return Array.from({ length: n }, (_, i) => {
        const pos = (i + offset) % n;
        if (pos % 3 !== 0) return bg;
        const wrapped = (i + offset) >= n ? 1 : 0;
        const colorIdx = (loopCount + wrapped) % 7;
        return colors[colorIdx];
      });
    }

    // ── GROUP 8: STREET MODES ──
    case 101:
    case 102:
    case 103:
    case 104:
    case 105:
      return buildStreetMode(patternId, fg, bg, n, tick, options);

    default:
      return Array(n).fill(fg);
  }
}


/**
 * Get the initial hardware seed pixel array for a pattern.
 * Applies tick heuristics to avoid all-black seed frames for strobe/breathe patterns.
 *
 * Cited: docs/ZENGGE_PROTOCOL_BIBLE.md — §5 Pattern Dispatch (tick=0.33 default,
 * tick=0.0 for strobe/jump IDs, tick=0.25 for breathe IDs)
 */
export function getHardwarePixelArray(
  patternId: PatternId,
  fg: RGB,
  bg: RGB,
  numLEDs: number,
  options?: PatternOptions
): RGB[] | null {
  // All 61 patterns now use 0x59. commandType selects the hardware behavior:
  //   IDs 20-22, 30-31, 33, 37, 39-40, 46-47, 54-55 use Breathe/Jump/Strobe commandTypes.
  //   The pixel array is the color seed; hardware animates it autonomously.
  //
  // Use tick=0.33 for most patterns so the initial hardware seed frame is visually rich.
  // EXCEPTION: Jump (0x04) and Strobe (0x03) patterns, plus some Breathe patterns
  // These builders include an isOn phase-gate that can return all-black at tick=0.33.
  // Since hardware handles flash/jump autonomously once the array is received,
  // we need a fully-lit seed. We use tick=0.0 for strobes/jumps, and tick=0.25 for breathe.
  const ZERO_TICK_IDS = new Set([26, 28, 31, 46, 54]); // Strobe/Jump
  const QUARTER_TICK_IDS = new Set([24, 30, 36, 40]); // Breathe (sin wave peak at PI/2)
  const seedTick = ZERO_TICK_IDS.has(patternId) ? 0.0 : (QUARTER_TICK_IDS.has(patternId) ? 0.25 : 0.33);
  return generateArray(patternId, fg, bg, Math.max(1, numLEDs), seedTick, 1, options);
}

/**
 * Get the 0x59 transition (commandType) byte for a pattern.
 *
 * SINGLE SOURCE OF TRUTH: ZENGGE_PROTOCOL_BIBLE.md
 * 0x01=Static, 0x02=Running, 0x03=Strobe, 0x04=Jump, 0x05=Breathe, 0x06=Twinkle
 *
 * Previous bug: was returning 0x00 for animated patterns. 0x00 is NOT a valid
 * commandType — it maps to nothing. Hardware received undefined byte → no animation.
 */
export function getPatternTransitionType(patternId: PatternId): number {
  // ── GROUP 1: STATIC (IDs 1–5) ──────────────────────────────────────────────
  if (patternId >= 1 && patternId <= 5) return 0x01; // Static — hardware freezes array

  // ── GROUP 5a: HARDWARE TEMPORAL (IDs 20–22) ────────────────────────────────
  if (patternId === 17) return 0x05; // Breathe  — hardware pulses brightness

  // ── GROUP 7: ge.* BREATHE EFFECTS (IDs 30, 39, 40, 47, 55) ───────────────
  // Hardware receives the pixel array and pulses its brightness autonomously.
  // Visualizer uses buildColorBreathing / buildSmoothBreath / buildRainbowBreathing / buildNeonPulse at animTick.
  if (patternId === 24) return 0x05; // Color Breathing   — FG_ONLY
  if (patternId === 30) return 0x05; // Heartbeat Pulse   — FG_ONLY (breathe pulse)
  if (patternId === 36) return 0x05; // Rainbow Breathing — GENERATIVE
  if (patternId === 40) return 0x05; // Neon Pulse        — FG_BG (NeonPulse math uses buildNeonPulse)

  // ── GROUP 7: ge.* JUMP EFFECTS (IDs 31, 46, 54) ──────────────────────────
  // Hardware receives the pixel array and hard-cuts states autonomously.
  // Visualizer uses buildColorJump / buildPoliceLights / buildCyberGlitch at animTick.

  // ── GROUP 7: ge.* STROBE EFFECTS (IDs 33, 37) ────────────────────────────
  // Hardware receives the pixel array and flashes it on/off autonomously.
  // Visualizer uses buildStrobe / buildLightning at animTick.
  if (patternId === 26) return 0x03; // Strobe Flash      — FG_ONLY
  if (patternId === 28) return 0x03; // Lightning Strike  — FG_ONLY (random flash)

  // ── ALL OTHER ge.* SCROLL EFFECTS (IDs 29, 32, 34–36, 38, 41–45, 48–53, 56–61) ──
  // Hardware scrolls the pre-computed pixel array continuously via 0x02 Running.
  // NOTE: Twinkle-style patterns (35, 45, 49, 50, 58, 60) intentionally use 0x02 (Running)
  // because 0x06 (Twinkle) hardware support is incomplete.
  
  // ── GROUP 8: STREET MODES (101-105) ──────────────────────────────────────
  if (patternId === 101 || patternId === 104) return 0x01; // STOPPED/SLOWING — STATIC
  if (patternId === 103) return 0x02; // HARD BRAKING — FLASH/STROBE
  // CRUISING (102) & ACCELERATING (105) use 0x01 because the hardware cannot 
  // autonomously bounce the dot. It is just a static array we update periodically.
  if (patternId >= 100) return 0x01; 

  return 0x02; // Running — continuous hardware scroll
}

