"""
SK8Lytz Phase 3: Fix remaining impossible comparisons in slider handlers.
All these occur inside `if (activeMode === 'MULTIMODE')` blocks where TypeScript
knows activeMode is narrowed to 'MULTIMODE', so comparing it to other modes is impossible.

Fixes:
1. Color grid: inside(MULTIMODE) > else if(activeMode==='MUSIC') - move MUSIC handling to top-level else
2. Hue slider value expression: impossible MULTIMODE&&MUSIC
3. Hue slider onValueChange: else if(activeMode==='MUSIC') inside MULTIMODE block  
4. Hue slider onSlidingComplete: else if(activeMode==='MUSIC') inside MULTIMODE block
5. Brightness slider: else if(activeMode==='PROGRAMS') inside MULTIMODE block
6. Speed slider: else if(activeMode==='PROGRAMS') inside MULTIMODE block
7. Selected color bar: !(MULTIMODE && (MUSIC || ...))
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

# ── 1. Selected color bar visibility (line 1595) ──────────────────────────
# Old: !(activeMode === 'MULTIMODE' && (activeMode === 'MUSIC' || (fixedSubMode === 'PATTERN' && fixedPatternId !== 1)))
rep(
    "!(activeMode === 'MULTIMODE' && (activeMode === 'MUSIC' || (fixedSubMode === 'PATTERN' && fixedPatternId !== 1)))",
    "!(activeMode === 'MUSIC' || (activeMode === 'MULTIMODE' && fixedSubMode === 'PATTERN' && fixedPatternId !== 1))",
    "selected color bar visibility"
)

# ── 2. Color grid: inside MULTIMODE block, else if activeMode==='MUSIC' ───
# Lines 1720-1729: } else if (activeMode === 'MUSIC') { ... } else {
rep(
    "             } else if (activeMode === 'MUSIC') {\n                if (musicColorFocus === 'PRIMARY') {\n                    setMusicPrimaryColor(color);\n                    if (hueMap[color] !== undefined) setMusicHue(hueMap[color]);\n                    handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, color, musicSecondaryColor);\n                } else {\n                    setMusicSecondaryColor(color);\n                    if (hueMap[color] !== undefined) setMusicSecondaryHue(hueMap[color]);\n                    handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, color);\n                }\n             } else {\n                setSelectedColor(color);\n                if (hueMap[color] !== undefined) setFixedHue(hueMap[color]);\n             }\n          } else {",
    "             } else {\n                setSelectedColor(color);\n                if (hueMap[color] !== undefined) setFixedHue(hueMap[color]);\n             }\n          } else if (activeMode === 'MUSIC') {\n             if (musicColorFocus === 'PRIMARY') {\n                 setMusicPrimaryColor(color);\n                 if (hueMap[color] !== undefined) setMusicHue(hueMap[color]);\n                 handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, color, musicSecondaryColor);\n             } else {\n                 setMusicSecondaryColor(color);\n                 if (hueMap[color] !== undefined) setMusicSecondaryHue(hueMap[color]);\n                 handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, color);\n             }\n          } else {",
    "color grid MUSIC handler moved out of MULTIMODE block"
)

# ── 3. Hue slider value expression (line 1773) ────────────────────────────
# Old: activeMode === 'MULTIMODE' ? (activeMode === 'MUSIC' ? ... : fixedHue) : selectedHue
rep(
    "value={activeMode === 'MULTIMODE' ? (activeMode === 'MUSIC' ? (musicColorFocus === 'PRIMARY' ? musicHue : musicSecondaryHue) : fixedHue) : selectedHue}",
    "value={activeMode === 'MUSIC' ? (musicColorFocus === 'PRIMARY' ? musicHue : musicSecondaryHue) : activeMode === 'MULTIMODE' ? fixedHue : selectedHue}",
    "hue slider value expression"
)

# ── 4. Hue slider onValueChange: else if activeMode==='MUSIC' inside MULTIMODE
# Lines 1788-1799: inside if(MULTIMODE) block
rep(
    "      } else if (activeMode === 'MUSIC') {\n                         const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);\n                         const rgb2hex = (r: number, g: number, b: number) => \"#\" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, \"0\").toUpperCase()).join(\"\");\n                         const hex = rgb2hex(f(5), f(3), f(1));\n                         if (musicColorFocus === 'PRIMARY') {\n                            setMusicPrimaryColor(hex);\n                            setMusicHue(hue);\n                         } else {\n                            setMusicSecondaryColor(hex);\n                            setMusicSecondaryHue(hue);\n                         }\n                      } else {\n                        setFixedHue(hue);\n                        const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);\n                        const rgb2hex = (r: number, g: number, b: number) => \"#\" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, \"0\")).join(\"\");\n                        const hex = rgb2hex(f(5), f(3), f(1));\n                        setSelectedColor(hex);\n                      }\n                    } else if (activeMode === 'MUSIC') {\n                      if (musicColorFocus === 'PRIMARY') setMusicHue(hue);\n                      else setMusicSecondaryHue(hue);",
    "      } else {\n                        setFixedHue(hue);\n                        const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);\n                        const rgb2hex = (r: number, g: number, b: number) => \"#\" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, \"0\")).join(\"\");\n                        const hex = rgb2hex(f(5), f(3), f(1));\n                        setSelectedColor(hex);\n                      }\n                    } else if (activeMode === 'MUSIC') {\n                       const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);\n                       const rgb2hex = (r: number, g: number, b: number) => \"#\" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, \"0\").toUpperCase()).join(\"\");\n                       const hex = rgb2hex(f(5), f(3), f(1));\n                       if (musicColorFocus === 'PRIMARY') { setMusicPrimaryColor(hex); setMusicHue(hue); }\n                       else { setMusicSecondaryColor(hex); setMusicSecondaryHue(hue); }",
    "hue slider onValueChange MUSIC handler moved out of MULTIMODE block"
)

# ── 5. Hue slider onSlidingComplete: else if activeMode==='MUSIC' inside MULTIMODE
# Lines 1820-1831
rep(
    "                       } else if (activeMode === 'MUSIC') {\n                         const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);\n                         const rgb2hex = (r: number, g: number, b: number) => \"#\" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, \"0\").toUpperCase()).join(\"\");\n                         const hex = rgb2hex(f(5), f(3), f(1));\n                         if (musicColorFocus === 'PRIMARY') {\n                            handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, hex, musicSecondaryColor, musicMatrixStyle);\n                         } else {\n                            handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, hex, musicMatrixStyle);\n                         }\n                       }\n                      } else if (activeMode === 'MUSIC') {\n                        // Deprecated top route\n                      } else {",
    "                       }\n                      } else if (activeMode === 'MUSIC') {\n                         const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);\n                         const rgb2hex = (r: number, g: number, b: number) => \"#\" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, \"0\").toUpperCase()).join(\"\");\n                         const hex = rgb2hex(f(5), f(3), f(1));\n                         if (musicColorFocus === 'PRIMARY') {\n                            handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, hex, musicSecondaryColor, musicMatrixStyle);\n                         } else {\n                            handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, hex, musicMatrixStyle);\n                         }\n                      } else {",
    "hue slider onSlidingComplete MUSIC handler moved out of MULTIMODE block"
)

# ── 6. Brightness slider: else if(activeMode==='PROGRAMS') inside MULTIMODE block
# Lines 1879-1885: inside if(MULTIMODE) > else if(PROGRAMS) - impossible
rep(
    "                          } else if (activeMode === 'PROGRAMS') {\n                            if (selectedPatternId === 100) {\n                              applyEmergencyPattern(speed, val);\n                            } else {\n                              writeToDevice(ZenggeProtocol.setCustomRbm(selectedPatternId, speed, val));\n                            }\n                          }\n                        } else {",
    "                          }\n                        } else if (activeMode === 'PROGRAMS') {\n                          if (selectedPatternId === 100) {\n                            applyEmergencyPattern(speed, val);\n                          } else {\n                            writeToDevice(ZenggeProtocol.setCustomRbm(selectedPatternId, speed, val));\n                          }\n                        } else {",
    "brightness slider PROGRAMS handler moved out of MULTIMODE block"
)

# ── 7. Speed slider: else if(activeMode==='PROGRAMS') inside MULTIMODE block
# Lines 1928-1934
rep(
    "                          } else if (activeMode === 'PROGRAMS') {\n                            if (selectedPatternId === 100) {\n                              applyEmergencyPattern(val, brightness);\n                            } else {\n                              writeToDevice(ZenggeProtocol.setCustomRbm(selectedPatternId, val, brightness));\n                            }\n                          }\n                        }\n                      }\n                    }}",
    "                          }\n                        } else if (activeMode === 'PROGRAMS') {\n                          if (selectedPatternId === 100) {\n                            applyEmergencyPattern(val, brightness);\n                          } else {\n                            writeToDevice(ZenggeProtocol.setCustomRbm(selectedPatternId, val, brightness));\n                          }\n                        }\n                      }\n                    }}",
    "speed slider PROGRAMS handler moved out of MULTIMODE block"
)

# ── Write ──────────────────────────────────────────────────────────────────
result = content.replace('\n', '\r\n')
with open('src/components/DockedController.tsx', 'w', encoding='utf-8', newline='') as f:
    f.write(result)

print("Changes applied:")
for c in changes:
    print(f"  {c}")
print(f"\nDone! Length: {len(content)}")
