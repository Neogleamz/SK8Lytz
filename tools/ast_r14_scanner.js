const fs = require('fs');
const path = require('path');
const ts = require('typescript');

const srcPath = 'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src';
const findings = [];
let findingIdCounter = 1;
let filesScanned = 0;

function walk(dir, callback) {
    fs.readdirSync(dir).forEach(f => {
        let dirPath = path.join(dir, f);
        let isDirectory = fs.statSync(dirPath).isDirectory();
        isDirectory ? walk(dirPath, callback) : callback(path.join(dir, f));
    });
}

function analyzeFile(filePath) {
    // R-14 UI States only apply to React components, which should be in .tsx files
    if (!filePath.endsWith('.tsx')) return;
    filesScanned++;
    const code = fs.readFileSync(filePath, 'utf-8');
    const sourceFile = ts.createSourceFile(
        filePath,
        code,
        ts.ScriptTarget.Latest,
        true
    );

    let hasDataFetching = false;
    let hasLoadingState = false;
    let hasErrorState = false;
    let hasEmptyState = false;
    let fetchLine = 0;
    
    function visit(node) {
        if (ts.isCallExpression(node)) {
            const text = node.expression.getText(sourceFile);
            if (text.includes('supabase') || text.includes('useQuery') || text === 'fetch') {
                hasDataFetching = true;
                if (!fetchLine) {
                    fetchLine = sourceFile.getLineAndCharacterOfPosition(node.getStart()).line + 1;
                }
            }
        }
        
        if (ts.isIdentifier(node)) {
            const text = node.text.toLowerCase();
            if (text.includes('loading') || text.includes('activityindicator')) hasLoadingState = true;
            if (text.includes('error')) hasErrorState = true;
            if (text.includes('empty')) hasEmptyState = true;
        }
        
        // Let's also check string literals (for "Loading...", etc)
        if (ts.isStringLiteral(node) || ts.isNoSubstitutionTemplateLiteral(node)) {
            const text = node.text.toLowerCase();
            if (text.includes('loading')) hasLoadingState = true;
            if (text.includes('error')) hasErrorState = true;
            if (text.includes('empty') || text.includes('no data') || text.includes('no items')) hasEmptyState = true;
        }

        ts.forEachChild(node, visit);
    }
    visit(sourceFile);

    if (hasDataFetching) {
        if (!hasLoadingState || !hasErrorState || !hasEmptyState) {
            let missing = [];
            if (!hasLoadingState) missing.push("Loading");
            if (!hasErrorState) missing.push("Error");
            if (!hasEmptyState) missing.push("Empty");
            
            const lines = code.split('\n');
            const snippet = lines.slice(Math.max(0, fetchLine - 2), fetchLine + 2).join('\n');
            
            const fpRisk = missing.length === 3 ? "LOW" : "MEDIUM";
            
            findings.push({
                id: `R-14-${String(findingIdCounter).padStart(3, '0')}`,
                file: filePath.replace(/\\/g, '/').replace('C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/', ''),
                line: fetchLine,
                rule_violated: "R-14",
                severity: "HIGH",
                description: `Missing UI states for data fetching: ${missing.join(', ')}`,
                code_snippet: snippet,
                suggested_fix: `Implement ${missing.join(', ')} states in the component matrix.`,
                false_positive_risk: fpRisk
            });
            findingIdCounter++;
        }
    }
}

walk(srcPath, analyzeFile);

// Ensure directory exists
const outDir = 'C:/Users/Magma/.gemini/antigravity/brain/148744d3-1074-4373-86a4-b96867c0b71d/deepdive_raw';
if (!fs.existsSync(outDir)) {
    fs.mkdirSync(outDir, { recursive: true });
}

const result = {
    agent_id: "R-14",
    agent_type: "sniper",
    domain_or_rule: "R-14",
    files_scanned: filesScanned,
    completion_status: "COMPLETE",
    error_details: null,
    findings: findings,
    summary: {
        total_findings: findings.length,
        high: findings.length,
        medium: 0,
        low: 0,
        false_positives_noted: findings.filter(f => f.false_positive_risk !== "LOW").length
    }
};

fs.writeFileSync(path.join(outDir, 'R-14_findings.json'), JSON.stringify(result, null, 2));
console.log('Done!');
