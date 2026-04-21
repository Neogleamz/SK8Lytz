import { useEffect, useState } from 'react';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
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
  const numPoints = pixels.length;
  const totalLen = numPoints * 3 + 9;

  const raw = new Array(totalLen).fill(0);
  raw[0] = 0x59;
  raw[1] = (totalLen >> 8) & 0xFF;
  raw[2] = totalLen & 0xFF;
  let idx = 3;
  for (const p of pixels) {
    raw[idx++] = Math.max(0, Math.min(255, p.r | 0));
    raw[idx++] = Math.max(0, Math.min(255, p.g | 0));
    raw[idx++] = Math.max(0, Math.min(255, p.b | 0));
  }
  raw[idx++] = (numPoints >> 8) & 0xFF;
  raw[idx++] = numPoints & 0xFF;
  raw[idx++] = transitionType & 0xFF;
  raw[idx++] = Math.max(1, Math.min(255, speed | 0));
  raw[idx++] = direction & 0xFF;
  raw[idx] = ZenggeProtocol.calculateChecksum(raw.slice(0, totalLen - 1));

  const wrapped = ZenggeProtocol.wrapCommand(raw);
  const hex = wrapped.map(b => b.toString(16).toUpperCase().padStart(2, '0')).join(' ');

  const annotations = [
    `[0x59] Opcode`,
    `[${raw[1].toString(16).toUpperCase().padStart(2,'0')} ${raw[2].toString(16).toUpperCase().padStart(2,'0')}] totalLen=${totalLen}`,
    `[... ${numPoints * 3} pixel bytes (${numPoints} LEDs)]`,
    `[${raw[totalLen-6].toString(16).toUpperCase().padStart(2,'0')} ${raw[totalLen-5].toString(16).toUpperCase().padStart(2,'0')}] numPoints=${numPoints}`,
    `[${raw[totalLen-4].toString(16).toUpperCase().padStart(2,'0')}] transitionType=${transitionType} (${TRANSITION_TYPES.find(t=>t.byte===transitionType)?.label ?? 'UNKNOWN'})`,
    `[${raw[totalLen-3].toString(16).toUpperCase().padStart(2,'0')}] speed=${speed}`,
    `[${raw[totalLen-2].toString(16).toUpperCase().padStart(2,'0')}] direction=${direction}`,
    `[${raw[totalLen-1].toString(16).toUpperCase().padStart(2,'0')}] checksum`,
  ];

  return { raw, wrapped, hex, annotations };
}

export const useProtocolBuilder = (hwPts: number = 16) => {
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
  const [bldMatrixStyle, setBldMatrixStyle] = useState<0x26 | 0x27>(0x27);

  // Builder 0x62
  const [bldIc, setBldIc] = useState('WS2812B');
  const [bldOrder, setBldOrder] = useState('RGB');
  const [bldSegs, setBldSegs] = useState('1');

  const [bldResult, setBldResult] = useState<BldResult | null>(null);

  useEffect(() => {
    try {
      if (bldProtocol === '0x59') {
        const pts = Math.max(1, Math.min(300, safeParseInt(bldPoints, 16)));
        const pixels = Array(pts).fill(0).map((_, i) => bldColors[i % bldColors.length]);
        setBldResult(build0x59(pixels, bldTrans, bldSpeed, bldDir));
      } else if (bldProtocol === '0x61') {
        const id = safeParseInt(bldPatternId, 1);
        const spd = Math.max(1, bldSpeed);
        const br = Math.max(0, Math.min(100, safeParseInt(bldBright, 100)));
        const wrapped = ZenggeProtocol.setCustomRbm(id, spd, br);
        setBldResult({ raw: wrapped, wrapped, hex: wrapped.map(b=>b.toString(16).toUpperCase().padStart(2,'0')).join(' '), annotations: ['[0x61] RBM Pattern Payload', `Pattern: ${id}`, `Speed: ${spd}`, `Brightness: ${br}`] });
      } else if (bldProtocol === '0x51') {
        const mode = safeParseInt(bld51Mode, 1);
        const spd = Math.max(1, Math.min(31, safeParseInt(bld51Speed, 16)));
        const wrapped = ZenggeProtocol.setCustomMode([{
            mode, speed: spd, 
            color1: bld51Color1, color2: bld51Color2
        }]);
        setBldResult({ raw: wrapped, wrapped, hex: wrapped.map(b=>b.toString(16).toUpperCase().padStart(2,'0')).join(' '), annotations: ['[0x51] DIY Mode Payload', `Effect ID: ${mode}`, `Speed: ${spd}`] });
      } else if (bldProtocol === '0x73') {
        const id = safeParseInt(bldMusicMode, 1);
        const c1 = bldColors[0] || {r:255,g:0,b:0};
        const s = safeParseInt(bldSens, 100);
        const br = safeParseInt(bldBright, 100);
        const wrapped = ZenggeProtocol.setMusicConfig(id, bldMic ? 0x27 : 0x26, true, c1, bldC2, s, br);
        const matrixLabel = bldMatrixStyle === 0x27 ? 'Light Screen (0x27)' : 'Light Bar (0x26)';
        setBldResult({ raw: wrapped, wrapped, hex: wrapped.map(b=>b.toString(16).toUpperCase().padStart(2,'0')).join(' '), annotations: ['[0x73] Symphony/Music Config', `Mode: ${id} | Matrix: ${matrixLabel}`, `Mic: ${bldMic ? 'DEVICE' : 'APP'} | Sens: ${s} | Bright: ${br}`, `C1 RGB(${c1.r},${c1.g},${c1.b}) | C2 RGB(${bldC2.r},${bldC2.g},${bldC2.b})`] });
      } else if (bldProtocol === '0x62') {
        const pts = safeParseInt(bldPoints, 16);
        const seg = safeParseInt(bldSegs, 1);
        const wrapped = ZenggeProtocol.writeHardwareSettingsByName(pts, seg, bldIc, bldOrder);
        setBldResult({ raw: wrapped, wrapped, hex: wrapped.map(b=>b.toString(16).toUpperCase().padStart(2,'0')).join(' '), annotations: ['[0x62] EEPROM Write', `IC: ${bldIc} Order: ${bldOrder}`, `LEDs: ${pts} Seg: ${seg}`] });
      }
    } catch(e) { 
      AppLogger.error('[useProtocolBuilder] Build failed', e);
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
    bldSens, setBldSens,
    bldC2, setBldC2,
    bldMatrixStyle, setBldMatrixStyle,
    bldIc, setBldIc,
    bldOrder, setBldOrder,
    bldSegs, setBldSegs,
    bldResult,
  };
};
