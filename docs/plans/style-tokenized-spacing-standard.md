# ⚡ Flash-Executable Implementation Plan: Tokenized Spacing Standard

> **WARNING TO AUTHOR (THINK MODEL)**: This plan is designed to be executed blindly by a `[🤖 FLASH]` or pure execution model in a future session.
> Do NOT use line numbers (`Replace lines 45-50`). The codebase may have drifted between plan creation and execution.
> You MUST use **Semantic Anchors**.
> All code snippets must be 100% complete, fully typed, and ready to be copy-pasted via the `replace_file_content` tool.

---

## 1. Pre-Flight Context Check (Drift Verification)

Before executing any file modifications, execute the following strict checks using `view_file` or `grep_search`. Do NOT guess if the file looks different.

- [ ] **Check 1:** Open `src/theme/theme.ts`. Search for semantic anchor: `export const Layout = {`.
  - _Expected state:_ The `Layout.padding` property should exist and be set to `14` or another numeric literal.
  - _Abort Condition:_ If `Spacing` is already exported, **HALT** and instruct the user: _"Codebase has drifted. This plan is stale and must be recompiled by a THINK model."_

---

## 2. Step-by-Step Execution Strict Instructions

### Step 2.1: Inject the Spacing Object into the Theme

- **Target File:** `src/theme/theme.ts`
- **Semantic Anchor / Target Content:** The `Layout` object declaration block.

**Exact Replacement Snippet for `src/theme/theme.ts`:**
Find this block:

```typescript
export const Layout = {
  padding: 14,
  borderRadius: 24,
};
```

Replace it with:

```typescript
export const Spacing = {
  xxs: 2,
  xs: 4,
  sm: 8,
  md: 12,
  lg: 16,
  xl: 24,
  xxl: 32,
  xxxl: 40,
  huge: 48,
  giant: 64,
};

export const Layout = {
  padding: Spacing.lg,
  borderRadius: Spacing.xl,
};
```

### Step 2.2: Create the Spacing Codemod Script

Because there are over 1000 instances of hardcoded margins and padding across 52 files, a manual replacement is impossible. You will use a specialized AST-regex replacement script.

- **Target File:** `tools/apply-spacing-codemod.mjs` (Create this file using `write_to_file` and `Overwrite: true`)

**Exact File Snippet:**

```javascript
import fs from "fs";
import path from "path";

const SRC_DIR = path.resolve("src");
const THEME_PATH = path.resolve("src/theme/theme.ts");

const spacingTokens = {
  0: "0",
  2: "Spacing.xxs",
  4: "Spacing.xs",
  6: "Spacing.sm",
  8: "Spacing.sm",
  10: "Spacing.md",
  12: "Spacing.md",
  14: "Spacing.lg",
  16: "Spacing.lg",
  18: "Spacing.lg",
  20: "Spacing.xl",
  24: "Spacing.xl",
  28: "Spacing.xxl",
  32: "Spacing.xxl",
  36: "Spacing.xxxl",
  40: "Spacing.xxxl",
  48: "Spacing.huge",
  64: "Spacing.giant",
};

function getNearestToken(val) {
  if (val === 0) return "0";
  let nearest = 8;
  let minDiff = Infinity;
  for (const k of Object.keys(spacingTokens).map(Number)) {
    if (k === 0) continue;
    const diff = Math.abs(k - val);
    if (diff < minDiff) {
      minDiff = diff;
      nearest = k;
    }
  }
  return spacingTokens[nearest];
}

function processFiles(dir) {
  const files = fs.readdirSync(dir);
  for (const file of files) {
    const fullPath = path.join(dir, file);
    if (fs.statSync(fullPath).isDirectory()) {
      processFiles(fullPath);
    } else if (fullPath.endsWith(".tsx") || fullPath.endsWith(".ts")) {
      if (fullPath === THEME_PATH) continue;

      let content = fs.readFileSync(fullPath, "utf8");
      let modified = false;

      // Regex to find margin, padding, gap values
      const regex =
        /(margin|padding|gap)(Top|Bottom|Left|Right|Horizontal|Vertical)?:\s*([0-9]+)\b/g;

      let matchCount = 0;
      content = content.replace(regex, (match, p1, p2, val) => {
        const num = parseInt(val, 10);
        if (num === 0 || num === 1) return match;

        const token = getNearestToken(num);
        matchCount++;
        modified = true;
        return `${p1}${p2 || ""}: ${token}`;
      });

      if (modified) {
        // Handle importing the new Spacing token
        const themeRegex =
          /import\s+{([^}]*)}\s+from\s+['"]([^'"]*theme\/theme)['"]/;
        const themeMatch = content.match(themeRegex);

        if (themeMatch) {
          if (!themeMatch[1].includes("Spacing")) {
            content = content.replace(
              themeRegex,
              `import { ${themeMatch[1].trim()}, Spacing } from '${themeMatch[2]}'`,
            );
          }
        } else {
          // If no existing theme import, insert a new relative import block at the end of the existing imports
          let relPath = path
            .relative(path.dirname(fullPath), THEME_PATH)
            .replace(/\\\\/g, "/")
            .replace(".ts", "");
          if (!relPath.startsWith(".")) relPath = "./" + relPath;

          const importStr = `import { Spacing } from '${relPath}';\n`;
          let importMatch = [...content.matchAll(/import\s+.*?;?\n/g)];
          if (importMatch.length > 0) {
            const lastImportIndex =
              importMatch[importMatch.length - 1].index +
              importMatch[importMatch.length - 1][0].length;
            content =
              content.slice(0, lastImportIndex) +
              importStr +
              content.slice(lastImportIndex);
          } else {
            content = importStr + content;
          }
        }

        fs.writeFileSync(fullPath, content, "utf8");
        console.log(
          `Replaced ${matchCount} usages in ${path.basename(fullPath)}`,
        );
      }
    }
  }
}

// Execute
processFiles(SRC_DIR);
console.log("Codemod execution completed successfully.");
```

### Step 2.3: Execute the Codemod

- **Command:** `node tools/apply-spacing-codemod.mjs`
- Use the `run_command` tool to execute this. Ensure no major terminal errors occur.

### Step 2.4: Clean Up Codemod

- **Command:** Use `run_command` to execute `rm tools/apply-spacing-codemod.mjs` to remove the temporary script.

---

## 3. Post-Execution Verification

- [ ] **Command:** `npx tsc --noEmit`
  - _Expected Output:_ The compiler should cleanly parse all files with no syntax errors related to the injected `Spacing` variable. Note: Pre-existing TS errors (`DashboardScreen` type mismatches) are acceptable and do not warrant a rollback.
- [ ] **Command:** `git diff`
  - Ask the user to review the diff to evaluate spacing changes.

---

**Completion:** Once all checks pass, proceed to Step 7 of the Bucket List:
`git add .`
`git commit -m "style(theme): enforce tokenized 8pt spacing grid app-wide via codemod"`
