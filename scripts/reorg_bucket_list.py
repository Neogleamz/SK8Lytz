import sys
import re

with open('tools/SK8Lytz_Bucket_List.md', 'r', encoding='utf-8') as f:
    lines = f.readlines()

completed_items = []
new_lines = []
in_completed_section = False
completed_index = 0

for i, line in enumerate(lines):
    if line.startswith('## ✅ Completed This Session'):
        in_completed_section = True
        completed_index = len(new_lines)
        new_lines.append(line)
        continue
    
    if line.startswith('## ✅ Completed Previously'):
        in_completed_section = False
        new_lines.append(line)
        continue

    if in_completed_section:
        if line.strip() == '' or line.startswith('- [x]') or line.startswith('- [ ]'):
            new_lines.append(line)
        else:
            new_lines.append(line)
        continue

    if line.startswith('- [x]'):
        completed_items.append(line)
    else:
        new_lines.append(line)

# Now insert completed items at the end of the Completed This Session section
insert_idx = -1
for i, line in enumerate(new_lines):
    if line.startswith('## ✅ Completed Previously'):
        insert_idx = i - 1
        break

if insert_idx != -1:
    while insert_idx > 0 and new_lines[insert_idx].strip() == '':
        insert_idx -= 1
    
    # insert after the last non-empty line of the section
    for item in completed_items:
        new_lines.insert(insert_idx + 1, item)
        insert_idx += 1

with open('tools/SK8Lytz_Bucket_List.md', 'w', encoding='utf-8') as f:
    f.writelines(new_lines)

print(f"Moved {len(completed_items)} items.")
