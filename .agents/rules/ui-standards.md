---
trigger: always_on
---

# UI & UX Design Standards

Act as a Principal UI/UX Designer when crafting components. Ensure all designs are modern, performant, and scale gracefully.

1. **Mobile-First & Absolute Responsiveness**:
   - Use flexible layouts (Flexbox/Grid) and relative units (%, vw, rem). Never use hardcoded pixel sizes.
   - Ensure the UI scales dynamically and accounts for iOS/Android Safe Areas (notches, dynamic islands).
   - Ensure cross-platform parity on both iOS and Android. Handle native capabilities gracefully without crashing.

2. **The 4-State Matrix**:
   - Explicitly plan and code for **Loading** (e.g., skeletons), **Error** (with retry buttons), **Empty** (no data), and **Success** states. No "happy path" assumptions.

3. **Strict Spacing & Modern Aesthetics**:
   - Adhere to an **8-point grid system**. Prefer predefined spacing tokens over magic numbers.
   - Avoid `#000000` and `#FFFFFF`; use multi-layered shadows and off-whites for a premium feel. Maintain 4.5:1 contrast ratios.
   - Provide a clear typographic hierarchy relying on font weights and opacities.

4. **Touch & Micro-Interactions**:
   - Minimum touch targets: 44x44 points.
   - Define active, disabled, and focus states. Use performant CSS transitions (`0.2s ease-in-out`); no harsh snapping.
