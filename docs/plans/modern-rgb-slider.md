# High-Precision RGB UI Component - Design Choices

Per the `epic/ui-refinement` Epic, our objective is to replace the standard, uninspired RGB sliders in the `DockedController` with a premium, high-precision component that guarantees a stunning visual experience on mobile.

Before we lock in the code, I have conceptualized **three distinct design choices** for the new interface. 

Please review and reply with your choice (e.g. "I pick Choice 3") or any modifications.

````carousel
![Choice 1: Color Wheel](/C:/Users/Magma/.gemini/antigravity/brain/39ad188a-38fa-400e-9f7b-11988c9e263a/rgb_slider_choice_1_wheel_1775804657143.png)
<!-- slide -->
![Choice 2: Strip Faders](/C:/Users/Magma/.gemini/antigravity/brain/39ad188a-38fa-400e-9f7b-11988c9e263a/rgb_slider_choice_2_strip_1775804669848.png)
<!-- slide -->
![Choice 3: Glassmorphism Ribbons](/C:/Users/Magma/.gemini/antigravity/brain/39ad188a-38fa-400e-9f7b-11988c9e263a/rgb_slider_choice_3_ribbons_1775804683918.png)
````

---

### Choice 1: The Conic Color Wheel + Luminance Arc
**The Concept:** A sleek, circular touch-wheel using a full conic gradient. The user drags a glowing reticle around the circle to select the precise **Hue** and **Saturation** simultaneously. Below the wheel, a curved, arc-shaped slider controls the Brightness/Value.
*   **Pros:** Deeply intuitive, instantly recognizable, minimal vertical screen real estate, ultra-modern aesthetic.
*   **Cons:** Can be slightly tricky for users trying to hit an *exact* mathematical RGB value, as it operates radially.

### Choice 2: The Neon Touch-Strip (Pro Lighting Console Style)
**The Concept:** A thick, horizontal "neon rainbow" touch-strip (like a DJ controller or professional lighting console) to select the base **Hue**. Flanking it below are two vertical fader switches: one for washing the color out (Saturation), and one for dimming (Brightness).
*   **Pros:** Feels very tactile and professional, separating hue selection from tuning for extreme precision.
*   **Cons:** Re-orienting the user from RGB to HSL-based thinking; vertical sliders may crowd the already tight docked controller UI if not scaled perfectly.

### Choice 3: Tri-Channel Glassmorphism Fluid Ribbons
**The Concept:** We keep the absolute precision of individual Red, Green, and Blue channels, but completely reimagine the UI. Instead of standard thin sliders, we use three thick, frosted-glass "ribbon" sliders. The revolutionary part: the background gradient of each slider *dynamically reacts* to the other two colors. (e.g., If Green and Blue are at 100%, the Red slider's track shows a live gradient from Cyan to White).
*   **Pros:** Maximum mathematical control (retains raw RGB output natively for hardware), incredible visual flair, highly responsive, and keeps users grounded in the RGB additive color model they are used to in SK8Lytz.
*   **Cons:** Requires slightly more React rendering overhead to constantly recalculate the live gradients on slide (though easily optimized with `useMemo`).

---

## Technical Approach
Once you select a choice:
1. I will build an isolated, reusable `<ModernColorPicker />` component.
2. I will wire it to output standard `{ r, g, b }` objects to prevent breaking the `applyFixedPattern` pipelines.
3. I will replace the raw `Slider` components in the `DockedController.tsx` (around lines 1900-2000) with the new module.

> [!IMPORTANT]
> **User Review Required**
> Which of the three design choices do you want me to implement? Type your choice below, and I will proceed with building the UI.
