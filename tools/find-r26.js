const ts = require('typescript');
const fs = require('fs');
const path = require('path');

function walk(dir, callback) {
  const files = fs.readdirSync(dir);
  for (const f of files) {
    const full = path.join(dir, f);
    if (fs.statSync(full).isDirectory()) {
      walk(full, callback);
    } else if (f.endsWith('.ts') || f.endsWith('.tsx')) {
      callback(full);
    }
  }
}

const results = [];

function analyzeFile(filePath) {
  const content = fs.readFileSync(filePath, 'utf8');
  const sourceFile = ts.createSourceFile(
    filePath,
    content,
    ts.ScriptTarget.Latest,
    true
  );

  function visit(node) {
    if (ts.isCallExpression(node)) {
      const isSetInterval = ts.isIdentifier(node.expression) && node.expression.text === 'setInterval';
      const isUseEffect = ts.isIdentifier(node.expression) && node.expression.text === 'useEffect';

      if (isSetInterval || isUseEffect) {
        const arg0 = node.arguments[0];
        if (arg0 && (ts.isArrowFunction(arg0) || ts.isFunctionExpression(arg0))) {
          let asyncBodies = [];
          
          if (isSetInterval) {
            const isAsync = arg0.modifiers && arg0.modifiers.some(m => m.kind === ts.SyntaxKind.AsyncKeyword);
            if (isAsync) {
              asyncBodies.push(arg0.body);
            }
          }

          if (isUseEffect && ts.isBlock(arg0.body)) {
             // Look for inner async functions
             arg0.body.statements.forEach(stmt => {
               if (ts.isVariableStatement(stmt)) {
                 stmt.declarationList.declarations.forEach(decl => {
                   if (decl.initializer && (ts.isArrowFunction(decl.initializer) || ts.isFunctionExpression(decl.initializer))) {
                     const isAsyncDecl = decl.initializer.modifiers && decl.initializer.modifiers.some(m => m.kind === ts.SyntaxKind.AsyncKeyword);
                     if (isAsyncDecl) {
                       asyncBodies.push(decl.initializer.body);
                     }
                   }
                 });
               } else if (ts.isFunctionDeclaration(stmt)) {
                 const isAsyncDecl = stmt.modifiers && stmt.modifiers.some(m => m.kind === ts.SyntaxKind.AsyncKeyword);
                 if (isAsyncDecl) {
                   asyncBodies.push(stmt.body);
                 }
               }
             });
          }

          for (const body of asyncBodies) {
             let hasGuard = false;
             // We look for 'if (something) return' at the top level of the body
             if (ts.isBlock(body)) {
               body.statements.forEach(stmt => {
                 if (ts.isIfStatement(stmt)) {
                   if (ts.isReturnStatement(stmt.thenStatement) || 
                       (ts.isBlock(stmt.thenStatement) && stmt.thenStatement.statements.some(ts.isReturnStatement))) {
                     hasGuard = true;
                   }
                 }
               });
             }

             if (!hasGuard) {
                const { line } = sourceFile.getLineAndCharacterOfPosition(node.getStart());
                const preview = content.substring(node.getStart(), node.getStart() + 80).replace(/\n/g, ' ');
                results.push({
                  file: filePath.replace(/\\/g, '/'),
                  line: line + 1,
                  type: isSetInterval ? 'setInterval' : 'useEffect',
                  preview
                });
             }
          }
        }
      }
    }
    ts.forEachChild(node, visit);
  }

  visit(sourceFile);
}

walk(path.resolve(__dirname, '../src'), analyzeFile);
console.log(JSON.stringify(results, null, 2));
