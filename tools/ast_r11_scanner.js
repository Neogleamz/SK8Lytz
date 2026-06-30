const fs = require('fs');
const path = require('path');
const ts = require('typescript');

const srcPath = path.resolve(__dirname, '../src');
const findings = [];
let findingIdCounter = 1;
let filesScanned = 0;

function walk(dir, callback) {
    if (!fs.existsSync(dir)) return;
    fs.readdirSync(dir).forEach(f => {
        let dirPath = path.join(dir, f);
        let isDirectory = fs.statSync(dirPath).isDirectory();
        if (isDirectory) {
            walk(dirPath, callback);
        } else {
            callback(dirPath);
        }
    });
}

// Helper to check if a node has a TryStatement as ancestor before reaching function boundary
function isInsideTryCatch(node) {
    let current = node.parent;
    while (current) {
        if (ts.isTryStatement(current)) {
            return true;
        }
        // If we cross function boundaries, stop checking, as try-catch must be inside the same function
        if (
            ts.isFunctionDeclaration(current) ||
            ts.isFunctionExpression(current) ||
            ts.isArrowFunction(current) ||
            ts.isMethodDeclaration(current) ||
            ts.isConstructorDeclaration(current) ||
            ts.isGetAccessorDeclaration(current) ||
            ts.isSetAccessorDeclaration(current)
        ) {
            break;
        }
        current = current.parent;
    }
    return false;
}

// Check if a node (or its ancestors/sub-expressions) has a .catch call in its chain
function hasCatch(node) {
    let current = node;
    while (ts.isCallExpression(current)) {
        const expr = current.expression;
        if (ts.isPropertyAccessExpression(expr)) {
            if (expr.name.text === 'catch') {
                return true;
            }
            current = expr.expression;
        } else {
            break;
        }
    }
    return false;
}

// Check if expression is returned or assigned
function isReturnedOrAssigned(node) {
    let current = node;
    while (current && current.parent) {
        if (ts.isReturnStatement(current.parent)) {
            return true;
        }
        if (ts.isVariableDeclaration(current.parent) || ts.isBinaryExpression(current.parent)) {
            return true;
        }
        if (ts.isArrowFunction(current.parent) && current.parent.body === current) {
            // implicit return in arrow function
            return true;
        }
        if (ts.isPropertyAccessExpression(current.parent) || ts.isCallExpression(current.parent)) {
            current = current.parent;
        } else {
            break;
        }
    }
    return false;
}

// Check if a node is an intermediate promise in a chain (like the inner call of a .then/.catch chain)
function isIntermediatePromise(node) {
    if (node.parent && ts.isPropertyAccessExpression(node.parent) && node.parent.expression === node) {
        const name = node.parent.name.text;
        if (name === 'then' || name === 'catch' || name === 'finally') {
            return true;
        }
    }
    return false;
}

// Check if expression is direct IO
function isDirectIOExpression(expr) {
    if (ts.isCallExpression(expr)) {
        let current = expr.expression;
        while (ts.isCallExpression(current) || ts.isPropertyAccessExpression(current)) {
            if (ts.isPropertyAccessExpression(current)) {
                const obj = current.expression;
                if (ts.isIdentifier(obj)) {
                    const name = obj.text;
                    if (name === 'AsyncStorage' || name === 'supabase' || name === 'AppSettingsService') {
                        return true;
                    }
                }
                current = current.expression;
            } else if (ts.isCallExpression(current)) {
                current = current.expression;
            } else {
                break;
            }
        }
        if (ts.isIdentifier(current)) {
            const name = current.text;
            if (name === 'fetch' || name === 'writeToDevice' || name === '_executeWriteToDeviceInternal' || name === '_executeProtocolResultsInternal') {
                return true;
            }
        }
    } else if (ts.isPropertyAccessExpression(expr)) {
        let current = expr.expression;
        while (ts.isCallExpression(current) || ts.isPropertyAccessExpression(current)) {
            if (ts.isPropertyAccessExpression(current)) {
                const obj = current.expression;
                if (ts.isIdentifier(obj)) {
                    const name = obj.text;
                    if (name === 'AsyncStorage' || name === 'supabase' || name === 'AppSettingsService') {
                        return true;
                    }
                }
                current = current.expression;
            } else if (ts.isCallExpression(current)) {
                current = current.expression;
            } else {
                break;
            }
        }
        if (ts.isIdentifier(current)) {
            const name = current.text;
            if (name === 'AsyncStorage' || name === 'supabase' || name === 'AppSettingsService') {
                return true;
            }
        }
    } else if (ts.isIdentifier(expr)) {
        const name = expr.text;
        if (name === 'fetch' || name === 'writeToDevice' || name === 'AsyncStorage' || name === 'supabase') {
            return true;
        }
    }
    
    return false;
}

