"""
SK8Lytz Complete Mode Architecture Refactor
From: ModeType = 'PRESETS' | 'MULTIMODE' | 'RBM' | 'MUSIC' | 'STREET' | 'CAMERA'
      fixedSubMode = 'PATTERN' | 'MULTI' | 'RBM' | 'MUSIC' | 'CAMERA'

To:   ModeType = 'FAVORITES' | 'MULTIMODE' | 'PROGRAMS' | 'MUSIC' | 'STREET' | 'CAMERA'
      fixedSubMode = 'PATTERN' | 'DIY'  (PROGRAMS/MUSIC/CAMERA promoted to top-level)
"""

with open('src/components/DockedController.tsx', 'r', encoding='utf-8') as f:
    raw = f.read()

content = raw.replace('\r\n', '\n')
original_len = len(content)
changes = []

def rep(old, new, label):
    global content
    count = content.count(old)
    if count > 0:
        content = content.replace(old, new)
        changes.append(f"[{count}x] {label}")
    else:
        changes.append(f"[0x] NOT FOUND - {label}")

# ── PHASE 1: Type declarations ─────────────────────────────────────────────
rep(
    "type ModeType = 'PRESETS' | 'MULTIMODE' | 'RBM' | 'MUSIC' | 'STREET' | 'CAMERA';",
    "type ModeType = 'FAVORITES' | 'MULTIMODE' | 'PROGRAMS' | 'MUSIC' | 'STREET' | 'CAMERA';",
    "ModeType declaration"
)
rep(
    "useState<'PATTERN' | 'MULTI' | 'RBM' | 'MUSIC' | 'CAMERA'>('PATTERN')",
    "useState<'PATTERN' | 'DIY'>('PATTERN')",
    "fixedSubMode useState type"
)

# ── PHASE 2: activeMode value renames ──────────────────────────────────────
rep("setActiveMode('PRESETS')", "setActiveMode('FAVORITES')", "setActiveMode PRESETS->FAVORITES")
rep("activeMode === 'PRESETS'", "activeMode === 'FAVORITES'", "activeMode==='PRESETS'")
rep("activeMode !== 'PRESETS'", "activeMode !== 'FAVORITES'", "activeMode!=='PRESETS'")
rep("mode === 'PRESETS'", "mode === 'FAVORITES'", "mode==='PRESETS'")
rep("'PRESETS'", "'FAVORITES'", "remaining bare PRESETS strings")
rep("setActiveMode('RBM')", "setActiveMode('PROGRAMS')", "setActiveMode RBM->PROGRAMS")
rep("activeMode === 'RBM'", "activeMode === 'PROGRAMS'", "activeMode==='RBM'")
rep("activeMode !== 'RBM'", "activeMode !== 'PROGRAMS'", "activeMode!=='RBM'")

# ── PHASE 3: fixedSubMode MULTI->DIY ──────────────────────────────────────
rep("setFixedSubMode('MULTI')", "setFixedSubMode('DIY')", "setFixedSubMode MULTI->DIY")
rep("fixedSubMode === 'MULTI'", "fixedSubMode === 'DIY'", "fixedSubMode==='MULTI'")
rep("fixedSubMode !== 'MULTI'", "fixedSubMode !== 'DIY'", "fixedSubMode!=='MULTI'")
rep("targetSubMode === 'MULTI'", "targetSubMode === 'DIY'", "targetSubMode==='MULTI'")
rep("mode === 'MULTICOLOR' ? 'MULTI'", "mode === 'MULTICOLOR' ? 'DIY'", "MULTICOLOR?MULTI")

# ── PHASE 4: Promote RBM submode -> activeMode PROGRAMS ───────────────────
rep("fixedSubMode === 'RBM'", "activeMode === 'PROGRAMS'", "fixedSubMode==='RBM' promoted")
rep("setFixedSubMode('RBM')", "setActiveMode('PROGRAMS')", "setFixedSubMode RBM->setActiveMode PROGRAMS")
rep("targetSubMode === 'RBM'", "legacyMode === 'PROGRAMS'", "targetSubMode==='RBM'")

# ── PHASE 5: Promote MUSIC submode -> activeMode MUSIC ────────────────────
rep("fixedSubMode === 'MUSIC'", "activeMode === 'MUSIC'", "fixedSubMode==='MUSIC' promoted")
rep("setFixedSubMode('MUSIC')", "setActiveMode('MUSIC')", "setFixedSubMode MUSIC->setActiveMode MUSIC")
rep("targetSubMode === 'MUSIC'", "legacyMode === 'MUSIC'", "targetSubMode==='MUSIC'")

# ── PHASE 6: Promote CAMERA submode -> activeMode CAMERA ──────────────────
rep("fixedSubMode === 'CAMERA'", "activeMode === 'CAMERA'", "fixedSubMode==='CAMERA' promoted")
rep("setFixedSubMode('CAMERA')", "setActiveMode('CAMERA')", "setFixedSubMode CAMERA->setActiveMode CAMERA")
rep("targetSubMode === 'CAMERA'", "legacyMode === 'CAMERA'", "targetSubMode==='CAMERA'")

# ── PHASE 7: Display labels ────────────────────────────────────────────────
rep("case 'PRESETS': return 'Favorites'", "case 'FAVORITES': return 'Favorites'", "case PRESETS label")
rep("case 'RBM': return 'Programs'", "case 'PROGRAMS': return 'Programs'", "case RBM label")
rep("case 'PRESETS':", "case 'FAVORITES':", "case PRESETS switch")
rep("case 'RBM':", "case 'PROGRAMS':", "case RBM switch")
rep("mode: 'RBM'", "mode: 'PROGRAMS'", "mode:RBM save")

