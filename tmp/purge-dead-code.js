"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var ts_morph_1 = require("ts-morph");
var project = new ts_morph_1.Project({
    tsConfigFilePath: "tsconfig.json",
});
// We must override the tsconfig to strictly catch unused locals and params
project.compilerOptions.set({
    noUnusedLocals: true,
    noUnusedParameters: true,
});
var sourceFiles = project.getSourceFiles("src/**/*.{ts,tsx}");
console.log("Analyzing ".concat(sourceFiles.length, " files for unused declarations..."));
var fixCount = 0;
for (var _i = 0, sourceFiles_1 = sourceFiles; _i < sourceFiles_1.length; _i++) {
    var sourceFile = sourceFiles_1[_i];
    // Organize imports automatically removes unused imports!
    var originalText = sourceFile.getFullText();
    sourceFile.organizeImports();
    if (sourceFile.getFullText() !== originalText) {
        fixCount++;
    }
    // Now deal with unused locals & parameters
    var diagnostics = sourceFile.getPreEmitDiagnostics();
    for (var _a = 0, diagnostics_1 = diagnostics; _a < diagnostics_1.length; _a++) {
        var diagnostic = diagnostics_1[_a];
        // TS6133: 'xyz' is declared but its value is never read.
        if (diagnostic.getCode() === 6133) {
            var node = diagnostic.getNode();
            if (!node)
                continue;
            var symbol = node.getSymbol();
            if (!symbol)
                continue;
            var name_1 = symbol.getName();
            // If and only if it's not already prefixed
            if (!name_1.startsWith("_")) {
                try {
                    // Attempt to rename the identifier to _name to satisfy the compiler
                    // This avoids completely deleting object properties, function signatures, etc.
                    // Wait, actually TS allows `_` to suppress unused parameter warnings if we set an option,
                    // but just prepending `_` and not using it is standard JS convention. 
                    // TypeScript still flags it unless we tell configuration to ignore underscores.
                    // Let's just try to safely remove Variable declarations.
                    var parent_1 = node.getParent();
                    if ((parent_1 === null || parent_1 === void 0 ? void 0 : parent_1.getKindName()) === 'VariableDeclaration') {
                        parent_1.remove();
                        fixCount++;
                    }
                }
                catch (err) {
                    console.warn("Could not auto-fix ".concat(name_1, " in ").concat(sourceFile.getBaseName()));
                }
            }
        }
    }
    sourceFile.saveSync();
}
console.log("Executed ".concat(fixCount, " AST autofixes."));
