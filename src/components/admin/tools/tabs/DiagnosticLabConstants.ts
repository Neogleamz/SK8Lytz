export const TRANSITION_TYPES = [
  { byte: 0x00, label: 'CASCADE',       color: '#FF9500', confirmed: true,  desc: '✅ CONFIRMED — Continuous scroll. Hardware loops array around strip.' },
  { byte: 0x01, label: 'FREEZE',        color: '#00CC88', confirmed: true,  desc: '✅ CONFIRMED — Static lock. Array held in place, no movement.' },
  { byte: 0x02, label: 'STROBE',        color: '#FF4040', confirmed: true,  desc: '✅ CONFIRMED — Flash effect. Visually confirmed on 0xA3 hardware.' },
  { byte: 0x03, label: 'RUNNING WATER', color: '#FF69B4', confirmed: true,  desc: '✅ CONFIRMED — One-shot marquee trigger per command send.' },
  { byte: 0x04, label: 'JUMP?',         color: '#C084FC', confirmed: false, desc: '❓ HYPOTHESIS — APK StaticColorfulMode.java entry. Hardware response unknown.' },
];

export const QUICK_PALETTE = [
  { p: 'Red', hex: '#FF0000', r: 255, g: 0, b: 0 },
  { p: 'Green', hex: '#00FF00', r: 0, g: 255, b: 0 },
  { p: 'Blue', hex: '#0000FF', r: 0, g: 0, b: 255 },
  { p: 'Yellow', hex: '#FFFF00', r: 255, g: 255, b: 0 },
  { p: 'Cyan', hex: '#00FFFF', r: 0, g: 255, b: 255 },
  { p: 'Purple', hex: '#8000FF', r: 128, g: 0, b: 255 },
  { p: 'Orange', hex: '#FF8800', r: 255, g: 136, b: 0 },
  { p: 'Magenta', hex: '#FF00FF', r: 255, g: 0, b: 255 },
  { p: 'White', hex: '#FFFFFF', r: 255, g: 255, b: 255 },
  { p: 'Black', hex: '#000000', r: 0, g: 0, b: 0 },
];
