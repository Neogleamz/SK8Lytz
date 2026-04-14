---
trigger: always_on
---

# Critical Safety & Security Priorities

**⛔ CRITICAL SAFETY RULE 1: Never edit, parse, or delete `.git/hooks/` files.**
**⛔ CRITICAL SAFETY RULE 2: Never push ANY changes to `master` without explicit user consent.**
**⛔ CRITICAL SAFETY RULE 3: Passphrase amnesia: never reuse an authorized passphrase outside its immediate contextual use.**
**⛔ CRITICAL SAFETY RULE 4: Always anchor your progress: run `git add .` + `git commit` to create local checkpoints after every discrete file edit. Immediately after creating a confirmed checkpoint, you MUST explicitly ask the user for permission to push to the remote repository.**
**⛔ CRITICAL SAFETY RULE 5: Keep `tools/SK8Lytz_Bucket_List.md` updated. It is now completely untracked locally (in .gitignore) to prevent branch drift. DO NOT try to stage or commit it via git.**

### Branching & Execute Workflow
1. Verify State (`git pull origin master`).
2. Quarantine modifications inside an isolated feature branch (`feature-[name]`, `fix-[name]`, or branch matching Bucketlist slug).
3. Test locally and ask the user to verify functionality before merging.
4. Only upon explicit approval, perform a local merge into `master` and push directly to the remote repository.

### Security Boundaries
1. **Zero Hardcoding**: NEVER hardcode API keys, DB URIs, passwords, or device MAC addresses in code.
2. Use `process.env.VARIABLE_NAME`. Add dummies to `.env.example`. Never touch the local `.env` file.
3. Automatically correct detected hardcoded secrets by extracting them into env logic.
