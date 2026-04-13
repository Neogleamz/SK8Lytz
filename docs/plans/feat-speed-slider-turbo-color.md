# Dynamic Speed & Brightness Sliders (feat/speed-slider-turbo-color)

### Design Decisions & Rationale

Since both of your dynamic slider requests (Turbo Red for Speed and Dim-to-Bright for Brightness) physically target the exact same `TacticalSlider` component, I will consolidate both bucket list items into this single branch execution for efficiency. I will augment the base `TacticalSlider` with a new internal calculation engine that bypasses the React Native re-render loop, calculating the dynamic interpolation directly against the active PanResponder coordinates so it renders at 60FPS while dragging.

### Proposed Changes

#### `src/components/TacticalSlider.tsx`

- **Component Props**: Introduce `dynamicMode?: 'TURBO' | 'BRIGHTNESS'` to the `TacticalSliderProps`.
- **Dynamic Color Engine**:
  - `TURBO`: As the slider drags from 0 to 100%, the active fill color will dynamically transition from pure white `rgb(255,255,255)` at 0% to bright red `rgb(255,0,0)` at 100%.
  - `BRIGHTNESS`: The active fill background color will interpolate opacity, starting at a visually dim `rgba(255,255,255, 0.2)` at 0% and peaking at brilliant `rgba(255,255,255, 1.0)` at 100%.
- **80% Target Marker**: Since the components are rendered absolutely, I will inject a distinct vertical vector line (with a slight drop shadow) permanently fixed at `left: '80%'` inside the slider track when `dynamicMode === 'BRIGHTNESS'`. It will serve as the "High Output" watermark.

#### `src/components/DockedController.tsx`

- **MODIFY**: Inject the `dynamicMode="TURBO"` prop into the SPEED `TacticalSlider`.
- **MODIFY**: Inject the `dynamicMode="BRIGHTNESS"` prop into the BRIGHTNESS `TacticalSlider`.

> [!IMPORTANT]
> **User Review Required**
> Since I bundled the Brightness marker idea into this plan along with the Turbo feature, review the proposed dimness & 80% watermark logic. Reply with **"proceed"** to authorize coding, or tweak the plan!
