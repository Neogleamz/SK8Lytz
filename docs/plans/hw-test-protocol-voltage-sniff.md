# Plan: `hw-test/protocol-voltage-sniff`

### Design Decisions & Rationale
**Discovery Mode** — The Zengge protocol does not publicly document battery voltage telemetry. We will use the LED Diagnostic Lab's raw payload inspector to systematically log every 0x63 response packet from multiple hardware units across varying charge states to identify any consistent byte offsets that correlate to voltage/SoC data. This is a **research task**, not a production feature.

---

## Proposed Changes

### [MODIFY] `src/components/Sk8LytzDiagnosticLab.tsx`
- Add a dedicated "Voltage Sniffer" panel to the Lab (hidden behind `__DEV__` flag).
- Panel fires a 0x63 query every 30 seconds and logs the full raw response as a hex dump to the Timeline.
- Highlight bytes [10–15] specifically as "monitoring zone" since bytes 0–9 are confirmed by the Master Reference.

### [NEW] Research Log: `docs/research/voltage-sniff-findings.md`
- Created as a living document to record observed byte offsets and their correlations to charge states.

---

## Open Questions
- **Q:** Do we have access to a hardware unit at multiple discrete charge levels (100%, 50%, 20%) to capture a differential reading?
- **Q:** Has the Zengge community (GitHub, forums) documented any reverse-engineered SoC bytes?

## Verification Plan
This is a hardware research task. Success = identifying at least 1 byte offset that changes correlatively with known battery charge level.
