
const fs = require("fs");
const path = require("path");
const results = {};
function walk(dir) {
  const files = fs.readdirSync(dir);
  for (const file of files) {
    const fullPath = path.join(dir, file);
    if (fs.statSync(fullPath).isDirectory()) {
      walk(fullPath);
    } else if (fullPath.endsWith(".ts") || fullPath.endsWith(".tsx")) {
      const content = fs.readFileSync(fullPath, "utf8");
      const lines = content.split("\n");
      lines.forEach((line, i) => {
        const matches = line.matchAll(/AsyncStorage\.(get|set|remove)Item\(\s*([\`\x27\x22A-Za-z0-9_\-\$\{\}]+)/g);
        for (const match of matches) {
          const key = match[2];
          if (!results[key]) results[key] = new Set();
          results[key].add({ file: fullPath, line: i + 1, type: match[1] });
        }
      });
    }
  }
}
walk("src");
const output = {};
for (const [key, records] of Object.entries(results)) {
  const fileSet = new Set([...records].map(r => r.file));
  if (fileSet.size > 1) {
    output[key] = [...records];
  }
}
console.log(JSON.stringify(output, null, 2));

