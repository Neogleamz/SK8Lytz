# Plan: `tune-visualizer-pro-effects`

### Design Decisions & Rationale
The ProductVisualizer renders 33 Pro Effects patterns using interpolated mathematical models of the hardware behavior. The models are approximations and some patterns drift visually from the hardware's actual output. This is a pure tuning exercise — no protocol changes — comparing visualizer output side-by-side with the physical hardware for each of the 33 `transMode` IDs (0x01–0x21).

---

## Proposed Changes

### [MODIFY] `src/components/ProductVisualizer.tsx`
- For each of the 33 effect IDs, review the corresponding `renderEffect_XX()` (or equivalent animation logic).
- Adjust: timing curves, color blend modes, loop durations, and LED activation masks to match physical hardware output.
- Add a `__DEV__`-gated "Accuracy Mode" button that renders a side-by-side reference hex dump of what the hardware should be showing.

---

## Execution Approach
This task should be done in phases:
1. **Phase 1:** Tabulate all 33 effects and rate current visual accuracy (Good / Off / Broken).
2. **Phase 2:** Fix all "Broken" effects first (completely wrong behavior).
3. **Phase 3:** Fine-tune "Off" effects (correct concept, wrong timing/color).
4. **Phase 4:** Confirm "Good" effects are stable.

---

## Open Questions
- **Q:** Is there a reference video of all 33 effects from the Zengge hardware that we can use as ground truth?
- **Q:** Are there any effects that are fundamentally impossible to replicate in software (e.g. effects that require the hardware's internal microphone amplitude data)?

## Verification Plan
- After each effect is tuned, record a side-by-side screen + hardware comparison video.
- Target: all 33 effects visually match hardware output within ~1 frame.
