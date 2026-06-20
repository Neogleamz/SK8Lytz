const fs = require('fs');
const path = require('path');
const ts = require('typescript');

function walkDir(dir, callback) {
    fs.readdirSync(dir).forEach(f => {
        let dirPath = path.join(dir, f);
        let isDirectory = fs.statSync(dirPath).isDirectory();
        isDirectory ? walkDir(dirPath, callback) : callback(path.join(dir, f));
    });
}

const targetDir = path.join(__dirname, 'src');
const results = [];

walkDir(targetDir, function(filePath) {
    if (!filePath.endsWith('.ts') && !filePath.endsWith('.tsx')) return;
    
    const code = fs.readFileSync(filePath, 'utf-8');
    const sourceFile = ts.createSourceFile(
        filePath,
        code,
        ts.ScriptTarget.Latest,
        true
    );

    // Group by component or function
    // For simplicity we will collect by block or just file level if there's multiple FSM-like states
    
    let booleansFound = [];
    
    function visit(node) {
        if (ts.isCallExpression(node)) {
            // Check if it's a useState call
            if (node.expression && node.expression.getText(sourceFile) === 'useState') {
                const parent = node.parent;
                if (parent && ts.isVariableDeclaration(parent)) {
                    if (ts.isArrayBindingPattern(parent.name)) {
                        const elem = parent.name.elements[0];
                        if (elem && elem.name) {
                            const stateVar = elem.name.getText(sourceFile);
                            
                            let isBoolean = false;
                            if (node.arguments.length > 0) {
                                const arg = node.arguments[0];
                                if (arg.kind === ts.SyntaxKind.TrueKeyword || arg.kind === ts.SyntaxKind.FalseKeyword) {
                                    isBoolean = true;
                                }
                            } else if (node.typeArguments && node.typeArguments.length > 0) {
                                if (node.typeArguments[0].kind === ts.SyntaxKind.BooleanKeyword) {
                                    isBoolean = true;
                                }
                            }
                            
                            if (isBoolean || stateVar.startsWith('is') || stateVar.startsWith('has')) {
                                booleansFound.push({ 
                                    name: stateVar, 
                                    line: sourceFile.getLineAndCharacterOfPosition(node.getStart(sourceFile)).line + 1,
                                    text: parent.getText(sourceFile)
                                });
                            }
                        }
                    }
                }
            }
        }
        ts.forEachChild(node, visit);
    }
    
    visit(sourceFile);
    
    const stateRelatedBooleans = booleansFound.filter(b => 
        /^(is|has)(Loading|Error|Success|Saving|Fetching|Loaded|Ready|Failed)/.test(b.name)
    );
    
    if (stateRelatedBooleans.length >= 2) {
        results.push({
            file: filePath.replace(__dirname + '\\', '').replace(/\\/g, '/'),
            states: stateRelatedBooleans,
            snippet: stateRelatedBooleans.map(b => b.text).join('; ')
        });
    }
});

console.log(JSON.stringify(results, null, 2));
