"""Fix remaining fav.mode 'MULTI'/'RBM' icon checks to also handle renamed 'DIY'/'PROGRAMS'"""
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

# fav.mode icon - handle DIY alongside MULTI (two occurrences)
rep(
    "} else if (fav.mode === 'MULTI') {",
    "} else if (fav.mode === 'MULTI' || fav.mode === 'DIY') {",
    "fav.mode MULTI||DIY icon check"
)

# fav.mode icon - handle PROGRAMS alongside RBM (two occurrences)
rep(
    "name={fav.mode === 'RBM' ? 'animation-play' : 'shape-square-plus'}",
    "name={(fav.mode === 'RBM' || fav.mode === 'PROGRAMS') ? 'animation-play' : 'shape-square-plus'}",
    "fav.mode RBM||PROGRAMS icon"
)

result = content.replace('\n', '\r\n')
with open('src/components/DockedController.tsx', 'w', encoding='utf-8', newline='') as f:
    f.write(result)

print("Changes:")
for c in changes:
    print(f"  {c}")
