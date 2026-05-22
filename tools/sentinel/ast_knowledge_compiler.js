#!/usr/bin/env node
/**
 * ast_knowledge_compiler.js
 * 
 * Auto-Compiling Bibles Sentinel Engine.
 * Parses the TypeScript AST of ZenggeProtocol.ts using the TS Compiler API and synchronizes
 * its hardware constants, UUIDs, and constraints directly into the markdown bibles.
 * 
 * Satisfies the Dependency Diet Protocol (uses existing project TS dependency).
 */

const fs = require('fs');
const path = require('path');
const ts = require('typescript');

function log(msg) {
  console.log(`[AST-COMPILER] ${msg}`);
}

// 1. AST Parser Engine
function parseZenggeProtocol(filePath) {
  log(`Reading ${path.basename(filePath)}...`);
  const sourceCode = fs.readFileSync(filePath, 'utf8');
  const sourceFile = ts.createSourceFile(filePath, sourceCode, ts.ScriptTarget.Latest, true);

  const constants = {
    uuids: {},
    icTypes: {},
    colorSorting: {},
    hwConstraints: {}
  };

  function visitor(node) {
    if (ts.isVariableStatement(node)) {
      node.declarationList.declarations.forEach(decl => {
        const name = decl.name.text;
        
        // Extract UUIDs
        if (name === 'ZENGGE_SERVICE_UUID' || name === 'ZENGGE_CHARACTERISTIC_UUID' || name === 'ZENGGE_NOTIFY_UUID') {
          if (decl.initializer && ts.isStringLiteral(decl.initializer)) {
            constants.uuids[name] = decl.initializer.text;
          }
        } 
        
        // Extract IC Chip Types Map & Color Sorting Map
        else if (name === 'IC_TYPES' || name === 'COLOR_SORTING_RGB') {
          if (decl.initializer && ts.isObjectLiteralExpression(decl.initializer)) {
            const map = name === 'IC_TYPES' ? constants.icTypes : constants.colorSorting;
            decl.initializer.properties.forEach(prop => {
              if (ts.isPropertyAssignment(prop)) {
                // Get clean key removing string quotes
                const key = prop.name.getText(sourceFile).replace(/['"]/g, '').trim();
                let value = '';
                if (ts.isStringLiteral(prop.initializer)) {
                  value = prop.initializer.text;
                } else {
                  value = prop.initializer.getText(sourceFile).replace(/['"]/g, '').trim();
                }
                map[key] = value;
              }
            });
          }
        } 
        
        // Extract Hardware Constraints
        else if (name === 'HW_CONSTRAINTS') {
          if (decl.initializer && ts.isObjectLiteralExpression(decl.initializer)) {
            decl.initializer.properties.forEach(prop => {
              if (ts.isPropertyAssignment(prop)) {
                const key = prop.name.getText(sourceFile).replace(/['"]/g, '').trim();
                const valueText = prop.initializer.getText(sourceFile);
                constants.hwConstraints[key] = valueText;
              }
            });
          }
        }
      });
    }
    ts.forEachChild(node, visitor);
  }

  ts.forEachChild(sourceFile, visitor);
  return constants;
}

// 2. Markdown Markdown Compiler
function generateMarkdownBlock(constants) {
  let md = `#### 📝 Auto-Compiled Zengge Protocol Constants (AST Compiler)

##### 🔌 BLE UUIDs
- **Service UUID**: \`${constants.uuids.ZENGGE_SERVICE_UUID || 'Unknown'}\` (\`ZENGGE_SERVICE_UUID\`)
- **Write Characteristic UUID**: \`${constants.uuids.ZENGGE_CHARACTERISTIC_UUID || 'Unknown'}\` (\`ZENGGE_CHARACTERISTIC_UUID\`)
- **Notification Characteristic UUID**: \`${constants.uuids.ZENGGE_NOTIFY_UUID || 'Unknown'}\` (\`ZENGGE_NOTIFY_UUID\`)

##### 🛠️ Hardware Constraints
| Constraint | Value | Description |
|:---|:---:|:---|
| \`maxPoints\` | ${constants.hwConstraints.maxPoints || 'Unknown'} | Maximum addressable points per segment |
| \`maxSegments\` | ${constants.hwConstraints.maxSegments || 'Unknown'} | Maximum physical segment duplicates |
| \`maxPxS\` | ${constants.hwConstraints.maxPxS || 'Unknown'} | Max points * segments limit |
| \`maxMicPoints\` | ${constants.hwConstraints.maxMicPoints || 'Unknown'} | Maximum points when microphone is active |
| \`maxMicPxS\` | ${constants.hwConstraints.maxMicPxS || 'Unknown'} | Max micPoints * micSegments limit |
| \`defaultPoints\` | ${constants.hwConstraints.defaultPoints || 'Unknown'} | Fallback default point count |
| \`defaultSegments\` | ${constants.hwConstraints.defaultSegments || 'Unknown'} | Fallback default segment count |

##### 📟 IC Chip Types (\`IC_TYPES\`)
| Key | Chip Type |
|:---:|:---|
${Object.entries(constants.icTypes).map(([k, v]) => `| ${k} | ${v} |`).join('\n')}

##### 🎨 Color Sorting RGB (\`COLOR_SORTING_RGB\`)
| Key | RGB Order |
|:---:|:---|
${Object.entries(constants.colorSorting).map(([k, v]) => `| ${k} | ${v} |`).join('\n')}
`;
  return md;
}

// 3. Document Synchronizer
function syncMarkdownFile(filePath, markdownBlock) {
  if (!fs.existsSync(filePath)) {
    log(`[ERROR] Markdown file does not exist: ${filePath}`);
    return false;
  }

  log(`Synchronizing ${path.basename(filePath)}...`);
  let content = fs.readFileSync(filePath, 'utf8');

  const startMarker = '<!-- AST_COMPILER_START: ZENGGE_CONSTANTS -->';
  const endMarker = '<!-- AST_COMPILER_END: ZENGGE_CONSTANTS -->';

  const startIdx = content.indexOf(startMarker);
  const endIdx = content.indexOf(endMarker);

  if (startIdx === -1 || endIdx === -1) {
    log(`[ERROR] Could not find AST_COMPILER markers in ${path.basename(filePath)}`);
    return false;
  }

  // Preserve the markers themselves
  const updatedContent = 
    content.substring(0, startIdx + startMarker.length) + 
    '\n' + markdownBlock + '\n' + 
    content.substring(endIdx);

  fs.writeFileSync(filePath, updatedContent, 'utf8');
  log(`Successfully updated ${path.basename(filePath)}!`);
  return true;
}

function main() {
  const root = path.resolve(__dirname, '../..');
  const protocolFile = path.join(root, 'src/protocols/ZenggeProtocol.ts');
  const zenggeBible = path.join(root, 'tools/ZENGGE_PROTOCOL_BIBLE.md');
  const masterRef = path.join(root, 'tools/SK8Lytz_App_Master_Reference.md');

  try {
    const constants = parseZenggeProtocol(protocolFile);
    
    // Verify we actually parsed something
    if (!constants.uuids.ZENGGE_SERVICE_UUID) {
      throw new Error("Failed to parse essential UUIDs from ZenggeProtocol.ts");
    }

    log("Compiling AST data into markdown tables...");
    const mdBlock = generateMarkdownBlock(constants);

    const bibleSuccess = syncMarkdownFile(zenggeBible, mdBlock);
    const refSuccess = syncMarkdownFile(masterRef, mdBlock);

    if (bibleSuccess && refSuccess) {
      log("🎉 Closed-loop AST synchronization completed successfully!");
      process.exit(0);
    } else {
      log("❌ Synchronization partially or completely failed.");
      process.exit(1);
    }
  } catch (e) {
    console.error(`[ERROR] AST compilation process crashed:`, e);
    process.exit(1);
  }
}

main();
