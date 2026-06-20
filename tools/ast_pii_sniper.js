const fs = require('fs');
const path = require('path');
const ts = require('typescript');

const rootDir = path.join(__dirname, '../src');
const results = [];

function walk(dir) {
    let files = fs.readdirSync(dir);
    for (let file of files) {
        let fullPath = path.join(dir, file);
        if (fs.statSync(fullPath).isDirectory()) {
            walk(fullPath);
        } else if (fullPath.endsWith('.ts') || fullPath.endsWith('.tsx')) {
            analyzeFile(fullPath);
        }
    }
}

function analyzeFile(filePath) {
    const code = fs.readFileSync(filePath, 'utf-8');
    const sourceFile = ts.createSourceFile(
        filePath,
        code,
        ts.ScriptTarget.Latest,
        true
    );

    function isScrubbed(node) {
        if (ts.isCallExpression(node)) {
            if (ts.isIdentifier(node.expression) && node.expression.text === 'scrubPII') {
                return true;
            }
        }
        // If it's a template literal containing scrubPII, we'll consider it safe or just check its expression
        return false;
    }

    function checkProperties(properties) {
        for (const prop of properties) {
            if (ts.isPropertyAssignment(prop)) {
                let propName = '';
                if (ts.isIdentifier(prop.name)) {
                    propName = prop.name.text;
                } else if (ts.isStringLiteral(prop.name)) {
                    propName = prop.name.text;
                }
                
                const lowerName = propName.toLowerCase();
                if (lowerName.includes('mac') || lowerName.includes('email') || lowerName.includes('name') || lowerName === 'deviceid') {
                    // Check if the value is scrubbed
                    if (!isScrubbed(prop.initializer)) {
                        // Allow literal 'Unknown' or hardcoded strings without PII?
                        // Let's just flag it if it's an identifier or property access
                        if (ts.isStringLiteral(prop.initializer)) {
                            const val = prop.initializer.text.toLowerCase();
                            if (val === 'unknown' || val === 'default fleet') continue;
                        }

                        const line = sourceFile.getLineAndCharacterOfPosition(prop.getStart()).line + 1;
                        results.push({
                            file: filePath,
                            line: line,
                            property: propName,
                            code: prop.getText().replace(/\r?\n|\r/g, ' ')
                        });
                    }
                }
            } else if (ts.isShorthandPropertyAssignment(prop)) {
                const propName = prop.name.text;
                const lowerName = propName.toLowerCase();
                if (lowerName.includes('mac') || lowerName.includes('email') || lowerName.includes('name') || lowerName === 'deviceid') {
                    const line = sourceFile.getLineAndCharacterOfPosition(prop.getStart()).line + 1;
                    results.push({
                        file: filePath,
                        line: line,
                        property: propName,
                        code: prop.getText().replace(/\r?\n|\r/g, ' ')
                    });
                }
            }
        }
    }

    function checkTemplateLiteral(node) {
        // If AppLogger is passed a template string, e.g., AppLogger.log(`Name: ${user.name}`)
        // That's harder, but let's check for expressions in the template span
        if (ts.isTemplateExpression(node)) {
            for (const span of node.templateSpans) {
                if (!isScrubbed(span.expression)) {
                    // Just flag it to be safe if the text contains mac/name/email
                    const fullText = node.getText().toLowerCase();
                    if (fullText.includes('mac') || fullText.includes('name') || fullText.includes('email')) {
                        // Rough check
                    }
                }
            }
        }
    }

    function visit(node) {
        if (ts.isCallExpression(node)) {
            let isAppLogger = false;
            if (ts.isPropertyAccessExpression(node.expression)) {
                if (ts.isIdentifier(node.expression.expression) && node.expression.expression.text === 'AppLogger') {
                    isAppLogger = true;
                }
            }
            
            if (isAppLogger) {
                // Check all arguments
                for (const arg of node.arguments) {
                    if (ts.isObjectLiteralExpression(arg)) {
                        checkProperties(arg.properties);
                    } else if (ts.isTemplateExpression(arg) || ts.isStringLiteral(arg)) {
                        // Some logs might be just strings AppLogger.info(`email: ${email}`)
                        const text = arg.getText().toLowerCase();
                        if ((text.includes('mac') || text.includes('email') || text.includes('name')) && !text.includes('scrubpii')) {
                            const line = sourceFile.getLineAndCharacterOfPosition(arg.getStart()).line + 1;
                            results.push({
                                file: filePath,
                                line: line,
                                property: 'template_or_string',
                                code: arg.getText().replace(/\r?\n|\r/g, ' ')
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

walk(rootDir);
console.log(JSON.stringify(results, null, 2));
