# Agentic Velocity System — Turbo Flows, Tool Playbook & Self-Learning Loop

## Design Decisions & Rationale

This initiative has three mutually reinforcing components. Turbo Flows eliminate repetitive manual approval friction. The Tool Playbook eliminates "amnesia" around hard-won, specific tool syntaxes. The Self-Learning Meta-Evolution enhancement closes the loop — ensuring every pain point discovered in a session automatically improves the next one. These are implemented as agent rules + workflow macros so they are portable and version-controlled inside the codebase itself.

---

## Part 1: Turbo Flow Macro Library

**New workflow files** added to `.agents/workflows/`. Each is annotated `// turbo-all` for zero-friction execution.

### [NEW] `/nuke-cache` — nuke-cache.md

Kills the Metro bundler, wipes all caches (Metro, Watchman, Gradle), and restarts clean.

### [NEW] `/db-sync` — db-sync.md

Pulls the latest Supabase schema, generates TypeScript types, and injects them into `src/types/supabase.ts`.

### [NEW] `/health-sweep` — health-sweep.md

Runs the full tech debt audit: `npm audit`, TODO/FIXME hunt, architectural smell scan (files > 30KB), and injects findings into the Bucket List.

### [NEW] `/git-snapshot` — git-snapshot.md

The "save point" macro: stages all changes, creates a semantic commit checkpoint, and displays the current git log.

### [NEW] `/tsc-check` — tsc-check.md

Runs the TypeScript compiler in `--noEmit` mode and reports all errors in a clean, scannable summary.

---

## Part 2: Tool Playbook Registry

### [NEW] tool-playbook.md

A mandated lookup table: Before writing any complex terminal command or search query, I must check this playbook first.

Entries are organized by "Intent Category":

- **File Discovery**: Monolith scanning, hook count audits, TODO hunts
- **Git Operations**: Branch creation, semantic commits, diff review
- **BLE / Hardware**: ADB commands, APK install paths, log extraction
- **Database**: Supabase type generation, RLS policy inspection
- **Build System**: Gradle clean, Expo start flags, TSC invocation syntax

---

## Part 3: Enhanced Meta-Evolution (Self-Learning Loop)

### [MODIFY] meta-evolution.md

Add Step 5 - The Victory Snapshot: After any task where we spent significant effort figuring out the right sequence of tool calls, I must proactively offer to save it to the Tool Playbook permanently.
