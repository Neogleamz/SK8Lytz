# ADR-0001: The Symbiosis of Session Log and Architecture Decision Records (ADRs)

**Status:** Accepted
**Date:** 2026-06-06
**Author:** 🎯 PM — Jordan / 🏛️ Arch — Morgan

## 1. Context
The project heavily relies on `tools/SESSION_LOG.md` to track conversational memory, daily findings, and immediate decisions. However, as the application scales, relying solely on a chronological log causes critical architectural context to be buried beneath daily chat records. When new agents or developers attempt to understand *why* a specific technology or pattern was chosen (e.g., Supabase vs Firebase, XState vs Redux), they must grep through massive chat logs to find the reasoning.

We need a system to surface permanent, immutable technical decisions without discarding the speed and conversational utility of the `SESSION_LOG.md`.

## 2. Decision
We will run a symbiotic dual-logging system:
1. **`SESSION_LOG.md` (The Live Feed)**: Remains the conversational memory layer. Agents will continue to write `[DECISION]` blocks here during active sessions for speed and immediate context-sharing.
2. **`docs/ADR/` (The Vault)**: Becomes the permanent architecture repository. Whenever an agent writes a `[DECISION]` to the Session Log that introduces a new dependency, alters a core workflow, or establishes a new project-wide standard, the agent is **mandated by Rule 11** to formally draft and commit an ADR using the `0000-template.md`.

## 3. Rejected Alternatives
- **Replacing Session Log with ADRs**: Rejected because drafting a formal ADR for every minor daily decision creates extreme friction and slows down development loops.
- **Using Git Commit Messages for Decisions**: Rejected because commit messages are hard to search for high-level architectural constraints and are inherently tied to code lines rather than project philosophy.

## 4. Consequences
- **Positive:** We achieve an "Impenetrable Engineering Fortress". New agents can read the ADR directory to instantly absorb the project's technological boundaries without hallucinating or reverting to industry defaults.
- **Negative:** Adds a slight overhead to the agent's workflows during major architectural shifts (requires creating a new file).
- **Neutral:** Requires strict adherence to `agent-behavior.md` Rule 11 to enforce that the ADR is actually written when triggered.
