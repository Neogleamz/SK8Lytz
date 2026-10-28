---
description: "Use whenever a plan or a change would add a package or library: it must earn its weight against a native alternative with a size, last-activity and need check, and the owner approves before it is ins…"
argument-hint: "[package name]"
---
<!-- generated from HQ playbook add-dep v1, do not edit. Run `npm run gen:playbooks`. -->

Arguments: $ARGUMENTS

# Add a dependency: native first, default no

**Role:** architect (the HQ persona; a Claude Code subagent of that name does the same job in isolation). **When to use:** Use whenever a plan or a change would add a package or library: it must earn its weight against a native alternative with a size, last-activity and need check, and the owner approves before it is installed.

## Steps
1. Knowledge first: search_brain and the KB (search for the library and its alternatives). A current entry gives you the facts; a stale one is noted in the proposal and re-validated, not trusted.
   - Verify: The KB result is stated: found, stale or none.
2. Native alternative check: can this be done with the platform (a built-in module, ES features, CSS)? If yes, propose that instead and stop here.
   - Gate: Native first. Do not install while a native route exists.
   - Verify: Why native code cannot do it, or the native route is chosen.
3. If a library is unavoidable, write the proposal with three facts: weight (unpacked size), activity (last release or commit), need (what native code cannot do).
   - Verify: Three facts, each with a number or a date.
4. Name a smaller alternative: a micro-library with no dependencies, or a small copy of the one function you need, next to the standard choice.
   - Verify: At least one smaller option is named with its size.
5. Ask for approval: open_decision with the options (native, the library, the smaller one) and a recommendation. The default answer is no.
   - Gate: Do not add the dependency until a person has answered.
   - Verify: A decision is open or answered.
6. After approval and install, record what you learned about it (its version, the behaviours you rely on, gotchas) with write_kb and a stale_after_days of 90, so the next session does not research it again.
   - Verify: A KB article exists with the version and the window.

## Output

## Dependency proposal: <package>
**Native alternative:** ... (why it is not enough)
**Weight:** unpacked size
**Activity:** last release or commit
**Need:** what native code cannot do
**Smaller alternative:** name and size
**Recommendation:** native / library / smaller (default no)
