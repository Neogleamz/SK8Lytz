# Setup Wizard Button Overlap Fix

The user reported that the "Complete Setup" button overlaps the device configuration cards on the "Name Your Skates" page (Step 3), making it impossible to change the LED count for the bottom device.

## Design Decisions & Rationale

The root cause is that `renderStep3` in `HardwareSetupWizardScreen.tsx` wraps the list of devices in a standard `<View>` instead of a `<ScrollView>`. Because the footer containing the "Complete Setup" button is rendered as a flex layer at the bottom of the screen, the static device list overflows linearly and gets trapped underneath the button container. By converting the `scrollViewWrapper` from a `<View>` to a `<ScrollView>` with adequate `paddingBottom`, the user will be able to fully scroll the last device card above the button footprint.

## Proposed Changes

### UI / Component Fix

#### [MODIFY] `HardwareSetupWizardScreen.tsx`(file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)

- Inside the `renderStep3` function, replace `<View style={styles.scrollViewWrapper}>` around line 235 with `<ScrollView style={styles.scrollViewWrapper} contentContainerStyle={{ paddingBottom: 40 }} showsVerticalScrollIndicator={false}>`.
- Replace the closing `</View>` tag at the bottom of `renderStep3` with `</ScrollView>`.

## Open Questions

None. This is a targeted UI alignment fix.

## Verification Plan

### Manual Verification

- Deploy to the phone.
- Proceed to Step 3 of the Hardware Setup Wizard with multiple devices selected.
- Scroll to the bottom and verify the LED Count adjustment buttons are fully accessible above the "Complete Setup" action area.
