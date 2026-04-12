const fs = require('fs');
const glob = require('glob');

const files = fs.readdirSync('src/components/crew').filter(f => f.endsWith('.tsx'));

for (const file of files) {
  const filepath = 'src/components/crew/' + file;
  let code = fs.readFileSync(filepath, 'utf8');

  // Regex to remove the trailing }; that belonged to the lambda variable assignment
  // e.g. }; \n // RENDER... \n }
  code = code.replace(/};\s*\/\/ ═════.*?\s*\}$/s, '}');
  
  // also handle the single-line parens return ones closing );
  code = code.replace(/\);\s*\/\/ ═════.*?\s*\}$/s, '}');

  fs.writeFileSync(filepath, code);
}
console.log('Fixed trailing syntax on all screens.');
