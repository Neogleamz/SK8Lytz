import { useEffect, useState } from 'react';
import { getDefaultProtocol } from '../protocols/ControllerRegistry';
import { AppLogger } from '../services/AppLogger';

export type ProtocolType = '0x51' | '0x59' | '0x61' | '0x73' | '0x62';

export interface BldResult {
  raw: number[];
  wrapped: number[];
  hex: string;
  annotations: string[];
}

const TRANSITION_TYPES = [
  { byte: 0x00, label: 'CASCADE' },
  { byte: 0x01, label: 'FREEZE' },
  { byte: 0x02, label: 'STROBE' },
  { byte: 0x03, label: 'TRIGGER' },
];

/**
 * Safely parse a number from a string or number input, avoiding NaN.
 */
const safeParseInt = (val: any, fallback: number = 0): number => {
  const parsed = typeof val === 'string' ? parseInt(val, 10) : val;
  return isNaN(parsed) ? fallback : parsed;
};

function build0x59(
  pixels: {r:number;g:number;b:number}[],
  transitionType: number,
  speed: number,
  direction: number
): BldResult {
  const adapter = getDefaultProtocol();
  const result = adapter.buildMultiColor(pixels, pixels.length, speed, direction, transitionType);
  const raw = result.packets[0] || [];
  const wrapped = raw; // adapter already returns fully wrapped packets
  const hex = wrapped.map(b => b.toString(16).toUpperCase().padStart(2, '0')).join(' ');

  const numPoints = pixels.length;
  const annotations = [
    `[0x59] Opcode (via adapter.buildMultiColor)`,
    `[${numPoints} LEDs, ${numPoints * 3} pixel bytes]`,
    `transitionType=${transitionType} (${TRANSITION_TYPES.find(t=>t.byte===transitionType)?.label ?? 'UNKNOWN'})`,
    `speed=${speed}`,
    `direction=${direction}`,
  ];

  return { raw, wrapped, hex, annotations };
}

