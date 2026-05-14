const fs = require('fs');
const parser = require('@babel/parser');

const files = process.argv.slice(2);
let hasError = false;

if (files.length === 0) {
  console.log('Babel Syntax Gate: No files to check.');
  process.exit(0);
}

files.forEach(file => {
  if (!file.endsWith('.ts') && !file.endsWith('.tsx') && !file.endsWith('.js') && !file.endsWith('.jsx')) return;
  if (!fs.existsSync(file)) return; // File might have been deleted

  const content = fs.readFileSync(file, 'utf8');
  try {
    parser.parse(content, {
      sourceType: 'module',
      plugins: [
        'typescript',
        'jsx',
        'classProperties',
        'decorators-legacy',
        'optionalChaining',
        'nullishCoalescingOperator'
      ]
    });
  } catch (e) {
    console.error(`\n🚨 BABEL SYNTAX ERROR in ${file}`);
    console.error(`Line ${e.loc?.line}, Column ${e.loc?.column}: ${e.message}\n`);
    hasError = true;
  }
});

if (hasError) {
  console.error('❌ Commit rejected due to Babel syntax errors (AST parsing failed).');
  console.error('TSC may pass, but Metro bundler will crash. Fix the errors above.\n');
  process.exit(1);
} else {
  console.log('✅ Babel Syntax Gate passed.');
}
