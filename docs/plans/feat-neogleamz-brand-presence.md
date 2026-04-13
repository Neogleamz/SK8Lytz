# Plan: `feat/neogleamz-brand-presence`

### Design Decisions & Rationale

The parent brand "Neogleamz" needs stronger visibility in the app — particularly on the Auth screen (first impression) and Account section (brand identity anchor). We add "by Neogleamz" wordmark treatments and ensure the App Store identity (icon, name, description) aligns with the parent brand. The exact placement and treatment must be decided before execution — this plan surfaces the options.

---

## ⚠️ User Review Required

> [!IMPORTANT]
> Design direction is TBD per the Bucket List note. Before executing, confirm:
>
> 1. Wordmark placement (Auth screen watermark? Header? Splash screen?)
> 2. Typography treatment ("SK8Lytz by Neogleamz" or standalone Neogleamz logo?)
> 3. App Store metadata changes (name change? subtitle?)

---

## Proposed Changes (Pending Design Direction)

### [MODIFY] `src/screens/AuthScreen.tsx`

- Option A: Add a small "by neogleamz.com" attribution below the SK8Lytz logo (per the existing Master Reference "Tucked-in Attribution" pattern).
- Option B: Replace the auth background with a Neogleamz-branded header section.

### [MODIFY] App Store metadata (`app.json`)

- Update `expo.description` and App Store subtitle to include "by Neogleamz."

### [OPTIONAL] Splash Screen

- Update `assets/splash.png` to include the Neogleamz wordmark beneath the SK8Lytz logo.

---

## Open Questions

- **Q:** What is the exact wordmark / logo file for Neogleamz? (SVG or PNG at 2x/3x needed.)
- **Q:** Should App Store listings be updated as part of this, or just the in-app UI?
- **Q:** Is "Neogleamz" styled differently (e.g. different font, color) from the SK8Lytz wordmark?

## Verification Plan

1. After design assets confirmed: verify wordmark renders at correct scale on both small (SE-size) and large devices.
2. Confirm no Master Reference "Tucked-in Attribution" spacing specs are violated.
