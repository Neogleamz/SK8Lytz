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

function runStaticOP59Checks(srcDir) {
  log("Commencing Static Code Quality Guards for OP_0x59...");
  const violations = [];

  function getFiles(dir) {
    let results = [];
    if (!fs.existsSync(dir)) return results;
    const list = fs.readdirSync(dir);
    list.forEach(file => {
      file = path.join(dir, file);
      const stat = fs.statSync(file);
      if (stat && stat.isDirectory()) {
        results = results.concat(getFiles(file));
      } else if ((file.endsWith('.ts') || file.endsWith('.tsx')) && !file.includes('__tests__') && !file.endsWith('.test.ts') && !file.endsWith('.test.tsx')) {
        results.push(file);
      }
    });
    return results;
  }

  const files = getFiles(srcDir);
  log(`Found ${files.length} TypeScript files to scan.`);

  files.forEach(filePath => {
    const sourceCode = fs.readFileSync(filePath, 'utf8');
    const sourceFile = ts.createSourceFile(filePath, sourceCode, ts.ScriptTarget.Latest, true);

    function getNumericValue(element, sourceFile) {
      const rawText = element.getText(sourceFile).trim();
      if (rawText.startsWith('0x') || rawText.startsWith('0X')) {
        return parseInt(rawText, 16);
      }
      if (ts.isNumericLiteral(element)) {
        const text = element.text.trim();
        if (text.startsWith('0x') || text.startsWith('0X')) {
          return parseInt(text, 16);
        }
        return parseInt(text, 10);
      }
      return parseInt(rawText, 10);
    }

    function findArrayLiteralLengthForIdentifier(sourceFile, identifierText) {
      let length = null;
      function findDecl(node) {
        if (ts.isVariableDeclaration(node) && node.name.getText(sourceFile) === identifierText) {
          if (node.initializer && ts.isArrayLiteralExpression(node.initializer)) {
            length = node.initializer.elements.length;
          }
        }
        ts.forEachChild(node, findDecl);
      }
      ts.forEachChild(sourceFile, findDecl);
      return length;
    }

    function getLineAndChar(node, sourceFile) {
      const { line, character } = sourceFile.getLineAndCharacterOfPosition(node.getStart(sourceFile));
      return { line: line + 1, character: character + 1 };
    }

    function visitor(node) {
      // 1. Check call expressions
      if (ts.isCallExpression(node)) {
        let methodName = '';
        if (ts.isIdentifier(node.expression)) {
          methodName = node.expression.text;
        } else if (ts.isPropertyAccessExpression(node.expression)) {
          methodName = node.expression.name.text;
        }

        if (methodName === 'setMultiColor' || methodName === 'writeColorArray' || methodName === 'dispatchStaticColorful') {
          if (node.arguments.length > 0) {
            const firstArg = node.arguments[0];
            let length = null;
            let resolvedType = '';

            if (ts.isArrayLiteralExpression(firstArg)) {
              length = firstArg.elements.length;
              resolvedType = 'ArrayLiteral';
            } else if (ts.isIdentifier(firstArg)) {
              const varName = firstArg.text;
              length = findArrayLiteralLengthForIdentifier(sourceFile, varName);
              resolvedType = `VariableReference(${varName})`;
            }

            if (length !== null) {
              if (length > 1 && length < 12) {
                const { line, character } = getLineAndChar(node, sourceFile);
                violations.push({
                  file: filePath,
                  line,
                  character,
                  msg: `Unsafe color array dispatch! Method '${methodName}' called with array of length ${length} via ${resolvedType}. Minimum length is 12 (length 1 is allowed for padded solid mode). Potential physical controller EEPROM buffer lockout risk!`
                });
              }
            }
          }
        }
      }

      // 2. Check raw array literals
      if (ts.isArrayLiteralExpression(node)) {
        const elements = node.elements;
        if (elements.length > 0) {
          const val0 = getNumericValue(elements[0], sourceFile);
          if (val0 === 89) { // 0x59
            if (elements.length < 45) {
              const { line, character } = getLineAndChar(node, sourceFile);
              violations.push({
                file: filePath,
                line,
                character,
                msg: `Unsafe raw OP_0x59 payload array! Length is ${elements.length} (< 45 bytes). Minimum length is 45 bytes (12 RGB pixels + 9 bytes header). Potential physical controller EEPROM buffer lockout risk!`
              });
            }
          } else if (val0 === 0 && elements.length > 8) {
            const val8 = getNumericValue(elements[8], sourceFile);
            if (val8 === 89) {
              if (elements.length < 45) {
                const { line, character } = getLineAndChar(node, sourceFile);
                violations.push({
                  file: filePath,
                  line,
                  character,
                  msg: `Unsafe raw wrapped OP_0x59 payload array! Length is ${elements.length} (< 45 bytes). Minimum length is 45 bytes. Potential physical controller EEPROM buffer lockout risk!`
                });
              }
            }
          }
        }
      }

      ts.forEachChild(node, visitor);
    }

    ts.forEachChild(sourceFile, visitor);
  });

  return violations;
}

function main() {
  const root = path.resolve(__dirname, '../..');
  const srcDir = path.join(root, 'src');
  const protocolFile = path.join(root, 'src/protocols/ZenggeProtocol.ts');
  const zenggeBible = path.join(root, 'tools/ZENGGE_PROTOCOL_BIBLE.md');
  const masterRef = path.join(root, 'tools/SK8Lytz_App_Master_Reference.md');

  const isCheckMode = process.argv.includes('--check');

  if (isCheckMode) {
    try {
      const violations = runStaticOP59Checks(srcDir);
      if (violations.length > 0) {
        console.error(`\n🚨 [STATIC-OP59-GUARD] VIOLATIONS DETECTED (${violations.length}):`);
        violations.forEach(v => {
          console.error(`  ❌ ${path.relative(root, v.file)}:${v.line}:${v.character} - ${v.msg}`);
        });
        console.error("\n👉 Please fix the color array lengths to prevent physical controller lockouts!\n");
        process.exit(1);
      }
      log("✅ Static Code Quality Guards for OP_0x59 passed cleanly!");
      process.exit(0);
    } catch (e) {
      console.error("[ERROR] Static guard run crashed:", e);
      process.exit(1);
    }
  }

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
