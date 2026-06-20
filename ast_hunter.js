const fs = require('fs');
const path = require('path');
const ts = require('typescript');

function walk(node, sourceFile, filePath, results) {
    if (ts.isArrayLiteralExpression(node) || ts.isNewExpression(node) || ts.isCallExpression(node)) {
        // Look for [0x59, ...] or Buffer.from([0x59, ...]) or new Uint8Array([0x59, ...])
        let elements = [];
        if (ts.isArrayLiteralExpression(node)) {
            elements = node.elements;
        } else if (ts.isCallExpression(node) && node.arguments.length > 0 && ts.isArrayLiteralExpression(node.arguments[0])) {
             elements = node.arguments[0].elements;
        } else if (ts.isNewExpression(node) && node.arguments && node.arguments.length > 0 && ts.isArrayLiteralExpression(node.arguments[0])) {
             elements = node.arguments[0].elements;
        }
        
        if (elements && elements.length > 0) {
             const firstEl = elements[0];
             if (ts.isNumericLiteral(firstEl)) {
                  const text = firstEl.getText(sourceFile);
                  if (text.startsWith('0x') || text.startsWith('0X')) {
                       results.push({
                           file: filePath,
                           line: sourceFile.getLineAndCharacterOfPosition(node.getStart()).line + 1,
                           description: `Raw byte array constructed outside src/protocols/`,
                           severity: "HIGH",
                           snippet: node.getText(sourceFile).substring(0, 100) + "..."
                       });
                  }
             }
        }
    }
    ts.forEachChild(node, child => walk(child, sourceFile, filePath, results));
}

function processDir(dir, results) {
    const files = fs.readdirSync(dir);
    for (const file of files) {
        const fullPath = path.join(dir, file);
        if (fs.statSync(fullPath).isDirectory()) {
            if (file === 'protocols' || file === '__tests__') continue; // exclude protocols and tests
            processDir(fullPath, results);
        } else if (fullPath.endsWith('.ts') || fullPath.endsWith('.tsx')) {
            const code = fs.readFileSync(fullPath, 'utf8');
            const sourceFile = ts.createSourceFile(fullPath, code, ts.ScriptTarget.Latest, true);
            walk(sourceFile, sourceFile, fullPath, results);
        }
    }
}

const results = [];
processDir('C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src', results);
console.log(JSON.stringify(results, null, 2));
