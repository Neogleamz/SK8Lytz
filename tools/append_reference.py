"""Append mode architecture section to master reference"""
section = """

---

## 16. DockedController: Mode Architecture (FINALIZED — April 2026)

### 6-Mode Taxonomy (activeMode)
All UI modes map to a single top-level `activeMode` state. There are NO sub-modes for PROGRAMS, MUSIC, or CAMERA.

| activeMode   | UI Label  | Dock Icon            | Description                                  |
|:-------------|:----------|:---------------------|:---------------------------------------------|
| FAVORITES    | Styles    | cards-heart-outline  | User-saved favorite presets                  |
| MULTIMODE    | Fixed     | palette              | Solid Patterns (PATTERN) + DIY Array (DIY)   |
| PROGRAMS     | Programs  | animation-play       | Hardware RBM patterns (0x42, IDs 1-103)      |
| MUSIC        | Music     | music                | Microphone-reactive light sync (0x73/0x74)   |
| STREET       | Street    | run-fast             | Accelerometer brake-detect mode              |
| CAMERA       | Camera    | camera               | Camera color-capture mode                    |

### fixedSubMode (MULTIMODE only)
Used exclusively when activeMode === 'MULTIMODE':
- PATTERN: Solid Patterns 1-10 (0x59/0x61/0x51 commands)
- DIY: Custom multi-color array builder (0x59 Array)

CRITICAL: PROGRAMS, MUSIC, and CAMERA are NOT and NEVER WERE fixedSubModes. They are top-level activeMode values.

### Dock Routing (DockedController.tsx)
HOME       → onDisconnect()
FAVORITES  → setActiveMode('FAVORITES')
MULTI      → setActiveMode('MULTIMODE') + setFixedSubMode('PATTERN')
PROGRAMS   → setActiveMode('PROGRAMS')
MUSIC      → setActiveMode('MUSIC')
STREET     → setActiveMode('STREET')
CAMERA     → setActiveMode('CAMERA')

### loadFavorite — Legacy Mode Normalization
Saved favorites may have old mode names. The normalization map:
  'RBM' | 'PROGRAMS'              → 'PROGRAMS'    (top-level activeMode)
  'PRESETS' | 'FAVORITES'         → 'FAVORITES'   (top-level activeMode)
  'MULTIMODE' | 'PATTERN'         → MULTIMODE + fixedSubMode='PATTERN'
  'MULTI' | 'DIY' | 'MULTICOLOR'  → MULTIMODE + fixedSubMode='DIY'
  'MUSIC'                         → 'MUSIC'        (top-level activeMode)
  'CAMERA'                        → 'CAMERA'       (top-level activeMode)

### Slider/Dispatch Logic per Mode
- Hue Slider: Routes to fixedHue (MULTIMODE/PATTERN), musicHue (MUSIC), selectedHue (others)
- Brightness: applyFixedPattern (MULTIMODE/PATTERN), setMultiColor (MULTIMODE/DIY), setCustomRbm (PROGRAMS), handleMusicChange (MUSIC), sendColor (others)
- Speed: applyFixedPattern (MULTIMODE/PATTERN), setMultiColor (MULTIMODE/DIY), setCustomRbm (PROGRAMS)
- Color Grid: fixedFg/Bg (MULTIMODE/PATTERN), musicPrimary/Secondary (MUSIC), selectedColor+sendColor (others)

### TypeScript ModeType
type ModeType = 'FAVORITES' | 'MULTIMODE' | 'PROGRAMS' | 'MUSIC' | 'STREET' | 'CAMERA';
fixedSubMode: useState<'PATTERN' | 'DIY'>('PATTERN')  -- MULTIMODE only

### Naming History (April 2026 Refactor)
- PRESETS     → renamed to FAVORITES
- RBM         → renamed to PROGRAMS  
- MULTI       → renamed to DIY (fixedSubMode)
- PROGRAMS, MUSIC, CAMERA → promoted from fixedSubMode to top-level activeMode
"""

with open('tools/SK8Lytz_App_Master_Reference.txt', 'a', encoding='utf-8') as f:
    f.write(section)

print("Master reference updated!")