function analyzeFile(filePath) {
    if (!filePath.endsWith('.ts') && !filePath.endsWith('.tsx')) return;
    if (filePath.includes('__tests__') || filePath.includes('mock') || filePath.includes('.test.')) return;
    
    filesScanned++;
    const code = fs.readFileSync(filePath, 'utf-8');
    const sourceFile = ts.createSourceFile(
        filePath,
        code,
        ts.ScriptTarget.Latest,
        true
    );

    function visit(node) {
        // Case 1: AwaitExpression
        if (ts.isAwaitExpression(node)) {
            if (isDirectIOExpression(node.expression)) {
                if (!isInsideTryCatch(node) && !hasCatch(node.expression)) {
                    const lineInfo = sourceFile.getLineAndCharacterOfPosition(node.getStart());
                    const line = lineInfo.line + 1;
                    const lines = code.split('\n');
                    const snippet = lines.slice(Math.max(0, line - 2), line + 2).join('\n');
                    
                    findings.push({
                        id: `R-11-${String(findingIdCounter).padStart(3, '0')}`,
                        file: filePath.replace(/\\/g, '/').replace('C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/', ''),
                        line: line,
                        rule_violated: "R-11",
                        severity: "HIGH",
                        description: `Awaited promise for storage/network IO without try-catch block or chained .catch: "${node.getText(sourceFile)}"`,
                        code_snippet: snippet.trim(),
                        suggested_fix: `Wrap the call in a try-catch block or append .catch() to handle rejection.`,
                        false_positive_risk: "LOW"
                    });
                    findingIdCounter++;
                }
            }
        }

        // Case 2: Unawaited CallExpression (Floating promises for IO operations)
        if (ts.isCallExpression(node)) {
            if (isDirectIOExpression(node)) {
                // Check if this node is an intermediate call in a promise chain
                if (!isIntermediatePromise(node)) {
                    // Check if this node is inside an AwaitExpression
                    let isAwaited = false;
                    let parent = node.parent;
                    while (parent) {
                        if (ts.isAwaitExpression(parent)) {
                            isAwaited = true;
                            break;
                        }
                        if (
                            ts.isFunctionDeclaration(parent) ||
                            ts.isFunctionExpression(parent) ||
                            ts.isArrowFunction(parent) ||
                            ts.isMethodDeclaration(parent)
                        ) {
                            break;
                        }
                        parent = parent.parent;
                    }
                    
                    if (!isAwaited) {
                        const hasCatchBlock = hasCatch(node);
                        const returnedOrAssigned = isReturnedOrAssigned(node);
                        
                        if (!hasCatchBlock && !returnedOrAssigned) {
                            const lineInfo = sourceFile.getLineAndCharacterOfPosition(node.getStart());
                            const line = lineInfo.line + 1;
                            const lines = code.split('\n');
                            const snippet = lines.slice(Math.max(0, line - 2), line + 2).join('\n');
                            
                            findings.push({
                                id: `R-11-${String(findingIdCounter).padStart(3, '0')}`,
                                file: filePath.replace(/\\/g, '/').replace('C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/', ''),
                                line: line,
                                rule_violated: "R-11",
                                severity: "MEDIUM",
                                description: `Unawaited floating promise for storage/network IO without chained .catch: "${node.getText(sourceFile)}"`,
                                code_snippet: snippet.trim(),
                                suggested_fix: `Append .catch() or await/wrap with try-catch.`,
                                false_positive_risk: "LOW"
                            });
                            findingIdCounter++;
                        }
                    }
                }
            }
        }
        
        ts.forEachChild(node, visit);
    }
    visit(sourceFile);
}

walk(srcPath, analyzeFile);

console.log(`Scanned ${filesScanned} files. Found ${findings.length} findings.`);

const result = {
    agent_id: "R-11",
    agent_type: "sniper",
    domain_or_rule: "Promise/IO Safety",
    files_scanned: filesScanned,
    completion_status: "COMPLETE",
    error_details: null,
    findings: findings,
    summary: {
        total_findings: findings.length,
        high: findings.filter(f => f.severity === 'HIGH').length,
        medium: findings.filter(f => f.severity === 'MEDIUM').length,
        low: findings.filter(f => f.severity === 'LOW').length,
        false_positives_noted: 0
    }
};

fs.writeFileSync('C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/deepdive_raw/R-11_findings.json', JSON.stringify(result, null, 2));
console.log('Result written to C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/deepdive_raw/R-11_findings.json');
