"""Rename PRESETS/RBM strings in ProductVisualizer and CommunityModal"""
files = ['src/components/ProductVisualizer.tsx', 'src/components/CommunityModal.tsx']
for path in files:
    with open(path, 'r', encoding='utf-8') as f:
        c = f.read()
    c = c.replace("'PRESETS'", "'FAVORITES'")
    c = c.replace("'RBM'", "'PROGRAMS'")
    with open(path, 'w', encoding='utf-8') as f:
        f.write(c)
    print(f'Updated: {path}')