# ── PHASE 8: Dock id PRESETS->FAVORITES ──────────────────────────────────
rep(
    "{ id: 'PRESETS', icon: 'cards-heart-outline' }",
    "{ id: 'FAVORITES', icon: 'cards-heart-outline' }",
    "dock PRESETS id"
)

# ── PHASE 9: Dock isActive ────────────────────────────────────────────────
rep(
    "const isActive = (activeMode === 'MULTIMODE' && (fixedSubMode === dockItem.id || (dockItem.id === 'MULTI' && (fixedSubMode === 'PATTERN')))) || activeMode === dockItem.id;",
    "const isActive = dockItem.id === 'MULTI' ? activeMode === 'MULTIMODE' : activeMode === dockItem.id;",
    "dock isActive"
)

# ── PHASE 10: loadFavorite - rewrite entire function ──────────────────────
idx = content.find("const loadFavorite = (favRaw: IFavoriteState) => {")
if idx >= 0:
    # Find closing }; of this function
    end_idx = content.find("\n  };", idx) + 5
    print(f"loadFavorite: [{idx}:{end_idx}]")
    print("Block start:", repr(content[idx:idx+150]))

    new_lf = """const loadFavorite = (favRaw: IFavoriteState) => {
     const fav: any = favRaw;
     setActiveFavoriteId(fav.id);
     setSpeed(fav.speed);
     setBrightness(fav.brightness);
     if (fav.color) setSelectedColor(fav.color);

     // Normalize legacy mode names to new taxonomy
     const legacyMode = (fav.mode === 'RBM' || fav.mode === 'PROGRAMS') ? 'PROGRAMS'
       : (fav.mode === 'FAVORITES' || fav.mode === 'PRESETS') ? 'FAVORITES'
       : fav.mode;

     if (legacyMode === 'PROGRAMS') {
        setActiveMode('PROGRAMS');
        setSelectedPatternId(fav.patternId);
        if (writeToDevice) writeToDevice(ZenggeProtocol.setCustomRbm(fav.patternId, fav.speed, fav.brightness));
     } else if (legacyMode === 'MUSIC') {
        setActiveMode('MUSIC');
        setMusicPatternId(fav.patternId);
        handleMusicChange(fav.patternId, micSensitivity, fav.brightness, micSource);
     } else if (legacyMode === 'CAMERA') {
        setActiveMode('CAMERA');
     } else if (legacyMode === 'FAVORITES') {
        setActiveMode('FAVORITES');
     } else if (legacyMode === 'MULTIMODE' || legacyMode === 'PATTERN') {
        setActiveMode('MULTIMODE');
        setFixedSubMode('PATTERN');
        setFixedPatternId(fav.patternId);
        setFixedColorMode(fav.fixedColorMode);
        setFixedFgColor(fav.fixedFgColor);
        setFixedBgColor(fav.fixedBgColor);
        applyFixedPattern(fav.patternId, fav.fixedFgColor, fav.fixedBgColor, fav.speed, fav.brightness);
     } else if (legacyMode === 'MULTI' || legacyMode === 'DIY' || legacyMode === 'MULTICOLOR') {
        setActiveMode('MULTIMODE');
        setFixedSubMode('DIY');
        setMultiColors(fav.multiColors || []);
        setMultiTransition(fav.multiTransition || 3);
        setMultiLength(fav.multiLength || 16);
        if (writeToDevice && fav.multiColors) {
           const sortIdx = hwSettings?.colorSorting ?? 2;
           const rgbColors = fav.multiColors.map((h: string) => {
              const r = parseInt(h.slice(1,3), 16) || 0;
              const g = parseInt(h.slice(3,5), 16) || 0;
              const b = parseInt(h.slice(5,7), 16) || 0;
              return ZenggeProtocol.applyColorSorting(r, g, b, sortIdx);
           });
           writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, clampSpeed(fav.speed), 1, fav.multiTransition));
        }
     } else {
        // Unknown/legacy mode - best-effort color dispatch
        if (fav.color) setTimeout(() => {
           sendColor(parseInt(fav.color.slice(1,3),16)||0, parseInt(fav.color.slice(3,5),16)||0, parseInt(fav.color.slice(5,7),16)||0);
        }, 100);
     }
  }"""
    content = content[:idx] + new_lf + content[end_idx:]
    changes.append("[REWRITE] loadFavorite")
else:
    changes.append("[ERROR] loadFavorite not found!")

# ── PHASE 11: isMusicActive fix ───────────────────────────────────────────
rep(
    "const isMusicActive = activeMode === 'MUSIC' || (activeMode === 'MULTIMODE' && activeMode === 'MUSIC');",
    "const isMusicActive = activeMode === 'MUSIC';",
    "isMusicActive impossible double condition"
)

# ── WRITE RESULT ──────────────────────────────────────────────────────────
result = content.replace('\n', '\r\n')
with open('src/components/DockedController.tsx', 'w', encoding='utf-8', newline='') as f:
    f.write(result)

print(f"\nOriginal length: {original_len}")
print(f"New length: {len(content)}")
print(f"\nChanges applied:")
for c in changes:
    print(f"  {c}")
print(f"\nDone!")