export const useProtocolBuilder = (_hwPts: number = 16) => {
  const [bldProtocol, setBldProtocol] = useState<ProtocolType>('0x59');
  
  // Builder 0x59
  const [bldColors, setBldColors] = useState<{r:number,g:number,b:number}[]>([{r:255, g:0, b:0}]);
  const [bldTrans, setBldTrans] = useState(0x01);
  const [bldSpeed, setBldSpeed] = useState(16);
  const [bldPoints, setBldPoints] = useState('16');
  const [bldDir, setBldDir] = useState(1);
  
  // Builder 0x61
  const [bldPatternId, setBldPatternId] = useState('1');
  const [bldBright, setBldBright] = useState('100');

  // Builder 0x51 Custom Mode
  const [bld51Mode, setBld51Mode] = useState('1');
  const [bld51Speed, setBld51Speed] = useState('16');
  const [bld51Color1, setBld51Color1] = useState({ r:255, g:0, b:0 });
  const [bld51Color2, setBld51Color2] = useState({ r:0, g:255, b:0 });
  const [bld51Dir, setBld51Dir] = useState(1);
  const [bld51Seg, setBld51Seg] = useState(false);

  // Builder 0x73
  const [bldMic, setBldMic] = useState(true);
  const [bldMusicMode, setBldMusicMode] = useState('1');
  const [bldSens, setBldSens] = useState('100');
  const [bldC2, setBldC2] = useState({r:0, g:0, b:255});
  const [bldMusicIsOn, setBldMusicIsOn] = useState(true);
  const [bldMatrixStyle, setBldMatrixStyle] = useState<0x26 | 0x27>(0x27);

  // Builder 0x62
  const [bldIc, setBldIc] = useState('WS2812B');
  const [bldOrder, setBldOrder] = useState('RGB');
  const [bldSegs, setBldSegs] = useState('1');

  const [bldResult, setBldResult] = useState<BldResult | null>(null);

  useEffect(() => {
    const adapter = getDefaultProtocol();
    try {
      if (bldProtocol === '0x59') {
        const pts = Math.max(1, Math.min(300, safeParseInt(bldPoints, 16)));
        const pixels = Array(pts).fill(0).map((_, i) => bldColors[i % bldColors.length]);
        setBldResult(build0x59(pixels, bldTrans, bldSpeed, bldDir));
      } else if (bldProtocol === '0x61') {
        const id = safeParseInt(bldPatternId, 1);
        const spd = Math.max(1, bldSpeed);
        const br = Math.max(0, Math.min(100, safeParseInt(bldBright, 100)));
        const wrapped = adapter.buildEffect(id, spd, br).packets[0];
        setBldResult({ raw: wrapped, wrapped, hex: wrapped.map(b=>b.toString(16).toUpperCase().padStart(2,'0')).join(' '), annotations: ['[0x61] RBM Pattern Payload', `Pattern: ${id}`, `Speed: ${spd}`, `Brightness: ${br}`] });
      } else if (bldProtocol === '0x51') {
        const mode = safeParseInt(bld51Mode, 1);
        const spd = Math.max(1, Math.min(100, safeParseInt(bld51Speed, 16)));
        const wrapped = adapter.buildCustomMode([{
            mode, speed: spd, 
            color1: bld51Color1, color2: bld51Color2
        }]).packets[0];
        setBldResult({ raw: wrapped, wrapped, hex: wrapped.map(b=>b.toString(16).toUpperCase().padStart(2,'0')).join(' '), annotations: ['[0x51] DIY Mode Payload', `Effect ID: ${mode}`, `Speed: ${spd}`] });
      } else if (bldProtocol === '0x73') {
        const id = safeParseInt(bldMusicMode, 1);
        const c1 = bldColors[0] || {r:255,g:0,b:0};
        const s = safeParseInt(bldSens, 100);
        const br = safeParseInt(bldBright, 100);
        const wrapped = adapter.buildMusicConfig({ patternId: id, matrixStyle: bldMic ? 0x27 : 0x26, micSensitivity: s, brightness: br, color1: c1, color2: bldC2, speed: id }).packets[0];
        const matrixLabel = bldMatrixStyle === 0x27 ? 'Light Screen (0x27)' : 'Light Bar (0x26)';
        setBldResult({ raw: wrapped, wrapped, hex: wrapped.map(b=>b.toString(16).toUpperCase().padStart(2,'0')).join(' '), annotations: ['[0x73] Symphony/Music Config', `Mode: ${id} | Matrix: ${matrixLabel}`, `Mic: ${bldMic ? 'DEVICE' : 'APP'} | Sens: ${s} | Bright: ${br}`, `C1 RGB(${c1.r},${c1.g},${c1.b}) | C2 RGB(${bldC2.r},${bldC2.g},${bldC2.b})`] });
      } else if (bldProtocol === '0x62') {
        const pts = safeParseInt(bldPoints, 16);
        const seg = safeParseInt(bldSegs, 1);
        const wrapped = adapter.buildWriteSettingsByName(pts, seg, bldIc, bldOrder).packets[0];
        setBldResult({ raw: wrapped, wrapped, hex: wrapped.map(b=>b.toString(16).toUpperCase().padStart(2,'0')).join(' '), annotations: ['[0x62] EEPROM Write', `IC: ${bldIc} Order: ${bldOrder}`, `LEDs: ${pts} Seg: ${seg}`] });
      }
    } catch (e: unknown) { 
      AppLogger.error('[useProtocolBuilder] Build failed', e instanceof Error ? e.message : String(e), { payload_size: 0, ssi: 0 });
    }
  }, [bldProtocol, bldColors, bldTrans, bldSpeed, bldPoints, bldDir, bldPatternId, bldBright, bldMic, bldMusicMode, bldSens, bldC2, bldMatrixStyle, bldIc, bldOrder, bldSegs, bld51Mode, bld51Speed, bld51Color1, bld51Color2]);

  return {
    bldProtocol, setBldProtocol,
    bldColors, setBldColors,
    bldTrans, setBldTrans,
    bldSpeed, setBldSpeed,
    bldPoints, setBldPoints,
    bldDir, setBldDir,
    bldPatternId, setBldPatternId,
    bldBright, setBldBright,
    bld51Mode, setBld51Mode,
    bld51Speed, setBld51Speed,
    bld51Color1, setBld51Color1,
    bld51Color2, setBld51Color2,
    bld51Dir, setBld51Dir,
    bld51Seg, setBld51Seg,
    bldMic, setBldMic,
    bldMusicMode, setBldMusicMode,
    bldMusicIsOn, setBldMusicIsOn,
    bldSens, setBldSens,
    bldC2, setBldC2,
    bldMatrixStyle, setBldMatrixStyle,
    bldIc, setBldIc,
    bldOrder, setBldOrder,
    bldSegs, setBldSegs,
    bldResult,
  };
};

export type ProtocolBuilderContext = ReturnType<typeof useProtocolBuilder>;
