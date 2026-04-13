# ⚡ Flash-Executable Implementation Plan: feat/eula-in-account-manager

> **WARNING TO AUTHOR (THINK MODEL)**: This plan is designed to be executed blindly by a `[🤖 FLASH]` or pure execution model in a future session. 
> Do NOT use line numbers (`Replace lines 45-50`). The codebase may have drifted between plan creation and execution.
> You MUST use **Semantic Anchors** (e.g. `Find the entire useBLE hook`, `Search for the exact phrase: return <View>`).
> All code snippets must be 100% complete, fully typed, and ready to be copy-pasted via the `replace_file_content` tool.

---

## 1. Pre-Flight Context Check (Drift Verification)

Before executing any file modifications, execute the following strict checks using `view_file` or `grep_search`. Do NOT guess if the file looks different.

*   [ ] **Check 1:** Open `src/components/AccountModal.tsx`. Search for semantic anchor: `const [showNewPwd, setShowNewPwd] = useState(false);`. 
    *   *Expected state:* We will add our local state `const [showEula, setShowEula] = useState(false);` near here.
    *   *Abort Condition:* If the state declarations have been fundamentally moved or refactored, **HALT**.
*   [ ] **Check 2:** Search for semantic anchor: `{/* App preferences */}` inside `AccountModal.tsx`.
    *   *Expected state:* It should exist in the `renderSettings` function.

---

## 2. Step-by-Step Execution Strict Instructions

### Step 2.1: Inject EULA State and Import
- **Target File:** `src/components/AccountModal.tsx`
- **Semantic Anchor / Target Content:** Find the imports to ensure `EulaModal` is imported. If it isn't, import it.
- **Action:** Add `<EulaModal visible={showEula} onAccept={() => setShowEula(false)} onDecline={() => setShowEula(false)} isViewOnly={true} />` and its state to `AccountModal.tsx`.

We need three `multi_replace_file_content` replacements:
1. Import `EulaModal` at the top.
2. Add `const [showEula, setShowEula] = useState(false);` after `const [showNewPwd, setShowNewPwd] = useState(false);`.
3. Add a "LEGAL" section under `renderSettings` to trigger `showEula`.

**Replacement 1: Add state**
```typescript
  const [showNewPwd, setShowNewPwd] = useState(false);
  const [showEula, setShowEula] = useState(false);
```

**Replacement 2: Add trigger in renderSettings**
- Target Content:
```typescript
      {/* Sign out */}
      <Text style={[styles.sectionHeader, { marginTop: 24 }]}>ACCOUNT</Text>
```
- Replacement Content:
```typescript
      {/* Legal & Compliance */}
      <Text style={[styles.sectionHeader, { marginTop: 24 }]}>LEGAL</Text>

      <TouchableOpacity style={styles.signOutBtn} onPress={() => setShowEula(true)}>
        <MaterialCommunityIcons name="file-document-outline" size={18} color={Colors.textMuted} />
        <Text style={[styles.signOutText, { color: Colors.text }]}>Review EULA</Text>
      </TouchableOpacity>

      {/* Sign out */}
      <Text style={[styles.sectionHeader, { marginTop: 24 }]}>ACCOUNT</Text>
```

**Replacement 3: Add EulaModal to JSX**
- Target Content:
```typescript
    <Modal visible={visible} animationType="slide" transparent statusBarTranslucent>
      <KeyboardAvoidingView
        style={styles.overlay}
```
- Replacement Content:
```typescript
    <Modal visible={visible} animationType="slide" transparent statusBarTranslucent>
      {showEula && (
        <EulaModal 
          visible={showEula} 
          onAccept={() => setShowEula(false)} 
          onDecline={() => setShowEula(false)} 
          isViewOnly={true} 
        />
      )}
      <KeyboardAvoidingView
        style={styles.overlay}
```

---

## 3. Post-Execution Verification

*   [ ] **Command:** `npx tsc --noEmit`
    *   *Expected Output:* Clean exit (0 errors) relating to the modified files.
*   [ ] **Manual Step:** Ask user to load `localhost:8081`, open the Account Modal -> Settings Tab, and press "Review EULA" to make sure it functions without crashing.

---

**Completion:** Once all checks pass, proceed to Commit Phase using the semantic message: `feat(ui): add EULA review link to Account Manager settings`
