const fs = require('fs');
const path = require('path');
const parser = require('@babel/parser');
const traverse = require('@babel/traverse').default;

const SRC_DIR = path.join(__dirname, '../src');

function getAllFiles(dirPath, arrayOfFiles) {
    const files = fs.readdirSync(dirPath);
    arrayOfFiles = arrayOfFiles || [];

    files.forEach(function(file) {
        if (fs.statSync(dirPath + "/" + file).isDirectory()) {
            arrayOfFiles = getAllFiles(dirPath + "/" + file, arrayOfFiles);
        } else {
            if (file.endsWith('.ts') || file.endsWith('.tsx') || file.endsWith('.js') || file.endsWith('.jsx')) {
                arrayOfFiles.push(path.join(dirPath, file));
            }
        }
    });

    return arrayOfFiles;
}

const files = getAllFiles(SRC_DIR);
const findings = [];
let findingId = 1;

files.forEach(file => {
    const code = fs.readFileSync(file, 'utf8');
    try {
        const ast = parser.parse(code, {
            sourceType: 'module',
            plugins: ['typescript', 'jsx', 'classProperties', 'decorators-legacy'],
        });

        traverse(ast, {
            CallExpression(astPath) {
                try {
                    const callee = astPath.node.callee;
                    if (callee.type === 'MemberExpression' && callee.property.type === 'Identifier' && callee.property.name === 'error') {
                        const objectName = callee.object.name;
                        if (['console', 'AppLogger', 'Logger', 'logger'].includes(objectName)) {
                            // Check arguments
                            let hasPayloadSize = false;
                            let hasSsi = false;
                            
                            astPath.node.arguments.forEach(arg => {
                                if (arg.type === 'ObjectExpression') {
                                    arg.properties.forEach(prop => {
                                        if (prop.key && prop.key.type === 'Identifier') {
                                            if (prop.key.name === 'payload_size') hasPayloadSize = true;
                                            if (prop.key.name === 'ssi') hasSsi = true;
                                        }
                                        if (prop.key && prop.key.type === 'StringLiteral') {
                                            if (prop.key.value === 'payload_size') hasPayloadSize = true;
                                            if (prop.key.value === 'ssi') hasSsi = true;
                                        }
                                    });
                                }
                            });

                            if (!hasPayloadSize || !hasSsi) {
                                const relFile = file.replace(path.join(__dirname, '../'), '').replace(/\\/g, '/');
                                const line = astPath.node.loc.start.line;
                                const start = astPath.node.start;
                                const end = astPath.node.end;
                                const snippet = code.substring(start, end).replace(/\n/g, ' ');
                                findings.push({
                                    id: `R-04-${String(findingId).padStart(3, '0')}`,
                                    file: relFile,
                                    line: line,
                                    rule_violated: 'R-04',
                                    severity: 'MEDIUM',
                                    description: `Error logged via ${objectName}.error without telemetry context (missing payload_size or ssi).`,
                                    code_snippet: snippet.length > 200 ? snippet.substring(0, 197) + '...' : snippet,
                                    suggested_fix: `Add { payload_size: number, ssi: number } to the error arguments context object.`,
                                    false_positive_risk: 'LOW'
                                });
                                findingId++;
                            }
                        }
                    }
                } catch (innerE) {
                    console.error("Inner error:", innerE);
                }
            }
        });
    } catch (e) {
        console.error(`Error parsing ${file}`, e.message);
    }
});

const output = {
    agent_id: "R-04",
    agent_type: "sniper",
    domain_or_rule: "Telemetry Context",
    files_scanned: files.length,
    completion_status: "COMPLETE",
    error_details: null,
    findings: findings,
    summary: {
        total_findings: findings.length,
        high: 0,
        medium: findings.length,
        low: 0,
        false_positives_noted: 0
    }
};

fs.writeFileSync(path.join(__dirname, 'r04_results.json'), JSON.stringify(output, null, 2));
console.log(`Scan complete. Found ${findings.length} violations.`);
