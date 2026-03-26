import re
import os

text_path = r'c:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\SK8Lytz_App_Master_Reference.txt'
target_path = r'c:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\constants\RbmPatterns.ts'

with open(text_path, 'r', encoding='utf-8') as f:
    text = f.read()

patterns = {}
matches = re.finditer(r'- `symphony_SymphonyBuild_(\d+)`:\s*(.+)', text)
for m in matches:
    idx = int(m.group(1))
    name = m.group(2).strip().replace("\"", "\\\"").capitalize()
    patterns[idx] = name

# Ensure contiguous
res = []
for i in range(1, 301):
    if i in patterns:
        res.append(patterns[i])
    else:
        res.append(f"Symphony Effect {i}")

arr_str = "export const RBM_PATTERNS = [\n"
for name in res:
    arr_str += f'  "{name}",\n'
arr_str += "];\n\n"
arr_str += "export const getRbmPatternName = (index: number): string => {\n"
arr_str += "  if (index >= 1 && index <= RBM_PATTERNS.length) {\n"
arr_str += "    return `${index}: ${RBM_PATTERNS[index - 1]}`;\n"
arr_str += "  }\n"
arr_str += "  return `Effect ${index}`;\n"
arr_str += "};\n"

with open(target_path, 'w', encoding='utf-8') as f:
    f.write(arr_str)

print(f"Successfully generated {len(res)} pattern names in {target_path}")
