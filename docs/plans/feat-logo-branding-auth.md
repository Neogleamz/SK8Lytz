# Feature: SK8Lytz Logo & Brand Integration

This plan implements the requested branding update to integrate the SK8Lytz logo and "Glow your way." slogan onto the primary Auth/Welcome screen, replacing the default MaterialCommunity Icon.

## Design Decisions & Rationale

- **Native Image Tinting (Dark Mode):** Rather than manually generating and linking a secondary `logo-white.png` file, we will utilize React Native's `Image` style `tintColor` property. When the app is in Dark Mode, we will apply a `#FFFFFF` tint to the existing `assets/logo.png`. This is highly performant and keeps our asset footprint smaller (Dependency Diet protocol).
- **Brand Slogan:** We will update the primary greeting text to prominently display the "Glow your way." slogan, ensuring the application feels premium and on-brand at first launch.

### UI & Platform Strategy

- The logo image will be rendered using a relative width (`width: '80%'`, `maxWidth: 300`) with `resizeMode="contain"` to ensure it scales perfectly across different mobile screen sizes without clipping or distortion (Responsive UI constraint).
- It will remain centered using the Flexbox container properties already established.

## Proposed Changes

---

### UI Component Updates

#### [MODIFY] [AuthScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/AuthScreen.tsx)

- Import `Image` from `react-native`.
- Remove the `MaterialCommunityIcons` `roller-skate` component in the `headerContainer`.
- Inject the local asset `<Image source={require('../../assets/logo.png')} />`.
- Apply a dynamic `tintColor` property: `isDark ? '#FFFFFF' : undefined` (or the default primary blue).
- Update the title and subtitle strings to explicitly state "Glow your way." alongside the existing context.

## Verification Plan

### Manual Verification

1. I will boot the Dev Server locally.
2. I will open the app in Web Mode and visually verify that `logo.png` renders at the correct dimensions.
3. I will test the Dark Mode / Light Mode toggle in the top-right corner to ensure the `tintColor` successfully flips the logo from original blue to pure white.
4. I will share a screenshot of the results for your final approval.
