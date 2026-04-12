# [PLAN] style/tokenized-spacing-standard (The 8pt Grid)

### Design Decisions & Rationale
To ensure "Visual Rhythm" and eliminate magic numbers, we are enforcing a strict **8pt Grid System**. This aligns SK8Lytz with Apple and Google design standards, ensuring that all padding, margins, and heights are mathematically consistent across all device resolutions.

## Proposed Changes

### [Component Name] Design Tokens

#### [NEW] [constants/Spacing.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/constants/Spacing.ts)
- Define the grid:
    - `XS: 4` (Micro alignment)
    - `S: 8`
    - `M: 16`
    - `L: 24`
    - `XL: 32`
    - `XXL: 48`

#### [MODIFY] [Styles Audit]
- Search and replace all magic pixel values (e.g., `margin: 10`, `paddingTop: 15`) with their nearest 8pt token equivalent.

## Verification Plan
1. **Visual Regression**: Compare current screens with the tokenized version; verify that the "rhythm" feels more professional and alignment is cleaner.
2. **Lint Enforcement**: Explore `eslint-plugin-react-native` rules to forbid literal number values in style objects.
3. **Cross-Device Scale**: Verify the grid looks consistent on both a 5.5" iPhone SE and a 6.7" Pro Max.
