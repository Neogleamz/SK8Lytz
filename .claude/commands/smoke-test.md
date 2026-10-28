---
description: "Use after a UI change or before a release to walk every screen the app lists, in the browser pane, and prove each one mounted with no console errors, with a screenshot as evidence."
argument-hint: "[one screen id, optional]"
---
<!-- generated from HQ playbook smoke-test v1, do not edit. Run `npm run gen:playbooks`. -->

Arguments: $ARGUMENTS

# Smoke test: is every screen alive

**Role:** reviewer (the HQ persona; a Claude Code subagent of that name does the same job in isolation). **When to use:** Use after a UI change or before a release to walk every screen the app lists, in the browser pane, and prove each one mounted with no console errors, with a screenshot as evidence.

## Steps
1. Build first: the repo's build command (get_repo, build_cmd). A build that fails is the finding; stop and debug.
   - Gate: Do not walk the screens on a broken build.
   - Verify: The build passes.
2. Start the dev server with preview_start (never a shell command) and open the app in the browser pane. List the screens from the app's navigation file (HQ: public/js/nav.js) so none is missed.
   - Verify: The screen list is written down with its count.
3. For each screen: navigate to it, wait for it to render, read the page (read_page or get_page_text) and confirm the view mounted with its real content, then read_console_messages for errors. Skip no screen because it 'looks the same'.
   - Verify: Per screen: mounted yes or no, console errors yes or no.
4. Take one screenshot per screen (or one per failing screen and a sample of the rest) as the evidence.
   - Verify: The screenshots are attached or named.
5. Summarise in the output table. If any screen white-screens, shows an error overlay or logs a console error, say so first, run the debug playbook, and stop any commit or release.
   - Gate: A failed screen stops the commit and the release.
   - Verify: The verdict and the failing screens, if any, are at the top.

## Output

## Smoke test
| Screen | Mounted | Console errors | Screenshot |
|---|---|---|---|
| <screen> | yes / no | none / <text> | <file> |

**Verdict: ALIVE or BROKEN** (the failing screens first)
