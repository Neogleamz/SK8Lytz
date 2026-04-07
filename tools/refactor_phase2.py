"""
SK8Lytz Phase 2: Fix impossible TypeScript comparisons after mode promotion.
These comparisons exist inside `if (activeMode === 'MULTIMODE')` blocks,
where TypeScript narrows the type to 'MULTIMODE' and then flags comparisons to other modes.
"""

with open('src/components/DockedController.tsx', 'r', encoding='utf-8') as f:
    raw = f.read()

content = raw.replace('\r\n', '\n')
changes = []

def rep(old, new, label):
    global content
    count = content.count(old)
    if count > 0:
        content = content.replace(old, new)
        changes.append(f"[{count}x] {label}")
    else:
        changes.append(f"[0x] NOT FOUND - {label}")

# ── Fix visualizerColor (lines 1030-1041) ─────────────────────────────────
# Old: inside 'if (activeMode === MULTIMODE)', has impossible activeMode===MUSIC/CAMERA checks
rep(
    "  const visualizerColor = React.useMemo(() => {\n    if (activeMode === 'MULTIMODE') {\n      if (fixedSubMode === 'DIY' || fixedSubMode === 'PATTERN' || activeMode === 'CAMERA') return selectedColor;\n      if (activeMode === 'MUSIC') {\n        const f = (n: number, k = (n + musicHue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);\n        const hex = [f(5), f(3), f(1)].map(x => Math.round(x * 255).toString(16).padStart(2, '0')).join('');\n        return `#${hex}`;\n      }\n      return fixedColorMode === 'FOREGROUND' ? fixedFgColor : fixedBgColor;\n    }\n    return selectedColor;\n  }, [activeMode, fixedColorMode, fixedFgColor, fixedBgColor, musicHue, selectedColor, fixedSubMode]);",
    "  const visualizerColor = React.useMemo(() => {\n    if (activeMode === 'MULTIMODE') {\n      if (fixedSubMode === 'PATTERN') return fixedColorMode === 'FOREGROUND' ? fixedFgColor : fixedBgColor;\n      return selectedColor; // DIY\n    }\n    if (activeMode === 'MUSIC') {\n      const f = (n: number, k = (n + musicHue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);\n      const hex = [f(5), f(3), f(1)].map(x => Math.round(x * 255).toString(16).padStart(2, '0')).join('');\n      return `#${hex}`;\n    }\n    return selectedColor;\n  }, [activeMode, fixedColorMode, fixedFgColor, fixedBgColor, musicHue, selectedColor, fixedSubMode]);",
    "visualizerColor memo"
)

# ── Fix Visualizer mode prop (line 1086) ──────────────────────────────────
# Old: ...MULTIMODE ? (DIY ? MULTICOLOR : (PATTERN ? RBM : (activeMode==='MUSIC' (impossible) ...
rep(
    "mode={activeMode === 'FAVORITES' ? 'MULTICOLOR' : activeMode === 'MULTIMODE' ? (fixedSubMode === 'DIY' ? 'MULTICOLOR' : (fixedSubMode === 'PATTERN' ? 'RBM' : (activeMode === 'MUSIC' ? 'MUSIC' : (activeMode === 'CAMERA' ? 'CAMERA' : 'MULTIMODE')))) : activeMode}",
    "mode={activeMode === 'FAVORITES' ? 'MULTICOLOR' : activeMode === 'MULTIMODE' ? (fixedSubMode === 'DIY' ? 'MULTICOLOR' : 'MULTIMODE') : activeMode}",
    "visualizer mode prop"
)

# ── Fix patternId prop (line 1087): activeMode==='MULTIMODE' && activeMode==='MUSIC' impossible
rep(
    "patternId={activeMode === 'MULTIMODE' && activeMode === 'MUSIC' ? musicPatternId : (activeMode === 'MULTIMODE' && fixedSubMode === 'PATTERN' ? fixedPatternId : selectedPatternId)}",
    "patternId={activeMode === 'MUSIC' ? musicPatternId : (activeMode === 'MULTIMODE' && fixedSubMode === 'PATTERN' ? fixedPatternId : selectedPatternId)}",
    "visualizer patternId prop"
)

# ── Fix impossible conditions from narrowed activeMode in nested blocks ──
# These all follow the pattern: activeMode === 'MULTIMODE' && activeMode === 'MUSIC/CAMERA/PROGRAMS'
rep(
    "activeMode === 'MULTIMODE' && activeMode === 'PROGRAMS'",
    "activeMode === 'PROGRAMS'",
    "impossible MULTIMODE&&PROGRAMS"
)
rep(
    "activeMode === 'MULTIMODE' && activeMode === 'MUSIC'",
    "activeMode === 'MUSIC'",
    "impossible MULTIMODE&&MUSIC"
)
rep(
    "activeMode === 'MULTIMODE' && activeMode === 'CAMERA'",
    "activeMode === 'CAMERA'",
    "impossible MULTIMODE&&CAMERA"
)

# Also the negated forms (activeMode !== 'MULTIMODE' or activeMode === 'MULTIMODE' in conditionals)
# These appear in slider visibility guards like:
# !(activeMode === 'MULTIMODE' && (activeMode === 'PROGRAMS' || activeMode === 'CAMERA'))
rep(
    "!(activeMode === 'MULTIMODE' && (activeMode === 'PROGRAMS' || activeMode === 'CAMERA'))",
    "!(activeMode === 'PROGRAMS' || activeMode === 'CAMERA')",
    "slider guard PROGRAMS||CAMERA"
)
rep(
    "!(activeMode === 'MULTIMODE' && (activeMode === 'MUSIC' || activeMode === 'CAMERA'))",
    "!(activeMode === 'MUSIC' || activeMode === 'CAMERA')",
    "slider guard MUSIC||CAMERA"
)
rep(
    "!(activeMode === 'MULTIMODE' && (activeMode === 'PROGRAMS' || activeMode === 'MUSIC' || activeMode === 'CAMERA'))",
    "!(activeMode === 'PROGRAMS' || activeMode === 'MUSIC' || activeMode === 'CAMERA')",
    "slider guard PROGRAMS||MUSIC||CAMERA"
)

# WRITE
result = content.replace('\n', '\r\n')
with open('src/components/DockedController.tsx', 'w', encoding='utf-8', newline='') as f:
    f.write(result)

print("Changes:")
for c in changes:
    print(f"  {c}")
print(f"\nDone! Length: {len(content)}")
