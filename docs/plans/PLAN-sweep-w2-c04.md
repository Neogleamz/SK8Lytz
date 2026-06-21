# Implementation Plan

## Objective
Address cluster C04 (DevOps & Tooling lint errors) by fixing path misalignments, hardcoded system-specific paths, and performance bottlenecks in tooling scripts.

## Files to Create/Modify
- `tools/sentinel/pm_refinement.py`
- `tools/sentinel/ast_knowledge_compiler.js`
- `tools/sentinel/protocol_fuzzer.py`
- `tools/ninja-patcher-daemon.ps1`
- `tools/clean-native.ps1`
- `.husky/pre-commit`
- `tools/validate-plugins.ps1`
- `tools/extract_payloads.py`
- `tools/sentinel/layout_auditor.py`
- `tools/sentinel/google_antigravity.py`

## Step-by-Step Instructions
1. **`tools/sentinel/pm_refinement.py`**: Update `bucket_list_path` and `plans_dir` to point to `docs` and `docs/plans` instead of `tools` and `plans`.
2. **`tools/sentinel/ast_knowledge_compiler.js`**: Update `zenggeBible` and `masterRef` paths to point to `docs/` instead of `tools/`.
3. **`tools/sentinel/protocol_fuzzer.py`**: Update `bible_path` and `master_ref_path` to point to `docs/` instead of `tools/`.
4. **`tools/ninja-patcher-daemon.ps1`**: Increase `Start-Sleep` duration to 10 seconds or optimize `Get-ChildItem` to avoid an infinite loop of recursive scanning every 50ms.
5. **`tools/clean-native.ps1`**: Avoid a recursive `Get-ChildItem` over the entire `node_modules` directory just to touch file timestamps, targeting only the necessary file or avoiding recursion.
6. **`.husky/pre-commit`**: Change the spaces-in-path iteration for `$STAGED_FILES` (e.g. use `git diff --cached --name-only -z | while read -d '' FILE`) or simply set `IFS=$'\n'`.
7. **`tools/validate-plugins.ps1`**: Replace hardcoded `C:\Users\Magma` with `Join-Path $env:USERPROFILE`.
8. **`tools/extract_payloads.py`**: Replace hardcoded `C:\Users\Magma\.gemini\...` path with a relative path or an environment variable.
9. **`tools/sentinel/layout_auditor.py`**: Avoid hardcoded adb path.
10. **`tools/sentinel/google_antigravity.py`**: Avoid hardcoded `C:\Neogleamz\...` path.

## Out of Scope
Any other files or general refactoring outside of these specific lint findings.
