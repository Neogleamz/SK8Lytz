const fs = require('fs');
const files = fs.readdirSync('src/components/crew').filter(f => f.endsWith('.tsx'));

for (const file of files) {
  const filepath = 'src/components/crew/' + file;
  let code = fs.readFileSync(filepath, 'utf8');

  // Strip anything after the last `);` or `};` inside the function, and put a single } 
  // Let's find the last occurrence of "  // ════════════════" and just truncate there.
  const idx = code.indexOf('  // ════════════════');
  if (idx !== -1) {
    code = code.substring(0, idx);
    // remove the last `};` or `);`
    code = code.replace(/[\}\)]\s*;\s*$/, '');
    code += '\n}\n';
  }

  fs.writeFileSync(filepath, code);
}
console.log('Force truncated trailing comments.');
